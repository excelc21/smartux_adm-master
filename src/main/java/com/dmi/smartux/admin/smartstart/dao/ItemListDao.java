package com.dmi.smartux.admin.smartstart.dao;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;


import com.dmi.smartux.admin.code.vo.CodeVO;
import com.dmi.smartux.common.dao.CommonDao;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.configuration.vo.ConfigurationListVO;
import com.dmi.smartux.admin.code.vo.CodeItemVO;


@Repository
public class ItemListDao extends CommonDao {	

	private final Log logger = LogFactory.getLog(this.getClass());
	

	public String getMaxCode() throws Exception{
		String maxcode = (String)(getSqlMapClientTemplate().queryForObject("admin_code.getMaxCode"));
		return GlobalCom.appendLeftZero(maxcode, 3);
	}
	
	public List<CodeItemVO> getItemList() throws Exception {
		// TODO Auto-generated method stub
		Map<String, String> param = new HashMap<String, String>();
		
		//프로퍼티 파일 경로와 프로퍼티 키를 입력받아 해당 프로퍼티 파일 안에 있는 키에 해당하는 값을 읽어온다
		String config_prop_code = SmartUXProperties.getProperty("code.code3");
		logger.debug("config_item_code : " + config_prop_code );
		
		param.put("config_item_code", config_prop_code);		
		
		List<CodeItemVO> result = getSqlMapClientTemplate().queryForList("admin_itemlist.getItemList", param);
	 
		for(CodeItemVO item : result){
			logger.debug("idx : " 				+ item.getOrdered());			
			logger.debug("Item_code : " 		+ item.getItem_code());
			logger.debug("Item_nm : " 			+ item.getItem_nm());			
		}
				
		return result;
	}
	
	public CodeItemVO viewItem(String code, String item_code) throws Exception {
		// TODO Auto-generated method stub
		Map<String, String> param = new HashMap<String, String>();
		
		//String config_prop_code_string = SmartUXProperties.getProperty("smartstart.config_code");
		param.put("code", code);		
		param.put("item_code", item_code);
	
		logger.debug("config_item_code : " + code );
		
		CodeItemVO result = (CodeItemVO)(getSqlMapClientTemplate().queryForObject("admin_itemlist.viewItem", param));
		
		return result;
	}

	public int getCodeItemcodeCnt(String code, String item_code) throws Exception {
		// TODO Auto-generated method stub
		Map<String, String> param = new HashMap<String, String>();
		param.put("code", code);
		param.put("item_code", item_code);
		
		int count = (Integer)(getSqlMapClientTemplate().queryForObject("admin_itemlist.getCodeItemCnt", param));
		
		return count;
	}

	public int getCodeItemnmCnt(String code, String item_nm) throws Exception {
		// TODO Auto-generated method stub
		Map<String, String> param = new HashMap<String, String>();
		param.put("code", code);
		param.put("item_nm", item_nm);
		
		int count = (Integer)(getSqlMapClientTemplate().queryForObject("admin_itemlist.getCodeItemNmCnt", param));
		
		return count;
	}
	
	public int getCodeItemNmAddCnt(String code, String item_code, String item_nm) throws Exception {
		// TODO Auto-generated method stub
		Map<String, String> param = new HashMap<String, String>();
		param.put("code", code);
		param.put("item_code", item_code);
		param.put("item_nm", item_nm);
		
		int count = (Integer)(getSqlMapClientTemplate().queryForObject("admin_itemlist.getCodeItemNmAddCnt", param));
		
		return count;
	}
	
	public int updateCodeItem(String code, String item_code, String newItem_code, String item_nm, String selitemgenre, String selitemtype, String use_yn, String update_id, String ordered, String newOrdered) throws Exception {
		// TODO Auto-generated method stub
		Map<String, String> param = new HashMap<String, String>();
		param.put("code", code);
		param.put("item_code", item_code);
		param.put("newitem_code", newItem_code);
		param.put("item_nm", item_nm);
		param.put("selitemgenre", selitemgenre);
		param.put("selitemtype", selitemtype);
		param.put("use_yn", use_yn);
		param.put("update_id", update_id);
		param.put("ordered", ordered);
		param.put("newOrdered", newOrdered);
		
		int result = getSqlMapClientTemplate().update("admin_itemlist.updateCodeItem", param);
		return result;
	}

	public void insertItem(String code, String item_code, String item_nm,String selitemgenre, String selitemtype, String use_yn, String create_id) throws Exception {
		// TODO Auto-generated method stub
		Map<String, String> param = new HashMap<String, String>();
		param.put("code", code);
		param.put("item_code", item_code);
		param.put("item_nm", item_nm);
		param.put("selitemgenre", selitemgenre);
		param.put("selitemtype", selitemtype);
		param.put("use_yn", use_yn);
		param.put("create_id", create_id);
		param.put("update_id", create_id);
		
		getSqlMapClientTemplate().insert("admin_itemlist.insertItem", param);
	}

	public int deleteItem(String code, String item_code) throws Exception {
		// TODO Auto-generated method stub
		CodeItemVO item = new CodeItemVO();
		item.setCode(code);
		item.setItem_code(item_code);
		
		int result = getSqlMapClientTemplate().delete("admin_itemlist.deleteItem", item);
		
		return result;
	}

	public List<CodeItemVO> getCodeItemList(String code) throws Exception {
		// TODO Auto-generated method stub
		Map<String, String> param = new HashMap<String, String>();
		param.put("code", code);
		
		List<CodeItemVO> result = getSqlMapClientTemplate().queryForList("admin_itemlist.getCodeItemList", param);
		return result;
	}

	public int changeCodeItemOrder(int ordered, String code, String item_code, String update_id) throws Exception {
		CodeItemVO item = new CodeItemVO();
		item.setOrdered(ordered);
		item.setCode(code);
		item.setItem_code(item_code);
		item.setUpdate_id(update_id);
		
		int result = getSqlMapClientTemplate().update("admin_itemlist.changeCodeItemOrder", item);
		return result;
	}

	
}
