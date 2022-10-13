package com.dmi.smartux.admin.gallery.vo;

public class GalleryCateListParamVo {
	
	public String gallery_id;
	public String view_type; //목록화면 : A, 팝업 : P
	public String pop_type; //팝업타입 => A : 카테고리, 컨텐츠 노출 , 그외 : 디렉토리만 노출 
	
	public String getGallery_id() {
		return gallery_id;
	}
	public void setGallery_id(String gallery_id) {
		this.gallery_id = gallery_id;
	}
	public String getView_type() {
		return view_type;
	}
	public void setView_type(String view_type) {
		this.view_type = view_type;
	}
	public String getPop_type() {
		return pop_type;
	}
	public void setPop_type(String pop_type) {
		this.pop_type = pop_type;
	}

}
