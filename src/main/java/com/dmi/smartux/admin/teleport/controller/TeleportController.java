package com.dmi.smartux.admin.teleport.controller;

import com.dmi.commons.consts.ResultCode;
import com.dmi.commons.exception.MimsCommonException;
import com.dmi.commons.factory.FileProcessFunction;
import com.dmi.commons.factory.FileProcessLockFactory;
import com.dmi.commons.logger.CLog;
import com.dmi.commons.model.RestfulCodeMsg;
import com.dmi.commons.utility.HttpClientUtils;
import com.dmi.commons.utility.RestfulUtils;
import com.dmi.smartux.admin.mainpanel.service.PanelViewService;
import com.dmi.smartux.admin.mainpanel.vo.PanelVO;
import com.dmi.smartux.admin.mainpanel.vo.ViewVO;
import com.dmi.smartux.admin.teleport.service.TeleportService;
import com.dmi.smartux.admin.teleport.vo.TeleportComponent.Category;
import com.dmi.smartux.admin.teleport.vo.TeleportInfo;
import com.dmi.smartux.common.exception.ExceptionHandler;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.CookieUtil;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.util.HTMLCleaner;
import com.dmi.smartux.common.vo.CUDResult;
import net.sf.json.JSONSerializer;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
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
import java.util.List;

import static com.dmi.smartux.admin.teleport.vo.TeleportComponent.Category.NATION;
import static com.dmi.smartux.admin.teleport.vo.TeleportComponent.SearchType;
import static com.dmi.smartux.admin.teleport.vo.TeleportComponent.SearchType.anchor_type;
import static com.dmi.smartux.admin.teleport.vo.TeleportComponent.SearchType.order_change;

@Controller
public class TeleportController {
	
	private final Log logger = LogFactory.getLog(this.getClass());
	
	
	@Autowired
	TeleportService service;
	@Autowired
	PanelViewService panelViewService;

	private final Log applyLogger = LogFactory.getLog("adminApplyCacheHistroy");

