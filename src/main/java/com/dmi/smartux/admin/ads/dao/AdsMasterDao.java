package com.dmi.smartux.admin.ads.dao;

import java.sql.SQLException;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.dmi.smartux.common.dao.CommonDao;
import com.dmi.smartux.admin.ads.vo.AdsMasterVO;

@Repository
public class AdsMasterDao extends CommonDao {
	/**
	 * 광고 마스터 아이디생성
	 *
	 * @return 광고 마스터 아이디생성
	 * @throws DataAccessException
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public String getAdsMasterId() throws DataAccessException, SQLException {
		
		String adsId=(String)getSqlMapClientTemplate().getSqlMapClient().queryForObject("adsMaster.getAdsMasterId", null);
		
		return adsId;
	}
	/**
	 * 광고 마스터 리스트 조회
	 *
	 * @return 광고 마스터 리스트
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<AdsMasterVO> getAdsMasterList() throws DataAccessException {
		return (List<AdsMasterVO>) getSqlMapClientTemplate().queryForList("adsMaster.getAdsMasterList");
	}

	public AdsMasterVO getAdsMaster(AdsMasterVO adsMasterVO) {
		return (AdsMasterVO) getSqlMapClientTemplate().queryForObject("adsMaster.getAdsMaster", adsMasterVO);
	}

	/**
	 * 광고 마스터 등록
	 *
	 * @param adsMasterVO 광고 마스터 객체
	 * @throws DataAccessException
	 */
	public void insertAdsMaster(AdsMasterVO adsMasterVO) throws DataAccessException {
		adsMasterVO.setActGB("I");
		getSqlMapClientTemplate().insert("adsMaster.insertAdsMaster", adsMasterVO);
		getSqlMapClientTemplate().insert("adsMaster.insertLog", adsMasterVO);
	}

	/**
	 * 광고 마스터 수정
	 *
	 * @param adsMasterVO 광고 마스터 객체
	 * @throws DataAccessException
	 */
	public void updateAdsMaster(AdsMasterVO adsMasterVO) throws DataAccessException {
		adsMasterVO.setActGB("U");
		getSqlMapClientTemplate().update("adsMaster.updateAdsMaster", adsMasterVO);
		getSqlMapClientTemplate().insert("adsMaster.insertLog", adsMasterVO);
	}

	/**
	 * 광고 마스터 삭제
	 *
	 * @param adsMasterVO 광고 마스터 객체
	 * @throws DataAccessException
	 */
	public void deleteAdsMaster(AdsMasterVO adsMasterVO) throws DataAccessException {
		adsMasterVO.setActGB("D");
		getSqlMapClientTemplate().insert("adsMaster.insertLog", adsMasterVO);
		getSqlMapClientTemplate().delete("adsMaster.deleteAdsMaster", adsMasterVO);
	}


}
