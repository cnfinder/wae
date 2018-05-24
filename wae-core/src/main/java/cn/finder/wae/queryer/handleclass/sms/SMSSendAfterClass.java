package cn.finder.wae.queryer.handleclass.sms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import cn.finder.third.sms.request.SendSmsRequest;
import cn.finder.third.sms.response.SendSmsResponse;
import cn.finder.third.sms.service.SmsService;
import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.domain.TemplateMsg;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.business.module.common.service.CommonService;
import cn.finder.wae.common.comm.AppApplicationContextUtil;
import cn.finder.wae.common.comm.Common;
import cn.finder.wae.queryer.handleclass.QueryerDBAfterClass;

/**
 * @author: whl
 * @function:短信发送处理类
 */
public class SMSSendAfterClass extends QueryerDBAfterClass {
	
	private Logger logger=Logger.getLogger(SMSSendAfterClass.class);

	@Override
	public TableQueryResult handle(TableQueryResult tableQueryResult,
			long showTableConfigId, QueryCondition<Object[]> condition) {
		 super.handle(tableQueryResult, showTableConfigId, condition);
		 logger.info("===cn.cnxingkong.mk.processor.handler.FindUserInfoAfterClass");
		 @SuppressWarnings("unchecked")
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
			
		List<Map<String,Object>> resultList= new ArrayList<Map<String,Object>>();
		 	
		 Map<String,Object> item=new HashMap<String, Object>();
		 resultList.add(item);
		tableQueryResult.setResultList(resultList);
		 	
	 	tableQueryResult.setPageIndex(condition.getPageIndex());
	 	tableQueryResult.setPageSize(condition.getPageSize());
	 	tableQueryResult.setCount(1l);
		 
		//短信模板 
		String sms_template_code=data.get("sms_template_code").toString();	
    
		
		String phone_number=data.get("phone_number").toString();
		
		CommonService commonService = AppApplicationContextUtil.getContext().getBean("commonService", CommonService.class);;
		TemplateMsg tm = commonService.findTemplateMsg(sms_template_code);
		
		Map<String,Object> variables=new HashMap<String, Object>();
 		
		int valid_code=(int)(1000+Math.random()*8999);
		variables.put("valid_code", valid_code);
		
		String templateContent =tm.getTemplateContent();
		templateContent=Common.descWrapper(templateContent,variables);
		
		
		/*Map<String,String> parameters=new HashMap<String, String>();
		parameters.put("userid", "994");
		parameters.put("account", "HOO");
		parameters.put("password", "456789");
		parameters.put("mobile", phone_number);
		parameters.put("content", templateContent);
		parameters.put("sendTime", "");
		parameters.put("action", "send");
		parameters.put("extno", "");
		
		
		HttpUtil.post("", parameters);*/
		Map<String,Object> smsConfig= commonService.findSmsConfig("001");
		String  corp_id=smsConfig.get("corp_id").toString();
		String  account=smsConfig.get("account").toString();
		String  password=smsConfig.get("password").toString();
		String  field_str=smsConfig.get("field_str_one").toString();
		String  field_str_two=smsConfig.get("field_str_two").toString();
		String  field_str_three=smsConfig.get("field_str_three").toString();
		String  mch_code=smsConfig.get("mch_code").toString();
		
		
		SendSmsRequest req=new SendSmsRequest();
		
		req.setUserid(corp_id);
		req.setAccount(account);
		req.setPassword(password);
		req.setMobile(phone_number);
		req.setContent(templateContent);
		req.setSendTime(field_str_two);
		req.setAction(field_str);
		req.setExtno(field_str_three);
		
		
		SendSmsResponse smsResult= new SmsService().sendSmsMsg(req);
		//SendSmsResponse smsResult=new SendSmsResponse();
		//smsResult.setReturnstatus("Success");
		if(smsResult.getReturnstatus().equalsIgnoreCase("Success")){
			
			
			
			
			
			//插入到数据库
			Map<String,Object> msg=new HashMap<String, Object>();
			
			msg.put("mobille", phone_number);
			msg.put("msg_content", templateContent);
			msg.put("valid_code", valid_code+"");
			msg.put("valid_time", 5*60*1);//单位秒
			
			String msgid=commonService.saveSmsMsg(msg);
			item.put("msgid", msgid);
			item.put("status", "success");
			
		}else{
			
			item.put("status", "fail");
		}
		
		 return tableQueryResult;
		 
	}

	@Override
	public void setJDBCDataSource(DataSource dataSource) {
		super.setJdbcDataSource(dataSource);
	}

	
}
