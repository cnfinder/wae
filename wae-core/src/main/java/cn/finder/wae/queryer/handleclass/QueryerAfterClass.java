package cn.finder.wae.queryer.handleclass;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;

/*
 * 一般用来修改返回数据
 */
public interface QueryerAfterClass {

	public TableQueryResult handle(TableQueryResult tableQueryResult,long showTableConfigId,QueryCondition<Object[]> condition);
}
