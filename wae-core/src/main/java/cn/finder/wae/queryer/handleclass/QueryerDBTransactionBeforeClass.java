package cn.finder.wae.queryer.handleclass;

import cn.finder.ui.webtool.QueryCondition;


/***
 * 数据库事务处理   
 * 需要在 spring 配置全局事务 切入到业务的  DBBeforeClass 
 * @author whl
 *
 */
public abstract class QueryerDBTransactionBeforeClass extends QueryerDBBeforeClass {

	@Override
	public void handle(long showTableConfigId,
			QueryCondition<Object[]> condition) {
		// TODO Auto-generated method stub
		super.handle(showTableConfigId, condition);
		
		handleTransaction(showTableConfigId,condition);
		
	}
	
	public abstract void handleTransaction(long showTableConfigId,
			QueryCondition<Object[]> condition);


	
}
