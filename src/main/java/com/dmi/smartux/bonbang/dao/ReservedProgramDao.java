package com.dmi.smartux.bonbang.dao;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import com.dmi.smartux.bonbang.vo.ReservedProgramParamVO;
import com.dmi.smartux.bonbang.vo.ReservedProgramVO;
import com.dmi.smartux.common.dao.CommonDao;
import com.dmi.smartux.common.exception.SmartUXException;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.GlobalCom;

/**
 * 시청예약 DAO 클래스
 * @author YJ KIM
 *
 */
@Repository
public class ReservedProgramDao extends CommonDao {

	private final Log logger = LogFactory.getLog(this.getClass());

	/**
	 * 시청 예약 정보 데이터 개수 조회
	 * @param rVO		ReservedProgramParamVO 객체
	 * @return 시청예약 정보 개수
	 */
	public int getReservedProgramDataCnt(ReservedProgramParamVO rVO) throws DataAccessException{
		int result = 0;
		
		for(int i=0;i<rVO.getArr_service_id().length;i++){
			Map<String, String> param = new HashMap<String, String>();
			param.put("sa_id", rVO.getSa_id());
			param.put("stb_mac", rVO.getStb_mac());
			param.put("service_id", rVO.getArr_service_id()[i]);
			param.put("program_id", rVO.getArr_program_id()[i]);
			
			
			try{
				result += (Integer)getSqlMapClientTemplate().queryForObject("bonbang.cntReservedProgramData", param);
			}catch (DataIntegrityViolationException e) {
				SQLException se = (SQLException) e.getRootCause();
				if(se.getErrorCode() == 1846){	//요일타입이 맞지 않는경우
					result += (Integer)getSqlMapClientTemplate().queryForObject("bonbang.cntReservedProgramData_NotKor", param);
				}
			}
		}

		logger.debug("getReservedProgramDataCnt result = "+result);
		
		
		return result;
	}
	
