package cn.finder.wae.queryer.handleclass;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.ShowTableConfig;
import cn.finder.wae.business.domain.User;
import cn.finder.wae.business.dto.DeleteQueryConditionDto;
import cn.finder.wae.business.dto.ForeignRelationChksQueryConditionDto;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.common.exception.InfoException;

public class MenuToShortcutQueryerBeforeClass extends QueryerDBBeforeClass{
	
	@Override
	public void handle(long showTableConfigId,
			QueryCondition<Object[]> condition) {
		// TODO Auto-generated method stub
		super.handle(showTableConfigId, condition);
		
		
		logger.debug("====================HospitalDeleteQueryerBeforeClass.handle ");
		logger.debug("====================showTableConfigId: "+showTableConfigId);
		
		final ForeignRelationChksQueryConditionDto param=(ForeignRelationChksQueryConditionDto)condition;
		
		Object[] keys = param.getSubValues().toArray();
		
		if(keys!=null && keys.length>0){
			
			StringBuffer sb =new StringBuffer();
			for(Object id:keys){
				sb.append(",");
				sb.append(id.toString());
			}
			sb.deleteCharAt(0);
			
			
			String sql_menu = "select * from t_menu where id in ("+sb.toString()+")" ;
			
			final Object[] params = condition.getWherepParameterValues();
			
			List<Map<String, Object>>  dataList = getJdbcTemplate().query(sql_menu, params,  
	                new RowMapper<Map<String,Object>>() {  
	                    @Override  
	                    public Map<String,Object> mapRow(ResultSet rs, int rowNum) throws SQLException {  
	                    	Map<String,Object> item = new HashMap<String, Object>();
	                    	
	                    	item.put("id", rs.getInt("id"));
	                    	item.put("sort", rs.getInt("sort"));
	                    	item.put("command", rs.getString("command"));
	                    	item.put("name", rs.getString("name"));
	                    	item.put("style", rs.getString("style"));
	                    	item.put("icon_position", rs.getString("icon_position"));
	                        return item;  
	                    }

	                }); 
			
			
			 final String sql_shortcut = "insert into t_menu_shortcut (command,name,style,sort,icon_position,create_user_id,create_date) values(?,?,?,?,?,?,?)";
			 String sql_user_shortcut= "insert into t_user_menu_shortcut(user_id,menu_shortcut_id) values(?,?)" ;
			 
			 for(final Map<String, Object> map : dataList){
				 
				/* List<Object> values = new ArrayList<Object>();
				 Object s[] = map.keySet().toArray();
				 for(int i=0;i<map.size();i++){
					 values.add(map.get(s[i]));
				 }*/
				 KeyHolder holder = new GeneratedKeyHolder();
				 getJdbcTemplate().update(new PreparedStatementCreator() {
					
					
					@Override
					public PreparedStatement createPreparedStatement(Connection con)
							throws SQLException {
						 int i = 1;
						 PreparedStatement ps  = con.prepareStatement(sql_shortcut,new String[]{"id"});
						 ps.setObject(i++,map.get("command"));
						 ps.setObject(i++, map.get("name"));
						 ps.setObject(i++, map.get("style"));
						 ps.setObject(i++, map.get("sort"));
						 ps.setObject(i++,map.get("icon_position"));
						 ps.setObject(i++,param.getMainValue());
						 ps.setObject(i++,map.get("create_date"));
						 
						return ps;
					}
				},holder);
				 
				 int id = holder.getKey().intValue();
				 String userName = param.getMainValue();
				 
				 getJdbcTemplate().update(sql_user_shortcut,userName,id);
				 
				 
			 }
			 
		}
		
		
	}

}
