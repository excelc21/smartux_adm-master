package com.dmi.smartux.admin.season.service;

import java.util.List;
import java.util.Map;

import com.dmi.smartux.admin.season.vo.SeasonSearchVo;
import com.dmi.smartux.admin.season.vo.SeasonVo;


public interface SeasonService {
	/**
	 * 즉시적용하기 위한 캐시 API호출
	 * @param url
	 * @param param
	 * @param timeout
	 * @param retrycnt
	 * @param protocolName
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> applyCache(String url, String param, int timeout, int retrycnt, String protocolName) throws Exception;
	
	/**
	 * 시즌 목록 조회
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public List<SeasonVo> getSeasonList(SeasonSearchVo vo) throws Exception;
	
	/**
	 * 시즌 목록 개수
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public int getSeasonListCnt(SeasonSearchVo vo) throws Exception;
	
	/**
	 * 시즌 등록
	 * @param vo
	 * @throws Exception
	 */
	public void insertProc(SeasonVo vo) throws Exception;
	
	/**
	 * 시즌 상세조회 (부모)
	 * @param season_id
	 * @return
	 * @throws Exception
	 */
	public SeasonVo getSeasonDetail(String season_id) throws Exception;
	
	/**
	 * 시즌 상세조회 (자식)
	 * @param parent_season_id
	 * @return
	 * @throws Exception
	 */
	public String getSeasonDetailList(String parent_season_id) throws Exception;
	
	/**
	 * 시즌 수정
	 * @param vo
	 * @throws Exception
	 */
	public void updateProc(SeasonVo vo) throws Exception;
	
	/**
	 * 시즌 삭제
	 * @param delList
	 * @throws Exception
	 */
	public void deleteProc(String delList, String app_tp) throws Exception;
	
	/**
	 * 시즌적용 카테고리 목록 조회
	 * @param albumId
	 * @return
	 * @throws Exception
	 */
	public String getCategoryList(String albumId, String series_yn, String category_gb) throws Exception;
	
	/**
	 * 시즌 제외카테고리 조회
	 * @return
	 * @throws Exception
	 */
	public String getExceptList() throws Exception;
	
	/**
	 * 시즌 제외카테고리 등록/삭제 
	 * @param addList
	 * @throws Exception
	 */
	public void exceptProc(String addList) throws Exception;
	
	/**
	 * 시즌 캐쉬 반영일시 수정
	 * 
	 * @throws Exception
	 */
	public void updateCacheTime(String seasonId) throws Exception;
	
}
