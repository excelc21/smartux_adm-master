package com.dmi.smartux.ios_push;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.dmi.smartux.common.util.ByteUtil;

/**
 * IOS PushGW 연동 테스트 서버 클래스
 * @author YJ
 *
 */
public class PushOpenServer {
private static int serverSocketPort = 8888;
	
	public static void main(String[] args) {
		new PushOpenServer().start();
	}
	
	public void start() {
		ServerSocket serverSocket = null;
		Socket socket = null;

		try {
			serverSocket = new ServerSocket(serverSocketPort);
			System.out.println("서버가 시작되었습니다.");

			while(true) {
				socket = serverSocket.accept();
				System.out.println("["+socket.getInetAddress() + ":"+socket.getPort()+"]"+"에서 접속하였습니다.");
				ServerReceiver thread = new ServerReceiver(socket);
				thread.start();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	class ServerReceiver extends Thread {
		Socket socket;
		DataInputStream din;
		DataOutputStream dout;

		ServerReceiver(Socket socket) {
			this.socket = socket;
			try {
				din = new DataInputStream(socket.getInputStream());
				dout = new DataOutputStream(socket.getOutputStream());
			} catch(IOException e) {}
		}
		
		public void run() {
			while(din !=null){
				try {
					byte[] byte_response_MessageID = new byte[4];
					din.read(byte_response_MessageID, 0, byte_response_MessageID.length);
					int response_MessageID = ByteUtil.getint(byte_response_MessageID,0);
					if(response_MessageID != 0){
						System.out.println("IOS Push ##-OpenSocket  서버 응답 response_MessageID = "+response_MessageID);
					}
					byte[] byte_response_TransactionDate = new byte[8];
					din.read(byte_response_TransactionDate, 0, byte_response_TransactionDate.length);
					String response_TransactionDate = new String(byte_response_TransactionDate);
					if(response_MessageID != 0){
						System.out.println("IOS Push ##-OpenSocket  서버 응답 response_TransactionDate = "+response_TransactionDate);
					}
					byte[] byte_response_TransactionNum = new byte[4];
					din.read(byte_response_TransactionNum, 0, byte_response_TransactionNum.length);
					int response_TransactionNum = ByteUtil.getint(byte_response_TransactionNum,0);
					if(response_MessageID != 0){
						System.out.println("IOS Push ##-OpenSocket  서버 응답 response_TransactionNum = "+response_TransactionNum);
					}
					byte[] byte_response_SourceIP = new byte[16];
					din.read(byte_response_SourceIP, 0, byte_response_SourceIP.length);
					String response_SourceIP = new String(byte_response_SourceIP);
					if(response_MessageID != 0){
						System.out.println("IOS Push ##-OpenSocket  서버 응답 response_SourceIP = "+response_SourceIP);
					}
					byte[] byte_response_DestinationIP = new byte[16];
					din.read(byte_response_DestinationIP, 0, byte_response_DestinationIP.length);
					String response_DestinationIP = new String(byte_response_DestinationIP);
					if(response_MessageID != 0){
						System.out.println("IOS Push ##-OpenSocket  서버 응답 response_DestinationIP = "+response_DestinationIP);
					}
					byte[] byte_response_Reserved = new byte[12];
					din.read(byte_response_Reserved, 0, byte_response_Reserved.length);
					String response_Reserved = new String(byte_response_Reserved);
					if(response_MessageID != 0){
						System.out.println("IOS Push ##-OpenSocket  서버 응답 byte_response_Reserved = "+response_Reserved);
					}
					byte[] byte_response_DataLength = new byte[4];
					din.read(byte_response_DataLength, 0, byte_response_DataLength.length);
					int response_DataLength = ByteUtil.getint(byte_response_DataLength,0);
					if(response_MessageID != 0){
						System.out.println("IOS Push ##-OpenSocket  서버 응답 response_DataLength = "+response_DataLength);
					}
					byte[] byte_response_Data = new byte[response_DataLength];
					din.read(byte_response_Data, 0, byte_response_Data.length);
					String response_Data = new String(byte_response_Data);
					if(response_MessageID != 0){	
						System.out.println("IOS Push ##-OpenSocket  서버 응답 response_Data = "+response_Data);
					}
					//커넥션 응답
					if(response_MessageID == 1){
						byte[] byte_MessageID = ByteUtil.int2byte(response_MessageID);
						byte[] byte_TranscationID = new byte[12];
						ByteUtil.setbytes(byte_TranscationID,0,"111111111111".getBytes());
						byte[] byte_SourceIP = new byte[16];
						ByteUtil.setbytes(byte_SourceIP,0,"2222222222222222".getBytes());
						byte[] byte_DestinationIP = new byte[16];
						ByteUtil.setbytes(byte_DestinationIP,0,"3333333333333333".getBytes());
						byte[] byte_Reserved = new byte[12];
						byte[] byte_DATALength = ByteUtil.int2byte(2);
						byte[] byte_DATA = new byte[2];
						ByteUtil.setbytes(byte_DATA,0,"SC".getBytes());
						
						int byteTotalLen = byte_MessageID.length
								+byte_TranscationID.length
								+byte_SourceIP.length
								+byte_DestinationIP.length
								+byte_Reserved.length
								+byte_DATALength.length
								+byte_DATA.length;
						
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
						
						dout.write(byteTotalData);
						dout.flush();
					//Push Noti 응답
					}else{
						byte[] byte_MessageID = ByteUtil.int2byte(response_MessageID);
						byte[] byte_TranscationID = new byte[12];
						ByteUtil.setbytes(byte_TranscationID,0,"111111111111".getBytes());
						byte[] byte_SourceIP = new byte[16];
						ByteUtil.setbytes(byte_SourceIP,0,"2222222222222222".getBytes());
						byte[] byte_DestinationIP = new byte[16];
						ByteUtil.setbytes(byte_DestinationIP,0,"3333333333333333".getBytes());
						byte[] byte_Reserved = new byte[12];
						
						
						String result ="<?xml version=\"1.0\" encoding=\"utf-8\"?>";
						result +="<response>"
									+"<msg_id>PUSH_NOTI</msg_id>"
									+"<transaction_id>111111111111</transaction_id>"
									+"<app_device_os>iOS</app_device_os>"
									+"<app_device_ver>4</app_device_ver>"
									+"<result_code>200</result_code>"
									+"<result_msg>OK</result_msg>"
								+"</response>";
						
						
						
						byte[] byte_DATA = result.getBytes();
						byte[] byte_DATALength = ByteUtil.int2byte(result.length());
						
						
						int byteTotalLen = byte_MessageID.length
								+byte_TranscationID.length
								+byte_SourceIP.length
								+byte_DestinationIP.length
								+byte_Reserved.length
								+byte_DATALength.length
								+byte_DATA.length;
						
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
						
						dout.write(byteTotalData);
						dout.flush();
					}
					
				}catch(Exception e){}
			}
		}
	}
}
