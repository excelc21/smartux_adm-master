package com.dmi.smartux.admin.multipayment.controller;

import com.dmi.commons.consts.MimsConsts;
import com.dmi.commons.consts.ResultCode;
import com.dmi.commons.exception.MimsCommonException;
import com.dmi.commons.factory.FileProcessFunction;
import com.dmi.commons.factory.FileProcessLockFactory;
import com.dmi.commons.logger.CLog;
import com.dmi.commons.model.RestfulCodeMsg;
import com.dmi.commons.utility.ExcelWorkBookFactory;
import com.dmi.commons.utility.HttpClientUtils;
import com.dmi.commons.utility.RestfulUtils;
import com.dmi.smartux.admin.multipayment.service.MultiPaymentService;
import com.dmi.smartux.admin.multipayment.vo.MultiPaymentSearchVo;
import com.dmi.smartux.admin.multipayment.vo.MultiPaymentVo;
import com.dmi.smartux.admin.multipayment.vo.PtUxPaymentPpmVo;
import com.dmi.smartux.common.exception.ExceptionHandler;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.CookieUtil;
import com.dmi.smartux.common.util.DateUtils;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.util.HTMLCleaner;
import com.dmi.smartux.common.util.HttpCommUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Row;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author medialog
 * MultiPayment 내역 조회
 */
@Controller
public class MultiPaymentController {
	
	private final Log logger = LogFactory.getLog(MultiPaymentController.class);	
	
    @Autowired
    MultiPaymentService multipaymentService;

