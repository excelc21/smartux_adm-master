package com.dmi.smartux.admin.commonMng.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmi.smartux.admin.commonMng.dao.CommonModuleDao;
import com.dmi.smartux.admin.commonMng.service.CommonModuleService;
import com.dmi.smartux.admin.commonMng.vo.CategoryAlbumListVo;
import com.dmi.smartux.admin.commonMng.vo.ChannelVO;
import com.dmi.smartux.admin.commonMng.vo.EventCategoryAlbumVo;
import com.dmi.smartux.admin.commonMng.vo.FlatRateVO;
import com.dmi.smartux.admin.commonMng.vo.MenuTreeVo;

@Service
public class CommonModuleServiceImpl implements CommonModuleService{
	
	@Autowired
	CommonModuleDao dao;

	@Override
	public List<EventCategoryAlbumVo> getEventCategoryAlbumList(CategoryAlbumListVo categoryalbumlistVo) throws Exception {
		return dao.getEventCategoryAlbumList(categoryalbumlistVo);
	}
	
	
	@Override
	public List<EventCategoryAlbumVo> searchEventCategoryAlbumList(CategoryAlbumListVo categoryalbumlistVo)throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		
		
		param.put("categoryId", categoryalbumlistVo.getCategoryId());
		param.put("type", categoryalbumlistVo.getType());
		param.put("searchType", categoryalbumlistVo.getSearchType());
		param.put("searchVal", categoryalbumlistVo.getSearchVal());
		
		return dao.searchEventCategoryAlbumList(param);
	}
	
	@Override
	public List<FlatRateVO> getFlatRateList() throws Exception {
		return dao.getFlatRateList();
	}

	@Override
	public List<ChannelVO> getLiveCHList() throws Exception {
		return dao.getLiveCHList();
	}
	
	@Override
	public EventCategoryAlbumVo getContentsView(String categoryId, String contentsId) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		
		
		param.put("categoryId", categoryId);
		param.put("contentsId", contentsId);
		return dao.getContentsView(param);
	}
	
	@Override
	public EventCategoryAlbumVo getCategoryView(String categoryId) throws Exception {
		return dao.getCategoryView(categoryId);
	}

	@Override
	public List<HashMap<String, String>> getBannerMasterPop() throws Exception {
		return dao.getBannerMasterPop();
	}
	
	/**
	 * 메뉴트리 정보조회
	 */
	@Override
	public MenuTreeVo selectMenuTreeInfo(String menu_id) throws Exception {
		return dao.selectMenuTreeInfo(menu_id);
	}
	
	/**
	 * MIMS카테고리 조회
	 */
	@Override
	public List<HashMap<String, String>> getMimsCategoryList(Map param) throws Exception {
		return dao.getMimsCategoryList(param);
	}
	
	/**
	 * MIMS 카테고리로 구성된 카테고리 아이인지 조회
	 */
	@Override
	public String getMimsCategorySubYn(Map<String, String> param) {
		return dao.getMimsCategorySubYn(param);
	}
	
	/**
	 * MIMS카테고리 조회
	 */
	@Override
	public List<HashMap<String, String>> getMimsIptvCategoryList(Map param) throws Exception {
		return dao.getMimsIptvCategoryList(param);
	}
	
	/**
	 * MIMS 카테고리로 구성된 카테고리 아이인지 조회
	 */
	@Override
	public String getMimsIptvCategorySubYn(Map<String, String> param) {
		return dao.getMimsIptvCategorySubYn(param);
	}


	@Override
	public List<FlatRateVO> getFlatRateList2() {
		return dao.getFlatRateList2();
	}
}
