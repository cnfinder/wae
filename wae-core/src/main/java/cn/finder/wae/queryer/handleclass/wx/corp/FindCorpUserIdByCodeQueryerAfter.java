package cn.finder.wae.queryer.handleclass.wx.corp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.queryer.handleclass.QueryerAfterClass;
import cn.finder.wae.wx.data.WXAppInfo;
import cn.finder.wx.corp.response.FindUserIdInfoResponse;
import cn.finder.wx.service.WXService;

/***
 * 根据CODE获取用户corp_user_id
 * 和wx_appid
 * accessToken:accessToken,//可选  如果accessToken 不为空  那么使用accessToken去获取
 * @author whl
 *
 */
public class FindCorpUserIdByCodeQueryerAfter implements QueryerAfterClass {
 
	private Logger logger=Logger.getLogger(FindCorpUserIdByCodeQueryerAfter.class);
	@Override
	public TableQueryResult handle(TableQueryResult tableQueryResult,
			long showTableConfigId, QueryCondition<Object[]> condition) {
		
		
		tableQueryResult= new TableQueryResult();
		
		List<Map<String,Object>> resultList= new ArrayList<Map<String,Object>>();
		 	
		 	
		tableQueryResult.setResultList(resultList);
		 	
	 	tableQueryResult.setPageIndex(condition.getPageIndex());
	 	tableQueryResult.setPageSize(condition.getPageSize());
	 	tableQueryResult.setCount(1l);
			
	 			
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
		String code ="";
		try {
			code = data.get("code").toString();
		} catch (Exception e) {
			logger.error("====code is null");
		}
		String corpid="";
	 	try {
			 corpid= data.get("wx_appid").toString();
		} catch (Exception e) {
		}
	 	String corpsecret="";
	 	try {
			corpsecret= data.get("corpsecret").toString();
		} catch (Exception e) {
		}
	 	String accessToken="";
	 	try {
			 accessToken= data.get("accessToken").toString();
		} catch (Exception e) {
		}
	 	String wx_appid="";
	 	try {
	 		wx_appid= data.get("wx_appid").toString();
		} catch (Exception e) {
		}
	 	
	 	
	 	WXService.CorpService corpService=new WXService.CorpService();
	 	
	 	String userId="";
	 	
	 	if(!StringUtils.isEmpty(code)){
	 		
	 		
	 		if(!StringUtils.isEmpty(accessToken)){
	 		}
	 		else if(!StringUtils.isEmpty(corpid)){
	 			accessToken=WXAppInfo.getCorpAccessTokenInfo(corpid).getAccessToken();
	 		}
	 		
	 		
	 		FindUserIdInfoResponse 	userIdInfoResponse=corpService.findUserIdInfo(accessToken, code);
 		   logger.info("=====userIdInfoResponse:"+userIdInfoResponse.getBody());
		    if(!userIdInfoResponse.isError()){
			   if(userIdInfoResponse.isCorpUser()){
				  userId= userIdInfoResponse.getUserId();
			   }else{
				  userId= userIdInfoResponse.getOpenId();
			   }
		   }
 		    
 		  
	 	}
	 	logger.info("===corp_user_id:"+userId);
	 	
	 	Map<String,Object> mapItem = new HashMap<String, Object>();
	 	
 		mapItem.put("corp_user_id",userId);
 		
 		resultList.add(mapItem);
	 	
	 	return tableQueryResult;
	 	
		
	}
	
}
