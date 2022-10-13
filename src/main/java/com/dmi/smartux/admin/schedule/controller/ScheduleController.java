package com.dmi.smartux.admin.schedule.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
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
import org.springframework.util.CollectionUtils;
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
import com.dmi.smartux.admin.abtest.service.ABTestService;
import com.dmi.smartux.admin.abtest.vo.ABTestSearchVO;
import com.dmi.smartux.admin.abtest.vo.ABTestVO;
import com.dmi.smartux.admin.schedule.service.ScheduleService;
import com.dmi.smartux.admin.schedule.vo.CategoryAlbumVO;
import com.dmi.smartux.admin.schedule.vo.ScheduleDetailVO;
import com.dmi.smartux.admin.schedule.vo.ScheduleVO;
import com.dmi.smartux.common.exception.ExceptionHandler;
import com.dmi.smartux.common.exception.SmartUXException;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.CookieUtil;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.util.HTMLCleaner;
import com.dmi.smartux.common.vo.CUDResult;

import net.sf.json.JSONSerializer;

@Controller
public class ScheduleController {
	
	@Autowired
	ScheduleService service;
	@Autowired
	ABTestService abtestService;
	
	private ObjectMapper om = new ObjectMapper();
	
	private final Log applyLogger = LogFactory.getLog("adminApplyCacheHistroy");
	
