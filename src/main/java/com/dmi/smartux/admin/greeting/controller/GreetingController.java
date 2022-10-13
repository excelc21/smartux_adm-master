package com.dmi.smartux.admin.greeting.controller;

import static com.dmi.smartux.admin.greeting.vo.GreetingComponent.Category.NORMAL_DAY;
import static com.dmi.smartux.admin.greeting.vo.GreetingComponent.SearchType.date_type;
import static com.dmi.smartux.admin.greeting.vo.GreetingComponent.SearchType.order_change;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
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
import org.springframework.web.multipart.MultipartFile;

import com.dmi.commons.consts.ResultCode;
import com.dmi.commons.exception.MimsCommonException;
import com.dmi.commons.factory.FileProcessFunction;
import com.dmi.commons.factory.FileProcessLockFactory;
import com.dmi.commons.logger.CLog;
import com.dmi.commons.model.RestfulCodeMsg;
import com.dmi.commons.utility.HttpClientUtils;
import com.dmi.commons.utility.RestfulUtils;
import com.dmi.smartux.admin.greeting.service.GreetingService;
import com.dmi.smartux.admin.greeting.vo.GreetingComponent.Category;
import com.dmi.smartux.admin.greeting.vo.GreetingComponent.SearchType;
import com.dmi.smartux.admin.greeting.vo.GreetingInfo;
import com.dmi.smartux.common.exception.ExceptionHandler;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.CookieUtil;
import com.dmi.smartux.common.util.HTMLCleaner;
import com.dmi.smartux.common.vo.CUDResult;

import net.sf.json.JSONSerializer;

@Controller
public class GreetingController {
	
	private final Log logger = LogFactory.getLog(this.getClass());
	
	
	@Autowired
	GreetingService service;

	private final Log applyLogger = LogFactory.getLog("adminApplyCacheHistroy");

