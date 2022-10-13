package com.dmi.smartux.smartstart.service;

import java.util.List;
import com.dmi.smartux.smartstart.vo.SmartStartItemListVO;

public interface SmartStartItemListService {

	/**
	 * SmartStartItemList을 조회하기 위해 사용되는 정보의 갯수를 조회하는 Service 인터페이스
	 * @param sa_id			가입번호
	 * @param stb_mac		맥주소
	 * @param app_type		어플타입
	 * @return				검색된 레코드 총 갯수
	 * @throws Exception
	 */
	int getSmartStartItemListCount( String sa_id, String stb_mac, String app_type , String callByScheduler) throws Exception;
	
	/**
	 * SmartStartItemList을 조회하기 위해 사용되는 정보를 조회하는 Service 인터페이스
	 * @param sa_id			가입번호
	 * @param stb_mac		맥주소
	 * @param app_type		어플타입
	 * @return				SmartStartItemList 정보가 들어있는 SmartStartItemListVO 객체가 들어있는 List 객체
	 * @throws Exception
	 */
	List<SmartStartItemListVO> getSmartStartItemList( String sa_id, String stb_mac, String app_type , String callByScheduler) throws Exception;
	
}	
