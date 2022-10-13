package com.dmi.smartux.admin.statbbsAdmin.controller;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
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
import com.dmi.smartux.admin.statbbsAdmin.service.StatbbsAdminService;
import com.dmi.smartux.admin.statbbsAdmin.vo.StatParticipateFileVo;
import com.dmi.smartux.admin.statbbsAdmin.vo.StatPaticipantListArrVo;
import com.dmi.smartux.admin.statbbsAdmin.vo.StatPaticipantListVo;
import com.dmi.smartux.admin.statbbsAdmin.vo.StatbbsInsertProcVo;
import com.dmi.smartux.admin.statbbsAdmin.vo.StatbbsListArrVo;
import com.dmi.smartux.admin.statbbsAdmin.vo.StatbbsListVo;
import com.dmi.smartux.admin.statbbsAdmin.vo.StatbbsMiniListVo;
import com.dmi.smartux.admin.statbbsAdmin.vo.StatbbsUpdateProcVo;
import com.dmi.smartux.admin.statbbsAdmin.vo.StatbbsUpdateVo;
import com.dmi.smartux.admin.statbbsAdmin.vo.StatbbsVo;
import com.dmi.smartux.common.exception.ExceptionHandler;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.CookieUtil;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.util.HTMLCleaner;
import com.dmi.smartux.common.vo.CUDResult;
import com.dmi.smartux.common.vo.StatParticipateVo;

import net.sf.json.JSONSerializer;

@Controller
public class StatbbsAdminController {

	@Autowired
	StatbbsAdminService service;

	private final Log logger = LogFactory.getLog ( this.getClass ( ) );
	private final Log applyLogger = LogFactory.getLog("adminApplyCacheHistroy");
	
