package com.dmi.smartux.mainpanel.controller;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dmi.smartux.authentication.vo.AuthenticationCommon;
import com.dmi.smartux.common.exception.ExceptionHandler;
import com.dmi.smartux.common.exception.SmartUXException;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.CharacterSet;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.vo.Result;
import com.dmi.smartux.mainpanel.service.MainPanelService;
import com.dmi.smartux.mainpanel.vo.AlbumInfoVO;
import com.dmi.smartux.mainpanel.vo.MainPanelInfoVO;
import com.dmi.smartux.mainpanel.vo.MainPanelResult;
import com.dmi.smartux.mainpanel.vo.MainPanelVersionInfoVO;

@Controller
public class MainPanelController {

	private final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	MainPanelService service;
	
	@org.springframework.web.bind.annotation.ExceptionHandler(SmartUXException.class)
	//@ResponseStatus(value=HttpStatus.NOT_FOUND)
	public @ResponseBody String handleException(SmartUXException e, HttpServletRequest request,HttpServletResponse response) throws SmartUXException {
		//System.out.println("TEST = "+request.getHeader("accept"));
		//System.out.println("Handler : " + e.getMessage());
		
		//HttpHeaders rh = new HttpHeaders();
		//rh.add("Content-Type", "text/html; charset=UTF-8");
		if(request.getHeader("accept") == null ){
			response.setHeader("content-type", "text/plain;charset=UTF-8");
			StringBuffer sb = new StringBuffer();
			sb.append(e.getFlag());
			sb.append(GlobalCom.colsep);
			sb.append(CharacterSet.toKorean(e.getMessage()));
			String result;
			result = sb.toString();
			return result;
		}else{
			if(request.getHeader("accept").indexOf("application/json") != -1){
				response.setHeader("content-type", "application/json;charset=UTF-8");
				String result = "{\"error\":{\"code\":\""+e.getFlag()+"\",\"message\":\""+CharacterSet.toKorean(e.getMessage())+"\"}}";
				return result;
			}else if(request.getHeader("accept").indexOf("application/xml") != -1){
				response.setHeader("content-type", "application/xml;charset=UTF-8");
				String result;
				result = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><error><code>"+e.getFlag()+"</code><message>"+CharacterSet.toKorean(e.getMessage())+"</message></error>";
				return result;
			}else{
				response.setHeader("content-type", "text/plain;charset=UTF-8");
				StringBuffer sb = new StringBuffer();
				sb.append(e.getFlag());
				sb.append(GlobalCom.colsep);
				sb.append(CharacterSet.toKorean(e.getMessage()));
				String result;
				result = sb.toString();
				return result;
			}
		}
	}
	
