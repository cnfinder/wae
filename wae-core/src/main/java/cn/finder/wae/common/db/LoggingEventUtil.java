package cn.finder.wae.common.db;

import org.apache.log4j.Category;
import org.apache.log4j.Priority;
import org.apache.log4j.spi.LoggingEvent;

public class LoggingEventUtil extends LoggingEvent {

	private static final long serialVersionUID = 3359112146209080210L;  
	 
    public LoggingEventUtil(String fqnOfCategoryClass, Category logger,  
            Priority level, Object message, Throwable throwable) {  
        super(fqnOfCategoryClass, logger, level, message, throwable);  
    }  
 
    @Override 
    public String getThreadName() {  
        String thrdName = super.getThreadName();  
        if (thrdName.indexOf("'") != -1) {  
            thrdName = thrdName.replaceAll("'", "''");  
        }  
        return thrdName;  
    }  
 
    /**  
     *   
     *   
     * @see org.apache.log4j.spi.LoggingEvent#getRenderedMessage()  
     */ 
    @Override 
    public String getRenderedMessage() {  
        String renderedMessage = super.getRenderedMessage();  
 
        if (renderedMessage != null && renderedMessage.indexOf("'") != -1)  
            renderedMessage = renderedMessage.replaceAll("'", "''");  
        return renderedMessage;  
    }  
    
    
    
}
