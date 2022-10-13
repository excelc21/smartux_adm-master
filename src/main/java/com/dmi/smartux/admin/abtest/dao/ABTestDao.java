package com.dmi.smartux.admin.abtest.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dmi.smartux.admin.abtest.vo.ABTestSearchVO;
import com.dmi.smartux.admin.abtest.vo.ABTestVO;
import com.dmi.smartux.admin.mainpanel.vo.ViewVO;
import com.dmi.smartux.common.dao.CommonIptvDao;

@Repository
public class ABTestDao extends CommonIptvDao {

	/**
	 * 2021.04.16 AB테스트 MIMS
	 * AB테스트 목록 조회
	 * @param variation_id
	 * @return
	 */
	public ABTestVO getABTestList (String variation_id) {
		return (ABTestVO) getSqlMapClientTemplate().queryForObject("admin_abtest.getABTestList", variation_id);
	}
	
	/**
	 * 2021.04.20 AB테스트 MIMS
	 * 패널 목록
	 * @param searchVo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> getPanelList(ABTestSearchVO searchVo) {
		List<HashMap<String,String>> result = getSqlMapClientTemplate().queryForList("admin_abtest.getPanelList", searchVo);		
		return result;
	}
	
	/**
	 * 2021.04.20 AB테스트 MIMS
	 * AB테스트 정보 등록
	 * @param vo
	 * @return
	 */
	public void insertABTestInfo(ABTestVO vo) {
		getSqlMapClientTemplate().insert("admin_abtest.insertABTestInfo", vo);
	}
	
	/**
	 * 2021.04.20 AB테스트 MIMS
	 * AB테스트 Variation 정보 등록
	 * @param vo
	 * @return
	 */
	public void insertABVariationInfo(ABTestVO vo) {
		getSqlMapClientTemplate().insert("admin_abtest.insertABVariationInfo", vo);
	}
	
	/**
	 * 2021.04.20 AB테스트 MIMS
	 * AB테스트 패널정보 등록
	 * @param vo
	 * @return
	 */
	public void insertABPanel(ABTestVO vo) {
		getSqlMapClientTemplate().insert("admin_abtest.insertABPanel", vo);
	}
	
	/**
	 * 2021.04.20 AB테스트 MIMS
	 * AB테스트 패널정보 등록
	 * @param vo
	 * @return
	 */
	public void insertABPanelTestD(ABTestVO vo) {
		getSqlMapClientTemplate().insert("admin_abtest.insertABPanelTestD", vo);
	}
	/**
	 * 2021.04.20 AB테스트 MIMS
	 * AB테스트 지면정보 등록
	 * @param vo
	 * @return
	 */
	public void insertABPaper(ABTestVO vo) {
		getSqlMapClientTemplate().insert("admin_abtest.insertABPaper", vo);
	}
	
	/**
	 * 2021.04.23 AB테스트 MIMS
	 * AB테스트 variation 정보 조회
	 * @param variation_id
	 * @return
	 */
	public ABTestVO getVariationInfo(String variation_id){
		return (ABTestVO) getSqlMapClientTemplate().queryForObject("admin_abtest.getVariationInfo", variation_id);
	}
	
	/**
	 * 2021.04.23 AB테스트 MIMS
	 * AB테스트 variation 정보 조회
	 * @param variation_id
	 * @return
	 */
	public ABTestVO getMinVariationId(String test_id){
		return (ABTestVO) getSqlMapClientTemplate().queryForObject("admin_abtest.getMinVariationId", test_id);
	}
	
