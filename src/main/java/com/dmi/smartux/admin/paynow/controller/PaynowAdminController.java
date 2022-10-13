package com.dmi.smartux.admin.paynow.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.multipart.MultipartFile;

import com.dmi.commons.consts.ResultCode;
import com.dmi.commons.exception.MimsCommonException;
import com.dmi.commons.factory.FileProcessFunction;
import com.dmi.commons.factory.FileProcessLockFactory;
import com.dmi.commons.logger.CLog;
import com.dmi.commons.model.RestfulCodeMsg;
import com.dmi.commons.utility.HttpClientUtils;
import com.dmi.commons.utility.RestfulUtils;
import com.dmi.smartux.admin.paynow.service.PaynowAdminService;
import com.dmi.smartux.admin.paynow.vo.PaynowBannerVO;
import com.dmi.smartux.admin.paynow.vo.PaynowReqVO;
import com.dmi.smartux.admin.paynow.vo.PaynowSearchVO;
import com.dmi.smartux.common.exception.ExceptionHandler;
import com.dmi.smartux.common.exception.SmartUXException;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.CookieUtil;
import com.dmi.smartux.common.util.ExcelManager;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.util.HTMLCleaner;
import com.dmi.smartux.common.vo.CUDResult;

import net.sf.json.JSONSerializer;

/**
 * Paynow Info Admin Controller
 *
 * @author 
 */
@Controller
public class PaynowAdminController {
	
	private static final String PAYNOW_BNR_CD = SmartUXProperties.getProperty("code.code9");
	
    @Autowired
    PaynowAdminService mService;

    private final Log applyLogger = LogFactory.getLog("adminApplyCacheHistroy");
    
