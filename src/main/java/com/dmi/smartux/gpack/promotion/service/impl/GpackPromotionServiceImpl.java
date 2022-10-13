package com.dmi.smartux.gpack.promotion.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmi.smartux.cache.service.CacheIntervalService;
import com.dmi.smartux.common.exception.SmartUXException;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.service.CacheService;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.gpack.category.vo.GpackCategoryAlbumInfoVO;
import com.dmi.smartux.gpack.promotion.dao.GpackPromotionDao;
import com.dmi.smartux.gpack.promotion.service.GpackPromotionService;
import com.dmi.smartux.gpack.promotion.vo.GpackPromotionContentsVO;
import com.dmi.smartux.gpack.promotion.vo.GpackPromotionInfoVO;
import com.dmi.smartux.mainpanel.dao.MainPanelDao;
import com.dmi.smartux.mainpanel.service.MainPanelService;
import com.dmi.smartux.mainpanel.vo.AlbumInfoVO;
import com.dmi.smartux.mainpanel.vo.MainPanelInfoVO;

@Service
public class GpackPromotionServiceImpl implements GpackPromotionService {

	@Autowired
	GpackPromotionDao dao;
	
	@Autowired
	CacheService cacheservice;
	
	@Autowired
	CacheIntervalService cacheIntervalService;
	
	@Override
	//@Transactional 140708
	public int getPackPromotionCount(String sa_id, String stb_mac, String app_type, String pack_id, String promotion_id, String start_num, String req_count, String p_start_num, String p_req_count, String callByScheduler) throws Exception {
		// TODO Auto-generated method stub
		int result = 0;
		List<GpackPromotionInfoVO> dbresult = new ArrayList<GpackPromotionInfoVO>();
		
		String intervalStr = "0";
		try{
			intervalStr = Long.toString(cacheIntervalService.getCacheInterval("GpackPackPromotionDao.getPackPromotion.interval"));
		}catch (Exception e) {
			intervalStr = SmartUXProperties.getProperty("GpackPackPromotionDao.getPackPromotion.interval");
		}
		long interval = GlobalCom.getCacheInterval(callByScheduler, intervalStr);
		
		// Main Panel 연동 정보는 관리자가 확인 작업을 할때만 캐쉬가 갱신이 되므로 
		// 셋탑이 조회할때는 갱신주기를 0으로 주어 DB에서 다시 읽어서 캐쉬를 갱신하는 작업을 하지 않도록 한다 
		
		
		List<GpackPromotionInfoVO> mapresult = (List<GpackPromotionInfoVO>)cacheservice.getCache(dao
				, "getPackPromotion"
				, SmartUXProperties.getProperty("GpackPackPromotionDao.getPackPromotion.cacheKey")
				, interval
				, pack_id
				);
		
		if(mapresult == null){
			//
		}else{
			result = mapresult.size();
		}
		
		if(result<= 0){//맵이 있냐없냐가 아니라 일단 데이터가 없다면 에러로!!130906
			SmartUXException ex = new SmartUXException();
			ex.setFlag(SmartUXProperties.getProperty("flag.beNotData"));
			ex.setMessage(SmartUXProperties.getProperty("message.beNotData"));
			throw ex;
		}
		
		return result;
		
	}

