package com.dmi.smartux.admin.poster.controller;

import java.io.File;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
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
import org.springframework.web.multipart.MultipartFile;

import com.dmi.commons.logger.CLog;
import com.dmi.smartux.admin.poster.service.PosterAdminService;
import com.dmi.smartux.admin.poster.vo.PosterSearchVO;
import com.dmi.smartux.admin.poster.vo.PosterVO;
import com.dmi.smartux.common.exception.ExceptionHandler;
import com.dmi.smartux.common.exception.SmartUXException;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.CookieUtil;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.util.HTMLCleaner;

@Controller
public class PosterAdminController {
	
	private final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	PosterAdminService service;
	
	/**
	 * 전용포스터 목록 조회 화면
	 * @param model	Model 객체
	 * @return 전용포스터 목록 조회 화면 URL
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/poster/getPosterList", method=RequestMethod.GET)
	public String getPosterList(
			HttpServletRequest request,
			@RequestParam(value = "findName", required = false, defaultValue = "") String findName,
            @RequestParam(value = "findValue", required = false, defaultValue = "") String findValue,
            @RequestParam(value = "findServiceType", required = false, defaultValue = "") String findServiceType,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") String pageSize,
            @RequestParam(value = "pageNum", required = false, defaultValue = "1") String pageNum,
            Model model) throws Exception{
		
		final CLog cLog = new CLog(logger, request.getRemoteAddr(), request.getRequestURI(), request.getMethod());
		final String loginUser = CookieUtil.getCookieUserID(request);
		
		cLog.startLog("전용포스터 조회", loginUser);
		
		findName = HTMLCleaner.clean(findName);
        findValue = HTMLCleaner.clean(findValue);
        findServiceType = HTMLCleaner.clean(findServiceType);
        pageNum = HTMLCleaner.clean(pageNum);
        pageSize = HTMLCleaner.clean(pageSize);
        
        PosterSearchVO posterSearchVo = new PosterSearchVO();
        posterSearchVo.setFindName(StringUtils.defaultIfEmpty(findName, "ALBUM_ID"));
        posterSearchVo.setFindValue(StringUtils.defaultString(findValue));
        posterSearchVo.setFindServiceType(StringUtils.defaultString(findServiceType));
        
        posterSearchVo.setPageSize(NumberUtils.toInt(pageSize, 10));
        posterSearchVo.setBlockSize(10);
        posterSearchVo.setPageNum(GlobalCom.isNumber(Integer.parseInt(pageNum), 1));

        int resultSize = service.getPosterListCount(posterSearchVo);
        List<PosterVO> result = service.getPosterList(posterSearchVo);
		
        posterSearchVo.setPageCount(resultSize);
        
        model.addAttribute("vo", posterSearchVo);
		model.addAttribute("result", result);
		
		cLog.endLog("전용포스터 조회", loginUser);
		
		return "/admin/poster/getPosterList";
	}
	
	/**
	 * 전용포스터 등록/수정 및 조회 화면
	 * @param service_type
	 * @param albumId
	 * @param albumName
	 * @param w_fileName
	 * @param h_fileName
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/poster/viewPoster", method=RequestMethod.GET)
	public String insertPoster(
			@RequestParam(value="service_type", required=false) String serviceType,
			@RequestParam(value="album_id", required=false) String albumId,
			@RequestParam(value="album_name", required=false) String albumName,
			@RequestParam(value="w_file_name", required=false) String w_fileName,
			@RequestParam(value="h_file_name", required=false) String h_fileName,
			@RequestParam(value="category_gb", required=false) String category_gb,
			Model model) throws Exception{
		
		PosterVO result = new PosterVO();

		if(!GlobalCom.isEmpty(albumId)){
			result.setService_type(serviceType);
			result.setAlbum_id(albumId);
			result.setAlbum_name(URLDecoder.decode(albumName, "UTF-8"));
			result.setWidth_img(w_fileName);
			result.setHeight_img(h_fileName);
		}
		
		model.addAttribute("result", result);
		model.addAttribute("category_gb", category_gb);
		model.addAttribute("imgDir", SmartUXProperties.getProperty("panel.imageserver.url"));
		
		return "/admin/poster/viewPoster";
	}
	
	/**
	 * 전용포스터 등록
	 * @param request
	 * @param wFile
	 * @param hFile
	 * @param w_fileName
	 * @param h_fileName
	 * @param serviceType
	 * @param albumId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/poster/insertPoster", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> insertPosterProc(
			HttpServletRequest request, 
			 @RequestParam(value="w_file", required=false) MultipartFile wFile,
			 @RequestParam(value="h_file", required=false) MultipartFile hFile,
			 @RequestParam(value="w_file_name", required=false) String w_fileName,
			 @RequestParam(value="h_file_name", required=false) String h_fileName,
			 @RequestParam(value="service_type", required=false) String serviceType,
			 @RequestParam(value="album_id", required=false) String albumId
			 ) throws Exception {
		
		final CLog cLog = new CLog(logger, request.getRemoteAddr(), request.getRequestURI(), request.getMethod());
		final String loginUser = CookieUtil.getCookieUserID(request);
		
		ObjectMapper om = new ObjectMapper();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");  
		
		cLog.startLog("전용포스터 등록", loginUser);
		
		Map<String, String> resultMap = new HashMap<String, String>();
		
		String dirImg = SmartUXProperties.getProperty("panel.imgupload.dir");

		PosterVO vo = new PosterVO();
		vo = service.getSelectPoster(albumId, serviceType);
		if(null!=vo) {
			resultMap.put("code", "DUPLICATION POSTER ERROR");
			resultMap.put("msg","동일한 전용 포스터가 존재합니다.");
			return new ResponseEntity<String>(om.writeValueAsString(resultMap), responseHeaders, HttpStatus.OK);
		}
		
		if(StringUtils.isNotBlank(dirImg)){
			File dirChk = new File(dirImg);
			if(!dirChk.exists()) {
				dirChk.mkdirs();
			}
			
			try{
				//가로이미지 확장자
				if(wFile.getSize() != 0L){
					checkFileSize(wFile, 2);
					String wFileName = wFile.getOriginalFilename();
					
					if(StringUtils.isNotBlank(wFileName)){
						String w_ext = wFileName.substring(wFileName.lastIndexOf(".") + 1, wFileName.length());
						
						if(StringUtils.isNotBlank(albumId)){
							w_fileName = albumId + "W" + GlobalCom.getTodayFormat4_24()+"."+w_ext;
							
							File w_imgFile= new File(dirImg+"/"+w_fileName);
							wFile.transferTo(w_imgFile);
							
							resultMap.put("w_resultCode", "0000");
							resultMap.put("w_fileName", w_fileName);
						}
					}
				}else{
					if("delete".equals(w_fileName)){
						resultMap.put("w_resultCode", "1000");
						w_fileName=null;
					}
				}
			}catch(Exception e){
				ExceptionHandler handler = new ExceptionHandler(e);
				if(handler.getFlag().equals(SmartUXProperties.getProperty("flag.file.maxsize.over"))){
					resultMap.put("code", handler.getFlag());
					resultMap.put("msg",handler.getMessage());
					return new ResponseEntity<String>(om.writeValueAsString(resultMap), responseHeaders, HttpStatus.OK);
				}
				
				resultMap.put("w_resultCode",handler.getFlag());
				resultMap.put("msg", handler.getMessage());
			}
			
			try{
				//세로이미지 확장자
				if(hFile.getSize() != 0L){
					checkFileSize(hFile, 1);
					String hFileName = hFile.getOriginalFilename();
					
					if(StringUtils.isNotBlank(hFileName)){
						String h_ext = hFileName.substring(hFileName.lastIndexOf(".") + 1, hFileName.length());
						
						if(StringUtils.isNotBlank(albumId)){
							h_fileName = albumId + "H" + GlobalCom.getTodayFormat4_24()+"."+h_ext;
							
							File h_imgFile = new File(dirImg+"/"+h_fileName);
							hFile.transferTo(h_imgFile);
							
							resultMap.put("h_resultCode", "0000");
							resultMap.put("h_fileName", h_fileName);
						}
					}
				}else{
					if("delete".equals(h_fileName)){
						resultMap.put("h_resultCode", "1000");
						h_fileName=null;
					}
				}
				
			}catch(Exception e){
				ExceptionHandler handler = new ExceptionHandler(e);
				if(handler.getFlag().equals(SmartUXProperties.getProperty("flag.file.maxsize.over"))){
					resultMap.put("code", handler.getFlag());
					resultMap.put("msg",handler.getMessage());
					return new ResponseEntity<String>(om.writeValueAsString(resultMap), responseHeaders, HttpStatus.OK);
				}
				
				resultMap.put("h_resultCode",handler.getFlag());
				resultMap.put("msg", handler.getMessage());
			}
			
			if("0000".equals(resultMap.get("w_resultCode")) || "0000".equals(resultMap.get("h_resultCode"))){
				service.insertPoster(serviceType,albumId,w_fileName,h_fileName,CookieUtil.getCookieUserID(request));
				
				resultMap.put("code", "0000");
				resultMap.put("msg","이미지가 성공적으로 적용 되었습니다.");
			}else{
				//service.insertPoster(serviceType,albumId,w_fileName,h_fileName,CookieUtil.getCookieUserID(request));
				resultMap.put("code", "REGISTRATION POSTER ERROR");
				resultMap.put("msg","최소 하나의 포스터를 등록해야합니다.");
			}
		}
		resultMap.put("imgDir", SmartUXProperties.getProperty("panel.imageserver.url"));

		cLog.endLog("전용포스터 등록", loginUser);
		
		return new ResponseEntity<String>(om.writeValueAsString(resultMap), responseHeaders, HttpStatus.OK);
	}
	
	/**
	 * 전용포스터 수정
	 * @param request
	 * @param wFile
	 * @param hFile
	 * @param w_fileName
	 * @param h_fileName
	 * @param serviceType
	 * @param albumId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/poster/updatePoster", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> updatePosterProc(
			HttpServletRequest request, 
			 @RequestParam(value="w_file", required=false) MultipartFile wFile,
			 @RequestParam(value="h_file", required=false) MultipartFile hFile,
			 @RequestParam(value="w_file_name", required=false) String w_fileName,
			 @RequestParam(value="h_file_name", required=false) String h_fileName,
			 @RequestParam(value="service_type", required=false) String serviceType,
			 @RequestParam(value="album_id", required=false) String albumId
			 ) throws Exception {
		
		final CLog cLog = new CLog(logger, request.getRemoteAddr(), request.getRequestURI(), request.getMethod());
		final String loginUser = CookieUtil.getCookieUserID(request);
		
		ObjectMapper om = new ObjectMapper();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");  
		
		cLog.startLog("전용포스터 수정", loginUser);
		
		Map<String, String> resultMap = new HashMap<String, String>();
		
		String dirImg = SmartUXProperties.getProperty("panel.imgupload.dir");
		
		if(StringUtils.isNotBlank(dirImg)){
			File dirChk = new File(dirImg);
			if(!dirChk.exists()) {
				dirChk.mkdirs();
			}
			
			try{
				//가로이미지 확장자
				if(wFile.getSize() != 0L){
					checkFileSize(wFile, 2);
					String wFileName = wFile.getOriginalFilename();
					
					if(StringUtils.isNotBlank(wFileName)){
						String w_ext = wFileName.substring(wFileName.lastIndexOf(".") + 1, wFileName.length());
						
						if(StringUtils.isNotBlank(albumId)){
							w_fileName = albumId + "W" + GlobalCom.getTodayFormat4_24()+"."+w_ext;
							
							File w_imgFile= new File(dirImg+"/"+w_fileName);
							wFile.transferTo(w_imgFile);
							
							resultMap.put("w_resultCode", "0000");
							resultMap.put("w_fileName", w_fileName);
						}
					}
				}else{
					if("delete".equals(w_fileName)){
						resultMap.put("w_resultCode", "1000");
						w_fileName=null;
					}
				}
				
			}catch(Exception e){
				ExceptionHandler handler = new ExceptionHandler(e);
				if(handler.getFlag().equals(SmartUXProperties.getProperty("flag.file.maxsize.over"))){
					resultMap.put("code", handler.getFlag());
					resultMap.put("msg",handler.getMessage());
					return new ResponseEntity<String>(om.writeValueAsString(resultMap), responseHeaders, HttpStatus.OK);
				}
				
				resultMap.put("w_resultCode",handler.getFlag());
				resultMap.put("msg", handler.getMessage());
			}
			
			try{
				//세로이미지 확장자
				if(hFile.getSize() != 0L){
					checkFileSize(hFile, 1);
					String hFileName = hFile.getOriginalFilename();
					
					if(StringUtils.isNotBlank(hFileName)){
						String h_ext = hFileName.substring(hFileName.lastIndexOf(".") + 1, hFileName.length());
						
						if(StringUtils.isNotBlank(albumId)){
							h_fileName = albumId + "H" + GlobalCom.getTodayFormat4_24()+"."+h_ext;
							
							File h_imgFile = new File(dirImg+"/"+h_fileName);
							hFile.transferTo(h_imgFile);
							
							resultMap.put("h_resultCode", "0000");
							resultMap.put("h_fileName", h_fileName);
						}
					}
				}else{
					if("delete".equals(h_fileName)){
						resultMap.put("h_resultCode", "1000");
						h_fileName=null;
					}
				}
				
			}catch(Exception e){
				ExceptionHandler handler = new ExceptionHandler(e);
				if(handler.getFlag().equals(SmartUXProperties.getProperty("flag.file.maxsize.over"))){
					resultMap.put("code", handler.getFlag());
					resultMap.put("msg",handler.getMessage());
					return new ResponseEntity<String>(om.writeValueAsString(resultMap), responseHeaders, HttpStatus.OK);
				}
				
				resultMap.put("h_resultCode",handler.getFlag());
				resultMap.put("msg", handler.getMessage());
			}
			
			if("0000".equals(resultMap.get("w_resultCode")) || "0000".equals(resultMap.get("h_resultCode"))){
				service.updatePoster(serviceType,albumId,w_fileName,h_fileName,CookieUtil.getCookieUserID(request));
				
				resultMap.put("code", "0000");
				resultMap.put("msg","이미지가 성공적으로 적용 되었습니다.");
			}else{
				if(("1000".equals(resultMap.get("w_resultCode")) && "1000".equals(resultMap.get("h_resultCode"))) ||
				   ("1000".equals(resultMap.get("w_resultCode")) && GlobalCom.isEmpty(h_fileName)) ||
				   ("1000".equals(resultMap.get("h_resultCode")) && GlobalCom.isEmpty(w_fileName)) ||
				   (GlobalCom.isEmpty(h_fileName) && GlobalCom.isEmpty(w_fileName))){
					resultMap.put("code", "REGISTRATION POSTER ERROR");
					resultMap.put("msg","최소 하나의 포스터를 등록해야합니다.");
				}else{
					service.updatePoster(serviceType,albumId,w_fileName,h_fileName,CookieUtil.getCookieUserID(request));
					
					resultMap.put("code", "0000");
					resultMap.put("msg","이미지가 성공적으로 적용 되었습니다.");
				}
			}
		}
		resultMap.put("imgDir", SmartUXProperties.getProperty("panel.imageserver.url"));
		
		cLog.endLog("전용포스터 수정", loginUser);
		
		return new ResponseEntity<String>(om.writeValueAsString(resultMap), responseHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(value="/admin/poster/deletePoster", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> deleteRule(
			HttpServletRequest request, 
			@RequestParam(value="album_id[]") String [] albumIds,
			@RequestParam(value="service_type[]") String [] serviceTypes) throws Exception{
		
		final CLog cLog = new CLog(logger, request.getRemoteAddr(), request.getRequestURI(), request.getMethod());
		final String loginUser = CookieUtil.getCookieUserID(request);
		
		cLog.startLog("전용포스터 삭제", loginUser);
		
		Map<String, String> resultMap = new HashMap<String, String>();
		
		try{
			if((null == albumIds) || (albumIds.length == 0)){
				resultMap.put("flag", "9999");
				resultMap.put("message","코드값은 필수로 들어가야 합니다.");
			}
			
			service.deletePoster(albumIds, serviceTypes);
			
			resultMap.put("flag", "0000");
			resultMap.put("message","성공적으로 삭제 되었습니다.");
			
		}catch(Exception e){
			ExceptionHandler handler = new ExceptionHandler(e);
			
			resultMap.put("flag",handler.getFlag());
			resultMap.put("message", handler.getMessage());
		}
		
		cLog.endLog("전용포스터 삭제", loginUser);
		
		ObjectMapper om = new ObjectMapper();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");  
		return new ResponseEntity<String>(om.writeValueAsString(resultMap), responseHeaders, HttpStatus.OK);
	}
	
	/**
     * 파일 사이즈 제한
     * 1 : 가로이미지, 2 : 세로이미지
     * @param text_file
     * @param type
     */
	private void checkFileSize(MultipartFile img, int type) {
		String fileMaxSize = SmartUXProperties.getProperty("mims.poster.imagefile.size");
		String imgName = "세로 이미지";
		if(2==type) {
			fileMaxSize = SmartUXProperties.getProperty("mims.wide.imagefile.size");
			imgName = "가로 이미지";
		}
		
		if(null != fileMaxSize) {
			long iFileMaxSize = Long.parseLong(fileMaxSize);
			if(img.getSize() > iFileMaxSize) throw new SmartUXException(SmartUXProperties.getProperty("flag.file.maxsize.over")
					, SmartUXProperties.getProperty("message.file.maxsize.over",imgName + " : " + fileMaxSize + "Byte 미만"));
		}
	}
}
