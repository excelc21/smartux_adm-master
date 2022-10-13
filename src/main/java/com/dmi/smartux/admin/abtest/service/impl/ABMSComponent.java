package com.dmi.smartux.admin.abtest.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.dmi.smartux.common.exception.SmartUXException;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.Base64;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.util.HttpCommUtil;
import com.dmi.smartux.common.util.SHA512Hash;

/**
 * 2021.04.16 AB테스트 MIMS
 * ABMS 호출 Component
 * @author medialog
 *
 */
@Component
public class ABMSComponent {
	private Logger logger = LoggerFactory.getLogger("ABMSComponent");
	
	/**
	 * 2021.04.16 AB테스트 MIMS
	 * ABMS 호출(Variation)
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public String callABMSVariation(Map<String, String> params) throws Exception {
		String result = "";
		try{
			Map<String, String> header = new HashMap<String, String>();
			String headerValue = GlobalCom.getCustomTodayFormat("yyyyMMddHH") + SmartUXProperties.getProperty("call.abms.header.key");
			header.put("LGUplus-ABMS-Key", Base64.encodeString(SHA512Hash.getDigest(headerValue)));
			
			String url = SmartUXProperties.getProperty("call.abms.variation.url");
			String encoding = SmartUXProperties.getProperty("call.abms.encoding");
			
			result = HttpCommUtil.callGetHttpClient(url, header, params, encoding);
			
		}catch(Exception e){
			logger.warn("callABMSVariation Error [" + e + "]");
		}
		
		return result;
	}
	
	/**
	 * 2021.04.16 AB테스트 MIMS
	 * ABMS 호출(Variation)
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public String callABMSVariation2(Map<String, String> params) throws Exception {
		String result = "";
		try{
			Map<String, String> header = new HashMap<String, String>();
			String headerValue = GlobalCom.getCustomTodayFormat("yyyyMMddHH") + SmartUXProperties.getProperty("call.abms.header.key");
			header.put("LGUplus-ABMS-Key", Base64.encodeString(SHA512Hash.getDigest(headerValue)));
			String num = null;
			
			String url = SmartUXProperties.getProperty("call.abms.variation.url");
			String encoding = SmartUXProperties.getProperty("call.abms.encoding");
			result = HttpCommUtil.callGetHttpClient(url, header, params, encoding);

		}catch(Exception e){
			logger.warn("callABMSVariation Error [" + e + "]");
			
			SmartUXException ex = new SmartUXException();
			ex.setFlag(SmartUXProperties.getProperty("flag.etc"));
			ex.setMessage(SmartUXProperties.getProperty("message.abms.socket"));
			throw ex;
		}
		
		return result;
	}
	
	/**
	 * 2021.05.13 AB테스트 MIMS
	 * ABMS 호출(organize)
	 * @param jsonStr
	 * @return
	 * @throws Exception
	 */
	public String callABMSOrganize(String jsonStr) throws Exception {
		String result = "";
		try{
			Map<String, String> header = new HashMap<String, String>();
			String headerValue = GlobalCom.getCustomTodayFormat("yyyyMMddHH") + SmartUXProperties.getProperty("call.abms.header.key");
			header.put("LGUplus-ABMS-Key", Base64.encodeString(SHA512Hash.getDigest(headerValue)));
			
			String url = SmartUXProperties.getProperty("call.abms.organize.url");
			String encoding = SmartUXProperties.getProperty("call.abms.encoding");
			
			result = HttpCommUtil.callPutHttpClient(url, header, jsonStr, encoding);
			
		}catch(Exception e){
			logger.warn("callABMSOrganize Error [" + e + "]");
		}
		
		return result;
	}
	
	/**
	 * 2021.05.12 AB테스트 MIMS
	 * ABMS status 명 조회
	 * @param status
	 * @return
	 * @throws Exception
	 */
	public String getStatusName(String status) throws Exception {
		String status_info = SmartUXProperties.getProperty("abms.status.info");
		if(StringUtils.isNotBlank(status_info)){
			String[] info_arr = status_info.split("\\|");
			if(info_arr != null){
				for(String info : info_arr){
					if(StringUtils.isNotBlank(info)){
						String[] arr = info.split("\\^");
						
						if(arr != null){
							if(StringUtils.isNotBlank(arr[0]) && status.equals(arr[0])){
								return arr[1];
							}
						}
					}
				}
			}
		}
		return "";
	}
}
