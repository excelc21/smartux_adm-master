package com.dmi.smartux.admin.teleport.service;

import java.util.List;

import com.dmi.smartux.admin.mainpanel.vo.ViewVO;
import com.dmi.smartux.admin.teleport.vo.TeleportInfo;

public interface TeleportService {
	List getTeleportList(String searchType, String searchText) throws Exception;
	String insertTeleport(TeleportInfo teleportInfo) throws Exception;
	void deleteList(int[] orderList) throws Exception;
	String setTeleport(TeleportInfo teleportInfo) throws Exception;
	void setTeleportOrder(int[] codeList) throws Exception;
}
