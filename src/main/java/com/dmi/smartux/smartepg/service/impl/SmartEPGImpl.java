package com.dmi.smartux.smartepg.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmi.smartux.cache.service.CacheIntervalService;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.service.CacheService;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.smartepg.dao.SmartEPGDao;
import com.dmi.smartux.smartepg.service.SmartEPGService;
import com.dmi.smartux.smartepg.vo.RealRatingInfoThemeVO;
import com.dmi.smartux.smartepg.vo.RealRatingInfoVO;
import com.dmi.smartux.smartepg.vo.ThemeInfoVO;

@Service
public class SmartEPGImpl implements SmartEPGService {

	private final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	SmartEPGDao dao;
	
	@Autowired
	CacheService cacheservice;
	
	@Autowired
	CacheIntervalService cacheIntervalService;
	
	@Override
	// @Transactional(readOnly=true)
	public int getThemeInfoCount(String sa_id, String stb_mac, String app_type, String callByScheduler) throws Exception {
		// TODO Auto-generated method stub
		//long interval = GlobalCom.getCacheInterval(callByScheduler, SmartUXProperties.getProperty("SmartEPGDao.getThemeInfo.interval"),"SmartEPGDao.getThemeInfo.interval");
		String intervalStr = "0";
		try{
			intervalStr = Long.toString(cacheIntervalService.getCacheInterval("SmartEPGDao.getThemeInfo.interval"));
		}catch (Exception e) {
			intervalStr = SmartUXProperties.getProperty("SmartEPGDao.getThemeInfo.interval");
		}
		long interval = GlobalCom.getCacheInterval(callByScheduler, intervalStr);
		
		List<ThemeInfoVO> dbresult = (List<ThemeInfoVO>)cacheservice.getCache(dao
				, "getThemeInfo"
				, SmartUXProperties.getProperty("SmartEPGDao.getThemeInfo.cacheKey")
				, interval
				);
		int result = 0;
		if(dbresult != null){
			result = dbresult.size(); 
		}
		
		return result;
		
		
		// 더미 시작
		// return 4;
	}


