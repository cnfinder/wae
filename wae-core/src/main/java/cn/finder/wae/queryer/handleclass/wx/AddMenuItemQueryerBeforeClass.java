package cn.finder.wae.queryer.handleclass.wx;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.User;
import cn.finder.wae.business.dto.DeleteQueryConditionDto;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.common.exception.InfoException;
import cn.finder.wae.queryer.handleclass.QueryerDBBeforeClass;



public class AddMenuItemQueryerBeforeClass extends QueryerDBBeforeClass{
	
	@Override
	public void handle(long showTableConfigId,
			QueryCondition<Object[]> condition) {
		// TODO Auto-generated method stub
		super.handle(showTableConfigId, condition);
		
		
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
		
		
		
		String type="";
		try{
			type=data.get("type").toString();
		}catch(Exception e){
			
		}
		if("view".equals(type)){
			//一定要填写url
			String url="";
			
			try{
				url=data.get("url").toString();
			}catch(Exception e){
				
			}
			if(StringUtils.isEmpty(url)){
				throw new InfoException("view 类型 一定要填写 url参数");
			}
			
		}
		else if("click".equals(type)){
			//一定要填写url
			String key_id=data.get("key_id").toString();
			if(StringUtils.isEmpty(key_id)){
				throw new InfoException("click 类型 一定要填写key参数");
			}
			
		}
		int item_fk_key_value=Integer.parseInt(data.get("item_fk_key_value").toString());

		data.remove("wx_appinfo_id");
		
		data.put("wx_appinfo_id", item_fk_key_value);
		
	}
		

}
