package cn.finder.wae.controller.action.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.module.common.service.QueryService;
import cn.finder.wae.common.base.BaseActionSupport;
import cn.finder.wae.common.comm.JXLExcel;

public class ExportAction extends BaseActionSupport{

	
	private long showtableConfigId;
	
	private QueryService queryService;
	
	private TableQueryResult queryResult;
	
	private int pageIndex=1;
	
	private int pageSize;
	
	private QueryCondition<Object[]> queryCondition=new QueryCondition<Object[]>();
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public String exportExcel(){
		
		ByteArrayOutputStream outStream =new ByteArrayOutputStream();
		
		/*JXLExcel jxlExcel=new JXLExcel(item.getExcelFileData(),outStream);
		
		
		jxlExcel.close()*/
		
		return SUCCESS;
	}


	public long getShowtableConfigId() {
		return showtableConfigId;
	}


	public void setShowtableConfigId(long showtableConfigId) {
		this.showtableConfigId = showtableConfigId;
	}


	public TableQueryResult getQueryResult() {
		return queryResult;
	}


	public void setQueryResult(TableQueryResult queryResult) {
		this.queryResult = queryResult;
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


	public QueryCondition<Object[]> getQueryCondition() {
		return queryCondition;
	}


	public void setQueryCondition(QueryCondition<Object[]> queryCondition) {
		this.queryCondition = queryCondition;
	}


	public void setQueryService(QueryService queryService) {
		this.queryService = queryService;
	}

	
	
}
