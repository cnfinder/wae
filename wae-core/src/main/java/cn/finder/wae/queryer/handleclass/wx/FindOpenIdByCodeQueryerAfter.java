package cn.finder.wae.queryer.handleclass.wx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.queryer.handleclass.QueryerDBAfterClass;
import cn.finder.wae.wx.data.WXAppInfo;
import cn.finder.wx.response.FindOpenIdInfoByCodeResponse;
import cn.finder.wx.service.WXService;

/***
 * 根据CODE,appid获取用户OPENID 微信服务号
 * @author whl
 *
 */

public class FindOpenIdByCodeQueryerAfter extends QueryerDBAfterClass {
 
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
			
	 			
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
	 	String wx_code = data.get("wx_code").toString();
	 	String appid= data.get("wx_appid").toString();
	 	//String appsecret= data.get("wx_appsecret").toString();
	 	
	 	String appsecret= WXAppInfo.getAppInfo(appid).getAppSecret();
	 	
	 	WXService service=new WXService();
	 	//根据code获取openid
	 	//获取用户openid
		FindOpenIdInfoByCodeResponse findOpenIdInfoByCodeResp=service.findOpenIdInfoByCode(appid, appsecret, wx_code);
		logger.info("==findOpenIdInfoByCodeResp:"+findOpenIdInfoByCodeResp.getBody());
		logger.info("===openid:"+findOpenIdInfoByCodeResp.getOpenId());
	 	String user_openid="";
	 	String access_token="";
	 	if(!StringUtils.isEmpty(findOpenIdInfoByCodeResp.getOpenId())){
	 		user_openid= findOpenIdInfoByCodeResp.getOpenId();
	 		access_token=findOpenIdInfoByCodeResp.getAccessToken();
	 	}
	 	
	 	Map<String,Object> mapItem = new HashMap<String, Object>();
	 	
 		mapItem.put("user_openid",user_openid);
 		mapItem.put("access_token",access_token);
 		resultList.add(mapItem);
	 	
	 	return tableQueryResult;
	 	
		
	}
	
}
