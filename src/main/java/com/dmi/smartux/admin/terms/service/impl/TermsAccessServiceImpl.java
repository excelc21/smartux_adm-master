package com.dmi.smartux.admin.terms.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.admin.terms.dao.TermsAccessDao;
import com.dmi.smartux.admin.terms.vo.TermsAccessDetailSearchVo;
import com.dmi.smartux.admin.terms.vo.TermsAccessDetailVo;
import com.dmi.smartux.admin.terms.vo.TermsAccessGroupSearchVo;
import com.dmi.smartux.admin.terms.vo.TermsAccessGroupVo;
import com.dmi.smartux.admin.terms.vo.TermsAccessInfoSearchVo;
import com.dmi.smartux.admin.terms.vo.TermsAccessInfoVo;
import com.dmi.smartux.admin.terms.vo.TermsAccessSearchVo;
import com.dmi.smartux.admin.terms.vo.TermsAccessVo;
import com.dmi.smartux.admin.terms.vo.TermsAccessTypeVo;
import com.dmi.smartux.admin.terms.service.TermsAccessService;

@Service
public class TermsAccessServiceImpl implements TermsAccessService {
	
	@Autowired
	TermsAccessDao dao;

	/**
	 * 약관 마스터 목록조회
	 */
	@Override
	public List<TermsAccessVo> getAccessList(TermsAccessSearchVo vo) throws Exception {
		return dao.getAccessList(vo);
	}

	/**
	 * 약관 마스터 목록 개수 조회
	 */
	@Override
	public int getAccessListCnt(TermsAccessSearchVo vo) throws Exception {
		return dao.getAccessListCnt(vo);
	}
	
	/**
	 * 약관 마스터 조회
	 */
	@Override
	public TermsAccessVo getAccessMst(TermsAccessSearchVo vo) throws Exception {
		return dao.getAccessMst(vo);
	}
	
	/**
	 * 약관 마스터에 연결되어있는 그룹 목록 조회
	 */
	@Override
	public List<TermsAccessGroupVo> getRegGroupList(TermsAccessGroupSearchVo vo) throws Exception {
		return dao.getRegGroupList(vo);
	}
	
	@Override
	public List<TermsAccessTypeVo> getAccessType(String type) throws Exception {
		
		ArrayList<TermsAccessTypeVo> accessTypeList = new ArrayList<TermsAccessTypeVo>();
		
		String accessType = "";
		
		if("S".equals(type)) {
			 accessType = SmartUXProperties.getProperty("access.service.type");
		} else {
			 accessType = SmartUXProperties.getProperty("access.display.type");
		}
		
		if (StringUtils.isNotEmpty(accessType)) {
			String[] dataType01 = accessType.split("\\|");
			for (int i = 0; i < dataType01.length; i++) {
				String[] dataType02 = dataType01[i].split("\\^");
				if (dataType02.length == 2) {
					TermsAccessTypeVo accessDataType = new TermsAccessTypeVo();
					accessDataType.setCode(StringUtils.defaultString(dataType02[0]));
					accessDataType.setName(StringUtils.defaultString(dataType02[1]));
					accessTypeList.add(accessDataType);
				}
			}
		} else {
			return null;
		}
		
		return accessTypeList;
	}
	
	/**
	 * 약관 마스터 검수여부 Y 갯수 조회
	 */
	@Override
	public int getAdtYCnt(TermsAccessVo vo) throws Exception {
		return dao.getAdtYCnt(vo);
	}
	
	/**
	 * 약관 마스터 검수여부 N 갯수 조회
	 */
	@Override
	public int getAdtNCnt(TermsAccessVo vo) throws Exception {
		return dao.getAdtNCnt(vo);
	}
	
	/**
	 * 약관 마스터 등록
	 */
	@Override
	@Transactional
	public void insertProc(TermsAccessVo vo, String accessInfoIds) throws Exception {
		
		String access_mst_id = dao.insertProc(vo);
		
		TermsAccessGroupVo agVO = new TermsAccessGroupVo();
		agVO.setAccess_mst_id(access_mst_id);
		
		if(StringUtils.isNotEmpty(accessInfoIds)){
			String[] access_info_ids = accessInfoIds.split(",");
			
			for(int i=0; i<access_info_ids.length; i++) {
				agVO.setAccess_info_id(access_info_ids[i]);
				agVO.setCol((i+1)+"");
				dao.insertProcGroup(agVO);
			}
		}
	}
	
