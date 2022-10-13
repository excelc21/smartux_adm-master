package com.dmi.smartux.admin.profile.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmi.smartux.admin.profile.dao.ProfileMasterDao;
import com.dmi.smartux.admin.profile.service.ProfileMasterService;
import com.dmi.smartux.admin.profile.vo.ProfileMasterVO;


@Service
public class ProfileMasterServiceImpl implements ProfileMasterService {
	
	@Autowired
	ProfileMasterDao mDao;

	/**
	 * 광고 마스터 리스트 조회
	 *
	 * @return 광고 마스터 리스트
	 * @throws Exception
	 */
	@Override
	public List<ProfileMasterVO> getProfileMasterList(ProfileMasterVO profileMasterVO) throws Exception {
		return mDao.getProfileMasterList(profileMasterVO);
	}

	

	/**
	 * 광고 마스터 등록
	 *
	 * @param adsMasterVO 광고 마스터 객체
	 * @throws Exception
	 */
	@Override
	public void insertProfileMaster(ProfileMasterVO profileMasterVO) throws Exception {
		mDao.insertProfileMaster(profileMasterVO);
	}

	/**
	 * 광고 마스터 수정
	 *
	 * @param adsMasterVO 광고 마스터 객체
	 * @throws Exception
	 */
	@Override
	public void updateProfileMaster(ProfileMasterVO profileMasterVO) throws Exception {
		mDao.updateProfileMaster(profileMasterVO);
	}

	/**
	 * 광고 마스터 삭제
	 *
	 * @param adsMasterVO 광고 마스터 객체
	 * @throws Exception
	 */
	@Override
	public void deleteProfileMaster(ProfileMasterVO profileMasterVO) throws Exception {
		mDao.deleteProfileMaster(profileMasterVO);
	}


	/**
	 * 광고 마스터 중복체크
	 *
	 * @param adsMasterVO 광고 마스터 객체
	 * @throws Exception
	 */
	@Override
	public int checkProfileMaster(ProfileMasterVO vo) {
		return mDao.checkProfileMaster(vo);
	}
}