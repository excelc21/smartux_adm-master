package com.dmi.smartux.admin.terms.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.dmi.smartux.common.dao.CommonDao;
import com.dmi.smartux.admin.terms.vo.TermsAccessDetailSearchVo;
import com.dmi.smartux.admin.terms.vo.TermsAccessDetailVo;
import com.dmi.smartux.admin.terms.vo.TermsAccessGroupSearchVo;
import com.dmi.smartux.admin.terms.vo.TermsAccessGroupVo;
import com.dmi.smartux.admin.terms.vo.TermsAccessInfoSearchVo;
import com.dmi.smartux.admin.terms.vo.TermsAccessInfoVo;
import com.dmi.smartux.admin.terms.vo.TermsAccessSearchVo;
import com.dmi.smartux.admin.terms.vo.TermsAccessVo;

@Repository
public class TermsAccessDao extends CommonDao {
	
	/**
	 * 약관 마스터 목록 조회
	 * @param vo
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<TermsAccessVo> getAccessList(TermsAccessSearchVo vo) throws DataAccessException {
		vo.setStart_rnum(vo.getPageNum() * vo.getPageSize() - vo.getPageSize() + 1);
		vo.setEnd_rnum(vo.getStart_rnum() + vo.getPageSize() - 1);
		return (List<TermsAccessVo>) getSqlMapClientTemplate().queryForList("admin_terms_access.getAccessList", vo);
	}

	/**
	 * 약관 마스터 목록 개수 조회
	 * @param vo
	 * @return
	 * @throws DataAccessException
	 */
	public int getAccessListCnt(TermsAccessSearchVo vo) throws DataAccessException {
		vo.setStart_rnum(vo.getPageNum() * vo.getPageSize() - vo.getPageSize() + 1);
		vo.setEnd_rnum(vo.getStart_rnum() + vo.getPageSize() - 1);
		return (Integer) getSqlMapClientTemplate().queryForObject("admin_terms_access.getAccessListCnt", vo);
	}
	
	/**
	 * 약관 마스터 상세조회
	 * @param vo
	 * @return
	 * @throws DataAccessException
	 */
	public TermsAccessVo getAccessMst(TermsAccessSearchVo vo) throws DataAccessException {
		return (TermsAccessVo) getSqlMapClientTemplate().queryForObject("admin_terms_access.getAccessMst", vo);
	}
	
	/**
	 * 약관 마스터에 연결되어있는 그룹 목록 조호
	 * 
	 * @param vo
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<TermsAccessGroupVo> getRegGroupList(TermsAccessGroupSearchVo vo) throws DataAccessException {
		return (List<TermsAccessGroupVo>) getSqlMapClientTemplate().queryForList("admin_terms_access.getRegGroupList", vo);
	}
	
	/**
	 * 약관 마스터 검수여부 Y 갯수 조회
	 * @param vo
	 * @return
	 * @throws DataAccessException
	 */
	public int getAdtYCnt(TermsAccessVo vo) throws DataAccessException {
		return (Integer) getSqlMapClientTemplate().queryForObject("admin_terms_access.getAdtYCnt", vo);
	}
	
	/**
	 * 약관 마스터 검수여부 N 갯수 조회
	 * @param vo
	 * @return
	 * @throws DataAccessException
	 */
	public int getAdtNCnt(TermsAccessVo vo) throws DataAccessException {
		return (Integer) getSqlMapClientTemplate().queryForObject("admin_terms_access.getAdtNCnt", vo);
	}
	
	/**
	 * 약관 마스터 등록
	 * @param vo
	 * @throws DataAccessException
	 */
	public String insertProc(TermsAccessVo vo) throws DataAccessException {
		return (String) getSqlMapClientTemplate().insert("admin_terms_access.insertProc", vo);
	}
	
	/**
	 * 약관 그룹 등록
	 * @param vo
	 * @throws DataAccessException
	 */
	public void insertProcGroup(TermsAccessGroupVo vo) throws DataAccessException {
		getSqlMapClientTemplate().insert("admin_terms_access.insertProcGroup", vo);
	}
	
	/**
	 * 약관 마스터 수정
	 * @param vo
	 * @throws DataAccessException
	 */
	public void updateProc(TermsAccessVo vo) throws DataAccessException {
		getSqlMapClientTemplate().update("admin_terms_access.updateProc", vo);
	}
	
	/**
	 * 약관 그룹 삭제
	 * @param vo
	 * @throws DataAccessException
	 */
	public void deleteProcGroup(TermsAccessGroupVo vo) throws DataAccessException {
		getSqlMapClientTemplate().delete("admin_terms_access.deleteProcGroup", vo);
	}
	
	/**
	 * 약관 마스터 삭제
	 * @param vo
	 * @throws DataAccessException
	 */
	public void deleteProc(TermsAccessVo vo) throws DataAccessException {
		getSqlMapClientTemplate().update("admin_terms_access.deleteProc", vo);
	}
	
	/**
	 * 약관 속성 목록 조회
	 * @param vo
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<TermsAccessInfoVo> getAccessInfoList(TermsAccessInfoSearchVo vo) throws DataAccessException {
		vo.setStart_rnum(vo.getPageNum() * vo.getPageSize() - vo.getPageSize() + 1);
		vo.setEnd_rnum(vo.getStart_rnum() + vo.getPageSize() - 1);
		return (List<TermsAccessInfoVo>) getSqlMapClientTemplate().queryForList("admin_terms_access.getAccessInfoList", vo);
	}	
	
	/**
	 * 약관 속성 목록 개수 조회
	 * @param vo
	 * @return
	 * @throws DataAccessException
	 */
	public int getAccessInfoListCnt(TermsAccessInfoSearchVo vo) throws DataAccessException {
		vo.setStart_rnum(vo.getPageNum() * vo.getPageSize() - vo.getPageSize() + 1);
		vo.setEnd_rnum(vo.getStart_rnum() + vo.getPageSize() - 1);
		return (Integer) getSqlMapClientTemplate().queryForObject("admin_terms_access.getAccessInfoListCnt", vo);
	}
	
