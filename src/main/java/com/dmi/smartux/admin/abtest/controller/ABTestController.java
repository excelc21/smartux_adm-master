package com.dmi.smartux.admin.abtest.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.dmi.commons.consts.ResultCode;
import com.dmi.commons.exception.MimsCommonException;
import com.dmi.commons.factory.FileProcessFunction;
import com.dmi.commons.factory.FileProcessLockFactory;
import com.dmi.commons.logger.CLog;
import com.dmi.commons.model.RestfulCodeMsg;
import com.dmi.commons.utility.HttpClientUtils;
import com.dmi.commons.utility.RestfulUtils;
import com.dmi.smartux.admin.abtest.service.ABTestService;
import com.dmi.smartux.admin.abtest.vo.ABTestSearchVO;
import com.dmi.smartux.admin.abtest.vo.ABTestVO;
import com.dmi.smartux.admin.code.service.CodeService;
import com.dmi.smartux.admin.code.vo.CodeItemVO;
import com.dmi.smartux.admin.commonMng.service.CommonModuleService;
import com.dmi.smartux.admin.commonMng.vo.FlatRateVO;
import com.dmi.smartux.admin.mainpanel.service.PanelViewService;
import com.dmi.smartux.admin.mainpanel.vo.CategoryVO;
import com.dmi.smartux.admin.mainpanel.vo.ViewVO;
import com.dmi.smartux.admin.rank.service.EcrmRankService;
import com.dmi.smartux.admin.rank.vo.EcrmRankVO;
import com.dmi.smartux.admin.schedule.service.ScheduleService;
import com.dmi.smartux.admin.schedule.vo.ScheduleVO;
import com.dmi.smartux.common.exception.ExceptionHandler;
import com.dmi.smartux.common.exception.SmartUXException;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.CookieUtil;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.util.HTMLCleaner;
import com.dmi.smartux.common.vo.ResultVO;

import net.sf.json.JSONSerializer;

@Controller
public class ABTestController {
	
	private final Log logger = LogFactory.getLog(this.getClass());
	
	private final Log applyLogger = LogFactory.getLog("adminApplyCacheHistroy");
	
	@Autowired
	ABTestService service;
	@Autowired
	PanelViewService panelservice;
	@Autowired
	CodeService codeservice;
	@Autowired
	ScheduleService scheduleservice;
	@Autowired
	EcrmRankService ecrmrankservice;
	@Autowired
	CommonModuleService commService;
	@Autowired
	PanelViewService panelViewService;
	
