package cn.finder.wae.controller.action;

import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.cache.SysConfigCache;
import cn.finder.wae.common.base.BaseActionSupport;
import cn.finder.wae.common.comm.Common;

/***
 * 参数转码 是否转码从数据库配置 
 * 此操作为了不同服务器可能会使用不同编码，这样提交参数就可能需要转码， 所以所有的参数可以调用此方法
 * @author wu hualong
 *
 */
public class ParameterDecodeAction  extends BaseActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5636011033710590263L;

	
	protected String  parameterDecode(String value) {
		
		
		int needDecode = ArchCache.getInstance().getSysConfigCache().get(SysConfigCache.KEY_SYSCONFIG_CONFIG_NEED_DECODE).getIntValue();
		String fromEncodeType = ArchCache.getInstance().getSysConfigCache().get(SysConfigCache.KEY_SYSCONFIG_CONFIG_FROM_ENCODE_TYPE).getValue();
		String toEncodeType = ArchCache.getInstance().getSysConfigCache().get(SysConfigCache.KEY_SYSCONFIG_CONFIG_TO_ENCODE_TYPE).getValue();
		if(needDecode==1)
		{
			return Common.decoderString(value, fromEncodeType, toEncodeType);
		}
		else{
			return value;
		}
		
		
	}
}
