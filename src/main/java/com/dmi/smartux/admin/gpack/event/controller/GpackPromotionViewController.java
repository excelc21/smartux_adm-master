/**
 * Class Name : GpackPromotionViewController.java
 * Description : 
 *  프로모션 관리 구현을 위한 Controller class
 *
 * Modification Information
 *  
 * 수정일         수정자         수정내용
 * ----------     --------       ---------------------------
 * 2013.03.14     kimhahn		신규
 *    
 * @author kimhahn
 * @since 2014.03.14
 * @version 1.0
 */
package com.dmi.smartux.admin.gpack.event.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONSerializer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dmi.smartux.admin.gpack.contents.service.GpackContentsViewService;
import com.dmi.smartux.admin.gpack.contents.vo.GpackContentsAutoVO;
import com.dmi.smartux.admin.gpack.contents.vo.GpackImcsCategoryVO;
import com.dmi.smartux.admin.gpack.event.service.GpackPromotionViewService;
import com.dmi.smartux.admin.gpack.event.vo.GpackPlaylistVO;
import com.dmi.smartux.admin.gpack.event.vo.GpackProductVO;
import com.dmi.smartux.admin.gpack.event.vo.GpackPromotionVO;
import com.dmi.smartux.admin.gpack.event.vo.TVChannelVO;
import com.dmi.smartux.admin.gpack.pack.service.GPackPackViewService;
import com.dmi.smartux.admin.gpack.pack.vo.GPackPackVO;
import com.dmi.smartux.common.exception.ExceptionHandler;
import com.dmi.smartux.common.exception.SmartUXException;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.util.HTMLCleaner;

@Controller
public class GpackPromotionViewController {

	/** LOG */
	private final Log logger = LogFactory.getLog(this.getClass());
	
	/** 팩(템플릿) 서비스 */
	@Autowired
	GPackPackViewService packViewService;
	
	/** 프로모션 서비스 */
	@Autowired
	GpackPromotionViewService promotionViewService;
	
	/** 콘텐츠 서비스 */
	@Autowired
	GpackContentsViewService contentsViewService;
	
	/**
	 * 프로모션 - TV다시보기 목록 화면
	 * @param pack_id 				팩ID
	 * @param Model 				Model 객체
	 * @return 프로모션 목록 화면 URL
	 */
	@RequestMapping(value="/admin/gpack/event/getTvReplayPromotionList", method=RequestMethod.GET)
	public String getTvReplayPromotionList(
			@RequestParam(value="pack_id", required=false, defaultValue="P0001") String pack_id,
			Model model) throws Exception{
		
		pack_id = HTMLCleaner.clean(pack_id);
		
		// 팩 상세 조회
		GPackPackVO packVO = new GPackPackVO();
		packVO.setPack_id(pack_id);
		GPackPackVO gpackPackVO = packViewService.getGpackPackDetail(packVO);
		
		// 프로모션 목록 조회
		GpackPromotionVO promotionVO = new GpackPromotionVO();
		promotionVO.setPack_id(pack_id);
		List<GpackPromotionVO> result = promotionViewService.getGpackPromotionList(promotionVO);
		
		model.addAttribute("result", result);
		model.addAttribute("packVO", gpackPackVO);
		model.addAttribute("pack_id", pack_id);
		model.addAttribute("pack_default_title", SmartUXProperties.getProperty("gpack.pack.tv.replay"));
		
		return "admin/gpack/event/getPromotionList";
	}
	
	/**
	 * 프로모션 - 영화/애니 목록 화면
	 * @param pack_id 				팩ID
	 * @param Model 				Model 객체
	 * @return 프로모션 목록 화면 URL
	 */
	@RequestMapping(value="/admin/gpack/event/getMovieAniPromotionList", method=RequestMethod.GET)
	public String getMovieAniPromotionList(
			@RequestParam(value="pack_id", required=false, defaultValue="P0002") String pack_id,
			Model model) throws Exception{
		
		pack_id = HTMLCleaner.clean(pack_id);
		
		// 팩 상세 조회
		GPackPackVO packVO = new GPackPackVO();
		packVO.setPack_id(pack_id);
		GPackPackVO gpackPackVO = packViewService.getGpackPackDetail(packVO);
		
		// 프로모션 상세 조회
		GpackPromotionVO promotionVO = new GpackPromotionVO();
		promotionVO.setPack_id(pack_id);
		List<GpackPromotionVO> result = promotionViewService.getGpackPromotionList(promotionVO);
		
		model.addAttribute("result", result);
		model.addAttribute("packVO", gpackPackVO);
		model.addAttribute("pack_id", pack_id);
		model.addAttribute("pack_default_title", SmartUXProperties.getProperty("gpack.pack.movie.ani"));
		
		return "admin/gpack/event/getPromotionList";
	}
	
	/**
	 * 프로모션 - 키즈교육 목록 화면
	 * @param pack_id 				팩ID
	 * @param Model 				Model 객체
	 * @return 프로모션 목록 화면 URL
	 */
	@RequestMapping(value="/admin/gpack/event/getKidsEduPromotionList", method=RequestMethod.GET)
	public String getKidsEduPromotionList(
			@RequestParam(value="pack_id", required=false, defaultValue="P0003") String pack_id,
			Model model) throws Exception{
		
		pack_id = HTMLCleaner.clean(pack_id);
		
		// 팩 상세 조회
		GPackPackVO packVO = new GPackPackVO();
		packVO.setPack_id(pack_id);
		GPackPackVO gpackPackVO = packViewService.getGpackPackDetail(packVO);
		
		// 프로모션 상세 조회
		GpackPromotionVO promotionVO = new GpackPromotionVO();
		promotionVO.setPack_id(pack_id);
		List<GpackPromotionVO> result = promotionViewService.getGpackPromotionList(promotionVO);
		
		model.addAttribute("result", result);
		model.addAttribute("packVO", gpackPackVO);
		model.addAttribute("pack_id", pack_id);
		model.addAttribute("pack_default_title", SmartUXProperties.getProperty("gpack.pack.kids.edu"));
		
		return "admin/gpack/event/getPromotionList";
	}
	
