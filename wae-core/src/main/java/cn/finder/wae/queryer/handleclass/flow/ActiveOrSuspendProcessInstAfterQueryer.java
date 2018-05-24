package cn.finder.wae.queryer.handleclass.flow;

import java.util.Map;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.common.comm.AppApplicationContextUtil;
import cn.finder.wae.queryer.handleclass.QueryerDBAfterClass;
import cn.iron.activiti.ActivitiServiceManager;

/**
 * @author: wuhualong
 * @data:
 * @function: 激活或者挂起流程实例
 */
public class ActiveOrSuspendProcessInstAfterQueryer  extends QueryerDBAfterClass{
	
	
	ActivitiServiceManager activitiServiceManager=AppApplicationContextUtil.getContext().getBean("activitiServiceManager", ActivitiServiceManager.class);
	
	
	
	@Override
	public TableQueryResult handle(TableQueryResult tableQueryResult,long showTableConfigId,QueryCondition<Object[]> condition){
		super.handle(tableQueryResult, showTableConfigId, condition);
		
		
		
		@SuppressWarnings("unchecked")
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
		
		
		String processInstanceId =data.get("processInstanceId").toString();
		String status=data.get("status").toString();
		try {
			
			if("1".equals(status)){
				activitiServiceManager.getRuntimeService().activateProcessInstanceById(processInstanceId);
			}
			else{
				activitiServiceManager.getRuntimeService().suspendProcessInstanceById(processInstanceId);
			}
		} catch (Exception e) {
			logger.error("激活或者挂起异常!", e);
		}
		
	   
	   
     
       tableQueryResult.setCount(1l);
       tableQueryResult.setPageSize(1);
       tableQueryResult.setPageIndex(1);
       
       
	      
		return tableQueryResult;
	}
	
	

}
