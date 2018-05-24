package cn.finder.wae.wx.servlet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.cnxingkong.stc.token.service.STCService;
import cn.finder.wae.business.domain.TaskPlan;
import cn.finder.wae.business.domain.wx.AppInfo;
import cn.finder.wae.business.domain.wx.corp.CorpAdminGroup;
import cn.finder.wae.business.module.common.service.CommonService;
import cn.finder.wae.wx.data.CorpTokenInfo;
import cn.finder.wae.wx.data.WXApp;
import cn.finder.wae.wx.data.WXAppInfo;
import cn.finder.wx.response.FindAccessTokenResponse;
import cn.finder.wx.response.FindJsapiTicketResponse;
import cn.finder.wx.service.WXService;


/***
 * 定时刷新 token
 * @author whl
 *
 */
public class AccessTokenRefreshServlet extends HttpServlet {

	private static Logger logger = Logger.getLogger(AccessTokenRefreshServlet.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 8847503911589250340L;

	
	CommonService commonService=null;
	
	private ServletContext ctxt_p;
	
	List<TaskPlanExecutor> taskPlanExecutorPool;
	//定时调度器
	Timer taskTimer = new Timer(); 
	
	
	public void init(ServletConfig servletConfig) throws ServletException {
		
		ServletContext ctxt=servletConfig.getServletContext();
		ctxt_p=ctxt;
		String enabled = servletConfig.getInitParameter("enabled");
		if("true".equalsIgnoreCase(enabled)){
			logger.debug(" AccessTokenRefreshServlet.init  start....  ");
			
			
			try {
				//休眠  以便让缓存先加载好 
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			commonService =WebApplicationContextUtils.getWebApplicationContext(ctxt).getBean("commonService",CommonService.class);
			new AccessTokenRefreshController(commonService).start();
			
			logger.info("======access_token 刷新服务启动成功");
		}
		
		
		
	}
	
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		super.destroy();
		
		taskTimer.cancel();
	}


	/*
	 * 凭证刷新线程
	 * 
	 */
	class AccessTokenRefreshController extends Thread
	{
		
		
		private CommonService commonService;
		
		private long duration=60*60*1000/2;//每 30分钟
		
		
		public AccessTokenRefreshController(CommonService commonService)
		{
			this.commonService = commonService;
			taskPlanExecutorPool = (List<TaskPlanExecutor>) Collections.synchronizedList(new ArrayList<AccessTokenRefreshServlet.TaskPlanExecutor>());
			
		}

		@Override
		public void run() {
			
			
			while(true)
			{
				try
				{
	
					
					logger.info("======开始查询TOKEN");
					
					//================更新企业号票据 start=================//
					try{
						
					    List<CorpAdminGroup> corpAdminGroups=commonService.findCorpAdminGroup();
						
						if(corpAdminGroups!=null &&corpAdminGroups.size()>0)
						{
							WXService.CorpService corpService=new WXService.CorpService();
							
							int len=corpAdminGroups.size();
							
							logger.info("======corpAdminGroups.size():"+corpAdminGroups.size());
							for(int i=0;i<len;i++)
							{
								
								CorpAdminGroup corpAdminGroup=corpAdminGroups.get(i);
								
							
								/*
							
								String corpid=corpAdminGroup.getCorp_id();
								String corpsecret=corpAdminGroup.getSecret();
								
							    cn.finder.wx.corp.response.FindAccessTokenResponse resp=null;
							   
							    resp= corpService.findAccessToken(corpid,corpsecret);
							    
							    logger.info("企业号:"+corpAdminGroup.getCorpinfo_name()+"====Corp FindAccessTokenResponse:"+resp.getBody());
							    //if(StringUtils.isEmpty(resp.getErrcode())){
							    if("0".equals(resp.getErrcode())){
							    	//调用成功
							    	if(resp.getExpires_in()<=60*60*2){
							    		//在7200 之内  那么刷新 accessToken
							    		
							    		
							    		
							    	}
							    	
							    	
							    	//加入集合
							    	CorpTokenInfo corpTokenInfo=new CorpTokenInfo();
							    	corpTokenInfo.setCorpid(corpid);
							    	corpTokenInfo.setCorpsecret(corpsecret);
							    	corpTokenInfo.setAccessToken(resp.getAccess_token());
							    	corpTokenInfo.setExpiresIn(resp.getExpires_in());
							    	
							    	corpTokenInfo.setAdminGroupTypeId(corpAdminGroup.getAdminGroupTypeId());
							    	corpTokenInfo.setAdminGroupTypeName(corpAdminGroup.getAdminGroupTypeName());
							    	
							    	corpTokenInfo.setAdminGroupTypeCode(corpAdminGroup.getAdminGroupTypeCode());
							    	
							    	
							    	
							    	cn.finder.wx.corp.response.FindJsapiTicketResponse findJsapiTicketResponse=null;
							    	
							        try{
							        	//获取 jsapi ticket
							        	findJsapiTicketResponse=corpService.findJsapiTicket(corpTokenInfo.getAccessToken());
							        	 logger.info("企业号:"+corpAdminGroup.getCorpinfo_name()+"====Corp FindJsapiTicketResponse:"+findJsapiTicketResponse.getBody());
								        if("0".equalsIgnoreCase(findJsapiTicketResponse.getErrcode())){
								        	corpTokenInfo.setJsapi_ticket(findJsapiTicketResponse.getTicket());
								    		
								    	}
								    	else{
								    		String oldTicket=WXAppInfo.getCorpAccessTokenInfo(corpid, corpsecret).getJsapi_ticket();
								    		corpTokenInfo.setJsapi_ticket(oldTicket);
								    	}
								        logger.info("企业号:"+corpAdminGroup.getCorpinfo_name()+"=====corpTokenInfo.getJsapi_ticket:"+corpTokenInfo.getJsapi_ticket());
							        }
							        catch(Throwable e){
							        	logger.error(e);
							        }
							    	
							        
							        try{
							        	 //加载应用到 corpTokenInfo
								        
								        List<Map<String,Object>> apps= commonService.findWxApp(corpid);
								        logger.info("=====apps len: "+apps.toString());
								        if(apps!=null&&apps.size()>0){
								        	for(Map<String,Object> app:apps){
								        		try{
								        			
									        		WXApp wxapp=new WXApp();
									        		wxapp.setId(app.get("id").toString());
									        		wxapp.setAgentid(app.get("agentid").toString());
									        		wxapp.setSecret(app.get("secret").toString());
									        		wxapp.setWx_appid(app.get("wx_appid").toString());
									        		wxapp.setIs_msg_agent(app.get("is_msg_agent").toString());
									        		corpTokenInfo.getWxApps().add(wxapp);
								        		}
								        		catch(Throwable e){
								        			logger.error("===加载  agentid "+app.get("agentid").toString()+"");
								        		}
								        	}
								        }
								        
							        }
							        catch(Exception ee){
							        	logger.error("====加载企业号应用失败...");
							        }
							       
							        logger.info("===="+corpAdminGroup.getCorpinfo_name()+" 消息应用 "+corpTokenInfo.getMsgAgentId());
							    	
							        commonService.updateWxCorpTokenStatus(corpid, "Y");
							        
							    	WXAppInfo.updateCorpAccessTokenInfo(corpid, corpsecret, corpTokenInfo);
							    	
							    	
							    }else{
							    	logger.error("===获取企业号access token 失败");
							    	commonService.updateWxCorpTokenStatus(corpid, "N");
							    }
							    */
								
								
								//---------------------新升级版本-------------------------
								
								String corpid=corpAdminGroup.getCorp_id();
								String corpsecret=corpAdminGroup.getSecret();
								
								//加入集合
						    	CorpTokenInfo corpTokenInfo=new CorpTokenInfo();
						    	corpTokenInfo.setCorpid(corpid);
						    	corpTokenInfo.setCorpsecret(corpsecret);
						    	corpTokenInfo.setAdminGroupTypeId(corpAdminGroup.getAdminGroupTypeId());
						    	corpTokenInfo.setAdminGroupTypeName(corpAdminGroup.getAdminGroupTypeName());
						    	
						    	corpTokenInfo.setAdminGroupTypeCode(corpAdminGroup.getAdminGroupTypeCode());
								
								//-----------------这里为了支持老版本
							    cn.finder.wx.corp.response.FindAccessTokenResponse resp=null;
							   
							    resp= corpService.findAccessToken(corpid,corpsecret);
							    
							    
							    
							    logger.info("企业号:"+corpAdminGroup.getCorpinfo_name()+"====Corp FindAccessTokenResponse:"+resp.getBody());
							    //if(StringUtils.isEmpty(resp.getErrcode())){
							    if("0".equals(resp.getErrcode())){
							    	//调用成功
							    	if(resp.getExpires_in()<=60*60*2){
							    		//在7200 之内  那么刷新 accessToken
							    		
							    		
							    		
							    	}
							    	
							    	
							    	corpTokenInfo.setAccessToken(resp.getAccess_token());
							    	corpTokenInfo.setExpiresIn(resp.getExpires_in());
							    	
							    	
							    	
							    	
							    	
							    	cn.finder.wx.corp.response.FindJsapiTicketResponse findJsapiTicketResponse=null;
							    	
							        try{
							        	//获取 jsapi ticket
							        	findJsapiTicketResponse=corpService.findJsapiTicket(corpTokenInfo.getAccessToken());
							        	 logger.info("企业号:"+corpAdminGroup.getCorpinfo_name()+"====Corp FindJsapiTicketResponse:"+findJsapiTicketResponse.getBody());
								        if("0".equalsIgnoreCase(findJsapiTicketResponse.getErrcode())){
								        	corpTokenInfo.setJsapi_ticket(findJsapiTicketResponse.getTicket());
								    		
								    	}
								    	else{
								    		String oldTicket=WXAppInfo.getCorpAccessTokenInfo(corpid, corpsecret).getJsapi_ticket();
								    		corpTokenInfo.setJsapi_ticket(oldTicket);
								    	}
								        logger.info("企业号:"+corpAdminGroup.getCorpinfo_name()+"=====corpTokenInfo.getJsapi_ticket:"+corpTokenInfo.getJsapi_ticket());
							        }
							        catch(Throwable e){
							        	logger.error(e);
							        }
							    	
							       
							        
							    	WXAppInfo.updateCorpAccessTokenInfo(corpid, corpsecret, corpTokenInfo);
							    	
							    	
							    }else{
							    	logger.error("===获取企业号access token 失败");
							    	commonService.updateWxCorpTokenStatus(corpid, "N");
							    }
							    
							    //================应用权限单独加载

						        try{
						        	 //加载应用到 corpTokenInfo
							        
							        List<Map<String,Object>> apps= commonService.findWxApp(corpid);
							        logger.info("=====apps len: "+apps.toString());
							        if(apps!=null&&apps.size()>0){
							        	for(Map<String,Object> app:apps){
							        		try{
							        			
								        		WXApp wxapp=new WXApp();
								        		wxapp.setId(app.get("id").toString());
								        		wxapp.setAgentid(app.get("agentid").toString());
								        		wxapp.setSecret(app.get("secret").toString());
								        		wxapp.setWx_appid(app.get("wx_appid").toString());
								        		wxapp.setIs_msg_agent(app.get("is_msg_agent").toString());
								        		wxapp.setName(app.get("name").toString());
								        		
								        		
												   
											    resp= corpService.findAccessToken(corpid,wxapp.getSecret());
								        		if(resp.isSuccess()){
								        			wxapp.setAccess_token(resp.getAccess_token());
								        		}
								        		
								        		logger.info(String.format("====企业号:%s -> 应用: %s: access_token:%s", corpAdminGroup.getCorpinfo_name(),wxapp.getName(),resp.getAccess_token()));
								        		
								        		
								        		cn.finder.wx.corp.response.FindJsapiTicketResponse findJsapiTicketResponse=null;
										    	
										        try{
										        	//获取 jsapi ticket
										        	findJsapiTicketResponse=corpService.findJsapiTicket(wxapp.getAccess_token());
										        	 logger.info(String.format("应用: %s->findJsapiTicketResponse:%s", wxapp.getName(),findJsapiTicketResponse.getBody()));
											        if(findJsapiTicketResponse.isSuccess()){
											        	wxapp.setJsapi_ticket(findJsapiTicketResponse.getTicket());
											    		
											    	}
											    	else{
											    		/*String oldTicket=WXAppInfo.getCorpAccessTokenInfo(corpid, corpsecret).getJsapi_ticket();
											    		corpTokenInfo.setJsapi_ticket(oldTicket);*/
											    	}
										        }
										        catch(Throwable e){
										        	logger.error(e);
										        }
										    	
										        corpTokenInfo.addApp(wxapp);
								        		
								        		
							        		}
							        		catch(Throwable e){
							        			logger.error("===加载  agentid "+app.get("agentid").toString()+"");
							        		}
							        	}
							        }
							        
						        }
						        catch(Exception ee){
						        	logger.error("====加载企业号应用失败...");
						        }
						       
						        logger.info("===="+corpAdminGroup.getCorpinfo_name()+" 消息应用 "+corpTokenInfo.getMsgAgentId());
						        commonService.updateWxCorpTokenStatus(corpid, "Y");
						        WXAppInfo.updateCorpAccessTokenInfo(corpid, corpsecret, corpTokenInfo);
								
							    
							}
						}
						
						
						
						
						
						
					}
					catch(Throwable e){
						logger.error("====企业号token 加载异常:"+e.getMessage());
					}
					
					//================更新企业号票据 end=================//
					
					
					
					
					
					
					
					
					//================更新服务号票据 start=================//
					logger.info("===服务号token加载===========");
					List<AppInfo> appInfos = commonService.findAppInfo();
					logger.info("===appInfos:"+appInfos);
					if(appInfos!=null &&appInfos.size()>0)
					{
						int len=appInfos.size();
						logger.info("===微信服务号:"+len);
						for(int i=0;i<len;i++)
						{
							
						    AppInfo appInfo=appInfos.get(i);
						   
						    //基础验证 获取token
						    if(appInfo.getType()==0){
						    	
						    	//服务号
						    	 logger.info("==========微信服务号:"+appInfo.getName());
						    	
						    	 WXService service=new WXService();
								    
								    FindAccessTokenResponse resp= service.findAccessToken(appInfo.getGrantType(), appInfo.getAppid(), appInfo.getAppSecret());

								    if(StringUtils.isEmpty(resp.getErrcode())){
								    	//调用成功
								    	if(resp.getExpires_in()<=60*60*2){
								    		//在7200 之内  那么刷新 accessToken
								    		
								    		
								    		
								    	}
								    	
								    	//加入集合
								    	
								    	cn.finder.wae.wx.data.AppInfo appinfo=new cn.finder.wae.wx.data.AppInfo();
								    	//BeanUtils.copyProperties(appinfo, appInfo);
								    	
								    	appinfo.setAppid(appInfo.getAppid());
								    	appinfo.setAppSecret(appInfo.getAppSecret());
								    	appinfo.setGrantType(appInfo.getGrantType());
								    	appinfo.setName(appInfo.getName());
								    	appinfo.setOpenid(appInfo.getOpenid());
								    	appinfo.setAccessToken(resp.getAccess_token());
								    	appinfo.setExpiresIn(resp.getExpires_in());
								    	
								    	try{
								    		//设置支付商户信息
								    		 appinfo.setMerchantInfo(new cn.finder.wae.wx.data.AppInfo.MerchantInfo(appInfo.getMerchantInfo().getMerchant_id(), appInfo.getMerchantInfo().getMerchant_userid(),appInfo.getMerchantInfo().getNotify_url(),appInfo.getMerchantInfo().getMerchant_key()));
								    		 
								    		 logger.info("==appInfo.getMerchantInfo().getMerchant_id():"+appInfo.getMerchantInfo().getMerchant_id());
								    	}
								    	catch(Exception e){
								    		
								    	}
								    	
								    	logger.info("====FindAccessTokenResponse:"+resp.getBody());
								    	
								        FindJsapiTicketResponse findJsapiTicketResponse=null;
								    	
								        try{
								        	findJsapiTicketResponse=service.findJsapiTicket(resp.getAccess_token());
								        	 logger.info("====FindJsapiTicketResponse:"+findJsapiTicketResponse.getBody());
									        if("0".equalsIgnoreCase(findJsapiTicketResponse.getErrcode())){
									    		appinfo.setJsapi_ticket(findJsapiTicketResponse.getTicket());
									    		
									    	}
									    	else{
									    		String oldTicket=WXAppInfo.getAppInfo(appInfo.getAppid()).getJsapi_ticket();
									    		appinfo.setJsapi_ticket(oldTicket);
									    	}
									        logger.info("=====appinfo.getJsapi_ticket:"+appinfo.getJsapi_ticket());
									        
									        commonService.updateWxAppInfoTokenStatus(appInfo.getAppid(), "Y");
								        }
								        catch(Throwable e){
								        	logger.error(e);
								        	commonService.updateWxAppInfoTokenStatus(appInfo.getAppid(), "N");
								        }
								       
								       
								    	WXAppInfo.updateAppInfo(appInfo.getAppid(), appinfo);
								    	
								    	//启动定时器 根据时间定时刷新
								    	
								    }else{
								    	commonService.updateWxAppInfoTokenStatus(appInfo.getAppid(), "N");
								    }
						    }else if(appInfo.getType()==1){
						    	//应用号
						    	 logger.info("==========微信小程序:"+appInfo.getName());
						    	cn.finder.wae.wx.data.AppInfo appinfo=new cn.finder.wae.wx.data.AppInfo();
						    	//BeanUtils.copyProperties(appinfo, appInfo);
						    	
						    	appinfo.setAppid(appInfo.getAppid());
						    	appinfo.setAppSecret(appInfo.getAppSecret());
						    	appinfo.setGrantType(appInfo.getGrantType());
						    	appinfo.setName(appInfo.getName());
						    	appinfo.setOpenid(appInfo.getOpenid());
						    	
						    	try{
						    		//设置支付商户信息
						    		 appinfo.setMerchantInfo(new cn.finder.wae.wx.data.AppInfo.MerchantInfo(appInfo.getMerchantInfo().getMerchant_id(), appInfo.getMerchantInfo().getMerchant_userid(),appInfo.getMerchantInfo().getNotify_url(),appInfo.getMerchantInfo().getMerchant_key()));
						    		 
						    		 logger.info("==appInfo.getMerchantInfo().getMerchant_id():"+appInfo.getMerchantInfo().getMerchant_id());
						    		 
						    		 commonService.updateWxAppInfoTokenStatus(appInfo.getAppid(), "A");
						    	}
						    	catch(Exception e){
						    		commonService.updateWxAppInfoTokenStatus(appInfo.getAppid(), "N");
						    	}
						    	
						    	WXAppInfo.updateAppInfo(appInfo.getAppid(), appinfo);
						    	logger.info("=====加入成功");
						    	
						    }else if(appInfo.getType()==2){
						    	
						    	//服务号
						    	 logger.info("==========区块链:"+appInfo.getName());
						    	
						    	   STCService stcService=new STCService();
						    	   
								    
								    cn.cnxingkong.stc.token.response.FindAccessTokenResponse resp= stcService.findAccessToken(appInfo.getGrantType(), appInfo.getAppid(), appInfo.getAppSecret());

								    if(StringUtils.isEmpty(resp.getErrcode())){
								    	//调用成功
								    	if(resp.getExpires_in()<=60*60*2){
								    		//在7200 之内  那么刷新 accessToken
								    		
								    	}
								    	
								    	//加入集合
								    	
								    	cn.finder.wae.wx.data.AppInfo appinfo=new cn.finder.wae.wx.data.AppInfo();
								    	//BeanUtils.copyProperties(appinfo, appInfo);
								    	
								    	appinfo.setAppid(appInfo.getAppid());
								    	appinfo.setAppSecret(appInfo.getAppSecret());
								    	appinfo.setGrantType(appInfo.getGrantType());
								    	appinfo.setName(appInfo.getName());
								    	appinfo.setOpenid(appInfo.getOpenid());
								    	appinfo.setAccessToken(resp.getAccess_token());
								    	appinfo.setExpiresIn(resp.getExpires_in());
								    	
								    	
								    	logger.info("====区块链 FindAccessTokenResponse:"+resp.getBody());
								    	
								    	
								        try{
									        commonService.updateWxAppInfoTokenStatus(appInfo.getAppid(), "Y");
								        }
								        catch(Throwable e){
								        	logger.error(e);
								        	commonService.updateWxAppInfoTokenStatus(appInfo.getAppid(), "N");
								        }
								       
								       
								    	WXAppInfo.updateAppInfo(appInfo.getAppid(), appinfo);
								    	
								    	//启动定时器 根据时间定时刷新
								    	
								    }else{
								    	commonService.updateWxAppInfoTokenStatus(appInfo.getAppid(), "N");
								    }
						    }
						    
						   
						}
					}else{
						logger.info("===应用数量:0");
					}
					
					//================更新服务号票据 end=================//
					
							
					
					
				}
				catch(Throwable e){
					
					logger.error("======微信TOKEN刷新错误:"+e);
					duration=60*1000;//1分钟
				}
				
				finally
				{
					logger.info("===线程休眠:"+duration);
					//间隔时间
					try {
						Thread.sleep(duration); 
						
						duration=60*60*1000/2;//重置成 30分钟
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						logger.error(e.toString());
					}
				}
			}
		
			
		}
		
	}
	
	
	
	/*
	 * 
	 * 任务执行
	 */
	class TaskPlanExecutor extends TimerTask
	{
		
		
		private TaskPlan item;
		

		public TaskPlanExecutor(String threadName,TaskPlan item) {
			super();
			
			
			this.item = item;
			
		}

		@Override
		public void run() {
			
			
		}
		
		
		
	}
	
	
	/***
	 * 最新数据读取之后 ，  重置记录更新状态
	 * @author: wuhualong
	 * @data:2014-6-4下午3:01:02
	 * @function:
	 */
	class TaskPlanResetUpdateThread extends Thread
	{

		private List<TaskPlan> taskPlans;
		
		
		public TaskPlanResetUpdateThread(List<TaskPlan> taskPlans) {
			super();
			this.taskPlans = taskPlans;
		}


		@Override
		public void run() {
			
		}
		
		
	}
	
	
	
	
	
	
	
	
}
