package cn.finder.wae.common.exception;

/****
 * 抛出此异常， 将不执行 中心处理类，而直接返回到调用. 即 异常被 ChainStopException  捕获， 异常被正常化 ,也即不会抛出异常,事物也不会被回滚
 * 
 * 
 * @author finder
 *
 */
public class ChainStopException extends WaeRuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7998637193917344207L;

	private Throwable throwable;
	
	private MsgInfo errorInfo=new MsgInfo();
	
	
	public ChainStopException(String msg)
	{
		super(msg);
		errorInfo.setErrDesc(msg);
	}
	
	
	public ChainStopException(Throwable throwable){
		this(throwable,null);
		this.throwable=throwable;
	}
	public ChainStopException(Throwable throwable,MsgInfo errorInfo){
		this(throwable,null,errorInfo);
		this.throwable=throwable;
		this.errorInfo=errorInfo;
	}
	
	public ChainStopException(Throwable throwable,String msg,MsgInfo errorInfo){
		super(msg,throwable);
		this.throwable=throwable;
		this.errorInfo=errorInfo;
	}

	public MsgInfo getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(MsgInfo errorInfo) {
		this.errorInfo = errorInfo;
	}
	@Override
	public String toString(){
		return  String.format("errId:%s,errDesc:%s", errorInfo.getErrId(),errorInfo.getErrDesc());
	}
}
