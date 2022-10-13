package com.dmi.smartux.admin.guidelink.controller;

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

import com.dmi.commons.consts.ResultCode;
import com.dmi.commons.exception.MimsCommonException;
import com.dmi.commons.factory.FileProcessFunction;
import com.dmi.commons.factory.FileProcessLockFactory;
import com.dmi.commons.logger.CLog;
import com.dmi.commons.model.RestfulCodeMsg;
import com.dmi.commons.utility.HttpClientUtils;
import com.dmi.commons.utility.RestfulUtils;
import com.dmi.smartux.admin.guidelink.service.GuideLinkService;
import com.dmi.smartux.admin.guidelink.vo.GuideLinkVo;
import com.dmi.smartux.common.exception.ExceptionHandler;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.CookieUtil;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.util.HTMLCleaner;
import com.dmi.smartux.common.vo.CUDResult;

import net.sf.json.JSONSerializer;

@Controller
public class GuideLinkController {

	private final Log logger = LogFactory.getLog(this.getClass());
	private final Log applyLogger = LogFactory.getLog("adminApplyCacheHistroy");

	@Autowired
	GuideLinkService service;

	@SuppressWarnings("static-access")
	@RequestMapping(value = "/admin/guidelink/guideLinkList", method = RequestMethod.GET)
	public String getGuideLinkList(Model model,
			@RequestParam(value = "type", required = false, defaultValue = "") String type,
			@RequestParam(value = "search_text", required = false, defaultValue = "") String search_text,
			@RequestParam(value="pageNum", required=false, defaultValue="1") String pageNum)
			throws Exception {

		GuideLinkVo param = new GuideLinkVo();
		param.setType(type);
		param.setSearch_text(search_text);
		
		HTMLCleaner cleaner = new HTMLCleaner();
		pageNum = cleaner.clean(pageNum);
		
		logger.debug("guideLinkList = pageNum : " + pageNum);
		
		//페이지 
		param.setPageSize(GlobalCom.isNumber(param.getPageSize(),10));
		param.setBlockSize(GlobalCom.isNumber(param.getBlockSize(),10));
		param.setPageNum(GlobalCom.isNumber(Integer.parseInt(GlobalCom.isNull(pageNum, "1")),1));
		param.setPageCount(service.getGuideLinkListCnt(param));

		List<GuideLinkVo> list = service.getGuideLinkList(param);
		
		model.addAttribute("list", list);
		model.addAttribute("type", type);
		model.addAttribute("search_text", search_text);
		model.addAttribute("vo", param);

		return "/admin/guidelink/guideLinkList";
	}
	
	
	/**
	* 가이드채널 등록 페이지
	* 
	* @throws Exception
	*/

	@RequestMapping(value="/admin/guidelink/insertGuideLink", method=RequestMethod.GET)
	public String insertGuideLink(Model model) throws Exception {
	
		model.addAttribute("type", "insert");
		
		return "/admin/guidelink/insertGuideLink";
	}

	
	/**
	* 가이드채널 등록 ajax
	* 
	* @throws Exception
	*/
	@RequestMapping(value="/admin/guidelink/insertGuideLink", method=RequestMethod.POST)
	public ResponseEntity<String> insertGuideLink(GuideLinkVo guideLinkVo
		, HttpServletRequest request
		, Model model
		) throws Exception{
	
		String resCode = "";
		String resMessage = "";
		
		try {
			
			String result_code = service.insertGuideLink(guideLinkVo);
			if("0000".equals(result_code)) {
				resCode = SmartUXProperties.getProperty("flag.success");
				resMessage = SmartUXProperties.getProperty("message.success");
			}else {
				resCode = result_code;
			}
		} catch(Exception e) {
			logger.error("[insertGuideLink]["+e.getClass().getName()+"]["+e.getMessage()+"]");	
			e.printStackTrace();
			ExceptionHandler handler = new ExceptionHandler(e);
			resCode = handler.getFlag();
			resMessage = handler.getMessage();
		}
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>("{\"flag\" : \"" + resCode + "\", \"message\" : \"" + resMessage + "\"}", responseHeaders, HttpStatus.CREATED);
	}
	
