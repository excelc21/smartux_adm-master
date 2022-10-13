/**
 * Class Name : GpackProductVO.java
 * Description : 
 *  월정액 상품 정보 VO class
 *
 * Modification Information
 *  
 * 수정일         수정자         수정내용
 * ----------     --------       ---------------------------
 * 2013.03.21     kimhahn		신규
 *    
 * @author kimhahn
 * @since 2014.03.21
 * @version 1.0
 */
package com.dmi.smartux.admin.gpack.event.vo;

import java.util.List;


public class GpackProductVO {
	
	/** 상품 ID */
	private String product_id = "";
	
	/** 가격 */
	private String price = "";
	
	/** 상품명 */
	private String product_name = "";
	
	/** 검색조건 : 상품명 */
	private String srch_product_name = "";
	
	private List<GpackProductVO> list;	//리스트 객체
	private int pageSize;			//페이징시 게시물의 노출 개수
	private int blockSize;			//한 화면에 노출할 페이지 번호 개수
	private int pageNum;			//현재 페이지 번호
	private int pageCount;			//페이지 번호 전체 사이즈
	
	/**
	 * @return the product_id
	 */
	public String getProduct_id() {
		return product_id;
	}

	/**
	 * @param product_id the product_id to set
	 */
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	/**
	 * @return the price
	 */
	public String getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(String price) {
		this.price = price;
	}

	/**
	 * @return the product_name
	 */
	public String getProduct_name() {
		return product_name;
	}

	/**
	 * @param product_name the product_name to set
	 */
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	/**
	 * @return the srch_product_name
	 */
	public String getSrch_product_name() {
		return srch_product_name;
	}

	/**
	 * @param srch_product_name the srch_product_name to set
	 */
	public void setSrch_product_name(String srch_product_name) {
		this.srch_product_name = srch_product_name;
	}

	/**
	 * @return the list
	 */
	public List<GpackProductVO> getList() {
		return list;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(List<GpackProductVO> list) {
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
