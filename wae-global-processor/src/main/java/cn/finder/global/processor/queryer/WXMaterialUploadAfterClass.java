package cn.finder.global.processor.queryer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import cn.finder.httpcommons.utils.FileItem;
import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.common.exception.InfoException;
import cn.finder.wae.queryer.handleclass.QueryerDBAfterClass;
import cn.finder.wx.request.MediaAddTempRequest;
import cn.finder.wx.response.MediaAddTempResponse;
import cn.finder.wx.service.WXService;

/**
 * @author: whl
 * @function: 上传到微信临时素材
 */
public class WXMaterialUploadAfterClass extends QueryerDBAfterClass {
	
	private Logger logger=Logger.getLogger(WXMaterialUploadAfterClass.class);

	@Override
	public TableQueryResult handle(TableQueryResult tableQueryResult,
			long showTableConfigId, QueryCondition<Object[]> condition) {
		 super.handle(tableQueryResult, showTableConfigId, condition);
		 
		 List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
			Map<String,Object> item=new HashMap<String, Object>();
			list.add(item);
			tableQueryResult.setResultList(list);
		 
		 @SuppressWarnings("unchecked")
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
			
		 
		String access_token="";
		try{
			access_token=data.get("access_token").toString();
		}
		catch(Exception e){
			
		}
		
		if(!StringUtils.isEmpty(access_token)){
			String media_id="";
			
			
			try{
				media_id=data.get("media_id").toString();
				
				//
				String name="";
				
				try{
					name=data.get("name").toString();// 来自客户端  如果需要上传微信资源  文件名 需要有扩展名
				}
				catch(Exception e){
					name="unknow.jpg";
				}
				if(name.indexOf(".")==-1){
					//从 数据库中查找
					
					String sql="select ext_type from g_t_binary_data where  guid_value=?";
					String ext_type=getJdbcTemplate().queryForObject(sql, new Object[]{media_id},String.class);
					
					name="."+ext_type;
				}
				
				
				byte[] orig_binary_data=(byte[])data.get("binary_data");
				//上传数据到
				WXService service=new WXService();
				
				FileItem file=new FileItem(name, orig_binary_data);
				
				MediaAddTempResponse resp= service.mediaAdd(access_token, MediaAddTempRequest.MEDIA_TYPE_IMAGE, file);
				
				if(resp.isSuccess()){
					
					
					//更新到 文件数据库
					String sql="update g_t_binary_data set wx_media_id=? where guid_value=?";
					int eff_cnt=getJdbcTemplate().update(sql, new Object[]{resp.getMedia_id(),media_id});
					if(eff_cnt>0){
						//根据 
						item.put("wx_media_id", resp.getMedia_id());//返回 wx_media_id
					}
					
				}
			}
			catch(Exception e){
				if("-1".equalsIgnoreCase(media_id)){
					//文件上传本地失败
					throw new InfoException("上传到文件服务器失败");
				}
				
			}
			
			
			
			
			
			
			
		}
		 
		
		
		//更新 wx_media_id by media_id
		
		 
		
		
        tableQueryResult.setCount(1l);
        tableQueryResult.setPageSize(1);
        tableQueryResult.setPageIndex(1);
	       
		 
		 
		 return tableQueryResult;
		 
	}

	@Override
	public void setJDBCDataSource(DataSource dataSource) {
		super.setJdbcDataSource(dataSource);
	}

	
}