	/**
	 * Main Panel 버전 정보를 조회
	 * @param sa_id			가입번호
	 * @param stb_mac		맥주소
	 * @param app_type		어플타입(UX : 셋탑, SMA : 스마트폰)
	 * @param callByScheduler	단말에서 호출 했을 경우엔 N, 스케쥴러에서 호출했을땐 Y, 관리자툴에서 조회했을땐 A가 들어간다
	 * @return
	 */
	@RequestMapping(value="/getMainPanelVersionInfo")
	public MainPanelVersionInfoVO API_getMainPanelVersionInfo(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value="sa_id", required=false) String sa_id,
			@RequestParam(value="stb_mac", required=false) String stb_mac,
			@RequestParam(value="app_type", required=false) String app_type,
			@RequestParam(value="callByScheduler", required=false, defaultValue="N") String callByScheduler){
		
		//#########[LOG START]#########
		Log log_logger = LogFactory.getLog("getMainPanelVersionInfo");
		long startTime = System.currentTimeMillis();
		String log_ip = request.getRemoteAddr();
		String log_url = request.getRequestURI();
		Enumeration log_penum = request.getParameterNames();
		String log_key = null;
		String log_value = null;
		String log_info_mac = "";
		String log_info_id = "";
		String log_info_app_type = "";
		String log_debug = "";

		while(log_penum.hasMoreElements()){
			log_key = (String)log_penum.nextElement();
			log_value = (new String(request.getParameter(log_key)) == null) ? "" : new String(request.getParameter(log_key));
			
			if(log_key.equalsIgnoreCase("stb_mac")){
				log_info_mac = log_value;
			}
			if(log_key.equalsIgnoreCase("sa_id")){
				log_info_id = log_value;
			}
			if(log_key.equalsIgnoreCase("app_type")){
				log_info_app_type = log_value;
			}
			log_debug += " ["+log_key+"="+log_value+"]";
		}

		log_logger.info("["+log_ip+"] ["+log_url+"][START] - ["+log_info_mac+"]["+log_info_id+"]["+log_info_app_type+"]");
		log_logger.debug("["+log_ip+"] ["+log_url+"][START] -"+log_debug);

		
		MainPanelVersionInfoVO result = new MainPanelVersionInfoVO();
		
		try{
			if("N".equals(callByScheduler)){		// 단말에서 호출한 경우
				validateGetMainPanelVersionInfo(sa_id, stb_mac, app_type);
				// 검색된 Main Panel 버전 정보 셋팅
				String version = service.getMainPanelVersionInfo(sa_id, stb_mac, app_type, callByScheduler);
				result.setFlag(SmartUXProperties.getProperty("flag.success"));
				result.setMessage(SmartUXProperties.getProperty("message.success"));
				result.setVersion(version);
			
			}else if("A".equals(callByScheduler)){
				String version = service.getMainPanelVersionInfo("", "", "", callByScheduler);
				result.setFlag(SmartUXProperties.getProperty("flag.success"));
				result.setMessage(SmartUXProperties.getProperty("message.success"));
				result.setVersion(version);
				//response.setStatus(201);
			}else{
				String version = service.getMainPanelVersionInfo("", "", "", callByScheduler);
				result.setFlag(SmartUXProperties.getProperty("flag.success"));
				result.setMessage(SmartUXProperties.getProperty("message.success"));
				result.setVersion(version);
				//response.setStatus(201);
			}
		}catch(Exception e){
			log_logger.error("["+log_ip+"] ["+log_url+"]["+log_info_mac+"]["+log_info_id+"]["+log_info_app_type+"]"+"[getMainPanelVersionInfo]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			ExceptionHandler handler = new ExceptionHandler(e);
			result.setFlag(handler.getFlag());
			result.setMessage(handler.getMessage());
		}
		
		//#########[LOG END]#########
		long endTime = System.currentTimeMillis();
		String timeStr = Long.toString((endTime-startTime));
		if(timeStr.length() > 3){
			timeStr = timeStr.substring(0,timeStr.length()-3)+"."+timeStr.substring(timeStr.length()-3,timeStr.length());
		}else{
			for(int i=timeStr.length();i<3;i++){
				timeStr = "0"+timeStr;	
			}
			timeStr = "0."+timeStr;
		}
		log_logger.info("["+log_ip+"] ["+log_url+"][END]["+(timeStr)+" sec] - ["+log_info_mac+"]["+log_info_id+"]["+log_info_app_type+"] ["+result.getFlag()+"]");


				
		return result; 
	}
	
//	//@RequestMapping(value="/PanelVersion/sa_id/{sa_id}/stb_mac/{stb_mac}/app_type/{app_type}", method=RequestMethod.GET)
//	@RequestMapping(value="/v1/mainpanel/version/**", method=RequestMethod.GET)
//	public MainPanelVersionInfoVO Restful_getMainPanelVersionInfo(
//			HttpServletRequest request,
//			HttpServletResponse response
////			@PathVariable String sa_id,
////			@PathVariable String stb_mac,
////			@PathVariable String app_type
//		){
//		
//		//#########################[LOG SET]##########################
//		Log log_logger = LogFactory.getLog("OPENAPI_PanelVersion");
//		long startTime = System.currentTimeMillis();
//		String ip = request.getRemoteAddr();
//		String url = request.getRequestURI();
//		try{
//			url = "/v1/mainpanel/version/";
//		}catch(Exception e){
//		}
//		String method = request.getMethod();
//		//#########################[//LOG SET]##########################
//		
//		String sa_id = "";
//		String stb_mac = "";
//		String app_type = "";
//		
//		String access_key = null;
//		String cp_id = null;
//		
//		MainPanelVersionInfoVO result = new MainPanelVersionInfoVO();
//
//		String uri = request.getRequestURI();
//		try {
//			HashMap<String, String> paramMap = GlobalCom.getRestfulParamMap(5,uri);
//			sa_id 		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "sa_id"));
//			stb_mac		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "stb_mac"));
//			app_type 	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "app_type"));
//		
//			access_key = GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "access_key"));
//			cp_id = GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "cp_id"));
//		
//			
//			//#########################[LOG START]##########################
//			log_logger.info("["+ip+"] ["+url+"]["+method+"][START] - ["+stb_mac+"]["+sa_id+"]["+app_type+"]["+access_key+"]["+cp_id+"]");
//			//#########################[//LOG START]########################
//			
//	
//			//단말에서만 OPEN_API 사용
//			String callByScheduler ="N";
//			
//			//access_key 인증 허용 체크
//			String authResult = GlobalCom.isNull(AuthenticationCommon.isAuthenticationMap(access_key,cp_id,"GET","/v1/mainpanel/version"));
//			if(!authResult.equals("SUCCESS")){
//				SmartUXException exception = new SmartUXException();
//
//				if(authResult.equals("ACCESS_KEY")){
//					exception.setFlag(SmartUXProperties.getProperty("flag.authentication.accessKey"));
//					exception.setMessage(SmartUXProperties.getProperty("message.authentication.accessKey"));
//				}else if(authResult.equals("CP_ID")){
//					exception.setFlag(SmartUXProperties.getProperty("flag.authentication.cp_id"));
//					exception.setMessage(SmartUXProperties.getProperty("message.authentication.cp_id"));
//				}else if(authResult.equals("EXDATE")){
//					exception.setFlag(SmartUXProperties.getProperty("flag.authentication.exdate"));
//					exception.setMessage(SmartUXProperties.getProperty("message.authentication.exdate"));
//				}else if(authResult.equals("URL")){
//					exception.setFlag(SmartUXProperties.getProperty("flag.authentication.url"));
//					exception.setMessage(SmartUXProperties.getProperty("message.authentication.url"));
//				}else if(authResult.equals("FAIL")){
//					exception.setFlag(SmartUXProperties.getProperty("flag.authentication.fail"));
//					exception.setMessage(SmartUXProperties.getProperty("message.authentication.fail"));
//				}
//				
//				throw exception;
//			}
//		
//
//			if("N".equals(callByScheduler)){		// 단말에서 호출한 경우
//				validateGetMainPanelVersionInfo(sa_id, stb_mac, app_type);
//				// 검색된 Main Panel 버전 정보 셋팅
//				String version = service.getMainPanelVersionInfo(sa_id, stb_mac, app_type, callByScheduler);
//				result.setFlag(SmartUXProperties.getProperty("flag.success"));
//				result.setMessage(SmartUXProperties.getProperty("message.success"));
//				result.setVersion(version);
//			
//			}else if("A".equals(callByScheduler)){
//				String version = service.getMainPanelVersionInfo("", "", "", callByScheduler);
//				result.setFlag(SmartUXProperties.getProperty("flag.success"));
//				result.setMessage(SmartUXProperties.getProperty("message.success"));
//				result.setVersion(version);
//				//response.setStatus(201);
//			}else{
//				String version = service.getMainPanelVersionInfo("", "", "", callByScheduler);
//				result.setFlag(SmartUXProperties.getProperty("flag.success"));
//				result.setMessage(SmartUXProperties.getProperty("message.success"));
//				result.setVersion(version);
//				//response.setStatus(201);
//			}
//		}catch(Exception e){
//			log_logger.error("["+ip+"] ["+url+"]["+stb_mac+"]["+sa_id+"]["+app_type+"]["+access_key+"]["+cp_id+"]["+e.getClass().getName()+"]["+e.getMessage()+"]");
//			ExceptionHandler handler = new ExceptionHandler(e);
//			result.setFlag(handler.getFlag());
//			result.setMessage(handler.getMessage());
//		}
//		
//		//############################[LOG END]############################
//		long endTime = System.currentTimeMillis();
//		String timeStr = Long.toString((endTime-startTime));
//		if(timeStr.length() > 3){
//			timeStr = timeStr.substring(0,timeStr.length()-3)+"."+timeStr.substring(timeStr.length()-3,timeStr.length());
//		}else{
//			for(int i=timeStr.length();i<3;i++){
//				timeStr = "0"+timeStr;	
//			}
//			timeStr = "0."+timeStr;
//		}
//		log_logger.info("["+ip+"] ["+url+"]["+method+"][END] ["+(timeStr)+" sec] - ["+stb_mac+"]["+sa_id+"]["+app_type+"]["+access_key+"]["+cp_id+"] ["+result.getFlag()+"]");
//		//############################[//LOG END]##########################
//		
//		return result; 
//	}
	
	@RequestMapping(value="/v1/mainpanel/version", method=RequestMethod.GET)
	public MainPanelVersionInfoVO Restful_getMainPanelVersionInfo(
			HttpServletRequest request
			,HttpServletResponse response
			,@RequestParam(value="sa_id", required=false) String sa_id
			,@RequestParam(value="stb_mac", required=false) String stb_mac
			,@RequestParam(value="app_type", required=false) String app_type
			,@RequestParam(value="access_key", required=false, defaultValue="") String access_key
			,@RequestParam(value="cp_id", required=false, defaultValue="") String cp_id
		) throws Exception{
		
		SmartUXException exception = new SmartUXException();
		
		//#########################[LOG SET]##########################
		Log log_logger = LogFactory.getLog("OPENAPI_PanelVersion");
		long startTime = System.currentTimeMillis();
		String ip = request.getRemoteAddr();
		String url = request.getRequestURI();
		try{
			url = "/v1/mainpanel/version/";
		}catch(Exception e){
		}
		String method = request.getMethod();
		//#########################[//LOG SET]##########################
		
//		String sa_id = "";
//		String stb_mac = "";
//		String app_type = "";
//		
//		String access_key = null;
//		String cp_id = null;
		
		MainPanelVersionInfoVO result = new MainPanelVersionInfoVO();

		String uri = request.getRequestURI();
		try {
//			HashMap<String, String> paramMap = GlobalCom.getRestfulParamMap(5,uri);
//			sa_id 		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "sa_id"));
//			stb_mac		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "stb_mac"));
//			app_type 	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "app_type"));
//		
//			access_key = GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "access_key"));
//			cp_id = GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "cp_id"));
		
			
			//#########################[LOG START]##########################
			log_logger.info("["+ip+"] ["+url+"]["+method+"][START] - ["+stb_mac+"]["+sa_id+"]["+app_type+"]["+access_key+"]["+cp_id+"]");
			//#########################[//LOG START]########################
			
	
			//단말에서만 OPEN_API 사용
			String callByScheduler ="N";
			
			//access_key 인증 허용 체크
			String authResult = GlobalCom.isNull(AuthenticationCommon.isAuthenticationMap(access_key,cp_id,"GET","/v1/mainpanel/version"));
			if(!authResult.equals("SUCCESS")){
				//SmartUXException exception = new SmartUXException();

				if(authResult.equals("ACCESS_KEY")){
					exception.setFlag(SmartUXProperties.getProperty("flag.authentication.accessKey"));
					exception.setMessage(SmartUXProperties.getProperty("message.authentication.accessKey"));
				}else if(authResult.equals("CP_ID")){
					exception.setFlag(SmartUXProperties.getProperty("flag.authentication.cp_id"));
					exception.setMessage(SmartUXProperties.getProperty("message.authentication.cp_id"));
				}else if(authResult.equals("EXDATE")){
					exception.setFlag(SmartUXProperties.getProperty("flag.authentication.exdate"));
					exception.setMessage(SmartUXProperties.getProperty("message.authentication.exdate"));
				}else if(authResult.equals("URL")){
					exception.setFlag(SmartUXProperties.getProperty("flag.authentication.url"));
					exception.setMessage(SmartUXProperties.getProperty("message.authentication.url"));
				}else if(authResult.equals("FAIL")){
					exception.setFlag(SmartUXProperties.getProperty("flag.authentication.fail"));
					exception.setMessage(SmartUXProperties.getProperty("message.authentication.fail"));
				}
				
				response.setStatus(401);
				
				throw exception;
			}
		

			if("N".equals(callByScheduler)){		// 단말에서 호출한 경우
				validateGetMainPanelVersionInfo(sa_id, stb_mac, app_type);
				// 검색된 Main Panel 버전 정보 셋팅
				String version = service.getMainPanelVersionInfo(sa_id, stb_mac, app_type, callByScheduler);
				result.setFlag(SmartUXProperties.getProperty("flag.success"));
				result.setMessage(SmartUXProperties.getProperty("message.success"));
				result.setVersion(version);
			
			}else if("A".equals(callByScheduler)){
				String version = service.getMainPanelVersionInfo("", "", "", callByScheduler);
				result.setFlag(SmartUXProperties.getProperty("flag.success"));
				result.setMessage(SmartUXProperties.getProperty("message.success"));
				result.setVersion(version);
				//response.setStatus(201);
			}else{
				String version = service.getMainPanelVersionInfo("", "", "", callByScheduler);
				result.setFlag(SmartUXProperties.getProperty("flag.success"));
				result.setMessage(SmartUXProperties.getProperty("message.success"));
				result.setVersion(version);
				//response.setStatus(201);
			}
		} catch(SmartUXException e){
			log_logger.error("["+ip+"] ["+url+"]["+stb_mac+"]["+sa_id+"]["+app_type+"]["+access_key+"]["+cp_id+"]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			throw e;
			
		}catch(Exception e){
			log_logger.error("["+ip+"] ["+url+"]["+stb_mac+"]["+sa_id+"]["+app_type+"]["+access_key+"]["+cp_id+"]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			ExceptionHandler handler = new ExceptionHandler(e);
			result.setFlag(handler.getFlag());
			result.setMessage(handler.getMessage());
		}
		
		//############################[LOG END]############################
		long endTime = System.currentTimeMillis();
		String timeStr = Long.toString((endTime-startTime));
		if(timeStr.length() > 3){
			timeStr = timeStr.substring(0,timeStr.length()-3)+"."+timeStr.substring(timeStr.length()-3,timeStr.length());
		}else{
			for(int i=timeStr.length();i<3;i++){
				timeStr = "0"+timeStr;	
			}
			timeStr = "0."+timeStr;
		}
		log_logger.info("["+ip+"] ["+url+"]["+method+"][END] ["+(timeStr)+" sec] - ["+stb_mac+"]["+sa_id+"]["+app_type+"]["+access_key+"]["+cp_id+"] ["+result.getFlag()+"]");
		//############################[//LOG END]##########################
		
		return result; 
	}

	/**
	 * Main Panel 연동 정보를 조회
	 * @param sa_id			가입번호
	 * @param stb_mac		맥주소
	 * @param app_type		어플타입(UX : 셋탑, SMA : 스마트폰)
	 * @param pannel_id		특정 패널의 값을 읽을려고 할때 사용하는 패널 ID(ex:CP01), 이 변수명은 단말에 알려주지 말것(연동정의서엔 특정 패널 조회가 없다)
	 * @param callByScheduler	단말에서 호출 했을 경우엔 N, 스케쥴러에서 호출했을땐 Y, 관리자툴에서 조회했을땐 A가 들어간다
	 * @return
	 */
	@RequestMapping(value="/getMainPanelInterlockingInfo")
	public MainPanelResult API_getMainPanelInterlockingInfo(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value="sa_id", required=false) String sa_id,
			@RequestParam(value="stb_mac", required=false) String stb_mac,
			@RequestParam(value="app_type", required=false) String app_type,
			@RequestParam(value="panel_id", required=false, defaultValue="") String panel_id,
			@RequestParam(value="callByScheduler", required=false, defaultValue="N") String callByScheduler){
		
		//#########[LOG START]#########
		Log log_logger = LogFactory.getLog("getMainPanelInterlockingInfo");
		long startTime = System.currentTimeMillis();
		String log_ip = request.getRemoteAddr();
		String log_url = request.getRequestURI();
		Enumeration log_penum = request.getParameterNames();
		String log_key = null;
		String log_value = null;
		String log_info_mac = "";
		String log_info_id = "";
		String log_info_app_type = "";
		String log_debug = "";

		while(log_penum.hasMoreElements()){
			log_key = (String)log_penum.nextElement();
			log_value = (new String(request.getParameter(log_key)) == null) ? "" : new String(request.getParameter(log_key));
			
			if(log_key.equalsIgnoreCase("stb_mac")){
				log_info_mac = log_value;
			}
			if(log_key.equalsIgnoreCase("sa_id")){
				log_info_id = log_value;
			}
			if(log_key.equalsIgnoreCase("app_type")){
				log_info_app_type = log_value;
			}
			log_debug += " ["+log_key+"="+log_value+"]";
		}

		log_logger.info("["+log_ip+"] ["+log_url+"][START] - ["+log_info_mac+"]["+log_info_id+"]["+log_info_app_type+"]");
		log_logger.debug("["+log_ip+"] ["+log_url+"][START] -"+log_debug);

		
		MainPanelResult result = new MainPanelResult();
		result.setRecordset(null);
		
		
		try{
			if("N".equals(callByScheduler)){		// 단말에서 호출한 경우
				validateGetMainPanelInterlockingInfo(sa_id, stb_mac, app_type, panel_id);
				result.setFlag(SmartUXProperties.getProperty("flag.success"));
				result.setMessage(SmartUXProperties.getProperty("message.success"));
			
				// 버전정보 셋팅
				String version = service.getMainPanelVersionInfo(sa_id, stb_mac, app_type, callByScheduler);
				result.setVersion(version);
			
				// 검색된 연동정보 셋팅
				List<MainPanelInfoVO> list = service.getMainPanelInterlockingInfo(sa_id, stb_mac, app_type, panel_id, callByScheduler);
				result.setRecordset(list);
			
				// 검색된 총 갯수 셋팅
				result.setTotal_count(list.size());
			}else if("A".equals(callByScheduler)){
				List<MainPanelInfoVO> list = service.getMainPanelInterlockingInfo(sa_id, stb_mac, app_type, panel_id, callByScheduler);
				result.setFlag(SmartUXProperties.getProperty("flag.success"));
				result.setMessage(SmartUXProperties.getProperty("message.success"));
				
				//response.setStatus(201);
			}else{
				List<MainPanelInfoVO> list = service.getMainPanelInterlockingInfo(sa_id, stb_mac, app_type, panel_id, callByScheduler);
				result.setFlag(SmartUXProperties.getProperty("flag.success"));
				result.setMessage(SmartUXProperties.getProperty("message.success"));
				//response.setStatus(201);
			}
			
		}catch(Exception e){
			log_logger.error("["+log_ip+"] ["+log_url+"]["+log_info_mac+"]["+log_info_id+"]["+log_info_app_type+"]"+"[getMainPanelInterlockingInfo]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			ExceptionHandler handler = new ExceptionHandler(e);
			result.setFlag(handler.getFlag());
			result.setMessage(handler.getMessage());
		}
		
		//#########[LOG END]#########
		long endTime = System.currentTimeMillis();
		String timeStr = Long.toString((endTime-startTime));
		if(timeStr.length() > 3){
			timeStr = timeStr.substring(0,timeStr.length()-3)+"."+timeStr.substring(timeStr.length()-3,timeStr.length());
		}else{
			for(int i=timeStr.length();i<3;i++){
				timeStr = "0"+timeStr;	
			}
			timeStr = "0."+timeStr;
		}
		log_logger.info("["+log_ip+"] ["+log_url+"][END]["+(timeStr)+" sec] - ["+log_info_mac+"]["+log_info_id+"]["+log_info_app_type+"] ["+result.getFlag()+"]");


		
		return result;
	}

	
//	//@RequestMapping(value="/Panel/sa_id/{sa_id}/stb_mac/{stb_mac}/app_type/{app_type}/panel_id/{panel_id}", method=RequestMethod.GET)
//	@RequestMapping(value="/v1/mainpanel/**", method=RequestMethod.GET)
//	public MainPanelResult Restful_getMainPanelInterlockingInfo(
//			HttpServletRequest request,
//			HttpServletResponse response
////			@PathVariable String sa_id,
////			@PathVariable String stb_mac,
////			@PathVariable String app_type,
////			@PathVariable String panel_id
//		){
//		
//		//#########################[LOG SET]##########################
//		Log log_logger = LogFactory.getLog("OPENAPI_Panel");
//		long startTime = System.currentTimeMillis();
//		String ip = request.getRemoteAddr();
//		String url = request.getRequestURI();
//		try{
//			url = "/v1/mainpanel/";
//		}catch(Exception e){
//		}
//		String method = request.getMethod();
//		//#########################[//LOG SET]##########################
//		
//		String sa_id = "";
//		String stb_mac = "";
//		String app_type = "";
//		String panel_id = "";
//		
//		String access_key = null;
//		String cp_id = null;
//		
//		MainPanelResult result = new MainPanelResult();
//		result.setRecordset(null);
//		
//		String uri = request.getRequestURI();
//		try {
//			HashMap<String, String> paramMap = GlobalCom.getRestfulParamMap(4,uri);
//			sa_id 		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "sa_id"));
//			stb_mac		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "stb_mac"));
//			app_type 	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "app_type"));
//			panel_id 	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "panel_id"));
//			
//			access_key = GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "access_key"));
//			cp_id = GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "cp_id"));
//		
//		
//			access_key = GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "access_key"));
//			cp_id = GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "cp_id"));
//		
//			//#########################[LOG START]##########################
//			log_logger.info("["+ip+"] ["+url+"]["+method+"][START] - ["+stb_mac+"]["+sa_id+"]["+app_type+"]["+access_key+"]["+cp_id+"]");
//			//#########################[//LOG START]########################
//			
//			//access_key 인증 허용 체크
//			String authResult = GlobalCom.isNull(AuthenticationCommon.isAuthenticationMap(access_key,cp_id,"GET","/v1/mainpanel"));
//			if(!authResult.equals("SUCCESS")){
//				SmartUXException exception = new SmartUXException();
//				
//				if(authResult.equals("ACCESS_KEY")){
//					exception.setFlag(SmartUXProperties.getProperty("flag.authentication.accessKey"));
//					exception.setMessage(SmartUXProperties.getProperty("message.authentication.accessKey"));
//				}else if(authResult.equals("CP_ID")){
//					exception.setFlag(SmartUXProperties.getProperty("flag.authentication.cp_id"));
//					exception.setMessage(SmartUXProperties.getProperty("message.authentication.cp_id"));
//				}else if(authResult.equals("EXDATE")){
//					exception.setFlag(SmartUXProperties.getProperty("flag.authentication.exdate"));
//					exception.setMessage(SmartUXProperties.getProperty("message.authentication.exdate"));
//				}else if(authResult.equals("URL")){
//					exception.setFlag(SmartUXProperties.getProperty("flag.authentication.url"));
//					exception.setMessage(SmartUXProperties.getProperty("message.authentication.url"));
//				}else if(authResult.equals("FAIL")){
//					exception.setFlag(SmartUXProperties.getProperty("flag.authentication.fail"));
//					exception.setMessage(SmartUXProperties.getProperty("message.authentication.fail"));
//				}
//				
//				throw exception;
//			}
//			
//			//단말에서만 OPEN_API 사용
//			String callByScheduler ="N";
//		
//		
//			if("N".equals(callByScheduler)){		// 단말에서 호출한 경우
//				validateGetMainPanelInterlockingInfo(sa_id, stb_mac, app_type, panel_id);
//				result.setFlag(SmartUXProperties.getProperty("flag.success"));
//				result.setMessage(SmartUXProperties.getProperty("message.success"));
//			
//				// 버전정보 셋팅
//				String version = service.getMainPanelVersionInfo(sa_id, stb_mac, app_type, callByScheduler);
//				result.setVersion(version);
//			
//				// 검색된 연동정보 셋팅
//				List<MainPanelInfoVO> list = service.getMainPanelInterlockingInfo(sa_id, stb_mac, app_type, panel_id, callByScheduler);
//				result.setRecordset(list);
//			
//				// 검색된 총 갯수 셋팅
//				result.setTotal_count(list.size());
//			}else if("A".equals(callByScheduler)){
//				List<MainPanelInfoVO> list = service.getMainPanelInterlockingInfo(sa_id, stb_mac, app_type, panel_id, callByScheduler);
//				result.setFlag(SmartUXProperties.getProperty("flag.success"));
//				result.setMessage(SmartUXProperties.getProperty("message.success"));
//				
//				//response.setStatus(201);
//			}else{
//				List<MainPanelInfoVO> list = service.getMainPanelInterlockingInfo(sa_id, stb_mac, app_type, panel_id, callByScheduler);
//				result.setFlag(SmartUXProperties.getProperty("flag.success"));
//				result.setMessage(SmartUXProperties.getProperty("message.success"));
//				//response.setStatus(201);
//			}
//			
//		}catch(Exception e){
//			log_logger.error("["+ip+"] ["+url+"]["+stb_mac+"]["+sa_id+"]["+app_type+"]["+access_key+"]["+cp_id+"]["+e.getClass().getName()+"]["+e.getMessage()+"]");
//			ExceptionHandler handler = new ExceptionHandler(e);
//			result.setFlag(handler.getFlag());
//			result.setMessage(handler.getMessage());
//		}
//		
//		//############################[LOG END]############################
//		long endTime = System.currentTimeMillis();
//		String timeStr = Long.toString((endTime-startTime));
//		if(timeStr.length() > 3){
//			timeStr = timeStr.substring(0,timeStr.length()-3)+"."+timeStr.substring(timeStr.length()-3,timeStr.length());
//		}else{
//			for(int i=timeStr.length();i<3;i++){
//				timeStr = "0"+timeStr;	
//			}
//			timeStr = "0."+timeStr;
//		}
//		log_logger.info("["+ip+"] ["+url+"]["+method+"][END] ["+(timeStr)+" sec] - ["+stb_mac+"]["+sa_id+"]["+app_type+"]["+access_key+"]["+cp_id+"] ["+result.getFlag()+"]");
//		//############################[//LOG END]##########################
//		
//		return result;
//	}
	
	@RequestMapping(value="/v1/mainpanel", method=RequestMethod.GET)
	public MainPanelResult Restful_getMainPanelInterlockingInfo(
			HttpServletRequest request
			,HttpServletResponse response
			,@RequestParam(value="sa_id", required=false) String sa_id
			,@RequestParam(value="stb_mac", required=false) String stb_mac
			,@RequestParam(value="app_type", required=false) String app_type
			,@RequestParam(value="panel_id", required=false, defaultValue="") String panel_id
			,@RequestParam(value="access_key", required=false, defaultValue="") String access_key
			,@RequestParam(value="cp_id", required=false, defaultValue="") String cp_id
		) throws Exception{
		
		SmartUXException exception = new SmartUXException();
		
		//#########################[LOG SET]##########################
		Log log_logger = LogFactory.getLog("OPENAPI_Panel");
		long startTime = System.currentTimeMillis();
		String ip = request.getRemoteAddr();
		String url = request.getRequestURI();
		try{
			url = "/v1/mainpanel/";
		}catch(Exception e){
		}
		String method = request.getMethod();
		//#########################[//LOG SET]##########################
		
//		String sa_id = "";
//		String stb_mac = "";
//		String app_type = "";
//		String panel_id = "";
//		
//		String access_key = null;
//		String cp_id = null;
		
		MainPanelResult result = new MainPanelResult();
		result.setRecordset(null);
		
		String uri = request.getRequestURI();
		try {
//			HashMap<String, String> paramMap = GlobalCom.getRestfulParamMap(4,uri);
//			sa_id 		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "sa_id"));
//			stb_mac		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "stb_mac"));
//			app_type 	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "app_type"));
//			panel_id 	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "panel_id"));
//			
//			access_key = GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "access_key"));
//			cp_id = GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "cp_id"));
		
			//#########################[LOG START]##########################
			log_logger.info("["+ip+"] ["+url+"]["+method+"][START] - ["+stb_mac+"]["+sa_id+"]["+app_type+"]["+access_key+"]["+cp_id+"]");
			//#########################[//LOG START]########################
			
			//access_key 인증 허용 체크
			String authResult = GlobalCom.isNull(AuthenticationCommon.isAuthenticationMap(access_key,cp_id,"GET","/v1/mainpanel"));
			if(!authResult.equals("SUCCESS")){
				//SmartUXException exception = new SmartUXException();
				
				if(authResult.equals("ACCESS_KEY")){
					exception.setFlag(SmartUXProperties.getProperty("flag.authentication.accessKey"));
					exception.setMessage(SmartUXProperties.getProperty("message.authentication.accessKey"));
				}else if(authResult.equals("CP_ID")){
					exception.setFlag(SmartUXProperties.getProperty("flag.authentication.cp_id"));
					exception.setMessage(SmartUXProperties.getProperty("message.authentication.cp_id"));
				}else if(authResult.equals("EXDATE")){
					exception.setFlag(SmartUXProperties.getProperty("flag.authentication.exdate"));
					exception.setMessage(SmartUXProperties.getProperty("message.authentication.exdate"));
				}else if(authResult.equals("URL")){
					exception.setFlag(SmartUXProperties.getProperty("flag.authentication.url"));
					exception.setMessage(SmartUXProperties.getProperty("message.authentication.url"));
				}else if(authResult.equals("FAIL")){
					exception.setFlag(SmartUXProperties.getProperty("flag.authentication.fail"));
					exception.setMessage(SmartUXProperties.getProperty("message.authentication.fail"));
				}
				
				response.setStatus(401);
				
				throw exception;
			}
			
			//단말에서만 OPEN_API 사용
			String callByScheduler ="N";
		
		
			if("N".equals(callByScheduler)){		// 단말에서 호출한 경우
				validateGetMainPanelInterlockingInfo(sa_id, stb_mac, app_type, panel_id);
				result.setFlag(SmartUXProperties.getProperty("flag.success"));
				result.setMessage(SmartUXProperties.getProperty("message.success"));
			
				// 버전정보 셋팅
				String version = service.getMainPanelVersionInfo(sa_id, stb_mac, app_type, callByScheduler);
				result.setVersion(version);
			
				// 검색된 연동정보 셋팅
				List<MainPanelInfoVO> list = service.getMainPanelInterlockingInfo(sa_id, stb_mac, app_type, panel_id, callByScheduler);
				result.setRecordset(list);
			
				// 검색된 총 갯수 셋팅
				result.setTotal_count(list.size());
			}else if("A".equals(callByScheduler)){
				List<MainPanelInfoVO> list = service.getMainPanelInterlockingInfo(sa_id, stb_mac, app_type, panel_id, callByScheduler);
				result.setFlag(SmartUXProperties.getProperty("flag.success"));
				result.setMessage(SmartUXProperties.getProperty("message.success"));
				
				//response.setStatus(201);
			}else{
				List<MainPanelInfoVO> list = service.getMainPanelInterlockingInfo(sa_id, stb_mac, app_type, panel_id, callByScheduler);
				result.setFlag(SmartUXProperties.getProperty("flag.success"));
				result.setMessage(SmartUXProperties.getProperty("message.success"));
				//response.setStatus(201);
			}
		} catch(SmartUXException e){
			log_logger.error("["+ip+"] ["+url+"]["+stb_mac+"]["+sa_id+"]["+app_type+"]["+access_key+"]["+cp_id+"]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			throw e;
			
		}catch(Exception e){
			log_logger.error("["+ip+"] ["+url+"]["+stb_mac+"]["+sa_id+"]["+app_type+"]["+access_key+"]["+cp_id+"]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			ExceptionHandler handler = new ExceptionHandler(e);
			result.setFlag(handler.getFlag());
			result.setMessage(handler.getMessage());
		}
		
		//############################[LOG END]############################
		long endTime = System.currentTimeMillis();
		String timeStr = Long.toString((endTime-startTime));
		if(timeStr.length() > 3){
			timeStr = timeStr.substring(0,timeStr.length()-3)+"."+timeStr.substring(timeStr.length()-3,timeStr.length());
		}else{
			for(int i=timeStr.length();i<3;i++){
				timeStr = "0"+timeStr;	
			}
			timeStr = "0."+timeStr;
		}
		log_logger.info("["+ip+"] ["+url+"]["+method+"][END] ["+(timeStr)+" sec] - ["+stb_mac+"]["+sa_id+"]["+app_type+"]["+access_key+"]["+cp_id+"] ["+result.getFlag()+"]");
		//############################[//LOG END]##########################
		
		return result;
	}
	
	/**
	 * I20 편성 앨범 조회
	 * @param request			HttpServletRequest 객체
	 * @param response			HttpServletResponse 객체
	 * @param sa_id				가입번호
	 * @param stb_mac			맥주소
	 * @param app_type			어플타입
	 * @param start_num			검색 시작 인덱스(-1이면 req_count 값을 무시하고 전체를 검색한다)
	 * @param req_count			검색 레코드 갯수
	 * @param category_id		검색하고자 하는 앨범이 있는 category_id(2개 이상일 경우 ||로 결합되어 있다)
	 * @param callByScheduler	단말에서 호출 했을 경우엔 N, 스케쥴러에서 호출했을땐 Y, 관리자툴에서 조회했을땐 A가 들어간다
	 * @return
	 */
	@RequestMapping(value="/getI20AlbumList")
	public Result<AlbumInfoVO> API_getI20AlbumList(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value="sa_id", required=false) String sa_id,
			@RequestParam(value="stb_mac", required=false) String stb_mac,
			@RequestParam(value="app_type", required=false) String app_type,
			@RequestParam(value="start_num", required=false) String start_num,
			@RequestParam(value="req_count", required=false) String req_count,
			@RequestParam(value="category_id", required=false) String category_id,
			@RequestParam(value="net_typ",required=false, defaultValue="2") String net_typ,
			@RequestParam(value="callByScheduler", required=false, defaultValue="N") String callByScheduler
			){
		
		//#########[LOG START]#########
		Log log_logger = LogFactory.getLog("getI20AlbumList");
		long startTime = System.currentTimeMillis();
		String log_ip = request.getRemoteAddr();
		String log_url = request.getRequestURI();
		Enumeration log_penum = request.getParameterNames();
		String log_key = null;
		String log_value = null;
		String log_info_mac = "";
		String log_info_id = "";
		String log_info_app_type = "";
		String log_debug = "";

		while(log_penum.hasMoreElements()){
			log_key = (String)log_penum.nextElement();
			log_value = (new String(request.getParameter(log_key)) == null) ? "" : new String(request.getParameter(log_key));
			
			if(log_key.equalsIgnoreCase("stb_mac")){
				log_info_mac = log_value;
			}
			if(log_key.equalsIgnoreCase("sa_id")){
				log_info_id = log_value;
			}
			if(log_key.equalsIgnoreCase("app_type")){
				log_info_app_type = log_value;
			}
			log_debug += " ["+log_key+"="+log_value+"]";
		}

		log_logger.info("["+log_ip+"] ["+log_url+"][START] - ["+log_info_mac+"]["+log_info_id+"]["+log_info_app_type+"]");
		log_logger.debug("["+log_ip+"] ["+log_url+"][START] -"+log_debug);
		
		Result<AlbumInfoVO> result = new Result<AlbumInfoVO>();
		result.setRecordset(null);
		
		try{
			
			//망타입이 01,02,31.. 으로 넘어오는데 만약 A,B 등과 같은 형태로 넘어오면 잘못된 형식이다.
			int fh_gbn = 9999;
			try{
				int tmpGbn = Integer.parseInt(net_typ);
				if(tmpGbn==1 || tmpGbn==2){//fullHD 서비스 하는 망
					fh_gbn = 1;
				}else{
					fh_gbn = 2;
				}
			}catch(Exception e){
				net_typ = "FAIL";
			}
			
			if("N".equals(callByScheduler)){		// 단말에서 호출한 경우
				validateGetI20AlbumList(sa_id, stb_mac, app_type, start_num, req_count, category_id, net_typ);
				result.setFlag(SmartUXProperties.getProperty("flag.success"));
				result.setMessage(SmartUXProperties.getProperty("message.success"));
				
				// 검색된 총 갯수 셋팅
				int totalCount = service.getI20AlbumListCount(sa_id, stb_mac, app_type, category_id, start_num, req_count, callByScheduler);
				result.setTotal_count(totalCount);
				
				// 시작번호를 가지고 전체 조회, 또는 페이징 조회를 해야 하는지의 판단여부
				int intStartnum = Integer.parseInt(start_num);
				if((intStartnum > 0) || (intStartnum == -1)){					// 검색 시작 인덱스가 -1 또는 1 이상일때만 결과셋
					List<AlbumInfoVO> list = service.getI20AlbumList(sa_id, stb_mac, app_type, category_id, start_num, req_count, fh_gbn, callByScheduler);
					result.setRecordset(list);
				}
			}else if("A".equals(callByScheduler)){
				List<AlbumInfoVO> list = service.getI20AlbumList("", "", "", "", "", "", fh_gbn, callByScheduler);
				result.setFlag(SmartUXProperties.getProperty("flag.success"));
				result.setMessage(SmartUXProperties.getProperty("message.success"));
				result.setRecordset(list);
				
				//response.setStatus(201);
			}else{
				List<AlbumInfoVO> list = service.getI20AlbumList("", "", "", "", "", "", fh_gbn, callByScheduler);
				result.setFlag(SmartUXProperties.getProperty("flag.success"));
				result.setMessage(SmartUXProperties.getProperty("message.success"));
				result.setRecordset(list);
				//response.setStatus(201);
			}
		}catch(Exception e){
			log_logger.error("["+log_ip+"] ["+log_url+"]["+log_info_mac+"]["+log_info_id+"]["+log_info_app_type+"]"+"[getI20AlbumList]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			ExceptionHandler handler = new ExceptionHandler(e);
			result.setFlag(handler.getFlag());
			result.setMessage(handler.getMessage());
		}
		
		//#########[LOG END]#########
		long endTime = System.currentTimeMillis();
		String timeStr = Long.toString((endTime-startTime));
		if(timeStr.length() > 3){
			timeStr = timeStr.substring(0,timeStr.length()-3)+"."+timeStr.substring(timeStr.length()-3,timeStr.length());
		}else{
			for(int i=timeStr.length();i<3;i++){
				timeStr = "0"+timeStr;	
			}
			timeStr = "0."+timeStr;
		}
		log_logger.info("["+log_ip+"] ["+log_url+"][END]["+(timeStr)+" sec] - ["+log_info_mac+"]["+log_info_id+"]["+log_info_app_type+"] ["+result.getFlag()+"]");


		
		
		return result;
	}
	
//	//@RequestMapping(value="/I20Album/sa_id/{sa_id}/stb_mac/{stb_mac}/app_type/{app_type}/start_num/{start_num}/req_count/{req_count}/category_id/{category_id}", method=RequestMethod.GET)
//	//@RequestMapping(value="/I20Album/**", method=RequestMethod.GET)
//	@RequestMapping(value="/v1/i20list/**", method=RequestMethod.GET)
//	public Result<AlbumInfoVO> Restful_getI20AlbumList(
//			HttpServletRequest request,
//			HttpServletResponse response
////			@PathVariable String sa_id,
////			@PathVariable String stb_mac,
////			@PathVariable String app_type,
////			@PathVariable String start_num,
////			@PathVariable String req_count,
////			@PathVariable String category_id
//			){
//		
//		//#########################[LOG SET]##########################
//		Log log_logger = LogFactory.getLog("OPENAPI_I20Album");
//		long startTime = System.currentTimeMillis();
//		String ip = request.getRemoteAddr();
//		String url = request.getRequestURI();
//		try{
//			url = "/v1/i20list/";
//		}catch(Exception e){
//		}
//		String method = request.getMethod();
//		//#########################[//LOG SET]##########################
//		
//		String sa_id = "";
//		String stb_mac = "";
//		String app_type = "";
//		String start_num = "";
//		String req_count = "";
//		String category_id = "";
//		
//		String access_key = null;
//		String cp_id = null;
//		
//		Result<AlbumInfoVO> result = new Result<AlbumInfoVO>();
//		result.setRecordset(null);
//		
//		String uri = request.getRequestURI();
//		try {
//			HashMap<String, String> paramMap = GlobalCom.getRestfulParamMap(4,uri);
//			sa_id 		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "sa_id"));
//			stb_mac		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "stb_mac"));
//			app_type 	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "app_type"));
//			start_num 	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "start_num"));
//			req_count	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "req_count"));
//			category_id	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "category_id"));
//			
//			access_key = GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "access_key"));
//			cp_id = GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "cp_id"));
//		
//			//#########################[LOG START]##########################
//			log_logger.info("["+ip+"] ["+url+"]["+method+"][START] - ["+stb_mac+"]["+sa_id+"]["+app_type+"]["+access_key+"]["+cp_id+"]");
//			//#########################[//LOG START]########################
//	
//			//access_key 인증 허용 체크
//			String authResult = GlobalCom.isNull(AuthenticationCommon.isAuthenticationMap(access_key,cp_id,"GET","/v1/i20list"));
//			if(!authResult.equals("SUCCESS")){
//				SmartUXException exception = new SmartUXException();
//				
//				if(authResult.equals("ACCESS_KEY")){
//					exception.setFlag(SmartUXProperties.getProperty("flag.authentication.accessKey"));
//					exception.setMessage(SmartUXProperties.getProperty("message.authentication.accessKey"));
//				}else if(authResult.equals("CP_ID")){
//					exception.setFlag(SmartUXProperties.getProperty("flag.authentication.cp_id"));
//					exception.setMessage(SmartUXProperties.getProperty("message.authentication.cp_id"));
//				}else if(authResult.equals("EXDATE")){
//					exception.setFlag(SmartUXProperties.getProperty("flag.authentication.exdate"));
//					exception.setMessage(SmartUXProperties.getProperty("message.authentication.exdate"));
//				}else if(authResult.equals("URL")){
//					exception.setFlag(SmartUXProperties.getProperty("flag.authentication.url"));
//					exception.setMessage(SmartUXProperties.getProperty("message.authentication.url"));
//				}else if(authResult.equals("FAIL")){
//					exception.setFlag(SmartUXProperties.getProperty("flag.authentication.fail"));
//					exception.setMessage(SmartUXProperties.getProperty("message.authentication.fail"));
//				}
//				
//				throw exception;
//			}
//			
//			
//		
//			//단말에서만 OPEN_API 사용
//			String callByScheduler = "N";
//		
//			if("N".equals(callByScheduler)){		// 단말에서 호출한 경우
//				validateGetI20AlbumList(sa_id, stb_mac, app_type, start_num, req_count, category_id);
//				result.setFlag(SmartUXProperties.getProperty("flag.success"));
//				result.setMessage(SmartUXProperties.getProperty("message.success"));
//				
//				// 검색된 총 갯수 셋팅
//				int totalCount = service.getI20AlbumListCount(sa_id, stb_mac, app_type, category_id, start_num, req_count, callByScheduler);
//				result.setTotal_count(totalCount);
//				
//				// 시작번호를 가지고 전체 조회, 또는 페이징 조회를 해야 하는지의 판단여부
//				int intStartnum = Integer.parseInt(start_num);
//				if((intStartnum > 0) || (intStartnum == -1)){					// 검색 시작 인덱스가 -1 또는 1 이상일때만 결과셋
//					List<AlbumInfoVO> list = service.getI20AlbumList(sa_id, stb_mac, app_type, category_id, start_num, req_count, callByScheduler);
//					result.setRecordset(list);
//				}
//			}else if("A".equals(callByScheduler)){
//				List<AlbumInfoVO> list = service.getI20AlbumList("", "", "", "", "", "", callByScheduler);
//				result.setFlag(SmartUXProperties.getProperty("flag.success"));
//				result.setMessage(SmartUXProperties.getProperty("message.success"));
//				result.setRecordset(list);
//				
//				//response.setStatus(201);
//			}else{
//				List<AlbumInfoVO> list = service.getI20AlbumList("", "", "", "", "", "", callByScheduler);
//				result.setFlag(SmartUXProperties.getProperty("flag.success"));
//				result.setMessage(SmartUXProperties.getProperty("message.success"));
//				result.setRecordset(list);
//				//response.setStatus(201);
//			}
//		}catch(Exception e){
//			log_logger.error("["+ip+"] ["+url+"]["+stb_mac+"]["+sa_id+"]["+app_type+"]["+access_key+"]["+cp_id+"]["+e.getClass().getName()+"]["+e.getMessage()+"]");
//			ExceptionHandler handler = new ExceptionHandler(e);
//			result.setFlag(handler.getFlag());
//			result.setMessage(handler.getMessage());
//		}
//		
//		//############################[LOG END]############################
//		long endTime = System.currentTimeMillis();
//		String timeStr = Long.toString((endTime-startTime));
//		if(timeStr.length() > 3){
//			timeStr = timeStr.substring(0,timeStr.length()-3)+"."+timeStr.substring(timeStr.length()-3,timeStr.length());
//		}else{
//			for(int i=timeStr.length();i<3;i++){
//				timeStr = "0"+timeStr;	
//			}
//			timeStr = "0."+timeStr;
//		}
//		log_logger.info("["+ip+"] ["+url+"]["+method+"][END] ["+(timeStr)+" sec] - ["+stb_mac+"]["+sa_id+"]["+app_type+"]["+access_key+"]["+cp_id+"] ["+result.getFlag()+"]");
//		//############################[//LOG END]##########################
//
//		return result;
//	}
	
	
	@RequestMapping(value="/v1/i20list", method=RequestMethod.GET)
	public Result<AlbumInfoVO> Restful_getI20AlbumList(
			HttpServletRequest request
			,HttpServletResponse response
			,@RequestParam(value="sa_id", required=false) String sa_id
			,@RequestParam(value="stb_mac", required=false) String stb_mac
			,@RequestParam(value="app_type", required=false) String app_type
			,@RequestParam(value="start_num", required=false) String start_num
			,@RequestParam(value="req_count", required=false) String req_count
			,@RequestParam(value="category_id", required=false) String category_id
			,@RequestParam(value="access_key", required=false, defaultValue="") String access_key
			,@RequestParam(value="cp_id", required=false, defaultValue="") String cp_id
			,@RequestParam(value="net_typ",required=false, defaultValue="2") String net_typ
		) throws Exception{
		
		SmartUXException exception = new SmartUXException();
		
		//#########################[LOG SET]##########################
		Log log_logger = LogFactory.getLog("OPENAPI_I20Album");
		long startTime = System.currentTimeMillis();
		String ip = request.getRemoteAddr();
		String url = request.getRequestURI();
		try{
			url = "/v1/i20list/";
		}catch(Exception e){
		}
		String method = request.getMethod();
		//#########################[//LOG SET]##########################
		
//		String sa_id = "";
//		String stb_mac = "";
//		String app_type = "";
//		String start_num = "";
//		String req_count = "";
//		String category_id = "";
//		
//		String access_key = null;
//		String cp_id = null;
		
		Result<AlbumInfoVO> result = new Result<AlbumInfoVO>();
		result.setRecordset(null);
		
		String uri = request.getRequestURI();
		try {
//			HashMap<String, String> paramMap = GlobalCom.getRestfulParamMap(4,uri);
//			sa_id 		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "sa_id"));
//			stb_mac		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "stb_mac"));
//			app_type 	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "app_type"));
//			start_num 	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "start_num"));
//			req_count	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "req_count"));
//			category_id	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "category_id"));
//			
//			access_key = GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "access_key"));
//			cp_id = GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "cp_id"));
		
			//#########################[LOG START]##########################
			log_logger.info("["+ip+"] ["+url+"]["+method+"][START] - ["+stb_mac+"]["+sa_id+"]["+app_type+"]["+access_key+"]["+cp_id+"]");
			//#########################[//LOG START]########################
	
			//access_key 인증 허용 체크
			String authResult = GlobalCom.isNull(AuthenticationCommon.isAuthenticationMap(access_key,cp_id,"GET","/v1/i20list"));
			if(!authResult.equals("SUCCESS")){
				//SmartUXException exception = new SmartUXException();
				
				if(authResult.equals("ACCESS_KEY")){
					exception.setFlag(SmartUXProperties.getProperty("flag.authentication.accessKey"));
					exception.setMessage(SmartUXProperties.getProperty("message.authentication.accessKey"));
				}else if(authResult.equals("CP_ID")){
					exception.setFlag(SmartUXProperties.getProperty("flag.authentication.cp_id"));
					exception.setMessage(SmartUXProperties.getProperty("message.authentication.cp_id"));
				}else if(authResult.equals("EXDATE")){
					exception.setFlag(SmartUXProperties.getProperty("flag.authentication.exdate"));
					exception.setMessage(SmartUXProperties.getProperty("message.authentication.exdate"));
				}else if(authResult.equals("URL")){
					exception.setFlag(SmartUXProperties.getProperty("flag.authentication.url"));
					exception.setMessage(SmartUXProperties.getProperty("message.authentication.url"));
				}else if(authResult.equals("FAIL")){
					exception.setFlag(SmartUXProperties.getProperty("flag.authentication.fail"));
					exception.setMessage(SmartUXProperties.getProperty("message.authentication.fail"));
				}
				
				response.setStatus(401);
				
				throw exception;
			}
			
			
		
			//단말에서만 OPEN_API 사용
			String callByScheduler = "N";
		
			//망타입이 01,02,31.. 으로 넘어오는데 만약 A,B 등과 같은 형태로 넘어오면 잘못된 형식이다.
			int fh_gbn = 9999;
			try{
				int tmpGbn = Integer.parseInt(net_typ);
				if(tmpGbn==1 || tmpGbn==2){//fullHD 서비스 하는 망
					fh_gbn = 1;
				}else{
					fh_gbn = 2;
				}
			}catch(Exception e){
				net_typ = "FAIL";
			}
			
			if("N".equals(callByScheduler)){		// 단말에서 호출한 경우
				validateGetI20AlbumList(sa_id, stb_mac, app_type, start_num, req_count, category_id, net_typ);
				result.setFlag(SmartUXProperties.getProperty("flag.success"));
				result.setMessage(SmartUXProperties.getProperty("message.success"));
				
				// 검색된 총 갯수 셋팅
				int totalCount = service.getI20AlbumListCount(sa_id, stb_mac, app_type, category_id, start_num, req_count, callByScheduler);
				result.setTotal_count(totalCount);
				
				// 시작번호를 가지고 전체 조회, 또는 페이징 조회를 해야 하는지의 판단여부
				int intStartnum = Integer.parseInt(start_num);
				if((intStartnum > 0) || (intStartnum == -1)){					// 검색 시작 인덱스가 -1 또는 1 이상일때만 결과셋
					List<AlbumInfoVO> list = service.getI20AlbumList(sa_id, stb_mac, app_type, category_id, start_num, req_count, fh_gbn, callByScheduler);
					result.setRecordset(list);
				}
			}else if("A".equals(callByScheduler)){
				List<AlbumInfoVO> list = service.getI20AlbumList("", "", "", "", "", "", fh_gbn, callByScheduler);
				result.setFlag(SmartUXProperties.getProperty("flag.success"));
				result.setMessage(SmartUXProperties.getProperty("message.success"));
				result.setRecordset(list);
				
				//response.setStatus(201);
			}else{
				List<AlbumInfoVO> list = service.getI20AlbumList("", "", "", "", "", "", fh_gbn, callByScheduler);
				result.setFlag(SmartUXProperties.getProperty("flag.success"));
				result.setMessage(SmartUXProperties.getProperty("message.success"));
				result.setRecordset(list);
				//response.setStatus(201);
			}
		} catch(SmartUXException e){
			log_logger.error("["+ip+"] ["+url+"]["+stb_mac+"]["+sa_id+"]["+app_type+"]["+access_key+"]["+cp_id+"]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			throw e;
			
		}catch(Exception e){
			log_logger.error("["+ip+"] ["+url+"]["+stb_mac+"]["+sa_id+"]["+app_type+"]["+access_key+"]["+cp_id+"]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			ExceptionHandler handler = new ExceptionHandler(e);
			result.setFlag(handler.getFlag());
			result.setMessage(handler.getMessage());
		}
		
		//############################[LOG END]############################
		long endTime = System.currentTimeMillis();
		String timeStr = Long.toString((endTime-startTime));
		if(timeStr.length() > 3){
			timeStr = timeStr.substring(0,timeStr.length()-3)+"."+timeStr.substring(timeStr.length()-3,timeStr.length());
		}else{
			for(int i=timeStr.length();i<3;i++){
				timeStr = "0"+timeStr;	
			}
			timeStr = "0."+timeStr;
		}
		log_logger.info("["+ip+"] ["+url+"]["+method+"][END] ["+(timeStr)+" sec] - ["+stb_mac+"]["+sa_id+"]["+app_type+"]["+access_key+"]["+cp_id+"] ["+result.getFlag()+"]");
		//############################[//LOG END]##########################

		return result;
	}
	
	/**
	 * getMainPanelVersionInfo 함수에서 사용하는 validation 함수
	 * @param sa_id			가입번호
	 * @param stb_mac		맥주소
	 * @param app_type		어플타입
	 * @throws SmartUXException validate에 문제가 발생했을 경우 코드와 메시지가 셋팅되서 던져지는 SmartUXException 객체
	 */
	private void validateGetMainPanelVersionInfo(String sa_id, String stb_mac, String app_type) throws SmartUXException {
		SmartUXException exception = new SmartUXException();
		
		// 가입 번호 파라미터 존재 유무
		if(!(StringUtils.hasText(sa_id))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "sa_id"));
			throw exception;
		}
		
		// 맥주소 파라미터 존재 유무
		
		if(!(StringUtils.hasText(stb_mac))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "stb_mac"));
			throw exception;
		}
		
		// 어플 타입 파라미터 존재 유무
		
		if(!(StringUtils.hasText(app_type))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "app_type"));
			throw exception;
		}
//		130703 타입모두 허용
//		else{
//			// 어플 타입 파라미터 값이 SETTOP인지 확인
//			if(!("UX".equals(app_type)) && !("SMA".equals(app_type))){
//				exception.setFlag(SmartUXProperties.getProperty("flag.mismatchvalue"));
//				exception.setMessage(SmartUXProperties.getProperty("message.mismatchvalue", "app_type", "UX 이거나 SMA"));
//				throw exception;
//			}
//		}
	}
	
	/**
	 * getMainPanelInterlockingInfo 함수에서 사용하는 validation 함수
	 * @param sa_id			가입번호
	 * @param stb_mac		맥주소
	 * @param app_type		어플타입
	 * @throws SmartUXException validate에 문제가 발생했을 경우 코드와 메시지가 셋팅되서 던져지는 SmartUXException 객체
	 */
	private void validateGetMainPanelInterlockingInfo(String sa_id, String stb_mac, String app_type, String panel_id) throws SmartUXException {
		SmartUXException exception = new SmartUXException();
		
		// 가입 번호 파라미터 존재 유무
		if(!(StringUtils.hasText(sa_id))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "sa_id"));
			throw exception;
		}
		
		// 맥주소 파라미터 존재 유무
		
		if(!(StringUtils.hasText(stb_mac))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "stb_mac"));
			throw exception;
		}
		
		// 어플 타입 파라미터 존재 유무
		
		if(!(StringUtils.hasText(app_type))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "app_type"));
			throw exception;
		}
