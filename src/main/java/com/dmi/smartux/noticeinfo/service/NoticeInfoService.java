package com.dmi.smartux.noticeinfo.service;

import java.util.HashMap;
import java.util.List;

import com.dmi.smartux.noticeinfo.vo.CacheNoticeInfoListVo;

public interface NoticeInfoService {
	
	/**
	 * NoticeInfo 캐쉬에 담기 위해 조회
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, List<CacheNoticeInfoListVo>> cacheNoticeInfoList() throws Exception;
	
	/**
	 * 상용 프리뷰 보기
	 * @param service
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, List<CacheNoticeInfoListVo>> viewNoticeInfoListReal(String service) throws Exception;
	
	/**
	 * NoticeInfo 캐쉬 조회/등록
	 * @param service
	 * @param ntype
	 * @param category
	 * @param callByScheduler
	 * @return
	 * @throws Exception
	 */
	public List<CacheNoticeInfoListVo> getNoticeInfoApi(String service,String ntype,String category,String callByScheduler) throws Exception;

}
