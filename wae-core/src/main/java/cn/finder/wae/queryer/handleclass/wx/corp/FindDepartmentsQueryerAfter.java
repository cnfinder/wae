package cn.finder.wae.queryer.handleclass.wx.corp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.finder.httpcommons.utils.JsonUtils;
import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.queryer.handleclass.QueryerAfterClass;
import cn.finder.wae.wx.data.WXAppInfo;
import cn.finder.wx.corp.domain.Department;
import cn.finder.wx.corp.response.FindDepartmentResponse;
import cn.finder.wx.service.WXService;

/***
 * 获取微信企业号下的部门信息
 * @author whl
 *
 */
public class FindDepartmentsQueryerAfter implements QueryerAfterClass {
 
	private Logger logger=Logger.getLogger(FindDepartmentsQueryerAfter.class);
	@Override
	public TableQueryResult handle(TableQueryResult tableQueryResult,
			long showTableConfigId, QueryCondition<Object[]> condition) {
		
		
		tableQueryResult= new TableQueryResult();
		
		 	
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
		Integer department_id=null;
		
		String corpid="";
		try {
			department_id =Integer.valueOf(data.get("department_id").toString());
			
			corpid=data.get("wx_appid").toString();
			
		} catch (Exception e) {
			department_id=null;
		}
		
		 	
	 	tableQueryResult.setPageIndex(condition.getPageIndex());
	 	tableQueryResult.setPageSize(condition.getPageSize());
	 	tableQueryResult.setCount(1l);
	 	WXService.CorpService service =new WXService.CorpService();
	 	
		String accessToken=WXAppInfo.getCorpAccessTokenInfo(corpid).getAccessToken();
	 	
	 	FindDepartmentResponse departmentResp= service.findDepartment(accessToken, department_id);
	 	
		if(departmentResp.isSuccess()){
			
			if(departmentResp.getDepartment()!=null){
				tableQueryResult.setCount(Long.valueOf(departmentResp.getDepartment().size()));
				
				/*String jsonStr=JsonUtils.getJsonString4JavaPOJO(departmentResp.getDepartment());
				
				@SuppressWarnings("unchecked")
				List<Map<String,Object>> resultList= JsonUtil.getList4Json(jsonStr, ArrayList.class);*/
				
				List<Map<String,Object>> resultList=new ArrayList<Map<String,Object>>();
				for(int i=0;i<departmentResp.getDepartment().size();i++){
					Department d=departmentResp.getDepartment().get(i);
					
					
					Map<String,Object> item=JsonUtils.getMap4Json(JsonUtils.getJsonString4JavaPOJO(d));
					resultList.add(item);
					
				}
				
				
				tableQueryResult.setResultList(resultList);
			}
			
		}
	 	
	 	return tableQueryResult;
	 	
		
	}
	
}
