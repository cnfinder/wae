package cn.finder.wae.waepp.server.net;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.session.IoSessionConfig;

import common.Logger;
/***
 * WAE 处理器
 * @author WHL
 *
 */
public class WaeppIoHandler extends IoHandlerAdapter {

	private static Logger logger = Logger.getLogger(WaeppIoHandler.class);
	
	@Deprecated
	private long timerSec=30; // 30秒 PING  客户端， 判断客户端是否连接着
	
	
	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		// TODO Auto-generated method stub
		super.exceptionCaught(session, cause);
		
		logger.warn(session, cause);
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		// TODO Auto-generated method stub
		super.messageReceived(session, message);
		logger.debug("== messageReceived: "+ message.toString());
		
		
		session.write("收到:"+message.toString());
		
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		// TODO Auto-generated method stub
		super.messageSent(session, message);
		
		logger.debug(" === messageSent:"+message);
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		super.sessionClosed(session);
		//String userId = session.getAttribute("userId").toString();
		logger.debug(" === sessionClosed:"+session.getId());
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		super.sessionCreated(session);
		
		IoSessionConfig config = session.getConfig();  
       // config.setIdleTime(IdleStatus.BOTH_IDLE, timerSec);
		
		logger.debug(" === sessionCreated:");
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
		// TODO Auto-generated method stub
		super.sessionIdle(session, status);
		logger.debug(" === sessionIdle:");
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		super.sessionOpened(session);
		
		logger.debug(" === sessionOpened:"+session.getId());
	}

	
	
	
	public long getTimerSec() {
		return timerSec;
	}

	public void setTimerSec(long timerSec) {
		this.timerSec = timerSec;
	}

	
	
	
}
