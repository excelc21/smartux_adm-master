package com.dmi.smartux.notipop.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmi.smartux.admin.noti.dao.NotiViewDao;
import com.dmi.smartux.common.base.BaseService;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.vo.QualityMemberVo;
import com.dmi.smartux.notipop.dao.NotiPopDao;
import com.dmi.smartux.notipop.service.NotiPopService;
import com.dmi.smartux.notipop.vo.getNotiPopCacheVo;
import com.dmi.smartux.notipop.vo.getNotiPopContListVo;
import com.dmi.smartux.notipop.vo.getNotiPopEmergencyVo;
import com.dmi.smartux.notipop.vo.getNotiPopListVo;
import com.dmi.smartux.notipop.vo.getNotiPopNotiListVo;
import com.dmi.smartux.quality.service.QualityService;
import com.dmi.smartux.quality.vo.getQualityServerVo;

@Service
public class NotiPopServiceImpl extends BaseService implements NotiPopService {
	Log log_logger = LogFactory.getLog("startupnoti");
	Log log_logger_cache = LogFactory.getLog("refreshCacheOfNotiPop");
	
	@Autowired
	NotiPopDao dao;
	
	@Autowired
	NotiViewDao notiviewDao;
	
	@Autowired
	QualityService qualityService;

	@Override
	public void refreshCacheOfNotiPop(String callByScheduler) throws Exception {
		
		long interval = getInterval( callByScheduler, "SmartUXNotiPopDao.refreshNotiPop.interval" );
		
		//테마 정보
		service.getCache(
					this
					, "getNotiPopAllList"
					, SmartUXProperties.getProperty("SmartUXNotiPopDao.refreshNotiPop.cacheKey")
					, interval
				);
	}
	
