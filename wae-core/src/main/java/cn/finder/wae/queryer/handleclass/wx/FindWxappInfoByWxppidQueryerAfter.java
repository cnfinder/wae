package cn.finder.wae.queryer.handleclass.wx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.domain.wx.AppInfo;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.business.module.common.service.CommonService;
import cn.finder.wae.common.comm.AppApplicationContextUtil;
import cn.finder.wae.queryer.handleclass.QueryerDBAfterClass;

/***
 * 根据 服务号的 wx_appid 获取 主键 wx_appinfo_id
 * @author whl
 *
 */

public class FindWxappInfoByWxppidQueryerAfter extends QueryerDBAfterClass {
 
	@Override
	public TableQueryResult handle(TableQueryResult tableQueryResult,
			long showTableConfigId, QueryCondition<Object[]> condition) {
		
		 super.handle(tableQueryResult, showTableConfigId, condition);
		
		tableQueryResult= new TableQueryResult();
		
		List<Map<String,Object>> resultList= new ArrayList<Map<String,Object>>();
		 	
		 	
		tableQueryResult.setResultList(resultList);
		 	
	 	tableQueryResult.setPageIndex(condition.getPageIndex());
	 	tableQueryResult.setPageSize(condition.getPageSize());
	 	tableQueryResult.setCount(1l);
		CommonService commonService = AppApplicationContextUtil.getContext().getBean("commonService", CommonService.class);;
	 			
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
	 	String appid= data.get("wx_appid").toString();
	 	
	 	AppInfo appInfo= commonService.findAppInfoByWXAppId(appid);
	 	
	 	Map<String,Object> mapItem = new HashMap<String, Object>();
	 	
 		mapItem.put("wx_appinfo_id",appInfo.getId());
 		resultList.add(mapItem);
	 	
	 	return tableQueryResult;
	 	
		
	}
	
}
