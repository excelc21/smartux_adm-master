package com.dmi.smartux.configuration.dao;

import java.net.URLEncoder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import com.dmi.smartux.common.dao.CommonDao;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.configuration.vo.YoutubeAPIVO;
import com.dmi.smartux.configuration.vo.YoutubeResultVO;

@Repository
public class YoutubeAPIDao extends CommonDao{
	
	private final Log logger = LogFactory.getLog(this.getClass()); 	

	public YoutubeResultVO getYoutubeSearchKey() throws Exception {
		
		YoutubeAPIVO dbResult = (YoutubeAPIVO)getSqlMapClientTemplate().queryForObject("youtube.getYoutubeSearchKey");
		
		YoutubeResultVO result = new YoutubeResultVO();
		
		result.setFlag(SmartUXProperties.getProperty("flag.success"));
		result.setMessage(SmartUXProperties.getProperty("message.success"));
		
		if(dbResult == null){
			result.setSearch_key("");
		}else{
			result.setSearch_key(dbResult.getCategory()+"="+URLEncoder.encode(dbResult.getRecommend_text(),"UTF-8"));
		}
		
		return result;
	}
}
