package com.dmi.smartux.admin.mainpanel.controller;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Row;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.dmi.commons.consts.ResultCode;
import com.dmi.commons.exception.MimsCommonException;
import com.dmi.commons.factory.FileProcessFunction;
import com.dmi.commons.factory.FileProcessLockFactory;
import com.dmi.commons.logger.CLog;
import com.dmi.commons.model.RestfulCodeMsg;
import com.dmi.commons.utility.ExcelWorkBookFactory;
import com.dmi.commons.utility.HttpClientUtils;
import com.dmi.commons.utility.RestfulUtils;
import com.dmi.smartux.admin.abtest.service.ABTestService;
import com.dmi.smartux.admin.abtest.vo.ABTestSearchVO;
import com.dmi.smartux.admin.abtest.vo.ABTestVO;
import com.dmi.smartux.admin.code.service.CodeService;
import com.dmi.smartux.admin.code.vo.CodeItemVO;
import com.dmi.smartux.admin.commonMng.service.CommonModuleService;
import com.dmi.smartux.admin.commonMng.vo.FlatRateVO;
import com.dmi.smartux.admin.imcs.service.ImcsService;
import com.dmi.smartux.admin.mainpanel.service.PanelViewService;
import com.dmi.smartux.admin.mainpanel.vo.BubbleInsert;
import com.dmi.smartux.admin.mainpanel.vo.BubbleList;
import com.dmi.smartux.admin.mainpanel.vo.BubbleSearch;
import com.dmi.smartux.admin.mainpanel.vo.CategoryVO;
import com.dmi.smartux.admin.mainpanel.vo.FrameVO;
import com.dmi.smartux.admin.mainpanel.vo.LinkInfoVO;
import com.dmi.smartux.admin.mainpanel.vo.LocationCodeVO;
import com.dmi.smartux.admin.mainpanel.vo.PanelSearchVo;
import com.dmi.smartux.admin.mainpanel.vo.PanelVO;
import com.dmi.smartux.admin.mainpanel.vo.PreviewVO;
import com.dmi.smartux.admin.mainpanel.vo.ViewVO;
import com.dmi.smartux.admin.rank.service.EcrmRankService;
import com.dmi.smartux.admin.rank.vo.EcrmRankVO;
import com.dmi.smartux.admin.schedule.service.ScheduleService;
import com.dmi.smartux.admin.schedule.vo.ScheduleVO;
import com.dmi.smartux.common.exception.ExceptionHandler;
import com.dmi.smartux.common.exception.SmartUXException;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.CookieUtil;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.util.HTMLCleaner;
import com.dmi.smartux.common.vo.CUDResult;
import com.dmi.smartux.common.vo.ResultVO;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import net.sf.json.JSONSerializer;

@Controller
public class PanelViewController {

private final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	PanelViewService service;
	@Autowired
	CodeService codeservice;
	@Autowired
	ScheduleService scheduleservice;
	@Autowired
	EcrmRankService ecrmrankservice;
	@Autowired
	ImcsService imcsService;
	@Autowired
	ABTestService abservice;
	@Autowired
	CommonModuleService commService;
	
	private final Log applyLogger = LogFactory.getLog("adminApplyCacheHistroy");
	private ObjectMapper om = new ObjectMapper();
	
	/**
	 * 지면 즉시적용
	 * @param request
	 * @param response
	 * @param panel_id
	 * @param login_id
	 * @param callByScheduler
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/mainpanel/applyPanelTitle", method=RequestMethod.POST)
	public ResponseEntity<String> applyPanelTitle(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value="panel_id") final String panel_id,
			@RequestParam(value="callByScheduler", defaultValue="Y") final  String callByScheduler,
			@RequestParam(value="type", defaultValue="") final  String type
			) throws Exception{
		
		final CLog cLog = new CLog(applyLogger, request.getRemoteAddr(), request.getRequestURI(), request.getMethod());
		final String loginUser = CookieUtil.getCookieUserID(request);

		final Map<String, String> map = new HashMap<String, String>();
		int paneltitle_change_cnt = 0;
		
		map.put("versionFlag", "0000");
		map.put("versionMessage", "미진행");
		
		map.put("flag", "0000");
		map.put("message", "미진행");
		
		map.put("repFlag", "0000");
		map.put("repMessage", "미진행");
		
		map.put("categoryFlag", "0000");
		map.put("categoryMessage", "미진행");
		
		map.put("smartFlag", "0000");
		map.put("smartMessage", "미진행");

		try {
			cLog.startLog("지면 즉시적용", loginUser, callByScheduler, type);

			final String host = SmartUXProperties.getProperty("cache.sync.public.host");
			final String port = SmartUXProperties.getProperty("cache.sync.public.port");
			
			String fileLockPath = SmartUXProperties.getProperty("MainPanelDao.refreshMainPanelInterlockingInfo.fileLock");
			int waitTime = NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.wait"), 5);
			
			boolean isForceUpdate = request.getParameter("isForceUpdate") == null ? false : true;
			validateApplyPanelTitle(panel_id);						// 입력받은 파라미터 값들에 대한 validation 작업 진행
			// 하위 지면이 없으나 카테고리 코드가 등록이 되지 않은 지면을 조회한다
			List<String> titleList = service.mustCategorySettingList(panel_id);
			
			if(!CollectionUtils.isEmpty(titleList)){	// 하위 지면이 없는 모든 지면에 카테고리 코드가 등록되어 있으면 바로 확인 작업을 들어간다
				String resultmessage = "";
				
				for(String item : titleList){
					if("".equals(resultmessage)){
						resultmessage = item; 
					}else{
						resultmessage +=  ", " + item; 
					}
				}
				
				map.put("flag", "EXIST NOT_CATEGORY_TITLE");
				map.put("message", resultmessage);
			}else{
				paneltitle_change_cnt = isForceUpdate ? 1 : service.getPanelTitleChangeCount(panel_id);
				if( paneltitle_change_cnt != 0){
					
					FileProcessLockFactory.start(fileLockPath, waitTime, new FileProcessFunction("지면 DB", ResultCode.ApplyDBWriteFail){
						@Override
						public void run() throws Exception {
							service.insertPanelTitle(panel_id, loginUser);
							cLog.middleLog("지면 즉시적용", "통합DB", "성공");
						}
					}, new FileProcessFunction("패널 버전 즉시적용", ResultCode.ApplyRequestFail) {
						@Override
						public void run() throws Exception {
							
							//즉시적용 호출 시 sleep 값 추가 -> DB분리건으로 OGG가 10초 이상 걸리는 경우가 있어 즉시적용 시 반영이 안돼서 추가함
							Thread.sleep(Integer.parseInt(org.apache.commons.lang.StringUtils.defaultIfEmpty(SmartUXProperties.getProperty("apply.cache.call.sleep"), "10000")));
							
							RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
									host, 
									port, 
									SmartUXProperties.getProperty("MainPanelDao.refreshMainPanelVersionInfo.url")+"?callByScheduler="+callByScheduler,
									org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.method"), "POST"), 
									org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"), 
									MediaType.APPLICATION_JSON.toString(),
									NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.timeout"), 60000)).getResponseBody());
							
							map.put("versionFlag", codeMsg.getCode());
							map.put("versionMessage", codeMsg.getMessage());
							cLog.middleLog("지면 즉시적용", "패널 버전 즉시적용 API 호출", codeMsg.getCode(), codeMsg.getMessage());
						}
					}, new FileProcessFunction("지면 즉시적용", ResultCode.ApplyRequestFail) {
						@Override
						public void run() throws Exception {
							String p_panel_id = "P".equals(org.apache.commons.lang.StringUtils.defaultIfEmpty(type, ""))? panel_id:"";
							RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
									host, 
									port, 
									SmartUXProperties.getProperty("MainPanelDao.refreshMainPanelInfo.url")+"?callByScheduler="+callByScheduler +"&panel_id=" + p_panel_id ,
									org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.method"), "POST"), 
									org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"), 
									MediaType.APPLICATION_JSON.toString(),
									NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.timeout"), 60000)).getResponseBody());
							
							map.put("flag", codeMsg.getCode());
							map.put("message", codeMsg.getMessage());
							cLog.middleLog("지면 즉시적용", "API 호출 성공", type, p_panel_id, codeMsg);
						}
					}, new FileProcessFunction("지면별 대표컨텐츠 상세정보 즉시적용", ResultCode.ApplyRequestFail) {
						@Override
						public void run() throws Exception {
							String p_panel_id = "P".equals(org.apache.commons.lang.StringUtils.defaultIfEmpty(type, ""))? panel_id:"";
							RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
									host, 
									port, 
									SmartUXProperties.getProperty("MainPanelDao.refreshRepsContentList.url")+"?callByScheduler="+callByScheduler + "&panel_id=" + p_panel_id,
									org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.method"), "POST"), 
									org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"), 
									MediaType.APPLICATION_JSON.toString(),
									NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.timeout"), 60000)).getResponseBody());
							
							map.put("repFlag", codeMsg.getCode());
							map.put("repMessage", codeMsg.getMessage());
							cLog.middleLog("지면 즉시적용", "지면별 대표컨텐츠 상세정보 API 호출 성공", type, p_panel_id, codeMsg);
						}
					}, new FileProcessFunction("카테고리 맵 즉시적용", ResultCode.ApplyRequestFail) {
						@Override
						public void run() throws Exception {
							String p_panel_id = "P".equals(org.apache.commons.lang.StringUtils.defaultIfEmpty(type, ""))? panel_id:"";
							RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
									host, 
									port, 
									SmartUXProperties.getProperty("MainPanelDao.refreshI20AlbumList.url")+"?callByScheduler="+callByScheduler + "&panel_id=" + p_panel_id,
									org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.method"), "POST"), 
									org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"), 
									MediaType.APPLICATION_JSON.toString(),
									NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.timeout"), 60000)).getResponseBody());
							
							map.put("categoryFlag", codeMsg.getCode());
							map.put("categoryMessage", codeMsg.getMessage());
							cLog.middleLog("지면 즉시적용", "카테고리 맵 즉시적용 API 호출 성공", type, p_panel_id, codeMsg);
						}
					}, new FileProcessFunction("스마트스타트 즉시적용", ResultCode.ApplyRequestFail) {
						@Override
						public void run() throws Exception {
							RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
									host, 
									port, 
									SmartUXProperties.getProperty("SmartStartItemListDao.refreshSmartStartItemList.url")+"?callByScheduler="+callByScheduler,
									org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.method"), "POST"), 
									org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"), 
									MediaType.APPLICATION_JSON.toString(),
									NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.timeout"), 60000)).getResponseBody());
							
							map.put("smartFlag", codeMsg.getCode());
							map.put("smartMessage", codeMsg.getMessage());
							cLog.middleLog("지면 즉시적용", "스마트스타트 즉시적용 API 호출 성공", codeMsg);
						}
					});
				}else{
					map.put("flag", SmartUXProperties.getProperty("flag.notfound"));
					map.put("message", SmartUXProperties.getProperty("message.notfound"));
				}
				
			}
		
		} catch (MimsCommonException e) {
			cLog.warnLog("지면 즉시적용", "실패", e.getFlag(), e.getMessage(), e.getLocalizedMessage());
			map.put("flag", e.getFlag());
			map.put("message", e.getMessage());
		} finally {
			cLog.endLog("지면 즉시적용", loginUser, type, map.get("flag"), map.get("message"));
		}
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		return new ResponseEntity<String>(om.writeValueAsString(map), responseHeaders, HttpStatus.OK);
	}

	/**
	 * CJ 지면 즉시적용
	 * @param request
	 * @param response
	 * @param panel_id
	 * @param login_id
	 * @param callByScheduler
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/mainpanel/applyCjPanelTitle", method=RequestMethod.POST)
	public ResponseEntity<String> applyCjPanelTitle(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value="panel_id") final String panel_id,
			@RequestParam(value="callByScheduler", defaultValue="Y") final  String callByScheduler
			) throws Exception{
		
		final CLog cLog = new CLog(applyLogger, request.getRemoteAddr(), request.getRequestURI(), request.getMethod());
		final String loginUser = CookieUtil.getCookieUserID(request);

		final Map<String, String> map = new HashMap<String, String>();
		int paneltitle_change_cnt = 0;
		
		map.put("versionFlag", "0000");
		map.put("versionMessage", "미진행");
		
		map.put("flag", "0000");
		map.put("message", "미진행");
		
		map.put("repFlag", "0000");
		map.put("repMessage", "미진행");
		
		map.put("categoryFlag", "0000");
		map.put("categoryMessage", "미진행");
		
		map.put("smartFlag", "0000");
		map.put("smartMessage", "미진행");

		try {
			cLog.startLog("CJ 지면 즉시적용", loginUser, callByScheduler);

			final String host = SmartUXProperties.getProperty("cache.sync.public.cj.host");
			final String port = SmartUXProperties.getProperty("cache.sync.public.cj.port");
			
			String fileLockPath = SmartUXProperties.getProperty("MainPanelDao.refreshMainPanelInterlockingInfo.fileLock");
			int waitTime = NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.wait"), 5);
			
			boolean isForceUpdate = request.getParameter("isForceUpdate") == null ? false : true;
			validateApplyPanelTitle(panel_id);						// 입력받은 파라미터 값들에 대한 validation 작업 진행
			// 하위 지면이 없으나 카테고리 코드가 등록이 되지 않은 지면을 조회한다
			List<String> titleList = service.mustCategorySettingList(panel_id);
			
			if(!CollectionUtils.isEmpty(titleList)){	// 하위 지면이 없는 모든 지면에 카테고리 코드가 등록되어 있으면 바로 확인 작업을 들어간다
				String resultmessage = "";
				
				for(String item : titleList){
					if("".equals(resultmessage)){
						resultmessage = item; 
					}else{
						resultmessage +=  ", " + item; 
					}
				}
				
				map.put("flag", "EXIST NOT_CATEGORY_TITLE");
				map.put("message", resultmessage);
			}else{
				paneltitle_change_cnt = isForceUpdate ? 1 : service.getPanelTitleChangeCount(panel_id);
				if( paneltitle_change_cnt != 0){
					
					FileProcessLockFactory.start(fileLockPath, waitTime, new FileProcessFunction("지면 DB", ResultCode.ApplyDBWriteFail){
						@Override
						public void run() throws Exception {
							service.insertPanelTitle(panel_id, loginUser);
							cLog.middleLog("CJ 지면 즉시적용", "통합DB", "성공");
						}
					}, new FileProcessFunction("CJ 패널 버전 즉시적용", ResultCode.ApplyRequestFail) {
						@Override
						public void run() throws Exception {
							
							//즉시적용 호출 시 sleep 값 추가 -> DB분리건으로 OGG가 10초 이상 걸리는 경우가 있어 즉시적용 시 반영이 안돼서 추가함
							Thread.sleep(Integer.parseInt(org.apache.commons.lang.StringUtils.defaultIfEmpty(SmartUXProperties.getProperty("apply.cache.call.sleep"), "10000")));
							
							RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
									host, 
									port, 
									SmartUXProperties.getProperty("MainPanelDao.refreshMainPanelVersionInfo.cj.url")+"?callByScheduler="+callByScheduler,
									org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.method"), "POST"), 
									org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"), 
									MediaType.APPLICATION_JSON.toString(),
									NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.timeout"), 60000)).getResponseBody());
							
							map.put("versionFlag", codeMsg.getCode());
							map.put("versionMessage", codeMsg.getMessage());
							cLog.middleLog("CJ 지면 즉시적용", "CJ 패널 버전 즉시적용 API 호출", codeMsg.getCode(), codeMsg.getMessage());
						}
					}, new FileProcessFunction("CJ 지면 즉시적용", ResultCode.ApplyRequestFail) {
						@Override
						public void run() throws Exception {
							RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
									host, 
									port, 
									SmartUXProperties.getProperty("MainPanelDao.refreshMainPanelInfo.cj.url")+"?callByScheduler="+callByScheduler,
									org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.method"), "POST"), 
									org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"), 
									MediaType.APPLICATION_JSON.toString(),
									NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.timeout"), 60000)).getResponseBody());
							
							map.put("flag", codeMsg.getCode());
							map.put("message", codeMsg.getMessage());
							cLog.middleLog("CJ 지면 즉시적용", "API 호출 성공", codeMsg);
						}
					}, new FileProcessFunction("CJ 지면별 대표컨텐츠 상세정보 즉시적용", ResultCode.ApplyRequestFail) {
						@Override
						public void run() throws Exception {
							RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
									host, 
									port, 
									SmartUXProperties.getProperty("MainPanelDao.refreshRepsContentList.cj.url")+"?callByScheduler="+callByScheduler,
									org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.method"), "POST"), 
									org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"), 
									MediaType.APPLICATION_JSON.toString(),
									NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.timeout"), 60000)).getResponseBody());
							
							map.put("repFlag", codeMsg.getCode());
							map.put("repMessage", codeMsg.getMessage());
							cLog.middleLog("CJ 지면 즉시적용", "CJ 지면별 대표컨텐츠 상세정보 API 호출 성공", codeMsg);
						}
					});
				}else{
					map.put("flag", SmartUXProperties.getProperty("flag.notfound"));
					map.put("message", SmartUXProperties.getProperty("message.notfound"));
				}
				
			}
		
		} catch (MimsCommonException e) {
			cLog.warnLog("CJ 지면 즉시적용", "실패", e.getFlag(), e.getMessage(), e.getLocalizedMessage());
			map.put("flag", e.getFlag());
			map.put("message", e.getMessage());
		} finally {
			cLog.endLog("CJ 지면 즉시적용", loginUser, map.get("flag"), map.get("message"));
		}
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		return new ResponseEntity<String>(om.writeValueAsString(map), responseHeaders, HttpStatus.OK);
	}
	
	/**
	 * 패널 스케줄
	 * @param request
	 * @param response
	 * @param panel_id
	 * @param login_id
	 * @param callByScheduler
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/mainpanel/applyPanelTitleSchedule", method=RequestMethod.POST)
	public ResponseEntity<String> applyPanelTitleSchedule(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value="callByScheduler", defaultValue="Y") final  String callByScheduler
			) throws Exception{
		
		final CLog cLog = new CLog(applyLogger, request.getRemoteAddr(), request.getRequestURI(), request.getMethod());
		final Map<String, String> map = new HashMap<String, String>();
		
		map.put("versionFlag", "0000");
		map.put("versionMessage", "미진행");
		
		map.put("flag", "0000");
		map.put("message", "미진행");
		
		map.put("repFlag", "0000");
		map.put("repMessage", "미진행");
		
		map.put("categoryFlag", "0000");
		map.put("categoryMessage", "미진행");
		
		map.put("smartFlag", "0000");
		map.put("smartMessage", "미진행");

		try {
			cLog.startLog("지면 즉시적용", "batch", callByScheduler);

			final String host = SmartUXProperties.getProperty("cache.sync.public.host");
			final String port = SmartUXProperties.getProperty("cache.sync.public.port");
			
			String fileLockPath = SmartUXProperties.getProperty("MainPanelDao.refreshMainPanelInterlockingInfo.fileLock");
			int waitTime = NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.wait"), 5);
			
			FileProcessLockFactory.start(fileLockPath, waitTime, new FileProcessFunction("패널 버전 즉시적용", ResultCode.ApplyRequestFail) {
				@Override
				public void run() throws Exception {
					RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
							host, 
							port, 
							SmartUXProperties.getProperty("MainPanelDao.refreshMainPanelVersionInfo.url")+"?callByScheduler="+callByScheduler,
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.method"), "POST"), 
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"), 
							MediaType.APPLICATION_JSON.toString(),
							NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.timeout"), 60000)).getResponseBody());
					
					map.put("versionFlag", codeMsg.getCode());
					map.put("versionMessage", codeMsg.getMessage());
					cLog.middleLog("지면 즉시적용", "패널 버전 즉시적용 API 호출", codeMsg.getCode(), codeMsg.getMessage());
				}
			}, new FileProcessFunction("지면 즉시적용", ResultCode.ApplyRequestFail) {
				@Override
				public void run() throws Exception {
					RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
							host, 
							port, 
							SmartUXProperties.getProperty("MainPanelDao.refreshMainPanelInfo.url")+"?callByScheduler="+callByScheduler,
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.method"), "POST"), 
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"), 
							MediaType.APPLICATION_JSON.toString(),
							NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.timeout"), 60000)).getResponseBody());
					
					map.put("flag", codeMsg.getCode());
					map.put("message", codeMsg.getMessage());
					cLog.middleLog("지면 즉시적용", "API 호출 성공", codeMsg);
				}
			}, new FileProcessFunction("지면별 대표컨텐츠 상세정보 즉시적용", ResultCode.ApplyRequestFail) {
				@Override
				public void run() throws Exception {
					RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
							host, 
							port, 
							SmartUXProperties.getProperty("MainPanelDao.refreshRepsContentList.url")+"?callByScheduler="+callByScheduler,
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.method"), "POST"), 
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"), 
							MediaType.APPLICATION_JSON.toString(),
							NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.timeout"), 60000)).getResponseBody());
					
					map.put("repFlag", codeMsg.getCode());
					map.put("repMessage", codeMsg.getMessage());
					cLog.middleLog("지면 즉시적용", "지면별 대표컨텐츠 상세정보 API 호출 성공", codeMsg);
				}
			}, new FileProcessFunction("카테고리 맵 즉시적용", ResultCode.ApplyRequestFail) {
				@Override
				public void run() throws Exception {
					RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
							host, 
							port, 
							SmartUXProperties.getProperty("MainPanelDao.refreshI20AlbumList.url")+"?callByScheduler="+callByScheduler,
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.method"), "POST"), 
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"), 
							MediaType.APPLICATION_JSON.toString(),
							NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.timeout"), 60000)).getResponseBody());
					
					map.put("categoryFlag", codeMsg.getCode());
					map.put("categoryMessage", codeMsg.getMessage());
					cLog.middleLog("지면 즉시적용", "카테고리 맵 즉시적용 API 호출 성공", codeMsg);
				}
			}, new FileProcessFunction("스마트스타트 즉시적용", ResultCode.ApplyRequestFail) {
				@Override
				public void run() throws Exception {
					RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
							host, 
							port, 
							SmartUXProperties.getProperty("SmartStartItemListDao.refreshSmartStartItemList.url")+"?callByScheduler="+callByScheduler,
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.method"), "POST"), 
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"), 
							MediaType.APPLICATION_JSON.toString(),
							NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.timeout"), 60000)).getResponseBody());
					
					map.put("smartFlag", codeMsg.getCode());
					map.put("smartMessage", codeMsg.getMessage());
					cLog.middleLog("지면 즉시적용", "스마트스타트 즉시적용 API 호출 성공", codeMsg);
				}
			});
				
		
		} catch (MimsCommonException e) {
			cLog.warnLog("지면 즉시적용", "실패", e.getFlag(), e.getMessage(), e.getLocalizedMessage());
			map.put("flag", e.getFlag());
			map.put("message", e.getMessage());
		} finally {
			cLog.endLog("지면 즉시적용", "batch", map.get("flag"), map.get("message"));
		}
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		return new ResponseEntity<String>(om.writeValueAsString(map), responseHeaders, HttpStatus.OK);
	}
	
	/**
	 * CJ 패널 스케줄
	 * @param request
	 * @param response
	 * @param panel_id
	 * @param login_id
	 * @param callByScheduler
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/mainpanel/applyCjPanelTitleSchedule", method=RequestMethod.POST)
	public ResponseEntity<String> applyCjPanelTitleSchedule(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value="callByScheduler", defaultValue="Y") final  String callByScheduler
			) throws Exception{
		
		final CLog cLog = new CLog(applyLogger, request.getRemoteAddr(), request.getRequestURI(), request.getMethod());
		final Map<String, String> map = new HashMap<String, String>();
		
		map.put("versionFlag", "0000");
		map.put("versionMessage", "미진행");
		
		map.put("flag", "0000");
		map.put("message", "미진행");
		
		map.put("repFlag", "0000");
		map.put("repMessage", "미진행");
		
		map.put("categoryFlag", "0000");
		map.put("categoryMessage", "미진행");
		
		map.put("smartFlag", "0000");
		map.put("smartMessage", "미진행");

		try {
			cLog.startLog("CJ 지면 즉시적용", "batch", callByScheduler);

			final String host = SmartUXProperties.getProperty("cache.sync.public.cj.host");
			final String port = SmartUXProperties.getProperty("cache.sync.public.cj.port");
			
			String fileLockPath = SmartUXProperties.getProperty("MainPanelDao.refreshMainPanelInterlockingInfo.fileLock");
			int waitTime = NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.wait"), 5);
			
			FileProcessLockFactory.start(fileLockPath, waitTime, new FileProcessFunction("패널 버전 즉시적용", ResultCode.ApplyRequestFail) {
				@Override
				public void run() throws Exception {
					RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
							host, 
							port, 
							SmartUXProperties.getProperty("MainPanelDao.refreshMainPanelVersionInfo.cj.url")+"?callByScheduler="+callByScheduler,
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.method"), "POST"), 
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"), 
							MediaType.APPLICATION_JSON.toString(),
							NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.timeout"), 60000)).getResponseBody());
					
					map.put("versionFlag", codeMsg.getCode());
					map.put("versionMessage", codeMsg.getMessage());
					cLog.middleLog("CJ 지면 즉시적용", "CJ 패널 버전 즉시적용 API 호출", codeMsg.getCode(), codeMsg.getMessage());
				}
			}, new FileProcessFunction("CJ 지면 즉시적용", ResultCode.ApplyRequestFail) {
				@Override
				public void run() throws Exception {
					RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
							host, 
							port, 
							SmartUXProperties.getProperty("MainPanelDao.refreshMainPanelInfo.cj.url")+"?callByScheduler="+callByScheduler,
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.method"), "POST"), 
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"), 
							MediaType.APPLICATION_JSON.toString(),
							NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.timeout"), 60000)).getResponseBody());
					
					map.put("flag", codeMsg.getCode());
					map.put("message", codeMsg.getMessage());
					cLog.middleLog("CJ 지면 즉시적용", "API 호출 성공", codeMsg);
				}
			}, new FileProcessFunction("CJ 지면별 대표컨텐츠 상세정보 즉시적용", ResultCode.ApplyRequestFail) {
				@Override
				public void run() throws Exception {
					RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
							host, 
							port, 
							SmartUXProperties.getProperty("MainPanelDao.refreshRepsContentList.cj.url")+"?callByScheduler="+callByScheduler,
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.method"), "POST"), 
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"), 
							MediaType.APPLICATION_JSON.toString(),
							NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.timeout"), 60000)).getResponseBody());
					
					map.put("repFlag", codeMsg.getCode());
					map.put("repMessage", codeMsg.getMessage());
					cLog.middleLog("CJ 지면 즉시적용", "CJ 지면별 대표컨텐츠 상세정보 API 호출 성공", codeMsg);
				}
			});
				
		
		} catch (MimsCommonException e) {
			cLog.warnLog("CJ 지면 즉시적용", "실패", e.getFlag(), e.getMessage(), e.getLocalizedMessage());
			map.put("flag", e.getFlag());
			map.put("message", e.getMessage());
		} finally {
			cLog.endLog("CJ 지면 즉시적용", "batch", map.get("flag"), map.get("message"));
		}
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		return new ResponseEntity<String>(om.writeValueAsString(map), responseHeaders, HttpStatus.OK);
	}
	
	
	/**
	 * MainPanel 버전 정보 조회 단말 API(getMainPanelVersionInfo), MainPanel 연동 정보 조회 단말 API(getMainPanelInterlockingInfo),
	 * I20 편성앨범 조회 단말 API(getI20AlbumList), Best VOD 조회 단말 API(getGenreVodBestList), 자체편성 조회 단말 API(getSUXMAlbumList),
	 * Smart Start 항목 조회 단말 API(getSmartStartItemList)를 호출하는 함수
	 * 캐시 내용을 최신 내용으로 갱신하며 연관된 다른 WAS와 캐시를 동기화 하는 작업까지 진행한다
	 * @throws Exception
	 */
	/*private void CacheJob() throws Exception{
		
		String host = SmartUXProperties.getProperty("scheduler.host"); 					// 자기 자신것을 호출할때 사용하는 host(스케듈러에 정의된 것을 사용)
		int port = Integer.parseInt(SmartUXProperties.getProperty("scheduler.port"));	// 자기 자신것을 호출할때 사용하는 port(스케듈러에 정의된 것을 사용)
		String param = "callByScheduler=A";		// 관리자툴에서 호출한다는 의미로 셋팅해준다(이 값이 A여야 DB에서 바로 읽어서 캐쉬에 반영한다)
		int timeout = Integer.parseInt(SmartUXProperties.getProperty("scheduler.timeout"));		// timeout 값은 스케쥴러것을 사용
		int retrycnt = Integer.parseInt(SmartUXProperties.getProperty("scheduler.retrycnt"));	// 재시도 횟수는 스케줄러 것을 사용
		String protocolName = "";
		
		
		// MainPanel 버전 정보 조회 단말 API URL을 가져온다
		protocolName = SmartUXProperties.getProperty("MainPanelDao.getMainPanelVersionInfo.protocol");
		String url = SmartUXProperties.getProperty("MainPanelDao.refreshMainPanelVersionInfo.url");
		//GlobalCom.callAPI(host, port, url, param, timeout, retrycnt, protocolName);		// 현재 자기 자신꺼에 호출..
		GlobalCom.syncServerCache(url, param, timeout, retrycnt, protocolName);		// 다른 서버의 캐쉬 동기화 작업 진행
		
		// MainPanel 연동 정보 조회 단말 API URL을 가져온다
		protocolName = SmartUXProperties.getProperty("MainPanelDao.getMainPanelInterlockingInfo.protocol");
		url = SmartUXProperties.getProperty("MainPanelDao.refreshMainPanelInfo.url");
		//GlobalCom.callAPI(host, port, url, param, timeout, retrycnt, protocolName);		// 현재 자기 자신꺼에 호출..
		GlobalCom.syncServerCache(url, param, timeout * 3, retrycnt, protocolName); 	// 다른 서버의 캐쉬 동기화 작업 진행
		
		// 카테고리 맵에 따른 앨범 조회 단말 API URL을 가져온다
		protocolName = SmartUXProperties.getProperty("MainPanelDao.getI20AlbumList.protocol");
		url = SmartUXProperties.getProperty("MainPanelDao.refreshI20AlbumList.url");
		//GlobalCom.callAPI(host, port, url, param, timeout, retrycnt, protocolName);		// 현재 자기 자신꺼에 호출..
		GlobalCom.syncServerCache(url, param, timeout * 3, retrycnt, protocolName); 	// 다른 서버의 캐쉬 동기화 작업 진행
		
		// BEST VOD API 호출하여 캐쉬 갱신
		protocolName = SmartUXProperties.getProperty("GenreVodBestListDao.getGenreVodBestList.protocol");
		url = SmartUXProperties.getProperty("GenreVodBestListDao.getGenreVodBestList.CacheScheduleURL");
		GlobalCom.syncServerCache(url, param, timeout * 3, retrycnt, protocolName); 	// 다른 서버의 캐쉬 동기화 작업 진행
		
		// 자체편성 API 호출하여 캐쉬 갱신
		protocolName = SmartUXProperties.getProperty("SUXMAlbumListDao.getSUXMAlbumList.protocol");
		url = SmartUXProperties.getProperty("SUXMAlbumListDao.refreshSUXMAlbumList.url");
		GlobalCom.syncServerCache(url, param, timeout * 3, retrycnt, protocolName); 	// 다른 서버의 캐쉬 동기화 작업 진행
		
		// Smart Start API 호출하여 캐쉬 갱신
		protocolName = SmartUXProperties.getProperty("SmartStartItemListDao.getSmartStartItemList.protocol");
		url = SmartUXProperties.getProperty("SmartStartItemListDao.refreshSmartStartItemList.url");
		//GlobalCom.callAPI(host, port, url, param, timeout, retrycnt, protocolName);		// 현재 자기 자신꺼에 호출..
		GlobalCom.syncServerCache(url, param, timeout, retrycnt, protocolName); 	// 다른 서버의 캐쉬 동기화 작업 진행
	}*/

