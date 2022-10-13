package com.dmi.smartux.admin.login.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dmi.smartux.admin.login.vo.AdminMenuVO;
import com.dmi.smartux.common.dao.CommonIptvDao;


/**
 * 2021.08.23 password log
 * password log Dao
 * @author medialog
 *
 */
@Repository
public class AdminMenuDao extends CommonIptvDao{
	
	/**
	 * 2021.08.23 password log
	 * 권한별 메뉴 목록 조회
	 * @param variation_id
	 * @return
	 */
	public List<AdminMenuVO> getMenuList (String user_auth, String strArr) {
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("user_auth", user_auth);
		param.put("strArr", strArr);
		return (List<AdminMenuVO>) getSqlMapClientTemplate().queryForList("admin_menu.getMenuList", param);
	}
	
	
	
}
