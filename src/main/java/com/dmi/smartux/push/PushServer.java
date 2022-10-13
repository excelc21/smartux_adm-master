package com.dmi.smartux.push;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

import org.json.JSONException;
import org.json.JSONObject;

import com.dmi.smartux.common.util.ByteUtil;
import com.dmi.smartux.common.util.Json;

/**
 * Push G/W 서버 테스트 클래스
 * @author YJ KIM
 *
 */
public class PushServer {
	
	private static int serverSocketPort = 80;
	
	public static void main(String[] args) {
		new PushServer().start();
	}
	
	public void start() {
		ServerSocket serverSocket = null;
		Socket socket = null;

		try {
			serverSocket = new ServerSocket(80);
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
	//		byte[] byte_TranscationID = new byte[12];
	//		byte[] byte_TranscationDate = new byte[8];
	//		int byte_TranscationNum;
	//		din.readFully(byte_TranscationID);
	//		byte_TranscationDate = ByteUtil.getbytes(byte_TranscationID, 0, byte_TranscationDate.length);
	//		//byte_TranscationNum = ByteUtil.getbytes(byte_TranscationID, byte_TranscationDate.length, byte_TranscationNum.length);
	//		byte_TranscationNum = ByteUtil.getint(byte_TranscationID,byte_TranscationDate.length);
	//		String responseVal = new String(byte_TranscationDate);				
	//		System.out.println(" 클라이언트 요청1 = "+responseVal);
	//		System.out.println(" 클라이언트 요청2 = "+byte_TranscationNum);
			
			while(din !=null){
				try {
					byte[] byte_TotalData = new byte[464];
					
					din.readFully(byte_TotalData);
					
					int byte_MessageID;
					byte_MessageID = ByteUtil.getint(byte_TotalData,0);
					System.out.println("======================= Header =========================");
					System.out.println(" 클라이언트 요청 byte_MessageID = "+byte_MessageID);
					
					byte[] byte_TranscationDate = new byte[8];
					int byte_TranscationNum;
					byte_TranscationDate = ByteUtil.getbytes(byte_TotalData, ByteUtil.int2byte(byte_MessageID).length, byte_TranscationDate.length);
					byte_TranscationNum = ByteUtil.getint(byte_TotalData,(ByteUtil.int2byte(byte_MessageID).length+byte_TranscationDate.length));
					String byte_TranscationDateStr = new String(byte_TranscationDate);				
					System.out.println(" 클라이언트 요청 byte_TranscationDate = "+byte_TranscationDateStr);
					System.out.println(" 클라이언트 요청 byte_TranscationNum = "+byte_TranscationNum);
					
					byte[] byte_ChannelID = new byte[14];
					byte_ChannelID = ByteUtil.getbytes(byte_TotalData, 
							(ByteUtil.int2byte(byte_MessageID).length+byte_TranscationDate.length+ByteUtil.int2byte(byte_TranscationNum).length), 
							byte_ChannelID.length);
					String byte_ChannelIDStr = new String(byte_ChannelID);	
					System.out.println(" 클라이언트 요청 byte_ChannelIDStr = "+byte_ChannelIDStr);
					
					byte[] byte_Reserved1 = new byte[2];
					byte_Reserved1 = ByteUtil.getbytes(byte_TotalData, 
							(ByteUtil.int2byte(byte_MessageID).length+byte_TranscationDate.length+ByteUtil.int2byte(byte_TranscationNum).length+byte_ChannelID.length), 
							byte_Reserved1.length);
					String byte_Reserved1Str = new String(byte_Reserved1);	
					System.out.println(" 클라이언트 요청 byte_Reserved1 = "+byte_Reserved1Str);
									
					byte[] byte_DestinationIP = new byte[16];
					byte_DestinationIP = ByteUtil.getbytes(byte_TotalData, 
							(ByteUtil.int2byte(byte_MessageID).length+byte_TranscationDate.length+ByteUtil.int2byte(byte_TranscationNum).length+byte_ChannelID.length+byte_Reserved1.length), 
							byte_DestinationIP.length);
					String byte_DestinationIPStr = new String(byte_DestinationIP);	
					System.out.println(" 클라이언트 요청 byte_DestinationIP = "+byte_DestinationIPStr);
					
					byte[] byte_Reserved2 = new byte[12];
					byte_Reserved2 = ByteUtil.getbytes(byte_TotalData, 
							(ByteUtil.int2byte(byte_MessageID).length+byte_TranscationDate.length+ByteUtil.int2byte(byte_TranscationNum).length+byte_ChannelID.length+byte_Reserved1.length
							 +byte_DestinationIP.length), 
							byte_Reserved2.length);
					String byte_Reserved2Str = new String(byte_Reserved2);	
					System.out.println(" 클라이언트 요청 byte_Reserved2 = "+byte_Reserved2Str);
					
					int byte_DATALength;
					byte_DATALength = ByteUtil.getint(byte_TotalData,
							(ByteUtil.int2byte(byte_MessageID).length+byte_TranscationDate.length+ByteUtil.int2byte(byte_TranscationNum).length+byte_ChannelID.length+byte_Reserved1.length
							 +byte_DestinationIP.length+byte_Reserved2.length));
					System.out.println(" 클라이언트 요청 byte_DATALength = "+byte_DATALength);
					
					byte[] byte_DATA = new byte[byte_DATALength];
					byte_DATA = ByteUtil.getbytes(byte_TotalData, 
							(ByteUtil.int2byte(byte_MessageID).length+byte_TranscationDate.length+ByteUtil.int2byte(byte_TranscationNum).length+byte_ChannelID.length+byte_Reserved1.length
									 +byte_DestinationIP.length+byte_Reserved2.length+ByteUtil.int2byte(byte_DATALength).length), 
							 byte_DATA.length);
					String byte_DATAStr = new String(byte_DATA);	
					System.out.println(" 클라이언트 요청 byte_DATA = "+byte_DATAStr);
					
					System.out.println("======================= Body =========================");
					try {
						JSONObject jsonObject = new JSONObject(byte_DATAStr);
						JSONObject results = jsonObject.getJSONObject("request");
						String msg_id = results.getString("msg_id");
						String push_id = results.getString("push_id");
						String service_id = results.getString("service_id");
						String service_passwd = results.getString("service_passwd");
						String push_app_id = results.getString("push_app_id");
						String noti_type = results.getString("noti_type");
						String app_id = results.getString("app_id");
						String regist_id = results.getString("regist_id");
						String noti_contents = results.getString("noti_contents");
						
						System.out.println("클라이언트 요청 JSON msg_id = "+msg_id);
						System.out.println("클라이언트 요청 JSON push_id = "+push_id);
						System.out.println("클라이언트 요청 JSON service_id = "+service_id);
						System.out.println("클라이언트 요청 JSON service_passwd = "+service_passwd);
						System.out.println("클라이언트 요청 JSON push_app_id = "+push_app_id);
						System.out.println("클라이언트 요청 JSON noti_type = "+noti_type);
						System.out.println("클라이언트 요청 JSON app_id = "+app_id);
						System.out.println("클라이언트 요청 JSON regist_id = "+regist_id);
						System.out.println("클라이언트 요청 JSON noti_contents = "+noti_contents);
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
			//		OutputStream out = pushGWSocket.getOutputStream();
			//		DataOutputStream dout = new DataOutputStream(out);
			//		String data_response = "Server 성공";
			//		dout.writeUTF(data_response);
					
					System.out.println("================Response===========");
					
		//			OutputStream out = pushGWSocket.getOutputStream();
		//			DataOutputStream dout = new DataOutputStream(out);
					
					byte[] byte_MessageID2 = ByteUtil.int2byte(16);
					byte[] byte_TranscationDate2 = "20120326".getBytes();
					byte[] byte_TranscationNum2 = ByteUtil.int2byte(2222);
					byte[] byte_DestinationIP2 = "127.0.0.10000000".getBytes(); //자릿수 비교 하여 공백 바이트값 추가
					byte[] byte_ChannelID2 = "11111111111111".getBytes();
					byte[] byte_Reserved12 = "  ".getBytes();
					byte[] byte_Reserved22 = "            ".getBytes();
					
					Json json = new Json("response");
					Hashtable table1 = new Hashtable();
					table1.put("msg_id", "PUSH_NOTI");
					table1.put("push_id", byte_ChannelIDStr);
					table1.put("status_code", "200");
					
					json.setKeys(table1);		  
					json.addObject(table1);
					String jsonStr = json.toString();
					
					System.out.println(jsonStr);
					int dataLen = jsonStr.length();
					System.out.println("dataLen = "+dataLen);
					
					byte[] byte_DATALength2 = ByteUtil.int2byte(jsonStr.getBytes().length);
					byte[] byte_DATA2 = jsonStr.getBytes();
					
					System.out.println("byte_DATALength2 = "+ByteUtil.getint(byte_DATALength2,0));
					
					byte[] byte_TranscationID = new byte[byte_TranscationDate2.length+byte_TranscationNum2.length];
					//System.arraycopy(byte_TranscationDate, 0, byte_TranscationID, 0, byte_TranscationDate.length);
					//System.arraycopy(byte_TranscationNum, 0, byte_TranscationID, byte_TranscationDate.length, byte_TranscationNum.length);
					ByteUtil.setbytes(byte_TranscationID, 0, byte_TranscationDate2);
					ByteUtil.setbytes(byte_TranscationID, byte_TranscationDate2.length, byte_TranscationNum2);
					
					
					int byteTotalLen = byte_MessageID2.length
					+byte_TranscationID.length
					+byte_DestinationIP2.length
					+byte_ChannelID2.length
					+byte_Reserved12.length
					+byte_Reserved22.length
					+byte_DATALength2.length
					+byte_DATA2.length;
					
					System.out.println("byteTotalLen="+byteTotalLen);
					
					byte[] byteTotalData = new byte[byteTotalLen];
					
					ByteUtil.setbytes(byteTotalData, 0, 
							byte_MessageID2);
					ByteUtil.setbytes(byteTotalData, byte_MessageID2.length, 
							byte_TranscationID);
					ByteUtil.setbytes(byteTotalData, (byte_MessageID2.length+byte_TranscationID.length), 
							byte_DestinationIP2);
					ByteUtil.setbytes(byteTotalData, (byte_MessageID2.length+byte_TranscationID.length+byte_DestinationIP2.length), 
							byte_ChannelID2);
					ByteUtil.setbytes(byteTotalData, (byte_MessageID2.length+byte_TranscationID.length+byte_DestinationIP2.length+byte_ChannelID2.length), 
							byte_Reserved12);
					ByteUtil.setbytes(byteTotalData, (byte_MessageID2.length+byte_TranscationID.length+byte_DestinationIP2.length+byte_ChannelID2.length+byte_Reserved12.length), 
							byte_Reserved22);
					ByteUtil.setbytes(byteTotalData, (byte_MessageID2.length+byte_TranscationID.length+byte_DestinationIP2.length+byte_ChannelID2.length+byte_Reserved12.length+byte_Reserved22.length), 
							byte_DATALength2);
					ByteUtil.setbytes(byteTotalData, (byte_MessageID2.length+byte_TranscationID.length+byte_DestinationIP2.length+byte_ChannelID2.length+byte_Reserved12.length+byte_Reserved22.length+byte_DATALength2.length), 
							byte_DATA2);
					
					System.out.println("byteTotalData="+byteTotalData);
					dout.write(byteTotalData);
				}catch(Exception e){}
			}
			
		}
	}
	
//	public static void main(String[] args) {
//		ServerSocket pushGWserverSoket = null;
//		
//		try{
//			pushGWserverSoket = new ServerSocket(serverSocketPort);
//			System.out.println(getTime()+"서버 준비");
//			
//		}catch(IOException ioe){
//			System.out.println("서버 오픈 에러");
//		}
//		
//		while(true){
//			try{
//				Socket pushGWSocket = pushGWserverSoket.accept();
//				
//				InputStream in  = pushGWSocket.getInputStream();
//				DataInputStream din = new DataInputStream(in);
//				
//				//byte[] byte_DestinationIP = new byte[9];
//				//String responseVal = new String(byte_TranscationID);				
//				//System.out.println(" 클라이언트 요청 = "+responseVal);
//				//din.readFully(byte_DestinationIP);
//				//String responseVal = new String(byte_DestinationIP);
//				
////				byte[] byte_TranscationID = new byte[12];
////				byte[] byte_TranscationDate = new byte[8];
////				int byte_TranscationNum;
////				din.readFully(byte_TranscationID);
////				byte_TranscationDate = ByteUtil.getbytes(byte_TranscationID, 0, byte_TranscationDate.length);
////				//byte_TranscationNum = ByteUtil.getbytes(byte_TranscationID, byte_TranscationDate.length, byte_TranscationNum.length);
////				byte_TranscationNum = ByteUtil.getint(byte_TranscationID,byte_TranscationDate.length);
////				String responseVal = new String(byte_TranscationDate);				
////				System.out.println(" 클라이언트 요청1 = "+responseVal);
////				System.out.println(" 클라이언트 요청2 = "+byte_TranscationNum);
//				
//				byte[] byte_TotalData = new byte[464];
//				
//				din.readFully(byte_TotalData);
//				
//				int byte_MessageID;
//				byte_MessageID = ByteUtil.getint(byte_TotalData,0);
//				System.out.println("======================= Header =========================");
//				System.out.println(" 클라이언트 요청 byte_MessageID = "+byte_MessageID);
//				
//				byte[] byte_TranscationDate = new byte[8];
//				int byte_TranscationNum;
//				byte_TranscationDate = ByteUtil.getbytes(byte_TotalData, ByteUtil.int2byte(byte_MessageID).length, byte_TranscationDate.length);
//				byte_TranscationNum = ByteUtil.getint(byte_TotalData,(ByteUtil.int2byte(byte_MessageID).length+byte_TranscationDate.length));
//				String byte_TranscationDateStr = new String(byte_TranscationDate);				
//				System.out.println(" 클라이언트 요청 byte_TranscationDate = "+byte_TranscationDateStr);
//				System.out.println(" 클라이언트 요청 byte_TranscationNum = "+byte_TranscationNum);
//				
//				byte[] byte_ChannelID = new byte[14];
//				byte_ChannelID = ByteUtil.getbytes(byte_TotalData, 
//						(ByteUtil.int2byte(byte_MessageID).length+byte_TranscationDate.length+ByteUtil.int2byte(byte_TranscationNum).length), 
//						byte_ChannelID.length);
//				String byte_ChannelIDStr = new String(byte_ChannelID);	
//				System.out.println(" 클라이언트 요청 byte_ChannelIDStr = "+byte_ChannelIDStr);
//				
//				byte[] byte_Reserved1 = new byte[2];
//				byte_Reserved1 = ByteUtil.getbytes(byte_TotalData, 
//						(ByteUtil.int2byte(byte_MessageID).length+byte_TranscationDate.length+ByteUtil.int2byte(byte_TranscationNum).length+byte_ChannelID.length), 
//						byte_Reserved1.length);
//				String byte_Reserved1Str = new String(byte_Reserved1);	
//				System.out.println(" 클라이언트 요청 byte_Reserved1 = "+byte_Reserved1Str);
//								
//				byte[] byte_DestinationIP = new byte[16];
//				byte_DestinationIP = ByteUtil.getbytes(byte_TotalData, 
//						(ByteUtil.int2byte(byte_MessageID).length+byte_TranscationDate.length+ByteUtil.int2byte(byte_TranscationNum).length+byte_ChannelID.length+byte_Reserved1.length), 
//						byte_DestinationIP.length);
//				String byte_DestinationIPStr = new String(byte_DestinationIP);	
//				System.out.println(" 클라이언트 요청 byte_DestinationIP = "+byte_DestinationIPStr);
//				
//				byte[] byte_Reserved2 = new byte[12];
//				byte_Reserved2 = ByteUtil.getbytes(byte_TotalData, 
//						(ByteUtil.int2byte(byte_MessageID).length+byte_TranscationDate.length+ByteUtil.int2byte(byte_TranscationNum).length+byte_ChannelID.length+byte_Reserved1.length
//						 +byte_DestinationIP.length), 
//						byte_Reserved2.length);
//				String byte_Reserved2Str = new String(byte_Reserved2);	
//				System.out.println(" 클라이언트 요청 byte_Reserved2 = "+byte_Reserved2Str);
//				
//				int byte_DATALength;
//				byte_DATALength = ByteUtil.getint(byte_TotalData,
//						(ByteUtil.int2byte(byte_MessageID).length+byte_TranscationDate.length+ByteUtil.int2byte(byte_TranscationNum).length+byte_ChannelID.length+byte_Reserved1.length
//						 +byte_DestinationIP.length+byte_Reserved2.length));
//				System.out.println(" 클라이언트 요청 byte_DATALength = "+byte_DATALength);
//				
//				byte[] byte_DATA = new byte[byte_DATALength];
//				byte_DATA = ByteUtil.getbytes(byte_TotalData, 
//						(ByteUtil.int2byte(byte_MessageID).length+byte_TranscationDate.length+ByteUtil.int2byte(byte_TranscationNum).length+byte_ChannelID.length+byte_Reserved1.length
//								 +byte_DestinationIP.length+byte_Reserved2.length+ByteUtil.int2byte(byte_DATALength).length), 
//						 byte_DATA.length);
//				String byte_DATAStr = new String(byte_DATA);	
//				System.out.println(" 클라이언트 요청 byte_DATA = "+byte_DATAStr);
//				
//				System.out.println("======================= Body =========================");
//				try {
//					JSONObject jsonObject = new JSONObject(byte_DATAStr);
//					JSONObject results = jsonObject.getJSONObject("request");
//					String msg_id = results.getString("msg_id");
//					String push_id = results.getString("push_id");
//					String service_id = results.getString("service_id");
//					String service_passwd = results.getString("service_passwd");
//					String push_app_id = results.getString("push_app_id");
//					String noti_type = results.getString("noti_type");
//					String app_id = results.getString("app_id");
//					String regist_id = results.getString("regist_id");
//					String noti_contents = results.getString("noti_contents");
//					
//					System.out.println("클라이언트 요청 JSON msg_id = "+msg_id);
//					System.out.println("클라이언트 요청 JSON push_id = "+push_id);
//					System.out.println("클라이언트 요청 JSON service_id = "+service_id);
//					System.out.println("클라이언트 요청 JSON service_passwd = "+service_passwd);
//					System.out.println("클라이언트 요청 JSON push_app_id = "+push_app_id);
//					System.out.println("클라이언트 요청 JSON noti_type = "+noti_type);
//					System.out.println("클라이언트 요청 JSON app_id = "+app_id);
//					System.out.println("클라이언트 요청 JSON regist_id = "+regist_id);
//					System.out.println("클라이언트 요청 JSON noti_contents = "+noti_contents);
//					
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				
////				OutputStream out = pushGWSocket.getOutputStream();
////				DataOutputStream dout = new DataOutputStream(out);
////				String data_response = "Server 성공";
////				dout.writeUTF(data_response);
//				
//				System.out.println("================Response===========");
//				
//				OutputStream out = pushGWSocket.getOutputStream();
//				DataOutputStream dout = new DataOutputStream(out);
//				
//				byte[] byte_MessageID2 = ByteUtil.int2byte(16);
//				byte[] byte_TranscationDate2 = "20120326".getBytes();
//				byte[] byte_TranscationNum2 = ByteUtil.int2byte(2222);
//				byte[] byte_DestinationIP2 = "127.0.0.10000000".getBytes(); //자릿수 비교 하여 공백 바이트값 추가
//				byte[] byte_ChannelID2 = "11111111111111".getBytes();
//				byte[] byte_Reserved12 = "  ".getBytes();
//				byte[] byte_Reserved22 = "            ".getBytes();
//				
//				Json json = new Json("response");
//				Hashtable table1 = new Hashtable();
//				table1.put("msg_id", "PUSH_NOTI");
//				table1.put("push_id", byte_ChannelIDStr);
//				table1.put("status_code", "200");
//				
//				json.setKeys(table1);		  
//				json.addObject(table1);
//				String jsonStr = json.toString();
//				
//				System.out.println(jsonStr);
//				int dataLen = jsonStr.length();
//				System.out.println("dataLen = "+dataLen);
//				
//				byte[] byte_DATALength2 = ByteUtil.int2byte(jsonStr.getBytes().length);
//				byte[] byte_DATA2 = jsonStr.getBytes();
//				
//				System.out.println("byte_DATALength2 = "+ByteUtil.getint(byte_DATALength2,0));
//				
//				byte[] byte_TranscationID = new byte[byte_TranscationDate2.length+byte_TranscationNum2.length];
//				//System.arraycopy(byte_TranscationDate, 0, byte_TranscationID, 0, byte_TranscationDate.length);
//				//System.arraycopy(byte_TranscationNum, 0, byte_TranscationID, byte_TranscationDate.length, byte_TranscationNum.length);
//				ByteUtil.setbytes(byte_TranscationID, 0, byte_TranscationDate2);
//				ByteUtil.setbytes(byte_TranscationID, byte_TranscationDate2.length, byte_TranscationNum2);
//				
//				
//				int byteTotalLen = byte_MessageID2.length
//				+byte_TranscationID.length
//				+byte_DestinationIP2.length
//				+byte_ChannelID2.length
//				+byte_Reserved12.length
//				+byte_Reserved22.length
//				+byte_DATALength2.length
//				+byte_DATA2.length;
//				
//				System.out.println("byteTotalLen="+byteTotalLen);
//				
//				byte[] byteTotalData = new byte[byteTotalLen];
//				
//				ByteUtil.setbytes(byteTotalData, 0, 
//						byte_MessageID2);
//				ByteUtil.setbytes(byteTotalData, byte_MessageID2.length, 
//						byte_TranscationID);
//				ByteUtil.setbytes(byteTotalData, (byte_MessageID2.length+byte_TranscationID.length), 
//						byte_DestinationIP2);
//				ByteUtil.setbytes(byteTotalData, (byte_MessageID2.length+byte_TranscationID.length+byte_DestinationIP2.length), 
//						byte_ChannelID2);
//				ByteUtil.setbytes(byteTotalData, (byte_MessageID2.length+byte_TranscationID.length+byte_DestinationIP2.length+byte_ChannelID2.length), 
//						byte_Reserved12);
//				ByteUtil.setbytes(byteTotalData, (byte_MessageID2.length+byte_TranscationID.length+byte_DestinationIP2.length+byte_ChannelID2.length+byte_Reserved12.length), 
//						byte_Reserved22);
//				ByteUtil.setbytes(byteTotalData, (byte_MessageID2.length+byte_TranscationID.length+byte_DestinationIP2.length+byte_ChannelID2.length+byte_Reserved12.length+byte_Reserved22.length), 
//						byte_DATALength2);
//				ByteUtil.setbytes(byteTotalData, (byte_MessageID2.length+byte_TranscationID.length+byte_DestinationIP2.length+byte_ChannelID2.length+byte_Reserved12.length+byte_Reserved22.length+byte_DATALength2.length), 
//						byte_DATA2);
//				
//				System.out.println("byteTotalData="+byteTotalData);
//				dout.write(byteTotalData);
//				
//				//din.close();
//				//in.close();
//				
//				//out.close();
//				//dout.close();
//				//pushGWserverSoket.close();
//				//System.out.println(getTime()+"서버 종료");
//			}catch(IOException ioe){								
//				//System.out.println(getTime()+"서버 에러");
//			}
//		}
//	}
	
	

	private static String getTime() {
		SimpleDateFormat f = new SimpleDateFormat("[hh:mm:ss]");
		return f.format(new Date());		
	}
}
