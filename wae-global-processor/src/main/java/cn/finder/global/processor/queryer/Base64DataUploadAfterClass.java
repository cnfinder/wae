package cn.finder.global.processor.queryer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.common.comm.ImageUtil;
import cn.finder.wae.common.comm.MD5Util;
import cn.finder.wae.common.stream.StreamUtils;
import cn.finder.wae.common.thumbnail.ThumbnailatorUtils;
import cn.finder.wae.queryer.handleclass.QueryerDBAfterClass;

/**
 * @author: whl
 * @function:Base64 
 */
public class Base64DataUploadAfterClass extends QueryerDBAfterClass {
	
	private Logger logger=Logger.getLogger(Base64DataUploadAfterClass.class);

	@Override
	public TableQueryResult handle(TableQueryResult tableQueryResult,
			long showTableConfigId, QueryCondition<Object[]> condition) {
		 super.handle(tableQueryResult, showTableConfigId, condition);
		 
		 List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
			Map<String,Object> item=new HashMap<String, Object>();
			list.add(item);
			tableQueryResult.setResultList(list);
		 
		 @SuppressWarnings("unchecked")
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
			
		 String name="";
		 Object binary_data=null;
		 byte[] thumbnail_binary_data=null;
		 byte[] thumbnail_binary_data_m=null;
		 String file_type="";
		 
		 String media_id="-1";
		 
		 String ext_type="";
		 try{
			 binary_data=data.get("binary_data_str");
			 
			 if(binary_data!=null &&binary_data.toString().length()>0){
				 
				 //判断资源是否存在
				 
				 String orig_binary_data_str= data.get("binary_data_str").toString();
				
				 
				 orig_binary_data_str = orig_binary_data_str.replace("data:image/png;base64,", "");
				 orig_binary_data_str = orig_binary_data_str.replace("data:image/jpeg;base64,", "");
				 orig_binary_data_str = orig_binary_data_str.replace("data:image/gif;base64,", "");
				 orig_binary_data_str = orig_binary_data_str.replace("data:image/bmp;base64,", "");
				 String md5=MD5Util.getMD5(orig_binary_data_str);
				 media_id= findMediaIdByCryptoCode(md5);
				 
				 
				 if("-1".equals(media_id)){
					 //资源不存在
					 
					 byte[] orig_binary_data=ImageUtil.generateBase64ToImage(orig_binary_data_str);
					 
					 binary_data=orig_binary_data;
					
					 
					 file_type= StreamUtils.getMimeType(orig_binary_data);
					 logger.info("===========file_type:"+file_type);
					 
					 ext_type=file_type.substring(file_type.lastIndexOf("/")+1);
					
					 logger.info("===========ext_type:"+ext_type);
					 
					
					
					 if(file_type.toLowerCase().indexOf("image")!=-1){
						 
						 logger.info("===========image===");
						 //是图片类型
						 ByteArrayInputStream bais=new ByteArrayInputStream(orig_binary_data);
						/* AffineTransImage afi=new AffineTransImage(bais, 100);
						 afi.setImgFormat(ext_type);
						 afi.resizeFix();
						 thumbnail_binary_data=afi.getBuffer();*/
						/* ByteArrayOutputStream out=new ByteArrayOutputStream();
						 Thumbnails.of(bais).size(100, 100).toOutputStream(out);
						 thumbnail_binary_data=out.toByteArray();
						 */
						 
						 
						 ByteArrayOutputStream out=ThumbnailatorUtils.thumbnailator(bais, 100);
						 thumbnail_binary_data=out.toByteArray();
						 
						 
						 
						 ByteArrayInputStream bais_m=new ByteArrayInputStream(orig_binary_data);
						 /*AffineTransImage afi_m=new AffineTransImage(bais_m, 300);
						 afi_m.setImgFormat(ext_type);
						 afi_m.resizeFix();
						 thumbnail_binary_data_m=afi_m.getBuffer();*/
						 
						/* ByteArrayOutputStream out_m=new ByteArrayOutputStream();
						 Thumbnails.of(bais_m).size(300,300).toOutputStream(out_m);
						 thumbnail_binary_data_m=out_m.toByteArray();*/
						 
						 ByteArrayOutputStream out_m=ThumbnailatorUtils.thumbnailator(bais_m, 300);
						 thumbnail_binary_data_m=out_m.toByteArray();
					 }
					 else{
						 logger.info("===========文件===");
						 String rootPath_tmp = data.get("WebRoot_path").toString();
						 logger.info("===========rootPath_tmp:"+rootPath_tmp);
						 //是文件类型
						 if(file_type.toLowerCase().indexOf("word")!=-1){
							 String rootPath = data.get("WebRoot_path").toString();
							 String imgPath=rootPath+"images/word.png";
							 FileInputStream fis=new FileInputStream(imgPath);
							 thumbnail_binary_data=new byte[fis.available()];
							 fis.read(thumbnail_binary_data);
							 fis.close();
							 
							 ext_type="doc";
							 
						 }else if(file_type.toLowerCase().indexOf("excel")!=-1){
							 String rootPath = data.get("WebRoot_path").toString();
							 String imgPath=rootPath+"images/excel.png";
							 FileInputStream fis=new FileInputStream(imgPath);
							 thumbnail_binary_data=new byte[fis.available()];
							 fis.read(thumbnail_binary_data);
							 fis.close();
							 ext_type="xls";
						 }else if(file_type.toLowerCase().indexOf("pdf")!=-1){
							 String rootPath = data.get("WebRoot_path").toString();
							 String imgPath=rootPath+"images/pdf.png";
							 FileInputStream fis=new FileInputStream(imgPath);
							 thumbnail_binary_data=new byte[fis.available()];
							 fis.read(thumbnail_binary_data);
							 fis.close();
							 ext_type="pdf";
						 }
						 else if(file_type.toLowerCase().indexOf("octet-stream")!=-1){
							 String rootPath = data.get("WebRoot_path").toString();
							 String imgPath=rootPath+"images/file.png";
							 FileInputStream fis=new FileInputStream(imgPath);
							 thumbnail_binary_data=new byte[fis.available()];
							 fis.read(thumbnail_binary_data);
							 fis.close();
							 ext_type="unknown";
						 }
					 }
					 
					 
					 
					 
					 
					 
					 try{
						 name=data.get("name").toString();
					 }
					 catch(Exception e){
					 }
					 
					 
					 String tmp_media_id=UUID.randomUUID().toString().replace("-", "");;

					 
					 String sql="insert into g_t_binary_data(guid_value,binary_data,name,file_type,ext_type,thumbnail_binary_data,thumbnail_binary_data_m,crypto_code) values(?,?,?,?,?,?,?,?)";
					 logger.info("===========file_type:"+file_type);
					 int affectCnt=getJdbcTemplate().update(sql, new Object[]{tmp_media_id,binary_data,name,file_type,ext_type,thumbnail_binary_data,thumbnail_binary_data_m,md5});
					 
					 if(affectCnt>0){
						 media_id=tmp_media_id;
					 }
					 
				 }
				 
				 
			 }
		 }
		 catch(Exception e){
			 
		 }
		 
		 
		 if(binary_data==null){
			 //没有留数据 不进行插入 ，media_id返回 -1
			 media_id="-1";
		 }
		 
		item.put("media_id", media_id);
		
        tableQueryResult.setCount(1l);
        tableQueryResult.setPageSize(1);
        tableQueryResult.setPageIndex(1);
	       
		 
		 
		 return tableQueryResult;
		 
	}

	@Override
	public void setJDBCDataSource(DataSource dataSource) {
		super.setJdbcDataSource(dataSource);
	}

	
	/***
	 * 根据摘要获取 media_id
	 * @param crypto_code
	 * @return
	 */
	private String findMediaIdByCryptoCode(String crypto_code){
		
		String sql="select guid_value from g_t_binary_data where crypto_code=?";
		
		
		List<Map<String,Object>> resultList= queryForList(sql, new Object[]{crypto_code});
		
		if(resultList!=null&&resultList.size()>0){
			
			return resultList.get(0).get("guid_value").toString();
		}else{
			return "-1";
		}
		
	}
	
}
