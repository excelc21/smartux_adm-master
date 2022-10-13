package com.dmi.smartux.gpack.pack.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dmi.smartux.common.dao.CommonMimsDao;
import com.dmi.smartux.gpack.pack.vo.GpackPackCategoryIdVO;
import com.dmi.smartux.gpack.pack.vo.GpackPackInfoVO;
import com.dmi.smartux.gpack.pack.vo.GpackPackPromotionIdVO;

@Repository
public class GpackPackInfoDao extends CommonMimsDao {

	/**
	 * 팩 정보 가져오기
	 * @param pack_id
	 * @return
	 * @throws Exception
	 */
	public GpackPackInfoVO getGpackPackInfo(String pack_id) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("pack_id", 		pack_id);
		
		GpackPackInfoVO result = new GpackPackInfoVO();
		
		try {
			result = (GpackPackInfoVO) getSqlMapClientTemplate().queryForObject("gpack_info_pack.getGpackPackInfo", param);
			
			List<GpackPackPromotionIdVO> promotionIds = new ArrayList<GpackPackPromotionIdVO>();
			promotionIds = (List<GpackPackPromotionIdVO>) getSqlMapClientTemplate().queryForList("gpack_info_pack.getGpackPromotionId", param);
			
			List<GpackPackCategoryIdVO> categoryIds = new ArrayList<GpackPackCategoryIdVO>();
			categoryIds = (List<GpackPackCategoryIdVO>) getSqlMapClientTemplate().queryForList("gpack_info_pack.getGpackCategoryId", param);
			
			result.setPromotion_id(promotionIds);
			result.setPcategory_id(categoryIds);
			
		} catch (Exception e) {
			result = null;
			logger.error("Exception[getGpackPackInfo]:" + e.getMessage());
		}
		
		return result;
	}
}
