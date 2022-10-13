package com.dmi.smartux.admin.profile.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
import com.dmi.smartux.admin.profile.service.ProfileMasterService;
import com.dmi.smartux.admin.profile.service.ProfileService;
import com.dmi.smartux.admin.profile.vo.ProfileMasterVO;
import com.dmi.smartux.admin.profile.vo.ProfileVO;
import com.dmi.smartux.common.exception.ExceptionHandler;
import com.dmi.smartux.common.exception.SmartUXException;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.CookieUtil;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.util.HTMLCleaner;
import com.dmi.smartux.common.vo.CUDResult;

import net.sf.json.JSONSerializer;

/**
 * 광고 관리 컨트롤러 클래스
 *
 * @author Dongho, Shin
 */

@Controller
public class ProfileController {
	
	private final Log applyLogger = LogFactory.getLog("adminApplyCacheHistroy");
	private final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	ProfileService mService;

	@Autowired
	ProfileMasterService mProfileMasterService;

	/**
	 * 광고 리스트 페이지
	 *
	 * @param findName    검색 카테고리
	 * @param findValue   검색 값
	 * @param pageNum     페이지 번호(기본 1)
	 * @param adsListMode 목록 모드(전체목록/라이브/만료)
	 * @param model       View에 값을 전달할 모델 객체
	 * @return 광고 리스트 페이지 URL
	 */
	@RequestMapping(value = "/admin/profile/getProfileList", method = RequestMethod.GET)
	public String showProfileList(
			@RequestParam(value = "profileMstId", required = false, defaultValue = "") String profileMstId,
			@RequestParam(value = "service_type", required = false, defaultValue = "") String service_type,
			Model model) throws Exception {

		
		model.addAttribute("service_type", service_type);
		
		List<ProfileMasterVO> masterList = new ArrayList<ProfileMasterVO>();
		
		
		ProfileVO profileVO = new ProfileVO();
		
		
		try {
			
			ProfileMasterVO profileMasterVO = new ProfileMasterVO();
			profileMasterVO.setServiceType(service_type);
			masterList = mProfileMasterService.getProfileMasterList(profileMasterVO);
			
			if(("").equals(profileMstId) || profileMstId == null) {
				if(masterList.size() > 0) {
					profileVO.setProfileMstId(masterList.get(0).getProfileMstId());
				}else {
					profileVO.setProfileMstId("");
				}
			}else {
				profileVO.setProfileMstId(profileMstId);
			}
			
			
			profileVO.setServiceType(service_type);
			List<ProfileVO> list = mService.getProfileList(profileVO);
			profileVO.setList(list);
			profileVO.setPageCount(mService.getProfileListCtn(profileVO));
		
		} catch(Exception e) {
			throw new Exception(SmartUXProperties.getProperty("message.db"));
		}
		
		
		model.addAttribute("vo", profileVO);
		model.addAttribute("masterList", masterList);

		return "admin/profile/profileList";
	}

	
	
