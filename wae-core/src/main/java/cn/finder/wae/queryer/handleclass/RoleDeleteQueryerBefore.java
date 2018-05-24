package cn.finder.wae.queryer.handleclass;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.dto.DeleteQueryConditionDto;
import cn.finder.wae.common.exception.InfoException;

public class RoleDeleteQueryerBefore extends QueryerDBBeforeClass{
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
			
			String sql ="select (select count(*) cnt  from t_user where role_id in ("+sb.toString()
					+")) + (select count(*) from t_role_menu where role_id in ("+sb.toString()
					+")) + (select count(*) from t_role_request_command where role_id in(" + sb.toString()
					+")) + (select count(*) from t_role_showtable_config where role_id in("+ sb.toString()
					+"))";
			
			int cnt =queryForInt(sql);
			if(cnt>0){
				//返回 提示
				throw new InfoException("删除失败：该角色已被引用,请先检查其他配置!");
			}
		}
	}
}
