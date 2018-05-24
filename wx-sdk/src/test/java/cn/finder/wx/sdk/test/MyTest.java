package cn.finder.wx.sdk.test;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.junit.Test;

import cn.finder.httpcommons.parser.IParser;
import cn.finder.httpcommons.parser.XmlParser;
import cn.finder.httpcommons.utils.FileItem;
import cn.finder.wx.corp.domain.User;
import cn.finder.wx.corp.request.FindAccessTokenRequest;
import cn.finder.wx.corp.request.FindDepartmentUserRequest;
import cn.finder.wx.corp.request.WXCorpUserAddRequest;
import cn.finder.wx.corp.request.label.FindLabelUserRequest;
import cn.finder.wx.corp.response.FindDepartmentResponse;
import cn.finder.wx.corp.response.FindUserResponse;
import cn.finder.wx.request.ActiveTextMsgSendRequest;
import cn.finder.wx.request.GetQrRequest;
import cn.finder.wx.request.KFTextMsgSendRequest;
import cn.finder.wx.request.MediaAddTempRequest;
import cn.finder.wx.request.MediaListRequest;
import cn.finder.wx.request.WxOrderSearchRequest;
import cn.finder.wx.request.WxUnifiedorderRequest;
import cn.finder.wx.response.FindAccessTokenResponse;
import cn.finder.wx.response.GetQrResponse;
import cn.finder.wx.response.MediaAddTempResponse;
import cn.finder.wx.response.MediaItemForeverImageTextResponse;
import cn.finder.wx.response.MediaListResponse;
import cn.finder.wx.response.WeixinResponse;
import cn.finder.wx.response.WxOrderSearchResponse;
import cn.finder.wx.response.WxUnifiedorderResponse;
import cn.finder.wx.service.WXService;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.sun.istack.internal.logging.Logger;

public class MyTest {

	private Logger logger = Logger.getLogger(MyTest.class);
	
	
	@Test
	public void testActiveTextMsgSend(){
		
		ActiveTextMsgSendRequest txt=new ActiveTextMsgSendRequest("你好hahh");
		
		
	}
	
	@Test
	public void testAddCorpUser(){
		WXService.CorpService service=new WXService.CorpService();
		WXCorpUserAddRequest req=new WXCorpUserAddRequest();
		req.setUserid("user01");
		req.setName("李章");
		req.setMobile("17751451267");
		String accessToken="27o8y2NyuhWfEKMCGpw4sivWmKEf1j2uqRZpzUVxOSzyQ_dSqJNass2Fi_6RlmC0Ybhc1sdx65WjzY1HIqn4Lg";
	    WeixinResponse resp=service.addUser(accessToken, req);
	    
	    logger.info(resp.getBody());
	}
	
	@Test
	public void testFindDepartment(){
		WXService.CorpService service=new WXService.CorpService();
		String accessToken="D98EdzhXckmfznf5zmWP0WqWIpIRhA4ZAJoKCslwLp3eIvGxCUTf2NtYhxgWbhrIq7tEDhCylCkpbBvuegpoeA";
	    FindDepartmentResponse resp=	service.findDepartment(accessToken, null);
	    logger.info("departinfo:"+resp.getBody());
	}
	
	
	
	@Test
	public void testFindUser(){
		WXService.CorpService service=new WXService.CorpService();
		String accessToken="zxvRQUO854yin3ieEhH276IhHuPm6LOsscZnlLawUSdXWsO4oMBg8voVfUqkzQeEmEfO_xLFfQwV5kDT5PXbbA";
		
		FindUserResponse resp= service.findUser(accessToken, "wuhl");
		
		 logger.info("===userinfo:"+resp.getBody());
		
	}
	
	
	
