package cn.finder.wae.business.facade.rest;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;

import cn.finder.wae.business.module.common.service.QueryService;

/***
 * 此操作已经不需要了
 * @author wuhualong 
 *
 */
@Deprecated
public class PspFacade {

	private QueryService queryService;

	public void setQueryService(QueryService queryService) {
		this.queryService = queryService;
	}
	
	public TableQueryResult queryTableQueryResult(long showTableConfigId, QueryCondition<Object[]> condition){
		return queryService.queryTableQueryResult(showTableConfigId, condition);
	}
	
}
