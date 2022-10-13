package com.dmi.smartux.test.mainpanel;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.dmi.smartux.test.common.property.TestSmartUXProperties;
import com.dmi.smartux.test.common.util.TestGlobalCom;

public class GetMainPanelVersionInfo {

	
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
		String url = TestSmartUXProperties.getProperty("test.queryURL");
		String param = "query_id=test_mainpanel.create_pannel_backup_table&type=create";
		TestGlobalCom.callHttpClient(host, port, url, param, timeout);
		
		param = "query_id=test_mainpanel.insert_pannel_to_backup_table&type=insert";
		TestGlobalCom.callHttpClient(host, port, url, param, timeout);
		
		param = "query_id=test_mainpanel.delete_org_pannel_data&type=delete";
		TestGlobalCom.callHttpClient(host, port, url, param, timeout);
		
		param = "query_id=test_mainpanel.insert_test_data_to_org_pannel_table1&type=insert";
		TestGlobalCom.callHttpClient(host, port, url, param, timeout);
		
		param = "query_id=test_mainpanel.insert_test_data_to_org_pannel_table2&type=insert";
		TestGlobalCom.callHttpClient(host, port, url, param, timeout);
		
		// 캐쉬 데이터를 테스트 데이터로 바꿔주기 위한 작업을 진행
		host = TestSmartUXProperties.getProperty("test.host");								
		port = Integer.parseInt(TestSmartUXProperties.getProperty("test.port"));
		url = TestSmartUXProperties.getProperty("test.getMainPanelVersionInfo.URL");
		param = "sa_id=1&stb_mac=2&app_type=I30&callByScheduler=A";
		TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");
		
