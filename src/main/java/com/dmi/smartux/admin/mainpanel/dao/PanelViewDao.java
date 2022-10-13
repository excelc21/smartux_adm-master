package com.dmi.smartux.admin.mainpanel.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.dmi.smartux.admin.abtest.vo.ABTestVO;
import com.dmi.smartux.admin.mainpanel.vo.CategoryVO;
import com.dmi.smartux.admin.mainpanel.vo.FrameVO;
import com.dmi.smartux.admin.mainpanel.vo.LinkInfoVO;
import com.dmi.smartux.admin.mainpanel.vo.PanelSearchVo;
import com.dmi.smartux.admin.mainpanel.vo.PanelVO;
import com.dmi.smartux.admin.mainpanel.vo.ViewVO;
import com.dmi.smartux.common.dao.CommonDao;

@Repository
public class PanelViewDao extends CommonDao {

	/**
	 * 패널의 버전 정보 조회
	 * @return				패널의 버전
	 * @throws Exception
	 */
	public String getPanelVersion() throws Exception {
		String version = (String)(getSqlMapClientTemplate().queryForObject("admin_mainpanel.getPanelVersion"));
		return version;
	}
	
	/**
	 * 패널 테이블에서 패널 목록 조회
	 * @return				조회된 패널 목록
	 * @throws Exception
	 */
	public List<PanelVO> getPanelList() throws Exception {
		// TODO Auto-generated method stub
		List<PanelVO> result = getSqlMapClientTemplate().queryForList("admin_mainpanel.getPanelList");
		return result;
	}
	
	public List<PanelVO> getPanelList(PanelSearchVo vo) throws Exception {
		// TODO Auto-generated method stub
		List<PanelVO> result = getSqlMapClientTemplate().queryForList("admin_mainpanel.getPanelSearchList", vo);
		return result;
	}
	
	public List<ABTestVO> getPanelListWithABTest(PanelSearchVo vo) throws Exception {
		// TODO Auto-generated method stub
		List<ABTestVO> result = getSqlMapClientTemplate().queryForList("admin_mainpanel.getPanelListWithABTest", vo);
		return result;
	}
	
	public PanelVO getPanelId() throws Exception {
		// TODO Auto-generated method stub
		Map<String, String> param = new HashMap<String, String>();
		
		PanelVO result = (PanelVO)(getSqlMapClientTemplate().queryForObject("admin_mainpanel.getPanelId", param));
		return result;
	}

	/**
	 * 패널 테이블에서 상세 조회
	 * @param panel_id		상세 조회하고자 하는 panel_id
	 * @return				상세조회된 패널
	 * @throws Exception
	 */
	public PanelVO viewPanel(String panel_id) throws Exception {
		// TODO Auto-generated method stub
		Map<String, String> param = new HashMap<String, String>();
		param.put("pannel_id", panel_id);
		
		PanelVO result = (PanelVO)(getSqlMapClientTemplate().queryForObject("admin_mainpanel.viewPanel", param));
		return result;
	}
	
	/**
	 * 패널 테이블에서 입력받은 panel_id가 몇개가 있는지를 조회
	 * @param panel_id		갯수를 확인하고자 하는 panel_id
	 * @return				조회된 갯수
	 * @throws Exception
	 */
	public int getPanelidCnt(String panel_id) throws Exception {
		Map<String, String> param = new HashMap<String, String>();
		param.put("pannel_id", panel_id);
		
		int count = (Integer)(getSqlMapClientTemplate().queryForObject("admin_mainpanel.getPanelidCnt", param));
		return count;
	}
	
	
	/**
	 * 패널 테이블에서 입력받은 panel_nm이 몇개가 있는지를 조회
	 * @param panel_nm		갯수를 확인하고자 하는 panel_nm
	 * @return				조회된 갯수
	 * @throws Exception
	 */
	public int getPanelnmCnt(String panel_nm) throws Exception {
		Map<String, String> param = new HashMap<String, String>();
		param.put("pannel_nm", panel_nm);
		
		int count = (Integer)(getSqlMapClientTemplate().queryForObject("admin_mainpanel.getPanelnmCnt", param));
		return count;
	}

