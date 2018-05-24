package cn.finder.wae.queryer.common;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hsqldb.lib.StringUtil;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.ShowDataConfig;
import cn.finder.wae.business.domain.ShowTableConfig;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.cache.ConstantsCache;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.common.aspect.logger.ArchType;
import cn.finder.wae.common.aspect.logger.FinderLogger;


/***
 * 看做DAO层  因为这样处理数据源容易
 * @author wu hualong
 *
 * instead by EditCascadeCommonQueryer
 */

@Deprecated  
public class EditCommonQueryer extends  BaseCommonDBQueryer{
	Logger log = Logger.getLogger(this.getClass());
	
	private final static Logger logger = Logger.getLogger(EditCommonQueryer.class);
	@FinderLogger(archType=ArchType.FINDER_ARCH)
	@Override
	public TableQueryResult queryTableQueryResult(long showTableConfigId,
			QueryCondition<Object[]> condition) {
		@SuppressWarnings("unchecked")
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
		
		Long editConfigId = Long.valueOf(String.valueOf(data.get("showtableConfigId")));
		ShowTableConfig showTableConfig = ArchCache.getInstance().getShowTableConfigCache().get(editConfigId);
		
		ShowDataConfig primaryKeyConfig =ArchCache.getInstance().getShowTableConfigCache().getPKShowDataConfig(editConfigId);
		String primaryKey = primaryKeyConfig.getFieldName();
		if(data.get(primaryKey) == null || StringUtil.isEmpty(String.valueOf(data.get(primaryKey)))){
			log.info("主键设置错误！");
			return null;
		}
		String sql = "" ;
		String valueSql = "";
		List<Object> values = new ArrayList<Object>();
		List<ShowDataConfig> showDataConfigs = showTableConfig.getShowDataConfigs();
		
		for(ShowDataConfig showDataConfig:showDataConfigs){
			String fieldName = showDataConfig.getFieldName();
			Object value = data.get(fieldName);
			if(showDataConfig.getShowType() == ConstantsCache.ControlType.CONTROLTYPE_SYSTEM_DATE){
				valueSql += fieldName + " = ?, ";
				values.add(new Timestamp(System.currentTimeMillis()));
				continue;
			}
			if (value != null && showDataConfig.getShowType() != ConstantsCache.ControlType.CONTROLTYPE_PWD_CONFIRM) { // 33不是需要插入到数据库的类型
				if (value.equals("")) {
					if (showDataConfig.getShowType() != ConstantsCache.ControlType.CONTROLTYPE_PWD) {
						if (showDataConfig.getDataType() != ConstantsCache.DataType.DATATYPE_STRING) {
							if(!StringUtils.isEmpty(showDataConfig.getDefaultValue())){
								value = showDataConfig.getDefaultValue();
							}else{
								if(showDataConfig.getShowType() == ConstantsCache.ControlType.CONTROLTYPE_DATE){
									value = null;
								}
								if(showDataConfig.getShowType() == ConstantsCache.DataType.DATATYPE_NUMBER){
									value = -1;
								} 
							}
							
						}
						valueSql += fieldName + " = ?, ";
						values.add(value);
						
					}
					
				}else{
					valueSql += fieldName + " = ?, ";
					values.add(value);
				}
			}
		}
		
		
		valueSql = valueSql.substring(0,valueSql.lastIndexOf(","));//删除最后一个","
		
		sql = "update " + data.get("table001abcX") + " set " +valueSql;
		sql += " where 1=1";
		sql += " and "+ primaryKey + " = " +data.get(primaryKey); 
		
		getJdbcTemplate().update(sql, values.toArray());
		return null;
	}


	public void setJDBCDataSource(DataSource dataSource) {
		super.setJdbcDataSource(dataSource);
	}
}
