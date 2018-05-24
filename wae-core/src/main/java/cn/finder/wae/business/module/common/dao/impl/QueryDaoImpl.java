package cn.finder.wae.business.module.common.dao.impl;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.mongodb.core.MongoTemplate;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.ShowTableConfig;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.module.common.dao.QueryDao;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.common.aspect.logger.ArchType;
import cn.finder.wae.common.aspect.logger.FinderLogger;
import cn.finder.wae.common.comm.Reflection;
import cn.finder.wae.common.db.BaseJdbcDaoSupport;
import cn.finder.wae.common.db.DBType;
import cn.finder.wae.common.db.ds.DSManager;
import cn.finder.wae.common.exception.WaeRuntimeException;
import cn.finder.wae.queryer.Queryer;
import cn.finder.wae.queryer.common.nosql.MongoDBQueryer;

public class QueryDaoImpl extends BaseJdbcDaoSupport implements QueryDao {

	@FinderLogger(archType=ArchType.FINDER_ARCH)
	@Override
	public TableQueryResult queryTableQueryResult(long showTableConfigId,
			QueryCondition<Object[]> condition) {
		
		
		
		logger.debug("=========QueryDaoImpl :");
		logger.debug("=========showTableConfigId :" +showTableConfigId);
		
		ShowTableConfig showTableConfig=ArchCache.getInstance().getShowTableConfigCache().get(showTableConfigId);
		if(showTableConfig!=null){
			logger.debug("=========处理操作名称 :" +showTableConfig.getName());
		}
		
		if(condition!=null){
			logger.debug("==Sql:"+condition.getSql());
			logger.debug("==WhereCluster:"+condition.getWhereCluster());
			logger.debug("==WherepParameterValues:"+condition.getWherepParameterValues()==null ? null:StringUtils.join(condition.getWherepParameterValues(),","));
			logger.debug("==pageIndex:"+condition.getPageIndex());
			logger.debug("==pageSize:"+condition.getPageSize());
		}
		
		Reflection reflect = new Reflection();
		
		String queryClass=showTableConfig.getProcessCommand();
		
		if (queryClass == null || queryClass.equals("")) {
			queryClass = "cn.finder.wae.queryer.common.CommonQuery";
		}
		Queryer queryer=null;
		try {
			//动态创建 不会有数据源并发问题
			queryer = (Queryer) reflect.newInstance(queryClass,
					new Object[] {});
		} catch (Exception e) {
			throw new WaeRuntimeException(e.toString());
		}
		
		//设置数据源
		//DefaultContextHolder.setDataSourceType(showTableConfig.getTargetDs());
		
		//BaseJdbcDaoSupport baseJdbcDaoSupport = (BaseJdbcDaoSupport)queryer;
		
		DataSource ds = DSManager.getDataSource(showTableConfig.getTargetDs());
		
		if(ds!=null){
			BaseJdbcDaoSupport baseJdbcDaoSupport = (BaseJdbcDaoSupport)queryer;
			BasicDataSource basicDataSource = (BasicDataSource)ds;
			String driverClassName = basicDataSource.getDriverClassName();
			//判断数据库类型
			if(driverClassName.toLowerCase().indexOf("mysql")!=-1){
				baseJdbcDaoSupport.setDbType(DBType.MySql);
			}else if(driverClassName.toLowerCase().indexOf("sqlserver")!=-1){
				
				baseJdbcDaoSupport.setDbType(DBType.SqlServer);
			}
			else if(driverClassName.toLowerCase().indexOf("oracle")!=-1){
				
				baseJdbcDaoSupport.setDbType(DBType.Oracle);
			}
			
			//queryer.setJDBCDataSource(getDataSource());
			queryer.setJDBCDataSource(ds);
		}
		else{
			
			MongoTemplate mongoTemplate = DSManager.getMongoDataSource(showTableConfig.getTargetDs());
			MongoDBQueryer mongoQueryer=(MongoDBQueryer)queryer;
			mongoQueryer.setDataSource(mongoTemplate);
			
		}
		
		
		TableQueryResult result= queryer.queryTableQueryResultManager(showTableConfigId, condition);
		//DefaultContextHolder.clearDataSourceType();
		return result;
	}

}
