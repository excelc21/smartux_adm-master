package com.dmi.smartux.admin.keyword.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
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
import org.springframework.web.multipart.MultipartFile;

import com.dmi.commons.consts.ResultCode;
import com.dmi.commons.exception.MimsCommonException;
import com.dmi.commons.factory.FileProcessFunction;
import com.dmi.commons.factory.FileProcessLockFactory;
import com.dmi.commons.logger.CLog;
import com.dmi.commons.model.RestfulCodeMsg;
import com.dmi.commons.utility.HttpClientUtils;
import com.dmi.commons.utility.RestfulUtils;
import com.dmi.smartux.admin.commonMng.service.CommonModuleService;
import com.dmi.smartux.admin.keyword.service.KeywordService;
import com.dmi.smartux.admin.keyword.vo.DeleteKeywordProcVo;
import com.dmi.smartux.admin.keyword.vo.InsertKeywordProcVo;
import com.dmi.smartux.admin.keyword.vo.KeywordOrderListResultVo;
import com.dmi.smartux.admin.keyword.vo.KeywordProcResultVo;
import com.dmi.smartux.admin.keyword.vo.SearchKeywordResultVo;
import com.dmi.smartux.admin.keyword.vo.SelectKeywordDetailVo;
import com.dmi.smartux.common.exception.ExceptionHandler;
import com.dmi.smartux.common.exception.SmartUXException;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.CookieUtil;
import com.dmi.smartux.common.vo.CUDResult;

@Controller
public class KeywordController {
	
	private final Log logger = LogFactory.getLog(this.getClass());
	
	private final Log applyLogger = LogFactory.getLog("adminApplyCacheHistroy");
	
	@Autowired
	KeywordService service;

	@Autowired
	CommonModuleService commonModuleService;

	private ObjectMapper om = new ObjectMapper();
	
	private String imgPath = "keyword/image/";
	
	/**
	 * ????????? ??????????????? ??????
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/keyword/keywordCateList", method = RequestMethod.GET)
    public String getKeywordCateList(
            Model model) throws Exception {
    	
    	logger.info("[getKeywordCateList]");
    	int imagefile = Integer.parseInt(StringUtils.defaultIfEmpty(SmartUXProperties.getProperty("keyword.imagefile.size"), "20480000")) / 1024;
    	
    	model.addAttribute("imgSize", imagefile);
    	
        return "/admin/keyword/keywordList";
    }
	
	/**
	 * ????????? ????????? ??????
	 * @param parent ?????? ???????????? ID
	 * @param request
	 * @return
	 * @throws Exception
	 */	
	@RequestMapping(value = "/admin/keyword/searchKeywordProc", method = RequestMethod.POST)
    public ResponseEntity<String> searchKeywordProc(
			@RequestParam(value = "root", required = false) String root,
			HttpServletRequest request) throws Exception {

    	logger.info("[searchKeywordProc][START]["+root+"]");
	    List<SearchKeywordResultVo> trees = new ArrayList<SearchKeywordResultVo>();
    	try{
    	    if (null == root || "root".equals(root)) {
    	    	trees = service.getKeywordCateList(null);
    	    } else {
    	    	trees = service.getKeywordCateList(root);
    	    }
    	}catch(java.lang.Exception e){
    		logger.error("[searchKeywordProc][E]["+root+"]:" + e.getClass().getName() + ":" + e.getMessage());
    	}
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8"); 

    	logger.info("[searchKeywordProc][END]["+root+"]");
		return new ResponseEntity<String>(om.writeValueAsString(trees), responseHeaders, HttpStatus.OK);
	}	
	