	/**
	 * 패널 테이블에 패널 등록
	 * @param panel_id			등록하고자 하는 패널의 panel_id
	 * @param panel_nm			등록하고자 하는 패널의 panel_nm
	 * @param use_yn			패널의 사용여부(Y/N)
	 * @param version			패널의 버전
	 * @param create_id			관리자 사이트에 로그인 한 사람의 로그인 id
	 * @param panel_ui_type		등록하고자 하는 패널의 ui_type
	 * @param panel_image	등록하고자 하는 패널의 이미지
	 * @throws Exception
	 */
	public void insertPanel(String panel_id, String panel_nm, String use_yn, String version, String create_id, String panel_ui_type, String panel_image , String focus_type) throws Exception {
		// TODO Auto-generated method stub
		Map<String, String> param = new HashMap<String, String>();
		param.put("pannel_id", panel_id);
		param.put("pannel_nm", panel_nm);
		param.put("use_yn", use_yn);
		param.put("version", version);
		param.put("create_id", create_id);
		param.put("update_id", create_id);
		param.put("panel_ui_type", panel_ui_type);
		param.put("panel_image", panel_image);
		param.put("focus_type", focus_type);
		
		getSqlMapClientTemplate().insert("admin_mainpanel.insertPanel", param);
	}
	
	/**
	 * 패널 테이블에 패널 수정
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
	public int updatePanel(String panel_id, String newPanel_id, String panel_nm, String newPanel_nm, String use_yn, String update_id, String panel_ui_type, String panel_image , String focus_type , String panel_image_nm) throws Exception {
		// TODO Auto-generated method stub
		Map<String, String> param = new HashMap<String, String>();
		param.put("pannel_id", panel_id);
		param.put("pannel_nm", panel_nm);
		param.put("newPannel_id", newPanel_id);
		param.put("newPannel_nm", newPanel_nm);
		param.put("use_yn", use_yn);
		param.put("update_id", update_id);
		param.put("panel_ui_type", panel_ui_type);
		param.put("panel_image", panel_image);
		param.put("focus_type", focus_type);
		int result = 0;
		if(panel_image_nm.equals("")){
			 result = getSqlMapClientTemplate().update("admin_mainpanel.updatePanelDelImage", param);
		} else {
			 result = getSqlMapClientTemplate().update("admin_mainpanel.updatePanel", param);
		}
		return result;
	}
	
	/**
	 * 패널 테이블에서 패널 삭제
	 * @param panel_id			삭제하고자 하는 패널의 panel_id
	 * @return					삭제된 패널의 갯수
	 * @throws Exception
	 */
	public int deletePanel(String panel_id) throws Exception {
		// TODO Auto-generated method stub
		
		int result = getSqlMapClientTemplate().delete("admin_mainpanel.deletePanel", panel_id);
		return result;
	}
	
	/**
	 * 패널 테이블에서 패널의 버전 정보 수정
	 * @param version			수정된 패널 버전
	 * @param update_id			관리자 사이트에 로그인 한 사람의 로그인 id
	 * @return					수정된 패널 정보의 갯수
	 * @throws Exception
	 */
	public int updatePanelVersion(String version, String update_id) throws Exception {
		Map<String, String> param = new HashMap<String, String>();
		param.put("version", version);
		param.put("update_id", update_id);
		
		int result = getSqlMapClientTemplate().update("admin_mainpanel.updatePanelVersion", param);
		return result;
	}
	
	/**
	 * 패널지면 테이블에서 지면의 버전 정보 수정
	 * CommMimsDao로 이동
	 * @param version			수정된 패널 버전
	 * @param update_id			관리자 사이트에 로그인 한 사람의 로그인 id
	 * @return					수정된 지면 정보의 갯수
	 * @throws Exception
	 */
	/*public int updatePanelTitleVersion(String version, String update_id) throws Exception {
		Map<String, String> param = new HashMap<String, String>();
		param.put("version", version);
		param.put("update_id", update_id);
		
		int result = getSqlMapClientTemplate().update("admin_mainpanel.updatePanelTitleVersion", param);
		return result;
	}*/
	
