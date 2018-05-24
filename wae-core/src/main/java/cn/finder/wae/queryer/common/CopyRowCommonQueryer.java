package cn.finder.wae.queryer.common;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.CallableStatementCallback;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.ShowDataConfig;
import cn.finder.wae.business.domain.ShowTableConfig;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.dto.DeleteQueryConditionDto;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.common.aspect.logger.ArchType;
import cn.finder.wae.common.aspect.logger.FinderLogger;

public class CopyRowCommonQueryer extends  BaseCommonDBQueryer{

	
	private final static Logger logger = Logger.getLogger(DeleteCommonQueryer.class);
	@FinderLogger(archType=ArchType.FINDER_ARCH)
	@Override
	public TableQueryResult queryTableQueryResult(long showTableConfigId,
			QueryCondition<Object[]> condition) {
		
		logger.debug("**********=====go into CopyRowCommonQueryer======");
		
		logger.debug("==showTableConfigId:"+showTableConfigId);
		final Map<String, Object> para=((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
		//从缓存中获取 ShowTableConfig 配置
		ShowTableConfig showTableConfig = ArchCache.getInstance().getShowTableConfigCache().get(showTableConfigId);
		//ShowDataConfig showDataConfig =ArchCache.getInstance().getShowTableConfigCache().getPKShowDataConfig(showTableConfigId);
		//final DeleteQueryConditionDto dto= (DeleteQueryConditionDto)condition;
		//logger.debug("==ids:"+dto.getIds());
		final List<ShowDataConfig> showDataConfigs =showTableConfig.getShowDataConfigs();
		final List<String> filedNames=new ArrayList<String>();
		final List<String> sel_FiledNames=new ArrayList<String>();
		String tablename="";
		for(ShowDataConfig df:showDataConfigs)
		{
			if(df.getIsPrimaryKey()==1){
				sel_FiledNames.add("(select max( "+df.getFieldName()+") + 1 AS "+ df.getFieldName() +"  from "+ df.getFieldTableName() +" ) as "+df.getFieldName());
			}
			else{
				sel_FiledNames.add(df.getFieldName());
			}
	    	filedNames.add(df.getFieldName());
	    	if(tablename.equals(""))
	    		tablename=df.getFieldTableName();
		}
		StringBuffer fields_sb =new StringBuffer();
		StringBuffer sel_fields_sb =new StringBuffer();
		fields_sb.append(StringUtils.join(filedNames, ','));
		sel_fields_sb.append(StringUtils.join(sel_FiledNames, ','));
		StringBuffer sql_sb = new StringBuffer();
		sql_sb.append("insert into ").append(tablename).append(" ( "+fields_sb+" ) ");
		sql_sb.append(" select ").append(sel_fields_sb+" ").append(" from ").append(tablename).append(" where id = ?");
		logger.debug("==sql_sb:"+sql_sb.toString());
		Object oids=para.get("recordIds");
		final String[] ids=(String[])(oids);
		final int size = ids.length;		
		BatchPreparedStatementSetter batchSetter = new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				// TODO Auto-generated method stub
				ps.setObject(1, ids[i]);
			}
			@Override
			public int getBatchSize() {
				// TODO Auto-generated method stub
				return size;
			}
		};
		getJdbcTemplate().batchUpdate(sql_sb.toString(),batchSetter);
		//final TableQueryResult qr =new TableQueryResult();
		logger.debug("**********************=====end of CopyRowCommonQueryer======");
		return null;
	}

	public void setJDBCDataSource(DataSource dataSource) {
		super.setJdbcDataSource(dataSource);
	}

}