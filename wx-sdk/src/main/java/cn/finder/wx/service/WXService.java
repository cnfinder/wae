package cn.finder.wx.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.finder.httpcommons.HttpGetClient;
import cn.finder.httpcommons.HttpPostClient;
import cn.finder.httpcommons.HttpsGetClient;
import cn.finder.httpcommons.HttpsPostClient;
import cn.finder.httpcommons.HttpsSSLCertClient;
import cn.finder.httpcommons.IClient;
import cn.finder.httpcommons.parser.IParser;
import cn.finder.httpcommons.parser.JsonParser;
import cn.finder.httpcommons.utils.FileItem;
import cn.finder.httpcommons.utils.WebUtils;
import cn.finder.wx.DNLocation;
import cn.finder.wx.app.request.FindSessionKeyRequest;
import cn.finder.wx.app.response.FindSessionKeyResponse;
import cn.finder.wx.corp.domain.Agent;
import cn.finder.wx.corp.domain.Tag;
import cn.finder.wx.corp.domain.User;
import cn.finder.wx.corp.request.FindAgentInfoRequest;
import cn.finder.wx.corp.request.FindAgentsRequest;
import cn.finder.wx.corp.request.FindDepartmentRequest;
import cn.finder.wx.corp.request.FindDepartmentUserRequest;
import cn.finder.wx.corp.request.FindUserIdInfoRequest;
import cn.finder.wx.corp.request.FindUserRequest;
import cn.finder.wx.corp.request.MsgSendBaseRequest;
import cn.finder.wx.corp.request.WXCorpUserAddRequest;
import cn.finder.wx.corp.request.WXCorpUserDeleteRequest;
import cn.finder.wx.corp.request.WXCorpUserUpdateRequest;
import cn.finder.wx.corp.request.label.FindLabelUserRequest;
import cn.finder.wx.corp.request.label.FindLabelsRequest;
import cn.finder.wx.corp.response.FindAgentInfoResponse;
import cn.finder.wx.corp.response.FindAgentInfoResponse.Allow_userinfos;
import cn.finder.wx.corp.response.FindAgentsResponse;
import cn.finder.wx.corp.response.FindDepartmentResponse;
import cn.finder.wx.corp.response.FindDepartmentUserResponse;
import cn.finder.wx.corp.response.FindUserIdInfoResponse;
import cn.finder.wx.corp.response.FindUserResponse;
import cn.finder.wx.corp.response.MsgSendResponse;
import cn.finder.wx.corp.response.label.FindLabelUserResponse;
import cn.finder.wx.corp.response.label.FindLabelsResponse;
import cn.finder.wx.domain.Body;
import cn.finder.wx.domain.WXMenu;
import cn.finder.wx.request.AccessTokenRefreshRequest;
import cn.finder.wx.request.ActiveMsgSendRequest;
import cn.finder.wx.request.AddMenuRequest;
import cn.finder.wx.request.FindAccessTokenRequest;
import cn.finder.wx.request.FindJsapiTicketRequest;
import cn.finder.wx.request.FindOpenIdInfoByCodeRequest;
import cn.finder.wx.request.FindUserInfoLocalRequest;
import cn.finder.wx.request.FindUserInfoRequest;
import cn.finder.wx.request.GetQrRequest;
import cn.finder.wx.request.GetTemplateIdRequest;
import cn.finder.wx.request.KFImageMsgSendRequest;
import cn.finder.wx.request.KFTextMsgSendRequest;
import cn.finder.wx.request.MchPayRequest;
import cn.finder.wx.request.MediaAddTempRequest;
import cn.finder.wx.request.MediaItemForeverImageTextRequest;
import cn.finder.wx.request.MediaListRequest;
import cn.finder.wx.request.SendTempateMsgRequest;
import cn.finder.wx.request.SetIndustryTemplateMsgRequest;
import cn.finder.wx.request.WXMenuRequest;
import cn.finder.wx.request.WXPayRefundRequest;
import cn.finder.wx.request.WeixinRequest;
import cn.finder.wx.request.WxOrderSearchRequest;
import cn.finder.wx.request.WxUnifiedorderRequest;
import cn.finder.wx.response.AccessTokenRefreshResponse;
import cn.finder.wx.response.ActiveMsgSendResponse;
import cn.finder.wx.response.AddMenuResponse;
import cn.finder.wx.response.FindAccessTokenResponse;
import cn.finder.wx.response.FindJsapiTicketResponse;
import cn.finder.wx.response.FindOpenIdInfoByCodeResponse;
import cn.finder.wx.response.FindUserInfoLocalResponse;
import cn.finder.wx.response.FindUserInfoResponse;
import cn.finder.wx.response.GetQrResponse;
import cn.finder.wx.response.GetTemplateIdResponse;
import cn.finder.wx.response.MchPayResponse;
import cn.finder.wx.response.MediaAddTempResponse;
import cn.finder.wx.response.MediaItemForeverImageTextResponse;
import cn.finder.wx.response.MediaListResponse;
import cn.finder.wx.response.SendTempateMsgResponse;
import cn.finder.wx.response.SetIndustryTemplateMsgResponse;
import cn.finder.wx.response.WXPayRefundResponse;
import cn.finder.wx.response.WeixinResponse;
import cn.finder.wx.response.WxOrderSearchResponse;
import cn.finder.wx.response.WxUnifiedorderResponse;

