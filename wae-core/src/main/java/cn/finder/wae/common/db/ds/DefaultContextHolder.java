package cn.finder.wae.common.db.ds;


public class DefaultContextHolder {
 
	   private static final ThreadLocal<Long> contextHolder = 
	            new ThreadLocal<Long>();
	        
	   public static void setDataSourceType(Long dataSourceType) {
	      contextHolder.set(dataSourceType);
	   }

	   public static Long getDataSourceType() {
	      return contextHolder.get();
	   }

	   public static void clearDataSourceType() {
	      contextHolder.remove();
	   }
	   
	   
	}