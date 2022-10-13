package com.dmi.smartux.ios_push.service.impl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import java.io.StringReader;
 

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.springframework.stereotype.Service;

import com.dmi.smartux.common.exception.SmartUXException;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.ByteUtil;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.ios_push.IOS_PushCommon;
import com.dmi.smartux.ios_push.service.IOS_PushService;
import com.dmi.smartux.ios_push.vo.IOS_PushRequestVO;
import com.dmi.smartux.ios_push.vo.IOS_PushSocketVO;
import com.dmi.smartux.push.vo.PushResultVO;

/**
 * Push G/W 연동 서비스 클래스
 * @author YJ KIM
 *
 */
@Service
public class IOS_PushServiceImpl implements IOS_PushService{
	
	private final Log logger = LogFactory.getLog(this.getClass());
	private final Log logger_ios_push = LogFactory.getLog("IOS_PushCall");
	
	/**
	 * Push Noti 메시지 전달
	 * @param psVO
	 * @return
	 * @throws Exception
	 */
	public PushResultVO setNoti(IOS_PushSocketVO psVO)  throws Exception{
		
			logger.debug("[IOS Push ##-setNoti] START");
		
			PushResultVO resultVO = new PushResultVO();
			
			try{
				psVO.getPushGWSocket().setSoTimeout(IOS_PushCommon.pushGWSocketSendTimeout);
				
				//PushGW Request Header 값 설정		
				//4자리수 넘지 않도록 방어코드
				if(IOS_PushCommon.trancationIDNum >= 9999){
					IOS_PushCommon.trancationIDNum = 0;
				}
				
				byte[] byte_MessageID = ByteUtil.int2byte(15);
				byte[] byte_TranscationDate = GlobalCom.getTodayFormat().getBytes();
				byte[] byte_TranscationNum = ByteUtil.int2byte(IOS_PushCommon.trancationIDNum++);
				byte[] byte_SourceIP = new byte[16];
				ByteUtil.setbytes(byte_SourceIP,0,IOS_PushCommon.sourceIP.getBytes());
				int sourceIPNullCnt = 16-IOS_PushCommon.sourceIP.length();
				logger.debug("[IOS Push ##-setNoti] sourceIPNullCnt = "+sourceIPNullCnt);	
				for(int i=0;i<sourceIPNullCnt;i++){
					ByteUtil.setbytes(byte_SourceIP,(IOS_PushCommon.sourceIP.length()+i),new byte[1]);
				}
				
				byte[] byte_DestinationIP = new byte[16];
				ByteUtil.setbytes(byte_DestinationIP,0,IOS_PushCommon.destinationIP.getBytes());
				int destinationIPNullCnt = 16-IOS_PushCommon.destinationIP.length();				
				logger.debug("[IOS Push ##-setNoti] destinationIPNullCnt = "+destinationIPNullCnt);				
				for(int i=0;i<destinationIPNullCnt;i++){
					ByteUtil.setbytes(byte_DestinationIP,(IOS_PushCommon.destinationIP.length()+i),new byte[1]);
				}
				
				byte[] byte_Reserved = new byte[12];
				
				//Push Request Body 값 설정
				IOS_PushRequestVO prVO = new IOS_PushRequestVO();
				prVO.setMsg_id("PUSH_NOTI");
				prVO.setTransaction_id(GlobalCom.getTodayFormat()+IOS_PushCommon.trancationIDNum);
				prVO.setService_id(IOS_PushCommon.cpService_id);
				prVO.setService_passwd(IOS_PushCommon.cpService_passwd);
				prVO.setPush_app_id(IOS_PushCommon.cpPush_app_id);
				prVO.setNoti_type(IOS_PushCommon.cpNoti_type);
				prVO.setUser_id(psVO.getUser_id());
				prVO.setFrom_user_id(IOS_PushCommon.from_user_id);
				prVO.setNoti_contents(psVO.getNoti_message());
				prVO.setUser_param(psVO.getUser_param());
				
				String xmlStr = setXmlData(prVO);
				logger.debug("[IOS Push ##-setNoti] xmlStr = "+xmlStr);
				
				logger_ios_push.info("["+getTrancationStr()+IOS_PushCommon.trancationIDNum+"][S]["+IOS_PushCommon.pushGWSocketQueue.size()+"]["+psVO.getSa_id()+"]["+psVO.getStb_mac()+"]["+psVO.getWifi_mac()+"] - ["+xmlStr+"]");
				//GlobalCom.testIOS_PushGWLog(GlobalCom.getTodayFormat()+IOS_PushCommon.trancationIDNum, "[요청] : "+xmlStr);
				//GlobalCom.testIOS_PushGWLog(getTrancationStr()+IOS_PushCommon.trancationIDNum, "[S]["+IOS_PushCommon.pushGWSocketQueue.size()+"]["+psVO.getSa_id()+"]["+psVO.getStb_mac()+"]["+psVO.getWifi_mac()+"] - ["+xmlStr+"]");
				
				int dataLen = xmlStr.length();
				logger.debug("[IOS Push ##-setNoti] dataLen = "+dataLen);
				
				byte[] byte_DATALength = ByteUtil.int2byte(xmlStr.getBytes().length);
				byte[] byte_DATA = xmlStr.getBytes();
				
				byte[] byte_TranscationID = new byte[byte_TranscationDate.length+byte_TranscationNum.length];
				//System.arraycopy(byte_TranscationDate, 0, byte_TranscationID, 0, byte_TranscationDate.length);
				//System.arraycopy(byte_TranscationNum, 0, byte_TranscationID, byte_TranscationDate.length, byte_TranscationNum.length);
				ByteUtil.setbytes(byte_TranscationID, 0, byte_TranscationDate);
				ByteUtil.setbytes(byte_TranscationID, byte_TranscationDate.length, byte_TranscationNum);
				
				logger.debug("[IOS Push ##-setNoti] 서버 요청 byte_MessageID="+15);
				logger.debug("[IOS Push ##-setNoti] 서버 요청 byte_TranscationDate="+GlobalCom.getTodayFormat());
				logger.debug("[IOS Push ##-setNoti] 서버 요청 byte_TranscationNum="+IOS_PushCommon.trancationIDNum);
				logger.debug("[IOS Push ##-setNoti] 서버 요청 byte_SourceIP="+IOS_PushCommon.sourceIP);
				logger.debug("[IOS Push ##-setNoti] 서버 요청 byte_DestinationIP="+IOS_PushCommon.destinationIP);
				logger.debug("[IOS Push ##-setNoti] 서버 요청 byte_Reserved="+"       ");
				logger.debug("[IOS Push ##-setNoti] 서버 요청 byte_DATALength="+xmlStr.length());
				logger.debug("[IOS Push ##-setNoti] 서버 요청 byte_DATA="+xmlStr);
				
				int byteTotalLen = byte_MessageID.length
				+byte_TranscationID.length
				+byte_SourceIP.length
				+byte_DestinationIP.length
				+byte_Reserved.length
				+byte_DATALength.length
				+byte_DATA.length;
				
				logger.debug("IOS Push ##-byteTotalLen="+byteTotalLen);
				
				byte[] byteTotalData = new byte[byteTotalLen];
				ByteUtil.setbytes(byteTotalData, 0, 
						byte_MessageID);
				ByteUtil.setbytes(byteTotalData, byte_MessageID.length, 
						byte_TranscationID);
				ByteUtil.setbytes(byteTotalData, (byte_MessageID.length+byte_TranscationID.length), 
						byte_SourceIP);
				ByteUtil.setbytes(byteTotalData, (byte_MessageID.length+byte_TranscationID.length+byte_SourceIP.length), 
						byte_DestinationIP);
				ByteUtil.setbytes(byteTotalData, (byte_MessageID.length+byte_TranscationID.length+byte_SourceIP.length+byte_DestinationIP.length), 
						byte_Reserved);
				ByteUtil.setbytes(byteTotalData, (byte_MessageID.length+byte_TranscationID.length+byte_SourceIP.length+byte_DestinationIP.length+byte_Reserved.length), 
						byte_DATALength);
				ByteUtil.setbytes(byteTotalData, (byte_MessageID.length+byte_TranscationID.length+byte_SourceIP.length+byte_DestinationIP.length+byte_Reserved.length+byte_DATALength.length), 
						byte_DATA);
				
				logger.debug("IOS Push ##-byteTotalData="+byteTotalData);
				
				psVO.getPushGWDataOut().write(byteTotalData);
				psVO.getPushGWDataOut().flush();
				
				logger.debug("IOS Push ##-======================= Header =========================");
				byte[] byte_response_MessageID = new byte[4];
				psVO.getPushGWDataIn().read(byte_response_MessageID, 0, byte_response_MessageID.length);
				int response_MessageID = ByteUtil.getint(byte_response_MessageID,0);
				logger.debug("[IOS Push ##-setNoti]  서버 응답 response_MessageID = "+response_MessageID);
				
				byte[] byte_response_TransactionDate = new byte[8];
				psVO.getPushGWDataIn().read(byte_response_TransactionDate, 0, byte_response_TransactionDate.length);
				String response_TransactionDate = new String(byte_response_TransactionDate);
				logger.debug("[IOS Push ##-setNoti]  서버 응답 response_TransactionDate = "+response_TransactionDate);
				
				byte[] byte_response_TransactionNum = new byte[4];
				psVO.getPushGWDataIn().read(byte_response_TransactionNum, 0, byte_response_TransactionNum.length);
				int response_TransactionNum = ByteUtil.getint(byte_response_TransactionNum,0);
				logger.debug("[IOS Push ##-setNoti]  서버 응답 response_TransactionNum = "+response_TransactionNum);
				
				byte[] byte_response_SourceIP = new byte[16];
				psVO.getPushGWDataIn().read(byte_response_SourceIP, 0, byte_response_SourceIP.length);
				String response_SourceIP = new String(byte_response_SourceIP);
				logger.debug("[IOS Push ##-setNoti]  서버 응답 response_SourceIP = "+response_SourceIP);
				
				byte[] byte_response_DestinationIP = new byte[16];
				psVO.getPushGWDataIn().read(byte_response_DestinationIP, 0, byte_response_DestinationIP.length);
				String response_DestinationIP = new String(byte_response_DestinationIP);
				logger.debug("[IOS Push ##-setNoti]  서버 응답 response_DestinationIP = "+response_DestinationIP);
				
				byte[] byte_response_Reserved = new byte[12];
				psVO.getPushGWDataIn().read(byte_response_Reserved, 0, byte_response_Reserved.length);
				String response_Reserved = new String(byte_response_Reserved);
				logger.debug("[IOS Push ##-setNoti]  서버 응답 response_Reserved = "+response_Reserved);
				
				byte[] byte_response_DataLength = new byte[4];
				psVO.getPushGWDataIn().read(byte_response_DataLength, 0, byte_response_DataLength.length);
				int response_DataLength = ByteUtil.getint(byte_response_DataLength,0);
				logger.debug("[IOS Push ##-setNoti]  서버 응답 response_DataLength = "+response_DataLength);
				
				byte[] byte_response_Data = new byte[response_DataLength];
				psVO.getPushGWDataIn().read(byte_response_Data, 0, byte_response_Data.length);
				String responseValue = new String(byte_response_Data);
				logger.debug("[IOS Push ##-setNoti]  서버 응답 response_Data = "+responseValue);
				
				String response_code ="FA";
				
				try{
					response_code = responseValue.substring(0,2);
					responseValue = responseValue.substring(2,responseValue.length());
					
					logger.debug("[IOS Push ##-setNoti]  서버 응답 response_code = "+response_code);
					logger.debug("[IOS Push ##-setNoti]  서버 응답 responseValue = "+responseValue);
					
					//GlobalCom.testIOS_PushGWLog(GlobalCom.getTodayFormat()+IOS_PushCommon.trancationIDNum, "[응답 코드] : "+response_code);
					//GlobalCom.testIOS_PushGWLog(GlobalCom.getTodayFormat()+IOS_PushCommon.trancationIDNum, "[응답 결과] : "+responseValue);
					logger_ios_push.info("["+getTrancationStr()+IOS_PushCommon.trancationIDNum+"][R]["+IOS_PushCommon.pushGWSocketQueue.size()+"]["+psVO.getSa_id()+"]["+psVO.getStb_mac()+"]["+psVO.getWifi_mac()+"] - ["+response_code+"] ["+(responseValue.replace( System.getProperty( "line.separator" ), "" )).replace( "\r", "" )+"]");
					//GlobalCom.testIOS_PushGWLog(getTrancationStr()+IOS_PushCommon.trancationIDNum, "[R]["+IOS_PushCommon.pushGWSocketQueue.size()+"]["+psVO.getSa_id()+"]["+psVO.getStb_mac()+"]["+psVO.getWifi_mac()+"] - ["+response_code+"] ["+responseValue+"]");
					
					SAXBuilder builder = new SAXBuilder();
					Document doc = builder.build(new StringReader(responseValue));
					Element root = doc.getRootElement();
					
					List<Element> elements = root.getChildren();
					for(Element element : elements){
						if(element.getName().equals("result_code")){
							response_code = element.getText();
							logger.debug("[IOS Push ##-setNoti]  서버 응답 XML result_code = "+response_code);							
							break;
						}
					}
				}catch(Exception e){
		        	logger.debug("[IOS Push ##-setNoti] XML Parser Exception = "+e);
		        	response_code = "FA";
		        }
				
				logger.debug("[IOS Push ##-setNoti]  서버 응답 response_code = "+response_code);
				//성공
				if(!response_code.equalsIgnoreCase("FA")){
					logger.debug("[IOS Push ##-setNoti] psVO.getQueueAddChk = "+psVO.getQueueAddChk());
					logger.debug("[IOS Push ##-setNoti] IOS_PushCommon.pushGWSocketNowCnt = "+IOS_PushCommon.pushGWSocketNowCnt);
					logger.debug("[IOS Push ##-setNoti] IOS_PushCommon.pushGWSocketMaxCnt = "+IOS_PushCommon.pushGWSocketMaxCnt);

					if(psVO.getQueueAddChk().equals("ADD") && IOS_PushCommon.pushGWSocketNowCnt <= IOS_PushCommon.pushGWSocketMaxCnt){
						logger.debug("[IOS Push ##-setNoti] 큐 저장 ");
						psVO.setPushGWSocketCallTime(GlobalCom.getTodayUnixtime());
						IOS_addPushQueue(psVO);
					}else{
						logger.debug("[IOS Push ##-setNoti] 큐 미저장 ");
						try{
							closeSocket(psVO);
						}catch(Exception e){}
					}
					
					//TEST
					//status_code = "200";
					
					if(response_code.equals("200")){
						logger_ios_push.info("["+getTrancationStr()+IOS_PushCommon.trancationIDNum+"][E]["+IOS_PushCommon.pushGWSocketQueue.size()+"]["+psVO.getSa_id()+"]["+psVO.getStb_mac()+"]["+psVO.getWifi_mac()+"] - [SUCCESS]");
						//GlobalCom.testIOS_PushGWLog(getTrancationStr()+IOS_PushCommon.trancationIDNum, "[E]["+IOS_PushCommon.pushGWSocketQueue.size()+"]["+psVO.getSa_id()+"]["+psVO.getStb_mac()+"]["+psVO.getWifi_mac()+"] - [SUCCESS]");
						//성공
						resultVO.setFlag(SmartUXProperties.getProperty("flag.success"));
						resultVO.setMessage(SmartUXProperties.getProperty("message.success"));
					}else{
						logger_ios_push.info("["+getTrancationStr()+IOS_PushCommon.trancationIDNum+"][E]["+IOS_PushCommon.pushGWSocketQueue.size()+"]["+psVO.getSa_id()+"]["+psVO.getStb_mac()+"]["+psVO.getWifi_mac()+"] - [FAIL]");
						//GlobalCom.testIOS_PushGWLog(getTrancationStr()+IOS_PushCommon.trancationIDNum, "[E]["+IOS_PushCommon.pushGWSocketQueue.size()+"]["+psVO.getSa_id()+"]["+psVO.getStb_mac()+"]["+psVO.getWifi_mac()+"] - [FAIL]");
						//실패
						SmartUXException exception = new SmartUXException();
						if(response_code.equals("400")){
							exception.setFlag(SmartUXProperties.getProperty("flag.pushgw.badRequest"));
							exception.setMessage(SmartUXProperties.getProperty("message.pushgw.badRequest"));
						}else if(response_code.equals("401")){
							exception.setFlag(SmartUXProperties.getProperty("flag.pushgw.unAuthorized"));
							exception.setMessage(SmartUXProperties.getProperty("message.pushgw.unAuthorized"));
						}else if(response_code.equals("403")){				
							exception.setFlag(SmartUXProperties.getProperty("flag.pushgw.forbidden"));
							exception.setMessage(SmartUXProperties.getProperty("message.pushgw.forbidden"));
						}else if(response_code.equals("404")){
							exception.setFlag(SmartUXProperties.getProperty("flag.pushgw.notFound"));
							exception.setMessage(SmartUXProperties.getProperty("message.pushgw.notFound"));
						}else if(response_code.equals("412")){
							exception.setFlag(SmartUXProperties.getProperty("flag.pushgw.preconditionFailed"));
							exception.setMessage(SmartUXProperties.getProperty("message.pushgw.preconditionFailed"));
						}else if(response_code.equals("500")){
							exception.setFlag(SmartUXProperties.getProperty("flag.pushgw.internalError"));
							exception.setMessage(SmartUXProperties.getProperty("message.pushgw.internalError"));
						}else if(response_code.equals("503")){
							exception.setFlag(SmartUXProperties.getProperty("flag.pushgw.serviceUnavailable"));
							exception.setMessage(SmartUXProperties.getProperty("message.pushgw.serviceUnavailable"));
						}else{
							exception.setFlag(SmartUXProperties.getProperty("flag.etc"));
							exception.setMessage(SmartUXProperties.getProperty("message.etc"));
						}
						throw exception;
					}
					
				//실패
				}else{
					logger_ios_push.info("["+getTrancationStr()+IOS_PushCommon.trancationIDNum+"][E]["+IOS_PushCommon.pushGWSocketQueue.size()+"]["+psVO.getSa_id()+"]["+psVO.getStb_mac()+"]["+psVO.getWifi_mac()+"] - [FAIL]");
					//GlobalCom.testIOS_PushGWLog(getTrancationStr()+IOS_PushCommon.trancationIDNum, "[E]["+IOS_PushCommon.pushGWSocketQueue.size()+"]["+psVO.getSa_id()+"]["+psVO.getStb_mac()+"]["+psVO.getWifi_mac()+"] - [FAIL]");
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
				logger_ios_push.info("["+getTrancationStr()+IOS_PushCommon.trancationIDNum+"][E]["+IOS_PushCommon.pushGWSocketQueue.size()+"]["+psVO.getSa_id()+"]["+psVO.getStb_mac()+"]["+psVO.getWifi_mac()+"] - [FAIL]");
				//GlobalCom.testIOS_PushGWLog(getTrancationStr()+IOS_PushCommon.trancationIDNum, "[E]["+IOS_PushCommon.pushGWSocketQueue.size()+"]["+psVO.getSa_id()+"]["+psVO.getStb_mac()+"]["+psVO.getWifi_mac()+"] - [FAIL]");
				logger.debug("[IOS Push ##-setNoti ##-setNoti "+"]["+e.getClass().getName()+e.getMessage());
				closeSocketQueue(psVO);
				
				SmartUXException exception = new SmartUXException();
				exception.setFlag(SmartUXProperties.getProperty("flag.pushgw.socket"));
				exception.setMessage(SmartUXProperties.getProperty("message.pushgw.socket"));
				throw exception;
			}catch(SocketException e){
				logger_ios_push.info("["+getTrancationStr()+IOS_PushCommon.trancationIDNum+"][E]["+IOS_PushCommon.pushGWSocketQueue.size()+"]["+psVO.getSa_id()+"]["+psVO.getStb_mac()+"]["+psVO.getWifi_mac()+"] - [FAIL]");
				//GlobalCom.testIOS_PushGWLog(getTrancationStr()+IOS_PushCommon.trancationIDNum, "[E]["+IOS_PushCommon.pushGWSocketQueue.size()+"]["+psVO.getSa_id()+"]["+psVO.getStb_mac()+"]["+psVO.getWifi_mac()+"] - [FAIL]");
				logger.debug("[IOS Push ##-setNoti ##-setNoti "+"]["+e.getClass().getName()+e.getMessage());
				closeSocketQueue(psVO);
				
				SmartUXException exception = new SmartUXException();
				exception.setFlag(SmartUXProperties.getProperty("flag.pushgw.socket"));
				exception.setMessage(SmartUXProperties.getProperty("message.pushgw.socket"));
				throw exception;
				
			}catch(SocketTimeoutException e){
				logger_ios_push.info("["+getTrancationStr()+IOS_PushCommon.trancationIDNum+"][E]["+IOS_PushCommon.pushGWSocketQueue.size()+"]["+psVO.getSa_id()+"]["+psVO.getStb_mac()+"]["+psVO.getWifi_mac()+"] - [FAIL]");
				//GlobalCom.testIOS_PushGWLog(getTrancationStr()+IOS_PushCommon.trancationIDNum, "[E]["+IOS_PushCommon.pushGWSocketQueue.size()+"]["+psVO.getSa_id()+"]["+psVO.getStb_mac()+"]["+psVO.getWifi_mac()+"] - [FAIL]");
				logger.debug("[IOS Push ##-setNoti ##-setNoti "+"]["+e.getClass().getName()+e.getMessage());
				closeSocketQueue(psVO);
				
				SmartUXException exception = new SmartUXException();
				exception.setFlag(SmartUXProperties.getProperty("flag.pushgw.socketime"));
				exception.setMessage(SmartUXProperties.getProperty("message.pushgw.socketime"));
				throw exception;
				
			}catch(SmartUXException e){
				//Push G/W Error 
				throw e;
			}catch(Exception e){
				logger_ios_push.info("["+getTrancationStr()+IOS_PushCommon.trancationIDNum+"][E]["+IOS_PushCommon.pushGWSocketQueue.size()+"]["+psVO.getSa_id()+"]["+psVO.getStb_mac()+"]["+psVO.getWifi_mac()+"] - [FAIL]");
				//GlobalCom.testIOS_PushGWLog(getTrancationStr()+IOS_PushCommon.trancationIDNum, "[E]["+IOS_PushCommon.pushGWSocketQueue.size()+"]["+psVO.getSa_id()+"]["+psVO.getStb_mac()+"]["+psVO.getWifi_mac()+"] - [FAIL]");
				logger.debug("[IOS Push ##-setNoti ##-setNoti "+"]["+e.getClass().getName()+e.getMessage());
				closeSocketQueue(psVO);
				
				SmartUXException exception = new SmartUXException();
				exception.setFlag(SmartUXProperties.getProperty("flag.etc"));
				exception.setMessage(SmartUXProperties.getProperty("message.etc"));
				throw exception;
			}
			
			logger.debug("[IOS Push ##-setNoti] END");
			return resultVO;
		
	}
	
	/**
	 * PushGW 소켓 중 사용가능한 소켓 조회 메서드
	 * @return
	 */
	public IOS_PushSocketVO getPushGWSocket() throws Exception{
		
		logger.debug("[IOS Push ##-getPushGWSocket]START");
		//logger.debug("[IOS Push ##-getPushGWSocket]활성화 된 소켓 수= "+IOS_PushCommon.pushGWSocketQueue.size());
		
		synchronized(IOS_PushCommon.pushGWSocketQueue) {
			IOS_PushSocketVO psVO = null;
			
			try{
				//소켓 존재 하지 않을경우
				if(IOS_PushCommon.pushGWSocketQueue.size() <= 0){
					OpenSocket();
					psVO = IOS_PushCommon.pushGWSocketQueue.remove(0);
				}else{
					
					try{
						psVO = IOS_PushCommon.pushGWSocketQueue.remove(0);
					} catch(Exception e){
						logger_ios_push.info("[IOS Push ##-getPushGWSocket]IOS_Queue Clear");
						try{
							IOS_PushCommon.pushGWSocketQueue.clear();
						}catch(Exception ex){
							logger_ios_push.info("[IOS Push ##-getPushGWSocket]IOS_Queue Clear Error[" + ex.getMessage()+"]");
						}
						psVO = OpenSocket();
						//if(PushCommon.pushGWSocketQueue.size() >= PushCommon.pushGWSocketMinCnt) psVO.setQueueAddChk("NO_ADD");
					}
					
					if((GlobalCom.getTodayUnixtime()-psVO.getPushGWSocketCallTime()) >= IOS_PushCommon.pushGWSocketCloseSecend){
						//소켓 유효시간이 지났을 경우
						logger.debug("[IOS Push ##-getPushGWSocket]소켓 유효 시간 지남");
						try{
							closeSocket(psVO);
						}catch(Exception e){}
						OpenSocket();
						psVO = IOS_PushCommon.pushGWSocketQueue.remove(0);
						if(IOS_PushCommon.pushGWSocketQueue.size() >= IOS_PushCommon.pushGWSocketMinCnt){
							psVO.setQueueAddChk("NO_ADD");
						}
					}else{
						//소켓이 유효할 경우
						logger.debug("[IOS Push ##-getPushGWSocket]소켓 유효함");					
					}
				}
				
			
			}catch(ConnectException e){
				logger_ios_push.info("[IOS Push ##-getPushGWSocket][ConnectException]["+getTrancationStr()+IOS_PushCommon.trancationIDNum+"]["+IOS_PushCommon.pushGWSocketQueue.size()+"]["+psVO.getSa_id()+"]["+psVO.getStb_mac()+"]["+psVO.getWifi_mac()+"]");
				logger.debug("[IOS Push ##-getPushGWSocket]"+"["+e.getClass().getName()+"]["+e.getMessage()+"]");
				try{
					closeSocket(psVO);
				}catch(Exception e2){}
				
				SmartUXException exception = new SmartUXException();
				exception.setFlag(SmartUXProperties.getProperty("flag.pushgw.socket"));
				exception.setMessage(SmartUXProperties.getProperty("message.pushgw.socket"));
				throw exception;
			}catch(SocketException e){
				logger_ios_push.info("[IOS Push ##-getPushGWSocket][SocketException]["+getTrancationStr()+IOS_PushCommon.trancationIDNum+"]["+IOS_PushCommon.pushGWSocketQueue.size()+"]["+psVO.getSa_id()+"]["+psVO.getStb_mac()+"]["+psVO.getWifi_mac()+"]");
				logger.debug("[IOS Push ##-getPushGWSocket]"+"["+e.getClass().getName()+"]["+e.getMessage()+"]");
				try{
					closeSocket(psVO);
				}catch(Exception e2){}
				
				SmartUXException exception = new SmartUXException();
				exception.setFlag(SmartUXProperties.getProperty("flag.pushgw.socket"));
				exception.setMessage(SmartUXProperties.getProperty("message.pushgw.socket"));
				throw exception;
				
			}catch(SocketTimeoutException e){
				logger_ios_push.info("[IOS Push ##-getPushGWSocket][SocketTimeoutException]["+getTrancationStr()+IOS_PushCommon.trancationIDNum+"]["+IOS_PushCommon.pushGWSocketQueue.size()+"]["+psVO.getSa_id()+"]["+psVO.getStb_mac()+"]["+psVO.getWifi_mac()+"]");
				logger.debug("[IOS Push ##-getPushGWSocket]"+"["+e.getClass().getName()+"]["+e.getMessage()+"]");
				try{
					closeSocket(psVO);
				}catch(Exception e2){}
				
				SmartUXException exception = new SmartUXException();
				exception.setFlag(SmartUXProperties.getProperty("flag.pushgw.socketime"));
				exception.setMessage(SmartUXProperties.getProperty("message.pushgw.socketime"));
				throw exception;
				
			}catch(SmartUXException e){
				logger_ios_push.info("[IOS Push ##-getPushGWSocket][SmartUXException]"+e.getClass().getName());
				throw e;
			}catch(Exception e){
				logger_ios_push.info("[IOS Push ##-getPushGWSocket][Exception]["+getTrancationStr()+IOS_PushCommon.trancationIDNum+"]["+IOS_PushCommon.pushGWSocketQueue.size()+"]["+psVO.getSa_id()+"]["+psVO.getStb_mac()+"]["+psVO.getWifi_mac()+"]");
				logger.debug("[IOS Push ##-getPushGWSocket]"+"["+e.getClass().getName()+"]["+e.getMessage()+"]");
				try{
					closeSocket(psVO);
				}catch(Exception e2){}
				
				SmartUXException exception = new SmartUXException();
				exception.setFlag(SmartUXProperties.getProperty("flag.etc"));
				exception.setMessage(SmartUXProperties.getProperty("message.etc"));
				throw exception;
			}
			
			logger.debug("[IOS Push ##-getPushGWSocket]END");
			
			return psVO;
		}
	}
	
	
	/**
	 * PushGW 소켓을 전부 활성화 메서드
	 * @return
	 * @throws Exception 
	 */
	public void setPushGWSocketAllOpen() throws Exception{
		
		logger.debug("[IOS Push ##-setPushGWSocketAllOpen]START");
		
		logger.debug("IOS Push ##-IOS_PushCommon.pushGWSocketQueue.size() = "+IOS_PushCommon.pushGWSocketQueue.size());
		
		for(int i=0;i<IOS_PushCommon.pushGWSocketInitCnt && IOS_PushCommon.pushGWSocketQueue.size() < IOS_PushCommon.pushGWSocketInitCnt;i++){
			try{
				OpenSocket();
			}catch(SmartUXException e){
				logger.debug("[IOS Push ##-setPushGWSocketAllOpen]"+"]["+e.getClass().getName()+"]["+e.getMessage()+e.getFlag()+"]");
				throw e;
			}
		}
		
//		if(IOS_PushCommon.pushGWSocketQueue.size() < IOS_PushCommon.pushGWSocketMaxCnt){
//			setPushGWSocketAllOpen();
//		}
		logger.debug("[IOS Push ##-setPushGWSocketAllOpen]활성화 된 소켓 수 = "+IOS_PushCommon.pushGWSocketQueue.size());
		logger.debug("[IOS Push ##-setPushGWSocketAllOpen]END");
	}
	
	/**
	 * PushGW 소켓을 전부 비활성화 메서드
	 * @return
	 * @throws Exception 	 */
	public void setPushGWSocketAllClose() throws Exception{
		
		logger.debug("[IOS Push ##-setPushGWSocketAllClose]START");
			for(int i=0;i<IOS_PushCommon.pushGWSocketQueue.size();i++){
				try{
					IOS_PushSocketVO pvo = IOS_PushCommon.pushGWSocketQueue.remove(0);
					closeSocketQueue(pvo);
				}catch(Exception e){}
			}
			
			if(IOS_PushCommon.pushGWSocketQueue.size() < IOS_PushCommon.pushGWSocketInitCnt){
				setPushGWSocketAllOpen();
			}
		logger.debug("[IOS Push ##-setPushGWSocketAllClose]활성화 된 소켓 수 = "+IOS_PushCommon.pushGWSocketQueue.size());
		logger.debug("[IOS Push ##-setPushGWSocketAllClose]END");
	}
	
	/**
	 * PushGW 소켓 연결 메서드(Push GW ChannelConnectionRequest 요청 메서드)
	 * @return
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public IOS_PushSocketVO OpenSocket() throws Exception{
		
			logger.debug("[IOS Push ##-OpenSocket]START");
			
			IOS_PushSocketVO psVO = null;
	
			try{

				logger_ios_push.info("["+getTrancationStr()+IOS_PushCommon.trancationIDNum+"][OPEN_S]["+IOS_PushCommon.pushGWSocketQueue.size()+"]");
				//GlobalCom.testIOS_PushGWLog(getTrancationStr()+IOS_PushCommon.trancationIDNum, "[OPEN_S]["+IOS_PushCommon.pushGWSocketQueue.size()+"]");
				
				IOS_PushCommon.pushGWSocketNowCnt++;
				
				//Socket socket = new Socket(IOS_PushCommon.pushGWServerIP,IOS_PushCommon.pushGWServerPort);
				Socket socket = new Socket();
				SocketAddress addr = new InetSocketAddress(IOS_PushCommon.pushGWServerIP,IOS_PushCommon.pushGWServerPort);
				socket.connect(addr, IOS_PushCommon.pushGWSocketSendTimeout);
				psVO = new IOS_PushSocketVO(socket);
				openInOutStream(psVO);
				
				psVO.getPushGWSocket().setSoTimeout(IOS_PushCommon.pushGWSocketSendTimeout);
				
				byte[] byte_MessageID = ByteUtil.int2byte(1);
				byte[] byte_TranscationID = new byte[12];
				byte[] byte_SourceIP = new byte[16];
				ByteUtil.setbytes(byte_SourceIP,0,IOS_PushCommon.sourceIP.getBytes());
				int sourceIPNullCnt = 16-IOS_PushCommon.sourceIP.length();
				logger.debug("[IOS Push ##-OpenSocket]sourceIPNullCnt = "+sourceIPNullCnt);	
				for(int i=0;i<sourceIPNullCnt;i++){
					ByteUtil.setbytes(byte_SourceIP,(IOS_PushCommon.sourceIP.length()+i),new byte[1]);
				}
				
				byte[] byte_DestinationIP = new byte[16];
				ByteUtil.setbytes(byte_DestinationIP,0,IOS_PushCommon.destinationIP.getBytes());
				int destinationIPNullCnt = 16-IOS_PushCommon.destinationIP.length();				
				logger.debug("[IOS Push ##-OpenSocket]destinationIPNullCnt = "+destinationIPNullCnt);				
				for(int i=0;i<destinationIPNullCnt;i++){
					ByteUtil.setbytes(byte_DestinationIP,(IOS_PushCommon.destinationIP.length()+i),new byte[1]);
				}
				
				byte[] byte_Reserved = new byte[12];
				
				byte[] byte_DATALength = ByteUtil.int2byte(0);
				
				int byteTotalLen = byte_MessageID.length
						+byte_TranscationID.length
						+byte_SourceIP.length
						+byte_DestinationIP.length
						+byte_Reserved.length
						+byte_DATALength.length;
						
				logger.debug("[IOS Push ##-OpenSocket]byteTotalLen="+byteTotalLen);
				
				byte[] byteTotalData = new byte[byteTotalLen];
				ByteUtil.setbytes(byteTotalData, 0, 
						byte_MessageID);
				ByteUtil.setbytes(byteTotalData, byte_MessageID.length, 
						byte_TranscationID);
				ByteUtil.setbytes(byteTotalData, (byte_MessageID.length+byte_TranscationID.length), 
						byte_SourceIP);
				ByteUtil.setbytes(byteTotalData, (byte_MessageID.length+byte_TranscationID.length+byte_SourceIP.length), 
						byte_DestinationIP);
				ByteUtil.setbytes(byteTotalData, (byte_MessageID.length+byte_TranscationID.length+byte_SourceIP.length+byte_DestinationIP.length), 
						byte_Reserved);
				ByteUtil.setbytes(byteTotalData, (byte_MessageID.length+byte_TranscationID.length+byte_SourceIP.length+byte_DestinationIP.length+byte_Reserved.length), 
						byte_DATALength);
				
				logger.debug("[IOS Push ##-OpenSocket]서버 요청 byte_MessageID="+1);
				logger.debug("[IOS Push ##-OpenSocket]서버 요청 byte_TranscationDate="+"");
				logger.debug("[IOS Push ##-OpenSocket]서버 요청 byte_TranscationNum="+"");
				logger.debug("[IOS Push ##-OpenSocket]서버 요청 byte_SourceIP="+IOS_PushCommon.sourceIP);
				logger.debug("[IOS Push ##-OpenSocket]서버 요청 byte_DestinationIP="+IOS_PushCommon.destinationIP);
				logger.debug("[IOS Push ##-OpenSocket]서버 요청 byte_Reserved="+"       ");
				
				psVO.getPushGWDataOut().write(byteTotalData);
				psVO.getPushGWDataOut().flush();
				
				
				logger.debug("IOS Push ##-======================= Header =========================");
				byte[] byte_response_MessageID = new byte[4];
				psVO.getPushGWDataIn().read(byte_response_MessageID, 0, byte_response_MessageID.length);
				int response_MessageID = ByteUtil.getint(byte_response_MessageID,0);
				logger.debug("[IOS Push ##-OpenSocket] 서버 응답 response_MessageID = "+response_MessageID);
				
				byte[] byte_response_TransactionDate = new byte[8];
				psVO.getPushGWDataIn().read(byte_response_TransactionDate, 0, byte_response_TransactionDate.length);
				String response_TransactionDate = new String(byte_response_TransactionDate);
				logger.debug("[IOS Push ##-OpenSocket] 서버 응답 response_TransactionDate = "+response_TransactionDate);
				
				byte[] byte_response_TransactionNum = new byte[4];
				psVO.getPushGWDataIn().read(byte_response_TransactionNum, 0, byte_response_TransactionNum.length);
				int response_TransactionNum = ByteUtil.getint(byte_response_TransactionNum,0);
				logger.debug("[IOS Push ##-OpenSocket] 서버 응답 response_TransactionNum = "+response_TransactionNum);
				
				byte[] byte_response_SourceIP = new byte[16];
				psVO.getPushGWDataIn().read(byte_response_SourceIP, 0, byte_response_SourceIP.length);
				String response_SourceIP = new String(byte_response_SourceIP);
				logger.debug("[IOS Push ##-OpenSocket] 서버 응답 response_SourceIP = "+response_SourceIP);
				
				byte[] byte_response_DestinationIP = new byte[16];
				psVO.getPushGWDataIn().read(byte_response_DestinationIP, 0, byte_response_DestinationIP.length);
				String response_DestinationIP = new String(byte_response_DestinationIP);
				logger.debug("[IOS Push ##-OpenSocket] 서버 응답 response_DestinationIP = "+response_DestinationIP);
				
				byte[] byte_response_Reserved = new byte[12];
				psVO.getPushGWDataIn().read(byte_response_Reserved, 0, byte_response_Reserved.length);
				String response_Reserved = new String(byte_response_Reserved);
				logger.debug("[IOS Push ##-OpenSocket] 서버 응답 byte_response_Reserved = "+response_Reserved);
				
				byte[] byte_response_DataLength = new byte[4];
				psVO.getPushGWDataIn().read(byte_response_DataLength, 0, byte_response_DataLength.length);
				int response_DataLength = ByteUtil.getint(byte_response_DataLength,0);
				logger.debug("[IOS Push ##-OpenSocket] 서버 응답 response_DataLength = "+response_DataLength);
				
				byte[] byte_response_Data = new byte[response_DataLength];
				psVO.getPushGWDataIn().read(byte_response_Data, 0, byte_response_Data.length);
				String response_Data = new String(byte_response_Data);
				logger.debug("[IOS Push ##-OpenSocket] 서버 응답 response_Data = "+response_Data);

				logger_ios_push.info("["+getTrancationStr()+IOS_PushCommon.trancationIDNum+"][OPEN_R]["+IOS_PushCommon.pushGWSocketQueue.size()+"] - ["+(response_Data.replace( System.getProperty( "line.separator" ), "" )).replace( "\r", "" )+"]");
				//GlobalCom.testIOS_PushGWLog(getTrancationStr()+IOS_PushCommon.trancationIDNum, "[OPEN_R]["+IOS_PushCommon.pushGWSocketQueue.size()+"] - ["+response_Data+"]");
				
				if(response_DataLength >= 2){
					if(response_Data.substring(0,2).equalsIgnoreCase("SC")){
						logger_ios_push.info("["+getTrancationStr()+IOS_PushCommon.trancationIDNum+"][OPEN_E]["+IOS_PushCommon.pushGWSocketQueue.size()+"] - [SUCCESS]");
						//GlobalCom.testIOS_PushGWLog(getTrancationStr()+IOS_PushCommon.trancationIDNum, "[OPEN_E]["+IOS_PushCommon.pushGWSocketQueue.size()+"] - [SUCCESS]");
						logger.debug("[IOS Push ##-OpenSocket]ChannelConnectionRequest 성공");
						IOS_addPushQueue(psVO);
					}else{
						//logger_ios_push.info("["+getTrancationStr()+IOS_PushCommon.trancationIDNum+"][OPEN_E]["+IOS_PushCommon.pushGWSocketQueue.size()+"] - [FAIL]");
						//GlobalCom.testIOS_PushGWLog(getTrancationStr()+IOS_PushCommon.trancationIDNum, "[OPEN_E]["+IOS_PushCommon.pushGWSocketQueue.size()+"] - [FAIL]");
						logger.debug("[IOS Push ##-OpenSocket]ChannelConnectionRequest 실패");
						
						SocketException exception = new SocketException();
						throw exception;
					}
				}
			}catch(ConnectException e){
				logger_ios_push.info("[ConnectException][OPEN_E]["+IOS_PushCommon.pushGWSocketQueue.size()+"] - [FAIL]");
				//GlobalCom.testIOS_PushGWLog(getTrancationStr()+IOS_PushCommon.trancationIDNum, "[OPEN_E]["+IOS_PushCommon.pushGWSocketQueue.size()+"] - [FAIL]");
				logger.debug("[IOS Push ##-OpenSocket]"+e.getClass().getName()+"]["+e.getMessage()+"]");
				closeSocketQueue(psVO);
				
				SmartUXException exception = new SmartUXException();
				exception.setFlag(SmartUXProperties.getProperty("flag.pushgw.socket"));
				exception.setMessage(SmartUXProperties.getProperty("message.pushgw.socket"));
				throw exception;
			}catch(SocketException e){
				logger_ios_push.info("[SocketException][OPEN_E]["+IOS_PushCommon.pushGWSocketQueue.size()+"] - [FAIL]");
				//GlobalCom.testIOS_PushGWLog(getTrancationStr()+IOS_PushCommon.trancationIDNum, "[OPEN_E]["+IOS_PushCommon.pushGWSocketQueue.size()+"] - [FAIL]");
				logger.debug("[IOS Push ##-OpenSocket]"+e.getClass().getName()+"]["+e.getMessage()+"]");
				closeSocketQueue(psVO);
				
				SmartUXException exception = new SmartUXException();
				exception.setFlag(SmartUXProperties.getProperty("flag.pushgw.socket"));
				exception.setMessage(SmartUXProperties.getProperty("message.pushgw.socket"));
				throw exception;
				
			}catch(SocketTimeoutException e){
				logger_ios_push.info("[SocketTimeoutException][OPEN_E]["+IOS_PushCommon.pushGWSocketQueue.size()+"] - [FAIL]");
				//GlobalCom.testIOS_PushGWLog(getTrancationStr()+IOS_PushCommon.trancationIDNum, "[OPEN_E]["+IOS_PushCommon.pushGWSocketQueue.size()+"] - [FAIL]");
				logger.debug("[IOS Push ##-OpenSocket]"+e.getClass().getName()+"]["+e.getMessage()+"]");
				closeSocketQueue(psVO);
				
				SmartUXException exception = new SmartUXException();
				exception.setFlag(SmartUXProperties.getProperty("flag.pushgw.socketime"));
				exception.setMessage(SmartUXProperties.getProperty("message.pushgw.socketime"));
				throw exception;
				
			}catch(Exception e){
				logger_ios_push.info("[Exception][OPEN_E]["+IOS_PushCommon.pushGWSocketQueue.size()+"] - [FAIL]");
				//GlobalCom.testIOS_PushGWLog(getTrancationStr()+IOS_PushCommon.trancationIDNum, "[OPEN_E]["+IOS_PushCommon.pushGWSocketQueue.size()+"] - [FAIL]");
				logger.debug("[IOS Push ##-OpenSocket]"+e.getClass().getName()+"]["+e.getMessage()+"]");
				closeSocketQueue(psVO);
				
				SmartUXException exception = new SmartUXException();
				exception.setFlag(SmartUXProperties.getProperty("flag.etc"));
				exception.setMessage(SmartUXProperties.getProperty("message.etc"));
				throw exception;
			}
			logger.debug("[IOS Push ##-OpenSocket]활성화 된 소켓 수= "+IOS_PushCommon.pushGWSocketQueue.size());
			
			logger.debug("[IOS Push ##-OpenSocket]END");
			return psVO;
	}
	
	
	/**
	 * PushGW 소켓 및 큐 해제 메서드
	 * @param psVO
	 */
	public void closeSocketQueue(IOS_PushSocketVO psVO) throws Exception{
					
			logger.debug("[IOS Push ##-closeSocketQueue]START");
			try{
				closeSocket(psVO);
					
				//IOS_PushCommon.pushGWSocketQueue.remove(psVO);
			}catch(Exception e){
				logger.debug("[IOS Push ##-closeSocketQueue]"+e.getClass().getName()+"]["+e.getMessage()+"]");
			}
			logger.debug("[IOS Push ##-closeSocketQueue]END");
	}
	
	/**
	 * PushGW 소켓 해제 메서드
	 * @param psVO
	 */
	public void closeSocket(IOS_PushSocketVO psVO) throws Exception{
					
			logger.debug("[IOS Push ##-closeSocket]START");
			
			try{
				IOS_PushCommon.pushGWSocketNowCnt--;
			
				psVO.getPushGWIn().close();
				psVO.getPushGWDataIn().close();
				
				psVO.getPushGWOut().close();
				psVO.getPushGWDataOut().close();
				
				psVO.getPushGWSocket().close();
			
			}catch(Exception e){
				logger.debug("[IOS Push ##-closeSocket]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			}
			
			logger.debug("[IOS Push ##-closeSocket]END");
	}
	
	/**
	 * PushGW 소켓 In/Out 스트림 활성화 메서드
	 * @param psVO
	 */
	public void openInOutStream(IOS_PushSocketVO psVO) throws Exception{
			
			logger.debug("[IOS Push ##-openInOutStream]START");
		
			psVO.setPushGWIn(psVO.getPushGWSocket().getInputStream());
			psVO.setPushGWDataIn(new DataInputStream(psVO.getPushGWIn()));
			
			psVO.setPushGWOut(psVO.getPushGWSocket().getOutputStream());
			psVO.setPushGWDataOut(new DataOutputStream(psVO.getPushGWOut()));
			
			logger.debug("[IOS Push ##-openInOutStream]END");
	}	
	
	/**
	 * PushGW 소켓 In/Out 스트림 CLOSE 메서드
	 * @param psVO
	 * @throws Exception
	 */
	public void closeInOutStream(IOS_PushSocketVO psVO) throws Exception{
		
		logger.debug("[IOS Push ##-closeInOutStream]START");
	
		psVO.getPushGWIn().close();
		psVO.getPushGWDataIn().close();
		
		psVO.getPushGWOut().close();
		psVO.getPushGWDataOut().close();
		
		logger.debug("[IOS Push ##-closeInOutStream]END");
	}	
	
	
	public String setXmlData(IOS_PushRequestVO pvo){
		
		String result = "";
		result ="<?xml version=\"1.0\" encoding=\"utf-8\"?>";
		result +="<request>"
					+"<msg_id>"+pvo.getMsg_id()+"</msg_id>"
					+"<transaction_id>"+pvo.getTransaction_id()+"</transaction_id>"
					+"<service_id>"+pvo.getService_id()+"</service_id>"
					+"<service_passwd>"+pvo.getService_passwd()+"</service_passwd>"
					+"<push_app_id>"+pvo.getPush_app_id()+"</push_app_id>"
					+"<user user_id=\""+pvo.getUser_id()+"\">"
						+"<from_user_id>"+pvo.getFrom_user_id()+"</from_user_id>"
						+"<origin_user_id>"+pvo.getFrom_user_id()+"</origin_user_id>"
						+"<noti_type>"+pvo.getNoti_type()+"</noti_type>"
						+"<noti_contents>"+pvo.getNoti_contents()+"</noti_contents>"
						+"<user_param>"+pvo.getUser_param()+"</user_param>"
					+"</user>"
				+"</request>";
		
		return result;
	}
	
	public String getTrancationStr(){
		String result;
		InetAddress addr;
		String hostname;
		try {
			addr = InetAddress.getLocalHost();
			hostname = addr.getHostName();
		} catch (UnknownHostException e1) {
			hostname = "";
		}
		logger.debug("hostname 1 = "+hostname); 
		if(hostname.length() > 8){
			hostname = hostname.substring((hostname.length()-8),hostname.length());
		}else{
			int hostnameCnt = 8-hostname.length();
			for(int i=0;i<hostnameCnt;i++){
				hostname += "0";
			}
		}
		String port = GlobalCom.getSystemProperty("JBOSS_PORT");
		try{
			port = port.substring(0,2);
		}catch (Exception e) {	//방어코드
			port = "00";
		}
		logger.debug("port 2 = "+port);
		result = hostname+port;
		return result;
	}
	
	public void IOS_addPushQueue(IOS_PushSocketVO psVO){
		synchronized(IOS_PushCommon.pushGWSocketQueue) {
			IOS_PushCommon.pushGWSocketQueue.add(psVO);
		}
	}
}
