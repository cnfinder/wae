package cn.finder.wae.common.base;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.interceptor.ServletRequestAware;

import cn.finder.wae.common.comm.Common;
import cn.finder.wae.common.exception.Fault;
import cn.finder.wae.helper.MsgConstants;

public abstract class BaseValidateActionSupport extends BaseActionSupport implements
		ServletRequestAware {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected boolean validateSuccess=true;

	 
	public String validateFieldIsEmpty(String paramName){
		return validateFieldIsEmpty(paramName,getText(MsgConstants.ERR_COMM_NOT_EMPTY));
	}
	 /***
		 * 验证字段是否为空
		 * if null  为空
		 */
		public String validateFieldIsEmpty(String paramName,String errMsg)
		{
			String paramValue=request.getParameter(paramName);
			if(StringUtils.isEmpty(paramValue)){
				//"工程名称不能为空
				setValidateSuccess(false);
				getFault().addErrorInfo(paramName, errMsg);
				return null;
			}
			return paramValue;
			
		}
		
		public String validateFieldIsEmpty(String paramName,String errMsg,boolean isRequired)
		{
			String paramValue=request.getParameter(paramName);
			if(isRequired)
			{
				if(StringUtils.isEmpty(paramValue)){
					//"工程名称不能为空
					setValidateSuccess(false);
					getFault().addErrorInfo(paramName, errMsg);
					return null;
				}
			}
			return paramValue;
			
		}
		
		
		
		
		
		/***
		 * 验证字段是否为空 和 是否是日前类型
		 * @param paramName
		 * @param errMsg
		 * @return
		 */
		public Date validateFieldDate(String paramName,String errMsg)
		{
			String paramValue=request.getParameter(paramName);
			if(validateFieldIsEmpty(paramName,errMsg)!=null)
			{
				Date date=Common.parseDate(paramValue);
				if(date==null)
				{
					setValidateSuccess(false);
					getFault().addErrorInfo(paramName, errMsg);
					return null;
				}
				else{
					return date;
				}
			}
			return null;
		}
		
		public Date validateFieldDate(String paramName){
			return validateFieldDate(paramName,getText(MsgConstants.ERR_COMM_NOT_EMPTY));
		}
		
		
		
		public boolean validateFieldBoolean(String paramName)
		{
			return validateFieldBoolean(paramName,getText(MsgConstants.ERR_COMM_DATA_ERROR));
		}
		
		
		
		/***
		 * 验证字段是否为空和布尔类型
		 * @param paramName
		 * @param errMsg
		 * @return
		 */
		public boolean validateFieldBoolean(String paramName,String errMsg)
		{
			boolean retVal=false;
			String paramValue=request.getParameter(paramName);
			if(validateFieldIsEmpty(paramName,errMsg)!=null)
			{

				try
				{
					retVal=Boolean.parseBoolean(paramValue);
					setValidateSuccess(true);
				}
				catch(NumberFormatException exp)
				{
					setValidateSuccess(false);
					getFault().addErrorInfo(paramName, errMsg);
				}
			}
			
			return retVal;
		}
		
		
		public int validateFieldNumber(String paraName)
		{
			return validateFieldNumber(paraName,getText(MsgConstants.ERR_COMM_INVALIDATE_NUMBER));
		}
		
		/***
		 * 验证字段是否为空和是否为数字
		 * @param paramName
		 * @param errMsg
		 * @return
		 */
		public int validateFieldNumber(String paramName,String errMsg)
		{
			int retVal=-1;
			String paramValue=request.getParameter(paramName);
			if(validateFieldIsEmpty(paramName,errMsg)!=null)
			{

				try
				{
					retVal=Integer.parseInt(paramValue);
					setValidateSuccess(true);
				}
				catch(NumberFormatException exp)
				{
					setValidateSuccess(false);
					getFault().addErrorInfo(paramName, errMsg);
				}
			}
			
			return retVal;
		}
		
		
		public long validateFieldLong(String paraName)
		{
			return validateFieldLong(paraName,getText(MsgConstants.ERR_COMM_INVALIDATE_NUMBER));
		}

		public long validateFieldLong(String paramName,String errMsg)
		{
			long retVal=-1;
			String paramValue=request.getParameter(paramName);
			if(validateFieldIsEmpty(paramName,errMsg)!=null)
			{

				try
				{
					retVal=Long.parseLong(paramValue);
					setValidateSuccess(true);
				}
				catch(NumberFormatException exp)
				{
					setValidateSuccess(false);
					getFault().addErrorInfo(paramName, errMsg);
				}
			}
			
			return retVal;
		}

		/***
		 * 验证字段是否为空和是否为double
		 * @param paramName
		 * @param errMsg
		 * @return
		 */
		public double validateFieldDouble(String paramName,String errMsg)
		{
			double retVal=-1;
			String paramValue=request.getParameter(paramName);
			if(validateFieldIsEmpty(paramName,errMsg)!=null)
			{

				try
				{
					retVal=Double.parseDouble(paramValue);
				}
				catch(NumberFormatException exp)
				{
					setValidateSuccess(false);
					getFault().addErrorInfo(paramName, errMsg);
				}
			}
			return retVal;
		}
		
		public double validateFieldDouble(String paramName){
			return validateFieldDouble(paramName,getText(MsgConstants.ERR_COMM_DATA_ERROR));
		}


		public void setValidateSuccess(boolean validateSuccess) {
			if(!validateSuccess)
			{
				this.validateSuccess=validateSuccess;
			}
		}
		
		/**
		 * 清除的方法
		 */
		public void resetFault(){
			validateSuccess=true;
			getFault().getErrs().clear();
		}
		
		
		protected abstract Fault getFault();
}
