package com.dmi.smartux.admin.abtest.service;

import java.util.List;

import com.dmi.smartux.admin.abtest.vo.BPASListVo;
import com.dmi.smartux.admin.abtest.vo.BPASSearchVo;
import com.dmi.smartux.admin.abtest.vo.CategoryListVo;
import com.dmi.smartux.admin.abtest.vo.PanelListVo;
import com.dmi.smartux.admin.abtest.vo.TestListVo;

/**
 * 2021.04.16 BPAS 편성관리
 * BPAS 편성관리 Service
 * @author medialog
 *
 */
public interface BPASService {
	
	/**
	 * 2021.04.16 BPAS편성관리 목록
	 * ABMS 호출
	 * @param searchVo
	 * @return
	 * @throws Exception
	 */
	public List<BPASListVo> getBPASList(BPASSearchVo searchVo) throws Exception;
	
	/**
	 * 2021.04.16 BPAS편성관리 목록 카운트
	 * AB테스트 목록 조회
	 * @param abmsList
	 * @return
	 * @throws Exception
	 */
	public int getBPASListCount (BPASSearchVo searchVo) throws Exception;

	/**
	 * 2021.04.16 카테고리 리스트
	 * @param category_id 
	 * @param abmsList
	 * @return
	 * @throws Exception
	 */
	public List<CategoryListVo> getCategoryList(String imcs_id);

	/**
	 * 2021.04.16 카테고리id 리스트
	 * @param category_id 
	 * @param abmsList
	 * @return
	 * @throws Exception
	 */
	public List<CategoryListVo> getCategoryIdList(String imcs_id);

	/**
	 * 2021.04.16 지면패널조회
	 * @param category_id 
	 * @param abmsList
	 * @return
	 * @throws Exception
	 */
	public List<PanelListVo> getPanelList(String category_id);

	/**
	 * 2021.04.16 지면패널조회
	 * @param category_id 
	 * @param abmsList
	 * @return
	 * @throws Exception
	 */
	public List<PanelListVo> getPanelList2(PanelListVo param);

	/**
	 * 2021.04.16 지면패널조회
	 * @param category_id 
	 * @param abmsList
	 * @return
	 * @throws Exception
	 */
	public List<PanelListVo> getPanelList3(String ads_no);

	/**
	 * 2021.04.16 지면패널조회
	 * @param category_id 
	 * @param abmsList
	 * @return
	 * @throws Exception
	 */
	public List<PanelListVo> getPanelList4(String content_id);

	/**
	 * 2021.04.16 테스트정보조회
	 * @param category_id 
	 * @param abmsList
	 * @return
	 * @throws Exception
	 */
	public List<TestListVo> getTestIdInfo(BPASSearchVo vo);

	/**
	 * 2021.04.16 테스트정보조회
	 * @param category_id 
	 * @param abmsList
	 * @return
	 * @throws Exception
	 */
	public List<TestListVo> getTestIdInfo2(BPASSearchVo vo);
	
}
