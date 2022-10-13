package com.dmi.smartux.common.module;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;

import com.dmi.smartux.common.property.SmartUXProperties;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.vo.StatPushLogVo;

/**
 * @author 	deoksan
 * @package	com.dmi.smartux.common.module
 * @name 	StatPushLogger.java
 * @comment	통계푸시로거모듈
 */
public class StatPushLogger {
	// 푸시로그
	public void writePushLog(StatPushLogVo vo) {
		
		String txt = "";
		String logType = "SVC";			// SVC
		String svcName = "SECONDTV";	// 서비스명(세컨드TV)
		String devInfo = GlobalCom.isNull(vo.getDev_info());
		devInfo = ( "".equals(devInfo) ) ? "SERVE" : devInfo;
		String osInfo  = GlobalCom.isNull(vo.getOs_info());
		osInfo  = ( "".equals(devInfo) ) ? "LINUX" : osInfo;
		
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
		txt += "OS_INFO=" 		+ osInfo + "|";
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
        writeLog(txt);
//		logger.info(txt);
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

		String errCode;
		
		// 에러구분 - 필수 파람 미존재시
		if ("".equals(clientIp) && "".equals(devInfo) && "".equals(resCode)) {
			errCode = "1000";
		} 
		else {
			if ("".equals(clientIp) && "".equals(devInfo)) {
				errCode = "2000";
			} 
			else if ("".equals(devInfo) && "".equals(resCode)) {
				errCode = "3000";
			} 
			else if ("".equals(resCode) && "".equals(clientIp)) {
				errCode = "4000";
			}
			else {
				if ("".equals(clientIp)) {
					errCode = "5000";
				} 
				else if ("".equals(devInfo)) {
					errCode = "6000";
				} 
				else if ("".equals(resCode)) {
					errCode = "7000";
				}
				else {
					errCode = resCode;
				}
			}
		}
		
		// 푸시결과세팅
		resCode = ( "0000".equals(resCode) ) ? "20000000" : "3000" + errCode;
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

    /**
     * 로그 파일을 만든다.
     *
     * @return 성공 여부
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public boolean makeFile() {
        boolean result = true;

        File folder = new File(getLogFolderPath());

        if (!folder.exists()) {
            folder.mkdirs();
        }

        File file = new File(getLogFullPath());

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ignored) {
                result = false;
            }
        }

        return result;
    }

    /**
     * 로그를 남긴다.
     *
     * @param text 남길 로그
     */
    private synchronized void writeLog(String text) {
        makeFile();

        BufferedWriter resultFile = null;

        try {
            resultFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(getLogFullPath(), true), "UTF-8"));
            resultFile.write(text);
            resultFile.newLine();
        } catch (UnsupportedEncodingException ignored) {
        } catch (FileNotFoundException ignored) {
        } catch (IOException ignored) {
        } finally {
            if (null != resultFile) {
                try {
                    resultFile.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    /**
     * 로그 폴더 경로를 가져온다.
     *
     * @return 로그 폴더 경로
     */
    private String getLogFolderPath() {
        String folderPath = GlobalCom.isNull(SmartUXProperties.getPathProperty("total.stat.path"), "/app/smartux/total_log/");
        return folderPath + new SimpleDateFormat("yyyyMMdd").format(new Date()) + "/";
    }

    /**
     * 로그 파일 명을 5분 단위로 가져온다.
     *
     * @return 로그 파일명
     */
    private String getLogFileName() {
        String index = GlobalCom.isNull(GlobalCom.getSystemProperty("server.index"), "1");

        Date date = new Date();

        long minutes = DateUtils.getFragmentInMinutes(date, Calendar.HOUR_OF_DAY);

        // 5분 단위 절삭
        minutes = minutes / 5;
        minutes = minutes * 5;

        date = DateUtils.setMinutes(date, (int) minutes);

        return "SECONDTV." + GlobalCom.pad(index, 3) + "." + new SimpleDateFormat("yyyyMMddHHmm").format(date) + ".log";
    }

    /**
     * 로그 파일명이 포함된 풀 경로를 가져온다.
     *
     * @return 풀 경로
     */
    private String getLogFullPath() {
        return getLogFolderPath() + getLogFileName();
    }
}
