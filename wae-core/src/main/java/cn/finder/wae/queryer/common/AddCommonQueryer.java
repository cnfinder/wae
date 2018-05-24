package cn.finder.wae.queryer.common;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.ShowDataConfig;
import cn.finder.wae.business.domain.ShowTableConfig;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.cache.ConstantsCache;
import cn.finder.wae.cache.ArchCache;


/***
 * 看做DAO层  因为这样处理数据源容易
 * @author wu hualong
 * 加载添加页面控件数据
 *
 */
public class AddCommonQueryer extends  BaseCommonDBQueryer{

	
private final static Logger logger = Logger.getLogger(AddCommonQueryer.class);
	
	@Override
	public TableQueryResult queryTableQueryResult(long showTableConfigId,
			QueryCondition<Object[]> condition){
		
		@SuppressWarnings("unchecked")
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
		Long addConfigId = Long.valueOf(String.valueOf(data.get("showtableConfigId")));
		ShowTableConfig showTableConfig = ArchCache.getInstance().getShowTableConfigCache().get(addConfigId);
		
		String sql = "insert into " + data.get("table001abcX") + "(";
		String valueSql = "";
		List<Object> values = new ArrayList<Object>();
		List<ShowDataConfig> showDataConfigs = showTableConfig.getShowDataConfigs();
		
		for(ShowDataConfig showDataConfig:showDataConfigs){
			String fieldName = showDataConfig.getFieldName();
			String[] strs = fieldName.split("\\.");                     //删除表名前缀
			fieldName = strs[strs.length-1];
			Object value = data.get(fieldName);
			//系统日期类型控件，赋于系统当前日期
			if(showDataConfig.getShowType() == ConstantsCache.ControlType.CONTROLTYPE_SYSTEM_DATE){
				sql += fieldName+", ";
				valueSql += " ?, " ;
				values.add(new Timestamp(System.currentTimeMillis()));
				continue;
			}
			if(value != null && !value.equals("")&& showDataConfig.getShowType() != ConstantsCache.ControlType.CONTROLTYPE_PWD_CONFIRM){ //33不是需要插入到数据库的类型
//				if(value.equals("")){
//					if(showDataConfig.getDataType() != 11){
//						value = showDataConfig.getDefaultValue();
//					}
//				}
				sql += fieldName+", ";
				valueSql += " ?, " ;
				values.add(value);
			}
		}
//		sql += " create_date)" ;//添加创建日期
//		valueSql += "?";
//		values.add(new Timestamp(System.currentTimeMillis()));
		sql = sql.substring(0,sql.lastIndexOf(","));     //删除最后一个","字符
		valueSql = valueSql.substring(0,valueSql.lastIndexOf(","));
		sql += ") values(" + valueSql + ")";
		getJdbcTemplate().update(sql, values.toArray());
		return null;
	}
	
	

	public void setJDBCDataSource(DataSource dataSource) {
		super.setJdbcDataSource(dataSource);
	}

}
