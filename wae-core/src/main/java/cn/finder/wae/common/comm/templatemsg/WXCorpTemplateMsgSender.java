package cn.finder.wae.common.comm.templatemsg;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import cn.finder.wae.business.domain.TemplateMsg;
import cn.finder.wae.common.comm.Common;
import cn.finder.wae.wx.data.WXAppInfo;
import cn.finder.wx.corp.response.MsgSendResponse;
import cn.finder.wx.service.WXService;
/***
 * 微信企业号 发送文本消息模板
 * @author whl
 *
 */
public class WXCorpTemplateMsgSender {

	
	private Logger logger=Logger.getLogger(WXCorpTemplateMsgSender.class);
	private TemplateMsg tm;
	
	public WXCorpTemplateMsgSender(TemplateMsg tm){
		this.tm=tm;
	}
	
	public  void send(Map<String,Object> variables){
		 //模板消息编号
		Object templateMsgCode=variables.get("wx_corp_templateMsgCode");
	    if(templateMsgCode==null){
	    	//throw new RuntimeException("can't find parameter:templateMsgCode");
	    	return ;
	    }
	    Object wx_appid = variables.get("wx_appid");
	   if(variables.containsKey("toUserId")){
		   String toUserId=variables.get("toUserId").toString();
		   String[] userIds= toUserId.split(",");
		   String userIdCorp =StringUtils.join(userIds, "|");
		   
		   variables.put("toUserIdCorp", userIdCorp);
	   }
		if(!variables.containsKey("agentid")){
			//由于  腾讯升级到企业微信后  默认 agentid=0 没有了 
			try{
				String msgAgentid=WXAppInfo.getCorpAccessTokenInfo(wx_appid.toString()).getMsgAgentId();
				variables.put("agentid", msgAgentid);
			}
			catch(Exception e){
				variables.put("agentid", "0");
			}
			
		}
		logger.info("====agentid:"+variables.get("agentid").toString());
		String templateContent =tm.getTemplateContent();
		//logger.info("=====templateContent:"+templateContent);
		
		
		
		
		templateContent=Common.descWrapper(templateContent,variables);
		
		WXService.CorpService corpService=new WXService.CorpService();
		
		
		if(wx_appid == null || wx_appid == "" || wx_appid.toString().length() == 0){
			return;
		}
		
		String corpid=variables.get("wx_appid").toString();

		//logger.info("======wx_appid:"+corpid);
		String accessToken=WXAppInfo.getCorpAccessTokenInfo(corpid).getAccessToken();
		
		MsgSendResponse resp=corpService.sendMsg(accessToken, templateContent);
		logger.info("====发送企业消息resp:"+resp.getBody());
		
		
	}
}
