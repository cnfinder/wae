package cn.finder.wae.queryer.handleclass.wx.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.queryer.handleclass.QueryerDBAfterClass;
import cn.finder.wae.wx.cache.WXCache;
import cn.finder.wae.wx.data.WXAppInfo;
import cn.finder.wx.app.request.FindSessionKeyRequest;
import cn.finder.wx.app.response.FindSessionKeyResponse;
import cn.finder.wx.service.WXService;

/***
 * 创建session
 * 根据CODE获取微信用户信息
 * 
 * @author whl
 *
 */
public class CreateSessionKeyByCodeQueryerAfter extends QueryerDBAfterClass {
 
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
	 	
	 	logger.info("===wx_code="+wx_code+",appid="+appid+",appsecret="+appsecret);
	 	
	 	WXService.WXAppService service=new WXService.WXAppService();
	 	//根据code获取openid
	 	//获取用户openid
	 	
	 	FindSessionKeyRequest req=new FindSessionKeyRequest();
	 	
	 	req.setAppid(appid);
	 	req.setJs_code(wx_code);
	 	req.setSecret(appsecret);
	 	
		FindSessionKeyResponse resp=service.findSessionKeyResponse(req);
		logger.info("==findSessionKeyResponse:"+resp.getBody());
		logger.info("===openid:"+resp.getOpenid());
	 	
	 	Map<String,Object> mapItem = new HashMap<String, Object>();
	 	
	 	if(!StringUtils.isEmpty(resp.getOpenid())){
	 		
	 		/*mapItem.put("openid", resp.getOpenid());
	 		mapItem.put("session_key",resp.getSession_key());*/
	 		
	 		String session=RandomStringUtils.randomAlphanumeric(64);
	 		
	 		HashMap<String,Object> sessionInfo=new HashMap<String, Object>();
	 		sessionInfo.put("openid", resp.getOpenid());
	 		sessionInfo.put("session_key",resp.getSession_key());
	 		sessionInfo.put("expires_in",resp.getExpires_in()); //expires_in:2592000
	 		
	 		WXCache.getInstance().getWXSessionCache().add(session, sessionInfo,resp.getExpires_in());
	 		
	 		
	 		mapItem.put("wx_session", session);
	 		mapItem.put("openid", resp.getOpenid());//返回openid 为了方便
	 		
	 	}
 		
 		resultList.add(mapItem);
	 	
	 	return tableQueryResult;
	 	
		
	}
	
}
