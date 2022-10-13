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

@XmlRootElement(name="result")
@XmlType(name="Result", namespace="com.dmi.smartux.gpack.category.vo.GpackCategoryResult", propOrder={"flag", "message", "total_count", "recordset_depth1"})
public class GpackCategoryResult extends BaseVO implements Serializable {

	String flag = "";
	String message = "";
	
	int total_count = 0;
	
	List<GpackCategoryInfoVO> recordset_depth1;
	
	public GpackCategoryResult(){
		
	}
	
	public GpackCategoryResult(int total_count){
		this.total_count = total_count;
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
	
	public int getTotal_count() {
		return total_count;
	}

	@XmlElement(name="total_count")
	public void setTotal_count(int total_count) {
		this.total_count = total_count;
	}
	
	@XmlElementWrapper(name="recordset_depth1")
	@XmlElementRefs({
        @XmlElementRef(name="record", type=GpackCategoryInfoVO.class)
    })
	public List<GpackCategoryInfoVO> getRecordset_depth1() {
		return recordset_depth1;
	}

	public void setRecordset_depth1(List<GpackCategoryInfoVO> recordset_depth1) {
		this.recordset_depth1 = recordset_depth1;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer result = new StringBuffer();
		result.append(getFlag());
		result.append(GlobalCom.colsep);
		result.append(getMessage());
		result.append(GlobalCom.colsep);
		result.append(getTotal_count());
		result.append(GlobalCom.colsep);
		
		if(recordset_depth1 != null){
			if(recordset_depth1.size() != 0){
				StringBuffer record = new StringBuffer();
				record.append(GlobalCom.rssep);
				boolean start = true;
				for(GpackCategoryInfoVO item : recordset_depth1){
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