//		130703 타입모두 허용
//		else{
//			// 어플 타입 파라미터 값이 SETTOP인지 확인
//			if(!("UX".equals(app_type)) && !("SMA".equals(app_type))){
//				exception.setFlag(SmartUXProperties.getProperty("flag.mismatchvalue"));
//				exception.setMessage(SmartUXProperties.getProperty("message.mismatchvalue", "app_type", "UX 이거나 SMA"));
//				throw exception;
//			}
//		}
		
		//특정 패널의 값을 읽을려고 할때 사용하는 패널
		if(!(StringUtils.hasText(panel_id))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "panel_id"));
			throw exception;
		}
	}
	
	/**
	 * getI20AlbumList 함수에서 사용하는 validation 함수
	 * @param sa_id			가입번호
	 * @param stb_mac		맥주소
	 * @param app_type		어플타입
	 * @param start_num		검색 시작 인덱스(-1이면 req_count 값을 무시하고 전체를 검색하고 0일땐 검색된 레코드 총갯수만 리턴한다)
	 * @param req_count		검색 레코드 갯수
	 * @param category_id	|| 구분자로 연결된 카테고리 코드
	 * @throws SmartUXException validate에 문제가 발생했을 경우 코드와 메시지가 셋팅되서 던져지는 SmartUXException 객체
	 */
	private void validateGetI20AlbumList(String sa_id, String stb_mac, String app_type, String start_num, String req_count, String category_id, String net_typ) throws SmartUXException {
		SmartUXException exception = new SmartUXException();
		
		// 가입 번호 파라미터 존재 유무
		if(!(StringUtils.hasText(sa_id))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "sa_id"));
			throw exception;
		}
		
		// 맥주소 파라미터 존재 유무
		
		if(!(StringUtils.hasText(stb_mac))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "stb_mac"));
			throw exception;
		}
		
		// 어플 타입 파라미터 존재 유무
		
		if(!(StringUtils.hasText(app_type))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "app_type"));
			throw exception;
		}
