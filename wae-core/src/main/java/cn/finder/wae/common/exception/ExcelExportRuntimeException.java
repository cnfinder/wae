package cn.finder.wae.common.exception;

public class ExcelExportRuntimeException extends RuntimeException{
	/**
	 * 
	 */
	public ExcelExportRuntimeException() {
		super("Excel导出异常");
		// TODO Auto-generated constructor stub
	}
	
	public ExcelExportRuntimeException(Throwable e)
	{
		super(e);
	}


	


	public ExcelExportRuntimeException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}


	public ExcelExportRuntimeException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
}
