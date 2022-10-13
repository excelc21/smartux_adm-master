package com.dmi.smartux.bonbang.controller;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
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
import com.dmi.smartux.bonbang.service.RegistrationIDService;
import com.dmi.smartux.bonbang.service.ReservedProgramService;
import com.dmi.smartux.bonbang.vo.RegistrationIDParamVO;
import com.dmi.smartux.bonbang.vo.RegistrationIDVO;
import com.dmi.smartux.bonbang.vo.ReservedProgramParamVO;
import com.dmi.smartux.bonbang.vo.ReservedProgramVO;
import com.dmi.smartux.common.exception.ExceptionHandler;
import com.dmi.smartux.common.exception.SmartUXException;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.push.PushCommon;
import com.dmi.smartux.push.service.PushService;
import com.dmi.smartux.push.vo.PushResultVO;
import com.dmi.smartux.push.vo.PushSocketVO;
import com.dmi.smartux.common.util.CharacterSet;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.vo.Result;
import com.dmi.smartux.ios_push.IOS_PushCommon;
import com.dmi.smartux.ios_push.service.IOS_PushService;
import com.dmi.smartux.ios_push.vo.IOS_PushSocketVO;

/**
 * 본방사수 서비스 컨트롤러 클래스
 * @author YJ KIM
 *
 */
@Controller
public class BonbangController {
	
	private final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	PushService psService;
	
	@Autowired
	IOS_PushService ios_psService;
	
	@Autowired
	ReservedProgramService reservedService;
	
	@Autowired
	RegistrationIDService pairService;
	
	
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
	 * PushGW 초기 소켓 활성화 메서드
	 * (PushGW 소켓 사용하는 Controller에서 init 메서드 추가하여야 함)
	 */
	@PostConstruct
	public void init(){
		//PushGW 설정값 세팅
		if(PushCommon.pushGWServerIP.equals("")){			
			PushCommon common = new PushCommon();
			common.setPushCommon();
		}		
		try {
			//설정된 값만큼 소켓을 전부 활성화
			psService.setPushGWSocketAllOpen();
			logger.debug("init "+"소켓 연결 성공");
			logger.debug("init "+"활성화 된 총 소켓 수 = "+PushCommon.pushGWSocketQueue.size());
		} catch (Exception e) {
			logger.debug("init "+" 소켓 연결 실패 원인 = "+e.getMessage());
		}
		
		//IOS PushGW 설정값 세팅
		//PushGW 서버설정은 pushGWServerIP값에 존재 유무로 판단한다.
		if(IOS_PushCommon.pushGWServerIP.equals("")){
			IOS_PushCommon common = new IOS_PushCommon();
			common.setIOS_PushCommon();
		}
		try {
			//설정된 값만큼 소켓을 전부 활성화
			ios_psService.setPushGWSocketAllOpen();
			logger.debug("IOS Push ##-init "+"소켓 연결 성공");
			logger.debug("IOS Push ##-init "+"활성화 된 총 소켓 수 = "+IOS_PushCommon.pushGWSocketQueue.size());
		} catch (Exception e) {
			logger.debug("IOS Push ##-init "+" 소켓 연결 실패 원인 = "+e.getMessage());
		}
		
	}
	
	@PreDestroy
	public void cleanUp(){
		logger.debug("=======cleanUp "+"소켓 정리 시작==========");
		logger.debug("=======cleanUp "+"size "+PushCommon.pushGWSocketQueue.size()+"==========");
		int push_size = PushCommon.pushGWSocketQueue.size();
		try{
			for(int i=0;i<push_size;i++){
				try{
					PushSocketVO psVO = PushCommon.pushGWSocketQueue.remove(0);					
					logger.debug("=======cleanUp Push "+"소켓 정리["+i+"] "+psVO+"  ==========");
					psService.closeSocketQueue(psVO);
				}catch(Exception e){}
			}
		}catch(Exception e){}
		
		
		int ios_push_size = IOS_PushCommon.pushGWSocketQueue.size(); 
		try{
			for(int i=0;i<ios_push_size;i++){
				try{
					IOS_PushSocketVO psVO = IOS_PushCommon.pushGWSocketQueue.remove(0);
					logger.debug("=======cleanUp IOS "+"소켓 정리["+i+"]  "+psVO+" ==========");
					ios_psService.closeSocketQueue(psVO);
				}catch(Exception e){}
			}
		}catch(Exception e){}
		
		logger.debug("=======cleanUp "+"소켓 정리 종료==========");		
	}
	
