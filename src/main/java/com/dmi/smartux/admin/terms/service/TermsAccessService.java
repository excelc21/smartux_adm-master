package com.dmi.smartux.admin.terms.service;

import java.util.List;

import com.dmi.smartux.admin.terms.vo.TermsAccessDetailSearchVo;
import com.dmi.smartux.admin.terms.vo.TermsAccessDetailVo;
import com.dmi.smartux.admin.terms.vo.TermsAccessGroupSearchVo;
import com.dmi.smartux.admin.terms.vo.TermsAccessGroupVo;
import com.dmi.smartux.admin.terms.vo.TermsAccessInfoSearchVo;
import com.dmi.smartux.admin.terms.vo.TermsAccessInfoVo;
import com.dmi.smartux.admin.terms.vo.TermsAccessSearchVo;
import com.dmi.smartux.admin.terms.vo.TermsAccessVo;
import com.dmi.smartux.admin.terms.vo.TermsAccessTypeVo;

public interface TermsAccessService {
	
	/**
	 * 약관 마스터 목록 조회
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public List<TermsAccessVo> getAccessList(TermsAccessSearchVo vo) throws Exception;

	/**
	 * 약관 마스터 목록 개수 조회
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public int getAccessListCnt(TermsAccessSearchVo vo) throws Exception;
	
	/**
	 * 약관 타입 조회
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public List<TermsAccessTypeVo> getAccessType(String type) throws Exception;
	
	/**
	 * 약관 마스터 상세조회
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public TermsAccessVo getAccessMst(TermsAccessSearchVo vo) throws Exception;
	
	/**
	 * 약관 마스터에 연결되어있는 그룹 목록 조회
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public List<TermsAccessGroupVo> getRegGroupList(TermsAccessGroupSearchVo vo) throws Exception;
	
	/**
	 * 약관 마스터 검수여부 Y 갯수 조회
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public int getAdtYCnt(TermsAccessVo vo) throws Exception;
	
	/**
	 * 약관 마스터 검수여부 N 갯수 조회
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public int getAdtNCnt(TermsAccessVo vo) throws Exception;
	
	/**
	 * 약관 마스터 등록
	 * @param vo
	 * @throws Exception
	 */
	public void insertProc(TermsAccessVo vo, String accessInfoIds) throws Exception;
	
	/**
	 * 약관 마스터 수정
	 * @param vo
	 * @throws Exception
	 */
	public void updateProc(TermsAccessVo vo, String accessInfoIds) throws Exception;
	
	/**
	 * 약관 마스터 삭제
	 * @param vo
	 * @throws Exception
	 */
	public void deleteProc(TermsAccessVo vo) throws Exception;
	
	/**
	 * 약관 속성 목록 조회
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public List<TermsAccessInfoVo> getAccessInfoList(TermsAccessInfoSearchVo vo) throws Exception;
	
	/**
	 * 약관 속성 목록 개수 조회
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public int getAccessInfoListCnt(TermsAccessInfoSearchVo vo) throws Exception;
	
	/**
	 * 약관 속성 상세조회
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public TermsAccessInfoVo getAccessInfoDetail(TermsAccessInfoSearchVo vo) throws Exception;
	
	/**
	 * 약관 속성 존재 개수 조회
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public int getAccessInfoCnt(TermsAccessInfoSearchVo vo) throws Exception;
	
	/**
	 * 약관 속성 등록
	 * @param vo
	 * @throws Exception
	 */
	public void insertProcInfo(TermsAccessInfoVo vo) throws Exception;
	
	/**
	 * 약관 속성 수정
	 * @param vo
	 * @throws Exception
	 */
	public void updateProcInfo(TermsAccessInfoVo vo, String version_update_yn) throws Exception;
	
	/**
	 * 약관 속성 삭제
	 * @param vo
	 * @throws Exception
	 */
	public boolean deleteProcInfo(TermsAccessInfoVo vo) throws Exception;
	
	/**
	 * 약관마스터 항목 목록 팝업 조회
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public List<TermsAccessGroupVo> getAccessInfoPopList(TermsAccessGroupSearchVo vo) throws Exception;
	
	/**
	 * 약관항목 상세 팝업 목록 조회
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public List<TermsAccessDetailVo> getAccessDetailList(TermsAccessDetailSearchVo vo) throws Exception;
	
	/**
	 * 약관 상세 목록 개수 조회
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public int getAccessDetailListCnt(TermsAccessDetailSearchVo vo) throws Exception;
	
	/**
	 * 약관 속성 상세 삭제
	 * @param vo
	 * @throws Exception
	 */
	public boolean deleteProcDetail(TermsAccessDetailVo vo) throws Exception;	
	
	/**
	 * 약관 속성 상세 조회
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public TermsAccessDetailVo getAccessDetail(TermsAccessDetailSearchVo vo) throws Exception;
	
	/**
	 * 약관 속성 상세 수정
	 * @param vo
	 * @throws Exception
	 */
	public void updateProcDetail(TermsAccessDetailVo vo) throws Exception;
	
	/**
	 * 약관 속성 상세 등록
	 * @param vo
	 * @throws Exception
	 */
	public void insertProcDetail(TermsAccessDetailVo vo) throws Exception;

}
