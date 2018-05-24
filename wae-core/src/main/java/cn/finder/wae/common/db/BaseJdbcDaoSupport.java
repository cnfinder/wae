package cn.finder.wae.common.db;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;

import cn.finder.wae.jdbc.wrapper.SimpleJdbcDaoSupport;




public abstract class BaseJdbcDaoSupport extends SimpleJdbcDaoSupport
{


	protected  DBType dbType;
	//这个里面有分页的共同的方法接口
	
	
	/***
	 * 提供单个对象 的返回  ， 因为queryForObject不可行
	 */
	public <T> T queryForSingle(String sql,Object[] args,RowMapper<T> rowMapper)
	{
		List<T> results=getJdbcTemplate().query(sql, args, rowMapper);
		if(results!=null && results.size()==0)
		{
			return null;
		}
		else{
			return results.get(0);
		}
	}

	public <T> T queryForSingle(String sql,RowMapper<T> rowMapper)
	{
		return queryForSingle(sql,null,rowMapper);
	}
	
	
	public <T> T queryForObject(String sql,Object[] args,Class<T> clazz)
	{
		T result;
		try{
			result=getJdbcTemplate().queryForObject(sql, args, clazz);
		}
		catch(DataAccessException exp){
			result=null;
		}
		return result;
	}
	public <T> T queryForObject(String sql,Class<T> clazz)
	{
		return queryForObject(sql,null,clazz);
	}
	
	public <T> List<T> queryForList(String sql,Object[] params,Class<T> clazz)
	{
		List<T> res =null;
		try{
			res=getJdbcTemplate().queryForList(sql,params,clazz);
		}
		catch(Exception e){
			res=null;
		}
		return res;
	}
	public  List<Map<String,Object>> queryForList(String sql,Object[] params)
	{
		List<Map<String,Object>> res =null;
		try{
			res=getJdbcTemplate().queryForList(sql,params);
		}
		catch(Exception e){
			res=null;
		}
		return res;
	}
	
	
	public Map<String,Object> queryForMap(String sql,Object[] args)
	{
		Map<String,Object> result=null;
		try{
			result=getJdbcTemplate().queryForMap(sql, args);
		}
		catch(Exception exp){
			result=null;
		}
		return result;
	}
	
	
	
	
	public void setDbType(DBType dbType) {
		this.dbType = dbType;
	}
	
	public void setJdbcDataSource(DataSource dataSource)
	{
		setDataSource(dataSource);
		
		BasicDataSource basicDataSource = (BasicDataSource)dataSource;
		String driverClassName = basicDataSource.getDriverClassName();
		//判断数据库类型
		if(driverClassName.toLowerCase().indexOf("mysql")!=-1){
			setDbType(DBType.MySql);
		}else if(driverClassName.toLowerCase().indexOf("sqlserver")!=-1){
			
			setDbType(DBType.SqlServer);
		}
		else if(driverClassName.toLowerCase().indexOf("oracle")!=-1){
			
			setDbType(DBType.Oracle);
		}
		
	}
	
	
	
	
	

	/****
	 * 包装多数据源SQL入口
	 * @param sql
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	protected String wrapperPagerSql(String sql, int pageIndex, int pageSize) {
		String retString="";
		if(dbType == DBType.MySql){
			retString= wrapperPagerSqlForMysql(sql,pageIndex,pageSize);
			
		}else if(dbType == DBType.SqlServer){
			retString= wrapperPagerSqlForSqlserver(sql,pageIndex,pageSize);
		}else if(dbType == DBType.Oracle){
			retString= wrapperPagerSqlForOracle(sql,pageIndex,pageSize);
		}
		return retString;
	}
	
	
	/***
	 * Mysql包装
	 * @param sql
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	protected String wrapperPagerSqlForMysql(String sql, int pageIndex, int pageSize) {
		//sql_sb.append(" limit ").append((condition.getPageIndex()-1)*condition.getPageSize()).append(",").append(condition.getPageSize());
		String sql_total = sql;
		StringBuffer sql_pager = new StringBuffer();
		sql_pager.append(sql_total).append(" limit ").append((pageIndex-1)*pageSize).append(",").append(pageSize);

		return sql_pager.toString();
	}
	
	
	
	/***
	 * Oracle包装
	 * @param sql
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	protected String wrapperPagerSqlForOracle(String sql, int pageIndex, int pageSize) {

		String sql_total = sql;
		StringBuffer sql_pager = new StringBuffer();
		sql_pager.append("select * from ( select t.*,rownum rn from (")
				.append(sql_total).append(") t ").append("  ").append(" where rownum <=")
				.append(pageIndex * pageSize).append(")").append(" where rn >")
				.append((pageIndex - 1) * pageSize);

		return sql_pager.toString();
	}
	
	
	
	/***
	 * SqlServer 包装
	 * @param sql
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	protected String wrapperPagerSqlForSqlserver(String sql, int pageIndex, int pageSize) {

		String sql_total = sql.replaceFirst("select ", "select top 999999999999999 ");
		StringBuffer sql_pager = new StringBuffer();
		sql_pager.append("select * from (")
			.append(" select row_number()over(order by TempColmun) rowNumber,* from (")
			.append(" select top ").append(pageIndex*pageSize).append(" 0 TempColmun,* from (")
			.append(sql_total).append(")t1 ) t2) t3 ")
			.append("where rowNumber>").append((pageIndex-1)*pageSize);

		return sql_pager.toString();
	}
}
