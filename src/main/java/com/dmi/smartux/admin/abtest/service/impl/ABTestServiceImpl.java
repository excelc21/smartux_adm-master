package com.dmi.smartux.admin.abtest.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.dmi.smartux.admin.abtest.dao.ABTestDao;
import com.dmi.smartux.admin.abtest.service.ABTestService;
import com.dmi.smartux.admin.abtest.vo.ABTestSearchVO;
import com.dmi.smartux.admin.abtest.vo.ABTestVO;
import com.dmi.smartux.admin.mainpanel.dao.PanelViewIptvDao;
import com.dmi.smartux.admin.mainpanel.service.PanelViewService;
import com.dmi.smartux.admin.mainpanel.vo.PanelTitleTreamVO;
import com.dmi.smartux.admin.mainpanel.vo.ViewVO;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.DateUtils;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.util.HTMLCleaner;
import com.dmi.smartux.common.util.DateUtils.DatePattern;

@Service
public class ABTestServiceImpl implements ABTestService{
	
	private Logger logger = LoggerFactory.getLogger("ABTestService");

	@Autowired
	ABTestDao dao;
	
	@Autowired
	ABMSComponent abmsCompo;
	
	@Autowired
	PanelViewIptvDao panelViewIptvDao;
	
	@Autowired
	PanelViewService panelViewService;
	
	@Override
	public Map<String, Object> getABMSCall(ABTestSearchVO searchVo) throws Exception {
		Map<String, Object> abmsResultMap = new HashMap<String, Object>();
		
		List<ABTestVO> abmsList = new ArrayList<ABTestVO>();
		
		//AMBS 호출
		Map<String, String> params = new HashMap<String, String>();
		params.put("reqSystem", "1");	// MIMS
		params.put("screenType", "1");	// IPTV
		//params.put("serviceType", "I");	// IPTV
		
		if(StringUtils.isNotBlank(searchVo.getFindName())){
			if("test_id".equals(searchVo.getFindName())){
				params.put("testId", searchVo.getFindValue());
			}else if("test_name".equals(searchVo.getFindName())){
				params.put("testName", searchVo.getFindValue());
			}else if("variation_id".equals(searchVo.getFindName())){
				params.put("variationId", searchVo.getFindValue());
			}else if("variation_name".equals(searchVo.getFindName())){
				params.put("variationName", searchVo.getFindValue());
			}
		}
		
		if("0".equals(searchVo.getOffset())){
			params.put("offset", searchVo.getOffset());
		}else{
			params.put("offset", String.valueOf((searchVo.getPageNum()-1)*searchVo.getPageSize()));
		}
		params.put("limit", String.valueOf(searchVo.getPageSize()));
		
		String result = abmsCompo.callABMSVariation(params);
//		String result = "{\"resCode\":0,\"resMsg\":\"성공\",\"totalCount\":1,\"list\":[{\"testId\":\"abms-20210308-d001\",\"testName\":\"개발abtest1\",\"testStatus\":0,\"regDatetime\":\"2021-03-08 15:00:00\","+
//			"\"startDatetime\":\"2021-03-15 15:00:00\",\"endDatetime\":\"2021-04-01 15:00:00\",\"variationId\":\"abms-20210308-d001-var1\",\"variationName\":\"개발abtest-var1\",\"elementId\":\"G01254\",\"elementName\":\"대여1\" "+
//			"},{\"testId\":\"abms-20210308-d001\",\"testName\":\"개발abtest1\",\"testStatus\":0,\"regDatetime\":\"2021-03-08 15:00:00\",\"startDatetime\":\"2021-03-15 15:00:00\",\"endDatetime\":\"2021-04-01 15:00:00\", " +
//			"\"variationId\":\"abms-20210308-d001-var2\",\"variationName\":\"개발abtest-var2\",\"elementId\":\"G01255\",\"elementName\":\"대여2\"}	]}";
		
		if(StringUtils.isNotBlank(result)){
			JSONObject jsonObject = (JSONObject) JSONValue.parse(result);
			
			if(jsonObject != null){
				String resCode = GlobalCom.getJsonString(jsonObject, "resCode");
				
				abmsResultMap.put("resCode", resCode);
				abmsResultMap.put("resMsg", GlobalCom.getJsonString(jsonObject, "resMsg"));
				abmsResultMap.put("totalCount", GlobalCom.getJsonString(jsonObject, "totalCount"));
				
				if(StringUtils.isNotBlank(resCode) && "0".equals(resCode)){
					JSONArray variationArray = (JSONArray) jsonObject.get("list");
					if(!CollectionUtils.isEmpty(variationArray)){
						for(int i=0;i<variationArray.size();i++){
							try{
								JSONObject o = (JSONObject) variationArray.get(i);
								if(o != null){
									ABTestVO variationVo = new ABTestVO();
									variationVo.setTest_id(HTMLCleaner.clean(GlobalCom.getJsonString(o, "testId")));
									variationVo.setTest_name(HTMLCleaner.clean(GlobalCom.getJsonString(o, "testName")));
									variationVo.setAbms_status(HTMLCleaner.clean(GlobalCom.getJsonString(o, "testStatus")));
									variationVo.setStart_date_time(HTMLCleaner.clean(GlobalCom.getJsonString(o, "startDatetime")));
									variationVo.setEnd_date_time(HTMLCleaner.clean(GlobalCom.getJsonString(o, "endDatetime")));
									variationVo.setVariation_id(HTMLCleaner.clean(GlobalCom.getJsonString(o, "variationId")));
									variationVo.setVariation_name(HTMLCleaner.clean(GlobalCom.getJsonString(o, "variationName")));
									variationVo.setAbms_status_name(abmsCompo.getStatusName(HTMLCleaner.clean(GlobalCom.getJsonString(o, "testStatus"))));
									abmsList.add(variationVo);
								}
								
							}catch (Exception e){
								logger.warn("[AMBS 호출 오류][WARN][JSON파싱 오류][" + e + "]" );
							}
						}
					}
				}else{
					logger.warn("[AMBS 호출 오류][WARN][RESULT][" + result + "]" );
				}
			}
		}
		abmsResultMap.put("abmsList", abmsList);
		
		return abmsResultMap;
	}

