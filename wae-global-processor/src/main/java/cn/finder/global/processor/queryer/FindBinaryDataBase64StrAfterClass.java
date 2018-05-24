package cn.finder.global.processor.queryer;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.common.comm.ImageUtil;
import cn.finder.wae.queryer.handleclass.QueryerDBAfterClass;

/**
 * @author: whl
 * @function:获取图片的base64 字符串 media_ids 参数逗号隔开
 */
public class FindBinaryDataBase64StrAfterClass extends QueryerDBAfterClass {
	
	private Logger logger=Logger.getLogger(FindBinaryDataBase64StrAfterClass.class);

	@Override
	public TableQueryResult handle(TableQueryResult tableQueryResult,
			long showTableConfigId, QueryCondition<Object[]> condition) {
		 super.handle(tableQueryResult, showTableConfigId, condition);
		 
		 
		 @SuppressWarnings("unchecked")
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
			
		String[] media_ids = data.get("media_ids").toString().split(",");
		
		String sql="select guid_value,binary_data,file_type,ext_type from g_t_binary_data where ";
		
		
		
		Object[] args=new Object[media_ids.length];
		
		
		for(int i=0;i<media_ids.length;i++){
			if(i!=0)
				sql+=" or ";
			sql+=" guid_value=? ";
			args[i]=media_ids[i];
		}
		
		
		
		List<Map<String,Object>> dbitems = queryForList(sql, args);
		
		if(dbitems!=null && dbitems.size()>0){
			for(Map<String,Object> dbitem:dbitems){
				
				byte[] binary_data=(byte[])dbitem.get("binary_data");
				
				String byteBase64=ImageUtil.getImageToBase64(binary_data);
				
				
				dbitem.put("binary_data", byteBase64);
				
				
				
			}
			
			 tableQueryResult.setCount((long)dbitems.size());
		}
		
		else{
			 tableQueryResult.setCount((long)0);
		}
		
       
        tableQueryResult.setPageSize(1);
        tableQueryResult.setPageIndex(1);
        tableQueryResult.setResultList(dbitems);
		 
		 
		 return tableQueryResult;
		 
	}

	@Override
	public void setJDBCDataSource(DataSource dataSource) {
		super.setJdbcDataSource(dataSource);
	}

	
}