	/**
	 * RegistrationID 등록 API
	 * @param sa_id				셋탑 가입 번호
	 * @param stb_mac			셋탑 맥주소
	 * @param sma_mac			모바일앱 맥주소
	 * @param reg_id			RegistrationID
	 * @param app_type			어플타입
	 * @param pushagent_yn		모바일앱 자사/타사 구분
	 * @return					응답 VO 객체(결과코드, 결과메세지)
	 */
	@RequestMapping(value="/addRegistrationID")
	public RegistrationIDVO API_addRegistrationID(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value="sa_id", required=false, defaultValue="") String sa_id
			,@RequestParam(value="stb_mac", required=false, defaultValue="") String stb_mac
			,@RequestParam(value="sma_mac", required=false, defaultValue="") String sma_mac
			,@RequestParam(value="reg_id", required=false, defaultValue="") String reg_id
			,@RequestParam(value="app_type", required=false, defaultValue="") String app_type
			,@RequestParam(value="pushagent_yn", required=false, defaultValue="") String pushagent_yn
			){
		
		//#########[LOG START]#########
		Log log_logger = LogFactory.getLog("addRegistrationID");
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
		
		
		
		RegistrationIDVO result = new RegistrationIDVO();
		
		try{
			RegistrationIDParamVO pvo = new RegistrationIDParamVO();
			pvo.setSa_id(sa_id);
			pvo.setStb_mac(stb_mac);
			pvo.setSma_mac(sma_mac);
			pvo.setReg_id(reg_id);
			pvo.setApp_type(app_type);
			pvo.setPushagent_yn(pushagent_yn);
			
			//reg_id = URLDecoder.decode(reg_id, "UTF-8");
		
			validateAddRegistrationID(pvo);
			
			pairService.addRegistrationID(pvo);
			
			result.setFlag(SmartUXProperties.getProperty("flag.success"));
			result.setMessage(SmartUXProperties.getProperty("message.success"));
			
		}catch(Exception e){
			//logger.error("addRegistrationID "+e.getClass().getName());
			//logger.error("addRegistrationID "+e.getMessage());
			//logger.error("[addRegistrationID]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			log_logger.error("["+log_ip+"] ["+log_url+"]["+log_info_mac+"]["+log_info_id+"]["+log_info_app_type+"]"+"[addRegistrationID]["+e.getClass().getName()+"]["+e.getMessage()+"]");
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
		log_logger.info("["+log_ip+"] ["+log_url+"][END] ["+(timeStr)+" sec] - ["+log_info_mac+"]["+log_info_id+"]["+log_info_app_type+"] ["+result.getFlag()+"]");
		
		return result;
	}
	
//	//@RequestMapping(value="/RegistrationID/sa_id/{sa_id}/stb_mac/{stb_mac}/sma_mac/{sma_mac}/reg_id/{reg_id}/app_type/{app_type}/pushagent_yn/{pushagent_yn}", method=RequestMethod.POST)
//	//@RequestMapping(value="/RegistrationID/**", method=RequestMethod.POST)
//	@RequestMapping(value="/v1/push/**", method=RequestMethod.POST)
//	public RegistrationIDVO Restful_addRegistrationID(
//			HttpServletRequest request,
//			HttpServletResponse response
////			,@PathVariable String sa_id
////			,@PathVariable String stb_mac
////			,@PathVariable String sma_mac
////			,@PathVariable String reg_id
////			,@PathVariable String app_type
////			,@PathVariable String pushagent_yn
//			){
//		
//		//#########################[LOG SET]##########################
//		Log log_logger = LogFactory.getLog("OPENAPI_RegistrationID");
//		long startTime = System.currentTimeMillis();
//		String ip = request.getRemoteAddr();
//		String url = request.getRequestURI();
//		try{
//			url = "/v1/push/";
//		}catch(Exception e){
//		}
//		String method = request.getMethod();
//		//#########################[//LOG SET]##########################
//		String access_key = null;
//		String cp_id = null;
//		
//		
//		String sa_id = "";
//		String stb_mac = "";
//		String sma_mac = "";
//		String reg_id = "";
//		String app_type = "";
//		String pushagent_yn = "";
//		
//		RegistrationIDVO result = new RegistrationIDVO();
//		
//		String uri = request.getRequestURI();
//		try {
//			HashMap<String, String> paramMap = GlobalCom.getRestfulParamMap(4,uri);
//			sa_id 		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "sa_id"));
//			stb_mac		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "stb_mac"));
//			sma_mac 	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "sma_mac"));
//			reg_id 	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "reg_id"));
//			app_type	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "app_type"));
//			pushagent_yn	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "pushagent_yn"));
//			
//			access_key = GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "access_key"));
//			cp_id = GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "cp_id"));
//		
//			//#########################[LOG START]##########################
//			log_logger.info("["+ip+"] ["+url+"]["+method+"][START] - ["+stb_mac+"]["+sa_id+"]["+app_type+"]["+access_key+"]["+cp_id+"]");
//			//#########################[//LOG START]########################
//	
//			//access_key 인증 허용 체크
//			String authResult = GlobalCom.isNull(AuthenticationCommon.isAuthenticationMap(access_key,cp_id,"POST","/v1/push"));
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
//			RegistrationIDParamVO pvo = new RegistrationIDParamVO();
//			pvo.setSa_id(sa_id);
//			pvo.setStb_mac(stb_mac);
//			pvo.setSma_mac(sma_mac);
//			pvo.setReg_id(reg_id);
//			pvo.setApp_type(app_type);
//			pvo.setPushagent_yn(pushagent_yn);
//			
//			//reg_id = URLDecoder.decode(reg_id, "UTF-8");
//		
//			validateAddRegistrationID(pvo);
//			
//			pairService.addRegistrationID(pvo);
//			
//			result.setFlag(SmartUXProperties.getProperty("flag.success"));
//			result.setMessage(SmartUXProperties.getProperty("message.success"));
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
	

	@RequestMapping(value="/v1/push", method=RequestMethod.POST)
	public RegistrationIDVO Restful_addRegistrationID(
			 HttpServletRequest request
			,HttpServletResponse response
			,@RequestParam(value="sa_id", required=false, defaultValue="") String sa_id
			,@RequestParam(value="stb_mac", required=false, defaultValue="") String stb_mac
			,@RequestParam(value="sma_mac", required=false, defaultValue="") String sma_mac
			,@RequestParam(value="reg_id", required=false, defaultValue="") String reg_id
			,@RequestParam(value="app_type", required=false, defaultValue="") String app_type
			,@RequestParam(value="pushagent_yn", required=false, defaultValue="") String pushagent_yn
			,@RequestParam(value="access_key", required=false, defaultValue="") String access_key
			,@RequestParam(value="cp_id", required=false, defaultValue="") String cp_id
		
			) throws Exception{
		
		SmartUXException exception = new SmartUXException();
		
		//#########################[LOG SET]##########################
		Log log_logger = LogFactory.getLog("OPENAPI_RegistrationID");
		long startTime = System.currentTimeMillis();
		String ip = request.getRemoteAddr();
		String url = request.getRequestURI();
		try{
			url = "/v1/push/";
		}catch(Exception e){
		}
		String method = request.getMethod();
		//#########################[//LOG SET]##########################
//		String access_key = null;
//		String cp_id = null;
//		
//		
//		String sa_id = "";
//		String stb_mac = "";
//		String sma_mac = "";
//		String reg_id = "";
//		String app_type = "";
//		String pushagent_yn = "";
		
		RegistrationIDVO result = new RegistrationIDVO();
		
		String uri = request.getRequestURI();
		try {
//			HashMap<String, String> paramMap = GlobalCom.getRestfulParamMap(4,uri);
//			sa_id 		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "sa_id"));
//			stb_mac		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "stb_mac"));
//			sma_mac 	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "sma_mac"));
//			reg_id 	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "reg_id"));
//			app_type	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "app_type"));
//			pushagent_yn	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "pushagent_yn"));
//			
//			access_key = GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "access_key"));
//			cp_id = GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "cp_id"));
		
			//#########################[LOG START]##########################
			log_logger.info("["+ip+"] ["+url+"]["+method+"][START] - ["+stb_mac+"]["+sa_id+"]["+app_type+"]["+access_key+"]["+cp_id+"]");
			//#########################[//LOG START]########################
	
			//access_key 인증 허용 체크
			String authResult = GlobalCom.isNull(AuthenticationCommon.isAuthenticationMap(access_key,cp_id,"POST","/v1/push"));
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
			
			RegistrationIDParamVO pvo = new RegistrationIDParamVO();
			pvo.setSa_id(sa_id);
			pvo.setStb_mac(stb_mac);
			pvo.setSma_mac(sma_mac);
			pvo.setReg_id(reg_id);
			pvo.setApp_type(app_type);
			pvo.setPushagent_yn(pushagent_yn);
			
			//reg_id = URLDecoder.decode(reg_id, "UTF-8");
		
			validateAddRegistrationID(pvo);
			
			pairService.addRegistrationID(pvo);
			
			result.setFlag(SmartUXProperties.getProperty("flag.success"));
			result.setMessage(SmartUXProperties.getProperty("message.success"));
		
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
	 * RegistrationID 삭제 API
	 * @param sa_id				셋탑 가입 번호
	 * @param stb_mac			셋탑 맥주소
	 * @param sma_mac			모바일앱 맥주소
	 * @param reg_id			RegistrationID
	 * @param app_type			어플타입
	 * @param pushagent_yn		모바일앱 자사/타사 구분
	 * @return					응답 VO 객체(결과코드, 결과메세지)
	 */
	@RequestMapping(value="/removeRegistrationID")
	public RegistrationIDVO API_removeRegistrationID(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value="sa_id", required=false, defaultValue="") String sa_id
			,@RequestParam(value="stb_mac", required=false, defaultValue="") String stb_mac
			,@RequestParam(value="sma_mac", required=false, defaultValue="") String sma_mac
			,@RequestParam(value="reg_id", required=false, defaultValue="") String reg_id
			,@RequestParam(value="app_type", required=false, defaultValue="") String app_type
			,@RequestParam(value="pushagent_yn", required=false, defaultValue="") String pushagent_yn
			){
		
		//#########[LOG START]#########
		Log log_logger = LogFactory.getLog("removeRegistrationID");
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
		
		
		RegistrationIDVO result = new RegistrationIDVO();
		
		try{
			RegistrationIDParamVO pvo = new RegistrationIDParamVO();
			pvo.setSa_id(sa_id);
			pvo.setStb_mac(stb_mac);
			pvo.setSma_mac(sma_mac);
			pvo.setReg_id(reg_id);
			pvo.setApp_type(app_type);
			pvo.setPushagent_yn(pushagent_yn);
			
			//reg_id = URLDecoder.decode(reg_id, "UTF-8");
		
			validateAddRegistrationID(pvo);
			
			pairService.removeRegistrationID(pvo);
			
			result.setFlag(SmartUXProperties.getProperty("flag.success"));
			result.setMessage(SmartUXProperties.getProperty("message.success"));
			
		}catch(Exception e){
			//logger.error("removeRegistrationID "+e.getClass().getName());
			//logger.error("removeRegistrationID "+e.getMessage());
			//logger.error("[removeRegistrationID]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			log_logger.error("["+log_ip+"] ["+log_url+"]["+log_info_mac+"]["+log_info_id+"]["+log_info_app_type+"]"+"[removeRegistrationID]["+e.getClass().getName()+"]["+e.getMessage()+"]");
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
		log_logger.info("["+log_ip+"] ["+log_url+"][END] ["+(timeStr)+" sec] - ["+log_info_mac+"]["+log_info_id+"]["+log_info_app_type+"] ["+result.getFlag()+"]");
		
		return result;
	}
	
//	//@RequestMapping(value="/RegistrationID/sa_id/{sa_id}/stb_mac/{stb_mac}/sma_mac/{sma_mac}/reg_id/{reg_id}/app_type/{app_type}/pushagent_yn/{pushagent_yn}", method=RequestMethod.DELETE)
//	@RequestMapping(value="/v1/push/**", method=RequestMethod.DELETE)
//	public RegistrationIDVO Restful_removeRegistrationID(
//			HttpServletRequest request,
//			HttpServletResponse response
////			,@PathVariable String sa_id
////			,@PathVariable String stb_mac
////			,@PathVariable String sma_mac
////			,@PathVariable String reg_id
////			,@PathVariable String app_type
////			,@PathVariable String pushagent_yn
//			){
//		
//		//#########################[LOG SET]##########################
//		Log log_logger = LogFactory.getLog("OPENAPI_RegistrationID");
//		long startTime = System.currentTimeMillis();
//		String ip = request.getRemoteAddr();
//		String url = request.getRequestURI();
//		try{
//			url = "/v1/push/";
//		}catch(Exception e){
//		}
//		String method = request.getMethod();
//		//#########################[//LOG SET]##########################
//		String access_key = null;
//		String cp_id = null;
//		
//		String sa_id = "";
//		String stb_mac = "";
//		String sma_mac = "";
//		String reg_id = "";
//		String app_type = "";
//		String pushagent_yn = "";
//		
//		RegistrationIDVO result = new RegistrationIDVO();
//		
//		String uri = request.getRequestURI();
//		try {
//			HashMap<String, String> paramMap = GlobalCom.getRestfulParamMap(4,uri);
//			sa_id 		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "sa_id"));
//			stb_mac		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "stb_mac"));
//			sma_mac 	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "sma_mac"));
//			reg_id 	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "reg_id"));
//			app_type	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "app_type"));
//			pushagent_yn	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "pushagent_yn"));
//	
//			access_key = GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "access_key"));
//			cp_id = GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "cp_id"));
//		
//			//#########################[LOG START]##########################
//			log_logger.info("["+ip+"] ["+url+"]["+method+"][START] - ["+stb_mac+"]["+sa_id+"]["+app_type+"]["+access_key+"]["+cp_id+"]");
//			//#########################[//LOG START]########################
//	
//			//access_key 인증 허용 체크
//			String authResult = GlobalCom.isNull(AuthenticationCommon.isAuthenticationMap(access_key,cp_id,"DELETE","/v1/push"));
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
//			RegistrationIDParamVO pvo = new RegistrationIDParamVO();
//			pvo.setSa_id(sa_id);
//			pvo.setStb_mac(stb_mac);
//			pvo.setSma_mac(sma_mac);
//			pvo.setReg_id(reg_id);
//			pvo.setApp_type(app_type);
//			pvo.setPushagent_yn(pushagent_yn);
//			
//			//reg_id = URLDecoder.decode(reg_id, "UTF-8");
//		
//			validateAddRegistrationID(pvo);
//			
//			pairService.removeRegistrationID(pvo);
//			
//			result.setFlag(SmartUXProperties.getProperty("flag.success"));
//			result.setMessage(SmartUXProperties.getProperty("message.success"));
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
	

	@RequestMapping(value="/v1/push", method=RequestMethod.DELETE)
	public RegistrationIDVO Restful_removeRegistrationID(
			HttpServletRequest request
			,HttpServletResponse response
			,@RequestParam(value="sa_id", required=false, defaultValue="") String sa_id
			,@RequestParam(value="stb_mac", required=false, defaultValue="") String stb_mac
			,@RequestParam(value="sma_mac", required=false, defaultValue="") String sma_mac
			,@RequestParam(value="reg_id", required=false, defaultValue="") String reg_id
			,@RequestParam(value="app_type", required=false, defaultValue="") String app_type
			,@RequestParam(value="pushagent_yn", required=false, defaultValue="") String pushagent_yn
			,@RequestParam(value="access_key", required=false, defaultValue="") String access_key
			,@RequestParam(value="cp_id", required=false, defaultValue="") String cp_id
			) throws Exception{
		
		SmartUXException exception = new SmartUXException();
		
		//#########################[LOG SET]##########################
		Log log_logger = LogFactory.getLog("OPENAPI_RegistrationID");
		long startTime = System.currentTimeMillis();
		String ip = request.getRemoteAddr();
		String url = request.getRequestURI();
		try{
			url = "/v1/push/";
		}catch(Exception e){
		}
		String method = request.getMethod();
		//#########################[//LOG SET]##########################
//		String access_key = null;
//		String cp_id = null;
		
//		String sa_id = "";
//		String stb_mac = "";
//		String sma_mac = "";
//		String reg_id = "";
//		String app_type = "";
//		String pushagent_yn = "";
		
		RegistrationIDVO result = new RegistrationIDVO();
		
//		String uri = request.getRequestURI();
		try {
//			HashMap<String, String> paramMap = GlobalCom.getRestfulParamMap(4,uri);
//			sa_id 		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "sa_id"));
//			stb_mac		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "stb_mac"));
//			sma_mac 	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "sma_mac"));
//			reg_id 	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "reg_id"));
//			app_type	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "app_type"));
//			pushagent_yn	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "pushagent_yn"));
//	
//			access_key = GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "access_key"));
//			cp_id = GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "cp_id"));
		
			//#########################[LOG START]##########################
			log_logger.info("["+ip+"] ["+url+"]["+method+"][START] - ["+stb_mac+"]["+sa_id+"]["+app_type+"]["+access_key+"]["+cp_id+"]");
			//#########################[//LOG START]########################
	
			//access_key 인증 허용 체크
			String authResult = GlobalCom.isNull(AuthenticationCommon.isAuthenticationMap(access_key,cp_id,"DELETE","/v1/push"));
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
	
			RegistrationIDParamVO pvo = new RegistrationIDParamVO();
			pvo.setSa_id(sa_id);
			pvo.setStb_mac(stb_mac);
			pvo.setSma_mac(sma_mac);
			pvo.setReg_id(reg_id);
			pvo.setApp_type(app_type);
			pvo.setPushagent_yn(pushagent_yn);
			
			//reg_id = URLDecoder.decode(reg_id, "UTF-8");
		
			validateAddRegistrationID(pvo);
			
			pairService.removeRegistrationID(pvo);
			
			result.setFlag(SmartUXProperties.getProperty("flag.success"));
			result.setMessage(SmartUXProperties.getProperty("message.success"));
			
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
	
//	/**
//	 * Push 알림 설정 API
//	 * @param sa_id			셋탑 가입 번호
//	 * @param stb_mac		셋탑 맥주소
//	 * @param stb_reg_id	셋탑 REG_ID
//	 * @param ctn			모바일앱 번호
//	 * @param ctn_reg_id	모바일앱 REG_ID
//	 * @param app_type		어플타입
//	 * @param is_push		알림 설정값
//	 * @return	Push 요청에 대한 결과코드,결과메세지
//	 */
//	@RequestMapping(value="/setPushNoti")
//	public PushResultVO setPushNoti(
//			 @RequestParam(value="sa_id", required=false, defaultValue="") String sa_id
//			,@RequestParam(value="stb_mac", required=false, defaultValue="") String stb_mac
//			,@RequestParam(value="ctn", required=false, defaultValue="") String ctn
//			,@RequestParam(value="reg_id", required=false, defaultValue="") String reg_id
//			,@RequestParam(value="app_type", required=false, defaultValue="") String app_type
//			,@RequestParam(value="is_push", required=false, defaultValue="") String is_push
//			){
//		
//		
//		ReservedProgramParamVO rVO = new ReservedProgramParamVO();
//		PushResultVO prVO = new PushResultVO();
//		
//		try{
//			rVO.setSa_id(sa_id);
//			rVO.setStb_mac(stb_mac);
//			rVO.setCtn(ctn);
//			rVO.setApp_type(app_type);
//			rVO.setIs_push(is_push); 
//			
//			reg_id = URLDecoder.decode(reg_id, "UTF-8");
//			
//			if(reg_id.indexOf(GlobalCom.colsep) != -1){
//				String[] _reg_id = reg_id.split(GlobalCom.splitcolsep);
//				for(int i=0;i<_reg_id.length;i++){
//					//_reg_id[i] = URLDecoder.decode(_reg_id[i], "UTF-8");
//				}
//				rVO.setReg_id(_reg_id);
//			}else{
//				String[] _reg_id = {reg_id};
//				//_reg_id[0] = URLDecoder.decode(_reg_id[0], "UTF-8");
//				rVO.setReg_id(_reg_id);			
//			}
//			
//			logger.debug("setPushNoti sa_id : " + rVO.getSa_id());
//			logger.debug("setPushNoti stb_mac :" + rVO.getStb_mac());
//			logger.debug("setPushNoti ctn :" + rVO.getCtn());
//			logger.debug("setPushNoti reg_id param :" + reg_id);
//			logger.debug("setPushNoti reg_id :" + rVO.getReg_id()[0]);
//			logger.debug("setPushNoti reg_id.size : " + rVO.getReg_id().length);
//			logger.debug("setPushNoti app_type :" + rVO.getApp_type());
//			logger.debug("setPushNoti is_push :" + rVO.getIs_push());
//		
//			//=====유효성 검사=====
//			validateSetPushNoti(rVO);
//			
//			prVO = reservedService.setPushNoti(rVO);
//			
//		}catch(Exception e){
//			logger.error("setPushNoti "+e.getClass().getName());
//			logger.error("setPushNoti "+e.getMessage());
//			ExceptionHandler handler = new ExceptionHandler(e);
//			prVO.setFlag(handler.getFlag());
//			prVO.setMessage(handler.getMessage());
//		}
//		
//		return prVO;
//	}
	
	

	/**
	 * 시청 예약 추가 API
	 * @param sa_id				셋탑 가입 번호
	 * @param stb_mac			셋탑 맥주소
	 * @param stb_reg_id		셋탑 REG_ID
	 * @param sma_mac			모바일앱 맥주소
	 * @param ctn_reg_id		모바일앱 REG_ID
	 * @param app_type			어플타입
	 * @param service_id		서비스 ID
	 * @param channel_no		채널 번호
	 * @param channel_name		채널 이름
	 * @param program_id		프로그램 ID
	 * @param program_name		프로그램 명
	 * @param program_info		프로그램 연결
	 * @param defin_flag		프로그램 화질
	 * @param program_stime		프로그램 시작시간
	 * @return	Push 요청에 대한 결과코드,결과메세지
	 */
	@RequestMapping(value="/addReservedProgram")
	public PushResultVO API_addReservedProgram(
			HttpServletRequest request,
			HttpServletResponse response,
			 @RequestParam(value="sa_id", required=false, defaultValue="") String sa_id
			,@RequestParam(value="stb_mac", required=false, defaultValue="") String stb_mac
			,@RequestParam(value="sma_mac", required=false, defaultValue="") String sma_mac
			,@RequestParam(value="app_type", required=false, defaultValue="") String app_type
			,@RequestParam(value="service_id", required=false, defaultValue="") String service_id
			,@RequestParam(value="channel_no", required=false, defaultValue="") String channel_no
			,@RequestParam(value="channel_name", required=false, defaultValue="") String channel_name
			,@RequestParam(value="program_id", required=false, defaultValue="") String program_id
			,@RequestParam(value="program_name", required=false, defaultValue="") String program_name
			,@RequestParam(value="program_info", required=false, defaultValue="") String program_info
			,@RequestParam(value="defin_flag", required=false, defaultValue="") String defin_flag
			,@RequestParam(value="program_stime", required=false, defaultValue="") String program_stime
			,@RequestParam(value="repeat_day", required=false, defaultValue="") String repeat_day
			){
		
		//#########[LOG START]#########
		Log log_logger = LogFactory.getLog("addReservedProgram");
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
		
		
		ReservedProgramParamVO rVO = new ReservedProgramParamVO();
		rVO.setSa_id(sa_id);
		rVO.setStb_mac(stb_mac);
		rVO.setSma_mac(sma_mac);
		rVO.setApp_type(app_type);
		rVO.setService_id(service_id);
		rVO.setChannel_no(channel_no);
		rVO.setChannel_name(channel_name);
		rVO.setProgram_id(program_id);
		rVO.setProgram_name(program_name);
		rVO.setProgram_info(program_info);
		rVO.setDefin_flag(defin_flag);
		rVO.setProgram_stime(program_stime);
		rVO.setRepeat_day(repeat_day);
		
		PushResultVO prVO = new PushResultVO();
				
		try{
			//=====유효성 검사=====
			rVO = validateAddReservedProgram(rVO);	//배열변수를 유효성에서 데이터를 세팅한다(split을 여러번 하지 않게 하기 위하여)
			
//			for(int i=0;i<rVO.getArr_channel_name().length;i++){
//				rVO.setArr_channel_name(URLDecoder.decode(rVO.getArr_channel_name()[i], "UTF-8"),i);
//			}
//			for(int i=0;i<rVO.getArr_program_name().length;i++){
//				rVO.setArr_program_name(URLDecoder.decode(rVO.getArr_program_name()[i], "UTF-8"),i);
//			}
			
			prVO = reservedService.addReservedProgram(rVO);
			
		}catch(Exception e){
			//logger.error("addReservedProgram "+e.getClass().getName());
			//logger.error("addReservedProgram "+e.getMessage());
			//logger.error("[addReservedProgram]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			log_logger.error("["+log_ip+"] ["+log_url+"]["+log_info_mac+"]["+log_info_id+"]["+log_info_app_type+"]"+"[addReservedProgram]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			ExceptionHandler handler = new ExceptionHandler(e);
			prVO.setFlag(handler.getFlag());
			prVO.setMessage(handler.getMessage());
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
		log_logger.info("["+log_ip+"] ["+log_url+"][END] ["+(timeStr)+" sec] - ["+log_info_mac+"]["+log_info_id+"]["+log_info_app_type+"] ["+prVO.getFlag()+"]");
		
		return prVO;
		
	}// end addReservedProgram
	
	
//	//@RequestMapping(value="/ReservedProgram/sa_id/{sa_id}/stb_mac/{stb_mac}/sma_mac/{sma_mac}/app_type/{app_type}/service_id/{service_id}/channel_no/{channel_no}/channel_name/{channel_name}/program_id/{program_id}/program_name/{program_name}/program_info/{program_info}/defin_flag/{defin_flag}/program_stime/{program_stime}/repeat_day/{repeat_day}", method=RequestMethod.POST)
//	//@RequestMapping(value="/ReservedProgram/**", method=RequestMethod.POST)
//	@RequestMapping(value="/v1/reservation/**", method=RequestMethod.POST)
//	public PushResultVO Restful_addReservedProgram(
//			HttpServletRequest request,
//			HttpServletResponse response
////			,@PathVariable String sa_id
////			,@PathVariable String stb_mac
////			,@PathVariable String sma_mac
////			,@PathVariable String app_type
////			,@PathVariable String service_id
////			,@PathVariable String channel_no
////			,@PathVariable String channel_name
////			,@PathVariable String program_id
////			,@PathVariable String program_name
////			,@PathVariable String program_info
////			,@PathVariable String defin_flag
////			,@PathVariable String program_stime
////			,@PathVariable String repeat_day
//			){
//		
//		//#########################[LOG SET]##########################
//		Log log_logger = LogFactory.getLog("OPENAPI_ReservedProgram");
//		long startTime = System.currentTimeMillis();
//		String ip = request.getRemoteAddr();
//		String url = request.getRequestURI();
//		try{
//			url = "/v1/reservation/";
//		}catch(Exception e){
//		}
//		String method = request.getMethod();
//		//#########################[//LOG SET]##########################
//		String access_key = null;
//		String cp_id = null;
//		
//		String sa_id = "";
//		String stb_mac = "";
//		String sma_mac = "";
//		String app_type = "";
//		String service_id = "";
//		String channel_no = "";
//		String channel_name = "";
//		String program_id = "";
//		String program_name = "";
//		String program_info = "";
//		String defin_flag = "";
//		String program_stime = "";
//		String repeat_day = "";
//		
//		PushResultVO prVO = new PushResultVO();
//		
//		String uri = request.getRequestURI();
//		try {
//			HashMap<String, String> paramMap = GlobalCom.getRestfulParamMap(4,uri);
//			sa_id 		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "sa_id"));
//			stb_mac		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "stb_mac"));
//			sma_mac		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "sma_mac"));
//			app_type		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "app_type"));
//			service_id		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "service_id"));
//			channel_no		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "channel_no"));
//			channel_name		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "channel_name"));
//			program_id		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "program_id"));
//			program_name		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "program_name"));
//			program_info		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "program_info"));
//			defin_flag		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "defin_flag"));
//			program_stime		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "program_stime"));
//			repeat_day		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "repeat_day"));
//			
//			access_key = GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "access_key"));
//			cp_id = GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "cp_id"));
//		
//			//#########################[LOG START]##########################
//			log_logger.info("["+ip+"] ["+url+"]["+method+"][START] - ["+stb_mac+"]["+sa_id+"]["+app_type+"]["+access_key+"]["+cp_id+"]");
//			//#########################[//LOG START]########################
//	
//			//access_key 인증 허용 체크
//			String authResult = GlobalCom.isNull(AuthenticationCommon.isAuthenticationMap(access_key,cp_id,"POST","/v1/reservation"));
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
//			ReservedProgramParamVO rVO = new ReservedProgramParamVO();
//			rVO.setSa_id(sa_id);
//			rVO.setStb_mac(stb_mac);
//			rVO.setSma_mac(sma_mac);
//			rVO.setApp_type(app_type);
//			rVO.setService_id(service_id);
//			rVO.setChannel_no(channel_no);
//			rVO.setChannel_name(channel_name);
//			rVO.setProgram_id(program_id);
//			rVO.setProgram_name(program_name);
//			rVO.setProgram_info(program_info);
//			rVO.setDefin_flag(defin_flag);
//			rVO.setProgram_stime(program_stime);
//			rVO.setRepeat_day(repeat_day);
//		
//		
//				
//			//=====유효성 검사=====
//			rVO = validateAddReservedProgram(rVO);	//배열변수를 유효성에서 데이터를 세팅한다(split을 여러번 하지 않게 하기 위하여)
//			
////			for(int i=0;i<rVO.getArr_channel_name().length;i++){
////				rVO.setArr_channel_name(URLDecoder.decode(rVO.getArr_channel_name()[i], "UTF-8"),i);
////			}
////			for(int i=0;i<rVO.getArr_program_name().length;i++){
////				rVO.setArr_program_name(URLDecoder.decode(rVO.getArr_program_name()[i], "UTF-8"),i);
////			}
//			
//			prVO = reservedService.addReservedProgram(rVO);
//			
//		}catch(Exception e){
//			log_logger.error("["+ip+"] ["+url+"]["+stb_mac+"]["+sa_id+"]["+app_type+"]["+access_key+"]["+cp_id+"]["+e.getClass().getName()+"]["+e.getMessage()+"]");
//			ExceptionHandler handler = new ExceptionHandler(e);
//			prVO.setFlag(handler.getFlag());
//			prVO.setMessage(handler.getMessage());
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
//		log_logger.info("["+ip+"] ["+url+"]["+method+"][END] ["+(timeStr)+" sec] - ["+stb_mac+"]["+sa_id+"]["+app_type+"]["+access_key+"]["+cp_id+"] ["+prVO.getFlag()+"]");
//		//############################[//LOG END]##########################
//		
//		return prVO;
//		
//	}// end addReservedProgram
	
	@RequestMapping(value="/v1/reservation", method=RequestMethod.POST)
	public PushResultVO Restful_addReservedProgram(
			HttpServletRequest request
			,HttpServletResponse response
			,@RequestParam(value="sa_id", required=false, defaultValue="") String sa_id
			,@RequestParam(value="stb_mac", required=false, defaultValue="") String stb_mac
			,@RequestParam(value="sma_mac", required=false, defaultValue="") String sma_mac
			,@RequestParam(value="app_type", required=false, defaultValue="") String app_type
			,@RequestParam(value="service_id", required=false, defaultValue="") String service_id
			,@RequestParam(value="channel_no", required=false, defaultValue="") String channel_no
			,@RequestParam(value="channel_name", required=false, defaultValue="") String channel_name
			,@RequestParam(value="program_id", required=false, defaultValue="") String program_id
			,@RequestParam(value="program_name", required=false, defaultValue="") String program_name
			,@RequestParam(value="program_info", required=false, defaultValue="") String program_info
			,@RequestParam(value="defin_flag", required=false, defaultValue="") String defin_flag
			,@RequestParam(value="program_stime", required=false, defaultValue="") String program_stime
			,@RequestParam(value="repeat_day", required=false, defaultValue="") String repeat_day
			,@RequestParam(value="access_key", required=false, defaultValue="") String access_key
			,@RequestParam(value="cp_id", required=false, defaultValue="") String cp_id
			) throws Exception{
		
		SmartUXException exception = new SmartUXException();
		
		//#########################[LOG SET]##########################
		Log log_logger = LogFactory.getLog("OPENAPI_ReservedProgram");
		long startTime = System.currentTimeMillis();
		String ip = request.getRemoteAddr();
		String url = request.getRequestURI();
		try{
			url = "/v1/reservation/";
		}catch(Exception e){
		}
		String method = request.getMethod();
		//#########################[//LOG SET]##########################
//		String access_key = null;
//		String cp_id = null;
//		
//		String sa_id = "";
//		String stb_mac = "";
//		String sma_mac = "";
//		String app_type = "";
//		String service_id = "";
//		String channel_no = "";
//		String channel_name = "";
//		String program_id = "";
//		String program_name = "";
//		String program_info = "";
//		String defin_flag = "";
//		String program_stime = "";
//		String repeat_day = "";
		
		PushResultVO prVO = new PushResultVO();
		
		String uri = request.getRequestURI();
		try {
//			HashMap<String, String> paramMap = GlobalCom.getRestfulParamMap(4,uri);
//			sa_id 		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "sa_id"));
//			stb_mac		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "stb_mac"));
//			sma_mac		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "sma_mac"));
//			app_type		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "app_type"));
//			service_id		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "service_id"));
//			channel_no		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "channel_no"));
//			channel_name		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "channel_name"));
//			program_id		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "program_id"));
//			program_name		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "program_name"));
//			program_info		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "program_info"));
//			defin_flag		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "defin_flag"));
//			program_stime		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "program_stime"));
//			repeat_day		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "repeat_day"));
//			
//			access_key = GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "access_key"));
//			cp_id = GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "cp_id"));
		
			//#########################[LOG START]##########################
			log_logger.info("["+ip+"] ["+url+"]["+method+"][START] - ["+stb_mac+"]["+sa_id+"]["+app_type+"]["+access_key+"]["+cp_id+"]");
			//#########################[//LOG START]########################
	
			//access_key 인증 허용 체크
			String authResult = GlobalCom.isNull(AuthenticationCommon.isAuthenticationMap(access_key,cp_id,"POST","/v1/reservation"));
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
			
		
			ReservedProgramParamVO rVO = new ReservedProgramParamVO();
			rVO.setSa_id(sa_id);
			rVO.setStb_mac(stb_mac);
			rVO.setSma_mac(sma_mac);
			rVO.setApp_type(app_type);
			rVO.setService_id(service_id);
			rVO.setChannel_no(channel_no);
			rVO.setChannel_name(channel_name);
			rVO.setProgram_id(program_id);
			rVO.setProgram_name(program_name);
			rVO.setProgram_info(program_info);
			rVO.setDefin_flag(defin_flag);
			rVO.setProgram_stime(program_stime);
			rVO.setRepeat_day(repeat_day);
		
		
				
			//=====유효성 검사=====
			rVO = validateAddReservedProgram(rVO);	//배열변수를 유효성에서 데이터를 세팅한다(split을 여러번 하지 않게 하기 위하여)
			
//				for(int i=0;i<rVO.getArr_channel_name().length;i++){
//					rVO.setArr_channel_name(URLDecoder.decode(rVO.getArr_channel_name()[i], "UTF-8"),i);
//				}
//				for(int i=0;i<rVO.getArr_program_name().length;i++){
//					rVO.setArr_program_name(URLDecoder.decode(rVO.getArr_program_name()[i], "UTF-8"),i);
//				}
			
			prVO = reservedService.addReservedProgram(rVO);
			
			
		} catch(SmartUXException e){
			log_logger.error("["+ip+"] ["+url+"]["+stb_mac+"]["+sa_id+"]["+app_type+"]["+access_key+"]["+cp_id+"]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			throw e;
		}catch(Exception e){
			log_logger.error("["+ip+"] ["+url+"]["+stb_mac+"]["+sa_id+"]["+app_type+"]["+access_key+"]["+cp_id+"]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			ExceptionHandler handler = new ExceptionHandler(e);
			prVO.setFlag(handler.getFlag());
			prVO.setMessage(handler.getMessage());
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
		log_logger.info("["+ip+"] ["+url+"]["+method+"][END] ["+(timeStr)+" sec] - ["+stb_mac+"]["+sa_id+"]["+app_type+"]["+access_key+"]["+cp_id+"] ["+prVO.getFlag()+"]");
		//############################[//LOG END]##########################
		
		return prVO;
		
	}// end addReservedProgram
	
	
	/**
	 * 시청 예약 목록 조회 API
	 * @param sa_id				셋탑 가입 번호
	 * @param stb_mac			셋탑 맥주소
	 * @param stb_reg_id		셋탑 REG_ID
	 * @param sma_mac			모바일앱 맥주소
	 * @param ctn_reg_id		모바일앱 REG_ID
	 * @param app_type			어플타입
	 * @param start_num			검색 시작 인덱스(-1이면 req_count 값을 무시하고 전체를 검색한다)
	 * @param req_count			검색 레코드 갯수
	 * @return					성공여부 코드값과 결과 메시지와 검색된 시청 예약 정보가 들어있는 List 객체가 들어가 있는 Result 객체
	 */
	@RequestMapping(value="/getReservedProgramList")
	public Result<ReservedProgramVO> API_getReservedProgramList(
			HttpServletRequest request,
			HttpServletResponse response,
			 @RequestParam(value="sa_id", required=false, defaultValue="") String sa_id
			,@RequestParam(value="stb_mac", required=false, defaultValue="") String stb_mac
			,@RequestParam(value="sma_mac", required=false, defaultValue="") String sma_mac
			,@RequestParam(value="app_type", required=false, defaultValue="") String app_type
			,@RequestParam(value="start_num", required=false, defaultValue="") String start_num
			,@RequestParam(value="req_count", required=false, defaultValue="") String req_count
			){
		
		//#########[LOG START]#########
		Log log_logger = LogFactory.getLog("getReservedProgramList");
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
		
		ReservedProgramParamVO rVO = new ReservedProgramParamVO();
		rVO.setSa_id(sa_id);
		rVO.setStb_mac(stb_mac);
		rVO.setSma_mac(sma_mac);
		rVO.setApp_type(app_type);
		rVO.setStart_num(start_num);
		rVO.setReq_count(req_count);
		
		Result<ReservedProgramVO> result = new Result<ReservedProgramVO>();
		
		try {
			//=====유효성 검사=====
			validateGetReservedProgramList(rVO);

			result.setTotal_count(reservedService.getReservedProgramListTotalCnt(rVO));
			
			if(!rVO.getStart_num().equals("0")){
				result.setRecordset(reservedService.getReservedProgramList(rVO));
			}
			
			result.setFlag(SmartUXProperties.getProperty("flag.success"));
			result.setMessage(SmartUXProperties.getProperty("message.success"));
			
		}catch(Exception e){
			//logger.error("getReservedProgramList "+e.getClass().getName());
			//logger.error("getReservedProgramList "+e.getMessage());
			//logger.error("[getReservedProgramList]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			log_logger.error("["+log_ip+"] ["+log_url+"]["+log_info_mac+"]["+log_info_id+"]["+log_info_app_type+"]"+"[getReservedProgramList]["+e.getClass().getName()+"]["+e.getMessage()+"]");
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
		log_logger.info("["+log_ip+"] ["+log_url+"][END] ["+(timeStr)+" sec] - ["+log_info_mac+"]["+log_info_id+"]["+log_info_app_type+"] ["+result.getFlag()+"]");
		
		return result;
	}
	
//	//@RequestMapping(value="/ReservedProgram/sa_id/{sa_id}/stb_mac/{stb_mac}/sma_mac/{sma_mac}/app_type/{app_type}/start_num/{start_num}/req_count/{req_count}", method=RequestMethod.GET)
//	@RequestMapping(value="/v1/reservation/**", method=RequestMethod.GET)
//	public Result<ReservedProgramVO> Restful_getReservedProgramList(
//			HttpServletRequest request,
//			HttpServletResponse response
////			,@PathVariable String sa_id
////			,@PathVariable String stb_mac
////			,@PathVariable String sma_mac
////			,@PathVariable String app_type
////			,@PathVariable String start_num
////			,@PathVariable String req_count
//			){
//		
//		//#########################[LOG SET]##########################
//		Log log_logger = LogFactory.getLog("OPENAPI_ReservedProgram");
//		long startTime = System.currentTimeMillis();
//		String ip = request.getRemoteAddr();
//		String url = request.getRequestURI();
//		try{
//			url = "/v1/reservation/";
//		}catch(Exception e){
//		}
//		String method = request.getMethod();
//		//#########################[//LOG SET]##########################
//		String access_key = null;
//		String cp_id = null;
//		
//		
//		String sa_id = "";
//		String stb_mac = "";
//		String sma_mac = "";
//		String app_type = "";
//		String start_num = "";
//		String req_count = "";
//				
//		Result<ReservedProgramVO> result = new Result<ReservedProgramVO>();
//		
//		String uri = request.getRequestURI();
//		try {
//			HashMap<String, String> paramMap = GlobalCom.getRestfulParamMap(4,uri);
//			sa_id 		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "sa_id"));
//			stb_mac		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "stb_mac"));
//			sma_mac		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "sma_mac"));
//			app_type 	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "app_type"));
//			start_num 	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "start_num"));
//			req_count	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "req_count"));
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
//			String authResult = GlobalCom.isNull(AuthenticationCommon.isAuthenticationMap(access_key,cp_id,"GET","/v1/reservation"));
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
//			ReservedProgramParamVO rVO = new ReservedProgramParamVO();
//			rVO.setSa_id(sa_id);
//			rVO.setStb_mac(stb_mac);
//			rVO.setSma_mac(sma_mac);
//			rVO.setApp_type(app_type);
//			rVO.setStart_num(start_num);
//			rVO.setReq_count(req_count);
//			
//		
//		
//			//=====유효성 검사=====
//			validateGetReservedProgramList(rVO);
//
//			result.setTotal_count(reservedService.getReservedProgramListTotalCnt(rVO));
//			
//			if(!rVO.getStart_num().equals("0")){
//				result.setRecordset(reservedService.getReservedProgramList(rVO));
//			}
//			
//			result.setFlag(SmartUXProperties.getProperty("flag.success"));
//			result.setMessage(SmartUXProperties.getProperty("message.success"));
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
	
	@RequestMapping(value="/v1/reservation", method=RequestMethod.GET)
	public Result<ReservedProgramVO> Restful_getReservedProgramList(
			HttpServletRequest request
			,HttpServletResponse response
			,@RequestParam(value="sa_id", required=false, defaultValue="") String sa_id
			,@RequestParam(value="stb_mac", required=false, defaultValue="") String stb_mac
			,@RequestParam(value="sma_mac", required=false, defaultValue="") String sma_mac
			,@RequestParam(value="app_type", required=false, defaultValue="") String app_type
			,@RequestParam(value="start_num", required=false, defaultValue="") String start_num
			,@RequestParam(value="req_count", required=false, defaultValue="") String req_count
			,@RequestParam(value="access_key", required=false, defaultValue="") String access_key
			,@RequestParam(value="cp_id", required=false, defaultValue="") String cp_id
			) throws Exception{
		
		SmartUXException exception = new SmartUXException();
		
		//#########################[LOG SET]##########################
		Log log_logger = LogFactory.getLog("OPENAPI_ReservedProgram");
		long startTime = System.currentTimeMillis();
		String ip = request.getRemoteAddr();
		String url = request.getRequestURI();
		try{
			url = "/v1/reservation/";
		}catch(Exception e){
		}
		String method = request.getMethod();
		//#########################[//LOG SET]##########################
//		String access_key = null;
//		String cp_id = null;
//		
//		
//		String sa_id = "";
//		String stb_mac = "";
//		String sma_mac = "";
//		String app_type = "";
//		String start_num = "";
//		String req_count = "";
				
		Result<ReservedProgramVO> result = new Result<ReservedProgramVO>();
		
		String uri = request.getRequestURI();
		try {
//			HashMap<String, String> paramMap = GlobalCom.getRestfulParamMap(4,uri);
//			sa_id 		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "sa_id"));
//			stb_mac		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "stb_mac"));
//			sma_mac		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "sma_mac"));
//			app_type 	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "app_type"));
//			start_num 	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "start_num"));
//			req_count	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "req_count"));
//			
//			
//			access_key = GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "access_key"));
//			cp_id = GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "cp_id"));
		
			//#########################[LOG START]##########################
			log_logger.info("["+ip+"] ["+url+"]["+method+"][START] - ["+stb_mac+"]["+sa_id+"]["+app_type+"]["+access_key+"]["+cp_id+"]");
			//#########################[//LOG START]########################
	
			//access_key 인증 허용 체크
			String authResult = GlobalCom.isNull(AuthenticationCommon.isAuthenticationMap(access_key,cp_id,"GET","/v1/reservation"));
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
		
			
			
			ReservedProgramParamVO rVO = new ReservedProgramParamVO();
			rVO.setSa_id(sa_id);
			rVO.setStb_mac(stb_mac);
			rVO.setSma_mac(sma_mac);
			rVO.setApp_type(app_type);
			rVO.setStart_num(start_num);
			rVO.setReq_count(req_count);
			
		
		
			//=====유효성 검사=====
			validateGetReservedProgramList(rVO);

			result.setTotal_count(reservedService.getReservedProgramListTotalCnt(rVO));
			
			if(!rVO.getStart_num().equals("0")){
				result.setRecordset(reservedService.getReservedProgramList(rVO));
			}
			
			result.setFlag(SmartUXProperties.getProperty("flag.success"));
			result.setMessage(SmartUXProperties.getProperty("message.success"));
			
			
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
	 * 시청 예약 삭제 API
	 * @param sa_id				셋탑 가입 번호
	 * @param stb_mac			셋탑 맥주소
	 * @param stb_reg_id		셋탑 REG_ID
	 * @param sma_mac			모바일앱 맥주소
	 * @param ctn_reg_id		모바일앱 REG_ID
	 * @param app_type			어플타입
	 * @param service_id		서비스 ID
	 * @param program_id		프로그램 ID
	 * @return					Push 요청에 대한 결과코드,결과메세지
	 */
	@RequestMapping(value="/removeReservedProgram")
	public PushResultVO API_removeReservedProgram(
			HttpServletRequest request,
			HttpServletResponse response,
			 @RequestParam(value="sa_id", required=false, defaultValue="") String sa_id
			,@RequestParam(value="stb_mac", required=false, defaultValue="") String stb_mac
			,@RequestParam(value="sma_mac", required=false, defaultValue="") String sma_mac
			,@RequestParam(value="app_type", required=false, defaultValue="") String app_type
			,@RequestParam(value="service_id", required=false, defaultValue="") String service_id
			,@RequestParam(value="program_id", required=false, defaultValue="") String program_id
			,@RequestParam(value="repeat_day", required=false, defaultValue="") String repeat_day
			){
		
		//#########[LOG START]#########
		Log log_logger = LogFactory.getLog("removeReservedProgram");
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
		
		ReservedProgramParamVO rVO = new ReservedProgramParamVO();
		rVO.setSa_id(sa_id);
		rVO.setStb_mac(stb_mac);
		rVO.setSma_mac(sma_mac);
		rVO.setApp_type(app_type);
		rVO.setService_id(service_id);
		rVO.setProgram_id(program_id);
		rVO.setRepeat_day(repeat_day);
		
		PushResultVO prVO = new PushResultVO();
		
		try{
			//=====유효성 검사=====
			validateRemoveReservedProgram(rVO);
			
			prVO = reservedService.removeReservedProgram(rVO);
			
		}catch(Exception e){
			//logger.error("removeReservedProgram "+e.getClass().getName());
			//logger.error("removeReservedProgram "+e.getMessage());
			//logger.error("[removeReservedProgram]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			log_logger.error("["+log_ip+"] ["+log_url+"]["+log_info_mac+"]["+log_info_id+"]["+log_info_app_type+"]"+"[removeReservedProgram]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			ExceptionHandler handler = new ExceptionHandler(e);
			prVO.setFlag(handler.getFlag());
			prVO.setMessage(handler.getMessage());
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
		log_logger.info("["+log_ip+"] ["+log_url+"][END] ["+(timeStr)+" sec] - ["+log_info_mac+"]["+log_info_id+"]["+log_info_app_type+"] ["+prVO.getFlag()+"]");
		
		return prVO;
	}
	
	
//	//@RequestMapping(value="/ReservedProgram/sa_id/{sa_id}/stb_mac/{stb_mac}/sma_mac/{sma_mac}/app_type/{app_type}/service_id/{service_id}/program_id/{program_id}/repeat_day/{repeat_day}" , method=RequestMethod.DELETE)
//	@RequestMapping(value="/v1/reservation/**" , method=RequestMethod.DELETE)
//	public PushResultVO Restful_removeReservedProgram(
//			HttpServletRequest request,
//			HttpServletResponse response
////			,@PathVariable String sa_id
////			,@PathVariable String stb_mac
////			,@PathVariable String sma_mac
////			,@PathVariable String app_type
////			,@PathVariable String service_id
////			,@PathVariable String program_id
////			,@PathVariable String repeat_day
//			){
//		
//		//#########################[LOG SET]##########################
//		Log log_logger = LogFactory.getLog("OPENAPI_ReservedProgram");
//		long startTime = System.currentTimeMillis();
//		String ip = request.getRemoteAddr();
//		String url = request.getRequestURI();
//		try{
//			url = "/v1/reservation/";
//		}catch(Exception e){
//		}
//		String method = request.getMethod();
//		//#########################[//LOG SET]##########################
//		String access_key = null;
//		String cp_id = null;
//		
//		String sa_id = "";
//		String stb_mac = "";
//		String sma_mac = "";
//		String app_type = "";
//		String service_id = "";
//		String program_id = "";
//		String repeat_day = "";
//		
//		PushResultVO prVO = new PushResultVO();
//		
//		String uri = request.getRequestURI();
//		try {
//			HashMap<String, String> paramMap = GlobalCom.getRestfulParamMap(4,uri);
//			sa_id 		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "sa_id"));
//			stb_mac		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "stb_mac"));
//			sma_mac		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "sma_mac"));
//			app_type 	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "app_type"));
//			service_id 	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "service_id"));
//			program_id 	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "program_id"));
//			repeat_day 	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "repeat_day"));
//		
//			access_key = GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "access_key"));
//			cp_id = GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "cp_id"));
//		
//			//#########################[LOG START]##########################
//			log_logger.info("["+ip+"] ["+url+"]["+method+"][START] - ["+stb_mac+"]["+sa_id+"]["+app_type+"]["+access_key+"]["+cp_id+"]");
//			//#########################[//LOG START]########################
//	
//			//access_key 인증 허용 체크
//			String authResult = GlobalCom.isNull(AuthenticationCommon.isAuthenticationMap(access_key,cp_id,"DELETE","/v1/reservation"));
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
//			ReservedProgramParamVO rVO = new ReservedProgramParamVO();
//			rVO.setSa_id(sa_id);
//			rVO.setStb_mac(stb_mac);
//			rVO.setSma_mac(sma_mac);
//			rVO.setApp_type(app_type);
//			rVO.setService_id(service_id);
//			rVO.setProgram_id(program_id);
//			rVO.setRepeat_day(repeat_day);
//			
//			
//			
//				//=====유효성 검사=====
//				validateRemoveReservedProgram(rVO);
//				
//				prVO = reservedService.removeReservedProgram(rVO);
//			
//		}catch(Exception e){
//			log_logger.error("["+ip+"] ["+url+"]["+stb_mac+"]["+sa_id+"]["+app_type+"]["+access_key+"]["+cp_id+"]["+e.getClass().getName()+"]["+e.getMessage()+"]");
//			ExceptionHandler handler = new ExceptionHandler(e);
//			prVO.setFlag(handler.getFlag());
//			prVO.setMessage(handler.getMessage());
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
//		log_logger.info("["+ip+"] ["+url+"]["+method+"][END] ["+(timeStr)+" sec] - ["+stb_mac+"]["+sa_id+"]["+app_type+"]["+access_key+"]["+cp_id+"] ["+prVO.getFlag()+"]");
//		//############################[//LOG END]##########################
//		
//		return prVO;
//		
//	}
	
	@RequestMapping(value="/v1/reservation" , method=RequestMethod.DELETE)
	public PushResultVO Restful_removeReservedProgram(
			HttpServletRequest request
			,HttpServletResponse response
			,@RequestParam(value="sa_id", required=false, defaultValue="") String sa_id
			,@RequestParam(value="stb_mac", required=false, defaultValue="") String stb_mac
			,@RequestParam(value="sma_mac", required=false, defaultValue="") String sma_mac
			,@RequestParam(value="app_type", required=false, defaultValue="") String app_type
			,@RequestParam(value="service_id", required=false, defaultValue="") String service_id
			,@RequestParam(value="program_id", required=false, defaultValue="") String program_id
			,@RequestParam(value="repeat_day", required=false, defaultValue="") String repeat_day
			,@RequestParam(value="access_key", required=false, defaultValue="") String access_key
			,@RequestParam(value="cp_id", required=false, defaultValue="") String cp_id
			) throws Exception{
		
		SmartUXException exception = new SmartUXException();
		
		//#########################[LOG SET]##########################
		Log log_logger = LogFactory.getLog("OPENAPI_ReservedProgram");
		long startTime = System.currentTimeMillis();
		String ip = request.getRemoteAddr();
		String url = request.getRequestURI();
		try{
			url = "/v1/reservation/";
		}catch(Exception e){
		}
		String method = request.getMethod();
		//#########################[//LOG SET]##########################
//		String access_key = null;
//		String cp_id = null;
//		
//		String sa_id = "";
//		String stb_mac = "";
//		String sma_mac = "";
//		String app_type = "";
//		String service_id = "";
//		String program_id = "";
//		String repeat_day = "";
		
		PushResultVO prVO = new PushResultVO();
		
		String uri = request.getRequestURI();
		try {
//			HashMap<String, String> paramMap = GlobalCom.getRestfulParamMap(4,uri);
//			sa_id 		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "sa_id"));
//			stb_mac		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "stb_mac"));
//			sma_mac		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "sma_mac"));
//			app_type 	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "app_type"));
//			service_id 	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "service_id"));
//			program_id 	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "program_id"));
//			repeat_day 	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "repeat_day"));
//		
//			access_key = GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "access_key"));
//			cp_id = GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "cp_id"));
		
			//#########################[LOG START]##########################
			log_logger.info("["+ip+"] ["+url+"]["+method+"][START] - ["+stb_mac+"]["+sa_id+"]["+app_type+"]["+access_key+"]["+cp_id+"]");
			//#########################[//LOG START]########################
	
			//access_key 인증 허용 체크
			String authResult = GlobalCom.isNull(AuthenticationCommon.isAuthenticationMap(access_key,cp_id,"DELETE","/v1/reservation"));
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
		
	
			ReservedProgramParamVO rVO = new ReservedProgramParamVO();
			rVO.setSa_id(sa_id);
			rVO.setStb_mac(stb_mac);
			rVO.setSma_mac(sma_mac);
			rVO.setApp_type(app_type);
			rVO.setService_id(service_id);
			rVO.setProgram_id(program_id);
			rVO.setRepeat_day(repeat_day);
			
			
			
				//=====유효성 검사=====
				validateRemoveReservedProgram(rVO);
				
				prVO = reservedService.removeReservedProgram(rVO);
			
				
		} catch(SmartUXException e){
			log_logger.error("["+ip+"] ["+url+"]["+stb_mac+"]["+sa_id+"]["+app_type+"]["+access_key+"]["+cp_id+"]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			throw e;
		}catch(Exception e){
			log_logger.error("["+ip+"] ["+url+"]["+stb_mac+"]["+sa_id+"]["+app_type+"]["+access_key+"]["+cp_id+"]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			ExceptionHandler handler = new ExceptionHandler(e);
			prVO.setFlag(handler.getFlag());
			prVO.setMessage(handler.getMessage());
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
		log_logger.info("["+ip+"] ["+url+"]["+method+"][END] ["+(timeStr)+" sec] - ["+stb_mac+"]["+sa_id+"]["+app_type+"]["+access_key+"]["+cp_id+"] ["+prVO.getFlag()+"]");
		//############################[//LOG END]##########################
		
		return prVO;
		
	}
	
	
	
	//@RequestMapping(value="/ReservedProgram/sa_id/{sa_id}/stb_mac/{stb_mac}/sma_mac/{sma_mac}/app_type/{app_type}" , method=RequestMethod.DELETE)
//	@RequestMapping(value="/ReservedProgram/**" , method=RequestMethod.DELETE)
//	public PushResultVO Restful_removeAllReservedProgram(
//			HttpServletRequest request,
//			HttpServletResponse response
////			,@PathVariable String sa_id
////			,@PathVariable String stb_mac
////			,@PathVariable String sma_mac
////			,@PathVariable String app_type
//			){
//		
//		
//		String sa_id = "";
//		String stb_mac = "";
//		String sma_mac = "";
//		String app_type = "";
//		
//		PushResultVO result = new PushResultVO();
//		
//		String uri = request.getRequestURI();
//		try {
//			HashMap<String, String> paramMap = GlobalCom.getRestfulParamMap(uri);
//			sa_id 		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "sa_id"));
//			stb_mac		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "stb_mac"));
//			sma_mac		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "sma_mac"));
//			app_type 	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "app_type"));
//		} catch (Exception e1) {
//			logger.error("[Restful_removeAllReservedProgram]["+e1.getClass().getName()+"]["+e1.getMessage()+"]");
//			ExceptionHandler handler = new ExceptionHandler(e1);
//			result.setFlag(handler.getFlag());
//			result.setMessage(handler.getMessage());
//			return result;
//		}
//		
//		
//			
//		
//	}
	
//	/**
//	 * 시청예약정보 Push 알림
//	  * @param sa_id			셋탑 가입 번호
//	 * @param stb_mac			셋탑 맥주소
//	 * @param app_type			어플 타입
//	 * @param service_id		서비스 ID
//	 * @param channel_no		채널 번호
//	 * @param channel_name		채널 이름
//	 * @param program_id		프로그램 ID
//	 * @param program_name		프로그램 명
//	 * @param program_info		프로그램 연결
//	 * @param defin_flag		프로그램 화질
//	 * @param program_stime		프로그램 시작시간
//	 * @return					Push 요청에 대한 결과코드,결과메세지
//	 */
//	@RequestMapping(value="/reqPushMessage")
//	public PushResultVO API_reqPushMessage(
//			HttpServletRequest request,
//			HttpServletResponse response,
//			@RequestParam(value="sa_id", required=false, defaultValue="") String sa_id
//			,@RequestParam(value="stb_mac", required=false, defaultValue="") String stb_mac			
//			,@RequestParam(value="app_type", required=false, defaultValue="") String app_type
//			,@RequestParam(value="service_id", required=false, defaultValue="") String service_id
//			,@RequestParam(value="channel_no", required=false, defaultValue="") String channel_no
//			,@RequestParam(value="channel_name", required=false, defaultValue="") String channel_name
//			,@RequestParam(value="program_id", required=false, defaultValue="") String program_id
//			,@RequestParam(value="program_name", required=false, defaultValue="") String program_name
//			,@RequestParam(value="program_info", required=false, defaultValue="") String program_info
//			,@RequestParam(value="defin_flag", required=false, defaultValue="") String defin_flag
//			,@RequestParam(value="program_stime", required=false, defaultValue="") String program_stime
//			){
//		
//		//#########[LOG START]#########
//		Log log_logger = LogFactory.getLog("reqPushMessage");
//		long startTime = System.currentTimeMillis();
//		String log_ip = request.getRemoteAddr();
//		String log_url = request.getRequestURI();
//		Enumeration log_penum = request.getParameterNames();
//		String log_key = null;
//		String log_value = null;
//		String log_info_mac = "";
//		String log_info_id = "";
//		String log_info_app_type = "";
//		String log_debug = "";
//		
//		while(log_penum.hasMoreElements()){
//			log_key = (String)log_penum.nextElement();
//			log_value = (new String(request.getParameter(log_key)) == null) ? "" : new String(request.getParameter(log_key));
//			
//			if(log_key.equalsIgnoreCase("stb_mac")){
//				log_info_mac = log_value;
//			}
//			if(log_key.equalsIgnoreCase("sa_id")){
//				log_info_id = log_value;
//			}
//			if(log_key.equalsIgnoreCase("app_type")){
//				log_info_app_type = log_value;
//			}
//			log_debug += " ["+log_key+"="+log_value+"]";
//		}
//		
//		log_logger.info("["+log_ip+"] ["+log_url+"][START] - ["+log_info_mac+"]["+log_info_id+"]["+log_info_app_type+"]");
//		log_logger.debug("["+log_ip+"] ["+log_url+"][START] -"+log_debug);
//		
//		PushResultVO result = new PushResultVO();
//		
//		try {
//			
//			//channel_name = URLDecoder.decode(channel_name, "UTF-8");
//			//program_name = URLDecoder.decode(program_name, "UTF-8");
//			
//			ReservedProgramParamVO rVO = new ReservedProgramParamVO();
//			rVO.setSa_id(sa_id);
//			rVO.setStb_mac(stb_mac);
//			rVO.setApp_type(app_type);
//			rVO.setService_id(service_id);
//			rVO.setChannel_no(channel_no);
//			rVO.setChannel_name(channel_name);
//			rVO.setProgram_id(program_id);
//			rVO.setProgram_name(program_name);
//			rVO.setProgram_info(program_info);
//			rVO.setDefin_flag(defin_flag);
//			rVO.setProgram_stime(program_stime);
//			
//			//유효성 체크
//			validateRegPushMessage(rVO);
//						
//			result = reservedService.reqPushMessage(rVO);
//			
//		}catch(Exception e){
//			//logger.error("addRegistrationID "+e.getClass().getName());
//			//logger.error("addRegistrationID "+e.getMessage());
//			//logger.error("[addRegistrationID]["+e.getClass().getName()+"]["+e.getMessage()+"]");
//			log_logger.error("["+log_ip+"] ["+log_url+"]["+log_info_mac+"]["+log_info_id+"]["+log_info_app_type+"]"+"[reqPushMessage]["+e.getClass().getName()+"]["+e.getMessage()+"]");
//			ExceptionHandler handler = new ExceptionHandler(e);
//			result.setFlag(handler.getFlag());
//			result.setMessage(handler.getMessage());
//		}
//		
//		//#########[LOG END]#########
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
//		log_logger.info("["+log_ip+"] ["+log_url+"][END]["+(timeStr)+" sec] - ["+log_info_mac+"]["+log_info_id+"]["+log_info_app_type+"] ["+result.getFlag()+"]");
//		
//		return result;
//	}
	
	/**
	 * 시청예약 정보 전체 삭제
	 * @param sa_id			셋탑 가입 번호
	 * @param stb_mac		셋탑 맥주소
	 * @param sma_mac		모바일앱 맥주소
	 * @param app_type		어플타입
	 * @return
	 */
	@RequestMapping(value="/removeAllReservedProgram")
	public PushResultVO API_removeAllReservedProgram(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value="sa_id", required=false, defaultValue="") String sa_id
			,@RequestParam(value="stb_mac", required=false, defaultValue="") String stb_mac
			,@RequestParam(value="sma_mac", required=false, defaultValue="") String sma_mac
			,@RequestParam(value="app_type", required=false, defaultValue="") String app_type
			){
		
		//#########[LOG START]#########
		Log log_logger = LogFactory.getLog("removeAllReservedProgram");
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
		

		PushResultVO result = new PushResultVO();

		try {
			ReservedProgramParamVO rVO = new ReservedProgramParamVO();
			rVO.setSa_id(sa_id);
			rVO.setStb_mac(stb_mac);
			rVO.setSma_mac(sma_mac);
			rVO.setApp_type(app_type);

			validateRemoveAllReservedProgram(rVO);
			
			reservedService.removeAllReservedProgram(rVO);
			
			result.setFlag(SmartUXProperties.getProperty("flag.success"));
			result.setMessage(SmartUXProperties.getProperty("message.success"));
			
		}catch(Exception e){
			//logger.error("removeAllReservedProgram "+e.getClass().getName());
			//logger.error("removeAllReservedProgram "+e.getMessage());
			//logger.error("[removeAllReservedProgram]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			log_logger.error("["+log_ip+"] ["+log_url+"]["+log_info_mac+"]["+log_info_id+"]["+log_info_app_type+"]"+"[removeAllReservedProgram]["+e.getClass().getName()+"]["+e.getMessage()+"]");
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
	
	
//	@RequestMapping(value="/v1/reservation/all/**" , method=RequestMethod.DELETE)
//	public PushResultVO Restful_removeAllReservedProgram(
//			HttpServletRequest request,
//			HttpServletResponse response
////			,@PathVariable String sa_id
////			,@PathVariable String stb_mac
////			,@PathVariable String sma_mac
////			,@PathVariable String app_type
////			,@PathVariable String service_id
////			,@PathVariable String program_id
////			,@PathVariable String repeat_day
//			){
//		
//		//#########################[LOG SET]##########################
//		Log log_logger = LogFactory.getLog("OPENAPI_ReservedProgram");
//		long startTime = System.currentTimeMillis();
//		String ip = request.getRemoteAddr();
//		String url = request.getRequestURI();
//		try{
//			url = "/v1/reservation/all/";
//		}catch(Exception e){
//		}
//		String method = request.getMethod();
//		//#########################[//LOG SET]##########################
//		String access_key = null;
//		String cp_id = null;
//		
//		String sa_id = "";
//		String stb_mac = "";
//		String sma_mac = "";
//		String app_type = "";
//		String service_id = "";
//		String program_id = "";
//		String repeat_day = "";
//		
//		PushResultVO prVO = new PushResultVO();
//		
//		String uri = request.getRequestURI();
//		try {
//			HashMap<String, String> paramMap = GlobalCom.getRestfulParamMap(5,uri);
//			sa_id 		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "sa_id"));
//			stb_mac		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "stb_mac"));
//			sma_mac		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "sma_mac"));
//			app_type 	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "app_type"));
//			service_id 	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "service_id"));
//			program_id 	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "program_id"));
//			repeat_day 	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "repeat_day"));
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
//			String authResult = GlobalCom.isNull(AuthenticationCommon.isAuthenticationMap(access_key,cp_id,"DELETE","/v1/reservation/all"));
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
//			ReservedProgramParamVO rVO = new ReservedProgramParamVO();
//			rVO.setSa_id(sa_id);
//			rVO.setStb_mac(stb_mac);
//			rVO.setSma_mac(sma_mac);
//			rVO.setApp_type(app_type);
//
//			validateRemoveAllReservedProgram(rVO);
//			
//			reservedService.removeAllReservedProgram(rVO);
//			
//			prVO.setFlag(SmartUXProperties.getProperty("flag.success"));
//			prVO.setMessage(SmartUXProperties.getProperty("message.success"));
//			
//		}catch(Exception e){
//			log_logger.error("["+ip+"] ["+url+"]["+stb_mac+"]["+sa_id+"]["+app_type+"]["+access_key+"]["+cp_id+"]["+e.getClass().getName()+"]["+e.getMessage()+"]");
//			ExceptionHandler handler = new ExceptionHandler(e);
//			prVO.setFlag(handler.getFlag());
//			prVO.setMessage(handler.getMessage());
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
//		log_logger.info("["+ip+"] ["+url+"]["+method+"][END] ["+(timeStr)+" sec] - ["+stb_mac+"]["+sa_id+"]["+app_type+"]["+access_key+"]["+cp_id+"] ["+prVO.getFlag()+"]");
//		//############################[//LOG END]##########################
//		
//		return prVO;
//		
//	}
	
	
	@RequestMapping(value="/v1/reservation/all" , method=RequestMethod.DELETE)
	public PushResultVO Restful_removeAllReservedProgram(
			HttpServletRequest request
			,HttpServletResponse response
			,@RequestParam(value="sa_id", required=false, defaultValue="") String sa_id
			,@RequestParam(value="stb_mac", required=false, defaultValue="") String stb_mac
			,@RequestParam(value="sma_mac", required=false, defaultValue="") String sma_mac
			,@RequestParam(value="app_type", required=false, defaultValue="") String app_type
			,@RequestParam(value="access_key", required=false, defaultValue="") String access_key
			,@RequestParam(value="cp_id", required=false, defaultValue="") String cp_id
			) throws Exception{
		
		SmartUXException exception = new SmartUXException();
		
		//#########################[LOG SET]##########################
		Log log_logger = LogFactory.getLog("OPENAPI_ReservedProgram");
		long startTime = System.currentTimeMillis();
		String ip = request.getRemoteAddr();
		String url = request.getRequestURI();
		try{
			url = "/v1/reservation/all/";
		}catch(Exception e){
		}
		String method = request.getMethod();
		//#########################[//LOG SET]##########################
//		String access_key = null;
//		String cp_id = null;
//		
//		String sa_id = "";
//		String stb_mac = "";
//		String sma_mac = "";
//		String app_type = "";
//		String service_id = "";
//		String program_id = "";
//		String repeat_day = "";
		
		PushResultVO prVO = new PushResultVO();
		
		String uri = request.getRequestURI();
		try {
//			HashMap<String, String> paramMap = GlobalCom.getRestfulParamMap(5,uri);
//			sa_id 		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "sa_id"));
//			stb_mac		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "stb_mac"));
//			sma_mac		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "sma_mac"));
//			app_type 	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "app_type"));
//			service_id 	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "service_id"));
//			program_id 	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "program_id"));
//			repeat_day 	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "repeat_day"));
//		
//		
//			access_key = GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "access_key"));
//			cp_id = GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "cp_id"));
		
			//#########################[LOG START]##########################
			log_logger.info("["+ip+"] ["+url+"]["+method+"][START] - ["+stb_mac+"]["+sa_id+"]["+app_type+"]["+access_key+"]["+cp_id+"]");
			//#########################[//LOG START]########################
	
			//access_key 인증 허용 체크
			String authResult = GlobalCom.isNull(AuthenticationCommon.isAuthenticationMap(access_key,cp_id,"DELETE","/v1/reservation/all"));
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
		
			
		
			ReservedProgramParamVO rVO = new ReservedProgramParamVO();
			rVO.setSa_id(sa_id);
			rVO.setStb_mac(stb_mac);
			rVO.setSma_mac(sma_mac);
			rVO.setApp_type(app_type);

			validateRemoveAllReservedProgram(rVO);
			
			reservedService.removeAllReservedProgram(rVO);
			
			prVO.setFlag(SmartUXProperties.getProperty("flag.success"));
			prVO.setMessage(SmartUXProperties.getProperty("message.success"));
			
		} catch(SmartUXException e){
			log_logger.error("["+ip+"] ["+url+"]["+stb_mac+"]["+sa_id+"]["+app_type+"]["+access_key+"]["+cp_id+"]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			throw e;
		}catch(Exception e){
			log_logger.error("["+ip+"] ["+url+"]["+stb_mac+"]["+sa_id+"]["+app_type+"]["+access_key+"]["+cp_id+"]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			ExceptionHandler handler = new ExceptionHandler(e);
			prVO.setFlag(handler.getFlag());
			prVO.setMessage(handler.getMessage());
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
		log_logger.info("["+ip+"] ["+url+"]["+method+"][END] ["+(timeStr)+" sec] - ["+stb_mac+"]["+sa_id+"]["+app_type+"]["+access_key+"]["+cp_id+"] ["+prVO.getFlag()+"]");
		//############################[//LOG END]##########################
		
		return prVO;
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private void validateRemoveAllReservedProgram(ReservedProgramParamVO rVO)throws SmartUXException{
		
		SmartUXException exception = new SmartUXException();
	
		// 가입 번호 파라미터 존재 유무
		if(!(StringUtils.hasText(rVO.getSa_id()))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "sa_id"));
			throw exception;
		}
		
		// 맥주소 파라미터 존재 유무
		if(!(StringUtils.hasText(rVO.getStb_mac()))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "stb_mac"));
			throw exception;
		}
		
		//어플타입 존재 유무 
		if(!(StringUtils.hasText(rVO.getApp_type()))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "app_type"));
			throw exception;
		}else{
//			//STB이 아닌 경우 CTN은 필수 항목
//			if(rVO.getApp_type().equalsIgnoreCase("ux")){	
//				rVO.setSma_mac("");
//			}else{
//				// CTN 파라미터 존재 유무
//				if(!(StringUtils.hasText(rVO.getSma_mac()))){
//					exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
//					exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "sma_mac"));
//					throw exception;
//				}
//			}
			//130703 타입 로직 변경
			if(rVO.getApp_type().equalsIgnoreCase("sma")){
				// CTN 파라미터 존재 유무
				if(!(StringUtils.hasText(rVO.getSma_mac()))){
					exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
					exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "sma_mac"));
					throw exception;
				}
			}else{
				rVO.setSma_mac("");
			}
		}
	}
	
	/**
	 * 시청예약 정보 Push 알림 API 유효성 검사
	 * @param rVO
	 * @throws SmartUXException
	 */
	private void validateRegPushMessage(ReservedProgramParamVO rVO) throws SmartUXException{
		
		SmartUXException exception = new SmartUXException();
		
		// 가입 번호 파라미터 존재 유무
		if(!(StringUtils.hasText(rVO.getSa_id()))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "sa_id"));
			throw exception;
		}
		
		// 맥주소 파라미터 존재 유무
		if(!(StringUtils.hasText(rVO.getStb_mac()))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "stb_mac"));
			throw exception;
		}
		
		//어플타입 존재 유무 
		if(!(StringUtils.hasText(rVO.getApp_type()))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "app_type"));
			throw exception;
		}
		
		//서비스 ID 존재 유무 
		if(!(StringUtils.hasText(rVO.getService_id()))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "service_id"));
			throw exception;
		}
		
		//채널 ID 존재 유무 
		if(!(StringUtils.hasText(rVO.getChannel_no()))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "channel_no"));
			throw exception;
		}
		
		//채널 명 존재 유무 
		if(!(StringUtils.hasText(rVO.getChannel_name()))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "channel_name"));
			throw exception;
		}
		
		//프로그램 ID 존재 유무 
		if(!(StringUtils.hasText(rVO.getProgram_id()))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "program_id"));
			throw exception;
		}

		//프로그램 명 존재 유무 
		if(!(StringUtils.hasText(rVO.getProgram_name()))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "program_name"));
			throw exception;
		}
		
		//프로그램 연령 존재 유무 
		if(!(StringUtils.hasText(rVO.getProgram_info()))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "program_info"));
			throw exception;
		}
		
		//프로그램 화질 존재 유무 
		if(!(StringUtils.hasText(rVO.getDefin_flag()))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "defin_flag"));
			throw exception;
		}
		
		//프로그램 시작시간 존재 유무 
		if(!(StringUtils.hasText(rVO.getProgram_stime()))){
			exception.setFlag(SmartUXProperties.getProperty("flag.format"));
			exception.setMessage(SmartUXProperties.getProperty("message.format", "program_stime"));
			throw exception;
		}else{
			//YYYY-MM-DD HH:MM:SS
//					if(rVO.getProgram_stime().indexOf(" ") == -1 || rVO.getProgram_stime().indexOf("-") == -1 || rVO.getProgram_stime().indexOf(":") == -1){
//						exception.setFlag(SmartUXProperties.getProperty("flag.format"));
//						exception.setMessage(SmartUXProperties.getProperty("message.format", "program_stime"));
//						throw exception;
//					}else{
//						String _program_stime = rVO.getProgram_stime().replaceAll(" ", "").replaceAll("-","").replaceAll(":","");
//						if(_program_stime.length() != 14){
//							exception.setFlag(SmartUXProperties.getProperty("flag.format"));
//							exception.setMessage(SmartUXProperties.getProperty("message.format", "program_stime"));
//							throw exception;
//						}
//					}
			//YYYYMMDDHHMMSS
			if(rVO.getProgram_stime().length() != 14){
				exception.setFlag(SmartUXProperties.getProperty("flag.format"));
				exception.setMessage(SmartUXProperties.getProperty("message.format", "program_stime"));
				throw exception;
			}else{
				if(GlobalCom.checkStrInt(rVO.getProgram_stime()) != 3){
					logger.debug("reqPushMessage CHECK : " + GlobalCom.checkStrInt(rVO.getProgram_stime()));
					exception.setFlag(SmartUXProperties.getProperty("flag.format"));
					exception.setMessage(SmartUXProperties.getProperty("message.format", "program_stime"));
					throw exception;
				}
			}
		}
	}
	
	
