package cn.finder.wae.activiti.servicetask;

import java.util.Map;

import nl.justobjects.pushlet.util.Log;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.finder.wae.business.domain.TemplateMsg;
import cn.finder.wae.business.module.common.service.CommonService;
import cn.finder.wae.common.comm.templatemsg.WXCorpTemplateMsgSender;
import cn.finder.wae.common.thread.AppContent;

/***
 * 微信企业文本通知
 * 模板编号:wx_corp_templateMsgCode
 *  touser:user1|user2 竖线隔开
 * @author whl
 *
 */
public class WXCorpTextMsgNoticeServiceTask implements JavaDelegate{
	CommonService commonService= WebApplicationContextUtils.getWebApplicationContext(AppContent.getSession().getServletContext()).getBean("commonService", CommonService.class);
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		//应该是获取到当前 服务任务中设置的 变量
	    final Map<String,Object> variables= execution.getVariablesLocal();
	    
	    Object templateMsgCode=variables.get("wx_corp_templateMsgCode");
	    
	    Object wx_appid = variables.get("wx_appid");
		if(wx_appid == null || wx_appid == "" || wx_appid.toString().length() == 0){
			Log.info("==========wx_appid为空==========");
			return;
		}
	    
	    TemplateMsg tm= commonService.findTemplateMsg(templateMsgCode.toString());
	   
	    WXCorpTemplateMsgSender wxTemplateMsgSender=new WXCorpTemplateMsgSender(tm);
	    String[] users = variables.get("toUserId").toString().split(",");
	    System.out.println("===============微信群发开始=====================");
	    for(String user:users){
	    	variables.put("toUserId", user);
	    	variables.put("task_user_id", user);
	    	Log.info("用户："+variables.get("toUserId").toString());
	    	wxTemplateMsgSender.send(variables);
	    }
	    System.out.println("===============微信群发结束=====================");
	}
	
	

}
