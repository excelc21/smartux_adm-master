package com.dmi.smartux.push.service;

import org.springframework.scheduling.annotation.Async;

import com.dmi.smartux.bonbang.vo.ReservedProgramParamVO;
import com.dmi.smartux.push.vo.PushResultVO;
import com.dmi.smartux.push.vo.PushSocketVO;

/**
 * Push G/W 연동 서비스 인터페이스
 * @author YJ KIM
 *
 */
public interface PushService {

	/**
	 * Push Noti 메시지 전달
	 * @param psVO	PushSocketVO
	 * @return	결과코드,결과메세지
	 * @throws Exception
	 */
	public PushResultVO setNoti(PushSocketVO psVO)throws Exception;
	
	/**
	 * PushGW 소켓 중 사용가능한 소켓 조회 메서드
	 * @return 결과코드,결과메세지
	 */
	public PushSocketVO getPushGWSocket() throws Exception;
	
	
	/**
	 * PushGW 소켓을 전부 활성화 메서드
	 * @return 결과코드,결과메세지
	 */
	public void setPushGWSocketAllOpen() throws Exception;
	
	/**
	 * PushGW 소켓을 전부 비활성화 메소드
	 * @throws Exception
	 */
	public void setPushGWSocketAllClose() throws Exception;
	
	/**
	 * PushGW 소켓 연결 메서드
	 * @return 결과코드,결과메세지
	 */
	
	/**
	 * PushGW 소켓 연결 메서드
	 * @param addQueue	소켓관리 큐 저장 여부(true: 큐저장 false: 큐미저장)
	 * @return	결과코드,결과메세지
	 * @throws Exception
	 */
	public PushSocketVO OpenSocket(boolean addQueue) throws Exception;
	
	/**
	 * PushGW 소켓 및 큐 해제 메서드
	 * @param psVO	PushSocketVO
	 */
	public void closeSocketQueue(PushSocketVO psVO) throws Exception;
	
	/**
	 * PushGW 소켓 해제 메서드
	 * @param psVO PushSocketVO
	 */
	public void closeSocket(PushSocketVO psVO) throws Exception;
	
	/**
	 * PushGW 소켓 In/Out 스트림 활성화 메서드
	 * @param psVO PushSocketVO
	 */
	public void openInOutStream(PushSocketVO psVO) throws Exception;
	
	@Async
	public void pushSendForaddReservedProgram(ReservedProgramParamVO rVO);
}
