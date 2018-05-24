package cn.finder.wae.business.dto;

import java.util.HashMap;
import java.util.Map;

import cn.finder.ui.webtool.QueryCondition;

/**
 * @author: wuhualong
 * @data:2014-5-11下午4:21:16
 * @function:
 */
public class SearchQueryCondition extends QueryCondition<Object[]> {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 3979689060281713019L;
	
	// 参数 传递  Object为数组
	private Map<String,Object>  forwardParams=new HashMap<String, Object>();

	public Map<String, Object> getForwardParams() {
		return forwardParams;
	}

	public void setForwardParams(Map<String, Object> forwardParams) {
		this.forwardParams = forwardParams;
	}
	
	
	
	
	
}
