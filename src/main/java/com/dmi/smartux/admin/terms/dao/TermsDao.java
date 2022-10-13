package com.dmi.smartux.admin.terms.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.dmi.smartux.admin.terms.vo.TermsListVo;
import com.dmi.smartux.admin.terms.vo.TermsVo;
import com.dmi.smartux.common.dao.CommonDao;

@Repository
public class TermsDao extends CommonDao {
	
	/**
	 * 약관내용 저장
	 * @param termsprocVo
	 * @return
	 * @throws DataAccessException
	 * @throws Exception
	 */
	public void insertTermsProc(TermsVo termsVo) throws DataAccessException, Exception{	
		getSqlMapClientTemplate().insert("admin_terms.insertTermsProc", termsVo);
	}
	
	/**
	 * 약관내용 저장 로그
	 * @param termsprocVo
	 * @throws DataAccessException
	 */
	public void insertTermsProcLog(TermsVo termsVo) throws DataAccessException{	
		getSqlMapClientTemplate().insert("admin_terms.insertTermsProcLog", termsVo);		
	}
	
	/**
	 * 약관 전체 갯수
	 * @param termslistVo
	 * @return
	 * @throws DataAccessException
	 */
	public int getTermsListCnt(TermsListVo termslistVo) throws DataAccessException{
		
		int result=0;
		result=(Integer)getSqlMapClientTemplate().queryForObject("admin_terms.getTermsListCnt",termslistVo);
		
		return result;
	}
	
	/**
	 * 약관 목록 조회
	 * @param termslistVo
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<TermsVo> getTermsList(TermsListVo termslistVo) throws DataAccessException{
		
		termslistVo.setStart_rnum(termslistVo.getPageNum()*termslistVo.getPageSize()-termslistVo.getPageSize()+1);
		termslistVo.setEnd_rnum(termslistVo.getStart_rnum()+termslistVo.getPageSize()-1);
		
		List<TermsVo> result= null;
		
		result=getSqlMapClientTemplate().queryForList("admin_terms.getTermsList",termslistVo);
		
		return result;
	}
	
	/**
	 * 약관 수정
	 * @param termsprocVo
	 * @throws DataAccessException
	 * @throws Exception
	 */
	public void updateTermsProc(TermsVo termsVo) throws DataAccessException, Exception{	
		  
		getSqlMapClientTemplate().update("admin_terms.updateTermsProc", termsVo);
	}
	
	/**
	 * 약관 수정 로그
	 * @param termsprocVo
	 * @throws DataAccessException
	 */
	public void termsUpdateLog(TermsVo termsVo) throws DataAccessException{	
		getSqlMapClientTemplate().insert("admin_terms.termsUpdateLog", termsVo);		
	}
	
	/**
	 * 약관 삭제
	 * @param terms_id
	 * @throws DataAccessException
	 */
	public void deleteTermsProc(TermsVo termsVo) throws DataAccessException{
		getSqlMapClientTemplate().delete("admin_terms.deleteTermsProc", termsVo); 
	}
	
	/**
	 * 약관 상세 조회
	 * @param terms_id
	 * @return
	 * @throws DataAccessException
	 */
	public TermsVo getTermsView(TermsVo termsVo) throws DataAccessException{		
		
		TermsVo result=null;			
		result=(TermsVo)getSqlMapClientTemplate().queryForObject("admin_terms.getTermsView",termsVo);
		
		return result;		
	}
	


}
