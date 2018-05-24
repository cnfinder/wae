package cn.finder.wae.business.module.common.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import cn.finder.wae.business.domain.TaskPlan;
import cn.finder.wae.business.module.common.dao.TaskPlanDao;
import cn.finder.wae.business.rowmapper.RowMapperFactory;
import cn.finder.wae.common.db.BaseJdbcDaoSupport;

public class TaskPlanDaoImpl extends BaseJdbcDaoSupport implements TaskPlanDao {

	@Override
	public List<TaskPlan> list() {
		String sql ="select * from v_taskplan";
		return getJdbcTemplate().query(sql, new RowMapperFactory.TaskPlanRowMapper());
	}

	@Override
	public int resetStatus(long taskPlanId) {
		String sql ="{call proc_taskplan_resetstatus(?)}";
		return getJdbcTemplate().update(sql, new Object[]{taskPlanId});
	}

	@Override
	public void updateTaskPlanUpdate(final List<TaskPlan> taskPlans) {
		
		
		String sql ="{call proc_taskplan_resetupdate (?)}";
		
		final int size = taskPlans.size();
		
		BatchPreparedStatementSetter batchSetter = new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setObject(1, taskPlans.get(i).getId());
				
			}
			
			@Override
			public int getBatchSize() {
				return size;
			}
		};
				
			
		 getJdbcTemplate().batchUpdate(sql, batchSetter);
		
	}

	@Override
	public void updateStatusToRunForStartWithServer() {
		// TODO Auto-generated method stub
		String sql = "update t_taskplan set status =1 where start_with_server = 1";
		
		getJdbcTemplate().update(sql);
	}

	@Override
	public int updateParamValue(long taskPlanId, String param_name, String param_value) {
		
		String sql="update t_taskplan set params=? where id=?";
		
		TaskPlan item= findTaskPlan(taskPlanId);
		Map<String,Object> param=item.getMapPara();
		param.put(param_name, param_value);
		
		//转成字符串参数
		StringBuffer sb=new StringBuffer();
		Set<Entry<String,Object>> setEntry=param.entrySet();
		
		Iterator<Entry<String,Object>> itr= setEntry.iterator();
		while(itr.hasNext()){
			Entry<String,Object> entry=itr.next();
			String key=entry.getKey();
			Object value=entry.getValue();
			if(!StringUtils.isEmpty(sb.toString())){
				sb.append(";");
			}
			sb.append(key).append("=").append(value);
		}
		
		
		int cnt=getJdbcTemplate().update(sql,new Object[]{sb.toString(),taskPlanId});
		
		return cnt;
	}

	@Override
	public TaskPlan findTaskPlan(long taskPlanId) {
		String sql="select * from t_taskplan where id=?";
		return queryForSingle(sql, new Object[]{taskPlanId},new RowMapperFactory.TaskPlanRowMapper());
	}

}
