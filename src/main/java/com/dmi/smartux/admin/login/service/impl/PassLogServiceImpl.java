package com.dmi.smartux.admin.login.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmi.smartux.admin.abtest.dao.BPASDao;
import com.dmi.smartux.admin.abtest.service.BPASService;
import com.dmi.smartux.admin.abtest.vo.BPASListVo;
import com.dmi.smartux.admin.abtest.vo.BPASSearchVo;
import com.dmi.smartux.admin.abtest.vo.CategoryListVo;
import com.dmi.smartux.admin.abtest.vo.PanelListVo;
import com.dmi.smartux.admin.abtest.vo.TestListVo;
import com.dmi.smartux.admin.login.dao.PassLogDao;
import com.dmi.smartux.admin.login.service.PassLogService;
import com.dmi.smartux.admin.login.vo.LoginVO;
import com.dmi.smartux.admin.login.vo.PassLogVO;


/**
 *  2021.08.23 password log
 *  password log ServiceImpl
 * @author medialog
 *
 */
@Service
public class PassLogServiceImpl implements PassLogService {

	private Logger logger = LoggerFactory.getLogger("abms_paper");
	
	@Autowired
	PassLogDao dao;

	@Override
	public List<PassLogVO> getPassList(PassLogVO searchVo)throws Exception {
		return dao.getPassList(searchVo);
	}

	@Override
	@Transactional
	public void insertPass(PassLogVO vo) throws Exception {
		dao.insertPass(vo);
	}
	
	@Override
	@Transactional
	public void deletePass(int pid) throws Exception {
		dao.deletePass(pid);
	}
	

}
