package com.dmi.smartux.admin.paynow.service;

import java.util.List;

import com.dmi.smartux.admin.paynow.vo.PaynowBannerVO;
import com.dmi.smartux.admin.paynow.vo.PaynowReqVO;
import com.dmi.smartux.admin.paynow.vo.PaynowSearchVO;

/**
 * Paynow Info Admin Service
 *
 * @author 
 */
public interface PaynowAdminService {

    /**
     * Paynow 결제 정보 리스트 조회
     *
     * @param PaynowSearchVO paynowSearchVO
     * @return Paynow 결제 정보 리스트
     * @throws Exception
     */
	public List<PaynowReqVO> getPaynowReqList(PaynowSearchVO paynowSearchVO) throws Exception;
	
	/**
     * Paynow 결제 정보 조회
     *
     * @param PaynowSearchVO paynowSearchVO
     * @return Paynow 결제 정보
     * @throws Exception
     */
	public PaynowReqVO getPaynowReqInfo(PaynowSearchVO paynowSearchVO) throws Exception;

    /**
     * Paynow 결제 정보 개수 조회
     *
     * @param PaynowSearchVO paynowSearchVO
     * @return Paynow 결제 정보 개수
     * @throws Exception
     */
    public int getPaynowReqListCount(PaynowSearchVO paynowSearchVO) throws Exception;
    
    /**
     * Paynow 안내 배너 리스트 조회
     *
     * @param String codeID
     * @return Paynow 안내 배너 리스트
     * @throws Exception
     */
    public List<PaynowBannerVO> getBannerList(String code) throws Exception;
    
    /**
     * Paynow 안내 배너 조회
     *
     * @param String code, String banner_id
     * @return Paynow 안내 배너
     * @throws Exception
     */
    public PaynowBannerVO getBanner(String code, String banner_id) throws Exception;
    
    /**
     * Paynow 안내 배너 등록
     *
     * @throws Exception
     */
    public void addBanner(String code, String banner_id, String banner_text, String use_yn, String create_id) throws Exception;
    
    /**
     * Paynow 안내 배너 수정
     *
     * @throws Exception
     */
    public void updateBanner(String code, String banner_id, String banner_text, String use_yn, String update_id) throws Exception;
    
    /**
     * Paynow 안내 배너 삭제
     *
     * @throws Exception
     */
    public void deleteBanner(String code, String banner_id) throws Exception;
    
    /**
     * Paynow 배너 이미지 파일 삭제
     *
     * @throws Exception
     */
    public boolean deleteImageFile(String fname) throws Exception;
    
}
