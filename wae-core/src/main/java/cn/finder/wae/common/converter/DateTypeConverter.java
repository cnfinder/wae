package cn.finder.wae.common.converter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.opensymphony.xwork2.conversion.impl.DefaultTypeConverter;

public class DateTypeConverter extends DefaultTypeConverter {

	@SuppressWarnings("unchecked")
	@Override
	public Object convertValue(Map<String, Object> context, Object value,
			Class toType) {
		//yyyy-MM-dd'T'HH:mm:ss   yyyy-MM-dd HH:mm:ss
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		try {
			if (toType == Date.class) { // 当字符串向Date类型转换时
				String[] params = (String[]) value;
				return sdf.parseObject(params[0]);
			} else if (toType == String.class) { // 当Date转换成字符串时
				Date date = (Date) value;
				return sdf.format(date);
			}
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}