	/**
	 * ????????? ???????????? ??????
	 * @param keyword_id ???????????? ID
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/keyword/getKeywordDetail", method = RequestMethod.GET)
    public ResponseEntity<String> getKeywordDetail(
			@RequestParam(value = "keyword_id", required = true, defaultValue="") String keyword_id,
    		HttpServletRequest request) throws Exception {

    	logger.info("[getKeywordDetail][START]");
    	SelectKeywordDetailVo result;
    	try{
    		result = service.getKeywordDetail(keyword_id);
    		if(null==result) {
    			result = new SelectKeywordDetailVo();
        		result.setFlag(SmartUXProperties.getProperty("flag.beNotData"));
        		result.setMessage(SmartUXProperties.getProperty("message.beNotData"));
    		}else {
	    		result.setFlag(SmartUXProperties.getProperty("flag.success"));
	    		result.setMessage(SmartUXProperties.getProperty("message.success"));
    		}
    	}catch(java.lang.Exception e){
    		ExceptionHandler eh = new ExceptionHandler(e);
    		result = new SelectKeywordDetailVo();
    		result.setFlag(eh.getFlag());
    		result.setMessage(eh.getMessage());
    		logger.error("[getKeywordDetail][E]:" + e.getClass().getName() + ":" + e.getMessage());
    	}
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8"); 

    	logger.info("[getKeywordDetail][END]");
		return new ResponseEntity<String>(om.writeValueAsString(result), responseHeaders, HttpStatus.OK);
    }
	
	/**
	 * ????????? ??????/??????
	 * @param proc_type ?????? / ?????? ??????
	 * @param keyword_id cat_id??? ??????
	 * @param p_keyword_id ????????? cat_id
	 * @param keyword_type D - ????????????(????????????) / C - ?????????(?????????)
	 * @param keyword_name ?????????(????????????)???
	 * @param keyword_image ????????? 1
	 * @param keyword_image_old ????????? 1 ?????? ?????? ???
	 * @param keyword_image2 ????????? 2
	 * @param keyword_image2_old ????????? 2 ?????? ?????? ???
	 * @param use_yn ?????? ??????
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/keyword/insertKeywordProc", method = RequestMethod.POST)
    public ResponseEntity<String> insertKeywordProc(
			@RequestParam(value = "proc_type", required = true, defaultValue = "") String proc_type,
			@RequestParam(value = "keyword_id", required = true, defaultValue = "") String keyword_id,
			@RequestParam(value = "p_keyword_id", required = false, defaultValue = "") String p_keyword_id,
			@RequestParam(value = "keyword_type", required = false, defaultValue = "") String keyword_type,
			@RequestParam(value = "keyword_name", required = false, defaultValue = "") String keyword_name,
			@RequestParam(value = "keyword_image", required = false, defaultValue = "") MultipartFile keyword_image,
			@RequestParam(value = "keyword_image_old", required = false, defaultValue = "") String keyword_image_old,
			@RequestParam(value = "keyword_image2", required = false, defaultValue = "") MultipartFile keyword_image2,
			@RequestParam(value = "keyword_image2_old", required = false, defaultValue = "") String keyword_image2_old,
			@RequestParam(value = "use_yn", required = false, defaultValue = "") String use_yn,
			HttpServletRequest request) throws Exception {

    	logger.info("[insertKeywordProc]["+proc_type+"][START]");
    	KeywordProcResultVo result = new KeywordProcResultVo();
		
    	try{
    		String cookieID = CookieUtil.getCookieUserID(request);
    		
    		InsertKeywordProcVo insertkeywordprocVo = new InsertKeywordProcVo();
    		insertkeywordprocVo.setKeyword_id(keyword_id);
    		insertkeywordprocVo.setP_keyword_id(p_keyword_id);
    		insertkeywordprocVo.setKeyword_name(keyword_name);
    		insertkeywordprocVo.setKeyword_type(keyword_type);
    		insertkeywordprocVo.setReg_id(cookieID);
    		insertkeywordprocVo.setUse_yn(use_yn);

    		{
    			String fileName = "";
    			String imageName = keyword_image_old;
    			if (keyword_image.getSize() != 0L) {
    				checkFileSize(keyword_image);
    				fileName = keyword_image.getOriginalFilename();
    				String ext = FilenameUtils.getExtension(fileName); // ????????? ?????????
    				ext = ext.toLowerCase();
    				
    				imageName = Long.toString(System.currentTimeMillis()) + "." + ext; // ???????????????.????????? ????????? ??????
    				String newFileName = SmartUXProperties.getProperty("keyword.img.path") + imageName;
    				
    				imageName = imgPath + imageName;
    				
    				File file = new File(newFileName);
    				keyword_image.transferTo(file);
    			}
    			insertkeywordprocVo.setImage(imageName);
    		}
    		
    		{
        		String fileName = "";
    	    	String imageName = keyword_image2_old;
        		if (keyword_image2.getSize() != 0L) {
        			checkFileSize(keyword_image2);
    				fileName = keyword_image2.getOriginalFilename();
    				String ext = FilenameUtils.getExtension(fileName); // ????????? ?????????
    				ext = ext.toLowerCase();
    	
    				imageName = Long.toString(System.currentTimeMillis()) + "." + ext; // ???????????????.????????? ????????? ??????
    				String newFileName = SmartUXProperties.getProperty("keyword.img.path") + imageName;
    				
    				imageName = imgPath + imageName;
    	
    				File file = new File(newFileName);
    				keyword_image2.transferTo(file);
    			}
        		insertkeywordprocVo.setImage2(imageName);
    		}
    		
    		if("1".equals(proc_type)) { //??????
    			service.insertKeywordProc(insertkeywordprocVo);
    		} else { //??????
    			service.updateKeywordProc(insertkeywordprocVo);
    		}
    		
    		result.setFlag(SmartUXProperties.getProperty("flag.success"));
    		result.setMessage(SmartUXProperties.getProperty("message.success"));
    		result.setKeyword_id(keyword_id);
    		result.setP_keyword_id(p_keyword_id);
    		result.setKeyword_name(keyword_name);
    		result.setProc_type(proc_type);
    		result.setKeyword_type(keyword_type);
    		result.setUse_yn(use_yn);
    	}catch(java.lang.Exception e){
    		ExceptionHandler eh = new ExceptionHandler(e);
    		result.setFlag(eh.getFlag());
    		result.setMessage(eh.getMessage());
    		logger.error("[insertKeywordMstProc]["+proc_type+"][E]:" + e.getClass().getName() + ":" + e.getMessage());
    	}
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/plain; charset=UTF-8"); 

    	logger.info("[insertKeywordProc]["+proc_type+"][END]");
		return new ResponseEntity<String>(om.writeValueAsString(result), responseHeaders, HttpStatus.OK);
    }
	
	/**
	 * ????????? ??????
	 * @param keyword_id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/keyword/deleteKeywordProc", method = RequestMethod.GET)
    public ResponseEntity<String> deleteKeywordProc(
			@RequestParam(value = "keyword_id", required = true, defaultValue = "") String keyword_id,
			HttpServletRequest request) throws Exception {

    	logger.info("[deleteKeywordProc]["+keyword_id+"][START]");
    	KeywordProcResultVo result = new KeywordProcResultVo();
		
    	try{
    		String cookieID = CookieUtil.getCookieUserID(request);
    		
    		DeleteKeywordProcVo deletekeywordprocVo = new DeleteKeywordProcVo();
    		deletekeywordprocVo.setKeyword_id(keyword_id);
    		deletekeywordprocVo.setMod_id(cookieID);
    		
    		String p_keyword_id = service.deleteKeywordProc(deletekeywordprocVo);
    		result.setFlag(SmartUXProperties.getProperty("flag.success"));
    		result.setMessage(SmartUXProperties.getProperty("message.success"));
    		result.setKeyword_id(keyword_id);
    		result.setP_keyword_id(p_keyword_id);
    	}catch(java.lang.Exception e){
    		ExceptionHandler eh = new ExceptionHandler(e);
    		result.setFlag(eh.getFlag());
    		result.setMessage(eh.getMessage());
    		logger.error("[deleteKeywordProc]["+keyword_id+"][E]:" + e.getClass().getName() + ":" + e.getMessage());
    	}
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8"); 

    	logger.info("[deleteKeywordProc]["+keyword_id+"][END]");
		return new ResponseEntity<String>(om.writeValueAsString(result), responseHeaders, HttpStatus.OK);
    }
	
	/**
	 * ????????? ??????????????? ?????? ??????
	 * @param keyword_id
	 * @param model
	 * @return
	 * @throws Exception
	 */
    @RequestMapping(value = "/admin/keyword/keywordOrderChangePop", method = RequestMethod.GET)
    public String getKeywordOrderChangePop(
            @RequestParam(value = "keyword_id", required = false, defaultValue = "root") String keyword_id,
            @RequestParam(value = "callbak", required = false, defaultValue = "") String callbak,
			HttpServletRequest request,
            Model model) throws Exception {
    	
    	logger.info("[getKeywordOrderChangePop][START]["+keyword_id+"]");
    	List<KeywordOrderListResultVo> galleryorderlistresultVo = service.getKeywordOrderList("root".equals(keyword_id) ? "" : keyword_id);
    	logger.info("[getKeywordOrderChangePop][END]["+keyword_id+"]");

    	model.addAttribute("vo", galleryorderlistresultVo);
    	model.addAttribute("keyword_id", keyword_id);
    	model.addAttribute("callbak", callbak);
    	return "/admin/keyword/keywordOrderChangePop";
    }
    
