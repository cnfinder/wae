package cn.finder.wae.common.comm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/***
 * 
 * @author wuhualong
 *
 */
public class ProcessUtils {

	
	private final static Logger logger = Logger.getLogger(ProcessUtils.class);
	/*****
	 * 执行程序 并且返回结果每一行
	 * @param cmdPath  执行程序路径 如: cmd.exe /c LookHandle.exe
	 * @return
	 */
	public static List<String> runProcess(String cmdPath){
		
		List<String> resList=new ArrayList<String>();
		StringBuffer sb = new StringBuffer();
		sb.append(cmdPath);
		Process process;
		try {
			
			
			process = Runtime.getRuntime().exec(sb.toString());
			final InputStream stream = process.getInputStream();
			
			BufferedReader br = new BufferedReader(new InputStreamReader(stream));
			String line = null;
			while ((line = br.readLine()) != null) {
				resList.add(line);
			}
			
			br.close();
			
			
		} catch (IOException e) {
			logger.error(e);
		}catch(Exception e){
			logger.error(e);
		}
		
		return resList;
		
	}
}
