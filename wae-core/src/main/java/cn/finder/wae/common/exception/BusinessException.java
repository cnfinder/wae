package cn.finder.wae.common.exception;


/***
 * 业务异常 ，事物将不会回滚
 * @author whl
 *
 */
public class BusinessException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3064828048372767242L;

	public BusinessException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BusinessException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public BusinessException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public BusinessException(Throwable cause) {
		super(cause);
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