	/**
	 * 순간이동 리스트 즉시적용
	 * @param request
	 * @return ResponseEntity<String>
	 * @throws Exception
	 */
	@SuppressWarnings("JavaDoc")
	@RequestMapping(value="/admin/teleport/activateCache", method=RequestMethod.POST )
	public ResponseEntity<String> setTeleportActivateCache(
			HttpServletRequest request,
			@RequestParam(value="schedule_yn", defaultValue="Y")  String schedule_yn,
			@RequestParam(value="callByScheduler",  defaultValue="Y") final  String callByScheduler
			) throws Exception{

		final CLog cLog = new CLog(applyLogger, request.getRemoteAddr(), request.getRequestURI(), request.getMethod());
		final String loginUser = schedule_yn.equals("Y") ? "batch" : CookieUtil.getCookieUserID(request);
		final CUDResult result = new CUDResult();

		try {
			cLog.startLog("순간이동 검색어 즉시적용 : ",  loginUser);
			final String host = SmartUXProperties.getProperty("cache.sync.public.host");
			final String port = SmartUXProperties.getProperty("cache.sync.public.port");

			String fileLockPath = SmartUXProperties.getProperty("Teleport.refreshTeleport.fileLock");
			int waitTime = NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.wait"), 5);

			FileProcessLockFactory.start(fileLockPath, waitTime,  new FileProcessFunction("순간이동 검색어 즉시적용", ResultCode.ApplyRequestFail) {

				@Override
				public void run() throws Exception {
					
					//즉시적용 호출 시 sleep 값 추가 -> DB분리건으로 OGG가 10초 이상 걸리는 경우가 있어 즉시적용 시 반영이 안돼서 추가함
					Thread.sleep(Integer.parseInt(org.apache.commons.lang.StringUtils.defaultIfEmpty(SmartUXProperties.getProperty("apply.cache.call.sleep"), "10000")));
					
					RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
							host,
							port,
							SmartUXProperties.getProperty("Teleport.refreshTeleport.url")+
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
					cLog.middleLog("순간이동 검색어 즉시적용", "API 호출", codeMsg.getCode(), codeMsg.getMessage());
				}
			});
		} catch (MimsCommonException e) {
			cLog.warnLog("순간이동 검색어 즉시적용", "실패", e.getFlag(), e.getMessage(), e.getLocalizedMessage());
			result.setFlag(e.getFlag());
			result.setMessage(e.getMessage());
		} finally {
			cLog.endLog("순간이동 검색어 즉시적용", loginUser, result.getFlag(), result.getMessage());
		}

		String resultMsg = "{\"flag\" : \"" + result.getFlag() + "\", \"message\" : \"" + result.getMessage() + "\"}";
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(resultMsg).toString(), responseHeaders, HttpStatus.OK);
	}



	/**
	 * 메뉴트리 화면
	 * @return String
	 * @throws Exception
	 */
	@SuppressWarnings("JavaDoc")
	@RequestMapping(value="/admin/teleport/getMenuTreeList",method=RequestMethod.GET)
	public String  getMenuTreeList(
			@RequestParam(value="panel_id", defaultValue="") String panel_id,
			Model model,
			@RequestParam(value = "callbak", required = false, defaultValue = "") String callbak
	) {

		try {
			HTMLCleaner cleaner = new HTMLCleaner();
			panel_id 	= cleaner.clean(panel_id);

			// 패널 목록을 먼저 조회한다
			List<PanelVO> panel_result = panelViewService.getPanelList();
			model.addAttribute("panel_result", panel_result);

			//Default SP02
			final String openPanelId = "SP02";
			if("".equals(GlobalCom.isNull(panel_id))){
				if(panel_result.size() != 0){
					panel_id = openPanelId;
				}
			}

			List<ViewVO> result = panelViewService.getPanelTitleTempList(panel_id);
			model.addAttribute("panel_id", panel_id);
			model.addAttribute("result", result);

			model.addAttribute("itemList", this.panelViewService.getPanelTitleTempList(openPanelId));
			model.addAttribute("openPanelId", openPanelId);
			model.addAttribute("callbak", callbak);
		} catch (Exception e) {
			logger.error("[getMenuTreeList]["+e.getClass().getName()+"]["+e.getMessage()+"]");
		}

		return "/admin/senior/getMenuTreeListPop";
	}
	@RequestMapping(value="/admin/teleport/list", method={RequestMethod.GET, RequestMethod.POST} )
	public String getTeleportList(
			Model model,
			@RequestParam(value="searchType", required=false, defaultValue="") String searchType,
			@RequestParam(value="searchText", required=false, defaultValue="") String searchText
			){

		try {
			searchType 	= HTMLCleaner.clean(searchType);
			searchText 	= HTMLCleaner.clean(searchText);
			model.addAttribute("list", this.service.getTeleportList(searchType, searchText));
			model.addAttribute("searchType", searchType);
			model.addAttribute("searchText", searchText);
		} catch (Exception e) {
			logger.error("[getTeleportList]["+e.getClass().getName()+"]["+e.getMessage()+"]");
		}

		return "/admin/senior/teleportList";
	}

	@RequestMapping(value="/admin/teleport/insert", method=RequestMethod.GET )
	public String getTeleportInsert(Model model){

		try {
			List list = this.service.getTeleportList(anchor_type.key(), String.valueOf(NATION.getType()));
			model.addAttribute("list", list);
			model.addAttribute("nationType", NATION.getType());
            model.addAttribute("typeList", Category.getList());
		} catch (Exception e) {
			logger.error("[getTeleportInsert]["+e.getClass().getName()+"]["+e.getMessage()+"]");
		}
		return "/admin/senior/teleportInsert";
	}
	@RequestMapping(value="/admin/teleport/update", method=RequestMethod.GET )
	public String getTeleportUpdate(Model model,
				@RequestParam(value="anchor_id", required=false, defaultValue="1") int anchor_id){

		try {
			List list = this.service.getTeleportList(SearchType.anchor_id.key(), String.valueOf(anchor_id));
			TeleportInfo teleportInfo = new TeleportInfo();
			if(CollectionUtils.isNotEmpty(list))  {
				teleportInfo = (TeleportInfo) list.get(0);
			}
			model.addAttribute("teleportInfo", teleportInfo);

			model.addAttribute("list",
					this.service.getTeleportList(anchor_type.key(), String.valueOf(NATION.getType())));
			model.addAttribute("nationType", NATION.getType());
			model.addAttribute("typeList", Category.getList());
		} catch (Exception e) {
			logger.error("[getTeleportUpdate]["+e.getClass().getName()+"]["+e.getMessage()+"]");
		}
		return "/admin/senior/teleportUpdate";
	}
    @RequestMapping(value="/admin/teleport/order/update", method=RequestMethod.GET )
    public String getTeleportOrderUpdate(Model model){

        try {
        	List list = this.service.getTeleportList(order_change.key(), "");
            model.addAttribute("list", list);
        } catch (Exception e) {
            logger.error("[getTeleportOrderUpdate]["+e.getClass().getName()+"]["+e.getMessage()+"]");
        }
        return "/admin/senior/teleportOrderUpdate";
    }

	@RequestMapping(value="/admin/teleport/insert", method=RequestMethod.POST )
	public @ResponseBody String setTeleportInsert(
			@RequestParam(value="anchor_type") int anchor_type,
			@RequestParam(value="panel_id", required=false, defaultValue="") String panel_id,
			@RequestParam(value="paper_code", required=false, defaultValue="") String paper_code,
            @RequestParam(value="paper_name", required=false, defaultValue="") String paper_name,
			@RequestParam(value="anchor_txt", required=false, defaultValue="") String anchor_txt,
			@RequestParam(value="parent_id", required=false, defaultValue="1") int parent_id,
			@RequestParam(value="parent_txt", required=false, defaultValue="") String parent_txt,
			@RequestParam(value="write_id", required=false, defaultValue="") String write_id
			){

		panel_id 	= HTMLCleaner.clean(panel_id);
		paper_code 	= HTMLCleaner.clean(paper_code);
        paper_name 	= HTMLCleaner.clean(paper_name);
		anchor_txt  = HTMLCleaner.clean(anchor_txt);
		parent_txt  = HTMLCleaner.clean(parent_txt);
		write_id 	= HTMLCleaner.clean(write_id);

		String resultcode;
		String resultmessage;

        try{
			TeleportInfo teleportInfo = new TeleportInfo();
			teleportInfo.setAnchor_type(anchor_type);
			teleportInfo.setPanel_id(panel_id);
			teleportInfo.setPaper_code(paper_code);
            teleportInfo.setPaper_name(paper_name);
			teleportInfo.setAnchor_txt(anchor_txt);
			teleportInfo.setParent_id(parent_id);
			teleportInfo.setParent_txt(parent_txt);
			teleportInfo.setWrite_id(write_id);

			logger.info("anchor_type : " + anchor_type);
			logger.info("panel_id : " + panel_id);
			logger.info("paper_code : " + paper_code);
			logger.info("paper_name : " + paper_name);
			logger.info("anchor_txt : " + anchor_txt);
			logger.info("parent_id : " + parent_id);
			logger.info("parent_txt : " + parent_txt);
			logger.info("write_id : " + write_id);


			resultcode = SmartUXProperties.getProperty("flag.success");
			resultmessage = SmartUXProperties.getProperty("message.success");

			String msg = this.service.insertTeleport(teleportInfo);
			if(StringUtils.isNotEmpty(msg)) {
				resultcode = SmartUXProperties.getProperty("flag.key1");
				resultmessage = SmartUXProperties.getProperty("message.key1");
			}
		}catch(Exception e){
			logger.error("[setTeleportInsert]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}

		return "{\"flag\" : \"" + resultcode + "\", \"message\" : \"" + resultmessage + "\"}";
	}
	@RequestMapping(value="/admin/teleport/update", method=RequestMethod.POST )
	public @ResponseBody String setTeleportUpdate(
			@RequestParam(value="order", required=false, defaultValue="1") int order,
			@RequestParam(value="anchor_id", required=false, defaultValue="1") int anchor_id,
			@RequestParam(value="anchor_type") int anchor_type,
			@RequestParam(value="panel_id", required=false, defaultValue="") String panel_id,
			@RequestParam(value="paper_code", required=false, defaultValue="") String paper_code,
			@RequestParam(value="paper_name", required=false, defaultValue="") String paper_name,
			@RequestParam(value="anchor_txt", required=false, defaultValue="") String anchor_txt,
			@RequestParam(value="parent_id", required=false, defaultValue="1") int parent_id,
			@RequestParam(value="parent_txt", required=false, defaultValue="") String parent_txt,
			@RequestParam(value="write_id", required=false, defaultValue="") String write_id
	){

		panel_id 	= HTMLCleaner.clean(panel_id);
		paper_code 	= HTMLCleaner.clean(paper_code);
		paper_name 	= HTMLCleaner.clean(paper_name);
		anchor_txt  = HTMLCleaner.clean(anchor_txt);
		parent_txt  = HTMLCleaner.clean(parent_txt);
		write_id 	= HTMLCleaner.clean(write_id);

		String resultcode;
		String resultmessage;

		try{
			TeleportInfo teleportInfo = new TeleportInfo();
			teleportInfo.setOrder(order);
			teleportInfo.setAnchor_id(anchor_id);
			teleportInfo.setAnchor_type(anchor_type);
			teleportInfo.setPanel_id(panel_id);
			teleportInfo.setPaper_code(paper_code);
			teleportInfo.setPaper_name(paper_name);
			teleportInfo.setAnchor_txt(anchor_txt);
			teleportInfo.setParent_id(parent_id);
			teleportInfo.setParent_txt(parent_txt);
			teleportInfo.setWrite_id(write_id);

			resultcode = SmartUXProperties.getProperty("flag.success");
			resultmessage = SmartUXProperties.getProperty("message.success");

			String msg = this.service.setTeleport(teleportInfo);
			if(StringUtils.isNotEmpty(msg)) {
				resultcode = SmartUXProperties.getProperty("flag.key1");
				resultmessage = SmartUXProperties.getProperty("message.key1");
			}
		}catch(Exception e){
			logger.error("[setTeleportUpdate]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}

		return "{\"flag\" : \"" + resultcode + "\", \"message\" : \"" + resultmessage + "\"}";
	}


	@RequestMapping(value="/admin/teleport/delete", method=RequestMethod.POST)
	public @ResponseBody String setTeleportDelete(@RequestParam(value="order[]") int[] orderList){

		String resultcode;
		String resultmessage;

		try{
			service.deleteList(orderList);

			resultcode = SmartUXProperties.getProperty("flag.success");
			resultmessage = SmartUXProperties.getProperty("message.success");
		}catch(Exception e){
			logger.error("[setTeleportDelete]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}

		return "{\"flag\" : \"" + resultcode + "\", \"message\" : \"" + resultmessage + "\"}";
	}

	@RequestMapping(value="/admin/teleport/order/update", method=RequestMethod.POST)
	public @ResponseBody String setTeleportOrderUpdate(@RequestParam(value="code[]") int[] codeList){

		String resultcode;
		String resultmessage;

		try{
			service.setTeleportOrder(codeList);

			resultcode = SmartUXProperties.getProperty("flag.success");
			resultmessage = SmartUXProperties.getProperty("message.success");
		}catch(Exception e){
			logger.error("[setTeleportDelete]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}

		return "{\"flag\" : \"" + resultcode + "\", \"message\" : \"" + resultmessage + "\"}";
	}

}