	/**
	 * 약관 마스터 수정
	 */
	@Override
	@Transactional
	public void updateProc(TermsAccessVo vo, String accessInfoIds) throws Exception {
		
		dao.updateProc(vo);
		TermsAccessGroupVo agVO = new TermsAccessGroupVo();
		agVO.setAccess_mst_id(vo.getAccess_mst_id());
		dao.deleteProcGroup(agVO);
		
		if(StringUtils.isNotEmpty(accessInfoIds)){
			String[] access_info_ids = accessInfoIds.split(",");
			for(int i=0; i<access_info_ids.length; i++) {
				agVO.setAccess_info_id(access_info_ids[i]);
				agVO.setCol((i+1)+"");
				dao.insertProcGroup(agVO);
			}
		}
	}
	
	/**
	 * 약관 마스터/약관 그룹 삭제
	 */
	@Override
	@Transactional
	public void deleteProc(TermsAccessVo vo) throws Exception {
		dao.deleteProc(vo);
		
		TermsAccessGroupVo grVO = new TermsAccessGroupVo();
		grVO.setAccess_mst_id(vo.getAccess_mst_id());
		dao.deleteProcGroup(grVO);
	}
	
	/**
	 * 약관 속성 목록조회
	 */
	@Override
	public List<TermsAccessInfoVo> getAccessInfoList(TermsAccessInfoSearchVo vo) throws Exception {
		return dao.getAccessInfoList(vo);
	}

	/**
	 * 약관 속성 목록개수 조회
	 */
	@Override
	public int getAccessInfoListCnt(TermsAccessInfoSearchVo vo) throws Exception {
		return dao.getAccessInfoListCnt(vo);
	}
	
	/**
	 * 약관 속성 상세조회
	 */
	@Override
	public TermsAccessInfoVo getAccessInfoDetail(TermsAccessInfoSearchVo vo) throws Exception {
		return dao.getAccessInfoDetail(vo);
	}

	/**
	 * 약관 속성 존재 개수 확인
	 */
	@Override
	public int getAccessInfoCnt(TermsAccessInfoSearchVo vo) throws Exception {
		return dao.getAccessInfoCnt(vo);
	}
	
	/**
	 * 약관 속성 등록
	 */
	@Override
	@Transactional
	public void insertProcInfo(TermsAccessInfoVo vo) throws Exception {
		
		String access_version = dao.getAccessVersion(vo.getAccess_info_id());

		vo.setAccess_version(access_version);
		dao.insertProcInfo(vo);
		
		TermsAccessDetailVo adVO = new TermsAccessDetailVo();
		adVO.setAccess_info_id(vo.getAccess_info_id());
		adVO.setAccess_version(access_version);
		adVO.setDisplay_yn("Y");
		adVO.setDelete_yn("N");
		adVO.setMod_id(vo.getMod_id());
		adVO.setReg_id(vo.getReg_id());
		
		dao.insertProcDetail(adVO);
	}

	/**
	 * 약관 속성 수정
	 */
	@Override
	@Transactional
	public void updateProcInfo(TermsAccessInfoVo vo, String version_update_yn) throws Exception {
		
		String access_version = "";
		
		if("Y".equals(version_update_yn)){
			access_version = dao.getAccessVersion(vo.getAccess_info_id());
			vo.setAccess_version(access_version);
			
			TermsAccessDetailVo adVO = new TermsAccessDetailVo();
			adVO.setAccess_info_id(vo.getAccess_info_id());
			adVO.setAccess_version(access_version);
			adVO.setDisplay_yn("Y");
			adVO.setDelete_yn("N");
			adVO.setMod_id(vo.getMod_id());
			adVO.setReg_id(vo.getReg_id());
			
			dao.insertProcDetail(adVO);
		} else {
			vo.setAccess_version(null);
		}
		dao.updateProcInfo(vo);
		
	}
	
