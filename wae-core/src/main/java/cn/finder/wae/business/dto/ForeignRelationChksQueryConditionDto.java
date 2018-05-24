package cn.finder.wae.business.dto;

import java.util.List;

import cn.finder.ui.webtool.QueryCondition;

public class ForeignRelationChksQueryConditionDto extends QueryCondition<Object[]> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3572219798927066055L;

	private String  mainValue;
	
	private List<String> subValues;

	

	public String getMainValue() {
		return mainValue;
	}

	public void setMainValue(String mainValue) {
		this.mainValue = mainValue;
	}

	public List<String> getSubValues() {
		return subValues;
	}

	public void setSubValues(List<String> subValues) {
		this.subValues = subValues;
	}
	
	


	

	

	
	
}
