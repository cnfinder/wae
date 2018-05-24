package cn.finder.wae.controller.action.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import javax.servlet.ServletOutputStream;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.json.annotations.JSON;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.Menu;
import cn.finder.wae.business.domain.ShowDataConfig;
import cn.finder.wae.business.domain.ShowTableConfig;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.domain.UserSetting;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.business.dto.StringParaQueryConditionDto;
import cn.finder.wae.business.module.common.service.CommonService;
import cn.finder.wae.business.module.common.service.QueryService;
import cn.finder.wae.business.module.sys.service.SysService;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.cache.ConstantsCache;
import cn.finder.wae.cache.SysConfigCache;
import cn.finder.wae.common.base.BaseActionSupport;
import cn.finder.wae.common.comm.Common;
import cn.finder.wae.common.comm.ExcelUtil;
import cn.finder.wae.common.comm.JsonUtil;




/***
 * 通用的表格数据显示 
 * @author wu hualong
 *
 */
public class CommonShowDataAction extends BaseActionSupport{

	private static Logger logger=Logger.getLogger(CommonShowDataAction.class); 
	/**
	 * 
	 */
	private static final long serialVersionUID = -1474577676853164998L;

	/** 参数
	 * t_showtable_config 的 id
	 */
	private long showtableConfigId;
	
	private QueryService queryService;
	
	private CommonService commonService;
	
	private SysService sysService;
	
	
	private TableQueryResult queryResult;
	
	private int pageIndex=1;
	
	private int pageSize=0;
	
	private MapParaQueryConditionDto<String, Object> queryCondition=new MapParaQueryConditionDto<String, Object>();
	
	
	private boolean ok=false;
	
	private String msg;
	
	
	
	private InputStream downLoadInputStream;
	
	private String exportFileName;
	
	private String includeProperties;
	
	private String excludeProperties;
	
	
	private InputStream inStream;
	private String contentType;
	
	public CommonShowDataAction(){
		
		queryCondition.getForwardParams().put("servletContext", ServletActionContext.getServletContext());
		
		
		
	}
	
	/**
	 * 显示数据界面 
	 * @return
	 * 
	 */
	public String showDataPage()
	{
		return retureName(showtableConfigId);
	}
	
	/***
	 * 处理返回 action的 result name,和struts配置文件对应
	 * @param showtableConfigId
	 * @return
	 */
	private String retureName(long showtableConfigId)
	{
		 String result=ArchCache.getInstance().getShowTableConfigCache().get(showtableConfigId).getResultName();
		 if(StringUtils.isEmpty(result))
		 {
			 return SUCCESS;
		 }
		 else{
			 return result;
		 }
	}
	