	/**
	 * 약관 속성/약관 그룹 삭제
	 */
	@Override
	@Transactional
	public boolean deleteProcInfo(TermsAccessInfoVo vo) throws Exception {
		
		boolean resultFlag = true;
		
		TermsAccessGroupVo grVO = new TermsAccessGroupVo();
		grVO.setAccess_info_id(vo.getAccess_info_id());
		
		if(dao.getAccessGroupCnt(grVO) > 0) {
			resultFlag = false;
		} else {
			dao.deleteProcInfo(vo);
			dao.deleteProcGroup(grVO);
		}
		
		return resultFlag;
	}
	
	/**
	 * 약관마스터 항목 목록 팝업 조회
	 */
	@Override
	public List<TermsAccessGroupVo> getAccessInfoPopList(TermsAccessGroupSearchVo vo) throws Exception {
		return dao.getAccessInfoPopList(vo);
	}
	
	/**
	 * 약관항목 상세 목록 조회
	 */
	@Override
	public List<TermsAccessDetailVo> getAccessDetailList(TermsAccessDetailSearchVo vo) throws Exception {
		return dao.getAccessDetailList(vo);
	}

	/**
	 * 약관 상세 목록 개수 조회
	 */
	@Override
	public int getAccessDetailListCnt(TermsAccessDetailSearchVo vo) throws Exception {
		return dao.getAccessDetailListCnt(vo);
	}
	
	/**
	 * 약관 속성 상세 삭제
	 */
	@Override
	public boolean deleteProcDetail(TermsAccessDetailVo vo) throws Exception {
		
		boolean isDelete = true;
		
		TermsAccessDetailSearchVo dsVO = new TermsAccessDetailSearchVo();
		TermsAccessInfoSearchVo isVO = new TermsAccessInfoSearchVo();
		
		dsVO.setAccess_detail_id(vo.getAccess_detail_id());
		TermsAccessDetailVo accessDetailVO = dao.getAccessDetail(dsVO);
		
		if(accessDetailVO != null){
			isVO.setAccess_info_id(accessDetailVO.getAccess_info_id());
			TermsAccessInfoVo accessInfoVO = dao.getAccessInfoDetail(isVO);
			
			if(accessInfoVO != null) {
				if(!accessInfoVO.getAccess_version().equals(accessDetailVO.getAccess_version())){
					dao.deleteProcDetail(vo);
				} else {
					//속성 테이블의 버전과 동일 한 경우는 삭제 하면 안됨.
					isDelete = false;
				}
			} else {
				dao.deleteProcDetail(vo);
			}
		} else {
			dao.deleteProcDetail(vo);
		}
		
		return isDelete;
	}
	
	/**
	 * 약관 속성 상세 조회
	 */
	@Override
	public TermsAccessDetailVo getAccessDetail(TermsAccessDetailSearchVo vo) throws Exception {
		return dao.getAccessDetail(vo);
	}
	
	/**
	 * 약관 속성 상세 수정
	 */
	@Override
	public void updateProcDetail(TermsAccessDetailVo vo) throws Exception {
		dao.updateProcDetail(vo);
	}
	
	/**
	 * 약관 속성 상세 등록
	 */
	@Override
	@Transactional
	public void insertProcDetail(TermsAccessDetailVo vo) throws Exception {
		
		String access_version = "";
		TermsAccessInfoSearchVo infoSerachVO = new TermsAccessInfoSearchVo();
		infoSerachVO.setAccess_info_id(vo.getAccess_info_id());
		TermsAccessInfoVo infoVO = dao.getAccessInfoDetail(infoSerachVO);
		
		access_version = dao.getAccessVersion(infoVO.getAccess_info_id());
		infoVO.setAccess_version(access_version);
		infoVO.setMod_id(vo.getMod_id());
		
		vo.setAccess_version(access_version);
		vo.setDisplay_yn("Y");
		vo.setDelete_yn("N");
		
		dao.updateProcInfo(infoVO);
		dao.insertProcDetail(vo);
	}
	
}
