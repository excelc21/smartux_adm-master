package com.dmi.smartux.test.rich;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.dmi.smartux.test.common.property.TestSmartUXProperties;
import com.dmi.smartux.test.common.util.TestGlobalCom;

public class moveDevicePageURL {
	
	static String host;
	static int port;
	static int timeout;
	
	@BeforeClass
	public static void beforeClass() throws Exception {
		// Spring Context 파일로 Spring 로딩
		ApplicationContext context = new ClassPathXmlApplicationContext("/com/dmi/smartux/test/resource/spring/test-context.xml");
		
		// 테스트 데이터를 만들기 위한 작업을 진행한다
		host = TestSmartUXProperties.getProperty("test.host");								
		port = Integer.parseInt(TestSmartUXProperties.getProperty("test.port"));
		timeout = Integer.parseInt(TestSmartUXProperties.getProperty("test.timeout"));
		
		// 테스트에서 사용할 로그 파일을 연다
		TestGlobalCom.testLogOpen();
	}
	
	@AfterClass
	public static void afterClass() throws Exception {
		// 테스트에서 사용한 로그 파일을 닫는다
		TestGlobalCom.testLogClose();
	}
	
	/**
	 * 모바일 해당 Page URL 이동 정상 테스트(XML)
	 * @throws Exception
	 */
	@Test
	public void moveDevicePageURLToXML() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.moveDevicePageURL.URL");
		String param = "sa_id=sa_id&stb_mac=stb_mac&stb_reg_id=stb_reg_id&ctn=ctn&ctn_reg_id=ctn_reg_id1&app_type=STB&device_page_url=http://naver.com";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");
		
		System.out.println("=====================moveDevicePageURLToXML=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("moveDevicePageURLToXML")), is(true));
	}
	
	/**
	 * 모바일 해당 Page URL 이동 정상 테스트(JSON)
	 * @throws Exception
	 */
	@Test
	public void moveDevicePageURLToJSON() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.moveDevicePageURL.URL");
		String param = "sa_id=sa_id&stb_mac=stb_mac&stb_reg_id=stb_reg_id&ctn=ctn&ctn_reg_id=ctn_reg_id1&app_type=STB&device_page_url=http://naver.com";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/json");
		
		System.out.println("=====================moveDevicePageURLToJSON=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("moveDevicePageURLToJSON")), is(true));
	}
	
