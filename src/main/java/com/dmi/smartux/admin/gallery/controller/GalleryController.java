package com.dmi.smartux.admin.gallery.controller;

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
import com.dmi.smartux.admin.gallery.service.GalleryService;
import com.dmi.smartux.admin.gallery.vo.DeleteGalleryProcVo;
import com.dmi.smartux.admin.gallery.vo.GalleryCateListParamVo;
import com.dmi.smartux.admin.gallery.vo.GalleryOrderListResultVo;
import com.dmi.smartux.admin.gallery.vo.GalleryProcResultVo;
import com.dmi.smartux.admin.gallery.vo.InsertGalleryProcVo;
import com.dmi.smartux.admin.gallery.vo.SearchGalleryResultVo;
import com.dmi.smartux.admin.gallery.vo.SelectGalleryDetailVo;
import com.dmi.smartux.common.exception.ExceptionHandler;
import com.dmi.smartux.common.exception.SmartUXException;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.CookieUtil;
import com.dmi.smartux.common.vo.CUDResult;

@Controller
public class GalleryController {
	
	private final Log logger = LogFactory.getLog(this.getClass());
	
	private final Log applyLogger = LogFactory.getLog("adminApplyCacheHistroy");
	
	@Autowired
	GalleryService service;

	@Autowired
	CommonModuleService commonModuleService;

	private ObjectMapper om = new ObjectMapper();
	
	private String imgPath = "gallery/image/";
	private String thumbnailPath = "gallery/thumb/";
	private String repsImgPath = "gallery/reps/";
	
	/**
	 * 갤러리 마스터정보 목록
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/gallery/galleryCateList", method = RequestMethod.GET)
    public String getGalleryCateList(
            Model model) throws Exception {
    	
    	logger.info("[getGalleryCateList]");
//    	model.addAttribute("imgPath", imgPath);
//    	model.addAttribute("repsImgPath", repsImgPath);
//    	model.addAttribute("thumbnailPath", thumbnailPath);
    	int imagefile = Integer.parseInt(StringUtils.defaultIfEmpty(SmartUXProperties.getProperty("gallery.imagefile.size"), "20480000")) / 1024;
    	int thumbnailfile = Integer.parseInt(StringUtils.defaultIfEmpty(SmartUXProperties.getProperty("gallery.thumbfile.size"), "20480000")) / 1024;
    	int repsfile = Integer.parseInt(StringUtils.defaultIfEmpty(SmartUXProperties.getProperty("gallery.repsfile.size"), "20480000")) / 1024;
    	
    	
    	model.addAttribute("imgSize", imagefile);
    	model.addAttribute("thumbSize", thumbnailfile);
    	model.addAttribute("repsSize", repsfile);
    	
        return "/admin/gallery/galleryList";
    }

	/**
	 * 갤러리 리스트 조회
	 * @param parent
	 * @param request
	 * @return
	 * @throws Exception
	 */	
	@RequestMapping(value = "/admin/gallery/searchGalleryProc", method = RequestMethod.POST)
    public ResponseEntity<String> searchGalleryProc(
			@RequestParam(value = "root", required = false) String root,
			@RequestParam(value = "view_type", required = false, defaultValue="A") String view_type,
			@RequestParam(value = "pop_type", required = false, defaultValue="") String pop_type,
			HttpServletRequest request) throws Exception {

    	logger.info("[searchGalleryProc][START]["+root+"]["+view_type+"]");
	    List<SearchGalleryResultVo> trees = new ArrayList<SearchGalleryResultVo>();
    	try{
    		GalleryCateListParamVo gallerycatelistparamVo = new GalleryCateListParamVo();
    		gallerycatelistparamVo.setView_type(view_type);
    		gallerycatelistparamVo.setPop_type(pop_type);
    	    if (null == root || "root".equals(root)) {
        		gallerycatelistparamVo.setGallery_id(null);
    	    } else {
        		gallerycatelistparamVo.setGallery_id(root);
    	    }
	    	trees = service.getGalleryCateList(gallerycatelistparamVo);
    	}catch(java.lang.Exception e){
    		logger.error("[searchGalleryProc][E]["+root+"]["+view_type+"]:" + e.getClass().getName() + ":" + e.getMessage());
    	}
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8"); 

    	logger.info("[searchGalleryProc][END]["+root+"]["+view_type+"]");
		return new ResponseEntity<String>(om.writeValueAsString(trees), responseHeaders, HttpStatus.OK);
	}
	
