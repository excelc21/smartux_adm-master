package com.dmi.smartux.admin.news.service;

import com.dmi.smartux.admin.news.vo.NewsVO;
import com.dmi.smartux.admin.news.vo.TargetVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 새소식 Service
 *
 * @author dongho
 */
public interface NewsService {
	/**
	 * 새소식 리스트 조회
	 *
	 * @param newsVO 새소식 객체
	 * @return 새소식 리스트
	 * @throws Exception
	 */
	public List getList(NewsVO newsVO) throws Exception;

	/**
	 * 새소식 조회
	 *
	 * @param number 새소식 게시물 번호
	 * @return 새소식
	 * @throws Exception
	 */
	public NewsVO getData(String number) throws Exception;

	/**
	 * 새소식 전체 개수
	 *
	 * @param newsVO 새소식 객체
	 * @return 전체 개수
	 * @throws Exception
	 */
	public int getCount(NewsVO newsVO) throws Exception;

	/**
	 * 새소식 등록
	 *
	 * @param newsVO 새소식 객체
	 * @return 삽입된 새소식 번호
	 * @throws Exception
	 */
	public int insert(NewsVO newsVO) throws Exception;

	/**
	 * 새소식 수정
	 *
	 * @param newsVO 새소식 객체
	 * @return 성공 1 / 실패 0
	 * @throws Exception
	 */
	public int update(NewsVO newsVO) throws Exception;

	/**
	 * 새소식 삭제
	 *
	 * @param newsVO 새소식 객체
	 * @return 삭제된 행 개수
	 * @throws Exception
	 */
	public int delete(NewsVO newsVO) throws Exception;

	/**
	 * WAS가 작업할 새소식 작업을 가져온다.
	 *
	 * @param index WAS Index
	 * @return 작업할 객체
	 * @throws Exception
	 */
	public NewsVO getMarkingNews(String index) throws Exception;

	/**
	 * 새소식 전송 상태값 변경
	 *
	 * @param resultMap 결과맵(result, index)
	 * @return 성공 1 / 실패 0
	 * @throws Exception
	 */
	public int updateNewsStatus(HashMap<String, String> resultMap) throws Exception;

    /**
     * 메시지 유효성검사(메시지길이체크)
     *
     * @param newsVo 새소식 객체
     * @throws Exception
     */
    public Map<String,String> checkPushMessage(NewsVO newsVo) throws Exception;

    /**
     * 타겟룰 조회
     *
     * @param regNumber 타겟룰 시퀀스
     * @return 타겟룰 객체
     * @throws Exception
     */
    public TargetVO getTargetRule(int regNumber) throws Exception;
}
