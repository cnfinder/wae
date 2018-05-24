package cn.finder.wae.business.module.common.dao;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;

public interface QueryDao {

	/****
	 * 一般用于表格数据显示
	 * @param showTableConfigId
	 * @param condition
	 * @return
	 */
	public TableQueryResult queryTableQueryResult(long showTableConfigId,QueryCondition<Object[]> condition);
}
