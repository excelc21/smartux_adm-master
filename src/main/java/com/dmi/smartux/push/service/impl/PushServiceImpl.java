package com.dmi.smartux.push.service.impl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.dmi.smartux.admin.pushconfig.component.PushConfigComponent;
import com.dmi.smartux.admin.pushconfig.vo.PushConfigVo;
import com.dmi.smartux.admin.retry.dao.RetryDao;
import com.dmi.smartux.bonbang.dao.RegistrationIDDao;
import com.dmi.smartux.bonbang.vo.RegistrationIDParamVO;
import com.dmi.smartux.bonbang.vo.ReservedProgramParamVO;
import com.dmi.smartux.common.exception.SmartUXException;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.service.CacheService;
import com.dmi.smartux.common.util.ByteUtil;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.util.Json;
import com.dmi.smartux.common.util.SHA512Hash;
import com.dmi.smartux.ios_push.IOS_PushCall;
import com.dmi.smartux.push.PushCall;
import com.dmi.smartux.push.PushCommon;
import com.dmi.smartux.push.service.PushService;
import com.dmi.smartux.push.vo.PushResultVO;
import com.dmi.smartux.push.vo.PushSocketVO;

/**
 * Push G/W 연동 서비스 클래스
 * @author YJ KIM
 *
 */
@Service
public class PushServiceImpl implements PushService{
	
	@Autowired
	CacheService service;
	
	@Autowired
	RetryDao retryDao;

	@Autowired
	RegistrationIDDao rdao;
	
	@Autowired
	PushConfigComponent pushConfigComponent;
	
	private final Log logger = LogFactory.getLog(this.getClass());
	private final Log logger_ip_push = LogFactory.getLog("PushCall");
	//private final Log logger_develMode = LogFactory.getLog("develMode");
	
