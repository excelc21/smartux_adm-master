/**
 * Class Name : GpackEventServiceImpl.java
 * Description : 
 *  G Pack 이벤트 구현을 위한 service implement class
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
package com.dmi.smartux.admin.gpack.event.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmi.smartux.admin.gpack.contents.dao.GpackContentsViewDao;
import com.dmi.smartux.admin.gpack.contents.vo.GpackContentsAutoVO;
import com.dmi.smartux.admin.gpack.contents.vo.GpackContentsVO;
import com.dmi.smartux.admin.gpack.event.dao.GpackPromotionViewDao;
import com.dmi.smartux.admin.gpack.event.service.GpackPromotionViewService;
import com.dmi.smartux.admin.gpack.event.vo.GpackPlaylistVO;
import com.dmi.smartux.admin.gpack.event.vo.GpackProductVO;
import com.dmi.smartux.admin.gpack.event.vo.GpackPromotionContentsVO;
import com.dmi.smartux.admin.gpack.event.vo.GpackPromotionVO;
import com.dmi.smartux.admin.gpack.event.vo.TVChannelVO;
import com.dmi.smartux.admin.gpack.pack.dao.GPackPackViewDao;
import com.dmi.smartux.common.property.SmartUXProperties;

@Service
public class GpackPromotionViewServiceImpl implements GpackPromotionViewService {

	/** 프로모션 DAO */
	@Autowired
	GpackPromotionViewDao gpackPromotionDao;
	
	/** 팩(템플릿) DAO */
	@Autowired
	GPackPackViewDao gpackPackDao;
	
	/** 콘텐츠 DAO */
	@Autowired
	GpackContentsViewDao gpackContentsDao;
	
	/**
	 * 프로모션 목록 정보 조회
	 * @param promotionVO			조회 조건 정보
	 * @return 조회된 프로모션 목록 정보
	 * @throws Exception
	 */
	@Override
	@Transactional(readOnly=true)
	public List<GpackPromotionVO> getGpackPromotionList(GpackPromotionVO promotionVO) throws Exception {
		return gpackPromotionDao.getGpackPromotionList(promotionVO);
	}
	
	/**
	 * 프로모션 목록 정보 조회(상용)
	 * @param promotionVO			조회 조건 정보
	 * @return 조회된 프로모션 목록 정보
	 * @throws Exception
	 */
	@Override
	@Transactional(readOnly=true)
	public List<GpackPromotionVO> getGpackPromotionListBiz(GpackPromotionVO promotionVO) throws Exception {
		return gpackPromotionDao.getGpackPromotionListBiz(promotionVO);
	}
	
	/**
	 * 프로모션 상세 정보 조회
	 * @param promotionVO			조회 조건 정보
	 * @return 조회된 프로모션 상세 정보
	 * @throws Exception
	 */
	@Override
	@Transactional(readOnly=true)
	public GpackPromotionVO getGpackPromotionDetail(GpackPromotionVO promotionVO) throws Exception {
		return gpackPromotionDao.getGpackPromotionDetail(promotionVO);
	}
	
	/**
	 * 영상 프로모션 개수 조회
	 * @param promotionVO			조회 조건 정보
	 * @return 영상 프로모션 개수
	 * @throws Exception
	 */
	@Override
	@Transactional(readOnly=true)
	public int countGprackVideoPromotion(GpackPromotionVO promotionVO) throws Exception {
		return gpackPromotionDao.countGprackVideoPromotion(promotionVO);
	}
	
	/**
	 * TV 채널 목록 조회
	 * @param channelVO		조회 조건 정보
	 * @return 조회된 TV 채널 목록
	 * @throws Exception
	 */
	@Override
	@Transactional(readOnly=true)
	public List<TVChannelVO> getTVChannelList(TVChannelVO channelVO) throws Exception {
		return gpackPromotionDao.getTVChannelList(channelVO);
	}
	
	/**
	 * TV 채널 카운트
	 * @param channelVO		조회 조건 정보
	 * @return 조회된 TV 채널 개수
	 * @throws Exception
	 */
	@Override
	@Transactional(readOnly=true)
	public int countTVChannelList(TVChannelVO channelVO) throws Exception {
		return gpackPromotionDao.countTVChannelList(channelVO);
	}
	
	/**
	 * 월정액 상품 목록 조회
	 * @param productVO		조회 조건 정보
	 * @return 조회된 월정액 상품 목록
	 * @throws Exception
	 */
	@Override
	@Transactional(readOnly=true)
	public List<GpackProductVO> getGpackProductList(GpackProductVO productVO) throws Exception {
		return gpackPromotionDao.getGpackProductList(productVO);
	}
	
	/**
	 * 월정액 상품 카운트
	 * @param productVO		조회 조건 정보
	 * @return 조회된 월정액 상품 개수
	 * @throws Exception
	 */
	@Override
	@Transactional(readOnly=true)
	public int countGpackProductList(GpackProductVO productVO) throws Exception {
		return gpackPromotionDao.countGpackProductList(productVO);
	}
	
	/**
	 * 플레이리스트 정보 목록 조회
	 * @param playlistVO		조회 조건 정보
	 * @return 조회된 플레이리스트 정보 목록
	 * @throws Exception
	 */
	@Override
	@Transactional(readOnly=true)
	public List<GpackPlaylistVO> getGpackPlaylistList(GpackPlaylistVO playlistVO) throws Exception {
		return gpackPromotionDao.getGpackPlaylistList(playlistVO);
	}
	
	/**
	 * 플레이리스트 정보 목록 조회(상용)
	 * @param playlistVO		조회 조건 정보
	 * @return 조회된 플레이리스트 정보 목록
	 * @throws Exception
	 */
	@Override
	@Transactional(readOnly=true)
	public List<GpackPlaylistVO> getGpackPlaylistListBiz(GpackPlaylistVO playlistVO) throws Exception {
		return gpackPromotionDao.getGpackPlaylistListBiz(playlistVO);
	}
	
	/**
	 * 플레이리스트 정보 조회
	 * @param playlistVO		조회 조건 정보
	 * @return 조회된 플레이리스트 정보
	 * @throws Exception
	 */
	@Override
	@Transactional(readOnly=true)
	public GpackPlaylistVO getGpackPlaylistDetail(GpackPlaylistVO playlistVO) throws Exception {
		return gpackPromotionDao.getGpackPlaylistDetail(playlistVO);
	}
	
	/**
	 * VOD 최저가 검색
	 * @param album_id			VOD 앨범 ID
	 * @return VOD 최저가
	 * @throws Exception
	 */
	@Override
	@Transactional(readOnly=true)
	public String getGpackVodPrice(String album_id) throws Exception {
		return gpackPromotionDao.getGpackVodPrice(album_id);
	}
	
	/**
	 * 프로모션 미리보기 조회
	 * @param pack_id			팩ID
	 * @param template_type		템플릿타입
	 * @return 조회된 프로모션 정보
	 * @throws Exception
	 */
	public List<GpackPromotionVO> getGpackPromotionPreview(String pack_id, String template_type) throws Exception {
		
		//템플릿별 유효 컨텐츠 개수
		int valid_contents_count = 1;
		if("TP001".equals(template_type)) {
			valid_contents_count = Integer.parseInt(SmartUXProperties.getProperty("admin.gpack.promotion.template1.valid.contents.count"));
		} else {
			valid_contents_count = Integer.parseInt(SmartUXProperties.getProperty("admin.gpack.promotion.template2.valid.contents.count"));
		}
		
		// 프로모션 조회
		GpackPromotionVO promotionVO = new GpackPromotionVO();
		promotionVO.setPack_id(pack_id);
		promotionVO.setWhere_use_yn("Y");
		List<GpackPromotionVO> promotionVOList = getGpackPromotionList(promotionVO);
		
		List<GpackPromotionVO> result = new ArrayList<GpackPromotionVO>();
		for(GpackPromotionVO item : promotionVOList){
			
			// 콘텐츠
			List<GpackPromotionContentsVO> contentsList = gpackPromotionDao.getGpackContentsView(pack_id, item.getCategory_id(), item.getAuto_yn());
			List<GpackPlaylistVO> vedioList = null;
			
			if(contentsList.size() >= valid_contents_count) {
				item.setGpackPromotionContentsVOList(contentsList);
			
				// 템플릿2인 경우 영상정보
				if("TP002".equals(template_type)) {
					if("PV001".equals(item.getPromotion_video_gb())) {
						vedioList = new ArrayList<GpackPlaylistVO>();
						// 채널
						GpackPlaylistVO vedio = new GpackPlaylistVO();
	
						vedio.setPlaylist_nm(item.getPromotion_chnl_info());
						vedioList.add(vedio);
						
					} else if("PV002".equals(item.getPromotion_video_gb())) {
						vedioList = new ArrayList<GpackPlaylistVO>();
						// 플레이리스트
						GpackPlaylistVO playlistVO = new GpackPlaylistVO();
						playlistVO.setPack_id(pack_id);
						playlistVO.setCategory_id(item.getCategory_id());
						playlistVO.setWhere_use_yn("Y");
						vedioList = getGpackPlaylistList(playlistVO);
					}
					item.setGpackPlaylistVOList(vedioList);
				}
				
				// 영상이 포함된 프로모션은 최상위에 표시되어야 함
				if("TP002".equals(template_type) && ("PV001".equals(item.getPromotion_video_gb()) || "PV002".equals(item.getPromotion_video_gb()))) {
					// 영상 포함 프로모션은 영상을 하나 이상은 등록되어야함
					if(vedioList.size() > 0) {
						result.add(0, item);
					}
				} else {
					result.add(item);
				}
			}
		}
		
		return result;
	}
	
	/**
	 * 프로모션 미리보기 조회(상용)
	 * @param pack_id			팩ID
	 * @param template_type		템플릿타입
	 * @return 조회된 프로모션 정보
	 * @throws Exception
	 */
	public List<GpackPromotionVO> getGpackPromotionPreviewBiz(String pack_id, String template_type) throws Exception {
		
		//템플릿별 유효 컨텐츠 개수
		int valid_contents_count = 1;
		if("TP001".equals(template_type)) {
			valid_contents_count = Integer.parseInt(SmartUXProperties.getProperty("admin.gpack.promotion.template1.valid.contents.count"));
		} else {
			valid_contents_count = Integer.parseInt(SmartUXProperties.getProperty("admin.gpack.promotion.template2.valid.contents.count"));
		}
		
		// 프로모션 조회(상용)
		GpackPromotionVO promotionVO = new GpackPromotionVO();
		promotionVO.setPack_id(pack_id);
		promotionVO.setWhere_use_yn("Y");
		List<GpackPromotionVO> promotionVOList = getGpackPromotionListBiz(promotionVO);
		
		List<GpackPromotionVO> result = new ArrayList<GpackPromotionVO>();
		for(GpackPromotionVO item : promotionVOList){
			
			// 콘텐츠
			List<GpackPromotionContentsVO> contentsList = gpackPromotionDao.getGpackContentsViewBiz(pack_id, item.getCategory_id(), item.getAuto_yn());
			List<GpackPlaylistVO> vedioList = null;
			
			if(contentsList.size() >= valid_contents_count) {
				item.setGpackPromotionContentsVOList(contentsList);
			
				// 템플릿2인 경우 영상정보
				if("TP002".equals(template_type)) {
					if("PV001".equals(item.getPromotion_video_gb())) {
						vedioList = new ArrayList<GpackPlaylistVO>();
						// 채널
						GpackPlaylistVO vedio = new GpackPlaylistVO();
	
						vedio.setPlaylist_nm(item.getPromotion_chnl_info());
						vedioList.add(vedio);
						
					} else if("PV002".equals(item.getPromotion_video_gb())) {
						vedioList = new ArrayList<GpackPlaylistVO>();
						// 플레이리스트
						GpackPlaylistVO playlistVO = new GpackPlaylistVO();
						playlistVO.setPack_id(pack_id);
						playlistVO.setCategory_id(item.getCategory_id());
						playlistVO.setWhere_use_yn("Y");
						vedioList = getGpackPlaylistListBiz(playlistVO);
					}
					item.setGpackPlaylistVOList(vedioList);
				}
				
				// 영상이 포함된 프로모션은 최상위에 표시되어야 함
				if("TP002".equals(template_type) && ("PV001".equals(item.getPromotion_video_gb()) || "PV002".equals(item.getPromotion_video_gb()))) {
					// 영상 포함 프로모션은 영상을 하나 이상은 등록되어야함
					if(vedioList.size() > 0) {
						result.add(0, item);
					}
				} else {
					result.add(item);
				}
			}
		}
		
		return result;
	}
	
	/**
	 * 프로모션 등록
	 * @param promotionVO		등록하고자 하는 프로모션 정보
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void insertGpackPromotion(GpackPromotionVO promotionVO) throws Exception {
		gpackPromotionDao.insertGpackPromotion(promotionVO);
	}
	
	/**
	 * 프로모션 수정
	 * @param promotionVO		수정하고자 하는 프로모션 정보
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void updateGpackPromotion(GpackPromotionVO promotionVO) throws Exception {
		gpackPromotionDao.updateGpackPromotion(promotionVO);
	}
	
	/**
	 * 프로모션 순서 수정
	 * @param pack_id			팩ID
	 * @param update_id 		유저ID	
	 * @param promotionVOList	수정하고자 하는 프로모션 정보
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void updateGpackPromotionOrderby(String pack_id, String update_id, List<GpackPromotionVO> promotionVOList) throws Exception {
		for(GpackPromotionVO promotionVO : promotionVOList) {
			gpackPromotionDao.updateGpackPromotionOrderby(promotionVO);
		}
	}
	
	/**
	 * 프로모션 삭제
	 * @param pack_id			팩ID
	 * @param category_ids		삭제하고자 하는 프로모션ID 값들 배열
	 * @param login_id 		 	유저ID	
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void deleteGpackPromotion(String pack_id, String [] category_ids, String login_id) throws Exception {
		for(String category_id : category_ids) {
			
			// 프로모션 삭제
			GpackPromotionVO promotionVO = new GpackPromotionVO();
			promotionVO.setPack_id(pack_id);
			promotionVO.setCategory_id(category_id);
			gpackPromotionDao.deleteGpackPromotion(promotionVO);
			
			//플레이리스트 삭제
			GpackPlaylistVO playlistVO = new GpackPlaylistVO();
			playlistVO.setPack_id(pack_id);
			playlistVO.setCategory_id(category_id);
			gpackPromotionDao.deleteGpackPlaylist(playlistVO);
			
			//컨텐츠 삭제
			GpackContentsVO contentsVO = new GpackContentsVO();
			contentsVO.setPack_id(pack_id);
			contentsVO.setCategory_id(category_id);
			gpackContentsDao.deleteGpackContents(contentsVO);
			
			//AUTO 삭제
			GpackContentsAutoVO contentsAutoVO = new GpackContentsAutoVO();
			contentsAutoVO.setPack_id(pack_id);
			contentsAutoVO.setCategory_id(category_id);
			gpackContentsDao.deleteGpackContentsAuto(contentsAutoVO);
			
		}
	}
	
	/**
	 * 플레이리스트 등록
	 * @param playlistVO		등록하고자 하는 플레이리스트 정보
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void insertGpackPlaylist(GpackPlaylistVO playlistVO) throws Exception {
		gpackPromotionDao.insertGpackPlaylist(playlistVO);
	}
	
	/**
	 * 플레이리스트 순서 수정
	 * @param pack_id			팩ID
	 * @param update_id 		유저ID	
	 * @param playlistVO		수정하고자 하는 플레이리스트 정보
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void updateGpackPlaylistOrderby(String pack_id, String update_id, List<GpackPlaylistVO> playlistVOList) throws Exception {
		for(GpackPlaylistVO playlistVO : playlistVOList) {
			gpackPromotionDao.updateGpackPlaylistOrderby(playlistVO);
		}
	}
	
	/**
	 * 플레이리스트 수정
	 * @param playlistVO		수정하고자 하는 플레이리스트 정보
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void updateGpackPlaylist(GpackPlaylistVO playlistVO) throws Exception {
		gpackPromotionDao.updateGpackPlaylist(playlistVO);
	}
	
	/**
	 * 플레이리스트 삭제
	 * @param pack_id			팩ID
	 * @param category_id		프로모션ID
	 * @param playlist_ids		삭제하고자 하는 플레이리스트ID 값들 배열
	 * @param login_id 		 	유저ID	
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void deleteGpackPlaylist(String pack_id, String category_id, String [] playlist_ids, String login_id) throws Exception {
		for(String playlist_id : playlist_ids) {
			
			GpackPlaylistVO playlistVO = new GpackPlaylistVO();
			playlistVO.setPack_id(pack_id);
			playlistVO.setCategory_id(category_id);
			playlistVO.setPlaylist_id(playlist_id);
			gpackPromotionDao.deleteGpackPlaylist(playlistVO);
		}
	}

	/**
	 * 프로모션 상세 정보의 컨텐츠 유무
	 */
	@Override
	public String getPromotionByUseYn ( GpackPromotionVO promotionVO ) throws Exception {
		return gpackPromotionDao.getPromotionByUseYn(promotionVO);
	}
}
