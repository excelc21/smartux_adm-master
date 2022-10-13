package com.dmi.smartux.admin.terms.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.dmi.commons.consts.ResultCode;
import com.dmi.commons.exception.MimsCommonException;
import com.dmi.commons.factory.FileProcessFunction;
import com.dmi.commons.factory.FileProcessLockFactory;
import com.dmi.commons.logger.CLog;
import com.dmi.commons.model.RestfulCodeMsg;
import com.dmi.commons.utility.HttpClientUtils;
import com.dmi.commons.utility.RestfulUtils;
import com.dmi.smartux.admin.terms.service.TermsAccessService;
import com.dmi.smartux.admin.terms.vo.TermsAccessDetailSearchVo;
import com.dmi.smartux.admin.terms.vo.TermsAccessDetailVo;
import com.dmi.smartux.admin.terms.vo.TermsAccessGroupSearchVo;
import com.dmi.smartux.admin.terms.vo.TermsAccessGroupVo;
import com.dmi.smartux.admin.terms.vo.TermsAccessInfoSearchVo;
import com.dmi.smartux.admin.terms.vo.TermsAccessInfoVo;
import com.dmi.smartux.admin.terms.vo.TermsAccessSearchVo;
import com.dmi.smartux.admin.terms.vo.TermsAccessVo;
import com.dmi.smartux.common.exception.ExceptionHandler;
import com.dmi.smartux.common.exception.SmartUXException;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.CookieUtil;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.util.HTMLCleaner;
import com.dmi.smartux.common.vo.CUDResult;

import net.sf.json.JSONSerializer;

@Controller
public class TermsAccessController {
	
	private final Log logger = LogFactory.getLog(this.getClass());

	private final Log applyLogger = LogFactory.getLog("adminApplyCacheHistroy");
	
	@Autowired
	private TermsAccessService service;
	
