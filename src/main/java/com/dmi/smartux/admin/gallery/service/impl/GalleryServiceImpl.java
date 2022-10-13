package com.dmi.smartux.admin.gallery.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmi.smartux.admin.gallery.dao.GalleryDao;
import com.dmi.smartux.admin.gallery.service.GalleryService;
import com.dmi.smartux.admin.gallery.vo.DeleteGalleryProcVo;
import com.dmi.smartux.admin.gallery.vo.GalleryCateListParamVo;
import com.dmi.smartux.admin.gallery.vo.GalleryOrderListResultVo;
import com.dmi.smartux.admin.gallery.vo.GalleryRelationResultVo;
import com.dmi.smartux.admin.gallery.vo.InsertGalleryProcVo;
import com.dmi.smartux.admin.gallery.vo.SearchGalleryResultVo;
import com.dmi.smartux.admin.gallery.vo.SelectGalleryDetailVo;
import com.dmi.smartux.admin.gallery.vo.UpdateGalleryOrderProcVo;
import com.dmi.smartux.common.exception.SmartUXException;
import com.dmi.smartux.common.property.SmartUXProperties;

@Service
public class GalleryServiceImpl implements GalleryService {
	
	@Autowired
	GalleryDao dao;

	@Override
	public List<SearchGalleryResultVo> getGalleryCateList(GalleryCateListParamVo gallerycatelistparamVo) throws Exception {
		return dao.getGalleryCateList(gallerycatelistparamVo);
	}

	@Override
	public SelectGalleryDetailVo getGalleryDetail(String galleryId) throws Exception {
		return dao.getGalleryDetail(galleryId);
	}

	@Override
	@Transactional
	public void insertGalleryProc(InsertGalleryProcVo insertgalleryprocVo) throws Exception {
		dao.insertGalleryMst(insertgalleryprocVo);
		//디렉토리가 아니라면 상세 등록
		if(!"D".equals(insertgalleryprocVo.getGallery_type())) dao.insertGalleryDetail(insertgalleryprocVo);
	}

	@Override
	@Transactional
	public void updateGalleryProc(InsertGalleryProcVo insertgalleryprocVo) throws Exception {
		dao.updateGalleryMst(insertgalleryprocVo);
		//디렉토리가 아니라면 상세 등록
		if(!"D".equals(insertgalleryprocVo.getGallery_type())) dao.updateGalleryDetail(insertgalleryprocVo);
	}

	@Override
	public String deleteGalleryProc(DeleteGalleryProcVo deletegalleryprocVo) throws Exception {
		GalleryRelationResultVo galleryrelationresultVo = dao.getGalleryRelation(deletegalleryprocVo.getGallery_id());
		if(galleryrelationresultVo.getCnt() > 0)
			throw new SmartUXException(SmartUXProperties.getProperty("flag.child.bedata"), SmartUXProperties.getProperty("message.child.bedata"));
		else {
			dao.deleteGallery(deletegalleryprocVo);
		}
		return galleryrelationresultVo.getP_gallery_id();
	}

	@Override
	public List<GalleryOrderListResultVo> getGalleryOrderList(String pGalleryId) throws Exception {
		return dao.getGalleryOrderList(pGalleryId);
	}
	
	@Override
	@Transactional
	public void galleryOrderChangeProc(String[] gallery_ids, String mod_id) throws Exception {
		UpdateGalleryOrderProcVo updategalleryorderprocVo = new UpdateGalleryOrderProcVo();
		updategalleryorderprocVo.setMod_id(mod_id);
		updategalleryorderprocVo.setOrd(0);
		for (String gallery_id : gallery_ids) {
			updategalleryorderprocVo.setGallery_id(gallery_id);
			updategalleryorderprocVo.setOrd(updategalleryorderprocVo.getOrd()+1);
			dao.updateGalleryOrder(updategalleryorderprocVo);
		}
	}
	
}
