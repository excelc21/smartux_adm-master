package com.dmi.smartux.admin.code.controller;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.util.StringUtils;
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
import com.dmi.smartux.admin.code.service.CodeService;
import com.dmi.smartux.admin.code.vo.CodeItemVO;
import com.dmi.smartux.admin.code.vo.CodeVO;
import com.dmi.smartux.common.exception.ExceptionHandler;
import com.dmi.smartux.common.exception.SmartUXException;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.CookieUtil;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.util.HTMLCleaner;

@Controller
public class CodeController {
	
	private final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	CodeService service;
	
	private final Log applyLogger = LogFactory.getLog("adminApplyCacheHistroy");
	private ObjectMapper om = new ObjectMapper();
	
	/**
	 * SmartUX 코드 아이템 목록화면에서 즉시적용 버튼을 클릭하여 캐쉬를 바로 적용할때 이용된다
	 * @param code	캐쉬를 바로 적용시킬 코드
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/admin/code/applyCache", method=RequestMethod.POST)
	public ResponseEntity<String> applyCache(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value="callByScheduler",  defaultValue="Y") final  String callByScheduler,
			@RequestParam(value="code") final String code) throws Exception {
		
		final CLog cLog = new CLog(applyLogger, request.getRemoteAddr(), request.getRequestURI(), request.getMethod());
		final String loginUser = callByScheduler.equals("Y") ? "batch" : CookieUtil.getCookieUserID(request);
		final Map<String, String> map = new HashMap<String, String>();
		
		map.put("flag", SmartUXProperties.getProperty("flag.success"));
		map.put("message", SmartUXProperties.getProperty("message.success"));
		
		map.put("ux_flag", "0000");
		map.put("ux_message", "미진행");
		
		map.put("i20_flag", "0000");
		map.put("i20_message", "미진행");
		
		map.put("sma_flag", "0000");
		map.put("sma_message", "미진행");
		
		map.put("i30_flag", "0000");
		map.put("i30_message", "미진행");
		
		map.put("simplepay_flag", "0000");
		map.put("simplepay_message", "미진행");
		
		map.put("nowpay_flag", "0000");
		map.put("nowpay_message", "미진행");
		
		map.put("pgpay_flag", "0000");
		map.put("pgpay_message", "미진행");
		
		map.put("tvpay_flag", "0000");
		map.put("tvpay_message", "미진행");
		
		try {
			cLog.startLog("설정정보 즉시적용",  loginUser);
			final String host = SmartUXProperties.getProperty("cache.sync.public.host");
			final String port = SmartUXProperties.getProperty("cache.sync.public.port");
			
			String fileLockPath = SmartUXProperties.getProperty("SettingDao.refreshSetting.fileLock");
			int waitTime = NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.wait"), 5);
			
			FileProcessLockFactory.start(fileLockPath, waitTime,  new FileProcessFunction("설정정보 즉시적용", ResultCode.ApplyRequestFail) {
				
				@Override
				public void run() throws Exception {
					if(SmartUXProperties.getProperty("code.code1").equals(code) || "Y".equals(callByScheduler)){
						
						//즉시적용 호출 시 sleep 값 추가 -> DB분리건으로 OGG가 10초 이상 걸리는 경우가 있어 즉시적용 시 반영이 안돼서 추가함
						Thread.sleep(Integer.parseInt(org.apache.commons.lang.StringUtils.defaultIfEmpty(SmartUXProperties.getProperty("apply.cache.call.sleep"), "10000")));
						
						RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
								host, 
								port, 
								SmartUXProperties.getProperty("SmartEPGDao.refreshThemeInfo.url")+"?callByScheduler="+callByScheduler,
								org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.method"), "POST"), 
								org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"), 
								MediaType.APPLICATION_JSON.toString(),
								NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.timeout"), 60000)).getResponseBody());
						
						map.put("flag", codeMsg.getCode());
						map.put("message", codeMsg.getMessage());
						cLog.middleLog("설정정보 즉시적용", "테마 즉시적용 API 호출", codeMsg.getCode(), codeMsg.getMessage());
					}
				}
			}, new FileProcessFunction("설정정보 즉시적용", ResultCode.ApplyRequestFail) {
				
				@Override
				public void run() throws Exception {
					if(SmartUXProperties.getProperty("code.code2").equals(code) || "Y".equals(callByScheduler)){
						
						//즉시적용 호출 시 sleep 값 추가 -> DB분리건으로 OGG가 10초 이상 걸리는 경우가 있어 즉시적용 시 반영이 안돼서 추가함
						Thread.sleep(Integer.parseInt(org.apache.commons.lang.StringUtils.defaultIfEmpty(SmartUXProperties.getProperty("apply.cache.call.sleep"), "10000")));
						
						RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
								host, 
								port, 
								SmartUXProperties.getProperty("SmartStartItemListDao.refreshSmartStartItemList.url")+"?callByScheduler="+callByScheduler,
								org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.method"), "POST"), 
								org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"), 
								MediaType.APPLICATION_JSON.toString(),
								NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.timeout"), 60000)).getResponseBody());
						
						map.put("flag", codeMsg.getCode());
						map.put("message", codeMsg.getMessage());
						cLog.middleLog("설정정보 즉시적용", "스마트스타트 즉시적용 API 호출", codeMsg.getCode(), codeMsg.getMessage());
					}
				}
			}, new FileProcessFunction("설정정보 즉시적용", ResultCode.ApplyRequestFail) {
				
				@Override
				public void run() throws Exception {
					//스케줄일때는 호출 안함
					if(SmartUXProperties.getProperty("code.code6").equals(code) && "A".equals(callByScheduler)){
						
						//즉시적용 호출 시 sleep 값 추가 -> DB분리건으로 OGG가 10초 이상 걸리는 경우가 있어 즉시적용 시 반영이 안돼서 추가함
						Thread.sleep(Integer.parseInt(org.apache.commons.lang.StringUtils.defaultIfEmpty(SmartUXProperties.getProperty("apply.cache.call.sleep"), "10000")));
						
						RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
								host, 
								port, 
								SmartUXProperties.getProperty("settingDao.refreshCacheInterval.url")+"?callByScheduler="+callByScheduler,
								org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.method"), "POST"), 
								org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"), 
								MediaType.APPLICATION_JSON.toString(),
								NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.timeout"), 60000)).getResponseBody());
						
						map.put("flag", codeMsg.getCode());
						map.put("message", codeMsg.getMessage());
						cLog.middleLog("설정정보 즉시적용", "cache interval 즉시적용 API 호출", codeMsg.getCode(), codeMsg.getMessage());
					}
				}
			}, new FileProcessFunction("설정정보 즉시적용", ResultCode.ApplyRequestFail) {
				
				@Override
				public void run() throws Exception {
					if(SmartUXProperties.getProperty("code.code5").equals(code) || "Y".equals(callByScheduler)){
						
						//즉시적용 호출 시 sleep 값 추가 -> DB분리건으로 OGG가 10초 이상 걸리는 경우가 있어 즉시적용 시 반영이 안돼서 추가함
						Thread.sleep(Integer.parseInt(org.apache.commons.lang.StringUtils.defaultIfEmpty(SmartUXProperties.getProperty("apply.cache.call.sleep"), "10000")));
						
						RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
								host, 
								port, 
								SmartUXProperties.getProperty("RetryDao.refreshPushGateWayRetry.url")+"?callByScheduler="+callByScheduler,
								org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.method"), "POST"), 
								org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"), 
								MediaType.APPLICATION_JSON.toString(),
								NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.timeout"), 60000)).getResponseBody());
						
						map.put("flag", codeMsg.getCode());
						map.put("message", codeMsg.getMessage());
						cLog.middleLog("설정정보 즉시적용", "push retry cache 즉시적용 API 호출", codeMsg.getCode(), codeMsg.getMessage());
					}
				}
			}, new FileProcessFunction("설정정보 즉시적용", ResultCode.ApplyRequestFail) {
				
				@Override
				public void run() throws Exception {
					if(SmartUXProperties.getProperty("code.code3").equals(code) || "Y".equals(callByScheduler)){
						
						//즉시적용 호출 시 sleep 값 추가 -> DB분리건으로 OGG가 10초 이상 걸리는 경우가 있어 즉시적용 시 반영이 안돼서 추가함
						Thread.sleep(Integer.parseInt(org.apache.commons.lang.StringUtils.defaultIfEmpty(SmartUXProperties.getProperty("apply.cache.call.sleep"), "10000")));
						
						RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
								host, 
								port, 
								SmartUXProperties.getProperty("ConfigurationListDao.refreshConfigurationList1.url")+"&callByScheduler="+callByScheduler,
								org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.method"), "POST"), 
								org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"), 
								MediaType.APPLICATION_JSON.toString(),
								NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.timeout"), 60000)).getResponseBody());
						
						map.put("ux_flag", codeMsg.getCode());
						map.put("ux_message", codeMsg.getMessage());
						cLog.middleLog("설정정보 즉시적용", "Configuration UX 즉시적용 API 호출", codeMsg.getCode(), codeMsg.getMessage());
					}
				}
			}, new FileProcessFunction("설정정보 즉시적용", ResultCode.ApplyRequestFail) {
				
				@Override
				public void run() throws Exception {
					if(SmartUXProperties.getProperty("code.code3").equals(code) || "Y".equals(callByScheduler)){
						RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
								host, 
								port, 
								SmartUXProperties.getProperty("ConfigurationListDao.refreshConfigurationList2.url")+"&callByScheduler="+callByScheduler,
								org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.method"), "POST"), 
								org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"), 
								MediaType.APPLICATION_JSON.toString(),
								NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.timeout"), 60000)).getResponseBody());
						
						map.put("i20_flag", codeMsg.getCode());
						map.put("i20_message", codeMsg.getMessage());
						cLog.middleLog("설정정보 즉시적용", "Configuration I20 즉시적용 API 호출", codeMsg.getCode(), codeMsg.getMessage());
					}
				}
			}, new FileProcessFunction("설정정보 즉시적용", ResultCode.ApplyRequestFail) {
				
				@Override
				public void run() throws Exception {
					if(SmartUXProperties.getProperty("code.code3").equals(code) || "Y".equals(callByScheduler)){
						RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
								host, 
								port, 
								SmartUXProperties.getProperty("ConfigurationListDao.refreshConfigurationList3.url")+"&callByScheduler="+callByScheduler,
								org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.method"), "POST"), 
								org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"), 
								MediaType.APPLICATION_JSON.toString(),
								NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.timeout"), 60000)).getResponseBody());
						
						map.put("sma_flag", codeMsg.getCode());
						map.put("sma_message", codeMsg.getMessage());
						cLog.middleLog("설정정보 즉시적용", "Configuration SMA 즉시적용 API 호출", codeMsg.getCode(), codeMsg.getMessage());
					}
				}
			}, new FileProcessFunction("설정정보 즉시적용", ResultCode.ApplyRequestFail) {
				
				@Override
				public void run() throws Exception {
					if(SmartUXProperties.getProperty("code.code3").equals(code)|| "Y".equals(callByScheduler)){
						RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
								host, 
								port, 
								SmartUXProperties.getProperty("ConfigurationListDao.refreshConfigurationList4.url")+"&callByScheduler="+callByScheduler,
								org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.method"), "POST"), 
								org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"), 
								MediaType.APPLICATION_JSON.toString(),
								NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.timeout"), 60000)).getResponseBody());
						
						map.put("i30_flag", codeMsg.getCode());
						map.put("i30_message", codeMsg.getMessage());
						cLog.middleLog("설정정보 즉시적용", "Configuration I30 즉시적용 API 호출", codeMsg.getCode(), codeMsg.getMessage());
					}
				}
			}, new FileProcessFunction("설정정보 즉시적용", ResultCode.ApplyRequestFail) {
				
				@Override
				public void run() throws Exception {
					if(SmartUXProperties.getProperty("code.code3").equals(code)|| "Y".equals(callByScheduler)){
						RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
								host, 
								port, 
								SmartUXProperties.getProperty("ConfigurationListDao.refreshConfigurationList.payment1.url"),
								"GET", 
								org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"), 
								MediaType.APPLICATION_JSON.toString(),
								NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.timeout"), 60000)).getResponseBody());
						
						map.put("simplepay_flag", codeMsg.getCode());
						map.put("simplepay_message", codeMsg.getMessage());
						cLog.middleLog("설정정보 즉시적용", "simplepaytv 즉시적용 API 호출", codeMsg.getCode(), codeMsg.getMessage());
					}
				}
			}, new FileProcessFunction("설정정보 즉시적용", ResultCode.ApplyRequestFail) {
				
				@Override
				public void run() throws Exception {
					if(SmartUXProperties.getProperty("code.code3").equals(code)|| "Y".equals(callByScheduler)){
						RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
								host, 
								port, 
								SmartUXProperties.getProperty("ConfigurationListDao.refreshConfigurationList.payment2.url"),
								"GET",  
								org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"), 
								MediaType.APPLICATION_JSON.toString(),
								NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.timeout"), 60000)).getResponseBody());
						
						map.put("nowpay_flag", codeMsg.getCode());
						map.put("nowpay_message", codeMsg.getMessage());
						cLog.middleLog("설정정보 즉시적용", "nowpaytv 즉시적용 API 호출", codeMsg.getCode(), codeMsg.getMessage());
					}
				}
			}, new FileProcessFunction("설정정보 즉시적용", ResultCode.ApplyRequestFail) {
				
				@Override
				public void run() throws Exception {
					if(SmartUXProperties.getProperty("code.code3").equals(code)|| "Y".equals(callByScheduler)){
						RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
								host, 
								port, 
								SmartUXProperties.getProperty("ConfigurationListDao.refreshConfigurationList.payment3.url"),
								"GET",  
								org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"), 
								MediaType.APPLICATION_JSON.toString(),
								NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.timeout"), 60000)).getResponseBody());
						
						map.put("pgpay_flag", codeMsg.getCode());
						map.put("pgpay_message", codeMsg.getMessage());
						cLog.middleLog("설정정보 즉시적용", "pgpay 즉시적용 API 호출", codeMsg.getCode(), codeMsg.getMessage());
					}
				}
			}, new FileProcessFunction("설정정보 즉시적용", ResultCode.ApplyRequestFail) {
				
				@Override
				public void run() throws Exception {
					if(SmartUXProperties.getProperty("code.code3").equals(code)|| "Y".equals(callByScheduler)){
						RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
								host, 
								port, 
								SmartUXProperties.getProperty("ConfigurationListDao.refreshConfigurationList.payment4.url"),
								"GET",  
								org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"), 
								MediaType.APPLICATION_JSON.toString(),
								NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.timeout"), 60000)).getResponseBody());
						
						map.put("tvpay_flag", codeMsg.getCode());
						map.put("tvpay_message", codeMsg.getMessage());
						cLog.middleLog("설정정보 즉시적용", "tvpay 즉시적용 API 호출", codeMsg.getCode(), codeMsg.getMessage());
					}
				}
			});
			
			
		} catch (MimsCommonException e) {
			cLog.warnLog("설정정보 즉시적용", "실패", e.getFlag(), e.getMessage(), e.getLocalizedMessage());
			map.put("flag", e.getFlag());
			map.put("message", e.getMessage());
		} finally {
			cLog.endLog("설정정보 즉시적용", loginUser, map.get("flag"), map.get("message"));
		}
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		return new ResponseEntity<String>(om.writeValueAsString(map), responseHeaders, HttpStatus.OK);
		
	}
	
	/**
	 * SmartUX 코드 목록 조회 화면
	 * @param model		Model 객체
	 * @return			SmartUX 코드 목록 조회 화면 URL
	 */
	@RequestMapping(value="/admin/code/getCodeList")
	public String getCodeList(Model model) throws Exception{
		List<CodeVO> result = new ArrayList<CodeVO>();
		
		// 현재 DB에 등록되어 있는 코드를 읽어온다
		List<CodeVO> dbresult = service.getCodeList();
		
		for(CodeVO objVO : dbresult){
			// pannel.properties 파일에서 등록할 패널들을 읽어온다
			objVO.setExistProperty("N");
			int propertylength = Integer.parseInt(SmartUXProperties.getProperty("code.length"));
			for(int i=1; i <= propertylength; i++){
				String idkey = "code.code" + i;
				String code = SmartUXProperties.getProperty(idkey);
				
				if(objVO.getCode().equals(code)){
					objVO.setExistProperty("Y");
					break;
				}
			}
			
			result.add(objVO);
		}
		
		model.addAttribute("result", result);
		
		return "/admin/code/getCodeList";
	}
	
