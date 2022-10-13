package com.dmi.smartux.gpack.category.vo;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.vo.BaseVO;

@XmlRootElement(name="record")
@XmlType(name="GpackCategoryInfoVO", namespace="com.dmi.smartux.gpack.category.vo.GpackCategoryInfoVO"
, propOrder={"title", "ment", "pcategory_id", "total_count","recordset_depth2"})
public class GpackCategoryInfoVO extends BaseVO implements Serializable{

	String title = "";
	String ment = "";
	String pcategory_id = "";
	int total_count = 0;
	
	List<GpackCategoryAlbumInfoVO> recordset_depth2;

	public GpackCategoryInfoVO(){
		
	}
	
	public GpackCategoryInfoVO(int total_count){
		this.total_count = total_count;
	}

	public String getTitle() {
		return GlobalCom.isNull(title);
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMent() {
		return GlobalCom.isNull(ment);
	}

	public void setMent(String ment) {
		this.ment = ment;
	}

	public String getPcategory_id() {
		return GlobalCom.isNull(pcategory_id);
	}

	public void setPcategory_id(String pcategory_id) {
		this.pcategory_id = pcategory_id;
	}

	public int getTotal_count() {
		return total_count;
	}

	public void setTotal_count(int total_count) {
		this.total_count = total_count;
	}

	@XmlElementWrapper(name="recordset_depth2")
	@XmlElementRefs({
        @XmlElementRef(name="record", type=GpackCategoryInfoVO.class)
    })
	public List<GpackCategoryAlbumInfoVO> getRecordset_depth2() {
		return recordset_depth2;
	}

	public void setRecordset_depth2(List<GpackCategoryAlbumInfoVO> recordset_depth2) {
		this.recordset_depth2 = recordset_depth2;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer result = new StringBuffer();
		result.append(getTitle());
		result.append(GlobalCom.colsep);
		result.append(getMent());
		result.append(GlobalCom.colsep);
		result.append(getPcategory_id());
		result.append(GlobalCom.colsep);
		
		if(recordset_depth2 != null){
			if(recordset_depth2.size() != 0){
				StringBuffer record = new StringBuffer();
				record.append(GlobalCom.rssep);
				boolean start = true;
				for(GpackCategoryAlbumInfoVO item : recordset_depth2){
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
