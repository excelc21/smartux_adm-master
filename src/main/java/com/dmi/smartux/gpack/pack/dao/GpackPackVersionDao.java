package com.dmi.smartux.gpack.pack.dao;

import org.springframework.stereotype.Repository;

import com.dmi.smartux.common.dao.CommonMimsDao;

@Repository
public class GpackPackVersionDao extends CommonMimsDao {

	/**
	 * 팩 버전 정보 조회
	 * @return		팩 버전 정보
	 * @throws Exception
	 */
	public String getPackVersion(String pack_id) throws Exception{
		String result = (String)(getSqlMapClientTemplate().queryForObject("gpack_pack_version.getPackVersion", pack_id));
		return result;
	}
	
}
