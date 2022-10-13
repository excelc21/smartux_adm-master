package com.dmi.smartux.gpack.pack.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmi.smartux.cache.service.CacheIntervalService;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.service.CacheService;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.gpack.pack.dao.GpackPackInfoDao;
import com.dmi.smartux.gpack.pack.service.GpackPackInfoService;
import com.dmi.smartux.gpack.pack.vo.GpackPackInfoVO;


@Service
public class GpackPackInfoServiceImpl implements GpackPackInfoService {

	@Autowired
	GpackPackInfoDao dao;
	
	@Autowired
	CacheService cacheservice;
	
	@Autowired
	CacheIntervalService cacheIntervalService;

	public GpackPackInfoVO getGpackPackInfo(String sa_id, String stb_mac, String pack_id, String callByScheduler) throws Exception{

		String intervalStr = "0";
		try{
			intervalStr = Long.toString(cacheIntervalService.getCacheInterval("GpackCategoryInfoDao.getPackInfo.interval"));
		}catch (Exception e) {
			intervalStr = SmartUXProperties.getProperty("GpackCategoryInfoDao.getPackInfo.interval");
		}

		// 스케쥴러에서 호출한 경우는 해당 interval, 
		// 관리자에서 호출한 경우 무조건 DB에서 읽어오도록,
		// 현제 셋팅이 강제로 DB에서 읽어오도록 되어 있는지를 확인, 아니면 무조건 캐시에서 읽어온다.
		long interval = GlobalCom.getCacheInterval(callByScheduler, intervalStr);
		
		GpackPackInfoVO gpackPackInfoVO = new GpackPackInfoVO();
		try {
			gpackPackInfoVO = (GpackPackInfoVO)cacheservice.getCache(dao
					, "getGpackPackInfo"
					, SmartUXProperties.getProperty("GpackCategoryInfoDao.getPackInfo.cacheKey")
					, interval
					, pack_id
					);
		} catch ( Exception e ) {
			gpackPackInfoVO = new GpackPackInfoVO();
		}
		
		return gpackPackInfoVO;
	}
}
