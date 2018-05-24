package cn.finder.wae.queryer.handleclass.properties;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.queryer.handleclass.QueryerDBAfterClass;

/**
 * @author: whl
 * @data: 
 * @function:
 */
public class PropertiesSetDefaultValueQueryerAfter  extends QueryerDBAfterClass {

	/* (non-Javadoc)
	 * @see cn.finder.wae.queryer.handleclass.QueryerAfterClass#handle(cn.finder.wae.business.domain.TableQueryResult, long, cn.finder.ui.webtool.QueryCondition)
	 */
	@Override
	public TableQueryResult handle(TableQueryResult tableQueryResult,long showTableConfigId,QueryCondition<Object[]> condition){
		super.handle(tableQueryResult, showTableConfigId, condition);
		
		logger.debug("====================PropertiesSetDefaultValueQueryerAfter.handle ");
		logger.debug("====================showTableConfigId: "+showTableConfigId);
		
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>) condition).getMapParas();
	
		
	    
	    Map<String, Object>  item = (Map<String, Object>)data.get("properties_item_info");
	    
	    List<Map<String, Object>> list= tableQueryResult.getResultList();
		
		if(list!=null && list.size()>0){
			
			for(int i=0;i<list.size();i++){
				Map<String,Object> prop_item= list.get(i);
				
				String static_default_value="";
				String dynamic_default_value ="";
				try{
					
					static_default_value=prop_item.get("static_default_value").toString();
					
				}catch(Exception e){
					
				}
				
				try{
					
					dynamic_default_value=prop_item.get("dynamic_default_value").toString();
					
				}catch(Exception e){
					
				}
				if(prop_item.get("prop_value")==null){
					if(!StringUtils.isEmpty(dynamic_default_value)){
						//查询业务信息
						prop_item.put("prop_value", item.get(dynamic_default_value));
					}
					else{
						prop_item.put("prop_value", static_default_value);
						
					}
				}
			
				
				
			}
		}
		
		
		return tableQueryResult;
	}
	
	
}
