package cn.finder.wx.pay;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.UUID;

import org.apache.log4j.Logger;

public class WxSign {
	
	 private static Logger  logger = Logger.getLogger(WxSign.class);
	 private static String characterEncoding = "UTF-8";
	  
	  @SuppressWarnings("rawtypes")
	  public static String createSign(SortedMap<Object,Object> parameters,String key){ 
	    StringBuffer sb = new StringBuffer(); 
	    Set es = parameters.entrySet();//所有参与传参的参数按照accsii排序（升序） 
	    Iterator it = es.iterator(); 
	    while(it.hasNext()) { 
	      Map.Entry entry = (Map.Entry)it.next(); 
	      String k = (String)entry.getKey(); 
	      Object v = entry.getValue(); 
	      if(null != v && !"".equals(v)  
	          && !"sign".equals(k) && !"key".equals(k)) { 
	        sb.append(k + "=" + v + "&"); 
	      } 
	    } 
	    sb.append("key=" + key);
	    
	   logger.info("=========WxSign.param:"+sb.toString());
	   
	    
	   // String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase(); 
	    String sign =  EncoderHandler.encodeByMD5(sb.toString()).toUpperCase();
	    return sign; 
	  }
	    
	  public static String getNonceStr() {
	   // Random random = new Random();
	   // return MD5Util.MD5Encode(String.valueOf(random.nextInt(10000)), "UTF-8");
	    
	     return UUID.randomUUID().toString().replace("-", "");
	  }
	  
	  public static String getTimeStamp() {
	    return String.valueOf(System.currentTimeMillis() / 1000);
	  }
}
