package cn.finder.wae.queryer.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.ShowDataConfig;
import cn.finder.wae.business.domain.ShowTableConfig;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.domain.properties.PropertiesItem;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.business.rowmapper.RowMapperFactory;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.cache.ConstantsCache;
import cn.finder.wae.common.aspect.logger.ArchType;
import cn.finder.wae.common.aspect.logger.FinderLogger;
/***
 * 属性添加处理类
 * @author whl
 *
 */
public class PropertiesSaveCommonQueryer extends  BaseCommonDBQueryer{
	
	
	@FinderLogger(archType=ArchType.FINDER_ARCH)
	@Override
	public TableQueryResult queryTableQueryResult(long showTableConfigId,
			QueryCondition<Object[]> condition){
		
		logger.debug("=====go into PropertiesAddCommonQueryer======");
		
		logger.debug("==showTableConfigId:"+showTableConfigId);

		final TableQueryResult qr =new TableQueryResult();
		
		@SuppressWarnings("unchecked")
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();

		ShowTableConfig showTableConfig = ArchCache.getInstance().getShowTableConfigCache().get(showTableConfigId);
		List<ShowDataConfig> showDataConfigs = showTableConfig.getShowDataConfigs();
		
		List<ShowDataConfig> sdfFKs=new ArrayList<ShowDataConfig>();//外键 code room_id
	
		StringBuffer sbFk=new StringBuffer();
		
		List<Object> whereParas=new ArrayList<Object>();
		
		boolean isFirst=true;
		
		for(ShowDataConfig sdf:showDataConfigs){
		
			if(sdf.getShowType() == ConstantsCache.ControlType.CONTROLTYPE_RETURN_PRIMARY_KEY){
				sdfFKs.add(sdf);
				whereParas.add(data.get(sdf.getFieldNameAlias()));
				
				if(isFirst){
					sbFk.append(sdf.getFieldName()+" = ? ");
					isFirst=false;
				}
				else{
					sbFk.append(" and "+sdf.getFieldName()+" = ? ");
				}
			}
		}
		
		
		//主键  id
		ShowDataConfig sdfPK= ArchCache.getInstance().getShowTableConfigCache().getPKShowDataConfig(showTableConfigId);
		
		
		
		
		//获取属性
		String sql="select * from "+sdfPK.getFieldTableName()+" where "+sbFk.toString();
		
		
		List<PropertiesItem> propitems= getJdbcTemplate().query(sql,whereParas.toArray() ,new RowMapperFactory.PropertiesItemRowMapper());
		
		if(propitems!=null && propitems.size()>0){
			
			for(int i=0;i<propitems.size();i++){
				PropertiesItem propitem= propitems.get(i);
				String prop_name=propitem.getPropName();
				String sql_update_item= "update "+sdfPK.getFieldTableName()+" set prop_value =? where "+sdfPK.getFieldName()+"=?";
				
				getJdbcTemplate().update(sql_update_item, new Object[]{data.get(prop_name),propitem.getId()});
			}
		}
		
		
		qr.setResultList(new ArrayList<Map<String,Object>>());
		qr.setPageIndex(1);
		qr.setPageSize(10);
		qr.setCount(0L);
		
		return qr;
		
		
	}
	
	public void setJDBCDataSource(DataSource dataSource) {
		super.setJdbcDataSource(dataSource);
	}
	
}
