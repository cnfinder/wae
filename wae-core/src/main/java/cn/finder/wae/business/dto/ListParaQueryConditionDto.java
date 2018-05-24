package cn.finder.wae.business.dto;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.record.formula.functions.T;

import cn.finder.ui.webtool.QueryCondition;

public class ListParaQueryConditionDto extends QueryCondition<Object[]> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3572219798927066055L;

	private List<T>  params;
	
	

	public ListParaQueryConditionDto() {
		super();
		this.params = new ArrayList<T>();
	}



	public List<T> getParams() {
		return params;
	}



	public void setParams(List<T> params) {
		this.params = params;
	}



	

	
	
}
