package cn.finder.wae.queryer.common;

import javax.sql.DataSource;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.common.aspect.logger.ArchType;
import cn.finder.wae.common.aspect.logger.FinderLogger;

/***
 * 
 * @author: wuhualong
 * @data:2014-7-16上午9:56:31
 * @function:不需要做任何业务处理的处理类
 */
public class EmptyCommonQueryer extends  BaseCommonDBQueryer{
	
	@Override
	public TableQueryResult queryTableQueryResult(long showTableConfigId,
			QueryCondition<Object[]> condition){
		
		logger.debug("=====go into EmptyCommonQueryer======");
		
		logger.debug("==showTableConfigId:"+showTableConfigId);
		
		TableQueryResult queryResult =new TableQueryResult();
		
		queryResult.setCount(0l);
		queryResult.setPageIndex(condition.getPageIndex());
		queryResult.setPageSize(condition.getPageSize());
		return queryResult;
		
	}
	
	public void setJDBCDataSource(DataSource dataSource) {
		super.setJdbcDataSource(dataSource);
	}
	
}
