package com.dmi.smartux.ios_push.vo;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * IOS PushGW 연동 시 요청 VO 클래스
 * @author YJ
 *
 */
@XmlRootElement(name = "request")
public class IOS_PushRequestVO {
	
	private String msg_id;				//msg_id
	private String transaction_id;		//transaction id
	private String service_id;			//app 서비스 ID
	private String service_passwd;		//운영자가 서비스 등록시 입력한 비밀번호
	private String push_app_id;		//설치된 앱 명
	private String noti_type;			//실제 서비스 알림 내역
	private String from_user_id;		//push noti를 요청한 사용자 ID (고객센터번호)
	private String user_id;			//push noti를 수신할 사용자 ID (맥주소 : "."제거, 대문자)
	private String noti_contents;		//notification 내용 (OS에서 출력될 문구)
	private String user_param;			//앱으로 전달할 파라미터 정보
	
	/**
	 * msg_id
	 * @return the msg_id
	 */
	public String getMsg_id() {
		return msg_id;
	}
	/**
	 * msg_id
	 * @param msg_id the msg_id to set
	 */
	public void setMsg_id(String msg_id) {
		this.msg_id = msg_id;
	}
	/**
	 * transaction_id
	 * @return the transaction_id
	 */
	public String getTransaction_id() {
		return transaction_id;
	}
	/**
	 * transaction_id
	 * @param transaction_id the transaction_id to set
	 */
	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}
	/**
	 * app 서비스 아이디
	 * @return the service_id
	 */
	public String getService_id() {
		return service_id;
	}
	/**
	 * app 서비스 아이디
	 * @param service_id the service_id to set
	 */
	public void setService_id(String service_id) {
		this.service_id = service_id;
	}
	/**
	 * 운영자가 서비스 등록시 입력한 비밀번호
	 * @return the service_passwd
	 */
	public String getService_passwd() {
		return service_passwd;
	}
	/**
	 * 운영자가 서비스 등록시 입력한 비밀번호
	 * @param service_passwd the service_passwd to set
	 */
	public void setService_passwd(String service_passwd) {
		this.service_passwd = service_passwd;
	}
	/**
	 * 설치된 앱 명
	 * @return the push_app_id
	 */
	public String getPush_app_id() {
		return push_app_id;
	}
	/**
	 * 설치된 앱 명
	 * @param push_app_id the push_app_id to set
	 */
	public void setPush_app_id(String push_app_id) {
		this.push_app_id = push_app_id;
	}
	/**
	 * 실제 서비스 알림 내역
	 * @return the noti_type
	 */
	public String getNoti_type() {
		return noti_type;
	}
	/**
	 * 실제 서비스 알림 내역
	 * @param noti_type the noti_type to set
	 */
	public void setNoti_type(String noti_type) {
		this.noti_type = noti_type;
	}
	/**
	 * push noti를 요청한 사용자 ID (고객센터번호)
	 * @return the from_user_id
	 */
	public String getFrom_user_id() {
		return from_user_id;
	}
	/**
	 * push noti를 요청한 사용자 ID (고객센터번호)
	 * @param from_user_id the from_user_id to set
	 */
	public void setFrom_user_id(String from_user_id) {
		this.from_user_id = from_user_id;
	}
	/**
	 * push noti를 수신할 사용자 ID (맥주소 : "."제거, 대문자)
	 * @return the user_id
	 */
	public String getUser_id() {
		return user_id;
	}
	/**
	 * push noti를 수신할 사용자 ID (맥주소 : "."제거, 대문자)
	 * @param user_id the user_id to set
	 */
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	/**
	 * notification 내용 (OS에서 출력될 문구)
	 * @return the noti_contents
	 */
	public String getNoti_contents() {
		return noti_contents;
	}
	/**
	 * notification 내용 (OS에서 출력될 문구)
	 * @param noti_contents the noti_contents to set
	 */
	public void setNoti_contents(String noti_contents) {
		this.noti_contents = noti_contents;
	}
	/**
	 * 앱으로 전달할 파라미터 정보
	 * @return the user_param
	 */
	public String getUser_param() {
		return user_param;
	}
	/**
	 * 앱으로 전달할 파라미터 정보
	 * @param user_param the user_param to set
	 */
	public void setUser_param(String user_param) {
		this.user_param = user_param;
	}
	
	
	
}
