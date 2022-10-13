package com.dmi.smartux.admin.code.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmi.smartux.admin.code.dao.CodeDao;
import com.dmi.smartux.admin.code.service.CodeService;
import com.dmi.smartux.admin.code.vo.CodeItemVO;
import com.dmi.smartux.admin.code.vo.CodeVO;
import com.dmi.smartux.admin.common.dao.CommMimsDao;

@Service
public class CodeServiceImpl implements CodeService {

	@Autowired
	CodeDao dao;
	
	@Autowired
	CommMimsDao mimsDao;
	
	@Override
	@Transactional(readOnly=true)
	public List<CodeVO> getCodeList() throws Exception {
		// TODO Auto-generated method stub
		return dao.getCodeList();
	}

	@Override
	@Transactional(readOnly=true)
	public List<String> getSmartstartList() throws Exception {
		return dao.getSmartstartList();
	}
	
	@Override
	@Transactional(readOnly=true)
	public int getUseSsgbnCnt(String ss_gbn) throws Exception {
		return mimsDao.getUseSsgbnCnt(ss_gbn);
	}
	
	@Override
	@Transactional(readOnly=true)
	public CodeVO viewCode(String code) throws Exception {
		// TODO Auto-generated method stub
		return dao.viewCode(code);
	}

	
	@Override
	@Transactional(readOnly=true)
	public int getCodenmCnt(String code_nm) throws Exception {
		// TODO Auto-generated method stub
		return dao.getCodenmCnt(code_nm);
	}

	@Override
	@Transactional
	public void insertCode(String code, String code_nm, String create_id) throws Exception {
		// TODO Auto-generated method stub
		dao.insertCode(code, code_nm, create_id);
	}

	@Override
	@Transactional
	public int updateCode(String code, String code_nm, String update_id) throws Exception {
		// TODO Auto-generated method stub
		return dao.updateCode(code, code_nm, update_id);
	}

	@Override
	@Transactional
	public void deleteCode(String [] codes) throws Exception {
		// TODO Auto-generated method stub
		for(String code : codes){
			dao.deleteCode(code);
		}
	}
	
	@Override
	@Transactional(readOnly=true)
	public CodeVO checkItemCode(String code) throws Exception {
		// TODO Auto-generated method stub
		return dao.checkItemCode(code);
	}
	
	
	
	// PT_UX_ITEM_CODE
	@Override
	@Transactional(readOnly=true)
	public List<CodeItemVO> getCodeItemList(String code) throws Exception {
		// TODO Auto-generated method stub
		return dao.getCodeItemList(code);
	}

	@Override
	@Transactional(readOnly=true)
	public CodeItemVO viewCodeItem(String code, String item_code) throws Exception {
		// TODO Auto-generated method stub
		return dao.viewCodeItem(code, item_code);
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
	@Transactional(readOnly=true)
	public int getCodeItemssgbnCnt(String ss_gbn) throws Exception {
		return dao.getCodeItemssgbnCnt(ss_gbn);
	}
	
	@Override
	@Transactional
	public void insertCodeItem(String code, String item_code, String item_nm, String ss_gbn, String app_type, String use_yn, String create_id) throws Exception {
		// TODO Auto-generated method stub
		dao.insertCodeItem(code, item_code, item_nm, ss_gbn, app_type, use_yn, create_id);
	}

	@Override
	@Transactional
	public int updateCodeItem(String code, String item_code, String newItem_code, String item_nm, String ss_gbn, String app_type, String use_yn, String update_id) throws Exception {
		// TODO Auto-generated method stub
		return dao.updateCodeItem(code, item_code, newItem_code, item_nm, ss_gbn, app_type, use_yn, update_id);
	}

	@Override
	@Transactional
	public void deleteCodeItem(String code, String [] item_codes) throws Exception {
		// TODO Auto-generated method stub
		for(String item_code : item_codes){
			dao.deleteCodeItem(code, item_code);
		}
		
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
	
	
}
