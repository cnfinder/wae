package cn.finder.wae.queryer.common;



import java.util.List;
import java.util.Map;

import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.ShowDataConfig;
import cn.finder.wae.business.domain.properties.PropertiesTemplateItem;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.business.module.common.service.CommonService;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.common.thread.AppContent;
import cn.finder.wae.queryer.handleclass.QueryerDBBeforeClass;

/**
 * 
 * @author  finder
 * @function: 生成业务属性
 */
public class PropertiesGenerateQueryerBefore extends QueryerDBBeforeClass {

	CommonService commonService = WebApplicationContextUtils.getWebApplicationContext(AppContent.getSession().getServletContext()).getBean("commonService", CommonService.class);


	@Override
	public void handle(long showTableConfigId,
			QueryCondition<Object[]> condition) {
		super.handle(showTableConfigId, condition);

		@SuppressWarnings("unchecked")
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>) condition).getMapParas();
		String propertiesTemplateTypeCode = data.get("properties_template_type_code").toString();
		
		String business_key =  data.get("business_key").toString();
	
		List<PropertiesTemplateItem> propertiesTemplateItem = commonService.findPropertiesTemplate(propertiesTemplateTypeCode);
		
		ShowDataConfig pksdf= ArchCache.getInstance().getShowTableConfigCache().getPKShowDataConfig(showTableConfigId);
	   String tableName=	pksdf.getFieldTableName();
	   
	   String tableNameSql = "select count(*)  from "+tableName+" where  business_key =? AND  properties_template_type_code =?";
	   int count = queryForInt(tableNameSql,new Object[]{business_key,propertiesTemplateTypeCode});
	   
	   if(count==0){
			if(propertiesTemplateItem!=null && propertiesTemplateItem.size()>0){
				
				String sql = "insert into "+tableName+"(name,show_type,data_type,process_command,data_showtableConfigId," +
						"data_source_static,prop_name,is_new_line,is_required,sort,business_key,properties_template_type_code) values(?,?,?,?,?,?,?,?,?,?,?,?) ";
				for(int i=0;i<propertiesTemplateItem.size();i++){
					PropertiesTemplateItem propitem= propertiesTemplateItem.get(i);
					String name=propitem.getName();
					int show_type = propitem.getShow_type();
					int data_type = propitem.getData_type();
					String process_command = propitem.getProcess_command();
					int data_showtableConfigId = propitem.getData_showtableConfigId();
					String data_source_static = propitem.getData_source_static();
					String prop_name = propitem.getProp_name();
					int is_new_line = propitem.getIs_new_line();
					int is_required = propitem.getIs_required();
					int sort = propitem.getSort();
					
					getJdbcTemplate().update(sql, new Object[]{name,show_type,data_type,process_command,data_showtableConfigId,data_source_static,
							prop_name,is_new_line,is_required,sort,business_key,propertiesTemplateTypeCode});
				}
			}
	   }

	}

}
