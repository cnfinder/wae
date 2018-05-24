package cn.finder.wae.business.domain;

import java.util.List;

public  class Tree<T> {

	protected T parent;
	
	protected List<T> children;

	public T getParent() {
		return parent;
	}

	public void setParent(T parent) {
		this.parent = parent;
	}

	public List<T> getChildren() {
		return children;
	}

	public void setChildren(List<T> children) {
		this.children = children;
	}
	
	
} 
