package cn.finder.wae.queryer.handleclass;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.User;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.common.comm.Common;

/**
 * 
 * @author finder
 *@function:设置时间参数yy-MM-dd
 */
public class SetUserIdQueryerBefore extends  QueryerDBBeforeClass{
	
	
	@Override
	public void handle(long showTableConfigId,
			QueryCondition<Object[]> condition) {
		super.handle(showTableConfigId, condition);

		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
			
		String user_id=null;
		//获取用户名
		User user =(User)data.get("session_user");
		if(user!=null){
			user_id=user.getAccount();
			}
			List<Object> whereValues=new ArrayList<Object>();
			if(condition.getWherepParameterValues()!=null){
				for(Object o:condition.getWherepParameterValues()){
					whereValues.add(o);
				}
				
			}
			whereValues.add(user_id);
			
			//添加过滤条件
			condition.setWherepParameterValues(whereValues.toArray());
		
	}

}
