package com.dmi.smartux.admin.keyword.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.dmi.smartux.admin.keyword.vo.DeleteKeywordProcVo;
import com.dmi.smartux.admin.keyword.vo.InsertKeywordProcVo;
import com.dmi.smartux.admin.keyword.vo.KeywordOrderListResultVo;
import com.dmi.smartux.admin.keyword.vo.KeywordRelationResultVo;
import com.dmi.smartux.admin.keyword.vo.SearchKeywordResultVo;
import com.dmi.smartux.admin.keyword.vo.SelectKeywordDetailVo;
import com.dmi.smartux.admin.keyword.vo.UpdateKeywordOrderProcVo;
import com.dmi.smartux.common.dao.CommonIptvDao;

@Repository
public class KeywordDao extends CommonIptvDao {
	
	/**
	 * 발화어 목록 조회
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<SearchKeywordResultVo> getKeywordCateList(String keyword_id) throws DataAccessException {
		return getSqlMapClientTemplate().queryForList("admin_keyword.getKeywordCateList", keyword_id);
	}

	/**
	 * 발화어 상세 조회
	 * @param keyword_id
	 * @return
	 * @throws DataAccessException
	 */
	public SelectKeywordDetailVo getKeywordDetail(String keyword_id) throws DataAccessException {
		return (SelectKeywordDetailVo) getSqlMapClientTemplate().queryForObject("admin_keyword.getKeywordDetail", keyword_id);	
	}

	/**
	 * 발화어 상세정보 등록
	 * @param insertkeywordprocVo
	 * @throws DataAccessException
	 */
	public void insertKeyword(InsertKeywordProcVo insertkeywordprocVo) throws DataAccessException {
		getSqlMapClientTemplate().insert("admin_keyword.insertKeyword",insertkeywordprocVo);
	}

	/**
	 * 발화어 상세정보 수정
	 * @param insertkeywordprocVo
	 * @throws DataAccessException
	 */
	public void updateKeyword(InsertKeywordProcVo insertkeywordprocVo) throws DataAccessException {
		getSqlMapClientTemplate().update("admin_keyword.updateKeyword",insertkeywordprocVo);
	}

	/**
	 * DB 버전 업데이트
	 * @throws DataAccessException
	 */
	public void updateVersion() throws DataAccessException {
		getSqlMapClientTemplate().update("admin_keyword.updateVersion");
	}
	
	/**
	 * 현재 발화어 정보의 자식 발화어가 존재하는지 확인
	 * @param keyword_id
	 * @return
	 * @throws DataAccessException
	 */
	public KeywordRelationResultVo getKeywordRelation(String keyword_id) throws DataAccessException {
		return (KeywordRelationResultVo) getSqlMapClientTemplate().queryForObject("admin_keyword.getKeywordRelation", keyword_id);	
	}
	
	/**
	 * 발화어 정보를 삭제한다.
	 * @param deletekeywordprocVo
	 * @throws DataAccessException
	 */
	public void deleteKeyword(DeleteKeywordProcVo deletekeywordprocVo) throws DataAccessException {
		getSqlMapClientTemplate().update("admin_keyword.deleteKeyword", deletekeywordprocVo);	
	}

	/**
	 * 발화어 순서변경을 위한 리스트
	 * @param keyword_id
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<KeywordOrderListResultVo> getKeywordOrderList(String keyword_id) throws DataAccessException {
		return getSqlMapClientTemplate().queryForList("admin_keyword.getKeywordOrderList", keyword_id);	
	}
	
	/**
	 * 갤러리 순서변경
	 * @param updatekeywordorderprocVo
	 * @throws DataAccessException
	 */
	public void updateKeywordOrder(UpdateKeywordOrderProcVo updatekeywordorderprocVo) throws DataAccessException {
		getSqlMapClientTemplate().update("admin_keyword.updateKeywordOrder",updatekeywordorderprocVo);
	}

}