//	/**
//	 * Push 알림 설정 API 유효성 검사
//	 * @param rVO	시청 예약 정보 파라미터 클래스
//	 * @throws SmartUXException	validate에 문제가 발생했을 경우 코드와 메시지가 셋팅되서 던져지는 SmartUXException 객체
//	 */
//	private void validateSetPushNoti(ReservedProgramParamVO rVO) throws SmartUXException{
//
//		SmartUXException exception = new SmartUXException();
//		
//		// 가입 번호 파라미터 존재 유무
//		if(!(StringUtils.hasText(rVO.getSa_id()))){
//			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
//			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "sa_id"));
//			throw exception;
//		}
//		
//		// 맥주소 파라미터 존재 유무
//		if(!(StringUtils.hasText(rVO.getStb_mac()))){
//			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
//			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "stb_mac"));
//			throw exception;
//		}
//
//		// 셋탑 REG_ID 파라미터 존재 유무
////		if(!(StringUtils.hasText(rVO.getStb_reg_id()))){
////			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
////			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "stb_reg_id"));
////			throw exception;
////		}
//		
//		// CTN 파라미터 존재 유무
//		if(!(StringUtils.hasText(rVO.getCtn()))){
//			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
//			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "ctn"));
//			throw exception;
//		}
//		
//		// CTN REG_ID 파라미터 존재 유무
////		if(!(StringUtils.hasText(rVO.getCtn_reg_id()))){
////			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
////			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "ctn_reg_id"));
////			throw exception;
////		}
//		
//		//REG_ID 파라미터 존재 유무
//		boolean reg_idChk = true;
//		for(int i=0;i<rVO.getReg_id().length;i++){
//			
//			logger.debug("validateSetPushNoti !!!!!!!!!!!!!!!!!!!!!!!!!!!!rVO.getReg_id()["+i+"] : " + rVO.getReg_id()[i]);
//			if("".equals(GlobalCom.isNull(rVO.getReg_id()[i]))){
//				reg_idChk = false;
//				break;
//			}
//		}
//		if(!reg_idChk){
//			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
//			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "reg_id"));
//			throw exception;
//		}
//		
//		//어플타입 존재 유무 
//		if(!(StringUtils.hasText(rVO.getApp_type()))){
//			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
//			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "app_type"));
//			throw exception;
//		}
//		
//		// Push 설정값 타입 존재 유무 
//		if(!(StringUtils.hasText(rVO.getIs_push()))){
//			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
//			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "is_push"));
//			throw exception;
//		}
//		
//	}
	
	/**
	 * 시청 예약 추가 API 유효성 검사
	 * @param rVO	시청 예약 정보 파라미터 클래스
	 * @throws SmartUXException	validate에 문제가 발생했을 경우 코드와 메시지가 셋팅되서 던져지는 SmartUXException 객체
	 */
	private ReservedProgramParamVO validateAddReservedProgram(ReservedProgramParamVO rVO) throws SmartUXException{

		SmartUXException exception = new SmartUXException();
		
		int arrServiceIDCnt = 0;		//다중식으로 전달될 때 개수 유효성 체크
		
		// 가입 번호 파라미터 존재 유무
		if(!(StringUtils.hasText(rVO.getSa_id()))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "sa_id"));
			throw exception;
		}
		
		// 맥주소 파라미터 존재 유무
		if(!(StringUtils.hasText(rVO.getStb_mac()))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "stb_mac"));
			throw exception;
		}

		//어플타입 존재 유무 
		if(!(StringUtils.hasText(rVO.getApp_type()))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "app_type"));
			throw exception;
		}else{
//			//STB이 아닌 경우 CTN은 필수 항목
//			if(rVO.getApp_type().equalsIgnoreCase("ux")){	
//				rVO.setSma_mac("");
//			}else{
//				// CTN 파라미터 존재 유무
//				if(!(StringUtils.hasText(rVO.getSma_mac()))){
//					exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
//					exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "sma_mac"));
//					throw exception;
//				}
//			}
			//130703 타입 로직 변경
			if(rVO.getApp_type().equalsIgnoreCase("sma")){
				// CTN 파라미터 존재 유무
				if(!(StringUtils.hasText(rVO.getSma_mac()))){
					exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
					exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "sma_mac"));
					throw exception;
				}
			}else{
				rVO.setSma_mac("");
			}
		}
		
		//서비스 ID 존재 유무 
		if(!(StringUtils.hasText(rVO.getService_id()))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "service_id"));
			throw exception;
		}else{
			logger.debug("YJ #####- rVO.getService_id() :"+rVO.getService_id());
			rVO.setArr_service_id(rVO.getService_id().split("\\|\\|"));
			arrServiceIDCnt = rVO.getArr_service_id().length;
			logger.debug("YJ #####- arrServiceIDCnt :"+arrServiceIDCnt);
			
			for(int i=0;i<rVO.getArr_service_id().length;i++){
				String _str = rVO.getArr_service_id()[i];
				logger.debug("YJ #####- service_id["+i+"] :"+_str);
				if(!(StringUtils.hasText(_str))){
					exception.setFlag(SmartUXProperties.getProperty("flag.format"));
					exception.setMessage(SmartUXProperties.getProperty("message.format", "service_id"));
					throw exception;
				}
			}
		}
		
		//채널 ID 존재 유무 
		if(!(StringUtils.hasText(rVO.getChannel_no()))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "channel_no"));
			throw exception;
		}else{
			logger.debug("YJ #####- rVO.getChannel_no() :"+rVO.getChannel_no());
			rVO.setArr_channel_no(rVO.getChannel_no().split("\\|\\|"));
			logger.debug("YJ #####- getArr_channel_no.length :"+rVO.getArr_channel_no().length);
			
			if(arrServiceIDCnt != rVO.getArr_channel_no().length){
				exception.setFlag(SmartUXProperties.getProperty("flag.format"));
				exception.setMessage(SmartUXProperties.getProperty("message.format", "channel_no"));
				throw exception;
			}
			
			for(int i=0;i<rVO.getArr_channel_no().length;i++){
				String _str = rVO.getArr_channel_no()[i];
				logger.debug("YJ #####- channel_no["+i+"] :"+_str);
				if(!(StringUtils.hasText(_str))){
					exception.setFlag(SmartUXProperties.getProperty("flag.format"));
					exception.setMessage(SmartUXProperties.getProperty("message.format", "channel_no"));
					throw exception;
				}
			}
		}
		
		//채널 명 존재 유무 
		if(!(StringUtils.hasText(rVO.getChannel_name()))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "channel_name"));
			throw exception;
		}else{
			logger.debug("YJ #####- rVO.getChannel_name() :"+rVO.getChannel_name());
			rVO.setArr_channel_name(rVO.getChannel_name().split("\\|\\|"));
			logger.debug("YJ #####- getArr_channel_name.length :"+rVO.getArr_channel_name().length);
			
			if(arrServiceIDCnt != rVO.getArr_channel_name().length){
				exception.setFlag(SmartUXProperties.getProperty("flag.format"));
				exception.setMessage(SmartUXProperties.getProperty("message.format", "channel_name"));
				throw exception;
			}
			
			for(int i=0;i<rVO.getArr_channel_name().length;i++){
				String _str = rVO.getArr_channel_name()[i];
				logger.debug("YJ #####- channel_name["+i+"] :"+_str);
				if(!(StringUtils.hasText(_str))){
					exception.setFlag(SmartUXProperties.getProperty("flag.format"));
					exception.setMessage(SmartUXProperties.getProperty("message.format", "channel_name"));
					throw exception;
				}
			}
		}
		
		//프로그램 ID 존재 유무 
		if(!(StringUtils.hasText(rVO.getProgram_id()))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "program_id"));
			throw exception;
		}else{
			logger.debug("YJ #####- rVO.getProgram_id() :"+rVO.getProgram_id());
			rVO.setArr_program_id(rVO.getProgram_id().split("\\|\\|"));
			logger.debug("YJ #####- getArr_program_id.length :"+rVO.getArr_program_id().length);
			
			if(arrServiceIDCnt != rVO.getArr_program_id().length){
				exception.setFlag(SmartUXProperties.getProperty("flag.format"));
				exception.setMessage(SmartUXProperties.getProperty("message.format", "program_id"));
				throw exception;
			}
			
			for(int i=0;i<rVO.getArr_program_id().length;i++){
				String _str = rVO.getArr_program_id()[i];
				logger.debug("YJ #####- program_id["+i+"] :"+_str);
				if(!(StringUtils.hasText(_str))){
					exception.setFlag(SmartUXProperties.getProperty("flag.format"));
					exception.setMessage(SmartUXProperties.getProperty("message.format", "program_id"));
					throw exception;
				}
			}
		}

		//프로그램 명 존재 유무 
		if(!(StringUtils.hasText(rVO.getProgram_name()))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "program_name"));
			throw exception;
		}else{
			logger.debug("YJ #####- rVO.getProgram_name() :"+rVO.getProgram_name());
			rVO.setArr_program_name(rVO.getProgram_name().split("\\|\\|"));
			logger.debug("YJ #####- getArr_program_name.length :"+rVO.getArr_program_name().length);
			
			if(arrServiceIDCnt != rVO.getArr_program_name().length){
				exception.setFlag(SmartUXProperties.getProperty("flag.format"));
				exception.setMessage(SmartUXProperties.getProperty("message.format", "program_name"));
				throw exception;
			}
			
			for(int i=0;i<rVO.getArr_program_name().length;i++){
				String _str = rVO.getArr_program_name()[i];
				logger.debug("YJ #####- program_name["+i+"] :"+_str);
				if(!(StringUtils.hasText(_str))){
					exception.setFlag(SmartUXProperties.getProperty("flag.format"));
					exception.setMessage(SmartUXProperties.getProperty("message.format", "program_name"));
					throw exception;
				}
			}
		}
		
		//프로그램 연령 존재 유무 
		if(!(StringUtils.hasText(rVO.getProgram_info()))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "program_info"));
			throw exception;
		}else{
			logger.debug("YJ #####- rVO.getProgram_info() :"+rVO.getProgram_info());
			rVO.setArr_program_info(rVO.getProgram_info().split("\\|\\|"));
			logger.debug("YJ #####- getArr_program_info.length :"+rVO.getArr_program_info().length);
			
			if(arrServiceIDCnt != rVO.getArr_program_info().length){
				exception.setFlag(SmartUXProperties.getProperty("flag.format"));
				exception.setMessage(SmartUXProperties.getProperty("message.format", "program_info"));
				throw exception;
			}
			
			for(int i=0;i<rVO.getArr_program_info().length;i++){
				String _str = rVO.getArr_program_info()[i];
				logger.debug("YJ #####- program_info["+i+"] :"+_str);
				if(!(StringUtils.hasText(_str))){
					exception.setFlag(SmartUXProperties.getProperty("flag.format"));
					exception.setMessage(SmartUXProperties.getProperty("message.format", "program_info"));
					throw exception;
				}
			}
		}
		
		//프로그램 화질 존재 유무 
		if(!(StringUtils.hasText(rVO.getDefin_flag()))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "defin_flag"));
			throw exception;
		}else{
			logger.debug("YJ #####- rVO.getDefin_flag() :"+rVO.getDefin_flag());
			rVO.setArr_defin_flag(rVO.getDefin_flag().split("\\|\\|"));
			logger.debug("YJ #####- getArr_Defin_flag.length :"+rVO.getArr_defin_flag().length);
			
			if(arrServiceIDCnt != rVO.getArr_defin_flag().length){
				exception.setFlag(SmartUXProperties.getProperty("flag.format"));
				exception.setMessage(SmartUXProperties.getProperty("message.format", "defin_flag"));
				throw exception;
			}
			
			for(int i=0;i<rVO.getArr_defin_flag().length;i++){
				String _str = rVO.getArr_defin_flag()[i];
				logger.debug("YJ #####- defin_flag["+i+"] :"+_str);
				if(!(StringUtils.hasText(_str))){
					exception.setFlag(SmartUXProperties.getProperty("flag.format"));
					exception.setMessage(SmartUXProperties.getProperty("message.format", "defin_flag"));
					throw exception;
				}
			}
		}
		
		//프로그램 시작시간 존재 유무 
		if(!(StringUtils.hasText(rVO.getProgram_stime()))){
			exception.setFlag(SmartUXProperties.getProperty("flag.format"));
			exception.setMessage(SmartUXProperties.getProperty("message.format", "program_stime"));
			throw exception;
		}else{
			//YYYY-MM-DD HH:MM:SS
//			if(rVO.getProgram_stime().indexOf(" ") == -1 || rVO.getProgram_stime().indexOf("-") == -1 || rVO.getProgram_stime().indexOf(":") == -1){
//				exception.setFlag(SmartUXProperties.getProperty("flag.format"));
//				exception.setMessage(SmartUXProperties.getProperty("message.format", "program_stime"));
//				throw exception;
//			}else{
//				String _program_stime = rVO.getProgram_stime().replaceAll(" ", "").replaceAll("-","").replaceAll(":","");
//				if(_program_stime.length() != 14){
//					exception.setFlag(SmartUXProperties.getProperty("flag.format"));
//					exception.setMessage(SmartUXProperties.getProperty("message.format", "program_stime"));
//					throw exception;
//				}
//			}
			
			logger.debug("YJ #####- rVO.getProgram_stime() :"+rVO.getProgram_stime());
			rVO.setArr_program_stime(rVO.getProgram_stime().split("\\|\\|"));
			logger.debug("YJ #####- getArr_program_stime.length :"+rVO.getArr_program_stime().length);
			
			if(arrServiceIDCnt != rVO.getArr_program_stime().length){
				exception.setFlag(SmartUXProperties.getProperty("flag.format"));
				exception.setMessage(SmartUXProperties.getProperty("message.format", "program_stime"));
				throw exception;
			}
			
			for(int i=0;i<rVO.getArr_program_stime().length;i++){
				String _str = rVO.getArr_program_stime()[i];
				logger.debug("YJ #####- program_stime["+i+"] :"+_str);
				if(!(StringUtils.hasText(_str))){
					exception.setFlag(SmartUXProperties.getProperty("flag.format"));
					exception.setMessage(SmartUXProperties.getProperty("message.format", "program_stime"));
					throw exception;
				}else{
					if(_str.length() != 14){
						exception.setFlag(SmartUXProperties.getProperty("flag.format"));
						exception.setMessage(SmartUXProperties.getProperty("message.format", "program_stime"));
						throw exception;
					}else{
						if(GlobalCom.checkStrInt(_str) != 3){
							logger.debug("reqPushMessage CHECK : " + GlobalCom.checkStrInt(rVO.getProgram_stime()));
							exception.setFlag(SmartUXProperties.getProperty("flag.format"));
							exception.setMessage(SmartUXProperties.getProperty("message.format", "program_stime"));
							throw exception;
						}
					}
				}
			}
		}
		//요일 반복 정보 존재 시
		if((StringUtils.hasText(rVO.getRepeat_day()))){
			//String repeatStr = rVO.getRepeat_day();
			logger.debug("YJ #####- rVO.getRepeat_day() :"+rVO.getRepeat_day());
			rVO.setArr_repeat_day(rVO.getRepeat_day().split("\\|\\|"));
			
			logger.debug("YJ #####- getArr_repeat_day.length :"+rVO.getArr_repeat_day().length);
			
			ArrayList<String> list = new ArrayList<String>();
			
			for(int i=0;i<rVO.getArr_repeat_day().length;i++){
				String _str = rVO.getArr_repeat_day()[i];
				logger.debug("YJ #####- repeat_day["+i+"] :"+_str);
				
				for(int j=0;j<_str.length();j++){
					try{
						//6 이상일 경우
						if(Integer.parseInt(_str.substring(j,j+1)) > 6){
							exception.setFlag(SmartUXProperties.getProperty("flag.range5"));
							exception.setMessage(SmartUXProperties.getProperty("message.range5", "repeat_day", "0~6"));
							throw exception;
						}
					}catch(NumberFormatException ne){
						//잘못된 포멧
						exception.setFlag(SmartUXProperties.getProperty("flag.format"));
						exception.setMessage(SmartUXProperties.getProperty("message.format", "repeat_day"));
						throw exception;
					}
				}
				
				list.add(_str);
			}
			
			int gap = arrServiceIDCnt - rVO.getArr_repeat_day().length;
			for(int i=0;i<gap;i++){
				list.add("");
			}
			rVO.setList_repeat_day(list);
		}else{
			ArrayList<String> list = new ArrayList<String>();
			for(int i=0;i<arrServiceIDCnt;i++){
				list.add("");
			}
			rVO.setList_repeat_day(list);
		}
		
		return rVO;
	}
	
	/**
	 * 시청 예약 삭제 API 유효성 검사
	 * @param rVO	시청 예약 정보 파라미터 클래스
	 * @throws SmartUXException	validate에 문제가 발생했을 경우 코드와 메시지가 셋팅되서 던져지는 SmartUXException 객체
	 */
	private void validateRemoveReservedProgram(ReservedProgramParamVO rVO) throws SmartUXException{

		SmartUXException exception = new SmartUXException();
		
		int arrServiceIDCnt = 0;		//다중식으로 전달될 때 개수 유효성 체크
		
		// 가입 번호 파라미터 존재 유무
		if(!(StringUtils.hasText(rVO.getSa_id()))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "sa_id"));
			throw exception;
		}
		
		// 맥주소 파라미터 존재 유무
		if(!(StringUtils.hasText(rVO.getStb_mac()))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "stb_mac"));
			throw exception;
		}

		//어플타입 존재 유무 
		if(!(StringUtils.hasText(rVO.getApp_type()))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "app_type"));
			throw exception;
		}else{
//			//STB이 아닌 경우 CTN은 필수 항목
//			if(rVO.getApp_type().equalsIgnoreCase("ux")){	
//				rVO.setSma_mac("");
//			}else{
//				// CTN 파라미터 존재 유무
//				if(!(StringUtils.hasText(rVO.getSma_mac()))){
//					exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
//					exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "sma_mac"));
//					throw exception;
//				}
//			}
			//130703 타입 로직 변경
			if(rVO.getApp_type().equalsIgnoreCase("sma")){
				// CTN 파라미터 존재 유무
				if(!(StringUtils.hasText(rVO.getSma_mac()))){
					exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
					exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "sma_mac"));
					throw exception;
				}
			}else{
				rVO.setSma_mac("");
			}
		}
		
