package cn.finder.wae.activiti.servicetask;

import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.finder.wae.business.domain.TemplateMsg;
import cn.finder.wae.business.module.common.service.CommonService;
import cn.finder.wae.common.comm.templatemsg.WXTemplateMsgSender;
import cn.finder.wae.common.thread.AppContent;

/***
 * 微信通知
 * 接收者: wx_touser_openid
 * 模板消息ID： wx_templatemsg_id
 * 
 * @author whl
 *
 */
public class WXMsgNoticeServiceTask implements JavaDelegate{
	CommonService commonService= WebApplicationContextUtils.getWebApplicationContext(AppContent.getSession().getServletContext()).getBean("commonService", CommonService.class);
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		//应该是获取到当前 服务任务中设置的 变量
	    final Map<String,Object> variables= execution.getVariablesLocal();
	    
	    Object templateMsgCode=variables.get("templateMsgCode");
	    
	    TemplateMsg tm= commonService.findTemplateMsg(templateMsgCode.toString());
	   
	    WXTemplateMsgSender wxTemplateMsgSender=new WXTemplateMsgSender(tm);
	    
	    wxTemplateMsgSender.send(variables);
	}
	
	

}