	/**
	 * 광고 등록 페이지
	 *
	 * @param model View에 값을 전달할 모델 객체
	 * @return 광고 등록 페이지 URL
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/profile/getInsertProfileView", method = RequestMethod.GET)
	public String showInsertProfile(
			@RequestParam(value = "profileMstId", required = false, defaultValue = "") String profileMstId,
			@RequestParam(value = "profileMstName", required = false, defaultValue = "") String profileMstName,
			@RequestParam(value = "service_type", required = false, defaultValue = "") String service_type,
			Model model) throws Exception {

		//-------이미지 체크----------//
		int imgSize = Integer.parseInt(GlobalCom.isNull(SmartUXProperties.getProperty("ads.size"),""))/1024;
		String imgFormat = GlobalCom.isNull(SmartUXProperties.getProperty("ads.format"),"");
		model.addAttribute("imgSize", imgSize);
		model.addAttribute("imgFormat", imgFormat);
		//-------이미지 체크----------//		
		
		ProfileVO vo = new ProfileVO();
		vo.setProfileMstId(profileMstId);
		model.addAttribute("vo", vo);
		model.addAttribute("profileMstName", profileMstName);
		model.addAttribute("service_type", service_type);
		
		return "admin/profile/insertProfile";
	}

	
	/**
	 * 광고 수정 페이지
	 *
	 * @param num   광고 번호
	 * @param model View에 값을 전달할 모델 객체
	 * @return 광고 수정 페이지 URL
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/profile/getUpdateProfileView", method = RequestMethod.GET)
	public String showUpdateProfile(
			@RequestParam(value = "profileImgId", required = false, defaultValue = "") String profileImgId,
			@RequestParam(value = "profileMstId", required = false, defaultValue = "") String profileMstId,
			@RequestParam(value = "profileMstName", required = false, defaultValue = "") String profileMstName,
			@RequestParam(value = "service_type", required = false, defaultValue = "") String service_type,
			Model model) throws Exception {
		
		if(!("").equals(profileImgId) && profileImgId != null) {
			ProfileVO vo = mService.getProfile(profileImgId);
			
			
			String regID;
			String textUrl = "";
			String eventNm = "";
		//-------이미지 체크----------//
			int imgSize = Integer.parseInt(GlobalCom.isNull(SmartUXProperties.getProperty("ads.size"),""))/1024;
			String imgFormat = GlobalCom.isNull(SmartUXProperties.getProperty("ads.format"),"");
			model.addAttribute("imgSize", imgSize);
			model.addAttribute("imgFormat", imgFormat);
			model.addAttribute("textUrl", textUrl);
			model.addAttribute("profileMstId", profileMstId);
			model.addAttribute("profileMstName", profileMstName);
			model.addAttribute("service_type", service_type);
			model.addAttribute("vo", vo);
			
		
			
			
		}
		return "admin/profile/insertProfile";
	}

	

	/**
	 * 광고 삭제
	 *
	 * @param numbers 삭제할 광고 번호
	 * @return Json 결과(Code / Message)
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/profile/deleteProfile", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> deleteProfile(
			@RequestParam(value = "profileImgIds", required = false, defaultValue = "") String profileImgIds,
			HttpServletRequest request) throws Exception {
		String resultCode = "";
		String resultMsg = "";

		if (StringUtils.isNotEmpty(profileImgIds)) {
			try {
				String cookieID = CookieUtil.getCookieUserID(request);
				String ip = request.getRemoteAddr();
				
				String[] profileList = profileImgIds.split(",");

				for (String profile : profileList) {
				
					ProfileVO vo = mService.getProfile(profile);
					
					String orgImgUrl = vo.getOrgImgUrl();
					String delUrl = "";
					
					if(!("").equals(orgImgUrl) && orgImgUrl != null) {
						delUrl = SmartUXProperties.getProperty("imgupload.dir") + orgImgUrl;
						
						(new File(delUrl)).delete();
					}
				}
				mService.deleteProfile(profileImgIds, cookieID, ip);

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
	 * 광고 등록
	 *
	 * @param title       제목
	 * @param rolTime     광고 요청 시간
	 * @param adsType     광고 타입
	 * @param linkUrl     광고 링크 URL
	 * @param startTime   시작일
	 * @param expiredTime 만료일
	 * @param liveType    라이브 여부
	 * @param grade       노출 등급
	 * @param product_code
	 * @return redirect 주소
	 * @throws Exception
	 */
	
