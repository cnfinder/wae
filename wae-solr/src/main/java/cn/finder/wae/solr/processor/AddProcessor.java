package cn.finder.wae.solr.processor;

import java.util.Map;
import java.util.UUID;

import javax.sql.DataSource;

import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;

import cn.finder.test.solr.Notice;
import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.common.aspect.logger.ArchType;
import cn.finder.wae.common.aspect.logger.FinderLogger;
import cn.finder.wae.queryer.common.BaseCommonDBQueryer;


/***
 * 添加索引数据
 * 
 * @author Administrator
 *
 */
public class AddProcessor extends  BaseCommonDBQueryer{
	
	
	@FinderLogger(archType=ArchType.FINDER_ARCH)
	@Override
	public TableQueryResult queryTableQueryResult(long showTableConfigId,
			QueryCondition<Object[]> condition){
		
		logger.info("=====enter=>"+this.getClass().toString());
		logger.debug("==showTableConfigId:"+showTableConfigId);
		
		
		@SuppressWarnings("unchecked")
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
		TableQueryResult queryResult = new TableQueryResult();
		queryResult.setCount(0l);
		queryResult.setPageIndex(1);
		queryResult.setPageSize(10);
		
		
		String solr_srv_url="http://localhost:8983/solr";
		
		 HttpSolrClient httpSolrClient = new HttpSolrClient(solr_srv_url);
	        httpSolrClient.setParser(new XMLResponseParser()); // 设置响应解析器
	        httpSolrClient.setConnectionTimeout(500); // 建立连接的最长时间
	        
	      Notice notice = new Notice();
	      String desc = "该应用场景为AdMaster DMP缓存存储需求，DMP需要管理非常多的第三方id数据，其中包括各媒体cookie与自身cookie（以下统称admckid）的mapping关系，还包括了admckid的人口标签、移动端id（主要是idfa和imei）的人口标签，以及一些黑名单id、ip等数据。";
	      notice.setId(UUID.randomUUID().toString());
	      notice.setTitle("Redis百亿级Key存储方案");
	      notice.setSubject("该应用场景为AdMaster DMP缓存存储需求");
	      notice.setDescription(desc);
	      notice.setText("hahh adfad");
	      httpSolrClient.addBean(notice);
	      httpSolrClient.commit();
		
		
		
		
		
		
		
		
		
		
		return queryResult;
	}

	public void setJDBCDataSource(DataSource dataSource) {
		super.setJdbcDataSource(dataSource);
		
	}
}
