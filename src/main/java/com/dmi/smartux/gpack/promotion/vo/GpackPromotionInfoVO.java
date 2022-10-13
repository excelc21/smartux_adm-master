package com.dmi.smartux.gpack.promotion.vo;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.vo.BaseVO;

@XmlRootElement(name="record")
@XmlType(name="GpackPromotionInfoVO", namespace="com.dmi.smartux.gpack.promotion.vo.GpackPromotionInfoVO"
, propOrder={"title","ment", "promotion_id", "mov_total_count", "recordset_mov", "vod_total_count", "recordset_vod"})
@JsonPropertyOrder({"title","ment", "promotion_id", "mov_total_count", "recordset_mov", "vod_total_count", "recordset_vod"}) 
public class GpackPromotionInfoVO extends BaseVO implements Serializable{

	String title = "";
	String ment = "";
	String promotion_id = "";
	String mov_total_count = "";
	
	List<GpackPlaylistVO> recordset_mov;
	
	String vod_total_count = "";
	
	List<GpackPromotionContentsVO> recordset_vod;
	
	public String getTitle() {
		return GlobalCom.isNull(title);
	}
	
	@XmlElement(name="title")
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getMent() {
		return GlobalCom.isNull(ment);
	}
	
	@XmlElement(name="ment")
	public void setMent(String ment) {
		this.ment = ment;
	}
	
	public String getPromotion_id() {
		return GlobalCom.isNull(promotion_id);
	}
	
	@XmlElement(name="promotion_id")
	public void setPromotion_id(String promotion_id) {
		this.promotion_id = promotion_id;
	}
	
	public String getMov_total_count() {
		return GlobalCom.isNull(mov_total_count);
	}
	
	@XmlElement(name="mov_total_count")
	public void setMov_total_count(String mov_total_count) {
		this.mov_total_count = mov_total_count;
	}
	
	public String getVod_total_count() {
		return GlobalCom.isNull(vod_total_count);
	}
	
	@XmlElementWrapper(name="recordset_mov")
	@XmlElementRefs({
        @XmlElementRef(name="record_mov", type=GpackPlaylistVO.class)
    })
	public List<GpackPlaylistVO> getRecordset_mov() {
		return recordset_mov;
	}

	public void setRecordset_mov(List<GpackPlaylistVO> recordset_mov) {
		this.recordset_mov = recordset_mov;
	}
	
	@XmlElement(name="vod_total_count")
	public void setVod_total_count(String vod_total_count) {
		this.vod_total_count = vod_total_count;
	}
	
	@XmlElementWrapper(name="recordset_vod")
	@XmlElementRefs({
        @XmlElementRef(name="record_vod", type=GpackPromotionContentsVO.class)
    })
	public List<GpackPromotionContentsVO> getRecordset_vod() {
		return recordset_vod;
	}

	public void setRecordset_vod(List<GpackPromotionContentsVO> recordset_vod) {
		this.recordset_vod = recordset_vod;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer result = new StringBuffer();
		result.append(getTitle());
		result.append(GlobalCom.colsep);
		result.append(getMent());
		result.append(GlobalCom.colsep);
		result.append(getPromotion_id());
		result.append(GlobalCom.colsep);
		result.append(getMov_total_count());


		
		if(recordset_mov != null){
			if(recordset_mov.size() != 0){
				StringBuffer record = new StringBuffer();
				record.append(GlobalCom.rssep);
				boolean start = true;
				for(GpackPlaylistVO item : recordset_mov){
					if(start){
						start = false;
					}else{
						record.append(GlobalCom.rowsep);
					}
					record.append(item.toString());
				}
				result.append(record.toString());
			}
		}

		result.append(GlobalCom.colsep);
		result.append(getVod_total_count());
		
		if(recordset_vod != null){
			if(recordset_vod.size() != 0){
				StringBuffer record = new StringBuffer();
				record.append(GlobalCom.rssep);
				boolean start = true;
				for(GpackPromotionContentsVO item : recordset_vod){
					if(start){
						start = false;
					}else{
						record.append(GlobalCom.rowsep);
					}
					record.append(item.toString());
				}
				result.append(record.toString());
			}
		}
		
		return result.toString();
	}
}
