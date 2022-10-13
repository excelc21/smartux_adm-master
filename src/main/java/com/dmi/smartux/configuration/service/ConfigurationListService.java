package com.dmi.smartux.configuration.service;

import java.util.List;

import com.dmi.smartux.configuration.vo.ConfigurationListVO;

public interface ConfigurationListService {

	/**
	 * ConfigurationList 조회하기 위해 사용되는 정보의 갯수를 조회하는 Service 인터페이스
	 * @param sa_id			가입번호
	 * @param stb_mac		맥주소
	 * @param app_type		어플타입
	 * @return				검색된 레코드 총 갯수
	 * @throws Exception
	 */
	int getConfigurationListCount( String sa_id, String stb_mac, String app_type , String callByScheduler) throws Exception;
	
	/**
	 * ConfigurationList 조회하기 위해 사용되는 정보를 조회하는 Service 인터페이스
	 * @param sa_id			가입번호
	 * @param stb_mac		맥주소
	 * @param app_type		어플타입
	 * @return				ConfigurationList 정보가 들어있는 ConfigurationListVO 객체가 들어있는 List 객체
	 * @throws Exception
	 */
	List<ConfigurationListVO> getConfigurationList(String sa_id, String stb_mac, String app_type , String callByScheduler) throws Exception;
	
	/**
	 * ConfigurationList 조회하기 위해 사용되는 정보를 조회하는 Service 인터페이스
	 * @param sa_id			가입번호
	 * @param stb_mac		맥주소
	 * @param app_type		어플타입
	 * @param config_id		설정정보ID
	 * @return				ConfigurationList 정보가 들어있는 ConfigurationListVO 객체가 들어있는 List 객체
	 * @throws Exception
	 */
	List<ConfigurationListVO> getConfigurationList(String sa_id, String stb_mac, String app_type , String callByScheduler, String config_id) throws Exception;
	
	/**
	 * ConfigurationList 조회하기 위해 사용되는 정보를 사전 체크하는 Service 인터페이스
	 * @param app_type		어플타입
	 * @return				ConfigurationList 정보가 들어있는 ConfigurationListVO 객체가 들어있는 List 객체
	 * @throws Exception
	 */
	List<ConfigurationListVO> getConfigChecklist(String app_type) throws Exception;
	
}	

