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
	 * Main Panel ?????? ????????? ??????
	 * @param sa_id			????????????
	 * @param stb_mac		?????????
	 * @param app_type		????????????(UX : ??????, SMA : ????????????)
	 * @param callByScheduler	???????????? ?????? ?????? ????????? N, ?????????????????? ??????????????? Y, ?????????????????? ??????????????? A??? ????????????
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
			if("N".equals(callByScheduler)){		// ???????????? ????????? ??????
				validateGetMainPanelVersionInfo(sa_id, stb_mac, app_type);
				// ????????? Main Panel ?????? ?????? ??????
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
//			//??????????????? OPEN_API ??????
//			String callByScheduler ="N";
//			
//			//access_key ?????? ?????? ??????
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
//			if("N".equals(callByScheduler)){		// ???????????? ????????? ??????
//				validateGetMainPanelVersionInfo(sa_id, stb_mac, app_type);
//				// ????????? Main Panel ?????? ?????? ??????
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
			
	
			//??????????????? OPEN_API ??????
			String callByScheduler ="N";
			
			//access_key ?????? ?????? ??????
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
		

			if("N".equals(callByScheduler)){		// ???????????? ????????? ??????
				validateGetMainPanelVersionInfo(sa_id, stb_mac, app_type);
				// ????????? Main Panel ?????? ?????? ??????
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
	 * Main Panel ?????? ????????? ??????
	 * @param sa_id			????????????
	 * @param stb_mac		?????????
	 * @param app_type		????????????(UX : ??????, SMA : ????????????)
	 * @param pannel_id		?????? ????????? ?????? ???????????? ?????? ???????????? ?????? ID(ex:CP01), ??? ???????????? ????????? ???????????? ??????(?????????????????? ?????? ?????? ????????? ??????)
	 * @param callByScheduler	???????????? ?????? ?????? ????????? N, ?????????????????? ??????????????? Y, ?????????????????? ??????????????? A??? ????????????
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
			if("N".equals(callByScheduler)){		// ???????????? ????????? ??????
				validateGetMainPanelInterlockingInfo(sa_id, stb_mac, app_type, panel_id);
				result.setFlag(SmartUXProperties.getProperty("flag.success"));
				result.setMessage(SmartUXProperties.getProperty("message.success"));
			
				// ???????????? ??????
				String version = service.getMainPanelVersionInfo(sa_id, stb_mac, app_type, callByScheduler);
				result.setVersion(version);
			
				// ????????? ???????????? ??????
				List<MainPanelInfoVO> list = service.getMainPanelInterlockingInfo(sa_id, stb_mac, app_type, panel_id, callByScheduler);
				result.setRecordset(list);
			
				// ????????? ??? ?????? ??????
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
//			//access_key ?????? ?????? ??????
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
//			//??????????????? OPEN_API ??????
//			String callByScheduler ="N";
//		
//		
//			if("N".equals(callByScheduler)){		// ???????????? ????????? ??????
//				validateGetMainPanelInterlockingInfo(sa_id, stb_mac, app_type, panel_id);
//				result.setFlag(SmartUXProperties.getProperty("flag.success"));
//				result.setMessage(SmartUXProperties.getProperty("message.success"));
//			
//				// ???????????? ??????
//				String version = service.getMainPanelVersionInfo(sa_id, stb_mac, app_type, callByScheduler);
//				result.setVersion(version);
//			
//				// ????????? ???????????? ??????
//				List<MainPanelInfoVO> list = service.getMainPanelInterlockingInfo(sa_id, stb_mac, app_type, panel_id, callByScheduler);
//				result.setRecordset(list);
//			
//				// ????????? ??? ?????? ??????
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
			
			//access_key ?????? ?????? ??????
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
			
			//??????????????? OPEN_API ??????
			String callByScheduler ="N";
		
		
			if("N".equals(callByScheduler)){		// ???????????? ????????? ??????
				validateGetMainPanelInterlockingInfo(sa_id, stb_mac, app_type, panel_id);
				result.setFlag(SmartUXProperties.getProperty("flag.success"));
				result.setMessage(SmartUXProperties.getProperty("message.success"));
			
				// ???????????? ??????
				String version = service.getMainPanelVersionInfo(sa_id, stb_mac, app_type, callByScheduler);
				result.setVersion(version);
			
				// ????????? ???????????? ??????
				List<MainPanelInfoVO> list = service.getMainPanelInterlockingInfo(sa_id, stb_mac, app_type, panel_id, callByScheduler);
				result.setRecordset(list);
			
				// ????????? ??? ?????? ??????
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
	 * I20 ?????? ?????? ??????
	 * @param request			HttpServletRequest ??????
	 * @param response			HttpServletResponse ??????
	 * @param sa_id				????????????
	 * @param stb_mac			?????????
	 * @param app_type			????????????
	 * @param start_num			?????? ?????? ?????????(-1?????? req_count ?????? ???????????? ????????? ????????????)
	 * @param req_count			?????? ????????? ??????
	 * @param category_id		??????????????? ?????? ????????? ?????? category_id(2??? ????????? ?????? ||??? ???????????? ??????)
	 * @param callByScheduler	???????????? ?????? ?????? ????????? N, ?????????????????? ??????????????? Y, ?????????????????? ??????????????? A??? ????????????
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
			
			//???????????? 01,02,31.. ?????? ??????????????? ?????? A,B ?????? ?????? ????????? ???????????? ????????? ????????????.
			int fh_gbn = 9999;
			try{
				int tmpGbn = Integer.parseInt(net_typ);
				if(tmpGbn==1 || tmpGbn==2){//fullHD ????????? ?????? ???
					fh_gbn = 1;
				}else{
					fh_gbn = 2;
				}
			}catch(Exception e){
				net_typ = "FAIL";
			}
			
			if("N".equals(callByScheduler)){		// ???????????? ????????? ??????
				validateGetI20AlbumList(sa_id, stb_mac, app_type, start_num, req_count, category_id, net_typ);
				result.setFlag(SmartUXProperties.getProperty("flag.success"));
				result.setMessage(SmartUXProperties.getProperty("message.success"));
				
				// ????????? ??? ?????? ??????
				int totalCount = service.getI20AlbumListCount(sa_id, stb_mac, app_type, category_id, start_num, req_count, callByScheduler);
				result.setTotal_count(totalCount);
				
				// ??????????????? ????????? ?????? ??????, ?????? ????????? ????????? ?????? ???????????? ????????????
				int intStartnum = Integer.parseInt(start_num);
				if((intStartnum > 0) || (intStartnum == -1)){					// ?????? ?????? ???????????? -1 ?????? 1 ??????????????? ?????????
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
//			//access_key ?????? ?????? ??????
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
//			//??????????????? OPEN_API ??????
//			String callByScheduler = "N";
//		
//			if("N".equals(callByScheduler)){		// ???????????? ????????? ??????
//				validateGetI20AlbumList(sa_id, stb_mac, app_type, start_num, req_count, category_id);
//				result.setFlag(SmartUXProperties.getProperty("flag.success"));
//				result.setMessage(SmartUXProperties.getProperty("message.success"));
//				
//				// ????????? ??? ?????? ??????
//				int totalCount = service.getI20AlbumListCount(sa_id, stb_mac, app_type, category_id, start_num, req_count, callByScheduler);
//				result.setTotal_count(totalCount);
//				
//				// ??????????????? ????????? ?????? ??????, ?????? ????????? ????????? ?????? ???????????? ????????????
//				int intStartnum = Integer.parseInt(start_num);
//				if((intStartnum > 0) || (intStartnum == -1)){					// ?????? ?????? ???????????? -1 ?????? 1 ??????????????? ?????????
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
	
			//access_key ?????? ?????? ??????
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
			
			
		
			//??????????????? OPEN_API ??????
			String callByScheduler = "N";
		
			//???????????? 01,02,31.. ?????? ??????????????? ?????? A,B ?????? ?????? ????????? ???????????? ????????? ????????????.
			int fh_gbn = 9999;
			try{
				int tmpGbn = Integer.parseInt(net_typ);
				if(tmpGbn==1 || tmpGbn==2){//fullHD ????????? ?????? ???
					fh_gbn = 1;
				}else{
					fh_gbn = 2;
				}
			}catch(Exception e){
				net_typ = "FAIL";
			}
			
			if("N".equals(callByScheduler)){		// ???????????? ????????? ??????
				validateGetI20AlbumList(sa_id, stb_mac, app_type, start_num, req_count, category_id, net_typ);
				result.setFlag(SmartUXProperties.getProperty("flag.success"));
				result.setMessage(SmartUXProperties.getProperty("message.success"));
				
				// ????????? ??? ?????? ??????
				int totalCount = service.getI20AlbumListCount(sa_id, stb_mac, app_type, category_id, start_num, req_count, callByScheduler);
				result.setTotal_count(totalCount);
				
				// ??????????????? ????????? ?????? ??????, ?????? ????????? ????????? ?????? ???????????? ????????????
				int intStartnum = Integer.parseInt(start_num);
				if((intStartnum > 0) || (intStartnum == -1)){					// ?????? ?????? ???????????? -1 ?????? 1 ??????????????? ?????????
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
	 * getMainPanelVersionInfo ???????????? ???????????? validation ??????
	 * @param sa_id			????????????
	 * @param stb_mac		?????????
	 * @param app_type		????????????
	 * @throws SmartUXException validate??? ????????? ???????????? ?????? ????????? ???????????? ???????????? ???????????? SmartUXException ??????
	 */
	private void validateGetMainPanelVersionInfo(String sa_id, String stb_mac, String app_type) throws SmartUXException {
		SmartUXException exception = new SmartUXException();
		
		// ?????? ?????? ???????????? ?????? ??????
		if(!(StringUtils.hasText(sa_id))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "sa_id"));
			throw exception;
		}
		
		// ????????? ???????????? ?????? ??????
		
		if(!(StringUtils.hasText(stb_mac))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "stb_mac"));
			throw exception;
		}
		
		// ?????? ?????? ???????????? ?????? ??????
		
		if(!(StringUtils.hasText(app_type))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "app_type"));
			throw exception;
		}
//		130703 ???????????? ??????
//		else{
//			// ?????? ?????? ???????????? ?????? SETTOP?????? ??????
//			if(!("UX".equals(app_type)) && !("SMA".equals(app_type))){
//				exception.setFlag(SmartUXProperties.getProperty("flag.mismatchvalue"));
//				exception.setMessage(SmartUXProperties.getProperty("message.mismatchvalue", "app_type", "UX ????????? SMA"));
//				throw exception;
//			}
//		}
	}
	
	/**
	 * getMainPanelInterlockingInfo ???????????? ???????????? validation ??????
	 * @param sa_id			????????????
	 * @param stb_mac		?????????
	 * @param app_type		????????????
	 * @throws SmartUXException validate??? ????????? ???????????? ?????? ????????? ???????????? ???????????? ???????????? SmartUXException ??????
	 */
	private void validateGetMainPanelInterlockingInfo(String sa_id, String stb_mac, String app_type, String panel_id) throws SmartUXException {
		SmartUXException exception = new SmartUXException();
		
		// ?????? ?????? ???????????? ?????? ??????
		if(!(StringUtils.hasText(sa_id))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "sa_id"));
			throw exception;
		}
		
		// ????????? ???????????? ?????? ??????
		
		if(!(StringUtils.hasText(stb_mac))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "stb_mac"));
			throw exception;
		}
		
		// ?????? ?????? ???????????? ?????? ??????
		
		if(!(StringUtils.hasText(app_type))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "app_type"));
			throw exception;
		}
