package com.dmi.smartux.smartstart.service.impl;

import java.util.ArrayList;
import java.util.List;

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
import com.dmi.smartux.smartstart.dao.SUXMAlbumListDao;
import com.dmi.smartux.smartstart.service.SUXMAlbumListService;
import com.dmi.smartux.smartstart.vo.SUXMAlbumListVO;

@Service
public class SUXMAlbumListServiceImpl implements SUXMAlbumListService {

	private final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	SUXMAlbumListDao dao;
	
	@Autowired
	CacheService cacheservice;
	
	@Autowired
	CacheIntervalService cacheIntervalService;

	/**
	 * SUXMAlbumList 조회하기 위해 사용되는 정보의 갯수를 조회하는 서비스구현 클래스
	 * @param sa_id			가입번호
	 * @param stb_mac		맥주소
	 * @param app_type		어플타입
	 * @param schedule_code	자체편성코드
	 * @param callByScheduler	스케듈러에서 호출했을 경우 여기 값에 Y가 들어온다. 스케줄러에서 호출한 경우 validation 로직을 타지 않고 바로 서비스를 호출한다
	 * @return				검색된 레코드 총 갯수
	 * @throws Exception
	 */
	@Override
	// @Transactional(readOnly=true)
//	@Transactional 140708
	public int getSUXMAlbumListCount(String sa_id, String stb_mac, String app_type, String schedule_code, String callByScheduler) throws Exception {
												
		//long interval = GlobalCom.getCacheInterval(callByScheduler, SmartUXProperties.getProperty("SUXMAlbumListDao.getSUXMAlbumList.interval"),"SUXMAlbumListDao.getSUXMAlbumList.interval");
		String intervalStr = "0";
		try{
			intervalStr = Long.toString(cacheIntervalService.getCacheInterval("SUXMAlbumListDao.getSUXMAlbumList.interval"));
		}catch (Exception e) {
			intervalStr = SmartUXProperties.getProperty("SUXMAlbumListDao.getSUXMAlbumList.interval");
		}
		long interval = GlobalCom.getCacheInterval(callByScheduler, intervalStr);
			
		List<SUXMAlbumListVO> dbresult = (List<SUXMAlbumListVO>)cacheservice.getCache	(dao, "getSUXMAlbumList"
				, SmartUXProperties.getProperty("SUXMAlbumListDao.getSUXMAlbumList.cacheKey")
				, interval
			, schedule_code
			);
		
		int result = 0;
		if(dbresult != null){
			result = dbresult.size(); 
		}
		
		if(result <= 0){//여기까지 왔다는건 맵은 있다는 얘기!!! 하지만 데이터는 없다!!130906
			SmartUXException ex = new SmartUXException();
			ex.setFlag(SmartUXProperties.getProperty("flag.beNotData"));
			ex.setMessage(SmartUXProperties.getProperty("message.beNotData"));
			throw ex;
		}
		
		return result;				
	}
	
