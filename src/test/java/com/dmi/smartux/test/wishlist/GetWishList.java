package com.dmi.smartux.test.wishlist;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

//import net.sf.json.JSONObject;

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
public class GetWishList {
	
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
		String param = "query_id=test_wishlist.create_wishlist_backup_table&type=create";
		TestGlobalCom.callHttpClient(host, port, url, param, timeout);
		
		param = "query_id=test_wishlist.insert_wishlist_backup_table&type=insert";
		TestGlobalCom.callHttpClient(host, port, url, param, timeout);
		
		param = "query_id=test_wishlist.delete_org_wishlist_data&type=delete";
		TestGlobalCom.callHttpClient(host, port, url, param, timeout);
		
		param = "query_id=test_wishlist.insert_test_data_to_org_wishlist_table1&type=insert";
		TestGlobalCom.callHttpClient(host, port, url, param, timeout);
		
		param = "query_id=test_wishlist.insert_test_data_to_org_wishlist_table2&type=insert";
		TestGlobalCom.callHttpClient(host, port, url, param, timeout);
		
		param = "query_id=test_wishlist.insert_test_data_to_org_wishlist_table3&type=insert";
		TestGlobalCom.callHttpClient(host, port, url, param, timeout);
		
		param = "query_id=test_wishlist.insert_test_data_to_org_wishlist_table4&type=insert";
		TestGlobalCom.callHttpClient(host, port, url, param, timeout);
		
		
		// 캐쉬 데이터를 테스트 데이터로 바꿔주기 위한 작업을 진행
		//host = TestSmartUXProperties.getProperty("test.host");								
		//port = Integer.parseInt(TestSmartUXProperties.getProperty("test.port"));
		//url = TestSmartUXProperties.getProperty("test.getWishList.URL");
		//param = "sa_id=209017787002&stb_mac=0002.1416.f8b6&app_type=I30&callByScheduler=A";
		//TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");
	
