package com.dmi.smartux.test.configuration;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import net.sf.json.JSONObject;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.dmi.smartux.test.common.property.TestSmartUXProperties;
import com.dmi.smartux.test.common.util.TestGlobalCom;

//import com.dmi.smartux.test.smartstart.service.TestGenreVodBestListService;
//import com.dmi.smartux.test.smartstart.service.impl.TestGenreVodBestListServiceImpl;
//import com.dmi.smartux.test.smartstart.vo.GenreVodBestListResult;
//import com.dmi.smartux.test.smartstart.vo.GenreVodBestListVO;


// @RunWith(SpringJUnit4ClassRunner.class)
// @ContextConfiguration(locations="/com/dmi/smartux/test/resource/spring/test-context.xml")
public class GetConfiguration {
	
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
		String param = "query_id=test_configuration.create_configuration_temp_table&type=create";
		TestGlobalCom.callHttpClient(host, port, url, param, timeout);
		
		param = "query_id=test_configuration.insert_configuration_to_temp_table&type=insert";
		TestGlobalCom.callHttpClient(host, port, url, param, timeout);
		
		param = "query_id=test_configuration.delete_org_configuration_data&type=delete";
		TestGlobalCom.callHttpClient(host, port, url, param, timeout);
		
		param = "query_id=test_configuration.insert_test_data_to_org_configuration_table1&type=insert";
		TestGlobalCom.callHttpClient(host, port, url, param, timeout);
		
		param = "query_id=test_configuration.insert_test_data_to_org_configuration_table2&type=insert";
		TestGlobalCom.callHttpClient(host, port, url, param, timeout);
		
		param = "query_id=test_configuration.insert_test_data_to_org_configuration_table3&type=insert";
		TestGlobalCom.callHttpClient(host, port, url, param, timeout);
		
		// 캐쉬 데이터를 테스트 데이터로 바꿔주기 위한 작업을 진행
		//host = TestSmartUXProperties.getProperty("test.host");								
		//port = Integer.parseInt(TestSmartUXProperties.getProperty("test.port"));
		//url = TestSmartUXProperties.getProperty("test.getConfiguration.URL");
		//param = "sa_id=209017787002&stb_mac=0002.1416.f8b6&app_type=I30&callByScheduler=A";
		//TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");
	
