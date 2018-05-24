package cn.finder.wae.common.comm.templatemsg;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cn.finder.common.util.JsonUtil;
import cn.finder.wae.business.domain.TemplateMsg;
import cn.finder.wae.common.comm.Common;
import cn.finder.wae.wx.data.WXAppInfo;
import cn.finder.wx.request.SendTempateMsgRequest;
import cn.finder.wx.request.SendTempateMsgRequest.TemplateData;
import cn.finder.wx.response.SendTempateMsgResponse;
import cn.finder.wx.service.WXService;
/***
 * 
 * @author whl
 *
 */
public class WXTemplateMsgSender {

	private TemplateMsg tm;
	
	public WXTemplateMsgSender(TemplateMsg tm){
		this.tm=tm;
	}
	
	public  void send(Map<String,Object> variables){
		 //模板消息编号
		Object templateMsgCode=variables.get("templateMsgCode");
	    if(templateMsgCode==null){
	    	//throw new RuntimeException("can't find parameter:templateMsgCode");
	    	return ;
	    }
	    
	    String wx_template_id="";
	    String toUserId="";
	    String url="";
		String topcolor="";
		
		String wx_appid="";// 公众号 appid
		
//		try{
//			wx_appid=variables.get("appid").toString();
//		}
//		catch(Exception e){
//			wx_appid=ArchCache.getInstance().getSysConfigCache().get(SysConfigCache.KEY_CONFIG_SYS_GLOBAL_WX_OPENID).getValue();
//		}
		
		if(variables.get("appid") != null){
			wx_appid = variables.get("appid").toString();
		}else{
			//服务号appid为空，直接返回
			return;
		}
		
		Map<String,TemplateData> data=new HashMap<String, SendTempateMsgRequest.TemplateData>();
		
	//	{"template_id":"fasd","toUserId":"{toUserId}","first":{value:"维修服务"},"keyword1":{value:"{repair_name}"},"keyword2":{value:"{repair_phone}"},"keyword3":{value:"{repair_time}"},"remark":{value:"如有需要请联系维修人员"}}
	    Map<String,Object> template_datamap=JsonUtil.getMap4Json(tm.getTemplateContent());
	    
	    
		
	    Set<Entry<String, Object>> template_datamap_set= template_datamap.entrySet();
	    
	    Iterator<Entry<String, Object>>  itr=  template_datamap_set.iterator();
	    
	    
	    while(itr.hasNext()){
	    	Entry<String, Object> entry= itr.next();
	    	
	    	String key=entry.getKey();
	    	Object value=entry.getValue();
	    	
	    	if("template_id".equals(key)){
		    	
				try{
					wx_template_id=Common.descWrapper(value.toString(), variables);
				}
				catch(Exception e){
					//throw new RuntimeException("can't find parameter:template_id");
					return ;
				}
				continue;
	    	}
	    	
	    	if("toUserId".equals(key)){
	    		
				try{
					toUserId = Common.descWrapper(value.toString(), variables);
					
				}
				catch(Exception e){
					//throw new RuntimeException("can't find parameter:toUserId");
					return ;
				}
				continue;
	    	}
	    	
	    	if("url".equals(key)){
	    		
				try{
					//url=value.toString();
					url = Common.descWrapper(value.toString(),variables);
				}
				catch(Exception e){
				}
				continue;
	    	}
	    	if("topcolor".equals(key)){
				try{
					topcolor=Common.descWrapper(value.toString(),variables);
				}
				catch(Exception e){
				}
				continue;
	    	}
			
	    	
	    	
	    	
	    	
	    	Map<String,Object> item=(Map<String,Object>)value;
	    	
	    	String v=item.get("value").toString();
	    	
	    	v=Common.descWrapper(v, variables);
	    	
	    	
	    	String color= "";
	    	try{
	    		color= item.get("color").toString();
	    		color=Common.descWrapper(color, variables);
	    	}
	    	catch(Exception e){
	    		
	    	}
	    	
	    	TemplateData td=new TemplateData();
	    	td.setValue(v);
	    	td.setColor(color);
	    	
	    	data.put(key, td);
	   
	        
	         
	    }
	    
	    
		
		WXService wxService=new WXService();
		SendTempateMsgRequest req=new SendTempateMsgRequest();
		
		req.setTouser(toUserId.toString());
		req.setTemplate_id(wx_template_id);
		req.setUrl(url);
		req.setTopcolor(topcolor);
		
		req.setData(data);
		
		
		SendTempateMsgResponse sendTempateMsgResponse=wxService.sendTempateMsg(WXAppInfo.getAppInfo(wx_appid).getAccessToken(), req);
		
	}
}
