package cn.finder.wae.cache;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import cn.finder.wae.business.domain.Constants;
import cn.finder.wae.business.domain.SysConfig;

public class ConstantsCache extends DefaultCacheInterface<Long, Constants>{

	public final static String CACHE_KEY="cache_constants";
	
	private static Cache<String,Long, Constants> cache;
	
	public void setCache(Cache<String,Long, Constants> cache){
		ConstantsCache.cache=cache;
	}
	
	public Cache<String,Long, Constants> getCache(){
		return ConstantsCache.cache;
	}
	
	public Map<Long, Constants> getBaseDatas() {
		return get();
	}
	
	
	/***
	 * 数据类型
	 * @author wuhualong
	 *
	 */
	public static class DataType{
		public final static int DATATYPE_STRING=11;
		public final static int DATATYPE_NUMBER=12;
		public final static int DATATYPE_BOOLEAN=13;
		public final static int DATATYPE_DATE=14;
		public final static int DATATYPE_FLOAT=15;
		public final static int DATATYPE_BINARY=16;
	}
	
	
	public static class TableType{
		public final static int TABLETYPE_TABLE=5;
		public final static int TABLETYPE_VIEW=6;
		public final static int TABLETYPE_PROCEDURE=7;
		public final static int TABLETYPE_FUNCTION=8;
		public final static int TABLETYPE_MONGO_COLLECTION=9;
	}
	
	
	/***
	 * 控件类型
	 * @author wuhualong
	 *
	 */
	public static class ControlType{
		public final static int CONTROLTYPE_TEXT=21;
		public final static int CONTROLTYPE_TEXTBOX=22;
		public final static int CONTROLTYPE_COMBOX=23;
		public final static int CONTROLTYPE_CHECKBOX=24;
		public final static int CONTROLTYPE_DATE=25;
		public final static int CONTROLTYPE_COLORPICKER=26;
		public final static int CONTROLTYPE_COMMAND=27;
		public final static int CONTROLTYPE_RADIO=28;
		public final static int CONTROLTYPE_NUMBER_BETWEEN=29;
		public final static int CONTROLTYPE_PIC_PATH=30;
		public final static int CONTROLTYPE_PWD=31;
		public final static int CONTROLTYPE_SEARCHSUGGESTION=32;
		public final static int CONTROLTYPE_PWD_CONFIRM=33;
		public final static int CONTROLTYPE_FILE=34;         //文件类型
		public final static int CONTROLTYPE_BINARY_FILE=35;  //二进制图片
		public final static int CONTROLTYPE_POPUP_DROPDOWN=36;
		public final static int CONTROLTYPE_MULTI_TEXTBOX=37;
		public final static int CONTROLTYPE_SPINNER=38;
		
		public final static int CONTROLTYPE_LISTBOX=39;
		public final static int CONTROLTYPE_POPUP_TABLESELECT=40;
		
		public final static int CONOTROLTYPE_SHOW_SELECT = 46; //显示关联用下拉框
		
		public final static int CONTROLTYPE_POPUP_TREESELECT=400;
		public final static int CONTROLTYPE_CASCADE_COMBOX=401;
		public final static int CONTROLTYPE_RICH_TEXTBOX=402;
		public final static int CONTROLTYPE_HIDDEN=403;
		
		public final static int CONTROLTYPE_SYSTEM_DATE=404;     //当前服务器日期类型
		
		public final static int CONTROLTYPE_LOGIN_USER=405;     //当前用户账户（推荐） 或者 用户ID 
		
		public final static int CONTROLTYPE_POSITION_FLAG=406;     //占位符控件
		
		public final static int CONTROLTYPE_RETURN_PRIMARY_KEY = 407; //返回主键
		
		public final static int CONTROLTYPE_COMBOX_SINGLE_TREE_SELECT = 408; //返回主键
		public final static int CONTROLTYPE_COMBOX_MUTLI_TREE_SELECT = 409; //返回主键
		
