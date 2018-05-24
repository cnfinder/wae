package cn.finder.wae.common.http;

public class CommonUtils {

	public static boolean  isEmpty(String value){
		if(value==null || (value.toString().length()==0)){
			return true;
		}
		
		return false;
	}
}
