package com.dmi.smartux.admin.mainpanel.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.dmi.smartux.admin.abtest.dao.ABTestDao;
import com.dmi.smartux.admin.abtest.service.impl.ABMSComponent;
import com.dmi.smartux.admin.abtest.vo.ABTestSearchVO;
import com.dmi.smartux.admin.abtest.vo.ABTestVO;
import com.dmi.smartux.admin.common.dao.CommMimsDao;
import com.dmi.smartux.admin.imcs.service.ImcsService;
import com.dmi.smartux.admin.mainpanel.dao.BubbleIptvDao;
import com.dmi.smartux.admin.mainpanel.dao.PanelViewDao;
import com.dmi.smartux.admin.mainpanel.dao.PanelViewIptvDao;
import com.dmi.smartux.admin.mainpanel.service.PanelViewService;
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
import com.dmi.smartux.common.exception.ExceptionHandler;
import com.dmi.smartux.common.exception.SmartUXException;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.service.CacheService;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.util.HTMLCleaner;
import com.dmi.smartux.mainpanel.dao.MainPanelDao;
import com.dmi.smartux.smartepg.dao.SmartEPGDao;
import com.dmi.smartux.smartepg.vo.RealRatingInfoVO;
import com.dmi.smartux.smartstart.dao.GenreVodBestListDao;
import com.dmi.smartux.smartstart.dao.SUXMAlbumListDao;
import com.dmi.smartux.smartstart.vo.GenreVodBestListVO;
import com.dmi.smartux.smartstart.vo.SUXMAlbumListVO;

@Service
public class PanelViewServiceImpl implements PanelViewService {
	
	private final Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	PanelViewDao dao;
	
	@Autowired
	BubbleIptvDao iptv_dao;
	
	@Autowired
	MainPanelDao mainpaneldao;
	
	@Autowired
	CacheService cacheservice;
	
	@Autowired
	SmartEPGDao smartepgdao;			// 실시간 시청률 조회를 하기 위한 dao
	
	@Autowired
	GenreVodBestListDao genredao;		// Best VOD 조회를 하기 위한 dao
	
	@Autowired
	SUXMAlbumListDao selfscheduledao;	// 자체편성을 조회하기 위한 dao
	
	@Autowired
	CommMimsDao mimsDao;
	
	@Autowired
	ImcsService imcsService;
	
	@Autowired
	ABTestDao abTestDao;
	
	@Autowired
	ABMSComponent abmsCompo;
	
	@Autowired
	PanelViewIptvDao panelViewIptvDao;		
	