	/**
	 * 프로모션 - 특별/이벤트 목록 화면
	 * @param pack_id 				팩ID
	 * @param Model 				Model 객체
	 * @return 프로모션 목록 화면 URL
	 */
	@RequestMapping(value="/admin/gpack/event/getSpecEventPromotionList", method=RequestMethod.GET)
	public String getSpecEventPromotionList(
			@RequestParam(value="pack_id", required=false, defaultValue="P0004") String pack_id,
			Model model) throws Exception{
		
		pack_id = HTMLCleaner.clean(pack_id);
		
		// 팩 상세 조회
		GPackPackVO packVO = new GPackPackVO();
		packVO.setPack_id(pack_id);
		GPackPackVO gpackPackVO = packViewService.getGpackPackDetail(packVO);
		
		// 프로모션 상세 조회
		GpackPromotionVO promotionVO = new GpackPromotionVO();
		promotionVO.setPack_id(pack_id);
		List<GpackPromotionVO> result = promotionViewService.getGpackPromotionList(promotionVO);
		
		model.addAttribute("result", result);
		model.addAttribute("packVO", gpackPackVO);
		model.addAttribute("pack_id", pack_id);
		model.addAttribute("pack_default_title", SmartUXProperties.getProperty("gpack.pack.spec.event"));
		
		return "admin/gpack/event/getPromotionList";
	}
	
	/**
	 * 프로모션 상세 화면
	 * @param pack_id 				팩ID
	 * @param category_id  			프로모션 ID
	 * @param Model 				Model 객체
	 * @return 프로모션 목록 화면 URL
	 */
	@RequestMapping(value="/admin/gpack/event/getGpackPromotionView", method=RequestMethod.GET)
	public String getGpackPromotionView(
			@RequestParam(value="pack_id") String pack_id,
			@RequestParam(value="category_id", required=false, defaultValue="") String category_id,
			Model model) throws Exception{
		
		pack_id = HTMLCleaner.clean(pack_id);
		category_id = HTMLCleaner.clean(category_id);
		String pack_default_title = "";
		
		// 팩 상세 조회
		GPackPackVO packVO = new GPackPackVO();
		packVO.setPack_id(pack_id);
		GPackPackVO gpackPackVO = packViewService.getGpackPackDetail(packVO);
		if("P0001".equals(pack_id)) {
			pack_default_title = SmartUXProperties.getProperty("gpack.pack.tv.replay");
		} else if("P0002".equals(pack_id)) {
			pack_default_title = SmartUXProperties.getProperty("gpack.pack.movie.ani");
		} else if("P0003".equals(pack_id)) {
			pack_default_title = SmartUXProperties.getProperty("gpack.pack.kids.edu");
		} else if("P0004".equals(pack_id)) {
			pack_default_title = SmartUXProperties.getProperty("gpack.pack.spec.event");
		}
		
		// 영상프로모션 개수 조회
		GpackPromotionVO promotionVO = new GpackPromotionVO();
		promotionVO.setPack_id(pack_id);
		int video_promotion_count = promotionViewService.countGprackVideoPromotion(promotionVO);
		
		// 프로모션 상세 조회
		GpackPromotionVO result = new GpackPromotionVO();
		if(!"".equals(category_id)) {
			promotionVO.setCategory_id(category_id);
			result = promotionViewService.getGpackPromotionDetail(promotionVO);
			model.addAttribute("result", result);
		}
		
		// 프로모션 영상 구분 설정
		boolean editVideoGb = false;
		if(video_promotion_count == 0 || "PV001".equalsIgnoreCase(result.getPromotion_video_gb()) || "PV002".equalsIgnoreCase(result.getPromotion_video_gb())) {
			editVideoGb = true;
		}
		
		model.addAttribute("packVO", gpackPackVO);
		model.addAttribute("pack_id", pack_id);
		model.addAttribute("result", result);
		model.addAttribute("editVideoGb", editVideoGb);
		model.addAttribute("pack_default_title", pack_default_title);
		
		return "admin/gpack/event/getPromotionView";
	}
	
