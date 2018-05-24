package cn.finder.wae.wx.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;


public class TulingApiProcessUtils {
	
	private Logger logger = Logger.getLogger(TulingApiProcessUtils.class);
	/** 
     * 调用图灵机器人api接口，获取智能回复内容，解析获取自己所需结果 
     * @param content 
     * @return 
     */  
    public String getTulingResult(String content){  
    	
    	logger.info("===content:"+content);
    	if(StringUtils.isEmpty(content)){
    		content="没有输入";
    	}
        /** 此处为图灵api接口，参数key需要自己去注册申请，先以11111111代替 */  
        String apiUrl = "http://www.tuling123.com/openapi/api?key=e97c98fb4147b79746a3664b3257d9b0&info=";  
        String param = "";  
        try {  
            param = apiUrl+URLEncoder.encode(content,"utf-8");  
        } catch (UnsupportedEncodingException e1) {  
            // TODO Auto-generated catch block  
            e1.printStackTrace();  
        } //将参数转为url编码  
          
        /** 发送httpget请求 */  
        HttpGet request = new HttpGet(param);  
        String result = "";  
        try {  
        	HttpClient httpclient=new  DefaultHttpClient();
            HttpResponse response = httpclient.execute(request); 
            if(response.getStatusLine().getStatusCode()==200){  
                result = EntityUtils.toString(response.getEntity());  
            }  
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        /** 请求失败处理 */  
        if(null==result){  
            return "对不起，你说的话真是太高深了……";  
        }  
          
        try {  
            JSONObject json = new JSONObject(result);  
            //以code=100000为例，参考图灵机器人api文档  
            if(100000==json.getInt("code")){  
                result = json.getString("text");  
            }
            else{
            	result = json.getString("text");
            }
        } catch (JSONException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
        return result;  
    }  
}