	@RequestMapping(value = "/admin/profile/insertProfile", method = RequestMethod.POST)
	public String insertProfile(
			@RequestParam(value = "profileImgId", required = false, defaultValue = "") String profileImgId,
			@RequestParam(value = "profileMstId", required = false, defaultValue = "") String profileMstId,
			@RequestParam(value = "profileImgName", required = false, defaultValue = "") String profileImgName,
			@RequestParam(value = "profileImgUrl", required = false, defaultValue = "") String profileImgUrl,
			@RequestParam(value = "orgImgUrl", required = false, defaultValue = "") String orgImgUrl,
			@RequestParam(value = "service_type", required = false, defaultValue = "") String service_type,
			@RequestParam(value = "file", required = false, defaultValue = "") MultipartFile uploadFile, HttpServletRequest request) throws Exception {
		
		
		//boolean isUpdate = 0 < num;

		ProfileVO profileVO = new ProfileVO();
		if(!("").equals(profileImgId) && profileImgId != null) {
			profileVO = mService.getProfile(profileImgId);
		}
		
		String saveFileName = "";
		String newFileName = "";
		String fileName = "";
		String delUrl = "";
		
		if (uploadFile.getSize() != 0L) {
			fileName = uploadFile.getOriginalFilename();
			String ext = FilenameUtils.getExtension(fileName); // 확장자 구하기
			ext = ext.toLowerCase();

			saveFileName = Long.toString(System.currentTimeMillis()) + "." + ext; // 시스템타임.확장자 구조로 한다
			newFileName = SmartUXProperties.getProperty("profile.imgupload.dir") + saveFileName;
			
			File file = new File(newFileName);
			uploadFile.transferTo(file);	
		}
		
		profileVO.setProfileMstId(profileMstId);
		profileVO.setProfileImgName(profileImgName);
		profileVO.setServiceType(service_type);
		if (StringUtils.isNotEmpty(saveFileName)) {
			
			if(!("").equals(orgImgUrl) && orgImgUrl != null) {
				delUrl = SmartUXProperties.getProperty("profile.imgupload.dir") + orgImgUrl;
				
				(new File(delUrl)).delete();
			}
			
			profileVO.setProfileImgUrl(saveFileName);
		}else {
			profileVO.setProfileImgUrl(orgImgUrl);
		}
		
		
		
		String cookieID = CookieUtil.getCookieUserID(request);

		/*profileVO.setRegID(cookieID);
		profileVO.setActID(cookieID);
		profileVO.setActIP(request.getRemoteAddr());
		*/
		
		try {
			if (("").equals(profileVO.getProfileImgId()) || profileVO.getProfileImgId() == null) {
				//profileVO.setActGbn("I");
				mService.insertProfile(profileVO);
				
			} else {
				//profileVO.setActGbn("U");
				mService.updateProfile(profileVO);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "redirect:/admin/profile/getProfileList.do?profileMstId="+profileMstId+"&service_type="+service_type;
	}

	

	@RequestMapping(value="/admin/profile/changeProfileOrder", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> updateOrdered(
			@RequestParam(value="seqs[]")		String[] seqs,
			@RequestParam(value="orders[]") 		int[] orders,
			@RequestParam(value="profileMstId") 		String profileMstId
			){

		HTMLCleaner cleaner		= new HTMLCleaner();
		
		String resultcode = "";
		String resultmessage = "";
		SmartUXException e = new SmartUXException();
		int count 	= 0;
		try{
	
			validateLengthChk(seqs, orders);
			count	= mService.updateSearch(orders, profileMstId, seqs);
			if(count == seqs.length){
				resultcode = SmartUXProperties.getProperty("flag.success");
				resultmessage = SmartUXProperties.getProperty("message.success");
			}else{
				e.setFlag("UPDATE FAIL SEARCH WORD");
				e.setMessage("업데이트 실패");
				throw e;
			}
			
		}catch(Exception ex){
			logger.info("SearchWordController updateOrdered: " + e + " | " + System.currentTimeMillis());
			ExceptionHandler handler = new ExceptionHandler(ex);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}
		HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
        return new ResponseEntity<String>("{\"flag\" : \"" + resultcode + "\", \"message\" : \"" + resultmessage + "\"}", responseHeaders, HttpStatus.CREATED);
	}



	private void validateLengthChk(String[] seqs, int[] orders) throws SmartUXException{
		SmartUXException exception = new SmartUXException();
		
		if(seqs == null || seqs.length == 0 || orders == null || orders.length == 0){	
			exception.setFlag("SEARCHWORD NOT FOUND PRARAM");
			exception.setMessage("필수 파라미터가 누락되었습니다.");
			throw exception;
		}
		
		if(seqs.length != orders.length){
			exception.setFlag("SEARCHWORD LENGTH ERR");
			exception.setMessage("필수 파라미터 길이가 상이합니다.");
			throw exception;
		}
	}
	
	/**
	* 프로필 즉시적용
	* @param request
	* @param response
	* @return
	* @throws Exception
	*/
	@RequestMapping(value="/admin/profile/applyCache", method=RequestMethod.POST)
	public ResponseEntity<String> applyCache(
		HttpServletRequest request, 
		HttpServletResponse response
		) throws Exception {
	
		final CLog cLog = new CLog(applyLogger, request.getRemoteAddr(), request.getRequestURI(), request.getMethod());
		final String loginUser = CookieUtil.getCookieUserID(request);
		final CUDResult result = new CUDResult();
		
		try {
			cLog.startLog("프로필 즉시적용", loginUser);
			final String host = SmartUXProperties.getProperty("cache.sync.public.host");
			final String port = SmartUXProperties.getProperty("cache.sync.public.port");
			
			String fileLockPath = SmartUXProperties.getProperty("ProfileDao.refreshProfileList.fileLock");
			int waitTime = NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.wait"), 5);
			
			FileProcessLockFactory.start(fileLockPath, waitTime,  new FileProcessFunction("프로필 즉시적용", ResultCode.ApplyRequestFail) {
				
				@Override
				public void run() throws Exception {
					
					RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
							host, 
							port, 
							SmartUXProperties.getProperty("Profile.refreshProfile.url"),
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.method"), "POST"), 
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"), 
							MediaType.APPLICATION_JSON.toString(),
							NumberUtils.toInt(SmartUXProperties.getProperty("Profile.refreshProfile.timeout"), 90000)).getResponseBody());
					
					result.setFlag(codeMsg.getCode());
					result.setMessage(codeMsg.getMessage());
					cLog.middleLog("프로필 즉시적용", "API 호출", codeMsg.getCode(), codeMsg.getMessage());
				}
			});
		} catch (MimsCommonException e) {
			cLog.warnLog("프로필 즉시적용", "실패", e.getFlag(), e.getMessage(), e.getLocalizedMessage());
			result.setFlag(e.getFlag());
			result.setMessage(e.getMessage());
		} finally {
			cLog.endLog("프로필 즉시적용", loginUser, result.getFlag(), result.getMessage());
		}
		
		String resultMsg = "{\"flag\" : \"" + result.getFlag() + "\", \"message\" : \"" + result.getMessage() + "\"}";
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(resultMsg).toString(), responseHeaders, HttpStatus.OK);
	}
}
