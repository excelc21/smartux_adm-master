package com.dmi.smartux.admin.hotvod.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dmi.smartux.admin.hotvod.vo.HotvodContentVO;
import com.dmi.smartux.admin.hotvod.vo.HotvodExcelVo;
import com.dmi.smartux.admin.hotvod.vo.HotvodFilteringSiteVo;
import com.dmi.smartux.admin.hotvod.vo.HotvodSearchVO;

/**
 * 화제동영상 - Service
 * @author JKJ
 */
public interface HotvodService {

	/**
	 * 컨텐츠 목록
	 * @param hotvodSearchVO
	 * @return
	 * @throws Exception
	 */
	public List<HotvodContentVO> contentList(HotvodSearchVO hotvodSearchVO) throws Exception;
	
	/**
	 * 컨텐츠 목록갯수
	 * @param hotvodSearchVO
	 * @return
	 * @throws Exception
	 */
	public int contentListCnt(HotvodSearchVO hotvodSearchVO) throws Exception;

	/**
	 * 카테고리 상세/수정
	 * @param hotvodSearchVO
	 * @return
	 * @throws Exception
	 */
	public HotvodContentVO categoryDetail(HotvodSearchVO hotvodSearchVO) throws Exception;
	
	/**
	 * 컨텐츠 상세/수정
	 * @param hotvodSearchVO
	 * @return
	 * @throws Exception
	 */
	public HotvodContentVO contentDetail(HotvodSearchVO hotvodSearchVO) throws Exception;
	
	/**
	 * 컨텐츠 수정
	 * @param hotvodContentVO
	 * @throws Exception
	 */
	public void contentUpdate(HotvodContentVO hotvodContentVO) throws Exception;
	
	/**
	 * 카테고리 수정
	 * @param hotvodContentVO
	 * @throws Exception
	 */
	public void categoryUpdate(HotvodContentVO hotvodContentVO) throws Exception;	
	
	/**
	 * 컨텐츠 아이디 신규 채번
	 * @return
	 * @throws Exception
	 */
	public String getContentId() throws Exception;
	
	/**
	 * 컨텐츠 등록
	 * @param hotvodContentVO
	 * @throws Exception
	 */
	public void contentInsert(HotvodContentVO hotvodContentVO) throws Exception;
	
	/**
	 * 카테고리 등록
	 * @param hotvodContentVO
	 * @throws Exception
	 */
	public void categoryInsert(HotvodContentVO hotvodContentVO) throws Exception;	
	
	/**
	 * 컨텐츠 아이디 중복검사
	 * @param content_id
	 * @return
	 * @throws Exception
	 */
	public int contentIdChk(String content_id) throws Exception;
	
	/**
	 * 카테고리 아이디 삭제
	 * @param content_id, String mod_id
	 * @throws Exception
	 */
	public void categoryDelete(String content_id, String mod_id) throws Exception;	
	
	/**
	 * 컨텐츠 아이디 삭제
	 * @param content_id, String mod_id
	 * @throws Exception
	 */
	public void contentDelete(String content_id, String mod_id, String parent_id) throws Exception;
	
	/**
	 * 상세화면 카테고리-컨텐츠 아이디 삭제
	 * @param content_id, String mod_id
	 * @throws Exception
	 */
	public void contentDetailDelete(String content_id, String content_type, String mod_id) throws Exception;	
	
	/**
	 * 카테고리 목록 (팝업)
	 * @param content_type
	 * @return
	 * @throws Exception
	 */
	public List<HotvodContentVO> getCategoryList(String content_type) throws Exception;
	
	/**
	 * 순서바꾸기 목록 (팝업)
	 * @param content_id
	 * @return
	 * @throws Exception
	 */
	public List<HotvodContentVO> getChangeList(String content_id) throws Exception;
	
	/**
	 * 순서 바꾸기 저장
	 * @param content_id
	 * @throws Exception
	 */
	public void changeSave(List<String> orderList, List<String> parList, String id) throws Exception;
	
	/**
	 * 하위 목록 (팝업)
	 * @param hotvodSearchVO
	 * @return
	 * @throws Exception
	 */
	public List<HotvodContentVO> getSubList(HotvodSearchVO hotvodSearchVO) throws Exception;
	
	/**
	 * 하위 목록 갯수
	 * @param hotvodSearchVO
	 * @return
	 * @throws Exception
	 */
	public int getSubListCnt(HotvodSearchVO hotvodSearchVO) throws Exception;
	
	/**
	 * 카테고리, 앨범 정보 조회
	 * @param choiceData
	 * @return
	 * @throws Exception
	 */
	public HotvodContentVO getAlbumCateInfo(String[] choiceData) throws Exception;
	
	/**
	 * 구간점프 이미지 가져오기
	 * @param albumId, startTime, endTime
	 * @return
	 * @throws Exception
	 */
	public String getContentImg(String albumId, String startTime, String endTime) throws Exception;
	
	/**
	 * 상위카테고리의 노출여부 조회 
	 * @param hotvodContentVO
	 * @return
	 * @throws Exception
	 */
	public HotvodContentVO getParentContentDelYn(HotvodContentVO hotvodContentVO) throws Exception;
	
	/**
	 * 대박영상 통합검색 파일업로드
	 * @throws Exception
	 */
	public void hotvodFileUpload() throws Exception;
	
	public HashMap<String,Boolean> getBadgeDataInsertYn(String content_id,String type) throws Exception;
	
	/**
	 * 화제동영상 엑셀다운로드 
	 * @param hotvodContentVO
	 * @return
	 * @throws Exception
	 */
	public List<HotvodExcelVo> getExcelCategory(HotvodSearchVO hotvodSearchVo) throws Exception;
	
	/**
	 * 컨텐츠 복사존재여부 체크
	 * @param hotvodContentVO
	 * @return
	 * @throws Exception
	 */
	public int contentCopyChk(String parent_id, String content_id) throws Exception;
	
	/**
	 * 컨텐츠 복사 
	 * @param hotvodContentVO
	 * @return
	 * @throws Exception
	 */
	public void contentCopy(String parent_id, String content_id) throws Exception;
	
	/**
	 * <pre>
	 * 배지 자리수 확인 
	 * </pre>
	 * @author medialog
	 * @date 2017. 9. 20.
	 * @method toBinaryString
	 * @return String
	 */
	public String toBinaryString(String value);
	
	/**
	 * 카테고리 변경
	 * @param hotvodContentVO
	 * @throws Exception
	 */
	public void parentCateUpdate(HotvodContentVO hotvodContentVO) throws Exception;

	/**
	 * 카테고리 정보조회
	 * @param cate_no
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Map getCateInfo(String cate_no);
	
	/**
	 * 필터링사이트 목록
	 * @param HotvodFilteringSiteVo
	 * @return
	 * @throws Exception
	 */
	public List<HotvodFilteringSiteVo> getFilteringSiteList(HotvodFilteringSiteVo filtSiteVo) throws Exception;
}
