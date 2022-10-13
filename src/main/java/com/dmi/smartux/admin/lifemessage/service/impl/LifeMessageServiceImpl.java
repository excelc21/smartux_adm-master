package com.dmi.smartux.admin.lifemessage.service.impl;

import com.dmi.smartux.admin.lifemessage.service.LifeMessageService;
import com.dmi.smartux.admin.lifemessage.vo.LifeMessageInfo;
import com.dmi.smartux.common.util.JsonFileator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LifeMessageServiceImpl implements LifeMessageService {

	private final JsonFileator lifemessage;
	@Autowired
	public LifeMessageServiceImpl(@Qualifier("lifemessage") JsonFileator lifeMessage) {
		this.lifemessage = lifeMessage;
	}

	@Override
	public List getLifeMessageList() throws Exception {
		return this.lifemessage.getList();
	}

	@Override
	public void insertLifeMessage(LifeMessageInfo lifeMessageInfo) throws Exception {
		this.lifemessage.insert(lifeMessageInfo);
	}


	@Override
	public void deleteLifeMessage(int[] orderList) throws Exception {
		this.lifemessage.delete(orderList);
	}

	@Override
	public LifeMessageInfo getViewLifeMessage(String type, String order) throws Exception {
		LifeMessageInfo view = new LifeMessageInfo(); 
		List lifeMessageList = this.lifemessage.getList(type, order);
		view = (LifeMessageInfo)lifeMessageList.get(0);
		return view;
	}
	
	@Override
	public void updateLifeMessage(LifeMessageInfo lifeMessageInfo) throws Exception {
		this.lifemessage.update(lifeMessageInfo);
	}
}