		// 테스트에서 사용할 로그 파일을 연다
		TestGlobalCom.testLogOpen();
	}

	/**
	 * 장르VOD리스트 전체 갯수 조회를 XML로 return 받을때의 테스트
	 * @throws Exception
	 */
	@Test
	public void getWishList_Count_ToXML() throws Exception {
		
		// 단말 API 호출
		String url = TestSmartUXProperties.getProperty("test.getWishList.URL");
		String param = "sa_id=209017787002&stb_mac=0002.1416.f8b6&app_type=I30&start_num=0";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getWishList_Count_ToXML")), is(true));
	}	
	
	/**
	 * 장르VOD리스트 전체 내용 조회를 XML로 return 받을때의 테스트
	 * @throws Exception
	 */
	@Test
	public void getWishList_All_ToXML() throws Exception {
		// 단말 API 호출
		String url = TestSmartUXProperties.getProperty("test.getWishList.URL");
		String param = "sa_id=209017787002&stb_mac=0002.1416.f8b6&app_type=I30&start_num=-1";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getWishList_All_ToXML")), is(true));
	}
	
	/**
	 * 장르VOD리스트 Paging 조회를 XML로 return 받을때의 테스트
	 * @throws Exception
	 */
	@Test
	public void getWishList_Paging_ToXML() throws Exception {
		// 단말 API 호출
		String url = TestSmartUXProperties.getProperty("test.getWishList.URL");
		String param = "sa_id=209017787002&stb_mac=0002.1416.f8b6&app_type=I30&start_num=1&req_count=3";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getWishList_Paging_ToXML")), is(true));
	}
	
	/**
	 * 장르VOD리스트 전체 갯수 조회를 JSON으로 return 받을때의 테스트
	 * @throws Exception
	 */
	@Test
	public void getWishList_Count_ToJSON() throws Exception {
		// 단말 API 호출
		String url = TestSmartUXProperties.getProperty("test.getWishList.URL");
		String param = "sa_id=209017787002&stb_mac=0002.1416.f8b6&app_type=I30&start_num=0";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/json");
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getWishList_Count_ToJSON")), is(true));
	}

	
	/**
	 * 장르VOD리스트 전체 내용 조회를 JSON으로 return 받을때의 테스트
	 * @throws Exception
	 */
	@Test
	public void getWishList_All_ToJSON() throws Exception {
		// 단말 API 호출
		String url = TestSmartUXProperties.getProperty("test.getWishList.URL");
		String param = "sa_id=209017787002&stb_mac=0002.1416.f8b6&app_type=I30&start_num=-1";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/json");
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getWishList_All_ToJSON")), is(true));
	}
	
	/**
	 * 장르VOD리스트 Paging 조회를 JSON로 return 받을때의 테스트
	 * @throws Exception
	 */
	@Test
	public void getWishList_Paging_ToJSON() throws Exception {
		// 단말 API 호출
		String url = TestSmartUXProperties.getProperty("test.getWishList.URL");
		String param = "sa_id=209017787002&stb_mac=0002.1416.f8b6&app_type=I30&start_num=1&req_count=3";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/json");
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getWishList_Paging_ToJSON")), is(true));
	}
	

	/**
	 * 장르VOD리스트 전체 갯수 조회를 Text로 return 받을때의 테스트
	 * @throws Exception
	 */
	@Test
	public void getWishList_Count_ToText() throws Exception {
		// 단말 API 호출
		String url = TestSmartUXProperties.getProperty("test.getWishList.URL");
		String param = "sa_id=209017787002&stb_mac=0002.1416.f8b6&app_type=I30&start_num=0";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "");
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getWishList_Count_ToText")), is(true));
	}
	
	/**
	 * 장르VOD리스트 전체 내용 조회를 Text으로 return 받을때의 테스트
	 * @throws Exception
	 */
	
	@Test
	public void getWishList_All_ToText() throws Exception {
		// 단말 API 호출
		String url = TestSmartUXProperties.getProperty("test.getWishList.URL");
		String param = "sa_id=209017787002&stb_mac=0002.1416.f8b6&app_type=I30&start_num=-1";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "");
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getWishList_All_ToText")), is(true));
	}
	
	/**
	 * 장르VOD리스트 Paging 조회를 Text로 return 받을때의 테스트
	 * @throws Exception
	 */
	@Test
	public void getWishList_Paging_ToText() throws Exception {
		// 단말 API 호출
		String url = TestSmartUXProperties.getProperty("test.getWishList.URL");
		String param = "sa_id=209017787002&stb_mac=0002.1416.f8b6&app_type=I30&start_num=1&req_count=3";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "");
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getWishList_Paging_ToText")), is(true));
	}	
	
	/**
	 * 장르VOD리스트 조회시 가입번호 파라미터(sa_id)를 빼고 호출했을때의 결과를 XML로 받을때의 테스트
	 * @throws Exception
	 */
	@Test
	public void getWishList_notSA_ID_ToXML() throws Exception{ 
		String url = TestSmartUXProperties.getProperty("test.getWishList.URL");
		String param = "stb_mac=0002.1416.f8b6&app_type=I30&start_num=0";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getWishList_notSA_ID_ToXML")), is(true));
	}
	
	/**
	 * 장르VOD리스트 조회시 가입번호 파라미터(sa_id)를 빼고 호출했을때의 결과를 JSON로 받을때의 테스트
	 * @throws Exception
	 */
	@Test
	public void getWishList_notSA_ID_ToJSON() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.getWishList.URL");
		String param = "stb_mac=0002.1416.f8b6&app_type=I30&start_num=0";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/json");
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getWishList_notSA_ID_ToJSON")), is(true));
	}
	
	/**
	 * 장르VOD리스트 조회시 가입번호 파라미터(sa_id)를 빼고 호출했을때의 결과를 Text로 받을때의 테스트
	 * @throws Exception
	 */
	@Test
	public void getWishList_notSA_ID_ToText() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.getWishList.URL");
		String param = "stb_mac=0002.1416.f8b6&app_type=I30&start_num=0";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "");
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getWishList_notSA_ID_ToText")), is(true));
	}
	
	
	/**
	 * 장르VOD리스트 조회시 맥주소 파라미터(stb_mac)를 빼고 호출했을때의 결과를 XML로 받을때의 테스트
	 * @throws Exception
	 */
	@Test
	public void getWishList_notSTB_MAC_ToXML() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.getWishList.URL");
		String param = "sa_id=209017787002&app_type=I30&start_num=0";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getWishList_notSTB_MAC_ToXML")), is(true));
	}
	
	/**
	 * 장르VOD리스트 조회시 맥주소 파라미터(stb_mac)를 빼고 호출했을때의 결과를 JSON로 받을때의 테스트
	 * @throws Exception
	 */
	@Test
	public void getWishList_notSTB_MACToJSON() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.getWishList.URL");
		String param = "sa_id=209017787002&app_type=I30&start_num=0";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/json");
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getWishList_notSTB_MACToJSON")), is(true));
	}
	
	
	/**
	 * 장르VOD리스트 조회시 맥주소 파라미터(stb_mac)를 빼고 호출했을때의 결과를 Text로 받을때의 테스트
	 * @throws Exception
	 */
	@Test
	public void getWishList_notSTB_MAC_ToText() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.getWishList.URL");
		String param = "sa_id=209017787002&app_type=I30&start_num=0";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "");
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getWishList_notSTB_MAC_ToText")), is(true));
	}
	
	/**
	 * 장르VOD리스트 조회시 어플타입 파라미터(app_type)를 빼고 호출했을때의 결과를 XML로 받을때의 테스트
	 * @throws Exception
	 */
	@Test
	public void getWishList_notAPP_TYPE_ToXML() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.getWishList.URL");
		String param = "sa_id=209017787002&stb_mac=0002.1416.f8b60&start_num=0";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");
		
		// 객체를 비교하여 테스트
			assertThat(result.equals(TestSmartUXProperties.getProperty("getWishList_notAPP_TYPE_ToXML")), is(true));
		}
	
	/**
	 * 장르VOD리스트 조회시 어플타입 파라미터(app_type)를 빼고 호출했을때의 결과를 JSON로 받을때의 테스트
	 * @throws Exception
	 */
	@Test
	public void getWishList_notAPP_TYPE_ToJSON() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.getWishList.URL");
		String param = "sa_id=209017787002&stb_mac=0002.1416.f8b60&start_num=0";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/json");
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getWishList_notAPP_TYPE_ToJSON")), is(true));
	}
	
	/**
	 * 장르VOD리스트 조회시 어플타입 파라미터(app_type)를 빼고 호출했을때의 결과를 Text로 받을때의 테스트
	 * @throws Exception
	 */
	@Test
	public void getWishList_notAPP_TYPE_ToText() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.getWishList.URL");
		String param = "sa_id=209017787002&stb_mac=0002.1416.f8b60&start_num=0";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "");
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getWishList_notAPP_TYPE_ToText")), is(true));
	}
		
	/**
	 * 장르VOD리스트 조회시 어플타입 파라미터(app_type)에 I30이 아닌 다른 값으로 호출했을때의 결과를 XML로 받을때의 테스트
	 * @throws Exception
	 */
	@Test
	public void getWishList_not_I30_APP_TYPE_ToXML() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.getWishList.URL");
		String param = "sa_id=209017787002&stb_mac=0002.1416.f8b60&app_type=AAA&start_num=0";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getWishList_not_I30_APP_TYPE_ToXML")), is(true));
	}

	/**
	 * 장르VOD리스트 조회시 어플타입 파라미터(app_type)에 I30이 아닌 다른 값으로 호출했을때의 결과를 JSON로 받을때의 테스트
	 * @throws Exception
	 */
	@Test
	public void getWishList_not_I30_APP_TYPE_ToJSON() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.getWishList.URL");
		String param = "sa_id=209017787002&stb_mac=0002.1416.f8b60&app_type=AAA&start_num=0";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/json");
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getWishList_not_I30_APP_TYPE_ToJSON")), is(true));
	}

	/**
	 * 장르VOD리스트 조회시 어플타입 파라미터(app_type)에 I30이 아닌 다른 값으로 호출했을때의 결과를 Text로 받을때의 테스트
	 * @throws Exception
	 */
	@Test
	public void getWishList_not_I30_APP_TYPE_ToText() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.getWishList.URL");
		String param = "sa_id=209017787002&stb_mac=0002.1416.f8b60&app_type=AAA&start_num=0";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "");
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getWishList_not_I30_APP_TYPE_ToText")), is(true));
	}
	
	/**
	 * 장르VOD리스트 조회시 검색 시작 인덱스 파라미터(start_num)을 빼고 호출했을때의 결과를 XML로 받을때의 테스트
	 * @throws Exception
	 */
	@Test
	public void getWishList_not_START_NUM_ToXML() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.getWishList.URL");
		String param = "sa_id=209017787002&stb_mac=0002.1416.f8b60&app_type=I30";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getWishList_not_START_NUM_ToXML")), is(true));
	}
	
	/**
	 * 장르VOD리스트 조회시 검색 시작 인덱스 파라미터(start_num)을 빼고 호출했을때의 결과를 JSON로 받을때의 테스트
	 * @throws Exception
	 */
	@Test
	public void getWishList_not_START_NUM_ToJSON() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.getWishList.URL");
		String param = "sa_id=209017787002&stb_mac=0002.1416.f8b60&app_type=I30";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/json");
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getWishList_not_START_NUM_ToJSON")), is(true));
	}
	
	/**
	 * 장르VOD리스트 조회시 검색 시작 인덱스 파라미터(start_num)을 빼고 호출했을때의 결과를 Text로 받을때의 테스트
	 * @throws Exception
	 */
	@Test
	public void getWishList_not_START_NUM_ToText() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.getWishList.URL");
		String param = "sa_id=209017787002&stb_mac=0002.1416.f8b60&app_type=I30";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "");
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getWishList_not_START_NUM_ToText")), is(true));
	}
	
	/**
	 * 장르VOD리스트 조회시 검색 시작 인덱스 파라미터(start_num)의 값을 -2 이하의 값으로 호출했을때의 결과를 XML로 받을때의 테스트
	 * @throws Exception
	 */
	@Test
	public void getWishList_Check_START_NUM_ToXML() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.getWishList.URL");
		String param = "sa_id=209017787002&stb_mac=0002.1416.f8b60&app_type=I30&start_num=-2";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getWishList_Check_START_NUM_ToXML")), is(true));
	}
	
	/**
	 * 장르VOD리스트 조회시 검색 시작 인덱스 파라미터(start_num)의 값을 -2 이하의 값으로 호출했을때의 결과를 JSON로 받을때의 테스트
	 * @throws Exception
	 */
	@Test
	public void getWishList_Check_START_NUM_ToJSON() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.getWishList.URL");
		String param = "sa_id=209017787002&stb_mac=0002.1416.f8b60&app_type=I30&start_num=-2";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/json");
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getWishList_Check_START_NUM_ToJSON")), is(true));
	}
	
	/**
	 * 장르VOD리스트 조회시 검색 시작 인덱스 파라미터(start_num)의 값을 -2 이하의 값으로 호출했을때의 결과를 Text로 받을때의 테스트
	 * @throws Exception
	 */
	@Test
	public void getWishList_Check_START_NUM_ToText() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.getWishList.URL");
		String param = "sa_id=209017787002&stb_mac=0002.1416.f8b60&app_type=I30&start_num=-2";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "");
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getWishList_Check_START_NUM_ToText")), is(true));
	}

	/**
	 * 장르VOD리스트 조회시 검색 레코드 개수 파라미터(req_count)을 빼고 호출했을때의 결과를 XML로 받을때의 테스트
	 * @throws Exception
	 */
	@Test
	public void getWishList_not_REQ_COUNT_ToXML() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.getWishList.URL");
		String param = "sa_id=209017787002&stb_mac=0002.1416.f8b60&app_type=I30&start_num=1";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getWishList_not_REQ_COUNT_ToXML")), is(true));
	}
	
	/**
	 * 장르VOD리스트 조회시 검색 레코드 개수 파라미터(req_count)을 빼고 호출했을때의 결과를 JSON로 받을때의 테스트
	 * @throws Exception
	 */
	@Test
	public void getWishList_not_REQ_COUNT_ToJSON() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.getWishList.URL");
		String param = "sa_id=209017787002&stb_mac=0002.1416.f8b60&app_type=I30&start_num=1";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/json");
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getWishList_not_REQ_COUNT_ToJSON")), is(true));
	}
	
	/**
	 * 장르VOD리스트 조회시 검색 레코드 개수 파라미터(req_count)을 빼고 호출했을때의 결과를 Text로 받을때의 테스트
	 * @throws Exception
	 */
	@Test
	public void getWishList_not_REQ_COUNT_ToText() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.getWishList.URL");
		String param = "sa_id=209017787002&stb_mac=0002.1416.f8b60&app_type=I30&start_num=1";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "");
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getWishList_not_REQ_COUNT_ToText")), is(true));
	}
	
	/**
	 * 장르VOD리스트 조회시 검색 레코드 개수 파라미터(req_count)의 값이 0 이하의 값으로 호출했을때의 결과를 XML로 받을때의 테스트
	 * @throws Exception
	 */
	@Test
	public void getWishList_under_Zero_REQ_COUNT_ToXML() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.getWishList.URL");
		String param = "sa_id=209017787002&stb_mac=0002.1416.f8b60&app_type=I30&start_num=1&req_count=-5";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getWishList_under_Zero_REQ_COUNT_ToXML")), is(true));
	}
	
	/**
	 * 장르VOD리스트 조회시 검색 레코드 개수 파라미터(req_count)의 값이 0 이하의 값으로 호출했을때의 결과를 JSON로 받을때의 테스트
	 * @throws Exception
	 */
	@Test
	public void getWishList_under_Zero_REQ_COUNT_ToJSON() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.getWishList.URL");
		String param = "sa_id=209017787002&stb_mac=0002.1416.f8b60&app_type=I30&start_num=1&req_count=-5";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/json");
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getWishList_under_Zero_REQ_COUNT_ToJSON")), is(true));
	}
	
	/**
	 * 장르VOD리스트 조회시 검색 레코드 개수 파라미터(req_count)의 값이 0 이하의 값으로 호출했을때의 결과를 Text로 받을때의 테스트
	 * @throws Exception
	 */
	@Test
	public void getWishList_under_Zero_REQ_COUNT_ToText() throws Exception{
		String url = TestSmartUXProperties.getProperty("test.getWishList.URL");
		String param = "sa_id=209017787002&stb_mac=0002.1416.f8b60&app_type=I30&start_num=1&req_count=-5";
		String result = TestGlobalCom.callHttpClient(host, port, url, param, timeout, "");
		
		// 객체를 비교하여 테스트
		assertThat(result.equals(TestSmartUXProperties.getProperty("getWishList_under_Zero_REQ_COUNT_ToText")), is(true));
	}			

	
	@AfterClass
	public static void afterClass() throws Exception {
		// 테스트에서 사용한 로그 파일을 닫는다
		TestGlobalCom.testLogClose();
		
		// 테스트에서 사용한 테이블을 정리한다.
		String url = TestSmartUXProperties.getProperty("test.queryURL");
		String param = "query_id=test_wishlist.delete_test_wishlist_data&type=delete";
		TestGlobalCom.callHttpClient(host, port, url, param, timeout);
		
		param = "query_id=test_wishlist.insert_wishlist_to_org_table&type=insert";
		TestGlobalCom.callHttpClient(host, port, url, param, timeout);
		
		param = "query_id=test_wishlist.drop_wishlist_backup_table&type=drop";
		TestGlobalCom.callHttpClient(host, port, url, param, timeout);
		
		// 캐쉬 작업
		//host = TestSmartUXProperties.getProperty("test.host");
		//port = Integer.parseInt(TestSmartUXProperties.getProperty("test.port"));
		//url = TestSmartUXProperties.getProperty("test.getWishList.URL");
		//param = "sa_id=209017787002&stb_mac=0002.1416.f8b6&app_type=I30&callByScheduler=A";
		//TestGlobalCom.callHttpClient(host, port, url, param, timeout, "application/xml");				
	}
	
	
}


