package com.dmi.smartux.noti.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dmi.smartux.common.base.HDTVInfo;
import com.dmi.smartux.common.dao.CommonDao;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.service.CacheService;
import com.dmi.smartux.common.service.impl.CacheServiceImpl;
import com.dmi.smartux.common.util.DateUtils;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.noti.vo.BaseNotiVO;
import com.dmi.smartux.noti.vo.NotiEtcInfo;
import com.dmi.smartux.noti.vo.NotiIdAndVersionVO;

/**
 * 게시판 관련 DB 접근 DAO 객체
 * 
 * @author jch82@naver.com
 */
@Repository
public class NotiDao extends CommonDao {
	private int mRetryCount = 0;

	/**
	 * 전체적인 캐쉬를 넣고 빼며 관리한다.
	 */
	@Autowired
	CacheService service;

	/**
	 * @return 각 게시판의 게시판 명과 버전을 반환한다.
	 * @throws Exception
	 */
	@SuppressWarnings( "unchecked" )
	public List<NotiIdAndVersionVO> getNotiIdAndVersionList ( ) throws Exception {
		return getSqlMapClientTemplate ( ).queryForList ( "notiapi.getNotiIdAndVersionList" );
	}

	/**
	 * bbs_id 와 일치하는 게시판의 모든 정보를 반환한다.
	 * 
	 * @param bbs_id 게시판 아이디
	 * @return bbs_id 에 일치하는 게시판의 모든 row
	 * @throws Exception
	 */
	@SuppressWarnings( "unchecked" )
	public List<BaseNotiVO> getNotiInfoList ( String bbs_id ) throws Exception {
		Log log_logger = LogFactory.getLog ( "refreshCacheOfNoti" );

		Map<String, String> param = new HashMap<String, String> ( );
		param.put ( "bbs_id", bbs_id );
		param.put ( "beforeday", SmartUXProperties.getProperty("smartux.noti.beforeday"));

		List<BaseNotiVO> returnValue = null;
		try {
			returnValue = getSqlMapClientTemplate ( ).queryForList ( "notiapi.getNotiInfoList", param );// 해당 기간안의 게시물
		} catch ( Exception exception ) {
			if ( Integer.parseInt ( SmartUXProperties.getProperty("smartux.db.retrycount") ) <= mRetryCount ) {
				mRetryCount = 0;
			} else {
				mRetryCount++;
				getNotiInfoList ( bbs_id );
			}
		}

		if ( returnValue != null ) {
			setEtcInfoInCache ( returnValue, bbs_id );// 단말 별 전체 리스트 수와 new리스트 수를 구해서
		}

		// LOG!!!!!!!!!!
		log_logger.info ( "=========getNotiInfoList START (bbs_id=" + bbs_id + ")=========" );
		for ( int i = 0; i < returnValue.size ( ); i++ ) {
			BaseNotiVO data = (BaseNotiVO) returnValue.get ( i );
			log_logger.info ( "=BaseNotiVO[" + i + "] bbs_id[" + data.getBbs_id ( ) + "] reg_no[" + data.getReg_no ( ) + "] title[" + data.getTitle ( ) + "] trem_model[" + data.getTerm_model ( ) + "]" );
		}
		log_logger.info ( "=========getNotiInfoList END=========" );

		return returnValue;
	}

	@SuppressWarnings( "unchecked" )
	public List<BaseNotiVO> getNotiInfoListTest ( String bbs_id ) throws Exception {
		Map<String, String> param = new HashMap<String, String> ( );
		param.put ( "bbs_id", bbs_id );
		param.put ( "beforeday", SmartUXProperties.getProperty("smartux.noti.beforeday") );

		List<BaseNotiVO> returnValue = null;
		try {
			returnValue = getSqlMapClientTemplate ( ).queryForList ( "notiapi.getNotiInfoListTest", param );
		} catch ( Exception exception ) {
			if ( Integer.parseInt ( SmartUXProperties.getProperty("smartux.db.retrycount") ) <= mRetryCount ) {
				mRetryCount = 0;
			} else {
				mRetryCount++;
				getNotiInfoList ( bbs_id );
			}
		}

		if ( returnValue != null ) {
			setEtcInfoInCacheTest ( returnValue, bbs_id );
		}

		return returnValue;
	}

