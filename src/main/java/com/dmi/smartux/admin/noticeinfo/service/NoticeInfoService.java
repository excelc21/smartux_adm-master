package com.dmi.smartux.admin.noticeinfo.service;

import java.util.HashMap;
import java.util.List;

import com.dmi.smartux.admin.noticeinfo.vo.InsertNoticeInfoVo;
import com.dmi.smartux.admin.noticeinfo.vo.NotiCodeListVo;
import com.dmi.smartux.admin.noticeinfo.vo.NotiMainCodeListVo;
import com.dmi.smartux.admin.noticeinfo.vo.NoticeInfoListProcVo;
import com.dmi.smartux.admin.noticeinfo.vo.NoticeInfoListVo;
import com.dmi.smartux.admin.noticeinfo.vo.UpdateNoticeInfoVo;
import com.dmi.smartux.admin.noticeinfo.vo.ViewNoticeInfoListVo;

public interface NoticeInfoService {
	
	/**
	 * NoticeInfo 공지 리스트
	 * @param noticeinfolistprocVo
	 * @return
	 * @throws Exception
	 */
	public List<NoticeInfoListVo> getNoticeInfoList(NoticeInfoListProcVo noticeinfolistprocVo) throws Exception;
	
	/**
	 * NoticeInfo 공지 리스트 총 갯수
	 * @param noticeinfolistprocVo
	 * @return
	 * @throws Exception
	 */
	public int getNoticeInfoListTotalCnt(NoticeInfoListProcVo noticeinfolistprocVo) throws Exception;
	
	/**
	 * NoticeInfo 장르 코드 목록
	 * @return
	 * @throws Exception
	 */
	public List<NotiCodeListVo> getNoticeInfoCodeList() throws Exception;
	
	/**
	 * NoticeInfo 메인 코드 목록
	 * @return
	 * @throws Exception
	 */
	public List<NotiMainCodeListVo> getNotiMainCodeList() throws Exception;
	
	/**
	 * NoticeInfo 저장
	 * @param insertnoticeinfoVo
	 * @throws Exception
	 */
	public void insertNoticeInfo(InsertNoticeInfoVo insertnoticeinfoVo) throws Exception;

	/**
	 * NoticeInfo 수정
	 * @param updatenoticeinfoVo
	 * @throws Exception
	 */
	public void updateNoticeInfo(UpdateNoticeInfoVo updatenoticeinfoVo) throws Exception;
	
	/**
	 * NoticeInfo 삭제
	 * @param seq
	 * @throws Exception
	 */
	public void deleteNoticeInfo(String seq) throws Exception;
	
	/**
	 * 프리뷰 보기
	 * @param service
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, List<ViewNoticeInfoListVo>> viewNoticeInfoList(String service) throws Exception;
}
