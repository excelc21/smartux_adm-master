package com.dmi.smartux.admin.quality.controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.dmi.commons.consts.ResultCode;
import com.dmi.commons.exception.MimsCommonException;
import com.dmi.commons.factory.FileProcessFunction;
import com.dmi.commons.factory.FileProcessLockFactory;
import com.dmi.commons.model.RestfulCodeMsg;
import com.dmi.commons.utility.HttpClientUtils;
import com.dmi.commons.utility.RestfulUtils;
import com.dmi.smartux.admin.quality.service.QualityAdminService;
import com.dmi.smartux.admin.quality.vo.AddQualityMemberVo;
import com.dmi.smartux.admin.quality.vo.QualityListVo;
import com.dmi.smartux.admin.quality.vo.QualitySearchVo;
import com.dmi.smartux.admin.quality.vo.ViewQualityMemberVo;
import com.dmi.smartux.common.exception.ExceptionHandler;
import com.dmi.smartux.common.exception.SmartUXException;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.CLog;
import com.dmi.smartux.common.util.CookieUtil;
import com.dmi.smartux.common.util.ExcelManager;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.util.HTMLCleaner;
import com.dmi.smartux.common.vo.CUDResult;

import net.sf.json.JSONSerializer;

@Controller
public class QualityAdminController {

	@Autowired
	QualityAdminService service;
	
	public static String chacheYn = "Y";
	
	private final Log logger = LogFactory.getLog(this.getClass());

	private final Log applyLogger = LogFactory.getLog("adminApplyCacheHistroy");
	
