/**
 * Class Name : GpackPromotionService.java
 * Description : 
 *  프로모션 관리 구현을 위한 service class
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
package com.dmi.smartux.admin.gpack.event.service;

import java.util.List;

import com.dmi.smartux.admin.gpack.event.vo.GpackPlaylistVO;
import com.dmi.smartux.admin.gpack.event.vo.GpackProductVO;
import com.dmi.smartux.admin.gpack.event.vo.TVChannelVO;
import com.dmi.smartux.admin.gpack.event.vo.GpackPromotionVO;


public interface GpackPromotionViewService {

	/**
	 * 프로모션 목록 정보 조회
	 * @param promotionVO			조회 조건 정보
	 * @return 조회된 프로모션 목록 정보
	 * @throws Exception
	 */
	public List<GpackPromotionVO> getGpackPromotionList(GpackPromotionVO promotionVO) throws Exception;
	
	/**
	 * 프로모션 목록 정보 조회(상용)
	 * @param promotionVO			조회 조건 정보
	 * @return 조회된 프로모션 목록 정보
	 * @throws Exception
	 */
	public List<GpackPromotionVO> getGpackPromotionListBiz(GpackPromotionVO promotionVO) throws Exception;
	
	/**
	 * 프로모션 상세 정보 조회
	 * @param promotionVO			조회 조건 정보
	 * @return 조회된 프로모션 상세 정보
	 * @throws Exception
	 */
	public GpackPromotionVO getGpackPromotionDetail(GpackPromotionVO promotionVO) throws Exception;
	
	/**
	 * 영상 프로모션 개수 조회
	 * @param promotionVO			조회 조건 정보
	 * @return 영상 프로모션 개수
	 * @throws Exception
	 */
	public int countGprackVideoPromotion(GpackPromotionVO promotionVO) throws Exception;
	
	/**
	 * TV 채널 목록 조회
	 * @param channelVO		조회 조건 정보
	 * @return 조회된 TV 채널 목록
	 * @throws Exception
	 */
	public List<TVChannelVO> getTVChannelList(TVChannelVO channelVO) throws Exception;
	
	/**
	 * TV 채널 카운트
	 * @param channelVO		조회 조건 정보
	 * @return 조회된 TV 채널 개수
	 * @throws Exception
	 */
	public int countTVChannelList(TVChannelVO channelVO) throws Exception;
	
	/**
	 * 월정액 상품 목록 조회
	 * @param productVO		조회 조건 정보
	 * @return 조회된 월정액 상품 목록
	 * @throws Exception
	 */
	public List<GpackProductVO> getGpackProductList(GpackProductVO productVO) throws Exception;
	
	/**
	 * 월정액 상품 카운트
	 * @param productVO		조회 조건 정보
	 * @return 조회된 월정액 상품 개수
	 * @throws Exception
	 */
	public int countGpackProductList(GpackProductVO productVO) throws Exception;
	
	/**
	 * 플레이리스트 정보 목록 조회
	 * @param playlistVO		조회 조건 정보
	 * @return 조회된 플레이리스트 정보 목록
	 * @throws Exception
	 */
	public List<GpackPlaylistVO> getGpackPlaylistList(GpackPlaylistVO playlistVO) throws Exception;
	
	/**
	 * 플레이리스트 정보 목록 조회(상용)
	 * @param playlistVO		조회 조건 정보
	 * @return 조회된 플레이리스트 정보 목록
	 * @throws Exception
	 */
	public List<GpackPlaylistVO> getGpackPlaylistListBiz(GpackPlaylistVO playlistVO) throws Exception;
	
	/**
	 * 플레이리스트 정보 조회
	 * @param playlistVO		조회 조건 정보
	 * @return 조회된 플레이리스트 정보
	 * @throws Exception
	 */
	public GpackPlaylistVO getGpackPlaylistDetail(GpackPlaylistVO playlistVO) throws Exception;
	
	/**
	 * VOD 최저가 검색
	 * @param album_id			VOD 앨범 ID
	 * @return VOD 최저가
	 * @throws Exception
	 */
	public String getGpackVodPrice(String album_id) throws Exception;
	
	/**
	 * 프로모션 미리보기 조회
	 * @param pack_id			팩ID
	 * @param template_type		템플릿타입
	 * @return 조회된 프로모션 정보
	 * @throws Exception
	 */
	public List<GpackPromotionVO> getGpackPromotionPreview(String pack_id, String template_type) throws Exception;
	
	/**
	 * 프로모션 미리보기 조회(상용)
	 * @param pack_id			팩ID
	 * @param template_type		템플릿타입
	 * @return 조회된 프로모션 정보
	 * @throws Exception
	 */
	public List<GpackPromotionVO> getGpackPromotionPreviewBiz(String pack_id, String template_type) throws Exception;
	
	/**
	 * 프로모션 등록
	 * @param promotionVO		등록하고자 하는 프로모션 정보
	 * @throws Exception
	 */
	public void insertGpackPromotion(GpackPromotionVO promotionVO) throws Exception;
	
	/**
	 * 프로모션 수정
	 * @param promotionVO		수정하고자 하는 프로모션 정보
	 * @throws Exception
	 */
	public void updateGpackPromotion(GpackPromotionVO promotionVO) throws Exception;
	
	/**
	 * 프로모션 순서 수정
	 * @param pack_id			팩ID
	 * @param update_id 		유저ID	
	 * @param promotionVOList	수정하고자 하는 프로모션 정보
	 * @throws Exception
	 */
	public void updateGpackPromotionOrderby(String pack_id, String update_id, List<GpackPromotionVO> promotionVOList) throws Exception;
	
	/**
	 * 프로모션 삭제
	 * @param pack_id			팩ID
	 * @param category_ids		삭제하고자 하는 프로모션ID 값들 배열
	 * @param login_id 		 	유저ID	
	 * @throws Exception
	 */
	public void deleteGpackPromotion(String pack_id, String [] category_ids, String login_id) throws Exception;
	
	/**
	 * 플레이리스트 등록
	 * @param playlistVO		등록하고자 하는 플레이리스트 정보
	 * @throws Exception
	 */
	public void insertGpackPlaylist(GpackPlaylistVO playlistVO) throws Exception;
	
	/**
	 * 플레이리스트 순서 수정
	 * @param pack_id			팩ID
	 * @param update_id 		유저ID	
	 * @param playlistVO		수정하고자 하는 플레이리스트 정보
	 * @throws Exception
	 */
	public void updateGpackPlaylistOrderby(String pack_id, String update_id, List<GpackPlaylistVO> playlistVOList) throws Exception;
	
	/**
	 * 플레이리스트 수정
	 * @param playlistVO		수정하고자 하는 플레이리스트 정보
	 * @throws Exception
	 */
	public void updateGpackPlaylist(GpackPlaylistVO playlistVO) throws Exception;
	
	/**
	 * 플레이리스트 삭제
	 * @param pack_id			팩ID
	 * @param category_id		프로모션ID
	 * @param playlist_ids		삭제하고자 하는 플레이리스트ID 값들 배열
	 * @param login_id 		 	유저ID	
	 * @throws Exception
	 */
	public void deleteGpackPlaylist(String pack_id, String category_id, String [] playlist_ids, String login_id) throws Exception;

	/**
	 *  프로모션 상세 정보의 컨텐츠 유무
	 * @param promotionVO
	 * @return
	 */
	public String getPromotionByUseYn ( GpackPromotionVO promotionVO ) throws Exception;
}
