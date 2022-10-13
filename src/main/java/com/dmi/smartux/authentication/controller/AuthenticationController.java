package com.dmi.smartux.authentication.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dmi.smartux.authentication.service.AuthenticationService;
import com.dmi.smartux.authentication.vo.AuthenticationCommon;
import com.dmi.smartux.authentication.vo.AuthenticationVO;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.GlobalCom;

@Controller
public class AuthenticationController {
	
	private final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	AuthenticationService service;
	
	/**
	 * OGW 서비스에서 호출해주는 로직
	 * (현재 크로스 풀렛폼에서 호출은 받을수 있는 상황이 될 수 없다. DB 데이터 조작을 OGW에서만 하고 있다)
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/authentication", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> setAuthentication(HttpServletRequest request) throws Exception {
		
		StringBuilder sb = new StringBuilder();
		BufferedReader br= new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8")); 
		String str;
		while ((str = br.readLine()) != null) {
            sb.append(str);
        }
		
		String use = "";
		
		String key_type = "";
		String service_id = "";
		String api_id = "";
		String job = "";
		String access_key = "";
		String service_life = "";
		String cp_id = "";
		String sessionKey = "";
		
		String resultcode = "0000";
		String resultmessage = "success";
		
		try{
			JSONObject json = JSONObject.fromObject(sb.toString());
			logger.debug(json.get("param"));
			
			JSONObject json_sub = JSONObject.fromObject(json.get("param"));
			job = json_sub.getString("JOB");
			key_type = json_sub.getString("KEY_TYPE");
			service_id = json_sub.getString("SERVICE_ID");
			api_id = json_sub.getString("API_ID");
			
			logger.debug(json_sub.get("params"));
			if(json_sub.get("params")!=null && !json_sub.get("params").equals("")){
				JSONArray jsonArr_sub_lev2 = JSONArray.fromObject(json_sub.get("params"));
				for(int y = 0; y < jsonArr_sub_lev2.size(); y++) {
					net.sf.json.JSONObject subObject_lev2 = jsonArr_sub_lev2.getJSONObject(y);
					if(subObject_lev2.getString("name").equals("ACCESS_KEY")){
						access_key = subObject_lev2.getString("value");
					}else if(subObject_lev2.getString("name").equals("SERVICE_LIFE")){
						service_life = subObject_lev2.getString("value");
					}else if(subObject_lev2.getString("name").equals("CP_ID")){
						cp_id = subObject_lev2.getString("value");
					}else if(subObject_lev2.getString("name").equals("SESS_KEY")){
						sessionKey = subObject_lev2.getString("value");
					}
				}
			}
			
//			HTMLCleaner cleaner = new HTMLCleaner();
//			service_id = cleaner.clean(service_id);
//			uri = cleaner.clean(uri);
//			access_key = cleaner.clean(access_key);
//			key_type = cleaner.clean(key_type);
//			cp_id = cleaner.clean(cp_id);
//			updateType = cleaner.clean(updateType);
//			sessionKey = cleaner.clean(sessionKey);
//			service_life = cleaner.clean(service_life);
			
			String mapp_uri = "";
			if(api_id!=null && !api_id.equals("")){
				if(api_id.indexOf("/") <= 0){
					mapp_uri = api_id;
				}else{
					mapp_uri = api_id.substring(api_id.indexOf("/"), api_id.length());
				}
			}
			
			String mapp_method = "";
			if(api_id!=null && !api_id.equals("")){
				int startNum = api_id.indexOf("[");
				int endNum = api_id.indexOf("]");
				
				if(startNum==-1 && endNum==-1){
					mapp_method = "";
				}else{
					mapp_method = api_id.substring(startNum+1, endNum);
				}
			}
			
			String mapp_system = "";
			if(api_id!=null && !api_id.equals("")){
				int startNum = api_id.indexOf("]");
				int endNum = api_id.indexOf("/");
				
				if(startNum==-1 && endNum==-1){
					mapp_system = "";
				}else{
					mapp_system = api_id.substring(startNum+1, endNum);
				}
			}
			
			if(job.toUpperCase().equals("USE") || job.toUpperCase().equals("REGISTER")){
				use = "Y";//사용
			}else if(job.toUpperCase().equals("NOT_USE")){
				use = "N";//미사용
			}else{
				use = "";
			}
			
			AuthenticationVO data = new AuthenticationVO();
			data.setAccess_key(access_key);
			data.setApi_id(mapp_uri);
			data.setCp_id(cp_id);
			data.setKey_type(key_type);
			data.setService_id(service_id);
			data.setService_life(service_life);
			data.setSessionKey(sessionKey);
			data.setUse(use);
			data.setMethod(mapp_method);
			
			String mapKey = access_key;
			
			AuthenticationVO checkData = null;
			try{
				checkData = AuthenticationCommon.AUTH_MAP.get(mapKey);
			}catch(Exception e){
				checkData = null;
			}
			
			//업데이트
//			if("USE".equals(job.toUpperCase()) || "NOT_USE".equals(job.toUpperCase())){
//
//				//캐싱 구조 깨짐으로 인하여 전체 캐싱 리로드
//				if(checkData == null){
//					authsync();
//				//삭제 후 재저장
//				}else{
//					service.setAuthUpdate(mapKey,data);
//				}
//			//삭제
//			}else if("DELETE".equals(job.toUpperCase())){
//				//캐싱 구조 깨짐으로 인하여 전체 캐싱 리로드
//				if(checkData == null){
//					authsync();
//				//삭제
//				}else{
//					service.setAuthDelete(mapKey);
//				}
//			//등록
//			}else if("REGISTER".equals(job.toUpperCase())){
//				//저장
//				if(checkData == null){
//					service.setAuthInsert(mapKey,data);
//				//캐싱 구조 깨짐으로 인하여 전체 캐싱 리로드
//				}else{
//					authsync();
//				}
//			}else{
//				resultcode = "9999";
//				resultmessage = "datavalidation";
//			}
			
			logger.debug("=================전송==========================");
			//싱크
			serverMapSyncCall();
			logger.debug("=================완료==========================");
			
		}catch(JSONException e){
			logger.debug("["+e.getClass().getName()+"]["+e.getMessage()+"]");
			resultcode = "9999";
			resultmessage = "parsingerror";
		}catch(Exception e){
			logger.debug("["+e.getClass().getName()+"]["+e.getMessage()+"]");
			resultcode = "9999";
			resultmessage = "etcError";
		}
		
		
		
		HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
        
        String result = "{\"param\":{\"params\":[{\"name\":\"RT\",\"value\":\""+resultcode+"\"},{\"name\":\"RT_MSG\",\"value\":\""+resultmessage+"\"},{\"name\":\"SESS_KEY\",\"value\":\""+sessionKey+"\"}]}}"; 
        
        return new ResponseEntity<String>(result, responseHeaders, HttpStatus.CREATED);
	}
	
	/**
	 * 서버간 데이터 싱크를 맞추기 위하여 지정한 URL을 호출하는 메서드
	 * @throws Exception
	 */
	private void serverMapSyncCall() throws Exception{
		int timeout = Integer.parseInt(SmartUXProperties.getProperty("authentication.timeout"));		// timeout 값은 스케쥴러것을 사용
		int retrycnt = Integer.parseInt(SmartUXProperties.getProperty("authentication.retrycnt"));	// 재시도 횟수는 스케줄러 것을 사용
		String url = SmartUXProperties.getProperty("authentication.url");
		String protocolName = SmartUXProperties.getProperty("authentication.protocol");
		String param = "";
		
		GlobalCom.syncServerCache(url, param, timeout, retrycnt, protocolName); 			// 다른 서버의 캐쉬 동기화 작업 진행
	}
	
