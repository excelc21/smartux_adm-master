package com.dmi.smartux.admin.common.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.dmi.smartux.admin.hotvod.vo.HotvodContentVO;
import com.dmi.smartux.admin.hotvod.vo.HotvodFileUploadVO;
import com.dmi.smartux.admin.mainpanel.vo.PreviewVO;
import com.dmi.smartux.admin.mainpanel.vo.ViewVO;
import com.dmi.smartux.admin.schedule.vo.ScheduleVO;
import com.dmi.smartux.admin.secondtv_push.vo.LatestContentVO;
import com.dmi.smartux.bonbang.vo.RegistrationIDParamVO;
import com.dmi.smartux.common.dao.CommonMimsDao;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.push.vo.NextUiPushContentVO;
import com.dmi.smartux.push.vo.NextUiPushUserParamVO;
import com.dmi.smartux.push.vo.NextUiPushUserVO;

/**
 * @author medialog
 * 
 * DB 분리로 인한 DAO분리건
 */
@Repository
public class CommMimsDao extends CommonMimsDao{
		
	/**
	 * 컨텐츠 수정
	 * 기존 경로 : /smartux_adm/src/main/java/com/dmi/smartux/admin/hotvod/dao/HotvodDao.java
	 * @param hotvodContentVO
	 */
	public void contentUpdate(HotvodContentVO hotvodContentVO){
		getSqlMapClientTemplate().update("mims.contentUpdate", hotvodContentVO);
	}
	
	/**
	 * 컨텐츠 등록
	 * 기존 경로 : /smartux_adm/src/main/java/com/dmi/smartux/admin/hotvod/dao/HotvodDao.java
	 * @param hotvodContentVO
	 */
	public void contentInsert(HotvodContentVO hotvodContentVO){
		getSqlMapClientTemplate().insert("mims.contentInsert", hotvodContentVO);
	}
	
	/**
	 * 컨텐츠 아이디 신규 채번
	 * 기존 경로 : /smartux_adm/src/main/java/com/dmi/smartux/admin/hotvod/dao/HotvodDao.java
	 * @return
	 */
	public String getContentId(){
		return (String)getSqlMapClientTemplate().queryForObject("mims.getContentId", null);
	}
	
	/**
	 * 대박영상 통합검색 목록 조회 
	 * 기존 경로 : /smartux_adm/src/main/java/com/dmi/smartux/admin/hotvod/dao/HotvodDao.java
	 * @param service_type (서비스타입)
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<HotvodFileUploadVO> getHotvodServiceListAll(String service_type){
		return getSqlMapClientTemplate().queryForList("mims.getHotvodServiceListAll", service_type);
	}	
	
	/**
	 * 컨텐츠 조회수 로그 수정
	 * 기존 경로 : /smartux_adm/src/main/java/com/dmi/smartux/admin/hotvod/dao/HotvodDao.java
	 * @param hotvodContentVO
	 */
	public int contentLogUpdate(HotvodContentVO hotvodContentVO){
		return getSqlMapClientTemplate().update("mims.contentLogUpdate", hotvodContentVO);
	}
	
	/**
	 * 컨텐츠 조회수 로그 등록
	 * 기존 경로 : /smartux_adm/src/main/java/com/dmi/smartux/admin/hotvod/dao/HotvodDao.java
	 * @param hotvodContentVO
	 */
	public void contentLogInsert(HotvodContentVO hotvodContentVO){
		getSqlMapClientTemplate().insert("mims.contentLogInsert", hotvodContentVO);
	}
	
	/**
	 * 예약발송 컨텐츠 별 사용자 리스트 총 건수 조회
	 * 기존 경로 : /smartux_adm/src/main/java/com/dmi/smartux/push/dao/NextUiPushDao.java
	 * @return
	 * @throws DataAccessException
	 */
	public int getNextUiPushUserCount(NextUiPushContentVO param) throws DataAccessException{
		int result = 0;
		result = (Integer)getSqlMapClientTemplate().queryForObject("mims.getNextUiPushUserCount", param); 
		return result;
	}
	
