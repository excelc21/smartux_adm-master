package com.dmi.smartux.admin.hotvod.vo;

import com.dmi.smartux.common.util.GlobalCom;

/**
 * 화제동영상 - 검색 VO
 * @author JKJ
 */
public class HotvodSearchVO {
	//검색
	private String findName;		//검색 구분
	private String findValue;		//검색어
	private String site_id;			//사이트 아이디
	private String content_id;		//컨텐츠 아이디
	private String parent_id;		//상위 컨텐츠 아이디
	private String startDt;			//시작일자
	private String endDt;			//종료일자
	private String sortOrder;		//정렬기준
	private String serviceType;		//검색조건 서비스타입
	private String defaultServiceDec;	// 전체 서비스 10진수 값
	private String multiServiceType;//다중선택 서비스타입	
	private Boolean isLock;			//서비스타입 고정여부
	
	//페이징
	private int pageSize;			//페이징시 게시물의 노출 개수
	private int blockSize;			//한 화면에 노출할 페이지 번호 개수
	private int pageNum;			//현재 페이지 번호
	private int pageCount;			//페이지 번호 전체 사이즈
	private int start_rnum;			//DB(오라클) 페이징 시작 번호
	private int end_rnum;			//DB(오라클) 페이징 끝 번호
	private String validate;		//요청/응답 결과 메세지
	private String delYn;			//노출여부 (원래 삭제여부지만 고도화 때문에 재활용하여 노출여부로 사용함 N:노출 , Y:비노출)
	private String rootId;			//최상위 카테고리 아이디
	private String pcateid[];		//파워크리에이터 관리자 카테고리
	
	public String getFindName() {
		return findName;
	}

	public void setFindName(String findName) {
		this.findName = findName;
	}

	public String getFindValue() {
		return findValue;
	}

	public void setFindValue(String findValue) {
		this.findValue = findValue;
	}
	
	public String getSite_id() {
		return site_id;
	}

	public void setSite_id(String site_id) {
		this.site_id = site_id;
	}

	public String getContent_id() {
		return content_id;
	}

	public void setContent_id(String content_id) {
		this.content_id = content_id;
	}

	public String getParent_id() {
		return parent_id;
	}

	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}
	
	public String getStartDt() {
		return startDt;
	}

	public void setStartDt(String startDt) {
		this.startDt = startDt;
	}

	public String getEndDt() {
		return endDt;
	}

	public void setEndDt(String endDt) {
		this.endDt = endDt;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getBlockSize() {
		return blockSize;
	}

	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getStart_rnum() {
		return start_rnum;
	}

	public void setStart_rnum(int start_rnum) {
		this.start_rnum = start_rnum;
	}

	public int getEnd_rnum() {
		return end_rnum;
	}

	public void setEnd_rnum(int end_rnum) {
		this.end_rnum = end_rnum;
	}

	public String getValidate() {
		return validate;
	}

	public void setValidate(String validate) {
		this.validate = validate;
	}

	public String getDelYn() {
		return delYn;
	}

	public void setDelYn(String delYn) {
		this.delYn = GlobalCom.isNull(delYn, "N");
	}

	public String getRootId() {
		return rootId;
	}

	public void setRootId(String rootId) {
		this.rootId = rootId;
	}

	public String[] getPcateid() {
		return pcateid;
	}

	public void setPcateid(String[] pcateid) {
		this.pcateid = pcateid;
	}
	
	public String getDefaultServiceDec() {
		return GlobalCom.isNull(defaultServiceDec);
	}

	public void setDefaultServiceDec(String defaultServiceDec) {
		this.defaultServiceDec = defaultServiceDec;
	}

	public String getServiceType() {
		return GlobalCom.isNull(serviceType);
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	
	public String getMultiServiceType() {
		return GlobalCom.isNull(multiServiceType);
	}

	public void setMultiServiceType(String multiServiceType) {
		this.multiServiceType = multiServiceType;
	}

	public Boolean getIsLock() {
		return isLock;
	}

	public void setIsLock(Boolean isLock) {
		this.isLock = isLock;
	}
}
