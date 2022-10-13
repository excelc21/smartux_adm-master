package com.dmi.smartux.noticeinfo.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dmi.smartux.admin.rank.vo.EcrmRankVO;
import com.dmi.smartux.authentication.vo.AuthenticationCommon;
import com.dmi.smartux.common.exception.ExceptionHandler;
import com.dmi.smartux.common.exception.SmartUXException;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.CharacterSet;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.vo.Result;
import com.dmi.smartux.noticeinfo.service.NoticeInfoService;
import com.dmi.smartux.noticeinfo.vo.CacheNoticeInfoListVo;
import com.dmi.smartux.smartstart.vo.SUXMAlbumListVO;

@Controller
public class NoticeInfoControll {
	
	@Autowired
	NoticeInfoService service;
	
	private final Log logger = LogFactory.getLog(this.getClass());
    
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
	 * NoticeInfo 캐쉬 조회/등록
	 * @param request
	 * @param response
	 * @param p_service
	 * @param ntype
	 * @param category
	 * @param callByScheduler
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/getNoticeInfo")
	public Result<CacheNoticeInfoListVo> getNoticeInfoApi(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value="service", 	 required=false) String p_service,
			@RequestParam(value="ntype", 	 required=false) String ntype,
			@RequestParam(value="category",  required=false) String category,
			@RequestParam(value="callByScheduler", required=false, defaultValue="N") String callByScheduler
			) throws Exception{
		
		//#########[LOG START]#########
		Log log_logger = LogFactory.getLog("getNoticeInfo");
		long startTime = System.currentTimeMillis();
		String log_ip = request.getRemoteAddr();
		String log_url = request.getRequestURI();
		Enumeration log_penum = request.getParameterNames();
		String log_key = null;
		String log_value = null;
		String log_service = "";
		String log_ntype = "";
		String log_category = "";
		String log_debug = "";

		while(log_penum.hasMoreElements()){
			log_key = (String)log_penum.nextElement();
			log_value = (new String(request.getParameter(log_key)) == null) ? "" : new String(request.getParameter(log_key));
			
			if(log_key.equalsIgnoreCase("service")){
				log_service = log_value;
			}
			if(log_key.equalsIgnoreCase("ntype")){
				log_ntype = log_value;
			}
			if(log_key.equalsIgnoreCase("category")){
				log_category = log_value;
			}
			log_debug += " ["+log_key+"="+log_value+"]";
		}

		log_logger.info("["+log_ip+"] ["+log_url+"][START] - ["+log_service+"]["+log_ntype+"]["+log_category+"]");
		log_logger.debug("["+log_ip+"] ["+log_url+"][START] -"+log_debug);
		
		Result<CacheNoticeInfoListVo> result = new Result<CacheNoticeInfoListVo>();
		result.setRecordset(null);

		try {
			
			if("N".equals(callByScheduler)) {		// 단말에서 호출한 경우
				API_getNoticeInfoParamValidation(p_service);								
				result.setFlag(SmartUXProperties.getProperty("flag.success"));
				result.setMessage(SmartUXProperties.getProperty("message.success"));
				
				List<CacheNoticeInfoListVo> list = service.getNoticeInfoApi(p_service,ntype,category,callByScheduler);
				result.setTotal_count(list.size());
				result.setRecordset(list);
			}else{	//A:관리자에서 호출(즉시적용), Y:스케쥴러에서 호출					
				result.setFlag(SmartUXProperties.getProperty("flag.success"));
				result.setMessage(SmartUXProperties.getProperty("message.success"));
				
				service.getNoticeInfoApi("","","",callByScheduler);
			}
		}catch(Exception e){
			log_logger.error("["+log_ip+"] ["+log_url+"]["+log_service+"]["+log_ntype+"]["+log_category+"]"+"[getNoticeInfo]["+e.getClass().getName()+"]["+e.getMessage()+"]");
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
		log_logger.info("["+log_ip+"] ["+log_url+"][END]["+(timeStr)+" sec] - ["+log_service+"]["+log_ntype+"]["+log_category+"]");
		
		return result;	
		
	}

	/**
	 * NoticeInfo 캐쉬 조회/등록(openAPI)
	 * @param request
	 * @param response
	 * @param p_service
	 * @param ntype
	 * @param category
	 * @param callByScheduler
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/v1/noticeinfo")
	public Result<CacheNoticeInfoListVo> noticeInfoOpenApi(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value="service", 	 required=false) String p_service,
			@RequestParam(value="ntype", 	 required=false) String ntype,
			@RequestParam(value="category",  required=false) String category,
			@RequestParam(value="access_key", required=false, defaultValue="") String access_key,
			@RequestParam(value="cp_id", required=false, defaultValue="") String cp_id
			) throws Exception{
		
		SmartUXException exception = new SmartUXException();
		
		//#########################[LOG SET]##########################
		Log log_logger = LogFactory.getLog("OPENAPI_noticeinfo");
		long startTime = System.currentTimeMillis();
		String ip = request.getRemoteAddr();
		String url = request.getRequestURI();
		try{
			url = "/v1/noticeinfo/";
		}catch(Exception e){
		}
		String method = request.getMethod();
		//#########################[//LOG SET]##########################
		
		Result<CacheNoticeInfoListVo> result = new Result<CacheNoticeInfoListVo>();
		result.setRecordset(null);

		try {
		
			//#########################[LOG START]##########################
			log_logger.info("["+ip+"] ["+url+"]["+method+"][START] - ["+p_service+"]["+ntype+"]["+category+"]["+access_key+"]["+cp_id+"]");
			//#########################[//LOG START]########################
			
				OpenAPI_getNoticeInfoParamValidation(access_key,cp_id,p_service);
				
				//access_key 인증 허용 체크
				String authResult = GlobalCom.isNull(AuthenticationCommon.isAuthenticationMap(access_key,cp_id,"GET","/v1/noticeinfo"));
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
				
				
				result.setFlag(SmartUXProperties.getProperty("flag.success"));
				result.setMessage(SmartUXProperties.getProperty("message.success"));
				
				List<CacheNoticeInfoListVo> list = service.getNoticeInfoApi(p_service,ntype,category,"N");
				result.setTotal_count(list.size());
				result.setRecordset(list);
		} catch(SmartUXException e){
			log_logger.error("["+ip+"] ["+url+"]["+p_service+"]["+ntype+"]["+category+"]["+access_key+"]["+cp_id+"]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			throw e;
		}catch(Exception e){
			System.out.println("??");
			log_logger.error("["+ip+"] ["+url+"]["+p_service+"]["+ntype+"]["+category+"]["+access_key+"]["+cp_id+"]["+e.getClass().getName()+"]["+e.getMessage()+"]");
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
		log_logger.info("["+ip+"] ["+url+"]["+method+"][END] ["+(timeStr)+" sec] - ["+p_service+"]["+ntype+"]["+category+"]["+access_key+"]["+cp_id+"] ["+result.getFlag()+"]");
		//############################[//LOG END]##########################
		
		return result;	
		
	}
	
	private void API_getNoticeInfoParamValidation(String service) throws SmartUXException {
		SmartUXException exception = new SmartUXException();
		
		// 가입 번호 파라미터 존재 유무
		if(!(StringUtils.hasText(service))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "service"));
			throw exception;
		}
	}
	
	private void OpenAPI_getNoticeInfoParamValidation(String access_key,String cp_id,String service) throws SmartUXException {
		SmartUXException exception = new SmartUXException();
		
		// 인증키 파라미터 존재 유무
		if(!(StringUtils.hasText(access_key))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "access_key"));
			throw exception;
		}
		
		// CP_ID 파라미터 존재 유무
		if(!(StringUtils.hasText(cp_id))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "cp_id"));
			throw exception;
		}
		
		// 가입 번호 파라미터 존재 유무
		if(!(StringUtils.hasText(service))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "service"));
			throw exception;
		}
	}	

}