	/**
	 * 약관동의 즉시적용
	 * @param
	 * @return
	 */	
	@RequestMapping(value = "/admin/terms/applyCache", method = RequestMethod.POST)
	public ResponseEntity<String> termsAccessApplyCache(
			HttpServletRequest request,
			HttpServletResponse response
			) throws Exception {
		
		final CLog cLog = new CLog(applyLogger, request.getRemoteAddr(), request.getRequestURI(), request.getMethod());
		final String loginUser = CookieUtil.getCookieUserID(request);
		final CUDResult result = new CUDResult();
		
		
		try {
			cLog.startLog("약관동의 즉시적용 : ",  loginUser);
			final String host = SmartUXProperties.getProperty("cache.sync.public.host");
			final String port = SmartUXProperties.getProperty("cache.sync.public.port");
			
			String fileLockPath = SmartUXProperties.getProperty("TermsAccessDao.refreshTermsAccess.fileLock");
			int waitTime = NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.wait"), 5);
			
			FileProcessLockFactory.start(fileLockPath, waitTime,  new FileProcessFunction("약관동의 즉시적용", ResultCode.ApplyRequestFail) {
				
				@Override
				public void run() throws Exception {
					
					//즉시적용 호출 시 sleep 값 추가 -> DB분리건으로 OGG가 10초 이상 걸리는 경우가 있어 즉시적용 시 반영이 안돼서 추가함
					Thread.sleep(Integer.parseInt(org.apache.commons.lang.StringUtils.defaultIfEmpty(SmartUXProperties.getProperty("apply.cache.call.sleep"), "10000")));
					
					RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
							host, 
							port, 
							SmartUXProperties.getProperty("TermsAccessDao.refreshTermsAccess.url"),
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.method"), "POST"), 
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"), 
							MediaType.APPLICATION_JSON.toString(),
							NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.timeout"), 60000)).getResponseBody());
					
					result.setFlag(codeMsg.getCode());
					result.setMessage(codeMsg.getMessage());
					cLog.middleLog("약관동의 즉시적용", "API 호출", codeMsg.getCode(), codeMsg.getMessage());
				}
			});
		} catch (MimsCommonException e) {
			cLog.warnLog("약관동의 즉시적용", "실패", e.getFlag(), e.getMessage(), e.getLocalizedMessage());
			result.setFlag(e.getFlag());
			result.setMessage(e.getMessage());
		} finally {
			cLog.endLog("약관동의 즉시적용", loginUser, result.getFlag(), result.getMessage());
		}

		String resultMsg = "{\"flag\" : \"" + result.getFlag() + "\", \"message\" : \"" + result.getMessage() + "\"}";
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(resultMsg).toString(), responseHeaders, HttpStatus.OK);
		
	}
	
	/**
	 * 약관 마스터 목록
	 * 
	 * @param model
	 * @param findName
	 * @param findValue
	 * @param display
	 * @param pageNum
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/terms/getAccessList", method = RequestMethod.GET)
	public String getAccessList(Model model,
			@RequestParam(value = "findName", required = false, defaultValue = "") String findName,
			@RequestParam(value = "findValue", required = false, defaultValue = "") String findValue,
			@RequestParam(value = "display", required = false, defaultValue = "") String display,
			@RequestParam(value = "pageNum", required = false, defaultValue = "1") String pageNum) throws Exception {

		findName = HTMLCleaner.clean(findName);
		display = HTMLCleaner.clean(display);
		pageNum = HTMLCleaner.clean(pageNum);

		TermsAccessSearchVo vo = new TermsAccessSearchVo();
		// 페이지
		vo.setPageSize(GlobalCom.isNumber(vo.getPageSize(), 10));
		vo.setBlockSize(GlobalCom.isNumber(vo.getBlockSize(), 10));
		vo.setPageNum(GlobalCom.isNumber(Integer.parseInt(GlobalCom.isNull(pageNum, "1")), 1));
		// 검색
		vo.setFindName(GlobalCom.isNull(findName, "ACCESS_MST_TITLE"));
		vo.setFindValue(findValue);
		vo.setDisplay(display);
		
		String[] dataType01 = SmartUXProperties.getProperty("access.service.type").split("\\|");		
		List<String> serviceType = new ArrayList<String>();
		for (int i = 0; i < dataType01.length; i++) {
			String[] dataType02 = dataType01[i].split("\\^");
			serviceType.add(dataType02[0]);
			vo.setService_type(serviceType);
		}
				
		List<TermsAccessVo> list = service.getAccessList(vo);
		vo.setPageCount(service.getAccessListCnt(vo));

		model.addAttribute("list", list);
		model.addAttribute("vo", vo);
		model.addAttribute("serviceType", service.getAccessType("S"));
		model.addAttribute("displayType", service.getAccessType("D"));

		return "/admin/terms/getAccessList";
	}
	
	/**
	 * 약관 마스터 상세조회
	 * 
	 * @param model
	 * @param findName
	 * @param findValue
	 * @param display
	 * @param pageNum
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/terms/updateAccess", method = RequestMethod.GET)
	public String getAccessMst(Model model,
			@RequestParam(value = "access_mst_id", required = false, defaultValue = "") String access_mst_id,
			@RequestParam(value = "findName", required = false, defaultValue = "") String findName,
			@RequestParam(value = "findValue", required = false, defaultValue = "") String findValue,
			@RequestParam(value = "display", required = false, defaultValue = "") String display,
			@RequestParam(value = "pageNum", required = false, defaultValue = "1") String pageNum) throws Exception {

		findName = HTMLCleaner.clean(findName);
		display = HTMLCleaner.clean(display);
		pageNum = HTMLCleaner.clean(pageNum);
		access_mst_id = HTMLCleaner.clean(access_mst_id);

		TermsAccessSearchVo vo = new TermsAccessSearchVo();
		// 페이지
		vo.setPageSize(GlobalCom.isNumber(vo.getPageSize(), 10));
		vo.setBlockSize(GlobalCom.isNumber(vo.getBlockSize(), 10));
		vo.setPageNum(GlobalCom.isNumber(Integer.parseInt(GlobalCom.isNull(pageNum, "1")), 1));
		// 검색
		vo.setFindName(GlobalCom.isNull(findName, "ACCESS_MST_TITLE"));
		vo.setFindValue(findValue);
		vo.setDisplay(display);
		vo.setAccess_mst_id(access_mst_id);

		TermsAccessVo accessVo = service.getAccessMst(vo);

		TermsAccessGroupSearchVo agsVO = new TermsAccessGroupSearchVo();
		agsVO.setAccess_mst_id(access_mst_id);

		List<TermsAccessGroupVo> list = service.getRegGroupList(agsVO);
		String access_info_ids = "";
		for (int i = 0; i < list.size(); i++) {
			if (i == 0) {
				access_info_ids = list.get(i).getAccess_info_id();
			} else {
				access_info_ids = access_info_ids + "," + list.get(i).getAccess_info_id();
			}
		}

		model.addAttribute("vo", vo);
		model.addAttribute("accessInfo", accessVo);
		model.addAttribute("serviceType", service.getAccessType("S"));
		model.addAttribute("displayType", service.getAccessType("D"));
		model.addAttribute("isUpdate", "1");
		model.addAttribute("access_info_ids", access_info_ids);
		model.addAttribute("access_info_titles", list);

		return "/admin/terms/accessForm";
	}
	
	/**
	 * 약관 마스터 등록 화면
	 * 
	 * @param model
	 * @param findName
	 * @param findValue
	 * @param display
	 * @param pageNum
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/terms/insertAccess", method = RequestMethod.GET)
	public String insertAccess(Model model,
			@RequestParam(value = "findName", required = false, defaultValue = "") String findName,
			@RequestParam(value = "findValue", required = false, defaultValue = "") String findValue,
			@RequestParam(value = "display", required = false, defaultValue = "") String display,
			@RequestParam(value = "pageNum", required = false, defaultValue = "1") String pageNum) throws Exception {

		findName = HTMLCleaner.clean(findName);
		display = HTMLCleaner.clean(display);
		pageNum = HTMLCleaner.clean(pageNum);

		TermsAccessSearchVo vo = new TermsAccessSearchVo();
		// 페이지
		vo.setPageSize(GlobalCom.isNumber(vo.getPageSize(), 10));
		vo.setBlockSize(GlobalCom.isNumber(vo.getBlockSize(), 10));
		vo.setPageNum(GlobalCom.isNumber(Integer.parseInt(GlobalCom.isNull(pageNum, "1")), 1));
		// 검색
		vo.setFindName(GlobalCom.isNull(findName, "ACCESS_MST_TITLE"));
		vo.setFindValue(findValue);
		vo.setDisplay(display);

		TermsAccessVo accessVo = new TermsAccessVo();

		model.addAttribute("vo", vo);
		model.addAttribute("accessInfo", accessVo);
		model.addAttribute("serviceType", service.getAccessType("S"));
		model.addAttribute("displayType", service.getAccessType("D"));
		model.addAttribute("isUpdate", "0");
		model.addAttribute("access_info_ids", "");
		model.addAttribute("access_info_titles", "");

		return "/admin/terms/accessForm";
	}
	
	/**
	 * 약관 마스터 등록
	 * 
	 * @param service_type
	 * @param display
	 * @param access_mst_title
	 * @param header_text
	 * @param footer_text
	 * @param display_yn
	 * @param is_adt
	 * @param delete_yn
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/terms/insertProc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> accessInsertProc(Model model,
			@RequestParam(value = "service_type", required = false, defaultValue = "") String service_type,
			@RequestParam(value = "display", required = false, defaultValue = "") String display,
			@RequestParam(value = "access_mst_title", required = false, defaultValue = "") String access_mst_title,
			@RequestParam(value = "header_text", required = false, defaultValue = "") String header_text,
			@RequestParam(value = "footer_text", required = false, defaultValue = "") String footer_text,
			@RequestParam(value = "display_yn", required = false, defaultValue = "") String display_yn,
			@RequestParam(value = "is_adt", required = false, defaultValue = "") String is_adt,
			@RequestParam(value = "delete_yn", required = false, defaultValue = "") String delete_yn,
			@RequestParam(value = "access_info_id", required = false, defaultValue = "") String access_info_id,
			HttpServletRequest request) throws Exception {

		String resCode = "";
		String resMessage = "";
		String cookieID = CookieUtil.getCookieUserID(request);

		try {

			TermsAccessVo vo = new TermsAccessVo();
			vo.setService_type(service_type);
			vo.setDisplay(display);
			vo.setAccess_mst_title(access_mst_title);
			vo.setHeader_text(header_text);
			vo.setFooter_text(footer_text);
			vo.setDisplay_yn("Y");
			vo.setIs_adt(is_adt);
			vo.setDelete_yn("N");
			vo.setReg_id(cookieID);
			vo.setMod_id(cookieID);

			if ("Y".equals(vo.getIs_adt()) && service.getAdtYCnt(vo) > 0) {
				resCode = SmartUXProperties.getProperty("flag.fail");
				resMessage = "이미 동일한 타입과 노출화면이 존재하여 등록할 수 없습니다.";
			} else if ("N".equals(vo.getIs_adt()) && service.getAdtNCnt(vo) > 0) {
				resCode = SmartUXProperties.getProperty("flag.fail");
				resMessage = "이미 동일한 타입과 노출화면이 존재하여 등록할 수 없습니다.";
			} else {
				service.insertProc(vo, access_info_id);
				resCode = SmartUXProperties.getProperty("flag.success");
				resMessage = SmartUXProperties.getProperty("message.success");
			}

		} catch (Exception e) {
			logger.error("[accessInsertProc]["+e.getClass().getName()+"]["+e.getMessage()+"]");		
			ExceptionHandler handler = new ExceptionHandler(e);
			resCode = handler.getFlag();
			resMessage = handler.getMessage();
		}

		String result = "{\"res\" : \"" + resCode + "\", \"msg\" : \"" + resMessage + "\"}";
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(result, responseHeaders, HttpStatus.CREATED);
	}
	
	/**
	 * 약관 마스터 수정
	 * 
	 * @param model
	 * @param access_mst_id
	 * @param service_type
	 * @param display
	 * @param access_mst_title
	 * @param header_text
	 * @param footer_text
	 * @param display_yn
	 * @param is_adt
	 * @param delete_yn
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/terms/updateProc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> accessUpdateProc(Model model,
			@RequestParam(value = "access_mst_id", required = false, defaultValue = "") String access_mst_id,
			@RequestParam(value = "service_type", required = false, defaultValue = "") String service_type,
			@RequestParam(value = "display", required = false, defaultValue = "") String display,
			@RequestParam(value = "access_mst_title", required = false, defaultValue = "") String access_mst_title,
			@RequestParam(value = "header_text", required = false, defaultValue = "") String header_text,
			@RequestParam(value = "footer_text", required = false, defaultValue = "") String footer_text,
			@RequestParam(value = "display_yn", required = false, defaultValue = "") String display_yn,
			@RequestParam(value = "is_adt", required = false, defaultValue = "") String is_adt,
			@RequestParam(value = "delete_yn", required = false, defaultValue = "") String delete_yn,
			@RequestParam(value = "access_info_id", required = false, defaultValue = "") String access_info_id,
			HttpServletRequest request) throws Exception {

		String resCode = "";
		String resMessage = "";
		String cookieID = CookieUtil.getCookieUserID(request);

		try {

			TermsAccessVo vo = new TermsAccessVo();
			vo.setAccess_mst_id(access_mst_id);
			vo.setService_type(service_type);
			vo.setDisplay(display);
			vo.setAccess_mst_title(access_mst_title);
			vo.setHeader_text(header_text);
			vo.setFooter_text(footer_text);
			vo.setDisplay_yn("Y");
			vo.setIs_adt(is_adt);
			vo.setReg_id(cookieID);
			vo.setMod_id(cookieID);

			if ("Y".equals(vo.getIs_adt()) && service.getAdtYCnt(vo) > 0) {
				resCode = SmartUXProperties.getProperty("flag.fail");
				resMessage = "검수용 Y인 약관 마스터가 이미 존재 하여 등록 하실수 없습니다.";
			} else if ("N".equals(vo.getIs_adt()) && service.getAdtNCnt(vo) > 0) {
				resCode = SmartUXProperties.getProperty("flag.fail");
				resMessage = "검수용 N인 약관 마스터가 이미 존재 하여 등록 하실수 없습니다.";
			} else {
				service.updateProc(vo, access_info_id);
				resCode = SmartUXProperties.getProperty("flag.success");
				resMessage = SmartUXProperties.getProperty("message.success");
			}

		} catch (Exception e) {
			logger.error("[accessUpdateProc]["+e.getClass().getName()+"]["+e.getMessage()+"]");		
			ExceptionHandler handler = new ExceptionHandler(e);
			resCode = handler.getFlag();
			resMessage = handler.getMessage();
		}

		String result = "{\"res\" : \"" + resCode + "\", \"msg\" : \"" + resMessage + "\"}";
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(result, responseHeaders, HttpStatus.CREATED);
	}

	/**
	 * 약관 마스터 삭제
	 * 
	 * @param model
	 * @param access_mst_ids
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/terms/deleteProc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> accessDeleteProc(Model model,
			@RequestParam(value = "access_mst_ids[]", required = false, defaultValue = "") String[] access_mst_ids,
			HttpServletRequest request) throws Exception {

		String resCode = "";
		String resMessage = "";
		String cookieID = CookieUtil.getCookieUserID(request);

		try {

			TermsAccessVo vo = new TermsAccessVo();
			vo.setDelete_yn("Y");
			vo.setMod_id(cookieID);

			for (int i = 0; i < access_mst_ids.length; i++) {
				if (StringUtils.isNotEmpty(access_mst_ids[i])) {
					vo.setAccess_mst_id(access_mst_ids[i]);
					service.deleteProc(vo);
				}
			}

			resCode = SmartUXProperties.getProperty("flag.success");
			resMessage = SmartUXProperties.getProperty("message.success");

		} catch (Exception e) {
			ExceptionHandler handler = new ExceptionHandler(e);
			resCode = handler.getFlag();
			resMessage = handler.getMessage();
		}

		String result = "{\"res\" : \"" + resCode + "\", \"msg\" : \"" + resMessage + "\"}";
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(result, responseHeaders, HttpStatus.CREATED);
	}
	
	/**
	 * 약관 항목 목록
	 * 
	 * @param model
	 * @param findName
	 * @param findValue
	 * @param pageNum
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/terms/getAccessInfoList", method = RequestMethod.GET)
	public String getAccessInfoList(Model model,
			@RequestParam(value = "findName", required = false, defaultValue = "") String findName,
			@RequestParam(value = "findValue", required = false, defaultValue = "") String findValue,
			@RequestParam(value = "pageNum", required = false, defaultValue = "1") String pageNum) throws Exception {

		findName = HTMLCleaner.clean(findName);
		pageNum = HTMLCleaner.clean(pageNum);

		TermsAccessInfoSearchVo vo = new TermsAccessInfoSearchVo();
		// 페이지
		vo.setPageSize(GlobalCom.isNumber(vo.getPageSize(), 10));
		vo.setBlockSize(GlobalCom.isNumber(vo.getBlockSize(), 10));
		vo.setPageNum(GlobalCom.isNumber(Integer.parseInt(GlobalCom.isNull(pageNum, "1")), 1));
		// 검색
		vo.setFindName(GlobalCom.isNull(findName, "ACCESS_TITLE"));
		vo.setFindValue(findValue);

		List<TermsAccessInfoVo> list = service.getAccessInfoList(vo);
		vo.setPageCount(service.getAccessInfoListCnt(vo));

		model.addAttribute("list", list);
		model.addAttribute("vo", vo);

		return "/admin/terms/getAccessInfoList";
	}
	
	/**
	 * 약관 항목 등록화면
	 * 
	 * @param model
	 * @param findName
	 * @param findValue
	 * @param pageNum
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/terms/insertAccessInfo", method = RequestMethod.GET)
	public String insertAccessInfo(Model model,
			@RequestParam(value = "findName", required = false, defaultValue = "") String findName,
			@RequestParam(value = "findValue", required = false, defaultValue = "") String findValue,
			@RequestParam(value = "pageNum", required = false, defaultValue = "1") String pageNum) throws Exception {

		findName = HTMLCleaner.clean(findName);
		pageNum = HTMLCleaner.clean(pageNum);

		TermsAccessInfoSearchVo vo = new TermsAccessInfoSearchVo();
		// 페이지
		vo.setPageSize(GlobalCom.isNumber(vo.getPageSize(), 10));
		vo.setBlockSize(GlobalCom.isNumber(vo.getBlockSize(), 10));
		vo.setPageNum(GlobalCom.isNumber(Integer.parseInt(GlobalCom.isNull(pageNum, "1")), 1));
		// 검색
		vo.setFindName(GlobalCom.isNull(findName, "ACCESS_TITLE"));
		vo.setFindValue(findValue);

		TermsAccessInfoVo accessInfoVo = new TermsAccessInfoVo();

		model.addAttribute("vo", vo);
		model.addAttribute("accessInfo", accessInfoVo);
		model.addAttribute("isUpdate", "0");

		return "/admin/terms/accessInfoForm";
	}
	
	/**
	 * 약관 항목 상세조회
	 * 
	 * @param model
	 * @param findName
	 * @param findValue
	 * @param display
	 * @param pageNum
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/terms/updateAccessInfo", method = RequestMethod.GET)
	public String getAccessInfoDetail(Model model,
			@RequestParam(value = "access_info_id", required = false, defaultValue = "") String access_info_id,
			@RequestParam(value = "findName", required = false, defaultValue = "") String findName,
			@RequestParam(value = "findValue", required = false, defaultValue = "") String findValue,
			@RequestParam(value = "pageNum", required = false, defaultValue = "1") String pageNum) throws Exception {

		findName = HTMLCleaner.clean(findName);
		pageNum = HTMLCleaner.clean(pageNum);
		access_info_id = HTMLCleaner.clean(access_info_id);

		TermsAccessInfoSearchVo vo = new TermsAccessInfoSearchVo();
		// 페이지
		vo.setPageSize(GlobalCom.isNumber(vo.getPageSize(), 10));
		vo.setBlockSize(GlobalCom.isNumber(vo.getBlockSize(), 10));
		vo.setPageNum(GlobalCom.isNumber(Integer.parseInt(GlobalCom.isNull(pageNum, "1")), 1));
		// 검색
		vo.setFindName(GlobalCom.isNull(findName, "ACCESS_TITLE"));
		vo.setFindValue(findValue);
		vo.setAccess_info_id(access_info_id);

		TermsAccessInfoVo accessVo = service.getAccessInfoDetail(vo);

		model.addAttribute("vo", vo);
		model.addAttribute("accessInfo", accessVo);
		model.addAttribute("isUpdate", "1");

		return "/admin/terms/accessInfoForm";
	}
	
	/**
	 * 약관 항목 등록
	 * 
	 * @param model
	 * @param access_info_id
	 * @param access_title
	 * @param required_yn
	 * @param access_version
	 * @param display_yn
	 * @param delete_yn
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/terms/insertProcInfo", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> accessInfoInsertProc(Model model,
			@RequestParam(value = "access_info_id", required = false, defaultValue = "") String access_info_id,
			@RequestParam(value = "access_title", required = false, defaultValue = "") String access_title,
			@RequestParam(value = "required_yn", required = false, defaultValue = "") String required_yn,
			@RequestParam(value = "access_version", required = false, defaultValue = "") String access_version,
			@RequestParam(value = "display_yn", required = false, defaultValue = "") String display_yn,
			@RequestParam(value = "delete_yn", required = false, defaultValue = "") String delete_yn,
			@RequestParam(value = "etc", required = false, defaultValue = "") String etc, HttpServletRequest request)
			throws Exception {

		String resCode = "";
		String resMessage = "";
		String cookieID = CookieUtil.getCookieUserID(request);

		try {

			TermsAccessInfoVo vo = new TermsAccessInfoVo();
			vo.setAccess_info_id(access_info_id);
			vo.setAccess_title(access_title);
			vo.setRequired_yn(required_yn);
			vo.setAccess_version(access_version);
			vo.setDisplay_yn(display_yn);
			vo.setDelete_yn("N");
			vo.setEtc(etc);
			vo.setReg_id(cookieID);
			vo.setMod_id(cookieID);

			TermsAccessInfoSearchVo srVO = new TermsAccessInfoSearchVo();
			srVO.setAccess_info_id(access_info_id);

			if (service.getAccessInfoCnt(srVO) > 0) {
				resCode = SmartUXProperties.getProperty("flag.key1");
				resMessage = "약관 항목 아이디가 이미 존재 합니다.";
			} else {
				service.insertProcInfo(vo);
				resCode = SmartUXProperties.getProperty("flag.success");
				resMessage = SmartUXProperties.getProperty("message.success");
			}

		} catch (Exception e) {
			ExceptionHandler handler = new ExceptionHandler(e);
			resCode = handler.getFlag();
			resMessage = handler.getMessage();
		}

		String result = "{\"res\" : \"" + resCode + "\", \"msg\" : \"" + resMessage + "\"}";
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(result, responseHeaders, HttpStatus.CREATED);
	}

	/**
	 * 약관 항목 수정
	 * 
	 * @param model
	 * @param access_info_id
	 * @param access_title
	 * @param required_yn
	 * @param access_version
	 * @param display_yn
	 * @param delete_yn
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/terms/updateProcInfo", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> accessUpdateProcInfo(Model model,
			@RequestParam(value = "access_info_id", required = false, defaultValue = "") String access_info_id,
			@RequestParam(value = "access_title", required = false, defaultValue = "") String access_title,
			@RequestParam(value = "required_yn", required = false, defaultValue = "") String required_yn,
			@RequestParam(value = "version_update_yn", required = false, defaultValue = "") String version_update_yn,
			@RequestParam(value = "access_version", required = false, defaultValue = "") String access_version,
			@RequestParam(value = "display_yn", required = false, defaultValue = "") String display_yn,
			@RequestParam(value = "delete_yn", required = false, defaultValue = "") String delete_yn,
			@RequestParam(value = "etc", required = false, defaultValue = "") String etc, HttpServletRequest request)
			throws Exception {

		String resCode = "";
		String resMessage = "";
		String cookieID = CookieUtil.getCookieUserID(request);

		try {

			TermsAccessInfoVo vo = new TermsAccessInfoVo();
			vo.setAccess_info_id(access_info_id);
			vo.setAccess_title(access_title);
			vo.setRequired_yn(required_yn);
			vo.setAccess_version(access_version);
			vo.setDisplay_yn(display_yn);
			vo.setEtc(etc);
			vo.setReg_id(cookieID);
			vo.setMod_id(cookieID);

			service.updateProcInfo(vo, version_update_yn);
			resCode = SmartUXProperties.getProperty("flag.success");
			resMessage = SmartUXProperties.getProperty("message.success");

		} catch (Exception e) {
			ExceptionHandler handler = new ExceptionHandler(e);
			resCode = handler.getFlag();
			resMessage = handler.getMessage();
		}

		String result = "{\"res\" : \"" + resCode + "\", \"msg\" : \"" + resMessage + "\"}";
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(result, responseHeaders, HttpStatus.CREATED);
	}

	/**
	 * 약관 항목 삭제
	 * 
	 * @param model
	 * @param access_mst_ids
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/terms/deleteProcInfo", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> accessDeleteProcInfo(Model model,
			@RequestParam(value = "access_info_ids[]", required = false, defaultValue = "") String[] access_info_ids,
			HttpServletRequest request) throws Exception {

		String resCode = "";
		String resMessage = "";
		String cookieID = CookieUtil.getCookieUserID(request);
		boolean isSuccess = true;
		boolean resultSuccess = true;
		int successCnt = 0;
		int totalCnt = 0;

		try {

			TermsAccessInfoVo vo = new TermsAccessInfoVo();
			vo.setDelete_yn("Y");
			vo.setMod_id(cookieID);

			for (int i = 0; i < access_info_ids.length; i++) {
				if (StringUtils.isNotEmpty(access_info_ids[i])) {
					vo.setAccess_info_id(access_info_ids[i]);
					isSuccess = service.deleteProcInfo(vo);
					totalCnt++;
					if (!isSuccess) {
						resultSuccess = false;
					} else {
						successCnt++;
					}
				}
			}

			if (resultSuccess) {
				resCode = SmartUXProperties.getProperty("flag.success");
				resMessage = SmartUXProperties.getProperty("message.success");
			} else {
				if (totalCnt == 1 || successCnt == 0) {
					resCode = SmartUXProperties.getProperty("flag.can_not_delete");
					resMessage = SmartUXProperties.getProperty("message.can_not_delete", "약관 마스터와 연결된 약관 항목");
				} else if (successCnt > 0) {
					resCode = SmartUXProperties.getProperty("flag.warn");
					resMessage = successCnt + "개의 " + SmartUXProperties.getProperty("message.warn");
				}
			}

		} catch (Exception e) {
			ExceptionHandler handler = new ExceptionHandler(e);
			resCode = handler.getFlag();
			resMessage = handler.getMessage();
		}

		String result = "{\"res\" : \"" + resCode + "\", \"msg\" : \"" + resMessage + "\"}";
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(result, responseHeaders, HttpStatus.CREATED);
	}
	
	/**
	 * 약관마스터 항목 목록 팝업
	 * 
	 * @param model
	 * @param findName
	 * @param findValue
	 * @param pageNum
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/terms/getAccessInfoListPop", method = RequestMethod.GET)
	public String getAccessInfoPopList(Model model,
			@RequestParam(value = "pageNum", required = false, defaultValue = "1") String pageNum,
			@RequestParam(value = "access_mst_id", required = false, defaultValue = "") String access_mst_id)
			throws Exception {

		TermsAccessGroupSearchVo vo = new TermsAccessGroupSearchVo();
		vo.setAccess_mst_id(access_mst_id);

		List<TermsAccessGroupVo> list = service.getAccessInfoPopList(vo);
		model.addAttribute("list", list);

		return "/admin/terms/getAccessInfoListPop";
	}
	
	/**
	 * 약관 항목 상세목록팝업
	 * 
	 * @param model
	 * @param findName
	 * @param findValue
	 * @param pageNum
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/terms/getAccessDetailListPop", method = RequestMethod.GET)
	public String getAccessDetailListPop(Model model,
			@RequestParam(value = "pageNum", required = false, defaultValue = "1") String pageNum,
			@RequestParam(value = "access_info_id", required = false, defaultValue = "") String access_info_id)
			throws Exception {

		TermsAccessDetailSearchVo vo = new TermsAccessDetailSearchVo();
		vo.setPageSize(GlobalCom.isNumber(vo.getPageSize(), 10));
		vo.setBlockSize(GlobalCom.isNumber(vo.getBlockSize(), 10));
		vo.setPageNum(GlobalCom.isNumber(Integer.parseInt(GlobalCom.isNull(pageNum, "1")), 1));
		vo.setAccess_info_id(access_info_id);

		TermsAccessInfoSearchVo srInfoVO = new TermsAccessInfoSearchVo();
		srInfoVO.setAccess_info_id(access_info_id);
		TermsAccessInfoVo accessInfoVO = service.getAccessInfoDetail(srInfoVO);

		List<TermsAccessDetailVo> list = service.getAccessDetailList(vo);
		vo.setPageCount(service.getAccessDetailListCnt(vo));
		model.addAttribute("list", list);
		model.addAttribute("vo", vo);
		model.addAttribute("access_title", accessInfoVO.getAccess_title());
		model.addAttribute("access_version", accessInfoVO.getAccess_version());

		return "/admin/terms/getAccessDetailListPop";
	}
	
	/**
	 * 약관 마스터 삭제
	 * 
	 * @param model
	 * @param access_mst_ids
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/terms/deleteProcDetail", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> accessDeleteProcDetail(Model model,
			@RequestParam(value = "access_detail_ids[]", required = false, defaultValue = "") String[] access_detail_ids,
			HttpServletRequest request) throws Exception {

		String resCode = SmartUXProperties.getProperty("flag.success");
		String resMessage = SmartUXProperties.getProperty("message.success");
		String cookieID = CookieUtil.getCookieUserID(request);
		boolean isDelete = true;

		try {

			TermsAccessDetailVo vo = new TermsAccessDetailVo();
			vo.setDelete_yn("Y");
			vo.setMod_id(cookieID);

			for (int i = 0; i < access_detail_ids.length; i++) {
				if (StringUtils.isNotEmpty(access_detail_ids[i])) {
					vo.setAccess_detail_id(access_detail_ids[i]);
					isDelete = service.deleteProcDetail(vo);

					if (!isDelete) {
						resCode = SmartUXProperties.getProperty("flag.etc");
						resMessage = "현재 버전의 항목 상세는 삭제 할수 없습니다.";
					}
				}
			}

		} catch (Exception e) {
			ExceptionHandler handler = new ExceptionHandler(e);
			resCode = handler.getFlag();
			resMessage = handler.getMessage();
		}

		String result = "{\"res\" : \"" + resCode + "\", \"msg\" : \"" + resMessage + "\"}";
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(result, responseHeaders, HttpStatus.CREATED);
	}
	
	/**
	 * 약관 상세 등록 팝업페이지
	 * 
	 * @param model
	 * @param findName
	 * @param findValue
	 * @param display
	 * @param pageNum
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/terms/insertAccessDetail", method = RequestMethod.GET)
	public String insertAccessDetail(Model model,
			@RequestParam(value = "access_info_id", required = false, defaultValue = "") String access_info_id,
			@RequestParam(value = "pageNum", required = false, defaultValue = "1") String pageNum) throws Exception {

		pageNum = HTMLCleaner.clean(pageNum);
		access_info_id = HTMLCleaner.clean(access_info_id);

		TermsAccessDetailSearchVo vo = new TermsAccessDetailSearchVo();
		// 페이지
		vo.setPageSize(GlobalCom.isNumber(vo.getPageSize(), 10));
		vo.setBlockSize(GlobalCom.isNumber(vo.getBlockSize(), 10));
		vo.setPageNum(GlobalCom.isNumber(Integer.parseInt(GlobalCom.isNull(pageNum, "1")), 1));
		// 검색

		TermsAccessDetailVo accessDetailVO = new TermsAccessDetailVo();
		accessDetailVO.setAccess_info_id(access_info_id);

		TermsAccessInfoSearchVo srInfoVO = new TermsAccessInfoSearchVo();
		srInfoVO.setAccess_info_id(access_info_id);
		TermsAccessInfoVo accessInfoVO = service.getAccessInfoDetail(srInfoVO);

		model.addAttribute("filePath", "");
		model.addAttribute("vo", vo);
		model.addAttribute("accessDetail", accessDetailVO);
		model.addAttribute("access_title", accessInfoVO.getAccess_title());
		model.addAttribute("access_version", accessInfoVO.getAccess_version());
		model.addAttribute("isUpdate", "0");

		return "/admin/terms/accessDetailForm";
	}
	
	/**
	 * 약관 상세 조회
	 * 
	 * @param model
	 * @param findName
	 * @param findValue
	 * @param display
	 * @param pageNum
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/terms/getAccessDetail", method = RequestMethod.GET)
	public String getAccessDetail(Model model,
			@RequestParam(value = "access_detail_id", required = false, defaultValue = "") String access_detail_id,
			@RequestParam(value = "pageNum", required = false, defaultValue = "1") String pageNum) throws Exception {

		pageNum = HTMLCleaner.clean(pageNum);
		access_detail_id = HTMLCleaner.clean(access_detail_id);

		TermsAccessDetailSearchVo vo = new TermsAccessDetailSearchVo();
		// 페이지
		vo.setPageSize(GlobalCom.isNumber(vo.getPageSize(), 10));
		vo.setBlockSize(GlobalCom.isNumber(vo.getBlockSize(), 10));
		vo.setPageNum(GlobalCom.isNumber(Integer.parseInt(GlobalCom.isNull(pageNum, "1")), 1));
		// 검색
		vo.setAccess_detail_id(access_detail_id);

		TermsAccessDetailVo accessDetailVO = service.getAccessDetail(vo);

		TermsAccessInfoSearchVo srInfoVO = new TermsAccessInfoSearchVo();
		srInfoVO.setAccess_info_id(accessDetailVO.getAccess_info_id());
		TermsAccessInfoVo accessInfoVO = service.getAccessInfoDetail(srInfoVO);

		StringBuilder filePath = new StringBuilder();
		filePath.append(SmartUXProperties.getProperty("smartux.domain.http")).append("/");
		filePath.append("smartux_adm/admin/download?");
		filePath.append("path=").append("videolte/access/").append(accessDetailVO.getSave_file_nm());
		filePath.append("&system=").append("mims");
		
		model.addAttribute("filePath", filePath.toString());
		model.addAttribute("vo", vo);
		model.addAttribute("accessDetail", accessDetailVO);
		model.addAttribute("access_title", accessInfoVO.getAccess_title());
		model.addAttribute("access_version", accessInfoVO.getAccess_version());
		model.addAttribute("isUpdate", "1");

		return "/admin/terms/accessDetailForm";
	}
	
	/**
	 * 약관 항목 상세 수정
	 * 
	 * @param model
	 * @param access_mst_ids
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/terms/updateProcDetail", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> accessUpdateProcDetail(Model model,
			@RequestParam(value = "access_detail_id", required = false, defaultValue = "") String access_detail_id,
			@RequestParam(value = "access_url", required = false, defaultValue = "") String access_url,
			@RequestParam(value = "text_file", required = false, defaultValue = "") MultipartFile text_file,
			@RequestParam(value = "display_yn", required = false, defaultValue = "") String display_yn,
			@RequestParam(value = "org_file_nm", required = false, defaultValue = "") String org_file_nm,
			@RequestParam(value = "save_file_nm", required = false, defaultValue = "") String save_file_nm,
			HttpServletRequest request) throws Exception {

		String resCode = "";
		String resMessage = "";
		String cookieID = CookieUtil.getCookieUserID(request);

		access_detail_id = HTMLCleaner.clean(access_detail_id);
		access_url = HTMLCleaner.clean(access_url);
		display_yn = HTMLCleaner.clean(display_yn);

		try {

			String filePath = SmartUXProperties.getProperty("videolte.access.dir");

			TermsAccessDetailVo vo = new TermsAccessDetailVo();
			vo.setAccess_detail_id(access_detail_id);
			vo.setAccess_url(access_url);
			vo.setDisplay_yn(display_yn);
			vo.setMod_id(cookieID);

			if (text_file.getSize() != 0L) {
				checkFileSize(text_file);
				
				String org_name = text_file.getOriginalFilename();
				String ext = org_name.substring(org_name.lastIndexOf(".") + 1, org_name.length()); // 확장자
																									// 구하기
				ext = ext.toLowerCase();

				String save_file_name = Long.toString(System.currentTimeMillis()) + "." + ext; // 시스템타임.확장자
																								// 구조로
																								// 한다
				String newFileName = filePath + save_file_name;				

				File dir = new File(filePath);
				if (!dir.isDirectory()) {
					dir.mkdirs();
				}

				File file = new File(newFileName);
				text_file.transferTo(file);

				vo.setOrg_file_nm(org_name);
				vo.setSave_file_nm(save_file_name);
			} else {
				vo.setOrg_file_nm(org_file_nm);
				vo.setSave_file_nm(save_file_nm);
			}

			service.updateProcDetail(vo);

			resCode = SmartUXProperties.getProperty("flag.success");
			resMessage = SmartUXProperties.getProperty("message.success");

		} catch (Exception e) {
			ExceptionHandler handler = new ExceptionHandler(e);
			resCode = handler.getFlag();
			resMessage = handler.getMessage();
		}

		String result = "{\"flag\" : \"" + resCode + "\", \"message\" : \"" + resMessage + "\"}";

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(result, responseHeaders, HttpStatus.CREATED);
	}

	/**
	 * 약관 항목 상세 등록
	 * 
	 * @param model
	 * @param access_mst_ids
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/terms/insertProcDetail", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> insertProcDetail(Model model,
			@RequestParam(value = "access_info_id", required = false, defaultValue = "") String access_info_id,
			@RequestParam(value = "access_url", required = false, defaultValue = "") String access_url,
			@RequestParam(value = "text_file", required = false, defaultValue = "") MultipartFile text_file,
			@RequestParam(value = "org_file_nm", required = false, defaultValue = "") String org_file_nm,
			@RequestParam(value = "save_file_nm", required = false, defaultValue = "") String save_file_nm,
			HttpServletRequest request) throws Exception {

		String resCode = "";
		String resMessage = "";
		String cookieID = CookieUtil.getCookieUserID(request);

		access_info_id = HTMLCleaner.clean(access_info_id);
		access_url = HTMLCleaner.clean(access_url);

		try {

			String filePath = SmartUXProperties.getProperty("videolte.access.dir");

			TermsAccessDetailVo vo = new TermsAccessDetailVo();
			vo.setAccess_info_id(access_info_id);
			vo.setAccess_url(access_url);
			vo.setDisplay_yn("Y");
			vo.setReg_id(cookieID);
			vo.setMod_id(cookieID);

			if (text_file.getSize() != 0L) {
				checkFileSize(text_file);
				
				String org_name = text_file.getOriginalFilename();
				String ext = org_name.substring(org_name.lastIndexOf(".") + 1, org_name.length()); // 확장자
																									// 구하기
				ext = ext.toLowerCase();

				String save_file_name = Long.toString(System.currentTimeMillis()) + "." + ext; // 시스템타임.확장자
																								// 구조로
																								// 한다
				String newFileName = filePath + save_file_name;

				File dir = new File(filePath);
				if (!dir.isDirectory()) {
					dir.mkdirs();
				}

				File file = new File(newFileName);
				text_file.transferTo(file);

				vo.setOrg_file_nm(org_name);
				vo.setSave_file_nm(save_file_name);
			} else {
				vo.setOrg_file_nm(org_file_nm);
				vo.setSave_file_nm(save_file_nm);
			}

			service.insertProcDetail(vo);

			resCode = SmartUXProperties.getProperty("flag.success");
			resMessage = SmartUXProperties.getProperty("message.success");

		} catch (Exception e) {
			ExceptionHandler handler = new ExceptionHandler(e);
			resCode = handler.getFlag();
			resMessage = handler.getMessage();
		}

		String result = "{\"flag\" : \"" + resCode + "\", \"message\" : \"" + resMessage + "\"}";

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(result, responseHeaders, HttpStatus.CREATED);
	}
	
	private void checkFileSize(MultipartFile text_file) {
		String fileMaxSize = SmartUXProperties.getProperty("access.detail.file.size");
		if(null != fileMaxSize) {
			long iFileMaxSize = Long.parseLong(fileMaxSize);
			if(text_file.getSize() > iFileMaxSize) throw new SmartUXException(SmartUXProperties.getProperty("flag.file.maxsize.over")
					, SmartUXProperties.getProperty("message.file.maxsize.over",fileMaxSize + "Byte 미만"));
		}
	}

}
