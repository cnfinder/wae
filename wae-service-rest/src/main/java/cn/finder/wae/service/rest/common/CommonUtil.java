package cn.finder.wae.service.rest.common;

import javax.servlet.http.HttpServletRequest;

import org.restlet.Request;
import org.restlet.engine.adapter.HttpRequest;
import org.restlet.engine.adapter.ServerCall;
import org.restlet.ext.servlet.internal.ServletCall;

public class CommonUtil {

	public static HttpServletRequest getRequest(Request request) {  
	    HttpServletRequest result = null;  
	  
	    if (request instanceof HttpRequest) {  
	        final ServerCall  httpCall = ((HttpRequest) request).getHttpCall();  
	  
	        if (httpCall instanceof ServletCall) {  
	            result = ((ServletCall) httpCall).getRequest();  
	        }  
	    }  
	    return result;  
	}
}
