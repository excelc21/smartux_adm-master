package com.dmi.smartux.authentication.service;

import com.dmi.smartux.authentication.vo.AuthenticationVO;

public interface AuthenticationService {

	public void setAuthUpdate(String mapKey, AuthenticationVO data) throws Exception;

	public void setAuthDelete(String mapKey)  throws Exception;

	public void setAuthInsert(String mapKey, AuthenticationVO data) throws Exception;

	public void setAuthDataReload(String systemCode) throws Exception;

	public void setAuthDataReload() throws Exception;
	
}
