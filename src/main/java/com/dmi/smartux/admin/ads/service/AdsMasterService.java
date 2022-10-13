package com.dmi.smartux.admin.ads.service;

import java.util.List;

import com.dmi.smartux.admin.ads.vo.AdsMasterVO;

/**
 * 광고 마스터 서비스 인터페이스
 *
 * @author Dongho, Shin
 */

public interface AdsMasterService {
	/**
	 * 광고 마스터 아이디생성
	 *
	 * @return 광고 마스터 아이디생성
	 * @throws Exception
	 */
	public String getAdsMasterId() throws Exception;

	/**
	 * 광고 마스터 리스트 조회
	 *
	 * @return 광고 마스터 리스트
	 * @throws Exception
	 */
	public List<AdsMasterVO> getAdsMasterList() throws Exception;


	/**
	 * 광고 마스터 조회
	 * @param adsMasterVO
	 * @return
	 * @throws Exception
	 */
	public AdsMasterVO getAdsMaster(AdsMasterVO adsMasterVO) throws Exception;

	/**
	 * 광고 마스터 등록
	 *
	 * @param adsMasterVO 광고 마스터 객체
	 * @throws Exception
	 */
	public void insertAdsMaster(AdsMasterVO adsMasterVO) throws Exception;

	/**
	 * 광고 마스터 수정
	 *
	 * @param adsMasterVO 광고 마스터 객체
	 * @throws Exception
	 */
	public void updateAdsMaster(AdsMasterVO adsMasterVO) throws Exception;

	/**
	 * 광고 마스터 삭제
	 *
	 * @param adsMasterVO 광고 마스터 객체
	 * @throws Exception
	 */
	public void deleteAdsMaster(AdsMasterVO adsMasterVO) throws Exception;
}