	/**
	 * 2021.04.16 AB????????? MIMS
	 * AB????????? ?????? ??????
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/admin/abtest/getABTestList", method = RequestMethod.GET)
	public String getABTestList(
			@RequestParam(value = "findName", required = false, defaultValue = "") String findName,
			@RequestParam(value = "findValue", required = false, defaultValue = "") String findValue,
			@RequestParam(value = "pageNum", required = false, defaultValue = "1") String pageNum,
			@RequestParam(value = "offset", required = false, defaultValue = "-1") String offset,
			Model model) throws Exception {

		ABTestSearchVO searchVo = new ABTestSearchVO();
		
		//?????????
		searchVo.setPageSize(GlobalCom.isNumber(searchVo.getPageSize(), 10));
		searchVo.setBlockSize(GlobalCom.isNumber(searchVo.getBlockSize(), 10));
		searchVo.setPageNum(GlobalCom.isNumber(Integer.parseInt(GlobalCom.isNull(pageNum, "1")), 1));
		
		searchVo.setFindName(findName);
		searchVo.setFindValue(findValue);
		searchVo.setOffset(offset);
		
		List<ABTestVO> abmsList =null;
		int totalCount = 0;
		Map<String, Object> abmsMap = service.getABMSCall(searchVo);
		
		if(!CollectionUtils.isEmpty(abmsMap)){
			if("0".equals(abmsMap.get("resCode"))){
				
				abmsList = (List<ABTestVO>) abmsMap.get("abmsList");
				if(!CollectionUtils.isEmpty(abmsList)){
					//AMBS ?????? ????????? DB??? ??????
					abmsList = service.getABTestList(abmsList);	
				}
				totalCount= Integer.parseInt((String) abmsMap.get("totalCount"));
						
			}else{
				logger.warn("[getABTestList][ABMS ????????????][resCode: " + abmsMap.get("resCode") + "][resMsg: " + abmsMap.get("resMsg") + "]");
			}
		}
		
		searchVo.setPageCount(totalCount);
		model.addAttribute("list", abmsList);
		model.addAttribute("searchVo", searchVo);

		return "/admin/abtest/abtestList";
	}
	
	/**
	 * 2021.04.16 AB????????? MIMS
	 * AB????????? ?????? ?????? ?????? ?????? View
	 * @param findName
	 * @param findValue
	 * @param pageNum
	 * @param variation_id
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/abtest/setABTestMapping", method = RequestMethod.GET)
	public String setABTestMapping(
			@RequestParam(value = "findName", required = false, defaultValue = "") String findName,
			@RequestParam(value = "findValue", required = false, defaultValue = "") String findValue,
			@RequestParam(value = "pageNum", required = false, defaultValue = "1") String pageNum,
			@RequestParam(value = "test_id", required = false) String test_id,
			Model model) throws Exception {

		ABTestSearchVO searchVo = new ABTestSearchVO();
		
		//?????????
		searchVo.setPageSize(GlobalCom.isNumber(searchVo.getPageSize(), 10));
		searchVo.setBlockSize(GlobalCom.isNumber(searchVo.getBlockSize(), 10));
		searchVo.setPageNum(GlobalCom.isNumber(Integer.parseInt(GlobalCom.isNull(pageNum, "1")), 1));
		
		searchVo.setFindName(findName);
		searchVo.setFindValue(findValue);
		
		model.addAttribute("test_id", test_id);
		model.addAttribute("searchVo", searchVo);

		return "/admin/abtest/abtestMapping";
	}
	
	/**
	 * 2021.04.20 AB????????? MIMS
	 * ?????? ?????? ??????
	 * @param findName
	 * @param findValue
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/abtest/popPanelList", method = RequestMethod.GET)
	public String getPanelListPopup(
			@RequestParam(value = "findName", required = false, defaultValue = "") String findName,
			@RequestParam(value = "findValue", required = false, defaultValue = "") String findValue,
			@RequestParam(value = "callback", required = false, defaultValue = "") String callback,
			Model model) throws Exception {
		
		ABTestSearchVO searchVo = new ABTestSearchVO();
		searchVo.setFindName(findName);
		searchVo.setFindValue(findValue);
		
		List<HashMap<String,String>> panelList =service.getPanelList(searchVo);
		model.addAttribute("searchVo", searchVo);
		model.addAttribute("panelList", panelList);
		model.addAttribute("callback", callback);

		return "/admin/abtest/popPanelList";
	}
	
	/**
	 * 2021.04.20 AB????????? MIMS
	 * AB????????? ??????
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/abtest/insertABTest", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> insertABTest(
			@RequestParam(value = "org_mims_id", required = false, defaultValue = "") String org_mims_id,
			@RequestParam(value = "test_id", required = false, defaultValue = "") String test_id,
			@RequestParam(value = "mtype", required = false, defaultValue = "") String mtype,
			@RequestParam(value = "test_type", required = false, defaultValue = "") String test_type,
			Model model, HttpServletRequest request) throws Exception {
		
		String loginUser = CookieUtil.getCookieUserID(request);
		logger.info("[AB????????? ??????][ABTestController][insertABTest][org_mims_id: " + org_mims_id + "][test_id: " + test_id + "][mtype: " + mtype + "][loginUser: " + loginUser +"][test_type: "+ test_type+ "]");
		String resultCode = "";
		String resultMsg = "";
		try{
			service.insertABTest(org_mims_id, test_id, mtype, loginUser, test_type);
			resultCode = SmartUXProperties.getProperty("flag.success");
			resultMsg = SmartUXProperties.getProperty("message.success");
		}catch(Exception e){
			logger.info("[AB????????? ??????][ABTestController][insertABTest][ERROR][" + e +"]");
		}
		
		String result = "{\"flag\" : \"" + resultCode + "\", \"message\" : \"" + resultMsg + "\"}";

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(result, responseHeaders, HttpStatus.CREATED);
	}
	
	/**
	 * 2021.04.20 AB????????? MIMS
	 * AB????????? ?????? ??????
	 * @param findName
	 * @param findValue
	 * @param pageNum
	 * @param variation_id
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/abtest/getABTestPaperList", method = RequestMethod.GET)
	public String getABTestPaperList(
			@RequestParam(value = "findName", required = false, defaultValue = "") String findName,
			@RequestParam(value = "findValue", required = false, defaultValue = "") String findValue,
			@RequestParam(value = "pageNum", required = false, defaultValue = "1") String pageNum,
			@RequestParam(value = "test_id", required = false) String test_id,
			@RequestParam(value = "variation_id", required = false) String variation_id,
			@RequestParam(value = "mims_id", required = false) String mims_id,
			@RequestParam(value = "org_mims_id", required = false) String org_mims_id,
			Model model) throws Exception {

		boolean abmsUptFlag = true;
		
		if(StringUtils.isBlank(variation_id)){
			ABTestVO variationVo = service.getMinVariationId(test_id);
			variation_id = variationVo.getVariation_id();
		}
		
		//variation ??????
		ABTestVO variationVo = service.getVariationInfo(variation_id);
		String test_type = "";
		if(variationVo != null){
			mims_id=variationVo.getMims_id();
			test_type = variationVo.getTest_type();
		}else{
			mims_id="";
		}
		
		//AB ??????
		List<ViewVO> abPaperList = service.getABTestPaperList(mims_id);
		
		
		//ORG ??????
		List<ViewVO> orgPaperList = service.getABTestOrgPaperList(test_id, "O"+test_id);
		
		//ABMS ??????
		ABTestSearchVO searchVo = new ABTestSearchVO();
		//?????????
		searchVo.setBlockSize(GlobalCom.isNumber(searchVo.getBlockSize(), 10));
		//searchVo.setPageSize(GlobalCom.isNumber(searchVo.getPageSize(), 10));
		//searchVo.setPageNum(GlobalCom.isNumber(Integer.parseInt(GlobalCom.isNull(pageNum, "1")), 1));
		searchVo.setPageSize(0);
		searchVo.setPageNum(1);
		//?????????ID
		searchVo.setFindName("test_id");
		searchVo.setFindValue(test_id);
		Map<String, Object> abmsMap = service.getABMSCall(searchVo);
		
		if(!CollectionUtils.isEmpty(abmsMap)){
			if("0".equals(abmsMap.get("resCode"))){
				List<ABTestVO> abmsList = (List<ABTestVO>) abmsMap.get("abmsList");
				model.addAttribute("abmsList", abmsList);
				
				//?????? ????????? variation ?????? ??????
				if(!CollectionUtils.isEmpty(abmsList)){
					abmsUptFlag = service.checkABMSStatus(variation_id, abmsList);
				}
			}
		}
		
		model.addAttribute("abmsUptFlag", abmsUptFlag);
		model.addAttribute("abPaperList", abPaperList);
		model.addAttribute("orgPaperList", orgPaperList);
		model.addAttribute("test_id", test_id);
		model.addAttribute("variation_id", variation_id);
		model.addAttribute("org_mims_id", org_mims_id);
		model.addAttribute("d_mims_id", "");
		if (!CollectionUtils.isEmpty(orgPaperList)) {
			model.addAttribute("d_mims_id", orgPaperList.get(0).getPannel_id());
		}
		
		model.addAttribute("mims_id", mims_id);
		
		model.addAttribute("findName", findName);
		model.addAttribute("findValue", findValue);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("test_type", test_type);

		return "/admin/abtest/abtestPaperList";
	}
	
	/**
	 * 2021.04.27 AB????????? MIMS
	 * AB????????? ?????? ??????
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/abtest/updateABTestPaper", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> updateABTestPaper(
			@RequestParam(value = "new_panel_id", required = false, defaultValue = "") String new_panel_id,	
			@RequestParam(value = "mims_id", required = false, defaultValue = "") String mims_id,
			@RequestParam(value = "variation_id", required = false, defaultValue = "") String variation_id,
			Model model, HttpServletRequest request) throws Exception {
		
		String loginUser = CookieUtil.getCookieUserID(request);
		logger.info("[AB????????? ?????? ??????][ABTestController][updateABTestPaper][panel_id: " + new_panel_id + "][mims_id: " + mims_id + "][variation_id: " + variation_id + "][loginUser: " + loginUser +"]");
		String resultCode = SmartUXProperties.getProperty("flag.etc");
		String resultMsg = SmartUXProperties.getProperty("message.etc");
		
		try{
			service.updateABTestPaper(mims_id, loginUser, new_panel_id);
			resultCode = SmartUXProperties.getProperty("flag.success");
			resultMsg = SmartUXProperties.getProperty("message.success");
		}catch(Exception e){
			logger.info("[AB????????? ?????? ??????][ABTestController][updateABTestPaper][ERROR][" + e +"]");
		}
		
		String result = "{\"flag\" : \"" + resultCode + "\", \"message\" : \"" + resultMsg + "\"}";

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(result, responseHeaders, HttpStatus.CREATED);
	}
	
	/**
	 * ???????????? ????????? ??????
	 * @param panel_id
	 * @param p_title_id
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/abtest/popUpdatePaperOrder", method=RequestMethod.GET)
	public String getUpdatePaperOrderPopup(
			@RequestParam(value = "variation_id", required = false, defaultValue = "") String variation_id,
			@RequestParam(value = "mims_id", required = false, defaultValue = "") String mims_id,
			@RequestParam(value = "p_title_id", required = false, defaultValue = "") String p_title_id,
			Model model) throws Exception{
		
		List<ViewVO> result = service.getPaperOrder(mims_id, p_title_id);
		model.addAttribute("variation_id", variation_id);
		model.addAttribute("panel_id", mims_id);
		model.addAttribute("result", result);
		
		return "/admin/abtest/popUpdatePaperOrder";
	}
	
	/**
	 * ?????? ?????? ????????? ??????
	 * @param panel_id		????????? ???????????? ?????? ???????????? ?????? panel_id
	 * @param title_ids		????????? ???????????? ?????? ???????????? ?????? ?????? ?????? id
	 * @param login_id		????????? ???????????? ????????? ??? ????????? ????????? id
	 * @return				?????? ????????? ????????? json ?????????
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/abtest/updatePaperOrder", method=RequestMethod.POST)
	public ResponseEntity<String> updatePaperOrder(
			@RequestParam(value = "variation_id", required = false, defaultValue = "") String variation_id,
			@RequestParam(value = "panel_id", required = false, defaultValue = "") String panel_id,
			@RequestParam(value = "title_id[]", required = false, defaultValue = "") String [] title_ids,
			@RequestParam(value = "smartUXManager", required = false, defaultValue = "") String login_id
			) throws Exception{
		
		String update_id = login_id;
		
		String resultCode = "";
		String resultMsg = "";
		
		try{
			service.updatePaperOrder(panel_id, title_ids, update_id);
			resultCode = SmartUXProperties.getProperty("flag.success");
			resultMsg = SmartUXProperties.getProperty("message.success");
		}catch(Exception e){
			ExceptionHandler handler = new ExceptionHandler(e);
			resultCode = handler.getFlag();
			resultMsg = handler.getMessage();
		}
		
		String result = "{\"flag\" : \"" + resultCode + "\", \"message\" : \"" + resultMsg + "\"}";

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(result, responseHeaders, HttpStatus.CREATED);
	}
	
	/**
	 * ?????? ?????? ??????
	 * @param test_id
	 * @param variation_id
	 * @param mims_id
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/abtest/popInsertABTestPaper", method = RequestMethod.GET)
	public String getInsertABTestPaperPopup(
		@RequestParam(value = "test_id", required = false) String test_id,
		@RequestParam(value = "variation_id", required = false) String variation_id,
		@RequestParam(value = "mims_id", required = false) String mims_id,	//????????? ??????ID
		Model model) throws Exception {
		
		ABTestSearchVO searchVO = new ABTestSearchVO();
		//?????? ??????
		List<HashMap<String, String>> panelList = service.getPanelList(searchVO);
		
		model.addAttribute("panelList", panelList);
		model.addAttribute("test_id", test_id);
		model.addAttribute("variation_id", variation_id);
		model.addAttribute("mims_id", mims_id);

		return "/admin/abtest/popInsertABTestPaper";
	}
	
	/**
	 * 2021.05.03 AB????????? MIMS
	 * ?????? (???????????? ??????)
	 * @param test_id
	 * @param variation_id
	 * @param mims_id
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/abtest/getPaperList", method = RequestMethod.POST)
	public ResponseEntity<String> getPaperList(
		@RequestParam(value = "mims_id", required = false) String mims_id,	//????????? ??????ID
		@RequestParam(value = "sel_panel_id", required = false) String sel_panel_id,	//????????? ??????
		@RequestParam(value = "findName", required = false, defaultValue = "") String findName,	
		@RequestParam(value = "findValue", required = false, defaultValue = "") String findValue,
		Model model) throws Exception {
		
		ABTestSearchVO searchVO = new ABTestSearchVO();
		searchVO.setFindName(findName);
		searchVO.setFindValue(findValue);
		searchVO.setPanel_id(sel_panel_id);
		
		//??????
		List<ViewVO> paperList = service.getPaperList(searchVO);

		ObjectMapper mapper = new ObjectMapper();
		String jsonList = mapper.writeValueAsString(paperList);

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		return new ResponseEntity<String>(jsonList, responseHeaders, HttpStatus.CREATED);
	}
	
	/**
	 * ???????????? ??????
	 * @param mims_id
	 * @param sel_panel_id
	 * @param findName
	 * @param findValue
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/abtest/insertABTestPaper", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> insertABTestPaper(
			@RequestParam(value = "mims_id", required = false) String mims_id,
			@RequestParam(value = "org_title_id", required = false) String org_title_id,
			@RequestParam(value = "org_panel_id", required = false) String org_panel_id,
			HttpServletRequest request) throws Exception{

		String loginUser = CookieUtil.getCookieUserID(request);
		logger.info("[AB????????? ?????? ??????][ABTestController][insertABTestPaper][mims_id: " + mims_id + "][loginUser: " + loginUser +"][org_title_id: " + org_title_id +"][org_panel_id: " + org_panel_id +"]");
		String resultCode = SmartUXProperties.getProperty("flag.etc");
		String resultMsg = SmartUXProperties.getProperty("message.etc");
		
		try{
			ViewVO vo = new ViewVO();
			vo.setPannel_id(mims_id);
			vo.setTitle_id(org_title_id);
			
			int count = service.selectPaperCount(vo);
			
			if(count == 0){
				service.insertABTestPaper(mims_id, org_title_id, org_panel_id, loginUser);
				logger.info("[AB????????? ?????? ??????][ABTestController][insertABTestPaper][?????? ?????? ??????][mims_id: " + mims_id + "][loginUser: " + loginUser +"][org_title_id: " + org_title_id +"][org_panel_id: " + org_panel_id +"]");
				resultCode = SmartUXProperties.getProperty("flag.success");
				resultMsg = SmartUXProperties.getProperty("message.success");
			}else{
				logger.info("[AB????????? ?????? ??????][ABTestController][insertABTestPaper][?????? ?????? ??????][?????? ?????? ??? ??????][mims_id: " + mims_id + "][loginUser: " + loginUser +"][org_title_id: " + org_title_id +"][org_panel_id: " + org_panel_id +"]");
				resultMsg +="[?????? ????????? ??????]";
			}
			
		}catch(Exception e){
			logger.warn("[AB????????? ?????? ??????][ABTestController][insertABTestPaper][?????? ?????? ??????][" + e + "]");
			ExceptionHandler handler = new ExceptionHandler(e);
			resultCode = handler.getFlag();
			resultMsg = handler.getMessage();
		}
		
		HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
        return new ResponseEntity<String>("{\"flag\" : \"" + resultCode + "\", \"message\" : \"" + resultMsg + "\"}", responseHeaders, HttpStatus.CREATED);
	}
	
	/**
	 * 2021.05.03 AB????????? MIMS
	 * AB????????? ?????? ??????
	 * @param mims_id
	 * @param org_paper_id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/abtest/deleteABTestPaper", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> deleteABTestPaper(
			@RequestParam(value="title_id_arr_str", required = false) String title_id_arr_str,
			@RequestParam(value="mims_id", required = false) String mims_id,
			HttpServletRequest request) throws Exception{

		String loginUser = CookieUtil.getCookieUserID(request);
		logger.info("[AB????????? ?????? ??????][ABTestController][deleteABTestPaper][mims_id: " + mims_id + "][loginUser: " + loginUser +"]");
		String resultCode = SmartUXProperties.getProperty("flag.etc");
		String resultMsg = SmartUXProperties.getProperty("message.etc");
		
		String [] title_id_arr =  title_id_arr_str.split("\\|");
		
		try{
			if(title_id_arr == null || title_id_arr.length==0){
				logger.info("[AB????????? ?????? ??????][ABTestController][deleteABTestPaper][?????? ?????? ??????][mims_id: " + mims_id + "][loginUser: " + loginUser +"]");
				resultMsg+="[????????? ?????? ??????]";
			}else{
				service.deleteABTestPaper(title_id_arr, mims_id);
				logger.info("[AB????????? ?????? ??????][ABTestController][deleteABTestPaper][?????? ?????? ??????][mims_id: " + mims_id + "][loginUser: " + loginUser +"]");
				resultCode = SmartUXProperties.getProperty("flag.success");
				resultMsg = SmartUXProperties.getProperty("message.success");
			}
		}catch(Exception e){
			logger.warn("[AB????????? ?????? ??????][ABTestController][deleteABTestPaper][?????? ?????? ??????][" + e + "]");
			ExceptionHandler handler = new ExceptionHandler(e);
			resultCode = handler.getFlag();
			resultMsg = handler.getMessage();
		}
		HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
        return new ResponseEntity<String>("{\"flag\" : \"" + resultCode + "\", \"message\" : \"" + resultMsg + "\"}", responseHeaders, HttpStatus.CREATED);
	}
	
	/**
	 * ?????? ?????? ?????? ??????
	 * @param panel_id		???????????? ????????? ?????? ????????? ?????? panel_id
	 * @param title_id		???????????? ????????? ?????? ????????? title_id
	 * @param model			Model ??????
	 * @return				?????? ?????? ?????? ?????? URL
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/abtest/popUpdatePaperInfo")
	public String getPaperInfoPopup(
			@RequestParam(value="variation_id") String variation_id,
			@RequestParam(value="test_type") String test_type, 
			@RequestParam(value="panel_id") String panel_id, 
			@RequestParam(value="title_id") String title_id,
			@RequestParam(value="abmsUptFlag", required=false) String abmsUptFlag,
			Model model) throws Exception{
		
		panel_id 	= HTMLCleaner.clean(panel_id);
		title_id 	= HTMLCleaner.clean(title_id);
		variation_id = HTMLCleaner.clean(variation_id);
		abmsUptFlag 	= HTMLCleaner.clean(abmsUptFlag);
		test_type = HTMLCleaner.clean(test_type);
		
		ViewVO result = service.getPaperInfo(panel_id, title_id);
		ViewVO p_result = service.getPaperInfo(panel_id, result.getP_title_id());
		
		model.addAttribute("p_result", p_result);
		model.addAttribute("result", result);
		model.addAttribute("variation_id", variation_id);
		model.addAttribute("abmsUptFlag", abmsUptFlag);
		model.addAttribute("test_type", test_type);

		return "/admin/abtest/popUpdatePaperInfo";
	}
	
	/**
	 * ?????? ?????? ??????
	 * @param panel_id		??????????????? ?????? ????????? ?????? panel_id
	 * @param p_title_id	??????????????? ?????? ????????? ?????? ?????? ?????? id
	 * @param title_id		??????????????? ?????? ????????? title_id
	 * @param title_nm		??????????????? ?????? ????????? ?????? ?????? ??????
	 * @param newTitle_nm	??????????????? ?????? ????????? ????????? ?????? ??????
	 * @param use_yn		????????? ????????????(Y/N)
	 * @param login_id		????????? ???????????? ????????? ??? ????????? ????????? id
	 * @return
	 */
	@RequestMapping(value="/admin/abtest/updatePaperInfo", method=RequestMethod.POST)
	public ResponseEntity<String> updatePaperInfo(
			@RequestParam(value="variation_id") String variation_id,
			@RequestParam(value="panel_id") String panel_id,
			@RequestParam(value="p_title_id") String p_title_id,
			@RequestParam(value="title_id") String title_id,
			@RequestParam(value="title_nm") String title_nm,
			@RequestParam(value="title_color") String title_color,
			@RequestParam(value="newTitle_nm") String newTitle_nm,
			@RequestParam(value="use_yn") String use_yn,
			@RequestParam(value="smartUXManager") String login_id,
			@RequestParam(value="title_bg_img_file_nm") String title_bg_img_file_nm,
			@RequestParam(value="bg_img_file_nm") String bg_img_file_nm,
			@RequestParam(value="bg_img_file2_nm") String bg_img_file2_nm,
			@RequestParam(value="bg_img_file3_nm") String bg_img_file3_nm,
			//@RequestParam(value="badge_img_file_nm") String bg_img_file4_nm,
			@RequestParam(value="title_bg_img_file", required=false) MultipartFile uploadfile,
			@RequestParam(value="bg_img_file", required=false) MultipartFile uploadfile1,
			@RequestParam(value="bg_img_file2", required=false) MultipartFile uploadfile2, 
			@RequestParam(value="bg_img_file3", required=false) MultipartFile uploadfile3,
			@RequestParam(value="abtest_yn") String abtest_yn
			//@RequestParam(value="badge_img_file", required=false) MultipartFile uploadfile4
			) throws Exception{
		
		panel_id 	= HTMLCleaner.clean(panel_id);
		p_title_id 	= HTMLCleaner.clean(p_title_id);
		title_id 	= HTMLCleaner.clean(title_id);
		title_nm 	= HTMLCleaner.clean(title_nm);
		title_color 	= HTMLCleaner.clean(title_color);
		newTitle_nm 	= HTMLCleaner.clean(newTitle_nm);
		use_yn 	= HTMLCleaner.clean(use_yn);
		login_id 	= HTMLCleaner.clean(login_id);
		
		ObjectMapper mapper = new ObjectMapper();
		String update_id = login_id;			// ????????? ID(????????? ????????? ??? ????????? ????????? ID??? ?????????
		ResultVO result = new ResultVO();
		
		try{
			String tmp_file_name = "";
			String tmp_file_name1 = "";
			String tmp_file_name2 = "";
			String tmp_file_name3 = "";
			String tmp_file_name4 = "";
			String title_bg_img_file = "";
			String bg_img_file = "";
			String bg_img_file2 = "";
			String bg_img_file3 = "";
			String bg_img_file4 = "";
			File img_file = null;
			File img_file1 = null;
			File img_file2 = null;
			File img_file3 = null;
			File img_file4 = null;
			
			if(uploadfile != null && uploadfile.getSize() != 0L){
				checkFileSize(uploadfile, 1);
				tmp_file_name = uploadfile.getOriginalFilename();
				String ext = tmp_file_name.substring(tmp_file_name.lastIndexOf(".")+1,tmp_file_name.length()); // ????????? ?????????
				title_bg_img_file = "panel/" + panel_id + "_" + Long.toString(System.currentTimeMillis()) + "." + ext; // ?????? ????????? ??????????????? ????????????(panel/) ?????? id_?????? id_???????????????.????????? ????????? ??????
				
				String newFilename = SmartUXProperties.getProperty("imgupload.dir") + "/" + title_bg_img_file;
				
				logger.debug("tmp_file_name : " + tmp_file_name);
				logger.debug("ext :  " + ext);
				logger.debug("title_bg_img_file :  " + title_bg_img_file);
				logger.debug("newFilename :  " + newFilename);
				img_file = new File(newFilename);
				uploadfile.transferTo(img_file);
			}
			
			if(uploadfile1 != null && uploadfile1.getSize() != 0L){
				checkFileSize(uploadfile1, 2);
				tmp_file_name1 = uploadfile1.getOriginalFilename();
				String ext = tmp_file_name1.substring(tmp_file_name1.lastIndexOf(".")+1,tmp_file_name1.length()); // ????????? ?????????
				bg_img_file = "panel/" + panel_id + "_" + Long.toString(System.currentTimeMillis()+1) + "." + ext; // ?????? ????????? ??????????????? ????????????(panel/) ?????? id_?????? id_???????????????.????????? ????????? ??????
				
				String newFilename = SmartUXProperties.getProperty("imgupload.dir") + "/" + bg_img_file;
				
				logger.debug("tmp_file_name1 : " + tmp_file_name1);
				logger.debug("ext :  " + ext);
				logger.debug("bg_img_file :  " + bg_img_file);
				logger.debug("newFilename :  " + newFilename);
				img_file1 = new File(newFilename);
				uploadfile1.transferTo(img_file1);
			}
			
			if(uploadfile2 != null && uploadfile2.getSize() != 0L){
				checkFileSize(uploadfile2, 3);
				tmp_file_name2 = uploadfile2.getOriginalFilename();
				String ext = tmp_file_name2.substring(tmp_file_name2.lastIndexOf(".")+1,tmp_file_name2.length()); // ????????? ?????????
				bg_img_file2 = "panel/" + panel_id + "_" + Long.toString(System.currentTimeMillis()+2) + "." + ext; // ?????? ????????? ??????????????? ????????????(panel/) ?????? id_?????? id_???????????????.????????? ????????? ??????
				
				String newFilename = SmartUXProperties.getProperty("imgupload.dir") + "/" + bg_img_file2;
				
				logger.debug("tmp_file_name2 : " + tmp_file_name2);
				logger.debug("ext :  " + ext);
				logger.debug("bg_img_file2 :  " + bg_img_file2);
				logger.debug("newFilename :  " + newFilename);
				img_file2 = new File(newFilename);
				uploadfile2.transferTo(img_file2);
			}
			
			if(uploadfile3 != null && uploadfile3.getSize() != 0L){
				checkFileSize(uploadfile3, 4);
				tmp_file_name3 = uploadfile3.getOriginalFilename();
				String ext = tmp_file_name3.substring(tmp_file_name3.lastIndexOf(".")+1,tmp_file_name3.length()); // ????????? ?????????
				bg_img_file3 = "panel/" + panel_id + "_" + Long.toString(System.currentTimeMillis()+2) + "." + ext; // ?????? ????????? ??????????????? ????????????(panel/) ?????? id_?????? id_???????????????.????????? ????????? ??????
				
				String newFilename = SmartUXProperties.getProperty("imgupload.dir") + "/" + bg_img_file3;
				
				logger.debug("tmp_file_name3 : " + tmp_file_name3);
				logger.debug("ext :  " + ext);
				logger.debug("bg_img_file3 :  " + bg_img_file3);
				logger.debug("newFilename :  " + newFilename);
				img_file3 = new File(newFilename);
				uploadfile3.transferTo(img_file3);
			}
			
//			if(uploadfile4 != null && uploadfile4.getSize() != 0L){
//				checkFileSize(uploadfile4, 5);
//				tmp_file_name4 = uploadfile4.getOriginalFilename();
//				String ext = tmp_file_name4.substring(tmp_file_name4.lastIndexOf(".")+1,tmp_file_name4.length()); // ????????? ?????????
//				bg_img_file4 = "panel/" + panel_id + "_" + Long.toString(System.currentTimeMillis()+2) + "." + ext; // ?????? ????????? ??????????????? ????????????(panel/) ?????? id_?????? id_???????????????.????????? ????????? ??????
//				
//				String newFilename = SmartUXProperties.getProperty("imgupload.dir") + "/" + bg_img_file4;
//				
//				logger.debug("tmp_file_name4 : " + tmp_file_name4);
//				logger.debug("ext :  " + ext);
//				logger.debug("bg_img_file4 :  " + bg_img_file4);
//				logger.debug("newFilename :  " + newFilename);
//				img_file4 = new File(newFilename);
//				uploadfile4.transferTo(img_file4);
//			}
			
			if("".equals(GlobalCom.isNull(title_bg_img_file))){
				title_bg_img_file = GlobalCom.isNull(title_bg_img_file_nm);
			}
			if("".equals(GlobalCom.isNull(bg_img_file))){
				bg_img_file = GlobalCom.isNull(bg_img_file_nm);
			}
			if("".equals(GlobalCom.isNull(bg_img_file2))){
				bg_img_file2 = GlobalCom.isNull(bg_img_file2_nm);
			}
			if("".equals(GlobalCom.isNull(bg_img_file3))){
				bg_img_file3 = GlobalCom.isNull(bg_img_file3_nm);
			}
//			if("".equals(GlobalCom.isNull(bg_img_file4))){
//				bg_img_file4 = GlobalCom.isNull(bg_img_file4_nm);
//			}
			
			if(title_nm.equals(newTitle_nm)){															// ?????? ???????????? ?????? ???????????? ?????? ????????? ??????????????? ???????????? ???????????? ?????? ??????????????? ??????
				service.updatePaperInfo(variation_id, panel_id, title_id, newTitle_nm, title_color, use_yn, update_id, title_bg_img_file, bg_img_file, bg_img_file2 , bg_img_file3 , bg_img_file4, abtest_yn);
				result.setFlag(SmartUXProperties.getProperty("flag.success"));
				result.setMessage(SmartUXProperties.getProperty("message.success"));
			}else{																						// ?????? ???????????? ?????? ???????????? ?????? ????????? ????????? ?????? ????????? ?????? ??????
				// ?????? ?????? ?????? ID ????????? ?????? ???????????? ???????????? ??????
				int count = service.getPaperInfoTitleNmCnt(panel_id, p_title_id, newTitle_nm);				// ???????????? ?????? ???????????? ????????? ?????????????????? ??????
				
				if(count == 0){																			// ???????????? ?????? ???????????? ????????? ???????????? ?????????
					service.updatePaperInfo(variation_id, panel_id, title_id, newTitle_nm, title_color, use_yn, update_id, title_bg_img_file, bg_img_file, bg_img_file2 , bg_img_file3 , bg_img_file4, abtest_yn);
					result.setFlag(SmartUXProperties.getProperty("flag.success"));
					result.setMessage(SmartUXProperties.getProperty("message.success"));
				}else{																					// ???????????? ?????? ???????????? ????????? ????????????
					SmartUXException e = new SmartUXException();
					e.setFlag("EXISTS NEW_TITLE_NM");
					e.setMessage("????????? ??????");
					throw e;
				}
			}
			
		}catch(Exception e){
			ExceptionHandler handler = new ExceptionHandler(e);
			result.setFlag(handler.getFlag());
			result.setMessage(handler.getMessage());
		}
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/plain; charset=UTF-8");
		return new ResponseEntity<String>(mapper.writeValueAsString(result), responseHeaders, HttpStatus.OK);
	}
	
