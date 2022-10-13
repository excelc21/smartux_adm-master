package com.dmi.smartux.smartstart.controller;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dmi.smartux.common.exception.ExceptionHandler;
import com.dmi.smartux.common.exception.SmartUXException;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.CharacterSet;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.vo.Result;
import com.dmi.smartux.admin.rank.vo.EcrmRankVO;
import com.dmi.smartux.authentication.vo.AuthenticationCommon;
import com.dmi.smartux.smartstart.vo.SUXMAlbumListVO;
import com.dmi.smartux.smartstart.service.SUXMAlbumListService;

@Controller
public class SUXMAlbumListController {

	private final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	SUXMAlbumListService service;
    
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
	 * SUXMAlbumListController 를 조회하기 위해 사용되는 Controller 클래스
	 * @param sa_id			가입번호
	 * @param stb_mac		맥주소
	 * @param app_type		어플타입
	 * @param start_num		검색 시작 인덱스(-1이면 req_count 값을 무시하고 전체를 검색한다)
	 * @param req_count		검색 레코드 갯수
	 * @param schedule_code	자체편성코드
	 * @param callByScheduler	스케듈러에서 호출했을 경우 여기 값에 Y가 들어온다. 스케줄러에서 호출한 경우 validation 로직을 타지 않고 바로 서비스를 호출한다
	 * @return				성공여부 코드값과 결과 메시지와 검색된 GenreVodBest 정보가 들어있는 List 객체가 들어가 있는 Result 객체
	 * @throws Exception
	 */
	@RequestMapping(value="/getSUXMAlbumList")
	public Result<SUXMAlbumListVO> API_getSUXMAlbumList(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value="sa_id", 	 required=false) String sa_id,
			@RequestParam(value="stb_mac", 	 required=false) String stb_mac,
			@RequestParam(value="app_type",  required=false) String app_type,
			@RequestParam(value="start_num", required=false) String start_num,
			@RequestParam(value="req_count", required=false) String req_count,
			@RequestParam(value="schedule_code",required=false) String schedule_code,
			@RequestParam(value="net_typ",required=false, defaultValue="2") String net_typ,
			@RequestParam(value="callByScheduler", required=false, defaultValue="N") String callByScheduler
			) throws Exception{	
		
		//#########[LOG START]#########
		Log log_logger = LogFactory.getLog("getSUXMAlbumList");
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
		logger.debug("start_num : " + start_num);
		logger.debug("req_count :" 	+ req_count);
		logger.debug("schedule_code :"	+ schedule_code);
		logger.debug("callByScheduler :"	+ callByScheduler);
		logger.debug("method : " 	+ request.getMethod());
		
		Result<SUXMAlbumListVO> result = new Result<SUXMAlbumListVO>();
		result.setRecordset(null);
		
		try {
			
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
			
			if("N".equals(callByScheduler)) {		// 단말에서 호출한 경우
				getSUXMAlbumListParamValidation(sa_id, stb_mac, app_type, start_num, req_count, schedule_code, net_typ);								
				result.setFlag(SmartUXProperties.getProperty("flag.success"));
				result.setMessage(SmartUXProperties.getProperty("message.success"));
				
				List<EcrmRankVO> ScheduleCodelist = service.getCheckScheduleCodelist(schedule_code);

				if (ScheduleCodelist.size() == 0) {
					// 검색된 총 갯수 셋팅
//					int totalCount = 0;
//					result.setTotal_count(totalCount);
					//맵 생성 안했다면 에러로 변경!!130906
					SmartUXException ex = new SmartUXException();
					ex.setFlag(SmartUXProperties.getProperty("flag.beNotData"));
					ex.setMessage(SmartUXProperties.getProperty("message.beNotData"));
					throw ex;
				}else{
					// 검색된 총 갯수 셋팅
					int totalCount = service.getSUXMAlbumListCount(sa_id, stb_mac, app_type, schedule_code, callByScheduler);
					result.setTotal_count(totalCount);
					
					// 시작번호를 가지고 전체 조회, 또는 페이징 조회를 해야 하는지의 판단여부
					int intStartnum = Integer.parseInt(start_num);
		
					if((intStartnum > 0) || (intStartnum == -1)){					// 검색 시작 인덱스가 -1 또는 1 이상일때만 결과셋
						List<SUXMAlbumListVO> list = service.getSUXMAlbumList(sa_id, stb_mac, app_type, start_num, req_count, schedule_code, fh_gbn, callByScheduler); 
						result.setRecordset(list);
					}
				}		
			}else if("A".equals(callByScheduler)){
				List<EcrmRankVO> checklist = service.getCheckSCHEDULEList();
				logger.debug("checklist.size(); :"	+ checklist.size());
				
				// (getSUXMAlbumList - item.getCategory_id) 가 캐시키를 구성하게되어 단말에서 schedule_code에 대응하게 된다.
				for(EcrmRankVO item : checklist){
					logger.debug("getCategory_id : " 	+ item.getCategory_id());	
					
					List<SUXMAlbumListVO> list = service.getSUXMAlbumList("", "", "", "", "", item.getCategory_id(), fh_gbn, callByScheduler);
					result.setFlag(SmartUXProperties.getProperty("flag.success"));
					result.setMessage(SmartUXProperties.getProperty("message.success"));
					result.setRecordset(list);
				}
				
				//response.setStatus(201);
			}else{									// 스케듈러가 호출한 경우(조회 결과는 셋팅하지 않아도 된다(작업 성공 여부만 셋팅))
				List<EcrmRankVO> checklist = service.getCheckSCHEDULEList();
				logger.debug("checklist.size(); :"	+ checklist.size());
				
				// (getSUXMAlbumList - item.getCategory_id) 가 캐시키를 구성하게되어 단말에서 schedule_code에 대응하게 된다.
				for(EcrmRankVO item : checklist){
					logger.debug("getCategory_id : " 	+ item.getCategory_id());	
					
					List<SUXMAlbumListVO> list = service.getSUXMAlbumList("", "", "", "", "", item.getCategory_id(), fh_gbn, callByScheduler);
					result.setFlag(SmartUXProperties.getProperty("flag.success"));
					result.setMessage(SmartUXProperties.getProperty("message.success"));
					result.setRecordset(list);
				}
				//response.setStatus(201);
			}

			
		}catch(Exception e){
			//logger.error("getSUXMAlbumList "+e.getClass().getName());
			//logger.error("getSUXMAlbumList "+e.getMessage());
			//logger.error("[getSUXMAlbumList]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			log_logger.error("["+log_ip+"] ["+log_url+"]["+log_info_mac+"]["+log_info_id+"]["+log_info_app_type+"]"+"[getSUXMAlbumList]["+e.getClass().getName()+"]["+e.getMessage()+"]");
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
	
//	//@RequestMapping(value="/SUXMAlbum/sa_id/{sa_id}/stb_mac/{stb_mac}/app_type/{app_type}/start_num/{start_num}/req_count/{req_count}/schedule_code/{schedule_code}", method=RequestMethod.GET)
//	//@RequestMapping(value="/SUXMAlbum/**", method=RequestMethod.GET)
//	@RequestMapping(value="/v1/album/**", method=RequestMethod.GET)
//	public Result<SUXMAlbumListVO> Restful_getSUXMAlbumList(
//			HttpServletRequest request,
//			HttpServletResponse response
////			@PathVariable String sa_id,
////			@PathVariable String stb_mac,
////			@PathVariable String app_type,
////			@PathVariable String start_num,
////			@PathVariable String req_count,
////			@PathVariable String schedule_code
//		) throws Exception{	
//		
//		//#########################[LOG SET]##########################
//		Log log_logger = LogFactory.getLog("OPENAPI_SUXMAlbum");
//		long startTime = System.currentTimeMillis();
//		String ip = request.getRemoteAddr();
//		String url = request.getRequestURI();
//		try{
//			url = "/v1/album/";
//		}catch(Exception e){
//		}
//		String method = request.getMethod();
//		//#########################[//LOG SET]##########################
//		String access_key = null;
//		String cp_id = null;
//		
//		String sa_id = "";
//		String stb_mac = "";
//		String app_type = "";
//		String start_num = "";
//		String req_count = "";
//		String schedule_code = "";
//		
//		Result<SUXMAlbumListVO> result = new Result<SUXMAlbumListVO>();
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
//			schedule_code	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "schedule_code"));
//				
//			access_key = GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "access_key"));
//			cp_id = GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "cp_id"));
//		
//			//#########################[LOG START]##########################
//			log_logger.info("["+ip+"] ["+url+"]["+method+"][START] - ["+stb_mac+"]["+sa_id+"]["+app_type+"]["+access_key+"]["+cp_id+"]");
//			//#########################[//LOG START]########################
//	
//			//access_key 인증 허용 체크
//			String authResult = GlobalCom.isNull(AuthenticationCommon.isAuthenticationMap(access_key,cp_id,"GET","/v1/album"));
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
//			String callByScheduler = "N";
//			
//			logger.debug("sa_id : " 	+ sa_id);
//			logger.debug("stb_mac : " 	+ stb_mac);
//			logger.debug("app_type :" 	+ app_type);
//			logger.debug("start_num : " + start_num);
//			logger.debug("req_count :" 	+ req_count);
//			logger.debug("schedule_code :"	+ schedule_code);
//			logger.debug("callByScheduler :"	+ callByScheduler);
//		
//		
//			if("N".equals(callByScheduler)) {		// 단말에서 호출한 경우
//				getSUXMAlbumListParamValidation(sa_id, stb_mac, app_type, start_num, req_count, schedule_code);								
//				result.setFlag(SmartUXProperties.getProperty("flag.success"));
//				result.setMessage(SmartUXProperties.getProperty("message.success"));
//				
//				List<EcrmRankVO> ScheduleCodelist = service.getCheckScheduleCodelist(schedule_code);
//
//				if (ScheduleCodelist.size() == 0) {
//					// 검색된 총 갯수 셋팅
//					int totalCount = 0;
//					result.setTotal_count(totalCount);	
//					
//				}else{
//					// 검색된 총 갯수 셋팅
//					int totalCount = service.getSUXMAlbumListCount(sa_id, stb_mac, app_type, schedule_code, callByScheduler);
//					result.setTotal_count(totalCount);
//					
//					// 시작번호를 가지고 전체 조회, 또는 페이징 조회를 해야 하는지의 판단여부
//					int intStartnum = Integer.parseInt(start_num);
//		
//					if((intStartnum > 0) || (intStartnum == -1)){					// 검색 시작 인덱스가 -1 또는 1 이상일때만 결과셋
//						List<SUXMAlbumListVO> list = service.getSUXMAlbumList(sa_id, stb_mac, app_type, start_num, req_count, schedule_code, callByScheduler); 
//						result.setRecordset(list);
//					}
//				}		
//			}else if("A".equals(callByScheduler)){
//				List<EcrmRankVO> checklist = service.getCheckSCHEDULEList();
//				logger.debug("checklist.size(); :"	+ checklist.size());
//				
//				// (getSUXMAlbumList - item.getCategory_id) 가 캐시키를 구성하게되어 단말에서 schedule_code에 대응하게 된다.
//				for(EcrmRankVO item : checklist){
//					logger.debug("getCategory_id : " 	+ item.getCategory_id());	
//					
//					List<SUXMAlbumListVO> list = service.getSUXMAlbumList("", "", "", "", "", item.getCategory_id(), callByScheduler);
//					result.setFlag(SmartUXProperties.getProperty("flag.success"));
//					result.setMessage(SmartUXProperties.getProperty("message.success"));
//					result.setRecordset(list);
//				}
//				
//				//response.setStatus(201);
//			}else{									// 스케듈러가 호출한 경우(조회 결과는 셋팅하지 않아도 된다(작업 성공 여부만 셋팅))
//				List<EcrmRankVO> checklist = service.getCheckSCHEDULEList();
//				logger.debug("checklist.size(); :"	+ checklist.size());
//				
//				// (getSUXMAlbumList - item.getCategory_id) 가 캐시키를 구성하게되어 단말에서 schedule_code에 대응하게 된다.
//				for(EcrmRankVO item : checklist){
//					logger.debug("getCategory_id : " 	+ item.getCategory_id());	
//					
//					List<SUXMAlbumListVO> list = service.getSUXMAlbumList("", "", "", "", "", item.getCategory_id(), callByScheduler);
//					result.setFlag(SmartUXProperties.getProperty("flag.success"));
//					result.setMessage(SmartUXProperties.getProperty("message.success"));
//					result.setRecordset(list);
//				}
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
	
	@RequestMapping(value="/v1/album", method=RequestMethod.GET)
	public Result<SUXMAlbumListVO> Restful_getSUXMAlbumList(
			HttpServletRequest request
			,HttpServletResponse response
			,@RequestParam(value="sa_id", 	 required=false) String sa_id
			,@RequestParam(value="stb_mac", 	 required=false) String stb_mac
			,@RequestParam(value="app_type",  required=false) String app_type
			,@RequestParam(value="start_num", required=false) String start_num
			,@RequestParam(value="req_count", required=false) String req_count
			,@RequestParam(value="schedule_code",required=false) String schedule_code
			,@RequestParam(value="access_key", required=false, defaultValue="") String access_key
			,@RequestParam(value="cp_id", required=false, defaultValue="") String cp_id
			,@RequestParam(value="net_typ",required=false, defaultValue="2") String net_typ
		) throws Exception{	
		
		SmartUXException exception = new SmartUXException();
		
		//#########################[LOG SET]##########################
		Log log_logger = LogFactory.getLog("OPENAPI_SUXMAlbum");
		long startTime = System.currentTimeMillis();
		String ip = request.getRemoteAddr();
		String url = request.getRequestURI();
		try{
			url = "/v1/album/";
		}catch(Exception e){
		}
		String method = request.getMethod();
		//#########################[//LOG SET]##########################
//		String access_key = null;
//		String cp_id = null;
//		
//		String sa_id = "";
//		String stb_mac = "";
//		String app_type = "";
//		String start_num = "";
//		String req_count = "";
//		String schedule_code = "";
		
		Result<SUXMAlbumListVO> result = new Result<SUXMAlbumListVO>();
		result.setRecordset(null);

		String uri = request.getRequestURI();
		try {
//			HashMap<String, String> paramMap = GlobalCom.getRestfulParamMap(4,uri);
//			sa_id 		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "sa_id"));
//			stb_mac		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "stb_mac"));
//			app_type 	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "app_type"));
//			start_num 	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "start_num"));
//			req_count	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "req_count"));
//			schedule_code	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "schedule_code"));
//				
//			access_key = GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "access_key"));
//			cp_id = GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "cp_id"));
		
			//#########################[LOG START]##########################
			log_logger.info("["+ip+"] ["+url+"]["+method+"][START] - ["+stb_mac+"]["+sa_id+"]["+app_type+"]["+access_key+"]["+cp_id+"]");
			//#########################[//LOG START]########################
	
			//access_key 인증 허용 체크
			String authResult = GlobalCom.isNull(AuthenticationCommon.isAuthenticationMap(access_key,cp_id,"GET","/v1/album"));
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
			
			logger.debug("sa_id : " 	+ sa_id);
			logger.debug("stb_mac : " 	+ stb_mac);
			logger.debug("app_type :" 	+ app_type);
			logger.debug("start_num : " + start_num);
			logger.debug("req_count :" 	+ req_count);
			logger.debug("schedule_code :"	+ schedule_code);
			logger.debug("callByScheduler :"	+ callByScheduler);
			
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
		
		
			if("N".equals(callByScheduler)) {		// 단말에서 호출한 경우
				getSUXMAlbumListParamValidation(sa_id, stb_mac, app_type, start_num, req_count, schedule_code, net_typ);								
				result.setFlag(SmartUXProperties.getProperty("flag.success"));
				result.setMessage(SmartUXProperties.getProperty("message.success"));
				
				List<EcrmRankVO> ScheduleCodelist = service.getCheckScheduleCodelist(schedule_code);

				if (ScheduleCodelist.size() == 0) {
					// 검색된 총 갯수 셋팅
					int totalCount = 0;
					result.setTotal_count(totalCount);	
					
				}else{
					// 검색된 총 갯수 셋팅
					int totalCount = service.getSUXMAlbumListCount(sa_id, stb_mac, app_type, schedule_code, callByScheduler);
					result.setTotal_count(totalCount);
					
					// 시작번호를 가지고 전체 조회, 또는 페이징 조회를 해야 하는지의 판단여부
					int intStartnum = Integer.parseInt(start_num);
		
					if((intStartnum > 0) || (intStartnum == -1)){					// 검색 시작 인덱스가 -1 또는 1 이상일때만 결과셋
						List<SUXMAlbumListVO> list = service.getSUXMAlbumList(sa_id, stb_mac, app_type, start_num, req_count, schedule_code, fh_gbn, callByScheduler); 
						result.setRecordset(list);
					}
				}		
			}else if("A".equals(callByScheduler)){
				List<EcrmRankVO> checklist = service.getCheckSCHEDULEList();
				logger.debug("checklist.size(); :"	+ checklist.size());
				
				// (getSUXMAlbumList - item.getCategory_id) 가 캐시키를 구성하게되어 단말에서 schedule_code에 대응하게 된다.
				for(EcrmRankVO item : checklist){
					logger.debug("getCategory_id : " 	+ item.getCategory_id());	
					
					List<SUXMAlbumListVO> list = service.getSUXMAlbumList("", "", "", "", "", item.getCategory_id(), fh_gbn, callByScheduler);
					result.setFlag(SmartUXProperties.getProperty("flag.success"));
					result.setMessage(SmartUXProperties.getProperty("message.success"));
					result.setRecordset(list);
				}
				
				//response.setStatus(201);
			}else{									// 스케듈러가 호출한 경우(조회 결과는 셋팅하지 않아도 된다(작업 성공 여부만 셋팅))
				List<EcrmRankVO> checklist = service.getCheckSCHEDULEList();
				logger.debug("checklist.size(); :"	+ checklist.size());
				
				// (getSUXMAlbumList - item.getCategory_id) 가 캐시키를 구성하게되어 단말에서 schedule_code에 대응하게 된다.
				for(EcrmRankVO item : checklist){
					logger.debug("getCategory_id : " 	+ item.getCategory_id());	
					
					List<SUXMAlbumListVO> list = service.getSUXMAlbumList("", "", "", "", "", item.getCategory_id(), fh_gbn, callByScheduler);
					result.setFlag(SmartUXProperties.getProperty("flag.success"));
					result.setMessage(SmartUXProperties.getProperty("message.success"));
					result.setRecordset(list);
				}
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
	 * getSUXMAlbumList 메소드에서 사용하는 validation 메소드
	 * @param sa_id			가입번호
	 * @param stb_mac		맥주소
	 * @param app_type		어플타입
	 * @param start_num		검색 시작 인덱스(-1이면 req_count 값을 무시하고 전체를 검색하고 0일땐 검색된 레코드 총갯수만 리턴한다)
	 * @param req_count		검색 레코드 갯수
	 * @param schedule_code	자체편성코드
	 * @throws SmartUXException validation 에 문제가 발생했을 경우 코드와 메시지가 셋팅되서 던져지는 SmartUXException 객체
	 */
	private void getSUXMAlbumListParamValidation(String sa_id, String stb_mac, String app_type, String start_num, String req_count, String schedule_code, String net_typ ) throws SmartUXException {
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
//			130703 타입모두 허용
//			else{
//				// 어플 타입 파라미터 값이 SETTOP인지 확인
//				if(!("UX".equals(app_type)) && !("SMA".equals(app_type))){
//					exception.setFlag(SmartUXProperties.getProperty("flag.mismatchvalue"));
//					exception.setMessage(SmartUXProperties.getProperty("message.mismatchvalue", "app_type", "UX 이거나 SMA"));
//					throw exception;
//				}
//			}
			
			// 자체편성코드 파라미터 존재 유무
			if(!(StringUtils.hasText(schedule_code))){
				exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
				exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "schedule_code"));
				throw exception;
			}
			
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
							exception.setMessage(SmartUXProperties.getProperty("message.numberformat", "req_count)"));
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
					exception.setMessage(SmartUXProperties.getProperty("message.range2", "startnum", "-1"));
					throw exception;
				}
			}else{	// 검색 시작 인덱스가 없을 경우
				exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
				exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "start_num"));
				throw exception;
			}
	}		

}



