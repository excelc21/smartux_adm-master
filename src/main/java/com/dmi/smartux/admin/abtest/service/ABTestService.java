package com.dmi.smartux.admin.abtest.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dmi.smartux.admin.abtest.vo.ABTestSearchVO;
import com.dmi.smartux.admin.abtest.vo.ABTestVO;
import com.dmi.smartux.admin.mainpanel.vo.ViewVO;

public interface ABTestService {

	/**
	 * 2021.04.16 AB테스트 MIMS
	 * ABMS 호출
	 * @param searchVo
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getABMSCall(ABTestSearchVO searchVo) throws Exception;
	
	/**
	 * 2021.04.16 AB테스트 MIMS
	 * AB테스트 목록 조회
	 * @param abmsList
	 * @return
	 * @throws Exception
	 */
	public List<ABTestVO> getABTestList (List<ABTestVO> abmsList) throws Exception;
	
	/**
	 * 2021.04.20 AB테스트 MIMS
	 * 패널 목록
	 * @param searchVo
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, String>> getPanelList(ABTestSearchVO searchVo) throws Exception;
	
	/**
	 * 2021.04.20 AB테스트 MIMS
	 * AB테스트 등록
	 * @param org_mims_id
	 * @param test_id
	 * @param mtype
	 * @param admin_id
	 * @param test_type 
	 * @throws Exception
	 */
	public void  insertABTest(String org_mims_id, String test_id, String mtype, String admin_id, String test_type) throws Exception;
	
	/**
	 * 2021.04.23 AB테스트 MIMS
	 * AB테스트 지면 목록
	 * @param mims_id
	 * @return
	 * @throws Exception
	 */
	public List<ViewVO> getABTestPaperList (String mims_id) throws Exception;
	
	/**
	 * 2021.04.23 AB테스트 MIMS
	 * AB테스트 오리지날 지면 목록
	 * @param org_mims_id
	 * @param  
	 * @return
	 * @throws Exception
	 */
	public List<ViewVO> getABTestOrgPaperList (String test_id, String variation_id) throws Exception;
	
	/**
	 * @param variation_id
	 * @return
	 * @throws Exception
	 */
	public ABTestVO getVariationInfo(String variation_id) throws Exception;

	/**
	 * @param test_id
	 * @return
	 * @throws Exception
	 */
	public ABTestVO getMinVariationId(String test_id) throws Exception;
	
	/**
	 * @param mims_id
	 * @param loginUser
	 * @param new_panel_id
	 * @throws Exception
	 */
	public void updateABTestPaper(String mims_id, String loginUser, String new_panel_id) throws Exception;
	
	/**
	 * @param panel_id
	 * @param p_title_id
	 * @return
	 * @throws Exception
	 */
	public List<ViewVO> getPaperOrder(String panel_id, String p_title_id) throws Exception;
	
	/**
	 * @param panel_id
	 * @param title_ids
	 * @param update_id
	 * @throws Exception
	 */
	public void updatePaperOrder(String panel_id, String[] title_ids, String update_id) throws Exception;
	
	/**
	 * @param searchVo
	 * @return
	 * @throws Exception
	 */
	public List<ViewVO> getPaperList(ABTestSearchVO searchVo) throws Exception;
	
	/**
	 * @param searchVo
	 * @return
	 * @throws Exception
	 */
	public void insertABTestPaper(String mims_id, String org_title_id, String org_panel_id, String loginUser) throws Exception;
	
	/**
	 * 2021.05.04 AB테스트 MIMS
	 * AB테스트 지면 count 확인
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public int selectPaperCount(ViewVO vo) throws Exception;
	
	/**
	 * AB테스트 지면 삭제
	 * @param paper_id_arr
	 * @param mims_id
	 * @throws Exception
	 */
	public void deleteABTestPaper(String[] title_id_arr, String mims_id) throws Exception;
	
	/**
	 * 지면상세조회
	 * @param panel_id
	 * @param title_id
	 * @return
	 * @throws Exception
	 */
	public ViewVO getPaperInfo(String panel_id, String title_id)	throws Exception;
	
	/**
	 * 지면상세정보업데이트
	 * @param panel_id
	 * @param title_id
	 * @param title_nm
	 * @param title_color
	 * @param use_yn
	 * @param update_id
	 * @param title_bg_img_file
	 * @param bg_img_file
	 * @param bg_img_file2
	 * @param bg_img_file3
	 * @param bg_img_file4
	 * @return
	 * @throws Exception
	 */
	public int updatePaperInfo(String variation_id, String panel_id, String title_id, String title_nm, String title_color, String use_yn, String update_id, String title_bg_img_file, String bg_img_file, String bg_img_file2, String bg_img_file3 , String bg_img_file4, String abtest_yn) throws Exception;
	
	/**
	 * 동일지면이름 체크
	 * @param panel_id
	 * @param p_title_id
	 * @param title_nm
	 * @return
	 * @throws Exception
	 */
	public int getPaperInfoTitleNmCnt(String panel_id, String p_title_id, String title_nm) throws Exception;
	
	/**
	 * 지면데이터정보
	 * @param panel_id
	 * @param title_id
	 * @return
	 * @throws Exception
	 */
	public ViewVO getPaperDataType(String panel_id, String title_id) throws Exception;
	
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
	 * @param terminal_arr 
	 * @param terminal_all_yn 
	 * @param show_cnt 
	 * @param product_code_not 
	 * @param product_code 
	 * @return
	 * @throws Exception
	 */
	public int updatePaperDataType(String variation_id, String panel_id, String title_id, String title_nm, String category_id, String category_type, String album_cnt, String ui_type, String description, String update_id, String page_type, String page_code, String category_gb, 
			String reps_album_id, String reps_category_id, String trailer_viewing_type, String reps_trailer_viewing_type, String location_code, String location_yn, String paper_ui_type, String abtest_yn, String product_code, String product_code_not, String show_cnt, String terminal_all_yn, String terminal_arr) throws Exception;
	
	/**
	 * 2021.05.12 AB테스트 MIMS
	 * AB테스트 variation_id 별 status 상태 확인
	 * @param variation_id
	 * @param abmsList
	 * @return
	 * @throws Exception
	 */
	public boolean checkABMSStatus(String variation_id, List<ABTestVO> abmsList) throws Exception;
	
	/**
	 * 2021.05.13 AB테스트 MIMS
	 * AB테스트 지면 최종완료
	 * @param test_id
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> finishABTestPaper(String test_id) throws Exception;
	
	/**
	 * 2021.05.13 AB테스트 MIMS
	 * AB테스트 지면 최종완료 상태 수정
	 * @param status
	 * @param test_id
	 * @throws Exception
	 */
	public void updateABTestStatus(String status, String test_id) throws Exception;
	
	/**
	 * 2021.04.20 AB테스트 MIMS
	 * 즉시적용 AB테스트 등록
	 * @param org_mims_id
	 * @param test_id
	 * @param mtype
	 * @param admin_id
	 * @throws Exception
	 */
	public void  insertABTestPanelTitle(String panel_id, String create_id) throws Exception;
}
