package com.dmi.smartux.admin.login.vo;

import java.util.List;

import com.dmi.smartux.common.util.GlobalCom;

/**
 * password log 정보 클래스
 * @author YJ KIM
 *
 */
public class AdminMenuVO{
	
	private int menu_id = 0;		//아이디
	private String user_auth;	//권한
	private String menu_name;		//메뉴
	
	public int getMenu_id() {
		return menu_id;
	}
	public void setMenu_id(int menu_id) {
		this.menu_id = menu_id;
	}
	public String getUser_auth() {
		return user_auth;
	}
	public void setUser_auth(String user_auth) {
		this.user_auth = user_auth;
	}
	public String getMenu_name() {
		return menu_name;
	}
	public void setMenu_name(String menu_name) {
		this.menu_name = menu_name;
	}

	
	

	
}