	public getNotiPopCacheVo getStartupNoti(String version, String app_gbn, String sa_id) throws Exception{
		
		getNotiPopCacheVo getnotipopcacheVo = new getNotiPopCacheVo();
		
		String rowGbn = "\\f\\^"; //컨텐츠 연동형 레코드 분리자
		String colsGbn= "\b\\^"; //컨텐츠 연동형 컬럼 분리자
		
		String disp_type = "S";//상용 데이터
		if(version.equals(SmartUXProperties.getProperty("version.test"))) disp_type = "T";//개발 데이터

		List<getNotiPopListVo> getnotipoplistVo = (List<getNotiPopListVo>) service.getCache(SmartUXProperties.getProperty("SmartUXNotiPopDao.refreshNotiPop.cacheKey"));
		
		for(getNotiPopListVo loopVo : getnotipoplistVo){
			
			if(app_gbn.equalsIgnoreCase(loopVo.getScr_tp()) && disp_type.equals(loopVo.getDisplay_type())){//찾았다!!!
				
				String status = GlobalCom.isNull(loopVo.getStatus());
				String reg_no = GlobalCom.isNull(loopVo.getReg_no());
				
				//데이터가 있다면.
				if(!"".equals(status)){
					getNotiPopEmergencyVo getnotipopemergencyVo = new getNotiPopEmergencyVo();
					getnotipopemergencyVo.setStatus(GlobalCom.isNull(loopVo.getStatus()));
					getnotipopemergencyVo.setMessage(GlobalCom.isNull(loopVo.getMessage()));
					getnotipopemergencyVo.setMessage_yn(GlobalCom.isNull(loopVo.getMessage_yn()));
					getnotipopemergencyVo.setNet_type(GlobalCom.isNull(loopVo.getNet_type()));
					getnotipopcacheVo.setEmergency(getnotipopemergencyVo);
				}
				if(!"".equals(reg_no)){
					getNotiPopNotiListVo getnotipopnotilistVo = new getNotiPopNotiListVo();
					getnotipopnotilistVo.setNoti_tit(GlobalCom.isNull(loopVo.getTitle()));
					getnotipopnotilistVo.setNoti_cont(GlobalCom.isNull(loopVo.getContent()));
					getnotipopnotilistVo.setNet_type(GlobalCom.isNull(loopVo.getShow_type ( )));
					getnotipopnotilistVo.setS_date(GlobalCom.isNull(loopVo.getS_date()));
					getnotipopnotilistVo.setE_date(GlobalCom.isNull(loopVo.getE_date()));
					getnotipopnotilistVo.setModel_cnt(Integer.toString(GlobalCom.isNumber(loopVo.getModels().size())));
					getnotipopnotilistVo.setModel(loopVo.getModels());
					getnotipopnotilistVo.setNoti_no(GlobalCom.isNull(loopVo.getReg_no()));
					getnotipopnotilistVo.setEv_cont_id(GlobalCom.isNull(loopVo.getEv_cont_id()));
					getnotipopnotilistVo.setImg_url(GlobalCom.isNull(loopVo.getSave_file_nm()));
					getnotipopnotilistVo.setEv_type(GlobalCom.isNull(loopVo.getEv_type()));
					
					getnotipopnotilistVo.setEv_stat_id(loopVo.getEv_stat_id());
					
					if("ev1".equals(loopVo.getEv_type())){//컨텐츠 연동
						List<getNotiPopContListVo> getnotipopcontlistVo_arr = new ArrayList<getNotiPopContListVo>();
						String[] recCtn = loopVo.getEv_detail().split(rowGbn);
						for(String colCnt : recCtn){
							String[] arrCtn = colCnt.split(colsGbn);
							if(arrCtn.length==2){
								getNotiPopContListVo getnotipopcontlistVo = new getNotiPopContListVo();
								getnotipopcontlistVo.setEv_id(arrCtn[0]);
								getnotipopcontlistVo.setEv_name(arrCtn[1]);
								getnotipopcontlistVo_arr.add(getnotipopcontlistVo);
							}
						}
						getnotipopnotilistVo.setEv_detail_list(getnotipopcontlistVo_arr);
						getnotipopnotilistVo.setEv_detail(null);
					}else if("ev7".equals(loopVo.getEv_type())){//공지/이벤트 게시글
						String[] recCtn = loopVo.getEv_detail().split(",");
						StringBuffer sb = new StringBuffer();
						
						if(recCtn.length==3){
							sb.append ( recCtn[1] );
							sb.append ( "," );
							sb.append ( recCtn[2] );
						}
						getnotipopnotilistVo.setEv_detail(sb.toString ( ));
						getnotipopnotilistVo.setEv_detail_list(null);
					}else if("ev2".equals(loopVo.getEv_type()) || "ev5".equals(loopVo.getEv_type()) || "ev6".equals(loopVo.getEv_type())){//월정액 가입, URL링크,특정카테고리
						getnotipopnotilistVo.setEv_detail(loopVo.getEv_detail());
						getnotipopnotilistVo.setEv_detail_list(null);
					}
					getnotipopcacheVo.setNoti(getnotipopnotilistVo);
				}
				
				if(!"".equals(sa_id)){
					//품질데이터 sa_id
					Map<String, Object> quality = qualityService.getQualityMemberList();
					Map<String, QualityMemberVo> qltDeList = (Map<String, QualityMemberVo>)quality.get("DE");
					List<QualityMemberVo> qltPrList = (List<QualityMemberVo>)quality.get("PR");
					List<QualityMemberVo> qltPoList = (List<QualityMemberVo>)quality.get("PO");
					String qualityServer = (String)quality.get("SERVER");
					
					QualityMemberVo qltDeVo = null;
					if(qltDeList != null) qltDeVo = qltDeList.get(sa_id);
					
					getQualityServerVo getqualityserverVo = new getQualityServerVo();
					int findYn = 0;//못찾음
					if(qltDeVo!=null){
						getqualityserverVo.setLog_type(qltDeVo.getLog_type());
						getqualityserverVo.setSize(qltDeVo.getSize());
						getqualityserverVo.setServer_ip(qualityServer);
						log_logger.info( "["+sa_id+"]["+version+"]["+app_gbn+"][said found]" );
						findYn = 1;
					}else{
						if(qltPrList!=null){
							for(QualityMemberVo qltPrVo : qltPrList){
								if(sa_id.startsWith(qltPrVo.getSa_id())){
									getqualityserverVo.setLog_type(qltPrVo.getLog_type());
									getqualityserverVo.setSize(qltPrVo.getSize());
									getqualityserverVo.setServer_ip(qualityServer);
									log_logger.info( "["+sa_id+"]["+version+"]["+app_gbn+"][startwith said found]" );
									findYn = 1;
									break;
								}
							}
						}
						if(findYn == 0){//아직 못찾았다면
							if(qltPoList!=null){
								for(QualityMemberVo qltPoVo : qltPoList){
									if(sa_id.endsWith(qltPoVo.getSa_id())){
										getqualityserverVo.setLog_type(qltPoVo.getLog_type());
										getqualityserverVo.setSize(qltPoVo.getSize());
										getqualityserverVo.setServer_ip(qualityServer);
										log_logger.info( "["+sa_id+"]["+version+"]["+app_gbn+"][endwith said found]" );
										findYn = 1;
										break;
									}
								}
							}
						}
					}
					if(findYn==1){
						getnotipopcacheVo.setQuality(getqualityserverVo);
					}
				}
					
				break;
			}
		}
		
		return getnotipopcacheVo;
	}
	
	public List<getNotiPopListVo> getNotiPopAllList() throws Exception {
		
		List<getNotiPopListVo> getnotipoplistVo = dao.getNotiPopAllList();
		log_logger_cache.info("getNotiPopAllList Size : "+getnotipoplistVo.size());
		for(getNotiPopListVo subVo : getnotipoplistVo){
			log_logger_cache.info("getNotiPopAllList : bbs_id=>"+subVo.getBbs_id()+" reg_no=>"+subVo.getReg_no());
			if(!"".equals(GlobalCom.isNull(subVo.getReg_no()))){
				List<Map<String ,String>> modelMap = notiviewDao.getNotiTerminal(subVo.getReg_no());
				List<String> modelVo = new ArrayList<String>();
				if(modelMap!=null && modelMap.size() > 0){
					for(Map<String ,String> subMap : modelMap){
						modelVo.add(GlobalCom.isNull(subMap.get("TERM_MODEL")));
					}
				}else{
					modelVo.add("ALL");
				}
				subVo.setModels(modelVo);
			}
		}
		
		return getnotipoplistVo;
		
	}

}
