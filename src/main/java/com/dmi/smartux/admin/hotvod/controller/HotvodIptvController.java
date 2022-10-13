package com.dmi.smartux.admin.hotvod.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.springframework.web.bind.annotation.ModelAttribute;
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
import com.dmi.smartux.admin.hotvod.service.HotvodIptvService;
import com.dmi.smartux.admin.hotvod.vo.HotvodHitstatsVO;
import com.dmi.smartux.admin.hotvod.vo.HotvodSearchVO;
import com.dmi.smartux.admin.hotvod.vo.HotvodSiteVO;
import com.dmi.smartux.admin.login.service.LoginService;
import com.dmi.smartux.common.exception.ExceptionHandler;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.CookieUtil;
import com.dmi.smartux.common.util.ExcelManager;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.util.HTMLCleaner;
import com.dmi.smartux.common.vo.CUDResult;
import com.dmi.smartux.common.vo.HotvodFilteringSiteVo;

import net.sf.json.JSONSerializer;

/**
 * 화제동영상 - Controller
 * @author JKJ
 */
@SuppressWarnings("static-access")
@Controller
public class HotvodIptvController {

	private final Log logger = LogFactory.getLog(this.getClass());

	private final Log applyLogger = LogFactory.getLog("adminApplyCacheHistroy");

	@Autowired
	HotvodIptvService service;

	@Autowired
	LoginService loginService;

	@RequestMapping(value="/admin/hotvod/appFilteringSite", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> applyCacheByFiltering(HttpServletRequest request, HttpServletResponse response ) throws Exception {

		final CLog cLog = new CLog(applyLogger, request.getRemoteAddr(), request.getRequestURI(), request.getMethod());
		final String loginUser = CookieUtil.getCookieUserID(request);
		final CUDResult result = new CUDResult();

		boolean isFileLockPass = false;
		String fileLockPath = SmartUXProperties.getProperty("SmartUXHotvodDao.filteringSite.fileLock");
		int waitTime = NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.wait"), 5);

		try {
			cLog.startLog("화제동영상 필터링사이트 즉시적용", loginUser);
			
			final String host = SmartUXProperties.getProperty("cache.sync.public.host");
			final String port = SmartUXProperties.getProperty("cache.sync.public.port");
					
			FileProcessLockFactory.start(fileLockPath, waitTime, new FileProcessFunction("화제동영상 필터 즉시적용", ResultCode.ApplyRequestFail) {
				@Override
				public void run() throws Exception {
					
					//즉시적용 호출 시 sleep 값 추가 -> DB분리건으로 OGG가 10초 이상 걸리는 경우가 있어 즉시적용 시 반영이 안돼서 추가함
					Thread.sleep(Integer.parseInt(org.apache.commons.lang.StringUtils.defaultIfEmpty(SmartUXProperties.getProperty("apply.cache.call.sleep"), "10000")));
					
					RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
							host, port,
							SmartUXProperties.getProperty("SmartUXHotvodDao.filteringSite.url"),
							StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.method"), "POST"),
							StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"),
							MediaType.APPLICATION_JSON.toString(),
							NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.timeout"), 60000)).getResponseBody());

					result.setFlag(codeMsg.getCode());
					result.setMessage(codeMsg.getMessage());
					cLog.middleLog("화제동영상 필터링사이트 즉시적용", "API 호출 성공", codeMsg.getCode(), codeMsg.getMessage());
				}
			});
		} catch (MimsCommonException e) {
			result.setFlag(e.getFlag());
			result.setMessage(e.getMessage());
		} finally {
			cLog.endLog("화제동영상 필터링사이트 즉시적용", loginUser, result.getFlag(), result.getMessage());
		}
		String resultMsg = "{\"flag\" : \"" + result.getFlag() + "\", \"message\" : \"" + result.getMessage() + "\"}";
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(resultMsg).toString(), responseHeaders, HttpStatus.OK);
	}
	
