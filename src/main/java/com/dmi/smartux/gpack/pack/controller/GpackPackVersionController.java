package com.dmi.smartux.gpack.pack.controller;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dmi.smartux.common.exception.ExceptionHandler;
import com.dmi.smartux.common.exception.SmartUXException;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.CharacterSet;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.gpack.pack.service.GpackPackVersionService;
import com.dmi.smartux.gpack.pack.vo.GPackPackVersionVO;

@Controller
public class GpackPackVersionController {

	private final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	GpackPackVersionService service;
	
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
	 * Pack ?????? ????????? ??????
	 * @param sa_id			????????????
	 * @param stb_mac		?????????
	 * @param app_type		????????????(UX : ??????, SMA : ????????????)
	 * @param pack_id		???ID
	 * @param callByScheduler	???????????? ?????? ?????? ????????? N, ?????????????????? ??????????????? Y, ?????????????????? ??????????????? A??? ????????????
	 * @return
	 */
	@RequestMapping(value="/gpack/getPackVersion")
	public GPackPackVersionVO API_getPackVersion(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value="sa_id", required=false) String sa_id,
			@RequestParam(value="stb_mac", required=false) String stb_mac,
			@RequestParam(value="app_type", required=false) String app_type,
			@RequestParam(value="pack_id", required=false) String pack_id,
			@RequestParam(value="callByScheduler", required=false, defaultValue="N") String callByScheduler){
		
		//#########[LOG START]#########
		Log log_logger = LogFactory.getLog("getPackVersion");
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

		
		GPackPackVersionVO result = new GPackPackVersionVO();
		
		try{
			if("N".equals(callByScheduler)){		// ???????????? ????????? ??????
				validateGetPackVersion(sa_id, stb_mac, app_type, pack_id);
				// ????????? Main Panel ?????? ?????? ??????
				String version = service.getPackVersion(sa_id, stb_mac, app_type, callByScheduler, pack_id);
				result.setFlag(SmartUXProperties.getProperty("flag.success"));
				result.setMessage(SmartUXProperties.getProperty("message.success"));
				result.setPack_version(version);
			
			}else if("A".equals(callByScheduler)){
				String version = service.getPackVersion("", "", "", callByScheduler, pack_id);
				result.setFlag(SmartUXProperties.getProperty("flag.success"));
				result.setMessage(SmartUXProperties.getProperty("message.success"));
				result.setPack_version(version);
			}else{
				String version = service.getPackVersion("", "", "", callByScheduler, pack_id);
				result.setFlag(SmartUXProperties.getProperty("flag.success"));
				result.setMessage(SmartUXProperties.getProperty("message.success"));
				result.setPack_version(version);
			}
		}catch(Exception e){
			log_logger.error("["+log_ip+"] ["+log_url+"]["+log_info_mac+"]["+log_info_id+"]["+log_info_app_type+"]"+"[getPackVersion]["+e.getClass().getName()+"]["+e.getMessage()+"]");
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
	
	/**
	 * getPackVersion ???????????? ???????????? validation ??????
	 * @param sa_id			????????????
	 * @param stb_mac		?????????
	 * @param app_type		????????????
	 * @param pack_id		???ID
	 * @throws SmartUXException validate??? ????????? ???????????? ?????? ????????? ???????????? ???????????? ???????????? SmartUXException ??????
	 */
	private void validateGetPackVersion(String sa_id, String stb_mac, String app_type, String pack_id) throws SmartUXException {
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
		
		// ???ID
		
		if(!(StringUtils.hasText(pack_id))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "pack_id"));
			throw exception;
		}
		
	}
	
}
