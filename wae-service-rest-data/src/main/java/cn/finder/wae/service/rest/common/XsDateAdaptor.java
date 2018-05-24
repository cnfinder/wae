package cn.finder.wae.service.rest.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;


public class XsDateAdaptor extends XmlAdapter<String, Date> {

	private static SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");

	public Date unmarshal(String s) {

		Date d;
		try {
			d = sd.parse(s);
		} catch (ParseException e) {
			throw new RuntimeException("Invalid xs:date format!" + s);
		}
		return d;
	}

	@Override
	public String marshal(Date d) throws Exception {
		if (d != null)
			return sd.format(d);
		else
			return null;
	}
}