package com.dmi.smartux.configuration.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmi.smartux.cache.service.CacheIntervalService;
import com.dmi.smartux.common.exception.SmartUXException;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.service.CacheService;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.configuration.dao.ConfigurationListDao;
import com.dmi.smartux.configuration.service.ConfigurationListService;
import com.dmi.smartux.configuration.vo.ConfigurationListVO;

@Service
public class ConfigurationListServiceImpl implements ConfigurationListService{

	private final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	ConfigurationListDao dao;
	
	@Autowired
	CacheService cacheservice;
	
	@Autowired
	CacheIntervalService cacheIntervalService;

	/**
	 * ConfigurationList 조회하기 위해 사용되는 정보의 갯수를 조회하는 서비스구현 클래스
	 * @param sa_id			가입번호
	 * @param stb_mac		맥주소
	 * @param app_type		어플타입
	 * @return				검색된 레코드 총 갯수
	 * @throws Exception
	 */
	@Override
	// @Transactional(readOnly=true)
	public int getConfigurationListCount(String sa_id, String stb_mac, String app_type, String callByScheduler) throws Exception {
		// TODO Auto-generated method stub		
		List<ConfigurationListVO> dbresult = (List<ConfigurationListVO>)cacheservice.getCache(dao, "getConfigurationList"
			, SmartUXProperties.getProperty("ConfigurationListDao.getConfigurationList.cacheKey")
			, Long.parseLong(SmartUXProperties.getProperty("ConfigurationListDao.getConfigurationList.interval"))
			, app_type
			);
		
		int result = 0;
		if(dbresult != null){
			result = dbresult.size(); 
		}
		
		return result;				
	}
	
	/**
	 * ConfigurationList 조회하기 위해 사용되는 정보를 조회하는 서비스구현 클래스
	 * @param sa_id			가입번호
	 * @param stb_mac		맥주소
	 * @param app_type		어플타입
	 * @return				ConfigurationList 정보가 들어있는 ConfigurationListVO 객체가 들어있는 List 객체
	 * @throws Exception
	 */
	@Override
	// @Transactional(readOnly=true)
	public List<ConfigurationListVO> getConfigurationList(String sa_id, String stb_mac, String app_type , String callByScheduler) throws Exception {
		
		String intervalStr = "0";
		try{
			intervalStr = Long.toString(cacheIntervalService.getCacheInterval("ConfigurationListDao.getConfigurationList.interval"));
		}catch (Exception e) {
			intervalStr = SmartUXProperties.getProperty("ConfigurationListDao.getConfigurationList.interval");
		}
		long interval = GlobalCom.getCacheInterval(callByScheduler, intervalStr);
				
		// 설정정보는 관리자가 확인 작업을 할때만 캐쉬가 갱신이 되므로 
		// 셋탑이 조회할때는 갱신주기를 0으로 주어 DB에서 다시 읽어서 캐쉬를 갱신하는 작업을 하지 않도록 한다 
		List<ConfigurationListVO> result = (List<ConfigurationListVO>)cacheservice.getCache(dao
				, "getConfigurationList"
				, SmartUXProperties.getProperty("ConfigurationListDao.getConfigurationList.cacheKey")
				, interval
				, app_type
				);
	
		return result;
	}
	
	/**
	 * ConfigurationList 조회하기 위해 사용되는 정보를 조회하는 서비스구현 클래스
	 * @param sa_id			가입번호
	 * @param stb_mac		맥주소
	 * @param app_type		어플타입
	 * @param config_id		설정정보ID
	 * @return				ConfigurationList 정보가 들어있는 ConfigurationListVO 객체가 들어있는 List 객체
	 * @throws Exception
	 */
	@Override
	// @Transactional(readOnly=true)
	public List<ConfigurationListVO> getConfigurationList(String sa_id, String stb_mac, String app_type , String callByScheduler, String config_id) throws Exception {
		
		String intervalStr = "0";
		try{
			
			intervalStr = Long.toString(cacheIntervalService.getCacheInterval("ConfigurationListDao.getConfigurationList.interval"));
			config_id = (GlobalCom.isEmpty(config_id)) ? "": config_id.toUpperCase();
			
		}catch (Exception e) {
			intervalStr = SmartUXProperties.getProperty("ConfigurationListDao.getConfigurationList.interval");
		}
		long interval = GlobalCom.getCacheInterval(callByScheduler, intervalStr);
		// 설정정보는 관리자가 확인 작업을 할때만 캐쉬가 갱신이 되므로 
		// 셋탑이 조회할때는 갱신주기를 0으로 주어 DB에서 다시 읽어서 캐쉬를 갱신하는 작업을 하지 않도록 한다 
		List<ConfigurationListVO> result = (List<ConfigurationListVO>)cacheservice.getCache(dao
				, "getConfigurationList"
				, SmartUXProperties.getProperty("ConfigurationListDao.getConfigurationList.cacheKey")
				, interval
				, app_type
				);
		
		// 캐시목록에서 해당 설정정보ID 목록만 추출해서 가져온다.
		if (!GlobalCom.isEmpty(config_id)) {
			List<ConfigurationListVO> finalResult = new ArrayList<ConfigurationListVO>();
			for (int i=0;i<result.size();i++) {
				if ((result.get(i).getConfig_id()).equalsIgnoreCase(config_id)) {
					finalResult.add(result.get(i));
					break;
				}
			}
			result = finalResult;
		}
		return result;
	}
	
	/**
	 * ConfigurationList 조회하기 위해 사용되는 정보를 사전 체크하는 서비스구현 클래스
	 * @param app_type		어플타입
	 * @return				ConfigurationList 정보가 들어있는 ConfigurationListVO 객체가 들어있는 List 객체
	 * @throws Exception
	 */
	@Override
	public List<ConfigurationListVO> getConfigChecklist(String app_type) throws Exception {
		// TODO Auto-generated method stub
		return dao.getConfigChecklist(app_type);
	}
	
	public void callException(String flag, String message) throws RuntimeException{
		throw new SmartUXException(flag, message);
	}
	
}
