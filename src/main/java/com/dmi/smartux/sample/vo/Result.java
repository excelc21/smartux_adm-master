package com.dmi.smartux.sample.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.vo.BaseVO;

// @XmlType(name="클래스명" namespace="풀패키지명.클래스명", propOrder={위에서 아래로 보여줘야 할 멤버 순서들})
// @XmlType에서 name과 namespace는 필수, propOrder는 옵션으로 주지 않았을 경우엔 멤버변수 정의된 순서대로 보여준다
@XmlRootElement(name="result")
@XmlType(name="Result", namespace="com.dmi.smartux.sample.vo.Result", propOrder={"flag", "message", "recordset"})
public class Result<T> extends BaseVO{
	
	String flag;
	String message;
	
	List<T> recordset;

	public String getFlag() {
		return flag;
	}

	@XmlElement(name="flag")
	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getMessage() {
		return message;
	}

	@XmlElement(name="message")
	public void setMessage(String message) {
		this.message = message;
	}

	@XmlElementWrapper(name="recordset")
	@XmlElementRefs({
        @XmlElementRef(name="record", type=SampleVO.class),
        @XmlElementRef(name="record", type=SampleVO2.class),
        @XmlElementRef(name="record", type=EHCacheVO.class)
    })
	public List<T> getRecordset() {
		return recordset;
	}

	public void setRecordset(List<T> recordset) {
		this.recordset = recordset;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer result = new StringBuffer();
		result.append(flag);
		result.append(GlobalCom.colsep);
		result.append(message);
		
		
		if(recordset != null){
			if(recordset.size() != 0){
				StringBuffer record = new StringBuffer();
				record.append(GlobalCom.rssep);
				for(T item : recordset){
					if(record.length() != 0){
						
					}
					record.append(item);
					
				}
				record.append(GlobalCom.rssep);
				result.append(record.toString());
			}
		}
		
		return result.toString();
	}
	
	
	
}
