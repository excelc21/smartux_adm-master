package com.dmi.smartux.admin.cache.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dmi.smartux.common.service.CacheService;

@Controller

public class CacheController {

	@Autowired
	CacheService cacheService;
	
	/**
	 * 관리자 화면의 Cache > Cache Size 조회 화면
	 * @param model			Model 객체
	 * @return				Cache Size 조회 화면 URL
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/cache/getCacheSize")
	public String getCacheSize(Model model) throws Exception{
		model.addAttribute("cacheSize", cacheService.getCacheSize());
		return "/admin/cache/getCacheSize";
	}
	
}
