package cn.finder.wae.business.dto;

import cn.finder.ui.webtool.QueryCondition;

public class ObjectParaQueryConditionDto extends QueryCondition<Object[]> {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2976645182671836074L;
	private Object objectPara;
	public Object getObjectPara() {
		return objectPara;
	}
	public void setObjectPara(Object objectPara) {
		this.objectPara = objectPara;
	}

	
	
	
}
