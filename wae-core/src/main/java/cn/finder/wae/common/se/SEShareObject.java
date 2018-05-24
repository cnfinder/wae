package cn.finder.wae.common.se;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/***
 * lucene搜索引擎共享对象
 * @author finder
 *
 */
public class SEShareObject {
	private static Logger logger=Logger.getLogger(SEShareObject.class);
	
	private static String indexPath;
	
	private static IndexSearcher searcher=null;
	
	private static IndexWriter indexWriter=null;
	
	private static IndexReader reader=null;
	

	public static void setSearcher(IndexSearcher searcher) {
		SEShareObject.searcher = searcher;
	}

	public static IndexWriter getIndexWriter() {
		return indexWriter;
	}

	public static void setIndexWriter(IndexWriter indexWriter) {
		SEShareObject.indexWriter = indexWriter;
	}

	public static IndexReader getReader() {
		return reader;
	}

	public static void setReader(IndexReader reader) {
		SEShareObject.reader = reader;
	}
	
	
	
	public static String getIndexPath() {
		return indexPath;
	}

	public  void setIndexPath(String indexPath) {
		SEShareObject.indexPath = indexPath;
	}

	public synchronized static  IndexSearcher getSearcher(){
		
		/*if(searcher==null){
			FSDirectory dir;
			try {
				dir = FSDirectory.open(Paths.get(indexPath));
				IndexReader reader = DirectoryReader.open(dir);
				
				searcher = new IndexSearcher(reader);
				
			} catch (IOException e) {
				logger.error(e);
			}
		}
		return searcher;*/
		if(searcher==null){
			FSDirectory dir;
			try {
				dir = FSDirectory.open(Paths.get(indexPath));
				reader = DirectoryReader.open(dir);
				
				
			} catch (IOException e) {
				logger.error(e);
			}
		}else{
			try {
				DirectoryReader newReader = DirectoryReader.openIfChanged((DirectoryReader)reader,getWriter(),true);
				if (newReader != null) {  
	                reader.close();
	                reader = newReader;  
	            }  
				
			} catch (IOException e) {
				logger.error(e);
			}
			
		}
		searcher = new IndexSearcher(reader);
		return searcher;
		
	}
	
	public synchronized static IndexWriter getWriter() throws IOException{
		if(indexWriter!=null){
			return indexWriter;
		}
		 Analyzer analyzer = new StandardAnalyzer();
	     IndexWriterConfig iwc = new IndexWriterConfig(analyzer);

	      Directory dir = FSDirectory.open(Paths.get(indexPath));
	      iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);

	      // Optional: for better indexing performance, if you
	      // are indexing many documents, increase the RAM
	      // buffer.  But if you do this, increase the max heap
	      // size to the JVM (eg add -Xmx512m or -Xmx1g):
	      //
	      // iwc.setRAMBufferSizeMB(256.0);

	      indexWriter = new IndexWriter(dir, iwc);
	     // indexWriter.commit();//在索引库没有建立并且没有索引文件的时候首先要commit一下让他建立一个,在索引库没有建立并且没有索引文件的时候首先要commit一下让他建立一个
	      return indexWriter;
	}
	
	
}
