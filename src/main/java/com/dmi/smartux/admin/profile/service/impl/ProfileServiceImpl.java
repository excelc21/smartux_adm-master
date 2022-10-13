package com.dmi.smartux.admin.profile.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmi.smartux.admin.profile.dao.ProfileDao;
import com.dmi.smartux.admin.profile.service.ProfileService;
import com.dmi.smartux.admin.profile.vo.ProfileVO;
import com.dmi.smartux.common.util.GlobalCom;


@Service
public class ProfileServiceImpl implements ProfileService {
	@Autowired
	ProfileDao mDao;

	@Override
	public void insertProfile(ProfileVO profileVO) throws Exception {
		mDao.insertProfile(profileVO);
	}

	@Override
	public int getProfileListCtn(ProfileVO profileVO) throws Exception {
		return mDao.getProfileListCtn(profileVO);
	}
	

	@Override
	public List<ProfileVO> getProfileList(ProfileVO profileVO) throws Exception {
		
	
		return mDao.getProfileList(profileVO);
	}

	/*@Override
	public List<ProfileVO> getProfileAllList(String masterID) throws Exception {
		return mDao.getAllList(masterID);
	}*/

	@Override
	@Transactional
	public void deleteProfile(String profileImgIds, String cookieID, String ip) throws Exception {

		String[] profileList = profileImgIds.split(",");

		for (String profile : profileList) {
			String profileImgId = profile.trim();

			ProfileVO profileVO = mDao.getProfile(profileImgId);
			/*profileVO.setActGbn("D");
			profileVO.setActID(cookieID);
			profileVO.setActIP(ip);*/

			String profileImgName = profileVO.getProfileImgName();

			if (!"".equals(GlobalCom.isNull(profileImgName))) {
				profileVO.setProfileImgName(profileImgName.substring(profileImgName.lastIndexOf("/") + 1));
			}

			//mDao.insertProfileLog(profileVO);
			mDao.deleteProfile(profileImgId);
		}
	}

	@Override
	public ProfileVO getProfile(String profileImgId) throws Exception {
		return mDao.getProfile(profileImgId);
	}

	@Override
	public void updateProfile(ProfileVO profileVO) throws Exception {
		mDao.updateProfile(profileVO);
		//mDao.insertProfileLog(profileVO);
	}

	
	
	@Override
	@Transactional
	public int updateSearch(int[] orders, String profileMstId, String[] seqs)
			throws Exception {
		int result = 0;
		for(int i = 0; i < seqs.length; i ++){
			result += mDao.updateSearch(orders[i], profileMstId, seqs[i]);
		}
		return result;
	}
	
}
