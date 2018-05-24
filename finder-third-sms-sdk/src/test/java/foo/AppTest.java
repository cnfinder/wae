package foo;

import org.junit.Test;

import cn.finder.third.sms.request.JuheSmsRequest;
import cn.finder.third.sms.service.SmsService;

public class AppTest 
{
	
	@Test
	public void testJuhe(){
		
		SmsService service=new SmsService();
		JuheSmsRequest req=new JuheSmsRequest();
		req.setMobile("17715158152");
		service .sendSmsMsg(req);
	}
}
