package com.dmi.smartux.mainpanel.vo;

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
@XmlType(name="Result", namespace="com.dmi.smartux.mainpanel.vo.MainPanelResult", propOrder={"flag", "message", "total_count", "version", "recordset"})
public class MainPanelResult extends BaseVO implements Serializable {

	String flag = "";
	String message = "";
	
	int total_count = 0;
	String version = "";
	
	List<MainPanelInfoVO> recordset;
	
	public MainPanelResult(){
		
	}
	
	public MainPanelResult(int total_count){
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

	public String getVersion() {
		return GlobalCom.isNull(version);
	}

	@XmlElement(name="version")
	public void setVersion(String version) {
		this.version = version;
	}

	@XmlElementWrapper(name="recordset")
	@XmlElementRefs({
        @XmlElementRef(name="record", type=MainPanelInfoVO.class)
    })
	public List<MainPanelInfoVO> getRecordset() {
		return recordset;
	}

	public void setRecordset(List<MainPanelInfoVO> recordset) {
		this.recordset = recordset;
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
		result.append(getVersion());
		
		if(recordset != null){
			if(recordset.size() != 0){
				StringBuffer record = new StringBuffer();
				record.append(GlobalCom.rssep);
				boolean start = true;
				for(MainPanelInfoVO item : recordset){
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
