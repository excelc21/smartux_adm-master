package com.dmi.smartux.bonbang.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmi.smartux.bonbang.dao.RegistrationIDDao;
import com.dmi.smartux.bonbang.service.RegistrationIDService;
import com.dmi.smartux.bonbang.vo.RegistrationIDParamVO;
import com.dmi.smartux.common.exception.SmartUXException;
import com.dmi.smartux.common.property.SmartUXProperties;

/**
 * RegistrationID 관리 서비스 클래스
 * @author YJ KIM
 *
 */
@Service
public class RegistrationIDServiceImpl implements RegistrationIDService {

	@Autowired
	RegistrationIDDao dao;
	
	@Override
	//@Transactional 140708
	public void addRegistrationID(RegistrationIDParamVO pvo) throws Exception {
		
		//STB 페어링 등록
		if(pvo.getApp_type().equalsIgnoreCase("sma")){
			
			String regId = dao.getSMARegistrationID(pvo);
			
			if(regId == null) dao.insertSMARegistrationID(pvo);
			else if(!regId.equals(pvo.getReg_id())) dao.updateSMARegistrationID(pvo);
			
		//SMA 페어링 등록
		}else{
			
			String regId = dao.getUXRegistrationID(pvo);
			
			if(regId == null) dao.insertUXRegistrationID(pvo);
			else if(!regId.equals(pvo.getReg_id())) dao.updateUXRegistrationID(pvo);
			
		}
		
	}

	@Override
	//@Transactional 140708
	public void removeRegistrationID(RegistrationIDParamVO pvo) throws Exception {
		int result = dao.removeRegistrationID(pvo);
		
		//DB에서 삭제할 데이터가 존재 하지 않는경우 에러를 리턴한다
		if(result <= 0){
			SmartUXException exception = new SmartUXException();
			exception.setFlag(SmartUXProperties.getProperty("flag.beNotData"));
			exception.setMessage(SmartUXProperties.getProperty("message.beNotData"));
			throw exception;
		}		
	}
}