	public  PushResultVO setNoti(PushSocketVO psVO)  throws Exception{
		
			logger.debug("[setNoti] START");
		
			PushResultVO resultVO = new PushResultVO();
			
			try{
				psVO.getPushGWSocket().setSoTimeout(PushCommon.pushGWSocketSendTimeout);
				
				//PushGW Request Header 값 설정		
				//4자리수 넘지 않도록 방어코드
				if(PushCommon.trancationIDNum >= 9999){
					PushCommon.trancationIDNum = 0;
				}
				
				String tTransactionDate = GlobalCom.getTodayFormat();
				int tTransactionNum = PushCommon.trancationIDNum++;
				String tRealTransaction = "";
				try{
					tRealTransaction = tTransactionDate+Integer.toString(tTransactionNum);
				}catch(Exception e){
					logger.info("[setNoti][TransactionID Error]["+e.getClass().getName()+"]["+e.getMessage()+"]");
					throw e;
				}
				
				byte[] byte_MessageID = ByteUtil.int2byte(15);
				byte[] byte_TranscationDate = tTransactionDate.getBytes();
				byte[] byte_TranscationNum = ByteUtil.int2byte(tTransactionNum);
				byte[] byte_ChannelID = psVO.getChannelID().getBytes();
				byte[] byte_Reserved1 = new byte[2];
				byte[] byte_Reserved2 = new byte[12];
				
				byte[] byte_DestinationIP = new byte[16];
				ByteUtil.setbytes(byte_DestinationIP,0,PushCommon.destinationIP.getBytes());
				
				int destinationIPNullCnt = 16-PushCommon.destinationIP.length();
				
				logger.debug("[setNoti] destinationIPNullCnt = "+destinationIPNullCnt);
				
				for(int i=0;i<destinationIPNullCnt;i++){
					ByteUtil.setbytes(byte_DestinationIP,(PushCommon.destinationIP.length()+i),new byte[1]);
				}
				//byte_DestinationIP = PushCommon.destinationIP.getBytes(); //자릿수 비교 하여 공백 바이트값 추가
				
				//Push Request Body 값 설정
				Json json = new Json("request");
						
				Hashtable table1 = new Hashtable();
				table1.put("msg_id", "PUSH_NOTI");
				table1.put("push_id", tRealTransaction);	
				table1.put("service_id",PushCommon.cpService_id);
				table1.put("service_passwd", SHA512Hash.getDigest(PushCommon.cpService_passwd));	//SHA512 암호화 값
				table1.put("push_app_id",PushCommon.cpPush_app_id);
				table1.put("noti_type",PushCommon.cpNoti_type);
				table1.put("app_id",PushCommon.cpAppID);
				//table1.put("regist_id","LvSg+j3b5FsoEgzt3Nt4lFNEXvkHkBGLstD2so0MCgk=");
				table1.put("regist_id",psVO.getRegist_id());
				
				logger.debug("[setNoti] regist_id =========== "+psVO.getRegist_id());
				
				table1.put("noti_contents",psVO.getNoti_message());
						  
				json.setKeys(table1);		  
				json.addObject(table1);
				String jsonStr = json.toString();
				logger.debug(jsonStr);
				
				int dataLen = jsonStr.length();
				logger.debug("[setNoti] dataLen = "+dataLen);
				
				byte[] byte_DATALength = ByteUtil.int2byte(jsonStr.getBytes().length);
				byte[] byte_DATA = jsonStr.getBytes();
				
				byte[] byte_TranscationID = new byte[byte_TranscationDate.length+byte_TranscationNum.length];
				//System.arraycopy(byte_TranscationDate, 0, byte_TranscationID, 0, byte_TranscationDate.length);
				//System.arraycopy(byte_TranscationNum, 0, byte_TranscationID, byte_TranscationDate.length, byte_TranscationNum.length);
				ByteUtil.setbytes(byte_TranscationID, 0, byte_TranscationDate);
				ByteUtil.setbytes(byte_TranscationID, byte_TranscationDate.length, byte_TranscationNum);
				
				logger.debug("[setNoti] byte_MessageID="+byte_MessageID);
				logger.debug("[setNoti] byte_TranscationID="+byte_TranscationID);
				logger.debug("[setNoti] byte_TranscationDate="+byte_TranscationDate);
				logger.debug("[setNoti] byte_TranscationNum="+byte_TranscationNum);
				logger.debug("[setNoti] byte_ChannelID="+byte_ChannelID);
				logger.debug("[setNoti] byte_Reserved1="+byte_Reserved1);
				logger.debug("[setNoti] byte_DestinationIP="+byte_DestinationIP);
				logger.debug("[setNoti] byte_Reserved2="+byte_Reserved2);
				logger.debug("[setNoti] byte_DATALength="+byte_DATALength);
				logger.debug("[setNoti] byte_DATA="+byte_DATA);

				logger.debug("[setNoti] 서버 요청 byte_MessageID="+15);
				logger.debug("[setNoti] 서버 요청 byte_TranscationDate="+GlobalCom.getTodayFormat());
				logger.debug("[setNoti] 서버 요청 byte_TranscationNum="+PushCommon.trancationIDNum);
				logger.debug("[setNoti] 서버 요청 byte_ChannelID="+psVO.getChannelID());
				logger.debug("[setNoti] 서버 요청 byte_Reserved1="+"  ");
				logger.debug("[setNoti] 서버 요청 byte_DestinationIP="+PushCommon.destinationIP);
				logger.debug("[setNoti] 서버 요청 byte_Reserved2="+"       ");
				logger.debug("[setNoti] 서버 요청 byte_DATALength="+jsonStr.length());
				logger.debug("[setNoti] 서버 요청 byte_DATA="+jsonStr);
				
				int byteTotalLen = byte_MessageID.length
				+byte_TranscationID.length
				+byte_ChannelID.length
				+byte_Reserved1.length
				+byte_DestinationIP.length
				+byte_Reserved2.length
				+byte_DATALength.length
				+byte_DATA.length;
				
				logger.debug("byteTotalLen="+byteTotalLen);
				
				//redSun_sleep(1000);
				
				byte[] byteTotalData = new byte[byteTotalLen];
				ByteUtil.setbytes(byteTotalData, 0, 
						byte_MessageID);
				ByteUtil.setbytes(byteTotalData, byte_MessageID.length, 
						byte_TranscationID);
				ByteUtil.setbytes(byteTotalData, (byte_MessageID.length+byte_TranscationID.length), 
						byte_ChannelID);
				ByteUtil.setbytes(byteTotalData, (byte_MessageID.length+byte_TranscationID.length+byte_ChannelID.length), 
						byte_Reserved1);
				ByteUtil.setbytes(byteTotalData, (byte_MessageID.length+byte_TranscationID.length+byte_ChannelID.length+byte_Reserved1.length), 
						byte_DestinationIP);
				ByteUtil.setbytes(byteTotalData, (byte_MessageID.length+byte_TranscationID.length+byte_ChannelID.length+byte_Reserved1.length+byte_DestinationIP.length), 
						byte_Reserved2);
				ByteUtil.setbytes(byteTotalData, (byte_MessageID.length+byte_TranscationID.length+byte_ChannelID.length+byte_Reserved1.length+byte_DestinationIP.length+byte_Reserved2.length), 
						byte_DATALength);
				ByteUtil.setbytes(byteTotalData, (byte_MessageID.length+byte_TranscationID.length+byte_ChannelID.length+byte_Reserved1.length+byte_DestinationIP.length+byte_Reserved2.length+byte_DATALength.length), 
						byte_DATA);
				
				logger.debug("byteTotalData="+byteTotalData);
				
				//GlobalCom.testPushGWLog(psVO.getChannelID(), "[S]["+PushCommon.pushGWSocketQueue.size()+"]["+psVO.getSa_id()+"]["+psVO.getStb_mac()+"]["+psVO.getWifi_mac()+"] - ["+jsonStr+"]");
				logger_ip_push.info("["+psVO.getChannelID()+"][S]["+PushCommon.pushGWSocketQueue.size()+"]["+psVO.getSa_id()+"]["+psVO.getStb_mac()+"]["+psVO.getWifi_mac()+"] - ["+jsonStr+"]");
				
				psVO.getPushGWDataOut().write(byteTotalData);
				psVO.getPushGWDataOut().flush();
				
				logger.debug("======================= Header =========================");
				byte[] byte_response_MessageID = new byte[4];
				psVO.getPushGWDataIn().read(byte_response_MessageID, 0, byte_response_MessageID.length);
				int response_MessageID = ByteUtil.getint(byte_response_MessageID,0);
				logger.debug("[setNoti]  서버 응답 response_MessageID = "+response_MessageID);
				
				byte[] byte_response_TransactionDate = new byte[8];
				psVO.getPushGWDataIn().read(byte_response_TransactionDate, 0, byte_response_TransactionDate.length);
				String response_TransactionDate = new String(byte_response_TransactionDate);
				logger.debug("[setNoti]  서버 응답 response_TransactionDate = "+response_TransactionDate);
				
				byte[] byte_response_TransactionNum = new byte[4];
				psVO.getPushGWDataIn().read(byte_response_TransactionNum, 0, byte_response_TransactionNum.length);
				int response_TransactionNum = ByteUtil.getint(byte_response_TransactionNum,0);
				logger.debug("[setNoti]  서버 응답 response_TransactionNum = "+response_TransactionNum);
				
				byte[] byte_response_DestinationIP = new byte[16];
				psVO.getPushGWDataIn().read(byte_response_DestinationIP, 0, byte_response_DestinationIP.length);
				String response_DestinationIP = new String(byte_response_DestinationIP);
				logger.debug("[setNoti]  서버 응답 response_DestinationIP = "+response_DestinationIP);
				
				byte[] byte_response_ChannelID = new byte[14];
				psVO.getPushGWDataIn().read(byte_response_ChannelID, 0, byte_response_ChannelID.length);
				String response_ChannelID = new String(byte_response_ChannelID);
				logger.debug("[setNoti]  서버 응답 response_ChannelID = "+response_ChannelID);
				
				byte[] byte_response_Reserved1 = new byte[2];
				psVO.getPushGWDataIn().read(byte_response_Reserved1, 0, byte_response_Reserved1.length);
				String response_Reserved1 = new String(byte_response_Reserved1);
				logger.debug("[setNoti]  서버 응답 response_Reserved1 = "+response_Reserved1);
				
				byte[] byte_response_Reserved2 = new byte[12];
				psVO.getPushGWDataIn().read(byte_response_Reserved2, 0, byte_response_Reserved2.length);
				String response_Reserved2 = new String(byte_response_Reserved2);
				logger.debug("[setNoti]  서버 응답 response_Reserved2 = "+response_Reserved2);
				
				byte[] byte_response_DataLength = new byte[4];
				psVO.getPushGWDataIn().read(byte_response_DataLength, 0, byte_response_DataLength.length);
				int response_DataLength = ByteUtil.getint(byte_response_DataLength,0);
				logger.debug("[setNoti]  서버 응답 response_DataLength = "+response_DataLength);
				//logger.info("setNoti  서버 응답 response_DataLength = "+response_DataLength);
				
				if(response_DataLength > 1000){
					response_DataLength = 16;
				}
				
				//redSun_sleep(1000);
				
				byte[] byte_response_Data = new byte[response_DataLength];
				psVO.getPushGWDataIn().read(byte_response_Data, 0, byte_response_Data.length);
				String responseValue = new String(byte_response_Data);
				logger.debug("[setNoti]  서버 응답 response_Data = "+responseValue);
				
				String response_code ="FA";
				String response_data = "";
				if(responseValue.length() > 2){
					response_code = responseValue.substring(0,2);
					response_data = responseValue.substring(2,responseValue.length());
				}
				
				logger.debug("[setNoti]  서버 응답 response_code = "+response_code);
				logger.debug("[setNoti]  서버 응답 response_data = "+response_data);
				
				
				//GlobalCom.testPushGWLog(psVO.getChannelID(), "[R]["+PushCommon.pushGWSocketQueue.size()+"]["+psVO.getSa_id()+"]["+psVO.getStb_mac()+"]["+psVO.getWifi_mac()+"] - ["+response_code+"] ["+response_data+"]");
				logger_ip_push.info("["+psVO.getChannelID()+"][R]["+PushCommon.pushGWSocketQueue.size()+"]["+psVO.getSa_id()+"]["+psVO.getStb_mac()+"]["+psVO.getWifi_mac()+"] - ["+response_code+"] ["+(response_data.replace( System.getProperty( "line.separator" ), "" )).replace( "\r", "" )+"]");
				//GlobalCom.testPushGWLog(psVO.getChannelID(), "[응답 코드] : "+response_code);
				//GlobalCom.testPushGWLog(psVO.getChannelID(), "[응답 결과] : "+response_data);
				
				
				if(response_code.equalsIgnoreCase("SC")){
					
					JSONObject jsonObject = new JSONObject(response_data);
					JSONObject response = jsonObject.getJSONObject("response");
					//String msg_id = response.getString("msg_id");
					//String push_id = response.getString("push_id");
					String status_code = response.getString("status_code");
					
					logger.debug("======================= Body =========================");
					//logger.debug("[setNoti]  서버 응답 JSON msg_id = "+msg_id);
					//logger.debug("[setNoti]  서버 응답 JSON push_id = "+push_id);
					logger.debug("[setNoti]  서버 응답 JSON status_code = "+status_code);
										
					//PushCommon.pushGWSocketQueue.add(psVO);
					//closeSocketQueue(psVO);
					
					logger.debug("[setNoti] psVO.getQueueAddChk = "+psVO.getQueueAddChk());
					logger.debug("[setNoti] PushCommon.pushGWSocketNowCnt = "+PushCommon.pushGWSocketNowCnt);
					logger.debug("[setNoti] PushCommon.pushGWSocketMaxCnt = "+PushCommon.pushGWSocketMaxCnt);
					
					if(psVO.getQueueAddChk().equals("ADD") && PushCommon.pushGWSocketNowCnt <= PushCommon.pushGWSocketMaxCnt){
						logger.debug("[setNoti] 큐 저장 ");
						psVO.setPushGWSocketCallTime(GlobalCom.getTodayUnixtime());
						addPushQueue(psVO);
					}else{
						logger.debug("[setNoti] 큐 미저장 ");
						try{
							closeSocket(psVO);
						}catch(Exception e){}
					}
					
					//TEST
					//status_code = "200";
					
					if(status_code.equals("200")){
						logger_ip_push.info("["+psVO.getChannelID()+"][E]["+PushCommon.pushGWSocketQueue.size()+"]["+psVO.getSa_id()+"]["+psVO.getStb_mac()+"]["+psVO.getWifi_mac()+"] - [SUCCESS]");
						//GlobalCom.testPushGWLog(psVO.getChannelID(), "[E]["+PushCommon.pushGWSocketQueue.size()+"]["+psVO.getSa_id()+"]["+psVO.getStb_mac()+"]["+psVO.getWifi_mac()+"] - [SUCCESS]");
						//성공
						resultVO.setFlag(SmartUXProperties.getProperty("flag.success"));
						resultVO.setMessage(SmartUXProperties.getProperty("message.success"));
					}else{
						logger_ip_push.info("["+psVO.getChannelID()+"][E]["+PushCommon.pushGWSocketQueue.size()+"]["+psVO.getSa_id()+"]["+psVO.getStb_mac()+"]["+psVO.getWifi_mac()+"] - [FAIL]");
						//GlobalCom.testPushGWLog(psVO.getChannelID(), "[E]["+PushCommon.pushGWSocketQueue.size()+"]["+psVO.getSa_id()+"]["+psVO.getStb_mac()+"]["+psVO.getWifi_mac()+"] - [FAIL]");
						//실패
						SmartUXException exception = new SmartUXException();
						if(status_code.equals("400")){
							exception.setFlag(SmartUXProperties.getProperty("flag.pushgw.badRequest"));
							exception.setMessage(SmartUXProperties.getProperty("message.pushgw.badRequest"));
						}else if(status_code.equals("401")){
							exception.setFlag(SmartUXProperties.getProperty("flag.pushgw.unAuthorized"));
							exception.setMessage(SmartUXProperties.getProperty("message.pushgw.unAuthorized"));
						}else if(status_code.equals("403")){				
							exception.setFlag(SmartUXProperties.getProperty("flag.pushgw.forbidden"));
							exception.setMessage(SmartUXProperties.getProperty("message.pushgw.forbidden"));
						}else if(status_code.equals("404")){
							exception.setFlag(SmartUXProperties.getProperty("flag.pushgw.notFound"));
							exception.setMessage(SmartUXProperties.getProperty("message.pushgw.notFound"));
						}else if(status_code.equals("412")){
							exception.setFlag(SmartUXProperties.getProperty("flag.pushgw.preconditionFailed"));
							exception.setMessage(SmartUXProperties.getProperty("message.pushgw.preconditionFailed"));
						}else if(status_code.equals("500")){
							exception.setFlag(SmartUXProperties.getProperty("flag.pushgw.internalError"));
							exception.setMessage(SmartUXProperties.getProperty("message.pushgw.internalError"));
						}else if(status_code.equals("503")){
							exception.setFlag(SmartUXProperties.getProperty("flag.pushgw.serviceUnavailable"));
							exception.setMessage(SmartUXProperties.getProperty("message.pushgw.serviceUnavailable"));
						}else{
							//logger.info("message.etcmessage.etcmessage.etcmessage.etc>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ");
							exception.setFlag(SmartUXProperties.getProperty("flag.etc"));
							exception.setMessage(SmartUXProperties.getProperty("message.etc"));
						}
						throw exception;
					}
					
				}else{
					logger_ip_push.info("["+psVO.getChannelID()+"][E]["+PushCommon.pushGWSocketQueue.size()+"]["+psVO.getSa_id()+"]["+psVO.getStb_mac()+"]["+psVO.getWifi_mac()+"] - [FAIL]");

					//ByteUtil.getshort(byte_response_Data,2)

					//GlobalCom.testPushGWLog(psVO.getChannelID(), "[E]["+PushCommon.pushGWSocketQueue.size()+"]["+psVO.getSa_id()+"]["+psVO.getStb_mac()+"]["+psVO.getWifi_mac()+"] - [FAIL]");
					try{
						closeSocket(psVO);
					}catch(Exception e){}
					resultVO.setFlag(SmartUXProperties.getProperty("flag.pushgw.fail"));
					resultVO.setMessage(SmartUXProperties.getProperty("message.pushgw.fail"));
					
					SmartUXException exception = new SmartUXException();
					exception.setFlag(SmartUXProperties.getProperty("flag.pushgw.fail"));
					exception.setMessage(SmartUXProperties.getProperty("message.pushgw.fail"));
					
					throw exception;
				}
			}catch(ConnectException e){
				logger_ip_push.info("["+psVO.getChannelID()+"][E]["+PushCommon.pushGWSocketQueue.size()+"]["+psVO.getSa_id()+"]["+psVO.getStb_mac()+"]["+psVO.getWifi_mac()+"] - [FAIL]");
				//GlobalCom.testPushGWLog(psVO.getChannelID(), "[E]["+PushCommon.pushGWSocketQueue.size()+"]["+psVO.getSa_id()+"]["+psVO.getStb_mac()+"]["+psVO.getWifi_mac()+"] - [FAIL]");
				logger.debug("[setNoti][ConnectException]["+e.getClass().getName()+"]["+e.getMessage()+"]");
				closeSocketQueue(psVO);
				
				SmartUXException exception = new SmartUXException();
				exception.setFlag(SmartUXProperties.getProperty("flag.pushgw.socket"));
				exception.setMessage(SmartUXProperties.getProperty("message.pushgw.socket"));
				throw exception;
			}catch(SocketException e){
				logger_ip_push.info("["+psVO.getChannelID()+"][E]["+PushCommon.pushGWSocketQueue.size()+"]["+psVO.getSa_id()+"]["+psVO.getStb_mac()+"]["+psVO.getWifi_mac()+"] - [FAIL]");
				//GlobalCom.testPushGWLog(psVO.getChannelID(), "[E]["+PushCommon.pushGWSocketQueue.size()+"]["+psVO.getSa_id()+"]["+psVO.getStb_mac()+"]["+psVO.getWifi_mac()+"] - [FAIL]");
				logger.debug("[setNoti][SocketException]["+e.getClass().getName()+"]["+e.getMessage()+"]");
				closeSocketQueue(psVO);
				
				SmartUXException exception = new SmartUXException();
				exception.setFlag(SmartUXProperties.getProperty("flag.pushgw.socket"));
				exception.setMessage(SmartUXProperties.getProperty("message.pushgw.socket"));
				throw exception;
				
			}catch(SocketTimeoutException e){
				logger_ip_push.info("["+psVO.getChannelID()+"][E]["+PushCommon.pushGWSocketQueue.size()+"]["+psVO.getSa_id()+"]["+psVO.getStb_mac()+"]["+psVO.getWifi_mac()+"] - [FAIL]");
				//GlobalCom.testPushGWLog(psVO.getChannelID(), "[E]["+PushCommon.pushGWSocketQueue.size()+"]["+psVO.getSa_id()+"]["+psVO.getStb_mac()+"]["+psVO.getWifi_mac()+"] - [FAIL]");
				logger.debug("[setNoti][SocketTimeoutException]["+e.getClass().getName()+"]["+e.getMessage()+"]");
				closeSocketQueue(psVO);
				
				SmartUXException exception = new SmartUXException();
				exception.setFlag(SmartUXProperties.getProperty("flag.pushgw.socketime"));
				exception.setMessage(SmartUXProperties.getProperty("message.pushgw.socketime"));
				throw exception;
				
			}catch(SmartUXException e){
				//Push G/W Error 
				//logger.info("SmartUXException setNoti "+e.getMessage());
				throw e;
			}catch(Exception e){
				logger_ip_push.info("["+psVO.getChannelID()+"][E]["+PushCommon.pushGWSocketQueue.size()+"]["+psVO.getSa_id()+"]["+psVO.getStb_mac()+"]["+psVO.getWifi_mac()+"] - [FAIL]");
				//GlobalCom.testPushGWLog(psVO.getChannelID(), "[E]["+PushCommon.pushGWSocketQueue.size()+"]["+psVO.getSa_id()+"]["+psVO.getStb_mac()+"]["+psVO.getWifi_mac()+"] - [FAIL]");
				logger.debug("[setNoti][Exception]["+e.getClass().getName() + "]["+e.getMessage()+"]");
				closeSocketQueue(psVO);
				
				SmartUXException exception = new SmartUXException();
				exception.setFlag(SmartUXProperties.getProperty("flag.etc"));
				exception.setMessage(SmartUXProperties.getProperty("message.etc"));
				throw exception;
			}
			
			//redSun_sleep(1000);
			
			logger.debug("[setNoti] END");
			return resultVO;
		
	}