	@Override
	public List<ABTestVO> getABTestList(List<ABTestVO> abmsList) throws Exception {
		if(!CollectionUtils.isEmpty(abmsList)){
			for(ABTestVO vo : abmsList){
				ABTestVO abTestVo = dao.getABTestList(vo.getVariation_id());
				if(abTestVo != null){
					vo.setOrg_mims_id(abTestVo.getOrg_mims_id());
					vo.setMims_id(abTestVo.getMims_id());
					vo.setPannel_nm(abTestVo.getPannel_nm());
					vo.setMod_id(abTestVo.getMod_id());
					vo.setMod_name(abTestVo.getMod_name());
					vo.setStatus(abTestVo.getStatus());
					vo.setTest_type(abTestVo.getTest_type());
					vo.setD_mims_id(abTestVo.getD_mims_id());
				}
			}
		}
		return abmsList;
	}
	
	/**
	 * 2021.04.20 AB테스트 MIMS
	 * 패널 목록
	 */
	@Override
	public List<HashMap<String, String>> getPanelList(ABTestSearchVO searchVo) throws Exception {
		return dao.getPanelList(searchVo);
	}

	/**
	 * 2021.04.20 AB테스트 MIMS
	 * AB테스트 등록
	 */
	@Override
	@Transactional
	public void insertABTest(String org_mims_id, String test_id, String mtype, String admin_id, String test_type) throws Exception {
		
		try{
			//ABMS 호출
			ABTestSearchVO searchVo = new ABTestSearchVO();
			//페이지
			searchVo.setPageSize(0);
			searchVo.setPageNum(1);
			searchVo.setFindName("test_id");
			searchVo.setFindValue(test_id);
			Map<String, Object> abmsMap = getABMSCall(searchVo);
			List<String> variationIdList = new ArrayList<String>();
			
			if(!CollectionUtils.isEmpty(abmsMap)){
				List<ABTestVO> abmsList = (List<ABTestVO>) abmsMap.get("abmsList");
				if(!CollectionUtils.isEmpty(abmsList)){
					for(ABTestVO vo : abmsList){
						variationIdList.add(vo.getVariation_id());
					}
					
					logger.info("[AB테스트 등록][ABTestPaperServiceImpl][insertABTest][ABMS 호출 완료][variationIdList size: " + variationIdList.size() +"]");
				}
			}else{
				logger.info("[AB테스트 등록][ABTestPaperServiceImpl][insertABTest][ABMS 호출 결과 없음]");
			}
			
			if(!CollectionUtils.isEmpty(variationIdList)){
				
				ABTestVO vo = new ABTestVO();
				vo.setStatus("D");
				vo.setTest_id(test_id);
				vo.setTest_type(test_type);
				vo.setAbtest_yn("N");
				if (test_type.equals("O")) { //순서 테스트인 경우 
					vo.setAbtest_yn("Y");
				}
				//Test등록
				dao.insertABTestInfo(vo);
				
				logger.info("[AB테스트 등록][ABTestPaperServiceImpl][insertABTest][Test 등록완료]");
				vo.setCreate_id(admin_id);
				vo.setUpdate_id(admin_id);
				vo.setOrg_mims_id(org_mims_id);
				
				//개인화스쿼드_IPTV
				List<PanelTitleTreamVO> terminal_list= panelViewService.getLikePaperTerminal(vo.getOrg_mims_id());
				
				for(String variation_id: variationIdList){
					
					dao.insertABPanel(vo);
					
					vo.setTest_id(test_id);
					vo.setVariation_id(variation_id);
					vo.setMtype(mtype);
					vo.setMims_id(vo.getPannel_id());
					
					dao.insertABVariationInfo(vo);
					
					dao.insertABPaper(vo);
					
					//개인화스쿼드_IPTV
					if (!CollectionUtils.isEmpty(terminal_list)) {
						for (int i = 0; i < terminal_list.size(); i++) {
							String paperId = terminal_list.get(i).getPaper_id();
							String unit = terminal_list.get(i).getTerm_model();
							unit = unit.trim();
							Map<String, String> param2 = new HashMap<String, String>();
							param2.put("paper_id", vo.getPannel_id()+"_"+paperId);
							param2.put("term_model", unit);
							panelViewIptvDao.insertPaperTerminal(param2);
						}
					}
				}
				
				//대조군 등록
				ABTestVO testVo = vo;
				
				//알파벳
				char aString = testVo.getPannel_id().charAt(0);
				String num = testVo.getPannel_id().substring(1);
				
				if (num.equals("999")) {
					aString = (char) (aString+1);
					num = "001";
				} else {
					num = String.valueOf(Integer.parseInt(num)+1);
					
					if (num.length() != 3) {
						String lapd = "";
						for (int i = 0; i < 3- num.length(); i++) {
							lapd = lapd+ "0";
						}
						num = lapd+num;
					}
				}
				
				testVo.setPannel_id(String.valueOf(aString) + num);
				
				dao.insertABPanelTestD(testVo);
				testVo.setVariation_id("O"+test_id);
				testVo.setMims_id(testVo.getPannel_id());
				dao.insertABVariationInfo(testVo);
				
				dao.insertABPaper(testVo);
				
				//개인화스쿼드_IPTV
				if (!CollectionUtils.isEmpty(terminal_list)) {
					for (int i = 0; i < terminal_list.size(); i++) {
						String paperId = terminal_list.get(i).getPaper_id();
						String unit = terminal_list.get(i).getTerm_model();
						unit = unit.trim();
						Map<String, String> param2 = new HashMap<String, String>();
						param2.put("paper_id", testVo.getPannel_id()+"_"+paperId);
						param2.put("term_model", unit);
						panelViewIptvDao.insertPaperTerminal(param2);
					}
				}
				
				
				logger.info("[AB테스트 등록][ABTestPaperServiceImpl][insertABTest][panel, variation 정보 등록완료]");
				
			}else{
				logger.info("[AB테스트 등록][ABTestPaperServiceImpl][insertABTest][variationIdList 데이터 없음]");
			}
		}catch(Exception e){
			logger.error("[AB테스트 등록][ABTestPaperServiceImpl][insertABTest][ERROR][" + e +"]");
			throw e;
		}
		
	}

