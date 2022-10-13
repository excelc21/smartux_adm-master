package com.dmi.smartux.test.bonbang;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.dmi.smartux.test.common.property.TestSmartUXProperties;
import com.dmi.smartux.test.common.util.TestGlobalCom;

public class getReservedProgramList {
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
		String param = "query_id=test_bonbang.create_reserved_backup_table&type=create";
		TestGlobalCom.callHttpClient(host, port, url, param, timeout);
		
		param = "query_id=test_bonbang.insert_reserved_to_backup_table&type=insert";
		TestGlobalCom.callHttpClient(host, port, url, param, timeout);
		
		param = "query_id=test_bonbang.delete_reserved_org_data&type=delete";
		TestGlobalCom.callHttpClient(host, port, url, param, timeout);
		
		//임의 데이터 삽입
		param = "query_id=test_bonbang.insert_reserved_to_backup_table_1&type=insert";
		TestGlobalCom.callHttpClient(host, port, url, param, timeout);
		
		param = "query_id=test_bonbang.insert_reserved_to_backup_table_2&type=insert";
		TestGlobalCom.callHttpClient(host, port, url, param, timeout);
		
		param = "query_id=test_bonbang.insert_reserved_to_backup_table_3&type=insert";
		TestGlobalCom.callHttpClient(host, port, url, param, timeout);
		
