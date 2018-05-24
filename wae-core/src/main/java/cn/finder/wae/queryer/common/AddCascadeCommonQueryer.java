package cn.finder.wae.queryer.common;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.sql.DataSource;

import net.sf.json.JSONNull;

import org.apache.commons.lang3.StringUtils;
import org.hsqldb.lib.StringUtil;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.ShowDataConfig;
import cn.finder.wae.business.domain.ShowTableConfig;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.domain.User;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.cache.ConstantsCache;
import cn.finder.wae.cache.SysConfigCache;
import cn.finder.wae.common.aspect.logger.ArchType;
import cn.finder.wae.common.aspect.logger.FinderLogger;
import cn.finder.wae.common.comm.Common;
import cn.finder.wae.common.comm.MD5Util;
import cn.finder.wae.common.db.ds.DSManager;

import com.ssh.common.util.FileUtil;

public class AddCascadeCommonQueryer extends  BaseCommonDBQueryer{
	private static Object foreignKey=null;
	
    private	ShowDataConfig pkShowDataConfig; //主表 主键列 配置
	private String mainTableName =null; //主表
	
	private Map<String, String> map;
	@FinderLogger(archType=ArchType.FINDER_ARCH)
	@Override
	public TableQueryResult queryTableQueryResult(long showTableConfigId,
			QueryCondition<Object[]> condition){
		
		logger.debug("=====go into AddCascadeCommonQueryer======");
		
		logger.debug("==showTableConfigId:"+showTableConfigId);
		
		
		@SuppressWarnings("unchecked")
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
		Long addConfigId;
		Object objshowtableConfigId= data.get("showtableConfigId");
		if(objshowtableConfigId==null){
			addConfigId=showTableConfigId;
		}else{
			addConfigId= Long.valueOf(String.valueOf(objshowtableConfigId));
		}
		ShowTableConfig showTableConfig = ArchCache.getInstance().getShowTableConfigCache().get(addConfigId);
		List<ShowDataConfig> showDataConfigs = showTableConfig.getShowDataConfigs();
		
		DataSource ds = DSManager.getDataSourceMaps().get(showTableConfig.getTargetDs());
		setJdbcDataSource(ds);//set datasource
		
		//Map<String, String> tables = new TreeMap<String, String>();
		Map<String, Object> tables = new TreeMap<String, Object>();
		for(ShowDataConfig sdc:showDataConfigs){
			String tableName = sdc.getFieldTableName();
			if(!StringUtil.isEmpty(tableName)){
				if(!tables.containsKey(tableName)){             //
					//String jsonStr = String.valueOf(data.get(tableName));
					
					tables.put(tableName, data.get(tableName));
				}
				
			}
		}
		
		Set<String > key = tables.keySet();
		int i = 0;
		for(Iterator<String > it = key.iterator(); it.hasNext();i++){
			String tableName = it.next();
//			Map<String, Object> map = JsonUtil.getMap4Json(tables.get(tableName));  //暂时不考虑有不同表的列同名
			if(i == 0){
				foreignKey = singleAddRecord(addConfigId, tableName, data); 
				data.put("item_pk", foreignKey);
			}else{
				singleAddRecord(addConfigId, tableName, data); 
			}
			
			
		}
		
		TableQueryResult queryResult = new TableQueryResult();
		queryResult.setCount(0l);
		queryResult.setPageIndex(1);
		queryResult.setPageSize(10);
		
		
		return queryResult;
	}
	
