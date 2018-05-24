package cn.finder.wae.global.api.test;

import org.junit.Test;

import cn.finder.httpcommons.utils.FileItem;
import cn.finder.wae.global.api.domain.Media;
import cn.finder.wae.global.api.request.MediaUploadRequest;
import cn.finder.wae.global.api.service.GloablApiService;
import cn.finder.wae.httpcommons.ApiConfig.ServiceInterfaceConfig;

public class AppTest 
{
	@Test
	public void before(){
		 ServiceInterfaceConfig.setContextRootUrl("http://iv.cwintop.com/iv");
	}
	
	@Test
   public void testUploadMedia(){
	  
		
	   GloablApiService service=new GloablApiService();
	   MediaUploadRequest req=new MediaUploadRequest();
	   req.setName("ce");
	   FileItem file=new FileItem("d:\\a.png");
	   file=new FileItem("binary_data", file.getContent());
	   req.setMedia_file(file);
	   Media media= service.uploadMedia("http://iv.cwintop.com/iv",req);
	   System.out.println(media.getMedia_id());
	   
	   
		
   }
	
	@Test
	 public void testMediaGet(){
		 ServiceInterfaceConfig.setContextRootUrl("http://iv.cwintop.com/iv");
		   GloablApiService service=new GloablApiService();
		   
		   String url = service.mediaGet("http://iv.cwintop.com/iv","27df5f30-440e-4411-89d5-2072916775c2");
		   System.out.println(url);
			
	   }
	
	@Test
	 public void testMediaDescInfo(){

		   GloablApiService service=new GloablApiService();
		   
		   Media media = service.findMediaDescInfo("http://localhost:8080/mk","d10808e2-d15c-4cc5-bfd5-68de27dc8d25");
		   
		   System.out.println(media.getMedia_id());
	
	 }
	
	@Test
	 public void testMediaDelete(){

		   GloablApiService service=new GloablApiService();
		   
		   boolean b = service.mediaDelete("http://localhost:8080/mk","8cdb4b812488475ab2fddc5ce3fd189f");
		   
		   System.out.println(b);
	 }
	
	
}