	/**
	 * 2021.04.23 AB테스트 MIMS
	 * AB테스트 지면 목록
	 */
	@Override
	public List<ViewVO> getABTestPaperList(String mims_id) throws Exception {
		return dao.getABTestPaperList(mims_id);
	}
	
	/**
	 * 2021.04.23 AB테스트 MIMS
	 * AB테스트 오리지날 지면 목록
	 */
	@Override
	public List<ViewVO> getABTestOrgPaperList(String test_id, String variation_id) throws Exception {
		
		return dao.getABTestOrgPaperList(test_id, variation_id);
	}
	
	/**
	 * 2021.04.27 AB테스트 MIMS
	 * AB테스트 variation 정보 조회
	 */
	@Override
	public ABTestVO getVariationInfo(String variation_id) throws Exception {
		return dao.getVariationInfo(variation_id);
	}
	
	/**
	 * 2021.04.27 AB테스트 MIMS
	 * AB테스트 variation 정보 조회
	 */
	@Override
	public ABTestVO getMinVariationId(String test_id) throws Exception {
		return dao.getMinVariationId(test_id);
	}

	/**
	 * 2021.04.27 AB테스트 MIMS
	 * AB테스트 패널 복사
	 */
	@Override
	@Transactional
	public void updateABTestPaper(String mims_id, String loginUser, String new_panel_id) throws Exception {
		//AB 지면 테이블 삭제
		dao.deleteABTestPaperList(mims_id);
		
		ABTestVO vo = new ABTestVO();
		vo.setPannel_id(mims_id);
		vo.setCreate_id(loginUser);
		vo.setUpdate_id(loginUser);
		vo.setOrg_mims_id(new_panel_id);
		vo.setAbtest_yn("N");

		//선택한 패널ID로 AB테이블 insert
		dao.insertABPaper(vo);
		
		//개인화스쿼드_IPTV
		panelViewIptvDao.deleteLikePaperTerminal(vo.getPannel_id());
		
		List<PanelTitleTreamVO> terminal_list= panelViewService.getLikePaperTerminal(vo.getOrg_mims_id());
		
		if (!CollectionUtils.isEmpty(terminal_list)) {
			for (int i = 0; i < terminal_list.size(); i++) {
				String paperId = terminal_list.get(i).getPaper_id();
				String unit = terminal_list.get(i).getTerm_model();
				unit = unit.trim();
				Map<String, String> param2 = new HashMap<String, String>();
				param2.put("paper_id", vo.getPannel_id()+"_"+paperId);
				param2.put("term_model", unit);
				panelViewIptvDao.insertPaperTerminal(param2);
			}
		}
	}

	
	/* (non-Javadoc)
	 * 
	 */
	@Override
	public List<ViewVO> getPaperOrder(String mims_id, String p_title_id) throws Exception {
		return dao.getPaperOrder(mims_id, p_title_id);
	}
	
