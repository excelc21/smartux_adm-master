package com.dmi.smartux.push.vo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.dmi.smartux.common.util.GlobalCom;

/**
 * Push Socket 정보 클래스
 * @author YJ KIM
 *
 */
public class PushSocketVO {
	
	private Socket pushGWSocket;			//Push G/W 소켓
	private InputStream pushGWIn;			//Push G/W InputStream
	private DataInputStream pushGWDataIn;	//Push G/W DataInputStream
	private OutputStream pushGWOut;		//Push G/W OutputStream
	private DataOutputStream pushGWDataOut;//Push G/W DataOutputStream
	
	private long pushGWSocketCallTime;		//Push G/W 소켓 생성/사용 시간 
	
	private int messageID;			//Push G/W Header messageID
	
	private String transactionDate;	//Push G/W Header transactionDate
	private int transactionNum;		//Push G/W Header transactionNum
	private String transactionIDStr;	//Push G/W Header transactionIDStr
	private Byte[] transactionID;		//Push G/W Header transactionID

	private String channelID;			//Push G/W Header channelID
	private int channelNum;			//Push G/W Header channelNum
	
	private String destinationIP;	//Push G/W Header Push G/W IP
	private String reserved1;		//Push G/W Header Reserved1 (공백)
	private String reserved2;		//Push G/W Header Reserved2 (공백)
	private String data_length;	//Push G/W Body Data 길이
	private String noti_message;	//Push G/W Noti 통해 전달 될 값
	
	private String regist_id;		//Push G/W Noti 전달 위치의 값 Regist_id
	private String queueAddChk;	//사용한 소켓정보 클래스를 큐에 저장할지 판단 값(ADD: 큐에 저장   NO_ADD: 큐에 미저장)
	
	private String request_ip;		//Push Noti 요청 IP
	
	private String sa_id;
	private String stb_mac;
	private String wifi_mac;
	
	/**
	 * PushGWSocketVO 생성자
	 * @param pushGWSocket PushGW 소켓
	 */
	public PushSocketVO(Socket pushGWSocket) {
		setPushGWSocket(pushGWSocket);
		setPushGWSocketCallTime(GlobalCom.getTodayUnixtime());	//유닉스타임 설정
		setQueueAddChk("ADD");	//큐에 저장 설정
	}
	
	/**
	 * Push G/W 소켓
	 * @return the pushGWSocket
	 */
	public Socket getPushGWSocket() {
		return pushGWSocket;
	}



	/**
	 * Push G/W 소켓
	 * @param pushGWSocket the pushGWSocket to set
	 */
	public void setPushGWSocket(Socket pushGWSocket) {
		this.pushGWSocket = pushGWSocket;
	}



	/**
	 * Push G/W InputStream
	 * @return the pushGWIn
	 */
	public InputStream getPushGWIn() {
		return pushGWIn;
	}



	/**
	 * Push G/W InputStream
	 * @param pushGWIn the pushGWIn to set
	 */
	public void setPushGWIn(InputStream pushGWIn) {
		this.pushGWIn = pushGWIn;
	}



	/**
	 * Push G/W DataInputStream
	 * @return the pushGWDataIn
	 */
	public DataInputStream getPushGWDataIn() {
		return pushGWDataIn;
	}



	/**
	 * Push G/W DataInputStream
	 * @param pushGWDataIn the pushGWDataIn to set
	 */
	public void setPushGWDataIn(DataInputStream pushGWDataIn) {
		this.pushGWDataIn = pushGWDataIn;
	}



	/**
	 * Push G/W OutputStream
	 * @return the pushGWOut
	 */
	public OutputStream getPushGWOut() {
		return pushGWOut;
	}



	/**
	 * Push G/W OutputStream
	 * @param pushGWOut the pushGWOut to set
	 */
	public void setPushGWOut(OutputStream pushGWOut) {
		this.pushGWOut = pushGWOut;
	}



	/**
	 * Push G/W DataOutputStream
	 * @return the pushGWDataOut
	 */
	public DataOutputStream getPushGWDataOut() {
		return pushGWDataOut;
	}



	/**
	 * Push G/W DataOutputStream
	 * @param pushGWDataOut the pushGWDataOut to set
	 */
	public void setPushGWDataOut(DataOutputStream pushGWDataOut) {
		this.pushGWDataOut = pushGWDataOut;
	}



	/**
	 * Push G/W 소켓 생성/사용 시간
	 * @return the pushGWSocketCallTime
	 */
	public long getPushGWSocketCallTime() {
		return pushGWSocketCallTime;
	}



	/**
	 * Push G/W 소켓 생성/사용 시간 
	 * @param pushGWSocketCallTime the pushGWSocketCallTime to set
	 */
	public void setPushGWSocketCallTime(long pushGWSocketCallTime) {
		this.pushGWSocketCallTime = pushGWSocketCallTime;
	}



	/**
	 * Push G/W Header messageID
	 * @return the messageID
	 */
	public int getMessageID() {
		return messageID;
	}



	/**
	 * Push G/W Header messageID
	 * @param messageID the messageID to set
	 */
	public void setMessageID(int messageID) {
		this.messageID = messageID;
	}



	/**
	 * Push G/W Header transactionDate
	 * @return the transactionDate
	 */
	public String getTransactionDate() {
		return transactionDate;
	}



