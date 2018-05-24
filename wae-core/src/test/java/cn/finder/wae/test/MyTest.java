package cn.finder.wae.test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import cn.finder.httpcommons.utils.JsonUtils;
import cn.finder.wae.common.comm.AESV3;
import cn.finder.wae.common.comm.Common;
import cn.finder.wae.common.third.ThirdUtils;
import cn.finder.wx.corp.domain.Department;
import cn.finder.wx.corp.response.FindDepartmentResponse;
import cn.finder.wx.service.WXService;
import sun.misc.BASE64Decoder;

public class MyTest {

	@Test
	public  void testBigDecimal(){
		float rate=0.1f;
		float rateMoney= ((1.0f/365)*rate*100);
			System.out.println("===rateMoney："+rateMoney);
			BigDecimal   bd=new  BigDecimal((double)rateMoney);    
			bd = bd.setScale(2,BigDecimal.ROUND_HALF_UP);    
			rateMoney =bd.floatValue();  //精确收益
			 
			System.out.println("===rateMoney："+rateMoney);
	}
	
	public static void main(String[] args) {
		String v="扫一扫通知：\n09-17\n对不起，没有扫描到相关信息";
		String newv=Common.replaceLine(v, 13);
		System.out.println(newv);
	}
	@Test
	public void testPattern(){
		Map<String, Object> variables=new HashMap<String, Object>();
		variables.put("ip", "iv.cwintop.com");
		variables.put("port", 8080);
		
		String url="http://{ip}:{port}/iv/page/rreprot.jsp";
		
		String newUrl =descWrapper(url,variables);
		System.out.println(newUrl);
	}
	
