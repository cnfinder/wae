package cn.finder.wae.queryer.handleclass.properties;

import java.util.Map;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.queryer.handleclass.QueryerDBBeforeClass;

/**
 * @author: wuhualong
 * @data:
 * @function: 属性模板编辑前置处理
 */
public class PropertiesTemplateEditQueryerBeforeClass extends QueryerDBBeforeClass {

	
	
	@Override
	public void handle(long showTableConfigId,
			QueryCondition<Object[]> condition) {
		
		super.handle(showTableConfigId, condition);

		logger.debug("====================PropertiesTemplateAddQueryerBeforeClass.handle ");
		logger.debug("====================showTableConfigId: "+showTableConfigId);
		
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
		
		
		//1. 判断 prop_name 是否已经存在
		
	}
	
	

}
