package cn.finder.wae.queryer.handleclass.wx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.common.comm.AppApplicationContextUtil;
import cn.finder.wae.queryer.handleclass.QueryerDBAfterClass;
import cn.finder.wae.wx.data.WXAppInfo;
import cn.finder.wx.response.FindOpenIdInfoByCodeResponse;
import cn.finder.wx.response.FindUserInfoResponse;
import cn.finder.wx.service.WXService;

/***
 * 根据CODE获取微信用户信息
 * @author whl
 *
 */
public class FindUserInfoByCodeQueryerAfter extends QueryerDBAfterClass {
 
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
	 	String appsecret= WXAppInfo.getAppInfo(appid).getAppSecret();
	 	
	 	
	 	
	 	
	 	WXService service=new WXService();
	 	//根据code获取openid
	 	//获取用户openid
		FindOpenIdInfoByCodeResponse findOpenIdInfoByCodeResp=service.findOpenIdInfoByCode(appid, appsecret, wx_code);
		logger.info("==findOpenIdInfoByCodeResp:"+findOpenIdInfoByCodeResp.getBody());
		logger.info("===openid:"+findOpenIdInfoByCodeResp.getOpenId());
	 	String user_openid="";
	 	
	 	Map<String,Object> mapItem = new HashMap<String, Object>();
	 	
	 	if(!StringUtils.isEmpty(findOpenIdInfoByCodeResp.getOpenId())){
	 		user_openid= findOpenIdInfoByCodeResp.getOpenId();
	 		
	 		FindUserInfoResponse userinfoResp=service.findUserInfo(findOpenIdInfoByCodeResp.getAccessToken(), user_openid);
	 		
	 		mapItem.put("nickname", userinfoResp.getNickname());
	 		mapItem.put("openid", userinfoResp.getOpenid());
	 		mapItem.put("sex", userinfoResp.getSex());
	 		mapItem.put("province", userinfoResp.getProvince());
	 		mapItem.put("city", userinfoResp.getCity());
	 		mapItem.put("country", userinfoResp.getCountry());
	 		mapItem.put("headimgurl", userinfoResp.getHeadimgurl());
	 		mapItem.put("unionid", userinfoResp.getUnionid());
	 		
	 	
	 		
	 		
	 	}
	 	
	 	
	 	data.put("openid", user_openid);
 		mapItem.put("user_openid",user_openid);
 		
 		resultList.add(mapItem);
	 	
	 	return tableQueryResult;
	 	
		
	}
	
}
