package com.dmi.smartux.admin.noti.vo;

public class NotiListVO {

	private String reg_no; // 게시글 등록번호
	private String bbs_id; // 게시판 아이디
	private String bbs_gbn;// 게시판 구분
	private String is_adt;// 검수 유무
	private String scr_tp;// screan_type

	/*
	 * private int noti_gbn; //공지구분 private String terminal_list;
	 */

	/*
	 * private Map<String,String> notiData; //개별 공지 데이터 private List<Map<String, String>> notiList; //공지 리스트 private
	 * String [] paramListArray; private List<Map<String,String>> paramInfoArray; // 컬럼 이름,컬럼 값,컬럼 타입 private String
	 * paramList;// 파라메타로 받은 컬럼 리스트 private Map<String, String> paramMap; private List<Map<String,String>> boardList;
	 */

	// /////////////////////페이징
	private int pageSize; // 페이징시 게시물의 노출 개수
	private int blockSize; // 한 화면에 노출할 페이지 번호 개수
	private int pageNum; // 현재 페이지 번호
	private int pageCount; // 페이지 번호 전체 사이즈

	private String findName; // 검색 구분
	private String findValue; // 검색어

	private int start_rnum; // DB(오라클) 페이징 시작 번호
	private int end_rnum; // DB(오라클) 페이징 끝 번호
	private String validate; // 요청/응답 결과 메세지

	public String getScr_tp ( ) {
		return scr_tp;
	}

	public void setScr_tp ( String scr_tp ) {
		this.scr_tp = scr_tp;
	}

	public String getIs_adt ( ) {
		return is_adt;
	}

	public void setIs_adt ( String is_adt ) {
		this.is_adt = is_adt;
	}

	public String getBbs_gbn ( ) {
		return bbs_gbn;
	}

	public void setBbs_gbn ( String bbs_gbn ) {
		this.bbs_gbn = bbs_gbn;
	}

	public String getReg_no ( ) {
		return reg_no;
	}

	public void setReg_no ( String reg_no ) {
		this.reg_no = reg_no;
	}

	public String getBbs_id ( ) {
		return bbs_id;
	}

	public void setBbs_id ( String bbs_id ) {
		this.bbs_id = bbs_id;
	}

	public int getPageSize ( ) {
		return pageSize;
	}

	public void setPageSize ( int pageSize ) {
		this.pageSize = pageSize;
	}

	public int getBlockSize ( ) {
		return blockSize;
	}

	public void setBlockSize ( int blockSize ) {
		this.blockSize = blockSize;
	}

	public int getPageNum ( ) {
		return pageNum;
	}

	public void setPageNum ( int pageNum ) {
		this.pageNum = pageNum;
	}

	public int getPageCount ( ) {
		return pageCount;
	}

	public void setPageCount ( int pageCount ) {
		this.pageCount = pageCount;
	}

	public String getFindName ( ) {
		return findName;
	}

	public void setFindName ( String findName ) {
		this.findName = findName;
	}

	public String getFindValue ( ) {
		return findValue;
	}

	public void setFindValue ( String findValue ) {
		this.findValue = findValue;
	}

	public int getStart_rnum ( ) {
		return start_rnum;
	}

	public void setStart_rnum ( int start_rnum ) {
		this.start_rnum = start_rnum;
	}

	public int getEnd_rnum ( ) {
		return end_rnum;
	}

	public void setEnd_rnum ( int end_rnum ) {
		this.end_rnum = end_rnum;
	}

	public String getValidate ( ) {
		return validate;
	}

	public void setValidate ( String validate ) {
		this.validate = validate;
	}
}
