package cn.finder.wae.queryer.common;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.ShowDataConfig;
import cn.finder.wae.business.domain.ShowTableConfig;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.business.module.common.dao.QueryDao;
import cn.finder.wae.business.module.common.dao.impl.QueryDaoImpl;
import cn.finder.wae.cache.ConstantsCache;
import cn.finder.wae.cache.ArchCache;


/***
 * 
 * @author wanggan
 * 加载添加页面控件数据
 *
 */

@Deprecated
public class ProcessImportQueryer extends  BaseCommonDBQueryer{

	
private final static Logger logger = Logger.getLogger(ProcessImportQueryer.class);
	
	@Override
	public TableQueryResult queryTableQueryResult(long showTableConfigId,
			QueryCondition<Object[]> condition){
		
		logger.debug("=====go into ProcessImportQueryer======");
		
		logger.debug("==showTableConfigId:"+showTableConfigId);
		@SuppressWarnings("unchecked")
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
		
		Long resourceSTCID = Long.valueOf(String.valueOf(data.get("resourceShowtableConfigId")));
		
		Long destSTCID = Long.valueOf(String.valueOf(data.get("destShowtableConfigId")));
		
		logger.debug("==resourceSTCID:"+resourceSTCID);
		logger.debug("==destSTCID:"+destSTCID);
		
		//ShowTableConfig resourceShowTableConfig = ArchCache.getInstance().getShowTableConfigCache().get(resourceSTCID);
		
		//ShowTableConfig destShowTableConfig = ArchCache.getInstance().getShowTableConfigCache().get(destSTCID);
		// 需要修改？
		QueryDao queryDaoImpl = new QueryDaoImpl();
		
		TableQueryResult tableQueryResult=queryDaoImpl.queryTableQueryResult(resourceSTCID, condition);
		
		List<Map<String,Object>> list=tableQueryResult.getResultList();
		
		//Long addShowTableConfigId=resourceShowTableConfig.getAddShowtableConfigId();
		
		Iterator<Map<String,Object>> it=list.iterator();
		while(it.hasNext()){
			Map<String,Object> map=it.next();
			map.put("showtableConfigId", destSTCID);
			MapParaQueryConditionDto<String, Object> para=new MapParaQueryConditionDto<String, Object>();
			para.setMapParas(map);
			queryDaoImpl.queryTableQueryResult(destSTCID, para);
		}
		return null;
	}
	
	

	public void setJDBCDataSource(DataSource dataSource) {
		super.setJdbcDataSource(dataSource);
	}

}
