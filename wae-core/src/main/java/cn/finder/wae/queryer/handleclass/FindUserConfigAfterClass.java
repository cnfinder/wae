package cn.finder.wae.queryer.handleclass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.cache.ArchCache;

/**
 * @author: finder
 * @function:获取用户参数
 */
public class FindUserConfigAfterClass extends QueryerDBAfterClass {

	@Override
	public TableQueryResult handle(TableQueryResult tableQueryResult,
			long showTableConfigId, QueryCondition<Object[]> condition) {
		 super.handle(tableQueryResult, showTableConfigId, condition);
		 
		logger.info("========="+this.getClass().toString());
		 
		 @SuppressWarnings("unchecked")
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
			
		 List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		
		 String config_key=data.get("config_key").toString();
		 logger.info("======config_key:"+config_key);
		 Map<String,Object> item=new HashMap<String,Object>();
		 list.add(item);
		 String value= ArchCache.getInstance().getUserConfigCache().get(config_key).getValue();
		 logger.info("======config value:"+value);
		 item.put("value", value);
		 
		 
		 tableQueryResult.setResultList(list);
	       tableQueryResult.setPageSize(1);
	       tableQueryResult.setPageIndex(1);
	       tableQueryResult.setCount(1l);
		 return tableQueryResult;
		 
	}

	@Override
	public void setJDBCDataSource(DataSource dataSource) {
		super.setJdbcDataSource(dataSource);
	}

	
	
	
	
	
}