	/**
	 * 갤러리 상세정보 조회
	 * @param gallery_id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/gallery/getGalleryDetail", method = RequestMethod.GET)
    public ResponseEntity<String> getGalleryDetail(
			@RequestParam(value = "gallery_id", required = true, defaultValue="") String gallery_id,
    		HttpServletRequest request) throws Exception {

    	logger.info("[getGalleryDetail][START]");
    	SelectGalleryDetailVo result;
    	try{
    		result = service.getGalleryDetail(gallery_id);
    		if(null==result) {
    			result = new SelectGalleryDetailVo();
        		result.setFlag(SmartUXProperties.getProperty("flag.beNotData"));
        		result.setMessage(SmartUXProperties.getProperty("message.beNotData"));
    		}else {
    			if(("1".equals(result.getContent_type()) || "3".equals(result.getContent_type())) && !StringUtils.isEmpty(result.getCategory_id()) 
    					&& !StringUtils.isEmpty(result.getAlbum_id())) result.setAlbum_name(
    							commonModuleService.getContentsView(result.getCategory_id(), result.getAlbum_id()).getAlbum_name()
    							);
	    		result.setFlag(SmartUXProperties.getProperty("flag.success"));
	    		result.setMessage(SmartUXProperties.getProperty("message.success"));
    		}
    	}catch(java.lang.Exception e){
    		ExceptionHandler eh = new ExceptionHandler(e);
    		result = new SelectGalleryDetailVo();
    		result.setFlag(eh.getFlag());
    		result.setMessage(eh.getMessage());
    		logger.error("[getGalleryDetail][E]:" + e.getClass().getName() + ":" + e.getMessage());
    	}
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8"); 

    	logger.info("[getGalleryDetail][END]");
		return new ResponseEntity<String>(om.writeValueAsString(result), responseHeaders, HttpStatus.OK);
    }
	
	/**
	 * 갤러리 저장/수정
	 * @param proc_type
	 * @param gallery_id
	 * @param p_gallery_id
	 * @param gallery_type
	 * @param gallery_name
	 * @param gallery_image
	 * @param gallery_image_old
	 * @param use_yn
	 * @param content_type
	 * @param gallery_thumbnail
	 * @param gallery_thumbnail_old
	 * @param content_info
	 * @param simple_content
	 * @param contents_source
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/gallery/insertGalleryProc", method = RequestMethod.POST)
    public ResponseEntity<String> insertGalleryProc(
			@RequestParam(value = "proc_type", required = true, defaultValue = "") String proc_type,
			@RequestParam(value = "gallery_id", required = true, defaultValue = "") String gallery_id,
			@RequestParam(value = "p_gallery_id", required = false, defaultValue = "") String p_gallery_id,
			@RequestParam(value = "gallery_type", required = false, defaultValue = "") String gallery_type,
			@RequestParam(value = "gallery_name", required = false, defaultValue = "") String gallery_name,
			@RequestParam(value = "gallery_image", required = false, defaultValue = "") MultipartFile gallery_image,
			@RequestParam(value = "gallery_image_old", required = false, defaultValue = "") String gallery_image_old,
			@RequestParam(value = "gallery_reps_image", required = false, defaultValue = "") MultipartFile gallery_reps_image,
			@RequestParam(value = "gallery_reps_image_old", required = false, defaultValue = "") String gallery_reps_image_old,
			@RequestParam(value = "use_yn", required = false, defaultValue = "") String use_yn,
			@RequestParam(value = "content_type", required = false, defaultValue = "") String content_type,
			@RequestParam(value = "gallery_thumbnail", required = false, defaultValue = "") MultipartFile gallery_thumbnail,
			@RequestParam(value = "gallery_thumbnail_old", required = false, defaultValue = "") String gallery_thumbnail_old,
			@RequestParam(value = "content_info", required = false, defaultValue = "") String content_info,
			@RequestParam(value = "simple_content", required = false, defaultValue = "") String simple_content,
			@RequestParam(value = "contents_source", required = false, defaultValue = "") String contents_source,
			HttpServletRequest request) throws Exception {

    	logger.info("[insertGalleryProc]["+proc_type+"][START]");
    	GalleryProcResultVo result = new GalleryProcResultVo();
		
    	try{
    		String cookieID = CookieUtil.getCookieUserID(request);
    		//gallery_id = HTMLCleaner.clean(gallery_id);
    		//p_gallery_id = HTMLCleaner.clean(p_gallery_id);
    		//gallery_name = HTMLCleaner.clean(gallery_name);
    		//simple_content = HTMLCleaner.clean(simple_content);
    		//contents_source = HTMLCleaner.clean(contents_source);
    		
    		InsertGalleryProcVo insertgalleryprocVo = new InsertGalleryProcVo();
    		insertgalleryprocVo.setGallery_id(gallery_id);
    		insertgalleryprocVo.setP_gallery_id(p_gallery_id);
    		insertgalleryprocVo.setGallery_name(gallery_name);
    		insertgalleryprocVo.setGallery_type(gallery_type);
    		insertgalleryprocVo.setReg_id(cookieID);
    		insertgalleryprocVo.setUse_yn(use_yn);
    		insertgalleryprocVo.setContent_type(content_type);
    		insertgalleryprocVo.setSimple_content(simple_content);
    		insertgalleryprocVo.setContents_source(contents_source);

    		String fileName = "";
	    	String imageName = gallery_image_old;
    		if (gallery_image.getSize() != 0L) {
    			checkFileSize(gallery_image, 1);
				fileName = gallery_image.getOriginalFilename();
				String ext = FilenameUtils.getExtension(fileName); // 확장자 구하기
				ext = ext.toLowerCase();
	
				imageName = Long.toString(System.currentTimeMillis()) + "." + ext; // 시스템타임.확장자 구조로 한다
				String newFileName = SmartUXProperties.getProperty("gallery.img.path") + imageName;
				
				imageName = imgPath + imageName;
	
				File file = new File(newFileName);
				gallery_image.transferTo(file);
			}
    		insertgalleryprocVo.setImage(imageName);
    		
    		String repsImageName = gallery_reps_image_old;
    		if (gallery_reps_image.getSize() != 0L) {
    			checkFileSize(gallery_reps_image, 2);
				fileName = gallery_reps_image.getOriginalFilename();
				String ext = FilenameUtils.getExtension(fileName); // 확장자 구하기
				ext = ext.toLowerCase();
	
				repsImageName = Long.toString(System.currentTimeMillis()) + "." + ext; // 시스템타임.확장자 구조로 한다
				String newFileName = SmartUXProperties.getProperty("gallery.repsImg.path") + repsImageName;
				
				repsImageName = repsImgPath + repsImageName;
	
				File file = new File(newFileName);
				gallery_reps_image.transferTo(file);
			}
    		insertgalleryprocVo.setReps_image(repsImageName);
    		
	    	String thumbnailName = gallery_thumbnail_old;
    		if (gallery_thumbnail.getSize() != 0L) {
    			checkFileSize(gallery_thumbnail, 3);
				fileName = gallery_thumbnail.getOriginalFilename();
				String ext = FilenameUtils.getExtension(fileName); // 확장자 구하기
				ext = ext.toLowerCase();
	
				thumbnailName = Long.toString(System.currentTimeMillis()) + "." + ext; // 시스템타임.확장자 구조로 한다
				String newFileName = SmartUXProperties.getProperty("gallery.thumbnail.path") + thumbnailName;
				
				thumbnailName = thumbnailPath + thumbnailName;
	
				File file = new File(newFileName);
				gallery_thumbnail.transferTo(file);
			}
    		insertgalleryprocVo.setThumbnail(thumbnailName);
    		
    		if(!StringUtils.isEmpty(content_info)) {
    			String[] arrContentInfo = content_info.split("\\|\\|");
    			if(arrContentInfo.length >= 2) {
		    		insertgalleryprocVo.setCategory_id(arrContentInfo[0]);
		    		insertgalleryprocVo.setAlbum_id(arrContentInfo[1]);
    			}
    		}
    		
    		if("1".equals(proc_type)) { //등록
    			service.insertGalleryProc(insertgalleryprocVo);
    		} else { //수정
    			service.updateGalleryProc(insertgalleryprocVo);
    		}
    		
    		result.setFlag(SmartUXProperties.getProperty("flag.success"));
    		result.setMessage(SmartUXProperties.getProperty("message.success"));
    		result.setGallery_id(gallery_id);
    		result.setP_gallery_id(p_gallery_id);
    		result.setGallery_name(gallery_name);
    		result.setProc_type(proc_type);
    		result.setGallery_type(gallery_type);
    		result.setUse_yn(use_yn);
    	}catch(java.lang.Exception e){
    		ExceptionHandler eh = new ExceptionHandler(e);
    		result.setFlag(eh.getFlag());
    		result.setMessage(eh.getMessage());
    		logger.error("[insertGalleryMstProc]["+proc_type+"][E]:" + e.getClass().getName() + ":" + e.getMessage());
    	}
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/plain; charset=UTF-8"); 

    	logger.info("[insertGalleryProc]["+proc_type+"][END]");
		return new ResponseEntity<String>(om.writeValueAsString(result), responseHeaders, HttpStatus.OK);
    }
	
	/**
	 * 갤러리 삭제
	 * @param gallery_id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/gallery/deleteGalleryProc", method = RequestMethod.GET)
    public ResponseEntity<String> deleteGalleryProc(
			@RequestParam(value = "gallery_id", required = true, defaultValue = "") String gallery_id,
			HttpServletRequest request) throws Exception {

    	logger.info("[deleteGalleryProc]["+gallery_id+"][START]");
    	GalleryProcResultVo result = new GalleryProcResultVo();
		
    	try{
    		String cookieID = CookieUtil.getCookieUserID(request);
    		
    		DeleteGalleryProcVo deletegalleryprocVo = new DeleteGalleryProcVo();
    		deletegalleryprocVo.setGallery_id(gallery_id);
    		deletegalleryprocVo.setMod_id(cookieID);
    		
    		String p_gallery_id = service.deleteGalleryProc(deletegalleryprocVo);
    		result.setFlag(SmartUXProperties.getProperty("flag.success"));
    		result.setMessage(SmartUXProperties.getProperty("message.success"));
    		result.setGallery_id(gallery_id);
    		result.setP_gallery_id(p_gallery_id);
    	}catch(java.lang.Exception e){
    		ExceptionHandler eh = new ExceptionHandler(e);
    		result.setFlag(eh.getFlag());
    		result.setMessage(eh.getMessage());
    		logger.error("[deleteGalleryProc]["+gallery_id+"][E]:" + e.getClass().getName() + ":" + e.getMessage());
    	}
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8"); 

    	logger.info("[deleteGalleryProc]["+gallery_id+"][END]");
		return new ResponseEntity<String>(om.writeValueAsString(result), responseHeaders, HttpStatus.OK);
    }
	
	/**
	 * 갤러리 순서변경을 위한 팝업
	 * @param gallery_id
	 * @param model
	 * @return
	 * @throws Exception
	 */
    @RequestMapping(value = "/admin/gallery/galleryOrderChangePop", method = RequestMethod.GET)
    public String getGalleryOrderChangePop(
            @RequestParam(value = "gallery_id", required = false, defaultValue = "root") String gallery_id,
            @RequestParam(value = "callbak", required = false, defaultValue = "") String callbak,
			HttpServletRequest request,
            Model model) throws Exception {
    	
    	logger.info("[getGalleryOrderChangePop][START]["+gallery_id+"]");
    	List<GalleryOrderListResultVo> galleryorderlistresultVo = service.getGalleryOrderList("root".equals(gallery_id) ? "" : gallery_id);
    	logger.info("[getGalleryOrderChangePop][END]["+gallery_id+"]");

    	model.addAttribute("vo", galleryorderlistresultVo);
    	model.addAttribute("gallery_id", gallery_id);
    	model.addAttribute("callbak", callbak);
    	return "/admin/gallery/galleryOrderChangePop";
    }
    
