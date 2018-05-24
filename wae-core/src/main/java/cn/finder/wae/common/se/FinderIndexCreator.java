package cn.finder.wae.common.se;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;


public class FinderIndexCreator {

	private Logger logger=Logger.getLogger(FinderIndexCreator.class);
	
	/***
	 * 创建索引
	 */
	public  void createIndex(String indexPath,boolean isCreate,List<Document> docs,String keyfield) throws Exception{
		
		
		Date start = new Date();
	    try {
	      logger.info("Indexing to directory '" + indexPath + "'...");


	      // Optional: for better indexing performance, if you
	      // are indexing many documents, increase the RAM
	      // buffer.  But if you do this, increase the max heap
	      // size to the JVM (eg add -Xmx512m or -Xmx1g):
	      //
	      // iwc.setRAMBufferSizeMB(256.0);
	      
	      

	      indexData(SEShareObject.getWriter(),indexPath,docs,keyfield);

	      // NOTE: if you want to maximize search performance,
	      // you can optionally call forceMerge here.  This can be
	      // a terribly costly operation, so generally it's only
	      // worth it when your index is relatively static (ie
	      // you're done adding documents to it):
	      //
	      // writer.forceMerge(1);

	     

	      Date end = new Date();
	      logger.info(end.getTime() - start.getTime() + " total milliseconds");

	    } catch (Exception e) {
	    	logger.error(" caught a " + e.getClass() +
	       "\n with message: " + e.getMessage());
	    }finally{
	    	if(SEShareObject.getWriter()!=null){
	    		SEShareObject.getWriter().close();
	    		SEShareObject.setIndexWriter(null);
	    	}
	    }
	}
	
	/***
	 * 获取索引数据
	 * @param writer
	 */
	private void indexData(IndexWriter writer,String indexPath,List<Document> docs,String keyfield) throws Exception{
		Path p_indexPath=Paths.get(indexPath);
		
		if(!Files.exists(p_indexPath)){
			//不存在
			Files.createDirectory(p_indexPath);
		}
		
		if (Files.isDirectory(p_indexPath)) {

			if(!Files.isReadable(p_indexPath)){
				logger.error("the directory can not be read:=>"+indexPath);
			}
			else{
				
				
				if(docs!=null&&docs.size()>0){
					for(int i=0;i<docs.size();i++){
						Document doc=docs.get(i);
						
						try{
						 
					      if (writer.getConfig().getOpenMode() == OpenMode.CREATE ||writer.getConfig().getOpenMode() == OpenMode.CREATE_OR_APPEND) {
					        // New index, so we just add the document (no old document can be there):
					        writer.addDocument(doc);
					      } else {
					        // Existing index (an old copy of this document may have been indexed) so 
					        // we use updateDocument instead to replace the old one matching the exact 
					        // path, if present:
					    	String keyvalue=doc.getField(keyfield).stringValue();
					        writer.updateDocument(new Term(keyfield, keyvalue), doc);
					      }
					     // writer.flush();
						}
						catch(Throwable e){
							logger.error("=======error add docuemt=>keyfield value:"+doc.getField(keyfield).stringValue());
						}
						
					}
				}
				
			}
		}else{
			logger.error("the directory is not directory:=>"+indexPath);
		}
		
	}
	
	public void deleteDocument(String keyField,String keyFieldValue) throws IOException{
		
		SEShareObject.getWriter().deleteDocuments(new Term(keyField, keyFieldValue));
	}
}
