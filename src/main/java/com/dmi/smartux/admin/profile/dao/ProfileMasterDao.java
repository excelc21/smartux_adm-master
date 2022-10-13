package com.dmi.smartux.admin.profile.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.dmi.smartux.admin.profile.vo.ProfileMasterVO;
import com.dmi.smartux.common.dao.CommonHmimsDao;


@Repository
public class ProfileMasterDao extends CommonHmimsDao {
	/**
	 * 광고 마스터 리스트 조회
	 * @param service_type 
	 *
	 * @return 광고 마스터 리스트
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<ProfileMasterVO> getProfileMasterList(ProfileMasterVO profileMasterVO) throws DataAccessException {
		return (List<ProfileMasterVO>) getSqlMapClientTemplate().queryForList("admin_profileMaster.getProfileMasterList", profileMasterVO);
	}

	
	/**
	 * 광고 마스터 등록
	 *
	 * @param profileMasterVO 광고 마스터 객체
	 * @throws DataAccessException
	 */
	public void insertProfileMaster(ProfileMasterVO profileMasterVO) throws DataAccessException {
		//profileMasterVO.setActGB("I");
		getSqlMapClientTemplate().insert("admin_profileMaster.insertProfileMaster", profileMasterVO);
		//getSqlMapClientTemplate().insert("admin_profileMaster.insertLog", profileMasterVO);
	}

	/**
	 * 광고 마스터 수정
	 *
	 * @param profileMasterVO 광고 마스터 객체
	 * @throws DataAccessException
	 */
	public void updateProfileMaster(ProfileMasterVO profileMasterVO) throws DataAccessException {
	//	profileMasterVO.setActGB("U");
		getSqlMapClientTemplate().update("admin_profileMaster.updateProfileMaster", profileMasterVO);
		//getSqlMapClientTemplate().insert("admin_profileMaster.insertLog", profileMasterVO);
	}

	/**
	 * 광고 마스터 삭제
	 *
	 * @param profileMasterVO 광고 마스터 객체
	 * @throws DataAccessException
	 */
	public void deleteProfileMaster(ProfileMasterVO profileMasterVO) throws DataAccessException {
		//profileMasterVO.setActGB("D");
	//	getSqlMapClientTemplate().insert("admin_profileMaster.insertLog", profileMasterVO);
		getSqlMapClientTemplate().delete("admin_profileMaster.deleteProfileMaster", profileMasterVO);
	}


	/**
	 * 광고 마스터 중복체크
	 *
	 * @param profileMasterVO 광고 마스터 객체
	 * @throws DataAccessException
	 */
	public int checkProfileMaster(ProfileMasterVO vo) {
		return (Integer) getSqlMapClientTemplate().queryForObject("admin_profileMaster.checkProfileMaster", vo);
	}


}
