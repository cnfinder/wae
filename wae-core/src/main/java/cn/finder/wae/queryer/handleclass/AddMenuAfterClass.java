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
 * 添加菜单到app
 * 
 * @author lizhi
 * 
 */
public class AddMenuAfterClass extends QueryerDBAfterClass {

	CommonService commonService = WebApplicationContextUtils.getWebApplicationContext(AppContent.getSession().getServletContext()).getBean("commonService", CommonService.class);

	@Override
	public TableQueryResult handle(TableQueryResult tableQueryResult, long showTableConfigId, QueryCondition<Object[]> condition) {
		super.handle(tableQueryResult, showTableConfigId, condition);

		@SuppressWarnings("unchecked")
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>) condition).getMapParas();
		String menu_ids_str = data.get("menu_ids").toString();
		String[] menu_ids = menu_ids_str.split(",");
		int product_id = Integer.parseInt(data.get("product_id").toString());
		String user_id = data.get("user_id").toString();
		//删除所有菜单
		commonService.deleteMenuFromApp(product_id, user_id);
		if(menu_ids_str != null && menu_ids_str.length() > 0){
			for (String menu_id : menu_ids) {
				//添加选择的菜单
				commonService.addMenuToApp(Integer.parseInt(menu_id), product_id, user_id);
			}
		}
		return tableQueryResult;

	}

	@Override
	public void setJDBCDataSource(DataSource dataSource) {
		super.setJdbcDataSource(dataSource);
	}

}
