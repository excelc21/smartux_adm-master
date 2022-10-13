package com.dmi.smartux.admin.starrating.service;

import java.util.List;

import com.dmi.smartux.admin.starrating.vo.HistoryVO;
import com.dmi.smartux.admin.starrating.vo.StarRatingSearchVO;
import com.dmi.smartux.admin.starrating.vo.StarRatingVO;

public interface StarRatingService {
	
	/**
	 * 별점 목록 조회
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public List<StarRatingVO> getStarRatingList(StarRatingSearchVO vo) throws Exception;
	
	/**
	 * 별점 목록 개수 조회
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public int getStarRatingListCnt(StarRatingSearchVO vo) throws Exception;
	
	/**
	 * 별점 등록
	 * @param vo
	 * @throws Exception
	 */
	public void insertProc(StarRatingVO vo) throws Exception;
	
	/**
	 * 별점 수정
	 * @param vo
	 * @throws Exception
	 */
	public void updateProc(StarRatingVO vo) throws Exception;
	
	/**
	 * 이미지 서버 URL 조회
	 * @return
	 * @throws Exception
	 */
	public String getImgServer() throws Exception;
	
	/**
	 * 별점 제목 조회
	 * @param sr_id
	 * @return
	 * @throws Exception
	 */
	public String getTitle(String sr_id) throws Exception;
	
	/**
	 * 별점 상세 목록 조회
	 * @param sr_pid
	 * @return
	 * @throws Exception
	 */
	public String getSrList(String sr_pid) throws Exception;
	
	/**
	 * 활성화상태 조회
	 * @param system_gb
	 * @return
	 * @throws Exception
	 */
	public int getUseYnCnt(String system_gb) throws Exception;
	
	/**
	 * 활성화상태 수정
	 * @param vo
	 * @throws Exception
	 */
	public void updateUseYn(StarRatingVO vo) throws Exception;
	
	/**
	 * 별점 내역 조회
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public List<HistoryVO> getAlbumHistoryList(StarRatingSearchVO vo) throws Exception;
	
	/**
	 * 별점 내역 개수 조회
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public int getAlbumHistoryListCnt(StarRatingSearchVO vo) throws Exception;
	
	/**
	 * 별점 주기 내역 조회
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public List<HistoryVO> getSrHistoryList(StarRatingSearchVO vo) throws Exception;
	
	/**
	 * 별점 주기 내역 개수 조회
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public int getSrHistoryListCnt(StarRatingSearchVO vo) throws Exception;
	
	/**
	 * 별점 주기 내역 평균별점 조회(검색조건)
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public String getSrHistoryListAvg(StarRatingSearchVO vo) throws Exception;
	
	/**
	 * 앨범정보 조회
	 * @param sr_id
	 * @param album_id
	 * @return
	 * @throws Exception
	 */
	public HistoryVO getAlbum(String sr_id, String album_id) throws Exception;
}
