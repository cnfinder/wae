package cn.finder.wae.queryer.handleclass;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.business.module.common.service.CommonService;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.common.comm.AppApplicationContextUtil;

public class RoleAddQueryerAfter extends QueryerDBAfterClass {


	CommonService commonService =AppApplicationContextUtil.getContext().getBean("commonService", CommonService.class);
	
	
	
	
	@Override
	public TableQueryResult handle(TableQueryResult tableQueryResult,
			long showTableConfigId, QueryCondition<Object[]> condition) {
		// TODO Auto-generated method stub
		  super.handle(tableQueryResult, showTableConfigId, condition);
		  
		  
		  @SuppressWarnings("unchecked")
			Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
		  String roleName = data.get("name").toString();
		 //获取角色ID  根据  name
		  String role_sql = "select id from t_role where name = ?";
		 long roleId = queryForLong(role_sql,new Object[]{roleName});
		  
		  String addrole_default_request_auth_values = ArchCache.getInstance().getSysConfigCache().get("config_addrole_default_request_auth").getValue();
		  
		  //插入默认的角色到请求 权限
		  commonService.addRoleRequest(roleId, StringUtils.split(addrole_default_request_auth_values,","));
		  
		  
		  return tableQueryResult;
	}
	
	
	

	
}
