package com.dmi.smartux.gpack.pack.service;

import java.util.List;

import com.dmi.smartux.gpack.pack.vo.GpackPackCategoryIdVO;
import com.dmi.smartux.gpack.pack.vo.GpackPackInfoVO;
import com.dmi.smartux.gpack.pack.vo.GpackPackPromotionIdVO;

public interface GpackPackInfoService {
	
	public GpackPackInfoVO getGpackPackInfo(String sa_id, String stb_mac, String pack_id, String callByScheduler) throws Exception;
}