	/**
     * ?????? ????????? ??????
     * 1 : ?????? ?????????, 2 : ?????? ?????????1, 3 : ?????? ?????????2
     * @param text_file
     * @param type
     */
	private void checkFileSize(MultipartFile text_file, int type) {
		String fileMaxSize = SmartUXProperties.getProperty("panel.paper.bg.imagefile.size");
		String imgName = "?????? ?????????";
		if(2==type) {
			fileMaxSize = SmartUXProperties.getProperty("panel.paper.icon1.imagefile.size");
			imgName = "?????? ?????????1";
		}else if(3==type) {
			fileMaxSize = SmartUXProperties.getProperty("panel.paper.icon2.imagefile.size");
			imgName = "?????? ?????????2";
		}else if(4==type) {
			fileMaxSize = SmartUXProperties.getProperty("panel.paper.icon3.imagefile.size");
			imgName = "?????? ?????????3";
		}else if(5==type) {
			fileMaxSize = SmartUXProperties.getProperty("panel.paper.badge.imagefile.size");
			imgName = "?????? ?????????";
		}
		
		if(null != fileMaxSize) {
			long iFileMaxSize = Long.parseLong(fileMaxSize);
			if(text_file.getSize() > iFileMaxSize) throw new SmartUXException(SmartUXProperties.getProperty("flag.file.maxsize.over")
					, SmartUXProperties.getProperty("message.file.maxsize.over",imgName + " : " + fileMaxSize + "Byte ??????"));
		}
	}
	
