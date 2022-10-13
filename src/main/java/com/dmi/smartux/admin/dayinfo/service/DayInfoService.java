package com.dmi.smartux.admin.dayinfo.service;

import java.util.List;

import com.dmi.smartux.admin.dayinfo.vo.DayInfoParamVo;
import com.dmi.smartux.admin.dayinfo.vo.DayInfoProcVo;
import com.dmi.smartux.admin.dayinfo.vo.InsertDayInfoVo;
import com.dmi.smartux.admin.dayinfo.vo.SelectDayInfoDtailVo;
import com.dmi.smartux.admin.dayinfo.vo.UpdateDayInfoVo;

public interface DayInfoService {
	
	/**
	 * 일별정보 조회
	 * @param dayinfoparamVo
	 * @return
	 * @throws Exception
	 */
	public List<DayInfoProcVo> getDayInfoList(DayInfoParamVo dayinfoparamVo) throws Exception;
	
	/**
	 * 일별정보 삭제
	 * @param seq
	 * @param mod_id
	 * @throws Exception
	 */
	public void deleteDayInfo(String seq, String mod_id) throws Exception;
	
	/**
	 * 일별정보 등록
	 * @param insertdayinfoVo
	 * @throws Exception
	 */
	public void insertDayInfoProc(InsertDayInfoVo insertdayinfoVo) throws Exception;
	
	/**
	 * 일별정보 상세
	 * @param seq
	 * @return
	 * @throws Exception
	 */
	public SelectDayInfoDtailVo getDayInfoDatail(int seq) throws Exception;
	
	/**
	 * 일별정보 수정
	 * @param updatedayinfoVo
	 * @throws Exception
	 */
	public void updateDayInfoProc(UpdateDayInfoVo updatedayinfoVo) throws Exception;

}
