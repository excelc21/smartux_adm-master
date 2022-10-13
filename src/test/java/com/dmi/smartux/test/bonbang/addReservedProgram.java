package com.dmi.smartux.test.bonbang;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.net.URLDecoder;
import java.net.URLEncoder;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.dmi.smartux.test.common.property.TestSmartUXProperties;
import com.dmi.smartux.test.common.util.TestGlobalCom;

public class addReservedProgram {
	
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
		
		// 테스트에서 사용할 로그 파일을 연다
		TestGlobalCom.testLogOpen();
	}
	
	@AfterClass
	public static void afterClass() throws Exception {
		// 테스트에서 사용한 로그 파일을 닫는다
		TestGlobalCom.testLogClose();
		
		String url = TestSmartUXProperties.getProperty("test.queryURL");
		String param = "query_id=test_bonbang.delete_reserved_test_data&type=delete";
		TestGlobalCom.callHttpClient(host, port, url, param, timeout);
		
		param = "query_id=test_bonbang.insert_reserved_to_org_table&type=insert";
		TestGlobalCom.callHttpClient(host, port, url, param, timeout);
		
		param = "query_id=test_bonbang.drop_reserved_backup_table&type=drop";
		TestGlobalCom.callHttpClient(host, port, url, param, timeout);
		
	}
	
	/**
	 * 시청예약 추가 정상 테스트(XML)
	 * @throws Exception
	 */
	@Test
	public void addReservedProgramToXML() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.addReservedProgram.URL");
		
		String param = "sa_id=sa_id1&stb_mac=stb_mac1&stb_reg_id=stb_reg_id1&ctn=ctn1&ctn_reg_id=ctn_reg_id1&app_type=I30&service_id=S1234&channel_no=011&channel_name=TV조선&program_id=M1234&program_name=무한도전&program_info=15&defin_flag=HD&program_stime=20120404123000";
		
		//중복 데이터 삭제
		removeReservedProgramData(param);
		
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");
		
		System.out.println("=====================addReservedProgramToXML=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("addReservedProgramToXML")), is(true));
	}
	
	/**
	 * 시청예약 추가 정상 테스트(JSON)
	 * @throws Exception
	 */
	@Test
	public void addReservedProgramToJSON() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.addReservedProgram.URL");
		
		String param = "sa_id=sa_id1&stb_mac=stb_mac1&stb_reg_id=stb_reg_id1&ctn=ctn1&ctn_reg_id=ctn_reg_id1&app_type=I30&service_id=S1234&channel_no=011&channel_name=TV조선&program_id=M1234&program_name=무한도전&program_info=15&defin_flag=HD&program_stime=20120404123000";
		
		//중복 데이터 삭제
		removeReservedProgramData(param);
		
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/json");
		
		System.out.println("=====================addReservedProgramToJSON=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("addReservedProgramToJSON")), is(true));
	}
	
	/**
	 * 시청예약 추가 정상 테스트(Text)
	 * @throws Exception
	 */
	@Test
	public void addReservedProgramToText() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.addReservedProgram.URL");
		String param = "sa_id=sa_id1&stb_mac=stb_mac1&stb_reg_id=stb_reg_id1&ctn=ctn1&ctn_reg_id=ctn_reg_id1&app_type=I30&service_id=S1234&channel_no=011&channel_name=TV조선&program_id=M1234&program_name=무한도전&program_info=15&defin_flag=HD&program_stime=20120404123000";
		
		//중복 데이터 삭제
		removeReservedProgramData(param);
		
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "");
		
		System.out.println("=====================addReservedProgramToText=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("addReservedProgramToText")), is(true));
	}
	
	
	/**
	 * 시청예약 추가 SA_ID 정보 없는 경우(XML)
	 * @throws Exception
	 */
	@Test
	public void not_addReservedProgram_SA_ID_ToXML() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.addReservedProgram.URL");
		String param = "sa_id=&stb_mac=stb_mac1&stb_reg_id=stb_reg_id1&ctn=ctn1&ctn_reg_id=ctn_reg_id1&app_type=I30&service_id=S1234&channel_no=011&channel_name=TV조선&program_id=M1234&program_name=무한도전&program_info=15&defin_flag=HD&program_stime=20120404123000";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");

		System.out.println("=====================not_addReservedProgram_SA_ID_ToXML=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_addReservedProgram_SA_ID_ToXML")), is(true));
	}
	
	/**
	 * 시청예약 추가 SA_ID 정보 없는 경우(JSON)
	 * @throws Exception
	 */
	@Test
	public void not_addReservedProgram_SA_ID_ToJSON() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.addReservedProgram.URL");
		String param = "sa_id=&stb_mac=stb_mac1&stb_reg_id=stb_reg_id1&ctn=ctn1&ctn_reg_id=ctn_reg_id1&app_type=I30&service_id=S1234&channel_no=011&channel_name=TV조선&program_id=M1234&program_name=무한도전&program_info=15&defin_flag=HD&program_stime=20120404123000";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/json");
		
		System.out.println("=====================not_addReservedProgram_SA_ID_ToJSON=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_addReservedProgram_SA_ID_ToJSON")), is(true));
	}
	
	/**
	 * 시청예약 추가 SA_ID 정보 없는 경우(Text)
	 * @throws Exception
	 */
	@Test
	public void not_addReservedProgram_SA_ID_ToText() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.addReservedProgram.URL");
		String param = "sa_id=&stb_mac=stb_mac1&stb_reg_id=stb_reg_id1&ctn=ctn1&ctn_reg_id=ctn_reg_id1&app_type=I30&service_id=S1234&channel_no=011&channel_name=TV조선&program_id=M1234&program_name=무한도전&program_info=15&defin_flag=HD&program_stime=20120404123000";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "");
		
		System.out.println("=====================not_addReservedProgram_SA_ID_ToText=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_addReservedProgram_SA_ID_ToText")), is(true));
	}
	
	
	/**
	 * 시청예약 추가 STB_MAC 정보 없는 경우(XML)
	 * @throws Exception
	 */
	@Test
	public void not_addReservedProgram_STB_MAC_ToXML() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.addReservedProgram.URL");
		String param = "sa_id=sa_id1&stb_mac=&stb_reg_id=stb_reg_id1&ctn=ctn1&ctn_reg_id=ctn_reg_id1&app_type=I30&service_id=S1234&channel_no=011&channel_name=TV조선&program_id=M1234&program_name=무한도전&program_info=15&defin_flag=HD&program_stime=20120404123000";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");
		
		System.out.println("=====================not_addReservedProgram_STB_MAC_ToXML=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_addReservedProgram_STB_MAC_ToXML")), is(true));
	}
	
	/**
	 * 시청예약 추가 STB_MAC 정보 없는 경우(JSON)
	 * @throws Exception
	 */
	@Test
	public void not_addReservedProgram_STB_MAC_ToJSON() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.addReservedProgram.URL");
		String param = "sa_id=sa_id1&stb_mac=&stb_reg_id=stb_reg_id1&ctn=ctn1&ctn_reg_id=ctn_reg_id1&app_type=I30&service_id=S1234&channel_no=011&channel_name=TV조선&program_id=M1234&program_name=무한도전&program_info=15&defin_flag=HD&program_stime=20120404123000";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/json");
		
		System.out.println("=====================not_addReservedProgram_STB_MAC_ToJSON=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_addReservedProgram_STB_MAC_ToJSON")), is(true));
	}
	
	/**
	 * 시청예약 추가 STB_MAC 정보 없는 경우(TEXT)
	 * @throws Exception
	 */
	@Test
	public void not_addReservedProgram_STB_MAC_ToText() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.addReservedProgram.URL");
		String param = "sa_id=sa_id1&stb_mac=&stb_reg_id=stb_reg_id1&ctn=ctn1&ctn_reg_id=ctn_reg_id1&app_type=I30&service_id=S1234&channel_no=011&channel_name=TV조선&program_id=M1234&program_name=무한도전&program_info=15&defin_flag=HD&program_stime=20120404123000";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "");
		
		System.out.println("=====================not_addReservedProgram_STB_MAC_ToText=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_addReservedProgram_STB_MAC_ToText")), is(true));
	}
	
	/**
	 * 시청예약 추가 STB_REG_ID 정보 없는 경우(XML)
	 * @throws Exception
	 */
	@Test
	public void not_addReservedProgram_STB_REG_ID_ToXML() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.addReservedProgram.URL");
		String param = "sa_id=sa_id1&stb_mac=stb_mac1&stb_reg_id=&ctn=ctn1&ctn_reg_id=ctn_reg_id1&app_type=I30&service_id=S1234&channel_no=011&channel_name=TV조선&program_id=M1234&program_name=무한도전&program_info=15&defin_flag=HD&program_stime=20120404123000";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");
		
		System.out.println("=====================not_addReservedProgram_STB_REG_ID_ToXML=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_addReservedProgram_STB_REG_ID_ToXML")), is(true));
	}
	
	/**
	 * 시청예약 추가 STB_REG_ID 정보 없는 경우(JSON)
	 * @throws Exception
	 */
	@Test
	public void not_addReservedProgram_STB_REG_ID_ToJSON() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.addReservedProgram.URL");
		String param = "sa_id=sa_id1&stb_mac=stb_mac1&stb_reg_id=&ctn=ctn1&ctn_reg_id=ctn_reg_id1&app_type=I30&service_id=S1234&channel_no=011&channel_name=TV조선&program_id=M1234&program_name=무한도전&program_info=15&defin_flag=HD&program_stime=20120404123000";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/json");
		
		System.out.println("=====================not_addReservedProgram_STB_REG_ID_ToJSON=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_addReservedProgram_STB_REG_ID_ToJSON")), is(true));
	}
	
	/**
	 * 시청예약 추가 STB_REG_ID 정보 없는 경우(TEXT)
	 * @throws Exception
	 */
	@Test
	public void not_addReservedProgram_STB_REG_ID_ToText() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.addReservedProgram.URL");
		String param = "sa_id=sa_id1&stb_mac=stb_mac1&stb_reg_id=&ctn=ctn1&ctn_reg_id=ctn_reg_id1&app_type=I30&service_id=S1234&channel_no=011&channel_name=TV조선&program_id=M1234&program_name=무한도전&program_info=15&defin_flag=HD&program_stime=20120404123000";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "");
		
		System.out.println("=====================not_addReservedProgram_STB_REG_ID_ToText=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_addReservedProgram_STB_REG_ID_ToText")), is(true));
	}
	
	/**
	 * 시청예약 추가 CTN 정보 없는 경우(XML)
	 * @throws Exception
	 */
	@Test
	public void not_addReservedProgram_CTN_ToXML() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.addReservedProgram.URL");
		String param = "sa_id=sa_id1&stb_mac=stb_mac1&stb_reg_id=stb_reg_id1&ctn=&ctn_reg_id=ctn_reg_id1&app_type=I30&service_id=S1234&channel_no=011&channel_name=TV조선&program_id=M1234&program_name=무한도전&program_info=15&defin_flag=HD&program_stime=20120404123000";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");
		
		System.out.println("=====================not_addReservedProgram_CTN_ToXML=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_addReservedProgram_CTN_ToXML")), is(true));
	}
	
	/**
	 * 시청예약 추가 CTN 정보 없는 경우(JSON)
	 * @throws Exception
	 */
	@Test
	public void not_addReservedProgram_CTN_ToJSON() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.addReservedProgram.URL");
		String param = "sa_id=sa_id1&stb_mac=stb_mac1&stb_reg_id=stb_reg_id1&ctn=&ctn_reg_id=ctn_reg_id1&app_type=I30&service_id=S1234&channel_no=011&channel_name=TV조선&program_id=M1234&program_name=무한도전&program_info=15&defin_flag=HD&program_stime=20120404123000";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/json");
		
		System.out.println("=====================not_addReservedProgram_CTN_ToJSON=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_addReservedProgram_CTN_ToJSON")), is(true));
	}
	
	/**
	 * 시청예약 추가 CTN 정보 없는 경우(TEXT)
	 * @throws Exception
	 */
	@Test
	public void not_addReservedProgram_CTN_ToText() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.addReservedProgram.URL");
		String param = "sa_id=sa_id1&stb_mac=stb_mac1&stb_reg_id=stb_reg_id1&ctn=&ctn_reg_id=ctn_reg_id1&app_type=I30&service_id=S1234&channel_no=011&channel_name=TV조선&program_id=M1234&program_name=무한도전&program_info=15&defin_flag=HD&program_stime=20120404123000";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "");
		
		System.out.println("=====================not_addReservedProgram_CTN_ToText=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_addReservedProgram_CTN_ToText")), is(true));
	}
	
	/**
	 * 시청예약 추가 CTN_REG_ID 정보 없는 경우(XML)
	 * @throws Exception
	 */
	@Test
	public void not_addReservedProgram_CTN_REG_ID_ToXML() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.addReservedProgram.URL");
		String param = "sa_id=sa_id1&stb_mac=stb_mac1&stb_reg_id=stb_reg_id1&ctn=ctn1&ctn_reg_id=&app_type=I30&service_id=S1234&channel_no=011&channel_name=TV조선&program_id=M1234&program_name=무한도전&program_info=15&defin_flag=HD&program_stime=20120404123000";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");
		
		System.out.println("=====================not_addReservedProgram_CTN_REG_ID_ToXML=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_addReservedProgram_CTN_REG_ID_ToXML")), is(true));
	}
	
	/**
	 * 시청예약 추가 CTN_REG_ID 정보 없는 경우(JSON)
	 * @throws Exception
	 */
	@Test
	public void not_addReservedProgram_CTN_REG_ID_ToJSON() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.addReservedProgram.URL");
		String param = "sa_id=sa_id1&stb_mac=stb_mac1&stb_reg_id=stb_reg_id1&ctn=ctn1&ctn_reg_id=&app_type=I30&service_id=S1234&channel_no=011&channel_name=TV조선&program_id=M1234&program_name=무한도전&program_info=15&defin_flag=HD&program_stime=20120404123000";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/json");
		
		System.out.println("=====================not_addReservedProgram_CTN_REG_ID_ToJSON=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_addReservedProgram_CTN_REG_ID_ToJSON")), is(true));
	}
	
	/**
	 * 시청예약 추가 CTN_REG_ID 정보 없는 경우(TEXT)
	 * @throws Exception
	 */
	@Test
	public void not_addReservedProgram_CTN_REG_ID_ToText() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.addReservedProgram.URL");
		String param = "sa_id=sa_id1&stb_mac=stb_mac1&stb_reg_id=stb_reg_id1&ctn=ctn1&ctn_reg_id=&app_type=I30&service_id=S1234&channel_no=011&channel_name=TV조선&program_id=M1234&program_name=무한도전&program_info=15&defin_flag=HD&program_stime=20120404123000";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "");
		
		System.out.println("=====================not_addReservedProgram_CTN_REG_ID_ToText=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_addReservedProgram_CTN_REG_ID_ToText")), is(true));
	}
	
	/**
	 * 시청예약 추가 APP_TYPE 정보 없는 경우(XML)
	 * @throws Exception
	 */
	@Test
	public void not_addReservedProgram_APP_TYPE_ToXML() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.addReservedProgram.URL");
		String param = "sa_id=sa_id1&stb_mac=stb_mac1&stb_reg_id=stb_reg_id1&ctn=ctn1&ctn_reg_id=ctn_reg_id1&app_type=&service_id=S1234&channel_no=011&channel_name=TV조선&program_id=M1234&program_name=무한도전&program_info=15&defin_flag=HD&program_stime=20120404123000";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");
		
		System.out.println("=====================not_addReservedProgram_APP_TYPE_ToXML=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_addReservedProgram_APP_TYPE_ToXML")), is(true));
	}
	
	/**
	 * 시청예약 추가 APP_TYPE 정보 없는 경우(JSON)
	 * @throws Exception
	 */
	@Test
	public void not_addReservedProgram_APP_TYPE_ToJSON() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.addReservedProgram.URL");
		String param = "sa_id=sa_id1&stb_mac=stb_mac1&stb_reg_id=stb_reg_id1&ctn=ctn1&ctn_reg_id=ctn_reg_id1&app_type=&service_id=S1234&channel_no=011&channel_name=TV조선&program_id=M1234&program_name=무한도전&program_info=15&defin_flag=HD&program_stime=20120404123000";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/json");
		
		System.out.println("=====================not_addReservedProgram_APP_TYPE_ToJSON=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_addReservedProgram_APP_TYPE_ToJSON")), is(true));
	}
	
	/**
	 * 시청예약 추가 APP_TYPE 정보 없는 경우(TEXT)
	 * @throws Exception
	 */
	@Test
	public void not_addReservedProgram_APP_TYPE_ToText() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.addReservedProgram.URL");
		String param = "sa_id=sa_id1&stb_mac=stb_mac1&stb_reg_id=stb_reg_id1&ctn=ctn1&ctn_reg_id=ctn_reg_id1&app_type=&service_id=S1234&channel_no=011&channel_name=TV조선&program_id=M1234&program_name=무한도전&program_info=15&defin_flag=HD&program_stime=20120404123000";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "");
		
		System.out.println("=====================not_addReservedProgram_APP_TYPE_ToText=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_addReservedProgram_APP_TYPE_ToText")), is(true));
	}
	
	/**
	 * 시청예약 추가 SERVICE_ID 정보 없는 경우(XML)
	 * @throws Exception
	 */
	@Test
	public void not_addReservedProgram_SERVICE_ID_ToXML() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.addReservedProgram.URL");
		String param = "sa_id=sa_id1&stb_mac=stb_mac1&stb_reg_id=stb_reg_id1&ctn=ctn1&ctn_reg_id=ctn_reg_id1&app_type=I30&service_id=&channel_no=011&channel_name=TV조선&program_id=M1234&program_name=무한도전&program_info=15&defin_flag=HD&program_stime=20120404123000";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");
		
		System.out.println("=====================not_addReservedProgram_SERVICE_ID_ToXML=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_addReservedProgram_SERVICE_ID_ToXML")), is(true));
	}
	
	/**
	 * 시청예약 추가 SERVICE_ID 정보 없는 경우(JSON)
	 * @throws Exception
	 */
	@Test
	public void not_addReservedProgram_SERVICE_ID_ToJSON() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.addReservedProgram.URL");
		String param = "sa_id=sa_id1&stb_mac=stb_mac1&stb_reg_id=stb_reg_id1&ctn=ctn1&ctn_reg_id=ctn_reg_id1&app_type=I30&service_id=&channel_no=011&channel_name=TV조선&program_id=M1234&program_name=무한도전&program_info=15&defin_flag=HD&program_stime=20120404123000";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/json");
		
		System.out.println("=====================not_addReservedProgram_SERVICE_ID_ToJSON=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_addReservedProgram_SERVICE_ID_ToJSON")), is(true));
	}
	
	/**
	 * 시청예약 추가 SERVICE_ID 정보 없는 경우(Text)
	 * @throws Exception
	 */
	@Test
	public void not_addReservedProgram_SERVICE_ID_ToText() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.addReservedProgram.URL");
		String param = "sa_id=sa_id1&stb_mac=stb_mac1&stb_reg_id=stb_reg_id1&ctn=ctn1&ctn_reg_id=ctn_reg_id1&app_type=I30&service_id=&channel_no=011&channel_name=TV조선&program_id=M1234&program_name=무한도전&program_info=15&defin_flag=HD&program_stime=20120404123000";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "");
		
		System.out.println("=====================not_addReservedProgram_SERVICE_ID_ToText=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_addReservedProgram_SERVICE_ID_ToText")), is(true));
	}
	

	/**
	 * 시청예약 추가 CHANNEL_NO 정보 없는 경우(XML)
	 * @throws Exception
	 */
	@Test
	public void not_addReservedProgram_CHANNEL_NO_ToXML() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.addReservedProgram.URL");
		String param = "sa_id=sa_id1&stb_mac=stb_mac1&stb_reg_id=stb_reg_id1&ctn=ctn1&ctn_reg_id=ctn_reg_id1&app_type=I30&service_id=S1234&channel_no=&channel_name=TV조선&program_id=M1234&program_name=무한도전&program_info=15&defin_flag=HD&program_stime=20120404123000";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");
		
		System.out.println("=====================not_addReservedProgram_CHANNEL_NO_ToXML=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_addReservedProgram_CHANNEL_NO_ToXML")), is(true));
	}
	
	/**
	 * 시청예약 추가 CHANNEL_NO 정보 없는 경우(JSON)
	 * @throws Exception
	 */
	@Test
	public void not_addReservedProgram_CHANNEL_NO_ToJSON() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.addReservedProgram.URL");
		String param = "sa_id=sa_id1&stb_mac=stb_mac1&stb_reg_id=stb_reg_id1&ctn=ctn1&ctn_reg_id=ctn_reg_id1&app_type=I30&service_id=S1234&channel_no=&channel_name=TV조선&program_id=M1234&program_name=무한도전&program_info=15&defin_flag=HD&program_stime=20120404123000";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/json");
		
		System.out.println("=====================not_addReservedProgram_CHANNEL_NO_ToJSON=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_addReservedProgram_CHANNEL_NO_ToJSON")), is(true));
	}
	
	/**
	 * 시청예약 추가 CHANNEL_NO 정보 없는 경우(Text)
	 * @throws Exception
	 */
	@Test
	public void not_addReservedProgram_CHANNEL_NO_ToText() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.addReservedProgram.URL");
		String param = "sa_id=sa_id1&stb_mac=stb_mac1&stb_reg_id=stb_reg_id1&ctn=ctn1&ctn_reg_id=ctn_reg_id1&app_type=I30&service_id=S1234&channel_no=&channel_name=TV조선&program_id=M1234&program_name=무한도전&program_info=15&defin_flag=HD&program_stime=20120404123000";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "");
		
		System.out.println("=====================not_addReservedProgram_CHANNEL_NO_ToText=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_addReservedProgram_CHANNEL_NO_ToText")), is(true));
	}
	
	
	/**
	 * 시청예약 추가 CHANNEL_NAME 정보 없는 경우(XML)
	 * @throws Exception
	 */
	@Test
	public void not_addReservedProgram_CHANNEL_NAME_ToXML() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.addReservedProgram.URL");
		String param = "sa_id=sa_id1&stb_mac=stb_mac1&stb_reg_id=stb_reg_id1&ctn=ctn1&ctn_reg_id=ctn_reg_id1&app_type=I30&service_id=S1234&channel_no=011&channel_name=&program_id=M1234&program_name=무한도전&program_info=15&defin_flag=HD&program_stime=20120404123000";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");
		
		System.out.println("=====================not_addReservedProgram_CHANNEL_NAME_ToXML=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_addReservedProgram_CHANNEL_NAME_ToXML")), is(true));
	}
	
	/**
	 * 시청예약 추가 CHANNEL_NAME 정보 없는 경우(JSON)
	 * @throws Exception
	 */
	@Test
	public void not_addReservedProgram_CHANNEL_NAME_ToJSON() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.addReservedProgram.URL");
		String param = "sa_id=sa_id1&stb_mac=stb_mac1&stb_reg_id=stb_reg_id1&ctn=ctn1&ctn_reg_id=ctn_reg_id1&app_type=I30&service_id=S1234&channel_no=011&channel_name=&program_id=M1234&program_name=무한도전&program_info=15&defin_flag=HD&program_stime=20120404123000";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/json");
		
		System.out.println("=====================not_addReservedProgram_CHANNEL_NAME_ToJSON=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_addReservedProgram_CHANNEL_NAME_ToJSON")), is(true));
	}
	
	/**
	 * 시청예약 추가 CHANNEL_NAME 정보 없는 경우(Text)
	 * @throws Exception
	 */
	@Test
	public void not_addReservedProgram_CHANNEL_NAME_ToText() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.addReservedProgram.URL");
		String param = "sa_id=sa_id1&stb_mac=stb_mac1&stb_reg_id=stb_reg_id1&ctn=ctn1&ctn_reg_id=ctn_reg_id1&app_type=I30&service_id=S1234&channel_no=011&channel_name=&program_id=M1234&program_name=무한도전&program_info=15&defin_flag=HD&program_stime=20120404123000";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "");
		
		System.out.println("=====================not_addReservedProgram_CHANNEL_NAME_ToText=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_addReservedProgram_CHANNEL_NAME_ToText")), is(true));
	}
	
	/**
	 * 시청예약 추가 PROGRAM_ID 정보 없는 경우(XML)
	 * @throws Exception
	 */
	@Test
	public void not_addReservedProgram_PROGRAM_ID_ToXML() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.addReservedProgram.URL");
		String param = "sa_id=sa_id1&stb_mac=stb_mac1&stb_reg_id=stb_reg_id1&ctn=ctn1&ctn_reg_id=ctn_reg_id1&app_type=I30&service_id=S1234&channel_no=011&channel_name=TV조선&program_id=&program_name=무한도전&program_info=15&defin_flag=HD&program_stime=20120404123000";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");
		
		System.out.println("=====================not_addReservedProgram_PROGRAM_ID_ToXML=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_addReservedProgram_PROGRAM_ID_ToXML")), is(true));
	}
	
	/**
	 * 시청예약 추가 PROGRAM_ID 정보 없는 경우(JSON)
	 * @throws Exception
	 */
	@Test
	public void not_addReservedProgram_PROGRAM_ID_ToJSON() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.addReservedProgram.URL");
		String param = "sa_id=sa_id1&stb_mac=stb_mac1&stb_reg_id=stb_reg_id1&ctn=ctn1&ctn_reg_id=ctn_reg_id1&app_type=I30&service_id=S1234&channel_no=011&channel_name=TV조선&program_id=&program_name=무한도전&program_info=15&defin_flag=HD&program_stime=20120404123000";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/json");
		
		System.out.println("=====================not_addReservedProgram_PROGRAM_ID_ToJSON=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_addReservedProgram_PROGRAM_ID_ToJSON")), is(true));
	}
	
	/**
	 * 시청예약 추가 PROGRAM_ID 정보 없는 경우(Text)
	 * @throws Exception
	 */
	@Test
	public void not_addReservedProgram_PROGRAM_ID_ToText() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.addReservedProgram.URL");
		String param = "sa_id=sa_id1&stb_mac=stb_mac1&stb_reg_id=stb_reg_id1&ctn=ctn1&ctn_reg_id=ctn_reg_id1&app_type=I30&service_id=S1234&channel_no=011&channel_name=TV조선&program_id=&program_name=무한도전&program_info=15&defin_flag=HD&program_stime=20120404123000";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "");
		
		System.out.println("=====================not_addReservedProgram_PROGRAM_ID_ToText=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_addReservedProgram_PROGRAM_ID_ToText")), is(true));
	}
	
	/**
	 * 시청예약 추가 PROGRAM_NAME 정보 없는 경우(XML)
	 * @throws Exception
	 */
	@Test
	public void not_addReservedProgram_PROGRAM_NAME_ToXML() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.addReservedProgram.URL");
		String param = "sa_id=sa_id1&stb_mac=stb_mac1&stb_reg_id=stb_reg_id1&ctn=ctn1&ctn_reg_id=ctn_reg_id1&app_type=I30&service_id=S1234&channel_no=011&channel_name=TV조선&program_id=M1234&program_name=&program_info=15&defin_flag=HD&program_stime=20120404123000";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");
		
		System.out.println("=====================not_addReservedProgram_PROGRAM_NAME_ToXML=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_addReservedProgram_PROGRAM_NAME_ToXML")), is(true));
	}
	
	/**
	 * 시청예약 추가 PROGRAM_NAME 정보 없는 경우(JSON)
	 * @throws Exception
	 */
	@Test
	public void not_addReservedProgram_PROGRAM_NAME_ToJSON() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.addReservedProgram.URL");
		String param = "sa_id=sa_id1&stb_mac=stb_mac1&stb_reg_id=stb_reg_id1&ctn=ctn1&ctn_reg_id=ctn_reg_id1&app_type=I30&service_id=S1234&channel_no=011&channel_name=TV조선&program_id=M1234&program_name=&program_info=15&defin_flag=HD&program_stime=20120404123000";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/json");
		
		System.out.println("=====================not_addReservedProgram_PROGRAM_NAME_ToJSON=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_addReservedProgram_PROGRAM_NAME_ToJSON")), is(true));
	}
	
	/**
	 * 시청예약 추가 PROGRAM_NAME 정보 없는 경우(Text)
	 * @throws Exception
	 */
	@Test
	public void not_addReservedProgram_PROGRAM_NAME_ToText() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.addReservedProgram.URL");
		String param = "sa_id=sa_id1&stb_mac=stb_mac1&stb_reg_id=stb_reg_id1&ctn=ctn1&ctn_reg_id=ctn_reg_id1&app_type=I30&service_id=S1234&channel_no=011&channel_name=TV조선&program_id=M1234&program_name=&program_info=15&defin_flag=HD&program_stime=20120404123000";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "");
		
		System.out.println("=====================not_addReservedProgram_PROGRAM_NAME_ToText=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_addReservedProgram_PROGRAM_NAME_ToText")), is(true));
	}
	
	/**
	 * 시청예약 추가 PROGRAM_INFO 정보 없는 경우(XML)
	 * @throws Exception
	 */
	@Test
	public void not_addReservedProgram_PROGRAM_INFO_ToXML() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.addReservedProgram.URL");
		String param = "sa_id=sa_id1&stb_mac=stb_mac1&stb_reg_id=stb_reg_id1&ctn=ctn1&ctn_reg_id=ctn_reg_id1&app_type=I30&service_id=S1234&channel_no=011&channel_name=TV조선&program_id=M1234&program_name=무한도전&program_info=&defin_flag=HD&program_stime=20120404123000";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");
		
		System.out.println("=====================not_addReservedProgram_PROGRAM_INFO_ToXML=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_addReservedProgram_PROGRAM_INFO_ToXML")), is(true));
	}
	
	/**
	 * 시청예약 추가 PROGRAM_INFO 정보 없는 경우(JSON)
	 * @throws Exception
	 */
	@Test
	public void not_addReservedProgram_PROGRAM_INFO_ToJSON() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.addReservedProgram.URL");
		String param = "sa_id=sa_id1&stb_mac=stb_mac1&stb_reg_id=stb_reg_id1&ctn=ctn1&ctn_reg_id=ctn_reg_id1&app_type=I30&service_id=S1234&channel_no=011&channel_name=TV조선&program_id=M1234&program_name=무한도전&program_info=&defin_flag=HD&program_stime=20120404123000";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/json");
		
		System.out.println("=====================not_addReservedProgram_PROGRAM_INFO_ToJSON=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_addReservedProgram_PROGRAM_INFO_ToJSON")), is(true));
	}
	
	/**
	 * 시청예약 추가 PROGRAM_INFO 정보 없는 경우(Text)
	 * @throws Exception
	 */
	@Test
	public void not_addReservedProgram_PROGRAM_INFO_ToText() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.addReservedProgram.URL");
		String param = "sa_id=sa_id1&stb_mac=stb_mac1&stb_reg_id=stb_reg_id1&ctn=ctn1&ctn_reg_id=ctn_reg_id1&app_type=I30&service_id=S1234&channel_no=011&channel_name=TV조선&program_id=M1234&program_name=무한도전&program_info=&defin_flag=HD&program_stime=20120404123000";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "");
		
		System.out.println("=====================not_addReservedProgram_PROGRAM_INFO_ToText=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_addReservedProgram_PROGRAM_INFO_ToText")), is(true));
	}
	
	/**
	 * 시청예약 추가 DEFIN_FLAG 정보 없는 경우(XML)
	 * @throws Exception
	 */
	@Test
	public void not_addReservedProgram_DEFIN_FLAG_ToXML() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.addReservedProgram.URL");
		String param = "sa_id=sa_id1&stb_mac=stb_mac1&stb_reg_id=stb_reg_id1&ctn=ctn1&ctn_reg_id=ctn_reg_id1&app_type=I30&service_id=S1234&channel_no=011&channel_name=TV조선&program_id=M1234&program_name=무한도전&program_info=15&defin_flag=&program_stime=20120404123000";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");
		
		System.out.println("=====================not_addReservedProgram_DEFIN_FLAG_ToXML=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_addReservedProgram_DEFIN_FLAG_ToXML")), is(true));
	}
	
	/**
	 * 시청예약 추가 DEFIN_FLAG 정보 없는 경우(JSON)
	 * @throws Exception
	 */
	@Test
	public void not_addReservedProgram_DEFIN_FLAG_ToJSON() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.addReservedProgram.URL");
		String param = "sa_id=sa_id1&stb_mac=stb_mac1&stb_reg_id=stb_reg_id1&ctn=ctn1&ctn_reg_id=ctn_reg_id1&app_type=I30&service_id=S1234&channel_no=011&channel_name=TV조선&program_id=M1234&program_name=무한도전&program_info=15&defin_flag=&program_stime=20120404123000";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/json");
		
		System.out.println("=====================not_addReservedProgram_DEFIN_FLAG_ToJSON=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_addReservedProgram_DEFIN_FLAG_ToJSON")), is(true));
	}
	
	/**
	 * 시청예약 추가 DEFIN_FLAG 정보 없는 경우(Text)
	 * @throws Exception
	 */
	@Test
	public void not_addReservedProgram_DEFIN_FLAG_ToText() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.addReservedProgram.URL");
		String param = "sa_id=sa_id1&stb_mac=stb_mac1&stb_reg_id=stb_reg_id1&ctn=ctn1&ctn_reg_id=ctn_reg_id1&app_type=I30&service_id=S1234&channel_no=011&channel_name=TV조선&program_id=M1234&program_name=무한도전&program_info=15&defin_flag=&program_stime=20120404123000";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "");
		
		System.out.println("=====================not_addReservedProgram_DEFIN_FLAG_ToText=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_addReservedProgram_DEFIN_FLAG_ToText")), is(true));
	}
	
	/**
	 * 시청예약 추가 PROGRAM_STIME 정보 없는 경우(XML)
	 * @throws Exception
	 */
	@Test
	public void not_addReservedProgram_PROGRAM_STIME_ToXML() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.addReservedProgram.URL");
		String param = "sa_id=sa_id1&stb_mac=stb_mac1&stb_reg_id=stb_reg_id1&ctn=ctn1&ctn_reg_id=ctn_reg_id1&app_type=I30&service_id=S1234&channel_no=011&channel_name=TV조선&program_id=M1234&program_name=무한도전&program_info=15&defin_flag=HD&program_stime=";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");
		
		System.out.println("=====================not_addReservedProgram_PROGRAM_STIME_ToXML=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_addReservedProgram_PROGRAM_STIME_ToXML")), is(true));
	}
	
	/**
	 * 시청예약 추가 PROGRAM_STIME 정보 없는 경우(JSON)
	 * @throws Exception
	 */
	@Test
	public void not_addReservedProgram_PROGRAM_STIME_ToJSON() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.addReservedProgram.URL");
		String param = "sa_id=sa_id1&stb_mac=stb_mac1&stb_reg_id=stb_reg_id1&ctn=ctn1&ctn_reg_id=ctn_reg_id1&app_type=I30&service_id=S1234&channel_no=011&channel_name=TV조선&program_id=M1234&program_name=무한도전&program_info=15&defin_flag=HD&program_stime=";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/json");
		
		System.out.println("=====================not_addReservedProgram_PROGRAM_STIME_ToJSON=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_addReservedProgram_PROGRAM_STIME_ToJSON")), is(true));
	}
	
	/**
	 * 시청예약 추가 PROGRAM_STIME 정보 없는 경우(Text)
	 * @throws Exception
	 */
	@Test
	public void not_addReservedProgram_PROGRAM_STIME_ToText() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.addReservedProgram.URL");
		String param = "sa_id=sa_id1&stb_mac=stb_mac1&stb_reg_id=stb_reg_id1&ctn=ctn1&ctn_reg_id=ctn_reg_id1&app_type=I30&service_id=S1234&channel_no=011&channel_name=TV조선&program_id=M1234&program_name=무한도전&program_info=15&defin_flag=HD&program_stime=";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "");
		
		System.out.println("=====================not_addReservedProgram_PROGRAM_STIME_ToText=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("not_addReservedProgram_PROGRAM_STIME_ToText")), is(true));
	}
	
	/**
	 * 시청예약 추가 기존 시청예약 정보 존재 경우(XML)
	 * @throws Exception
	 */
	@Test
	public void bedata_ToXML() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.addReservedProgram.URL");
		String param = "sa_id=sa_id1&stb_mac=stb_mac1&stb_reg_id=stb_reg_id1&ctn=ctn1&ctn_reg_id=ctn_reg_id1&app_type=I30&service_id=S1234&channel_no=011&channel_name=TV조선&program_id=M1234&program_name=무한도전&program_info=15&defin_flag=HD&program_stime=20120404123000";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");
		
		System.out.println("=====================bedata_ToXML=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("bedata_ToXML")), is(true));
	}
	
	/**
	 * 시청예약 추가 기존 시청예약 정보 존재 경우(JSON)
	 * @throws Exception
	 */
	@Test
	public void bedata_ToJSON() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.addReservedProgram.URL");
		String param = "sa_id=sa_id1&stb_mac=stb_mac1&stb_reg_id=stb_reg_id1&ctn=ctn1&ctn_reg_id=ctn_reg_id1&app_type=I30&service_id=S1234&channel_no=011&channel_name=TV조선&program_id=M1234&program_name=무한도전&program_info=15&defin_flag=HD&program_stime=20120404123000";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/json");
		
		System.out.println("=====================bedata_ToJSON=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("bedata_ToJSON")), is(true));
	}
	
	/**
	 * 시청예약 추가 기존 시청예약 정보 존재 경우(Text)
	 * @throws Exception
	 */
	@Test
	public void bedata_ToText() throws Exception {
		String url = TestSmartUXProperties.getProperty("test.addReservedProgram.URL");
		String param = "sa_id=sa_id1&stb_mac=stb_mac1&stb_reg_id=stb_reg_id1&ctn=ctn1&ctn_reg_id=ctn_reg_id1&app_type=I30&service_id=S1234&channel_no=011&channel_name=TV조선&program_id=M1234&program_name=무한도전&program_info=15&defin_flag=HD&program_stime=20120404123000";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "");
		
		System.out.println("=====================bedata_ToText=============================");
		System.out.println(result);
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("bedata_ToText")), is(true));
	}

	/***************************************************************************
	 * UTIL
	 ***************************************************************************/
	/**
	 * 시청 예약 데이터 삽입
	 * @param f_param	URL
	 * @throws Exception
	 */
	private void addReservedProgramData(String f_param) throws Exception{
		String url = TestSmartUXProperties.getProperty("test.addReservedProgram.URL");
		String param = f_param;
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "");
		
		//System.out.println("=====================addReservedProgramData=============================");
		//System.out.println(result);
	}
	
	/**
	 * 시청예약 데이터 삭제
	 * @param f_param	URL
	 * @throws Exception
	 */
	private void removeReservedProgramData(String f_param) throws Exception {
		String url = TestSmartUXProperties.getProperty("test.removeReservedProgram.URL");
		//String param = "sa_id=sa_id1&stb_mac=stb_mac1&stb_reg_id=stb_reg_id1&ctn=ctn1&ctn_reg_id=ctn_reg_id1&app_type=I30&service_id=S1234&program_id=M1234";
		String param = f_param;
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "");
		
		//System.out.println("=====================removeReservedProgram=============================");
		//System.out.println(result);
	}
	
}
