package cn.finder.ddpush.v1.client.appserver;

import java.util.Map;

import org.ddpush.im.util.StringUtil;
import org.ddpush.im.v1.client.appserver.Pusher;

import cn.finder.ddpush.v1.client.appuser.JsonUtils;

/***
 * 发送推送信息给对方
 * @author whl
 *
 */
public class FinderPusher extends Pusher{

	private String uuid;
	
	/***
	 * 
	 * @param host
	 * @param port
	 * @param timeoutMillis
	 * @param uuid 用户uuid 或者sessionkey
	 * @throws Exception
	 */
	public FinderPusher(String host, int port, int timeoutMillis,String uuid) throws Exception{
		super(host,port,timeoutMillis);
		this.uuid = uuid;
	}
	
	/***
	 * 
	 * @param host
	 * @param port
	 * @param timeoutMillis
	 * @param version
	 * @param appId
	 * @param uuid 用户uuid 或者sessionkey
	 * @throws Exception
	 */
	public FinderPusher(String host, int port, int timeoutMillis, int version, int appId,String uuid) throws Exception{
		super(host,port,timeoutMillis,version,appId);
		this.uuid =uuid;
	}
	
	
	
	
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	
	
	@Override
	public boolean push0x10Message(byte[] to_uuid) throws Exception {
		throw new Exception("the method is not supported!");
	}

	@Override
	public boolean push0x11Message(byte[] to_uuid, byte[] data) throws Exception {
		throw new Exception("the method is not supported!");
	}

	@Override
	public boolean push0x11Message(byte[] to_uuid, long data) throws Exception {
		throw new Exception("the method is not supported!");
	}

	
	
	
	/***
	 * 
	 * @param uuid 接受者UUID
	 * @param data map类型的数据
	 * @return
	 * @throws Exception
	 */
	public boolean push0x20MessageExt(String toUserUUId, Map<String,Object> data) throws Exception
	{
		byte[] touuidbyte =StringUtil.md5Byte(toUserUUId);
		return push0x20MessageExt(touuidbyte,data);
		
	}
	
	/***
	 * 
	 * @param touuidbyte 接收者UUID 字节数组
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public boolean push0x20MessageExt(byte[] touuidbyte, Map<String,Object> data) throws Exception
	{
		data.put("fromUser", uuid);
		String jsonStrData = JsonUtils.getJsonString4JavaPOJO(data);
		byte[] byteData =jsonStrData.getBytes("UTF-8");
		
		
		return push0x20Message(touuidbyte,byteData);
		
		
	}
	
}
