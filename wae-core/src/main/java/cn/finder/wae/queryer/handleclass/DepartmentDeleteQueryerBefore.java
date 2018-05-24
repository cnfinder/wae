package cn.finder.wae.queryer.handleclass;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.dto.DeleteQueryConditionDto;
import cn.finder.wae.common.exception.InfoException;

public class DepartmentDeleteQueryerBefore extends QueryerDBBeforeClass {
	@Override
	public void handle(long showTableConfigId,
			QueryCondition<Object[]> condition) {
		// TODO Auto-generated method stub
		super.handle(showTableConfigId, condition);
		
		
		logger.debug("====================HospitalDeleteQueryerBeforeClass.handle ");
		logger.debug("====================showTableConfigId: "+showTableConfigId);
		DeleteQueryConditionDto deleteDto =(DeleteQueryConditionDto)condition;
		
		String[] keys = deleteDto.getIds();
		
		if(keys!=null && keys.length>0){
			
			StringBuffer sb =new StringBuffer();
			for(String id:keys){
				sb.append(",");
				sb.append(id);
			}
			sb.deleteCharAt(0);
			
			String sql ="select count(*) cnt  from t_user where department_id in ("+sb.toString()+")";
			
			int cnt =queryForInt(sql);
			if(cnt>0){
				//返回 提示
				throw new InfoException("删除失败：该部门下现有用户存在,不能删除!!。");
			}
		}
	}
}
