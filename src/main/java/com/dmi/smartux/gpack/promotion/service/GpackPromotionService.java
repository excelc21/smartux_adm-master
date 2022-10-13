package com.dmi.smartux.gpack.promotion.service;

import java.util.List;

import com.dmi.smartux.gpack.promotion.vo.GpackPromotionInfoVO;
import com.dmi.smartux.mainpanel.vo.AlbumInfoVO;

public interface GpackPromotionService {
	
	/**
	 * 프로모션 개수를 조회
	 * @param request			HttpServletRequest 객체
	 * @param response			HttpServletResponse 객체
	 * @param sa_id				가입번호
	 * @param stb_mac			맥주소
	 * @param app_type			어플타입
	 * @param pack_id			팩ID
	 * @param promotion_id		프로모션ID
	 * @param start_num			검색 시작 인덱스
	 * 								-1  : req_count 값을 무시하고 전체를 검색
	 * 								0 : req_count 값은 무시하며 레코드의 총갯수의 정보만 전달
	 * 								양수 : req_count 값을 확인하여 페이징 된 레코드만큼 정보 전달
	 * @param req_count			검색 레코드 갯수
	 * @param p_start_num		프로모션 시작 NUMBER	
	 * @param p_req_count		가져올 프로모션 개수(행의 개수)
	 * @param callByScheduler	단말에서 호출 했을 경우엔 N, 스케쥴러에서 호출했을땐 Y, 관리자툴에서 조회했을땐 A가 들어간다
	 * @return					프로모션 개수
	 * @throws Exception
	 */
	public int getPackPromotionCount(String sa_id, String stb_mac, String app_type, String pack_id, String promotion_id, String start_num, String req_count, String p_start_num, String p_req_count, String callByScheduler) throws Exception;
	
	/**
	 * 프로모션 조회
	 * @param request			HttpServletRequest 객체
	 * @param response			HttpServletResponse 객체
	 * @param sa_id				가입번호
	 * @param stb_mac			맥주소
	 * @param app_type			어플타입
	 * @param pack_id			팩ID
	 * @param promotion_id		프로모션ID
	 * @param start_num			검색 시작 인덱스
	 * 								-1  : req_count 값을 무시하고 전체를 검색
	 * 								0 : req_count 값은 무시하며 레코드의 총갯수의 정보만 전달
	 * 								양수 : req_count 값을 확인하여 페이징 된 레코드만큼 정보 전달
	 * @param req_count			검색 레코드 갯수
	 * @param p_start_num		프로모션 시작 NUMBER	
	 * @param p_req_count		가져올 프로모션 개수(행의 개수)
	 * @param fh_gbn			FULL HD 구분
	 * @param callByScheduler	단말에서 호출 했을 경우엔 N, 스케쥴러에서 호출했을땐 Y, 관리자툴에서 조회했을땐 A가 들어간다
	 * @return					프로모션 정보
	 * @throws Exception
	 */
	public List<GpackPromotionInfoVO> getPackPromotion(String sa_id, String stb_mac, String app_type, String pack_id, String promotion_id, String start_num, String req_count, String p_start_num, String p_req_count, int fh_gbn, String callByScheduler) throws Exception;
	
}
