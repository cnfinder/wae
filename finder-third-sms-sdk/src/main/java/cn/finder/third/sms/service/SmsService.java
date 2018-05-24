package cn.finder.third.sms.service;

import cn.finder.httpcommons.HttpGetClient;
import cn.finder.httpcommons.HttpPostClient;
import cn.finder.httpcommons.IClient;
import cn.finder.httpcommons.parser.IParser;
import cn.finder.httpcommons.parser.XmlParser;
import cn.finder.third.sms.request.JuheSmsRequest;
import cn.finder.third.sms.request.SendSmsRequest;
import cn.finder.third.sms.response.JuheSmsResponse;
import cn.finder.third.sms.response.SendSmsResponse;

public class SmsService {

	
	public SendSmsResponse sendSmsMsg(SendSmsRequest req){
		

		String url="http://120.25.147.10:8002/sms.aspx";
		IClient client =new HttpPostClient(url);
		
		
		
		SendSmsResponse resp=client.execute(req);
		
		resp.setBody(resp.getBody().replace("returnsms", "xml"));
		IParser<SendSmsResponse> parser = new XmlParser<SendSmsResponse>();
		
		
		resp=  parser.parse(resp.getBody(),req.responseClassType());
		
		return resp;
	}
	
	/***
	 * 不支持营销短信
	 * @param req
	 * @return
	 */
	@Deprecated
	public JuheSmsResponse sendSmsMsg(JuheSmsRequest req){
		
		String url="http://v.juhe.cn/sms/send";
		IClient client =new  HttpGetClient(url);
		
		
		JuheSmsResponse resp=client.execute(req);
		
		return resp;
	}
}
