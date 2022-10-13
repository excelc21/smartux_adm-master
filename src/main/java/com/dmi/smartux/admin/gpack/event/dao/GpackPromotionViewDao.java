/**
 * Class Name : GpackPromotionDao.java
 * Description : 
 *  프로모션 관리 구현을 위한 dao class
 *
 * Modification Information
 *  
 * 수정일         수정자         수정내용
 * ----------     --------       ---------------------------
 * 2013.03.14     kimhahn		신규
 *    
 * @author kimhahn
 * @since 2014.03.14
 * @version 1.0
 */
package com.dmi.smartux.admin.gpack.event.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dmi.smartux.admin.gpack.event.vo.GpackPlaylistVO;
import com.dmi.smartux.admin.gpack.event.vo.GpackProductVO;
import com.dmi.smartux.admin.gpack.event.vo.GpackPromotionContentsVO;
import com.dmi.smartux.admin.gpack.event.vo.GpackPromotionVO;
import com.dmi.smartux.admin.gpack.event.vo.TVChannelVO;
import com.dmi.smartux.common.dao.CommonMimsDao;

@Repository
public class GpackPromotionViewDao extends CommonMimsDao {

	/**
	 * 프로모션 목록 정보 조회
	 * @param promotionVO			조회 조건 정보
	 * @return 조회된 프로모션 목록 정보
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<GpackPromotionVO> getGpackPromotionList(GpackPromotionVO promotionVO) throws Exception {
		return (List<GpackPromotionVO>)(getSqlMapClientTemplate().queryForList("gpack_promotion.getGpackPromotionList", promotionVO));
	}
	
	/**
	 * 프로모션 목록 정보 조회(상용)
	 * @param promotionVO			조회 조건 정보
	 * @return 조회된 프로모션 목록 정보
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<GpackPromotionVO> getGpackPromotionListBiz(GpackPromotionVO promotionVO) throws Exception {
		return (List<GpackPromotionVO>)(getSqlMapClientTemplate().queryForList("gpack_promotion.getGpackPromotionListBiz", promotionVO));
	}
	
	/**
	 * 프로모션 상세 정보 조회
	 * @param promotionVO			조회 조건 정보
	 * @return 조회된 프로모션 상세 정보
	 * @throws Exception
	 */
	public GpackPromotionVO getGpackPromotionDetail(GpackPromotionVO promotionVO) throws Exception {
		return (GpackPromotionVO)(getSqlMapClientTemplate().queryForObject("gpack_promotion.getGpackPromotionDetail", promotionVO));
	}
	
	/**
	 * 영상 프로모션 개수 조회
	 * @param promotionVO			조회 조건 정보
	 * @return 영상 프로모션 개수
	 * @throws Exception
	 */
	public int countGprackVideoPromotion(GpackPromotionVO promotionVO) throws Exception {
		return (Integer)(getSqlMapClientTemplate().queryForObject("gpack_promotion.countGprackVideoPromotion", promotionVO));
	}
	
	/**
	 * TV 채널 목록 조회
	 * @param channelVO		조회 조건 정보
	 * @return 조회된 TV 채널 목록
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<TVChannelVO> getTVChannelList(TVChannelVO channelVO) throws Exception {
		return (List<TVChannelVO>)(getSqlMapClientTemplate().queryForList("gpack_promotion.getTVChannelList", channelVO));
	}
	
	/**
	 * TV 채널 카운트
	 * @param channelVO		조회 조건 정보
	 * @return 조회된 TV 채널 개수
	 * @throws Exception
	 */
	public int countTVChannelList(TVChannelVO channelVO) throws Exception {
		return (Integer)(getSqlMapClientTemplate().queryForObject("gpack_promotion.countTVChannelList", channelVO));
	}
	
