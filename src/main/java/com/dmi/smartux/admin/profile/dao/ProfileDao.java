package com.dmi.smartux.admin.profile.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.dmi.smartux.admin.profile.vo.ProfileVO;
import com.dmi.smartux.common.dao.CommonHmimsDao;


@Repository
public class ProfileDao extends CommonHmimsDao {
	/**
	 * 광고 전체 리스트 조회
	 *
	 * @param adsVO 광고 객체
	 * @return 광고 전체 리스트
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<ProfileVO> getProfileList(ProfileVO profileVO) throws DataAccessException {
		//adsVO.setStartNum(adsVO.getPageNum() * adsVO.getPageSize() - adsVO.getPageSize() + 1);
		//adsVO.setEndNum(adsVO.getStartNum() + adsVO.getPageSize() - 1);

		return getSqlMapClientTemplate().queryForList("admin_profile.getProfileList", profileVO);
	}

	/**
	 * 순서 변경을 위해 모든 리스트를 가져온다.
	 *
	 * @param masterID 마스터 아이디
	 * @return 모든 리스트
	 * @throws DataAccessException
	 */
	/*@SuppressWarnings("unchecked")
	public List<ProfileVO> getAllList(String profileMstId) throws DataAccessException {
		Map<String, String> param = new HashMap<String, String>();
		param.put("profileMstId", profileMstId);

		return getSqlMapClientTemplate().queryForList("admin_profile.getProfileAllList", param);
	}*/

	/**
	 * 광고 조회
	 *
	 * @param number 게시물 번호
	 * @return 광고 객체
	 * @throws DataAccessException
	 */
	public ProfileVO getProfile(String profileImgId) throws DataAccessException {
		return (ProfileVO) getSqlMapClientTemplate().queryForObject("admin_profile.getProfile", profileImgId);
	}

	/**
	 * 광고 삽입
	 *
	 * @param adsVO 광고 객체
	 * @return 삽입된 광고 번호
	 * @throws DataAccessException
	 */
	
	
	public void insertProfile(ProfileVO profileVO) throws DataAccessException {
		//profileMasterVO.setActGB("I");
		getSqlMapClientTemplate().insert("admin_profile.insertProfile", profileVO);
		//getSqlMapClientTemplate().insert("profileMaster.insertLog", profileMasterVO);
	}
	

	/**
	 * 광고 개수 조회
	 *
	 * @param adsVO 광고 객체
	 * @return 광고 개수
	 * @throws DataAccessException
	 */
	public int getProfileListCtn(ProfileVO profileVO) throws DataAccessException {
		return (Integer) getSqlMapClientTemplate().queryForObject("admin_profile.getProfileListCtn", profileVO);
	}

	/**
	 * 광고 삭제
	 *
	 * @param numbers 게시물 번호
	 * @throws DataAccessException
	 */
	public void deleteProfile(String profileImgId) throws DataAccessException {
		getSqlMapClientTemplate().delete("admin_profile.deleteProfile", profileImgId);
	}

	/**
	 * 광고 업데이트
	 *
	 * @param adsVO 광고 객체
	 * @throws DataAccessException
	 */
	public void updateProfile(ProfileVO profileVO) throws DataAccessException {
		getSqlMapClientTemplate().update("admin_profile.updateProfile", profileVO);
	}

	/**
	 * 광고 순서 변경
	 *
	 * @param adsVO 광고 객체
	 * @throws DataAccessException
	 */
	public int updateSearch(int orders, String profileMstId, String seqs)throws DataAccessException{
		
		Map<String, String> param = new HashMap<String, String>();
		
		
		param.put("seqs", seqs);
		param.put("orders", Integer.toString(orders));
		param.put("profileMstId", profileMstId);
		
		
		int result = getSqlMapClientTemplate().update("admin_profile.updateSearch", param);
		
		return result;
	}

}
