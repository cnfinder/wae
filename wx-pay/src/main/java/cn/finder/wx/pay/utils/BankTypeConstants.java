package cn.finder.wx.pay.utils;

import java.util.HashMap;
import java.util.Map;

public class BankTypeConstants {

	private static Map<String,String> bankTypes=new HashMap<String, String>();
	
	static {
		bankTypes.put("CFT", "微信钱包");
		bankTypes.put("ICBC_DEBIT", "工商银行(借记卡)");
		bankTypes.put("ICBC_CREDIT", "工商银行(信用卡)");
		bankTypes.put("ABC_DEBIT", "农业银行(借记卡)");
		bankTypes.put("ABC_CREDIT", "农业银行(信用卡)");
		bankTypes.put("PSBC_DEBIT", "邮政储蓄银行(借记卡)");
		bankTypes.put("PSBC_CREDIT", "邮政储蓄银行(信用卡)");
		bankTypes.put("CCB_DEBIT", "建设银行(借记卡)");
		bankTypes.put("CCB_CREDIT", "建设银行(信用卡)");
		bankTypes.put("CMB_DEBIT", "光大银行(借记卡)");
		bankTypes.put("CMB_CREDIT", "光大银行(信用卡)");
		bankTypes.put("CIB_DEBIT", "兴业银行(借记卡)");
		bankTypes.put("CIB_CREDIT", "兴业银行(信用卡)");
		bankTypes.put("CITIC_DEBIT", "中信银行(借记卡)");
		bankTypes.put("CITIC_CREDIT", "中信银行(信用卡)");
		bankTypes.put("BOSH_DEBIT", "上海银行(借记卡)");
		bankTypes.put("BOSH_CREDIT", "上海银行(信用卡)");
		bankTypes.put("CRB_DEBIT", "华润银行(借记卡)");
		bankTypes.put("HZB_DEBIT", "杭州银行(借记卡)");
		bankTypes.put("HZB_CREDIT", "杭州银行(信用卡)");
		bankTypes.put("BSB_DEBIT", "包商银行(借记卡)");
		bankTypes.put("BSB_CREDIT", "包商银行(信用卡)");
		bankTypes.put("CQB_DEBIT", "重庆银行(借记卡)");
		bankTypes.put("SDEB_DEBIT", "顺德农商行(借记卡)");
		bankTypes.put("SZRCB_DEBIT", "深圳农商银行(借记卡)");
		bankTypes.put("HRBB_DEBIT", "哈尔滨银行(借记卡)");
		bankTypes.put("BOCD_DEBIT", "成都银行(借记卡)");
		bankTypes.put("GDNYB_DEBIT", "南粤银行(借记卡)");
		bankTypes.put("GDNYB_CREDIT", "南粤银行(信用卡)");
		bankTypes.put("GZCB_DEBIT", "广州银行(借记卡)");
		bankTypes.put("GZCB_CREDIT", "广州银行(信用卡)");
		bankTypes.put("JSB_DEBIT", "江苏银行(借记卡)");
		bankTypes.put("JSB_CREDIT", "江苏银行(信用卡)");
		bankTypes.put("NBCB_DEBIT", "宁波银行(借记卡)");
		bankTypes.put("NBCB_CREDIT", "宁波银行(信用卡)");
		bankTypes.put("NJCB_DEBIT", "南京银行(借记卡)");
		bankTypes.put("JZB_DEBIT", "晋中银行(借记卡)");
		bankTypes.put("KRCB_DEBIT", "昆山农商(借记卡)");
		bankTypes.put("LJB_DEBIT", "龙江银行(借记卡)");
		bankTypes.put("LNNX_DEBIT", "辽宁农信(借记卡)");
		bankTypes.put("LZB_DEBIT", "兰州银行(借记卡)");
		bankTypes.put("WRCB_DEBIT", "无锡农商(借记卡)");
		bankTypes.put("ZYB_DEBIT", "中原银行(借记卡)");
		bankTypes.put("ZJRCUB_DEBIT", "浙江农信(借记卡)");
		bankTypes.put("WZB_DEBIT", "温州银行(借记卡)");
		bankTypes.put("XAB_DEBIT", "西安银行(借记卡)");
		bankTypes.put("JXNXB_DEBIT", "江西农信(借记卡)");
		bankTypes.put("NCB_DEBIT", "宁波通商银行(借记卡)");
		bankTypes.put("NYCCB_DEBIT", "南阳村镇银行(借记卡)");
		bankTypes.put("NMGNX_DEBIT", "内蒙古农信(借记卡)");
		bankTypes.put("SXXH_DEBIT", "陕西信合(借记卡)");
		bankTypes.put("SRCB_CREDIT", "上海农商银行(信用卡)");
		/*bankTypes.put("", "");
		bankTypes.put("", "");
		bankTypes.put("", "");
		bankTypes.put("", "");
		bankTypes.put("", "");
		bankTypes.put("", "");
		bankTypes.put("", "");
		bankTypes.put("", "");
		bankTypes.put("", "");
		bankTypes.put("", "");
		bankTypes.put("", "");
		bankTypes.put("", "");
		bankTypes.put("", "");
		bankTypes.put("", "");
		bankTypes.put("", "");
		bankTypes.put("", "");
		bankTypes.put("", "");
		bankTypes.put("", "");
		bankTypes.put("", "");
		bankTypes.put("", "");
		bankTypes.put("", "");
		bankTypes.put("", "");
		bankTypes.put("", "");
		bankTypes.put("", "");
		bankTypes.put("", "");
		bankTypes.put("", "");
		bankTypes.put("", "");
		bankTypes.put("", "");
		bankTypes.put("", "");
		bankTypes.put("", "");
		bankTypes.put("", "");
		bankTypes.put("", "");
		bankTypes.put("", "");
		bankTypes.put("", "");
		bankTypes.put("", "");
		bankTypes.put("", "");
		bankTypes.put("", "");
		bankTypes.put("", "");
		bankTypes.put("", "");
		bankTypes.put("", "");
		bankTypes.put("", "");
		bankTypes.put("", "");
		bankTypes.put("", "");
		bankTypes.put("", "");
		bankTypes.put("", "");
		bankTypes.put("", "");
		bankTypes.put("", "");
		bankTypes.put("", "");
		bankTypes.put("", "");
		bankTypes.put("", "");
		bankTypes.put("", "");
		bankTypes.put("", "");
		bankTypes.put("", "");
		bankTypes.put("", "");
		bankTypes.put("", "");
		bankTypes.put("", "");
		bankTypes.put("", "");
		bankTypes.put("", "");*/
		
	}
	
	/***
	 * 获取银行的描述
	 * @param key
	 * @return
	 */
	public static String getBankTypeDes(String key){
		if(bankTypes.containsKey(key)){
			return bankTypes.get(key);
		}else{
			return "";
		}
	}
}
