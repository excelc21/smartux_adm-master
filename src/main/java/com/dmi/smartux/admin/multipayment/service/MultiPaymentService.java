package com.dmi.smartux.admin.multipayment.service;

import java.util.List;

import com.dmi.smartux.admin.multipayment.vo.MultiPaymentSearchVo;
import com.dmi.smartux.admin.multipayment.vo.MultiPaymentVo;
import com.dmi.smartux.admin.multipayment.vo.PtUxPaymentPpmVo;

public interface MultiPaymentService {
	/**
     * MultiPayment 결제 정보 리스트 조회
     * @throws Exception
     */
	public List<MultiPaymentVo> getMultiPaymentList(MultiPaymentSearchVo multipaymentSearchVO) throws Exception;
	
	/**
     * MultiPayment 결제 정보 조회
     * @throws Exception
     */
	public MultiPaymentVo getMultiPaymentInfo(MultiPaymentSearchVo multipaymentSearchVO) throws Exception;

    /**
     * MultiPayment 결제 정보 개수 조회
     * @throws Exception
     */
    public int getMultiPaymentListCount(MultiPaymentSearchVo multipaymentSearchVO) throws Exception;
    
    
    
    
    /**
     * 다회선 결제 정보 리스트 조회 key
     * @throws Exception
     */
	public List<PtUxPaymentPpmVo> getPtUxPaymentPpmListByPakey(PtUxPaymentPpmVo ptuxpaymentppmVo) throws Exception;
	
    /**
     * MultiPayment 다회선 결제 정보 개수 조회 key
     * @throws Exception
     */
    public int getMultiPaymentCountByPakey(PtUxPaymentPpmVo ptuxpaymentppmVo) throws Exception;
    
	
	/**
     * 다회선 결제 정보 리스트 조회 said
     * @throws Exception
     */
	public List<PtUxPaymentPpmVo> getPtUxPaymentPpmListBySaid(PtUxPaymentPpmVo ptuxpaymentppmVo) throws Exception;
	
	
    /**
     * MultiPayment 다회선 결제 정보 개수 조회 said
     * @throws Exception
     */
    public int getMultiPaymentCountBySaid(PtUxPaymentPpmVo ptuxpaymentppmVo) throws Exception;
    
	/**
     * 다회선 결제 정보 리스트 조회 
     * @throws Exception
     */
	public List<PtUxPaymentPpmVo> getPtUxPaymentPpmListByDate(PtUxPaymentPpmVo ptuxpaymentppmVo) throws Exception;
	
	
    /**
     * MultiPayment 다회선 결제 정보 개수 조회 
     * @throws Exception
     */
    public int getMultiPaymentCountByDate(PtUxPaymentPpmVo ptuxpaymentppmVo) throws Exception;
   
    
    
    
	/**
     * MultiPayment 다회선 결제 정보 조회
     * @throws Exception
     */
	public PtUxPaymentPpmVo getMultiPaymentPpmInfo(PtUxPaymentPpmVo ptuxpaymentppmVo) throws Exception;

    
    
    
	
}