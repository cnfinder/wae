package cn.finder.wae.queryer.handleclass.wx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.domain.wx.AppInfo;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.business.module.common.service.CommonService;
import cn.finder.wae.common.comm.AppApplicationContextUtil;
import cn.finder.wae.queryer.handleclass.QueryerDBAfterClass;
import cn.finder.wae.wx.data.WXAppInfo;
import cn.finder.wx.request.WXMenuRequest;
import cn.finder.wx.service.WXService;

/***
 * 
 * 微信菜单推送到 微信服务器
 * @author whl
 *
 */
public class WXMenuPushServerQueryerAfter extends QueryerDBAfterClass {
 

 	private CommonService commonService = AppApplicationContextUtil.getContext().getBean("commonService", CommonService.class);;
 	int wx_appinfo_id;
	//微信菜单
	List<Map<String,Object>> menus=new ArrayList<Map<String,Object>>();
			
	@SuppressWarnings("unchecked")
	@Override
	public TableQueryResult handle(TableQueryResult tableQueryResult,
			long showTableConfigId, QueryCondition<Object[]> condition) {
		
		 super.handle(tableQueryResult, showTableConfigId, condition);
		
		tableQueryResult= new TableQueryResult();
		tableQueryResult.getMessage().setMsg("微信菜单刷新成功");
	 	tableQueryResult.setPageIndex(condition.getPageIndex());
	 	tableQueryResult.setPageSize(condition.getPageSize());
	 	tableQueryResult.setCount(1l);
	 	
		List<Map<String,Object>> listData = new ArrayList<Map<String,Object>>();
	 	Map<String,Object> ret_item =new HashMap<String, Object>();
	 	
	 	listData.add(ret_item);
	 	tableQueryResult.setResultList(listData);
	 	
	 	
	 	
		
	 		
		@SuppressWarnings("unchecked")
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();

		
		
		wx_appinfo_id =Integer.parseInt(data.get("wx_appinfo_id").toString());
		
		List<Map<String,Object>> menuList= commonService.findWxMenus(wx_appinfo_id, -1);
		
		
		if(menuList==null || menuList.size()==0){
			tableQueryResult.getMessage().setMsg("无菜单，不可刷新");
		}else{
			
			for(int i=0;i<menuList.size();i++){
				
				Map<String,Object> menu_item=new HashMap<String, Object>();//微信每项菜单
				
				Map<String,Object> item=menuList.get(i);
				menuParser(item,menu_item);
				
				menus.add(menu_item);
				
			}
			
		    AppInfo appInfo=commonService.findAppInfo(wx_appinfo_id);
		    
			
			//通过菜单接口提交
			WXService service=new WXService();
			
			WXMenuRequest req=new WXMenuRequest();
			req.setMenus(menus);
			
			boolean success=service.addWXMenus(WXAppInfo.getAppInfo(appInfo.getAppid()).getAccessToken(), req);
			ret_item.put("result", success);
		}
		
		
	
	 	
		return tableQueryResult;
		
	}
	
	
	
	/***
	 * 
	 * @param item 数据库 返回的对象
	 * @Param wx_item 微信结果对象
	 * @return
	 */
	private void menuParser(Map<String,Object> item,Map<String,Object> wx_item){
		
	
		
		String name=item.get("name").toString();
		wx_item.put("name", name);//每个都需要
		
		int menuId=Integer.parseInt(item.get("id").toString());
		
		List<Map<String,Object>> childMenus= commonService.findWxMenus(wx_appinfo_id, menuId);
		if(childMenus==null || childMenus.size()==0){
			//无子菜单
			
			//判断 type
			String type=item.get("type").toString();
			
			if("click".equalsIgnoreCase(type)){
				//click 需要 获取 key
				String key=item.get("key_id").toString();
				
				wx_item.put("type", type);
				wx_item.put("key", key);
				
				
			}else if("view".equalsIgnoreCase(type)){
				
				String url=item.get("url").toString();
				wx_item.put("type", type);
				wx_item.put("url", url);
			}
			//其他的 type  暂时不考虑
			
		}else{
			List<Map<String,Object>> wx_childMenus=new ArrayList<Map<String,Object>>();
			wx_item.put("sub_button", wx_childMenus);
			
			
			for(int i=0;i<childMenus.size();i++){
				
				Map<String,Object> wx_menu_item=new HashMap<String, Object>();//微信每项菜单
				
				Map<String,Object> db_item=childMenus.get(i);
				
				menuParser(db_item,wx_menu_item);
				
				wx_childMenus.add(wx_menu_item);
				
			}
			
			
			
			
			
		}
		
	}
	
	
}
