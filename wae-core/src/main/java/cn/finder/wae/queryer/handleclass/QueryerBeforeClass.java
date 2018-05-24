package cn.finder.wae.queryer.handleclass;

import cn.finder.ui.webtool.QueryCondition;

/*
 * 
 * 一般用来修改提交参数
 * 以及处理前业务判断
 *  如果业务有错误，那么直接抛出 PSISDaoRuntimeException 
 */
public interface QueryerBeforeClass {

	public void handle(long showTableConfigId,QueryCondition<Object[]> condition);
}
