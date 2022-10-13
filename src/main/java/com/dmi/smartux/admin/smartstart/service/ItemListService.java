package com.dmi.smartux.admin.smartstart.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.dmi.smartux.admin.code.vo.CodeItemVO;

/**
 * SmartUX SmartStart 항목정보 목록조회 Service 구현
 * @param model		Model 객체
 * @return			SmartUX SmartStart 항목정보 목록 List<CodeItemVO>
 */
public interface ItemListService {
	public List<CodeItemVO> getItemList() throws Exception;
	
	public List<CodeItemVO> getCodeItemList(String code) throws Exception;

	public CodeItemVO viewItem(String code, String item_code) throws Exception;

	public int updateCodeItem(String code, String item_code, String newItem_code, String item_nm, String selitemgenre, String selitemtype, String use_yn, String update_id, String ordered, String newOrdered) throws Exception;

	public int getCodeItemcodeCnt(String code, String newItem_code) throws Exception;

	public int getCodeItemnmCnt(String code, String newItem_nm) throws Exception;
	
	public void insertItem(String code, String item_code, String item_nm, String selitemgenre, String selitemtype, String use_yn, String create_id) throws Exception;

	public void deleteItem(String code, String [] item_codes) throws Exception;
	
	public void changeCodeItemOrder(String code, String [] item_codes, String update_id) throws Exception;

	public int getCodeItemNmAddCnt(String code, String item_code, String item_nm)  throws Exception;

}