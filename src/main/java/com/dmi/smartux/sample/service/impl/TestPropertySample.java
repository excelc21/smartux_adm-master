package com.dmi.smartux.sample.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TestPropertySample {

	private final Log logger = LogFactory.getLog(this.getClass());
	
	private String ipaddr;

	public void setIpaddr(String ipaddr) {
		this.ipaddr = ipaddr;
	}
	
	public void printipaddr(){
		logger.debug("TestPropertySample.ipaddr : " + ipaddr);
	}
}
