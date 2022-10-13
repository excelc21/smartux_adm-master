package com.dmi.smartux.admin.smartepg.service;

import java.util.List;

import com.dmi.smartux.admin.smartepg.vo.ThemeInfoVO;

public interface SmartEPGService {

	public List<ThemeInfoVO> getThemeInfoList() throws Exception;
	public void insertThemeInfoProc(String theme_code, String theme_name, String use_yn, String create_id) throws Exception;
	public ThemeInfoVO viewThemeInfo(String theme_code) throws Exception;
	public int updateThemeInfoProc(String theme_code, String theme_name, String use_yn, String update_id) throws Exception;
	public int deleteThemeInfoProc(String theme_code) throws Exception;
	public void orderThemeInfoProc() throws Exception;
}
