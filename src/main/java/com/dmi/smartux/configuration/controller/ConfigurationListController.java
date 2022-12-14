package com.dmi.smartux.configuration.controller;

import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
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
import com.dmi.smartux.configuration.service.ConfigurationListService;
import com.dmi.smartux.configuration.vo.ConfigurationListVO;

@Controller
public class ConfigurationListController {

	private final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	ConfigurationListService service;
	
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
	 * ConfigurationList ??? ???????????? ?????? ???????????? Controller ?????????
	 * @param sa_id			????????????
	 * @param stb_mac		?????????
	 * @param app_type		????????????
	 * @param callByScheduler	??????????????????
	 * @return				???????????? ???????????? ?????? ???????????? ????????? ConfigurationList ????????? ???????????? List ????????? ????????? ?????? Result ??????
	 * @throws Exception
	 */
	@RequestMapping(value="/getConfiguration")
	public Result<ConfigurationListVO> API_getConfiguration(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value="sa_id", 	 required=false) String sa_id,
			@RequestParam(value="stb_mac", 	 required=false) String stb_mac,
			@RequestParam(value="app_type",  required=false) String app_type,
			@RequestParam(value="callByScheduler", required=false, defaultValue="N") String callByScheduler,
			@RequestParam(value="config_id",  required=false) String config_id
			) throws Exception{	
		
		//#########[LOG START]#########
		Log log_logger = LogFactory.getLog("getConfiguration");
		long startTime = System.currentTimeMillis();
		String log_ip = request.getRemoteAddr();
		String log_url = request.getRequestURI();
		Enumeration log_penum = request.getParameterNames();
		String log_key = null;
		String log_value = null;
		String log_info_mac = "";
		String log_info_id = "";
		String log_info_app_type = "";
		String log_info_config_id = "";
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
			if(log_key.equalsIgnoreCase("config_id")){
				log_info_config_id = log_value;
			}
			log_debug += " ["+log_key+"="+log_value+"]";
		}
		
		log_logger.info("["+log_ip+"] ["+log_url+"][START] - ["+log_info_mac+"]["+log_info_id+"]["+log_info_app_type+"]["+log_info_config_id+"]");
		log_logger.debug("["+log_ip+"] ["+log_url+"][START] -"+log_debug);
	 
		logger.debug("sa_id : " 	+ sa_id);
		logger.debug("stb_mac : " 	+ stb_mac);
		logger.debug("app_type :" 	+ app_type);
		logger.debug("config_id : " + config_id);
		logger.debug("method : " 	+ request.getMethod());
		
		Result<ConfigurationListVO> result = new Result<ConfigurationListVO>();
		result.setRecordset(null);
		//result.setTotalcount(0);
		
		try {
			if("N".equals(callByScheduler)){		// ???????????? ????????? ??????
				getConfigurationListParamValidation(sa_id, stb_mac, app_type);
				
				result.setFlag(SmartUXProperties.getProperty("flag.success"));
				result.setMessage(SmartUXProperties.getProperty("message.success"));
				
				//????????? ??????????????? ????????? ??????(A0009)
				List<ConfigurationListVO> configChecklist = service.getConfigChecklist(app_type);
				
				if (configChecklist.size() == 0) {
					// ????????? ??? ?????? ??????
					int totalCount = 0;
					result.setTotal_count(totalCount);	
					
				}else{
					//?????? ???????????? ????????? ?????? (A0010)
					List<ConfigurationListVO> list = service.getConfigurationList(sa_id, stb_mac, app_type, callByScheduler, config_id);
					result.setRecordset(list);	
					
					// ????????? ??? ?????? ??????
					result.setTotal_count(list.size());
				}
				
			}else if("A".equals(callByScheduler)){
				List<ConfigurationListVO> list = service.getConfigurationList("", "", app_type, callByScheduler, config_id);
				result.setFlag(SmartUXProperties.getProperty("flag.success"));
				result.setMessage(SmartUXProperties.getProperty("message.success"));
				result.setRecordset(list);	
				result.setTotal_count(list.size());
				
				//response.setStatus(201);
			}else{
				List<ConfigurationListVO> list = service.getConfigurationList("", "", app_type, callByScheduler, config_id);
				result.setFlag(SmartUXProperties.getProperty("flag.success"));
				result.setMessage(SmartUXProperties.getProperty("message.success"));
				result.setRecordset(list);
				//response.setStatus(201);
			}
				
		
//////////////////////////////////////////////////////////////////////////////////////
//dummy API test process START			
//////////////////////////////////////////////////////////////////////////////////////
/*
			List<ConfigurationListVO> list = new ArrayList<ConfigurationListVO>();			
			ConfigurationListVO configvo;
			int limit = 5;									//?????? ????????? 10?????? ??????	
			
			for(int i=0 ; i < limit ; i++){
				configvo = new ConfigurationListVO();				
				configvo.setIdx(String.valueOf(i+1));
				//configvo.setSaid("209074797225__"+(i+1));
				//configvo.setMacaddr("0002.141b.b06d__"+(i+1));
				configvo.setConfig_id("AS_PhoneNumber__"+(i+1));
				configvo.setConfig_contents("???????????? 100???__"+(i+1));
				
				list.add(configvo);

			}						
*/			
//////////////////////////////////////////////////////////////////////////////////////
//dummy API test process END			
//////////////////////////////////////////////////////////////////////////////////////
			
		}catch(Exception e){
			log_logger.error("["+log_ip+"] ["+log_url+"]["+log_info_mac+"]["+log_info_id+"]["+log_info_app_type+"]"+"[getConfiguration]["+e.getClass().getName()+"]["+e.getMessage()+"]");
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
		
		// model.addAttribute("result", result.toString());
		return result;
	}
	