	/**
	 * SmartUX 코드 등록 화면
	 * @param model		Model 객체
	 * @return			SmartUX 코드 등록 화면 URL
	 */
	@RequestMapping(value="/admin/code/insertCode", method=RequestMethod.GET)
	public String insertCode(Model model) throws Exception{
		
		List<CodeVO> result = new ArrayList<CodeVO>();
		
		// 현재 DB에 등록되어 있는 패널을 읽어온다
		List<CodeVO> dbresult = service.getCodeList();
		
		// pannel.properties 파일에서 등록할 패널들을 읽어온다
		int propertylength = Integer.parseInt(SmartUXProperties.getProperty("code.length"));
		for(int i=1; i <= propertylength; i++){
			String idkey = "code.code" + i;
			String code = SmartUXProperties.getProperty(idkey);
			String nmkey = "code.msg" + i;
			String code_nm = SmartUXProperties.getProperty(nmkey);
			
			boolean find = false;
			for(int j=0; j < dbresult.size(); j++){
				CodeVO dbobj = dbresult.get(j);
				if(dbobj.getCode().equals(code)){					// DB에서 읽은 것이 현재 프로퍼티에 존재하면
					find = true;
					break;
				}
			}
			
			if(find == false){
				CodeVO obj = new CodeVO();
				obj.setCode(code);
				obj.setCode_nm(code_nm);
				result.add(obj);
			}
		}
		
		model.addAttribute("result", result);

		return "/admin/code/insertCode";
	}
	
