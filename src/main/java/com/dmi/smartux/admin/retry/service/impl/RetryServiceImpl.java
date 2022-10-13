package com.dmi.smartux.admin.retry.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmi.smartux.admin.retry.dao.RetryDao;
import com.dmi.smartux.admin.retry.service.RetryService;
import com.dmi.smartux.cache.service.CacheIntervalService;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.service.CacheService;
import com.dmi.smartux.common.util.GlobalCom;

@Service
public class RetryServiceImpl implements RetryService{
	
	private final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	CacheService service;
	
	@Autowired
	CacheIntervalService cacheIntervalService;
	
	@Autowired
	RetryDao retryDao;

	@Override
	public String setPushGateWayRetry(String code_id, String callByScheduler) throws Exception {
		
		String retryNum = "";
		

		String intervalStr = "0";
		try{
			intervalStr = Long.toString(cacheIntervalService.getCacheInterval("RetryDao.getPushGateWayRetry.interval"));
		}catch (Exception e) {
			intervalStr = SmartUXProperties.getProperty("RetryDao.getPushGateWayRetry.interval");
		}
		long interval = GlobalCom.getCacheInterval(callByScheduler, intervalStr);
		
		retryNum = (String) service.getCache(retryDao, "setPushGateWayRetry", SmartUXProperties.getProperty("RetryDao.getPushGateWayRetry.cacheKey"), interval,code_id);
		
		return retryNum;
	}

}