public class WXService {
	
	/***
	 * 用户授权
	 * @param grantType
	 * @param appid
	 * @param secret
	 * https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx0f1c1c22200ecede&secret=78d58d46f6eea517cdd25dd9272bcc83
	 * updated by finder 应对腾讯服务器安全访问 ，使用 客户端使用https调用方式
	 * @return
	 */
	public FindAccessTokenResponse findAccessToken(String grantType,String appid,String secret)
	{
		
		String url="https://"+DNLocation.DN_API+"/cgi-bin/token";
		IClient client =new HttpGetClient(url); //用HttpsGetClient 反而拿不到 报 appid错误
		FindAccessTokenRequest req =new  FindAccessTokenRequest();
		req.setGrantType(grantType);
		req.setAppid(appid);
		req.setSecret(secret);
		
	    FindAccessTokenResponse resp =	client.execute(req);
	    return resp;
		
	}
	
	
	/***
	 * 获取用户OPENID 
	 * @param appId
	 * @param secret
	 * @param code
	 * @return
	 */
	public FindOpenIdInfoByCodeResponse findOpenIdInfoByCode(String appId,String secret,String code){
		String url="https://"+DNLocation.DN_API+"/sns/oauth2/access_token";
		IClient client =new HttpGetClient(url);//
		FindOpenIdInfoByCodeRequest req =new  FindOpenIdInfoByCodeRequest();
		
		req.setAppId(appId);
		req.setAppSecret(secret);
		req.setCode(code);
		
		FindOpenIdInfoByCodeResponse resp = client.execute(req);
		
		return resp;
		
		
	}
	
	
	/***
	 * 刷新access_token 由于access_token拥有较短的有效期，当access_token超时后，可以使用refresh_token进行刷新，refresh_token拥有较长的有效期（7天、30天、60天、90天），
	 * 当refresh_token失效的后，需要用户重新授权
	 * @param appId
	 * @param refreshToken
	 * @return
	 */
	public AccessTokenRefreshResponse accessTokenRefresh(String appId,String refreshToken){
		
		String url="https://"+DNLocation.DN_API+"/sns/oauth2/refresh_token";
		IClient client =new HttpGetClient(url);//用HttpsGetClient 反而拿不到 报 appid错误
		AccessTokenRefreshRequest req =new  AccessTokenRefreshRequest();
		
		req.setAppId(appId);
		req.setRefreshToken(refreshToken);
		
		AccessTokenRefreshResponse resp = client.execute(req);
		
		return resp;
	}
	
	
	
	/***
	 * 网页通过access_token拉取用户信息(仅限scope= snsapi_userinfo)：
	 * @param accessToken 通过 appid, appsecret, wx_code获取
	 * @param openId
	 * @return
	 */
	public FindUserInfoResponse findUserInfo(String accessToken,String openId)
	{
		String url="https://"+DNLocation.DN_API+"/sns/userinfo";
		
		IClient client =new HttpGetClient(url);//用HttpsGetClient 反而拿不到 报 appid错误
		FindUserInfoRequest req =new  FindUserInfoRequest();
		
		req.setAccessToken(accessToken);
		req.setOpenId(openId);
		
		FindUserInfoResponse resp = client.execute(req);
		
		return resp;
		
	}
	
