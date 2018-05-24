package cn.finder.wae.common.base;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import cn.finder.wae.business.domain.Message;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.domain.User;
import cn.finder.wae.common.comm.DateJsonValueProcessor;
import cn.finder.wae.common.constant.Constant;
import cn.finder.wae.common.exception.InfoException;
import cn.finder.wae.common.exception.ResponseMsg;
import cn.finder.wae.common.exception.WaeRuntimeException;
import cn.finder.wae.helper.EmailHelper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsDateJsonBeanProcessor;
import net.sf.json.util.CycleDetectionStrategy;

public abstract class BaseActionSupport extends ActionSupport implements
		ServletRequestAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected PageContext pageContext;
	protected HttpServletRequest request;
	protected HttpSession session;
	protected ServletContext application;
	protected HttpServletResponse response;
	protected final static String SUCCESS = "success";
	protected final static String FAILURE = "failure";
	protected final static String WARNING = "warning";
	protected final static String ERROR = "error";
	
	/***
	 *JSON序列化的时候 ，去掉 ActionContent中
	 */
	private static String[] jsonExcludeProperties=new String[]{"actionErrors",
							"actionMessages",
							"errorMessages",
							"errors",
							"excludeProperties",
							"includeProperties",
							"fieldErrors",
							"locale",
							"texts",
							"downLoadInputStream",
							"queryCondition",
							"exportFileName"
							};

	protected String retFlag = SUCCESS;

	// for json ,we need to difine this field to themself filed
	protected String msg;
	

	@Override
	public void setServletRequest(HttpServletRequest request) {
		ActionContext context = ActionContext.getContext();
		this.request = request;
		session = request.getSession();
		application = session.getServletContext();
		response  = (HttpServletResponse) context.get(ServletActionContext.HTTP_RESPONSE);
		
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public  User findSessionUser()
	{
		return (User)session.getAttribute(Constant.KEY_SESSION_USER);
	}

	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	
	
	public void responseJson(String jsonString)
	{
		  HttpServletResponse response = ServletActionContext.getResponse();
	      response.setContentType("text/json; charset=utf-8");
          try {  
	    		 response.getWriter().write(jsonString); //处理json字符串
	      } catch (IOException e) {
	    		 e.printStackTrace();
	      } 
	}
	
	
	/***
	 * obj 为 Collection 或者 	对象类型
	 * @param obj
	 */
	@SuppressWarnings("rawtypes")
	public void responseJson(Object obj)
	{
		if(obj instanceof Collection)
		{
			JSONArray ja = JSONArray.fromObject((Collection)obj);
			responseJson(ja.toString());
		}
		else 
		{
			JSONObject jo = JSONObject.fromObject(obj);
			responseJson(jo.toString());
		}
	}
	
	
	
	/****
	 * 出入的数据对象是当前 ActionContent
	 * @author Administrator
	 *
	 */
	 protected abstract class ActionTemplateExecuter {

			private final  Logger logger=Logger.getLogger(ActionTemplateExecuter.class);
			public String result="success";
			
			public  String run()
			{
				try{
					result=execute();
				//	result="success";
				}
				catch(InfoException infoEx){
					ResponseMsg msg = new ResponseMsg();
					msg.setOk(false);
					msg.setMsg(infoEx.getErrorInfo().getErrDesc());
					assignMsg(infoEx.getErrorInfo().getErrDesc());
				}
				
				catch(WaeRuntimeException e)
				{
					logger.error(e.toString());
					ResponseMsg msg = new ResponseMsg();
					msg.setOk(false);
					msg.setMsg(e.getMessage());
					assignMsg(e.getMessage());
					new EmailHelper().sendEmailToAdminInThread("WAE异常", e.getErrInfo().getErrMsg());
				}
				
				catch(Throwable e)
				{
					logger.error(e.toString());
					ResponseMsg msg = new ResponseMsg();
					msg.setOk(false);
					msg.setMsg("操作异常，请联系管理员");
					assignMsg("操作异常，请联系管理员");
					new EmailHelper().sendEmailToAdminInThread("异常", e.toString());
				}
				
				
				finally{
				}
				return result;
			}
			
			
			/***
			 * 主要传入的是 ActionContent
			 * @param obj
			 * @param pExcludeProperties
			 * @param pIncludeProperties
			 */
			public  void responseJsonString(Object obj,String pExcludeProperties,String pIncludeProperties)
			{
				try{
					result=execute();
					
					
				}
				catch(InfoException infoEx){
					ResponseMsg msg = new ResponseMsg();
					msg.setOk(false);
					msg.setMsg(infoEx.getErrorInfo().getErrDesc());
					assignMsg(infoEx.getErrorInfo().getErrDesc());
				}
				catch(WaeRuntimeException e)
				{
					logger.error(e.toString());
					ResponseMsg msg = new ResponseMsg();
					msg.setOk(false);
					msg.setMsg(e.getMessage());
					
					assignMsg(e.getMessage());
					
					new EmailHelper().sendEmailToAdminInThread("WAE异常", e.getErrInfo().getErrMsg());
				}
				
				catch(Throwable e)
				{
					logger.error(e.toString());
					ResponseMsg msg = new ResponseMsg();
					msg.setOk(false);
					msg.setMsg(e.toString());
					new EmailHelper().sendEmailToAdminInThread("异常", e.toString());
				}
				finally{
					
					
				/*	String[] excludeProperties=StringUtils.split(pExcludeProperties, ",");
					
					Collection<Pattern> excludePropertiesPatterns=new ArrayList<Pattern>();
					excludePropertiesPatterns.add(Pattern.compile("actionMessages"));
					excludePropertiesPatterns.add(Pattern.compile("errorMessages"));
					excludePropertiesPatterns.add(Pattern.compile("errors"));
					excludePropertiesPatterns.add(Pattern.compile("actionErrors"));
					excludePropertiesPatterns.add(Pattern.compile("fieldErrors"));
					excludePropertiesPatterns.add(Pattern.compile("locale"));
					excludePropertiesPatterns.add(Pattern.compile("texts"));
					
					excludePropertiesPatterns.add(Pattern.compile("texts"));
					
					
					//excludePropertiesPatterns.add(Pattern.compile("\\w+\\.showTableConfig"));
					if(excludeProperties!=null &&excludeProperties.length>0){
						
						for(int i=0;i<excludeProperties.length;i++){
							Pattern p = Pattern.compile(""+excludeProperties[i]);
							excludePropertiesPatterns.add(p);
						}
					}
					
					String[] includeProperties=StringUtils.split(pIncludeProperties, ",");
					
					Collection<Pattern> includePropertiesPatterns=null;
					if(includeProperties!=null &&includeProperties.length>0){
						includePropertiesPatterns=new ArrayList<Pattern>();
						for(int i=0;i<includeProperties.length;i++){
							Pattern p = Pattern.compile(""+includeProperties[i]);
							includePropertiesPatterns.add(p);
						}
					}
					
					String respString="";
					try {
						respString = JSONUtil.serialize(obj, excludePropertiesPatterns, includePropertiesPatterns, false, false);
					} catch (org.apache.struts2.json.JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					responseJson(respString);*/
					
					
					List<String> arrExcludeProperties =new ArrayList<String>();
					
					List<String> defaultExcludeProperties =Arrays.asList(jsonExcludeProperties);
					
					arrExcludeProperties.addAll(defaultExcludeProperties);
					String[] excludeProperties=StringUtils.split(pExcludeProperties, ",");
					
					if(excludeProperties!=null){
						for(String ep:excludeProperties){
							arrExcludeProperties.add(ep);
						}
					}
					JsonConfig jsonConfig = new JsonConfig();
					
					jsonConfig.setExcludes(arrExcludeProperties.toArray(new String[arrExcludeProperties.size()]));  //过滤掉不需要的属性
					jsonConfig.setIgnoreDefaultExcludes(false);//DEFAULT_EXCLUDES    "class", "declaringClass",  "metaClass" 
				    jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT); //去掉自包含
		//		    jsonConfig.setIgnoreJPATransient(true);
				//    jsonConfig.setIgnoreTransientFields(true);
				   /* jsonConfig.registerJsonBeanProcessor(Date.class,  
				            new JsDateJsonBeanProcessor());*/ // 当输出时间格式时，采用和JS兼容的格式输出  
				    jsonConfig.registerJsonValueProcessor(Date.class,new DateJsonValueProcessor());
				    JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(obj, jsonConfig);
				    
				   // logger.info("===返回给客户端JSON 数据:"+jsonObject.toString());
				    responseJson(jsonObject.toString());
				    
				    
				    
				}
			}
			
			
			
			
			/***
			 * 主要传入的是 ActionContent
			 * @param obj
			 * @param pExcludeProperties
			 * @param pIncludeProperties
			 */
			public  void responseXmlString(Object obj,String pExcludeProperties,String pIncludeProperties)
			{
				try{
					result=execute();
					
					
				}
				catch(InfoException infoEx){
					ResponseMsg msg = new ResponseMsg();
					msg.setOk(false);
					msg.setMsg(infoEx.getErrorInfo().getErrDesc());
					assignMsg(infoEx.getErrorInfo().getErrDesc());
				}
				catch(WaeRuntimeException e)
				{
					logger.error(e.toString());
					ResponseMsg msg = new ResponseMsg();
					msg.setOk(false);
					msg.setMsg(e.getMessage());
					
					
					assignMsg(e.getMessage());
					
					new EmailHelper().sendEmailToAdminInThread("WAE异常", e.getErrInfo().getErrMsg());
				}
				
				catch(Throwable e)
				{
					logger.error(e.toString());
					ResponseMsg msg = new ResponseMsg();
					msg.setOk(false);
					msg.setMsg(e.toString());
					new EmailHelper().sendEmailToAdminInThread("异常", e.toString());
				}
				finally{
					//对象 obj 转XML
					
					
				}
			}
			
			
			
			protected abstract  String execute()  throws Exception;
			
			
			protected abstract void assignMsg(String msg);
				
		}
	

		class A{
			private TableQueryResult queryResult;

			public TableQueryResult getQueryResult() {
				return queryResult;
			}

			public void setQueryResult(TableQueryResult queryResult) {
				this.queryResult = queryResult;
			}

			
		}
	 protected abstract class TemplateExecuter<T> {

		private final  Logger logger=Logger.getLogger(TemplateExecuter.class);
		
		/***
		 * 任意对象
		 */
		public T resp=null;
		
		public  void run()
		{
			TableQueryResult queryResult=new TableQueryResult();
			queryResult.setCount(0l);
			try{
				
				resp=execute();
				
			}
			catch(InfoException infoEx){
				//没有特殊处理
				ResponseMsg msg = new ResponseMsg();
				msg.setOk(false);
				msg.setMsg(infoEx.getErrorInfo().getErrDesc());
				queryResult.getMessage().setStatusCode(Message.StatusCode_OK);
				queryResult.getMessage().setMsg(infoEx.getErrorInfo().getErrDesc());
			}
			catch(WaeRuntimeException e)
			{	
				logger.error(e.toString());
				ResponseMsg msg = new ResponseMsg();
				msg.setOk(false);
				msg.setMsg(e.toString());
				queryResult.getMessage().setStatusCode(Message.StatusCode_ServerError);
				queryResult.getMessage().setMsg("异常");
				new EmailHelper().sendEmailToAdminInThread("WAE异常", e.getErrInfo().getErrMsg());
			}
			catch(Throwable e)
			{	
				logger.error(e.toString());
				ResponseMsg msg = new ResponseMsg();
				msg.setOk(false);
				msg.setMsg(e.toString());
				queryResult.getMessage().setStatusCode(Message.StatusCode_ServerError);
				queryResult.getMessage().setMsg("异常");
				new EmailHelper().sendEmailToAdminInThread("异常", e.toString());
			}
			finally{
				JsonConfig jsonConfig = new JsonConfig();
				
				jsonConfig.setIgnoreDefaultExcludes(false);//DEFAULT_EXCLUDES    "class", "declaringClass",  "metaClass" 
			    jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT); //去掉自包含
			    
			    jsonConfig.registerJsonBeanProcessor(Date.class,  
			            new JsDateJsonBeanProcessor()); // 当输出时间格式时，采用和JS兼容的格式输出  
			    
			    if(resp!=null){
			    	Object o  =  JSONSerializer.toJSON(resp, jsonConfig);
				    if(o instanceof JSONObject){
				    
				    	JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(resp, jsonConfig);
				    	 responseJson(jsonObject.toString());
				    }else{
				    	JSONArray jsonObject = (JSONArray) JSONSerializer.toJSON(resp, jsonConfig);
				    	 responseJson(jsonObject.toString());
				    }
			    }
			    else{
			    	Object o  =  JSONSerializer.toJSON(queryResult, jsonConfig);
				    if(o instanceof JSONObject){
				    
				    	JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(queryResult, jsonConfig);
				    	 responseJson(jsonObject.toString());
				    }else{
				    	JSONArray jsonObject = (JSONArray) JSONSerializer.toJSON(queryResult, jsonConfig);
				    	 responseJson(jsonObject.toString());
				    }
			    }
			    
			   
			    
			}
			
			
		}
		
		
		/***
		 * 可以设置序列化字段
		 * @param pExcludeProperties
		 * @param pIncludeProperties
		 */
		public  void run(String pExcludeProperties,String pIncludeProperties)
		{
			try{
				
				resp=execute();
				
			}
			catch(InfoException infoEx){
				//没有特殊处理
				ResponseMsg msg = new ResponseMsg();
				msg.setOk(false);
				msg.setMsg(infoEx.getErrorInfo().getErrDesc());
			}
			catch(WaeRuntimeException e)
			{	
				logger.error(e.toString());
				ResponseMsg msg = new ResponseMsg();
				msg.setOk(false);
				msg.setMsg(e.toString());
				new EmailHelper().sendEmailToAdminInThread("WAE异常", e.getErrInfo().getErrMsg());
			}
			catch(Throwable e)
			{	
				e.printStackTrace();
				ResponseMsg msg = new ResponseMsg();
				msg.setOk(false);
				msg.setMsg(e.toString());
				new EmailHelper().sendEmailToAdminInThread("异常", e.toString());
			}
			finally{

				
				String[] excludeProperties=StringUtils.split(pExcludeProperties, ",");
				
				JsonConfig jsonConfig = new JsonConfig();
				
				jsonConfig.setExcludes(excludeProperties);  //过滤掉不需要的属性
				jsonConfig.setIgnoreDefaultExcludes(false);//DEFAULT_EXCLUDES    "class", "declaringClass",  "metaClass" 
			    jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT); //去掉自包含
	//		    jsonConfig.setIgnoreJPATransient(true);
			//    jsonConfig.setIgnoreTransientFields(true);
			    jsonConfig.registerJsonBeanProcessor(Date.class,  
			            new JsDateJsonBeanProcessor()); // 当输出时间格式时，采用和JS兼容的格式输出  
			    
			    Object o  =  JSONSerializer.toJSON(resp, jsonConfig);
			    if(o instanceof JSONObject){
			    
			    	JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(resp, jsonConfig);
			    	 responseJson(jsonObject.toString());
			    }else{
			    	JSONArray jsonObject = (JSONArray) JSONSerializer.toJSON(resp, jsonConfig);
			    	 responseJson(jsonObject.toString());
			    }
			    
			}
		}
		
		
		
		
		public  void responseJsonString(String pExcludeProperties,String pIncludeProperties)
		{
			try{
				
				resp=execute();
				
				
			}
			catch(InfoException infoEx){
				//没有特殊处理
				ResponseMsg msg = new ResponseMsg();
				msg.setOk(false);
				msg.setMsg(infoEx.getErrorInfo().getErrDesc());
			}
			catch(WaeRuntimeException e)
			{	
				logger.error(e.toString());
				ResponseMsg msg = new ResponseMsg();
				msg.setOk(false);
				msg.setMsg(e.toString());
				new EmailHelper().sendEmailToAdminInThread("WAE异常", e.getErrInfo().getErrMsg());
			}
			catch(Throwable e)
			{	
				e.printStackTrace();
				logger.error(e.toString());
				ResponseMsg msg = new ResponseMsg();
				msg.setOk(false);
				msg.setMsg(e.toString());
				new EmailHelper().sendEmailToAdminInThread("异常", e.toString());
			}
			finally{
				JsonConfig jsonConfig = new JsonConfig();
				
				jsonConfig.setIgnoreDefaultExcludes(false);//DEFAULT_EXCLUDES    "class", "declaringClass",  "metaClass" 
			    jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT); //去掉自包含
			    
			    jsonConfig.registerJsonBeanProcessor(Date.class,  
			            new JsDateJsonBeanProcessor()); // 当输出时间格式时，采用和JS兼容的格式输出  
			    
			    
			    Object o  =  JSONSerializer.toJSON(resp, jsonConfig);
			    if(o instanceof JSONObject){
			    
			    	JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(resp, jsonConfig);
			    	 responseJson(jsonObject.toString());
			    }else{
			    	JSONArray jsonObject = (JSONArray) JSONSerializer.toJSON(resp, jsonConfig);
			    	 responseJson(jsonObject.toString());
			    }
			}
		}
		
		protected abstract void assignMsg(String msg);
		protected abstract  T execute()  throws Exception;
	}

	 
	 
}
