package com.dmi.smartux.cache.service;

import java.util.List;

import com.dmi.smartux.admin.code.vo.CodeItemVO;

public interface CacheIntervalService {
	public List<CodeItemVO> getCacheIntervalInit() throws Exception;
	
	public long getCacheInterval(String cacheIntervalName) throws Exception;
}
