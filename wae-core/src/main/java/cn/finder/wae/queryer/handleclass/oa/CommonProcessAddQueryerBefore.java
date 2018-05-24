package cn.finder.wae.queryer.handleclass.oa;

import java.io.BufferedInputStream;
import java.net.URL;
import java.util.Date;
import java.util.Map;

import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.finder.httpcommons.utils.FileItem;
import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.oaFlow.OAFlowBase;
import cn.finder.wae.business.domain.wx.corp.CorpInfo;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.business.module.common.service.CommonService;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.common.thread.AppContent;
import cn.finder.wae.global.api.request.MediaUploadRequest;
import cn.finder.wae.global.api.service.GloablApiService;
import cn.finder.wae.queryer.handleclass.QueryerDBBeforeClass;
import cn.finder.wae.wx.data.CorpTokenInfo;
import cn.finder.wae.wx.data.WXAppInfo;
import cn.finder.wx.service.WXService;

/**
 * 通用流程上传
 * 
 * @author lizhi
 * 
 */
public class CommonProcessAddQueryerBefore extends QueryerDBBeforeClass {

	CommonService commonService = WebApplicationContextUtils.getWebApplicationContext(AppContent.getSession().getServletContext()).getBean("commonService", CommonService.class);

	@Override
	public void handle(long showTableConfigId, QueryCondition<Object[]> condition) {

		super.handle(showTableConfigId, condition);
		@SuppressWarnings("unchecked")
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>) condition).getMapParas();
		String wx_appid = "";
		if (data.get("wx_appid") != null) {
			wx_appid = data.get("wx_appid").toString();
		} else {
			String wx_corpinfo_id = data.get("wx_corpinfo_id").toString();
			CorpInfo corpInfo = commonService.findCorpInfo(Integer.parseInt(wx_corpinfo_id));
			CorpTokenInfo corpTokeInfo = WXAppInfo.getCorpAccessTokenInfo(corpInfo.getCorp_id());
			wx_appid = corpTokeInfo.getCorpid();
			data.put("wx_appid", wx_appid);
		}
		String remark = data.get("remark").toString();
		String user_id = data.get("user_id").toString();

		OAFlowBase oAFlowBase = new OAFlowBase();
		oAFlowBase.setContent(remark);
		Date current_date = new Date(System.currentTimeMillis());
		oAFlowBase.setCreate_time(current_date);
		oAFlowBase.setComplete_time(null);
		oAFlowBase.setInitiator_userid(user_id);
		OAFlowBase oaFlowBaseInfo = commonService.addOAFlowBase(oAFlowBase);
		String business_key = oaFlowBaseInfo.getBusiness_key();
		data.put("business_key", business_key);

		try {
			if (data.get("wx_img_serverIds") != null && data.get("wx_img_serverIds").toString().length() != 0) {
				String wx_img_serverIdstr = data.get("wx_img_serverIds").toString();
				String[] wx_img_serverIds = wx_img_serverIdstr.split(",");
				for (String img_serverId : wx_img_serverIds) {
					String mediaUrl = "";
					// 下载图片,企业号
					WXService.CorpService service = new WXService.CorpService();
					mediaUrl = service.mediaGet(WXAppInfo.getCorpAccessTokenInfo(wx_appid).getAccessToken(), img_serverId);
					logger.info("===mediaUrl:" + mediaUrl);
					// 下载成流数据
					BufferedInputStream mediaUrlStream = new BufferedInputStream(new URL(mediaUrl).openStream());
					byte[] buffer = new byte[mediaUrlStream.available()];
					mediaUrlStream.read(buffer);
					String mediaSrv = ArchCache.getInstance().getSysConfigCache().get("config_sys_wae_global_server").getValue();
					// ServiceInterfaceConfig.setContextRootUrl(mediaSrv);
					GloablApiService global_service = new GloablApiService();
					MediaUploadRequest req = new MediaUploadRequest();
					FileItem file = new FileItem("binary_data", buffer);
					req.setMedia_file(file);
					cn.finder.wae.global.api.domain.Media media = global_service.uploadMedia(mediaSrv, req);
					String media_id = media.getMedia_id();
					commonService.addOAFlowBaseAttach(business_key, media_id);
				}
			} else if (data.get("media_ids") != null && data.get("media_ids").toString().length() != 0) {
				String[] media_ids = data.get("media_ids").toString().split(",");
				for (String media_id : media_ids) {
					commonService.addOAFlowBaseAttach(business_key, media_id);
				}
			}
		} catch (Exception e) {

		}

	}

}
