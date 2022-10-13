package com.dmi.smartux.smartstart.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmi.smartux.admin.rank.vo.EcrmRankVO;
import com.dmi.smartux.cache.service.CacheIntervalService;
import com.dmi.smartux.common.exception.SmartUXException;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.service.CacheService;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.smartstart.dao.GenreVodBestListDao;
import com.dmi.smartux.smartstart.service.GenreVodBestListService;
import com.dmi.smartux.smartstart.vo.GenreVodBestListVO;

@Service
public class GenreVodBestListServiceImpl implements GenreVodBestListService {

	private final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	GenreVodBestListDao dao;
	
	@Autowired
	CacheService cacheservice;

	@Autowired
	CacheIntervalService cacheIntervalService;
	
	/**
	 * GenreVodBestList 조회하기 위해 사용되는 정보의 갯수를 조회하는 서비스구현 클래스
	 * @param sa_id			가입번호
	 * @param stb_mac		맥주소
	 * @param app_type		어플타입
	 * @param genre_code	장르코드
	 * @param callByScheduler	스케듈러에서 호출했을 경우 여기 값에 Y가 들어온다. 스케줄러에서 호출한 경우 validation 로직을 타지 않고 바로 서비스를 호출한다
	 * @return				검색된 레코드 총 갯수
	 * @throws Exception
	 */
	@Override
	// @Transactional(readOnly=true)
	public int getGenreVodBestListCount(String sa_id, String stb_mac, String app_type,String start_num, String req_count, String genre_code, String callByScheduler) throws Exception {
		// TODO Auto-generated method stub	
		int result = 0;
		List<GenreVodBestListVO> dbresult = new ArrayList<GenreVodBestListVO>();
		 		
		//long interval = GlobalCom.getCacheInterval(callByScheduler, SmartUXProperties.getProperty("GenreVodBestListDao.getGenreVodBestList.interval"),"GenreVodBestListDao.getGenreVodBestList.interval");
		String intervalStr = "0";
		try{
			intervalStr = Long.toString(cacheIntervalService.getCacheInterval("GenreVodBestListDao.getGenreVodBestList.interval"));
		}catch (Exception e) {
			intervalStr = SmartUXProperties.getProperty("GenreVodBestListDao.getGenreVodBestList.interval");
		}
		long interval = GlobalCom.getCacheInterval(callByScheduler, intervalStr);
		
		Map<String, List<GenreVodBestListVO>> mapresult = (Map<String, List<GenreVodBestListVO>>)cacheservice.getCache(dao
				, "getGenreVodBestList"
				, SmartUXProperties.getProperty("GenreVodBestListDao.getGenreVodBestList.cacheKey")
				, interval
			);
		
			dbresult = mapresult.get(genre_code);
			
			if(dbresult == null){
				
			}else{
				result = dbresult.size();
			}
	
		return result;				
	}
	