	/* (non-Javadoc)
	 * 
	 */
	@Override
	@Transactional
	public void updatePaperOrder(String panel_id, String[] title_ids, String update_id) throws Exception {
		int ordered = 1;
		for(String title_id : title_ids){
			dao.updatePaperOrder(ordered++, panel_id, title_id, update_id);
		}
	}
	
	/**
	 * 2021.04.29 AB테스트 MIMS
	 * 지면 목록 조회
	 */
	@Override
	public List<ViewVO> getPaperList(ABTestSearchVO searchVo) throws Exception {
		return dao.getPaperList(searchVo);
	}
	
	/**
	 * @param mims_id
	 * @param org_title_id
	 * @param org_panel_id
	 * @param loginUser
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void insertABTestPaper(String mims_id, String org_title_id, String org_panel_id, String loginUser) throws Exception {
		
		ABTestVO vo = new ABTestVO();
		vo.setCreate_id(loginUser);
		vo.setUpdate_id(loginUser);
		vo.setPannel_id(mims_id);
		vo.setOrg_title_id(org_title_id);
		vo.setOrg_mims_id(org_panel_id);
		vo.setAbtest_yn("Y");

		//맥스
		int ordered = dao.selectPaperMaxOrd(mims_id);
		//지면 등록
		dao.insertABPaper(vo);
		//개인화스쿼드_IPTV
		List<String> terminal_list = panelViewService.getPaperTerminal(vo.getOrg_mims_id()+"_"+ vo.getOrg_title_id());
		if (!CollectionUtils.isEmpty(terminal_list)) {
			
			panelViewIptvDao.deletePaperTerminal(vo.getPannel_id()+"_"+vo.getOrg_mims_id()+"_"+ vo.getOrg_title_id());

			for(String unit : terminal_list) {
				unit = unit.trim();
				Map<String, String> param2 = new HashMap<String, String>();
				param2.put("paper_id", vo.getPannel_id()+"_"+vo.getOrg_mims_id()+"_"+ vo.getOrg_title_id());
				param2.put("term_model", unit);
				panelViewIptvDao.insertPaperTerminal(param2);
			}
		}
		//순서 마지막으로 변경
		dao.updatePaperOrder(ordered, mims_id, org_panel_id + "_" + org_title_id, loginUser);
	}
	
	/**
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@Override
	public int selectPaperCount(ViewVO vo) throws Exception {
		return dao.selectPaperCount(vo);
	}
	
	/**
	 * 2021.05.04 AB테스트 MIMS
	 * AB테스트 지면 삭제
	 */
	@Override
	public void deleteABTestPaper(String[] title_id_arr, String mims_id) throws Exception {
		
		for(String s : title_id_arr){
			ViewVO vo = new ViewVO();
			vo.setTitle_id(s);
			vo.setPannel_id(mims_id);
			dao.deleteABTestPaper(vo);
			logger.info("[AB테스트 지면 삭제][ABTestPaperServiceImpl][deleteABTestPaper][지면 삭제][mims_id: " + mims_id + "][paper_id: " + s + "]");
		}
	}
	
