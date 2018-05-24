package cn.finder.wae.wx.test;

import org.junit.Test;

public class MyTest {

	@Test
	public void test01()
	{
		/*String url="https://api.weixin.qq.com/cgi-bin/token";
		IClient client =new HttpGetClient(url);
		
		FindAccessTokenRequest req =new  FindAccessTokenRequest();
		req.setGrantType("client_credential");
		req.setAppid("wx0d2e57c1d3fdae5d");
		req.setSecret("6ef69e9a1aa732185a5dda45a2a3a44f");*/
		
	   /* FindAccessTokenResponse resp =	client.execute(req);
	    
	    System.out.println("errcode:"+resp.getErrcode()+",errmsg:"+resp.getErrmsg() +" access_token:"+resp.getAccess_token()+",expires_in:"+resp.getExpires_in());
		*/
		
		/*WXService service=new WXService();
		FindAccessTokenResponse resp =service.findAccessToken("client_credential", "wx0d2e57c1d3fdae5d", "6ef69e9a1aa732185a5dda45a2a3a44f");
		 System.out.println("errcode:"+resp.getErrcode()+",errmsg:"+resp.getErrmsg() +" access_token:"+resp.getAccess_token()+",expires_in:"+resp.getExpires_in());*/
	}
}
