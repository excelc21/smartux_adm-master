package com.dmi.smartux.admin.lifemessage.vo;

/**
 * LifeMessageInfo
 */
public class LifeMessageInfo implements Comparable<LifeMessageInfo> {

    private int order;
    private String cont_msg;
    private String cont_type;
    private String cont_type_name;
    private String display_time;
    private String start_point;
    private String end_point;
    private String writeId;
    private String insertDate;

    
   
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public String getCont_msg() {
		return cont_msg;
	}
	public void setCont_msg(String cont_msg) {
		this.cont_msg = cont_msg;
	}
	public String getCont_type() {
		return cont_type;
	}
	public void setCont_type(String cont_type) {
		this.cont_type = cont_type;
	}
	public String getCont_type_name() {
		return cont_type_name;
	}
	public void setCont_type_name(String cont_type_name) {
		this.cont_type_name = cont_type_name;
	}
	public String getDisplay_time() {
		return display_time;
	}
	public void setDisplay_time(String display_time) {
		this.display_time = display_time;
	}
	public String getStart_point() {
		return start_point;
	}
	public void setStart_point(String start_point) {
		this.start_point = start_point;
	}
	public String getEnd_point() {
		return end_point;
	}
	public void setEnd_point(String end_point) {
		this.end_point = end_point;
	}
	public String getWriteId() {
		return writeId;
	}
	public void setWriteId(String writeId) {
		this.writeId = writeId;
	}
	public String getInsertDate() {
		return insertDate;
	}
	public void setInsertDate(String insertDate) {
		this.insertDate = insertDate;
	}

	
	@Override
    public int compareTo(LifeMessageInfo o) {
        if(order > o.order) {
            return 1;
        } else if( order < o.order) {
            return -1;
        }
        return 0;
    }
}