package com.dmi.smartux.admin.ads.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.dmi.smartux.common.dao.CommonDao;
import com.dmi.smartux.admin.ads.vo.AdsVO;

@Repository
public class AdsDao extends CommonDao {
	/**
	 * 광고 전체 리스트 조회
	 *
	 * @param adsVO 광고 객체
	 * @return 광고 전체 리스트
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<AdsVO> getAdsList(AdsVO adsVO) throws DataAccessException {
		adsVO.setStartNum(adsVO.getPageNum() * adsVO.getPageSize() - adsVO.getPageSize() + 1);
		adsVO.setEndNum(adsVO.getStartNum() + adsVO.getPageSize() - 1);

		return getSqlMapClientTemplate().queryForList("ads.getAdsList", adsVO);
	}

	/**
	 * 순서 변경을 위해 모든 리스트를 가져온다.
	 *
	 * @param masterID 마스터 아이디
	 * @return 모든 리스트
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<AdsVO> getAllList(String masterID) throws DataAccessException {
		Map<String, String> param = new HashMap<String, String>();
		param.put("masterID", masterID);

		return getSqlMapClientTemplate().queryForList("ads.getAllList", param);
	}

	/**
	 * 광고 조회
	 *
	 * @param number 게시물 번호
	 * @return 광고 객체
	 * @throws DataAccessException
	 */
	public AdsVO getAds(int number) throws DataAccessException {
		return (AdsVO) getSqlMapClientTemplate().queryForObject("ads.getAds", number);
	}

	/**
	 * 광고 삽입
	 *
	 * @param adsVO 광고 객체
	 * @return 삽입된 광고 번호
	 * @throws DataAccessException
	 */
	public int insertAds(AdsVO adsVO) throws DataAccessException {
		return (Integer) getSqlMapClientTemplate().insert("ads.insertAds", adsVO);
	}

	/**
	 * 로그 삽입
	 *
	 * @param adsVO 광고 객체
	 * @throws DataAccessException
	 */
	public void insertAdsLog(AdsVO adsVO) throws DataAccessException {
		getSqlMapClientTemplate().insert("ads.insertAdsLog", adsVO);
	}

	/**
	 * 광고 개수 조회
	 *
	 * @param adsVO 광고 객체
	 * @return 광고 개수
	 * @throws DataAccessException
	 */
	public int getAdsListCtn(AdsVO adsVO) throws DataAccessException {
		return (Integer) getSqlMapClientTemplate().queryForObject("ads.getAdsListCtn", adsVO);
	}

	/**
	 * 광고 삭제
	 *
	 * @param numbers 게시물 번호
	 * @throws DataAccessException
	 */
	public void deleteAds(String numbers) throws DataAccessException {
		getSqlMapClientTemplate().delete("ads.deleteAds", numbers);
	}

	/**
	 * 광고 업데이트
	 *
	 * @param adsVO 광고 객체
	 * @throws DataAccessException
	 */
	public void updateAds(AdsVO adsVO) throws DataAccessException {
		getSqlMapClientTemplate().update("ads.updateAds", adsVO);
	}

	/**
	 * 광고 순서 변경
	 *
	 * @param adsVO 광고 객체
	 * @throws DataAccessException
	 */
	public void changeOrder(AdsVO adsVO) throws DataAccessException {
		getSqlMapClientTemplate().update("ads.changeOrder", adsVO);
	}

	/**
	 * 라이브된 광고 수 조회
	 *
	 * @param masterID 광고 아이디
	 * @return 광고 아이디에 속해 있는 광고 수
	 * @throws DataAccessException
	 */
	public int getLiveCount(String masterID) throws DataAccessException {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("masterID", masterID);

		return (Integer) getSqlMapClientTemplate().queryForObject("ads.getLiveCount", paramMap);
	}

	/**
     * 만료된 라이브 광고의 LiveType을 D로 변경
     */
    public void checkExpire() throws DataAccessException {
        getSqlMapClientTemplate().update("ads.checkExpire");
    }

	public void insertBannerVersion() {
		getSqlMapClientTemplate().insert("ads.insertBannerVersion");
	}

	public String checkLastApply() {
		return (String) getSqlMapClientTemplate().queryForObject("ads.checkLastApply");
	}

	public String getNow() {
		return (String) getSqlMapClientTemplate().queryForObject("ads.getNow");
	}
}
