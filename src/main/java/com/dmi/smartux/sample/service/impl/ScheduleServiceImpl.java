package com.dmi.smartux.sample.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.dmi.smartux.sample.service.ScheduleService;

@Service("MyScheduleService")
public class ScheduleServiceImpl implements ScheduleService {

	private final Log logger = LogFactory.getLog(this.getClass());
	
	@Override
	public void fixedrateTest() throws Exception {
		// TODO Auto-generated method stub
		logger.debug("fixedrateTest running...");
	}

	@Override
	public void fixeddelayTest() throws Exception {
		// TODO Auto-generated method stub
		logger.debug("fixeddelayTest running...");
	}

	@Override
	public void cronTest() throws Exception {
		// TODO Auto-generated method stub
		logger.debug("cronTest running...");
	}

}
