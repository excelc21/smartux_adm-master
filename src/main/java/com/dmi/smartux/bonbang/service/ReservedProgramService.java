package com.dmi.smartux.bonbang.service;

import java.util.List;

import com.dmi.smartux.bonbang.vo.ReservedProgramParamVO;
import com.dmi.smartux.bonbang.vo.ReservedProgramVO;
import com.dmi.smartux.push.vo.PushResultVO;

/**
 * 시청 예약 관리 서비스 인터페이스
 * @author YJ KIM
 *
 */
public interface ReservedProgramService {
	
//	/**
//	 * Push Noti 시청예약 알림 설정
//	 * @param rVO 
//	 * @return
//	 * @throws Exception
//	 */
//	public PushResultVO setPushNoti(ReservedProgramParamVO rVO) throws Exception;
	
	/**
	 * 시청 예약 추가
	 * @param rVO
	 * @return	PushResultVO 객체(결과코드,메세지)
	 * @throws Exception
	 */
	public PushResultVO addReservedProgram(ReservedProgramParamVO rVO) throws Exception;
	
	/**
	 * 시청 예약 취소
	 * @param rVO
	 * @return PushResultVO 객체(결과코드,메세지)
	 * @throws Exception
	 */
	public PushResultVO removeReservedProgram(ReservedProgramParamVO rVO) throws Exception;
	
	/**
	 * 시청 예약 목록 조회
	 * @param rVO	ReservedProgramParamVO
	 * @return PushResultVO 객체(결과코드,메세지)
	 * @throws Exception
	 */
	public List<ReservedProgramVO> getReservedProgramList(ReservedProgramParamVO rVO) throws Exception;
	
	/**
	 * 시청 예약 목록 개수 조회
	 * @param rVO	ReservedProgramParamVO
	 * @return PushResultVO 객체(결과코드,메세지)
	 * @throws Exception
	 */
	public int getReservedProgramListTotalCnt(ReservedProgramParamVO rVO) throws Exception;
	
	/**
	 * 시청예약 정보 Push 알림
	 * @param rVO	ReservedProgramParamVO
	 * @return PushResultVO 객체(결과코드,메세지)
	 * @throws Exception
	 */
	public PushResultVO reqPushMessage(ReservedProgramParamVO rVO) throws Exception;
	
	/**
	 * 시청예약 정보 전체 삭제
	 * @param rVO	ReservedProgramParamVO
	 * @throws Exception
	 */
	public void removeAllReservedProgram(ReservedProgramParamVO rVO) throws Exception;
	
}