	/**
	 * 예약발송  컨텐츠 별 사용자 리스트 조회
	 * 기존 경로 : /smartux_adm/src/main/java/com/dmi/smartux/push/dao/NextUiPushDao.java
	 * @param cat_id
	 * @return
	 * @throws DataAccessException
	 */
	public List<NextUiPushUserVO> getNextUiPushUserList(NextUiPushUserParamVO param) throws DataAccessException{
		List<NextUiPushUserVO> result = null; 
		result = getSqlMapClientTemplate().queryForList("mims.getNextUiPushUserList", param);
		return result;
	}
	
	
	/**
	 * 조회한 최신회의 푸쉬 해줄 사용자 조회
	 * 기존 경로 : /smartux_adm/src/main/java/com/dmi/smartux/admin/secondtv_push/dao/LatestScheduleDao.java
	 *
	 * @param latestContentVO 최신회 컨텐츠 VO
	 * @return 푸시 사용자 리스트
	 * @throws org.springframework.dao.DataAccessException
	 */
	public List getUserList(LatestContentVO latestContentVO) throws DataAccessException {
		return getSqlMapClientTemplate().queryForList("mims.getUserList", latestContentVO);
	}
	
	/**
	 * 최신회가 있는지 확인할 카테고리 리스트 조회
	 * 기존 경로 : /smartux_adm/src/main/java/com/dmi/smartux/admin/secondtv_push/dao/LatestScheduleDao.java
	 *
	 * @return 카테고리 리스트
	 * @throws org.springframework.dao.DataAccessException
	 */
	public List getCategoryList() throws DataAccessException {
		return getSqlMapClientTemplate().queryForList("mims.getCategoryList");
	}
	
	/**
	 * 푸쉬할 사용자의 카테고리에 맞는 전체 카운트를 가져온다.
	 * 기존 경로 : /smartux_adm/src/main/java/com/dmi/smartux/admin/secondtv_push/dao/LatestScheduleDao.java
	 *
	 * @param catID 카테고리 아이디
	 * @return 카테고리에 맞는 전체 카운트
	 * @throws org.springframework.dao.DataAccessException
	 */
	public int getUserListCount(String catID) throws DataAccessException {
		return (Integer)getSqlMapClientTemplate().queryForObject("mims.getUserListCount", catID);
	}
	
	/**
	 * 패널지면 테이블에서 지면의 버전 정보 수정
	 * 기존 경로 : /smartux_adm/src/main/java/com/dmi/smartux/admin/mainpanel/dao/PanelViewDao.java
	 * @param version			수정된 패널 버전
	 * @param update_id			관리자 사이트에 로그인 한 사람의 로그인 id
	 * @return					수정된 지면 정보의 갯수
	 * @throws Exception
	 */
	public int updatePanelTitleVersion(String version, String update_id) throws Exception {
		Map<String, String> param = new HashMap<String, String>();
		param.put("version", version);
		param.put("update_id", update_id);
		
		int result = getSqlMapClientTemplate().update("mims.updatePanelTitleVersion", param);
		return result;
	}
	
	/**
	 * 패널지면 테이블에서 입력받은 패널에 속한 지면 정보를 삭제
	 * 기존 경로 :/smartux_adm/src/main/java/com/dmi/smartux/admin/mainpanel/dao/PanelViewDao.java
	 * @param panel_id		삭제하고자 하는 지면이 속한 패널의 panel_id
	 * @return
	 * @throws Exception
	 */
	public int deletePanelTitle(String panel_id) throws Exception {
		int result = getSqlMapClientTemplate().delete("mims.deletePanelTitle", panel_id);
		
		return result;
	}
	
	/**
	 * 지면에서 사용중인 ss_gbn 개수 조회 
	 * 기존 경로 : /smartux_adm/src/main/java/com/dmi/smartux/admin/code/dao/CodeDao.java
	 * @param ss_gbn
	 * @return
	 * @throws Exception
	 */
	public int getUseSsgbnCnt(String ss_gbn) throws Exception {
		int count = (Integer)(getSqlMapClientTemplate().queryForObject("mims.getUseSsgbnCnt", ss_gbn));
		return count;
	}
	