	/**
	 * 자체편성 전체 즉시적용
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/schedule/activateCache", method=RequestMethod.POST)
	public ResponseEntity<String> activateCache(
			HttpServletRequest request, 
			HttpServletResponse response,
			@RequestParam(value="schedule_yn", defaultValue="Y")  String schedule_yn,
			@RequestParam(value="callByScheduler",  defaultValue="Y") final  String callByScheduler
			) throws Exception {
		
		final CLog cLog = new CLog(applyLogger, request.getRemoteAddr(), request.getRequestURI(), request.getMethod());
		final String loginUser = schedule_yn.equals("Y") ? "batch" : CookieUtil.getCookieUserID(request);
		final CUDResult result = new CUDResult();
		
		try {
			cLog.startLog("자체편성 즉시적용 : ",  loginUser);
			final String host = SmartUXProperties.getProperty("cache.sync.public.host");
			final String port = SmartUXProperties.getProperty("cache.sync.public.port");
			
			String fileLockPath = SmartUXProperties.getProperty("SUXMAlbumListDao.refreshSUXMAlbumList.fileLock");
			int waitTime = NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.wait"), 5);
			
			FileProcessLockFactory.start(fileLockPath, waitTime,  new FileProcessFunction("자체편성 즉시적용", ResultCode.ApplyRequestFail) {
				
				@Override
				public void run() throws Exception {
					
					//즉시적용 호출 시 sleep 값 추가 -> DB분리건으로 OGG가 10초 이상 걸리는 경우가 있어 즉시적용 시 반영이 안돼서 추가함
					Thread.sleep(Integer.parseInt(org.apache.commons.lang.StringUtils.defaultIfEmpty(SmartUXProperties.getProperty("apply.cache.call.sleep"), "10000")));
					
					RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
							host, 
							port, 
							SmartUXProperties.getProperty("SUXMAlbumListDao.refreshSUXMAlbumList.url")+"?callByScheduler="+callByScheduler,
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.method"), "POST"), 
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"), 
							MediaType.APPLICATION_JSON.toString(),
							NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.timeout"), 60000)).getResponseBody());
					
					result.setFlag(codeMsg.getCode());
					result.setMessage(codeMsg.getMessage());
					cLog.middleLog("자체편성 즉시적용", "API 호출", codeMsg.getCode(), codeMsg.getMessage());
				}
			});
		} catch (MimsCommonException e) {
			cLog.warnLog("자체편성 즉시적용", "실패", e.getFlag(), e.getMessage(), e.getLocalizedMessage());
			result.setFlag(e.getFlag());
			result.setMessage(e.getMessage());
		} finally {
			cLog.endLog("자체편성 즉시적용", loginUser, result.getFlag(), result.getMessage());
		}

		String resultMsg = "{\"flag\" : \"" + result.getFlag() + "\", \"message\" : \"" + result.getMessage() + "\"}";
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(resultMsg).toString(), responseHeaders, HttpStatus.OK);
	}
	
	/**
	 * 자체편성 부분 즉시적용
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/schedule/applyCacheById", method=RequestMethod.POST)
	public ResponseEntity<String> applyCacheById(
			HttpServletRequest request, 
			HttpServletResponse response,
			@RequestParam(value="schedule_code") final String schedule_code
			) throws Exception {
		
		final CLog cLog = new CLog(applyLogger, request.getRemoteAddr(), request.getRequestURI(), request.getMethod());
		final String loginUser = CookieUtil.getCookieUserID(request);
		final CUDResult result = new CUDResult();
		
		try {
			cLog.startLog("자체편성 부분 즉시적용 : ",  loginUser);
			
			validateDeleteSchedule(schedule_code);
			
			String checkSchedulecodes = schedule_code.replace("|", ",");
			List<ScheduleVO> useScheduleList = service.selectUseSchedule(checkSchedulecodes);
			
			if(CollectionUtils.isEmpty(useScheduleList)){
				throw new MimsCommonException(ResultCode.ApplyFail,"편성되지 않은 코드 입니다");
			}
			
			final String host = SmartUXProperties.getProperty("cache.sync.public.host");
			final String port = SmartUXProperties.getProperty("cache.sync.public.port");
			
			String fileLockPath = SmartUXProperties.getProperty("SUXMAlbumListDao.refreshSUXMAlbumList.fileLock");
			int waitTime = NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.wait"), 5);
			
			
			
			FileProcessLockFactory.start(fileLockPath, waitTime,  new FileProcessFunction("자체편성 부분 즉시적용", ResultCode.ApplyRequestFail) {
				
				@Override
				public void run() throws Exception {
					
					//즉시적용 호출 시 sleep 값 추가 -> DB분리건으로 OGG가 10초 이상 걸리는 경우가 있어 즉시적용 시 반영이 안돼서 추가함
					Thread.sleep(Integer.parseInt(org.apache.commons.lang.StringUtils.defaultIfEmpty(SmartUXProperties.getProperty("apply.cache.call.sleep"), "10000")));
					
					RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
							host, 
							port, 
							SmartUXProperties.getProperty("SUXMAlbumListDao.refreshSUXMAlbumList.url")+ "?schedule_code=" + schedule_code +"&callByScheduler=A",
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.method"), "POST"), 
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"), 
							MediaType.APPLICATION_JSON.toString(),
							NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.timeout"), 60000)).getResponseBody());
					
					result.setFlag(codeMsg.getCode());
					result.setMessage(codeMsg.getMessage());
					cLog.middleLog("자체편성 부분 즉시적용", "API 호출", codeMsg.getCode(), codeMsg.getMessage());
				}
			});
		} catch (MimsCommonException e) {
			cLog.warnLog("자체편성 부분 즉시적용", "실패", e.getFlag(), e.getMessage(), e.getLocalizedMessage());
			result.setFlag(e.getFlag());
			result.setMessage(e.getMessage());
		} finally {
			cLog.endLog("자체편성 부분 즉시적용", loginUser, result.getFlag(), result.getMessage());
		}
		
		String resultMsg = "{\"flag\" : \"" + result.getFlag() + "\", \"message\" : \"" + result.getMessage() + "\"}";
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(resultMsg).toString(), responseHeaders, HttpStatus.OK);
		
	}
	
	
	/**
	 * 자체편성 목록 조회 화면
	 * @param model			Model 객체
	 * @return				자체편성 목록 조회 화면 URL
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/schedule/getScheduleList")
	public String getScheduleList(Model model) throws Exception{
		List<ScheduleVO> result = service.getScheduleList("");
		
		
		model.addAttribute("result", result);
		return "/admin/schedule/getScheduleList";
	}
	
	/**
	 * 순서 변경
	 * 
	 * @param s_panel_id
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/schedule/changeScheOrder", method = RequestMethod.GET)
	public String changeScheOrder(
			HttpServletRequest request, 
			@RequestParam(value="schedule_code") String schedule_code, Model model) throws Exception {
		
		ScheduleVO main = service.viewSchedule(schedule_code);
		List<ScheduleDetailVO> detail = service.getScheduleDetailList(schedule_code, main.getCategory_gb());
		
		
		model.addAttribute("result", detail);
		model.addAttribute("schedule_code", schedule_code);
		return "/admin/schedule/changeScheOrder";
	}
	
	@RequestMapping(value="/admin/schedule/changeOrder", method=RequestMethod.POST)
	public @ResponseBody String changePanelTitleTempOrder(@RequestParam(value="schedule_code") int schedule_code
			, @RequestParam(value="optionArray[]") String [] optionArray
			) throws Exception{
		
		
		String resultcode = "";
		String resultmessage = "";
		try{
			service.changeOrder(optionArray, schedule_code);
			resultcode = SmartUXProperties.getProperty("flag.success");
			resultmessage = SmartUXProperties.getProperty("message.success");
		}catch(Exception e){
			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}
		return "{\"flag\" : \"" + resultcode + "\", \"message\" : \"" + resultmessage + "\"}";
		
	}
	/*@RequestMapping(value = "/admin/schedule/changeOrder", method = RequestMethod.PUT)
	@ResponseBody
	public String changeOrder(
			@RequestBody final MultiValueMap<String, String> data,
			HttpServletRequest request) throws Exception {
		String resultCode;
		String resultMsg;

		try {

			List<String> optionArray = data.get("optionArray[]");
			int schedule_code = 1;
			service.changeOrder(optionArray, schedule_code);

			resultCode = SmartUXProperties.getProperty("flag.success");
			resultMsg = SmartUXProperties.getProperty("message.success");
		} catch (Exception e) {
			ExceptionHandler handler = new ExceptionHandler(e);
			resultCode = handler.getFlag();
			resultMsg = handler.getMessage();
		}

		return "{\"flag\" : \"" + resultCode + "\", \"message\" : \"" + resultMsg + "\"}";
	}*/
	/**
	 * 자체편성 상세 조회 화면
	 * @param schedule_code		상세조회하고자 하는 자체편성의 schedule_code
	 * @param model				Model 객체
	 * @return					자체편성 상세 조회 화면 URL
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/schedule/viewSchedule")
	public String viewSchedule(@RequestParam(value="schedule_code") String schedule_code, Model model) throws Exception{
		
		HTMLCleaner cleaner = new HTMLCleaner();
		schedule_code 	= cleaner.clean(schedule_code);
		
		ScheduleVO main = service.viewSchedule(schedule_code);
		List<ScheduleDetailVO> detail = service.getScheduleDetailList(schedule_code, main.getCategory_gb());
		List<String> album_list = new ArrayList<String>();
		for(int i = 0 ; i < detail.size(); i++) {
			album_list.add(detail.get(i).getCategory_id()+"^"+detail.get(i).getAlbum_id());
		}
		
		String album_list_str = album_list.toString();
		
		model.addAttribute("album_list_str", album_list_str);
		model.addAttribute("main", main);			// 메인 정보
		model.addAttribute("detail", detail);		// 상세 정보
		
		return "/admin/schedule/viewSchedule";
		
	}
	
	/**
	 * 자체편성 등록 화면
	 * @param model			Model 객체
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/schedule/insertSchedule", method=RequestMethod.GET)
	public String insertSchedule(Model model) throws Exception{
		
		
		return "/admin/schedule/insertSchedule";
	}
	
	/**
	 * 자체편성 등록 처리 작업
	 * @param schedule_name		등록하고자 하는 자체편성 이름
	 * @param album_id			등록하고자 하는 자체편성에 속한 앨범 코드값들의 배열
	 * @param category_id		등록하고자 하는 자체편성에 속한 카테고리 코드 값들의 배열
	 * @param login_id			관리자 사이트에 로그인 한 사람의 로그인 id
	 * @return					처리 결과를 기록한 json 문자열
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/schedule/insertSchedule", method=RequestMethod.POST)
	public @ResponseBody String insertSchedule(@RequestParam(value="schedule_name") String schedule_name,
			@RequestParam(value="album_id[]", required=false) String [] album_id,
			@RequestParam(value="category_id[]", required=false) String [] category_id,
			@RequestParam(value="smartUXManager") String login_id,
			@RequestParam(value="category_gb") String category_gb,
			@RequestParam(value="test_id") String test_id,
			@RequestParam(value="variation_id") String variation_id
			) throws Exception{
		
		HTMLCleaner cleaner = new HTMLCleaner();
		schedule_name 	= cleaner.clean(schedule_name);
		
		String resultcode = "";
		String resultmessage = "";
		
		try{
			validateInsertSchedule(schedule_name, album_id, category_id);

			service.insertSchedule(schedule_name, album_id, category_id, login_id, category_gb, test_id, variation_id);
			
			
			resultcode = SmartUXProperties.getProperty("flag.success");
			resultmessage = SmartUXProperties.getProperty("message.success");
			
		}catch(Exception e){
			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}
		
		return "{\"flag\" : \"" + resultcode + "\", \"message\" : \"" + resultmessage + "\"}";
	}
	
	/**
	 * insertSchedule에 대한 validation 함수
	 * @param schedule_name		등록하고자 하는 자체편성 이름
	 * @param album_id			등록하고자 하는 자체편성에 속한 앨범 코드값들의 배열
	 * @param category_id		등록하고자 하는 자체편성에 속한 카테고리 코드 값들의 배열
	 * @throws SmartUXException
	 */
	private void validateInsertSchedule(String schedule_name, String [] album_id, String [] category_id) throws SmartUXException{
		SmartUXException exception = new SmartUXException();
		if(StringUtils.isBlank(schedule_name)){
			
			exception.setFlag("NOT FOUND SCHEDULE_NM");
			exception.setMessage("편성명은 필수로 들어가야 합니다");
			throw exception;
		}
		
		if(schedule_name.length() > 100){
			exception.setFlag("SCHEDULE_NM LENGTH");
			exception.setMessage("편성명은 100자 이내이어야 합니다");
			throw exception;
		}
		
		
		if((album_id == null) || (album_id.length == 0)){
			exception.setFlag("NOT FOUND ALBUM_ID");
			exception.setMessage("앨범이 입력되지 않았습니다");
			throw exception;
		}

		for (int i=0; i<album_id.length; i++) {
			for (int j=0; j<i; j++) {
				if (album_id[i].equals(album_id[j])) {
					exception.setFlag("DUPLICATION ALBUM_ID");
					exception.setMessage("동일한 앨범ID는 편성할 수 없습니다.");
					throw exception;
				}
			}
		}
	
		if((category_id == null) || (category_id.length == 0)){
			exception.setFlag("NOT FOUND CATEGORY_ID");
			exception.setMessage("카테고리가 입력되지 않았습니다");
			throw exception;
		}
			
	}
	
