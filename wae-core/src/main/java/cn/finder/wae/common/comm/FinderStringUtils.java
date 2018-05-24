package cn.finder.wae.common.comm;

import java.util.ArrayList;
import java.util.List;

public class FinderStringUtils {
	/**
	 * 替换参数值
	 * 如:
	 * "http://uland.taobao.com/coupon/edetail?activityId=eb77c0ea3cfc46d0888959c7e6295eed&pid=mm_123635337_25066762_85948572&itemId=550963599514&src=hpt_hptzs&dx=",
				"pid",
				"mm_17414766_18284280_65102598"
	 * @param parameter
	 * @param name
	 * @param value
	 * 
	 */

	public static String replaceUrlParamValue(String url,String name,String value){
		
		if(url.indexOf("?")<0){
			//没有任何参数
			return url;
		}
		else{
			StringBuffer sb=new StringBuffer();
			String http_prefix=url.substring(0,url.indexOf("?"));
			sb.append(http_prefix+"?");
			
			String kv=url.substring(url.indexOf("?")+1);
		
			String[] kv_arr=kv.split("&");
			
			for(int i=0;i<kv_arr.length;i++){
				String[] para_kv=kv_arr[i].split("=");
				if(i!=0){
					sb.append("&");
				}
				sb.append(para_kv[0]);
				sb.append("=");
				if(name.equalsIgnoreCase(para_kv[0])){
					sb.append(value);
				}else{
					try{
						sb.append(para_kv[1]); //可能参数是这样  k=
					}
					catch(Exception e){
						
					}
				}
			}
			return sb.toString();
		}
	}
	
	
	/***
	 * 解析URL 参数值
	 * @param url
	 * @param name
	 * @return
	 */
	public static List<String> findUrlParamValue(String url,String name){
		
		List<String> values=new ArrayList<String>();
		if(url.indexOf("?")<0){
			//没有任何参数
			
		}
		else{
			
			String kv=url.substring(url.indexOf("?")+1);
		
			String[] kv_arr=kv.split("&");
			
			for(int i=0;i<kv_arr.length;i++){
				String[] para_kv=kv_arr[i].split("=");
				
				if(name.equalsIgnoreCase(para_kv[0])){
					if(para_kv.length==2){
						values.add(para_kv[1]);
					}else{
						values.add("");
					}
					
				}else{
				}
			}
		}
		
		return values;
	}
	
	
	public static String findUrlParamValueSingle(String url,String name){
		List<String> values=findUrlParamValue(url,name);
		if(values.size()>0){
			return values.get(0);
		}
		else{
			return null;
		}
	}
}
