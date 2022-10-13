package com.dmi.smartux.admin.lifemessage.service;

import java.util.List;

import com.dmi.smartux.admin.lifemessage.vo.LifeMessageInfo;

public interface LifeMessageService {

	
	List getLifeMessageList() throws Exception;	
	void insertLifeMessage(LifeMessageInfo teleportInfo) throws Exception;
	void deleteLifeMessage(int[] orderList) throws Exception;
	LifeMessageInfo getViewLifeMessage(String type, String order) throws Exception;
	void updateLifeMessage(LifeMessageInfo teleportInfo) throws Exception;
}
