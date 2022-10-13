package com.dmi.smartux.cache.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmi.smartux.admin.code.vo.CodeItemVO;
import com.dmi.smartux.cache.dao.CacheIntervalDao;
import com.dmi.smartux.cache.service.CacheIntervalService;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.service.CacheService;
import com.dmi.smartux.common.service.impl.CacheServiceImpl;

@Service
public class CacheIntervalServiceImpl implements CacheIntervalService{
	
	private final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	CacheService service;
	
	@Autowired
	CacheIntervalDao dao;
	
	@Override
	public List<CodeItemVO> getCacheIntervalInit() throws Exception {
		// TODO Auto-generated method stub
		List<CodeItemVO> list = (List<CodeItemVO>) service.getCache(dao, "getCacheInterval", "CacheIntervalKey", -1);
		return list;
	}

	@Override
	public long getCacheInterval(String cacheIntervalName) throws Exception {
		logger.debug("############Cache Service getCacheInterval 조회 !!!!!!!!!!!##########");
		// TODO Auto-generated method stub
//		if(service == null){
//			service = new CacheServiceImpl();
//		}
//		if(dao == null){
//			dao = new CacheIntervalDao();
//		}
		
		List<CodeItemVO> list = (List<CodeItemVO>) service.getCache(dao, "getCacheInterval", "CacheIntervalKey", 0);
		
		long interval = 0;
		
		try{
			boolean listChk = false;
			for(int i=0;i<list.size();i++){
				CodeItemVO data = (CodeItemVO)list.get(i);
				if(data.getItem_code().equals(cacheIntervalName)){
					interval = Long.parseLong(data.getItem_nm());
					logger.debug("############Cache Service getCacheInterval List Return ["+interval+"] !!!!!!!!!!!##########");
					listChk = true;
				}
			}
			if(!listChk){
				interval = Long.parseLong(SmartUXProperties.getProperty(cacheIntervalName));
			}
		}catch(Exception e){
			interval = Long.parseLong(SmartUXProperties.getProperty(cacheIntervalName));
		}		
		
		
		
		return interval;
	}
	
}
