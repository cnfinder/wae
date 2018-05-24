package cn.finder.wae.common.se;

import java.util.List;

public class SEResult<T> {

	
	private List<T> result;
	
	private int pageIndex;
	
	private int pageSize;
	
	private long count;
	
	

	public SEResult() {
		super();
	}

	public List<T> getResult() {
		return result;
	}

	public void setResult(List<T> result) {
		this.result = result;
	}

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

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}
	
	
	
}
