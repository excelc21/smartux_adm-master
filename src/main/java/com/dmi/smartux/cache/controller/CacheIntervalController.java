package com.dmi.smartux.cache.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dmi.smartux.admin.code.vo.CodeItemVO;
import com.dmi.smartux.admin.code.vo.CodeVO;
import com.dmi.smartux.cache.dao.CacheIntervalDao;
import com.dmi.smartux.cache.service.CacheIntervalService;
import com.dmi.smartux.common.service.CacheService;
import com.dmi.smartux.configuration.vo.YoutubeResultVO;

@Controller
public class CacheIntervalController {
	private final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	CacheIntervalService service;
	
	@PostConstruct
	public void init(){
		logger.debug("############Cache INIT 시작##########");
		try {
			//List<CodeItemVO> list = (List<CodeItemVO>) service.getCache(dao, "getCacheInterval", "CacheIntervalKey", -1);
			List<CodeItemVO> list = service.getCacheIntervalInit();
			logger.debug("############Cache INIT 성공 LIST##########="+list.size());
		} catch (Exception e) {
			logger.debug("############Cache INIT 에러 발생##########");
		}
		logger.debug("############Cache INIT 종료##########");
	}
	
	
	@RequestMapping(value="/setCacheIntervalUpdate")
	public void API_setCacheIntervalUpdate(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value="callByScheduler", required=false, defaultValue="N") String callByScheduler
			){
		logger.debug("############Cache setCacheIntervalUpdate 시작##########");
		try {
			//List<CodeItemVO> list = (List<CodeItemVO>) service.getCache(dao, "getCacheInterval", "CacheIntervalKey", -1);
			List<CodeItemVO> list = service.getCacheIntervalInit();
			logger.debug("############Cache setCacheIntervalUpdate 성공 LIST##########="+list.size());
			
			if(!"N".equals(callByScheduler)){
				//response.setStatus(201);
			}
			
		} catch (Exception e) {
			logger.debug("############Cache setCacheIntervalUpdate 에러 발생##########");
		}
		logger.debug("############Cache setCacheIntervalUpdate 종료##########");
	}
			
}