	/**
	 * 패널지면 임시 테이블에서 지면 목록 조회
	 * @param panel_id			조회하고자 하는 지면들이 속한 panel_id
	 * @return					조회된 지면 목록
	 * @throws Exception
	 */
	public List<ViewVO> getPanelTitleTempList(String panel_id) throws Exception {
		Map<String, String> param = new HashMap<String, String>();
		param.put("pannel_id", panel_id);
		
		List<ViewVO> result = getSqlMapClientTemplate().queryForList("admin_mainpanel.getPanelTitleTempList", param);
		
		return result;
	}
	
	
	/**
	 * 패널지면 임시 테이블에서 지면 목록 조회
	 * @param panel_id			조회하고자 하는 지면들이 속한 panel_id
	 * @return					조회된 지면 목록
	 * @throws Exception
	 */
	public List<ViewVO> getPanelTitleTempListSub(String panel_id , String p_title_id) throws Exception {
		Map<String, String> param = new HashMap<String, String>();
		param.put("pannel_id", panel_id);
		param.put("p_title_id", p_title_id);
		
		
		List<ViewVO> result = getSqlMapClientTemplate().queryForList("admin_mainpanel.getPanelTitleTempListSub", param);
		
		return result;
	}
	
	
	/**
	 * 패널지면 임시 테이블에서 지면 목록 조회
	 * @param panel_id			조회하고자 하는 지면들이 속한 panel_id
	 * @return					조회된 지면 목록
	 * @throws Exception
	 */
	public List<ViewVO> getPanelTitleTempListSubNull(String panel_id) throws Exception {
		Map<String, String> param = new HashMap<String, String>();
		param.put("pannel_id", panel_id);

		
		
		List<ViewVO> result = getSqlMapClientTemplate().queryForList("admin_mainpanel.getPanelTitleTempListSubNull", param);
		
		return result;
	}
	
	
	/**
	 * 패널지면 임시 테이블에서 입력받은 지면의 이름 갯수 조회
	 * CommMimsDao로 이동
	 * @param panel_id			조회하고자 하는 지면이 속한 패널의 panel_id
	 * @param p_title_id		조회하고자 하는 지면이 속한 상위 지면의 title_id
	 * @param title_nm			지면 이름
	 * @return					조회된 지면 이름 갯수
	 * @throws Exception
	 */
	/*public int getPanelTitleTempTitlenmCnt(String panel_id, String p_title_id, String title_nm) throws Exception{
		// 최상위 레벨의 PanelTitle을 넣을 경우엔 -1로 셋팅한다. 그래서 쿼리문에서 null 조건으로 비교가 될수 있도록 한다
		if("".equals(p_title_id)){
			p_title_id = "-1";
		}
				
		Map<String, String> param = new HashMap<String, String>();
		param.put("panel_id", panel_id);
		param.put("p_title_id", p_title_id);
		param.put("title_nm", title_nm);
		
		int count = (Integer)(getSqlMapClientTemplate().queryForObject("admin_mainpanel.getPanelTitleTempTitlenmCnt", param));
		return count;
		
	}*/
	
	/**
	 * 패널지면 임시 테이블에 지면 등록시 등록할 지면의 title_id를 조회한다(max 값 조회)
	 * CommMimsDao로 이동
	 * @param panel_id			등록하고자 하는 지면이 속한 패널의 panel_id
	 * @param p_title_id		등록하고자 하는 지면이 속한 상위 지면 title-id
	 * @return					지면의 title_id
	 * @throws Exception
	 */
	/*public String getMaxTitleid(String panel_id, String p_title_id) throws Exception{
		// 최상위 레벨의 PanelTitle을 넣을 경우엔 -1로 셋팅한다. 그래서 쿼리문에서 null 조건으로 비교가 될수 있도록 한다
		if("".equals(p_title_id)){
			p_title_id = "-1";
		}
		Map<String, String> param = new HashMap<String, String>();
		param.put("pannel_id", panel_id);
		param.put("p_title_id", p_title_id);
		
		String maxcode = (String)(getSqlMapClientTemplate().queryForObject("admin_mainpanel.getPanelTitleTempTitleid", param));
		return GlobalCom.appendLeftZero(maxcode, 2);
	}*/
	
	/**
	 * 패널지면 임시 테이블에 지면 등록
	 * CommMimsDao로 이동
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
	/*public void insertPanelTitleTemp(String panel_id, String title_nm, String title_color, String p_title_id, String use_yn, String create_id, String title_bg_img_file, String bg_img_file, String bg_img_file2) throws Exception {
		// TODO Auto-generated method stub
		
		// 최상위 레벨의 PanelTitle을 넣을 경우엔 -1로 셋팅한다. 그래서 쿼리문에서 -1을 만났을때 decode문을 이용해서 p_title_id 컬럼에 null이 들어가도록 한다
		if("".equals(p_title_id)){
			p_title_id = "-1";
		}
		
		String title_id = getMaxTitleid(panel_id, p_title_id);				// title_id 값을 구한다
		
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
		
		getSqlMapClientTemplate().insert("admin_mainpanel.insertPanelTitleTemp", param);
	}*/
	
	/**
	 * 패널지면 임시 테이블에 지면을 등록시 등록된 지면의 상위 지면의 카테고리 id, SMARTUX 타입, 카테고리 코드에 매핑되는 앨범 갯수, 
	 * UI 타입, 지면 배경 이미지 파일, 지면 설명을 null로 셋팅한다
	 * (상위 지면이 될 경우 이 컬럼의 값들은 하위 지면들이 대신 표현해주기때문에 값들이 의미가 없어진다)
	 * CommMimsDao로 이동
	 * @param panel_id			상위 지면이 속한 panel_id
	 * @param p_title_id		상위 지면 title_id 
	 * @param update_id			관리자 사이트에 로그인 한 사람의 로그인 id
	 * @return					수정된 지면의 갯수
	 * @throws Exception
	 */
	/*public int updatePanelTitleTempCategorySetNull(String panel_id, String p_title_id, String update_id) throws Exception{
		Map<String, String> param = new HashMap<String, String>();
		param.put("update_id", update_id);
		param.put("pannel_id", panel_id);
		param.put("p_title_id", p_title_id);
		
		int result = getSqlMapClientTemplate().update("admin_mainpanel.updatePanelTitleTempCategorySetNull", param);
		return result;
	}*/
	
