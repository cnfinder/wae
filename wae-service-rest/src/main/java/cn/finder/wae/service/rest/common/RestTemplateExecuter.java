package cn.finder.wae.service.rest.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.json.JSONUtil;
import org.restlet.data.Status;

import cn.finder.wae.business.domain.Response;
import cn.finder.wae.common.exception.WaeRuntimeException;


/***
 *  执行模板,这样在Resource层就不需要单独处理异常
 * @author 吴华龙
 *
 * @param <T> 返回的统一类型
 */
public abstract class RestTemplateExecuter<T> {

	private final static Logger logger=Logger.getLogger(RestTemplateExecuter.class);
	
	public Response<T> resp=null;
	
	@SuppressWarnings("finally")
	public  Response<T> run()
	{
		resp=new Response<T>();
		try{
			resp=execute();
		}
		catch(WaeRuntimeException e)
		{
			resp.getMessage().setStatusCode(Status.SERVER_ERROR_INTERNAL.getCode());
			resp.getMessage().setMsg(e.getMessage());
			resp.getMessage().setDetail("");
			logger.error(e);
		}
		catch(Throwable e)
		{
			resp.getMessage().setStatusCode(Status.SERVER_ERROR_INTERNAL.getCode());
			resp.getMessage().setMsg(e.getMessage());
			resp.getMessage().setDetail("");
			logger.error(e);
		}
		finally{
			return resp;
		}
	}
	
	/**
	 * 
	 * 
	 * 返回JSON字符串 ，主要是为了处理 返回特定的字段数据
	 * @param pExcludeProperties
	 * @param pIncludeProperties
	 * @return
	 */
	
	@SuppressWarnings("finally")
	public  String responseJsonString(String pExcludeProperties,String pIncludeProperties)
	{
		String jsonString=null;
		resp=new Response<T>();
		try{
				
			resp=execute();
			
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
			
			jsonString=JSONUtil.serialize(resp, excludePropertiesPatterns, includePropertiesPatterns, false, false);
			
		}
		catch(RuntimeException e)
		{
			e.printStackTrace();
			resp.getMessage().setStatusCode(Status.SERVER_ERROR_INTERNAL.getCode());
			resp.getMessage().setMsg(e.getMessage());
			//resp.getMessage().setDetail(e.toString());
			//logger.error(e.);
			logger.error(e);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			resp.getMessage().setStatusCode(Status.SERVER_ERROR_INTERNAL.getCode());
			resp.getMessage().setMsg(e.getMessage());
			//resp.getMessage().setDetail(e.toString());
			//logger.error(e.);
			logger.error(e);
		}
		finally{
			return jsonString;
		}
		
		
	}
	
	
	protected abstract  Response<T> execute()  throws Exception;
}
