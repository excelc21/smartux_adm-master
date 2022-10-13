package com.dmi.smartux.admin.hotvod.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
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
import com.dmi.commons.utility.ExcelWorkBookFactory;
import com.dmi.commons.utility.HttpClientUtils;
import com.dmi.commons.utility.RestfulUtils;
import com.dmi.smartux.admin.hotvod.service.HotvodIptvService;
import com.dmi.smartux.admin.hotvod.service.HotvodService;
import com.dmi.smartux.admin.hotvod.tag.HotvodUtil;
import com.dmi.smartux.admin.hotvod.vo.HotvodConst;
import com.dmi.smartux.admin.hotvod.vo.HotvodContentIdListVo;
import com.dmi.smartux.admin.hotvod.vo.HotvodContentVO;
import com.dmi.smartux.admin.hotvod.vo.HotvodExcelVo;
import com.dmi.smartux.admin.hotvod.vo.HotvodSearchVO;
import com.dmi.smartux.admin.hotvod.vo.HotvodSiteVO;
import com.dmi.smartux.admin.hotvod.vo.UpdateHotvodCateApiVo;
import com.dmi.smartux.admin.imcs.service.ImcsService;
import com.dmi.smartux.admin.login.service.LoginService;
import com.dmi.smartux.common.exception.ExceptionHandler;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.CookieUtil;
import com.dmi.smartux.common.util.FileUtil;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.util.HTMLCleaner;
import com.dmi.smartux.common.vo.CUDResult;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

/**
 * 화제동영상 - Controller
 * @author JKJ
 */
@SuppressWarnings("static-access")
@Controller
public class HotvodController {

	private final Log logger = LogFactory.getLog(this.getClass());

	private final Log applyLogger = LogFactory.getLog("adminApplyCacheHistroy");

	@Autowired
	HotvodService service;
	
	@Autowired
	HotvodIptvService hotvodIptvService;
	
	@Autowired
	LoginService loginService;
	
	@Autowired
	ImcsService imcsService;