	/**
	 * 참여통계 즉시적용
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping( value = "/admin/statbbs/statbbsApplyCache", method = RequestMethod.POST )
	public ResponseEntity<String> statbbsApplyCache (
			HttpServletRequest request, 
			HttpServletResponse response,
			@RequestParam(value="callByScheduler",  defaultValue="Y") final  String callByScheduler
			) throws Exception {

		final CLog cLog = new CLog(applyLogger, request.getRemoteAddr(), request.getRequestURI(), request.getMethod());
		final String loginUser = callByScheduler.equals("Y") ? "batch" : CookieUtil.getCookieUserID(request);
		final CUDResult result = new CUDResult();
		
		try {
			cLog.startLog("참여통계 즉시적용",  loginUser);
			final String host = SmartUXProperties.getProperty("cache.sync.public.host");
			final String port = SmartUXProperties.getProperty("cache.sync.public.port");
			
			String fileLockPath = SmartUXProperties.getProperty("BbsStatDao.refreshBbsStat.fileLock");
			int waitTime = NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.wait"), 5);
			
			FileProcessLockFactory.start(fileLockPath, waitTime,  new FileProcessFunction("참여통계 즉시적용", ResultCode.ApplyRequestFail) {
				
				@Override
				public void run() throws Exception {
					
					//즉시적용 호출 시 sleep 값 추가 -> DB분리건으로 OGG가 10초 이상 걸리는 경우가 있어 즉시적용 시 반영이 안돼서 추가함
					Thread.sleep(Integer.parseInt(org.apache.commons.lang.StringUtils.defaultIfEmpty(SmartUXProperties.getProperty("apply.cache.call.sleep"), "10000")));
					
					RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
							host, 
							port, 
							SmartUXProperties.getProperty("BbsStatDao.refreshBbsStat.url")+"?callByScheduler="+callByScheduler,
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.method"), "POST"), 
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"), 
							MediaType.APPLICATION_JSON.toString(),
							NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.timeout"), 60000)).getResponseBody());
					
					result.setFlag(codeMsg.getCode());
					result.setMessage(codeMsg.getMessage());
					cLog.middleLog("참여통계 즉시적용", "API 호출", codeMsg.getCode(), codeMsg.getMessage());
				}
			});
		} catch (MimsCommonException e) {
			cLog.warnLog("참여통계 즉시적용", "실패", e.getFlag(), e.getMessage(), e.getLocalizedMessage());
			result.setFlag(e.getFlag());
			result.setMessage(e.getMessage());
		} finally {
			cLog.endLog("참여통계 즉시적용", loginUser, result.getFlag(), result.getMessage());
		}
		
		String resultMsg = "{\"flag\" : \"" + result.getFlag() + "\", \"message\" : \"" + result.getMessage() + "\"}";
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(resultMsg).toString(), responseHeaders, HttpStatus.OK);
	}
	
	
	/**
	 * 참여통계 목록
	 * @param findName
	 * @param findValue
	 * @param use_yn
	 * @param pageNum
	 * @param model
	 * @return
	 */
	@RequestMapping( value = "/admin/statbbs/statbbsList", method = RequestMethod.GET )
	public String getStatbbsList ( @RequestParam( value = "findName", required = false, defaultValue = "" )
	String findName, @RequestParam( value = "findValue", required = false, defaultValue = "" )
	String findValue, @RequestParam( value = "use_yn", required = false, defaultValue = "" )
	String use_yn, @RequestParam( value = "pageNum", required = false, defaultValue = "1" )
	String pageNum, Model model ) {

		HTMLCleaner cleaner = new HTMLCleaner ( );
		findName = cleaner.clean ( findName );
		findValue = cleaner.clean ( findValue );
		use_yn = cleaner.clean ( use_yn );
		pageNum = cleaner.clean ( pageNum );

		StatbbsListVo statbbslistVo = new StatbbsListVo ( );

		pageNum = GlobalCom.isNull ( pageNum, "1" );

		statbbslistVo.setFindName ( findName );
		statbbslistVo.setFindValue ( findValue );
		statbbslistVo.setPageNum ( Integer.parseInt ( pageNum ) );

		statbbslistVo.setPageSize ( GlobalCom.isNumber ( statbbslistVo.getPageSize ( ), 10 ) );
		statbbslistVo.setPageNum ( GlobalCom.isNumber ( statbbslistVo.getPageNum ( ), 1 ) );
		statbbslistVo.setUse_yn ( GlobalCom.isNull ( use_yn, "" ) );

		statbbslistVo.setFindName ( GlobalCom.isNull ( statbbslistVo.getFindName ( ), "TITLE" ) );
		statbbslistVo.setFindValue ( GlobalCom.isNull ( statbbslistVo.getFindValue ( ) ) );

		statbbslistVo.setBlockSize ( GlobalCom.isNumber ( statbbslistVo.getBlockSize ( ), 10 ) );

		try {
			List<StatbbsListArrVo> list = service.getStatbbsList ( statbbslistVo );

			statbbslistVo.setPageCount ( service.getStatbbsListTotalCnt ( statbbslistVo ) );

			statbbslistVo.setList ( list );

			model.addAttribute ( "vo", statbbslistVo );
		} catch ( Exception e ) {
			// logger.error("getAdminList "+e.getClass().getName());
			// logger.error("getAdminList "+e.getMessage());
			logger.error ( "[statbbsList][" + e.getClass ( ).getName ( ) + "][" + e.getMessage ( ) + "]" );
		}

		return "/admin/statbbs/statbbsList";
	}

	@RequestMapping( value = "/admin/statbbs/statbbsInsert", method = RequestMethod.GET )
	public String setStatbbsInsert ( @RequestParam( value = "findName", required = false, defaultValue = "" )
	String findName, @RequestParam( value = "findValue", required = false, defaultValue = "" )
	String findValue, @RequestParam( value = "use_yn", required = false, defaultValue = "" )
	String use_yn, @RequestParam( value = "pageNum", required = false, defaultValue = "1" )
	String pageNum, Model model ) {

		HTMLCleaner cleaner = new HTMLCleaner ( );
		findName = cleaner.clean ( findName );
		findValue = cleaner.clean ( findValue );
		use_yn = cleaner.clean ( use_yn );
		pageNum = GlobalCom.isNull ( cleaner.clean ( pageNum ), "1" );

		try {
			StatbbsVo statbbsVo = new StatbbsVo ( );
			statbbsVo.setP_findName ( findName );
			statbbsVo.setP_findValue ( findValue );
			statbbsVo.setP_pageNum ( Integer.parseInt ( pageNum ) );
			statbbsVo.setP_use_yn ( use_yn );

			model.addAttribute ( "vo", statbbsVo );

		} catch ( java.lang.Exception e ) {
			logger.error ( "[setStatbbsInsert][" + e.getClass ( ).getName ( ) + "][" + e.getMessage ( ) + "]" );
		}

		return "/admin/statbbs/statbbsInsert";
	}