	/**
	 * 프로모션 등록 처리 작업
	 * 
	 * @param request						HttpServletRequest 객체
	 * @param pack_id  						팩ID
	 * @param category_id  					프로모션 ID
	 * @param category_nm  					프로모션 명
	 * @param category_comment 				프로모션 멘트
	 * @param promotion_video_gb 			프로모션 영상 구분
	 * @param promotion_chnl				프로모션 채널
	 * @param auto_yn 						1 카테고리편성여부
	 * @param use_yn 						사용여부(Y/N)
	 * @param old_promotion_video_gb 		전 프로모션 영상 구분
	 * @param old_use_yn 					전 사용여부(Y/N)
	 * @param login_id 		 		유저ID
	 * @return 처리 결과를 기록한 json 문자열
	 */
	@RequestMapping(value="/admin/gpack/event/updatePromotion", method=RequestMethod.POST)
	public @ResponseBody String updateGpackPromotion(
			HttpServletRequest request, 
			@RequestParam(value="pack_id") String pack_id,
			@RequestParam(value="category_id") String category_id,
			@RequestParam(value="category_nm") String category_nm,
			@RequestParam(value="category_comment", required=false, defaultValue="") String category_comment,
			@RequestParam(value="promotion_video_gb", required=false, defaultValue="") String promotion_video_gb, 
			@RequestParam(value="promotion_chnl", required=false, defaultValue="") String promotion_chnl, 
			@RequestParam(value="auto_yn") String auto_yn, 
			@RequestParam(value="use_yn") String use_yn, 
			@RequestParam(value="old_promotion_video_gb") String old_promotion_video_gb, 
			@RequestParam(value="old_use_yn") String old_use_yn, 
			@RequestParam(value="smartUXManager") String login_id) {
		
		pack_id = HTMLCleaner.clean(pack_id);
		category_id = HTMLCleaner.clean(category_id);
		category_nm = HTMLCleaner.clean(category_nm);
		category_comment = HTMLCleaner.clean(category_comment);
		promotion_video_gb = HTMLCleaner.clean(promotion_video_gb);
		promotion_chnl = HTMLCleaner.clean(promotion_chnl);
		auto_yn = HTMLCleaner.clean(auto_yn);
		use_yn = HTMLCleaner.clean(use_yn);
		old_promotion_video_gb = HTMLCleaner.clean(old_promotion_video_gb);
		old_use_yn = HTMLCleaner.clean(old_use_yn);
		login_id = HTMLCleaner.clean(login_id);
		
		GpackPromotionVO promotionVO = new GpackPromotionVO();
		promotionVO.setPack_id(pack_id);
		promotionVO.setCategory_id(category_id);
		promotionVO.setCategory_nm(category_nm);
		promotionVO.setCategory_comment(category_comment);
		promotionVO.setPromotion_video_gb(promotion_video_gb);
		promotionVO.setPromotion_chnl(promotion_chnl);
		promotionVO.setAuto_yn(auto_yn);
		promotionVO.setUse_yn(use_yn);
		promotionVO.setCreate_id(login_id);
		promotionVO.setUpdate_id(login_id);
		
		String resultcode = "";
		String resultmessage = "";
		
		try{
			// input validation check
			validateUpdatePromotion(promotionVO, old_promotion_video_gb, old_use_yn);
			
			if("".equals(category_id)) {
				promotionViewService.insertGpackPromotion(promotionVO);
			} else {
				promotionViewService.updateGpackPromotion(promotionVO);
			}
			
			resultcode = SmartUXProperties.getProperty("flag.success");
			resultmessage = SmartUXProperties.getProperty("message.success");
		}catch(Exception e){
			logger.error("GpackPromotionViewController.updateGpackPromotion:" + e);
			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}
		
		return "{\"flag\" : \"" + resultcode + "\", \"message\" : \"" + resultmessage + "\"}";
	}
	
	/**
	 * 프로모션 채널 설정 화면
	 * @param srch_channel_name		검색어:채널명
	 * @param pageNum				페이지 번호
	 * @param pageSize				페이징시 게시물의 노출 개수
	 * @param blockSize				한 화면에 노출할 페이지 번호 개수
	 * @param Model 				Model 객체
	 * @return 팩 등록 화면 URL
	 */
	@RequestMapping(value="/admin/gpack/event/getGpackPromotionChannelView")
	public String getGpackPromotionChannelView(
			@RequestParam(value="srch_channel_name", required=false, defaultValue="") String srch_channel_name,
			@RequestParam(value="pageNum", required=false, defaultValue="1") String pageNum,
			@RequestParam(value="pageSize", required=false, defaultValue="10") String pageSize,
			@RequestParam(value="blockSize", required=false, defaultValue="10") String blockSize,
			@RequestParam(value="opener", required=false, defaultValue="") String opener,
			Model model) throws Exception{
		
		srch_channel_name = HTMLCleaner.clean(srch_channel_name);
		pageNum = HTMLCleaner.clean(pageNum);
		pageSize = HTMLCleaner.clean(pageSize);
		blockSize = HTMLCleaner.clean(blockSize);
		opener = HTMLCleaner.clean(opener);
		
		// TV채널목록 조회
		TVChannelVO tvChannelVO = new TVChannelVO();
		tvChannelVO.setSrch_channel_name(srch_channel_name);
		tvChannelVO.setPageNum(Integer.parseInt(pageNum));
		tvChannelVO.setPageSize(Integer.parseInt(pageSize));
		tvChannelVO.setBlockSize(Integer.parseInt(blockSize));
		
		List<TVChannelVO> tvChannelList = promotionViewService.getTVChannelList(tvChannelVO);
		int totalCount = promotionViewService.countTVChannelList(tvChannelVO);
		logger.debug("GpackPromotionViewController.getGpackPromotionChannelView:totalCount : " + totalCount);
		
		tvChannelVO.setList(tvChannelList);
		tvChannelVO.setPageCount(totalCount);
		
		model.addAttribute("srch_channel_name", srch_channel_name);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("blockSize", blockSize);
		model.addAttribute("opener", opener);
		model.addAttribute("tv_list", tvChannelVO);
		
		return "admin/gpack/event/getPromotionChannelList";
	}
	