	/**
	 * 자체편성 수정 처리 작업
	 * @param schedule_code		수정하고자 하는 자체편성의 schedule_code
	 * @param schedule_name		수정하고자 하는 자체편성의 자체편성 이름
	 * @param album_id			수정하고자 하는 자체편성에 새로이 편성된 앨범코드 값들의 배열
	 * @param category_id		수정하고자 하는 자체편성에 새로이 편성된 카테고리 코드 값들의 배열
	 * @param login_id			관리자 사이트에 로그인 한 사람의 로그인 id
	 * @return					처리 결과를 기록한 json 문자열
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/schedule/updateSchedule", method=RequestMethod.POST)
	public @ResponseBody String updateSchedule(@RequestParam(value="schedule_code") String schedule_code,
			@RequestParam(value="schedule_name") String schedule_name,
			@RequestParam(value="album_id[]", required=false) String [] album_id,
			@RequestParam(value="category_id[]", required=false) String [] category_id,
			@RequestParam(value="smartUXManager") String login_id,
			@RequestParam(value="category_gb") String category_gb,
			@RequestParam(value="test_id") String test_id,
			@RequestParam(value="variation_id") String variation_id
			) throws Exception{
		
		HTMLCleaner cleaner = new HTMLCleaner();
		schedule_code 	= cleaner.clean(schedule_code);
		schedule_name 	= cleaner.clean(schedule_name);
		
		String resultcode = "";
		String resultmessage = "";
		String album_list_str = "";
		
		try{
			validateUpdateSchedule(schedule_code, schedule_name, album_id, category_id);
			
			service.updateSchedule(schedule_code, schedule_name, album_id, category_id, login_id, category_gb, test_id, variation_id);
			
			resultcode = SmartUXProperties.getProperty("flag.success");
			resultmessage = SmartUXProperties.getProperty("message.success");
			
			ScheduleVO main = service.viewSchedule(schedule_code);
			List<ScheduleDetailVO> detail = service.getScheduleDetailList(schedule_code, main.getCategory_gb());
			List<String> album_list = new ArrayList<String>();
			for(int i = 0 ; i < detail.size(); i++) {
				album_list.add(detail.get(i).getCategory_id()+"^"+detail.get(i).getAlbum_id());
			}
			
			album_list_str = album_list.toString();
			
			
		}catch(Exception e){
			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}
		
		return "{\"flag\" : \"" + resultcode + "\", \"message\" : \"" + resultmessage + "\", \"album_list_str\" : \"" + album_list_str + "\"}";
	}
	
	/**
	 * insertSchedule에 대한 validation 함수
	 * @param schedule_code			수정하고자 하는 자체편성의 schedule_code
	 * @param schedule_name			수정하고자 하는 자체편성의 자체편성 이름
	 * @param album_id				수정하고자 하는 자체편성에 새로이 편성된 앨범코드 값들의 배열
	 * @param category_id			수정하고자 하는 자체편성에 새로이 편성된 카테고리 코드 값들의 배열
	 * @throws SmartUXException
	 */
	private void validateUpdateSchedule(String schedule_code, String schedule_name, String [] album_id, String [] category_id) throws SmartUXException{
		SmartUXException exception = new SmartUXException();
		if(StringUtils.isBlank(schedule_code)){
			
			exception.setFlag("NOT FOUND SCHEDULE_CODE");
			exception.setMessage("편성코드는 필수로 들어가야 합니다");
			throw exception;
		}

		if(StringUtils.isBlank(schedule_name)){
			
			exception.setFlag("NOT FOUND SCHEDULE_NM");
			exception.setMessage("편성명은 필수로 들어가야 합니다");
			throw exception;
		}
		
		if(schedule_name.length() > 100){
			exception.setFlag("SCHEDULE_NM LENGTH");
			exception.setMessage("편성명은 100자 이내이어야 합니다");
			throw exception;
		}
		
		
		if((album_id == null) || (album_id.length == 0)){
			exception.setFlag("NOT FOUND ALBUM_ID");
			exception.setMessage("앨범이 입력되지 않았습니다");
			throw exception;
		}
		
		for (int i=0; i<album_id.length; i++) {
			for (int j=0; j<i; j++) {
				if (album_id[i].equals(album_id[j])) {
					exception.setFlag("DUPLICATION ALBUM_ID");
					exception.setMessage("동일한 앨범ID는 편성할 수 없습니다.");
					throw exception;
				}
			}
		}		
	
		if((category_id == null) || (category_id.length == 0)){
			exception.setFlag("NOT FOUND CATEGORY_ID");
			exception.setMessage("카테고리가 입력되지 않았습니다");
			throw exception;
		}
			
	}
	
