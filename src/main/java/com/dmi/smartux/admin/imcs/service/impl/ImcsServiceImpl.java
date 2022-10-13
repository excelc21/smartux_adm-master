package com.dmi.smartux.admin.imcs.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmi.smartux.admin.commonMng.vo.MenuTreeVo;
import com.dmi.smartux.admin.imcs.dao.ImcsDao;
import com.dmi.smartux.admin.imcs.service.ImcsService;
import com.dmi.smartux.admin.imcs.vo.ImcsVO;

/**
 * 2020-06-25 추가
 */
@Service
public class ImcsServiceImpl implements ImcsService {
	
    @Autowired
    ImcsDao imcsDao;

	@Override
	public int insertImcs(String gubun, String mims_type, String mims_id, String imcs_type, String imcs_id, String ab_yn) throws Exception {
		
		ImcsVO param = new ImcsVO();
		param.setMims_type(mims_type);
		param.setMims_id(mims_id);
		param.setImcs_type(imcs_type);
		param.setImcs_id(imcs_id);
		param.setAb_yn(ab_yn);
		
		if("I".equals(gubun)) {
			imcsDao.insertImcsInfo(param);
		}else if("U".equals(gubun)) {
			imcsDao.updateImcsInfo(param);
		}else if("D".equals(gubun)) {
			imcsDao.deleteImcsInfo(param);
		}
		
		return 0;
	}

	@Override
	public int checkImcs(String mims_type, String mims_id) {
		
		ImcsVO param = new ImcsVO();
		param.setMims_type(mims_type);
		param.setMims_id(mims_id);
		
		int count = imcsDao.getImcsInfoCnt(param);
		
		return count;
	}

	@Override
	public void deletePap(String panel_id, String title_id) {
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("pannel_id", panel_id);
		param.put("title_id", title_id);
		
		imcsDao.deletePap(param);
	}


}
