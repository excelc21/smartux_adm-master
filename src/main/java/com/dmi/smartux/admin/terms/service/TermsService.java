package com.dmi.smartux.admin.terms.service;

import java.util.List;

import com.dmi.smartux.admin.terms.vo.TermsListVo;
import com.dmi.smartux.admin.terms.vo.TermsVo;

public interface TermsService {
	
	/**
	 * 약관 내용 저장
	 * @param termsprocVo
	 * @throws Exception
	 */
	public void insertTermsProc(TermsVo termsVo) throws Exception;
	
	/**
	 * 약관 전체 갯수
	 * @param termslistVo
	 * @return
	 * @throws Exception
	 */
	public int getTermsListCnt(TermsListVo termslistVo) throws Exception;
	
	/**
	 * 약관 목록 조회
	 * @param termslistVo
	 * @return
	 * @throws Exception
	 */
	public List<TermsVo> getTermsList(TermsListVo termslistVo) throws Exception;
	
	/**
	 * 약관 삭제
	 * @param terms_id
	 * @param id
	 * @param ip
	 * @throws Exception
	 */
	public void deleteTermsProc(String terms_id, String id, String ip) throws Exception;
	
	/**
	 * 약관 상세
	 * @param terms_id
	 * @return
	 * @throws Exception
	 */
	public TermsVo getTermsView(TermsVo termsVo) throws Exception;
	
	/**
	 * 약관정보 삭제
	 * @param termsprocVo
	 * @throws Exception
	 */
	public void updateTermsProc(TermsVo termsVo) throws Exception;

}
