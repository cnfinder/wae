package cn.finder.wx.request;

import cn.finder.wx.response.SetIndustryTemplateMsgResponse;

/***
 * 模板消息-设置所属行业
 * @author whl
 *
 */
public class SetIndustryTemplateMsgRequest extends WeixinRequest<SetIndustryTemplateMsgResponse>{

	//互联网/电子商务
	public static String INDUSTRY_CODE_IT_INTERNET_ELC="1";
	
	public static String INDUSTRY_CODE_FINANCE_BANK="7";
	
	
	//餐饮
	public static String INDUSTRY_CODE_CATERING_BCATERING="10";
	
	private String industry_id1;
	
	private String industry_id2;

	public String getIndustry_id1() {
		return industry_id1;
	}

	public void setIndustry_id1(String industry_id1) {
		this.industry_id1 = industry_id1;
	}

	public String getIndustry_id2() {
		return industry_id2;
	}

	public void setIndustry_id2(String industry_id2) {
		this.industry_id2 = industry_id2;
	}
	
	
	
}
