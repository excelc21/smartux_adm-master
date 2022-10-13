package com.dmi.smartux.sample.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.dmi.smartux.common.dao.CommonDao;
import com.dmi.smartux.sample.vo.EHCacheVO;
import com.dmi.smartux.sample.vo.ResultOne;
import com.dmi.smartux.sample.vo.SampleVO;
import com.dmi.smartux.sample.vo.SampleVO2;
import com.googlecode.ehcache.annotations.Cacheable;

@Repository
public class SampleTableDao extends CommonDao {
	
	private final Log logger = LogFactory.getLog(this.getClass());
	
	/**
	 * 데이터 검색
	 * @param start		검색 시작 인덱스
	 * @param end		검색 종료 인덱스
	 * @return			검색 결과
	 * @throws DataAccessException
	 */
	//@Cacheable(cacheName = "selectCache")
	public List<SampleVO> select(int start, int end) throws DataAccessException {
		Map<String, String> param = new HashMap<String, String>();
		param.put("start", String.valueOf(start));
		param.put("end", String.valueOf(end));
		
		List<SampleVO> result = getSqlMapClientTemplate().queryForList("board.list", param);
		
		for(SampleVO item : result){
			logger.debug("IDX : " + item.getIdx());
			logger.debug("NAME : " +  item.getName());
		}
		return result;
	}
	
	public List<SampleVO2> select2(int start, int end) throws DataAccessException {
		Map<String, String> param = new HashMap<String, String>();
		param.put("start", String.valueOf(start));
		param.put("end", String.valueOf(end));
		
		List<SampleVO2> result = getSqlMapClientTemplate().queryForList("board.list2", param);
		
		for(SampleVO2 item : result){
			logger.debug("IDX : " + item.getIdx());
			logger.debug("NAME : " +  item.getName());
		}
		return result;
	}
	
	public ResultOne oneselect(int idx) throws DataAccessException {
		Map<String, String> param = new HashMap<String, String>();
		param.put("idx", String.valueOf(idx));
		
		ResultOne result = (ResultOne)getSqlMapClientTemplate().queryForObject("board.oneselect", param);
		return result;
	}
	
	public void insert(int idx, String name) throws DataAccessException {
		Map<String, String> param = new HashMap<String, String>();
		param.put("idx", String.valueOf(idx));
		param.put("name", name);
		
		getSqlMapClientTemplate().insert("board.insert", param);
		// int result = getSqlMapClientTemplate().update("board.insert", param);
		
		// logger.debug("insert result : " + result);
	}
	
	public int update(int idx, String name) throws DataAccessException {
		Map<String, String> param = new HashMap<String, String>();
		param.put("idx", String.valueOf(idx));
		param.put("name", name);
		
		int result = getSqlMapClientTemplate().update("board.update", param);
		// int result = getSqlMapClientTemplate().update("board.insert", param);
		
		// logger.debug("insert result : " + result);
		return result;
	}
	
	public int delete(int idx) throws DataAccessException {
		Map<String, String> param = new HashMap<String, String>();
		param.put("idx", String.valueOf(idx));
		
		int result = getSqlMapClientTemplate().delete("board.delete", param);
		// int result = getSqlMapClientTemplate().update("board.insert", param);
		
		// logger.debug("insert result : " + result);
		return result;
	}
	
	@Cacheable(cacheName = "selectCache")
	public List<EHCacheVO> ehcache() throws DataAccessException{
		
		List<EHCacheVO> result = getSqlMapClientTemplate().queryForList("board.ehcache");
		return result;
	}
	
	public List<EHCacheVO> ehcacheCustom(String category) throws DataAccessException{
		Map<String, String> param = new HashMap<String, String>();
		param.put("category", category);
		
		List<EHCacheVO> result = getSqlMapClientTemplate().queryForList("board.ehcacheCustom", param);
		return result;
	}
}