	/**
	 * 패널지면 임시 테이블에서 지면 상세 조회 
	 * @param panel_id			조회하고자 하는 지면이 속한 panel_id
	 * @param title_id			조회하고자 하는 지면의 title_id
	 * @return					상세조회된 지면
	 * @throws Exception
	 */
	public ViewVO viewPanelTitleTemp(String panel_id, String title_id) throws Exception{
		Map<String, String> param = new HashMap<String, String>();
		param.put("pannel_id", panel_id);
		param.put("title_id", title_id);
		
		ViewVO result = (ViewVO)(getSqlMapClientTemplate().queryForObject("admin_mainpanel.viewPanelTitleTemp", param));
		return result;
		
	}
	
	/**
	 * 패널지면 임시 테이블에서 지면 수정
	 * CommMimsDao로 이동
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
	/*public int updatePanelTitleTemp(String panel_id, String title_id, String title_nm, String title_color, String use_yn, String update_id, String title_bg_img_file, String bg_img_file, String bg_img_file2 , String bg_img_file3) throws Exception {
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
		int result = getSqlMapClientTemplate().update("admin_mainpanel.updatePanelTitleTemp", param);
		return result;
	}*/
	
	/**
	 * 사용안함(ibatis의 admin_mainpanel.xml 에서 getChildPanelTitleTempList 쿼리가 없음)
	 * @param panel_id
	 * @param title_id
	 * @return
	 * @throws Exception
	 */
	public List<ViewVO> getChildPanelTitleTempList(String panel_id, String title_id) throws Exception {
		Map<String, String> param = new HashMap<String, String>();
		param.put("pannel_id", panel_id);
		param.put("title_id", title_id);
		
		List<ViewVO> result = getSqlMapClientTemplate().queryForList("admin_mainpanel.getChildPanelTitleTempList", param);
		
		return result;
	}
	
	/**
	 * 패널지면 임시 테이블에서 지면 삭제
	 * CommMimsDao로 이동
	 * @param panel_id			삭제하고자 하는 지면들이 속한 패널의 panel_id
	 * @param title_id			삭제하고자 하는 지면의 title_id
	 * @return					삭제된 지면의 갯수
	 * @throws Exception
	 */
	/*public int deletePanelTitleTemp(String panel_id, String title_id) throws Exception {
		Map<String, String> param = new HashMap<String, String>();
		param.put("pannel_id", panel_id);
		param.put("title_id", title_id);
		
		int result = getSqlMapClientTemplate().delete("admin_mainpanel.deletePanelTitleTemp", param);
		
		return result;
	}*/
	
	/**
	 * 패널지면 임시 테이블의 순서바꾸기 화면에서 사용할 지면의 목록 조회
	 * CommMimsDao로 이동
	 * @param panel_id			순서바꾸기에서 조회되어야 하는 지면들이 속한 패널의 panel_id
	 * @param p_title_id		순서바꾸기에서 조회되어야 하는 지면들이 속한 상위 지면의 title_id
	 * @return					조회된 지면 목록
	 * @throws Exception
	 */
	/*public List<ViewVO> changePanelTitleTempOrderList(String panel_id, String p_title_id) throws Exception{
		// 최상위 레벨의 지면 순서를 바꿀때는 p_title_id 값이 "" 이므로 -1로 바꾸어서 쿼리에서 적용이 될수 있도록 해준다
		if("".equals(p_title_id)){
			p_title_id = "-1";
		}
				
		Map<String, String> param = new HashMap<String, String>();
		param.put("pannel_id", panel_id);
		param.put("p_title_id", p_title_id);
		
		
		List<ViewVO> result = getSqlMapClientTemplate().queryForList("admin_mainpanel.changePanelTitleTempOrderList", param);
		
		return result;
	}*/
	
	/**
	 * 패널지면 임시 테이블에서 지면의 순서 바꾸기 작업
	 * CommMimsDao로 이동
	 * @param ordered		지면 순서
	 * @param panel_id		지면이 속한 패널의 panel_id
	 * @param title_id		지면의 title_id
	 * @param update_id		관리자 사이트에 로그인 한 사람의 로그인 id
	 * @return
	 * @throws Exception
	 */
	/*public int changePanelTitleTempOrderJob(int ordered, String panel_id, String title_id, String update_id) throws Exception{
		ViewVO item = new ViewVO();
		item.setOrdered(ordered);
		item.setPannel_id(panel_id);
		item.setTitle_id(title_id);
		item.setUpdate_id(update_id);
		
		int result = getSqlMapClientTemplate().update("admin_mainpanel.changePanelTitleTempOrderJob", item);
		return result;
	}*/
	
