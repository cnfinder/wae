package cn.finder.global.processor.queryer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.queryer.handleclass.QueryerDBAfterClass;

/**
 * @author: whl
 * @function: 根据  md5 摘要获取  media_id
 */
public class FindMediaCryptoCodeAfterClass extends QueryerDBAfterClass {
	
	private Logger logger=Logger.getLogger(FindMediaCryptoCodeAfterClass.class);

	@Override
	public TableQueryResult handle(TableQueryResult tableQueryResult,
			long showTableConfigId, QueryCondition<Object[]> condition) {
		 super.handle(tableQueryResult, showTableConfigId, condition);
		 
		 
		 @SuppressWarnings("unchecked")
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
			
		String crypto_code = data.get("crypto_code").toString();
		

		String sql="select guid_value from g_t_binary_data where crypto_code=?";
		
		
		List<Map<String,Object>> resultList= queryForList(sql, new Object[]{crypto_code});
		
		if(resultList!=null&&resultList.size()>0){
			
			List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
			Map<String,Object> item=new HashMap<String, Object>();
			item.put("media_id", resultList.get(0).get("guid_value").toString());
			
			list.add(item);
			tableQueryResult.setResultList(list);
			tableQueryResult.setCount(1l);
		}
		else{
			tableQueryResult.setCount(0l);
		}
       
        tableQueryResult.setPageSize(1);
        tableQueryResult.setPageIndex(1);
		 
		 return tableQueryResult;
		 
	}

	@Override
	public void setJDBCDataSource(DataSource dataSource) {
		super.setJdbcDataSource(dataSource);
	}

	
}
