package com.dmi.smartux.admin.smartepg.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmi.smartux.admin.smartepg.dao.SmartEPGDao;
import com.dmi.smartux.admin.smartepg.service.SmartEPGService;
import com.dmi.smartux.admin.smartepg.vo.ThemeInfoVO;

@Service("adminSmartEPGService")
public class SmartEPGServiceImpl implements SmartEPGService{

	@Autowired
	SmartEPGDao dao;

	@Override
	@Transactional(readOnly=true)
	public List<ThemeInfoVO> getThemeInfoList() throws Exception {
		// TODO Auto-generated method stub
		return dao.getThemeInfoList();
	}

	@Override
	@Transactional
	public void insertThemeInfoProc(String theme_code, String theme_name, String use_yn, String create_id) throws Exception {
		// TODO Auto-generated method stub
		dao.insertThemeInfoProc(theme_code, theme_name, use_yn, create_id);
	}

	@Override
	@Transactional
	public ThemeInfoVO viewThemeInfo(String theme_code) throws Exception {
		// TODO Auto-generated method stub
		return dao.viewThemeInfo(theme_code);
	}

	@Override
	@Transactional
	public int updateThemeInfoProc(String theme_code, String theme_name, String use_yn, String update_id) throws Exception {
		// TODO Auto-generated method stub
		return dao.updateThemeInfoProc(theme_code, theme_name, use_yn, update_id);
	}

	@Override
	@Transactional
	public int deleteThemeInfoProc(String theme_code) throws Exception {
		// TODO Auto-generated method stub
		return dao.deleteThemeInfoProc(theme_code);
	}

	@Override
	@Transactional
	public void orderThemeInfoProc() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	
}
