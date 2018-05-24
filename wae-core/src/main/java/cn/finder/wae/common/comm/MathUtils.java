package cn.finder.wae.common.comm;

import java.util.regex.Pattern;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

public class MathUtils {

	/***
	 * 判断是否是区间格式表达式
	 * @param betweenPattern
	 * @return
	 */
	public static boolean isBetweenPatter(String betweenPattern){
		boolean b=Pattern.matches("^(\\(|\\[)(-?)\\d+\\.{0,1}\\d*,(-?)\\d+\\.{0,1}\\d*(\\)|\\])$", betweenPattern);
		return b;
	}
	/***
	 * 判断某个值是否在 指定的区间内  
	 * @param betweenPattern  example:[2,5)
	 * @param value example: 3
	 * @return 
	 */
	public static boolean isBetween(String betweenPattern,String value){
		
		boolean b=Pattern.matches("^(\\(|\\[)(-?)\\d+\\.{0,1}\\d*,(-?)\\d+\\.{0,1}\\d*(\\)|\\])$", betweenPattern);
		if(b){
			String tmpStr=betweenPattern.replace("[", "").replace("]", "").replace(")", "").replace("(", "");
			String[] values=tmpStr.split(",");
			String v1=values[0];
			String v2=values[1];
			
			String ope1="";
			String ope2="";
			if(betweenPattern.startsWith("[")){
				ope1=">=";
			}else{
				ope1=">";
			}
			
			if(betweenPattern.endsWith("]")){
				ope2="<=";
			}else{
				ope2="<";
			}
			ExpressionParser parser = new SpelExpressionParser();  
			
		    boolean result=parser.parseExpression(value+ope1+v1).getValue(boolean.class) && parser.parseExpression(value+ope2+v2).getValue(boolean.class);
			
			return result;
		}
		return false;
	}
}