	/**
	 * 프로모션 월정액 상품 설정 화면
	 * @param srch_product_name		검색어:상품명
	 * @param pageNum				페이지 번호
	 * @param pageSize				페이징시 게시물의 노출 개수
	 * @param blockSize				한 화면에 노출할 페이지 번호 개수
	 * @param Model 				Model 객체
	 * @return 팩 등록 화면 URL
	 */
	@RequestMapping(value="/admin/gpack/event/getGpackPromotionProductView")
	public String getGpackPromotionProductView(
			@RequestParam(value="srch_product_name", required=false, defaultValue="") String srch_product_name,
			@RequestParam(value="srch_product_name_encode", required=false, defaultValue="") String srch_product_name_encode,
			@RequestParam(value="pageNum", required=false, defaultValue="1") String pageNum,
			@RequestParam(value="pageSize", required=false, defaultValue="10") String pageSize,
			@RequestParam(value="blockSize", required=false, defaultValue="10") String blockSize,
			Model model) throws Exception{
		
		srch_product_name = HTMLCleaner.clean(srch_product_name);
		pageNum = HTMLCleaner.clean(pageNum);
		pageSize = HTMLCleaner.clean(pageSize);
		blockSize = HTMLCleaner.clean(blockSize);
		
		// TV채널목록 조회
		GpackProductVO productVO = new GpackProductVO();
		productVO.setSrch_product_name(srch_product_name);
		productVO.setPageNum(Integer.parseInt(pageNum));
		productVO.setPageSize(Integer.parseInt(pageSize));
		productVO.setBlockSize(Integer.parseInt(blockSize));
		
		List<GpackProductVO> productList = promotionViewService.getGpackProductList(productVO);
		int totalCount = promotionViewService.countGpackProductList(productVO);
		logger.debug("GpackPromotionViewController.getGpackPromotionProductView:totalCount : " + totalCount);
		
		productVO.setList(productList);
		productVO.setPageCount(totalCount);
		
		model.addAttribute("srch_product_name", srch_product_name);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("blockSize", blockSize);
		model.addAttribute("result", productVO);
		
		return "admin/gpack/event/getPromotionProductList";
	}
	
	/**
	 * 프로모션 순서바꾸기 화면
	 * @param pack_id				팩ID
	 * @param model 				Model 객체
	 * @return 프로모션 순서바꾸기 화면 URL
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/gpack/event/changeGpackPromotionOrder", method=RequestMethod.GET)
	public String getGpackPromotionOrderList(
			@RequestParam(value="pack_id") String pack_id,
			Model model) throws Exception {
		
		pack_id = HTMLCleaner.clean(pack_id);
		
		// 프로모션 순서바꾸기
		GpackPromotionVO promotionVO = new GpackPromotionVO();
		promotionVO.setPack_id(pack_id);
		List<GpackPromotionVO> result = promotionViewService.getGpackPromotionList(promotionVO);
			
		model.addAttribute("result", result);
		model.addAttribute("pack_id", pack_id);
		
		return "admin/gpack/event/changePromotionOrder";
	}
	
	/**
	 * 프로모션 순서바꾸기 작업
	 * @param pack_id			팩ID
	 * @param category_ids		프로모션 ID 값들 배열	
	 * @param update_id			관리자 사이트에 로그인 한 사람의 로그인 id
	 * @return 처리 결과를 기록한 json 문자열
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/gpack/event/changeGpackPromotionOrder", method=RequestMethod.POST)
	public @ResponseBody String changeGpackPromotionOrder(
			@RequestParam(value="pack_id") String pack_id,
			@RequestParam(value="category_ids[]") String [] category_ids,
			@RequestParam(value="smartUXManager") String update_id) throws Exception{	
		
		pack_id = HTMLCleaner.clean(pack_id);
		update_id = HTMLCleaner.clean(update_id);
		
		String resultcode = "";
		String resultmessage = "";
		try{
			
			int ordered = 1;
			List<GpackPromotionVO> promotionVOList = new ArrayList<GpackPromotionVO>();
			for(String category_id : category_ids) {
				GpackPromotionVO vo = new GpackPromotionVO();
				vo.setPack_id(pack_id);
				vo.setCategory_id(category_id);
				vo.setUpdate_id(update_id);
				vo.setOrdered(Integer.toString(ordered++));
				
				promotionVOList.add(vo);
			}
			promotionViewService.updateGpackPromotionOrderby(pack_id, update_id, promotionVOList);
			
			resultcode = SmartUXProperties.getProperty("flag.success");
			resultmessage = SmartUXProperties.getProperty("message.success");
		}catch(Exception e){
			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}
		return "{\"flag\" : \"" + resultcode + "\", \"message\" : \"" + resultmessage + "\"}";
	}
	
	/**
	 * 프로모션 삭제 
	 * @param pack_id			팩ID
	 * @param category_ids		삭제하고자 하는 프로모션ID 값들 배열
	 * @param login_id 		 	유저ID	
	 * @return					처리 결과를 기록한 json 문자열
	 */
	@RequestMapping(value="/admin/gpack/event/deleteGpackPromotion", method=RequestMethod.POST)
	public @ResponseBody String deleteGpackPromotion(
			@RequestParam(value="pack_id") String pack_id,
			@RequestParam(value="category_ids[]") String [] category_ids,
			@RequestParam(value="smartUXManager") String login_id) {
		
		pack_id = HTMLCleaner.clean(pack_id);
		login_id = HTMLCleaner.clean(login_id);
		
		String resultcode = "";
		String resultmessage = "";
		try{
			
			promotionViewService.deleteGpackPromotion(pack_id, category_ids, login_id);
			
			resultcode = SmartUXProperties.getProperty("flag.success");
			resultmessage = SmartUXProperties.getProperty("message.success");
		}catch(Exception e){
			logger.error("GpackPromotionController.deleteGpackPromotion:" + e);
			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}
		return "{\"flag\" : \"" + resultcode + "\", \"message\" : \"" + resultmessage + "\"}";
	}
	
