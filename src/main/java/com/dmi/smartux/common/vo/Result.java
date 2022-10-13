package com.dmi.smartux.common.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.dmi.smartux.bonbang.vo.ReservedProgramVO;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.mainpanel.vo.AlbumInfoVO;
import com.dmi.smartux.noticeinfo.vo.CacheNoticeInfoListVo;
import com.dmi.smartux.sample.vo.EHCacheVO;
import com.dmi.smartux.smartepg.vo.RealRatingInfoThemeVO;
import com.dmi.smartux.smartepg.vo.RealRatingInfoVO;
import com.dmi.smartux.smartepg.vo.ThemeInfoVO;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

//@XmlType(name="클래스명" namespace="풀패키지명.클래스명", propOrder={위에서 아래로 보여줘야 할 멤버 순서들})
//@XmlType에서 name과 namespace는 필수, propOrder는 옵션으로 주지 않았을 경우엔 멤버변수 정의된 순서대로 보여준다

@JsonPropertyOrder({"flag", "message", "total_count", "recordset"})
@XmlRootElement(name="result")
@XmlType(name="Result", namespace="com.dmi.smartux.common.vo.Result", propOrder={"flag", "message", "total_count", "recordset"})
public class Result<T> extends BaseVO{
	
	String flag = "";
	String message = "";
	
	int total_count = 0;
	
	List<T> recordset;
	
	public Result(){
		
	}
	
	public Result(int total_count){
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

	@XmlElementWrapper(name="recordset")
	@XmlElementRefs({
        @XmlElementRef(name="record", type=ThemeInfoVO.class)
        , @XmlElementRef(name="record", type=EHCacheVO.class)
        , @XmlElementRef(name="record", type=RealRatingInfoThemeVO.class)
        , @XmlElementRef(name="record", type=RealRatingInfoVO.class)
		, @XmlElementRef(name="record", type=ReservedProgramVO.class)
        , @XmlElementRef(name="record", type=AlbumInfoVO.class)
        , @XmlElementRef(name="record", type=CacheNoticeInfoListVo.class)
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
		result.append(GlobalCom.colsep);
		result.append(total_count);
		
		if(recordset != null){
			if(recordset.size() != 0){
				StringBuffer record = new StringBuffer();
				record.append(GlobalCom.rssep);
				boolean start = true;
				for(T item : recordset){
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

