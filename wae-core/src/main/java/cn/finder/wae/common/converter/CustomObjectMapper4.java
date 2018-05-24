package cn.finder.wae.common.converter;

import java.text.SimpleDateFormat;

import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * @author: finder 
 * for spring 4.x
 * @data:2014-6-12下午1:30:59
 * @function:解决Date类型返回json格式为自定义格式
 */
public class CustomObjectMapper4 extends ObjectMapper {  
  
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CustomObjectMapper4(){  
		super();
		/*this.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
	    this.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
	    this.setSerializationInclusion(Include.NON_NULL);*/
		this.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"));
		
    }  
}  
