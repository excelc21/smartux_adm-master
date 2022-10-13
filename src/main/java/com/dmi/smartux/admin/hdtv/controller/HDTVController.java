package com.dmi.smartux.admin.hdtv.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dmi.smartux.admin.hdtv.service.HDTVService;
import com.dmi.smartux.admin.hdtv.vo.HDTVVO;
import com.dmi.smartux.common.util.HTMLCleaner;

@Controller
public class HDTVController {

	private final Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	HDTVService service;
	
	@RequestMapping(value="/admin/hdtv/view", method=RequestMethod.GET )
	public String getHDTVView(
			HttpServletRequest request,
			HttpServletResponse response,
			Model model
			){
		
		try {
			
			HDTVVO data = service.getHDTVStartupXml();
			
			model.addAttribute("vo",data);
			
		} catch (Exception e) {
			logger.error("[getHDTVView]["+e.getClass().getName()+"]["+e.getMessage()+"]");
		}
		
		
		return "/admin/hdtv/update";
		//return "/admin/code/getCodeList";
	}
	
	@RequestMapping(value="/admin/hdtv/update", method=RequestMethod.POST )
	public String getHDTVUpdate(
			@RequestParam(value="status", required=false, defaultValue="") String status,
			@RequestParam(value="message_yn", required=false, defaultValue="") String message_yn,
			@RequestParam(value="message", required=false, defaultValue="") String message,
			@RequestParam(value="net_type", required=false, defaultValue="") String net_type,
			Model model
			){
		
		HTMLCleaner cleaner = new HTMLCleaner();
		status 	= cleaner.clean(status);
		message_yn 	= cleaner.clean(message_yn);
		message 	= cleaner.clean(message);
		net_type 	= cleaner.clean(net_type);
		
		try {
			
			HDTVVO vo = new HDTVVO();
			vo.setStatus(status);
			vo.setMessage_yn(message_yn);
			vo.setMessage(message);
			vo.setNet_type(net_type);
			
			service.setHDTVStartupXml(vo);
			
			HDTVVO data = service.getHDTVStartupXml();
			model.addAttribute("vo",data);
			model.addAttribute("validate","SUCCESS");
		} catch (Exception e) {
			logger.error("[getHDTVUpdate]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			model.addAttribute("validate","ERROR");
		}
		
		
		return "/admin/hdtv/update";
	}
			
}
