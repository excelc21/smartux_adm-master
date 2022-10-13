package com.dmi.smartux.notipop.controller;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dmi.smartux.common.exception.SmartUXException;
import com.dmi.smartux.common.module.StatPushLogger;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.CLog;
import com.dmi.smartux.common.util.CharacterSet;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.vo.Result;
import com.dmi.smartux.common.vo.StatPushLogVo;
import com.dmi.smartux.notipop.service.NotiPopService;
import com.dmi.smartux.notipop.vo.getNotiPopCacheVo;

@Controller
public class NotiPopController {

	@Autowired
	NotiPopService service;
	
	/*
	 * handleException 			Error/Exception Handling
	 * @param ex				SmartUXException
	 * @return					errorVO
	 * @throws Exception
	 */
	@ExceptionHandler(SmartUXException.class)
	public @ResponseBody String handleException(SmartUXException e, HttpServletRequest request,HttpServletResponse response) throws SmartUXException {

		if(request.getHeader("accept") == null || "*/*".equals(request.getHeader("accept"))){
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
	 * 긴급(비상)/팝업 공지 캐싱
	 * @param request
	 * @param response
	 * @param callByScheduler
	 * @return
	 */
	@RequestMapping(value="/notipop/cache", method=RequestMethod.GET)
	public Result<String> refreshCacheOfNotiPop(
		HttpServletRequest request
		, HttpServletResponse response
		, @RequestParam(value="callByScheduler", required=false, defaultValue="N") String callByScheduler
	) {
		//#########[LOG START]#########
		Log log_logger = LogFactory.getLog("refreshCacheOfNotiPop");
		CLog cLog = new CLog(log_logger, request);
		cLog.startLog( "["+GlobalCom.isNull(callByScheduler)+"]" );

		Result<String> result = new Result<String>();
		result.setFlag(SmartUXProperties.getProperty("flag.success"));
		result.setMessage(SmartUXProperties.getProperty("message.success"));
		result.setRecordset(null);
		try {
			service.refreshCacheOfNotiPop(callByScheduler);
		} catch(Exception e) {
			cLog.errorLog("["+GlobalCom.isNull(callByScheduler)+"]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			com.dmi.smartux.common.exception.ExceptionHandler handler = new com.dmi.smartux.common.exception.ExceptionHandler(e);
			result.setFlag(handler.getFlag());
			result.setMessage(handler.getMessage());
		}

		//#########[LOG END]#########
		cLog.endLog( "["+GlobalCom.isNull(callByScheduler)+"] ["+result.getFlag()+"]");

		return result;
	}

    @RequestMapping(value="/comm/startup/noti", method=RequestMethod.GET)
    public getNotiPopCacheVo getStartupNoti (
            @RequestParam(value="sa_id", required=false, defaultValue="") String sa_id,
            @RequestParam(value="stb_mac", required=false, defaultValue="") String stb_mac,
            @RequestParam(value="version", required=false, defaultValue="") String version,
            @RequestParam(value="app_gbn", required=false) String app_gbn,
            @RequestParam(value="stat_type", required=false) String stat_type,
            @RequestParam(value="stat_val", required=false) String stat_val,
            @RequestParam(value="dev_info", required=false) String dev_info,
            @RequestParam(value="nw_info", required=false) String nw_info,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		
		//#########[LOG SET]#########
		Log log_logger = LogFactory.getLog("startupnoti");
		CLog cLog = new CLog(log_logger, request);
		
		//SmartUXException exception = new SmartUXException();
		getNotiPopCacheVo result = new getNotiPopCacheVo();

		try {
			sa_id = GlobalCom.isNull(sa_id);
			stb_mac = GlobalCom.isNull(stb_mac);
			version = GlobalCom.isNull(version);
			
			//#########[LOG START]#########
			cLog.startLog( "["+request.getMethod()+"]-["+sa_id+"]["+stb_mac+"]["+version+"]["+app_gbn+"]["+stat_type+"]["+stat_val+"]["+dev_info+"]["+nw_info+"]" );
			
			//유효성 검사
			validateStartupNoti(app_gbn);
			
			try {
				result = service.getStartupNoti(version, app_gbn, sa_id);
			} catch(Exception ex) {
				result = null;
			}
			
			result.setFlag(SmartUXProperties.getProperty("flag.success"));
			result.setMessage(SmartUXProperties.getProperty("message.success"));

			/* ***************************************
			 * stat_type: 통계타입(PUSH 팝업 클릭시 사용)
			 * ***************************************
			 * 1) LAT:최신회차
			 * 2) NEW:새소식
			 * *************************************** */
			if (stat_type!=null && !stat_type.equals("")) {
				// 푸시통계로그 남기기
				writeStatPushLogger(sa_id, stat_type, stat_val, dev_info, nw_info);
			}
			
		}catch (SmartUXException e){
			cLog.endLog( "["+request.getMethod()+"]-["+sa_id+"]["+stb_mac+"]["+version+"]["+app_gbn+"]" );
			throw e;		
		}catch(Exception e){
			cLog.errorLog( "["+request.getMethod()+"]-["+sa_id+"]["+stb_mac+"]["+version+"]["+app_gbn+"]["+e.getClass().getName()+"]["+e.getMessage()+"]" );
			com.dmi.smartux.common.exception.ExceptionHandler handler = new com.dmi.smartux.common.exception.ExceptionHandler(e);
			result.setFlag(handler.getFlag());
			result.setMessage(handler.getMessage());
		}

		//#########[LOG END]#########
		cLog.endLog( "["+request.getMethod()+"]-["+sa_id+"]["+stb_mac+"]["+version+"]["+app_gbn+"]["+stat_type+"]["+stat_val+"]["+dev_info+"]["+nw_info+"]["+result.getFlag()+"]" );
		
		return result;
    }

    @RequestMapping(value="/v1/startup/noti", method=RequestMethod.GET)
    public getNotiPopCacheVo getStartupNoti_OpenApi(
			@RequestParam(value="access_key", required=false) String access_key,
			@RequestParam(value="cp_id", required=false) String cp_id,
            @RequestParam(value="sa_id", required=false, defaultValue="") String sa_id,
            @RequestParam(value="stb_mac", required=false, defaultValue="") String stb_mac,
            @RequestParam(value="version", required=false, defaultValue="") String version,
            @RequestParam(value="app_gbn", required=false) String app_gbn,
            @RequestParam(value="stat_type", required=false) String stat_type,
            @RequestParam(value="stat_val", required=false) String stat_val,
            @RequestParam(value="dev_info", required=false) String dev_info,
            @RequestParam(value="nw_info", required=false) String nw_info,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		
		//#########[LOG SET]#########
		Log log_logger = LogFactory.getLog("startupnoti");
		CLog cLog = new CLog(log_logger, request);
		
		//SmartUXException exception = new SmartUXException();
		getNotiPopCacheVo result = new getNotiPopCacheVo();
		
		try {
			sa_id 	= GlobalCom.isNull(sa_id);
			stb_mac = GlobalCom.isNull(stb_mac);
			version = GlobalCom.isNull(version);
			
			//#########[LOG START]#########
			cLog.startLog( "["+request.getMethod()+"]-["+sa_id+"]["+stb_mac+"]["+version+"]["+app_gbn+"]["+stat_type+"]["+stat_val+"]["+dev_info+"]["+nw_info+"]" );
			
			// 유효성 검사
			validateStartupNoti(app_gbn);
			
			try {
				// 캐시리스트 가져오기
				result = service.getStartupNoti(version, app_gbn, sa_id);
			} catch(Exception ex) {
				result = new getNotiPopCacheVo();
			}
			
			result.setFlag(SmartUXProperties.getProperty("flag.success"));
			result.setMessage(SmartUXProperties.getProperty("message.success"));
			

			/* ***************************************
			 * stat_type: 통계타입(PUSH 팝업 클릭시 사용)
			 * ***************************************
			 * 1) LAT:최신회차
			 * 2) NEW:새소식
			 * *************************************** */
			if (stat_type!=null && !stat_type.equals("")) {
				// 푸시통계로그 남기기
				writeStatPushLogger(sa_id, stat_type, stat_val, dev_info, nw_info);
			}
			
		}catch (SmartUXException e){
			cLog.endLog( "["+request.getMethod()+"]-["+sa_id+"]["+stb_mac+"]["+version+"]["+app_gbn+"]["+stat_type+"]["+stat_val+"]["+dev_info+"]["+nw_info+"]" );
			throw e;		
		}catch(Exception e){
			cLog.errorLog( "["+request.getMethod()+"]-["+sa_id+"]["+stb_mac+"]["+version+"]["+app_gbn+"]["+e.getClass().getName()+"]["+e.getMessage()+"]" );
			com.dmi.smartux.common.exception.ExceptionHandler handler = new com.dmi.smartux.common.exception.ExceptionHandler(e);
			result.setFlag(handler.getFlag());
			result.setMessage(handler.getMessage());
		}

		//#########[LOG END]#########
		cLog.endLog( "["+request.getMethod()+"]-["+sa_id+"]["+stb_mac+"]["+version+"]["+app_gbn+"]["+result.getFlag()+"]" );
		
		return result;
    }

	private void validateStartupNoti(String app_gbn) {
		SmartUXException exception = new SmartUXException();

		if(!(StringUtils.hasText(app_gbn))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "app_gbn"));
			throw exception;
		}
		
	}
	
	/**
	 * @Method Name : writeStatPushLogger
	 * @Comment : 푸시통계로그 남기기 
	 */
	private void writeStatPushLogger(String sa_id, String stat_type, String stat_val, String dev_info, String nw_info) {
		
		String clientIp = "120.0.0.1";					// Default:120.0.0.1
		dev_info  = ( "".equals(GlobalCom.isNull(dev_info)) ) ? "PHONE" : GlobalCom.isNull(dev_info); 
		
		if ("LAT".equalsIgnoreCase(stat_type)) {
			stat_type = "L";
		} else if ("NEW".equalsIgnoreCase(stat_type)) {
			stat_type = "N";
		}
		
		// 1. 푸시 기본정보 세팅
		StatPushLogger pushLogger = new StatPushLogger();
		StatPushLogVo pushVo = new StatPushLogVo();
		
		pushVo.setClient_ip(clientIp);					// ClientIp 값 세팅
		pushVo.setSid(GlobalCom.isNull(sa_id));			// 가입자번호
		pushVo.setDev_info(dev_info);					// 접속단말타입(PHONE,SERVE)
		pushVo.setNw_info(GlobalCom.isNull(nw_info));	// 접속네트워크(3G,4G,5G,WIFI,WIRE,ETC)
		pushVo.setResult_code(SmartUXProperties.getProperty("flag.success"));	// 푸시전송결과('0000' 성공)
		
		// 2. 푸시 추가정보 세팅
		LinkedHashMap<String,String> addInfo = new LinkedHashMap<String,String>();
		
		addInfo.put("PUSH_STYPE", stat_type);
		addInfo.put("PUSH_ID", stat_val);
		addInfo.put("PUSH_LTYPE", "C");
		addInfo.put("PUSH_SENDCNT", "");
		addInfo.put("PUSH_NAME", "");
		addInfo.put("PUSH_TYPE1", "");
		addInfo.put("PUSH_TYPE2", "");
		addInfo.put("PUSH_REGDATE", "");
		addInfo.put("SVC_SUB_NAME", "01");
		
		pushVo.setAddInfo(addInfo);
		
		// 3. 푸시 로그 생성
		pushLogger.writePushLog(pushVo);
	}

}