package com.dmi.smartux.authentication.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmi.smartux.authentication.dao.AuthenticationDao;
import com.dmi.smartux.authentication.service.AuthenticationService;
import com.dmi.smartux.authentication.vo.AuthenticationCommon;
import com.dmi.smartux.authentication.vo.AuthenticationVO;

import javax.annotation.PostConstruct;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	@Autowired
	AuthenticationDao dao;

	@PostConstruct
	public void initService() {
		try {
			setAuthDataReload();
		} catch (Exception ignored) {
		}
	}
	
	@Override
	public void setAuthUpdate(String mapKey, AuthenticationVO data)
			throws Exception {
		//AuthenticationCommon.AUTH_MAP.remove(mapKey);
		//AuthenticationCommon.AUTH_MAP.put(mapKey, data);
		
		AuthenticationVO mapData = AuthenticationCommon.AUTH_MAP.get(mapKey);
		
		//업데이트 시 변경되어야 할 정보들
		mapData.setUse(data.getUse());
		mapData.setService_life(data.getService_life());
		
		AuthenticationCommon.AUTH_MAP.remove(mapKey);
		AuthenticationCommon.AUTH_MAP.put(mapKey, mapData);
	}

	@Override
	public void setAuthDelete(String mapKey) throws Exception {
		AuthenticationCommon.AUTH_MAP.remove(mapKey);
	}

	@Override
	public void setAuthInsert(String mapKey, AuthenticationVO data)
			throws Exception {
		AuthenticationCommon.AUTH_MAP.put(mapKey, data);
	}

	/**
	 * 안씀.
	 */
	@Override
	public void setAuthDataReload(String systemCode) throws Exception {		
		List<AuthenticationVO> list = dao.getAuthSystemData(systemCode);
		
		System.out.println("list size===================="+list.size());
		
		if(list != null){
			AuthenticationCommon.AUTH_MAP.clear();
			for(int i=0;i<list.size();i++){
				AuthenticationVO data = (AuthenticationVO)list.get(i);
				
				//구분 키 값이 존재 하여야만 MAP에 입력
//				if(	    !data.getKey_type().equals("")
//					&&	!data.getService_id().equals("")
//					&&	!data.getApi_id().equals("")
//				){
//					String mapKey = data.getKey_type() + AuthenticationCommon.GBN + data.getService_id() + AuthenticationCommon.GBN + data.getApi_id();
//					AuthenticationCommon.AUTH_MAP.put(mapKey, data);
//				}
				
				if(!data.getAccess_key().equals("")){
					AuthenticationCommon.AUTH_MAP.put(data.getAccess_key(), data);
				}
			}
		}
		
	}

	@Override
	public void setAuthDataReload() throws Exception {
		// TODO Auto-generated method stub
		List<AuthenticationVO> list = dao.getAuthSystemData();
		
		System.out.println("list size===================="+list.size());
		
		if(list != null){
			AuthenticationCommon.AUTH_MAP.clear();
			for(int i=0;i<list.size();i++){
				AuthenticationVO data = (AuthenticationVO)list.get(i);
				
				//구분 키 값이 존재 하여야만 MAP에 입력
//				if(	    !data.getKey_type().equals("")
//					&&	!data.getService_id().equals("")
//					&&	!data.getApi_id().equals("")
//				){
//					String mapKey = data.getKey_type() + AuthenticationCommon.GBN + data.getService_id() + AuthenticationCommon.GBN + data.getApi_id();
//					AuthenticationCommon.AUTH_MAP.put(mapKey, data);
//				}
				
				if(!data.getAccess_key().equals("")){
					AuthenticationCommon.AUTH_MAP.put(data.getAccess_key(), data);
				}
			}
		}
	}
	
}
