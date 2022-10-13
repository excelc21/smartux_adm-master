package com.dmi.smartux.admin.smartstart.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmi.smartux.admin.code.vo.CodeItemVO;
import com.dmi.smartux.admin.code.vo.CodeVO;
import com.dmi.smartux.admin.smartstart.dao.ItemListDao;
import com.dmi.smartux.admin.smartstart.service.ItemListService;

@Service
public class ItemListServiceImpl implements ItemListService {
	
	private final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	ItemListDao dao;
	
	@Override
	@Transactional(readOnly=true)
	public List<CodeItemVO> getItemList() throws Exception {
		// TODO Auto-generated method stub
		return dao.getItemList();
	}
	
	@Override
	@Transactional(readOnly=true)
	public CodeItemVO viewItem(String code, String item_code) throws Exception {
		// TODO Auto-generated method stub
		return dao.viewItem(code, item_code);
	}
	
	@Override
	@Transactional(readOnly=true)
	public int getCodeItemcodeCnt(String code, String item_code) throws Exception {
		// TODO Auto-generated method stub
		return dao.getCodeItemcodeCnt(code, item_code);
	}

	@Override
	@Transactional(readOnly=true)
	public int getCodeItemnmCnt(String code, String item_nm) throws Exception {
		// TODO Auto-generated method stub
		return dao.getCodeItemnmCnt(code, item_nm);
	}
	
	@Override
	@Transactional
	public int updateCodeItem(String code, String item_code, String newItem_code, String item_nm, String selitemgenre, String selitemtype, String use_yn, String update_id, String ordered, String newOrdered) throws Exception {
		// TODO Auto-generated method stub
		return dao.updateCodeItem(code, item_code, newItem_code, item_nm, selitemgenre, selitemtype, use_yn, update_id , ordered, newOrdered);
	}
	
	@Override
	@Transactional
	public void insertItem(String code, String item_code, String item_nm, String selitemgenre, String selitemtype, String use_yn, String create_id) throws Exception {
		// TODO Auto-generated method stub
		dao.insertItem(code, item_code, item_nm, selitemgenre, selitemtype, use_yn, create_id);
	}
	
	@Override
	@Transactional
	public void deleteItem(String code, String [] item_codes) throws Exception {
		// TODO Auto-generated method stub
		for(String item_code : item_codes){
			dao.deleteItem(code, item_code);
		}		
	}

	@Override
	@Transactional(readOnly=true)
	public List<CodeItemVO> getCodeItemList(String code) throws Exception {
		// TODO Auto-generated method stub
		return dao.getCodeItemList(code);
	}
	
	@Transactional
	@Override
	public void changeCodeItemOrder(String code, String[] item_codes, String update_id) throws Exception {
		// TODO Auto-generated method stub
		int ordered = 1;
		for(String item_code : item_codes){
			dao.changeCodeItemOrder(ordered++, code, item_code, update_id);
		}
	}

	@Override
	@Transactional(readOnly=true)
	public int getCodeItemNmAddCnt(String code, String item_code, String item_nm)
			throws Exception {
		// TODO Auto-generated method stub
		return dao.getCodeItemNmAddCnt(code, item_code, item_nm);
	}
}