	@RequestMapping(value="/admin/hotvod/hotvodApplyCache", method=RequestMethod.POST)
	public ResponseEntity<String> applyCache(HttpServletRequest request, HttpServletResponse response ) throws Exception {

		final CLog cLog = new CLog(applyLogger, request.getRemoteAddr(), request.getRequestURI(), request.getMethod());
		final String loginUser = CookieUtil.getCookieUserID(request);
		final CUDResult result = new CUDResult();

		String fileLockPath = SmartUXProperties.getProperty("SmartUXHotvodDao.refreshHotvod.fileLock");
		int waitTime = NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.wait"), 5);

		try {
			cLog.startLog("화제동영상 즉시적용", loginUser);
			
			final String host = SmartUXProperties.getProperty("cache.sync.public.host");
			final String port = SmartUXProperties.getProperty("cache.sync.public.port");
			
			FileProcessLockFactory.start(fileLockPath, waitTime, new FileProcessFunction("즉시적용", ResultCode.ApplyRequestFail) {
				@Override
				public void run() throws Exception {
					
					//즉시적용 호출 시 sleep 값 추가 -> DB분리건으로 OGG가 10초 이상 걸리는 경우가 있어 즉시적용 시 반영이 안돼서 추가함
					Thread.sleep(Integer.parseInt(org.apache.commons.lang.StringUtils.defaultIfEmpty(SmartUXProperties.getProperty("apply.cache.call.sleep"), "10000")));
					
					RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
							host, port,
							SmartUXProperties.getProperty("SmartUXHotvodDao.refreshHotvod.url"),
							StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.method"), "POST"),
							StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"),
							MediaType.APPLICATION_JSON.toString(),
							NumberUtils.toInt(SmartUXProperties.getProperty("SmartUXHotvodDao.refreshHotvod.timeout"), 60000)).getResponseBody());

					result.setFlag(codeMsg.getCode());
					result.setMessage(codeMsg.getMessage());
					cLog.middleLog("화제동영상 즉시적용", "API 호출 성공", codeMsg.getCode(), codeMsg.getMessage());
				}
			});
		} catch (MimsCommonException e) {
			cLog.warnLog("화제동영상 즉시적용", "실패", e.getFlag(), e.getMessage(), e.getLocalizedMessage());
			result.setFlag(e.getFlag());
			result.setMessage(e.getMessage());
		} finally {
			cLog.endLog("화제동영상 즉시적용", loginUser, result.getFlag(), result.getMessage());
		}
		
		String resultMsg = "{\"flag\" : \"" + result.getFlag() + "\", \"message\" : \"" + result.getMessage() + "\"}";
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(resultMsg).toString(), responseHeaders, HttpStatus.OK);
	}
	
	/**
	 * CJ 서버 아이들나라 추가 2019.12.17 : CJ서버 화제동영상 컨텐츠 즉시적용 추가
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/hotvod/cjHotvodApplyCache", method=RequestMethod.POST)
	public ResponseEntity<String> cjApplyCache(HttpServletRequest request, HttpServletResponse response ) throws Exception {
		
		final CLog cLog = new CLog(applyLogger, request.getRemoteAddr(), request.getRequestURI(), request.getMethod());
		final String loginUser = CookieUtil.getCookieUserID(request);
		final CUDResult result = new CUDResult();
		
		String fileLockPath = SmartUXProperties.getProperty("SmartUXHotvodDao.refreshHotvod.fileLock");
		int waitTime = NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.wait"), 5);
		
		try {
			cLog.startLog("CJ 화제동영상 즉시적용", loginUser);
			
			final String host = SmartUXProperties.getProperty("cache.sync.public.cj.host");
			final String port = SmartUXProperties.getProperty("cache.sync.public.cj.port");
			
			FileProcessLockFactory.start(fileLockPath, waitTime, new FileProcessFunction("CJ 화제동영상 즉시적용", ResultCode.ApplyRequestFail) {
				@Override
				public void run() throws Exception {
					
					//즉시적용 호출 시 sleep 값 추가 -> DB분리건으로 OGG가 10초 이상 걸리는 경우가 있어 즉시적용 시 반영이 안돼서 추가함
					Thread.sleep(Integer.parseInt(org.apache.commons.lang.StringUtils.defaultIfEmpty(SmartUXProperties.getProperty("apply.cache.call.sleep"), "10000")));
					
					RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
							host, port,
							SmartUXProperties.getProperty("SmartUXHotvodDao.refreshCjHotvod.url"),
							StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.method"), "POST"),
							StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"),
							MediaType.APPLICATION_JSON.toString(),
							NumberUtils.toInt(SmartUXProperties.getProperty("SmartUXHotvodDao.refreshHotvod.timeout"), 60000)).getResponseBody());
					
					result.setFlag(codeMsg.getCode());
					result.setMessage(codeMsg.getMessage());
					cLog.middleLog("CJ 화제동영상 즉시적용", "API 호출 성공", codeMsg.getCode(), codeMsg.getMessage());
				}
			});
		} catch (MimsCommonException e) {
			cLog.warnLog("CJ 화제동영상 즉시적용", "실패", e.getFlag(), e.getMessage(), e.getLocalizedMessage());
			result.setFlag(e.getFlag());
			result.setMessage(e.getMessage());
		} finally {
			cLog.endLog("CJ 화제동영상 즉시적용", loginUser, result.getFlag(), result.getMessage());
		}
		
		String resultMsg = "{\"flag\" : \"" + result.getFlag() + "\", \"message\" : \"" + result.getMessage() + "\"}";
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(resultMsg).toString(), responseHeaders, HttpStatus.OK);
	}

	/**
	 * 컨텐츠 목록
	 * @param findName
	 * @param findValue
	 * @param pageNum
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/hotvod/contentList.do",method=RequestMethod.GET)
	public String contentList(
			@RequestParam(value="findName", required=false, defaultValue="") String findName,
			@RequestParam(value="findValue", required=false, defaultValue="") String findValue,
			@RequestParam(value="delYn", required=false, defaultValue="N") String delYn,
			@RequestParam(value="pageNum", required=false, defaultValue="1") String pageNum,
			@RequestParam(value="pageSize", required=false, defaultValue="20") String pageSize,
			@RequestParam(value="serviceType", required=false, defaultValue="") String serviceType,
			@RequestParam(value="isLock", required=false, defaultValue="false") Boolean isLock,
			HttpServletRequest request,
			Model model) throws Exception {
		HTMLCleaner cleaner = new HTMLCleaner();
		findName 	= cleaner.clean(findName);
		findValue = cleaner.clean(findValue);
		delYn = cleaner.clean(delYn);
		pageNum = cleaner.clean(pageNum);

		String cookieID = CookieUtil.getCookieUserID(request);

		HotvodSearchVO hotvodSearchVO = new HotvodSearchVO();

		//페이지 
		hotvodSearchVO.setPageSize(Integer.parseInt(pageSize));
		hotvodSearchVO.setPageSize(GlobalCom.isNumber(hotvodSearchVO.getPageSize(),10));
		hotvodSearchVO.setBlockSize(GlobalCom.isNumber(hotvodSearchVO.getBlockSize(),10));
		hotvodSearchVO.setPageNum(GlobalCom.isNumber(Integer.parseInt(GlobalCom.isNull(pageNum, "1")),1));
		//검색 
		hotvodSearchVO.setFindName(GlobalCom.isNull(findName, "CONTENT_NAME"));
		hotvodSearchVO.setFindValue(findValue);
		hotvodSearchVO.setDelYn(delYn);
		hotvodSearchVO.setServiceType(serviceType);
		hotvodSearchVO.setMultiServiceType(HotvodUtil.toDecStringByService(serviceType));
		hotvodSearchVO.setDefaultServiceDec(HotvodUtil.toDecStringByAllService());
		hotvodSearchVO.setIsLock(isLock);

		List<HotvodContentVO> list = service.contentList(hotvodSearchVO);
		hotvodSearchVO.setPageCount(service.contentListCnt(hotvodSearchVO));

		model.addAttribute("vo", hotvodSearchVO);
		model.addAttribute("list", list);
		model.addAttribute("pageSize", hotvodSearchVO.getPageSize());
		
		String hotvodServiceList = GlobalCom.isNull(SmartUXProperties.getProperty("hotvod.service.list"), HotvodConst.DEFAULT_HOTVOD_SERVICE_LIST);
		model.addAttribute("hotvodServiceList", hotvodServiceList);

		return "/admin/hotvod/contentList";
	}

	/**
	 * 컨텐츠 상세/수정
	 * @param content_id
	 * @param findName
	 * @param findValue
	 * @param pageNum
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/hotvod/contentDetail.do",method=RequestMethod.GET)
	public String contentDetail(
			@RequestParam(value="content_id", required=false, defaultValue="") String content_id,
			@RequestParam(value="content_type", required=false, defaultValue="") String content_type,
			@RequestParam(value="findName", required=false, defaultValue="") String findName,
			@RequestParam(value="findValue", required=false, defaultValue="") String findValue,
			@RequestParam(value="delYn", required=false, defaultValue="") String delYn,
			@RequestParam(value="rootId", required=false, defaultValue="") String rootId,
			@RequestParam(value="pageNum", required=false, defaultValue="1") String pageNum,
			@RequestParam(value="pageSize", required=false, defaultValue="20") String pageSize,
			@RequestParam(value="result", required=false, defaultValue="") String result,
			@RequestParam(value="serviceType", required=false, defaultValue="") String serviceType,
			@RequestParam(value="isLock", required=false, defaultValue="false") Boolean isLock,
			HttpServletRequest request,
			Model model) throws Exception {
		HTMLCleaner cleaner = new HTMLCleaner();
		content_id  = cleaner.clean(content_id);
		findName 	= cleaner.clean(findName);
		findValue   = cleaner.clean(findValue);
		delYn   = cleaner.clean(delYn);
		rootId   = cleaner.clean(rootId);
		pageNum     = cleaner.clean(pageNum);
		pageSize     = cleaner.clean(pageSize);
		result      = cleaner.clean(result);
		String cookieID = CookieUtil.getCookieUserID(request);

		HotvodSearchVO hotvodSearchVO = new HotvodSearchVO();

		hotvodSearchVO.setContent_id(content_id);
		hotvodSearchVO.setFindName(findName);
		hotvodSearchVO.setFindValue(findValue);
		hotvodSearchVO.setDelYn(delYn);
		hotvodSearchVO.setRootId(rootId);
		hotvodSearchVO.setPageNum(Integer.parseInt(pageNum));
		hotvodSearchVO.setPageSize(Integer.parseInt(pageSize));
		hotvodSearchVO.setServiceType(serviceType);
		hotvodSearchVO.setMultiServiceType(HotvodUtil.toDecStringByService(serviceType));
		hotvodSearchVO.setDefaultServiceDec(HotvodUtil.toDecStringByAllService());
		hotvodSearchVO.setIsLock(isLock);

		HotvodContentVO content = new HotvodContentVO();

		if (HotvodConst.CONTENT_TYPE.CATEGORY.equals(content_type)) {
			content = service.categoryDetail(hotvodSearchVO);
		} else {
			content = service.contentDetail(hotvodSearchVO);
		}
		
		List<HotvodSiteVO> siteList = hotvodIptvService.getSiteList();

		Long hdtvSize = Long.parseLong(GlobalCom.isNull(SmartUXProperties.getProperty("hotvod.hdtv.image"),"0"));
		Long iptvSize = Long.parseLong(GlobalCom.isNull(SmartUXProperties.getProperty("hotvod.iptv.image"),"0"));

		//역사 항목 추가
		String cate_info="";
		if(HotvodConst.CONTENT_TYPE.IMG_TEXT.equals(content.getContent_type())) {
			String cate=content.getContent_url();
			if(cate!=null){
				String cate_ay[]=cate.split("\\|");
				for (String cate_no : cate_ay) {
					Map<String,String> cate_map = service.getCateInfo(cate_no);
					if(cate_map==null){
						//cate_info+=cate_no+"^"+"error"+"|";
					}else{
						cate_info+=cate_no+"^"+cate_map.get("CATE_NM")+"|";
					}
				}
			}
		}
		model.addAttribute("cate_info", cate_info);
		//역사 항목 추가

		model.addAttribute("vo", hotvodSearchVO);
		model.addAttribute("content", content);
		model.addAttribute("siteList", siteList);
		model.addAttribute("result", result);

		model.addAttribute("hdtvSize", (hdtvSize/1024)+"KB");
		model.addAttribute("iptvSize", (iptvSize/1024)+"KB");

		//-------이미지 체크----------//
		int imgHdtvSize = Integer.parseInt(GlobalCom.isNull(SmartUXProperties.getProperty("hotvod.hdtv.size"),""))/1024;
		int imgIptvSize = Integer.parseInt(GlobalCom.isNull(SmartUXProperties.getProperty("hotvod.iptv.size"),""))/1024;
		String imgFormat = GlobalCom.isNull(SmartUXProperties.getProperty("hotvod.format"),"");
		model.addAttribute("imgHdtvSize", imgHdtvSize);
		model.addAttribute("imgIptvSize", imgIptvSize);
		model.addAttribute("imgFormat", imgFormat);
		//-------이미지 체크----------//

		String hotvodBadgeList = GlobalCom.isNull(SmartUXProperties.getProperty("hotvod.badge.list"), HotvodConst.DEFAULT_HOTVOD_BADGE_LIST);
		model.addAttribute("hotvodBadgeList", hotvodBadgeList);
		
		String hotvodServiceList = GlobalCom.isNull(SmartUXProperties.getProperty("hotvod.service.list"), HotvodConst.DEFAULT_HOTVOD_SERVICE_LIST);
		model.addAttribute("hotvodServiceList", hotvodServiceList);

		return "/admin/hotvod/contentDetail";
	}

	/**
	 * 컨텐츠 수정
	 * @param hotvodContentVO
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/hotvod/contentUpdate.do",method=RequestMethod.POST)
	public String contentUpdate(
			@RequestParam(value="file", required=false, defaultValue="") MultipartFile uploadfile,
			@RequestParam(value="file_tv", required=false, defaultValue="") MultipartFile uploadfile_tv,
			@RequestParam(value="isLock", required=false, defaultValue="false") Boolean isLock,
			HotvodContentVO hotvodContentVO,
			HotvodSearchVO hotvodSearchVO,
			HttpServletRequest request,
			Model model) throws Exception {
			
		logger.info(hotvodContentVO.toString());
		
		String cookieID = CookieUtil.getCookieUserID(request);

		String tmp_file_name = "";
		String bg_img_file = "";
		File img_file = null;
		String result = "0000";
		String contentInfo = hotvodContentVO.getContent_info();
		String contentName = hotvodContentVO.getContent_name();
		
		contentInfo = contentInfo.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
		contentName = contentName.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
		
		hotvodContentVO.setContent_info(contentInfo);
		hotvodContentVO.setContent_name(contentName);
		
		boolean imgCheck = true;
		if(!FileUtil.fileSizeCheck(uploadfile,SmartUXProperties.getProperty("hotvod.hdtv.image"))){
			imgCheck = false;
			logger.info("[contentUpdate]uploadfile over size : " + uploadfile.getSize());
		}else if(!FileUtil.fileSizeCheck(uploadfile_tv,SmartUXProperties.getProperty("hotvod.iptv.image"))){
			imgCheck = false;
			logger.info("[contentUpdate]uploadfile_tv over size : " + uploadfile_tv.getSize());
		}

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		String year = sdf.format(date);
		sdf = new SimpleDateFormat("MM");
		String month = sdf.format(date);
		String subPath = year + "/" + month + "/";
		String filePath = SmartUXProperties.getProperty("hotvod.upload.dir") + subPath;
		File pathChk = new File(filePath);

		if(!pathChk.exists()){
			pathChk.mkdirs();
		}

		if(imgCheck){
			if (uploadfile.getSize() != 0L) {
				tmp_file_name = uploadfile.getOriginalFilename();
				String ext = tmp_file_name.substring(	tmp_file_name.lastIndexOf(".") + 1,	tmp_file_name.length()); // 확장자 구하기
				ext = ext.toLowerCase();
				bg_img_file = subPath + Long.toString(System.currentTimeMillis()) + "."+ ext; // 시스템타임.확장자 구조로 한다
				String newFilename=SmartUXProperties.getProperty("hotvod.upload.dir")+ bg_img_file;

				img_file = new File(newFilename);
				uploadfile.transferTo(img_file);

				hotvodContentVO.setContent_img(bg_img_file);
			}

			if (uploadfile_tv.getSize() != 0L) {
				tmp_file_name = uploadfile_tv.getOriginalFilename();
				String ext = tmp_file_name.substring(	tmp_file_name.lastIndexOf(".") + 1,	tmp_file_name.length()); // 확장자 구하기
				ext = ext.toLowerCase();
				bg_img_file = subPath + Long.toString(System.currentTimeMillis()) + "."+ ext; // 시스템타임.확장자 구조로 한다
				if(bg_img_file.equals(hotvodContentVO.getContent_img())){
					bg_img_file = subPath + Long.toString(System.currentTimeMillis()+1) + "."+ ext;
				}
				String newFilename=SmartUXProperties.getProperty("hotvod.upload.dir")+ bg_img_file;

				img_file = new File(newFilename);
				uploadfile_tv.transferTo(img_file);

				hotvodContentVO.setContent_img_tv(bg_img_file);
			}

			hotvodContentVO.setMod_id(cookieID);

			if(HotvodConst.CONTENT_TYPE.CATEGORY.equals(hotvodContentVO.getContent_type()) && "Y".equals(hotvodContentVO.getDel_yn())){
				service.contentDetailDelete(hotvodContentVO.getContent_id(), hotvodContentVO.getContent_type(), cookieID);
			}
			if (!HotvodConst.CONTENT_TYPE.CATEGORY.equals(hotvodContentVO.getContent_type())) {
				service.contentUpdate(hotvodContentVO);
			} else {
				service.categoryUpdate(hotvodContentVO);
			}
			
		}else{
			result = "0002";
		}

		model.addAttribute("content_id", hotvodSearchVO.getContent_id());
		model.addAttribute("findName", hotvodSearchVO.getFindName());
		model.addAttribute("findValue", hotvodSearchVO.getFindValue());
		model.addAttribute("delYn", hotvodSearchVO.getDelYn());
		model.addAttribute("pageNum", hotvodSearchVO.getPageNum());
		model.addAttribute("pageSize", hotvodSearchVO.getPageSize());

		String domain = SmartUXProperties.getProperty("smartux.domain.http");
		return "redirect:"+domain+"/smartux_adm/admin/hotvod/contentDetail.do?result=" + result+"&isLock="+isLock+"&content_type="+hotvodContentVO.getContent_type();
	}

	/**
	 * 컨텐츠 등록 폼
	 * @param findName
	 * @param findValue
	 * @param pageNum
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/hotvod/contentInsertForm.do",method=RequestMethod.GET)
	public String contentInsertForm(
			@RequestParam(value="findName", required=false, defaultValue="") String findName,
			@RequestParam(value="findValue", required=false, defaultValue="") String findValue,
			@RequestParam(value="pageNum", required=false, defaultValue="1") String pageNum,
			@RequestParam(value="result", required=false, defaultValue="") String result,
			@RequestParam(value="serviceType", required=false, defaultValue="") String serviceType,
			@RequestParam(value="isLock", required=false, defaultValue="false") Boolean isLock,
			HttpServletRequest request,
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
		hotvodSearchVO.setServiceType(serviceType);
		hotvodSearchVO.setIsLock(isLock);

		//컨텐츠 아이디 신규생성
		hotvodSearchVO.setContent_id(service.getContentId());

		List<HotvodSiteVO> siteList = hotvodIptvService.getSiteList();

		Long iptvSize = Long.parseLong(GlobalCom.isNull(SmartUXProperties.getProperty("hotvod.iptv.image"),"0"));

		model.addAttribute("vo", hotvodSearchVO);
		model.addAttribute("siteList", siteList);
		model.addAttribute("result", result);

		model.addAttribute("iptvSize", (iptvSize/1024)+"KB");
		//-------이미지 체크----------//
		int imgIptvSize = Integer.parseInt(GlobalCom.isNull(SmartUXProperties.getProperty("hotvod.iptv.size"),""))/1024;
		String imgFormat = GlobalCom.isNull(SmartUXProperties.getProperty("hotvod.format"),"");
		model.addAttribute("imgIptvSize", imgIptvSize);
		model.addAttribute("imgFormat", imgFormat);

		String hotvodBadgeList = GlobalCom.isNull(SmartUXProperties.getProperty("hotvod.badge.list"), HotvodConst.DEFAULT_HOTVOD_BADGE_LIST);
		model.addAttribute("hotvodBadgeList", hotvodBadgeList);
		
		//-------이미지 체크----------//
		return "/admin/hotvod/contentInsert";
	}

	/**
	 * 컨텐츠 등록
	 * @param hotvodContentVO
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/hotvod/contentInsert.do",method=RequestMethod.POST)
	public String contentInsert(
			@RequestParam(value="file_tv", required=false, defaultValue="") MultipartFile uploadfile_tv,	//컨텐츠 이미지IPTV
			HotvodContentVO hotvodContentVO,
			HttpServletRequest request,
			Model model) throws Exception {
		String cookieID = CookieUtil.getCookieUserID(request);
		
		String result = insertHotVodProc( uploadfile_tv, hotvodContentVO, cookieID);
		
		String domain = SmartUXProperties.getProperty("smartux.domain.https");
		return "redirect:"+domain+"/smartux_adm/admin/hotvod/contentInsertForm.do?result=" + result;
	}
	
	/**
	 * <pre>
	 * 화제동영상 컨텐츠 등록 
	 * </pre>
	 * @author medialog
	 * @date 2017. 9. 19.
	 * @method insertHotVodProc
	 * @return String
	 */
	private String insertHotVodProc(
			MultipartFile uploadfile_tv, 
			HotvodContentVO hotvodContentVO, String id) throws Exception {
		
		logger.info(hotvodContentVO.toString());
		
		String tmp_file_name = "";
		String bg_img_file = "";
		File img_file = null;
		String result = "0000";
	
		boolean imgCheck = true;
		if(!FileUtil.fileSizeCheck(uploadfile_tv,SmartUXProperties.getProperty("hotvod.iptv.image"))){
			imgCheck = false;
			logger.info("[contentInsert]uploadfile_tv over size : " + uploadfile_tv.getSize());
		}
	
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		String year = sdf.format(date);
		sdf = new SimpleDateFormat("MM");
		String month = sdf.format(date);
		String subPath = year + "/" + month + "/";
		String filePath = SmartUXProperties.getProperty("hotvod.upload.dir") + subPath;
		File pathChk = new File(filePath);
	
		if(!pathChk.exists()){
			pathChk.mkdirs();
		}
	
		if(imgCheck){
	
			if (uploadfile_tv != null && uploadfile_tv.getSize() != 0L) {
				tmp_file_name = uploadfile_tv.getOriginalFilename();
				String ext = tmp_file_name.substring(	tmp_file_name.lastIndexOf(".") + 1,	tmp_file_name.length()); // 확장자 구하기
				ext = ext.toLowerCase();
	
				bg_img_file = subPath + Long.toString(System.currentTimeMillis()) + "."+ ext; // 시스템타임.확장자 구조로 한다
				if(bg_img_file.equals(hotvodContentVO.getContent_img())){
					bg_img_file = subPath + Long.toString(System.currentTimeMillis()+1) + "."+ ext;
				}
				String newFilename=SmartUXProperties.getProperty("hotvod.upload.dir")+ bg_img_file;
	
				img_file = new File(newFilename);
				uploadfile_tv.transferTo(img_file);
	
				hotvodContentVO.setContent_img_tv(bg_img_file);
			}
			hotvodContentVO.setReg_id(id);
	
			if("0000".equals(result)){
				if( !HotvodConst.CONTENT_TYPE.CATEGORY.equals(hotvodContentVO.getContent_type()) ) {
					service.contentInsert(hotvodContentVO);
				}else{
					service.categoryInsert(hotvodContentVO);
				}
				
				//2021-04-22 BPAS 편성관리 추가
				try {
					String content_type = hotvodContentVO.getContent_type();
					if("M".equals(content_type)) {
						imcsService.insertImcs("I", "HOT", hotvodContentVO.getContent_id(), "ALB", hotvodContentVO.getAlbum_id(), "N");
					}else if("N".equals(content_type)) {
						imcsService.insertImcs("I", "HOT", hotvodContentVO.getContent_id(), "CAT", hotvodContentVO.getCategory_id(), "N");
					}
				}catch (Exception e) {
					logger.error("[insertImcs]["+e.getClass().getName()+"]["+e.getMessage()+"]");
				}
			}
		}else{
			result = "0002";
		}
		
		return result;
	}

	/**
	 * 컨텐츠 아이디 중복검사
	 * @param content_id
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/hotvod/contentIdChk.do",method=RequestMethod.POST)
	public ResponseEntity<String> contentIdChk(
			@RequestParam(value="content_id", required=false, defaultValue="") String content_id,
			HttpServletRequest request,
			Model model
	) throws Exception{

		String result = "";
		String resultcode = "";
		String resultmessage = "";
		String resultCount = "";

		try{
			resultCount = Integer.toString(service.contentIdChk(content_id));

			if(!"0".equals(resultCount)){
				content_id = service.getContentId();
			}
			resultcode = SmartUXProperties.getProperty("flag.success");
			resultmessage = SmartUXProperties.getProperty("message.success");
		} catch (Exception e) {
			logger.error("[contentIdChk]["+e.getClass().getName()+"]["+e.getMessage()+"]");		

			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}

		result = "{\"result\":{\"message\":\""+resultmessage+"\",\"flag\":\""+resultcode+"\",\"resultCount\":\""+resultCount+"\",\"content_id\":\""+content_id+"\"}}";

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(result).toString(), responseHeaders, HttpStatus.CREATED);
	}

	/**
	 * 컨텐츠 삭제
	 * @param content_id
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/hotvod/contentDelete.do",method=RequestMethod.POST)
	public ResponseEntity<String> contentDelete(
			@RequestParam(value="content_id", required=false, defaultValue="") String content_id,
			@RequestParam(value="content_type", required=false, defaultValue="") String content_type,
			@RequestParam(value="parent_id", required=false, defaultValue="") String parent_id,
			HttpServletRequest request,
			Model model
	) throws Exception{

		String result = "";
		String resultcode = "";
		String resultmessage = "";
		String mod_id = CookieUtil.getCookieUserID(request);

		try{
			if (HotvodConst.CONTENT_TYPE.CATEGORY.equals(content_type)) {
				service.categoryDelete(content_id, mod_id);
			} else {
				service.contentDelete(content_id, mod_id, parent_id);
			}
			
			resultcode = SmartUXProperties.getProperty("flag.success");
			resultmessage = SmartUXProperties.getProperty("message.success");
		} catch (Exception e) {
			logger.error("[contentDelete]["+e.getClass().getName()+"]["+e.getMessage()+"]");

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
	 * 카테고리 선택 팝업
	 * @param content_type
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/hotvod/openCategoryPop.do",method=RequestMethod.GET)
	public String openCategoryPop(
			@RequestParam(value="content_id", required=false, defaultValue="") String content_id,
			@RequestParam(value="parent_id", required=false, defaultValue="") String parent_id,
			@RequestParam(value="content_type", required=false, defaultValue="") String content_type,
			@RequestParam(value="choice_type", required=false, defaultValue="") String choice_type,
			HttpServletRequest request,
			Model model) throws Exception {

		HTMLCleaner cleaner = new HTMLCleaner();
		content_type = cleaner.clean(content_type);

		List<HotvodContentVO> result = service.getCategoryList(content_type);

		model.addAttribute("categoryResult", result);//카테고리
		model.addAttribute("content_id", content_id);
		model.addAttribute("parent_id", parent_id);
		model.addAttribute("choice_type", choice_type);

		return "/admin/hotvod/categoryPop";
	}

	/**
	 * 순서 바꾸기 팝업
	 * @param content_id
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/hotvod/openChangePop.do",method=RequestMethod.GET)
	public String openChangePop(
			@RequestParam(value="content_id", required=false, defaultValue="") String content_id,
			@RequestParam(value="findName", required=false, defaultValue="") String findName,
			@RequestParam(value="findValue", required=false, defaultValue="") String findValue,
			@RequestParam(value="delYn", required=false, defaultValue="N") String delYn,
			@RequestParam(value="parent_id", required=false, defaultValue="") String parent_id,
			@RequestParam(value="pageNum", required=false, defaultValue="1") String pageNum,
			@RequestParam(value="pageSize", required=false, defaultValue="20") String pageSize,
			@RequestParam(value="serviceType", required=false, defaultValue="") String serviceType,
			@RequestParam(value="isLock", required=false, defaultValue="false") Boolean isLock,
			Model model) throws Exception {

		HTMLCleaner cleaner = new HTMLCleaner();
		content_id = cleaner.clean(content_id);
		findName = cleaner.clean(findName);
		findValue = cleaner.clean(findValue);
		delYn = cleaner.clean(delYn);
		parent_id = cleaner.clean(parent_id);
		pageNum = cleaner.clean(pageNum);
		pageSize = cleaner.clean(pageSize);
		
		HotvodSearchVO hotvodSearchVO = new HotvodSearchVO();
		//검색
		hotvodSearchVO.setContent_id(content_id);
		hotvodSearchVO.setFindName(findName);
		hotvodSearchVO.setFindValue(findValue);
		hotvodSearchVO.setDelYn(delYn);
		hotvodSearchVO.setParent_id(parent_id);
		hotvodSearchVO.setServiceType(serviceType);
		hotvodSearchVO.setIsLock(isLock);

		//페이지 
		hotvodSearchVO.setPageNum(Integer.parseInt(pageNum));
		hotvodSearchVO.setPageSize(Integer.parseInt(pageSize));
		hotvodSearchVO.setPageSize(GlobalCom.isNumber(hotvodSearchVO.getPageSize(),10));
		hotvodSearchVO.setBlockSize(GlobalCom.isNumber(hotvodSearchVO.getBlockSize(),10));
		hotvodSearchVO.setPageNum(GlobalCom.isNumber(Integer.parseInt(GlobalCom.isNull(pageNum, "1")),1));

		List<HotvodContentVO> list = service.getChangeList(content_id);

		model.addAttribute("list", list);//순서바꾸기 목록
		model.addAttribute("vo", hotvodSearchVO);

		return "/admin/hotvod/changePop";
	}

	/**
	 * 순서 바꾸기 저장
	 * @param content_id
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/hotvod/changeSave", method = RequestMethod.PUT)
	@ResponseBody
	public String changeSave(
			@RequestBody final MultiValueMap<String, String> data,
			HttpServletRequest request) throws Exception {
		String resultCode;
		String resultMsg;

		try {

			List<String> optionArray = data.get("optionArray[]");
			List<String> parArray = data.get("parArray[]");
			
			String id = CookieUtil.getCookieUserID(request);
			//mService.changeOrder(optionArray);
			service.changeSave(optionArray, parArray, id);
			resultCode = SmartUXProperties.getProperty("flag.success");
			resultMsg = SmartUXProperties.getProperty("message.success");
		} catch (Exception e) {
			ExceptionHandler handler = new ExceptionHandler(e);
			resultCode = handler.getFlag();
			resultMsg = handler.getMessage();
		}

		return "{\"flag\" : \"" + resultCode + "\", \"message\" : \"" + resultMsg + "\"}";
	}



	/**
	 * 하위 목록 팝업
	 * @param content_id
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/hotvod/openSubListPop.do",method=RequestMethod.GET)
	public String openSubListPop(
			@RequestParam(value="content_id", required=false, defaultValue="") String content_id,
			@RequestParam(value="findName", required=false, defaultValue="") String findName,
			@RequestParam(value="findValue", required=false, defaultValue="") String findValue,
			@RequestParam(value="delYn", required=false, defaultValue="N") String delYn,
			@RequestParam(value="parent_id", required=false, defaultValue="") String parent_id,
			@RequestParam(value="pageNum", required=false, defaultValue="1") String pageNum,
			@RequestParam(value="pageSize", required=false, defaultValue="20") String pageSize,
			@RequestParam(value="serviceType", required=false, defaultValue="") String serviceType,
			@RequestParam(value="isLock", required=false, defaultValue="false") Boolean isLock,
			HttpServletRequest request,
			Model model) throws Exception {

		HTMLCleaner cleaner = new HTMLCleaner();
		content_id = cleaner.clean(content_id);
		findName = cleaner.clean(findName);
		findValue = cleaner.clean(findValue);
		delYn = cleaner.clean(delYn);
		parent_id = cleaner.clean(parent_id);
		pageNum = cleaner.clean(pageNum);
		pageSize = cleaner.clean(pageSize);
		String cookieID = CookieUtil.getCookieUserID(request);

		HotvodSearchVO hotvodSearchVO = new HotvodSearchVO();
		//검색
		hotvodSearchVO.setContent_id(content_id);
		hotvodSearchVO.setFindName(findName);
		hotvodSearchVO.setFindValue(findValue);
		hotvodSearchVO.setDelYn(delYn);
		hotvodSearchVO.setParent_id(parent_id);
		hotvodSearchVO.setServiceType(serviceType);
		hotvodSearchVO.setIsLock(isLock);

		//페이지 
		hotvodSearchVO.setPageNum(Integer.parseInt(pageNum));
		hotvodSearchVO.setPageSize(Integer.parseInt(pageSize));
		hotvodSearchVO.setPageSize(GlobalCom.isNumber(hotvodSearchVO.getPageSize(),10));
		hotvodSearchVO.setBlockSize(GlobalCom.isNumber(hotvodSearchVO.getBlockSize(),10));
		hotvodSearchVO.setPageNum(GlobalCom.isNumber(Integer.parseInt(GlobalCom.isNull(pageNum, "1")),1));

		List<HotvodContentVO> list = service.getSubList(hotvodSearchVO);
		hotvodSearchVO.setPageCount(service.getSubListCnt(hotvodSearchVO));

		model.addAttribute("list", list);
		model.addAttribute("vo", hotvodSearchVO);
		model.addAttribute("pageSize", hotvodSearchVO.getPageSize());
		
		return "/admin/hotvod/subListPop";
	}

	/**
	 * 하위 목록 상세
	 * @param content_id
	 * @param findName
	 * @param findValue
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/hotvod/subDetail.do",method=RequestMethod.GET)
	public String subDetail(
			@RequestParam(value="content_id", required=false, defaultValue="") String content_id,
			@RequestParam(value="content_type", required=false, defaultValue="") String content_type,
			@RequestParam(value="findName", required=false, defaultValue="") String findName,
			@RequestParam(value="findValue", required=false, defaultValue="") String findValue,
			@RequestParam(value="delYn", required=false, defaultValue="") String delYn,
			@RequestParam(value="rootId", required=false, defaultValue="") String rootId,
			@RequestParam(value="parent_id", required=false, defaultValue="") String parent_id,
			@RequestParam(value="pageNum", required=false, defaultValue="1") String pageNum,
			@RequestParam(value="result", required=false, defaultValue="1") String result,
			@RequestParam(value="serviceType", required=false, defaultValue="") String serviceType,
			@RequestParam(value="isLock", required=false, defaultValue="false") Boolean isLock,
			HttpServletRequest request,
			Model model) throws Exception {
		HTMLCleaner cleaner = new HTMLCleaner();
		content_id  = cleaner.clean(content_id);
		findName 	= cleaner.clean(findName);
		findValue   = cleaner.clean(findValue);
		delYn   = cleaner.clean(delYn);
		rootId   = cleaner.clean(rootId);
		parent_id   = cleaner.clean(parent_id);
		pageNum   = cleaner.clean(pageNum);
		result   = cleaner.clean(result);
		String cookieID = CookieUtil.getCookieUserID(request);

		HotvodSearchVO hotvodSearchVO = new HotvodSearchVO();

		hotvodSearchVO.setContent_id(content_id);
		hotvodSearchVO.setFindName(findName);
		hotvodSearchVO.setFindValue(findValue);
		hotvodSearchVO.setDelYn(delYn);
		hotvodSearchVO.setParent_id(parent_id);
		hotvodSearchVO.setRootId(rootId);
		hotvodSearchVO.setPageNum(Integer.parseInt(pageNum));
		hotvodSearchVO.setServiceType(serviceType);
		hotvodSearchVO.setIsLock(isLock);

		HotvodContentVO content = new HotvodContentVO();
		
		if (HotvodConst.CONTENT_TYPE.CATEGORY.equals(content_type)) {
			content = service.categoryDetail(hotvodSearchVO);
		} else {
			content = service.contentDetail(hotvodSearchVO);
		}
		
		List<HotvodSiteVO> siteList = hotvodIptvService.getSiteList();

		Long iptvSize = Long.parseLong(GlobalCom.isNull(SmartUXProperties.getProperty("hotvod.iptv.image"),"0"));

		//역사 항목 추가
		String cate_info="";
		if(HotvodConst.CONTENT_TYPE.IMG_TEXT.equals(content.getContent_type())) {
			String cate=content.getContent_url();
			if(cate!=null){
				String cate_ay[]=cate.split("\\|");
				for (String cate_no : cate_ay) {
					Map<String,String> cate_map = service.getCateInfo(cate_no);
					if(cate_map==null){
						//cate_info+=cate_no+"^"+"error"+"|";
					}else{
						cate_info+=cate_no+"^"+cate_map.get("CATE_NM")+"|";
					}
				}
			}
		}
		model.addAttribute("cate_info", cate_info);
		//역사 항목 추가

		model.addAttribute("vo", hotvodSearchVO);
		model.addAttribute("content", content);
		model.addAttribute("siteList", siteList);
		model.addAttribute("result", result);

		model.addAttribute("iptvSize", (iptvSize/1024)+"KB");
		//-------이미지 체크----------//
		int imgIptvSize = Integer.parseInt(GlobalCom.isNull(SmartUXProperties.getProperty("hotvod.iptv.size"),""))/1024;
		String imgFormat = GlobalCom.isNull(SmartUXProperties.getProperty("hotvod.format"),"");
		
		model.addAttribute("imgIptvSize", imgIptvSize);
		model.addAttribute("imgFormat", imgFormat);
		//-------이미지 체크----------//
		
		String hotvodBadgeList = GlobalCom.isNull(SmartUXProperties.getProperty("hotvod.badge.list"), HotvodConst.DEFAULT_HOTVOD_BADGE_LIST);
		model.addAttribute("hotvodBadgeList", hotvodBadgeList);
		
		String hotvodServiceList = GlobalCom.isNull(SmartUXProperties.getProperty("hotvod.service.list"), HotvodConst.DEFAULT_HOTVOD_SERVICE_LIST);
		model.addAttribute("hotvodServiceList", hotvodServiceList);
		
		return "/admin/hotvod/subDetail";
	}

	/**
	 * 하위목록 수정
	 * @param uploadfile
	 * @param uploadfile_tv
	 * @param hotvodContentVO
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/hotvod/subUpdate.do",method=RequestMethod.POST)
	public String subUpdate(
			@RequestParam(value="file_tv", required=false, defaultValue="") MultipartFile uploadfile_tv,
			HotvodSearchVO hotvodSearchVO,
			HotvodContentVO hotvodContentVO,
			HttpServletRequest request,
			Model model) throws Exception {
		String cookieID = CookieUtil.getCookieUserID(request);

		String tmp_file_name = "";
		String bg_img_file = "";
		File img_file = null;
		String result = SmartUXProperties.getProperty("flag.success");

		boolean imgCheck = true;
		if(!FileUtil.fileSizeCheck(uploadfile_tv,SmartUXProperties.getProperty("hotvod.iptv.image"))){
			imgCheck = false;
			logger.info("[subUpdate]uploadfile_tv over size : " + uploadfile_tv.getSize());
		}

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		String year = sdf.format(date);
		sdf = new SimpleDateFormat("MM");
		String month = sdf.format(date);
		String subPath = year + "/" + month + "/";
		String filePath = SmartUXProperties.getProperty("hotvod.upload.dir") + subPath;
		File pathChk = new File(filePath);

		if(!pathChk.exists()){
			pathChk.mkdirs();
		}

		if(imgCheck){
			if (uploadfile_tv.getSize() != 0L) {
				tmp_file_name = uploadfile_tv.getOriginalFilename();
				String ext = tmp_file_name.substring(	tmp_file_name.lastIndexOf(".") + 1,	tmp_file_name.length()); // 확장자 구하기
				ext = ext.toLowerCase();
				bg_img_file = subPath + Long.toString(System.currentTimeMillis()) + "."+ ext; // 시스템타임.확장자 구조로 한다
				if(bg_img_file.equals(hotvodContentVO.getContent_img())){
					bg_img_file = subPath + Long.toString(System.currentTimeMillis()+1) + "."+ ext;
				}
				String newFilename=SmartUXProperties.getProperty("hotvod.upload.dir")+ bg_img_file;

				img_file = new File(newFilename);
				uploadfile_tv.transferTo(img_file);

				hotvodContentVO.setContent_img_tv(bg_img_file);
			}else{
				String savefilenm = hotvodContentVO.getContent_img_tv();
				if(savefilenm != null  && !savefilenm.isEmpty() ){
					hotvodContentVO.setContent_img_tv(savefilenm.substring(savefilenm.lastIndexOf("hotvod/") +1));
				}
			}

			hotvodContentVO.setMod_id(cookieID);

			if("0000".equals(result)){

				if(HotvodConst.CONTENT_TYPE.CATEGORY.equals(hotvodContentVO.getContent_type()) && "Y".equals(hotvodContentVO.getDel_yn())){
					service.contentDetailDelete(hotvodContentVO.getContent_id(), hotvodContentVO.getContent_type(), cookieID);
				}
				if (!HotvodConst.CONTENT_TYPE.CATEGORY.equals(hotvodContentVO.getContent_type())) {
					service.contentUpdate(hotvodContentVO);
				} else {
					service.categoryUpdate(hotvodContentVO);
				}
				
				//2021-04-22 BPAS 편성관리 추가
				try {
					String content_type = hotvodContentVO.getContent_type();
					if("M".equals(content_type)) {
						imcsService.insertImcs("U", "HOT", hotvodContentVO.getContent_id(), "ALB", hotvodContentVO.getAlbum_id(), "N");
					}else if("N".equals(content_type)) {
						imcsService.insertImcs("U", "HOT", hotvodContentVO.getContent_id(), "CAT", hotvodContentVO.getCategory_id(), "N");
					}
				}catch (Exception e) {
					logger.error("[insertImcs]["+e.getClass().getName()+"]["+e.getMessage()+"]");
				}
			}
		}else{
			result = "0002";
		}

		model.addAttribute("content_id", hotvodSearchVO.getContent_id());
		model.addAttribute("findName", hotvodSearchVO.getFindName());
		model.addAttribute("findValue", hotvodSearchVO.getFindValue());
		model.addAttribute("delYn", hotvodSearchVO.getDelYn());
		model.addAttribute("pageNum", hotvodSearchVO.getPageNum());
		model.addAttribute("parent_id", hotvodSearchVO.getParent_id());

		String domain = SmartUXProperties.getProperty("smartux.domain.https");
		return "redirect:"+domain+"/smartux_adm/admin/hotvod/subDetail.do?result=" + result+"&content_type="+hotvodContentVO.getContent_type();
	}

	/**
	 * 앨범, 카테고리 정보 조회
	 * @param choiceCts
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/hotvod/getAlbumCateInfo.do",method=RequestMethod.POST)
	public ResponseEntity<String> getAlbumCateInfo(
			@RequestParam(value="choiceCts", required=false, defaultValue="") String choiceCts,
			HttpServletRequest request,
			Model model
	) throws Exception{

		String result = "";
		String resultcode = "";
		String resultmessage = "";
		String resultCount = "";
		HotvodContentVO albumCateInfo = new HotvodContentVO();

		try{
			String choiceData[] = choiceCts.split("\\|\\|");
			albumCateInfo = service.getAlbumCateInfo(choiceData);

			if(albumCateInfo == null){
				albumCateInfo = new HotvodContentVO();
				
				resultcode = SmartUXProperties.getProperty("flag.notfound");
				resultmessage = SmartUXProperties.getProperty("message.notfound");
			}else{
				resultcode = SmartUXProperties.getProperty("flag.success");
				resultmessage = SmartUXProperties.getProperty("message.success");
			}

			result = "{\"result\":{\"message\":\""+resultmessage+"\",\"flag\":\""+resultcode+"\",\"resultCount\":\""+resultCount
					+"\",\"category_id\":\""+ GlobalCom.isNull(albumCateInfo.getCategory_id(), "") +"\",\"album_id\":\""+GlobalCom.isNull(albumCateInfo.getAlbum_id(), "")+"\",\"contents_name\":\""+ StringUtils.defaultString(StringUtils.escape(albumCateInfo.getContents_name()))
					+"\",\"series_no\":\""+GlobalCom.isNull(albumCateInfo.getSeries_no(), "")+"\",\"sponsor_name\":\""+GlobalCom.isNull(albumCateInfo.getSponsor_name(), "")+"\",\"still_img\":\""+GlobalCom.isNull(albumCateInfo.getStill_img(), "")+"\"}}";
		} catch (Exception e) {
			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
			result = "{\"result\":{\"message\":\""+resultmessage+"\",\"flag\":\""+resultcode+"\"}}";
		}

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(result).toString(), responseHeaders, HttpStatus.CREATED);
	}

	/**
	 * 상위 카테고리의 노출여부 조회
	 * @param parent_id
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/hotvod/getParentContentDelYn.do",method=RequestMethod.POST)
	public ResponseEntity<String> getParentContentDelYn(
			@RequestParam(value="parent_id", required=false, defaultValue="") String parent_id,
			@RequestParam(value="content_id", required=false, defaultValue="") String content_id,
			@RequestParam(value="content_type", required=false, defaultValue="") String content_type,
			HttpServletRequest request,
			Model model
	) throws Exception{

		String result = "";
		String resultcode = "";
		String resultmessage = "";
		String delYn = "";
		String contentName = "";

		try{
			HotvodContentVO hotvodContentVO = new HotvodContentVO();
			HotvodContentVO resultVo = new HotvodContentVO();
			hotvodContentVO.setParent_id(parent_id);
			hotvodContentVO.setContent_id(content_id);
			hotvodContentVO.setContent_type(content_type);

			resultVo = service.getParentContentDelYn(hotvodContentVO);
			if(resultVo != null){
				delYn = resultVo.getDel_yn();
				contentName = resultVo.getContent_name();
			}
			resultcode = SmartUXProperties.getProperty("flag.success");
			resultmessage = SmartUXProperties.getProperty("message.success");
		} catch (Exception e) {
			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}

		result = "{\"result\":{\"message\":\""+resultmessage+"\",\"flag\":\""+resultcode+"\",\"delYn\":\""+delYn+"\",\"contentName\":\""+contentName+"\"}}";

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(result).toString(), responseHeaders, HttpStatus.CREATED);
	}

	/**
	 * 화제동영상 통합검색 파일 업로드
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/admin/hotvod/hotvodFileUpload.do", method=RequestMethod.POST)
	@ResponseBody
	public CUDResult hotvodFileUpload(HttpServletRequest request, HttpServletResponse response){
		CUDResult result = new CUDResult();
		String resultcode = SmartUXProperties.getProperty("flag.success");
		String resultmessage = SmartUXProperties.getProperty("message.success");

		try{
			service.hotvodFileUpload();
			result.setFlag(resultcode);
			result.setMessage(resultmessage);
		}catch(Exception e){
			ExceptionHandler handler = new ExceptionHandler(e);
			result.setFlag(handler.getFlag());
			result.setMessage(handler.getMessage());
		}

		return result;
	}

	/**
	 * 메타 데이터 체크 조회
	 * @param content_id
	 * @param is_master
	 * @param is_focus
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/hotvod/getBadgeDataInsertYn.do",method=RequestMethod.POST)
	public ResponseEntity<String> getBadgeDataInsertYn(
			@RequestParam(value="content_id", required=false, defaultValue="") String content_id,
			@RequestParam(value="type", required=false, defaultValue="") String type,
			HttpServletRequest request,
			Model model
	) throws Exception
	{

		String result = "";
		String resultcode = "";
		String resultmessage = "";
		HashMap<String,Boolean> resultMap = null;
		try
		{
			resultMap  = service.getBadgeDataInsertYn(content_id,type);

			resultcode = SmartUXProperties.getProperty("flag.success");
			resultmessage = SmartUXProperties.getProperty("message.success");
		}
		catch (Exception e)
		{
			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}

		result = "{\"result\":{\"message\":\""+resultmessage+"\",\"flag\":\""+resultcode+"\",\"isValid\":\""+resultMap.get("isValid")+"\"}}";

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(result).toString(), responseHeaders, HttpStatus.CREATED);
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/admin/hotvod/contents/downloadExcelFile", method = RequestMethod.GET)
	public byte[] downloadExcelFile(
			@RequestParam(value="findName", required=false, defaultValue="") String findName,
			@RequestParam(value="findValue", required=false, defaultValue="") String findValue,
			@RequestParam(value="delYn", required=false, defaultValue="N") String delYn,
			@RequestParam(value="parent_id", required=false, defaultValue="N") String parent_id,
			HttpServletRequest request, HttpServletResponse response,
			Model model) throws Exception {
		
		logger.debug("[HotvodController][downloadExcelFile][START]");
		
		HTMLCleaner cleaner = new HTMLCleaner();
		findName 	= cleaner.clean(findName);
		findValue = cleaner.clean(findValue);

		HotvodSearchVO hotvodSearchVo = new HotvodSearchVO();

		hotvodSearchVo.setFindName(GlobalCom.isNull(findName, "CONTENT_NAME"));
		hotvodSearchVo.setFindValue(findValue);
		hotvodSearchVo.setDelYn(delYn);
		hotvodSearchVo.setParent_id(parent_id);

		String domain = SmartUXProperties.getProperty("smartux.domain.https");
		model.addAttribute("domain_https", domain);

		String[] headerAry = SmartUXProperties.getProperty("hotvod.contents.excel.header").split("\\|");
		
		String name ="";
		
		if(!StringUtils.isEmpty(findValue)) {
			if(!StringUtils.isEmpty(findName)) {
				if("CONTENT_NAME".equals(findName)) {
					name += "컨텐츠명_";
				}else if("SITE_NAME".equals(findName)) {
					name += "사이트명_";
				}else if("PARNT_NAME".equals(findName)) {
					name += "상위 컨텐츠명_";
				}
			}
			name += findValue+"_";
		}
		
		name += "화제의동영상_";
		ExcelWorkBookFactory.ExcelSheetFactory sheet = ExcelWorkBookFactory.create(10000).sheet(name);
		sheet.headers(1, headerAry);

		List<HotvodExcelVo> list = service.getExcelCategory(hotvodSearchVo);
		
		if (list.size() > 0) {
			
			
			sheet.rowCellValues(list, new ExcelWorkBookFactory.CellValueRef<HotvodExcelVo>() {
				@Override
				public void setRowData(Row row, HotvodExcelVo vo) {
					int col = 1;
					row.createCell(col++).setCellValue(vo.getHvCategoryId());
					row.createCell(col++).setCellValue(vo.getFullCategoryName());
					row.createCell(col++).setCellValue(vo.getContentsType());
					row.createCell(col++).setCellValue(vo.getContentsId());
					row.createCell(col++).setCellValue(vo.getContentsName());
					row.createCell(col++).setCellValue(vo.getAlbumId());
					row.createCell(col++).setCellValue(vo.getViewTime());
					row.createCell(col++).setCellValue(vo.getContentsUrl());
					if("N".equals(vo.getDelYn())){
						row.createCell(col++).setCellValue("노출");	
					}else {
						row.createCell(col++).setCellValue("비노출");
					}
					//2019.11.22 브릿지홈 개편 : 앨범 카테고리ID추가
					row.createCell(col++).setCellValue(vo.getVodCategoryId());
				}
			});
		}

		byte[] bytes = sheet.end().make();

		name = new String(name.getBytes("KSC5601"), "8859_1");
		
		response.setHeader("Content-Disposition", "attachment; filename=" + name + GlobalCom.getTodayFormat() + ".xlsx");
		response.setContentLength(bytes.length);
		response.setContentType("application/vnd.ms-excel");

		logger.debug("[HotvodController][downloadExcelFile][END]");

		return bytes;
	}
	
	/**
	 * 
	 * 컨텐츠 중복복사여부 체크
	 * @param term_model	단말모델	 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/hotvod/contentCopyChk", method=RequestMethod.POST )
	public @ResponseBody  String contentCopyChk(
			@RequestParam(value="parent_id", required=false, defaultValue="") String parent_id,
			@RequestParam(value="content_id", required=false, defaultValue="") String content_id,
			HttpServletRequest request
			) throws Exception{

		String resultcode = "";
		String resultmessage = "";
		String resultstate = "";
		
		try{
			resultstate = String.valueOf(service.contentCopyChk(parent_id, content_id));				
			resultcode  = SmartUXProperties.getProperty("flag.success");
			resultmessage = SmartUXProperties.getProperty("message.success");
				
		}catch(Exception e){
			logger.error("contentCopyChk : "+e.getClass().getName());
			logger.error("contentCopyChk : "+e.getMessage());
			
			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}
		
		return "{\"flag\" : \"" + resultcode + "\", \"message\" : \"" + resultmessage + "\",\"resultstate\" : \"" + resultstate + "\"}";
	}	
	
	/**
	 * 컨텐츠 복사
	 * @param content_id
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/hotvod/contentCopy.do",method=RequestMethod.POST)
	public ResponseEntity<String> contentCopy(
			@RequestParam(value="parent_id", required=false, defaultValue="") String parent_id,
			@RequestParam(value="content_id", required=false, defaultValue="") String content_id,
			Model model
	) throws Exception{

		String result = "";
		String resultcode = "";
		String resultmessage = "";

		try{
			service.contentCopy(parent_id, content_id);
			
			resultcode = SmartUXProperties.getProperty("flag.success");
			resultmessage = SmartUXProperties.getProperty("message.success");
		} catch (Exception e) {
			logger.error("[contentCopy]["+e.getClass().getName()+"]["+e.getMessage()+"]");

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
	 * <pre>
	 * 카테고리 변경요청 
	 * </pre>
	 * @author medialog
	 * @date 2017. 11. 30.
	 * @method parentCategoryUpdate
	 * @return UpdateHotvodCateApiVo
	 */
	public UpdateHotvodCateApiVo parentCategoryUpdate(String parent_id, String body) throws Exception {
		UpdateHotvodCateApiVo updatehotvodcateapiVo = new UpdateHotvodCateApiVo();
		JSONObject jBody= (JSONObject) JSONSerializer.toJSON(body);
		JSONArray jArray = jBody.getJSONArray("body");
		if (null != jArray) {
			List<HotvodContentIdListVo> success = new ArrayList<HotvodContentIdListVo>();
			List<HotvodContentIdListVo> failure = new ArrayList<HotvodContentIdListVo>();
			
			int length = jArray.size();
			for (int i = 0; i < length; i++) {
				JSONObject obj = jArray.getJSONObject(i);
				String contId = StringUtils.defaultString(obj.getString("content_id"));
				String preParentId = StringUtils.defaultString(obj.getString("pre_parent_id"));
				logger.info("[parentCategoryUpdate]["+parent_id+"]["+preParentId+"]["+contId+"]");
				if(!"".equals(contId) && !"".equals(preParentId)){
					try{
						HotvodContentVO HotvodContentVO = new HotvodContentVO();
						HotvodContentVO.setParent_id(parent_id);
						HotvodContentVO.setPre_parent_id(preParentId);
						HotvodContentVO.setContent_id(contId);
						service.parentCateUpdate(HotvodContentVO);
						success.add(new HotvodContentIdListVo(contId));
					}catch(java.lang.Exception e){
						failure.add(new HotvodContentIdListVo(contId));
						logger.info("[parentCategoryUpdate]["+parent_id+"]["+preParentId+"]["+contId+"]["+e.getClass().getName()+"]"+e.getMessage());
					}
				}
			}
			updatehotvodcateapiVo.setSuccess(success);
			updatehotvodcateapiVo.setFailure(failure);
		} else {
			throw new java.lang.Exception("Content does not exist.");
		}
		return updatehotvodcateapiVo;
	}
	
	/**
	 * <pre>
	 * 뱃지 체크 
	 * </pre>
	 * @author medialog
	 * @date 2017. 9. 19.
	 * @method checkBadge
	 * @return CUDResult
	 */
	private CUDResult checkBadge(String contentId, String ContentType, String badgeData){
		CUDResult result = new CUDResult();
		result.setFlag(SmartUXProperties.getProperty("flag.success"));
		
		try{
			HashMap<String,Boolean> map = service.getBadgeDataInsertYn(contentId, ContentType);
			if(map.get("isValid") == false){
				result.setFlag(SmartUXProperties.getProperty("flag.etc"));
				result.setMessage("파라메터 값이 정확하지 않습니다.");
			}
			
			return result;
		}catch(StringIndexOutOfBoundsException e){
			result.setFlag(SmartUXProperties.getProperty("flag.etc"));
			result.setMessage("뱃지선택 에러발생[OutOfBounds]");
			return result;
		}catch(Exception e){
			result.setFlag(SmartUXProperties.getProperty("flag.etc"));
			result.setMessage("뱃지선택 에러발생");
			return result;
		}
	}
	
}