		param = "query_id=test_bonbang.insert_reserved_to_backup_table_4&type=insert";
		TestGlobalCom.callHttpClient(host, port, url, param, timeout);
		
		
		// 테스트에서 사용할 로그 파일을 연다
		TestGlobalCom.testLogOpen();
	}
	
	@AfterClass
	public static void afterClass() throws Exception {
		// 테스트에서 사용한 로그 파일을 닫는다
		TestGlobalCom.testLogClose();
		
//		String url = TestSmartUXProperties.getProperty("test.queryURL");
//		String param = "query_id=test_bonbang.delete_reserved_test_data&type=delete";
//		TestGlobalCom.callHttpClient(host, port, url, param, timeout);
//		
//		param = "query_id=test_bonbang.insert_reserved_to_org_table&type=insert";
//		TestGlobalCom.callHttpClient(host, port, url, param, timeout);
//		
//		param = "query_id=test_bonbang.drop_reserved_backup_table&type=drop";
//		TestGlobalCom.callHttpClient(host, port, url, param, timeout);
		
	}
	
	/**
	 * 시청예약 정보 목록 전체 갯수 조회 정상 테스트 (XML)
	 * @throws Exception
	 */
	@Test
	public void getReservedProgramList_Count_ToXML() throws Exception {
		// 단말 API 호출
		String url = TestSmartUXProperties.getProperty("test.getReservedProgramList.URL");
		String param = "sa_id=200000000000&stb_mac=001c.6270.4959&stb_reg_id=STB_REGI_ID_1&ctn=01011111111&ctn_reg_id=CTN_REGI_ID_1&app_type=SMA&start_num=0&req_count=";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");
		
		System.out.println("=====================getReservedProgramList_Count_ToXML=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getReservedProgramList_Count_ToXML")), is(true));
	}
	
	/**
	 * 시청예약 정보 목록 전체 갯수 조회 정상 테스트 (JSON)
	 * @throws Exception
	 */
	@Test
	public void getReservedProgramList_Count_ToJSON() throws Exception {
		// 단말 API 호출
		String url = TestSmartUXProperties.getProperty("test.getReservedProgramList.URL");
		String param = "sa_id=200000000000&stb_mac=001c.6270.4959&stb_reg_id=STB_REGI_ID_1&ctn=01011111111&ctn_reg_id=CTN_REGI_ID_1&app_type=SMA&start_num=0&req_count=";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/json");
		
		System.out.println("=====================getReservedProgramList_Count_ToJSON=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getReservedProgramList_Count_ToJSON")), is(true));
	}
	
	/**
	 * 시청예약 정보 목록 전체 갯수 조회 정상 테스트 (Text)
	 * @throws Exception
	 */
	@Test
	public void getReservedProgramList_Count_ToText() throws Exception {
		// 단말 API 호출
		String url = TestSmartUXProperties.getProperty("test.getReservedProgramList.URL");
		String param = "sa_id=200000000000&stb_mac=001c.6270.4959&stb_reg_id=STB_REGI_ID_1&ctn=01011111111&ctn_reg_id=CTN_REGI_ID_1&app_type=SMA&start_num=0&req_count=";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "");
		
		System.out.println("=====================getReservedProgramList_Count_ToText=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getReservedProgramList_Count_ToText")), is(true));
	}
	
	
	/**
	 * 시청예약 정보 목록 Paging 조회 정상 테스트 (XML)
	 * @throws Exception
	 */
	@Test
	public void getReservedProgramList_Paging_ToXML() throws Exception {
		// 단말 API 호출
		String url = TestSmartUXProperties.getProperty("test.getReservedProgramList.URL");
		String param = "sa_id=200000000000&stb_mac=001c.6270.4959&stb_reg_id=STB_REGI_ID_1&ctn=01011111111&ctn_reg_id=CTN_REGI_ID_1&app_type=SMA&start_num=2&req_count=3";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");
		
		System.out.println("=====================getReservedProgramList_Paging_ToXML=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getReservedProgramList_Paging_ToXML")), is(true));
	}
	
	/**
	 * 시청예약 정보 목록 Paging 조회 정상 테스트 (JSON)
	 * @throws Exception
	 */
	@Test
	public void getReservedProgramList_Paging_ToJSON() throws Exception {
		// 단말 API 호출
		String url = TestSmartUXProperties.getProperty("test.getReservedProgramList.URL");
		String param = "sa_id=200000000000&stb_mac=001c.6270.4959&stb_reg_id=STB_REGI_ID_1&ctn=01011111111&ctn_reg_id=CTN_REGI_ID_1&app_type=SMA&start_num=2&req_count=3";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/json");
		
		System.out.println("=====================getReservedProgramList_Paging_ToJSON=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getReservedProgramList_Paging_ToJSON")), is(true));
	}
	
	/**
	 * 시청예약 정보 목록 Paging 조회 정상 테스트 (Text)
	 * @throws Exception
	 */
	@Test
	public void getReservedProgramList_Paging_ToText() throws Exception {
		// 단말 API 호출
		String url = TestSmartUXProperties.getProperty("test.getReservedProgramList.URL");
		String param = "sa_id=200000000000&stb_mac=001c.6270.4959&stb_reg_id=STB_REGI_ID_1&ctn=01011111111&ctn_reg_id=CTN_REGI_ID_1&app_type=SMA&start_num=2&req_count=3";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "");
		
		System.out.println("=====================getReservedProgramList_Paging_ToText=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getReservedProgramList_Paging_ToText")), is(true));
	}
	
	/**
	 * 시청예약 정보 목록 전체 내용 조회 정상 테스트 (XML)
	 * @throws Exception
	 */
	@Test
	public void getReservedProgramList_All_ToXML() throws Exception {
		// 단말 API 호출
		String url = TestSmartUXProperties.getProperty("test.getReservedProgramList.URL");
		String param = "sa_id=200000000000&stb_mac=001c.6270.4959&stb_reg_id=STB_REGI_ID_1&ctn=01011111111&ctn_reg_id=CTN_REGI_ID_1&app_type=SMA&start_num=-1&req_count=";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");
		
		System.out.println("=====================getReservedProgramList_All_ToXML=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getReservedProgramList_All_ToXML")), is(true));
	}
	
	/**
	 * 시청예약 정보 목록 전체 내용 조회 정상 테스트 (JSON)
	 * @throws Exception
	 */
	@Test
	public void getReservedProgramList_All_ToJSON() throws Exception {
		// 단말 API 호출
		String url = TestSmartUXProperties.getProperty("test.getReservedProgramList.URL");
		String param = "sa_id=200000000000&stb_mac=001c.6270.4959&stb_reg_id=STB_REGI_ID_1&ctn=01011111111&ctn_reg_id=CTN_REGI_ID_1&app_type=SMA&start_num=-1&req_count=";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/json");
		
		System.out.println("=====================getReservedProgramList_All_ToJSON=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getReservedProgramList_All_ToJSON")), is(true));
	}
	
	/**
	 * 시청예약 정보 목록 전체 내용 조회 정상 테스트 (Text)
	 * @throws Exception
	 */
	@Test
	public void getReservedProgramList_All_ToText() throws Exception {
		// 단말 API 호출
		String url = TestSmartUXProperties.getProperty("test.getReservedProgramList.URL");
		String param = "sa_id=200000000000&stb_mac=001c.6270.4959&stb_reg_id=STB_REGI_ID_1&ctn=01011111111&ctn_reg_id=CTN_REGI_ID_1&app_type=SMA&start_num=-1&req_count=";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "");
		
		System.out.println("=====================getReservedProgramList_All_ToText=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getReservedProgramList_All_ToText")), is(true));
	}
	
	/**
	 * 시청예약 정보 목록 SA_ID 정보 없는 경우(XML)
	 * @throws Exception
	 */
	@Test
	public void not_getReservedProgramList_SA_ID_ToXML() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.getReservedProgramList.URL");
		String param = "sa_id=&stb_mac=001c.6270.4959&stb_reg_id=STB_REGI_ID_1&ctn=01011111111&ctn_reg_id=CTN_REGI_ID_1&app_type=SMA&start_num=2&req_count=3";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");

		System.out.println("=====================not_getReservedProgramList_SA_ID_ToXML=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_getReservedProgramList_SA_ID_ToXML")), is(true));
	}
	
	/**
	 * 시청예약 정보 목록 SA_ID 정보 없는 경우(JSON)
	 * @throws Exception
	 */
	@Test
	public void not_getReservedProgramList_SA_ID_ToJSON() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.getReservedProgramList.URL");
		String param = "sa_id=&stb_mac=001c.6270.4959&stb_reg_id=STB_REGI_ID_1&ctn=01011111111&ctn_reg_id=CTN_REGI_ID_1&app_type=SMA&start_num=2&req_count=3";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/json");
		
		System.out.println("=====================not_getReservedProgramList_SA_ID_ToJSON=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_getReservedProgramList_SA_ID_ToJSON")), is(true));
	}
	
	/**
	 * 시청예약 정보 목록 SA_ID 정보 없는 경우(Text)
	 * @throws Exception
	 */
	@Test
	public void not_getReservedProgramList_SA_ID_ToText() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.getReservedProgramList.URL");
		String param = "sa_id=&stb_mac=001c.6270.4959&stb_reg_id=STB_REGI_ID_1&ctn=01011111111&ctn_reg_id=CTN_REGI_ID_1&app_type=SMA&start_num=2&req_count=3";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "");
		
		System.out.println("=====================not_getReservedProgramList_SA_ID_ToText=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_getReservedProgramList_SA_ID_ToText")), is(true));
	}
	
	
	/**
	 * 시청예약 정보 목록 STB_MAC 정보 없는 경우(XML)
	 * @throws Exception
	 */
	@Test
	public void not_getReservedProgramList_STB_MAC_ToXML() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.getReservedProgramList.URL");
		String param = "sa_id=200000000000&stb_mac=&stb_reg_id=STB_REGI_ID_1&ctn=01011111111&ctn_reg_id=CTN_REGI_ID_1&app_type=SMA&start_num=2&req_count=3";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");
		
		System.out.println("=====================not_getReservedProgramList_STB_MAC_ToXML=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_getReservedProgramList_STB_MAC_ToXML")), is(true));
	}
	
	/**
	 * 시청예약 정보 목록 STB_MAC 정보 없는 경우(JSON)
	 * @throws Exception
	 */
	@Test
	public void not_getReservedProgramList_STB_MAC_ToJSON() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.getReservedProgramList.URL");
		String param = "sa_id=200000000000&stb_mac=&stb_reg_id=STB_REGI_ID_1&ctn=01011111111&ctn_reg_id=CTN_REGI_ID_1&app_type=SMA&start_num=2&req_count=3";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/json");
		
		System.out.println("=====================not_getReservedProgramList_STB_MAC_ToJSON=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_getReservedProgramList_STB_MAC_ToJSON")), is(true));
	}
	
	/**
	 * 시청예약 정보 목록 STB_MAC 정보 없는 경우(TEXT)
	 * @throws Exception
	 */
	@Test
	public void not_getReservedProgramList_STB_MAC_ToText() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.getReservedProgramList.URL");
		String param = "sa_id=200000000000&stb_mac=&stb_reg_id=STB_REGI_ID_1&ctn=01011111111&ctn_reg_id=CTN_REGI_ID_1&app_type=SMA&start_num=2&req_count=3";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "");
		
		System.out.println("=====================not_getReservedProgramList_STB_MAC_ToText=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_getReservedProgramList_STB_MAC_ToText")), is(true));
	}
	
	/**
	 * 시청예약 정보 목록 STB_REG_ID 정보 없는 경우(XML)
	 * @throws Exception
	 */
	@Test
	public void not_getReservedProgramList_STB_REG_ID_ToXML() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.getReservedProgramList.URL");
		String param = "sa_id=200000000000&stb_mac=001c.6270.4959&stb_reg_id=&ctn=01011111111&ctn_reg_id=CTN_REGI_ID_1&app_type=SMA&start_num=2&req_count=3";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");
		
		System.out.println("=====================not_getReservedProgramList_STB_REG_ID_ToXML=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_getReservedProgramList_STB_REG_ID_ToXML")), is(true));
	}
	
	/**
	 * 시청예약 정보 목록 STB_REG_ID 정보 없는 경우(JSON)
	 * @throws Exception
	 */
	@Test
	public void not_getReservedProgramList_STB_REG_ID_ToJSON() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.getReservedProgramList.URL");
		String param = "sa_id=200000000000&stb_mac=001c.6270.4959&stb_reg_id=&ctn=01011111111&ctn_reg_id=CTN_REGI_ID_1&app_type=SMA&start_num=2&req_count=3";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/json");
		
		System.out.println("=====================not_getReservedProgramList_STB_REG_ID_ToJSON=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_getReservedProgramList_STB_REG_ID_ToJSON")), is(true));
	}
	
	/**
	 * 시청예약 정보 목록 STB_REG_ID 정보 없는 경우(TEXT)
	 * @throws Exception
	 */
	@Test
	public void not_getReservedProgramList_STB_REG_ID_ToText() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.getReservedProgramList.URL");
		String param = "sa_id=200000000000&stb_mac=001c.6270.4959&stb_reg_id=&ctn=01011111111&ctn_reg_id=CTN_REGI_ID_1&app_type=SMA&start_num=2&req_count=3";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "");
		
		System.out.println("=====================not_getReservedProgramList_STB_REG_ID_ToText=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_getReservedProgramList_STB_REG_ID_ToText")), is(true));
	}
	
	/**
	 * 시청예약 정보 목록 CTN 정보 없는 경우(XML)
	 * @throws Exception
	 */
	@Test
	public void not_getReservedProgramList_CTN_ToXML() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.getReservedProgramList.URL");
		String param = "sa_id=200000000000&stb_mac=001c.6270.4959&stb_reg_id=STB_REGI_ID_1&ctn=&ctn_reg_id=CTN_REGI_ID_1&app_type=SMA&start_num=2&req_count=3";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");
		
		System.out.println("=====================not_getReservedProgramList_CTN_ToXML=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_getReservedProgramList_CTN_ToXML")), is(true));
	}
	
	/**
	 * 시청예약 정보 목록 CTN 정보 없는 경우(JSON)
	 * @throws Exception
	 */
	@Test
	public void not_getReservedProgramList_CTN_ToJSON() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.getReservedProgramList.URL");
		String param = "sa_id=200000000000&stb_mac=001c.6270.4959&stb_reg_id=STB_REGI_ID_1&ctn=&ctn_reg_id=CTN_REGI_ID_1&app_type=SMA&start_num=2&req_count=3";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/json");
		
		System.out.println("=====================not_getReservedProgramList_CTN_ToJSON=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_getReservedProgramList_CTN_ToJSON")), is(true));
	}
	
	/**
	 * 시청예약 정보 목록 CTN 정보 없는 경우(TEXT)
	 * @throws Exception
	 */
	@Test
	public void not_getReservedProgramList_CTN_ToText() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.getReservedProgramList.URL");
		String param = "sa_id=200000000000&stb_mac=001c.6270.4959&stb_reg_id=STB_REGI_ID_1&ctn=&ctn_reg_id=CTN_REGI_ID_1&app_type=SMA&start_num=2&req_count=3";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "");
		
		System.out.println("=====================not_getReservedProgramList_CTN_ToText=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_getReservedProgramList_CTN_ToText")), is(true));
	}
	
	/**
	 * 시청예약 정보 목록 CTN_REG_ID 정보 없는 경우(XML)
	 * @throws Exception
	 */
	@Test
	public void not_getReservedProgramList_CTN_REG_ID_ToXML() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.getReservedProgramList.URL");
		String param = "sa_id=200000000000&stb_mac=001c.6270.4959&stb_reg_id=STB_REGI_ID_1&ctn=01011111111&ctn_reg_id=&app_type=SMA&start_num=2&req_count=3";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");
		
		System.out.println("=====================not_getReservedProgramList_CTN_REG_ID_ToXML=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_getReservedProgramList_CTN_REG_ID_ToXML")), is(true));
	}
	
	/**
	 * 시청예약 정보 목록 CTN_REG_ID 정보 없는 경우(JSON)
	 * @throws Exception
	 */
	@Test
	public void not_getReservedProgramList_CTN_REG_ID_ToJSON() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.getReservedProgramList.URL");
		String param = "sa_id=200000000000&stb_mac=001c.6270.4959&stb_reg_id=STB_REGI_ID_1&ctn=01011111111&ctn_reg_id=&app_type=SMA&start_num=2&req_count=3";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/json");
		
		System.out.println("=====================not_getReservedProgramList_CTN_REG_ID_ToJSON=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_getReservedProgramList_CTN_REG_ID_ToJSON")), is(true));
	}
	
	/**
	 * 시청예약 정보 목록 CTN_REG_ID 정보 없는 경우(TEXT)
	 * @throws Exception
	 */
	@Test
	public void not_getReservedProgramList_CTN_REG_ID_ToText() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.getReservedProgramList.URL");
		String param = "sa_id=200000000000&stb_mac=001c.6270.4959&stb_reg_id=STB_REGI_ID_1&ctn=01011111111&ctn_reg_id=&app_type=SMA&start_num=2&req_count=3";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "");
		
		System.out.println("=====================not_getReservedProgramList_CTN_REG_ID_ToText=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_getReservedProgramList_CTN_REG_ID_ToText")), is(true));
	}
	
	/**
	 * 시청예약 정보 목록 APP_TYPE 정보 없는 경우(XML)
	 * @throws Exception
	 */
	@Test
	public void not_getReservedProgramList_APP_TYPE_ToXML() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.getReservedProgramList.URL");
		String param = "sa_id=200000000000&stb_mac=001c.6270.4959&stb_reg_id=STB_REGI_ID_1&ctn=01011111111&ctn_reg_id=CTN_REGI_ID_1&app_type=&start_num=2&req_count=3";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");
		
		System.out.println("=====================not_getReservedProgramList_APP_TYPE_ToXML=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_getReservedProgramList_APP_TYPE_ToXML")), is(true));
	}
	
	/**
	 * 시청예약 정보 목록 APP_TYPE 정보 없는 경우(JSON)
	 * @throws Exception
	 */
	@Test
	public void not_getReservedProgramList_APP_TYPE_ToJSON() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.getReservedProgramList.URL");
		String param = "sa_id=200000000000&stb_mac=001c.6270.4959&stb_reg_id=STB_REGI_ID_1&ctn=01011111111&ctn_reg_id=CTN_REGI_ID_1&app_type=&start_num=2&req_count=3";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/json");
		
		System.out.println("=====================not_getReservedProgramList_APP_TYPE_ToJSON=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_getReservedProgramList_APP_TYPE_ToJSON")), is(true));
	}
	
	/**
	 * 시청예약 정보 목록 APP_TYPE 정보 없는 경우(TEXT)
	 * @throws Exception
	 */
	@Test
	public void not_getReservedProgramList_APP_TYPE_ToText() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.getReservedProgramList.URL");
		String param = "sa_id=200000000000&stb_mac=001c.6270.4959&stb_reg_id=STB_REGI_ID_1&ctn=01011111111&ctn_reg_id=CTN_REGI_ID_1&app_type=&start_num=2&req_count=3";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "");
		
		System.out.println("=====================not_getReservedProgramList_APP_TYPE_ToText=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_getReservedProgramList_APP_TYPE_ToText")), is(true));
	}
	
	/**
	 * 시청예약 정보 목록 검색 시작 인덱스 파라미터(start_num) 정보 없는 경우(XML)
	 * @throws Exception
	 */
	@Test
	public void not_getReservedProgramList_START_NUM_ToXML() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.getReservedProgramList.URL");
		String param = "sa_id=200000000000&stb_mac=001c.6270.4959&stb_reg_id=STB_REGI_ID_1&ctn=01011111111&ctn_reg_id=CTN_REGI_ID_1&app_type=SMA&start_num=&req_count=";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");
		
		System.out.println("=====================not_getReservedProgramList_START_NUM_ToXML=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_getReservedProgramList_START_NUM_ToXML")), is(true));
	}
	
	/**
	 * 시청예약 정보 목록 검색 시작 인덱스 파라미터(start_num) 정보 없는 경우(JSON)
	 * @throws Exception
	 */
	@Test
	public void not_getReservedProgramList_START_NUM_ToJSON() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.getReservedProgramList.URL");
		String param = "sa_id=200000000000&stb_mac=001c.6270.4959&stb_reg_id=STB_REGI_ID_1&ctn=01011111111&ctn_reg_id=CTN_REGI_ID_1&app_type=SMA&start_num=&req_count=";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/json");
		
		System.out.println("=====================not_getReservedProgramList_START_NUM_ToJSON=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_getReservedProgramList_START_NUM_ToJSON")), is(true));
	}
	
	/**
	 * 시청예약 정보 목록 검색 시작 인덱스 파라미터(start_num) 정보 없는 경우(Text)
	 * @throws Exception
	 */
	@Test
	public void not_getReservedProgramList_START_NUM_ToText() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.getReservedProgramList.URL");
		String param = "sa_id=200000000000&stb_mac=001c.6270.4959&stb_reg_id=STB_REGI_ID_1&ctn=01011111111&ctn_reg_id=CTN_REGI_ID_1&app_type=SMA&start_num=&req_count=";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "");
		
		System.out.println("=====================not_getReservedProgramList_START_NUM_ToText=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_getReservedProgramList_START_NUM_ToText")), is(true));
	}
	
	/**
	 * 시청예약 정보 목록 검색 시작 인덱스 파라미터(start_num)의 값이 숫자형 데이타가 아닌 값으로 호출 경우(XML)
	 * @throws Exception
	 */
	@Test
	public void not_getReservedProgramList_Number_START_NUM_ToXML() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.getReservedProgramList.URL");
		String param = "sa_id=200000000000&stb_mac=001c.6270.4959&stb_reg_id=STB_REGI_ID_1&ctn=01011111111&ctn_reg_id=CTN_REGI_ID_1&app_type=SMA&start_num=AAA&req_count=";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");
		
		System.out.println("=====================not_getReservedProgramList_Number_START_NUM_ToXML=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_getReservedProgramList_Number_START_NUM_ToXML")), is(true));
	}
	
	/**
	 * 시청예약 정보 목록 검색 시작 인덱스 파라미터(start_num)의 값이 숫자형 데이타가 아닌 값으로 호출 경우(JSON)
	 * @throws Exception
	 */
	@Test
	public void not_getReservedProgramList_Number_START_NUM_ToJSON() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.getReservedProgramList.URL");
		String param = "sa_id=200000000000&stb_mac=001c.6270.4959&stb_reg_id=STB_REGI_ID_1&ctn=01011111111&ctn_reg_id=CTN_REGI_ID_1&app_type=SMA&start_num=AAA&req_count=";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/json");
		
		System.out.println("=====================not_getReservedProgramList_Number_START_NUM_ToJSON=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_getReservedProgramList_Number_START_NUM_ToJSON")), is(true));
	}
	
	/**
	 * 시청예약 정보 목록 검색 시작 인덱스 파라미터(start_num)의 값이 숫자형 데이타가 아닌 값으로 호출 경우(Text)
	 * @throws Exception
	 */
	@Test
	public void not_getReservedProgramList_Number_START_NUM_ToText() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.getReservedProgramList.URL");
		String param = "sa_id=200000000000&stb_mac=001c.6270.4959&stb_reg_id=STB_REGI_ID_1&ctn=01011111111&ctn_reg_id=CTN_REGI_ID_1&app_type=SMA&start_num=AAA&req_count=";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "");
		
		System.out.println("=====================not_getReservedProgramList_Number_START_NUM_ToText=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_getReservedProgramList_Number_START_NUM_ToText")), is(true));
	}
	
	/**
	 * 시청예약 정보 목록 검색 레코드 개수 파라미터(req_count)을 빼고 호출 경우 (XML)
	 * @throws Exception
	 */
	@Test
	public void not_getReservedProgramList_REQ_COUNT_ToXML() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.getReservedProgramList.URL");
		String param = "sa_id=200000000000&stb_mac=001c.6270.4959&stb_reg_id=STB_REGI_ID_1&ctn=01011111111&ctn_reg_id=CTN_REGI_ID_1&app_type=SMA&start_num=1&req_count=";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");
		
		System.out.println("=====================not_getReservedProgramList_REQ_COUNT_ToXML=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_getReservedProgramList_REQ_COUNT_ToXML")), is(true));
	}
	
	/**
	 * 시청예약 정보 목록 검색 레코드 개수 파라미터(req_count)을 빼고 호출 경우 (JSON)
	 * @throws Exception
	 */
	@Test
	public void not_getReservedProgramList_REQ_COUNT_ToJSON() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.getReservedProgramList.URL");
		String param = "sa_id=200000000000&stb_mac=001c.6270.4959&stb_reg_id=STB_REGI_ID_1&ctn=01011111111&ctn_reg_id=CTN_REGI_ID_1&app_type=SMA&start_num=1&req_count=";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/json");
		
		System.out.println("=====================not_getReservedProgramList_REQ_COUNT_ToJSON=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_getReservedProgramList_REQ_COUNT_ToJSON")), is(true));
	}
	
	/**
	 * 시청예약 정보 목록 검색 레코드 개수 파라미터(req_count)을 빼고 호출 경우 (Text)
	 * @throws Exception
	 */
	@Test
	public void not_getReservedProgramList_REQ_COUNT_ToText() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.getReservedProgramList.URL");
		String param = "sa_id=200000000000&stb_mac=001c.6270.4959&stb_reg_id=STB_REGI_ID_1&ctn=01011111111&ctn_reg_id=CTN_REGI_ID_1&app_type=SMA&start_num=1&req_count=";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "");
		
		System.out.println("=====================not_getReservedProgramList_REQ_COUNT_ToText=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_getReservedProgramList_REQ_COUNT_ToText")), is(true));
	}
	
	/**
	 * 시청예약 정보 목록 검색 레코드 개수 파라미터(req_count)의 값이 숫자형 데이타가 아닌 값으로 호출 경우 (XML)
	 * @throws Exception
	 */
	@Test
	public void not_getReservedProgramList_Number_REQ_COUNT_ToXML() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.getReservedProgramList.URL");
		String param = "sa_id=200000000000&stb_mac=001c.6270.4959&stb_reg_id=STB_REGI_ID_1&ctn=01011111111&ctn_reg_id=CTN_REGI_ID_1&app_type=SMA&start_num=1&req_count=AAA";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");

		System.out.println("=====================not_getReservedProgramList_Number_REQ_COUNT_ToXML=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_getReservedProgramList_Number_REQ_COUNT_ToXML")), is(true));
	}
	
	/**
	 * 시청예약 정보 목록 검색 레코드 개수 파라미터(req_count)의 값이 숫자형 데이타가 아닌 값으로 호출 경우 (JSON)
	 * @throws Exception
	 */
	@Test
	public void not_getReservedProgramList_Number_REQ_COUNT_ToJSON() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.getReservedProgramList.URL");
		String param = "sa_id=200000000000&stb_mac=001c.6270.4959&stb_reg_id=STB_REGI_ID_1&ctn=01011111111&ctn_reg_id=CTN_REGI_ID_1&app_type=SMA&start_num=1&req_count=AAA";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/json");

		System.out.println("=====================not_getReservedProgramList_Number_REQ_COUNT_ToJSON=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_getReservedProgramList_Number_REQ_COUNT_ToJSON")), is(true));
	}
	
	/**
	 * 시청예약 정보 목록 검색 레코드 개수 파라미터(req_count)의 값이 숫자형 데이타가 아닌 값으로 호출 경우 (Text)
	 * @throws Exception
	 */
	@Test
	public void not_getReservedProgramList_Number_REQ_COUNT_ToText() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.getReservedProgramList.URL");
		String param = "sa_id=200000000000&stb_mac=001c.6270.4959&stb_reg_id=STB_REGI_ID_1&ctn=01011111111&ctn_reg_id=CTN_REGI_ID_1&app_type=SMA&start_num=1&req_count=AAA";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "");

		System.out.println("=====================not_getReservedProgramList_Number_REQ_COUNT_ToText=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_getReservedProgramList_Number_REQ_COUNT_ToText")), is(true));
	}
	
	/**
	 * 시청예약 정보 목록 검색 레코드 개수 파라미터(req_count)의 값이 0 이하의 값으로 호출 경우 (XML)
	 * @throws Exception
	 */
	@Test
	public void under_Zero_REQ_COUNT_ToXML() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.getReservedProgramList.URL");
		String param = "sa_id=200000000000&stb_mac=001c.6270.4959&stb_reg_id=STB_REGI_ID_1&ctn=01011111111&ctn_reg_id=CTN_REGI_ID_1&app_type=SMA&start_num=1&req_count=-1";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");
		
		System.out.println("=====================under_Zero_REQ_COUNT_ToXML=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("under_Zero_REQ_COUNT_ToXML")), is(true));
	}
	
	/**
	 * 시청예약 정보 목록 검색 레코드 개수 파라미터(req_count)의 값이 0 이하의 값으로 호출 경우 (JSON)
	 * @throws Exception
	 */
	@Test
	public void under_Zero_REQ_COUNT_ToJSON() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.getReservedProgramList.URL");
		String param = "sa_id=200000000000&stb_mac=001c.6270.4959&stb_reg_id=STB_REGI_ID_1&ctn=01011111111&ctn_reg_id=CTN_REGI_ID_1&app_type=SMA&start_num=1&req_count=-1";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/json");
		
		System.out.println("=====================under_Zero_REQ_COUNT_ToJSON=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("under_Zero_REQ_COUNT_ToJSON")), is(true));
	}
	
	/**
	 * 시청예약 정보 목록 검색 레코드 개수 파라미터(req_count)의 값이 0 이하의 값으로 호출 경우 (Text)
	 * @throws Exception
	 */
	@Test
	public void under_Zero_REQ_COUNT_ToText() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.getReservedProgramList.URL");
		String param = "sa_id=200000000000&stb_mac=001c.6270.4959&stb_reg_id=STB_REGI_ID_1&ctn=01011111111&ctn_reg_id=CTN_REGI_ID_1&app_type=SMA&start_num=1&req_count=-1";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "");
		
		System.out.println("=====================under_Zero_REQ_COUNT_ToText=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("under_Zero_REQ_COUNT_ToText")), is(true));
	}
}
