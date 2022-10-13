package com.dmi.smartux.admin.guidelink.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmi.smartux.admin.guidelink.dao.GuideLinkDao;
import com.dmi.smartux.admin.guidelink.service.GuideLinkService;
import com.dmi.smartux.admin.guidelink.vo.GuideLinkVo;
import com.dmi.smartux.cache.service.CacheIntervalService;
import com.dmi.smartux.common.service.CacheService;

@Service
public class GuideLinkServiceImpl implements GuideLinkService {

	@Autowired
	GuideLinkDao dao;
	
	@Autowired
	CacheService cacheservice;
	
	@Autowired
	CacheIntervalService cacheIntervalService;

	@Override
	public List<GuideLinkVo> getGuideLinkList(GuideLinkVo param) {
		return dao.getGuideLinkList(param);
	}

	@Override
	public String insertGuideLink(GuideLinkVo guideLinkVo) {
		String result_code = "0000";
		int cnt = dao.duplicateDca(guideLinkVo);
		if(cnt > 0) {
			result_code = "5555";
		}else {
			dao.insertGuideLink(guideLinkVo);
		}
	
		return result_code;
	}

	@Override
	public GuideLinkVo getGuideLinkDetail(String seq) {
		return dao.getGuideLinkDetail(seq);
	}

	@Override
	public String updateGuideLink(GuideLinkVo guideLinkVo) {
		String result_code = "0000";
		dao.updateGuideLink(guideLinkVo);
	
		return result_code;
	}

	@Override
	public String deleteGuideLink(GuideLinkVo guideLinkVo) {
		String result_code = "0000";
		
		String delList = guideLinkVo.getDelList();
		String del_arr[] = delList.split(",");
		for(int i = 0 ; i < del_arr.length; i++) {
			dao.deleteGuideLink(del_arr[i]);
		}
		
		return result_code;
		
	}

	@Override
	public int getGuideLinkListCnt(GuideLinkVo param) {
		return dao.getGuideLinkListCnt(param);
	}
}