    /**
     * paynow 배너 즉시적용
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/admin/paynow/bannerApplyCache", method=RequestMethod.POST)
	public ResponseEntity<String> bannerApplyCache(
			HttpServletRequest request, 
			HttpServletResponse response
			) throws Exception {
		
    	final CLog cLog = new CLog(applyLogger, request.getRemoteAddr(), request.getRequestURI(), request.getMethod());
		final String loginUser = CookieUtil.getCookieUserID(request);
		final CUDResult result = new CUDResult();
		
		try {
			cLog.startLog("paynow 배너 즉시적용",  loginUser);
			final String host = SmartUXProperties.getProperty("cache.sync.public.host");
			final String port = SmartUXProperties.getProperty("cache.sync.public.port");
			
			String fileLockPath = SmartUXProperties.getProperty("PaynowDao.refreshBanner.fileLock");
			int waitTime = NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.wait"), 5);
			
			FileProcessLockFactory.start(fileLockPath, waitTime,  new FileProcessFunction("paynow 배너 즉시적용", ResultCode.ApplyRequestFail) {
				
				@Override
				public void run() throws Exception {
					
					//즉시적용 호출 시 sleep 값 추가 -> DB분리건으로 OGG가 10초 이상 걸리는 경우가 있어 즉시적용 시 반영이 안돼서 추가함
					Thread.sleep(Integer.parseInt(org.apache.commons.lang.StringUtils.defaultIfEmpty(SmartUXProperties.getProperty("apply.cache.call.sleep"), "10000")));
					
					RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
							host, 
							port, 
							SmartUXProperties.getProperty("PaynowDao.refreshBanner.url"),
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.method"), "POST"), 
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"), 
							MediaType.APPLICATION_JSON.toString(),
							NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.timeout"), 60000)).getResponseBody());
					
					result.setFlag(codeMsg.getCode());
					result.setMessage(codeMsg.getMessage());
					cLog.middleLog("paynow 배너 즉시적용", "API 호출", codeMsg.getCode(), codeMsg.getMessage());
				}
			});
		} catch (MimsCommonException e) {
			cLog.warnLog("paynow 배너 즉시적용", "실패", e.getFlag(), e.getMessage(), e.getLocalizedMessage());
			result.setFlag(e.getFlag());
			result.setMessage(e.getMessage());
		} finally {
			cLog.endLog("paynow 배너 즉시적용", loginUser, result.getFlag(), result.getMessage());
		}
		
		String resultMsg = "{\"flag\" : \"" + result.getFlag() + "\", \"message\" : \"" + result.getMessage() + "\"}";
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(resultMsg).toString(), responseHeaders, HttpStatus.OK);
	}
    
    
    @RequestMapping(value = "/admin/paynow/list", method = RequestMethod.GET)
    public String getPaynowReqList(
            @RequestParam(value = "findName", required = false, defaultValue = "") String findName,
            @RequestParam(value = "findValue", required = false, defaultValue = "") String findValue,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") String pageSize,
            @RequestParam(value = "pageNum", required = false, defaultValue = "1") String pageNum,
            @RequestParam(value = "status", required = false, defaultValue = "") String status,
            @RequestParam(value = "startDt", required = false, defaultValue = "") String startDt,
            @RequestParam(value = "endDt", required = false, defaultValue = "") String endDt,
            Model model) throws Exception {
        String domain = GlobalCom.getProperties(SmartUXProperties.getProperty("filepath.suxm.common"), "smartux.domain.https");
        model.addAttribute("domain_https", domain);

        findName = HTMLCleaner.clean(findName);
        findValue = HTMLCleaner.clean(findValue);
        pageNum = HTMLCleaner.clean(pageNum);

        PaynowSearchVO paynowSearchVO = new PaynowSearchVO();
        paynowSearchVO.setFindName(StringUtils.defaultIfEmpty(findName, "TID"));
        paynowSearchVO.setFindValue(StringUtils.defaultString(findValue));

        paynowSearchVO.setPageSize(NumberUtils.toInt(pageSize, 10));
        paynowSearchVO.setBlockSize(10);
        paynowSearchVO.setPageNum(GlobalCom.isNumber(Integer.parseInt(pageNum), 1));
        
        paynowSearchVO.setStatus(status);
        
        if (!startDt.isEmpty() && !endDt.isEmpty()) {
	        paynowSearchVO.setStartDt(startDt);
	        paynowSearchVO.setEndDt(endDt);
	        // 검색 조건에서 시작일과 종료일의 년도가 동일한지 체크함   
	        paynowSearchVO.setCompareYear((startDt.substring(0, 4).equals(endDt.substring(0, 4))) ? 1 : 0);
        } else {	// 기본값 (최근 한달이내 거래 조회)
        	paynowSearchVO.setStartDt(GlobalCom.getTodayAddMonth(-1));
	        paynowSearchVO.setEndDt(GlobalCom.getTodayFormat2());
	        paynowSearchVO.setCompareYear((paynowSearchVO.getStartDt().substring(0, 4).equals(paynowSearchVO.getEndDt().substring(0, 4))) ? 1 : 0);
        }

        try {
            List<PaynowReqVO> list = mService.getPaynowReqList(paynowSearchVO);
            paynowSearchVO.setPageCount(mService.getPaynowReqListCount(paynowSearchVO));
            
            model.addAttribute("vo", paynowSearchVO);
            model.addAttribute("list", list);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "admin/paynow/paynowList";
    }
    
    @ResponseBody
	@RequestMapping(value = "/admin/paynow/downloadExcelFile", method = RequestMethod.GET)
	public byte[] downloadResultFile(
			@RequestParam(value = "findName", required = false, defaultValue = "") String findName,
            @RequestParam(value = "findValue", required = false, defaultValue = "") String findValue,
            @RequestParam(value = "status", required = false, defaultValue = "") String status,
            @RequestParam(value = "startDt", required = false, defaultValue = "") String startDt,
            @RequestParam(value = "endDt", required = false, defaultValue = "") String endDt,
			HttpServletResponse response, 
			Model model) throws Exception {
		String domain = GlobalCom.getProperties(SmartUXProperties.getProperty("filepath.suxm.common"), "smartux.domain.https");
		model.addAttribute("domain_https", domain);
		
		findName = HTMLCleaner.clean(findName);
        findValue = HTMLCleaner.clean(findValue);

        PaynowSearchVO paynowSearchVO = new PaynowSearchVO();
        paynowSearchVO.setFindName(StringUtils.defaultIfEmpty(findName, "TID"));
        paynowSearchVO.setFindValue(StringUtils.defaultString(findValue));

        paynowSearchVO.setPageSize(99999);
        paynowSearchVO.setPageNum(1);
        
        paynowSearchVO.setStatus(status);
        
        if (!startDt.isEmpty() && !endDt.isEmpty()) {
	        paynowSearchVO.setStartDt(startDt);
	        paynowSearchVO.setEndDt(endDt);
	        paynowSearchVO.setCompareYear((startDt.substring(0, 4).equals(endDt.substring(0, 4))) ? 1 : 0);
        }

		List<Object> header = new ArrayList<Object>();
		List<List<Object>> data = new ArrayList<List<Object>>();

		String[] headerAry = SmartUXProperties.getProperty("paynow.excel.header").split("\\|");

		for (String str : headerAry) {
			header.add(str);
		}

		List<PaynowReqVO> list = mService.getPaynowReqList(paynowSearchVO);

		for (PaynowReqVO vo : list) {
			List<Object> obj = new ArrayList<Object>();
			obj.add(vo.getM_type());
			obj.add(vo.getSa_id());
			obj.add(vo.getMac());
			obj.add(vo.getCtn());
			obj.add(vo.getTid());
			obj.add(vo.getAmount());
			obj.add(vo.getPkg_yn());
			obj.add(vo.getCat_id());
			obj.add(vo.getAlbum_id());
			obj.add(vo.getAlbum_name());
			obj.add(vo.getApp_type());
			obj.add(vo.getStatus());
			obj.add(vo.getErr_typ());
			obj.add(vo.getErr_cd());
			obj.add(vo.getErr_msg());
			obj.add(vo.getReg_dt());
			data.add(obj);
		}

		ExcelManager excelManager = new ExcelManager(header, data);
		excelManager.setSheetName("Paynow 결제 리스트");
		excelManager.setWidth(6000);
		byte[] bytes = excelManager.makeExcel();

		response.setHeader("Content-Disposition", "attachment; filename=Paynow_IPTV_" + GlobalCom.getTodayFormat() + ".xlsx");
		response.setContentLength(bytes.length);
		response.setContentType("application/vnd.ms-excel");

		return bytes;
	}
    
    @RequestMapping(value = "/admin/paynow/view", method = RequestMethod.GET)
    public String getPaynowReqInfo(
            @RequestParam(value = "findName", required = false, defaultValue = "") String findName,
            @RequestParam(value = "findValue", required = false, defaultValue = "") String findValue,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") String pageSize,
            @RequestParam(value = "pageNum", required = false, defaultValue = "1") String pageNum,
            @RequestParam(value = "status", required = false, defaultValue = "") String status,
            @RequestParam(value = "startDt", required = false, defaultValue = "") String startDt,
            @RequestParam(value = "endDt", required = false, defaultValue = "") String endDt,
            @RequestParam(value = "tid", required = false, defaultValue = "") String tid,
            @RequestParam(value = "ptYear", required = false, defaultValue = "") String ptYear,
            @RequestParam(value = "ptMonth", required = false, defaultValue = "") String ptMonth,
            Model model) throws Exception {
        String domain = GlobalCom.getProperties(SmartUXProperties.getProperty("filepath.suxm.common"), "smartux.domain.https");
        model.addAttribute("domain_https", domain);

        findName = HTMLCleaner.clean(findName);
        findValue = HTMLCleaner.clean(findValue);
        pageNum = HTMLCleaner.clean(pageNum);

        PaynowSearchVO paynowSearchVO = new PaynowSearchVO();
        paynowSearchVO.setFindName(StringUtils.defaultString(findName));
        paynowSearchVO.setFindValue(StringUtils.defaultString(findValue));

        paynowSearchVO.setPageSize(NumberUtils.toInt(pageSize, 10));
        paynowSearchVO.setBlockSize(10);
        paynowSearchVO.setPageNum(GlobalCom.isNumber(Integer.parseInt(pageNum), 1));
        
        paynowSearchVO.setStatus(status);
        paynowSearchVO.setStartDt(startDt);
        paynowSearchVO.setEndDt(endDt);
        
        try {
        	paynowSearchVO.setTid(tid);
            paynowSearchVO.setPt_year(Integer.parseInt(ptYear));
            paynowSearchVO.setPt_month(Integer.parseInt(ptMonth));
        	
            PaynowReqVO view = mService.getPaynowReqInfo(paynowSearchVO);

            model.addAttribute("vo", paynowSearchVO);
            model.addAttribute("view", view);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "admin/paynow/paynowReqInfo";
    }
    
    /**
     * paynow 배너 목록 조회
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/admin/paynow/bannerList", method = RequestMethod.GET)
	public String getBannerList(Model model) throws Exception {
    	
		try {
			List<PaynowBannerVO> bList = mService.getBannerList(PAYNOW_BNR_CD);
           
            model.addAttribute("bList", bList);
        } catch (Exception e) {
            e.printStackTrace();
        }
		
		return "/admin/paynow/bannerList";
	}
    
    /**
     * paynow 배너 등록 화면
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/admin/paynow/addBanner", method = RequestMethod.GET)
	public String addBanner(Model model) throws Exception {	
		return "/admin/paynow/bannerAdd";		
	}
    
    /**
     * paynow 배너 등록 처리
     * @param banner_id
     * @param banner_img
     * @param banner_text
     * @param use_yn
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/admin/paynow/addBannerProc", method = RequestMethod.POST)
    @ResponseBody
	public  ResponseEntity<String> addBannerProc(
			@RequestParam(value="banner_id", required = false, defaultValue = "") String banner_id, 
			@RequestParam(value="banner_img", required = false, defaultValue = "") MultipartFile banner_img,
			@RequestParam(value="banner_text", required = false, defaultValue = "") String banner_text,
			@RequestParam(value="use_yn", required = false, defaultValue = "") String use_yn,
			HttpServletRequest request) throws Exception {	

    	banner_id = HTMLCleaner.clean(banner_id);
    	banner_text = HTMLCleaner.clean(banner_text);
		use_yn = HTMLCleaner.clean(use_yn);
		
		String resultCode = "";
		String resultMessage = "";
		
		try {
			String cookieID = CookieUtil.getCookieUserID(request);
			
			PaynowBannerVO bannerVO = mService.getBanner(PAYNOW_BNR_CD, banner_id);	
			
			if(bannerVO == null) {	
				String img_file_name = "";
				
				if (banner_img.getSize() != 0L) {
					String tmp_file_name = banner_img.getOriginalFilename();
					String ext = tmp_file_name.substring(tmp_file_name.lastIndexOf(".") + 1, tmp_file_name.length()); 	// 확장자 구하기
					img_file_name = Long.toString(System.currentTimeMillis()) + "_bnr." + ext.toLowerCase(); 		// 시스템타임.확장자 구조로 한다
					
					banner_img.transferTo(new File(SmartUXProperties.getProperty("paynow.banner.img.path") + img_file_name));
				}

				mService.addBanner(PAYNOW_BNR_CD, banner_id.trim(), img_file_name + "||" + banner_text, use_yn, cookieID);
				
				resultCode = SmartUXProperties.getProperty("flag.success");
				resultMessage = SmartUXProperties.getProperty("message.success");
			} else {											
				SmartUXException e = new SmartUXException();
				e.setFlag("EXIST_BANNER");
				e.setMessage("");
				throw e;
			}
		} catch(Exception e) {
			e.printStackTrace();
			ExceptionHandler handler = new ExceptionHandler(e);
			resultCode = handler.getFlag();
			resultMessage = handler.getMessage();
		}
		
		String result = "{\"flag\" : \"" + resultCode + "\", \"message\" : \"" + resultMessage + "\"}";
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(result, responseHeaders, HttpStatus.CREATED);
	}
    
    /**
     * paynow 배너 수정화면
     * @param banner_id
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/admin/paynow/updateBanner", method = RequestMethod.GET)
	public String updateBanner(
			@RequestParam(value="banner_id", required = false, defaultValue = "") String banner_id, 
			Model model) throws Exception {	
    	
    	try {
			PaynowBannerVO bannerVO = mService.getBanner(PAYNOW_BNR_CD, banner_id);	

			model.addAttribute("vo", bannerVO);
		} catch(Exception e) {
			e.printStackTrace();
		}
    	
		return "/admin/paynow/bannerUpdate";		
	}
    
    /**
     * paynow 배너 수정 처리
     * @param banner_id
     * @param banner_img
     * @param org_banner_img
     * @param banner_text
     * @param use_yn
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/admin/paynow/updateBannerProc", method = RequestMethod.POST)
	@ResponseBody
    public ResponseEntity<String> updateBannerProc(
			@RequestParam(value="banner_id", required = false, defaultValue = "") String banner_id, 
			@RequestParam(value="banner_img", required = false, defaultValue = "") MultipartFile banner_img,
			@RequestParam(value="org_banner_img", required = false, defaultValue = "") String org_banner_img,
			@RequestParam(value="banner_text", required = false, defaultValue = "") String banner_text,
			@RequestParam(value="use_yn", required = false, defaultValue = "") String use_yn,
			HttpServletRequest request) throws Exception {	
    	
    	banner_id = HTMLCleaner.clean(banner_id);
    	banner_text = HTMLCleaner.clean(banner_text);
		use_yn = HTMLCleaner.clean(use_yn);
		
		String resultCode = "";
		String resultMessage = "";
		
		try {
			String cookieID = CookieUtil.getCookieUserID(request);
			
			String img_file_name = "";
			
			if (banner_img.getSize() != 0L) {
				//기존 배너 이미지 파일 삭제
				if (!StringUtils.isEmpty(org_banner_img)) {
					mService.deleteImageFile(org_banner_img);
				}
				
				String tmp_file_name = banner_img.getOriginalFilename();
				String ext = tmp_file_name.substring(tmp_file_name.lastIndexOf(".") + 1, tmp_file_name.length()); 	// 확장자 구하기
				img_file_name = Long.toString(System.currentTimeMillis()) + "_bnr." + ext.toLowerCase(); 		// 시스템타임.확장자 구조로 한다
				
				banner_img.transferTo(new File(SmartUXProperties.getProperty("paynow.banner.img.path") + img_file_name));
			} else {
				if (!StringUtils.isEmpty(org_banner_img)) {
					img_file_name = org_banner_img;
				}
			}
									
			mService.updateBanner(PAYNOW_BNR_CD, banner_id, img_file_name + "||" + banner_text, use_yn, cookieID);
			
			resultCode = SmartUXProperties.getProperty("flag.success");
			resultMessage = SmartUXProperties.getProperty("message.success");
		} catch(Exception e) {
			ExceptionHandler handler = new ExceptionHandler(e);
			resultCode = handler.getFlag();
			resultMessage = handler.getMessage();
		}
		
		String result = "{\"flag\" : \"" + resultCode + "\", \"message\" : \"" + resultMessage + "\"}";
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(result, responseHeaders, HttpStatus.CREATED);
	}
    
    /**
     * paynow 배너 삭제 처리
     * @param banner_id
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/admin/paynow/deleteBannerProc", method = RequestMethod.POST)
    @ResponseBody
   	public  ResponseEntity<String> deleteBannerProc(
   			@RequestParam(value="banner_id", required = false, defaultValue = "") String banner_id, 
   			HttpServletRequest request) throws Exception {	
       		
   		String resultCode = "";
   		String resultMessage = "";
   		
   		try {					
   			mService.deleteBanner(PAYNOW_BNR_CD, banner_id);
   			
   			resultCode = SmartUXProperties.getProperty("flag.success");
   			resultMessage = SmartUXProperties.getProperty("message.success");
   		} catch(Exception e) {
   			ExceptionHandler handler = new ExceptionHandler(e);
   			resultCode = handler.getFlag();
   			resultMessage = handler.getMessage();
   		}
   		
   		String result = "{\"flag\" : \"" + resultCode + "\", \"message\" : \"" + resultMessage + "\"}";
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(result, responseHeaders, HttpStatus.CREATED);
   	}
}