	/**
	 * 모바일 해당 Page URL 이동 정상 테스트(TEXT)
	 * @throws Exception
	 */
	@Test
	public void moveDevicePageURLToText() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.moveDevicePageURL.URL");
		String param = "sa_id=sa_id&stb_mac=stb_mac&stb_reg_id=stb_reg_id&ctn=ctn&ctn_reg_id=ctn_reg_id1&app_type=STB&device_page_url=http://naver.com";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "");
		
		System.out.println("=====================moveDevicePageURLToText=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("moveDevicePageURLToText")), is(true));
	}
	
	
	/**
	 * 모바일 해당 Page URL 이동 SA_ID 정보 없는 경우(XML)
	 * @throws Exception
	 */
	@Test
	public void not_moveDevicePageURL_SA_ID_ToXML() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.moveDevicePageURL.URL");
		String param = "sa_id=&stb_mac=stb_mac&stb_reg_id=stb_reg_id&ctn=ctn&ctn_reg_id=ctn_reg_id1&app_type=STB&device_page_url=http://naver.com";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");
		
		System.out.println("=====================not_moveDevicePageURL_SA_ID_ToXML=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_moveDevicePageURL_SA_ID_ToXML")), is(true));
	}
	
	/**
	 * 모바일 해당 Page URL 이동 SA_ID 정보 없는 경우(JSON)
	 * @throws Exception
	 */
	@Test
	public void not_moveDevicePageURL_SA_ID_ToJSON() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.moveDevicePageURL.URL");
		String param = "sa_id=&stb_mac=stb_mac&stb_reg_id=stb_reg_id&ctn=ctn&ctn_reg_id=ctn_reg_id1&app_type=STB&device_page_url=http://naver.com";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/json");
		
		System.out.println("=====================not_moveDevicePageURL_SA_ID_ToJSON=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_moveDevicePageURL_SA_ID_ToJSON")), is(true));
	}
	
	/**
	 * 모바일 해당 Page URL 이동 SA_ID 정보 없는 경우(Text)
	 * @throws Exception
	 */
	@Test
	public void not_moveDevicePageURL_SA_ID_ToText() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.moveDevicePageURL.URL");
		String param = "sa_id=&stb_mac=stb_mac&stb_reg_id=stb_reg_id&ctn=ctn&ctn_reg_id=ctn_reg_id1&app_type=STB&device_page_url=http://naver.com";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "");
		
		System.out.println("=====================not_moveDevicePageURL_SA_ID_ToText=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_moveDevicePageURL_SA_ID_ToText")), is(true));
	}
	
	
	/**
	 * 모바일 해당 Page URL 이동 STB_MAC 정보 없는 경우(XML)
	 * @throws Exception
	 */
	@Test
	public void not_moveDevicePageURL_STB_MAC_ToXML() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.moveDevicePageURL.URL");
		String param = "sa_id=sa_id&stb_mac=&stb_reg_id=stb_reg_id&ctn=ctn&ctn_reg_id=ctn_reg_id1&app_type=STB&device_page_url=http://naver.com";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");
		
		System.out.println("=====================not_moveDevicePageURL_STB_MAC_ToXML=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_moveDevicePageURL_STB_MAC_ToXML")), is(true));
	}
	
	/**
	 * 모바일 해당 Page URL 이동 STB_MAC 정보 없는 경우(JSON)
	 * @throws Exception
	 */
	@Test
	public void not_moveDevicePageURL_STB_MAC_ToJSON() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.moveDevicePageURL.URL");
		String param = "sa_id=sa_id&stb_mac=&stb_reg_id=stb_reg_id&ctn=ctn&ctn_reg_id=ctn_reg_id1&app_type=STB&device_page_url=http://naver.com";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/json");
		
		System.out.println("=====================not_moveDevicePageURL_STB_MAC_ToJSON=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_moveDevicePageURL_STB_MAC_ToJSON")), is(true));
	}
	
	/**
	 * 모바일 해당 Page URL 이동 STB_MAC 정보 없는 경우(TEXT)
	 * @throws Exception
	 */
	@Test
	public void not_moveDevicePageURL_STB_MAC_ToText() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.moveDevicePageURL.URL");
		String param = "sa_id=sa_id&stb_mac=&stb_reg_id=stb_reg_id&ctn=ctn&ctn_reg_id=ctn_reg_id1&app_type=STB&device_page_url=http://naver.com";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "");
		
		System.out.println("=====================not_moveDevicePageURL_STB_MAC_ToText=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_moveDevicePageURL_STB_MAC_ToText")), is(true));
	}
	
	/**
	 * 모바일 해당 Page URL 이동 STB_REG_ID 정보 없는 경우(XML)
	 * @throws Exception
	 */
	@Test
	public void not_moveDevicePageURL_STB_REG_ID_ToXML() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.moveDevicePageURL.URL");
		String param = "sa_id=sa_id&stb_mac=stb_mac&stb_reg_id=&ctn=ctn&ctn_reg_id=ctn_reg_id1&app_type=STB&device_page_url=http://naver.com";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");
		
		System.out.println("=====================not_moveDevicePageURL_STB_REG_ID_ToXML=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_moveDevicePageURL_STB_REG_ID_ToXML")), is(true));
	}
	
	/**
	 * 모바일 해당 Page URL 이동 STB_REG_ID 정보 없는 경우(JSON)
	 * @throws Exception
	 */
	@Test
	public void not_moveDevicePageURL_STB_REG_ID_ToJSON() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.moveDevicePageURL.URL");
		String param = "sa_id=sa_id&stb_mac=stb_mac&stb_reg_id=&ctn=ctn&ctn_reg_id=ctn_reg_id1&app_type=STB&device_page_url=http://naver.com";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/json");
		
		System.out.println("=====================not_moveDevicePageURL_STB_REG_ID_ToJSON=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_moveDevicePageURL_STB_REG_ID_ToJSON")), is(true));
	}
	
	/**
	 * 모바일 해당 Page URL 이동 STB_REG_ID 정보 없는 경우(TEXT)
	 * @throws Exception
	 */
	@Test
	public void not_moveDevicePageURL_STB_REG_ID_ToText() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.moveDevicePageURL.URL");
		String param = "sa_id=sa_id&stb_mac=stb_mac&stb_reg_id=&ctn=ctn&ctn_reg_id=ctn_reg_id1&app_type=STB&device_page_url=http://naver.com";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "");
		
		System.out.println("=====================not_moveDevicePageURL_STB_REG_ID_ToText=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_moveDevicePageURL_STB_REG_ID_ToText")), is(true));
	}
	
	/**
	 * 모바일 해당 Page URL 이동 CTN 정보 없는 경우(XML)
	 * @throws Exception
	 */
	@Test
	public void not_moveDevicePageURL_CTN_ToXML() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.moveDevicePageURL.URL");
		String param = "sa_id=sa_id&stb_mac=stb_mac&stb_reg_id=stb_reg_id&ctn=&ctn_reg_id=ctn_reg_id1&app_type=STB&device_page_url=http://naver.com";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");
		
		System.out.println("=====================not_moveDevicePageURL_CTN_ToXML=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_moveDevicePageURL_CTN_ToXML")), is(true));
	}
	
	/**
	 * 모바일 해당 Page URL 이동 CTN 정보 없는 경우(JSON)
	 * @throws Exception
	 */
	@Test
	public void not_moveDevicePageURL_CTN_ToJSON() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.moveDevicePageURL.URL");
		String param = "sa_id=sa_id&stb_mac=stb_mac&stb_reg_id=stb_reg_id&ctn=&ctn_reg_id=ctn_reg_id1&app_type=STB&device_page_url=http://naver.com";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/json");
		
		System.out.println("=====================not_moveDevicePageURL_CTN_ToJSON=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_moveDevicePageURL_CTN_ToJSON")), is(true));
	}
	
	/**
	 * 모바일 해당 Page URL 이동 CTN 정보 없는 경우(TEXT)
	 * @throws Exception
	 */
	@Test
	public void not_moveDevicePageURL_CTN_ToText() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.moveDevicePageURL.URL");
		String param = "sa_id=sa_id&stb_mac=stb_mac&stb_reg_id=stb_reg_id&ctn=&ctn_reg_id=ctn_reg_id1&app_type=STB&device_page_url=http://naver.com";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "");
		
		System.out.println("=====================not_moveDevicePageURL_CTN_ToText=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_moveDevicePageURL_CTN_ToText")), is(true));
	}
	
	/**
	 * 모바일 해당 Page URL 이동 CTN_REG_ID 정보 없는 경우(XML)
	 * @throws Exception
	 */
	@Test
	public void not_moveDevicePageURL_CTN_REG_ID_ToXML() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.moveDevicePageURL.URL");
		String param = "sa_id=sa_id&stb_mac=stb_mac&stb_reg_id=stb_reg_id&ctn=ctn&ctn_reg_id=&app_type=STB&device_page_url=http://naver.com";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");
		
		System.out.println("=====================not_moveDevicePageURL_CTN_REG_ID_ToXML=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_moveDevicePageURL_CTN_REG_ID_ToXML")), is(true));
	}
	
	/**
	 * 모바일 해당 Page URL 이동 CTN_REG_ID 정보 없는 경우(JSON)
	 * @throws Exception
	 */
	@Test
	public void not_moveDevicePageURL_CTN_REG_ID_ToJSON() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.moveDevicePageURL.URL");
		String param = "sa_id=sa_id&stb_mac=stb_mac&stb_reg_id=stb_reg_id&ctn=ctn&ctn_reg_id=&app_type=STB&device_page_url=http://naver.com";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/json");
		
		System.out.println("=====================not_moveDevicePageURL_CTN_REG_ID_ToJSON=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_moveDevicePageURL_CTN_REG_ID_ToJSON")), is(true));
	}
	
	/**
	 * 모바일 해당 Page URL 이동 CTN_REG_ID 정보 없는 경우(TEXT)
	 * @throws Exception
	 */
	@Test
	public void not_moveDevicePageURL_CTN_REG_ID_ToText() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.moveDevicePageURL.URL");
		String param = "sa_id=sa_id&stb_mac=stb_mac&stb_reg_id=stb_reg_id&ctn=ctn&ctn_reg_id=&app_type=STB&device_page_url=http://naver.com";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "");
		
		System.out.println("=====================not_moveDevicePageURL_CTN_REG_ID_ToText=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_moveDevicePageURL_CTN_REG_ID_ToText")), is(true));
	}
	
	/**
	 * 모바일 해당 Page URL 이동 APP_TYPE 정보 없는 경우(XML)
	 * @throws Exception
	 */
	@Test
	public void not_moveDevicePageURL_APP_TYPE_ToXML() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.moveDevicePageURL.URL");
		String param = "sa_id=sa_id&stb_mac=stb_mac&stb_reg_id=stb_reg_id&ctn=ctn&ctn_reg_id=ctn_reg_id1&app_type=&device_page_url=http://naver.com";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");
		
		System.out.println("=====================not_moveDevicePageURL_APP_TYPE_ToXML=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_moveDevicePageURL_APP_TYPE_ToXML")), is(true));
	}
	
	/**
	 * 모바일 해당 Page URL 이동 APP_TYPE 정보 없는 경우(JSON)
	 * @throws Exception
	 */
	@Test
	public void not_moveDevicePageURL_APP_TYPE_ToJSON() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.moveDevicePageURL.URL");
		String param = "sa_id=sa_id&stb_mac=stb_mac&stb_reg_id=stb_reg_id&ctn=ctn&ctn_reg_id=ctn_reg_id1&app_type=&device_page_url=http://naver.com";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/json");
		
		System.out.println("=====================not_moveDevicePageURL_APP_TYPE_ToJSON=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_moveDevicePageURL_APP_TYPE_ToJSON")), is(true));
	}
	
	/**
	 * 모바일 해당 Page URL 이동 APP_TYPE 정보 없는 경우(TEXT)
	 * @throws Exception
	 */
	@Test
	public void not_moveDevicePageURL_APP_TYPE_ToText() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.moveDevicePageURL.URL");
		String param = "sa_id=sa_id&stb_mac=stb_mac&stb_reg_id=stb_reg_id&ctn=ctn&ctn_reg_id=ctn_reg_id1&app_type=&device_page_url=http://naver.com";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "");
		
		System.out.println("=====================not_moveDevicePageURL_APP_TYPE_ToText=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_moveDevicePageURL_APP_TYPE_ToText")), is(true));
	}
	
	/**
	 * 모바일 해당 Page URL 이동 DEVICE_PAGE_URL 정보 없는 경우(XML)
	 * @throws Exception
	 */
	@Test
	public void not_moveDevicePageURL_DEVICE_PAGE_URL_ToXML() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.moveDevicePageURL.URL");
		String param = "sa_id=sa_id&stb_mac=stb_mac&stb_reg_id=stb_reg_id&ctn=ctn&ctn_reg_id=ctn_reg_id1&app_type=STB&device_page_url=";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");
		
		System.out.println("=====================not_moveDevicePageURL_DEVICE_PAGE_URL_ToXML=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_moveDevicePageURL_DEVICE_PAGE_URL_ToXML")), is(true));
	}
	
	/**
	 * 모바일 해당 Page URL 이동 DEVICE_PAGE_URL 정보 없는 경우(JSON)
	 * @throws Exception
	 */
	@Test
	public void not_moveDevicePageURL_DEVICE_PAGE_URL_ToJSON() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.moveDevicePageURL.URL");
		String param = "sa_id=sa_id&stb_mac=stb_mac&stb_reg_id=stb_reg_id&ctn=ctn&ctn_reg_id=ctn_reg_id1&app_type=STB&device_page_url=";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/json");
		
		System.out.println("=====================not_moveDevicePageURL_DEVICE_PAGE_URL_ToJSON=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_moveDevicePageURL_DEVICE_PAGE_URL_ToJSON")), is(true));
	}
	
	/**
	 * 모바일 해당 Page URL 이동 DEVICE_PAGE_URL 정보 없는 경우(TEXT)
	 * @throws Exception
	 */
	@Test
	public void not_moveDevicePageURL_DEVICE_PAGE_URL_ToText() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.moveDevicePageURL.URL");
		String param = "sa_id=sa_id&stb_mac=stb_mac&stb_reg_id=stb_reg_id&ctn=ctn&ctn_reg_id=ctn_reg_id1&app_type=STB&device_page_url=";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "");
		
		System.out.println("=====================not_moveDevicePageURL_DEVICE_PAGE_URL_ToText=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_moveDevicePageURL_DEVICE_PAGE_URL_ToText")), is(true));
	}
}
