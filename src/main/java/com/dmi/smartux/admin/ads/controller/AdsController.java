package com.dmi.smartux.admin.ads.controller;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
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
import com.dmi.commons.utility.HttpClientUtils;
import com.dmi.commons.utility.RestfulUtils;
import com.dmi.smartux.admin.ads.service.AdsMasterService;
import com.dmi.smartux.admin.ads.service.AdsService;
import com.dmi.smartux.admin.ads.vo.AdsMasterVO;
import com.dmi.smartux.admin.ads.vo.AdsVO;
import com.dmi.smartux.admin.commonMng.service.CommonModuleService;
import com.dmi.smartux.admin.imcs.service.ImcsService;
import com.dmi.smartux.admin.statbbsAdmin.service.StatbbsAdminService;
import com.dmi.smartux.admin.statbbsAdmin.vo.StatbbsMiniListVo;
import com.dmi.smartux.admin.statbbsAdmin.vo.StatbbsUpdateVo;
import com.dmi.smartux.common.exception.ExceptionHandler;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.CookieUtil;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.util.HTMLCleaner;
import com.dmi.smartux.common.vo.CUDResult;

import net.sf.json.JSONSerializer;


/**
 * ?????? ?????? ???????????? ?????????
 *
 * @author Dongho, Shin
 */

@Controller
public class AdsController {
	
	private final Log applyLogger = LogFactory.getLog("adminApplyCacheHistroy");
	
	private final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	AdsService mService;

	@Autowired
	AdsMasterService mAdsMasterService;
	
	@Autowired
	StatbbsAdminService stat_service;

	@Autowired
	CommonModuleService commonModuleService;
	
	@Autowired
	ImcsService imcsService;
	
	/**
	 * ?????? ????????? ?????????
	 *
	 * @param findName    ?????? ????????????
	 * @param findValue   ?????? ???
	 * @param pageNum     ????????? ??????(?????? 1)
	 * @param adsListMode ?????? ??????(????????????/?????????/??????)
	 * @param model       View??? ?????? ????????? ?????? ??????
	 * @return ?????? ????????? ????????? URL
	 * @throws Exception 
	 */
	@RequestMapping(value = "/admin/ads/getAdsList", method = RequestMethod.GET)
	public String showAdsList(
			@RequestParam(value = "findName", required = false, defaultValue = "") String findName,
			@RequestParam(value = "findValue", required = false, defaultValue = "") String findValue,
			@RequestParam(value = "pageNum", required = false, defaultValue = "1") String pageNum,
			@RequestParam(value = "adsListMode", required = false, defaultValue = "total") String adsListMode,
			@RequestParam(value = "masterID", required = false, defaultValue = "") String masterID,
			@RequestParam(value = "scr_tp", required = false, defaultValue = "L") String scr_tp,
			Model model) throws Exception {

		List<AdsMasterVO> masterList = new ArrayList<AdsMasterVO>();
		AdsVO adsVO = new AdsVO();
		String masterYn = "Y";
		
		String domain = SmartUXProperties.getProperty("smartux.domain.https");
		model.addAttribute("domain_https", domain);

		findName = HTMLCleaner.clean(findName);
		findValue = HTMLCleaner.clean(findValue);
		pageNum = HTMLCleaner.clean(pageNum);
		
		adsVO.setFindName(StringUtils.defaultIfEmpty(findName, "TITLE"));
		adsVO.setFindValue(StringUtils.defaultString(findValue));

		adsVO.setPageSize(10);
		adsVO.setBlockSize(10);
		adsVO.setPageNum(GlobalCom.isNumber(Integer.parseInt(pageNum), 1));
		adsVO.setActGbn(scr_tp);
		adsVO.setAdsListMode(adsListMode);
		adsVO.setScr_tp(scr_tp);
		
		if (adsListMode.equals("live")) {
			adsVO.setLiveType("Y");
		} else if (adsListMode.equals("expire")) {
			adsVO.setLiveType("D");
		}
		
		try {
			masterList = mAdsMasterService.getAdsMasterList();
		} catch(Exception e) {
			throw new Exception(SmartUXProperties.getProperty("message.db"));
		}
		
		if(masterList.size() == 0) {
			masterYn = "N";
		} else {
			if(StringUtils.isEmpty(masterID)) {
				masterID = masterList.get(0).getMasterID();
			}
			
			adsVO.setMasterID(masterID);
			
			try {
				List<AdsVO> list = mService.getAdsList(adsVO);
				adsVO.setList(list);
				adsVO.setPageCount(mService.getAdsListCtn(adsVO));
			} catch (Exception e) {
				throw new Exception(SmartUXProperties.getProperty("message.db"));
			}
		}
		model.addAttribute("masterYn", masterYn);
		model.addAttribute("vo", adsVO);
		model.addAttribute("masterList", masterList);
		

		return "admin/ads/adsList";
	}

