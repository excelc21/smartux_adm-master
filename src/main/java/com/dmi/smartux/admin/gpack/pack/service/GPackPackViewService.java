/**
 * Class Name : GpackPackViewService.java
 * Description : 
 *  팩(템플릿) 관리 구현을 위한 서비스 class
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
package com.dmi.smartux.admin.gpack.pack.service;

import com.dmi.smartux.admin.gpack.event.vo.GpackPromotionVO;
import com.dmi.smartux.admin.gpack.pack.vo.GPackPackVO;

public interface GPackPackViewService {

	/**
	 * 팩(템플릿) 상세 정보 조회
	 * @param packVO		조회 조건 정보
	 * @return 조회된 팩(템플릿) 상세 정보
	 * @throws Exception
	 */
	public GPackPackVO getGpackPackDetail(GPackPackVO packVO) throws Exception;
	
	/**
	 * 팩(템플릿) 상세 정보 조회(상용)
	 * @param packVO		조회 조건 정보
	 * @return 조회된 팩(템플릿) 상세 정보
	 * @throws Exception
	 */
	public GPackPackVO getGpackPackDetailBiz(GPackPackVO packVO) throws Exception;
	
	/**
	 * 팩(템플릿) 수정
	 * @param packVO		수정하고자 하는 팩(템플릿) 정보
	 * @throws Exception
	 */
	public void updateGpackPack(GPackPackVO packVO) throws Exception;
	
	/**
	 * 카테고리 유효여부 확인 (콘텐츠)
	 * @param packVO
	 * @return
	 * @throws Exception
	 */
	public String chkAvailableCategory(String pack_id) throws Exception;
	
	/**
	 * 카테고리 유효여부 확인
	 * 템플릿타입이 템플릿2일 경우 - 영상정보가 있는 프로모션이 있는지 확인 (플레이리스트 타입일 경우 플레이리스트 정보 포함) 
     * 콘텐츠 정보 확인 
	 * @param packVO
	 * @return
	 * @throws Exception
	 */
	public String chkAvailablePromotion(String pack_id) throws Exception;
	
	/**
	 * 상용 반영 
	 * @param pack_id
	 * @param login_id
	 * @throws Exception
	 */
	public void insertGpackBiz(String pack_id, String login_id) throws Exception;

	/**
	 * 템플릿2(영상포함) 이지만 채널/플레이리스트 선택되어 있는지 여부
	 * @param pack_id
	 * @param template_type
	 * @return
	 * @throws Exception
	 */
	public String isPromotionByTemplate2 ( String pack_id, String template_type ) throws Exception;

	/**
	 * 프로모션 카테고리/프로모션 비디오구분 아이디 가져오기
	 * @param pack_id
	 * @return
	 */
	public GpackPromotionVO getPromotionInfoBy ( String pack_id ) throws Exception;
}
