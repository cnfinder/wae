package cn.finder.wae.queryer.handleclass;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.common.comm.Common;

/**
 * 
 * @author finder
 *@function:设置时间参数yy-MM-dd
 */
public class SetTodayTimeQueryerBefore extends  QueryerDBBeforeClass{
	
	
	@Override
	public void handle(long showTableConfigId,
			QueryCondition<Object[]> condition) {
		super.handle(showTableConfigId, condition);

		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();

			List<Object> whereValues=new ArrayList<Object>();
			if(condition.getWherepParameterValues()!=null){
				for(Object o:condition.getWherepParameterValues()){
				        whereValues.add(o);
				}
				
			}
			whereValues.add(Common.formatDate(new Date(),"yyyy-MM-dd"));
			
			//添加过滤条件
			condition.setWherepParameterValues(whereValues.toArray());
		
	}

}
