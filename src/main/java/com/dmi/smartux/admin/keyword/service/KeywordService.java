package com.dmi.smartux.admin.keyword.service;

import java.util.List;

import com.dmi.smartux.admin.keyword.vo.DeleteKeywordProcVo;
import com.dmi.smartux.admin.keyword.vo.InsertKeywordProcVo;
import com.dmi.smartux.admin.keyword.vo.KeywordOrderListResultVo;
import com.dmi.smartux.admin.keyword.vo.SearchKeywordResultVo;
import com.dmi.smartux.admin.keyword.vo.SelectKeywordDetailVo;

public interface KeywordService {
	
	/**
	 * 발화어 목록 조회
	 * @return
	 * @throws Exception
	 */
	List<SearchKeywordResultVo> getKeywordCateList(String keyword_id) throws Exception;

	/**
	 * 갤러리 상세 조회
	 * @param pGalleryId
	 * @return
	 * @throws Exception
	 */
	SelectKeywordDetailVo getKeywordDetail(String keyword_id) throws Exception;

	/**
	 * 발화어 정보 등록
	 * @param insertkeywordprocVo
	 * @throws Exception
	 */
	void insertKeywordProc(InsertKeywordProcVo insertkeywordprocVo) throws Exception;
	
	/**
	 * 발화어 정보 수정
	 * @param insertkeywordprocVo
	 * @throws Exception
	 */
	void updateKeywordProc(InsertKeywordProcVo insertkeywordprocVo) throws Exception;

	
	/**
	 * 발화어 삭제
	 * @param deletekeywordprocVo
	 * @return
	 * @throws Exception
	 */
	String deleteKeywordProc(DeleteKeywordProcVo deletekeywordprocVo) throws Exception;

	/**
	 * 발화어 순서변경을 위한 리스트
	 * @param keyword_id
	 * @return
	 * @throws Exception
	 */
	List<KeywordOrderListResultVo> getKeywordOrderList(String keyword_id) throws Exception;

	/**
	 * 발화어 순서변경
	 * @param keyword_ids
	 * @param mod_id
	 * @throws Exception
	 */
	void keywordOrderChangeProc(String[] keyword_ids, String mod_id) throws Exception;

}
