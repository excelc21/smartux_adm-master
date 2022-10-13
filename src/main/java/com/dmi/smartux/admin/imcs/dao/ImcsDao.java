package com.dmi.smartux.admin.imcs.dao;

import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.dmi.smartux.admin.commonMng.vo.MenuTreeVo;
import com.dmi.smartux.admin.imcs.vo.ImcsVO;
import com.dmi.smartux.common.dao.CommonDao;
import com.dmi.smartux.common.dao.CommonIptvDao;

/**
 * 2020-06-25 추가
 */
@Repository
public class ImcsDao extends CommonIptvDao {
	
	/**
	 * Imcs 매핑정보 등록
	 * @param paramVO
	 * @return void
	 * @throws DataAccessException
	 */
	public void insertImcsInfo(ImcsVO paramVO) throws DataAccessException{
		getSqlMapClientTemplate().insert("imcs_info.insertImcsInfo",paramVO);
	}

	/**
	 * Imcs 매핑정보 수정
	 * @param paramVO
	 * @return void
	 * @throws DataAccessException
	 */
	public void updateImcsInfo(ImcsVO param) {
		getSqlMapClientTemplate().update("imcs_info.updateImcsInfo", param);
	}

	/**
	 * Imcs 매핑정보 삭제
	 * @param paramVO
	 * @return void
	 * @throws DataAccessException
	 */
	public void deleteImcsInfo(ImcsVO param) {
		getSqlMapClientTemplate().delete("imcs_info.deleteImcsInfo", param);
	}

	/**
	 * Imcs 매핑정보 카운트 조회
	 * @param paramVO
	 * @return void
	 * @throws DataAccessException
	 */
	public int getImcsInfoCnt(ImcsVO param) {
		return (Integer) getSqlMapClientTemplate().queryForObject("imcs_info.getImcsInfoCnt", param);
	}

	/**
	 * Imcs 지면 삭제
	 * @param paramVO
	 * @return void
	 * @throws DataAccessException
	 */
	public void deletePap(Map<String, String> param) {
		getSqlMapClientTemplate().delete("imcs_info.deleteImcsPaper", param);
	}
	

}