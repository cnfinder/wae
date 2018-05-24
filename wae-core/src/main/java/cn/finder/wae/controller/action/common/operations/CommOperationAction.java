package cn.finder.wae.controller.action.common.operations;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletInputStream;

import org.apache.log4j.Logger;
import org.apache.struts2.json.annotations.JSON;

import cn.finder.wae.business.domain.ShowDataConfig;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.dto.DeleteQueryConditionDto;
import cn.finder.wae.business.dto.ForeignRelationChksQueryConditionDto;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.business.module.common.service.CommOperationService;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.common.base.BaseValidateActionSupport;
import cn.finder.wae.common.comm.JsonUtil;
import cn.finder.wae.common.exception.Fault;

public class CommOperationAction extends BaseValidateActionSupport{
	
	/**
	 * 
	 */
	
	private Logger logger = Logger.getLogger(CommOperationAction.class);
	private static final long serialVersionUID = 1L;
	private boolean ok=false;
	
	private String msg;
	
	private long showtableConfigId;
	
	private String[]  ids=null;
	
	private CommOperationService commOperationService;
	
	private MapParaQueryConditionDto<String, Object> queryCondition=new MapParaQueryConditionDto<String, Object>();
	
	private TableQueryResult queryResult;
	
	private Fault fault=new Fault();
	
	//------------------------文件上传 接收参数---------------------//
	private File[] files;//获取上传文件
	private String[] filesFileName;//获取上传文件名称
	private String[] filesContentType;//获取上传文件类型
    
    
	
	

	public void setCommOperationService(CommOperationService commOperationService) {
		this.commOperationService = commOperationService;
	}
	
	public String addRecordPage(){
		return SUCCESS;
	}

	/***
	 * replaced by commRequest
	 * @return
	 */
	@Deprecated
	public String addRecord()
	{
		
		return commRequest();
		
	}

	
	@Deprecated
	public String addRecordCascade()
	{

		return new ActionTemplateExecuter() {
			
			@Override
			protected String execute() throws Exception {
				String jsondata = request.getParameter("data");
				
				int i =commOperationService.addRecordCascade(jsondata);
				ok = true;	
				msg = "添加成功";
				return SUCCESS;
				
			}
			
			@Override
			protected void assignMsg(String msg) {
				// TODO Auto-generated method stub
				CommOperationAction.this.msg = msg;
			}
		}.run();
	}
	
	@Deprecated
	public String editRecordPage(){
		return SUCCESS;
	}
	
	/***
	 * replaced by commRequest
	 * @return
	 */
	@Deprecated
	public String editRecord()
	{
		return commRequest();
	}
	
	
	@Deprecated
	public String editRecordCascade(){
		
		return new ActionTemplateExecuter() {
			
			@Override
			protected String execute() throws Exception {
				String jsondata = request.getParameter("data");
				int i = commOperationService.editRecordCascade(jsondata);
				ok = true;
				msg="修改成功！";
				return SUCCESS;
				
			}
			
			@Override
			protected void assignMsg(String msg) {
				CommOperationAction.this.msg = msg;
			}
		}.run();
		
	}
	/***
	 * 通用删除记录行
	 * @return
	 */
	public String deleteRecords(){
		
		return new ActionTemplateExecuter() {
			
			@Override
			protected String execute() throws Exception {
				
				//commOperationService.deleteRows(showtableConfigId, ids);
				
				DeleteQueryConditionDto deleteDto = new DeleteQueryConditionDto();
				deleteDto.setShowtableConfigId(showtableConfigId);
				deleteDto.setIds(ids);
				deleteDto.put("session_user", findSessionUser());
				queryResult=commOperationService.queryTableQueryResult(showtableConfigId, deleteDto);
				ok = true;	
				CommOperationAction.this.msg = "删除成功";
				return SUCCESS;
				
			}
			
			@Override
			protected void assignMsg(String msg) {
				CommOperationAction.this.msg = msg;
			}
		}.run();
	}
	
	
	
	
	/***
	 * 简单的自动补全搜索,现在用于组合搜索
	 */
	public void commonAutoComplete()
	{
		final String tableName = request.getParameter("tableName");
		final String fieldName = request.getParameter("fieldName");
		final String key = "%"+request.getParameter("key").trim()+"%";
		queryCondition.setWherepParameterValues(new Object[]{key});
		new TemplateExecuter<List<Map<String,Object>>>() {

			@Override
			protected List<Map<String,Object>> execute() throws Exception {
				
				return commOperationService.commAutoComplete(showtableConfigId,tableName, fieldName,queryCondition);
			}
			@Override
			protected void assignMsg(String msg) {
				CommOperationAction.this.msg = msg;
				
			}
		}.run();
	}
	
