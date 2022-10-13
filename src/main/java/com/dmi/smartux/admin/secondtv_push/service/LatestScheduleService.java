package com.dmi.smartux.admin.secondtv_push.service;

import com.dmi.smartux.admin.secondtv_push.vo.LatestContentVO;

import java.util.List;

/**
 * 최신회 관리자 Service
 *
 * @author dongho
 */
public interface LatestScheduleService {
	/**
	 * 최신회가 있는지 확인할 카테고리 리스트 조회
	 *
	 * @return 카테고리 리스트
	 * @throws Exception
	 */
	public List getCategoryList() throws Exception;

	/**
	 * 최신회 컨텐츠 리스트 조회
	 *
	 * @param startDate 검색 시작 날짜
	 * @param endDate 검색 종료 날짜
	 * @return 최신회 컨텐츠 리스트
	 * @throws Exception
	 */
	public List getLatestContentList(String startDate, String endDate) throws Exception;

	/**
	 * 푸쉬할 사용자의 카테고리에 맞는 전체 카운트를 가져온다.
	 *
	 * @param catID 카테고리 아이디
	 * @return 카테고리에 맞는 전체 카운트
	 * @throws Exception
	 */
	public int getUserListCount(String catID) throws Exception;

	/**
	 * 조회한 최신회의 푸쉬 해줄 사용자 조회
	 *
	 * @param latestContentVO 최신회 컨텐츠 VO
	 * @return 푸시 사용자 리스트
	 * @throws Exception
	 */
	public List getUserList(LatestContentVO latestContentVO) throws Exception;

	/**
	 * Push후 결과 저장
	 *
	 * @param completeVO 완료 VO
	 * @return 성공 여부
	 * @throws Exception
	 */
	public int updatePushComplete(LatestContentVO completeVO) throws Exception;
}
