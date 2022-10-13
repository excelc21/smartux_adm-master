package com.dmi.smartux.admin.paynow.vo;

public class PaynowReqVO {
	
	private String pt_year;		//파티션 연도 필드(0: 짝수, 1: 홀수)
	private String pt_month;	//파티션 월 필드
	private String tid;			//가맹점 거래 번호
	private String m_type;		//단말 타입 (2: Smart7, 3: tvG, 4: HDTV)
	private String sa_id;		//가입자 정보
	private String mac;			//가입자 MAC 주소
	private String ctn;			//구매자 전화 번호
	private String amount;		//구매 금액
	private String pkg_yn;		//패키지 구매 여부
	private String album_id;	//앨범 ID
	private String album_name;	//앨범명
	private String cat_id;		//컨텐츠 카테고리 ID
	private String app_type;	//어플타입(RABC)
	private String status;		//결제 상태 코드
	private String err_typ;		//오류 타입 (N: Paynow, I: IMCS, P: PG)
	private String err_cd;		//오류 코드
	private String err_msg;		//오류 메시지
	private String reg_dt;		//등록일
	private String mod_dt;		//수정일
	private String coupon_msg;	//쿠폰 발급 정보 메시지
	private String buying_gb;	//구매유형 구분 (N: 일반, S: 선판매)

	public String getPt_year() {
		return pt_year;
	}

	public void setPt_year(String pt_year) {
		this.pt_year = pt_year;
	}

	public String getPt_month() {
		return pt_month;
	}

	public void setPt_month(String pt_month) {
		this.pt_month = pt_month;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getM_type() {
		return m_type;
	}

	public void setM_type(String m_type) {
		this.m_type = m_type;
	}

	public String getSa_id() {
		return sa_id;
	}

	public void setSa_id(String sa_id) {
		this.sa_id = sa_id;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getCtn() {
		return ctn;
	}

	public void setCtn(String ctn) {
		this.ctn = ctn;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getPkg_yn() {
		return pkg_yn;
	}

	public void setPkg_yn(String pkg_yn) {
		this.pkg_yn = pkg_yn;
	}

	public String getAlbum_id() {
		return album_id;
	}

	public void setAlbum_id(String album_id) {
		this.album_id = album_id;
	}

	public String getAlbum_name() {
		return album_name;
	}

	public void setAlbum_name(String album_name) {
		this.album_name = album_name;
	}

	public String getCat_id() {
		return cat_id;
	}

	public void setCat_id(String cat_id) {
		this.cat_id = cat_id;
	}

	public String getApp_type() {
		return app_type;
	}

	public void setApp_type(String app_type) {
		this.app_type = app_type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getErr_typ() {
		return (err_typ == null) ? "" : err_typ;
	}

	public void setErr_typ(String err_typ) {
		this.err_typ = err_typ;
	}

	public String getErr_cd() {
		return (err_cd == null) ? "" : err_cd;
	}

	public void setErr_cd(String err_cd) {
		this.err_cd = err_cd;
	}

	public String getErr_msg() {
		return (err_msg == null) ? "" : err_msg;
	}

	public void setErr_msg(String err_msg) {
		this.err_msg = err_msg;
	}

	public String getReg_dt() {
		return reg_dt;
	}

	public void setReg_dt(String reg_dt) {
		this.reg_dt = reg_dt;
	}

	public String getMod_dt() {
		return mod_dt;
	}

	public void setMod_dt(String mod_dt) {
		this.mod_dt = mod_dt;
	}

	public String getCoupon_msg() {
		return coupon_msg;
	}

	public void setCoupon_msg(String coupon_msg) {
		this.coupon_msg = coupon_msg;
	}

	public String getBuying_gb() {
		return buying_gb;
	}

	public void setBuying_gb(String buying_gb) {
		this.buying_gb = buying_gb;
	}

}