	public PushSocketVO getPushGWSocket() throws Exception{
		
		logger.debug("getPushGWSocket START");
		//logger.debug("getPushGWSocket 활성화 된 소켓 수= "+PushCommon.pushGWSocketQueue.size());
			synchronized(PushCommon.pushGWSocketQueue) {

				PushSocketVO psVO = null;
			
				try{
					//소켓 존재 하지 않을경우
					if(PushCommon.pushGWSocketQueue.size() <= 0){
						OpenSocket(true);
						psVO = PushCommon.pushGWSocketQueue.remove(0);
					}else{
						try{
							psVO = PushCommon.pushGWSocketQueue.remove(0);
						} catch(Exception e){
							logger_ip_push.info("[getPushGWSocket]Queue Clear");
							try{
								PushCommon.pushGWSocketQueue.clear();
							}catch(Exception ex){
								logger_ip_push.info("[getPushGWSocket]Queue Clear Error[" + ex.getMessage() + "]");
							}
							psVO = OpenSocket(false);
						}

						if((GlobalCom.getTodayUnixtime()-psVO.getPushGWSocketCallTime()) >= PushCommon.pushGWSocketCloseSecend){
							//소켓 유효시간이 지났을 경우
							logger.debug("[getPushGWSocket]소켓 유효 시간 지남");
							try{
								closeSocket(psVO);
							}catch(Exception e){}
							psVO = OpenSocket(false);
							if(PushCommon.pushGWSocketQueue.size() >= PushCommon.pushGWSocketMinCnt){
								psVO.setQueueAddChk("NO_ADD");
							}
						}else{
							//소켓이 유효할 경우
							logger.debug("[getPushGWSocket] 소켓 유효함");					
						}
					}
	
				}catch(ConnectException e){
					logger_ip_push.info("[getPushGWSocket][ConnectException]["+psVO.getChannelID()+"]["+PushCommon.pushGWSocketQueue.size()+"]["+psVO.getSa_id()+"]["+psVO.getStb_mac()+"]["+psVO.getWifi_mac()+"]");
					logger.debug("[getPushGWSocket][ConnectException]["+e.getClass().getName()+"]["+e.getMessage()+"]");
					try{
						closeSocket(psVO);
					}catch(Exception e2){}
					
					SmartUXException exception = new SmartUXException();
					exception.setFlag(SmartUXProperties.getProperty("flag.pushgw.socket"));
					exception.setMessage(SmartUXProperties.getProperty("message.pushgw.socket"));
					throw exception;
				}catch(SocketException e){
					logger_ip_push.info("[getPushGWSocket][SocketException]["+psVO.getChannelID()+"]["+PushCommon.pushGWSocketQueue.size()+"]["+psVO.getSa_id()+"]["+psVO.getStb_mac()+"]["+psVO.getWifi_mac()+"]");
					logger.debug("[getPushGWSocket][SocketException]["+e.getClass().getName()+"]["+e.getMessage()+"]");
					try{
						closeSocket(psVO);
					}catch(Exception e2){}
					
					SmartUXException exception = new SmartUXException();
					exception.setFlag(SmartUXProperties.getProperty("flag.pushgw.socket"));
					exception.setMessage(SmartUXProperties.getProperty("message.pushgw.socket"));
					throw exception;
					
				}catch(SocketTimeoutException e){
					logger_ip_push.info("[getPushGWSocket][SocketTimeoutException]["+psVO.getChannelID()+"]["+PushCommon.pushGWSocketQueue.size()+"]["+psVO.getSa_id()+"]["+psVO.getStb_mac()+"]["+psVO.getWifi_mac()+"]");
					logger.debug("[getPushGWSocket][SocketTimeoutException]["+e.getClass().getName()+"]["+e.getMessage()+"]");
					try{
						closeSocket(psVO);
					}catch(Exception e2){}
					
					SmartUXException exception = new SmartUXException();
					exception.setFlag(SmartUXProperties.getProperty("flag.pushgw.socketime"));
					exception.setMessage(SmartUXProperties.getProperty("message.pushgw.socketime"));
					throw exception;
					
				}catch(SmartUXException e){
					logger_ip_push.info("[getPushGWSocket][SmartUXException]["+e.getClass().getName()+"]");
					logger.debug("[getPushGWSocket][ConnectException]["+e.getClass().getName()+"]["+e.getMessage()+"]");
					throw e;
				}catch(Exception e){
					logger_ip_push.info("[getPushGWSocket][Exception]["+psVO.getChannelID()+"]["+PushCommon.pushGWSocketQueue.size()+"]["+psVO.getSa_id()+"]["+psVO.getStb_mac()+"]["+psVO.getWifi_mac()+"] ["+e.getMessage()+"]["+e.getClass().getName()+"]");
					logger.debug("[getPushGWSocket][Exception]["+e.getClass().getName()+"]["+e.getMessage()+"]");
					try{
						closeSocket(psVO);
					}catch(Exception e2){}
					
					SmartUXException exception = new SmartUXException();
					exception.setFlag(SmartUXProperties.getProperty("flag.etc"));
					exception.setMessage(SmartUXProperties.getProperty("message.etc"));
					throw exception;
				}
				
				logger.debug("[getPushGWSocket]END");
				//redSun_sleep(5000);
				//Thread.sleep(5000);
				return psVO;
			}
	}
	
	
	public  void setPushGWSocketAllOpen() throws Exception{
		
		logger.debug("[setPushGWSocketAllOpen]START");
		
		logger.debug("[setPushGWSocketAllOpen]PushCommon.pushGWSocketQueue.size() = "+PushCommon.pushGWSocketQueue.size());
		
		for(int i=0;i<PushCommon.pushGWSocketInitCnt && PushCommon.pushGWSocketQueue.size() < PushCommon.pushGWSocketInitCnt;i++){
			try{
				OpenSocket(true);
			}catch(SmartUXException e){
				logger.debug("[setPushGWSocketAllOpen]"+"["+e.getClass().getName()+"]["+e.getMessage()+"]["+e.getFlag()+"]");
				throw e;
			}
		}
		
//		if(PushCommon.pushGWSocketQueue.size() < PushCommon.pushGWSocketMaxCnt){
//			setPushGWSocketAllOpen();
//		}
		logger.debug("[setPushGWSocketAllOpen]활성화 된 소켓 수 = "+PushCommon.pushGWSocketQueue.size());
		logger.debug("[setPushGWSocketAllOpen]END");
	}

