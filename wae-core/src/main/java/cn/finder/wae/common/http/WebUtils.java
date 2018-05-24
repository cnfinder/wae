package cn.finder.wae.common.http;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


public class WebUtils {
	
	
	 private int timeout = 6000;
	 

     public String buildGetUrl(String url, Map<String, String> parameters)
     {
         if ((parameters != null) && (parameters.size() > 0))
         {
             if (url.contains("?"))
             {
                 url = url + "&" + buildQuery(parameters);
                 return url;
             }
             url = url + "?" + buildQuery(parameters);
         }
         return url;
     }
     
     public static String buildQuery(Map<String, String> parameters)
     {
         StringBuffer builder = new StringBuffer();
         
         boolean flag = false;
         
         Set<Entry<String, String>> setEntrys = parameters.entrySet();
         
         Iterator<Entry<String, String>> itr = setEntrys.iterator();
         
         
         while (itr.hasNext())
         {
             
             Entry<String,String> current = itr.next();
             
             String key = current.getKey();
             String value = current.getValue();

             if (!CommonUtils.isEmpty(key))
             {
                 if (flag)
                 {
                     builder.append("&");
                 }
                 builder.append(key);
                 builder.append("=");

                 
                 try {
					builder.append(URLEncoder.encode(value, "utf-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
                 flag = true;
             }
         }
         return builder.toString();
         
         
     }

   /*    public boolean checkValidationResult(Object sender, X509Certificate certificate, X509Chain chain, SslPolicyErrors errors)
     {
         return true;
     }*/

     public String doGet(String url, Map<String, String> parameters)
     {
         if ((parameters != null) && (parameters.size() > 0))
         {
             if (url.contains("?"))
             {
                 url = url + "&" + buildQuery(parameters);
             }
             else
             {
                 url = url + "?" + buildQuery(parameters);
             }
         }
         String retStr ="";
         try {
        	 retStr = doGet(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
         return retStr;
         
     }

     public String doPost(String url, Map<String, String> parameters)
     {
    	return  doPost(url, parameters,null);
     }


     
     /// <summary>
     /// 发送Post请求
     /// </summary>
     /// <param name="url"></param>
     /// <param name="textParams"></param>
     /// <param name="fileParams"></param>
     /// <returns></returns>
     public String doGet(String url)
     {
    	 String returnStr="{}";
       

    	try {
    		 // 创建默认的httpClient实例.    
    		HttpClient httpclient=new  DefaultHttpClient();
    		
            // 创建httppost    
            HttpGet http = new HttpGet(url);
            
            UrlEncodedFormEntity uefEntity;  
            try {  
                
                HttpResponse response = httpclient.execute(http);  
                try {  
                    HttpEntity entity = response.getEntity();  
                    if (entity != null) {  
                    	returnStr=EntityUtils.toString(entity, "UTF-8");
                    	
                    }  
                } finally {  
                   // response.close();
                }  
            } catch (Exception e) {  
            
            	e.printStackTrace();
            } finally {  
                // 关闭连接,释放资源    
                try {  
                   // httpclient.close();  
                } catch (Exception e) {  
                    e.printStackTrace();  
                }  
            }  
    		
		} catch (Exception e) {
			e.printStackTrace();
		}

    	 return returnStr;

     }
     
     /// <summary>
     /// 发送Post请求
     /// </summary>
     /// <param name="url"></param>
     /// <param name="textParams"></param>
     /// <param name="fileParams"></param>
     /// <returns></returns>
     public String doPost(String url, Map<String, String> textParams, Map<String, FileItem> fileParams)
     {
    	 String returnStr="{}";
    	/* FormFile[] files=null;
    	 if(fileParams!=null && fileParams.size()>0){
    		 
    		 
    		 //转换文件
    		 files = new FormFile[fileParams.size()];
    		 
    		 Set<Entry<String,FileItem>> setEntry = fileParams.entrySet();
    		 
    		 Iterator<Entry<String,FileItem>> itr =  setEntry.iterator();
    		 int fileCnt=0;
    		 while(itr.hasNext()){
    			 fileCnt++;
    			 Entry<String,FileItem> entry  = itr.next();
    			 
    			 String key = entry.getKey();
    			 FileItem fileItem = entry.getValue();
    			 
    			 
    			 FormFile formFile=new FormFile(fileItem.getContent(), key, "myForm", fileItem.getMimeType());
    			 
    			 
    			 files[fileCnt-1]= formFile; 
    		 }
    		 
    		 
    	 }*/
       

    	try {
    		
    		
    		
    		
    		
    		
    		
    		
    		 // 创建默认的httpClient实例.    
    		
    		HttpClient httpclient=new  DefaultHttpClient();
    		
    		
    		/*
    		HttpGet get =new HttpGet("http://www.baidu.com");
    	    HttpResponse res =	httpclient.execute(get);
    	    HttpEntity entity1 = res.getEntity();  
            if (entity1 != null) {  
            	returnStr=EntityUtils.toString(entity1, "UTF-8");
            	
            }  
            */
            
    		
            // 创建httppost    
            HttpPost httppost = new HttpPost(url);
            
         // 创建参数队列    
            List<NameValuePair> formparams = new ArrayList<NameValuePair>(); 
            
            if(textParams!=null && textParams.size()>0){
            	Set<Entry<String, String>> entrySets= textParams.entrySet();
            	Iterator<Entry<String, String>> itr = entrySets.iterator();
            	
            	while(itr.hasNext()){
            		Entry<String,String> entry = itr.next();
                    BasicNameValuePair vp=new BasicNameValuePair(entry.getKey(), entry.getValue());
            		formparams.add(vp);
            	}
            	
            }
            UrlEncodedFormEntity uefEntity;  
            try {  
                uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");  
                httppost.setEntity(uefEntity);  
                
                HttpResponse response = httpclient.execute(httppost);  
                try {  
                    HttpEntity entity = response.getEntity();  
                    if (entity != null) {  
                    	returnStr=EntityUtils.toString(entity, "UTF-8");
                    	
                    }  
                } finally {  
                   // response.close();
                }  
            } catch (Exception e) {  
            
            	e.printStackTrace();
            } finally {  
                // 关闭连接,释放资源    
                try {  
                   // httpclient.close();  
                } catch (Exception e) {  
                    e.printStackTrace();  
                }  
            }  
    		
		} catch (Exception e) {
			e.printStackTrace();
		}

    	 return returnStr;

     }


	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	

    
}
