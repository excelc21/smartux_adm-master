package com.dmi.smartux.admin.gpack.contents.service;

import java.util.List;

import com.dmi.smartux.admin.gpack.contents.vo.GpackContentsAutoVO;
import com.dmi.smartux.admin.gpack.contents.vo.GpackContentsVO;
import com.dmi.smartux.admin.gpack.contents.vo.GpackImcsCategoryAlbumVO;
import com.dmi.smartux.admin.gpack.contents.vo.GpackImcsCategoryVO;

public interface GpackContentsViewService {

	/**
	 * 직접편성 목록 조회
	 * @param contentsVO
	 * @return 
	 * @throws Exception
	 */
	public List<GpackContentsVO> getGpackContentsList(GpackContentsVO contentsVO) throws Exception;
	
	/**
	 * 직접편성 정보 조회
	 * @param contentsVO
	 * @return
	 * @throws Exception
	 */
	public GpackContentsVO getGpackContentsView(GpackContentsVO contentsVO) throws Exception;
	
	/**
	 * 직접편성 저장
	 * @param contentsVO
	 * @throws Exception
	 */
	public void insertGpackContents(GpackContentsVO contentsVO) throws Exception;

	/**
	 * 직접편성 수정
	 * @param contentsVO
	 * @throws Exception
	public int updateGpackContents(GpackContentsVO contentsVO) throws Exception;
	 */

	/**
	 * 직접편성 삭제
	 * @param contentsVO
	 * @throws Exception
	 */
	public void deleteGpackContents(String pack_id, String category_id, String[] contents_ids, String login_id) throws Exception;

	/**
	 * 자동편성(IMCS 카테고리 연동) 정보 조회
	 * @param contentsAutoVO
	 * @return
	 * @throws Exception
	 */
	public GpackContentsAutoVO getGpackContentsAutoView(GpackContentsAutoVO contentsAutoVO) throws Exception;
	
	/**
	 * 프로모션 자동편성(IMCS 카테고리 연동) 조회
	 * @param contentsAutoVO
	 * @return
	 * @throws Exception
	 */
	public GpackContentsAutoVO getGpackOneCategory(GpackContentsAutoVO contentsAutoVO) throws Exception;
	
	/**
	 * 자동편성(IMCS 카테고리 연동) 저장
	 * @param contentsAutoVO
	 * @throws Exception
	 */
	public void insertGpackContentsAuto(GpackContentsAutoVO contentsAutoVO) throws Exception;

	/**
	 * 자동편성(IMCS 카테고리 연동) 수정
	 * @param contentsAutoVO
	 * @throws Exception
	public int updateGpackContentsAuto(GpackContentsAutoVO contentsAutoVO) throws Exception;
	 */

	/**
	 * 자동편성(IMCS 카테고리 연동) 삭제
	 * @param contentsAutoVO
	 * @throws Exception
	 */
	public void deleteGpackContentsAuto(GpackContentsAutoVO contentsAutoVO) throws Exception;

	/**
	 * 직접편성 순서변경
	 * @param pack_id
	 * @param category_id
	 * @param vod_ids
	 * @param update_id
	 * @throws Exception
	 */
	public void updateGpackContentsOrderby(String pack_id, String category_id, String[] contents_ids, String update_id) throws Exception;
	
	/**
	 * IMCS 카테고리 조회
	 * @param category_id
	 * @param leaf_yn
	 * @return
	 * @throws Exception
	 */
	public List<GpackImcsCategoryVO> getImcsCategoryList(String category_id, String gubun, String callby) throws Exception;
	
	/**
	 * 미리보기 - IMCS 컨텐츠 조회 
	 * @param pack_id
	 * @param category_id
	 * @return
	 * @throws Exception
	 */
	public List<GpackImcsCategoryAlbumVO> getPrevCategoryContentsList(String pack_id, String category_id) throws Exception;
	
	/**
	 * 미리보기 - IMCS 컨텐츠 조회  (상용)
	 * @param pack_id
	 * @param category_id
	 * @return
	 * @throws Exception
	 */
	public List<GpackImcsCategoryAlbumVO> getPrevCategoryContentsBizList(String pack_id, String category_id) throws Exception;
}
