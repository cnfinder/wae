package cn.finder.wae.service.rest.model;

import java.io.Serializable;
/**
 *@author finder
 */
public class Item implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2109557841067508655L;

	private int intValue;
	
	private long longValue;
	
	private String stringValue;

	public int getIntValue() {
		return intValue;
	}

	public void setIntValue(int intValue) {
		this.intValue = intValue;
	}

	public long getLongValue() {
		return longValue;
	}

	public void setLongValue(long longValue) {
		this.longValue = longValue;
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}
	
	
	
	 
}
