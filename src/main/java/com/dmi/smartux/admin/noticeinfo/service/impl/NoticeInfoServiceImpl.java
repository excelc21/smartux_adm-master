package com.dmi.smartux.admin.noticeinfo.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmi.smartux.admin.noticeinfo.dao.NoticeInfoDao;
import com.dmi.smartux.admin.noticeinfo.service.NoticeInfoService;
import com.dmi.smartux.admin.noticeinfo.vo.InsertNoticeInfoVo;
import com.dmi.smartux.admin.noticeinfo.vo.NotiCodeListVo;
import com.dmi.smartux.admin.noticeinfo.vo.NotiMainCodeListVo;
import com.dmi.smartux.admin.noticeinfo.vo.NoticeInfoListProcVo;
import com.dmi.smartux.admin.noticeinfo.vo.NoticeInfoListVo;
import com.dmi.smartux.admin.noticeinfo.vo.UpdateNoticeInfoVo;
import com.dmi.smartux.admin.noticeinfo.vo.ViewNoticeInfoListVo;
import com.dmi.smartux.cache.service.CacheIntervalService;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.service.CacheService;
import com.dmi.smartux.common.util.GlobalCom;

@Service("adminNoticeInfoServiceImpl")
public class NoticeInfoServiceImpl implements NoticeInfoService {

	@Autowired
	NoticeInfoDao dao;
	
	@Override
	public List<NoticeInfoListVo> getNoticeInfoList(
			NoticeInfoListProcVo fourchlistprocVo) throws Exception {
		
		fourchlistprocVo.setStart_rnum(fourchlistprocVo.getPageNum()*fourchlistprocVo.getPageSize()-fourchlistprocVo.getPageSize()+1);
		fourchlistprocVo.setEnd_rnum(fourchlistprocVo.getStart_rnum()+fourchlistprocVo.getPageSize()-1);
		
		return dao.getNoticeInfoList(fourchlistprocVo);
	}

	@Override
	public int getNoticeInfoListTotalCnt(NoticeInfoListProcVo fourchlistprocVo)
			throws Exception {
		int result = 0;
		
		result = dao.getNoticeInfoListTotalCnt(fourchlistprocVo);
		
		return result;
	}

	@Override
	public List<NotiCodeListVo> getNoticeInfoCodeList() throws Exception {
		return dao.getNoticeInfoCodeList();
	}

	@Override
	public List<NotiMainCodeListVo> getNotiMainCodeList() throws Exception {
		return dao.getNotiMainCodeList();
	}

	@Override
	public void updateNoticeInfo(UpdateNoticeInfoVo updatenoticeinfoVo)
			throws Exception {
		dao.updateNoticeInfo(updatenoticeinfoVo);
	}

	@Override
	public void insertNoticeInfo(InsertNoticeInfoVo insertnoticeinfoVo)
			throws Exception {
		dao.insertNoticeInfo(insertnoticeinfoVo);
	}

	@Override
	public void deleteNoticeInfo(String seq) throws Exception {
		dao.deleteNoticeInfo(seq);
	}

	@Override
	public HashMap<String, List<ViewNoticeInfoListVo>> viewNoticeInfoList(String service)
			throws Exception {
		
		List<ViewNoticeInfoListVo> viewnoticeinfolistVo = dao.viewNoticeInfoList(service);
		

		HashMap<String, List<ViewNoticeInfoListVo>> noti_Map = new HashMap<String, List<ViewNoticeInfoListVo>>();
		List<ViewNoticeInfoListVo> txtVo = new ArrayList<ViewNoticeInfoListVo>();
		List<ViewNoticeInfoListVo> imgVo = new ArrayList<ViewNoticeInfoListVo>();
		
		for(ViewNoticeInfoListVo tmpVo : viewnoticeinfolistVo){
			if(tmpVo.getNtype().equals("TXT")){
				txtVo.add(tmpVo);
			}else if(tmpVo.getNtype().equals("IMG")){
				imgVo.add(tmpVo);
			}
		}
		
		if(txtVo!=null) noti_Map.put("TXT", txtVo);
		if(imgVo!=null) noti_Map.put("IMG", imgVo);
		
		return noti_Map;
	}
	
}
