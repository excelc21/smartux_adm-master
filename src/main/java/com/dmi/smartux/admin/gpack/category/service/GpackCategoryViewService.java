package com.dmi.smartux.admin.gpack.category.service;

import java.util.List;

import com.dmi.smartux.admin.gpack.category.vo.GpackCategoryVO;


public interface GpackCategoryViewService {

	/**
	 * 카테고리의 콘텐츠 수 조회 (직접편성, IMCS카테고리 연동)
	 * @param categoryVO
	 * @return
	 * @throws Exception
	 */
	public int countGpackCategoryContents(GpackCategoryVO categoryVO) throws Exception;
	
	/**
	 * 새로운 카테고리 ID 생성
	 * @return
	 * @throws Exception
	 */
	public String getNewGpackCategoryId() throws Exception;
	
	/**
	 * 카테고리 목록 조회
	 * @param categoryVO
	 * @return 
	 * @throws Exception
	 */
	public List<GpackCategoryVO> getGpackCategoryList(GpackCategoryVO categoryVO) throws Exception;
	
	/**
	 * 카테고리 정보 조회
	 * @param categoryVO
	 * @return
	 * @throws Exception
	 */
	public GpackCategoryVO getGpackCategoryView(GpackCategoryVO categoryVO) throws Exception;
	
	/**
	 * 카테고리 저장
	 * @param categoryVO
	 * @throws Exception
	 */
	public void insertGpackCategory(GpackCategoryVO categoryVO) throws Exception;

	/**
	 * 카테고리 수정
	 * @param categoryVO
	 * @throws Exception
	public int updateGpackCategory(GpackCategoryVO categoryVO) throws Exception;
	 */
	
	/**
	 * 카테고리 순서바꾸기
	 * @param pack_id
	 * @param category_ids
	 * @param update_id
	 * @throws Exception
	 */
	public void updateGpackCategoryOrderby(String pack_id, String[] category_ids, String update_id) throws Exception;

	/**
	 * 카테고리 삭제
	 * @param categoryVO
	 * @throws Exception
	 */
	public void deleteGpackCategory(String pack_id, String[] category_ids) throws Exception;
	
	/**
	 * 미리보기 카테고리 목록 조회 
	 * @param categoryVO
	 * @param preview_gb
	 * @return
	 * @throws Exception
	 */
	public List<GpackCategoryVO> previewGpackCategory(GpackCategoryVO categoryVO, String preview_gb) throws Exception;
	
}
