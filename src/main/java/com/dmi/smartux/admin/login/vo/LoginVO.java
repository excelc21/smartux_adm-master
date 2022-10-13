package com.dmi.smartux.admin.login.vo;

import java.util.List;

import com.dmi.smartux.common.util.GlobalCom;

/**
 * 관리자 계정 정보 클래스
 * @author YJ KIM
 *
 */
public class LoginVO{
	
	private String user_id;		//관리자 아이디
	private String user_id_aes;	//관리자 아이디(암호화된 값)
	private String password;		//관리자 비밀번호
	private String name;			//관리자 명
	private String email;			//이메일 주소
	private String memo;			//관리자 메모
	private String created;		//생성일
	private int loginfailcnt = 0;		//로그인 실패 횟수
	private String exp_date;		//로그인 만료일
	private int exp_day = 0;		//로그인 만료일수
	private String user_auth;		//관리자 권한 00:슈퍼관리자 01:일반관리자
	private String salt_key;
	
	
	private List<LoginVO> list;	//리스트 객체
	private int pageSize;			//페이징시 게시물의 노출 개수
	private int blockSize;			//한 화면에 노출할 페이지 번호 개수
	private int pageNum;			//현재 페이지 번호
	private int pageCount;			//페이지 번호 전체 사이즈
	
	private String findName;		//검색 구분
	private String findValue;		//검색어
	
	private int start_rnum;		//DB(오라클) 페이징 시작 번호
	private int end_rnum;			//DB(오라클) 페이징 끝 번호
	
	private String validate;		//요청/응답 결과 메세지
	
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
	 * 관리자 이름
	 * @return the name
	 */
	public String getName() {
		return GlobalCom.isNull(name);
	}
	/**
	 * 관리자 이름
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = GlobalCom.isNull(name);
	}
	/**
	 * 관리자 이메일 주소
	 * @return the email
	 */
	public String getEmail() {
		return GlobalCom.isNull(email);
	}
	/**
	 * 관리자 이메일 주소
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = GlobalCom.isNull(email);
	}
	/**
	 * 관리자 메모
	 * @return the memo
	 */
	public String getMemo() {
		return GlobalCom.isNull(memo);
	}
	/**
	 * 관리자 메모
	 * @param memo the memo to set
	 */
	public void setMemo(String memo) {
		this.memo = GlobalCom.isNull(memo);
	}
	/**
	 * 관리자 계정 생성일
	 * @return the created
	 */
	public String getCreated() {
		return GlobalCom.isNull(created);
	}
	/**
	 * 관리자 계정 생성일
	 * @param created the created to set
	 */
	public void setCreated(String created) {
		this.created = GlobalCom.isNull(created);
	}
	/**
	 * 로그인 실패 횟수
	 * @return the loginfailcnt
	 */
	public int getLoginfailcnt() {
		return GlobalCom.isNumber(loginfailcnt,0);
	}
	/**
	 * 로그인 실패 횟수
	 * @param lofinfailcnt the loginfailcnt to set
	 */
	public void setLoginfailcnt(int loginfailcnt) {
		this.loginfailcnt = GlobalCom.isNumber(loginfailcnt,0);
	}
	/**
	 * 로그인 만료날짜
	 * @return the exp_date
	 */
	public String getExp_date() {
		return GlobalCom.isNull(exp_date);
	}
	/**
	 * 로그인 만료날짜
	 * @param exp_date the exp_date to set
	 */
	public void setExp_date(String exp_date) {
		this.exp_date = GlobalCom.isNull(exp_date);
	}
	/**
	 * 로그인 만료일수
	 * @return the exp_day
	 */
	public int getExp_day() {
		return GlobalCom.isNumber(exp_day,0);
	}
	/**
	 * 로그인 만료일수
	 * @param exp_day the exp_day to set
	 */
	public void setExp_day(int exp_day) {
		this.exp_day = GlobalCom.isNumber(exp_day,0);
	}
	/**
	 * 리스트 객체
	 * @return the list
	 */
	public List<LoginVO> getList() {
		return list;
	}
	/**
	 * 리스트 객체
	 * @param list the list to set
	 */
	public void setList(List<LoginVO> list) {
		this.list = list;
	}
	/**
	 * 페이징시 게시물의 노출 개수
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}
	/**
	 * 페이징시 게시물의 노출 개수
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	/**
	 * 한 화면에 노출할 페이지 번호 개수
	 * @return the blockSize
	 */
	public int getBlockSize() {
		return blockSize;
	}
	/**
	 * 한 화면에 노출할 페이지 번호 개수
	 * @param blockSize the blockSize to set
	 */
	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
	}
	/**
	 * 현재 페이지 번호
	 * @return the pageNum
	 */
	public int getPageNum() {
		return pageNum;
	}
	/**
	 * 현재 페이지 번호
	 * @param pageNum the pageNum to set
	 */
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	/**
	 * 검색 구분
	 * @return the findName
	 */
	public String getFindName() {
		return findName;
	}
	/**
	 * 검색 구분
	 * @param findName the findName to set
	 */
	public void setFindName(String findName) {
		this.findName = findName;
	}
	/**
	 * 검색어
	 * @return the findValue
	 */
	public String getFindValue() {
		return findValue;
	}
	/**
	 * 검색어
	 * @param findValue the findValue to set
	 */
	public void setFindValue(String findValue) {
		this.findValue = findValue;
	}
	/**
	 * DB(오라클) 페이징 시작 번호
	 * @return the start_rnum
	 */
	public int getStart_rnum() {
		return start_rnum;
	}
	/**
	 * DB(오라클) 페이징 시작 번호
	 * @param start_rnum the start_rnum to set
	 */
	public void setStart_rnum(int start_rnum) {
		this.start_rnum = start_rnum;
	}
	/**
	 * DB(오라클) 페이징 끝 번호
	 * @return the end_rnum
	 */
	public int getEnd_rnum() {
		return end_rnum;
	}
	/**
	 * DB(오라클) 페이징 끝 번호
	 * @param end_rnum the end_rnum to set
	 */
	public void setEnd_rnum(int end_rnum) {
		this.end_rnum = end_rnum;
	}
	/**
	 * 페이지 번호 전체 사이즈
	 * @return the pageCount
	 */
	public int getPageCount() {
		return pageCount;
	}
	/**
	 * 페이지 번호 전체 사이즈
	 * @param pageCount the pageCount to set
	 */
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	/**
	 * 요청/응답 결과 메시지
	 * @return the validate
	 */
	public String getValidate() {
		return GlobalCom.isNull(validate);
	}
	/**
	 * 요청/응답 결과 메시지
	 * @param validate the validate to set
	 */
	public void setValidate(String validate) {
		this.validate = GlobalCom.isNull(validate);
	}
	/**
	 * 관리자 권한
	 * @return the user_auth
	 */
	public String getUser_auth() {
		return user_auth;
	}
	/**
	 * 관리자 권한
	 * @param user_auth the user_auth to set
	 */
	public void setUser_auth(String user_auth) {
		this.user_auth = user_auth;
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
	public String getSalt_key() {
		return salt_key;
	}
	public void setSalt_key(String salt_key) {
		this.salt_key = salt_key;
	}
	
	
	
}