		// 테스트에서 사용할 로그 파일을 연다
		TestGlobalCom.testLogOpen();
		
	}
		

	/**
	 * 스마트스타트 항목리스트 조회시 가입번호 파라미터(sa_id)를 빼고 호출했을때의 결과를 XML로 받을때의 테스트
	 * @throws Exception
	 */
	@Test
	public void getConfiguration_notSA_ID_ToXML() throws Exception{ 
		String url = TestSmartUXProperties.getProperty("test.getConfiguration.URL");
		String param = "stb_mac=0002.1416.f8b6&app_type=I30";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getConfiguration_notSA_ID_ToXML")), is(true));
	}
	
	/**
	 * 스마트스타트 항목리스트 조회시 가입번호 파라미터(sa_id)를 빼고 호출했을때의 결과를 JSON로 받을때의 테스트
	 * @throws Exception
	 */
	@Test
	public void getConfiguration_notSA_ID_ToJSON() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.getConfiguration.URL");
		String param = "stb_mac=0002.1416.f8b6&app_type=I30";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/json");
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getConfiguration_notSA_ID_ToJSON")), is(true));
	}
	
	/**
	 * 스마트스타트 항목리스트 조회시 가입번호 파라미터(sa_id)를 빼고 호출했을때의 결과를 Text로 받을때의 테스트
	 * @throws Exception
	 */
	@Test
	public void getConfiguration_notSA_ID_ToText() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.getConfiguration.URL");
		String param = "stb_mac=0002.1416.f8b6&app_type=I30";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "");
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getConfiguration_notSA_ID_ToText")), is(true));
	}
	
	
	/**
	 * 스마트스타트 항목리스트 조회시 맥주소 파라미터(stb_mac)를 빼고 호출했을때의 결과를 XML로 받을때의 테스트
	 * @throws Exception
	 */
	@Test
	public void getConfiguration_notSTB_MAC_ToXML() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.getConfiguration.URL");
		String param = "sa_id=209017787002&app_type=I30";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getConfiguration_notSTB_MAC_ToXML")), is(true));
	}
	
	/**
	 * 스마트스타트 항목리스트 조회시 맥주소 파라미터(stb_mac)를 빼고 호출했을때의 결과를 JSON로 받을때의 테스트
	 * @throws Exception
	 */
	@Test
	public void getConfiguration_notSTB_MACToJSON() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.getConfiguration.URL");
		String param = "sa_id=209017787002&app_type=I30";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/json");
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getConfiguration_notSTB_MACToJSON")), is(true));
	}
	
	
	/**
	 * 스마트스타트 항목리스트 조회시 맥주소 파라미터(stb_mac)를 빼고 호출했을때의 결과를 Text로 받을때의 테스트
	 * @throws Exception
	 */
	@Test
	public void getConfiguration_notSTB_MAC_ToText() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.getConfiguration.URL");
		String param = "sa_id=209017787002&app_type=I30";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "");
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getConfiguration_notSTB_MAC_ToText")), is(true));
	}
	
	/**
	 * 스마트스타트 항목리스트 조회시 어플타입 파라미터(app_type)를 빼고 호출했을때의 결과를 XML로 받을때의 테스트
	 * @throws Exception
	 */
	@Test
	public void getConfiguration_notAPP_TYPE_ToXML() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.getConfiguration.URL");
		String param = "sa_id=209017787002&stb_mac=0002.1416.f8b60";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");
		
		// 객체를 비교하여 테스트
			assertThat(result.equals(TestSmartUXProperties.getProperty("getConfiguration_notAPP_TYPE_ToXML")), is(true));
		}
	
	/**
	 * 스마트스타트 항목리스트 조회시 어플타입 파라미터(app_type)를 빼고 호출했을때의 결과를 JSON로 받을때의 테스트
	 * @throws Exception
	 */
	@Test
	public void getConfiguration_notAPP_TYPEToJSON() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.getConfiguration.URL");
		String param = "sa_id=209017787002&stb_mac=0002.1416.f8b60";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/json");
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getConfiguration_notAPP_TYPEToJSON")), is(true));
	}
	
	/**
	 * 스마트스타트 항목리스트 조회시 어플타입 파라미터(app_type)를 빼고 호출했을때의 결과를 Text로 받을때의 테스트
	 * @throws Exception
	 */
	@Test
	public void getConfiguration_notAPP_TYPE_ToText() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.getConfiguration.URL");
		String param = "sa_id=209017787002&stb_mac=0002.1416.f8b60";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "");
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getConfiguration_notAPP_TYPE_ToText")), is(true));
	}
		
	/**
	 * 스마트스타트 항목리스트 조회시 어플타입 파라미터(app_type)에 I30이 아닌 다른 값으로 호출했을때의 결과를 XML로 받을때의 테스트
	 * @throws Exception
	 */
	@Test
	public void getConfiguration_not_I30_APP_TYPE_ToXML() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.getConfiguration.URL");
		String param = "sa_id=209017787002&stb_mac=0002.1416.f8b60&app_type=AAA";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getConfiguration_not_I30_APP_TYPE_ToXML")), is(true));
	}

	/**
	 * 스마트스타트 항목리스트 조회시 어플타입 파라미터(app_type)에 I30이 아닌 다른 값으로 호출했을때의 결과를 JSON로 받을때의 테스트
	 * @throws Exception
	 */
	@Test
	public void getConfiguration_not_I30_APP_TYPE_ToJSON() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.getConfiguration.URL");
		String param = "sa_id=209017787002&stb_mac=0002.1416.f8b60&app_type=AAA";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/json");
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getConfiguration_not_I30_APP_TYPE_ToJSON")), is(true));
	}

	/**
	 * 스마트스타트 항목리스트 조회시 어플타입 파라미터(app_type)에 I30이 아닌 다른 값으로 호출했을때의 결과를 Text로 받을때의 테스트
	 * @throws Exception
	 */
	@Test
	public void getConfiguration_not_I30_APP_TYPE_ToText() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.getConfiguration.URL");
		String param = "sa_id=209017787002&stb_mac=0002.1416.f8b60&app_type=AAA";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "");
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getConfiguration_not_I30_APP_TYPE_ToText")), is(true));
	}
	
	@AfterClass
	public static void afterClass() throws Exception {
		// 테스트에서 사용한 로그 파일을 닫는다
		TestGlobalCom.testLogClose();
		
		// 테스트에서 사용한 테이블을 정리한다.
		String url = TestSmartUXProperties.getProperty("test.queryURL");
		String param = "query_id=test_configuration.delete_test_configuration_data&type=delete";
		TestGlobalCom.callHttpClient(host, port, url, param, timeout);
		
		param = "query_id=test_configuration.insert_configuration_to_org_table&type=insert";
		TestGlobalCom.callHttpClient(host, port, url, param, timeout);
		
		param = "query_id=test_configuration.drop_configuration_temp_table&type=drop";
		TestGlobalCom.callHttpClient(host, port, url, param, timeout);
		
		// 캐쉬 작업
		//host = TestSmartUXProperties.getProperty("test.host");
		//port = Integer.parseInt(TestSmartUXProperties.getProperty("test.port"));
		//url = TestSmartUXProperties.getProperty("test.getConfiguration.URL");
		//param = "sa_id=209017787002&stb_mac=0002.1416.f8b6&app_type=I30&callByScheduler=A";
		//TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");				
	}
	
	
}


