package com.dmi.smartux.admin.trigger.controller;

import java.util.List;

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
import com.dmi.smartux.admin.gpack.event.vo.TVChannelVO;
import com.dmi.smartux.admin.trigger.service.TriggerService;
import com.dmi.smartux.common.util.HTMLCleaner;

@Controller
public class TriggerController {

	private final Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	TriggerService service;

	/**
	 * 트리거 설정 화면
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/trigger/getTriggerInfo")
	public String getTriggerInfo(
			@RequestParam(value="itemCode", required=false, defaultValue="TVapp_tr") String itemCode,
			Model model) throws Exception {
		
		itemCode = HTMLCleaner.clean(itemCode);
		
		CodeItemVO codeItemVO = service.getCodeItem(itemCode);
		
		String itemNm = "";
		if(codeItemVO != null){
			itemNm = codeItemVO.getItem_nm();
		}
		
		model.addAttribute("item_code", itemCode);
		model.addAttribute("item_nm", itemNm);
		
		return "/admin/trigger/getTriggerInfo";
	}
	
	/**
	 * 트리거 정보 수정
	 * @param itemCode
	 * @param itemNm
	 * @param loginId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/trigger/updateTriggerInfo" , method=RequestMethod.POST)
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
	
	/**
	 * 채널 설정 화면
	 * @param srch_channel_name		검색어:채널명
	 * @param pageNum				페이지 번호
	 * @param pageSize				페이징시 게시물의 노출 개수
	 * @param blockSize				한 화면에 노출할 페이지 번호 개수
	 * @param Model 				Model 객체
	 * @return 팩 등록 화면 URL
	 */
	@RequestMapping(value="/admin/trigger/getChannelInfo")
	public String getChannelInfo(
			@RequestParam(value="srch_channel_name", required=false, defaultValue="") String srch_channel_name,
			@RequestParam(value="pageNum", required=false, defaultValue="1") String pageNum,
			@RequestParam(value="pageSize", required=false, defaultValue="10") String pageSize,
			@RequestParam(value="blockSize", required=false, defaultValue="10") String blockSize,
			@RequestParam(value="opener", required=false, defaultValue="") String opener,
			@RequestParam(value="target", required=false, defaultValue="") String target,
			Model model) throws Exception{
		
		srch_channel_name = HTMLCleaner.clean(srch_channel_name);
		pageNum = HTMLCleaner.clean(pageNum);
		pageSize = HTMLCleaner.clean(pageSize);
		blockSize = HTMLCleaner.clean(blockSize);
		opener = HTMLCleaner.clean(opener);
		target = HTMLCleaner.clean(target);
		
		// TV채널목록 조회
		TVChannelVO tvChannelVO = new TVChannelVO();
		tvChannelVO.setSrch_channel_name(srch_channel_name);
		tvChannelVO.setPageNum(Integer.parseInt(pageNum));
		tvChannelVO.setPageSize(Integer.parseInt(pageSize));
		tvChannelVO.setBlockSize(Integer.parseInt(blockSize));
		
		List<TVChannelVO> tvChannelList = service.getTVChannelList(tvChannelVO);
		int totalCount = service.getTVChannelCount(tvChannelVO);
		logger.debug("[getTVChannelCount] totalCount : " + totalCount);
		
		tvChannelVO.setList(tvChannelList);
		tvChannelVO.setPageCount(totalCount);
		
		model.addAttribute("srch_channel_name", srch_channel_name);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("blockSize", blockSize);
		model.addAttribute("opener", opener);
		model.addAttribute("target", target);
		model.addAttribute("tv_list", tvChannelVO);
		
		return "admin/trigger/getChannelInfo";
	}
}