	/**
	 * 패널지면 임시 테이블에서 입력받은 지면의 이름 갯수 조회
	 * 기존 경로 :/smartux_adm/src/main/java/com/dmi/smartux/admin/mainpanel/dao/PanelViewDao.java
	 * @param panel_id			조회하고자 하는 지면이 속한 패널의 panel_id
	 * @param p_title_id		조회하고자 하는 지면이 속한 상위 지면의 title_id
	 * @param title_nm			지면 이름
	 * @return					조회된 지면 이름 갯수
	 * @throws Exception
	 */
	public int getPanelTitleTempTitlenmCnt(String panel_id, String p_title_id, String title_nm) throws Exception{
		// 최상위 레벨의 PanelTitle을 넣을 경우엔 -1로 셋팅한다. 그래서 쿼리문에서 null 조건으로 비교가 될수 있도록 한다
		if("".equals(p_title_id)){
			p_title_id = "-1";
		}
				
		Map<String, String> param = new HashMap<String, String>();
		param.put("panel_id", panel_id);
		param.put("p_title_id", p_title_id);
		param.put("title_nm", title_nm);
		
		int count = (Integer)(getSqlMapClientTemplate().queryForObject("mims.getPanelTitleTempTitlenmCnt", param));
		return count;
		
	}
	
	/**
	 * 패널지면 임시 테이블에 지면 등록시 등록할 지면의 title_id를 조회한다(max 값 조회)
	 * 기존 경로 :/smartux_adm/src/main/java/com/dmi/smartux/admin/mainpanel/dao/PanelViewDao.java
	 * @param panel_id			등록하고자 하는 지면이 속한 패널의 panel_id
	 * @param p_title_id		등록하고자 하는 지면이 속한 상위 지면 title-id
	 * @return					지면의 title_id
	 * @throws Exception
	 */
	public String getMaxTitleid(String panel_id, String p_title_id) throws Exception{
		// 최상위 레벨의 PanelTitle을 넣을 경우엔 -1로 셋팅한다. 그래서 쿼리문에서 null 조건으로 비교가 될수 있도록 한다
		if("".equals(p_title_id)){
			p_title_id = "-1";
		}
		Map<String, String> param = new HashMap<String, String>();
		param.put("pannel_id", panel_id);
		param.put("p_title_id", p_title_id);
		
		String maxcode = (String)(getSqlMapClientTemplate().queryForObject("mims.getPanelTitleTempTitleid", param));
		return GlobalCom.appendLeftZero(maxcode, 2);
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
		
		getSqlMapClientTemplate().insert("mims.insertPanelTitleTemp", param);
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
		
		int result = getSqlMapClientTemplate().update("mims.updatePanelTitleTempCategorySetNull", param);
		return result;
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
		
		
		int result = getSqlMapClientTemplate().update("mims.updatePanelTitleTemp", param);
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
		
		int result = getSqlMapClientTemplate().delete("mims.deletePanelTitleTemp", param);
		
		return result;
	}
	
	/**
	 * 패널지면 포커스저장
	 * 기존 경로 : /smartux_adm/src/main/java/com/dmi/smartux/admin/mainpanel/dao/PanelViewDao.java
	 * @param panel_id			
	 * @param title_id			
	 * @return					
	 * @throws Exception
	 */
	public int focusPanelTitleTemp(String panel_id, String title_id) throws Exception {
		Map<String, String> param = new HashMap<String, String>();
		param.put("pannel_id", panel_id);
		param.put("title_id", title_id);
		int result =getSqlMapClientTemplate().update("mims.focusPanelTitle", param);
		 result = getSqlMapClientTemplate().update("mims.focusPanelTitleTemp", param);
		
		return result;
	}
	
	/**
	 * 패널지면 포커스저장
	 * 기존 경로 : /smartux_adm/src/main/java/com/dmi/smartux/admin/mainpanel/dao/PanelViewDao.java
	 * @param panel_id			
	 * @param title_id			
	 * @return					
	 * @throws Exception
	 */
	public int focusPanelTitleNull(String panel_id) throws Exception {
		Map<String, String> param = new HashMap<String, String>();
		param.put("pannel_id", panel_id);
		int result =getSqlMapClientTemplate().update("mims.focusPanelTitleNull", param);

		return result;
	}
	
	
	
	
	