	/**
	 * 카테고리 코드를 입력받아 하위 카테고리 목록을 조회
	 * @param category_id		카테고리 코드
	 * @return					하위 카테고리 목록
	 * @throws Exception
	 */
	public List<CategoryVO> getCategoryList(String category_id) throws Exception{
				
		Map<String, String> param = new HashMap<String, String>();
		param.put("category_id", category_id);

		List<CategoryVO> result = getSqlMapClientTemplate().queryForList("admin_mainpanel.getCategoryList", param);
		
		return result;
	}
	
	
	
	
	

	/**
	 * 카테고리 구분과 카테고리 코드를 입력받아 하위 카테고리 목록을 조회
	 * @param category_gb		카테고리 구분(I20:IPTV / NSC:HDTV)
	 * @param category_id		카테고리 코드
	 * @return					하위 카테고리 목록
	 * @throws Exception
	 */
	public List<CategoryVO> getCategoryList(String category_gb, String category_id) throws Exception{

		Map<String, String> param = new HashMap<String, String>();
		param.put("category_gb", category_gb);
		param.put("category_id", category_id);

		List<CategoryVO> result = getSqlMapClientTemplate().queryForList("admin_mainpanel.getCategoryList", param);

		return result;
	}

	/*
	public int updateCategory(String panel_id, String title_id, String category_id, String update_id) throws Exception{
		Map<String, String> param = new HashMap<String, String>();
		param.put("pannel_id", panel_id);
		param.put("title_id", title_id);
		param.put("category_id", category_id);
		param.put("update_id", update_id);
		
		int result = getSqlMapClientTemplate().update("admin_mainpanel.updateCategory", param);
		
		return result;
	}
	*/
	
	/**
	 * 패널지면 임시 테이블에서 지면 데이터 설정 작업
	 * CommMimsDao로 이동
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
	/*public int updateCategory(String panel_id, String title_id, String category_id, String category_type, String album_cnt, String ui_type, String description, String update_id, String page_type, String page_code, 
			String category_gb, String reps_album_id, String reps_category_id, String trailer_viewing_type, String reps_trailer_viewing_type, String location_code, String location_yn) throws Exception{
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
		
		int result = getSqlMapClientTemplate().update("admin_mainpanel.updateCategory", param);
		
		return result;
	}*/
	
	/**
	 * 패널지면 임시 테이블에서 하위 지면이 더는 없는데 카테고리 코드가 셋팅이 안된 지면들의 이름을 검색한다
	 * CommMimsDao로 이동
	 * @param panel_id	지면이 속한 패널의 panel_id
	 * @return			카테고리 코드가 셋팅이 안된 지면 목록
	 * @throws Exception
	 */
	/*public List<String> mustCategorySettingList(String panel_id) throws Exception{
		Map<String, String> param = new HashMap<String, String>();
		param.put("pannel_id", panel_id);
		
		List<String> result = getSqlMapClientTemplate().queryForList("admin_mainpanel.mustCategorySettingList", param);
		
		return result;
	}*/
	
	/**
	 * 패널에 등록된 임시 지면 정보와 상용 지면 정보를 비교하여 변경 된 지면 수 검색한다
	 * CommMimsDao로 이동
	 * @param panel_id	지면이 속한 패널의 panel_id
	 * @return			사용에 반영되지 않은 지면의 수
	 * @throws Exception
	 */
/*	public int getPanelTitleChangeCount(String panel_id) throws Exception {
		Map<String, String> param = new HashMap<String, String>();
		param.put("pannel_id", panel_id);
		
		int result = (Integer)getSqlMapClientTemplate().queryForObject("admin_mainpanel.getPanelTitleChangeCount", param);
		
		return result;
	}*/
	
	/**
	 * 패널지면 테이블에서 입력받은 패널에 속한 지면 정보를 삭제
	 * CommMimsDao로 이동
	 * @param panel_id		삭제하고자 하는 지면이 속한 패널의 panel_id
	 * @return
	 * @throws Exception
	 */
	/*public int deletePanelTitle(String panel_id) throws Exception {
		int result = getSqlMapClientTemplate().delete("admin_mainpanel.deletePanelTitle", panel_id);
		
		return result;
	}*/
	
	/**
	 * 패널 테이블 에서 패널 삭제시 패널지면 임시 테이블에서 해당 패널에 속한 지면을 삭제
	 * CommMimsDao로 이동
	 * @param panel_id		삭제하는 패널의 panel_id
	 * @return
	 * @throws Exception
	 */
	/*public int deletePanelTitleTempByDeletePanel(String panel_id) throws Exception{
		// TODO Auto-generated method stub
		int result = getSqlMapClientTemplate().delete("admin_mainpanel.deletePanelTitleTempByDeletePanel", panel_id);
		
		return result;
	}*/
	
