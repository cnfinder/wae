package cn.finder.wae.business.module.common.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.finder.wae.business.module.common.dao.CommOperationDao;
import cn.finder.wae.business.module.common.service.FileService;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.cache.SysConfigCache;

public class FileServiceImpl implements FileService {
	private CommOperationDao commOperationDao;
	
	public void setCommOperationDao(CommOperationDao commOperationDao) {
		this.commOperationDao = commOperationDao;
	}

	@Override
	public int addFile(File file) {
		String showTableConfigId = ArchCache.getInstance().getSysConfigCache().get(SysConfigCache.KEY_SYSCONFIG_CONFIG_UPLOAD_FILE_SHOWTABLECONFIGID).getValue();
		String tableName = ArchCache.getInstance().getSysConfigCache().get(SysConfigCache.KEY_SYSCONFIG_CONFIG_UPLOAD_FILE_TABLE_NAME).getValue();
		
		String sql = " insert into t_file(name,file_path,file_type,file_size,create_date) values(?,?,?,?,?)";
		List<Object> list = new ArrayList<Object>();
		list.add(file.getName());
		list.add(file.getPath());
		list.add(getExtentionName(file.getName()));
		list.add(file.length());
		list.add(new Date());
		commOperationDao.addRecord(Long.valueOf(showTableConfigId), sql, list);
		return 0;
	}

	@Override
	public File downLoadFile(Long id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getExtentionName(String fileName) {
		String name = "";
		String extention = "";
		if (fileName.length() > 0 && fileName != null) { // --截取文件名
			int i = fileName.lastIndexOf(".");
			if (i > -1 && i < fileName.length()) {
				name = fileName.substring(0, i); // --文件名
				extention = fileName.substring(i + 1); // --扩展名
			}
		}
		return extention;
	}
	
	public int addFileBinary(File file){
		return -1;
	}

	@Override
	public BufferedImage getBufferedImage(long showTableConfigId,String primaryKeyValue,
			String fieldName) {
		// TODO Auto-generated method stub
		return commOperationDao.getBufferedImage(showTableConfigId, primaryKeyValue, fieldName);
	}
	
	@Override
	public InputStream getBufferedFile(long showTableConfigId,String primaryKeyValue,
			String fieldName) {
		// TODO Auto-generated method stub
		return commOperationDao.getBufferedFile(showTableConfigId, primaryKeyValue, fieldName);
	}
	
	
}
