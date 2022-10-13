package com.dmi.smartux.noti.vo;

/**
 * 게시판 별 단말종류에 따른 new 게시물 갯수 와 전체 게시물 정보의VO 다.
 * 
 * @author jch82@naver.com
 */
public class NotiEtcInfo {
	/**
	 * 단말에 따른 "New" 게시물 수
	 */
	private int newCount = 0;
	/**
	 * 단말에 따른 전체 게시물 수
	 */
	private int totalCount = 0;

	/**
	 * @return 단말에 따른 "New" 게시물 수
	 */
	public int getNewCount ( ) {
		return newCount;
	}

	/**
	 * @param newCount 단말에 따른 "New" 게시물 수
	 */
	public void setNewCount ( int newCount ) {
		this.newCount = newCount;
	}

	/**
	 * @return 단말에 따른 전체 게시물 수
	 */
	public int getTotalCount ( ) {
		return totalCount;
	}

	/**
	 * @param totalCount 단말에 따른 전체 게시물 수
	 */
	public void setTotalCount ( int totalCount ) {
		this.totalCount = totalCount;
	}

	/**
	 * 단말에 따른 "New" 게시물 수의 값을 +1 한다.
	 */
	public void plusNewCount ( ) {
		newCount++;
	}

	/**
	 * 단말에 따른 전체 게시물 수의 값을 +1 한다.
	 */
	public void plusTotalCount ( ) {
		totalCount++;
	}
}