	/***
	 * 获取用户基本信息（包括UnionID机制）
	 * @param accessToken
	 * @param openId
	 * @return
	 */
	public FindUserInfoLocalResponse findUserInfoLocal(String accessToken,String openId)
	{
		String url="https://"+DNLocation.DN_API+"/cgi-bin/user/info";
		
		IClient client =new HttpGetClient(url);
		FindUserInfoLocalRequest req =new  FindUserInfoLocalRequest();
		
		req.setAccessToken(accessToken);
		req.setOpenId(openId);
		
		FindUserInfoLocalResponse resp = client.execute(req);
		
		return resp;
		
	}
	
	/***
	 * 添加微信菜单到微信服务器
	 * 直接把$json POST到微信的服务器，不要做任何处理
	 * @param menus
	 * @return
	 */
	@Deprecated
	public AddMenuResponse addMenu(String accessToken,List<WXMenu> menus){
		String url="https://"+DNLocation.DN_API+"/cgi-bin/menu/create?access_token="+accessToken;
		
		IClient client =new HttpsPostClient(url);
		AddMenuRequest req =new  AddMenuRequest();
		
		Body body=new Body();
		body.setButton(menus);
		req.setBody(body);
		//验证json
		/*ObjectMapper mapper = new ObjectMapper();
		String json = null;
        try {
			json=mapper.writeValueAsString(req);
			System.out.println(json);
		} catch (IOException e) {
			e.printStackTrace();
		} */
		AddMenuResponse resp = client.execute(req);
		return resp;
		
	}
	
	
	/***
	 * 添加微信菜单
	 * @param accessToken
	 * @param req
	 * @return
	 */
	public boolean addWXMenus(String accessToken,WXMenuRequest req){
		String url="https://"+DNLocation.DN_API+"/cgi-bin/menu/create?access_token="+accessToken;
		
		IClient client =new HttpsPostClient(url);
		
		WeixinResponse resp= client.execute(req);
		if(resp.isSuccess()){
			return true;
		}
		return false;
		
	}
	
	
	
	
	/***
	 * 设置模板所属行业
	 * @param accessToken
	 * @return
	 */
	public boolean setIndustry(String accessToken,String industry_id1,String industry_id2){
		String url="https://"+DNLocation.DN_API+"/cgi-bin/template/api_set_industry?access_token="+accessToken;
		
		IClient client =new HttpsPostClient(url);
		SetIndustryTemplateMsgRequest req=new SetIndustryTemplateMsgRequest();
		req.setIndustry_id1(industry_id1);
		req.setIndustry_id2(industry_id2);
		
	    SetIndustryTemplateMsgResponse resp=	client.execute(req);
	    
	    
	    return true;
		
	}
	
	
	
	/***
	 * 获取模板ID, 每次获取到的模板ID 都不一样 而且经常获取不到，应该直接使用后台的模板ID
	 * {"errcode":45027,"errmsg":"template conflict with industry"}
	 * @param template_id_short  模板库中模板的编号，有“TM**”和“OPENTMTM**”等形式
	 * @return
	 */
	@Deprecated
	public String findTemplateIdTemplateMsg(String accessToken,String template_id_short){
		
		String url="https://"+DNLocation.DN_API+"/cgi-bin/template/api_add_template?access_token="+accessToken;
		
		IClient client =new HttpsPostClient(url);
		GetTemplateIdRequest req=new GetTemplateIdRequest();
		req.setTemplate_id_short(template_id_short);
		
	    GetTemplateIdResponse resp=	client.execute(req);
	    
	    return resp.getTemplate_id();
	    
		
	}
	
	
	/***
	 * 发送模板消息
	 * @param accessToken
	 * @param templateMsg
	 * @return
	 */
	public SendTempateMsgResponse sendTempateMsg(String accessToken,SendTempateMsgRequest req){
		String url="https://"+DNLocation.DN_API+"/cgi-bin/message/template/send?access_token="+accessToken;
		
		IClient client =new HttpsPostClient(url);
		SendTempateMsgResponse resp=client.execute(req);
	    
	    return resp;
	}
	
	
	/***
	 * 微信端主动发送消息
	 * 参考:http://www.cnblogs.com/wuhuacong/p/3995494.html
	 * @param accessToken
	 * @param req  主动消息体
	 * @return
	 */
	public ActiveMsgSendResponse activeMsgSend(String accessToken,ActiveMsgSendRequest req){
		String url="https://qy"+DNLocation.DN_API+"/cgi-bin/message/send?access_token="+accessToken;
		IClient client =new HttpsPostClient(url);
		ActiveMsgSendResponse resp=client.execute(req);
	    
	    return resp;
	}
	
	
	/***
	 * 获取Jsapi ticket
	 * @param accessToken
	 * @return
	 */
	public FindJsapiTicketResponse findJsapiTicket(String accessToken){
		String url="https://"+DNLocation.DN_API+"/cgi-bin/ticket/getticket?access_token="+accessToken+"&type=jsapi";
		IClient client =new HttpGetClient(url);
		FindJsapiTicketRequest req=new FindJsapiTicketRequest();
		FindJsapiTicketResponse resp=client.execute(req);
	    return resp;
	}
	
