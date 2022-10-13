package com.dmi.smartux.admin.notimng.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.dmi.smartux.common.dao.CommonDao;

@Repository
public class NotiMngViewDao extends CommonDao {

	/**
	 * 게시판 목록 조회
	 * 
	 * @param bbs_Gbn 게시판 구분
	 * @return 게시판 목록 정보
	 * @throws DataAccessException
	 */
	@SuppressWarnings( "unchecked" )
	public List<Map<String, String>> getNotiMngViewList ( String bbs_Gbn, String scr_Type ) throws DataAccessException {

		String listNum = null;
		List<Map<String, String>> data = null;
		Map<String, String> param = new HashMap<String, String> ( );
		param.put ( "bbs_Gbn", bbs_Gbn );
		param.put ( "scr_Type", scr_Type );
		// 현재 특정 bbs 의 data 갯수를 count로 가져온다. 그러하기에 값이 없다면 listNum 에는 "0" 값이 들어있다.
		data = getSqlMapClientTemplate ( ).queryForList ( "admin_notimng.getNotiMngViewList", param );

		logger.debug ( "data = " + data );
		// select를 수행한 bbs_Gbn 이 db 에 몇개 가 있는지를 세어서 data의 0 에 listNum 으로 추가한다.
		// 만약 db에 값이 없을 경우 data.add(0, param); 이런식으로 추가한다. 만약 DB에 값이 있을 경우 data.get(0).put("listNum", listNum); 이런식으로
		// 추가한다.
		// 이렇게 하는 이유는 data 에 아무것도 없는데 data.get 할 경우 error 발생되기 때문이다.

		logger.debug ( "getListSize = " + data.size ( ) );
		return data;
	}

	/**
	 * 기존 게시판 목록 UPDATE 작업 수행
	 * 
	 * @param bbs_Id 게시판 아이디
	 * @param bbs_NM 게시판 제목
	 * @throws DataAccessException
	 */
	public void setNotiMngUpdate ( String bbs_Id, String bbs_Nm, String admin, String id, String ip ) throws DataAccessException {

		Map<String, String> param = new HashMap<String, String> ( );
		param.put ( "bbs_Id", bbs_Id );
		param.put ( "bbs_Nm", bbs_Nm );
		param.put ( "bbs_Mod_Id", admin );
		param.put ( "act_id", id );
		param.put ( "act_ip", ip );
		param.put ( "act_gbn", "U" );

		logger.debug ( bbs_Id + " " + bbs_Nm + " " + admin );
		getSqlMapClientTemplate ( ).update ( "admin_notimng.setNotiMngUpdate", param );

		getSqlMapClientTemplate ( ).insert ( "admin_notimng.writeLog2", param );
	}

	/**
	 * 게시판 목록 INSERT 작업 수행
	 * 
	 * @param bbs_Id 게시판 아이디
	 * @param bbs_Nm 게시판 제목
	 * @param bbs_Gbn 게시판 구분
	 * @param bbs_Version 게시판 버전
	 * @throws DataAccessException
	 */
	public String setNotiMngInsert ( String bbs_Id, String bbs_Nm, String bbs_Gbn, String bbs_Version, String admin, String scr_Type, String id, String ip ) throws DataAccessException {
		String result = "SUCCESS";

		Map<String, String> param = new HashMap<String, String> ( );
		param.put ( "act_id", id );
		param.put ( "act_ip", ip );
		param.put ( "act_gbn", "I" );
		param.put ( "bbs_Id", bbs_Id );
		param.put ( "bbs_Nm", bbs_Nm );
		param.put ( "bbs_Gbn", bbs_Gbn );
		param.put ( "bbs_Version", bbs_Version );
		param.put ( "bbs_Reg_Id", admin );
		param.put ( "scr_Type", scr_Type );

		logger.info ( bbs_Id + " " + bbs_Nm + " " + bbs_Gbn + " " + bbs_Version + " " + admin );
		// DB 에 BBS_ID 가 중복일 경우 1 을 반환 없을경우 0 을 반환
		String bbs_IdCheck = (String) getSqlMapClientTemplate ( ).queryForObject ( "admin_notimng.getNotiBbsidCheck", bbs_Id );
		logger.debug ( "bbs_IdCheck = " + bbs_IdCheck );

		// DB 에 BBS_ID 중복값이 없을 때만 insert 작업을 한다.
		if ( bbs_IdCheck.equals ( "0" ) ) {
			logger.info ( bbs_Id + " " + bbs_Nm + " " + bbs_Gbn + " " + bbs_Version + " " + admin );
			getSqlMapClientTemplate ( ).insert ( "admin_notimng.setNotiMngInsert", param );

			getSqlMapClientTemplate ( ).insert ( "admin_notimng.writeLog", param );
		} else {
			// 중복
			result = "DUPLECATE";
		}

		return result;
	}

	/**
	 * 게시판 목록 DELETE 작업 수행
	 * 
	 * @param bbs_Id 게시판 아이디
	 * @param bbs_Nm 게시판 제목
	 * @throws DataAccessException
	 */
	public void setNotiMngDelete ( String bbs_Id, String bbs_Nm, String del_Yn, String id, String ip ) throws DataAccessException {

		Map<String, String> param = new HashMap<String, String> ( );
		param.put ( "bbs_Id", bbs_Id );
		param.put ( "bbs_Nm", bbs_Nm );
		param.put ( "del_Yn", del_Yn );
		param.put ( "act_id", id );
		param.put ( "act_ip", ip );
		param.put ( "act_gbn", "D" );

		getSqlMapClientTemplate ( ).update ( "admin_notimng.setNotiMngDelete", param );
		getSqlMapClientTemplate ( ).insert ( "admin_notimng.writeLog2", param );
	}
}
