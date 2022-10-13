package com.dmi.smartux.admin.profile.service;

import java.util.List;

import com.dmi.smartux.admin.profile.vo.ProfileMasterVO;


/**
 * 광고 마스터 서비스 인터페이스
 *
 * @author Dongho, Shin
 */

public interface ProfileMasterService {
	/**
	 * 광고 마스터 리스트 조회
	 * @param service_type 
	 *
	 * @return 광고 마스터 리스트
	 * @throws Exception
	 */
	public List<ProfileMasterVO> getProfileMasterList(ProfileMasterVO profileMasterVO) throws Exception;


	
	/**
	 * 광고 마스터 등록
	 *
	 * @param profileMasterVO 광고 마스터 객체
	 * @throws Exception
	 */
	public void insertProfileMaster(ProfileMasterVO profileMasterVO) throws Exception;

	/**
	 * 광고 마스터 수정
	 *
	 * @param profileMasterVO 광고 마스터 객체
	 * @throws Exception
	 */
	public void updateProfileMaster(ProfileMasterVO profileMasterVO) throws Exception;

	/**
	 * 광고 마스터 삭제
	 *
	 * @param profileMasterVO 광고 마스터 객체
	 * @throws Exception
	 */
	public void deleteProfileMaster(ProfileMasterVO profileMasterVO) throws Exception;


	/**
	 * 광고 마스터 중복체크
	 *
	 * @param profileMasterVO 광고 마스터 객체
	 * @throws Exception
	 */
	public int checkProfileMaster(ProfileMasterVO vo);
}