	/**
	 * bbs_id 에 따른 게시판의 new count 과 total count 를 캐쉬에 입력한다.
	 * 
	 * <pre>
	 * new count : 전체 중 새로운 게시글 ( 일 단위로 새 게시글 구분 )
	 * total count : 전체 게시물의 개수
	 * </pre>
	 * 
	 * @param baseNotiVOList bbs_id 에 따른 전체 게시글
	 * @param bbs_id 게시판 아이디
	 * @throws Exception
	 */
	@SuppressWarnings( "unchecked" )
	private void setEtcInfoInCache ( List<BaseNotiVO> baseNotiVOList, String bbs_id ) throws Exception {// 해당 게시판의
																										// 리스트/개시판 ID
		Map<String, NotiEtcInfo> tempNotiEtcInfoMap = new HashedMap ( ); // 단말별 new 와 total 갯수를 임시로 구해서 담는다.
		NotiEtcInfo commonNotiEctInfo = new NotiEtcInfo ( );
		NotiEtcInfo tempNotiEtcInfo;
		Date nowDate = new Date ( );
		// 게시판 의 단말 별 new 와 total 을 을 구한다. (리스트 만큼 돌면서 현재리스의 게시물 제공 단말이 ALL이면(특정단말에 소속되지 않은 게시물) 토탈리스트와 토탈new리스트에 +하고 특정
		// 단말에 소속되어 있으면 특정 단말 카운트에 +)
		for ( int k = 0; k < baseNotiVOList.size ( ); k++ ) {
			String termModel = baseNotiVOList.get ( k ).getTerm_model ( );
			if ( termModel == null ) {// 전체의
				commonNotiEctInfo.plusTotalCount ( );
				Date nDate = baseNotiVOList.get ( k ).getN_date ( );
				// if (nDate != null && DateUtils.compare(nDate, nowDate) == CompareValue.High) {
				// commonNotiEctInfo.plusNewCount();
				// }
				if ( nDate != null && DateUtils.DateStr ( GlobalCom.getTodayFormat ( ), nDate ) ) {
					commonNotiEctInfo.plusNewCount ( );
				}
			} else {// 단말별의
				tempNotiEtcInfo = tempNotiEtcInfoMap.get ( termModel );
				if ( tempNotiEtcInfo == null ) {
					tempNotiEtcInfo = new NotiEtcInfo ( );
				} else {
					tempNotiEtcInfoMap.remove ( termModel );
				}
				tempNotiEtcInfo.plusTotalCount ( );
				Date nDate = baseNotiVOList.get ( k ).getN_date ( );
				// if (nDate != null && DateUtils.compare(nDate, nowDate) == CompareValue.High) {
				// tempNotiEtcInfo.plusNewCount();
				// }
				if ( nDate != null && DateUtils.DateStr ( GlobalCom.getTodayFormat ( ), nDate ) ) {
					tempNotiEtcInfo.plusNewCount ( );
				}
				tempNotiEtcInfoMap.put ( termModel, tempNotiEtcInfo );
			}
		}

		// 단말 수만큼 돌며 자신의 리스트 수와 new리스트 수에 전체(특정단말에 소속되지 않은 게시물) 리스트를 더해 준다.(실제 해당 단말의 게시물 수를 만든다.)
		Object[] notiEtcInfoMapKeySet = tempNotiEtcInfoMap.keySet ( ).toArray ( );// 저장되어 있는 단말 갯수
		Map<String, NotiEtcInfo> notiEtcInfoMap = new HashedMap ( ); // 단말별 new 와 total 갯수를 구해서 담는다. (자신의 개수 + total 개수)
		String keyName;
		for ( int k = 0; k < notiEtcInfoMapKeySet.length; k++ ) {
			keyName = (String) notiEtcInfoMapKeySet[k];
			tempNotiEtcInfo = tempNotiEtcInfoMap.get ( keyName );
			tempNotiEtcInfo.setNewCount ( tempNotiEtcInfo.getNewCount ( ) + commonNotiEctInfo.getNewCount ( ) );
			tempNotiEtcInfo.setTotalCount ( tempNotiEtcInfo.getTotalCount ( ) + commonNotiEctInfo.getTotalCount ( ) );
			notiEtcInfoMap.put ( keyName, tempNotiEtcInfo );
		}

		// ALL (Common) MAP Key 추가 됨!! -- 게시물 조회시 특정단말 선택하지 않았을 때
		notiEtcInfoMap.put ( HDTVInfo.AllTermModel, commonNotiEctInfo );
		service.putCache ( CacheServiceImpl.getNotiEtcCacheKey ( bbs_id ), notiEtcInfoMap );

		// //LOG!!!!!!!!!!
		// for(int i=0;i<baseNotiVOList.size();i++){
		// BaseNotiVO data = (BaseNotiVO)baseNotiVOList.get(i);
		// }
	}

