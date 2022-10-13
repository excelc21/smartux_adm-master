package com.dmi.smartux.admin.gallery.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.dmi.smartux.admin.gallery.vo.DeleteGalleryProcVo;
import com.dmi.smartux.admin.gallery.vo.GalleryCateListParamVo;
import com.dmi.smartux.admin.gallery.vo.GalleryOrderListResultVo;
import com.dmi.smartux.admin.gallery.vo.GalleryRelationResultVo;
import com.dmi.smartux.admin.gallery.vo.InsertGalleryProcVo;
import com.dmi.smartux.admin.gallery.vo.SearchGalleryResultVo;
import com.dmi.smartux.admin.gallery.vo.SelectGalleryDetailVo;
import com.dmi.smartux.admin.gallery.vo.UpdateGalleryOrderProcVo;
import com.dmi.smartux.common.dao.CommonDao;

@Repository
public class GalleryDao extends CommonDao {
	
	/**
	 * 갤러리 목록 조회
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<SearchGalleryResultVo> getGalleryCateList(GalleryCateListParamVo gallerycatelistparamVo) throws DataAccessException {
		return getSqlMapClientTemplate().queryForList("admin_gallery.getGalleryCateList", gallerycatelistparamVo);	
	}
	
	/**
	 * 갤러리 상세 조회
	 * @param pGalleryId
	 * @return
	 * @throws DataAccessException
	 */
	public SelectGalleryDetailVo getGalleryDetail(String galleryId) throws DataAccessException {
		return (SelectGalleryDetailVo) getSqlMapClientTemplate().queryForObject("admin_gallery.getGalleryDetail", galleryId);	
	}
	
	
	/**
	 * 갤러리 마스터정보 등록
	 * @param insertgalleryprocVo
	 * @throws DataAccessException
	 */
	public void insertGalleryMst(InsertGalleryProcVo insertgalleryprocVo) throws DataAccessException{
		getSqlMapClientTemplate().insert("admin_gallery.insertGalleryMst",insertgalleryprocVo);
	}
	
	/**
	 * 갤러리 상세정보 등록
	 * @param insertgalleryprocVo
	 * @throws DataAccessException
	 */
	public void insertGalleryDetail(InsertGalleryProcVo insertgalleryprocVo) throws DataAccessException{
		getSqlMapClientTemplate().insert("admin_gallery.insertGalleryDetail",insertgalleryprocVo);
	}
	
	
	/**
	 * 갤러리 마스터정보 수정
	 * @param insertgalleryprocVo
	 * @throws DataAccessException
	 */
	public void updateGalleryMst(InsertGalleryProcVo insertgalleryprocVo) throws DataAccessException{
		getSqlMapClientTemplate().update("admin_gallery.updateGalleryMst",insertgalleryprocVo);
	}
	
	/**
	 * 갤러리 상세정보 수정
	 * @param insertgalleryprocVo
	 * @throws DataAccessException
	 */
	public void updateGalleryDetail(InsertGalleryProcVo insertgalleryprocVo) throws DataAccessException{
		getSqlMapClientTemplate().update("admin_gallery.updateGalleryDetail",insertgalleryprocVo);
	}
	
	/**
	 * 현재 갤러리정보의 자식 갤러리가 존재하는지 확인
	 * @param galleryId
	 * @return
	 * @throws DataAccessException
	 */
	public GalleryRelationResultVo getGalleryRelation(String galleryId) throws DataAccessException {
		return (GalleryRelationResultVo) getSqlMapClientTemplate().queryForObject("admin_gallery.getGalleryRelation", galleryId);	
	}
	
	/**
	 * 갤러리 정보를 삭제한다.
	 * @param deletegalleryprocVo
	 * @throws DataAccessException
	 */
	public void deleteGallery(DeleteGalleryProcVo deletegalleryprocVo) throws DataAccessException {
		getSqlMapClientTemplate().update("admin_gallery.deleteGallery", deletegalleryprocVo);	
	}
	
	/**
	 * 갤러리 순서변경을 위한 리스트
	 * @param pGalleryId
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<GalleryOrderListResultVo> getGalleryOrderList(String pGalleryId) throws DataAccessException {
		return getSqlMapClientTemplate().queryForList("admin_gallery.getGalleryOrderList", pGalleryId);	
	}
	
	/**
	 * 갤러리 순서변경
	 * @param updategalleryorderprocVo
	 * @throws DataAccessException
	 */
	public void updateGalleryOrder(UpdateGalleryOrderProcVo updategalleryorderprocVo) throws DataAccessException{
		getSqlMapClientTemplate().update("admin_gallery.updateGalleryOrder",updategalleryorderprocVo);
	}

}
