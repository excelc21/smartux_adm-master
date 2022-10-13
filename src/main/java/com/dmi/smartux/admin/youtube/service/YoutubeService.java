package com.dmi.smartux.admin.youtube.service;

import java.util.List;

import com.dmi.smartux.admin.youtube.vo.YoutubeVO;

public interface YoutubeService {

	
	public List<YoutubeVO> getYoutubeList() throws Exception;

	public void setYoutubeInsert(String category, String recommend_text, String write_id) throws Exception;

	public void setYoutubeDelete(String[] codes) throws Exception;

	public void setUseUpdate(String code,String use_yn) throws Exception;

	public YoutubeVO getYoutubeData(String code) throws Exception;

	public void setYoutubeUpdate(String code, String category, String recommend_text, String write_id) throws Exception;
	
}
