package com.dmi.smartux.admin.youtube.controller;

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

import com.dmi.commons.consts.ResultCode;
import com.dmi.commons.exception.MimsCommonException;
import com.dmi.commons.factory.FileProcessFunction;
import com.dmi.commons.factory.FileProcessLockFactory;
import com.dmi.commons.logger.CLog;
import com.dmi.commons.model.RestfulCodeMsg;
import com.dmi.commons.utility.HttpClientUtils;
import com.dmi.commons.utility.RestfulUtils;
import com.dmi.smartux.admin.youtube.service.YoutubeService;
import com.dmi.smartux.admin.youtube.vo.YoutubeVO;
import com.dmi.smartux.common.exception.ExceptionHandler;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.CookieUtil;
import com.dmi.smartux.common.util.HTMLCleaner;
import com.dmi.smartux.common.vo.CUDResult;

import net.sf.json.JSONSerializer;

@Controller
public class YoutubeController {
	
	private final Log logger = LogFactory.getLog(this.getClass());
	
	
	@Autowired
	YoutubeService service;
	
	private final Log applyLogger = LogFactory.getLog("adminApplyCacheHistroy");
	
	/**
	 * youtube 검색어 즉시적용
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/admin/youtube/activateCache", method=RequestMethod.POST )
	public ResponseEntity<String> setYoutubeActivateCache(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value="callByScheduler",  defaultValue="Y") final  String callByScheduler
			) throws Exception{
		
		final CLog cLog = new CLog(applyLogger, request.getRemoteAddr(), request.getRequestURI(), request.getMethod());
		final String loginUser = callByScheduler.equals("Y") ? "batch" : CookieUtil.getCookieUserID(request);
		final CUDResult result = new CUDResult();
		
		try {
			cLog.startLog("youtube 검색어 즉시적용 : ",  loginUser);
			final String host = SmartUXProperties.getProperty("cache.sync.public.host");
			final String port = SmartUXProperties.getProperty("cache.sync.public.port");
			
			String fileLockPath = SmartUXProperties.getProperty("YoutubeSearchKeywordDao.refreshYoutubeSearchKeyword.fileLock");
			int waitTime = NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.wait"), 5);
			
			FileProcessLockFactory.start(fileLockPath, waitTime,  new FileProcessFunction("youtube 검색어 즉시적용", ResultCode.ApplyRequestFail) {
				
				@Override
				public void run() throws Exception {
					
					//즉시적용 호출 시 sleep 값 추가 -> DB분리건으로 OGG가 10초 이상 걸리는 경우가 있어 즉시적용 시 반영이 안돼서 추가함
					Thread.sleep(Integer.parseInt(org.apache.commons.lang.StringUtils.defaultIfEmpty(SmartUXProperties.getProperty("apply.cache.call.sleep"), "10000")));
					
					RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
							host, 
							port, 
							SmartUXProperties.getProperty("YoutubeSearchKeywordDao.refreshYoutubeSearchKeyword.url")+"?callByScheduler="+callByScheduler,
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.method"), "POST"), 
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"), 
							MediaType.APPLICATION_JSON.toString(),
							NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.timeout"), 60000)).getResponseBody());
					
					result.setFlag(codeMsg.getCode());
					result.setMessage(codeMsg.getMessage());
					cLog.middleLog("youtube 검색어 즉시적용", "API 호출", codeMsg.getCode(), codeMsg.getMessage());
				}
			});
		} catch (MimsCommonException e) {
			cLog.warnLog("youtube 검색어 즉시적용", "실패", e.getFlag(), e.getMessage(), e.getLocalizedMessage());
			result.setFlag(e.getFlag());
			result.setMessage(e.getMessage());
		} finally {
			cLog.endLog("youtube 검색어 즉시적용", loginUser, result.getFlag(), result.getMessage());
		}
		
		String resultMsg = "{\"flag\" : \"" + result.getFlag() + "\", \"message\" : \"" + result.getMessage() + "\"}";
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(resultMsg).toString(), responseHeaders, HttpStatus.OK);
	}
	
	
	@RequestMapping(value="/admin/youtube/list", method=RequestMethod.GET )
	public String getYoutubeList(
			Model model
			){
		
		YoutubeVO youtube = new YoutubeVO(); 
		
		try {
			List<YoutubeVO> list = service.getYoutubeList();
			youtube.setList(list);
			
			model.addAttribute("vo", youtube);
		} catch (Exception e) {
			//logger.error("getYoutubeList "+e.getClass().getName()); 
			//logger.error("getYoutubeList "+e.getMessage());
			logger.error("[getYoutubeList]["+e.getClass().getName()+"]["+e.getMessage()+"]");
		}
		
		return "/admin/youtube/list";
	}
	
	@RequestMapping(value="/admin/youtube/insert", method=RequestMethod.GET )
	public String getYoutubeInsert(){
		
		return "/admin/youtube/insert";
	}
	
	@RequestMapping(value="/admin/youtube/insert", method=RequestMethod.POST )
	public @ResponseBody String setYoutubeInsert(
			@RequestParam(value="category", required=false, defaultValue="") String category,
			@RequestParam(value="recommend_text", required=false, defaultValue="") String recommend_text,
			@RequestParam(value="write_id", required=false, defaultValue="") String write_id
			){
		
		HTMLCleaner cleaner = new HTMLCleaner();
		category 	= cleaner.clean(category);
		recommend_text 	= cleaner.clean(recommend_text);
		write_id 	= cleaner.clean(write_id);
		
		logger.debug(category);
		logger.debug(recommend_text);
		logger.debug(write_id);
		
		String resultcode = "";
		String resultmessage = "";
		
		try{
			service.setYoutubeInsert(category,recommend_text,write_id);
			
			resultcode = SmartUXProperties.getProperty("flag.success");
			resultmessage = SmartUXProperties.getProperty("message.success");			
		}catch(Exception e){
			//logger.error("setYoutubeInsert "+e.getClass().getName()); 
			//logger.error("setYoutubeInsert "+e.getMessage());
			logger.error("[setYoutubeInsert]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}
		
		return "{\"flag\" : \"" + resultcode + "\", \"message\" : \"" + resultmessage + "\"}";
	}
	
	
	@RequestMapping(value="/admin/youtube/delete", method=RequestMethod.POST)
	public @ResponseBody String setYoutubeDelete(@RequestParam(value="code[]") String [] codes){
		
		String resultcode = "";
		String resultmessage = "";
		
		try{
			service.setYoutubeDelete(codes);
			
			resultcode = SmartUXProperties.getProperty("flag.success");
			resultmessage = SmartUXProperties.getProperty("message.success");			
		}catch(Exception e){
			//logger.error("setYoutubeDelete "+e.getClass().getName()); 
			//logger.error("setYoutubeDelete "+e.getMessage());
			logger.error("[setYoutubeDelete]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}
		
		return "{\"flag\" : \"" + resultcode + "\", \"message\" : \"" + resultmessage + "\"}";
	}
			
	@RequestMapping(value="/admin/youtube/useUpdate", method=RequestMethod.POST)
	public @ResponseBody String setUseUpdate(
			@RequestParam(value="code") String code,
			@RequestParam(value="use_yn") String use_yn
			){
		
		HTMLCleaner cleaner = new HTMLCleaner();
		code 	= cleaner.clean(code);
		use_yn 	= cleaner.clean(use_yn);
		
		String resultcode = "";
		String resultmessage = "";
		
		try{
			//service.setYoutubeDelete(codes);
			service.setUseUpdate(code,use_yn);
			
			resultcode = SmartUXProperties.getProperty("flag.success");
			resultmessage = SmartUXProperties.getProperty("message.success");			
		}catch(Exception e){
			//logger.error("setYoutubeDelete "+e.getClass().getName()); 
			//logger.error("setYoutubeDelete "+e.getMessage());
			logger.error("[setYoutubeDelete]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}
		
		return "{\"flag\" : \"" + resultcode + "\", \"message\" : \"" + resultmessage + "\"}";
	}
	
	@RequestMapping(value="/admin/youtube/update", method=RequestMethod.GET )
	public String getYoutubeData(
			@RequestParam(value="code", required=false, defaultValue="") String code,
			Model model
			){
		
		HTMLCleaner cleaner = new HTMLCleaner();
		code 	= cleaner.clean(code);
		
		YoutubeVO yVO = new YoutubeVO();		
		
		try{
			yVO = service.getYoutubeData(code);
			
			model.addAttribute("vo", yVO);
			
		}catch(Exception e){
			//logger.error("getYoutubeData "+e.getClass().getName()); 
			//logger.error("getYoutubeData "+e.getMessage());
			logger.error("[getYoutubeData]["+e.getClass().getName()+"]["+e.getMessage()+"]");
		}

		return "/admin/youtube/update";
	}
	
	@RequestMapping(value="/admin/youtube/update", method=RequestMethod.POST )
	public @ResponseBody String setYoutubeUpdate(
			@RequestParam(value="code", required=false, defaultValue="") String code,
			@RequestParam(value="category", required=false, defaultValue="") String category,
			@RequestParam(value="recommend_text", required=false, defaultValue="") String recommend_text,
			@RequestParam(value="write_id", required=false, defaultValue="") String write_id
			){
		
		HTMLCleaner cleaner = new HTMLCleaner();
		code 	= cleaner.clean(code);
		category 	= cleaner.clean(category);
		recommend_text 	= cleaner.clean(recommend_text);
		write_id 	= cleaner.clean(write_id);
		
		logger.debug(category);
		logger.debug(recommend_text);
		logger.debug(write_id);
		
		String resultcode = "";
		String resultmessage = "";
		
		try{
			service.setYoutubeUpdate(code,category,recommend_text,write_id);
			
			resultcode = SmartUXProperties.getProperty("flag.success");
			resultmessage = SmartUXProperties.getProperty("message.success");			
		}catch(Exception e){
			//logger.error("setYoutubeInsert "+e.getClass().getName()); 
			//logger.error("setYoutubeInsert "+e.getMessage());
			logger.error("[setYoutubeInsert]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}
		
		return "{\"flag\" : \"" + resultcode + "\", \"message\" : \"" + resultmessage + "\"}";
	}
	
}
