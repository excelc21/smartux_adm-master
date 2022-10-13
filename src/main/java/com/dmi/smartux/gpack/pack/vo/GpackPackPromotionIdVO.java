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

@XmlRootElement(name="record")
@XmlType(name="GpackPackPromotionIdVO", namespace="com.dmi.smartux.gpack.pack.vo.GpackPackPromotionIdVO", 
		propOrder={"promotion_id"})
public class GpackPackPromotionIdVO extends BaseVO implements Serializable {
	String promotion_id;	//프로모션 ID
	
	public String getPromotion_id() {
		return GlobalCom.isNull(promotion_id);
	}

	@XmlElement(name="promotion_id")
	public void setPromotion_id(String promotion_id) {
		this.promotion_id = promotion_id;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer result = new StringBuffer();
		result.append(getPromotion_id());
		return result.toString();
	}
}
