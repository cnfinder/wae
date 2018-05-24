package cn.finder.wae.service.rest.controller;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.service.rest.cache.UserLoginedCache;
import cn.finder.wae.service.rest.common.RestTemplateExecuter;
import cn.finder.wae.business.domain.Message;
import cn.finder.wae.business.domain.Response;
import cn.finder.wae.business.domain.ServiceInterface;
import cn.finder.wae.business.domain.ServiceInterfaceLog;
import cn.finder.wae.business.domain.ShowDataConfig;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.domain.User;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.business.module.auth.service.AuthService;
import cn.finder.wae.business.module.common.service.CommonService;
import cn.finder.wae.business.module.common.service.QueryService;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.cache.ConstantsCache;
import cn.finder.wae.cache.SysConfigCache;
import cn.finder.wae.common.comm.Common;
import cn.finder.wae.common.comm.DateUtils;
import cn.finder.wae.common.comm.EncoderHandler;
import cn.finder.wae.common.comm.JsonUtil;
import cn.finder.wae.common.constant.Constant;
import cn.finder.wae.common.exception.InfoException;
import cn.finder.wae.common.exception.WaeRuntimeException;
import cn.finder.wae.common.type.FinderLinkedMap;

/**
 * @author: wuhualong
 * @data:2014-4-16上午9:15:36
 * @function:
 */

@Controller
@RequestMapping(value = "/service/rest")
public class ServiceInterfaceController {

	
		private static Logger logger =Logger.getLogger(ServiceInterfaceController.class);
	
		public boolean retureExceptionToClient=true;
		
		@Autowired
		private QueryService queryService;
		
		@Autowired
		private AuthService authService;
		
	
	
		@Autowired
		private CommonService commonService;
		
		//HttpServletRequest request;
		
		/**
		 * 登陆成功后返回 用户密码 密文 作为用户操作API 的 session
		 * @author: finder
		 * @function:
		 * @param userName
		 * @param password
		 * @return
		 */
	   @RequestMapping(value = "auth/login", method = { RequestMethod.POST, RequestMethod.GET })
	   @ResponseBody
	   public Response<User> login(@RequestParam("user_name") final String userName,
	    		@RequestParam("user_password") final String password) {
	     
		   logger.debug("=====execute ServiceInterfaceController.login");
	 	    return new RestTemplateExecuter<User>() {
	 	
	 			@Override
	 			protected Response<User> execute() throws Exception {
	 				
	 				try{

 				    	User user =new User();
 				    	user.setAccount(userName);
 				    	user.setPassword(password);
 				    	user = authService.login(user); //返回的密码是密文
 				    	
	 					if(user!=null){
		 				    resp.setTag(1);
		 				    user.setSessionId(EncoderHandler.encodeByMD5(userName+"_____"+System.currentTimeMillis()));
		 				    
		 				    //生成 session
		 				    /*String token="";
		 				    String timestamp=System.currentTimeMillis()+"";
		 				    String nonce=System.currentTimeMillis()+"";
		 				    String encrypt=UUID.randomUUID().toString().replace("-", "");
		 				    
		 				    String[] array = new String[] { token, timestamp, nonce, encrypt };
		 					StringBuffer sb = new StringBuffer();
		 					// 字符串排序
		 					Arrays.sort(array);
		 					for (int i = 0; i < 4; i++) {
		 						sb.append(array[i]);
		 					}
		 					String str = sb.toString();
		 				    
		 				    String session=EncoderHandler.encodeByMD5(str);
		 				    
		 				    user.setSessionId(session);
		 				    */
		 				  
		 				    
		 				    
	 				    	user.setPassword("");//清空密码
	 				    	
	 				    	
 				    	    //保存session到缓存
		 				    UserLoginedCache.getInstance().add(user.getSessionId(), user,7200);
	 				    	
		 				    resp.setEntity(user);
		 				    resp.getMessage().setMsg("登陆成功");
	 					}
	 					else{
	 						resp.getMessage().setStatusCode(Message.StatusCode_UnAuth);
	 						resp.getMessage().setMsg("用户名或密码错误");
	 					}
	 				   logger.debug("====="+resp.getMessage().getMsg());
	 				}catch(Exception e)
	 				{
	 					resp.getMessage().setMsg("登陆失败");
	 					logger.error("====="+resp.getMessage().getMsg());
	 				}
	 				return resp;
	 			}
	 	    }.run();
	     
	   }
	   
	   
	   /***
	    * 登录后会话状态检查
	    * @param session  
	    * @return
	    */
	   @RequestMapping(value = "auth/checkSession", method = { RequestMethod.POST, RequestMethod.GET })
	   @ResponseBody
	   public Response<User> checkSession(@RequestParam("session") final String session) {
	     
		   logger.debug("=====execute ServiceInterfaceController.checkSession");
	 	    return new RestTemplateExecuter<User>() {
	 	
	 			@Override
	 			protected Response<User> execute() throws Exception {
	 				
	 				try{

		 				User user=UserLoginedCache.getInstance().get(session);
	 				    if(user!=null){
		 				    resp.setEntity(user);
		 				    resp.getMessage().setMsg("session is sign in");
	 				    }
	 					else{
	 						resp.getMessage().setStatusCode(Message.StatusCode_UnAuth);
	 						resp.getMessage().setMsg("session is not exist or expire time");
	 					}
	 				   logger.debug("====="+resp.getMessage().getMsg());
	 				}catch(Exception e)
	 				{
	 					resp.getMessage().setMsg("checkSession service invoke failure");
	 					logger.error("====="+resp.getMessage().getMsg());
	 				}
	 				return resp;
	 			}
	 	    }.run();
	     
	   }
	   
	   
	   /***
	    * 登录后会话状态检查
	    * @param session  
	    * @return
	    */
	   @RequestMapping(value = "auth/refreshSession", method = { RequestMethod.POST, RequestMethod.GET })
	   @ResponseBody
	   public Response<User> refreshSession(@RequestParam("session") final String session) {
	     
		   logger.debug("=====execute ServiceInterfaceController.refreshSession");
	 	    return new RestTemplateExecuter<User>() {
	 	
	 			@Override
	 			protected Response<User> execute() throws Exception {
	 				
	 				try{

		 				User user=UserLoginedCache.getInstance().get(session);
	 				    if(user!=null){
	 				    	UserLoginedCache.getInstance().add(session, user,300); //旧的session 在5分钟后失效
	 				    	
	 				    	//生成最新 session
	 				    	user.setSessionId(EncoderHandler.encodeByMD5(user.getAccount()+"_____"+System.currentTimeMillis()));
	 				    	
	 				    	 
	 				    	//保存session到缓存
		 				    UserLoginedCache.getInstance().add(user.getSessionId(), user,7200);
	 				    	
		 				    resp.setEntity(user);
		 				    resp.getMessage().setMsg("session is refresh");
	 				    }
	 					else{
	 						resp.getMessage().setStatusCode(Message.StatusCode_UnAuth);
	 						resp.getMessage().setMsg("session is not exist or expire time");
	 					}
	 				   logger.debug("====="+resp.getMessage().getMsg());
	 				}catch(Exception e)
	 				{
	 					resp.getMessage().setMsg("checkSession service invoke failure");
	 					logger.error("====="+resp.getMessage().getMsg());
	 				}
	 				return resp;
	 			}
	 	    }.run();
	     
	   }
	   
