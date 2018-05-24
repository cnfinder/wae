package cn.finder.wae.queryer.common;

import java.util.ArrayList;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.ShowTableConfig;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.cache.ArchCache;


/***
 * 默认不做 处理类
 * @author wu hualong
 *
 */
public class CommonNoDealQueryer extends  BaseCommonDBQueryer{

	
	private final static Logger logger = Logger.getLogger(CommonNoDealQueryer.class);
	
	@Override
	public TableQueryResult queryTableQueryResult(long showTableConfigId,
			QueryCondition<Object[]> condition) {
		
		logger.debug("=====go into CommonNoDealQueryer======");
		
		logger.debug("==showTableConfigId:"+showTableConfigId);
		
		final TableQueryResult qr =new TableQueryResult();
		ShowTableConfig showTableConfig = ArchCache.getInstance().getShowTableConfigCache().get(showTableConfigId);
		if(condition.getPageSize()==0){
	    	//默认为数据库设置大小
	    	condition.setPageSize(showTableConfig.getPageSize()==0?10:showTableConfig.getPageSize());
	    }
	    qr.setResultList(new ArrayList<Map<String,Object>>());
		qr.setFields(showTableConfig.getShowDataConfigs());
		qr.setPageIndex(condition.getPageIndex());
		qr.setPageSize(condition.getPageSize());
		qr.setCount(1L);
		
		qr.setShowTableConfig(showTableConfig);
		return qr;
	}

	public void setJDBCDataSource(DataSource dataSource) {
		super.setJdbcDataSource(dataSource);
	}

}