	/*
	 * 
	 获取微信临时素材
	 * 
	 */
	public String mediaGet(String accessToken,String mediaId){
		String url="https://"+DNLocation.DN_API+"/cgi-bin/media/get?access_token="+accessToken+"&media_id="+mediaId;
		return url;
	}
	
	
	
	/***
	 * 新增临时素材 
	 * @param accessToken
	 * @param type
	 * @param media  注意微信接口上传的filename一定要是有 扩展名  如m.jpg 否自 40005
	 * @return
	 */
	public MediaAddTempResponse mediaAdd(String accessToken,String type,FileItem media)
	{
		//String url="https://"+DNLocation.DN_API+"/cgi-bin/media/upload?access_token="+accessToken+"&type=image";
		//String url="http://api.weixin.qq.com/cgi-bin/material/add_material?access_token="+accessToken+"&type=image";
		String url="http://file.api.weixin.qq.com/cgi-bin/media/upload?access_token="+accessToken+"&type=image";
		MediaAddTempRequest req=new MediaAddTempRequest();
		/*IClient client =new HttpPostClient(url);
		MediaAddTempRequest req=new MediaAddTempRequest();
		//req.setType(type);
		
		req.setMedia(media);
		MediaAddTempResponse resp=client.execute(req);
		return resp;*/
		
		WebUtils webUtils=new WebUtils();
		
	  // String resstr=webUtils.post(url, null, null, null, new FileItem[]{media});
		Map<String,FileItem> files=new HashMap<String,FileItem>();
		files.put(media.getFileName(), media);
		
		//String retStr=webUtils.doPostMultipartHttps(url, null, files);
		
		
		String retStr="{}";
		try {
			retStr = webUtils.post(url, null, null, null, new FileItem[]{media});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 IParser<MediaAddTempResponse> parser = new JsonParser<MediaAddTempResponse>();
         
		 MediaAddTempResponse resp =  parser.parse(retStr,req.responseClassType());
		
			
		return resp;
		
		
	}
	
	
	/***
	 * 查询永久素材接口
	 * @param accessToken
	 * @param req
	 * @return
	 */
	public MediaListResponse mediaList(String accessToken,MediaListRequest req){
		
		String url="https://"+DNLocation.DN_API+"/cgi-bin/material/batchget_material?access_token="+accessToken;
		
		IClient client =new HttpsPostClient(url);
		
		MediaListResponse resp=client.execute(req);
		
		return resp;
		
	}
	
	
	
	
	/*
	 * 
	 获取微信永久素材文件素材
	 * 
	 */
	public String mediaForeverGet(String accessToken,String mediaId){
		String url="https://"+DNLocation.DN_API+"/cgi-bin/material/get_material?access_token="+accessToken+"&media_id="+mediaId;
		return url;
	}
	
	
	/***
	 * 获取 永久素材 图文消息
	 * @param accessToken
	 * @param mediaId
	 * @return
	 */
	public MediaItemForeverImageTextResponse mediaItemForeverImageText(String accessToken,String mediaId){
		
		String url="https://"+DNLocation.DN_API+"/cgi-bin/material/get_material?access_token="+accessToken;
		IClient client =new HttpsPostClient(url);
		MediaItemForeverImageTextRequest req=new MediaItemForeverImageTextRequest();
		req.setMedia_id(mediaId);
		MediaItemForeverImageTextResponse resp= client.execute(req);
		
		return resp;
		
		
	}
	

	
	/***
	 * 对公众号的所有api调用（包括第三方帮其调用）次数进行清零
	 * @param accessToken
	 * @param appid 
	 * @return
	 */
	public boolean clearQuota(String accessToken,String appid){
		
		String url="https://"+DNLocation.DN_API+"/cgi-bin/clear_quota?access_token="+accessToken;
		IClient client =new HttpPostClient(url);
		WeixinRequest<WeixinResponse> req=new WeixinRequest<WeixinResponse>();
		
		WeixinResponse resp=client.execute(req);
		if(resp.isSuccess())
			return true;
	    return false;
	}
	
	
	
	/***
	 * 获取推广二维码
	 * @param req
	 * @return
	 */
	public GetQrResponse createQr(String accessToken,GetQrRequest req){
		
		String url="https://"+DNLocation.DN_API+"/cgi-bin/qrcode/create?access_token="+accessToken;
		
		IClient client =new HttpsPostClient(url);
		GetQrResponse resp=client.execute(req);
	    
	    return resp;
	}
	
	
	/***
	 * 获取二维码ticket后，开发者可用ticket换取二维码图片。请注意，本接口无须登录态即可调用。
	 * ticket正确情况下，http 返回码是200，是一张图片，可以直接展示或者下载。
	 * 错误情况下（如ticket非法）返回HTTP错误码404。
	 * @param ticketStr
	 * @return
	 */
	public String findQrImage(String ticketStr){
		//https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=TICKET提醒：TICKET记得进行UrlEncode
		try {
			String url="https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+URLEncoder.encode(ticketStr,"utf-8");
			return url;
			
		} catch (UnsupportedEncodingException e) {
			
		}
		return "";
		
	}
	
	
	
	/***
	 * 微信支付相关接口
	 * @author Administrator
	 *
	 */
	public static class  WXPayService{
		
		/***
		 * 微信支付统一下单接口
		 * @param req
		 * @return
		 */
		public WxUnifiedorderResponse unifiedorder(WxUnifiedorderRequest req){
			
			String url="https://api.mch.weixin.qq.com/pay/unifiedorder";
			IClient client =new HttpsPostClient(url,"xml");
			
			
			WxUnifiedorderResponse resp=client.execute(req);
			return resp;
		}
		
		
		
		

		/***
		 * 企业付款
		 * @param req
		 * @return
		 */
		public MchPayResponse mchPay(String certPwd,String certFilePath,MchPayRequest req){
			
			String url="https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";
			
			HttpsSSLCertClient client=new HttpsSSLCertClient(url,"xml");
			client.setCertFilePath(certFilePath);
			client.setCertPwd(certPwd);
			client.setMethod("POST");
			MchPayResponse  resp=client.execute(req);
			
			return resp;
		}
		
		/***
		 * 微信支付订单查询接口
		 * @param req
		 * @return
		 */
		public WxOrderSearchResponse orderSearch(WxOrderSearchRequest req){
			
			String url="https://api.mch.weixin.qq.com/pay/orderquery";
			IClient client =new HttpsPostClient(url,"xml");
			
			
			WxOrderSearchResponse resp=client.execute(req);
			return resp;
		}
		
		
		
		/***
		 * 申请退款
		 * @param req
		 * @return
		 */
		public WXPayRefundResponse payRefund(String certPwd,String certFilePath,WXPayRefundRequest req){
			
			String url="https://api.mch.weixin.qq.com/secapi/pay/refund";
			
			HttpsSSLCertClient client=new HttpsSSLCertClient(url,"xml");
			client.setCertFilePath(certFilePath);
			client.setCertPwd(certPwd);
			client.setMethod("POST");
			WXPayRefundResponse  resp=client.execute(req);
			
			return resp;
		}
		
	}
	
	
	
	/***
	 * 微信客服接口消息
	 * @author Administrator
	 *
	 */
	public static class  KFMsgService{
		
		/***
		 * 发送文本消息
		 * @param accessToken
		 * @param req
		 * @return
		 */
		public WeixinResponse sendMsg(String accessToken,KFTextMsgSendRequest req){
			
			String url="https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+accessToken;
			
			IClient client =new HttpPostClient(url);
			
			WeixinResponse resp=client.execute(req);
			
			return resp;
			
		}
		
		

		/***
		 * 发送图片消息
		 * @param accessToken
		 * @param req
		 * @return
		 */
		public WeixinResponse sendMsg(String accessToken,KFImageMsgSendRequest req){
			
			String url="https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+accessToken;
			
			IClient client =new HttpPostClient(url);
			
			WeixinResponse resp=client.execute(req);
			
			return resp;
			
		}
		
		
		
	}
	
	
	
	
	/***
	 * 企业号服务接口
	 * @author whl
	 *
	 */
	public static class CorpService{
		
		/***
		 * 获取企业号的 access token
		 * @param corpid
		 * @param corpsecret
		 * @return
		 */
		public cn.finder.wx.corp.response.FindAccessTokenResponse findAccessToken(String corpid,String corpsecret)
		{
			String url="https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid="+corpid+"&corpsecret="+corpsecret;
			IClient client =new HttpGetClient(url); 
			cn.finder.wx.corp.request.FindAccessTokenRequest req =new  cn.finder.wx.corp.request.FindAccessTokenRequest();
			
			req.setCorpid(corpid);
			req.setCorpsecret(corpsecret);
			
		    cn.finder.wx.corp.response.FindAccessTokenResponse resp =	client.execute(req);
		    return resp;
			
		} 
		
		/***
		 * 获取本部或子部门
		 * @param accessToken
		 * @param departmentId if null, find all departments
		 * @return
		 */
		public FindDepartmentResponse findDepartment(String accessToken,Integer departmentId){
			
			String url="https://qyapi.weixin.qq.com/cgi-bin/department/list?access_token="+accessToken;
			if(departmentId!=null){
				url+="&id="+departmentId.intValue();
			}
			
			IClient client =new HttpGetClient(url);
			FindDepartmentRequest req=new FindDepartmentRequest();
			
			FindDepartmentResponse resp =client.execute(req);
			
			return resp;
			
		}
		
		
		/***
		 * 根据CODE获取用户USERID
		 * @param accessToken
		 * @param code
		 * @return
		 */
		public FindUserIdInfoResponse findUserIdInfo(String accessToken,String code){
			String url="https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token="+accessToken+"&code="+code;
			IClient client =new HttpGetClient(url);
			FindUserIdInfoRequest req=new FindUserIdInfoRequest();
			FindUserIdInfoResponse resp= client.execute(req);
			return resp;
			
		}
		
		/***
		 * 获取Jsapi ticket
		 * @param accessToken
		 * @return
		 */
		public cn.finder.wx.corp.response.FindJsapiTicketResponse findJsapiTicket(String accessToken){
			String url="https://qyapi.weixin.qq.com/cgi-bin/get_jsapi_ticket?access_token="+accessToken;
			IClient client =new HttpGetClient(url);
			cn.finder.wx.corp.request.FindJsapiTicketRequest req=new cn.finder.wx.corp.request.FindJsapiTicketRequest();
			cn.finder.wx.corp.response.FindJsapiTicketResponse resp=client.execute(req);
		    return resp;
		}
		
		
		/*
		 * 
		 获取微信多媒体文件
		 * 
		 */
		public String mediaGet(String accessToken,String mediaId){
			String url="https://qyapi.weixin.qq.com/cgi-bin/media/get?access_token="+accessToken+"&media_id="+mediaId;
			return url;
		}
		
		
		
		
		/***
		 * 发送消息 各种消息
		 * @param msgSendReq
		 * @return
		 */
		public MsgSendResponse sendMsg(String accessToken,MsgSendBaseRequest msgSendReq){
			String url="https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token="+accessToken;
			IClient client =new HttpPostClient(url);
			MsgSendResponse resp=client.execute(msgSendReq);
		    return resp;
			
		}
		
		

		/***
		 * 发送消息 各种消息
		 * @param msgSendReq
		 * @return
		 */
		public MsgSendResponse sendMsg(String accessToken,String body){
			String url="https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token="+accessToken;
			WebUtils webUtils=new WebUtils();
			String retStr=webUtils.doPostData(url, body);
			// IParser<MsgSendResponse> parser = new JsonParser<MsgSendResponse>();
			//MsgSendResponse resp =  parser.parse(retStr,MsgSendResponse.class);
			 MsgSendResponse resp =new MsgSendResponse();
			 resp.setBody(retStr);
		    return resp;
			
		}
		
		
		/***
		 * 添加用户
		 * @param accessToken
		 * @return
		 */
		public WeixinResponse addUser(String accessToken,WXCorpUserAddRequest req){
			String url="https://qyapi.weixin.qq.com/cgi-bin/user/create?access_token="+accessToken;
			IClient client =new HttpsPostClient(url);
			WeixinResponse resp=client.execute(req);
		    return resp;
		}
		
		
		/***
		 * 添加用户
		 * @param accessToken
		 * @return
		 */
		public WeixinResponse updateUser(String accessToken,WXCorpUserUpdateRequest req){
			String url="https://qyapi.weixin.qq.com/cgi-bin/user/update?access_token="+accessToken;
			IClient client =new HttpsPostClient(url);
			WeixinResponse resp=client.execute(req);
		    return resp;
		}
		
		
		/***
		 * 删除用户
		 * @param accessToken
		 * @return
		 */
		public WeixinResponse deleteUser(String accessToken,String userid){
			String url="https://qyapi.weixin.qq.com/cgi-bin/user/delete?access_token="+accessToken+"&userid="+userid;
			IClient client =new HttpGetClient(url);
			
			
			WXCorpUserDeleteRequest req=new WXCorpUserDeleteRequest();
			WeixinResponse resp=client.execute(req);
		    return resp;
		}
		
		
		/***
		 * 根据userid获取 用户信息
		 * @param accessToken
		 * @return
		 */
		public FindUserResponse findUser(String accessToken,String userid){
			String url="https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token="+accessToken+"&userid="+userid;
			IClient client =new HttpGetClient(url);
			FindUserRequest req=new FindUserRequest();
			FindUserResponse resp=client.execute(req);
		    return resp;
		}
		
		
		/***
		 * 获取部门下的用户
		 * @param accessToken
		 * @param req
		 * @return
		 */
		public List<User> findDepartmentUsers(String accessToken,FindDepartmentUserRequest req){
			
			String url="https://qyapi.weixin.qq.com/cgi-bin/user/simplelist?access_token="+accessToken;
			IClient client =new HttpGetClient(url);
			FindDepartmentUserResponse resp=client.execute(req);
			if(resp.isSuccess())
				return resp.getEntities();
		    return null;
		}
		
		/***
		 * 获取部门下的用户详细
		 * @param accessToken
		 * @param req
		 * @return
		 */
		public List<User> findDetailDepartmentUsers(String accessToken,FindDepartmentUserRequest req){
			
			String url="https://qyapi.weixin.qq.com/cgi-bin/user/list?access_token="+accessToken;
			IClient client =new HttpGetClient(url);
			FindDepartmentUserResponse resp=client.execute(req);
			if(resp.isSuccess())
				return resp.getEntities();
		    return null;
		}
		
		/***
		 * 获取 应用
		 * @param accessToken
		 * @param req
		 * @return
		 */
		public List<Agent> findAgents(String accessToken){
			
			String url="https://qyapi.weixin.qq.com/cgi-bin/agent/list?access_token="+accessToken;
			IClient client =new HttpGetClient(url);
			FindAgentsRequest req=new FindAgentsRequest();
			
			FindAgentsResponse resp=client.execute(req);
			if(resp.isSuccess())
				return resp.getEntities();
		    return null;
		}
		
		/***
		 * 获取企业号应用详细信息
		 * @param accessToken
		 * @param req
		 * @return
		 */
		public FindAgentInfoResponse findAgentInfo(String accessToken,String agentid){
			
			String url="https://qyapi.weixin.qq.com/cgi-bin/agent/get?access_token="+accessToken+"&agentid="+agentid;
			IClient client =new HttpGetClient(url);
			FindAgentInfoRequest req=new FindAgentInfoRequest();
			
			FindAgentInfoResponse resp=client.execute(req);
			if(resp.isSuccess())
				return resp;
		    return null;
		}
		
		
		/***
		 * 获取用户授权的应用
		 * @param accessToken
		 * @param req
		 * @return
		 */
		public List<Agent> findAgentsForUser(String accessToken,String userId){
			
			List<Agent> auth_agents=new ArrayList<Agent>();
			List<Agent> agents=findAgents(accessToken);
			
			if(agents!=null && agents.size()>0){
				for(int i=0;i<agents.size();i++){
					Agent agent=agents.get(i);
				    FindAgentInfoResponse agentInfoResp=findAgentInfo(accessToken,agent.getAgentid());
				    
				    Allow_userinfos  allow_userinfos=agentInfoResp.getAllow_userinfos();
				    if(allow_userinfos!=null){
				        List<cn.finder.wx.corp.response.FindAgentInfoResponse.Allow_userinfos.User> allow_user=allow_userinfos.getUser();
				        
				        if(allow_user!=null&&allow_user.size()>0){
				        	auth_agents.add(agent);
				        	continue;
				        }
				        
				    }
					
				}
			}
			
			return auth_agents;
			
		}
		/***
		 * 获取标签下的用户
		 * @param accessToken
		 * @param req
		 * @return
		 */
		public List<User> findLabelUsers(String accessToken,FindLabelUserRequest req){
			String url="https://qyapi.weixin.qq.com/cgi-bin/tag/get?access_token="+accessToken;
			IClient client =new HttpGetClient(url);
			FindLabelUserResponse resp=client.execute(req);
			if(resp.isSuccess())
				return resp.getEntities();
		    return null;
		}
		
	
		/***
		 * 获取所有标签
		 * @param accessToken
		 * @param req
		 * @return
		 */
		public List<Tag> findLabels(String accessToken){
			String url="https://qyapi.weixin.qq.com/cgi-bin/tag/list?access_token="+accessToken;
			IClient client =new HttpGetClient(url);
			FindLabelsRequest req=new FindLabelsRequest();
			FindLabelsResponse resp=client.execute(req);
			if(resp.isSuccess())
				return resp.getEntities();
		    return null;
		}
		
		
		
		
	}
	
	
	
	public static class  WXAppService{
		
		/***
		 * code 换取 session_key
​ 这是一个 HTTPS 接口，开发者服务器使用登录凭证 code 获取 session_key 和 openid。其中 session_key 是对用户数据进行加密签名的密钥。为了自身应用安全，session_key 不应该在网络上传输。
		 * @param req
		 * @return
		 */
		public FindSessionKeyResponse findSessionKeyResponse(FindSessionKeyRequest req){
			
			
			//String url="https://"+DNLocation.DN_API+"/sns/jscode2session?appid="+req.getAppid()+"&secret="+req.getSecret()+"&js_code="+req.getJs_code()+"&grant_type="+req.getGrant_type();
			String url="https://"+DNLocation.DN_API+"/sns/jscode2session";
			IClient client =new HttpGetClient(url);
			
			FindSessionKeyResponse resp=client.execute(req);
			return resp;
			
			
		}
		
		
		
		
		
	}
	
	
	public static void main(String[] args) {
		
		WXService.CorpService service=new WXService.CorpService();
		/*WXCorpUserAddRequest req=new WXCorpUserAddRequest();
		req.setUserid("addafasdf");
		req.setName("李章");
		req.setMobile("17751451237");
		req.setDepartment(new int[]{1});
		String accessToken="27o8y2NyuhWfEKMCGpw4sivWmKEf1j2uqRZpzUVxOSzyQ_dSqJNass2Fi_6RlmC0Ybhc1sdx65WjzY1HIqn4Lg";
	    WeixinResponse resp=service.addUser(accessToken, req);*/
		
		String accessToken="27o8y2NyuhWfEKMCGpw4sivWmKEf1j2uqRZpzUVxOSzyQ_dSqJNass2Fi_6RlmC0Ybhc1sdx65WjzY1HIqn4Lg";
		//WeixinResponse resp=service.deleteUser(accessToken, "wangjp");
	    
		WeixinResponse resp=service.findUser(accessToken, "config_admin");
		
	    System.out.println(resp.getBody());
	}
	
}