	public  void setPushGWSocketAllClose() throws Exception{
		
		logger.debug("[setPushGWSocketAllClose]START");
			for(int i=0;i<PushCommon.pushGWSocketQueue.size();i++){
				try{
					PushSocketVO pvo = PushCommon.pushGWSocketQueue.remove(0);
					closeSocketQueue(pvo);
				}catch(Exception e){}
			}
			
			if(PushCommon.pushGWSocketQueue.size() < PushCommon.pushGWSocketInitCnt){
				setPushGWSocketAllOpen();
			}
		logger.debug("[setPushGWSocketAllClose]활성화 된 소켓 수 = "+PushCommon.pushGWSocketQueue.size());
		logger.debug("[setPushGWSocketAllClose]END");
	}
	
	public  PushSocketVO OpenSocket(boolean addQueue) throws Exception{
		
			logger.debug("[OpenSocket]START");
			logger.debug("[OpenSocket]addQueue = "+addQueue);
			
			PushSocketVO psVO = null;
	
			try{
				
				PushCommon.pushGWSocketNowCnt++;
				
				//Socket socket = new Socket(PushCommon.pushGWServerIP,PushCommon.pushGWServerPort);
				Socket socket = new Socket();
				SocketAddress addr = new InetSocketAddress(PushCommon.pushGWServerIP,PushCommon.pushGWServerPort);
				socket.connect(addr, PushCommon.pushGWSocketSendTimeout);
				psVO = new PushSocketVO(socket);
				openInOutStream(psVO);
				
				String _channelNum = Integer.toString(PushCommon.channelNum++);
				if(_channelNum.length() < 4){
					int _channelNumChkLen = 4-_channelNum.length();
					for(int i=0;i<_channelNumChkLen;i++){
						_channelNum = "0"+_channelNum;
					}
				}else{
					if(_channelNum.equals("9999")){
						_channelNum = "0001";
						PushCommon.channelNum = 1;
					}
				}
				psVO.setChannelID(PushCommon.channelID+_channelNum);
				//psVO.setChannelID("smartuxap3"+_channelNum);
				
				//psVO = ChannelConnectionRequest(psVO);

				logger_ip_push.info("["+psVO.getChannelID()+"][OPEN_S]["+PushCommon.pushGWSocketQueue.size()+"]");
				//GlobalCom.testPushGWLog(psVO.getChannelID(), "[OPEN_S]["+PushCommon.pushGWSocketQueue.size()+"]");
				
				psVO.getPushGWSocket().setSoTimeout(PushCommon.pushGWSocketSendTimeout);
				
				byte[] byte_MessageID = ByteUtil.int2byte(1);
				byte[] byte_TranscationID = new byte[12];
				byte[] byte_ChannelID = psVO.getChannelID().getBytes();
				byte[] byte_Reserved1 = new byte[2];
				
				byte[] byte_DestinationIP = new byte[16];
				ByteUtil.setbytes(byte_DestinationIP,0,PushCommon.destinationIP.getBytes());
				
				int destinationIPNullCnt = 16-PushCommon.destinationIP.length();
				
				logger.debug("[OpenSocket]destinationIPNullCnt = "+destinationIPNullCnt);
				
				for(int i=0;i<destinationIPNullCnt;i++){
					ByteUtil.setbytes(byte_DestinationIP,(PushCommon.destinationIP.length()+i),new byte[1]);
				}
				
				byte[] byte_Reserved2 = new byte[12];
				byte[] byte_DATALength = ByteUtil.int2byte(0);
				
				int byteTotalLen = byte_MessageID.length
						+byte_TranscationID.length
						+byte_ChannelID.length
						+byte_Reserved1.length
						+byte_DestinationIP.length
						+byte_Reserved2.length
						+byte_DATALength.length;
						
				logger.debug("[OpenSocket]byteTotalLen="+byteTotalLen);
				
				byte[] byteTotalData = new byte[byteTotalLen];
				ByteUtil.setbytes(byteTotalData, 0, 
						byte_MessageID);
				ByteUtil.setbytes(byteTotalData, byte_MessageID.length, 
						byte_TranscationID);
				ByteUtil.setbytes(byteTotalData, (byte_MessageID.length+byte_TranscationID.length), 
						byte_ChannelID);
				ByteUtil.setbytes(byteTotalData, (byte_MessageID.length+byte_TranscationID.length+byte_ChannelID.length), 
						byte_Reserved1);
				ByteUtil.setbytes(byteTotalData, (byte_MessageID.length+byte_TranscationID.length+byte_ChannelID.length+byte_Reserved1.length), 
						byte_DestinationIP);
				ByteUtil.setbytes(byteTotalData, (byte_MessageID.length+byte_TranscationID.length+byte_ChannelID.length+byte_Reserved1.length+byte_DestinationIP.length), 
						byte_Reserved2);
				ByteUtil.setbytes(byteTotalData, (byte_MessageID.length+byte_TranscationID.length+byte_ChannelID.length+byte_Reserved1.length+byte_DestinationIP.length+byte_Reserved2.length), 
						byte_DATALength);
				
				logger.debug("[OpenSocket]byteTotalData="+byteTotalData);
				
				logger.debug("[OpenSocket]byte_MessageID="+byte_MessageID);
				logger.debug("[OpenSocket]byte_TranscationID="+byte_TranscationID);
				logger.debug("[OpenSocket]byte_ChannelID="+byte_ChannelID);
				logger.debug("[OpenSocket]byte_Reserved1="+byte_Reserved1);
				logger.debug("[OpenSocket]byte_DestinationIP="+byte_DestinationIP);
				logger.debug("[OpenSocket]byte_Reserved2="+byte_Reserved2);
				logger.debug("[OpenSocket]byte_DATALength="+byte_DATALength);
				
				logger.debug("[OpenSocket]서버 요청 byte_MessageID="+1);
				logger.debug("[OpenSocket]서버 요청 byte_TranscationDate="+"");
				logger.debug("[OpenSocket]서버 요청 byte_TranscationNum="+"");
				logger.debug("[OpenSocket]서버 요청 byte_ChannelID="+psVO.getChannelID());
				logger.debug("[OpenSocket]서버 요청 byte_Reserved1="+"  ");
				logger.debug("[OpenSocket]서버 요청 byte_DestinationIP="+PushCommon.destinationIP);
				logger.debug("[OpenSocket]서버 요청 byte_Reserved2="+"       ");
				
				psVO.getPushGWDataOut().write(byteTotalData);
				psVO.getPushGWDataOut().flush();
				
				
				logger.debug("======================= Header =========================");
				byte[] byte_response_MessageID = new byte[4];
				psVO.getPushGWDataIn().read(byte_response_MessageID, 0, byte_response_MessageID.length);
				int response_MessageID = ByteUtil.getint(byte_response_MessageID,0);
				logger.debug("[OpenSocket] 서버 응답 response_MessageID = "+response_MessageID);
				
				byte[] byte_response_TransactionDate = new byte[8];
				psVO.getPushGWDataIn().read(byte_response_TransactionDate, 0, byte_response_TransactionDate.length);
				String response_TransactionDate = new String(byte_response_TransactionDate);
				logger.debug("[OpenSocket] 서버 응답 response_TransactionDate = "+response_TransactionDate);
				
				byte[] byte_response_TransactionNum = new byte[4];
				psVO.getPushGWDataIn().read(byte_response_TransactionNum, 0, byte_response_TransactionNum.length);
				int response_TransactionNum = ByteUtil.getint(byte_response_TransactionNum,0);
				logger.debug("[OpenSocket] 서버 응답 response_TransactionNum = "+response_TransactionNum);
				
				byte[] byte_response_DestinationIP = new byte[16];
				psVO.getPushGWDataIn().read(byte_response_DestinationIP, 0, byte_response_DestinationIP.length);
				String response_DestinationIP = new String(byte_response_DestinationIP);
				logger.debug("[OpenSocket] 서버 응답 response_DestinationIP = "+response_DestinationIP);
				
				byte[] byte_response_ChannelID = new byte[14];
				psVO.getPushGWDataIn().read(byte_response_ChannelID, 0, byte_response_ChannelID.length);
				String response_ChannelID = new String(byte_response_ChannelID);
				logger.debug("[OpenSocket] 서버 응답 response_ChannelID = "+response_ChannelID);
				
				byte[] byte_response_Reserved1 = new byte[2];
				psVO.getPushGWDataIn().read(byte_response_Reserved1, 0, byte_response_Reserved1.length);
				String response_Reserved1 = new String(byte_response_Reserved1);
				logger.debug("[OpenSocket] 서버 응답 response_Reserved1 = "+response_Reserved1);
				
				byte[] byte_response_Reserved2 = new byte[12];
				psVO.getPushGWDataIn().read(byte_response_Reserved2, 0, byte_response_Reserved2.length);
				String response_Reserved2 = new String(byte_response_Reserved2);
				logger.debug("[OpenSocket] 서버 응답 response_Reserved2 = "+response_Reserved2);
				
				byte[] byte_response_DataLength = new byte[4];
				psVO.getPushGWDataIn().read(byte_response_DataLength, 0, byte_response_DataLength.length);
				int response_DataLength = ByteUtil.getint(byte_response_DataLength,0);
				logger.debug("[OpenSocket] 서버 응답 response_DataLength = "+response_DataLength);
				
				byte[] byte_response_Data = new byte[response_DataLength];
				psVO.getPushGWDataIn().read(byte_response_Data, 0, byte_response_Data.length);
				String response_Data = new String(byte_response_Data);
				logger.debug("[OpenSocket] 서버 응답 response_Data = "+response_Data);
				

				logger_ip_push.info("["+psVO.getChannelID()+"][OPEN_R]["+PushCommon.pushGWSocketQueue.size()+"] - ["+(response_Data.replace( System.getProperty( "line.separator" ), "" )).replace( "\r", "" )+"]");
				//GlobalCom.testPushGWLog(psVO.getChannelID(), "[OPEN_R]["+PushCommon.pushGWSocketQueue.size()+"] - ["+response_Data+"]");
				
				if(response_DataLength >= 2){
					if(response_Data.substring(0,2).equalsIgnoreCase("SC")){
						logger.debug("[OpenSocket]ChannelConnectionRequest 성공");
						logger_ip_push.info("["+psVO.getChannelID()+"][OPEN_E]["+PushCommon.pushGWSocketQueue.size()+"] - [SUCCESS]");
						//GlobalCom.testPushGWLog(psVO.getChannelID(), "[OPEN_E]["+PushCommon.pushGWSocketQueue.size()+"] - [SUCCESS]");
						if(addQueue){
							addPushQueue(psVO);
						}
					}else{
						//logger_ip_push.info("["+psVO.getChannelID()+"][OPEN_E]["+PushCommon.pushGWSocketQueue.size()+"] - [FAIL]");
						//GlobalCom.testPushGWLog(psVO.getChannelID(), "[OPEN_E]["+PushCommon.pushGWSocketQueue.size()+"] - [FAIL]");
						logger.debug("[OpenSocket]ChannelConnectionRequest 실패");
						
						SocketException exception = new SocketException();
						throw exception;
					}
				}
			}catch(ConnectException e){
				logger_ip_push.info("[ConnectException][OPEN_E]["+PushCommon.pushGWSocketQueue.size()+"] - [FAIL]");
				//GlobalCom.testPushGWLog(psVO.getChannelID(), "[OPEN_E]["+PushCommon.pushGWSocketQueue.size()+"] - [FAIL]");
				logger.debug("[OpenSocket]["+e.getClass().getName()+"]["+e.getMessage()+"]");
				if(addQueue){
					closeSocketQueue(psVO);
				}else{
					closeSocket(psVO);
				}
				
				SmartUXException exception = new SmartUXException();
				exception.setFlag(SmartUXProperties.getProperty("flag.pushgw.socket"));
				exception.setMessage(SmartUXProperties.getProperty("message.pushgw.socket"));
				throw exception;
			}catch(SocketException e){
				logger_ip_push.info("[SocketException][OPEN_E]["+PushCommon.pushGWSocketQueue.size()+"] - [FAIL]");
				//GlobalCom.testPushGWLog(psVO.getChannelID(), "[OPEN_E]["+PushCommon.pushGWSocketQueue.size()+"] - [FAIL]");
				logger.debug("[OpenSocket]["+e.getClass().getName()+"]["+e.getMessage()+"]");
				if(addQueue){
					closeSocketQueue(psVO);
				}else{
					closeSocket(psVO);
				}
				
				SmartUXException exception = new SmartUXException();
				exception.setFlag(SmartUXProperties.getProperty("flag.pushgw.socket"));
				exception.setMessage(SmartUXProperties.getProperty("message.pushgw.socket"));
				throw exception;
				
			}catch(SocketTimeoutException e){
				logger_ip_push.info("[SocketTimeoutException][OPEN_E]["+PushCommon.pushGWSocketQueue.size()+"] - [FAIL]");
				//GlobalCom.testPushGWLog(psVO.getChannelID(), "[OPEN_E]["+PushCommon.pushGWSocketQueue.size()+"] - [FAIL]");
				logger.debug("[OpenSocket]["+e.getClass().getName()+"]["+e.getMessage()+"]");
				if(addQueue){
					closeSocketQueue(psVO);
				}else{
					closeSocket(psVO);
				}
				
				SmartUXException exception = new SmartUXException();
				exception.setFlag(SmartUXProperties.getProperty("flag.pushgw.socketime"));
				exception.setMessage(SmartUXProperties.getProperty("message.pushgw.socketime"));
				throw exception;
				
			}catch(Exception e){
				logger_ip_push.info("[Exception][OPEN_E]["+PushCommon.pushGWSocketQueue.size()+"] - [FAIL]");
				//GlobalCom.testPushGWLog(psVO.getChannelID(), "[OPEN_E]["+PushCommon.pushGWSocketQueue.size()+"] - [FAIL]");
				logger.debug("[OpenSocket]["+e.getClass().getName()+"]["+e.getMessage()+"]");
				if(addQueue){
					closeSocketQueue(psVO);
				}else{
					closeSocket(psVO);
				}
				
				SmartUXException exception = new SmartUXException();
				exception.setFlag(SmartUXProperties.getProperty("flag.etc"));
				exception.setMessage(SmartUXProperties.getProperty("message.etc"));
				throw exception;
			}
			logger.debug("[OpenSocket]활성화 된 소켓 수= "+PushCommon.pushGWSocketQueue.size());
			
			logger.debug("[OpenSocket]END");
			return psVO;
	}
	
	
	public  void closeSocketQueue(PushSocketVO psVO) throws Exception{
					
			logger.debug("[closeSocketQueue]START");
			try{
				closeSocket(psVO);
					
				//PushCommon.pushGWSocketQueue.remove(psVO);
			}catch(Exception e){
				logger.debug("[closeSocketQueue]"+e.getClass().getName());
				logger.debug("[closeSocketQueue]"+e.getMessage());
			}
			logger.debug("[closeSocketQueue]END");
	}
	
