package com.dmi.smartux.common.util;


import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.util.CollectionUtils;

import com.dmi.smartux.common.property.SmartUXProperties;

public class HttpCommUtil {
	
	@SuppressWarnings("deprecation")
	public static CloseableHttpClient getHttpClient() throws Exception {

		SSLContextBuilder builder = new SSLContextBuilder();
		builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build(),SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		
		Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("http", new PlainConnectionSocketFactory())
				.register("https", sslsf)
				.build();

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout(Integer.parseInt(StringUtils.isBlank(SmartUXProperties.getProperty("httpclient.conn.timeout")) ? "2000" : SmartUXProperties.getProperty("httpclient.conn.timeout")))
				.setSocketTimeout(Integer.parseInt(StringUtils.isBlank(SmartUXProperties.getProperty("httpclient.read.timeout")) ? "3000" : SmartUXProperties.getProperty("httpclient.read.timeout")))
				.build();

    	return HttpClients.custom()
    			.setSSLSocketFactory(sslsf)
    			.setConnectionManager(connectionManager)
    			.setDefaultRequestConfig(requestConfig)
    			.build();

    }
	
	@SuppressWarnings("deprecation")
	public static CloseableHttpClient getHttpClient(int timeout) throws Exception {

		SSLContextBuilder builder = new SSLContextBuilder();
		builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build(),SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		
		Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("http", new PlainConnectionSocketFactory())
				.register("https", sslsf)
				.build();

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout(timeout)
				.setSocketTimeout(timeout)
				.build();

    	return HttpClients.custom()
    			.setSSLSocketFactory(sslsf)
    			.setConnectionManager(connectionManager)
    			.setDefaultRequestConfig(requestConfig)
    			.build();

    }
	
	/**
	 * POST 호출
	 * @param url
	 * @param header
	 * @param body
	 * @param encoding
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public static String callPostHttpClient(String url, Map<String, String> header, String body, String encoding) throws Exception {

		String result = "";
		HttpClient httpclient = getHttpClient();
		try {
			HttpPost postRequest = new HttpPost(url);  

			if(!CollectionUtils.isEmpty(header)){
				for( String key : header.keySet() ){
					postRequest.setHeader(key, header.get(key));
				}
			}

		    postRequest.setEntity(new StringEntity(body, encoding));
			HttpResponse response = httpclient.execute(postRequest);
			//Response 출력
			if (response.getStatusLine().getStatusCode() == 200) {
				ResponseHandler<String> handler = new BasicResponseHandler();
				result= handler.handleResponse(response);
			} else {
				//System.out.println("response is error : " + response.getStatusLine().getStatusCode());
				throw new ClientProtocolException("callPostHttpClient Unexpected response status: " + response.getStatusLine().getStatusCode());
			}
		} catch (Exception e){
			throw e;
		} finally{
			if(httpclient != null) httpclient.getConnectionManager().shutdown();
		}
		return result;
	}
	
	/**
	 * Get 호출
	 * @param url
	 * @param header
	 * @param params
	 * @param encoding
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public static String callGetHttpClient(String url, Map<String, String> header, Map<String, String> params, String encoding) throws Exception {
		
		String result = "";
		HttpClient httpclient = getHttpClient();
		
		try {
			ArrayList<NameValuePair> getParameters = new ArrayList<NameValuePair>();
			 
		    for( String key : params.keySet() ){
		    	getParameters.add(new BasicNameValuePair(key, params.get(key)));
	        }
			
			HttpGet getRequest = new HttpGet(url+"?"+URLEncodedUtils.format(getParameters, encoding)); //GET 메소드 URL 생성

			for( String key : header.keySet() ){
				getRequest.setHeader(key, header.get(key));
			}

			HttpResponse response = httpclient.execute(getRequest);

			//Response 출력
			if (response.getStatusLine().getStatusCode() == 200) {
				ResponseHandler<String> handler = new BasicResponseHandler();
				result = handler.handleResponse(response);
			} else {
				throw new ClientProtocolException("callGetHttpClient Unexpected response status: " + response.getStatusLine().getStatusCode());
			}

		} catch (Exception e){
			throw e;
		} finally{
			if(httpclient != null) httpclient.getConnectionManager().shutdown();
		}

		return result;
	}
	
	/**
	 * Put 호출
	 * @param url
	 * @param header
	 * @param jsonStr
	 * @param encoding
	 * @return
	 * @throws Exception
	 */
	public static String callPutHttpClient(String url, Map<String, String> header, String jsonStr, String encoding) throws Exception {
		
		String result = "";
		HttpClient httpclient = getHttpClient();
		
		try {
			HttpPut putRequest = new HttpPut(url); //PUT 메소드 URL 생성
			
			for( String key : header.keySet() ){
				putRequest.setHeader(key, header.get(key));
			}
			
			StringEntity requestEntity = new StringEntity(jsonStr , "utf-8");
			requestEntity.setContentType(new BasicHeader("Content-Type", "application/json"));
			putRequest.setEntity(requestEntity); 
			HttpResponse response = httpclient.execute(putRequest);
			
			//Response 출력
			if (response.getStatusLine().getStatusCode() == 200) {
				ResponseHandler<String> handler = new BasicResponseHandler();
				result = handler.handleResponse(response);
			} else {
				throw new ClientProtocolException("callPutHttpClient Unexpected response status: " + response.getStatusLine().getStatusCode());
			}
			
		} catch (Exception e){
			throw e;
		} finally{
			if(httpclient != null) httpclient.getConnectionManager().shutdown();
		}
		
		return result;
	}
	
	/**
	 * @param host
	 * @param port
	 * @param url
	 * @param params
	 * @param timeout
	 * @param protocolName
	 * @param method
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public static String callPostHttpClient(String url, Map<String, String> header, Map<String, String> params, String encoding) throws Exception {
		
		String result = "";
		HttpClient httpclient = getHttpClient(60000);
		
		try {
			HttpPost postRequest = new HttpPost(url); 
			if(!CollectionUtils.isEmpty(header)){

				for( String key : header.keySet() ){
					postRequest.setHeader(key, header.get(key));
					//postRequest.addHeader(key, header.get(key));
				}
			}
			ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			
		    for( String key : params.keySet() ){
		    	postParameters.add(new BasicNameValuePair(key, params.get(key)));
	        }
		    postRequest.setEntity(new UrlEncodedFormEntity(postParameters, encoding));

			HttpResponse response = httpclient.execute(postRequest);

			//Response 출력
			if (response.getStatusLine().getStatusCode() == 200) {
				ResponseHandler<String> handler = new BasicResponseHandler();
				result= handler.handleResponse(response);
			} else {
				throw new ClientProtocolException("callGetHttpClient Unexpected response status: " + response.getStatusLine().getStatusCode());
			}
		} catch (Exception e){
			throw e;
		} finally{
			if(httpclient != null) httpclient.getConnectionManager().shutdown();
		}

		return result;
	}
}