	/**
	 * 2021.04.23 AB테스트 MIMS
	 * AB테스트 지면 목록
	 * @param mimd_id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ViewVO> getABTestPaperList(String mimd_id) {
		return (List<ViewVO>) getSqlMapClientTemplate().queryForList("admin_abtest.getABTestPaperList", mimd_id);
	}
	
	/**
	 * 2021.04.23 AB테스트 MIMS
	 * AB테스트 오리지날 지면 목록
	 * @param org_mims_id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ViewVO> getABTestOrgPaperList(String test_id, String variation_id) {
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("test_id", test_id);
		param.put("variation_id", variation_id);
		
		return (List<ViewVO>) getSqlMapClientTemplate().queryForList("admin_abtest.getABTestOrgPaperList", param);
	}
	
	/**
	 * 2021.04.27 AB테스트 MIMS
	 * 지면 삭제
	 * @param mims_id
	 */
	public void deleteABTestPaperList(String mims_id) {
		getSqlMapClientTemplate().delete("admin_abtest.deleteABTestPaperList", mims_id);
	}
	
	/**
	 * @param panel_id
	 * @param p_title_id
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<ViewVO> getPaperOrder(String mims_id, String p_title_id) throws Exception{
		// 최상위 레벨의 지면 순서를 바꿀때는 p_title_id 값이 "" 이므로 -1로 바꾸어서 쿼리에서 적용이 될수 있도록 해준다
		if("".equals(p_title_id)){
			p_title_id = "-1";
		}
				
		Map<String, String> param = new HashMap<String, String>();
		param.put("pannel_id", mims_id);
		param.put("p_title_id", p_title_id);
		
		return (List<ViewVO>) getSqlMapClientTemplate().queryForList("admin_abtest.getPaperOrder", param);
	}
	
	/**
	 * @param ordered
	 * @param panel_id
	 * @param title_id
	 * @param update_id
	 * @return
	 * @throws Exception
	 */
	public int updatePaperOrder(int ordered, String panel_id, String title_id, String update_id) throws Exception{
		ViewVO item = new ViewVO();
		item.setOrdered(ordered);
		item.setPannel_id(panel_id);
		item.setTitle_id(title_id);
		item.setUpdate_id(update_id);
		
		return getSqlMapClientTemplate().update("admin_abtest.updatePaperOrder", item);
	}
	
	/**
	 * 2021.04.29 AB테스트 MIMS
	 * 지면 목록 조회
	 * @param searchVo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ViewVO> getPaperList(ABTestSearchVO searchVo) {
		return (List<ViewVO>)getSqlMapClientTemplate().queryForList("admin_abtest.getPaperList", searchVo);
	}
	
	/**
	 * 2021.05.04 AB테스트 MIMS
	 * AB테스트 지면 count 확인
	 * @param vo
	 * @return
	 */
	public int selectPaperCount(ViewVO vo){
		return (Integer) getSqlMapClientTemplate().queryForObject("admin_abtest.selectPaperCount", vo);
	}
	
	/**
	 * 2021.05.04 AB테스트 MIMS
	 * AB테스트 지면 order 최대값+1
	 * @param mims_id
	 * @return
	 */
	public int selectPaperMaxOrd(String mims_id) {
		return (Integer) getSqlMapClientTemplate().queryForObject("admin_abtest.selectPaperMaxOrd", mims_id);
	}
	
	/**
	 * 2021.05.04 AB테스트 MIMS
	 * AB테스트 지면 삭제
	 * @param vo
	 */
	public void deleteABTestPaper(ViewVO vo) {
		getSqlMapClientTemplate().delete("admin_abtest.deleteABTestPaper", vo);
	}
	
	/**
	 * 패널지면 임시 테이블에서 지면 상세 조회 
	 * @param panel_id			조회하고자 하는 지면이 속한 panel_id
	 * @param title_id			조회하고자 하는 지면의 title_id
	 * @return					상세조회된 지면
	 * @throws Exception
	 */
	public ViewVO getPaperInfo(String panel_id, String title_id) throws Exception{
		Map<String, String> param = new HashMap<String, String>();
		param.put("pannel_id", panel_id);
		param.put("title_id", title_id);
		
		ViewVO result = (ViewVO)(getSqlMapClientTemplate().queryForObject("admin_abtest.getPaperInfo", param));
		return result;
	}
	
