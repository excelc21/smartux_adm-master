package com.dmi.smartux.admin.season.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.dmi.smartux.admin.season.vo.SeasonSearchVo;
import com.dmi.smartux.admin.season.vo.SeasonVo;
import com.dmi.smartux.common.dao.CommonDao;


@Repository("A.SeasonDao")
public class SeasonDao extends CommonDao {
	
	@SuppressWarnings("unchecked")
	public List<SeasonVo> getSeasonList(SeasonSearchVo vo) throws DataAccessException {
		vo.setStart_rnum(vo.getPageNum()*vo.getPageSize()-vo.getPageSize()+1);
		vo.setEnd_rnum(vo.getStart_rnum()+vo.getPageSize()-1);
		return getSqlMapClientTemplate().queryForList("admin_season.getSeasonList", vo);
	}
	
	public int getSeasonListCnt(SeasonSearchVo vo) throws DataAccessException {
		return (Integer)getSqlMapClientTemplate().queryForObject("admin_season.getSeasonListCnt", vo);
	}
	
	public String getSeasonId() throws DataAccessException {
		return (String)getSqlMapClientTemplate().queryForObject("admin_season.getSeasonId");
	}
	
	public void insertProc(SeasonVo vo) throws DataAccessException {
		getSqlMapClientTemplate().insert("admin_season.insertProc", vo);
	}
	
	public SeasonVo getSeasonDetail(String season_id) throws DataAccessException {
		return (SeasonVo)getSqlMapClientTemplate().queryForObject("admin_season.getSeasonDetail", season_id);
	}
	
	@SuppressWarnings("unchecked")
	public List<SeasonVo> getSeasonDetailList(String parent_season_id) throws DataAccessException {
		return (List<SeasonVo>)getSqlMapClientTemplate().queryForList("admin_season.getSeasonDetailList", parent_season_id);
	}
	
	public void updateProc(SeasonVo vo) throws DataAccessException {
		getSqlMapClientTemplate().update("admin_season.updateProc", vo);
	}
	
	public void deleteProc(String season_id, String parentYn) throws DataAccessException {
		Map<String, String> param = new HashMap<String, String>();
		param.put("season_id", season_id);
		param.put("parentYn", parentYn);
		getSqlMapClientTemplate().delete("admin_season.deleteProc", param);
	}
	
	public String getCategoryList(String albumId, String series_yn, String category_gb) throws DataAccessException {
		Map<String, String> param = new HashMap<String, String>();
		param.put("albumId", albumId);
		param.put("series_yn", series_yn);
		param.put("category_gb", category_gb);
		return (String)getSqlMapClientTemplate().queryForObject("admin_season.getCategoryList", param);
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getExceptList() throws DataAccessException{
		return (List<String>)getSqlMapClientTemplate().queryForList("admin_season.getExceptList");
	}
	
	public void insertExcept(String keyword, String keywordYn) throws DataAccessException{
		Map<String, String> param = new HashMap<String, String>();
		param.put("keyword", keyword);
		param.put("keywordYn", keywordYn);
		
		getSqlMapClientTemplate().insert("admin_season.insertExcept", param);
	}
	
	public void deleteExcept() throws DataAccessException{
		getSqlMapClientTemplate().delete("admin_season.deleteExcept");
	}
	
	public void updateCacheTime(String seasonId) throws DataAccessException{
		getSqlMapClientTemplate().update("admin_season.updateCacheTimeProc", seasonId);
	}
	
}