	/**
	 * Panel 목록 조회 화면
	 * @param model		Model 객체
	 * @return			패널 목록 조회 화면 URL
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/mainpanel/getPanelList")
	public String getPanelList(
			@RequestParam(value = "findName", required = false, defaultValue = "") String findName,
            @RequestParam(value = "findValue", required = false, defaultValue = "") String findValue,
            @RequestParam(value = "startDt", required = false, defaultValue = "") String startDt,
            @RequestParam(value = "endDt", required = false, defaultValue = "") String endDt,
            @RequestParam(value = "abtestYnChk", required = false, defaultValue = "") String abtestYnChk,
			Model model) throws Exception{
		
		List<PanelVO> result = new ArrayList<PanelVO>();
		
		findName = HTMLCleaner.clean(findName);
        findValue = HTMLCleaner.clean(findValue);
        startDt = HTMLCleaner.clean(startDt);
        endDt = HTMLCleaner.clean(endDt);
        abtestYnChk = HTMLCleaner.clean(abtestYnChk);
		
		PanelSearchVo vo = new PanelSearchVo();
		vo.setFindName(findName);
		vo.setFindValue(findValue);
		vo.setAbtestYnChk(abtestYnChk);
		if (!startDt.isEmpty() && !endDt.isEmpty()) {
			vo.setStartDt(startDt);
			vo.setEndDt(endDt);
		}
		
		// 현재 DB에 등록되어 있는 패널을 읽어온다
		List<PanelVO> dbresult = service.getPanelList(vo);
		
		for(PanelVO objVO : dbresult){
			// pannel.properties 파일에서 등록할 패널들을 읽어온다
			objVO.setExistProperty("N");
			int propertylength = Integer.parseInt(SmartUXProperties.getProperty("pannel.length"));
			for(int i=1; i <= propertylength; i++){
				String idkey = "pannel.code" + i;
				String pannel_id = SmartUXProperties.getProperty(idkey);
				
				if(objVO.getPannel_id().equals(pannel_id)){
					objVO.setExistProperty("Y");
					break;
				}
			}
			
			if(objVO.getFocus_type() == null){
				objVO.setFocus_type("");
			}
			else if(objVO.getFocus_type().equals("1")){
				objVO.setFocus_type("지정");
			}
			else if(objVO.getFocus_type().equals("2")){
				objVO.setFocus_type("히스토리");
			}
			else{
				objVO.setFocus_type("");
			}
			
			result.add(objVO);
		}
		
		model.addAttribute("vo", vo);
		model.addAttribute("result", result);
		
		return "/admin/mainpanel/getPanelList";
		
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
	@RequestMapping(value = "/admin/mainpanel/changePanelOrder", method = RequestMethod.GET)
	public String changeScheOrder(
			HttpServletRequest request, Model model) throws Exception {
		List<PanelVO> result = new ArrayList<PanelVO>();
		
		// 현재 DB에 등록되어 있는 패널을 읽어온다
		List<PanelVO> dbresult = service.getPanelList();
		
		for(PanelVO objVO : dbresult){
			// pannel.properties 파일에서 등록할 패널들을 읽어온다
			objVO.setExistProperty("N");
			int propertylength = Integer.parseInt(SmartUXProperties.getProperty("pannel.length"));
			for(int i=1; i <= propertylength; i++){
				String idkey = "pannel.code" + i;
				String pannel_id = SmartUXProperties.getProperty(idkey);
				
				if(objVO.getPannel_id().equals(pannel_id)){
					objVO.setExistProperty("Y");
					break;
				}
			}
			
			
			if(objVO.getFocus_type() == null){
				objVO.setFocus_type("");
			}
			else if(objVO.getFocus_type().equals("1")){
				objVO.setFocus_type("지정");
			}
			else if(objVO.getFocus_type().equals("2")){
				objVO.setFocus_type("히스토리");
			}
			else{
				objVO.setFocus_type("");
			}
			
			result.add(objVO);
		}
		
		model.addAttribute("result", result);
		return "/admin/mainpanel/changePanelOrder";
	}
	
	@RequestMapping(value = "/admin/mainpanel/changeOrder", method = RequestMethod.PUT)
	@ResponseBody
	public String changeOrder(
			@RequestBody final MultiValueMap<String, String> data,
			HttpServletRequest request) throws Exception {
		String resultCode;
		String resultMsg;

		try {

			List<String> optionArray = data.get("optionArray[]");
			service.changeOrder(optionArray);

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
	 * Panel 목록 조회 화면
	 * @param model		Model 객체
	 * @return			패널 목록 조회 화면 URL
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/mainpanel/getPanelListPopup")
	public String getPanelListPopup(Model model) throws Exception{
		
		List<PanelVO> result = new ArrayList<PanelVO>();
		
		// 현재 DB에 등록되어 있는 패널을 읽어온다
		List<PanelVO> dbresult = service.getPanelList();
		
		for(PanelVO objVO : dbresult){
			// pannel.properties 파일에서 등록할 패널들을 읽어온다
			objVO.setExistProperty("N");
			int propertylength = Integer.parseInt(SmartUXProperties.getProperty("pannel.length"));
			for(int i=1; i <= propertylength; i++){
				String idkey = "pannel.code" + i;
				String pannel_id = SmartUXProperties.getProperty(idkey);
				
				if(objVO.getPannel_id().equals(pannel_id)){
					objVO.setExistProperty("Y");
					break;
				}
			}
			
			

			result.add(objVO);
		}
		
		model.addAttribute("result", result);
		
		return "/admin/mainpanel/getPanelListPopup";
		
	}
	
	
	
	
	
	/**
	 * Panel 목록 조회 화면
	 * @param model		Model 객체
	 * @return			패널 목록 조회 화면 URL
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/mainpanel/getPanelListPop")
	public String getPanelListPop(Model model) throws Exception{
		
		List<PanelVO> result = new ArrayList<PanelVO>();
		
		// 현재 DB에 등록되어 있는 패널을 읽어온다
		List<PanelVO> dbresult = service.getPanelList();
		
		for(PanelVO objVO : dbresult){
			// pannel.properties 파일에서 등록할 패널들을 읽어온다
			objVO.setExistProperty("N");
			int propertylength = Integer.parseInt(SmartUXProperties.getProperty("pannel.length"));
			for(int i=1; i <= propertylength; i++){
				String idkey = "pannel.code" + i;
				String pannel_id = SmartUXProperties.getProperty(idkey);
				
				if(objVO.getPannel_id().equals(pannel_id)){
					objVO.setExistProperty("Y");
					break;
				}
			}
			
			result.add(objVO);
		}
		
		model.addAttribute("result", result);
		
		return "/admin/mainpanel/getPanelListPop";
		
	}
	
	
	/**
	 * Panel 상세 조회 화면
	 * @param panel_id		Panel을 상세조회 하고자 하는 panel_id
	 * @param model			Model 객체
	 * @return				패널 상세조회 화면 URL
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/mainpanel/viewPanel")
	public String viewPanel(@RequestParam(value="panel_id") String panel_id, Model model) throws Exception{
		
		HTMLCleaner cleaner = new HTMLCleaner();
		panel_id 	= cleaner.clean(panel_id);
		
		// 파라미터로 입력받은 panel_id가 프로퍼티에 있는 것인지를 확인한다
		
		int propertylength = Integer.parseInt(SmartUXProperties.getProperty("pannel.length"));
		String existCategory = "N";
		for(int i=1; i <= propertylength; i++){
			String idkey = "pannel.code" + i;
			String pannel_id = SmartUXProperties.getProperty(idkey);
			
			if(panel_id.equals(pannel_id)){
				existCategory = "Y";
				break;
			}
		}

		PanelVO result = service.viewPanel(panel_id);
		model.addAttribute("result", result);
		model.addAttribute("existCategory", existCategory);
		return "/admin/mainpanel/viewPanel";
	}
	
	/**
	 * Panel 등록 화면
	 * @param Model			Model 객체
	 * @return				패널 등록 화면 URL
	 */
	@RequestMapping(value="/admin/mainpanel/insertPanel", method=RequestMethod.GET)
	public String insertPanel(Model model
			, @RequestParam(value="frame_type", required=false, defaultValue="30") String frame_type) throws Exception{
		//시니어서비스 panel_ui_type : 30
		//String frame_type = "30";
		HTMLCleaner cleaner = new HTMLCleaner();
		frame_type 	= cleaner.clean(frame_type);
		
		List<PanelVO> result = new ArrayList<PanelVO>();
		// 패널UI타입을 읽어온다
		List<FrameVO> panelUiTypeSelect = service.getPanelUiTypeSelect(frame_type);
		
		// 현재 DB에 등록되어 있는 패널을 읽어온다
		List<PanelVO> dbresult = service.getPanelList();
		
		// pannel.properties 파일에서 등록할 패널들을 읽어온다
		int propertylength = Integer.parseInt(SmartUXProperties.getProperty("pannel.length"));
		for(int i=1; i <= propertylength; i++){
			String idkey = "pannel.code" + i;
			String pannel_id = SmartUXProperties.getProperty(idkey);
			String nmkey = "pannel.msg" + i;
			String pannel_nm = SmartUXProperties.getProperty(nmkey);
			
			boolean find = false;
			for(int j=0; j < dbresult.size(); j++){
				PanelVO dbobj = dbresult.get(j);
				if(dbobj.getPannel_id().equals(pannel_id)){					// DB에서 읽은 것이 현재 프로퍼티에 존재하면
					find = true;
					break;
				}
			}
			
			if(find == false){
				PanelVO obj = new PanelVO();
				obj.setPannel_id(pannel_id);
				obj.setPannel_nm(pannel_nm);
				result.add(obj);
			}
		}
		
		PanelVO panelVO = service.getPanelId();
		model.addAttribute("result", result);
		model.addAttribute("panelVO", panelVO);
		model.addAttribute("panelUiTypeSelect", panelUiTypeSelect);
		
		return "/admin/mainpanel/insertPanel";
	}
	