//		//서비스 ID 존재 유무 
//		if(!(StringUtils.hasText(rVO.getService_id()))){
//			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
//			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "service_id"));
//			throw exception;
//		}
//		
//		//프로그램 ID 존재 유무 
//		if(!(StringUtils.hasText(rVO.getProgram_id()))){
//			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
//			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "program_id"));
//			throw exception;
//		}
		
		//서비스 ID 존재 유무 
		if(!(StringUtils.hasText(rVO.getService_id()))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "service_id"));
			throw exception;
		}else{
			logger.debug("YJ #####- rVO.getService_id() :"+rVO.getService_id());
			rVO.setArr_service_id(rVO.getService_id().split("\\|\\|"));
			arrServiceIDCnt = rVO.getArr_service_id().length;
			logger.debug("YJ #####- arrServiceIDCnt :"+arrServiceIDCnt);
			
			for(int i=0;i<rVO.getArr_service_id().length;i++){
				String _str = rVO.getArr_service_id()[i];
				logger.debug("YJ #####- service_id["+i+"] :"+_str);
				if(!(StringUtils.hasText(_str))){
					exception.setFlag(SmartUXProperties.getProperty("flag.format"));
					exception.setMessage(SmartUXProperties.getProperty("message.format", "service_id"));
					throw exception;
				}
			}
		}
		
		//프로그램 ID 존재 유무 
		if(!(StringUtils.hasText(rVO.getProgram_id()))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "program_id"));
			throw exception;
		}else{
			logger.debug("YJ #####- rVO.getProgram_id() :"+rVO.getProgram_id());
			rVO.setArr_program_id(rVO.getProgram_id().split("\\|\\|"));
			logger.debug("YJ #####- getArr_program_id.length :"+rVO.getArr_program_id().length);
			
			if(arrServiceIDCnt != rVO.getArr_program_id().length){
				exception.setFlag(SmartUXProperties.getProperty("flag.format"));
				exception.setMessage(SmartUXProperties.getProperty("message.format", "program_id"));
				throw exception;
			}
			
			for(int i=0;i<rVO.getArr_program_id().length;i++){
				String _str = rVO.getArr_program_id()[i];
				logger.debug("YJ #####- program_id["+i+"] :"+_str);
				if(!(StringUtils.hasText(_str))){
					exception.setFlag(SmartUXProperties.getProperty("flag.format"));
					exception.setMessage(SmartUXProperties.getProperty("message.format", "program_id"));
					throw exception;
				}
			}
		}
		
		//요일 반복 정보 존재 시
