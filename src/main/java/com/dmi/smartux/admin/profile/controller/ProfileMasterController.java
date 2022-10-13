package com.dmi.smartux.admin.profile.controller;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dmi.smartux.admin.profile.service.ProfileMasterService;
import com.dmi.smartux.admin.profile.service.ProfileService;
import com.dmi.smartux.admin.profile.vo.ProfileMasterVO;
import com.dmi.smartux.admin.profile.vo.ProfileVO;
import com.dmi.smartux.common.exception.ExceptionHandler;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.CookieUtil;
import com.dmi.smartux.common.util.GlobalCom;

/**
 * 광고 마스터 컨트롤러 클래스
 *
 * @author Dongho, Shin
 */

@Controller
public class ProfileMasterController {
	
	@Autowired
	ProfileMasterService mService;

	@Autowired
	ProfileService mProfileService;
	
	private final Log applyLogger = LogFactory.getLog("adminApplyCacheHistroy");

	/**
	 * 프로필이미지 마스터 페이지
	 *
	 * @param model View에 값을 전달할 모델 객체
	 * @return 프로필이미지 마스터 페이지 url
	 */
	@RequestMapping(value = "/admin/profile/profileMaster", method = RequestMethod.GET)
	public String showProfileMaster(
			@RequestParam(value = "service_type", required = false, defaultValue = "") String service_type,
			Model model) throws Exception {

		String domain = SmartUXProperties.getProperty("smartux.domain.https");
		model.addAttribute("domain_https", domain);

		ProfileMasterVO profileMasterVO = new ProfileMasterVO();
		profileMasterVO.setServiceType(service_type);
		
		try {
			List<ProfileMasterVO> list = mService.getProfileMasterList(profileMasterVO);
			//System.out.println(list.toString());
			model.addAttribute("profileMaster", list);
			model.addAttribute("service_type", service_type);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return "admin/profile/profileMaster";
	}

	@RequestMapping(value = "/admin/profile/profileMaster", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> insertProfileMaster(
			@RequestParam(value = "profileMstId", required = false, defaultValue = "") String profileMstId,
			@RequestParam(value = "profileMstName", required = false, defaultValue = "") String profileMstName,
			@RequestParam(value = "serviceType", required = false, defaultValue = "") String serviceType,
			HttpServletRequest request) throws Exception {
		String resultCode = "";
		String resultMsg = "";

		if (!GlobalCom.isNull(profileMstId).isEmpty() && !GlobalCom.isNull(profileMstId).isEmpty()) {
			try {
				ProfileMasterVO vo = new ProfileMasterVO();
				vo.setProfileMstId(profileMstId);
				vo.setProfileMstName(profileMstName);
				vo.setServiceType(serviceType);
				
				String cookieID = CookieUtil.getCookieUserID(request);
				String ip = request.getRemoteAddr();

				vo.setRegID(cookieID);
				vo.setActID(cookieID);
				vo.setActIP(ip);
				
				int cnt = mService.checkProfileMaster(vo); 

				if(cnt > 0) {
					resultCode = SmartUXProperties.getProperty("flag.duplicate");
				}else {
					mService.insertProfileMaster(vo);
					resultCode = SmartUXProperties.getProperty("flag.success");
					resultMsg = SmartUXProperties.getProperty("message.success");
				}
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

	@RequestMapping(value = "/admin/profile/profileMaster", method = RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity<String> updateProfileMaster(
			@RequestBody final MultiValueMap<String, String> data,
			HttpServletRequest request) throws Exception {
		String resultCode = "";
		String resultMsg = "";

		Map<String, String> params = data.toSingleValueMap();
		String profileMstId = params.get("profileMstId");
		String profileMstName = params.get("profileMstName");
		String serviceType = params.get("serviceType");
		

		if (!GlobalCom.isNull(profileMstId).isEmpty() && !GlobalCom.isNull(profileMstName).isEmpty()) {
			try {
				ProfileMasterVO vo = new ProfileMasterVO();
				vo.setProfileMstId(profileMstId);
				vo.setProfileMstName(profileMstName);
				vo.setServiceType(serviceType);

				String cookieID = CookieUtil.getCookieUserID(request);
				String ip = request.getRemoteAddr();

				vo.setRegID(cookieID);
				vo.setActID(cookieID);
				vo.setActIP(ip);
				vo.setActGB("U");

				mService.updateProfileMaster(vo);

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
	 * 삭제
	 * @param data
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/profile/profileMaster", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<String> deleteProfileMaster(
			@RequestBody final MultiValueMap<String, String> data,
			HttpServletRequest request) throws Exception {
		String resultCode = "";
		String resultMsg = "";

		Map<String, String> params = data.toSingleValueMap();
		String profileMstId = params.get("profileMstId");
		//String profileMstName = params.get("profileMstName");
		

		if (!GlobalCom.isNull(profileMstId).isEmpty()) {
			try {
				ProfileVO profileVO = new ProfileVO();
				
				profileVO.setProfileMstId(profileMstId);
				
				List<ProfileVO> list = mProfileService.getProfileList(profileVO);
				
				if(list.size() > 0) {
					
					resultCode = "dup";
					resultMsg = SmartUXProperties.getProperty("message.fail");
				}else {
					ProfileMasterVO vo = new ProfileMasterVO();
					vo.setProfileMstId(profileMstId);
					
					String cookieID = CookieUtil.getCookieUserID(request);
					String ip = request.getRemoteAddr();
	
					vo.setRegID(cookieID);
					vo.setActID(cookieID);
					vo.setActIP(ip);
					vo.setActGB("D");
	
					mService.deleteProfileMaster(vo);
					
					resultCode = SmartUXProperties.getProperty("flag.success");
					resultMsg = SmartUXProperties.getProperty("message.success");
				}
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
	
	/*@RequestMapping(value = "/admin/profile/profileMaster", method = RequestMethod.DELETE)
	@ResponseBody
	public CUDResult deleteProfileMaster(
			@RequestBody final MultiValueMap<String, String> data,
			HttpServletRequest request) throws Exception {
		
		final CLog cLog = new CLog(applyLogger, request.getRemoteAddr(), request.getRequestURI(), request.getMethod());
		final String loginUser = CookieUtil.getCookieUserID(request);
		final CUDResult result = new CUDResult();
		
		Map<String, String> params = data.toSingleValueMap();
		final String profileMstId = params.get("profileMstId");
		

		if (!GlobalCom.isNull(profileMstId).isEmpty()) {
			try {
				cLog.startLog("프로필이미지 마스터 삭제");
				final String cookieID = CookieUtil.getCookieUserID(request);
				final String ip = request.getRemoteAddr();

				final String host =
						StringUtils.defaultString(GlobalCom.getProperties(SmartUXProperties.getProperty("filepath.hdtv.common"), "cache.sync.apply.send99"),"N").equals("Y") ?
						GlobalCom.getProperties(SmartUXProperties.getProperty("filepath.hdtv.common"), "cache.sync.public.host") + "|" + GlobalCom.getProperties(SmartUXProperties.getProperty("filepath.hdtv.common"), "cache.sync.public.host99"):
						GlobalCom.getProperties(SmartUXProperties.getProperty("filepath.hdtv.common"), "cache.sync.public.host");
				final String port = 
						StringUtils.defaultString(GlobalCom.getProperties(SmartUXProperties.getProperty("filepath.hdtv.common"), "cache.sync.apply.send99"),"N").equals("Y") ?
						GlobalCom.getProperties(SmartUXProperties.getProperty("filepath.hdtv.common"), "cache.sync.public.port") + "|" + GlobalCom.getProperties(SmartUXProperties.getProperty("filepath.hdtv.common"), "cache.sync.public.port99"):
						GlobalCom.getProperties(SmartUXProperties.getProperty("filepath.hdtv.common"), "cache.sync.public.port");
				
				String fileLockPath = SmartUXProperties.getProperty("HDTVAdsDao.refreshAds.fileLock");
				int waitTime = NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.wait"), 5);
				
				FileProcessLockFactory.start(fileLockPath, waitTime,  new FileProcessFunction("배너마스터 삭제 DB", ResultCode.ApplyDBWriteFail) {
					
					@Override
					public void run() throws Exception {
						ProfileMasterVO vo = new ProfileMasterVO();
						vo.setProfileMstId(profileMstId);
						vo.setRegID(cookieID);
						vo.setActID(cookieID);
						vo.setActIP(ip);
						if ("ad0000".equalsIgnoreCase(profileMstId)) {
							result.setFlag(SmartUXProperties.getProperty("flag.can_not_delete"));
							result.setMessage(SmartUXProperties.getProperty("message.can_not_delete", profileMstId));
						} else {
							int liveCount = mProfileService.getLiveCount(profileMstId);

							if (0 < liveCount) {
								result.setFlag(SmartUXProperties.getProperty("flag.bedata"));
								result.setMessage(SmartUXProperties.getProperty("message.bedata"));
							} else {
								mService.deleteProfileMaster(vo);
								result.setFlag(ResultCode.Success.getFlag());
								result.setMessage(ResultCode.Success.getFlag());
							}
						}
					}
				},  new FileProcessFunction("배너마스터 삭제 API호출", ResultCode.ApplyRequestFail){
					@Override
					public void run() throws Exception {
						if("0000".equals(result.getFlag())){
							
							//즉시적용 호출 시 sleep 값 추가 -> DB분리건으로 OGG가 10초 이상 걸리는 경우가 있어 즉시적용 시 반영이 안돼서 추가함
							Thread.sleep(Integer.parseInt(StringUtils.defaultIfEmpty(GlobalCom.getProperties(SmartUXProperties.getProperty("filepath.hdtv.common"), "apply.cache.call.sleep"), "10000")));

							RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
									host,
									port,
									SmartUXProperties.getProperty("HDTVAdsDao.refreshAds.url")+"?callByScheduler=A",
									StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.method"), "POST"),
									StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"),
									MediaType.APPLICATION_JSON.toString(),
									NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.timeout"), 60000)).getResponseBody());
							result.setFlag(codeMsg.getCode());
							result.setMessage(codeMsg.getMessage());
							cLog.middleLog("배너마스터 삭제", "API 호출 성공", codeMsg.getCode(), codeMsg.getMessage());
						}
					}
				});
				
			} catch (MimsCommonException e) {
				cLog.warnLog("배너마스터 삭제", "실패", e.getFlag(), e.getMessage(), e.getLocalizedMessage());
				result.setFlag(e.getFlag());
				result.setMessage(e.getMessage());
			} finally {
				cLog.endLog("배너마스터 삭제", loginUser, result.getFlag(), result.getMessage());
			}
		}
		return result;
	}*/
}
