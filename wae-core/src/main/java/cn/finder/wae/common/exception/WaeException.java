package cn.finder.wae.common.exception;

public class WaeException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4692691269805983593L;
	public WaeException() {
		super("Wae异常");
		// TODO Auto-generated constructor stub
	}
	
	public WaeException(Throwable e)
	{
		super(e);
	}


	


	public WaeException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}


	public WaeException(String message) {
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