	@Override
	// @Transactional(readOnly=true)
	public List<ThemeInfoVO> getThemeInfo(String sa_id, String stb_mac, String app_type, String start_num, String req_count, String callByScheduler)	throws Exception {
		// TODO Auto-generated method stub
		//long interval = GlobalCom.getCacheInterval(callByScheduler, SmartUXProperties.getProperty("SmartEPGDao.getThemeInfo.interval"),"SmartEPGDao.getThemeInfo.interval");
		String intervalStr = "0";
		try{
			intervalStr = Long.toString(cacheIntervalService.getCacheInterval("SmartEPGDao.getThemeInfo.interval"));
		}catch (Exception e) {
			intervalStr = SmartUXProperties.getProperty("SmartEPGDao.getThemeInfo.interval");
		}
		long interval = GlobalCom.getCacheInterval(callByScheduler, intervalStr);
		
		List<ThemeInfoVO> dbresult = (List<ThemeInfoVO>)cacheservice.getCache(dao
				, "getThemeInfo"
				, SmartUXProperties.getProperty("SmartEPGDao.getThemeInfo.cacheKey")
				, interval
				);
		List<ThemeInfoVO> result = null;
		
		// 스케듈러에서 호출한 경우와 단말에서 호출한 경우를 구분해서 작업을 진행하도록 한다
		if("Y".equals(callByScheduler)){			// 스케듈러에서 호출한 경우
			
		}else if("A".equals(callByScheduler)){			// 스케듈러에서 호출한 경우
			
		}else{										// 단말에서 호출한 경우
			int startnum = Integer.parseInt(start_num);
			if(startnum >= 1){						// 1보다 큰 경우는 페이징 기법이 적용되므로 전체 조회한 내용에서 대상되는 레코드만큼만 꺼내서 넣는다
				startnum--;
				int endnum = startnum + Integer.parseInt(req_count);
				
				// endnum 수치가 검색된 리스트의 마지막 인덱스보다 큰지를 조사해서 그거보다 큰 경우엔 endnum을 검색된 리스트의 마지막 인덱스로 셋팅해준다
				if(endnum > dbresult.size()){
					endnum = dbresult.size();
				}
				
				result = new ArrayList<ThemeInfoVO>();
				logger.debug("start_num : " + start_num);
				logger.debug("req_count : " + req_count);
				logger.debug("startnum : " + startnum);
				logger.debug("endnum : " + endnum);
				for(int i=startnum; i < endnum; i++){
					result.add(dbresult.get(i));
				}
			}else if(startnum == -1){				// -1인 경우 전체 조회이므로 db 조회 결과를 return 해준다
				result = dbresult;
			}
			
			if(result == null){
				logger.debug("result is null");
			}else{
				logger.debug("result is not null : " + result.size());
			}
		}
		
		// 더미 시작
		/*
		List<ThemeInfoVO> dbresult = new ArrayList<ThemeInfoVO>();
		List<ThemeInfoVO> result = null;
		
		ThemeInfoVO info1 = new ThemeInfoVO();
		info1.setTheme_code("01");
		info1.setTheme_name("드라마");
		dbresult.add(info1);
		ThemeInfoVO info2 = new ThemeInfoVO();
		info2.setTheme_code("02");
		info2.setTheme_name("영화");
		dbresult.add(info2);
		ThemeInfoVO info3 = new ThemeInfoVO();
		info3.setTheme_code("03");
		info3.setTheme_name("예능");
		dbresult.add(info3);
		ThemeInfoVO info4 = new ThemeInfoVO();
		info4.setTheme_code("04");
		info4.setTheme_name("스포츠");
		dbresult.add(info4);
		
		
		int startnum = Integer.parseInt(start_num);
		
		if(startnum >= 1){						// 1보다 큰 경우는 페이징 기법이 적용되므로 전체 조회한 내용에서 대상되는 레코드만큼만 꺼내서 넣는다
			startnum--;
			int endnum = startnum + Integer.parseInt(req_count);
			
			// endnum 수치가 검색된 리스트의 마지막 인덱스보다 큰지를 조사해서 그거보다 큰 경우엔 endnum을 검색된 리스트의 마지막 인덱스로 셋팅해준다
			if(endnum > dbresult.size()){
				endnum = dbresult.size();
			}
			
			result = new ArrayList<ThemeInfoVO>();
			logger.debug("start_num : " + start_num);
			logger.debug("req_count : " + req_count);
			logger.debug("startnum : " + startnum);
			logger.debug("endnum : " + endnum);
			for(int i=startnum; i < endnum; i++){
				result.add(dbresult.get(i));
			}
		}else if(startnum == -1){				// -1인 경우 전체 조회이므로 db 조회 결과를 return 해준다
			result = dbresult;
		}
		*/
		// 더미 끝
		
		return result;
	}

	

	@Override
	// @Transactional(readOnly=true)
	public int getRealRatingByThemeCount(String sa_id, String stb_mac,	String app_type, String theme_code, String callByScheduler) throws Exception {
		// TODO Auto-generated method stub
		
		//long interval = GlobalCom.getCacheInterval(callByScheduler, SmartUXProperties.getProperty("SmartEPGDao.getRealRatingByTheme.interval"),"SmartEPGDao.getRealRatingByTheme.interval");
		String intervalStr = "0";
		try{
			intervalStr = Long.toString(cacheIntervalService.getCacheInterval("SmartEPGDao.getRealRatingByTheme.interval"));
		}catch (Exception e) {
			intervalStr = SmartUXProperties.getProperty("SmartEPGDao.getRealRatingByTheme.interval");
		}
		long interval = GlobalCom.getCacheInterval(callByScheduler, intervalStr);
		
		List<RealRatingInfoThemeVO> dbresult = (List<RealRatingInfoThemeVO>)cacheservice.getCache(dao
				, "getRealRatingByTheme"
				, SmartUXProperties.getProperty("SmartEPGDao.getRealRatingByTheme.cacheKey")
				, interval
				, theme_code);
		int result = 0;
		if(dbresult != null){
			result = dbresult.size(); 
		}
		return result;
		
		// return 5;
	}


