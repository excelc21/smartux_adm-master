package com.dmi.smartux.admin.gpack.category.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmi.smartux.admin.gpack.category.dao.GpackCategoryViewDao;
import com.dmi.smartux.admin.gpack.category.service.GpackCategoryViewService;
import com.dmi.smartux.admin.gpack.category.vo.GpackCategoryVO;
import com.dmi.smartux.admin.gpack.contents.dao.GpackContentsViewDao;
import com.dmi.smartux.admin.gpack.contents.vo.GpackContentsAutoVO;
import com.dmi.smartux.admin.gpack.contents.vo.GpackContentsVO;

@Service
public class GpackCategoryViewServiceImpl implements GpackCategoryViewService {
	
	private final Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	GpackCategoryViewDao dao;
	@Autowired
	GpackContentsViewDao contentsdao;

	@Override
	@Transactional(readOnly=true)
	public int countGpackCategoryContents(GpackCategoryVO categoryVO)  throws Exception {
		return dao.countGpackCategoryContents(categoryVO);
	}

	@Override
	@Transactional(readOnly=true)
	public String getNewGpackCategoryId() throws Exception{
		return dao.getNewGpackCategoryId();
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<GpackCategoryVO> getGpackCategoryList(GpackCategoryVO categoryVO) throws Exception {
		return dao.getGpackCategoryList(categoryVO);
	}

	@Override
	@Transactional(readOnly=true)
	public GpackCategoryVO getGpackCategoryView(GpackCategoryVO categoryVO) throws Exception {
		return dao.getGpackCategoryView(categoryVO);
	}

	@Override
	@Transactional
	public void insertGpackCategory(GpackCategoryVO categoryVO) throws Exception {

		//자동여부가 바뀌면 하위 콘텐츠 삭제 
		GpackCategoryVO CateTmpVO = new GpackCategoryVO();
		CateTmpVO = dao.getGpackCategoryView(categoryVO);
		if(CateTmpVO != null) {
			String type = CateTmpVO.getAuto_yn();
			if(type!=null && !type.equals(categoryVO.getAuto_yn())){
				deleteCategoryContents(categoryVO.getPack_id(), categoryVO.getCategory_id());
			}
		}
		
		//Todo : 반영여부 - 즉시 반영일 경우 이 후에 api 호출해서 캐시에 담고 따로 update
		dao.insertGpackCategory(categoryVO);
	}

	/*
	@Override
	@Transactional
	public int updateGpackCategory(GpackCategoryVO categoryVO) throws Exception {
		
		GpackCategoryVO CateTmpVO = new GpackCategoryVO();
		CateTmpVO = dao.getGpackCategoryView(categoryVO);
		
		//자동여부가 바뀌면 하위 콘텐츠 삭제 
		String type = CateTmpVO.getAuto_yn();
		
		if(!type.equals(categoryVO.getAuto_yn())){
			deleteCategoryContents(categoryVO.getPack_id(), categoryVO.getCategory_id());
		}

		//Todo :  반영여부 - 즉시 반영일 경우 이 후에 api 호출해서 캐시에 담고 따로 update
		int updcnt = dao.updateGpackCategory(categoryVO);
		return updcnt;
	}
	*/
	
	@Override
	@Transactional
	public void deleteGpackCategory(String pack_id, String[] category_ids) throws Exception {
		
		GpackCategoryVO categoryVO = new GpackCategoryVO();
		for(String category_id : category_ids){
			categoryVO.setPack_id(pack_id);
			categoryVO.setCategory_id(category_id);

			//카테고리 삭제시 하위 콘텐츠 삭제 
			deleteCategoryContents(categoryVO.getPack_id(), categoryVO.getCategory_id());
			
			dao.deleteGpackCategory(categoryVO);
		}
	}
	
	/**
	 * 카테고리 순서 바꾸기 
	 */
	public void updateGpackCategoryOrderby(String pack_id, String[] category_ids, String update_id) throws Exception {
		GpackCategoryVO categoryVO = new GpackCategoryVO();
		int ordered = 1;
		for(String category_id : category_ids){
			categoryVO.setPack_id(pack_id);
			categoryVO.setCategory_id(category_id);
			categoryVO.setOrdered(Integer.toString(ordered++));
			categoryVO.setUpdate_id(update_id);
			dao.updateGpackCategoryOrderby(categoryVO);
		}
	}
	
	/**
	 * 해당 카테고리에 연관된 콘텐츠 삭제 (난 VO에 담는게 편하다구!!)
	 * @param pack_id
	 * @param category_id
	 * @throws Exception
	 */
	public void deleteCategoryContents(String pack_id, String category_id) throws Exception {
		
		GpackContentsVO contentsVO = new GpackContentsVO();
		GpackContentsAutoVO contentsAutoVo = new GpackContentsAutoVO();
		if(pack_id != null && pack_id.length() > 0 && category_id != null && category_id.length() > 0){
			//vod수동
			//contentsVO.setPack_id(pack_id);
			//contentsVO.setCategory_id(category_id);
			//contentsdao.deleteGpackContents(contentsVO);
			//vod자동
			contentsAutoVo.setPack_id(pack_id);
			contentsAutoVo.setCategory_id(category_id);
			contentsdao.deleteGpackContentsAuto(contentsAutoVo);
		}
	}

	public List<GpackCategoryVO> previewGpackCategory(GpackCategoryVO categoryVO, String preview_gb) throws Exception{
		List<GpackCategoryVO> result = new ArrayList<GpackCategoryVO>();

		if(preview_gb.equals("BIZ")){
			result = dao.previewGpackCategory(categoryVO, preview_gb);
		}else{
			result = dao.getGpackCategoryList(categoryVO);
		}
		
		return result;
		
	}
}
