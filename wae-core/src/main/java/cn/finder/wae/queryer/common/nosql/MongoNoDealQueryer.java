package cn.finder.wae.queryer.common.nosql;

import java.util.ArrayList;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.ShowTableConfig;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.cache.ArchCache;


/**
 * mongo 不处理类
 * 
 * @author lizhi
 */
public class MongoNoDealQueryer extends MongoDBQueryer {

	private final static Logger logger = Logger.getLogger(MongoNoDealQueryer.class);

	@Override
	public TableQueryResult queryTableQueryResult(long showTableConfigId, QueryCondition<Object[]> condition) {
		logger.debug("=====go into MongoNoDealQueryer======");
		final TableQueryResult qr = new TableQueryResult();
		ShowTableConfig showTableConfig = ArchCache.getInstance().getShowTableConfigCache().get(showTableConfigId);
		if(condition.getPageSize()==0){
	    	//默认为数据库设置大小
	    	condition.setPageSize(showTableConfig.getPageSize()==0?10:showTableConfig.getPageSize());
	    }
		
		qr.setResultList(new ArrayList<Map<String, Object>>());
		qr.setFields(showTableConfig.getShowDataConfigs());
		qr.setPageIndex(condition.getPageIndex());
		qr.setPageSize(condition.getPageSize());
		qr.setCount(0L);
		qr.setShowTableConfig(showTableConfig);
		return qr;
	}

}
