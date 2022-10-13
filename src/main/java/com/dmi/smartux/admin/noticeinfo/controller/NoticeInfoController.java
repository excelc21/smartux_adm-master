package com.dmi.smartux.admin.noticeinfo.controller;

import java.io.File;
import java.util.HashMap;
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
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.dmi.commons.consts.ResultCode;
import com.dmi.commons.exception.MimsCommonException;
import com.dmi.commons.factory.FileProcessFunction;
import com.dmi.commons.factory.FileProcessLockFactory;
import com.dmi.commons.logger.CLog;
import com.dmi.commons.model.RestfulCodeMsg;
import com.dmi.commons.utility.HttpClientUtils;
import com.dmi.commons.utility.RestfulUtils;
import com.dmi.smartux.admin.noticeinfo.service.NoticeInfoService;
import com.dmi.smartux.admin.noticeinfo.vo.InsertNoticeInfoVo;
import com.dmi.smartux.admin.noticeinfo.vo.NotiCodeListVo;
import com.dmi.smartux.admin.noticeinfo.vo.NotiMainCodeListVo;
import com.dmi.smartux.admin.noticeinfo.vo.NoticeInfoListProcVo;
import com.dmi.smartux.admin.noticeinfo.vo.NoticeInfoListVo;
import com.dmi.smartux.admin.noticeinfo.vo.UpdateNoticeInfoVo;
import com.dmi.smartux.admin.noticeinfo.vo.ViewNoticeInfoListVo;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.CookieUtil;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.util.HTMLCleaner;
import com.dmi.smartux.common.vo.CUDResult;
import com.dmi.smartux.noticeinfo.vo.CacheNoticeInfoListVo;

import net.sf.json.JSONSerializer;

@Controller("adminNoticeInfoController")
public class NoticeInfoController {
	
	@Autowired
	NoticeInfoService service;
	
	@Autowired
	com.dmi.smartux.noticeinfo.service.NoticeInfoService api_service;
	
	private final Log logger = LogFactory.getLog(this.getClass());
	
	private final Log applyLogger = LogFactory.getLog("adminApplyCacheHistroy");
	