		public final static int CONTROLTYPE_FILE_NAMEED_BY_ID = 410; //根据主键命名的文件类型
		public final static int CONTROLTYPE_FILE_Suffix = 411; //文件扩展名类型
		
		public final static int CONTROLTYPE_FIELD_MATCH = 414; //字段匹配
		
		public final static int CONTROLTYPE_IN_OPERATION = 415; //in 操作符控件
		
		public final static int CONTROLTYPE_NOTIN_OPERATION = 416; //not in 操作符控件
		
		public final static int CONTROLTYPE_NOTEQUAL_OPERATION = 417; // 不等于 操作符控件
		
		public final static int CONTROLTYPE_EQUAL_OPERATION = 418; // 不等于 操作符控件
		
		
		public final static int CONTROLTYPE_OR_OPERATION = 420; // 或者  操作符控件
		
		public final static int CONTROLTYPE_MORE_CHECK = 425; // 多选下拉框
		
		
		// 传入的数据 等于号 进行编码     传出的数据 进行 完全解码
		public final static int CONTROLTYPE_PROPERTY_DATA_ENCODING_DECODING_MULTI_TEXTBOX = 421; // 属性 key1=value1 文本框编码
		
		public final static int CONTROLTYPE_GREATTHEN_OPERATION = 423; // 大于等于  操作符控件
		
		public final static int CONTROLTYPE_LESSTHAN_OPERATION = 424; // 小于等于 操作符控件
		
		public final static int CONTROLTYPE_ONEDATE = 2006; // 小于等于 操作符控件
		
		public final static int[] CONTROL_LOAD_COMBOX_DATA={ConstantsCache.ControlType.CONTROLTYPE_COMBOX,
		                                                  ConstantsCache.ControlType.CONTROLTYPE_CASCADE_COMBOX,
		                                                  ConstantsCache.ControlType.CONTROLTYPE_COMBOX_SINGLE_TREE_SELECT,
		                                                  ConstantsCache.ControlType.CONTROLTYPE_COMBOX_MUTLI_TREE_SELECT};
	}
	
	
	
	/**
	 * 判断控件是否加载所有父项数据
	 * @param controlType
	 * @return
	 */
	public static boolean  isLoadComboxData(int controlType){
		
		String value = ArchCache.getInstance().getSysConfigCache().get(SysConfigCache.KEY_SYSCONFIG_CONFIG_CONTROL_TYPE_COMBOX_LOAD_DATA).getValue();
		
		if(StringUtils.isEmpty(value)){
			return false;
		}
		else{
			
			String[] controlTypesString = value.split(",");
			
			for(int i=0;i<controlTypesString.length;i++){
				
				int int_controlType = Integer.valueOf(controlTypesString[i]);
				
				if(int_controlType==controlType){
					return true;
				}
			}
			return false;
			
			
		}
		
		
	}



	public boolean add(Long key, Constants value) {
		return cache.add(CACHE_KEY, key,value);
	}

	public  boolean add(HashMap<Long,Constants> value){
		return cache.add(CACHE_KEY, value);
	}


	public boolean remove(Long key) {
		// TODO Auto-generated method stub
		return cache.remove(CACHE_KEY,key);
	}



	public boolean replace(Long key, Constants newValue) {
		// TODO Auto-generated method stub
		return cache.replace(CACHE_KEY,key, newValue);
	}



	public boolean contains(Long key) {
		// TODO Auto-generated method stub
		return cache.contains(CACHE_KEY,key);
	}




	public Constants get(Long key) {
		// TODO Auto-generated method stub
		return cache.get(CACHE_KEY,key);
	}


	public HashMap<Long,Constants> get(){
		return cache.get(CACHE_KEY);
	}


	public boolean clear() {
		// TODO Auto-generated method stub
		return cache.clear(CACHE_KEY);
	}
	
	
	
	
	
	
	
	
	
}
