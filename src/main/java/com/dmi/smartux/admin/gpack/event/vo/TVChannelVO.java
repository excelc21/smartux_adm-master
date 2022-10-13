/**
 * Class Name : TVChannelVO.java
 * Description : 
 *  채널 정보 VO class
 *
 * Modification Information
 *  
 * 수정일         수정자         수정내용
 * ----------     --------       ---------------------------
 * 2013.03.14     kimhahn		신규
 *    
 * @author kimhahn
 * @since 2014.03.14
 * @version 1.0
 */
package com.dmi.smartux.admin.gpack.event.vo;

import java.util.List;


public class TVChannelVO {
	
	/** 채널 서비스 ID */
	private String service_id = "";
	
	/** 채널 번호 */
	private String channel_no = "";
	
	/** 채널명 */
	private String channel_name = "";
	
	/** 채널 ID */
	private String channel_id = "";
	
	/** 검색조건 : 채널명 */
	private String srch_channel_name = "";
	
	private List<TVChannelVO> list;	//리스트 객체
	private int pageSize;			//페이징시 게시물의 노출 개수
	private int blockSize;			//한 화면에 노출할 페이지 번호 개수
	private int pageNum;			//현재 페이지 번호
	private int pageCount;			//페이지 번호 전체 사이즈
	//private int start_rnum;		//DB(오라클) 페이징 시작 번호
	//private int end_rnum;			//DB(오라클) 페이징 끝 번호
	
	/**
	 * @return the service_id
	 */
	public String getService_id() {
		return service_id;
	}

	/**
	 * @param service_id the service_id to set
	 */
	public void setService_id(String service_id) {
		this.service_id = service_id;
	}

	/**
	 * @return the channel_no
	 */
	public String getChannel_no() {
		return channel_no;
	}

	/**
	 * @param channel_no the channel_no to set
	 */
	public void setChannel_no(String channel_no) {
		this.channel_no = channel_no;
	}

	/**
	 * @return the channel_name
	 */
	public String getChannel_name() {
		return channel_name;
	}

	/**
	 * @param channel_name the channel_name to set
	 */
	public void setChannel_name(String channel_name) {
		this.channel_name = channel_name;
	}
	
	/**
	 * @return the service_id
	 */
	public String getChannel_id() {
		return channel_id;
	}

	/**
	 * @param service_id the service_id to set
	 */
	public void setChannel_id(String channel_id) {
		this.channel_id = channel_id;
	}

	/**
	 * @return the srch_channel_name
	 */
	public String getSrch_channel_name() {
		return srch_channel_name;
	}

	/**
	 * @param srch_channel_name the srch_channel_name to set
	 */
	public void setSrch_channel_name(String srch_channel_name) {
		this.srch_channel_name = srch_channel_name;
	}

	/**
	 * @return the list
	 */
	public List<TVChannelVO> getList() {
		return list;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(List<TVChannelVO> list) {
		this.list = list;
	}

	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @return the blockSize
	 */
	public int getBlockSize() {
		return blockSize;
	}

	/**
	 * @param blockSize the blockSize to set
	 */
	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
	}

	/**
	 * @return the pageNum
	 */
	public int getPageNum() {
		return pageNum;
	}

	/**
	 * @param pageNum the pageNum to set
	 */
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	/**
	 * @return the pageCount
	 */
	public int getPageCount() {
		return pageCount;
	}

	/**
	 * @param pageCount the pageCount to set
	 */
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	/**
	 * @return the start_rnum
	 */
	public int getStart_rnum() {
		return getPageNum() * getPageSize() - getPageSize() + 1;
	}

	/**
	 * @return the end_rnum
	 */
	public int getEnd_rnum() {
		return getStart_rnum() + getPageSize() - 1;
	}
}
