package com.dmi.smartux.admin.season.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dmi.smartux.admin.season.service.SeasonService;
import com.dmi.smartux.admin.season.vo.SeasonSearchVo;
import com.dmi.smartux.admin.season.vo.SeasonVo;
import com.dmi.smartux.common.exception.ExceptionHandler;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.CookieUtil;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.util.HTMLCleaner;


@Controller("A.SeasonCotroller")
public class SeasonCotroller {
	
	@Autowired
	SeasonService service;
	
	/**
	 * 전체 시즌 캐시 즉시적용
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/season/applyCache", method = RequestMethod.POST)
	public ResponseEntity<String> applyCache() throws Exception {
		String resultcode = "";
		String resultmessage = "";

		try {
			//캐시갱신 호출 전 시즌 제외카테고리를 한번 갱신한다
			String list = service.getExceptList();
			if(list.length() > 0) {
				service.exceptProc(list);
			}
			
			String param = "callByScheduler=A"; // 관리자툴에서 호출한다는 의미로 셋팅해준다(이 값이 A여야 DB에서 바로 읽어서 캐쉬에 반영한다)
			int timeout = Integer.parseInt(SmartUXProperties.getProperty("scheduler.timeout"));      // timeout 값은 스케쥴러것을 사용
			int retrycnt = Integer.parseInt(SmartUXProperties.getProperty("scheduler.retrycnt"));    // 재시도 횟수는 스케줄러 것을 사용
			//VideoLTE
			String url = SmartUXProperties.getProperty("SeasonDao.refreshSeason.CacheURL");
			String protocolName = SmartUXProperties.getProperty("SeasonDao.refreshSeason.protocol");
						
			Map<String,Object> result = service.applyCache(url, param, timeout, retrycnt, protocolName); // 다른 서버의 캐쉬 동기화 작업 진행
			resultcode = (String)result.get("res");
			resultmessage = (String)result.get("msg");
		} catch (Exception e) {
			com.dmi.smartux.common.exception.ExceptionHandler handler = new com.dmi.smartux.common.exception.ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}

		String result = "{\"flag\" : \"" + resultcode + "\", \"message\" : \"" + resultmessage + "\"}";

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(result, responseHeaders, HttpStatus.CREATED);
	}
	
	/**
	 * 개별 시즌 캐시 즉시적용
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/season/applyCacheById", method = RequestMethod.POST)
	public ResponseEntity<String> applyCacheById(
			@RequestParam(value="season_id", required=false, defaultValue="") String seasonId,
			@RequestParam(value="app_tp", required=false, defaultValue="") String app_tp
			) throws Exception {
		String resultcode = "";
		String resultmessage = "";

		try {
			String param = "operation=U&season_id=" + seasonId + "&app_tp=" + app_tp;		// U : add/upate, D : delete 
			int timeout = Integer.parseInt(SmartUXProperties.getProperty("scheduler.timeout"));      // timeout 값은 스케쥴러것을 사용
			int retrycnt = Integer.parseInt(SmartUXProperties.getProperty("scheduler.retrycnt"));    // 재시도 횟수는 스케줄러 것을 사용
			//VideoLTE
			String url = SmartUXProperties.getProperty("SeasonDao.refreshSeasonById.CacheURL");
			String protocolName = SmartUXProperties.getProperty("SeasonDao.refreshSeason.protocol");
			
			Map<String,Object> result = service.applyCache(url, param, timeout, retrycnt, protocolName); // 다른 서버의 캐쉬 동기화 작업 진행
			resultcode = (String)result.get("res");
			resultmessage = (String)result.get("msg");
			
			if (SmartUXProperties.getProperty("flag.success").equals(resultcode)) {
				// 시즌 캐쉬 반영일시 갱신
				service.updateCacheTime(seasonId);
			}
			
		} catch (Exception e) {
			com.dmi.smartux.common.exception.ExceptionHandler handler = new com.dmi.smartux.common.exception.ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}

		String result = "{\"flag\" : \"" + resultcode + "\", \"message\" : \"" + resultmessage + "\"}";

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(result, responseHeaders, HttpStatus.CREATED);
	}
	
	/**
	 * 시즌 목록 페이지
	 * @param findName
	 * @param findValue
	 * @param pageNum
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/season/seasonList", method=RequestMethod.GET)
	public String seasonList(@RequestParam(value="findName", required=false, defaultValue="") String findName
			, @RequestParam(value="findValue", required=false, defaultValue="") String findValue
			, @RequestParam(value="seriesYn", required=false, defaultValue="") String seriesYn
			, @RequestParam(value="app_tp", required=false, defaultValue="N") String app_tp
			, @RequestParam(value="pageNum", required=false, defaultValue="1") String pageNum
			, Model model) throws Exception {

		findName = HTMLCleaner.clean(findName);
		findValue = HTMLCleaner.clean(findValue);
		pageNum = HTMLCleaner.clean(pageNum);
		
		SeasonSearchVo vo = new SeasonSearchVo();
		
		//페이지 
		vo.setPageSize(GlobalCom.isNumber(vo.getPageSize(),10));
		vo.setBlockSize(GlobalCom.isNumber(vo.getBlockSize(),10));
		vo.setPageNum(GlobalCom.isNumber(Integer.parseInt(GlobalCom.isNull(pageNum, "1")),1));
		//검색 
		vo.setFindName(GlobalCom.isNull(findName, "SEASON_TITLE"));
		vo.setFindValue(findValue);
		vo.setSeries_yn(seriesYn);
		vo.setApp_tp(GlobalCom.isNull(app_tp, "N"));
		
		List<SeasonVo> list = service.getSeasonList(vo);
		vo.setPageCount(service.getSeasonListCnt(vo));
		
		model.addAttribute("list", list);
		model.addAttribute("vo", vo);
		return "/admin/season/seasonList";
	}
	
	/**
	 * 시즌 등록 페이지
	 * @param findName
	 * @param findValue
	 * @param pageNum
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/season/insertSeason", method=RequestMethod.GET)
	public String insertSeason(@RequestParam(value="findName", required=false, defaultValue="") String findName
			, @RequestParam(value="findValue", required=false, defaultValue="") String findValue
			, @RequestParam(value="seriesYn", required=false, defaultValue="") String seriesYn
			, @RequestParam(value="app_tp", required=false, defaultValue="N") String app_tp
			, @RequestParam(value="pageNum", required=false, defaultValue="1") String pageNum
			, Model model) throws Exception {
		
		findName = HTMLCleaner.clean(findName);
		findValue = HTMLCleaner.clean(findValue);
		pageNum = HTMLCleaner.clean(pageNum);
		
		SeasonSearchVo vo = new SeasonSearchVo();
		vo.setFindName(GlobalCom.isNull(findName, "SEASON_TITLE"));
		vo.setFindValue(findValue);
		vo.setSeries_yn(seriesYn);
		vo.setApp_tp(GlobalCom.isNull(app_tp, "N"));
		vo.setPageNum(GlobalCom.isNumber(Integer.parseInt(GlobalCom.isNull(pageNum, "1")),1));
		
		model.addAttribute("vo", vo);
		model.addAttribute("isUpdate", 0);
		return "/admin/season/seasonForm";
	}
	
	/**
	 * 시즌 등록
	 * @param season_title
	 * @param seasonData
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/season/insertProc", method=RequestMethod.POST)
	@ResponseBody
	public String insertProc(@RequestParam(value="season_title", required=false, defaultValue="") String season_title
			, @RequestParam(value="series_yn", required=false, defaultValue="") String series_yn
			, @RequestParam(value="app_tp", required=false, defaultValue="N") String app_tp
			, @RequestParam(value="seasonData[]") List<String> seasonData
			, HttpServletRequest request) throws Exception{
		
		String resCode = "";
		String resMessage = "";
		String cookieID = CookieUtil.getCookieUserID(request);

		try{
				SeasonVo vo = new SeasonVo();
				vo.setSeason_title(season_title);
				vo.setSeries_yn(series_yn);
				vo.setApp_tp(GlobalCom.isNull(app_tp, "N"));
				vo.setSeasonData(seasonData);
				vo.setReg_id(cookieID);
				vo.setMod_id(cookieID);
				
				service.insertProc(vo);
				resCode = SmartUXProperties.getProperty("flag.success");
				resMessage = SmartUXProperties.getProperty("message.success");
		}catch(Exception e){
			ExceptionHandler handler = new ExceptionHandler(e);
			resCode 	= handler.getFlag();
			resMessage 	= handler.getMessage();
		}
		
		return "{\"res\" : \"" + resCode + "\", \"msg\" : \"" + resMessage + "\"}";
	}
	
	/**
	 * 시즌 수정 페이지
	 * @param season_id
	 * @param findName
	 * @param findValue
	 * @param pageNum
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/season/updateSeason", method=RequestMethod.GET)
	public String updateSeason(@RequestParam(value="season_id", required=false, defaultValue="") String season_id
			, @RequestParam(value="findName", required=false, defaultValue="") String findName
			, @RequestParam(value="findValue", required=false, defaultValue="") String findValue
			, @RequestParam(value="seriesYn", required=false, defaultValue="") String seriesYn
			, @RequestParam(value="app_tp", required=false, defaultValue="") String app_tp
			, @RequestParam(value="pageNum", required=false, defaultValue="1") String pageNum
			, Model model) throws Exception {

		season_id = HTMLCleaner.clean(season_id);
		findName = HTMLCleaner.clean(findName);
		findValue = HTMLCleaner.clean(findValue);
		pageNum = HTMLCleaner.clean(pageNum);
		
		SeasonSearchVo vo = new SeasonSearchVo();
		vo.setFindName(GlobalCom.isNull(findName, "SEASON_TITLE"));
		vo.setFindValue(findValue);
		vo.setSeries_yn(seriesYn);
		vo.setApp_tp(app_tp);
		vo.setPageNum(GlobalCom.isNumber(Integer.parseInt(GlobalCom.isNull(pageNum, "1")),1));
		
		SeasonVo parent_season = service.getSeasonDetail(season_id);
		String childList = service.getSeasonDetailList(season_id);
		
		model.addAttribute("vo", vo);
		model.addAttribute("parent_season", parent_season);
		model.addAttribute("childList", childList);
		model.addAttribute("isUpdate", 1);
		return "/admin/season/seasonForm";
	}
	
	/**
	 * 시즌 수정
	 * @param season_title
	 * @param season_id
	 * @param seasonData
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/season/updateProc", method=RequestMethod.POST)
	@ResponseBody
	public String updateProc(@RequestParam(value="season_title", required=false, defaultValue="") String season_title
			, @RequestParam(value="series_yn", required=false, defaultValue="") String series_yn
			, @RequestParam(value="season_id", required=false, defaultValue="") String season_id
			, @RequestParam(value="seasonData[]") List<String> seasonData
			, HttpServletRequest request) throws Exception{
		
		String resCode = "";
		String resMessage = "";
		String cookieID = CookieUtil.getCookieUserID(request);
		
		try{
			SeasonVo vo = new SeasonVo();
			vo.setSeason_title(season_title);
			vo.setSeries_yn(series_yn);
			vo.setSeason_id(season_id);
			vo.setSeasonData(seasonData);
			vo.setReg_id(cookieID);
			vo.setMod_id(cookieID);
			
			service.updateProc(vo);
			resCode = SmartUXProperties.getProperty("flag.success");
			resMessage = SmartUXProperties.getProperty("message.success");
		}catch(Exception e){
			ExceptionHandler handler = new ExceptionHandler(e);
			resCode 	= handler.getFlag();
			resMessage 	= handler.getMessage();
		}
		
		return "{\"res\" : \"" + resCode + "\", \"msg\" : \"" + resMessage + "\"}";
	}
	
	/**
	 * 시즌 삭제
	 * @param delList
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/season/deleteProc", method=RequestMethod.POST)
	@ResponseBody
	public String deleteProc(@RequestParam(value="delList", required=false) String delList
			, @RequestParam(value="app_tp", required=false) String app_tp
			, HttpServletRequest request) throws Exception {
		String resCode = "";
		String resMessage = "";
		
		try{
			service.deleteProc(delList, app_tp);
			resCode = SmartUXProperties.getProperty("flag.success");
			resMessage = SmartUXProperties.getProperty("message.success");
		}catch(Exception e){
			ExceptionHandler handler = new ExceptionHandler(e);
			resCode 	= handler.getFlag();
			resMessage 	= handler.getMessage();
		}
		return "{\"res\" : \"" + resCode + "\", \"msg\" : \"" + resMessage + "\"}";
	}
	
	/**
	 * 시즌적용 카테고리 목록 조회
	 * @param albumId
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/season/getCategoryList", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> getCategoryList(@RequestParam(value="albumId", required=false) String albumId
			, @RequestParam(value="series_yn", required=false, defaultValue="") String series_yn
			, @RequestParam(value="category_gb", required=false, defaultValue="I") String category_gb) throws Exception {
		String resCode = "";
		String resMessage = "";
		String categoryList = "";
		
		try{
			categoryList = service.getCategoryList(albumId, series_yn, category_gb);
			resCode = SmartUXProperties.getProperty("flag.success");
			resMessage = SmartUXProperties.getProperty("message.success");
		}catch(Exception e){
			ExceptionHandler handler = new ExceptionHandler(e);
			resCode 	= handler.getFlag();
			resMessage 	= handler.getMessage();
		}
		
		String result = "{\"res\" : \"" + resCode + "\", \"msg\" : \"" + resMessage + "\", \"categoryList\" : \"" + categoryList + "\"}";
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(result, responseHeaders, HttpStatus.CREATED);
	}
	
	/**
	 * 시즌 제외카테고리 팝업
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/season/exceptPop", method=RequestMethod.GET)
	public String exceptPop(Model model) throws Exception {
		String list =  service.getExceptList();
		
		model.addAttribute("list", list);
		return "/admin/season/exceptPop";
	}
	
	/**
	 * 시즌 제외 카테고리 등록/삭제
	 * @param addList
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/season/exceptProc", method=RequestMethod.POST)
	@ResponseBody
	public String exceptProc(@RequestParam(value="addList", required=false) String addList
			, HttpServletRequest request) throws Exception {
		String resCode = "";
		String resMessage = "";
		
		try{
			service.exceptProc(addList);
			resCode = SmartUXProperties.getProperty("flag.success");
			resMessage = SmartUXProperties.getProperty("message.success");
		}catch(Exception e){
			ExceptionHandler handler = new ExceptionHandler(e);
			resCode 	= handler.getFlag();
			resMessage 	= handler.getMessage();
		}
		
		return "{\"res\" : \"" + resCode + "\", \"msg\" : \"" + resMessage + "\"}";
	}
	
}
