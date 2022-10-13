package com.dmi.smartux.admin.quality.service;

import java.util.List;

import com.dmi.smartux.admin.quality.vo.AddQualityMemberVo;
import com.dmi.smartux.admin.quality.vo.QualityListVo;
import com.dmi.smartux.admin.quality.vo.ViewQualityMemberVo;

public interface QualityAdminService {
	
	/**
	 * 관리자 쪽 품질 수집 단말 캐싱
	 * @throws Exception
	 */
	public void refreshAdminCacheOfQuality() throws Exception;
	
	/**
	 * 리스트를 보여주기 위해 품질 수집 단말 리스트를 가져온다.
	 * @param qualitylistVo
	 * @param cacheYn
	 * @return
	 * @throws Exception
	 */
	public QualityListVo getCacheQualityMemberList(QualityListVo qualitylistVo, String cacheYn) throws Exception;
	
	/**
	 * 품질 대상 단말 등록
	 * @param addqualitymemberVo
	 * @param userIp
	 * @param userId
	 * @param serviceType (2019.01.17추가 포터블TV(P))
	 * @throws Exception
	 */
	public void addQualityMember(AddQualityMemberVo addqualitymemberVo, String userIp, String userId, String serviceType) throws Exception;
	
	/**
	 * 품질 대상 단말을 액셀로 한번에 등록
	 * @param addqualitymemberVo
	 * @param userId
	 * @param userIp
	 * @param serviceType (2019.01.17추가 포터블TV(P))
	 * @return 이 안에서 실패한 횟수
	 * @throws Exception
	 */
	public int addQualityMemberList(List<AddQualityMemberVo> addqualitymemberVo, String userId, String userIp, String serviceType) throws Exception;
	
	/**
	 * 품질 대상 단말 상세페이지
	 * @param file_id
	 * @param serviceType (2019.01.17추가 포터블TV(P))
	 * @return
	 * @throws Exception
	 */
	public ViewQualityMemberVo viewQualityMember(String file_id, String serviceType);
	
	/**
	 * 품질 대상 단말 수정
	 * @param addqualitymemberVo
	 * @param userIp
	 * @param userId
	 * @param serviceType (2019.01.17추가 포터블TV(P))
	 * @throws Exception
	 */
	public void modifyQualityMember(AddQualityMemberVo addqualitymemberVo, String userIp, String userId, String serviceType) throws Exception;
	
	/**
	 * 품질 대상 단말 삭제
	 * @param file_id
	 * @param serviceType (2019.01.17추가 포터블TV(P))
	 * @throws Exception
	 */
	public void deleteQualityMember(String file_id, String serviceType) throws Exception;

}