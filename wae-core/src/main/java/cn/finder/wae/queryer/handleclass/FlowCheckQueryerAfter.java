package cn.finder.wae.queryer.handleclass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



import net.sf.json.JSONArray;
import net.sf.json.JSONObject;




import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;

import cn.finder.wae.queryer.handleclass.QueryerDBAfterClass;

/**
 * @author: wuhualong
 * @data:2014-4-23下午2:15:59
 * @function:生成设备 槽位  参数验证
 */
public class FlowCheckQueryerAfter  extends QueryerDBAfterClass {

	/* (non-Javadoc)
	 * @see cn.finder.wae.queryer.handleclass.QueryerAfterClass#handle(cn.finder.wae.business.domain.TableQueryResult, long, cn.finder.ui.webtool.QueryCondition)
	 */
	@Override
	public TableQueryResult handle(TableQueryResult tableQueryResult,long showTableConfigId,QueryCondition<Object[]> condition){
		super.handle(tableQueryResult, showTableConfigId, condition);
		
		
		
		if(tableQueryResult==null || tableQueryResult.getResultList()==null || tableQueryResult.getResultList().size()==0){
			tableQueryResult.setFields(null);
			return tableQueryResult;
		}
		
		List<Map<String, Object>> resultList= tableQueryResult.getResultList();
		
		String data =(String) (resultList.get(0).get("flow_key"));
		
		
		
		String sql  = "select count(*) from ACT_RE_PROCDEF where KEY_ = ?";
		int cnt = queryForInt(sql,data);
		
		if(cnt > 0){
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("isExist", "1");
			resultList.add(0, map);
			tableQueryResult.setResultList(resultList);
		}
		
		return tableQueryResult;
	}
	
	
	
	 
	
	
}
