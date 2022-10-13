package com.dmi.smartux.admin.gpack.contents.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmi.smartux.admin.gpack.category.dao.GpackCategoryViewDao;
import com.dmi.smartux.admin.gpack.category.vo.GpackCategoryVO;
import com.dmi.smartux.admin.gpack.contents.dao.GpackContentsViewDao;
import com.dmi.smartux.admin.gpack.contents.service.GpackContentsViewService;
import com.dmi.smartux.admin.gpack.contents.vo.GpackContentsAutoVO;
import com.dmi.smartux.admin.gpack.contents.vo.GpackContentsVO;

@Service
public class GpackContentsViewServiceImpl implements GpackContentsViewService {
	
	private final Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	GpackContentsViewDao dao;
	@Autowired
	GpackCategoryViewDao category_dao;
	
	@Override
	@Transactional(readOnly=true)
	public List<GpackContentsVO> getGpackContentsList(GpackContentsVO contentsVO) throws Exception {
		return dao.getGpackContentsList(contentsVO);
	}

	@Override
	@Transactional(readOnly=true)
	public GpackContentsVO getGpackContentsView(GpackContentsVO contentsVO) throws Exception {
		return dao.getGpackContentsView(contentsVO);
	}

	@Override
	@Transactional
	public void insertGpackContents(GpackContentsVO contentsVO) throws Exception {
		dao.insertGpackContents(contentsVO);
	}

	/*
	@Override
	@Transactional
	public int updateGpackContents(GpackContentsVO contentsVO) throws Exception {
		int updcnt = dao.updateGpackContents(contentsVO);
		return updcnt;
	}
	*/

	@Override
	@Transactional
	public void deleteGpackContents(String pack_id, String category_id, String[] contents_ids, String login_id) throws Exception {
		GpackContentsVO contentsVO = new GpackContentsVO();
		for(String contents_id : contents_ids){
			contentsVO.setPack_id(pack_id);
			contentsVO.setCategory_id(category_id);
			contentsVO.setContents_id(contents_id);
			dao.deleteGpackContents(contentsVO);		
		}
	}

	@Override
	@Transactional(readOnly=true)
	public GpackContentsAutoVO getGpackContentsAutoView(GpackContentsAutoVO contentsAutoVO) throws Exception {
		return dao.getGpackContentsAutoView(contentsAutoVO);
	}
	
	@Override
	@Transactional(readOnly=true)
	public GpackContentsAutoVO getGpackOneCategory(GpackContentsAutoVO contentsAutoVO) throws Exception {
		return dao.getGpackOneCategory(contentsAutoVO);
	}

	@Override
	@Transactional
	public void insertGpackContentsAuto(GpackContentsAutoVO contentsAutoVO) throws Exception {
		dao.insertGpackContentsAuto(contentsAutoVO);
	}

	/*
	@Override
	@Transactional
	public int updateGpackContentsAuto(GpackContentsAutoVO contentsAutoVO) throws Exception {
		int updcnt = dao.updateGpackContentsAuto(contentsAutoVO);

		return updcnt;
	}
	*/

	@Override
	@Transactional
	public void deleteGpackContentsAuto(GpackContentsAutoVO contentsAutoVO) throws Exception {
		dao.deleteGpackContentsAuto(contentsAutoVO);
	}
	
	
	public void updateGpackContentsOrderby(String pack_id, String category_id, String[] contents_ids, String update_id) throws Exception {
		GpackContentsVO contentsVO = new GpackContentsVO();
		int ordered = 1;
		for(String contents_id : contents_ids){
			contentsVO.setPack_id(pack_id);
			contentsVO.setCategory_id(category_id);
			contentsVO.setContents_id(contents_id);
			contentsVO.setOrdered(Integer.toString(ordered++));
			contentsVO.setUpdate_id(update_id);
			dao.updateGpackContentsOrderby(contentsVO);
		}
	}

	@Override
	@Transactional(readOnly=true)
	public List getImcsCategoryList (String category_id, String gubun, String callby) throws Exception {
		return dao.getImcsCategoryList(category_id, gubun, callby);
	}

	@Override
	@Transactional(readOnly=true)
	public List getPrevCategoryContentsList(String pack_id, String category_id) throws Exception {

		GpackCategoryVO categoryVO = new GpackCategoryVO();
		categoryVO.setPack_id(pack_id);
		categoryVO.setCategory_id(category_id);
		categoryVO = category_dao.getGpackCategoryView(categoryVO);
		
		String auto_yn = categoryVO.getAuto_yn();

		List contentsList = new ArrayList();
		if(auto_yn.equals("Y"))
			contentsList = dao.getPrevCategoryAutoList(pack_id, category_id);
		else
			contentsList = dao.getPrevCategoryContentsList(pack_id, category_id);
		
		return contentsList;
	}

	@Override
	@Transactional(readOnly=true)
	public List getPrevCategoryContentsBizList(String pack_id, String category_id) throws Exception {

		GpackCategoryVO categoryVO = new GpackCategoryVO();
		categoryVO.setPack_id(pack_id);
		categoryVO.setCategory_id(category_id);
		categoryVO = category_dao.getGpackCategoryBizView(categoryVO);
		
		String auto_yn = categoryVO.getAuto_yn();

		List contentsList = new ArrayList();
		if(auto_yn.equals("Y"))
			contentsList = dao.getPrevCategoryAutoBizList(pack_id, category_id);
		else
			contentsList = dao.getPrevCategoryContentsBizList(pack_id, category_id);
		
		return contentsList;
	}
}
