package cn.finder.wae.api.sdk;

/**
 *
 * @author finder
 *
 * 开发者可以根据自己的服务器部署情况，选择最佳的接入点（延时更低，稳定性更高）。除此之外，可以将其他接入点用作容灾用途，当网络链路发生故障时，可以考虑选择备用接入点来接入。
1. 通用域名(api.aa.tt.com)，使用该域名将访问官方指定就近的接入点；

 */
public class DNLocation {

	public  static String DN_API="https://api.cnxingkong.cn";
}
