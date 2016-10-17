package com.thinkland.base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

/**
 * @author murphy.yang
 * @date 2016-01-22
 * @category BaseTest
 */
public abstract class BaseTest {

	public LogHandle log = new LogHandle();

	/**
	 * 向指定 URL 发送POST\GET方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param postData
	 *            post的数据。
	 * @param isPost
	 *            post or get 标志
	 * @return 所代表远程资源的响应结果
	 */
	public String sendHTTPRequest(String url, String postData, boolean isPost) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
			// 设置通用的请求属性
			if (isPost) {
				conn.setRequestMethod("POST");
			} else {
				conn.setRequestMethod("GET");
			}
			conn.setUseCaches(false);
			conn.setInstanceFollowRedirects(true);
			conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setConnectTimeout(2000);
			conn.connect();
			conn.setReadTimeout(5000);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(postData);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception ex) {
			System.out.println("发送 POST 请求出现异常！" + ex);
			ex.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * HttpClient Get请求
	 * 
	 * @param url
	 * @param postData
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String httpClientGet(String url, String postData) throws ClientProtocolException, IOException {
		String reponse = "";
		HttpClient httpclient = new DefaultHttpClient();
		/*
		 * HttpHost proxy = new HttpHost("127.0.0.1", 8888);
		 * httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
		 * proxy);
		 */
		HttpGet httpgets = new HttpGet(url + postData);
		HttpResponse response = httpclient.execute(httpgets);
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			InputStream instreams = entity.getContent();
			reponse = convertStreamToString(instreams);
			httpgets.abort();
		}
		return reponse;
	}

	private static String convertStreamToString(InputStream is) throws UnsupportedEncodingException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	/**
	 * Get 请求
	 * 
	 * @param url
	 * @param map
	 * @return
	 * @throws URISyntaxException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String httpGetClient(String url, Map<String, String> map)
			throws URISyntaxException, ClientProtocolException, IOException {
		String reponse = "";
		String key;
		String value;
		HttpClient httpclient = new DefaultHttpClient();
		URIBuilder builder = new URIBuilder();
		if (!url.contains("http")) {
			builder.setScheme("http");
		}
		builder.setHost(url);
		Iterator it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry field = (Map.Entry) it.next();
			key = field.getKey().toString();
			value = field.getValue().toString();
			if (!value.equals("null")) {
				builder.addParameter(key, value);
			}
		}
		URI uri = builder.build();
		System.out.println("uri=" + uri);
		HttpGet httpget = new HttpGet(uri);
		HttpResponse response = httpclient.execute(httpget);
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			InputStream instreams = entity.getContent();
			reponse = convertStreamToString(instreams);
			httpget.abort();
		}
		log.logInfo("uri= " + uri + "\n" + "reponse= " + reponse);
		return reponse;
	}

	/**
	 * Post 请求
	 * 
	 * @param url
	 * @param map
	 * @return
	 * @throws URISyntaxException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String httpPostClient(String url, Map<String, String> map)
			throws URISyntaxException, ClientProtocolException, IOException {
		String reponse = "";
		if (!url.contains("http")) {
			url = "http://" + url;
		}
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		Set<String> keySet = map.keySet();
		for (String key : keySet) {
			if (!map.get(key).equals("null")) {
				nvps.add(new BasicNameValuePair(key, map.get(key)));
			}
		}
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
		HttpResponse httpReponse = httpclient.execute(httpPost);
		HttpEntity entity = httpReponse.getEntity();
		if (entity != null) {
			InputStream instreams = entity.getContent();
			reponse = convertStreamToString(instreams);
			httpPost.abort();
		}
		log.logInfo("url: " + url + "\n" + "postData: " + map.toString() + "\n" + "reponse= " + reponse);
		return reponse;
	}
}
