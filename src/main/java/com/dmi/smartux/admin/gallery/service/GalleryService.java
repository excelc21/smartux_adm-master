package com.dmi.smartux.admin.gallery.service;

import java.util.List;

import com.dmi.smartux.admin.gallery.vo.DeleteGalleryProcVo;
import com.dmi.smartux.admin.gallery.vo.GalleryCateListParamVo;
import com.dmi.smartux.admin.gallery.vo.GalleryOrderListResultVo;
import com.dmi.smartux.admin.gallery.vo.InsertGalleryProcVo;
import com.dmi.smartux.admin.gallery.vo.SearchGalleryResultVo;
import com.dmi.smartux.admin.gallery.vo.SelectGalleryDetailVo;

public interface GalleryService {
	
	/**
	 * 갤러리 목록 조회
	 * @return
	 * @throws Exception
	 */
	public List<SearchGalleryResultVo> getGalleryCateList(GalleryCateListParamVo gallerycatelistparamVo) throws Exception;
	
	 /**
	  * 갤러리 상세 조회
	  * @param pGalleryId
	  * @return
	  * @throws Exception
	  */
	 public SelectGalleryDetailVo getGalleryDetail(String galleryId) throws Exception;
	 
	 /**
	  * 갤러리 정보 등록
	  * @param insertgalleryprocVo
	  * @throws Exception
	  */
	 public void insertGalleryProc(InsertGalleryProcVo insertgalleryprocVo) throws Exception;
	 
	 /**
	  * 갤러리 정보 수정
	  * @param insertgalleryprocVo
	  * @throws Exception
	  */
	 public void updateGalleryProc(InsertGalleryProcVo insertgalleryprocVo) throws Exception;
	 
	 /**
	  * 갤러리 정보 삭제
	  * @param deletegalleryprocVo
	  * @throws Exception
	  */
	 public String deleteGalleryProc(DeleteGalleryProcVo deletegalleryprocVo) throws Exception;
	 
	 /**
	  * 갤러리 순서변경을 위한 리스트
	  * @param pGalleryId
	  * @return
	  * @throws Exception
	  */
	 public List<GalleryOrderListResultVo> getGalleryOrderList(String pGalleryId) throws Exception;
	 
	 /**
	  * 갤러리 순서변경
	  * @param gallery_ids
	  * @param mod_id
	  * @throws Exception
	  */
	 public void galleryOrderChangeProc(String[] gallery_ids, String mod_id) throws Exception;


}