	/**
	 * 시청 예약 정보 일정 시간안에 존재하는 데이터 개수 조회
	 * @param rVO		ReservedProgramParamVO 객체
	 * @return 시청예약 정보 개수
	 */
	public int getReservedProgramDataTimeCnt(ReservedProgramParamVO rVO) throws DataAccessException{
		int result = 0;
		
		for(int i=0;i<rVO.getArr_service_id().length;i++){
			
			String _repeat_day = (String)rVO.getList_repeat_day().get(i);
			
			//반복 요일만큼 레코드를 입력한다
			if(GlobalCom.isNull(_repeat_day,"").equals("")){
				logger.debug("getReservedProgramDataTimeCnt NULL IN ");
				Map<String, String> param = new HashMap<String, String>();
				param.put("sa_id", rVO.getSa_id());
				param.put("stb_mac", rVO.getStb_mac());
				param.put("service_id", rVO.getArr_service_id()[i]);
				param.put("program_id", rVO.getArr_program_id()[i]);
				param.put("program_stime", rVO.getArr_program_stime()[i]);
				
				try{
					result += (Integer)getSqlMapClientTemplate().queryForObject("bonbang.cntReservedProgramDataTime", param);
				}catch (DataIntegrityViolationException e) {
					SQLException se = (SQLException) e.getRootCause();
					if(se.getErrorCode() == 1846){	//요일타입이 맞지 않는경우
						result += (Integer)getSqlMapClientTemplate().queryForObject("bonbang.cntReservedProgramDataTime_NotKor", param);
					}
				}
			}else{
				logger.debug("getReservedProgramDataTimeCnt NOT NULL IN ");
				logger.debug("getReservedProgramDataTimeCnt NOT length =  "+_repeat_day.length());
				
				String repeatStr = _repeat_day;
				
				for(int j=0;j<repeatStr.length();j++){
					Map<String, String> param = new HashMap<String, String>();
					param.put("sa_id", rVO.getSa_id());
					param.put("stb_mac", rVO.getStb_mac());
					param.put("service_id", rVO.getArr_service_id()[i]);
					param.put("program_id", rVO.getArr_program_id()[i]);
					
					Calendar oCalendar = Calendar.getInstance( );
				    int week = oCalendar.get(Calendar.DAY_OF_WEEK)-1;
					int _weekChk =  Integer.parseInt(repeatStr.substring(j,j+1));
					
					//현재 요일 보다 반복요일이 지났으면 다음주 요일 날짜
					if (_weekChk < week) {
						param.put("program_stime", GlobalCom.getDate((_weekChk - week)+7)+rVO.getArr_program_stime()[i].substring(8));
						
					//현재 요일과 반복요일이 같은 경우 시간으로 다시 체크한다
					}else if(week == _weekChk){
						//시간이 지난 경우 다음주 요일 날짜
						if(Integer.parseInt(GlobalCom.getTodayFormat4_24().substring(8)) >= Integer.parseInt(rVO.getArr_program_stime()[i].substring(8))){
							param.put("program_stime", GlobalCom.getDate(7)+rVO.getArr_program_stime()[i].substring(8));
						//시간이 지나지 않은 경우 금일 날짜
						}else{
							param.put("program_stime", GlobalCom.getDate(0)+rVO.getArr_program_stime()[i].substring(8));
						}
						
					//현재 요일이 지나지 않은 경우 금주 요일 날짜 
					}else{
						param.put("program_stime", GlobalCom.getDate(_weekChk - week)+rVO.getArr_program_stime()[i].substring(8));
					}
					
					try{
						result += (Integer)getSqlMapClientTemplate().queryForObject("bonbang.cntReservedProgramDataTime", param);
					}catch (DataIntegrityViolationException e) {
						SQLException se = (SQLException) e.getRootCause();
						if(se.getErrorCode() == 1846){	//요일타입이 맞지 않는경우
							result += (Integer)getSqlMapClientTemplate().queryForObject("bonbang.cntReservedProgramDataTime_NotKor", param);
						}
					}
				}
			}
		}
		
//		//반복 요일만큼 레코드를 입력한다
//		if(GlobalCom.isNull(rVO.getRepeat_day(),"").equals("")){
//			logger.debug("getReservedProgramDataTimeCnt NULL IN ");
//			Map<String, String> param = new HashMap<String, String>();
//			param.put("sa_id", rVO.getSa_id());
//			param.put("stb_mac", rVO.getStb_mac());
//			param.put("service_id", rVO.getService_id());
//			param.put("program_id", rVO.getProgram_id());
//			param.put("program_stime", rVO.getProgram_stime());
//			
//			result = (Integer)getSqlMapClientTemplate().queryForObject("bonbang.cntReservedProgramDataTime", param);
//			
//		}else{
//			logger.debug("getReservedProgramDataTimeCnt NOT NULL IN ");
//			logger.debug("getReservedProgramDataTimeCnt NOT length =  "+rVO.getRepeat_day().length());
//			
//			String repeatStr = rVO.getRepeat_day();
//			for(int i=0;i<repeatStr.length();i++){
//				Map<String, String> param = new HashMap<String, String>();
//				param.put("sa_id", rVO.getSa_id());
//				param.put("stb_mac", rVO.getStb_mac());
//				param.put("service_id", rVO.getService_id());
//				param.put("program_id", rVO.getProgram_id());
//				
//				Calendar oCalendar = Calendar.getInstance( );
//			    int week = oCalendar.get(Calendar.DAY_OF_WEEK)-1;
//				int _weekChk =  Integer.parseInt(repeatStr.substring(i,i+1));
//				
//				//현재 요일 보다 반복요일이 지났으면 다음주 요일 날짜
//				if (_weekChk < week) {
//					param.put("program_stime", GlobalCom.getDate((_weekChk - week)+7)+rVO.getProgram_stime().substring(8));
//					
//				//현재 요일과 반복요일이 같은 경우 시간으로 다시 체크한다
//				}else if(week == _weekChk){
//					//시간이 지난 경우 다음주 요일 날짜
//					if(Integer.parseInt(GlobalCom.getTodayFormat4_24().substring(8)) >= Integer.parseInt(rVO.getProgram_stime().substring(8))){
//						param.put("program_stime", GlobalCom.getDate(7)+rVO.getProgram_stime().substring(8));
//					//시간이 지나지 않은 경우 금일 날짜
//					}else{
//						param.put("program_stime", GlobalCom.getDate(0)+rVO.getProgram_stime().substring(8));
//					}
//					
//				//현재 요일이 지나지 않은 경우 금주 요일 날짜 
//				}else{
//					param.put("program_stime", GlobalCom.getDate(_weekChk - week)+rVO.getProgram_stime().substring(8));
//				}
//				
//				result += (Integer)getSqlMapClientTemplate().queryForObject("bonbang.cntReservedProgramDataTime", param);
//			}
//		}
		
		logger.debug("getReservedProgramDataCnt result = "+result);
		
		return result;
	}
	
	
	/**
	 * 시청 예약 정보 추가
	 * @param rVO	ReservedProgramParamVO 객체
	 * @throws DataAccessException
	 */
	public void setReservedProgramData(ReservedProgramParamVO rVO) throws DataAccessException{
		
		ReservedProgramParamVO paramVO = rVO;
		
		for(int i=0;i<rVO.getArr_service_id().length;i++){
			String _repeat_day = (String)rVO.getList_repeat_day().get(i);
		
			//반복 요일만큼 레코드를 입력한다
			if(GlobalCom.isNull(_repeat_day,"").equals("")){
				logger.debug("setReservedProgramData NULL IN ");
				
				paramVO.setService_id(rVO.getArr_service_id()[i]);
				paramVO.setChannel_no(rVO.getArr_channel_no()[i]);
				paramVO.setChannel_name(rVO.getArr_channel_name()[i]);
				paramVO.setProgram_id(rVO.getArr_program_id()[i]);
				paramVO.setProgram_name(rVO.getArr_program_name()[i]);
				paramVO.setProgram_info(rVO.getArr_program_info()[i]);
				paramVO.setDefin_flag(rVO.getArr_defin_flag()[i]);
				paramVO.setProgram_stime(rVO.getArr_program_stime()[i]);
				paramVO.setRepeat_day("");
				
				getSqlMapClientTemplate().insert("bonbang.insertReservedProgramData", paramVO);
			}else{
				logger.debug("setReservedProgramData NOT NULL IN ");
				logger.debug("setReservedProgramData NOT length =  "+_repeat_day);
				
				paramVO.setService_id(rVO.getArr_service_id()[i]);
				paramVO.setChannel_no(rVO.getArr_channel_no()[i]);
				paramVO.setChannel_name(rVO.getArr_channel_name()[i]);
				paramVO.setProgram_id(rVO.getArr_program_id()[i]);
				paramVO.setProgram_name(rVO.getArr_program_name()[i]);
				paramVO.setProgram_info(rVO.getArr_program_info()[i]);
				paramVO.setDefin_flag(rVO.getArr_defin_flag()[i]);
				paramVO.setProgram_stime(rVO.getArr_program_stime()[i]);
				
				String repeatStr = _repeat_day;
				for(int j=0;j<repeatStr.length();j++){
					paramVO.setRepeat_day(repeatStr.substring(j,j+1));
					getSqlMapClientTemplate().insert("bonbang.insertReservedProgramData", paramVO);
				}
			}
		
		}
	}
	
