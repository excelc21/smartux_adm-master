package com.dmi.smartux.admin.notimng.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.dmi.smartux.admin.notimng.dao.NotiMngViewDao;
import com.dmi.smartux.admin.notimng.service.NotiMngViewService;
import com.dmi.smartux.admin.notimng.vo.NotiMngViewVO;

@Repository
public class NotiMngViewServiceImpl implements NotiMngViewService {

	@Autowired
	NotiMngViewDao dao;

	private final Log logger = LogFactory.getLog ( getClass ( ) );

	@Override
	public List<Map<String, String>> getNotiMngViewList ( String bbs_Gbn, String scr_Type ) {
		List<Map<String, String>> data = null;
		data = dao.getNotiMngViewList ( bbs_Gbn, scr_Type );
		return data;

	}

	@Override
	@Transactional
	public void setNotiMngUpdate ( NotiMngViewVO vo ) throws Exception {
		dao.setNotiMngUpdate ( vo.getBbsId ( ), vo.getBbsNm ( ), vo.getModId ( ), vo.getAct_id ( ), vo.getAct_ip ( ) );

	}

	@Override
	@Transactional
	public String setNotiMngInsert ( NotiMngViewVO vo ) throws Exception {
		String result = dao.setNotiMngInsert ( vo.getBbsId ( ), vo.getBbsNm ( ), vo.getBbsGbn ( ), vo.getVerSion ( ), vo.getRegId ( ), vo.getScr_Type ( ), vo.getAct_id ( ), vo.getAct_ip ( ) );

		return result;
	}

	@Override
	@Transactional
	public void NotiMngDelete ( NotiMngViewVO vo ) throws Exception {
		dao.setNotiMngDelete ( vo.getBbsId ( ), vo.getBbsNm ( ), vo.getDelYn ( ), vo.getAct_id ( ), vo.getAct_ip ( ) );
	}
}
