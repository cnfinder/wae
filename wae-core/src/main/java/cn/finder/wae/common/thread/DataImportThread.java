package cn.finder.wae.common.thread;

import java.util.TimerTask;

import org.apache.log4j.Logger;

import cn.finder.wae.business.domain.DataImportConfig;
import cn.finder.wae.business.module.common.service.CommOperationService;

/***
 * 不建议使用
 * @author Administrator
 *
 */
@Deprecated
public class DataImportThread extends TimerTask{
	
	Logger log = Logger.getLogger(this.getClass());
	
	private DataImportConfig dataImportConfig;
	private CommOperationService commOperationService;
	
	public void setCommOperationService(CommOperationService commOperationService){
		this.commOperationService = commOperationService;
	}
	public void setDataImportConfig(DataImportConfig dataImportConfig){
		this.dataImportConfig = dataImportConfig;
	}
	public synchronized void run(){
//		System.out.println("test>>>>>>>>>>>>>>");
//		while(1==1){
			try {
//				this.wait(dataImportConfig.getFrequency());
				commOperationService.dataImport(dataImportConfig.getId().intValue(),null);
					log.info("后台自动执行了一次数据导入！id=" + dataImportConfig.getId()+"  name="+ dataImportConfig.getName());
				if(dataImportConfig.getFrequency() == null || dataImportConfig.getFrequency() == 0 ){
					this.cancel();
				}
			} catch (Exception e) {
				log.info("执行数据导入时发生错误！id=" + dataImportConfig.getId());
				e.printStackTrace();
			}
			//执行一次导入
			
		}
//	}
	
	public static void main(String[] args){
//		Thread t = new DataImportThread();
//		t.start();
	}
}