	/**
	 * 패널지면 임시 테이블에서 해당 패널에 속한 지면을 패널지면 테이블로 등록
	 * CommMimsDao로 이동
	 * @param panel_id			패널의 panel_id
	 * @param create_id			관리자 사이트에 로그인 한 사람의 로그인 id	
	 * @throws Exception
	 */
	/*public void insertPanelTitle(String panel_id, String create_id) throws Exception {
		// TODO Auto-generated method stub
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("pannel_id", panel_id);
		param.put("create_id", create_id);
		
		getSqlMapClientTemplate().insert("admin_mainpanel.insertPanelTitle", param);
	}*/

	/**
	 * 프리뷰 화면을 보여주기 위한 지면 목록 조회
	 * @param panel_id			프리뷰 화면을 보여주기 위한 지면들이 속한 패널의 panel_id
	 * @param separator			앨범 목록을 보여줄때 앨범과 앨범간의 연결 구분자
	 * @return					지면 목록
	 * @throws Exception
	 */
	
	/**
	 * 패널지면 임시 테이블에서 프리뷰 화면을 보여주기 위한 지면 목록 조회
	 * CommMimsDao로 이동
	 * @param panel_id			프리뷰 화면을 보여주기 위한 지면들이 속한 패널의 panel_id
	 * @return					지면 목록
	 * @throws Exception
	 */
	/*public List<PreviewVO> previewPanelTitleTemp(String panel_id) throws Exception {
		Map<String, String> param = new HashMap<String, String>();
		param.put("pannel_id", panel_id);
		
		List<PreviewVO> result = getSqlMapClientTemplate().queryForList("admin_mainpanel.previewPanelTitleTemp", param);
		
		return result;
	}*/
	
	/**
	 * 패널지면 테이블에서 프리뷰(상용) 화면을 보여주기 위한 지면 목록 조회
	 * CommMimsDao로 이동
	 * @param panel_id			프리뷰(상용) 화면을 보여주기 위한 지면들이 속한 패널의 panel_id
	 * @return					지면 목록
	 * @throws Exception
	 */
	/*public List<PreviewVO> previewPanelTitle(String panel_id) throws Exception {
		Map<String, String> param = new HashMap<String, String>();
		param.put("pannel_id", panel_id);
		
		List<PreviewVO> result = getSqlMapClientTemplate().queryForList("admin_mainpanel.previewPanelTitle", param);
		
		return result;
	}*/
	
	
	/**
	 * 프리뷰에서 자체편성 코드 값을 받아 해당 자체편성에 있는 앨범 이름들을 조회해서 가져온다
	 * @param schedule_code		자체편성 코드
	 * @return					앨범목록
	 * @throws Exception
	 */
	public List<String> getScheduleAlbumList(String schedule_code) throws Exception {
		Map<String, String> param = new HashMap<String, String>();
		param.put("schedule_code", schedule_code);
		
		List<String> result = getSqlMapClientTemplate().queryForList("admin_mainpanel.getScheduleAlbumList", param);
		
		return result;
	}
	
	/**
	 * 프리뷰에서 카테고리 코드 값을 받아 해당 카테고리에 속하는 앨범 목록들을 조회한다
	 * @param category_id		카테고리 코드
	 * @return					앨범목록
	 * @throws Exception
	 */
	public List<String> getCategoryAlbumList(String category_id) throws Exception {
		Map<String, String> param = new HashMap<String, String>();
		param.put("category_id", category_id);
		
		List<String> result = getSqlMapClientTemplate().queryForList("admin_mainpanel.getCategoryAlbumList", param);
		
		return result;
	}

	/**
	 * 프리뷰에서 카테고리 코드 값을 받아 해당 카테고리에 속하는 앨범 목록들을 조회한다
	 * @param category_id		카테고리 코드
	 * @param category_gb		카테고리 구분 (I20 : IPTV / NSC : HDTV)
	 * @return					앨범목록
	 * @throws Exception
	 */
	public List<String> getCategoryAlbumList(String category_id, String category_gb) throws Exception {
		Map<String, String> param = new HashMap<String, String>();
		param.put("category_id", category_id);
		param.put("category_gb", category_gb);

		List<String> result = getSqlMapClientTemplate().queryForList("admin_mainpanel.getCategoryAlbumList", param);

		return result;
	}

