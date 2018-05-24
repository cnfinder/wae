package cn.finder.wae.queryer.handleclass.third;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.common.third.ThirdUtils;
import cn.finder.wae.queryer.handleclass.QueryerDBAfterClass;

/**
 * @author: finder
 * @function:长链接转短链接
 */
public class FindToShortUrlAfterClass extends QueryerDBAfterClass {

	@Override
	public TableQueryResult handle(TableQueryResult tableQueryResult,
			long showTableConfigId, QueryCondition<Object[]> condition) {
		 super.handle(tableQueryResult, showTableConfigId, condition);
		 
		 
		 @SuppressWarnings("unchecked")
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
			
		  List<Map<String,Object>> list = new java.util.ArrayList<Map<String,Object>>();
	       
	       Map<String,Object> item=new HashMap<>();
	       list.add(item);
	       
	       tableQueryResult.setCount(1l);
	       tableQueryResult.setPageSize(1);
	       tableQueryResult.setPageIndex(1);
	       tableQueryResult.setResultList(list);
		 
		String long_url = data.get("long_url").toString();
		String short_url="";
		try {
			short_url=ThirdUtils.toShortUrl(long_url);
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		item.put("short_url", short_url);
		
	     
		 
		 return tableQueryResult;
		 
	}

	@Override
	public void setJDBCDataSource(DataSource dataSource) {
		super.setJdbcDataSource(dataSource);
	}

	
}