	/**
	 * 지면상세정보 업데이트
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
	public int updatePaperInfo(String panel_id, String title_id, String title_nm, String title_color, String use_yn, String update_id, String title_bg_img_file, String bg_img_file, String bg_img_file2 , String bg_img_file3 , String bg_img_file4, String abtest_yn) throws Exception {
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("pannel_id", panel_id);
		param.put("title_id", title_id);
		param.put("title_nm", title_nm);
		param.put("title_color", title_color);
		param.put("use_yn", use_yn);
		param.put("update_id", update_id);
		param.put("title_bg_img_file", title_bg_img_file);
		param.put("bg_img_file", bg_img_file);
		param.put("bg_img_file2", bg_img_file2);
		param.put("bg_img_file3", bg_img_file3);
		param.put("bg_img_file4", bg_img_file4);
		param.put("abtest_yn", abtest_yn);

		return getSqlMapClientTemplate().update("admin_abtest.updatePaperInfo", param);
	}
	
	/**
	 * 패널지면 임시 테이블에서 입력받은 지면의 이름 갯수 조회
	 * @param panel_id			조회하고자 하는 지면이 속한 패널의 panel_id
	 * @param p_title_id		조회하고자 하는 지면이 속한 상위 지면의 title_id
	 * @param title_nm			지면 이름
	 * @return					조회된 지면 이름 갯수
	 * @throws Exception
	 */
	public int getPaperInfoTitleNmCnt(String panel_id, String p_title_id, String title_nm) throws Exception{
		// 최상위 레벨의 PanelTitle을 넣을 경우엔 -1로 셋팅한다. 그래서 쿼리문에서 null 조건으로 비교가 될수 있도록 한다
		if("".equals(p_title_id)){
			p_title_id = "-1";
		}
				
		Map<String, String> param = new HashMap<String, String>();
		param.put("panel_id", panel_id);
		param.put("p_title_id", p_title_id);
		param.put("title_nm", title_nm);
		
		int count = (Integer)(getSqlMapClientTemplate().queryForObject("admin_abtest.getPaperInfoTitleNmCnt", param));
		return count;
	}
	
	/**
	 * 지면 데이터 설정에서 지면의 UI 타입과 지면 설명을 조회
	 * @param panel_id		지면이 속한 패널의 panel_id
	 * @param title_id		지면의 title_id
	 * @return				지면의 UI 타입과 지면 설명이 있는 지면 정보
	 * @throws Exception
	 */
	public ViewVO getPaperDataType(String panel_id, String title_id) throws Exception {
		Map<String, String> param = new HashMap<String, String>();
		param.put("pannel_id", panel_id);
		param.put("title_id", title_id);
		
		ViewVO result = (ViewVO)getSqlMapClientTemplate().queryForObject("admin_abtest.getPaperDataType", param);
		
		return result;
	}
	
	/**
	 * 패널지면 임시 테이블에서 지면 데이터 설정 작업
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
	 * @param reps_trailer_viewing_type		대표컨텐츠 예고편 노출타입
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
	public int updatePaperDataType(String panel_id, String title_id, String category_id, String category_type, String album_cnt, String ui_type, String description, String update_id, String page_type, String page_code, 
			String category_gb, String reps_album_id, String reps_category_id, String trailer_viewing_type, String reps_trailer_viewing_type, String location_code, String location_yn, String paper_ui_type, String abtest_yn, String product_code, String product_code_not, String show_cnt, String terminal_all_yn, String terminal_arr) throws Exception{
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("pannel_id", panel_id);
		param.put("title_id", title_id);
		param.put("category_id", category_id);
		param.put("category_type", category_type);
		param.put("album_cnt", album_cnt);
		param.put("ui_type", ui_type);
		//param.put("bg_img_file", bg_img_file);
		param.put("description", description);
		param.put("page_type", page_type);
		param.put("page_code", page_code);
		param.put("category_gb", category_gb);
		param.put("reps_album_id", reps_album_id);
		param.put("reps_category_id", reps_category_id);
		param.put("trailer_viewing_type", trailer_viewing_type);
		param.put("reps_trailer_viewing_type", reps_trailer_viewing_type);
		//param.put("bg_img_file2", bg_img_file2);
		param.put("location_code", location_code);
		param.put("location_yn", location_yn);
		param.put("update_id", update_id);
		
		param.put("paper_ui_type", paper_ui_type);
		param.put("abtest_yn", abtest_yn);
		
		param.put("product_code", product_code);
		param.put("product_code_not", product_code_not);
		param.put("show_cnt", show_cnt);
		
		int result = getSqlMapClientTemplate().update("admin_abtest.updatePaperDataType", param);
		
		return result;
	}
	
	/**
	 * 2021.05.07 AB테스트 MIMS
	 * AB테스트 수정한 지면정보 set
	 * @param vo
	 */
	public void updateABVariationInfo(ABTestVO vo) {
		getSqlMapClientTemplate().update("admin_abtest.updateABVariationInfo",vo);
	}
	
