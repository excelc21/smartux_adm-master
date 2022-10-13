package com.dmi.smartux.bonbang.service;

import com.dmi.smartux.bonbang.vo.RegistrationIDParamVO;

/**
 * RegistrationID 관리 서비스 인터페이스
 * @author YJ KIM
 *
 */
public interface RegistrationIDService {
	
	/**
	 * RegistrationID 등록
	 * @param pvo	RegistrationIDParamVO
	 */
	public void addRegistrationID(RegistrationIDParamVO pvo) throws Exception;

	/**
	 * RegistrationID 삭제
	 * @param pvo	RegistrationIDParamVO
	 */
	public void removeRegistrationID(RegistrationIDParamVO pvo) throws Exception ;


	
	
}
	

