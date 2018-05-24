package cn.finder.wae.httpcommons;

/***
 * 全局配置
 * @author dragon
 *
 */
public class ApiConfig {
	 /// <summary>
    ///  接口服务参数配置, 应在应用启动加载
    /// </summary>
    public static class ServiceInterfaceConfig
    {
        public static String AppKey = "testKey";

        public static String AppSecret = "testSecret";

        
        private static String contextRootUrl;
        
        /// <summary>
        /// 应用根目录 http://xxx.xxx.xxx.xxx:80/APS
        /// </summary>
        public static String getContextRootUrl() {
            return contextRootUrl;
        }
        
        public static void setContextRootUrl(String contextRootUrl) {
        	ServiceInterfaceConfig.contextRootUrl=contextRootUrl;
        }
        

        /// <summary>
        /// 普通请求服务地址
        /// </summary>
        public static String getAuthInterfaceUrl(){
            return getContextRootUrl() + "/service/rest/auth/login";
                
        }
        
        public static String getAuthInterfaceUrl(String url){
            return url + "/service/rest/auth/login";
                
        }
        /***
         * 获取 检验 session url
         * @param url
         * @return
         */
        public static String getCheckSessionUrl(String url){
        	return url+"/service/rest/auth/checkSession";
        }

        /***
         * 获取 refresh session url
         * @param url
         * @return
         */
        public static String getRefreshSessionUrl(String url){
        	return url+"/service/rest/auth/refreshSession";
        }
       
        
        /// <summary>
        /// 普通请求服务地址
        /// </summary>
        public static String getServiceInterfaceUrl(String... excludeProperties){
                 return  getUrl(getContextRootUrl() + "/service/rest/interface",excludeProperties);
        }

        /// <summary>
        /// 下载文件数据服务地址
        /// </summary>
        public static String getServiceInterfaceStreamUrl(String... excludeProperties) {
                return getUrl(getContextRootUrl() + "/service/rest/stream_interface",excludeProperties);
        }


        /// <summary>
        /// 上传文件数据服务地址
        /// </summary>
        public static String getServiceInterfaceUploadStreamUrl(String... excludeProperties) {
                return getUrl(getContextRootUrl() + "/service/rest/upload_interface",excludeProperties);
        }


      /// <summary>
        /// 普通请求服务地址
        /// </summary>
        public static String getServiceInterfaceUrlExt(String contextRootUrl,String... excludeProperties){
                 return  getUrl(contextRootUrl + "/service/rest/interface",excludeProperties);
        }
        
        /// <summary>
        /// 下载文件数据服务地址
        /// </summary>
        public static String getServiceInterfaceStreamUrlExt(String contextRootUrl,String... excludeProperties) {
                return getUrl(contextRootUrl + "/service/rest/stream_interface",excludeProperties);
        }
        /// <summary>
        /// 上传文件数据服务地址
        /// </summary>
        public static String getServiceInterfaceUploadStreamUrlExt(String contextRootUrl,String... excludeProperties) {
                return getUrl(contextRootUrl + "/service/rest/upload_interface",excludeProperties);
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="url">被包装的服务地址</param>
        /// <param name="includeProperties">包含序列化的属性</param>
        /// <param name="excludeProperties"> 不包含序列化属性 如不包含 fields -》 new String[]{"fields"}</param>
        /// <returns></returns>
        public static String getUrl(String url, String... excludeProperties)
        {
            StringBuilder sb_url = new StringBuilder();
            sb_url.append(url);
            if (excludeProperties != null && excludeProperties.length > 0)
            {

                sb_url.append("?excludeProperties=");
                for (String item : excludeProperties)
                {
                    sb_url.append(item).append(",");
                }
                sb_url.deleteCharAt(sb_url.length()-1);
            }



            return sb_url.toString();

        }
        
        
        


        /// <summary>
        /// 获取主机 或者IP地址
        /// </summary>
        public static String getHost()
        {

            String url = getContextRootUrl().toString().toLowerCase();

            String url_tmp = url.replace("http://", "");
            url_tmp += "/";
            url_tmp = url_tmp.substring(0, url_tmp.indexOf("/"));

            String[] datas = url_tmp.split(":");

          
            return datas[0];

        }


        /// <summary>
        /// 获取端口
        /// </summary>
        public  static int getPort()
        {

                String url = getContextRootUrl().toString().toLowerCase();

                String url_tmp= url.replace("http://", "");
                url_tmp += "/";
                url_tmp = url_tmp.substring(0, url_tmp.indexOf("/"));

                String[] datas = url_tmp.split(":");

                try
                {
                    return Integer.valueOf(datas[1]);
                }
                catch(Exception e)
                {
                    return 80;
                }

              
        }



        
        public static void main(String[] args){
        	System.out.print(getServiceInterfaceUploadStreamUrlExt("http://iv.cwintop.com/iv"));
        }



    }
}
