package cn.finder.wae.business.module.sys.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;

import cn.finder.wae.business.domain.RequestCommand;
import cn.finder.wae.business.domain.Role;
import cn.finder.wae.business.domain.RoleMenu;
import cn.finder.wae.business.domain.RoleReqCommand;
import cn.finder.wae.business.module.sys.dao.RoleDao;
import cn.finder.wae.business.rowmapper.RowMapperFactory;
import cn.finder.wae.common.db.BaseJdbcDaoSupport;
import cn.finder.wae.controller.servlet.ArchCacheServlet;

public class RoleDaoImpl  extends BaseJdbcDaoSupport implements RoleDao{
	private static Logger logger = Logger.getLogger(ArchCacheServlet.class);
	@Override
	public List<Role> findAll() {
		String sql = "select * from v_role";
		List<Role> list = getJdbcTemplate().query(sql, new RowMapperFactory.RoleRowMapper());
		return list;
		
		
	}


	@Override
	public Role findById(Long roleId) {
		String sql = "select * from v_role where id = ?";
		List<Role> list = getJdbcTemplate().query(sql, new Object[]{roleId}, new RowMapperFactory.RoleRowMapper());
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
		
	}

	@Override
	public List<RequestCommand> findReqCommdsByRoleId(Long roleId) {
		String sql = "select * from v_request_command where id in (select request_command_id from t_role_request_command where role_id = ?)";
		List<RequestCommand> list = getJdbcTemplate().query(sql,new Object[]{roleId}, new RowMapperFactory.RequestCommandRowMapper());
		return list;
	}

	@Override
	public List<RequestCommand> findAllReqCommds() {
		String sql = "select * from v_request_command";
		List<RequestCommand> list = getJdbcTemplate().query(sql,new RowMapperFactory.RequestCommandRowMapper());
		return list;
	}


	@Override
	public List<RoleReqCommand> findAllRoleReqCommd() {

		String sql="select rrc.*,rc.name request_command_name,rc.command,rc.remark from t_role_request_command rrc join t_request_command rc on rrc.request_command_id=rc.id";
		
		return getJdbcTemplate().query(sql,  new RowMapperFactory.RoleReqCommandRowMapper());
	}
	
	@Override
	public List<RoleMenu> findMenusByRoleId(Long roleId) {
		String sql = "select * from v_role_menu where role_id = ?";
		List<RoleMenu> list = getJdbcTemplate().query(sql, new Object[]{roleId}, new RowMapperFactory.RoleMenuRowMapper());
		return list;
	}


	@Override
	public Role findByRoleCode(String roleCode) {
		String sql = "select * from v_role where role_code = ?";
		List<Role> list = getJdbcTemplate().query(sql, new Object[]{roleCode}, new RowMapperFactory.RoleRowMapper());
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}
	

	
}
