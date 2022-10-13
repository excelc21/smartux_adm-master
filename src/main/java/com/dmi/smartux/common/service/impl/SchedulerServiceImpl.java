package com.dmi.smartux.common.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.service.SchedulerService;
import com.dmi.smartux.common.util.GlobalCom;

@Service("SmartUXScheduleService")
public class SchedulerServiceImpl implements SchedulerService {

	private final Log logger = LogFactory.getLog(this.getClass()); 
	
	@Override
	/**
	 * 주기적으로 캐쉬 URL을 호출
	 */
	public void checkCache() throws Exception {
		// TODO Auto-generated method stub
		
		logger.debug("checkCache 스케줄러 작업 시작 시간 : " + System.currentTimeMillis());
		// Spring에서 관리하는 프로퍼티 키 값들중 .CacheScheduleURL을 포함하는 key값들을 찾는다
		List<String> keyset = SmartUXProperties.getKeys(".CacheScheduleURL");
		for(String item : keyset){
			// 리턴받은 keyset 중에 .CacheScheduleURL로 끝나는 key를 찾는다
			logger.debug("item : " + item);
			if(item.endsWith(".CacheScheduleURL")){
				String protocolItem = item;
				String protocolKey = protocolItem.replace(".CacheScheduleURL", ".protocol");			// .CacheScheduleUrl로 끝난 것을 .protocol 로 바꾸어서 API가 사용할 프로토콜을 읽어온다
				String protocolName = SmartUXProperties.getProperty(protocolKey);
				logger.debug("protocolKey : " + protocolKey);
				logger.debug("protocolName : " + protocolName);
				callCache(SmartUXProperties.getProperty(item), protocolName);
			}
		}
		
		logger.debug("checkCache 스케줄러 작업 마친 시간 : " + System.currentTimeMillis());
		
	}
	
	/**
	 * 주기적으로 캐쉬 URL을 호출(실시간시청률 용) - 주기의 텀이 너무 차이가 나서 별로도 구현됨
	 */
	public void checkCache2nd() throws Exception {
		logger.debug("checkCache2nd 스케줄러 작업 시작 시간 : " + System.currentTimeMillis());
		// Spring에서 관리하는 프로퍼티 키 값들중 .CacheScheduleURL을 포함하는 key값들을 찾는다
		List<String> keyset = SmartUXProperties.getKeys(".2ndCacheScheduleURL");
		for(String item : keyset){
			// 리턴받은 keyset 중에 .CacheScheduleURL로 끝나는 key를 찾는다
			logger.debug("item : " + item);
			if(item.endsWith(".2ndCacheScheduleURL")){
				String protocolItem = item;
				String protocolKey = protocolItem.replace(".2ndCacheScheduleURL", ".protocol");			// .CacheScheduleUrl로 끝난 것을 .protocol 로 바꾸어서 API가 사용할 프로토콜을 읽어온다
				String protocolName = SmartUXProperties.getProperty(protocolKey);
				logger.debug("protocolKey : " + protocolKey);
				logger.debug("protocolName : " + protocolName);
				callCache(SmartUXProperties.getProperty(item), protocolName);
			}
		}
		logger.debug("checkCache2nd 스케줄러 작업 마친 시간 : " + System.currentTimeMillis());
	}
	
	private void callCache(String url, String protocolName){
		String host = "";
		int port = 0;
		String param = "";
		int timeout = 0;
		int retrycnt = Integer.parseInt(SmartUXProperties.getProperty("scheduler.retrycnt"));
		
		try{
			host = SmartUXProperties.getProperty("scheduler.host");
			port = Integer.parseInt(SmartUXProperties.getProperty("scheduler.port"));
			param = "callByScheduler=Y";
			
			timeout = Integer.parseInt(SmartUXProperties.getProperty("scheduler.timeout"));
			
			//GlobalCom.callAPI(host, port, url, param, timeout, retrycnt, protocolName);
			GlobalCom.callAPI(url, param, timeout, retrycnt, protocolName);
		}catch(Exception e){				
			
		}
	}
	
}
