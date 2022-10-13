package com.dmi.smartux.admin.mainpanel.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dmi.smartux.admin.abtest.vo.ABTestSearchVO;
import com.dmi.smartux.admin.abtest.vo.ABTestVO;
import com.dmi.smartux.admin.mainpanel.vo.BubbleInsert;
import com.dmi.smartux.admin.mainpanel.vo.BubbleList;
import com.dmi.smartux.admin.mainpanel.vo.BubbleSearch;
import com.dmi.smartux.admin.mainpanel.vo.CategoryVO;
import com.dmi.smartux.admin.mainpanel.vo.FrameVO;
import com.dmi.smartux.admin.mainpanel.vo.LinkInfoVO;
import com.dmi.smartux.admin.mainpanel.vo.PanelSearchVo;
import com.dmi.smartux.admin.mainpanel.vo.PanelTitleTreamVO;
import com.dmi.smartux.admin.mainpanel.vo.PanelVO;
import com.dmi.smartux.admin.mainpanel.vo.PreviewVO;
import com.dmi.smartux.admin.mainpanel.vo.ViewVO;

public interface PanelViewService {
	/**
	 * 패널 ID 생성
	 * @return				조회된 패널 목록
	 * @throws Exception
	 */
	public PanelVO getPanelId() throws Exception;
	/**
	 * 패널 목록 조회
	 * @return				조회된 패널 목록
	 * @throws Exception
	 */
	public List<PanelVO> getPanelList() throws Exception;
	/**
	 * 패널 목록 조회
	 * @return				조회된 패널 목록
	 * @throws Exception
	 */
	public List<PanelVO> getPanelList(PanelSearchVo vo) throws Exception;
	
	/**
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public List<ABTestVO> getPanelListWithABTest(PanelSearchVo vo) throws Exception;
	
	/**
	 * 패널 상세 조회
	 * @param panel_id		상세 조회하고자 하는 panel_id
	 * @return				상세조회된 패널
	 * @throws Exception
	 */
	public PanelVO viewPanel(String panel_id) throws Exception;
	
	/**
	 * 입력받은 panel_id가 몇개가 있는지를 조회
	 * @param panel_id		갯수를 확인하고자 하는 panel_id
	 * @return				조회된 갯수
	 * @throws Exception
	 */
	public int getPanelidCnt(String panel_id) throws Exception;
	
	/**
	 * 입력받은 panel_nm이 몇개가 있는지를 조회
	 * @param panel_nm		갯수를 확인하고자 하는 panel_nm
	 * @return				조회된 갯수
	 * @throws Exception
	 */
	public int getPanelnmCnt(String panel_nm) throws Exception;
	
	/**
	 * 패널 등록
	 * @param panel_id			등록하고자 하는 패널의 panel_id
	 * @param panel_nm			등록하고자 하는 패널의 panel_nm
	 * @param use_yn			패널의 사용여부(Y/N)
	 * @param create_id			관리자 사이트에 로그인 한 사람의 로그인 id
	 * @param panel_ui_type		등록하고자 하는 패널의 ui_type
	 * @param panel_image	등록하고자 하는 패널의 이미지
	 * @throws Exception
	 */
	public void insertPanel(String panel_id, String panel_nm, String use_yn, String create_id, String panel_ui_type, String panel_image , String focus_type) throws Exception;
	
	/**
	 * 패널 수정
	 * @param panel_id			수정하고자 하는 패널의 기존 panel_id
	 * @param newPanel_id		수정하고자 하는 패널의 새로이 입력받은 panel_id
	 * @param panel_nm			수정하고자 하는 패널의 기존 panel_nm
	 * @param newPanel_nm		수정하고자 하는 패널의 새로이 입력받은 panel_nm
	 * @param use_yn			패널의 사용여부(Y/N)
	 * @param update_id			관리자 사이트에 로그인 한 사람의 로그인 id
	 * @param panel_ui_type		패널UI타입
	 * @param panel_image		패널 이미지
	 * @return					수정된 패널의 갯수
	 * @throws Exception
	 */
	public int updatePanel(String panel_id, String newPanel_id, String panel_nm, String newPanel_nm, String use_yn, String update_id, String panel_ui_type, String panel_image , String focus_type , String panel_image_nm) throws Exception;
	