	/***
	 * 简单的自动补全搜索
	 * 多列搜索  现在用于 工具栏 配置的 搜索
	 * 参数：
	 *   showtableConfigId
	 *   fieldName
	 *   key
	 */
	public void commonMutilFieldAutoComplete()
	{
		//final String tableName = request.getParameter("tableName");
		
		final String[] fieldNames = request.getParameterValues("fieldName");
		final String key = "%"+request.getParameter("key").trim()+"%";
		
		List<Object> whereP =new ArrayList<Object>();
		//添加参数值
		whereP.add(key);
		
		if(queryCondition.getWherepParameterValues()!=null){
			whereP.addAll(Arrays.asList(queryCondition.getWherepParameterValues()));
		}
		
		queryCondition.setWherepParameterValues(whereP.toArray());
		queryCondition.setSearchObject(fieldNames);
		
		new TemplateExecuter<List<Map<String,Object>>>() {

			@Override
			protected List<Map<String,Object>> execute() throws Exception {
				
				//return commOperationService.commonMutilFieldAutoComplete(showtableConfigId, fieldNames, key,null);
				return commOperationService.commonMutilFieldAutoComplete(showtableConfigId,queryCondition);
			}
			@Override
			protected void assignMsg(String msg) {
				CommOperationAction.this.msg = msg;
				
			}
		}.run();
	}
	
	
	
	
	public void saveForeignRelationChks(){
		
		
		new TemplateExecuter<TableQueryResult>() {

			@Override
			protected TableQueryResult execute() throws Exception {
				
				String main_value = validateFieldIsEmpty("main_value");
				 java.net.URLDecoder.decode(main_value,"utf-8");  
				ForeignRelationChksQueryConditionDto condition = new ForeignRelationChksQueryConditionDto();
				
				condition.setMainValue(URLDecoder.decode(main_value,"utf-8"));
				List<String> idsList =new ArrayList<String>();
				if(ids!=null && ids.length>0){
					for(int i=0;i<ids.length;i++){
						idsList.add(ids[i]);
					}
				}
				condition.setSubValues(idsList);
				
				return commOperationService.queryTableQueryResult(showtableConfigId, condition);
			}

			@Override
			protected void assignMsg(String msg) {
				CommOperationAction.this.msg = msg;
				
			}
		}.run();
	}
	
	public String addDataImportConfig(){
		
		return SUCCESS;
	}
	
	//数据导入处理
	@Deprecated
	public String dataImport(){
		try{
			
			Map<String, Object> data = new HashMap<String, Object>();
			
//			data.put("session_user", findSessionUser());
			
			MapParaQueryConditionDto<String, Object> para=new MapParaQueryConditionDto<String, Object>();
			
			para.setMapParas(data);
			commOperationService.dataImport(new Long(showtableConfigId).intValue(),para);
			ok = true;
			msg="插入成功！";
			
		}catch(Exception e){
			msg="插入失败！异常："+ e.getMessage();
		}
		return SUCCESS;
	}
	
	
	/***
	 * 通用请求处理   参数从data中接收， JSON类型{data:{}}
	 * @author: wuhualong
	 * @data:2014-5-19下午3:55:14
	 * @function:
	 * @return
	 */
	public String commRequest()
	{
		
		return new ActionTemplateExecuter() {
			
			@Override
			protected String execute() throws Exception {
				String jsondata = request.getParameter("data");
				logger.info("===提交data数据:"+jsondata);
				//int i =commOperationService.addRecord(jsondata);
				Map<String, Object> data = jsondata==null? new HashMap<String, Object>():JsonUtil.getMap4Json(jsondata);

				//data.put("SESSION", session);
				
				data.put("recordIds", ids);
				data.put("session_user", findSessionUser());
				//将webroot路径传入
				
				data.put("WebRoot_path", application.getRealPath("/"));
				
				data.put("files", files);
				data.put("filesFileName", filesFileName);
				data.put("filesContentType", filesContentType);
				
				
				/*MapParaQueryConditionDto<String, Object> para=new MapParaQueryConditionDto<String, Object>();
				para.setWhereCluster(queryCondition.getWhereCluster());
				para.setWherepParameterValues(queryCondition.getWherepParameterValues());
				para.setMapParas(data);*/
				queryCondition.setMapParas(data);
				
				queryResult=commOperationService.queryTableQueryResult(showtableConfigId, queryCondition);
				ok = true;
				msg="处理成功";
				return SUCCESS;
				
			}
			
			@Override
			protected void assignMsg(String msg) {
				// TODO Auto-generated method stub
				CommOperationAction.this.msg = msg;
			}
		}.run();
		
	}
	
	
	
