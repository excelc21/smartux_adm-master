package com.dmi.smartux.mainpanel.vo;

import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.vo.BaseVO;

public class CategoryInfoVO extends BaseVO {
	String pt_id;
	String category_id;
	String album_cnt;

	public String getPt_id() {
		return GlobalCom.isNull(pt_id);
	}
	public void setPt_id(String pt_id) {
		this.pt_id = pt_id;
	}	
	
	public String getCategory_id() {
		return GlobalCom.isNull(category_id);
	}
	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}
	public String getAlbum_cnt() {
		return GlobalCom.isNull(album_cnt);
	}
	public void setAlbum_cnt(String album_cnt) {
		this.album_cnt = album_cnt;
	}
	
	
}
