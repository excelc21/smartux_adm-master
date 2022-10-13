package com.dmi.smartux.admin.retry.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dmi.smartux.admin.retry.service.RetryService;

@Controller
public class RetryController {
	
	private final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	RetryService retryService;
	
	/**
	 * PushG/W RETRY 횟수 캐쉬저장
	 * @param callByScheduler	단말에서 호출 했을 경우엔 N, 스케쥴러에서 호출했을땐 Y, 관리자툴에서 조회했을땐 A가 들어간다
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/setPushGateWayRetry")
	public @ResponseBody String setPushGateWayRetry(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value="callByScheduler", required=false, defaultValue="N") String callByScheduler
			) throws Exception{
		
		String resultcode = "";
		try {
			String retryNum = "";
			String code_id = "A0011";
			retryNum = retryService.setPushGateWayRetry(code_id, callByScheduler);
			resultcode = "0000";
		}catch(Exception e){
			logger.info("[setPushGateWayRetry] [Exception] "+e.getClass().getName());
			//throw e;
		}
		
		return resultcode;
		
	}

}
