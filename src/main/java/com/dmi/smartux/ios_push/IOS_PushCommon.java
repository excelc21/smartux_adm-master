package com.dmi.smartux.ios_push;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.ios_push.vo.IOS_PushSocketVO;

/**
 * IOS Push G/W 사용시 공통 변수 저장 클래스
 * @author YJ KIM
 *
 */
public class IOS_PushCommon {

	private final Log logger = LogFactory.getLog(this.getClass());
	
	public static List<IOS_PushSocketVO> pushGWSocketQueue = Collections.synchronizedList(new LinkedList<IOS_PushSocketVO>());		//활성화 되어있는 소켓 저장 큐
	public static int trancationIDNum = 0;	//Trancation 번호 (증가)
	
	public static int pushGWSocketInitCnt;	//초기 활성화 시킬 소켓 개수
	public static int pushGWSocketNowCnt = 0;	//현재 사용중인 소켓 개수
	public static int pushGWSocketMaxCnt;		//현재 사용중인 소켓이 최대값을 넘었을 경우 큐에 저장할지 체크 하기 위한 제한 값
	public static int pushGWSocketMinCnt;		//큐에 미저장 시 최소값일 경우 큐에 바로 저장하기 위한 제한 값
	public static String pushGWServerIP = "";	//Push G/W IP
	public static int pushGWServerPort;		//Push G/W Port
	public static int pushGWSocketCloseSecend;	//Push G/W Server에서 강제로 세션을 끊는 시간
	public static int pushGWSocketSendTimeout;	//Push G/W 요청 시 대기 시간
	
	public static String cpAppID ="";	//CP 등록값
	public static String cpService_id = "";	//CP 등록값 
	public static String cpService_passwd = "";	//CP 등록값
	public static String cpPush_app_id = ""; //CP 등록값
	public static String cpNoti_type = ""; //CP 등록값	
	public static String destinationIP = "";	//Push G/W IP 와 동일하게 값으로 사용하면 됨
	public static String sourceIP = ""; //서버 IP 정보
	public static String from_user_id = "";	//고객번호
	
	
	/**
	 * PushGW 기본 값을 Propery에 설정되어 있는 값으로 설정
	 */
	public void setIOS_PushCommon(){
		logger.debug("setIOS_PushCommon START ");

		IOS_PushCommon.pushGWServerIP = SmartUXProperties.getProperty("ios_pushgw.server.ip");
		IOS_PushCommon.pushGWServerPort = Integer.parseInt(SmartUXProperties.getProperty("ios_pushgw.server.port"));
		IOS_PushCommon.pushGWSocketInitCnt = Integer.parseInt(SmartUXProperties.getProperty("ios_pushgw.socket.init"));
		IOS_PushCommon.pushGWSocketMaxCnt = Integer.parseInt(SmartUXProperties.getProperty("ios_pushgw.socket.max"));
		IOS_PushCommon.pushGWSocketMinCnt = Integer.parseInt(SmartUXProperties.getProperty("ios_pushgw.socket.min"));
		IOS_PushCommon.pushGWSocketCloseSecend = Integer.parseInt(SmartUXProperties.getProperty("ios_pushgw.socket.close_secend"));
		IOS_PushCommon.pushGWSocketSendTimeout = Integer.parseInt(SmartUXProperties.getProperty("ios_pushgw.socket.timeout"));
		
		IOS_PushCommon.cpAppID = SmartUXProperties.getProperty("ios_pushgw.cp.app_id");
		IOS_PushCommon.cpService_id = SmartUXProperties.getProperty("ios_pushgw.cp.service_id");
		IOS_PushCommon.cpService_passwd = SmartUXProperties.getProperty("ios_pushgw.cp.service_passwd");
		IOS_PushCommon.cpPush_app_id = SmartUXProperties.getProperty("ios_pushgw.cp.push_app_id");
		IOS_PushCommon.cpNoti_type = SmartUXProperties.getProperty("ios_pushgw.cp.noti_type");
		IOS_PushCommon.destinationIP = SmartUXProperties.getProperty("ios_pushgw.cp.destination_ip");
		IOS_PushCommon.from_user_id = SmartUXProperties.getProperty("ios_pushgw.cp.from_user_id");
		//IOS_PushCommon.sourceIP = SmartUXProperties.getProperty("ios_pushgw.cp.source_ip");
		IOS_PushCommon.sourceIP = GlobalCom.getIPAddress();
		if(IOS_PushCommon.sourceIP.length() > 16){
			IOS_PushCommon.sourceIP = IOS_PushCommon.sourceIP.substring(0,16);
		}
				
		logger.debug("setIOS_PushCommon pushGWServerIP = "+IOS_PushCommon.pushGWServerIP);
		logger.debug("setIOS_PushCommon pushGWServerPort = "+IOS_PushCommon.pushGWServerPort);
		logger.debug("setIOS_PushCommon pushGWSocketInitCnt = "+IOS_PushCommon.pushGWSocketInitCnt);
		logger.debug("setIOS_PushCommon pushGWSocketNowCnt = "+IOS_PushCommon.pushGWSocketNowCnt);
		logger.debug("setIOS_PushCommon pushGWSocketMaxCnt = "+IOS_PushCommon.pushGWSocketMaxCnt);
		logger.debug("setIOS_PushCommon pushGWSocketMinCnt = "+IOS_PushCommon.pushGWSocketMinCnt);
		logger.debug("setIOS_PushCommon pushGWSocketCloseSecend = "+IOS_PushCommon.pushGWSocketCloseSecend);
		logger.debug("setIOS_PushCommon pushGWSocketSendTimeout = "+IOS_PushCommon.pushGWSocketSendTimeout);
		
		logger.debug("setIOS_PushCommon cpAppID = "+IOS_PushCommon.cpAppID);
		logger.debug("setIOS_PushCommon cpService_id = "+IOS_PushCommon.cpService_id);
		logger.debug("setIOS_PushCommon cpService_passwd = "+IOS_PushCommon.cpService_passwd);
		logger.debug("setIOS_PushCommon cpPush_app_id = "+IOS_PushCommon.cpPush_app_id);
		logger.debug("setIOS_PushCommon cpNoti_type = "+IOS_PushCommon.cpNoti_type);
		logger.debug("setIOS_PushCommon destinationIP = "+IOS_PushCommon.destinationIP);
		logger.debug("setIOS_PushCommon sourceIP = "+IOS_PushCommon.sourceIP);
		logger.debug("setIOS_PushCommon from_user_id = "+IOS_PushCommon.from_user_id);
		
		
		logger.debug("setIOS_PushCommon END ");
	}
}