	/**
	 * 월정액 상품 목록 조회
	 * @param productVO		조회 조건 정보
	 * @return 조회된 월정액 상품 목록
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<GpackProductVO> getGpackProductList(GpackProductVO productVO) throws Exception {
		return (List<GpackProductVO>)(getSqlMapClientTemplate().queryForList("gpack_promotion.getProductList", productVO));
	}
	
	/**
	 * 월정액 상품 카운트
	 * @param productVO		조회 조건 정보
	 * @return 조회된 월정액 상품 개수
	 * @throws Exception
	 */
	public int countGpackProductList(GpackProductVO productVO) throws Exception {
		return (Integer)(getSqlMapClientTemplate().queryForObject("gpack_promotion.countProductList", productVO));
	}
	
	/**
	 * 플레이리스트 정보 목록 조회
	 * @param playlistVO		조회 조건 정보
	 * @return 조회된 플레이리스트 정보 목록
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<GpackPlaylistVO> getGpackPlaylistList(GpackPlaylistVO playlistVO) throws Exception {
		return (List<GpackPlaylistVO>)(getSqlMapClientTemplate().queryForList("gpack_promotion.getGpackPlaylistList", playlistVO));
	}
	
	/**
	 * 플레이리스트 정보 목록 조회(상용)
	 * @param playlistVO		조회 조건 정보
	 * @return 조회된 플레이리스트 정보 목록
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<GpackPlaylistVO> getGpackPlaylistListBiz(GpackPlaylistVO playlistVO) throws Exception {
		return (List<GpackPlaylistVO>)(getSqlMapClientTemplate().queryForList("gpack_promotion.getGpackPlaylistListBiz", playlistVO));
	}
	
	/**
	 * 플레이리스트 정보 조회
	 * @param playlistVO		조회 조건 정보
	 * @return 조회된 플레이리스트 정보
	 * @throws Exception
	 */
	public GpackPlaylistVO getGpackPlaylistDetail(GpackPlaylistVO playlistVO) throws Exception {
		return (GpackPlaylistVO)(getSqlMapClientTemplate().queryForObject("gpack_promotion.getGpackPlaylistDetail", playlistVO));
	}
	
	/**
	 * VOD 최저가 검색
	 * @param album_id			VOD 앨범 ID
	 * @return VOD 최저가
	 * @throws Exception
	 */
	public String getGpackVodPrice(String album_id) throws Exception {
		Map<String, String> param = new HashMap<String, String>();
		param.put("album_id", album_id);
		return (String)(getSqlMapClientTemplate().queryForObject("gpack_promotion.getGpackVodPrice", param));
	}
	
	/**
	 * 컨텐츠 정보 목록 조회
	 * @param pack_id			팩ID
	 * @param promotion_id		프로모션ID
	 * @param auto_yn			자동/수동 여부
	 * @return 컨텐츠 정보 목록
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<GpackPromotionContentsVO> getGpackContentsView(String pack_id, String promotion_id, String auto_yn) throws Exception {
		Map<String, String> param = new HashMap<String, String>();
		param.put("pack_id", pack_id);
		param.put("category_id", promotion_id);
		param.put("auto_yn",auto_yn);
		
		if("N".equals(auto_yn)) {
			return (List<GpackPromotionContentsVO>)(getSqlMapClientTemplate().queryForList("gpack_promotion.getGpackContentsView", param));
		} else {
			return (List<GpackPromotionContentsVO>)(getSqlMapClientTemplate().queryForList("gpack_promotion.getGpackPromotionAlbumList", param));
		}
	}
	
	/**
	 * 컨텐츠 정보 목록 조회(상용)
	 * @param pack_id			팩ID
	 * @param promotion_id		프로모션ID
	 * @param auto_yn			자동/수동 여부
	 * @return 컨텐츠 정보 목록
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<GpackPromotionContentsVO> getGpackContentsViewBiz(String pack_id, String promotion_id, String auto_yn) throws Exception {
		Map<String, String> param = new HashMap<String, String>();
		param.put("pack_id", pack_id);
		param.put("category_id", promotion_id);
		param.put("auto_yn",auto_yn);
		
		if("N".equals(auto_yn)) {
			return (List<GpackPromotionContentsVO>)(getSqlMapClientTemplate().queryForList("gpack_promotion.getGpackContentsViewBiz", param));
		} else {
			return (List<GpackPromotionContentsVO>)(getSqlMapClientTemplate().queryForList("gpack_promotion.getGpackPromotionAlbumListBiz", param));
		}
	}
	
	/**
	 * 프로모션 등록
	 * @param promotionVO		등록하고자 하는 프로모션 정보
	 * @throws Exception
	 */
	public void insertGpackPromotion(GpackPromotionVO promotionVO) throws Exception {
		getSqlMapClientTemplate().insert("gpack_promotion.insertGpackPromotion", promotionVO);
	}
	
