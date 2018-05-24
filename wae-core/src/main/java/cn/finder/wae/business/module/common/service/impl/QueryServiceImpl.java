package cn.finder.wae.business.module.common.service.impl;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.ShowTableConfig;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.module.common.dao.QueryDao;
import cn.finder.wae.business.module.common.service.QueryService;
import cn.finder.wae.cache.ArchCache;

public class QueryServiceImpl implements QueryService {

	private QueryDao queryDao;
	
	public void setQueryDao(QueryDao queryDao)
	{
		this.queryDao = queryDao;
	}
	@Override
	public TableQueryResult queryTableQueryResult(final long showTableConfigId,
			final QueryCondition<Object[]> condition) {
		
		TableQueryResult result=null;
		
		ShowTableConfig sdc = ArchCache.getInstance().getShowTableConfigCache().get(showTableConfigId);
		if(sdc.getUseTransaction() ==0){
			result= queryTableQueryResultNoTransaction(showTableConfigId,condition);
		}else
			result= queryTableQueryResultTransaction(showTableConfigId,condition);
		return result;
	}
	@Override
	public TableQueryResult queryTableQueryResultNoTransaction(
			long showTableConfigId, QueryCondition<Object[]> condition) {
		return queryDao.queryTableQueryResult(showTableConfigId, condition);
	}
	
	
	@Override
	public TableQueryResult queryTableQueryResultTransaction(
			long showTableConfigId, QueryCondition<Object[]> condition) {
		return queryDao.queryTableQueryResult(showTableConfigId, condition);
	}

	
	
}
