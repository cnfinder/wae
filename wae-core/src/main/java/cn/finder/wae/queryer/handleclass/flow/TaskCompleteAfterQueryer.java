package cn.finder.wae.queryer.handleclass.flow;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.iron.activiti.ActivitiServiceManager;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.common.comm.AppApplicationContextUtil;
import cn.finder.wae.common.comm.JsonUtil;
import cn.finder.wae.queryer.handleclass.QueryerDBAfterClass;

/**
 * @author: wuhualong
 * @data:
 * @function: 任务完成收操作处理
 */
public class TaskCompleteAfterQueryer  extends QueryerDBAfterClass{
	
	
	/* (non-Javadoc)
	 * @see cn.finder.wae.queryer.handleclass.QueryerAfterClass#handle(cn.finder.wae.business.domain.TableQueryResult, long, cn.finder.ui.webtool.QueryCondition)
	 */
	@Override
	public TableQueryResult handle(TableQueryResult tableQueryResult,long showTableConfigId,QueryCondition<Object[]> condition){
		super.handle(tableQueryResult, showTableConfigId, condition);
		
		
		
		@SuppressWarnings("unchecked")
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
		
		Map<String,Object> variables = new HashMap<String, Object>();
		
		String taskId =data.get("taskId").toString();
		String paramString="";
		try {
			paramString = data.get("paramString").toString();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			
		}
		
		variables = findVariables(paramString);
		
		
	   ActivitiServiceManager activitiServiceManager=AppApplicationContextUtil.getContext().getBean("activitiServiceManager", ActivitiServiceManager.class);
	   
	   try{
		    activitiServiceManager.getTaskService().complete(taskId,variables);
		    tableQueryResult.getMessage().setMsg("任务处理完成");
		}catch(Exception e)
		{
			 logger.error("error on complete task {"+taskId+"}, variables={"+variables+"}"+e);
			 tableQueryResult.getMessage().setMsg("任务处理失败");
		}
	   
	   
	   
	   
       List<Map<String,Object>> list = new java.util.ArrayList<Map<String,Object>>();
       tableQueryResult.setCount(1l);
       tableQueryResult.setPageSize(1);
       tableQueryResult.setPageIndex(1);
       tableQueryResult.setResultList(list);
       
       
	      
		return tableQueryResult;
	}
	
	/****
	    * 获取请求变量数据  act_data
	    * 数据类型:dataType: int  long  bool string date 默认为string  然后通过参数  act_data进行传输为 JSON数组
				//[{pass:true,dataType:"int"}]或者 dataType可能不需要传入
	    * @return
	    */
	  /* private Map<String,Object> findVariables(){
		   HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder
					.getRequestAttributes()).getRequest();
		   Map<String, Object> variables=new HashMap<String, Object>();
			try
			{
				
				String reqData = request.getParameter("act_data");
				return findVariables(reqData);
			}
			catch(Exception e)
			{
				
			}
			
			
			return variables;
	   }
	   */
	   
	   private Map<String,Object> findVariables(String reqData){
		   Map<String, Object> variables=new HashMap<String, Object>();
			try
			{
				
				if(!StringUtils.isEmpty(reqData)){
					reqData = URLDecoder.decode(reqData,"UTF-8");
					JSONArray reqDataJA = JSONArray.fromObject(reqData);
					if(reqDataJA!=null && reqDataJA.size()>0){
						
						for(int i=0;i<reqDataJA.size();i++){
							
							JSONObject jo = (JSONObject)reqDataJA.get(i);
							
							Map<String, Object> mapData = JsonUtil.getMap4Json(jo.toString());
							
							String dataTypeName = (String)mapData.get("dataType");
							
							
							Set<String> keyset = mapData.keySet();
							Iterator<String> ite = keyset.iterator();
							while(ite.hasNext()){
								String key = ite.next();
								if(!"dataType".equalsIgnoreCase(key)){
									if(StringUtils.isEmpty(dataTypeName)){
										variables.put(key, mapData.get(key));
		 							}else{
		 								
		 								variables.put(key, mapData.get(key));
		 							}
								}
									
							}
							
							
						}
					}
					
				}
			}
			catch(Exception e)
			{
				
			}
			
			
			return variables;
	   }

	
	

}