	/***
	 * 多个列配置同时提交    参数从data中接收， JSON类型[{data:{}}]
	 * @author: wuhualong
	 * @data:2014-5-19下午3:55:14
	 * @function:
	 * @return
	 */
	public String mutilShowdataConfigUpdate()
	{
		
		return new ActionTemplateExecuter() {
			
			@Override
			protected String execute() throws Exception {
				String jsondata = request.getParameter("data");
				
				//int i =commOperationService.addRecord(jsondata);
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> dataList = JsonUtil.getList4Json(jsondata,Map.class);
					
				Map<String, Object> data =new LinkedHashMap<String, Object>();
				
				//data.put("SESSION", session);
				
				
				data.put("session_user", findSessionUser());
				//将webroot路径传入
				
				data.put("WebRoot_path", application.getRealPath("/"));
				
				data.put("showdataconfig", dataList);
				
				MapParaQueryConditionDto<String, Object> para=new MapParaQueryConditionDto<String, Object>();
				para.setWhereCluster(queryCondition.getWhereCluster());
				para.setWherepParameterValues(queryCondition.getWherepParameterValues());
				para.setMapParas(data);
				
				commOperationService.queryTableQueryResult(showtableConfigId, para);
				ok = true;
				msg="处理成功";
				return SUCCESS;
				
			}
			
			@Override
			protected void assignMsg(String msg) {
				// TODO Auto-generated method stub
				CommOperationAction.this.msg = msg;
			}
		}.run();
		
	}
	
	
	
	
	
	/***
	 * 通用获取二进制数据
	 * @throws IOException
	 */
	