	@RequestMapping( value = "/admin/statbbs/statbbsInsertProc", method = RequestMethod.POST )
	public String setStatbbsInsertProc ( @RequestParam( value = "stat_no", required = false, defaultValue = "" )
	String stat_no, @RequestParam( value = "title", required = false, defaultValue = "" )
	String title, @RequestParam( value = "content", required = false, defaultValue = "" )
	String content, @RequestParam( value = "e_date", required = false, defaultValue = "" )
	String e_date, @RequestParam( value = "use_yn", required = false, defaultValue = "" )
	String use_yn, HttpServletRequest request ) throws Exception {

		// 참여 내역 저장할 파일을 생성한다.
		String filePath = SmartUXProperties.getProperty ( "statbbs.filemake.dir" ) + GlobalCom.getTodayFormat4_24 ( ) + ".stat";

		FileOutputStream fileout = null;
		ObjectOutputStream out = null;

		HTMLCleaner cleaner = new HTMLCleaner ( );
		stat_no = cleaner.clean ( stat_no );
		title = cleaner.clean ( title );
		// content = cleaner.clean(content);
		e_date = cleaner.clean ( e_date );
		use_yn = cleaner.clean ( use_yn );
		String cookieID = CookieUtil.getCookieUserID ( request );
		String userIp = request.getRemoteAddr ( );

		StatbbsInsertProcVo statbbsinsertprocVo = new StatbbsInsertProcVo ( );
		statbbsinsertprocVo.setStat_no ( stat_no );
		statbbsinsertprocVo.setTitle ( title );
		statbbsinsertprocVo.setContent ( content );
		statbbsinsertprocVo.setE_date ( e_date );
		statbbsinsertprocVo.setUse_yn ( use_yn );
		statbbsinsertprocVo.setAct_gbn ( "I" );
		statbbsinsertprocVo.setAct_id ( cookieID );
		statbbsinsertprocVo.setAct_ip ( userIp );
		statbbsinsertprocVo.setStat_file_path ( filePath );

		try {
			String statNo = service.setStatbbsInsertProc ( statbbsinsertprocVo );
		} catch ( Exception e ) {
			logger.error ( "[setStatbbsInsertProc][" + e.getClass ( ).getName ( ) + "][" + e.getMessage ( ) + "]" );
		}

		String domain = SmartUXProperties.getProperty("smartux.domain.http");

		return "redirect:" + domain + "/smartux_adm/admin/statbbs/statbbsList.do";
	}

	// 테스트를 위해 만들어 놓음.
	@RequestMapping( value = "/admin/statbbs/testWriteStatFile", method = RequestMethod.GET )
	public ResponseEntity<String> testWriteStatFile ( @RequestParam( value = "filePath", required = false, defaultValue = "" )
	String filePath, HttpServletRequest request, Model model ) {
		String result = "";
		FileOutputStream fileout = null;
		ObjectOutputStream out = null;
		try {
			// 참여 내역 저장할 파일을 생성한다.
			fileout = new FileOutputStream ( filePath );
			out = new ObjectOutputStream ( fileout );

			for ( int x = 0; x < 100; x++ ) {
				StatParticipateVo plsVo = new StatParticipateVo ( "M12061600093" + x, "001c.627d.6f62", "010-1234-1234", GlobalCom.getTodayFormat4_24 ( ) );
				out.writeObject ( plsVo );
			}
		} catch ( java.lang.Exception e ) {
		} finally {
			try {
				out.close ( );
				fileout.close ( );
			} catch ( java.lang.Exception e ) {
			}
		}

		result = "{\"result\":{\"message\":\"0000\",\"flag\":\"성공\"}}";

		HttpHeaders responseHeaders = new HttpHeaders ( );
		responseHeaders.add ( "Content-Type", "text/html; charset=UTF-8" );
		return new ResponseEntity<String> ( JSONSerializer.toJSON ( result ).toString ( ), responseHeaders, HttpStatus.CREATED );
	}

