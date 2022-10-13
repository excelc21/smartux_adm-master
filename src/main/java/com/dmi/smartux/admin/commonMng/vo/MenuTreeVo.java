package com.dmi.smartux.admin.commonMng.vo;

import java.util.List;

import com.dmi.smartux.admin.commonMng.vo.FlatRateVO;

public class MenuTreeVo {

	private String menu_id; /* 메뉴ID */
	private String menu_nm; /* 메뉴명 */
	private String menu_parent_id; /* 상위메뉴ID */
	private String depth; /* 깊이 */
	private String ordered; /* 정렬값 */
	private String data_val; /* 데이터값 */
	private String use_yn; /* 사용유무 */
	private String created; /* 입력일 */
	private String updated; /* 수정일 */
	private String create_id; /* 입력자 */
	private String update_id; /* 수정자 */
	private String menu_type; /* 메뉴타입 */
	private String data_type; /* 데이터타입 */
	private String frame_type_code; /* 프레임타입코드 */
	private String side_menu_yn; /* 사이드메뉴유무 */
	private String leaf_node_yn; /* 리프노드 유무 */
	private String version; /* 버전 */
	private String del_yn; /* 삭제여부 */
	private String menu_img_file; /* 메뉴이미지파일 */
	private String top_img; /*대표이미지파일*/
	private String frame_list_order; /* 프레임 정렬값 */
	private String chck_yn; /* 검수유무 */
	private String chck_version; /* 검수용버전 */
	private String clone_parent_id; /* 상용 메뉴 ID */
	private String os_gb;/*os 구분*/
	private String test_yn; /*검수 사용자 메뉴 여부*/
	private String product_code; /* 월정액상품코드*/
	private List<FlatRateVO> product_code_list; /* 월정액상품코드목록*/
	private String ui_type; /*입접관 UI 타입*/
	private String menu_noti; /* 메뉴 공지사항 */
	private String menu_float; /* 카테고리 하위 서브 메뉴 정렬 위치*/ 

	private String action_flag; //Log 테이블 이관시 action Flag (I:INSERT, U:UPDATE, D:DELETE, N:N/A ) 
	
	private String banner_val; // 메뉴 배너 ID 
	
	public String getAction_flag() {
		return action_flag;
	}

	public void setAction_flag(String action_flag) {
		this.action_flag = action_flag;
	}
	
	public List<FlatRateVO> getProduct_code_list() {
		return product_code_list;
	}

	public void setProduct_code_list(List<FlatRateVO> product_code_list) {
		this.product_code_list = product_code_list;
	}

	public String getProduct_code() {
		return product_code;
	}

	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}

	public String getDel_yn() {
		return del_yn;
	}

	public void setDel_yn(String del_yn) {
		this.del_yn = del_yn;
	}

	public String getClone_parent_id() {
		return clone_parent_id;
	}

	public void setClone_parent_id(String clone_parent_id) {
		this.clone_parent_id = clone_parent_id;
	}

	public String getChck_version() {
		return chck_version;
	}

	public void setChck_version(String chck_version) {
		this.chck_version = chck_version;
	}

	public String getChck_yn() {
		return chck_yn;
	}

	public void setChck_yn(String chck_yn) {
		this.chck_yn = chck_yn;
	}

	public String getMenu_img_file() {
		return menu_img_file;
	}

	public void setMenu_img_file(String menu_img_file) {
		this.menu_img_file = menu_img_file;
	}
	
	public String getTop_img() {
		return top_img;
	}

	public void setTop_img(String top_img) {
		this.top_img = top_img;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getSide_menu_yn() {
		return side_menu_yn;
	}

	public void setSide_menu_yn(String side_menu_yn) {
		this.side_menu_yn = side_menu_yn;
	}

	public String getMenu_id() {
		return menu_id;
	}

	public void setMenu_id(String menu_id) {
		this.menu_id = menu_id;
	}

	public String getMenu_nm() {
		return menu_nm;
	}

	public void setMenu_nm(String menu_nm) {
		this.menu_nm = menu_nm;
	}

	public String getMenu_parent_id() {
		return menu_parent_id;
	}

	public void setMenu_parent_id(String menu_parent_id) {
		this.menu_parent_id = menu_parent_id;
	}

	public String getDepth() {
		return depth;
	}

	public void setDepth(String depth) {
		this.depth = depth;
	}

	public String getOrdered() {
		return ordered;
	}

	public void setOrdered(String ordered) {
		this.ordered = ordered;
	}

	public String getMenu_type() {
		return menu_type;
	}

	public void setMenu_type(String menu_type) {
		this.menu_type = menu_type;
	}

	public String getData_type() {
		return data_type;
	}

	public void setData_type(String data_type) {
		this.data_type = data_type;
	}

	public String getData_val() {
		return data_val;
	}

	public void setData_val(String data_val) {
		this.data_val = data_val;
	}

	public String getUse_yn() {
		return use_yn;
	}

	public void setUse_yn(String use_yn) {
		this.use_yn = use_yn;
	}

	public String getFrame_type_code() {
		return frame_type_code;
	}

	public void setFrame_type_code(String frame_type_code) {
		this.frame_type_code = frame_type_code;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getUpdated() {
		return updated;
	}

	public void setUpdated(String updated) {
		this.updated = updated;
	}

	public String getCreate_id() {
		return create_id;
	}

	public void setCreate_id(String create_id) {
		this.create_id = create_id;
	}

	public String getUpdate_id() {
		return update_id;
	}

	public void setUpdate_id(String update_id) {
		this.update_id = update_id;
	}

	public String getLeaf_node_yn() {
		return leaf_node_yn;
	}

	public void setLeaf_node_yn(String leaf_node_yn) {
		this.leaf_node_yn = leaf_node_yn;
	}

	public String getFrame_list_order() {
		return frame_list_order;
	}

	public void setFrame_list_order(String frame_list_order) {
		this.frame_list_order = frame_list_order;
	}

	public String getOs_gb() {
		return os_gb;
	}

	public void setOs_gb(String os_gb) {
		this.os_gb = os_gb;
	}

	public String getTest_yn() {
		return test_yn;
	}

	public void setTest_yn(String test_yn) {
		this.test_yn = test_yn;
	}

	public String getUi_type() {
		return ui_type;
	}

	public void setUi_type(String ui_type) {
		this.ui_type = ui_type;
	}
	
	public String getMenu_noti() {
		return menu_noti;
	}

	public void setMenu_noti(String menu_noti) {
		this.menu_noti = menu_noti;
	}

	public String getMenu_float() {
		return menu_float;
	}

	public void setMenu_float(String menu_float) {
		this.menu_float = menu_float;
	}

	public String getBanner_val() {
		return banner_val;
	}
	
	public void setBanner_val(String banner_val) {
		this.banner_val = banner_val;
	}
}