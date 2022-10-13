/**
 * Class Name : GpackPackViewController.java
 * Description : 
 *  팩(템플릿) 관리 구현을 위한 Controller class
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
package com.dmi.smartux.admin.gpack.pack.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.math.NumberUtils;
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

import com.dmi.commons.consts.ResultCode;
import com.dmi.commons.exception.MimsCommonException;
import com.dmi.commons.factory.FileProcessFunction;
import com.dmi.commons.factory.FileProcessLockFactory;
import com.dmi.commons.logger.CLog;
import com.dmi.smartux.admin.gpack.category.service.GpackCategoryViewService;
import com.dmi.smartux.admin.gpack.category.vo.GpackCategoryVO;
import com.dmi.smartux.admin.gpack.event.service.GpackPromotionViewService;
import com.dmi.smartux.admin.gpack.event.vo.GpackPlaylistVO;
import com.dmi.smartux.admin.gpack.event.vo.GpackPromotionVO;
import com.dmi.smartux.admin.gpack.pack.service.GPackPackViewService;
import com.dmi.smartux.admin.gpack.pack.vo.GPackPackVO;
import com.dmi.smartux.admin.mainpanel.service.PanelViewService;
import com.dmi.smartux.common.exception.ExceptionHandler;
import com.dmi.smartux.common.exception.SmartUXException;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.service.FileCacheService;
import com.dmi.smartux.common.util.CookieUtil;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.util.HTMLCleaner;
import com.dmi.smartux.common.vo.CUDResult;

import net.sf.json.JSONSerializer;

@Controller
public class GPackPackViewController {

	/** LOG */
	private final Log logger = LogFactory.getLog(this.getClass());
	
	private final Log applyLogger = LogFactory.getLog("adminApplyCacheHistroy");
	
	@Autowired
	PanelViewService service;
	
	/** 팩(템플릿) 서비스 */
	@Autowired
	GPackPackViewService packViewService;
	
	/** 프로모션 서비스 */
	@Autowired
	GpackPromotionViewService promotionViewService;
	
	/** 카테고리 서비스 */
	@Autowired
	GpackCategoryViewService categoryViewService;
	
	@Autowired
	FileCacheService fileCache;
	
	/**
	 * 팩(템플릿) - TV다시보기 상세/수정 화면
	 * @param pack_id 				팩ID
	 * @param Model 				Model 객체
	 * @return 팩(템플릿) 상세/수정 화면 URL
	 */
	@RequestMapping(value="/admin/gpack/pack/getTvReplayPackView", method=RequestMethod.GET)
	public String getTvReplayPackView(
			@RequestParam(value="pack_id", required=false, defaultValue="P0001") String pack_id,
			Model model) throws Exception{
		
		pack_id = HTMLCleaner.clean(pack_id);
		GPackPackVO packVO = new GPackPackVO();
		packVO.setPack_id(pack_id);
		
		// 팩 상세 조회
		GPackPackVO result = packViewService.getGpackPackDetail(packVO);
			
		model.addAttribute("result", result);
		model.addAttribute("pack_id", pack_id);
		model.addAttribute("pack_default_title", SmartUXProperties.getProperty("gpack.pack.tv.replay"));
		
		return "admin/gpack/pack/getPackView";
	}
	
	/**
	 * 팩(템플릿) - 영화/애니 상세/수정 화면
	 * @param pack_id 				팩ID
	 * @param Model 				Model 객체
	 * @return 팩(템플릿) 상세/수정 화면 URL
	 */
	@RequestMapping(value="/admin/gpack/pack/getMovieAniPackView", method=RequestMethod.GET)
	public String getMovieAniPackView(
			@RequestParam(value="pack_id", required=false, defaultValue="P0002") String pack_id,
			Model model) throws Exception{
		
		pack_id = HTMLCleaner.clean(pack_id);
		GPackPackVO packVO = new GPackPackVO();
		packVO.setPack_id(pack_id);
		
		// 팩 상세 조회
		GPackPackVO result = packViewService.getGpackPackDetail(packVO);
			
		model.addAttribute("result", result);
		model.addAttribute("pack_id", pack_id);
		model.addAttribute("pack_default_title", SmartUXProperties.getProperty("gpack.pack.movie.ani"));
		
		return "admin/gpack/pack/getPackView";
	}
	
	/**
	 * 팩(템플릿) - 키즈교육 상세/수정 화면
	 * @param pack_id 				팩ID
	 * @param Model 				Model 객체
	 * @return 팩(템플릿) 상세/수정 화면 URL
	 */
	@RequestMapping(value="/admin/gpack/pack/getKidsEduPackView", method=RequestMethod.GET)
	public String getKidsEduPackView(
			@RequestParam(value="pack_id", required=false, defaultValue="P0003") String pack_id,
			Model model) throws Exception{
		
		pack_id = HTMLCleaner.clean(pack_id);
		GPackPackVO packVO = new GPackPackVO();
		packVO.setPack_id(pack_id);
		
		// 팩 상세 조회
		GPackPackVO result = packViewService.getGpackPackDetail(packVO);
			
		model.addAttribute("result", result);
		model.addAttribute("pack_id", pack_id);
		model.addAttribute("pack_default_title", SmartUXProperties.getProperty("gpack.pack.kids.edu"));
		
		return "admin/gpack/pack/getPackView";
	}
	
	/**
	 * 팩(템플릿) - 특별/이벤트 상세/수정 화면
	 * @param pack_id 				팩ID
	 * @param Model 				Model 객체
	 * @return 팩(템플릿) 상세/수정 화면 URL
	 */
	@RequestMapping(value="/admin/gpack/pack/getSpecEventPackView", method=RequestMethod.GET)
	public String getSpecEventPackView(
			@RequestParam(value="pack_id", required=false, defaultValue="P0004") String pack_id,
			Model model) throws Exception{
		
		pack_id = HTMLCleaner.clean(pack_id);
		GPackPackVO packVO = new GPackPackVO();
		packVO.setPack_id(pack_id);
		
		// 팩 상세 조회
		GPackPackVO result = packViewService.getGpackPackDetail(packVO);
		
		model.addAttribute("result", result);
		model.addAttribute("pack_id", pack_id);
		model.addAttribute("pack_default_title", SmartUXProperties.getProperty("gpack.pack.spec.event"));
		
		return "admin/gpack/pack/getPackView";
	}
	
	/**
	 * Pack 등록 처리 작업
	 * 
	 * @param request				HttpServletRequest 객체
	 * @param pack_id  				팩ID
	 * @param pack_nm  				팩명
	 * @param template_type 		템플릿타입
	 * @param login_id 		 		유저ID
	 * @return 처리 결과를 기록한 json 문자열
	 */
	@RequestMapping(value="/admin/gpack/pack/updatePack", method=RequestMethod.POST)
	public @ResponseBody String updateGpackPack(
			HttpServletRequest request, 
			@RequestParam(value="pack_id") String pack_id,
			@RequestParam(value="pack_nm", required=false, defaultValue="") String pack_nm,
			@RequestParam(value="template_type") String template_type, 
			@RequestParam(value="smartUXManager") String login_id) {
		
		pack_id = HTMLCleaner.clean(pack_id);
		pack_nm = HTMLCleaner.clean(pack_nm);
		template_type = HTMLCleaner.clean(template_type);
		login_id = HTMLCleaner.clean(login_id);
		
		GPackPackVO packVO = new GPackPackVO();
		packVO.setPack_id(pack_id);
		packVO.setPack_nm(pack_nm);
		packVO.setTemplate_type(template_type);
		packVO.setUpdate_id(login_id);
		
		String resultcode = "";
		String resultmessage = "";
		
		try{
			// input validation check
			validateUpdatePack(packVO);
			
			packViewService.updateGpackPack(packVO);
			
			resultcode = SmartUXProperties.getProperty("flag.success");
			resultmessage = SmartUXProperties.getProperty("message.success");
		}catch(Exception e){
			//logger.error("GPackPackViewController.insertPack:" + e);
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
	@RequestMapping(value="/admin/gpack/pack/previewGpackPack", method=RequestMethod.GET )
	public String previewGpackPack(
			@RequestParam(value="pack_id") String pack_id, 
			@RequestParam(value="template_type") String template_type, 
			Model model) throws Exception{

		pack_id = HTMLCleaner.clean(pack_id);
		template_type = HTMLCleaner.clean(template_type);
		
		String packPath = "";
		if("P0001".equals(pack_id)) {
			packPath = SmartUXProperties.getProperty("gpack.imgupload.tv.dir");
		} else if("P0002".equals(pack_id)) {
			packPath = SmartUXProperties.getProperty("gpack.imgupload.movie.dir");
		} else if("P0003".equals(pack_id)) {
			packPath = SmartUXProperties.getProperty("gpack.imgupload.kids.dir");
		} else {
			packPath = SmartUXProperties.getProperty("gpack.imgupload.spec.dir");
		}
		
		// 배경이미지 path
		// local
		//String bg_img_url = GlobalCom.isNull(SmartUXProperties.getProperty("imgserver.weburl")) + GlobalCom.isNull(SmartUXProperties.getProperty("imgserver.imgpath")) + "/" +packPath;
		// 상용
		String bg_img_url = service.getImageServerURL("I") + packPath;
		
		//이미지 리사이즈 (porperty에 선언되어있음, 나중에 이미지 사이즈 고정되면 수정)
		String img_poster_width = SmartUXProperties.getProperty("gpack.poster.img.width");
		String img_poster_height = SmartUXProperties.getProperty("gpack.poster.img.height");
		
		// 팩 상세 조회
		GPackPackVO packVO = new GPackPackVO();
		packVO.setPack_id(pack_id);
		GPackPackVO pack = packViewService.getGpackPackDetail(packVO);
		
		// 프로모션 정보
		List<GpackPromotionVO> promotionList =  promotionViewService.getGpackPromotionPreview(pack_id, template_type);
		
		// 카테고리 정보
		GpackCategoryVO categoryVO = new GpackCategoryVO();
		categoryVO.setPack_id(pack_id);
		categoryVO.setUse_yn("Y");
		List<GpackCategoryVO> categorylist = categoryViewService.previewGpackCategory(categoryVO, "");
		
		model.addAttribute("pack_id", pack_id);
		model.addAttribute("template_type", template_type);
		model.addAttribute("pack", pack);
		model.addAttribute("promotionList", promotionList);
		model.addAttribute("categorylist", categorylist);
		model.addAttribute("bg_img_url", bg_img_url);
		model.addAttribute("img_poster_width", img_poster_width);
		model.addAttribute("img_poster_height", img_poster_height);
		
		return "admin/gpack/pack/previewGpackPack";
	}
	
	/**
	 * 미리보기 화면(상용)
	 * @param pack_id			팩ID
	 * @param template_type		템플릿타입
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/gpack/pack/previewGpackPackBiz", method=RequestMethod.GET )
	public String previewGpackPackBiz(
			@RequestParam(value="pack_id") String pack_id, 
			@RequestParam(value="template_type") String template_type, 
			Model model) throws Exception{

		pack_id = HTMLCleaner.clean(pack_id);
		template_type = HTMLCleaner.clean(template_type);
		
		String packPath = "";
		if("P0001".equals(pack_id)) {
			packPath = SmartUXProperties.getProperty("gpack.imgupload.tv.dir");
		} else if("P0002".equals(pack_id)) {
			packPath = SmartUXProperties.getProperty("gpack.imgupload.movie.dir");
		} else if("P0003".equals(pack_id)) {
			packPath = SmartUXProperties.getProperty("gpack.imgupload.kids.dir");
		} else {
			packPath = SmartUXProperties.getProperty("gpack.imgupload.spec.dir");
		}
		
		// 배경이미지 path
		// local
		//String bg_img_url = GlobalCom.isNull(SmartUXProperties.getProperty("imgserver.weburl")) + GlobalCom.isNull(SmartUXProperties.getProperty("imgserver.imgpath")) + "/" +packPath;
		// 상용
		String bg_img_url = service.getImageServerURL("I") + packPath;
		
		//이미지 리사이즈 (porperty에 선언되어있음, 나중에 이미지 사이즈 고정되면 수정)
		String img_poster_width = SmartUXProperties.getProperty("gpack.poster.img.width");
		String img_poster_height = SmartUXProperties.getProperty("gpack.poster.img.height");
		
		// 팩 상세 조회(상용)
		GPackPackVO packVO = new GPackPackVO();
		packVO.setPack_id(pack_id);
		GPackPackVO pack = packViewService.getGpackPackDetailBiz(packVO);
		
		// 프로모션 정보(상용)
		List<GpackPromotionVO> promotionList =  promotionViewService.getGpackPromotionPreviewBiz(pack_id, template_type);
		
		// 카테고리 정보(상용)
		GpackCategoryVO categoryVO = new GpackCategoryVO();
		categoryVO.setPack_id(pack_id);
		categoryVO.setUse_yn("Y");
		List<GpackCategoryVO> categorylist = categoryViewService.previewGpackCategory(categoryVO, "BIZ");
		
		model.addAttribute("pack_id", pack_id);
		model.addAttribute("template_type", template_type);
		model.addAttribute("pack", pack);
		model.addAttribute("promotionList", promotionList);
		model.addAttribute("categorylist", categorylist);
		model.addAttribute("bg_img_url", bg_img_url);
		model.addAttribute("preview_gb", "BIZ");
		model.addAttribute("img_poster_width", img_poster_width);
		model.addAttribute("img_poster_height", img_poster_height);
		
		return "admin/gpack/pack/previewGpackPack";
	}
	
	/**
	 * 상용 테이블에 반영 -> 캐쉬 동기화 
	 * @param pack_id 				팩ID
	 * @param login_id 		 		유저ID
	 * @param Model 				Model 객체
	 * @return 처리 결과를 기록한 json 문자열
	 * @throws Exception 
	 */
	@RequestMapping(value="/admin/gpack/pack/applyGpackBiz")
	public ResponseEntity<String> applyGpackBiz(
			HttpServletRequest request, 
			@RequestParam(value="pack_id") final String pack_id,
			@RequestParam(value="smartUXManager") final String login_id,
			@RequestParam(value="template_type") final String template_type ) throws Exception {
		
		final CLog cLog = new CLog(applyLogger, request.getRemoteAddr(), request.getRequestURI(), request.getMethod());
		final String loginUser = CookieUtil.getCookieUserID(request);
		final CUDResult result = new CUDResult();
		
		result.setFlag("0000");
		result.setMessage("미진행");
		
		try {
			cLog.startLog("템플릿 즉시적용 :", pack_id, template_type, loginUser);
			
			String fileLockPath = SmartUXProperties.getProperty("SmartUXNotiDao.refreshNoti.fileLock");
			int waitTime = NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.wait"), 5);
			
			FileProcessLockFactory.start(fileLockPath, waitTime,  new FileProcessFunction("템플릿 즉시적용", ResultCode.ApplyRequestFail) {
				
				@Override
				public void run() throws Exception {
					
					HTMLCleaner.clean(pack_id);
					HTMLCleaner.clean(login_id);
					HTMLCleaner.clean(template_type); 
					
					String chkCategory  = packViewService.chkAvailableCategory(pack_id);
					String chkPromotion = packViewService.chkAvailablePromotion(pack_id);
					
					String chkPromotionByTemplate2 = "Y";
					
					if ( "TP002".equals ( template_type ) ) {
						chkPromotionByTemplate2 = packViewService.isPromotionByTemplate2 ( pack_id,template_type);
						
						GpackPromotionVO promotionVo = packViewService.getPromotionInfoBy (pack_id);
						
						if ( StringUtils.hasText ( promotionVo.getCategory_id ( ) ) ) {
							
							if ( "PV002".equals ( promotionVo.getPromotion_video_gb ( ) )) {
								// 플레이리스트 목록 정보의 컨텐츠 유무
								GpackPlaylistVO playlistVO = new GpackPlaylistVO();
								playlistVO.setPack_id(pack_id);
								playlistVO.setCategory_id(promotionVo.getCategory_id ( ));
								List<GpackPlaylistVO> playlistVOList = promotionViewService.getGpackPlaylistList(playlistVO);
								
								if ( playlistVOList.size ( ) < 1 ) {
									chkPromotionByTemplate2 = "N";
								}
								
								int playListUseYNCnt=0;
								for ( GpackPlaylistVO gpackPlaylistVO : playlistVOList ) {
									if ( "N".equals ( gpackPlaylistVO.getUse_yn ( ) )) {
										playListUseYNCnt++;
									}
								}
								
								if ( playlistVOList.size ( ) == playListUseYNCnt ) {
									chkPromotionByTemplate2 = "N";
								}
							}
							
							// 프로모션 상세 정보의 컨텐츠 유무
							GpackPromotionVO promotionVO = new GpackPromotionVO();
							promotionVO.setPack_id(pack_id);
							promotionVO.setCategory_id ( promotionVo.getCategory_id ( ) );
							String c_use_yn = promotionViewService.getPromotionByUseYn(promotionVO);
							
							if ( "N".equals ( c_use_yn ) ) {
								chkPromotionByTemplate2 = "N";
							}
						}
					} else {
						chkCategory = "Y";
						chkPromotion = "Y";
						chkPromotionByTemplate2 = "Y";
					}
					
					validateApplyGpackBiz(pack_id, chkCategory, chkPromotion, chkPromotionByTemplate2);
					
					packViewService.insertGpackBiz(pack_id, login_id);
					// CacheJob(pack_id);
					
					// FileCache를 이용한 CacheJob 호출
					fileCache.fileCacheJob(pack_id);
					
					
					result.setFlag(SmartUXProperties.getProperty("flag.success"));
					result.setMessage(SmartUXProperties.getProperty("message.success"));
					cLog.middleLog("템플릿 즉시적용", "API 호출", pack_id, template_type, loginUser, result.getFlag(), result.getMessage());
				}
			});
		} catch (MimsCommonException e) {
			cLog.warnLog("템플릿 즉시적용", "실패", pack_id, template_type, loginUser, e.getFlag(), e.getMessage(), e.getLocalizedMessage());
			result.setFlag(e.getFlag());
			result.setMessage(e.getMessage());
		} finally {
			cLog.endLog("템플릿 즉시적용", pack_id, template_type, loginUser, result.getFlag(), result.getMessage());
		}
		
		String resultMsg = "{\"flag\" : \"" + result.getFlag() + "\", \"message\" : \"" + result.getMessage() + "\"}";
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(resultMsg).toString(), responseHeaders, HttpStatus.OK);
		
	}
	
/*	@RequestMapping(value="/admin/gpack/pack/applyGpackBiz")
	public @ResponseBody String applyGpackBiz(
			HttpServletRequest request, 
			@RequestParam(value="pack_id") String pack_id,
			@RequestParam(value="smartUXManager") String login_id,
			@RequestParam(value="template_type") String template_type ) {
		
		pack_id = HTMLCleaner.clean(pack_id);
		login_id = HTMLCleaner.clean(login_id);
		template_type = HTMLCleaner.clean(template_type);
		
		String resultcode = "";
		String resultmessage = "";
		
		try{
			
			String chkCategory  = packViewService.chkAvailableCategory(pack_id);
			String chkPromotion = packViewService.chkAvailablePromotion(pack_id);
			
			String chkPromotionByTemplate2 = "Y";
			
			if ( "TP002".equals ( template_type ) ) {
				chkPromotionByTemplate2 = packViewService.isPromotionByTemplate2 ( pack_id,template_type);
				
				GpackPromotionVO promotionVo = packViewService.getPromotionInfoBy (pack_id);
				
				if ( StringUtils.hasText ( promotionVo.getCategory_id ( ) ) ) {
					
					if ( "PV002".equals ( promotionVo.getPromotion_video_gb ( ) )) {
						// 플레이리스트 목록 정보의 컨텐츠 유무
						GpackPlaylistVO playlistVO = new GpackPlaylistVO();
						playlistVO.setPack_id(pack_id);
						playlistVO.setCategory_id(promotionVo.getCategory_id ( ));
						List<GpackPlaylistVO> playlistVOList = promotionViewService.getGpackPlaylistList(playlistVO);
						
						if ( playlistVOList.size ( ) < 1 ) {
							chkPromotionByTemplate2 = "N";
						}
						
						int playListUseYNCnt=0;
						for ( GpackPlaylistVO gpackPlaylistVO : playlistVOList ) {
							if ( "N".equals ( gpackPlaylistVO.getUse_yn ( ) )) {
								playListUseYNCnt++;
							}
						}
						
						if ( playlistVOList.size ( ) == playListUseYNCnt ) {
							chkPromotionByTemplate2 = "N";
						}
					}
					
					// 프로모션 상세 정보의 컨텐츠 유무
					GpackPromotionVO promotionVO = new GpackPromotionVO();
					promotionVO.setPack_id(pack_id);
					promotionVO.setCategory_id ( promotionVo.getCategory_id ( ) );
					String c_use_yn = promotionViewService.getPromotionByUseYn(promotionVO);
					
					if ( "N".equals ( c_use_yn ) ) {
						chkPromotionByTemplate2 = "N";
					}
				}
			} else {
				chkCategory = "Y";
				chkPromotion = "Y";
				chkPromotionByTemplate2 = "Y";
			}
			
			validateApplyGpackBiz(pack_id, chkCategory, chkPromotion, chkPromotionByTemplate2);
			
			packViewService.insertGpackBiz(pack_id, login_id);
			// CacheJob(pack_id);
			
			// FileCache를 이용한 CacheJob 호출
			fileCache.fileCacheJob(pack_id);
			
			resultcode = SmartUXProperties.getProperty("flag.success");
			resultmessage = SmartUXProperties.getProperty("message.success");
			
		}catch(Exception e){
			//logger.error("GPackPackViewController.applyGpackBiz:" + e);
			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}
		
		return "{\"flag\" : \"" + resultcode + "\", \"message\" : \"" + resultmessage + "\"}";
	}
*/	
	/**
	 * 캐쉬 동기화 
	 * @param pack_id 				팩ID
	 * @param Model 				Model 객체
	 * @return 처리 결과를 기록한 json 문자열
	 */
	@RequestMapping(value="/admin/gpack/pack/applyCache")
	public @ResponseBody String applyCache(
			HttpServletRequest request, 
			@RequestParam(value="pack_id") String pack_id) {
		
		pack_id = HTMLCleaner.clean(pack_id);
		
		String resultcode = "";
		String resultmessage = "";
		
		try{
			validateApplyGpackBiz(pack_id, "Y", "Y", "Y");

			CacheJob(pack_id);
			
			resultcode = SmartUXProperties.getProperty("flag.success");
			resultmessage = SmartUXProperties.getProperty("message.success");
		}catch(Exception e){
			//logger.error("GPackPackViewController.applyCache:" + e);
			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}
		
		return "{\"flag\" : \"" + resultcode + "\", \"message\" : \"" + resultmessage + "\"}";
	}

	/**
	 * applyGpackBiz 함수의 validation 작업을 하는 함수
	 * @param pack_id		
	 */
	private void validateApplyGpackBiz(String pack_id, String chkCategory, String chkPromotion, String chkPromotionByTemplate2){
		
		SmartUXException exception = new SmartUXException();
		
		if(!(StringUtils.hasText(pack_id))){
			exception.setFlag("NOT FOUND PACK_ID");
			exception.setMessage("PACK ID값은 필수로 들어가야 합니다");
			throw exception;
		}
		
		if(!chkCategory.equals("Y")){
			exception.setFlag("NOT FOUND CATEGORY");
			exception.setMessage("유효한 카테고리가 존재해야 합니다");
			throw exception;
		}
		
		if(!chkPromotion.equals("Y")){
			exception.setFlag("NOT FOUND PROMOTION");
			exception.setMessage("유효한 프로모션이 존재해야 합니다");
			throw exception;
		}
		
		if(!chkPromotionByTemplate2.equals("Y")){
			exception.setFlag("NOT FOUND PROMOTION");
			exception.setMessage("채널 또는 플레이리스트를 영역을 재확인 바랍니다.");
			throw exception;
		}

	}
	
	/**
	 * 캐시 내용을 최신 내용으로 갱신하며 연관된 다른 WAS와 캐시를 동기화 하는 작업까지 진행한다
	 * @throws Exception
	 */
	private void CacheJob(String pack_id) throws Exception{
		
		//String host = SmartUXProperties.getProperty("scheduler.host"); 					// 자기 자신것을 호출할때 사용하는 host(스케듈러에 정의된 것을 사용)
		//int port = Integer.parseInt(SmartUXProperties.getProperty("scheduler.port"));	// 자기 자신것을 호출할때 사용하는 port(스케듈러에 정의된 것을 사용)
		String param = "callByScheduler=A&pack_id=" + pack_id;		// 관리자툴에서 호출한다는 의미로 셋팅해준다(이 값이 A여야 DB에서 바로 읽어서 캐쉬에 반영한다)
		int timeout = Integer.parseInt(SmartUXProperties.getProperty("scheduler.timeout"));		// timeout 값은 스케쥴러것을 사용
		int retrycnt = Integer.parseInt(SmartUXProperties.getProperty("scheduler.retrycnt"));	// 재시도 횟수는 스케줄러 것을 사용
		String protocolName = "";
		String url = "";
		
		// 팩 버전 정보 조회 단말 API URL을 가져온다
		protocolName = SmartUXProperties.getProperty("GpackPackVersionDao.getPackVersion.protocol");
		url = SmartUXProperties.getProperty("GpackPackVersionDao.getPackVersion.CacheScheduleURL");
		GlobalCom.syncServerCache(url, param, timeout, retrycnt, protocolName);		// 다른 서버의 캐쉬 동기화 작업 진행
		
		// 팩 기본정보 조회 단말 API URL을 가져온다
		protocolName = SmartUXProperties.getProperty("GpackCategoryInfoDao.getPackInfo.protocol");
		if(pack_id.equals("P0001")){
			url = SmartUXProperties.getProperty("GpackCategoryInfoDao.getPackInfo1.CacheScheduleURL");
		}else if(pack_id.equals("P0002")){
			url = SmartUXProperties.getProperty("GpackCategoryInfoDao.getPackInfo2.CacheScheduleURL");
		}else if(pack_id.equals("P0003")){
			url = SmartUXProperties.getProperty("GpackCategoryInfoDao.getPackInfo3.CacheScheduleURL");
		}else if(pack_id.equals("P0004")){
			url = SmartUXProperties.getProperty("GpackCategoryInfoDao.getPackInfo4.CacheScheduleURL");
		}
		GlobalCom.syncServerCache(url, "callByScheduler=A", timeout, retrycnt, protocolName);		// 다른 서버의 캐쉬 동기화 작업 진행
		
		// 카테고리 조회 단말 API URL을 가져온다
		protocolName = SmartUXProperties.getProperty("GpackCategoryInfoDao.getPackCategory.protocol");
		if(pack_id.equals("P0001")){
			url = SmartUXProperties.getProperty("GpackCategoryInfoDao.getPackCategory1.CacheScheduleURL");
		}else if(pack_id.equals("P0002")){
			url = SmartUXProperties.getProperty("GpackCategoryInfoDao.getPackCategory2.CacheScheduleURL");
		}else if(pack_id.equals("P0003")){
			url = SmartUXProperties.getProperty("GpackCategoryInfoDao.getPackCategory3.CacheScheduleURL");
		}else if(pack_id.equals("P0004")){
			url = SmartUXProperties.getProperty("GpackCategoryInfoDao.getPackCategory4.CacheScheduleURL");
		}			
		GlobalCom.syncServerCache(url, "callByScheduler=A", timeout, retrycnt, protocolName);		// 다른 서버의 캐쉬 동기화 작업 진행
		
		// 프로모션 조회 단말 API URL을 가져온다
		protocolName = SmartUXProperties.getProperty("GpackPackPromotionDao.getPackPromotion.protocol");
		if(pack_id.equals("P0001")){
			url = SmartUXProperties.getProperty("GpackPackPromotionDao.getPackPromotion1.CacheScheduleURL");
		}else if(pack_id.equals("P0002")){
			url = SmartUXProperties.getProperty("GpackPackPromotionDao.getPackPromotion2.CacheScheduleURL");
		}else if(pack_id.equals("P0003")){
			url = SmartUXProperties.getProperty("GpackPackPromotionDao.getPackPromotion3.CacheScheduleURL");
		}else if(pack_id.equals("P0004")){
			url = SmartUXProperties.getProperty("GpackPackPromotionDao.getPackPromotion4.CacheScheduleURL");
		}
		GlobalCom.syncServerCache(url, "callByScheduler=A", timeout, retrycnt, protocolName);		// 다른 서버의 캐쉬 동기화 작업 진행
	}
	
	/**
	 * 팩(템플릿) 수정에 대한 validation 작업 함수
	 * @param packVO				팩(템플릿) 정보	
	 * @throws SmartUXException
	 */
	private void validateUpdatePack(GPackPackVO packVO) throws SmartUXException{
		SmartUXException exception = new SmartUXException();
		
		// 필수 체크
		// 타이틀
		if(!(StringUtils.hasText(packVO.getPack_nm()))){
			exception.setFlag(SmartUXProperties.getProperty("NOT FOUND PACK_NM"));
			exception.setMessage("타이틀은 필수로 들어가야 합니다");
			throw exception;
		}
		// 템플릿 타입
		if(!(StringUtils.hasText(packVO.getTemplate_type()))){
			exception.setFlag(SmartUXProperties.getProperty("NOT FOUND TEMPATE_TYPE"));
			exception.setMessage("템플릿 타입은 필수로 선택해야 합니다");
			throw exception;
		}
		
		// BYTE 수 체크
		// 타이틀
		if(packVO.getPack_nm().getBytes().length > 100){
			exception.setFlag("PACK_NM LENGTH");
			exception.setMessage("타이틀은 100Byte 이내이어야 합니다");
		
			throw exception;
		}

	}
}
