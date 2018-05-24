package cn.finder.wae.service.rest.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 统一返回的数据对象
 * @author 吴华龙
 * 
 * @param <T> model下的类型
 * 
 */
@XmlRootElement(name="response")
@XmlAccessorType(XmlAccessType.FIELD)
public class Response<T> {
	
	@XmlElement(name="message")
	private Message message=new Message();
	
	@XmlElement(name="pageIndex")
	private int pageIndex;
	
	@XmlElement(name="pageSize")
	private int pageSize;
	
	@XmlElement(name="totalRecord")
	private long totalRecord;
	
	@XmlElement(name="entity")
	private List<T> entities=new ArrayList<T>();

	@XmlElement(name="tag")
	private Object tag;

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}


	public long getTotalRecord() {
		return totalRecord;
	}

	public void setTotalRecord(long totalRecord) {
		this.totalRecord = totalRecord;
	}

	public List<T> getEntities() {
		return entities;
	}

	public void setEntities(List<T> entities) {
		this.entities = entities;
	}

	
	
	public void setEntity(T entity)
	{
		this.entities.removeAll(this.entities);
		this.entities.add(entity);
	}

	public Object getTag() {
		return tag;
	}

	public void setTag(Object tag) {
		this.tag = tag;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}
	
	
	
}