	/**
	 * 자체편성 삭제
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/schedule/deleteSchedule", method=RequestMethod.POST)
	public ResponseEntity<String> deleteSchedule(
			HttpServletRequest request, 
			HttpServletResponse response,
			@RequestParam(value="schedule_code") final String schedule_codes
			) throws Exception {
		
		final CLog cLog = new CLog(applyLogger, request.getRemoteAddr(), request.getRequestURI(), request.getMethod());
		final String loginUser = CookieUtil.getCookieUserID(request);
		
		final Map<String, String> map = new HashMap<String, String>();
		map.put("db_flag", "0000");
		map.put("db_message", "미진행");
		map.put("flag", "0000");
		map.put("message", "미진행");
		
		try {
			cLog.startLog("자체편성 삭제 : ",  loginUser);
			validateDeleteSchedule(schedule_codes);
						
			final String host = SmartUXProperties.getProperty("cache.sync.public.host");
			final String port = SmartUXProperties.getProperty("cache.sync.public.port");
			
			String fileLockPath = SmartUXProperties.getProperty("SUXMAlbumListDao.refreshSUXMAlbumList.fileLock");
			int waitTime = NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.wait"), 5);
			
			
			FileProcessLockFactory.start(fileLockPath, waitTime,  new FileProcessFunction("자체편성 DB 삭제", ResultCode.ApplyDBWriteFail) {
				
				@Override
				public void run() throws Exception {
					// 삭제하고자 하는 자체편성이 현재 사용중인지를 확인한다
					String checkSchedulecodes = schedule_codes.replace("|", ",");
					String[] scheduleCodeArr = schedule_codes.split("\\|");
					
					List<ScheduleVO> useScheduleList = service.selectUseSchedule(checkSchedulecodes);
					
					if(CollectionUtils.isEmpty(useScheduleList)){
						service.deleteSchedule(scheduleCodeArr);
					
						map.put("db_flag", ResultCode.Success.getFlag());
						map.put("db_message", ResultCode.Success.getMessage());
						
					}else{	// 삭제하고자 하는 자체편성이 현재 사용중이면
						String msgResult = "";
						for(ScheduleVO item : useScheduleList){
							if("".equals(msgResult)){
								msgResult = item.getSchedule_name();
							}else{
								msgResult += ", " + item.getSchedule_name();
							}
						}
						map.put("db_flag", "USE SCHEDULE CODE");
						map.put("db_message", msgResult);
					}
					cLog.middleLog("자체편성 삭제", "DB작업", map.get("db_flag"), map.get("db_message"));
				}
			},  new FileProcessFunction("자체편성 삭제 API호출 ", ResultCode.ApplyRequestFail) {
				
				@Override
				public void run() throws Exception {
					
					if("0000".equals(map.get("db_flag"))){
						
						//즉시적용 호출 시 sleep 값 추가 -> DB분리건으로 OGG가 10초 이상 걸리는 경우가 있어 즉시적용 시 반영이 안돼서 추가함
						Thread.sleep(Integer.parseInt(org.apache.commons.lang.StringUtils.defaultIfEmpty(SmartUXProperties.getProperty("apply.cache.call.sleep"), "10000")));
						
						RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
								host, 
								port, 
								SmartUXProperties.getProperty("SUXMAlbumListDao.refreshSUXMAlbumList.url")+ "?schedule_code=" + schedule_codes,
								"DELETE", 
								StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"), 
								MediaType.APPLICATION_JSON.toString(),
								NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.timeout"), 60000)).getResponseBody());
						
						map.put("flag", codeMsg.getCode());
						map.put("message",  codeMsg.getMessage());
						cLog.middleLog("자체편성 삭제", "API 호출", codeMsg.getCode(), codeMsg.getMessage());
					}else{
						map.put("flag", map.get("db_flag"));
						map.put("message",  map.get("db_message"));
						cLog.middleLog("자체편성 삭제", "DB삭제 실패", map.get("db_flag"), map.get("db_message"));
					}
				}
			});
		} catch (MimsCommonException e) {
			cLog.warnLog("자체편성 삭제", "실패", e.getFlag(), e.getMessage(), e.getLocalizedMessage());
			map.put("flag", e.getFlag());
			map.put("message", e.getMessage());
		} finally {
			cLog.endLog("자체편성 삭제", loginUser, map.get("flag"), map.get("message"));
		}

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		return new ResponseEntity<String>(om.writeValueAsString(map), responseHeaders, HttpStatus.OK);
	}
	
	
	/**
	 * deleteSchedule에 대한 validation 함수
	 * @param schedule_codes			삭제하고자 하는 자체편성의 schedule_code 값들의 배열
	 * @throws SmartUXException
	 */
	private void validateDeleteSchedule(String schedule_codes) throws SmartUXException{
		SmartUXException exception = new SmartUXException();
		
		if(StringUtils.isBlank(schedule_codes)){
			
			exception.setFlag("NOT FOUND SCHEDULE_CODE");
			exception.setMessage("코드값은 필수로 들어가야 합니다");
			throw exception;
		}
		
	}
	
