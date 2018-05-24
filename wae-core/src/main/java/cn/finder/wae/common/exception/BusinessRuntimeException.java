package cn.finder.wae.common.exception;

/***
 * 业务运行时异常  将对事物回滚
 * @author whl
 *
 */
public class BusinessRuntimeException extends WaeRuntimeException {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5447982402218240019L;
	public BusinessRuntimeException() {
		super();
		// TODO Auto-generated constructor stub
	}


	public BusinessRuntimeException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public BusinessRuntimeException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public BusinessRuntimeException(Throwable cause) {
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
