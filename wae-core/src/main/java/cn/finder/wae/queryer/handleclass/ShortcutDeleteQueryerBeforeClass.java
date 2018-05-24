package cn.finder.wae.queryer.handleclass;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.User;
import cn.finder.wae.business.dto.DeleteQueryConditionDto;
import cn.finder.wae.common.base.BaseActionSupport;
import cn.finder.wae.common.exception.InfoException;

public class ShortcutDeleteQueryerBeforeClass extends QueryerDBBeforeClass{
	
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
			
			//String userName = BaseActionSupport.findSessionUser().getAccount();
			
			String userName = ((User)deleteDto.get("session_user")).getAccount();
			
			String sql_a= "select *  from t_menu_shortcut where id in ("+sb.toString()+")";
			
			final Object[] params = condition.getWherepParameterValues();
			
			List<Map<String, Object>>  dataList = getJdbcTemplate().query(sql_a, params,  
	                new RowMapper<Map<String,Object>>() {  
	                    @Override  
	                    public Map<String,Object> mapRow(ResultSet rs, int rowNum) throws SQLException {  
	                    	Map<String,Object> item = new HashMap<String, Object>();
	                    	item.put("create_user_id", rs.getString("create_user_id"));
	                        return item;  
	                    }

	                }); 
			
			String createName = (String)dataList.get(0).get("create_user_id");
			
			if(userName.equals(createName)){
				String sql_delete = "delete from t_user_menu_shortcut where menu_shortcut_id =? and user_id= ? ";
				getJdbcTemplate().update(sql_delete,sb.toString(),createName);
			}else{
				String sql ="select count(*) cnt  from t_user_menu_shortcut where menu_shortcut_id in ("+sb.toString()+")";
				
				int cnt =queryForInt(sql);
				if(cnt>0){
					//返回 提示
					throw new InfoException("删除失败：该菜单已经赋予角色,请先删除角色-菜单中数据!。");
				}
			}
			
			
			
		}
		
		
	}

}
