package cn.finder.wae.queryer.handleclass.webpush;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.pushlet.PushWebSender;
import cn.finder.wae.queryer.handleclass.QueryerDBAfterClass;

/**
 * @author: wuhualong
 * @data:
 * @function:Web推送测试
 * 需要参数  from_userid 
 * 		 from_username 可选
 * 		 user_id 接收者
 *       content: 消息内容
 */
public class WebPushTestAfterClass extends QueryerDBAfterClass {

	@Override
	public TableQueryResult handle(TableQueryResult tableQueryResult,
			long showTableConfigId, QueryCondition<Object[]> condition) {
		 super.handle(tableQueryResult, showTableConfigId, condition);
		 
		
		 
		 @SuppressWarnings("unchecked")
		 Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
			
		 String subject=data.get("subject").toString();
		 
		 String user_id=data.get("user_id").toString();
		 
		 String content=data.get("content").toString();
		 
		 PushWebSender sender=new PushWebSender();
		 Map<String,Object> values=new HashMap<String, Object>();
		 values.put("content", content);


		 sender.sendToUser(subject, user_id, data);
		 
		 logger.info("=====发送给:"+user_id+" 成功");
		
		 
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