	/**
	 * 패널 삭제
	 * @param panel_ids			삭제하고자 하는 패널들의 panel_id 배열
	 * @param update_id			관리자 사이트에 로그인 한 사람의 로그인 id
	 * @throws Exception
	 */
	public void deletePanel(String[] panel_ids, String update_id) throws Exception;
	
	
	/**
	 * 지면 목록 조회
	 * @param panel_id			조회하고자 하는 지면들이 속한 panel_id
	 * @return					조회된 지면 목록
	 * @throws Exception
	 */
	public List<ViewVO> getPanelTitleTempList(String panel_id) throws Exception;
	
	
	
	
	/**
	 * 하위 지면 목록 조회
	 * @param panel_id			조회하고자 하는 지면들이 속한 panel_id
	 * @return					조회된 지면 목록
	 * @throws Exception
	 */
	public List<ViewVO> getPanelTitleTempListSub(String panel_id , String p_title_id) throws Exception;
	
	
	/**
	 * 하위 지면 목록 조회
	 * @param panel_id			조회하고자 하는 지면들이 속한 panel_id
	 * @return					조회된 지면 목록
	 * @throws Exception
	 */
	public List<ViewVO> getPanelTitleTempListSubNull(String panel_id) throws Exception;
	
	
	
	/**
	 * 지면 상세 조회
	 * @param panel_id			조회하고자 하는 지면이 속한 panel_id
	 * @param title_id			조회하고자 하는 지면의 title_id
	 * @return					상세조회된 지면			
	 * @throws Exception
	 */
	public ViewVO viewPanelTitleTemp(String panel_id, String title_id) throws Exception;
	
	/**
	 * 지면 등록
	 * @param panel_id			등록하고자 하는 지면이 속한 panel_id
	 * @param title_nm			등록하고자 하는 지면의 title_nm
	 * @param title_color			등록하고자 하는 지면의 title_color
	 * @param p_title_id		등록하고자 하는 지면의 상위 지면 title_id
	 * @param use_yn			지면의 사용여부(Y/N)
	 * @param create_id			관리자 사이트에 로그인 한 사람의 로그인 id
	 * @param bg_img_file		지면 아이콘 이미지
	 * @param bg_img_file2		지면 아이콘 이미지2
	 * @throws Exception
	 */
	public void insertPanelTitleTemp(String panel_id, String title_nm, String title_color, String p_title_id, String use_yn, String create_id, String title_bg_img_file, String bg_img_file, String bg_img_file2 , String bg_img_file3) throws Exception;
	
	/**
	 * 지면 수정
	 * @param panel_id			수정하고자 하는 지면이 속한 패널의 panel_id
	 * @param title_id			수정하고자 하는 지면의 title_id
	 * @param title_nm			수정하고자 하는 지면의 title_nm
	 * @param title_nm			수정하고자 하는 지면의 title_color
	 * @param use_yn			지면의 사용여부(Y/N)	
	 * @param update_id			관리자 사이트에 로그인 한 사람의 로그인 id
	 * @param bg_img_file		지면 아이콘 이미지
	 * @param bg_img_file2		지면 아이콘 이미지2
	 * @return					수정된 지면의 갯수
	 * @throws Exception
	 */
	public int updatePanelTitleTemp(String panel_id, String title_id, String title_nm, String title_color, String use_yn, String update_id, String title_bg_img_file, String bg_img_file, String bg_img_file2, String bg_img_file3) throws Exception;
	
	/**
	 * 지면 삭제
	 * @param panel_id			삭제하고자 하는 지면들이 속한 panel_id
	 * @param title_id			삭제하고자 하는 지면의 title_id 배열
	 * @throws Exception
	 */
	public void deletePanelTitleTemp(String panel_id, String[] title_id) throws Exception;
	
	
	/**
	 * 지면 포커스
	 * @param panel_id			삭제하고자 하는 지면들이 속한 panel_id
	 * @param title_id			삭제하고자 하는 지면의 title_id 배열
	 * @throws Exception
	 */
	public void focusPanelTitleTemp(String panel_id, String[] title_id) throws Exception;
	
	
	/**
	 * 지면 이름 갯수 조회
	 * @param panel_id			지면 이름 갯수를 조회하기 위한 panel_id
	 * @param p_title_id		지면 이름 갯수를 조회하기 위한 지면이 속해 있는 상위 지면 title_id
	 * @param title_nm			지면 이름
	 * @return					조회된 지면 이름 갯수
	 * @throws Exception
	 */
	public int getPanelTitleTempTitlenmCnt(String panel_id, String p_title_id, String title_nm) throws Exception;
	