//		if((StringUtils.hasText(rVO.getRepeat_day()))){
//			String repeatStr = rVO.getRepeat_day();
//			for(int i=0;i<repeatStr.length();i++){
//				try{
//					//6 이상일 경우
//					if(Integer.parseInt(repeatStr.substring(i,i+1)) > 6){
//						exception.setFlag(SmartUXProperties.getProperty("flag.range5"));
//						exception.setMessage(SmartUXProperties.getProperty("message.range5", "repeat_day", "0~6"));
//						throw exception;
//					}
//				}catch(NumberFormatException ne){
//					//잘못된 포멧
//					exception.setFlag(SmartUXProperties.getProperty("flag.format"));
//					exception.setMessage(SmartUXProperties.getProperty("message.format", "repeat_day"));
//					throw exception;
//				}
//			}
//		}
		//요일 반복 정보 존재 시
		if((StringUtils.hasText(rVO.getRepeat_day()))){
			//String repeatStr = rVO.getRepeat_day();
			logger.debug("YJ #####- rVO.getRepeat_day() :"+rVO.getRepeat_day());
			rVO.setArr_repeat_day(rVO.getRepeat_day().split("\\|\\|"));
			
			logger.debug("YJ #####- getArr_repeat_day.length :"+rVO.getArr_repeat_day().length);
			
			ArrayList<String> list = new ArrayList<String>();
			
			for(int i=0;i<rVO.getArr_repeat_day().length;i++){
				String _str = rVO.getArr_repeat_day()[i];
				logger.debug("YJ #####- repeat_day["+i+"] :"+_str);
				
				for(int j=0;j<_str.length();j++){
					try{
						//6 이상일 경우
						if(Integer.parseInt(_str.substring(j,j+1)) > 6){
							exception.setFlag(SmartUXProperties.getProperty("flag.range5"));
							exception.setMessage(SmartUXProperties.getProperty("message.range5", "repeat_day", "0~6"));
							throw exception;
						}
					}catch(NumberFormatException ne){
						//잘못된 포멧
						exception.setFlag(SmartUXProperties.getProperty("flag.format"));
						exception.setMessage(SmartUXProperties.getProperty("message.format", "repeat_day"));
						throw exception;
					}
				}
				
				list.add(_str);
			}
			
			int gap = arrServiceIDCnt - rVO.getArr_repeat_day().length;
			for(int i=0;i<gap;i++){
				list.add("");
			}
			rVO.setList_repeat_day(list);
		}else{
			ArrayList<String> list = new ArrayList<String>();
			for(int i=0;i<arrServiceIDCnt;i++){
				list.add("");
			}
			rVO.setList_repeat_day(list);
		}

	}
	
	/**
	 * 시청 예약 조회 API 유효성 검사
	 * @param rVO	시청 예약 정보 파라미터 클래스
	 * @throws SmartUXException	validate에 문제가 발생했을 경우 코드와 메시지가 셋팅되서 던져지는 SmartUXException 객체
	 */
	private void validateGetReservedProgramList(ReservedProgramParamVO rVO) throws SmartUXException{

		SmartUXException exception = new SmartUXException();
		
		// 가입 번호 파라미터 존재 유무
		if(!(StringUtils.hasText(rVO.getSa_id()))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "sa_id"));
			throw exception;
		}
		
		// 맥주소 파라미터 존재 유무
		if(!(StringUtils.hasText(rVO.getStb_mac()))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "stb_mac"));
			throw exception;
		}

		//어플타입 존재 유무 
		if(!(StringUtils.hasText(rVO.getApp_type()))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "app_type"));
			throw exception;
		}else{
//			//STB이 아닌 경우 CTN은 필수 항목
//			if(rVO.getApp_type().equalsIgnoreCase("ux")){	
//				rVO.setSma_mac("");
//			}else{
//				// CTN 파라미터 존재 유무
//				if(!(StringUtils.hasText(rVO.getSma_mac()))){
//					exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
//					exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "sma_mac"));
//					throw exception;
//				}
//			}
			//130703 타입 로직 변경
			if(rVO.getApp_type().equalsIgnoreCase("sma")){
				// CTN 파라미터 존재 유무
				if(!(StringUtils.hasText(rVO.getSma_mac()))){
					exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
					exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "sma_mac"));
					throw exception;
				}
			}else{
				rVO.setSma_mac("");
			}
		}
		
		// 검색 시작 인덱스가 있을 경우
		if(StringUtils.hasText(rVO.getStart_num())){
			
			int intStartnum = 0;
			// 검색 시작 인덱스가 숫자형인지 확인
			try{
				intStartnum = Integer.parseInt(rVO.getStart_num());
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
					if(!(StringUtils.hasText(rVO.getReq_count()))){			// req_count 파라미터가 없으면
						exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
						exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "req_count")); 
						throw exception;
					}
					
					int intReqcount = 0;
					
					try{
						intReqcount = Integer.parseInt(rVO.getReq_count());	// req_count 숫자형인지 확인
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
	}
	
	/**
	 * RegistrationID 등록/삭제 API 유효성 검사
	 * @param pVO	페어링 정보 파라미터 클래스
	 * @throws SmartUXException
	 */
	private void validateAddRegistrationID(RegistrationIDParamVO pVO) throws SmartUXException{
		SmartUXException exception = new SmartUXException();
		
		// 가입 번호 파라미터 존재 유무
		if(!(StringUtils.hasText(pVO.getSa_id()))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "sa_id"));
			throw exception;
		}
		
		// 맥주소 파라미터 존재 유무
		if(!(StringUtils.hasText(pVO.getStb_mac()))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "stb_mac"));
			throw exception;
		}
		
		// 셋탑 REG_ID 파라미터 존재 유무
		if(!(StringUtils.hasText(pVO.getSma_mac()))){
			if(pVO.getApp_type().equalsIgnoreCase("ux")){	
				if(!(StringUtils.hasText(pVO.getReg_id()))){
					exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
					exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "reg_id"));
					throw exception;
				}
			}
		}
		
		// 자사/타사 단말 구분 존재 유무
		if(!(StringUtils.hasText(pVO.getPushagent_yn()))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "pushagent_yn"));
			throw exception;
		}
		
		//어플타입 존재 유무 
		if(!(StringUtils.hasText(pVO.getApp_type()))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "app_type"));
			throw exception;
		}else{
			//STB이 아닌 경우 CTN은 필수 항목
			//130703 타입 로직 변경
			//if(!pVO.getApp_type().equalsIgnoreCase("ux")){	
			if(pVO.getApp_type().equalsIgnoreCase("sma")){	
				// SMA_MAC 파라미터 존재 유무
				if(!(StringUtils.hasText(pVO.getSma_mac()))){
					exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
					exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "sma_mac"));
					throw exception;
				}
				
				//자사단말일 경우 reg_id 필수 항목
				if(pVO.getPushagent_yn().equals("Y")){
					if(!(StringUtils.hasText(pVO.getReg_id()))){
						exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
						exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "reg_id"));
						throw exception;
					}
				}
			}
		}
	}
	
}// end BonbangController
