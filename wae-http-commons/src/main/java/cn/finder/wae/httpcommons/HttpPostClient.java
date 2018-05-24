package cn.finder.wae.httpcommons;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.finder.httpcommons.ApiDictionary;
import cn.finder.httpcommons.domain.Message;
import cn.finder.httpcommons.parser.IParser;
import cn.finder.httpcommons.parser.JsonParser;
import cn.finder.httpcommons.parser.XmlParser;
import cn.finder.httpcommons.request.BaseRequest;
import cn.finder.httpcommons.request.IUploadRequest;
import cn.finder.httpcommons.request.StreamRequest;
import cn.finder.httpcommons.response.ApiResponse;
import cn.finder.httpcommons.utils.ApiUtils;
import cn.finder.httpcommons.utils.FileItem;
import cn.finder.httpcommons.utils.JsonUtils;

public class HttpPostClient extends cn.finder.httpcommons.HttpPostClient {

	public final String APP_KEY = "app_key";
	
	
	
	 public final String PARTNER_ID = "partner_id";
	 public final String SDK_VERSION = "api-sdk-java-v1.0";
	 public final String METHOD = "method";
	 public final String VERSION = "v";
	 
	 private String appKey;
	 private String appSecret;
	 private String version="1.0"; //接口的版本
	 
	public HttpPostClient(String url,String appKey, String appSecret) {
		super(url);
		this.appKey = appKey;
		this.appSecret=appSecret;
		
	}

	
	public HttpPostClient(String serverUrl, String format,String appKey, String appSecret)
   {
		super(serverUrl,format);
		this.appKey = appKey;
	   	this.appSecret=appSecret;
   }
   public HttpPostClient(String serverUrl, String format,boolean disableParser,String appKey, String appSecret)
   {
	   	super(serverUrl,format,disableParser);
	   	this.appKey = appKey;
	   	this.appSecret=appSecret;
   }
   
   
   public  <T extends ApiResponse>  T doExecute(BaseRequest<T> request, String session, Date timestamp)
   {
	   T resp;
       try
       {
           //请求参数验证
           request.validate();
       }
       catch (Exception exception)
       {
           return this.createErrorResponse(Message.StatusCode_ClientError+"", exception.toString());
       }
       Map<String,String> reqParams = request.parameters();
       ApiDictionary parameters = new ApiDictionary((HashMap<String,String>)reqParams);

       parameters.add(METHOD, request.apiName());
       
       parameters.add(VERSION,version);
       parameters.add(APP_KEY, this.appKey);
       parameters.add(FORMAT, this.format);
       parameters.add(PARTNER_ID, SDK_VERSION);
       parameters.add(TIMESTAMP, timestamp);

       parameters.add(SESSION, session==null?"":session);

       parameters.addAll(this.systemParameters);
       
       String strGetUrl = this.webUtils.buildGetUrl(this.url, parameters);

       String dataJsonstr = JsonUtils.getJsonString4JavaPOJO(reqParams);
       parameters.add("data", dataJsonstr);
       try
       {
           T respTmp = null;
           String retStr;

           if (request instanceof IUploadRequest<?>)
           {//  二进制(文件、图片)请求
               Map<String, FileItem> fileParams = ApiUtils.CleanupDictionary(((IUploadRequest<T>)request).fileParameters());
               if (fileParams != null && fileParams.size() > 0)
               {

               }

              // retStr = this.webUtils.doPostMultipart(this.url, parameters, fileParams);===>报400错误
               
               retStr = this.webUtils.post(this.url, parameters,null,null, new FileItem[]{fileParams.get("binary_data")});
           }
           else if (request instanceof StreamRequest<?>)
           {
               retStr = this.webUtils.buildGetUrl(this.url, parameters);
               this.disableParser=true;//流类型 默认的为true;
           }
           else
           {
               //发送普通文本请求
               retStr = this.webUtils.doPost(this.url, parameters);
              // retStr = "{message:{statusCode:404,msg:\"调用失败，查看服务器是否开启或者网络是否连接\",detail:null},pageIndex:1,pageSize:10,totalRecord:0,pageCount:0,entities:[]}";
           }
           if (this.disableParser)
           {
           	
           	Object c = request.responseClassType().newInstance();
           	respTmp=(T)c;
           	
               respTmp.setBody(retStr);
           }
           else if (FORMAT_XML.equals(this.format))
           {
               IParser<T> parser = new XmlParser<T>();
               respTmp =  parser.parse(retStr,request.responseClassType());
           }
           else
           {
               IParser<T> parser = new JsonParser<T>();
               
              
               respTmp =  parser.parse(retStr,request.responseClassType());
           }
           if (!this.disableTrace && respTmp.getMessage().getStatusCode() != Message.StatusCode_OK)
           {
               this.apiLogger.warn(new StringBuffer(strGetUrl).append(" response error!\r\n").append(respTmp.getBody()).toString());
           }
           resp = respTmp;
       }
       catch (Exception exp)
       {
           if (!this.disableTrace)
           {
               this.apiLogger.error(new StringBuffer(strGetUrl).append(" request error!\r\n").append(exp.toString()).toString());
           }
           // throw exp;

           String respStrNotFound = "{message:{statusCode:404,msg:\"调用失败，查看服务器是否开启或者网络是否连接\",detail:null},pageIndex:1,pageSize:10,totalRecord:0,pageCount:0,entities:[]}";
           IParser<T> parser = new JsonParser<T>();
           resp = parser.parse(respStrNotFound,request.responseClassType());
           
       }
       //if(OnResponse!=null)
       //    OnResponse(resp);
      // finally
      // {
           return resp;
     //  }
   }
}
