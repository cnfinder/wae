package cn.finder.wae.common.aspect;


import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;

import cn.finder.wae.business.domain.ShowTableConfig;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.common.db.BaseJdbcDaoSupport;
import cn.finder.wae.common.db.DBType;
import cn.finder.wae.common.db.ds.DSManager;
import cn.finder.wae.common.exception.WaeRuntimeException;


public class MultiDataSourceAspect {
	private final static Logger logger=Logger.getLogger(MultiDataSourceAspect.class);
	
	
	public Object doSetDataSource(ProceedingJoinPoint joinPoint) throws Throwable {
		logger.debug(" ===== go into MultiDataSourceAspect.doSetDataSource");
		try {
			
			//这里可以通过异步写入数据库
		

			BaseJdbcDaoSupport baseJdbcDaoSupport = (BaseJdbcDaoSupport)joinPoint.getTarget();
			Object[] args = joinPoint.getArgs();
			long showtableConfigId=(Long)args[0];
			
			ShowTableConfig showTableConfig = ArchCache.getInstance().getShowTableConfigCache().get(showtableConfigId);
			
			
			DataSource ds = DSManager.getDataSource(showTableConfig.getTargetDs());
		
			
			logger.debug(" ===设置数据源："+showTableConfig.getTargetDs());
			
			baseJdbcDaoSupport.setDataSource(ds);
			
			logger.debug(" ===成功设置数据源："+showTableConfig.getTargetDs());
			
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
			
			
			Object returnVal = joinPoint.proceed();
			
			
			return returnVal;
		} catch (Throwable e) {
			throw new WaeRuntimeException(e);
		}
	}
	
	
	
}
