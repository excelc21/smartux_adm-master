package com.dmi.smartux.admin.dayinfo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmi.smartux.admin.dayinfo.dao.DayInfoDao;
import com.dmi.smartux.admin.dayinfo.service.DayInfoService;
import com.dmi.smartux.admin.dayinfo.vo.DayInfoParamVo;
import com.dmi.smartux.admin.dayinfo.vo.DayInfoProcVo;
import com.dmi.smartux.admin.dayinfo.vo.DeleteDayInfoProcVo;
import com.dmi.smartux.admin.dayinfo.vo.InsertDayInfoVo;
import com.dmi.smartux.admin.dayinfo.vo.SelectDayInfoDtailVo;
import com.dmi.smartux.admin.dayinfo.vo.UpdateDayInfoVo;

@Service
public class DayInfoServiceImpl implements DayInfoService {

	@Autowired
	DayInfoDao dao;
	
	@Override
	public List<DayInfoProcVo> getDayInfoList(DayInfoParamVo dayinfoparamVo) throws Exception {
		return dao.getDayInfoList(dayinfoparamVo);
	}

	@Override
	public void deleteDayInfo(String seq, String mod_id) throws Exception {
		DeleteDayInfoProcVo deletedayinfoprocVo = new DeleteDayInfoProcVo();
		deletedayinfoprocVo.setMod_id(mod_id);
		
        String[] arrSeq = seq.split(",");
        for(String tmpSeq : arrSeq){
        	deletedayinfoprocVo.setSeq(tmpSeq);
        	dao.deleteDayInfo(deletedayinfoprocVo);
        }
	}

	@Override
	public void insertDayInfoProc(InsertDayInfoVo insertdayinfoVo) throws Exception {
		dao.insertDayInfoProc(insertdayinfoVo);
	}

	@Override
	public SelectDayInfoDtailVo getDayInfoDatail(int seq) throws Exception {
		return dao.getDayInfoDatail(seq);
	}

	@Override
	public void updateDayInfoProc(UpdateDayInfoVo updatedayinfoVo) throws Exception {
		dao.updateDayInfoProc(updatedayinfoVo);
		
	}

}
