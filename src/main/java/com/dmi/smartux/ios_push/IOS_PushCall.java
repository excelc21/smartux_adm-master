package com.dmi.smartux.ios_push;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.dmi.smartux.ios_push.dao.IOS_PushDao;
import com.dmi.smartux.ios_push.service.IOS_PushService;
import com.dmi.smartux.ios_push.service.impl.IOS_PushServiceImpl;
import com.dmi.smartux.ios_push.vo.IOS_PushSocketVO;

/**
 * IOS PushGW 연동 Thread 시작 클래스
 *
 */
public class IOS_PushCall{

	private final Log logger = LogFactory.getLog(this.getClass());

	IOS_PushService psService;
	
	private String user_id;
	private String noti_message;
	private String user_param;
	private int retryCtn;
	private String sa_id;
	private String stb_mac;
	private String wifi_mac;
	
	public IOS_PushCall(){}
	
	/**
	 * IOS PushThread 생성자
	 * @param noti_message	Push Noti 알림 메세지
	 * @param user_id		맥주소("."제거 , 대문자)
	 * @param user_param	Push Noti 전달값
	 */
	public IOS_PushCall(String noti_message, String user_id,String user_param,int retryCtn,String sa_id,String stb_mac,String wifi_mac){
		logger.debug("IOS_PushCall  생성자");
		logger.debug("IOS_PushCall  noti_message = "+noti_message);
		logger.debug("IOS_PushCall  user_id = "+user_id);
		logger.debug("IOS_PushCall  user_param = "+user_param);
		logger.debug("IOS_PushCall  retryCtn = "+retryCtn);
		logger.debug("PushCall  sa_id = "+sa_id);
		logger.debug("PushCall  stb_mac = "+stb_mac);
		logger.debug("PushCall  wifi_mac = "+wifi_mac);
		
		this.noti_message = noti_message;
		this.user_id = user_id;
		this.user_param = user_param;
		this.retryCtn = retryCtn;
		this.sa_id = sa_id;
		this.stb_mac = stb_mac;
		this.wifi_mac = wifi_mac;
		
		psService = new IOS_PushServiceImpl();
	}

	public void call() {
		logger.debug("IOS_PushCall  메서드 시작");
		logger.debug("IOS_PushCall  메서드 retryCtn 카운트 = "+retryCtn);
		
		try {
			IOS_PushSocketVO psVO = psService.getPushGWSocket();
			psVO.setNoti_message(this.noti_message);
			psVO.setUser_id(this.user_id);
			psVO.setUser_param(this.user_param);
			psVO.setSa_id(this.sa_id);
			psVO.setStb_mac(this.stb_mac);
			psVO.setWifi_mac(this.wifi_mac);
			psService.setNoti(psVO);
		} catch (Exception e) {
			retryCtn--;
			logger.debug("PushCall 자사단말 PUSH 에러 발생!!!!");
			logger.debug("PushCall 자사단말 PUSH 에러 발생 removeReservedProgram "+e.getClass().getName()+" : "+e.getMessage());
			if(retryCtn >= 0){
				call();
			}
		}
		logger.debug("IOS_PushCall  메서드 종료");
	}
	
	
}