	/**
	 * 2021.05.13 AB테스트 MIMS
	 * AB테스트 지면 최종완료 상태 수정
	 * @param status
	 * @param test_id
	 */
	public void updateABTestStatus(String status, String test_id) {
		ABTestVO vo = new ABTestVO();
		vo.setStatus(status);
		vo.setTest_id(test_id);
		getSqlMapClientTemplate().update("admin_abtest.updateABTestStatus",vo);
	}
	
	/**
	 * @param test_id
	 * @return
	 */
	public ABTestVO getABTestStatus(String test_id) {
		ABTestVO vo = new ABTestVO();
		vo.setTest_id(test_id);
		return (ABTestVO) getSqlMapClientTemplate().queryForObject("admin_abtest.getABTestStatus",vo);
	}
	
	/**
	 * PT_UX_AB_PANEL_TITLE 테이블에서 panel_id에 해당하는 패널 것 삭제
	 * @param panel_id
	 */
	public void deleteABTestPanelTitle(String panel_id){
		getSqlMapClientTemplate().delete("admin_abtest.deleteABTestPanelTitle", panel_id);
	}
	
	/**
	 * PT_UX_AB_PANEL_TITLE_TEMP 테이블에서 panel_id에 해당하는 패널 것을 PT_UX_AB_PANEL_TITLE 테이블에 등록
	 * @param panel_id
	 * @param create_id
	 */
	public void insertABTestPanelTitle(String panel_id, String create_id){
		Map<String, String> param = new HashMap<String, String>();
		param.put("pannel_id", panel_id);
		param.put("create_id", create_id);
		
		getSqlMapClientTemplate().insert("admin_abtest.insertABTestPanelTitle", param);
	}
	
	/**
	 * PT_UX_AB_PANEL 테이블에 Version 업데이트
	 * @param panel_id
	 * @param create_id
	 */
	public void updateABTestPanelVersion(String version, String create_id){
		Map<String, String> param = new HashMap<String, String>();
		param.put("version", version);
		param.put("update_id", create_id);
		
		getSqlMapClientTemplate().update("admin_abtest.updateABTestPanelVersion", param);
	}
	
	/**
	 * PT_UX_AB_PANEL_TITLE 테이블에 Version 업데이트
	 * @param panel_id
	 * @param create_id
	 */
	public void updateABTestPanelTitleVersion(String version, String create_id){
		Map<String, String> param = new HashMap<String, String>();
		param.put("version", version);
		param.put("update_id", create_id);
		
		getSqlMapClientTemplate().update("admin_abtest.updateABTestPanelTitleVersion", param);
	}
	
	/**
	 * 2021.09.06 AB테스트 MIMS
	 * AB테스트 variation 정보 조회
	 * @param variation_id
	 * @return
	 */
	public ABTestVO getDmimsIdVariationInfo(String variation_id){
		return (ABTestVO) getSqlMapClientTemplate().queryForObject("admin_abtest.getDmimsIdVariationInfo", variation_id);
	}
	
	public int updatePaperInfoDMims(String panel_id, String title_id, String abtest_yn) throws Exception {
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("pannel_id", panel_id);
		param.put("title_id", title_id);
		param.put("abtest_yn", abtest_yn);

		return getSqlMapClientTemplate().update("admin_abtest.updatePaperInfoDMims", param);
	}
	
