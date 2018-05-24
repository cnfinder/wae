package cn.finder.global.processor.queryer;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.common.qrcode.QRCodeUtil;
import cn.finder.wae.queryer.handleclass.QueryerDBAfterClass;

/**
 * 生成二维码
 * 
 * @author lizhi
 * 
 */
public class GeneratorQRQueryerAfter extends QueryerDBAfterClass {

	@Override
	public TableQueryResult handle(TableQueryResult tableQueryResult, long showTableConfigId, QueryCondition<Object[]> condition) {

		super.handle(tableQueryResult, showTableConfigId, condition);

		@SuppressWarnings("unchecked")
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>) condition).getMapParas();
		String media_id = data.get("media_id").toString();
		List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		Map<String, Object> item = new HashMap<String, Object>();

		String host = ArchCache.getInstance().getUserConfigCache().get("user_config_iv_app_host").getValue();
		String qr_url = host + "/service/rest/stream_interface?excludeProperties=fields&app_key=testKey&v=1.0&format=json&partner_id=api-sdk-js-v1.0&session=&timestamp=&method=wae.global.image.thumbnail.get&data={'media_id':'" + media_id + "'}";

		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		try {
			QRCodeUtil.encode(qr_url, outStream);
			byte[] buffer = outStream.toByteArray();
			item.put("binary_data", buffer);

		} catch (Exception e) {
			e.printStackTrace();
		}
		listData.add(item);
		tableQueryResult.setResultList(listData);
		return tableQueryResult;

	}

}
