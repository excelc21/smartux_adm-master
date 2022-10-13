package com.dmi.smartux.mainpanel.service.impl;

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
import com.dmi.smartux.mainpanel.dao.MainPanelDao;
import com.dmi.smartux.mainpanel.service.MainPanelService;
import com.dmi.smartux.mainpanel.vo.AlbumInfoVO;
import com.dmi.smartux.mainpanel.vo.MainPanelInfoVO;

@Service
public class MainPanelServiceImpl implements MainPanelService {

	@Autowired
	MainPanelDao dao;
	
	@Autowired
	CacheService cacheservice;
	
	@Autowired
	CacheIntervalService cacheIntervalService;
	
	@Override
	// @Transactional(readOnly=true)
	public String getMainPanelVersionInfo(String sa_id, String stb_mac, String app_type, String callByScheduler) throws Exception {
		// TODO Auto-generated method stub
		
		//long interval = GlobalCom.getCacheInterval(callByScheduler, SmartUXProperties.getProperty("MainPanelDao.getMainPanelVersionInfo.interval"),"MainPanelDao.getMainPanelVersionInfo.interval");
		String intervalStr = "0";
		try{
			intervalStr = Long.toString(cacheIntervalService.getCacheInterval("MainPanelDao.getMainPanelVersionInfo.interval"));
		}catch (Exception e) {
			intervalStr = SmartUXProperties.getProperty("MainPanelDao.getMainPanelVersionInfo.interval");
		}
		long interval = GlobalCom.getCacheInterval(callByScheduler, intervalStr);
		
		// Main Panel Version 정보는 관리자가 확인 작업을 할때만 캐쉬가 갱신이 되므로 
		// 셋탑이 조회할때는 갱신주기를 0으로 주어 DB에서 다시 읽어서 캐쉬를 갱신하는 작업을 하지 않도록 한다 
		String result = (String)cacheservice.getCache(dao
				, "getMainPanelVersionInfo"
				, SmartUXProperties.getProperty("MainPanelDao.getMainPanelVersionInfo.cacheKey")
				, interval
				);
		return result;
	}