	/**
	* 가이드채널 수정 페이지
	* 
	* @throws Exception
	*/
	@RequestMapping(value="/admin/guidelink/updateGuideLink", method=RequestMethod.GET)
	public String updateGuideLink(@RequestParam(value="seq", required=false, defaultValue="") String seq,
		@RequestParam(value="type_filter_str", required=false, defaultValue="") String type_filter_str,
		@RequestParam(value="view", required=false, defaultValue="") String view,
		Model model) throws Exception {
	
		GuideLinkVo vo = service.getGuideLinkDetail(seq);
		
		model.addAttribute("type", "update");
		model.addAttribute("detail", vo);
		model.addAttribute("linkType", vo.getLinkType());
		model.addAttribute("seq", seq);
	
		return "/admin/guidelink/insertGuideLink";
	}
	
	/**
	* 가이드채널 수정 ajax
	* 
	* @throws Exception
	*/
	@RequestMapping(value="/admin/guidelink/updateGuideLink", method=RequestMethod.POST)
	public ResponseEntity<String> updateGuideLink(GuideLinkVo guideLinkVo
		, HttpServletRequest request
		, Model model
		) throws Exception{
	
		String resCode = "";
		String resMessage = "";
		
		try {
			
			String result_code = service.updateGuideLink(guideLinkVo);
			if("0000".equals(result_code)) {
				resCode = SmartUXProperties.getProperty("flag.success");
				resMessage = SmartUXProperties.getProperty("message.success");
			}else {
				resCode = result_code;
			}
		} catch(Exception e) {
			logger.error("[updateGuideLink]["+e.getClass().getName()+"]["+e.getMessage()+"]");	
			e.printStackTrace();
			ExceptionHandler handler = new ExceptionHandler(e);
			resCode = handler.getFlag();
			resMessage = handler.getMessage();
		}
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>("{\"flag\" : \"" + resCode + "\", \"message\" : \"" + resMessage + "\"}", responseHeaders, HttpStatus.CREATED);
	}
	
