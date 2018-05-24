package cn.finder.wx;

/**
 *
 * @author finder
 *
 * 开发者可以根据自己的服务器部署情况，选择最佳的接入点（延时更低，稳定性更高）。除此之外，可以将其他接入点用作容灾用途，当网络链路发生故障时，可以考虑选择备用接入点来接入。
1. 通用域名(api.weixin.qq.com)，使用该域名将访问官方指定就近的接入点；
2. 上海域名(sh.api.weixin.qq.com)，使用该域名将访问上海的接入点；
3. 深圳域名(sz.api.weixin.qq.com)，使用该域名将访问深圳的接入点；
4. 香港域名(hk.api.weixin.qq.com)，使用该域名将访问香港的接入点。
 */
public class DNLocation {

	public final static String DN_API="api.weixin.qq.com";
	public final static String DN_SH="sh.weixin.qq.com";
	public final static String DN_SZ="sz.weixin.qq.com";
	public final static String DN_HK="hk.weixin.qq.com";
}