	/**
	 * 플레이리스트 설정 화면
	 * @param pack_id 				팩ID
	 * @param category_id 			프로모션ID
	 * @param Model 				Model 객체
	 * @return 프로모션 상세/수정 화면 URL
	 */
	@RequestMapping(value="/admin/gpack/event/getGpackPromotionPlaylistView", method=RequestMethod.GET)
	public String getGpackPromotionPlaylistView(
			@RequestParam(value="pack_id") String pack_id,
			@RequestParam(value="category_id") String category_id,
			Model model) throws Exception{
		
		pack_id = HTMLCleaner.clean(pack_id);
		category_id = HTMLCleaner.clean(category_id);
		
		// 팩 상세 조회
		GPackPackVO packVO = new GPackPackVO();
		packVO.setPack_id(pack_id);
		GPackPackVO gpackPackVO = packViewService.getGpackPackDetail(packVO);
		
		// 프로모션 상세 조회
		GpackPromotionVO promotionVO = new GpackPromotionVO();
		promotionVO.setPack_id(pack_id);
		promotionVO.setCategory_id(category_id);
		GpackPromotionVO gpackPromotionVO = promotionViewService.getGpackPromotionDetail(promotionVO);
		
		// 플레이리스트 목록 조회
		GpackPlaylistVO playlistVO = new GpackPlaylistVO();
		playlistVO.setPack_id(pack_id);
		playlistVO.setCategory_id(category_id);
		List<GpackPlaylistVO> playlistVOList = promotionViewService.getGpackPlaylistList(playlistVO);
		
		// IMCS 카테고리 목록 조회
		List<GpackImcsCategoryVO> categoryResult = contentsViewService.getImcsCategoryList(gpackPackVO.getImcs_category_id(), "First", "playlist");
		
		model.addAttribute("packVO", gpackPackVO);
		model.addAttribute("promotionVO", gpackPromotionVO);
		model.addAttribute("playlistVOList", playlistVOList);
		model.addAttribute("categoryResult", categoryResult);
		model.addAttribute("pack_id", pack_id);
		model.addAttribute("category_id", category_id);
		
		return "admin/gpack/event/getPlaylistView";
	}
	
	/**
	 * 플레이리스트 상세 조회
	 * @param pack_id 				팩ID
	 * @param category_id 			프로모션ID
	 * @param playlist_id			플레이리스트ID
	 * @param model
	 * @return 플레이리스트 상세 정보 JSON 타입
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/gpack/event/getGpackPlaylistView", method=RequestMethod.POST )
	public ResponseEntity<String> getGpackPlaylistView(
			@RequestParam(value="pack_id")     String pack_id, 
			@RequestParam(value="category_id") String category_id, 
			@RequestParam(value="playlist_id") String playlist_id, 
			Model model) throws Exception{
		
		pack_id = HTMLCleaner.clean(pack_id);
		category_id = HTMLCleaner.clean(category_id);
		playlist_id = HTMLCleaner.clean(playlist_id);
		
		GpackPlaylistVO playlistVO = new GpackPlaylistVO();
		playlistVO.setPack_id(pack_id);
		playlistVO.setCategory_id(category_id);
		playlistVO.setPlaylist_id(playlist_id);
		
		GpackPlaylistVO result = promotionViewService.getGpackPlaylistDetail(playlistVO);
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(result).toString(), responseHeaders, HttpStatus.CREATED);
	}
	
	/**
	 * 플레이리스트 등록 처리 작업
	 * 
	 * @param pack_id  						팩ID
	 * @param category_id  					프로모션 ID
	 * @param playlist_id  					플레이리스트 ID
	 * @param playlist_nm 					플레이리스트 타이틀
	 * @param preview_imcs_category_id 		예고편 IMCS 카테고리 ID
	 * @param preview_imcs_album_id			예고편 IMCS 앨범 ID
	 * @param feature_imcs_category_id 		본편 IMCS 카테고리 ID
	 * @param feature_imcs_album_id			본편  IMCS 앨범 ID
	 * @param use_yn 						사용여부(Y/N)
	 * @param login_id 		 				유저ID
	 * @return 처리 결과를 기록한 json 문자열
	 */
	@RequestMapping(value="/admin/gpack/event/updateGpackPlaylist", method=RequestMethod.POST)
	public @ResponseBody String updateGpackPlaylist(
			HttpServletRequest request, 
			@RequestParam(value="pack_id") String pack_id,
			@RequestParam(value="category_id") String category_id,
			@RequestParam(value="playlist_id", required=false, defaultValue="") String playlist_id,
			@RequestParam(value="playlist_nm", required=false, defaultValue="") String playlist_nm,
			@RequestParam(value="preview_imcs_category_id", required=false, defaultValue="") String preview_imcs_category_id,
			@RequestParam(value="preview_imcs_album_id", required=false, defaultValue="") String preview_imcs_album_id, 
			@RequestParam(value="feature_imcs_category_id", required=false, defaultValue="") String feature_imcs_category_id, 
			@RequestParam(value="feature_imcs_album_id", required=false, defaultValue="") String feature_imcs_album_id, 
			@RequestParam(value="use_yn") String use_yn, 
			@RequestParam(value="smartUXManager") String login_id) {
		
		pack_id = HTMLCleaner.clean(pack_id);
		category_id = HTMLCleaner.clean(category_id);
		playlist_id = HTMLCleaner.clean(playlist_id);
		playlist_nm = HTMLCleaner.clean(playlist_nm);
		preview_imcs_category_id = HTMLCleaner.clean(preview_imcs_category_id);
		preview_imcs_album_id = HTMLCleaner.clean(preview_imcs_album_id);
		feature_imcs_category_id = HTMLCleaner.clean(feature_imcs_category_id);
		feature_imcs_album_id = HTMLCleaner.clean(feature_imcs_album_id);
		use_yn = HTMLCleaner.clean(use_yn);
		login_id = HTMLCleaner.clean(login_id);
		
		GpackPlaylistVO playlistVO = new GpackPlaylistVO();
		playlistVO.setPack_id(pack_id);
		playlistVO.setCategory_id(category_id);
		playlistVO.setPlaylist_id(playlist_id);
		playlistVO.setPlaylist_nm(playlist_nm);
		playlistVO.setPreview_imcs_category_id(preview_imcs_category_id);
		playlistVO.setPreview_imcs_album_id(preview_imcs_album_id);
		playlistVO.setFeature_imcs_category_id(feature_imcs_category_id);
		playlistVO.setFeature_imcs_album_id(feature_imcs_album_id);
		playlistVO.setUse_yn(use_yn);
		playlistVO.setCreate_id(login_id);
		playlistVO.setUpdate_id(login_id);
		
		String resultcode = "";
		String resultmessage = "";
		
		try{
			// input validation check
			validateUpdatePlaylist(playlistVO);
			
			if("".equals(playlist_id)) {
				promotionViewService.insertGpackPlaylist(playlistVO);
			} else {
				promotionViewService.updateGpackPlaylist(playlistVO);
			}
			
			resultcode = SmartUXProperties.getProperty("flag.success");
			resultmessage = SmartUXProperties.getProperty("message.success");
		}catch(Exception e){
			logger.error("GpackPromotionViewController.updateGpackPlaylist:" + e);
			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}
		
		return "{\"flag\" : \"" + resultcode + "\", \"message\" : \"" + resultmessage + "\"}";
	}
	
