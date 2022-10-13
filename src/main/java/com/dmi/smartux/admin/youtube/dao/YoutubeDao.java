package com.dmi.smartux.admin.youtube.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.dmi.smartux.admin.youtube.vo.YoutubeVO;
import com.dmi.smartux.common.dao.CommonMimsDao;

//DB분리로 인해 extends CommonDao -> extends CommonMimsDao로 변경
@Repository
public class YoutubeDao extends CommonMimsDao {
	private final Log logger = LogFactory.getLog(this.getClass());

	
	
	public List<YoutubeVO> getYoutubeList() throws DataAccessException{
		
		List<YoutubeVO> result = getSqlMapClientTemplate().queryForList("youtube.getYoutubeList");

		logger.debug("result.size() = " + result.size());
		
		return result;

	}



	public void setYoutubeInsert(String category, String recommend_text, String write_id) throws DataAccessException{
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("category", category);
		param.put("recommend_text", recommend_text);
		param.put("write_id", write_id);
		
		getSqlMapClientTemplate().insert("youtube.setYoutubeInsert", param);
	}



	public void setYoutubeDelete(String code)  throws DataAccessException{
		Map<String, String> param = new HashMap<String, String>();
		param.put("code", code);
		
		getSqlMapClientTemplate().delete("youtube.setYoutubeDelete", param);
	}



	public void setAllUseUpdate(String useYN) throws DataAccessException{
		Map<String, String> param = new HashMap<String, String>();
		param.put("useYN", useYN);
		
		getSqlMapClientTemplate().update("youtube.setAllUseUpdate", param);
	}



	public void setUseUpdate(String code) throws DataAccessException{
		Map<String, String> param = new HashMap<String, String>();
		param.put("code", code);
		
		getSqlMapClientTemplate().update("youtube.setUseUpdate", param);
	}



	public YoutubeVO getYoutubeData(String code) throws DataAccessException{
		Map<String, String> param = new HashMap<String, String>();
		param.put("code", code);
		
		YoutubeVO vo = (YoutubeVO)getSqlMapClientTemplate().queryForObject("youtube.getYoutubeData", param); 
		
		return vo;
	}



	public void setYoutubeUpdate(String code, String category, String recommend_text, String write_id) throws DataAccessException{
		Map<String, String> param = new HashMap<String, String>();
		param.put("code", code);
		param.put("category", category);
		param.put("recommend_text", recommend_text);
		param.put("write_id", write_id);
		
		getSqlMapClientTemplate().update("youtube.setYoutubeUpdate", param);
	}
	
	
	
}
