package com.dmi.smartux.noticeinfo.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.dmi.smartux.cache.service.CacheIntervalService;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.service.CacheService;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.noticeinfo.dao.NoticeInfoDao;
import com.dmi.smartux.noticeinfo.service.NoticeInfoService;
import com.dmi.smartux.noticeinfo.vo.CacheNoticeInfoListVo;
import com.dmi.smartux.noticeinfo.vo.NotiMainCodeListVo;

@Service
public class NoticeInfoServiceImpl implements NoticeInfoService {
	
	@Autowired
	CacheService cacheservice;
	
	@Autowired
	CacheIntervalService cacheIntervalService;
	
	@Autowired
	NoticeInfoDao dao;

	@Override
	public HashMap<String, List<CacheNoticeInfoListVo>> cacheNoticeInfoList() throws Exception {

		HashMap<String, List<CacheNoticeInfoListVo>> notiMap = new HashMap<String, List<CacheNoticeInfoListVo>>();
		
		List<NotiMainCodeListVo> notimaincodelistVo = dao.getNotiMainCodeList();
		for(NotiMainCodeListVo tmpVo : notimaincodelistVo){
			List<CacheNoticeInfoListVo> NoticeInfoListMap = dao.cacheNoticeInfo(tmpVo.getCode());
			
			if(NoticeInfoListMap!=null){
				notiMap.put(tmpVo.getCode(), NoticeInfoListMap);
			}
			
		}
		
		return notiMap;
	}

	/*@Override
	public void cacheNoticeInfo(String callByScheduler) throws Exception {
		
		String intervalStr = "0";
		try{
			intervalStr = Long.toString(cacheIntervalService.getCacheInterval("NoticeInfoDao.cacheNoticeInfo.interval"));
		}catch (Exception e) {
			intervalStr = SmartUXProperties.getProperty("NoticeInfoDao.cacheNoticeInfo.interval");
		}
		long interval = GlobalCom.getCacheInterval(callByScheduler, intervalStr);
		
		cacheservice.getCache(this
		, "cacheNoticeInfoList"
		, SmartUXProperties.getProperty("NoticeInfoDao.cacheNoticeInfo.cacheKey")
		, interval
		);
		
	}*/

	@Override
	public HashMap<String, List<CacheNoticeInfoListVo>> viewNoticeInfoListReal(
			String service) throws Exception {
		
		HashMap<String, List<CacheNoticeInfoListVo>> notiMap = (HashMap<String, List<CacheNoticeInfoListVo>>) cacheservice.getCache(this
				, "cacheNoticeInfoList"
				, SmartUXProperties.getProperty("NoticeInfoDao.cacheNoticeInfo.cacheKey")
				, 0
				);
		
		List<CacheNoticeInfoListVo> cachenoticeinfolistVo = notiMap.get(service);
		

		HashMap<String, List<CacheNoticeInfoListVo>> noti_Map = new HashMap<String, List<CacheNoticeInfoListVo>>();
		List<CacheNoticeInfoListVo> txtVo = new ArrayList<CacheNoticeInfoListVo>();
		List<CacheNoticeInfoListVo> imgVo = new ArrayList<CacheNoticeInfoListVo>();
		
		for(CacheNoticeInfoListVo tmpVo : cachenoticeinfolistVo){
			if(tmpVo.getNtype().equals("TXT")){
				txtVo.add(tmpVo);
			}else if(tmpVo.getNtype().equals("IMG")){
				imgVo.add(tmpVo);
			}
		}
		
		if(txtVo!=null) noti_Map.put("TXT", txtVo);
		if(imgVo!=null) noti_Map.put("IMG", imgVo);
		
		return noti_Map;
	}

	@Override
	public List<CacheNoticeInfoListVo> getNoticeInfoApi(String service,
			String ntype, String category, String callByScheduler)
			throws Exception {
		
		String intervalStr = "0";
		try{
			intervalStr = Long.toString(cacheIntervalService.getCacheInterval("NoticeInfoDao.cacheNoticeInfo.interval"));
		}catch (Exception e) {
			intervalStr = SmartUXProperties.getProperty("NoticeInfoDao.cacheNoticeInfo.interval");
		}
		long interval = GlobalCom.getCacheInterval(callByScheduler, intervalStr);
		
		List<CacheNoticeInfoListVo> listVo = new ArrayList<CacheNoticeInfoListVo>();
		HashMap<String, List<CacheNoticeInfoListVo>> notiMap = (HashMap<String, List<CacheNoticeInfoListVo>>) cacheservice.getCache(this
				, "cacheNoticeInfoList"
				, SmartUXProperties.getProperty("NoticeInfoDao.cacheNoticeInfo.cacheKey")
				, interval
				);

		// 스케듈러에서 호출한 경우와 단말에서 호출한 경우를 구분해서 작업을 진행하도록 한다
		if("Y".equals(callByScheduler)||"A".equals(callByScheduler)){			// 스케듈러에서 호출한 경우
					
		}else{	
			List<CacheNoticeInfoListVo> cachenoticeinfolistVo = notiMap.get(service);
			
			if(cachenoticeinfolistVo!=null){
				if(!StringUtils.hasText(ntype) && !StringUtils.hasText(category)){
					listVo = cachenoticeinfolistVo;
				}else{
					for(CacheNoticeInfoListVo tmpVo : cachenoticeinfolistVo){
						String putYn = "Y";
						if(StringUtils.hasText(ntype) && !tmpVo.getNtype().equals(ntype)){
							putYn = "N";
						}
						if(StringUtils.hasText(category) && !tmpVo.getCategory().equals(category)){
							putYn = "N";
						}
						if(putYn=="Y"){
							listVo.add(tmpVo);
						}
					}
				}
			}
		}
		
		return listVo;
	}

}
