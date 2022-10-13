package com.dmi.smartux.push;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.dmi.smartux.ios_push.dao.IOS_PushDao;
import com.dmi.smartux.push.service.PushService;
import com.dmi.smartux.push.service.impl.PushServiceImpl;
import com.dmi.smartux.push.vo.PushSocketVO;

/**
 * PushGW 연동 Call 시작 클래스
 *
 */
public class PushCall {
	
	private final Log logger = LogFactory.getLog(this.getClass());
	
	PushService psService;
	
	private String reg_id;
	private String noti_message;
	private int retryCtn;
	private String sa_id;
	private String stb_mac;
	private String wifi_mac;
	
	public PushCall(){}
	
	/**
	 * PushGW Call 생성자
	 * @param noti_message		Push Noti 전달값
	 * @param reg_id			RegistrationID
	 */
	public PushCall(String noti_message, String reg_id, int retry ,String sa_id,String stb_mac,String wifi_mac){
		logger.debug("PushCall  생성자");
		logger.debug("PushCall  noti_message = "+noti_message);
		logger.debug("PushCall  reg_id = "+reg_id);
		logger.debug("PushCall  retry = "+retry);
		logger.debug("PushCall  sa_id = "+sa_id);
		logger.debug("PushCall  stb_mac = "+stb_mac);
		logger.debug("PushCall  wifi_mac = "+wifi_mac);
		
		this.noti_message = noti_message;
		this.reg_id = reg_id;
		this.retryCtn = retry;
		this.sa_id = sa_id;
		this.stb_mac = stb_mac;
		this.wifi_mac = wifi_mac;
		
		psService = new PushServiceImpl();
	}
	
	public void call() {
		logger.debug("PushCall  메서드 시작");
		logger.debug("PushCall  메서드 Retry 카운트 = "+retryCtn);
		
		try {
			PushSocketVO psVO = psService.getPushGWSocket();
			psVO.setNoti_message(this.noti_message);
			psVO.setRegist_id(this.reg_id);
			psVO.setSa_id(this.sa_id);
			psVO.setStb_mac(this.stb_mac);
			psVO.setWifi_mac(this.wifi_mac);			
			psService.setNoti(psVO);
		} catch (Exception e) {
			retryCtn--;
			
			logger.debug("PushCall 자사단말 PUSH 에러 발생!!!!");
			logger.info("PushCall 자사단말 PUSH 에러 발생 call "+e.getClass().getName()+" : "+e.getMessage());
			if(retryCtn >= 0){
				call();
			}
		}
		logger.debug("PushCall  메서드 종료");
	}

}