//		130703 ???????????? ??????
//		else{
//			// ?????? ?????? ???????????? ?????? SETTOP?????? ??????
//			if(!("UX".equals(app_type)) && !("SMA".equals(app_type))){
//				exception.setFlag(SmartUXProperties.getProperty("flag.mismatchvalue"));
//				exception.setMessage(SmartUXProperties.getProperty("message.mismatchvalue", "app_type", "UX ????????? SMA"));
//				throw exception;
//			}
//		}
		
		//?????? ????????? ?????? ???????????? ?????? ???????????? ??????
		if(!(StringUtils.hasText(panel_id))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "panel_id"));
			throw exception;
		}
	}
	
	/**
	 * getI20AlbumList ???????????? ???????????? validation ??????
	 * @param sa_id			????????????
	 * @param stb_mac		?????????
	 * @param app_type		????????????
	 * @param start_num		?????? ?????? ?????????(-1?????? req_count ?????? ???????????? ????????? ???????????? 0?????? ????????? ????????? ???????????? ????????????)
	 * @param req_count		?????? ????????? ??????
	 * @param category_id	|| ???????????? ????????? ???????????? ??????
	 * @throws SmartUXException validate??? ????????? ???????????? ?????? ????????? ???????????? ???????????? ???????????? SmartUXException ??????
	 */
	private void validateGetI20AlbumList(String sa_id, String stb_mac, String app_type, String start_num, String req_count, String category_id, String net_typ) throws SmartUXException {
		SmartUXException exception = new SmartUXException();
		
		// ?????? ?????? ???????????? ?????? ??????
		if(!(StringUtils.hasText(sa_id))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "sa_id"));
			throw exception;
		}
		
		// ????????? ???????????? ?????? ??????
		
		if(!(StringUtils.hasText(stb_mac))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "stb_mac"));
			throw exception;
		}
		
		// ?????? ?????? ???????????? ?????? ??????
		
		if(!(StringUtils.hasText(app_type))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "app_type"));
			throw exception;
		}