	/* 
	 * 지면상세조회
	 */
	@Override
	@Transactional(readOnly=true)
	public ViewVO getPaperInfo(String panel_id, String title_id)	throws Exception {
		return dao.getPaperInfo(panel_id, title_id);
	}
	
	/* 
	 * 지면상세정보업데이트
	 */
	@Override
	@Transactional
	public int updatePaperInfo(String variation_id, String panel_id, String title_id, String title_nm, String title_color, String use_yn, String update_id, String title_bg_img_file, String bg_img_file, String bg_img_file2, String bg_img_file3 , String bg_img_file4, String abtest_yn) throws Exception {
		
		int result;
		
		result = dao.updatePaperInfo(panel_id, title_id, title_nm, title_color, use_yn, update_id, title_bg_img_file, bg_img_file, bg_img_file2 ,bg_img_file3 , bg_img_file4, abtest_yn);
		
		ABTestVO vo = new ABTestVO();
		vo.setMod_id(title_id);
		vo.setMod_name(title_nm);
		vo.setVariation_id(variation_id);
		dao.updateABVariationInfo(vo);
		
		ABTestVO dMimsVo = dao.getDmimsIdVariationInfo(variation_id);
		if (dMimsVo.getMims_id() != null) {
			result = dao.updatePaperInfoDMims(dMimsVo.getMims_id(), title_id, abtest_yn);
			
			dMimsVo.setMod_id(title_id);
			dMimsVo.setMod_name(title_nm);
			dao.updateABVariationInfo(dMimsVo);
		}
		
		
		return result;
	}
	
