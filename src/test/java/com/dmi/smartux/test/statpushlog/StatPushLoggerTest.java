package com.dmi.smartux.test.statpushlog;

import java.util.LinkedHashMap;

import org.junit.Test;

import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.vo.StatPushLogVo;

public class StatPushLoggerTest {

	@Test
	public void writePushLog() {
		
		StatPushLogVo vo = new StatPushLogVo(); 

		// 기본정보 세팅
		vo.setSid("11223344556677");
		vo.setClient_ip("120.0.0.1");
		vo.setDev_info("PHONE");
		vo.setResult_code("0000");

		// 추가정보 세팅
		LinkedHashMap<String,String> info = new LinkedHashMap<String,String>();
		info.put("PUSH_STYPE", "N");
		info.put("PUSH_ID", "123");
		info.put("PUSH_LTYPE", "S");
		info.put("PUSH_SENDCNT", "100");
		info.put("PUSH_NAME", "PUSH TEST");
		info.put("PUSH_TYPE1", "P");
		info.put("PUSH_TYPE2", "N");
		info.put("PUSH_REGDATE", "20140903");
		vo.setAddInfo(info);
		
		String txt = "";
		String logType = "SVC";
		String svcName = "SUMX";
		String devInfo = GlobalCom.isNull(vo.getDev_info());
		devInfo = ( "".equals(devInfo) ) ? "serve" : devInfo;
		
		// 1. set 필수정보
		txt += "SEQ_ID=" 		+ createSeqId() + "|";
		txt += "LOG_TIME=" 		+ GlobalCom.getTodayFormat4_24() + "|";
		txt += "LOG_TYPE=" 		+ logType + "|";
		txt += "SID=" 			+ GlobalCom.isNull(vo.getSid()) + "|";
		txt += "RESULT_CODE=" 	+ setPushResult(vo) + "|";
		txt += "REQ_TIME=" 		+ GlobalCom.getTodayFormat4_24_m() + "|";
		txt += "RSP_TIME=" 		+ GlobalCom.getTodayFormat4_24_m() + "|";
		txt += "CLIENT_IP=" 	+ GlobalCom.isNull(vo.getClient_ip()) + "|";
		txt += "DEV_INFO=" 		+ devInfo + "|";
		txt += "OS_INFO=" 		+ GlobalCom.isNull(vo.getOs_info()) + "|";
		txt += "NW_INFO=" 		+ GlobalCom.isNull(vo.getNw_info()) + "|";
		txt += "SVC_NAME=" 		+ svcName + "|";
		txt += "DEV_MODEL=" 	+ GlobalCom.isNull(vo.getDev_model()) + "|";
		txt += "CARRIER_TYPE=" 	+ GlobalCom.isNull(vo.getCarrier_type()) + "|";

		// 2. set 추가정보
		LinkedHashMap<String,String> pushInfo = new LinkedHashMap<String,String>();
		pushInfo = vo.getAddInfo();
		
		for (String k:pushInfo.keySet()) {
			txt += k + "=" + pushInfo.get(k) + "|";
		}
		
		// 3. 로그남기기
		System.out.println("[writePushLog] ############# ");
		System.out.println(txt);
	}
	
	/**
	 * @Method Name : setPushResult
	 * @Comment : 푸시전송결과값 세팅(필수 파라미터 체크)
	 */
	public String setPushResult(StatPushLogVo vo) {
		
		// 필수 파람 - 반드시 존재해야함
		String clientIp = GlobalCom.isNull(vo.getClient_ip());	// 접속 클라이언트 IP
		String devInfo  = GlobalCom.isNull(vo.getDev_info());	// 접속 단말타입
		String resCode  = GlobalCom.isNull(vo.getResult_code());// 푸시 발송결과

		String errCode  = "000";	// 에러코드(000:정상)
		
		// 에러구분 - 필수 파람 미존재시
		if ("".equals(clientIp) && "".equals(devInfo) && "".equals(resCode)) {
			errCode = "100";
		} 
		else {
			if ("".equals(clientIp) && "".equals(devInfo)) {
				errCode = "200";
			} 
			else if ("".equals(devInfo) && "".equals(resCode)) {
				errCode = "300";
			} 
			else if ("".equals(resCode) && "".equals(clientIp)) {
				errCode = "400";
			}
			else {
				if ("".equals(clientIp)) {
					errCode = "500";
				} 
				else if ("".equals(devInfo)) {
					errCode = "600";
				} 
				else if ("".equals(resCode)) {
					errCode = "700";
				}
				else {
					errCode = "800";
				}
			}
		}
		
		// 푸시결과세팅
		resCode = ( "0000".equals(resCode) ) ? "20000000" : "30000" + errCode;
		return resCode;
	}
	
	/**
	 * @Method Name : createSeqId
	 * @Comment : 시퀀스 ID 생성 (ex> YYYYMMDDHHmmSSsss+[난수4자리]+[난수4자리])
	 */
	private String createSeqId() {

		String currTime = GlobalCom.getTodayFormat4_24_m();	// yyyyMMddHHmmssSSS
		String randomNo = "";
		for (int i=0;i<2;i++) {
			randomNo += String.valueOf((int)(Math.random() * 10000));
		}
		return currTime + randomNo;
	}
}
	