	/**
	 * 프리뷰에서 랭킹 코드 값을 받아 해당 랭킹 코드에 속하는 앨범 목록들을 조회한다
	 * @param rank_code			랭킹 코드
	 * @return					앨범 목록
	 * @throws Exception
	 */
	public List<String> getBestVODAlbumList(String rank_code) throws Exception {
		Map<String, String> param = new HashMap<String, String>();
		param.put("rank_code", rank_code);
		
		List<String> result = getSqlMapClientTemplate().queryForList("admin_mainpanel.getBestVODAlbumList", param);
		
		return result;
	}

	/**
	 * 카테고리 코드를 입력받아 카테고리에 대한 이름을 조회
	 * @param category_code		카테고리 코드
	 * @return					카테고리 정보
	 * @throws Exception
	 */
	public CategoryVO getCategoryIdName(String category_code) throws Exception{
		Map<String, String> param = new HashMap<String, String>();
		param.put("category_code", category_code);
		
		CategoryVO result = (CategoryVO)getSqlMapClientTemplate().queryForObject("admin_mainpanel.getCategoryIdName", param);
		
		return result;
	}

	/**
	 * 시스템 타입과 카테고리 코드를 입력받아 카테고리에 대한 이름을 조회
	 *
	 * @param category_gb 카테고리 구분 (I20 : IPTV / NSC : HDTV)
	 * @param category_code 카테고리 코드
	 * @return 카테고리 정보
	 * @throws Exception
	 */
	public CategoryVO getCategoryIdName(String category_gb, String category_code) throws Exception{
		Map<String, String> param = new HashMap<String, String>();
		param.put("category_gb", category_gb);
		param.put("category_code", category_code);

		CategoryVO result = (CategoryVO)getSqlMapClientTemplate().queryForObject("admin_mainpanel.getCategoryIdName", param);

		return result;
	}
	
	/**
	 * 지면 데이터 설정에서 지면의 UI 타입과 지면 설명을 조회
	 * @param panel_id		지면이 속한 패널의 panel_id
	 * @param title_id		지면의 title_id
	 * @return				지면의 UI 타입과 지면 설명이 있는 지면 정보
	 * @throws Exception
	 */
	public ViewVO getUITypeDescription(String panel_id, String title_id) throws Exception {
		Map<String, String> param = new HashMap<String, String>();
		param.put("pannel_id", panel_id);
		param.put("title_id", title_id);
		
		ViewVO result = (ViewVO)getSqlMapClientTemplate().queryForObject("admin_mainpanel.getUITypeDescription", param);
		
		return result;
	}
	
	/**
	 * 이미지서버 URL 조회
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public String getImageServerURL(String type) throws Exception {
		Map<String, String> param = new HashMap<String, String>();
		param.put("type", type);
		
		String imageServerUrl = (String)(getSqlMapClientTemplate().queryForObject("admin_mainpanel.getImageServerURL", param));
		return imageServerUrl;
	}
	
	/**
	 * 앨범명 조회
	 * @param album_id
	 * @return
	 * @throws Exception
	 */
	public String getAlbumName(String album_id) throws Exception {
		return (String)getSqlMapClientTemplate().queryForObject("admin_mainpanel.getAlbumName", album_id);
	}
	
	/**
	 * 패널UI타입 리스트 조회
	 * @param frame_type	30: 패널UI타입
	 * @return
	 * @throws Exception
	 */
	public List<FrameVO> getPanelUiTypeList(String frame_type, int pageNum, int pageSize) throws DataAccessException{
		String startNum = Integer.toString(pageNum*pageSize-pageSize+1); 
		String endNum 	= Integer.toString(Integer.parseInt(startNum)+pageSize-1);
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("frame_type", frame_type);
		param.put("startNum", startNum);
		param.put("endNum", endNum);
		
		List<FrameVO> result = getSqlMapClientTemplate().queryForList("admin_mainpanel.getPanelUiTypeList", param);
		return result;
	}
	
	/**
	 * 페이징을 위한 전체 카운트
	 * @return
	 * @throws Exception
	 */
	public int getPanelUiTypeCnt(String frame_type) throws DataAccessException{
		Map<String, String> param = new HashMap<String, String>();
		param.put("frame_type", frame_type);
		
		int result = (Integer)getSqlMapClientTemplate().queryForObject("admin_mainpanel.getPanelUiTypeCnt", param);
		return result;
	}
	
	/**
	 * frame 테이블에 frame 등록
	 * @param frame_flag
	 * @param frame_type
	 * @param frame_nm
	 * @param img_file
	 * @param use_yn
	 * @param create_id
	 * @throws Exception
	 */
	public void insertPanelUiType(String frame_flag, String frame_type, String frame_nm, String img_file, String use_yn, String create_id) throws DataAccessException{
		Map<String, String> param = new HashMap<String, String>();
		param.put("frame_flag", frame_flag);
		param.put("frame_type", frame_type);
		param.put("frame_nm", frame_nm);
		param.put("img_file", img_file);
		param.put("use_yn", use_yn);
		param.put("del_yn", "N");
		param.put("create_id", create_id);
		
		getSqlMapClientTemplate().insert("admin_mainpanel.insertPanelUiType", param);
	}
	