	/**
	* 가이드채널 삭제 ajax
	* 
	* @throws Exception
	*/
	@RequestMapping(value="/admin/guidelink/deleteGuideLink", method=RequestMethod.POST)
	public ResponseEntity<String> deleteGuideLink(GuideLinkVo guideLinkVo
		, HttpServletRequest request
		, Model model
		) throws Exception{
	
		String resCode = "";
		String resMessage = "";
		
		try {
			
			String result_code = service.deleteGuideLink(guideLinkVo);
			if("0000".equals(result_code)) {
				resCode = SmartUXProperties.getProperty("flag.success");
				resMessage = SmartUXProperties.getProperty("message.success");
			}else {
				resCode = result_code;
			}
		} catch(Exception e) {
			logger.error("[updateGuideLink]["+e.getClass().getName()+"]["+e.getMessage()+"]");	
			e.printStackTrace();
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
	@RequestMapping(value="/admin/guidelink/applyCache", method=RequestMethod.POST)
	public ResponseEntity<String> applyCache(
		HttpServletRequest request, 
		HttpServletResponse response
		) throws Exception {
	
		final CLog cLog = new CLog(applyLogger, request.getRemoteAddr(), request.getRequestURI(), request.getMethod());
		final String loginUser = CookieUtil.getCookieUserID(request);
		final CUDResult result = new CUDResult();
		
		try {
			cLog.startLog("가이드채널 즉시적용", loginUser);
			final String host = SmartUXProperties.getProperty("cache.sync.public.host");
			final String port = SmartUXProperties.getProperty("cache.sync.public.port");
			
			String fileLockPath = SmartUXProperties.getProperty("GuideLinkDao.refreshGuideLinkList.fileLock");
			int waitTime = NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.wait"), 5);
			
			FileProcessLockFactory.start(fileLockPath, waitTime,  new FileProcessFunction("가이드채널 즉시적용", ResultCode.ApplyRequestFail) {
				
				@Override
				public void run() throws Exception {
					
					RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
							host, 
							port, 
							SmartUXProperties.getProperty("GuideLink.refreshGuideLink.url"),
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.method"), "POST"), 
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"), 
							MediaType.APPLICATION_JSON.toString(),
							NumberUtils.toInt(SmartUXProperties.getProperty("GuideLink.refreshGuideLink.timeout"), 90000)).getResponseBody());
					
					result.setFlag(codeMsg.getCode());
					result.setMessage(codeMsg.getMessage());
					cLog.middleLog("가이드채널 즉시적용", "API 호출", codeMsg.getCode(), codeMsg.getMessage());
				}
			});
		} catch (MimsCommonException e) {
			cLog.warnLog("가이드채널 즉시적용", "실패", e.getFlag(), e.getMessage(), e.getLocalizedMessage());
			result.setFlag(e.getFlag());
			result.setMessage(e.getMessage());
		} finally {
			cLog.endLog("가이드채널 즉시적용", loginUser, result.getFlag(), result.getMessage());
		}
		
		String resultMsg = "{\"flag\" : \"" + result.getFlag() + "\", \"message\" : \"" + result.getMessage() + "\"}";
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(resultMsg).toString(), responseHeaders, HttpStatus.OK);
	}
	
	/**
	 * 가이드채널 목록 조회 ajax
	 * 
	 * @throws Exception
	 */
	/*
	@RequestMapping(value="/admin/guidelink/getGuideLinkList", method=RequestMethod.POST)
	public ResponseEntity<String> getGuideLinkList(InsertGuideLinkVo guideLinkInsert
		, HttpServletRequest request
		, Model model
		) throws Exception{
	
	if(!"null".equals(guideLinkInsert.getServiceId())) {
		guideLinkInsert.setServiceIdArr(guideLinkInsert.getServiceId().split(","));
	}
	
	List<InsertGuideLinkVo> list = new ArrayList<InsertGuideLinkVo>();
	try {
		list = service.getGuideLinkList(guideLinkInsert);
		System.out.println(list.toString());
	} catch(Exception e) {
		logger.error("[getGuideLinkList]["+e.getClass().getName()+"]["+e.getMessage()+"]");	
		e.printStackTrace();
	}
	
	ObjectMapper om = new ObjectMapper();
	HttpHeaders responseHeaders = new HttpHeaders();
	responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
	return new ResponseEntity<String>(om.writeValueAsString(list), responseHeaders, HttpStatus.CREATED);
	}
	
	*//**
		* 가이드채널 등록 페이지
		* 
		* @throws Exception
		*/
	/*
	@RequestMapping(value="/admin/guidelink/insertGuideLink", method=RequestMethod.GET)
	public String insertGuideLink(Model model) throws Exception {
	
	model.addAttribute("type", "insert");
	
	List<HashMap<String, Object>> channelList = service.getChannelList();
	model.addAttribute("channelList", channelList);
	
	return "/admin/guidelink/insertGuideLink";
	}
	
	*//**
		* 가이드채널 등록 ajax
		* 
		* @throws Exception
		*/
	/*
	@RequestMapping(value="/admin/guidelink/insertGuideLink", method=RequestMethod.POST)
	public ResponseEntity<String> insertGuideLink(InsertGuideLinkVo guideLinkInsert
		, HttpServletRequest request
		, Model model
		) throws Exception{
	
	String resCode = "";
	String resMessage = "";
	
	try {
		
		String result_code = service.insertGuideLink(guideLinkInsert);
		if("0000".equals(result_code)) {
			resCode = SmartUXProperties.getProperty("flag.success");
			resMessage = SmartUXProperties.getProperty("message.success");
		}else {
			resCode = result_code;
		}
	} catch(Exception e) {
		logger.error("[insertGuideLink]["+e.getClass().getName()+"]["+e.getMessage()+"]");	
		e.printStackTrace();
		ExceptionHandler handler = new ExceptionHandler(e);
		resCode = handler.getFlag();
		resMessage = handler.getMessage();
	}
	
	HttpHeaders responseHeaders = new HttpHeaders();
	responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
	return new ResponseEntity<String>("{\"flag\" : \"" + resCode + "\", \"message\" : \"" + resMessage + "\"}", responseHeaders, HttpStatus.CREATED);
	}
	
	*//**
		* 가이드채널 수정 페이지
		* 
		* @throws Exception
		*/
	/*
	@RequestMapping(value="/admin/guidelink/updateGuideLink", method=RequestMethod.GET)
	public String updateGuideLink(@RequestParam(value="seq", required=false, defaultValue="") String seq,
		@RequestParam(value="type_filter_str", required=false, defaultValue="") String type_filter_str,
		@RequestParam(value="view", required=false, defaultValue="") String view,
		Model model) throws Exception {
	
	List<HashMap<String, Object>> channelList = service.getChannelList();
	model.addAttribute("channelList", channelList);
	
	InsertGuideLinkVo vo = service.getGuideLinkDetail(seq);
	
	model.addAttribute("type", "update");
	model.addAttribute("detail", vo);
	model.addAttribute("linkType", vo.getLinkType());
	model.addAttribute("seq", seq);
	model.addAttribute("type_filter_str", type_filter_str);
	model.addAttribute("view", view);
	
	return "/admin/guidelink/insertGuideLink";
	}
	
	*//**
		* 가이드채널 등록 ajax
		* 
		* @throws Exception
		*/
	/*
	@RequestMapping(value="/admin/guidelink/updateGuideLink", method=RequestMethod.POST)
	public ResponseEntity<String> updateGuideLink(InsertGuideLinkVo guideLinkInsert
		, HttpServletRequest request
		, Model model
		) throws Exception{
	
	String resCode = "";
	String resMessage = "";
	
	try {
		
		String result_code = service.updateGuideLink(guideLinkInsert);
		if("0000".equals(result_code)) {
			resCode = SmartUXProperties.getProperty("flag.success");
			resMessage = SmartUXProperties.getProperty("message.success");
		}else {
			resCode = result_code;
		}
	} catch(Exception e) {
		logger.error("[updateGuideLink]["+e.getClass().getName()+"]["+e.getMessage()+"]");	
		e.printStackTrace();
		ExceptionHandler handler = new ExceptionHandler(e);
		resCode = handler.getFlag();
		resMessage = handler.getMessage();
	}
	
	HttpHeaders responseHeaders = new HttpHeaders();
	responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
	return new ResponseEntity<String>("{\"flag\" : \"" + resCode + "\", \"message\" : \"" + resMessage + "\"}", responseHeaders, HttpStatus.CREATED);
	}
	
	
	*//**
		* 가이드채널 시간변경 업데이트
		* 
		* @throws Exception
		*/
	/*
	@RequestMapping(value="/admin/guidelink/updateGuideTime", method=RequestMethod.POST)
	public ResponseEntity<String> updateGuideTime(InsertGuideLinkVo guideLinkInsert
		, HttpServletRequest request
		, Model model
		) throws Exception{
	
	String resCode = "";
	String resMessage = "";
	
	try {
		
		String result_code = service.updateGuideTime(guideLinkInsert);
		if("0000".equals(result_code)) {
			resCode = SmartUXProperties.getProperty("flag.success");
			resMessage = SmartUXProperties.getProperty("message.success");
		}else {
			resCode = result_code;
		}
	} catch(Exception e) {
		logger.error("[updateGuideTime]["+e.getClass().getName()+"]["+e.getMessage()+"]");	
		e.printStackTrace();
		ExceptionHandler handler = new ExceptionHandler(e);
		resCode = handler.getFlag();
		resMessage = handler.getMessage();
	}
	
	HttpHeaders responseHeaders = new HttpHeaders();
	responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
	return new ResponseEntity<String>("{\"flag\" : \"" + resCode + "\", \"message\" : \"" + resMessage + "\"}", responseHeaders, HttpStatus.CREATED);
	}
	
	*//**
		* 가이드채널 시간변경 업데이트
		* 
		* @throws Exception
		*/
	/*
	@RequestMapping(value="/admin/guidelink/testdata", method=RequestMethod.POST)
	public ResponseEntity<String> testdata(InsertGuideLinkVo guideLinkInsert
		, HttpServletRequest request
		, Model model
		) throws Exception{
	
	String resCode = "";
	String resMessage = "";
	
	try {
		
		String result_code = service.testdata(guideLinkInsert);
		if("0000".equals(result_code)) {
			resCode = SmartUXProperties.getProperty("flag.success");
			resMessage = SmartUXProperties.getProperty("message.success");
		}else {
			resCode = result_code;
		}
	} catch(Exception e) {
		logger.error("[updateGuideTime]["+e.getClass().getName()+"]["+e.getMessage()+"]");	
		e.printStackTrace();
		ExceptionHandler handler = new ExceptionHandler(e);
		resCode = handler.getFlag();
		resMessage = handler.getMessage();
	}
	
	HttpHeaders responseHeaders = new HttpHeaders();
	responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
	return new ResponseEntity<String>("{\"flag\" : \"" + resCode + "\", \"message\" : \"" + resMessage + "\"}", responseHeaders, HttpStatus.CREATED);
	}
	
	*//**
		* 말풍선 즉시적용
		* @param request
		* @param response
		* @return
		* @throws Exception
		*//*
			@RequestMapping(value="/admin/guidelink/applyCache", method=RequestMethod.POST)
			public ResponseEntity<String> applyCache(
				HttpServletRequest request, 
				HttpServletResponse response
				) throws Exception {
			
			final CLog cLog = new CLog(applyLogger, request.getRemoteAddr(), request.getRequestURI(), request.getMethod());
			final String loginUser = CookieUtil.getCookieUserID(request);
			final CUDResult result = new CUDResult();
			
			try {
				cLog.startLog("가이드채널 즉시적용",  loginUser);
				final String host = SmartUXProperties.getProperty("cache.sync.public.host");
				final String port = SmartUXProperties.getProperty("cache.sync.public.port");
				
				String fileLockPath = SmartUXProperties.getProperty("GuideLinkDao.refreshGuideLinkList.fileLock");
				int waitTime = NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.wait"), 5);
				
				FileProcessLockFactory.start(fileLockPath, waitTime,  new FileProcessFunction("가이드채널 즉시적용", ResultCode.ApplyRequestFail) {
					
					@Override
					public void run() throws Exception {
						
						RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
								host, 
								port, 
								SmartUXProperties.getProperty("GuideLink.refreshGuideLink.url"),
								org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.method"), "POST"), 
								org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"), 
								MediaType.APPLICATION_JSON.toString(),
								NumberUtils.toInt(SmartUXProperties.getProperty("GuideLink.refreshGuideLink.timeout"), 90000)).getResponseBody());
						
						result.setFlag(codeMsg.getCode());
						result.setMessage(codeMsg.getMessage());
						cLog.middleLog("가이드채널 즉시적용", "API 호출", codeMsg.getCode(), codeMsg.getMessage());
					}
				});
			} catch (MimsCommonException e) {
				cLog.warnLog("가이드채널 즉시적용", "실패", e.getFlag(), e.getMessage(), e.getLocalizedMessage());
				result.setFlag(e.getFlag());
				result.setMessage(e.getMessage());
			} finally {
				cLog.endLog("가이드채널 즉시적용", loginUser, result.getFlag(), result.getMessage());
			}
			
			String resultMsg = "{\"flag\" : \"" + result.getFlag() + "\", \"message\" : \"" + result.getMessage() + "\"}";
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
			return new ResponseEntity<String>(JSONSerializer.toJSON(resultMsg).toString(), responseHeaders, HttpStatus.OK);
			}*/
}