	/**
	 * 플레이리스트 삭제 
	 * @param pack_id			팩ID
	 * @param category_id  		프로모션 ID
	 * @param playlist_ids		삭제하고자 하는 플레이리스트ID 값들 배열
	 * @param login_id 		 	유저ID	
	 * @return					처리 결과를 기록한 json 문자열
	 */
	@RequestMapping(value="/admin/gpack/event/deleteGpackPlaylist", method=RequestMethod.POST)
	public @ResponseBody String deleteGpackPlaylist(
			@RequestParam(value="pack_id") String pack_id,
			@RequestParam(value="category_id") String category_id,
			@RequestParam(value="playlist_ids[]") String [] playlist_ids,
			@RequestParam(value="smartUXManager") String login_id) {
		
		pack_id = HTMLCleaner.clean(pack_id);
		login_id = HTMLCleaner.clean(login_id);
		
		String resultcode = "";
		String resultmessage = "";
		try{
			
			promotionViewService.deleteGpackPlaylist(pack_id, category_id, playlist_ids, login_id);
			
			resultcode = SmartUXProperties.getProperty("flag.success");
			resultmessage = SmartUXProperties.getProperty("message.success");
		}catch(Exception e){
			logger.error("GpackPromotionController.deleteGpackPlaylist:" + e);
			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}
		return "{\"flag\" : \"" + resultcode + "\", \"message\" : \"" + resultmessage + "\"}";
	}
	