	/**
	 * 处理where条件
	 */
	private void processWhereCondition()
	{
		long searchShowTableConfigId =-1;
		
		try{
			searchShowTableConfigId= Long.valueOf(request.getParameter("searchShowTableConfigId"));
		}
		catch(Exception e)
		{
			searchShowTableConfigId = showtableConfigId; //当前列表的表配置
		}
		
		if(pageSize==0){
			pageSize=ArchCache.getInstance().getShowTableConfigCache().get(searchShowTableConfigId).getPageSize();
			if(pageSize==0){
				pageSize=10;
			}
		}
		
		String whereCondition = request.getParameter("whereCondition");
		if(StringUtils.isEmpty(whereCondition)){
			//以前版本是从  whereCondition中区获取 ，新可以同时到 data 中获取
			whereCondition = request.getParameter("data");
		}
		logger.info("通用搜索参数:"+whereCondition);
		
		Map<String,Object> dataMap = null;
		if(!StringUtils.isEmpty(whereCondition))
		{
			dataMap=JsonUtil.getMap4Json(whereCondition);
			queryCondition.setMapParas(dataMap);
			StringBuffer sb = new StringBuffer();
			if(dataMap!=null){
				//设置 whereCluster 和 wherepParameterValues
				
				//List<ShowDataConfig>  showDataConfigs = ArchCache.getInstance().getShowTableConfigCache().getSearchShowDataConfigs(showtableConfigId);
				List<ShowDataConfig>  showDataConfigs = ArchCache.getInstance().getShowTableConfigCache().getSearchShowDataConfigs(searchShowTableConfigId);
				
				Set<Entry<String, Object>> set_entry =dataMap.entrySet();
				Iterator<Entry<String, Object>> ite=set_entry.iterator();
				
				//保存 wherepParameterValues
				List<Object> whereValues = new ArrayList<Object>();
				
				sb.append(" 1=1 ");
				
				while(ite.hasNext())
				{
					Entry<String,Object> entry = ite.next();
					String name = entry.getKey();
					Object value = entry.getValue();
					
					int dataType=ConstantsCache.DataType.DATATYPE_STRING;
					
					ShowDataConfig  showDataConfig = null;
					
					boolean isExistShowDataConfig=false;
					for(ShowDataConfig sdf:showDataConfigs)
					{
						if(sdf.getFieldName().equals(name))
						{
							dataType = sdf.getDataType();
							showDataConfig= sdf;
							isExistShowDataConfig=true;
							break;
						}
					}
					
					if(!isExistShowDataConfig){
						continue;
					}
					
					if(dataType!=ConstantsCache.DataType.DATATYPE_DATE && showDataConfig.getShowType() !=ConstantsCache.ControlType.CONTROLTYPE_NUMBER_BETWEEN)
					{
						if(StringUtils.isEmpty(""+value))
						{
							continue;
						}
					}
					
					
					
					
					if(showDataConfig.getShowType() ==ConstantsCache.ControlType.CONTROLTYPE_NOTEQUAL_OPERATION){
						// 不等于 操作
						sb.append(" and ");
						sb.append(name).append(" <> ").append("?");
						whereValues.add(value);
						continue;
					}
					else if(showDataConfig.getShowType() ==ConstantsCache.ControlType.CONTROLTYPE_EQUAL_OPERATION){
						// 不等于 操作
						sb.append(" and ");
						sb.append(name).append(" = ").append("?");
						whereValues.add(value);
						continue;
					}
					else if(showDataConfig.getShowType() ==ConstantsCache.ControlType.CONTROLTYPE_IN_OPERATION){
						// in 操作
						sb.append(" and ");
						sb.append(name).append(" in ").append("(");
						
						String[] values = StringUtils.split((String)value,',');
						
						StringBuffer sb_v= new StringBuffer();
						
						for(String str:values){
							sb_v.append(",").append("'"+str+"'");
						}
						sb_v.deleteCharAt(0);
						
						sb.append(sb_v.toString());
						sb.append(")");
						
						//whereValues.add(value);
						continue;
					}
					else if(showDataConfig.getShowType() ==ConstantsCache.ControlType.CONTROLTYPE_NOTIN_OPERATION){
						// not in 操作
						sb.append(" and ");
						sb.append(name).append(" not in ").append("(");
						
						String[] values = StringUtils.split((String)value,',');
						
						StringBuffer sb_v= new StringBuffer();
						
						for(String str:values){
							sb_v.append(",").append("'"+str+"'");
						}
						sb_v.deleteCharAt(0);
						
						sb.append(sb_v.toString());
						sb.append(")");
						continue;
					}
					else if(showDataConfig.getShowType() ==ConstantsCache.ControlType.CONTROLTYPE_GREATTHEN_OPERATION){
						// 不等于 操作
						sb.append(" and ");
						sb.append(name).append(" >= ").append("?");
						whereValues.add(value);
						continue;
					}
					else if(showDataConfig.getShowType() ==ConstantsCache.ControlType.CONTROLTYPE_LESSTHAN_OPERATION){
						// 不等于 操作
						sb.append(" and ");
						sb.append(name).append(" <= ").append("?");
						whereValues.add(value);
						continue;
					}
					else if(showDataConfig.getShowType() ==ConstantsCache.ControlType.CONTROLTYPE_ONEDATE){
						// 单个日期控件
						sb.append(" and ");
						sb.append(name).append(" = ").append("?");
						
						if(dataType==ConstantsCache.DataType.DATATYPE_STRING){
						    Date da=Common.parseDate((String)value);
						    
						    value=  Common.formatDate(da, showDataConfig.getFormat());
						}
						
						whereValues.add(value);
						continue;
					}
					
					
					//根据 dataType 判断数据类型
					
					switch(dataType)
					{
						case ConstantsCache.DataType.DATATYPE_STRING:
							//字符串 做 like 
							
							sb.append(" and ");
							sb.append(name).append(" = ? ");
							whereValues.add(StringUtils.trim((String)value));
							break;
							
						
						case ConstantsCache.DataType.DATATYPE_NUMBER:
						case ConstantsCache.DataType.DATATYPE_FLOAT:
					//	case 6:
							//数字类型   =
							//如果是 showType为区间显示，那么使用 <= 否则  >=
							if(showDataConfig.getShowType() ==ConstantsCache.ControlType.CONTROLTYPE_NUMBER_BETWEEN)
							{
								if(!StringUtils.isEmpty((String)value))
								{
									sb.append(" and ");
									sb.append(name).append(" >= ? ");
									whereValues.add(value);
								}
								
								
								ite.hasNext();
								entry = ite.next();
								value = entry.getValue();
								
								if(!StringUtils.isEmpty((String)value))
								{
									sb.append(" and ");
									sb.append(name).append(" <= ? ");
									whereValues.add(value);
								}
								
							}
							
							
						
							
							else{
								sb.append(" and ");
								sb.append(name).append("=?");
								whereValues.add(value);
								
							}
							
							break;
						
							
						case ConstantsCache.DataType.DATATYPE_DATE:
							//日期   between and 
							
							Date startDate = Common.parseDate3((String)value);
							if(startDate!=null)
							{
								sb.append(" and ");
								sb.append(name).append(" >= ?");
								whereValues.add(startDate);
							}
							
							
							ite.hasNext();
							entry = ite.next();
							value = entry.getValue();
							
							Date endDate = Common.parseDate3((String)value);
							if(endDate!=null)
							{
								sb.append(" and ");
								sb.append(name).append(" <= ?");
								whereValues.add(endDate);
							}
							
							
							break;
						
					}
					
					
					
					
				}
				
				logger.info("通用搜索WhereSQL:"+sb.toString());
				queryCondition.setWhereCluster(sb.toString());

				List<Object> whereP =new ArrayList<Object>();
				//添加参数值
				whereP.addAll(Arrays.asList(whereValues.toArray()));
				
				
				if(queryCondition.getWherepParameterValues()!=null){
					//添加 地址参数值
					whereP.addAll(Arrays.asList(queryCondition.getWherepParameterValues()));
				}
				

				//queryCondition.setWherepParameterValues(whereValues.toArray());
				queryCondition.setWherepParameterValues(whereP.toArray());
			}
		}
	}
	
