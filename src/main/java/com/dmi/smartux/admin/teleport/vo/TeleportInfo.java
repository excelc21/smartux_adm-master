package com.dmi.smartux.admin.teleport.vo;

/**
 * TeleportInfo
 *
 * @author ckkim on 2018-08-20
 */
public class TeleportInfo implements Comparable<TeleportInfo> {

    private int order;
    private int anchor_id;
    private int anchor_type;
    private String anchor_type_txt;
    private int parent_id;
    private String parent_txt;
    private String panel_id;
    private String paper_code;
    private String paper_name;
    private String anchor_txt;
    private String write_id;
    private String insert_date;

    public String getPaper_name() {
        return paper_name;
    }

    public void setPaper_name(String paper_name) {
        this.paper_name = paper_name;
    }

    public String getInsert_date() {
        return insert_date;
    }

    public void setInsert_date(String insert_date) {
        this.insert_date = insert_date;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getAnchor_id() {
        return anchor_id;
    }

    public void setAnchor_id(int anchor_id) {
        this.anchor_id = anchor_id;
    }

    public int getAnchor_type() {
        return anchor_type;
    }

    public void setAnchor_type(int anchor_type) {
        this.anchor_type = anchor_type;
    }

    public String getAnchor_type_txt() {
        return anchor_type_txt;
    }

    public void setAnchor_type_txt(String anchor_type_txt) {
        this.anchor_type_txt = anchor_type_txt;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public String getParent_txt() {
        return parent_txt;
    }

    public void setParent_txt(String parent_txt) {
        this.parent_txt = parent_txt;
    }

    public String getPanel_id() {
        return panel_id;
    }

    public void setPanel_id(String panel_id) {
        this.panel_id = panel_id;
    }

    public String getPaper_code() {
        return paper_code;
    }

    public void setPaper_code(String paper_code) {
        this.paper_code = paper_code;
    }

    public String getAnchor_txt() {
        return anchor_txt;
    }

    public void setAnchor_txt(String anchor_txt) {
        this.anchor_txt = anchor_txt;
    }

    public String getWrite_id() {
        return write_id;
    }

    public void setWrite_id(String write_id) {
        this.write_id = write_id;
    }

    @Override
    public int compareTo(TeleportInfo o) {
        if(order > o.order) {
            return 1;
        } else if( order < o.order) {
            return -1;
        }
        return 0;
    }
}