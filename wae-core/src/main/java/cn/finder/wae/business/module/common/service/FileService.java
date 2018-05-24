package cn.finder.wae.business.module.common.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;

public interface FileService {
	
	public int addFile(File file);
	
	public File downLoadFile(Long id);
	
	public String getExtentionName(String fileName) ;
	
	public int addFileBinary(File file);
	
	public BufferedImage getBufferedImage(long showTableConfigId,String primaryKeyValue, String fieldName);
	
	public InputStream getBufferedFile(long showTableConfigId,String primaryKeyValue, String fieldName);
	
}