	/**
	 * 2021.09.06 AB테스트 MIMS
	 * AB테스트 variation 정보 조회
	 * @param testType_list 
	 * @param variation_id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ABTestVO> getOrgMimsIdVariationInfo(String org_mims_id, List<String> testType_list){
		//Map<String, String> param = new HashMap<String, String>();
		//param.put("org_mims_id", org_mims_id);
		ABTestVO aBTestVO = new ABTestVO();
		aBTestVO.setOrg_mims_id(org_mims_id);
		aBTestVO.setTestType_list(testType_list);
		
		return getSqlMapClientTemplate().queryForList("admin_abtest.getOrgMimsIdVariationInfo", aBTestVO);
	}
	/**
	 * 2021.09.06 AB테스트 MIMS
	 * AB테스트 variation 정보 조회
	 * @param variation_id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ABTestVO> getAbPanelTitleTempInfo(String org_mims_id, String title_id, String test_type){
		Map<String, String> param = new HashMap<String, String>();
		param.put("org_mims_id", org_mims_id);
		param.put("title_id", title_id);
		param.put("test_type", test_type);

		return getSqlMapClientTemplate().queryForList("admin_abtest.getAbPanelTitleTempInfo", param);
	}
	/**
	 * 2021.09.06 AB테스트 MIMS
	 * AB테스트 variation 정보 조회
	 * @param variation_id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ABTestVO> getAbPanelTitleTempInfoList(String org_mims_id, List<String> testType_List, String abtest_yn, List<String> titleIdList){
		
		ABTestVO aBTestVO = new ABTestVO();
		aBTestVO.setOrg_mims_id(org_mims_id);
		aBTestVO.setTestType_list(testType_List);
		aBTestVO.setAbtest_yn(abtest_yn);
		aBTestVO.setTitleId_list(titleIdList);
		
		return getSqlMapClientTemplate().queryForList("admin_abtest.getAbPanelTitleTempInfoList", aBTestVO);
	}
	/**
	 * 패널지면 임시 테이블에 지면 등록
	 * 기존 경로 :/smartux_adm/src/main/java/com/dmi/smartux/admin/mainpanel/dao/PanelViewDao.java
	 * @param panel_id			등록하고자 하는 지면이 속한 panel_id 
	 * @param title_nm			등록하고자 하는 지면의 title_nm
	 * @param title_color		등록하고자 하는 지면의 title_color
	 * @param p_title_id		등록하고자 하는 지면의 상위 지면 title_id
	 * @param use_yn			지면의 사용여부(Y/N)
	 * @param create_id			관리자 사이트에 로그인 한 사람의 로그인 id
	 * @param bg_img_file		지면 아이콘 이미지
	 * @param bg_img_file2		지면 아이콘 이미지2
	 * @throws Exception
	 */
	public void insertPanelTitleTemp(String panel_id, String title_nm, String title_color, String p_title_id, String use_yn, String create_id, String title_bg_img_file, String bg_img_file, String bg_img_file2 , String bg_img_file3, String title_id) throws Exception {
		// TODO Auto-generated method stub
		
		// 최상위 레벨의 PanelTitle을 넣을 경우엔 -1로 셋팅한다. 그래서 쿼리문에서 -1을 만났을때 decode문을 이용해서 p_title_id 컬럼에 null이 들어가도록 한다
		if("".equals(p_title_id)){
			p_title_id = "-1";
		}
		
		//String title_id = getMaxTitleid(panel_id, p_title_id);				// title_id 값을 구한다
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("pannel_id", panel_id);
		param.put("title_id", title_id);
		param.put("title_nm", title_nm);
		param.put("title_color", title_color);
		param.put("p_title_id", p_title_id);
		param.put("use_yn", use_yn);
		param.put("create_id", create_id);
		param.put("update_id", create_id);
		param.put("title_bg_img_file", title_bg_img_file);
		param.put("bg_img_file", bg_img_file);
		param.put("bg_img_file2", bg_img_file2);
		param.put("bg_img_file3", bg_img_file3);
		
		getSqlMapClientTemplate().insert("admin_abtest.insertPanelTitleTemp", param);
	}
	
