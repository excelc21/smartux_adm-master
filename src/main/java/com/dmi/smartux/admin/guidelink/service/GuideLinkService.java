package com.dmi.smartux.admin.guidelink.service;

import java.util.List;

import com.dmi.smartux.admin.guidelink.vo.GuideLinkVo;

public interface GuideLinkService {
	
	/**
	 * 가이드채널 목록 조회
	 * @return					채널목록
	 * @throws Exception
	 */
	public List<GuideLinkVo> getGuideLinkList(GuideLinkVo param);

	/**
	 * 등록
	 * @return					
	 * @throws Exception
	 */
	public String insertGuideLink(GuideLinkVo guideLinkVo);

	/**
	 * 가이드채널 상세조회
	 * @return					
	 * @throws Exception
	 */
	public GuideLinkVo getGuideLinkDetail(String seq);

	/**
	 * 가이드채널  수정
	 * @return					
	 * @throws Exception
	 */
	public String updateGuideLink(GuideLinkVo guideLinkVo);

	/**
	 * 가이드채널  삭제
	 * @return					
	 * @throws Exception
	 */
	public String deleteGuideLink(GuideLinkVo guideLinkVo);

	/**
	 * 가이드채널 카운트 조회
	 * @return					
	 * @throws Exception
	 */
	public int getGuideLinkListCnt(GuideLinkVo param);
}