	/**
	 * 인터페이스 서버에 캐시 동기화 요청
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/quality/qualityApplyCache", method=RequestMethod.POST )
	public ResponseEntity<String> qualityApplyCache(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value="callByScheduler",  defaultValue="Y") final  String callByScheduler
			) throws Exception{
		
		final com.dmi.commons.logger.CLog cLog = new com.dmi.commons.logger.CLog(applyLogger, request.getRemoteAddr(), request.getRequestURI(), request.getMethod());
		final String loginUser = callByScheduler.equals("Y") ? "batch" : CookieUtil.getCookieUserID(request);
		final CUDResult result = new CUDResult();
		
		try {
			cLog.startLog("품질대상단말정보 즉시적용 :", callByScheduler, loginUser);
			final String host = SmartUXProperties.getProperty("cache.sync.public.host");
			final String port = SmartUXProperties.getProperty("cache.sync.public.port");
			
			String fileLockPath = SmartUXProperties.getProperty("SmartUXQualityDao.refreshQuality.fileLock");
			int waitTime = NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.wait"), 5);
			
			FileProcessLockFactory.start(fileLockPath, waitTime,  new FileProcessFunction("품질대상단말정보 즉시적용", ResultCode.ApplyRequestFail) {
				
				@Override
				public void run() throws Exception {
					
					//즉시적용 호출 시 sleep 값 추가 -> DB분리건으로 OGG가 10초 이상 걸리는 경우가 있어 즉시적용 시 반영이 안돼서 추가함
					Thread.sleep(Integer.parseInt(org.apache.commons.lang.StringUtils.defaultIfEmpty(SmartUXProperties.getProperty("apply.cache.call.sleep"), "10000")));
					
					RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
							host, 
							port, 
							SmartUXProperties.getProperty("SmartUXQualityDao.refreshQuality.url")+"?callByScheduler="+callByScheduler,
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.method"), "POST"), 
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"), 
							MediaType.APPLICATION_JSON.toString(),
							NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.timeout"), 60000)).getResponseBody());
					
					result.setFlag(codeMsg.getCode());
					result.setMessage(codeMsg.getMessage());
					cLog.middleLog("품질대상단말정보 즉시적용", "API 호출", codeMsg.getCode(), codeMsg.getMessage());
				}
			});
		} catch (MimsCommonException e) {
			cLog.warnLog("품질대상단말정보 즉시적용", "실패", e.getFlag(), e.getMessage(), e.getLocalizedMessage());
			result.setFlag(e.getFlag());
			result.setMessage(e.getMessage());
		} finally {
			cLog.endLog("품질대상단말정보 즉시적용", loginUser, result.getFlag(), result.getMessage());
		}
		
		String resultMsg = "{\"flag\" : \"" + result.getFlag() + "\", \"message\" : \"" + result.getMessage() + "\"}";
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(resultMsg).toString(), responseHeaders, HttpStatus.OK);
	}
	

	/**
	 * 품질 대상 내역이 수정되었으니 리스트 접근 시 리캐싱하라고 명령(여러개 품질대상을 추가한다고 하면 그때마다 여러대의 장비를 리캐싱할필요 없이 이렇게 한다.)
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/admin/quality/cache", method=RequestMethod.GET)
	public ResponseEntity<String> refreshAdminCacheOfQuality(
		HttpServletRequest request
		, HttpServletResponse response
	) {
		//#########[LOG START]#########
		CLog cLog = new CLog(logger, request);
		cLog.startLog( "QualityMemberCacheCall" );

		String resultcode = SmartUXProperties.getProperty("flag.success");
		String resultmessage = SmartUXProperties.getProperty("message.success");
		
		try {
			chacheYn = "Y";
		} catch(Exception e) {
			cLog.errorLog("QualityMemberCacheCall");
			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}

		//#########[LOG END]#########
		cLog.endLog( "QualityMemberCacheCall");

		String result = "{\"result\":{\"message\":\""+resultmessage+"\",\"flag\":\""+resultcode+"\"}}";
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(result).toString(), responseHeaders, HttpStatus.CREATED);
	}
	
	/**
	 * 자신의 서버만 데이터 캐싱한다.(URL호출로 사용하기 위해 만들어 놓음..)
	 * @param request
	 * @param response
	 * @param callByScheduler
	 * @return
	 */
	@RequestMapping(value="/admin/quality/hide/cache", method=RequestMethod.GET)
	public ResponseEntity<String> refreshAdminCacheOfQualityHide(
		HttpServletRequest request
		, HttpServletResponse response
		, @RequestParam(value="callByScheduler", required=false, defaultValue="N") String callByScheduler
	) {
		//#########[LOG START]#########
		CLog cLog = new CLog(logger, request);
		cLog.startLog( "["+GlobalCom.isNull(callByScheduler)+"]" );

		String resultcode = SmartUXProperties.getProperty("flag.success");
		String resultmessage = SmartUXProperties.getProperty("message.success");
		
		try {
			service.refreshAdminCacheOfQuality();
		} catch(Exception e) {
			cLog.errorLog("["+GlobalCom.isNull(callByScheduler)+"]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}

		//#########[LOG END]#########
		cLog.endLog( "["+GlobalCom.isNull(callByScheduler)+"] ["+resultcode+"]");

		String result = "{\"result\":{\"message\":\""+resultmessage+"\",\"flag\":\""+resultcode+"\"}}";
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(result).toString(), responseHeaders, HttpStatus.CREATED);
	}
	

	@RequestMapping(value="/admin/quality/qualityMemberList", method=RequestMethod.GET )
	public String getQualityMemberList(
			@RequestParam(value="findName", required=false, defaultValue="") String findName,
			@RequestParam(value="findValue", required=false, defaultValue="") String findValue,
			@RequestParam(value="s_log_type", required=false, defaultValue="") String s_log_type,
			@RequestParam(value="pageNum", required=false, defaultValue="1") String pageNum,
			@RequestParam(value="serviceType", required=false, defaultValue="") String serviceType,
			Model model
			){
		
			HTMLCleaner cleaner = new HTMLCleaner();
			findName = cleaner.clean(findName);
			findValue = cleaner.clean(findValue);
			s_log_type = cleaner.clean(s_log_type);
			pageNum = cleaner.clean(pageNum);
			serviceType = cleaner.clean(serviceType);
			
			QualityListVo qualitylistVo = new QualityListVo();
			
			pageNum = GlobalCom.isNull(pageNum, "1");
			
			qualitylistVo.setFindName(findName);
			qualitylistVo.setFindValue(findValue);
			qualitylistVo.setPageNum(Integer.parseInt(pageNum));
			
			qualitylistVo.setPageSize(GlobalCom.isNumber(qualitylistVo.getPageSize(),10));
			qualitylistVo.setPageNum(GlobalCom.isNumber(qualitylistVo.getPageNum(),1));
			qualitylistVo.setS_log_type(GlobalCom.isNull(s_log_type,""));
			
			qualitylistVo.setFindName(GlobalCom.isNull(qualitylistVo.getFindName(), "sa_id"));
			qualitylistVo.setFindValue(GlobalCom.isNull(qualitylistVo.getFindValue()));
			
			qualitylistVo.setBlockSize(GlobalCom.isNumber(qualitylistVo.getBlockSize(),10));
			
			//포터블 TV 타입 추가(P)
			qualitylistVo.setServiceType(serviceType);
			try {
				qualitylistVo = service.getCacheQualityMemberList(qualitylistVo, chacheYn);
				model.addAttribute("vo", qualitylistVo);
			} catch (Exception e) {
				//logger.error("getAdminList "+e.getClass().getName()); 
				//logger.error("getAdminList "+e.getMessage());
				logger.error("[getQualityMemberList]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			}
			
			return "/admin/quality/qualityMemberList";
	}
	
	@RequestMapping(value="/admin/quality/qualityMemberInsert", method=RequestMethod.GET )
	public String qualityMemberInsert(
			@RequestParam(value="findName", required=false, defaultValue="") String findName,
			@RequestParam(value="findValue", required=false, defaultValue="") String findValue,
			@RequestParam(value="s_log_type", required=false, defaultValue="") String s_log_type,
			@RequestParam(value="pageNum", required=false, defaultValue="1") String pageNum,
			@RequestParam(value="serviceType", required=false, defaultValue="") String serviceType,
			Model model){
		
			HTMLCleaner cleaner = new HTMLCleaner();
			findName = cleaner.clean(findName);
			findValue = cleaner.clean(findValue);
			s_log_type = cleaner.clean(s_log_type);
			pageNum = GlobalCom.isNull(cleaner.clean(pageNum), "1");
			
			QualitySearchVo qualitysearchVo = new QualitySearchVo();
			qualitysearchVo.setFindName(findName);
			qualitysearchVo.setFindValue(findValue);
			qualitysearchVo.setS_log_type(s_log_type);
			qualitysearchVo.setPageNum(Integer.parseInt(pageNum));
			qualitysearchVo.setServiceType(serviceType);
			
			model.addAttribute("vo", qualitysearchVo);
			
			return "/admin/quality/qualityMemberInsert";
	}
	
	@RequestMapping(value="/admin/quality/qualityMemberInsert", method=RequestMethod.POST )
	public String qualityMemberInsert(
			AddQualityMemberVo addqualitymemberVo,
			@RequestParam(value="findName", required=false, defaultValue="") String findName,
			@RequestParam(value="findValue", required=false, defaultValue="") String findValue,
			@RequestParam(value="s_log_type", required=false, defaultValue="") String s_log_type,
			@RequestParam(value="pageNum", required=false, defaultValue="1") String pageNum,
			@RequestParam(value="serviceType", required=false, defaultValue="") String serviceType,
			HttpServletRequest request,
			Model model){
		
			HTMLCleaner cleaner = new HTMLCleaner();
			findName = cleaner.clean(findName);
			findValue = cleaner.clean(findValue);
			s_log_type = cleaner.clean(s_log_type);
			pageNum = GlobalCom.isNull(cleaner.clean(pageNum), "1");
			serviceType = cleaner.clean(serviceType);
			
			QualitySearchVo qualitysearchVo = new QualitySearchVo();
			qualitysearchVo.setFindName(findName);
			qualitysearchVo.setFindValue(findValue);
			qualitysearchVo.setS_log_type(s_log_type);
			qualitysearchVo.setPageNum(Integer.parseInt(pageNum));
			qualitysearchVo.setServiceType(serviceType);
			model.addAttribute("vo", qualitysearchVo);
			
			String success = "Y";
			try{
				String cookieID = CookieUtil.getCookieUserID(request);
				String userIp = request.getRemoteAddr();
				
				service.addQualityMember(addqualitymemberVo, cookieID, userIp, serviceType);
				
			}catch(SmartUXException hex){
				success = "N";
			}catch(java.lang.Exception e){
				success = "X";
				logger.error("[qualityMemberInsert-POST]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			}

			model.addAttribute("success", success);
			
			return "/admin/quality/qualityMemberInsertProc";
	}
	
	@RequestMapping(value="/admin/quality/qualityMemberUpdate", method=RequestMethod.GET )
	public String qualityMemberUpdate(
			@RequestParam(value="file_id", required=false, defaultValue="") String file_id,
			@RequestParam(value="findName", required=false, defaultValue="") String findName,
			@RequestParam(value="findValue", required=false, defaultValue="") String findValue,
			@RequestParam(value="s_log_type", required=false, defaultValue="") String s_log_type,
			@RequestParam(value="pageNum", required=false, defaultValue="1") String pageNum,
			@RequestParam(value="serviceType", required=false, defaultValue="") String serviceType,
			Model model){
		
			HTMLCleaner cleaner = new HTMLCleaner();
			findName = cleaner.clean(findName);
			findValue = cleaner.clean(findValue);
			s_log_type = cleaner.clean(s_log_type);
			pageNum = GlobalCom.isNull(cleaner.clean(pageNum), "1");
			serviceType = cleaner.clean(serviceType);
			
			QualitySearchVo qualitysearchVo = new QualitySearchVo();
			qualitysearchVo.setFile_id(file_id);
			qualitysearchVo.setFindName(findName);
			qualitysearchVo.setFindValue(findValue);
			qualitysearchVo.setS_log_type(s_log_type);
			qualitysearchVo.setPageNum(Integer.parseInt(pageNum));
			qualitysearchVo.setServiceType(serviceType);
			model.addAttribute("vo", qualitysearchVo);
			
			try{
				ViewQualityMemberVo viewqualitymemberVo = service.viewQualityMember(file_id, serviceType);
				model.addAttribute("viewVo", viewqualitymemberVo);
			}catch(java.lang.Exception e){
				logger.error("[qualityMemberUpdate-GET]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			}
			
			return "/admin/quality/qualityMemberUpdate";
	}
	
	@RequestMapping(value="/admin/quality/qualityMemberUpdate", method=RequestMethod.POST )
	public String qualityMemberUpdate(
			AddQualityMemberVo addqualitymemberVo,
			@RequestParam(value="file_id", required=false, defaultValue="") String file_id,
			@RequestParam(value="findName", required=false, defaultValue="") String findName,
			@RequestParam(value="findValue", required=false, defaultValue="") String findValue,
			@RequestParam(value="s_log_type", required=false, defaultValue="") String s_log_type,
			@RequestParam(value="pageNum", required=false, defaultValue="1") String pageNum,
			@RequestParam(value="serviceType", required=false, defaultValue="") String serviceType,
			HttpServletRequest request,
			Model model){
		
			HTMLCleaner cleaner = new HTMLCleaner();
			findName = cleaner.clean(findName);
			findValue = cleaner.clean(findValue);
			s_log_type = cleaner.clean(s_log_type);
			pageNum = GlobalCom.isNull(cleaner.clean(pageNum), "1");
			serviceType = cleaner.clean(serviceType);
			
			try{
				String cookieID = CookieUtil.getCookieUserID(request);
				String userIp = request.getRemoteAddr();
				
				service.modifyQualityMember(addqualitymemberVo, cookieID, userIp, serviceType);
			}catch(java.lang.Exception e){
				logger.error("[qualityMemberUpdate-POST]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			}

			String domain = SmartUXProperties.getProperty("smartux.domain.http");
			
			model.addAttribute("findName", findName);
			model.addAttribute("findValue", findValue);
			model.addAttribute("pageNum", pageNum);
			model.addAttribute("s_log_type", s_log_type);
			model.addAttribute("serviceType", serviceType);
			return "redirect:"+domain+"/smartux_adm/admin/quality/qualityMemberList.do";
	}

	@RequestMapping(value="/admin/quality/qualityMemberDelete", method=RequestMethod.POST )
	public ResponseEntity<String> qualityMemberDelete(
			@RequestParam(value="file_id", required=false, defaultValue="") String file_id,
			@RequestParam(value="serviceType", required=false, defaultValue="") String serviceType,
			HttpServletRequest request,
			Model model
			) throws Exception{
			
			String result = "";
			String resultcode = "";
			String resultmessage = "";
			
			try{
				service.deleteQualityMember(file_id, serviceType);
				resultcode = SmartUXProperties.getProperty("flag.success");
				resultmessage = SmartUXProperties.getProperty("message.success");
			} catch (Exception e) {
				logger.error("[qualityMemberDelete]["+e.getClass().getName()+"]["+e.getMessage()+"]");		
				
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
	 * 엑셀 업로드
	 * @param excelFile
	 * @param serviceType
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/quality/qualityMemberExcelUpload", method=RequestMethod.POST )
	public ResponseEntity<String> qualityMemberExcelUpload(
			@RequestParam(value="excelFile", required=false, defaultValue="") MultipartFile excelFile,
			@RequestParam(value="serviceType", required=false, defaultValue="") String serviceType,
			HttpServletRequest request,
			Model model
			) throws Exception{
		
		String result = "";
		String resultcode = "";
		String resultmessage = "";
		
		HTMLCleaner cleaner = new HTMLCleaner();
		
		try{
			int successNum = 0;
			int failNum = 0;
			if (excelFile.getSize() != 0L) {
				ExcelManager excelManager = new ExcelManager(excelFile.getInputStream());
				List<List<Object>> excelData = excelManager.readExcel();
				if(excelData.size()==0){
					resultcode = "2";
					resultmessage = "데이터 없음";
					logger.info("[qualityMemberExcelUpload][excelData 0]");
				}else{
					int rowCnt = 0;
					List<AddQualityMemberVo> qltmemarrVo = new ArrayList<AddQualityMemberVo>();
					AddQualityMemberVo addqualitymemberVo = null;
					for(List<Object> cellData : excelData){
						if(rowCnt++==0) continue;//첫번째 데이터는 타이틀
						
						String saId = "";
						String logType = "";
						String size = "";
						String findType = "";
						String logLevel = "";
						
						if(cellData.size()>=5){
							try{
								saId = cleaner.clean((String)cellData.get(0));
								logType = GlobalCom.isNullNumber(cleaner.clean(changeFormatString(cellData.get(1))));
								size = GlobalCom.isNull(cleaner.clean(changeFormatString(cellData.get(2))),"5");
								String tmpFindType = cleaner.clean((String)cellData.get(3));
								String excelLogLevel = GlobalCom.isNull(cleaner.clean(changeFormatString(cellData.get(4))), "3");
								
								if(saId.length() > 20){
									logger.info("[qualityMemberExcelUpload][SA_ID MaxSize Over.]["+saId+"]");
									continue;
								}
								else if(logType.length() > 2){
									logger.info("[qualityMemberExcelUpload][LogType MaxSize Over.]["+saId+"]");
									continue;
								}
								else if(size.length() > 5){
									logger.info("[qualityMemberExcelUpload][LogSize MaxSize Over.]["+saId+"]");
									continue;
								}
								if(GlobalCom.containsHangul(saId)){
									logger.info("[qualityMemberExcelUpload][SA_ID Hangul Data]["+saId+"]");
									continue;
								}
								
								if("P".equals(serviceType)){
									boolean flag = false;
									if(StringUtils.isNotBlank(excelLogLevel)){
										String logLevelStr = SmartUXProperties.getProperty("quality.loglevel.list");
										
										if(StringUtils.isNotBlank(logLevelStr)){
											String[] logLevelarr = logLevelStr.split("\\|");
											if(logLevelarr != null){
												for(String data : logLevelarr){
													if(excelLogLevel.equals(data)){
														flag = true;
														break;
													}
												}
											}
										}
										
										if(!flag){
											logger.info("[qualityMemberExcelUpload][LOG LEVEL] [유효하지 않은 값입니다.]["+excelLogLevel+"]");
											continue;
										}
									}else{
										logger.info("[qualityMemberExcelUpload][LOG LEVEL] [빈값입니다.]["+excelLogLevel+"]");
										continue;
									}
								}
								
								//int형 체크
								int intLogType = Integer.parseInt(logType);
								int intSize = Integer.parseInt(size);
								
								if("default".equalsIgnoreCase(tmpFindType)){
									findType = "DE";
								}else if("prefix".equalsIgnoreCase(tmpFindType)){
									findType = "PR";
								}else if("postfix".equalsIgnoreCase(tmpFindType)){
									findType = "PO";
								}else{
									findType = "";
								}
								
								//2019.01.31 포터블 TV -> 로그레벨 추가 
								if(StringUtils.isNotBlank(excelLogLevel)){
									if("Verbose".equalsIgnoreCase(excelLogLevel)){
										logLevel="1";
									}else if("Debug".equalsIgnoreCase(excelLogLevel)){
										logLevel="2";
									}else if("Info".equalsIgnoreCase(excelLogLevel)){
										logLevel="3";
									}else if("Warning".equalsIgnoreCase(excelLogLevel)){
										logLevel="4";
									}else if("Error".equalsIgnoreCase(excelLogLevel)){
										logLevel="5";
									}else {
										logLevel="";
									}
								}
							}catch(java.lang.Exception e){
								logger.info("[qualityMemberExcelUpload][type is not valid]["+e.getClass().getName()+"]["+e.getMessage()+"]");
							}
						}else{
							logger.info("[qualityMemberExcelUpload][cellData:"+cellData.size()+"]");
						}
						
						if(!"".equals(saId) && !"".equals(logType) && !"".equals(size) && !"".equals(findType)){
							addqualitymemberVo = new AddQualityMemberVo();
							addqualitymemberVo.setSa_id(saId);
							addqualitymemberVo.setLog_type(logType);
							addqualitymemberVo.setSize(size);
							addqualitymemberVo.setFind_type(findType);
							
							if("P".equals(serviceType)){
								addqualitymemberVo.setLog_level(logLevel);
							}
							
							qltmemarrVo.add(addqualitymemberVo);
							successNum++; 
						}else{
							logger.info("[qualityMemberExcelUpload][type is not valid]");
							failNum++;
						}
					}
					
					String cookieID = CookieUtil.getCookieUserID(request);
					String userIp = request.getRemoteAddr();
					if(qltmemarrVo.size()==0){
						resultcode = "2";
						resultmessage = "데이터 없음";
						logger.info("[qualityMemberExcelUpload][excelArray 0]");
					}else{
						int duplNum = service.addQualityMemberList(qltmemarrVo, cookieID, userIp, serviceType);
						successNum = successNum - duplNum;

						resultcode = "1";
						resultmessage = "Success:"+successNum+" Fail:"+failNum+" Dupl:"+duplNum;
						
						logger.info("[qualityMemberExcelUpload][Success "+successNum+"][Fail "+failNum+"][Dupl "+duplNum+"]");
					}
				}
			}
		}catch(java.lang.Exception e){
			logger.error("[qualityMemberExcelUpload][ExcelParsing Fail]["+e.getClass().getName()+"]["+e.getMessage()+"]");	
			resultcode = "3";
			resultmessage = "처리에 문제 발생";
		}
		
		//동기화 요청
		service.refreshAdminCacheOfQuality();
		
		result = "{\"result\":{\"message\":\""+resultmessage+"\",\"flag\":\""+resultcode+"\"}}";
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(result).toString(), responseHeaders, HttpStatus.CREATED);	
	}

    @ResponseBody
    @RequestMapping(value = "/admin/quality/downloadQualityExcel", method = RequestMethod.GET)
    public byte[] downloadResultFile(HttpServletResponse response, Model model) throws Exception {
        List<Object> header = new ArrayList<Object>();

        String[] headerAry = SmartUXProperties.getProperty("quality.excel.header").split("\\|");
        for (String str : headerAry) {
            header.add(str);
        }
    	
        List<List<Object>> data = new ArrayList<List<Object>>();
        List<Object> obj = new ArrayList<Object>();
        obj.add("M00000000000");
        obj.add("1");
        obj.add("5");
        obj.add("default");
        obj.add("Info");
        data.add(obj);
        obj = new ArrayList<Object>();
        obj.add("M00000000000");
        obj.add("2");
        obj.add("10");
        obj.add("prefix");
        obj.add("Warning ");
        data.add(obj);
        obj = new ArrayList<Object>();
        obj.add("M00000000000");
        obj.add("3");
        obj.add("15");
        obj.add("postfix");
        obj.add("Error");
        data.add(obj);

        ExcelManager excelManager = new ExcelManager(header, data);
        excelManager.setSheetName("quality_format");
        excelManager.setWidth(6000);
        byte[] bytes = excelManager.makeExcel();

        response.setHeader("Content-Disposition", "attachment; filename=quality_format.xlsx");
        response.setContentLength(bytes.length);
        response.setContentType("application/vnd.ms-excel");

        return bytes;
    }
    
    private String changeFormatString(Object obj){
    	String ret = "";
    	
		if(obj instanceof Integer){
			ret = Integer.toString((Integer)obj);
		}else if(obj instanceof Double){
			ret = String.format("%.0f" , (Double)obj);
		}else{
			ret = (String)obj;
		}
    	
    	return ret;
    }
	
}
