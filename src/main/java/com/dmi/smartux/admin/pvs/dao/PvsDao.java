package com.dmi.smartux.admin.pvs.dao;

import com.dmi.smartux.admin.news.vo.TargetVO;
import com.dmi.smartux.admin.pvs.vo.PvsProductVO;
import com.dmi.smartux.common.dao.CommonVpsDao;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Pvs Dao
 *
 * @author dongho
 */
@Repository
public class PvsDao extends CommonVpsDao {
    /**
     * PVS 상품 리스트 조회
     *
     * @return 상품 리스트
     * @throws DataAccessException
     */
    @SuppressWarnings("unchecked")
    public List<PvsProductVO> getPvsProductList() throws DataAccessException {
        return getSqlMapClientTemplate().queryForList("pvs.getPvsProductList");
    }

    /**
     * PVS 부가 상품 리스트 조회
     *
     * @return 상품 리스트
     * @throws DataAccessException
     */
    @SuppressWarnings("unchecked")
    public List<PvsProductVO> getOptionalServiceList() throws DataAccessException {
        return getSqlMapClientTemplate().queryForList("pvs.getOptionalServiceList");
    }

    /**
     * 타겟팅된 가입자의 가입번호 리스트 조회(ALL)
     *
     * @param targetVO 타겟룰 객체
     * @return 가입번호 리스트
     * @throws DataAccessException
     */
    @SuppressWarnings("unchecked")
    public List<String> getAllSaIDList(TargetVO targetVO) throws DataAccessException {
        return getSqlMapClientTemplate().queryForList("pvs.getAllSaIDList", targetVO);
    }

    /**
     * 타겟팅된 가입자의 가입번호 개수 조회(ALL)
     *
     * @param targetVO 타겟룰 객체
     * @return 가입번호 개수
     * @throws DataAccessException
     */
    public int getAllSaIDCount(TargetVO targetVO) throws DataAccessException {
        return (Integer) getSqlMapClientTemplate().queryForObject("pvs.getAllSaIDCount", targetVO);
    }

    /**
     * 타겟팅된 가입자의 가입번호 리스트 조회(부가서비스 포함)
     *
     * @param targetVO 타겟룰 객체
     * @return 가입번호 리스트
     * @throws DataAccessException
     */
    @SuppressWarnings("unchecked")
    public List<String> getIncludeSaIDList(TargetVO targetVO) throws DataAccessException {
        return getSqlMapClientTemplate().queryForList("pvs.getIncludeSaIDList", targetVO);
    }

    /**
     * 타겟팅된 가입자의 가입번호 개수 조회(부가서비스 포함)
     *
     * @param targetVO 타겟룰 객체
     * @return 가입번호 개수
     * @throws DataAccessException
     */
    public int getIncludeSaIDCount(TargetVO targetVO) throws DataAccessException {
        return (Integer) getSqlMapClientTemplate().queryForObject("pvs.getIncludeSaIDCount", targetVO);
    }

    /**
     * 타겟팅된 가입자의 가입번호 리스트 조회(부가서비스 미포함)
     *
     * @param targetVO 타겟룰 객체
     * @return 가입번호 리스트
     * @throws DataAccessException
     */
    @SuppressWarnings("unchecked")
    public List<String> getExcludeSaIDList(TargetVO targetVO) throws DataAccessException {
        return getSqlMapClientTemplate().queryForList("pvs.getExcludeSaIDList", targetVO);
    }

    /**
     * 타겟팅된 가입자의 가입번호 개수 조회(부가서비스 미포함)
     *
     * @param targetVO 타겟룰 객체
     * @return 가입번호 개수
     * @throws DataAccessException
     */
    public int getExcludeSaIDCount(TargetVO targetVO) throws DataAccessException {
        return (Integer) getSqlMapClientTemplate().queryForObject("pvs.getExcludeSaIDCount", targetVO);
    }
}
