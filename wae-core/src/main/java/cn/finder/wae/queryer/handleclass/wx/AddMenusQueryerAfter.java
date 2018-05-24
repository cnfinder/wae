package cn.finder.wae.queryer.handleclass.wx;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.domain.wx.AppInfo;
import cn.finder.wae.business.domain.wx.WXMenu;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.business.rowmapper.RowMapperFactory;
import cn.finder.wae.queryer.handleclass.QueryerDBAfterClass;
import cn.finder.wae.wx.data.WXAppInfo;
import cn.finder.wx.response.AddMenuResponse;
import cn.finder.wx.response.FindAccessTokenResponse;
import cn.finder.wx.service.WXService;

/***
 * 微信添加菜单- 应该是从服务器 获取菜单 然后提交到微信服务器 
 * @author whl
 *
 */
public class AddMenusQueryerAfter extends QueryerDBAfterClass {
 
	@Override
	public TableQueryResult handle(TableQueryResult tableQueryResult,
			long showTableConfigId, QueryCondition<Object[]> condition) {
		
		 super.handle(tableQueryResult, showTableConfigId, condition);
		
		tableQueryResult= new TableQueryResult();
	 	tableQueryResult.getMessage().setMsg("添加菜单");
	 	tableQueryResult.setPageIndex(condition.getPageIndex());
	 	tableQueryResult.setPageSize(condition.getPageSize());
	 	tableQueryResult.setCount(1l);
			
	 			
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
	 	String menuid = data.get("menuid").toString();
	 	String errCode = null;
	 	String errMsg = null;
	 	List<AppInfo> appInfos = getJdbcTemplate().query("select * from wx_t_appinfo where menus_group_id=?",new Object[] {menuid},new RowMapperFactory.AppInfoRowMapper());
		AppInfo info = appInfos.get(0);
		WXService service = new WXService();
		cn.finder.wae.wx.data.AppInfo appinfo = WXAppInfo.getAppInfo(info.getAppid());
		//FindAccessTokenResponse wxresp = service.findAccessToken(info.getGrantType(), info.getAppid(), info.getAppSecret());
		if(appinfo.getAccessToken() != null){
	 		List<WXMenu> firstMenus = getJdbcTemplate().query("select * from wx_t_menus where p_id=?",new Object[] {menuid},new RowMapperFactory.WXMenus());
	 		for(WXMenu m:firstMenus){
		 		//获取二级菜单
		 		List<WXMenu> subMenus = getJdbcTemplate().query("select * from wx_t_menus where p_id = ?",new Object[] {m.getId()},new RowMapperFactory.WXMenus());
		 		m.setSubButton(subMenus);
		 	}
	 		List<cn.finder.wx.domain.WXMenu> newMenus = transferMenu(firstMenus);
		 	AddMenuResponse adresp = service.addMenu(appinfo.getAccessToken(), newMenus);
		 	errCode = adresp.getErrcode();
		 	errMsg = adresp.getErrmsg();
	 	}
	 	List<Map<String,Object>> listData = new ArrayList<Map<String,Object>>();
	 	Map<String,Object> item =new HashMap<String, Object>();
	 	item.put("errcode", errCode);
	 	item.put("errmsg", errCode);
	 	listData.add(item);
	 	tableQueryResult.setResultList(listData);
	 	tableQueryResult.getMessage().setMsg("添加菜单成功");
		return tableQueryResult;
		
	}
	
	/**
	 * 递归传递Menu的参数值
	 * @param oldMenus
	 * @return
	 */
	public List<cn.finder.wx.domain.WXMenu> transferMenu(List<WXMenu> oldMenus){
		List<cn.finder.wx.domain.WXMenu> newMenus = new ArrayList<cn.finder.wx.domain.WXMenu>();
		for(WXMenu oldMenu:oldMenus){
			cn.finder.wx.domain.WXMenu newMenu = new cn.finder.wx.domain.WXMenu();
			newMenu.setKey(oldMenu.getKey());
			newMenu.setName(oldMenu.getKey());
			newMenu.setType(oldMenu.getType());
			newMenu.setUrl(oldMenu.getUrl());
			if(oldMenu.getSubButton() != null){
				//递归传递参数
				newMenu.setSubButton(transferMenu(oldMenu.getSubButton()));
			}
			newMenus.add(newMenu);
		}
		return newMenus;
	}
	
}
