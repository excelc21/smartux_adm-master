package com.dmi.smartux.noti.controller;

import java.util.List;

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
import com.dmi.smartux.noti.service.NotiService;
import com.dmi.smartux.noti.vo.BaseNotiVO;

/**
 * 게시판 API 관련 Controller
 * 
 */
@Controller
public class NotiController {

	@Autowired
	NotiService notiService;

	/*
	 * handleException Error/Exception Handling
	 * 
	 * @param ex HDTVException
	 * 
	 * @return errorVO
	 * 
	 * @throws Exception
	 */
	@ExceptionHandler( SmartUXException.class )
	// @ResponseStatus(value=HttpStatus.NOT_FOUND)
	public @ResponseBody
	String handleException ( SmartUXException e, HttpServletRequest request, HttpServletResponse response ) throws SmartUXException {
		// HttpHeaders rh = new HttpHeaders();
		// rh.add("Content-Type", "text/html; charset=UTF-8");
		if ( request.getHeader ( "accept" ) == null ) {
			response.setHeader ( "content-type", "text/plain;charset=UTF-8" );
			StringBuffer sb = new StringBuffer ( );
			sb.append ( e.getFlag ( ) );
			sb.append ( GlobalCom.colsep );
			sb.append ( CharacterSet.toKorean ( e.getMessage ( ) ) );
			String result;
			// try {
			// result = new String(sb.toString().getBytes("UTF-8"));
			// } catch (UnsupportedEncodingException e1) {
			// result = sb.toString();
			// }
			result = sb.toString ( );
			return result;
			// return new ResponseEntity<String>(sb.toString(),rh,HttpStatus.CREATED);
		} else {
			if ( request.getHeader ( "accept" ).indexOf ( "application/json" ) != -1 ) {
				response.setHeader ( "content-type", "application/json;charset=UTF-8" );
				String result = "{\"error\":{\"code\":\"" + e.getFlag ( ) + "\",\"message\":\"" + CharacterSet.toKorean ( e.getMessage ( ) ) + "\"}}";
				// return new ResponseEntity<String>(result,rh,HttpStatus.CREATED);
				return result;
			} else if ( request.getHeader ( "accept" ).indexOf ( "application/xml" ) != -1 ) {
				response.setHeader ( "content-type", "application/xml;charset=UTF-8" );
				String result;
				result = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><error><code>" + e.getFlag ( ) + "</code><message>" + CharacterSet.toKorean ( e.getMessage ( ) ) + "</message></error>";
				// return new ResponseEntity<String>(result,rh,HttpStatus.CREATED);
				return result;
			} else {
				response.setHeader ( "content-type", "text/plain;charset=UTF-8" );
				StringBuffer sb = new StringBuffer ( );
				sb.append ( e.getFlag ( ) );
				sb.append ( GlobalCom.colsep );
				sb.append ( CharacterSet.toKorean ( e.getMessage ( ) ) );
				String result;
				// try {
				// result = new String(sb.toString().getBytes("UTF-8"));
				// } catch (UnsupportedEncodingException e1) {
				// result = sb.toString();
				// }
				result = sb.toString ( );
				return result;
			}
		}
	}

	/**
	 * 게시판의 캐쉬 정보를 갱신하기 위하여 사용한다.
	 * 
	 * <pre>
	 * ex)
	 * 전체 갱신		: http://접속 주소/smartux_adm/noti/cache
	 * 부분 게시판 갱신	: http://접속 주소/smartux_adm/noti/cache?callByScheduler=Y&bbs_id=게시판아이디
	 * </pre>
	 * 
	 * @param request
	 * @param response
	 * @param bbs_id 게시판 id
	 * @param callByScheduler 스케줄러 호출과 관리자의 강제 호출을 구분하기 위한 플래그로 사용한다.<br/>
	 *            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;관리자 호출 시 A 스케줄러 호출 시 Y, 단말 호출 시 N 을 사용한다.
	 * @return 갱신 성공 여부 VO
	 */
	@RequestMapping( value = "/noti/cache", method = RequestMethod.GET )
	public Result<String> refreshCacheOfNoti ( HttpServletRequest request, HttpServletResponse response, @RequestParam( value = "bbs_id", required = false )
	String bbs_id, @RequestParam( value = "callByScheduler", required = false, defaultValue = "N" )
	String callByScheduler ) {
		// #########[LOG START]#########
		Log log_logger = LogFactory.getLog ( "refreshCacheOfNoti" );
		CLog cLog = new CLog ( log_logger, request );
		cLog.startLog ( "[" + GlobalCom.isNull ( bbs_id ) + "][" + GlobalCom.isNull ( callByScheduler ) + "]" );

		Result<String> result = new Result<String> ( );
		result.setFlag ( SmartUXProperties.getProperty ( "flag.success" ) );
		result.setMessage ( SmartUXProperties.getProperty ( "message.success" ) );
		result.setRecordset ( null );
		try {
			if ( callByScheduler != null ) {
				if ( "N".equals ( callByScheduler ) ) { // 단말에서 호출한 경우
					List<BaseNotiVO> baseNotiVOList = refreshCacheOfNoti ( bbs_id, callByScheduler );
					if ( baseNotiVOList != null && baseNotiVOList.size ( ) != 0 ) {
						result.setTotal_count ( baseNotiVOList.size ( ) );
					} else {
						result.setTotal_count ( 0 );
					}
				} else if ( "A".equals ( callByScheduler ) || "Y".equals ( callByScheduler ) ) { // A:관리자 호출, Y:스케줄러 호출
																									// 에 대하여 수행한다.
					refreshCacheOfNoti ( bbs_id, callByScheduler );
				} else {
					result = getMismatchValue ( result );
				}
			} else {
				result = getMismatchValue ( result );
			}
		} catch ( Exception e ) {
			cLog.errorLog ( "[" + GlobalCom.isNull ( bbs_id ) + "][" + GlobalCom.isNull ( callByScheduler ) + "][" + e.getClass ( ).getName ( ) + "][" + e.getMessage ( ) + "]" );
			com.dmi.smartux.common.exception.ExceptionHandler handler = new com.dmi.smartux.common.exception.ExceptionHandler ( e );
			result.setFlag ( handler.getFlag ( ) );
			result.setMessage ( handler.getMessage ( ) );
		}

		// #########[LOG END]#########
		cLog.endLog ( "[" + GlobalCom.isNull ( bbs_id ) + "][" + GlobalCom.isNull ( callByScheduler ) + "] [" + result.getFlag ( ) + "]" );

		return result;
	}

	private Result<String> getMismatchValue ( Result<String> result ) {
		result.setFlag ( SmartUXProperties.getProperty ( "flag.mismatchvalue" ) );
		result.setMessage ( SmartUXProperties.getProperty ( "message.mismatchvalue", "callByScheduler", "N, A, Y 중에 하나" ) );
		return result;
	}

	private List<BaseNotiVO> refreshCacheOfNoti ( String bbs_id, String callByScheduler ) throws Exception {
		List<BaseNotiVO> returnValue = null;
		if ( bbs_id == null || "".equals ( bbs_id ) ) {
			notiService.refreshCacheOfNoti ( callByScheduler );
		} else {
			returnValue = notiService.refreshCacheOfNoti ( bbs_id, callByScheduler );
		}
		return returnValue;
	}

}
