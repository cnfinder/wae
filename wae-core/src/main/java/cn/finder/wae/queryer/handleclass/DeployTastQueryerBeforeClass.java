package cn.finder.wae.queryer.handleclass;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.jdbc.core.RowMapper;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.activiti.utils.WorkflowUtils;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.common.comm.AppApplicationContextUtil;
import cn.iron.activiti.ActivitiServiceManager;

/**
 * @author: wuhualong
 * @data:2014-4-23下午2:15:59
 * @function:部署流程
 */
public class DeployTastQueryerBeforeClass extends QueryerDBBeforeClass {

	
	
	@Override
	public void handle(long showTableConfigId,
			QueryCondition<Object[]> condition) {
		
		super.handle(showTableConfigId, condition);

		logger.debug("====================MachineEditQueryerBeforeClass.handle ");
		logger.debug("====================showTableConfigId: "+showTableConfigId);
		
		MapParaQueryConditionDto<String, Object> searchQueryCondition = (MapParaQueryConditionDto<String, Object>)condition;
		String id = searchQueryCondition.getWherepParameterValues()[0].toString();
		
		
		String sql = "select *  from t_flows where id = ? ";
		final Object[] params = {id};
		List<Map<String, Object>>  dataList  = getListData(sql,params);
		
		byte[] filedate = (byte[])dataList.get(0).get("filedata");
		String filename = (String)dataList.get(0).get("fileurl");
		
		InputStream fileInputStream = new ByteArrayInputStream(filedate);
		
		try {
		      
		       Deployment deployment = null;

		       ActivitiServiceManager activitiServiceManager =AppApplicationContextUtil.getContext().getBean("activitiServiceManager", ActivitiServiceManager.class);
		       
		       RepositoryService repositoryService = activitiServiceManager.getRepositoryService();
		       
		       deployment = repositoryService.createDeployment().addInputStream(filename, fileInputStream).deploy();
		       

		       List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).list();

		       for (ProcessDefinition processDefinition : list) {
		         WorkflowUtils.exportDiagramToFile(repositoryService, processDefinition, "/tmp/ActivitiPlus");
		       }

		       String sql_update = "update t_flows set flow_type = ?";
		       getJdbcTemplate().update(sql_update, '1');
		       
		     } catch (Exception e) {
		       logger.error("error on deploy process, because of file input stream", e);
		     }
	}
	
	
	private List<Map<String, Object>> getListData(String sql,Object[] params){
		List<Map<String, Object>>  dataList = getJdbcTemplate().query(sql,params,  
                new RowMapper<Map<String,Object>>() {  

					@Override
					public Map<String, Object> mapRow(java.sql.ResultSet arg0,
							int arg1) throws SQLException {
						Map<String,Object> item = new HashMap<String, Object>();
                    	item.put("filename", arg0.getString("flow_key"));
                    	item.put("filedata", arg0.getBytes("file_data"));
                    	item.put("fileurl", arg0.getString("flow_url"));
                        return item;  
					}

                }); 
		
	return dataList;
	}
	
	

}
