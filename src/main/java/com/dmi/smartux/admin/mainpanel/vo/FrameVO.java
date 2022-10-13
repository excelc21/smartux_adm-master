package com.dmi.smartux.admin.mainpanel.vo;

public class FrameVO {
	//FRAME_TYPE_CODE, FRAME_TYPE, FRAME_NM, IMG_FILE, USE_YN
	private String frame_type_code;		//프레임 타입 코드(pk)
	private String frame_type;			//프레임 타입(10:지면, 20:프레임, 30:패널UI타입)
	private String frame_nm;			//프레임 
	private String img_file;			//이미지 파일명
	private String use_yn;				//사용여부
	private String del_yn;				//삭제 여부(추후 사용을 위함)
	private String update_id; 			//업데이트 요청 ID
	private String data_type;			//데이터 타입
	
	public String getUpdate_id() {
		return update_id;
	}
	public void setUpdate_id(String update_id) {
		this.update_id = update_id;
	}
	public String getFrame_type_code() {
		return frame_type_code;
	}
	public void setFrame_type_code(String frame_type_code) {
		this.frame_type_code = frame_type_code;
	}
	public String getFrame_type() {
		return frame_type;
	}
	public void setFrame_type(String frame_type) {
		this.frame_type = frame_type;
	}
	public String getFrame_nm() {
		return frame_nm;
	}
	public void setFrame_nm(String frame_nm) {
		this.frame_nm = frame_nm;
	}
	public String getImg_file() {
		return img_file;
	}
	public void setImg_file(String img_file) {
		this.img_file = img_file;
	}
	public String getUse_yn() {
		return use_yn;
	}
	public void setUse_yn(String use_yn) {
		this.use_yn = use_yn;
	}
	public String getDel_yn() {
		return del_yn;
	}
	public void setDel_yn(String del_yn) {
		this.del_yn = del_yn;
	}
	public String getData_type() {
		return data_type;
	}
	public void setData_type(String data_type) {
		this.data_type = data_type;
	}
}
