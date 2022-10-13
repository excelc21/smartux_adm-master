package com.dmi.smartux.admin.login.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.dmi.smartux.admin.login.vo.PassLogVO;
import com.dmi.smartux.common.dao.CommonIptvDao;


/**
 * 2021.08.23 password log
 * password log Dao
 * @author medialog
 *
 */
@Repository
public class PassLogDao extends CommonIptvDao{
	
	/**
	 * 2021.08.23 password log
	 * password 목록 조회
	 * @param variation_id
	 * @return
	 */
	public List<PassLogVO> getPassList (PassLogVO param) {
		return (List<PassLogVO>) getSqlMapClientTemplate().queryForList("admin_pass.getPassList", param);
	}
	
	/**
	 * 관리자 계정 정보 수정
	 * @param vo	관리자 계정 정보 객체
	 * @throws DataAccessException
	 */
	public void insertPass(PassLogVO vo)  throws DataAccessException{
		getSqlMapClientTemplate().insert("admin_pass.insertPass", vo);
	}
	
	/**
	 * 관리자 계정 정보 수정
	 * @param vo	관리자 계정 정보 객체
	 * @throws DataAccessException
	 */
	public void deletePass(int pid)  throws DataAccessException{
		getSqlMapClientTemplate().delete("admin_pass.deletePass", pid);
	}
	
}
