package cn.finder.wae.queryer.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.domain.report.TReport;
import cn.finder.wae.business.domain.report.TReportField;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.business.rowmapper.RowMapperFactory;

public class CommonReportDataImportQueryer extends BaseCommonDBQueryer{

	@Override
	public void setJDBCDataSource(DataSource dataSource) {
		super.setJdbcDataSource(dataSource);
		
	}

	@Override
	public TableQueryResult queryTableQueryResult(long showTableConfigId,
			QueryCondition<Object[]> condition) {
		/*
		 * 1.根据reportId获取report的列属性
		 * 2.对于每条记录在report_row表中插入一条记录，返回row 的id
		 * 3.根据reportId查询列属性配置
		 * 4.根据列配置中的filed_code从源数据结果中查询值，插入到report_value表中
		 */
		@SuppressWarnings("unchecked")
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
		TableQueryResult queryResult = (TableQueryResult)data.get("queryResult");
		List<Map<String, Object>> sResult = queryResult.getResultList();
		
		TReport report = (TReport) data.get("report");
		final int report_id = report.getId();
		
		 //获取约束字段
		String limit = report.getLimitField();
	
		for (Map<String, Object> map : sResult) {
			//如果该条记录创建时间小于报表上次统计I时间，就跳过
			if(!limitCheck(report.getLastUpdate(), map.get(limit))) continue;
			KeyHolder keyHolder = new GeneratedKeyHolder();
			getJdbcTemplate().update(
					new PreparedStatementCreator() {
						public PreparedStatement createPreparedStatement(
								Connection con) throws SQLException {
							String sqlString = "insert into t_report_row(report_id) values (?) ";
							PreparedStatement ps = con
									.prepareStatement(
											sqlString,
											Statement.RETURN_GENERATED_KEYS);
							ps.setObject(1, report_id);
							return ps;
						}
					}, keyHolder);

			int rowId = keyHolder.getKey().intValue();
			
			String fieldSql = " select * from t_report_field where report_id = "+report_id;
			List<TReportField> fieldList = getJdbcTemplate().query(fieldSql, new RowMapperFactory.ReportFieldRowMapper());
			for(TReportField reportField: fieldList){
				String contentSql = " insert into t_report_value(row_id, field_id,value) values (?, ?, ?)	";
				Object[] para = {
						rowId,
						reportField.getId(),
						map.get(reportField.getFieldCode())
				};
				getJdbcTemplate().update(contentSql, para);
			}
		}
		return null;
	}
	
	
	//限制判断方法
	public boolean limitCheck(Date lastUpdateDate, Object value){
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
		
		try {
			Date time1 = lastUpdateDate;
			Date time2 = formatDate.parse(value.toString());
			
			boolean flat = time1.before(time2);
			
			return flat;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
