package cn.finder.wae.common.comm.templatemsg;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/***
 * 模板消息 微信消息 使用map里面的值 替换  标记 {data}
 * @author whl
 *
 */
public abstract class WXTemplateMsgParser {

	private static Logger logger = Logger.getLogger(WXTemplateMsgParser.class);
	public String parser(String content)
	{
		Map<String, Object> paras = findMapPara();
		if(paras==null || paras.size()<1 || StringUtils.isEmpty(content))
		{
			return content;
		}
		
		Set<Entry<String, Object>> set =paras.entrySet();
		
		Iterator<Map.Entry<String,Object>> ite =set.iterator();
		
		while(ite.hasNext())
		{
			Entry<String, Object> entry = ite.next();
			String key = entry.getKey();
			Object value = entry.getValue();
			logger.debug("===key:key"+",value="+value);
			content = content.replaceAll("\\{"+key+"\\}", value.toString());
		}
		
		return content;
	}
	
	
	public abstract Map<String, Object> findMapPara();
	
}