	/**
	 * 시청 예약 정보 삭제
	 * @param rVO	ReservedProgramParamVO 객체
	 * @throws DataAccessException
	 */
	public int delReservedProgramData(ReservedProgramParamVO rVO) throws DataAccessException{
		int result = 0;
		boolean delCheck = false;
		
		ReservedProgramParamVO paramVO = rVO;
		
//		//반복 요일만큼 레코드를 삭제한다
//		if(GlobalCom.isNull(rVO.getRepeat_day(),"").equals("")){
//			logger.debug("delReservedProgramData NULL IN ");
//			result = (Integer)getSqlMapClientTemplate().delete("bonbang.deleteReservedProgramData", rVO);
//		}else{
//			logger.debug("delReservedProgramData NOT NULL IN ");
//			logger.debug("delReservedProgramData NOT length =  "+rVO.getRepeat_day().length());
//			
//			String repeatStr = rVO.getRepeat_day();
//			for(int i=0;i<repeatStr.length();i++){
//				paramVO.setRepeat_day(repeatStr.substring(i,i+1));
//				result += (Integer)getSqlMapClientTemplate().delete("bonbang.deleteReservedProgramData", paramVO);
//			}
//		}
		
		for(int i=0;i<rVO.getArr_service_id().length;i++){
			String _repeat_day = (String)rVO.getList_repeat_day().get(i);
			
			//반복 요일만큼 레코드를 입력한다
			if(GlobalCom.isNull(_repeat_day,"").equals("")){
				int delCheckCnt = 0;
				logger.debug("delReservedProgramData NULL IN ");
				
				paramVO.setService_id(rVO.getArr_service_id()[i]);
				paramVO.setProgram_id(rVO.getArr_program_id()[i]);
				paramVO.setRepeat_day("");
				
				delCheckCnt = (Integer)getSqlMapClientTemplate().delete("bonbang.deleteReservedProgramData", paramVO);
				result += delCheckCnt;
				if(delCheckCnt <= 0){
					delCheck = true;
					break;
				}
			}else{
				logger.debug("delReservedProgramData NOT NULL IN ");
				logger.debug("delReservedProgramData NOT length =  "+_repeat_day);
				
				paramVO.setService_id(rVO.getArr_service_id()[i]);
				paramVO.setProgram_id(rVO.getArr_program_id()[i]);
				
				String repeatStr = _repeat_day;
				for(int j=0;j<repeatStr.length();j++){
					int delCheckCnt = 0;
					
					paramVO.setRepeat_day(repeatStr.substring(j,j+1));
					delCheckCnt = (Integer)getSqlMapClientTemplate().delete("bonbang.deleteReservedProgramData", paramVO);
					result += delCheckCnt;
					if(delCheckCnt <= 0){
						delCheck = true;
						break;
					}
				}
			}
			
		}
		
		if(delCheck){
			SmartUXException exception = new SmartUXException();
			//Push쪽 문제로 이경우도 성공으로 표시하라고 함. 130124
			//exception.setFlag(SmartUXProperties.getProperty("flag.beNotData"));
			//exception.setMessage(SmartUXProperties.getProperty("message.beNotData"));
			exception.setFlag(SmartUXProperties.getProperty("flag.success"));
			exception.setMessage(SmartUXProperties.getProperty("message.success"));
			throw exception;
		}
		
		logger.debug("delReservedProgramData NOT result = "+result);
		
		return result;
		
	}
	
