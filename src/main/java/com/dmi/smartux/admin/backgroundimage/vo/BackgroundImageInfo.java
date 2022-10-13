package com.dmi.smartux.admin.backgroundimage.vo;

/**
 * BackgroundImageInfo
 */
public class BackgroundImageInfo implements Comparable<BackgroundImageInfo> {

    private int order_no;
    private int seq;					
    private String position_fix;
    private String album_id;
    private String album_title;
    private String cat_id;
    private String category_name;
    private String write_id;
    private String insert_date;
    private String album_dup;
   
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public int getOrder_no() {
		return order_no;
	}
	public void setOrder_no(int order_no) {
		this.order_no = order_no;
	}
	public String getPosition_fix() {
		return position_fix;
	}
	public void setPosition_fix(String position_fix) {
		this.position_fix = position_fix;
	}
	public String getAlbum_id() {
		return album_id;
	}
	public void setAlbum_id(String album_id) {
		this.album_id = album_id;
	}
	public String getAlbum_title() {
		return album_title;
	}
	public void setAlbum_title(String album_title) {
		this.album_title = album_title;
	}
	public String getCat_id() {
		return cat_id;
	}
	public void setCat_id(String category_id) {
		this.cat_id = category_id;
	}
	public String getCategory_name() {
		return category_name;
	}
	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}
	public String getWrite_id() {
		return write_id;
	}
	public void setWrite_id(String write_id) {
		this.write_id = write_id;
	}
	public String getInsert_date() {
		return insert_date;
	}
	public void setInsert_date(String insert_date) {
		this.insert_date = insert_date;
	}
	public String getAlbum_dup() {
		return album_dup;
	}
	public void setAlbum_dup(String album_dup) {
		this.album_dup = album_dup;
	}
	
	@Override
    public int compareTo(BackgroundImageInfo o) {
        if(order_no > o.order_no) {
            return 1;
        } else if( order_no < o.order_no) {
            return -1;
        }
        return 0;
    }
}