	/**
	 * ?????? ????????? ?????? ?????? ??????
	 * @param panel_id				?????? ????????? ?????? ????????? ?????? ?????? ????????? ?????? panel_id
	 * @param title_id				?????? ????????? ?????? ????????? ?????? ?????? ????????? ?????? title_id
	 * @param category_id			????????? ?????? ????????? ????????? ?????? ?????? ????????? ????????? ???????????? ?????? category_id
	 * @param category_type			????????? ?????? ????????? ????????? ?????? ?????? ????????? ????????? ???????????? ?????? category_type(LIVE, VOD, CAT_MAP, SCHEDULE, WISH)
	 * @param album_cnt				????????? ?????? ????????? ????????? ?????? ?????? ????????? ????????? ???????????? ?????? album_cnt(??? ????????? category_type??? CAT_MAP ??? ???????????? ?????? ????????? ??????)
	 * @param model					Model ??????
	 * @return						?????? ????????? ?????? ?????? ?????? URL
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/abtest/popUpdatePaperDataType", method=RequestMethod.GET)
	public String getPaperDataTypePopUp(
			@RequestParam(value="variation_id") String variation_id
			,@RequestParam(value="test_type") String test_type
			,@RequestParam(value="panel_id") String panel_id
			, @RequestParam(value="title_id") String title_id
			, @RequestParam(value="category_id") String category_id
			, @RequestParam(value="category_type", required=false, defaultValue="") String category_type
			, @RequestParam(value="album_cnt", required=false, defaultValue="") String album_cnt
			, @RequestParam(value="category_gb", required=false) String category_gb
			, @RequestParam(value="abmsUptFlag", required=false) String abmsUptFlag
			, @RequestParam(value="scr_tp" , required=false, defaultValue="T") String scr_tp
			, Model model) throws Exception{
		
		variation_id 	= HTMLCleaner.clean(variation_id);
		panel_id 	= HTMLCleaner.clean(panel_id);
		title_id 	= HTMLCleaner.clean(title_id);
		category_id 	= HTMLCleaner.clean(category_id);
		category_type 	= HTMLCleaner.clean(category_type);
		album_cnt 	= HTMLCleaner.clean(album_cnt);
		abmsUptFlag 	= HTMLCleaner.clean(abmsUptFlag);
		test_type = HTMLCleaner.clean(test_type);
		
		logger.debug("category_id : " + category_id);
		logger.debug("category_type : " + category_type);
		
		// panel_id??? title_id ?????? ???????????? UI_TYPE ????????? DESCRIPTION ?????? ????????????
		ViewVO mainresult = service.getPaperDataType(panel_id, title_id);
		
		String isUpdate = "N";
		if(!("".equals(mainresult.getPage_type()))){				// ui_type ????????? ?????? ????????? ?????? ?????????????????? ????????????(?????? ???????????? ???????????? ????????? ?????? ????????? ????????? ?????? validation ?????? ??????)
			isUpdate = "Y";
		}
		
		List<CodeItemVO> coderesult = codeservice.getCodeItemList("A0008");
		// ???????????? ??????
		List<EcrmRankVO> ecrmresult = ecrmrankservice.getRankList("I20", "N");
		List<EcrmRankVO> ecrmresult_i30 = ecrmrankservice.getRankList("I30", "N");
		
		List<EcrmRankVO> ecrmresult_cat = ecrmrankservice.getRankList("I20", "Y");
		List<EcrmRankVO> ecrmresult_cat_i30 = ecrmrankservice.getRankList("I30", "Y");


		String hdtvPanelID = SmartUXProperties.getProperty("hdtv.panel.id");

		List<CategoryVO> categoryresult;
		List<CategoryVO> categoryresult_i30 = null;

		if (GlobalCom.contains(hdtvPanelID, panel_id, "\\|")) {
			// ????????? ???????????? ?????? ??????
			categoryresult = panelservice.getCategoryList("NSC", "VC");
		} else {
			// ????????? ???????????? ?????? ??????
			categoryresult = panelservice.getCategoryList("VC");
			categoryresult_i30 = panelservice.getCategoryList("I30", "VC");
		}

		// ???????????? ??????
		List<ScheduleVO> scheduleresult = scheduleservice.getScheduleList("I20");
		List<ScheduleVO> scheduleresult_i30 = scheduleservice.getScheduleList("I30");
		
		// category_type ?????? ????????? ?????? ?????? ????????? ????????????
		if("VOD".equals(category_type)){
			
		}else if("CAT_MAP".equals(category_type) || "BOOKTV".equals(category_type) || "ENG_PRESCH".equals(category_type) || "BRAND".equals(category_type)){
			// || ??? ???????????? ?????? ???????????????
			String [] category_ids = category_id.split("\\|\\|");
			String [] album_cnts = album_cnt.split("\\|\\|");
			List<CategoryVO> userCategoryresult = new ArrayList<CategoryVO>();
			
			int idx = 0;
			for(String item : category_ids){
				CategoryVO obj;

				if (hdtvPanelID.contains(panel_id)) {
					obj = panelservice.getCategoryIdName("NSC", item);
				} else {
					obj = panelservice.getCategoryIdName(category_gb, item);
				}

				if(obj != null){
					obj.setAlbum_cnt(album_cnts[idx++]);
					userCategoryresult.add(obj);
				}
			}
			model.addAttribute("userCategoryresult", userCategoryresult);
		}else if("SCHEDULE".equals(category_type)){
			
		}
		
		String cateTypeList = SmartUXProperties.getProperty("category.type.code");
		
		String reps_album_name = panelservice.getAlbumName(mainresult.getReps_album_id());
		mainresult.setReps_album_name(reps_album_name);
		
		
		//???????????? ??????
		List<CodeItemVO> apptyperesult = new ArrayList<CodeItemVO>();
		String apptype = SmartUXProperties.getProperty("paneltitle.apptype");
		String[] apptype_arr = apptype.split("\\|");
		for(int i = 0 ; i < apptype_arr.length; i++) {
			String apptypeunit = apptype_arr[i];
			if(apptypeunit.indexOf("^") > -1) {
				String[] apptypeunit_arr = apptypeunit.split("\\^");
				CodeItemVO vo = new CodeItemVO();
				vo.setItem_code(apptypeunit_arr[0]);
				vo.setItem_nm(apptypeunit_arr[1]);
				apptyperesult.add(vo);
			}
		}
		
		//20180912 ????????? ???????????? ??????
		if(!GlobalCom.isEmpty(mainresult.getProduct_code())) {
			FlatRateVO flatVO = new FlatRateVO();
			flatVO.setProductID(SmartUXProperties.getProperty("smartux.product.code"));
			flatVO.setProductName(SmartUXProperties.getProperty("smartux.product.name"));
			
			List<FlatRateVO> flatRateList = commService.getFlatRateList2();
			flatRateList.add(0, flatVO);
			
			String[] dataArr = mainresult.getProduct_code().split("\\|");
			
			List<FlatRateVO> productCodeList = new ArrayList<FlatRateVO>();
			for(int i =0; i<dataArr.length;i++){
				boolean find = false;
				for(FlatRateVO flatRateVO : flatRateList){
					//????????? ????????? ??????????????? ????????? ??????
					if(flatRateVO.getProductID().equals(dataArr[i])){
						flatRateVO.setProductCodeUse(true);
						productCodeList.add(flatRateVO);
						find = true;
						break;
					}
				}
				if(find) {
					continue;
				}
				FlatRateVO noUseFlatRate = new FlatRateVO();
				noUseFlatRate.setProductID(dataArr[i]);
				noUseFlatRate.setProductName("???????????? ?????? ??????");
				noUseFlatRate.setProductCodeUse(false);
				productCodeList.add(noUseFlatRate);
			}
			mainresult.setProduct_code_list(productCodeList);
		}
		
		//20180912 ????????? ???????????? ??????(?????????)
		if(!GlobalCom.isEmpty(mainresult.getProduct_code_not())) {
			FlatRateVO flatVO = new FlatRateVO();
			flatVO.setProductID(SmartUXProperties.getProperty("smartux.product.code"));
			flatVO.setProductName(SmartUXProperties.getProperty("smartux.product.name"));
			
			List<FlatRateVO> flatRateList = commService.getFlatRateList2();
			flatRateList.add(0, flatVO);
			
			String[] dataArr = mainresult.getProduct_code_not().split("\\|");
			
			List<FlatRateVO> productCodeList = new ArrayList<FlatRateVO>();
			for(int i =0; i<dataArr.length;i++){
				boolean find = false;
				for(FlatRateVO flatRateVO : flatRateList){
					//????????? ????????? ??????????????? ????????? ??????
					if(flatRateVO.getProductID().equals(dataArr[i])){
						flatRateVO.setProductCodeUse(true);
						productCodeList.add(flatRateVO);
						find = true;
						break;
					}
				}
				if(find) {
					continue;
				}
				FlatRateVO noUseFlatRate = new FlatRateVO();
				noUseFlatRate.setProductID(dataArr[i]);
				noUseFlatRate.setProductName("???????????? ?????? ??????");
				noUseFlatRate.setProductCodeUse(false);
				productCodeList.add(noUseFlatRate);
			}
			mainresult.setProduct_code_list_not(productCodeList);
		}
		
		
		List<String> terminal_list = panelViewService.getPaperTerminal(panel_id+"_"+title_id);
		String terminal_str = "";
		for(String terminal : terminal_list) {
			if("".equals(terminal_str)) {
				terminal_str = terminal;
			}else {
				terminal_str += ","+terminal;
			}
		}
		mainresult.setTerminal_arr(terminal_str);
		model.addAttribute("mainresult", mainresult);
		model.addAttribute("isUpdate", isUpdate);
		model.addAttribute("ecrmresult", ecrmresult);
		model.addAttribute("ecrmresult_i30", ecrmresult_i30);
		model.addAttribute("ecrmresult_cat", ecrmresult_cat);
		model.addAttribute("ecrmresult_cat_i30", ecrmresult_cat_i30);
		model.addAttribute("coderesult", coderesult);
		model.addAttribute("categoryresult", categoryresult);
		model.addAttribute("categoryresult_i30", categoryresult_i30);
		model.addAttribute("scheduleresult", scheduleresult);
		model.addAttribute("scheduleresult_i30", scheduleresult_i30);
		model.addAttribute("category_id", category_id);
		model.addAttribute("category_type", category_type);
		model.addAttribute("category_gb", category_gb);
		model.addAttribute("panel_id", panel_id);
		model.addAttribute("title_id", title_id);
		model.addAttribute("variation_id", variation_id);
		model.addAttribute("cateTypeList", cateTypeList);
		model.addAttribute("abmsUptFlag", abmsUptFlag);
		model.addAttribute("test_type", test_type);
		model.addAttribute("scr_tp", scr_tp);
		model.addAttribute("apptyperesult", apptyperesult);

		return "/admin/abtest/popUpdatePaperDataType";
	}
	
	/**
	 * ?????? ????????? ?????? ??????(jQuery??? ajaxForm?????? ?????? ????????? ????????? ????????? ??????????????? ????????? ??????????????? ?????? HttpServletRequest ???????????? ??? ???????????? ?????? ????????? ????????? ????????????)
	 * @param request			HttpServletRequest ??????
	 * @param response			HttpServletResponse ??????
	 * @param uploadfile		??????????????? ????????? ??????
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/abtest/updatePaperDataType", method=RequestMethod.POST)
	public ResponseEntity<String> updatePaperDataType(HttpServletRequest request
			, HttpServletResponse response
			, @RequestParam(value="bg_img_file", required=false) MultipartFile uploadfile
			, @RequestParam(value="bg_img_file2", required=false) MultipartFile uploadfile2
			) throws Exception{
		
		logger.debug("contentType : " + request.getContentType());
		if(request instanceof MultipartHttpServletRequest){
			logger.debug("MultipartHttpServletRequest OK");
		}else{
			logger.debug("MultipartHttpServletRequest NOT OK");
		}
		
		// ????????? ????????? ?????? ????????? ????????? ????????? request ????????? MultipartHttpServletRequest??? ????????? ??? ????????? ??????
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		 
		String variation_id = GlobalCom.isNull(multipartRequest.getParameter("variation_id"));				// ?????? ????????? ?????? ????????? ?????? ?????? ????????? ?????? variation_id
		String panel_id = GlobalCom.isNull(multipartRequest.getParameter("panel_id"));				// ?????? ????????? ?????? ????????? ?????? ?????? ????????? ?????? panel_id
		String title_id = GlobalCom.isNull(multipartRequest.getParameter("title_id"));				// ?????? ????????? ?????? ????????? ?????? ?????? ????????? ?????? title_id
		String title_nm = GlobalCom.isNull(multipartRequest.getParameter("title_nm"));				// ?????? ????????? ?????? ????????? ?????? ?????? ????????? ?????? title_nm
		String category_id = GlobalCom.isNull(multipartRequest.getParameter("category_id"));		// ?????? ????????? ?????? ????????? ?????? ?????? ????????? category_id
		String category_type = GlobalCom.isNull(multipartRequest.getParameter("category_type"));	// ?????? ????????? ?????? ????????? ?????? ?????? ????????? category_type(LIVE, VOD, CAT_MAP, SCHEDULE, WISH)
		String album_cnt = GlobalCom.isNull(multipartRequest.getParameter("album_cnt"));			// ?????? ????????? ?????? ????????? ?????? ?????? ????????? album_cnt(category_type??? CAT_MAP????????? ?????? ????????? ??????)
		String ui_type = GlobalCom.isNull(multipartRequest.getParameter("ui_type"));				// ????????? UI ??????
		String description = GlobalCom.isNull(multipartRequest.getParameter("description"));		// ?????? ??????
		String login_id = GlobalCom.isNull(multipartRequest.getParameter("smartUXManager"));		// ????????? ???????????? ????????? ??? ????????? ????????? id
		String page_type = GlobalCom.isNull(multipartRequest.getParameter("page_type"));			// ?????? ??????
		String page_code = GlobalCom.isNull(multipartRequest.getParameter("page_code"));			// ?????? ??????
		String category_gb = GlobalCom.isNull(multipartRequest.getParameter("category_gb"));		// ???????????? ??????
		String reps_album_id = GlobalCom.isNull(multipartRequest.getParameter("reps_album_id"));		// ????????? ??????????????? ??????
		String reps_category_id = GlobalCom.isNull(multipartRequest.getParameter("reps_category_id"));	// ????????? ??????????????? ??????
		String trailer_viewing_type = GlobalCom.isNull(multipartRequest.getParameter("trailer_viewing_type"));		// ????????? ????????????
		String reps_trailer_viewing_type = GlobalCom.isNull(multipartRequest.getParameter("reps_trailer_viewing_type"));	// ??????????????? ????????? ????????????
		String location_code = GlobalCom.isNull(multipartRequest.getParameter("location_code"));	// ????????????
		String location_yn = GlobalCom.isNull(multipartRequest.getParameter("location_yn"));		// ??????/????????????
		
		String paper_ui_type = GlobalCom.isNull(multipartRequest.getParameter("paper_ui_type"));	// ?????? UI ??????
		String abtest_yn = GlobalCom.isNull(multipartRequest.getParameter("abtest_yn"));	// ABTEST ??????
		
		String product_code = GlobalCom.isNull(multipartRequest.getParameter("product_code"));		// ?????????????????????
		String product_code_not = GlobalCom.isNull(multipartRequest.getParameter("product_code2"));	// ?????????????????????(?????????)
		String show_cnt = GlobalCom.isNull(multipartRequest.getParameter("show_cnt"));	// ?????????????????????(?????????)
		String terminal_all_yn = GlobalCom.isNull(multipartRequest.getParameter("terminal_all_yn"));// ???????????? ????????????
		String terminal_arr = GlobalCom.isNull(multipartRequest.getParameter("terminal_arr"));		// ????????????
		
		logger.debug("variation_id : " + variation_id);
		logger.debug("panel_id : " + panel_id);
		logger.debug("title_id : " + title_id);
		logger.debug("title_nm : " + title_nm);
		logger.debug("category_id : " + category_id);
		logger.debug("category_type : " + category_type);
		logger.debug("album_cnt : " + album_cnt);
		logger.debug("ui_type : " + ui_type);
		logger.debug("description : " + description);
		logger.debug("login_id : " + login_id);
		logger.debug("page_type : " + page_type);
		logger.debug("page_code : " + page_code);
		logger.debug("category_gb : " + category_gb);
		logger.debug("reps_album_id : " + reps_album_id);
		logger.debug("reps_category_id : " + reps_category_id);
		logger.debug("trailer_viewing_type : " + trailer_viewing_type);
		logger.debug("reps_trailer_viewing_type : " + reps_trailer_viewing_type);
		logger.debug("location_code : " + location_code);
		logger.debug("location_yn : " + location_yn);
		logger.debug("paper_ui_type : " + paper_ui_type);
		logger.debug("abtest_yn : " + abtest_yn);
		
		logger.debug("product_code : " + product_code);
		logger.debug("product_code_not : " + product_code_not);
		logger.debug("show_cnt : " + show_cnt);
		logger.debug("terminal_all_yn : " + terminal_all_yn);
		logger.debug("terminal_arr : " + terminal_arr);

		description = description.replace("\r", "");
		
		String update_id = login_id;			// ????????? ID(????????? ????????? ??? ????????? ????????? ID??? ?????????
		
		String resultcode = "";
		String resultmessage = "";
		
//		String tmp_file_name = "";
//		String tmp_file_name2 = "";
//		String bg_img_file = "";
//		String bg_img_file2 = "";
//		File img_file = null;
//		File img_file2 = null;
		
		try{
			service.updatePaperDataType(variation_id, panel_id, title_id, title_nm, category_id, category_type, album_cnt, ui_type, description, update_id, page_type, page_code, category_gb, 
					reps_album_id, reps_category_id, trailer_viewing_type, reps_trailer_viewing_type, location_code, location_yn, paper_ui_type, abtest_yn, product_code, product_code_not, show_cnt, terminal_all_yn, terminal_arr);
			resultcode = SmartUXProperties.getProperty("flag.success");
			resultmessage = SmartUXProperties.getProperty("message.success");
		}catch(Exception e){
			logger.error("[updatePaperDataType]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			
			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}
		
		String result = "{\"result\":{\"flag\":\"" + resultcode + "\",\"message\":\"" + resultmessage + "\"}}";

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(result).toString(), responseHeaders, HttpStatus.CREATED);
	}
	
	/**
	 * 2021.05.13 AB????????? MIMS
	 * AB????????? ?????? ????????????
	 * @param variation_id
	 * @param test_id
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/abtest/finishABTestPaper", method=RequestMethod.POST)
	public ResponseEntity<String> finishABTestPaper(
			@RequestParam(value = "test_id", required = true, defaultValue = "") final String test_id,
			HttpServletRequest request, HttpServletResponse response ) throws Exception {
		
		String loginUser = CookieUtil.getCookieUserID(request);
		logger.info("[AB????????? ?????? ????????????][start][ABTestController][finishABTestPaper][test_id: " + test_id + "][loginUser: " + loginUser +"]");
		String resultCode = SmartUXProperties.getProperty("flag.etc");
		String resultMsg = SmartUXProperties.getProperty("message.etc");
		
		try{
			Map<String, String> abmsMap =  service.finishABTestPaper(test_id);
			if(!CollectionUtils.isEmpty(abmsMap)){
				logger.info("[AB????????? ?????? ????????????][ABTestController][finishABTestPaper][test_id: " + test_id + "][loginUser: " + loginUser +"][ABMS ???????????? resCode: " + abmsMap.get("resCode") + "][ABMS ???????????? resMsg:" + abmsMap.get("resMsg") + "]");
				
				if("0".equals(abmsMap.get("resCode"))){
					//????????????DB update
					service.updateABTestStatus("C", test_id);
					resultCode = SmartUXProperties.getProperty("flag.success");
					resultMsg = SmartUXProperties.getProperty("message.success");
				}else if(SmartUXProperties.getProperty("flag.cache.reload.waittime").equals(abmsMap.get("resCode"))){
					resultCode = abmsMap.get("resCode");
					resultMsg = abmsMap.get("resMsg");
				}else{
					//?????????DB update
					service.updateABTestStatus("F", test_id);
					String msg = "[ABMS ???????????? resCode: " + abmsMap.get("resCode") + "][ABMS ???????????? resMsg: " + abmsMap.get("resMsg") + "]";
					resultMsg+=msg;
				}
			}else{
				logger.info("[AB????????? ?????? ????????????][ABTestController][finishABTestPaper][test_id: " + test_id + "][loginUser: " + loginUser +"][ABMS ???????????? ??????]");
				//?????????DB update
				service.updateABTestStatus("F", test_id);
				resultMsg+="[ABMS ???????????? ??????]";
			}
			
			logger.info("[AB????????? ?????? ????????????][end][ABTestController][finishABTestPaper][ABMS ?????? ??????][test_id: " + test_id + "][loginUser: " + loginUser +"]");
			
		}catch(Exception e){
			logger.warn("[AB????????? ?????? ????????????][ABTestController][finishABTestPaper][?????? ???????????? ??????][" + e + "]");
			ExceptionHandler handler = new ExceptionHandler(e);
			resultCode = handler.getFlag();
			resultMsg = handler.getMessage();
		}
		HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
        return new ResponseEntity<String>("{\"flag\" : \"" + resultCode + "\", \"message\" : \"" + resultMsg + "\"}", responseHeaders, HttpStatus.CREATED);
	}
	
	/**
	 * 2021.05.13 AB????????? MIMS
	 * AB????????? ?????? ????????????
	 * @param variation_id
	 * @param test_id
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/abtest/applyABTestPaperCache", method=RequestMethod.POST)
	public ResponseEntity<String> applyABTestPaperCache(
			@RequestParam(value = "panel_id", required = true, defaultValue = "") final String panel_id,
			@RequestParam(value = "test_id", required = true, defaultValue = "") final String test_id,
			@RequestParam(value = "variation_id", required = true, defaultValue = "") final String variation_id,
			@RequestParam(value = "org_mims_id", required = true, defaultValue = "") final String org_mims_id,
			@RequestParam(value = "d_mims_id", required = true, defaultValue = "") final String d_mims_id,
		  HttpServletRequest request, HttpServletResponse response ) throws Exception {

		final CLog cLog = new CLog(applyLogger, request.getRemoteAddr(), request.getRequestURI(), request.getMethod());

		final String loginUser = CookieUtil.getCookieUserID(request);
		final Map<String, String> map = new HashMap<String, String>();

		try {
			logger.info("[AB????????? ?????? ????????????][start][ABTestController][applyABTestPaperCache][variation_id: " + variation_id + "][test_id: " + test_id + "][loginUser: " + loginUser +"]");
			
			final String host = SmartUXProperties.getProperty("cache.sync.public.host");
			final String port = SmartUXProperties.getProperty("cache.sync.public.port");
			
			String fileLockPath = SmartUXProperties.getProperty("ABTestDao.refreshABTestList.fileLock");
			int waitTime = NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.wait"), 5);
			
			FileProcessLockFactory.start(fileLockPath, waitTime,
				new FileProcessFunction("AB????????? ?????? DB??????", ResultCode.ApplyDBWriteFail){
				@Override
				public void run() throws Exception {
					service.insertABTestPanelTitle(panel_id, loginUser);
					cLog.middleLog("?????? ????????????", "??????DB", "??????");
				}
			}, 
				new FileProcessFunction("AB????????? ?????? ????????????", ResultCode.ApplyRequestFail){
				@Override
				public void run() throws Exception {
					
					//???????????? ?????? ??? sleep ??? ?????? -> DB??????????????? OGG??? 10??? ?????? ????????? ????????? ?????? ???????????? ??? ????????? ????????? ?????????
					Thread.sleep(Integer.parseInt(StringUtils.defaultIfEmpty(SmartUXProperties.getProperty("apply.cache.call.sleep"), "10000")));
					
					RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
							host, 
							port, 
							SmartUXProperties.getProperty("ABTestDao.refreshABTestList.url") + "?variation_id=" + variation_id + "&test_id=" + test_id,
							StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.method"), "POST"),
							StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"),
							MediaType.APPLICATION_JSON.toString(),
							NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.timeout"), 60000)).getResponseBody());
					map.put("flag", codeMsg.getCode());
					map.put("message",  codeMsg.getMessage());
					cLog.middleLog("AB????????? ?????? ????????????", "API ?????? ??????", codeMsg.getCode(), codeMsg.getMessage());
				}
			});
			
			//????????? ????????????
			if (StringUtils.isNotBlank(d_mims_id)) {
				FileProcessLockFactory.start(fileLockPath, waitTime,
						new FileProcessFunction("AB????????? ?????? DB??????", ResultCode.ApplyDBWriteFail){
						@Override
						public void run() throws Exception {
							service.insertABTestPanelTitle(d_mims_id, loginUser);
							cLog.middleLog("?????? ????????????", "??????DB", "??????");
						}
					}, 
						new FileProcessFunction("AB????????? ?????? ????????????", ResultCode.ApplyRequestFail){
						@Override
						public void run() throws Exception {
							
							//???????????? ?????? ??? sleep ??? ?????? -> DB??????????????? OGG??? 10??? ?????? ????????? ????????? ?????? ???????????? ??? ????????? ????????? ?????????
							Thread.sleep(Integer.parseInt(StringUtils.defaultIfEmpty(SmartUXProperties.getProperty("apply.cache.call.sleep"), "10000")));
							
							RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
									host, 
									port, 
									SmartUXProperties.getProperty("ABTestDao.refreshABTestList.url") + "?variation_id=" + "O"+test_id + "&test_id=" + test_id,
									StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.method"), "POST"),
									StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"),
									MediaType.APPLICATION_JSON.toString(),
									NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.timeout"), 60000)).getResponseBody());
							map.put("flag", codeMsg.getCode());
							map.put("message",  codeMsg.getMessage());
							cLog.middleLog("AB????????? ?????? ????????????", "API ?????? ??????", codeMsg.getCode(), codeMsg.getMessage());
						}
				});
			}
			
		} catch (MimsCommonException e) {
			cLog.warnLog("AB????????? ?????? ????????????", "??????", e.getFlag(), e.getMessage(), e.getLocalizedMessage());
			map.put("flag", e.getFlag());
			map.put("message", e.getMessage());
		} finally {
			logger.info("[AB????????? ?????? ????????????][end][ABTestController][applyABTestPaperCache][variation_id: " + variation_id + "][test_id: " + test_id + "][loginUser: " + loginUser +"][flag: " + map.get("flag") + "][message: " + map.get("message") + "]");
		}
		
		ObjectMapper mapper = new ObjectMapper();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		return new ResponseEntity<String>(mapper.writeValueAsString(map), responseHeaders, HttpStatus.OK);
	}
	
	/**
	 * 2021.05.14 AB????????? MIMS
	 * ABMS ????????? ??????
	 * @param variation_id
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/abtest/checkABMSStatus", method=RequestMethod.POST)
	public ResponseEntity<String> checkABMSStatus(
			@RequestParam(value = "variation_id", required = true, defaultValue = "") String variation_id,
			HttpServletRequest request, HttpServletResponse response ) throws Exception {
		
		
		String loginUser = CookieUtil.getCookieUserID(request);
		logger.info("[AB????????? ABMS ????????? ??????][start][ABTestController][checkABMSStatus][variation_id: " + variation_id + "][loginUser: " + loginUser +"]");
		String resultCode = SmartUXProperties.getProperty("flag.etc");
		String resultMsg = SmartUXProperties.getProperty("message.etc");
		boolean abmsUptFlag = false;
		
		try{
			//ABMS ??????
			ABTestSearchVO searchVo = new ABTestSearchVO();
			searchVo.setFindName("variation_id");
			searchVo.setFindValue(variation_id);
			searchVo.setPageSize(0);
			searchVo.setPageNum(1);
			
			Map<String, Object> abmsMap = service.getABMSCall(searchVo);
			
			if(!CollectionUtils.isEmpty(abmsMap)){
				logger.info("[AB????????? ABMS ????????? ??????][ABTestController][checkABMSStatus][variation_id: " + variation_id + "][loginUser: " + loginUser +"][ABMS ???????????? resCode: " + abmsMap.get("resCode") + "][ABMS ???????????? resMsg:" + abmsMap.get("resMsg") + "]");
				
				if("0".equals(abmsMap.get("resCode"))){
					List<ABTestVO> abmsList = (List<ABTestVO>) abmsMap.get("abmsList");
					//?????? ????????? variation ?????? ??????
					if(!CollectionUtils.isEmpty(abmsList)){
						abmsUptFlag = service.checkABMSStatus(variation_id, abmsList);
						
						resultCode = SmartUXProperties.getProperty("flag.success");
						resultMsg = SmartUXProperties.getProperty("message.success");
					}
				}else{
					String s = "[ABMS ???????????? resCode: " + abmsMap.get("resCode") + "][ABMS ???????????? resMsg:" + abmsMap.get("resMsg") + "]";
					resultMsg+=s;
				}
			}
			
			logger.info("[AB????????? ABMS ????????? ??????][end][ABTestController][checkABMSStatus][ABMS ?????? ??????][variation_id: " + variation_id + "][loginUser: " + loginUser +"][abmsUptFlag: " + abmsUptFlag + "]");
			
		}catch(Exception e){
			logger.warn("[AB????????? ABMS ????????? ??????][ABTestController][checkABMSStatus][AB????????? ABMS ????????? ?????? ??????][" + e + "]");
			ExceptionHandler handler = new ExceptionHandler(e);
			resultCode = handler.getFlag();
			resultMsg = handler.getMessage();
		}
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>("{\"flag\" : \"" + resultCode + "\", \"message\" : \"" + resultMsg + "\", \"abmsUptFlag\" : \"" + abmsUptFlag + "\"}", responseHeaders, HttpStatus.CREATED);
	}
}
