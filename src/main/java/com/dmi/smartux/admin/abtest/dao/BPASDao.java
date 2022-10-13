package com.dmi.smartux.admin.abtest.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dmi.smartux.admin.abtest.vo.BPASListVo;
import com.dmi.smartux.admin.abtest.vo.BPASSearchVo;
import com.dmi.smartux.admin.abtest.vo.CategoryListVo;
import com.dmi.smartux.admin.abtest.vo.PanelListVo;
import com.dmi.smartux.admin.abtest.vo.TestListVo;
import com.dmi.smartux.common.dao.CommonDao;
import com.dmi.smartux.common.dao.CommonIptvDao;


/**
 * 2021.04.16 AB테스트 MIMS
 * AB 테스트 지면 Dao
 * @author medialog
 *
 */
@Repository
public class BPASDao extends CommonIptvDao{
	
	/**
	 * 2021.04.16 BPAS 편성관리
	 * AB테스트 목록 조회
	 * @param variation_id
	 * @return
	 */
	public List<BPASListVo> getBPASList (BPASSearchVo param) {
		return (List<BPASListVo>) getSqlMapClientTemplate().queryForList("bpas.getBPASList", param);
	}
	
	/**
	 * 2021.04.16 BPAS 편성관리
	 * AB테스트 목록 카운트
	 * @param variation_id
	 * @return
	 */
	public int getBPASListCount (BPASSearchVo param) {
		return (Integer) getSqlMapClientTemplate().queryForObject("bpas.getBPASListCount", param);
	}

	public List<CategoryListVo> getCategoryList(String category_id) {
		return getSqlMapClientTemplate().queryForList("bpas.getCategoryList", category_id);
	}

	public List<CategoryListVo> getCategoryIdList(String imcs_id) {
		return getSqlMapClientTemplate().queryForList("bpas.getCategoryIdList", imcs_id);
	}

	public List<PanelListVo> getPanelList(String category_id) {
		return getSqlMapClientTemplate().queryForList("bpas.getPanelList", category_id);
	}

	public List<PanelListVo> getPanelList2(PanelListVo param) {
		return getSqlMapClientTemplate().queryForList("bpas.getPanelList2", param);
	}

	public List<PanelListVo> getPanelList3(String ads_no) {
		return getSqlMapClientTemplate().queryForList("bpas.getPanelList3", ads_no);
	}

	public List<PanelListVo> getPanelList4(String content_id) {
		return getSqlMapClientTemplate().queryForList("bpas.getPanelList4", content_id);
	}

	public List<TestListVo> getTestIdInfo(BPASSearchVo vo) {
		return getSqlMapClientTemplate().queryForList("bpas.getTestIdInfo", vo);
	}

	public List<TestListVo> getTestIdInfo2(BPASSearchVo vo) {
		return getSqlMapClientTemplate().queryForList("bpas.getTestIdInfo2", vo);
	}
	
}
