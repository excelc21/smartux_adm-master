package com.dmi.smartux.admin.smartepg.controller;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dmi.smartux.admin.smartepg.service.SmartEPGService;
import com.dmi.smartux.admin.smartepg.vo.ThemeInfoVO;
import com.dmi.smartux.common.exception.ExceptionHandler;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.HTMLCleaner;

@Controller("adminSmartEPGController")
public class SmartEPGController {
	
	private final Log logger = LogFactory.getLog(this.getClass()); 
	
	@Autowired
	SmartEPGService service;
	
	/**
	 * 테마 정보 리스트
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/smartepg/getThemeInfoList")
	public String getThemeInfoList(Model model) throws Exception {
		List<ThemeInfoVO> result = service.getThemeInfoList();
		model.addAttribute("result", result);
		return "/admin/smartepg/getThemeInfoList";
	}
	
	/**
	 * 테마 정보 상세 조회
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/smartepg/viewThemeInfo")
	public String viewThemeInfo(@RequestParam(value="theme_code") String theme_code, Model model) throws Exception {
		
		HTMLCleaner cleaner = new HTMLCleaner();
		theme_code 	= cleaner.clean(theme_code);
		
		ThemeInfoVO result = service.viewThemeInfo(theme_code);
		model.addAttribute("result", result);
		
		return "/admin/smartepg/updateThemeInfo";
	}
	
	/**
	 * 테마 정보 등록 화면
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/smartepg/insertThemeInfo", method=RequestMethod.GET)
	public String insertThemeInfo() throws Exception {
		return "/admin/smartepg/insertThemeInfo";
	}
	
	/**
	 * 테마 정보 등록 처리
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/smartepg/insertThemeInfo", method=RequestMethod.POST)
	public @ResponseBody String insertThemeInfoProc(
			@RequestParam(value="theme_code") String theme_code,
			@RequestParam(value="theme_name") String theme_name,
			@RequestParam(value="use_yn") String use_yn) throws Exception {
		
		HTMLCleaner cleaner = new HTMLCleaner();
		theme_code 	= cleaner.clean(theme_code);
		theme_name 	= cleaner.clean(theme_name);
		use_yn 	= cleaner.clean(use_yn);
		
		logger.debug("theme_name : " + theme_name);
		String create_id="create_id";
		String resultcode = "";
		String resultmessage = "";
		
		try{
			service.insertThemeInfoProc(theme_code, theme_name, use_yn, create_id);
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
	 * 테마 정보 수정 화면
	 * @return
	 * @throws Exception
	 */
	
	public String updateThemeInfo() throws Exception {
		return null;
	}
	
	/**
	 * 테마 정보 수정 처리
	 * @return
	 * @throws Exception
	 */
	public String updateThemeInfoProc() throws Exception {
		return null;
	}
	
	/**
	 * 테마 정보 삭제 작업
	 * @return
	 * @throws Exception
	 */
	public String deleteThemeInfoProc() throws Exception {
		return null;
	}
	
	/**
	 * 테마 순서 수정 화면
	 * @return
	 * @throws Exception
	 */
	public String orderThemeInfo() throws Exception {
		return null;
	}
	
	/**
	 * 테마 순서 처리 작업
	 * @return
	 * @throws Exception
	 */
	public String orderThemeInfoProc() throws Exception {
		return null;
	}

}