	/**
	 * 플레이리스트 순서바꾸기 화면
	 * @param pack_id				팩ID
	 * @param category_id 			프로모션 ID
	 * @param model 				Model 객체
	 * @return 프로모션 순서바꾸기 화면 URL
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/gpack/event/changeGpackPlaylistOrder", method=RequestMethod.GET)
	public String getGpackPlaylistOrderList(
			@RequestParam(value="pack_id") String pack_id,
			@RequestParam(value="category_id") String category_id,
			Model model) throws Exception {
		
		pack_id = HTMLCleaner.clean(pack_id);
		category_id = HTMLCleaner.clean(category_id);
		
		// 프로모션 상세 조회
		GpackPromotionVO promotionVO = new GpackPromotionVO();
		promotionVO.setPack_id(pack_id);
		promotionVO.setCategory_id(category_id);
		GpackPromotionVO gpackPromotionVO = promotionViewService.getGpackPromotionDetail(promotionVO);
		
		// 프로모션 순서바꾸기
		GpackPlaylistVO playlistVO = new GpackPlaylistVO();
		playlistVO.setPack_id(pack_id);
		playlistVO.setCategory_id(category_id);
		List<GpackPlaylistVO> result = promotionViewService.getGpackPlaylistList(playlistVO);
			
		model.addAttribute("result", result);
		model.addAttribute("pack_id", pack_id);
		model.addAttribute("promotionVO", gpackPromotionVO);
		model.addAttribute("category_id", category_id);
		
		return "admin/gpack/event/changePlaylistOrder";
	}
	
	/**
	 * 플레이리스트 순서바꾸기 작업
	 * @param pack_id			팩ID
	 * @param category_id 		프로모션 ID
	 * @param playlist_ids		플레이리스트 ID 값들 배열	
	 * @param update_id			관리자 사이트에 로그인 한 사람의 로그인 id
	 * @return 처리 결과를 기록한 json 문자열
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/gpack/event/changeGpackPlaylistOrder", method=RequestMethod.POST)
	public @ResponseBody String changeGpackPlaylistOrder(
			@RequestParam(value="pack_id") String pack_id,
			@RequestParam(value="category_id") String category_id,
			@RequestParam(value="playlist_ids[]") String [] playlist_ids,
			@RequestParam(value="smartUXManager") String update_id) throws Exception{	
		
		pack_id = HTMLCleaner.clean(pack_id);
		category_id = HTMLCleaner.clean(category_id);
		update_id = HTMLCleaner.clean(update_id);
		
		String resultcode = "";
		String resultmessage = "";
		try{
			
			int ordered = 1;
			List<GpackPlaylistVO> playlistVOList = new ArrayList<GpackPlaylistVO>();
			for(String playlist_id : playlist_ids) {
				GpackPlaylistVO vo = new GpackPlaylistVO();
				vo.setPack_id(pack_id);
				vo.setCategory_id(category_id);
				vo.setPlaylist_id(playlist_id);
				vo.setUpdate_id(update_id);
				vo.setOrdered(Integer.toString(ordered++));
				
				playlistVOList.add(vo);
			}
			promotionViewService.updateGpackPlaylistOrderby(pack_id, update_id, playlistVOList);
			
			resultcode = SmartUXProperties.getProperty("flag.success");
			resultmessage = SmartUXProperties.getProperty("message.success");
		}catch(Exception e){
			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}
		return "{\"flag\" : \"" + resultcode + "\", \"message\" : \"" + resultmessage + "\"}";
	}
	
	/**
	 * 자동 편성(IMCS 카테고리 연동)  조회 화면
	 * @param pack_id			팩ID
	 * @param category_id 		프로모션 ID
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/gpack/event/getGpackOneCategoryView", method=RequestMethod.GET )
	public String getGpackOneCategoryView(
			@RequestParam(value="pack_id")     String pack_id, 
			@RequestParam(value="category_id") String category_id, 
			Model model) throws Exception{
		
		// 프로모션 상세 조회
		GpackPromotionVO promotionVO = new GpackPromotionVO();
		promotionVO.setPack_id(pack_id);
		promotionVO.setCategory_id(category_id);
		GpackPromotionVO gpackPromotionVO = promotionViewService.getGpackPromotionDetail(promotionVO);
		
		GpackContentsAutoVO autoVO = new GpackContentsAutoVO();
		autoVO.setPack_id(pack_id);
		autoVO.setCategory_id(category_id);

		autoVO = contentsViewService.getGpackOneCategory(autoVO);
		model.addAttribute("autoVO", autoVO);

		// 팩이 최상위 카테고리가 되었으니까 팩 정보를 조회해서 IMCS_CATEGORY_ID를 조회한 후 다시 조회
		GPackPackVO packVO = new GPackPackVO();
		packVO.setPack_id(pack_id);
		packVO = packViewService.getGpackPackDetail(packVO);
		List<GpackImcsCategoryVO> categoryResult = contentsViewService.getImcsCategoryList(packVO.getImcs_category_id(), "First","");
		model.addAttribute("categoryResult", categoryResult);
		
		model.addAttribute("pack_id", pack_id);
		model.addAttribute("category_id", category_id);
		model.addAttribute("promotionVO", gpackPromotionVO);
		
		return "/admin/gpack/event/getGpackOneCategoryView";
	}
	
	/**
	 * 자동 편성(IMCS 카테고리 연동) 등록
	 * @param pack_id				팩ID
	 * @param category_id 			프로모션 ID
	 * @param auto_set_id			1카테고리편성ID(자동 설정 ID)
	 * @param imcs_category_id		IMCS 카테고리 ID
	 * @param ordered				순번
	 * @param use_yn				사용여부
	 * @param login_id				사용자ID
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/gpack/event/updateGpackOneCategoryView", method=RequestMethod.POST)
	public @ResponseBody String updateGpackOneCategoryView(
			@RequestParam(value="pack_id") String pack_id, 
			@RequestParam(value="category_id") String category_id, 
			@RequestParam(value="auto_set_id", required=false) String auto_set_id,
			@RequestParam(value="imcs_category_id") String imcs_category_id,
			@RequestParam(value="ordered", required=false) String ordered,
			@RequestParam(value="use_yn") String use_yn,
			@RequestParam(value="smartUXManager") String login_id) throws Exception{
		
		String resultcode = "";
		String resultmessage = "";
		
		try{
			GpackContentsAutoVO contentsAutoVO = new GpackContentsAutoVO();
			
			contentsAutoVO.setPack_id(pack_id);
			contentsAutoVO.setCategory_id(category_id);
			contentsAutoVO.setAuto_set_id(auto_set_id);
			contentsAutoVO.setImcs_category_id(imcs_category_id);
			contentsAutoVO.setUse_yn(use_yn);
			contentsAutoVO.setCreate_id(login_id);
			contentsAutoVO.setUpdate_id(login_id);
			
			validateInsertGpackAuto(contentsAutoVO);

			contentsViewService.insertGpackContentsAuto(contentsAutoVO);

			resultcode = SmartUXProperties.getProperty("flag.success");
			resultmessage = SmartUXProperties.getProperty("message.success");
		}catch(Exception e){
			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}
		return "{\"flag\" : \"" + resultcode + "\", \"message\" : \"" + resultmessage + "\"}";
	}
	
	/**
	 * 미리보기 화면
	 * @param pack_id			팩ID
	 * @param template_type		템플릿타입
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/gpack/event/previewGpackPromotion", method=RequestMethod.GET )
	public String previewGpackPromotion(
			@RequestParam(value="pack_id") String pack_id, 
			@RequestParam(value="template_type") String template_type, 
			Model model) throws Exception{

		// 배경이미지 path
		String bg_img_url = GlobalCom.isNull(SmartUXProperties.getProperty("imgserver.weburl")) + GlobalCom.isNull(SmartUXProperties.getProperty("imgserver.imgpath"));
		
		// 프로모션 정보
		List<GpackPromotionVO> result =  promotionViewService.getGpackPromotionPreview(pack_id, template_type);
		
		model.addAttribute("pack_id", pack_id);
		model.addAttribute("template_type", template_type);
		model.addAttribute("result", result);
		model.addAttribute("bg_img_url", bg_img_url);
		
		return "admin/gpack/event/previewGpackPromotion";
	}
	
	/**
	 * 프로모션 등록에 대한 validation 작업 함수
	 * @param promotionVO				프로모션 정보	
	 * @param old_promotion_video_gb	전 프로모션 영상 구분
	 * @param old_use_yn				전 사용여부
	 * @throws SmartUXException
	 */
	private void validateUpdatePromotion(GpackPromotionVO promotionVO, String old_promotion_video_gb, String old_use_yn) throws Exception {
		SmartUXException exception = new SmartUXException();
		
		// 필수 체크
		// 타이틀
		if(!(StringUtils.hasText(promotionVO.getCategory_nm()))){
			exception.setFlag(SmartUXProperties.getProperty("NOT FOUND CATEGORY_NM"));
			exception.setMessage("타이틀은 필수로 들어가야 합니다");
			throw exception;
		}
		
		// BYTE 수 체크
		// 타이틀
		if(promotionVO.getCategory_nm().getBytes().length > 100){
			exception.setFlag("CATEGORY_NM LENGTH");
			exception.setMessage("타이틀은 100Byte 이내이어야 합니다");
		
			throw exception;
		}
		// 멘트
		if(promotionVO.getCategory_comment().getBytes().length > 100){
			exception.setFlag("CATEGORY_COMMENT LENGTH");
			exception.setMessage("타이틀은 300Byte 이내이어야 합니다");
		
			throw exception;
		}

		// 한 팩에 영상이 포함된 유효한(USE_YN = 'Y') 프로모션은 하나여야 한다
		if("Y".equals(promotionVO.getUse_yn())  && ("PV001".equals(promotionVO.getPromotion_video_gb()) || "PV002".equals(promotionVO.getPromotion_video_gb()))){
			
			// 영상프로모션 개수 조회
			int video_promotion_count = promotionViewService.countGprackVideoPromotion(promotionVO);
			
			if(video_promotion_count > 0) {
				
				if(!("Y".equals(old_use_yn) && ("PV001".equals(old_promotion_video_gb) || "PV002".equals(old_promotion_video_gb)))) {
					exception.setFlag(SmartUXProperties.getProperty("NOT FOUND PROMOTION_CHNL"));
					exception.setMessage("영상이 포함된 유효한 프로모션이 존재합니다");
					throw exception;
				}
			}
		}
		
		// 채널
		if("PV001".equals(promotionVO.getPromotion_video_gb())){
			if(!(StringUtils.hasText(promotionVO.getPromotion_chnl()))){
				exception.setFlag(SmartUXProperties.getProperty("NOT FOUND PROMOTION_CHNL"));
				exception.setMessage("등록하고자 하는 채널을 입력해주세요");
				throw exception;
			}
		}
	}
	