//	//@RequestMapping(value="/Configuration/sa_id/{sa_id}/stb_mac/{stb_mac}/app_type/{app_type}", method=RequestMethod.GET)
//	//@RequestMapping(value="/Configuration/**", method=RequestMethod.GET)
//	@RequestMapping(value="/v1/setting/**", method=RequestMethod.GET)
//	public Result<ConfigurationListVO> Restful_getConfiguration(
//			HttpServletRequest request,
//			HttpServletResponse response
////			@PathVariable String sa_id,
////			@PathVariable String stb_mac,
////			@PathVariable String app_type
//			) throws Exception{	
//		
//		//#########################[LOG SET]##########################
//		Log log_logger = LogFactory.getLog("OPENAPI_Configuration");
//		long startTime = System.currentTimeMillis();
//		String ip = request.getRemoteAddr();
//		String url = request.getRequestURI();
//		try{
//			url = "/v1/setting/";
//		}catch(Exception e){
//		}
//		String method = request.getMethod();
//		//#########################[//LOG SET]##########################
//		String access_key = null;
//		String cp_id = null;
//		
//		Result<ConfigurationListVO> result = new Result<ConfigurationListVO>();
//		result.setRecordset(null);
//		//result.setTotalcount(0);
//		
//		String sa_id = "";
//		String stb_mac = "";
//		String app_type = "";
//		
//		
//		
//		String uri = request.getRequestURI();
//		try {
//			HashMap<String, String> paramMap = GlobalCom.getRestfulParamMap(4,uri);
//			sa_id 		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "sa_id"));
//			stb_mac		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "stb_mac"));
//			app_type 	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "app_type"));
//		
//			access_key = GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "access_key"));
//			cp_id = GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "cp_id"));
//		
//			//#########################[LOG START]##########################
//			log_logger.info("["+ip+"] ["+url+"]["+method+"][START] - ["+stb_mac+"]["+sa_id+"]["+app_type+"]["+access_key+"]["+cp_id+"]");
//			//#########################[//LOG START]########################
//	
//			//access_key ?????? ?????? ??????
//			String authResult = GlobalCom.isNull(AuthenticationCommon.isAuthenticationMap(access_key,cp_id,"GET","/v1/setting"));
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
//			String callByScheduler = "N";
//		 
//			logger.debug("sa_id : " 	+ sa_id);
//			logger.debug("stb_mac : " 	+ stb_mac);
//			logger.debug("app_type :" 	+ app_type);
//			
//
//		
//			if("N".equals(callByScheduler)){		// ???????????? ????????? ??????						
//				getConfigurationListParamValidation(sa_id, stb_mac, app_type);
//				
//				result.setFlag(SmartUXProperties.getProperty("flag.success"));
//				result.setMessage(SmartUXProperties.getProperty("message.success"));
//				
//				List<ConfigurationListVO> configChecklist = service.getConfigChecklist(app_type);
//				
//				if (configChecklist.size() == 0) {
//					// ????????? ??? ?????? ??????
//					int totalCount = 0;
//					result.setTotal_count(totalCount);	
//					
//				}else{
//					List<ConfigurationListVO> list = service.getConfigurationList(sa_id, stb_mac, app_type , callByScheduler);
//					result.setRecordset(list);	
//					
//					// ????????? ??? ?????? ??????
//					result.setTotal_count(list.size());
//				}				
//				
//			}else if("A".equals(callByScheduler)){
//				List<ConfigurationListVO> list = service.getConfigurationList("", "", app_type, callByScheduler);
//				result.setFlag(SmartUXProperties.getProperty("flag.success"));
//				result.setMessage(SmartUXProperties.getProperty("message.success"));
//				result.setRecordset(list);	
//				result.setTotal_count(list.size());
//				
//				//response.setStatus(201);
//			}else{
//				List<ConfigurationListVO> list = service.getConfigurationList("", "", app_type, callByScheduler);
//				result.setFlag(SmartUXProperties.getProperty("flag.success"));
//				result.setMessage(SmartUXProperties.getProperty("message.success"));
//				result.setRecordset(list);
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
	
	@RequestMapping(value="/v1/setting", method=RequestMethod.GET)
	public Result<ConfigurationListVO> Restful_getConfiguration(
			HttpServletRequest request
			,HttpServletResponse response
			,@RequestParam(value="sa_id", 	 required=false) String sa_id
			,@RequestParam(value="stb_mac", 	 required=false) String stb_mac
			,@RequestParam(value="app_type",  required=false) String app_type
			,@RequestParam(value="access_key", required=false, defaultValue="") String access_key
			,@RequestParam(value="cp_id", required=false, defaultValue="") String cp_id
			) throws Exception{	
		
		SmartUXException exception = new SmartUXException();
		
		//#########################[LOG SET]##########################
		Log log_logger = LogFactory.getLog("OPENAPI_Configuration");
		long startTime = System.currentTimeMillis();
		String ip = request.getRemoteAddr();
		String url = request.getRequestURI();
		try{
			url = "/v1/setting/";
		}catch(Exception e){
		}
		String method = request.getMethod();
		//#########################[//LOG SET]##########################
//		String access_key = null;
//		String cp_id = null;
		
//		String sa_id = "";
//		String stb_mac = "";
//		String app_type = "";
		
		Result<ConfigurationListVO> result = new Result<ConfigurationListVO>();
		result.setRecordset(null);
		//result.setTotalcount(0);
		
		String uri = request.getRequestURI();
		try {
//			HashMap<String, String> paramMap = GlobalCom.getRestfulParamMap(4,uri);
//			sa_id 		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "sa_id"));
//			stb_mac		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "stb_mac"));
//			app_type 	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "app_type"));
//		
//			access_key = GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "access_key"));
//			cp_id = GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "cp_id"));
		
			//#########################[LOG START]##########################
			log_logger.info("["+ip+"] ["+url+"]["+method+"][START] - ["+stb_mac+"]["+sa_id+"]["+app_type+"]["+access_key+"]["+cp_id+"]");
			//#########################[//LOG START]########################
	
			//access_key ?????? ?????? ??????
			String authResult = GlobalCom.isNull(AuthenticationCommon.isAuthenticationMap(access_key,cp_id,"GET","/v1/setting"));
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
		 
			logger.debug("sa_id : " 	+ sa_id);
			logger.debug("stb_mac : " 	+ stb_mac);
			logger.debug("app_type :" 	+ app_type);
			

		
			if("N".equals(callByScheduler)){		// ???????????? ????????? ??????						
				getConfigurationListParamValidation(sa_id, stb_mac, app_type);
				
				result.setFlag(SmartUXProperties.getProperty("flag.success"));
				result.setMessage(SmartUXProperties.getProperty("message.success"));
				
				List<ConfigurationListVO> configChecklist = service.getConfigChecklist(app_type);
				
				if (configChecklist.size() == 0) {
					// ????????? ??? ?????? ??????
					int totalCount = 0;
					result.setTotal_count(totalCount);	
					
				}else{
					List<ConfigurationListVO> list = service.getConfigurationList(sa_id, stb_mac, app_type , callByScheduler);
					result.setRecordset(list);	
					
					// ????????? ??? ?????? ??????
					result.setTotal_count(list.size());
				}				
				
			}else if("A".equals(callByScheduler)){
				List<ConfigurationListVO> list = service.getConfigurationList("", "", app_type, callByScheduler);
				result.setFlag(SmartUXProperties.getProperty("flag.success"));
				result.setMessage(SmartUXProperties.getProperty("message.success"));
				result.setRecordset(list);	
				result.setTotal_count(list.size());
				
				//response.setStatus(201);
			}else{
				List<ConfigurationListVO> list = service.getConfigurationList("", "", app_type, callByScheduler);
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
	 * ConfigurationList ??????????????? ???????????? validation ?????????
	 * @param sa_id			????????????
	 * @param stb_mac		?????????
	 * @param app_type		????????????
	 * @param start_num		?????? ?????? ?????????(-1?????? req_count ?????? ???????????? ????????? ???????????? 0?????? ????????? ????????? ???????????? ????????????)
	 * @param req_count		?????? ????????? ??????
	 * @param genre_code	????????????
	 * @throws SmartUXException validation ??? ????????? ???????????? ?????? ????????? ???????????? ???????????? ???????????? SmartUXException ??????
	 */
	private void getConfigurationListParamValidation(String sa_id, String stb_mac, String app_type ) throws SmartUXException {
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
				
	}		
	
}