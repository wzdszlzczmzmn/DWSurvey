package net.diaowen.common.dao;

import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;


/**
 *
 * @ClassName: SuperHttpDao
 * @Description: TODO(http请求超类，http请求更高级原始的封装)
 * @author keyuan
 * @date 2016年9月17日 上午11:22:21
 *
 * SuperHttpDao类是一个DAO类，提供HTTP GET请求。
 * 它用于执行HTTP GET请求并将响应作为字符串返回。
 * 此类是线程安全的，可在多线程环境中使用。
 */
@Component
public class SuperHttpDao {

	private static Logger logger = LogManager.getLogger(SuperHttpDao.class.getName());
	//utf8格式
	private static final String STANDRDCHARSETS = "UTF-8";
	@Autowired
	protected CloseableHttpClient httpClient;
	@Autowired
	protected RequestConfig requestConfig;

	/**
	 * 执行HTTP GET请求并将响应作为字符串返回。
	 *
	 * @param httpGet 要执行的HTTP GET请求
	 * @return 响应作为字符串
	 */
	public String doGet(HttpGet httpGet) {
		CloseableHttpResponse response = null;
		try {
			// 执行请求
			response = httpClient.execute(httpGet);
			// 判断返回状态是否为200
			StatusLine statusLine = response.getStatusLine();
			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
				return EntityUtils.toString(response.getEntity(), STANDRDCHARSETS);
			}
		} catch (ClientProtocolException e) {
			logger.error("doGet(HttpGet httpGet)  ClientProtocolException : {} ", httpGet.getURI().toString());
		} catch (IOException e) {
			logger.error("doGet(HttpGet httpGet)  IOException : {} ", httpGet.getURI().toString());
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	/**
	 * 执行HTTP POST请求并将响应作为字符串返回。
	 *
	 * @param httpPost 要执行的HTTP POST请求
	 * @return 响应作为字符串
	 */
	public String doPost(HttpPost httpPost) {
		CloseableHttpResponse response = null;
		try {
			// 执行请求
			response = httpClient.execute(httpPost);
			// 判断返回状态是否为200
			if (response.getStatusLine().getStatusCode() == 200) {
				return EntityUtils.toString(response.getEntity(), STANDRDCHARSETS);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			logger.error("doPost(HttpPost httpPost) ClientProtocolException : {} ", httpPost.getURI().toString());
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("doPost(HttpPost httpPost) IOException : {} ", httpPost.getURI().toString());
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	/**
	 * 执行HTTP DELETE请求并将响应作为字符串返回。
	 *
	 * @param httpDelete 要执行的HTTP DELETE请求
	 * @return 响应作为字符串
	 */
	public String doDelete(HttpDelete httpDelete) {
		CloseableHttpResponse response = null;
		try {
			// 执行请求
			response = httpClient.execute(httpDelete);
			// 判断返回状态是否为200
			if (response.getStatusLine().getStatusCode() == 200) {
				return EntityUtils.toString(response.getEntity(), STANDRDCHARSETS);
			}
		} catch (ClientProtocolException e) {
			logger.error("doDelete(HttpDelete httpDelete) ClientProtocolException : {} ", httpDelete.getURI().toString());
		} catch (IOException e) {
			logger.error("doDelete(HttpDelete httpDelete) IOException : {} ", httpDelete.getURI().toString());
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	/**
	 * 执行HTTP PATCH请求并将响应作为字符串返回。
	 *
	 * @param httpPatch 要执行的HTTP PATCH请求
	 * @return 响应作为字符串
	 */
	public String doPatch(HttpPatch httpPatch) {
		CloseableHttpResponse response = null;
		try {
			Header[] headers2 = httpPatch.getAllHeaders();
			// 执行请求
			response = httpClient.execute(httpPatch);
			// 判断返回状态是否为200
			if (response.getStatusLine().getStatusCode() == 200) {
				return EntityUtils.toString(response.getEntity(), STANDRDCHARSETS);
			}
		} catch (ClientProtocolException e) {
			logger.error("doPatch(HttpPatch httpPatch) ClientProtocolException : {} ", httpPatch.getURI().toString());
		} catch (IOException e) {
			logger.error("doPatch(HttpPatch httpPatch) IOException : {} ", httpPatch.getURI().toString());
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * 执行HTTP PUT请求。
	 *
	 * @param httpPut HTTP PUT请求
	 * @return 响应字符串
	 */
	public String doPut(HttpPut httpPut) {
		CloseableHttpResponse response = null;
		try {
			// 执行请求
			response = httpClient.execute(httpPut);
			// 判断返回状态是否为200
			if (response.getStatusLine().getStatusCode() == 200) {
				return EntityUtils.toString(response.getEntity(), STANDRDCHARSETS);
			}
		} catch (ClientProtocolException e) {
			logger.error("doPatch(HttpPatch httpPatch) ClientProtocolException : {} ", httpPut.getURI().toString());
		} catch (IOException e) {
			logger.error("doPatch(HttpPatch httpPatch) IOException : {} ", httpPut.getURI().toString());
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * 执行HTTP GET请求。
	 *
	 * @param url 请求URL
	 * @return 响应字符串
	 */
	public String doGet(String url) {
		// 创建http GET请求
		HttpGet httpGet = new HttpGet(url);
		httpGet.setConfig(this.requestConfig);
		return doGet(httpGet);
	}

	/**
	 * 执行HTTP GET请求。
	 *
	 * @param url    请求URL
	 * @param params 请求参数
	 * @return 响应字符串
	 */
	public String doGet(String url, Map<String, String> params) {
		try {
			URIBuilder uriBuilder = new URIBuilder(url);
			for (Map.Entry<String,String> entry : params.entrySet()) {
				String key = entry.getKey();
				uriBuilder.addParameter(key, params.get(key));
			}
			return this.doGet(uriBuilder.build().toString());
		} catch (URISyntaxException e) {
			logger.error("doGet(String url, Map<String, String> params) URISyntaxException : {} ", url);
		}
		return null;
	}

	/**
	 *
	 * @param url 请求URL
	 * @param params 请求参数
	 * @return 生成的URL字符串
	 */
	public String buildUrl(String url, Map<String, String> params) {
		try {
			URIBuilder uriBuilder = new URIBuilder(url);
			for (Map.Entry<String,String> entry : params.entrySet()) {
				String key = entry.getKey();
				uriBuilder.addParameter(key, params.get(key));
			}
			return uriBuilder.build().toString();
		} catch (URISyntaxException e) {
			logger.error("doGet(String url, Map<String, String> params) URISyntaxException : {} ", url);
		}
		return null;
	}

}