	@Deprecated
	public void loadBinaryData() throws Exception{
		

		
		
		long showtableConfigId = validateFieldLong("showtableConfigId");
		
		long showdataConfigId = validateFieldLong("showdataConfigId");
		String primaryKeyField = validateFieldIsEmpty("primary_key_field");
		String primaryKeyValue =validateFieldIsEmpty("primary_key_value");
		String resourceType = validateFieldIsEmpty("resource_type"); //如 image/jpej
		
		ShowDataConfig sdc=ArchCache.getInstance().getShowTableConfigCache().getShowDataConfigById(showdataConfigId);
		
		
		
		byte[] d = commOperationService.loadBinaryData(showtableConfigId,sdc.getShowTableName(), primaryKeyField, primaryKeyValue, sdc.getFieldName());
		if(d==null || d.length==0){
			response.setHeader("Content-Type","text/html");
			
			String content = "没有相应的数据。";
			byte[] byte_content = content.getBytes("UTF-8");
			response.getOutputStream().write(byte_content, 0, byte_content.length);
		}
		else{
			response.setHeader("Content-Type",resourceType);
			
			ByteArrayInputStream stream = new ByteArrayInputStream(d);
			
		    byte[] b = new byte[1024];
		    int len = -1;
		    while ((len = stream.read(b, 0, 1024)) != -1) {
		    	response.getOutputStream().write(b, 0, len);
		    }
		}
		
	    
	    
	}
	public void rptUpload(){
		
		
		
		
		//boolean isMultipart = ServletFileUpload.isMultipartContent(request);//检查输入请求是否
		
		try
		{
			/* if (isMultipart == true) {
			       FileItemFactory factory = new DiskFileItemFactory();//为该请求创建一个DiskFileItemFactory对象，通过它来解析请求。执行解析后，所有的表单项目都保存在一个List中。
			       ServletFileUpload upload = new ServletFileUpload(factory);
			       List<FileItem> items = upload.parseRequest(request);
			       if(items!=null && items.size()>0){
			    	   FileItem  item = items.get(0);
			    	   
			    	  byte[] rptData= item.get();
			    	  
			    	  commOperationService.updateRptData(rptId, rptData);
			       }
			       
			    } else {
			       logger.debug("the enctype must be multipart/form-data");
			    }*/
			
		/*	int bufSize=request.getContentLength();
			   //读出客户端发送的数据，并写入文件流
	        byte[] dataBuf = new byte[bufSize];   
	        ServletInputStream sif = request.getInputStream();
	        
	        sif.read(dataBuf);
	        sif.close();
	        commOperationService.updateRptData(showtableConfigId,rptId, dataBuf);
	        
	        logger.debug("===报表设计文件更新成功");*/
	        
	        
	        new TemplateExecuter<Integer>() {

				@Override
				protected Integer execute() throws Exception {
					int rptId =Integer.valueOf(request.getParameter("rpt_id"));
					int bufSize=request.getContentLength();
					   //读出客户端发送的数据，并写入文件流
			        byte[] dataBuf = new byte[bufSize];   
			        ServletInputStream sif = request.getInputStream();
			        
			        sif.read(dataBuf);
			        sif.close();
			        int res = commOperationService.updateRptData(showtableConfigId,rptId, dataBuf);
			        
			        logger.debug("===报表设计文件更新成功");
					return res;
				}
				@Override
				protected void assignMsg(String msg) {
					CommOperationAction.this.msg = msg;
					
				}
			}.run();
	        
		}
		catch(Exception e){
			
		}
		finally{
			
		}
	}
	
	
	
	
	  /*
     * 上传文件入口,具体处理通过 showtableConfigId 
     */
  /*  public 	String uploadFile()
    {
    	
    	return new ActionTemplateExecuter() {
			
			@Override
			protected String execute() throws Exception {
				String jsondata = request.getParameter("data");
				
				//int i =commOperationService.addRecord(jsondata);
				Map<String, Object> data = jsondata==null? new HashMap<String, Object>():JsonUtil.getMap4Json(jsondata);

				data.put("SESSION", session);
				
				data.put("session_user", findSessionUser());
				//将webroot路径传入
				
				data.put("WebRoot_path", application.getRealPath("/"));
				
				MapParaQueryConditionDto<String, Object> para=new MapParaQueryConditionDto<String, Object>();
				para.put("files", files);
				para.put("fileNames", fileNames);
				para.put("contentTypes", contentTypes);
				para.setMapParas(data);
				
				commOperationService.queryTableQueryResult(showtableConfigId, para);
				ok = true;
				msg="处理成功";
				return SUCCESS;
				
			}
			
			@Override
			protected void assignMsg(String msg) {
				// TODO Auto-generated method stub
				CommOperationAction.this.msg = msg;
			}
		}.run();
		
    }
    */
	
	

	public boolean isOk() {
		return ok;
	}


	public void setOk(boolean ok) {
		this.ok = ok;
	}


	public String getMsg() {
		return msg;
	}


	public void setMsg(String msg) {
		this.msg = msg;
	}

	public long getShowtableConfigId() {
		return showtableConfigId;
	}

	public void setShowtableConfigId(long showtableConfigId) {
		this.showtableConfigId = showtableConfigId;
	}

	@JSON(serialize=false)
	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
		
	}

	@Override
	protected Fault getFault() {
		// TODO Auto-generated method stub
		return fault;
	}

	public void setQueryCondition(MapParaQueryConditionDto<String, Object>  queryCondition) {
		this.queryCondition = queryCondition;
	}
	
	public void setFiles(File[] files) {
		this.files = files;
	}

	public void setFilesFileName(String[] filesFileName) {
		this.filesFileName = filesFileName;
	}

	public void setFilesContentType(String[] filesContentType) {
		this.filesContentType = filesContentType;
	}

	public TableQueryResult getQueryResult() {
		return queryResult;
	}

	public void setQueryResult(TableQueryResult queryResult) {
		this.queryResult = queryResult;
	}

	
	
	
}
