package cn.finder.global.processor.queryer;

import java.util.Map;

import javax.sql.DataSource;

import org.activiti.engine.TaskService;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.Message;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.queryer.handleclass.QueryerDBAfterClass;

/**
 * @author: whl
 * @function:图片删除处理
 */
public class BinaryDataDeleteAfterClass extends QueryerDBAfterClass {
	

	@Override
	public TableQueryResult handle(TableQueryResult tableQueryResult,
			long showTableConfigId, QueryCondition<Object[]> condition) {
		 super.handle(tableQueryResult, showTableConfigId, condition);
		 
		
		 
		 @SuppressWarnings("unchecked")
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
			
		 
		 String media_id="";
		 
		 try{
			 media_id=data.get("media_id").toString();
			 
			 String sql="delete from g_t_binary_data where guid_value=?";
			 int cnt= getJdbcTemplate().update(sql,new Object[]{media_id});
			 if(cnt==0)
				 tableQueryResult.getMessage().setMsg("media_id is not exists");
			 else{
				 tableQueryResult.getMessage().setMsg("the media_id is deleted");
			 }
		 }
		 catch(Exception e){
			 tableQueryResult.getMessage().setMsg("media_id missing...");
			 tableQueryResult.getMessage().setStatusCode(Message.StatusCode_InfoExp);
		 }
		 
		 
	       
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
