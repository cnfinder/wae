package cn.finder.wae.common.comm;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

public class DateUtils {
	
	
	private final static String DATE_FORMAT="yyyy-MM-dd HH:mm:ss";
	private final static String DATE_FORMAT2="yyyy-MM-dd HH";
	private final static String DATE_FORMAT3="yyyy-MM-dd'T'HH:mm:ss";
	private final static String DATE_FORMAT4="yyyy-MM-dd";
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
			return null;
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
	 * 判断当前日期是星期几<br>
	 * <br>
	 * 
	 * @param pTime
	 *            修要判断的时间<br>
	 * @return dayForWeek 判断结果<br>
	 * @Exception 发生异常<br>
	 */
	public static int dayForWeek(String pTime) throws Exception {
		return dayForWeek(pTime,"yyyy-MM-dd");
	}
	
	
	public static int dayForWeek(String pTime,String formatStr) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		Date date = format.parse(pTime);
		return dayForWeek(date);
	}
	
	/****
	 * 判断当前日期是星期几
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public static int dayForWeek(Date date) throws Exception {
		
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int dayForWeek = 0;
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			dayForWeek = 7;
		} else {
			dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		}
		return dayForWeek;
	}
	

}
