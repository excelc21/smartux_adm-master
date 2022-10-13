package com.dmi.smartux.admin.ads.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.admin.ads.dao.AdsDao;
import com.dmi.smartux.admin.ads.service.AdsService;
import com.dmi.smartux.admin.ads.vo.AdsVO;
import com.dmi.smartux.admin.imcs.service.ImcsService;

@Service
public class AdsServiceImpl implements AdsService {
	
	private final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	AdsDao mDao;
	
	@Autowired
	ImcsService imcsService;

	@Override
	@Transactional
	public int insertAds(AdsVO adsVO) throws Exception {
		int num = mDao.insertAds(adsVO);
		adsVO.setNumber(num);
		mDao.insertAdsLog(adsVO);
		
		return num;
	}

	@Override
	public int getAdsListCtn(AdsVO adsVO) throws Exception {
		return mDao.getAdsListCtn(adsVO);
	}

	@Override
	public List<AdsVO> getAdsList(AdsVO adsVO) throws Exception {
		return mDao.getAdsList(adsVO);
	}

	@Override
	public List<AdsVO> getAllList(String masterID) throws Exception {
		return mDao.getAllList(masterID);
	}

	@Override
	@Transactional
	public void deleteAds(String numbers, String cookieID, String ip) throws Exception {

		String[] adsList = numbers.split(",");

		for (String ads : adsList) {
			String number = ads.trim();

			AdsVO adsVO = mDao.getAds(Integer.parseInt(number));
			adsVO.setActGbn("D");
			adsVO.setActID(cookieID);
			adsVO.setActIP(ip);

			String saveFileName = adsVO.getSaveFileName();

			if (!"".equals(GlobalCom.isNull(saveFileName))) {
				adsVO.setSaveFileName(saveFileName.substring(saveFileName.lastIndexOf("/") + 1));
			}

			mDao.insertAdsLog(adsVO);
			mDao.deleteAds(number);
			
			//2021-04-22 BPAS 편성관리 추가
			try {
				if(adsVO.getType() == 2 || adsVO.getType() == 3) {
					imcsService.insertImcs("D", "BAN", String.valueOf(adsVO.getNumber()), "", "", "");
				}
			}catch (Exception e) {
				logger.error("[insertImcs]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			}
		}
	}

	@Override
	public AdsVO getAds(int number) throws Exception {
		return mDao.getAds(number);
	}

	@Override
	public void updateAds(AdsVO adsVO) throws Exception {
		mDao.updateAds(adsVO);
		mDao.insertAdsLog(adsVO);
	}

	@Override
	@Transactional
	public void changeOrder(List<String> orderList) throws Exception {
		int count = orderList.size();

		for (int i=0; i<count; i++) {
			AdsVO vo = new AdsVO();
			vo.setNumber(Integer.parseInt(orderList.get(i)));
			vo.setOrder(String.valueOf(i + 1));
			mDao.changeOrder(vo);
		}
	}

	@Override
	public int getLiveCount(String masterID) throws Exception {
		return mDao.getLiveCount(masterID);
	}

	/**
	 * 만료된 라이브 광고의 LiveType을 D로 변경
	 */
	@Override
	public void checkExpire() throws Exception {
		mDao.checkExpire();
	}

	@Override
	public void insertBannerVersion() {
		mDao.insertBannerVersion();
	}

	@Override
	public String checkLastApply() {
		return mDao.checkLastApply();
	}

	@Override
	public String getNow() {
		return mDao.getNow();
	}

	
}
