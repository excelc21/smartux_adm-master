package com.dmi.smartux.admin.noti.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dmi.smartux.admin.noti.vo.CopyNotiPopupVo;
import com.dmi.smartux.admin.noti.vo.NotiListVO;
import com.dmi.smartux.admin.noti.vo.NotiPopVo;
import com.dmi.smartux.admin.noti.vo.NotiVO;

/**
 * 
 * 관리자 공지사항 서비스 인터페이스
 */

public interface NotiViewService {
	
	/**
	 * 공지사항 리스트 조회 기능
	 * @param notiListVO
	 * @return
	 * @throws Exception
	 */
	public List<NotiVO> getNotiList(NotiListVO notiListVO) throws Exception;
	
	/**
	 *공지사항 리스트 갯수 조회 		
	 * @param notiListVO
	 * @return
	 * @throws Exception
	 */
	public int getNotiListCnt(NotiListVO notiListVO) throws Exception;
	
	/**
	 * 공지사항 등록
	 * @param notiVO
	 * @throws Exception
	 */	
	public void createNoti(NotiVO notiVO) throws Exception;
	
	/**
	 * 공지사항 등록화면
	 * @param reg_no
	 * @return
	 * @throws Exception
	 */
	public  NotiVO getNotiView(String reg_no) throws Exception;  
	
	
	/**
	 *  공지사항 삭제
	 * @param reg_no
	 * @param cookieID
	 * @throws Exception
	 */
	public void deleteNoti(String reg_no,String cookieID,String ip) throws Exception;
	
	/**
	 * 공지 사항 수정 	
	 * @param notiVO
	 * @throws Exception
	 */
	public void updateNoti(NotiVO notiVO) throws Exception;
	
	/**
	 * 단말 정보 리스트
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>>  getTerm(Map<String, String> param) throws Exception;
	
	/**
	 * 게시글 단말 리스트 서비스
	 * @param reg_no
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>>  getNotiTerminal(String reg_no) throws Exception;
	
	/**
	 * 
	 * 게시판 종류 리스트
	 * @param bbs_gbn
	 * @return List<Map<String , String >>
	 * @throws Exception
	 */
	
	public List<Map<String , String >> getBoardList(NotiListVO notiListVO) throws Exception;
	
	/**
	 *  팝업 설정 /해지
	 * @param reg_no
	 * @param bbs_id
	 * @param is_fixed
	 * @throws Exception
	 */
	public void popUpSetting(String reg_no,String bbs_id,String is_fixed,String cookieID,String ip) throws Exception;
	

	/**
	 * 상단 게시물 갯수
	 * @param bbs_id
	 * @return
	 * @throws Exception
	 */
	public  Integer getIsFixedCount(String bbs_id) throws Exception;
		
	/**
	 * 상단 여부 설정/해지	
	 * @param notiVO
	 * @throws Exception
	 */
	public  void updateIsFixed(NotiVO notiVO,String cookieID,String ip) throws Exception;
	
	/**
	 * 검수 확인
	 * @param notiVO
	 * @throws Exception
	 */
	public  void updateConfirmAdt(String reg_no,String is_adt, String bbs_id,String cookieID,String ip, String bbs_gbn) throws Exception;
	
	/**
	 * 단말기 등록
	 * @param param
	 * @throws Exception
	 */
	public void createTermManage(Map<String, String> param , String id , String ip) throws Exception;
	
	
	/**
	 * 단말기 삭제
	 * @param modelList
	 * @param del_yn
	 * @throws Exception
	 */
	public void deleteTermManage(String modelList,String del_yn,String id , String ip, String scr_tp) throws Exception;
	
	/**
	 * 단말모델 등록여부
	 * @param bbs_id
	 * @return
	 * @throws Exception
	 */
	public  Integer getIsExistModel(String term_model, String scr_tp) throws Exception;
	

	/**
	 * 팝업공지 가져오기(팝업공지는 무조건 하나임)
	 * @return
	 * @throws Exception
	 */
	public String getNotiPopupId(String scr_tp) throws Exception;
	
	/**
	 * 게시판 팝업공지로 복사
	 * @param copynotipopupVo
	 * @throws Exception
	 */
	public void copyNotiPopup(CopyNotiPopupVo copynotipopupVo) throws Exception;
	
	/**
	 * 팝업 리스트 조회 기능
	 * @param notiListVO
	 * @return
	 * @throws Exception
	 */
	public List<NotiPopVo> getNotiPopList(NotiListVO notiListVO) throws Exception;
	
	/**
	 *팝업 리스트 갯수 조회 		
	 * @param notiListVO
	 * @return
	 * @throws Exception
	 */
	public int getNotiPopListCnt(NotiListVO notiListVO) throws Exception;
}