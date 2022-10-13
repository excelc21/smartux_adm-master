package com.dmi.smartux.admin.abtest.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmi.smartux.admin.abtest.dao.BPASDao;
import com.dmi.smartux.admin.abtest.service.BPASService;
import com.dmi.smartux.admin.abtest.vo.BPASListVo;
import com.dmi.smartux.admin.abtest.vo.BPASSearchVo;
import com.dmi.smartux.admin.abtest.vo.CategoryListVo;
import com.dmi.smartux.admin.abtest.vo.PanelListVo;
import com.dmi.smartux.admin.abtest.vo.TestListVo;


/**
 * 2021.04.16 AB테스트 MIMS
 * AB 테스트 지면 ServiceImpl
 * @author medialog
 *
 */
@Service
public class BPASServiceImpl implements BPASService {

	private Logger logger = LoggerFactory.getLogger("abms_paper");
	
	@Autowired
	BPASDao dao;

	@Override
	public List<BPASListVo> getBPASList(BPASSearchVo searchVo)throws Exception {
		return dao.getBPASList(searchVo);
	}

	@Override
	public int getBPASListCount(BPASSearchVo searchVo) throws Exception {
		return dao.getBPASListCount(searchVo);
	}

	@Override
	public List<CategoryListVo> getCategoryList(String category_id) {
		return dao.getCategoryList(category_id);
	}

	@Override
	public List<CategoryListVo> getCategoryIdList(String imcs_id) {
		return dao.getCategoryIdList(imcs_id);
	}

	@Override
	public List<PanelListVo> getPanelList(String category_id) {
		return dao.getPanelList(category_id);
	}

	@Override
	public List<PanelListVo> getPanelList2(PanelListVo param) {
		return dao.getPanelList2(param);
	}

	@Override
	public List<PanelListVo> getPanelList3(String ads_no) {
		return dao.getPanelList3(ads_no);
	}

	@Override
	public List<PanelListVo> getPanelList4(String content_id) {
		return dao.getPanelList4(content_id);
	}

	@Override
	public List<TestListVo> getTestIdInfo(BPASSearchVo vo) {
		return dao.getTestIdInfo(vo);
	}

	@Override
	public List<TestListVo> getTestIdInfo2(BPASSearchVo vo) {
		return dao.getTestIdInfo2(vo);
	}

}