	/**
	 * DB를 다시 조회하여 맵을 초기화하고 새로 데이터를 삽입하여 싱크를 맞추는 메서드
	 * @return
	 */
	@RequestMapping(value="/authsync", method=RequestMethod.GET)
	public @ResponseBody String authsync(){
		
		String resultcode = "0000";
		
		logger.debug("======변경 전 START=========");
		logMap();
		logger.debug("======변경 전 END=========");
		
		try{
			logger.debug("======DB 조회=========");
			//service.setAuthDataReload(AuthenticationCommon.SYSTEM_CODE);
			service.setAuthDataReload();
			logger.debug("======DB 조회 끝=========");
		}catch (Exception e) {
			logger.debug("[authsync]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			resultcode = "9999";
		}
		
		logger.debug("======변경 후 START=========");
		logMap();
		logger.debug("======변경 후 END=========");
		
		return resultcode;
	}
	
	/**
	 * 맵 정보 확인하기 위한 메서드 
	 */
	public void logMap(){
		Set s=AuthenticationCommon.AUTH_MAP.entrySet();

		Iterator it=s.iterator();
		while(it.hasNext()){
			Map.Entry m =(Map.Entry)it.next();
			
			String key=(String)m.getKey();
			AuthenticationVO value=(AuthenticationVO)m.getValue();
			
			logger.debug("AuthenticationCommon.AUTH_MAP Key["+key+"] key_type["+value.getKey_type()+"] api_id["+value.getApi_id()+"] use["+value.getUse()+"] date["+value.getService_life()+"]");
		}
	}
	
	@RequestMapping(value="/test", method=RequestMethod.GET)
	public @ResponseBody String jsonSendTransfer(){ 
		
		String response ="";
		
		try{
        DefaultHttpClient httpClient = new DefaultHttpClient(); 

        String jsonString = "{\"param\":{\"JOB\":\"REGISTER\",\"KEY_TYPE\":\"T\",\"SERVICE_ID\":\"key0050\",\"API_ID\":\"/vod/bestvod\",\"params\":[{\"name\":\"ACCESS_KEY\",\"value\":\"acckey0050\"},{\"name\":\"SERVICE_LIFE\",\"value\":\"20.10.01\"},{\"name\":\"CP_ID\",\"value\":\"cp0050\"},{\"name\":\"SESS_KEY\",\"value\":\"sesskey0050\"}]}	}";
        //String jsonString = "{\"param\":{\"JOB\":\"USE\",\"KEY_TYPE\":\"T\",\"SERVICE_ID\":\"key0050\",\"API_ID\":\"/vod/bestvod\",\"params\":[{\"name\":\"SERVICE_LIFE\",\"value\":\"YY.MM.DD\"},{\"name\":\"SESS_KEY\",\"value\":\"sess_key0011\"}]}	}";
        //String jsonString = "{\"param\":{\"JOB\":\"NOT_USE\",\"KEY_TYPE\":\"T\",\"SERVICE_ID\":\"key0050\",\"API_ID\":\"/vod/bestvod\",\"params\":[{\"name\":\"SERVICE_LIFE\",\"value\":\"YY.MM.DD\"},{\"name\":\"SESS_KEY\",\"value\":\"sess_key0011\"}]}	}";
        //String jsonString = "{\"param\":{\"JOB\":\"DELETE\",\"KEY_TYPE\":\"T\",\"SERVICE_ID\":\"key0050\",\"API_ID\":\"/vod/bestvod\",\"params\":[{\"name\":\"SESS_KEY\",\"value\":\"accc012\"}]}	}";
        String url = "http://localhost/smartuxs/authentication";
        
        HttpPost post = new HttpPost(url); 
        post.setHeader("Content-Type", "application/json"); 
        post.setEntity(new StringEntity(jsonString,"UTF-8")); 

        ResponseHandler<String> responseHandler = new BasicResponseHandler(); 
        response = httpClient.execute(post,responseHandler); 
        
		}catch(Exception e){
			
		}
		logger.debug("요청 response="+response);
		return response;
    }
	
	
	
}
