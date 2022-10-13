package com.dmi.smartux.sample.service;

import java.util.List;

import com.dmi.smartux.sample.vo.EHCacheVO;
import com.dmi.smartux.sample.vo.ResultOne;
import com.dmi.smartux.sample.vo.SampleVO;
import com.dmi.smartux.sample.vo.SampleVO2;

public interface SampleTable {
	
	public List<SampleVO> select(int start, int end) throws Exception;
	
	public List<SampleVO2> select2(int start, int end) throws Exception;
	
	public ResultOne oneselect(int idx) throws Exception;
	
	public void insert(int idx, String name) throws Exception;
	
	public void update(int idx, String name) throws Exception;
	
	public void delete(int idx) throws Exception;
	
	public List<EHCacheVO> ehcache() throws Exception;
	
	public List<EHCacheVO> ehcacheCustom(String category, String startnum, String reqcount) throws Exception;
}
