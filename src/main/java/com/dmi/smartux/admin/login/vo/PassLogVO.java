package com.dmi.smartux.admin.login.vo;

import java.util.List;

import com.dmi.smartux.common.util.GlobalCom;

/**
 * password log 정보 클래스
 * @author YJ KIM
 *
 */
public class PassLogVO{
	
	private int p_id = 0;		//시퀀스
	private String user_id;		//관리자 아이디
	private String user_id_aes;	//관리자 아이디(암호화된 값)
	private String password;		//관리자 비밀번호
	private String reg_date;		//생성일
	
	
	/**
	 * 관리자 아이디
	 * @return the user_id
	 */
	public String getUser_id() {
		return GlobalCom.isNull(user_id);
	}
	/**
	 * 관리자 아이디
	 * @param user_id the user_id to set
	 */
	public void setUser_id(String user_id) {
		this.user_id = GlobalCom.isNull(user_id);
	}
	/**
	 * 관리자 비밀번호
	 * @return the password
	 */
	public String getPassword() {
		return GlobalCom.isNull(password);
	}
	/**
	 * 관리자 비밀번호
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = GlobalCom.isNull(password);
	}
	
	/**
	 * 관리자 아이디(암호화된 값)
	 * @return the user_id_aes
	 */
	public String getUser_id_aes() {
		return user_id_aes;
	}
	/**
	 * 관리자 아이디(암호화된 값)
	 * @param user_id_aes the user_id_aes to set
	 */
	public void setUser_id_aes(String user_id_aes) {
		this.user_id_aes = user_id_aes;
	}
	/**
	 * 시퀀스
	 * @return the p_id
	 */
	public int getP_id() {
		return p_id;
	}
	/**
	 * 시퀀스
	 * @param p_id the p_id to set
	 */
	public void setP_id(int p_id) {
		this.p_id = p_id;
	}
	/**
	 * 등록일
	 * @return the reg_date
	 */
	public String getReg_date() {
		return reg_date;
	}
	/**
	 * 등록일
	 * @param reg_date the reg_date to set
	 */
	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}

	
}
