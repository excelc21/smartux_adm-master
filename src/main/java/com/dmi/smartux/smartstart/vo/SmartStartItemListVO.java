package com.dmi.smartux.smartstart.vo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.dmi.smartux.common.vo.BaseVO;
import com.dmi.smartux.common.util.GlobalCom;

//@XmlType(name="클래스명" namespace="풀패키지명.클래스명", propOrder={위에서 아래로 보여줘야 할 멤버 순서들})
//@XmlType에서 name과 namespace는 필수, propOrder는 옵션으로 주지 않았을 경우엔 멤버변수 정의된 순서대로 보여준다
@XmlRootElement(name = "record") 	// vo의 XmlRootElement name속성은 record로 통일
@XmlType(name="SmartStartItemListVO", namespace="com.dmi.smartux.smartstart.vo.SmartStartItemListVO", propOrder={"item_type","item_title","genre_code","ui_type","bg_img_file","description","order_seq"})
public class SmartStartItemListVO extends BaseVO  implements Serializable{
   
	String item_type;
	String item_title;	
	String genre_code; 	
	String ui_type;
	String bg_img_file;
	String description;
	String order_seq;		

	@XmlElement(name="item_type")
	public void setItem_type(String item_type) {
		this.item_type = item_type;
	}		
	
	public String getItem_type() {
		return GlobalCom.isNull(item_type);
	}	
	
	@XmlElement(name="item_title")
	public void setItem_title(String item_title) {
		this.item_title = item_title;
	}	
	
	public String getItem_title() {
		return GlobalCom.isNull(item_title);
	}		
	
	@XmlElement(name="genre_code")
	public void setGenre_code(String genre_code) {
		this.genre_code = genre_code;
	}
	
	public String getGenre_code() {
		return GlobalCom.isNull(genre_code);
	}	
	
	@XmlElement(name="ui_type")
	public void setUi_type(String ui_type) {
		this.ui_type = ui_type;
	}
	
	public String getUi_type() {
		return GlobalCom.isNull(ui_type);
	}
	
	@XmlElement(name="bg_img_file")
	public void setBg_img_file(String bg_img_file) {
		this.bg_img_file = bg_img_file;
	}
	
	public String getBg_img_file() {
		return GlobalCom.isNull(bg_img_file);
	}
	
	@XmlElement(name="description")
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return GlobalCom.isNull(description);
	}
		
	@XmlElement(name="order_seq")
	public void setOrder_seq(String order_seq) {
		this.order_seq = order_seq;
	}
	
	public String getOrder_seq() {
		return GlobalCom.isNull(order_seq);
	}
		
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();	
		sb.append(getItem_type());
		sb.append(GlobalCom.colsep);
		sb.append(getItem_title());		
		sb.append(GlobalCom.colsep);
		sb.append(getGenre_code());		
		sb.append(GlobalCom.colsep);
		sb.append(getUi_type());
		sb.append(GlobalCom.colsep);
		sb.append(getBg_img_file());
		sb.append(GlobalCom.colsep);
		sb.append(getDescription());		
		sb.append(GlobalCom.colsep);		
		sb.append(getOrder_seq());	
		
		return sb.toString();
	}	
}