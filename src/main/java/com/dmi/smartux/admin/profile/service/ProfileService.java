package com.dmi.smartux.admin.profile.service;

import java.util.List;

import com.dmi.smartux.admin.profile.vo.ProfileVO;


/**
 * 광고 관리 서비스 인터페이스
 *
 * @author Dongho, Shin
 */

public interface ProfileService {
	/**
	 * 광고 목록 조회
	 *
	 * @param adsVO 광고 객체
	 * @return 광고 목록
	 * @throws Exception
	 */
	public List<ProfileVO> getProfileList(ProfileVO profileVO) throws Exception;

	/**
	 * 순서 변경을 위해 모든 리스트를 가져온다.
	 *
	 * @param masterID 마스터 아이디
	 * @return 모든 리스트
	 * @throws Exception
	 */
/*	public List<ProfileVO> getProfileAllList(String masterID) throws Exception;*/

	/**
	 * 광고 조회
	 *
	 * @param number 게시물 번호
	 * @return 광고 객체
	 * @throws Exception
	 */
	public ProfileVO getProfile(String profileImgId) throws Exception;


	/**
	 * 광고 삽입
	 *
	 * @param adsVO 광고 객체
	 * @throws Exception
	 */
	public void insertProfile(ProfileVO profileVO) throws Exception;

	/**
	 * 광고 개수 조회
	 *
	 * @param adsVO 광고 객체
	 * @return 광고 개수
	 * @throws Exception
	 */
	public int getProfileListCtn(ProfileVO profileVO) throws Exception;

	/**
	 * 광고 삭제
	 *
	 * @param numbers  게시물 번호 (ex: 1,2,3,4,)
	 * @param cookieID 접근 아이디
	 * @param ip       접근 아이피
	 * @throws Exception
	 */
	public void deleteProfile(String profileImgIds, String cookieID, String ip) throws Exception;

	/**
	 * 광고 업데이트
	 *
	 * @param adsVO 광고 객체
	 * @throws Exception
	 */
	public void updateProfile(ProfileVO profileVO) throws Exception;

	/**
	 * 광고 순서 변경 (orderList의 순서대로 순서를 변경)
	 *
	 * @param orderList regNumber List
	 * @throws Exception
	 */
	public int updateSearch(int[] orders, String profileMstId, String[] seqs) throws Exception;

	

	
}