	/**
	 * 플레이리스트 등록에 대한 validation 작업 함수
	 * @param promotionVO			프로모션 정보	
	 * @throws SmartUXException
	 */
	private void validateUpdatePlaylist(GpackPlaylistVO playlistVO) throws Exception {
		SmartUXException exception = new SmartUXException();

		// 필수 체크
		// 타이틀
		if(!(StringUtils.hasText(playlistVO.getPlaylist_nm()))){
			exception.setFlag(SmartUXProperties.getProperty("NOT FOUND PLAYLIST_NM"));
			exception.setMessage("타이틀은 필수로 들어가야 합니다");
			throw exception;
		}
		if(!(StringUtils.hasText(playlistVO.getPreview_imcs_album_id()))){
			exception.setFlag(SmartUXProperties.getProperty("NOT FOUND PREVIEW_ALBUM"));
			exception.setMessage("예고편은 필수로 들어가야 합니다");
			throw exception;
		}
		if(!(StringUtils.hasText(playlistVO.getFeature_imcs_album_id()))){
			exception.setFlag(SmartUXProperties.getProperty("NOT FOUND FEATURE_ALBUM"));
			exception.setMessage("본편은 필수로 들어가야 합니다");
			throw exception;
		}
		
		// BYTE 수 체크
		// 타이틀
		if(playlistVO.getPlaylist_nm().getBytes().length > 100){
			exception.setFlag("PLAYLIST_NM LENGTH");
			exception.setMessage("타이틀은 100Byte 이내이어야 합니다");
		
			throw exception;
		}

		// 예고편 무료VOD 체크
		String price = promotionViewService.getGpackVodPrice(playlistVO.getPreview_imcs_album_id());
		if(Integer.parseInt(price) > 0){
			exception.setFlag("PREVIEW_IMCS_ALBUM_ID PRICE");
			exception.setMessage("예고편은 무료VOD를 선택해야 합니다");
		
			throw exception;
		}
		
		// 본편 무료VOD 체크
		price = promotionViewService.getGpackVodPrice(playlistVO.getFeature_imcs_album_id());
		if(Integer.parseInt(price) > 0){
			exception.setFlag("PREVIEW_IMCS_ALBUM_ID PRICE");
			exception.setMessage("본편은 무료VOD를 선택해야 합니다");
		
			throw exception;
		}
	}
	
	/**
	 * updateGpackOneCategoryView 함수에 대한 validation 작업 함수
	 * @param contentsAutoVO
	 * @throws SmartUXException
	 */
	private void validateInsertGpackAuto(GpackContentsAutoVO contentsAutoVO) throws SmartUXException {
		SmartUXException exception = new SmartUXException();
		
		if(!(StringUtils.hasText(contentsAutoVO.getPack_id()))){
			exception.setFlag("NOT FOUND PACK ID");
			exception.setMessage("PACK ID가 없습니다.");
			throw exception;
		}
		
		if(!(StringUtils.hasText(contentsAutoVO.getCategory_id()))){
			exception.setFlag("NOT FOUND CATEGORY ID");
			exception.setMessage("CATEGORY ID가 없습니다.");
			throw exception;
		}
		if(!(StringUtils.hasText(contentsAutoVO.getImcs_category_id()))){
			exception.setFlag("NOT FOUND IMCS IMCS_CATEGORY_ID");
			exception.setMessage("IMCS 카테고리 ID가 없습니다.");
			throw exception;
		}
	}
}