	/**
	 * 순서바꾸기 화면에서 사용되는 지면 목록 조회
	 * @param panel_id			지면 목록을 조회하기 위한 panel_id
	 * @param p_title_id		순서바꾸기의 대상이 되는 지면의 title_id(이 지면에 속한 하위 지면을 조회함으로써 하위 지면들의 순서를 바꾸게 된다)
	 * @return					순서바꾸기 화면에서 사용되는 지면 목록
	 * @throws Exception
	 */
	public List<ViewVO> changePanelTitleTempOrderList(String panel_id, String p_title_id) throws Exception;
	
	/**
	 * 지면의 순서바꾸기 작업
	 * @param panel_id			순서를 바꾸고자 하는 지면들의 panel_id
	 * @param title_ids			바뀌어지는 순서대로 들어가 있는 지면의 title_id 배열( 이 배열 안에 들어가 있는 순서대로 지면들의 순서가 셋팅된다)
	 * @param update_id			관리자 사이트에 로그인 한 사람의 로그인 id
	 * @throws Exception
	 */
	public void changePanelTitleTempOrderJob(String panel_id, String[] title_ids, String update_id) throws Exception;
	
	/**
	 * 카테고리 id를 입력받아 해당 카테고리에 속한 하위 카테고리 목록 조회
	 * @param category_id	카테고리 id
	 * @return				하위 카테고리 목록
	 * @throws Exception
	 */
	public List<CategoryVO> getCategoryList(String category_id) throws Exception;

	/**
	 * 카테고리 id를 입력받아 해당 카테고리에 속한 하위 카테고리 목록 조회
	 * @param category_gb		카테고리 구분(I20:IPTV / NSC:HDTV)
	 * @param category_id	카테고리 id
	 * @return				하위 카테고리 목록
	 * @throws Exception
	 */
	public List<CategoryVO> getCategoryList(String category_gb, String category_id) throws Exception;
	

	
	/**
	 * 지면의 상용 테이블 적용시에 하위 지면이 없으나 카테고리 코드가 등록이 되지 않은 지면을 조회
	 * @param panel_id		지면이 속한 패널의 panel_id
	 * @return				카테고리 코드가 셋팅이 안된 지면 목록
	 * @throws Exception
	 */
	public List<String> mustCategorySettingList(String panel_id) throws Exception;
	
	/**
	 * 패널에 등록된 임시 지면 정보와 상용 지면 정보를 비교하여 변경 된 지면 수 조회
	 * @param panel_id		지면의 상용 테이블에 등록하고자 하는 지면들이 속한 패널의 panel_id
	 * @return				지면의 사용 테이블과 임시 테이블의 비교 하여 변경 된 지면 수
	 * @throws Exception
	 */
	public int getPanelTitleChangeCount(String panel_id) throws Exception;
	
	/**
	 * 지면의 상용 테이블 등록
	 * @param panel_id		지면의 상용 테이블에 등록하고자 하는 지면들이 속한 패널의 panel_id
	 * @param create_id		관리자 사이트에 로그인 한 사람의 로그인 id
	 * @throws Exception
	 */
	public void insertPanelTitle(String panel_id, String create_id) throws Exception;
	
	/**
	 * 프리뷰 화면을 보여주기 위한 지면 목록 조회
	 * @param panel_id			프리뷰 화면을 보여주기 위한 지면들이 속한 패널의 panel_id
	 * @param separator			앨범 목록을 보여줄때 앨범과 앨범간의 연결 구분자
	 * @return					지면 목록
	 * @throws Exception
	 */
	public List<PreviewVO> previewPanelTitleTemp(String panel_id, String separator) throws Exception;
	
	/**
	 * 프리뷰 상용 화면을 보여주기 위한 지면 목록 조회
	 * @param panel_id			프리뷰(상용) 화면을 보여주기 위한 지면들이 속한 패널의 panel_id
	 * @param separator			앨범 목록을 보여줄때 앨범과 앨범간의 연결 구분자
	 * @return					지면 목록
	 * @throws Exception
	 */
	public List<PreviewVO> previewPanelTitle(String panel_id, String separator) throws Exception;
	
