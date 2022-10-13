package com.dmi.smartux.admin.ads.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmi.smartux.admin.ads.dao.AdsMasterDao;
import com.dmi.smartux.admin.ads.service.AdsMasterService;
import com.dmi.smartux.admin.ads.vo.AdsMasterVO;

@Service
public class AdsMasterServiceImpl implements AdsMasterService {
	@Autowired
	AdsMasterDao mDao;
	
	/**
	 * 광고 마스터 아이디생성
	 *
	 * @return 광고 마스터 아이디생성
	 * @throws Exception
	 */
	@Override
	public String getAdsMasterId() throws Exception {
		return mDao.getAdsMasterId();
	}
	/**
	 * 광고 마스터 리스트 조회
	 *
	 * @return 광고 마스터 리스트
	 * @throws Exception
	 */
	@Override
	public List<AdsMasterVO> getAdsMasterList() throws Exception {
		return mDao.getAdsMasterList();
	}

	@Override
	public AdsMasterVO getAdsMaster(AdsMasterVO adsMasterVO) throws Exception {
		return mDao.getAdsMaster(adsMasterVO);
	}

	/**
	 * 광고 마스터 등록
	 *
	 * @param adsMasterVO 광고 마스터 객체
	 * @throws Exception
	 */
	@Override
	public void insertAdsMaster(AdsMasterVO adsMasterVO) throws Exception {
		mDao.insertAdsMaster(adsMasterVO);
	}

	/**
	 * 광고 마스터 수정
	 *
	 * @param adsMasterVO 광고 마스터 객체
	 * @throws Exception
	 */
	@Override
	public void updateAdsMaster(AdsMasterVO adsMasterVO) throws Exception {
		mDao.updateAdsMaster(adsMasterVO);
	}

	/**
	 * 광고 마스터 삭제
	 *
	 * @param adsMasterVO 광고 마스터 객체
	 * @throws Exception
	 */
	@Override
	public void deleteAdsMaster(AdsMasterVO adsMasterVO) throws Exception {
		mDao.deleteAdsMaster(adsMasterVO);
	}
}