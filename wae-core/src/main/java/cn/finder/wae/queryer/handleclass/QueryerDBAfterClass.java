package cn.finder.wae.queryer.handleclass;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.ShowTableConfig;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.common.db.DBType;
import cn.finder.wae.common.db.ds.DSManager;
import cn.finder.wae.queryer.common.BaseCommonDBQueryer;


/*
 * 有对数据库操作的 那么轻继承此类,重写 handler方法  并且调用 基类的 handle
 * 就可以直接操作数据库
 * 
 */
public abstract class QueryerDBAfterClass extends BaseCommonDBQueryer implements QueryerAfterClass
{

	

	@Override
	public TableQueryResult handle(TableQueryResult tableQueryResult,
			long showTableConfigId, QueryCondition<Object[]> condition) {
		
		ShowTableConfig showTableConfig=ArchCache.getInstance().getShowTableConfigCache().get(showTableConfigId);
		DataSource ds = DSManager.getDataSource(showTableConfig.getTargetDs());
		BasicDataSource basicDataSource = (BasicDataSource)ds;
		String driverClassName = basicDataSource.getDriverClassName();
		//判断数据库类型
		if(driverClassName.toLowerCase().indexOf("mysql")!=-1){
			setDbType(DBType.MySql);
		}else if(driverClassName.toLowerCase().indexOf("sqlserver")!=-1){
			
			setDbType(DBType.SqlServer);
		}
		else if(driverClassName.toLowerCase().indexOf("oracle")!=-1){
			
			setDbType(DBType.Oracle);
		}
		
		setJDBCDataSource(ds);
		
		return tableQueryResult;
	}

	@Override
	public void setJDBCDataSource(DataSource dataSource) {
		// TODO Auto-generated method stub
		super.setDataSource(dataSource);
	}

	@Override
	public TableQueryResult queryTableQueryResult(long showTableConfigId,
			QueryCondition<Object[]> condition) {
		// TODO Auto-generated method stub
		return null;
	}

}
