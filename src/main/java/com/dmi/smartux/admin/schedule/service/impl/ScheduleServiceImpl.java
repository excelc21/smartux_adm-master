package com.dmi.smartux.admin.schedule.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmi.smartux.admin.common.dao.CommMimsDao;
import com.dmi.smartux.admin.imcs.service.ImcsService;
import com.dmi.smartux.admin.schedule.dao.ScheduleDao;
import com.dmi.smartux.admin.schedule.service.ScheduleService;
import com.dmi.smartux.admin.schedule.vo.CategoryAlbumVO;
import com.dmi.smartux.admin.schedule.vo.ScheduleDetailVO;
import com.dmi.smartux.admin.schedule.vo.ScheduleVO;

@Service
public class ScheduleServiceImpl implements ScheduleService {

	private final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	ScheduleDao dao;
	
	@Autowired
	CommMimsDao mimsDao;
	
	@Autowired
	ImcsService imcsService;
	
	@Override
	@Transactional(readOnly=true)
	public List<ScheduleVO> getScheduleList(String category_gb) throws Exception {
		// TODO Auto-generated method stub
		return dao.getScheduleList(category_gb);
	}

	@Override
	@Transactional(readOnly=true)
	public ScheduleVO viewSchedule(String schedule_code) throws Exception {
		// TODO Auto-generated method stub
		return dao.viewSchedule(schedule_code);
	}
	

	@Override
	@Transactional
	public void updateSchedule(String schedule_code, String schedule_name, String [] album_id, String [] category_id, String update_id, String category_gb, String test_id, String variation_id) throws Exception {
		// TODO Auto-generated method stub
		dao.updateSchedule(schedule_code, schedule_name, update_id, category_gb, test_id, variation_id);
		dao.deleteScheduleDetail(schedule_code);
		
		try {
			//2021-04-22 BPAS 편성관리 추가
			imcsService.insertImcs("D", "SUM", schedule_code, "", "", "");
		}catch (Exception e) {
			logger.error("[insertImcs]["+e.getClass().getName()+"]["+e.getMessage()+"]");
		}
		
		int length = album_id.length;
		for(int i=0; i < length; i++){
			insertScheduleDetail(schedule_code, album_id[i], category_id[i], i+1, update_id);
			
			//2021-04-22 BPAS 편성관리 추가
			try {
				imcsService.insertImcs("I", "SUM", schedule_code, "ALB", album_id[i], "N");
			}catch (Exception e) {
				logger.error("[insertImcs]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			}
		}
		
	}

	@Override
	@Transactional
	public void deleteSchedule(String [] schedule_codes) throws Exception {
		for(String schedule_code : schedule_codes){
			dao.deleteSchedule(schedule_code);
			dao.deleteScheduleDetail(schedule_code);
			
			//2021-04-22 BPAS 편성관리 추가
			try {
				imcsService.insertImcs("D", "SUM", schedule_code, "", "", "");
			}catch (Exception e) {
				logger.error("[insertImcs]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			}
		}
	}

	@Override
	@Transactional(readOnly=true)
	public List<ScheduleDetailVO> getScheduleDetailList(String schedule_code, String category_gb) throws Exception {
		// TODO Auto-generated method stub
		return dao.getScheduleDetailList(schedule_code, category_gb);
	}

	/**
	 * 자체편성 등록
	 * @param schedule_name		등록하고자 하는 자체편성 이름
	 * @param create_id			관리자 사이트에 로그인 한 사람의 로그인 id
	 * @return					등록된 자체편성의 shcedule_code
	 * @throws Exception
	 */
	private long insertSchedule(String schedule_name, String create_id, String category_gb, String test_id, String variation_id) throws Exception {
		// TODO Auto-generated method stub
		long result = dao.insertSchedule(schedule_name, create_id, category_gb, test_id, variation_id);
		return result;
	}
	
	/**
	 * 자체편성 서브 데이터 등록
	 * @param schedule_code		등록하고자 하는 자체편성 서브 데이터가 속하는 자체편성의 schedule_code
	 * @param album_id			앨범 코드
	 * @param category_id		카테고리 코드
	 * @param ordered			앨범 순서
	 * @param create_id			관리자 사이트에 로그인 한 사람의 로그인 id
	 * @throws Exception
	 */
	private void insertScheduleDetail(String schedule_code, String album_id, String category_id, int ordered, String create_id) throws Exception {
		// TODO Auto-generated method stub
		dao.insertScheduleDetail(schedule_code, album_id, category_id, ordered, create_id);
	}
	
	@Override
	@Transactional
	public void insertSchedule(String schedule_name, String[] album_id, String [] category_id, String create_id, String category_gb, String test_id, String variation_id) throws Exception{
		// TODO Auto-generated method stub
		String schedule_code = String.valueOf(insertSchedule(schedule_name, create_id, category_gb, test_id, variation_id));
		
		int length = album_id.length;
		for(int i=0; i < length; i++){
			insertScheduleDetail(schedule_code, album_id[i], category_id[i], i+1, create_id);
			
			//2021-04-22 BPAS 편성관리 추가
			try {
				imcsService.insertImcs("I", "SUM", schedule_code, "ALB", album_id[i], "N");
			}catch (Exception e) {
				logger.error("[insertImcs]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			}
		}
	}

	@Override
	@Transactional(readOnly=true)
	public List<CategoryAlbumVO> getCategoryAlbumList(String category_id, String type, String series_yn) throws Exception {
		// TODO Auto-generated method stub
		return dao.getCategoryAlbumList(category_id, type, series_yn);
	}

	@Override
	@Transactional(readOnly=true)
	public List<ScheduleVO> selectUseSchedule(String schedule_codes) throws Exception {
		// TODO Auto-generated method stub
		return mimsDao.selectUseSchedule(schedule_codes);
	}

	/**
	 * 메인패널 편성 시 이미지 등록 및 수정 16.12.09
	 */
	@Override
	public void mergeContentsImg(String albumId, String widthImg, String heightImg,String createId) throws Exception {
		dao.mergeContentsImg(albumId, widthImg, heightImg, createId);
	}

	@Override
	@Transactional(readOnly=true)
	public List<CategoryAlbumVO> getAlbumListByCategoryIdAlbumId(String category_id,String album_id,String type) throws Exception {
		// TODO Auto-generated method stub
		return dao.getAlbumListByCategoryIdAlbumId(category_id,album_id,type);
	}
	
	@Override
	@Transactional
	public void changeOrder(String[] orderList, int schedule_code) throws Exception {
		/*
		 * int count = orderList.size();
		 * 
		 * for (int i=0; i<count; i++) { ScheduleVO vo = new ScheduleVO();
		 * vo.setSchedule_code(Integer.parseInt(orderList.get(i)));
		 * vo.setOrder(String.valueOf(i + 1)); dao.changeOrder(vo); }
		 */
		
		ScheduleDetailVO vo = new ScheduleDetailVO();
		int idx = 1;
		
		for(String orderId : orderList){
			vo = new ScheduleDetailVO();
        	int index = orderId.indexOf("^");
			String type_id = orderId.substring(0, index);
			String content_type = orderId.substring(index+1);
			
			vo.setSchedule_code(schedule_code);
			vo.setCategory_id(type_id);
			vo.setAlbum_id(content_type);
			vo.setOrdered(idx);
        	
			dao.changeOrder(vo);     	
        	idx++;
		}
	}
}
