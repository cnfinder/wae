package cn.finder.wae.queryer.handleclass;

import java.io.File;
import java.util.Date;
import java.util.Map;

import javax.sql.DataSource;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.common.comm.Common;
import cn.finder.wae.common.db.BaseJdbcDaoSupport;
import cn.finder.wae.common.db.ds.DSManager;

/**
 * @author: wuhualong
 * @data:2014-7-16上午11:25:21
 * @function:备份数据库  
 */
public class BackupDBAfterClass extends QueryerDBAfterClass {

	@Override
	public TableQueryResult handle(TableQueryResult tableQueryResult,
			long showTableConfigId, QueryCondition<Object[]> condition) {
		 super.handle(tableQueryResult, showTableConfigId, condition);
		 
		
		 
		 @SuppressWarnings("unchecked")
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
			
		 //data.get("")
		 
		final long dsId =  Long.valueOf(data.get("dsId").toString());
		 
		
		/*new Thread(new Runnable(){

			@Override
			public void run() {

				
				
			}}).start();
		*/
		 
		 
		Backup bk =new Backup(dsId,(String)data.get("WebRoot_path"));
		bk.doWork();
		 
	       tableQueryResult.setCount(1l);
	       tableQueryResult.setPageSize(1);
	       tableQueryResult.setPageIndex(1);
	       
	     //  logger.debug(msg);
		 
		 
		 return tableQueryResult;
		 
	}

	@Override
	public void setJDBCDataSource(DataSource dataSource) {
		super.setJdbcDataSource(dataSource);
	}

	
	class Backup extends BaseJdbcDaoSupport
	{
		
		private long dsId;
		private String webRoot_path;
		
		public Backup(long dsId,String webRoot_path)
		{
			this.dsId = dsId;
			this.webRoot_path = webRoot_path;
			Map<Long,DataSource> dsMap = DSManager.getDataSourceMaps();
			DataSource ds = dsMap.get(dsId);
			this.setJdbcDataSource(ds);
			
		}
		
		public void doWork()
		{
			String sql ="";
			
			try
	        {
				String timenow = Common.formatDate(new Date(), "yyyyMMddHHmmss");
				
				String dbName = ArchCache.getInstance().getConstantsCache().get(dsId).getName();
				
				
				
				
				String dbDir = webRoot_path+"copydb\\";
				File file = new File(dbDir);
				if(!file.exists()){
					file.mkdir();
				}
				
				
				String filepath  = dbDir + dbName+timenow+".bak";
				//use master; 这里不能 使用 use master  如果使用  那么就会把数据库切换到 master
	            sql = "backup database "+dbName+" to disk='"+filepath+"' with format,name='full backup of "+dbName+"'";
	            
	            getJdbcTemplate().execute(sql);
	        }
	        //异常
	        catch(Exception e)
	        {
	        	logger.equals(e);
	        }
	        //状态集释放
	        finally
	        {
	        }

		}
		
		
	}
	
	
	
	
	
}