	   /**
	    * 测试:http://localhost:8080/IronArch/service/rest/interface?userName=dragon&password=%23%23%23%23%23%23%23%23&method=aps.user.login&v=1.0&session=1212&app_key=testKey&format=json&partner_id=aps-sdk-net-20140410&timestamp=2014-04-16+12%3a32%3a02&sign=FD2E981B4B2BE1EA19CC967F48F6C792
	    * @author: finder
	    * @function:
	    * @param method
	    * @param v
	    * @param appKey
	    * @param format
	    * @param partnerId
	    * @param sessionKey
	    * @param timestamp
	    * , produces = {"application/json;charset=UTF-8"}
	    * @return
	    */
	   @RequestMapping(value = "interface", method = { RequestMethod.POST, RequestMethod.GET })
	   @ResponseBody
	   public Response<Map<String,Object>> serviceInterface(@RequestParam("method") final String method,
			   @RequestParam("v") final String v,
			   @RequestParam("app_key") final String appKey,   
			   @RequestParam("format") final String format,
			   @RequestParam("partner_id") final String partnerId,
			   @RequestParam("session") final String sessionKey, //使用account md5后的32位hex  作为 session
			   @RequestParam("timestamp") final String timestamp,
			   HttpServletResponse response) {
	     
		   logger.debug("=====execute ServiceInterfaceController.serviceInterface");
	     
		   logger.info("====系统参数:v="+v+",app_key:"+appKey+",format="+format+",partner_id="+partnerId+",session="+sessionKey+",timestamp="+timestamp);
		   
		  // response.setContentType("application/"+format);
		 
		   /*  try {
				Thread.sleep(3000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}*/
		 
		   
		    final Date startTime =new Date();
		    
	 	    return new RestTemplateExecuter<Map<String,Object>>() {
	 	
	 			@SuppressWarnings("unchecked")
				@Override
	 			protected Response<Map<String,Object>> execute() throws Exception {
	 				
	 				 HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder
	 						.getRequestAttributes()).getRequest();
	 				 
	 				  String excludePropertiesString =request.getParameter("excludeProperties");
	 				  String includePropertiesString =request.getParameter("includeProperties");
	 				   
	 				  String[] excludeProperties=StringUtils.split(excludePropertiesString, ",");
	 				   
	 				 ServiceInterfaceLog log = new ServiceInterfaceLog();
	 				log.setInvokeTime(new Date());
	 				log.setInterfaceName(method);
	 				
	 						
	 				 
	 				ServiceInterface  serviceInterface=null;
	 				try{
	 					Message msg =validateInterface(method);
	 					if(msg.getStatusCode()!=Message.StatusCode_OK){
	 						//错误
	 						logger.error(String.format("===接口验证：%1$s,%2$s",msg.getStatusCode(),msg.getMsg()));
	 					}else{
	 						//1.获取接口
		 					
		 				   serviceInterface=ArchCache.getInstance().getServiceInterfaceCache().get(method);
		 				    
		 				   @SuppressWarnings("rawtypes")
  		 				   Map<String,Object>  paraMap =new FinderLinkedMap<String, Object>();
		 				   Map<String,Object> reqMap =  request.getParameterMap();
		 				  
		 				 
		 				   if(serviceInterface.getEnableLog()==1){
		 					   //启用日志
		 					  
		 					   log.setInterfaceName(serviceInterface.getInterfaceName());
		 					   log.setInterfaceNameCn(serviceInterface.getInterfaceNameCn());
		 					   log.setShowtableConfigId(serviceInterface.getShowtableConfigId());
		 					   log.setIsNeedAuth(serviceInterface.getIsNeedAuth());
		 					   log.setVersion(serviceInterface.getVersion());
		 					   log.setGroupName(serviceInterface.getGroupName());
		 					   log.setRemark(serviceInterface.getRemark());
		 					   log.setEnabled(serviceInterface.getEnabled());
		 					   
		 					   
		 					  Map<String,Object>  map =new FinderLinkedMap<String, Object>();
		 					   Set<Entry<String, Object>>  setEntry= reqMap.entrySet();
		 					   
		 					   Iterator<Entry<String, Object>> iteEntry = setEntry.iterator();
		 					   
		 					   while(iteEntry.hasNext()){
		 						   
		 						   Entry<String,Object> entry = iteEntry.next();
		 						   
		 						  map.put(entry.getKey(), ((String[])entry.getValue())[0]);
		 						   
		 					   }
		 					   
		 					  log.setInputContent(JSONArray.fromObject(map).toString());
		 					   
		 					 // commonService.addServiceInterfaceLog(log);
		 					   
		 					   
		 				   }
		 				  
		 				   
		 				   if(reqMap!=null){
		 					   Set<Entry<String, Object>>  setEntry= reqMap.entrySet();
		 					   
		 					   Iterator<Entry<String, Object>> iteEntry = setEntry.iterator();
		 					   
		 					   while(iteEntry.hasNext()){
		 						   
		 						   Entry<String,Object> entry = iteEntry.next();
		 						   
		 						   paraMap.put(entry.getKey(), ((String[])entry.getValue())[0]);
		 						   
		 					   }
		 				   }
		 				
		 				
		 				   
		 				    boolean authSuccess=true;
		 				    if(1==serviceInterface.getIsNeedAuth()){
		 				    	//进行授权验证
		 				    	logger.debug("===进行身份验证");
		 				    	//使用 session 值处理
		 				    	
		 				    	/*String user_name= (String)paraMap.get("user_name");
		 				    	if(EncoderHandler.encodeByMD5(user_name).equalsIgnoreCase(sessionKey)){
		 				    		authSuccess=true;
		 				    	}
		 				    	else{
		 				    		authSuccess=false;
		 				    	}*/
		 				    	
		 				    	User user=UserLoginedCache.getInstance().get(sessionKey); //判断是否在缓存存在
		 				    	if(user==null)
		 				    		authSuccess=false;
		 				    	
		 				    	if(!authSuccess){
				 					  
			 					   logger.debug("===身份验证失败");
			 					   msg.setMsg("===身份验证失败");
			 					   msg.setStatusCode(Message.StatusCode_UnAuth);
		 				    	}
			 					  else{
			 				    	   logger.debug("===身份验证成功");
			 					  }
		 				    }
		 				    
		 				   if(authSuccess){
		 					  
	 				    		
	 				    	   long showtableConfigId = serviceInterface.getShowtableConfigId();
	 				    	   paraMap.put("showtableConfigId", showtableConfigId+"");
	 		 				   
	 				    	   paraMap.put("table001abcX", ArchCache.getInstance().getShowTableConfigCache().get(showtableConfigId).getShowTableName());
	 				    	   
	 		 				   
	 		 				   
	 		 				   MapParaQueryConditionDto<String,Object> queryCondition =new MapParaQueryConditionDto<String,Object>();
	 				    	   String dataStr = request.getParameter("data");
		 				 		
		 				 		logger.info(" data str: "+dataStr);
		 				 		
		 				 		Map<String, Object> data = JsonUtil.getMap4Json(dataStr);
	 				 		
	 				    	   
	 		 				   processWhereCondition(data,queryCondition,showtableConfigId);
	 		 				   
	 		 				   processParameter(paraMap,queryCondition);
	 		 				   
	 		 				   
	 		 				   paraMap.put("WebRoot_path", request.getSession().getServletContext().getRealPath("/"));
	 		 				   paraMap.put("session_user", (User)request.getSession().getAttribute(Constant.KEY_SESSION_USER));
	 		 				   queryCondition.setMapParas(paraMap);
	 		 				   
	 		 				    TableQueryResult queryResult=  queryService.queryTableQueryResult(showtableConfigId, queryCondition);
	 		 				    if(queryResult!=null){
		 		 				    resp.setPageIndex(queryResult.getPageIndex());
		 		 				    resp.setPageSize(queryResult.getPageSize());
		 		 				    resp.setTotalRecord(queryResult.getCount());
		 		 				    resp.setPageCount(queryResult.getPageCount());
		 		 				    resp.setEntities(queryResult.getResultList());
		 		 				  boolean fieldsSer=true;
		 		 				    if(excludeProperties!=null &&excludeProperties.length>0){
		 		 				    	
		 		 				    	for(int k=0;k<excludeProperties.length;k++){
		 		 				    		String excludePropertiesItem=excludeProperties[k];
		 		 				    		if("fields".equals(excludePropertiesItem)){
		 		 				    			fieldsSer =false;
		 		 				    			
		 		 				    		}else{
		 		 				    			if(resp.getEntities()!=null && resp.getEntities().size()>0){
		 		 				    				for(int ep_idx=0;ep_idx<resp.getEntities().size();ep_idx++){
		 		 				    					
		 		 				    					resp.getEntities().get(ep_idx).remove(excludePropertiesItem);
		 		 				    				}
		 		 				    			}
		 		 				    			
		 		 				    		}
		 		 				    	}
		 		 				    	
		 		 				    }
		 		 				  if(fieldsSer)
	 		 				    		resp.setFields(queryResult.getFields());
		 		 				    
	 		 				    }
	 		 				    resp.setTag(1);
	 		 				    
	 		 				    String msgTxt =String.format("调用接口 %1$s(%2$s) 成功 %3$s", serviceInterface.getInterfaceName(),serviceInterface.getInterfaceNameCn(),
	 		 				    		queryResult==null?"":(queryResult.getMessage().getMsg()==null?"":queryResult.getMessage().getMsg()));
	 		 				    
	 		 				    
	 		 				    msg.setStatusCode(Message.StatusCode_OK);
	 		 				    msg.setMsg(msgTxt);
 		 				    	msg.setResultCode(queryResult.getMessage().getResultCode());
 		 				    	msg.setResultMsg(queryResult.getMessage().getResultMsg());
	 		 				   logger.debug(msgTxt);
	 		 				   
	 				    		
	 				    	}
		 				    
		 				   
		 				}
	 					
	 					resp.setMessage(msg);
	 					
	 					
	 					
 						 log.setStatusCode(Message.StatusCode_OK+"");
 						log.setOutMsg(objToJsonString(resp,excludePropertiesString,includePropertiesString));
	 					 
 						logger.debug(log.toString());
	 				    
	 				}
	 				catch(InfoException infoExp){
	 					resp.getMessage().setMsg(infoExp.getErrorInfo().getErrDesc());
	 					resp.getMessage().setStatusCode(Message.StatusCode_InfoExp);
	 					
	 					
	 					log.setStatusCode(Message.StatusCode_InfoExp+"");
						log.setOutMsg(infoExp.getErrorInfo().getErrDesc());
						
						logger.debug(infoExp.getErrorInfo().getErrDesc());
						
						
	 				}
	 				catch(WaeRuntimeException e)
	 				{
	 					
	 					String msgTxt =String.format("调用接口 %1$s(%2$s) 失败 开始时间: %3$s 结束时间: %4$s", serviceInterface.getInterfaceName(),serviceInterface.getInterfaceNameCn(),DateUtils.formatDate(startTime),DateUtils.formatDate(new Date()));
	 					
	 					if(retureExceptionToClient){
	 						msgTxt+= ":"+ e.getMessage();
	 					}
	 					
	 					resp.getMessage().setMsg(msgTxt);
	 					resp.getMessage().setStatusCode(Message.StatusCode_ServerError);
	 					 logger.error(msgTxt);
	 					 
 						 log.setStatusCode(Message.StatusCode_ServerError+"");
 						 log.setOutMsg(msgTxt);
	 					 
 						logger.error(log.toString());
 						logger.error(e.getErrInfo().getErrMsg());
	 				}
	 				catch(Throwable e)
	 				{
	 					
	 					String msgTxt =String.format("调用接口 %1$s(%2$s) 失败 开始时间: %3$s 结束时间: %4$s", serviceInterface.getInterfaceName(),serviceInterface.getInterfaceNameCn(),DateUtils.formatDate(startTime),DateUtils.formatDate(new Date()));
	 					
	 					if(retureExceptionToClient){
	 						msgTxt+= ":"+ e.getMessage();
	 					}
	 					
	 					resp.getMessage().setMsg(msgTxt);
	 					resp.getMessage().setStatusCode(Message.StatusCode_ServerError);
	 					 logger.error(msgTxt);
	 					 
 						 log.setStatusCode(Message.StatusCode_ServerError+"");
 						 log.setOutMsg(msgTxt);
	 					 
 						logger.error(log.toString());
 						logger.error(e.toString());
	 				}
	 				finally
	 				{
	 					try
	 					{
	 						if(ArchCache.getInstance().getSysConfigCache().get(SysConfigCache.KEY_CONFIG_INTERFACELOG_ENABLE).getIntValue()==1 && serviceInterface!=null && serviceInterface.getEnableLog()==1){
	 						
	 							//commonService.updateServiceInterfaceLog(log);
	 							log.setCompleteTime(new Date());
	 							commonService.addServiceInterfaceLog(log);
	 						
	 						}
	 						
	 					}
	 					finally{
	 						
	 					}
	 				}
	 				return resp;
	 			}
	 	    }.run();
	     
	   }
	   
	   
	   
	   
	   
	   
	   
	   /**
	    * 测试:http://localhost:8080/IronArch/service/rest/upload_interface?userName=dragon&password=%23%23%23%23%23%23%23%23&method=aps.user.login&v=1.0&session=1212&app_key=testKey&format=json&partner_id=aps-sdk-net-20140410&timestamp=2014-04-16+12%3a32%3a02&sign=FD2E981B4B2BE1EA19CC967F48F6C792
	    * @author: wuhualong
	    * @data:2014-08-01下午12:49:37
	    * @function:
	    * @param method
	    * @param v
	    * @param appKey
	    * @param format
	    * @param partnerId
	    * @param sessionKey
	    * @param timestamp
	    * , produces = {"application/json;charset=UTF-8"}
	    * @return
	    */
	   @RequestMapping(value = "upload_interface", method = { RequestMethod.POST, RequestMethod.GET })
	   @ResponseBody
	   public Response<Map<String,Object>> uploadInterface(@RequestParam("method") final String method,
			   @RequestParam("v") final String v,
			   @RequestParam("app_key") final String appKey,   
			   @RequestParam("format") final String format,
			   @RequestParam("partner_id") final String partnerId,
			   @RequestParam("session") final String sessionKey, //使用密文密码 作为 session
			   @RequestParam("timestamp") final String timestamp,
			  // @RequestParam(value = "pic1", required = false) MultipartFile file,
			  final HttpServletRequest request,
			   HttpServletResponse response) {
	     
		 
		  // this.request = request;
		   logger.debug("=====execute ServiceInterfaceController.uploadInterface");
	     
		   logger.debug("====系统参数:v="+v+",app_key:"+appKey+",format="+format+",partner_id="+partnerId+",session="+sessionKey+",timestamp="+timestamp);
		   
		  // logger.debug(" == file:"+file.getName()+" ,contenttype:"+file.getContentType());
		  // response.setContentType("application/"+format);
		 
		   /*  try {
				Thread.sleep(3000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}*/
		 
	        
		    final Date startTime =new Date();
		    
	 	    return new RestTemplateExecuter<Map<String,Object>>() {
	 	
	 			@SuppressWarnings("unchecked")
				@Override
	 			protected Response<Map<String,Object>> execute() throws Exception {
	 				
	 				 
	 				  String excludePropertiesString =request.getParameter("excludeProperties");
	 				  String includePropertiesString =request.getParameter("includeProperties");
	 				   
	 				  String[] excludeProperties=StringUtils.split(excludePropertiesString, ",");
	 			
	 				
	 				
	 				ServiceInterface  serviceInterface=null;
	 				try{
	 					Message msg =validateInterface(method);
	 					if(msg.getStatusCode()!=Message.StatusCode_OK){
	 						//错误
	 						logger.error(String.format("===接口验证：%1$s,%2$s",msg.getStatusCode(),msg.getMsg()));
	 					}else{
	 						//1.获取接口
		 					
		 				    serviceInterface=ArchCache.getInstance().getServiceInterfaceCache().get(method);
		 				    
		 				   @SuppressWarnings("rawtypes")
		 				   Map<String,Object>  paraMap =new LinkedHashMap<String, Object>();
		 				   Map<String,Object> reqMap =  request.getParameterMap();
		 				  
		 				   
		 				   
		 				  
		 				   
		 				   if(reqMap!=null){
		 					   Set<Entry<String, Object>>  setEntry= reqMap.entrySet();
		 					   
		 					   Iterator<Entry<String, Object>> iteEntry = setEntry.iterator();
		 					   
		 					   while(iteEntry.hasNext()){
		 						   
		 						   Entry<String,Object> entry = iteEntry.next();
		 						   String key = entry.getKey();
		 						   Object value = ((String[])entry.getValue())[0];
		 						   
		 						  long showtableConfigId = serviceInterface.getShowtableConfigId();
		 						  
		 							List<ShowDataConfig> showDataConfigs = ArchCache.getInstance()
		 									.getShowTableConfigCache() 
		 									.getSearchShowDataConfigs(showtableConfigId);
		 						   
		 							for(ShowDataConfig sdc:showDataConfigs){
		 								
		 								if(sdc.getFieldNameAlias().equals(key)  && sdc.getShowType() ==ConstantsCache.ControlType.CONTROLTYPE_BINARY_FILE){
		 									
		 									 MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;  
		 		 					        MultipartFile multipartFile = multipartRequest.getFile(key); 
		 		 					        logger.info(" == file:"+multipartFile.getName()+" ,contenttype:"+multipartFile.getContentType());
		 		 							paraMap.put("file_type", multipartFile.getContentType());
		 		 					        InputStream is = multipartFile.getInputStream();
		 		 					        byte[] b =new byte[is.available()];
		 		 					        is.read(b);
		 		 					        is.close();
		 		 					        
		 		 					      value = b;
		 		 					      
		 		 					      
		 		 					      break;
		 								}
		 							}
		 							
		 						  
		 						   
		 						   paraMap.put(key, value);
		 						   
		 						   
		 						   
		 					   }
		 				   }
		 				
		 				   
		 				   
		 				    boolean authSuccess=true;
		 				    if(1==serviceInterface.getIsNeedAuth()){
		 				    	//进行授权验证
		 				    	logger.debug("===进行身份验证");
		 				    	//使用 session 值处理
		 				    	
		 				    	
		 				    	
		 				    	User user =new User();
		 				    
		 				    	user.setAccount((String)paraMap.get("user_name"));
		 				    	
		 				    	user.setPassword(sessionKey);
		 				    	
		 				    	user = authService.sessionLogin(user);
		 				    	
		 				    	authSuccess = (user ==null?false:true);
		 				    
		 				    	paraMap.put("session_user", user);
		 				    	if(!authSuccess){
				 					  
			 					   logger.debug("===身份验证失败");
			 					   msg.setMsg("===身份验证失败");
			 					   msg.setStatusCode(Message.StatusCode_UnAuth);
		 				    	}
			 					  else{
			 				    	   logger.debug("===身份验证成功");
			 					  }
		 				    }
		 				    
		 				   if(authSuccess){
		 					  
	 				    		
	 				    	   long showtableConfigId = serviceInterface.getShowtableConfigId();
	 				    	   paraMap.put("showtableConfigId", showtableConfigId+"");
	 		 				   
	 				    	   paraMap.put("table001abcX", ArchCache.getInstance().getShowTableConfigCache().get(showtableConfigId).getShowTableName());
	 				    	   
	 		 				   
	 		 				   
	 		 				   MapParaQueryConditionDto<String,Object> queryCondition =new MapParaQueryConditionDto<String,Object>();
	 				    	   String dataStr = URLDecoder.decode(request.getParameter("data"),"UTF-8");
		 				 		
		 				 		logger.debug(" data str: "+dataStr);
		 				 		
		 				 		
		 				 		
		 				 		

		 						
		 						 
		 					/*	String saveDirectory = request.getRealPath("uploadfiles");
		 						try {
		 							MultipartRequest multi = new MultipartRequest(request,saveDirectory,100 * 1024 * 1024, "UTF-8");
		 							//如果有上传文件, 则保存到数据内
		 							Enumeration files = multi.getFileNames();
		 							
		 							
		 							//只读取第一个
		 							if (files.hasMoreElements()) {
		 								String fname = (String)files.nextElement();
		 								File f = multi.getFile(fname);
		 								Object value=null;
		 								if(f!=null){
		 									
		 									
		 									InputStream fileStream = new FileInputStream(f);
		 									fileStream.read((byte[])value);
		 								}
		 								sb.append(" and ");
		 								sb.append(fname).append("=?");
		 								whereValues.add(value);
		 							}
		 							
		 							
		 							
		 						} catch (IOException e) {
		 							// TODO Auto-generated catch block
		 							e.printStackTrace();
		 						}
		 						
		 						
		 						*/
		 						
		 				 		
		 				 		
		 				 		
		 				 		
		 				 		
		 				 		Map<String, Object> data = JsonUtil.getMap4Json(dataStr);
	 				 		
	 				    	   
	 		 				   processWhereCondition(data,queryCondition,showtableConfigId);
	 		 				   
	 		 				   processParameter(paraMap,queryCondition);
	 		 				   
	 		 				   
	 		 				   paraMap.put("WebRoot_path", request.getSession().getServletContext().getRealPath("/"));
	 		 				  paraMap.put("session_user", (User)request.getSession().getAttribute(Constant.KEY_SESSION_USER));
	 		 				   queryCondition.setMapParas(paraMap);
	 		 				   
	 		 				    TableQueryResult queryResult=  queryService.queryTableQueryResult(showtableConfigId, queryCondition);
	 		 				    if(queryResult!=null){
		 		 				    resp.setPageIndex(queryResult.getPageIndex());
		 		 				    resp.setPageSize(queryResult.getPageSize());
		 		 				    resp.setTotalRecord(queryResult.getCount());
		 		 				    resp.setPageCount(queryResult.getPageCount());
		 		 				    resp.setEntities(queryResult.getResultList());
		 		 				  boolean fieldsSer=true;
		 		 				    if(excludeProperties!=null &&excludeProperties.length>0){
		 		 				    	
		 		 				    	for(int k=0;k<excludeProperties.length;k++){
		 		 				    		if("fields".equals(excludeProperties[k])){
		 		 				    			fieldsSer =false;
		 		 				    			break;
		 		 				    		}
		 		 				    	}
		 		 				    	
		 		 				    }
		 		 				  if(fieldsSer)
	 		 				    		resp.setFields(queryResult.getFields());
		 		 				    
	 		 				    }
	 		 				    resp.setTag(1);
	 		 				    
	 		 				    String msgTxt =String.format("调用接口 %1$s(%2$s) 成功 %3$s", serviceInterface.getInterfaceName(),serviceInterface.getInterfaceNameCn(),
	 		 				    		queryResult==null?"":queryResult.getMessage().getMsg());
	 		 				    
	 		 				    
	 		 				    msg.setStatusCode(Message.StatusCode_OK);
	 		 				    msg.setMsg(msgTxt);
	 		 				    
	 		 				   logger.debug(msgTxt);
	 				    		
	 				    	}
		 				    
		 				   
		 				}
	 					
	 					resp.setMessage(msg);
	 				    
	 				}
	 				catch(InfoException infoExp){
	 					resp.getMessage().setMsg(infoExp.getErrorInfo().getErrDesc());
	 					resp.getMessage().setStatusCode(Message.StatusCode_InfoExp);
	 					
	 					
	 					//log.setStatusCode(Message.StatusCode_InfoExp+"");
						//log.setOutMsg(infoExp.getErrorInfo().getErrDesc());
						
						logger.debug(infoExp.getErrorInfo().getErrDesc());
						
						
	 				}
	 				catch(WaeRuntimeException e)
	 				{
	 					
	 					String msgTxt =String.format("调用接口 %1$s(%2$s) 失败 开始时间: %3$s 结束时间: %4$s", serviceInterface.getInterfaceName(),serviceInterface.getInterfaceNameCn(),DateUtils.formatDate(startTime),DateUtils.formatDate(new Date()));
	 					
	 					if(retureExceptionToClient){
	 						msgTxt+= ":"+ e.getMessage();
	 					}
	 					
	 					resp.getMessage().setMsg(msgTxt);
	 					resp.getMessage().setStatusCode(Message.StatusCode_ServerError);
	 					 logger.error(msgTxt);
	 					 
	 					 
	 				}
	 				catch(Throwable e)
	 				{
	 					
	 					String msgTxt =String.format("调用接口 %1$s(%2$s) 失败 开始时间: %3$s 结束时间: %4$s", serviceInterface.getInterfaceName(),serviceInterface.getInterfaceNameCn(),DateUtils.formatDate(startTime),DateUtils.formatDate(new Date()));
	 					
	 					if(retureExceptionToClient){
	 						msgTxt+= ":"+ e.getMessage();
	 					}
	 					
	 					resp.getMessage().setMsg(msgTxt);
	 					resp.getMessage().setStatusCode(Message.StatusCode_ServerError);
	 					 logger.error(msgTxt+":"+e.toString());
	 					
	 				}
	 				return resp;
	 			}
	 	    }.run();
	     
	   }
	   
	   
	   
	   
	   
	   
	   /**
	    * 测试:http://localhost:8080/IronArch/service/rest/interface?userName=dragon&password=%23%23%23%23%23%23%23%23&method=aps.user.login&v=1.0&session=1212&app_key=testKey&format=json&partner_id=aps-sdk-net-20140410&timestamp=2014-04-16+12%3a32%3a02&sign=FD2E981B4B2BE1EA19CC967F48F6C792
	    * @author: wuhualong
	    * @data:2014-4-16下午12:49:37
	    * @function:
	    * @param method
	    * @param v
	    * @param appKey
	    * @param format
	    * @param partnerId
	    * @param sessionKey
	    * @param timestamp
	    * , produces = {"application/json;charset=UTF-8"}
	    * @return
	    */
	   @RequestMapping(value = "stream_interface", method = { RequestMethod.POST, RequestMethod.GET })
	   @ResponseBody
	   public Response<Map<String,Object>> serviceStream(@RequestParam("method") final String method,
			   @RequestParam("v") final String v,
			   @RequestParam("app_key") final String appKey,   
			   @RequestParam("format") final String format,
			   @RequestParam("partner_id") final String partnerId,
			   @RequestParam("session") final String sessionKey, //使用密文密码 作为 session
			   @RequestParam("timestamp") final String timestamp,
			   final HttpServletResponse response) {
	     
		   logger.debug("=====execute ServiceInterfaceController.serviceStream");
	     
		   logger.debug("====系统参数:v="+v+",app_key:"+appKey+",format="+format+",partner_id="+partnerId+",session="+sessionKey+",timestamp="+timestamp);
		   
	 	    return new RestTemplateExecuter<Map<String,Object>>() {
	 	
	 			@SuppressWarnings("unchecked")
				@Override
	 			protected Response<Map<String,Object>> execute() throws Exception {
	 				
	 				 HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder
	 						.getRequestAttributes()).getRequest();
	 				 
	 				
	 				
	 				ServiceInterface  serviceInterface=null;
	 				try{
	 					Message msg =validateInterface(method);
	 					if(msg.getStatusCode()!=Message.StatusCode_OK){
	 						//错误
	 						logger.error(String.format("===接口验证：%1$s,%2$s",msg.getStatusCode(),msg.getMsg()));
	 					}else{
	 						//1.获取接口
		 					
		 				    serviceInterface=ArchCache.getInstance().getServiceInterfaceCache().get(method);
		 				    
		 				   @SuppressWarnings("rawtypes")
		 				   Map<String,Object>  paraMap =new LinkedHashMap<String, Object>();
		 				   Map<String,Object> reqMap =  request.getParameterMap();
		 				  
		 				   
		 				   
		 				  
		 				   
		 				   if(reqMap!=null){
		 					   Set<Entry<String, Object>>  setEntry= reqMap.entrySet();
		 					   
		 					   Iterator<Entry<String, Object>> iteEntry = setEntry.iterator();
		 					   
		 					   while(iteEntry.hasNext()){
		 						   
		 						   Entry<String,Object> entry = iteEntry.next();
		 						   
		 						   paraMap.put(entry.getKey(), ((String[])entry.getValue())[0]);
		 						   
		 					   }
		 				   }
		 				
		 				   
		 				   
		 				    boolean authSuccess=true;
		 				    if(1==serviceInterface.getIsNeedAuth()){
		 				    	//进行授权验证
		 				    	logger.debug("===进行身份验证");
		 				    	//使用 session 值处理
		 				    	
		 				    	
		 				    	
		 				    	User user =new User();
		 				    
		 				    	user.setAccount((String)paraMap.get("user_name"));
		 				    	
		 				    	user.setPassword(sessionKey);
		 				    	
		 				    	user = authService.sessionLogin(user);
		 				    	
		 				    	authSuccess = (user ==null?false:true);
		 				    
		 				    	paraMap.put("session_user", user);
		 				    	if(!authSuccess){
				 					  
			 					   logger.debug("===身份验证失败");
			 					   msg.setMsg("===身份验证失败");
			 					   msg.setStatusCode(Message.StatusCode_UnAuth);
		 				    	}
			 					  else{
			 				    	   logger.debug("===身份验证成功");
			 					  }
		 				    }
		 				    
		 				   if(authSuccess){
		 					  
	 				    		
	 				    	   long showtableConfigId = serviceInterface.getShowtableConfigId();
	 				    	   paraMap.put("showtableConfigId", showtableConfigId+"");
	 		 				   
	 				    	   MapParaQueryConditionDto<String,Object> queryCondition =new MapParaQueryConditionDto<String,Object>();
	 				    	   String dataStr = request.getParameter("data");
		 				 		
	 				    	  logger.debug(" data str: "+dataStr);
		 				 		
		 				 		Map<String, Object> data = JsonUtil.getMap4Json(request.getParameter("data"));
	 				 		
	 				    	   
	 		 				   processWhereCondition(data,queryCondition,showtableConfigId);
	 		 				   
	 		 				   processParameter(paraMap,queryCondition);
	 		 				   
	 		 				   
	 		 				   paraMap.put("WebRoot_path", request.getSession().getServletContext().getRealPath("/"));
	 		 				   paraMap.put("session_user", (User)request.getSession().getAttribute(Constant.KEY_SESSION_USER));
	 		 				   queryCondition.setMapParas(paraMap);
	 		 				   
	 		 				    TableQueryResult queryResult=  queryService.queryTableQueryResult(showtableConfigId, queryCondition);
	 		 				    if(queryResult!=null){
		 		 				    resp.setPageIndex(queryResult.getPageIndex());
		 		 				    resp.setPageSize(queryResult.getPageSize());
		 		 				    resp.setTotalRecord(queryResult.getCount());
		 		 				    resp.setPageCount(queryResult.getPageCount());
		 		 				 //   resp.setEntities(queryResult.getResultList());
		 		 				    
		 		 				    
		 		 				    
		 		 				  
		 		 				    
			 		 				 List<Map<String, Object>> rlist = queryResult.getResultList();
			 		 				//byte[] imgData =null; //图片流字节;
			 		 				if(rlist!=null && rlist.size()>0){
			 		 					Map<String, Object> item = rlist.get(0);
			 		 					Object binaryData = item.get("binary_data"); //获取图片 
			 		 					
			 		 					if(binaryData==null){
			 		 						
			 		 						logger.debug("===没有数据");
			 		 					
			 		 						response.getOutputStream().write("没有数据".getBytes(), 0, 0);
			 		 					}
			 		 					else{
			 		 						logger.debug(" binary data type:"+binaryData.getClass());
				 		 					final byte[] imgData= (byte[])binaryData;
				 		 					String fileExt = (String)item.get("file_type_ext");  //文件 扩展名
				 		 					String fileName = (String)item.get("file_name");  //文件 扩展名
				 		 					String attachment=(String)item.get("attachment");
				 		 					String contentType = "application/octet-stream";
				 		 					if(StringUtils.isEmpty(fileExt)){
				 		 						fileExt ="jpg";
				 		 						contentType="image/jpeg";
				 		 					}
				 		 					
				 		 					if(StringUtils.isEmpty(fileName)){
				 		 						fileName=UUID.randomUUID().toString();
				 		 					}
				 		 					String wae_attachement="";
				 		 					try{
				 		 						wae_attachement=data.get("wae_attachement").toString();
				 		 					}
				 		 					catch(Exception e)
				 		 					{
				 		 						wae_attachement="";
				 		 					}
				 		 					
				 		 					
				 		 					if(StringUtils.isEmpty(wae_attachement)){
					 		 					if(StringUtils.isEmpty(attachment)){
					 		 						attachment="0";
					 		 					}
				 		 					}else{
				 		 						attachment=wae_attachement;
				 		 					}
				 		 					
				 		 					String wae_file_ext="";
				 		 					try{
				 		 						wae_file_ext=data.get("wae_file_ext").toString();
				 		 						
				 		 						fileName=fileName+"."+wae_file_ext;
				 		 						
				 		 						if(wae_file_ext.toLowerCase().indexOf("jpg")!=-1){
				 		 							contentType="image/jpeg";
				 		 						}
				 		 						if(wae_file_ext.toLowerCase().indexOf("png")!=-1){
				 		 							contentType="image/png";
				 		 						}
				 		 						
				 		 					}
				 		 					catch(Exception e){
				 		 						wae_file_ext="";
				 		 					}
				 		 					
				 		 					
				 		 					
				 		 					response.setContentType(contentType);
				 		 					//如果文件名是中文，要经过URL编码
				 		 					if("1".equals(attachment)){
				 		 						String filenamefull=URLEncoder.encode(fileName,"UTF-8");
				 		 						response.setHeader("content-disposition","attachment;filename="+filenamefull);
				 		 					}
				 		 					ByteArrayInputStream instream =new ByteArrayInputStream(imgData);
				 		 					
				 		 					byte[] b = new byte[1024];
				 		 				    int len = -1;
				 		 				   ServletOutputStream outStream= response.getOutputStream();
				 		 				   
				 		 				   
				 		 				    while ((len = instream.read(b, 0, 1024)) != -1) {
				 		 				    	
				 		 				    	
				 		 				       try {
				 		 				    	  outStream.write(b, 0, len);
				 			 					} catch (IOException e) {
				 			 						// TODO Auto-generated catch block
				 			 						e.printStackTrace();
				 			 					}
				 		 				    }
				 		 				    try
				 		 				    {
				 		 				    	outStream.flush();
				 		 				    	outStream.close();
				 		 				    	if(instream!=null)
				 		 				    		instream.close();
				 		 				    }
				 		 				    finally{
				 		 				    	
				 		 				    }
				 		 		
				 		 				}
			 		 				}
	 		 				    }
	 		 				    
	 		 				    
	 		 				    resp.setTag(1);
	 		 				    
	 		 				    String msgTxt =String.format("调用接口 %1$s(%2$s) 成功 %3$s", serviceInterface.getInterfaceName(),serviceInterface.getInterfaceNameCn(),queryResult==null?"":queryResult.getMessage().getMsg());
	 		 				    
	 		 				    
	 		 				    msg.setStatusCode(Message.StatusCode_OK);
	 		 				    msg.setMsg(msgTxt);
	 		 				    
	 		 				   logger.debug(msgTxt);
	 				    		
	 				    	}
		 				    
		 				   
		 				}
	 					
	 					resp.setMessage(msg);
	 				    
	 				}
	 				catch(WaeRuntimeException e)
	 				{
	 					
String 					msgTxt =String.format("调用接口 %1$s(%2$s) 失败", serviceInterface.getInterfaceName(),serviceInterface.getInterfaceNameCn());
	 					
	 					if(retureExceptionToClient){
	 						msgTxt+= ":"+ e.getMessage();
	 					}
	 					
	 					resp.getMessage().setMsg(msgTxt);
	 					resp.getMessage().setStatusCode(Message.StatusCode_ServerError);
	 					 logger.error(msgTxt+":"+e.toString());
	 					 e.printStackTrace();
	 					 
	 					 
	 				}
	 				catch(Throwable e)
	 				{
	 					
	 					String msgTxt =String.format("调用接口 %1$s(%2$s) 失败", serviceInterface.getInterfaceName(),serviceInterface.getInterfaceNameCn());
	 					
	 					if(retureExceptionToClient){
	 						msgTxt+= ":"+ e.getMessage();
	 					}
	 					
	 					resp.getMessage().setMsg(msgTxt);
	 					resp.getMessage().setStatusCode(Message.StatusCode_ServerError);
	 					 logger.error(msgTxt+":"+e.toString());
	 					 e.printStackTrace();
	 				}
	 				return resp;
	 			}
	 	    }.run();
	     
	   }
	   
	   
	   
	   
	   
	   /**
	    * 搜索服务接口
	    * @author: wuhualong
	    * @data:2014-4-16下午12:49:37
	    * @function:
	    * @param method
	    * @param v
	    * @param appKey
	    * @param format
	    * @param partnerId
	    * @param sessionKey
	    * @param timestamp
	    * , produces = {"application/json;charset=UTF-8"}
	    * @return
	    */
	   
	   @Deprecated
	   @RequestMapping(value = "search_interface", method = { RequestMethod.POST, RequestMethod.GET })
	   @ResponseBody
	   public Response<Map<String,Object>> searchServiceInterface(@RequestParam("method") final String method,
			   @RequestParam("v") final String v,
			   @RequestParam("app_key") final String appKey,   
			   @RequestParam("format") final String format,
			   @RequestParam("partner_id") final String partnerId,
			   @RequestParam("session") final String sessionKey, //使用密文密码 作为 session
			   @RequestParam("timestamp") final String timestamp,
			   HttpServletResponse response) {
	     
		   logger.debug("=====execute ServiceInterfaceController.serviceInterface");
	     
		   logger.debug("====系统参数:v="+v+",app_key:"+appKey+",format="+format+",partner_id="+partnerId+",session="+sessionKey+",timestamp="+timestamp);
		   
		  // response.setContentType("application/"+format);
		 
		   /*  try {
				Thread.sleep(3000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}*/
	 	    return new RestTemplateExecuter<Map<String,Object>>() {
	 	
	 			@SuppressWarnings("unchecked")
				@Override
	 			protected Response<Map<String,Object>> execute() throws Exception {
	 				
	 				 HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder
	 						.getRequestAttributes()).getRequest();
	 				 
	 				
	 				
	 				ServiceInterface  serviceInterface=null;
	 				try{
	 					Message msg =validateInterface(method);
	 					if(msg.getStatusCode()!=Message.StatusCode_OK){
	 						//错误
	 						logger.error(String.format("===接口验证：%1$s,%2$s",msg.getStatusCode(),msg.getMsg()));
	 					}else{
	 						//1.获取接口
		 					
		 				   serviceInterface=ArchCache.getInstance().getServiceInterfaceCache().get(method);
		 				    
		 				   
		 				    
		 				    
		 				    
		 				   @SuppressWarnings("rawtypes")
		 				   Map<String,Object>  sortParamMap =new TreeMap<String, Object>();
		 				   Map<String,Object> reqMap =  request.getParameterMap();
		 				  
		 				   
		 				   
		 				  
		 				   
		 				   if(reqMap!=null){
		 					   Set<Entry<String, Object>>  setEntry= reqMap.entrySet();
		 					   
		 					   Iterator<Entry<String, Object>> iteEntry = setEntry.iterator();
		 					   
		 					   while(iteEntry.hasNext()){
		 						   
		 						   Entry<String,Object> entry = iteEntry.next();
		 						   
		 						   sortParamMap.put(entry.getKey(), ((String[])entry.getValue())[0]);
		 						   
		 					   }
		 				   }
		 				
		 				   
		 				   
		 				    boolean authSuccess=true;
		 				    if(1==serviceInterface.getIsNeedAuth()){
		 				    	//进行授权验证
		 				    	logger.debug("===进行身份验证");
		 				    	//使用 session 值处理
		 				    	
		 				    	
		 				    	
		 				    	User user =new User();
		 				    
		 				    	user.setAccount((String)sortParamMap.get("user_name"));
		 				    	
		 				    	user.setPassword(sessionKey);
		 				    	
		 				    	user = authService.sessionLogin(user);
		 				    	
		 				    	authSuccess = (user ==null?false:true);
		 				    
		 				    	sortParamMap.put("session_user", user);
		 				    	if(!authSuccess){
				 					  
			 					   logger.debug("===身份验证失败");
			 					   msg.setMsg("===身份验证失败");
			 					   msg.setStatusCode(Message.StatusCode_UnAuth);
		 				    	}
			 					  else{
			 				    	   logger.debug("===身份验证成功");
			 					  }
		 				    }
		 				    
		 				   if(authSuccess){
		 					  
	 				    		
	 				    	   long showtableConfigId = serviceInterface.getShowtableConfigId();
	 				    	   sortParamMap.put("showtableConfigId", showtableConfigId+"");
	 		 				   
	 				    	   MapParaQueryConditionDto<String,Object> queryCondition =new MapParaQueryConditionDto<String,Object>();
	 				    	   
	 				    	  String dataStr = request.getParameter("data");
	 				 		
	 				    	 logger.debug(" data str: "+dataStr);
		 				 		
		 				 		Map<String, Object> data = JsonUtil.getMap4Json(request.getParameter("data"));
	 				 		
	 				    	   
	 		 				   processWhereCondition(data,queryCondition,showtableConfigId);
	 		 				   processParameter(sortParamMap,queryCondition);
	 		 				   
	 		 				   
	 		 				   sortParamMap.put("WebRoot_path", request.getSession().getServletContext().getRealPath("/"));
	 		 				   
	 		 				   queryCondition.setMapParas(sortParamMap);
	 		 				   
	 		 				    TableQueryResult queryResult=  queryService.queryTableQueryResult(showtableConfigId, queryCondition);
	 		 				    if(queryResult!=null){
		 		 				    resp.setPageIndex(queryResult.getPageIndex());
		 		 				    resp.setPageSize(queryResult.getPageSize());
		 		 				    resp.setTotalRecord(queryResult.getCount());
		 		 				    resp.setPageCount(queryResult.getPageCount());
		 		 				    resp.setEntities(queryResult.getResultList());
		 		 				    
		 		 				   
		 		 				    
	 		 				    }
	 		 				    
	 		 				    
	 		 				    
	 		 				    
	 		 				    
	 		 				    resp.setTag(1);
	 		 				    
	 		 				    String msgTxt =String.format("调用接口 %1$s(%2$s) 成功", serviceInterface.getInterfaceName(),serviceInterface.getInterfaceNameCn());
	 		 				    msg.setStatusCode(Message.StatusCode_OK);
	 		 				    msg.setMsg(msgTxt);
	 		 				    
	 		 				   logger.debug(msgTxt);
	 				    		
	 				    	}
		 				    
		 				   
		 				}
	 					
	 					resp.setMessage(msg);
	 				    
	 				}
	 				catch(WaeRuntimeException e)
	 				{
	 					
	 					String msgTxt =String.format("调用接口 %1$s(%2$s) 失败", serviceInterface.getInterfaceName(),serviceInterface.getInterfaceNameCn());
	 					
	 					if(retureExceptionToClient){
	 						msgTxt+= ":"+ e.getMessage();
	 					}
	 					
	 					resp.getMessage().setMsg(msgTxt);
	 					resp.getMessage().setStatusCode(Message.StatusCode_ServerError);
	 					 logger.error(msgTxt+":"+e.toString());
	 					 e.printStackTrace();
	 				}
	 				catch(Throwable e)
	 				{
	 					
	 					String msgTxt =String.format("调用接口 %1$s(%2$s) 失败", serviceInterface.getInterfaceName(),serviceInterface.getInterfaceNameCn());
	 					
	 					if(retureExceptionToClient){
	 						msgTxt+= ":"+ e.getMessage();
	 					}
	 					
	 					resp.getMessage().setMsg(msgTxt);
	 					resp.getMessage().setStatusCode(Message.StatusCode_ServerError);
	 					 logger.error(msgTxt+":"+e.toString());
	 					 e.printStackTrace();
	 				}
	 				return resp;
	 			}
	 	    }.run();
	     
	   }
	   
	   
	   
	   
	   
	   /**
	    * 接口验证
	    * @author: wuhualong
	    * @data:2014-4-16上午11:23:33
	    * @param apiName: API名称
	    * @function:
	    * @return
	    */
	   private Message validateInterface(String apiName){
		   Message msg =new Message();
		
			if(StringUtils.isEmpty(apiName)){
				msg.setStatusCode(Message.StatusCode_ApiNameError);
				msg.setMsg("接口名称为空");
			}
			else{
				ServiceInterface  serviceInterface=ArchCache.getInstance().getServiceInterfaceCache().get(apiName);
				if(serviceInterface==null){
					msg.setStatusCode(Message.StatusCode_ApiNameError);
					msg.setMsg(String.format("找不到此接口:%1$s", apiName));
				}
				else if(serviceInterface.getEnabled()==0){
					
					msg.setStatusCode(Message.StatusCode_Disabled);
					msg.setMsg(String.format("接口未启用:%1$s", apiName));
					
				}
			}
			
			return msg;
			
	   }
	   
	   
	/****
	 * 处理参数
	 */
	private void processWhereCondition(Map<String, Object> paraMap,
			MapParaQueryConditionDto<String, Object> queryCondition,
			long searchShowTableConfigId) {

		logger.debug("===多条件组合搜索参数处理...");

		
		Map<String, Object> dataMap = paraMap;
		StringBuffer sb = new StringBuffer();
		if (dataMap != null) {
			// 设置 whereCluster 和 wherepParameterValues

			// List<ShowDataConfig> showDataConfigs =
			// ArchCache.getInstance().getShowTableConfigCache().getSearchShowDataConfigs(showtableConfigId);
			List<ShowDataConfig> showDataConfigs = ArchCache.getInstance()
					.getShowTableConfigCache() 
					.getSearchShowDataConfigs(searchShowTableConfigId);

			Set<Entry<String, Object>> set_entry = dataMap.entrySet();
			Iterator<Entry<String, Object>> ite = set_entry.iterator();

			// 保存 wherepParameterValues
			List<Object> whereValues = new ArrayList<Object>();

			sb.append(" 1=1 ");

			while (ite.hasNext()) {
				Entry<String, Object> entry = ite.next();
				String name = entry.getKey();
				Object value = entry.getValue();

				int dataType = ConstantsCache.DataType.DATATYPE_STRING;

				ShowDataConfig showDataConfig = null;
				for (ShowDataConfig sdf : showDataConfigs) {
					if (sdf.getFieldNameAlias().equals(name)) {
						//别名判断
						dataType = sdf.getDataType();
						showDataConfig = sdf;
						break;
					}
				}
				if(showDataConfig==null)
					continue;

				if (dataType != ConstantsCache.DataType.DATATYPE_DATE
						&& showDataConfig.getShowType() != ConstantsCache.ControlType.CONTROLTYPE_NUMBER_BETWEEN) {
					if (StringUtils.isEmpty("" + value)) {
						continue;
					}
				}

				if(showDataConfig.getShowType() ==ConstantsCache.ControlType.CONTROLTYPE_NOTEQUAL_OPERATION){
					// 不等于 操作
					sb.append(" and ");
					sb.append(name).append(" <> ").append("?");
					whereValues.add(value);
					continue;
				}
				
				if(showDataConfig.getShowType() ==ConstantsCache.ControlType.CONTROLTYPE_EQUAL_OPERATION){
					// 不等于 操作
					sb.append(" and ");
					sb.append(name).append(" = ").append("?");
					whereValues.add(value);
					continue;
				}
				
				else if(showDataConfig.getShowType() ==ConstantsCache.ControlType.CONTROLTYPE_IN_OPERATION){
					// in 操作
					sb.append(" and ");
					sb.append(name).append(" in ").append("(");
					
					String[] values = StringUtils.split((String)value,',');
					
					StringBuffer sb_v= new StringBuffer();
					
					for(String str:values){
						sb_v.append(",").append("'"+str+"'");
					}
					sb_v.deleteCharAt(0);
					
					sb.append(sb_v.toString());
					sb.append(")");
					
					//whereValues.add(value);
					continue;
				}
				else if(showDataConfig.getShowType() ==ConstantsCache.ControlType.CONTROLTYPE_NOTIN_OPERATION){
					// not in 操作
					sb.append(" and ");
					sb.append(name).append(" not in ").append("(");
					
					String[] values = StringUtils.split((String)value,',');
					
					StringBuffer sb_v= new StringBuffer();
					
					for(String str:values){
						sb_v.append(",").append("'"+str+"'");
					}
					sb_v.deleteCharAt(0);
					
					sb.append(sb_v.toString());
					sb.append(")");
					continue;
				}
				
				else if(showDataConfig.getShowType() ==ConstantsCache.ControlType.CONTROLTYPE_OR_OPERATION){
					//或者  操作
					/*sb.append(" and ");
					sb.append(name).append(" = ").append("?");
					whereValues.add(value);*/
					
					String fields = showDataConfig.findExtPropertiesMap().get("fields").toString(); //约定使用 fields 为键
					String[] fieldArr = fields.split(",");
					
					sb.append(" and ");
					sb.append(" ( ");
					for(int tt=0;tt<fieldArr.length;tt++){
						String field= fieldArr[tt];
						if(tt!=0){
							sb.append(" or ");
						}
						sb.append(field).append(" = ").append(" ? ");
						whereValues.add(value);
					}
					
					sb.append(" ) ");
				
					
					continue;
				}
				
				else if(showDataConfig.getShowType() ==ConstantsCache.ControlType.CONTROLTYPE_GREATTHEN_OPERATION){
					// 不等于 操作
					sb.append(" and ");
					sb.append(name).append(" >= ").append("?");
					whereValues.add(value);
					continue;
				}
				else if(showDataConfig.getShowType() ==ConstantsCache.ControlType.CONTROLTYPE_LESSTHAN_OPERATION){
					// 不等于 操作
					sb.append(" and ");
					sb.append(name).append(" <= ").append("?");
					whereValues.add(value);
					continue;
				}
				
				
				/*
				
				else if(showDataConfig.getShowType() ==ConstantsCache.ControlType.CONTROLTYPE_BINARY_FILE){
					// 二进制图片存储
					
					try {
						
						MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;  
				        MultipartFile multipartFile = multipartRequest.getFile(name); 
				        logger.debug(" == file:"+multipartFile.getName()+" ,contenttype:"+multipartFile.getContentType());
						
				        InputStream is = multipartFile.getInputStream();
				        byte[] b =new byte[is.available()];
				        is.read(b);
				        is.close();
				        
						sb.append(" and ");
						sb.append(name).append("=?");
						whereValues.add(b);
						
						
						paraMap.remove(name);
						paraMap.put(name, b);
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
			
					continue;
				}*/
				
				
				
				
				// 根据 dataType 判断数据类型

				switch (dataType) {
				case ConstantsCache.DataType.DATATYPE_STRING:
					// 字符串 做 like

					sb.append(" and ");
					sb.append(name).append(" like ? ");
					whereValues.add("%" + StringUtils.trim((String) value)
							+ "%");
					break;
				case ConstantsCache.DataType.DATATYPE_NUMBER:
				case ConstantsCache.DataType.DATATYPE_FLOAT:
					// case 6:
					// 数字类型 =
					// 如果是 showType为区间显示，那么使用 <= 否则 >=
					
					if(showDataConfig.getShowType() ==ConstantsCache.ControlType.CONTROLTYPE_NUMBER_BETWEEN)
					{
						if(!StringUtils.isEmpty((String)value))
						{
							sb.append(" and ");
							sb.append(name).append(" >= ? ");
							whereValues.add(Long.parseLong(value.toString()));
						}
						
						
						ite.hasNext();
						entry = ite.next();
						value = entry.getValue();
						
						if(!StringUtils.isEmpty((String)value))
						{
							sb.append(" and ");
							sb.append(name).append(" <= ? ");
							whereValues.add(Long.parseLong(value.toString()));
						}
						
					}
					
					
				
					
					else{
						sb.append(" and ");
						sb.append(name).append("=?");
						whereValues.add(value);
						
					}

					break;

				case ConstantsCache.DataType.DATATYPE_DATE:
					// 日期 between and
					try
					{
						Date startDate = Common.parseDate((String) value,Common.DATE_FORMAT_FULL);
						if (startDate != null) {
							sb.append(" and ");
							sb.append(name).append(" >= ?");
							whereValues.add(startDate);
						}
	
						ite.hasNext();
						entry = ite.next();
						value = entry.getValue();
	
						Date endDate = Common.parseDate((String) value,Common.DATE_FORMAT_FULL);
						if (endDate != null) {
							sb.append(" and ");
							sb.append(name).append(" <= ?");
							whereValues.add(endDate);
						}
					}
					catch(Exception e){
						logger.error("日期字段错误");
					}
					break;

				}

			}
			
			
			
			
			
			

			logger.debug("通用搜索WhereSQL:" + sb.toString());
			queryCondition.setWhereCluster(sb.toString());

			List<Object> whereP = new ArrayList<Object>();
			// 添加参数值
			whereP.addAll(Arrays.asList(whereValues.toArray()));

			if (queryCondition.getWherepParameterValues() != null) {
				// 添加 地址参数值
				whereP.addAll(Arrays.asList(queryCondition
						.getWherepParameterValues()));
			}

			// queryCondition.setWherepParameterValues(whereValues.toArray());
			queryCondition.setWherepParameterValues(whereP.toArray());
		}
	}

	   /****
		 * 处理参数
		 */
		private void processParameter(Map<String,Object> paraMap,QueryCondition<Object[]> queryCondition)
		{
			
			
			String sortField =paraMap.get("sortField")==null?null:(String)paraMap.get("sortField");
			String sortDirect = paraMap.get("sortDirect")==null?null:(String)paraMap.get("sortDirect");
			
			
			
			
			
			int pageIndex = 1;
			int pageSize = 10;
			try{
				pageIndex = Integer.parseInt(String.valueOf(paraMap.get("pageIndex")));
				if(pageIndex<1){
					pageIndex=1;
				}
			}
			catch(Exception e){
				pageIndex=1;
			}
			try{
				pageSize = Integer.parseInt(String.valueOf(paraMap.get("pageSize")));
				if(pageSize<1){
					ServiceInterface serviceInterface=ArchCache.getInstance().getServiceInterfaceCache().get(paraMap.get("method").toString());
					pageSize=ArchCache.getInstance().getShowTableConfigCache().get(serviceInterface.getShowtableConfigId()).getPageSize();
					if(pageSize==0){
						pageSize=10;
					}
				}
			}
			catch(Exception e){
				ServiceInterface serviceInterface=ArchCache.getInstance().getServiceInterfaceCache().get(paraMap.get("method").toString());
				pageSize=ArchCache.getInstance().getShowTableConfigCache().get(serviceInterface.getShowtableConfigId()).getPageSize();
				if(pageSize==0){
					pageSize=10;
				}
			}
			
			LinkedHashMap<String, String> sortMap = new LinkedHashMap<String, String>();
			if(!StringUtils.isEmpty(sortField) && !StringUtils.isEmpty(sortDirect)){
				sortMap.put(sortField, sortDirect);
			}
			
			
			queryCondition.setOrderBy(sortMap);
			
			queryCondition.setPageIndex(pageIndex);
			queryCondition.setPageSize(pageSize);
		}
		
		
		/***
		 * 对象转json字符串
		 * @param obj
		 * @param pExcludeProperties
		 * @param pIncludeProperties
		 * @return
		 */
		private String objToJsonString(Object obj,String pExcludeProperties,String pIncludeProperties)
		{
			String[] excludeProperties=StringUtils.split(pExcludeProperties, ",");
			
			Collection<Pattern> excludePropertiesPatterns=null;
			if(excludeProperties!=null &&excludeProperties.length>0){
				excludePropertiesPatterns=new ArrayList<Pattern>();
				for(int i=0;i<excludeProperties.length;i++){
					Pattern p = Pattern.compile("entities\\[\\d+\\]\\."+excludeProperties[i]);
					excludePropertiesPatterns.add(p);
				}
			}
			
			String[] includeProperties=StringUtils.split(pIncludeProperties, ",");
			
			Collection<Pattern> includePropertiesPatterns=null;
			if(includeProperties!=null &&includeProperties.length>0){
				includePropertiesPatterns=new ArrayList<Pattern>();
				for(int i=0;i<includeProperties.length;i++){
					Pattern p = Pattern.compile("entities\\[\\d+\\]\\."+includeProperties[i]);
					includePropertiesPatterns.add(p);
				}
			}
			
			String jsonString="";
			try {
				jsonString = JSONUtil.serialize(obj, excludePropertiesPatterns, includePropertiesPatterns, false, false);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return jsonString;
		}
	   
		
		
		
		 /**
		  * 参数是以字符串形式传递 获取参数需要 req.getInputStream();
		    * 测试:http://localhost:8080/IronArch/service/rest/interface?userName=dragon&password=%23%23%23%23%23%23%23%23&method=aps.user.login&v=1.0&session=1212&app_key=testKey&format=json&partner_id=aps-sdk-net-20140410&timestamp=2014-04-16+12%3a32%3a02&sign=FD2E981B4B2BE1EA19CC967F48F6C792
		    * @author: wuhualong
		    * @data:2014-4-16下午12:49:37
		    * @function:
		    * @param method
		    * @param v
		    * @param appKey
		    * @param format
		    * @param partnerId
		    * @param sessionKey
		    * @param timestamp
		    * , produces = {"application/json;charset=UTF-8"}
		    * @return
		    */
		   @RequestMapping(value = "datastring_interface", method = { RequestMethod.POST, RequestMethod.GET })
		   @ResponseBody
		   public Response<Map<String,Object>> serviceDataStringInterface(@RequestParam("method") final String method,
				   @RequestParam("v") final String v,
				   @RequestParam("app_key") final String appKey,   
				   @RequestParam("format") final String format,
				   @RequestParam("partner_id") final String partnerId,
				   @RequestParam("session") final String sessionKey, //使用account md5后的32位hex  作为 session
				   @RequestParam("timestamp") final String timestamp,
				   HttpServletResponse response) {
		     
			   logger.debug("=====execute ServiceInterfaceController.serviceInterface");
		     
			   logger.info("====系统参数:v="+v+",app_key:"+appKey+",format="+format+",partner_id="+partnerId+",session="+sessionKey+",timestamp="+timestamp);
			   
			  // response.setContentType("application/"+format);
			 
			   /*  try {
					Thread.sleep(3000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}*/
			 
			   
			    final Date startTime =new Date();
			    
		 	    return new RestTemplateExecuter<Map<String,Object>>() {
		 	
		 			@SuppressWarnings("unchecked")
					@Override
		 			protected Response<Map<String,Object>> execute() throws Exception {
		 				
		 				 HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder
		 						.getRequestAttributes()).getRequest();
		 				 
		 				  String excludePropertiesString =request.getParameter("excludeProperties");
		 				  String includePropertiesString =request.getParameter("includeProperties");
		 				   
		 				  String[] excludeProperties=StringUtils.split(excludePropertiesString, ",");
		 				   
		 				  
		 				 /** 读取接收到的l消息 */  
		 				  String dataString="{}";
					        StringBuffer sb = new StringBuffer();  
					        InputStream is=null;
							try {
								is = request.getInputStream();
								 InputStreamReader isr = new InputStreamReader(is, "UTF-8");  
						        BufferedReader br = new BufferedReader(isr);  
						        String s = "";  
						        while ((s = br.readLine()) != null) {  
						            sb.append(s);  
						        }  
						        
							}
							catch(Exception e){
								logger.error(e);
								
							}
							dataString = sb.toString(); //次即为接收到微信端发送过来的数据 
		 				  
		 				  logger.info("=======dataString:"+dataString);
		 				  
		 				  
		 				  
		 				ServiceInterfaceLog log = new ServiceInterfaceLog();
		 				log.setInvokeTime(new Date());
		 				log.setInterfaceName(method);
		 				
		 						
		 				 
		 				ServiceInterface  serviceInterface=null;
		 				try{
		 					Message msg =validateInterface(method);
		 					if(msg.getStatusCode()!=Message.StatusCode_OK){
		 						//错误
		 						logger.error(String.format("===接口验证：%1$s,%2$s",msg.getStatusCode(),msg.getMsg()));
		 					}else{
		 						//1.获取接口
			 					
			 				    serviceInterface=ArchCache.getInstance().getServiceInterfaceCache().get(method);
			 				    
			 				   @SuppressWarnings("rawtypes")
	  		 				   Map<String,Object>  paraMap =new FinderLinkedMap<String, Object>();
			 				   Map<String,Object> reqMap =  request.getParameterMap();
			 				  
			 				 
			 				   if(serviceInterface.getEnableLog()==1){
			 					   //启用日志
			 					  
			 					   log.setInterfaceName(serviceInterface.getInterfaceName());
			 					   log.setInterfaceNameCn(serviceInterface.getInterfaceNameCn());
			 					   log.setShowtableConfigId(serviceInterface.getShowtableConfigId());
			 					   log.setIsNeedAuth(serviceInterface.getIsNeedAuth());
			 					   log.setVersion(serviceInterface.getVersion());
			 					   log.setGroupName(serviceInterface.getGroupName());
			 					   log.setRemark(serviceInterface.getRemark());
			 					   log.setEnabled(serviceInterface.getEnabled());
			 					   
			 					   
			 					  Map<String,Object>  map =new FinderLinkedMap<String, Object>();
			 					   Set<Entry<String, Object>>  setEntry= reqMap.entrySet();
			 					   
			 					   Iterator<Entry<String, Object>> iteEntry = setEntry.iterator();
			 					   
			 					   while(iteEntry.hasNext()){
			 						   
			 						   Entry<String,Object> entry = iteEntry.next();
			 						   
			 						  map.put(entry.getKey(), ((String[])entry.getValue())[0]);
			 						   
			 					   }
			 					   
			 					  log.setInputContent(JSONArray.fromObject(map).toString());
			 					   
			 					 // commonService.addServiceInterfaceLog(log);
			 					   
			 					   
			 				   }
			 				  
			 				   
			 				   if(reqMap!=null){
			 					   Set<Entry<String, Object>>  setEntry= reqMap.entrySet();
			 					   
			 					   Iterator<Entry<String, Object>> iteEntry = setEntry.iterator();
			 					   
			 					   while(iteEntry.hasNext()){
			 						   
			 						   Entry<String,Object> entry = iteEntry.next();
			 						   
			 						   paraMap.put(entry.getKey(), ((String[])entry.getValue())[0]);
			 						   
			 					   }
			 				   }
			 				
			 				
			 				   
			 				    boolean authSuccess=true;
			 				    if(1==serviceInterface.getIsNeedAuth()){
			 				    	//进行授权验证
			 				    	logger.debug("===进行身份验证");
			 				    	//使用 session 值处理
			 				    	
			 				    	
			 				    	
			 				    	/*User user =new User();
			 				    
			 				    	user.setAccount((String)paraMap.get("user_name"));
			 				    	
			 				    	user.setPassword(sessionKey);
			 				    	
			 				    	user = authService.sessionLogin(user);
			 				    	
			 				    	authSuccess = (user ==null?false:true);
			 				    
			 				    	paraMap.put("session_user", user);*/
			 				    	
			 				    	String user_name= (String)paraMap.get("user_name");
			 				    	if(EncoderHandler.encodeByMD5(user_name).equalsIgnoreCase(sessionKey)){
			 				    		authSuccess=true;
			 				    	}
			 				    	else{
			 				    		authSuccess=false;
			 				    	}
			 				    	
			 				    	if(!authSuccess){
					 					  
				 					   logger.debug("===身份验证失败");
				 					   msg.setMsg("===身份验证失败");
				 					   msg.setStatusCode(Message.StatusCode_UnAuth);
			 				    	}
				 					  else{
				 				    	   logger.debug("===身份验证成功");
				 					  }
			 				    }
			 				    
			 				   if(authSuccess){
			 					  
		 				    		
		 				    	   long showtableConfigId = serviceInterface.getShowtableConfigId();
		 				    	   paraMap.put("showtableConfigId", showtableConfigId+"");
		 		 				   
		 				    	   paraMap.put("table001abcX", ArchCache.getInstance().getShowTableConfigCache().get(showtableConfigId).getShowTableName());
		 				    	   
		 		 				   
		 		 				   
		 		 				   MapParaQueryConditionDto<String,Object> queryCondition =new MapParaQueryConditionDto<String,Object>();
		 				    	   String dataStr = request.getParameter("data");
			 				 		
			 				 		logger.info(" data str: "+dataStr);
			 				 		
			 				 		Map<String, Object> data = JsonUtil.getMap4Json(dataStr);
		 				 		
		 				    	   
		 		 				   processWhereCondition(data,queryCondition,showtableConfigId);
		 		 				   
		 		 				   processParameter(paraMap,queryCondition);
		 		 				   
		 		 				   
		 		 				   paraMap.put("WebRoot_path", request.getSession().getServletContext().getRealPath("/"));
		 		 				   paraMap.put("session_user", (User)request.getSession().getAttribute(Constant.KEY_SESSION_USER));
		 		 				   queryCondition.setMapParas(paraMap);
		 		 				   
		 		 				    TableQueryResult queryResult=  queryService.queryTableQueryResult(showtableConfigId, queryCondition);
		 		 				    if(queryResult!=null){
			 		 				    resp.setPageIndex(queryResult.getPageIndex());
			 		 				    resp.setPageSize(queryResult.getPageSize());
			 		 				    resp.setTotalRecord(queryResult.getCount());
			 		 				    resp.setPageCount(queryResult.getPageCount());
			 		 				    resp.setEntities(queryResult.getResultList());
			 		 				  boolean fieldsSer=true;
			 		 				    if(excludeProperties!=null &&excludeProperties.length>0){
			 		 				    	
			 		 				    	for(int k=0;k<excludeProperties.length;k++){
			 		 				    		if("fields".equals(excludeProperties[k])){
			 		 				    			fieldsSer =false;
			 		 				    			break;
			 		 				    		}
			 		 				    	}
			 		 				    	
			 		 				    }
			 		 				  if(fieldsSer)
		 		 				    		resp.setFields(queryResult.getFields());
			 		 				    
		 		 				    }
		 		 				    resp.setTag(1);
		 		 				    
		 		 				    String msgTxt =String.format("调用接口 %1$s(%2$s) 成功 %3$s", serviceInterface.getInterfaceName(),serviceInterface.getInterfaceNameCn(),
		 		 				    		queryResult==null?"":(queryResult.getMessage().getMsg()==null?"":queryResult.getMessage().getMsg()));
		 		 				    
		 		 				    
		 		 				    msg.setStatusCode(Message.StatusCode_OK);
		 		 				    msg.setMsg(msgTxt);
		 		 				    
		 		 				  
		 		 				   logger.debug(msgTxt);
		 		 				   
		 				    		
		 				    	}
			 				    
			 				   
			 				}
		 					
		 					resp.setMessage(msg);
		 					
		 					
		 					
	 						 log.setStatusCode(Message.StatusCode_OK+"");
	 						log.setOutMsg(objToJsonString(resp,excludePropertiesString,includePropertiesString));
		 					 
	 						logger.debug(log.toString());
		 				    
		 				}
		 				catch(InfoException infoExp){
		 					resp.getMessage().setMsg(infoExp.getErrorInfo().getErrDesc());
		 					resp.getMessage().setStatusCode(Message.StatusCode_InfoExp);
		 					
		 					
		 					log.setStatusCode(Message.StatusCode_InfoExp+"");
							log.setOutMsg(infoExp.getErrorInfo().getErrDesc());
							
							logger.debug(infoExp.getErrorInfo().getErrDesc());
							
							
		 				}
		 				catch(Throwable e)
		 				{
		 					
		 					String msgTxt =String.format("调用接口 %1$s(%2$s) 失败 开始时间: %3$s 结束时间: %4$s", serviceInterface.getInterfaceName(),serviceInterface.getInterfaceNameCn(),DateUtils.formatDate(startTime),DateUtils.formatDate(new Date()));
		 					
		 					if(retureExceptionToClient){
		 						msgTxt+= ":"+ e.toString();
		 					}
		 					
		 					resp.getMessage().setMsg(msgTxt);
		 					resp.getMessage().setStatusCode(Message.StatusCode_ServerError);
		 					 logger.error(msgTxt);
		 					 
	 						 log.setStatusCode(Message.StatusCode_ServerError+"");
	 						 log.setOutMsg(msgTxt);
		 					 
	 						logger.error(log.toString());
	 						logger.error(e.toString());
		 				}
		 				finally
		 				{
		 					try
		 					{
		 						if(ArchCache.getInstance().getSysConfigCache().get(SysConfigCache.KEY_CONFIG_INTERFACELOG_ENABLE).getIntValue()==1 && serviceInterface!=null && serviceInterface.getEnableLog()==1){
		 						
		 							//commonService.updateServiceInterfaceLog(log);
		 							log.setCompleteTime(new Date());
		 							commonService.addServiceInterfaceLog(log);
		 						
		 						}
		 						
		 					}
		 					finally{
		 						
		 					}
		 				}
		 				return resp;
		 			}
		 	    }.run();
		     
		   }
		   
		   
		   
		   
}