		// 테스트에서 사용할 로그 파일을 연다
		TestGlobalCom.testLogOpen();
	}
	
	/**
	 * Main Panel 버전 정보 조회를 XML로 return 받을때의 테스트
	 * @throws Exception
	 */
	@Test
	public void getMainPanelVersionInfo_ToXML() throws Exception {
		// 단말 API 호출
		String url = TestSmartUXProperties.getProperty("test.getMainPanelVersionInfo.URL");
		String param = "sa_id=1&stb_mac=2&app_type=I30";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");
		
		
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getMainPanelVersionInfo_ToXML")), is(true));
	}
	
	
	
	/**
	 * Main Panel 버전 정보 조회를 JSON으로 return 받을때의 테스트
	 * @throws Exception
	 */
	@Test
	public void getMainPanelVersionInfo_ToJSON() throws Exception {
		// 단말 API 호출
		String url = TestSmartUXProperties.getProperty("test.getMainPanelVersionInfo.URL");
		String param = "sa_id=1&stb_mac=2&app_type=I30";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/json");

		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getMainPanelVersionInfo_ToJSON")), is(true));
	}
	
	/**
	 * Main Panel 버전 정보 조회를 Text로 return 받을때의 테스트
	 * @throws Exception
	 */
	@Test
	public void getMainPanelVersionInfo_ToText() throws Exception {
		// 단말 API 호출
		String url = TestSmartUXProperties.getProperty("test.getMainPanelVersionInfo.URL");
		String param = "sa_id=1&stb_mac=2&app_type=I30";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "");
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getMainPanelVersionInfo_ToText")), is(true));
	}
	
	/**
	 * Main Panel 버전 정보 조회시 가입번호 파라미터(sa_id)를 빼고 호출했을때의 결과를 XML로 받을때의 테스트!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	 * @throws Exception
	 */
	@Test
	public void getMainPanelVersionInfo_notSA_ID_ToXML() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.getMainPanelVersionInfo.URL");
		String param = "stb_mac=2&app_type=I30";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");
		
		
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getMainPanelVersionInfo_notSA_ID_ToXML")), is(true));
	}
	
	/**
	 * Main Panel 버전 정보 조회시 가입번호 파라미터(sa_id)를 빼고 호출했을때의 결과를 JSON로 받을때의 테스트
	 * @throws Exception
	 */
	@Test
	public void getMainPanelVersionInfo_notSA_ID_ToJSON() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.getMainPanelVersionInfo.URL");
		String param = "stb_mac=2&app_type=I30";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/json");
		
		
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getMainPanelVersionInfo_notSA_ID_ToJSON")), is(true));
	}
	
	/**
	 * Main Panel 버전 정보 조회시 가입번호 파라미터(sa_id)를 빼고 호출했을때의 결과를 Text로 받을때의 테스트
	 * @throws Exception
	 */
	@Test
	public void getMainPanelVersionInfo_notSA_ID_ToText() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.getMainPanelVersionInfo.URL");
		String param = "stb_mac=2&app_type=I30";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "");
		
		
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getMainPanelVersionInfo_notSA_ID_ToText")), is(true));
	}
	
	/**
	 * Main Panel 버전 정보 조회시 맥주소 파라미터(stb_mac)를 빼고 호출했을때의 결과를 XML로 받을때의 테스트
	 * @throws Exception
	 */
	@Test
	public void getMainPanelVersionInfo_notSTB_MAC_ToXML() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.getMainPanelVersionInfo.URL");
		String param = "sa_id=1&app_type=I30";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");
		
		
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getMainPanelVersionInfo_notSTB_MAC_ToXML")), is(true));
	}
	
	/**
	 * Main Panel 버전 정보 조회시 맥주소 파라미터(stb_mac)를 빼고 호출했을때의 결과를 JSON로 받을때의 테스트
	 * @throws Exception
	 */
	@Test
	public void getMainPanelVersionInfo_notSTB_MAC_ToJSON() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.getMainPanelVersionInfo.URL");
		String param = "sa_id=1&app_type=I30";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/json");
		
		
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getMainPanelVersionInfo_notSTB_MAC_ToJSON")), is(true));
	}
	
	/**
	 * Main Panel 버전 정보 조회시 맥주소 파라미터(stb_mac)를 빼고 호출했을때의 결과를 Text로 받을때의 테스트
	 * @throws Exception
	 */
	@Test
	public void getMainPanelVersionInfo_notSTB_MAC_ToText() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.getMainPanelVersionInfo.URL");
		String param = "sa_id=1&&app_type=I30";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "");
		
		
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getMainPanelVersionInfo_notSTB_MAC_ToText")), is(true));
	}
	
	/**
	 * Main Panel 버전 정보 조회시 어플타입 파라미터(app_type)를 빼고 호출했을때의 결과를 XML로 받을때의 테스트
	 * @throws Exception
	 */
	@Test
	public void getMainPanelVersionInfo_notAPP_TYPE_ToXML() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.getMainPanelVersionInfo.URL");
		String param = "sa_id=1&stb_mac=20";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");
		
		
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getMainPanelVersionInfo_notAPP_TYPE_ToXML")), is(true));
	}
	
	/**
	 * Main Panel 버전 정보 조회시 어플타입 파라미터(app_type)를 빼고 호출했을때의 결과를 JSON로 받을때의 테스트
	 * @throws Exception
	 */
	@Test
	public void getMainPanelVersionInfo_notAPP_TYPE_ToJSON() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.getMainPanelVersionInfo.URL");
		String param = "sa_id=1&stb_mac=2";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/json");
		
		
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getMainPanelVersionInfo_notAPP_TYPE_ToJSON")), is(true));
	}
	
	/**
	 * Main Panel 버전 정보 조회시 어플타입 파라미터(app_type)를 빼고 호출했을때의 결과를 Text로 받을때의 테스트
	 * @throws Exception
	 */
	@Test
	public void getMainPanelVersionInfo_notAPP_TYPE_ToText() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.getMainPanelVersionInfo.URL");
		String param = "sa_id=1&stb_mac=2";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "");
		
		
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getMainPanelVersionInfo_notAPP_TYPE_ToText")), is(true));
	}
	
	/**
	 * Main Panel 버전 정보 조회시 어플타입 파라미터(app_type)에 I30이 아닌 다른 값으로 호출했을때의 결과를 XML로 받을때의 테스트
	 * @throws Exception
	 */
	@Test
	public void getMainPanelVersionInfo_not_I30_APP_TYPE_ToXML() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.getMainPanelVersionInfo.URL");
		String param = "sa_id=1&stb_mac=20&app_type=AAA";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");
		
		
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getMainPanelVersionInfo_not_I30_APP_TYPE_ToXML")), is(true));
	}
	
	/**
	 * Main Panel 버전 정보 조회시 어플타입 파라미터(app_type)에 I30이 아닌 다른 값으로 호출했을때의 결과를 JSON로 받을때의 테스트
	 * @throws Exception
	 */
	@Test
	public void getMainPanelVersionInfo_not_I30_APP_TYPE_ToJSON() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.getMainPanelVersionInfo.URL");
		String param = "sa_id=1&stb_mac=20&app_type=AAA";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/json");
		
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getMainPanelVersionInfo_not_I30_APP_TYPE_ToJSON")), is(true));
	}
	
	/**
	 * Main Panel 버전 정보 조회시 어플타입 파라미터(app_type)에 I30이 아닌 다른 값으로 호출했을때의 결과를 Text로 받을때의 테스트
	 * @throws Exception
	 */
	@Test
	public void getMainPanelVersionInfo_not_I30_APP_TYPE_ToText() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.getMainPanelVersionInfo.URL");
		String param = "sa_id=1&stb_mac=20&app_type=AAA";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "");
		
		
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getMainPanelVersionInfo_not_I30_APP_TYPE_ToText")), is(true));
	}
	
	
	
	
	
	@AfterClass
	public static void afterClass() throws Exception {
		
		// 테스트에서 사용한 로그 파일을 닫는다
		TestGlobalCom.testLogClose();
		
		// 테스트 전의 데이터로 원복 작업
		String url = TestSmartUXProperties.getProperty("test.queryURL");
		String param = "query_id=test_mainpanel.delete_test_pannel_data&type=delete";
		TestGlobalCom.callHttpClient(host, port, url, param, timeout);
		
		param = "query_id=test_mainpanel.insert_pannel_to_org_table&type=insert";
		TestGlobalCom.callHttpClient(host, port, url, param, timeout);
		
		param = "query_id=test_mainpanel.drop_pannel_backup_table&type=drop";
		TestGlobalCom.callHttpClient(host, port, url, param, timeout);
		
		// 원복된 데이터로 캐쉬 작업
		url = TestSmartUXProperties.getProperty("test.getMainPanelVersionInfo.URL");
		param = "sa_id=1&stb_mac=2&app_type=I30&callByScheduler=A";
		TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");
	}
}
