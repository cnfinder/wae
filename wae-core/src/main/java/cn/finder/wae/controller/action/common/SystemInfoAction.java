package cn.finder.wae.controller.action.common;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Properties;

import cn.finder.wae.common.base.BaseActionSupport;
import cn.finder.wae.controller.action.sysinfo.SystemInfo;

public class SystemInfoAction extends BaseActionSupport{

	
	private String IP;
	private String Mac;
	private String hostName;
	private String systemName;
	
	
	public String systemInfo(){
		IP = getIP();
		
		return SUCCESS;
	}
	
	//当前实例
		private static SystemInfoAction currentSystem = null;
		private InetAddress localHost = null;
		
		private SystemInfoAction(){
			try {
				localHost = InetAddress.getLocalHost();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
		
		/****
		 * 单例模式获取对象
		 */
		public static SystemInfoAction getInstance(){
			if (currentSystem == null) {
				currentSystem = new SystemInfoAction();
			}
			return currentSystem;
		}
		
		
		
		/**
		 * 本地IP
		 * @return
		 */
		public String getIP(){
			String ip = localHost.getHostAddress();
			
			return ip;
		}
		
		
		/**
		 * 获取用户机器名称
		 * @return
		 */
		public String getHostName(){
			
			return localHost.getHostName();
		}
		
		
		/**
		 * 获取Mac地址
		 * @return
		 */
		public String getMac(){
			NetworkInterface byInetAddress;
			try {
				byInetAddress = NetworkInterface.getByInetAddress(localHost);
				byte[] hardwareAddress = byInetAddress.getHardwareAddress();
				return getMacFromBytes(hardwareAddress);
			} catch (SocketException e) {
				e.printStackTrace();
			}
			
			return null;
		}
		
		
		
		private String getMacFromBytes(byte[] bytes) {
			StringBuffer mac = new StringBuffer();
			byte currentByte;
			boolean first = false;
			for (byte b : bytes) {
				if(first){
					mac.append("-");
				}
				currentByte = (byte) ((b&240)>>4);
				mac.append(Integer.toHexString(currentByte));
				currentByte = (byte) (b&15);
				mac.append(Integer.toHexString(currentByte));
				first = true;
			}
			return mac.toString().toUpperCase();
		}

		/**
		 * 获取系统名称
		 * @return
		 */
		public String getSystemName(){
			
			Properties sysProperty = System.getProperties();
			//系统名称
			String systemName = sysProperty.getProperty("os.name");
			return systemName;
		}

		public void setIP(String iP) {
			IP = iP;
		}

		public void setMac(String mac) {
			Mac = mac;
		}

		public void setHostName(String hostName) {
			this.hostName = hostName;
		}

		public void setSystemName(String systemName) {
			this.systemName = systemName;
		}
		
		
		
}
