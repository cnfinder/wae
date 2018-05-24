package cn.finder.wae.queryer.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hsqldb.lib.StringUtil;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.ShowDataConfig;
import cn.finder.wae.business.domain.ShowTableConfig;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.cache.ConstantsCache;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.cache.SysConfigCache;
import cn.finder.wae.common.aspect.logger.ArchType;
import cn.finder.wae.common.aspect.logger.FinderLogger;
import cn.finder.wae.common.comm.MD5Util;
import cn.finder.wae.common.db.ds.DSManager;

import com.ssh.common.util.FileUtil;


/***
 * 通用编辑 更新 处理类
 * @author whl
 *
 */
public class EditCascadeCommonQueryer extends  BaseCommonDBQueryer{
	Logger log = Logger.getLogger(this.getClass());
	
	
	@FinderLogger(archType=ArchType.FINDER_ARCH)
	@Override
	public TableQueryResult queryTableQueryResult(long showTableConfigId,
			QueryCondition<Object[]> condition){
		

		logger.debug("=====go into EditCascadeCommonQueryer======");
		
		logger.debug("==showTableConfigId:"+showTableConfigId);
		
		
		TableQueryResult qr =new TableQueryResult();
		qr.getMessage().setMsg("更新失败成功");
		
		@SuppressWarnings("unchecked")
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
		Long editConfigId ;
		
		try{
			editConfigId = Long.valueOf(String.valueOf(data.get("showtableConfigId")));
		}
		catch(Exception e){
			editConfigId=showTableConfigId;
		}
		
		
		ShowTableConfig showTableConfig = ArchCache.getInstance().getShowTableConfigCache().get(editConfigId);
		if(condition.getPageSize()==0){
	    	//默认为数据库设置大小
	    	condition.setPageSize(showTableConfig.getPageSize()==0?10:showTableConfig.getPageSize());
	    }
		if(condition!=null)
		{
			logger.debug("==Sql:"+condition.getSql());
			logger.debug("==WhereCluster:"+condition.getWhereCluster());
			logger.debug("==WherepParameterValues:"+condition.getWherepParameterValues());
			logger.debug("==pageIndex:"+condition.getPageIndex());
			logger.debug("==pageSize:"+condition.getPageSize());
		}
		
		
		
		List<ShowDataConfig> showDataConfigs = showTableConfig.getShowDataConfigs();
		
		DataSource ds = DSManager.getDataSourceMaps().get(showTableConfig.getTargetDs());
		setJdbcDataSource(ds);//set datasource
		
		Map<String, Map<String,Object>> tables = new HashMap<String, Map<String,Object>>();
		for(ShowDataConfig sdc:showDataConfigs){
			String tableName = sdc.getFieldTableName();
			if(!StringUtil.isEmpty(tableName)){
				if(!tables.containsKey(tableName)){    //判断是表是否已经初始化过
					Map<String, Object> table = new HashMap<String, Object>();
					table.put("tableName", tableName);
					table.put("jsondata",data);
					List<ShowDataConfig> fields = new ArrayList<ShowDataConfig>();
					fields.add(sdc);
					table.put("fields", fields);
					tables.put(tableName, table);
				}else{
					Map<String, Object> table = tables.get(tableName);
					@SuppressWarnings("unchecked")
					List<ShowDataConfig> list = (List<ShowDataConfig>)table.get("fields");
					list.add(sdc);
				}
				
			}
		}
		
		Set<String > key = tables.keySet();
		int affect_cnt=0;
		for(Iterator<String > it = key.iterator(); it.hasNext();){
			String tableName = it.next();
			Map<String, Object> map = tables.get(tableName);
			affect_cnt = affect_cnt+singlEditRecord(editConfigId, tableName, map); 
		}
		qr.setPageIndex(1);
		qr.setPageSize(10);
		qr.setCount((long)affect_cnt);
		qr.getMessage().setMsg("更新成功");
		
		
		return qr;
	}
	
	
	public int singlEditRecord(long editConfigId, String tableName, Map<String, Object> map ){
		@SuppressWarnings("unchecked")
		Map<String, Object> data =(Map<String, Object>)map.get("jsondata");
		
		String primaryKey="";
		Object primaryKeyValue = null;
		
		String sql = "" ;
		String valueSql = "";
		List<Object> values = new ArrayList<Object>();
		@SuppressWarnings("unchecked")
		List<ShowDataConfig> showDataConfigs = (List<ShowDataConfig>)map.get("fields");
		
		ShowDataConfig primaryKeyConfig =ArchCache.getInstance().getShowTableConfigCache().getPKShowDataConfig(editConfigId);
		primaryKey = primaryKeyConfig.getFieldName();
		
		
		
		primaryKeyValue = data.get(primaryKey);
		
		if(primaryKeyValue==null){
			primaryKeyValue = data.get(primaryKeyConfig.getFieldNameAlias());
		}
		
		
		for(ShowDataConfig showDataConfig:showDataConfigs){
			String fieldName = showDataConfig.getFieldName();
			String[] strs = fieldName.split("\\.");
			fieldName = strs[strs.length-1];
			Object value = data.get(fieldName);
			

			if(value==null){
				value = data.get(showDataConfig.getFieldNameAlias());
			}
			
			if(showDataConfig.getIsPrimaryKey() == 1){        //判断是否是主键
				primaryKey = fieldName;
				continue;			}
			//多表编辑的时候，每个表的where条件不同，这里只考虑字段不同，值相同的情况，即除了主表，其他表都是使用外键作为where查询条件
			if(showDataConfig.getShowType() == ConstantsCache.ControlType.CONTROLTYPE_RETURN_PRIMARY_KEY){ //外键引用
				primaryKey = fieldName;
				continue;
			}
			
			if(showDataConfig.getShowType() == ConstantsCache.ControlType.CONTROLTYPE_SYSTEM_DATE){
				valueSql += fieldName + " = ?, ";
				values.add(new Timestamp(System.currentTimeMillis()));
				continue;
			}
			//需要二进制存储列类型的
			if(showDataConfig.getShowType() == ConstantsCache.ControlType.CONTROLTYPE_BINARY_FILE){
				
				if(value instanceof byte[]){
					//如果值已经设置了字节数据 ，那么就不需要再次进行设置
					valueSql += fieldName + " = ?, ";
					values.add(value);
					continue;
				}
				else{
					//根据相对路径获取绝对路径+
					if(value == null || value.equals("")){   //如果传过来的图片信息没有值就不做
						continue;
					}
					valueSql += fieldName + " = ?, ";
					String rootPath = data.get("WebRoot_path").toString();
					String filePath = rootPath +value; 
					File file = new File(filePath);
					try {
						FileInputStream input = new FileInputStream(file);
						byte[] b = new byte[input.available()];
						input.read(b);
						value = b;
						values.add(value);
						continue;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
			/**
			 * 1.判断文件路径是否为空
			 * 2.获取当前文件类型
			 * 3.获取主键拼接 主键.类型文件
			 * 4.将文件改名
			 * 5.设置文件路径属性字段值
			*/
			if(showDataConfig.getShowType() == ConstantsCache.ControlType.CONTROLTYPE_FILE_NAMEED_BY_ID){
				Object filePath = data.get(fieldName);
				if(filePath != null && !StringUtil.isEmpty(filePath.toString()) ){
					String rootPath = data.get("WebRoot_path").toString();
					String uploadPath = ArchCache.getInstance().getSysConfigCache().get(SysConfigCache.KEY_CONFIG_DRUG_IMG_PATH).getValue();
					String path = filePath.toString();
					String extension = FileUtil.getSuffixByFileName(path);
					String newName =rootPath+ "/" +uploadPath + "/" + primaryKeyValue + "." + extension;
					
					File file = new File(rootPath+ "/"+path);
					File newFile = new File(newName);
					if(newFile.exists()){
						newFile.delete();  //如果目标文件已经存在，就删除
					}
					
					file.renameTo(newFile);
					valueSql += fieldName + " = ?, ";
					value = uploadPath + "/" +newFile.getName();
					values.add(value);
					
				}
				continue;
			}
			if (value != null && showDataConfig.getShowType() != ConstantsCache.ControlType.CONTROLTYPE_PWD_CONFIRM) { // 33不是需要插入到数据库的类型
				if (value.equals("")) {
					if (showDataConfig.getShowType() != ConstantsCache.ControlType.CONTROLTYPE_PWD) {
						if (showDataConfig.getDataType() != ConstantsCache.DataType.DATATYPE_STRING) {
							if(!StringUtils.isEmpty(showDataConfig.getDefaultValue())){
								value = showDataConfig.getDefaultValue();
							}else{
//								if(showDataConfig.getShowType() == ConstantsCache.ControlType.CONTROLTYPE_DATE){
//									value = null;
//								}
								if(showDataConfig.getShowType() == ConstantsCache.DataType.DATATYPE_NUMBER){
									value = -1;
								} 
								else{
									value = null;
								}
							}
							
						}
						valueSql += fieldName + " = ?, ";
						values.add(value);
					}
					
				}else{
					if(showDataConfig.getShowType() == ConstantsCache.ControlType.CONTROLTYPE_PWD){
						valueSql += fieldName + " = ?, ";
						values.add(MD5Util.getMD5(value.toString()));
					}
					else{
						valueSql += fieldName + " = ?, ";
						values.add(value);
					}
				}				
			}
		}
		
		
		valueSql = valueSql.substring(0,valueSql.lastIndexOf(","));//删除最后一个","
		
		sql = "update " + tableName + " set " +valueSql;
		sql += " where 1=1";
		sql += " and "+ primaryKey + " =?"; 
		values.add(primaryKeyValue);
		
		if(primaryKeyValue == null || StringUtil.isEmpty(String.valueOf(primaryKeyValue))){
			log.error("error:字段所在表配置错误");
			return -4;//主键设置错误！
		}
		
		int iValue= getJdbcTemplate().update(sql, values.toArray());
		return iValue;
	}
	

	public void setJDBCDataSource(DataSource dataSource) {
		super.setJdbcDataSource(dataSource);
	}
	
	public boolean returnKeyHolder(List<ShowDataConfig> showDataConfigs){   //判断是否需要返回主键值
		for(ShowDataConfig showDataConfig : showDataConfigs){
			if(1l == showDataConfig.getShowType()){
				return true;
			}
			
		}
		return false;
	}
}
