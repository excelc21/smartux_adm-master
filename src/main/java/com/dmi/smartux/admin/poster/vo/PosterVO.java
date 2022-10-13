package com.dmi.smartux.admin.poster.vo;

import com.dmi.smartux.common.vo.BaseVO;

public class PosterVO extends BaseVO{

	private String album_id;		//앨범아이디
	private String album_name;		//앨범명
	private String width_img; 		//
	private String height_img; 		//
	private String created;			//
	private String create_id; 		//
	private String service_type;	//서비스타입(ex. 시니어TV, IPTV)
	
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
	public String getWidth_img() {
		return width_img;
	}
	public void setWidth_img(String width_img) {
		this.width_img = width_img;
	}
	public String getHeight_img() {
		return height_img;
	}
	public void setHeight_img(String height_img) {
		this.height_img = height_img;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public String getCreate_id() {
		return create_id;
	}
	public void setCreate_id(String create_id) {
		this.create_id = create_id;
	}
	public String getService_type() {
		return service_type;
	}
	public void setService_type(String service_type) {
		this.service_type = service_type;
	}
}
