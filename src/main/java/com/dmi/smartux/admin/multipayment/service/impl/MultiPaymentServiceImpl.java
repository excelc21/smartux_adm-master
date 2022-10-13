package com.dmi.smartux.admin.multipayment.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmi.smartux.admin.multipayment.dao.MultiPaymentDao;
import com.dmi.smartux.admin.multipayment.service.MultiPaymentService;
import com.dmi.smartux.admin.multipayment.vo.MultiPaymentSearchVo;
import com.dmi.smartux.admin.multipayment.vo.MultiPaymentVo;
import com.dmi.smartux.admin.multipayment.vo.PtUxPaymentPpmVo;

@Service
public class MultiPaymentServiceImpl implements MultiPaymentService{
	
	@Autowired
	MultiPaymentDao multipaymentDao;
    
    @Override
    public List<MultiPaymentVo> getMultiPaymentList(MultiPaymentSearchVo multipaymentSearchVo) throws Exception {
    	
        return multipaymentDao.getMultiPaymentList(multipaymentSearchVo);
    }
    
    @Override
    public MultiPaymentVo getMultiPaymentInfo(MultiPaymentSearchVo multipaymentSearchVo) throws Exception {
    	
        return multipaymentDao.getMultiPaymentInfo(multipaymentSearchVo);
    }

    @Override
    public int getMultiPaymentListCount(MultiPaymentSearchVo multipaymentSearchVo)  throws Exception {
    	
        return multipaymentDao.getMultiPaymentListCount(multipaymentSearchVo);
    }
    
    
    
    
    @Override
    public List<PtUxPaymentPpmVo> getPtUxPaymentPpmListByPakey(PtUxPaymentPpmVo ptuxpaymentppmVo) throws Exception {
    	
        return multipaymentDao.getPtUxPaymentPpmVoListByPakey(ptuxpaymentppmVo);
    }
    
    
    @Override
    public int getMultiPaymentCountByPakey(PtUxPaymentPpmVo ptuxpaymentppmVo)  throws Exception {
    	
        return multipaymentDao.getPtUxPaymentPpmVoCountByPakey(ptuxpaymentppmVo);
    }
    
    
    
    @Override
    public List<PtUxPaymentPpmVo> getPtUxPaymentPpmListBySaid(PtUxPaymentPpmVo ptuxpaymentppmVo) throws Exception {
    	
        return multipaymentDao.getPtUxPaymentPpmVoListBySaid(ptuxpaymentppmVo);
    }
    
    @Override
    public int getMultiPaymentCountBySaid(PtUxPaymentPpmVo ptuxpaymentppmVo)  throws Exception {
    	
        return multipaymentDao.getPtUxPaymentPpmVoCountBySaid(ptuxpaymentppmVo);
    }
    
    
    @Override
    public List<PtUxPaymentPpmVo> getPtUxPaymentPpmListByDate(PtUxPaymentPpmVo ptuxpaymentppmVo) throws Exception {
    	
        return multipaymentDao.getPtUxPaymentPpmVoListByDate(ptuxpaymentppmVo);
    }
    
    @Override
    public int getMultiPaymentCountByDate(PtUxPaymentPpmVo ptuxpaymentppmVo)  throws Exception {
    	
        return multipaymentDao.getPtUxPaymentPpmVoCountByDate(ptuxpaymentppmVo);
    }
    
    
    
    
    @Override
    public PtUxPaymentPpmVo getMultiPaymentPpmInfo(PtUxPaymentPpmVo ptuxpaymentppmVo) throws Exception {
    	
        return multipaymentDao.getMultiPaymentPpmInfo(ptuxpaymentppmVo);
    }

    
}