	@RequestMapping( value = "/admin/statbbs/statbbsUpdate", method = RequestMethod.GET )
	public String setStatbbsUpdate ( @RequestParam( value = "findName", required = false, defaultValue = "" )
	String findName, @RequestParam( value = "findValue", required = false, defaultValue = "" )
	String findValue, @RequestParam( value = "use_yn", required = false, defaultValue = "" )
	String use_yn, @RequestParam( value = "pageNum", required = false, defaultValue = "1" )
	String pageNum, @RequestParam( value = "stat_no", required = false, defaultValue = "" )
	String stat_no, Model model ) {
		HTMLCleaner cleaner = new HTMLCleaner ( );
		findName = cleaner.clean ( findName );
		findValue = cleaner.clean ( findValue );
		use_yn = cleaner.clean ( use_yn );
		pageNum = cleaner.clean ( pageNum );
		stat_no = cleaner.clean ( stat_no );

		StatbbsUpdateVo statbbsupdateVo = null;

		try {

			statbbsupdateVo = service.setStatbbsUpdate ( stat_no );
			statbbsupdateVo.setP_findName ( findName );
			statbbsupdateVo.setP_findValue ( findValue );
			statbbsupdateVo.setP_pageNum ( Integer.parseInt ( pageNum ) );
			statbbsupdateVo.setP_use_yn ( use_yn );

		} catch ( Exception e ) {
			logger.error ( "[setStatbbsUpdate][" + e.getClass ( ).getName ( ) + "][" + e.getMessage ( ) + "]" );
		}

		model.addAttribute ( "vo", statbbsupdateVo );

		return "/admin/statbbs/statbbsUpdate";
	}

	@RequestMapping( value = "/admin/statbbs/statbbsUpdateProc", method = RequestMethod.POST )
	public String setStatbbsUpdateProc ( @RequestParam( value = "stat_no", required = false, defaultValue = "" )
	String stat_no, @RequestParam( value = "title", required = false, defaultValue = "" )
	String title, @RequestParam( value = "content", required = false, defaultValue = "" )
	String content, @RequestParam( value = "e_date", required = false, defaultValue = "" )
	String e_date, @RequestParam( value = "use_yn", required = false, defaultValue = "" )
	String use_yn, @RequestParam( value = "findName", required = false, defaultValue = "" )
	String findName, @RequestParam( value = "findValue", required = false, defaultValue = "" )
	String findValue, @RequestParam( value = "p_use_yn", required = false, defaultValue = "" )
	String p_use_yn, @RequestParam( value = "pageNum", required = false, defaultValue = "1" )
	String pageNum, HttpServletRequest request, Model model ) throws Exception {

		HTMLCleaner cleaner = new HTMLCleaner ( );
		stat_no = cleaner.clean ( stat_no );
		title = cleaner.clean ( title );
		// content = cleaner.clean(content);
		e_date = cleaner.clean ( e_date );
		use_yn = cleaner.clean ( use_yn );
		findName = cleaner.clean ( findName );
		findValue = cleaner.clean ( findValue );
		p_use_yn = cleaner.clean ( p_use_yn );
		pageNum = cleaner.clean ( pageNum );

		String cookieID = CookieUtil.getCookieUserID ( request );
		String userIp = request.getRemoteAddr ( );

		StatbbsUpdateProcVo statbbsupdateprocVo = new StatbbsUpdateProcVo ( );
		statbbsupdateprocVo.setStat_no ( stat_no );
		statbbsupdateprocVo.setTitle ( title );
		statbbsupdateprocVo.setContent ( content );
		statbbsupdateprocVo.setE_date ( e_date );
		statbbsupdateprocVo.setUse_yn ( use_yn );
		statbbsupdateprocVo.setAct_gbn ( "U" );
		statbbsupdateprocVo.setAct_id ( cookieID );
		statbbsupdateprocVo.setAct_ip ( userIp );

		try {
			service.setStatbbsUpdateProc ( statbbsupdateprocVo );
		} catch ( Exception e ) {
			logger.error ( "[setStatbbsUpdateProc][" + e.getClass ( ).getName ( ) + "][" + e.getMessage ( ) + "]" );
		}

		String domain = SmartUXProperties.getProperty("smartux.domain.http");

		model.addAttribute ( "findName", findName );
		model.addAttribute ( "findValue", findValue );
		model.addAttribute ( "pageNum", pageNum );
		model.addAttribute ( "use_yn", p_use_yn );
		return "redirect:" + domain + "/smartux_adm/admin/statbbs/statbbsList.do";
	}