	public  void closeSocket(PushSocketVO psVO) throws Exception{
					
			logger.debug("[closeSocket]START");
			
			try{
				PushCommon.pushGWSocketNowCnt--;
				
				if(!psVO.getPushGWSocket().isClosed()){
					psVO.getPushGWIn().close();
					psVO.getPushGWDataIn().close();
					
					psVO.getPushGWOut().close();
					psVO.getPushGWDataOut().close();
					
					psVO.getPushGWSocket().close();
				}
			
			}catch(Exception e){
				logger.debug("[closeSocket]"+e.getClass().getName());
				logger.debug("[closeSocket]"+e.getMessage());
			}
			
			logger.debug("[closeSocket]END");
	}
	
	public  void openInOutStream(PushSocketVO psVO) throws Exception{
			
			logger.debug("[openInOutStream]START");
		
			psVO.setPushGWIn(psVO.getPushGWSocket().getInputStream());
			psVO.setPushGWDataIn(new DataInputStream(psVO.getPushGWIn()));
			
			psVO.setPushGWOut(psVO.getPushGWSocket().getOutputStream());
			psVO.setPushGWDataOut(new DataOutputStream(psVO.getPushGWOut()));
			
			logger.debug("[openInOutStream]END");
	}	
	
	public  void closeInOutStream(PushSocketVO psVO) throws Exception{
		
		logger.debug("[closeInOutStream]START");
	
		psVO.getPushGWIn().close();
		psVO.getPushGWDataIn().close();
		
		psVO.getPushGWOut().close();
		psVO.getPushGWDataOut().close();
		
		logger.debug("[closeInOutStream]END");
	}
	
