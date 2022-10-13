package com.dmi.smartux.admin.schedule.vo;

import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.vo.BaseVO;

public class ScheduleDetailVO extends BaseVO{
	int schedule_code;
	String category_id;			// category_id
	String album_id;			// album_id
	String album_name;			// album_name
	int ordered;				// ordered
	String series_no;
	
	public String getSeries_no() {
		return series_no;
	}
	public void setSeries_no(String series_no) {
		this.series_no = series_no;
	}
	public int getSchedule_code() {
		return schedule_code;
	}
	public void setSchedule_code(int schedule_code) {
		this.schedule_code = schedule_code;
	}
	public String getCategory_id() {
		return GlobalCom.isNull(category_id);
	}
	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}
	public String getAlbum_id() {
		return GlobalCom.isNull(album_id);
	}
	public void setAlbum_id(String album_id) {
		this.album_id = album_id;
	}
	public String getAlbum_name() {
		return GlobalCom.isNull(album_name);
	}
	public void setAlbum_name(String album_name) {
		this.album_name = album_name;
	}
	public int getOrdered() {
		return ordered;
	}
	public void setOrdered(int ordered) {
		this.ordered = ordered;
	}
	
	
}
