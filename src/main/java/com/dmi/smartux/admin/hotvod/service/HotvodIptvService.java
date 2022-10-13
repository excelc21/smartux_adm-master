package com.dmi.smartux.admin.hotvod.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.dmi.smartux.admin.hotvod.vo.HotvodHitstatsVO;
import com.dmi.smartux.admin.hotvod.vo.HotvodSearchVO;
import com.dmi.smartux.admin.hotvod.vo.HotvodSiteVO;
import com.dmi.smartux.common.vo.HotvodFilteringSiteVo;

/**
 * 화제동영상 - Service
 * @author JKJ
 */
public interface HotvodIptvService {
	/**
	 * 사이트 목록
	 * @param hotvodSearchVO
	 * @return
	 * @throws Exception
	 */
	public List<HotvodSiteVO> siteList(HotvodSearchVO hotvodSearchVO) throws Exception;
	
	/**
	 * 사이트 목록갯수
	 * @param hotvodSearchVO
	 * @throws Exception
	 * @return
	 */
	public int siteListCnt(HotvodSearchVO hotvodSearchVO) throws Exception;

	/**
	 * 사이트 아이디 신규 채번
	 * @return
	 * @throws Exception
	 */
	public String getSiteId() throws Exception;
	
	/**
	 * 사이트 상세/수정
	 * @param hotvodSearchVO
	 * @return
	 * @throws Exception
	 */
	public HotvodSiteVO siteDetail(HotvodSearchVO hotvodSearchVO) throws Exception;
	
	/**
	 * 사이트 수정
	 * @param hotvodSiteVO
	 * @throws Exception
	 */
	public void siteUpdate(HotvodSiteVO hotvodSiteVO) throws Exception;
	
	/**
	 * 사이트 등록
	 * @param hotvodSiteVO
	 * @throws Exception
	 */
	public void siteInsert(HotvodSiteVO hotvodSiteVO) throws Exception;
	
	/**
	 * 사이트 아이디 중복검사
	 * @param site_id
	 * @return
	 * @throws Exception
	 */
	public int siteIdChk(String site_id) throws Exception;
	
	/**
	 * 사이트 삭제
	 * @param site_id
	 * @throws Exception
	 */
	public void siteDelete(String site_id) throws Exception;
	
	/**
	 * 사이트 목록(콤보박스)
	 * @return
	 * @throws Exception
	 */
	public List<HotvodSiteVO> getSiteList() throws Exception;
	
	/**
	 * 조회수 통계 목록
	 * @param hotvodSearchVO
	 * @return
	 * @throws Exception
	 */
	public List<HotvodHitstatsVO> getHitstatsList(HotvodSearchVO hotvodSearchVO) throws Exception;
	
	/**
	 * 필터링사이트 목록
	 * @param HotvodFilteringSiteVo
	 * @return
	 * @throws Exception
	 */
	public List<HotvodFilteringSiteVo> getFilteringSiteList(HotvodFilteringSiteVo filtSiteVo) throws Exception;
	
	/**
	 * 필터링사이트 등록
	 * @param HotvodFilteringSiteVo
	 * @return
	 * @throws Exception
	 */
	public ResponseEntity<String> regFilteringSite(HotvodFilteringSiteVo filtSiteVo) throws Exception;
	
	/**
	 * 필터링사이트 수정
	 * @param HotvodFilteringSiteVo
	 * @return
	 * @throws Exception
	 */
	public ResponseEntity<String> modFilteringSite(HotvodFilteringSiteVo filtSiteVo) throws Exception;
	
	/**
	 * 필터링사이트 삭제
	 * @param HotvodFilteringSiteVo
	 * @return
	 * @throws Exception
	 */
	public ResponseEntity<String> delFilteringSite(HotvodFilteringSiteVo filtSiteVo) throws Exception;
}