	@Override
	//@Transactional 140708
	public List<GpackPromotionInfoVO> getPackPromotion(String sa_id, String stb_mac, String app_type, String pack_id, String promotion_id, String start_num, String req_count, String p_start_num, String p_req_count, int fh_gbn, String callByScheduler) throws Exception {
		// TODO Auto-generated method stub
		List<GpackPromotionInfoVO> result = new ArrayList<GpackPromotionInfoVO>();
		 
		String intervalStr = "0";
		try{
			intervalStr = Long.toString(cacheIntervalService.getCacheInterval("GpackPackPromotionDao.getPackPromotion.interval"));
		}catch (Exception e) {
			intervalStr = SmartUXProperties.getProperty("GpackPackPromotionDao.getPackPromotion.interval");
		}
		long interval = GlobalCom.getCacheInterval(callByScheduler, intervalStr);
		
		// Main Panel 연동 정보는 관리자가 확인 작업을 할때만 캐쉬가 갱신이 되므로 
		// 셋탑이 조회할때는 갱신주기를 0으로 주어 DB에서 다시 읽어서 캐쉬를 갱신하는 작업을 하지 않도록 한다 
		
		List<GpackPromotionInfoVO> mapresult = (List<GpackPromotionInfoVO>)cacheservice.getCache(dao
				, "getPackPromotion"
				, SmartUXProperties.getProperty("GpackPackPromotionDao.getPackPromotion.cacheKey")
				, interval
				, pack_id
				);
		
		if(mapresult != null){
		
			if("Y".equals(callByScheduler) || "A".equals(callByScheduler)){			// 스케줄러,관리툴 에서 호출한 경우
				
			}else{										// 단말에서 호출한 경우
				
				int p_startnum = 1;
				int p_endnum = mapresult.size();
				
				if("0".equals(promotion_id)) {
					p_startnum = Integer.parseInt(p_start_num);
					p_endnum = p_startnum + Integer.parseInt(p_req_count) - 1;
					
					if(p_endnum > mapresult.size()){
						p_endnum = mapresult.size();
					}
				}
				p_startnum--;
				for(int p = p_startnum; p < p_endnum; p++) {
					
					GpackPromotionInfoVO promotionInfoVO = mapresult.get(p);
					
					if(promotion_id.equals(promotionInfoVO.getPromotion_id()) || "0".equals(promotion_id)) {
						
						GpackPromotionInfoVO resultPromotionInfoVO = new GpackPromotionInfoVO();
						
						// 콘텐츠 페이징
						List<GpackPromotionContentsVO> contentsList = promotionInfoVO.getRecordset_vod();
						List<GpackPromotionContentsVO> newContentsList = new ArrayList<GpackPromotionContentsVO>();
						
						int startnum = Integer.parseInt(start_num);
						if(startnum >= 1){						// 1보다 큰 경우는 페이징 기법이 적용되므로 전체 조회한 내용에서 대상되는 레코드만큼만 꺼내서 넣는다
							startnum--;
							int endnum = startnum + Integer.parseInt(req_count);	//다음 페이지까지 
							
							// endnum 수치가 검색된 리스트의 마지막 인덱스보다 큰지를 조사해서 그거보다 큰 경우엔 endnum을 검색된 리스트의 마지막 인덱스로 셋팅해준다
							if(endnum > contentsList.size()){
								endnum = contentsList.size();
							}
							
							if(fh_gbn==1){//망 타입이 FullHD 서비스를 하는 망이라면..
								for(int i=startnum; i < endnum; i++){
									newContentsList.add(contentsList.get(i));
								}
							}else{
								GpackPromotionContentsVO tmpVO;
								for(int i=startnum; i < endnum; i++){
									tmpVO = (GpackPromotionContentsVO) contentsList.get(i).clone();
									tmpVO.setIs_fh("N");//FullHD 서비스하지 않는 망이므로 무조건N
									newContentsList.add(tmpVO);
								}
							}
						}else if(startnum == -1){				// -1인 경우 전체 조회이므로 db 조회 결과를 return 해준다
							if(fh_gbn==1){//망 타입이 FullHD 서비스를 하는 망이라면..
								newContentsList = contentsList;
							}else{
								GpackPromotionContentsVO tmpVO;
								for(GpackPromotionContentsVO vo : contentsList) {
									tmpVO = (GpackPromotionContentsVO) vo.clone();
									tmpVO.setIs_fh("N");//FullHD 서비스하지 않는 망이므로 무조건N
									newContentsList.add(tmpVO);
								}
							}
						}else if(startnum == 0){
							newContentsList = null;
						}

						resultPromotionInfoVO.setPromotion_id(promotionInfoVO.getPromotion_id());
						resultPromotionInfoVO.setTitle(promotionInfoVO.getTitle());
						resultPromotionInfoVO.setMent(promotionInfoVO.getMent());
						resultPromotionInfoVO.setMov_total_count(promotionInfoVO.getMov_total_count());
						resultPromotionInfoVO.setVod_total_count(promotionInfoVO.getVod_total_count());
						resultPromotionInfoVO.setRecordset_mov(promotionInfoVO.getRecordset_mov());
						resultPromotionInfoVO.setRecordset_vod(newContentsList);
						
						result.add(resultPromotionInfoVO);
					}
				}
			}
		}
		
		return result;
	}
	
}
