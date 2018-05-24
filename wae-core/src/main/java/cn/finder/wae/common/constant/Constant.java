package cn.finder.wae.common.constant;

/***
 * 常量配置规则
 * @author wu hualong
 *
 */
@SuppressWarnings("all")
public class Constant {

	//request 类型数据保存键前缀
	private final static String PREFIX_REQUEST_SCOPE="r_";
	
	
	//session 级别数据保存
	private final static String PREFIX_SESSION_SCOPE="s_";
	
	//application 级别数据保存
	private final static String PREFIX_APPLICATION_SCOPE="a_";
	
	//用户信息保存
	public final static String KEY_SESSION_USER=PREFIX_SESSION_SCOPE+"session_user";
	
	//全局 缓存 键
	public final static String KEY_APPLICATION_CACHE=PREFIX_APPLICATION_SCOPE+"cache";
	
	
	
	
	
	
	
	
	
}
