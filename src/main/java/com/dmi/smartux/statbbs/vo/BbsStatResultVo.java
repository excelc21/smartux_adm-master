package com.dmi.smartux.statbbs.vo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.vo.ResultVO;

@SuppressWarnings("serial")
@XmlRootElement(name = "result")
@XmlType(name="BbsStatResultVo", namespace="com.dmi.smartux.statbbs.vo.BbsStatResultVo"
)
public class BbsStatResultVo  extends ResultVO implements Serializable {

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(getFlag());
		sb.append(GlobalCom.colsep);
		sb.append(getMessage());

		return sb.toString();
	}


}
