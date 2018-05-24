package cn.finder.wae.queryer.handleclass.wx.media;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.domain.wx.AppInfo;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.business.module.common.service.CommonService;
import cn.finder.wae.common.comm.AppApplicationContextUtil;
import cn.finder.wae.queryer.handleclass.QueryerDBAfterClass;
import cn.finder.wae.wx.data.WXAppInfo;
import cn.finder.wx.request.MediaListRequest;
import cn.finder.wx.response.MediaListResponse;
import cn.finder.wx.response.MediaListResponse.MediaItem;
import cn.finder.wx.service.WXService;

/***
 * 获取微信图文消息列表(主要用来处理 key_id 关联 media_id)
 * @author whl
 *
 */
public class FindWXMediaNewsQueryerAfter extends QueryerDBAfterClass {
 
	@Override
	public TableQueryResult handle(TableQueryResult tableQueryResult,
			long showTableConfigId, QueryCondition<Object[]> condition) {
		
		 super.handle(tableQueryResult, showTableConfigId, condition);
		
		tableQueryResult= new TableQueryResult();
		
		List<Map<String,Object>> resultList= new ArrayList<Map<String,Object>>();
		 	
		 	
		tableQueryResult.setResultList(resultList);
		 	
	 	tableQueryResult.setPageIndex(condition.getPageIndex());
	 	tableQueryResult.setPageSize(condition.getPageSize());
	 	//从wx_appinfo_id中获取
 		CommonService commonService = AppApplicationContextUtil.getContext().getBean("commonService", CommonService.class);;
 		
	 			
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
	 	String appid="";
		try{
			appid=data.get("wx_appid").toString();
	 	}
	 	catch(Exception e){
	 		appid="";
	 	}
	 	
	 	
	 	int wx_appinfo_id=-1;
	 	
	 	try{
	 		wx_appinfo_id=Integer.parseInt(data.get("wx_appinfo_id").toString());
	 	}
	 	catch(Exception e){
	 		wx_appinfo_id=-1;
	 	}
	 	
	 	
	 	if("".equals(appid)){
	 		
	 		AppInfo appInfo=commonService.findAppInfo(wx_appinfo_id);
	 		appid=appInfo.getAppid();
	 		
	 	}
	 	
	 	
	 	WXService service=new WXService();
    	String accessToken=WXAppInfo.getAppInfo(appid).getAccessToken();
    	logger.info("======accessToken:"+accessToken);
    	MediaListRequest req=new MediaListRequest();
    	req.setType(MediaListRequest.TYPE_NEWS);
    	req.setOffset((condition.getPageIndex()-1)*condition.getPageSize());
    	req.setCount(condition.getPageSize());
    	
    	MediaListResponse resp= service.mediaList(accessToken,req);
    	logger.info("======resp.getBody():"+resp.getBody());
	 	if(resp.getItem_count()>0){
	 		  List<MediaItem> mediaItems=resp.getItem();
	 	    	
 	    	logger.info("============mediaItems.size:"+mediaItems.size());
 	    	//List<Map<String,Object>> list= JSONArray.toList(JSONArray.fromObject(mediaItems),Map.class);
 	    	List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
 	    	JSONObject jsonData = JSONObject.fromObject(resp.getBody());
 	    	Map<String,Object> item = (Map<String,Object>)jsonData;
 	    	item.put("access_token", accessToken);
 	    	list.add(item);
 	    	logger.info("===List<Map<String,Object>> list:"+list.size());
 	    	tableQueryResult.setResultList(list);
 	    	
 	    	
	 	}
	 	
	 	tableQueryResult.setCount((long)resp.getTotal_count());//设置总数量
		logger.info("====="+tableQueryResult.getCount());
      
	 	return tableQueryResult;
	 	
		
	}
	
}
