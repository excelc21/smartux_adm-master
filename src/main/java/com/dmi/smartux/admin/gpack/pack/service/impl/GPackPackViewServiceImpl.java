/**
 * Class Name : GpackPackViewServiceImpl.java
 * Description : 
 *  팩(템플릿) 관리 구현을 위한 서비스 구현 class
 *
 * Modification Information
 *  
 * 수정일         수정자         수정내용
 * ----------     --------       ---------------------------
 * 2013.03.14     kimhahn		신규
 *    
 * @author kimhahn
 * @since 2014.03.14
 * @version 1.0
 */
package com.dmi.smartux.admin.gpack.pack.service.impl;


import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmi.smartux.admin.gpack.event.vo.GpackPromotionVO;
import com.dmi.smartux.admin.gpack.pack.dao.GPackPackViewDao;
import com.dmi.smartux.admin.gpack.pack.service.GPackPackViewService;
import com.dmi.smartux.admin.gpack.pack.vo.GPackPackVO;

@Service
public class GPackPackViewServiceImpl implements GPackPackViewService {

	/** 팩DAO */ 
	@Autowired
	GPackPackViewDao packViewDao;
	
	/**
	 * 팩(템플릿) 상세 정보 조회
	 * @param packVO		조회 조건 정보
	 * @return 조회된 팩(템플릿) 상세 정보
	 * @throws Exception
	 */
	@Override
	@Transactional(readOnly=true)
	public GPackPackVO getGpackPackDetail(GPackPackVO packVO) throws Exception {
		return packViewDao.getGpackPackDetail(packVO);
	}
	
	/**
	 * 팩(템플릿) 상세 정보 조회(상용)
	 * @param packVO		조회 조건 정보
	 * @return 조회된 팩(템플릿) 상세 정보
	 * @throws Exception
	 */
	@Override
	@Transactional(readOnly=true)
	public GPackPackVO getGpackPackDetailBiz(GPackPackVO packVO) throws Exception {
		return packViewDao.getGpackPackDetailBiz(packVO);
	}
	
	/**
	 * 팩(템플릿) 수정
	 * @param packVO		수정하고자 하는 팩(템플릿) 정보
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void updateGpackPack(GPackPackVO packVO) throws Exception {
		packViewDao.updateGpackPack(packVO);
	}

	
	/**
	 * 즉시반영시 유효한 카테고리 여부 확인 (콘텐츠 정보가 있는지, 유요하면 1이상 값)
	 * @param packVO
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional
	public String chkAvailableCategory(String pack_id) throws Exception{
		String result = "N";
		GPackPackVO packVO = new GPackPackVO();
		packVO.setPack_id(pack_id);
		
		int chk = packViewDao.chkAvailableCategory(packVO);
		if(chk > 0)	result = "Y";
		else 		result = "N";

		return result;
	}
	
	/**
    	즉시반영시 유효한 프로모션 여부 확인 (유효하지 않은 프로모션이 있을 경우 0이상 값)
    	VAL_CHK - 하위 콘텐츠가 있는지 확인 (없으면 N, 체크할 필요없음)
    	템플릿타입이 템플릿2이고, 플레이리스트 타입일 경우 플레이리스트 정보가 있는지 확인
	 * @param packVO
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional
	public String chkAvailablePromotion(String pack_id) throws Exception{
		String result = "Y";
		GPackPackVO packVO = new GPackPackVO();
		packVO.setPack_id(pack_id);
		
		int chk = packViewDao.chkAvailablePromotion(packVO);
		if ( chk > 0) {
			result = "N";
		} else {
			result = "Y";
		}
		
		return result;
	}
	
	
	/**
	 * 저장 
	 */
	@Override
	@Transactional
	public void insertGpackBiz(String pack_id, String login_id) throws Exception{
		GPackPackVO packVO = new GPackPackVO();
		packVO.setPack_id(pack_id);
		packVO.setUpdate_id(login_id);
		packVO.setCreate_id(login_id);
		
		String version = packViewDao.getGpackPackVersion(packVO);
		packVO.setVersion(version);
		
		//TEMP 테이블 > 상용 테이블
		packViewDao.insertGpackBiz(packVO);
		
		//상용 테이블 버젼 업
		packViewDao.updateGpackPackVersion_Biz(packVO);
	}

	/**
	 * 템플릿2(영상포함) 이지만 채널/플레이리스트 선택되어 있는지 여부 Service
	 */
	@Override
	@Transactional
	public String isPromotionByTemplate2 ( String pack_id, String template_type ) throws Exception {
		String result = "Y";
		GPackPackVO packVO = new GPackPackVO();
		packVO.setPack_id(pack_id);
		
		if ( StringUtils.equals ( template_type, "TP002" )) { //템플릿2(영상포함) 이지만 채널/플레이리스트 선택되어 있는지 한번 더 체크  ( 0 보다 크면 Y : 둘중에 한개 등록 되어 있음 )
			int chk = packViewDao.isPromotionByTemplate2 ( packVO );
			if ( chk > 0 ) {
				result = "Y";
			} else {
				result = "N";
			}
		}

		return result;
	}

	/**
	 * 프로모션 카테고리/프로모션 비디오구분 아이디 가져오기
	 * @param pack_id
	 * @return
	 */
	@Override
	public GpackPromotionVO getPromotionInfoBy ( String pack_id ) throws Exception {
		GPackPackVO packVO = new GPackPackVO();
		packVO.setPack_id(pack_id);
		
		GpackPromotionVO promotionVo= packViewDao.getPromotionInfoBy(packVO);
		
		return promotionVo;
	}
}
