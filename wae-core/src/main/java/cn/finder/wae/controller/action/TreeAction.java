package cn.finder.wae.controller.action;


import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.common.base.BaseActionSupport;
import cn.finder.wae.queryer.common.LazyTreeCommonQueryer;

public class TreeAction extends BaseActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7522863906276893698L;
	
	private Long showTableConfigId;
	QueryCondition<Object[]> queryCondition = new QueryCondition<Object[]>();
	
	private LazyTreeCommonQueryer lazyTreeCommonQueryer = new LazyTreeCommonQueryer();
	
	public String loadNodes() throws InterruptedException{
		queryCondition.setPageIndex(1);
		queryCondition.setPageSize(1000);
		Long treeId = 0l;
		if(request.getParameter("id") != null){
			treeId = Long.valueOf(request.getParameter("id"));
			Object[] params = {treeId};
			queryCondition.setWherepParameterValues(params);
		}
		TableQueryResult tqr =lazyTreeCommonQueryer.queryTableQueryResult(showTableConfigId, queryCondition);
		String jsonStr = tqr.getResultList().toString();
		responseJson(jsonStr);
		return null;
	}

	public Long getShowTableConfigId() {
		return showTableConfigId;
	}

	public void setShowTableConfigId(Long showTableConfigId) {
		this.showTableConfigId = showTableConfigId;
	}

	public LazyTreeCommonQueryer getLazyTreeCommonQueryer() {
		return lazyTreeCommonQueryer;
	}

	public void setLazyTreeCommonQueryer(LazyTreeCommonQueryer lazyTreeCommonQueryer) {
		this.lazyTreeCommonQueryer = lazyTreeCommonQueryer;
	}

	public QueryCondition<Object[]> getQueryCondition() {
		return queryCondition;
	}

	public void setQueryCondition(QueryCondition<Object[]> queryCondition) {
		this.queryCondition = queryCondition;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
	
}