	/**
	 * 동일지면이름 체크
	 * @param panel_id
	 * @param p_title_id
	 * @param title_nm
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional(readOnly=true)
	public int getPaperInfoTitleNmCnt(String panel_id, String p_title_id, String title_nm) throws Exception {
		return dao.getPaperInfoTitleNmCnt(panel_id, p_title_id, title_nm);
	}
	
	/**
	 * 지면데이터정보
	 * @param panel_id
	 * @param title_id
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional(readOnly=true)
	public ViewVO getPaperDataType(String panel_id, String title_id) throws Exception {
		// TODO Auto-generated method stub
		return dao.getPaperDataType(panel_id, title_id);
	}
	
	/**
	 * @param panel_id
	 * @param title_id
	 * @param category_id
	 * @param category_type
	 * @param album_cnt
	 * @param ui_type
	 * @param description
	 * @param update_id
	 * @param page_type
	 * @param page_code
	 * @param category_gb
	 * @param reps_album_id
	 * @param reps_category_id
	 * @param trailer_viewing_type
	 * @param reps_trailer_viewing_type
	 * @param location_code
	 * @param location_yn
	 * @param paper_ui_type
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional
	public int updatePaperDataType(String variation_id, String panel_id, String title_id, String title_nm, String category_id, String category_type, String album_cnt, String ui_type, String description, String update_id, String page_type, 
			String page_code, String category_gb, String reps_album_id, String reps_category_id, String trailer_viewing_type, String reps_trailer_viewing_type, String location_code, String location_yn, String paper_ui_type, String abtest_yn, String product_code, String product_code_not, String show_cnt, String terminal_all_yn, String terminal_arr) throws Exception {
		
		int result;
		
		result = dao.updatePaperDataType(panel_id, title_id, category_id, category_type, album_cnt, ui_type, description, update_id, page_type, page_code, category_gb, reps_album_id, 
				reps_category_id, trailer_viewing_type, reps_trailer_viewing_type, location_code, location_yn ,paper_ui_type, abtest_yn, product_code, product_code_not, show_cnt, terminal_all_yn, terminal_arr);
		
		panelViewIptvDao.deletePaperTerminal(panel_id+"_"+title_id);
		 
		 if("N".equals(terminal_all_yn)) {
			String terminal[] = terminal_arr.split(",");
			for(String unit : terminal) {
				unit = unit.trim();
				Map<String, String> param2 = new HashMap<String, String>();
				param2.put("paper_id", panel_id+"_"+title_id);
				param2.put("term_model", unit);
				panelViewIptvDao.insertPaperTerminal(param2);
			}
		}
		
		ABTestVO vo = new ABTestVO();
		vo.setMod_id(title_id);
		vo.setMod_name(title_nm);
		vo.setVariation_id(variation_id);
		dao.updateABVariationInfo(vo);
		
		ABTestVO dMimsVo = dao.getDmimsIdVariationInfo(variation_id);
		if (dMimsVo.getMims_id() != null) {
			result = dao.updatePaperInfoDMims(dMimsVo.getMims_id(), title_id, abtest_yn);
			
			dMimsVo.setMod_id(title_id);
			dMimsVo.setMod_name(title_nm);
			dao.updateABVariationInfo(dMimsVo);
		}
		
		return result;
	}
	
	/**
	 * 2021.05.12 AB테스트 MIMS
	 * AB테스트 variation_id 별 status 상태 확인
	 * false : 수정 불가 / true : 수정 가능
	 */
	@Override
	public boolean checkABMSStatus(String variation_id, List<ABTestVO> abmsList) throws Exception {
		String abms_status = "";
		for(ABTestVO abVariationVo : abmsList){
			if(variation_id.equals(abVariationVo.getVariation_id())){
				abms_status = abVariationVo.getAbms_status();
				break;
			}
		}
		
		//ABMS 상태 체크
		if(StringUtils.isNotBlank(abms_status)){
			String abms_status_info = StringUtils.defaultIfEmpty(SmartUXProperties.getProperty("abms.upt.status"), "120");
			
			String[] array = abms_status_info.split("\\|");
			if(array != null){
				for(String s : array){
					if(abms_status.equals(s)){
						return true;
					}
				}
			}
		}
		
		return false;
	}

