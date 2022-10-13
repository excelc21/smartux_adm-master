package com.dmi.smartux.admin.imcs.controller;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dmi.smartux.admin.imcs.vo.goodsVO;
import com.dmi.smartux.admin.imcs.vo.paramVO;
import com.dmi.smartux.admin.imcs.service.goodsService;




@Controller
public class goodsController {

	@Autowired
    goodsService goodsService;

	/**
	 * IMCS 굿즈 목록 조회 화면
	 * @param model		Model 객체
	 * @return			패널 목록 조회 화면 URL
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/imcs/getGoodsList")
	public String getGoodsList(
	        @RequestParam(value = "goods_type", required = false, defaultValue="") String goods_type,
	        @RequestParam(value = "hiddenName", required = false) String hiddenName,      
	        @RequestParam(value = "textName", required = false) String textName,	        
	        @RequestParam(value = "textHtml", required = false) String textHtml,
			Model model) throws Exception{
		
		paramVO param = new paramVO(); 
		param.setGoods_type(goods_type);
		
		List<goodsVO> result = goodsService.getGoodsList(param);
		
		model.addAttribute("popupName", "IMCS 상품");
		model.addAttribute("result", result);
		model.addAttribute("hiddenName", hiddenName);
		model.addAttribute("textName", textName);
		model.addAttribute("textHtml",textHtml);
		
		return "/admin/imcs/getGoodsList";
	}
    
}
