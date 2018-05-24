package cn.finder.wae.common.comm;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import cn.finder.wae.common.type.FinderMap;



public class Common {
	public final static String DATE_FORMAT_FULL="yyyy-MM-dd HH:mm:ss.S";
	public final static String DATE_FORMAT="yyyy-MM-dd HH:mm:ss";
	public final static String DATE_FORMAT2="yyyy-MM-dd HH";
	public final static String DATE_FORMAT3="yyyy-MM-dd'T'HH:mm:ss";
	public final static String DATE_FORMAT4="yyyy-MM-dd";
	
	public static Date parseDate(String dateStr,String format)
	{
		SimpleDateFormat sdf=new SimpleDateFormat(format);
		if(StringUtils.isEmpty(dateStr))
			return null;
		try {
			
			return sdf.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			
			return parseDate2(dateStr);
		}
	}
	
	

	public static Date parseDateFull(String dateStr)
	{
		SimpleDateFormat sdf=new SimpleDateFormat(DATE_FORMAT_FULL);
		if(StringUtils.isEmpty(dateStr))
			return null;
		try {
			
			return sdf.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			try
			{
				return parseDate(dateStr, DATE_FORMAT);
			}
			catch(Exception exp)
			{
				return parseDate2(dateStr);
			}
		}
	}
	
	public static Date parseDate(String dateStr)
	{
		SimpleDateFormat sdf=new SimpleDateFormat(DATE_FORMAT);
		if(StringUtils.isEmpty(dateStr))
			return null;
		try {
			
			return sdf.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			
			return parseDate2(dateStr);
		}
	}
	
