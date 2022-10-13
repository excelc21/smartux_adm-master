package com.dmi.smartux.admin.poster.service;

import java.util.List;

import com.dmi.smartux.admin.poster.vo.PosterSearchVO;
import com.dmi.smartux.admin.poster.vo.PosterVO;

public interface PosterAdminService {
	
	/**
	 * 전용 포스터 목록 조회
	 * @return 전용포스트 목록
	 * @throws Exception
	 */
	public List<PosterVO> getPosterList(PosterSearchVO posterSearchVo) throws Exception;
	
	/**
	 * 전용 포스터 목록 조회
	 * @return 전용포스트 목록
	 * @throws Exception
	 */
	public int getPosterListCount(PosterSearchVO posterSearchVo) throws Exception;
	
	/**
	 * 전용 포스터 조회
	 * @return 전용포스트
	 * @throws Exception
	 */
	public PosterVO getSelectPoster(String albumId, String serviceType) throws Exception;
	
	/**
	 * 전용 포스터 등록
	 * @param serviceType
	 * @param albumId
	 * @param widthImg
	 * @param heightImg
	 * @param createId
	 * @throws Exception
	 */
	public void insertPoster(String serviceType, String albumId, String widthImg, String heightImg,String createId) throws Exception;
	
	/**
	 * 전용 포스터 수정
	 * @param serviceType
	 * @param albumId
	 * @param widthImg
	 * @param heightImg
	 * @param createId
	 * @throws Exception
	 */
	public void updatePoster(String serviceType, String albumId, String widthImg, String heightImg,String createId) throws Exception;
	
	/**
	 * 전용 포스터 삭제
	 * @param album_ids
	 * @throws Exception
	 */
	public void deletePoster(String [] albumIds, String [] serviceTypes) throws Exception;
}