	/**
	 * Panel 등록 처리 작업
	 * @param panel_id				Panel 코드
	 * @param panel_nm				Panel 이름
	 * @param use_yn				Panel의 사용여부(Y/N)	
	 * @return						처리 결과를 기록한 json 문자열
	 */
	@RequestMapping(value="/admin/mainpanel/insertPanel", method=RequestMethod.POST)
	public @ResponseBody String insertPanel(
			HttpServletRequest request,
			@RequestParam(value="panel_id") String panel_id, 
			@RequestParam(value="panel_nm") String panel_nm,
			@RequestParam(value="panel_ui_type", defaultValue="") String panel_ui_type,
			@RequestParam(value="use_yn") String use_yn,
			@RequestParam(value="panel_image", required=false) MultipartFile uploadfile,
			@RequestParam(value="focus_type", defaultValue="") String focus_type,
			@RequestParam(value="smartUXManager") String login_id) {
		
		HTMLCleaner cleaner = new HTMLCleaner();
		panel_id 	= cleaner.clean(panel_id);
		panel_nm 	= cleaner.clean(panel_nm);
		panel_ui_type = cleaner.clean(panel_ui_type);
		use_yn 	= cleaner.clean(use_yn);
		login_id 	= cleaner.clean(login_id);
		focus_type 	= cleaner.clean(focus_type);
		
		String create_id = login_id;			// 등록자 ID(차후에 로그인 한 사람의 로그인 ID로 바꿀것
		String resultcode = "";
		String resultmessage = "";
		
		String tmp_file_name = "";
		String panel_image = "";
		File img_file = null;
		
		try{
			
			validateInsertPanel(panel_id, panel_nm, use_yn);					// 입력받은 파라미터 값들에 대한 validation 작업 진행
			
			int count = service.getPanelidCnt(panel_id);						// 입력받은 panel 코드가 기존에 존재하는지를 확인
			
			if(count == 0){														// 입력받은 panel 코드가 기존에 존재하지 않으면
				count = service.getPanelnmCnt(panel_nm);						// 입력받은 panel 이름이 기존에 존재하는지를 확인
				if(count == 0){													// 입력받은 panel 이름이 기존에 존재하지 않으면
					if(uploadfile != null && uploadfile.getSize() != 0L){
						tmp_file_name = uploadfile.getOriginalFilename();
						String ext = tmp_file_name.substring(tmp_file_name.lastIndexOf(".")+1,tmp_file_name.length()); // 확장자 구하기
						panel_image = "panel/" + panel_id + "_" + Long.toString(System.currentTimeMillis()) + "." + ext; // 파일 이름을 패널이미지 저장위치(panel/)패널 id_시스템타임.확장자 구조로 한다
						
						String newFilename = SmartUXProperties.getProperty("imgupload.dir") + panel_image;
						
						logger.debug("tmp_file_name : " + tmp_file_name);
						logger.debug("ext :  " + ext);
						logger.debug("panel_image :  " + panel_image);
						logger.debug("newFilename :  " + newFilename);
						img_file = new File(newFilename);
						uploadfile.transferTo(img_file);
					}

					service.insertPanel(panel_id, panel_nm, use_yn, create_id, panel_ui_type, panel_image , focus_type);
					// CacheJob();
					resultcode = SmartUXProperties.getProperty("flag.success");
					resultmessage = SmartUXProperties.getProperty("message.success");
				}else{															// 입력받은 panel 이름이 기존에 존재하면
					SmartUXException e = new SmartUXException();
					e.setFlag("EXISTS PANEL_NM");
					e.setMessage("패널명 존재");
					throw e;
				}
			}else{																// 입력받은 panel 코드가 기존에 존재하면
				SmartUXException e = new SmartUXException();
				e.setFlag("EXISTS PANEL_ID");
				e.setMessage("패널 코드 존재");
				throw e;
			}
			
		}catch(Exception e){
			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}
		
		
		return "{\"flag\" : \"" + resultcode + "\", \"message\" : \"" + resultmessage + "\"}";
	}
	
	/**
	 * insertPanel 함수에 대한 validation 작업 함수
	 * @param panel_id				Panel 코드
	 * @param panel_nm				Panel 이름
	 * @param use_yn				Panel의 사용여부(Y/N)	
	 * @throws SmartUXException
	 */
	private void validateInsertPanel(String panel_id, String panel_nm, String use_yn) throws SmartUXException{
		SmartUXException exception = new SmartUXException();
		if(!(StringUtils.hasText(panel_id))){
			exception.setFlag("NOT FOUND PANEL_ID");
			exception.setMessage("패널 코드는 필수로 들어가야 합니다");
			throw exception;
		}
		
		if(panel_id.length() > 4){
			exception.setFlag("PANEL_ID LENGTH");
			exception.setMessage("패널 코드는 4자 이내이어야 합니다");
		
			throw exception;
		}
		
		if(!(StringUtils.hasText(panel_nm))){
			exception.setFlag(SmartUXProperties.getProperty("NOT FOUND PANEL_NM"));
			exception.setMessage("패널명은 필수로 들어가야 합니다");
			throw exception;
		}
		
		if(panel_nm.length() > 100){
			exception.setFlag("PANEL_NM LENGTH");
			exception.setMessage("패널명은 100자 이내이어야 합니다");
		
			throw exception;
		}
		
		if(!(StringUtils.hasText(use_yn))){
			exception.setFlag(SmartUXProperties.getProperty("NOT FOUND USE_YN"));
			exception.setMessage(SmartUXProperties.getProperty("사용여부는 필수로 들어가야 합니다"));
			throw exception;
		}
	}
	
	/**
	 * 패널 수정 처리 작업
	 * @param panel_id			기존의 수정하고자 하는 panel_id
	 * @param newPanel_id		새로이 수정하고자 입력받은 panel_id
	 * @param panel_nm			기존의 수정하고자 하는 panel_nm
	 * @param newPanel_nm		새로이 수정하고자 입력받은 panel_nm
	 * @param use_yn			패널의 사용여부(Y/N)
	 * @param login_id			관리자 사이트에 로그인 한 사람의 로그인 id
	 * @return					처리 결과를 기록한 json 문자열
	 */
	@RequestMapping(value="/admin/mainpanel/updatePanel", method=RequestMethod.POST)
	public @ResponseBody String updatePanel(@RequestParam(value="panel_id") String panel_id, 
			@RequestParam(value="newPanel_id") String newPanel_id, 
			@RequestParam(value="panel_nm") String panel_nm,
			@RequestParam(value="newPanel_nm") String newPanel_nm,
			@RequestParam(value="use_yn") String use_yn,
			@RequestParam(value="smartUXManager") String login_id,
			@RequestParam(value="panel_ui_type") String panel_ui_type,
			@RequestParam(value="panel_image_nm") String panel_image_nm,
			@RequestParam(value="focus_type") String focus_type,
			@RequestParam(value="panel_image", required=false) MultipartFile uploadfile) {
		
		HTMLCleaner cleaner = new HTMLCleaner();
		panel_id 	= cleaner.clean(panel_id);
		newPanel_id 	= cleaner.clean(newPanel_id);
		panel_nm 	= cleaner.clean(panel_nm);
		newPanel_nm 	= cleaner.clean(newPanel_nm);
		use_yn 	= cleaner.clean(use_yn);
		login_id 	= cleaner.clean(login_id);
		panel_ui_type = cleaner.clean(panel_ui_type);
		focus_type = cleaner.clean(focus_type);
		
		String update_id = login_id;			// 수정자 ID(차후에 로그인 한 사람의 로그인 ID로 바꿀것
		String resultcode = "";
		String resultmessage = "";
		
		/*String tmp_file_name = "";
		String bg_img_file = "";
		File img_file = null;*/
		
		String tmp_file_name = "";
		String panel_image = "";
		File img_file = null;
		
		try{
			validateUpdatePanel(panel_id, newPanel_id, panel_nm, newPanel_nm, use_yn);			// 입력받은 파라미터 값들에 대한 validation 작업 진행
			
			if(uploadfile != null &&  uploadfile.getSize() != 0L){
				tmp_file_name = uploadfile.getOriginalFilename();
				String ext = tmp_file_name.substring(tmp_file_name.lastIndexOf(".")+1,tmp_file_name.length()); // 확장자 구하기
				panel_image = "panel/" + panel_id + "_" + Long.toString(System.currentTimeMillis()) + "." + ext; // 파일 이름을 패널이미지 저장위치(panel/)패널 id_시스템타임.확장자 구조로 한다
				
				String newFilename = SmartUXProperties.getProperty("imgupload.dir") + panel_image;
				
				logger.debug("tmp_file_name : " + tmp_file_name);
				logger.debug("ext :  " + ext);
				logger.debug("panel_image :  " + panel_image);
				logger.debug("newFilename :  " + newFilename);
				img_file = new File(newFilename);
				uploadfile.transferTo(img_file);
			}
			
			
			if((panel_id.equals(newPanel_id)) && (panel_nm.equals(newPanel_nm))){				// 기존의 패널코드와 새로운 패널코드가 같고 기존의 패널명과 새로이 수정하고자 하는 패널명이 같은 경우는 사용여부에 대한 수정만을 의미하므로 바로 수정하는 함수를 실행하도록 한다
				service.updatePanel(panel_id, newPanel_id, panel_nm, newPanel_nm, use_yn, update_id, panel_ui_type, panel_image , focus_type , panel_image_nm);
				// CacheJob();
				resultcode = SmartUXProperties.getProperty("flag.success");
				resultmessage = SmartUXProperties.getProperty("message.success");
			}else{
				
				
				if(!(panel_id.equals(newPanel_id))){			// 기존의 패널코드값과 새로이 입력한 패널코드값이 다르면 새로이 입력한 패널코드값이 기존에 존재하는지를 확인한다
					int count = service.getPanelidCnt(newPanel_id);
					
					if(count == 0){								// 새로이 입력한 패널값이 존재하지 않으면
						if(panel_nm.equals(newPanel_nm)){		// 기존의 패널명과 신규 패널명이 같으면 패널 코드만 수정하는 것이므로 바로 수정이 들어간다
							service.updatePanel(panel_id, newPanel_id, panel_nm, newPanel_nm, use_yn, update_id, panel_ui_type, panel_image , focus_type , panel_image_nm);
							// CacheJob();
							resultcode = SmartUXProperties.getProperty("flag.success");
							resultmessage = SmartUXProperties.getProperty("message.success");
						}else{									// 기존의 패널명과 신규 패널명이 다르면 새로운 패널명이 존재하는지를 확인한다
							count = service.getPanelnmCnt(newPanel_nm);
							if(count == 0){						// 사용자가 입력한 패널명이 존재하지 않으면 사용자가 입력한 패널코드와 패널명이 기존에 입력된 것들이 아니기 때문에 바로 수정이 들어간다
								service.updatePanel(panel_id, newPanel_id, panel_nm, newPanel_nm, use_yn, update_id, panel_ui_type, panel_image , focus_type , panel_image_nm);
								// CacheJob();
								resultcode = SmartUXProperties.getProperty("flag.success");
								resultmessage = SmartUXProperties.getProperty("message.success");
							}else{								// 사용자가 입력한 패널명이 존재하면
								SmartUXException e = new SmartUXException();
								e.setFlag("EXISTS PANEL_NM");
								e.setMessage("패널명 존재");
								throw e;
							}
						}
						
						
					}else{										// 새로이 입력한 패널코드값이 존재하면
						SmartUXException e = new SmartUXException();
						e.setFlag("EXISTS PANEL_ID");
						e.setMessage("패널 코드 존재");
						throw e;
					}
					
				}else if(!(panel_nm.equals(newPanel_nm))){		// 기존의 패널명과 새로이 입력한 패널명이 다르면 새로이 입력한 패널명이 기존에 존재하는지를 확인한다
					int count = service.getPanelnmCnt(newPanel_nm);
					
					if(count == 0){								// 새로이 입력한 패널명이 존재하지 않으면
						service.updatePanel(panel_id, newPanel_id, panel_nm, newPanel_nm, use_yn, update_id, panel_ui_type, panel_image , focus_type , panel_image_nm);
						// CacheJob();
						resultcode = SmartUXProperties.getProperty("flag.success");
						resultmessage = SmartUXProperties.getProperty("message.success");
					}else{										// 새로이 입력한 패널명이 존재하면
						SmartUXException e = new SmartUXException();
						e.setFlag("EXISTS PANEL_NM");
						e.setMessage("패널명 존재");
						throw e;
					}
				}
			}
		}catch(Exception e){
			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}
		
		
		return "{\"flag\" : \"" + resultcode + "\", \"message\" : \"" + resultmessage + "\"}";
	}
	
	/**
	 * updatePanel 함수에 대한 validation 작업 함수
	 * @param panel_id			기존의 수정하고자 하는 panel_id
	 * @param newPanel_id		새로이 수정하고자 입력받은 panel_id
	 * @param panel_nm			기존의 수정하고자 하는 panel_nm
	 * @param newPanel_nm		새로이 수정하고자 입력받은 panel_nm
	 * @param use_yn			패널의 사용여부(Y/N)
	 */
	private void validateUpdatePanel(String panel_id, String newPanel_id, String panel_nm, String newPanel_nm, String use_yn) throws SmartUXException{
		SmartUXException exception = new SmartUXException();
		
		
		
		if(!(StringUtils.hasText(panel_id))){
			exception.setFlag("NOT ORG_PANEL_ID");
			exception.setMessage("기존 패널코드 값은 필수로 들어가야 합니다");
			throw exception;
		}
		
		if(panel_id.length() > 4){
			exception.setFlag("ORG_PANEL_ID LENGTH");
			exception.setMessage("기존 패널 코드는 4자 이내이어야 합니다");
		
			throw exception;
		}
		
		if(!(StringUtils.hasText(newPanel_id))){
			exception.setFlag("NOT NEW_PANEL_ID");
			exception.setMessage("신규 패널코드 값은 필수로 들어가야 합니다");
			throw exception;
		}
		
		if(newPanel_id.length() > 4){
			exception.setFlag("NEW_PANEL_ID LENGTH");
			exception.setMessage("신규 패널 코드는 4자 이내이어야 합니다");
		
			throw exception;
		}
		
		if(!(StringUtils.hasText(panel_nm))){
			exception.setFlag("NOT ORG_PANEL_NM");
			exception.setMessage("기존 패널명은 필수로 들어가야 합니다");
			throw exception;
		}
		
		if(panel_nm.length() > 100){
			exception.setFlag("ORG_PANEL_NM LENGTH");
			exception.setMessage("기존 패널명은 100자 이내이어야 합니다");
		
			throw exception;
		}
		
		if(!(StringUtils.hasText(newPanel_id))){
			exception.setFlag("NOT NEW_PANEL_NM");
			exception.setMessage("신규 패널명은 필수로 들어가야 합니다");
			throw exception;
		}
		
		if(newPanel_nm.length() > 100){
			exception.setFlag("NEW_PANEL_NM LENGTH");
			exception.setMessage("신규 패널명은 100자 이내이어야 합니다");
		
			throw exception;
		}
		
		if(!(StringUtils.hasText(use_yn))){
			exception.setFlag("NOT FOUND USE_YN");
			exception.setMessage("사용여부는 필수로 들어가야 합니다");
			throw exception;
		}
		
	}
	
