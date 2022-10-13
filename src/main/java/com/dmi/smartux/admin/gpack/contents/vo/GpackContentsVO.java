package com.dmi.smartux.admin.gpack.contents.vo;

import com.dmi.smartux.common.vo.BaseVO;

public class GpackContentsVO extends BaseVO {

	private String pack_id;			//팩 ID
	private String category_id;		//카테고리 ID
	private String contents_id;		//콘텐츠 ID			
	private String contents_nm;		//콘텐츠 제목
	private String movepath_type;	//이동경로 타입 (MT001 : 월정액가입팝업, MT002 : VOD 상세페이지, MT003 : 양방향 어플, MT004 : DAL, MT005 : 채널, MT006 : 카테고리_시리즈, MT007 : 카테고리_상위) 
	private String movepath;		//이동경로
	private String imcs_category_id;	//IMCS 카테고리 ID
	private String album_id;		//IMCS 앨범 ID
	private String img_path;		//포스터 이미지 경로
	private String img_file;		//포스터 이미지 파일명
	private String ordered;			//정렬순번
	private String use_yn;
	private String created;
	private String updated;
	private String create_id;
	private String update_id;
	
	//콘텐츠 이름이 없고, movepath_type이 'MT006' 이면 IMCS 카테고리 제목, movepath_type이 'MT002' 이면 IMCS 콘텐츠 제목
	//이미지 파일이 없고, movepath_type이 'MT006' 이면 IMCS 카테고리 이미지, movepath_type이 'MT002' 이면 IMCS 앨범 이미지
	private String imcs_text;		//IMCS 제목
	private String imcs_img_path;	//IMCS 이미지 경로
	private String imcs_img_file;	//IMCS 이미지 파일 
	
	private String chnl_info;		//채널정보
	private String product_info;	//월정액상품정보
	private String dal_type;		//DAL 타입
	
	
	public String getPack_id() {
		return pack_id;
	}
	public void setPack_id(String pack_id) {
		this.pack_id = pack_id;
	}
	public String getCategory_id() {
		return category_id;
	}
	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}
	public String getContents_id() {
		return contents_id;
	}
	public void setContents_id(String contents_id) {
		this.contents_id = contents_id;
	}
	public String getContents_nm() {
		return contents_nm;
	}
	public void setContents_nm(String contents_nm) {
		this.contents_nm = contents_nm;
	}
	public String getMovepath_type() {
		return movepath_type;
	}
	public void setMovepath_type(String movepath_type) {
		this.movepath_type = movepath_type;
	}
	
	public String getMovepath() {
		return movepath;
	}
	public void setMovepath(String movepath) {
		this.movepath = movepath;
	}
	public String getImcs_category_id() {
		return imcs_category_id;
	}
	public void setImcs_category_id(String imcs_category_id) {
		this.imcs_category_id = imcs_category_id;
	}
	public String getAlbum_id() {
		return album_id;
	}
	public void setAlbum_id(String album_id) {
		this.album_id = album_id;
	}
	public String getImg_path() {
		return img_path;
	}
	public void setImg_path(String img_path) {
		this.img_path = img_path;
	}
	public String getImg_file() {
		return img_file;
	}
	public void setImg_file(String img_file) {
		this.img_file = img_file;
	}
	public String getOrdered() {
		return ordered;
	}
	public void setOrdered(String ordered) {
		this.ordered = ordered;
	}
	public String getUse_yn() {
		return use_yn;
	}
	public void setUse_yn(String use_yn) {
		this.use_yn = use_yn;
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

	public String getImcs_text() {
		return imcs_text;
	}
	public void setImcs_text(String imcs_text) {
		this.imcs_text = imcs_text;
	}
	public String getImcs_img_path() {
		return imcs_img_path;
	}
	public void setImcs_img_path(String imcs_img_path) {
		this.imcs_img_path = imcs_img_path;
	}
	public String getImcs_img_file() {
		return imcs_img_file;
	}
	public void setImcs_img_file(String imcs_img_file) {
		this.imcs_img_file = imcs_img_file;
	}
	public String getChnl_info() {
		return chnl_info;
	}
	public void setChnl_info(String chnl_info) {
		this.chnl_info = chnl_info;
	}
	public String getProduct_info() {
		return product_info;
	}
	public void setProduct_info(String product_info) {
		this.product_info = product_info;
	}
	public String getDal_type() {
		return dal_type;
	}
	public void setDal_type(String dal_type) {
		this.dal_type = dal_type;
	}
}
