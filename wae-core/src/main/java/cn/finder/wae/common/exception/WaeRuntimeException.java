package cn.finder.wae.common.exception;

public class WaeRuntimeException extends RuntimeException{

	private ErrorInfo errInfo=new ErrorInfo();
	
	public ErrorInfo getErrInfo(){
		return this.errInfo;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -4692691269805983593L;
	public WaeRuntimeException() {
		super("Wae运行时异常");
		// TODO Auto-generated constructor stub
	}
	
	public WaeRuntimeException(Throwable e)
	{
		super(e);
	}


	


	public WaeRuntimeException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}


	public WaeRuntimeException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String toString(){
		StackTraceElement[] trace = getStackTrace();
		StringBuffer sb=new StringBuffer();
		sb.append(this.getMessage());
		sb.append("\n");
        for (int i=0; i < trace.length; i++)
        	sb.append("\tat " + trace[i]).append("\n");
        return sb.toString();
	}
}
