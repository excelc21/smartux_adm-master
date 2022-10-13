package com.dmi.smartux.gpack.pack.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.vo.BaseVO;

@XmlRootElement(name="result")
@XmlType(name="GpackPackInfoVO", namespace="com.dmi.smartux.gpack.pack.vo.GpackPackInfoVO", 
		propOrder={"flag", "message", "version", "category_id", "title", "type", "p_total_count", "promotion_id", "c_total_count", "pcategory_id"})
public class GpackPackInfoVO extends BaseVO implements Serializable {

	String flag = "";
	String message = "";

	String version = "";		//팩 버전
	String category_id = "";	//imcs 카테고리 id
	String title = "";			//팩 제목
	String type = "";			//템플릿 타입
	int p_total_count = 0;		//프로모션 개수
	List<GpackPackPromotionIdVO> promotion_id;	//프로모션 ID[]
	int c_total_count = 0;		//카테고리 편성 개수
	List<GpackPackCategoryIdVO> pcategory_id;	//카테고리 ID[]

	public GpackPackInfoVO(){
		
	}

	public String getFlag() {
		return GlobalCom.isNull(flag);
	}

	@XmlElement(name="flag")
	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getMessage() {
		return GlobalCom.isNull(message);
	}

	@XmlElement(name="message")
	public void setMessage(String message) {
		this.message = message;
	}

	public String getVersion() {
		return GlobalCom.isNull(version);
	}

	@XmlElement(name="version")
	public void setVersion(String version) {
		this.version = version;
	}

	public String getCategory_id() {
		return GlobalCom.isNull(category_id);
	}

	@XmlElement(name="category_id")
	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}

	public String getTitle() {
		return GlobalCom.isNull(title);
	}

	@XmlElement(name="title")
	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return GlobalCom.isNull(type);
	}

	@XmlElement(name="type")
	public void setType(String type) {
		this.type = type;
	}

	public int getP_total_count() {
		return p_total_count;
	}

	@XmlElement(name="p_total_count")
	public void setP_total_count(int p_total_count) {
		this.p_total_count = p_total_count;
	}


	@XmlElementWrapper(name="recordset")
	@XmlElementRefs({
        @XmlElementRef(name="record")
    })
	public List<GpackPackPromotionIdVO> getPromotion_id() {
		if (promotion_id==null) {
			promotion_id = new ArrayList<GpackPackPromotionIdVO>();
		}
		return promotion_id;
	}

	public void setPromotion_id(List<GpackPackPromotionIdVO> promotion_id) {
		this.promotion_id = promotion_id;
	}

	public int getC_total_count() {
		return c_total_count;
	}

	@XmlElement(name="c_total_count")
	public void setC_total_count(int c_total_count) {
		this.c_total_count = c_total_count;
	}


	@XmlElementWrapper(name="recordset")
	@XmlElementRefs({
        @XmlElementRef(name="record")
    })
	public List<GpackPackCategoryIdVO> getPcategory_id() {
		if (pcategory_id==null) {
			pcategory_id = new ArrayList<GpackPackCategoryIdVO>();
		}
		return pcategory_id;
	}

	public void setPcategory_id(List<GpackPackCategoryIdVO> pcategory_id) {
		this.pcategory_id = pcategory_id;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer result = new StringBuffer();
		result.append(getFlag());
		result.append(GlobalCom.colsep);
		result.append(getMessage());
		result.append(GlobalCom.colsep);
		result.append(getVersion());
		result.append(GlobalCom.colsep);
		result.append(getCategory_id());
		result.append(GlobalCom.colsep);
		result.append(getTitle());
		result.append(GlobalCom.colsep);
		result.append(getType());
		result.append(GlobalCom.colsep);
		result.append(getP_total_count());

		if(promotion_id != null){
			if(promotion_id.size() != 0){
				StringBuffer record = new StringBuffer();
				record.append(GlobalCom.rssep);
				boolean start = true;
				for(GpackPackPromotionIdVO item : promotion_id){
					if(start){
						start = false;
					}else{
						record.append(GlobalCom.rowsep);
					}
					record.append(item);
				}
				result.append(record.toString());
			}
		}

		result.append(GlobalCom.colsep);
		result.append(getC_total_count());

		if(pcategory_id != null){
			if(promotion_id.size() != 0){
				StringBuffer record = new StringBuffer();
				record.append(GlobalCom.rssep);
				boolean start = true;
				for(GpackPackCategoryIdVO item : pcategory_id){
					if(start){
						start = false;
					}else{
						record.append(GlobalCom.rowsep);
					}
					record.append(item);
				}
				result.append(record.toString());
			}
		}
		return result.toString();
	}
}