	/**
	 * SmartUX 코드 등록 처리 화면
	 * @param code		SmartUX 코드에 등록할 코드닶(프로퍼티 파일에서 관리되는 코드였을 경우 프로퍼티 파일에 저장되어 있는 코드값이 들어가야 한다)
	 * @param code_nm	SmartUX 코드에 등록할 코드명
	 * @param smartUXManager	관리자 사이트에 로그인 한 사람의 로그인 id
	 * @return 			처리 결과를 기록한 json 문자열
	 */
	@RequestMapping(value="/admin/code/insertCode", method=RequestMethod.POST)
	public @ResponseBody String insertCode(@RequestParam(value="code") String code,
			@RequestParam(value="code_nm") String code_nm, 
			@RequestParam(value="smartUXManager") String login_id){
		
		HTMLCleaner cleaner = new HTMLCleaner();
		code 	= cleaner.clean(code);		
		code_nm 	= cleaner.clean(code_nm);
		login_id 	= cleaner.clean(login_id);
		
		String create_id = login_id;			// 등록자 ID(차후에 로그인 한 사람의 로그인 ID로 바꿀것
		String resultcode = "";
		String resultmessage = "";
		
		try{
			validateInsertCode(code_nm);												// 입력받은 파라미터 값들에 대한 validation 작업 진행
			int count = service.getCodenmCnt(code_nm);									// 입력받은 코드명이 기존에 사용중인 코드명인지를 확인한다
			if(count == 0){																// 입력받은 코드명이 기존에 없으면
				service.insertCode(code, code_nm, create_id);
			
				resultcode = SmartUXProperties.getProperty("flag.success");
				resultmessage = SmartUXProperties.getProperty("message.success");
			}else{																		// 입력받은 코드명이 기존에 있으면
				SmartUXException e = new SmartUXException();
				e.setFlag("EXISTS CODE_NM");
				e.setMessage("코드명 존재");
				throw e;
			}
		}catch(Exception e){
			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}
		
		// String result = "{flag : " + resultcode + ", message : " + resultmessage + "}";
		
		return "{\"flag\" : \"" + resultcode + "\", \"message\" : \"" + resultmessage + "\"}";
	}
	
	/**
	 * insertCode 함수에 대한 validation 작업 함수
	 * @param code_nm				코드명
	 * @throws SmartUXException
	 */
	private void validateInsertCode(String code_nm) throws SmartUXException{
		SmartUXException exception = new SmartUXException();
		if(!(StringUtils.hasText(code_nm))){
			
			/*
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "코드명"));
			*/
			exception.setFlag("NOT FOUND CODE_NM");
			exception.setMessage("코드명은 필수로 들어가야 합니다");
			throw exception;
		}
		
		if(code_nm.length() > 100){
			exception.setFlag("CODE_NM LENGTH");
			exception.setMessage("코드명은 100자 이내이어야 합니다");
			throw exception;
		}
	}
	