	/**
	 * 패널지면 임시 테이블에서 지면 수정
	 * 기존 경로 : /smartux_adm/src/main/java/com/dmi/smartux/admin/mainpanel/dao/PanelViewDao.java
	 * @param panel_id			수정하고자 하는 지면이 속한 패널의 panel_id
	 * @param title_id			수정하고자 하는 지면의 title_id
	 * @param title_nm			수정하고자 하는 지면의 title_nm
	 * @param title_color		수정하고자 하는 지면의 title_color
	 * @param use_yn			지면의 사용여부(Y/N)
	 * @param update_id			관리자 사이트에 로그인 한 사람의 로그인 id
	 * @param bg_img_file		지면 아이콘 이미지
	 * @param bg_img_file2		지면 아이콘 이미지2
	 * @return					수정된 지면의 갯수
	 * @throws Exception
	 */
	public int updatePanelTitleTemp(String panel_id, String title_id, String title_nm, String title_color, String use_yn, String update_id, String title_bg_img_file, String bg_img_file, String bg_img_file2 , String bg_img_file3) throws Exception {
		// TODO Auto-generated method stub
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("pannel_id", panel_id);
		param.put("title_id", title_id);
		param.put("title_nm", title_nm);
		param.put("title_color", title_color);
		param.put("use_yn", use_yn);
		param.put("update_id", update_id);
		param.put("title_bg_img_file", title_bg_img_file);
		param.put("bg_img_file", bg_img_file);
		param.put("bg_img_file2", bg_img_file2);
		param.put("bg_img_file3", bg_img_file3);
		
		
		int result = getSqlMapClientTemplate().update("admin_abtest.updatePanelTitleTemp", param);
		return result;
	}
	
