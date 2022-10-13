package com.dmi.smartux.admin.poster.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.dmi.smartux.admin.poster.vo.PosterSearchVO;
import com.dmi.smartux.admin.poster.vo.PosterVO;
import com.dmi.smartux.common.dao.CommonDao;

@Repository
public class PosterAdminDao extends CommonDao {

	/**
	 * 포스터 정보 테이블에서 포스터 목록 조회
	 * @return 조회된 포스터 목록
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PosterVO> getPosterList(PosterSearchVO posterSearchVo) throws Exception {
		posterSearchVo.setStart_rnum(posterSearchVo.getPageNum() * posterSearchVo.getPageSize() - posterSearchVo.getPageSize() + 1);
		posterSearchVo.setEnd_rnum(posterSearchVo.getStart_rnum() + posterSearchVo.getPageSize() - 1);
		
		List<PosterVO> result = getSqlMapClientTemplate().queryForList("admin_poster.getPosterList", posterSearchVo);
		return result;
	}
	
	/**
	 * 포스터 정보 테이블에서 포스터 목록 COUNT
	 * @return 조회된 포스터 목록 COUNT
	 * @throws Exception
	 */
	public int getPosterListCount(PosterSearchVO posterSearchVo) throws DataAccessException {
        return (Integer) getSqlMapClientTemplate().queryForObject("admin_poster.getPosterListCount", posterSearchVo);
    }
	
	/**
	 * 포스터 정보 테이블에서 포스터 조회
	 * @return 조회된 포스터
	 * @throws Exception
	 */
	public PosterVO getSelectPoster(String albumId, String serviceType) throws Exception {
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("albumId", albumId);
		paramMap.put("serviceType", serviceType);
		
		PosterVO result = (PosterVO) getSqlMapClientTemplate().queryForObject("admin_poster.getSelectPoster", paramMap);
		return result;
	}
	
	/**
	 * 포스터 정보 테이블에 포스터 등록
	 * @param serviceType
	 * @param albumId
	 * @param widthImg
	 * @param heightImg
	 * @param createId
	 * @throws Exception
	 */
	public void insertPoster(String serviceType, String albumId, String widthImg, String heightImg, String createId) throws Exception{
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("serviceType", serviceType);
		paramMap.put("albumId", albumId);
		paramMap.put("widthImg", widthImg);
		paramMap.put("heightImg", heightImg);
		paramMap.put("createId", createId);
		getSqlMapClientTemplate().insert("admin_poster.insertPoster", paramMap);
	}
	
	/**
	 * 포스터 정보 테이블에 포스터 수정
	 * @param serviceType
	 * @param albumId
	 * @param widthImg
	 * @param heightImg
	 * @param createId
	 * @throws Exception
	 */
	public void updatePoster(String serviceType, String albumId, String widthImg, String heightImg, String createId) throws Exception{
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("serviceType", serviceType);
		paramMap.put("albumId", albumId);
		paramMap.put("widthImg", widthImg);
		paramMap.put("heightImg", heightImg);
		paramMap.put("createId", createId);
		getSqlMapClientTemplate().update("admin_poster.updatePoster", paramMap);
	}
	
	/**
	 * 포스터 정보 테이블에 포스터 삭제
	 * @param albumId
	 * @param serviceType
	 * @return
	 * @throws Exception
	 */
	public int deletePoster(String albumId, String serviceType) throws Exception {
		Map<String, String> param = new HashMap<String, String>();
		param.put("albumId", albumId);
		param.put("serviceType", serviceType);
		int result = getSqlMapClientTemplate().delete("admin_poster.deletePoster", param);
		return result;
	}
}
