package cn.finder.wae.queryer.handleclass.flow;

import java.util.List;
import java.util.Map;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.iron.activiti.ActivitiServiceManager;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.common.comm.AppApplicationContextUtil;
import cn.finder.wae.queryer.handleclass.QueryerDBAfterClass;

/**
 * @author: wuhualong
 * @data:
 * @function: 清空所有流程实例数据
 */
public class ProcessInstClearAllAfterQueryer  extends QueryerDBAfterClass{
	
	
	/* (non-Javadoc)
	 * @see cn.finder.wae.queryer.handleclass.QueryerAfterClass#handle(cn.finder.wae.business.domain.TableQueryResult, long, cn.finder.ui.webtool.QueryCondition)
	 */
	@Override
	public TableQueryResult handle(TableQueryResult tableQueryResult,long showTableConfigId,QueryCondition<Object[]> condition){
		super.handle(tableQueryResult, showTableConfigId, condition);
		
		StringBuffer sb = new StringBuffer();
		sb.append("delete from  ACT_RU_VARIABLE;");
		sb.append("delete from  ACT_RU_JOB;");
		sb.append("delete from ACT_HI_VARINST;");
		sb.append("delete from  ACT_HI_TASKINST;");
		sb.append("delete from  ACT_HI_PROCINST;");
		sb.append("delete from  ACT_HI_IDENTITYLINK;");
		sb.append("delete from  ACT_HI_DETAIL;");
		sb.append("delete from  ACT_HI_COMMENT;");
		sb.append("delete from  ACT_HI_ACTINST;");
		sb.append("delete from  ACT_RU_IDENTITYLINK;");
		sb.append("delete from  ACT_RU_TASK;");
		sb.append("delete from ACT_RU_EXECUTION;");


		getJdbcTemplate().update(sb.toString());


	   
       List<Map<String,Object>> list = new java.util.ArrayList<Map<String,Object>>();
       tableQueryResult.setCount(0l);
       tableQueryResult.setPageSize(10);
       tableQueryResult.setPageIndex(1);
       tableQueryResult.setResultList(list);
       tableQueryResult.getMessage().setMsg("流程数据清空成功");
       
	      
		return tableQueryResult;
	}
	
	
	
	

}
