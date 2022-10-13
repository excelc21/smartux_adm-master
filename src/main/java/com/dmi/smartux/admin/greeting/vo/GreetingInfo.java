package com.dmi.smartux.admin.greeting.vo;

/**
 * GreetingInfo
 *
 * @author ckkim on 2018-08-29
 */
@SuppressWarnings("unused")
public class GreetingInfo implements Comparable<GreetingInfo> {

    private int order;
    private int greeting_id;

    private String img_url;
    private String bg_image;
    private String bg_image_original_name;
    private String start_point;
    private String end_point;
    private String greeting_txt;
    private String greeting_voice;
    private String greeting_voice_original_name;
    private int date_type;
    private String event_day;

    private String write_id;
    private String insert_date;

    public void setFileInfo(GreetingInfo greetingInfo) {
        this.greeting_voice = greetingInfo.getGreeting_voice();
        this.greeting_voice_original_name = greetingInfo.getGreeting_voice_original_name();
        this.bg_image = greetingInfo.getBg_image();
        this.bg_image_original_name = greetingInfo.getBg_image_original_name();
        this.img_url = greetingInfo.getImg_url();
    }

    public String getBg_image_original_name() {
        return bg_image_original_name;
    }

    public void setBg_image_original_name(String bg_image_original_name) {
        this.bg_image_original_name = bg_image_original_name;
    }

    public String getGreeting_voice_original_name() {
        return greeting_voice_original_name;
    }

    public void setGreeting_voice_original_name(String greeting_voice_original_name) {
        this.greeting_voice_original_name = greeting_voice_original_name;
    }

    public int getGreeting_id() {
        return greeting_id;
    }

    public void setGreeting_id(int greeting_id) {
        this.greeting_id = greeting_id;
    }

    public int getDate_type() {
        return date_type;
    }

    public void setDate_type(int date_type) {
        this.date_type = date_type;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getBg_image() {
        return bg_image;
    }

    public void setBg_image(String bg_image) {
        this.bg_image = bg_image;
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

    public String getGreeting_txt() {
        return greeting_txt;
    }

    public void setGreeting_txt(String greeting_txt) {
        this.greeting_txt = greeting_txt;
    }

    public String getGreeting_voice() {
        return greeting_voice;
    }

    public void setGreeting_voice(String greeting_voice) {
        this.greeting_voice = greeting_voice;
    }

    public String getEvent_day() {
        return event_day;
    }

    public void setEvent_day(String event_day) {
        this.event_day = event_day;
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

    @Override
    public int compareTo(GreetingInfo o) {
        if(order > o.order) {
            return 1;
        } else if( order < o.order) {
            return -1;
        }
        return 0;
    }
}