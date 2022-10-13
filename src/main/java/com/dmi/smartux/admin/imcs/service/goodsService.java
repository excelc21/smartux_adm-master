package com.dmi.smartux.admin.imcs.service;

import java.util.List;

import com.dmi.smartux.admin.imcs.vo.goodsVO;
import com.dmi.smartux.admin.imcs.vo.paramVO;

public interface goodsService {
	/**
	 * imcs 인터페이스 테이블에서 상품 조회
	 * @param param
	 * @return List<goodsVO>
	 * @throws Exception
	 */
    public List<goodsVO> getGoodsList(paramVO param) throws Exception;
}