	/**
	 * frame update/delete(use_yn 변경)
	 * @param frame_nm		프레임 명
	 * @param img_file		이미지 파일명
	 * @param frame_type	30: 패널UI타입
	 * @param use_yn		사용여부
	 * @param update_id		업데이트 요청 ID
	 * @return
	 */
	public int updatePanelUiType(String frame_type_code, String frame_nm, String img_file, String use_yn, String update_id)throws DataAccessException{

		FrameVO frameVO = new FrameVO();
		
		String del_yn = "N";
		
		if(!frame_nm.equals("")){				
			frameVO.setFrame_nm(frame_nm);	
		}
		
		if(!img_file.equals("")){			
			frameVO.setImg_file(img_file);
		}

		frameVO.setUse_yn(use_yn);
		frameVO.setDel_yn(del_yn);
		frameVO.setUpdate_id(update_id);
		frameVO.setFrame_type_code(frame_type_code);
		//frameVO.setData_type(data_type);
		
		int result = getSqlMapClientTemplate().update("admin_mainpanel.updatePanelUiType", frameVO);
		return result;
	}
	
	/**
	 * frame 개별 조회
	 * @param frame_type_code	프레임 코드(pk)
	 * @return
	 * @throws Exception
	 */
	public FrameVO viewPanelUiType(String frame_type_code) throws DataAccessException{
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("frame_type_code", frame_type_code);
		
		FrameVO result = (FrameVO)getSqlMapClientTemplate().queryForObject("admin_mainpanel.viewPanelUiTypeList", param);
		return result;
	}
	
	/**
	 * 패널UI타입 리스트 조회
	 * @param frame_type	30: 패널UI타입
	 * @return
	 * @throws Exception
	 */
	public List<FrameVO> getPanelUiTypeSelect(String frame_type) throws DataAccessException{
		Map<String, String> param = new HashMap<String, String>();
		param.put("frame_type", frame_type);
		
		List<FrameVO> result = getSqlMapClientTemplate().queryForList("admin_mainpanel.getPanelUiTypeSelect", param);
		return result;
	}
	
	/**
	 * 패널지면 포커스저장
	 * CommMimsDao로 이동
	 * @param panel_id			
	 * @param title_id			
	 * @return					
	 * @throws Exception
	 */
	/*public int focusPanelTitleTemp(String panel_id, String title_id) throws Exception {
		Map<String, String> param = new HashMap<String, String>();
		param.put("pannel_id", panel_id);
		param.put("title_id", title_id);
		int result =getSqlMapClientTemplate().update("admin_mainpanel.focusPanelTitle", param);
		 result = getSqlMapClientTemplate().update("admin_mainpanel.focusPanelTitleTemp", param);
		
		return result;
	}*/
	
	/**
	 * 패널지면 포커스저장
	 *  CommMimsDao로 이동
	 * @param panel_id			
	 * @param title_id			
	 * @return					
	 * @throws Exception
	 */
	/*public int focusPanelTitleNull(String panel_id) throws Exception {
		Map<String, String> param = new HashMap<String, String>();
		param.put("pannel_id", panel_id);
		int result =getSqlMapClientTemplate().update("admin_mainpanel.focusPanelTitleNull", param);

		
		return result;
	}*/
	

	/**
	 * 지면전체조회
	 * @return
	 * @throws Exception
	 */
	public List<ViewVO> getPanelTitleTempList() {
		return getSqlMapClientTemplate().queryForList("admin_mainpanel.getPanelTitleTempListAll");
	}
	/**
	 * 순서 변경
	 *
	 * @param PanelVO  객체
	 * @throws DataAccessException
	 */
	public void changeOrder(PanelVO panelVO) throws DataAccessException {
		getSqlMapClientTemplate().update("admin_mainpanel.changeOrder", panelVO);
	}
	
	/**
	 * OTT/추천앱 팝업조회
	 *
	 * @param PanelVO  객체
	 * @throws DataAccessException
	 */
	public List<CategoryVO> getOtCategorySelect2(LinkInfoVO param) {
		return (List<CategoryVO>) getSqlMapClientTemplate().queryForList("admin_mainpanel.getOtCategorySelect2", param);
	}

	/**
	 * OTT/추천앱 팝업조회
	 *
	 * @param PanelVO  객체
	 * @throws DataAccessException
	 */
	public List<CategoryVO> getOtCategorySelect1() {
		return (List<CategoryVO>) getSqlMapClientTemplate().queryForList("admin_mainpanel.getOtCategorySelect1");
	}
}
