package cn.finder.wae.queryer.handleclass.ble;

import java.util.Map;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.business.module.common.service.CommonService;
import cn.finder.wae.common.comm.AppApplicationContextUtil;
import cn.finder.wae.common.exception.InfoException;
import cn.finder.wae.queryer.handleclass.QueryerDBBeforeClass;

public class BeaconUpdateQueryerBefore extends QueryerDBBeforeClass {
	@Override
	public void handle(long showTableConfigId,
			QueryCondition<Object[]> condition) {
		// TODO Auto-generated method stub
		super.handle(showTableConfigId, condition);
		
		
		logger.debug("====================BeaconUpdateQueryerBefore.handle ");
		logger.debug("====================showTableConfigId: "+showTableConfigId);
		
		@SuppressWarnings("unchecked")
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>) condition).getMapParas();
		
		CommonService commonService = AppApplicationContextUtil.getContext().getBean("commonService", CommonService.class);
		int id=Integer.valueOf(data.get("id").toString());
		String uuid=data.get("uuid").toString();
		
		String major=data.get("major").toString();
		String minor=data.get("minor").toString();
		
		//判断设备是否已经存在 
		boolean isExist = commonService.isBleEquipmentExists(id,uuid, major, minor);
		if(isExist)
			throw new InfoException("该设备已经存在!");
		
	}
}