	/**
	 * 패널지면 임시 테이블의 순서바꾸기 화면에서 사용할 지면의 목록 조회
	 * 기존 경로 : /smartux_adm/src/main/java/com/dmi/smartux/admin/mainpanel/dao/PanelViewDao.java
	 * @param panel_id			순서바꾸기에서 조회되어야 하는 지면들이 속한 패널의 panel_id
	 * @param p_title_id		순서바꾸기에서 조회되어야 하는 지면들이 속한 상위 지면의 title_id
	 * @return					조회된 지면 목록
	 * @throws Exception
	 */
	public List<ViewVO> changePanelTitleTempOrderList(String panel_id, String p_title_id) throws Exception{
		// 최상위 레벨의 지면 순서를 바꿀때는 p_title_id 값이 "" 이므로 -1로 바꾸어서 쿼리에서 적용이 될수 있도록 해준다
		if("".equals(p_title_id)){
			p_title_id = "-1";
		}
				
		Map<String, String> param = new HashMap<String, String>();
		param.put("pannel_id", panel_id);
		param.put("p_title_id", p_title_id);
		
		
		List<ViewVO> result = getSqlMapClientTemplate().queryForList("mims.changePanelTitleTempOrderList", param);
		
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
		
		int result = getSqlMapClientTemplate().update("mims.changePanelTitleTempOrderJob", item);
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
	 * @return
	 * @throws Exception
	 */
	public int updateCategory(String panel_id, String title_id, String category_id, String category_type, String album_cnt, String ui_type, String description, String update_id, String page_type, String page_code, 
			String category_gb, String reps_album_id, String reps_category_id, String trailer_viewing_type, String reps_trailer_viewing_type, String location_code, String location_yn
/* 2019.11.04 : 지면 UI 타입 [paper_ui_type 변수 추가] 추가 Start - 이태광 */					
			, String paper_ui_type, String product_code, String product_code_not, String show_cnt, String terminal_all_yn, String terminal_arr)
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
		
		
		int result = getSqlMapClientTemplate().update("mims.updateCategory", param);
		
		return result;
	}
	
	/**
	 * 패널에 등록된 임시 지면 정보와 상용 지면 정보를 비교하여 변경 된 지면 수 검색한다
	 * 기존 경로 : /smartux_adm/src/main/java/com/dmi/smartux/admin/mainpanel/dao/PanelViewDao.java
	 * @param panel_id	지면이 속한 패널의 panel_id
	 * @return			사용에 반영되지 않은 지면의 수
	 * @throws Exception
	 */
	public int getPanelTitleChangeCount(String panel_id) throws Exception {
		Map<String, String> param = new HashMap<String, String>();
		param.put("pannel_id", panel_id);
		
		int result = (Integer)getSqlMapClientTemplate().queryForObject("mims.getPanelTitleChangeCount", param);
		
		return result;
	}
	
	/**
	 * 패널지면 임시 테이블에서 하위 지면이 더는 없는데 카테고리 코드가 셋팅이 안된 지면들의 이름을 검색한다
	 * 기존 경로 : /smartux_adm/src/main/java/com/dmi/smartux/admin/mainpanel/dao/PanelViewDao.java
	 * @param panel_id	지면이 속한 패널의 panel_id
	 * @return			카테고리 코드가 셋팅이 안된 지면 목록
	 * @throws Exception
	 */
	public List<String> mustCategorySettingList(String panel_id) throws Exception{
		Map<String, String> param = new HashMap<String, String>();
		param.put("pannel_id", panel_id);
		
		List<String> result = getSqlMapClientTemplate().queryForList("mims.mustCategorySettingList", param);
		
		return result;
	}
	
