package cn.finder.wae.common.constant;

/**
 * 消息键，对应资源文件里面的键
 * 
 * @author wu hualong
 *
 */
public class MsgContant {

	//这些键被MsgInfo中errorId使用
	private final static String PREFIX_ERR="err.";
	
	//提示消息
	private final static String PREFIX_INFO="info.";
	
	
	//系统异常
	public final static String ERR_SYSTEM=PREFIX_ERR+"system";
	
	
	public final static String INFO_TEST=PREFIX_INFO+"test";
	
	
	
	public final static String ERR_LOGIN=PREFIX_ERR+"login";
	
	public final static String ERR_LOGIN_EMPTY=PREFIX_ERR+"login.empty";
	
	
	public final static String KEY_COMMON_PAGE_MSG="page_msg";
}
