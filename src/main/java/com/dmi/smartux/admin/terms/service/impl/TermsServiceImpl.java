package com.dmi.smartux.admin.terms.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmi.smartux.admin.terms.dao.TermsDao;
import com.dmi.smartux.admin.terms.service.TermsService;
import com.dmi.smartux.admin.terms.vo.TermsListVo;
import com.dmi.smartux.admin.terms.vo.TermsVo;

@Service
public class TermsServiceImpl implements TermsService {

	private final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	TermsDao dao;
	
	@Override
	@Transactional
	public void insertTermsProc(TermsVo termsVo) throws Exception {
		
		dao.insertTermsProc(termsVo);

		//LOG
		dao.insertTermsProcLog(termsVo);
	}

	@Override
	public int getTermsListCnt(TermsListVo termslistVo) throws Exception {
		return dao.getTermsListCnt(termslistVo);
	}

	@Override
	public List<TermsVo> getTermsList(TermsListVo termslistVo) throws Exception {
		
		List<TermsVo> result=null;
		result=dao.getTermsList(termslistVo);
		
		return result;
	}

	@Override
	@Transactional
	public void deleteTermsProc(String terms_id, String id, String ip) throws Exception {
		
        String[] req_no_arr = terms_id.split(",");
        TermsVo logVo = new TermsVo();
        
        for(String temp_no : req_no_arr){
        	
        	String[] req_no_arr2 = temp_no.split("\\|");
        	
			logVo.setService_type(req_no_arr2[0].trim());
			logVo.setService_gbn(req_no_arr2[1].trim());
			logVo.setTerms_gbn(req_no_arr2[2].trim());
			logVo.setAct_gbn("D");
			logVo.setAct_id(id);
			logVo.setAct_ip(ip);

			dao.termsUpdateLog(logVo);
			dao.deleteTermsProc(logVo);
        }
        
	}
	
	@Override
	public TermsVo getTermsView(TermsVo termsVo) throws Exception {
		TermsVo result=null;		 			 
		result=dao.getTermsView(termsVo);
			
		return result;
	}
	
	@Override
	@Transactional
	public void updateTermsProc(TermsVo termsVo) throws Exception {

		dao.updateTermsProc(termsVo);

		//LOG
		dao.termsUpdateLog(termsVo);
	}

}