	/**
	 * GenreVodBestList 조회하기 위해 사용되는 정보를 조회하는 서비스구현 클래스
	 * @param sa_id			가입번호
	 * @param stb_mac		맥주소
	 * @param app_type		어플타입
	 * @param start_num		검색 시작 인덱스(-1이면 req_count 값을 무시하고 전체를 검색한다)
	 * @param req_count		검색 레코드 갯수
	 * @param genre_code	장르코드
	 * @param callByScheduler	스케듈러에서 호출했을 경우 여기 값에 Y가 들어온다. 스케줄러에서 호출한 경우 validation 로직을 타지 않고 바로 서비스를 호출한다
	 * @return				GenreVodBestList 정보가 들어있는 GenreVodBestListVO 객체가 들어있는 List 객체
	 * @throws Exception
	 */
	@Override
	// @Transactional(readOnly=true)
//	@Transactional 140708
	public List<GenreVodBestListVO> getGenreVodBestList(String sa_id, String stb_mac, String app_type, String start_num, String req_count, String genre_code, int fh_gbn, String callByScheduler, String category_gb) throws Exception {
		// TODO Auto-generated method stub
		List<GenreVodBestListVO> result = new ArrayList<GenreVodBestListVO>();
		List<GenreVodBestListVO> dbresult = new ArrayList<GenreVodBestListVO>();
		
		//long interval = GlobalCom.getCacheInterval(callByScheduler, SmartUXProperties.getProperty("GenreVodBestListDao.getGenreVodBestList.interval"),"GenreVodBestListDao.getGenreVodBestList.interval");
		String intervalStr = "0";
		try{
			intervalStr = Long.toString(cacheIntervalService.getCacheInterval("GenreVodBestListDao.getGenreVodBestList.interval"));
		}catch (Exception e) {
			intervalStr = SmartUXProperties.getProperty("GenreVodBestListDao.getGenreVodBestList.interval");
		}
		long interval = GlobalCom.getCacheInterval(callByScheduler, intervalStr);
		
		Map<String, List<GenreVodBestListVO>> mapresult = (Map<String, List<GenreVodBestListVO>>)cacheservice.getCache(dao
				, "getGenreVodBestList"
				, SmartUXProperties.getProperty("GenreVodBestListDao.getGenreVodBestList.cacheKey")
				, interval
				);
		
		List<GenreVodBestListVO> templist = mapresult.get(genre_code);
		category_gb = GlobalCom.isNull(category_gb, "I20").toUpperCase();
		
		if(templist != null){
			for(GenreVodBestListVO vo : templist){
				if(category_gb.equals(vo.getCategory_gb())){
					dbresult.add(vo);
				}
			}
		}
		
		if(dbresult != null){
		
			if("Y".equals(callByScheduler) || "A".equals(callByScheduler)){			// 스케듈러에서 호출한 경우
				
			}else{										// 단말에서 호출한 경우
				int startnum = Integer.parseInt(start_num);
				
				if(startnum >= 1){						// 1보다 큰 경우는 페이징 기법이 적용되므로 전체 조회한 내용에서 대상되는 레코드만큼만 꺼내서 넣는다
					startnum--;
					int endnum = startnum + Integer.parseInt(req_count);
					
					// endnum 수치가 검색된 리스트의 마지막 인덱스보다 큰지를 조사해서 그거보다 큰 경우엔 endnum을 검색된 리스트의 마지막 인덱스로 셋팅해준다
					if(endnum > dbresult.size()){
						endnum = dbresult.size();
					}
					
					result = new ArrayList<GenreVodBestListVO>();

					if(fh_gbn==1){//망 타입이 FullHD 서비스를 하는 망이라면..
						for(int i=startnum; i < endnum; i++){
							result.add(dbresult.get(i));
						}
					}else{
						GenreVodBestListVO tmpVO;
						for(int i=startnum; i < endnum; i++){
							tmpVO = (GenreVodBestListVO) dbresult.get(i).clone();
							tmpVO.setIs_fh("N");//FullHD 서비스하지 않는 망이므로 무조건N
							result.add(tmpVO);
						}
					}
				}else if(startnum == -1){				// -1인 경우 전체 조회이므로 db 조회 결과를 return 해준다
					if(fh_gbn==1){//망 타입이 FullHD 서비스를 하는 망이라면..
						result = dbresult;
					}else{
						GenreVodBestListVO tmpVO;
						result = new ArrayList<GenreVodBestListVO>();
						for(int i=0; i < dbresult.size(); i++){
							tmpVO = (GenreVodBestListVO) dbresult.get(i).clone();
							tmpVO.setIs_fh("N");//FullHD 서비스하지 않는 망이므로 무조건N
							result.add(tmpVO);
						}
					}
				}
				
				if(result == null || result.size() <= 0 ){//맵이 있냐없냐가 아니라 일단 데이터가 없다면 에러로!!130906
					SmartUXException ex = new SmartUXException();
					ex.setFlag(SmartUXProperties.getProperty("flag.beNotData"));
					ex.setMessage(SmartUXProperties.getProperty("message.beNotData"));
					throw ex;
				}
			}
		}
		return result;
	}	
	
	/**
	 * GenreVodBestList 조회하기 위해 사용되는 정보를 사전체크하는 서비스구현 클래스
	 * @return				EcrmRank 정보가 들어있는 EcrmRankVO 객체가 들어있는 List 객체
	 * @throws Exception
	 */
	@Override
	public List<EcrmRankVO> getCheckVODList() throws Exception {
		// TODO Auto-generated method stub
		return dao.getCheckVODList();
	}
	
	
}



