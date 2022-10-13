package com.dmi.smartux.common.vo;

import com.dmi.smartux.common.util.GlobalCom;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.annotate.JsonSetter;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * 공통으로 사용하는 flag, message VO
 */
@JsonPropertyOrder({"flag", "message"})
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "result")
@XmlType(name = "ResultVO", namespace = "com.dmi.smartux.common.vo", propOrder = {"flag", "message"})
public class ResultVO implements Serializable {
	private String mFlag;
	private String mMessage;

	public String getFlag() {
		return GlobalCom.isEmpty(mFlag) ? "" : mFlag;
	}

	@XmlElement(name = "flag")
	@JsonSetter("flag")
	public void setFlag(String flag) {
		mFlag = flag;
	}

	public String getMessage() {
		return GlobalCom.isEmpty(mMessage) ? "" : mMessage;
	}

	@XmlElement(name = "message")
	@JsonSetter("message")
	public void setMessage(String message) {
		mMessage = message;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getFlag());
		sb.append(GlobalCom.colsep);
		sb.append(getMessage());

		return sb.toString();
	}
}
