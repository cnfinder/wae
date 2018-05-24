package cn.finder.wae.controller.action.common;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;

import cn.finder.wae.business.module.common.service.FileService;
import cn.finder.wae.common.base.BaseValidateActionSupport;
import cn.finder.wae.common.comm.MD5Util;
import cn.finder.wae.common.exception.Fault;

public class FileAction extends BaseValidateActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7200320031364953226L;
	private File[] image;//获取上传文件
    private String[] imageFileName;//获取上传文件名称
    private String[] imageContentType;//获取上传文件类型
    private String md5Value;

    //读取数据库中二进制图片使用
    private FileInputStream input;
    private long showTableConfigId;
    private String primaryKeyValue;
    private String fieldName;
    private String fileName;  
    
    private FileService fileService;
    private Fault fault=new Fault();
    InputStream file;
	
    
    //获取图片流
    public String getBufferedImage(){
    	BufferedImage image = fileService.getBufferedImage(showTableConfigId, primaryKeyValue, fieldName);
    	try {
    		if(image!=null)
    			ImageIO.write(image, "JPEG", ServletActionContext.getResponse().getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
    }
    
    
  //获取流
    public String getBufferedFile(){
    	
    	file = fileService.getBufferedFile(showTableConfigId, primaryKeyValue, fieldName);
    	try {
    		 this.fileName = new String(this.fileName.getBytes("UTF-8"),"ISO-8859-1");  
    		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return SUCCESS;
    }
    
    public String fileCosUpload(){
    	return SUCCESS;
    }
	public String page(){
		return SUCCESS;
	}
    public String upload() throws Exception{
        String path = ServletActionContext.getServletContext().getRealPath("/images");
        System.out.println(path);
       
        if(image != null){
               File savedir = new File(path);
               if(!savedir.exists()) savedir.mkdirs();
               String[] paths = new String[image.length];
               for(int i = 0; i < image.length; i++){
                      File saveFile = new File(savedir,imageFileName[i]);
                      FileUtils.copyFile(image[i], saveFile);
                      paths[i] = savedir+imageFileName[i]+"("+new Date()+")";
               }
               md5Value = MD5Util.getMD5(image[0]);
               //getResponse().getWriter().write(md5Value);//返回json字符串,先返回第一个试试
        }
        return SUCCESS;
 }

   
    
    
    public HttpServletResponse getResponse() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		return response;
	}

	public File[] getImage() {
		return image;
	}



	public void setImage(File[] image) {
		this.image = image;
	}



	public String[] getImageFileName() {
		return imageFileName;
	}



	public void setImageFileName(String[] imageFileName) {
		this.imageFileName = imageFileName;
	}



	public String[] getImageContentType() {
		return imageContentType;
	}



	public void setImageContentType(String[] imageContentType) {
		this.imageContentType = imageContentType;
	}
	public String getMd5Value() {
		return md5Value;
	}
	public void setMd5Value(String md5Value) {
		this.md5Value = md5Value;
	}
	public FileInputStream getInput() {
		return input;
	}
	public void setInput(FileInputStream input) {
		this.input = input;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}


	public long getShowTableConfigId() {
		return showTableConfigId;
	}

	public void setShowTableConfigId(long showTableConfigId) {
		this.showTableConfigId = showTableConfigId;
	}

	public String getPrimaryKeyValue() {
		return primaryKeyValue;
	}

	public void setPrimaryKeyValue(String primaryKeyValue) {
		this.primaryKeyValue = primaryKeyValue;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public void setFileService(FileService fileService) {
		this.fileService = fileService;
	}

	@Override
	protected Fault getFault() {
		// TODO Auto-generated method stub
		return fault;
	}


	public String getFileName() {
		return fileName;
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	public InputStream getFile() {
		return file;
	}


	public void setFile(InputStream file) {
		this.file = file;
	}
    
	
    
}
