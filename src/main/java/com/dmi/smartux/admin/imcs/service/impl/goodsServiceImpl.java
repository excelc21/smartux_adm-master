package com.dmi.smartux.admin.imcs.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmi.smartux.admin.imcs.service.goodsService;
import com.dmi.smartux.admin.imcs.vo.goodsVO;
import com.dmi.smartux.admin.imcs.vo.paramVO;
import com.dmi.smartux.admin.imcs.dao.goodsDao;


@Service
public class goodsServiceImpl implements goodsService {
	
    @Autowired
    goodsDao goodsDao;

	@Override
	public List<goodsVO> getGoodsList(paramVO paramVO) throws Exception {
		return goodsDao.getGoodsList(paramVO);
	}

}
