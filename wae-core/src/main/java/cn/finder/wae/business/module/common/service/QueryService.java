package cn.finder.wae.business.module.common.service;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;

/***
 * 统一业务调用接口入口  支持 有事务、非事务 2中模式调用， 默认为事务调用
 * @author Administrator
 *
 */
public interface QueryService {

	/****
	 *  通用接口  （默认 启动spring 事务）
	 * @param showTableConfigId
	 * @param condition
	 * @return
	 */
	public TableQueryResult queryTableQueryResult(long showTableConfigId,QueryCondition<Object[]> condition);
	
	/****
	 *  通用接口  （默认 启动spring 事务）
	 * @param showTableConfigId
	 * @param condition
	 * @return
	 */
	public TableQueryResult queryTableQueryResultTransaction(long showTableConfigId,QueryCondition<Object[]> condition);
	
	
	
	/***
	 *  通用接口  （不受spring事务管理）
	 * @param showTableConfigId
	 * @param condition
	 * @return
	 */
	public TableQueryResult queryTableQueryResultNoTransaction(long showTableConfigId,QueryCondition<Object[]> condition);
	
	
	
}
