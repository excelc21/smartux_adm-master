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
import com.dmi.smartux.mainpanel.vo.AlbumInfoVO;
import com.dmi.smartux.smartstart.vo.GenreVodBestListVO;
import com.dmi.smartux.smartstart.service.GenreVodBestListService;

@Controller
public class GenreVodBestListController {

	private final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	GenreVodBestListService service;
	
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
	 * GenreVodBest ??? ???????????? ?????? ???????????? Controller ?????????
	 * @param sa_id			????????????
	 * @param stb_mac		?????????
	 * @param app_type		????????????
	 * @param start_num		?????? ?????? ?????????(-1?????? req_count ?????? ???????????? ????????? ????????????)
	 * @param req_count		?????? ????????? ??????
	 * @param genre_code	????????????
	 * @param callByScheduler	?????????????????? ???????????? ?????? ?????? ?????? Y??? ????????????. ?????????????????? ????????? ?????? validation ????????? ?????? ?????? ?????? ???????????? ????????????
	 * @return				???????????? ???????????? ?????? ???????????? ????????? GenreVodBest ????????? ???????????? List ????????? ????????? ?????? Result ??????
	 * @throws Exception
	 */
	@RequestMapping(value="/getGenreVodBestList")
	public Result<GenreVodBestListVO> API_getGenreVodBestList(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value="sa_id", 	 required=false) String sa_id,
			@RequestParam(value="stb_mac", 	 required=false) String stb_mac,
			@RequestParam(value="app_type",  required=false) String app_type,
			@RequestParam(value="start_num", required=false) String start_num,
			@RequestParam(value="req_count", required=false) String req_count,
			@RequestParam(value="genre_code",required=false) String genre_code,
			@RequestParam(value="net_typ",required=false, defaultValue="2") String net_typ,
			@RequestParam(value="callByScheduler", required=false, defaultValue="N") String callByScheduler,
			@RequestParam(value="category_gb", required=false, defaultValue="I20") String category_gb
			) throws Exception{		 
		
		//#########[LOG START]#########
		Log log_logger = LogFactory.getLog("getGenreVodBestList");
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
		logger.debug("genre_code :"	+ genre_code);
		logger.debug("callByScheduler :"	+ callByScheduler);
		logger.debug("method : " 	+ request.getMethod());
		logger.debug("category_gb : " 	+ category_gb);
		
		Result<GenreVodBestListVO> result = new Result<GenreVodBestListVO>();
		result.setRecordset(null);
		
		try {
			
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
			
			if("N".equals(callByScheduler)) {		// ???????????? ????????? ??????
				getGenreVodBestListParamValidation(sa_id, stb_mac, app_type, start_num, req_count, genre_code, net_typ);								
				result.setFlag(SmartUXProperties.getProperty("flag.success"));
				result.setMessage(SmartUXProperties.getProperty("message.success"));
				
				// ??????????????? ????????? ?????? ??????, ?????? ????????? ????????? ?????? ???????????? ????????????
				int intStartnum = Integer.parseInt(start_num);
	
				if((intStartnum > 0) || (intStartnum == -1)){					// ?????? ?????? ???????????? -1 ?????? 1 ??????????????? ?????????
					List<GenreVodBestListVO> list = service.getGenreVodBestList(sa_id, stb_mac, app_type, start_num, req_count, genre_code, fh_gbn, callByScheduler, category_gb); 
					result.setRecordset(list);
					// ????????? ??? ?????? ??????
					List<GenreVodBestListVO> listCount = service.getGenreVodBestList(sa_id, stb_mac, app_type, "-1", req_count, genre_code, fh_gbn, callByScheduler, category_gb);
					result.setTotal_count(listCount.size());
				}
				if(intStartnum == 0){					// ?????? ?????? ???????????? -1 ?????? 1 ??????????????? ?????????
					// ????????? ??? ?????? ??????
					List<GenreVodBestListVO> listCount = service.getGenreVodBestList(sa_id, stb_mac, app_type, "-1", req_count, genre_code, fh_gbn, callByScheduler, category_gb);
					result.setTotal_count(listCount.size());
				}
			}else if("A".equals(callByScheduler)){				
				// (getGenreVodBestList - item.getCategory_id) ??? ???????????? ?????????????????? ???????????? genre_code??? ???????????? ??????.
				List<GenreVodBestListVO> list = service.getGenreVodBestList("", "", "", "", "", "", fh_gbn, callByScheduler, "");
				result.setFlag(SmartUXProperties.getProperty("flag.success"));
				result.setMessage(SmartUXProperties.getProperty("message.success"));
				result.setRecordset(list);
				//result.setTotal_count(list.size());
				
				//response.setStatus(201);
				
			}else{									// ??????????????? ????????? ??????(?????? ????????? ???????????? ????????? ??????(?????? ?????? ????????? ??????)
				// (getGenreVodBestList - item.getCategory_id) ??? ???????????? ?????????????????? ???????????? genre_code??? ???????????? ??????.
				List<GenreVodBestListVO> list = service.getGenreVodBestList("", "", "", "", "", "", fh_gbn, callByScheduler, "");
				result.setFlag(SmartUXProperties.getProperty("flag.success"));
				result.setMessage(SmartUXProperties.getProperty("message.success"));
				result.setRecordset(list);
				//result.setTotal_count(list.size());
				//response.setStatus(201);
			}
		
		}catch(Exception e){
			log_logger.error("["+log_ip+"] ["+log_url+"]["+log_info_mac+"]["+log_info_id+"]["+log_info_app_type+"]"+"[getGenreVodBestList]["+e.getClass().getName()+"]["+e.getMessage()+"]");
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
	
//	//@RequestMapping(value="/BestVOD/sa_id/{sa_id}/stb_mac/{stb_mac}/app_type/{app_type}/start_num/{start_num}/req_count/{req_count}/genre_code/{genre_code}", method=RequestMethod.GET)
//	//@RequestMapping(value="/BestVOD/**", method=RequestMethod.GET)
//	@RequestMapping(value="/v1/bestvod/**", method=RequestMethod.GET)
//	public Result<GenreVodBestListVO> Restful_getGenreVodBestList(
//			HttpServletRequest request,
//			HttpServletResponse response
////			@PathVariable String sa_id,
////			@PathVariable String stb_mac,
////			@PathVariable String app_type,
////			@PathVariable String start_num,
////			@PathVariable String req_count,
////			@PathVariable String genre_code
//			
//		) throws Exception{		 
//		
//		//#########################[LOG SET]##########################
//		Log log_logger = LogFactory.getLog("OPENAPI_BestVOD");
//		long startTime = System.currentTimeMillis();
//		String ip = request.getRemoteAddr();
//		String url = request.getRequestURI();
//		try{
//			url = "/v1/bestvod/";
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
//		String genre_code = "";
//		
//		Result<GenreVodBestListVO> result = new Result<GenreVodBestListVO>();
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
//			genre_code	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "genre_code"));
//			
//			access_key = GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "access_key"));
//			cp_id = GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "cp_id"));
//		
//			//#########################[LOG START]##########################
//			log_logger.info("["+ip+"] ["+url+"]["+method+"][START] - ["+stb_mac+"]["+sa_id+"]["+app_type+"]["+access_key+"]["+cp_id+"]");
//			//#########################[//LOG START]########################
//	
//			//access_key ?????? ?????? ??????
//			String authResult = GlobalCom.isNull(AuthenticationCommon.isAuthenticationMap(access_key,cp_id,"GET","/v1/bestvod"));
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
//			//??????????????? OPEN_API ??????
//			String callByScheduler ="N";
//			
//			logger.debug("sa_id : " 	+ sa_id);
//			logger.debug("stb_mac : " 	+ stb_mac);
//			logger.debug("app_type :" 	+ app_type);
//			logger.debug("start_num : " + start_num);
//			logger.debug("req_count :" 	+ req_count);
//			logger.debug("genre_code :"	+ genre_code);
//			logger.debug("callByScheduler :"	+ callByScheduler);
//		
//		
//		
//			if("N".equals(callByScheduler)) {		// ???????????? ????????? ??????
//				getGenreVodBestListParamValidation(sa_id, stb_mac, app_type, start_num, req_count, genre_code);								
//				result.setFlag(SmartUXProperties.getProperty("flag.success"));
//				result.setMessage(SmartUXProperties.getProperty("message.success"));
//				
//				// ??????????????? ????????? ?????? ??????, ?????? ????????? ????????? ?????? ???????????? ????????????
//				int intStartnum = Integer.parseInt(start_num);
//	
//				if((intStartnum > 0) || (intStartnum == -1)){					// ?????? ?????? ???????????? -1 ?????? 1 ??????????????? ?????????
//					List<GenreVodBestListVO> list = service.getGenreVodBestList(sa_id, stb_mac, app_type, start_num, req_count, genre_code, callByScheduler); 
//					result.setRecordset(list);
//					// ????????? ??? ?????? ??????
//					List<GenreVodBestListVO> listCount = service.getGenreVodBestList(sa_id, stb_mac, app_type, "-1", req_count, genre_code, callByScheduler);
//					result.setTotal_count(listCount.size());
//				}
//				if(intStartnum == 0){					// ?????? ?????? ???????????? -1 ?????? 1 ??????????????? ?????????
//					// ????????? ??? ?????? ??????
//					List<GenreVodBestListVO> listCount = service.getGenreVodBestList(sa_id, stb_mac, app_type, "-1", req_count, genre_code, callByScheduler);
//					result.setTotal_count(listCount.size());
//				}
//			}else if("A".equals(callByScheduler)){				
//				// (getGenreVodBestList - item.getCategory_id) ??? ???????????? ?????????????????? ???????????? genre_code??? ???????????? ??????.
//				List<GenreVodBestListVO> list = service.getGenreVodBestList("", "", "", "", "", "", callByScheduler);
//				result.setFlag(SmartUXProperties.getProperty("flag.success"));
//				result.setMessage(SmartUXProperties.getProperty("message.success"));
//				result.setRecordset(list);
//				//result.setTotal_count(list.size());
//				
//				//response.setStatus(201);
//				
//			}else{									// ??????????????? ????????? ??????(?????? ????????? ???????????? ????????? ??????(?????? ?????? ????????? ??????)
//				// (getGenreVodBestList - item.getCategory_id) ??? ???????????? ?????????????????? ???????????? genre_code??? ???????????? ??????.
//				List<GenreVodBestListVO> list = service.getGenreVodBestList("", "", "", "", "", "", callByScheduler);
//				result.setFlag(SmartUXProperties.getProperty("flag.success"));
//				result.setMessage(SmartUXProperties.getProperty("message.success"));
//				result.setRecordset(list);
//				//result.setTotal_count(list.size());
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
//		
//
//		return result;
//	}
	
	
	@RequestMapping(value="/v1/bestvod", method=RequestMethod.GET)
	public Result<GenreVodBestListVO> Restful_getGenreVodBestList(
			HttpServletRequest request
			,HttpServletResponse response
			,@RequestParam(value="sa_id", 	 required=false) String sa_id
			,@RequestParam(value="stb_mac", 	 required=false) String stb_mac
			,@RequestParam(value="app_type",  required=false) String app_type
			,@RequestParam(value="start_num", required=false) String start_num
			,@RequestParam(value="req_count", required=false) String req_count
			,@RequestParam(value="genre_code",required=false) String genre_code
			,@RequestParam(value="access_key", required=false, defaultValue="") String access_key
			,@RequestParam(value="cp_id", required=false, defaultValue="") String cp_id
			,@RequestParam(value="net_typ",required=false, defaultValue="2") String net_typ
			,@RequestParam(value="category_gb", required=false, defaultValue="I20") String category_gb
		) throws Exception{		 
		
		SmartUXException exception = new SmartUXException();
		
		//#########################[LOG SET]##########################
		Log log_logger = LogFactory.getLog("OPENAPI_BestVOD");
		long startTime = System.currentTimeMillis();
		String ip = request.getRemoteAddr();
		String url = request.getRequestURI();
		try{
			url = "/v1/bestvod/";
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
//		String genre_code = "";
		
		Result<GenreVodBestListVO> result = new Result<GenreVodBestListVO>();
		result.setRecordset(null);
		
		String uri = request.getRequestURI();
		try {
//			HashMap<String, String> paramMap = GlobalCom.getRestfulParamMap(4,uri);
//			sa_id 		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "sa_id"));
//			stb_mac		= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "stb_mac"));
//			app_type 	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "app_type"));
//			start_num 	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "start_num"));
//			req_count	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "req_count"));
//			genre_code	= GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "genre_code"));
//			
//			access_key = GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "access_key"));
//			cp_id = GlobalCom.isNull(GlobalCom.getRestfulParam(paramMap, "cp_id"));
		
			//#########################[LOG START]##########################
			log_logger.info("["+ip+"] ["+url+"]["+method+"][START] - ["+stb_mac+"]["+sa_id+"]["+app_type+"]["+access_key+"]["+cp_id+"]");
			//#########################[//LOG START]########################
	
			//access_key ?????? ?????? ??????
			String authResult = GlobalCom.isNull(AuthenticationCommon.isAuthenticationMap(access_key,cp_id,"GET","/v1/bestvod"));
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
			
			logger.debug("sa_id : " 	+ sa_id);
			logger.debug("stb_mac : " 	+ stb_mac);
			logger.debug("app_type :" 	+ app_type);
			logger.debug("start_num : " + start_num);
			logger.debug("req_count :" 	+ req_count);
			logger.debug("genre_code :"	+ genre_code);
			logger.debug("callByScheduler :"	+ callByScheduler);
			logger.debug("category_gb :"	+ category_gb);
		
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
		
			if("N".equals(callByScheduler)) {		// ???????????? ????????? ??????
				getGenreVodBestListParamValidation(sa_id, stb_mac, app_type, start_num, req_count, genre_code, net_typ);								
				result.setFlag(SmartUXProperties.getProperty("flag.success"));
				result.setMessage(SmartUXProperties.getProperty("message.success"));
				
				// ??????????????? ????????? ?????? ??????, ?????? ????????? ????????? ?????? ???????????? ????????????
				int intStartnum = Integer.parseInt(start_num);
	
				if((intStartnum > 0) || (intStartnum == -1)){					// ?????? ?????? ???????????? -1 ?????? 1 ??????????????? ?????????
					List<GenreVodBestListVO> list = service.getGenreVodBestList(sa_id, stb_mac, app_type, start_num, req_count, genre_code,fh_gbn, callByScheduler, category_gb); 
					result.setRecordset(list);
					// ????????? ??? ?????? ??????
					List<GenreVodBestListVO> listCount = service.getGenreVodBestList(sa_id, stb_mac, app_type, "-1", req_count, genre_code,fh_gbn, callByScheduler, category_gb);
					result.setTotal_count(listCount.size());
				}
				if(intStartnum == 0){					// ?????? ?????? ???????????? -1 ?????? 1 ??????????????? ?????????
					// ????????? ??? ?????? ??????
					List<GenreVodBestListVO> listCount = service.getGenreVodBestList(sa_id, stb_mac, app_type, "-1", req_count, genre_code,fh_gbn, callByScheduler, category_gb);
					result.setTotal_count(listCount.size());
				}
			}else if("A".equals(callByScheduler)){				
				// (getGenreVodBestList - item.getCategory_id) ??? ???????????? ?????????????????? ???????????? genre_code??? ???????????? ??????.
				List<GenreVodBestListVO> list = service.getGenreVodBestList("", "", "", "", "", "",fh_gbn, callByScheduler, "");
				result.setFlag(SmartUXProperties.getProperty("flag.success"));
				result.setMessage(SmartUXProperties.getProperty("message.success"));
				result.setRecordset(list);
				//result.setTotal_count(list.size());
				
				//response.setStatus(201);
				
			}else{									// ??????????????? ????????? ??????(?????? ????????? ???????????? ????????? ??????(?????? ?????? ????????? ??????)
				// (getGenreVodBestList - item.getCategory_id) ??? ???????????? ?????????????????? ???????????? genre_code??? ???????????? ??????.
				List<GenreVodBestListVO> list = service.getGenreVodBestList("", "", "", "", "", "",fh_gbn, callByScheduler, "");
				result.setFlag(SmartUXProperties.getProperty("flag.success"));
				result.setMessage(SmartUXProperties.getProperty("message.success"));
				result.setRecordset(list);
				//result.setTotal_count(list.size());
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
	 * getGenreVodBestList ??????????????? ???????????? validation ?????????
	 * @param sa_id			????????????
	 * @param stb_mac		?????????
	 * @param app_type		????????????
	 * @param start_num		?????? ?????? ?????????(-1?????? req_count ?????? ???????????? ????????? ???????????? 0?????? ????????? ????????? ???????????? ????????????)
	 * @param req_count		?????? ????????? ??????
	 * @param genre_code	????????????
	 * @throws SmartUXException validation ??? ????????? ???????????? ?????? ????????? ???????????? ???????????? ???????????? SmartUXException ??????
	 */

	private void getGenreVodBestListParamValidation(String sa_id, String stb_mac, String app_type, String start_num, String req_count, String genre_code, String net_typ ) throws SmartUXException {
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
//			130703 ???????????? ??????
//			else{
//				// ?????? ?????? ???????????? ?????? SETTOP?????? ??????
//				if(!("UX".equals(app_type)) && !("SMA".equals(app_type))){
//					exception.setFlag(SmartUXProperties.getProperty("flag.mismatchvalue"));
//					exception.setMessage(SmartUXProperties.getProperty("message.mismatchvalue", "app_type", "UX ????????? SMA"));
//					throw exception;
//				}
//			}
			
			// ???????????? ???????????? ?????? ??????
			if(!(StringUtils.hasText(genre_code))){
				exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
				exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "genre_code"));
				throw exception;
			}
			
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
							exception.setMessage(SmartUXProperties.getProperty("message.numberformat", "req_count)"));
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
					exception.setMessage(SmartUXProperties.getProperty("message.range2", "startnum", "-1"));
					throw exception;
				}
			}else{	// ?????? ?????? ???????????? ?????? ??????
				exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
				exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "start_num"));
				throw exception;
			}
	}		

}