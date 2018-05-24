package cn.finder.global.processor.queryer;

import java.util.Map;

import javax.sql.DataSource;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.Message;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.queryer.handleclass.QueryerDBAfterClass;

/**
 * @author: finder
 * @function:修改图片资源的名称
 */
public class UpdateMediaNameAfterClass extends QueryerDBAfterClass {
	
	@Override
	public TableQueryResult handle(TableQueryResult tableQueryResult,
			long showTableConfigId, QueryCondition<Object[]> condition) {
		 super.handle(tableQueryResult, showTableConfigId, condition);
		 
		 tableQueryResult.getMessage().setMsg("修改失败");
		 
		 @SuppressWarnings("unchecked")
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
			
		 
		 String media_id="";
		 
		 try{
			 media_id=data.get("media_id").toString();
			 
		 }
		 catch(Exception e){
			 tableQueryResult.getMessage().setMsg("media_id missing...");
			 tableQueryResult.getMessage().setStatusCode(Message.StatusCode_InfoExp);
		 }
		 
		 
		String name= data.get("name").toString();
		
		
		String sql="update g_t_binary_data set name=? where guid_value=?";
		
		int c=getJdbcTemplate().update(sql,new Object[]{name,media_id});
		
		tableQueryResult.getMessage().setMsg("修改成功");
	       
       tableQueryResult.setCount(1l);
       tableQueryResult.setPageSize(1);
       tableQueryResult.setPageIndex(1);
	       
		 
		 
		 return tableQueryResult;
		 
	}

	@Override
	public void setJDBCDataSource(DataSource dataSource) {
		super.setJdbcDataSource(dataSource);
	}

	
}
