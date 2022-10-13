package com.dmi.smartux.push;

import java.net.InetAddress;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.push.vo.PushSocketVO;

/**
 * Push G/W 사용시 공통 변수 저장 클래스
 * @author YJ KIM
 *
 */
public class PushCommon {
	
	private final Log logger = LogFactory.getLog(this.getClass());
	
	//public static LinkedList<PushSocketVO> pushGWSocketQueue = new LinkedList<PushSocketVO>();		//활성화 되어있는 소켓 저장 큐
	public static List<PushSocketVO> pushGWSocketQueue = Collections.synchronizedList(new LinkedList<PushSocketVO>());		//활성화 되어있는 소켓 저장 큐
	public static int trancationIDNum = 0;	//Trancation 번호(증가)
	public static int channelNum = 0;		// 채널 번호
	public static String channelID; 		// 채널 아이디(서버 호스트이름)
	
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
	
	/**
	 * PushGW 기본 값을 Propery에 설정되어 있는 값으로 설정
	 */
	public void setPushCommon(){
		logger.debug("setPushCommon START ");
		PushCommon.pushGWServerIP = SmartUXProperties.getProperty("pushgw.server.ip");
		PushCommon.pushGWServerPort = Integer.parseInt(SmartUXProperties.getProperty("pushgw.server.port"));
		PushCommon.pushGWSocketInitCnt = Integer.parseInt(SmartUXProperties.getProperty("pushgw.socket.init"));
		PushCommon.pushGWSocketMaxCnt = Integer.parseInt(SmartUXProperties.getProperty("pushgw.socket.max"));
		PushCommon.pushGWSocketMinCnt = Integer.parseInt(SmartUXProperties.getProperty("pushgw.socket.min"));
		PushCommon.pushGWSocketCloseSecend = Integer.parseInt(SmartUXProperties.getProperty("pushgw.socket.close_secend"));
		PushCommon.pushGWSocketSendTimeout = Integer.parseInt(SmartUXProperties.getProperty("pushgw.socket.timeout"));
		PushCommon.channelNum = Integer.parseInt(SmartUXProperties.getProperty("pushgw.socket.channelNum"));
		try{
			//ChannelID는 HOST NAME 뒷자리에서 8자리를 자르고 8자리보다 작을경우 0을 붙여준다
			InetAddress addr = InetAddress.getLocalHost();
			String hostname = addr.getHostName();
			logger.debug("hostname 1 = "+hostname); 
			if(hostname.length() > 8){
				hostname = hostname.substring((hostname.length()-8),hostname.length());
			}else{
				int hostnameCnt = 8-hostname.length();
				for(int i=0;i<hostnameCnt;i++){
					hostname += "0";
				}
			}
			logger.debug("hostname 2 = "+hostname);
			
			//Host(8) + Port(2)
			String port = GlobalCom.getSystemProperty("JBOSS_PORT");
			try{
				port = port.substring(0,2);
			}catch (Exception e) {	//방어코드
				port = "00";
			}
			logger.debug("port 2 = "+port);
			PushCommon.channelID = hostname+port;
		}catch(Exception e){
			PushCommon.channelID = SmartUXProperties.getProperty("pushgw.socket.channelID");
		}
		PushCommon.cpAppID = SmartUXProperties.getProperty("pushgw.cp.app_id");
		PushCommon.cpService_id = SmartUXProperties.getProperty("pushgw.cp.service_id");
		PushCommon.cpService_passwd = SmartUXProperties.getProperty("pushgw.cp.service_passwd");
		PushCommon.cpPush_app_id = SmartUXProperties.getProperty("pushgw.cp.push_app_id");
		PushCommon.cpNoti_type = SmartUXProperties.getProperty("pushgw.cp.noti_type");
		PushCommon.destinationIP = SmartUXProperties.getProperty("pushgw.cp.destination_ip");
		
		logger.debug("pushGWServerIP = "+PushCommon.pushGWServerIP);
		logger.debug("pushGWServerPort = "+PushCommon.pushGWServerPort);
		logger.debug("pushGWSocketInitCnt = "+PushCommon.pushGWSocketInitCnt);
		logger.debug("pushGWSocketNowCnt = "+PushCommon.pushGWSocketNowCnt);
		logger.debug("pushGWSocketMaxCnt = "+PushCommon.pushGWSocketMaxCnt);
		logger.debug("pushGWSocketMinCnt = "+PushCommon.pushGWSocketMinCnt);
		logger.debug("pushGWSocketCloseSecend = "+PushCommon.pushGWSocketCloseSecend);
		logger.debug("pushGWSocketSendTimeout = "+PushCommon.pushGWSocketSendTimeout);
		
		logger.debug("cpAppID = "+PushCommon.cpAppID);
		logger.debug("cpService_id = "+PushCommon.cpService_id);
		logger.debug("cpService_passwd = "+PushCommon.cpService_passwd);
		logger.debug("cpPush_app_id = "+PushCommon.cpPush_app_id);
		logger.debug("cpNoti_type = "+PushCommon.cpNoti_type);
		logger.debug("destinationIP = "+PushCommon.destinationIP);
		
		logger.debug("setPushCommon END ");
	}

}
