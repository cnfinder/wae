package cn.finder.wae.common.comm;

import javax.servlet.http.HttpServletRequest;

public class WebUtil {
	/**
	 * 获取远程用户IP
	 * 
	 * @param request
	 * @return
	 */
	public static String getIp(HttpServletRequest request) {
		String ip = request.getRemoteAddr();
		if (ip == null || ip.trim().length() == 0
				|| "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("x-forwarded-for");
		}
		if (ip == null || ip.trim().length() == 0
				|| "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.trim().length() == 0
				|| "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.trim().length() == 0
				|| "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Real-IP");
		}
		if (ip != null && ip.startsWith("0:0")) {
			ip = "127.0.0.1";
		}
		return ip;
	}

	
	/***
	 * @param request
	 * @return
	 */
	public static String getRequestURL(HttpServletRequest request) {
		if (request == null) {
			return "";
		}
		String url = "";
		url = request.getContextPath();
		url = url + request.getServletPath();

		java.util.Enumeration names = request.getParameterNames();
		int i = 0;
		if (!"".equals(request.getQueryString())
				|| request.getQueryString() != null) {
			url = url + "?" + request.getQueryString();
		}

		if (names != null) {
			while (names.hasMoreElements()) {
				String name = (String) names.nextElement();
				if (i == 0) {
					url = url + "?";
				} else {
					url = url + "&";
				}
				i++;

				String value = request.getParameter(name);
				if (value == null) {
					value = "";
				}

				url = url + name + "=" + value;
				try {
					// java.net.URLEncoder.encode(url, "ISO-8859");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		try {
			// String enUrl = java.net.URLEncoder.encode(url, "utf-8");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return url;
	}
}
