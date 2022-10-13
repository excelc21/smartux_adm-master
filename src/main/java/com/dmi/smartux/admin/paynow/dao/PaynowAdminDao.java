package com.dmi.smartux.admin.paynow.dao;

import com.dmi.smartux.common.dao.CommonDao;
import com.dmi.smartux.admin.paynow.vo.PaynowBannerVO;
import com.dmi.smartux.admin.paynow.vo.PaynowReqVO;
import com.dmi.smartux.admin.paynow.vo.PaynowSearchVO;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Paynow Info Admin Data Access Object
 */
@Repository
public class PaynowAdminDao extends CommonDao {
    
    /**
     * Paynow 결제 정보 리스트 조회
     *
     * @param PaynowSearchVO paynowSearchVO
     * @return Paynow 결제 정보 리스트
     * @throws DataAccessException
     */
    @SuppressWarnings("unchecked")
	public List<PaynowReqVO> getPaynowReqList(PaynowSearchVO paynowSearchVO) throws DataAccessException {
    	paynowSearchVO.setStart_rnum(paynowSearchVO.getPageNum() * paynowSearchVO.getPageSize() - paynowSearchVO.getPageSize() + 1);
    	paynowSearchVO.setEnd_rnum(paynowSearchVO.getStart_rnum() + paynowSearchVO.getPageSize() - 1);

        return getSqlMapClientTemplate().queryForList("admin_paynow.paymentReqList", paynowSearchVO);
    }
    
    /**
     * Paynow 결제 정보 리스트 조회
     *
     * @param PaynowSearchVO paynowSearchVO
     * @return Payment 결제 정보 
     * @throws DataAccessException
     */
	public PaynowReqVO getPaynowReqInfo(PaynowSearchVO paynowSearchVO) throws DataAccessException {
        return (PaynowReqVO) getSqlMapClientTemplate().queryForObject("admin_paynow.paymentReqInfo", paynowSearchVO);
    }

    /**
     * Paynow 결제 정보 개수 조회
     *
     * @param PaynowSearchVO paynowSearchVO
     * @return Paynow 결제 정보 개수
     * @throws DataAccessException
     */
    public int getPaynowReqListCount(PaynowSearchVO paynowSearchVO) throws DataAccessException {
        return (Integer) getSqlMapClientTemplate().queryForObject("admin_paynow.paymentListCount", paynowSearchVO);
    }
    
    /**
     * Paynow 안내 배너 리스트 조회
     *
     * @param String codeID
     * @return Paynow 안내 배너 리스트
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	public List<PaynowBannerVO> getBannerList(String code) throws DataAccessException {
    	return getSqlMapClientTemplate().queryForList("admin_paynow.getBannerList", code);
    }
    
    /**
     * Paynow 안내 배너 조회
     *
     * @param String code, String banner_id
     * @return Paynow 안내 배너
     * @throws Exception
     */
	public PaynowBannerVO getBanner(String code, String banner_id) throws DataAccessException {
    	Map<String, String> param = new HashMap<String, String>();
		param.put("code", code);
		param.put("item_code", banner_id);
		
    	return (PaynowBannerVO) getSqlMapClientTemplate().queryForObject("admin_paynow.getBanner", param);
    }
    
    /**
     * Paynow 안내 배너 등록
     *
     * @throws Exception
     */
    public void addBanner(String code, String banner_id, String banner_text, String use_yn, String create_id) throws DataAccessException {
		Map<String, String> param = new HashMap<String, String>();
		param.put("code", code);
		param.put("item_code", banner_id);
		param.put("item_nm", banner_text);
		param.put("use_yn", use_yn);
		param.put("create_id", create_id);
		param.put("update_id", create_id);
		
		getSqlMapClientTemplate().insert("admin_paynow.addBanner", param);
	}
    
    /**
     * Paynow 안내 배너 수정
     *
     * @throws Exception
     */
    public void updateBanner(String code, String banner_id, String banner_text, String use_yn, String update_id) throws DataAccessException {
		Map<String, String> param = new HashMap<String, String>();
		param.put("code", code);
		param.put("item_code", banner_id);
		param.put("item_nm", banner_text);
		param.put("use_yn", use_yn);
		param.put("update_id", update_id);
		
		getSqlMapClientTemplate().update("admin_paynow.updateBanner", param);
	}
    
    /**
     * Paynow 안내 배너 삭제
     *
     * @throws Exception
     */
    public int deleteBanner(String code, String banner_id) throws DataAccessException {
		Map<String, String> param = new HashMap<String, String>();
		param.put("code", code);
		param.put("item_code", banner_id);
		
		return getSqlMapClientTemplate().delete("admin_paynow.deleteBanner", param);
	}
    
}
