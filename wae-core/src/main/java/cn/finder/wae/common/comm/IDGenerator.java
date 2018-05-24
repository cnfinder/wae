package cn.finder.wae.common.comm;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.log4j.Logger;

public class IDGenerator {

	private static Logger logger = Logger.getLogger(IDGenerator.class);

	/** The FieldPosition. */
	private static final FieldPosition HELPER_POSITION = new FieldPosition(0);
	/** This Format for format the data to special format. */
	private final static Format dateFormat = new SimpleDateFormat("YYYYMMddHHmmssS");
	/** This Format for format the number tospecial format. */
	private final static NumberFormat numberFormat = new DecimalFormat("0000");
	/** This int is the sequence number ,the default value is 0. */
	private static int seq = 0;
	private static final int MAX = 9999;

	/**
	 * 时间格式生成序列
	 * 
	 * @return String
	 */
	public static synchronized String generate() {
		return generate("");
	}
	
	public static synchronized String generate(String prefix){
		
		String retStr="";
		Calendar rightNow = Calendar.getInstance();
		StringBuffer sb = new StringBuffer();
		dateFormat.format(rightNow.getTime(), sb, HELPER_POSITION);
		numberFormat.format(seq, sb, HELPER_POSITION);
		if (seq == MAX) {
			seq = 0;
		} else {
			seq++;
		}

		if(prefix!=null){
			retStr=prefix+sb.toString();
		}
		
		logger.info("THE SQUENCE IS :" + retStr);

		return retStr;
	}
	
	public static void main(String[] args){
		generate();
	}
}
