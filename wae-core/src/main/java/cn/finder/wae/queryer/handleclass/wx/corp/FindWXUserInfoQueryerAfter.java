package cn.finder.wae.queryer.handleclass.wx.corp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.queryer.handleclass.QueryerAfterClass;
import cn.finder.wae.wx.data.WXAppInfo;
import cn.finder.wx.corp.domain.Department;
import cn.finder.wx.corp.response.FindDepartmentResponse;
import cn.finder.wx.corp.response.FindUserResponse;
import cn.finder.wx.service.WXService;

/***
 * 获取微信企业号用户信息
 * @author whl
 *
 */
public class FindWXUserInfoQueryerAfter implements QueryerAfterClass {
 
	private Logger logger=Logger.getLogger(FindWXUserInfoQueryerAfter.class);
	@Override
	public TableQueryResult handle(TableQueryResult tableQueryResult,
			long showTableConfigId, QueryCondition<Object[]> condition) {
		
		
		tableQueryResult= new TableQueryResult();
		
		 	
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
		String user_id=null;
		List<Map<String,Object>>  list=new ArrayList<Map<String,Object>>();
		
		Map<String,Object> item=new HashMap<String, Object>();
		list.add(item);
		
		tableQueryResult.setResultList(list);
		
		
		String corpid="";
		try {
			user_id =data.get("user_id").toString();
			
			corpid=data.get("wx_appid").toString();
			
			
			tableQueryResult.setPageIndex(condition.getPageIndex());
		 	tableQueryResult.setPageSize(condition.getPageSize());
		 	tableQueryResult.setCount(1l);
		 	WXService.CorpService service =new WXService.CorpService();
		 	
			String accessToken=WXAppInfo.getCorpAccessTokenInfo(corpid).getAccessToken();
		 	
		 	FindUserResponse  userResp= service.findUser(accessToken, user_id);
		    logger.info("======service.findUser(accessToken, user_id);:"+userResp.getBody());
		 	item.put("useid", userResp.getUserid());
		 	item.put("name", userResp.getName());
		 	item.put("mobile", userResp.getMobile());
		 	item.put("department_ids", userResp.getDepartment());
		    Integer[] depts=userResp.getDepartment();
		    if(depts!=null){
		    	
		    	List<Department>  deptList=new ArrayList<Department>();
		    	
		    	for(int i=0;i<depts.length;i++){
		    		
		    		try{
			    		FindDepartmentResponse deptResp= service.findDepartment(accessToken, depts[i]);
			    		
			    	   Department dept=	deptResp.getDepartment().get(0);
			    	  
			    	   deptList.add(dept);
		    		}
		    		catch(Exception e){
		    			
		    		}
		    	
		    	}
		    	 item.put("depts", deptList);
		    	
		    }
		    
		    
			
		} catch (Exception e) {
		}
		
		 	
	 
	 	
	 	return tableQueryResult;
	 	
		
	}
	
}
