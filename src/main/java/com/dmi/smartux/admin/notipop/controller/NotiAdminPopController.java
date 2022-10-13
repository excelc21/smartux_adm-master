package com.dmi.smartux.admin.notipop.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dmi.commons.consts.ResultCode;
import com.dmi.commons.exception.MimsCommonException;
import com.dmi.commons.factory.FileProcessFunction;
import com.dmi.commons.factory.FileProcessLockFactory;
import com.dmi.commons.logger.CLog;
import com.dmi.commons.model.RestfulCodeMsg;
import com.dmi.commons.utility.HttpClientUtils;
import com.dmi.commons.utility.RestfulUtils;
import com.dmi.smartux.admin.notipop.service.NotiAdminPopService;
import com.dmi.smartux.admin.notipop.vo.getEmergencyViewVo;
import com.dmi.smartux.admin.notipop.vo.getEmergencyVo;
import com.dmi.smartux.admin.notipop.vo.notiPopVo;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.CookieUtil;
import com.dmi.smartux.common.util.HTMLCleaner;
import com.dmi.smartux.common.vo.CUDResult;

import net.sf.json.JSONSerializer;

@Controller
public class NotiAdminPopController {
	private final Log logger = LogFactory.getLog ( this.getClass ( ) );
	
	private final Log applyLogger = LogFactory.getLog("adminApplyCacheHistroy");
	
	@Autowired
	NotiAdminPopService service;

	/**
	 * 긴급(비상) 팝업 공지 즉시적용
	 * 
	 * @param code 캐쉬를 바로 적용시킬 코드
	 * @return
	 */
	@RequestMapping(value="/admin/notipop/notiPopApplyCache", method=RequestMethod.POST )
	public ResponseEntity<String> notiPopApplyCache(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value="callByScheduler", defaultValue="Y") final String callByScheduler
			) throws Exception{
		
		final CLog cLog = new CLog(applyLogger, request.getRemoteAddr(), request.getRequestURI(), request.getMethod());
		final String loginUser = callByScheduler.equals("Y") ? "batch" : CookieUtil.getCookieUserID(request);
		final CUDResult result = new CUDResult();
		
		try {
			cLog.startLog("긴급(비상) 팝업 공지 즉시적용 : ",  callByScheduler, loginUser);
			final String host = SmartUXProperties.getProperty("cache.sync.public.host");
			final String port = SmartUXProperties.getProperty("cache.sync.public.port");
			
			String fileLockPath = SmartUXProperties.getProperty("SmartUXNotiPopDao.refreshNotiPop.fileLock");
			int waitTime = NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.wait"), 5);
			
			FileProcessLockFactory.start(fileLockPath, waitTime,  new FileProcessFunction("팝업공지 즉시적용", ResultCode.ApplyRequestFail) {
				
				@Override
				public void run() throws Exception {
					
					//즉시적용 호출 시 sleep 값 추가 -> DB분리건으로 OGG가 10초 이상 걸리는 경우가 있어 즉시적용 시 반영이 안돼서 추가함
					Thread.sleep(Integer.parseInt(org.apache.commons.lang.StringUtils.defaultIfEmpty(SmartUXProperties.getProperty("apply.cache.call.sleep"), "10000")));
					
					RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
							host, 
							port, 
							SmartUXProperties.getProperty("SmartUXNotiPopDao.refreshNotiPop.url")+"?callByScheduler="+callByScheduler,
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.method"), "POST"), 
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"), 
							MediaType.APPLICATION_JSON.toString(),
							NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.timeout"), 60000)).getResponseBody());
					
					result.setFlag(codeMsg.getCode());
					result.setMessage(codeMsg.getMessage());
					cLog.middleLog("긴급(비상) 팝업 공지 즉시적용", "API 호출", codeMsg.getCode(), codeMsg.getMessage());
				}
			});
		} catch (MimsCommonException e) {
			cLog.warnLog("긴급(비상) 팝업 공지 즉시적용", "실패", e.getFlag(), e.getMessage(), e.getLocalizedMessage());
			result.setFlag(e.getFlag());
			result.setMessage(e.getMessage());
		} finally {
			cLog.endLog("긴급(비상) 팝업 공지 즉시적용", loginUser, result.getFlag(), result.getMessage());
		}
		
		String resultMsg = "{\"flag\" : \"" + result.getFlag() + "\", \"message\" : \"" + result.getMessage() + "\"}";
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(resultMsg).toString(), responseHeaders, HttpStatus.OK);
	}

	@RequestMapping( value = "/admin/startup/getEmergency", method = RequestMethod.GET )
	public String getStartupNotiViewList ( @RequestParam( value = "scr_tp", required = false, defaultValue = "T" )
	String scr_tp, @RequestParam( value = "display_type", required = false, defaultValue = "S" )
	String display_type, Model model ) throws Exception {

		HTMLCleaner cleaner = new HTMLCleaner ( );
		scr_tp = cleaner.clean ( scr_tp );
		display_type = cleaner.clean ( display_type );

		getEmergencyVo getemergencyVo = new getEmergencyVo ( );
		getemergencyVo.setScr_tp ( scr_tp );
		getemergencyVo.setDisplay_type ( display_type );

		getEmergencyViewVo getemergencyviewVo = service.getEmergencyView ( getemergencyVo );

		model.addAttribute ( "scr_tp", scr_tp );
		model.addAttribute ( "display_type", display_type );
		model.addAttribute ( "vo", getemergencyviewVo );

		return "/admin/notipop/getEmergencyView";
	}

	@RequestMapping( value = "/admin/startup/getEmergencyProc", method = RequestMethod.POST )
	public String getBumpCouponInsertProc ( 
			@RequestParam( value = "scr_tp", required = false, defaultValue = "" ) String scr_tp, 
			@RequestParam( value = "display_type", required = false, defaultValue = "" ) String display_type, 
			@RequestParam( value = "status", required = false, defaultValue = "" ) String status,
			@RequestParam( value = "message_yn", required = false, defaultValue = "" ) String message_yn,
			@RequestParam( value = "message", required = false, defaultValue = "" ) String message,
			@RequestParam( value = "net_type", required = false, defaultValue = "" ) String net_type,
			@RequestParam( value = "noti_no", required = false, defaultValue = "" ) String noti_no,
			HttpServletRequest request ) throws Exception {

		String cookieID = CookieUtil.getCookieUserID ( request );
		String userIp = request.getRemoteAddr ( );

		HTMLCleaner cleaner = new HTMLCleaner ( );
		message = cleaner.clean ( message );

		try {
			if ( "".equals ( message ) ) {// 메시지가 없다면 삭제이다.
				// 캐시용 긴급공지 삭제
				notiPopVo notipopVo = new notiPopVo ( scr_tp, display_type, cookieID, userIp, noti_no);
				service.callDeleteNotiPopProc ( notipopVo, "EM" );
			} else {
				// 캐시용 긴급공지 등록
				notiPopVo notipopVo = new notiPopVo ( scr_tp, display_type, status, net_type, message_yn, message, cookieID, userIp, noti_no);
				service.callInsertNotiPopProc ( notipopVo, "EM" );
			}
		} catch ( Exception e ) {
			logger.error ( "[getEmergencyProc][" + e.getClass ( ).getName ( ) + "][" + e.getMessage ( ) + "]" );
		}

		String domain = SmartUXProperties.getProperty("smartux.domain.http");
		
		return "redirect:" + domain + "/smartux_adm/admin/startup/getEmergency.do?scr_tp=" + scr_tp+"&display_type="+display_type;
	}

}
