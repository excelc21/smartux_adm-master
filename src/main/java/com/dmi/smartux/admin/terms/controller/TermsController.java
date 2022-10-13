package com.dmi.smartux.admin.terms.controller;

import java.io.File;
import java.util.List;

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
import org.springframework.web.multipart.MultipartFile;

import com.dmi.commons.consts.ResultCode;
import com.dmi.commons.exception.MimsCommonException;
import com.dmi.commons.factory.FileProcessFunction;
import com.dmi.commons.factory.FileProcessLockFactory;
import com.dmi.commons.logger.CLog;
import com.dmi.commons.model.RestfulCodeMsg;
import com.dmi.commons.utility.HttpClientUtils;
import com.dmi.commons.utility.RestfulUtils;
import com.dmi.smartux.admin.terms.service.TermsService;
import com.dmi.smartux.admin.terms.vo.TermsListVo;
import com.dmi.smartux.admin.terms.vo.TermsVo;
import com.dmi.smartux.common.exception.ExceptionHandler;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.CookieUtil;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.util.HTMLCleaner;
import com.dmi.smartux.common.vo.CUDResult;

import net.sf.json.JSONSerializer;

@Controller
public class TermsController {
	private final Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	TermsService service;

	private final Log applyLogger = LogFactory.getLog("adminApplyCacheHistroy");

