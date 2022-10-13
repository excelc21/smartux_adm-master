package com.dmi.smartux.ios_push.service;

import com.dmi.smartux.ios_push.vo.IOS_PushResultVO;
import com.dmi.smartux.ios_push.vo.IOS_PushSocketVO;
import com.dmi.smartux.push.vo.PushResultVO;

/**
 * Push G/W 연동 서비스 인터페이스
 * @author YJ KIM
 *
 */
public interface IOS_PushService {

	/**
	 * Push Noti 메시지 전달
	 * @param psVO
	 * @return
	 * @throws Exception
	 */
	public PushResultVO setNoti(IOS_PushSocketVO psVO)throws Exception;
	
	/**
	 * PushGW 소켓 중 사용가능한 소켓 조회 메서드
	 * @return
	 */
	public IOS_PushSocketVO getPushGWSocket() throws Exception;
	
	
	/**
	 * PushGW 소켓을 전부 활성화 메서드
	 * @return
	 */
	public void setPushGWSocketAllOpen() throws Exception;
	
	/**
	 * PushGW 소켓을 전부 비활성화 메소드
	 * @throws Exception
	 */
	public void setPushGWSocketAllClose() throws Exception;
	
	/**
	 * PushGW 소켓 연결 메서드
	 * @return
	 */
	public IOS_PushSocketVO OpenSocket() throws Exception;
	
	/**
	 * PushGW 소켓 및 큐 해제 메서드
	 * @param psVO
	 */
	public void closeSocketQueue(IOS_PushSocketVO psVO) throws Exception;
	
	/**
	 * PushGW 소켓 해제 메서드
	 * @param psVO
	 */
	public void closeSocket(IOS_PushSocketVO psVO) throws Exception;
	
	/**
	 * PushGW 소켓 In/Out 스트림 활성화 메서드
	 * @param psVO
	 */
	public void openInOutStream(IOS_PushSocketVO psVO) throws Exception;
}
