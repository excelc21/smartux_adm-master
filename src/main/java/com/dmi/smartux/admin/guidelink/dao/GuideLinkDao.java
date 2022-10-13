package com.dmi.smartux.admin.guidelink.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dmi.smartux.admin.guidelink.vo.GuideLinkVo;
import com.dmi.smartux.common.dao.CommonIptvDao;

@Repository
public class GuideLinkDao extends CommonIptvDao {

	/**
	 * 가이드채널 목록 조회
	 * @return				채널목록
	 * @throws Exception
	 */
	public List<GuideLinkVo> getGuideLinkList(GuideLinkVo vo) {
		
		vo.setStart_rnum(vo.getPageNum() * vo.getPageSize() - vo.getPageSize() + 1);
		vo.setEnd_rnum(vo.getStart_rnum() + vo.getPageSize() - 1);
		
		return getSqlMapClientTemplate().queryForList("admin_guidelink.getGuideLinkList", vo);
	}

	/**
	 * DCA 중복 체크
	 * @return				
	 * @throws Exception
	 */
	public int duplicateDca(GuideLinkVo guideLinkVo) {
		return (Integer) getSqlMapClientTemplate().queryForObject("admin_guidelink.duplicateDca", guideLinkVo);
	}

	/**
	 * 가이드채널 등록
	 * @return				
	 * @throws Exception
	 */
	public void insertGuideLink(GuideLinkVo guideLinkVo) {
		getSqlMapClientTemplate().insert("admin_guidelink.insertGuideLink", guideLinkVo);
	}

	/**
	 * 가이드채널 상세조회
	 * @return				
	 * @throws Exception
	 */
	public GuideLinkVo getGuideLinkDetail(String seq) {
		return (GuideLinkVo) getSqlMapClientTemplate().queryForObject("admin_guidelink.getGuideLinkDetail", seq);
	}

	/**
	 * 가이드채널 수정
	 * @return				
	 * @throws Exception
	 */
	public void updateGuideLink(GuideLinkVo guideLinkVo) {
		getSqlMapClientTemplate().update("admin_guidelink.updateGuideLink", guideLinkVo);
	}

	/**
	 * 가이드채널 삭제
	 * @return				
	 * @throws Exception
	 */
	public void deleteGuideLink(String seq) {
		getSqlMapClientTemplate().delete("admin_guidelink.deleteGuideLink", seq);
	}

	/**
	 * 가이드채널 카운트조회
	 * @return				
	 * @throws Exception
	 */
	public int getGuideLinkListCnt(GuideLinkVo param) {
		return (Integer) getSqlMapClientTemplate().queryForObject("admin_guidelink.getGuideLinkListCnt", param);
	}
}