	/**
	 * SmartUX 코드 상세 조회 화면
	 * @param code		SmartUX 코드에서 상세조회할 코드
	 * @param model		Model 객체
	 * @return			SmartUX 코드 상세조회 화면 URL
	 */
	@RequestMapping(value="/admin/code/viewCode", method=RequestMethod.GET)
	public String viewCode(@RequestParam(value="code") String code, 
			Model model) throws Exception{
		
		HTMLCleaner cleaner = new HTMLCleaner();
		code 	= cleaner.clean(code);		
		
		CodeVO result = service.viewCode(code);
		model.addAttribute("result", result);
		return "/admin/code/viewCode";
	}
	
	/**
	 * SmartUX 코드 수정 처리 작업
	 * @param code		SmartUX 코드에서 코드명을 수정할 코드값
	 * @param code_nm	SmartUX 코드에서 수정할 코드명
	 * @param smartUXManager	관리자 사이트에 로그인 한 사람의 로그인 id
	 * @return			처리 결과를 기록한 json 문자열	
	 */
	@RequestMapping(value="/admin/code/updateCode", method=RequestMethod.POST)
	public @ResponseBody String updateCode(@RequestParam(value="code") String code, 
			@RequestParam(value="code_nm") String code_nm, 
			@RequestParam(value="smartUXManager") String login_id){
		
		HTMLCleaner cleaner = new HTMLCleaner();
		code 	= cleaner.clean(code);
		code_nm 	= cleaner.clean(code_nm);
		login_id 	= cleaner.clean(login_id);
		
		String update_id = login_id;			// 수정자 ID(차후에 로그인 한 사람의 로그인 ID로 바꿀것
		String resultcode = "";
		String resultmessage = "";
		try{
			validateUpdateCode(code, code_nm);										// 입력받은 파라미터 값들에 대한 validation 작업 진행
			int count = service.getCodenmCnt(code_nm);								// 수정하고자 입력한 새로운 코드명이 기존에 사용중인 코드명인지를 확인한다
			if(count == 0){															// 기존에 사용중인 코드명이 아니면
				service.updateCode(code, code_nm, update_id);
				resultcode = SmartUXProperties.getProperty("flag.success");
				resultmessage = SmartUXProperties.getProperty("message.success");
			}else{																	// 기존에 사용중인 코드명이면
				SmartUXException e = new SmartUXException();
				// e.setFlag(SmartUXProperties.getProperty("flag.key1"));
				// e.setMessage("수정하고자 하는 " + code_nm + "은 현재 사용중인 코드명입니다");
				e.setFlag("EXISTS CODE_NM");
				e.setMessage(code_nm);
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
	 * updateCode 함수에 대한 validation 작업 함수
	 * @param code					SmartUX 코드에서 코드명을 수정할 코드값		
	 * @param code_nm				SmartUX 코드에서 수정할 코드명
	 * @throws SmartUXException
	 */
	private void validateUpdateCode(String code, String code_nm) throws SmartUXException{
		SmartUXException exception = new SmartUXException();
		
		if(!(StringUtils.hasText(code))){
			/*
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "코드"));
			*/
			exception.setFlag("NOT FOUND CODE");
			exception.setMessage("코드는 필수로 들어가야 합니다");
			throw exception;
		}

		if(!(StringUtils.hasText(code_nm))){
			exception.setFlag("NOT FOUND CODE_NM");
			exception.setMessage("코드명은 필수로 들어가야 합니다");
			throw exception;
		}
		
		if(code_nm.length() > 100){
			exception.setFlag("CODE_NM LENGTH");
			exception.setMessage("코드명은 100자 이내이어야 합니다");
			throw exception;
		}
	}
	
	/**
	 * SmartUX 코드 삭제 작업
	 * @param codes		SmartUX 코드에서 삭제할 코드값들의 배열
	 * @return			처리 결과를 기록한 json 문자열
	 */
	@RequestMapping(value="/admin/code/deleteCode", method=RequestMethod.POST)
	public @ResponseBody String deleteCode(@RequestParam(value="code[]") String [] codes){
		String resultcode = "";
		String resultmessage = "";
		try{
			validateDeleteCode(codes);			// 입력받은 파라미터 값들에 대한 validation 작업 진행
			checkCodeItem(codes);				// 입력받은 삭제할 코드값들의 배열을 넘겨줘서 해당 코드들에 서브 아이템 코드들이 있는지를 조사한다. 있을경우 예외를 던진다
			service.deleteCode(codes);
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
	 * deleteCode 함수에 대한 validation 작업 함수
	 * @param codes					SmartUX 코드에서 삭제할 코드값들의 배열
	 * @throws SmartUXException
	 */
	private void validateDeleteCode(String [] codes) throws SmartUXException{
		SmartUXException exception = new SmartUXException();
		
		if((codes == null) || (codes.length == 0)){
			/*
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "삭제할 코드값들"));
			throw exception;
			*/
			exception.setFlag("NOT FOUND CODE");
			exception.setMessage("코드값은 필수로 들어가야 합니다");
			throw exception;
		}
		
	}
	
	/**
	 * 입력받은 코드에 대해서 아이템 코드가 있는지를 확인하여 있을 경우 예외를 던지도록 한다
	 * @param codes					아이템 코드가 있는지를 확인할 코드 값들의 배열
	 * @throws SmartUXException
	 */
	private void checkCodeItem(String [] codes) throws SmartUXException{
		SmartUXException exception = new SmartUXException();
		String result = "";
		for(String code : codes){
			CodeVO objVo = null;
			try{
				objVo = service.checkItemCode(code);
			}catch(Exception e){
				ExceptionHandler handler = new ExceptionHandler(e);
				exception.setFlag(handler.getFlag());
				exception.setMessage(handler.getMessage());
				throw exception;
			}
			
			if(objVo != null){
				if(objVo.getItemcodecnt() > 0){
					if("".equals(result)){
						result = objVo.getCode_nm();
					}else{
						result += ", " + objVo.getCode_nm();
					}
				}
			}
		}
		
		// 서브 아이템이 있을 경우 result 변수 문자열은 ""가 아니게 된다. 그래서 이럴 경우 예외를 던져서 호출한 쪽에서 받도록 한다
		if(!("".equals(result))){
			/*
			exception.setFlag(SmartUXProperties.getProperty("flag.deleteitemcode"));
			exception.setMessage(result);
			throw exception;
			*/
			exception.setFlag("FOUND ITEM CODE");
			exception.setMessage(result);
			throw exception;
		}
	}
	
	
	/**
	 * SmartUX Item 코드값 목록 조회 화면
	 * @param code		SmartUX Code Item을 조회하고자 하는 code
	 * @param model		Model 객체
	 * @return			SmartUX Code Item 코드값 목록 조회 화면 URL
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/code/getCodeItemList")
	public String getCodeItemList(@RequestParam(value="code", defaultValue="") String code, Model model) throws Exception{
		
		HTMLCleaner cleaner = new HTMLCleaner();
		code 	= cleaner.clean(code);
		
		List<CodeVO> result = service.getCodeList();
		List<CodeItemVO> itemresult = null;
		
		// 처음 화면을 로드했을땐 code를 입력받은 것이 없을 것이므로 SmartUX 코드 조회결과로 나온것중 첫번째 것을 code로 셋팅한다
		if("".equals(GlobalCom.isNull(code))){	
			code = result.get(0).getCode();
		}
		
		itemresult = service.getCodeItemList(code);
		model.addAttribute("code", code);
		model.addAttribute("result", result);
		model.addAttribute("itemresult", itemresult);
		
		return "/admin/code/getCodeItemList";
		
	}
	
	/**
	 * SmartUX Item 코드값 상세 조회 화면
	 * @param code			SmartUX Code Item을 상세조회 하고자 하는 code
	 * @param item_code		SmartUX Code Item을 상세조회 하고자 하는 item_code
	 * @param model			Model 객체
	 * @return				SmartUX Item 상세조회 화면 URL
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/code/viewCodeItem", method=RequestMethod.GET)
	public String viewCodeItem(@RequestParam(value="code") String code, @RequestParam(value="item_code") String item_code, Model model) throws Exception{
		
		HTMLCleaner cleaner = new HTMLCleaner();
		code 	= cleaner.clean(code);
		item_code 	= cleaner.clean(item_code);
		
		
		// Smart Start 관련 아이템을 등록할때 사용하는 SmartUX 타입들을 조회해서 가져온다
//		List<String> smartstart = service.getSmartstartList();
		
		// 설정정보 조회할때 사용되는 어플타입을 조회해서 가져온다
		// List<CodeItemVO> app_type_list = service.getCodeItemList("A0010");
		List<CodeItemVO> app_type_list = service.getCodeItemList(SmartUXProperties.getProperty("code.code4"));
				
		CodeItemVO result = service.viewCodeItem(code, item_code);
		
		model.addAttribute("result", result);
//		model.addAttribute("smartstart", smartstart);
		model.addAttribute("app_type_list", app_type_list);
		return "/admin/code/viewCodeItem";
	}
	
	/**
	 * SmartUX Item 코드값 등록 화면
	 * @param code			SmartUX Code Item을 등록하고자 하는 code
	 * @return				SmartUX Code Item 코드값 등록 화면 URL
	 */
	@RequestMapping(value="/admin/code/insertCodeItem", method=RequestMethod.GET)
	public String insertCodeItem(@RequestParam(value="code") String code, Model model) throws Exception{
		
		HTMLCleaner cleaner = new HTMLCleaner();
		code 	= cleaner.clean(code);
		
		// Smart Start 관련 아이템을 등록할때 사용하는 SmartUX 타입들을 조회해서 가져온다
		List<String> smartstart = new ArrayList<String>();
		
		int length = Integer.parseInt(SmartUXProperties.getProperty("itemtype.length"));
		
				
		// SmartUX 지면 항목 설정 정보를 읽어온다
		List<CodeItemVO> currentitemtype = service.getCodeItemList(SmartUXProperties.getProperty("code.code2"));
		
		for(int i= 1; i <= length; i++){
			String key = "itemtype.code" + i;
			String value = SmartUXProperties.getProperty(key);
			boolean blfind = false;
			for(CodeItemVO item : currentitemtype){
				if(value.equals(item.getSs_gbn())){
					blfind = true;
				}
			}
			
			if(blfind == false){
				smartstart.add(value);
			}
		}
		
		// 설정정보 조회할때 사용되는 어플타입을 조회해서 가져온다
		// List<CodeItemVO> app_type_list = service.getCodeItemList("A0010");
		List<CodeItemVO> app_type_list = service.getCodeItemList(SmartUXProperties.getProperty("code.code4"));
		
		model.addAttribute("code", code);
		
		model.addAttribute("smartstart", smartstart);
		model.addAttribute("app_type_list", app_type_list);
		return "/admin/code/insertCodeItem";
	}
	
	/**
	 * SmartUX Item 코드값 등록 처리 작업
	 * @param code				SmartUX Code Item을 등록하고자 하는 code
	 * @param item_code			SmartUX Code Item의 item_code
	 * @param item_nm			SmartUX Code Item의 item_nm
	 * @param ss_gbn			Smart Start 관련 코드 아이템 등록할때 들어가지는 스마트스타트 구분 코드
	 * @param app_type			설정 정보 코드 아이템 등록할때 들어가지는 어플타입 코드 
	 * @param use_yn			SmartUX Item의 사용여부(Y/N)
	 * @param smartUXManager	관리자 사이트에 로그인 한 사람의 로그인 id	
	 * @return					처리 결과를 기록한 json 문자열
	 */
	@RequestMapping(value="/admin/code/insertCodeItem", method=RequestMethod.POST)
	public @ResponseBody String insertCodeItem(
			@RequestParam(value="code") String code, 
			@RequestParam(value="item_code") String item_code, 
			@RequestParam(value="item_nm") String item_nm,
			@RequestParam(value="ss_gbn", required=false, defaultValue="") String ss_gbn,
			@RequestParam(value="app_type", required=false, defaultValue="") String app_type,
			@RequestParam(value="use_yn") String use_yn,
			@RequestParam(value="smartUXManager") String login_id) {
		
		HTMLCleaner cleaner = new HTMLCleaner();
		code 	= cleaner.clean(code);
		item_code 	= cleaner.clean(item_code);
		item_nm 	= cleaner.clean(item_nm);
		ss_gbn 	= cleaner.clean(ss_gbn);
		app_type 	= cleaner.clean(app_type);
		use_yn 	= cleaner.clean(use_yn);
		login_id 	= cleaner.clean(login_id);
		
		String create_id = login_id;			// 등록자 ID(차후에 로그인 한 사람의 로그인 ID로 바꿀것
		String resultcode = "";
		String resultmessage = "";
		try{
			validateInsertCodeItem(code, item_code, item_nm, ss_gbn, use_yn);		// 입력받은 파라미터 값들에 대한 validation 작업 진행
			int count = service.getCodeItemcodeCnt(code, item_code);		// 입력받은 코드값(item_code)이 기존에 존재하는지를 확인
			
			//Cache 갱신주기는 중복되어도 됨
			if(code.equals(SmartUXProperties.getProperty("code.code6"))){
				count = 0;
			}
			
			if(count == 0){													// 입력받은 코드값이 기존에 존재하지 않으면
				count = service.getCodeItemnmCnt(code, item_nm);			// 입력받은 코드명이 기존에 존재하는지를 확인
				//Cache 갱신주기는 중복되어도 됨
				if(code.equals(SmartUXProperties.getProperty("code.code6"))){
					count = 0;
				}
				if(count == 0){												// 입력받은 코드명이 기존에 존재하지 않으면
					if(code.equals(SmartUXProperties.getProperty("code.code2"))){
						ss_gbn = ss_gbn.toUpperCase();
						int ssgbnCnt = service.getCodeItemssgbnCnt(ss_gbn);				// 입력받은 SmartUX 타입(ss_gbn이)이 기존에 존재하는지 확인
						
						if(ssgbnCnt > 0){												// 입력받은 SmartUX 타입(ss_gbn이)이 기존에 존재하면
							SmartUXException e = new SmartUXException();
							e.setFlag("EXISTS SS_GBN");
							e.setMessage("SmartUX 타입 존재");
							throw e;
						}
					}
					service.insertCodeItem(code, item_code, item_nm, ss_gbn, app_type, use_yn, create_id);
					resultcode = SmartUXProperties.getProperty("flag.success");
					resultmessage = SmartUXProperties.getProperty("message.success");
				}else{														// 입력받은 코드명이 기존에 존재하면
					SmartUXException e = new SmartUXException();
					e.setFlag("EXISTS ITEM_NM");
					e.setMessage("코드명 존재");
					throw e;
				}
			}else{															// 입력받은 코드값(item_code)이 기존에 존재하면
				SmartUXException e = new SmartUXException();
				e.setFlag("EXISTS ITEM_CODE");
				e.setMessage("코드값 존재");
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
	 * insertCodeItem 함수에 대한 validation 작업 함수
	 * @param code			SmartUX Code Item을 등록하고자 하는 code
	 * @param item_code		SmartUX Code Item의 item_code
	 * @param item_nm		SmartUX Code Item의 item_nm
	 * @param use_yn		SmartUX Code Item의 사용여부(Y/N)
	 */
	private void validateInsertCodeItem(String code, String item_code, String item_nm, String ss_gbn, String use_yn){
		
		SmartUXException exception = new SmartUXException();
		
		if(!(StringUtils.hasText(code))){
			exception.setFlag("NOT FOUND CODE");
			exception.setMessage("코드값은 필수로 들어가야 합니다");
			
			throw exception;
		}
		
		if(!(StringUtils.hasText(item_code))){
			exception.setFlag("NOT FOUND ITEM_CODE");
			exception.setMessage("아이템 코드값은 필수로 들어가야 합니다");
		
			throw exception;
		}
		
		if(item_code.getBytes(Charset.forName("EUC-KR")).length > 100){
			exception.setFlag("ITEM_CODE LENGTH");
			exception.setMessage("아이템 코드값은 100Byte 이내이어야 합니다");
		
			throw exception;
		}
		
		if(!(StringUtils.hasText(item_nm))){
			exception.setFlag("NOT FOUND ITEM_NM");
			exception.setMessage("아이템 코드명은 필수로 들어가야 합니다");
			
			throw exception;
		}
		
		if(item_nm.getBytes(Charset.forName("EUC-KR")).length > 300){
			exception.setFlag("ITEM_NM LENGTH");
			exception.setMessage("아이템 코드명은 300Byte 이내이어야 합니다");
		
			throw exception;
		}
		
		if(ss_gbn.length() > 10){
			exception.setFlag("SS_GBN LENGTH");
			exception.setMessage("SmartUX 타입은 10자 이내이어야 합니다");
		
			throw exception;
		}
		
		if(GlobalCom.containsHangul(ss_gbn)){
			exception.setFlag("SS_GBN LANGUAGE");
			exception.setMessage("SmartUX 타입은 한글이 포함될 수 없습니다.");
			
			throw exception;
		}
		
		if(!(StringUtils.hasText(use_yn))){
			exception.setFlag(SmartUXProperties.getProperty("NOT FOUND USE_YN"));
			exception.setMessage(SmartUXProperties.getProperty("사용여부는 필수로 들어가야 합니다"));
			throw exception;
		}
	}
	
	
	
	/**
	 * SmartUX Item 코드값 수정 처리 작업
	 * @param code				SmartUX Code Item을 수정하고자 하는 code
	 * @param item_code			SmartUX Code Item의 기존 item_code
	 * @param newItem_code		SmartUX Code Item의 새로운 item_code
	 * @param item_nm			SmartUX Code Item의 기존 item_nm
	 * @param newItem_nm		SmartUX Code Item의 새로운 item_nm
	 * @param ss_gbn			Smart Start 관련 코드 아이템 등록할때 들어가지는 스마트스타트 구분 코드
	 * @param app_type			설정 정보 코드 아이템 등록할때 들어가지는 어플타입 코드 
	 * @param use_yn			SmartUX Item의 사용여부(Y/N)
	 * @param smartUXManager	관리자 사이트에 로그인 한 사람의 로그인 id 
	 * @return					처리 결과를 기록한 json 문자열
	 */
	@RequestMapping(value="/admin/code/updateCodeItem", method=RequestMethod.POST)
	public @ResponseBody String updateCodeItem(@RequestParam(value="code") String code, 
			@RequestParam(value="item_code") String item_code, 
			@RequestParam(value="newItem_code") String newItem_code,
			@RequestParam(value="item_nm") String item_nm,
			@RequestParam(value="newItem_nm") String newItem_nm,
			@RequestParam(value="ss_gbn", required=false, defaultValue="") String ss_gbn,
			@RequestParam(value="newSs_gbn", required=false, defaultValue="") String newSs_gbn,
			@RequestParam(value="app_type", required=false, defaultValue="") String app_type,
			@RequestParam(value="use_yn") String use_yn,
			@RequestParam(value="smartUXManager") String login_id) {

		HTMLCleaner cleaner = new HTMLCleaner();
		code 	= cleaner.clean(code);
		item_code 	= cleaner.clean(item_code);
		newItem_code 	= cleaner.clean(newItem_code);
		item_nm 	= cleaner.clean(item_nm);
		newItem_nm 	= cleaner.clean(newItem_nm);
		ss_gbn 	= cleaner.clean(ss_gbn);
		newSs_gbn 	= cleaner.clean(newSs_gbn);
		app_type 	= cleaner.clean(app_type);
		use_yn 	= cleaner.clean(use_yn);
		login_id 	= cleaner.clean(login_id);
		
		logger.debug("code : " + code);
		logger.debug("item_code : " + item_code);
		logger.debug("newItem_code : " + newItem_code);
		logger.debug("item_nm : " + item_nm);
		logger.debug("newItem_nm : " + newItem_nm);
		logger.debug("use_yn : " + use_yn);
		
		String update_id = login_id;			// 수정자 ID(차후에 로그인 한 사람의 로그인 ID로 바꿀것
		String resultcode = "";
		String resultmessage = "";
		try{
			validateUpdateCodeItem(code, item_code, newItem_code, item_nm, newItem_nm, newSs_gbn, use_yn);		// 입력받은 파라미터 값들에 대한 validation 작업 진행
			
			if((item_code.equals(newItem_code)) && (item_nm.equals(newItem_nm)) && (ss_gbn.equals(newSs_gbn))){				// 기존의 코드값, 코드명과 새로이 수정하고자 하는 코드값, 코드명이 같은 경우는 사용여부에 대한 수정만을 의미하므로 바로 수정하는 함수를 실행하도록 한다
				service.updateCodeItem(code, item_code, newItem_code, newItem_nm, newSs_gbn, app_type, use_yn, update_id);
				resultcode = SmartUXProperties.getProperty("flag.success");
				resultmessage = SmartUXProperties.getProperty("message.success");
			}else{
				if(!(item_code.equals(newItem_code))){			// 기존의 코드값과 새로이 입력한 코드값이 다르면 새로이 입력한 코드값이 기존에 존재하는지를 확인한다
					int count = service.getCodeItemcodeCnt(code, newItem_code);
					//Cache 갱신주기는 중복되어도 됨
					if(code.equals(SmartUXProperties.getProperty("code.code6"))){
						count = 0;
					}
					if(count == 0){								// 새로이 입력한 코드값이 존재하지 않으면
						newSs_gbn = newSs_gbn.toUpperCase();
						
						if(!ss_gbn.equals(newSs_gbn)){
							int ssgbnCnt = service.getCodeItemssgbnCnt(newSs_gbn);				// 입력받은 SmartUX 타입(ss_gbn이)이 기존에 존재하는지 확인
							if(ssgbnCnt > 0){
								SmartUXException e = new SmartUXException();
								e.setFlag("EXISTS SS_GBN");
								e.setMessage("SmartUX 타입 존재");
								throw e;
							}
							if(service.getUseSsgbnCnt(ss_gbn) > 0){		//지면에서 사용중인 SmartUX 타입이면 
								SmartUXException e = new SmartUXException();
								e.setFlag("IN USE SS_GBN");
								e.setMessage("사용중인 SmartUX 타입");
								throw e;
							}
						}
						service.updateCodeItem(code, item_code, newItem_code, newItem_nm, newSs_gbn, app_type, use_yn, update_id);
						resultcode = SmartUXProperties.getProperty("flag.success");
						resultmessage = SmartUXProperties.getProperty("message.success");
						
					}else{										// 새로이 입력한 코드값이 존재하면
						SmartUXException e = new SmartUXException();
						e.setFlag("EXISTS ITEM_CODE");
						e.setMessage("item_code");
						throw e;
					}
					
				}else if(!(item_nm.equals(newItem_nm))){		// 기존의 코드명과 새로이 입력한 코드명이 다르면 새로이 입력한 코드명이 기존에 존재하는지를 확인한다
					int count = service.getCodeItemnmCnt(code, newItem_nm);
					//Cache 갱신주기는 중복되어도 됨
					if(code.equals(SmartUXProperties.getProperty("code.code6"))){
						count = 0;
					}
					if(count == 0){								// 새로이 입력한 코드명이 존재하지 않으면
						newSs_gbn = newSs_gbn.toUpperCase();
						if(!ss_gbn.equals(newSs_gbn)){
							int ssgbnCnt = service.getCodeItemssgbnCnt(newSs_gbn);				// 입력받은 SmartUX 타입(ss_gbn이)이 기존에 존재하는지 확인
							if(ssgbnCnt > 0){
								SmartUXException e = new SmartUXException();
								e.setFlag("EXISTS SS_GBN");
								e.setMessage("SmartUX 타입 존재");
								throw e;
							}
							if(service.getUseSsgbnCnt(ss_gbn) > 0){		//지면에서 사용중인 SmartUX 타입이면 
								SmartUXException e = new SmartUXException();
								e.setFlag("IN USE SS_GBN");
								e.setMessage("사용중인 SmartUX 타입");
								throw e;
							}
						}
						service.updateCodeItem(code, item_code, newItem_code, newItem_nm, newSs_gbn, app_type, use_yn, update_id);
						resultcode = SmartUXProperties.getProperty("flag.success");
						resultmessage = SmartUXProperties.getProperty("message.success");
					}else{										// 새로이 입력한 코드명이 존재하면
						SmartUXException e = new SmartUXException();
						e.setFlag("EXISTS ITEM_NM");
						e.setMessage("item_nm");
						throw e;
					}
				}else if(!(ss_gbn.equals(newSs_gbn))){		// 기존의 SmartUX 타입과 새로이 입력한 SmartUX 타입이 다르면 새로이 입력한 SmartUX 타입이 기존에 존재하는지를 확인한다
					if(service.getUseSsgbnCnt(ss_gbn) > 0){		//지면에서 사용중인 SmartUX 타입이면 
						SmartUXException e = new SmartUXException();
						e.setFlag("IN USE SS_GBN");
						e.setMessage("사용중인 SmartUX 타입");
						throw e;
					}
					
					newSs_gbn = newSs_gbn.toUpperCase();
					int ssgbnCnt = service.getCodeItemssgbnCnt(newSs_gbn);				// 입력받은 SmartUX 타입(ss_gbn이)이 기존에 존재하는지 확인
					
					if(ssgbnCnt == 0){													// 입력받은 SmartUX 타입(ss_gbn이)이 기존에 존재하지 않으면
						service.updateCodeItem(code, item_code, newItem_code, newItem_nm, newSs_gbn, app_type, use_yn, update_id);
						resultcode = SmartUXProperties.getProperty("flag.success");
						resultmessage = SmartUXProperties.getProperty("message.success");
					}else{																// 입력받은 SmartUX 타입(ss_gbn이)이 기존에 존재하면
						SmartUXException e = new SmartUXException();
						e.setFlag("EXISTS SS_GBN");
						e.setMessage("SmartUX 타입 존재");
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
	 * updateCodeItem 함수에 대한 validation 작업 함수
	 * @param code				SmartUX Code Item을 수정하고자 하는 code
	 * @param item_code			SmartUX Code Item의 기존 item_code
	 * @param newItem_code		SmartUX Code Item의 새로운 item_code
	 * @param item_nm			SmartUX Code Item의 기존 item_nm
	 * @param newItem_nm		SmartUX Code Item의 새로운 item_nm
	 * @param use_yn			SmartUX Code Item의 사용여부(Y/N)
	 */
	
	private void validateUpdateCodeItem(String code, String item_code, String newItem_code, String item_nm, String newItem_nm, String newSs_gbn, String use_yn){
		
		SmartUXException exception = new SmartUXException();
		
		if(!(StringUtils.hasText(code))){
			exception.setFlag("NOT FOUND CODE");
			exception.setMessage("코드값은 필수로 들어가야 합니다");
			
			throw exception;
		}
		
		if(!(StringUtils.hasText(item_code))){
			exception.setFlag("NOT FOUND ORG_ITEM_CODE");
			exception.setMessage("기존 아이템 코드값은 필수로 들어가야 합니다");
		
			throw exception;
		}
		
		if(!(StringUtils.hasText(newItem_code))){
			exception.setFlag("NOT FOUND NEW_ITEM_CODE");
			exception.setMessage("신규 아이템 코드값은 필수로 들어가야 합니다");
		
			throw exception;
		}
		
		if(newItem_code.getBytes(Charset.forName("EUC-KR")).length > 100){
			exception.setFlag("NEW_ITEM_CODE LENGTH");
			exception.setMessage("신규 아이템 코드값은 100Byte 이내이어야 합니다");
		
			throw exception;
		}
		
		if(!(StringUtils.hasText(item_nm))){
			exception.setFlag("NOT FOUND ORG_ITEM_NM");
			exception.setMessage("기존 아이템 코드명은 필수로 들어가야 합니다");
		
			throw exception;
		}
		
		if(!(StringUtils.hasText(newItem_nm))){
			exception.setFlag("NOT FOUND NEW_ITEM_NM");
			exception.setMessage("신규 아이템 코드명은 필수로 들어가야 합니다");
		
			throw exception;
		}
		
		if(newItem_nm.getBytes(Charset.forName("EUC-KR")).length > 300){
			exception.setFlag("NEW_ITEM_NM LENGTH");
			exception.setMessage("신규 아이템 코드명은 300Byte 이내이어야 합니다");
		
			throw exception;
		}
		
		if(newSs_gbn.length() > 10){
			exception.setFlag("SS_GBN LENGTH");
			exception.setMessage("SmartUX 타입은 10자 이내이어야 합니다");
		
			throw exception;
		}
		
		if(GlobalCom.containsHangul(newSs_gbn)){
			exception.setFlag("SS_GBN LANGUAGE");
			exception.setMessage("SmartUX 타입은 한글이 포함될 수 없습니다.");
			
			throw exception;
		}
		
		if(!(StringUtils.hasText(use_yn))){
			exception.setFlag(SmartUXProperties.getProperty("NOT FOUND USE_YN"));
			exception.setMessage(SmartUXProperties.getProperty("사용여부는 필수로 들어가야 합니다"));
			throw exception;
		}
		
		
	}
	
	/**
	 * SmartUX Item 코드값 삭제 처리 작업
	 * @param code			SmartUX Code Item을 삭제하고자 하는 code	
	 * @param item_codes	SmartUX Code Item의 삭제하고자 하는 item_code들의 문자열 배열
	 * @return				처리 결과를 기록한 json 문자열
	 */
	@RequestMapping(value="/admin/code/deleteCodeItem", method=RequestMethod.POST)
	public @ResponseBody String deleteCodeItem(@RequestParam(value="code") String code, 
			@RequestParam(value="item_code[]") String [] item_codes,
			@RequestParam(value="ss_gbn[]") String [] ss_gbns) {
		
		HTMLCleaner cleaner = new HTMLCleaner();
		code 	= cleaner.clean(code);
		
		String resultcode = "";
		String resultmessage = "";
		try{
			for(int i=0;i<ss_gbns.length;i++){
				int count = service.getUseSsgbnCnt(ss_gbns[i]);
				if(count > 0){
					SmartUXException e = new SmartUXException();
					e.setFlag("IN USE SS_GBN");
					e.setMessage(item_codes[i]);
					throw e;
				}
			}
			validateDeleteCodeItem(code, item_codes);				// 입력받은 파라미터 값들에 대한 validation 작업 진행
			service.deleteCodeItem(code, item_codes);
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
	 * deleteCodeItem 함수에 대한 validation 작업 함수
	 * @param code			SmartUX Code Item을 삭제하고자 하는 code	
	 * @param item_codes	SmartUX Code Item의 삭제하고자 하는 item_code들의 문자열 배열
	 */
	
	private void validateDeleteCodeItem(String code, String [] item_codes){
		
		SmartUXException exception = new SmartUXException();
		
		if(!(StringUtils.hasText(code))){
			exception.setFlag("NOT FOUND CODE");
			exception.setMessage("코드값은 필수로 들어가야 합니다");
			throw exception;
		}
		
		if((item_codes == null) ||(item_codes.length == 0)){
			exception.setFlag("NOT FOUND ITEM_CODE");
			exception.setMessage("아이템 코드값은 필수로 들어가야 합니다");
			throw exception;
		}
		
	}
	
	/**
	 * SmartUX Item 순서바꾸기 화면
	 * @param code		SmartUX Item을 조회하고자 하는 code
	 * @param model		Model 객체
	 * @return			SmartUX Item 코드값 목록 조회 화면 URL
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/code/changeCodeItemOrder", method=RequestMethod.GET)
	public String changeCodeItemOrder(@RequestParam(value="code", defaultValue="") String code, Model model) throws Exception{
		
		HTMLCleaner cleaner = new HTMLCleaner();
		code 	= cleaner.clean(code);
		
		List<CodeItemVO> result = null;
		
		result = service.getCodeItemList(code);
		model.addAttribute("code", code);
		model.addAttribute("result", result);
		
		return "/admin/code/changeCodeItemOrder";
		
	}
	
	/**
	 * SmartUX Item 순서바꾸기 작업
	 * @param code				순서를 바꾸고자 하는 SmartUX Item의 code 
	 * @param item_codes		순서를 바꾸고자 하는 SmartUX Item의 item_code들의 문자열 배열
	 * @param smartUXManager	관리자 사이트에 로그인 한 사람의 로그인 id
	 * @return					처리 결과를 기록한 json 문자열
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/code/changeCodeItemOrder", method=RequestMethod.POST)
	public @ResponseBody String changeCodeItemOrder(@RequestParam(value="code") String code
			, @RequestParam(value="item_code[]") String [] item_codes
			, @RequestParam(value="smartUXManager") String login_id) {
		
		HTMLCleaner cleaner = new HTMLCleaner();
		code 	= cleaner.clean(code);
		login_id 	= cleaner.clean(login_id);
		
		String update_id = login_id;			// 수정자 ID(차후에 로그인 한 사람의 로그인 ID로 바꿀것
		
		String resultcode = "";
		String resultmessage = "";
		try{
			validateChangeCodeItemOrder(code, item_codes);						// 입력받은 파라미터 값들에 대한 validation 작업 진행
			service.changeCodeItemOrder(code, item_codes, update_id);
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
	 * changeCodeItemOrder 함수에 대한 validation 작업 함수
	 * @param code			순서를 바꾸고자 하는 SmartUX Item의 code 
	 * @param item_codes	순서를 바꾸고자 하는 SmartUX Item의 item_code들의 문자열 배열
	 */
	
	private void validateChangeCodeItemOrder(String code, String [] item_codes){
		
		SmartUXException exception = new SmartUXException();
		
		if(!(StringUtils.hasText(code))){
			exception.setFlag("NOT FOUND CODE");
			exception.setMessage("코드값은 필수로 들어가야 합니다");
			throw exception;
		}
		
		if((item_codes == null) ||(item_codes.length == 0)){
			exception.setFlag("NOT FOUND ITEM_CODE");
			exception.setMessage("아이템 코드값은 필수로 들어가야 합니다");
			throw exception;
		}
		
	}
	
}