	/**
	 * bbs_id 에 따른 게시판의 new count 과 total count 를 캐쉬에 입력하지 않는다(검수용)
	 * 
	 * <pre>
	 * new count : 전체 중 새로운 게시글 ( 일 단위로 새 게시글 구분 )
	 * total count : 전체 게시물의 개수
	 * </pre>
	 * 
	 * @param baseNotiVOList bbs_id 에 따른 전체 게시글
	 * @param bbs_id 게시판 아이디
	 * @throws Exception
	 */
	public Map<String, NotiEtcInfo> setEtcInfoInCacheTest ( List<BaseNotiVO> baseNotiVOList, String bbs_id ) throws Exception {
		Map<String, NotiEtcInfo> tempNotiEtcInfoMap = new HashedMap ( ); // 단말별 new 와 total 갯수를 임시로 구해서 담는다.
		NotiEtcInfo commonNotiEctInfo = new NotiEtcInfo ( );
		NotiEtcInfo tempNotiEtcInfo;
		Date nowDate = new Date ( );
		// 게시판 의 단말 별 new 와 total 을 을 구한다.
		for ( int k = 0; k < baseNotiVOList.size ( ); k++ ) {
			String termModel = baseNotiVOList.get ( k ).getTerm_model ( );
			if ( termModel == null ) { // 모델구분이 없는경우 ALL
				commonNotiEctInfo.plusTotalCount ( );
				Date nDate = baseNotiVOList.get ( k ).getN_date ( );
				// if (nDate != null && DateUtils.compare(nDate, nowDate) == CompareValue.High) {
				// commonNotiEctInfo.plusNewCount();
				// }
				if ( nDate != null && DateUtils.DateStr ( GlobalCom.getTodayFormat ( ), nDate ) ) {
					commonNotiEctInfo.plusNewCount ( );
				}
			} else { // 모델구분이 있는경우
				tempNotiEtcInfo = tempNotiEtcInfoMap.get ( termModel );
				if ( tempNotiEtcInfo == null ) {
					tempNotiEtcInfo = new NotiEtcInfo ( );
				} else {
					tempNotiEtcInfoMap.remove ( termModel );
				}
				tempNotiEtcInfo.plusTotalCount ( );
				Date nDate = baseNotiVOList.get ( k ).getN_date ( );
				// if (nDate != null && DateUtils.compare(nDate, nowDate) == CompareValue.High) {
				// tempNotiEtcInfo.plusNewCount();
				// }
				if ( nDate != null && DateUtils.DateStr ( GlobalCom.getTodayFormat ( ), nDate ) ) {
					tempNotiEtcInfo.plusNewCount ( );
				}
				tempNotiEtcInfoMap.put ( termModel, tempNotiEtcInfo );
			}
		}

		Object[] notiEtcInfoMapKeySet = tempNotiEtcInfoMap.keySet ( ).toArray ( );
		Map<String, NotiEtcInfo> notiEtcInfoMap = new HashedMap ( ); // 단말별 new 와 total 갯수를 구해서 담는다. (자신의 개수 + total 개수)
		String keyName;
		for ( int k = 0; k < notiEtcInfoMapKeySet.length; k++ ) {
			keyName = (String) notiEtcInfoMapKeySet[k];
			tempNotiEtcInfo = tempNotiEtcInfoMap.get ( keyName );
			tempNotiEtcInfo.setNewCount ( tempNotiEtcInfo.getNewCount ( ) + commonNotiEctInfo.getNewCount ( ) );
			tempNotiEtcInfo.setTotalCount ( tempNotiEtcInfo.getTotalCount ( ) + commonNotiEctInfo.getTotalCount ( ) );
			notiEtcInfoMap.put ( keyName, tempNotiEtcInfo );
		}

		// ALL (Common) MAP Key 추가 됨!!
		notiEtcInfoMap.put ( HDTVInfo.AllTermModel, commonNotiEctInfo );
		// service.putCache(CacheServiceImpl.getNotiEtcCacheKey(bbs_id), notiEtcInfoMap);
		return notiEtcInfoMap;
	}

}