	@Override
	// @Transactional(readOnly=true)
	public List<MainPanelInfoVO> getMainPanelInterlockingInfo(String sa_id, String stb_mac, String app_type, String panel_id, String callByScheduler) throws Exception {
		// TODO Auto-generated method stub
		
		//long interval = GlobalCom.getCacheInterval(callByScheduler, SmartUXProperties.getProperty("MainPanelDao.getMainPanelInterlockingInfo.interval"),"MainPanelDao.getMainPanelInterlockingInfo.interval");
		String intervalStr = "0";
		try{
			intervalStr = Long.toString(cacheIntervalService.getCacheInterval("MainPanelDao.getMainPanelInterlockingInfo.interval"));
		}catch (Exception e) {
			intervalStr = SmartUXProperties.getProperty("MainPanelDao.getMainPanelInterlockingInfo.interval");
		}
		long interval = GlobalCom.getCacheInterval(callByScheduler, intervalStr);
		
		
		// Main Panel 연동 정보는 관리자가 확인 작업을 할때만 캐쉬가 갱신이 되므로 
		// 셋탑이 조회할때는 갱신주기를 0으로 주어 DB에서 다시 읽어서 캐쉬를 갱신하는 작업을 하지 않도록 한다 
		List<MainPanelInfoVO> result = new ArrayList<MainPanelInfoVO>();
		
		List<MainPanelInfoVO> tmpresult = (List<MainPanelInfoVO>)cacheservice.getCache(dao
				, "getMainPanelInterlockingInfo"
				, SmartUXProperties.getProperty("MainPanelDao.getMainPanelInterlockingInfo.cacheKey")
				, interval
				);
		
		if("".equals(panel_id)){			// 전체 패널 조회일 경우
			result = tmpresult;
		}else{								// 특정 패널 조회일 경우
			for(MainPanelInfoVO item : tmpresult){
				String code = item.getCode();			// 코드는 pannel_id_title_id 형태로 되어 있다
				String [] tmps = code.split("\\_");
				if(panel_id.equals(tmps[0])){
					result.add(item);
				}
			}
		}
		
		
		return result;
		
		
		// 더미 시작
		/*
		List<MainPanelInfoVO> result = new ArrayList<MainPanelInfoVO>();
		
		MainPanelInfoVO info1 = new MainPanelInfoVO();
		info1.setCode("0001");
		info1.setP_code("CP01");
		info1.setPanelviewname("테스트뷰1");
		info1.setCategory_code("");
		info1.setOrder_seq("1");
		result.add(info1);
		
		MainPanelInfoVO info2 = new MainPanelInfoVO();
		info2.setCode("0002");
		info2.setP_code("0001");
		info2.setPanelviewname("테스트뷰1-1");
		info2.setCategory_code("M001");
		info2.setOrder_seq("1");
		result.add(info2);
		
		MainPanelInfoVO info3 = new MainPanelInfoVO();
		info3.setCode("0003");
		info3.setP_code("0001");
		info3.setPanelviewname("테스트뷰1-2");
		info3.setCategory_code("M002");
		info3.setOrder_seq("2");
		result.add(info3);
		
		MainPanelInfoVO info4 = new MainPanelInfoVO();
		info4.setCode("0004");
		info4.setP_code("CP01");
		info4.setPanelviewname("테스트뷰2");
		info4.setCategory_code("M003");
		info4.setOrder_seq("2");
		result.add(info4);
		
		MainPanelInfoVO info5 = new MainPanelInfoVO();
		info5.setCode("0005");
		info5.setP_code("CP01");
		info5.setPanelviewname("테스트뷰3");
		info5.setCategory_code("");
		info5.setOrder_seq("3");
		result.add(info5);
		
		MainPanelInfoVO info6 = new MainPanelInfoVO();
		info6.setCode("0006");
		info6.setP_code("0005");
		info6.setPanelviewname("테스트뷰3-1");
		info6.setCategory_code("M004");
		info6.setOrder_seq("1");
		result.add(info6);
		
		MainPanelInfoVO info7 = new MainPanelInfoVO();
		info7.setCode("0008");
		info7.setP_code("CP02");
		info7.setPanelviewname("테스트뷰4");
		info7.setCategory_code("");
		info7.setOrder_seq("1");
		result.add(info7);
		
		MainPanelInfoVO info8 = new MainPanelInfoVO();
		info8.setCode("0009");
		info8.setP_code("0008");
		info8.setPanelviewname("테스트뷰4-1");
		info8.setCategory_code("M005");
		info8.setOrder_seq("1");
		result.add(info8);
		
		MainPanelInfoVO info9 = new MainPanelInfoVO();
		info9.setCode("0010");
		info9.setP_code("0008");
		info9.setPanelviewname("테스트뷰4-2");
		info9.setCategory_code("M006");
		info9.setOrder_seq("2");
		result.add(info9);
		
		MainPanelInfoVO info10 = new MainPanelInfoVO();
		info10.setCode("0011");
		info10.setP_code("CP02");
		info10.setPanelviewname("테스트뷰5");
		info10.setCategory_code("M007");
		info10.setOrder_seq("2");
		result.add(info10);
		
		MainPanelInfoVO info11 = new MainPanelInfoVO();
		info11.setCode("0012");
		info11.setP_code("CP02");
		info11.setPanelviewname("테스트뷰6");
		info11.setCategory_code("");
		info11.setOrder_seq("3");
		result.add(info11);
		
		MainPanelInfoVO info12 = new MainPanelInfoVO();
		info12.setCode("0013");
		info12.setP_code("0012");
		info12.setPanelviewname("테스트뷰6-1");
		info12.setCategory_code("M008");
		info12.setOrder_seq("1");
		result.add(info12);
		return result;
		*/
		// 더미 끝
	}
	
