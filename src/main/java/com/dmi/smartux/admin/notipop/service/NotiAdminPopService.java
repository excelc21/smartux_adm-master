package com.dmi.smartux.admin.notipop.service;

import com.dmi.smartux.admin.notipop.vo.getEmergencyViewVo;
import com.dmi.smartux.admin.notipop.vo.getEmergencyVo;
import com.dmi.smartux.admin.notipop.vo.notiPopVo;

public interface NotiAdminPopService {

	/**
	 * 데이터 저장 및 수정
	 * 
	 * @param addnotipopVo
	 * @param callType 긴급:EM 팝업:PO
	 * @throws Exception
	 */
	public void callInsertNotiPopProc ( notiPopVo notipopVo, String callType ) throws Exception;

	/**
	 * 데이터 삭제
	 * 
	 * @param notipopVo
	 * @param callType 긴급:EM 팝업:PO
	 * @throws Exception
	 */
	public void callDeleteNotiPopProc ( notiPopVo notipopVo, String callType ) throws Exception;

	/**
	 * 긴급(비상)공지 조회
	 * 
	 * @param getemergencyVo
	 * @return
	 * @throws Exception
	 */
	public getEmergencyViewVo getEmergencyView ( getEmergencyVo getemergencyVo ) throws Exception;

}