package cn.finder.wae.controller.action.common;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import cn.finder.wae.common.base.BaseActionSupport;

import com.oreilly.servlet.MultipartRequest;

public class FileAction2 extends BaseActionSupport{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1531101596593343304L;

	public String upload(){
		try{
			String uploadPath = "/images";
			String saveDirectory = session.getServletContext().getRealPath(uploadPath);
			MultipartRequest multi = new MultipartRequest(request,saveDirectory,100 * 1024 * 1024, "UTF-8");
			 
			//如果有上传文件, 则保存到数据内
			Enumeration files = multi.getFileNames();
			while (files.hasMoreElements()) {
				String name = (String)files.nextElement();
				File f = multi.getFile(name);
				if(f!=null){
					//读取上传后的项目文件, 导入保存到数据中
					String fileName = multi.getFilesystemName(name);
					getResponse().getWriter().write(fileName +"("+new Date()+")");    //可以返回一个JSON字符串, 在客户端做更多处理					
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	 public HttpServletResponse getResponse() {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			return response;
		}
	 public HttpServletRequest getRequest() {
			HttpServletRequest request = ServletActionContext.getRequest();
			try {
				request.setCharacterEncoding("UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return request;
		}
}