	/**
	 * CJ 서버 아이들나라 추가 2019.12.23 : CJ서버 화제동영상 사이트 즉시적용 추가
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/hotvod/appCacheCjFilteringSite", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> applyCacheByCjFiltering(HttpServletRequest request, HttpServletResponse response ) throws Exception {
		
		final CLog cLog = new CLog(applyLogger, request.getRemoteAddr(), request.getRequestURI(), request.getMethod());
		final String loginUser = CookieUtil.getCookieUserID(request);
		final CUDResult result = new CUDResult();
		
		String fileLockPath = SmartUXProperties.getProperty("SmartUXHotvodDao.filteringSite.fileLock");
		int waitTime = NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.wait"), 5);
		
		try {
			cLog.startLog("CJ 화제동영상 필터링사이트 즉시적용", loginUser);
			
			final String host = SmartUXProperties.getProperty("cache.sync.public.cj.host");
			final String port = SmartUXProperties.getProperty("cache.sync.public.cj.port");
			
			FileProcessLockFactory.start(fileLockPath, waitTime, new FileProcessFunction("CJ 화제동영상 필터 즉시적용", ResultCode.ApplyRequestFail) {
				@Override
				public void run() throws Exception {
					
					//즉시적용 호출 시 sleep 값 추가 -> DB분리건으로 OGG가 10초 이상 걸리는 경우가 있어 즉시적용 시 반영이 안돼서 추가함
					Thread.sleep(Integer.parseInt(org.apache.commons.lang.StringUtils.defaultIfEmpty(SmartUXProperties.getProperty("apply.cache.call.sleep"), "10000")));
					
					RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
							host, port,
							SmartUXProperties.getProperty("SmartUXHotvodDao.refreshCjFilteringSite.url"),
							StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.method"), "POST"),
							StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"),
							MediaType.APPLICATION_JSON.toString(),
							NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.timeout"), 60000)).getResponseBody());
					
					result.setFlag(codeMsg.getCode());
					result.setMessage(codeMsg.getMessage());
					cLog.middleLog("CJ 화제동영상 필터링사이트 즉시적용", "API 호출 성공", codeMsg.getCode(), codeMsg.getMessage());
				}
			});
		} catch (MimsCommonException e) {
			result.setFlag(e.getFlag());
			result.setMessage(e.getMessage());
		} finally {
			cLog.endLog("CJ 화제동영상 필터링사이트 즉시적용", loginUser, result.getFlag(), result.getMessage());
		}
		String resultMsg = "{\"flag\" : \"" + result.getFlag() + "\", \"message\" : \"" + result.getMessage() + "\"}";
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(resultMsg).toString(), responseHeaders, HttpStatus.OK);
	}



	/**
	 * 사이트 목록
	 * @param findName
	 * @param findValue
	 * @param pageNum
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/hotvod/siteList.do",method=RequestMethod.GET)
	public String siteList(
			@RequestParam(value="findName", required=false, defaultValue="") String findName,
			@RequestParam(value="findValue", required=false, defaultValue="") String findValue,
			@RequestParam(value="pageNum", required=false, defaultValue="1") String pageNum,
			Model model) throws Exception {
		HTMLCleaner cleaner = new HTMLCleaner();
		findName 	= cleaner.clean(findName);
		findValue = cleaner.clean(findValue);
		pageNum = cleaner.clean(pageNum);

		HotvodSearchVO hotvodSearchVO = new HotvodSearchVO();

		//페이지 
		hotvodSearchVO.setPageSize(GlobalCom.isNumber(hotvodSearchVO.getPageSize(),10));
		hotvodSearchVO.setBlockSize(GlobalCom.isNumber(hotvodSearchVO.getBlockSize(),10));
		hotvodSearchVO.setPageNum(GlobalCom.isNumber(Integer.parseInt(GlobalCom.isNull(pageNum, "1")),1));
		//검색 
		hotvodSearchVO.setFindName(GlobalCom.isNull(findName, "SITE_NAME"));
		hotvodSearchVO.setFindValue(findValue);

		List<HotvodSiteVO> list =  service.siteList(hotvodSearchVO);
		hotvodSearchVO.setPageCount(service.siteListCnt(hotvodSearchVO));

		model.addAttribute("vo", hotvodSearchVO);
		model.addAttribute("list", list);

		return "/admin/hotvod/siteList";
	}

	/**
	 * 사이트 상세/수정
	 * @param site_id
	 * @param findName
	 * @param findValue
	 * @param pageNum
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/hotvod/siteDetail.do",method=RequestMethod.GET)
	public String siteDetail(
			@RequestParam(value="site_id", required=false, defaultValue="") String site_id,
			@RequestParam(value="findName", required=false, defaultValue="") String findName,
			@RequestParam(value="findValue", required=false, defaultValue="") String findValue,
			@RequestParam(value="pageNum", required=false, defaultValue="1") String pageNum,
			@RequestParam(value="result", required=false, defaultValue="") String result,
			Model model) throws Exception {
		HTMLCleaner cleaner = new HTMLCleaner();
		site_id 	= cleaner.clean(site_id);
		findName 	= cleaner.clean(findName);
		findValue = cleaner.clean(findValue);
		pageNum = cleaner.clean(pageNum);
		result = cleaner.clean(result);

		HotvodSearchVO hotvodSearchVO = new HotvodSearchVO();

		hotvodSearchVO.setSite_id(site_id);
		hotvodSearchVO.setFindName(findName);
		hotvodSearchVO.setFindValue(findValue);
		hotvodSearchVO.setPageNum(Integer.parseInt(pageNum));

		HotvodSiteVO hotvodSiteVO = service.siteDetail(hotvodSearchVO);

		model.addAttribute("vo", hotvodSearchVO);
		model.addAttribute("site", hotvodSiteVO);
		model.addAttribute("result", result);
		//-------이미지 체크----------//
		//int imgHdtvSize = Integer.parseInt(GlobalCom.isNull(SmartUXProperties.getProperty("hotvod.hdtv.size"),""))/1024;
		int imgIptvSize = Integer.parseInt(GlobalCom.isNull(SmartUXProperties.getProperty("hotvod.iptv.size"),""))/1024;
		String imgFormat = GlobalCom.isNull(SmartUXProperties.getProperty("hotvod.format"),"");
		//model.addAttribute("imgHdtvSize", imgHdtvSize);
		model.addAttribute("imgIptvSize", imgIptvSize);
		model.addAttribute("imgFormat", imgFormat);
		//-------이미지 체크----------//	
		return "/admin/hotvod/siteDetail";
	}

	/**
	 * 사이트 수정
	 * @param hotvodSiteVO
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/hotvod/siteUpdate.do",method=RequestMethod.POST)
	public String siteUpdate(
			@RequestParam(value="file_tv", required=false, defaultValue="") MultipartFile uploadfile_tv,
			HotvodSiteVO hotvodSiteVO,
			HotvodSearchVO hotvodSearchVO,
			HttpServletRequest request,
			Model model) throws Exception {
		String cookieID = CookieUtil.getCookieUserID(request);

		String tmp_file_name = "";
		String bg_img_file = "";
		File img_file = null;

		if (uploadfile_tv.getSize() != 0L) {
			tmp_file_name = uploadfile_tv.getOriginalFilename();
			String ext = tmp_file_name.substring(	tmp_file_name.lastIndexOf(".") + 1,	tmp_file_name.length()); // 확장자 구하기
			ext = ext.toLowerCase();
			bg_img_file = Long.toString(System.currentTimeMillis()) + "."+ ext; // 시스템타임.확장자 구조로 한다
			if(bg_img_file.equals(hotvodSiteVO.getSite_img())){
				bg_img_file = Long.toString(System.currentTimeMillis()+1) + "."+ ext;
			}
			String newFilename=SmartUXProperties.getProperty("hotvod.upload.dir")+ bg_img_file;

			img_file = new File(newFilename);
			uploadfile_tv.transferTo(img_file);

			hotvodSiteVO.setSite_img_tv(bg_img_file);
		}else{
			String savefilenm = hotvodSiteVO.getSite_img_tv();
			if(savefilenm != null  && !savefilenm.isEmpty() ){
				hotvodSiteVO.setSite_img_tv(savefilenm.substring(savefilenm.lastIndexOf("/") +1));
			}
		}

		hotvodSiteVO.setMod_id(cookieID);
		service.siteUpdate(hotvodSiteVO);

		model.addAttribute("site_id", hotvodSearchVO.getSite_id());
		model.addAttribute("findName", hotvodSearchVO.getFindName());
		model.addAttribute("findValue", hotvodSearchVO.getFindValue());
		model.addAttribute("pageNum", hotvodSearchVO.getPageNum());

		return "redirect:/admin/hotvod/siteDetail.do?result=" + SmartUXProperties.getProperty("flag.success");
	}

	/**
	 * 사이트 등록 폼
	 * @param findName
	 * @param findValue
	 * @param pageNum
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/hotvod/siteInsertForm.do",method=RequestMethod.GET)
	public String siteInsertForm(
			@RequestParam(value="findName", required=false, defaultValue="") String findName,
			@RequestParam(value="findValue", required=false, defaultValue="") String findValue,
			@RequestParam(value="pageNum", required=false, defaultValue="1") String pageNum,
			@RequestParam(value="result", required=false, defaultValue="") String result,
			Model model) throws Exception {
		HTMLCleaner cleaner = new HTMLCleaner();
		findName 	= cleaner.clean(findName);
		findValue = cleaner.clean(findValue);
		pageNum = cleaner.clean(pageNum);
		result = cleaner.clean(result);

		HotvodSearchVO hotvodSearchVO = new HotvodSearchVO();

		hotvodSearchVO.setFindName(findName);
		hotvodSearchVO.setFindValue(findValue);
		hotvodSearchVO.setPageNum(Integer.parseInt(pageNum));
		//사이트 아이디 신규 생성
		hotvodSearchVO.setSite_id(service.getSiteId());

		model.addAttribute("vo", hotvodSearchVO);
		model.addAttribute("result", result);
		//-------이미지 체크----------//
		//int imgHdtvSize = Integer.parseInt(GlobalCom.isNull(SmartUXProperties.getProperty("hotvod.hdtv.size"),""))/1024;
		int imgIptvSize = Integer.parseInt(GlobalCom.isNull(SmartUXProperties.getProperty("hotvod.iptv.size"),""))/1024;
		String imgFormat = GlobalCom.isNull(SmartUXProperties.getProperty("hotvod.format"),"");
		//model.addAttribute("imgHdtvSize", imgHdtvSize);
		model.addAttribute("imgIptvSize", imgIptvSize);
		model.addAttribute("imgFormat", imgFormat);
		//-------이미지 체크----------//		
		return "/admin/hotvod/siteInsert";
	}

	/**
	 * 사이트 등록
	 * @param hotvodSiteVO
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/hotvod/siteInsert.do",method=RequestMethod.POST)
	public String siteInsert(
			@RequestParam(value="file_tv", required=false, defaultValue="") MultipartFile uploadfile_tv,
			HotvodSiteVO hotvodSiteVO,
			HttpServletRequest request,
			Model model) throws Exception {
		String cookieID = CookieUtil.getCookieUserID(request);

		String tmp_file_name = "";
		String bg_img_file = "";
		File img_file = null;

		if (uploadfile_tv.getSize() != 0L) {
			tmp_file_name = uploadfile_tv.getOriginalFilename();
			String ext = tmp_file_name.substring(	tmp_file_name.lastIndexOf(".") + 1,	tmp_file_name.length()); // 확장자 구하기
			ext = ext.toLowerCase();
			bg_img_file = Long.toString(System.currentTimeMillis()) + "."+ ext; // 시스템타임.확장자 구조로 한다
			if(bg_img_file.equals(hotvodSiteVO.getSite_img())){
				bg_img_file = Long.toString(System.currentTimeMillis()+1) + "."+ ext;
			}
			String newFilename=SmartUXProperties.getProperty("hotvod.upload.dir")+ bg_img_file;

			img_file = new File(newFilename);
			uploadfile_tv.transferTo(img_file);

			hotvodSiteVO.setSite_img_tv(bg_img_file);
		}

		hotvodSiteVO.setReg_id(cookieID);
		service.siteInsert(hotvodSiteVO);

		return "redirect:/admin/hotvod/siteInsertForm.do?result=" + SmartUXProperties.getProperty("flag.success");
	}

	/**
	 * 사이트 아이디 중복 검사
	 * @param site_id
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/hotvod/siteIdChk.do",method=RequestMethod.POST)
	public ResponseEntity<String> siteIdChk(
			@RequestParam(value="site_id", required=false, defaultValue="") String site_id,
			HttpServletRequest request,
			Model model
	) throws Exception{

		String result = "";
		String resultcode = "";
		String resultmessage = "";
		String resultCount = "";

//			String cookieID = CookieUtil.getCookieUserID(request);
//			String userIp = request.getRemoteAddr();

		try{
			resultCount = Integer.toString(service.siteIdChk(site_id));
			resultcode = SmartUXProperties.getProperty("flag.success");
			resultmessage = SmartUXProperties.getProperty("message.success");
		} catch (Exception e) {
//				logger.error("[deleteEventNotiProc]["+e.getClass().getName()+"]["+e.getMessage()+"]");		

			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}

		result = "{\"result\":{\"message\":\""+resultmessage+"\",\"flag\":\""+resultcode+"\",\"resultCount\":\""+resultCount+"\"}}";

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(result).toString(), responseHeaders, HttpStatus.CREATED);
	}

	/**
	 * 사이트 삭제
	 * @param site_id
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/hotvod/siteDelete.do",method=RequestMethod.POST)
	public ResponseEntity<String> siteDelete(
			@RequestParam(value="site_id", required=false, defaultValue="") String site_id,
			HttpServletRequest request,
			Model model
	) throws Exception{

		String result = "";
		String resultcode = "";
		String resultmessage = "";

//			String cookieID = CookieUtil.getCookieUserID(request);
//			String userIp = request.getRemoteAddr();

		try{
			service.siteDelete(site_id);
			resultcode = SmartUXProperties.getProperty("flag.success");
			resultmessage = SmartUXProperties.getProperty("message.success");
		} catch (Exception e) {
//				logger.error("[deleteEventNotiProc]["+e.getClass().getName()+"]["+e.getMessage()+"]");		

			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}

		result = "{\"result\":{\"message\":\""+resultmessage+"\",\"flag\":\""+resultcode+"\"}}";

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(result).toString(), responseHeaders, HttpStatus.CREATED);
	}

	/**
	 * 조회수 통계
	 * @param startDt
	 * @param endDt
	 * @param pageSize
	 * @param sortOrder
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/hotvod/hitcntStats.do",method=RequestMethod.GET)
	public String hitcntStats(
			@RequestParam(value="startDt", required=false, defaultValue="") String startDt,
			@RequestParam(value="endDt", required=false, defaultValue="") String endDt,
			@RequestParam(value="pageSize", required=false, defaultValue="20") String pageSize,
			@RequestParam(value="sortOrder", required=false, defaultValue="I") String sortOrder,
			Model model) throws Exception {
		HTMLCleaner cleaner = new HTMLCleaner();
		startDt = cleaner.clean(startDt);
		endDt = cleaner.clean(endDt);
		pageSize = cleaner.clean(pageSize);
		sortOrder = cleaner.clean(sortOrder);

		HotvodSearchVO hotvodSearchVO = new HotvodSearchVO();
		hotvodSearchVO.setStartDt(startDt);
		hotvodSearchVO.setEndDt(endDt);
		hotvodSearchVO.setPageSize(Integer.parseInt(pageSize));
		hotvodSearchVO.setSortOrder(sortOrder);

		List<HotvodHitstatsVO> list = service.getHitstatsList(hotvodSearchVO);

		model.addAttribute("vo", hotvodSearchVO);
		model.addAttribute("list", list);

		return "/admin/hotvod/hitcntStats";
	}

	/**
	 * @Method Name : filteringSiteList
	 * @Comment 	: 필터링사이트 목록
	 */
	@RequestMapping(value="/admin/hotvod/filteringSiteList.do",method=RequestMethod.GET)
	public String filteringSiteList(
			@ModelAttribute HotvodFilteringSiteVo filtSiteVo,
			Model model) throws Exception {

		// 서비스 목록조회
		String svcAllName = SmartUXProperties.getProperty("hotvod.filtering.svcType");
		List<String> svcList = Arrays.asList(svcAllName.split("\\|"));

		// 셋디폴트 서비스타입
		if (filtSiteVo.getSvcType().equalsIgnoreCase("")) {
			filtSiteVo.setSvcType(svcList.get(0));
		}

		// 필터링사이트 목록조회
		List<HotvodFilteringSiteVo> urlList = new ArrayList<HotvodFilteringSiteVo>();
		urlList = service.getFilteringSiteList(filtSiteVo);

		model.addAttribute("vo", filtSiteVo);
		model.addAttribute("svcList", svcList);
		model.addAttribute("urlList", urlList);

		return "/admin/hotvod/filteringSiteList";
	}

