package com.dmi.smartux.admin.notimng.service;

import java.util.List;
import java.util.Map;

import com.dmi.smartux.admin.notimng.vo.NotiMngViewVO;

public interface NotiMngViewService {

	// DB의 HDTV_BBS_MASTER 로 부터 게시판 정보를 가져온다.
	public List<Map<String, String>> getNotiMngViewList ( String bbs_Gbn, String scr_Type ) throws Exception;

	// DB의 HDTV_BBS_MASTER 에 update 작업을 수행 한다.
	public void setNotiMngUpdate ( NotiMngViewVO vo ) throws Exception;

	// DB의 HDTV_BBS_MASTER 에 insert 작업을 수행 한다.
	public String setNotiMngInsert ( NotiMngViewVO vo ) throws Exception;

	// DB의 HDTV_BBS_MASTER 에 delete 작업을 수행 한다.
	public void NotiMngDelete ( NotiMngViewVO vo ) throws Exception;

}
