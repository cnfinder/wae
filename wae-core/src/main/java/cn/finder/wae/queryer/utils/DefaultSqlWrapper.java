package cn.finder.wae.queryer.utils;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

import cn.finder.wae.business.domain.ShowTableConfig;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.common.db.ds.DSManager;

public class DefaultSqlWrapper {

	private long showtableConfigId;
	
	
	
	public DefaultSqlWrapper(long showtableConfigId)
	{
		this.showtableConfigId = showtableConfigId;
	}
	
	public String wrapperPagerSql(String sql, int pageIndex, int pageSize) {
		String retString="";
		
		ShowTableConfig showTableConfig=ArchCache.getInstance().getShowTableConfigCache().get(showtableConfigId);
		DataSource ds = DSManager.getDataSource(showTableConfig.getTargetDs());
		BasicDataSource basicDataSource = (BasicDataSource)ds;
		String driverClassName = basicDataSource.getDriverClassName();
		//判断数据库类型
		if(driverClassName.toLowerCase().indexOf("mysql")!=-1){
			retString= wrapperPagerSqlForMysql(sql,pageIndex,pageSize);
		}else if(driverClassName.toLowerCase().indexOf("sqlserver")!=-1){
			retString= wrapperPagerSqlForSqlserver(sql,pageIndex,pageSize);
		}
		else if(driverClassName.toLowerCase().indexOf("oracle")!=-1){
			retString= wrapperPagerSqlForOracle(sql,pageIndex,pageSize);
			
		}
		
		return retString;
	}
	
	
	public String wrapperPagerSqlForMysql(String sql, int pageIndex, int pageSize) {
		//sql_sb.append(" limit ").append((condition.getPageIndex()-1)*condition.getPageSize()).append(",").append(condition.getPageSize());
		String sql_total = sql;
		StringBuffer sql_pager = new StringBuffer();
		sql_pager.append(sql_total).append(" limit ").append((pageIndex-1)*pageSize).append(",").append(pageSize);

		return sql_pager.toString();
	}
	
	public String wrapperPagerSqlForOracle(String sql, int pageIndex, int pageSize) {

		String sql_total = sql;
		StringBuffer sql_pager = new StringBuffer();
		sql_pager.append("select * from ( select t.*,rownum rn from (")
				.append(sql_total).append(") t ").append(" where rownum <=")
				.append(pageIndex * pageSize).append(")").append(" where rn >")
				.append((pageIndex - 1) * pageSize);

		return sql_pager.toString();
	}
	
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
