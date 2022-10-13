/**
 * Class Name : GpackPackViewDao.java
 * Description : 
 *  팩(템플릿) 관리 구현을 위한 dao class
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
package com.dmi.smartux.admin.gpack.pack.dao;


import org.springframework.stereotype.Repository;

import com.dmi.smartux.admin.gpack.event.vo.GpackPromotionVO;
import com.dmi.smartux.admin.gpack.pack.vo.GPackPackVO;
import com.dmi.smartux.common.dao.CommonMimsDao;

@Repository
public class GPackPackViewDao extends CommonMimsDao {

	/**
	 * 팩(템플릿) 상세 정보 조회
	 * @param packVO		조회 조건 정보
	 * @return 조회된 팩(템플릿) 상세 정보
	 * @throws Exception
	 */
	public GPackPackVO getGpackPackDetail(GPackPackVO packVO) throws Exception {
		return (GPackPackVO)(getSqlMapClientTemplate().queryForObject("gpack_pack.getGpackPackDetail", packVO));
	}
	
	/**
	 * 팩(템플릿) 상세 정보 조회(상용)
	 * @param packVO		조회 조건 정보
	 * @return 조회된 팩(템플릿) 상세 정보
	 * @throws Exception
	 */
	public GPackPackVO getGpackPackDetailBiz(GPackPackVO packVO) throws Exception {
		return (GPackPackVO)(getSqlMapClientTemplate().queryForObject("gpack_pack.getGpackPackDetailBiz", packVO));
	}
	
	/**
	 * 팩(템플릿) 수정
	 * @param packVO		수정하고자 하는 팩(템플릿) 정보
	 * @return 결과
	 * @throws Exception
	 */
	public int updateGpackPack(GPackPackVO packVO) throws Exception {
		return getSqlMapClientTemplate().update("gpack_pack.updateGpackPack", packVO);
	}
	
	/**
	 * 카테고리 유효 여부 판단하기 (콘텐츠)
	 * @param packVO
	 * @return
	 * @throws Exception
	 */
	public int chkAvailableCategory(GPackPackVO packVO) throws Exception {
		return (Integer) getSqlMapClientTemplate().queryForObject("gpack_pack.chkAvailableCategory", packVO);
	}
	
	/**
	 * 프로모션 유효 여부 판단하기 
	 * 템플릿타입이 템플릿2일 경우 - 영상정보가 있는 프로모션이 있는지 확인 (플레이리스트 타입일 경우 플레이리스트 정보 포함) 
     * 콘텐츠 정보 확인 
	 * @param packVO
	 * @return
	 * @throws Exception
	 */
	public int chkAvailablePromotion(GPackPackVO packVO) throws Exception {
		return (Integer) getSqlMapClientTemplate().queryForObject("gpack_pack.chkAvailablePromotion", packVO);
	}
	
	/**
	 * 팩(템플릿) 버전 구하기 
	 * @param packVO
	 * @return
	 * @throws Exception
	 */
	public String getGpackPackVersion(GPackPackVO packVO) throws Exception {
		return (String) getSqlMapClientTemplate().queryForObject("gpack_pack.getGpackPackVersion", packVO);
	}
	
	/**
	 * 상용반영 
	 * @param packVO
	 * @return
	 * @throws Exception
	 */
	public void insertGpackBiz(GPackPackVO packVO) throws Exception{
		getSqlMapClientTemplate().insert("gpack_pack.insertGpackBizTable", packVO);
	}
	
	/**
	 * 팩(템플릿) 버전 업(상용)
	 * @param packVO
	 * @return
	 * @throws Exception
	 */
	public int updateGpackPackVersion_Biz(GPackPackVO packVO) throws Exception {
		return getSqlMapClientTemplate().update("gpack_pack.updateGpackPackVersion_Biz", packVO);
	}

	/**
	 * 템플릿2(영상포함) 이지만 채널/플레이리스트 선택되어 있는지 여부 Dao
	 * @param packVO
	 * @return
	 */
	public int isPromotionByTemplate2 ( GPackPackVO packVO ) throws Exception {
		return (Integer) getSqlMapClientTemplate().queryForObject("gpack_pack.isPromotionByTemplate2", packVO);
	}

	/**
	 * 프로모션 카테고리/프로모션 비디오구분 아이디 가져오기
	 * @param pack_id
	 * @return
	 */
	public GpackPromotionVO getPromotionInfoBy ( GPackPackVO packVO ) throws Exception {
		return (GpackPromotionVO) getSqlMapClientTemplate().queryForObject("gpack_pack.getPromotionInfoBy", packVO);
	}
}
