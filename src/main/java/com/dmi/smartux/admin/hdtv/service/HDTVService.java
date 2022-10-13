package com.dmi.smartux.admin.hdtv.service;

import com.dmi.smartux.admin.hdtv.vo.HDTVVO;

public interface HDTVService {

	public HDTVVO getHDTVStartupXml() throws Exception;

	public void setHDTVStartupXml(HDTVVO vo)throws Exception;
}
