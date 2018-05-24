package cn.finder.wae.queryer.handleclass.chart;

import java.util.Map;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.queryer.handleclass.QueryerDBBeforeClass;

/**
 * @author: wuhualong
 * @data:2014-05-28
 * @function: 添加图表 记录  通用 图表模板ID 来获取 图表模板脚本数据
 */
public class ChartAddQueryerBefore  extends QueryerDBBeforeClass {

	@Override
	public void handle(long showTableConfigId,
			QueryCondition<Object[]> condition) {
		// TODO Auto-generated method stub
		super.handle(showTableConfigId, condition);
		
		
		logger.debug("====================ChartAddQueryerBefore.handle ");
		logger.debug("====================showTableConfigId: "+showTableConfigId);
		
		
		//Map<String,String> retMap = new  java.util.HashMap<String, String>();
		
		@SuppressWarnings("unchecked")
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
		
		

		//图表模板ID
		int chartTemplateId = Integer.valueOf(data.get("chart_template_id").toString());
		
		
		String sql ="select  chart_data from t_chart_template where id=?";
		
		//获取 图表模板脚本数据
		String chartData = getJdbcTemplate().queryForObject(sql,new Object[]{chartTemplateId},String.class);
		
		
		data.put("chart_data", chartData);
			
	}
}