	@RequestMapping( value = "/admin/statbbs/statbbsDelete", method = RequestMethod.POST )
	public ResponseEntity<String> setStatbbsDelete ( @RequestParam( value = "stat_no", required = false, defaultValue = "" )
	String stat_no, HttpServletRequest request, Model model ) throws Exception {

		String result = "";
		String resultcode = "";
		String resultmessage = "";

		String cookieID = CookieUtil.getCookieUserID ( request );
		String userIp = request.getRemoteAddr ( );

		try {
			service.setStatbbsDelete ( stat_no, cookieID, userIp );
			resultcode = SmartUXProperties.getProperty ( "flag.success" );
			resultmessage = SmartUXProperties.getProperty ( "message.success" );
		} catch ( Exception e ) {
			logger.error ( "[setStatbbsDelete][" + e.getClass ( ).getName ( ) + "][" + e.getMessage ( ) + "]" );

			ExceptionHandler handler = new ExceptionHandler ( e );
			resultcode = handler.getFlag ( );
			resultmessage = handler.getMessage ( );
		}

		result = "{\"result\":{\"message\":\"" + resultmessage + "\",\"flag\":\"" + resultcode + "\"}}";

		HttpHeaders responseHeaders = new HttpHeaders ( );
		responseHeaders.add ( "Content-Type", "text/html; charset=UTF-8" );
		return new ResponseEntity<String> ( JSONSerializer.toJSON ( result ).toString ( ), responseHeaders, HttpStatus.CREATED );
	}