	/**
	 * SUXMAlbumList 조회하기 위해 사용되는 정보를 조회하는 서비스구현 클래스
	 * @param sa_id			가입번호
	 * @param stb_mac		맥주소
	 * @param app_type		어플타입
	 * @param schedule_code	자체편성코드
	 * @param start_num		검색 시작 인덱스(-1이면 req_count 값을 무시하고 전체를 검색한다)
	 * @param req_count		검색 레코드 갯수 
	 * @param callByScheduler	스케듈러에서 호출했을 경우 여기 값에 Y가 들어온다. 스케줄러에서 호출한 경우 validation 로직을 타지 않고 바로 서비스를 호출한다
	 * @return				SmartStartItemList 정보가 들어있는 SmartStartItemListVO 객체가 들어있는 List 객체
	 * @throws Exception
	 */
	@Override
	// @Transactional(readOnly=true)
//	@Transactional 140708
	public List<SUXMAlbumListVO> getSUXMAlbumList(String sa_id, String stb_mac, String app_type, String start_num, String req_count, String schedule_code, int fh_gbn, String callByScheduler) throws Exception {
		
		//long interval = GlobalCom.getCacheInterval(callByScheduler, SmartUXProperties.getProperty("SUXMAlbumListDao.getSUXMAlbumList.interval"),"SUXMAlbumListDao.getSUXMAlbumList.interval");
		String intervalStr = "0";
		try{
			intervalStr = Long.toString(cacheIntervalService.getCacheInterval("SUXMAlbumListDao.getSUXMAlbumList.interval"));
		}catch (Exception e) {
			intervalStr = SmartUXProperties.getProperty("SUXMAlbumListDao.getSUXMAlbumList.interval");
		}
		long interval = GlobalCom.getCacheInterval(callByScheduler, intervalStr);
		
		List<SUXMAlbumListVO> dbresult = (List<SUXMAlbumListVO>)cacheservice.getCache	(dao, "getSUXMAlbumList"
				, SmartUXProperties.getProperty("SUXMAlbumListDao.getSUXMAlbumList.cacheKey")
				, interval
			, schedule_code
			);
		
		List<SUXMAlbumListVO> result = null;
		// 스케듈러에서 호출한 경우와 단말에서 호출한 경우를 구분해서 작업을 진행하도록 한다
		if("Y".equals(callByScheduler)||"A".equals(callByScheduler)){			// 스케듈러에서 호출한 경우
					
		}else{	
			int startnum = Integer.parseInt(start_num);
				
			if(startnum >= 1){						// 1보다 큰 경우는 페이징 기법이 적용되므로 전체 조회한 내용에서 대상되는 레코드만큼만 꺼내서 넣는다
				startnum--;
				int endnum = startnum + Integer.parseInt(req_count);
				
				// endnum 수치가 검색된 리스트의 마지막 인덱스보다 큰지를 조사해서 그거보다 큰 경우엔 endnum을 검색된 리스트의 마지막 인덱스로 셋팅해준다
				if(endnum > dbresult.size()){
					endnum = dbresult.size();
				}
				
				result = new ArrayList<SUXMAlbumListVO>();
				logger.debug("start_num : " + start_num);
				logger.debug("req_count : " + req_count);
				logger.debug("startnum : " + startnum);
				logger.debug("endnum : " + endnum);
				
				if(fh_gbn==1){//망 타입이 FullHD 서비스를 하는 망이라면..
					for(int i=startnum; i < endnum; i++){
						result.add(dbresult.get(i));
					}
				}else{
					SUXMAlbumListVO tmpVO;
					for(int i=startnum; i < endnum; i++){
						tmpVO = (SUXMAlbumListVO) dbresult.get(i).clone();
						tmpVO.setIs_fh("N");//FullHD 서비스하지 않는 망이므로 무조건N
						result.add(tmpVO);
					}
				}
				
				
			}else if(startnum == -1){				// -1인 경우 전체 조회이므로 db 조회 결과를 return 해준다
				if(fh_gbn==1){//망 타입이 FullHD 서비스를 하는 망이라면..
					result = dbresult;
				}else{
					SUXMAlbumListVO tmpVO;
					result = new ArrayList<SUXMAlbumListVO>();
					for(int i=0; i < dbresult.size(); i++){
						tmpVO = (SUXMAlbumListVO) dbresult.get(i).clone();
						tmpVO.setIs_fh("N");//FullHD 서비스하지 않는 망이므로 무조건N
						result.add(tmpVO);
					}
				}
			}
				
		}
		return result;
	}

	/**
	 * SUXMAlbumList 조회하기 위해 사용되는 사전정보를 체크하는 서비스구현 클래스
	 * @return				EcrmRank 정보가 들어있는 EcrmRankVO 클래스가 들어있는 List 객체
	 * @throws Exception
	 */
	@Override
	public List<EcrmRankVO> getCheckSCHEDULEList() throws Exception {
		// TODO Auto-generated method stub
		return dao.getCheckSCHEDULEList();
	}
		
	/**
	 * SUXMAlbumList 조회하기 위해 사용되는 사전코드정보를 체크하는 서비스구현 클래스
	 * @param schedule_code	자체편성코드
	 * @return				EcrmRank 정보가 들어있는 EcrmRankVO 클래스가 들어있는 List 객체
	 * @throws Exception
	 */
	@Override
	public List<EcrmRankVO> getCheckScheduleCodelist(String schedule_code) throws Exception {
		// TODO Auto-generated method stub
		return dao.getCheckScheduleCodelist(schedule_code);
	}	
}
