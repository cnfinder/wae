package cn.finder.wae.queryer.handleclass;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.business.module.common.service.CommonService;
import cn.finder.wae.common.thread.AppContent;

/**
 * 添加图表到用户
 * 
 * @author lizhi
 * 
 */
public class AddChartAfterClass extends QueryerDBAfterClass {

	CommonService commonService = WebApplicationContextUtils.getWebApplicationContext(AppContent.getSession().getServletContext()).getBean("commonService", CommonService.class);

	@Override
	public TableQueryResult handle(TableQueryResult tableQueryResult, long showTableConfigId, QueryCondition<Object[]> condition) {
		super.handle(tableQueryResult, showTableConfigId, condition);

		@SuppressWarnings("unchecked")
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>) condition).getMapParas();
		String chart_ids_str = data.get("chart_ids").toString();
		String[] chart_ids = chart_ids_str.split(",");
		String user_id = data.get("user_id").toString();
		//删除所有图表
		commonService.deleteChartFromUser(user_id);
		if(chart_ids_str != null && chart_ids_str.length() > 0){
			for (String char_id : chart_ids) {
				//添加选择的菜单
				commonService.addChartToUser(Integer.parseInt(char_id), user_id);
			}
		}
		return tableQueryResult;

	}

	@Override
	public void setJDBCDataSource(DataSource dataSource) {
		super.setJdbcDataSource(dataSource);
	}

}