	/**
	 * 지면 데이터 설정 작업
	 * 20180913 이미지 관련 등록 및 삭제는 하위지면 추가와 지면 상세조회에서만 등록 및 수정
	 * @param panel_id			지면 데이터 설정작업을 하는 지면이 속한 패널의 panel_id
	 * @param title_id			지면 데이터 설정작업을 하는 지면의 title_id
	 * @param category_id		지면 데이터 설정작업을 하는 지면의 cateogry_id
	 * @param category_type		지면 데이터 설정작업을 하는 지면의 category_type
	 * @param album_cnt			지면 데이터 설정작업을 하는 지면의 album_cnt
	 * @param ui_type			지면 데이터 설정작업을 하는 지면의 UI 타입
	 * @param bg_img_file		지면 데이터 설정작업을 하는 지면의 배경 이미지 파일명
	 * @param description		지면 데이터 설정작업을 하는 지면의 지면 설명
	 * @param update_id			관리자 사이트에 로그인 한 사람의 로그인 id
	 * @param page_type
	 * @param page_code
	 * @param category_gb
	 * @param reps_album_id		지면 데이터 설정작업을 하는 지면의 대표컨텐츠 정보
	 * @param reps_category_id	지면 데이터 설정작업을 하는 지면의 대표컨텐츠 정보
	 * @param trailer_viewing_type
	 * @param reps_trailer_viewing_type	대표컨텐츠 예고편 노출타입
	 * @param bg_img_file2		지면 데이터 설정작업을 하는 지면의 아이콘 이미지2
	 * @param location_code		지면 데이터 설정작업을 하는 지면의 지역 정보(code)
	 * @param location_yn		지면 데이터 설정작업을 하는 지면의 국내 여부(국내: Y, 해외: N)
	 * @param product_code 
	 * @return
	 * @throws Exception
	 */
	public int updateCategory(String panel_id, String title_id, String category_id, String category_type, String album_cnt, String ui_type, String description, String update_id, String page_type, String page_code, String category_gb, 
			String reps_album_id, String reps_category_id, String trailer_viewing_type, String reps_trailer_viewing_type, String location_code, String location_yn
/* 2019.11.04 : 지면 UI 타입 [paper_ui_type 변수 추가] 추가 Start - 이태광 */					
			, String paper_ui_type, String product_code, String product_code_not, String show_cnt, String terminal_all_yn, String terminal_arr)
/* 2019.11.04 : 지면 UI 타입 [paper_ui_type 변수 추가] 추가 End - 이태광 */			
					throws Exception; // 카테고리 셋팅 신규버전
	
	/**
	 * 지면 데이터 설정 화면에서 카테고리 매핑으로 설정되어 있는 지면의 카테고리 id에 대한 정보를 조회
	 * @param category_code		조회하고자 하는 카테고리의 category_id 
	 * @return					카테고리 정보
	 * @throws Exception
	 */
	public CategoryVO getCategoryIdName(String category_code) throws Exception;	// 지면 데이터 설정 화면에서 카테고리 매핑을 선택되어있을때 해당 카테고리 코드에 대한 카테고리 정보를 조회한다

	/**
	 * 지면 데이터 설정 화면에서 카테고리 매핑으로 설정되어 있는 지면의 카테고리 id에 대한 정보를 조회
	 * @param category_gb 카테고리 구분 (I20 : IPTV / NSC : HDTV)
	 * @param category_code		조회하고자 하는 카테고리의 category_id
	 * @return					카테고리 정보
	 * @throws Exception
	 */
	public CategoryVO getCategoryIdName(String category_gb, String category_code) throws Exception;	// 지면 데이터 설정 화면에서 카테고리 매핑을 선택되어있을때 해당 카테고리 코드에 대한 카테고리 정보를 조회한다
	
	/**
	 * 지면 데이터 설정 화면 로딩시 지면 타입과 지면 설명을 조회
	 * @param panel_id		지면이 속한 패널의 panel_id
	 * @param title_id		지면의 title_id
	 * @return				지면 타입과 지면 설명이 들어가 있는 지면 정보
	 * @throws Exception
	 */
	public ViewVO getUITypeDescription(String panel_id, String title_id) throws Exception;
	
	/**
	 * 메인패널 버전 정보 조회
	 * @return 버전
	 * @throws Exception
	 */
	public String getPanelVersion() throws Exception;
	
	/**
	 * 메인패널 버전 업데이트
	 * @return
	 * @throws Exception
	 */
	public String updatePanelVersion(String type, String version, String create_id) throws Exception;
	
	/**
	 * 이미지서버 URL 조회
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public String getImageServerURL(String type) throws Exception;
	
	/**
	 * 앨범명 조회
	 * @param album_id
	 * @return
	 * @throws Exception
	 */
	public String getAlbumName(String album_id) throws Exception;
	
