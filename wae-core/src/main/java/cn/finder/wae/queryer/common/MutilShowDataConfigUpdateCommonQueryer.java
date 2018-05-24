package cn.finder.wae.queryer.common;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import net.sf.ezmorph.bean.MorphDynaBean;

import org.hsqldb.lib.StringUtil;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.Message;
import cn.finder.wae.business.domain.ShowDataConfig;
import cn.finder.wae.business.domain.ShowTableConfig;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.domain.User;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.cache.ConstantsCache;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.cache.SysConfigCache;
import cn.finder.wae.common.aspect.logger.ArchType;
import cn.finder.wae.common.aspect.logger.FinderLogger;
import cn.finder.wae.common.comm.JsonUtil;
import cn.finder.wae.common.comm.MD5Util;
import cn.finder.wae.common.db.ds.DSManager;

import com.ssh.common.util.FileUtil;

public class MutilShowDataConfigUpdateCommonQueryer extends  BaseCommonDBQueryer{
	private static int foreignKey;
	private Map<String, String> map;
	
	@FinderLogger(archType=ArchType.FINDER_ARCH)
	@Override
	public TableQueryResult queryTableQueryResult(long showTableConfigId,
			QueryCondition<Object[]> condition){
		
		logger.debug("=====go into MutilShowDataConfigUpdateCommonQueryer======");
		
		logger.debug("==showTableConfigId:"+showTableConfigId);
		TableQueryResult queryResult =new TableQueryResult();
		queryResult.setCount(0l);
		Message msg=new Message();
		msg.setMsg("列配置失败");
		queryResult.setMessage(msg);
		
		
		@SuppressWarnings("unchecked")
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
		ShowTableConfig showTableConfig = ArchCache.getInstance().getShowTableConfigCache().get(showTableConfigId);
		List<ShowDataConfig> showDataConfigs = showTableConfig.getShowDataConfigs();
		
		DataSource ds = DSManager.getDataSourceMaps().get(showTableConfig.getTargetDs());
		setJdbcDataSource(ds);//set datasource
		
		List<Map<String,Object>> showdataconfigList = (List<Map<String,Object>>)data.get("showdataconfig");
		
		if(showdataconfigList!=null && showdataconfigList.size()>0){
			
			for(int i=0;i<showdataconfigList.size();i++){
				
				//"_state"
				Map<String,Object> showdataitem= showdataconfigList.get(i);
				
				String chk_value = showdataitem.get("chk_value").toString();
				
				String table_name = showdataitem.get("table_name").toString();
				
				String column_name = showdataitem.get("column_name").toString();
				
				String _state = showdataitem.get("_state").toString();
				
				
				
				
				logger.debug("===chk_value:"+chk_value+",column_name="+column_name+",_state="+_state);
				
				
				if("0".equals(chk_value)){
					//如果 chk_value=0 那么直接执行删除  不管之前是否存在此列配置
					String del_sql = "delete from t_showdata_config where showtable_name=? and field_name=?";
					
					getJdbcTemplate().update(del_sql, new Object[]{table_name,column_name});
				}else if("1".equals(chk_value)){
				
					//如果 chk_value=1 那么查询是否存在，不存在进行插入，存在进行更新
					
					String sql ="select count(*) from  t_showdata_config where showtable_name=? and field_name=?";
					
					int cnt = queryForInt(sql, new Object[]{table_name,column_name});
					
					//Map<String,Object> showdataconfigMap = (Map<String,Object>)JsonUtil.getMap4Json(showdataitem.get("showdata_config").toString());
					
					MorphDynaBean showdataconfigMap = (MorphDynaBean)showdataitem.get("showdata_config");
					ShowDataConfig config=new ShowDataConfig();
					config.setShowTableName(table_name);
					config.setFieldName(column_name);
					
					
					
					
					String field_name_alias;
					try{
						 if(showdataconfigMap.get("fieldNameAlias")!=null){
							 field_name_alias = showdataconfigMap.get("fieldNameAlias").toString();
							 
							 config.setFieldNameAlias(field_name_alias);
						 }
					}catch(Exception e){
					
					}
					 
					String field_tablename;
					try
					{
						if(showdataconfigMap.get("fieldTableName")!=null){
							field_tablename=showdataconfigMap.get("fieldTableName").toString();
							
							config.setFieldTableName(field_tablename);
						}
					}catch(Exception e){
						
					}
					
					
					String data_type;
					try{
						if(showdataconfigMap.get("dataType")!=null){
							data_type =showdataconfigMap.get("dataType").toString();
							config.setDataType(Integer.valueOf(data_type));
						}
					}catch(Exception e){
							
					}
					
					String show_type; 
					try{
						if(showdataconfigMap.get("showType")!=null){
							show_type =showdataconfigMap.get("showType").toString();
							config.setShowType(Integer.valueOf(show_type));
						}
					}catch(Exception e){
						
					}
					String group_name; 
					
					try{
						if(showdataconfigMap.get("groupName")!=null){
							group_name =showdataconfigMap.get("groupName").toString();
							config.setGroupName(group_name);
						}
					}catch(Exception e){
						
					}
					String field_nav; 
					try{
						if(showdataconfigMap.get("fieldNav")!=null){
							field_nav =showdataconfigMap.get("fieldNav").toString();
							config.setFieldNav(field_nav);
						}
					}catch(Exception e){
						
					}
					String item_render_event; 
					
					try{
						if(showdataconfigMap.get("itemRenderEvent")!=null){
							item_render_event =showdataconfigMap.get("itemRenderEvent").toString();
							config.setItemRenderEvent(item_render_event);
						}
					}catch(Exception e){
					
					}
					
					String sort; 
					
					try{
						if(showdataconfigMap.get("sort")!=null){
							sort =showdataconfigMap.get("sort").toString();
							config.setSort(Integer.valueOf(sort));
						}
					}catch(Exception e){
						
					}
					
					String is_primarykey;
					try{
						if(showdataconfigMap.get("isPrimaryKey")!=null){
							is_primarykey =showdataconfigMap.get("isPrimaryKey").toString();
							config.setIsPrimaryKey(Integer.valueOf(is_primarykey));
						}
					}catch(Exception e){
						
					}
					
					String is_hide; 
					try{
						if(showdataconfigMap.get("isHide")!=null){
							is_hide =showdataconfigMap.get("isHide").toString();
							config.setIsHide(Integer.valueOf(is_hide));
						}
					}catch(Exception e){
						
					}
					
					String editable; 
					try{
						if(showdataconfigMap.get("editable")!=null){
							editable =showdataconfigMap.get("editable").toString();
							config.setEditable(Integer.valueOf(editable));
						}
					}catch(Exception e){
						
					}
					
					
					
					if(cnt>0){
						//存在，进行更新
						String update_sql ="update t_showdata_config set field_name_alias=?,field_tablename=?,data_type=?,show_type=?,group_name=?,field_nav=?,item_render_event=?,sort=?,is_primarykey=?,is_hide=?,editable=? where showtable_name=? and field_name=?";
						
						getJdbcTemplate().update(update_sql, new Object[]{config.getFieldNameAlias(),config.getFieldTableName(),
								config.getDataType(),config.getShowType(),config.getGroupName(),config.getFieldNav(),config.getItemRenderEvent(),config.getSort(),config.getIsPrimaryKey(),
								config.getIsHide(),config.getEditable(),config.getShowTableName(),config.getFieldName()});
					}else{
						
						//插入，  其他列默认不操作
						
						String insert_sql ="insert into t_showdata_config(showtable_name,field_name,field_name_alias,field_tablename,data_type,show_type,group_name,field_nav,item_render_event,sort,is_primarykey,is_hide,editable) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
						
						getJdbcTemplate().update(insert_sql, new Object[]{config.getShowTableName(),config.getFieldName(),config.getFieldNameAlias(),config.getFieldTableName(),
								config.getDataType(),config.getShowType(),config.getGroupName(),config.getFieldNav(),config.getItemRenderEvent(),config.getSort(),config.getIsPrimaryKey(),
								config.getIsHide(),config.getEditable()});
						
					}
				}
				
			}
			
			
		}
		
		
		
		
		/*Map<String, String> tables = new HashMap<String, String>();
		for(ShowDataConfig sdc:showDataConfigs){
			String tableName = sdc.getFieldTableName();
			if(!StringUtil.isEmpty(tableName)){
				if(!tables.containsKey(tableName)){             //
					String jsonStr = String.valueOf(data.get(tableName));
					tables.put(tableName, jsonStr);
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
			}else{
				singleAddRecord(addConfigId, tableName, data); 
			}
			
			
		}*/
		
		msg.setMsg("列配置成功");
		
		return queryResult;
	}
	
