package com.dmi.smartux.common.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

@Repository
public class ImageServerDao extends CommonDao {
	
	/**
	 * <pre>
	 * 서버별 지정된 이미지서버 IP를 가져온다. 
	 * </pre>
	 * @author medialog
	 * @date 2017. 5. 23.
	 * @method getImageServerIp
	 * @return String
	 */
	public String getImageServerIp() throws DataAccessException {
		return (String) getSqlMapClientTemplate().queryForObject("common.getImageServerIp");
	}

	/**
	 * <pre>
	 * 서버별 지정된 이미지서버 IP를 가져온다.(MIMS 패스정보 포함)
	 * </pre>
	 * @author medialog
	 * @date 2018. 9. 20.
	 * @method getImageServerIpMims
	 * @return String
	 */
	public String getImageServerIpMims() throws DataAccessException {
		return (String) getSqlMapClientTemplate().queryForObject("common.getImageServerIpMims");
	}
}