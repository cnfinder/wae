package cn.finder.wae.queryer.handleclass.wx.corp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.finder.common.util.JsonUtil;
import cn.finder.httpcommons.utils.JsonUtils;
import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.Message;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.business.module.common.service.CommonService;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.common.thread.AppContent;
import cn.finder.wae.queryer.handleclass.QueryerAfterClass;
import cn.finder.wae.wx.data.WXAppInfo;
import cn.finder.wx.corp.domain.Department;
import cn.finder.wx.corp.domain.User;
import cn.finder.wx.corp.request.FindDepartmentUserRequest;
import cn.finder.wx.corp.response.FindDepartmentResponse;
import cn.finder.wx.corp.response.FindDepartmentUserResponse;
import cn.finder.wx.service.WXService;

/***
 * 获取微信企业号下的部门下用户信息 
 * @author whl
 *
 */
public class FindDepartmentUsersQueryerAfter implements QueryerAfterClass {
 
	private Logger logger=Logger.getLogger(FindDepartmentUsersQueryerAfter.class);
	@Override
	public TableQueryResult handle(TableQueryResult tableQueryResult,
			long showTableConfigId, QueryCondition<Object[]> condition) {
		
		
		tableQueryResult= new TableQueryResult();
		CommonService commonService= WebApplicationContextUtils.getWebApplicationContext(AppContent.getSession().getServletContext()).getBean("commonService", CommonService.class);
		 	
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
		Integer department_id=null;
		
		String corpid;
		
		try {
			department_id =Integer.valueOf(data.get("department_id").toString());
			corpid=data.get("wx_appid").toString();
			
			tableQueryResult.setPageIndex(condition.getPageIndex());
		 	tableQueryResult.setPageSize(condition.getPageSize());
				
		 	
		 	WXService.CorpService service =new WXService.CorpService();
			
		 	
		 	String accessToken=WXAppInfo.getCorpAccessTokenInfo(corpid).getAccessToken();
		 	
		 	FindDepartmentUserRequest req=new FindDepartmentUserRequest();
		 	req.setDepartment_id(department_id);
		 	req.setFetch_child(FindDepartmentUserRequest.FETCH_CHILD_YES);
		 	req.setStatus(FindDepartmentUserRequest.STATUS_ALL);
		 	tableQueryResult.setCount(0l);
		 	List<User> users= service.findDepartmentUsers(accessToken, req);
		 	
				
			if(users!=null){
				tableQueryResult.setCount(Long.valueOf(users.size()));
				
				
				List<Map<String,Object>> resultList=new ArrayList<Map<String,Object>>();
				for(int i=0;i<users.size();i++){
					User user=users.get(i);
					Map<String,Object> item=JsonUtils.getMap4Json(JsonUtils.getJsonString4JavaPOJO(user));
					resultList.add(item);
				}
				
				tableQueryResult.setResultList(resultList);
			}
			
		} catch (Exception e) {
			department_id=null;
			
			tableQueryResult.getMessage().setStatusCode(Message.StatusCode_OK);
			tableQueryResult.getMessage().setMsg("部门department_id 获取或者 wx_appid失败");
		}
		 	
	 	
	 	
	 	return tableQueryResult;
	 	
		
	}
	
}
