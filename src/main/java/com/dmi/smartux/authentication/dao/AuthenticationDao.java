package com.dmi.smartux.authentication.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.dmi.smartux.common.dao.CommonDao;
import com.dmi.smartux.authentication.vo.AuthenticationVO;

@Repository
public class AuthenticationDao extends CommonDao {
	
	/*이거 사용 안함*/
	public List<AuthenticationVO> getAuthSystemData(String system_code) throws DataAccessException {
		Map<String, String> param = new HashMap<String, String>();
		param.put("system_code", system_code);
		
		List<AuthenticationVO> list = getSqlMapClientTemplate().queryForList("authentication.getAuthSystemData", param);
		
		return list;
	}

	public List<AuthenticationVO> getAuthSystemData() {
		List<AuthenticationVO> list = getSqlMapClientTemplate().queryForList("authentication.getAuthSystemDataNotCode");
		
		return list;
	}
}
