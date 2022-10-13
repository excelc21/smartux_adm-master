package com.dmi.smartux.configuration.service;

import com.dmi.smartux.configuration.vo.YoutubeResultVO;

public interface YoutubeAPIService {

	YoutubeResultVO getYoutubeSearchKey(String sa_id, String stb_mac, String app_type, String callByScheduler) throws Exception;

}
