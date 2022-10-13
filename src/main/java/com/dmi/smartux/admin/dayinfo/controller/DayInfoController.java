package com.dmi.smartux.admin.dayinfo.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dmi.commons.consts.ResultCode;
import com.dmi.commons.exception.MimsCommonException;
import com.dmi.commons.factory.FileProcessFunction;
import com.dmi.commons.factory.FileProcessLockFactory;
import com.dmi.commons.logger.CLog;
import com.dmi.commons.model.RestfulCodeMsg;
import com.dmi.commons.utility.DateUtils;
import com.dmi.commons.utility.HttpClientUtils;
import com.dmi.commons.utility.RestfulUtils;
import com.dmi.smartux.admin.dayinfo.service.DayInfoService;
import com.dmi.smartux.admin.dayinfo.vo.DayInfoParamVo;
import com.dmi.smartux.admin.dayinfo.vo.DayInfoProcVo;
import com.dmi.smartux.admin.dayinfo.vo.InsertDayInfoVo;
import com.dmi.smartux.admin.dayinfo.vo.SelectDayInfoDtailVo;
import com.dmi.smartux.admin.dayinfo.vo.UpdateDayInfoVo;
import com.dmi.smartux.admin.gallery.vo.GalleryProcResultVo;
import com.dmi.smartux.common.exception.ExceptionHandler;
import com.dmi.smartux.common.exception.SmartUXException;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.CookieUtil;
import com.dmi.smartux.common.vo.CUDResult;
import com.dmi.smartux.common.vo.ResultVO;

@Controller
public class DayInfoController {
	
	private final Log logger = LogFactory.getLog(this.getClass());
	private final Log applyLogger = LogFactory.getLog("adminApplyCacheHistroy");

	private ObjectMapper om = new ObjectMapper();
	
	@Autowired
	DayInfoService service;
	