	/**
	 * 프로모션 수정
	 * @param promotionVO		수정하고자 하는 프로모션 정보
	 * @return 결과
	 * @throws Exception
	 */
	public int updateGpackPromotion(GpackPromotionVO promotionVO) throws Exception {
		return getSqlMapClientTemplate().update("gpack_promotion.updateGpackPromotion", promotionVO);
	}
	
	/**
	 * 프로모션 순서 수정
	 * @param promotionVO		수정하고자 하는 프로모션 정보
	 * @return 결과
	 * @throws Exception
	 */
	public int updateGpackPromotionOrderby(GpackPromotionVO promotionVO) throws Exception {
		return getSqlMapClientTemplate().update("gpack_promotion.updateGpackPromotionOrderby", promotionVO);
	}
	
	/**
	 * 프로모션 삭제
	 * @param promotionVO		삭제하고자 하는 프로모션 정보
	 * @return 결과
	 * @throws Exception
	 */
	public int deleteGpackPromotion(GpackPromotionVO promotionVO) throws Exception {
		return getSqlMapClientTemplate().delete("gpack_promotion.deleteGpackPromotion", promotionVO);
	}
	
	/**
	 * 플레이리스트 등록
	 * @param playlistVO		등록하고자 하는 플레이리스트 정보
	 * @return 결과
	 * @throws Exception
	 */
	public void insertGpackPlaylist(GpackPlaylistVO playlistVO) throws Exception {
		getSqlMapClientTemplate().insert("gpack_promotion.insertGpackPlaylist", playlistVO);
	}
	
	/**
	 * 플레이리스트 순서 수정
	 * @param playlistVO		수정하고자 하는 플레이리스트 정보
	 * @return 결과
	 * @throws Exception
	 */
	public int updateGpackPlaylistOrderby(GpackPlaylistVO playlistVO) throws Exception {
		return getSqlMapClientTemplate().update("gpack_promotion.updateGpackPlaylistOrderby", playlistVO);
	}
	
	/**
	 * 플레이리스트 수정
	 * @param playlistVO		수정하고자 하는 플레이리스트 정보
	 * @return 결과
	 * @throws Exception
	 */
	public int updateGpackPlaylist(GpackPlaylistVO playlistVO) throws Exception {
		return getSqlMapClientTemplate().update("gpack_promotion.updateGpackPlaylist", playlistVO);
	}
	
	/**
	 * 플레이리스트 삭제
	 * @param playlistVO		삭제하고자 하는 플레이리스트 정보
	 * @return 결과
	 * @throws Exception
	 */
	public int deleteGpackPlaylist(GpackPlaylistVO playlistVO) throws Exception {
		return getSqlMapClientTemplate().delete("gpack_promotion.deleteGpackPlaylist", playlistVO);
	}

	/**
	 *  프로모션 상세 정보의 컨텐츠 유무
	 * @param promotionVO
	 * @return
	 */
	public String getPromotionByUseYn ( GpackPromotionVO promotionVO ) {
		return (String)(getSqlMapClientTemplate().queryForObject("gpack_promotion.getPromotionByUseYn", promotionVO));
	}
}
