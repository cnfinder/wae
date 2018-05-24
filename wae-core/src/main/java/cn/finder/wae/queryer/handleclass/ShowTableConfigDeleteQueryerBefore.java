package cn.finder.wae.queryer.handleclass;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.dto.DeleteQueryConditionDto;
import cn.finder.wae.common.exception.InfoException;

public class ShowTableConfigDeleteQueryerBefore extends QueryerDBBeforeClass {

	@Override
	public void handle(long showTableConfigId,
			QueryCondition<Object[]> condition) {
		// TODO Auto-generated method stub
		super.handle(showTableConfigId, condition);
		
		
		logger.debug("====================ShowTableConfigDeleteQueryerBefore.handle ");
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
			
			String sql ="select (select count(*) cnt  from t_showtable_config where add_showtable_config_id in ("
					+sb.toString()+") or  edit_showtable_config_id in ("
					+sb.toString()+") or delete_showtable_config_id in ("
					+sb.toString()+") or search_showtable_config_id in ("
					+sb.toString()+") or child_showtable_config_id in ("
					+sb.toString()+")) ";
			
			String sql2= "select count(*) from t_role_showtable_config where showtable_config_id in ("+ sb.toString()+")";
			
			int cnt =queryForInt(sql);
			if(cnt>0){
				//返回 提示
				throw new InfoException("删除失败：该表配置已经被引用，请先检查表配置!");
			}
			int cnt2 =queryForInt(sql2);
			if(cnt2>0){
				//返回 提示
				throw new InfoException("删除失败：该表配置已经被引用，请先检查角色-表配置!");
			}
			
		}
		
		
	}

}
