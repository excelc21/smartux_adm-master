package com.dmi.smartux.configuration.controller;

import java.util.Enumeration;
import java.util.HashMap;

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
import com.dmi.smartux.configuration.service.YoutubeAPIService;
import com.dmi.smartux.configuration.vo.YoutubeResultVO;

@Controller
public class YoutubeAPIController {
	
	private final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	YoutubeAPIService service;
	
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
	
	@RequestMapping(value="/getYoutubeSearchKey")
	public YoutubeResultVO API_getYoutubeSearchKey(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value="sa_id", 	 required=false) String sa_id,
			@RequestParam(value="stb_mac", 	 required=false) String stb_mac,
			@RequestParam(value="app_type",  required=false) String app_type,
			@RequestParam(value="callByScheduler", required=false, defaultValue="N") String callByScheduler
			){
		
		//#########[LOG START]#########
		Log log_logger = LogFactory.getLog("getYoutubeSearchKey");
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

		
		logger.debug("sa_id : " 	+ sa_id);
		logger.debug("stb_mac : " 	+ stb_mac);
		logger.debug("app_type :" 	+ app_type);
		
		YoutubeResultVO result = new YoutubeResultVO();
		
		try{
			
			if("N".equals(callByScheduler)){		// 단말에서 호출한 경우
				validateGetYoutubeSearchKey(sa_id, stb_mac, app_type);
				result = service.getYoutubeSearchKey(sa_id, stb_mac, app_type , callByScheduler);
			}else if("A".equals(callByScheduler)){		// 관리자에서 호출한 경우
				result = service.getYoutubeSearchKey("", "", app_type , callByScheduler);
				//response.setStatus(201);
			}else{
				result = service.getYoutubeSearchKey("", "", app_type , callByScheduler);
				//response.setStatus(201);
			}
		}catch(Exception e){
			//logger.error("getYoutubeSearchKey "+e.getClass().getName());
			//logger.error("getYoutubeSearchKey "+e.getMessage());
			//logger.error("["+log_ip+"] ["+log_url+"]["+log_info_mac+"]["+log_info_id+"]["+log_info_app_type+"]"+"[getYoutubeSearchKey]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			log_logger.error("["+log_ip+"] ["+log_url+"]["+log_info_mac+"]["+log_info_id+"]["+log_info_app_type+"]"+"[getYoutubeSearchKey]["+e.getClass().getName()+"]["+e.getMessage()+"]");
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
	
//	//@RequestMapping(value="/YoutubeSearchKey/sa_id/{sa_id}/stb_mac/{stb_mac}/app_type/{app_type}", method=RequestMethod.GET)
//	//@RequestMapping(value="/YoutubeSearchKey/**", method=RequestMethod.GET)
//	@RequestMapping(value="/v1/search/youtube/**", method=RequestMethod.GET)
//	public YoutubeResultVO Restful_getYoutubeSearchKey(
//			HttpServletRequest request,
//			HttpServletResponse response
////			,@PathVariable String sa_id
////			,@PathVariable String stb_mac
////			,@PathVariable String app_type
//			){
//		
//		
//		//#########################[LOG SET]##########################
//		Log log_logger = LogFactory.getLog("OPENAPI_YoutubeSearchKey");
//		long startTime = System.currentTimeMillis();
//		String ip = request.getRemoteAddr();
//		String url = request.getRequestURI();
//		try{
//			url = "/v1/search/youtube/";
//		}catch(Exception e){
//		}
//		String method = request.getMethod();
//		//#########################[//LOG SET]##########################
//		
//		YoutubeResultVO result = new YoutubeResultVO();
//		
//		
//		String sa_id = "";
//		String stb_mac = "";
//		String app_type = "";
//		
//		String access_key = null;
//		String cp_id = null;
//		
//		String uri = request.getRequestURI();
//		
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
//			//access_key 인증 허용 체크
//			String authResult = GlobalCom.isNull(AuthenticationCommon.isAuthenticationMap(access_key,cp_id,"GET","/v1/search/youtube"));
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
//			//#########################[LOG START]##########################
//			log_logger.info("["+ip+"] ["+url+"]["+method+"][START] - ["+stb_mac+"]["+sa_id+"]["+app_type+"]["+access_key+"]["+cp_id+"]");
//			//#########################[//LOG START]########################
//			
//			logger.debug("###############getYoutubeSearchKey getYoutubeSearchKeyRestful 실행!!!!!!!!!!  ##############");
//			logger.debug("sa_id : " 	+ sa_id);
//			logger.debug("stb_mac : " 	+ stb_mac);
//			logger.debug("app_type :" 	+ app_type);
//			logger.debug("access_key :" 	+ access_key);
//			logger.debug("cp_id :" 	+ cp_id);
//			
//			//단말에서만 OPEN_API 사용
//			String callByScheduler ="N";
//		
//		
//			if("N".equals(callByScheduler)){		// 단말에서 호출한 경우
//				validateGetYoutubeSearchKey(sa_id, stb_mac, app_type);
//				result = service.getYoutubeSearchKey(sa_id, stb_mac, app_type , callByScheduler);
//			}else if("A".equals(callByScheduler)){		// 관리자에서 호출한 경우
//				result = service.getYoutubeSearchKey("", "", app_type , callByScheduler);
//			}else{
//				result = service.getYoutubeSearchKey("", "", app_type , callByScheduler);
//			}
//		}catch(Exception e){
//			//logger.error("getYoutubeSearchKey "+e.getClass().getName());
//			//logger.error("getYoutubeSearchKey "+e.getMessage());
//			//logger.error("[getYoutubeSearchKey]["+e.getClass().getName()+"]["+e.getMessage()+"]");
//			log_logger.error("["+ip+"] ["+url+"]["+stb_mac+"]["+sa_id+"]["+app_type+"]["+access_key+"]["+cp_id+"]["+e.getClass().getName()+"]["+e.getMessage()+"]");
//			ExceptionHandler handler = new ExceptionHandler(e);
//			result.setFlag(handler.getFlag());
//			result.setMessage(handler.getMessage());
//		}
//		
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
	
	
	@RequestMapping(value="/v1/search/youtube", method=RequestMethod.GET)
	public YoutubeResultVO Restful_getYoutubeSearchKey(
			HttpServletRequest request
			,HttpServletResponse response
			,@RequestParam(value="sa_id", 	 required=false) String sa_id
			,@RequestParam(value="stb_mac", 	 required=false) String stb_mac
			,@RequestParam(value="app_type",  required=false) String app_type
			,@RequestParam(value="access_key", required=false, defaultValue="") String access_key
			,@RequestParam(value="cp_id", required=false, defaultValue="") String cp_id
			)throws Exception{
		
		SmartUXException exception = new SmartUXException();
		
		//#########################[LOG SET]##########################
		Log log_logger = LogFactory.getLog("OPENAPI_YoutubeSearchKey");
		long startTime = System.currentTimeMillis();
		String ip = request.getRemoteAddr();
		String url = request.getRequestURI();
		try{
			url = "/v1/search/youtube/";
		}catch(Exception e){
		}
		String method = request.getMethod();
		//#########################[//LOG SET]##########################
		
		YoutubeResultVO result = new YoutubeResultVO();
		
		
//		String sa_id = "";
//		String stb_mac = "";
//		String app_type = "";
//		
//		String access_key = null;
//		String cp_id = null;
		
		String uri = request.getRequestURI();
		
		try {
//			HashMap<String, String> paramMap = GlobalCom.getRestfulParamMap(5,uri);
//			sa_id 		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "sa_id"));
//			stb_mac		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "stb_mac"));
//			app_type 	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "app_type"));
//			
//			access_key = GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "access_key"));
//			cp_id = GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "cp_id"));
			
			
			//access_key 인증 허용 체크
			String authResult = GlobalCom.isNull(AuthenticationCommon.isAuthenticationMap(access_key,cp_id,"GET","/v1/search/youtube"));
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

			
			//#########################[LOG START]##########################
			log_logger.info("["+ip+"] ["+url+"]["+method+"][START] - ["+stb_mac+"]["+sa_id+"]["+app_type+"]["+access_key+"]["+cp_id+"]");
			//#########################[//LOG START]########################
			
			logger.debug("###############getYoutubeSearchKey getYoutubeSearchKeyRestful 실행!!!!!!!!!!  ##############");
			logger.debug("sa_id : " 	+ sa_id);
			logger.debug("stb_mac : " 	+ stb_mac);
			logger.debug("app_type :" 	+ app_type);
			logger.debug("access_key :" 	+ access_key);
			logger.debug("cp_id :" 	+ cp_id);
			
			//단말에서만 OPEN_API 사용
			String callByScheduler ="N";
		
		
			if("N".equals(callByScheduler)){		// 단말에서 호출한 경우
				validateGetYoutubeSearchKey(sa_id, stb_mac, app_type);
				result = service.getYoutubeSearchKey(sa_id, stb_mac, app_type , callByScheduler);
			}else if("A".equals(callByScheduler)){		// 관리자에서 호출한 경우
				result = service.getYoutubeSearchKey("", "", app_type , callByScheduler);
			}else{
				result = service.getYoutubeSearchKey("", "", app_type , callByScheduler);
			}
		} catch(SmartUXException e){
			log_logger.error("["+ip+"] ["+url+"]["+stb_mac+"]["+sa_id+"]["+app_type+"]["+access_key+"]["+cp_id+"]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			throw e;		
		}catch(Exception e){
			//logger.error("getYoutubeSearchKey "+e.getClass().getName());
			//logger.error("getYoutubeSearchKey "+e.getMessage());
			//logger.error("[getYoutubeSearchKey]["+e.getClass().getName()+"]["+e.getMessage()+"]");
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
		
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private void validateGetYoutubeSearchKey(String sa_id, String stb_mac, String app_type)  throws SmartUXException {
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
				
	}
}