	/**
	 * 공지/이미지 정보관리 즉시적용
	 * @param request
	 * @param response
	 * @param login_id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/noticeinfo/cacheNoticeInfo", method=RequestMethod.POST)
	public ResponseEntity<String> cacheNoticeInfo(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value="callByScheduler",  defaultValue="Y") final  String callByScheduler
			) throws Exception{

		final CLog cLog = new CLog(applyLogger, request.getRemoteAddr(), request.getRequestURI(), request.getMethod());
		final String loginUser = callByScheduler.equals("Y") ? "batch" : CookieUtil.getCookieUserID(request);
		final CUDResult result = new CUDResult();
		
		try {
			cLog.startLog("공지/이미지 정보관리 즉시적용",  loginUser);
			final String host = SmartUXProperties.getProperty("cache.sync.public.host");
			final String port = SmartUXProperties.getProperty("cache.sync.public.port");
			
			String fileLockPath = SmartUXProperties.getProperty("NoticeInfoDao.refreshNoticeInfo.fileLock");
			int waitTime = NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.wait"), 5);
			
			FileProcessLockFactory.start(fileLockPath, waitTime,  new FileProcessFunction("공지/이미지 정보관리 즉시적용", ResultCode.ApplyRequestFail) {
				
				@Override
				public void run() throws Exception {
					
					//즉시적용 호출 시 sleep 값 추가 -> DB분리건으로 OGG가 10초 이상 걸리는 경우가 있어 즉시적용 시 반영이 안돼서 추가함
					Thread.sleep(Integer.parseInt(org.apache.commons.lang.StringUtils.defaultIfEmpty(SmartUXProperties.getProperty("apply.cache.call.sleep"), "10000")));
					
					RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
							host, 
							port, 
							SmartUXProperties.getProperty("NoticeInfoDao.refreshNoticeInfo.url")+"?callByScheduler="+callByScheduler,
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.method"), "POST"), 
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"), 
							MediaType.APPLICATION_JSON.toString(),
							NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.timeout"), 60000)).getResponseBody());
					
					result.setFlag(codeMsg.getCode());
					result.setMessage(codeMsg.getMessage());
					cLog.middleLog("공지/이미지 정보관리 즉시적용", "API 호출", codeMsg.getCode(), codeMsg.getMessage());
				}
			});
		} catch (MimsCommonException e) {
			cLog.warnLog("공지/이미지 정보관리 즉시적용", "실패", e.getFlag(), e.getMessage(), e.getLocalizedMessage());
			result.setFlag(e.getFlag());
			result.setMessage(e.getMessage());
		} finally {
			cLog.endLog("공지/이미지 정보관리 즉시적용", loginUser, result.getFlag(), result.getMessage());
		}
		
		String resultMsg = "{\"flag\" : \"" + result.getFlag() + "\", \"message\" : \"" + result.getMessage() + "\"}";
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(resultMsg).toString(), responseHeaders, HttpStatus.OK);
	}
	
	/**
	 * CJ 공지/이미지 정보관리 즉시적용
	 * @param request
	 * @param response
	 * @param login_id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/noticeinfo/cacheCjNoticeInfo", method=RequestMethod.POST)
	public ResponseEntity<String> cacheCjNoticeInfo(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value="callByScheduler",  defaultValue="Y") final  String callByScheduler
			) throws Exception{

		final CLog cLog = new CLog(applyLogger, request.getRemoteAddr(), request.getRequestURI(), request.getMethod());
		final String loginUser = callByScheduler.equals("Y") ? "batch" : CookieUtil.getCookieUserID(request);
		final CUDResult result = new CUDResult();
		
		try {
			cLog.startLog("CJ 공지/이미지 정보관리 즉시적용",  loginUser);
			final String host = SmartUXProperties.getProperty("cache.sync.public.cj.host");
			final String port = SmartUXProperties.getProperty("cache.sync.public.cj.port");
			
			String fileLockPath = SmartUXProperties.getProperty("NoticeInfoDao.refreshNoticeInfo.fileLock");
			int waitTime = NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.wait"), 5);
			
			FileProcessLockFactory.start(fileLockPath, waitTime,  new FileProcessFunction("CJ 공지/이미지 정보관리 즉시적용", ResultCode.ApplyRequestFail) {
				
				@Override
				public void run() throws Exception {
					
					//즉시적용 호출 시 sleep 값 추가 -> DB분리건으로 OGG가 10초 이상 걸리는 경우가 있어 즉시적용 시 반영이 안돼서 추가함
					Thread.sleep(Integer.parseInt(org.apache.commons.lang.StringUtils.defaultIfEmpty(SmartUXProperties.getProperty("apply.cache.call.sleep"), "10000")));
					
					RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
							host, 
							port, 
							SmartUXProperties.getProperty("NoticeInfoDao.refreshNoticeInfo.cj.url")+"?callByScheduler="+callByScheduler,
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.method"), "POST"), 
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"), 
							MediaType.APPLICATION_JSON.toString(),
							NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.timeout"), 60000)).getResponseBody());
					
					result.setFlag(codeMsg.getCode());
					result.setMessage(codeMsg.getMessage());
					cLog.middleLog("CJ 공지/이미지 정보관리 즉시적용", "API 호출", codeMsg.getCode(), codeMsg.getMessage());
				}
			});
		} catch (MimsCommonException e) {
			cLog.warnLog("CJ 공지/이미지 정보관리 즉시적용", "실패", e.getFlag(), e.getMessage(), e.getLocalizedMessage());
			result.setFlag(e.getFlag());
			result.setMessage(e.getMessage());
		} finally {
			cLog.endLog("CJ 공지/이미지 정보관리 즉시적용", loginUser, result.getFlag(), result.getMessage());
		}
		
		String resultMsg = "{\"flag\" : \"" + result.getFlag() + "\", \"message\" : \"" + result.getMessage() + "\"}";
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(resultMsg).toString(), responseHeaders, HttpStatus.OK);
	}
	
	/**
	 * 4채널 공지 및 배너 목록
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/noticeinfo/getNoticeInfoList")
	public String getNoticeInfoList(
			@RequestParam(value="findValue", required=false, defaultValue="") String findValue,
			@RequestParam(value="pageNum", required=false, defaultValue="1") String pageNum,
			@RequestParam(value="s_use_yn", required=false, defaultValue="") String s_use_yn,
			@RequestParam(value="s_category", required=false, defaultValue="") String s_category,
			@RequestParam(value="s_ntype", required=false, defaultValue="") String s_ntype,
			@RequestParam(value="s_service", required=false, defaultValue="") String s_service,
			Model model) throws Exception{

		HTMLCleaner cleaner = new HTMLCleaner();
		s_category 	= cleaner.clean(s_category);
		findValue = cleaner.clean(findValue);
		pageNum = cleaner.clean(pageNum);
		s_use_yn = cleaner.clean(s_use_yn);
		s_ntype = cleaner.clean(s_ntype);
		s_service = cleaner.clean(s_service);
		
		NoticeInfoListProcVo noticeinfolistprocVo = new NoticeInfoListProcVo();

		
		pageNum = GlobalCom.isNull(pageNum, "1");
		
		noticeinfolistprocVo.setFindValue(findValue);
		noticeinfolistprocVo.setS_use_yn(s_use_yn);
		noticeinfolistprocVo.setS_category(s_category);
		noticeinfolistprocVo.setS_ntype(s_ntype);
		noticeinfolistprocVo.setS_service(s_service);
		noticeinfolistprocVo.setPageNum(Integer.parseInt(pageNum));
		
		noticeinfolistprocVo.setPageSize(GlobalCom.isNumber(noticeinfolistprocVo.getPageSize(),10));
		noticeinfolistprocVo.setBlockSize(GlobalCom.isNumber(noticeinfolistprocVo.getBlockSize(),10));
		noticeinfolistprocVo.setPageNum(GlobalCom.isNumber(noticeinfolistprocVo.getPageNum(),1));
		
		noticeinfolistprocVo.setFindValue(GlobalCom.isNull(noticeinfolistprocVo.getFindValue()));
		noticeinfolistprocVo.setS_use_yn(GlobalCom.isNull(noticeinfolistprocVo.getS_use_yn()));
		noticeinfolistprocVo.setS_category(GlobalCom.isNull(noticeinfolistprocVo.getS_category()));
		noticeinfolistprocVo.setS_ntype(GlobalCom.isNull(noticeinfolistprocVo.getS_ntype()));
		
		try {
			List<NotiMainCodeListVo> service_code = service.getNotiMainCodeList();
			noticeinfolistprocVo.setService_code(service_code);
			
			if(service_code != null ){//초기 들어올때 서비스 구분은 첫번째 라인의 서비스로..
				NotiMainCodeListVo notimaincodelistVo = service_code.get(0);
				if (noticeinfolistprocVo.getS_service().equals("")) noticeinfolistprocVo.setS_service(notimaincodelistVo.getCode());
			}
			
			List<NotiCodeListVo> category_code = service.getNoticeInfoCodeList();
			noticeinfolistprocVo.setCategory_code(category_code);
			
			List<NoticeInfoListVo> list = service.getNoticeInfoList(noticeinfolistprocVo);
			noticeinfolistprocVo.setList(list);
			
			noticeinfolistprocVo.setPageCount(service.getNoticeInfoListTotalCnt(noticeinfolistprocVo));
			
			model.addAttribute("vo", noticeinfolistprocVo);
		} catch (Exception e) {
			//logger.error("getAdminList "+e.getClass().getName()); 
			//logger.error("getAdminList "+e.getMessage());
			logger.error("[getNoticeInfoList]["+e.getClass().getName()+"]["+e.getMessage()+"]");
		}
		
		return "/admin/noticeinfo/getNoticeInfoList";
	}
	
	/**
	 * NoticeInfo 추가
	 * @param request
	 * @param response
	 * @param nvalue_file
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/noticeinfo/insertNoticeInfo", method=RequestMethod.POST)
	public String insertNoticeInfo(
			HttpServletRequest request
			, HttpServletResponse response
			, @RequestParam(value="nvalue_file", required=false) MultipartFile nvalue_file
			, Model model
			) throws Exception{
		
		// 파일을 업로드 하는 것이기 때문에 기존의 request 객체를 MultipartHttpServletRequest로 업로드 할 필요가 있다
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

		String p_service = GlobalCom.isNull(multipartRequest.getParameter("service"));
		String ntype = GlobalCom.isNull(multipartRequest.getParameter("ntype"));		
		String category = GlobalCom.isNull(multipartRequest.getParameter("category"));		
		String nvalue = GlobalCom.isNull(multipartRequest.getParameter("nvalue"));	
		String display_sec = GlobalCom.isNull(multipartRequest.getParameter("display_sec"));
		String ordered = GlobalCom.isNull(multipartRequest.getParameter("ordered"));	
		String use_yn = GlobalCom.isNull(multipartRequest.getParameter("use_yn"));
		

		HTMLCleaner cleaner = new HTMLCleaner();
		p_service 	= cleaner.clean(p_service);
		ntype 	= cleaner.clean(ntype);
		category 	= cleaner.clean(category);
		nvalue 	= cleaner.clean(nvalue);
		display_sec 	= cleaner.clean(display_sec);
		ordered 	= cleaner.clean(ordered);
		use_yn 	= cleaner.clean(use_yn);
		if(use_yn==null || use_yn.equals("")) use_yn="N";
		
		File img_file = null;
		String tmp_file_name = "";
		String bg_img_file = "";
		String folderPath = SmartUXProperties.getProperty("imgupload.noticeinfo.folder");
		try{
			//logger.info("1111111111111111111111111111");
			if(nvalue_file.getSize() != 0L){
				tmp_file_name = nvalue_file.getOriginalFilename();
				String ext = tmp_file_name.substring(tmp_file_name.lastIndexOf(".")+1,tmp_file_name.length()); // 확장자 구하기
				if(folderPath!=null && !folderPath.equals("")){
					folderPath = folderPath + "/";
				}
				bg_img_file = folderPath + category + "_" + Long.toString(System.currentTimeMillis()) + "." + ext; // 저장파일명
				
				// String newFilename = "C:\\SmartUX\\workspaces\\smartux\\src\\main\\webapp\\tmpimgdir\\" + bg_img_file;
				//String newFilename = realPath + "/" + bg_img_file;
				String newFilename = SmartUXProperties.getProperty("imgupload.dir") + "/" + bg_img_file;
				
				logger.debug("tmp_file_name : " + tmp_file_name);
				logger.debug("ext :  " + ext);
				logger.debug("bg_img_file :  " + bg_img_file);
				logger.debug("newFilename :  " + newFilename);
				img_file = new File(newFilename);
				nvalue_file.transferTo(img_file);
			}
			//logger.info("22222222222222222");
			
			InsertNoticeInfoVo insertnoticeinfoVo = new InsertNoticeInfoVo();
			//logger.info("3333333");
			insertnoticeinfoVo.setCategory(category);
			//logger.info("4444444444");
			insertnoticeinfoVo.setDisplay_sec(display_sec);
			//logger.info("555555555555");
			insertnoticeinfoVo.setNtype(ntype);
			//logger.info("666");
			insertnoticeinfoVo.setNvalue(nvalue);
			//logger.info("7777");
			insertnoticeinfoVo.setOrdered(ordered);
			//logger.info("8888");
			insertnoticeinfoVo.setService(p_service);
			//logger.info("9999");
			insertnoticeinfoVo.setUse_yn(use_yn);
			//logger.info("10101010");
			
			if(ntype.equals("TXT")){
				insertnoticeinfoVo.setNvalue(nvalue);
			}else if(ntype.equals("IMG")){
				insertnoticeinfoVo.setNvalue(bg_img_file);
			}
			//logger.info("111_11111");
			
			service.insertNoticeInfo(insertnoticeinfoVo);
			//logger.info("1212121212");
		}catch(Exception e){
			logger.error("[insertNoticeInfo]["+e.getClass().getName()+"]["+e.getMessage()+"]");
		}

		model.addAttribute("s_service",p_service );
		return "redirect:/admin/noticeinfo/getNoticeInfoList.do";
	}

	/**
	 * NoticeInfo 수정
	 * @param request
	 * @param response
	 * @param model
	 * @param nvalue_file
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/noticeinfo/updateNoticeInfo", method=RequestMethod.POST)
	public String updateNoticeInfo(
			HttpServletRequest request
			, HttpServletResponse response
			, Model model
			, @RequestParam(value="nvalue_file", required=false) MultipartFile nvalue_file
			) throws Exception{
		
		// 파일을 업로드 하는 것이기 때문에 기존의 request 객체를 MultipartHttpServletRequest로 업로드 할 필요가 있다
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

		String seq = GlobalCom.isNull(multipartRequest.getParameter("seq"));
		//String service = GlobalCom.isNull(multipartRequest.getParameter("service"));
		String ntype = GlobalCom.isNull(multipartRequest.getParameter("ntype"));		
		String category = GlobalCom.isNull(multipartRequest.getParameter("category"));		
		String nvalue = GlobalCom.isNull(multipartRequest.getParameter("nvalue"));	
		String display_sec = GlobalCom.isNull(multipartRequest.getParameter("display_sec"));
		String ordered = GlobalCom.isNull(multipartRequest.getParameter("ordered"));	
		String use_yn = GlobalCom.isNull(multipartRequest.getParameter("use_yn"));
		
		String findValue = GlobalCom.isNull(multipartRequest.getParameter("findValue"));
		String pageNum = GlobalCom.isNull(multipartRequest.getParameter("pageNum"));
		String s_service = GlobalCom.isNull(multipartRequest.getParameter("s_service"));
		String s_category = GlobalCom.isNull(multipartRequest.getParameter("s_category"));
		String s_ntype = GlobalCom.isNull(multipartRequest.getParameter("s_ntype"));
		String s_use_yn = GlobalCom.isNull(multipartRequest.getParameter("s_use_yn"));
		

		HTMLCleaner cleaner = new HTMLCleaner();
		seq 	= cleaner.clean(seq);
		//service 	= cleaner.clean(service);
		ntype 	= cleaner.clean(ntype);
		category 	= cleaner.clean(category);
		nvalue 	= cleaner.clean(nvalue);
		display_sec 	= cleaner.clean(display_sec);
		ordered 	= cleaner.clean(ordered);
		use_yn 	= cleaner.clean(use_yn);
		if(use_yn==null || use_yn.equals("")) use_yn="N";
		findValue 	= cleaner.clean(findValue);
		pageNum 	= cleaner.clean(pageNum);
		s_service 	= cleaner.clean(s_service);
		s_category 	= cleaner.clean(s_category);
		s_ntype 	= cleaner.clean(s_ntype);
		s_use_yn 	= cleaner.clean(s_use_yn);
		
		File img_file = null;
		String tmp_file_name = "";
		String bg_img_file = "";
		String folderPath = SmartUXProperties.getProperty("imgupload.noticeinfo.folder");
		try{
			if(nvalue_file.getSize() != 0L){
				tmp_file_name = nvalue_file.getOriginalFilename();
				String ext = tmp_file_name.substring(tmp_file_name.lastIndexOf(".")+1,tmp_file_name.length()); // 확장자 구하기
				if(folderPath!=null && !folderPath.equals("")){
					folderPath = folderPath + "/";
				}
				bg_img_file = folderPath + category + "_" + Long.toString(System.currentTimeMillis()) + "." + ext; // 저장파일명
				
				// String newFilename = "C:\\SmartUX\\workspaces\\smartux\\src\\main\\webapp\\tmpimgdir\\" + bg_img_file;
				//String newFilename = realPath + "/" + bg_img_file;
				String newFilename = SmartUXProperties.getProperty("imgupload.dir") + "/" + bg_img_file;
				
				logger.debug("tmp_file_name : " + tmp_file_name);
				logger.debug("ext :  " + ext);
				logger.debug("bg_img_file :  " + bg_img_file);
				logger.debug("newFilename :  " + newFilename);
				img_file = new File(newFilename);
				nvalue_file.transferTo(img_file);
			}
			
			UpdateNoticeInfoVo updatenoticeinfoVo = new UpdateNoticeInfoVo();
			updatenoticeinfoVo.setSeq(seq);
			updatenoticeinfoVo.setCategory(category);
			updatenoticeinfoVo.setDisplay_sec(display_sec);
			updatenoticeinfoVo.setNtype(ntype);
			updatenoticeinfoVo.setOrdered(ordered);
			updatenoticeinfoVo.setUse_yn(use_yn);
			
			if(ntype.equals("TXT")){
				updatenoticeinfoVo.setNvalue(nvalue);
			}else if(ntype.equals("IMG")){
				updatenoticeinfoVo.setNvalue(bg_img_file);
			}
			
			service.updateNoticeInfo(updatenoticeinfoVo);
			
			
		}catch(Exception e){
			logger.error("[updateNoticeInfo]["+e.getClass().getName()+"]["+e.getMessage()+"]");
		}
		
		model.addAttribute("s_service",s_service );
		model.addAttribute("s_category",s_category );
		model.addAttribute("s_ntype",s_ntype );
		model.addAttribute("s_use_yn",s_use_yn );
		model.addAttribute("findValue",findValue );
		model.addAttribute("pageNum",pageNum );
		return "redirect:/admin/noticeinfo/getNoticeInfoList.do";
	
	}
	
	/**
	 * noticeinfo 삭제
	 * @param seq
	 * @param findValue
	 * @param pageNum
	 * @param s_use_yn
	 * @param s_category
	 * @param s_ntype
	 * @param s_service
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/noticeinfo/deleteNoticeInfo")
	public String deleteNoticeInfo(
			@RequestParam(value="seq", required=false, defaultValue="") String seq,
			@RequestParam(value="findValue", required=false, defaultValue="") String findValue,
			@RequestParam(value="pageNum", required=false, defaultValue="") String pageNum,
			@RequestParam(value="s_use_yn", required=false, defaultValue="") String s_use_yn,
			@RequestParam(value="s_category", required=false, defaultValue="") String s_category,
			@RequestParam(value="s_ntype", required=false, defaultValue="") String s_ntype,
			@RequestParam(value="s_service", required=false, defaultValue="") String s_service,
			Model model) throws Exception{

			HTMLCleaner cleaner = new HTMLCleaner();
			s_service 	= cleaner.clean(s_service);
			s_category 	= cleaner.clean(s_category);
			s_ntype 	= cleaner.clean(s_ntype);
			s_use_yn 	= cleaner.clean(s_use_yn);
			findValue 	= cleaner.clean(findValue);
			pageNum 	= cleaner.clean(pageNum);
			
			try{
			
				service.deleteNoticeInfo(seq);
			
			}catch(Exception e){
				logger.error("[deleteNoticeInfo]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			}

			
			model.addAttribute("s_service",s_service );
			model.addAttribute("s_category",s_category );
			model.addAttribute("s_ntype",s_ntype );
			model.addAttribute("s_use_yn",s_use_yn );
			model.addAttribute("findValue",findValue );
			model.addAttribute("pageNum",pageNum );
			return "redirect:/admin/noticeinfo/getNoticeInfoList.do";
	}
	
	
	/**
	 * 상용 프리뷰 보기
	 * @param s_service
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/noticeinfo/viewRealNoticeInfoList")
	public String viewRealNoticeInfoList(
			@RequestParam(value="service", required=true) String s_service
			,Model model
			) throws Exception{

		HTMLCleaner cleaner = new HTMLCleaner();
		s_service 	= cleaner.clean(s_service);
		
		HashMap<String, List<CacheNoticeInfoListVo>> notiMap = api_service.viewNoticeInfoListReal(s_service);
		
		List<CacheNoticeInfoListVo> txtVo = notiMap.get("TXT");
		List<CacheNoticeInfoListVo> imgVo = notiMap.get("IMG");

		model.addAttribute("txtVo", txtVo);
		model.addAttribute("imgVo", imgVo);
		return "/admin/noticeinfo/viewNoticeInfoList";
	}
	
	/**
	 * 프리뷰 보기
	 * @param s_service
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/noticeinfo/viewNoticeInfoList")
	public String viewNoticeInfoList(
			@RequestParam(value="service", required=true) String s_service
			,Model model
			) throws Exception{

		HTMLCleaner cleaner = new HTMLCleaner();
		s_service 	= cleaner.clean(s_service);
		
		HashMap<String, List<ViewNoticeInfoListVo>> notiMap = service.viewNoticeInfoList(s_service);
		
		List<ViewNoticeInfoListVo> txtVo = notiMap.get("TXT");
		List<ViewNoticeInfoListVo> imgVo = notiMap.get("IMG");

		model.addAttribute("txtVo", txtVo);
		model.addAttribute("imgVo", imgVo);
		return "/admin/noticeinfo/viewNoticeInfoList";
	}

}
