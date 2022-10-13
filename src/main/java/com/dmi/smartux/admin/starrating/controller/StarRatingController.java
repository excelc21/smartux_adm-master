package com.dmi.smartux.admin.starrating.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.dmi.smartux.admin.starrating.service.StarRatingService;
import com.dmi.smartux.admin.starrating.vo.HistoryVO;
import com.dmi.smartux.admin.starrating.vo.StarRatingSearchVO;
import com.dmi.smartux.admin.starrating.vo.StarRatingVO;
import com.dmi.smartux.common.exception.ExceptionHandler;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.CookieUtil;
import com.dmi.smartux.common.util.ExcelManager;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.util.HTMLCleaner;
import com.dmi.smartux.common.vo.CUDResult;

import net.sf.json.JSONSerializer;

@Controller
public class StarRatingController {
	@Autowired
	StarRatingService service;
	
	private final Log applyLogger = LogFactory.getLog("adminApplyCacheHistroy");
	
	/**
	 * 별점 즉시적용
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/starrating/applyCache", method=RequestMethod.POST)
	public ResponseEntity<String> applyCache(
			HttpServletRequest request,
			HttpServletResponse response
			) throws Exception {

		final CLog cLog = new CLog(applyLogger, request.getRemoteAddr(), request.getRequestURI(), request.getMethod());
		final String loginUser = CookieUtil.getCookieUserID(request);
		final CUDResult result = new CUDResult();
		
		try {
			cLog.startLog("별점 즉시적용",  loginUser);
			final String host = SmartUXProperties.getProperty("cache.sync.public.host");
			final String port = SmartUXProperties.getProperty("cache.sync.public.port");
			
			String fileLockPath = SmartUXProperties.getProperty("StarRatingDao.refreshStarRating.fileLock");
			int waitTime = NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.wait"), 5);
			
			FileProcessLockFactory.start(fileLockPath, waitTime,  new FileProcessFunction("별점 즉시적용", ResultCode.ApplyRequestFail) {
				
				@Override
				public void run() throws Exception {
					
					//즉시적용 호출 시 sleep 값 추가 -> DB분리건으로 OGG가 10초 이상 걸리는 경우가 있어 즉시적용 시 반영이 안돼서 추가함
					Thread.sleep(Integer.parseInt(org.apache.commons.lang.StringUtils.defaultIfEmpty(SmartUXProperties.getProperty("apply.cache.call.sleep"), "10000")));
					
					RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
							host, 
							port, 
							SmartUXProperties.getProperty("StarRatingDao.refreshStarRating.url"),
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.method"), "POST"), 
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"), 
							MediaType.APPLICATION_JSON.toString(),
							NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.timeout"), 60000)).getResponseBody());
					
					result.setFlag(codeMsg.getCode());
					result.setMessage(codeMsg.getMessage());
					cLog.middleLog("별점 즉시적용", "API 호출", codeMsg.getCode(), codeMsg.getMessage());
				}
			});
		} catch (MimsCommonException e) {
			cLog.warnLog("별점 즉시적용", "실패", e.getFlag(), e.getMessage(), e.getLocalizedMessage());
			result.setFlag(e.getFlag());
			result.setMessage(e.getMessage());
		} finally {
			cLog.endLog("별점 즉시적용", loginUser, result.getFlag(), result.getMessage());
		}
		
		String resultMsg = "{\"flag\" : \"" + result.getFlag() + "\", \"message\" : \"" + result.getMessage() + "\"}";
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(resultMsg).toString(), responseHeaders, HttpStatus.OK);
	}
	
	/**
	 * 별점 목록
	 * @param model
	 * @param findName
	 * @param findValue
	 * @param use_yn
	 * @param pageNum
	 * @param system_gb
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/starrating/getStarRatingList", method = RequestMethod.GET)
	public String getStarRatingList(Model model
			, @RequestParam(value="findName", required=false, defaultValue="") String findName
			, @RequestParam(value="findValue", required=false, defaultValue="") String findValue
			, @RequestParam(value="use_yn", required=false, defaultValue="") String use_yn
			, @RequestParam(value="pageNum", required=false, defaultValue="1") String pageNum
			, @RequestParam(value="system_gb", required=false, defaultValue="2")String system_gb) throws Exception {
		findName 	= HTMLCleaner.clean(findName);
		findValue = HTMLCleaner.clean(findValue);
		pageNum = HTMLCleaner.clean(pageNum);
		use_yn = HTMLCleaner.clean(use_yn);
		system_gb = HTMLCleaner.clean(system_gb);
		
		StarRatingSearchVO vo = new StarRatingSearchVO();
		//페이지 
		vo.setPageSize(GlobalCom.isNumber(vo.getPageSize(),10));
		vo.setBlockSize(GlobalCom.isNumber(vo.getBlockSize(),10));
		vo.setPageNum(GlobalCom.isNumber(Integer.parseInt(GlobalCom.isNull(pageNum, "1")),1));
		//검색 
		vo.setFindName(GlobalCom.isNull(findName, "SR_TITLE"));
		vo.setFindValue(findValue);
		vo.setUse_yn(use_yn);
		vo.setSystem_gb(system_gb);
		
		List<StarRatingVO> list = service.getStarRatingList(vo);
		vo.setPageCount(service.getStarRatingListCnt(vo));
		
		model.addAttribute("list", list);
		model.addAttribute("vo", vo);
		return "/admin/starrating/getStarRatingList";
	}
	
	/**
	 * 별점 등록 화면
	 * @param model
	 * @param findName
	 * @param findValue
	 * @param use_yn
	 * @param pageNum
	 * @param system_gb
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/starrating/insertStarRating", method = RequestMethod.GET)
	public String insertStarRating(Model model
			, @RequestParam(value="findName", required=false, defaultValue="") String findName
			, @RequestParam(value="findValue", required=false, defaultValue="") String findValue
			, @RequestParam(value="use_yn", required=false, defaultValue="") String use_yn
			, @RequestParam(value="pageNum", required=false, defaultValue="1") String pageNum
			, @RequestParam(value="system_gb", required=false, defaultValue="2")String system_gb) throws Exception {
		findName 	= HTMLCleaner.clean(findName);
		findValue = HTMLCleaner.clean(findValue);
		pageNum = HTMLCleaner.clean(pageNum);
		use_yn = HTMLCleaner.clean(use_yn);
		system_gb = HTMLCleaner.clean(system_gb);
		
		StarRatingSearchVO vo = new StarRatingSearchVO();
		vo.setFindName(findName);
		vo.setFindValue(findValue);
		vo.setPageNum(GlobalCom.isNumber(Integer.parseInt(GlobalCom.isNull(pageNum, "1")),1));
		vo.setUse_yn(use_yn);
		vo.setSystem_gb(system_gb);
		
		String imgServer = service.getImgServer();
		
		model.addAttribute("imgServer", imgServer);
		model.addAttribute("vo", vo);
		model.addAttribute("isUpdate", "0");
		return "/admin/starrating/starRatingForm";
	}
	
	/**
	 * 별점 등록
	 * @param model
	 * @param p_title
	 * @param system_gb
	 * @param srList
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/starrating/insertProc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> starRatingInsertProc(
			 @RequestParam(value="p_title", required=false, defaultValue="") String p_title
			, @RequestParam(value="system_gb", required=false, defaultValue="") String system_gb
			, @RequestParam(value="srList[]") List<String> srList
			, HttpServletRequest request) throws Exception {
		
		String resCode = "";
		String resMessage = "";
		String cookieID = CookieUtil.getCookieUserID(request);
		
		try{
			StarRatingVO vo = new StarRatingVO();
			vo.setSr_title(p_title);
			vo.setSrList(srList);
			vo.setReg_id(cookieID);
			vo.setMod_id(cookieID);
			vo.setSystem_gb(system_gb);
			
			service.insertProc(vo);
			resCode = SmartUXProperties.getProperty("flag.success");
			resMessage = SmartUXProperties.getProperty("message.success");
			
		}catch(Exception e){
			ExceptionHandler handler = new ExceptionHandler(e);
			resCode 	= handler.getFlag();
			resMessage 	= handler.getMessage();
		}
		
		String result = "{\"res\" : \"" + resCode + "\", \"msg\" : \"" + resMessage + "\"}";
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(result, responseHeaders, HttpStatus.CREATED);
	}
	
	/**
	 * 병점 수정 화면
	 * @param model
	 * @param findName
	 * @param findValue
	 * @param use_yn
	 * @param pageNum
	 * @param system_gb
	 * @param sr_pid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/starrating/updateStarRating", method = RequestMethod.GET)
	public String updateStarRating(Model model
			, @RequestParam(value="findName", required=false, defaultValue="") String findName
			, @RequestParam(value="findValue", required=false, defaultValue="") String findValue
			, @RequestParam(value="use_yn", required=false, defaultValue="") String use_yn
			, @RequestParam(value="pageNum", required=false, defaultValue="1") String pageNum
			, @RequestParam(value="system_gb", required=false, defaultValue="2")String system_gb
			, @RequestParam(value="sr_pid", required=false, defaultValue="")String sr_pid) throws Exception {
		findName 	= HTMLCleaner.clean(findName);
		findValue = HTMLCleaner.clean(findValue);
		pageNum = HTMLCleaner.clean(pageNum);
		use_yn = HTMLCleaner.clean(use_yn);
		system_gb = HTMLCleaner.clean(system_gb);
		sr_pid = HTMLCleaner.clean(sr_pid);
		
		StarRatingSearchVO vo = new StarRatingSearchVO();
		vo.setFindName(findName);
		vo.setFindValue(findValue);
		vo.setPageNum(GlobalCom.isNumber(Integer.parseInt(GlobalCom.isNull(pageNum, "1")),1));
		vo.setUse_yn(use_yn);
		vo.setSystem_gb(system_gb);
		vo.setSr_pid(sr_pid);
		
		String imgServer = service.getImgServer();
		String title = service.getTitle(sr_pid);
		String srList = service.getSrList(sr_pid);
		
		model.addAttribute("imgServer", imgServer);
		model.addAttribute("title", title);
		model.addAttribute("srList", srList);
		model.addAttribute("vo", vo);
		model.addAttribute("isUpdate", "1");
		return "/admin/starrating/starRatingForm";
	}
	
	/**
	 * 별점 수정
	 * @param model
	 * @param p_title
	 * @param sr_pid
	 * @param system_gb
	 * @param srList
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/starrating/updateProc", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> starRatingUpdateProc(
			 @RequestParam(value="p_title", required=false, defaultValue="") String p_title
			, @RequestParam(value="sr_pid", required=false, defaultValue="") String sr_pid
			, @RequestParam(value="system_gb", required=false, defaultValue="") String system_gb
			, @RequestParam(value="srList[]") List<String> srList
			, HttpServletRequest request) throws Exception {
		
		String resCode = "";
		String resMessage = "";
		String cookieID = CookieUtil.getCookieUserID(request);
		
		try{
			StarRatingVO vo = new StarRatingVO();
			vo.setSr_title(p_title);
			vo.setSr_pid(sr_pid);
			vo.setSrList(srList);
			vo.setReg_id(cookieID);
			vo.setMod_id(cookieID);
			vo.setSystem_gb(system_gb);
			
			service.updateProc(vo);
			resCode = SmartUXProperties.getProperty("flag.success");
			resMessage = SmartUXProperties.getProperty("message.success");
			
		}catch(Exception e){
			ExceptionHandler handler = new ExceptionHandler(e);
			resCode 	= handler.getFlag();
			resMessage 	= handler.getMessage();
		}
		
		String result = "{\"res\" : \"" + resCode + "\", \"msg\" : \"" + resMessage + "\"}";
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(result, responseHeaders, HttpStatus.CREATED);
	}
	
	/**
	 * 별점 이미지 등록
	 * @param img_file
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/starrating/imgFileUpload", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> imgFileUpload(@RequestParam(value="img_file", required=false) MultipartFile img_file) throws Exception {
		String resCode = "";
		String resMessage = "";
		String filePath = SmartUXProperties.getProperty("starrating.img.filepath");
		String tmp_file_name = "";
		String bg_img_file = "";
		File temp_file = null;
		
		try{
			File dirChk = new File(filePath);
			if(!dirChk.exists()){
				dirChk.mkdirs();
			}
			
			if (img_file.getSize() != 0L) {
				tmp_file_name = img_file.getOriginalFilename();
				String ext = tmp_file_name.substring(	tmp_file_name.lastIndexOf(".") + 1,	tmp_file_name.length()); // 확장자 구하기
				ext = ext.toLowerCase();
				bg_img_file = Long.toString(System.currentTimeMillis()) + "."+ ext; // 시스템타임.확장자 구조로 한다
				String newFilename=filePath+ bg_img_file;
				
				temp_file = new File(newFilename);
				img_file.transferTo(temp_file);
			}
			
			resCode = SmartUXProperties.getProperty("flag.success");
			resMessage = SmartUXProperties.getProperty("message.success");
		} catch(Exception e) {
			ExceptionHandler handler = new ExceptionHandler(e);
			resCode 	= handler.getFlag();
			resMessage 	= handler.getMessage();
		}
		
		String result = "{\"result\":{\"res\":\"" + resCode + "\",\"msg\":\"" + resMessage + "\",\"file_name\":\"" + bg_img_file + "\"}}";
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(result).toString(), responseHeaders, HttpStatus.CREATED);
	}
	
	/**
	 * 활성화 상태 수정
	 * @param model
	 * @param sr_id
	 * @param use_yn
	 * @param system_gb
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/starrating/updateUseYn", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> updateUseYn(
			 @RequestParam(value="sr_id", required=false, defaultValue="") String sr_id
			, @RequestParam(value="use_yn", required=false, defaultValue="") String use_yn
			, @RequestParam(value="system_gb", required=false, defaultValue="2") String system_gb
			, HttpServletRequest request) throws Exception {
		
		String resCode = "";
		String resMessage = "";
		String cookieID = CookieUtil.getCookieUserID(request);
		
		try{
			StarRatingVO vo = new StarRatingVO();
			vo.setSr_id(sr_id);
			vo.setUse_yn(use_yn);
			vo.setMod_id(cookieID);
			
			if("Y".equals(use_yn) && 0 < service.getUseYnCnt(system_gb)){
				resCode = SmartUXProperties.getProperty("flag.bedata");
				resMessage = SmartUXProperties.getProperty("message.bedata");
			}else{
				service.updateUseYn(vo);
				resCode = SmartUXProperties.getProperty("flag.success");
				resMessage = SmartUXProperties.getProperty("message.success");
			}
			
		}catch(Exception e){
			ExceptionHandler handler = new ExceptionHandler(e);
			resCode 	= handler.getFlag();
			resMessage 	= handler.getMessage();
		}
		
		String result = "{\"res\" : \"" + resCode + "\", \"msg\" : \"" + resMessage + "\"}";
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(result, responseHeaders, HttpStatus.CREATED);
	}
	
	/**
	 * 별점 내역 화면
	 * @param model
	 * @param findName
	 * @param findValue
	 * @param use_yn
	 * @param pageNum
	 * @param system_gb
	 * @param sr_pid
	 * @param album_findName
	 * @param album_findValue
	 * @param album_pageNum
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/starrating/getAlbumHistoryList", method = RequestMethod.GET)
	public String getAlbumHistoryList(Model model
			, @RequestParam(value="findName", required=false, defaultValue="") String findName
			, @RequestParam(value="findValue", required=false, defaultValue="") String findValue
			, @RequestParam(value="pageNum", required=false, defaultValue="1") String pageNum
			, @RequestParam(value="use_yn", required=false, defaultValue="") String use_yn
			, @RequestParam(value="system_gb", required=false, defaultValue="0")String system_gb
			, @RequestParam(value="sr_pid", required=false, defaultValue="")String sr_pid
			, @RequestParam(value="sr_findName", required=false, defaultValue="")String sr_findName
			, @RequestParam(value="sr_findValue", required=false, defaultValue="")String sr_findValue
			, @RequestParam(value="sr_pageNum", required=false, defaultValue="1") String sr_pageNum) throws Exception {
		//별점 목록의 검색조건
		sr_findName 	= HTMLCleaner.clean(sr_findName);
		sr_findValue = HTMLCleaner.clean(sr_findValue);
		sr_pageNum = HTMLCleaner.clean(sr_pageNum);
		use_yn = HTMLCleaner.clean(use_yn);
		system_gb = HTMLCleaner.clean(system_gb);
		sr_pid = HTMLCleaner.clean(sr_pid);
		
		StarRatingSearchVO sr_vo = new StarRatingSearchVO();
		sr_vo.setFindName(sr_findName);
		sr_vo.setFindValue(sr_findValue);
		sr_vo.setPageNum(GlobalCom.isNumber(Integer.parseInt(GlobalCom.isNull(sr_pageNum, "1")),1));
		sr_vo.setUse_yn(use_yn);
		sr_vo.setSystem_gb(system_gb);
		sr_vo.setSr_pid(sr_pid);
		
		//별점 내역 검색 조건
		findName 	= HTMLCleaner.clean(findName);
		findValue = HTMLCleaner.clean(findValue);
		pageNum = HTMLCleaner.clean(pageNum);
		
		StarRatingSearchVO vo = new StarRatingSearchVO();
		//페이지 
		vo.setPageSize(GlobalCom.isNumber(vo.getPageSize(),10));
		vo.setBlockSize(GlobalCom.isNumber(vo.getBlockSize(),10));
		vo.setPageNum(GlobalCom.isNumber(Integer.parseInt(GlobalCom.isNull(pageNum, "1")),1));
		//검색 
		vo.setFindName(findName);
		vo.setFindValue(findValue);
		vo.setSr_pid(sr_pid);
		
		List<HistoryVO> list = service.getAlbumHistoryList(vo);
		vo.setPageCount(service.getAlbumHistoryListCnt(vo));
		
		model.addAttribute("sr_vo", sr_vo);
		model.addAttribute("list", list);
		model.addAttribute("vo", vo);
		return "/admin/starrating/getAlbumHistoryList";
	}
	
	/**
	 * 별점 주기 내역 화면
	 * @param model
	 * @param findName
	 * @param findValue
	 * @param use_yn
	 * @param pageNum
	 * @param system_gb
	 * @param sr_pid
	 * @param album_findName
	 * @param album_findValue
	 * @param album_pageNum
	 * @param album_id
	 * @param sr_findName
	 * @param sr_findValue
	 * @param sr_pageNum
	 * @param start_date
	 * @param end_date
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/admin/starrating/getSrHistoryList", method = RequestMethod.GET)
	public String getSrHistoryList(Model model
			, @RequestParam(value="findName", required=false, defaultValue="") String findName
			, @RequestParam(value="findValue", required=false, defaultValue="") String findValue
			, @RequestParam(value="pageNum", required=false, defaultValue="1") String pageNum
			, @RequestParam(value="use_yn", required=false, defaultValue="") String use_yn
			, @RequestParam(value="system_gb", required=false, defaultValue="0")String system_gb
			, @RequestParam(value="sr_pid", required=false, defaultValue="")String sr_pid
			, @RequestParam(value="sr_findName", required=false, defaultValue="")String sr_findName
			, @RequestParam(value="sr_findValue", required=false, defaultValue="")String sr_findValue
			, @RequestParam(value="sr_pageNum", required=false, defaultValue="1") String sr_pageNum
			, @RequestParam(value="album_findName", required=false, defaultValue="")String album_findName
			, @RequestParam(value="album_findValue", required=false, defaultValue="")String album_findValue
			, @RequestParam(value="album_pageNum", required=false, defaultValue="1") String album_pageNum
			, @RequestParam(value="album_id", required=false, defaultValue="") String album_id
			, @RequestParam(value="start_date", required=false, defaultValue="")String start_date
			, @RequestParam(value="end_date", required=false, defaultValue="")String end_date) throws Exception {
		//별점 목록 검색조건
		sr_findName 	= HTMLCleaner.clean(sr_findName);
		sr_findValue = HTMLCleaner.clean(sr_findValue);
		sr_pageNum = HTMLCleaner.clean(sr_pageNum);
		use_yn = HTMLCleaner.clean(use_yn);
		system_gb = HTMLCleaner.clean(system_gb);
		sr_pid = HTMLCleaner.clean(sr_pid);
		
		StarRatingSearchVO sr_vo = new StarRatingSearchVO();
		sr_vo.setFindName(sr_findName);
		sr_vo.setFindValue(sr_findValue);
		sr_vo.setPageNum(GlobalCom.isNumber(Integer.parseInt(GlobalCom.isNull(sr_pageNum, "1")),1));
		sr_vo.setUse_yn(use_yn);
		sr_vo.setSystem_gb(system_gb);
		sr_vo.setSr_pid(sr_pid);
		
		//별점 내역 검색조건
		album_findName = HTMLCleaner.clean(album_findName);
		album_findValue = HTMLCleaner.clean(album_findValue);
		album_pageNum = HTMLCleaner.clean(album_pageNum);
		
		StarRatingSearchVO album_vo = new StarRatingSearchVO();
		album_vo.setFindName(album_findName);
		album_vo.setFindValue(album_findValue);
		album_vo.setPageNum(GlobalCom.isNumber(Integer.parseInt(GlobalCom.isNull(album_pageNum, "1")),1));
		
		//별점 주기 내역 검색조건
		album_id = HTMLCleaner.clean(album_id);
		findName = HTMLCleaner.clean(findName);
		findValue = HTMLCleaner.clean(findValue);
		pageNum = HTMLCleaner.clean(pageNum);
		start_date = HTMLCleaner.clean(start_date);
		end_date = HTMLCleaner.clean(end_date);
		
		StarRatingSearchVO vo = new StarRatingSearchVO();
		//페이지 
		vo.setPageSize(GlobalCom.isNumber(vo.getPageSize(),10));
		vo.setBlockSize(GlobalCom.isNumber(vo.getBlockSize(),10));
		vo.setPageNum(GlobalCom.isNumber(Integer.parseInt(GlobalCom.isNull(pageNum, "1")),1));
		//검색
		vo.setSr_pid(sr_pid);
		vo.setAlbum_id(album_id);
		vo.setFindName(findName);
		vo.setFindValue(findValue);
		vo.setStart_date(start_date);
		vo.setEnd_date(end_date);
		
		if("".equals(vo.getStart_date())){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH0000");
			Date date = new Date();
			date.setHours(date.getHours() + 1);
			vo.setEnd_date(sdf.format(date));
			date.setDate(date.getDate()-7);
			vo.setStart_date(sdf.format(date));
		}else{
			if(vo.getStart_date().replaceAll("[\\-\\: ]", "").length() == "yyyyMMddHH0000".length()){
				vo.setStart_date(vo.getStart_date().replaceAll("[\\-\\: ]", ""));
				vo.setEnd_date(vo.getEnd_date().replaceAll("[\\-\\: ]", ""));
			} else {
				vo.setStart_date(vo.getStart_date().replaceAll("[\\-\\: ]", "") + "00");
				vo.setEnd_date(vo.getEnd_date().replaceAll("[\\-\\: ]", "") + "00");
			}
		}
		
		HistoryVO content_vo = service.getAlbum(sr_pid, album_id);
		List<HistoryVO> list = service.getSrHistoryList(vo);
		vo.setPageCount(service.getSrHistoryListCnt(vo));
		
		model.addAttribute("sr_vo", sr_vo);
		model.addAttribute("album_vo", album_vo);
		model.addAttribute("vo", vo);
		model.addAttribute("content_vo", content_vo);
		model.addAttribute("list", list);
		model.addAttribute("avg", service.getSrHistoryListAvg(vo));
		return "/admin/starrating/getSrHistoryList";
	}
	
	/**
	 * 별점내역 엑셀다운
	 * @param response
	 * @param sr_pid
	 * @param findName
	 * @param findValue
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/starrating/albumExcelDown", method = RequestMethod.GET)
	@ResponseBody
	public byte[] albumExcelDown(HttpServletResponse response
			, @RequestParam(value="sr_pid", required=false, defaultValue="") String sr_pid
			, @RequestParam(value="findName", required=false, defaultValue="") String findName
			, @RequestParam(value="findValue", required=false, defaultValue="") String findValue) throws Exception {
		sr_pid = HTMLCleaner.clean(sr_pid);
		findName = HTMLCleaner.clean(findName);
		findValue = HTMLCleaner.clean(findValue);
		
		StarRatingSearchVO vo = new StarRatingSearchVO();
		vo.setExcel_yn("Y");
		vo.setSr_pid(sr_pid);
		vo.setFindName(findName);
		vo.setFindValue(findValue);
		
		List<Object> header = new ArrayList<Object>();
        List<List<Object>> data = new ArrayList<List<Object>>();
        
        header.add("번호");
        header.add("앨범ID");
        header.add("앨범이름");
        header.add("별점평균");
        
        List<HistoryVO> list = service.getAlbumHistoryList(vo);
        int pageCnt = service.getAlbumHistoryListCnt(vo);
        
        for(HistoryVO result : list){
        	List<Object> obj = new ArrayList<Object>();
        	obj.add(pageCnt-(Integer.parseInt(result.getRowno())-1));
        	obj.add(result.getAlbum_id());
        	obj.add(result.getAlbum_name());
        	obj.add(result.getSr_point());
        	
        	data.add(obj);
        }
        
        ExcelManager excelManager = new ExcelManager(header, data);
        excelManager.setSheetName("SR_ALBUM_HISTORY");
        excelManager.setWidth(6000);
        byte[] bytes = excelManager.makeExcel();

        response.setHeader("Content-Disposition", "attachment; filename=SR_ALBUM_HISTORY.xlsx");
        response.setContentLength(bytes.length);
        response.setContentType("application/vnd.ms-excel");

        return bytes;
	}
	
	/**
	 * 별점 주기 내역 엑셀다운 
	 * @param response
	 * @param sr_pid
	 * @param album_id
	 * @param start_date
	 * @param end_date
	 * @param findName
	 * @param findValue
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/starrating/srExcelDown", method = RequestMethod.GET)
	@ResponseBody
	public byte[] srExcelDown(HttpServletResponse response
			, @RequestParam(value="sr_pid", required=false, defaultValue="") String sr_pid
			, @RequestParam(value="album_id", required=false, defaultValue="") String album_id
			, @RequestParam(value="start_date", required=false, defaultValue="") String start_date
			, @RequestParam(value="end_date", required=false, defaultValue="") String end_date
			, @RequestParam(value="findName", required=false, defaultValue="") String findName
			, @RequestParam(value="findValue", required=false, defaultValue="") String findValue) throws Exception {
		sr_pid = HTMLCleaner.clean(sr_pid);
		album_id = HTMLCleaner.clean(album_id);
		start_date = HTMLCleaner.clean(start_date);
		end_date = HTMLCleaner.clean(end_date);
		findName = HTMLCleaner.clean(findName);
		findValue = HTMLCleaner.clean(findValue);
		
		StarRatingSearchVO vo = new StarRatingSearchVO();
		vo.setExcel_yn("Y");
		vo.setSr_pid(sr_pid);
		vo.setAlbum_id(album_id);
		vo.setStart_date(start_date);
		vo.setEnd_date(end_date);
		vo.setFindName(findName);
		vo.setFindValue(findValue);
		
		List<Object> header = new ArrayList<Object>();
        List<List<Object>> data = new ArrayList<List<Object>>();
        
        header.add("번호");
        header.add("가입자번호");
        header.add("가입자 맥주소");
        header.add("별점");
        header.add("등록일시");
        
        List<HistoryVO> list = service.getSrHistoryList(vo);
        int pageCnt = service.getSrHistoryListCnt(vo);
        
        for(HistoryVO result : list){
        	List<Object> obj = new ArrayList<Object>();
        	obj.add(pageCnt-(Integer.parseInt(result.getRowno())-1));
        	obj.add(result.getSa_id());
        	obj.add(result.getStb_mac());
        	obj.add(result.getSr_point());
        	obj.add(result.getReg_date());
        	
        	data.add(obj);
        }
        
        ExcelManager excelManager = new ExcelManager(header, data);
        excelManager.setSheetName("SR_HISTORY_" + album_id);
        excelManager.setWidth(6000);
        byte[] bytes = excelManager.makeExcel();

        response.setHeader("Content-Disposition", "attachment; filename=SR_HISTORY_"+ start_date.substring(0, 12) + "-" + end_date.substring(0, 12) + ".xlsx");
        response.setContentLength(bytes.length);
        response.setContentType("application/vnd.ms-excel");

        return bytes;
	}
}
