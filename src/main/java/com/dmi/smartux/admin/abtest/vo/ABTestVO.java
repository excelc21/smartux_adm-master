package com.dmi.smartux.admin.abtest.vo;

import java.util.ArrayList;
import java.util.List;

import com.dmi.smartux.admin.mainpanel.vo.ViewVO;

public class ABTestVO extends ViewVO{
	private String test_id;	//테스트ID
	private String variation_id;	//VariationID
	private String mtype;	//매핑타입 (P : 패널, G : 주요장르)
	private String mims_id;	//패널ID/주요장르 마스터ID
	private String org_mims_id;	//오리지날 패널ID/주요장르 마스터ID
	private String mod_id;	//수정ID(지면ID,메뉴ID)
	private String mod_name;	//수정명(지면명,메뉴명)
	private String status;	//상태
	private String reg_date;	//등록일
	private String mod_date;	//수정일
	private String test_name;	//테스트명
	private String start_date_time;		//테스트 시작일
	private String end_date_time;	//테스트 종료일
	private String variation_name;	//Variation 명
	private String abms_status;	//ABMS 상태
	private String test_type;	//테스트 타입
	private String abtest_yn;  //ab테스트지면여부
	private String d_mims_id; //대조군
	
	private List<String> variation_list = new ArrayList<String>();	//variation id 목록
	private List<String> titleId_list = new ArrayList<String>();	//title id 목록
	private List<String> testType_list = new ArrayList<String>();	//test type 목록

	private String org_title_id;	// 오리지날 지면 ID
	private String abms_status_name;	//ABMS 상태명
	
	public String getTest_id() {
		return test_id;
	}
	public void setTest_id(String test_id) {
		this.test_id = test_id;
	}
	public String getVariation_id() {
		return variation_id;
	}
	public void setVariation_id(String variation_id) {
		this.variation_id = variation_id;
	}
	public String getMtype() {
		return mtype;
	}
	public void setMtype(String mtype) {
		this.mtype = mtype;
	}
	public String getMims_id() {
		return mims_id;
	}
	public void setMims_id(String mims_id) {
		this.mims_id = mims_id;
	}
	public String getOrg_mims_id() {
		return org_mims_id;
	}
	public void setOrg_mims_id(String org_mims_id) {
		this.org_mims_id = org_mims_id;
	}
	public String getMod_id() {
		return mod_id;
	}
	public void setMod_id(String mod_id) {
		this.mod_id = mod_id;
	}
	public String getMod_name() {
		return mod_name;
	}
	public void setMod_name(String mod_name) {
		this.mod_name = mod_name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getReg_date() {
		return reg_date;
	}
	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}
	public String getMod_date() {
		return mod_date;
	}
	public void setMod_date(String mod_date) {
		this.mod_date = mod_date;
	}
	public String getTest_name() {
		return test_name;
	}
	public void setTest_name(String test_name) {
		this.test_name = test_name;
	}
	public String getStart_date_time() {
		return start_date_time;
	}
	public void setStart_date_time(String start_date_time) {
		this.start_date_time = start_date_time;
	}
	public String getEnd_date_time() {
		return end_date_time;
	}
	public void setEnd_date_time(String end_date_time) {
		this.end_date_time = end_date_time;
	}
	public String getVariation_name() {
		return variation_name;
	}
	public void setVariation_name(String variation_name) {
		this.variation_name = variation_name;
	}
	public String getAbms_status() {
		return abms_status;
	}
	public void setAbms_status(String abms_status) {
		this.abms_status = abms_status;
	}
	public List<String> getVariation_list() {
		return variation_list;
	}
	public void setVariation_list(List<String> variation_list) {
		this.variation_list = variation_list;
	}
	public String getOrg_title_id() {
		return org_title_id;
	}
	public void setOrg_title_id(String org_title_id) {
		this.org_title_id = org_title_id;
	}
	public String getAbms_status_name() {
		return abms_status_name;
	}
	public void setAbms_status_name(String abms_status_name) {
		this.abms_status_name = abms_status_name;
	}
	public String getTest_type() {
		return test_type;
	}
	public void setTest_type(String test_type) {
		this.test_type = test_type;
	}
	public String getAbtest_yn() {
		return abtest_yn;
	}
	public void setAbtest_yn(String abtest_yn) {
		this.abtest_yn = abtest_yn;
	}
	public String getD_mims_id() {
		return d_mims_id;
	}
	public void setD_mims_id(String d_mims_id) {
		this.d_mims_id = d_mims_id;
	}
	public List<String> getTitleId_list() {
		return titleId_list;
	}
	public void setTitleId_list(List<String> titleId_list) {
		this.titleId_list = titleId_list;
	}
	public List<String> getTestType_list() {
		return testType_list;
	}
	public void setTestType_list(List<String> testType_list) {
		this.testType_list = testType_list;
	}
}
