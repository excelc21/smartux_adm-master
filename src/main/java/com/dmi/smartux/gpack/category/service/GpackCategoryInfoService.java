package com.dmi.smartux.gpack.category.service;

import java.util.List;

import com.dmi.smartux.gpack.category.vo.GpackCategoryAlbumInfoVO;
import com.dmi.smartux.gpack.category.vo.GpackCategoryInfoVO;
import com.dmi.smartux.gpack.category.vo.GpackCategoryResult;

public interface GpackCategoryInfoService {

	/**
	 * 카테고리 목록 조회
	 * @param sa_id
	 * @param stb_mac
	 * @param pack_id
	 * @param category_id
	 * @param start_num
	 * @param req_count
	 * @param fh_gbn
	 * @param callByScheduler
	 * @return
	 * @throws Exception
	 */
	public GpackCategoryResult getPackCategory(String sa_id, String stb_mac, String pack_id, String category_id, String start_num, String req_count, String p_start_num, String p_req_count, int fh_gbn, String callByScheduler) throws Exception;
	
}