	// category_id는 항상 VC를 줄것(VC로 줘야 최상위 카테고리가 검색된다)
	// albumElementid는 팝업으로 뜨는 이 화면에서 카테고리를 선택할 경우 앨범들이 검색되는데 그 앨범이 셋팅될 부모창의 html element id를 넘겨준다
	/**
	 * 카테고리 선택 팝업 화면
	 * @param category_id			카테고리 선택 팝업 화면에서 처음에 보이는 카테고리 선택 select box를 구성하기 위한 부모 카테고리 코드(여기엔 항상 VC가 와야 한다)
	 * @param albumElementid		카테고리 선택시 해당 카테고리에 속한 앨범들을 보여줄 부모창의 앨범목록을 보여주는 html 태그의 element id
	 * @param model					Model 객체
	 * @return						카테고리 선택 팝업 화면 URL
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/schedule/getCategoryAlbumList", method=RequestMethod.GET)
	public String getCategoryAlbumList(@RequestParam(value="category_id") String category_id
			, @RequestParam(value="albumElementid") String albumElementid
			, @RequestParam(value="type", defaultValue="I20") String type
			, @RequestParam(value="series_yn", defaultValue="N") String series_yn
			, Model model) throws Exception{
		
		HTMLCleaner cleaner = new HTMLCleaner();
		category_id 	= cleaner.clean(category_id);
		albumElementid 	= cleaner.clean(albumElementid);
		type 	= cleaner.clean(type);
		series_yn 	= cleaner.clean(series_yn);
		
		List<CategoryAlbumVO> result = service.getCategoryAlbumList(category_id, GlobalCom.isNull(type, "I20"), series_yn);
		
		List<CategoryAlbumVO> categoryResult = new ArrayList<CategoryAlbumVO>();
		List<CategoryAlbumVO> albumResult = new ArrayList<CategoryAlbumVO>();
		
		// 화면을 띄울때 최상위 카테고리를 조사하는데 여기에 카테고리와 앨범이 섞여 있을수도 있어서 이를 분리하는 작업을 진행한다
		for(CategoryAlbumVO item : result){
			if("".equals(item.getAlbum_id())){			// album_id가 ""일 경우는 카테고리임을 의미한다
				categoryResult.add(item);
			}else{										// album_id가 ""가 아닐 경우는 앨범이다
				albumResult.add(item);
			}
		}
		
		model.addAttribute("categoryResult", categoryResult);
		model.addAttribute("albumResult", albumResult);
		model.addAttribute("albumElementid", albumElementid);
		model.addAttribute("type", type);
		model.addAttribute("series_yn", series_yn);
		return "/admin/schedule/getCategoryAlbumList";
	}
	
	/**
	 * 카테고리 하위의 앨범들을 조회한다
	 * @param category_id	카테고리 id
	 * @param album_id	앨범id
	 * @return				하위 카테고리와 앨범들의 목록 데이터가 있는 json 문자열
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/schedule/getAlbumListByCategoryIdAlbumId", method=RequestMethod.GET)
	public  String getAlbumListByCategoryIdAlbumId(
			@RequestParam(value="category_id" ) String category_id,
			@RequestParam(value="album_id", required=false) String album_id,
			@RequestParam(value="type", required=false, defaultValue="I20") String type, Model model) throws Exception{
		
		HTMLCleaner cleaner = new HTMLCleaner();
		album_id 	= cleaner.clean(album_id);
		category_id 	= cleaner.clean(category_id);
		type 	= cleaner.clean(type);
		if(category_id == null || "".equals(category_id))
			return "";
		
		List<CategoryAlbumVO> result = service.getAlbumListByCategoryIdAlbumId(category_id,album_id,GlobalCom.isNull(type, "I20"));
		
		
		model.addAttribute("imgDir", SmartUXProperties.getProperty("panel.imageserver.url"));
		model.addAttribute("category_id", category_id);
		model.addAttribute("album_id", album_id);
		model.addAttribute("resultList", result);
		
		
		return "/admin/schedule/getContentsImgPop";
	}
	
	
	
	/**
	 * 카테고리 선택 팝업화면에서 카테고리 선택시 선택된 카테고리의 하위 카테고리와 앨범들을 조회한다
	 * @param category_id	카테고리 id
	 * @return				하위 카테고리와 앨범들의 목록 데이터가 있는 json 문자열
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/schedule/getCategoryAlbumList", method=RequestMethod.POST)
	public ResponseEntity<String> getCategoryAlbumList(@RequestParam(value="category_id") String category_id
			, @RequestParam(value="type", defaultValue="I20") String type
			, @RequestParam(value="series_yn", defaultValue="N") String series_yn) throws Exception{
		
		HTMLCleaner cleaner = new HTMLCleaner();
		category_id 	= cleaner.clean(category_id);
		type 	= cleaner.clean(type);
		series_yn 	= cleaner.clean(series_yn);
		
		List<CategoryAlbumVO> result = service.getCategoryAlbumList(category_id, GlobalCom.isNull(type, "I20"), series_yn);
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(result).toString(), responseHeaders, HttpStatus.CREATED);
	}
	
	
	
	/**
	 * 메인 패널 이미지 업로드(가로 및 세로)
	 * @param request
	 * @param wFile
	 * @param hFile
	 * @param album_id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/schedule/insertImg", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> insertImg(
			HttpServletRequest request, 
			 @RequestParam(value="w_file", required=false) MultipartFile wFile,
			 @RequestParam(value="h_file", required=false) MultipartFile hFile,
			 @RequestParam(value="w_file_name", required=false) String w_fileName,
			 @RequestParam(value="h_file_name", required=false) String h_fileName,
			 @RequestParam(value="album_id", required=false) String albumId,
			 @RequestParam(value="delFlag", required=false) String delFlag
			 ) throws Exception {
		
		Map<String, String> resultMap = new HashMap<String, String>();
		
		String dirImg = SmartUXProperties.getProperty("panel.imgupload.dir");
		
		if(StringUtils.isNotBlank(dirImg)){
			File dirChk = new File(dirImg);
			if(!dirChk.exists()) {
				dirChk.mkdirs();
			}
			
			try{
				//가로이미지 확장자
				if(wFile.getSize() != 0L){
					String wFileName = wFile.getOriginalFilename();
					
					if(StringUtils.isNotBlank(wFileName)){
						String w_ext = wFileName.substring(wFileName.lastIndexOf(".") + 1, wFileName.length());
						
						if(StringUtils.isNotBlank(albumId)){
							w_fileName = albumId + "W" + GlobalCom.getTodayFormat4_24()+"."+w_ext;
							
							File w_imgFile= new File(dirImg+"/"+w_fileName);
							wFile.transferTo(w_imgFile);
							
							resultMap.put("w_resultCode", "0000");
							resultMap.put("w_fileName", w_fileName);
						}
					}
				}else{
					if("delete".equals(w_fileName)){
						resultMap.put("w_resultCode", "0000");
						w_fileName=null;
					}
				}
				
			}catch(Exception e){
				ExceptionHandler handler = new ExceptionHandler(e);
				
				resultMap.put("w_resultCode",handler.getFlag());
				resultMap.put("msg", handler.getMessage());
			}
			
			try{
				//세로이미지 확장자
				if(hFile.getSize() != 0L){
					String hFileName = hFile.getOriginalFilename();
					
					if(StringUtils.isNotBlank(hFileName)){
						String h_ext = hFileName.substring(hFileName.lastIndexOf(".") + 1, hFileName.length());
						
						if(StringUtils.isNotBlank(albumId)){
							h_fileName = albumId + "H" + GlobalCom.getTodayFormat4_24()+"."+h_ext;
							
							File h_imgFile = new File(dirImg+"/"+h_fileName);
							hFile.transferTo(h_imgFile);
							
							resultMap.put("h_resultCode", "0000");
							resultMap.put("h_fileName", h_fileName);
						}
					}
				}else{
					if("delete".equals(h_fileName)){
						resultMap.put("h_resultCode", "0000");
						h_fileName=null;
					}
				}
				
				
				
			}catch(Exception e){
				ExceptionHandler handler = new ExceptionHandler(e);
				
				resultMap.put("h_resultCode",handler.getFlag());
				resultMap.put("msg", handler.getMessage());
			}
			
			if("0000".equals(resultMap.get("w_resultCode")) || "0000".equals(resultMap.get("h_resultCode"))){
				service.mergeContentsImg(albumId,w_fileName,h_fileName,CookieUtil.getCookieUserID(request) );
				
				resultMap.put("code", "0000");
				resultMap.put("msg","이미지가 성공적으로 적용 되었습니다.");
			}
		}
		
		resultMap.put("imgDir", SmartUXProperties.getProperty("panel.imageserver.url"));
		ObjectMapper om = new ObjectMapper();
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");  
		return new ResponseEntity<String>(om.writeValueAsString(resultMap), responseHeaders, HttpStatus.OK);
	}
	
	/**
	 * AB테스트 목록 조회 팝업
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/admin/schedule/getABTestList", method = RequestMethod.GET)
	public String getABTestList(
			@RequestParam(value = "findName", required = false, defaultValue = "") String findName,
			@RequestParam(value = "findValue", required = false, defaultValue = "") String findValue,
			@RequestParam(value = "pageNum", required = false, defaultValue = "1") String pageNum,
			@RequestParam(value = "offset", required = false, defaultValue = "-1") String offset,
			Model model) throws Exception {

		ABTestSearchVO searchVo = new ABTestSearchVO();
		
		//페이지
		searchVo.setPageSize(GlobalCom.isNumber(searchVo.getPageSize(), 10));
		searchVo.setBlockSize(GlobalCom.isNumber(searchVo.getBlockSize(), 10));
		searchVo.setPageNum(GlobalCom.isNumber(Integer.parseInt(GlobalCom.isNull(pageNum, "1")), 1));
		
		searchVo.setFindName(findName);
		searchVo.setFindValue(findValue);
		searchVo.setOffset(offset);
		
		List<ABTestVO> abmsList =null;
		int totalCount = 0;
		Map<String, Object> abmsMap = abtestService.getABMSCall(searchVo);
		
		if(!CollectionUtils.isEmpty(abmsMap)){
			if("0".equals(abmsMap.get("resCode"))){
				
				abmsList = (List<ABTestVO>) abmsMap.get("abmsList");
				if(!CollectionUtils.isEmpty(abmsList)){
					//AMBS 호출 결과와 DB와 매핑
					abmsList = abtestService.getABTestList(abmsList);	
				}
				totalCount= Integer.parseInt((String) abmsMap.get("totalCount"));
			}
		}
		
		searchVo.setPageCount(totalCount);
		model.addAttribute("list", abmsList);
		model.addAttribute("searchVo", searchVo);

		return "/admin/schedule/scheduleAbtestListPop";
	}
	
}
