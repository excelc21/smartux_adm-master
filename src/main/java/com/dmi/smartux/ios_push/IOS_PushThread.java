package com.dmi.smartux.ios_push;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dmi.smartux.ios_push.service.IOS_PushService;
import com.dmi.smartux.ios_push.service.impl.IOS_PushServiceImpl;
import com.dmi.smartux.ios_push.vo.IOS_PushSocketVO;

/**
 * IOS PushGW 연동 Thread 시작 클래스
 * @author YJ KIM
 *
 */
public class IOS_PushThread implements Runnable{

	private final Log logger = LogFactory.getLog(this.getClass());

	IOS_PushService psService;
	
	private String user_id;
	private String noti_message;
	private String user_param;
	private int retryCtn;
	private String sa_id;
	private String stb_mac;
	private String wifi_mac;
	
	public IOS_PushThread(){}
	
	/**
	 * IOS PushThread 생성자
	 * @param noti_message	Push Noti 알림 메세지
	 * @param user_id		맥주소("."제거 , 대문자)
	 * @param user_param	Push Noti 전달값
	 */
	public IOS_PushThread(String noti_message, String user_id,String user_param,int retryCtn,String sa_id,String stb_mac,String wifi_mac){
		logger.debug("IOS_PushThread 쓰레드 생성자");
		logger.debug("IOS_PushThread 쓰레드 noti_message = "+noti_message);
		logger.debug("IOS_PushThread 쓰레드 user_id = "+user_id);
		logger.debug("IOS_PushThread 쓰레드 user_param = "+user_param);
		logger.debug("IOS_PushThread 쓰레드 retryCtn = "+retryCtn);
		logger.debug("PushThread 쓰레드 sa_id = "+sa_id);
		logger.debug("PushThread 쓰레드 stb_mac = "+stb_mac);
		logger.debug("PushThread 쓰레드 wifi_mac = "+wifi_mac);
		
		this.noti_message = noti_message;
		this.user_id = user_id;
		this.user_param = user_param;
		this.retryCtn = retryCtn;
		this.sa_id = sa_id;
		this.stb_mac = stb_mac;
		this.wifi_mac = wifi_mac;
		
		psService = new IOS_PushServiceImpl();
	}

	@Override
	public void run() {
		logger.debug("IOS_PushThread 쓰레드 메서드 시작");
		logger.debug("IOS_PushThread 쓰레드 메서드 retryCtn 카운트 = "+retryCtn);
		
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
			if(retryCtn >= 0){
				run();
			}
			logger.debug("PushThread 자사단말 PUSH 에러 발생!!!!");
			logger.debug("PushThread 자사단말 PUSH 에러 발생 removeReservedProgram "+e.getClass().getName());
			logger.debug("PushThread 자사단말 PUSH 에러 발생 removeReservedProgram "+e.getMessage());
		}
		logger.debug("PushThread 쓰레드 메서드 종료");
	}
	
	
}
