package com.dmi.smartux.admin.notipop.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.dmi.smartux.common.dao.CommonDao;
import com.dmi.smartux.admin.notipop.vo.getEmergencyViewVo;
import com.dmi.smartux.admin.notipop.vo.getEmergencyVo;
import com.dmi.smartux.admin.notipop.vo.notiPopVo;

@Repository
public class NotiAdminPopDao extends CommonDao {

	/**
	 * 팝업공지만 저장
	 * 
	 * @param notipopVo
	 * @throws DataAccessException
	 * @throws Exception
	 */
	public void insertNotiPopNotiProc ( notiPopVo notipopVo ) throws DataAccessException, Exception {

		getSqlMapClientTemplate ( ).insert ( "admin_notipop.insertNotiPopNotiProc", notipopVo );

	}

	/**
	 * 긴급공지만 저장
	 * 
	 * @param notipopVo
	 * @throws DataAccessException
	 * @throws Exception
	 */
	public void insertNotiPopEmergencyProc ( notiPopVo notipopVo ) throws DataAccessException, Exception {

		getSqlMapClientTemplate ( ).insert ( "admin_notipop.insertNotiPopEmergencyProc", notipopVo );

	}

	/**
	 * 등록 로그
	 * 
	 * @param notipopVo
	 * @throws DataAccessException
	 * @throws Exception
	 */
	public void insertNotiPopProcLog ( notiPopVo notipopVo ) throws DataAccessException, Exception {

		getSqlMapClientTemplate ( ).insert ( "admin_notipop.insertNotiPopProcLog", notipopVo );

	}

	/**
	 * 전체 공지 수정
	 * 
	 * @param notipopVo
	 * @throws DataAccessException
	 */
	public void updateNotiPopProc ( notiPopVo notipopVo ) throws DataAccessException {

		getSqlMapClientTemplate ( ).update ( "admin_notipop.updateNotiPopProc", notipopVo );

	}

	/**
	 * 업데이트/삭제 로그
	 * 
	 * @param notipopVo
	 * @throws DataAccessException
	 */
	public void updateNotiPopProcLog ( notiPopVo notipopVo ) throws DataAccessException {

		getSqlMapClientTemplate ( ).insert ( "admin_notipop.updateNotiPopProcLog", notipopVo );

	}

	/**
	 * 팝업공지만 수정
	 * 
	 * @param notipopVo
	 * @throws DataAccessException
	 */
	public void updateNotiPopNotiProc ( notiPopVo notipopVo ) throws DataAccessException {

		getSqlMapClientTemplate ( ).update ( "admin_notipop.updateNotiPopNotiProc", notipopVo );

	}

	/**
	 * 긴급공지만 수정
	 * 
	 * @param notipopVo
	 * @throws DataAccessException
	 */
	public void updateNotiPopEmergencyProc ( notiPopVo notipopVo ) throws DataAccessException {

		getSqlMapClientTemplate ( ).update ( "admin_notipop.updateNotiPopEmergencyProc", notipopVo );

	}

	/**
	 * 데이터가 존재하는지 확인
	 * 
	 * @param notipopVo
	 * @return
	 * @throws DataAccessException
	 */
	public int getNotiPopFind ( notiPopVo notipopVo ) throws DataAccessException {
		int result = 0;
		result = (Integer) getSqlMapClientTemplate ( ).queryForObject ( "admin_notipop.getNotiPopFind", notipopVo );

		return result;
	}

	/**
	 * 삭제할 데이터가 존재하는지
	 * 
	 * @param notipopVo
	 * @return
	 * @throws DataAccessException
	 */
	public int getNotiPopDeleteFind ( ) throws DataAccessException {
		int result = 0;
		result = (Integer) getSqlMapClientTemplate ( ).queryForObject ( "admin_notipop.getNotiPopDeleteFind" );

		return result;
	}

	/**
	 * 삭제
	 * 
	 * @throws DataAccessException
	 */
	public void deleteNotiPopProc ( ) throws DataAccessException {

		getSqlMapClientTemplate ( ).delete ( "admin_notipop.deleteNotiPopProc" );

	}

	/**
	 * 삭제 로그
	 * 
	 * @param notipopVo
	 * @throws DataAccessException
	 */
	public void deleteNotiPopProcLog ( notiPopVo notipopVo ) throws DataAccessException {

		getSqlMapClientTemplate ( ).insert ( "admin_notipop.deleteNotiPopProcLog", notipopVo );

	}

	/**
	 * 긴급(비상)공지 조회
	 * 
	 * @param getemergencyVo
	 * @return
	 * @throws DataAccessException
	 */
	public getEmergencyViewVo getEmergencyView ( getEmergencyVo getemergencyVo ) throws DataAccessException {

		getEmergencyViewVo result = null;
		result = (getEmergencyViewVo) getSqlMapClientTemplate ( ).queryForObject ( "admin_notipop.getEmergencyView", getemergencyVo );

		return result;
	}
	
	/**
	 * 긴급공지 순번 조회(MAX+1)
	 * @throws DataAccessException
	 * @throws Exception
	 */
	public int getMaxNotiNo() throws DataAccessException, Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("admin_notipop.getMaxNotiNo");
	}

}