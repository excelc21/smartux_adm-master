package com.dmi.smartux.noti.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmi.smartux.common.base.BaseService;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.DateUtils;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.noti.dao.NotiDao;
import com.dmi.smartux.noti.service.NotiService;
import com.dmi.smartux.noti.vo.BaseNotiVO;
import com.dmi.smartux.noti.vo.NotiIdAndVersionVO;

/**
 * 게시판 관련 Service 구현 class
 * 
 * @author jch82@naver.com
 */
@Service
public class NotiServiceImpl extends BaseService implements NotiService {
	Log log_logger = LogFactory.getLog ( "refreshCacheOfNoti" );

	/**
	 * 게시판에 대한 dao 이다.
	 */
	@Autowired
	NotiDao dao;

	@SuppressWarnings( "unused" )
	@Override
	public void refreshCacheOfNoti ( String callByScheduler ) throws Exception {
		List<NotiIdAndVersionVO> notiIdAndVersionVO = getNotiIdAndVersionList ( callByScheduler );

		long interval;
		for ( int i = 0; i < notiIdAndVersionVO.size ( ); i++ ) {
			List<BaseNotiVO> list = getNotiInfoList ( callByScheduler, notiIdAndVersionVO.get ( i ).getBbs_id ( ) );
		}
	}

	@SuppressWarnings( "unused" )
	@Override
	public List<BaseNotiVO> refreshCacheOfNoti ( String bbs_id, String callByScheduler ) throws Exception {
		List<NotiIdAndVersionVO> notiIdAndVersionVO = getNotiIdAndVersionList ( callByScheduler );// 모든 버전 정보 가져오기
		return getNotiInfoList ( callByScheduler, bbs_id );// 해당 bbs_id의 게시물 저장 및 가져오기(단말 별 전체 리스트와 단말별 new 리스트도 배열로 저장)
	}

	@SuppressWarnings( "unchecked" )
	@Override
	public List<NotiIdAndVersionVO> getNotiIdAndVersionList ( String callByScheduler ) throws Exception {

		long interval = getInterval ( callByScheduler, "SmartUXNotiDao.getNotiIdAndVersion.interval" );
		List<NotiIdAndVersionVO> list = (List<NotiIdAndVersionVO>) service.getCache ( dao, "getNotiIdAndVersionList", SmartUXProperties.getProperty ( "SmartUXNotiDao.getNotiIdAndVersion.cacheKey" ), interval );

		// LOG!!!!!!!!!!
		// log_logger.info("=========getNotiIdAndVersionList START (callByScheduler="+callByScheduler+") (interval="+interval+")=========");
		// for(int i=0;i<list.size();i++){
		// NotiIdAndVersionVO data = (NotiIdAndVersionVO)list.get(i);
		// log_logger.info("=NotiIdAndVersionVO["+i+"] version["+data.getVersion()+"]  bbs_id["+data.getBbs_id()+"]");
		// }
		// log_logger.info("=========getNotiIdAndVersionList END=========");

		return list;
	}

	@Override
	public String getBBSVersion ( String bbs_id, String callByScheduler ) throws Exception {
		List<NotiIdAndVersionVO> notiIdAndVersionVO = getNotiIdAndVersionList ( callByScheduler );

		String version = null;
		for ( int i = 0; i < notiIdAndVersionVO.size ( ); i++ ) {
			if ( notiIdAndVersionVO.get ( i ).getBbs_id ( ).equals ( bbs_id ) ) {
				version = notiIdAndVersionVO.get ( i ).getVersion ( );
				break;
			}
		}
		return version;
	}

	/**
	 * newDate 와 현재 날짜를 비교하여 년과 일이 현재 날짜보와 같거나 크다면 Y 를 돌려준다.
	 * 
	 * @param newDate
	 * @return Y : newDate 보다 현재 년도 와 월 이 크거나 같을 경우<br/>
	 *         N : newDate 보다 현재 년도 와 월 이 작은 경우<br/>
	 */
	@SuppressWarnings( "deprecation" )
	private String isNew ( Date newDate ) {
		String returnValue = "N";

		if ( newDate == null ) {
			return returnValue;
		}

		Date currentDate = new Date ( );

		// if (newDate != null) {
		// if (newDate.getYear() >= currentDate.getYear()
		// &&
		// newDate.getMonth() >= currentDate.getMonth()
		// &&
		// newDate.getDay() >= currentDate.getDay()
		// )
		// {
		// returnValue = "Y";
		// }
		// }
		if ( DateUtils.DateStr ( GlobalCom.getTodayFormat ( ), newDate ) ) {
			returnValue = "Y";
		}

		return returnValue;
	}

	/**
	 * bbs_id 에 따른 게시판 정보를 반환한다.
	 * 
	 * <pre>
	 * bbs_id 에 따른 게시판 정보를 반환하며, 게시판의 총 게시글 갯수 와 new 의 갯수를 캐쉬에 올린다.
	 * CacheServiceImpl.getNotiEtcCacheKey(bbs_id) 함수를 이용하여 [new 갯수] 및 [total 갯수] 가 저장된 캐쉬값을 알아온다.
	 * </pre>
	 * 
	 * @param callByScheduler 스케줄러 호출과 관리자의 강제 호출을 구분하기 위한 플래그<br/>
	 *            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;관리자 호출 시 A 스케줄러 호출 시 Y, 단말 호출 시 N 을 사용한다.
	 * @param bbs_id 게시판 아이디
	 * @return bbs_id 에 따른 게시판 정보를 List 에 담아 돌려준다.
	 * @throws Exception
	 */
	@SuppressWarnings( "unchecked" )
	private List<BaseNotiVO> getNotiInfoList ( String callByScheduler, String bbs_id ) throws Exception {

		long interval = getInterval ( callByScheduler, "SmartUXNotiDao.cache_noti.interval" );
		List<BaseNotiVO> list = (List<BaseNotiVO>) service.getCache ( dao, "getNotiInfoList", SmartUXProperties.getProperty ( "SmartUXNotiDao.cache_noti.cacheKey" ), interval, bbs_id );
		
		// LOG!!!!!!!!!!
		 log_logger.info("=========getNotiInfoList START (bbs_id="+bbs_id+") (callByScheduler="+callByScheduler+") (interval="+interval+")=========");
		 for(int i=0;i<list.size();i++){
		 BaseNotiVO data = (BaseNotiVO)list.get(i);
		 log_logger.info("=BaseNotiVO["+i+"] bbs_id["+data.getBbs_id()+"] reg_no["+data.getReg_no()+"] title["+data.getTitle()+"] trem_model["+data.getTerm_model()+"]");
		 }
		 log_logger.info("=========getNotiInfoList END=========");

		return list;
	}


}
