package com.dmi.smartux.admin.keyword.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmi.smartux.admin.keyword.dao.KeywordDao;
import com.dmi.smartux.admin.keyword.service.KeywordService;
import com.dmi.smartux.admin.keyword.vo.DeleteKeywordProcVo;
import com.dmi.smartux.admin.keyword.vo.InsertKeywordProcVo;
import com.dmi.smartux.admin.keyword.vo.KeywordOrderListResultVo;
import com.dmi.smartux.admin.keyword.vo.KeywordRelationResultVo;
import com.dmi.smartux.admin.keyword.vo.SearchKeywordResultVo;
import com.dmi.smartux.admin.keyword.vo.SelectKeywordDetailVo;
import com.dmi.smartux.admin.keyword.vo.UpdateKeywordOrderProcVo;
import com.dmi.smartux.common.exception.SmartUXException;
import com.dmi.smartux.common.property.SmartUXProperties;

@Service
public class KeywordServiceImpl implements KeywordService {
	
	@Autowired
	KeywordDao dao;
	
	@Override
	public List<SearchKeywordResultVo> getKeywordCateList(String keyword_id) throws Exception {
		return dao.getKeywordCateList(keyword_id);
	}

	@Override
	public SelectKeywordDetailVo getKeywordDetail(String keyword_id) throws Exception {
		return dao.getKeywordDetail(keyword_id);
	}

	@Override
	@Transactional
	public void insertKeywordProc(InsertKeywordProcVo insertkeywordprocVo) throws Exception {
		dao.insertKeyword(insertkeywordprocVo);
		dao.updateVersion();
	}

	@Override
	@Transactional
	public void updateKeywordProc(InsertKeywordProcVo insertkeywordprocVo) throws Exception {
		dao.updateKeyword(insertkeywordprocVo);
		dao.updateVersion();
	}

	@Override
	@Transactional
	public String deleteKeywordProc(DeleteKeywordProcVo deletekeywordprocVo) throws Exception {
		KeywordRelationResultVo keywordrelationresultVo = dao.getKeywordRelation(deletekeywordprocVo.getKeyword_id());
		if(keywordrelationresultVo.getCnt() > 0)
			throw new SmartUXException(SmartUXProperties.getProperty("flag.child.bedata"), SmartUXProperties.getProperty("message.child.bedata"));
		else {
			dao.deleteKeyword(deletekeywordprocVo);
			dao.updateVersion();
		}
		return keywordrelationresultVo.getP_keyword_id();
	}

	@Override
	public List<KeywordOrderListResultVo> getKeywordOrderList(String keyword_id) throws Exception {
		return dao.getKeywordOrderList(keyword_id);
	}

	@Override
	@Transactional
	public void keywordOrderChangeProc(String[] keyword_ids, String mod_id) throws Exception {
		UpdateKeywordOrderProcVo updatekeywordorderprocVo = new UpdateKeywordOrderProcVo();
		updatekeywordorderprocVo.setMod_id(mod_id);
		updatekeywordorderprocVo.setOrd(0);
		for (String keyword_id : keyword_ids) {
			updatekeywordorderprocVo.setKeyword_id(keyword_id);
			updatekeywordorderprocVo.setOrd(updatekeywordorderprocVo.getOrd()+1);
			dao.updateKeywordOrder(updatekeywordorderprocVo);
		}
	}

}