	/**
	 * 추천이용동의 약관관리 즉시적용
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/terms/termsApplyCache", method=RequestMethod.POST)
	public ResponseEntity<String> termsApplyCache(
			@RequestParam(value="callByScheduler",  defaultValue="Y") final  String callByScheduler,
			HttpServletRequest request,
			HttpServletResponse response
			) throws Exception {
		
		final CLog cLog = new CLog(applyLogger, request.getRemoteAddr(), request.getRequestURI(), request.getMethod());
		final String loginUser = callByScheduler.equals("Y") ? "batch" : CookieUtil.getCookieUserID(request);
		final CUDResult result = new CUDResult();
		
		try {
			cLog.startLog("추천이용동의 약관관리 즉시적용 : ",  loginUser);
			final String host = SmartUXProperties.getProperty("cache.sync.public.host");
			final String port = SmartUXProperties.getProperty("cache.sync.public.port");
			
			String fileLockPath = SmartUXProperties.getProperty("AhomeTermsDao.refreshAhomeTerms.fileLock");
			int waitTime = NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.wait"), 5);
			
			FileProcessLockFactory.start(fileLockPath, waitTime,  new FileProcessFunction("추천이용동의 약관관리 즉시적용", ResultCode.ApplyRequestFail) {
				
				@Override
				public void run() throws Exception {
					
					//즉시적용 호출 시 sleep 값 추가 -> DB분리건으로 OGG가 10초 이상 걸리는 경우가 있어 즉시적용 시 반영이 안돼서 추가함
					Thread.sleep(Integer.parseInt(org.apache.commons.lang.StringUtils.defaultIfEmpty(SmartUXProperties.getProperty("apply.cache.call.sleep"), "10000")));
					
					RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
							host, 
							port, 
							SmartUXProperties.getProperty("AhomeTermsDao.refreshAhomeTerms.url")+"?callByScheduler="+callByScheduler,
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.method"), "POST"), 
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"), 
							MediaType.APPLICATION_JSON.toString(),
							NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.timeout"), 60000)).getResponseBody());
					
					result.setFlag(codeMsg.getCode());
					result.setMessage(codeMsg.getMessage());
					cLog.middleLog("추천이용동의 약관관리 즉시적용", "API 호출", codeMsg.getCode(), codeMsg.getMessage());
				}
			});
		} catch (MimsCommonException e) {
			cLog.warnLog("추천이용동의 약관관리 즉시적용", "실패", e.getFlag(), e.getMessage(), e.getLocalizedMessage());
			result.setFlag(e.getFlag());
			result.setMessage(e.getMessage());
		} finally {
			cLog.endLog("추천이용동의 약관관리 즉시적용", loginUser, result.getFlag(), result.getMessage());
		}

		String resultMsg = "{\"flag\" : \"" + result.getFlag() + "\", \"message\" : \"" + result.getMessage() + "\"}";
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(resultMsg).toString(), responseHeaders, HttpStatus.OK);
		
	}
	
	@RequestMapping(value="/admin/terms/getTermsList",method=RequestMethod.GET)
	public String getTermsList(
			@RequestParam(value="findName", required=false, defaultValue="") String findName,
			@RequestParam(value="findValue", required=false, defaultValue="") String findValue,
			@RequestParam(value="pageNum", required=false, defaultValue="1") String pageNum,
			@RequestParam(value="s_service_type", required=false, defaultValue="") String s_service_type,
			@RequestParam(value="s_service_gbn", required=false, defaultValue="") String s_service_gbn,
			@RequestParam(value="s_terms_gbn", required=false, defaultValue="") String s_terms_gbn,
			@RequestParam(value="s_display_yn", required=false, defaultValue="") String s_display_yn,
			Model model
			) throws Exception{	
		
		HTMLCleaner cleaner = new HTMLCleaner();
		findName 	= cleaner.clean(findName);
		findValue = cleaner.clean(findValue);
		pageNum = cleaner.clean(pageNum);
		s_service_type = cleaner.clean(s_service_type);
		s_service_gbn = cleaner.clean(s_service_gbn);
		s_terms_gbn = cleaner.clean(s_terms_gbn);
		s_display_yn = cleaner.clean(s_display_yn);
		
		TermsListVo termslistVo = new TermsListVo();
		termslistVo.setPageSize(GlobalCom.isNumber(termslistVo.getPageSize(),10));
		termslistVo.setBlockSize(GlobalCom.isNumber(termslistVo.getBlockSize(),10));
		termslistVo.setPageNum(GlobalCom.isNumber(Integer.parseInt(GlobalCom.isNull(pageNum, "1")),1));
		//검색 
		termslistVo.setFindName(GlobalCom.isNull(findName, "title"));
		termslistVo.setFindValue(findValue);
		termslistVo.setS_service_type(s_service_type);
		termslistVo.setS_service_gbn(s_service_gbn);
		termslistVo.setS_terms_gbn(s_terms_gbn);
		termslistVo.setS_display_yn(s_display_yn);
		
		List<TermsVo> list = service.getTermsList(termslistVo);
		termslistVo.setPageCount(service.getTermsListCnt(termslistVo));
		
		model.addAttribute("list", list);//리스트
		model.addAttribute("vo", termslistVo);//파람
		
		return "/admin/terms/getTermsList";		
	}
	
	@RequestMapping(value="/admin/terms/insertTerms",method=RequestMethod.GET)
	public String insertTerms(
			@RequestParam(value="findName", required=false, defaultValue="") String findName,
			@RequestParam(value="findValue", required=false, defaultValue="") String findValue,
			@RequestParam(value="pageNum", required=false, defaultValue="1") String pageNum,
			@RequestParam(value="s_service_type", required=false, defaultValue="") String s_service_type,
			@RequestParam(value="s_service_gbn", required=false, defaultValue="") String s_service_gbn,
			@RequestParam(value="s_terms_gbn", required=false, defaultValue="") String s_terms_gbn,
			@RequestParam(value="s_display_yn", required=false, defaultValue="") String s_display_yn,
			Model model
			) throws Exception{	
		
		HTMLCleaner cleaner = new HTMLCleaner();
		findName 	= cleaner.clean(findName);
		findValue = cleaner.clean(findValue);
		pageNum = cleaner.clean(pageNum);
		s_service_type = cleaner.clean(s_service_type);
		s_service_gbn = cleaner.clean(s_service_gbn);
		s_terms_gbn = cleaner.clean(s_terms_gbn);
		s_display_yn = cleaner.clean(s_display_yn);
		
		TermsListVo termslistVo= new TermsListVo();
		termslistVo.setFindName(findName);
		termslistVo.setFindValue(findValue);
		termslistVo.setPageNum(Integer.parseInt(pageNum));
		termslistVo.setS_service_type(s_service_type);
		termslistVo.setS_service_gbn(s_service_gbn);
		termslistVo.setS_terms_gbn(s_terms_gbn);
		termslistVo.setS_display_yn(s_display_yn);

		model.addAttribute("vo", termslistVo);//파람
		
		return "/admin/terms/insertTerms";		
	}
	
	/*
	@RequestMapping(value="/admin/terms/insertTermsProc",method=RequestMethod.POST)
	public String insertTermsProc(
			TermsVo termsVo,
			@RequestParam(value="s_service_type", required=false, defaultValue="") String s_service_type,
			@RequestParam(value="s_service_gbn", required=false, defaultValue="") String s_service_gbn,
			@RequestParam(value="s_terms_gbn", required=false, defaultValue="") String s_terms_gbn,
			@RequestParam(value="s_display_yn", required=false, defaultValue="") String s_display_yn,
			HttpServletRequest request,
			Model model
			) throws Exception{
		
		HTMLCleaner cleaner = new HTMLCleaner();
		s_service_type = cleaner.clean(s_service_type);
		s_service_gbn = cleaner.clean(s_service_gbn);
		s_terms_gbn = cleaner.clean(s_terms_gbn);
		s_display_yn = cleaner.clean(s_display_yn);
		
		String cookieID = CookieUtil.getCookieUserID(request);
		
		termsVo.setReg_id(cookieID);
		termsVo.setMod_id(cookieID);
				
		termsVo.setAct_id(cookieID);
		termsVo.setAct_gbn("I");
		termsVo.setAct_ip(request.getRemoteAddr());
		
		service.insertTermsProc(termsVo);
		
		String domain = GlobalCom.getProperties(SmartUXProperties.getProperty("filepath.hdtv.common"), "hdtv.domain.http");
		
		model.addAttribute("s_service_type",s_service_type);
		model.addAttribute("s_service_gbn",s_service_gbn);
		model.addAttribute("s_terms_gbn",s_terms_gbn);
		model.addAttribute("s_display_yn",s_display_yn);
		
		return "redirect:"+domain+"/smartux_adm/admin/terms/getTermsList.do";
	}
	*/
	