	/**
	 * Panel 삭제 처리 작업(패널 삭제시엔 패널에 속한 지면들도 같이 삭제한다)
	 * @param panel_ids		삭제하고자 하는 panel_id 값들 배열	
	 * @return				처리 결과를 기록한 json 문자열
	 */
	@RequestMapping(value="/admin/mainpanel/deletePanel", method=RequestMethod.POST)
	public @ResponseBody String deletePanel(@RequestParam(value="panel_id[]") String [] panel_ids,
			@RequestParam(value="smartUXManager") String login_id) {
		
		String update_id = login_id;			// 수정자 ID(차후에 로그인 한 사람의 로그인 ID로 바꿀것
		String resultcode = "";
		String resultmessage = "";
		try{
			validateDeletePanel(panel_ids);								// 입력받은 파라미터 값들에 대한 validation 작업 진행
			service.deletePanel(panel_ids, update_id);
			// CacheJob();
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
	 * deletePanel 함수에 대한 validation 작업 함수
	 * @param panel_ids			삭제하고자 하는 panel_id 값들 배열	
	 */
	private void validateDeletePanel(String [] panel_ids) throws SmartUXException{
		SmartUXException exception = new SmartUXException();
		
		if((panel_ids == null) ||(panel_ids.length == 0)){
			exception.setFlag("NOT FOUND PANEL_CODE");
			exception.setMessage("패널코드값은 필수로 들어가야 합니다");
			throw exception;
		}
	}
	
	/**
	 * 지면 목록 조회 화면
	 * @param model		Model 객체
	 * @return			지면 목록 조회 화면 URL
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/mainpanel/getPanelTitleTempList")
	public String getPanelTitleTempList(@RequestParam(value="panel_id", defaultValue="") String panel_id, Model model) throws Exception{
		
		HTMLCleaner cleaner = new HTMLCleaner();
		panel_id 	= cleaner.clean(panel_id);
		
		// 패널 목록을 먼저 조회한다
		List<PanelVO> panel_result = service.getPanelList();
		model.addAttribute("panel_result", panel_result);
		
		// 처음 화면 로딩 했을땐 panel_id엔 값이 없을 것이므로 조회된 panel 목록에서 첫번째 것을 조회해서 보여준다
		if("".equals(GlobalCom.isNull(panel_id))){
			if(panel_result.size() != 0){
				panel_id = panel_result.get(0).getPannel_id();
			}
		}
		
		List<ViewVO> result = service.getPanelTitleTempList(panel_id);
		model.addAttribute("panel_id", panel_id);
		model.addAttribute("result", result);
		
		// 선택한 패널의 상용 프리뷰 검색(이것을 통해 현재 선택된 패널에 상용지면이 있는지를 확인할 수 있다)
		List<PreviewVO> realResult = null;
		try{
			realResult = service.previewPanelTitle(panel_id, "");
		}catch(Exception e){
			realResult = new ArrayList<PreviewVO>();
		}
		String realTitleYN = "N";
		if(realResult.size() == 0){		// 현재 선택한 패널에 상용 지면이 없을 경우
			realTitleYN = "N";
		}else{							// 현재 선택한 패널에 상용 지면이 있을 경우
			realTitleYN = "Y";
		}
		model.addAttribute("realTitleYN", realTitleYN);
		
		
		return "/admin/mainpanel/getPanelTitleTempList";
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 지면 목록 조회 화면
	 * @param model		Model 객체
	 * @return			지면 목록 조회 화면 URL
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/mainpanel/getPanelTitleTempListPopup")
	public ResponseEntity<String> getPanelTitleTempListPopup(
			@RequestParam(value="panel_id", defaultValue="") String panel_id, 
			@RequestParam(value="p_title_id", defaultValue="") String p_title_id,
			Model model
			) throws Exception{
		
		HTMLCleaner cleaner = new HTMLCleaner();
		panel_id 	= cleaner.clean(panel_id);
		p_title_id 	= cleaner.clean(p_title_id);
		
		List<ViewVO> result = new ArrayList<ViewVO>();
		
		if(p_title_id.equals("none")){
		 result = service.getPanelTitleTempListSubNull(panel_id );
		} else {
		 result = service.getPanelTitleTempListSub(panel_id , p_title_id);
		}
		
		System.out.println(JSONSerializer.toJSON(result).toString());
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(result).toString(), responseHeaders, HttpStatus.CREATED);
	
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 지면 등록 화면
	 * @param panel_id		등록하고자 하는 지면이 속하는 패널 id
	 * @param p_title_id	등록하고자 하는 지면이 속하는 상위 지면 id
	 * @param model			Model 객체
	 * @return				지면 등록 화면 URL
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/mainpanel/insertPanelTitleTemp", method=RequestMethod.GET)
	public String insertPanelTitleTemp(
		@RequestParam(value="panel_id") String panel_id,
		@RequestParam(value="p_title_id") String p_title_id,
		Model model) throws Exception{
		
		HTMLCleaner cleaner = new HTMLCleaner();
		panel_id 	= cleaner.clean(panel_id);
		p_title_id 	= cleaner.clean(p_title_id);
		
		model.addAttribute("panel_id", panel_id);
		model.addAttribute("p_title_id", p_title_id);
		
		return "/admin/mainpanel/insertPanelTitleTemp";
		
	}
	
	/**
	 * 지면 등록 처리 작업
	 * @param panel_id		등록하고자 하는 지면이 속하는 패널 id
	 * @param p_title_id	등록하고자 하는 지면이 속하는 상위 지면 id
	 * @param title_nm		지면 이름
	 * @param use_yn		지면의 사용여부(Y/N)
	 * @param login_id		관리자 사이트에 로그인 한 사람의 로그인 id
	 * @return				처리 결과를 기록한 json 문자열
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/mainpanel/insertPanelTitleTemp", method=RequestMethod.POST)
	public ResponseEntity<String> insertPanelTitleTemp(
			HttpServletRequest request,
			@RequestParam(value="panel_id") String panel_id,
			@RequestParam(value="p_title_id") String p_title_id,
			@RequestParam(value="title_nm") String title_nm,
			@RequestParam(value="title_color") String title_color,
			@RequestParam(value="use_yn") String use_yn,
			@RequestParam(value="smartUXManager") String login_id,
			@RequestParam(value="title_bg_img_file", required=false) MultipartFile uploadfile,
			@RequestParam(value="bg_img_file", required=false) MultipartFile uploadfile1,
			@RequestParam(value="bg_img_file2", required=false) MultipartFile uploadfile2, 
			@RequestParam(value="bg_img_file3", required=false) MultipartFile uploadfile3) throws Exception{
		
		HTMLCleaner cleaner = new HTMLCleaner();
		panel_id 	= cleaner.clean(panel_id);
		p_title_id 	= cleaner.clean(p_title_id);
		title_nm 	= cleaner.cleanwithoutLoginfunc(title_nm);
		title_color 	= cleaner.clean(title_color);
		use_yn 	= cleaner.clean(use_yn);
		login_id 	= cleaner.clean(login_id);
		
		String create_id = login_id;			// 등록자 ID(차후에 로그인 한 사람의 로그인 ID로 바꿀것
		ResultVO result = new ResultVO();
		try{
			
			validateInsertPanelTitleTemp(panel_id, title_nm, use_yn);					// 입력받은 파라미터 값들에 대한 validation 작업 진행
			
			// 같은 상위 지면 ID 것으로 같은 지면명이 있는지를 확인
			int count = service.getPanelTitleTempTitlenmCnt(panel_id, p_title_id, title_nm);
			
			if(count == 0){
				String tmp_file_name = "";
				String tmp_file_name1 = "";
				String tmp_file_name2 = "";
				String tmp_file_name3 = "";
				String title_bg_img_file = "";
				String bg_img_file = "";
				String bg_img_file2 = "";
				String bg_img_file3 = "";
				File img_file = null;
				File img_file1 = null;
				File img_file2 = null;
				File img_file3 = null;
				
				if(uploadfile != null && uploadfile.getSize() != 0L){
					checkFileSize(uploadfile, 1);
					tmp_file_name = uploadfile.getOriginalFilename();
					String ext = tmp_file_name.substring(tmp_file_name.lastIndexOf(".")+1,tmp_file_name.length()); // 확장자 구하기
					title_bg_img_file = "panel/" + panel_id + "_" + Long.toString(System.currentTimeMillis()) + "." + ext; // 파일 이름을 패널이미지 저장위치(panel/) 패널 id_지면 id_시스템타임.확장자 구조로 한다
					
					String newFilename = SmartUXProperties.getProperty("imgupload.dir") + "/" + title_bg_img_file;
					
					logger.debug("tmp_file_name : " + tmp_file_name);
					logger.debug("ext :  " + ext);
					logger.debug("title_bg_img_file :  " + title_bg_img_file);
					logger.debug("newFilename :  " + newFilename);
					img_file = new File(newFilename);
					uploadfile.transferTo(img_file);
				}
				
				if(uploadfile1 != null && uploadfile1.getSize() != 0L){
					checkFileSize(uploadfile1, 2);
					tmp_file_name1 = uploadfile1.getOriginalFilename();
					String ext = tmp_file_name1.substring(tmp_file_name1.lastIndexOf(".")+1,tmp_file_name1.length()); // 확장자 구하기
					bg_img_file = "panel/" + panel_id + "_" + Long.toString(System.currentTimeMillis()+1) + "." + ext; // 파일 이름을 패널이미지 저장위치(panel/) 패널 id_지면 id_시스템타임.확장자 구조로 한다
					
					String newFilename = SmartUXProperties.getProperty("imgupload.dir") + "/" + bg_img_file;
					
					logger.debug("tmp_file_name1 : " + tmp_file_name1);
					logger.debug("ext :  " + ext);
					logger.debug("bg_img_file :  " + bg_img_file);
					logger.debug("newFilename :  " + newFilename);
					img_file1 = new File(newFilename);
					uploadfile1.transferTo(img_file1);
				}
				
				if(uploadfile2 != null && uploadfile2.getSize() != 0L){
					checkFileSize(uploadfile2, 3);
					tmp_file_name2 = uploadfile2.getOriginalFilename();
					String ext = tmp_file_name2.substring(tmp_file_name2.lastIndexOf(".")+1,tmp_file_name2.length()); // 확장자 구하기
					bg_img_file2 = "panel/" + panel_id + "_" + Long.toString(System.currentTimeMillis()+2) + "." + ext; // 파일 이름을 패널이미지 저장위치(panel/) 패널 id_지면 id_시스템타임.확장자 구조로 한다
					
					String newFilename = SmartUXProperties.getProperty("imgupload.dir") + "/" + bg_img_file2;
					
					logger.debug("tmp_file_name2 : " + tmp_file_name2);
					logger.debug("ext :  " + ext);
					logger.debug("bg_img_file2 :  " + bg_img_file2);
					logger.debug("newFilename :  " + newFilename);
					img_file2 = new File(newFilename);
					uploadfile2.transferTo(img_file2);
				}
				
				
				if(uploadfile3 != null && uploadfile3.getSize() != 0L){
					checkFileSize(uploadfile3, 4);
					tmp_file_name3 = uploadfile3.getOriginalFilename();
					String ext = tmp_file_name3.substring(tmp_file_name3.lastIndexOf(".")+1,tmp_file_name3.length()); // 확장자 구하기
					bg_img_file3 = "panel/" + panel_id + "_" + Long.toString(System.currentTimeMillis()+2) + "." + ext; // 파일 이름을 패널이미지 저장위치(panel/) 패널 id_지면 id_시스템타임.확장자 구조로 한다
					
					String newFilename = SmartUXProperties.getProperty("imgupload.dir") + "/" + bg_img_file3;
					
					logger.debug("tmp_file_name3 : " + tmp_file_name3);
					logger.debug("ext :  " + ext);
					logger.debug("bg_img_file3 :  " + bg_img_file3);
					logger.debug("newFilename :  " + newFilename);
					img_file3 = new File(newFilename);
					uploadfile3.transferTo(img_file3);
				}
				
				service.insertPanelTitleTemp(panel_id, title_nm, title_color, p_title_id, use_yn, create_id, title_bg_img_file, bg_img_file, bg_img_file2 , bg_img_file3);
				result.setFlag(SmartUXProperties.getProperty("flag.success"));
				result.setMessage(SmartUXProperties.getProperty("message.success"));
			}else{
				SmartUXException e = new SmartUXException();
				e.setFlag(SmartUXProperties.getProperty("flag.key1"));
				e.setMessage("title_nm");
				
				e.setFlag("EXISTS TITLE_NM");
				e.setMessage("지면명 존재");
				throw e;
			}
			
		}catch(Exception e){
			ExceptionHandler handler = new ExceptionHandler(e);
			result.setFlag(handler.getFlag());
			result.setMessage(handler.getMessage());
		}
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/plain; charset=UTF-8");
		return new ResponseEntity<String>(om.writeValueAsString(result), responseHeaders, HttpStatus.OK);
	}
	
	/**
	 * insertPanelTitleTemp 함수에 대한 validation 작업 함수
	 * @param panel_id				등록하고자 하는 지면이 속하는 패널 id
	 * @param title_nm				지면 이름
	 * @param use_yn				지면의 사용여부(Y/N)
	 * @throws SmartUXException
	 */
	private void validateInsertPanelTitleTemp(String panel_id, String title_nm, String use_yn) throws SmartUXException{
		SmartUXException exception = new SmartUXException();
		

		if(!(StringUtils.hasText(panel_id))){
			exception.setFlag("NOT PANEL_ID");
			exception.setMessage("패널코드 값은 필수로 들어가야 합니다");
			throw exception;
		}
		
		if(panel_id.length() > 4){
			exception.setFlag("PANEL_ID LENGTH");
			exception.setMessage("패널코드는 4자 이내이어야 합니다");
		
			throw exception;
		}
		
		if(!(StringUtils.hasText(title_nm))){
			exception.setFlag("NOT TITLE_NM");
			exception.setMessage("지면명은 필수로 들어가야 합니다");
			throw exception;
		}
		
		if(title_nm.length() > 100){
			exception.setFlag("TITLE_NM LENGTH");
			exception.setMessage("지면명은 100자 이내이어야 합니다");
		
			throw exception;
		}
		
		if(!(StringUtils.hasText(use_yn))){
			exception.setFlag("NOT FOUND USE_YN");
			exception.setMessage("사용여부는 필수로 들어가야 합니다");
			throw exception;
		}
	}
	
	/**
	 * 지면 상세 조회 화면
	 * @param panel_id		상세조회 하고자 하는 지면이 속한 panel_id
	 * @param title_id		상세조회 하고자 하는 지면의 title_id
	 * @param model			Model 객체
	 * @return				지면 상세 조회 화면 URL
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/mainpanel/viewPanelTitleTemp")
	public String viewPanelTitleTemp(@RequestParam(value="panel_id") String panel_id, 
			@RequestParam(value="title_id") String title_id,
			Model model) throws Exception{
		
		HTMLCleaner cleaner = new HTMLCleaner();
		panel_id 	= cleaner.clean(panel_id);
		title_id 	= cleaner.clean(title_id);
		
		ViewVO result = service.viewPanelTitleTemp(panel_id, title_id);
		ViewVO p_result = service.viewPanelTitleTemp(panel_id, result.getP_title_id());
		
		model.addAttribute("p_result", p_result);
		model.addAttribute("result", result);
		return "/admin/mainpanel/viewPanelTitleTemp";
	}
	
	/**
	 * 지면 수정 작업
	 * @param panel_id		수정하고자 하는 지면이 속한 panel_id
	 * @param p_title_id	수정하고자 하는 지면이 속한 상위 지면 id
	 * @param title_id		수정하고자 하는 지면의 title_id
	 * @param title_nm		수정하고자 하는 지면의 기존 지면 이름
	 * @param newTitle_nm	수정하고자 하는 지면의 새로운 지면 이름
	 * @param use_yn		지면의 사용여부(Y/N)
	 * @param login_id		관리자 사이트에 로그인 한 사람의 로그인 id
	 * @return
	 */
	@RequestMapping(value="/admin/mainpanel/updatePanelTitleTemp", method=RequestMethod.POST)
	public ResponseEntity<String> updatePanelTitleTemp(
			@RequestParam(value="panel_id") String panel_id,
			@RequestParam(value="p_title_id") String p_title_id,
			@RequestParam(value="title_id") String title_id,
			@RequestParam(value="title_nm") String title_nm,
			@RequestParam(value="title_color") String title_color,
			@RequestParam(value="newTitle_nm") String newTitle_nm,
			@RequestParam(value="use_yn") String use_yn,
			@RequestParam(value="smartUXManager") String login_id,
			@RequestParam(value="title_bg_img_file_nm") String title_bg_img_file_nm,
			@RequestParam(value="bg_img_file_nm") String bg_img_file_nm,
			@RequestParam(value="bg_img_file2_nm") String bg_img_file2_nm,
			@RequestParam(value="bg_img_file3_nm") String bg_img_file3_nm,
			@RequestParam(value="title_bg_img_file", required=false) MultipartFile uploadfile,
			@RequestParam(value="bg_img_file", required=false) MultipartFile uploadfile1,
			@RequestParam(value="bg_img_file2", required=false) MultipartFile uploadfile2, 
			@RequestParam(value="bg_img_file3", required=false) MultipartFile uploadfile3) throws Exception{
		
		HTMLCleaner cleaner = new HTMLCleaner();
		panel_id 	= cleaner.clean(panel_id);
		p_title_id 	= cleaner.clean(p_title_id);
		title_id 	= cleaner.clean(title_id);
		title_nm 	= cleaner.cleanwithoutLoginfunc(title_nm);
		title_color 	= cleaner.clean(title_color);
		newTitle_nm 	= cleaner.cleanwithoutLoginfunc(newTitle_nm);
		use_yn 	= cleaner.clean(use_yn);
		login_id 	= cleaner.clean(login_id);
		
		String update_id = login_id;			// 수정자 ID(차후에 로그인 한 사람의 로그인 ID로 바꿀것
		ResultVO result = new ResultVO();
		try{
			
			validateUpdatePanelTitleTemp(panel_id, title_id, title_nm, newTitle_nm, use_yn);					// 입력받은 파라미터 값들에 대한 validation 작업 진행
			
			String tmp_file_name = "";
			String tmp_file_name1 = "";
			String tmp_file_name2 = "";
			String tmp_file_name3 = "";
			String title_bg_img_file = "";
			String bg_img_file = "";
			String bg_img_file2 = "";
			String bg_img_file3 = "";
			File img_file = null;
			File img_file1 = null;
			File img_file2 = null;
			File img_file3 = null;
			
			if(uploadfile != null && uploadfile.getSize() != 0L){
				checkFileSize(uploadfile, 1);
				tmp_file_name = uploadfile.getOriginalFilename();
				String ext = tmp_file_name.substring(tmp_file_name.lastIndexOf(".")+1,tmp_file_name.length()); // 확장자 구하기
				title_bg_img_file = "panel/" + panel_id + "_" + Long.toString(System.currentTimeMillis()) + "." + ext; // 파일 이름을 패널이미지 저장위치(panel/) 패널 id_지면 id_시스템타임.확장자 구조로 한다
				
				String newFilename = SmartUXProperties.getProperty("imgupload.dir") + "/" + title_bg_img_file;
				
				logger.debug("tmp_file_name : " + tmp_file_name);
				logger.debug("ext :  " + ext);
				logger.debug("title_bg_img_file :  " + title_bg_img_file);
				logger.debug("newFilename :  " + newFilename);
				img_file = new File(newFilename);
				uploadfile.transferTo(img_file);
			}
			
			if(uploadfile1 != null && uploadfile1.getSize() != 0L){
				checkFileSize(uploadfile1, 2);
				tmp_file_name1 = uploadfile1.getOriginalFilename();
				String ext = tmp_file_name1.substring(tmp_file_name1.lastIndexOf(".")+1,tmp_file_name1.length()); // 확장자 구하기
				bg_img_file = "panel/" + panel_id + "_" + Long.toString(System.currentTimeMillis()+1) + "." + ext; // 파일 이름을 패널이미지 저장위치(panel/) 패널 id_지면 id_시스템타임.확장자 구조로 한다
				
				String newFilename = SmartUXProperties.getProperty("imgupload.dir") + "/" + bg_img_file;
				
				logger.debug("tmp_file_name1 : " + tmp_file_name1);
				logger.debug("ext :  " + ext);
				logger.debug("bg_img_file :  " + bg_img_file);
				logger.debug("newFilename :  " + newFilename);
				img_file1 = new File(newFilename);
				uploadfile1.transferTo(img_file1);
			}
			
			if(uploadfile2 != null && uploadfile2.getSize() != 0L){
				checkFileSize(uploadfile2, 3);
				tmp_file_name2 = uploadfile2.getOriginalFilename();
				String ext = tmp_file_name2.substring(tmp_file_name2.lastIndexOf(".")+1,tmp_file_name2.length()); // 확장자 구하기
				bg_img_file2 = "panel/" + panel_id + "_" + Long.toString(System.currentTimeMillis()+2) + "." + ext; // 파일 이름을 패널이미지 저장위치(panel/) 패널 id_지면 id_시스템타임.확장자 구조로 한다
				
				String newFilename = SmartUXProperties.getProperty("imgupload.dir") + "/" + bg_img_file2;
				
				logger.debug("tmp_file_name2 : " + tmp_file_name2);
				logger.debug("ext :  " + ext);
				logger.debug("bg_img_file2 :  " + bg_img_file2);
				logger.debug("newFilename :  " + newFilename);
				img_file2 = new File(newFilename);
				uploadfile2.transferTo(img_file2);
			}
			
			
			if(uploadfile3 != null && uploadfile3.getSize() != 0L){
				checkFileSize(uploadfile3, 4);
				tmp_file_name3 = uploadfile3.getOriginalFilename();
				String ext = tmp_file_name3.substring(tmp_file_name3.lastIndexOf(".")+1,tmp_file_name3.length()); // 확장자 구하기
				bg_img_file3 = "panel/" + panel_id + "_" + Long.toString(System.currentTimeMillis()+2) + "." + ext; // 파일 이름을 패널이미지 저장위치(panel/) 패널 id_지면 id_시스템타임.확장자 구조로 한다
				
				String newFilename = SmartUXProperties.getProperty("imgupload.dir") + "/" + bg_img_file3;
				
				logger.debug("tmp_file_name3 : " + tmp_file_name3);
				logger.debug("ext :  " + ext);
				logger.debug("bg_img_file3 :  " + bg_img_file3);
				logger.debug("newFilename :  " + newFilename);
				img_file3 = new File(newFilename);
				uploadfile3.transferTo(img_file3);
			}
			
			if("".equals(GlobalCom.isNull(title_bg_img_file))){
				title_bg_img_file = GlobalCom.isNull(title_bg_img_file_nm);
			}
			if("".equals(GlobalCom.isNull(bg_img_file))){
				bg_img_file = GlobalCom.isNull(bg_img_file_nm);
			}
			if("".equals(GlobalCom.isNull(bg_img_file2))){
				bg_img_file2 = GlobalCom.isNull(bg_img_file2_nm);
			}
			if("".equals(GlobalCom.isNull(bg_img_file3))){
				bg_img_file3 = GlobalCom.isNull(bg_img_file3_nm);
			}
			
			if(title_nm.equals(newTitle_nm)){															// 기존 지면명과 신규 지면명이 같은 경우는 사용여부만 수정하는 것이므로 바로 수정하도록 한다
				service.updatePanelTitleTemp(panel_id, title_id, newTitle_nm, title_color, use_yn, update_id, title_bg_img_file, bg_img_file, bg_img_file2 , bg_img_file3);
				result.setFlag(SmartUXProperties.getProperty("flag.success"));
				result.setMessage(SmartUXProperties.getProperty("message.success"));
			}else{																						// 기존 지면명과 신규 지면명이 다른 경우는 지면명 체크 작업을 해야 한다
				// 같은 상위 지면 ID 것으로 같은 지면명이 있는지를 확인
				int count = service.getPanelTitleTempTitlenmCnt(panel_id, p_title_id, newTitle_nm);				// 입력받은 신규 지면명이 기존에 존재하는지를 확인
				
				if(count == 0){																			// 입력받은 신규 지면명이 기존에 존재하지 않으면
					service.updatePanelTitleTemp(panel_id, title_id, newTitle_nm, title_color, use_yn, update_id, title_bg_img_file, bg_img_file, bg_img_file2 , bg_img_file3);
					result.setFlag(SmartUXProperties.getProperty("flag.success"));
					result.setMessage(SmartUXProperties.getProperty("message.success"));
				}else{																					// 입력받은 신규 지면명이 기존에 존재하면
					SmartUXException e = new SmartUXException();
					e.setFlag("EXISTS NEW_TITLE_NM");
					e.setMessage("지면명 존재");
					throw e;
				}
			}
			
		}catch(Exception e){
			ExceptionHandler handler = new ExceptionHandler(e);
			result.setFlag(handler.getFlag());
			result.setMessage(handler.getMessage());
		}
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/plain; charset=UTF-8");
		return new ResponseEntity<String>(om.writeValueAsString(result), responseHeaders, HttpStatus.OK);
	}
	
	/**
	 * updatePanelTitleTemp 함수에 대한 validation 작업 함수
	 * @param panel_id				수정하고자 하는 지면이 속한 panel_id
	 * @param title_id				수정하고자 하는 지면의 title_id
	 * @param title_nm				수정하고자 하는 지면의 기존 지면 이름
	 * @param newTitle_nm			수정하고자 하는 지면의 새로운 지면 이름
	 * @param use_yn				지면의 사용여부(Y/N)
	 * @throws SmartUXException
	 */
	private void validateUpdatePanelTitleTemp(String panel_id, String title_id, String title_nm, String newTitle_nm, String use_yn) throws SmartUXException{
		SmartUXException exception = new SmartUXException();
		
		if(!(StringUtils.hasText(panel_id))){
			exception.setFlag("NOT PANEL_ID");
			exception.setMessage("패널코드 값은 필수로 들어가야 합니다");
			throw exception;
		}
		
		if(panel_id.length() > 4){
			exception.setFlag("PANEL_ID LENGTH");
			exception.setMessage("패널코드는 4자 이내이어야 합니다");
		
			throw exception;
		}
		
		if(!(StringUtils.hasText(title_id))){
			exception.setFlag("NOT TITLE_ID");
			exception.setMessage("지면코드는 필수로 들어가야 합니다");
			throw exception;
		}
		
		if(title_id.length() > 3){
			exception.setFlag("TITLE_ID LENGTH");
			exception.setMessage("지면코드는 3자 이내이어야 합니다");
		
			throw exception;
		}
		
		if(!(StringUtils.hasText(title_nm))){
			exception.setFlag("NOT TITLE_NM");
			exception.setMessage("기존 지면명은 필수로 들어가야 합니다");
			throw exception;
		}
		
		if(title_nm.length() > 100){
			exception.setFlag("TITLE_NM LENGTH");
			exception.setMessage("기존 지면명은 100자 이내이어야 합니다");
		
			throw exception;
		}
		
		if(!(StringUtils.hasText(newTitle_nm))){
			exception.setFlag("NOT NEW_TITLE_NM");
			exception.setMessage("신규 지면명은 필수로 들어가야 합니다");
			throw exception;
		}
		
		if(newTitle_nm.length() > 100){
			exception.setFlag("NEW_TITLE_NM LENGTH");
			exception.setMessage("신규 지면명은 100자 이내이어야 합니다");
		
			throw exception;
		}
		
		if(!(StringUtils.hasText(use_yn))){
			exception.setFlag("NOT FOUND USE_YN");
			exception.setMessage("사용여부는 필수로 들어가야 합니다");
			throw exception;
		}
	}


	/**
	 * 지면 삭제 처리 작업
	 * @param panel_id		삭제하고자 하는 지면이 속한 panel_id
	 * @param title_ids		삭제하고자 하는 지면의 title_id 배열
	 * @return				처리 결과를 기록한 json 문자열
	 */
	@RequestMapping(value="/admin/mainpanel/deletePanelTitleTemp", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> deletePanelTitleTemp(@RequestParam(value="panel_id") String panel_id, 
			@RequestParam(value="title_id[]") String [] title_ids) {
		
		String resultcode = "";
		String resultmessage = "";
		try{
			validateDeletePanelTitleTemp(panel_id, title_ids);								// 입력받은 파라미터 값들에 대한 validation 작업 진행
			service.deletePanelTitleTemp(panel_id, title_ids);
			resultcode = SmartUXProperties.getProperty("flag.success");
			resultmessage = SmartUXProperties.getProperty("message.success");
		}catch(Exception e){
			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}
		String result = "{\"flag\" : \"" + resultcode + "\", \"message\" : \"" + resultmessage + "\"}";

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(result, responseHeaders, HttpStatus.CREATED);
	}
	
	
	
	
	

	/**
	 * 지면 포커스 처리 작업
	 * @param panel_id		
	 * @param title_ids		
	 * @return				처리 결과를 기록한 json 문자열
	 */
	@RequestMapping(value="/admin/mainpanel/focusPanelTitleTemp", method=RequestMethod.POST)
	public @ResponseBody String focusPanelTitleTemp(@RequestParam(value="panel_id") String panel_id, 
			@RequestParam(value="title_id[]") String [] title_ids) {
		
		String resultcode = "";
		String resultmessage = "";
		try{
			validateDeletePanelTitleTemp(panel_id, title_ids);								// 입력받은 파라미터 값들에 대한 validation 작업 진행
		//	service.deletePanelTitleTemp(panel_id, title_ids);
		
			service.focusPanelTitleTemp(panel_id, title_ids);
			
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
	 * deletePanelTitleTemp 함수에 대한 validation 작업 함수
	 * @param panel_id				삭제하고자 하는 지면들의 panel_id 값	
	 * @param title_ids				삭제하고자 하는 지면들의 title_id 값들 배열	
	 * @throws SmartUXException
	 */
	private void validateDeletePanelTitleTemp(String panel_id, String [] title_ids) throws SmartUXException{
		SmartUXException exception = new SmartUXException();
		
		if(!(StringUtils.hasText(panel_id))){
			exception.setFlag("NOT FOUND PANEL_ID");
			exception.setMessage("패널코드값은 필수로 들어가야 합니다");
			throw exception;
		}
		
		if((title_ids == null) ||(title_ids.length == 0)){
			exception.setFlag("NOT FOUND TITLE_ID");
			exception.setMessage("지면코드값은 필수로 들어가야 합니다");
			throw exception;
		}
		
	}
	
	/**
	 * 지면 순서 바꾸기 화면
	 * @param panel_id			순서를 바꾸고자 하는 지면들이 속한 panel_id
	 * @param p_title_id		순서를 바꾸고자 하는 지면들이 속한 상위 지면 id
	 * @param model				Model 객체
	 * @return					지면 순서 바꾸기 화면 URL
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/mainpanel/changePanelTitleTempOrder", method=RequestMethod.GET)
	public String changePanelTitleTempOrder(@RequestParam(value="panel_id") String panel_id
			, @RequestParam(value="p_title_id") String p_title_id
			, Model model) throws Exception{
		
		HTMLCleaner cleaner = new HTMLCleaner();
		panel_id 	= cleaner.clean(panel_id);
		p_title_id 	= cleaner.clean(p_title_id);
		
		List<ViewVO> result = service.changePanelTitleTempOrderList(panel_id, p_title_id);
		model.addAttribute("panel_id", panel_id);
		model.addAttribute("result", result);
		
		return "/admin/mainpanel/changePanelTitleTempOrder";
	}
	
	/**
	 * 지면 순서 바꾸기 작업
	 * @param panel_id		순서를 바꾸고자 하는 지면들이 속한 panel_id
	 * @param title_ids		순서를 바꾸고자 하는 지면들이 속한 상위 지면 id
	 * @param login_id		관리자 사이트에 로그인 한 사람의 로그인 id
	 * @return				처리 결과를 기록한 json 문자열
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/mainpanel/changePanelTitleTempOrder", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> changePanelTitleTempOrder(@RequestParam(value="panel_id") String panel_id
			, @RequestParam(value="title_id[]") String [] title_ids
			, @RequestParam(value="smartUXManager") String login_id) throws Exception{
		
		String update_id = login_id;			// 수정자 ID(차후에 로그인 한 사람의 로그인 ID로 바꿀것
		
		String resultcode = "";
		String resultmessage = "";
		try{
			validateChangePanelTitleTempOrder(panel_id, title_ids);						// 입력받은 파라미터 값들에 대한 validation 작업 진행
			service.changePanelTitleTempOrderJob(panel_id, title_ids, update_id);
			resultcode = SmartUXProperties.getProperty("flag.success");
			resultmessage = SmartUXProperties.getProperty("message.success");
		}catch(Exception e){
			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}
		String result = "{\"flag\" : \"" + resultcode + "\", \"message\" : \"" + resultmessage + "\"}";
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(result, responseHeaders, HttpStatus.CREATED);
	}
	
	/**
	 * changePanelTitleTempOrder 함수에 대한 validation 작업 함수
	 * @param panel_id			순서를 바꾸고자 하는 지면들이 속한 panel_id
	 * @param title_ids			순서를 바꾸고자 하는 지면들이 속한 상위 지면 id
	 */
	private void validateChangePanelTitleTempOrder(String panel_id, String [] title_ids){
		
		SmartUXException exception = new SmartUXException();
		
		if(!(StringUtils.hasText(panel_id))){
			exception.setFlag("NOT FOUND PANEL_ID");
			exception.setMessage("패널코드값은 필수로 들어가야 합니다");
			throw exception;
		}
		
		if((title_ids == null) ||(title_ids.length == 0)){
			exception.setFlag("NOT FOUND TITLE_ID");
			exception.setMessage("지면코드값은 필수로 들어가야 합니다");
			throw exception;
		}
	}
	
	
	/**
	 * 예전 카테고리 조회 화면(더 이상 사용하지 않음)
	 * @param panel_id
	 * @param title_id
	 * @param category_id
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/mainpanel/getCategoryList", method=RequestMethod.GET)
	public String getCategoryList(@RequestParam(value="panel_id") String panel_id
			, @RequestParam(value="title_id") String title_id
			, @RequestParam(value="category_id") String category_id
			, Model model) throws Exception{
		
		HTMLCleaner cleaner = new HTMLCleaner();
		panel_id 	= cleaner.clean(panel_id);
		title_id 	= cleaner.clean(title_id);
		category_id 	= cleaner.clean(category_id);
		
		List<CategoryVO> result = service.getCategoryList(category_id);
		model.addAttribute("panel_id", panel_id);
		model.addAttribute("title_id", title_id);
		model.addAttribute("result", result);
		return "/admin/mainpanel/getCategoryList";
	}
	
	/**
	 * 예전 카테고리 조회 화면에서 카테고리를 조회하는 함수(더 이상 사용하지 않음)
	 * @param category_id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/mainpanel/getCategoryList", method=RequestMethod.POST)
	public ResponseEntity<String> getCategoryListJson(@RequestParam(value="category_id") String category_id
			) throws Exception{
		
		HTMLCleaner cleaner = new HTMLCleaner();
		category_id 	= cleaner.clean(category_id);
		
		List<CategoryVO> result = service.getCategoryList(category_id);
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(result).toString(), responseHeaders, HttpStatus.CREATED);
	}
	
	/**
	 * 예전 카테고리 조회 화면에서 카테고리 코드를 셋팅하는 함수(더 이상 사용하지 않음)
	 * @param panel_id
	 * @param title_id
	 * @param category_id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/mainpanel/updateCategory", method=RequestMethod.POST)
	public @ResponseBody String updateCategory(@RequestParam(value="panel_id") String panel_id
			,@RequestParam(value="title_id") String title_id
			,@RequestParam(value="category_id") String category_id
			) throws Exception{
		
		HTMLCleaner cleaner = new HTMLCleaner();
		panel_id 	= cleaner.clean(panel_id);
		title_id 	= cleaner.clean(title_id);
		category_id 	= cleaner.clean(category_id);
		
		String update_id = "update_id";			// 수정자 ID(차후에 로그인 한 사람의 로그인 ID로 바꿀것
		
		String resultcode = "";
		String resultmessage = "";
		try{
			validateUpdateCategory(panel_id, title_id, category_id);						// 입력받은 파라미터 값들에 대한 validation 작업 진행
			// service.updateCategory(panel_id, title_id, category_id, update_id);
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
	 * updateCategory 함수의 validation 작업을 하는 함수(더 이상 사용하지 않음)
	 * @param panel_id
	 * @param title_id
	 * @param category_id
	 */
	private void validateUpdateCategory(String panel_id, String title_id, String category_id){
		
		SmartUXException exception = new SmartUXException();
		
		if(!(StringUtils.hasText(panel_id))){
			exception.setFlag("NOT FOUND PANEL_ID");
			exception.setMessage("패널코드값은 필수로 들어가야 합니다");
			throw exception;
		}
		
		if(!(StringUtils.hasText(title_id))){
			exception.setFlag("NOT FOUND TITLE_ID");
			exception.setMessage("지면코드값은 필수로 들어가야 합니다");
			throw exception;
		}
		
		if(!(StringUtils.hasText(category_id))){
			exception.setFlag("NOT FOUND CATEGORY_ID");
			exception.setMessage("카테고리 코드값은 필수로 들어가야 합니다");
			throw exception;
		}
		
	}
	
	
	/**
	 * applyPanelTitle 함수의 validation 작업을 하는 함수
	 * @param panel_id		상용 지면 테이블에 반영하고자 하는 지면들이 속한 panel_id
	 */
	private void validateApplyPanelTitle(String panel_id){
		
		SmartUXException exception = new SmartUXException();
		
		if(!(StringUtils.hasText(panel_id))){
			exception.setFlag("NOT FOUND PANEL_ID");
			exception.setMessage("패널코드값은 필수로 들어가야 합니다");
			throw exception;
		}
	}
	
	/**
	 * 프리뷰 조회 화면
	 * @param panel_id			프리뷰를 보고자 하는 panel_id
	 * @param model				Model 객체
	 * @return					프리뷰 화면 URL
	 * @throws Exception	
	 */
	@RequestMapping(value="/admin/mainpanel/previewPanelTitleTemp", method=RequestMethod.GET)
	public String previewPanelTitleTemp(@RequestParam(value="panel_id") String panel_id, Model model) throws Exception{
		
		HTMLCleaner cleaner = new HTMLCleaner();
		panel_id 	= cleaner.clean(panel_id);
		
		//String bg_img_url = SmartUXProperties.getProperty("imgserver.weburl") + SmartUXProperties.getProperty("imgserver.imgpath");
		String bg_img_url = service.getImageServerURL("I");
		model.addAttribute("panel_id", panel_id);
		model.addAttribute("bg_img_url", bg_img_url);
		return "/admin/mainpanel/previewPanelTitleTemp";
	}
	
	/**
	 * 프리뷰 조회 화면을 보여주기 위한 데이터 조회 작업
	 * @param panel_id			프리뷰를 보고자 하는 panel_id
	 * @param separator			앨범 목록을 보여줄때 앨범과 앨범간의 연결 구분자(현재는 여기에 <br> 태그가 들어감으로써 화면에서 개행된 효과를 나타내고 있다)
	 * @return					처리 결과를 기록한 json 문자열
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/mainpanel/previewPanelTitleTemp", method=RequestMethod.POST)
	public ResponseEntity<String> previewPanelTitleTempJson(@RequestParam(value="panel_id") String panel_id
			, @RequestParam(value="separator") String separator) throws Exception{
		
		HTMLCleaner cleaner = new HTMLCleaner();
		panel_id 	= cleaner.clean(panel_id);
		
		List<PreviewVO> result = service.previewPanelTitleTemp(panel_id, separator);
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(result).toString(), responseHeaders, HttpStatus.CREATED);
	}
	
	/**
	 * 프리뷰(상용) 조회 화면
	 * @param panel_id			프리뷰(상용)을 보고자 하는 panel_id
	 * @param model				Model 객체
	 * @return					프리뷰(상용) 화면 URL
	 * @throws Exception	
	 */
	@RequestMapping(value="/admin/mainpanel/previewPanelTitle", method=RequestMethod.GET)
	public String previewPanelTitle(@RequestParam(value="panel_id") String panel_id, Model model) throws Exception{
		//String bg_img_url = SmartUXProperties.getProperty("imgserver.weburl") + SmartUXProperties.getProperty("imgserver.imgpath");
		String bg_img_url = service.getImageServerURL("I");
		
		HTMLCleaner cleaner = new HTMLCleaner();
		panel_id 	= cleaner.clean(panel_id);
		
		model.addAttribute("panel_id", panel_id);
		model.addAttribute("bg_img_url", bg_img_url);
		return "/admin/mainpanel/previewPanelTitle";
	}
	
	/**
	 * 프리뷰(상용) 조회 화면을 보여주기 위한 데이터 조회 작업
	 * @param panel_id			프리뷰(상용)을 보고자 하는 panel_id
	 * @param separator			앨범 목록을 보여줄때 앨범과 앨범간의 연결 구분자(현재는 여기에 <br> 태그가 들어감으로써 화면에서 개행된 효과를 나타내고 있다)
	 * @return					처리 결과를 기록한 json 문자열
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/mainpanel/previewPanelTitle", method=RequestMethod.POST)
	public ResponseEntity<String> previewPanelTitleJson(@RequestParam(value="panel_id") String panel_id
			, @RequestParam(value="separator") String separator) throws Exception{
		
		HTMLCleaner cleaner = new HTMLCleaner();
		panel_id 	= cleaner.clean(panel_id);
		
		List<PreviewVO> result = service.previewPanelTitle(panel_id, separator);
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(result).toString(), responseHeaders, HttpStatus.CREATED);
	}
	
	/**
	 * 프리뷰(상용) 조회 화면을 보여주기 위한 데이터 조회 작업
	 * @param panel_id			프리뷰(상용)을 보고자 하는 panel_id
	 * @param separator			앨범 목록을 보여줄때 앨범과 앨범간의 연결 구분자(현재는 여기에 <br> 태그가 들어감으로써 화면에서 개행된 효과를 나타내고 있다)
	 * @return					처리 결과를 기록한 json 문자열
	 * @throws Exception
	 * 관리자모드와 인터페이스 분리됨에 따라 관리자모드에서 브리뷰(상용)을 보기위해 호출 할 인터페이스
	 */
	@RequestMapping(value="/cacheCall/mainpanel/previewPanelTitle", method=RequestMethod.GET)
	public ResponseEntity<String> previewPanelTitleCacheCallJson(@RequestParam(value="panel_id") String panel_id
			, @RequestParam(value="separator") String separator) throws Exception{
		
		HTMLCleaner cleaner = new HTMLCleaner();
		panel_id 	= cleaner.clean(panel_id);
		
		List<PreviewVO> result = service.previewPanelTitle(panel_id, separator);
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(result).toString(), responseHeaders, HttpStatus.CREATED);
	}
	
	/**
	 * 지면 데이터 설정 조회 화면
	 * @param panel_id				지면 데이터 설정 작업을 하기 위한 지면이 속한 panel_id
	 * @param title_id				지면 데이터 설정 작업을 하기 위한 지면이 속한 title_id
	 * @param category_id			기존의 지면 데이터 설정이 되어 있을 경우의 기존의 설정되어 있는 category_id
	 * @param category_type			기존의 지면 데이터 설정이 되어 있을 경우의 기존의 설정되어 있는 category_type(LIVE, VOD, CAT_MAP, SCHEDULE, WISH)
	 * @param album_cnt				기존의 지면 데이터 설정이 되어 있을 경우의 기존의 설정되어 있는 album_cnt(이 변수는 category_type이 CAT_MAP 일 경우에만 값이 들어가 있다)
	 * @param model					Model 객체
	 * @return						지면 데이터 설정 조회 화면 URL
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/mainpanel/getTypeSelect", method=RequestMethod.GET)
	public String getTypeSelect(@RequestParam(value="panel_id") String panel_id
			, @RequestParam(value="title_id") String title_id
			, @RequestParam(value="category_id") String category_id
			, @RequestParam(value="category_type", required=false, defaultValue="") String category_type
			, @RequestParam(value="album_cnt", required=false, defaultValue="") String album_cnt
			, @RequestParam(value="category_gb", required=false) String category_gb
			, @RequestParam(value="scr_tp" , required=false, defaultValue="T") String scr_tp
			, Model model) throws Exception{
		
		HTMLCleaner cleaner = new HTMLCleaner();
		panel_id 	= cleaner.clean(panel_id);
		title_id 	= cleaner.clean(title_id);
		category_id 	= cleaner.clean(category_id);
		category_type 	= cleaner.clean(category_type);
		album_cnt 	= cleaner.clean(album_cnt);
		
		logger.debug("category_id : " + category_id);
		logger.debug("category_type : " + category_type);
		
		// panel_id와 title_id 값을 이용해서 PT_UX_PANEL_TITLE_TEMP 테이블의 UI_TYPE 컬럼과 DESCRIPTION 값을 조회한다
		ViewVO mainresult = service.getUITypeDescription(panel_id, title_id);
		
		String isUpdate = "N";
		if(!("".equals(mainresult.getPage_type()))){				// ui_type 컬럼에 값이 있다는 것은 수정작업임을 나타낸다(이걸 이용하여 화면에서 이미지 파일 올리는 부분에 대한 validation 작업 진행)
			isUpdate = "Y";
		}
		
		List<CodeItemVO> coderesult = codeservice.getCodeItemList("A0008");
		// 랭킹정보 조회
		List<EcrmRankVO> ecrmresult = ecrmrankservice.getRankList("I20", "N");
		List<EcrmRankVO> ecrmresult_i30 = ecrmrankservice.getRankList("I30", "N");
		
		List<EcrmRankVO> ecrmresult_cat = ecrmrankservice.getRankList("I20", "Y");
		List<EcrmRankVO> ecrmresult_cat_i30 = ecrmrankservice.getRankList("I30", "Y");


		String hdtvPanelID = SmartUXProperties.getProperty("hdtv.panel.id");

		List<CategoryVO> categoryresult;
		List<CategoryVO> categoryresult_i30 = null;

		if (GlobalCom.contains(hdtvPanelID, panel_id, "\\|")) {
			// 최상위 카테고리 정보 조회
			categoryresult = service.getCategoryList("NSC", "VC");
		} else {
			// 최상위 카테고리 정보 조회
			categoryresult = service.getCategoryList("VC");
			categoryresult_i30 = service.getCategoryList("I30", "VC");
		}

		// 자체편성 조회
		List<ScheduleVO> scheduleresult = scheduleservice.getScheduleList("I20");
		List<ScheduleVO> scheduleresult_i30 = scheduleservice.getScheduleList("I30");
		
		// category_type 값을 읽어서 그에 따른 값들을 조회한다
		if("VOD".equals(category_type)){
			
		}else if("CAT_MAP".equals(category_type) || "BOOKTV".equals(category_type) || "ENG_PRESCH".equals(category_type) || "BRAND".equals(category_type)){
			// || 를 구분자로 해서 분리해낸다
			String [] category_ids = category_id.split("\\|\\|");
			String [] album_cnts = album_cnt.split("\\|\\|");
			List<CategoryVO> userCategoryresult = new ArrayList<CategoryVO>();
			
			int idx = 0;
			for(String item : category_ids){
				CategoryVO obj;

				if (hdtvPanelID.contains(panel_id)) {
					obj = service.getCategoryIdName("NSC", item);
				} else {
					obj = service.getCategoryIdName(category_gb, item);
				}

				if(obj != null){
					obj.setAlbum_cnt(album_cnts[idx++]);
					userCategoryresult.add(obj);
				}
			}
			model.addAttribute("userCategoryresult", userCategoryresult);
		}else if("SCHEDULE".equals(category_type)){
			
		}
		
		String cateTypeList = SmartUXProperties.getProperty("category.type.code");
		
		String reps_album_name = service.getAlbumName(mainresult.getReps_album_id());
		mainresult.setReps_album_name(reps_album_name);
		
		
		//어플연동 타입
		List<CodeItemVO> apptyperesult = new ArrayList<CodeItemVO>();
		String apptype = SmartUXProperties.getProperty("paneltitle.apptype");
		String[] apptype_arr = apptype.split("\\|");
		for(int i = 0 ; i < apptype_arr.length; i++) {
			String apptypeunit = apptype_arr[i];
			if(apptypeunit.indexOf("^") > -1) {
				String[] apptypeunit_arr = apptypeunit.split("\\^");
				CodeItemVO vo = new CodeItemVO();
				vo.setItem_code(apptypeunit_arr[0]);
				vo.setItem_nm(apptypeunit_arr[1]);
				apptyperesult.add(vo);
			}
		}
		
		//20180912 월정액 상품코드 정보
		if(!GlobalCom.isEmpty(mainresult.getProduct_code())) {
			FlatRateVO flatVO = new FlatRateVO();
			flatVO.setProductID(SmartUXProperties.getProperty("smartux.product.code"));
			flatVO.setProductName(SmartUXProperties.getProperty("smartux.product.name"));
			
			List<FlatRateVO> flatRateList = commService.getFlatRateList2();
			flatRateList.add(0, flatVO);
			
			String[] dataArr = mainresult.getProduct_code().split("\\|");
			
			List<FlatRateVO> productCodeList = new ArrayList<FlatRateVO>();
			for(int i =0; i<dataArr.length;i++){
				boolean find = false;
				for(FlatRateVO flatRateVO : flatRateList){
					//선택한 월정액 상품코드로 상품명 조회
					if(flatRateVO.getProductID().equals(dataArr[i])){
						flatRateVO.setProductCodeUse(true);
						productCodeList.add(flatRateVO);
						find = true;
						break;
					}
				}
				if(find) {
					continue;
				}
				FlatRateVO noUseFlatRate = new FlatRateVO();
				noUseFlatRate.setProductID(dataArr[i]);
				noUseFlatRate.setProductName("유효하지 않은 상품");
				noUseFlatRate.setProductCodeUse(false);
				productCodeList.add(noUseFlatRate);
			}
			mainresult.setProduct_code_list(productCodeList);
		}
		
		//20180912 월정액 상품코드 정보(비노출)
		if(!GlobalCom.isEmpty(mainresult.getProduct_code_not())) {
			FlatRateVO flatVO = new FlatRateVO();
			flatVO.setProductID(SmartUXProperties.getProperty("smartux.product.code"));
			flatVO.setProductName(SmartUXProperties.getProperty("smartux.product.name"));
			
			List<FlatRateVO> flatRateList = commService.getFlatRateList2();
			flatRateList.add(0, flatVO);
			
			String[] dataArr = mainresult.getProduct_code_not().split("\\|");
			
			List<FlatRateVO> productCodeList = new ArrayList<FlatRateVO>();
			for(int i =0; i<dataArr.length;i++){
				boolean find = false;
				for(FlatRateVO flatRateVO : flatRateList){
					//선택한 월정액 상품코드로 상품명 조회
					if(flatRateVO.getProductID().equals(dataArr[i])){
						flatRateVO.setProductCodeUse(true);
						productCodeList.add(flatRateVO);
						find = true;
						break;
					}
				}
				if(find) {
					continue;
				}
				FlatRateVO noUseFlatRate = new FlatRateVO();
				noUseFlatRate.setProductID(dataArr[i]);
				noUseFlatRate.setProductName("유효하지 않은 상품");
				noUseFlatRate.setProductCodeUse(false);
				productCodeList.add(noUseFlatRate);
			}
			mainresult.setProduct_code_list_not(productCodeList);
		}
		
		
		List<String> terminal_list = service.getPaperTerminal(panel_id+"_"+title_id);
		String terminal_str = "";
		for(String terminal : terminal_list) {
			if("".equals(terminal_str)) {
				terminal_str = terminal;
			}else {
				terminal_str += ","+terminal;
			}
		}
		mainresult.setTerminal_arr(terminal_str);
		model.addAttribute("mainresult", mainresult);
		model.addAttribute("isUpdate", isUpdate);
		model.addAttribute("ecrmresult", ecrmresult);
		model.addAttribute("ecrmresult_i30", ecrmresult_i30);
		model.addAttribute("ecrmresult_cat", ecrmresult_cat);
		model.addAttribute("ecrmresult_cat_i30", ecrmresult_cat_i30);
		model.addAttribute("coderesult", coderesult);
		model.addAttribute("categoryresult", categoryresult);
		model.addAttribute("categoryresult_i30", categoryresult_i30);
		model.addAttribute("scheduleresult", scheduleresult);
		model.addAttribute("scheduleresult_i30", scheduleresult_i30);
		model.addAttribute("category_id", category_id);
		model.addAttribute("category_type", category_type);
		model.addAttribute("category_gb", category_gb);
		model.addAttribute("panel_id", panel_id);
		model.addAttribute("title_id", title_id);
		model.addAttribute("cateTypeList", cateTypeList);
		model.addAttribute("scr_tp", scr_tp);
		model.addAttribute("apptyperesult", apptyperesult);
		
		return "/admin/mainpanel/getTypeSelect";
	}
	
	/**
	 * 지면 데이터 설정 작업(jQuery의 ajaxForm으로 파일 업로드 형태로 등록이 되기때문에 기존의 작업들과는 달리 HttpServletRequest 객체에서 각 파라미터 값을 꺼내는 식으로 작업한다)
	 * @param request			HttpServletRequest 객체
	 * @param response			HttpServletResponse 객체
	 * @param uploadfile		업로드되는 이미지 파일
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/mainpanel/getTypeSelect", method=RequestMethod.POST)
	public ResponseEntity<String> getTypeSelect(HttpServletRequest request
			, HttpServletResponse response
			, @RequestParam(value="bg_img_file", required=false) MultipartFile uploadfile
			, @RequestParam(value="bg_img_file2", required=false) MultipartFile uploadfile2
			) throws Exception{
		
		logger.debug("contentType : " + request.getContentType());
		if(request instanceof MultipartHttpServletRequest){
			logger.debug("MultipartHttpServletRequest OK");
		}else{
			logger.debug("MultipartHttpServletRequest NOT OK");
		}
		
		// 파일을 업로드 하는 것이기 때문에 기존의 request 객체를 MultipartHttpServletRequest로 업로드 할 필요가 있다
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		 
		String panel_id = GlobalCom.isNull(multipartRequest.getParameter("panel_id"));				// 지면 데이터 설정 작업을 하기 위한 지면이 속한 panel_id
		String title_id = GlobalCom.isNull(multipartRequest.getParameter("title_id"));				// 지면 데이터 설정 작업을 하기 위한 지면이 속한 title_id
		String category_id = GlobalCom.isNull(multipartRequest.getParameter("category_id"));		// 지면 데이터 설정 작업을 하기 위해 설정된 category_id
		String category_type = GlobalCom.isNull(multipartRequest.getParameter("category_type"));	// 지면 데이터 설정 작업을 하기 위해 설정된 category_type(LIVE, VOD, CAT_MAP, SCHEDULE, WISH)
		String album_cnt = GlobalCom.isNull(multipartRequest.getParameter("album_cnt"));			// 지면 데이터 설정 작업을 하기 위해 설정된 album_cnt(category_type이 CAT_MAP일때만 값이 들어가 있다)
		String ui_type = GlobalCom.isNull(multipartRequest.getParameter("ui_type"));				// 지면의 UI 타입
		String description = GlobalCom.isNull(multipartRequest.getParameter("description"));		// 지면 설명
		String login_id = GlobalCom.isNull(multipartRequest.getParameter("smartUXManager"));		// 관리자 사이트에 로그인 한 사람의 로그인 id
		String page_type = GlobalCom.isNull(multipartRequest.getParameter("page_type"));			// 지면 타입
		String page_code = GlobalCom.isNull(multipartRequest.getParameter("page_code"));			// 지면 코드
		String category_gb = GlobalCom.isNull(multipartRequest.getParameter("category_gb"));		// 카테고리 구분
		String reps_album_id = GlobalCom.isNull(multipartRequest.getParameter("reps_album_id"));		// 지면의 대표컨텐츠 정보
		String reps_category_id = GlobalCom.isNull(multipartRequest.getParameter("reps_category_id"));	// 지면의 대표컨텐츠 정보
		String trailer_viewing_type = GlobalCom.isNull(multipartRequest.getParameter("trailer_viewing_type"));		// 예고편 노출타입
		String reps_trailer_viewing_type = GlobalCom.isNull(multipartRequest.getParameter("reps_trailer_viewing_type"));	// 대표컨텐츠 예고편 노출타입
		String location_code = GlobalCom.isNull(multipartRequest.getParameter("location_code"));	// 지역코드
		String location_yn = GlobalCom.isNull(multipartRequest.getParameter("location_yn"));		// 국내/해외구분
		String product_code = GlobalCom.isNull(multipartRequest.getParameter("product_code"));		// 월정액선택상품
		String product_code_not = GlobalCom.isNull(multipartRequest.getParameter("product_code2"));	// 월정액선택상품(비노출)
		String show_cnt = GlobalCom.isNull(multipartRequest.getParameter("show_cnt"));	// 월정액선택상품(비노출)
		String terminal_all_yn = GlobalCom.isNull(multipartRequest.getParameter("terminal_all_yn"));// 단말선택 전체여부
		String terminal_arr = GlobalCom.isNull(multipartRequest.getParameter("terminal_arr"));		// 단말선택
		
		
		
/* 2019.11.04 : 지면 UI 타입 추가 Start - 이태광 */
		String paper_ui_type = GlobalCom.isNull(multipartRequest.getParameter("paper_ui_type"));	// 지면 UI 타입
/* 2019.11.04 : 지면 UI 타입 추가 End - 이태광 */
		
		
		
		logger.debug("panel_id : " + panel_id);
		logger.debug("title_id : " + title_id);
		logger.debug("category_id : " + category_id);
		logger.debug("category_type : " + category_type);
		logger.debug("album_cnt : " + album_cnt);
		logger.debug("ui_type : " + ui_type);
		logger.debug("description : " + description);
		logger.debug("login_id : " + login_id);
		logger.debug("page_type : " + page_type);
		logger.debug("page_code : " + page_code);
		logger.debug("category_gb : " + category_gb);
		logger.debug("reps_album_id : " + reps_album_id);
		logger.debug("reps_category_id : " + reps_category_id);
		logger.debug("trailer_viewing_type : " + trailer_viewing_type);
		logger.debug("reps_trailer_viewing_type : " + reps_trailer_viewing_type);
		logger.debug("location_code : " + location_code);
		logger.debug("location_yn : " + location_yn);
/* 2019.11.04 : 지면 UI 타입 추가 Start - 이태광 */		
		logger.debug("paper_ui_type : " + paper_ui_type);
/* 2019.11.04 : 지면 UI 타입 추가 End - 이태광 */
		
		logger.debug("product_code : " + product_code);
		logger.debug("product_code_not : " + product_code_not);
		logger.debug("show_cnt : " + show_cnt);
		logger.debug("terminal_all_yn : " + terminal_all_yn);
		logger.debug("terminal_arr : " + terminal_arr);
		
		
		
		description = description.replace("\r", "");
		
		String update_id = login_id;			// 수정자 ID(차후에 로그인 한 사람의 로그인 ID로 바꿀것
		
		String resultcode = "";
		String resultmessage = "";
		String tmp_file_name = "";
		String tmp_file_name2 = "";
		String bg_img_file = "";
		String bg_img_file2 = "";
		File img_file = null;
		File img_file2 = null;
		try{
			// validateUpdateCategory(panel_id, title_id, category_id, category_type);						// 입력받은 파라미터 값들에 대한 validation 작업 진행
			
			// validation 작업에 통과하면 파일 업로드 작업을 진행한다
			//20180913 이미지 관련 등록 및 삭제는 하위지면 추가와 지면 상세조회에서만 등록 및 수정
			/*if(uploadfile.getSize() != 0L){
				tmp_file_name = uploadfile.getOriginalFilename();
				String ext = tmp_file_name.substring(tmp_file_name.lastIndexOf(".")+1,tmp_file_name.length()); // 확장자 구하기
				bg_img_file = "panel/" + panel_id + "_" + title_id + "_" + Long.toString(System.currentTimeMillis()) + "." + ext; // 파일 이름을 패널이미지 저장위치(panel/) 패널 id_지면 id_시스템타임.확장자 구조로 한다
				
				String realPath = request.getSession().getServletContext().getRealPath(SmartUXProperties.getProperty("imgupload.tmpdir"));
				// String newFilename = "C:\\SmartUX\\workspaces\\smartux\\src\\main\\webapp\\tmpimgdir\\" + bg_img_file;
				//String newFilename = realPath + "/" + bg_img_file;
				String newFilename = SmartUXProperties.getProperty("imgupload.dir") + "/" + bg_img_file;
				
				logger.debug("tmp_file_name : " + tmp_file_name);
				logger.debug("ext :  " + ext);
				logger.debug("bg_img_file :  " + bg_img_file);
				logger.debug("newFilename :  " + newFilename);
				logger.debug("realPath :  " + realPath);
				img_file = new File(newFilename);
				uploadfile.transferTo(img_file);
				
				
				// img_file을 FTP 로 보낼것
//				int servercnt = Integer.parseInt(SmartUXProperties.getProperty("imgserver.length"));
//				
//				for(int i=0; i < servercnt; i++){
//					String keyidx = Integer.toString(i+1);
//					String ip = SmartUXProperties.getProperty("imgserver" + keyidx + ".ip");
//					int port = Integer.parseInt(SmartUXProperties.getProperty("imgserver" + keyidx + ".port"));
//					String id = SmartUXProperties.getProperty("imgserver" + keyidx + ".userid");
//					String password = SmartUXProperties.getProperty("imgserver" + keyidx + ".password");
//					String directory = SmartUXProperties.getProperty("imgserver" + keyidx + ".approot") +  SmartUXProperties.getProperty("imgserver" + keyidx + ".imgpath");	
//							
//					GlobalCom.FTPUpload(ip, port, id, password, directory, img_file);
//				}
				
			}
			
			if(uploadfile2.getSize() != 0L){
				tmp_file_name2 = uploadfile2.getOriginalFilename();
				String ext = tmp_file_name2.substring(tmp_file_name2.lastIndexOf(".")+1,tmp_file_name2.length()); // 확장자 구하기
				bg_img_file2 = "panel/" + panel_id + "_" + title_id + "_" + Long.toString(System.currentTimeMillis()+1) + "." + ext; // 파일 이름을 패널이미지 저장위치(panel/) 패널 id_지면 id_시스템타임.확장자 구조로 한다
				
				String realPath = request.getSession().getServletContext().getRealPath(SmartUXProperties.getProperty("imgupload.tmpdir"));
				String newFilename2 = SmartUXProperties.getProperty("imgupload.dir") + "/" + bg_img_file2;
				
				logger.debug("tmp_file_name2 : " + tmp_file_name2);
				logger.debug("ext :  " + ext);
				logger.debug("bg_img_file2 :  " + bg_img_file2);
				logger.debug("newFilename2 :  " + newFilename2);
				logger.debug("realPath :  " + realPath);
				img_file2 = new File(newFilename2);
				uploadfile2.transferTo(img_file2);
				
			}*/
			
			service.updateCategory(panel_id, title_id, category_id, category_type, album_cnt, ui_type, description, update_id, page_type, page_code, category_gb, 
					reps_album_id, reps_category_id, trailer_viewing_type, reps_trailer_viewing_type, location_code, location_yn
/* 2019.11.04 : 지면 UI 타입 [paper_ui_type 변수 추가] 추가 Start - 이태광 */					
					, paper_ui_type, product_code, product_code_not, show_cnt, terminal_all_yn, terminal_arr);
/* 2019.11.04 : 지면 UI 타입 [paper_ui_type 변수 추가] 추가 End - 이태광 */
			resultcode = SmartUXProperties.getProperty("flag.success");
			resultmessage = SmartUXProperties.getProperty("message.success");
			
			
			//2021-04-22 BPAS 편성관리 추가
			try {
				imcsService.insertImcs("D", "PAP", panel_id+"|"+title_id, "", "", "");
				if(!"".equals(reps_album_id)) {
					imcsService.insertImcs("I", "PAP", panel_id+"|"+title_id, "ALB", reps_album_id, "N");
				}
				
				if(!"".equals(page_code)) {
					String category_id_t = "";
					if(page_code.indexOf(":") < 0) {
						category_id_t = page_code;
					}else {
						String[] category_id_t_arr = page_code.split("\\:"); 
						category_id_t = category_id_t_arr[category_id_t_arr.length-1];
					}
					
					imcsService.insertImcs("I", "PAP", panel_id+"|"+title_id, "CAT", category_id_t, "N");
				}
				
				if("CAT_MAP".equals(category_type) || "BOOKTV".equals(category_type) || "ENG_PRESCH".equals(category_type) || "BRAND".equals(category_type)) {
					if(!"".equals(category_id)) {
						String[] category_id_arr = category_id.split("\\|\\|");
						for(int i = 0 ; i < category_id_arr.length; i++) {
							if(!"".equals(category_id_arr[i])) {
								imcsService.insertImcs("I", "PAP", panel_id+"|"+title_id, "CAT", category_id_arr[i], "N");
							}
						}
					}
				}
			}catch (Exception e) {
				logger.error("[insertImcs]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			}
			
		}catch(Exception e){
			//logger.error("getTypeSelect "+e.getClass().getName());
			//logger.error("getTypeSelect "+e.getMessage());
			logger.error("[getTypeSelect]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			
			
			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}
		
		// return "{\"flag\" : \"" + resultcode + "\", \"message\" : \"" + resultmessage + "\"}";
		// 이 함수는 웹브라우저에서 jquery의 ajaxForm으로 호출을 하는데 
		// dataType을 json으로 설정할 경우 IE에서 json을 인식을 할 수 없어서
		// dataType을 text로 셋칭한 뒤에 javascript에서 split을 해서 파싱하도록 한다
		///return resultcode + "!@" + resultmessage;
		
		String result = "{\"result\":{\"flag\":\"" + resultcode + "\",\"message\":\"" + resultmessage + "\"}}";

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(result).toString(), responseHeaders, HttpStatus.CREATED);
	}
	
	/**
	 * updateCategory 함수에서 validation 작업을 하는 함수(updateCategory 함수를 더는 사용하지 않기 때문에 이 함수도 사용하지 않음)
	 * @param panel_id
	 * @param title_id
	 * @param category_id
	 * @param category_type
	 */
	private void validateUpdateCategory(String panel_id, String title_id, String category_id, String category_type){
		
		SmartUXException exception = new SmartUXException();
		
		if(!(StringUtils.hasText(panel_id))){
			exception.setFlag("NOT FOUND PANEL_ID");
			exception.setMessage("패널코드값은 필수로 들어가야 합니다");
			throw exception;
		}
		
		if(!(StringUtils.hasText(title_id))){
			exception.setFlag("NOT FOUND TITLE_ID");
			exception.setMessage("지면코드값은 필수로 들어가야 합니다");
			throw exception;
		}
		
		if(("LIVE").equals("category_type")){
			
		}else if(("VOD").equals("category_type")){
			if(!(StringUtils.hasText(category_id))){
				exception.setFlag("NOT FOUND VOD");
				exception.setMessage("VOD 랭킹을 선택해주세요");
				throw exception;
			}
		}else if(("CATEGORY_MAP").equals("category_type")){
			if(!(StringUtils.hasText(category_id))){
				exception.setFlag("NOT FOUND CAT_MAP");
				exception.setMessage("카테고리를 선택해주세요");
				throw exception;
			}
		}else if(("SCHEDULE").equals("category_type")){
			if(!(StringUtils.hasText(category_id))){
				exception.setFlag("NOT FOUND SCHEDULE");
				exception.setMessage("자체편성을 선택해주세요");
				throw exception;
			}
		}else if(("WISH").equals("category_type")){
			
		}
		
		
	}
	
	/**
	 * 카테고리 조회 화면(팝업용)
	 * 2019.11.12 IPTV 브릿지홈 개편 : isCategoryGbUse (카테고리 구분 선택 사용여부) 추가
	 * @param panel_id
	 * @param category_id
	 * @param category_gb
	 * @param isAds
	 * @param isImcsContents
	 * @param category_level
	 * @param isCategoryGbUse
	 * @return
	 */
	@RequestMapping(value="/admin/mainpanel/getPageCategoryList", method=RequestMethod.GET)
	public String getPageCategoryList(
			@RequestParam(value = "panel_id", required = false, defaultValue = "") String panel_id,
			@RequestParam(value="category_id") String category_id,
			@RequestParam(value="category_gb", required=false) String category_gb,
			@RequestParam(value="isAds", required=false, defaultValue = "N") String isAds,
			@RequestParam(value="isImcsContents", required=false, defaultValue = "N") String isImcsContents,
			@RequestParam(value="category_level", required=false, defaultValue = "") String category_level,
			@RequestParam(value="isCategoryGbUse", required=false, defaultValue = "N") String isCategoryGbUse
			, Model model) throws Exception{
		String hdtvPanelID = SmartUXProperties.getProperty("hdtv.panel.id");

		List<CategoryVO> result;

		if (GlobalCom.contains(hdtvPanelID, panel_id, "\\|")) {
			// 최상위 카테고리 정보 조회
			result = service.getCategoryList("NSC", category_id);
		} else {
			// 최상위 카테고리 정보 조회
			result = service.getCategoryList(GlobalCom.isNull(category_gb, "I20"), category_id);
		}

		model.addAttribute("categoryResult", result);
		model.addAttribute("category_gb", category_gb);
		model.addAttribute("isAds", isAds);
		model.addAttribute("category_level", category_level);
		model.addAttribute("isImcsContents", isImcsContents);
		model.addAttribute("isCategoryGbUse", isCategoryGbUse);
		return "/admin/mainpanel/getPageCategoryList";
	}
	
	
	/**
	 * 카테고리 조회 화면에서 카테고리를 조회하는 함수(팝업)
	 * @param category_id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/mainpanel/getPageCategoryList", method=RequestMethod.POST)
	public ResponseEntity<String> getPageCategoryListJson(
			@RequestParam(value = "panel_id", required = false, defaultValue = "") String panel_id,
			@RequestParam(value="category_id") String category_id,
			@RequestParam(value="category_gb") String category_gb
			) throws Exception{
		
		HTMLCleaner cleaner = new HTMLCleaner();
		category_id 	= cleaner.clean(category_id);

		String hdtvPanelID = SmartUXProperties.getProperty("hdtv.panel.id");

		List<CategoryVO> result;

		if (GlobalCom.contains(hdtvPanelID, panel_id, "\\|")) {
			// 최상위 카테고리 정보 조회
			result = service.getCategoryList("NSC", category_id);
		} else {
			// 최상위 카테고리 정보 조회
			result = service.getCategoryList(category_gb, category_id);
		}
		
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(result).toString(), responseHeaders, HttpStatus.CREATED);
	}
	
	@RequestMapping(value="/admin/mainpanel/showImgPopup", method=RequestMethod.GET)
	public String getShowImgPopup(
			@RequestParam(value="src") String src
			, Model model) throws Exception{
		model.addAttribute("src", src);
		return "/admin/mainpanel/showImgPopup";
	}
	
	
	/**
	 * Contingency Mode 설정 화면 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/mainpanel/getContingency", method=RequestMethod.GET)
	public String getVersionLock(
			Model model
			) throws Exception{
		
		String contingency_type = "";
		String version = service.getPanelVersion();
		if(version.indexOf("-") == -1){	//양수면 Normal Mode
			contingency_type = "N";
			version = "";
		}else{	//음수면 Contingency Mode
			contingency_type = "Y";
		}
		
		model.addAttribute("contingency_type", contingency_type);
		version = version.replaceAll("-", "");
		model.addAttribute("contingency_time", version);
		
		return "/admin/mainpanel/contingency";
	}
	
	/**
	 * Contingency Mode 설정 화면 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/mainpanel/setContingency", method=RequestMethod.POST)
	public String setVersionLock(
			HttpServletRequest request,
			HttpServletResponse response,
			 @RequestParam(value="contingency_type") String type
			,@RequestParam(value="contingency_time") String time
			,@RequestParam(value="user_id") String user_id
			, Model model) throws Exception{
		
		if(type.equals("N")){
			time = GlobalCom.getTodayFormat4_24();
		}else{
			time = "-"+time;
		}

		String result = service.updatePanelVersion(type,time,user_id);
		
		applyPanelTitleSchedule(request, response, "A");
		
		//화면 조회
		String contingency_type = "";
		String version = service.getPanelVersion();
		if(version.indexOf("-") == -1){	//양수면 Normal Mode
			contingency_type = "N";
			version = "";
		}else{	//음수면 Contingency Mode
			contingency_type = "Y";
		}
		
		model.addAttribute("contingency_type", contingency_type);
		version = version.replaceAll("-", "");
		model.addAttribute("contingency_time", version);
		model.addAttribute("validate", result);
		
		//response.setStatus(201);
		
		return "/admin/mainpanel/contingency";
	}
	
	/**
	 * 패널UI타입 목록 조회 화면
	 * @param model		Model 객체
	 * @return			패널UI타입 목록 조회 화면 URL
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/mainpanel/getPanelUiTypeList", method=RequestMethod.GET)
	public String getPanelUiType(
			Model model,
			@RequestParam(value="frame_type", defaultValue="30") String frame_type,
			@RequestParam(value="pageNum", required=false, defaultValue="1") String pageNum
			) throws Exception{
		
		HTMLCleaner cleaner = new HTMLCleaner();
		frame_type 	= cleaner.clean(frame_type);
		pageNum 	= cleaner.clean(pageNum);
		pageNum 	= GlobalCom.isNull(pageNum, "1");
				
		List<FrameVO> result = new ArrayList<FrameVO>();
		String pageCount = "0";
		String filePath = service.getImageServerURL("I") + SmartUXProperties.getProperty("img.paneluitype.web");
		
		List<FrameVO> dbResult = service.getPanelUiTypeList(frame_type, Integer.parseInt(pageNum), 10);
		
		for(FrameVO frameVO: dbResult) result.add(frameVO);
		pageCount = Integer.toString(service.getPanelUiTypeCnt(frame_type));

		model.addAttribute("dir", filePath);
		model.addAttribute("result", result);
		model.addAttribute("panel_type", frame_type);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", "10");
		model.addAttribute("blockSize", "10");
		model.addAttribute("pageCount", pageCount);
		
		return "/admin/mainpanel/getPanelUiTypeList";
	}
	
	/**
	 * 패널UI타입 등록 화면
	 * @param model		Model 객체
	 * @return			패널UI타입 등록 화면 URL
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/mainpanel/insertPanelUiType", method=RequestMethod.GET)
	public String insertPanelUiType(
			Model model,
			@RequestParam(value="frame_type", defaultValue="30") String frame_type
			) throws Exception{
		
		HTMLCleaner cleaner = new HTMLCleaner();
		frame_type 	= cleaner.clean(frame_type);

		model.addAttribute("frame_type", frame_type);
		
		return "/admin/mainpanel/insertPanelUiType";
	}
	
	/**
	 * 패널UI타입 등록 처리 작업
	 * @param frame_type			Panel ui type
	 * @param frame_nm				Panel ui type 이름
	 * @param img_file				Panel ui type 이미지
	 * @param use_yn				Panel ui type의 사용여부(Y/N)
	 * @param login_id				관리자 사이트에 로그인 한 사람의 로그인 id
	 * @return						처리 결과를 기록한 json 문자열
	 */
	@RequestMapping(value="/admin/mainpanel/insertPanelUiType", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> insertPanelUiType(
			@RequestParam(value="frame_type") String frame_type,
			@RequestParam(value="frame_nm") String frame_nm,
			@RequestParam(value="img_file", required=false) MultipartFile img_file,
			@RequestParam(value="use_yn") String use_yn,
			@RequestParam(value="smartUXManager") String login_id) {
		
		HTMLCleaner cleaner		= new HTMLCleaner();
		frame_type 		= cleaner.clean(frame_type).trim();
		frame_nm 		= cleaner.clean(frame_nm).trim();
		use_yn 			= cleaner.clean(use_yn).trim();
		login_id 		= cleaner.clean(login_id).trim();
		
		File fileName 			= null;
		String tmp_file_name 	= "";		//실제 파일 명, upload용 파일명 구성에 사용
		String bg_img_file 		= "";		//upload 용 파일 명
		String frame_flag		= "";		//frame_type_code(pk) 생성을 위한 구분자.
		
		String resultcode 		= "";
		String resultmessage 	= "";
		Long fileMaxSize		= 0L;
		ExceptionHandler handler = null;
		
		try{
			//validateInsertPanelTitleTemp(panel_id, title_nm, use_yn);					// 입력받은 파라미터 값들에 대한 validation 작업 진행
			
			File dir = new File(SmartUXProperties.getProperty("imgupload.paneluitype.dir"));
			if (!dir.exists()) {
				dir.mkdirs();
			}
			
			if(img_file != null && img_file.getSize() != 0L){
				tmp_file_name = img_file.getOriginalFilename();
				String ext = tmp_file_name.substring(tmp_file_name.lastIndexOf(".")+1,tmp_file_name.length()); // 확장자 구하기
				bg_img_file = randomStr() + "_" + Long.toString(System.currentTimeMillis()) + "_pn." + ext; // 파일 이름을 패널 id_지면 id_시스템타임.확장자 구조로 한다
				
				String newFilename = SmartUXProperties.getProperty("imgupload.paneluitype.dir") + bg_img_file;
				
				logger.debug("tmp_file_name : " + tmp_file_name);
				logger.debug("ext :  " + ext);
				logger.debug("title_bg_img_file :  " + bg_img_file);
				logger.debug("newFilename :  " + newFilename);
				fileName = new File(newFilename);
				img_file.transferTo(fileName);
			}
			
			frame_flag = transPanelUiTypeFlag(frame_type);		//frame_type_code(pk) 발급을 위한 구분자 생성.
			
			if(!(frame_flag.equals(""))) {
				service.insertPanelUiType(frame_flag, frame_type, frame_nm, bg_img_file, use_yn, login_id); // insert
				resultcode = SmartUXProperties.getProperty("flag.success");
				resultmessage = SmartUXProperties.getProperty("message.success");
			} else {
				if(fileName.exists()){
					fileName.delete();
				}
				
				SmartUXException e = new SmartUXException();
				e.setFlag("PANEL UI TYPE ERR");
				e.setMessage("panel_ui_type 이상");
				throw e;
			}
		}catch(NullPointerException ne){
			logger.error("PanelViewController insertPanelUiType 1: " + ne + " | " + System.currentTimeMillis());
			handler = new ExceptionHandler(ne);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}catch(Exception e){
			logger.error("PanelViewController insertPanelUiType 2: " + e + " | " + System.currentTimeMillis());
			handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}
		
		HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
        return new ResponseEntity<String>("{\"flag\" : \"" + resultcode + "\", \"message\" : \"" + resultmessage + "\"}", responseHeaders, HttpStatus.CREATED);
	}
	
	/**
	 * frame_type_code(pk) 발급을 위한 구분자 생성
	 * @param frame_type	30: 패널UI타입
	 * @return
	 * @throws Exception
	 */
	private String transPanelUiTypeFlag(String frame_type) throws Exception{
		String result 	= "";
		int tmp			= 0;
		try{
			tmp = Integer.parseInt(frame_type);
			switch(tmp){
				//case 10: result = "J";	break;
				//case 20: result = "F";	break;
				case 30: result = "P";  break;
				default: result=""; 	break; 
			}
			
		}catch(Exception e){
			logger.warn("PanelViewController transPanelUiTypeFlag: " + e + " | " + System.currentTimeMillis());
			throw e;
		}
		return result;
	}
	
	/**
	 * panelUiType 삭제처리(다중) - update(use_yn)
	 * @param frameVo			get/setter 
	 * @param frame_type_code	code(pk)
	 * @param img_file			이미지 파일명
	 * @param login_id			요청ID
	 * @return
	 */
	@RequestMapping(value="/admin/mainpanel/deletePanelUiType", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> deletePanelUiType(
			@RequestParam(value="frame_type_code[]") String[] frame_type_code,
			@RequestParam(value="smartUXManager") String login_id){
		StringBuffer sb = null;
		String resultcode = "";
		String resultmessage = "";
		
		HTMLCleaner cleaner		= new HTMLCleaner();
		login_id 				= cleaner.clean(login_id).trim();
		
		try{
			validateDeletePanelUiType(frame_type_code);
			
			if(frame_type_code.length > 0) {
				sb = service.deletePanelUiType(frame_type_code, login_id);
				
				if(sb.length() == 0) {
					resultcode = SmartUXProperties.getProperty("flag.success");
					resultmessage = SmartUXProperties.getProperty("message.success");
				} else {
					resultcode = SmartUXProperties.getProperty("flag.can_not_delete");
					resultmessage = sb.toString()+" deletePanelUiType fail";
				}				
			} else {
				resultcode = SmartUXProperties.getProperty("flag.fail");				
				resultmessage = frame_type_code.length+ " deletePanelUiType fail ";
				resultmessage += SmartUXProperties.getProperty("message.fail");
			}
		}catch(Exception e){
			logger.info("PanelViewController deletePanelUiType: " + e + " | " + System.currentTimeMillis());
			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}
		
		HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
        return new ResponseEntity<String>("{\"flag\" : \"" + resultcode + "\", \"message\" : \"" + resultmessage + "\"}", responseHeaders, HttpStatus.CREATED);
	}
	
	/**
	 * validateDeletePanelUiType 함수에 대한 validation 작업 함수
	 * @param frame_type_code			삭제하고자 하는 frame_type_code 값들 배열	
	 */
	private void validateDeletePanelUiType(String [] frame_type_code) throws SmartUXException{
		SmartUXException exception = new SmartUXException();
		
		if((frame_type_code == null) ||(frame_type_code.length == 0)){	
			exception.setFlag("NOT FOUND PANEL_UI_CODE");
			exception.setMessage("패널코드값은 필수로 들어가야 합니다");
			throw exception;
		}
	}
	
	/**
	 * update (파일 업로드 있을때 없을때를 구분)
	 * @param frameVo
	 * @param frame_nm
	 * @param img_file
	 * @param old_file
	 * @param use_yn
	 * @param frame_type_code
	 * @param login_id
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/admin/mainpanel/updatePanelUiType", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> updatePanelUiType(
			@RequestParam(value="frame_nm") String frame_nm,
			@RequestParam(value="img_file", required=false) MultipartFile img_file,
			@RequestParam(value="old_file") String old_file,
			@RequestParam(value="use_yn") String use_yn,
			@RequestParam(value="frame_type_code") String frame_type_code,
			@RequestParam(value="smartUXManager") String login_id, 
			HttpServletRequest request){
		
		HTMLCleaner cleaner		= new HTMLCleaner();
		frame_nm 				= cleaner.clean(frame_nm);
		use_yn 					= cleaner.clean(use_yn);
		login_id 				= cleaner.clean(login_id);
		frame_type_code 		= cleaner.clean(frame_type_code);
		old_file				= cleaner.clean(old_file);
		
		File fileName 			= null;
		String tmp_file_name 	= "";		//실제 파일 명, upload용 파일명 구성에 사용
		String bg_img_file 		= "";		//upload 용 파일 명
		String newFilename		= "";
		String resultcode 		= "";
		String resultmessage 	= "";
		Long fileMaxSize		= 0L;
		int result				= 0;
		
		try{
			
			if(img_file != null && img_file.getSize() != 0L) {	 // 파일 수정이 이뤄진 경우.
				tmp_file_name = img_file.getOriginalFilename();
				String ext = tmp_file_name.substring(tmp_file_name.lastIndexOf(".")+1,tmp_file_name.length()); // 확장자 구하기
				bg_img_file = randomStr() + "_" + Long.toString(System.currentTimeMillis()) + "_pn." + ext; // 파일 이름을 패널 id_지면 id_시스템타임.확장자 구조로 한다
				
				newFilename = SmartUXProperties.getProperty("imgupload.paneluitype.dir") + bg_img_file;
				
				logger.debug("tmp_file_name : " + tmp_file_name);
				logger.debug("ext :  " + ext);
				logger.debug("title_bg_img_file :  " + bg_img_file);
				logger.debug("newFilename :  " + newFilename);
				fileName = new File(newFilename);
				img_file.transferTo(fileName);
			
				result = service.updatePanelUiType(frame_type_code, frame_nm, bg_img_file, use_yn, login_id); // insert
				if(result == 1) {
					newFilename = SmartUXProperties.getProperty("imgupload.paneluitype.dir") + old_file;
					fileName = new File(newFilename);
					if(fileName.exists()){
						fileName.delete();
					}
				}
			} else {	// 파일 수정이 없는 경우
				result = service.updatePanelUiType(frame_type_code, frame_nm, "", use_yn, login_id);
			}
			
			if(result == 1) {
				resultcode = SmartUXProperties.getProperty("flag.success");
				resultmessage = SmartUXProperties.getProperty("message.success");
			} else {
				resultcode = SmartUXProperties.getProperty("flag.fail");
				resultmessage =  "updatePanelUiType fail";
			}
		}catch(Exception e){
			logger.info("PanelViewController updatePanelUiType: " + e + " | " + System.currentTimeMillis());
			ExceptionHandler handler = new ExceptionHandler(e);			
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}
		
		HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
        return new ResponseEntity<String>("{\"flag\" : \"" + resultcode + "\", \"message\" : \"" + resultmessage + "\"}", responseHeaders, HttpStatus.CREATED);
	}
	
	/*
	 * 랜덤 6자리 생성(문자+숫자) 
	 */
	public String randomStr(){
		StringBuffer sb = new StringBuffer();
		Random random	= new Random();
		String[] szTmp	= "z,x,c,v,b,n,m,a,s".split(",");
		int nTmp 		= 0;
		
		for(int i = 0 ; i < 3 ; i++){
			nTmp = random.nextInt(szTmp.length);
			sb.append(szTmp[nTmp]);
			sb.append(Integer.toString(nTmp+1));
		}
		
		return sb.toString();
	}
	
	/**
	 * 수정을 위한 단일 조회
	 * @param model
	 * @param frame_type_code
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/mainpanel/viewPanelUiType")
	public String getViewPanelUiType(Model model, 
				@RequestParam(value="frame_type_code") String frame_type_code
				)throws Exception{
		HTMLCleaner cleaner = new HTMLCleaner();
		//panel_id 	= cleaner.clean(panel_id);
		frame_type_code = cleaner.clean(frame_type_code).trim();

		String filePath = service.getImageServerURL("I") + SmartUXProperties.getProperty("img.paneluitype.web");
		
		FrameVO result = service.viewPanelUiType(frame_type_code);
		
		model.addAttribute("dir", filePath);
		model.addAttribute("result", result);
		
		return "/admin/mainpanel/viewPanelUiType";
	}
	
	/**
	 * 국가/도시 조회
	 * @param location_yn		//국내/해외 구분(국내: Y, 해외: N)
	 * @param step				//지역단계(step1: 가장 최상위 지역)
	 * @param code				//지역코드
	 * @return
	 * @throws Exception
	 */
	private List<LocationCodeVO> getLocationCode(String location_yn, String step, String code)throws Exception{	
		
		List<LocationCodeVO> result = new ArrayList<LocationCodeVO>();
		LocationCodeVO locationCodeVO = new LocationCodeVO();
		
		Set<String> useRuleSet = new HashSet<String>();
		
		FileReader file = null;
		
		boolean fileEmpty = false;
		
		try{
			String filePath = SmartUXProperties.getProperty("panel.location.dir");
			String fileName = filePath + "local_location_info_json.dat";
			
			if(location_yn.equals("Y")){
				fileName = filePath + "local_location_info_json.dat";
			}else{
				fileName = filePath + "global_location_info_json.dat";
			}
			
			JSONParser parser = new JSONParser();
			JSONObject jsonObj = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			
			file = new FileReader(fileName);
			jsonArray = (JSONArray) parser.parse(file);
			
			if((fileEmpty != true) && (jsonObj != null)){
				Iterator<Object> iterator = jsonArray.iterator();
				result.clear();
				useRuleSet.clear();
				
				while(iterator.hasNext()){
					JSONObject tmpObj = (JSONObject)iterator.next();
					locationCodeVO = new LocationCodeVO();
					
					if(step.equals("step1")){
						if(!useRuleSet.contains(tmpObj.get("step1_code").toString())){
							locationCodeVO.setStep("step1");
							locationCodeVO.setStep1_code(tmpObj.get("step1_code").toString());
							locationCodeVO.setStep1_name(tmpObj.get("step1_name").toString());
							
							result.add(locationCodeVO);
							useRuleSet.add(tmpObj.get("step1_code").toString());
						}
					}else if(step.equals("step2")){
						if(code.equals(tmpObj.get("step1_code").toString())){				
							if(!useRuleSet.contains(tmpObj.get("step2_code").toString())){
								locationCodeVO.setStep("step2");
								locationCodeVO.setStep2_code(tmpObj.get("step2_code").toString());
								locationCodeVO.setStep2_name(tmpObj.get("step2_name").toString());
								
								result.add(locationCodeVO);
								useRuleSet.add(tmpObj.get("step2_code").toString());
							}
						}
					}else if(step.equals("step3")){
						if(code.equals(tmpObj.get("step2_code").toString())){				
							locationCodeVO.setStep("step3");
							locationCodeVO.setStep3_code(tmpObj.get("step3_code").toString());
							locationCodeVO.setStep3_name(tmpObj.get("step3_name").toString());
							
							result.add(locationCodeVO);
						}
					}
				}
			}
			
			if((fileEmpty == true) || (jsonObj == null)){
				result = null;
			}
		}catch(Exception e){
			logger.info("getLocationCode:"+e.getClass().getName());
			logger.info("getLocationCode:"+e.getMessage());
			fileEmpty = true;
		}finally{
			if(file != null){
				file.close();
			}
		}
		
		return result;
	}
	
	@RequestMapping(value="/admin/mainpanel/getPanelUiTypeListPop", method=RequestMethod.GET)
	public String getPanelUiTypeListPop(
			Model model,
			@RequestParam(value="frame_type", defaultValue="30") String frame_type
			) throws Exception{
		
		HTMLCleaner cleaner = new HTMLCleaner();
		frame_type 	= cleaner.clean(frame_type);
	
		String type_nm = "";
				
		List<FrameVO> result = new ArrayList<FrameVO>();
		String filePath = service.getImageServerURL("I") + SmartUXProperties.getProperty("img.paneluitype.web");
		
		List<FrameVO> dbResult = service.getPanelUiTypeSelect(frame_type);
		
		for(FrameVO frameVO: dbResult) result.add(frameVO);
		
		if(frame_type.equals("30")){
        	type_nm="패널UI타입";
        }else{
        	type_nm="";
        }
		
		String img_path_url = filePath;
		
		model.addAttribute("type_nm", type_nm);       
        model.addAttribute("frame_type", frame_type);     
        model.addAttribute("img_path_url", img_path_url);     
        model.addAttribute("itemList", dbResult);
		
		return "/admin/mainpanel/getPanelUiTypeListPop";
	}
	
	@RequestMapping(value="/admin/mainpanel/getLinkInfoPop", method=RequestMethod.GET)
	public String getLinkInfoPop(
			Model model,
			@RequestParam(value="linkType", defaultValue="OTT") String link_type
			) throws Exception{
		
		link_type = link_type.replace("REC_", "");
		
		model.addAttribute("link_type", link_type);
		
		List<CategoryVO> category_list = service.getOtCategorySelect1();
		
		model.addAttribute("category_list", category_list);
		
		return "/admin/mainpanel/getLinkInfoPop";
	}
	
	/**
	 * 지역 코드 조회
	 * @param location_yn		국내/해외 구분(Y: 국내, N: 해외)
	 * @param step			단계
	 * @param code			parent code
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/mainpanel/getLinkInfoPop", method=RequestMethod.POST)
	public ResponseEntity<String> getLinkInfoPopPost(
			@RequestParam(value="sel_value", defaultValue="") String sel_value,
			@RequestParam(value="link_type", defaultValue="") String link_type
			) throws Exception{
		
		link_type = link_type.replace("REC_", "");
		
		LinkInfoVO param = new LinkInfoVO();
		param.setMims_link_type("P");
		param.setPairing_type(link_type);
		param.setParent_ott_cat_id(sel_value);
		
		List<CategoryVO> category_list = service.getOtCategorySelect2(param);
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(category_list).toString(), responseHeaders, HttpStatus.CREATED);
	}
	
	@RequestMapping(value="/admin/mainpanel/getLocationListPop", method=RequestMethod.GET)
	public String getLocationListPop(
			Model model,
			@RequestParam(value="location_yn", defaultValue="Y") String location_yn
			) throws Exception{
		
		HTMLCleaner cleaner = new HTMLCleaner();
		location_yn 	= cleaner.clean(location_yn);
	
		List<LocationCodeVO> locationCode = new ArrayList<LocationCodeVO>();
		locationCode = getLocationCode(location_yn, "step1", "");
		
		model.addAttribute("locationCode", locationCode);
		
		return "/admin/mainpanel/getLocationListPop";
	}
	
	/**
	 * 지역 코드 조회
	 * @param location_yn		국내/해외 구분(Y: 국내, N: 해외)
	 * @param step			단계
	 * @param code			parent code
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/mainpanel/getLocationListPop", method=RequestMethod.POST)
	public ResponseEntity<String> getLocationListPop(
			@RequestParam(value="location_yn", defaultValue="Y") String location_yn,
			@RequestParam(value="step", defaultValue="") String step,
			@RequestParam(value="code", defaultValue="") String code
			) throws Exception{
		
		HTMLCleaner cleaner = new HTMLCleaner();
		location_yn 	= cleaner.clean(location_yn);
		step 	= cleaner.clean(step);
		code 	= cleaner.clean(code);
		
		List<LocationCodeVO> result = new ArrayList<LocationCodeVO>();
		result = getLocationCode(location_yn, step, code);
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(result).toString(), responseHeaders, HttpStatus.CREATED);
	}
	
	/**
     * 파일 사이즈 제한
     * 1 : 지면 아미지, 2 : 지면 아이콘1, 3 : 지면 아이콘2
     * @param text_file
     * @param type
     */
	private void checkFileSize(MultipartFile text_file, int type) {
		String fileMaxSize = SmartUXProperties.getProperty("panel.paper.bg.imagefile.size");
		String imgName = "지면 이미지";
		if(2==type) {
			fileMaxSize = SmartUXProperties.getProperty("panel.paper.icon1.imagefile.size");
			imgName = "지면 아이콘1";
		}else if(3==type) {
			fileMaxSize = SmartUXProperties.getProperty("panel.paper.icon2.imagefile.size");
			imgName = "지면 아이콘2";
		}else if(4==type) {
			fileMaxSize = SmartUXProperties.getProperty("panel.paper.icon3.imagefile.size");
			imgName = "지면 아이콘3";
		}
		
		
		if(null != fileMaxSize) {
			long iFileMaxSize = Long.parseLong(fileMaxSize);
			if(text_file.getSize() > iFileMaxSize) throw new SmartUXException(SmartUXProperties.getProperty("flag.file.maxsize.over")
					, SmartUXProperties.getProperty("message.file.maxsize.over",imgName + " : " + fileMaxSize + "Byte 미만"));
		}
	}
	
	/**
	 * 말풍선 목록
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/mainpanel/getBubbleList",method=RequestMethod.GET)
	public String  getNotiList(
			@RequestParam(value="pageNum", required=false, defaultValue="1") String pageNum,
			Model model
			) throws Exception{	
		
		HTMLCleaner cleaner = new HTMLCleaner();
		pageNum = cleaner.clean(pageNum);
		
		logger.debug("getBubbleList = pageNum : " + pageNum);
		
		BubbleSearch vo = new BubbleSearch();
		
		
		//페이지 
		vo.setPageSize(GlobalCom.isNumber(vo.getPageSize(),10));
		vo.setBlockSize(GlobalCom.isNumber(vo.getBlockSize(),10));
		vo.setPageNum(GlobalCom.isNumber(Integer.parseInt(GlobalCom.isNull(pageNum, "1")),1));
		//검색 
		
		List<BubbleList> list = service.getBubbleList(vo);
		vo.setPageCount(service.getBubbleListCnt(vo));
		
		model.addAttribute("list", list);
		model.addAttribute("vo", vo);	
		
		return "/admin/mainpanel/bubbleList";		
	}
	
	/**
	 * 말풍선 삭제
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/mainpanel/deleteBubble", method=RequestMethod.POST)
	public ResponseEntity<String> deleteBubble(@RequestParam(value="delList", required=false) String delList
			, HttpServletRequest request
			, Model model) throws Exception {
		
		String resCode = "";
		String resMessage = "";
		
		try{
			service.deleteBubble(delList);
			
			resCode = SmartUXProperties.getProperty("flag.success");
			resMessage = SmartUXProperties.getProperty("message.success");
			
		} catch(Exception e) {
			logger.error("[deleteBubble]["+e.getClass().getName()+"]["+e.getMessage()+"]");	
			ExceptionHandler handler = new ExceptionHandler(e);
			resCode 	= handler.getFlag();
			resMessage 	= handler.getMessage();
		}
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>("{\"flag\" : \"" + resCode + "\", \"message\" : \"" + resMessage + "\"}", responseHeaders, HttpStatus.CREATED);
	}
	
	/**
	 * 말풍선 등록 페이지
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/mainpanel/insertBubble", method=RequestMethod.GET)
	public String insertBubble(@RequestParam(value="scr_tp" , required=false, defaultValue="T") String scr_tp,
			Model model) throws Exception {
		
		/*int imgSize = Integer.parseInt(GlobalCom.isNull(GlobalCom.getProperties(HDTVProperties.getProperty("filepath.hdtv.common"), "eventnoti.size"),""))/1024;
		String imgFormat = GlobalCom.isNull(GlobalCom.getProperties(HDTVProperties.getProperty("filepath.hdtv.common"), "eventnoti.format"),"");
		
		model.addAttribute("imgSize", imgSize);
		model.addAttribute("imgFormat", imgFormat);*/
		model.addAttribute("type", "insert");
		model.addAttribute("scr_tp", scr_tp);
		
		
		
		return "/admin/mainpanel/insertBubble";
	}
	
	/**
	 * 말풍선 등록 ajax
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/mainpanel/insertBubble", method=RequestMethod.POST)
	public ResponseEntity<String> insertBubble(BubbleInsert bubbleInsert
			, @RequestParam(value="image_url", required=false) MultipartFile image_url
			, HttpServletRequest request
			, Model model
			) throws Exception{

		String resCode = "";
		String resMessage = "";
		String tmp_file_name = "";
		String bubble_image = "";
		File img_file = null;

		try {
			String filepath = SmartUXProperties.getProperty("imgupload.dir")+"bubble/";
			File dirChk = new File(filepath);
			if(!dirChk.exists()) {
				dirChk.mkdirs();
			}

			if (image_url != null && image_url.getSize() != 0L) {
				tmp_file_name = image_url.getOriginalFilename();
				String ext = tmp_file_name.substring(tmp_file_name.lastIndexOf(".")+1,tmp_file_name.length()); // 확장자 구하기
				bubble_image = Long.toString(System.currentTimeMillis()) + "." + ext; // 파일 이름을 패널이미지 저장위치(panel/)패널 id_시스템타임.확장자 구조로 한다
				
				String newFilename = filepath + bubble_image;
				
				img_file = new File(newFilename);
				image_url.transferTo(img_file);
			}
			bubbleInsert.setImg(bubble_image);
			
			service.insertBubble(bubbleInsert);
			
			resCode = SmartUXProperties.getProperty("flag.success");
			resMessage = SmartUXProperties.getProperty("message.success");
		} catch(Exception e) {
			logger.error("[insertBubble]["+e.getClass().getName()+"]["+e.getMessage()+"]");	
			ExceptionHandler handler = new ExceptionHandler(e);
			resCode = handler.getFlag();
			resMessage = handler.getMessage();
		}
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>("{\"flag\" : \"" + resCode + "\", \"message\" : \"" + resMessage + "\"}", responseHeaders, HttpStatus.CREATED);
	}
	
	/**
	 * 지면&패널 조회
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/mainpanel/getPanelTitleListPop", method=RequestMethod.GET)
	public String getPanelTitleListPop(Model model) throws Exception {
		
		// 현재 DB에 등록되어 있는 패널을 읽어온다
		List<PanelVO> panel = service.getPanelList();
		
		// 지면전체조회
		List<ViewVO> title = service.getPanelTitleTempList();
		
		model.addAttribute("panel", panel);
		model.addAttribute("title", title);
		
		return "/admin/mainpanel/getPanelTitleListPop";
	}
	
	
	/**
	 * 지면조회
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonGenerationException 
	 */
	@RequestMapping(value="/admin/mainpanel/getTitleList", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> getTitleList(@RequestParam(value="pannel_id") String pannel_id
			) throws JsonGenerationException, JsonMappingException, IOException {
		List<ViewVO> title = new ArrayList<ViewVO>();
		try{
			title = service.getPanelTitleTempList(pannel_id);
			for(int i = 0 ; i < title.size(); i++) {
				title.get(i).setTitle_nm(URLEncoder.encode(title.get(i).getTitle_nm(), "UTF-8"));
			}
		}catch(Exception e){
			ExceptionHandler handler = new ExceptionHandler(e);
		}
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		return new ResponseEntity<String>(om.writeValueAsString(title), responseHeaders, HttpStatus.CREATED);
	}
	
	
	
	/**
	 * 말풍선 수정 페이지
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/mainpanel/updateBubble", method=RequestMethod.GET)
	public String updateBubble(@RequestParam(value="reg_no", required=false, defaultValue="") String reg_no,
			@RequestParam(value="scr_tp" , required=false, defaultValue="T") String scr_tp,
			Model model) throws Exception {

		BubbleInsert vo = service.getBubbleDetail(reg_no);
		if(!"".equals(vo.getContent()) && vo.getContent() != null) {
			vo.setContent(vo.getContent().replaceAll("\n", "\\r\\n"));
		}
		
		model.addAttribute("type", "update");
		
		model.addAttribute("bubble", vo);
		
		model.addAttribute("reg_no", reg_no);
		model.addAttribute("scr_tp", scr_tp);
		
		return "/admin/mainpanel/insertBubble";
	}
	
	@RequestMapping(value = "/admin/mainpanel/getBubbleDetail", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> getBubbleDetail(@RequestParam(value = "reg_no") String reg_no, HttpServletRequest request) throws Exception {
		
		BubbleInsert vo = new BubbleInsert();
        try {
        	vo = service.getBubbleDetail(reg_no);
    		if(!"".equals(vo.getContent()) && vo.getContent() != null) {
    			vo.setContent(vo.getContent().replaceAll("\n", "\\r\\n"));
    			
    			vo.setContent(URLEncoder.encode(vo.getContent(), "UTF-8"));
    			vo.setTitle(URLEncoder.encode(vo.getTitle(), "UTF-8"));
    		}

    		List<String> terminal_list = service.getBubbleTerminal(reg_no);
    		String terminal_str = "";
    		for(String terminal : terminal_list) {
    			if("".equals(terminal_str)) {
    				terminal_str = terminal;
    			}else {
    				terminal_str += ","+terminal;
    			}
    		}
    		vo.setTerminal_arr(terminal_str);
    		
        } catch (Exception e) {
            ExceptionHandler handler = new ExceptionHandler(e);
        }

        
        HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		return new ResponseEntity<String>(om.writeValueAsString(vo), responseHeaders, HttpStatus.OK);
    }
	
	/**
	 * 말풍선 수정 ajax
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/mainpanel/updateBubble", method=RequestMethod.POST)
	public ResponseEntity<String> updateBubble(BubbleInsert bubbleInsert
			, @RequestParam(value="image_url", required=false) MultipartFile image_url
			, HttpServletRequest request
			, Model model) throws Exception{

		String resCode = "";
		String resMessage = "";
		String tmp_file_name = "";
		String bubble_image = "";
		File img_file = null;

		try {
			String filepath = SmartUXProperties.getProperty("imgupload.dir")+"bubble/";
			if (image_url != null && image_url.getSize() != 0L) {
				tmp_file_name = image_url.getOriginalFilename();
				String ext = tmp_file_name.substring(tmp_file_name.lastIndexOf(".")+1,tmp_file_name.length()); // 확장자 구하기
				bubble_image = Long.toString(System.currentTimeMillis()) + "." + ext; // 파일 이름을 패널이미지 저장위치(panel/)패널 id_시스템타임.확장자 구조로 한다
				
				String newFilename = filepath + bubble_image;
				
				img_file = new File(newFilename);
				image_url.transferTo(img_file);
			} else {
				bubble_image = bubbleInsert.getOrg_image_url();
				if("true".equals(bubbleInsert.getImageDelete())) {
					bubble_image = "";
				}
			}
			bubbleInsert.setImg(bubble_image);
			
			bubbleInsert.setContent(HTMLCleaner.clean(bubbleInsert.getContent()));
			
			service.updateBubble(bubbleInsert);
			
			resCode = SmartUXProperties.getProperty("flag.success");
			resMessage = SmartUXProperties.getProperty("message.success");
		} catch(Exception e) {
			logger.error("[updateBubble]["+e.getClass().getName()+"]["+e.getMessage()+"]");	
			ExceptionHandler handler = new ExceptionHandler(e);
			resCode = handler.getFlag();
			resMessage = handler.getMessage();
		}
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>("{\"flag\" : \"" + resCode + "\", \"message\" : \"" + resMessage + "\"}", responseHeaders, HttpStatus.CREATED);
	}
	
	/**
     * 말풍선 즉시적용
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/admin/mainpanel/bubbleApplyCache", method=RequestMethod.POST)
	public ResponseEntity<String> bubbleApplyCache(
			HttpServletRequest request, 
			HttpServletResponse response
			) throws Exception {
		
    	final CLog cLog = new CLog(applyLogger, request.getRemoteAddr(), request.getRequestURI(), request.getMethod());
		final String loginUser = CookieUtil.getCookieUserID(request);
		final CUDResult result = new CUDResult();
		
		try {
			cLog.startLog("말풍선 즉시적용",  loginUser);
			final String host = SmartUXProperties.getProperty("cache.sync.public.host");
			final String port = SmartUXProperties.getProperty("cache.sync.public.port");
			
			String fileLockPath = SmartUXProperties.getProperty("MainPanelDao.refreshBubbleList.fileLock");
			int waitTime = NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.wait"), 5);
			
			FileProcessLockFactory.start(fileLockPath, waitTime,  new FileProcessFunction("말풍선 즉시적용", ResultCode.ApplyRequestFail) {
				
				@Override
				public void run() throws Exception {
					
					RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
							host, 
							port, 
							SmartUXProperties.getProperty("Bubble.refreshBubble.url"),
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.method"), "POST"), 
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"), 
							MediaType.APPLICATION_JSON.toString(),
							NumberUtils.toInt(SmartUXProperties.getProperty("Bubble.refreshBubble.timeout"), 90000)).getResponseBody());
					
					result.setFlag(codeMsg.getCode());
					result.setMessage(codeMsg.getMessage());
					cLog.middleLog("말풍선 즉시적용", "API 호출", codeMsg.getCode(), codeMsg.getMessage());
				}
			});
		} catch (MimsCommonException e) {
			cLog.warnLog("말풍선 즉시적용", "실패", e.getFlag(), e.getMessage(), e.getLocalizedMessage());
			result.setFlag(e.getFlag());
			result.setMessage(e.getMessage());
		} finally {
			cLog.endLog("말풍선 즉시적용", loginUser, result.getFlag(), result.getMessage());
		}
		
		String resultMsg = "{\"flag\" : \"" + result.getFlag() + "\", \"message\" : \"" + result.getMessage() + "\"}";
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(resultMsg).toString(), responseHeaders, HttpStatus.OK);
	}
	
    
    @ResponseBody
	@RequestMapping(value = "/admin/mainpanel/downloadExcelFile", method = RequestMethod.GET)
	public byte[] downloadExcelFile(
			@RequestParam(value = "findName", required = false, defaultValue = "") String findName,
            @RequestParam(value = "findValue", required = false, defaultValue = "") String findValue,
            @RequestParam(value = "startDt", required = false, defaultValue = "") String startDt,
            @RequestParam(value = "endDt", required = false, defaultValue = "") String endDt,
            @RequestParam(value = "abtestYnChk", required = false, defaultValue = "") String abtestYnChk,
            HttpServletRequest request,
            HttpServletResponse response,
			Model model) throws Exception {
    	
    	logger.debug("[PanelViewController][downloadExcelFile][START]");
        final String loginUser = CookieUtil.getCookieUserID(request);
    	logger.debug("[PanelViewController][downloadExcelFile][USER:"+loginUser+"]");
        
		String domain = GlobalCom.getProperties(SmartUXProperties.getProperty("filepath.suxm.common"), "smartux.domain.https");
		model.addAttribute("domain_https", domain);
		
		findName = HTMLCleaner.clean(findName);
        findValue = HTMLCleaner.clean(findValue);
        startDt = HTMLCleaner.clean(startDt);
        endDt = HTMLCleaner.clean(endDt);
        abtestYnChk = HTMLCleaner.clean(abtestYnChk);
		
		PanelSearchVo vo = new PanelSearchVo();
		vo.setFindName(findName);
		vo.setFindValue(findValue);
		vo.setAbtestYnChk(abtestYnChk);
		if (!startDt.isEmpty() && !endDt.isEmpty()) {
			vo.setStartDt(startDt);
			vo.setEndDt(endDt);
		}

        String[] headerAry = {"패널ID","패널명","지면ID","지면명","테스트ID","테스트명"};

        ExcelWorkBookFactory.ExcelSheetFactory sheet = ExcelWorkBookFactory.create(10000).sheet("패널 리스트");
        sheet.headers(1, headerAry);

        //패널지면
        List<ABTestVO> dbresult = service.getPanelListWithABTest(vo);
        
        //AB테스트 호출
        List<ABTestVO> abmsList = null;
        ABTestSearchVO searchVo = new ABTestSearchVO();
        searchVo.setPageSize(0);
        searchVo.setOffset("0");
        
		Map<String, Object> abmsMap = abservice.getABMSCall(searchVo);
		
		if(!CollectionUtils.isEmpty(abmsMap)){
			if("0".equals(abmsMap.get("resCode"))){
				abmsList = (List<ABTestVO>) abmsMap.get("abmsList");
				if(!CollectionUtils.isEmpty(abmsList)){
					//AMBS 호출 결과와 DB와 매핑
					abmsList = abservice.getABTestList(abmsList);	
				}
			}else{
				logger.warn("[PanelViewController][downloadExcelFile][ABMSCall][resCode: " + abmsMap.get("resCode") + "][resMsg: " + abmsMap.get("resMsg") + "]");
			}
		}
		
		for(ABTestVO dbvo : dbresult){
			LinkedHashSet<String> test_id= new LinkedHashSet<String>();
			LinkedHashSet<String> test_name= new LinkedHashSet<String>();
			for(ABTestVO abvo : abmsList){
				if(dbvo.getPannel_id().equals(abvo.getOrg_mims_id())){
					test_id.add(abvo.getTest_id());
					test_name.add(abvo.getTest_name());
				}
			}
			dbvo.setTest_id(test_id.toString().replace("[", "").replace("]", ""));
			dbvo.setTest_name(test_name.toString().replace("[", "").replace("]", ""));
		}
        
        if(dbresult == null || dbresult.size() == 0) {
        	logger.debug("[PanelViewController][downloadExcelFile][PANEL SIZE : 0]");
        } else {
            sheet.rowCellValues(dbresult, new ExcelWorkBookFactory.CellValueRef<ABTestVO>() {
                @Override
                public void setRowData(Row row, ABTestVO vo) {
                    int col = 1;
                    row.createCell(col++).setCellValue(vo.getPannel_id());
                    row.createCell(col++).setCellValue(vo.getPannel_nm());
                    row.createCell(col++).setCellValue(vo.getTitle_id());
                    row.createCell(col++).setCellValue(vo.getTitle_nm());
                    row.createCell(col++).setCellValue(vo.getTest_id());
                    row.createCell(col++).setCellValue(vo.getTest_name());
                }
            });
        }

		byte[] bytes = sheet.end().make();

		response.setHeader("Content-Disposition", "attachment; filename=MainPanel_IPTV_" + GlobalCom.getTodayFormat() + ".xlsx");
		response.setContentLength(bytes.length);
		response.setContentType("application/vnd.ms-excel");

		logger.debug("[PanelViewController][downloadExcelFile][END]");
		
		return bytes;
	}
    
    @RequestMapping(value = "/admin/mainpanel/showABInfoPopup", method = RequestMethod.GET)
	public String getPanelListPopup(
			@RequestParam(value = "panel_id", required = false, defaultValue = "") String panel_id,
			@RequestParam(value = "abtestYnChk", required = false, defaultValue = "") String abtestYnChk,
			Model model) throws Exception {
		
    	panel_id = HTMLCleaner.clean(panel_id);
    	abtestYnChk = HTMLCleaner.clean(abtestYnChk);
    	
    	PanelSearchVo vo = new PanelSearchVo();
    	vo.setAbtestYnChk(abtestYnChk);
		vo.setPanel_id(panel_id);

        //AB테스트 호출
        List<ABTestVO> abmsList = null;
        ABTestSearchVO searchVo = new ABTestSearchVO();
        searchVo.setPageSize(0);
        searchVo.setOffset("0");
        
		Map<String, Object> abmsMap = abservice.getABMSCall(searchVo);
		
		if(!CollectionUtils.isEmpty(abmsMap)){
			if("0".equals(abmsMap.get("resCode"))){
				abmsList = (List<ABTestVO>) abmsMap.get("abmsList");
				if(!CollectionUtils.isEmpty(abmsList)){
					//AMBS 호출 결과와 DB와 매핑
					abmsList = abservice.getABTestList(abmsList);	
				}
			}else{
				logger.warn("[PanelViewController][getPanelListPopup][ABMSCall][resCode: " + abmsMap.get("resCode") + "][resMsg: " + abmsMap.get("resMsg") + "]");
			}
		}
		
		Map<String, ABTestVO> resultMap = new LinkedHashMap<String, ABTestVO>();
		List<ABTestVO> result = new ArrayList<ABTestVO>();
		
		for(ABTestVO abvo : abmsList){
			if(panel_id.equals(abvo.getOrg_mims_id())){
				abvo.setPannel_id(panel_id);
				resultMap.put(abvo.getTest_id(), abvo);
			}
		}
    	
		for( String key : resultMap.keySet() ){
			result.add(resultMap.get(key));
        }
		
		model.addAttribute("list", result);

		return "/admin/mainpanel/showABInfoPopup";
	}
}