	/****
	 * 处理参数
	 */
	private void processParameter()
	{
		processWhereCondition();
		
		String sortField = request.getParameter("sortField");
		String sortDirect = request.getParameter("sortDirect");
		LinkedHashMap<String, String> sortMap = new LinkedHashMap<String, String>();
		if(!StringUtils.isEmpty(sortField) && !StringUtils.isEmpty(sortDirect)){
			sortMap.put(sortField, sortDirect);
		}
		
		
		queryCondition.setOrderBy(sortMap);
		
		
		
		queryCondition.setPageIndex(pageIndex);
		queryCondition.setPageSize(pageSize);
	}
	
	
	/**
	 * 这个是显示
	 * @return
	 */
	public String queryData()
	{
		
		Set<Entry<String, Object>>  setEntry= request.getParameterMap().entrySet();
		   
	   Iterator<Entry<String, Object>> iteEntry = setEntry.iterator();
	   
	   while(iteEntry.hasNext()){
		   
		   Entry<String,Object> entry = iteEntry.next();
		   
		   queryCondition.getForwardParams().put(entry.getKey(),entry.getValue());
		   
	   }
		
		processParameter();
		
		
		
		queryCondition.getMapParas().put("session_user", findSessionUser());
		//将webroot路径传入
		
		queryCondition.getMapParas().put("WebRoot_path", application.getRealPath("/"));
		queryCondition.getForwardParams().put("WebRoot_path",application.getRealPath("/"));
		
		String req_url=request.getRequestURL().toString();
		String queryString=request.getQueryString();
		if(!StringUtils.isEmpty(queryString)){
			req_url+="?"+request.getQueryString();
		}
		
		
		queryCondition.getForwardParams().put("wae_request_url",req_url);

		queryResult=queryService.queryTableQueryResult(showtableConfigId, queryCondition);
		
		return retureName(showtableConfigId);
	}
	