	@RequestMapping(value="/admin/terms/insertTermsProc",method=RequestMethod.POST)
	public ResponseEntity<String> insertTermsProc(
			TermsVo termsVo,
			@RequestParam(value="imageFile", required=false, defaultValue="") MultipartFile imageFile,
			@RequestParam(value="s_service_type", required=false, defaultValue="") String s_service_type,
			@RequestParam(value="s_service_gbn", required=false, defaultValue="") String s_service_gbn,
			@RequestParam(value="s_terms_gbn", required=false, defaultValue="") String s_terms_gbn,
			@RequestParam(value="s_display_yn", required=false, defaultValue="") String s_display_yn,
			HttpServletRequest request,
			Model model
			) throws Exception{
		
		String result = "";
		String resultcode = "";
		String resultmessage = "";
		
		HTMLCleaner cleaner = new HTMLCleaner();
		s_service_type = cleaner.clean(s_service_type);
		s_service_gbn = cleaner.clean(s_service_gbn);
		s_terms_gbn = cleaner.clean(s_terms_gbn);
		s_display_yn = cleaner.clean(s_display_yn);
		
		String tmp_file_name = "";
		String bg_img_file = "";
		File img_file = null;
		
		if (imageFile.getSize() != 0L) {
			tmp_file_name = imageFile.getOriginalFilename();
			String ext = tmp_file_name.substring(	tmp_file_name.lastIndexOf(".") + 1,	tmp_file_name.length()); // 확장자 구하기
			ext = ext.toLowerCase();
			bg_img_file = GlobalCom.isNull(SmartUXProperties.getPathProperty("terms.img.fileName"), "UX_TERMS_GUIDE_") + Long.toString(System.currentTimeMillis()) + "."+ ext; // 시스템타임.확장자 구조로 한다
			String newFilename=GlobalCom.isNull(SmartUXProperties.getPathProperty("imgupload.dir"), "/NAS_DATA/web/smartux/") + GlobalCom.isNull(SmartUXProperties.getPathProperty("imgupload.terms.folder"), "terms/img/")+ bg_img_file;
			
			img_file = new File(newFilename);
			if(!img_file.isDirectory()){
				img_file.mkdirs();
			}
			imageFile.transferTo(img_file);
			termsVo.setTerms_img_name(bg_img_file);
		}
		
		String cookieID = CookieUtil.getCookieUserID(request);
		
		termsVo.setReg_id(cookieID);
		termsVo.setMod_id(cookieID);
				
		termsVo.setAct_id(cookieID);
		termsVo.setAct_gbn("I");
		termsVo.setAct_ip(request.getRemoteAddr());
		
		try{
			
			TermsVo termsVoRe = new TermsVo();
			termsVoRe =	service.getTermsView(termsVo);
			
			if(termsVoRe == null){
				service.insertTermsProc(termsVo);
				resultcode = SmartUXProperties.getProperty("flag.success");
				resultmessage = SmartUXProperties.getProperty("message.success");
			}else{
				resultcode = "1000";
				resultmessage = "중복 된 데이터가 있습니다";
			}
		} catch (Exception e) {
			logger.error("[insertTermsProc]["+e.getClass().getName()+"]["+e.getMessage()+"]");		
			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}
		
		model.addAttribute("s_service_type",s_service_type);
		model.addAttribute("s_service_gbn",s_service_gbn);
		model.addAttribute("s_terms_gbn",s_terms_gbn);
		model.addAttribute("s_display_yn",s_display_yn);
		
		result = "{\"result\":{\"message\":\""+resultmessage+"\",\"flag\":\""+resultcode+"\"}}";
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(result).toString(), responseHeaders, HttpStatus.CREATED);
	}

	
	@RequestMapping(value="/admin/terms/deleteTermsProc", method=RequestMethod.POST )
	public ResponseEntity<String> deleteTermsProc(
			@RequestParam(value="terms_id", required=false, defaultValue="") String terms_id,
			HttpServletRequest request,
			Model model
			) throws Exception{
			
			String result = "";
			String resultcode = "";
			String resultmessage = "";
			
			String cookieID = CookieUtil.getCookieUserID(request);
			String userIp = request.getRemoteAddr();
			
			try{
				service.deleteTermsProc(terms_id,cookieID,userIp);
				resultcode = SmartUXProperties.getProperty("flag.success");
				resultmessage = SmartUXProperties.getProperty("message.success");
			} catch (Exception e) {
				logger.error("[deleteTermsProc]["+e.getClass().getName()+"]["+e.getMessage()+"]");		
				
				ExceptionHandler handler = new ExceptionHandler(e);
				resultcode = handler.getFlag();
				resultmessage = handler.getMessage();
			}
			
			result = "{\"result\":{\"message\":\""+resultmessage+"\",\"flag\":\""+resultcode+"\"}}";
			
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
			return new ResponseEntity<String>(JSONSerializer.toJSON(result).toString(), responseHeaders, HttpStatus.CREATED);
	}
	
	@RequestMapping(value="/admin/terms/updatetTerms",method=RequestMethod.GET)
	public String updateTheme(
			@RequestParam(value="service_type", required=false, defaultValue="") String service_type,
			@RequestParam(value="service_gbn", required=false, defaultValue="") String service_gbn,
			@RequestParam(value="terms_gbn", required=false, defaultValue="") String terms_gbn,
			@RequestParam(value="findName", required=false, defaultValue="") String findName,
			@RequestParam(value="findValue", required=false, defaultValue="") String findValue,
			@RequestParam(value="pageNum", required=false, defaultValue="1") String pageNum,
			@RequestParam(value="s_service_type", required=false, defaultValue="") String s_service_type,
			@RequestParam(value="s_service_gbn", required=false, defaultValue="") String s_service_gbn,
			@RequestParam(value="s_terms_gbn", required=false, defaultValue="") String s_terms_gbn,
			@RequestParam(value="s_display_yn", required=false, defaultValue="") String s_display_yn,
			Model model
			) throws Exception{	
		
		HTMLCleaner cleaner = new HTMLCleaner();
		service_type 	= cleaner.clean(service_type);
		service_gbn 	= cleaner.clean(service_gbn);
		terms_gbn 	= cleaner.clean(terms_gbn);
		findName 	= cleaner.clean(findName);
		findValue = cleaner.clean(findValue);
		pageNum = cleaner.clean(pageNum);	
		s_service_type = cleaner.clean(s_service_type);
		s_service_gbn = cleaner.clean(s_service_gbn);
		s_terms_gbn = cleaner.clean(s_terms_gbn);
		s_display_yn = cleaner.clean(s_display_yn);
		
		TermsListVo termslistVo= new TermsListVo();
		termslistVo.setFindName(findName);
		termslistVo.setFindValue(findValue);
		termslistVo.setPageNum(Integer.parseInt(pageNum));
		termslistVo.setS_service_type(s_service_type);
		termslistVo.setS_service_gbn(s_service_gbn);
		termslistVo.setS_terms_gbn(s_terms_gbn);
		termslistVo.setS_display_yn(s_display_yn);

		TermsVo termsVo = new TermsVo();
		termsVo.setService_type(service_type);
		termsVo.setService_gbn(service_gbn);
		termsVo.setTerms_gbn(terms_gbn);
				
		termsVo =	service.getTermsView(termsVo);

		model.addAttribute("vo", termslistVo);//파람
		model.addAttribute("viewVo", termsVo);

		return "/admin/terms/updateTerms";
	}
	/*
	@RequestMapping(value="/admin/terms/updateTermsProc",method=RequestMethod.POST)
	public String updateTermsProc(
			TermsVo termsVo,
			@RequestParam(value="imageFile", required=false, defaultValue="") MultipartFile imageFile,
			@RequestParam(value="findName", required=false, defaultValue="") String findName,
			@RequestParam(value="findValue", required=false, defaultValue="") String findValue,
			@RequestParam(value="pageNum", required=false, defaultValue="1") String pageNum,
			@RequestParam(value="s_service_type", required=false, defaultValue="") String s_service_type,
			@RequestParam(value="s_service_gbn", required=false, defaultValue="") String s_service_gbn,
			@RequestParam(value="s_terms_gbn", required=false, defaultValue="") String s_terms_gbn,
			@RequestParam(value="s_display_yn", required=false, defaultValue="") String s_display_yn,
			HttpServletRequest request,
			Model model
			) throws Exception{
		
		HTMLCleaner cleaner = new HTMLCleaner();
		findName 	= cleaner.clean(findName);
		findValue = cleaner.clean(findValue);
		pageNum = cleaner.clean(pageNum);	
		s_service_type = cleaner.clean(s_service_type);
		s_service_gbn = cleaner.clean(s_service_gbn);
		s_terms_gbn = cleaner.clean(s_terms_gbn);
		s_display_yn = cleaner.clean(s_display_yn);
		
		String tmp_file_name = "";
		String bg_img_file = "";
		File img_file = null;
		
		if (imageFile.getSize() != 0L) {
			tmp_file_name = imageFile.getOriginalFilename();
			String ext = tmp_file_name.substring(	tmp_file_name.lastIndexOf(".") + 1,	tmp_file_name.length()); // 확장자 구하기
			ext = ext.toLowerCase();
			bg_img_file = GlobalCom.isNull(SmartUXProperties.getPathProperty("terms.img.fileName"), "UX_TERMS_GUIDE_") + Long.toString(System.currentTimeMillis()) + "."+ ext; // 시스템타임.확장자 구조로 한다
			String newFilename=GlobalCom.isNull(SmartUXProperties.getPathProperty("imgupload.dir"), "/NAS_DATA/web/smartux/") + GlobalCom.isNull(SmartUXProperties.getPathProperty("imgupload.terms.folder"), "terms/img/")+ bg_img_file;
			
			img_file = new File(newFilename);
			if(!img_file.isDirectory()){
				img_file.mkdirs();
			}
			imageFile.transferTo(img_file);
			termsVo.setTerms_img_name(bg_img_file);
		}
		
		String cookieID = CookieUtil.getCookieUserID(request);
		
		termsVo.setMod_id(cookieID);

		termsVo.setAct_id(cookieID);
		termsVo.setAct_gbn("U");
		termsVo.setAct_ip(request.getRemoteAddr());
		
		service.updateTermsProc(termsVo);
		
		String domain = GlobalCom.getProperties(SmartUXProperties.getProperty("filepath.hdtv.common"), "hdtv.domain.http");
		
		model.addAttribute("s_service_type",s_service_type);
		model.addAttribute("s_service_gbn",s_service_gbn);
		model.addAttribute("s_terms_gbn",s_terms_gbn);
		model.addAttribute("s_display_yn",s_display_yn);
		model.addAttribute("findName",findName );
		model.addAttribute("findValue",findValue );
		model.addAttribute("pageNum",pageNum );
		
		return "redirect:"+domain+"/smartux_adm/admin/terms/getTermsList.do";
	}
	*/
	

	@RequestMapping(value="/admin/terms/updateTermsProc",method=RequestMethod.POST)
	public ResponseEntity<String> updateTermsProc(
			TermsVo termsVo,
			@RequestParam(value="imageFile", required=false, defaultValue="") MultipartFile imageFile,
			@RequestParam(value="findName", required=false, defaultValue="") String findName,
			@RequestParam(value="findValue", required=false, defaultValue="") String findValue,
			@RequestParam(value="pageNum", required=false, defaultValue="1") String pageNum,
			@RequestParam(value="s_service_type", required=false, defaultValue="") String s_service_type,
			@RequestParam(value="s_service_gbn", required=false, defaultValue="") String s_service_gbn,
			@RequestParam(value="s_terms_gbn", required=false, defaultValue="") String s_terms_gbn,
			@RequestParam(value="s_display_yn", required=false, defaultValue="") String s_display_yn,
			HttpServletRequest request,
			Model model
			) throws Exception{
		
		String result = "";
		String resultcode = "";
		String resultmessage = "";
		
		HTMLCleaner cleaner = new HTMLCleaner();
		findName 	= cleaner.clean(findName);
		findValue = cleaner.clean(findValue);
		pageNum = cleaner.clean(pageNum);	
		s_service_type = cleaner.clean(s_service_type);
		s_service_gbn = cleaner.clean(s_service_gbn);
		s_terms_gbn = cleaner.clean(s_terms_gbn);
		s_display_yn = cleaner.clean(s_display_yn);
		
		String tmp_file_name = "";
		String bg_img_file = "";
		File img_file = null;
		
		if (imageFile.getSize() != 0L) {
			tmp_file_name = imageFile.getOriginalFilename();
			String ext = tmp_file_name.substring(	tmp_file_name.lastIndexOf(".") + 1,	tmp_file_name.length()); // 확장자 구하기
			ext = ext.toLowerCase();
			bg_img_file = GlobalCom.isNull(SmartUXProperties.getPathProperty("terms.img.fileName"), "UX_TERMS_GUIDE_") + Long.toString(System.currentTimeMillis()) + "."+ ext; // 시스템타임.확장자 구조로 한다
			String newFilename=GlobalCom.isNull(SmartUXProperties.getPathProperty("imgupload.dir"), "/NAS_DATA/web/smartux/") + GlobalCom.isNull(SmartUXProperties.getPathProperty("imgupload.terms.folder"), "terms/img/")+ bg_img_file;
			
			img_file = new File(newFilename);
			if(!img_file.isDirectory()){
				img_file.mkdirs();
			}
			imageFile.transferTo(img_file);
			termsVo.setTerms_img_name(bg_img_file);
		}
		
		String cookieID = CookieUtil.getCookieUserID(request);
		
		termsVo.setMod_id(cookieID);

		termsVo.setAct_id(cookieID);
		termsVo.setAct_gbn("U");
		termsVo.setAct_ip(request.getRemoteAddr());
		
		try{
			
			TermsVo termsVoRe = new TermsVo();
			termsVoRe =	service.getTermsView(termsVo);
			
			if(termsVoRe == null){
				resultcode = "1000";
				resultmessage = "중복 된 데이터가 있습니다";
			}else{
				service.updateTermsProc(termsVo);
				resultcode = SmartUXProperties.getProperty("flag.success");
				resultmessage = SmartUXProperties.getProperty("message.success");
			}
		} catch (Exception e) {
			logger.error("[insertTermsProc]["+e.getClass().getName()+"]["+e.getMessage()+"]");		
			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}
		
		model.addAttribute("s_service_type",s_service_type);
		model.addAttribute("s_service_gbn",s_service_gbn);
		model.addAttribute("s_terms_gbn",s_terms_gbn);
		model.addAttribute("s_display_yn",s_display_yn);
		model.addAttribute("findName",findName );
		model.addAttribute("findValue",findValue );
		model.addAttribute("pageNum",pageNum );
		
		result = "{\"result\":{\"message\":\""+resultmessage+"\",\"flag\":\""+resultcode+"\"}}";
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(result).toString(), responseHeaders, HttpStatus.CREATED);
		
	}
}
