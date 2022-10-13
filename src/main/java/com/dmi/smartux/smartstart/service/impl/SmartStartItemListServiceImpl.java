package com.dmi.smartux.smartstart.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmi.smartux.cache.service.CacheIntervalService;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.service.CacheService;
import com.dmi.smartux.common.util.GlobalCom;

import com.dmi.smartux.smartstart.dao.SmartStartItemListDao;
import com.dmi.smartux.smartstart.service.SmartStartItemListService;
import com.dmi.smartux.smartstart.vo.SmartStartItemListVO;

@Service
public class SmartStartItemListServiceImpl implements SmartStartItemListService {

	private final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	SmartStartItemListDao dao;
	
	@Autowired
	CacheService cacheservice;
	
	@Autowired
	CacheIntervalService cacheIntervalService;
	

	/**
	 * SmartStartItemList을 조회하기 위해 사용되는 정보의 갯수를 조회하는 서비스구현 클래스
	 * @param sa_id			가입번호
	 * @param stb_mac		맥주소
	 * @param app_type		어플타입
	 * @param callByScheduler	단말에서 호출 했을 경우엔 N, 스케쥴러에서 호출했을땐 Y, 관리자툴에서 조회했을땐 A가 들어간다 
	 * @return				검색된 레코드 총 갯수
	 * @throws Exception
	 */
	@Override
	// @Transactional(readOnly=true)
	public int getSmartStartItemListCount(String sa_id, String stb_mac, String app_type, String callByScheduler) throws Exception {
		// TODO Auto-generated method stub		
		List<SmartStartItemListVO> dbresult = (List<SmartStartItemListVO>)cacheservice.getCache	(dao, "getSmartStartItemList"
			, SmartUXProperties.getProperty("SmartStartItemListDao.getSmartStartItemList.cacheKey")
			, Long.parseLong(SmartUXProperties.getProperty("SmartStartItemListDao.getSmartStartItemList.interval"))
			);
		
		int result = 0;
		if(dbresult != null){
			result = dbresult.size(); 
		}
		
		return result;				
	}
	
	/**
	 * SmartStartItemList을 조회하기 위해 사용되는 정보를 조회하는 서비스구현 클래스
	 * @param sa_id			가입번호
	 * @param stb_mac		맥주소
	 * @param app_type		어플타입
	 * @param callByScheduler	단말에서 호출 했을 경우엔 N, 스케쥴러에서 호출했을땐 Y, 관리자툴에서 조회했을땐 A가 들어간다  
	 * @return				SmartStartItemList 정보가 들어있는 SmartStartItemListVO 객체가 들어있는 List 객체
	 * @throws Exception
	 */
	@Override
	// @Transactional(readOnly=true)
	public List<SmartStartItemListVO> getSmartStartItemList(String sa_id, String stb_mac, String app_type , String callByScheduler) throws Exception {
		
		//long interval = GlobalCom.getCacheInterval(callByScheduler, SmartUXProperties.getProperty("SmartStartItemListDao.getSmartStartItemList.interval"),"SmartStartItemListDao.getSmartStartItemList.interval");
		String intervalStr = "0";
		try{
			intervalStr = Long.toString(cacheIntervalService.getCacheInterval("SmartStartItemListDao.getSmartStartItemList.interval"));
		}catch (Exception e) {
			intervalStr = SmartUXProperties.getProperty("SmartStartItemListDao.getSmartStartItemList.interval");
		}
		long interval = GlobalCom.getCacheInterval(callByScheduler, intervalStr);
				
		// SmartStartItemList 정보는 관리자가 확인 작업을 할때만 캐쉬가 갱신이 되므로 
		// 셋탑이 조회할때는 갱신주기를 0으로 주어 DB에서 다시 읽어서 캐쉬를 갱신하는 작업을 하지 않도록 한다 
		List<SmartStartItemListVO> dbresult = (List<SmartStartItemListVO>)cacheservice.getCache(dao
				, "getSmartStartItemList"
				, SmartUXProperties.getProperty("SmartStartItemListDao.getSmartStartItemList.cacheKey")
				, interval
				);
		
		List<SmartStartItemListVO> result = null;
		
		result = dbresult;	
		
		if(result == null){
			logger.debug("result is null");
		}else{
			logger.debug("result is not null : " + result.size());
		}
		
		return result;
	}	
	
	
}