	/**
	 * 패널 테이블 에서 패널 삭제시 패널지면 임시 테이블에서 해당 패널에 속한 지면을 삭제
	 * 기존 경로 : /smartux_adm/src/main/java/com/dmi/smartux/admin/mainpanel/dao/PanelViewDao.java
	 * @param panel_id		삭제하는 패널의 panel_id
	 * @return
	 * @throws Exception
	 */
	public int deletePanelTitleTempByDeletePanel(String panel_id) throws Exception{
		// TODO Auto-generated method stub
		int result = getSqlMapClientTemplate().delete("mims.deletePanelTitleTempByDeletePanel", panel_id);
		
		return result;
	}
	
	/**
	 * 패널지면 임시 테이블에서 해당 패널에 속한 지면을 패널지면 테이블로 등록
	 * 기존 경로 : /smartux_adm/src/main/java/com/dmi/smartux/admin/mainpanel/dao/PanelViewDao.java
	 * @param panel_id			패널의 panel_id
	 * @param create_id			관리자 사이트에 로그인 한 사람의 로그인 id	
	 * @throws Exception
	 */
	public void insertPanelTitle(String panel_id, String create_id) throws Exception {
		// TODO Auto-generated method stub
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("pannel_id", panel_id);
		param.put("create_id", create_id);
		
		getSqlMapClientTemplate().insert("mims.insertPanelTitle", param);
	}
	
	/**
	 * 패널지면 임시 테이블에서 프리뷰 화면을 보여주기 위한 지면 목록 조회
	 * 기존 경로 : /smartux_adm/src/main/java/com/dmi/smartux/admin/mainpanel/dao/PanelViewDao.java
	 * @param panel_id			프리뷰 화면을 보여주기 위한 지면들이 속한 패널의 panel_id
	 * @return					지면 목록
	 * @throws Exception
	 */
	public List<PreviewVO> previewPanelTitleTemp(String panel_id) throws Exception {
		Map<String, String> param = new HashMap<String, String>();
		param.put("pannel_id", panel_id);
		
		List<PreviewVO> result = getSqlMapClientTemplate().queryForList("mims.previewPanelTitleTemp", param);
		
		return result;
	}
	
	/**
	 * 패널지면 테이블에서 프리뷰(상용) 화면을 보여주기 위한 지면 목록 조회
	 * 기존 경로 : /smartux_adm/src/main/java/com/dmi/smartux/admin/mainpanel/dao/PanelViewDao.java
	 * @param panel_id			프리뷰(상용) 화면을 보여주기 위한 지면들이 속한 패널의 panel_id
	 * @return					지면 목록
	 * @throws Exception
	 */
	public List<PreviewVO> previewPanelTitle(String panel_id) throws Exception {
		Map<String, String> param = new HashMap<String, String>();
		param.put("pannel_id", panel_id);
		
		List<PreviewVO> result = getSqlMapClientTemplate().queryForList("mims.previewPanelTitle", param);
		
		return result;
	}
	
	/**
	 * 구분자로 결합된 자체편성의 schedule_code값들이 지면 상용과 지면 임시에서 사용되고 있는지를 조사하여 사용되고 있을 경우 사용되고 있는 자체편성의 정보를 조회한다
	 * /smartux_adm/src/main/java/com/dmi/smartux/admin/schedule/dao/ScheduleDao.java
	 * @param schedule_codes		구분자로 결합된 자체편성의 schedule_code값
	 * @return						자체편성 정보
	 * @throws Exception
	 */
	public List<ScheduleVO> selectUseSchedule(String schedule_codes) throws Exception {
		// TODO Auto-generated method stub
		Map<String, String> param = new HashMap<String, String>();
		param.put("schedule_codes", schedule_codes);
		
		List<ScheduleVO> result = getSqlMapClientTemplate().queryForList("mims.selectUseSchedule", param);
		return result;
	}
	
	/**
	 * 버전을 업데이트 한다.
	 * /smartux_adm/src/main/java/com/dmi/smartux/smartepg/dao/SmartEPGDao.java  getRealRating에서 분리함
	 * @throws Exception
	 */
	public void setMainPanelTitleVersion() throws Exception {
		//버전을 업데이트 한다.
		Map<String, String> param = new HashMap<String, String>();
		param.put("category_type", SmartUXProperties.getProperty("Versionitemtype.code1"));
		getSqlMapClientTemplate().update("mims.setMainPanelTitleVersion", param);
	}
	
}
