package cn.finder.wae.queryer.handleclass.wx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.business.module.common.service.CommonService;
import cn.finder.wae.common.comm.AppApplicationContextUtil;
import cn.finder.wae.queryer.handleclass.QueryerDBAfterClass;
import cn.finder.wae.wx.data.WXAppInfo;
import cn.finder.wx.response.FindOpenIdInfoByCodeResponse;
import cn.finder.wx.service.WXService;

/***
 * 微信菜单和资源关联
 * @author whl
 *
 */

public class WXMenuMediaLinkQueryerAfter extends QueryerDBAfterClass {
 
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
			
	 	CommonService commonService=AppApplicationContextUtil.getContext().getBean("commonService", CommonService.class);
			
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
	 	int wx_appinfo_id = Integer.parseInt(data.get("wx_appinfo_id").toString());
	 	int menu_id= Integer.parseInt(data.get("menu_id").toString());
	 	String media_id= data.get("media_id").toString();
	 	
	 	Map<String,Object> mapItem = new HashMap<String, Object>();
	 	int cnt=commonService.updateWXMenuMediaLink(menu_id, media_id);
	 	if(cnt>0){
	 		mapItem.put("status",true);
	 		mapItem.put("msg", "关联成功");
	 	}else{
	 		mapItem.put("status",false);
	 		mapItem.put("msg", "关联失败");
	 	}
	 	
	 	
 		
 		resultList.add(mapItem);
	 	
	 	return tableQueryResult;
	 	
		
	}
	
}
