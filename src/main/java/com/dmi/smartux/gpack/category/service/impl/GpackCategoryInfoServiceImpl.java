package com.dmi.smartux.gpack.category.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmi.smartux.cache.service.CacheIntervalService;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.service.CacheService;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.gpack.category.dao.GpackCategoryInfoDao;
import com.dmi.smartux.gpack.category.service.GpackCategoryInfoService;
import com.dmi.smartux.gpack.category.vo.GpackCategoryAlbumInfoVO;
import com.dmi.smartux.gpack.category.vo.GpackCategoryInfoVO;
import com.dmi.smartux.gpack.category.vo.GpackCategoryResult;

@Service
public class GpackCategoryInfoServiceImpl implements GpackCategoryInfoService {

	@Autowired
	GpackCategoryInfoDao dao;
	
	@Autowired
	CacheService cacheservice;
	
	@Autowired
	CacheIntervalService cacheIntervalService;
	

	public GpackCategoryResult getPackCategory(String sa_id, String stb_mac, String pack_id, String category_id, String start_num, String req_count, String p_start_num, String p_req_count, int fh_gbn, String callByScheduler) throws Exception{

		String intervalStr = "0";
		try{
			intervalStr = Long.toString(cacheIntervalService.getCacheInterval("GpackCategoryInfoDao.getPackCategory.interval"));
		}catch (Exception e) {
			intervalStr = SmartUXProperties.getProperty("GpackCategoryInfoDao.getPackCategory.interval");
		}

		// 스케쥴러에서 호출한 경우는 해당 interval, 
		// 관리자에서 호출한 경우 무조건 DB에서 읽어오도록,
		// 현제 셋팅이 강제로 DB에서 읽어오도록 되어 있는지를 확인, 아니면 무조건 캐시에서 읽어온다.
		long interval = GlobalCom.getCacheInterval(callByScheduler, intervalStr);

		GpackCategoryResult result = new GpackCategoryResult();
		GpackCategoryResult db_result = (GpackCategoryResult)cacheservice.getCache(dao
				, "getPackCategory"
				, SmartUXProperties.getProperty("GpackCategoryInfoDao.getPackCategory.cacheKey")
				, interval
				, pack_id
				);

		List<GpackCategoryInfoVO> category_result = new ArrayList<GpackCategoryInfoVO>();
		List<GpackCategoryInfoVO> t_category_result = getGpackCategoryPageing(pack_id, category_id, db_result.getRecordset_depth1(), p_start_num, p_req_count, callByScheduler);

		int reSize = t_category_result.size();
		if(reSize > 0){
			String t_category_id = "";
			List<GpackCategoryAlbumInfoVO> album_result = new ArrayList<GpackCategoryAlbumInfoVO>();

			for(int i=0; i<reSize; i++){
				GpackCategoryInfoVO tmpVO = new GpackCategoryInfoVO();
				tmpVO = t_category_result.get(i);
				t_category_id = tmpVO.getPcategory_id();

				album_result = (List<GpackCategoryAlbumInfoVO>)cacheservice.getCache(dao
						, "getGpackCategoryAlbumList"
						, SmartUXProperties.getProperty("GpackCategoryInfoDao.getPackCategoryAlbum.cacheKey")
						, interval
						, pack_id
						, t_category_id
						);

				if("Y".equals(callByScheduler) || "A".equals(callByScheduler)){			// 관리자, 스케듈러에서 호출한 경우
					tmpVO.setRecordset_depth2(album_result);
				}else{

					// 단말에서 호출한 경우
					// 시작번호를 가지고 전체 조회, 또는 페이징 조회를 해야 하는지의 판단여부
					int intStartnum = Integer.parseInt(start_num);
					if((intStartnum > 0) || (intStartnum == -1)){					// 검색 시작 인덱스가 -1 또는 1 이상일때만 결과셋
						tmpVO.setRecordset_depth2(getGpackCategoryAlbumPageing(album_result, start_num, req_count, fh_gbn, callByScheduler));
					}else{
						tmpVO.setRecordset_depth2(null);
					}
				}

				category_result.add(tmpVO);
			}
		}

		result.setTotal_count(db_result.getTotal_count());
		result.setRecordset_depth1(category_result);
		
		return result;
	}

