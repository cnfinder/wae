package cn.finder.wae.queryer.common;

import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.ShowTableConfig;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.common.db.BaseJdbcDaoSupport;
import cn.finder.wae.common.db.ds.DSManager;


/***
 * 前台创建试图
 * @author wu hualong
 * 输入参数
 *  1. 数据源ID
 *  2. SQL语句
 *  
 */
public class CreateViewQueryer extends  BaseCommonDBQueryer{

	
private final static Logger logger = Logger.getLogger(CreateViewQueryer.class);
	
	@Override
	public TableQueryResult queryTableQueryResult(long showTableConfigId,
			QueryCondition<Object[]> condition){
		
		logger.debug("=====go into CreateViewQueryer======");
		
		logger.debug("==showTableConfigId:"+showTableConfigId);
		
		if(condition!=null)
		{
			logger.debug("==Sql:"+condition.getSql());
			logger.debug("==WhereCluster:"+condition.getWhereCluster());
			logger.debug("==WherepParameterValues:"+condition.getWherepParameterValues());
			logger.debug("==pageIndex:"+condition.getPageIndex());
			logger.debug("==pageSize:"+condition.getPageSize());
		}
		
		final TableQueryResult qr =new TableQueryResult();
		
		
		
		//从缓存中获取 ShowTableConfig 配置
		ShowTableConfig showTableConfig = ArchCache.getInstance().getShowTableConfigCache().get(showTableConfigId);
		
	    
		MapParaQueryConditionDto<String, Object> dto = (MapParaQueryConditionDto<String, Object>)condition;
		
		Map<String, Object> params = dto.getMapParas();
		
		String sql_str = (String)params.get("sql_str");
		long dsKey = Long.parseLong((String)params.get("target_ds"));
		
		CreateSql createSql =new CreateSql(sql_str);
		createSql.setDataSource(DSManager.getDataSource(dsKey));
		createSql.create();
		
		qr.setCount(0l);
			
		 return qr;
		 
		
	}
	
	

	public void setJDBCDataSource(DataSource dataSource) {
		super.setJdbcDataSource(dataSource);
	}

	
	
	class CreateSql extends BaseJdbcDaoSupport{
		
		
		
		private String sql;
		public CreateSql(String sql){
			this.sql = sql;
			
		}
		
		
		public void create(){
			
			getJdbcTemplate().execute(sql);
		}
	}
}
