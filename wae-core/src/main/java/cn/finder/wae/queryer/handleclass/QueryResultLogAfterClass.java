package cn.finder.wae.queryer.handleclass;

import java.util.Date;
import java.util.Map;

import javax.sql.DataSource;

import cn.finder.common.util.JsonUtil;
import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.Message;
import cn.finder.wae.business.domain.ServiceInterfaceLog;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.business.module.common.service.CommonService;
import cn.finder.wae.common.comm.AppApplicationContextUtil;

/**
 * @author: wuhualong
 * @data:2014-1011
 * @function:查询结果的日志处理
 */
public class QueryResultLogAfterClass extends QueryerDBAfterClass {

	@Override
	public TableQueryResult handle(TableQueryResult tableQueryResult,
			long showTableConfigId, QueryCondition<Object[]> condition) {
		 super.handle(tableQueryResult, showTableConfigId, condition);
		 
		 CommonService commonService=AppApplicationContextUtil.getContext().getBean("commonService", CommonService.class);
		
		 
		 @SuppressWarnings("unchecked")
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
			
		 
		 ServiceInterfaceLog log =new ServiceInterfaceLog();
		 log.setInvokeTime(new Date());
		 log.setStatusCode(Message.StatusCode_OK+"");
		 log.setOutMsg(JsonUtil.getJsonString4JavaPOJO(data));
		 commonService.addServiceInterfaceLog(log);
		 
		 return tableQueryResult;
		 
	}

	@Override
	public void setJDBCDataSource(DataSource dataSource) {
		super.setJdbcDataSource(dataSource);
	}

	
}