	/**
	 * 버전 정보를 구한다
	 * @return
	 */
	private String getVersion(){
		//return String.valueOf(System.currentTimeMillis());
		return GlobalCom.getTodayFormat4_24();
	}
	
	
	@Override
	@Transactional(readOnly=true)
	public PanelVO getPanelId() throws Exception {
		// TODO Auto-generated method stub
		return dao.getPanelId();
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<PanelVO> getPanelList() throws Exception {
		// TODO Auto-generated method stub
		return dao.getPanelList();
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<PanelVO> getPanelList(PanelSearchVo vo) throws Exception {
		// TODO Auto-generated method stub
		return dao.getPanelList(vo);
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<ABTestVO> getPanelListWithABTest(PanelSearchVo vo) throws Exception {
		// TODO Auto-generated method stub
		return dao.getPanelListWithABTest(vo);
	}

	@Override
	@Transactional(readOnly=true)
	public PanelVO viewPanel(String panel_id) throws Exception {
		// TODO Auto-generated method stub
		return dao.viewPanel(panel_id);
	}

	@Override
	@Transactional(readOnly=true)
	public int getPanelidCnt(String panel_id) throws Exception {
		// TODO Auto-generated method stub
		return dao.getPanelidCnt(panel_id);
	}

	@Override
	@Transactional(readOnly=true)
	public int getPanelnmCnt(String panel_nm) throws Exception {
		// TODO Auto-generated method stub
		return dao.getPanelnmCnt(panel_nm);
	}

	@Override
	@Transactional
	public void insertPanel(String panel_id, String panel_nm, String use_yn, String create_id, String panel_ui_type, String panel_image , String focus_type) throws Exception {
		// TODO Auto-generated method stub
		// DB에서 먼저 버전을 조회한다
		String version = GlobalCom.isNull(dao.getPanelVersion());
		
		// Panel을 등록하지 않은 상황일 경우엔 시스테 시간으로 셋팅한다
		if("".equals(version)){
			version = getVersion();
		}
		
		dao.insertPanel(panel_id, panel_nm, use_yn, version, create_id, panel_ui_type, panel_image , focus_type);
		// PT_UX_PANEL 테이블에 Version 업데이트
		// dao.updatePanelVersion(version, create_id);
		// PT_UX_PANEL_TITLE 테이블에 Version 업데이트
		// dao.updatePanelTitleVersion(version, create_id);
		
		
		// 캐쉬 버전 정보 갱신
		/*
		cacheservice.getCache(mainpaneldao
				, "getMainPanelVersionInfo"
				, SmartUXProperties.getProperty("MainPanelDao.getMainPanelVersionInfo.cacheKey")
				, -1
				);
		*/
		
		// 캐쉬 View 내용 갱신
		/*
		cacheservice.getCache(mainpaneldao
				, "getMainPanelInterlockingInfo"
				, SmartUXProperties.getProperty("MainPanelDao.getMainPanelInterlockingInfo.cacheKey")
				, -1
				);
		*/
		
		
		
	}

	@Override
	@Transactional
	public int updatePanel(String panel_id, String newPanel_id, String panel_nm, String newPanel_nm, String use_yn, String update_id, String panel_ui_type, String panel_image , String focus_type , String panel_image_nm) throws Exception {
		// TODO Auto-generated method stub
		String version = getVersion();
		int updcnt = dao.updatePanel(panel_id, newPanel_id, panel_nm, newPanel_nm, use_yn, update_id, panel_ui_type, panel_image , focus_type , panel_image_nm);
		// PT_UX_PANEL 테이블에 Version 업데이트
		// dao.updatePanelVersion(version, update_id);
		// PT_UX_PANEL_TITLE 테이블에 Version 업데이트
		// dao.updatePanelTitleVersion(version, update_id);
		// 캐쉬 버전 정보 갱신
		/*
		cacheservice.getCache(mainpaneldao
				, "getMainPanelVersionInfo"
				, SmartUXProperties.getProperty("MainPanelDao.getMainPanelVersionInfo.cacheKey")
				, -1
				);
		*/
		// 캐쉬 View 내용 갱신
		/*
		cacheservice.getCache(mainpaneldao
				, "getMainPanelInterlockingInfo"
				, SmartUXProperties.getProperty("MainPanelDao.getMainPanelInterlockingInfo.cacheKey")
				, -1
				);
		*/
		
		
		
		return updcnt;
	}

	@Override
	@Transactional
	public void deletePanel(String [] panel_ids, String update_id) throws Exception {
		// TODO Auto-generated method stub
		String version = getVersion();
		for(String panel_id : panel_ids){
			// 삭제하고자 하는 패널의 작업중인 타이틀(PT_UX_PANEL_TITLE_TEMP)것을 지운다
			mimsDao.deletePanelTitleTempByDeletePanel(panel_id);
			// 삭제하고자 하는 패널의 타이틀(PT_UX_PANEL_TITLE)을 지운다
			mimsDao.deletePanelTitle(panel_id);
			// 삭제하고자 하는 패널을 지운다
			dao.deletePanel(panel_id);
		}
	}

	
	
	@Override
	@Transactional(readOnly=true)
	public List<ViewVO> getPanelTitleTempList(String panel_id) throws Exception {
		// TODO Auto-generated method stub
		return dao.getPanelTitleTempList(panel_id);
	}
	
	
	@Override
	@Transactional(readOnly=true)
	public List<ViewVO> getPanelTitleTempListSub(String panel_id , String p_title_id) throws Exception {
		// TODO Auto-generated method stub
		return dao.getPanelTitleTempListSub(panel_id , p_title_id);
	}

	@Override
	@Transactional(readOnly=true)
	public List<ViewVO> getPanelTitleTempListSubNull(String panel_id) throws Exception {
		// TODO Auto-generated method stub
		return dao.getPanelTitleTempListSubNull(panel_id);
	}

	

	@Override
	@Transactional(readOnly=true)
	public ViewVO viewPanelTitleTemp(String panel_id, String title_id)	throws Exception {
		// TODO Auto-generated method stub
		return dao.viewPanelTitleTemp(panel_id, title_id);
	}


	@Override
	@Transactional(readOnly=true)
	public int getPanelTitleTempTitlenmCnt(String panel_id, String p_title_id, String title_nm) throws Exception {
		// TODO Auto-generated method stub
		return mimsDao.getPanelTitleTempTitlenmCnt(panel_id, p_title_id, title_nm);
	}
	
	@Override
	@Transactional(rollbackFor = {Exception.class})
	public void insertPanelTitleTemp(String panel_id, String title_nm, String title_color, String p_title_id, String use_yn, String create_id, String title_bg_img_file, String bg_img_file, String bg_img_file2 , String bg_img_file3) throws Exception {
		// TODO Auto-generated method stub
		
		String title_id = mimsDao.getMaxTitleid(panel_id, p_title_id);				// title_id 값을 구한다

		mimsDao.insertPanelTitleTemp(panel_id, title_nm, title_color, p_title_id, use_yn, create_id, title_bg_img_file, bg_img_file, bg_img_file2 , bg_img_file3, title_id);
		// 지면을 등록했을때 상위 지면의 카테고리 코드는 null로 셋팅해주는 작업을 진행한다
		// 단 최상위 지면을 등록하는 경우(p_title_id가 "" 일 경우)엔 이 작업을 하지 않는다
		if(!("".equals(p_title_id))){
			mimsDao.updatePanelTitleTempCategorySetNull(panel_id, p_title_id, create_id);
		}
		
		try {
			List<String> testType_list = new ArrayList<String>();
			testType_list.add("M");
			testType_list.add("D");
			List<ABTestVO> list= abTestDao.getOrgMimsIdVariationInfo(panel_id, testType_list);
			
			if (!CollectionUtils.isEmpty(list)) {
				HashMap<String, List<ABTestVO>> map = new HashMap<String, List<ABTestVO>>();
				for (int i = 0; i < list.size(); i++) {
					List<ABTestVO> abList = null;
					if (map.get(list.get(i).getTest_id()) != null) {
						abList = map.get(list.get(i).getTest_id());
					}
					else {
						abList = new ArrayList<ABTestVO>();
					}
					abList.add(list.get(i));
					
					map.put(list.get(i).getTest_id(), abList);
				}
				
				for (Entry<String, List<ABTestVO>> entrySet : map.entrySet()) {
					List<ABTestVO> abTestlist = entrySet.getValue();
					//ABMS 호출
					ABTestSearchVO searchVo = new ABTestSearchVO();
					//페이지
					searchVo.setPageSize(0);
					searchVo.setPageNum(1);
					searchVo.setFindName("test_id");
					searchVo.setFindValue(entrySet.getKey());
					Map<String, Object> abmsMap = getABMSCall(searchVo);
					
					if(!CollectionUtils.isEmpty(abmsMap)){
						List<ABTestVO> abmsList = (List<ABTestVO>) abmsMap.get("abmsList");
						if(!CollectionUtils.isEmpty(abmsList)){
							
							for (int j = 0; j < abTestlist.size(); j++) {
								
								boolean isTrue = false;
								if(abTestlist.get(j).getVariation_id().substring(0,1).equals("O")
										&& (abmsStatusComparison(abmsList.get(0).getAbms_status()))) {  
									isTrue = true;
								}
								else {
									for (int i = 0; i < abmsList.size(); i++) {
										if (abTestlist.get(j).getVariation_id().equals(abmsList.get(i).getVariation_id())) {
											if (abmsStatusComparison(abmsList.get(i).getAbms_status())) {
												isTrue = true;
											}
										}
									}
								}
								if (isTrue) {
									abTestDao.insertPanelTitleTemp(abTestlist.get(j).getMims_id(), title_nm, title_color, p_title_id, use_yn, create_id, title_bg_img_file, bg_img_file, bg_img_file2 , bg_img_file3, panel_id+"_"+title_id);
									
									if(!("".equals(p_title_id))){
										abTestDao.updatePanelTitleTempCategorySetNull(abTestlist.get(j).getMims_id(), panel_id+"_"+p_title_id, create_id);
									}
								}
							}
							
							logger.info("[AB테스트 등록][insertPanelTitleTemp][ABMS 호출 완료]");
						}
					}else{
						logger.info("[AB테스트 등록][insertPanelTitleTemp][ABMS 호출 결과 없음]");
					}
				}
			}
		} catch (Exception e) {
			logger.error("[insertPanelTitleTemp]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			e.printStackTrace();
			throw e;		
		}
	}

	@Override
	@Transactional(rollbackFor = {Exception.class})
	public int updatePanelTitleTemp(String panel_id, String title_id, String title_nm, String title_color, String use_yn, String update_id, String title_bg_img_file, String bg_img_file, String bg_img_file2, String bg_img_file3) throws Exception {
		// TODO Auto-generated method stub
		int result = mimsDao.updatePanelTitleTemp(panel_id, title_id, title_nm, title_color, use_yn, update_id, title_bg_img_file, bg_img_file, bg_img_file2 ,bg_img_file3);
		
		try {
			List<ABTestVO> list= abTestDao.getAbPanelTitleTempInfo(panel_id, panel_id+"_"+title_id,"");
			
			if (!CollectionUtils.isEmpty(list)) {
				HashMap<String, List<ABTestVO>> map = new HashMap<String, List<ABTestVO>>();

				for (int i = 0; i < list.size(); i++) {
					if ((list.get(i).getTest_type() != null && list.get(i).getTest_type().equals("O"))  
							|| (list.get(i).getTest_type() != null && !list.get(i).getTest_type().equals("T") 
									&& list.get(i).getAbtest_yn() != null && list.get(i).getAbtest_yn().equals("N"))) {
						
						List<ABTestVO> abList = null;
						if (map.get(list.get(i).getTest_id()) != null) {
							abList = map.get(list.get(i).getTest_id());
						}
						else {
							abList = new ArrayList<ABTestVO>();
						}
						abList.add(list.get(i));
						
						map.put(list.get(i).getTest_id(), abList);
						
					}
				}
				
				for (Entry<String, List<ABTestVO>> entrySet : map.entrySet()) {
					List<ABTestVO> abTestlist = entrySet.getValue();
					//ABMS 호출
					ABTestSearchVO searchVo = new ABTestSearchVO();
					//페이지
					searchVo.setPageSize(0);
					searchVo.setPageNum(1);
					searchVo.setFindName("test_id");
					searchVo.setFindValue(entrySet.getKey());
					Map<String, Object> abmsMap = getABMSCall(searchVo);
					
					if(!CollectionUtils.isEmpty(abmsMap)){
						List<ABTestVO> abmsList = (List<ABTestVO>) abmsMap.get("abmsList");
						if(!CollectionUtils.isEmpty(abmsList)){
							
							for (int j = 0; j < abTestlist.size(); j++) {
								boolean isTrue = false;
								if(abTestlist.get(j).getVariation_id().substring(0,1).equals("O")
										&& (abmsStatusComparison(abmsList.get(0).getAbms_status()))) {
									isTrue = true;
								}
								else {
									for (int i = 0; i < abmsList.size(); i++) {
										if (abTestlist.get(j).getVariation_id().equals(abmsList.get(i).getVariation_id())) {
											if (abmsStatusComparison(abmsList.get(i).getAbms_status())) {
												isTrue = true;
											}
										}
									}
								}
								
								if (isTrue) {
									abTestDao.updatePanelTitleTemp(abTestlist.get(j).getMims_id(), panel_id+"_"+title_id, title_nm, title_color, use_yn, update_id, title_bg_img_file, bg_img_file, bg_img_file2 ,bg_img_file3);
								}
							}
							
							logger.info("[AB테스트 조회][updatePanelTitleTemp][ABMS 호출 완료]");
						}
					}else{
						logger.info("[AB테스트 조회][updatePanelTitleTemp][ABMS 호출 결과 없음]");
					}
				}
			}
			
		} catch (Exception e) {
			logger.error("[updatePanelTitleTemp]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			throw e;
		}
		return  result;
	}


	@Override
	@Transactional(rollbackFor = {Exception.class})
	public void deletePanelTitleTemp(String panel_id, String [] title_ids) throws Exception {
		// TODO Auto-generated method stub
		List<String> titleIdList = new ArrayList<String>();
		for (int i = 0; i < title_ids.length; i++) {
			titleIdList.add(panel_id+"_"+title_ids[i]);
		}
		
		List<String> testType_List = new ArrayList<String>();
		testType_List.add("M");
		//testType_List.add("D");
		
		List<ABTestVO> list =abTestDao.getAbPanelTitleTempInfoList(panel_id, testType_List, "Y", titleIdList);
		if (!CollectionUtils.isEmpty(list)) {
			
			HashMap<String, List<ABTestVO>> map = new HashMap<String, List<ABTestVO>>();
			for (int i = 0; i < list.size(); i++) {
				List<ABTestVO> abList = null;
				if (map.get(list.get(i).getTest_id()) != null) {
					abList = map.get(list.get(i).getTest_id());
				}
				else {
					abList = new ArrayList<ABTestVO>();
				}
				abList.add(list.get(i));
				
				map.put(list.get(i).getTest_id(), abList);
				
			}
			
			for (Entry<String, List<ABTestVO>> entrySet : map.entrySet()) {
				List<ABTestVO> abTestlist = entrySet.getValue();
				//ABMS 호출
				ABTestSearchVO searchVo = new ABTestSearchVO();
				//페이지
				searchVo.setPageSize(0);
				searchVo.setPageNum(1);
				searchVo.setFindName("test_id");
				searchVo.setFindValue(entrySet.getKey());
				Map<String, Object> abmsMap = getABMSCall(searchVo);
				
				if(!CollectionUtils.isEmpty(abmsMap)){
					List<ABTestVO> abmsList = (List<ABTestVO>) abmsMap.get("abmsList");
					if(!CollectionUtils.isEmpty(abmsList)){
						
						for (int j = 0; j < abTestlist.size(); j++) {
							
							for (int i = 0; i < abmsList.size(); i++) {
								if (abTestlist.get(j).getVariation_id().equals(abmsList.get(i).getVariation_id())) {
									if (abmsStatusComparison(abmsList.get(i).getAbms_status())) {
										
										SmartUXException e = new SmartUXException();
										e.setFlag(SmartUXProperties.getProperty("flag.fail"));
										e.setMessage(SmartUXProperties.getProperty("message.can_not_delete", "["+abTestlist.get(j).getTitle_nm()+"]"+" AB TEST 실험군 대상 지면"));
										throw e;
									}	
								}
							}
						}
						
						logger.info("[AB테스트 조회][deletePanelTitleTemp][ABMS 호출 완료]");
					}
				}else{
					logger.info("[AB테스트 조회][deletePanelTitleTemp][ABMS 호출 결과 없음]");
				}
			}
		}
		
		for(String title_id : title_ids){
			
			mimsDao.deletePanelTitleTemp(panel_id, title_id);
			
			//2021-04-22 BPAS 편성관리 추가
			try {
				imcsService.deletePap(panel_id, title_id);
			}catch (Exception e) {
				logger.error("[insertImcs]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			}
			
			try {
				List<ABTestVO> abTestList= abTestDao.getAbPanelTitleTempInfo(panel_id, panel_id+"_"+title_id,"");
				
				if (!CollectionUtils.isEmpty(abTestList)) {
					HashMap<String, List<ABTestVO>> map = new HashMap<String, List<ABTestVO>>();

					for (int i = 0; i < abTestList.size(); i++) {
						
						if (abTestList.get(i).getTest_type() != null) {
							if((abTestList.get(i).getTest_type().equals("M") && abTestList.get(i).getAbtest_yn() !=null && abTestList.get(i).getAbtest_yn().equals("N"))
									|| abTestList.get(i).getTest_type().equals("D")) {
									
								List<ABTestVO> abList = null;
								if (map.get(abTestList.get(i).getTest_id()) != null) {
									abList = map.get(abTestList.get(i).getTest_id());
								}
								else {
									abList = new ArrayList<ABTestVO>();
								}
								abList.add(abTestList.get(i));
								
								map.put(abTestList.get(i).getTest_id(), abList);
							}
						}
					}
					
					for (Entry<String, List<ABTestVO>> entrySet : map.entrySet()) {
						List<ABTestVO> abTestlist = entrySet.getValue();
						//ABMS 호출
						ABTestSearchVO searchVo = new ABTestSearchVO();
						//페이지
						searchVo.setPageSize(0);
						searchVo.setPageNum(1);
						searchVo.setFindName("test_id");
						searchVo.setFindValue(entrySet.getKey());
						Map<String, Object> abmsMap = getABMSCall(searchVo);
						
						if(!CollectionUtils.isEmpty(abmsMap)){
							List<ABTestVO> abmsList = (List<ABTestVO>) abmsMap.get("abmsList");
							if(!CollectionUtils.isEmpty(abmsList)){
								
								for (int j = 0; j < abTestlist.size(); j++) {
									
									boolean isTrue = false;
									if(abTestlist.get(j).getVariation_id().substring(0,1).equals("O")
											&& (abmsStatusComparison(abmsList.get(0).getAbms_status()))) {
										isTrue= true;
									}
									else {
										for (int i = 0; i < abmsList.size(); i++) {
											if (abTestlist.get(j).getVariation_id().equals(abmsList.get(i).getVariation_id())) {
												if (abmsStatusComparison(abmsList.get(i).getAbms_status())) {
													isTrue= true;
												}
											}
										}
									}
									
									if (isTrue) {
										abTestDao.deletePanelTitleTemp(abTestlist.get(j).getMims_id(), panel_id+"_"+title_id);
									}
								}
								
								logger.info("[AB테스트 조회][deletePanelTitleTemp][ABMS 호출 완료]");
							}
						}else{
							logger.info("[AB테스트 조회][deletePanelTitleTemp][ABMS 호출 결과 없음]");
							
						}
					}
				}
			} catch (Exception e) {
				logger.error("[deletePanelTitleTemp]["+e.getClass().getName()+"]["+e.getMessage()+"]");
				throw e;
			}
		}
	}

	
	
	@Override
	@Transactional
	public void focusPanelTitleTemp(String panel_id, String [] title_ids) throws Exception {

		
		mimsDao.focusPanelTitleNull(panel_id);
		
		
		for(String title_id : title_ids){
			mimsDao.focusPanelTitleTemp(panel_id, title_id);
		}
	}
	
	
	

	@Override
	@Transactional(readOnly=true)
	public List<ViewVO> changePanelTitleTempOrderList(String panel_id, String p_title_id) throws Exception {
		// TODO Auto-generated method stub
		return mimsDao.changePanelTitleTempOrderList(panel_id, p_title_id);
	}


	@Override
	@Transactional(rollbackFor = {Exception.class})
	public void changePanelTitleTempOrderJob(String panel_id, String[] title_ids, String update_id) throws Exception {
		// TODO Auto-generated method stub
		int ordered = 1;
		for(String title_id : title_ids){
			mimsDao.changePanelTitleTempOrderJob(ordered++, panel_id, title_id, update_id);
			
			try {
				List<ABTestVO> list= abTestDao.getAbPanelTitleTempInfo(panel_id, panel_id+"_"+title_id, "M");		
				
				if (!CollectionUtils.isEmpty(list)) {
					HashMap<String, List<ABTestVO>> map = new HashMap<String, List<ABTestVO>>();

					for (int i = 0; i < list.size(); i++) {
						List<ABTestVO> abList = null;
						if (map.get(list.get(i).getTest_id()) != null) {
							abList = map.get(list.get(i).getTest_id());
						}
						else {
							abList = new ArrayList<ABTestVO>();
						}
						abList.add(list.get(i));
						
						map.put(list.get(i).getTest_id(), abList);
						
					}
					
					for (Entry<String, List<ABTestVO>> entrySet : map.entrySet()) {
						List<ABTestVO> abTestlist = entrySet.getValue();
						//ABMS 호출
						ABTestSearchVO searchVo = new ABTestSearchVO();
						//페이지
						searchVo.setPageSize(0);
						searchVo.setPageNum(1);
						searchVo.setFindName("test_id");
						searchVo.setFindValue(entrySet.getKey());
						Map<String, Object> abmsMap = getABMSCall(searchVo);
						
						if(!CollectionUtils.isEmpty(abmsMap)){
							List<ABTestVO> abmsList = (List<ABTestVO>) abmsMap.get("abmsList");
							if(!CollectionUtils.isEmpty(abmsList)){
								
								for (int j = 0; j < abTestlist.size(); j++) {
									boolean isTrue = false;
									if(abTestlist.get(j).getVariation_id().substring(0,1).equals("O")
											&& (abmsStatusComparison(abmsList.get(0).getAbms_status()))) {
										isTrue = true;
									}
									else {
										for (int i = 0; i < abmsList.size(); i++) {
											if (abTestlist.get(j).getVariation_id().equals(abmsList.get(i).getVariation_id())) {
												if (abmsStatusComparison(abmsList.get(i).getAbms_status())) {
													isTrue = true;
												}
											}
										}
									}
									
									if (isTrue) {
										abTestDao.changePanelTitleTempOrderJob(ordered, abTestlist.get(j).getMims_id(), panel_id+"_"+title_id, update_id);
									}
								}
								
								logger.info("[AB테스트 조회][changePanelTitleTempOrderJob][ABMS 호출 완료]");
							}
						}else{
							logger.info("[AB테스트 조회][changePanelTitleTempOrderJob][ABMS 호출 결과 없음]");
						}
					}
				}
			} catch (Exception e) {
				logger.error("[changePanelTitleTempOrderJob]["+e.getClass().getName()+"]["+e.getMessage()+"]");
				throw e;
			}
		}
	}


	@Override
	@Transactional(readOnly=true)
	public List<CategoryVO> getCategoryList(String category_id) throws Exception {
		// TODO Auto-generated method stub
		return dao.getCategoryList(category_id);
	}
	
	
	

	@Override
	@Transactional(readOnly=true)
	public List<CategoryVO> getCategoryList(String category_gb, String category_id) throws Exception {
		// TODO Auto-generated method stub
		return dao.getCategoryList(category_gb, category_id);
	}

	@Override
	@Transactional(rollbackFor = {Exception.class})
	public int updateCategory(String panel_id, String title_id, String category_id, String category_type, String album_cnt, String ui_type, String description, String update_id, String page_type, 
			String page_code, String category_gb, String reps_album_id, String reps_category_id, String trailer_viewing_type, String reps_trailer_viewing_type, String location_code, String location_yn
/* 2019.11.04 : 지면 UI 타입 [paper_ui_type 변수 추가] 추가 Start - 이태광 */					
			, String paper_ui_type, String product_code, String product_code_not, String show_cnt, String terminal_all_yn, String terminal_arr)
/* 2019.11.04 : 지면 UI 타입 [paper_ui_type 변수 추가] 추가 End - 이태광 */			
			throws Exception {
		 int result = mimsDao.updateCategory(panel_id, title_id, category_id, category_type, album_cnt, ui_type, description, update_id, page_type, page_code, category_gb, reps_album_id, 
				reps_category_id, trailer_viewing_type, reps_trailer_viewing_type, location_code, location_yn
/* 2019.11.04 : 지면 UI 타입 [paper_ui_type 변수 추가] 추가 Start - 이태광 */					
				, paper_ui_type, product_code, product_code_not, show_cnt, terminal_all_yn, terminal_arr);
/* 2019.11.04 : 지면 UI 타입 [paper_ui_type 변수 추가] 추가 End - 이태광 */	
		
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
		 
		try {
			List<ABTestVO> list= abTestDao.getAbPanelTitleTempInfo(panel_id, panel_id+"_"+title_id, "");
			
			if (!CollectionUtils.isEmpty(list)) {
				HashMap<String, List<ABTestVO>> map = new HashMap<String, List<ABTestVO>>();

				for (int i = 0; i < list.size(); i++) {
					if ((list.get(i).getTest_type() != null && list.get(i).getTest_type().equals("O"))  
							|| (list.get(i).getTest_type() != null && !list.get(i).getTest_type().equals("T") 
									&& list.get(i).getAbtest_yn() != null && list.get(i).getAbtest_yn().equals("N"))) {
					
					List<ABTestVO> abList = null;
					if (map.get(list.get(i).getTest_id()) != null) {
						abList = map.get(list.get(i).getTest_id());
					}
					else {
						abList = new ArrayList<ABTestVO>();
					}
					abList.add(list.get(i));
					
					map.put(list.get(i).getTest_id(), abList);
						
					}
				}
				
				for (Entry<String, List<ABTestVO>> entrySet : map.entrySet()) {
					List<ABTestVO> abTestlist = entrySet.getValue();
					//ABMS 호출
					ABTestSearchVO searchVo = new ABTestSearchVO();
					//페이지
					searchVo.setPageSize(0);
					searchVo.setPageNum(1);
					searchVo.setFindName("test_id");
					searchVo.setFindValue(entrySet.getKey());
					Map<String, Object> abmsMap = getABMSCall(searchVo);
					
					if(!CollectionUtils.isEmpty(abmsMap)){
						List<ABTestVO> abmsList = (List<ABTestVO>) abmsMap.get("abmsList");
						if(!CollectionUtils.isEmpty(abmsList)){
							
							for (int j = 0; j < abTestlist.size(); j++) {
								
								boolean isTrue = false;
								
								if(abTestlist.get(j).getVariation_id().substring(0,1).equals("O")
										&& (abmsStatusComparison(abmsList.get(0).getAbms_status()))) {
									isTrue = true;
								}
								else {
									for (int i = 0; i < abmsList.size(); i++) {
										if (abTestlist.get(j).getVariation_id().equals(abmsList.get(i).getVariation_id())) {
											if (abmsStatusComparison(abmsList.get(i).getAbms_status())) {
												isTrue = true;
											}
										}
									}
								}
								if (isTrue) {
									abTestDao.updateCategory(abTestlist.get(j).getMims_id(), panel_id+"_"+title_id, category_id, category_type, album_cnt, ui_type, description, update_id, page_type, page_code, category_gb, reps_album_id, 
											reps_category_id, trailer_viewing_type, reps_trailer_viewing_type, location_code, location_yn, paper_ui_type, product_code, product_code_not, show_cnt);											
								
									panelViewIptvDao.deletePaperTerminal(abTestlist.get(j).getMims_id()+"_"+panel_id+"_"+title_id);
									 
									 if("N".equals(terminal_all_yn)) {
										String terminal[] = terminal_arr.split(",");
										for(String unit : terminal) {
											unit = unit.trim();
											Map<String, String> param2 = new HashMap<String, String>();
											param2.put("paper_id", abTestlist.get(j).getMims_id()+"_"+panel_id+"_"+title_id);
											param2.put("term_model", unit);
											panelViewIptvDao.insertPaperTerminal(param2);
										}
									}
								}
							}
							
							logger.info("[AB테스트 조회][updateCategory][ABMS 호출 완료]");
						}
					}else{
						logger.info("[AB테스트 조회][updateCategory][ABMS 호출 결과 없음]");
					}
				}
			}
		} catch (Exception e) {
			logger.error("[updateCategory]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			throw e;
		}
		return result;
	}
	

	@Override
	@Transactional(readOnly=true)
	public List<String> mustCategorySettingList(String panel_id) throws Exception {
		// TODO Auto-generated method stub
		return mimsDao.mustCategorySettingList(panel_id);
	}

	@Override
	@Transactional(readOnly=true)
	public int getPanelTitleChangeCount(String panel_id) throws Exception {
		// TODO Auto-generated method stub
		return mimsDao.getPanelTitleChangeCount(panel_id);
	}
	

	@Override
	@Transactional
	public void insertPanelTitle(String panel_id, String create_id) throws Exception {
		// TODO Auto-generated method stub
		String version = getVersion();
		mimsDao.deletePanelTitle(panel_id);					// PT_UX_PANEL_TITLE 테이블에서 panel_id에 해당하는 패널 것 삭제
		mimsDao.insertPanelTitle(panel_id, create_id);		// PT_UX_PANEL_TITLE_TEMP 테이블에서 panel_id에 해당하는 패널 것을 PT_UX_PANEL_TITLE 테이블에 등록
		
		// PT_UX_PANEL 테이블에 Version 업데이트
		dao.updatePanelVersion(version, create_id);
		// PT_UX_PANEL_TITLE 테이블에 Version 업데이트
		mimsDao.updatePanelTitleVersion(version, create_id);
		
		// 캐쉬 버전 정보 갱신
		/*
		cacheservice.getCache(mainpaneldao
				, "getMainPanelVersionInfo"
				, SmartUXProperties.getProperty("MainPanelDao.getMainPanelVersionInfo.cacheKey")
				, -1
				);
		*/
		// 캐쉬 View 내용 갱신
		/*
		cacheservice.getCache(mainpaneldao
				, "getMainPanelInterlockingInfo"
				, SmartUXProperties.getProperty("MainPanelDao.getMainPanelInterlockingInfo.cacheKey")
				, -1
				);
		*/
		
	}


	@Override
	@Transactional(readOnly=true)
	public List<PreviewVO> previewPanelTitleTemp(String panel_id, String separator)	throws Exception {
		// TODO Auto-generated method stub
		List<PreviewVO> previewlist =  mimsDao.previewPanelTitleTemp(panel_id);
		
		//버전을 업데이트
		//2019.11.13 DB 분리건으로 smartepgdao.getRealRating() 함수에 있던 버전 업데이트 구분을 분리함
		mimsDao.setMainPanelTitleVersion();
		
		// 실시간 시청률 데이터를 작업한다
		String realratingalbum = "";
		List<RealRatingInfoVO> realRatingList =  smartepgdao.getRealRating();				// 실시간 시청률을 가져온다
		for(RealRatingInfoVO realRatingInfo : realRatingList){
			if("".equals(realratingalbum)){
				realratingalbum = realRatingInfo.getProgram_name();
			}else{
				realratingalbum += separator + realRatingInfo.getProgram_name();
			}
		}
		
		// 앨범목록을 가져오는 작업을 진행한다
		for(PreviewVO item : previewlist){
			if("LIVE".equals(item.getCategory_type())){				// 실시간 시청률일땐 실시간 시청률 관련 데이터를 조회한다
				
				item.setAlbum_desc(realratingalbum);
				
			}else if("VOD".equals(item.getCategory_type()) || "CA_RANK".equals(item.getCategory_type())){		// VOD일땐 Best VOD 에서 앨범목록을 가져온다
				
				String vod_result = "";
				String [] tmps = item.getCategory_code().split(SmartUXProperties.getProperty("PanelTitle.CategoryCode.SplitString"));
				for(String subitem : tmps){
					// 한 랭킹당 최대 조회 갯수보다 넘게 조회되어 있으면 최대 조회 갯수까지만 조회되도록 한다
					logger.debug("previewTemp vod item : " + subitem);
					List<String> tmplist = dao.getBestVODAlbumList(subitem);
					int maxcnt = Integer.parseInt(SmartUXProperties.getProperty("BestVOD.recordcnt"));
					int cnt = 1;
					for(String obj : tmplist){
						if(cnt <= maxcnt){
							if("".equals(vod_result)){
								vod_result = obj;
							}else{
								vod_result += separator + obj;
							}
							cnt++;
						}else{
							break;
						}
					}
				}
				
				item.setAlbum_desc(vod_result);
				
			}else if("CAT_MAP".equals(item.getCategory_type())){	// 카테고리 매핑일 경우 매핑된 카테고리에 속하는 앨범 목록을 조회한다
				boolean isNSC = GlobalCom.isNSCPanel(panel_id);

				String cat_map_result = "";
				String [] tmps = item.getCategory_code().split(SmartUXProperties.getProperty("PanelTitle.CategoryCode.SplitString"));
				for(String subitem : tmps){
					List<String> tmplist;

					if (isNSC) {
						tmplist = dao.getCategoryAlbumList(subitem, "NSC");
					} else {
						tmplist = dao.getCategoryAlbumList(subitem);
					}

					for(String obj : tmplist){
						if("".equals(cat_map_result)){
							cat_map_result = obj;
						}else{
							cat_map_result += separator + obj;
						}
					}
				}
				
				item.setAlbum_desc(cat_map_result);
				
			}else if("SCHEDULE".equals(item.getCategory_type())){	// 자체 편성일 경우 자체 편성에 속한 앨범 목록을 가져온다
				
				List<String> albumList = dao.getScheduleAlbumList(item.getCategory_code());
				String tmp = "";
				for(String subitem : albumList){
					if("".equals(tmp)){
						tmp = subitem;
					}else{
						tmp += separator + subitem;
					}
				}
				
				item.setAlbum_desc(tmp);
			}else if("WISH".equals(item.getCategory_type())){
				
			}
		}
		
		return previewlist;
	}	
	
	
	@Override
	@Transactional(readOnly=true)
	public List<PreviewVO> previewPanelTitle(String panel_id, String separator)	throws Exception {
		// TODO Auto-generated method stub
		// return dao.previewPanelTitle(panel_id);
		
		
		// TODO Auto-generated method stub
		List<PreviewVO> previewlist =  mimsDao.previewPanelTitle(panel_id);
		
		// 실시간 시청률 데이터를 작업한다(캐쉬에서 가져온다)
		String realratingalbum = "";
		
		List<RealRatingInfoVO> realRatingList = (List<RealRatingInfoVO>)cacheservice.getCache(smartepgdao
				, "getRealRating"
				, SmartUXProperties.getProperty("SmartEPGDao.getRealRating.cacheKey")
				, 0
				); // 실시간 시청률을 가져온다
	
		for(RealRatingInfoVO realRatingInfo : realRatingList){
			if("".equals(realratingalbum)){
				realratingalbum = realRatingInfo.getProgram_name();
			}else{
				realratingalbum += separator + realRatingInfo.getProgram_name();
			}
		}
		
		// 앨범목록을 가져오는 작업을 진행한다
		for(PreviewVO item : previewlist){
			if("LIVE".equals(item.getCategory_type())){				// 실시간 시청률일땐 실시간 시청률 관련 데이터를 조회한다
				
				item.setAlbum_desc(realratingalbum);
				
			}else if("VOD".equals(item.getCategory_type()) || "CA_RANK".equals(item.getCategory_type())){		// VOD일땐 Best VOD 에서 앨범목록을 가져온다
				String albumresult = "";
				logger.debug("preview vod tem.getCategory_code() : " + item.getCategory_code());
				//List<GenreVodBestListVO> dbresult = (List<GenreVodBestListVO>)cacheservice.getCache	(genredao, "getGenreVodBestList"
				//		, SmartUXProperties.getProperty("GenreVodBestListDao.getGenreVodBestList.cacheKey")
				//		, 0
				//	, item.getCategory_code()
				//);
				Map<String, List<GenreVodBestListVO>> mapresult = (Map<String, List<GenreVodBestListVO>>)cacheservice.getCache(genredao
						, "getGenreVodBestList"
						, SmartUXProperties.getProperty("GenreVodBestListDao.getGenreVodBestList.cacheKey")
						, 0
						);
				List<GenreVodBestListVO> dbresult = new ArrayList<GenreVodBestListVO>();
				dbresult = mapresult.get(item.getCategory_code());
				
				for(GenreVodBestListVO obj : dbresult){
					if("".equals(albumresult)){
						albumresult = obj.getAlbum_title();
					}else{
						albumresult += separator + obj.getAlbum_title();
					}
				}
				
				item.setAlbum_desc(albumresult);
			}else if("CAT_MAP".equals(item.getCategory_type())){	// 카테고리 매핑일 경우 매핑된 카테고리에 속하는 앨범 목록을 조회한다
				boolean isNSC = GlobalCom.isNSCPanel(panel_id);

				String cat_map_result = "";
				String [] tmps = item.getCategory_code().split(SmartUXProperties.getProperty("PanelTitle.CategoryCode.SplitString"));
				for(String subitem : tmps){
					List<String> tmplist;

					if (isNSC) {
						tmplist = dao.getCategoryAlbumList(subitem, "NSC");
					} else {
						tmplist = dao.getCategoryAlbumList(subitem, item.getCategory_gb());
					}

					for(String obj : tmplist){
						if("".equals(cat_map_result)){
							cat_map_result = obj;
						}else{
							cat_map_result += separator + obj;
						}
					}
				}
				
				item.setAlbum_desc(cat_map_result);
				
			}else if("SCHEDULE".equals(item.getCategory_type())){	// 자체 편성일 경우 자체 편성에 속한 앨범 목록을 가져온다
				String albumresult = "";
				List<SUXMAlbumListVO> dbresult = (List<SUXMAlbumListVO>)cacheservice.getCache	(selfscheduledao, "getSUXMAlbumList"
						, SmartUXProperties.getProperty("SUXMAlbumListDao.getSUXMAlbumList.cacheKey")
						, 0
					, item.getCategory_code()
				);
				
				for(SUXMAlbumListVO obj : dbresult){
					if("".equals(albumresult)){
						albumresult = obj.getAlbum_title();
					}else{
						albumresult += separator + obj.getAlbum_title();
					}
				}
				
				item.setAlbum_desc(albumresult);
				
			}else if("WISH".equals(item.getCategory_type())){
				
			}
		}
		
		return previewlist;

		
	}

	@Override
	@Transactional(readOnly=true)
	public CategoryVO getCategoryIdName(String category_code) throws Exception {
		// TODO Auto-generated method stub
		return dao.getCategoryIdName(category_code);
	}

	@Override
	public CategoryVO getCategoryIdName(String category_gb, String category_code) throws Exception {
		return dao.getCategoryIdName(category_gb, category_code);
	}

	
	@Override
	@Transactional(readOnly=true)
	public ViewVO getUITypeDescription(String panel_id, String title_id) throws Exception {
		// TODO Auto-generated method stub
		return dao.getUITypeDescription(panel_id, title_id);
	}

	@Override
	public String getPanelVersion() throws Exception {
		// TODO Auto-generated method stub
		String version = GlobalCom.isNull(dao.getPanelVersion());
		return version;
	}

	@Override
	@Transactional
	public String updatePanelVersion(String type, String version, String create_id) throws Exception {
		// TODO Auto-generated method stub
		// PT_UX_PANEL 테이블에 Version 업데이트
		dao.updatePanelVersion(version, create_id);
		
		//Normal Mode 일 경우에만 PT_UX_PANEL_TITLE 정보를 업데이트 한다.
		if(type.equals("N")){
			// PT_UX_PANEL_TITLE 테이블에 Version 업데이트
			mimsDao.updatePanelTitleVersion(version, create_id);
		}

		return "SUCCESS";
	}

	@Override
	@Transactional(readOnly=true)
	public String getImageServerURL(String type) throws Exception {
		// TODO Auto-generated method stub
		
		String imageServerUrl = dao.getImageServerURL(type);
		
		return imageServerUrl;
	}	
	
	@Override
	@Transactional(readOnly=true)
	public String getAlbumName(String album_id) throws Exception {
		return dao.getAlbumName(album_id);
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<FrameVO> getPanelUiTypeList(String frame_type, int pageNum, int pageSize) throws Exception {
		return dao.getPanelUiTypeList(frame_type, pageNum, pageSize);
	}
	
	@Override
	@Transactional(readOnly=true)
	public int getPanelUiTypeCnt(String frame_type) throws Exception {
		return dao.getPanelUiTypeCnt(frame_type);
	}
	
	@Override
	@Transactional
	public void insertPanelUiType(String frame_flag, String frame_type, String frame_nm, String img_file, String use_yn, String create_id) throws Exception {
		dao.insertPanelUiType(frame_flag, frame_type, frame_nm, img_file, use_yn, create_id);
	}
	
	@Override
	@Transactional
	public StringBuffer deletePanelUiType(String[] frame_type_code, String update_id) throws Exception {
		int result 		= 0;
		StringBuffer sb = new StringBuffer();
		File fileName 	= null;
		String newFilename = "";
		
		for(int i = 0; i < frame_type_code.length; i++){
			result = dao.updatePanelUiType(frame_type_code[i], "", "", "N", update_id);
			
			if(result == 0){
				sb.append(frame_type_code[i] + " ");
			}
			
			result = 0;
		}

		return sb;
	}
	
	@Override
	@Transactional
	public int updatePanelUiType(String frame_type_code, String frame_nm, String img_file, String use_yn, String update_id) throws Exception {
		return dao.updatePanelUiType(frame_type_code, frame_nm, img_file, use_yn, update_id);
	}
	
	@Override
	@Transactional(readOnly=true)
	public FrameVO viewPanelUiType(String frame_type_code) throws Exception {
		return dao.viewPanelUiType(frame_type_code);
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<FrameVO> getPanelUiTypeSelect(String frame_type) throws Exception {
		return dao.getPanelUiTypeSelect(frame_type);
	}
	
	@Override
	public List<BubbleList> getBubbleList(BubbleSearch vo) {
		return iptv_dao.getBubbleList(vo);
	}

	@Override
	public int getBubbleListCnt(BubbleSearch vo) {
		return iptv_dao.getBubbleListCnt(vo);
	}

	@Override
	public void deleteBubble(String delList) {
		String[] ids = delList.split(",");
		
		for(int i=0; i < ids.length; i++) {
			iptv_dao.deleteBubble(ids[i]);
			BubbleInsert param = new BubbleInsert();
			param.setReg_no(ids[i]);
			iptv_dao.deleteBubbleTerminal(param);
		}
		
	}

	@Override
	public void insertBubble(BubbleInsert bubbleInsert) {
		String reg_no = iptv_dao.getRegNo();
		bubbleInsert.setReg_no(reg_no);
		
		iptv_dao.insertBubble(bubbleInsert);
		
		if("N".equals(bubbleInsert.getTerminal_all_yn())) {
			String terminal_arr = bubbleInsert.getTerminal_arr();
			String terminal[] = terminal_arr.split(",");
			for(String unit : terminal) {
				unit = unit.trim();
				bubbleInsert.setTerm_model(unit);
				iptv_dao.insertBubbleTerm(bubbleInsert);
			}
		}
	}

	@Override
	public List<ViewVO> getPanelTitleTempList() {
		return dao.getPanelTitleTempList();
	}

	@Override
	public BubbleInsert getBubbleDetail(String reg_no) {
		return iptv_dao.getBubbleDetail(reg_no);
	}

	@Override
	public List<String> getBubbleTerminal(String reg_no) {
		return iptv_dao.getBubbleTerminal(reg_no);
	}

	@Override
	public void updateBubble(BubbleInsert bubbleInsert) {
		iptv_dao.updateBubble(bubbleInsert);
		
		iptv_dao.deleteBubbleTerminal(bubbleInsert);
		
		String terminal_arr = bubbleInsert.getTerminal_arr();
		if(!"".equals(terminal_arr) && terminal_arr != null) {
			String terminal[] = terminal_arr.split(",");
			for(String unit : terminal) {
				unit = unit.trim();
				System.out.println(unit);
				bubbleInsert.setTerm_model(unit);
				iptv_dao.insertBubbleTerm(bubbleInsert);
			}
		}
	}
	
	@Override
	@Transactional
	public void changeOrder(List<String> orderList) throws Exception {
		int count = orderList.size();

		for (int i=0; i<count; i++) {
			PanelVO vo = new PanelVO();
			vo.setPannel_id(orderList.get(i));
			vo.setOrder(String.valueOf(i + 1));
			dao.changeOrder(vo);
		}
	}
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
		
		String result = abmsCompo.callABMSVariation2(params);
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
								logger.warn("[AMBS 호출 오류][WARN][JSON파싱 오류][" + e + "]");
								SmartUXException ex = new SmartUXException();
								ex.setFlag(SmartUXProperties.getProperty("flag.etc"));
								ex.setMessage(SmartUXProperties.getProperty("message.abms.socket"));
								throw ex;
							}
						}
					}
				}else{
					logger.warn("[AMBS 호출 오류][WARN][RESULT][" + result + "]");
					SmartUXException ex = new SmartUXException();
					ex.setFlag(SmartUXProperties.getProperty("flag.etc"));
					ex.setMessage(SmartUXProperties.getProperty("message.abms.socket"));
					throw ex;
				}
			}
		}
		abmsResultMap.put("abmsList", abmsList);
		
		return abmsResultMap;
	}
	
	public boolean abmsStatusComparison(String abms_staus) {
		boolean returnValue = false;		
		
		if (abms_staus.equals("120")         //제작중
				|| abms_staus.equals("130")  //검수중
				|| abms_staus.equals("145")  //앱배포중
				|| abms_staus.equals("150") ) { //테스트중
			returnValue = true;
		}
		
		return returnValue;
	}
	
	@Override
	public List<CategoryVO> getOtCategorySelect2(LinkInfoVO param) {
		return dao.getOtCategorySelect2(param);
	}


	@Override
	public List<CategoryVO> getOtCategorySelect1() {
		return dao.getOtCategorySelect1();
	}
	
	@Override
	public List<String> getPaperTerminal(String param) {
		return panelViewIptvDao.getPaperTerminal(param);
	}
	
	@Override
	public List<PanelTitleTreamVO> getLikePaperTerminal(String param) {
		return panelViewIptvDao.getLikePaperTerminal(param);
	}
}