	@Override
	// @Transactional(readOnly=true)
	public List<RealRatingInfoThemeVO> getRealRatingByTheme(String sa_id, String stb_mac, String app_type, String start_num, String req_count, String theme_code, String callByScheduler) throws Exception {
		// TODO Auto-generated method stub
		
		//long interval = GlobalCom.getCacheInterval(callByScheduler, SmartUXProperties.getProperty("SmartEPGDao.getRealRatingByTheme.interval"),"SmartEPGDao.getRealRatingByTheme.interval");
		String intervalStr = "0";
		try{
			intervalStr = Long.toString(cacheIntervalService.getCacheInterval("SmartEPGDao.getRealRatingByTheme.interval"));
		}catch (Exception e) {
			intervalStr = SmartUXProperties.getProperty("SmartEPGDao.getRealRatingByTheme.interval");
		}
		long interval = GlobalCom.getCacheInterval(callByScheduler, intervalStr);
		
		List<RealRatingInfoThemeVO> dbresult = (List<RealRatingInfoThemeVO>)cacheservice.getCache(dao
				, "getRealRatingByTheme"
				, SmartUXProperties.getProperty("SmartEPGDao.getRealRatingByTheme.cacheKey")
				, interval
				, theme_code);
		List<RealRatingInfoThemeVO> result = null;
		// 스케듈러에서 호출한 경우와 단말에서 호출한 경우를 구분해서 작업을 진행하도록 한다
		if("Y".equals(callByScheduler)){			// 스케듈러에서 호출한 경우
					
		}else if("A".equals(callByScheduler)){			// 스케듈러에서 호출한 경우
			
		}else{										// 단말에서 호출한 경우
			int startnum = Integer.parseInt(start_num);
				
			if(startnum >= 1){						// 1보다 큰 경우는 페이징 기법이 적용되므로 전체 조회한 내용에서 대상되는 레코드만큼만 꺼내서 넣는다
				startnum--;
				int endnum = startnum + Integer.parseInt(req_count);
				
				// endnum 수치가 검색된 리스트의 마지막 인덱스보다 큰지를 조사해서 그거보다 큰 경우엔 endnum을 검색된 리스트의 마지막 인덱스로 셋팅해준다
				if(endnum > dbresult.size()){
					endnum = dbresult.size();
				}
				
				result = new ArrayList<RealRatingInfoThemeVO>();
				for(int i=startnum; i < endnum; i++){
					result.add(dbresult.get(i));
				}
			}else if(startnum == -1){				// -1인 경우 전체 조회이므로 db 조회 결과를 return 해준다
				result = dbresult;
			}
		}

		return result;
		
		
		// 더미 시작
		/*
		List<RealRatingInfoThemeVO> dbresult = new ArrayList<RealRatingInfoThemeVO>();
		List<RealRatingInfoThemeVO> result = null;
		
		RealRatingInfoThemeVO info1 = new RealRatingInfoThemeVO();
		info1.setService_id("01");
		info1.setChannel_no("ch11");
		info1.setChannel_name("우리채널");
		info1.setProgram_id("10");
		info1.setProgram_name("테마01프로그램10");
		info1.setDefin_flag("SD");
		info1.setProgram_info("01");
		info1.setProgram_stime("20120329");
		info1.setProgram_etime("20120330");
		info1.setSeries_flag("Y");
		info1.setSeries_desc("01회");
		info1.setImg_url("imgurl");
		dbresult.add(info1);
		
		RealRatingInfoThemeVO info2 = new RealRatingInfoThemeVO();
		info2.setService_id("02");
		info2.setChannel_no("ch12");
		info2.setChannel_name("우리채널");
		info2.setProgram_id("10");
		info2.setProgram_name("테마02프로그램10");
		info2.setDefin_flag("HD");
		info2.setProgram_info("01");
		info2.setProgram_stime("20120329");
		info2.setProgram_etime("20120330");
		info2.setSeries_flag("Y");
		info2.setSeries_desc("01회");
		info2.setImg_url("imgurl");
		dbresult.add(info2);
		
		RealRatingInfoThemeVO info3 = new RealRatingInfoThemeVO();
		info3.setService_id("03");
		info3.setChannel_no("ch13");
		info3.setChannel_name("우리채널");
		info3.setProgram_id("10");
		info3.setProgram_name("테마03프로그램10");
		info3.setDefin_flag("3D");
		info3.setProgram_info("01");
		info3.setProgram_stime("20120329");
		info3.setProgram_etime("20120330");
		info3.setSeries_flag("Y");
		info3.setSeries_desc("01회");
		info3.setImg_url("imgurl");
		dbresult.add(info3);
		
		RealRatingInfoThemeVO info4 = new RealRatingInfoThemeVO();
		info4.setService_id("04");
		info4.setChannel_no("ch14");
		info4.setChannel_name("우리채널");
		info4.setProgram_id("10");
		info4.setProgram_name("테마04프로그램10");
		info4.setDefin_flag("SD");
		info4.setProgram_info("01");
		info4.setProgram_stime("20120329");
		info4.setProgram_etime("20120330");
		info4.setSeries_flag("Y");
		info4.setSeries_desc("01회");
		info4.setImg_url("imgurl");
		dbresult.add(info4);
		
		RealRatingInfoThemeVO info5 = new RealRatingInfoThemeVO();
		info5.setService_id("05");
		info5.setChannel_no("ch15");
		info5.setChannel_name("우리채널");
		info5.setProgram_id("10");
		info5.setProgram_name("테마05프로그램10");
		info5.setDefin_flag("SD");
		info5.setProgram_info("01");
		info5.setProgram_stime("20120329");
		info5.setProgram_etime("20120330");
		info5.setSeries_flag("Y");
		info5.setSeries_desc("01회");
		info5.setImg_url("imgurl");
		dbresult.add(info5);
		
		int startnum = Integer.parseInt(start_num);
		
		if(startnum >= 1){						// 1보다 큰 경우는 페이징 기법이 적용되므로 전체 조회한 내용에서 대상되는 레코드만큼만 꺼내서 넣는다
			startnum--;
			int endnum = startnum + Integer.parseInt(req_count);
			
			// endnum 수치가 검색된 리스트의 마지막 인덱스보다 큰지를 조사해서 그거보다 큰 경우엔 endnum을 검색된 리스트의 마지막 인덱스로 셋팅해준다
			if(endnum > dbresult.size()){
				endnum = dbresult.size();
			}
			
			result = new ArrayList<RealRatingInfoThemeVO>();
			for(int i=startnum; i < endnum; i++){
				result.add(dbresult.get(i));
			}
		}else if(startnum == -1){				// -1인 경우 전체 조회이므로 db 조회 결과를 return 해준다
			result = dbresult;
		}
		
		return result;
		*/
		// 더미 끝
	}


