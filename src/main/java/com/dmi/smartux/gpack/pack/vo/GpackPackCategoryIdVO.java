package com.dmi.smartux.gpack.pack.vo;

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
import com.dmi.smartux.gpack.category.vo.GpackCategoryInfoVO;

@XmlRootElement(name="GpackPackCategoryIdVO")
@XmlType(name="GpackPackCategoryIdVO", namespace="com.dmi.smartux.gpack.pack.vo.GpackPackCategoryIdVO", 
		propOrder={"pcategory_id"})
public class GpackPackCategoryIdVO extends BaseVO implements Serializable {
	String pcategory_id;	//카테고리 ID
	
	public GpackPackCategoryIdVO(){
		
	}
	
	public String getPcategory_id() {
		return GlobalCom.isNull(pcategory_id);
	}

	@XmlElement(name="pcategory_id")
	public void setPcategory_id(String pcategory_id) {
		this.pcategory_id = pcategory_id;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer result = new StringBuffer();
		result.append(getPcategory_id());
		return result.toString();
	}
}
