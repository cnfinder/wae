package cn.finder.wae.queryer.handleclass.dataimport;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.ShowTableConfig;
import cn.finder.wae.business.module.common.service.CommonService;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.common.comm.AppApplicationContextUtil;
import cn.finder.wae.common.comm.Common;
import cn.finder.wae.queryer.handleclass.QueryerDBBeforeClass;

/****
 *  获取时间段的数据  参数设置  (每隔  N秒 获取 之后的所有的数据)
 * @author 吴华龙
 * 需要配置参数: 
 * 	user_config_interface_sort_field 对方表 时间排序的字段
 *  user_config_interface_dataimport_endtime 最后一次获取数据的时间 
 *  user_config_interface_getdata_duration   获取多长时间内的数据
 *  
 */
public class DurtionTimeDataImportQueryerBefore extends QueryerDBBeforeClass {

	
	CommonService commonService=AppApplicationContextUtil.getContext().getBean("commonService", CommonService.class);
	
	@Override
	public void handle(long showTableConfigId,
			QueryCondition<Object[]> condition) {
		super.handle(showTableConfigId, condition);
		
		
		String sortTimeField=ArchCache.getInstance().getUserConfigCache().get("user_config_interface_sort_field").getValue();
		
		String endTimeStr = commonService.findUserConfig("user_config_interface_dataimport_endtime");
		
		Date lastEndTime=null; // 获取HIS数据的 上次记录的最后时间
		ShowTableConfig showtableConfig = ArchCache.getInstance().getShowTableConfigCache().get(showTableConfigId);
		
		condition.setPageSize(showtableConfig.getPageSize());
		
		if(!StringUtils.isEmpty(endTimeStr)){
			lastEndTime = Common.parseDateFull(endTimeStr);
		}else{
			lastEndTime = Common.parseDate("1999-01-01 00:00:00");
		}
		condition.setWhereCluster(sortTimeField + " > ? ");
		condition.setWherepParameterValues(new Object[]{lastEndTime});
		
		
	}

	
}
