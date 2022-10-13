package com.dmi.smartux.gpack.pack.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmi.smartux.cache.service.CacheIntervalService;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.service.CacheService;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.gpack.pack.dao.GpackPackVersionDao;
import com.dmi.smartux.gpack.pack.service.GpackPackVersionService;

@Service
public class GpackPackVersionServiceImpl implements GpackPackVersionService {

	@Autowired
	GpackPackVersionDao dao;
	
	@Autowired
	CacheService cacheservice;
	
	@Autowired
	CacheIntervalService cacheIntervalService;
	
	@Override
	// @Transactional(readOnly=true)
	public String getPackVersion(String sa_id, String stb_mac, String app_type, String callByScheduler, String pack_id) throws Exception {
		// TODO Auto-generated method stub
		
		//long interval = GlobalCom.getCacheInterval(callByScheduler, SmartUXProperties.getProperty("MainPanelDao.getMainPanelVersionInfo.interval"),"MainPanelDao.getMainPanelVersionInfo.interval");
		String intervalStr = "0";
		try{
			intervalStr = Long.toString(cacheIntervalService.getCacheInterval("GpackPackVersionDao.getPackVersion.interval"));
		}catch (Exception e) {
			intervalStr = SmartUXProperties.getProperty("GpackPackVersionDao.getPackVersion.interval");
		}
		long interval = GlobalCom.getCacheInterval(callByScheduler, intervalStr);
		
		// 팩 Version 정보는 관리자가 확인 작업을 할때만 캐쉬가 갱신이 되므로 
		// 셋탑이 조회할때는 갱신주기를 0으로 주어 DB에서 다시 읽어서 캐쉬를 갱신하는 작업을 하지 않도록 한다 
		String result = (String)cacheservice.getCache(dao
				, "getPackVersion"
				, SmartUXProperties.getProperty("GpackPackVersionDao.getPackVersion.cacheKey")
				, interval
				, pack_id
				);
		return result;
	}
	
}
