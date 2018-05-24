package cn.finder.wae.queryer.handleclass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.ShowDataConfig;
import cn.finder.wae.business.domain.ShowDataType;
import cn.finder.wae.business.domain.TableQueryResult;

/**
 * @author: whl
 * @data: 
 * @function:属性列表格式的数据 转成 一行  形式数据
 */
public class PropertiesListToRowQueryerAfter  extends QueryerDBAfterClass {

	/* (non-Javadoc)
	 * @see cn.finder.wae.queryer.handleclass.QueryerAfterClass#handle(cn.finder.wae.business.domain.TableQueryResult, long, cn.finder.ui.webtool.QueryCondition)
	 */
	@Override
	public TableQueryResult handle(TableQueryResult tableQueryResult,long showTableConfigId,QueryCondition<Object[]> condition){
		super.handle(tableQueryResult, showTableConfigId, condition);
		
		TableQueryResult qr=new TableQueryResult();
		
		List<Map<String,Object>> newResult=new ArrayList<Map<String,Object>>();
		Map<String,Object> item=new LinkedHashMap<String,Object>();//保留插入顺序
		newResult.add(item);
		
		qr.setCount(1l);
		qr.setPageIndex(1);
		qr.setPageSize(1);
		qr.setResultList(newResult);
		
		if(tableQueryResult==null || tableQueryResult.getResultList()==null || tableQueryResult.getResultList().size()==0){
			tableQueryResult.setFields(null);
			return qr;
		}
		
		List<Map<String, Object>> resultList= tableQueryResult.getResultList();
		
		
		for(int i=0;i<resultList.size();i++){
			//Map<String,Object> currentItem=new HashMap<String, Object>();
			String propName=resultList.get(i).get("prop_name").toString();
			Object propValue=resultList.get(i).get("prop_value");
			item.put(propName, propValue);
		}
		
		
		List<ShowDataConfig> fields=new ArrayList<ShowDataConfig>();
		
		ShowDataConfig sdf_prop_name=new ShowDataConfig();
		
		ShowDataType sdt_prop_name=new ShowDataType();
		sdt_prop_name.setCode("P");
		sdf_prop_name.setShowDataType(sdt_prop_name);
		
		ShowDataConfig sdf_prop_value=new ShowDataConfig();
		ShowDataType sdt_prop_value=new ShowDataType();
		sdt_prop_value.setCode("P");
		sdf_prop_value.setShowDataType(sdt_prop_value);
		
		fields.add(sdf_prop_name);
		fields.add(sdf_prop_value);
		
		qr.setFields(fields);
		
		
		return qr;
	}
	
	
}