    /**
     * ????????? ???????????? ??????
     * @param keyword_ids
     * @param request
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/admin/keyword/keywordOrderChangeProc", method = RequestMethod.POST)
    public ResponseEntity<String> keywordOrderChangeProc(
			@RequestParam(value = "keyword_ids[]", required = true, defaultValue = "") String[] keyword_ids,
			HttpServletRequest request,
            Model model) throws Exception {
    	
    	logger.info("[keywordOrderChangeProc][START]");
    	KeywordProcResultVo result = new KeywordProcResultVo();
		
    	try{
    		String cookieID = CookieUtil.getCookieUserID(request);
    		
    		service.keywordOrderChangeProc(keyword_ids, cookieID);
    		result.setFlag(SmartUXProperties.getProperty("flag.success"));
    		result.setMessage(SmartUXProperties.getProperty("message.success"));
    	}catch(java.lang.Exception e){
    		ExceptionHandler eh = new ExceptionHandler(e);
    		result.setFlag(eh.getFlag());
    		result.setMessage(eh.getMessage());
    		logger.error("[galleryOrderChangeProc][END]:" + e.getClass().getName() + ":" + e.getMessage());
    	}
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8"); 

    	logger.info("[keywordOrderChangeProc][END]");
		return new ResponseEntity<String>(om.writeValueAsString(result), responseHeaders, HttpStatus.OK);
    }
    
    /**
     * ?????? ????????????
     * @param callByScheduler
     * @param request
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/admin/keyword/applyCache", method = RequestMethod.POST)
    public ResponseEntity<String> applyCache(
			@RequestParam(value="callByScheduler",  defaultValue="Y") final  String callByScheduler,
			HttpServletRequest request,
            Model model) throws Exception {
    	
    	final CLog cLog = new CLog(applyLogger, request.getRemoteAddr(), request.getRequestURI(), request.getMethod());
		final String loginUser = CookieUtil.getCookieUserID(request);
		final CUDResult result = new CUDResult();
		
		try {
			cLog.startLog("????????? ???????????? : ",  loginUser);
			final String host = SmartUXProperties.getProperty("cache.sync.public.host");
			final String port = SmartUXProperties.getProperty("cache.sync.public.port");
			
			String fileLockPath = SmartUXProperties.getProperty("KeywordDao.refreshKeywordList.fileLock");
			int waitTime = NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.wait"), 5);
			
			FileProcessLockFactory.start(fileLockPath, waitTime,  new FileProcessFunction("????????? ????????????", ResultCode.ApplyRequestFail) {
				
				@Override
				public void run() throws Exception {
					
					//???????????? ?????? ??? sleep ??? ?????? -> DB??????????????? OGG??? 10??? ?????? ????????? ????????? ?????? ???????????? ??? ????????? ????????? ?????????
//					Thread.sleep(Integer.parseInt(org.apache.commons.lang.StringUtils.defaultIfEmpty(SmartUXProperties.getProperty("apply.cache.call.sleep"), "10000")));
					
					RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
							host, 
							port, 
							SmartUXProperties.getProperty("KeywordDao.refreshKeywordList.url")+"?callByScheduler="+callByScheduler,
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.method"), "POST"), 
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"), 
							MediaType.APPLICATION_JSON.toString(),
							NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.timeout"), 60000)).getResponseBody());
					
					result.setFlag(codeMsg.getCode());
					result.setMessage(codeMsg.getMessage());
					cLog.middleLog("????????? ????????????", "API ??????", codeMsg.getCode(), codeMsg.getMessage());
				}
			});
		} catch (MimsCommonException e) {
			cLog.warnLog("????????? ????????????", "??????", e.getFlag(), e.getMessage(), e.getLocalizedMessage());
			result.setFlag(e.getFlag());
			result.setMessage(e.getMessage());
		} finally {
			cLog.endLog("????????? ????????????", loginUser, result.getFlag(), result.getMessage());
		}
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8"); 
		return new ResponseEntity<String>(om.writeValueAsString(result), responseHeaders, HttpStatus.OK);
    }
    
    /**
     * ?????? ????????? ??????
     * @param image
     */
	private void checkFileSize(MultipartFile image) {
		String fileMaxSize = SmartUXProperties.getProperty("keyword.imagefile.size");
		String imgName = "?????????";
		
		if(null != fileMaxSize) {
			long iFileMaxSize = Long.parseLong(fileMaxSize);
			if(image.getSize() > iFileMaxSize) throw new SmartUXException(SmartUXProperties.getProperty("flag.file.maxsize.over")
					, SmartUXProperties.getProperty("message.file.maxsize.over",imgName + " : " + fileMaxSize + "Byte ??????"));
		}
	}
}