	@Override
	//@Transactional 140708
	public int getI20AlbumListCount(String sa_id, String stb_mac,	String app_type, String category_id, String start_num, String req_count, String callByScheduler) throws Exception {
		// TODO Auto-generated method stub
		int result = 0;
		List<AlbumInfoVO> dbresult = new ArrayList<AlbumInfoVO>();
		
		//long interval = GlobalCom.getCacheInterval(callByScheduler, SmartUXProperties.getProperty("MainPanelDao.getI20AlbumList.interval"),"MainPanelDao.getI20AlbumList.interval");
		String intervalStr = "0";
		try{
			intervalStr = Long.toString(cacheIntervalService.getCacheInterval("MainPanelDao.getI20AlbumList.interval"));
		}catch (Exception e) {
			intervalStr = SmartUXProperties.getProperty("MainPanelDao.getI20AlbumList.interval");
		}
		long interval = GlobalCom.getCacheInterval(callByScheduler, intervalStr);
		
		// Main Panel 연동 정보는 관리자가 확인 작업을 할때만 캐쉬가 갱신이 되므로 
		// 셋탑이 조회할때는 갱신주기를 0으로 주어 DB에서 다시 읽어서 캐쉬를 갱신하는 작업을 하지 않도록 한다 
		
		
		Map<String, List<AlbumInfoVO>> mapresult = (Map<String, List<AlbumInfoVO>>)cacheservice.getCache(dao
				, "getI20AlbumList"
				, SmartUXProperties.getProperty("MainPanelDao.getI20AlbumList.cacheKey")
				, interval
				);
		
		// Map<String, List<AlbumInfoVO>> mapresult = dao.getI20AlbumList();
		dbresult = mapresult.get(category_id);
		
		if(dbresult == null){
			//
		}else{
			result = dbresult.size();
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
	public List<AlbumInfoVO> getI20AlbumList(String sa_id, String stb_mac, String app_type, String category_id, String start_num, String req_count, int fh_gbn, String callByScheduler) throws Exception {
		// TODO Auto-generated method stub
		List<AlbumInfoVO> result = new ArrayList<AlbumInfoVO>();
		List<AlbumInfoVO> dbresult = new ArrayList<AlbumInfoVO>();
		
		//long interval = GlobalCom.getCacheInterval(callByScheduler, SmartUXProperties.getProperty("MainPanelDao.getI20AlbumList.interval"),"MainPanelDao.getI20AlbumList.interval");
		String intervalStr = "0";
		try{
			intervalStr = Long.toString(cacheIntervalService.getCacheInterval("MainPanelDao.getI20AlbumList.interval"));
		}catch (Exception e) {
			intervalStr = SmartUXProperties.getProperty("MainPanelDao.getI20AlbumList.interval");
		}
		long interval = GlobalCom.getCacheInterval(callByScheduler, intervalStr);
		
		// Main Panel 연동 정보는 관리자가 확인 작업을 할때만 캐쉬가 갱신이 되므로 
		// 셋탑이 조회할때는 갱신주기를 0으로 주어 DB에서 다시 읽어서 캐쉬를 갱신하는 작업을 하지 않도록 한다 
		
		Map<String, List<AlbumInfoVO>> mapresult = (Map<String, List<AlbumInfoVO>>)cacheservice.getCache(dao
				, "getI20AlbumList"
				, SmartUXProperties.getProperty("MainPanelDao.getI20AlbumList.cacheKey")
				, interval
				);
		
		// Map<String, List<AlbumInfoVO>> mapresult = dao.getI20AlbumList();
		dbresult = mapresult.get(category_id);
		
		if(dbresult != null){
		
			if("Y".equals(callByScheduler)){			// 스케듈러에서 호출한 경우
				
			}else{										// 단말에서 호출한 경우
				int startnum = Integer.parseInt(start_num);
				if(startnum >= 1){						// 1보다 큰 경우는 페이징 기법이 적용되므로 전체 조회한 내용에서 대상되는 레코드만큼만 꺼내서 넣는다
					startnum--;
					int endnum = startnum + Integer.parseInt(req_count);
					
					// endnum 수치가 검색된 리스트의 마지막 인덱스보다 큰지를 조사해서 그거보다 큰 경우엔 endnum을 검색된 리스트의 마지막 인덱스로 셋팅해준다
					if(endnum > dbresult.size()){
						endnum = dbresult.size();
					}
					
					result = new ArrayList<AlbumInfoVO>();
					
					
					if(fh_gbn==1){//망 타입이 FullHD 서비스를 하는 망이라면..
						for(int i=startnum; i < endnum; i++){
							result.add(dbresult.get(i));
						}
					}else{
						AlbumInfoVO tmpVO;
						for(int i=startnum; i < endnum; i++){
							tmpVO = (AlbumInfoVO) dbresult.get(i).clone();
							tmpVO.setIs_fh("N");//FullHD 서비스하지 않는 망이므로 무조건N
							result.add(tmpVO);
						}
					}
				}else if(startnum == -1){				// -1인 경우 전체 조회이므로 db 조회 결과를 return 해준다
					if(fh_gbn==1){//망 타입이 FullHD 서비스를 하는 망이라면..
						result = dbresult;
					}else{
						AlbumInfoVO tmpVO;
						result = new ArrayList<AlbumInfoVO>();
						for(int i=0; i < dbresult.size(); i++){
							tmpVO = (AlbumInfoVO) dbresult.get(i).clone();
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