	public int singleAddRecord(long showTableConfigId,String tableName, Map<String, Object> data){

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
			//密码控件，md5加密
			else if(showDataConfig.getShowType() == ConstantsCache.ControlType.CONTROLTYPE_PWD){
				sql += fieldName+", ";
				valueSql += " ?, " ;
				values.add(MD5Util.getMD5(value.toString()));
			}
				
			else if(showDataConfig.getShowType() ==  ConstantsCache.ControlType.CONOTROLTYPE_SHOW_SELECT) continue; //只显示用的下拉框	
			
			else if(showDataConfig.getShowType() == ConstantsCache.ControlType.CONTROLTYPE_RETURN_PRIMARY_KEY){
				if(foreignKey > 0){
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
				//根据相对路径获取绝对路径+
				String rootPath = data.get("WebRoot_path").toString();
				
				String filePath = rootPath +value; 
				
				ShowDataConfig suffixDataConfig = contansSuffixControl(showDataConfigs);
				if(suffixDataConfig != null){        //获取文件后缀名，如果有后缀名控件，在data中给控件赋值
					String suffixFieldName = suffixDataConfig.getFieldName();
					data.put(suffixFieldName, FileUtil.getSuffixByFileName(filePath));
				}
				
				File file = new File(filePath);
				try {
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
		
		final String sql_string =sql;
		int returnInt = returnKeyHolder(showDataConfigs);
		if(returnInt > 0){
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
