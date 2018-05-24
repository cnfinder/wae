package cn.finder.wae.queryer.handleclass.excel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import jxl.Sheet;

import org.apache.commons.lang.StringUtils;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.ShowDataConfig;
import cn.finder.wae.business.domain.ShowTableConfig;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.business.module.common.service.QueryService;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.common.comm.AppApplicationContextUtil;
import cn.finder.wae.common.comm.JXLExcel;
import cn.finder.wae.queryer.handleclass.QueryerDBAfterClass;

/**
 * @author: jcl
 * @data:2014-11-13
 * @function:通用EXCEL对数据更新, 文件名格式    表名称-时间   .  解析到对应 表名称   .如 : ypml-20141114.xls
 *   支持同时处理多个EXCEL文件
 *   注意： 主键字段 是EXCEl的第一列   ,只支持一张图片
 *     列配置 和数据库字段一一对应
 */
public class DataUpdateExcelNoIdNoImageAfterClass extends QueryerDBAfterClass {
	
	QueryService queryService=AppApplicationContextUtil.getContext().getBean("queryService", QueryService.class);
	@Override
	public TableQueryResult handle(TableQueryResult tableQueryResult,
			long showTableConfigId, QueryCondition<Object[]> condition) {
		 super.handle(tableQueryResult, showTableConfigId, condition);
		 
		 
		 logger.debug(" ========DataUpdateExcelAfterClass");
		 
		 ShowTableConfig showTableConfig = ArchCache.getInstance().getShowTableConfigCache().get(showTableConfigId);
		 List<ShowDataConfig> showDataConfigs =showTableConfig.getShowDataConfigs();
		 
		 @SuppressWarnings("unchecked")
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
			
		 
		 
	 	//流程实例ID
		 File[]  files =(File[])data.get("files");
		 String[]  filesFileName =(String[])data.get("filesFileName");
		 String[]  filesContentType =(String[])data.get("filesContentType");
		 
		 if(files!=null || files.length>0){
		
		 
			 for(int i=0;i<files.length;i++)
			 {
				
		 	 
				 File file = files[i]; //文件信息
				 String fileName = filesFileName[i];//文件名
				 
				 String tableName = fileName.split("-")[0]; //表名
				 
			 	
			 	 FileInputStream fis=null; 
			 	 
				try {
					fis = new FileInputStream(file);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 	 
			 	 ByteArrayOutputStream outStream=new ByteArrayOutputStream();
			 	 
			 	 JXLExcel jxlExcel = new JXLExcel(fis, outStream);
			 	 
			 	Sheet sheet = jxlExcel.getSheet();
			 	
			 	int cols =sheet.getColumns();
			 	
			 	int rows = sheet.getRows();
			 	
			 	
			 	
			 	//保存和EXCEL 列一致的 表配置
			 	/*List<ShowDataConfig> excelShowDataConfigs = new ArrayList<ShowDataConfig>();
			 	
				//主键 字段
			 	String pkName =ArchCache.getInstance().getShowTableConfigCache().getPKShowDataConfig(showTableConfigId).getFieldName();
		 		
		 		//主键字段在EXCEL 列中 某列
		 		int pk_index=0;
		 		
		 		
			 	//标题
			 	int title_row = 0;
		 		for(int c=0;c<cols;c++){
		 			String title = jxlExcel.findCellValueString(c,title_row);
		 			
		 			if(pkName.equalsIgnoreCase(title)){
						pk_index = c;
					}
			 		
		 			for(ShowDataConfig sdc:showDataConfigs){
		 				
		 				
		 				
		 				if(title.equalsIgnoreCase(sdc.getFieldNameAlias())){
		 					excelShowDataConfigs.add(sdc);
		 					break;
		 				}
		 			}
			 	}*/
		 		
		 		
		 	
		 		
		 		
		 		//字段集合
		 		List<String> fieldNames =new ArrayList<String>();
		 		
		 		//字段逗号分开  id,spm
		 		String sql_fieldNameStr ="";
		 		//字段 字符串
		 		for(int t=0;t<cols;t++)
		 	    {
		 			fieldNames.add(jxlExcel.findCellValueString(t, 0));
		 	    }
		 	    
		 		sql_fieldNameStr = StringUtils.join(fieldNames, ',');
		 		
		 		
			 
		 		
		 		/*for(int t=0;t<cols;t++){
		 			String fieldName = jxlExcel.findCellValueString(t, 0);
		 			
		 			if(t!=0){
		 				where_sb.append("  and  ");
		 			}
		 			
		 			where_sb.append(" ").append(fieldName).append("=").append(" ? ");
		 			
		 		}*/
		 		
		 		
		 		
		 		
		 		
		 		boolean isFirst=true;
		 	
		 		
		 		
		 		// 插入操作 ? 占位符
		 		StringBuffer insert_param_flag_sb = new StringBuffer();
		 		
		 		for(int t=0;t<cols;t++)
		 	    {
	 				if(!isFirst){
	 					insert_param_flag_sb.append(" , ");
	 	 			}
	 				isFirst=false;
	 				insert_param_flag_sb.append(" ? ");
		 	    }
		 		
			 	
				String db_item_insert = " insert into "+tableName+ "("+sql_fieldNameStr+") values ("+insert_param_flag_sb.toString()+")";
				
				//一条一条记录处理
			 	for(int r=1;r<rows;r++){
			 		
			 		//存储一条记录
			 		List<Object> values = new ArrayList<Object>();  //存放有主键的集合
			 		
			 		
			 		for(int c=0;c<cols;c++){
				 		//String v = jxlExcel.findCellValueString(c, r);
			 			
			 			Object v = jxlExcel.findCellValueNoImage(c, r);
				 		values.add(v);
			 			
			 			
				 	}
		 			
		 				//插入数据
		 			getJdbcTemplate().update(db_item_insert, values.toArray());
		 		
		 			
			 	}
			 	 
			 	 
		 	 
			 }
		 	 
		 	 
	 	 
		 
		 }
	 	 
	 	 
       List<Map<String,Object>> list = new java.util.ArrayList<Map<String,Object>>();
       
       tableQueryResult.setCount(1l);
       tableQueryResult.setPageSize(1);
       tableQueryResult.setPageIndex(1);
       tableQueryResult.setResultList(list);
	       
		 
		 
		 return tableQueryResult;
		 
	}

	@Override
	public void setJDBCDataSource(DataSource dataSource) {
		super.setJdbcDataSource(dataSource);
	}

	
	
}
