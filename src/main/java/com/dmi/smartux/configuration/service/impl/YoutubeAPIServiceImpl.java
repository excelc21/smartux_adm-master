package com.dmi.smartux.configuration.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmi.smartux.cache.service.CacheIntervalService;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.service.CacheService;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.configuration.dao.YoutubeAPIDao;
import com.dmi.smartux.configuration.service.YoutubeAPIService;
import com.dmi.smartux.configuration.vo.YoutubeResultVO;

@Service
public class YoutubeAPIServiceImpl implements YoutubeAPIService{

	@Autowired
	YoutubeAPIDao dao;
	
	@Autowired
	CacheService cacheservice;
	
	@Autowired
	CacheIntervalService cacheIntervalService;
	
	
	@Override
	public YoutubeResultVO getYoutubeSearchKey(String sa_id, String stb_mac, String app_type, String callByScheduler) throws Exception {
		
		
		String intervalStr = "0";
		try{
			intervalStr = Long.toString(cacheIntervalService.getCacheInterval("YoutubeAPIDao.getYoutubeSearchKey.interval"));
		}catch (Exception e) {
			intervalStr = SmartUXProperties.getProperty("YoutubeAPIDao.getYoutubeSearchKey.interval");
		}
		long interval = GlobalCom.getCacheInterval(callByScheduler, intervalStr);
		//long interval = GlobalCom.getCacheInterval(callByScheduler, SmartUXProperties.getProperty("YoutubeAPIDao.getYoutubeSearchKey.interval"),"YoutubeAPIDao.getYoutubeSearchKey.interval");
		
		YoutubeResultVO result = (YoutubeResultVO)cacheservice.getCache(dao
				, "getYoutubeSearchKey"
				, SmartUXProperties.getProperty("YoutubeAPIDao.getYoutubeSearchKey.cacheKey")
				, interval
				);
		
		return result;
	}
	
	
}