	/**
	 * 2021.05.13 AB테스트 MIMS
	 * AB테스트 지면 최종완료
	 */
	@Override
	public Map<String, String> finishABTestPaper(String test_id) throws Exception {
		Map<String, String> resultMap = new HashMap<String, String>();

		//연타?
		ABTestVO vo = dao.getABTestStatus(test_id);
		if(StringUtils.isNotBlank(vo.getMod_date())){
			Date modDate = DateUtils.convertToDate(vo.getMod_date(), DatePattern.yyyyMMddHHmmss_1);
			long elapsedTime = DateUtils.dateCompare(System.currentTimeMillis(), modDate.getTime());
			long waitTime = Long.parseLong(StringUtils.defaultIfEmpty(SmartUXProperties.getProperty("call.abms.waittime"), "600"));
			if(elapsedTime < waitTime){
				long remainingTime = waitTime - elapsedTime;
				resultMap.put("resCode", SmartUXProperties.getProperty("flag.cache.reload.waittime"));
				resultMap.put("resMsg", "갱신 대기 시간 미만 (" + remainingTime + ") 초 후에 다시 시도하세요.");
				return resultMap;
			}
		}
		
		//AMBS 호출
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("reqSystem", new Integer(1));	
		params.put("testId", test_id);
		params.put("sendMail", new Integer(1));
		params.put("categoryYn", null);
				
		JSONObject json = new JSONObject(params);
		
		String result = abmsCompo.callABMSOrganize(json.toString());
		
		if(StringUtils.isNotBlank(result)){
			JSONObject jsonObject = (JSONObject) JSONValue.parse(result);
			
			if(jsonObject != null){
				String resCode = GlobalCom.getJsonString(jsonObject, "resCode");
				String resMsg = GlobalCom.getJsonString(jsonObject, "resMsg");
				
				resultMap.put("resCode", resCode);
				resultMap.put("resMsg", resMsg);
				
			}
		}
		return resultMap;
	}

	/**
	 * 2021.05.13 AB테스트 MIMS
	 * AB테스트 지면 최종완료 상태 수정
	 */
	@Override
	public void updateABTestStatus(String status, String test_id) throws Exception {
		dao.updateABTestStatus(status, test_id);
	}
	
	/* 
	 * 2021.05.25
	 * AB테스트 즉시적용
	 */
	public void  insertABTestPanelTitle(String panel_id, String create_id) throws Exception{
		String version = GlobalCom.getTodayFormat4_24();
		
		// PT_UX_AB_PANEL_TITLE 테이블에서 panel_id에 해당하는 패널 것 삭제
		dao.deleteABTestPanelTitle(panel_id);					
		// PT_UX_AB_PANEL_TITLE_TEMP 테이블에서 panel_id에 해당하는 패널 것을 PT_UX_AB_PANEL_TITLE 테이블에 등록
		dao.insertABTestPanelTitle(panel_id, create_id);		
		// PT_UX_AB_PANEL 테이블에 Version 업데이트
		dao.updateABTestPanelVersion(version, create_id);
		// PT_UX_AB_PANEL_TITLE 테이블에 Version 업데이트
		dao.updateABTestPanelTitleVersion(version, create_id);
	}
}