	/***
	 * 返回JSON数据格式
	 * @return
	 */
	public void queryDataJson()
	{
		/*
		try
		{
			queryData();
			ok = true;
		}
		catch(Exception e)
		{
			ok =false;
			msg = e.toString();
		}
		return SUCCESS;*/
		
		/*return new ActionTemplateExecuter() {

			@Override
			protected String execute() throws Exception {
				queryData();
				ok = true;
				return SUCCESS;
				
			}
		}.run();*/
		
		new ActionTemplateExecuter() {
			
			@Override
			protected String execute() throws Exception {
				queryData();
				ok = true;
				return SUCCESS;
			}

			@Override
			protected void assignMsg(String msg) {
				// TODO Auto-generated method stub
				CommonShowDataAction.this.msg = msg;
			}
		}.responseJsonString(this,excludeProperties, includeProperties);
		
		
	}
	
	public void queryDataXml()
	{
		
		new ActionTemplateExecuter() {
			
			@Override
			protected String execute() throws Exception {
				queryData();
				ok = true;
				return SUCCESS;
			}

			@Override
			protected void assignMsg(String msg) {
				// TODO Auto-generated method stub
				CommonShowDataAction.this.msg = msg;
			}
		}.responseJsonString(this,excludeProperties, includeProperties);
		
	}
	
	
	/***
	 * 查询浏数据类型
	 * @return
	 */
	public String streamData(){
		
		processParameter();
		queryResult=queryService.queryTableQueryResult(showtableConfigId, queryCondition);
		
		 if(queryResult!=null){
			    
				 List<Map<String, Object>> rlist = queryResult.getResultList();
				//byte[] imgData =null; //图片流字节;
				if(rlist!=null && rlist.size()>0){
					Map<String, Object> item = rlist.get(0);
					Object binaryData = item.get("binary_data"); //获取图片 
					
					if(binaryData==null){
						
						logger.info("===没有数据");
					
						try {
							//response.getOutputStream().write("没有数据".getBytes(), 0, 0);
							String path = application.getRealPath("images")+File.separator+"defaultImg.jpg";
							inStream=new FileInputStream(new File(path));
							
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else{
						logger.info(" binary data type:"+binaryData.getClass());
	 					final byte[] imgData= (byte[])binaryData;
	 					String fileExt = (String)item.get("file_type_ext");  //文件 扩展名
	 					if(StringUtils.isEmpty(fileExt)){
	 						fileExt ="jpg";
	 					}
	 					
	 					String contentType = "application/octet-stream";
	 					if(StringUtils.isEmpty(fileExt)){
	 						fileExt ="jpg";
	 						contentType="image/jpeg";
	 					}
	 					
	 					//ByteArrayInputStream instream =new ByteArrayInputStream(sms_decrypt(imgData));
	 				/*	ByteArrayInputStream instream =new ByteArrayInputStream(imgData);
	 					
	 					byte[] b = new byte[1024];
	 				    int len = -1;
	 				   ServletOutputStream outStream=null;
					try {
						outStream = response.getOutputStream();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
	 				   
	 				   
	 				    while ((len = instream.read(b, 0, 1024)) != -1) {
	 				    	
	 				    	
	 				       try {
	 				    	  outStream.write(b, 0, len);
		 					} catch (IOException e) {
		 						e.printStackTrace();
		 					}
	 				    }
	 				    try
	 				    {
	 				    	outStream.flush();
	 				    	outStream.close();
	 				    	if(instream!=null)
	 				    		instream.close();
	 				    }
	 				    catch(Exception e){
	 				    	
	 				    }
	 				    finally{
	 				    	
	 				    }
	 		*/
	 					
	 					
	 					inStream = new ByteArrayInputStream(imgData);
	 					
	 				    this.contentType = contentType;
	 				}
				}
		    }
		    
		
		
		return SUCCESS;
		
	}
	
	
	
	/**
	 * 加载 通用搜索
	 * 参数 showtableConfigId
	 * @return
	 */
	public String loadSearchPageJson()
	{
		try
		{
			ok = true;
		}
		catch(Exception e)
		{
			ok =false;
			msg = e.toString();
		}
		return SUCCESS;
	}
	
	/**
	 * 加载  各个操作 
	 * 包括 工具栏  条目前面   条目后面菜单  
	 * 参数: showtableConfigId
	 * @return
	 */
	public String loadOperationsJson(){
		
		//showtableConfigId
		
		try
		{
			ok = true;
		}
		catch(Exception e)
		{
			ok =false;
			msg = e.toString();
		}
		return SUCCESS;
	}
	
	
	
	
	/**
	 * 加载工具栏
	 */
	public void loadGridToolbarJson()
	{
		
		new TemplateExecuter<Object>() {

			@Override
			protected Object execute() throws Exception {
				return  ArchCache.getInstance().getShowTableConfigCache().get(showtableConfigId).getToolBarMenus();
			}
			@Override
			protected void assignMsg(String msg) {
				CommonShowDataAction.this.msg = msg;
				
			}
		}.run(excludeProperties,includeProperties);
	}
	
	/**
	 * 加载上下文菜单
	 */
	public void loadGridContextMenusJson()
	{
		
		new TemplateExecuter<Object>() {

			@Override
			protected Object execute() throws Exception {
				return  ArchCache.getInstance().getShowTableConfigCache().get(showtableConfigId).getContextMenus();
			}
			@Override
			protected void assignMsg(String msg) {
				CommonShowDataAction.this.msg = msg;
				
			}
		}.run(excludeProperties,includeProperties);
	}
	
	

	public void loadShowtableConfig()
	{
		new TemplateExecuter<Object>() {

			@Override
			protected Object execute() throws Exception {
				return  ArchCache.getInstance().getShowTableConfigCache().get(showtableConfigId);
			}
			@Override
			protected void assignMsg(String msg) {
				CommonShowDataAction.this.msg = msg;
				
			}
		}.run(excludeProperties,includeProperties);
	}
	

	/**
	 * 查询当前用户下所有菜单
	 */
	public void loadSessionMenus()
	{
		new TemplateExecuter<Object>() {

			@Override
			protected Object execute() throws Exception {
				return  findSessionUser().getRole().getMenus();
			}
			@Override
			protected void assignMsg(String msg) {
				CommonShowDataAction.this.msg = msg;
				
			}
		}.run(excludeProperties,includeProperties);
	}
	
	
	/**
	 * 查询当前用户所在角色下所有菜单
	 */
	public void loadMgrSessionMenus()
	{
		new TemplateExecuter<Object>() {

			@Override
			protected Object execute() throws Exception {
				
				//角色下的菜单权限
				List<Menu> menus = findSessionUser().getRole().getMenus();
				if(menus!=null && menus.size()>0){
					for(Menu m:menus){
						if(m.getLevel()==ArchCache.getInstance().getSysConfigCache().get(SysConfigCache.KEY_SYSCONFIG_CONFIG_CONFIG_MGR_MENU_LEVEL).getIntValue()){
							List<Menu> mgrMenus= m.getChildren();
							
							return mgrMenus;
						}
						
					}
				}
				
				
				return null;
			}
			@Override
			protected void assignMsg(String msg) {
				CommonShowDataAction.this.msg = msg;
				
			}
		}.run(excludeProperties,includeProperties);
	}
	/**
	 * 查询当前用户下所有菜单
	 */
	public void loadMgrUserMenus()
	{
		new TemplateExecuter<Object>() {

			@Override
			protected Object execute() throws Exception {
				
				//角色下的菜单权限
				List<Menu> menus = sysService.findUserMgrMenus(findSessionUser());
				if(menus!=null && menus.size()>0){
					for(Menu m:menus){
						if(m.getLevel()==ArchCache.getInstance().getSysConfigCache().get(SysConfigCache.KEY_SYSCONFIG_CONFIG_CONFIG_MGR_MENU_LEVEL).getIntValue()){
							List<Menu> mgrMenus= m.getChildren();
							
							/*//同时加入私有菜单权限
							List<Menu> privilegeMenus=sysService.findPrivilegeMenusByUserId(findSessionUser().getAccount());
							
							menusMerger(mgrMenus,privilegeMenus);
							*/
							return mgrMenus;
						}
						
					}
				}
				
				
				/*//角色菜单没有 试着返回 私有菜单权限
				List<Menu> privilegeMenus=sysService.findPrivilegeMenusByUserId(findSessionUser().getAccount());
				if(privilegeMenus!=null && privilegeMenus.size()>0){
					for(Menu m:privilegeMenus){
						if(m.getLevel()==ArchCache.getInstance().getSysConfigCache().get(SysConfigCache.KEY_SYSCONFIG_CONFIG_CONFIG_MGR_MENU_LEVEL).getIntValue()){
							return m.getChildren();
						}
					}
				}*/
				
				
				return null;
			}
			@Override
			protected void assignMsg(String msg) {
				CommonShowDataAction.this.msg = msg;
				
			}
		}.run(excludeProperties,includeProperties);
	}
	
	
	/***
	 * 权限合并
	 * @param mgrMenus 角色菜单权限
	 * @param privilegeMenus 私有权限
	 */
	@Deprecated
	private void menusMerger(List<Menu> mgrMenus,List<Menu> privilegeMenus){
		
		
		if(privilegeMenus!=null && privilegeMenus.size()>0){
			
			for(Menu priMenu:privilegeMenus){
				
				boolean isExistRole=false;
				//判断当前的私有权限的菜单是否在角色权限中存在， 如果存在就查询该节点的子节点
				for(Menu mgrMenu:mgrMenus){
					if(mgrMenu.getId()==priMenu.getId()){
						//存在于角色菜单,继续找子节点
						List<Menu> priChilds=priMenu.getChildren();
						menusMerger(mgrMenus,priChilds);
						isExistRole=true;
						break;
					}
				}
				
				//不存在,挂在角色菜单父节点下面，如果父节点不存在就加入到mgrMenus
				if(!isExistRole){
					
					Menu parentMenu=findMenuParentFromRoleMenu(mgrMenus,priMenu.getParentId());
					
					if(parentMenu!=null){
						parentMenu.getChildren().add(priMenu);
						
					}else{
						mgrMenus.add(priMenu);
					}
					
				}
					
			}
			
		}
		
	}
	
	
	/***
	 * 从角色菜单中获取父项菜单
	 * @param mgrMenus
	 * @param menuId
	 * @return
	 */
	@Deprecated
	private Menu findMenuParentFromRoleMenu(List<Menu> mgrMenus,long menuId){
		for(Menu menu:mgrMenus){
			if(menu.getId()==menuId){
				return menu.getParent();
			}
			else{
				if(menu.getChildren()!=null){
					findMenuParentFromRoleMenu(mgrMenus,menu.getId());
				}
			}
		}
		
		return null;
	}
	
	
	
	
	/**
	 * 获取当前用户的个性化设置
	 */
	public void loadUseSetting()
	{
		new TemplateExecuter<UserSetting>() {

			@Override
			protected UserSetting execute() throws Exception {
				return  commonService.findUserSettingByUserName(findSessionUser().getAccount());
				
			}
			@Override
			protected void assignMsg(String msg) {
				CommonShowDataAction.this.msg = msg;
				
			}
		}.run(excludeProperties,includeProperties);
	}
	
	/*
	 * 获取搜索的 showtableConfigId
	 */
	public void findSearchShowtableConfigId()
	{
		new TemplateExecuter<Long>() {

			@Override
			protected Long execute() throws Exception {
				return  ArchCache.getInstance().getShowTableConfigCache().get(showtableConfigId).getSearchShowtableConfigId();
			}
			@Override
			protected void assignMsg(String msg) {
				CommonShowDataAction.this.msg = msg;
				
			}
		}.run(excludeProperties,includeProperties);
	}
	
	/***
	 * 
	 * @author: wuhualong
	 * @data:2014-6-23上午8:40:59
	 * @function:加载二进制数据  如图片、文件等  通过配置showtableConfigId和 binary_data, file_type_ext
	 * @throws Exception
	 */
	public void loadBinaryData() throws Exception{
		
		queryCondition.getMapParas().put("session_user", findSessionUser());
		//将webroot路径传入
		
		queryCondition.getMapParas().put("WebRoot_path", application.getRealPath("/"));
		queryCondition.getForwardParams().put("WebRoot_path",application.getRealPath("/"));
		
		processParameter();
		
		queryResult=queryService.queryTableQueryResult(showtableConfigId, queryCondition);
		 if(queryResult!=null){
			  
			    
			 List<Map<String, Object>> rlist = queryResult.getResultList();
			//byte[] imgData =null; //图片流字节;
			if(rlist!=null && rlist.size()>0){
				Map<String, Object> item = rlist.get(0);
				Object binaryData = item.get("binary_data"); //获取图片 
				
				if(binaryData==null){
					
					logger.info("===没有数据");
				
					response.getOutputStream().write("没有数据".getBytes(), 0, 0);
				}
				else{
					logger.info(" binary data type:"+binaryData.getClass());
 					final byte[] imgData= (byte[])binaryData;
 					String fileExt = (String)item.get("file_type_ext");  //文件 扩展名
 					String fileName = (String)item.get("file_name");  //文件 扩展名
 					String attachment=(String)item.get("attachment");
 					String contentType = "application/octet-stream";
 					if(StringUtils.isEmpty(fileExt)){
 						fileExt ="jpg";
 						contentType="image/jpeg";
 					}
 					
 					if(StringUtils.isEmpty(fileName)){
 						fileName=UUID.randomUUID().toString();
 					}
 					
 					if(StringUtils.isEmpty(attachment)){
 						attachment="0";
 					}
 					
 					response.setContentType(contentType);
 					//如果文件名是中文，要经过URL编码
 					if("1".equals(attachment)){
 						String filenamefull=URLEncoder.encode(fileName+"."+fileExt,"UTF-8");
 						response.setHeader("content-disposition","attachment;filename="+filenamefull);
 					}
 					
 					ByteArrayInputStream instream =new ByteArrayInputStream(imgData);
 					
 					byte[] b = new byte[1024];
 				    int len = -1;
 				    
 				   ServletOutputStream outStream= response.getOutputStream();
 				    while ((len = instream.read(b, 0, 1024)) != -1) {
 				    	
 				    	
 				       try {
 				    	  outStream.write(b, 0, len);
	 					} catch (IOException e) {
	 						// TODO Auto-generated catch block
	 						e.printStackTrace();
	 					}
 				    }
 				    try
 				    {
 				    	outStream.flush();
 				    	outStream.close();
 				    	if(instream!=null)
 				    		instream.close();
 				    }
 				    finally{
 				    	
 				    }
 				    
				}
			}
		 }
		
	    
	    
	}
	
	
	/***
	 * 导出excel文件
	 * @return
	 */
	public String exportExcel(){
		
		
		
		return new ActionTemplateExecuter() {
			@Override
			protected void assignMsg(String msg) {
				// TODO Auto-generated method stub
				CommonShowDataAction.this.msg = msg;
			}
			@Override
			protected String execute() throws Exception {
				processParameter();
				
				String fields = request.getParameter("fields");
				fields=URLDecoder.decode(fields, "utf-8");
				StringParaQueryConditionDto stringParaQueryConditionDto =new StringParaQueryConditionDto();
				
				BeanUtils.copyProperties(stringParaQueryConditionDto, queryCondition);
				stringParaQueryConditionDto.setStringPara(fields);
				
				
				//查询导出数据
				queryResult =queryService.queryTableQueryResult(showtableConfigId, stringParaQueryConditionDto);
				
				
				//根据excel模板文件生成EXCEL文档
				//文件名称根据 
				ShowTableConfig stc = ArchCache.getInstance().getShowTableConfigCache().get(showtableConfigId);
				String fileName = stc.getName();
				fileName = fileName+"_"+Common.formatDate(new Date(),"yyyy-MM-dd_HH_mm_ss")+".xls";
				
				setExportFileName(URLEncoder.encode(fileName, "UTF-8"));
				
				String templatePath = application.getRealPath(ArchCache.getInstance().getSysConfigCache().get(SysConfigCache.KEY_SYSCONFIG_CONFIG_CONFIG_EXPORT_EXCEL_TEMPLATE_PATH).getValue());
				
				
				List<String>  headerTextList = new ArrayList<String>();
				
				for(ShowDataConfig sdc:queryResult.getFields()){
					headerTextList.add(sdc.getFieldNameAlias());
					
				}
				
				ExcelUtil excelUtil =new ExcelUtil();
				ByteArrayOutputStream byteArrayOutputStream =excelUtil.exportExcel(headerTextList, queryResult.getResultList(), new FileInputStream(new File(templatePath)), stc.getName());
				
				
				byte[] byteDatas=byteArrayOutputStream.toByteArray();
				
				downLoadInputStream = new ByteArrayInputStream(byteDatas);
				
				ok = true;
				return retureName(showtableConfigId);
				
			}
		}.run();
	}
	
	
	
	/***
	 * 不同数据源表数据导入
	 * @return
	 */
	public void tableDataImport(){
		
		new TemplateExecuter<Long>() {

			@Override
			protected Long execute() throws Exception {
				
				long sourceShowtableConfigId = Long.parseLong(request.getParameter("sourceShowtableConfigId"));
				long destShowtableConfigId = Long.parseLong(request.getParameter("destShowtableConfigId"));
				
				MapParaQueryConditionDto<String, Long> paraCondition =new MapParaQueryConditionDto<String, Long>();
				
				BeanUtils.copyProperties(paraCondition, queryCondition);
				
				paraCondition.put("sourceShowtableConfigId",sourceShowtableConfigId);
				paraCondition.put("destShowtableConfigId",destShowtableConfigId);
				paraCondition.setPageSize(pageSize);
				queryService.queryTableQueryResult(showtableConfigId, paraCondition);
				
				return 1l;
			}
			@Override
			protected void assignMsg(String msg) {
				CommonShowDataAction.this.msg = msg;
				
			}
		}.run();
		
	}
	
	
	


	public long getShowtableConfigId() {
		return showtableConfigId;
	}



	public void setShowtableConfigId(long showtableConfigId) {
		this.showtableConfigId = showtableConfigId;
	}



	@JSON(serialize=false)
	public QueryCondition<Object[]> getQueryCondition() {
		return queryCondition;
	}





	public void setQueryCondition(MapParaQueryConditionDto<String, Object> queryCondition) {
		this.queryCondition = queryCondition;
	}



	public TableQueryResult getQueryResult() {
		return queryResult;
	}



	public void setQueryService(QueryService queryService) {
		this.queryService = queryService;
	}


	public int getPageIndex() {
		return pageIndex;
	}


	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}


	public int getPageSize() {
		if(pageSize==0)
			pageSize=10;
		return pageSize;
	}


	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
		
		
	}


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

	@JSON(serialize=false)
	public InputStream getDownLoadInputStream() {
		return downLoadInputStream;
	}

	@JSON(serialize=false)
	public String getExportFileName() {
		return exportFileName;
	}

	public void setExportFileName(String exportFileName) {
		this.exportFileName = exportFileName;
	}
	@JSON(serialize=false)
	public String getIncludeProperties() {
		return includeProperties;
	}

	public void setIncludeProperties(String includeProperties) {
		this.includeProperties = includeProperties;
	}

	@JSON(serialize=false)
	public String getExcludeProperties() {
		return excludeProperties;
	}

	public void setExcludeProperties(String excludeProperties) {
		this.excludeProperties = excludeProperties;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}
	@JSON(serialize=false)
	public InputStream getInStream() {
		return inStream;
	}
	@JSON(serialize=false)
	public String getContentType() {
		return contentType;
	}

	public void setSysService(SysService sysService) {
		this.sysService = sysService;
	}
	
	
	
	
	
}
