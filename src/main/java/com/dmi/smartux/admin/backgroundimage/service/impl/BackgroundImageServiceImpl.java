package com.dmi.smartux.admin.backgroundimage.service.impl;

import com.dmi.smartux.admin.backgroundimage.dao.BackgroundImageDao;
import com.dmi.smartux.admin.backgroundimage.service.BackgroundImageService;
import com.dmi.smartux.admin.backgroundimage.vo.BackgroundImageInfo;
import com.dmi.smartux.admin.backgroundimage.vo.CategoryAlbumVO;
import com.dmi.smartux.common.util.JsonFileator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class BackgroundImageServiceImpl implements BackgroundImageService {

	private final JsonFileator backgroundimage;
	private final BackgroundImageDao dao;
	@Autowired
	public BackgroundImageServiceImpl(@Qualifier("backgroundimage") JsonFileator backgroundimage, BackgroundImageDao dao) {
		this.backgroundimage = backgroundimage;
        this.dao = dao;
    }

	@Override
	public List getBackgroundImageList() throws Exception {
		return this.backgroundimage.getList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void insertBackgroundImage(BackgroundImageInfo info) throws Exception {
		this.backgroundimage.insert(info);
	}

	@Override
	public void deleteBackgroundImage(int[] orderList) throws Exception {
		this.backgroundimage.delete(orderList);
	}
	
	@Override
    public void backgroundImageOrderUpdate(int[] codeList) throws Exception {
        this.backgroundimage.setOrder(codeList);
    }
	
    @Override
    public void setLocationFix(BackgroundImageInfo backgroundImageInfo) throws Exception {
        this.backgroundimage.update(backgroundImageInfo);
    }
    
    @Override
	public List<CategoryAlbumVO> getCategoryAlbumList(String category_id, String type, String series_yn) throws Exception {
		// TODO Auto-generated method stub
		return dao.getCategoryAlbumList(category_id, type, series_yn);
	}
}