	@Override
	// @Transactional(readOnly=true)
	public int getRealRatingCount(String sa_id, String stb_mac, String app_type, String localareacode, String callByScheduler) throws Exception {
		// TODO Auto-generated method stub
		//long interval = GlobalCom.getCacheInterval(callByScheduler, SmartUXProperties.getProperty("SmartEPGDao.getRealRating.interval"),"SmartEPGDao.getRealRating.interval");
		String intervalStr = "0";
		try{
			intervalStr = Long.toString(cacheIntervalService.getCacheInterval("SmartEPGDao.getRealRating.interval"));
		}catch (Exception e) {
			intervalStr = SmartUXProperties.getProperty("SmartEPGDao.getRealRating.interval");
		}
		long interval = GlobalCom.getCacheInterval(callByScheduler, intervalStr);
		
		List<RealRatingInfoVO> dbresult = (List<RealRatingInfoVO>)cacheservice.getCache(dao
				, "getRealRating"
				, SmartUXProperties.getProperty("SmartEPGDao.getRealRating.cacheKey")
				, interval
				);
		int result = 0;
		
		
		
		if(dbresult != null){
			
			// 단말이 전달한 지역코드것으로만 따로 뽑는다
			List<RealRatingInfoVO> arearesult = getArearesult(dbresult, localareacode);
			result = arearesult.size(); 
		}
		return result;
		
		// return 5;
	}


