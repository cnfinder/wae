package cn.finder.wae.queryer.handleclass.wx;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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


/****
 * 微信菜单获取  设置 wx_appinfo_id 参数过滤
 * @author Administrator
 *
 */
public class SetWXappinfoIdFilterQueryerBeforeClass extends QueryerDBBeforeClass{
	
	@Override
	public void handle(long showTableConfigId,
			QueryCondition<Object[]> condition) {
		// TODO Auto-generated method stub
		super.handle(showTableConfigId, condition);
		
		
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
		Map<String, Object> forwardData =	((MapParaQueryConditionDto<String, Object>)condition).getForwardParams();
		
		String item_fk_key_value = ((String[])forwardData.get("item_fk_key_value"))[0].toString();
		List<Object> whereValues=new ArrayList<Object>();
		if(condition.getWherepParameterValues()!=null){

			for(Object o:condition.getWherepParameterValues()){
				whereValues.add(o);
			}
			
		}
		whereValues.add(item_fk_key_value); 
		//添加过滤条件
		condition.setWherepParameterValues(whereValues.toArray());
		
	}
		

}
