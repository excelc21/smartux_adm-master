package com.dmi.smartux.common.base;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.service.CacheService;
import com.dmi.smartux.common.util.GlobalCom;

/**
 * Service 의 공통적인 부분을 가진다.
 * 
 * @author jch82@naver.com
 */
public class BaseService {
	protected final Log logger = LogFactory.getLog ( this.getClass ( ) );

	/**
	 * 전체적인 캐쉬를 넣고 빼며 관리한다.
	 */
	@Autowired
	protected CacheService service;

	/**
	 * @param callByScheduler 스케줄러 호출과 관리자의 강제 호출을 구분하기 위한 플래그<br/>
	 *            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;관리자 호출 시 A 스케줄러 호출 시 Y, 단말 호출 시 N 을 사용한다.
	 * @return 캐쉬정보를 업데이트 하는 시간 간격
	 * @throws Exception
	 */
	protected long getInterval ( String callByScheduler, String propertyName ) {
		String intervalStr;
		try {
			intervalStr = SmartUXProperties.getProperty ( propertyName );
		} catch ( Exception exception ) {
			logger.error ( "getInterval() is error.", exception );
			intervalStr = "0";
		}
		return GlobalCom.getCacheInterval ( callByScheduler, intervalStr );
	}

	/**
	 * IntervalStr을 바로 전덜하여 체크할수 있도록 한다
	 * 
	 * @param callByScheduler
	 * @param intervalStr
	 * @return
	 */
	protected long getIntervalDirect ( String callByScheduler, String intervalStr ) {
		return GlobalCom.getCacheInterval ( callByScheduler, intervalStr );
	}

}