	public Object singleAddRecord(long showTableConfigId,String tableName, Map<String, Object> data){
		//主键列配置
		if(pkShowDataConfig==null)
			pkShowDataConfig =  ArchCache.getInstance().getShowTableConfigCache().getPKShowDataConfig(showTableConfigId);
		
		ShowTableConfig showTableConfig = ArchCache.getInstance().getShowTableConfigCache().get(showTableConfigId);
		String sql = "insert into " + tableName + "(";
		String valueSql = "";
		final List<Object> values = new ArrayList<Object>();
		List<ShowDataConfig> showDataConfigs = showTableConfig.getShowDataConfigs();
		
		//
		
		for(ShowDataConfig showDataConfig:showDataConfigs){
			if(!showDataConfig.getFieldTableName().equals(tableName)){   //排除不是表tableName的字段
				continue;
			}
			String fieldName = showDataConfig.getFieldName();
			String[] strs = fieldName.split("\\.");                     //删除表名前缀
			fieldName = strs[strs.length-1];
			Object value = data.get(fieldName);
			
			if(value==null){
				//接口部分是通用别名传递过来
				value = data.get(showDataConfig.getFieldNameAlias());
			}
			//updated by dragon  2015-04-21
			/*if(showDataConfig.getDataType() ==ConstantsCache.DataType.DATATYPE_STRING){
				if(value!=null){
					value =value.toString();//防止数据类型和数据库不一致 抛出 不支持从 UNKNOWN 到 UNKNOWN 的转换
				}
			}*/
			if(JSONNull.getInstance().equals(value)){
				value =null;
			}
			
			//系统日期类型控件，赋于系统当前日期
			if(showDataConfig.getShowType() == ConstantsCache.ControlType.CONTROLTYPE_SYSTEM_DATE){
//				sql += fieldName+", ";
//				valueSql += " ?, " ;
//				values.add(new Timestamp(System.currentTimeMillis()));
//				continue;
				value = (new Timestamp(System.currentTimeMillis()));
//				value = (new Date());
				sql += fieldName+", ";
				valueSql += " ?, " ;
				values.add(value);
			}
			
			else if(showDataConfig.getShowType() == ConstantsCache.ControlType.CONTROLTYPE_DATE && value!=null &&!(value instanceof java.util.Date)){
				//日期类型
				if(!StringUtils.isEmpty(value.toString())){
					
					String valueStr = value.toString();
					valueStr = valueStr.replace("T", " ");
				
					Date item_date=null;
					//Timestamp t_value = new Timestamp(Common.parseDateFull(valueStr).getTime());
					if(!StringUtils.isEmpty(showDataConfig.getFormat())){
						item_date=Common.parseDate(valueStr,showDataConfig.getFormat());
					}else{
						item_date=Common.parseDateFull(valueStr);
					}
					Timestamp t_value = new Timestamp(item_date.getTime());
					
					value= t_value;
					sql += fieldName+", ";
					valueSql += " ?, " ;
					values.add(t_value);
				}
			}
			//密码控件，md5加密
			else if(showDataConfig.getShowType() == ConstantsCache.ControlType.CONTROLTYPE_PWD){
				sql += fieldName+", ";
				valueSql += " ?, " ;
				values.add(MD5Util.getMD5(value.toString()));
			}
				
			else if(showDataConfig.getShowType() ==  ConstantsCache.ControlType.CONOTROLTYPE_SHOW_SELECT) continue; //只显示用的下拉框	
			
			else if(showDataConfig.getShowType() == ConstantsCache.ControlType.CONTROLTYPE_RETURN_PRIMARY_KEY){
				if(foreignKey!=null){
//					sql += fieldName+", ";
//					valueSql += " ?, " ;
//					values.add(foreignKey);
					value = foreignKey;
					
					sql += fieldName+", ";
					valueSql += " ?, " ;
					values.add(value);
				}
			}
			//需要二进制存储列类型的
			else if(showDataConfig.getShowType() == ConstantsCache.ControlType.CONTROLTYPE_BINARY_FILE){
				
				
				if(value instanceof byte[]){
					//如果值已经设置了字节数据 ，那么就不需要再次进行设置
					sql += fieldName+", ";
					valueSql += " ?, " ;
					values.add(value);
					
				}
				else{
				
					//根据相对路径获取绝对路径+
					String rootPath = data.get("WebRoot_path").toString();
					
					String filePath = rootPath +value; 
					
					ShowDataConfig suffixDataConfig = contansSuffixControl(showDataConfigs);
					if(suffixDataConfig != null){        //获取文件后缀名，如果有后缀名控件，在data中给控件赋值
						String suffixFieldName = suffixDataConfig.getFieldName();
						data.put(suffixFieldName, FileUtil.getSuffixByFileName(filePath));
					}
					
					
					try {
						File file = new File(filePath);
						if(file==null || !file.exists()||file.isDirectory())
							continue;
						FileInputStream input = new FileInputStream(file);
						byte[] b = new byte[input.available()];
						input.read(b);
						value = b;
						
						sql += fieldName+", ";
						valueSql += " ?, " ;
						values.add(value);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				}
			}
			else if(showDataConfig.getShowType() == ConstantsCache.ControlType.CONTROLTYPE_FILE_NAMEED_BY_ID){
				String filePath = (String)data.get(showDataConfig.getFieldName());
				map = new HashMap<String, String>();
				map.put("fieldName", showDataConfig.getFieldName());
				map.put("filePath", filePath);
				//获取后缀名
				//获取下一个ID值
				//合成文件名
				//修改文件名
			}
			
			else if(showDataConfig.getShowType() == ConstantsCache.ControlType.CONTROLTYPE_FILE_Suffix){
				String suffixStr = (String)data.get(showDataConfig.getFieldName());
				value = suffixStr;
				
				sql += fieldName+", ";
				valueSql += " ?, " ;
				values.add(value);
			}
			else if(showDataConfig.getShowType() == ConstantsCache.ControlType.CONTROLTYPE_LOGIN_USER){
				//当前用户控件
				String userName = ((User)data.get("session_user")).getAccount();
				sql += fieldName+", ";
				valueSql += " ?, " ;
				values.add(userName);
			}
			else if(value != null && !value.equals("")&& showDataConfig.getShowType() != ConstantsCache.ControlType.CONTROLTYPE_PWD_CONFIRM){ //33不是需要插入到数据库的类型
				sql += fieldName+", ";
				valueSql += " ?, " ;
				values.add(value);
			}
			
		}
		sql = sql.substring(0,sql.lastIndexOf(","));     //删除最后一个","字符
		valueSql = valueSql.substring(0,valueSql.lastIndexOf(","));
		sql += ") values(" + valueSql + ")";
		//Object[] parameters = values.toArray();
		//return getJdbcTemplate().update(sql, parameters);
		logger.debug("===insert sql:"+ sql);
		final String sql_string =sql;
		int returnInt = returnKeyHolder(showDataConfigs);
		if(returnInt > 0){
			
			if(pkShowDataConfig!=null){
				Object value = data.get(pkShowDataConfig.getFieldName());
				
				if(value==null){
					//接口部分是通用别名传递过来
					value = data.get(pkShowDataConfig.getFieldNameAlias());
				}
				Object[] parameters = values.toArray();
				getJdbcTemplate().update(sql, parameters);
				return value;
			}
			
			
			KeyHolder keyHolder = new GeneratedKeyHolder();
			
			getJdbcTemplate().update(new PreparedStatementCreator() {
		        public PreparedStatement createPreparedStatement(Connection con)
		         throws SQLException {
		                        int k=0;
		                        PreparedStatement ps = con.prepareStatement(sql_string,Statement.RETURN_GENERATED_KEYS);
		                        
		                        for(;k<values.size();k++)
		                        {
		                        	ps.setObject(k+1, values.get(k));
		                        }
		                        return ps;
		                  }
		            }, keyHolder);
			
			
			int v_key = keyHolder.getKey().intValue();
			
			logger.debug(" === key value:"+v_key);
			
			if(returnInt == 2){
				String rootPath = data.get("WebRoot_path").toString();
				String uploadPath = ArchCache.getInstance().getSysConfigCache().get(SysConfigCache.KEY_CONFIG_DRUG_IMG_PATH).getValue();
				
				String filePath = map.get("filePath");//文件改名
				
				File file = new File(rootPath+"/"+filePath);
				String newName =rootPath + "/" + uploadPath+"/"+ v_key + "." + FileUtil.getSuffixByFileName(filePath);
				File newFile = new File(newName);
				file.renameTo(newFile);
				
				ShowDataConfig primaryKeyConfig =ArchCache.getInstance().getShowTableConfigCache().getPKShowDataConfig(showTableConfigId); //主键名称
				String primaryKey = primaryKeyConfig.getFieldName();
				
				String updateSql = "update "+ tableName +" set " + map.get("fieldName") + " =?  where " + primaryKey +"=?";//拼接sql
				
				getJdbcTemplate().update(updateSql, uploadPath + "/" +newFile.getName(), v_key);  //执行更新
			}
			return v_key;
		}else{
			Object[] parameters = values.toArray();
			getJdbcTemplate().update(sql, parameters);
			return -1; //-1代表返回的不是外键值
		}
		
		
	}
	
	public void setJDBCDataSource(DataSource dataSource) {
		super.setJdbcDataSource(dataSource);
	}
	
	public int returnKeyHolder(List<ShowDataConfig> showDataConfigs){   //判断是否需要返回主键值
		
		
		
		List<Object> list = new ArrayList<Object>();
		list.add(ConstantsCache.ControlType.CONTROLTYPE_RETURN_PRIMARY_KEY);  //外键引用
		list.add(ConstantsCache.ControlType.CONTROLTYPE_FILE_NAMEED_BY_ID);   //id同名
		for(ShowDataConfig showDataConfig : showDataConfigs){
			if(ConstantsCache.ControlType.CONTROLTYPE_RETURN_PRIMARY_KEY == showDataConfig.getShowType()){
				return 1;
			}
			if(ConstantsCache.ControlType.CONTROLTYPE_FILE_NAMEED_BY_ID == showDataConfig.getShowType()){
				return 2;
			}
			
		}
		
		/*if(!showDataConfigs.get(0).getShowTableName().equalsIgnoreCase(mainTableName)){
			//判断是否和主表名称相同 如果不能 ，那么肯定加入的是关系表
			return 3;
		}*/
		
		return 0;
	}
	public ShowDataConfig contansSuffixControl(List<ShowDataConfig> showDataConfigs){
		for(ShowDataConfig showDataConfig : showDataConfigs){
			if(ConstantsCache.ControlType.CONTROLTYPE_FILE_Suffix == showDataConfig.getShowType()){
				return showDataConfig;
			}
		}
		return null;
	}
}
