package com.dmi.smartux.gpack.category.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dmi.smartux.common.dao.CommonMimsDao;
import com.dmi.smartux.gpack.category.vo.GpackCategoryAlbumInfoVO;
import com.dmi.smartux.gpack.category.vo.GpackCategoryInfoVO;
import com.dmi.smartux.gpack.category.vo.GpackCategoryResult;

@Repository
public class GpackCategoryInfoDao extends CommonMimsDao {
	
	/**
	 * 카테고리 정보 조회
	 * @param pack_id
	 * @param category_id
	 * @return
	 * @throws Exception
	 */
	public GpackCategoryResult getPackCategory(String pack_id) {
		
		GpackCategoryResult result = new GpackCategoryResult();
		
		try {
			Map<String, String> param = new HashMap<String, String>();
			param.put("pack_id", 		pack_id);
			
			int cnt = 0;
			List<GpackCategoryInfoVO> categoryResult = new ArrayList<GpackCategoryInfoVO>();
			
			cnt = (Integer) getSqlMapClientTemplate().queryForObject("gpack_info_category.getGpackCategoryCount", param);
			categoryResult = (List<GpackCategoryInfoVO>) getSqlMapClientTemplate().queryForList("gpack_info_category.getGpackCategoryList", param);

			result.setTotal_count(cnt);
			result.setRecordset_depth1(categoryResult);	
		} catch (Exception e) {
			result = null;
			logger.error("Exception[getPackCategory]:" + e.getMessage());
		}
		
		return result;
	}
	
	/**
	 * 앨범 목록 조회 (카테고리 정보 조회 후 for문 돌아서..);
	 * @param pack_id
	 * @param category_id
	 * @return
	 * @throws Exception
	 */
	public List<GpackCategoryAlbumInfoVO> getGpackCategoryAlbumList(String pack_id, String category_id) throws Exception{
		Map<String, String> param = new HashMap<String, String>();
		param.put("pack_id", 		pack_id);
		param.put("category_id", 	category_id);
		List<GpackCategoryAlbumInfoVO> result = new ArrayList<GpackCategoryAlbumInfoVO>();
		result = getSqlMapClientTemplate().queryForList("gpack_info_category.getGpackCategoryAlbumList", param);
		return result;
	}
}
