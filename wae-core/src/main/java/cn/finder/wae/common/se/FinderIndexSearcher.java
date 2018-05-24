package cn.finder.wae.common.se;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.queries.mlt.MoreLikeThis;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BooleanQuery.Builder;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopFieldDocs;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;

/***
 * 搜索引擎 搜索器
 * @author whl
 *
 */
public class FinderIndexSearcher {

	private static Logger logger=Logger.getLogger(FinderIndexSearcher.class);
	//private  String indexPath=ArchCache.getInstance().getUserConfigCache().get("user_config_kbs_indexPath").getValue();//索引目录
	private  String indexPath="";
	/***
	 * 排序字段
	 */
	private List<SortField> sortFields;
	
	/***
	 * 索引字段的权重
	 */
	private  Map<String,Float> boost=new HashMap<String, Float>();
	
	
	/***
	 * 构建 条件关系约束
	 */
	private BooleanQuery.Builder builder=new Builder();;
	
	private Filter filter;
	
	private float scoreMin=0.0f;
	
	//关键词 高亮显示  默认显示
	private boolean showLight=true;
	
	
	
	
	
	public boolean isShowLight() {
		return showLight;
	}

	public void setShowLight(boolean showLight) {
		this.showLight = showLight;
	}

	public Filter getFilter() {
		return filter;
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
	}

	public BooleanQuery.Builder getBuilder() {
		return builder;
	}

	/***
	 * 添加条件
	 * @param booleanClause
	 */
	public void addBooleanClause(BooleanClause booleanClause){
		builder.add(booleanClause);
	}



	public  void setIndexPath(String indexPath){
		this.indexPath=indexPath;
	}
	
	
	
	
	public List<SortField> getSortFields() {
		return sortFields;
	}


	public void setSortFields(List<SortField> sortFields) {
		this.sortFields = sortFields;
	}


	


	public Map<String, Float> getBoost() {
		return boost;
	}


	public void setBoost(Map<String, Float> boost) {
		this.boost = boost;
	}


	public String getIndexPath() {
		return indexPath;
	}

	

	

	
	
	
	public float getScoreMin() {
		return scoreMin;
	}

	public void setScoreMin(float scoreMin) {
		this.scoreMin = scoreMin;
	}

	public  SEResult<Map<String,Object>> doSearch(String searchText,int pageIndex,int pageSize,String[] fieldNames){
		SEResult<Map<String,Object>> result=new SEResult<Map<String,Object>>();
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		result.setResult(list);
		result.setPageIndex(pageIndex);
		result.setPageSize(pageSize);
		try {
			Analyzer analyzer = new StandardAnalyzer();
			IndexSearcher searcher=SEShareObject.getSearcher();
			
			//FieldValueQuery query=new FieldValueQuery(field)
			/* String[] fieldNames = new String[]{"postfollow_content","post_title","post_content","post_url_field","post_user_id","post_create_time","plate_name",
					 "post_reply_number","post_visit_number","plate_url"};*/
			MultiFieldQueryParser parser = new MultiFieldQueryParser(fieldNames, analyzer,boost);//在fieldNames多个字段分词中 查找 
			
			Query query=null;
			try {
				query = parser.parse(searchText);
			} catch (ParseException e1) {
				logger.error(e1);
			}
			
			this.addBooleanClause(new BooleanClause(query,Occur.MUST));
			
			BooleanQuery bq=builder.build();
			
			
			//帖子类型过滤
			/*Filter filter = new TermsFilter(
					new Term("postType_code",post_type_code)
				);*/
			
			
			
			
		//	SortField sf = new SortField("f1", SortField.FIELD_SCORE);
			//Sort sort = new Sort(SortField.FIELD_SCORE,new SortField("post_visit_number",SortField.Type.LONG,false),new SortField("post_reply_number",SortField.Type.LONG,false)/*,new SortField("post_create_time",Type.STRING,false)*/);
			
			Sort sort = new Sort();
			if(sortFields!=null && sortFields.size()>0){
				SortField[] sortFieldArr=new SortField[sortFields.size()];
				sortFields.toArray(sortFieldArr);
				sort.setSort(sortFieldArr);
			}
			
			
			//Collector
			
		//	TopFieldCollector  tfc= TopFieldCollector.create(sort, 1, true, true, true);
			
			//DuplicateFilter
			 //This class will be removed in Lucene 6.0. DiversifiedTopDocsCollector should be used instead with a maximum number of hits per key equal to 1.
		
			
			
			
			TopFieldDocs topFieldDocs=searcher.search(bq, filter,999999999, sort,true,true);
			
			result.setCount((long)topFieldDocs.totalHits);//设置总数
			
			
			MoreLikeThis mlt = new MoreLikeThis(searcher.getIndexReader());//匹配相似
            mlt.setFieldNames(fieldNames);
            mlt.setMinDocFreq(1);//至少命中频率
            mlt.setMinTermFreq(1);//至少在命中term的频率
            mlt.setAnalyzer(analyzer);
            
			if(topFieldDocs!=null){
				
				ScoreDoc[] sd = topFieldDocs.scoreDocs;//搜索到的所有的记录
				if(scoreMin!=0.0f){
					
					List<ScoreDoc> conditionScoreDoc=new ArrayList<ScoreDoc>();
					StringBuffer sb_score=new StringBuffer();
					for(int k=0;k<sd.length;k++){
						//logger.info("==="+sd[k].score);
						sb_score.append(sd[k].score).append(",");
						if(sd[k].score>=scoreMin){
							conditionScoreDoc.add(sd[k]);
						}
					}
					logger.info("===scores:"+sb_score);
					logger.info("====评分过滤后前："+sd.length);
					sd=conditionScoreDoc.toArray(new ScoreDoc[0]);
					logger.info("====评分过滤后结果："+sd.length);
					if(sd==null){
						sd=new ScoreDoc[0];
					}
					result.setCount(sd.length);
				}
			
			
				//查询起始记录位置
		        int begin = pageSize * (pageIndex - 1);
		        //查询终止记录位置
		        int end = Math.min(begin + pageSize, sd.length);
				
		        for (int i = begin; i < end; i++) {
		          //float score=sd[i].score;
		          Document doc=searcher.doc(sd[i].doc);
		          
		          List<IndexableField> fields= doc.getFields();
		          HashMap<String,Object> item=new HashMap<String, Object>();
		          for(int j=0;j<fields.size();j++){
		        	  IndexableField field= fields.get(j);
		        	  
		        	  String name=field.name();
		        	  String value=field.stringValue();
		        	  try {
		        		  if(value!=null && showLight)
		        			  value = SECommon.lighterStr(query, analyzer, name, value);
					} catch (InvalidTokenOffsetsException e) {
						logger.error(e);
					}
		        	 item.put(name, value);
		        	  
		          }
		          
		          list.add(item);
		          
		          
		        }
		        
		        
		        
		       
		        
		        
		        
		        
		        
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
		
	}
	
	
	
	
}
