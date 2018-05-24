package cn.finder.wae.queryer.handleclass.wx;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.wx.AppInfo;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.business.rowmapper.RowMapperFactory;
import cn.finder.wae.common.exception.InfoException;
import cn.finder.wae.queryer.handleclass.QueryerDBBeforeClass;
import cn.finder.wx.response.FindOpenIdInfoByCodeResponse;
import cn.finder.wx.service.WXService;

/**
 * @author: wuhualong
 * @data: 2015-06-18
 * @function:微信公众平台的管理员注册提交
 */
public class WXOpenManagerRegisterAddQueryerBefore extends QueryerDBBeforeClass {

	
	
	@Override
	public void handle(long showTableConfigId,
			QueryCondition<Object[]> condition) {
		
		super.handle(showTableConfigId, condition);

		logger.debug("====================WXOpenManagerRegisterAddQueryerBefore.handle ");
		logger.debug("====================showTableConfigId: "+showTableConfigId);
		
		@SuppressWarnings("unchecked")
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
		
		data.put("showtableConfigId", showTableConfigId);
		String code = data.get("code").toString();
		logger.info("====code:"+code);
		int appinfo_id=Integer.valueOf(data.get("appinfo_id").toString());
		logger.info("====appinfo_id:"+appinfo_id);
		
		
		
		String appinfosql="select * from  wx_t_appinfo where id=?";
	 	
	 	AppInfo appinfo = queryForSingle(appinfosql, new Object[]{appinfo_id},new RowMapperFactory.AppInfoRowMapper());
		
		//根据code获取openid
		WXService service=new WXService();
		
		FindOpenIdInfoByCodeResponse findOpenIdInfoByCodeResp=service.findOpenIdInfoByCode(appinfo.getAppid(), appinfo.getAppSecret(), code);
		logger.info("==findOpenIdInfoByCodeResp:"+findOpenIdInfoByCodeResp.getBody());
		
		String userOpenid=findOpenIdInfoByCodeResp.getOpenId(); //用户openid
		
		//业务有问题,判断用户是否已关注微信公众号？
		if(StringUtils.isEmpty(userOpenid)){
			if("40029".equals(findOpenIdInfoByCodeResp.getErrcode())){
				logger.info(findOpenIdInfoByCodeResp.getErrmsg());
				throw new InfoException(findOpenIdInfoByCodeResp.getErrmsg());
			}else{
				logger.info("==请先关注微信公众号，在进行申请微信公众平台管理员");
				throw new InfoException("请先关注微信公众号，在进行申请微信公众平台管理员");
			}
		}
		data.put("openid", userOpenid);
		logger.info("===参数设置成功 userOpenid："+userOpenid);
	}
	
	

	

}