    /**
     * 갤러리 순서변경 처리
     * @param gallery_ids
     * @param request
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/admin/gallery/galleryOrderChangeProc", method = RequestMethod.POST)
    public ResponseEntity<String> galleryOrderChangeProc(
			@RequestParam(value = "gallery_ids[]", required = true, defaultValue = "") String[] gallery_ids,
			HttpServletRequest request,
            Model model) throws Exception {
    	
    	logger.info("[galleryOrderChangeProc][START]");
    	GalleryProcResultVo result = new GalleryProcResultVo();
		
    	try{
    		String cookieID = CookieUtil.getCookieUserID(request);
    		
    		service.galleryOrderChangeProc(gallery_ids, cookieID);
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

    	logger.info("[galleryOrderChangeProc][END]");
		return new ResponseEntity<String>(om.writeValueAsString(result), responseHeaders, HttpStatus.OK);
    }
    
    /**
     * 카테고리 선택 팝업
     * @param gallery_id
     * @param callbak
     * @param type A : 전체, 그외 : 디렉토리만 노출
     * @param model
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/admin/gallery/choiceGalleryListPop", method = RequestMethod.GET)
    public String choiceGalleryListPop(
            @RequestParam(value = "gallery_id", required = false, defaultValue = "root") String gallery_id,
            @RequestParam(value = "callbak", required = false, defaultValue = "") String callbak,
            @RequestParam(value = "type", required = false, defaultValue = "D") String type,
            Model model, HttpServletRequest request) throws Exception {
    	
    	logger.info("[choiceGalleryListPop]["+gallery_id+"]");
    	if("".equals(gallery_id)) gallery_id="root";
    	if("".equals(type)) type="D";

    	model.addAttribute("gallery_id", gallery_id);
    	model.addAttribute("callbak", callbak);
    	model.addAttribute("type", type);
    	return "/admin/gallery/choiceGalleryListPop";
    }
    
    /**
     * 캐시 즉시적용
     * @param callByScheduler
     * @param request
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/admin/gallery/applyCache", method = RequestMethod.POST)
    public ResponseEntity<String> applyCache(
			@RequestParam(value="callByScheduler",  defaultValue="Y") final  String callByScheduler,
			HttpServletRequest request,
            Model model) throws Exception {
    	
    	final CLog cLog = new CLog(applyLogger, request.getRemoteAddr(), request.getRequestURI(), request.getMethod());
		final String loginUser = CookieUtil.getCookieUserID(request);
		final CUDResult result = new CUDResult();
		
		try {
			cLog.startLog("갤러리 즉시적용 : ",  loginUser);
			final String host = SmartUXProperties.getProperty("cache.sync.public.host");
			final String port = SmartUXProperties.getProperty("cache.sync.public.port");
			
			String fileLockPath = SmartUXProperties.getProperty("GalleryDao.refreshGalleryList.fileLock");
			int waitTime = NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.wait"), 5);
			
			FileProcessLockFactory.start(fileLockPath, waitTime,  new FileProcessFunction("갤러리 즉시적용", ResultCode.ApplyRequestFail) {
				
				@Override
				public void run() throws Exception {
					
					//즉시적용 호출 시 sleep 값 추가 -> DB분리건으로 OGG가 10초 이상 걸리는 경우가 있어 즉시적용 시 반영이 안돼서 추가함
					Thread.sleep(Integer.parseInt(org.apache.commons.lang.StringUtils.defaultIfEmpty(SmartUXProperties.getProperty("apply.cache.call.sleep"), "10000")));
					
					RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
							host, 
							port, 
							SmartUXProperties.getProperty("GalleryDao.refreshGalleryList.url")+"?callByScheduler="+callByScheduler,
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.method"), "POST"), 
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"), 
							MediaType.APPLICATION_JSON.toString(),
							NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.timeout"), 60000)).getResponseBody());
					
					result.setFlag(codeMsg.getCode());
					result.setMessage(codeMsg.getMessage());
					cLog.middleLog("갤러리 즉시적용", "API 호출", codeMsg.getCode(), codeMsg.getMessage());
				}
			});
		} catch (MimsCommonException e) {
			cLog.warnLog("갤러리 즉시적용", "실패", e.getFlag(), e.getMessage(), e.getLocalizedMessage());
			result.setFlag(e.getFlag());
			result.setMessage(e.getMessage());
		} finally {
			cLog.endLog("갤러리 즉시적용", loginUser, result.getFlag(), result.getMessage());
		}
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8"); 
		return new ResponseEntity<String>(om.writeValueAsString(result), responseHeaders, HttpStatus.OK);
    }
	
    /**
     * 파일 사이즈 제한
     * 1 : 고화질이미지, 2 : 대표이미지, 3 : 썸네일이미지
     * @param text_file
     * @param type
     */
	private void checkFileSize(MultipartFile text_file, int type) {
		String fileMaxSize = SmartUXProperties.getProperty("gallery.imagefile.size");
		String imgName = "고화질 이미지";
		if(2==type) {
			fileMaxSize = SmartUXProperties.getProperty("gallery.repsfile.size");
			imgName = "대표 이미지";
		}else if(3==type) {
			fileMaxSize = SmartUXProperties.getProperty("gallery.thumbfile.size");
			imgName = "썸네일 이미지";
		}
		
		if(null != fileMaxSize) {
			long iFileMaxSize = Long.parseLong(fileMaxSize);
			if(text_file.getSize() > iFileMaxSize) throw new SmartUXException(SmartUXProperties.getProperty("flag.file.maxsize.over")
					, SmartUXProperties.getProperty("message.file.maxsize.over",imgName + " : " + fileMaxSize + "Byte 미만"));
		}
	}
	

}