	@Override
	// @Transactional(readOnly=true)
//	@Transactional 140708
	public List<RealRatingInfoVO> getRealRating(String sa_id, String stb_mac, String app_type, String start_num, String req_count, String localareacode, String callByScheduler)
			throws Exception {
		// TODO Auto-generated method stub
		//long interval = GlobalCom.getCacheInterval(callByScheduler, SmartUXProperties.getProperty("SmartEPGDao.getRealRating.interval"),"SmartEPGDao.getRealRating.interval");
		String intervalStr = "0";
		try{
			intervalStr = Long.toString(cacheIntervalService.getCacheInterval("SmartEPGDao.getRealRating.interval"));
		}catch (Exception e) {
			intervalStr = SmartUXProperties.getProperty("SmartEPGDao.getRealRating.interval");
		}
		long interval = GlobalCom.getCacheInterval(callByScheduler, intervalStr);
		
		List<RealRatingInfoVO> dbresult = (List<RealRatingInfoVO>)cacheservice.getCache(dao
				, "getRealRating"
				, SmartUXProperties.getProperty("SmartEPGDao.getRealRating.cacheKey")
				, interval
				);
		
		// 단말이 전달한 지역코드것으로만 따로 뽑는다
		List<RealRatingInfoVO> arearesult = getArearesult(dbresult, localareacode);

		List<RealRatingInfoVO> result = null;
		// 스케듈러에서 호출한 경우와 단말에서 호출한 경우를 구분해서 작업을 진행하도록 한다
		if("Y".equals(callByScheduler)){			// 스케듈러에서 호출한 경우
		}else if("A".equals(callByScheduler)){			// 스케듈러에서 호출한 경우
		}else{										// 단말에서 호출한 경우
			int startnum = Integer.parseInt(start_num);
				
			if(startnum >= 1){						// 1보다 큰 경우는 페이징 기법이 적용되므로 전체 조회한 내용에서 대상되는 레코드만큼만 꺼내서 넣는다
				startnum--;
				int endnum = startnum + Integer.parseInt(req_count);
				
				// endnum 수치가 검색된 리스트의 마지막 인덱스보다 큰지를 조사해서 그거보다 큰 경우엔 endnum을 검색된 리스트의 마지막 인덱스로 셋팅해준다
				if(endnum > arearesult.size()){
					endnum = arearesult.size();
				}
				
				result = new ArrayList<RealRatingInfoVO>();
				for(int i=startnum; i < endnum; i++){
					result.add(arearesult.get(i));
				}
			}else if(startnum == -1){				// -1인 경우 전체 조회이므로 db 조회 결과를 return 해준다
				result = arearesult;
			}
		}
		return result;
		
		
		// 더미 시작
		/*
		List<RealRatingInfoVO> dbresult = new ArrayList<RealRatingInfoVO>();
		List<RealRatingInfoVO> result = null;
		RealRatingInfoVO info1 = new RealRatingInfoVO();
		info1.setRank_no("1");
		info1.setService_id("01");
		info1.setChannel_no("ch11");
		info1.setChannel_name("우리채널");
		info1.setProgram_id("10");
		info1.setProgram_name("테마01프로그램10");
		info1.setDefin_flag("SD");
		info1.setProgram_info("01");
		info1.setProgram_stime("20120329");
		info1.setProgram_etime("20120330");
		info1.setSeries_flag("Y");
		info1.setSeries_desc("01회");
		info1.setRating("60.2");
		info1.setImg_url("imgurl");
		dbresult.add(info1);
		
		RealRatingInfoVO info2 = new RealRatingInfoVO();
		info2.setRank_no("2");
		info2.setService_id("01");
		info2.setChannel_no("ch11");
		info2.setChannel_name("우리채널");
		info2.setProgram_id("10");
		info2.setProgram_name("테마01프로그램10");
		info2.setDefin_flag("HD");
		info2.setProgram_info("01");
		info2.setProgram_stime("20120329");
		info2.setProgram_etime("20120330");
		info2.setSeries_flag("Y");
		info2.setSeries_desc("01회");
		info2.setRating("59.2");
		info2.setImg_url("imgurl");
		dbresult.add(info2);
		
		RealRatingInfoVO info3 = new RealRatingInfoVO();
		info3.setRank_no("3");
		info3.setService_id("01");
		info3.setChannel_no("ch11");
		info3.setChannel_name("우리채널");
		info3.setProgram_id("10");
		info3.setProgram_name("테마01프로그램10");
		info3.setDefin_flag("3D");
		info3.setProgram_info("01");
		info3.setProgram_stime("20120329");
		info3.setProgram_etime("20120330");
		info3.setSeries_flag("Y");
		info3.setSeries_desc("01회");
		info3.setRating("58.2");
		info3.setImg_url("imgurl");
		dbresult.add(info3);
		
		RealRatingInfoVO info4 = new RealRatingInfoVO();
		info4.setRank_no("4");
		info4.setService_id("01");
		info4.setChannel_no("ch11");
		info4.setChannel_name("우리채널");
		info4.setProgram_id("10");
		info4.setProgram_name("테마01프로그램10");
		info4.setDefin_flag("SD");
		info4.setProgram_info("01");
		info4.setProgram_stime("20120329");
		info4.setProgram_etime("20120330");
		info4.setSeries_flag("Y");
		info4.setSeries_desc("01회");
		info4.setRating("57.2");
		info4.setImg_url("imgurl");
		dbresult.add(info4);
		
		RealRatingInfoVO info5 = new RealRatingInfoVO();
		info5.setRank_no("5");
		info5.setService_id("01");
		info5.setChannel_no("ch11");
		info5.setChannel_name("우리채널");
		info5.setProgram_id("10");
		info5.setProgram_name("테마01프로그램10");
		info5.setDefin_flag("SD");
		info5.setProgram_info("01");
		info5.setProgram_stime("20120329");
		info5.setProgram_etime("20120330");
		info5.setSeries_flag("Y");
		info5.setSeries_desc("01회");
		info5.setRating("56.2");
		info5.setImg_url("imgurl");
		dbresult.add(info5);
		
		int startnum = Integer.parseInt(start_num);
		
		if(startnum >= 1){						// 1보다 큰 경우는 페이징 기법이 적용되므로 전체 조회한 내용에서 대상되는 레코드만큼만 꺼내서 넣는다
			startnum--;
			int endnum = startnum + Integer.parseInt(req_count);
			
			// endnum 수치가 검색된 리스트의 마지막 인덱스보다 큰지를 조사해서 그거보다 큰 경우엔 endnum을 검색된 리스트의 마지막 인덱스로 셋팅해준다
			if(endnum > dbresult.size()){
				endnum = dbresult.size();
			}
			
			result = new ArrayList<RealRatingInfoVO>();
			for(int i=startnum; i < endnum; i++){
				result.add(dbresult.get(i));
			}
		}else if(startnum == -1){				// -1인 경우 전체 조회이므로 db 조회 결과를 return 해준다
			result = dbresult;
		}
		
		return result;
		*/
		// 더미 끝
	}
	
	private List<RealRatingInfoVO> getArearesult(List<RealRatingInfoVO> dbresult, String localareacode) throws Exception{
		// 입력받은 지역코드 값을 구분자를 이용해 분리해낸다
		String commoncode = SmartUXProperties.getProperty("RealRating.LocalAreaCode.CommonCode");
		String splitstr = SmartUXProperties.getProperty("RealRating.LocalAreaCode.SplitString");
		// String [] areacodes = localareacode.split("\\|\\|");
		String [] areacodes = localareacode.split(splitstr);
		
		List<RealRatingInfoVO> result = new ArrayList<RealRatingInfoVO>();
		
		for(RealRatingInfoVO item : dbresult){
			String dbareacode = item.getLocalareacode();
			if(dbareacode.equals(commoncode)){				// 실시간 시청률 데이터의 지역코드가 공통 지역 코드이면
				result.add(item);
				continue;
			}else{
				for(String acode : areacodes){				
					if(acode.equals(dbareacode)){			// 실시간 시청률 데이터의 지역코드가 단말이 보낸 지역코드들 중의 하나이면
						result.add(item);
						break;
					}
				}
			}
		}
		
		return result;
	}
	
}
