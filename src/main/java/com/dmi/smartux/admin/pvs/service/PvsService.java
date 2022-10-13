package com.dmi.smartux.admin.pvs.service;

import com.dmi.smartux.admin.news.vo.TargetVO;
import com.dmi.smartux.admin.pvs.vo.PvsProductVO;

import java.util.List;

/**
 * PVS 서비스
 *
 * @author dongho
 */
public interface PvsService {
    /**
     * PVS 상품 리스트 조회
     *
     * @return 상품 리스트
     * @throws Exception
     */
    public List<PvsProductVO> getPvsProductList() throws Exception;

    /**
     * PVS 부가 상품 리스트 조회
     *
     * @return 상품 리스트
     * @throws Exception
     */
    public List<PvsProductVO> getOptionalServiceList() throws Exception;

    /**
     * 타겟팅된 가입자의 가입번호 리스트 조회(ALL)
     *
     * @param targetVO 타겟룰 객체
     * @return 가입번호 리스트
     * @throws Exception
     */
    public List<String> getAllSaIDList(TargetVO targetVO) throws Exception;

    /**
     * 타겟팅된 가입자의 가입번호 개수 조회(ALL)
     *
     * @param targetVO 타겟룰 객체
     * @return 가입번호 개수
     * @throws Exception
     */
    public int getAllSaIDCount(TargetVO targetVO) throws Exception;

    /**
     * 타겟팅된 가입자의 가입번호 리스트 조회(부가서비스 포함)
     *
     * @param targetVO 타겟룰 객체
     * @return 가입번호 리스트
     * @throws Exception
     */
    public List<String> getIncludeSaIDList(TargetVO targetVO) throws Exception;

    /**
     * 타겟팅된 가입자의 가입번호 개수 조회(부가서비스 포함)
     *
     * @param targetVO 타겟룰 객체
     * @return 가입번호 개수
     * @throws Exception
     */
    public int getIncludeSaIDCount(TargetVO targetVO) throws Exception;

    /**
     * 타겟팅된 가입자의 가입번호 리스트 조회(부가서비스 미포함)
     *
     * @param targetVO 타겟룰 객체
     * @return 가입번호 리스트
     * @throws Exception
     */
    public List<String> getExcludeSaIDList(TargetVO targetVO) throws Exception;

    /**
     * 타겟팅된 가입자의 가입번호 개수 조회(부가서비스 미포함)
     *
     * @param targetVO 타겟룰 객체
     * @return 가입번호 개수
     * @throws Exception
     */
    public int getExcludeSaIDCount(TargetVO targetVO) throws Exception;
}
