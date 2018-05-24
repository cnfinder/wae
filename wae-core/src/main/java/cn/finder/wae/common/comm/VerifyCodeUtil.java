package cn.finder.wae.common.comm;

import java.util.UUID;

public class VerifyCodeUtil {
	public static String getVerifyCode(Integer id){
		UUID uuid = UUID.randomUUID();
		String code = uuid.toString()+"-"+id;
		return code;
	}
	public static String getVerifyCode(long id){
		UUID uuid = UUID.randomUUID();
		String code = uuid.toString()+"-"+id;
		return code;
	}
	public static String getVerifyCode(String value)
	{
		UUID uuid = UUID.randomUUID();
		String code = uuid.toString()+"-"+value;
		return code;
	}
	
	
	public static String getUUID(String code){
		if(code==null || "".equals(code)){
			return null;
		}
		if(code.indexOf("-")==-1)
		{
			return null;
		}
		int index = code.lastIndexOf("-");
		String uuid = code.substring(0,index);
		return uuid;
	}
	
	public static String getId(String code){
		if(code==null){
			return null;
		}
		int index = code.lastIndexOf("-");
		String id = code.substring(index+1);
		return id;
	}
	
	public static void main(String[] args){
		String code = getVerifyCode(10);
		System.out.println(code);
		System.out.println(getUUID(code));
		System.out.println(getId(code));
	}
}
