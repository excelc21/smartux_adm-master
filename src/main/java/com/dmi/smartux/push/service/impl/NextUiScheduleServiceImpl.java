package com.dmi.smartux.push.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmi.smartux.admin.common.dao.CommHmimsDao;
import com.dmi.smartux.admin.common.dao.CommMimsDao;
import com.dmi.smartux.push.dao.NextUiPushDao;
import com.dmi.smartux.push.service.NextUiScheduleService;
import com.dmi.smartux.push.vo.NextUiPushContentVO;
import com.dmi.smartux.push.vo.NextUiPushUserParamVO;
import com.dmi.smartux.push.vo.NextUiPushUserVO;

@Service
public class NextUiScheduleServiceImpl implements NextUiScheduleService {
//	private final Log logger = LogFactory.getLog("NextUiPushSchedule");

	@Autowired
	NextUiPushDao dao;
	
	@Autowired
	CommHmimsDao hmimsDao;
	
	@Autowired
	CommMimsDao mimsDao;

	@Override
	public List<NextUiPushContentVO> getNextUiPushContentList() throws Exception {
		return dao.getNextUiPushContentList();
	}
	
	@Override
	public int getNextUiPushUserCount(NextUiPushContentVO param) throws Exception {
		return mimsDao.getNextUiPushUserCount(param);
	}

	@Override
	public List<NextUiPushUserVO> getNextUiPushUserList(NextUiPushUserParamVO param) throws Exception {
		return mimsDao.getNextUiPushUserList(param);
	}
	
	@Override
	public void updatePushComplete(NextUiPushContentVO pushcompleteVo) throws Exception {
		hmimsDao.updatePushComplete(pushcompleteVo);
	}
}