	/**
	 * Push G/W Header transactionDate
	 * @param transactionDate the transactionDate to set
	 */
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}



	/**
	 * Push G/W Header transactionNum
	 * @return the transactionNum
	 */
	public int getTransactionNum() {
		return transactionNum;
	}



	/**
	 * Push G/W Header transactionNum
	 * @param transactionNum the transactionNum to set
	 */
	public void setTransactionNum(int transactionNum) {
		this.transactionNum = transactionNum;
	}



	/**
	 * Push G/W Header transactionID
	 * @return the transactionID
	 */
	public Byte[] getTransactionID() {
		return transactionID;
	}



	/**
	 * Push G/W Header transactionID
	 * @param transactionID the transactionID to set
	 */
	public void setTransactionID(Byte[] transactionID) {
		this.transactionID = transactionID;
	}



	/**
	 * Push G/W Header channelID
	 * @return the channelID
	 */
	public String getChannelID() {
		return channelID;
	}



	/**
	 * Push G/W Header channelID
	 * @param channelID the channelID to set
	 */
	public void setChannelID(String channelID) {
		this.channelID = channelID;
	}



	/**
	 * Push G/W Header channelNum
	 * @return the channelNum
	 */
	public int getChannelNum() {
		return channelNum;
	}



	/**
	 * Push G/W Header channelNum
	 * @param channelNum the channelNum to set
	 */
	public void setChannelNum(int channelNum) {
		this.channelNum = channelNum;
	}



	/**
	 * Push G/W Header Push G/W IP
	 * @return the destinationIP
	 */
	public String getDestinationIP() {
		return destinationIP;
	}



	/**
	 * Push G/W Header Push G/W IP
	 * @param destinationIP the destinationIP to set
	 */
	public void setDestinationIP(String destinationIP) {
		this.destinationIP = destinationIP;
	}



	/**
	 * Push G/W Header Reserved1 (공백)
	 * @return the reserved1
	 */
	public String getReserved1() {
		return reserved1;
	}



	/**
	 * Push G/W Header Reserved1 (공백)
	 * @param reserved1 the reserved1 to set
	 */
	public void setReserved1(String reserved1) {
		this.reserved1 = reserved1;
	}



	/**
	 * Push G/W Header Reserved2 (공백)
	 * @return the reserved2
	 */
	public String getReserved2() {
		return reserved2;
	}



	/**
	 * Push G/W Header Reserved2 (공백)
	 * @param reserved2 the reserved2 to set
	 */
	public void setReserved2(String reserved2) {
		this.reserved2 = reserved2;
	}



	/**
	 * Push G/W Body Data 길이
	 * @return the data_length
	 */
	public String getData_length() {
		return data_length;
	}



	/**
	 * Push G/W Body Data 길이
	 * @param data_length the data_length to set
	 */
	public void setData_length(String data_length) {
		this.data_length = data_length;
	}



	/**
	 * Push G/W Noti 통해 전달 될 값
	 * @return the noti_message
	 */
	public String getNoti_message() {
		return noti_message;
	}



	/**
	 * Push G/W Noti 통해 전달 될 값
	 * @param noti_message the noti_message to set
	 */
	public void setNoti_message(String noti_message) {
		this.noti_message = noti_message;
	}



	/**
	 * Push G/W Noti 전달 위치의 값 Regist_id
	 * @param regist_id the regist_id to set
	 */
	public void setRegist_id(String regist_id) {
		this.regist_id = regist_id;
	}

	/**
	 * Push G/W Noti 전달 위치의 값 Regist_id
	 * @return the regist_id
	 */
	public String getRegist_id() {
		return regist_id;
	}

	/**
	 * Push G/W Header transactionIDStr
	 * @param transactionIDStr the transactionIDStr to set
	 */
	public void setTransactionIDStr(String transactionIDStr) {
		this.transactionIDStr = transactionIDStr;
	}

	/**
	 * Push G/W Header transactionIDStr
	 * @return the transactionIDStr
	 */
	public String getTransactionIDStr() {
		return transactionIDStr;
	}

	/**
	 * 사용한 소켓정보 클래스를 큐에 저장할지 판단 값(ADD: 큐에 저장   NO_ADD: 큐에 미저장)
	 * @return the queueAddChk
	 */
	public String getQueueAddChk() {
		return queueAddChk;
	}

	/**
	 * 사용한 소켓정보 클래스를 큐에 저장할지 판단 값(ADD: 큐에 저장   NO_ADD: 큐에 미저장)
	 * @param queueAddChk the queueAddChk to set
	 */
	public void setQueueAddChk(String queueAddChk) {
		this.queueAddChk = queueAddChk;
	}

	/**
	 * Push Noti 요청 IP
	 * @return the request_ip
	 */
	public String getRequest_ip() {
		return request_ip;
	}

	/**
	 * Push Noti 요청 IP
	 * @param request_ip the request_ip to set
	 */
	public void setRequest_ip(String request_ip) {
		this.request_ip = request_ip;
	}

	/**
	 * @return the sa_id
	 */
	public String getSa_id() {
		return sa_id;
	}

	/**
	 * @param sa_id the sa_id to set
	 */
	public void setSa_id(String sa_id) {
		this.sa_id = sa_id;
	}

	/**
	 * @return the stb_mac
	 */
	public String getStb_mac() {
		return stb_mac;
	}

	/**
	 * @param stb_mac the stb_mac to set
	 */
	public void setStb_mac(String stb_mac) {
		this.stb_mac = stb_mac;
	}

	/**
	 * @return the wifi_mac
	 */
	public String getWifi_mac() {
		return wifi_mac;
	}

	/**
	 * @param wifi_mac the wifi_mac to set
	 */
	public void setWifi_mac(String wifi_mac) {
		this.wifi_mac = wifi_mac;
	}
	
	
	
}
