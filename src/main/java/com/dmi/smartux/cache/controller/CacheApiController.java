package com.dmi.smartux.cache.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.service.CacheService;

@Controller
public class CacheApiController {

	@Autowired
	CacheService cacheService;
	
	/**
	 * 관리자 화면의 Cache > Cache Size 조회 화면
	 * @param model			Model 객체
	 * @return				Cache Size 조회 화면 URL
	 * @throws Exception
	 * 관리자모드와 인터페이스 분리됨으로 관리자모드에서 인터페이스의 캐쉬사이즈 체크 인터페이스 추가
	 */
	@RequestMapping(value="/cacheCall/cache/getCacheSize")
	public @ResponseBody String getCacheSize(Model model) throws Exception{
		
		String resultcode = "";
		String resultmessage = "";
		long cacheSize;
		
		try{
			resultcode = SmartUXProperties.getProperty("flag.success");
			resultmessage = SmartUXProperties.getProperty("message.success");
			cacheSize = cacheService.getCacheSize();
		}catch(java.lang.Exception e){
			resultcode = SmartUXProperties.getProperty("flag.etc");
			resultmessage = SmartUXProperties.getProperty("message.etc");
			cacheSize = 0;
		}
		
		return "{\"flag\" : \"" + resultcode + "\", \"message\" : \"" + resultmessage + "\", \"size\" : \"" + cacheSize + "\"}";
		
	}
	
}