	/**
	 *  시청 예약 목록 조회
	 * @param rVO	ReservedProgramParamVO
	 * @return	리스트 객체(ReservedProgramParamVO)
	 * @throws DataAccessException
	 */
	public List<ReservedProgramVO> getReservedProgramList(ReservedProgramParamVO rVO) throws DataAccessException{
		List<ReservedProgramVO> result = null;
		
		try{
			result = getSqlMapClientTemplate().queryForList("bonbang.listReservedProgram", rVO);
		}catch (DataIntegrityViolationException e) {
			SQLException se = (SQLException) e.getRootCause();
			if(se.getErrorCode() == 1846){	//요일타입이 맞지 않는경우
				result = getSqlMapClientTemplate().queryForList("bonbang.listReservedProgram_NotKor", rVO);
			}
		}

		logger.debug("result.size() = " + result.size());
		
		return result;
	}

	/**
	 * 시청 예약 목록 총 개수 조회
	 * @param rVO	ReservedProgramParamVO
	 * @return	시청예약 정보 개수
	 * @throws DataAccessException
	 */
	public int getReservedProgramListTotalCnt(ReservedProgramParamVO rVO) throws DataAccessException{
		int result = 0;
		Map<String, String> param = new HashMap<String, String>();
		param.put("sa_id", rVO.getSa_id());
		param.put("stb_mac", rVO.getStb_mac());
		param.put("app_type", rVO.getApp_type());
		
		try{
			result = (Integer)getSqlMapClientTemplate().queryForObject("bonbang.getReservedProgramListTotalCnt", param);
		}catch (DataIntegrityViolationException e) {
			SQLException se = (SQLException) e.getRootCause();
			if(se.getErrorCode() == 1846){	//요일타입이 맞지 않는경우
				result = (Integer)getSqlMapClientTemplate().queryForObject("bonbang.getReservedProgramListTotalCnt_NotKor", param);
			}
		}
		
		
		logger.debug("getReservedProgramListTotalCnt result = "+result);
		
		return result;
	}

	/**
	 * 시청 예약 정보 전체 삭제
	 * @param rVO	ReservedProgramParamVO
	 * @return	처리 결과
	 * @throws DataAccessException
	 */
	public int removeAllReservedProgram(ReservedProgramParamVO rVO) throws DataAccessException{
		int result = 0;
		result = (Integer)getSqlMapClientTemplate().delete("bonbang.deleteAllReservedProgramData", rVO);
		return result;
	}
}
