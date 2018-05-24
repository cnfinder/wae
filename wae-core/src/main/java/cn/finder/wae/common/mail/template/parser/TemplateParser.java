package cn.finder.wae.common.mail.template.parser;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


public abstract class TemplateParser {

	private static Logger logger = Logger.getLogger(TemplateParser.class);
	public String parser(String content)
	{
		Map<String, String> paras = findMapPara();
		if(paras==null || paras.size()<1 || StringUtils.isEmpty(content))
		{
			return content;
		}
		
		Set<Entry<String, String>> set =paras.entrySet();
		
		Iterator<Map.Entry<String,String>> ite =set.iterator();
		
		while(ite.hasNext())
		{
			Entry<String, String> entry = ite.next();
			String key = entry.getKey();
			String value = entry.getValue();
			logger.debug("===key:key"+",value="+value);
			content = content.replaceAll("\\{"+key+"\\}", value);
		}
		
		return content;
	}
	
	
	public abstract Map<String, String> findMapPara();
	
}