    /**
     * MultiPayment 내역 리스트 조회
     */
    @RequestMapping(value = "/admin/multipayment/list", method = RequestMethod.GET)
    public String getMultiPaymentList(
    		@RequestParam(value = "findName", required = false, defaultValue = "") String findName,
            @RequestParam(value = "findValue", required = false, defaultValue = "") String findValue,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") String pageSize,
            @RequestParam(value = "pageNum", required = false, defaultValue = "1") String pageNum,
            @RequestParam(value = "status", required = false, defaultValue = "") String status,
            @RequestParam(value = "paType", required = false, defaultValue = "") String paType,
            @RequestParam(value = "mtype", required = false, defaultValue = "") String mtype,
            @RequestParam(value = "startDt", required = false, defaultValue = "") String startDt,
            @RequestParam(value = "endDt", required = false, defaultValue = "") String endDt,
            @RequestParam(value = "discount_div", required = false, defaultValue = "") String discount_div,
            HttpServletRequest request,  Model model) throws Exception {
    	
    	logger.debug("[getMultiPaymentList][START]");
    	
        String domain = GlobalCom.getProperties(SmartUXProperties.getProperty("filepath.suxm.common"), "smartux.domain.https");
        model.addAttribute("loginUser", CookieUtil.getCookieUserID(request));
        model.addAttribute("domain_https", domain);

        findName = HTMLCleaner.clean(findName);
        findValue = HTMLCleaner.clean(findValue);
        pageNum = HTMLCleaner.clean(pageNum);

        MultiPaymentSearchVo multipaymentSearchVo = new MultiPaymentSearchVo();

        /*DB부하발생:화면 처음표시 할 때 검색을 하지 않도록 수정*/
        boolean doSearch = true; 
        if(StringUtils.isEmpty(findName)) {
        	doSearch = false; 
        }

        multipaymentSearchVo.setFindName(StringUtils.defaultIfEmpty(findName, "PA_KEY"));
        multipaymentSearchVo.setFindValue(StringUtils.defaultString(findValue));

        multipaymentSearchVo.setPageSize(NumberUtils.toInt(pageSize, 10));
        multipaymentSearchVo.setBlockSize(10);
        multipaymentSearchVo.setPageNum(GlobalCom.isNumber(Integer.parseInt(pageNum), 1));
        
        multipaymentSearchVo.setStatus(status);
        multipaymentSearchVo.setPaType(StringUtils.defaultString(paType, MimsConsts.PA_TYPE.SIMPLE.getCode()));
        multipaymentSearchVo.setMtype(mtype);

        multipaymentSearchVo.setDiscount_div(discount_div);
        if (!startDt.isEmpty() && !endDt.isEmpty()) {
        	multipaymentSearchVo.setStartDt(startDt);
        	multipaymentSearchVo.setEndDt(endDt);
        	multipaymentSearchVo.setCompareYear((startDt.substring(0, 4).equals(endDt.substring(0, 4))) ? 1 : 0);
        } else {
            /*DB부하발생:기간 미입력시 하루전 날짜로 고정*/
        	multipaymentSearchVo.setStartDt(DateUtils.convertToString(DateUtils.addDate(new Date(), Calendar.DATE, -1), DateUtils.DatePattern.yyyyMMdd_2));
        	multipaymentSearchVo.setEndDt(DateUtils.convertToString(new Date(), DateUtils.DatePattern.yyyyMMdd_2));
        	multipaymentSearchVo.setCompareYear((multipaymentSearchVo.getStartDt().substring(0, 4).equals(multipaymentSearchVo.getEndDt().substring(0, 4))) ? 1 : 0);
        }
        List<String> monthList = new ArrayList<String>();
        int start = DateUtils.getDateField(DateUtils.convertToDate(multipaymentSearchVo.getStartDt(), DateUtils.DatePattern.yyyyMMdd_2), Calendar.MONTH) + 1;
        int end = DateUtils.getDateField(DateUtils.convertToDate(multipaymentSearchVo.getEndDt(), DateUtils.DatePattern.yyyyMMdd_2), Calendar.MONTH) + 1;
        for(int i = start; i <= end; i++) {
            monthList.add(String.valueOf(i));
        }
        multipaymentSearchVo.setMonth_list(monthList);

        
        try {
            /*DB부하발생:화면 처음표시시에 전체검색 금지*/
            List<MultiPaymentVo> list = new ArrayList<MultiPaymentVo>();
            multipaymentSearchVo.setPageCount(0);
            
            if(doSearch) {
            	list = multipaymentService.getMultiPaymentList(multipaymentSearchVo);
            	multipaymentSearchVo.setPageCount(multipaymentService.getMultiPaymentListCount(multipaymentSearchVo)); 
            }

            model.addAttribute("vo", multipaymentSearchVo);
            model.addAttribute("list", list);
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        logger.debug("[getMultiPaymentList][END]");


        return "admin/multipayment/multipaymentList";
    }
    
    
    /**
     * 구매내역 리스트 (라이브)MultiPayment 내역 리스트 조회
     */
    @RequestMapping(value = "/admin/multipayment/livelist", method = RequestMethod.GET)
    public String getMultiPaymentList_live(
    		@RequestParam(value = "findName", required = false, defaultValue = "") String findName,
            @RequestParam(value = "findValue", required = false, defaultValue = "") String findValue,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") String pageSize,
            @RequestParam(value = "pageNum", required = false, defaultValue = "1") String pageNum,
            @RequestParam(value = "status", required = false, defaultValue = "") String status,
            @RequestParam(value = "paType", required = false, defaultValue = "") String paType,
            @RequestParam(value = "mtype", required = false, defaultValue = "") String mtype,
            @RequestParam(value = "startDt", required = false, defaultValue = "") String startDt,
            @RequestParam(value = "endDt", required = false, defaultValue = "") String endDt,
            @RequestParam(value = "discount_div", required = false, defaultValue = "") String discount_div,
            @RequestParam(value = "find_pa_key", required = false, defaultValue = "") String find_pa_key,
            HttpServletRequest request,  Model model) throws Exception {
    	
    	logger.debug("[getMultiPaymentList][START]");
        String domain = GlobalCom.getProperties(SmartUXProperties.getProperty("filepath.suxm.common"), "smartux.domain.https");
        model.addAttribute("loginUser", CookieUtil.getCookieUserID(request));
        model.addAttribute("domain_https", domain);

        findName = HTMLCleaner.clean(findName);
        findValue = HTMLCleaner.clean(findValue);
        pageNum = HTMLCleaner.clean(pageNum);

        MultiPaymentSearchVo multipaymentSearchVo = new MultiPaymentSearchVo();

        /*DB부하발생:화면 처음표시 할 때 검색을 하지 않도록 수정*/
        boolean doSearch = true; 
        if(StringUtils.isEmpty(findName)) {
        	doSearch = false; 
        }

        multipaymentSearchVo.setFindName(StringUtils.defaultIfEmpty(findName, "PA_KEY"));
        multipaymentSearchVo.setFindValue(StringUtils.defaultString(findValue));

        multipaymentSearchVo.setPageSize(NumberUtils.toInt(pageSize, 10));
        multipaymentSearchVo.setBlockSize(10);
        multipaymentSearchVo.setPageNum(GlobalCom.isNumber(Integer.parseInt(pageNum), 1));
        
        multipaymentSearchVo.setStatus(status);
        multipaymentSearchVo.setPaType(StringUtils.defaultString(paType, MimsConsts.PA_TYPE.SIMPLE.getCode()));
        multipaymentSearchVo.setMtype(mtype);

        multipaymentSearchVo.setDiscount_div(discount_div);
        if (!startDt.isEmpty() && !endDt.isEmpty()) {
        	multipaymentSearchVo.setStartDt(startDt);
        	multipaymentSearchVo.setEndDt(endDt);
        	multipaymentSearchVo.setCompareYear((startDt.substring(0, 4).equals(endDt.substring(0, 4))) ? 1 : 0);
        } else {
            /*DB부하발생:기간 미입력시 하루전 날짜로 고정*/
        	multipaymentSearchVo.setStartDt(DateUtils.convertToString(DateUtils.addDate(new Date(), Calendar.DATE, -1), DateUtils.DatePattern.yyyyMMdd_2));
        	multipaymentSearchVo.setEndDt(DateUtils.convertToString(new Date(), DateUtils.DatePattern.yyyyMMdd_2));
        	multipaymentSearchVo.setCompareYear((multipaymentSearchVo.getStartDt().substring(0, 4).equals(multipaymentSearchVo.getEndDt().substring(0, 4))) ? 1 : 0);
        }
        List<String> monthList = new ArrayList<String>();
        int start = DateUtils.getDateField(DateUtils.convertToDate(multipaymentSearchVo.getStartDt(), DateUtils.DatePattern.yyyyMMdd_2), Calendar.MONTH) + 1;
        int end = DateUtils.getDateField(DateUtils.convertToDate(multipaymentSearchVo.getEndDt(), DateUtils.DatePattern.yyyyMMdd_2), Calendar.MONTH) + 1;
        for(int i = start; i <= end; i++) {
            monthList.add(String.valueOf(i));
        }
        multipaymentSearchVo.setMonth_list(monthList);
        
        multipaymentSearchVo.setFind_pa_key(find_pa_key);
        try {
            /*DB부하발생:화면 처음표시시에 전체검색 금지*/
            List<MultiPaymentVo> list = new ArrayList<MultiPaymentVo>();
            multipaymentSearchVo.setPageCount(0);
            
            if(doSearch) {
            	list = multipaymentService.getMultiPaymentList(multipaymentSearchVo);
            	multipaymentSearchVo.setPageCount(multipaymentService.getMultiPaymentListCount(multipaymentSearchVo)); 
            }

            model.addAttribute("vo", multipaymentSearchVo);
            model.addAttribute("list", list);
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        logger.debug("[getMultiPaymentList][END]");


        return "admin/multipayment/multipaymentLiveList";
    }
    
    
    
    
    
    /**
     * 다회선 내역 리스트 조회
     */
    @RequestMapping(value = "/admin/multipayment/ppmlist", method = RequestMethod.GET)
    public String getPpmList(
    		@RequestParam(value = "findName", required = false, defaultValue = "") String findName,
            @RequestParam(value = "findValue", required = false, defaultValue = "") String findValue,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") String pageSize,
            @RequestParam(value = "pageNum", required = false, defaultValue = "1") String pageNum,
            @RequestParam(value = "startDt", required = false, defaultValue = "") String startDt,
            @RequestParam(value = "endDt", required = false, defaultValue = "") String endDt,
            HttpServletRequest request,  Model model) throws Exception {
    	
    	logger.debug("[getPPMList][START]");
    	
        String domain = GlobalCom.getProperties(SmartUXProperties.getProperty("filepath.suxm.common"), "smartux.domain.https");
        model.addAttribute("loginUser", CookieUtil.getCookieUserID(request));
        model.addAttribute("domain_https", domain);

        

        
        findName = HTMLCleaner.clean(findName);
        findValue = HTMLCleaner.clean(findValue);
        pageNum = HTMLCleaner.clean(pageNum);

        PtUxPaymentPpmVo ptUxPaymentPpmVo = new PtUxPaymentPpmVo();
        List<PtUxPaymentPpmVo> list = new ArrayList<PtUxPaymentPpmVo>();
        
        ptUxPaymentPpmVo.setFindName(findName);
        ptUxPaymentPpmVo.setFindValue(findValue);
        
        /*DB부하발생:화면 처음표시 할 때 검색을 하지 않도록 수정*/
        boolean doSearch = true; 
        if(StringUtils.isEmpty(findName) && StringUtils.isEmpty(findValue)) {
        	doSearch = false; 
        }

        if( findValue.isEmpty()  && !startDt.isEmpty() && !endDt.isEmpty()){
        	findName = "DATE_ONLY";
        }else if(findName.equals("PA_KEY")) {
        	ptUxPaymentPpmVo.setPa_key(findValue);
        }else if(findName.equals("SA_ID")) {
        	ptUxPaymentPpmVo.setSa_id(findValue);
        } 
         
        
        
    


        ptUxPaymentPpmVo.setPageSize(NumberUtils.toInt(pageSize, 10));
        ptUxPaymentPpmVo.setBlockSize(10);
        ptUxPaymentPpmVo.setPageNum(GlobalCom.isNumber(Integer.parseInt(pageNum), 1));
        


        
        try {  
        	
            ptUxPaymentPpmVo.setPageCount(0);
          
            
            if (!startDt.isEmpty() && !endDt.isEmpty()) {
            
               String startDt_mod = startDt.replace("-", "");
               startDt_mod = startDt_mod + "000000";
               String endDt_mod = endDt.replace("-", "");
               endDt_mod = endDt_mod + "999999";
            	
            	ptUxPaymentPpmVo.setStart_dt(startDt_mod);
            	ptUxPaymentPpmVo.setEnd_dt(endDt_mod);
            } 
            
      
            
            if(findName.equals("PA_KEY") && doSearch){
            	list = multipaymentService.getPtUxPaymentPpmListByPakey(ptUxPaymentPpmVo);
            	ptUxPaymentPpmVo.setPageCount(multipaymentService.getMultiPaymentCountByPakey(ptUxPaymentPpmVo));  
            }else if(findName.equals("SA_ID") && doSearch){
            	list = multipaymentService.getPtUxPaymentPpmListBySaid(ptUxPaymentPpmVo);
            	ptUxPaymentPpmVo.setPageCount(multipaymentService.getMultiPaymentCountBySaid(ptUxPaymentPpmVo));  
            }else if(findName.equals("DATE_ONLY") && doSearch){
            	list = multipaymentService.getPtUxPaymentPpmListByDate(ptUxPaymentPpmVo);
            	ptUxPaymentPpmVo.setPageCount(multipaymentService.getMultiPaymentCountByDate(ptUxPaymentPpmVo));   	
            }
            	
        	ptUxPaymentPpmVo.setStart_dt(startDt);
        	ptUxPaymentPpmVo.setEnd_dt(endDt);
        //	ptUxPaymentPpmVo.setFindName("DATE_ONLY");
        	
        	ptUxPaymentPpmVo.setFindName(findName);
            model.addAttribute("vo", ptUxPaymentPpmVo);
            model.addAttribute("list", list);
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        logger.debug("[getPPMList][END]");


        return "admin/multipayment/multipaymentListTv";
    }
    
    
    
    
    
    
    /**
     * MultiPayment 상세내역 조회
     */
    @RequestMapping(value = "/admin/multipayment/viewLivelist", method = RequestMethod.GET)
    public String getLivelistMultiPaymentInfo(
    		@RequestParam(value = "type", required = false, defaultValue = "") String type,
            @RequestParam(value = "findName", required = false, defaultValue = "") String findName,
            @RequestParam(value = "findValue", required = false, defaultValue = "") String findValue,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") String pageSize,
            @RequestParam(value = "pageNum", required = false, defaultValue = "1") String pageNum,
            @RequestParam(value = "status", required = false, defaultValue = "") String status,
            @RequestParam(value = "startDt", required = false, defaultValue = "") String startDt,
            @RequestParam(value = "endDt", required = false, defaultValue = "") String endDt,
            @RequestParam(value = "paKey", required = false, defaultValue = "") String paKey,
            @RequestParam(value = "paType", required = false, defaultValue = "") String paType,
            @RequestParam(value = "mtype", required = false, defaultValue = "") String mtype,
            @RequestParam(value = "ptYear", required = false, defaultValue = "") String ptYear,
            @RequestParam(value = "ptMonth", required = false, defaultValue = "") String ptMonth,
            @RequestParam(value = "discount_div", required = false, defaultValue = "") String discount_div,
            @RequestParam(value = "find_pa_key", required = false, defaultValue = "") String find_pa_key,
            HttpServletRequest request, Model model) throws Exception {
    	
    	logger.debug("[getMultiPaymentInfo][START]");
    	
        String domain = GlobalCom.getProperties(SmartUXProperties.getProperty("filepath.suxm.common"), "smartux.domain.https");
        model.addAttribute("domain_https", domain);
        model.addAttribute("loginUser", CookieUtil.getCookieUserID(request));

        findName = HTMLCleaner.clean(findName);
        findValue = HTMLCleaner.clean(findValue);
        pageNum = HTMLCleaner.clean(pageNum);

        MultiPaymentSearchVo multipaymentSearchVo = new MultiPaymentSearchVo();
        multipaymentSearchVo.setFindName(StringUtils.defaultString(findName));
        multipaymentSearchVo.setFindValue(StringUtils.defaultString(findValue));

        multipaymentSearchVo.setPageSize(NumberUtils.toInt(pageSize, 10));
        multipaymentSearchVo.setBlockSize(10);
        multipaymentSearchVo.setPageNum(GlobalCom.isNumber(Integer.parseInt(pageNum), 1));
        
        multipaymentSearchVo.setStatus(status);
        multipaymentSearchVo.setStartDt(startDt);
        multipaymentSearchVo.setEndDt(endDt);
        multipaymentSearchVo.setPaType(paType);
        multipaymentSearchVo.setMtype(mtype);

        multipaymentSearchVo.setDiscount_div(discount_div);
        multipaymentSearchVo.setFind_pa_key(find_pa_key);
        try {
        	multipaymentSearchVo.setPaKey(paKey);
        	multipaymentSearchVo.setPt_year(ptYear);
        	multipaymentSearchVo.setPt_month(ptMonth);
        	multipaymentSearchVo.setType(type);
        	MultiPaymentVo view = multipaymentService.getMultiPaymentInfo(multipaymentSearchVo);

            model.addAttribute("vo", multipaymentSearchVo);
            model.addAttribute("view", view);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        logger.debug("[getMultiPaymentInfo][END]");

        return "admin/multipayment/multipaymentInfoLive";
    }
    
    /**
     * MultiPayment 상세내역 조회
     */
    @RequestMapping(value = "/admin/multipayment/view", method = RequestMethod.GET)
    public String getMultiPaymentInfo(
    		@RequestParam(value = "type", required = false, defaultValue = "") String type,
            @RequestParam(value = "findName", required = false, defaultValue = "") String findName,
            @RequestParam(value = "findValue", required = false, defaultValue = "") String findValue,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") String pageSize,
            @RequestParam(value = "pageNum", required = false, defaultValue = "1") String pageNum,
            @RequestParam(value = "status", required = false, defaultValue = "") String status,
            @RequestParam(value = "startDt", required = false, defaultValue = "") String startDt,
            @RequestParam(value = "endDt", required = false, defaultValue = "") String endDt,
            @RequestParam(value = "paKey", required = false, defaultValue = "") String paKey,
            @RequestParam(value = "paType", required = false, defaultValue = "") String paType,
            @RequestParam(value = "mtype", required = false, defaultValue = "") String mtype,
            @RequestParam(value = "ptYear", required = false, defaultValue = "") String ptYear,
            @RequestParam(value = "ptMonth", required = false, defaultValue = "") String ptMonth,
            @RequestParam(value = "discount_div", required = false, defaultValue = "") String discount_div,
            HttpServletRequest request, Model model) throws Exception {
    	
    	logger.debug("[getMultiPaymentInfo][START]");
    	
        String domain = GlobalCom.getProperties(SmartUXProperties.getProperty("filepath.suxm.common"), "smartux.domain.https");
        model.addAttribute("domain_https", domain);
        model.addAttribute("loginUser", CookieUtil.getCookieUserID(request));

        findName = HTMLCleaner.clean(findName);
        findValue = HTMLCleaner.clean(findValue);
        pageNum = HTMLCleaner.clean(pageNum);

        MultiPaymentSearchVo multipaymentSearchVo = new MultiPaymentSearchVo();
        multipaymentSearchVo.setFindName(StringUtils.defaultString(findName));
        multipaymentSearchVo.setFindValue(StringUtils.defaultString(findValue));

        multipaymentSearchVo.setPageSize(NumberUtils.toInt(pageSize, 10));
        multipaymentSearchVo.setBlockSize(10);
        multipaymentSearchVo.setPageNum(GlobalCom.isNumber(Integer.parseInt(pageNum), 1));
        
        multipaymentSearchVo.setStatus(status);
        multipaymentSearchVo.setStartDt(startDt);
        multipaymentSearchVo.setEndDt(endDt);
        multipaymentSearchVo.setPaType(paType);
        multipaymentSearchVo.setMtype(mtype);

        multipaymentSearchVo.setDiscount_div(discount_div);
        try {
        	multipaymentSearchVo.setPaKey(paKey);
        	multipaymentSearchVo.setPt_year(ptYear);
        	multipaymentSearchVo.setPt_month(ptMonth);
        	multipaymentSearchVo.setType(type);
        	MultiPaymentVo view = multipaymentService.getMultiPaymentInfo(multipaymentSearchVo);

            model.addAttribute("vo", multipaymentSearchVo);
            model.addAttribute("view", view);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        logger.debug("[getMultiPaymentInfo][END]");

        return "admin/multipayment/multipaymentInfo";
    }
    
    
    
    
    
    
    
    
    

    
    /**
     * MultiPayment 상세내역 조회
     */
    @RequestMapping(value = "/admin/multipayment/ppmview", method = RequestMethod.GET)
    public String getMultiPaymentPpmInfo(
            @RequestParam(value = "findName", required = false, defaultValue = "") String findName,
            @RequestParam(value = "findValue", required = false, defaultValue = "") String findValue,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") String pageSize,
            @RequestParam(value = "pageNum", required = false, defaultValue = "1") String pageNum,
            @RequestParam(value = "startDt", required = false, defaultValue = "") String startDt,
            @RequestParam(value = "endDt", required = false, defaultValue = "") String endDt,
            HttpServletRequest request, Model model) throws Exception {
    	
    	logger.debug("[getMultiPaymentInfo][START]");
    	
        String domain = GlobalCom.getProperties(SmartUXProperties.getProperty("filepath.suxm.common"), "smartux.domain.https");
        model.addAttribute("domain_https", domain);
        model.addAttribute("loginUser", CookieUtil.getCookieUserID(request));


        PtUxPaymentPpmVo ptUxPaymentPpmVo = new PtUxPaymentPpmVo();
        List<PtUxPaymentPpmVo> list = new ArrayList<PtUxPaymentPpmVo>();
        
        findName = HTMLCleaner.clean(findName);
        findValue = HTMLCleaner.clean(findValue);
        pageNum = HTMLCleaner.clean(pageNum);

        ptUxPaymentPpmVo.setFindName(StringUtils.defaultString(findName));
        ptUxPaymentPpmVo.setFindValue(StringUtils.defaultString(findValue));

        if(findName.equals("SA_ID")){
        	ptUxPaymentPpmVo.setSa_id(findValue);
        }else if(findName.equals("PA_KEY")){
        	ptUxPaymentPpmVo.setPa_key(findValue);
        }
        
        
        ptUxPaymentPpmVo.setPageSize(NumberUtils.toInt(pageSize, 10));
        ptUxPaymentPpmVo.setBlockSize(10);
        ptUxPaymentPpmVo.setPageNum(GlobalCom.isNumber(Integer.parseInt(pageNum), 1));
        
        ptUxPaymentPpmVo.setStart_dt(startDt);
        ptUxPaymentPpmVo.setEnd_dt(endDt);

        try {
        	
        	PtUxPaymentPpmVo view = multipaymentService.getMultiPaymentPpmInfo(ptUxPaymentPpmVo);

            model.addAttribute("vo", ptUxPaymentPpmVo);
            model.addAttribute("view", view);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        logger.debug("[getMultiPaymentInfo][END]");

        return "admin/multipayment/multipaymentInfoTv";
    }
    
    
    
    
    
    
    
    
    
    
//    @ResponseBody
//    @RequestMapping(value="/admin/multipayment/cancel", method=RequestMethod.POST )
//	public String cancelMultiPayment(
//			@RequestParam(value="cancelList", required=false, defaultValue="") String cancelList,
//			Model model
//			)  throws Exception{		
//    	String domain = GlobalCom.getProperties(HDTVProperties.getProperty("filepath.suxm.common"), "smartux.domain.https");
//        model.addAttribute("domain_https", domain);
//
//        logger.debug("cancelList : "+cancelList);
//        
//		String resultcode = "";
//		String resultmessage = "";	
//		
//		return "{\"flag\" : \"" + resultcode + "\", \"message\" : \"" + resultmessage + "\"}";		
//	}
    
    /**
     * MultiPayment EXCEL 다운로드
     */
    @ResponseBody
	@RequestMapping(value = "/admin/multipayment/downloadExcelFile", method = RequestMethod.GET)
	public byte[] downloadExcelFile(
			@RequestParam(value = "findName", required = false, defaultValue = "") String findName,
            @RequestParam(value = "findValue", required = false, defaultValue = "") String findValue,
            @RequestParam(value = "status", required = false, defaultValue = "") String status,
            @RequestParam(value = "paType", required = false, defaultValue = "") String paType,
            @RequestParam(value = "mtype", required = false, defaultValue = "") String mtype,
            @RequestParam(value = "startDt", required = false, defaultValue = "") String startDt,
            @RequestParam(value = "endDt", required = false, defaultValue = "") String endDt,
            @RequestParam(value = "discount_div", required = false, defaultValue = "") String discount_div,
            HttpServletRequest request,
            HttpServletResponse response,
			Model model) throws Exception {
    	
    	logger.debug("[MultiPaymentController][downloadExcelFile][START]");
        final String loginUser = CookieUtil.getCookieUserID(request);

		String domain = GlobalCom.getProperties(SmartUXProperties.getProperty("filepath.suxm.common"), "smartux.domain.https");
		model.addAttribute("domain_https", domain);
		
		findName = HTMLCleaner.clean(findName);
        findValue = HTMLCleaner.clean(findValue);

        MultiPaymentSearchVo multipaymentSearchVo = new MultiPaymentSearchVo();
        multipaymentSearchVo.setFindName(StringUtils.defaultIfEmpty(findName, "PA_KEY"));
        multipaymentSearchVo.setFindValue(StringUtils.defaultString(findValue));
        multipaymentSearchVo.setStatus(status);
        multipaymentSearchVo.setPaType(paType);
        multipaymentSearchVo.setMtype(mtype);
        multipaymentSearchVo.setDiscount_div(discount_div);
        multipaymentSearchVo.setDownloadDt(DateUtils.convertToString(new Date(), DateUtils.DatePattern.yyyyMMddHHmmss_1));
        if (!startDt.isEmpty() && !endDt.isEmpty()) {
            multipaymentSearchVo.setStartDt(startDt);
            multipaymentSearchVo.setEndDt(endDt);
            multipaymentSearchVo.setCompareYear((startDt.substring(0, 4).equals(endDt.substring(0, 4))) ? 1 : 0);
        } else {
            multipaymentSearchVo.setStartDt(DateUtils.convertToString(DateUtils.addDate(new Date(), Calendar.DATE, -31), DateUtils.DatePattern.yyyyMMdd_2));
            multipaymentSearchVo.setEndDt(DateUtils.convertToString(new Date(), DateUtils.DatePattern.yyyyMMdd_2));
            multipaymentSearchVo.setCompareYear((multipaymentSearchVo.getStartDt().substring(0, 4).equals(multipaymentSearchVo.getEndDt().substring(0, 4))) ? 1 : 0);
        }


        List<Object> header = new ArrayList<Object>();
        List<List<Object>> data = new ArrayList<List<Object>>();

        String[] headerAry = SmartUXProperties.getProperty("multipayment.excel.header").split("\\|");

        ExcelWorkBookFactory.ExcelSheetFactory sheet = ExcelWorkBookFactory.create(10000).sheet("비디오포털 복합결제 구매내역"+ multipaymentSearchVo.getStartDt() + " ~ " + multipaymentSearchVo.getEndDt());
        sheet.headers(1, headerAry);


        int pageCount = 1;
        while(true) {
            multipaymentSearchVo.setPageNum(pageCount++);
            multipaymentSearchVo.setPageSize(1000);
            List<MultiPaymentVo> list = multipaymentService.getMultiPaymentList(multipaymentSearchVo);
            if(list == null || list.size() == 0) {
                break;
            } else {
                sheet.rowCellValues(list, new ExcelWorkBookFactory.CellValueRef<MultiPaymentVo>() {
                    @Override
                    public void setRowData(Row row, MultiPaymentVo vo) {
                        int col = 1;
                        String div = vo.getDiscount_div().replaceAll(",", "");
                        row.createCell(col++).setCellValue(vo.getSa_id());
                        row.createCell(col++).setCellValue(vo.getProd_id());
                        row.createCell(col++).setCellValue(vo.getProd_name());
                        row.createCell(col++).setCellValue(vo.getBuy_type().equals(MimsConsts.BUY_TYPE.Contents.getValue()) ? "컨텐츠" : (vo.getBuy_type().equals(MimsConsts.BUY_TYPE.DataFree.getValue()) ? "DATAFREE" : "컨텐츠+DATAFREE"));
                        if(vo.getTotal_price() == -1) {
                            if(vo.getBuy_type().equals(MimsConsts.BUY_TYPE.Contents.getValue())) {
                                row.createCell(col++).setCellValue((vo.getProd_price() * 1.1));
                            } else if(vo.getBuy_type().equals(MimsConsts.BUY_TYPE.DataFree.getValue())) {
                                row.createCell(col++).setCellValue((vo.getDatafree_price() * 1.1));
                            } else {
                                row.createCell(col++).setCellValue(((vo.getProd_price() + vo.getDatafree_price()) * 1.1));
                            }
                        } else {
                            row.createCell(col++).setCellValue((vo.getTotal_price()));
                        }
                        if(vo.getPa_type().equals(MimsConsts.PA_TYPE.CARD.getCode())) {
                            row.createCell(col++).setCellValue(("신용카드"));
                        } else if(vo.getPa_type().equals(MimsConsts.PA_TYPE.PAYNOW.getCode())) {
                            row.createCell(col++).setCellValue(("페이나우"));
                        } else if(vo.getPa_type().equals(MimsConsts.PA_TYPE.SIMPLE.getCode())) {
                            row.createCell(col++).setCellValue(("기본결제"));
                        } else if(vo.getPa_type().equals(MimsConsts.PA_TYPE.TVPAY.getCode())) {
                            row.createCell(col++).setCellValue(("TV페이"));
                        } else {
                            row.createCell(col++).setCellValue(("정의되지않음"));
                        }
                        for(String val : vo.getDiscount_price().split(",")){
                            row.createCell(col++).setCellValue((val));
                        }
                        if(vo.getPa_status().equals(MimsConsts.PA_STATUS.FINISH.getCode())) {
                            row.createCell(col++).setCellValue("성공");
                        } else if(vo.getPa_status().equals(MimsConsts.PA_STATUS.START.getCode())) {
                            row.createCell(col++).setCellValue("대기중");
                        } else if(vo.getPa_status().equals(MimsConsts.PA_STATUS.CANCEL.getCode())) {
                            row.createCell(col++).setCellValue("취소");
                        } else if(vo.getPa_status().equals(MimsConsts.PA_STATUS.ERROR.getCode())) {
                            row.createCell(col++).setCellValue("실패");
                        } else if(vo.getPa_status().equals(MimsConsts.PA_STATUS.APPROVE.getCode())) {
                            row.createCell(col++).setCellValue("진행중");
                        } else if(vo.getPa_status().equals(MimsConsts.PA_STATUS.CANCELFAIL.getCode())) {
                            row.createCell(col++).setCellValue("취소실패");
                        } else {
                            row.createCell(col++).setCellValue("정의되지않음");
                        }
                        row.createCell(col++).setCellValue(vo.getPa_key());
                        row.createCell(col++).setCellValue(vo.getPa_reg_dt());

                        if(StringUtils.defaultString(loginUser).equals("admin") || StringUtils.defaultString(loginUser).equals("amims")) {
                            row.createCell(col++).setCellValue(vo.getPa_mod_dt());
                            row.createCell(col++).setCellValue(vo.getPa_flag());
                            row.createCell(col++).setCellValue(vo.getPa_message());
                            row.createCell(col++).setCellValue(vo.getFailover_div());
                        }
                    }
                });
            }
        }

		byte[] bytes = sheet.end().make();

		response.setHeader("Content-Disposition", "attachment; filename=MultiPayMent_IPTV_" + GlobalCom.getTodayFormat() + ".xlsx");
		response.setContentLength(bytes.length);
		response.setContentType("application/vnd.ms-excel");

		logger.debug("[MultiPaymentController][downloadExcelFile][END]");
		
		return bytes;
	}
    
    /**
     * MultiPayment EXCEL 다운로드
     */
    @ResponseBody
	@RequestMapping(value = "/admin/multipayment/downloadExcelFileLive", method = RequestMethod.GET)
	public byte[] downloadExcelFileLive(
			@RequestParam(value = "findName", required = false, defaultValue = "") String findName,
            @RequestParam(value = "findValue", required = false, defaultValue = "") String findValue,
            @RequestParam(value = "status", required = false, defaultValue = "") String status,
            @RequestParam(value = "paType", required = false, defaultValue = "") String paType,
            @RequestParam(value = "mtype", required = false, defaultValue = "") String mtype,
            @RequestParam(value = "startDt", required = false, defaultValue = "") String startDt,
            @RequestParam(value = "endDt", required = false, defaultValue = "") String endDt,
            @RequestParam(value = "discount_div", required = false, defaultValue = "") String discount_div,
            @RequestParam(value = "find_pa_key", required = false, defaultValue = "") String find_pa_key,
            HttpServletRequest request,
            HttpServletResponse response,
			Model model) throws Exception {
    	
    	logger.debug("[MultiPaymentController][downloadExcelFile][START]");
        final String loginUser = CookieUtil.getCookieUserID(request);

		String domain = GlobalCom.getProperties(SmartUXProperties.getProperty("filepath.suxm.common"), "smartux.domain.https");
		model.addAttribute("domain_https", domain);
		
		findName = HTMLCleaner.clean(findName);
        findValue = HTMLCleaner.clean(findValue);

        MultiPaymentSearchVo multipaymentSearchVo = new MultiPaymentSearchVo();
        multipaymentSearchVo.setFindName(StringUtils.defaultIfEmpty(findName, "PA_KEY"));
        multipaymentSearchVo.setFindValue(StringUtils.defaultString(findValue));
        multipaymentSearchVo.setStatus(status);
        multipaymentSearchVo.setPaType(paType);
        multipaymentSearchVo.setMtype(mtype);
        multipaymentSearchVo.setDiscount_div(discount_div);
        multipaymentSearchVo.setDownloadDt(DateUtils.convertToString(new Date(), DateUtils.DatePattern.yyyyMMddHHmmss_1));
        if (!startDt.isEmpty() && !endDt.isEmpty()) {
            multipaymentSearchVo.setStartDt(startDt);
            multipaymentSearchVo.setEndDt(endDt);
            multipaymentSearchVo.setCompareYear((startDt.substring(0, 4).equals(endDt.substring(0, 4))) ? 1 : 0);
        } else {
            multipaymentSearchVo.setStartDt(DateUtils.convertToString(DateUtils.addDate(new Date(), Calendar.DATE, -31), DateUtils.DatePattern.yyyyMMdd_2));
            multipaymentSearchVo.setEndDt(DateUtils.convertToString(new Date(), DateUtils.DatePattern.yyyyMMdd_2));
            multipaymentSearchVo.setCompareYear((multipaymentSearchVo.getStartDt().substring(0, 4).equals(multipaymentSearchVo.getEndDt().substring(0, 4))) ? 1 : 0);
        }

        multipaymentSearchVo.setFind_pa_key(find_pa_key);
        
        List<Object> header = new ArrayList<Object>();
        List<List<Object>> data = new ArrayList<List<Object>>();

        String[] headerAry = SmartUXProperties.getProperty("multipayment.excel.header").split("\\|");

        ExcelWorkBookFactory.ExcelSheetFactory sheet = ExcelWorkBookFactory.create(10000).sheet("비디오포털 복합결제 구매내역"+ multipaymentSearchVo.getStartDt() + " ~ " + multipaymentSearchVo.getEndDt());
        sheet.headers(1, headerAry);


        int pageCount = 1;
        while(true) {
            multipaymentSearchVo.setPageNum(pageCount++);
            multipaymentSearchVo.setPageSize(1000);
            List<MultiPaymentVo> list = multipaymentService.getMultiPaymentList(multipaymentSearchVo);
            if(list == null || list.size() == 0) {
                break;
            } else {
                sheet.rowCellValues(list, new ExcelWorkBookFactory.CellValueRef<MultiPaymentVo>() {
                    @Override
                    public void setRowData(Row row, MultiPaymentVo vo) {
                        int col = 1;
                        String div = vo.getDiscount_div().replaceAll(",", "");
                        row.createCell(col++).setCellValue(vo.getSa_id());
                        row.createCell(col++).setCellValue(vo.getProd_id());
                        row.createCell(col++).setCellValue(vo.getProd_name());
                        row.createCell(col++).setCellValue(vo.getBuy_type().equals(MimsConsts.BUY_TYPE.Contents.getValue()) ? "컨텐츠" : (vo.getBuy_type().equals(MimsConsts.BUY_TYPE.DataFree.getValue()) ? "DATAFREE" : "컨텐츠+DATAFREE"));
                        if(vo.getTotal_price() == -1) {
                            if(vo.getBuy_type().equals(MimsConsts.BUY_TYPE.Contents.getValue())) {
                                row.createCell(col++).setCellValue((vo.getProd_price() * 1.1));
                            } else if(vo.getBuy_type().equals(MimsConsts.BUY_TYPE.DataFree.getValue())) {
                                row.createCell(col++).setCellValue((vo.getDatafree_price() * 1.1));
                            } else {
                                row.createCell(col++).setCellValue(((vo.getProd_price() + vo.getDatafree_price()) * 1.1));
                            }
                        } else {
                            row.createCell(col++).setCellValue((vo.getTotal_price()));
                        }
                        if(vo.getPa_type().equals(MimsConsts.PA_TYPE.CARD.getCode())) {
                            row.createCell(col++).setCellValue(("신용카드"));
                        } else if(vo.getPa_type().equals(MimsConsts.PA_TYPE.PAYNOW.getCode())) {
                            row.createCell(col++).setCellValue(("페이나우"));
                        } else if(vo.getPa_type().equals(MimsConsts.PA_TYPE.SIMPLE.getCode())) {
                            row.createCell(col++).setCellValue(("기본결제"));
                        } else if(vo.getPa_type().equals(MimsConsts.PA_TYPE.TVPAY.getCode())) {
                            row.createCell(col++).setCellValue(("TV페이"));
                        } else {
                            row.createCell(col++).setCellValue(("정의되지않음"));
                        }
                        for(String val : vo.getDiscount_price().split(",")){
                            row.createCell(col++).setCellValue((val));
                        }
                        if(vo.getPa_status().equals(MimsConsts.PA_STATUS.FINISH.getCode())) {
                            row.createCell(col++).setCellValue("성공");
                        } else if(vo.getPa_status().equals(MimsConsts.PA_STATUS.START.getCode())) {
                            row.createCell(col++).setCellValue("대기중");
                        } else if(vo.getPa_status().equals(MimsConsts.PA_STATUS.CANCEL.getCode())) {
                            row.createCell(col++).setCellValue("취소");
                        } else if(vo.getPa_status().equals(MimsConsts.PA_STATUS.ERROR.getCode())) {
                            row.createCell(col++).setCellValue("실패");
                        } else if(vo.getPa_status().equals(MimsConsts.PA_STATUS.APPROVE.getCode())) {
                            row.createCell(col++).setCellValue("진행중");
                        } else if(vo.getPa_status().equals(MimsConsts.PA_STATUS.CANCELFAIL.getCode())) {
                            row.createCell(col++).setCellValue("취소실패");
                        } else if(vo.getPa_status().equals(MimsConsts.PA_STATUS.CANCELSUCESS_USER.getCode())) {
                            row.createCell(col++).setCellValue("취소성공(사용자)");
                        } else if(vo.getPa_status().equals(MimsConsts.PA_STATUS.CANCELFAIL_USER.getCode())) {
                            row.createCell(col++).setCellValue("취소실패(사용자)");
                        } else if(vo.getPa_status().equals(MimsConsts.PA_STATUS.CANCELSUCESS_ADMIN.getCode())) {
                            row.createCell(col++).setCellValue("취소성공(관리자)");
                        } else if(vo.getPa_status().equals(MimsConsts.PA_STATUS.CANCELFAIL_ADMIN.getCode())) {
                            row.createCell(col++).setCellValue("취소실패(관리자)");
                        } else {
                            row.createCell(col++).setCellValue("정의되지않음");
                        }
                        row.createCell(col++).setCellValue(vo.getPa_key());
                        row.createCell(col++).setCellValue(vo.getPa_reg_dt());

                        if(StringUtils.defaultString(loginUser).equals("admin") || StringUtils.defaultString(loginUser).equals("amims")) {
                            row.createCell(col++).setCellValue(vo.getPa_mod_dt());
                            row.createCell(col++).setCellValue(vo.getPa_flag());
                            row.createCell(col++).setCellValue(vo.getPa_message());
                            row.createCell(col++).setCellValue(vo.getFailover_div());
                        }
                    }
                });
            }
        }

		byte[] bytes = sheet.end().make();

		response.setHeader("Content-Disposition", "attachment; filename=MultiPayMent_IPTV_" + GlobalCom.getTodayFormat() + ".xlsx");
		response.setContentLength(bytes.length);
		response.setContentType("application/vnd.ms-excel");

		logger.debug("[MultiPaymentController][downloadExcelFile][END]");
		
		return bytes;
	}
    
    /**
     * MultiPayment EXCEL 다운로드
     */
    @ResponseBody
	@RequestMapping(value = "/admin/multipayment/downloadExcelFileTv", method = RequestMethod.GET)
	public byte[] downloadExcelFilePpm(
			@RequestParam(value = "findName", required = false, defaultValue = "") String findName,
            @RequestParam(value = "findValue", required = false, defaultValue = "") String findValue,
            @RequestParam(value = "status", required = false, defaultValue = "") String status,
            @RequestParam(value = "paType", required = false, defaultValue = "") String paType,
            @RequestParam(value = "startDt", required = false, defaultValue = "") String startDt,
            @RequestParam(value = "endDt", required = false, defaultValue = "") String endDt,
            HttpServletRequest request,
            HttpServletResponse response,
			Model model) throws Exception {
    	
    	logger.info("[MultiPaymentController][downloadExcelFileTv][START]");
        final String loginUser = CookieUtil.getCookieUserID(request);

		String domain = GlobalCom.getProperties(SmartUXProperties.getProperty("filepath.suxm.common"), "smartux.domain.https");
		model.addAttribute("domain_https", domain);
		
		findName = HTMLCleaner.clean(findName);
        findValue = HTMLCleaner.clean(findValue);

        
        PtUxPaymentPpmVo ptUxPaymentPpmVo = new PtUxPaymentPpmVo();
        List<PtUxPaymentPpmVo> list = new ArrayList<PtUxPaymentPpmVo>();
        
        
        ptUxPaymentPpmVo.setFindName(StringUtils.defaultIfEmpty(findName, "PA_KEY"));
        ptUxPaymentPpmVo.setFindValue(StringUtils.defaultString(findValue));

        
        if (!startDt.isEmpty() && !endDt.isEmpty()) {
        	
            String startDt_mod = startDt.replace("-", "");
            startDt_mod = startDt_mod + "000000";
            String endDt_mod = endDt.replace("-", "");
            endDt_mod = endDt_mod + "999999";
        	
        	ptUxPaymentPpmVo.setStart_dt(startDt_mod);
        	ptUxPaymentPpmVo.setEnd_dt(endDt_mod);
          
        } 
        
        if(findName.equals("PA_KEY")) {
        	ptUxPaymentPpmVo.setPa_key(findValue);
        }else if(findName.equals("SA_ID")) {
        	ptUxPaymentPpmVo.setSa_id(findValue);
        }   
    	

        List<Object> header = new ArrayList<Object>();
        List<List<Object>> data = new ArrayList<List<Object>>();

        String[] headerAry = SmartUXProperties.getProperty("svod.excel.header").split("\\|");

        ExcelWorkBookFactory.ExcelSheetFactory sheet = ExcelWorkBookFactory.create(10000).sheet("다회선 vod 구매내역");
        sheet.headers(1, headerAry);


        int pageCount = 1;
        while(true) {
        	ptUxPaymentPpmVo.setPageNum(pageCount++);
        	ptUxPaymentPpmVo.setPageSize(1000);
        	
        	if(findName.equals("PA_KEY")) {	
        		list = multipaymentService.getPtUxPaymentPpmListByPakey(ptUxPaymentPpmVo);
        		logger.info("[MultiPaymentController][downloadExcelFileTv][START] list size : "+ list.size());
        	}else if(findName.equals("SA_ID")){
        		list = multipaymentService.getPtUxPaymentPpmListBySaid(ptUxPaymentPpmVo);
        		logger.info("[MultiPaymentController][downloadExcelFileTv][START] list size : "+ list.size());
        	}else if(findName.equals("DATE_ONLY")){
        		list = multipaymentService.getPtUxPaymentPpmListByDate(ptUxPaymentPpmVo);
        		logger.info("[MultiPaymentController][downloadExcelFileTv][START] list size : "+ list.size());
        	}
        	
            if(list == null || list.size() == 0) {
                break;
            } else {
                sheet.rowCellValues(list, new ExcelWorkBookFactory.CellValueRef<PtUxPaymentPpmVo>() {
                    @Override
                    public void setRowData(Row row, PtUxPaymentPpmVo vo) {
                        int col = 1;
                        row.createCell(col++).setCellValue(vo.getSa_id()); 
                        row.createCell(col++).setCellValue(vo.getProduct_code());
                        row.createCell(col++).setCellValue(vo.getProduct_name());
                        
                        if(vo.getPa_status().equals(MimsConsts.PA_STATUS.FINISH.getCode())) {
                            row.createCell(col++).setCellValue("성공");
                        } else if(vo.getPa_status().equals(MimsConsts.PA_STATUS.START.getCode())) {
                            row.createCell(col++).setCellValue("대기중");
                        } else if(vo.getPa_status().equals(MimsConsts.PA_STATUS.CANCEL.getCode())) {
                            row.createCell(col++).setCellValue("취소");
                        } else if(vo.getPa_status().equals(MimsConsts.PA_STATUS.ERROR.getCode())) {
                            row.createCell(col++).setCellValue("실패");
                        } else if(vo.getPa_status().equals(MimsConsts.PA_STATUS.APPROVE.getCode())) {
                            row.createCell(col++).setCellValue("진행중");
                        } else if(vo.getPa_status().equals(MimsConsts.PA_STATUS.CANCELFAIL.getCode())) {
                            row.createCell(col++).setCellValue("취소실패");
                        } else {
                            row.createCell(col++).setCellValue("정의되지않음");
                        }
                        
                        row.createCell(col++).setCellValue(vo.getPa_key());
                        row.createCell(col++).setCellValue(vo.getPa_reg_dt());

                        if(StringUtils.defaultString(loginUser).equals("admin") || StringUtils.defaultString(loginUser).equals("amims")) {
                            row.createCell(col++).setCellValue(vo.getPa_mod_dt());
                            row.createCell(col++).setCellValue(vo.getPa_flag());
                            row.createCell(col++).setCellValue(vo.getPa_message());
                        }
                    }
                });
            }
        }

		byte[] bytes = sheet.end().make();

		response.setHeader("Content-Disposition", "attachment; filename=MultiPayMent_PPM_" + GlobalCom.getTodayFormat() + ".xlsx");
		response.setContentLength(bytes.length);
		response.setContentType("application/vnd.ms-excel");

		logger.debug("[MultiPaymentController][downloadExcelFile][END]");
		
		return bytes;
	}
    
    /**
     * 결제 취소 (청구서)
     * @param ptYear
     * @param ptMonth
     * @param pa_key
     * @param pa_type (S:청구서)
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/admin/multipayment/liveCancel", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> liveCancel(
			@RequestParam(value = "sa_id", required = false, defaultValue = "") String sa_id,
			@RequestParam(value = "stb_mac", required = false, defaultValue = "") String stb_mac,
			@RequestParam(value = "m_type", required = false, defaultValue = "") String m_type,
			@RequestParam(value = "pa_key", required = false, defaultValue = "") String pa_key,
			@RequestParam(value = "cmd", required = false, defaultValue = "") String cmd,
			HttpServletRequest request) throws Exception {
		
		String cookieID = CookieUtil.getCookieUserID(request);
		logger.info("[MultiPaymentController][liveCancel][START][pa_key: " + pa_key + "][loginUserId: " + cookieID + "]");
		
		String resultCode = "";
		String resultMsg = "";
		String jsonStr ="";
		String result ="";
		String errorCode="";

		try {
			String host = SmartUXProperties.getProperty("mims.domain.https");
			String url = SmartUXProperties.getProperty("simplepaytv.cancel.url");
			String encoding = StringUtils.defaultIfEmpty(SmartUXProperties.getProperty("mims.domain.https.encoding"), "UTF-8");
			
			Map<String,String> header = new HashMap<String,String>();
			header.put("Accept", "application/json");
			
			Map<String, String> params = new HashMap<String, String>();
			params.put("sa_id", sa_id);
			params.put("stb_mac", stb_mac);
			params.put("m_type", m_type ); 
			params.put("pa_key", pa_key);
			params.put("cmd", StringUtils.defaultIfEmpty(cmd, "admin"));
			logger.info("[MultiPaymentController][liveCancel][구매취소 호출 url][" + url + "]");
			
			jsonStr = HttpCommUtil.callPostHttpClient(host+url, header, params, encoding);
			logger.info("[MultiPaymentController][liveCancel][구매취소 호출 성공][" + jsonStr + "]");

			JSONObject jsonObject = (JSONObject) JSONValue.parse(jsonStr);
						
			if(jsonObject != null  ){
				JSONObject cancelResult = (JSONObject) jsonObject.get("result");
				
				if(cancelResult != null){
					resultCode = GlobalCom.getJsonString(cancelResult, "flag");
					resultMsg = GlobalCom.getJsonString(cancelResult, "message");
				} else {
					cancelResult = (JSONObject) jsonObject.get("error");
					
					resultCode = GlobalCom.getJsonString(cancelResult, "flag");
					resultMsg = GlobalCom.getJsonString(cancelResult, "message");
					errorCode = GlobalCom.getJsonString(cancelResult, "errorCode");
				}
				
			}
		} catch (Exception e) {
			ExceptionHandler handler = new ExceptionHandler(e);
			resultCode = handler.getFlag();
			resultMsg = handler.getMessage();
			throw e;
		}
		finally {
			logger.info("[MultiPaymentController][liveCancel][END][pa_key: " + pa_key + "][loginUserId: " + cookieID + "][flag: " + resultCode + "][message: " + resultMsg + "]");
		}

		result = "{\"flag\" : \"" + resultCode + "\", \"message\" : \"" + resultMsg + "\", \"errorCode\" : \"" + errorCode + "\"}";

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(result, responseHeaders, HttpStatus.CREATED);
	}
}