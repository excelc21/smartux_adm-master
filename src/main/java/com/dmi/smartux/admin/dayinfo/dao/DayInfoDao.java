package com.dmi.smartux.admin.dayinfo.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.dmi.smartux.admin.dayinfo.vo.DayInfoParamVo;
import com.dmi.smartux.admin.dayinfo.vo.DayInfoProcVo;
import com.dmi.smartux.admin.dayinfo.vo.DeleteDayInfoProcVo;
import com.dmi.smartux.admin.dayinfo.vo.InsertDayInfoVo;
import com.dmi.smartux.admin.dayinfo.vo.SelectDayInfoDtailVo;
import com.dmi.smartux.admin.dayinfo.vo.UpdateDayInfoVo;
import com.dmi.smartux.common.dao.CommonDao;

@Repository
public class DayInfoDao extends CommonDao {
	
	/**
	 * 일별정보 조회
	 * @param dayinfoparamVo
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<DayInfoProcVo> getDayInfoList(DayInfoParamVo dayinfoparamVo) throws DataAccessException {
		return getSqlMapClientTemplate().queryForList("admin_dayinfo.getDayInfoList", dayinfoparamVo);	
	}
	
	/**
	 * 일별정보 삭제
	 * @param deletedayinfoprocVo
	 * @throws DataAccessException
	 */
	public void deleteDayInfo(DeleteDayInfoProcVo deletedayinfoprocVo) throws DataAccessException {
		getSqlMapClientTemplate().update("admin_dayinfo.deleteDayInfo", deletedayinfoprocVo);	
	}
	
	/**
	 * 일별정보 등록
	 * @param insertdayinfoVo
	 * @throws DataAccessException
	 */
	public void insertDayInfoProc(InsertDayInfoVo insertdayinfoVo) throws DataAccessException{
		getSqlMapClientTemplate().insert("admin_dayinfo.insertDayInfoProc",insertdayinfoVo);
	}
	
	/**
	 * 일별정보 상세
	 * @param seq
	 * @return
	 * @throws DataAccessException
	 */
	public SelectDayInfoDtailVo getDayInfoDatail(int seq) throws DataAccessException {
		return (SelectDayInfoDtailVo) getSqlMapClientTemplate().queryForObject("admin_dayinfo.getDayInfoDatail", seq);	
	}
	
	/**
	 * 일별정보 수정
	 * @param updatedayinfoVo
	 * @throws DataAccessException
	 */
	public void updateDayInfoProc(UpdateDayInfoVo updatedayinfoVo) throws DataAccessException {
		getSqlMapClientTemplate().update("admin_dayinfo.updateDayInfoProc", updatedayinfoVo);	
	}
	
}
