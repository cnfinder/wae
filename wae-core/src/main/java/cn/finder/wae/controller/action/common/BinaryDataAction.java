package cn.finder.wae.controller.action.common;

import java.io.FileNotFoundException;
import java.io.InputStream;

import cn.finder.wae.business.domain.ShowDataConfig;
import cn.finder.wae.business.module.common.service.CommOperationService;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.common.base.BaseValidateActionSupport;
import cn.finder.wae.common.exception.Fault;

public class BinaryDataAction  extends BaseValidateActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private InputStream inStream;
	
	private CommOperationService commOperationService;

	private Fault falut =new Fault();

	public InputStream getInStream() {
		return inStream;
	}


	public void setInStream(InputStream inStream) {
		this.inStream = inStream;
	}


	/***
	 * 加载字段图片
	 * 参数 showdataConfigId  -- tablename fieldaname
	 *     primary key field   -- value
	 * @return
	 * @throws FileNotFoundException 
	 */
	public String loadImageData() {
		long showtableConfigId = validateFieldLong("ds_showdataConfigId");
		long showdataConfigId = validateFieldLong("showdataConfigId");
		String primaryKeyField = validateFieldIsEmpty("primary_key_field");
		String primaryKeyValue =validateFieldIsEmpty("primary_key_value");
		
		ShowDataConfig sdc=ArchCache.getInstance().getShowTableConfigCache().getShowDataConfigById(showdataConfigId);
		
		
	/*	byte[] d = commOperationService.loadImageData(showtableConfigId,sdc.getShowTableName(), primaryKeyField, primaryKeyValue, sdc.getFieldName());
		
		inStream = new ByteArrayInputStream(d);
		*/
		
		return SUCCESS;
	}


	@Override
	protected Fault getFault() {
		// TODO Auto-generated method stub
		return falut;
	}


	public void setCommOperationService(CommOperationService commOperationService) {
		this.commOperationService = commOperationService;
	}


	


}
