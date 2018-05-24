package cn.finder.wae.controller.servlet;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.finder.wae.business.module.common.service.FileService;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.cache.SysConfigCache;
import cn.finder.wae.common.comm.MD5Util;

import com.oreilly.servlet.MultipartRequest;

@Deprecated
public class FileUploadServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = -38586148787153368L;
	
	private FileService fileService;
	Logger log = Logger.getLogger("");
	
	 @Override    
	    public void init() throws ServletException {             
	        super.init();     
	                     
	        ServletContext servletContext = this.getServletContext();     
	                     
	        WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(servletContext);     
	                     
	        fileService = (FileService)ctx.getBean("fileService");     
	    }     
	 
	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String type = request.getParameter("type");
		String uploadPath = null;
		if(type != null && type.equals("durgImgPath")){
			uploadPath = ArchCache.getInstance().getSysConfigCache().get(SysConfigCache.KEY_CONFIG_DRUG_IMG_PATH).getValue();
		}else{
			uploadPath = ArchCache.getInstance().getSysConfigCache().get(SysConfigCache.KEY_SYSCONFIG_CONFIG_UPLOAD_FILE_ROOT_PATH).getValue();
		}
		
		HttpSession session = request.getSession(true);
		String saveDirectory = session.getServletContext().getRealPath(uploadPath);
		MultipartRequest multi = new MultipartRequest(request,saveDirectory,
			100 * 1024 * 1024, "UTF-8");
		 
		//如果有上传文件, 则保存到数据内
		Enumeration files = multi.getFileNames();
		while (files.hasMoreElements()) {
			String name = (String)files.nextElement();
			File f = multi.getFile(name);
			if(f!=null){
				//读取上传后的项目文件, 导入保存到数据中
				String extentionName = fileService.getExtentionName(f.getName());
				String newName = saveDirectory + "/" + MD5Util.getMD5(f)+"."+extentionName;
				
				File newFile = new File(newName);
				f.renameTo(newFile);
				
				fileService.addFile(newFile);                       
				log.info(newFile.getName());
				response.getWriter().write(uploadPath+"/"+newFile.getName());    //可以返回一个JSON字符串, 在客户端做更多处理					
			}
		}
	}
	

}
