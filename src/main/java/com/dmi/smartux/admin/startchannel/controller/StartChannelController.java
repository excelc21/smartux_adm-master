package com.dmi.smartux.admin.startchannel.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dmi.smartux.admin.code.vo.CodeItemVO;
import com.dmi.smartux.admin.startchannel.service.StartChannelService;
import com.dmi.smartux.common.util.HTMLCleaner;

@Controller
public class StartChannelController {

	private final Log logger = LogFactory.getLog(this.getClass());
	
	private static final String START_CHANNEL_CODE = "START_CH_CODE";
	private static final String START_DISCOUNT_CODE = "TV_POINT_DISCOUNT";

	@Autowired
	StartChannelService service;

	/**
	 * 시작 채널 설정 화면
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/trigger/getStartChannel")
	public String getStartChannel(Model model) throws Exception {
		
		CodeItemVO codeItemVO = service.getCodeItem(START_CHANNEL_CODE);
		
		String itemNm = "";
		if(codeItemVO != null){
			itemNm = codeItemVO.getItem_nm();
		}
		
		model.addAttribute("item_code", START_CHANNEL_CODE);
		model.addAttribute("item_nm", itemNm);
		
		return "/admin/trigger/getStartChannel";
	}
	
	
	
	
	
	/**
	 * 가격버튼 설정 화면
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/trigger/getStartDiscount")
	public String getDiscountChannel(Model model) throws Exception {
		
		CodeItemVO codeItemVO = service.getCodeItem(START_DISCOUNT_CODE);
		
		String itemNm = "";
		if(codeItemVO != null){
			itemNm = codeItemVO.getItem_nm();
		}
		
		model.addAttribute("item_code", START_DISCOUNT_CODE);
		model.addAttribute("item_nm", itemNm);
		
		return "/admin/trigger/getStartDiscount";
	}
	
	
	
	
	
	/**
	 * 트리거 정보 수정
	 * @param itemCode
	 * @param itemNm
	 * @param loginId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/trigger/updateStartChannel" , method=RequestMethod.POST)
	public @ResponseBody String updateTriggerInfo(
			@RequestParam(value="itemCode") String itemCode,
			@RequestParam(value="itemNm") String itemNm, 
			@RequestParam(value="smartUXManager") String loginId) throws Exception {
		
		itemCode = HTMLCleaner.clean(itemCode);
		itemNm = HTMLCleaner.clean(itemNm);
		loginId = HTMLCleaner.clean(loginId);

		String resultcode = "";
		String resultmessage = "";
		
		try {
			service.updateCodeItem(itemCode, itemNm, loginId);
			resultcode = "0000";
			resultmessage = "성공";
		} catch (Exception e) {
			resultcode = "9999";
			resultmessage = "코드 수정 실패";
			logger.debug("[updateTriggerInfo] ERROR : " + e.getMessage());
		}
		
		return "{\"flag\" : \"" + resultcode + "\", \"message\" : \"" + resultmessage + "\"}";
	}
	
	
	
}