//		130703 ???????????? ??????
//		else{
//			// ?????? ?????? ???????????? ?????? SETTOP?????? ??????
//			/*
//			if(!("UX".equals(app_type))){
//				exception.setFlag(SmartUXProperties.getProperty("flag.mismatchvalue"));
//				exception.setMessage(SmartUXProperties.getProperty("message.mismatchvalue", "app_type", "UX"));
//				throw exception;
//			}
//			*/
//			if(!("UX".equals(app_type)) && !("SMA".equals(app_type))){
//				exception.setFlag(SmartUXProperties.getProperty("flag.mismatchvalue"));
//				exception.setMessage(SmartUXProperties.getProperty("message.mismatchvalue", "app_type", "UX ????????? SMA"));
//				throw exception;
//			}
//		}
		
		//???????????? ???????????? ?????? ??????
		if("FAIL".equals(net_typ)){
			exception.setFlag(SmartUXProperties.getProperty("flag.format"));
			exception.setMessage(SmartUXProperties.getProperty("message.format", "net_typ"));
			throw exception;
		}
		
		// ?????? ?????? ???????????? ?????? ??????
		if(StringUtils.hasText(start_num)){
			
			int intStartnum = 0;
			// ?????? ?????? ???????????? ??????????????? ??????
			try{
				intStartnum = Integer.parseInt(start_num);
			}catch(NumberFormatException e){
				exception.setFlag(SmartUXProperties.getProperty("flag.numberformat"));
				exception.setMessage(SmartUXProperties.getProperty("message.numberformat" , "start_num"));
				throw exception;
			}
			
			// ?????? ?????? ???????????? -1 ????????? ??????
			if(intStartnum >= -1){
				if(intStartnum == -1){			// ?????? ?????? ???????????? -1??? ??????(?????? ??????)
					
				}else if(intStartnum == 0){		// ?????? ?????? ???????????? 0??? ??????(????????? ????????? ???????????? return ?????? ??????) 
					
				}else{							// ?????? ?????? ???????????? 1 ????????? ??????(?????????)
					if(!(StringUtils.hasText(req_count))){			// req_count ??????????????? ?????????
						exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
						exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "req_count")); 
						throw exception;
					}
					
					int intReqcount = 0;
					
					try{
						intReqcount = Integer.parseInt(req_count);	// req_count ??????????????? ??????
					}catch(NumberFormatException e){
						exception.setFlag(SmartUXProperties.getProperty("flag.numberformat"));
						exception.setMessage(SmartUXProperties.getProperty("message.numberformat", "req_count"));
						throw exception;
					}
					
					if(intReqcount <= 0){							// req_count ???????????? ?????? 0????????????(req_count ??????????????? 1 ?????? ???????????? ??????)
						exception.setFlag(SmartUXProperties.getProperty("flag.range2"));
						exception.setMessage(SmartUXProperties.getProperty("message.range2", "req_count", "1"));
						throw exception;
					}
				}
			}else{	// ?????? ?????? ???????????? -1 ????????? ??????
				exception.setFlag(SmartUXProperties.getProperty("flag.range2"));
				exception.setMessage(SmartUXProperties.getProperty("message.range2", "start_num", "-1"));
				throw exception;
			}
		}else{	// ?????? ?????? ???????????? ?????? ??????
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "start_num"));
			throw exception;
		}
		
		// ???????????? ?????? ???????????? ?????? ??????
		
		if(!(StringUtils.hasText(category_id))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "category_id"));
			throw exception;
		}
	}
}
