package cn.finder.wae.common.comm;

import java.util.UUID;

public class UUIDUtils {

	/***
	 * 默认 - 为连接符
	 * @return
	 */
	public static String defaultRandomUUID(){
		return UUID.randomUUID().toString();
	}
	
	
	/***
	 * 指定连接符
	 * @param split
	 * @return
	 */
	public static String randomUUID(String split){
		return UUID.randomUUID().toString().replace("-", split);
	}
	
	
	/***
	 *  无连接符
	 * @return
	 */
	public static String randomUUID(){
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	
	
	public static void main(String[] args){
		
	}
	
}
