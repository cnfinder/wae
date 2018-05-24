package cn.finder.wae.queryer.handleclass.properties;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.queryer.handleclass.QueryerDBBeforeClass;

/**
 * @author: xiaoht
 * @data:
 * @function: 属性模板复制
 */
public class CopyPropertiesTemplateTypeQueryerBeforeClass extends QueryerDBBeforeClass {

	
	
	@Override
	public void handle(long showTableConfigId,
			QueryCondition<Object[]> condition) {
		
		super.handle(showTableConfigId, condition);

		logger.debug("====================PropertiesTemplateAddQueryerBeforeClass.handle ");
		logger.debug("====================showTableConfigId: "+showTableConfigId);
		
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
		int selectId = Integer.parseInt(data.get("id").toString());
		int parent_id =-1;
		try{
			parent_id = Integer.parseInt(data.get("parent_id").toString());
		}catch(Exception e){
			
		}
		
		String  name = data.get("name").toString();
		String code =  data.get("code").toString();
		String remark = data.get("remark").toString();
		
		//获取自增id
		final String getId_sql = "insert into wae_t_properties_template_type(name,code,remark,parent_id) values(?,?,?,?)";
		

		final Object[] param=new Object[]{name,code,remark,parent_id};
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					int k=0;
					PreparedStatement ps = con.prepareStatement(getId_sql,Statement.RETURN_GENERATED_KEYS);
					for(;k<param.length;k++){
						ps.setObject(k+1, param[k]);
		            }
		            return ps;
			}
		}, keyHolder);
			
		int id = keyHolder.getKey().intValue();
		
		String copyTemplate_sql = "INSERT INTO `wae_t_properties_template` (`name`,`show_type`,`data_type`,`process_command`,`data_showtableConfigId`,`data_source_static`," +
				"`sort`,`prop_name`,`is_new_line`,`is_required`,`ctrl_width`,`ctrl_height`,`static_default_value`,`dynamic_default_value`,`is_editable`)(SELECT `name`,`show_type`," +
				"`data_type`,`process_command`,`data_showtableConfigId`,`data_source_static`,`sort`,`prop_name`,`is_new_line`,`is_required`,`ctrl_width`,`ctrl_height`," +
				"`static_default_value`,`dynamic_default_value`,`is_editable` FROM `wae_t_properties_template` WHERE `properties_template_type_id` = ?)";
		
		getJdbcTemplate().update(copyTemplate_sql,new Object[]{selectId});
		
		String updateTemplate_sql = "UPDATE `wae_t_properties_template` SET `properties_template_type_id` = ? WHERE properties_template_type_id IS NULL ";
	
		getJdbcTemplate().update(updateTemplate_sql,new Object[]{id});
		
	}
	
	

}