//		130703 타입모두 허용
//		else{
//			// 어플 타입 파라미터 값이 SETTOP인지 확인
//			/*
//			if(!("UX".equals(app_type))){
//				exception.setFlag(SmartUXProperties.getProperty("flag.mismatchvalue"));
//				exception.setMessage(SmartUXProperties.getProperty("message.mismatchvalue", "app_type", "UX"));
//				throw exception;
//			}
//			*/
//			if(!("UX".equals(app_type)) && !("SMA".equals(app_type))){
//				exception.setFlag(SmartUXProperties.getProperty("flag.mismatchvalue"));
//				exception.setMessage(SmartUXProperties.getProperty("message.mismatchvalue", "app_type", "UX 이거나 SMA"));
//				throw exception;
//			}
//		}
		
		//망타입이 잘못넘어 왔을 경우
		if("FAIL".equals(net_typ)){
			exception.setFlag(SmartUXProperties.getProperty("flag.format"));
			exception.setMessage(SmartUXProperties.getProperty("message.format", "net_typ"));
			throw exception;
		}
		
		// 검색 시작 인덱스가 있을 경우
		if(StringUtils.hasText(start_num)){
			
			int intStartnum = 0;
			// 검색 시작 인덱스가 숫자형인지 확인
			try{
				intStartnum = Integer.parseInt(start_num);
			}catch(NumberFormatException e){
				exception.setFlag(SmartUXProperties.getProperty("flag.numberformat"));
				exception.setMessage(SmartUXProperties.getProperty("message.numberformat" , "start_num"));
				throw exception;
			}
			
			// 검색 시작 인덱스가 -1 이상인 경우
			if(intStartnum >= -1){
				if(intStartnum == -1){			// 검색 시작 인덱스가 -1인 경우(전체 조회)
					
				}else if(intStartnum == 0){		// 검색 시작 인덱스가 0인 경우(검색된 레코드 총갯수만 return 하는 경우) 
					
				}else{							// 검색 시작 인덱스가 1 이상인 경우(페이징)
					if(!(StringUtils.hasText(req_count))){			// req_count 파라미터가 없으면
						exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
						exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "req_count")); 
						throw exception;
					}
					
					int intReqcount = 0;
					
					try{
						intReqcount = Integer.parseInt(req_count);	// req_count 숫자형인지 확인
					}catch(NumberFormatException e){
						exception.setFlag(SmartUXProperties.getProperty("flag.numberformat"));
						exception.setMessage(SmartUXProperties.getProperty("message.numberformat", "req_count"));
						throw exception;
					}
					
					if(intReqcount <= 0){							// req_count 파라미터 값이 0이하이면(req_count 파라미터는 1 이상 값이어야 한다)
						exception.setFlag(SmartUXProperties.getProperty("flag.range2"));
						exception.setMessage(SmartUXProperties.getProperty("message.range2", "req_count", "1"));
						throw exception;
					}
				}
			}else{	// 검색 시작 인덱스가 -1 미만인 경우
				exception.setFlag(SmartUXProperties.getProperty("flag.range2"));
				exception.setMessage(SmartUXProperties.getProperty("message.range2", "start_num", "-1"));
				throw exception;
			}
		}else{	// 검색 시작 인덱스가 없을 경우
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "start_num"));
			throw exception;
		}
		
		// 카테고리 코드 파라미터 존재 유무
		
		if(!(StringUtils.hasText(category_id))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "category_id"));
			throw exception;
		}
	}
}
