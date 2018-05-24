package cn.finder.wae.queryer.handleclass;

import java.util.HashMap;
import java.util.Map;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.domain.User;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.business.module.auth.service.AuthService;
import cn.finder.wae.common.comm.AppApplicationContextUtil;
import cn.finder.wae.common.comm.MD5Util;
import cn.finder.wae.queryer.handleclass.QueryerDBAfterClass;

/***
 * 密码修改
 * @author xiaoht
 *
 */
public class UpdateUserPwdQueryerAfter extends QueryerDBAfterClass {

	AuthService authService =AppApplicationContextUtil.getContext().getBean("authService", AuthService.class);
	@Override
	public TableQueryResult handle(TableQueryResult tableQueryResult,
			long showTableConfigId, QueryCondition<Object[]> condition) {
		
		super.handle(tableQueryResult, showTableConfigId, condition);
		@SuppressWarnings("unchecked")
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
		String account = data.get("account").toString();
		String oldPwd = data.get("oldPwd").toString();
		String newPwd = data.get("newPwd").toString();
		String oldPwdMD5 = MD5Util.getMD5(oldPwd);
		String newPwdMD5 = MD5Util.getMD5(newPwd);
		
		User user = authService.findByAccount(account);
		Map<String,Object> map = new HashMap<String,Object>();
		if(user.getPassword().equals(oldPwdMD5)){
			String user_sql = "update t_user set password = ? where account = ?";
			authService.addUser(user_sql, new Object[]{newPwdMD5,account});
			map.put("msg","密码修改成功！");
		}else{
			map.put("msg","原始密码不正确！");
		}
		tableQueryResult.getResultList().add(map);
		return tableQueryResult;
		
	}
	
	
	
}
