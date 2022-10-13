package com.dmi.smartux.notipop.vo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.dmi.smartux.common.vo.BaseVO;

@SuppressWarnings("serial")
@XmlRootElement(name = "models")
@XmlType(name="getNotiPopModelsVo", namespace="com.dmi.smartux.notipop.vo"
	, propOrder={"model"}
)
public class getNotiPopModelsVo extends BaseVO {
	
	private String model = "";

	public String getModel() {
		return model;
	}

	@XmlElement(name="model")
	public void setModel(String model) {
		this.model = model;
	}
	
	@Override
	public String toString() {
		return getModel();
	}

}
