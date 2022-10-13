package com.dmi.smartux.admin.multipayment.vo;

public class MultiPaymentVo {
	private String pt_year;					//	년(0-1) - 파티션 키	
	private String pt_month;				//	월(1~12) - 파티션 키	
	private String pa_key;					//	복합 결재 키	
	private String pa_type;					//	복합 결재 타입	
	private String pa_status;				//	복합 결재 진행 상태	
	private String pa_reg_dt;				//	복합 결재 요청 최초 신청	
	private String pa_mod_dt;				//	복합 결재 상태 변경일	
	private String pa_flag;					//	복합 결재 성공 여부	
	private String pa_message;				//	복합 결재 관련 메세지	
	private String m_type;					//	결재 요청 단말 타입	
	private String app_type;				//	결재 요청 어플 타입(RABC)	
	private String sa_id;					//	결재 요청 가입자 정보	
	private String stb_mac;					//	결재 요청 가입자 MAC 주소	
	private String prod_cate;				//	상품 카테고리 ID	
	private String prod_id;					//	상품 아이디	
	private String prod_name;				//	상품 이름	
	private String discount_div;			//	복합 결제 할인 : 수단 복합결제 할인 수단 구분 '0’(사용) 또는‘1’(미사용) 로 구성된 네자리 숫자 [쿠폰][멤버쉽][TV포인트][KLU포인트] 순으로 사용 여부 구분 제공	
	private String discount_price;			//	복합 결제 할인 적용 금액 : 복합결제 할인 수단별 할인 적용 금액 [쿠폰][멤버쉽][TV포인트][KLU포인트] 순으로 할인 적용 금액 제공 ex) 0,1000,500,300	
	private String cpn_evt_id;				//	쿠폰 이벤트 ID	
	private String cpn_no;					//	쿠폰 번호	
	private String cpn_offer_type;			//	쿠폰 공제 유형(1:컨텐츠, 2:데이터Free, 3:컨텐츠+데이터Free)
	private String cpn_type;				//  쿠폰타입(1:정액, 2:정률, 3:상품권)
	private int prod_price;				//	상품 금액	
	private int prod_price_sale;			//	상품 할인 금액	
	private int total_price;				//	실제 구매 금액	
	private int datafree_price;			//	데이터 Free 금액	
	private int datafree_price_sale;		//	데이터 Free 할인 금액(혜택금액)	
	private String buy_type;				//	구매 컨텐츠 유형	
	private String buy_gb;					//	구매 유형	
	private String prod_package;			//	상품 패키지 여부	
	private String failover_div;			//  취소 실패에 대한 상태
	
	private String pg_status;
	private String coupon_status;
	private String membership_status;
	private String tvpoint_status;
	private String kbpoint_status;
		
	private String rowno;
	
