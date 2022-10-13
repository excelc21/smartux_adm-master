package com.dmi.smartux.admin.poster.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmi.smartux.admin.poster.dao.PosterAdminDao;
import com.dmi.smartux.admin.poster.service.PosterAdminService;
import com.dmi.smartux.admin.poster.vo.PosterSearchVO;
import com.dmi.smartux.admin.poster.vo.PosterVO;

@Service
public class PosterAdminServiceImpl implements PosterAdminService{

	@Autowired
	PosterAdminDao dao;

	@Override
	public List<PosterVO> getPosterList(PosterSearchVO posterSearchVo) throws Exception {
		return dao.getPosterList(posterSearchVo);
	}
	
	@Override
	public int getPosterListCount(PosterSearchVO posterSearchVo) throws Exception {
		return dao.getPosterListCount(posterSearchVo);
	}
	
	@Override
	public PosterVO getSelectPoster(String albumId, String serviceType) throws Exception {
		return dao.getSelectPoster(albumId, serviceType);
	}
	
	@Override
	public void insertPoster(String serviceType, String albumId, String widthImg, String heightImg,String createId) throws Exception {
		dao.insertPoster(serviceType, albumId, widthImg, heightImg, createId);
	}
	
	@Override
	public void updatePoster(String serviceType, String albumId, String widthImg, String heightImg,String createId) throws Exception {
		dao.updatePoster(serviceType, albumId, widthImg, heightImg, createId);
	}
	
	@Override
	public void deletePoster(String [] albumIds, String [] serviceTypes) throws Exception {
		for(int i = 0 ; i < albumIds.length ; i++){
			dao.deletePoster(albumIds[i], serviceTypes[i]);
		}
	}
}