	/**
	 * 인사말 리스트 즉시적용
	 * @param request
	 * @return ResponseEntity<String>
	 * @throws Exception
	 */
	@SuppressWarnings("JavaDoc")
	@RequestMapping(value="/admin/greeting/activateCache", method=RequestMethod.POST )
	public ResponseEntity<String> setGreetingActivateCache(
			HttpServletRequest request,
			@RequestParam(value="schedule_yn", defaultValue="Y")  String schedule_yn,
			@RequestParam(value="callByScheduler",  defaultValue="Y") final  String callByScheduler
			) throws Exception{

		final CLog cLog = new CLog(applyLogger, request.getRemoteAddr(), request.getRequestURI(), request.getMethod());
		final String loginUser = schedule_yn.equals("Y") ? "batch" : CookieUtil.getCookieUserID(request);
		final CUDResult result = new CUDResult();

		try {
			cLog.startLog("인사말 검색어 즉시적용 : ",  loginUser);
			final String host = SmartUXProperties.getProperty("cache.sync.public.host");
			final String port = SmartUXProperties.getProperty("cache.sync.public.port");

			String fileLockPath = SmartUXProperties.getProperty("Greeting.refreshGreeting.fileLock");
			int waitTime = NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.wait"), 5);

			FileProcessLockFactory.start(fileLockPath, waitTime,
                    new FileProcessFunction("인사말 검색어 즉시적용", ResultCode.ApplyRequestFail) {

				@Override
				public void run() throws Exception {
					
					//즉시적용 호출 시 sleep 값 추가 -> DB분리건으로 OGG가 10초 이상 걸리는 경우가 있어 즉시적용 시 반영이 안돼서 추가함
					Thread.sleep(Integer.parseInt(org.apache.commons.lang.StringUtils.defaultIfEmpty(SmartUXProperties.getProperty("apply.cache.call.sleep"), "10000")));
					
					RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
							host,
							port,
							SmartUXProperties.getProperty("Greeting.refreshGreeting.url")+
									"?callByScheduler="+callByScheduler,
							org.apache.commons.lang.StringUtils.defaultString(
									SmartUXProperties.getProperty("cache.sync.apply.method"), "POST"),
							org.apache.commons.lang.StringUtils.defaultString(
									SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"),
							MediaType.APPLICATION_JSON.toString(),
							NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.timeout"),
									60000)).getResponseBody());

					result.setFlag(codeMsg.getCode());
					result.setMessage(codeMsg.getMessage());
					cLog.middleLog("인사말 검색어 즉시적용", "API 호출", codeMsg.getCode(), codeMsg.getMessage());
				}
			});
		} catch (MimsCommonException e) {
			cLog.warnLog("인사말 검색어 즉시적용", "실패", e.getFlag(), e.getMessage(), e.getLocalizedMessage());
			result.setFlag(e.getFlag());
			result.setMessage(e.getMessage());
		} finally {
			cLog.endLog("인사말 검색어 즉시적용", loginUser, result.getFlag(), result.getMessage());
		}

		String resultMsg = "{\"flag\" : \"" + result.getFlag() + "\", \"message\" : \"" + result.getMessage() + "\"}";
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(resultMsg).toString(), responseHeaders, HttpStatus.OK);
	}



	@RequestMapping(value="/admin/greeting/list", method={RequestMethod.GET, RequestMethod.POST} )
	public String getGreetingList(
			Model model,
			@RequestParam(value="searchType", required=false, defaultValue="") String searchType,
			@RequestParam(value="searchText", required=false, defaultValue="") String searchText
			){

		try {
			searchType 	= HTMLCleaner.clean(searchType);
			searchText 	= HTMLCleaner.clean(searchText);
			model.addAttribute("list", this.service.getGreetingList(searchType, searchText));
			model.addAttribute("searchType", searchType);
			model.addAttribute("searchText", searchText);
		} catch (Exception e) {
			logger.error("[getGreetingList]["+e.getClass().getName()+"]["+e.getMessage()+"]");
		}

		return "/admin/greeting/list";
	}

	@RequestMapping(value="/admin/greeting/insert", method=RequestMethod.GET )
	public String getGreetingInsert(Model model){

		try {
			List list = this.service.getGreetingList(date_type.key(), String.valueOf(NORMAL_DAY.getType()));
			model.addAttribute("list", list);
			model.addAttribute("nationType", NORMAL_DAY.getType());
            model.addAttribute("typeList", Category.getList());
		} catch (Exception e) {
			logger.error("[getGreetingInsert]["+e.getClass().getName()+"]["+e.getMessage()+"]");
		}
		return "/admin/greeting/insert";
	}
    @RequestMapping(value="/admin/greeting/update", method=RequestMethod.GET )
    public String getTeleportUpdate(Model model,
                                    @RequestParam(value="greeting_id", required=false, defaultValue="1") int greeting_id){

        try {
            List list = this.service.getGreetingList(SearchType.greeting_id.key(), String.valueOf(greeting_id));
            GreetingInfo greetingInfo = new GreetingInfo();
            if(CollectionUtils.isNotEmpty(list))  {
                greetingInfo = (GreetingInfo) list.get(0);
            }
            model.addAttribute("greetingInfo", greetingInfo);

            model.addAttribute("list",
                    this.service.getGreetingList(date_type.key(), String.valueOf(NORMAL_DAY.getType())));
            model.addAttribute("nationType", NORMAL_DAY.getType());
            model.addAttribute("typeList", Category.getList());
			model.addAttribute("imgUrl", this.service.getImageServerIpMims());
        } catch (Exception e) {
            logger.error("[getTeleportUpdate]["+e.getClass().getName()+"]["+e.getMessage()+"]");
        }
        return "/admin/greeting/update";
    }
    @RequestMapping(value="/admin/greeting/order/update", method=RequestMethod.GET )
    public String getGreetingOrderUpdate(Model model){

        try {
        	List list = this.service.getGreetingList(order_change.key(), "");
            model.addAttribute("list", list);
        } catch (Exception e) {
            logger.error("[getGreetingOrderUpdate]["+e.getClass().getName()+"]["+e.getMessage()+"]");
        }
        return "/admin/greeting/orderUpdate";
    }

	@RequestMapping(value="/admin/greeting/insert", method=RequestMethod.POST )
	public ResponseEntity<String> setGreetingInsert(
			@RequestParam(value="greeting_txt", required=false, defaultValue="") String greeting_txt,
			@RequestParam(value="greeting_voice", required=false, defaultValue="") MultipartFile greeting_voice,
			@RequestParam(value="bg_image", required=false, defaultValue="") MultipartFile bg_image,
			@RequestParam(value="date_type") int date_type,
			@RequestParam(value="event_day") String event_day,
			@RequestParam(value="start_point", required=false, defaultValue="") String start_point,
			@RequestParam(value="end_point", required=false, defaultValue="") String end_point,
			@RequestParam(value="write_id", required=false, defaultValue="") String write_id
			){

		greeting_txt = HTMLCleaner.clean(greeting_txt);
		event_day 	 = HTMLCleaner.clean(event_day);
		start_point  = HTMLCleaner.clean(start_point);
		end_point 	 = HTMLCleaner.clean(end_point);
		write_id 	 = HTMLCleaner.clean(write_id);

		String resultcode;
		String resultmessage;
        try{

			GreetingInfo greetingInfo = new GreetingInfo();
			greetingInfo.setGreeting_txt(greeting_txt);
			greetingInfo.setDate_type(date_type);
			greetingInfo.setEvent_day(event_day);
			greetingInfo.setStart_point(start_point);
			greetingInfo.setEnd_point(end_point);
			greetingInfo.setWrite_id(write_id);

			this.service.insertGreeting(greetingInfo, greeting_voice, bg_image);


			resultcode = SmartUXProperties.getProperty("flag.success");
			resultmessage = SmartUXProperties.getProperty("message.success");
		}catch(Exception e){
			logger.error("[setGreetingInsert]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}
		
		String resultMsg = "{\"flag\" : \"" + resultcode + "\", \"message\" : \"" + resultmessage + "\"}";
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(resultMsg).toString(), responseHeaders, HttpStatus.OK);
		
	}
	@RequestMapping(value="/admin/greeting/update", method=RequestMethod.POST )
	public ResponseEntity<String> setGreetingUpdate(
			@RequestParam(value="order", required=false, defaultValue="1") int order,
			@RequestParam(value="greeting_id", required=false, defaultValue="1") int greeting_id,
			@RequestParam(value="greeting_txt", required=false, defaultValue="") String greeting_txt,
			@RequestParam(value="greeting_voice", required=false, defaultValue="") MultipartFile greeting_voice,
			@RequestParam(value="bg_image", required=false, defaultValue="") MultipartFile bg_image,
			@RequestParam(value="date_type") int date_type,
			@RequestParam(value="event_day") String event_day,
			@RequestParam(value="start_point", required=false, defaultValue="") String start_point,
			@RequestParam(value="end_point", required=false, defaultValue="") String end_point,
			@RequestParam(value="write_id", required=false, defaultValue="") String write_id
	){

		greeting_txt = HTMLCleaner.clean(greeting_txt);
		event_day 	 = HTMLCleaner.clean(event_day);
		start_point  = HTMLCleaner.clean(start_point);
		end_point 	 = HTMLCleaner.clean(end_point);
		write_id 	 = HTMLCleaner.clean(write_id);

		String resultcode;
		String resultmessage;
		try{

			GreetingInfo greetingInfo = new GreetingInfo();
			greetingInfo.setOrder(order);
			greetingInfo.setGreeting_id(greeting_id);
			greetingInfo.setGreeting_txt(greeting_txt);
			greetingInfo.setDate_type(date_type);
			greetingInfo.setEvent_day(event_day);
			greetingInfo.setStart_point(start_point);
			greetingInfo.setEnd_point(end_point);
			greetingInfo.setWrite_id(write_id);

			this.service.updateGreeting(greetingInfo, greeting_voice, bg_image);


			resultcode = SmartUXProperties.getProperty("flag.success");
			resultmessage = SmartUXProperties.getProperty("message.success");
		}catch(Exception e){
			logger.error("[setGreetingInsert]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}

		String resultMsg = "{\"flag\" : \"" + resultcode + "\", \"message\" : \"" + resultmessage + "\"}";
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(resultMsg).toString(), responseHeaders, HttpStatus.OK);
	}


	@RequestMapping(value="/admin/greeting/delete", method=RequestMethod.POST)
	public @ResponseBody String setGreetingDelete(@RequestParam(value="order[]") int[] orderList){

		String resultcode;
		String resultmessage;

		try{
			service.deleteList(orderList);

			resultcode = SmartUXProperties.getProperty("flag.success");
			resultmessage = SmartUXProperties.getProperty("message.success");
		}catch(Exception e){
			logger.error("[setGreetingDelete]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}

		return "{\"flag\" : \"" + resultcode + "\", \"message\" : \"" + resultmessage + "\"}";
	}

	@RequestMapping(value="/admin/greeting/order/update", method=RequestMethod.POST)
	public @ResponseBody String setGreetingOrderUpdate(@RequestParam(value="code[]") int[] codeList){

		String resultcode;
		String resultmessage;

		try{
			service.setGreetingOrder(codeList);

			resultcode = SmartUXProperties.getProperty("flag.success");
			resultmessage = SmartUXProperties.getProperty("message.success");
		}catch(Exception e){
			logger.error("[setGreetingDelete]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}

		return "{\"flag\" : \"" + resultcode + "\", \"message\" : \"" + resultmessage + "\"}";
	}

}
