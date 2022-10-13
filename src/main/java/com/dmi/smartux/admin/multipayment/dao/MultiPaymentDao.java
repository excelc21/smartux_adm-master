package com.dmi.smartux.admin.multipayment.dao;

import com.dmi.smartux.admin.multipayment.vo.MultiPaymentSearchVo;
import com.dmi.smartux.admin.multipayment.vo.MultiPaymentVo;
import com.dmi.smartux.common.dao.CommonDao;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import com.dmi.smartux.admin.multipayment.vo.PtUxPaymentPpmVo;

import java.util.List;

@Repository
public class MultiPaymentDao extends CommonDao {
    
    /**
     * MultiPayment 결제 정보 리스트 조회
     *
     */
    @SuppressWarnings("unchecked")
	public List<MultiPaymentVo> getMultiPaymentList(MultiPaymentSearchVo multiPaymentSearchVO) throws DataAccessException {
    	multiPaymentSearchVO.setStart_rnum(multiPaymentSearchVO.getPageNum() * multiPaymentSearchVO.getPageSize() - multiPaymentSearchVO.getPageSize() + 1);
    	multiPaymentSearchVO.setEnd_rnum(multiPaymentSearchVO.getStart_rnum() + multiPaymentSearchVO.getPageSize() - 1);

        return getSqlMapClientTemplate().queryForList("multipayment.multipaymentList", multiPaymentSearchVO);
    }
    
    /**
     * MultiPayment 결제 정보 상세
     *
     */
	public MultiPaymentVo getMultiPaymentInfo(MultiPaymentSearchVo multiPaymentSearchVO) throws DataAccessException {
        return (MultiPaymentVo) getSqlMapClientTemplate().queryForObject("multipayment.multipaymentInfo", multiPaymentSearchVO);
    }

    /**
     * MultiPayment 결제 정보 개수 조회
     *
     */
    public int getMultiPaymentListCount(MultiPaymentSearchVo multiPaymentSearchVO) throws DataAccessException {
        return (Integer) getSqlMapClientTemplate().queryForObject("multipayment.multipaymentListCount", multiPaymentSearchVO);
    }
    
    
    
    
    
    /**
     * 다회선 결제 정보 리스트 조회 pakey
     *
     */
    @SuppressWarnings("unchecked")
	public List<PtUxPaymentPpmVo> getPtUxPaymentPpmVoListByPakey(PtUxPaymentPpmVo ptuxPaymentPpmVo) throws DataAccessException {
    	ptuxPaymentPpmVo.setStart_rnum(ptuxPaymentPpmVo.getPageNum() * ptuxPaymentPpmVo.getPageSize() - ptuxPaymentPpmVo.getPageSize() + 1);
    	ptuxPaymentPpmVo.setEnd_rnum(ptuxPaymentPpmVo.getStart_rnum() + ptuxPaymentPpmVo.getPageSize() - 1);

    	
    	
        return getSqlMapClientTemplate().queryForList("multipayment.multipaymentListBypakey", ptuxPaymentPpmVo);
    }
    
    
    /**
     * 다회선 결제 정보 개수 조회 pakey
     *
     */
    public int getPtUxPaymentPpmVoCountByPakey(PtUxPaymentPpmVo ptuxPaymentPpmVo) throws DataAccessException {
        return (Integer) getSqlMapClientTemplate().queryForObject("multipayment.multipaymentCountBypakey", ptuxPaymentPpmVo);
    }
    
    
    
    /**
     * 다회선 결제 정보 리스트 조회 said
     *
     */
    @SuppressWarnings("unchecked")
	public List<PtUxPaymentPpmVo> getPtUxPaymentPpmVoListBySaid(PtUxPaymentPpmVo ptuxPaymentPpmVo) throws DataAccessException {
    	ptuxPaymentPpmVo.setStart_rnum(ptuxPaymentPpmVo.getPageNum() * ptuxPaymentPpmVo.getPageSize() - ptuxPaymentPpmVo.getPageSize() + 1);
    	ptuxPaymentPpmVo.setEnd_rnum(ptuxPaymentPpmVo.getStart_rnum() + ptuxPaymentPpmVo.getPageSize() - 1);
    	logger.info("query check : "+ptuxPaymentPpmVo.getStart_rnum());
    	logger.info("query check : "+ptuxPaymentPpmVo.getEnd_rnum());
    	
        return getSqlMapClientTemplate().queryForList("multipayment.multipaymentListBysaid", ptuxPaymentPpmVo);
    }
    
    
    /**
     * 다회선 결제 정보 개수 조회 said
     *
     */
    public int getPtUxPaymentPpmVoCountBySaid(PtUxPaymentPpmVo ptuxPaymentPpmVo) throws DataAccessException {
        return (Integer) getSqlMapClientTemplate().queryForObject("multipayment.multipaymentCountBysaid", ptuxPaymentPpmVo);
    }
    
    
    /**
     * 다회선 결제 정보 리스트 조회 
     *
     */
    @SuppressWarnings("unchecked")
	public List<PtUxPaymentPpmVo> getPtUxPaymentPpmVoListByDate(PtUxPaymentPpmVo ptuxPaymentPpmVo) throws DataAccessException {
    	ptuxPaymentPpmVo.setStart_rnum(ptuxPaymentPpmVo.getPageNum() * ptuxPaymentPpmVo.getPageSize() - ptuxPaymentPpmVo.getPageSize() + 1);
    	ptuxPaymentPpmVo.setEnd_rnum(ptuxPaymentPpmVo.getStart_rnum() + ptuxPaymentPpmVo.getPageSize() - 1);
    	logger.info("query check : "+ptuxPaymentPpmVo.getStart_rnum());
    	logger.info("query check : "+ptuxPaymentPpmVo.getEnd_rnum());
    	
        return getSqlMapClientTemplate().queryForList("multipayment.multipaymentListBydate", ptuxPaymentPpmVo);
    }
    
    
    /**
     * 다회선 결제 정보 개수 조회 
     *
     */
    public int getPtUxPaymentPpmVoCountByDate(PtUxPaymentPpmVo ptuxPaymentPpmVo) throws DataAccessException {
        return (Integer) getSqlMapClientTemplate().queryForObject("multipayment.multipaymentCountBydate", ptuxPaymentPpmVo);
    }
    
    
    
    /**
     * MultiPayment 다회선 결제 정보 상세
     *
     */
	public PtUxPaymentPpmVo getMultiPaymentPpmInfo(PtUxPaymentPpmVo ptuxPaymentPpmVo) throws DataAccessException {
        return (PtUxPaymentPpmVo)getSqlMapClientTemplate().queryForObject("multipayment.multipaymentPpmInfo", ptuxPaymentPpmVo);
    }
    
    
}