	/**
	 * 갤러리 마스터정보 목록
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/dayinfo/dayInfoList", method = RequestMethod.GET)
    public String getDayInfoList(
			@RequestParam(value = "search_type", required = false, defaultValue="1") String search_type,
			@RequestParam(value = "display_type", required = false, defaultValue="") String display_type,
			@RequestParam(value = "start_dt", required = false, defaultValue="") String start_dt,
			@RequestParam(value = "end_dt", required = false, defaultValue="") String end_dt,
            Model mode, HttpServletRequest request) throws Exception {
    	
    	logger.info("[getDayInfoList][S]["+search_type+"]["+display_type+"]["+start_dt+"]["+end_dt+"]");
    	try {
    		search_type = StringUtils.isEmpty(search_type) ? "1" : search_type;
	    	DayInfoParamVo dayinfoparamVo = new DayInfoParamVo();
	    	dayinfoparamVo.setSearch_type(search_type);
	    	if("1".equals(search_type)) {
	    		start_dt = StringUtils.isEmpty(start_dt) ? DateUtils.convertToString(new Date(), "MM.dd") : start_dt;
	    		end_dt = StringUtils.isEmpty(end_dt) ? DateUtils.convertToString(DateUtils.addDate(Calendar.DATE, 6), "MM.dd") : end_dt;
	    		
		    	dayinfoparamVo.setStart_dt(start_dt.replace(".", ""));
		    	dayinfoparamVo.setEnd_dt(end_dt.replace(".", ""));
	    	} else if("2".equals(search_type)) {
		    	dayinfoparamVo.setDisplay_type(display_type);
	    	}
	    	
	    	List<DayInfoProcVo> dayinfoprocVo = service.getDayInfoList(dayinfoparamVo);
	    	
	    	mode.addAttribute("vo", dayinfoprocVo);
	    	mode.addAttribute("search_type", search_type);
	    	mode.addAttribute("display_type", display_type);
	    	mode.addAttribute("start_dt", start_dt);
	    	mode.addAttribute("end_dt", end_dt);
    	}catch(java.lang.Exception e) {
        	logger.error("[getDayInfoList][ERROR]["+search_type+"]["+display_type+"]["+start_dt+"]["+end_dt+"]["+e.getClass().getName()+"]["+e.getMessage()+"]");
    	}
    	logger.info("[getDayInfoList][E]["+search_type+"]["+display_type+"]["+start_dt+"]["+end_dt+"]");
        return "/admin/dayinfo/dayInfoList";
    }
	
	/**
	 * 일별정보 삭제
	 * @param seq
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/dayinfo/deleteDayInfoProc", method = RequestMethod.GET)
    public ResponseEntity<String> deleteDayInfoProc(
			@RequestParam(value = "seq", required = true, defaultValue = "") String seq,
			HttpServletRequest request) throws Exception {

    	logger.info("[deleteDayInfoProc]["+seq+"][START]");
    	ResultVO result = new ResultVO();
		
    	try{
    		String cookieID = CookieUtil.getCookieUserID(request);
    		service.deleteDayInfo(seq, cookieID);
    		
    		result.setFlag(SmartUXProperties.getProperty("flag.success"));
    		result.setMessage(SmartUXProperties.getProperty("message.success"));
    	}catch(java.lang.Exception e){
    		ExceptionHandler eh = new ExceptionHandler(e);
    		result.setFlag(eh.getFlag());
    		result.setMessage(eh.getMessage());
    		logger.error("[deleteDayInfoProc]["+seq+"][E]:" + e.getClass().getName() + ":" + e.getMessage());
    	}
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8"); 

    	logger.info("[deleteDayInfoProc]["+seq+"][END]");
		return new ResponseEntity<String>(om.writeValueAsString(result), responseHeaders, HttpStatus.OK);
    }
	
	/**
	 * 일별정보 등록 화면
	 * @param callbak
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/dayinfo/insertDayInfoPop", method = RequestMethod.GET)
    public String insertDayInfoPop(
            @RequestParam(value = "callbak", required = false, defaultValue = "") String callbak,
            Model model, HttpServletRequest request) throws Exception {
    	logger.info("[insertDayInfoPop]");
    	
    	model.addAttribute("callbak", callbak);
    	return "/admin/dayinfo/insertDayInfoPop";
    }
	
	/**
	 * 일별정보 등록
	 * @param message
	 * @param display_type
	 * @param display_time
	 * @param display_date
	 * @param speaker
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/dayinfo/insertDayInfoProc", method = RequestMethod.POST)
    public ResponseEntity<String> insertDayInfoProc(
			@RequestParam(value = "message", required = true, defaultValue = "") String message,
			@RequestParam(value = "display_type", required = true, defaultValue = "") String display_type,
			@RequestParam(value = "display_time", required = true, defaultValue = "") String display_time,
			@RequestParam(value = "display_date", required = true, defaultValue = "") String display_date,
			@RequestParam(value = "speaker", required = false, defaultValue = "") String speaker,
			@RequestParam(value = "etc", required = false, defaultValue = "") String etc,
			HttpServletRequest request) throws Exception {

    	logger.info("[insertDayInfoProc][START]");
    	GalleryProcResultVo result = new GalleryProcResultVo();
		
    	try{
    		String cookieID = CookieUtil.getCookieUserID(request);
    		
    		InsertDayInfoVo insertdayinfoVo = new InsertDayInfoVo();
    		insertdayinfoVo.setMessage(message);
    		insertdayinfoVo.setDisplay_type(display_type);
    		insertdayinfoVo.setDisplay_time(display_time);
    		insertdayinfoVo.setSpeaker(speaker);
    		insertdayinfoVo.setEtc(etc);
    		insertdayinfoVo.setReg_id(cookieID);
    		
    		String[] arrDate = display_date.split("\\.");
    		if(arrDate.length == 2) {
    			insertdayinfoVo.setDisplay_year("00");
    			insertdayinfoVo.setDisplay_date(arrDate[0]+arrDate[1]);
    		}else if(arrDate.length > 2) {
    			insertdayinfoVo.setDisplay_year(arrDate[0]);
    			insertdayinfoVo.setDisplay_date(arrDate[1]+arrDate[2]);
    		}else {
    			throw new SmartUXException(SmartUXProperties.getProperty("flag.etc"),"날짜지정이 잘못되었습니다.");
    		}
    		
    		service.insertDayInfoProc(insertdayinfoVo);
    		
    		result.setFlag(SmartUXProperties.getProperty("flag.success"));
    		result.setMessage(SmartUXProperties.getProperty("message.success"));
    	}catch(java.lang.Exception e){
    		ExceptionHandler eh = new ExceptionHandler(e);
    		result.setFlag(eh.getFlag());
    		result.setMessage(eh.getMessage());
    		logger.error("[insertDayInfoProc][E]:" + e.getClass().getName() + ":" + e.getMessage());
    	}
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/plain; charset=UTF-8"); 

    	logger.info("[insertDayInfoProc][END]");
		return new ResponseEntity<String>(om.writeValueAsString(result), responseHeaders, HttpStatus.OK);
    }
	
	/**
	 * 일별정보 수정화면
	 * @param seq
	 * @param callbak
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/dayinfo/updateDayInfoPop", method = RequestMethod.GET)
    public String updateDayInfoPop(
            @RequestParam(value = "seq", required = true, defaultValue = "") int seq,
            @RequestParam(value = "callbak", required = false, defaultValue = "") String callbak,
            Model model, HttpServletRequest request) throws Exception {
    	logger.info("[updateDayInfoPop][START]["+seq+"]");
    	
    	SelectDayInfoDtailVo selectdayinfodtailVo = service.getDayInfoDatail(seq);

    	model.addAttribute("vo", selectdayinfodtailVo);
    	model.addAttribute("callbak", callbak);
    	logger.info("[updateDayInfoPop][END]["+seq+"]");
    	return "/admin/dayinfo/updateDayInfoPop";
    }
	
	/**
	 * 일별정보 수정
	 * @param seq
	 * @param message
	 * @param display_type
	 * @param display_time
	 * @param display_date
	 * @param speaker
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/dayinfo/updateDayInfoProc", method = RequestMethod.POST)
    public ResponseEntity<String> updateDayInfoProc(
			@RequestParam(value = "seq", required = true, defaultValue = "") int seq,
			@RequestParam(value = "message", required = true, defaultValue = "") String message,
			@RequestParam(value = "display_type", required = true, defaultValue = "") String display_type,
			@RequestParam(value = "display_time", required = true, defaultValue = "") String display_time,
			@RequestParam(value = "display_date", required = true, defaultValue = "") String display_date,
			@RequestParam(value = "speaker", required = false, defaultValue = "") String speaker,
			@RequestParam(value = "etc", required = false, defaultValue = "") String etc,
			HttpServletRequest request) throws Exception {

    	logger.info("[updateDayInfoProc][START]["+seq+"]");
    	GalleryProcResultVo result = new GalleryProcResultVo();
		
    	try{
    		String cookieID = CookieUtil.getCookieUserID(request);
    		
    		UpdateDayInfoVo updateDayInfoVo = new UpdateDayInfoVo();
    		updateDayInfoVo.setSeq(seq);
    		updateDayInfoVo.setMessage(message);
    		updateDayInfoVo.setDisplay_type(display_type);
    		updateDayInfoVo.setDisplay_time(display_time);
    		updateDayInfoVo.setSpeaker(speaker);
    		updateDayInfoVo.setEtc(etc);
    		updateDayInfoVo.setMod_id(cookieID);
    		
    		String[] arrDate = display_date.split("\\.");
    		if(arrDate.length == 2) {
    			updateDayInfoVo.setDisplay_year("00");
    			updateDayInfoVo.setDisplay_date(arrDate[0]+arrDate[1]);
    		}else if(arrDate.length > 2) {
    			updateDayInfoVo.setDisplay_year(arrDate[0]);
    			updateDayInfoVo.setDisplay_date(arrDate[1]+arrDate[2]);
    		}else {
    			throw new SmartUXException(SmartUXProperties.getProperty("flag.etc"),"날짜지정이 잘못되었습니다.");
    		}
    		
    		service.updateDayInfoProc(updateDayInfoVo);
    		
    		result.setFlag(SmartUXProperties.getProperty("flag.success"));
    		result.setMessage(SmartUXProperties.getProperty("message.success"));
    	}catch(java.lang.Exception e){
    		ExceptionHandler eh = new ExceptionHandler(e);
    		result.setFlag(eh.getFlag());
    		result.setMessage(eh.getMessage());
    		logger.error("[updateDayInfoProc][E]["+seq+"]:" + e.getClass().getName() + ":" + e.getMessage());
    	}
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/plain; charset=UTF-8"); 

    	logger.info("[updateDayInfoProc][END]["+seq+"]");
		return new ResponseEntity<String>(om.writeValueAsString(result), responseHeaders, HttpStatus.OK);
    }
	
	/**
	 * 캐시 즉시적용
	 * @param callByScheduler
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
    @RequestMapping(value = "/admin/dayinfo/applyCache", method = RequestMethod.POST)
    public ResponseEntity<String> applyCache(
			@RequestParam(value="callByScheduler",  defaultValue="Y") final  String callByScheduler,
			HttpServletRequest request,
            Model model) throws Exception {
    	
    	final CLog cLog = new CLog(applyLogger, request.getRemoteAddr(), request.getRequestURI(), request.getMethod());
		final String loginUser = CookieUtil.getCookieUserID(request);
		final CUDResult result = new CUDResult();
		
		try {
			cLog.startLog("일별정보 즉시적용 : ",  loginUser);
			final String host = SmartUXProperties.getProperty("cache.sync.public.host");
			final String port = SmartUXProperties.getProperty("cache.sync.public.port");
			
			String fileLockPath = SmartUXProperties.getProperty("DayInfoDao.refreshDayInfoList.fileLock");
			int waitTime = NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.wait"), 5);
			
			FileProcessLockFactory.start(fileLockPath, waitTime,  new FileProcessFunction("갤러리 즉시적용", ResultCode.ApplyRequestFail) {
				
				@Override
				public void run() throws Exception {
					
					//즉시적용 호출 시 sleep 값 추가 -> DB분리건으로 OGG가 10초 이상 걸리는 경우가 있어 즉시적용 시 반영이 안돼서 추가함
					Thread.sleep(Integer.parseInt(org.apache.commons.lang.StringUtils.defaultIfEmpty(SmartUXProperties.getProperty("apply.cache.call.sleep"), "10000")));
					
					RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
							host, 
							port, 
							SmartUXProperties.getProperty("DayInfoDao.refreshDayInfoList.url")+"?callByScheduler="+callByScheduler,
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.method"), "POST"), 
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"), 
							MediaType.APPLICATION_JSON.toString(),
							NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.timeout"), 60000)).getResponseBody());
					
					result.setFlag(codeMsg.getCode());
					result.setMessage(codeMsg.getMessage());
					cLog.middleLog("일별정보 즉시적용", "API 호출", codeMsg.getCode(), codeMsg.getMessage());
				}
			});
		} catch (MimsCommonException e) {
			cLog.warnLog("일별정보 즉시적용", "실패", e.getFlag(), e.getMessage(), e.getLocalizedMessage());
			result.setFlag(e.getFlag());
			result.setMessage(e.getMessage());
		} finally {
			cLog.endLog("일별정보 즉시적용", loginUser, result.getFlag(), result.getMessage());
		}
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8"); 
		return new ResponseEntity<String>(om.writeValueAsString(result), responseHeaders, HttpStatus.OK);
    }

}
