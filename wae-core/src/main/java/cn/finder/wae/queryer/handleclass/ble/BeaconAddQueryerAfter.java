package cn.finder.wae.queryer.handleclass.ble;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.domain.ble.BleEquipment;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.business.module.common.service.CommonService;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.common.comm.AppApplicationContextUtil;
import cn.finder.wae.queryer.handleclass.QueryerDBAfterClass;

/**
 * @author: wuhualong
 * @data: 2015-12-04
 * @function: 新增BLE设备后置处理
 */
public class BeaconAddQueryerAfter  extends QueryerDBAfterClass {

	/* (non-Javadoc)
	 * @see cn.finder.wae.queryer.handleclass.QueryerAfterClass#handle(cn.finder.wae.business.domain.TableQueryResult, long, cn.finder.ui.webtool.QueryCondition)
	 */
	@Override
	public TableQueryResult handle(TableQueryResult tableQueryResult,long showTableConfigId,QueryCondition<Object[]> condition){
		super.handle(tableQueryResult, showTableConfigId, condition);
		
		
		
		 
		 @SuppressWarnings("unchecked")
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
			
		 String name=data.get("name").toString();
		int equip_number=Integer.valueOf(data.get("equip_number").toString());
		
		String loc_address=data.get("loc_address").toString();
		
		String remark=data.get("remark").toString();
		
		CommonService commonService = AppApplicationContextUtil.getContext().getBean("commonService", CommonService.class);
		
		
		String uuid=ArchCache.getInstance().getSysConfigCache().get("config_sys_ble_company_uuid").getValue();
		String major=commonService.bleGeneratorNewMajor();
		
		for(int i=0;i<equip_number;i++){
			BleEquipment item=new BleEquipment();
			
			String equipment_code=commonService.bleGeneratorNewEquipmentCode(); //8为数字 
			//获取最大的设备id
			
			//String uuid=UUID.randomUUID().toString();
			//93EF3F7D-F300-4E2C-AE17-52E4EDE6792F
		
			String minor=commonService.bleGeneratorNewMinor();
			
			item.setName(name);
			
			item.setEquipmentCode(equipment_code);
			item.setUuid(uuid);
			item.setMajor(major);
			item.setMinor(minor);
			item.setLocAddress(loc_address);
			item.setRemark(remark);
			
			commonService.addBleEquipment(item);
		}
		
		
		
		
		return tableQueryResult;
	}
	
	
	
	 
	
	
}
