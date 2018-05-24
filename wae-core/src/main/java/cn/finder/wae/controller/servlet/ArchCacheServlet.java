package cn.finder.wae.controller.servlet;

import java.util.HashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;

import cn.finder.wae.business.domain.Constants;
import cn.finder.wae.business.domain.PageIndex;
import cn.finder.wae.business.domain.Role;
import cn.finder.wae.business.domain.ServiceInterface;
import cn.finder.wae.business.domain.ShowTableConfig;
import cn.finder.wae.business.domain.SysConfig;
import cn.finder.wae.business.domain.UserConfig;
import cn.finder.wae.business.domain.wx.WXCommand;
import cn.finder.wae.business.module.common.service.CommonService;
import cn.finder.wae.business.module.sys.service.SysService;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.cache.redis.RedisCache;
import cn.finder.wae.common.comm.AppApplicationContextUtil;
import cn.finder.wae.common.constant.Constant;

/***
 * 服务器启动 载入缓存
 * 
 * @author finder
 *
 */
public class ArchCacheServlet extends HttpServlet {

	private static Logger logger = Logger.getLogger(ArchCacheServlet.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 8847503911589250340L;

	private SysService sysService;

	private CommonService commonService;

	public void init(ServletConfig servletConfig) throws ServletException {

		ServletContext ctxt = servletConfig.getServletContext();

		sysService = WebApplicationContextUtils.getWebApplicationContext(ctxt).getBean("sysService", SysService.class);
		commonService = WebApplicationContextUtils.getWebApplicationContext(ctxt).getBean("commonService",
				CommonService.class);

		boolean cache_enable = Boolean.parseBoolean(servletConfig.getInitParameter("cache-enable"));

		String cache_mode = servletConfig.getInitParameter("cache-mode");

		if (cache_enable) {
			// 开启初始化缓存

			if ("local".equalsIgnoreCase(cache_mode)) {
				archCacheLoad(ctxt);
			} else if ("memcached".equalsIgnoreCase(cache_mode)) {

				SockIOPool s = AppApplicationContextUtil.getContext().getBean("memcachedPool", SockIOPool.class);
				logger.info("s=" + s.getInitConn());

				MemCachedClient mc = AppApplicationContextUtil.getContext().getBean("memcachedClient",
						MemCachedClient.class);

				// 开始设值
				mc.set("name", " string  ");
				mc.set("int", 5);
				mc.set("double", 5.5);

				// 开始取值
				String name = (String) mc.get("name");
				int i = (Integer) mc.get("int");
				double d = (Double) mc.get("double");

				System.out.println("字符串：" + name);
				System.out.println("数字型：" + i);
				System.out.println("双精度：" + d);

				throw new RuntimeException("因为不推荐使用memcached 所有暂无实现, 推荐使用 redis缓存");
			} else if ("redis".equalsIgnoreCase(cache_mode)) {

				RedisCache redisCache = new RedisCache();
				/*
				 * redisCache.add("a", 1); redisCache.add("b", "2");
				 * 
				 * String b=redisCache.getString("b");
				 * 
				 * int a=redisCache.getInt("a");
				 * 
				 * System.out.println("a="+a);
				 */

				archCacheLoad(ctxt);
			}

		}

	}

	private void archCacheLoad(ServletContext ctxt) {
		logger.info("WAE  CacheServlet.init  start....  ");

		logger.info("WAE cache clearing ...");

		logger.info("WAE cache cleared ...");

		logger.info("load constants start ...");

		try {

			HashMap<Long, Constants> constants = sysService.loadConstantsCache();
			ArchCache.getInstance().getConstantsCache().clear();
			ArchCache.getInstance().getConstantsCache().add(constants);

			logger.info("load sysconfig  start....  ");
			HashMap<String, SysConfig> sysConfigs = sysService.loadSysConfigCache();
			ArchCache.getInstance().getSysConfigCache().clear();
			ArchCache.getInstance().getSysConfigCache().add(sysConfigs);

			logger.info("load show table  start....  ");

			HashMap<Long, ShowTableConfig> showTableConfigs = sysService.loadShowTable();
			ArchCache.getInstance().getShowTableConfigCache().clear();
			ArchCache.getInstance().getShowTableConfigCache().add(showTableConfigs);

			logger.info("load role authorities start ...");

			HashMap<Long, Role> roles = sysService.loadRoleCache();
			ArchCache.getInstance().getRoleCache().clear();
			ArchCache.getInstance().getRoleCache().add(roles);

			// ****** loading other cache *************//
			// load Menu cache

			logger.info("load serviceinterface cache start...");

			HashMap<String, ServiceInterface> serviceInterfaces = sysService.loadServiceInterfaceCache();
			ArchCache.getInstance().getServiceInterfaceCache().clear();
			ArchCache.getInstance().getServiceInterfaceCache().add(serviceInterfaces);

			logger.info("load userconfig  start....  ");

			HashMap<String, UserConfig> userConfigs = sysService.loadUserConfigCache();
			ArchCache.getInstance().getUserConfigCache().clear();
			ArchCache.getInstance().getUserConfigCache().add(userConfigs);
			UserConfig uc = ArchCache.getInstance().getUserConfigCache().get("user_config_safe_days");

			
			logger.info("load wxcommand  start....  ");
			HashMap<String, WXCommand> wxCommands = commonService.loadWXCommand();
			ArchCache.getInstance().getWxCommandCache().clear();
			ArchCache.getInstance().getWxCommandCache().add(wxCommands);
			
			logger.info("load page index  start....  ");
			HashMap<String, PageIndex> pageIndexs = sysService.loadPageIndexCache();
			ArchCache.getInstance().getPageIndexCache().clear();
			ArchCache.getInstance().getPageIndexCache().add(pageIndexs);

			

			logger.info("WAE  CacheServlet.init  complete....  ");

			ctxt.setAttribute(Constant.KEY_APPLICATION_CACHE, ArchCache.getInstance());

		} catch (Throwable e) {
			logger.error(e);
		}
	}

}
