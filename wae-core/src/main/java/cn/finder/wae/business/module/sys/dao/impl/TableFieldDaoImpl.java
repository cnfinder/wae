package cn.finder.wae.business.module.sys.dao.impl;

import java.util.List;

import cn.finder.wae.business.domain.Role;
import cn.finder.wae.business.domain.TableField;
import cn.finder.wae.business.module.sys.dao.TableFieldDao;
import cn.finder.wae.business.rowmapper.RowMapperFactory;
import cn.finder.wae.common.db.BaseJdbcDaoSupport;

public class TableFieldDaoImpl  extends BaseJdbcDaoSupport  implements TableFieldDao{

	@Override
	public List<TableField> findAll() {
		String sql = "SELECT * FROM information_schema.columns WHERE table_schema = 'psis'";
		List<TableField> list = getJdbcTemplate().query(sql, new RowMapperFactory.TableFieldRowMapper());
		return list;
	}

}
