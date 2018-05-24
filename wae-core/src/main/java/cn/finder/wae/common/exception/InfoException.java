package cn.finder.wae.common.exception;

/****
 * 此异常并非是错误异常， 是为了提示消息 往上层抛出，然后能显示到客户端
 * @author dragon
 *
 */
public class InfoException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7998637193917344207L;

	private Throwable throwable;
	
	private MsgInfo errorInfo=new MsgInfo();
	
	
	public InfoException(String msg)
	{
		super(msg);
		errorInfo.setErrDesc(msg);
	}
	
	
	public InfoException(Throwable throwable){
		this(throwable,null);
		this.throwable=throwable;
	}
	public InfoException(Throwable throwable,MsgInfo errorInfo){
		this(throwable,null,errorInfo);
		this.throwable=throwable;
		this.errorInfo=errorInfo;
	}
	
	public InfoException(Throwable throwable,String msg,MsgInfo errorInfo){
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