	/***
	 * 测试获取部门用户
	 */
	@Test
	public void testFindDepartmentUsers(){
		WXService.CorpService service=new WXService.CorpService();
		String accessToken="D98EdzhXckmfznf5zmWP0WqWIpIRhA4ZAJoKCslwLp3eIvGxCUTf2NtYhxgWbhrIq7tEDhCylCkpbBvuegpoeA";
		FindDepartmentUserRequest req=new FindDepartmentUserRequest();
		req.setDepartment_id(1);
		req.setFetch_child(FindDepartmentUserRequest.FETCH_CHILD_YES);
		req.setStatus(FindDepartmentUserRequest.STATUS_ALL);
		
	    List<User> userList=service.findDepartmentUsers(accessToken, req);
	    
	    if(userList!=null && userList.size()>0){
	    	logger.info("userList:"+userList.size());
	    }
	    else{
	    	logger.info("userList is 0");
	    }
	    
	}
	
	@Test
	public void testGetQr(){
		
		GetQrRequest req=new GetQrRequest();
		
		req.setAction_name(GetQrRequest.ACTION_NAME_QR_LIMIT_STR_SCENE);
		Map<String,GetQrRequest.Scene> action_info=new  HashMap<String,GetQrRequest.Scene>();
		
		GetQrRequest.Scene scene=new GetQrRequest.Scene();
		scene.setScene_str("dra");
		action_info.put("action_info", scene);
		req.setAction_info(action_info);
		
		WXService wxservice=new WXService();
		String access_token="Zr08aGu0tldr8Nar5n7sdvWDvj89i5uA9khuZMNDXVJZjd7KNaNdQYWxCZZAqlvsHNVUSXVdwDpZgyaWhrnR0druE0zbBhMOGcgKsPdf71LQR-QUS0Vc0Z9IXC0JXQR9ZHAhAEAJRK";
		
		GetQrResponse resp= wxservice.createQr(access_token,req);
		
		if(resp.isSuccess()){
			String ticket=resp.getTicket();
			String url=resp.getUrl();
			
			logger.info("ticket:"+ticket+",url="+url);
			
		}
	}
	
    @Test
	public void testAddMedia(){
    	WXService wxservice=new WXService();
		String access_token="ikuC8n6LCrAHmiQVRGOMojH803FO1bBPLYTA8f07-AjXTHK5-2_RvCWzE0zeAegam3SdwFST59S0DzgjU3pbAoZ1jRouBEwJwYDsuE7xiktXGipkBqM6DTteo02HZWXwLQPhAHAWBA";
		
		
		FileItem fi=new FileItem("C:/a/1.jpg");
		
	    MediaAddTempResponse resp=	wxservice.mediaAdd(access_token, MediaAddTempRequest.MEDIA_TYPE_IMAGE, fi);
	    
		
		
		
	    logger.info(resp.getBody());
	    
	    
	    
	}
    