	@RequestMapping( value = "/admin/statbbs/statPaticipantList", method = RequestMethod.GET )
	public String getStatPaticipantList ( @RequestParam( value = "findName", required = false, defaultValue = "" )
	String findName, @RequestParam( value = "findValue", required = false, defaultValue = "" )
	String findValue, @RequestParam( value = "pageNum", required = false, defaultValue = "1" )
	String pageNum, @RequestParam( value = "stat_no", required = false, defaultValue = "" )
	String stat_no, Model model ) {

		HTMLCleaner cleaner = new HTMLCleaner ( );
		findName = cleaner.clean ( findName );
		findValue = cleaner.clean ( findValue );
		pageNum = cleaner.clean ( pageNum );
		stat_no = cleaner.clean ( stat_no );

		StatPaticipantListVo statpaticipantlistVo = new StatPaticipantListVo ( );

		pageNum = GlobalCom.isNull ( pageNum, "1" );

		statpaticipantlistVo.setFindName ( findName );
		statpaticipantlistVo.setFindValue ( findValue );
		statpaticipantlistVo.setPageNum ( Integer.parseInt ( pageNum ) );
		statpaticipantlistVo.setStat_no ( stat_no );

		statpaticipantlistVo.setPageSize ( GlobalCom.isNumber ( statpaticipantlistVo.getPageSize ( ), 10 ) );
		statpaticipantlistVo.setPageNum ( GlobalCom.isNumber ( statpaticipantlistVo.getPageNum ( ), 1 ) );

		statpaticipantlistVo.setFindName ( GlobalCom.isNull ( statpaticipantlistVo.getFindName ( ), "SA_ID" ) );
		statpaticipantlistVo.setFindValue ( GlobalCom.isNull ( statpaticipantlistVo.getFindValue ( ) ) );

		statpaticipantlistVo.setBlockSize ( GlobalCom.isNumber ( statpaticipantlistVo.getBlockSize ( ), 10 ) );
		try {

			String statbbsListCnt = SmartUXProperties.getProperty ( "statbbs.sbox.listcnt" );// 지정 갯수만 선택가능 하도록(지난 이벤트
																								// 참여이력을 볼 필요가 없으므로)
			List<StatbbsMiniListVo> sboxListVo = service.getStatbbsMiniList ( statbbsListCnt );
			if ( ( statpaticipantlistVo.getStat_no ( ) == null || statpaticipantlistVo.getStat_no ( ).equals ( "" ) ) && sboxListVo != null ) {
				statpaticipantlistVo.setStat_no ( sboxListVo.get ( 0 ).getStat_no ( ) );
			}

			StatParticipateFileVo statparticipatefileVo = service.getStatPaticipantLoadList ( statpaticipantlistVo );
			List<StatPaticipantListArrVo> list = statparticipatefileVo.getStatpaticipantlistarrVo ( );
			// List<StatPaticipantListArrVo> list = service.getStatPaticipantList(statpaticipantlistVo);

			statpaticipantlistVo.setPageCount ( statparticipatefileVo.getTotalCnt ( ) );
			statpaticipantlistVo.setList ( list );

			model.addAttribute ( "sbox_vo", sboxListVo );
			model.addAttribute ( "vo", statpaticipantlistVo );
		} catch ( Exception e ) {
			logger.error ( "[getStatPaticipantList][" + e.getClass ( ).getName ( ) + "][" + e.getMessage ( ) + "]" );
		}

		return "/admin/statbbs/statPaticipantList";
	}

	@RequestMapping( value = "/admin/statbbs/excelPaticipantList", method = RequestMethod.GET )
	public void excelPaticipantList ( @RequestParam( value = "stat_no", required = false, defaultValue = "" )
	String stat_no, HttpServletResponse response, HttpServletRequest request, Map<String, Object> model ) {

		HTMLCleaner cleaner = new HTMLCleaner ( );
		stat_no = cleaner.clean ( stat_no );

		String excelName = "PaticipantList_" + GlobalCom.getTodayFormat ( );

		StatPaticipantListVo statpaticipantlistVo = new StatPaticipantListVo ( );

		statpaticipantlistVo.setStat_no ( stat_no );

		statpaticipantlistVo.setFindName ( GlobalCom.isNull ( statpaticipantlistVo.getFindName ( ), "SA_ID" ) );
		statpaticipantlistVo.setFindValue ( GlobalCom.isNull ( statpaticipantlistVo.getFindValue ( ) ) );

		response.setHeader ( "Content-Disposition", "attachment; filename=" + excelName + ".csv" );
		// response.setContentType("application/vnd.ms-excel");
		response.setContentType ( "text/csv" );
		try {
			String cookieID = CookieUtil.getCookieUserID ( request );

			if ( cookieID != null && !cookieID.equals ( "" ) ) {
				File statFile = service.excelPaticipantList ( statpaticipantlistVo );

				FileInputStream filein = null;
				DataInputStream in = null;
				BufferedReader br = null;

				ServletOutputStream outStream = response.getOutputStream ( );

				if ( statFile.exists ( ) ) {// 파일 있다면
					filein = new FileInputStream ( statFile );
					in = new DataInputStream ( filein );
					br = new BufferedReader ( new InputStreamReader ( in ) );

					String strReadLine = "";
					while ( ( strReadLine = br.readLine ( ) ) != null ) {
						outStream.println ( strReadLine );
					}
				}

			} else {
				// 비로그인!!!
			}
		} catch ( Exception e ) {
			logger.error ( "[excelPaticipantList][" + e.getClass ( ).getName ( ) + "][" + e.getMessage ( ) + "]" );
		}

	}

}