	/**
	 * ???????????? ????????????
	 * 2021.12.07 AMIMS ?????? : ?????????????????? ??????
	 * @param master_id
	 * @return ?????????
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/ads/applyCache", method = RequestMethod.POST)
	public ResponseEntity<String> activateCache(
			@RequestParam(value = "master_id", required = false, defaultValue = "") final String master_id,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		final CLog cLog = new CLog(applyLogger, request.getRemoteAddr(), request.getRequestURI(), request.getMethod());
		final String loginUser = CookieUtil.getCookieUserID(request);
		final CUDResult result = new CUDResult();
		
		
		try {
			cLog.startLog("???????????? ???????????? : ",  loginUser);
			final String host = SmartUXProperties.getProperty("cache.sync.public.host");
			final String port = SmartUXProperties.getProperty("cache.sync.public.port");

			String fileLockPath = SmartUXProperties.getProperty("SmartUXAdsDao.refreshAds.fileLock");
			int waitTime = NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.wait"), 5);

			FileProcessLockFactory.start(fileLockPath, waitTime,  new FileProcessFunction("????????? ?????? ?????? ?????? DB", ResultCode.ApplyDBWriteFail) {

				@Override
				public void run() throws Exception {
					try{
						mService.checkExpire();
						cLog.middleLog("???????????? ????????????", "????????? ?????? ?????? ?????? ??????DB", "??????");
					}catch(Exception e){
						cLog.warnLog("???????????? ????????????", "????????? ?????? ?????? ?????? ??????DB ??????", e.getMessage());
						throw e;
					}
				}
			}, new FileProcessFunction("???????????? ????????????", ResultCode.ApplyRequestFail) {

				@Override
				public void run() throws Exception {
					
					//???????????? ?????? ??? sleep ??? ?????? -> DB??????????????? OGG??? 10??? ?????? ????????? ????????? ?????? ???????????? ??? ????????? ????????? ?????????
					Thread.sleep(Integer.parseInt(org.apache.commons.lang.StringUtils.defaultIfEmpty(SmartUXProperties.getProperty("apply.cache.call.sleep"), "10000")));
					
					RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
							host,
							port,
							SmartUXProperties.getProperty("SmartUXAdsDao.refreshAds.url") + "?master_id=" + master_id,
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.method"), "POST"),
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"),
							MediaType.APPLICATION_JSON.toString(),
							NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.timeout"), 60000)).getResponseBody());

					result.setFlag(codeMsg.getCode());
					result.setMessage(codeMsg.getMessage());
					cLog.middleLog("???????????? ????????????", "API ??????", codeMsg.getCode(), codeMsg.getMessage());
				}
			});
		} catch (MimsCommonException e) {
			cLog.warnLog("???????????? ????????????", "??????", e.getFlag(), e.getMessage(), e.getLocalizedMessage());
			result.setFlag(e.getFlag());
			result.setMessage(e.getMessage());
		} finally {
			cLog.endLog("???????????? ????????????", loginUser, result.getFlag(), result.getMessage());
		}

		String resultMsg = "{\"flag\" : \"" + result.getFlag() + "\", \"message\" : \"" + result.getMessage() + "\"}";
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(resultMsg).toString(), responseHeaders, HttpStatus.OK);
	}
	
	/**
	 * ???????????? ?????? ????????????
	 *
	 * @return ?????????
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/ads/checkLastApply", method = RequestMethod.POST)
	public ResponseEntity<String> checkLastApply(
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		final CLog cLog = new CLog(applyLogger, request.getRemoteAddr(), request.getRequestURI(), request.getMethod());
		final String loginUser = CookieUtil.getCookieUserID(request);
		final CUDResult result = new CUDResult();
		
		try {
			String updated = mService.checkLastApply();
			String now = mService.getNow();
			
			if("".equals(updated) || updated == null) {
				result.setFlag("0000");
			}else {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				Date day1 = sdf.parse(updated);
				Date day2 = sdf.parse(now);
				
				if(day1.compareTo(day2) < 0) {
					result.setFlag("0000");
				}else {
					result.setFlag("9999");
				}
			}
		}catch (Exception e) {
			result.setFlag("9999");
			cLog.warnLog("?????????????????? ??????", "??????", e.getMessage(), e.getLocalizedMessage());
		}
		

		String resultMsg = "{\"flag\" : \"" + result.getFlag() + "\", \"message\" : \"" + result.getMessage() + "\"}";
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(resultMsg).toString(), responseHeaders, HttpStatus.OK);
	}
	
	/**
	 * ?????? ?????? ????????????
	 *
	 * @return ?????????
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/ads/applyVersionCache", method = RequestMethod.POST)
	public ResponseEntity<String> applyVersionCache(
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		final CLog cLog = new CLog(applyLogger, request.getRemoteAddr(), request.getRequestURI(), request.getMethod());
		final String loginUser = CookieUtil.getCookieUserID(request);
		final CUDResult result = new CUDResult();
		
		try {
			mService.insertBannerVersion();
			result.setFlag("0000");
		}catch (Exception e) {
			result.setFlag("9999");
			cLog.warnLog("?????????????????? ??????", "??????", e.getMessage(), e.getLocalizedMessage());
		}
		

		String resultMsg = "{\"flag\" : \"" + result.getFlag() + "\", \"message\" : \"" + result.getMessage() + "\"}";
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(resultMsg).toString(), responseHeaders, HttpStatus.OK);
	}
	
	/**
	 * ?????? ?????? ?????????
	 *
	 * @param model View??? ?????? ????????? ?????? ??????
	 * @return ?????? ?????? ????????? URL
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/ads/getInsertAdsView", method = RequestMethod.GET)
	public String showInsertAds(
			@RequestParam(value = "adsListMode", required = false, defaultValue = "total") String adsListMode,
			@RequestParam(value = "masterID", required = false, defaultValue = "") String masterID,
			@RequestParam(value = "scr_tp", required = false, defaultValue = "L") String scr_tp,
			Model model) throws Exception {
		
		if(StringUtils.isEmpty(masterID)) {
			throw new Exception(SmartUXProperties.getProperty("message.paramnotfound", "?????? ????????? ?????????"));
		}
		
		SimpleDateFormat currentDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:00", Locale.KOREA);
		GregorianCalendar cal = new GregorianCalendar();
		String currentDate = currentDateFormat.format(cal.getTime());

		cal.add(Calendar.DAY_OF_MONTH, 7);

		SimpleDateFormat nextWeekDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:59", Locale.KOREA);
		String nextWeekDate = nextWeekDateFormat.format(cal.getTime());

		//-------????????? ??????----------//
		int imgSize = Integer.parseInt(GlobalCom.isNull(SmartUXProperties.getProperty("ads.size"),""))/1024;
		String imgFormat = GlobalCom.isNull(SmartUXProperties.getProperty("ads.format"),"");
		model.addAttribute("imgSize", imgSize);
		model.addAttribute("imgFormat", imgFormat);
		//-------????????? ??????----------//
		
		//-------????????????----------//
		String statbbsListCnt = SmartUXProperties.getProperty("statbbs.sbox.listcnt");//?????? ????????? ???????????? ?????????(?????? ????????? ??????????????? ??? ????????? ????????????)
		List<StatbbsMiniListVo> sboxListVo = stat_service.getStatbbsActive(statbbsListCnt);
		//-------????????????----------//
		
		AdsVO vo = new AdsVO();
		vo.setNumber(0);
		vo.setMasterID(masterID);
		vo.setScr_tp(scr_tp);
		vo.setRolTime(10);
		vo.setUpdateYn("Y");
		vo.setStartTime(new Date());
		vo.setAdsListMode(adsListMode);
		model.addAttribute("vo", vo);
		model.addAttribute("currentDate", currentDate);
		model.addAttribute("nextWeekDate", nextWeekDate);
		model.addAttribute("sbox_vo", sboxListVo);
		
		return "admin/ads/insertAds";
	}

	/**
	 * ?????? ?????? ?????????
	 *
	 * @param num   ?????? ??????
	 * @param model View??? ?????? ????????? ?????? ??????
	 * @return ?????? ?????? ????????? URL
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/ads/getUpdateAdsView", method = RequestMethod.GET)
	public String showUpdateAds(
			@RequestParam(value = "num", required = false, defaultValue = "0") int num,
			@RequestParam(value = "adsListMode", required = false, defaultValue = "total") String adsListMode,
			@RequestParam(value = "scr_tp", required = false, defaultValue = "L") String scr_tp,
			Model model) throws Exception {
		if (0 < num) {
			AdsVO vo = mService.getAds(num);
			vo.setAdsListMode(adsListMode);
			vo.setScr_tp(scr_tp);
			
			String categoryId = "";
			String textUrl = "";

			switch (vo.getType()) {
			case 1: //????????? ??????
				if (vo.getLinkUrl() != null) {
					textUrl = vo.getLinkUrl();
				}
				break;
			case 2: //?????????
				if (vo.getLinkUrl() != null) {
					String[] linkUrlArray = vo.getLinkUrl().split("\\|\\|");
					if(linkUrlArray.length>1){
						categoryId = linkUrlArray[0];
						String contentsId = linkUrlArray[1];
						textUrl = commonModuleService.getContentsView(categoryId, contentsId).getAlbum_name();
					}
				}
				break;
			case 3: // ????????????
				if (vo.getLinkUrl() != null) {
					if(vo.getLinkUrl().split("\\|\\|").length>1){
						categoryId = vo.getLinkUrl().split("\\|\\|")[0];
						textUrl = commonModuleService.getCategoryView(categoryId).getCategory_name();
					}
				}
				break;
			case 6: // ??????
				if (vo.getLinkUrl() != null) {
					textUrl = vo.getLinkUrl();
				}
				break;
			default:
				break;
			}

			if(!GlobalCom.isNull(vo.getEventID()).equals("")){
				StatbbsUpdateVo joinVo = new StatbbsUpdateVo();
				joinVo = stat_service.setStatbbsUpdate(vo.getEventID());
				if(joinVo != null) { 
					vo.setEventName(joinVo.getTitle());
				} else {
					vo.setEventName("???????????? ?????? ??? ??????");
				}
			}
			
			//-------????????????----------//
			String statbbsListCnt = SmartUXProperties.getProperty("statbbs.sbox.listcnt");//?????? ????????? ???????????? ?????????(?????? ????????? ??????????????? ??? ????????? ????????????)
			List<StatbbsMiniListVo> sboxListVo = stat_service.getStatbbsActive(statbbsListCnt);
			//-------????????????----------//
			
			//-------????????? ??????----------//
			int imgSize = Integer.parseInt(GlobalCom.isNull(SmartUXProperties.getProperty("ads.size"),""))/1024;
			String imgFormat = GlobalCom.isNull(SmartUXProperties.getProperty("ads.format"),"");
			model.addAttribute("imgSize", imgSize);
			model.addAttribute("imgFormat", imgFormat);
			model.addAttribute("textUrl", textUrl);
			model.addAttribute("vo", vo);
			model.addAttribute("sbox_vo", sboxListVo);
		}
		return "admin/ads/insertAds";
	}

	/**
	 * ?????? ?????? ?????????
	 *
	 * @param model View??? ?????? ????????? ?????? ??????
	 * @return ?????? ?????? ?????????
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/ads/changeOrder", method = RequestMethod.GET)
	public String showChangeOrder(
			@RequestParam(value = "masterID", required = false, defaultValue = "") String masterID,
			Model model) throws Exception {
		
		if(StringUtils.isEmpty(masterID)) {
			throw new Exception(SmartUXProperties.getProperty("message.paramnotfound", "?????? ????????? ?????????"));
		}
		
		List<AdsVO> result = mService.getAllList(masterID);
		model.addAttribute("result", result);
		return "admin/ads/changeAdsOrder";
	}

	/**
	 * ?????? ??????
	 *
	 * @param numbers ????????? ?????? ??????
	 * @return Json ??????(Code / Message)
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/ads/deleteAds", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> deleteAds(
			@RequestParam(value = "numbers", required = false, defaultValue = "") String numbers,
			HttpServletRequest request) throws Exception {
		String resultCode = "";
		String resultMsg = "";

		if (StringUtils.isNotEmpty(numbers)) {
			try {
				String cookieID = CookieUtil.getCookieUserID(request);
				String ip = request.getRemoteAddr();

				mService.deleteAds(numbers, cookieID, ip);

				resultCode = SmartUXProperties.getProperty("flag.success");
				resultMsg = SmartUXProperties.getProperty("message.success");
			} catch (Exception e) {
				ExceptionHandler handler = new ExceptionHandler(e);
				resultCode = handler.getFlag();
				resultMsg = handler.getMessage();
			}
		}

		String result = "{\"flag\" : \"" + resultCode + "\", \"message\" : \"" + resultMsg + "\"}";

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(result, responseHeaders, HttpStatus.CREATED);
	}

	/**
	 * ?????? ??????
	 *
	 * @param title       ??????
	 * @param rolTime     ?????? ?????? ??????
	 * @param adsType     ?????? ??????
	 * @param linkUrl     ?????? ?????? URL
	 * @param startTime   ?????????
	 * @param expiredTime ?????????
	 * @param liveType    ????????? ??????
	 * @param grade       ?????? ??????
	 * @return redirect ??????
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/ads/insertAds", method = RequestMethod.POST)
	public String insertAds(
			@RequestParam(value = "num", required = false, defaultValue = "0") int num,
			@RequestParam(value = "title", required = false, defaultValue = "") String title,
			@RequestParam(value = "rolTime", required = false, defaultValue = "10") int rolTime,
			@RequestParam(value = "adsType", required = false, defaultValue = "") int adsType,
			@RequestParam(value = "file", required = false, defaultValue = "") MultipartFile uploadFile,
			@RequestParam(value = "fileName", required = false, defaultValue = "") String fileName,
			@RequestParam(value = "file2", required = false, defaultValue = "") MultipartFile uploadFile2,
			@RequestParam(value = "fileName2", required = false, defaultValue = "") String fileName2,
			@RequestParam(value = "linkUrl", required = false, defaultValue = "") String linkUrl,
			@RequestParam(value = "startTime", required = false, defaultValue = "") String startTime,
			@RequestParam(value = "expiredTime", required = false, defaultValue = "") String expiredTime,
			@RequestParam(value = "liveType", required = false, defaultValue = "N") String liveType,
			@RequestParam(value = "grade", required = false, defaultValue = "01") String grade,
			@RequestParam(value = "eventID", required = false, defaultValue = "0") String eventID,
			@RequestParam(value = "masterID", required = false, defaultValue = "") String masterID,
			@RequestParam(value = "picker", required = false, defaultValue = "") String bgColor,
			@RequestParam(value = "verticalBgFile", required = false, defaultValue = "") MultipartFile verticalBgFile,
			@RequestParam(value = "verticalBgFileName", required = false, defaultValue = "") String verticalBgFileName,
			@RequestParam(value = "horizontalBgFile", required = false, defaultValue = "") MultipartFile horizontalBgFile,
			@RequestParam(value = "horizontalBgFileName", required = false, defaultValue = "") String horizontalBgFileName,
			@RequestParam(value = "dateType", required = false, defaultValue = "00") String dateType,
			@RequestParam(value = "adsListMode", required = false, defaultValue = "total") String setAdsListMode,
			@RequestParam(value = "scr_tp", required = false, defaultValue = "L") String scr_tp,
			@RequestParam(value = "textEtc", required = false, defaultValue = "") String textEtc,
			@RequestParam(value = "textEtc2", required = false, defaultValue = "") String textEtc2,
			HttpServletRequest request) throws Exception {
				
		if(StringUtils.isEmpty(masterID)) {
			throw new Exception(SmartUXProperties.getProperty("message.paramnotfound", "?????? ????????? ?????????"));
		}
		
		masterID = masterID.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
		scr_tp = scr_tp.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
		setAdsListMode = setAdsListMode.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
		title = title.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
		
		title = HTMLCleaner.clean(title);
		textEtc = HTMLCleaner.cleanwithoutLoginfunc(textEtc);
		textEtc2 = HTMLCleaner.cleanwithoutLoginfunc(textEtc2);
		
		boolean isUpdate = 0 < num;

		AdsVO adsVO = new AdsVO();

		if (isUpdate) {
			adsVO = mService.getAds(num);
		}

		String saveFileName = "";
		String saveFileName2 = "";
		String verticalSaveFileName = "";
		String horizontalSaveFileName = "";

		if (uploadFile.getSize() != 0L) {
			fileName = uploadFile.getOriginalFilename();
			String ext = FilenameUtils.getExtension(fileName); // ????????? ?????????
			ext = ext.toLowerCase();

			saveFileName = Long.toString(System.currentTimeMillis()) + "." + ext; // ???????????????.????????? ????????? ??????
			String newFileName = SmartUXProperties.getProperty("imgupload.dir") + "ads/" + saveFileName;

			File file = new File(newFileName);
			uploadFile.transferTo(file);
		}

		if (uploadFile2.getSize() != 0L) {
			fileName2 = uploadFile2.getOriginalFilename();
			String ext = FilenameUtils.getExtension(fileName2); // ????????? ?????????
			ext = ext.toLowerCase();

			saveFileName2 = Long.toString(System.currentTimeMillis()) + "." + ext; // ???????????????.????????? ????????? ??????
			String newFileName = SmartUXProperties.getProperty("imgupload.dir") + "ads/" + saveFileName2;

			File file = new File(newFileName);
			uploadFile2.transferTo(file);
		}

		if (verticalBgFile.getSize() != 0L) {
			verticalBgFileName = verticalBgFile.getOriginalFilename();
			String ext = FilenameUtils.getExtension(verticalBgFileName); // ????????? ?????????
			ext = ext.toLowerCase();

			verticalSaveFileName = Long.toString(System.currentTimeMillis()) + "." + ext; // ???????????????.????????? ????????? ??????
			String newFileName = SmartUXProperties.getProperty("imgupload.dir") + "ads/" + verticalSaveFileName;

			File file = new File(newFileName);
			verticalBgFile.transferTo(file);
		}

		if (horizontalBgFile.getSize() != 0L) {
			horizontalBgFileName = horizontalBgFile.getOriginalFilename();
			String ext = FilenameUtils.getExtension(horizontalBgFileName); // ????????? ?????????
			ext = ext.toLowerCase();

			horizontalSaveFileName = Long.toString(System.currentTimeMillis()) + "." + ext; // ???????????????.????????? ????????? ??????
			String newFileName = SmartUXProperties.getProperty("imgupload.dir") + "ads/" + horizontalSaveFileName;

			File file = new File(newFileName);
			horizontalBgFile.transferTo(file);
		}

		if (isUpdate && StringUtils.isNotEmpty(fileName) && StringUtils.isEmpty(saveFileName)) {
			saveFileName = adsVO.getSaveFileName();
		}

		if (isUpdate && StringUtils.isNotEmpty(fileName2) && StringUtils.isEmpty(saveFileName2)) {
			saveFileName2 = adsVO.getSaveFileName2();
		}

		if (isUpdate && StringUtils.isNotEmpty(verticalBgFileName) && StringUtils.isEmpty(verticalSaveFileName)) {
			verticalSaveFileName = adsVO.getVerticalBgSaveFileName();
		}

		if (isUpdate && StringUtils.isNotEmpty(horizontalBgFileName) && StringUtils.isEmpty(horizontalSaveFileName)) {
			horizontalSaveFileName = adsVO.getHorizontalBgSaveFileName();
		}

		adsVO.setRolTime(rolTime);

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA);
		
		title = title.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
		
		adsVO.setTitle(title);
		adsVO.setType(adsType);
		adsVO.setLinkUrl(linkUrl);
		adsVO.setStartTime(dateFormat.parse(startTime));
		adsVO.setExpiredTime(dateFormat.parse(expiredTime));
		adsVO.setLiveType(liveType);
		adsVO.setGrade(grade);
		adsVO.setMasterID(masterID);
		adsVO.setBgColor(bgColor);
		adsVO.setFileName(fileName);
		adsVO.setSaveFileName(FilenameUtils.getName(saveFileName));
		adsVO.setFileName2(fileName2);
		adsVO.setSaveFileName2(FilenameUtils.getName(saveFileName2));
		adsVO.setVerticalBgFileName(verticalBgFileName);
		adsVO.setVerticalBgSaveFileName(FilenameUtils.getName(verticalSaveFileName));
		adsVO.setHorizontalBgFileName(horizontalBgFileName);
		adsVO.setHorizontalBgSaveFileName(FilenameUtils.getName(horizontalSaveFileName));
		adsVO.setDateType(dateType);
		adsVO.setTextEtc(textEtc);
		adsVO.setTextEtc2(textEtc2);

		if (StringUtils.isNotBlank(eventID) && !"0".equals(eventID)) {
			adsVO.setEventID(eventID);
		} else {
			adsVO.setEventID("");
		}

		String cookieID = CookieUtil.getCookieUserID(request);

		adsVO.setRegID(cookieID);
		adsVO.setActID(cookieID);
		adsVO.setActIP(request.getRemoteAddr());

		int seq = 0;
		
		try {
			if (!isUpdate) {
				adsVO.setActGbn("I");
				seq = mService.insertAds(adsVO);
			} else {
				adsVO.setActGbn("U");
				mService.updateAds(adsVO);
			}
			
			//2021-04-22 BPAS ???????????? ??????
			try {
				boolean imcsProcess = false; 
				String imcsType = "";
				String imcsId = "";
				if(adsType == 2) {
					imcsProcess = true;
					imcsType = "ALB";
					String[] linkUrl_arr = linkUrl.split("\\|\\|");
					if(linkUrl_arr.length > 1) {
						imcsId = linkUrl_arr[1];
					}
				}else if(adsType == 3) {
					imcsProcess = true;
					imcsType = "CAT";
					String[] linkUrl_arr = linkUrl.split("\\|\\|");
					if(linkUrl_arr.length > 0) {
						imcsId = linkUrl_arr[0];
					}
				}
				if(imcsProcess) {
					if (!isUpdate) {
						imcsService.insertImcs("I", "BAN", String.valueOf(seq), imcsType, imcsId, "N");
					}else {
						imcsService.insertImcs("U", "BAN", String.valueOf(adsVO.getNumber()), imcsType, imcsId, "N");
					}
				}
			}catch (Exception e) {
				logger.error("[insertImcs]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "redirect:/admin/ads/getAdsList.do?adsListMode="+setAdsListMode+"&masterID="+masterID+"&scr_tp=" + scr_tp;
	}
	

	@RequestMapping(value = "/admin/ads/changeOrder", method = RequestMethod.PUT)
	@ResponseBody
	public String changeOrder(
			@RequestBody final MultiValueMap<String, String> data,
			HttpServletRequest request) throws Exception {
		String resultCode;
		String resultMsg;

		try {

			List<String> optionArray = data.get("optionArray[]");
			mService.changeOrder(optionArray);

			resultCode = SmartUXProperties.getProperty("flag.success");
			resultMsg = SmartUXProperties.getProperty("message.success");
		} catch (Exception e) {
			ExceptionHandler handler = new ExceptionHandler(e);
			resultCode = handler.getFlag();
			resultMsg = handler.getMessage();
		}

		return "{\"flag\" : \"" + resultCode + "\", \"message\" : \"" + resultMsg + "\"}";
	}
}
