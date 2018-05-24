package cn.finder.wae.business.dto;

import cn.finder.ui.webtool.QueryCondition;

public class StringParaQueryConditionDto extends QueryCondition<Object[]> {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2976645182671836074L;
	private String stringPara;

	public String getStringPara() {
		return stringPara;
	}

	public void setStringPara(String stringPara) {
		this.stringPara = stringPara;
	}
	
	
	
}
