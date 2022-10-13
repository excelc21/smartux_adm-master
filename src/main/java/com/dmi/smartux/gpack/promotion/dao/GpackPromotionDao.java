package com.dmi.smartux.gpack.promotion.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import com.dmi.smartux.common.dao.CommonMimsDao;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.gpack.promotion.vo.GpackPlaylistVO;
import com.dmi.smartux.gpack.promotion.vo.GpackPromotionContentsVO;
import com.dmi.smartux.gpack.promotion.vo.GpackPromotionInfoVO;
import com.dmi.smartux.gpack.promotion.vo.GpackPromotionVO;

@Repository
public class GpackPromotionDao extends CommonMimsDao {

	private final Log logger = LogFactory.getLog(this.getClass());
	
	/**
	 * 프로모션 조회
	 * @param pack_id		팩ID
	 * @return				프로모션 조회
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<GpackPromotionInfoVO> getPackPromotion(String pack_id) {
		
		List<GpackPromotionInfoVO> result = new ArrayList<GpackPromotionInfoVO>();
		
		try {
			Map<String, String> param = new HashMap<String, String>();
			param.put("pack_id", pack_id);
			
			// 프로모션 조회
			List<GpackPromotionVO> promotionVOList = getSqlMapClientTemplate().queryForList("gpack_promotion_info.getGpackPromotionList", param);
			
			for(GpackPromotionVO item : promotionVOList){
				
				GpackPromotionInfoVO promotionInfo = new GpackPromotionInfoVO();
				//String pack_id = item.getPack_id();
				String promotion_id = item.getCategory_id();
				String template_type = item.getTemplate_type();
				String category_nm = item.getCategory_nm();
				String category_comment = item.getCategory_comment();
				String promotion_video_gb = item.getPromotion_video_gb();
				String promotion_chnl = item.getPromotion_chnl();
				String promotion_chnl_info = item.getPromotion_chnl_info();
				String auto_yn = item.getAuto_yn();
				int valid_contents_count = 1;
				
				//템플릿별 유효 컨텐츠 개수
				if("TP001".equals(template_type)) {
					valid_contents_count = Integer.parseInt(SmartUXProperties.getProperty("api.gpack.promotion.template1.valid.contents.count"));
				} else {
					valid_contents_count = Integer.parseInt(SmartUXProperties.getProperty("api.gpack.promotion.template2.valid.contents.count"));
				}
				
				// 콘텐츠
				List<GpackPromotionContentsVO> contentsList = new ArrayList<GpackPromotionContentsVO>();
				if("Y".equals(auto_yn)){
					//자동
					param = new HashMap<String, String>();
					param.put("promotion_id", promotion_id);
					param.put("imcs_category_id", item.getImcs_category_id());
					
					contentsList = getSqlMapClientTemplate().queryForList("gpack_promotion_info.getGpackPromotionAlbumList", param);
					
				}else{
					
					// [start]
					/* ************************************************************
					 * CACHE 갱신시, PT_GP_CONTENTS_INFO, PT_GP_CONTENTS_INFO_TEMP
					 * 테이블 내에 ALBUM_ID를 최신회차 ALBUM_ID로 갱신
					 * DEOKSAN(20160610)
					 * ************************************************************ */
					
					// 1) pack_id 를 통해 imcs_category_id 값을 조회
					List<String> imcsCategoryIdList = new ArrayList<String>();
					try {
						imcsCategoryIdList = getSqlMapClientTemplate().queryForList("gpack_promotion_info.getImcsCategoryIdList", pack_id);
					} catch (Exception e) {
						logger.error("Exception[getPackPromotion]imcsCategoryIdList:" + e.getMessage());
					}
					
					try {
						if (imcsCategoryIdList.size()>0) {
							for (String imcsCategoryId : imcsCategoryIdList) {
								
								// 2) 최신회차 album_id 값을 조회한다.
								String latestAlbumId = (String) getSqlMapClientTemplate().queryForObject("gpack_promotion_info.getLatestAlbumId", imcsCategoryId);
								Map<String,String> map = new HashMap<String,String>();
								
								map.put("imcs_category_id", imcsCategoryId);
								map.put("latest_album_id", latestAlbumId);
								
								// 3) 최신회차 album_id 값으로 갱신한다.(PT_GP_CONTENTS_INFO_TEMP Table)
								getSqlMapClientTemplate().update("gpack_promotion_info.updateLatestAlbumIdForTemp", map);
								// 4) 최신회차 album_id 값으로 갱신한다.(PT_GP_CONTENTS_INFO Table)
								getSqlMapClientTemplate().update("gpack_promotion_info.updateLatestAlbumId", map);
							}
						}
					} catch (Exception e) {
						logger.info ("Exception[GpackPromotionDao][updateLatestAlbumId]:" + e.getMessage());
					}
					// [end]
					
					//수동
					contentsList = getSqlMapClientTemplate().queryForList("gpack_promotion_info.getGpackContentsView", item);
					
					String packPath = "";
					if("P0001".equals(pack_id)) {
						packPath = SmartUXProperties.getProperty("gpack.imgupload.tv.dir");
					} else if("P0002".equals(pack_id)) {
						packPath = SmartUXProperties.getProperty("gpack.imgupload.movie.dir");
					} else if("P0003".equals(pack_id)) {
						packPath = SmartUXProperties.getProperty("gpack.imgupload.kids.dir");
					} else {
						packPath = SmartUXProperties.getProperty("gpack.imgupload.spec.dir");
					}
					
					for(GpackPromotionContentsVO vo : contentsList) {
						vo.setImgurl(vo.getImgurl() + packPath + "/");
					}
				}
				
				if(contentsList.size() >= valid_contents_count) {
					
					promotionInfo.setRecordset_vod(contentsList);
					promotionInfo.setVod_total_count(String.valueOf(contentsList.size()));
						
					// 프로모션 기본정보
					promotionInfo.setTitle(category_nm);
					promotionInfo.setMent(category_comment);
					promotionInfo.setPromotion_id(promotion_id);
					
					// 템플릿2인 경우 영상정보
					if("TP002".equals(template_type)) {
						List<GpackPlaylistVO> vedioList = null;
						if("PV001".equals(promotion_video_gb)) {
							vedioList = new ArrayList<GpackPlaylistVO>();
							// 채널
							GpackPlaylistVO vedio = new GpackPlaylistVO();

							vedio.setP_title(promotion_chnl_info);
							vedio.setService_id(promotion_chnl);
							vedio.setAlbumid(null);
							vedio.setP_albumid(null);
							
							vedioList.add(vedio);
							
							promotionInfo.setMov_total_count("1");
						} else if("PV002".equals(promotion_video_gb)){
							vedioList = new ArrayList<GpackPlaylistVO>();
							// 플레이리스트
							vedioList = getSqlMapClientTemplate().queryForList("gpack_promotion_info.getGpackPlaylistList", item);
							
							promotionInfo.setMov_total_count(String.valueOf(vedioList.size()));
						}
						promotionInfo.setRecordset_mov(vedioList);
					}
					
					// 영상이 포함된 프로모션은 최상위에 표시되어야 함
					if("TP002".equals(template_type) && ("PV001".equals(promotion_video_gb) || "PV002".equals(promotion_video_gb))) {
						result.add(0, promotionInfo);
					} else {
						result.add(promotionInfo);
					}
				}
					
			}
			//logger.info("promotion cache : " + result.toString());
		} catch (Exception e) {
			result = null;
			logger.error("Exception[getPackPromotion]:" + e.getMessage());
		}
		
		return result;
	}
}
