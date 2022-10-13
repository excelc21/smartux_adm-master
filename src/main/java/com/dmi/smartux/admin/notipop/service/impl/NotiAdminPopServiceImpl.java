package com.dmi.smartux.admin.notipop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmi.smartux.admin.notipop.dao.NotiAdminPopDao;
import com.dmi.smartux.admin.notipop.service.NotiAdminPopService;
import com.dmi.smartux.admin.notipop.vo.getEmergencyViewVo;
import com.dmi.smartux.admin.notipop.vo.getEmergencyVo;
import com.dmi.smartux.admin.notipop.vo.notiPopVo;

@Service
public class NotiAdminPopServiceImpl implements NotiAdminPopService {

	@Autowired
	NotiAdminPopDao dao;

	@Override
	@Transactional
	public void callInsertNotiPopProc ( notiPopVo notipopVo, String callType ) throws Exception {

		int notCnt = dao.getNotiPopFind ( notipopVo );

		if ( notCnt > 0 ) {// 기 데이터 존재
			notipopVo.setAct_gbn ( "U" );
			if ( "EM".equals ( callType ) ) {// 긴급공지에서 호출
				dao.updateNotiPopEmergencyProc ( notipopVo );
			} else if ( "PO".equals ( callType ) ) {// 팝업공지에서 호출
				dao.updateNotiPopNotiProc ( notipopVo );
			}
			dao.updateNotiPopProcLog ( notipopVo );
		} else {// 기 데이터 없음
			notipopVo.setAct_gbn ( "I" );
			notipopVo.setNoti_no(String.valueOf(dao.getMaxNotiNo()));
			if ( "EM".equals ( callType ) ) {// 긴급공지에서 호출
				dao.insertNotiPopEmergencyProc ( notipopVo );
			} else if ( "PO".equals ( callType ) ) {// 팝업공지에서 호출
				dao.insertNotiPopNotiProc ( notipopVo );
			}
			dao.insertNotiPopProcLog ( notipopVo );
		}

	}

	@Override
	@Transactional
	public void callDeleteNotiPopProc ( notiPopVo notipopVo, String callType ) throws Exception {
		int notCnt = dao.getNotiPopFind ( notipopVo );
		if ( notCnt > 0 ) {// 기 데이터 존재
			if ( "EM".equals ( callType ) ) {// 긴급공지에서 호출
				notipopVo.setStatus ( "" );
				notipopVo.setNet_type ( "" );
				notipopVo.setMessage ( "" );
				notipopVo.setMessage_yn ( "" );
				dao.updateNotiPopEmergencyProc ( notipopVo );
			} else if ( "PO".equals ( callType ) ) {// 팝업공지에서 호출
				notipopVo.setReg_no ( "" );
				notipopVo.setBbs_id ( "" );
				dao.updateNotiPopNotiProc ( notipopVo );
			}
			notipopVo.setAct_gbn ( "U" );
			dao.updateNotiPopProcLog ( notipopVo );
		}

		// 긴급공지 팝업공지 둘다 비어있는 데이터가 있다면
		if ( 0 < dao.getNotiPopDeleteFind ( ) ) {
			notipopVo.setAct_gbn ( "D" );
			// dao.deleteNotiPopProcLog(notipopVo); U로그에서 어차피 알수 있다.
			dao.deleteNotiPopProc ( );
		}
	}

	@Override
	public getEmergencyViewVo getEmergencyView ( getEmergencyVo getemergencyVo ) throws Exception {
		return dao.getEmergencyView ( getemergencyVo );
	}

}
