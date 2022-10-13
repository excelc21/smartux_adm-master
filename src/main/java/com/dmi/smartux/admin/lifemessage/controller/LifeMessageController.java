package com.dmi.smartux.admin.lifemessage.controller;

import com.dmi.commons.consts.ResultCode;
import com.dmi.commons.exception.MimsCommonException;
import com.dmi.commons.factory.FileProcessFunction;
import com.dmi.commons.factory.FileProcessLockFactory;
import com.dmi.commons.logger.CLog;
import com.dmi.commons.model.RestfulCodeMsg;
import com.dmi.commons.utility.HttpClientUtils;
import com.dmi.commons.utility.RestfulUtils;
import com.dmi.smartux.admin.lifemessage.service.LifeMessageService;
import com.dmi.smartux.admin.lifemessage.vo.LifeMessageInfo;
import com.dmi.smartux.common.exception.ExceptionHandler;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.CookieUtil;
import com.dmi.smartux.common.util.HTMLCleaner;
import com.dmi.smartux.common.vo.CUDResult;
import net.sf.json.JSONSerializer;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class LifeMessageController {
	
	private final Log logger = LogFactory.getLog(this.getClass());
	
	
	@Autowired
	LifeMessageService service;
	
	private final Log applyLogger = LogFactory.getLog("adminApplyCacheHistroy");

	/**
	 * 생활지수 문구 즉시적용
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/lifemessage/lifeMessageApplyCache", method=RequestMethod.POST )
	public ResponseEntity<String> setYoutubeActivateCache(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value="schedule_yn", defaultValue="Y")  String schedule_yn,
			@RequestParam(value="callByScheduler",  defaultValue="Y") final  String callByScheduler
			) throws Exception{

		final CLog cLog = new CLog(applyLogger, request.getRemoteAddr(), request.getRequestURI(), request.getMethod());
		final String loginUser = schedule_yn.equals("Y") ? "batch" : CookieUtil.getCookieUserID(request);
		final CUDResult result = new CUDResult();

		try {
			cLog.startLog("생활지수 문구 즉시적용 : ",  loginUser);
			final String host = SmartUXProperties.getProperty("cache.sync.public.host");
			final String port = SmartUXProperties.getProperty("cache.sync.public.port");

			String fileLockPath = SmartUXProperties.getProperty("LifeMessage.refreshLifeMessageList.fileLock");
			int waitTime = NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.wait"), 5);

			FileProcessLockFactory.start(fileLockPath, waitTime,  new FileProcessFunction("생활지수 문구 즉시적용", ResultCode.ApplyRequestFail) {

				@Override
				public void run() throws Exception {
					
					//즉시적용 호출 시 sleep 값 추가 -> DB분리건으로 OGG가 10초 이상 걸리는 경우가 있어 즉시적용 시 반영이 안돼서 추가함
					Thread.sleep(Integer.parseInt(org.apache.commons.lang.StringUtils.defaultIfEmpty(SmartUXProperties.getProperty("apply.cache.call.sleep"), "10000")));
					
					RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
							host,
							port,
							SmartUXProperties.getProperty("LifeMessage.refreshLifeMessageList.url"),//+"?callByScheduler="+callByScheduler,
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.method"), "POST"),
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"),
							MediaType.APPLICATION_JSON.toString(),
							NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.timeout"), 60000)).getResponseBody());

					result.setFlag(codeMsg.getCode());
					result.setMessage(codeMsg.getMessage());
					cLog.middleLog("생활지수 문구 즉시적용", "API 호출", codeMsg.getCode(), codeMsg.getMessage());
				}
			});
		} catch (MimsCommonException e) {
			cLog.warnLog("생활지수 문구 즉시적용", "실패", e.getFlag(), e.getMessage(), e.getLocalizedMessage());
			result.setFlag(e.getFlag());
			result.setMessage(e.getMessage());
		} finally {
			cLog.endLog("생활지수 문구 즉시적용", loginUser, result.getFlag(), result.getMessage());
		}

		String resultMsg = "{\"flag\" : \"" + result.getFlag() + "\", \"message\" : \"" + result.getMessage() + "\"}";
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(resultMsg).toString(), responseHeaders, HttpStatus.OK);
	}


	@RequestMapping(value="/admin/lifemessage/getLifeMessageList", method={RequestMethod.GET, RequestMethod.POST} )
	public String getLifeMessageList(
			Model model,
			@RequestParam(value="searchType", required=false, defaultValue="") String searchType,
			@RequestParam(value="searchText", required=false, defaultValue="") String searchText
			){

		try {
			searchType 	= HTMLCleaner.clean(searchType);
			searchText 	= HTMLCleaner.clean(searchText);
			
			List<LifeMessageInfo> list = this.service.getLifeMessageList();
			
			model.addAttribute("list", list);
		} catch (Exception e) {
			logger.error("[getLifeMessageList]["+e.getClass().getName()+"]["+e.getMessage()+"]");
		}

		return "/admin/senior/lifemessage/getLifeMessageList";
	}

	@RequestMapping(value="/admin/lifemessage/insertLifeMessage", method=RequestMethod.GET )
	public String insertLifeMessage(Model model){

		try {
			//List list = this.service.getLifeMessageList(type.name(), String.valueOf(NATION.getType()));
			//model.addAttribute("list", list);
            //model.addAttribute("typeList", Category.getList());
		} catch (Exception e) {
			logger.error("[insertLifeMessage]["+e.getClass().getName()+"]["+e.getMessage()+"]");
		}
		return "/admin/senior/lifemessage/insertLifeMessage";
	}

	@RequestMapping(value="/admin/lifemessage/insertLifeMessage", method=RequestMethod.POST )
	public @ResponseBody String insertLifeMessage(
			@RequestParam(value="cont_msg", required=false, defaultValue="") String cont_msg,
			@RequestParam(value="cont_type", required=false, defaultValue="") String cont_type,
			@RequestParam(value="cont_type_name", required=false, defaultValue="") String cont_type_name,
			@RequestParam(value="display_time", required=false, defaultValue="") String display_time,
			@RequestParam(value="start_point", required=false, defaultValue="") String start_point,
			@RequestParam(value="end_point", required=false, defaultValue="") String end_point,
			@RequestParam(value="writeId", required=false, defaultValue="") String writeId
			){

		cont_msg 		= HTMLCleaner.clean(cont_msg);
		cont_type 		= HTMLCleaner.clean(cont_type);
		cont_type_name 	= HTMLCleaner.clean(cont_type_name);
		display_time 	= HTMLCleaner.clean(display_time);
		start_point  	= HTMLCleaner.clean(start_point);
		end_point  		= HTMLCleaner.clean(end_point);
        writeId 		= HTMLCleaner.clean(writeId);

		String resultcode;
		String resultmessage;

        try{
			LifeMessageInfo lifeMessageInfo = new LifeMessageInfo();
			lifeMessageInfo.setCont_msg(cont_msg);
			lifeMessageInfo.setCont_type(cont_type);
			lifeMessageInfo.setCont_type_name(cont_type_name);
			lifeMessageInfo.setDisplay_time(display_time);
			lifeMessageInfo.setStart_point(start_point);
			lifeMessageInfo.setEnd_point(end_point);
			lifeMessageInfo.setWriteId(writeId);

			this.service.insertLifeMessage(lifeMessageInfo);

			resultcode = SmartUXProperties.getProperty("flag.success");
			resultmessage = SmartUXProperties.getProperty("message.success");
		}catch(Exception e){
			logger.error("[insertLifeMessage]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}

		return "{\"flag\" : \"" + resultcode + "\", \"message\" : \"" + resultmessage + "\"}";
	}


	@RequestMapping(value="/admin/lifemessage/deleteLifeMessage", method=RequestMethod.POST)
	public @ResponseBody String deleteLifeMessage(@RequestParam(value="order[]") int[] orderList){

		String resultcode;
		String resultmessage;

		try{
			service.deleteLifeMessage(orderList);

			resultcode = SmartUXProperties.getProperty("flag.success");
			resultmessage = SmartUXProperties.getProperty("message.success");
		}catch(Exception e){
			logger.error("[deleteLifeMessage]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}

		return "{\"flag\" : \"" + resultcode + "\", \"message\" : \"" + resultmessage + "\"}";
	}
	
	@RequestMapping(value="/admin/lifemessage/viewLifeMessage", method=RequestMethod.GET )
	public String viewLifeMessage(
			Model model,
			@RequestParam(value="searchType", required=false, defaultValue="") String searchType,
			@RequestParam(value="order", required=false, defaultValue="") String order
			){

		try {
			LifeMessageInfo view = this.service.getViewLifeMessage(searchType, order);
			model.addAttribute("view", view);
            //model.addAttribute("typeList", Category.getList());
		} catch (Exception e) {
			logger.error("[viewLifeMessage]["+e.getClass().getName()+"]["+e.getMessage()+"]");
		}
		return "/admin/senior/lifemessage/viewLifeMessage";
	}

	@RequestMapping(value="/admin/lifemessage/updateLifeMessage", method=RequestMethod.POST )
	public @ResponseBody String updateLifeMessage(
			@RequestParam(value="cont_msg", required=false, defaultValue="") String cont_msg,
			@RequestParam(value="cont_type", required=false, defaultValue="") String cont_type,
			@RequestParam(value="cont_type_name", required=false, defaultValue="") String cont_type_name,
			@RequestParam(value="display_time", required=false, defaultValue="") String display_time,
			@RequestParam(value="start_point", required=false, defaultValue="") String start_point,
			@RequestParam(value="end_point", required=false, defaultValue="") String end_point,
			@RequestParam(value="writeId", required=false, defaultValue="") String writeId,
			@RequestParam(value="order", required=false, defaultValue="") String order
			){

		cont_msg 		= HTMLCleaner.clean(cont_msg);
		cont_type 		= HTMLCleaner.clean(cont_type);
		cont_type_name 	= HTMLCleaner.clean(cont_type_name);
		display_time 	= HTMLCleaner.clean(display_time);
		start_point  	= HTMLCleaner.clean(start_point);
		end_point  		= HTMLCleaner.clean(end_point);
        writeId 		= HTMLCleaner.clean(writeId);
        order 			= HTMLCleaner.clean(order);

		String resultcode;
		String resultmessage;

        try{
			LifeMessageInfo lifeMessageInfo = new LifeMessageInfo();
			lifeMessageInfo.setCont_msg(cont_msg);
			lifeMessageInfo.setCont_type(cont_type);
			lifeMessageInfo.setCont_type_name(cont_type_name);
			lifeMessageInfo.setDisplay_time(display_time);
			lifeMessageInfo.setStart_point(start_point);
			lifeMessageInfo.setEnd_point(end_point);
			lifeMessageInfo.setWriteId(writeId);
			lifeMessageInfo.setOrder(Integer.parseInt(order));

			this.service.updateLifeMessage(lifeMessageInfo);

			resultcode = SmartUXProperties.getProperty("flag.success");
			resultmessage = SmartUXProperties.getProperty("message.success");
		}catch(Exception e){
			logger.error("[updateLifeMessage]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}

		return "{\"flag\" : \"" + resultcode + "\", \"message\" : \"" + resultmessage + "\"}";
	}

}
