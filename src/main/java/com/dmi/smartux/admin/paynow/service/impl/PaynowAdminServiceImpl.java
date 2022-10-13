package com.dmi.smartux.admin.paynow.service.impl;

import com.dmi.smartux.admin.paynow.dao.PaynowAdminDao;
import com.dmi.smartux.admin.paynow.service.PaynowAdminService;

import com.dmi.smartux.admin.paynow.vo.PaynowBannerVO;
import com.dmi.smartux.admin.paynow.vo.PaynowReqVO;
import com.dmi.smartux.admin.paynow.vo.PaynowSearchVO;
import com.dmi.smartux.common.property.SmartUXProperties;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;

/**
 * Paynow Info Admin Service Implement
 *
 * @author 
 */
@Service
public class PaynowAdminServiceImpl implements PaynowAdminService {
	
    @Autowired
    PaynowAdminDao mDao;
    
    @Override
    public List<PaynowReqVO> getPaynowReqList(PaynowSearchVO paynowSearchVO) throws Exception {
        return mDao.getPaynowReqList(paynowSearchVO);
    }
    
    @Override
    public PaynowReqVO getPaynowReqInfo(PaynowSearchVO paynowSearchVO) throws Exception {
        return mDao.getPaynowReqInfo(paynowSearchVO);
    }

    @Override
    public int getPaynowReqListCount(PaynowSearchVO paynowSearchVO)  throws Exception {
        return mDao.getPaynowReqListCount(paynowSearchVO);
    }
    
    @Override
    public List<PaynowBannerVO> getBannerList(String code) throws Exception {
        return mDao.getBannerList(code);
    }
    
    @Override
    public PaynowBannerVO getBanner(String code, String banner_id) throws Exception {
        return mDao.getBanner(code, banner_id);
    }
    
    @Override
    public void addBanner(String code, String banner_id, String banner_text, String use_yn, String create_id) throws Exception {
    	mDao.addBanner(code, banner_id, banner_text, use_yn, create_id);
    }
    
    @Override
    public void updateBanner(String code, String banner_id, String banner_text, String use_yn, String update_id) throws Exception {
    	mDao.updateBanner(code, banner_id, banner_text, use_yn, update_id);
    }
    
    @Override
    @Transactional
    public void deleteBanner(String code, String banner_id) throws Exception {
    	String[] lstBanner = banner_id.split(",");

		for(String id : lstBanner) {
			PaynowBannerVO pvo = mDao.getBanner(code, id.trim());

			if (mDao.deleteBanner(code, id.trim()) > 0 && 
					!StringUtils.isEmpty(pvo.getBanner_img())) {
				this.deleteImageFile(pvo.getBanner_img());
			}
		}
    }
    
    @Override
    public boolean deleteImageFile(String fname) throws Exception {
    	if (StringUtils.isEmpty(fname)) {
    		return false;
    	}
    	
    	File file = new File(SmartUXProperties.getProperty("paynow.banner.img.path") + fname);
    	return file.delete();
    }
    
}
