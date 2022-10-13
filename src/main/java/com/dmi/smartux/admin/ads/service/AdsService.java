package com.dmi.smartux.admin.ads.service;

import java.util.List;

import com.dmi.smartux.admin.ads.vo.AdsVO;

/**
 * 광고 관리 서비스 인터페이스
 *
 * @author Dongho, Shin
 */

public interface AdsService {
	/**
	 * 광고 목록 조회
	 *
	 * @param adsVO 광고 객체
	 * @return 광고 목록
	 * @throws Exception
	 */
	public List<AdsVO> getAdsList(AdsVO adsVO) throws Exception;

	/**
	 * 순서 변경을 위해 모든 리스트를 가져온다.
	 *
	 * @param masterID 마스터 아이디
	 * @return 모든 리스트
	 * @throws Exception
	 */
	public List<AdsVO> getAllList(String masterID) throws Exception;

	/**
	 * 광고 조회
	 *
	 * @param number 게시물 번호
	 * @return 광고 객체
	 * @throws Exception
	 */
	public AdsVO getAds(int number) throws Exception;


	/**
	 * 광고 삽입
	 *
	 * @param adsVO 광고 객체
	 * @throws Exception
	 */
	public int insertAds(AdsVO adsVO) throws Exception;

	/**
	 * 광고 개수 조회
	 *
	 * @param adsVO 광고 객체
	 * @return 광고 개수
	 * @throws Exception
	 */
	public int getAdsListCtn(AdsVO adsVO) throws Exception;

	/**
	 * 광고 삭제
	 *
	 * @param numbers  게시물 번호 (ex: 1,2,3,4,)
	 * @param cookieID 접근 아이디
	 * @param ip       접근 아이피
	 * @throws Exception
	 */
	public void deleteAds(String numbers, String cookieID, String ip) throws Exception;

	/**
	 * 광고 업데이트
	 *
	 * @param adsVO 광고 객체
	 * @throws Exception
	 */
	public void updateAds(AdsVO adsVO) throws Exception;

	/**
	 * 광고 순서 변경 (orderList의 순서대로 순서를 변경)
	 *
	 * @param orderList regNumber List
	 * @throws Exception
	 */
	public void changeOrder(List<String> orderList) throws Exception;

	/**
	 * 라이브된 광고 수 조회
	 *
	 * @param masterID 광고 아이디
	 * @return 광고 아이디에 속해 있는 라이브 광고 수
	 * @throws Exception
	 */
	public int getLiveCount(String masterID) throws Exception;
	
	/**
	 * 만료된 라이브 광고의 LiveType을 D로 변경
	 *
	 * @throws Exception
	 */
	public void checkExpire() throws Exception;

	/**
	 * 배너버전 업데이트
	 *
	 * @throws Exception
	 */
	public void insertBannerVersion();

	/**
	 * 상단메뉴 즉시적용 시간체크
	 *
	 * @throws Exception
	 */
	public String checkLastApply();

	/**
	 * 현제시간조회
	 *
	 * @throws Exception
	 */
	public String getNow();
}