	/**
	 * 엑셀 다운로드
	 * @param response
	 * @param startDt
	 * @param endDt
	 * @param pageSize
	 * @param sortOrder
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/admin/hotvod/downloadHotvodExcelFile", method = RequestMethod.GET)
	public byte[] downloadResultFile(HttpServletResponse response,
									 @RequestParam(value="startDt", required=false, defaultValue="") String startDt,
									 @RequestParam(value="endDt", required=false, defaultValue="") String endDt,
									 @RequestParam(value="pageSize", required=false, defaultValue="20") String pageSize,
									 @RequestParam(value="sortOrder", required=false, defaultValue="I") String sortOrder,
									 Model model) throws Exception {
		HTMLCleaner cleaner = new HTMLCleaner();
		startDt = cleaner.clean(startDt);
		endDt = cleaner.clean(endDt);
		pageSize = cleaner.clean(pageSize);
		sortOrder = cleaner.clean(sortOrder);

		HotvodSearchVO hotvodSearchVO = new HotvodSearchVO();
		hotvodSearchVO.setStartDt(startDt);
		hotvodSearchVO.setEndDt(endDt);
		hotvodSearchVO.setPageSize(Integer.parseInt(pageSize));
		hotvodSearchVO.setSortOrder(sortOrder);

		String domain = SmartUXProperties.getProperty("smartux.domain.https");
		model.addAttribute("domain_https", domain);

		List<Object> header = new ArrayList<Object>();
		List<List<Object>> data = new ArrayList<List<Object>>();

		String[] headerAry = SmartUXProperties.getProperty("hotvod.stats.excel.header").split("\\|");

		for (String str : headerAry) {
			header.add(str);
		}

		//noinspection unchecked
		List<HotvodHitstatsVO> list = service.getHitstatsList(hotvodSearchVO);

		for (HotvodHitstatsVO vo : list) {
			List<Object> obj = new ArrayList<Object>();
			obj.add(vo.getRanking());
			obj.add(vo.getContent_name());
			obj.add(vo.getContent_url());
			obj.add(vo.getParent_cate());
			obj.add(vo.getSite_name());
			obj.add(vo.getS_hit_cnt());
			obj.add(vo.getI_hit_cnt());
			obj.add(vo.getE_hit_cnt());
			data.add(obj);
		}

		ExcelManager excelManager = new ExcelManager(header, data);
		excelManager.setSheetName("hit_stats");
		excelManager.setWidth(6000);
		byte[] bytes = excelManager.makeExcel();

		response.setHeader("Content-Disposition", "attachment; filename=hit_stats.xlsx");
		response.setContentLength(bytes.length);
		response.setContentType("application/vnd.ms-excel");

		return bytes;
	}
	
	/**
	 * @Method Name : regFilteringSite
	 * @Comment 	: 필터링사이트 등록 
	 */
	@RequestMapping(value="/admin/hotvod/regFilteringSite.do",method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> regFilteringSite(
			@ModelAttribute HotvodFilteringSiteVo filtSiteVo) throws Exception {
		return service.regFilteringSite(filtSiteVo);
	}

	/**
	 * @Method Name : modFilteringSite
	 * @Comment 	: 필터링사이트 수정 
	 */
	@RequestMapping(value="/admin/hotvod/modFilteringSite.do",method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> modFilteringSite(
			@ModelAttribute HotvodFilteringSiteVo filtSiteVo) throws Exception {
		return service.modFilteringSite(filtSiteVo);
	}

	/**
	 * @Method Name : delFilteringSite
	 * @Comment 	: 필터링사이트 삭제 
	 */
	@RequestMapping(value="/admin/hotvod/delFilteringSite.do",method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> delFilteringSite(
			@ModelAttribute HotvodFilteringSiteVo filtSiteVo) throws Exception {
		return service.delFilteringSite(filtSiteVo);
	}
	
	/**
	 * 검수단말기 정보 캐쉬
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/hotvod/testServerUserCache", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> testServerUserCache(HttpServletRequest request, HttpServletResponse response ) throws Exception {

		final CLog cLog = new CLog(applyLogger, request.getRemoteAddr(), request.getRequestURI(), request.getMethod());
		final CUDResult result = new CUDResult();
		String loginUser = CookieUtil.getCookieUserID(request);

		try {
			cLog.startLog("화제동영상 검수단말기 즉시 적용", loginUser);
			String fileLockPath = SmartUXProperties.getProperty("SmartUXTestUserDao.refreshTestUser.fileLock");
			int waitTime = NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.hotvodSbcWait"), 15);
			
			final String host = SmartUXProperties.getProperty("cache.sync.public.host");
			final String port = SmartUXProperties.getProperty("cache.sync.public.port");

			FileProcessLockFactory.start(fileLockPath, waitTime, new FileProcessFunction("화제동영상 검수 단말 즉시적용", ResultCode.ApplyRequestFail){
				@Override
				public void run() throws Exception {
					
					//즉시적용 호출 시 sleep 값 추가 -> DB분리건으로 OGG가 10초 이상 걸리는 경우가 있어 즉시적용 시 반영이 안돼서 추가함
					Thread.sleep(Integer.parseInt(org.apache.commons.lang.StringUtils.defaultIfEmpty(SmartUXProperties.getProperty("apply.cache.call.sleep"), "10000")));
					
					RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
							host, port,
							SmartUXProperties.getProperty("SmartUXTestUserDao.refreshTestUser.url"),
							StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.method"), "POST"),
							StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"),
							MediaType.APPLICATION_JSON.toString(),
							NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.hotvodSbcTimeout"), 900000)).getResponseBody());
					
					result.setFlag(codeMsg.getCode());
					result.setMessage(codeMsg.getMessage());
					cLog.middleLog("화제동영상 검수 단말 즉시적용", "SMARTUX SBC", codeMsg.getCode(), codeMsg.getMessage());
				}
			});
		} catch (MimsCommonException e) {
			cLog.warnLog("화제동영상 검수단말기 즉시 적용", "실패", e.getFlag(), e.getMessage(), e.getLocalizedMessage());
			result.setFlag(e.getFlag());
			result.setMessage(e.getMessage());
		}  finally {
			cLog.endLog("화제동영상 검수단말기 즉시 적용", loginUser, result.getFlag(), result.getMessage());
		}
		String resultMsg = "{\"flag\" : \"" + result.getFlag() + "\", \"message\" : \"" + result.getMessage() + "\"}";
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(resultMsg).toString(), responseHeaders, HttpStatus.OK);

	}
	
	/**
	 * CJ 서버 아이들나라 추가 2019.12.17 :  CJ 서버 검수단말기 정보 즉시적용
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/hotvod/setCjTestDeviceCache", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> setCjTestDeviceCache(HttpServletRequest request, HttpServletResponse response ) throws Exception {
		
		final CLog cLog = new CLog(applyLogger, request.getRemoteAddr(), request.getRequestURI(), request.getMethod());
		final CUDResult result = new CUDResult();
		String loginUser = CookieUtil.getCookieUserID(request);
		
		try {
			cLog.startLog("CJ 화제동영상 검수단말기 즉시 적용", loginUser);
			String fileLockPath = SmartUXProperties.getProperty("SmartUXTestUserDao.refreshTestUser.fileLock");
			int waitTime = NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.hotvodSbcWait"), 15);
			
			final String host = SmartUXProperties.getProperty("cache.sync.public.cj.host");
			final String port = SmartUXProperties.getProperty("cache.sync.public.cj.port");
			
			FileProcessLockFactory.start(fileLockPath, waitTime, new FileProcessFunction("CJ 화제동영상 검수 단말 즉시적용", ResultCode.ApplyRequestFail){
				@Override
				public void run() throws Exception {
					
					//즉시적용 호출 시 sleep 값 추가 -> DB분리건으로 OGG가 10초 이상 걸리는 경우가 있어 즉시적용 시 반영이 안돼서 추가함
					Thread.sleep(Integer.parseInt(org.apache.commons.lang.StringUtils.defaultIfEmpty(SmartUXProperties.getProperty("apply.cache.call.sleep"), "10000")));
					
					RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
							host, port,
							SmartUXProperties.getProperty("SmartUXTestUserDao.refreshCjTestUser.url"),
							StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.method"), "POST"),
							StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"),
							MediaType.APPLICATION_JSON.toString(),
							NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.hotvodSbcTimeout"), 900000)).getResponseBody());
					
					result.setFlag(codeMsg.getCode());
					result.setMessage(codeMsg.getMessage());
					cLog.middleLog("CJ 화제동영상 검수 단말 즉시적용", "SMARTUX SBC", codeMsg.getCode(), codeMsg.getMessage());
				}
			});
		} catch (MimsCommonException e) {
			cLog.warnLog("CJ 화제동영상 검수단말기 즉시 적용", "실패", e.getFlag(), e.getMessage(), e.getLocalizedMessage());
			result.setFlag(e.getFlag());
			result.setMessage(e.getMessage());
		}  finally {
			cLog.endLog("CJ 화제동영상 검수단말기 즉시 적용", loginUser, result.getFlag(), result.getMessage());
		}
		String resultMsg = "{\"flag\" : \"" + result.getFlag() + "\", \"message\" : \"" + result.getMessage() + "\"}";
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(resultMsg).toString(), responseHeaders, HttpStatus.OK);
		
	}
}