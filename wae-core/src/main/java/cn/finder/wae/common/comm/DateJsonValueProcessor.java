package cn.finder.wae.common.comm;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

public class DateJsonValueProcessor implements JsonValueProcessor {
	private String format = "yyyy-MM-dd HH:mm:ss.SSS";
	private SimpleDateFormat sdf = new SimpleDateFormat(format); // 处理数组的值

	public DateJsonValueProcessor() {
		super();
	}
	
	public DateJsonValueProcessor(String format) {
		super();
		this.format = format;
	}

	@Override
	public Object processArrayValue(Object value, JsonConfig jsonConfig) {
		return this.process(value);
	} // 处理对象的值

	@Override
	public Object processObjectValue(String key, Object value,
			JsonConfig jsonConfig) {

		return this.process(value);
	} // 处理Date类型返回的Json数值

	private Object process(Object value) {
		if (value == null) {
			return "";
		} else if (value instanceof Date){
			value =  sdf.format((Date) value);
			return value;
		}
		
		else {
			return value;
		}
	}
	
	
}