	/**
	 * 패널UI타입 조회
	 * @param frame_type	30: 패널UI타입
	 * @return
	 * @throws Exception
	 */
	public List<FrameVO> getPanelUiTypeList(String frame_type, int pageNum, int pageSize) throws Exception;
	
	/**
	 * 페이징 처리를 위한 전체 카운트 
	 * @return
	 * @throws Exception
	 */
	public int getPanelUiTypeCnt(String frame_type)throws Exception;
	
	/**
	 * frame insert
	 * @param frame_flag	frame 구분자(코드 입력 관련. P: 패널)
	 * @param frame_type	30: 패널UI타입
	 * @param frame_nm		프레임 이름
	 * @param img_file		파일 이름
	 * @param use_yn		사용 여부
	 * @param create_id		요청 ID
	 * @param data_type 
	 * @throws Exception
	 */
	public void insertPanelUiType(String frame_flag, String frame_type, String frame_nm, String img_file, String use_yn, String create_id) throws Exception;
	
	/**
	 * frames delete(여러개 삭제 flag로 변경)
	 * @param frame_type_code	프레임 코드(pk)
	 * @param update_id			요청 ID
	 * @return
	 * @throws Exception
	 */
	public StringBuffer deletePanelUiType(String[] frame_type_code, String update_id) throws Exception;
	
	/**
	 * frame update
	 * @param frame_type_code	프레임 코드(pk)
	 * @param frame_nm			프레임 이름
	 * @param img_file			파일 이름
	 * @param use_yn			사용 여부
	 * @param update_id			요청 ID
	 * @return
	 * @throws Exception
	 */
	public int updatePanelUiType(String frame_type_code, String frame_nm, String img_file, String use_yn, String update_id) throws Exception;

	/**
	 * 개별 frame 조회
	 * @param frame_type_code	프레임 코드(pk)
	 * @return					
	 * @throws Exception
	 */
	public FrameVO viewPanelUiType(String frame_type_code) throws Exception;
	
	/**
	 * 패널UI타입 조회(select)
	 * @param frame_type	30: 패널UI타입
	 * @return
	 * @throws Exception
	 */
	public List<FrameVO> getPanelUiTypeSelect(String frame_type) throws Exception;

	/**
	 * 말풍선목록(select)
	 * @return
	 * @throws Exception
	 */
	public List<BubbleList> getBubbleList(BubbleSearch vo);

	/**
	 * 말풍선목록카운트(select)
	 * @return
	 * @throws Exception
	 */
	public int getBubbleListCnt(BubbleSearch vo);

	/**
	 * 말풍선 삭제
	 * @return
	 * @throws Exception
	 */
	public void deleteBubble(String delList);

	/**
	 * 말풍선 등록
	 * @return
	 * @throws Exception
	 */
	public void insertBubble(BubbleInsert bubbleInsert);

	/**
	 * 지면전체조회
	 * @return
	 * @throws Exception
	 */
	public List<ViewVO> getPanelTitleTempList();

	/**
	 * 말풍선상세조회
	 * @return
	 * @throws Exception
	 */
	public BubbleInsert getBubbleDetail(String reg_no);

	/**
	 * 말풍선단말 조회
	 * @return
	 * @throws Exception
	 */
	public List<String> getBubbleTerminal(String reg_no);

	/**
	 * 말풍선 수정
	 * @return
	 * @throws Exception
	 */
	public void updateBubble(BubbleInsert bubbleInsert);
	
	/**
	 * 순서 변경 (orderList의 순서대로 순서를 변경)
	 *
	 * @param orderList regNumber List
	 * @throws Exception
	 */
	public void changeOrder(List<String> orderList) throws Exception;
	Map<String, Object> getABMSCall(ABTestSearchVO searchVo) throws Exception;
	
	/**
	 * OTT/추천앱 선택팝업
	 *
	 * @param orderList regNumber List
	 * @throws Exception
	 */
	public List<CategoryVO> getOtCategorySelect2(LinkInfoVO param);
	
	/**
	 * OTT/추천앱 선택팝업
	 *
	 * @param orderList regNumber List
	 * @throws Exception
	 */
	public List<CategoryVO> getOtCategorySelect1();
	/**
	 * 노출단말 조회
	 *
	 * @param orderList regNumber List
	 * @throws Exception
	 */
	public List<String> getPaperTerminal(String string);
	
	/**
	 * 노출단말 조회
	 *
	 * @param orderList regNumber List
	 * @throws Exception
	 */
	public List<PanelTitleTreamVO> getLikePaperTerminal(String string);
}
