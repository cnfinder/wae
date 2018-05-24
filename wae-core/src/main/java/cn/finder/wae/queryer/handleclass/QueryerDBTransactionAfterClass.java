package cn.finder.wae.queryer.handleclass;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;
/***
 * 数据库事务处理   
 * 需要在 spring 配置全局事务 切入到业务的  DBAfterClass 
 * @author whl
 *
 */
public abstract class QueryerDBTransactionAfterClass extends QueryerDBAfterClass {

	@Override
	public TableQueryResult handle(TableQueryResult tableQueryResult,
			long showTableConfigId, QueryCondition<Object[]> condition) {
		// TODO Auto-generated method stub
		 super.handle(tableQueryResult, showTableConfigId, condition);
		 
		 tableQueryResult=handleTransaction(tableQueryResult,showTableConfigId,condition);
		return tableQueryResult;
	}

	public abstract TableQueryResult handleTransaction(TableQueryResult tableQueryResult,long showTableConfigId,
			QueryCondition<Object[]> condition);

}
