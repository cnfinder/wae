package cn.finder.wae.api.sdk.test;
import org.junit.Test;

import cn.finder.wae.api.sdk.DNLocation;
import cn.finder.wae.api.sdk.domain.User;
import cn.finder.wae.api.sdk.service.AuthService;


public class Test01 {
	
	
	@Test
	public void testCheckSession(){
		
		DNLocation.DN_API="http://localhost:8080/labor";
		AuthService service =new AuthService();
		boolean isValid=service.checkSession("1ea2d28397048fbdc4536b7ab733d15b");
		System.out.println(isValid);
		
	}
	
	@Test
	public void testRefreshSession(){
		DNLocation.DN_API="http://localhost:8080/labor";
		AuthService service =new AuthService();
		User user=service.refreshSession("1524f93783c7fcf8641c02bb737fec28");
		if(user!=null){
			System.out.println(user.getSessionId());
		}
		
	}
}