	private String descWrapper(String value,Map<String, Object> variables){
		
		if(value==null || value.length()==0){
			return "";
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
	
	@Test
	public void testEncoder(){
		
		String v="即佛啊";
		try {
			String res=URLDecoder.decode(v, "UTF-8");
			System.out.println(res);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testReplaceLine(){
		
		//String v="扫一扫通知：\n对不起，没有扫描到相关信息";
		//String v="扫一扫通知\n09-16\n <br/>设备类型:电表\n 设备编号:EXO000-code\n  安装时间:2015-09-15 14:37:14.0\n\n  < a href=>上报故障</a>";
		String v="扫一扫通知：\n09-17\n对不起，没有扫描到相关信息";
		System.out.println(v.length());
		System.out.println(v.getBytes().length);
		
		
		String newv=replaceLine(v,13);
		
		System.out.println("==v"+v);
		System.out.println(newv);
		
		newv=Common.replaceLine(v, 13);
		System.out.println("===================");
		System.out.println(newv);
	}
	
	
	private String replaceLine(final String value,int lineChars){
		
		String tmpValue=value;
		
		if(StringUtils.isEmpty(tmpValue)){
			return "";
		}
		StringBuffer sb=new StringBuffer();
		
		int idx=tmpValue.indexOf("\n");
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
		
		return sb.toString();
	}
	
	
	@Test
	public void testDep(){
		WXService.CorpService service=new WXService.CorpService();
		String accessToken="27o8y2NyuhWfEKMCGpw4sivWmKEf1j2uqRZpzUVxOSzyQ_dSqJNass2Fi_6RlmC0Ybhc1sdx65WjzY1HIqn4Lg";
	    FindDepartmentResponse departmentResp=service.findDepartment(accessToken, null);
	 	
		if(departmentResp.isSuccess()){
			
			if(departmentResp.getDepartment()!=null){
				
				/*String jsonStr=JsonUtils.getJsonString4JavaPOJO(departmentResp.getDepartment());
				
				
				
				@SuppressWarnings("unchecked")
				List resultList=  JsonUtil.getList4Json(jsonStr, ArrayList.class);
				
				System.out.println("dfasdf");*/
				
				List<Map<String,Object>> resList=new ArrayList<Map<String,Object>>();
				for(int i=0;i<departmentResp.getDepartment().size();i++){
					Department d=departmentResp.getDepartment().get(i);
					
					
					Map<String,Object> item=JsonUtils.getMap4Json(JsonUtils.getJsonString4JavaPOJO(d));
					resList.add(item);
					
				}
				
				System.out.println("dfasdf");
				
				
			}
			
		}
	}
	
	
	/***
	 * 
	 */
	
	@Test
	public void test02(){
		//String patter ="[5.1,8.2)".trim();
		//String v="8.2";
		
		String patter ="[0,2)".trim();
		String v="0";
		
		//^\\[\\w+,\\w+\\)|\\]$
		//boolean b=Pattern.matches("^(\\(|\\[)\\d+\\.{0,1}\\d*,\\d+\\.{0,1}\\d*(\\)|\\])$", patter);
		boolean b=Pattern.matches("^(\\(|\\[)(-?)\\d+\\.{0,1}\\d*,(-?)\\d+\\.{0,1}\\d*(\\)|\\])$", patter);
		if(b){
			String tmpStr=patter.replace("[", "").replace("]", "").replace(")", "").replace("(", "");
			String[] values=tmpStr.split(",");
			String v1=values[0];
			String v2=values[1];
			
			String ope1="";
			String ope2="";
			if(patter.startsWith("[")){
				ope1=">=";
			}else{
				ope1=">";
			}
			
			if(patter.endsWith("]")){
				ope2="<=";
			}else{
				ope2="<";
			}
			ExpressionParser parser = new SpelExpressionParser();  
		    EvaluationContext context = new StandardEvaluationContext();
			
		    boolean bOpe1=parser.parseExpression(v+ope1+v1).getValue(boolean.class) && parser.parseExpression(v+ope2+v2).getValue(boolean.class);
			
			
		    System.out.println(bOpe1);
		    
		    
			//if(v ope1 v1 && v ope2 v2)
			
			
			
			
		}
		
	}
	
	
	@Test
	public void test2(){
		String a ="aaaaa_oXkUgwB2bv0_p_zCMaAJvm0-iryk";
		int idx=a.indexOf("_");
		
		String parent_openid=a.substring(idx+1);
		
		System.out.println(parent_openid);
	}
	
	@Test
	public void testCalander(){
		
		 Calendar   cal_nows=Calendar.getInstance();
		 int hour=8;
		 
		 
		 cal_nows.set(Calendar.HOUR_OF_DAY, hour);
		 cal_nows.set(Calendar.MINUTE, 0);
		 cal_nows.set(Calendar.SECOND, 0);
		// cal_nows.set(Calendar.HOUR, -12);//不知道为什么 快12个小时
		 
		 
		 
		 
		 Date set_date=cal_nows.getTime();//比较时间
		 System.out.println(set_date.toString());
	}
	
	
	
	@Test
	public void testSF(){
		String DATE_FORMAT_FULL="yyyy-MM-dd HH:mm:ss";
		String DATE_FORMAT="yyyy-MM-dd HH:mm:ss";
		 String DATE_FORMAT2="yyyy-MM-dd HH";
		String DATE_FORMAT3="yyyy-MM-dd'T'HH:mm:ss";
		String DATE_FORMAT4="yyyy-MM-dd";
		String dateStr="2016-08-10";
		SimpleDateFormat sdf=new SimpleDateFormat(DATE_FORMAT_FULL);
		 Date d=null;
		try {
			
			 d= sdf.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			try
			{
				d= Common.parseDate(dateStr, DATE_FORMAT);
			}
			catch(Exception exp)
			{
				//d= Common.parseDate2(dateStr);
				System.out.println(exp);
			}
		}
	}
	
	
	@Test
	public void base64ToImg(){
		
		String imgStr="data:image/png;base64,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";
		
		if (imgStr == null) // 图像数据为空
			return ;
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			// Base64解码
			byte[] b = decoder.decodeBuffer(imgStr);
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {// 调整异常数据
					b[i] += 256;
				}
			}
			// 生成jpeg图片
			String imgFilePath = "d:/test22.png";// 新生成的图片
			OutputStream out = new FileOutputStream(imgFilePath);
			out.write(b);
			out.flush();
			out.close();
			return ;
		} catch (Exception e) {
			return ;
		}
		
		
	}
	
	
	@Test
	public void testEmoji(){
		String content="\\xF0\\x9F\\x98\\x84";
		System.out.println("==============:"+content);
		if(content.indexOf("{表情}")!=-1){
			content=content.replace("{表情}", "\\x");
		}else{
			content=content.replace("\\x", "{表情}");
		}
		
		System.out.println("==============:"+content);
	}
	
	@Test
	public void testNewJDK(){
		ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
		long l=threadLocalRandom.nextLong();
	}
	
	@Test
	public void testShortLinkUrl() throws IOException{
		String shortUrl=ThirdUtils.toShortUrl("http://www.baidu.com");
		
		System.out.println(shortUrl);
	}
	
	
	@Test
	public void testAesv2(){
		String d=AESV3.encrypt("123","pass","myiv");
		System.out.println(d);
		
		
		
		String dec_d=new String(AESV3.decrypt(d, "pass","myiv"));
		System.out.println(dec_d);
	}
}
