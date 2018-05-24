package cn.finder.wae.common.http;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ApiUtils {

	        private static Map<String, Integer> switchDict;
	        public final String API_AUTH_URL = "http://cnxingkong.cn/container?authcode=";

	        protected ApiUtils()
	        {
	        }
	        /// <summary>
	        /// 清理字典集合，去掉value中为 null的 项
	        /// </summary>
	        /// <typeparam name="T"></typeparam>
	        /// <param name="dict"></param>
	        /// <returns></returns>
	public static <T> Map<String, T> CleanupDictionary(Map<String, T> dict) {

		Map<String, T> map = new HashMap<String, T>(dict.size());

		Set<Entry<String, T>> setEntry = dict.entrySet();

		Iterator<Entry<String, T>> itr = setEntry.iterator();

		while (itr.hasNext()) {

			Entry<String, T> entry = itr.next();

			String key = entry.getKey();
			T value = entry.getValue();

			if (value != null) {
				map.put(key, value);
			}

		}
		return map;

	}

	       /* public static IDictionary<string, string> DecodeParams(string topParams)
	        {
	            return DecodeParams(topParams, Encoding.GetEncoding("GBK"));
	        }

	        public static IDictionary<string, string> DecodeParams(string topParams, Encoding encoding)
	        {
	            if (string.IsNullOrEmpty(topParams))
	            {
	                return null;
	            }
	            byte[] bytes = Convert.FromBase64String(Uri.UnescapeDataString(topParams));
	            return SplitUrlQuery(encoding.GetString(bytes));
	        }

	        public static long GetCurrentTimeMillis()
	        {
	            return (long) DateTime.UtcNow.Subtract(new DateTime(0x7b2, 1, 1)).TotalMilliseconds;
	        }*/

	        public static String GetFileSuffix(byte[] fileData)
	        {
	            if ((fileData != null) && (fileData.length >= 10))
	            {
	                if (((fileData[0] == 0x47) && (fileData[1] == 0x49)) && (fileData[2] == 70))
	                {
	                    return "GIF";
	                }
	                if (((fileData[1] == 80) && (fileData[2] == 0x4e)) && (fileData[3] == 0x47))
	                {
	                    return "PNG";
	                }
	                if (((fileData[6] == 0x4a) && (fileData[7] == 70)) && ((fileData[8] == 0x49) && (fileData[9] == 70)))
	                {
	                    return "JPG";
	                }
	                if ((fileData[0] == 0x42) && (fileData[1] == 0x4d))
	                {
	                    return "BMP";
	                }
	            }
	            return null;
	        }

	        public static String GetMimeType(byte[] fileData)
	        {
	        	String fileSuffix = GetFileSuffix(fileData);
	            if (fileSuffix != null)
	            {
	                int num;
	                if (switchDict== null)
	                {
	                    Map<String, Integer> dictionary = new HashMap<String, Integer>(4);
	                    dictionary.put("JPG", 0);
	                    dictionary.put("GIF", 1);
	                    dictionary.put("PNG", 2);
	                    dictionary.put("BMP", 3);
	                    switchDict = dictionary;
	                }
	                
	                Object numObj = switchDict.get(fileSuffix);
	                
	                if(numObj!=null)
	                {
	                	num = ((Integer)numObj).intValue();
	                
	                
	                    switch (num)
	                    {
	                        case 0:
	                            return "image/jpeg";
	
	                        case 1:
	                            return "image/gif";
	
	                        case 2:
	                            return "image/png";
	
	                        case 3:
	                            return "image/bmp";
	                    }
	                }
	            }
	            return "application/octet-stream";
	        }

	       /* public static string GetMimeType(string fileName)
	        {
	            fileName = fileName.ToLower();
	            if (fileName.EndsWith(".bmp", StringComparison.CurrentCulture))
	            {
	                return "image/bmp";
	            }
	            if (fileName.EndsWith(".gif", StringComparison.CurrentCulture))
	            {
	                return "image/gif";
	            }
	            if (fileName.EndsWith(".jpg", StringComparison.CurrentCulture) || fileName.EndsWith(".jpeg", StringComparison.CurrentCulture))
	            {
	                return "image/jpeg";
	            }
	            if (fileName.EndsWith(".png", StringComparison.CurrentCulture))
	            {
	                return "image/png";
	            }
	            return "application/octet-stream";
	        }

	        public static string GetRootElement(string api)
	        {
	            int index = api.IndexOf(".");
	            if ((index != -1) && (api.Length > index))
	            {
	                api = api.Substring(index + 1).Replace('.', '_');
	            }
	            return (api + "_response");
	        }

	        public static ApiContext GetContext(string authCode)
	        {
	            string url = "http://container.open.taobao.com/container?authcode=" + authCode;
	            string str2 = new WebUtils().DoGet(url, null);
	            if (string.IsNullOrEmpty(str2))
	            {
	                return null;
	            }
	            ApiContext context = new ApiContext();
	            IEnumerator<KeyValuePair<string, string>> enumerator = SplitUrlQuery(str2).GetEnumerator();
	            while (enumerator.MoveNext())
	            {
	                KeyValuePair<string, string> current = enumerator.Current;
	                if ("top_parameters".Equals(current.Key))
	                {
	                    KeyValuePair<string, string> pair2 = enumerator.Current;
	                    context.AddParameters(DecodeParams(pair2.Value));
	                }
	                else
	                {
	                    KeyValuePair<string, string> pair3 = enumerator.Current;
	                    KeyValuePair<string, string> pair4 = enumerator.Current;
	                    context.AddParameter(pair3.Key, pair4.Value);
	                }
	            }
	            return context;
	        }

	        public static IDictionary ParseJson(string json)
	        {
	            return (JsonConvert.Import(json) as IDictionary);
	        }

	        public static T ParseResponse<T>(string json) where T: ApiResponse
	        {
	            JsonParser parser = new JsonParser();
	            return parser.Parse<T>(json);
	        }

	        public static string SignRequest(IDictionary<string, string> parameters, string appSecret)
	        {
	            return SignRequest(parameters, appSecret, false);
	        }

	        /// <summary>
	        /// 对参数签名 （appSecret+对参数+appSecret）
	        /// </summary>
	        /// <param name="parameters">参数字典 key-value</param>
	        /// <param name="appSecret">应用秘钥</param>
	        /// <param name="qhs"></param>
	        /// <returns></returns>
	        public static string SignRequest(IDictionary<string, string> parameters, string appSecret, bool qhs)
	        {
	            IDictionary<string, string> dictionary = new SortedDictionary<string, string>(parameters);
	            IEnumerator<KeyValuePair<string, string>> enumerator = dictionary.GetEnumerator();
	            StringBuilder builder = new StringBuilder(appSecret);
	            while (enumerator.MoveNext())
	            {
	                KeyValuePair<string, string> current = enumerator.Current;
	                string key = current.Key;

	                string value = current.Value;
	                if (!string.IsNullOrEmpty(key) && !string.IsNullOrEmpty(value))
	                {
	                    builder.Append(key).Append(value);
	                }
	            }
	            if (qhs)
	            {
	                builder.Append(appSecret);
	            }

	            //MD5对参数加密
	            byte[] buffer = MD5.Create().ComputeHash(Encoding.UTF8.GetBytes(builder.ToString()));

	            StringBuilder hexBuilder = new StringBuilder();
	            for (int i = 0; i < buffer.Length; i++)
	            {
	                // 把 byte[] 转化成 2位的 16 进制数
	                hexBuilder.Append(buffer[i].ToString("X2"));
	            }
	            return hexBuilder.ToString();
	        }

	        private static IDictionary<string, string> SplitUrlQuery(string query)
	        {
	            IDictionary<string, string> dictionary = new Dictionary<string, string>();
	            char[] separator = new char[] { '&' };
	            string[] strArray = query.Split(separator);
	            if ((strArray != null) && (strArray.Length > 0))
	            {
	                foreach (string str in strArray)
	                {
	                    char[] chArray2 = new char[] { '=' };
	                    string[] strArray3 = str.Split(chArray2, 2);
	                    if ((strArray3 != null) && (strArray3.Length == 2))
	                    {
	                        dictionary.Add(strArray3[0], strArray3[1]);
	                    }
	                }
	            }
	            return dictionary;
	        }

	        public static bool VerifyTopResponse(string callbackUrl, string appSecret)
	        {
	            string str2;
	            string str3;
	            string str4;
	            string str5;
	            Uri uri = new Uri(callbackUrl);
	            string query = uri.Query;
	            if (string.IsNullOrEmpty(query))
	            {
	                return false;
	            }
	            char[] trimChars = new char[] { '?', ' ' };
	            query = query.Trim(trimChars);
	            if (query.Length == 0)
	            {
	                return false;
	            }
	            IDictionary<string, string> dictionary = SplitUrlQuery(query);
	            dictionary.TryGetValue("aps_parameters", out str2);
	            dictionary.TryGetValue("aps_session", out str3);
	            dictionary.TryGetValue("aps_sign", out str4);
	            dictionary.TryGetValue("aps_appkey", out str5);
	            str4 = (str4 != null) ? Uri.UnescapeDataString(str4) : null;
	            return VerifyTopResponse(str2, str3, str4, str5, appSecret);
	        }

	        public static bool VerifyTopResponse(string topParams, string topSession, string topSign, string appKey, string appSecret)
	        {
	            StringBuilder builder = new StringBuilder();
	            builder.Append(appKey).Append(topParams).Append(topSession).Append(appSecret);
	            return (Convert.ToBase64String(MD5.Create().ComputeHash(Encoding.UTF8.GetBytes(builder.ToString()))) == topSign);
	        }*/
}