	public String getPg_status() {
		return pg_status;
	}
	public void setPg_status(String pg_status) {
		this.pg_status = pg_status;
	}
	public String getCoupon_status() {
		return coupon_status;
	}
	public void setCoupon_status(String coupon_status) {
		this.coupon_status = coupon_status;
	}
	public String getMembership_status() {
		return membership_status;
	}
	public void setMembership_status(String membership_status) {
		this.membership_status = membership_status;
	}
	public String getTvpoint_status() {
		return tvpoint_status;
	}
	public void setTvpoint_status(String tvpoint_status) {
		this.tvpoint_status = tvpoint_status;
	}
	public String getKbpoint_status() {
		return kbpoint_status;
	}
	public void setKbpoint_status(String kbpoint_status) {
		this.kbpoint_status = kbpoint_status;
	}
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
	public String getPa_key() {
		return pa_key;
	}
	public void setPa_key(String pa_key) {
		this.pa_key = pa_key;
	}
	public String getPa_type() {
		return pa_type;
	}
	public void setPa_type(String pa_type) {
		this.pa_type = pa_type;
	}
	public String getPa_status() {
		return pa_status;
	}
	public void setPa_status(String pa_status) {
		this.pa_status = pa_status;
	}
	public String getPa_reg_dt() {
		return pa_reg_dt;
	}
	public void setPa_reg_dt(String pa_reg_dt) {
		this.pa_reg_dt = pa_reg_dt;
	}
	public String getPa_mod_dt() {
		return pa_mod_dt;
	}
	public void setPa_mod_dt(String pa_mod_dt) {
		this.pa_mod_dt = pa_mod_dt;
	}
	public String getPa_flag() {
		return pa_flag;
	}
	public void setPa_flag(String pa_flag) {
		this.pa_flag = pa_flag;
	}
	public String getPa_message() {
		return pa_message;
	}
	public void setPa_message(String pa_message) {
		this.pa_message = pa_message;
	}
	public String getM_type() {
		return m_type;
	}
	public void setM_type(String m_type) {
		this.m_type = m_type;
	}
	public String getApp_type() {
		return app_type;
	}
	public void setApp_type(String app_type) {
		this.app_type = app_type;
	}
	public String getSa_id() {
		return sa_id;
	}
	public void setSa_id(String sa_id) {
		this.sa_id = sa_id;
	}
	public String getStb_mac() {
		return stb_mac;
	}
	public void setStb_mac(String stb_mac) {
		this.stb_mac = stb_mac;
	}
	public String getProd_cate() {
		return prod_cate;
	}
	public void setProd_cate(String prod_cate) {
		this.prod_cate = prod_cate;
	}
	public String getProd_id() {
		return prod_id;
	}
	public void setProd_id(String prod_id) {
		this.prod_id = prod_id;
	}
	public String getProd_name() {
		return prod_name;
	}
	public void setProd_name(String prod_name) {
		this.prod_name = prod_name;
	}
	public String getDiscount_div() {
		return discount_div;
	}
	public void setDiscount_div(String discount_div) {
		this.discount_div = discount_div;
	}
	public String getDiscount_price() {
		return discount_price;
	}
	public void setDiscount_price(String discount_price) {
		this.discount_price = discount_price;
	}
	public String getCpn_evt_id() {
		return cpn_evt_id;
	}
	public void setCpn_evt_id(String cpn_evt_id) {
		this.cpn_evt_id = cpn_evt_id;
	}
	public String getCpn_no() {
		return cpn_no;
	}
	public void setCpn_no(String cpn_no) {
		this.cpn_no = cpn_no;
	}
	public String getCpn_offer_type() {
		return cpn_offer_type;
	}
	public void setCpn_offer_type(String cpn_offer_type) {
		this.cpn_offer_type = cpn_offer_type;
	}
	
	public int getProd_price() {
		return prod_price;
	}
	public void setProd_price(int prod_price) {
		this.prod_price = prod_price;
	}
	public int getProd_price_sale() {
		return prod_price_sale;
	}
	public void setProd_price_sale(int prod_price_sale) {
		this.prod_price_sale = prod_price_sale;
	}
	public int getTotal_price() {
		return total_price;
	}
	public void setTotal_price(int total_price) {
		this.total_price = total_price;
	}
	public int getDatafree_price() {
		return datafree_price;
	}
	public void setDatafree_price(int datafree_price) {
		this.datafree_price = datafree_price;
	}
	public int getDatafree_price_sale() {
		return datafree_price_sale;
	}
	public void setDatafree_price_sale(int datafree_price_sale) {
		this.datafree_price_sale = datafree_price_sale;
	}
	public String getBuy_type() {
		return buy_type;
	}
	public void setBuy_type(String buy_type) {
		this.buy_type = buy_type;
	}
	public String getBuy_gb() {
		return buy_gb;
	}
	public void setBuy_gb(String buy_gb) {
		this.buy_gb = buy_gb;
	}
	public String getProd_package() {
		return prod_package;
	}
	public void setProd_package(String prod_package) {
		this.prod_package = prod_package;
	}
	public String getCpn_type() {
		return cpn_type;
	}
	public void setCpn_type(String cpn_type) {
		this.cpn_type = cpn_type;
	}
	public String getFailover_div() {
		return failover_div;
	}
	public void setFailover_div(String failover_div) {
		this.failover_div = failover_div;
	}
	public String getRowno() {
		return rowno;
	}
	public void setRowno(String rowno) {
		this.rowno = rowno;
	}
	
	
}
