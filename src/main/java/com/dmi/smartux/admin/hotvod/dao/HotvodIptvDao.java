package com.dmi.smartux.admin.hotvod.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dmi.smartux.common.dao.CommonDao;
import com.dmi.smartux.admin.hotvod.vo.HotvodContentVO;
import com.dmi.smartux.admin.hotvod.vo.HotvodExcelVo;
import com.dmi.smartux.admin.hotvod.vo.HotvodFileUploadVO;
import com.dmi.smartux.admin.hotvod.vo.HotvodHitstatsVO;
import com.dmi.smartux.admin.hotvod.vo.HotvodSearchVO;
import com.dmi.smartux.admin.hotvod.vo.HotvodSiteVO;

/**
 * 화제동영상 - DAO
 * @author JKJ
 */
@Repository
public class HotvodIptvDao extends CommonDao {
	
	/**
	 * 사이트 목록
	 * @param hotvodSearchVO
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<HotvodSiteVO> siteList(HotvodSearchVO hotvodSearchVO){
		hotvodSearchVO.setStart_rnum(hotvodSearchVO.getPageNum()*hotvodSearchVO.getPageSize()-hotvodSearchVO.getPageSize()+1);
		hotvodSearchVO.setEnd_rnum(hotvodSearchVO.getStart_rnum()+hotvodSearchVO.getPageSize()-1);
		return getSqlMapClientTemplate().queryForList("hotvod_iptv.siteList", hotvodSearchVO);
	}
	
	/**
	 * 사이트 목록갯수
	 * @param hotvodSearchVO
	 * @return
	 */
	public int siteListCnt(HotvodSearchVO hotvodSearchVO){
		return (Integer)getSqlMapClientTemplate().queryForObject("hotvod_iptv.siteListCnt", hotvodSearchVO);
	}
	
	/**
	 * 사이트 아이디 신규 채번
	 * @return
	 */
	public String getSiteId(){
		return (String)getSqlMapClientTemplate().queryForObject("hotvod_iptv.getSiteId", null);
	}
	
	/**
	 * 사이트 상세/수정
	 * @param hotvodSearchVO
	 * @return
	 */
	public HotvodSiteVO siteDetail(HotvodSearchVO hotvodSearchVO){
		return (HotvodSiteVO)getSqlMapClientTemplate().queryForObject("hotvod_iptv.siteDetail", hotvodSearchVO);
	}
	
	/**
	 * 사이트 수정
	 * @param hotvodSiteVO
	 */
	public void siteUpdate(HotvodSiteVO hotvodSiteVO){
		getSqlMapClientTemplate().update("hotvod_iptv.siteUpdate", hotvodSiteVO);
	}
	
	/**
	 * 사이트 등록
	 * @param hotvodSiteVO
	 */
	public void siteInsert(HotvodSiteVO hotvodSiteVO){
		getSqlMapClientTemplate().insert("hotvod_iptv.siteInsert", hotvodSiteVO);
	}
	
	/**
	 * 사이트 아이디 중복검사
	 * @param site_id
	 * @return
	 */
	public int siteIdChk(String site_id){
		return (Integer)getSqlMapClientTemplate().queryForObject("hotvod_iptv.siteIdChk", site_id);
	}
	
	/**
	 * 사이트 삭제
	 * @param site_id
	 */
	public void siteDelete(String site_id){
		getSqlMapClientTemplate().update("hotvod_iptv.siteDelete", site_id);
	}
	
	/**
	 * 사이트 목록 (콤보박스)
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<HotvodSiteVO> getSiteList(){
		return getSqlMapClientTemplate().queryForList("hotvod_iptv.getSiteList", null);
	}
	
	/**
	 * 조회수 통계 목록
	 * @param hotvodSearchVO
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<HotvodHitstatsVO> getHitstatsList(HotvodSearchVO hotvodSearchVO){
		return getSqlMapClientTemplate().queryForList("hotvod_iptv.getHitstatsList", hotvodSearchVO);
	}	
}