	/**
	 * 약관 속성 상세조회
	 * @param vo
	 * @throws DataAccessException
	 */
	public TermsAccessInfoVo getAccessInfoDetail(TermsAccessInfoSearchVo vo) throws DataAccessException {
		return (TermsAccessInfoVo) getSqlMapClientTemplate().queryForObject("admin_terms_access.getAccessInfoDetail", vo);
	}
	
	/**
	 * 약관 속성 목록 개수 조회
	 * @param vo
	 * @return
	 * @throws DataAccessException
	 */
	public int getAccessInfoCnt(TermsAccessInfoSearchVo vo) throws DataAccessException {
		return (Integer) getSqlMapClientTemplate().queryForObject("admin_terms_access.getAccessInfoCnt", vo);
	}
	
	/**
	 * 약관 버번 채번
	 * 
	 * @param vo
	 * @return
	 * @throws DataAccessException
	 */
	public String getAccessVersion(String access_info_id) throws DataAccessException {
		return (String) getSqlMapClientTemplate().queryForObject("admin_terms_access.getAccessVersion", access_info_id);
	}
	
	/**
	 * 약관 속성 등록
	 * @param vo
	 * @throws DataAccessException
	 */
	public void insertProcInfo(TermsAccessInfoVo vo) throws DataAccessException {
		getSqlMapClientTemplate().insert("admin_terms_access.insertProcInfo", vo);
	}
	
	/**
	 * 약관 상세 등록
	 * @param vo
	 * @throws DataAccessException
	 */
	public void insertProcDetail(TermsAccessDetailVo vo) throws DataAccessException {
		getSqlMapClientTemplate().insert("admin_terms_access.insertProcDetail", vo);
	}
	
	/**
	 * 약관 속성 수정
	 * @param vo
	 * @throws DataAccessException
	 */
	public void updateProcInfo(TermsAccessInfoVo vo) throws DataAccessException {
		getSqlMapClientTemplate().update("admin_terms_access.updateProcInfo", vo);
	}
	
	/**
	 * 약관 그룹 등록 개수 조회
	 * @param vo
	 * @return
	 * @throws DataAccessException
	 */
	public int getAccessGroupCnt(TermsAccessGroupVo vo) throws DataAccessException {
		return (Integer) getSqlMapClientTemplate().queryForObject("admin_terms_access.getAccessGroupCnt", vo);
	}
	
	/**
	 * 약관 속성 삭제
	 * @param vo
	 * @throws DataAccessException
	 */
	public void deleteProcInfo(TermsAccessInfoVo vo) throws DataAccessException {
		getSqlMapClientTemplate().update("admin_terms_access.deleteProcInfo", vo);
	}
	
	/**
	 * 약관마스터 항목 목록 팝업 조회
	 * 
	 * @param vo
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<TermsAccessGroupVo> getAccessInfoPopList(TermsAccessGroupSearchVo vo) throws DataAccessException {
		return (List<TermsAccessGroupVo>) getSqlMapClientTemplate().queryForList("admin_terms_access.getAccessInfoPopList", vo);
	}
	
	/**
	 * 약관항목 상세 목록 조회
	 * 
	 * @param vo
	 * @return
	 * @throws DataAccessException
	 */	
	@SuppressWarnings("unchecked")
	public List<TermsAccessDetailVo> getAccessDetailList(TermsAccessDetailSearchVo vo) throws DataAccessException {
		vo.setStart_rnum(vo.getPageNum() * vo.getPageSize() - vo.getPageSize() + 1);
		vo.setEnd_rnum(vo.getStart_rnum() + vo.getPageSize() - 1);
		return (List<TermsAccessDetailVo>) getSqlMapClientTemplate().queryForList("admin_terms_access.getAccessDetailList", vo);
	}
	
	/**
	 * 약관 상세 목록 개수 조회
	 * 
	 * @param vo
	 * @return
	 * @throws DataAccessException
	 */
	public int getAccessDetailListCnt(TermsAccessDetailSearchVo vo) throws DataAccessException {
		vo.setStart_rnum(vo.getPageNum() * vo.getPageSize() - vo.getPageSize() + 1);
		vo.setEnd_rnum(vo.getStart_rnum() + vo.getPageSize() - 1);
		return (Integer) getSqlMapClientTemplate().queryForObject("admin_terms_access.getAccessDetailListCnt", vo);
	}
	
	/**
	 * 약관 속성 상세 조회
	 * 
	 * @param vo
	 * @return
	 * @throws DataAccessException
	 */
	public TermsAccessDetailVo getAccessDetail(TermsAccessDetailSearchVo vo) throws DataAccessException {
		return (TermsAccessDetailVo) getSqlMapClientTemplate().queryForObject("admin_terms_access.getAccessDetail", vo);
	}
	
	/**
	 * 약관 속성 상세 삭제
	 * @param vo
	 * @throws DataAccessException
	 */
	public void deleteProcDetail(TermsAccessDetailVo vo) throws DataAccessException {
		getSqlMapClientTemplate().update("admin_terms_access.deleteProcDetail", vo);
	}
	
	/**
	 * 약관 속성 상세 수정
	 * @param vo
	 * @throws DataAccessException
	 */
	public void updateProcDetail(TermsAccessDetailVo vo) throws DataAccessException {
		getSqlMapClientTemplate().update("admin_terms_access.updateProcDetail", vo);
	}

}
