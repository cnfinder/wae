package cn.finder.wae.business.module.sys.dao;

import java.util.List;

import cn.finder.wae.business.domain.RequestCommand;
import cn.finder.wae.business.domain.Role;
import cn.finder.wae.business.domain.RoleMenu;
import cn.finder.wae.business.domain.RoleReqCommand;

public interface RoleDao {
	public List<Role> findAll();
	
	
	public Role findById(Long roleId);
	
	public List<RequestCommand> findReqCommdsByRoleId(Long roleId);
	
	public List<RequestCommand> findAllReqCommds();
	
	
	public List<RoleReqCommand> findAllRoleReqCommd();
	
	public List<RoleMenu> findMenusByRoleId(Long roleId);
	
	public Role findByRoleCode(String roleCode);
}
