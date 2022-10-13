package com.dmi.smartux.admin.ads.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
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
import com.dmi.smartux.common.exception.ExceptionHandler;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.CookieUtil;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.util.HTMLCleaner;
import com.dmi.smartux.common.vo.CUDResult;



/**
 * 광고 마스터 컨트롤러 클래스
 *
 * @author Dongho, Shin
 */

@Controller
public class AdsMasterController {
	@Autowired
	AdsMasterService mService;

	@Autowired
	AdsService mAdsService;

	private final Log applyLogger = LogFactory.getLog("adminApplyCacheHistroy");
	
	private ObjectMapper om = new ObjectMapper();
	
	/**
	 * 광고 마스터 페이지
	 *
	 * @param model View에 값을 전달할 모델 객체
	 * @return 광고 마스터 페이지 url
	 */
	@RequestMapping(value = "/admin/ads/adsMaster", method = RequestMethod.GET)
	public String showAdsMaster(Model model) throws Exception {

		String domain = SmartUXProperties.getProperty("smartux.domain.https");
		model.addAttribute("domain_https", domain);

		try {
			List<AdsMasterVO> list = mService.getAdsMasterList();
			String newId = mService.getAdsMasterId();
			model.addAttribute("adsMaster", list);
			model.addAttribute("newId", newId);
		} catch (Exception ignored) {
		}

		return "admin/ads/adsMaster";
	}

	@RequestMapping(value = "/admin/ads/adsMaster", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> insertAdsMaster(
			@RequestParam(value = "masterID", required = false, defaultValue = "") String masterID,
			@RequestParam(value = "masterName", required = false, defaultValue = "") String masterName,
			@RequestParam(value = "liveCount", required = false, defaultValue = "10") int liveCount,
			HttpServletRequest request) throws Exception {
		String resultCode = "";
		String resultMsg = "";
		masterName = masterName.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
		if (!GlobalCom.isNull(masterID).isEmpty() && !GlobalCom.isNull(masterName).isEmpty()) {
			try {
				
				masterName = HTMLCleaner.clean(masterName);
				
				AdsMasterVO vo = new AdsMasterVO();
				vo.setMasterID(masterID);
				vo.setMasterName(masterName);
				vo.setLiveCount(liveCount);

				String cookieID = CookieUtil.getCookieUserID(request);
				String ip = request.getRemoteAddr();

				vo.setRegID(cookieID);
				vo.setActID(cookieID);
				vo.setActIP(ip);

				mService.insertAdsMaster(vo);

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

	@RequestMapping(value = "/admin/ads/adsMaster", method = RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity<String> updateAdsMaster(
			@RequestBody final MultiValueMap<String, String> data,
			HttpServletRequest request) throws Exception {
		String resultCode = "";
		String resultMsg = "";

		Map<String, String> params = data.toSingleValueMap();
		String masterID = params.get("masterID");
		String masterName = params.get("masterName");

		if (!GlobalCom.isNull(masterID).isEmpty() && !GlobalCom.isNull(masterName).isEmpty()) {
			try {
				masterName = masterName.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
				masterName = HTMLCleaner.clean(masterName);
				AdsMasterVO vo = new AdsMasterVO();
				vo.setMasterID(masterID);
				vo.setMasterName(masterName);

				String cookieID = CookieUtil.getCookieUserID(request);
				String ip = request.getRemoteAddr();

				vo.setRegID(cookieID);
				vo.setActID(cookieID);
				vo.setActIP(ip);
				vo.setActGB("U");

				mService.updateAdsMaster(vo);

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
	 * 광고 삭제
	 * @param data
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/ads/adsMaster", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteAdsMaster(
			@RequestBody final MultiValueMap<String, String> data,
			HttpServletRequest request) throws Exception {
		
		final CLog cLog = new CLog(applyLogger, request.getRemoteAddr(), request.getRequestURI(), request.getMethod());
		final String loginUser = CookieUtil.getCookieUserID(request);
		final CUDResult result = new CUDResult();
		Map<String, String> params = data.toSingleValueMap();
		final String masterID = params.get("masterID");
		
		if (!GlobalCom.isNull(masterID).isEmpty()) {
			try{
				cLog.startLog("배너마스터 삭제");
				final String cookieID = CookieUtil.getCookieUserID(request);
				final String ip = request.getRemoteAddr();
				
				final String host = SmartUXProperties.getProperty("cache.sync.public.host");
				final String port = SmartUXProperties.getProperty("cache.sync.public.port");

				String fileLockPath = SmartUXProperties.getProperty("SmartUXAdsDao.refreshAds.fileLock");
				int waitTime = NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.wait"), 5);
				FileProcessLockFactory.start(fileLockPath, waitTime,  new FileProcessFunction("배너마스터 삭제 DB", ResultCode.ApplyDBWriteFail) {

					@Override
					public void run() throws Exception {
						AdsMasterVO vo = new AdsMasterVO();
						vo.setMasterID(masterID);
						vo.setRegID(cookieID);
						vo.setActID(cookieID);
						vo.setActIP(ip);
						
						int liveCount = mAdsService.getLiveCount(masterID);
						
						if(0 < liveCount){
							result.setFlag(SmartUXProperties.getProperty("flag.bedata"));
							result.setMessage(SmartUXProperties.getProperty("message.bedata"));
						}else{
							mService.deleteAdsMaster(vo);
							result.setFlag(ResultCode.Success.getFlag());
							result.setMessage(ResultCode.Success.getFlag());
						}
					}
					
				},  new FileProcessFunction("배너마스터 삭제 API호출", ResultCode.ApplyRequestFail){

					@Override
					public void run() throws Exception {
						if("0000".equals(result.getFlag())){
							
							//즉시적용 호출 시 sleep 값 추가 -> DB분리건으로 OGG가 10초 이상 걸리는 경우가 있어 즉시적용 시 반영이 안돼서 추가함
							Thread.sleep(Integer.parseInt(org.apache.commons.lang.StringUtils.defaultIfEmpty(SmartUXProperties.getProperty("apply.cache.call.sleep"), "10000")));
							
							RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
									host,
									port,
									SmartUXProperties.getProperty("SmartUXAdsDao.refreshAds.url"),
									org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.method"), "POST"),
									org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"),
									MediaType.APPLICATION_JSON.toString(),
									NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.timeout"), 60000)).getResponseBody());

							result.setFlag(codeMsg.getCode());
							result.setMessage(codeMsg.getMessage());
							cLog.middleLog("배너광고 즉시적용", "API 호출", codeMsg.getCode(), codeMsg.getMessage());
						}
					}
				});
				
			}catch(MimsCommonException e){
				cLog.warnLog("배너마스터 삭제", "실패", e.getFlag(), e.getMessage(), e.getLocalizedMessage());
				result.setFlag(e.getFlag());
				result.setMessage(e.getMessage());
			} finally {
				cLog.endLog("배너마스터 삭제", loginUser, result.getFlag(), result.getMessage());
			}
		} else {
			result.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			result.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "배너 마스터 아이디"));
		}
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		return new ResponseEntity<String>(om.writeValueAsString(result), responseHeaders, HttpStatus.OK);
	}
	
}
	