	/**
	 * 패널지면 임시 테이블에 지면을 등록시 등록된 지면의 상위 지면의 카테고리 id, SMARTUX 타입, 카테고리 코드에 매핑되는 앨범 갯수, 
	 * UI 타입, 지면 배경 이미지 파일, 지면 설명을 null로 셋팅한다
	 * (상위 지면이 될 경우 이 컬럼의 값들은 하위 지면들이 대신 표현해주기때문에 값들이 의미가 없어진다)
	 * 기존 경로 : /smartux_adm/src/main/java/com/dmi/smartux/admin/mainpanel/dao/PanelViewDao.java
	 * @param panel_id			상위 지면이 속한 panel_id
	 * @param p_title_id		상위 지면 title_id 
	 * @param update_id			관리자 사이트에 로그인 한 사람의 로그인 id
	 * @return					수정된 지면의 갯수
	 * @throws Exception
	 */
	public int updatePanelTitleTempCategorySetNull(String panel_id, String p_title_id, String update_id) throws Exception{
		Map<String, String> param = new HashMap<String, String>();
		param.put("update_id", update_id);
		param.put("pannel_id", panel_id);
		param.put("p_title_id", p_title_id);
		
		int result = getSqlMapClientTemplate().update("admin_abtest.updatePanelTitleTempCategorySetNull", param);
		return result;
	}
	
	
	/**
	 * 패널지면 임시 테이블에서 지면 데이터 설정 작업
	 * 기존 경로 : /smartux_adm/src/main/java/com/dmi/smartux/admin/mainpanel/dao/PanelViewDao.java
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
	 * @param reps_trailer_viewing_type		대표컨텐츠 예고편 노출타입
	 * @param bg_img_file2		지면 데이터 설정작업을 하는 지면의 아이콘 이미지2
	 * @param location_code		지면 데이터 설정작업을 하는 지면의 지역 정보(code)
	 * @param location_yn		지면 데이터 설정작업을 하는 지면의 국내 여부(국내: Y, 해외: N)
	 * @param show_cnt 
	 * @param product_code_not 
	 * @param product_code 
	 * @return
	 * @throws Exception
	 */
	public int updateCategory(String panel_id, String title_id, String category_id, String category_type, String album_cnt, String ui_type, String description, String update_id, String page_type, String page_code, 
			String category_gb, String reps_album_id, String reps_category_id, String trailer_viewing_type, String reps_trailer_viewing_type, String location_code, String location_yn
/* 2019.11.04 : 지면 UI 타입 [paper_ui_type 변수 추가] 추가 Start - 이태광 */					
			, String paper_ui_type, String product_code, String product_code_not, String show_cnt)
/* 2019.11.04 : 지면 UI 타입 [paper_ui_type 변수 추가] 추가 End - 이태광 */			
			throws Exception{
		Map<String, String> param = new HashMap<String, String>();
		param.put("pannel_id", panel_id);
		param.put("title_id", title_id);
		param.put("category_id", category_id);
		param.put("category_type", category_type);
		param.put("album_cnt", album_cnt);
		param.put("ui_type", ui_type);
		//param.put("bg_img_file", bg_img_file);
		param.put("description", description);
		param.put("page_type", page_type);
		param.put("page_code", page_code);
		param.put("category_gb", category_gb);
		param.put("reps_album_id", reps_album_id);
		param.put("reps_category_id", reps_category_id);
		param.put("trailer_viewing_type", trailer_viewing_type);
		param.put("reps_trailer_viewing_type", reps_trailer_viewing_type);
		//param.put("bg_img_file2", bg_img_file2);
		param.put("location_code", location_code);
		param.put("location_yn", location_yn);
		param.put("update_id", update_id);
		
		
/* 2019.11.04 : 지면 UI 타입 [paper_ui_type 변수 추가] 추가 Start - 이태광 */	
		param.put("paper_ui_type", paper_ui_type);
/* 2019.11.04 : 지면 UI 타입 [paper_ui_type 변수 추가] 추가 End - 이태광 */	
		
		param.put("product_code", product_code);
		param.put("product_code_not", product_code_not);
		param.put("show_cnt", show_cnt);
		
		int result = getSqlMapClientTemplate().update("admin_abtest.updateCategory", param);
		
		return result;
	}
	
	/**
	 * 패널지면 임시 테이블에서 지면 삭제
	 * 기존 경로 : /smartux_adm/src/main/java/com/dmi/smartux/admin/mainpanel/dao/PanelViewDao.java
	 * @param panel_id			삭제하고자 하는 지면들이 속한 패널의 panel_id
	 * @param title_id			삭제하고자 하는 지면의 title_id
	 * @return					삭제된 지면의 갯수
	 * @throws Exception
	 */
	public int deletePanelTitleTemp(String panel_id, String title_id) throws Exception {
		Map<String, String> param = new HashMap<String, String>();
		param.put("pannel_id", panel_id);
		param.put("title_id", title_id);
		
		int result = getSqlMapClientTemplate().delete("admin_abtest.deletePanelTitleTemp", param);
		
		return result;
	}
	
	/**
	 * 패널지면 임시 테이블에서 지면의 순서 바꾸기 작업
	 * 기존 경로 : /smartux_adm/src/main/java/com/dmi/smartux/admin/mainpanel/dao/PanelViewDao.java
	 * @param ordered		지면 순서
	 * @param panel_id		지면이 속한 패널의 panel_id
	 * @param title_id		지면의 title_id
	 * @param update_id		관리자 사이트에 로그인 한 사람의 로그인 id
	 * @return
	 * @throws Exception
	 */
	public int changePanelTitleTempOrderJob(int ordered, String panel_id, String title_id, String update_id) throws Exception{
		ViewVO item = new ViewVO();
		item.setOrdered(ordered);
		item.setPannel_id(panel_id);
		item.setTitle_id(title_id);
		item.setUpdate_id(update_id);
		
		int result = getSqlMapClientTemplate().update("admin_abtest.changePanelTitleTempOrderJob", item);
		return result;
	}
}
