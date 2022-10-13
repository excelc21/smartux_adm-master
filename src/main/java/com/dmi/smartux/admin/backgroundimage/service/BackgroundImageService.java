package com.dmi.smartux.admin.backgroundimage.service;

import java.util.List;

import com.dmi.smartux.admin.backgroundimage.vo.BackgroundImageInfo;
import com.dmi.smartux.admin.backgroundimage.vo.CategoryAlbumVO;

public interface BackgroundImageService {

	
	List getBackgroundImageList() throws Exception;
	
	void insertBackgroundImage(BackgroundImageInfo info) throws Exception;
	
	void deleteBackgroundImage(int[] orderList) throws Exception;
	
	void backgroundImageOrderUpdate(int[] codeList) throws Exception;
	
	void setLocationFix(BackgroundImageInfo info) throws Exception;
	
	/**
	 * 선택된 카테고리에 대한 하위 카테고리와 앨범 정보 조회
	 * @param category_id		카테고리 id
	 * @return					하위 카테고리와 앨범 정보
	 * @throws Exception
	 */
	public List<CategoryAlbumVO> getCategoryAlbumList(String category_id, String type, String series_yn) throws Exception;
}