	public void addPushQueue(PushSocketVO psVO){
		synchronized(PushCommon.pushGWSocketQueue) {
			PushCommon.pushGWSocketQueue.add(psVO);
		}
	}	
	
	public void redSun_sleep(int time){
		try {

		      Thread.sleep(time);

		} catch (InterruptedException e) { }

	}
	
	@Async
	public void pushSendForaddReservedProgram(ReservedProgramParamVO rVO) 
	{
		//=====Push GW 전송값 세팅 & 호출
		try
		{	
			// 푸시설정정보조회
			PushConfigVo pushConfigVo = pushConfigComponent.getPushConfig("T");
			if(null == pushConfigVo){
				pushConfigVo = new PushConfigVo();
			}
			String blockPush = GlobalCom.isNull(pushConfigVo.getBlock_push(), "N");
			
			if("N".equalsIgnoreCase(blockPush)){
				
				List<RegistrationIDParamVO> reg_list = rdao.getRegIDList(rVO.getSa_id(),rVO.getStb_mac(),rVO.getSma_mac(),rVO.getApp_type());
				
				int retry = Integer.parseInt((String) service.getCache(retryDao, "setPushGateWayRetry", SmartUXProperties.getProperty("RetryDao.getPushGateWayRetry.cacheKey"), 0,"A0011"));
				
				if(reg_list.size() == 0){
					
				}else{
					RegistrationIDParamVO ridVO = null;
					for(int i=0;i<reg_list.size();i++){
						ridVO = reg_list.get(i);
						
						logger.debug("addReservedProgram 단말 PUSH reg_list.size = "+reg_list.size());
						logger.debug("addReservedProgram 단말 PUSH sma_mac = "+ridVO.getSma_mac());
						logger.debug("addReservedProgram 단말 PUSH reg_id = "+ridVO.getReg_id());
						
						//타사단말 Push 이용
						if(ridVO.getReg_id().equals("")){
							logger.debug("addReservedProgram 타사단말 PUSH 호출!!!!");
							
							String noti_message =SmartUXProperties.getProperty("ios_pushgw.cp.noti_contenst3");
							String user_id = ridVO.getSma_mac();
							String user_param = "addReservedProgram";
							
							IOS_PushCall iosPushCall = new IOS_PushCall(noti_message, user_id, user_param,retry, ridVO.getSa_id(), ridVO.getStb_mac(), ridVO.getSma_mac());
							iosPushCall.call();
							//Thread thread = new Thread(new IOS_PushThread(noti_message, user_id, user_param,retry, ridVO.getSa_id(), ridVO.getStb_mac(), ridVO.getSma_mac()));
							//thread.start();
						}else{
							//자사단말 Push 이용
							logger.debug("addReservedProgram 자사단말 PUSH 호출!!!!");
							String noti_message ="addReservedProgram";
							String reg_id = ridVO.getReg_id();
							
							PushCall pushCall = new PushCall(noti_message, reg_id, retry, ridVO.getSa_id(), ridVO.getStb_mac(), ridVO.getSma_mac());
							pushCall.call();
							//Thread thread = new Thread(new PushThread(noti_message, reg_id, retry, ridVO.getSa_id(), ridVO.getStb_mac(), ridVO.getSma_mac()));
							//thread.start();
						}
					}
				}
			}else{
				logger.debug("addReservedProgram 단말 PUSH is BLOCK");
			}
		}
		catch(Exception e)
		{
			
		}
	}
	
}