	private static Date parseDate2(String dateStr)
	{
		SimpleDateFormat sdf=new SimpleDateFormat(DATE_FORMAT2);
		if(StringUtils.isEmpty(dateStr))
			return null;
		try {
			
			return sdf.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			return parseDate3(dateStr);
		}
	}
	
	
	public static Date parseDate3(String dateStr)
	{
		SimpleDateFormat sdf=new SimpleDateFormat(DATE_FORMAT3);
		if(StringUtils.isEmpty(dateStr))
			return null;
		try {
			
			return sdf.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}
	public static Date parseDate4(String dateStr)
	{
		SimpleDateFormat sdf=new SimpleDateFormat(DATE_FORMAT4);
		if(StringUtils.isEmpty(dateStr))
			return null;
		try {
			
			return sdf.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}
	
	public static String formatDate(Date date,String format)
	{
		SimpleDateFormat sdf=new SimpleDateFormat(format);
		if(date==null)
		{
			return null;
		}
		try
		{
			return sdf.format(date);
		}
		catch(Exception e)
		{
			return null;
		}
		
		
	}
	
	public static String formatDate(Date date)
	{
		SimpleDateFormat sdf=new SimpleDateFormat(DATE_FORMAT);
		if(date==null)
		{
			return null;
		}
		try
		{
			return sdf.format(date);
		}
		catch(Exception e)
		{
			//e.printStackTrace();
			return formatDate2(date);
		}
		
		
	}
	
	
	public static String formatDateFull(Date date)
	{
		SimpleDateFormat sdf=new SimpleDateFormat(DATE_FORMAT_FULL);
		if(date==null)
		{
			return null;
		}
		try
		{
			return sdf.format(date);
		}
		catch(Exception e)
		{
			//e.printStackTrace();
			return formatDate2(date);
		}
		
		
	}
	
	
	private static String formatDate2(Date date)
	{
		SimpleDateFormat sdf=new SimpleDateFormat(DATE_FORMAT2);
		if(date==null)
		{
			return null;
		}
		try
		{
			return sdf.format(date);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
		
	}
	
	
	/**
	 * use window cmd to copy
	 * @param sourceFullPath  full file path (include path & filename)
	 * @param destPath dest path ,not include filename
	 * @param fileName  dest filename
	 * @throws WaeException 
	 */
	public static void fileCopy(String sourceFullPath,String destPath,String fileName) throws RuntimeException
	{
		Runtime r=Runtime.getRuntime();
		try {
			sourceFullPath=sourceFullPath.replaceAll("\\/", "\\\\");
			String destFullPath=destPath+File.separator+fileName;
			
			r.exec("cmd.exe /C copy "+"\""+sourceFullPath+"\""+" "+"\""+destFullPath+"\"");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public static void deleteFile(String absolutePath)
	{
		java.io.File f=new java.io.File(absolutePath);
		if(f.exists())
		{
			f.delete();
		}
	}
	
	 /**
     * @param String email
     * @return boolean
     */
    public static boolean checkIsEmail(String str){
    	Pattern emailPattern = Pattern.compile("^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$");
    	Matcher m=emailPattern.matcher(str);
    	return m.matches();
    }
    
    public static String decoderString(String value)
    {
    	try {
			return new String(value.getBytes("ISO-8859-1"),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }
    
    public static String decoderString(String value,String fromEncodeType,String toEncodeType)
    {
    	try {
			return new String(value.getBytes(fromEncodeType),toEncodeType);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
    }
    
    
    /***
     * Map类型的数据 FinderMap
     * @author: wuhualong
     * @data:2014-8-22上午8:31:47
     * @function:
     * @param map
     * @return
     */
    public static FinderMap<String, Object> mapToFinderMap(Map<String,Object> map)
    {
    	FinderMap<String, Object> finderMap =new FinderMap<String, Object>();
    	if(map!=null){
    		Set<Entry<String, Object>> setEntry = map.entrySet();
    		Iterator<Entry<String, Object>> itr= setEntry.iterator();
    		while(itr.hasNext()){
    			
    			Entry<String, Object> entry = itr.next();
    			
    			finderMap.put(entry.getKey(), entry.getValue());
    		}
    		
    		
    		
    	}
    	return finderMap;
    }
    
    
    /***
     * 转换到字符串
     * @param value
     * @return
     */
    public static String convertToString(Object value){
    	if(value==null){
    		return "";
    	}
    	
    	return value.toString();
    }
    
    /***
     * 转换到整型
     * @param value
     * @return
     */
    public static int convertToInt(Object value){
    	if(value==null){
    		return 0;
    	}
    	
    	return Integer.parseInt(value.toString());
    }
    
    /***
     * 转换到单精度
     * @param value
     * @return
     */
    public static float convertToFloat(Object value){
    	if(value==null){
    		return 0.0f;
    	}
    	
    	return Float.parseFloat(value.toString());
    }
    
    /***
     * 转换到双精度
     * @param value
     * @return
     */
    public static double convertToDouble(Object value){
    	if(value==null){
    		return 0.0f;
    	}
    	
    	return Double.parseDouble(value.toString());
    }
    
    
    /***
     * 获取  地址 字符串等 后缀
     * @return
     */
    public static String getStrSuffix(String path){
    	String suffix=path.substring(path.lastIndexOf(".")+1);
    	
    	return suffix;
    }
    
    
    /****
     * 替换 MAP里面的值
     * @param data 
     * @param key
     * @param value
     */
    public static void replaceValue(Map<String,Object> data,String key,Object value){
    	if(data.containsKey(key)){
    		data.remove(key);
    		
    		
    	}
    	data.put(key,value);
    }
    
    /***
     * 替换花括号值为真是值
     * @param value 花括号值将被替换 http://{ip}:{port}/iv/page/rreprot.jsp
     * @param variables 实际变量值
     * @return
     */
    public static String descWrapper(String value,Map<String, Object> variables){
		
		if(value==null || value.length()==0){
			return "";
		}
		if(variables==null){
			variables=new HashMap<String, Object>();
		}
		String pattern_str="\\{\\w+\\}";
		Pattern pattern = Pattern.compile(pattern_str,Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(value);
		StringBuffer sbr = new StringBuffer();
		while (matcher.find()) {
		    String group=matcher.group();
		    
		    String key=group.replace("{", "").replace("}", "");
		    
		    Object matchValue=variables.get(key);
		    if(matchValue==null){
		    	matchValue="";
		    }
		    
		    matcher.appendReplacement(sbr, matchValue.toString());
		}
		matcher.appendTail(sbr);
		return sbr.toString();
	}
    
    
    /***
     * 
     * 把\n转成 空格替代换行
     * @param value 原始字符
     * @param lineChars  行字符数量
     * @return 处理后的字符
     */
	public static String replaceLine(final String value,int lineChars){
		
		String tmpValue=value;
		
		if(StringUtils.isEmpty(tmpValue)){
			return "";
		}
		StringBuffer sb=new StringBuffer();
		
		int idx=tmpValue.indexOf("\n");
		if(idx!=-1){
			while(idx!=-1){
				int addEmptyCharNumber=0;
				if(idx==0){
					addEmptyCharNumber=lineChars;
					tmpValue=tmpValue.substring(1);
				}else{
					String line=tmpValue.substring(0, idx);
					int lineNumber=line.length();
					addEmptyCharNumber=lineChars-lineNumber;
					sb.append(line);
					tmpValue=tmpValue.substring(idx+1);
				}
				
				for(int i=0;i<addEmptyCharNumber;i++){
					sb.append(" ");
				}
				
				idx=tmpValue.indexOf("\n");//再次搜索
				if(idx==-1){
					sb.append(tmpValue);
				}
			}
		}else{
			sb.append(value);
		}
		
		return sb.toString();
	}
	
	
	/***
     * 
     * 把\n转成 空格替代换行
     * @param value 原始字符
     * @param lineBytes  行字节数量
     * @return 处理后的字符
     */
	public static String replaceLineByte(final String value,int lineBytes){
		
		byte flag_byte=13;
		try {
			flag_byte = " ".getBytes("utf-8")[0];
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		byte n_byte=13;
		try {
			n_byte = "\n".getBytes("utf-8")[0];
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String tmpValue=value;
		
		if(StringUtils.isEmpty(tmpValue)){
			return "";
		}
		StringBuffer sb=new StringBuffer();
		
		int idx=tmpValue.indexOf("\n");
		if(idx!=-1){
			
		   try {
			byte[] value_bytes=value.getBytes("utf-8");
			
			List<List<Byte>> lineItems=new ArrayList<List<Byte>>();//所有行字节
			
			
			int index=0;
			List<Byte> lineItem=new ArrayList<Byte>();
			lineItems.add(lineItem);
			for(int i=0;i<value_bytes.length;i++){
				
				
				byte b=value_bytes[i];
				if(b==n_byte){
					index++;
					
					List<Byte> lineItem_n=new ArrayList<Byte>();
					lineItems.add(lineItem_n);
					
					continue;
				}else{
					lineItems.get(index).add(b);
				}
				
			}
			
			for(int i=0;i<lineItems.size();i++){
				List<Byte> lineByteArray=lineItems.get(i);
				
				int byte_len=lineByteArray.size();
				
				int short_n_byte=13-byte_len%13;
				if(short_n_byte>0){
					
					for(int j=0;j<short_n_byte;j++){
						lineByteArray.add(flag_byte);
					}
				}
				
				Byte[] act_data=new Byte[lineByteArray.size()];
				lineByteArray.toArray(act_data);
				
				byte[] act_data_bytearray=new byte[act_data.length];
				for(int k=0;k<act_data.length;k++){
					act_data_bytearray[k]=act_data[k];
				}
				
				
				String lineStr=new String(act_data_bytearray,"utf-8");
				
				sb.append(lineStr);
			}
			
			
			
			
			
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
			/*while(idx!=-1){
				int addEmptyCharNumber=0;
				if(idx==0){
					addEmptyCharNumber=lineBytes;
					tmpValue=tmpValue.substring(1);
				}else{
					String line=tmpValue.substring(0, idx);
					int lineNumber=line.length();
					addEmptyCharNumber=lineBytes-lineNumber;
					sb.append(line);
					tmpValue=tmpValue.substring(idx+1);
				}
				
				for(int i=0;i<addEmptyCharNumber;i++){
					sb.append(" ");
				}
				
				idx=tmpValue.indexOf("\n");//再次搜索
				if(idx==-1){
					sb.append(tmpValue);
				}
			}*/
			
			
			/*String pattern_str="\\n";
			Pattern pattern = Pattern.compile(pattern_str,Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(value);
			StringBuffer sbr = new StringBuffer();
			while (matcher.find()) {
			    String group=matcher.group();
			    
			    String key=group.replace("{", "").replace("}", "");
			    
			    Object matchValue=variables.get(key);
			    if(matchValue==null){
			    	matchValue="";
			    }
			    
			    matcher.appendReplacement(sbr, matchValue.toString());
			}
			matcher.appendTail(sbr);
			return sbr.toString();*/
			
			
			
		}else{
			sb.append(value);
		}
		
		return sb.toString();
	}
	
	
	
	/***
	 * 保留 N 为小数 ,四舍五入
	 * @param value 1.2345
	 * @param scale 2
	 * @return 1.23
	 */
	public static float scaleTwo(double value,int scale){
		BigDecimal   bd= new  BigDecimal(value);  //四舍五入
		float newValue =  bd.setScale(scale,   BigDecimal.ROUND_HALF_UP).floatValue();
		return newValue;
	}
	
	/***
	 * 判断是否哦是手机号码
	 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188

	　　联通：130、131、132、152、155、156、185、186
	
	　　电信：133、153、180、189、（1349卫通）、177
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles){  
		  
		//Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");  
		Pattern p = Pattern.compile("^1\\d{2}\\d{8}$");
		Matcher m = p.matcher(mobiles);  
		
		return m.matches();  
	  
	}  
	
	public static String getExceptionStackTrace(Throwable e){
		StackTraceElement[] trace = e.getStackTrace();
		StringBuffer sb=new StringBuffer();
		sb.append(e.getMessage());
		sb.append("\n");
        for (int i=0; i < trace.length; i++)
        	sb.append("\tat " + trace[i]).append("\n");
        return sb.toString();
	}
	  
	public static void main(String[] args) {
		String str="扫一扫通知\n12-31\n 巡检点:\n <a href=' \"whl-test\",place_region_id:36,task_identity:\"20151230170000:38\",appid:\"wx29eb62d6143b3ac0\",regiongroup_planitems_id:38}'>查看巡检项</a >\n\n 更多";
		String retstr=replaceLineByte(str,13);
		System.out.println(retstr);
	}
	
	
	
}
