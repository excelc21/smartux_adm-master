package com.dmi.smartux.quality.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dmi.smartux.common.exception.SmartUXException;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.CLog;
import com.dmi.smartux.common.util.CharacterSet;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.vo.Result;
import com.dmi.smartux.quality.service.QualityService;

@Controller
public class QualityController {
	//퀄리티 맵에 저장하는 로직/불러와서 인터페이스에 추가/관리자에서 캐싱 요청 연동 작업 진행 해야 함./log4j 추가

	@Autowired
	QualityService service;
	
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
	 * 품질 대상 단말 캐싱 캐싱
	 * @param request
	 * @param response
	 * @param callByScheduler
	 * @return
	 */
	@RequestMapping(value="/quality/cache", method=RequestMethod.GET)
	public Result<String> refreshCacheOfNotiPop(
		HttpServletRequest request
		, HttpServletResponse response
		, @RequestParam(value="callByScheduler", required=false, defaultValue="N") String callByScheduler
	) {
		//#########[LOG START]#########
		Log log_logger = LogFactory.getLog("refreshCacheOfQualityMember");
		CLog cLog = new CLog(log_logger, request);
		cLog.startLog( "["+GlobalCom.isNull(callByScheduler)+"]" );

		Result<String> result = new Result<String>();
		result.setFlag(SmartUXProperties.getProperty("flag.success"));
		result.setMessage(SmartUXProperties.getProperty("message.success"));
		result.setRecordset(null);
		try {
			service.refreshCacheOfQualityMember(callByScheduler);
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

}
