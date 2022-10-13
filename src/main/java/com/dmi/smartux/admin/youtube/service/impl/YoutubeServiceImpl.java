package com.dmi.smartux.admin.youtube.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmi.smartux.admin.youtube.dao.YoutubeDao;
import com.dmi.smartux.admin.youtube.service.YoutubeService;
import com.dmi.smartux.admin.youtube.vo.YoutubeVO;

@Service
public class YoutubeServiceImpl implements YoutubeService{

	@Autowired
	YoutubeDao dao;
	
	@Override
	public List<YoutubeVO> getYoutubeList() throws Exception {
		

		List<YoutubeVO> result = null;
		
		result = dao.getYoutubeList();
		
		return result;
	}

	@Override
	@Transactional
	public void setYoutubeInsert(String category, String recommend_text, String write_id) throws Exception {
		dao.setYoutubeInsert(category,recommend_text,write_id);
	}

	@Override
	@Transactional
	public void setYoutubeDelete(String[] codes) throws Exception {
		for(String code : codes){
			dao.setYoutubeDelete(code);
		}
	}

	@Override
	@Transactional
	public void setUseUpdate(String code,String use_yn) throws Exception {
		dao.setAllUseUpdate("N");
		
		if(use_yn.equals("Y")){
			dao.setUseUpdate(code);
		}
	}

	@Override
	public YoutubeVO getYoutubeData(String code) throws Exception {
		
		YoutubeVO vo =  dao.getYoutubeData(code);
		
		return vo;
	}

	@Override
	@Transactional
	public void setYoutubeUpdate(String code,String category, String recommend_text, String write_id) throws Exception {
		dao.setYoutubeUpdate(code, category, recommend_text, write_id);
	}

	
}