	/**
	 * 카테고리 페이징 
	 * @param album_result
	 * @param start_num
	 * @param req_count
	 * @param fh_gbn
	 * @param callByScheduler
	 * @return
	 * @throws Exception
	 */
	public List<GpackCategoryInfoVO> getGpackCategoryPageing(String pack_id, String category_id, List<GpackCategoryInfoVO> category_result, String p_start_num, String p_req_count, String callByScheduler) throws Exception{

		List<GpackCategoryInfoVO> result = new ArrayList<GpackCategoryInfoVO>();
		
		if("Y".equals(callByScheduler) || "A".equals(callByScheduler)){			// 관리자, 스케듈러에서 호출한 경우
			result = category_result;
		}else {
			if(category_id.equals("0")){	//단말에서 호출하고, 카테고리 id가 0인 경우 페이징 
				int startnum = Integer.parseInt(p_start_num);
				if(startnum > 0){						// 0보다 큰 경우는 페이징 기법이 적용되므로 전체 조회한 내용에서 대상되는 레코드만큼만 꺼내서 넣는다
					startnum--;
					int endnum = startnum + (Integer.parseInt(p_req_count));
					
					// endnum 수치가 검색된 리스트의 마지막 인덱스보다 큰지를 조사해서 그거보다 큰 경우엔 endnum을 검색된 리스트의 마지막 인덱스로 셋팅해준다
					if(endnum > category_result.size()){
						endnum = category_result.size();
					}
					
					for(int i=startnum; i < endnum; i++){
						result.add(category_result.get(i));
					}
				}		
			}else{
				String cg_id = "";
				for(int i=0; i < category_result.size(); i++){
					cg_id = (category_result.get(i)).getPcategory_id();
					if(cg_id.equals(category_id)){
						result.add(category_result.get(i));
					}
				}
			}
		}
		
		return result;
	}
	

	/**
	 * 앨범 페이징 
	 * @param album_result
	 * @param start_num
	 * @param req_count
	 * @param fh_gbn
	 * @param callByScheduler
	 * @return
	 * @throws Exception
	 */
	public List<GpackCategoryAlbumInfoVO> getGpackCategoryAlbumPageing(List<GpackCategoryAlbumInfoVO> album_result, String start_num, String req_count, int fh_gbn, String callByScheduler) throws Exception{

		List<GpackCategoryAlbumInfoVO> result = new ArrayList<GpackCategoryAlbumInfoVO>();
		
		if(album_result != null){
		
			if("Y".equals(callByScheduler) || "A".equals(callByScheduler)){			// 관리자, 스케듈러에서 호출한 경우
				result = album_result;
			}else{										// 단말에서 호출한 경우
				int startnum = Integer.parseInt(start_num);
				if(startnum >= 1){						// 1보다 큰 경우는 페이징 기법이 적용되므로 전체 조회한 내용에서 대상되는 레코드만큼만 꺼내서 넣는다
					startnum--;
					int endnum = startnum + (Integer.parseInt(req_count));
					
					// endnum 수치가 검색된 리스트의 마지막 인덱스보다 큰지를 조사해서 그거보다 큰 경우엔 endnum을 검색된 리스트의 마지막 인덱스로 셋팅해준다
					if(endnum > album_result.size()){
						endnum = album_result.size();
					}
					
					result = new ArrayList<GpackCategoryAlbumInfoVO>();
					
					
					if(fh_gbn==1){//망 타입이 FullHD 서비스를 하는 망이라면..
						for(int i=startnum; i < endnum; i++){
							result.add(album_result.get(i));
						}
					}else{
						GpackCategoryAlbumInfoVO tmpVO;
						for(int i=startnum; i < endnum; i++){
							tmpVO = (GpackCategoryAlbumInfoVO) album_result.get(i).clone();
							tmpVO.setIs_fh("N");//FullHD 서비스하지 않는 망이므로 무조건N
							result.add(tmpVO);
						}
					}
				}else if(startnum == -1){				// -1인 경우 전체 조회이므로 db 조회 결과를 return 해준다
					if(fh_gbn==1){//망 타입이 FullHD 서비스를 하는 망이라면..
						result = album_result;
					}else{
						GpackCategoryAlbumInfoVO tmpVO;
						result = new ArrayList<GpackCategoryAlbumInfoVO>();
						for(int i=0; i < album_result.size(); i++){
							tmpVO = (GpackCategoryAlbumInfoVO) album_result.get(i).clone();
							tmpVO.setIs_fh("N");//FullHD 서비스하지 않는 망이므로 무조건N
							result.add(tmpVO);
						}
					}
				}
			}
		}
		
		return result;
	}
	
	
}
