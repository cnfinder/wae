package cn.finder.wae.controller.action.sysinfo;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Properties;

/***
 * 获取当前系统信息
 * @author Xuchao
 *
 */
public class SystemInfo {

	//当前实例
	private static SystemInfo currentSystem = null;
	private InetAddress localHost = null;
	
	private SystemInfo(){
		try {
			localHost = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	/****
	 * 单例模式获取对象
	 */
	public static SystemInfo getInstance(){
		if (currentSystem == null) {
			currentSystem = new SystemInfo();
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
	
	
	
	
	public static void main(String[] args){
		SystemInfo si  = new SystemInfo();
		String hn = si.getHostName();
		System.out.println(hn);
		String ip = si.getIP();
		System.out.println(ip);
		String mac = si.getMac();
		System.out.println(mac);
				
	}
	
	
	
	
	
	
	
	
}