    /*
     * 
     *=====strDataReq:<xml>
  <parameters defined-in="cn.finder.httpcommons.request.StringDataRequest"/>
  <parameters/>
  <appid>wx09511865ee61a739</appid>
  <body>微信公众号支付</body>
  <mch_id>1358699702</mch_id>
  <nonce_str>81f0c48ed75740e491182a8616476a71</nonce_str>
  <notify_url>http://mk.cnanubis.com/mk/page/mk/pay/test/pay_success.jsp</notify_url>
  <out_trade_no>1468314642827</out_trade_no>
  <total_fee>10</total_fee>
  <trade_type>JSAPI</trade_type>
  <openid>oXkUgwBGkslhkCrHjowuYt8TotNY</openid>
  <attach>微信支付</attach>
  <spbill_create_ip>192.168.1.1</spbill_create_ip>
  <device_info>WEB</device_info>
  <sign>08415A7AAAF64B825AD6CEF1497AA9B7</sign>
</xml>
     */
    @Test
    public void testXML(){
    	WxUnifiedorderRequest wp_data = new WxUnifiedorderRequest();
        wp_data.setAppid("wx09511865ee61a739");
        wp_data.setAttach("微信支付");
        wp_data.setBody("微信公众号支付");
        wp_data.setMch_id("1358699702");
        wp_data.setNonce_str("81f0c48ed75740e491182a8616476a71");
        
        wp_data.setNotify_url("http://mk.cnanubis.com/mk/page/mk/pay/test/pay_success.jsp");
        
        wp_data.setOut_trade_no("1468314642827");
        wp_data.setTotal_fee(1);//单位：分
        wp_data.setTrade_type("JSAPI");
        wp_data.setSpbill_create_ip("192.168.1.1");
        wp_data.setOpenid("oXkUgwBGkslhkCrHjowuYt8TotNY");
        wp_data.setDevice_info("WEB");
        
        
        logger.info("===WxPaySendData:"+wp_data.toString());
        
    	 WXService.WXPayService payService=new WXService.WXPayService();
	      
    	 WxUnifiedorderResponse  resp= payService.unifiedorder(wp_data);
	      
	      logger.info("返回结果:" + resp.getBody());
    }
	
	
    
    
    @Test
    public void testAddMeterial(){
    	WXService service=new WXService();
    	String accessToken="lyCId5R-tOTLtgyATRTkDGNNUlCf77mC_R5Vso_FoxgNpZ6nsbk7rroMVm0TAPhWV6pUeIWYRlsTrCKxHx5ZzWFPfZXmBF7C3_MPEeU69b5e-9k14GHqSbdETjvf-IvBAQYdAGAHXF";
    			
    			
    	
    	String tg_mb_path="d:\\tg_mb1.jpg"; //推广图片模板
		
		BufferedImage tg_mb_img=null; //推广图片模板
		try {
			tg_mb_img = ImageIO.read(new File(tg_mb_path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//int width = tg_mb_img.getWidth(null);
		//int height = tg_mb_img.getHeight(null);
		
		
		
		/*Graphics2D graph = tg_mb_img.createGraphics();
		
		Font font= new Font("宋体", Font.BOLD, 20); 
		graph.setFont(font);
		graph.setColor(Color.BLACK);
		graph.drawString("呜呜呜呜呜呜", 210, 140);
		graph.dispose();*/
		
		//byte[] mediabytes = ((DataBufferByte)tg_mb_img.getData().getDataBuffer()).getData();
		
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		try {
			//FileOutputStream fos=new FileOutputStream(new File("d:\\aa.jpg"));
			//fos.write(mediabytes);
			//fos.close();
			
			JPEGImageEncoder   encoder = JPEGCodec.createJPEGEncoder(baos);     
		    
			encoder.encode(tg_mb_img); 
			 
			 
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
    	
    	//FileItem file=new FileItem(new File("d:\\aa.jpg"));
    	
    	FileItem file3=new FileItem("t1.jpg", baos.toByteArray());
		
		
		
		
		
		
		//FileItem file2=new FileItem("tg1.jpg", mediabytes);
		
    	MediaAddTempResponse resp= service.mediaAdd(accessToken, "image", file3);
    	System.out.println("aaaaa"+resp.getBody());
    	
    	
    	
    	
    }
    
    
    @Test
    public void testKFMsg(){
    	WXService.KFMsgService kfservice=new WXService.KFMsgService();
    	
    	String accessToken="bNjKemUxlrYCRn6BJjRzpDqVQ_LK9YIvki9b1XMCfCp7YR-48J64AdxGYGCo1BbQUFXzr3qQ8VQeDB1FNT9M_hdQaVHMyGs5WC5xjW8TOGlHwBbCC_pc6y58jXzLxOuILHUiADAWSF";
    	KFTextMsgSendRequest txtReq=new KFTextMsgSendRequest();
		txtReq.setTouser("oXkUgwBGkslhkCrHjowuYt8TotNY");
		
		
		KFTextMsgSendRequest.TextData tdata=new KFTextMsgSendRequest.TextData();
		tdata.setContent("请稍后，正在生成推广海报...");
		
		txtReq.setText(tdata);
		
		
		WeixinResponse wxResp=kfservice.sendMsg(accessToken, txtReq);
		logger.info("====kfservice.sendMsg(access_token, txtReq):"+wxResp.getBody());
    	
    }
    
    
    @Test
    public void testMediaList(){
    	
    	WXService service=new WXService();
    	String accessToken="AsnbSrXogufpYiqJF5s1WqjBP8cHgwyE2NqRw6VLMSHP4IANnHEYlS78Ip_jA__sWv_12QWOtGeR26AELVhVWIKNU-rNPHFlH6TG2UaIIKoyCcFG2-zZVLwgoya05bTuOLDeAIASPH";
    	
    	MediaListRequest req=new MediaListRequest();
    	req.setType(MediaListRequest.TYPE_NEWS);
    	req.setOffset(0);
    	req.setCount(20);
    	
    	MediaListResponse resp= service.mediaList(accessToken,req);
    	
    	System.out.println(resp.getBody());
    	
    }
    
    @Test
    public void testMediaItem(){
    	String accessToken="WzijQ0i0QbaVNuii6RZdzFs9j96u47bJC9Xp1MqytjJTN9iaqj0YjDCHeFq1WrCjixDZcIcQtm6pGyAL3mqo-HYpK6UMpZqVEXiinrfArAG3ZPv9mWzVwCdVBOWrmIU5NUUiAGAVCC";
    	
    	String media_id="44h-Z9KxfmGaMod20neDhFPxIvFrMNlXU-Qyo-yBDs0";
    	MediaItemForeverImageTextResponse mediaItemForeverImageTextResponse=new WXService().mediaItemForeverImageText(accessToken, media_id);
    	
    	System.out.println(mediaItemForeverImageTextResponse.getBody());
    	
    	
    }
    @Test
    public void testFindLabelList(){
    	
    	String accessToken="Osj49_zO4sv02XwxOFvOgM3V0I9u9tzvH9_-pRmwtGCIrt5I-p_4rUq5jET6qH69";
    	FindLabelUserRequest req=new FindLabelUserRequest();
		req.setTagid("1");
		List<User> users_tmp = new WXService.CorpService().findLabelUsers(accessToken, req);
    	
		System.out.println(users_tmp);
    }
    
    
    @Test
    public void testXMLParser(){
    	String retStr="<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg><appid><![CDATA[wx735aa1031b93253d]]></appid><mch_id><![CDATA[1368180002]]></mch_id><device_info><![CDATA[WEB]]></device_info><nonce_str><![CDATA[IsOpgg4pt0LRN5VP]]></nonce_str><sign><![CDATA[2FB67436F6AD53597A32A4325FF01AC9]]></sign><result_code><![CDATA[SUCCESS]]></result_code><openid><![CDATA[oeHpfwAm3lOKyc1TaWaMZ_HQ4n80]]></openid><is_subscribe><![CDATA[Y]]></is_subscribe><trade_type><![CDATA[JSAPI]]></trade_type><bank_type><![CDATA[CFT]]></bank_type><total_fee>1</total_fee><fee_type><![CDATA[CNY]]></fee_type><transaction_id><![CDATA[4002442001201610106300291240]]></transaction_id><out_trade_no><![CDATA[OD1476077551920]]></out_trade_no><attach><![CDATA[微信支付]]></attach><time_end><![CDATA[20161010133317]]></time_end><trade_state><![CDATA[SUCCESS]]></trade_state><cash_fee>1</cash_fee></xml>";
    	WxOrderSearchRequest request=new WxOrderSearchRequest();
    	IParser<WxOrderSearchResponse> parser = new XmlParser<WxOrderSearchResponse>();
    	WxOrderSearchResponse resp =  parser.parse(retStr,request.responseClassType());
    	
    	System.out.println(resp.getBody());
    	
    }
    
    @Test
    public void findAccessToken(){
    	
    	WXService service=new WXService();
    	
    	//FindAccessTokenResponse resp= service.findAccessToken("client_credential", "wxab09a1a409fa97f1", "8131cfe6b89a0d630881dd26595e017b");
    	FindAccessTokenResponse resp= service.findAccessToken("client_credential", "wx0d2e57c1d3fdae5d", "6ef69e9a1aa732185a5dda45a2a3a44f");
    	System.out.println(resp.getBody());
    	
    }
}
