package com.dmi.smartux.admin.backgroundimage.controller;

import com.dmi.commons.consts.ResultCode;
import com.dmi.commons.exception.MimsCommonException;
import com.dmi.commons.factory.FileProcessFunction;
import com.dmi.commons.factory.FileProcessLockFactory;
import com.dmi.commons.logger.CLog;
import com.dmi.commons.model.RestfulCodeMsg;
import com.dmi.commons.utility.HttpClientUtils;
import com.dmi.commons.utility.RestfulUtils;
import com.dmi.smartux.admin.backgroundimage.service.BackgroundImageService;
import com.dmi.smartux.admin.backgroundimage.vo.BackgroundImageInfo;
import com.dmi.smartux.admin.backgroundimage.vo.CategoryAlbumVO;
import com.dmi.smartux.common.exception.ExceptionHandler;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.CookieUtil;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.util.HTMLCleaner;
import com.dmi.smartux.common.vo.CUDResult;
import net.sf.json.JSONSerializer;

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
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class BackgroundImageController {
	
	private final Log logger = LogFactory.getLog(this.getClass());
	
	
	@Autowired
	BackgroundImageService service;
	
	private final Log applyLogger = LogFactory.getLog("adminApplyCacheHistroy");

	/**
	 * 생활지수 문구 즉시적용
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/backgroundimage/backgroundimageApplyCache", method=RequestMethod.POST )
	public ResponseEntity<String> backgroundimageApplyCache(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value="schedule_yn", defaultValue="Y")  String schedule_yn,
			@RequestParam(value="callByScheduler",  defaultValue="Y") final  String callByScheduler
			) throws Exception{

		final CLog cLog = new CLog(applyLogger, request.getRemoteAddr(), request.getRequestURI(), request.getMethod());
		final String loginUser = schedule_yn.equals("Y") ? "batch" : CookieUtil.getCookieUserID(request);
		final CUDResult result = new CUDResult();

		try {
			cLog.startLog("배경영상 즉시적용 : ",  loginUser);
			final String host = SmartUXProperties.getProperty("cache.sync.public.host");
			final String port = SmartUXProperties.getProperty("cache.sync.public.port");

			String fileLockPath = SmartUXProperties.getProperty("BackgroundImage.refreshBackgroundImageList.fileLock");
			int waitTime = NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.wait"), 5);

			FileProcessLockFactory.start(fileLockPath, waitTime,  new FileProcessFunction("배경영상 즉시적용", ResultCode.ApplyRequestFail) {

				@Override
				public void run() throws Exception {
					RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
							host,
							port,
							SmartUXProperties.getProperty("BackgroundImage.refreshBackgroundImageList.url"),//+"?callByScheduler="+callByScheduler,
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.method"), "POST"),
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"),
							MediaType.APPLICATION_JSON.toString(),
							NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.timeout"), 60000)).getResponseBody());

					result.setFlag(codeMsg.getCode());
					result.setMessage(codeMsg.getMessage());
					cLog.middleLog("배경영상 즉시적용", "API 호출", codeMsg.getCode(), codeMsg.getMessage());
				}
			});
		} catch (MimsCommonException e) {
			cLog.warnLog("배경영상 즉시적용", "실패", e.getFlag(), e.getMessage(), e.getLocalizedMessage());
			result.setFlag(e.getFlag());
			result.setMessage(e.getMessage());
		} finally {
			cLog.endLog("배경영상 즉시적용", loginUser, result.getFlag(), result.getMessage());
		}

		String resultMsg = "{\"flag\" : \"" + result.getFlag() + "\", \"message\" : \"" + result.getMessage() + "\"}";
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(resultMsg).toString(), responseHeaders, HttpStatus.OK);
	}


	@RequestMapping(value="/admin/backgroundimage/getBackgroundImageList", method={RequestMethod.GET, RequestMethod.POST} )
	public String getBackgroundImageList(
			Model model){

		try {
			
			List<BackgroundImageInfo> list = this.service.getBackgroundImageList();
			
			model.addAttribute("list", list);
		} catch (Exception e) {
			logger.error("[getLifeMessageList]["+e.getClass().getName()+"]["+e.getMessage()+"]");
		}

		return "/admin/senior/backgroundimage/getBackgroundImageList";
	}

	
	/**
	 * 카테고리 선택 팝업 화면
	 * @param category_id			카테고리 선택 팝업 화면에서 처음에 보이는 카테고리 선택 select box를 구성하기 위한 부모 카테고리 코드(여기엔 항상 VC가 와야 한다)
	 * @param albumElementid		카테고리 선택시 해당 카테고리에 속한 앨범들을 보여줄 부모창의 앨범목록을 보여주는 html 태그의 element id
	 * @param model					Model 객체
	 * @return						카테고리 선택 팝업 화면 URL
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/backgroundimage/insertBackgroundImage", method=RequestMethod.GET)
	public String insertBackgroundImage(
			@RequestParam(value="cat_id", required=false, defaultValue="L202") String cat_id
			, @RequestParam(value="category_gb", required=false, defaultValue="I20") String category_gb
			, Model model) throws Exception{
		
		try {
			HTMLCleaner cleaner = new HTMLCleaner();
			cat_id 			= cleaner.clean(cat_id);
			category_gb 	= cleaner.clean(category_gb);
			
			String maxSize = StringUtils.defaultIfEmpty(SmartUXProperties.getProperty("BackgroundImage.max.size"), "200");
			
			Set<String> compAlbumId = new HashSet<String>();
			
			List<BackgroundImageInfo> list = this.service.getBackgroundImageList();
			/*List<BackgroundImageInfo> result = service.getAlbumList(category_gb, cat_id);
			
			if(list != null && list.size() != 0)
			for(BackgroundImageInfo info : result){
				for(BackgroundImageInfo albumId : list){
					if(info.getAlbum_id().equals(albumId.getAlbum_id())){
						info.setAlbum_dup("Y");
					}
				}
			}
	*/
			//model.addAttribute("list", result);
			model.addAttribute("maxSize", maxSize);
			model.addAttribute("list", list);
		} catch (Exception e) {
			logger.error("[getLifeMessageList]["+e.getClass().getName()+"]["+e.getMessage()+"]");
		}
		
		return "/admin/senior/backgroundimage/insertBackgroundImage";
	}
	
	@RequestMapping(value="/admin/backgroundimage/insertBackgroundImage", method=RequestMethod.POST )
	public @ResponseBody String insertBackgroundImage(
			/*@RequestParam(value="order_no", required=false, defaultValue="") String order_no,
			@RequestParam(value="album_id", required=false, defaultValue="") String album_id,
			@RequestParam(value="cat_id", required=false, defaultValue="") String cat_id,
			@RequestParam(value="album_title", required=false, defaultValue="") String album_title,*/
			@RequestParam(value="album_id[]", required=false) String [] album_id,
			@RequestParam(value="cat_id[]", required=false) String [] cat_id,
			@RequestParam(value="album_title[]", required=false) String [] album_title,
			@RequestParam(value="position_fix[]", required=false) String [] position_fix,
			@RequestParam(value="smartUXManager", required=false, defaultValue="") String writeId
			){
		
		String resultcode;
		String resultmessage;

        try{
        	
        	int[] orderList = new int[0];
        	service.deleteBackgroundImage(orderList);
        	
        	for(int i=0; i < album_id.length; i++){
        		BackgroundImageInfo backgroundImage = new BackgroundImageInfo();
    			backgroundImage.setAlbum_id(album_id[i]);
    			backgroundImage.setCat_id(cat_id[i]);
    			backgroundImage.setAlbum_title(album_title[i]);
    			backgroundImage.setPosition_fix(position_fix[i]);
    			backgroundImage.setWrite_id(writeId);
    			
    			this.service.insertBackgroundImage(backgroundImage);
        	}
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


	@RequestMapping(value="/admin/backgroundimage/deleteBackgroundImage", method=RequestMethod.POST)
	public @ResponseBody String deleteBackgroundImage(@RequestParam(value="order[]") int[] orderList){

		String resultcode;
		String resultmessage;

		try{
			service.deleteBackgroundImage(orderList);

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
	
	
	@RequestMapping(value="/admin/backgroundimage/orderUpdate", method=RequestMethod.POST )
	public @ResponseBody String orderUpdate(
			@RequestParam(value="seq[]") int[] codeList){

		String resultcode;
		String resultmessage;

		try{
			service.backgroundImageOrderUpdate(codeList);
			
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
	
	@RequestMapping(value="/admin/backgroundimage/setLocationFix", method=RequestMethod.POST )
	public @ResponseBody String setLocationFix(
			@RequestParam(value="position_fix[]") String[] fixList,
			@RequestParam(value="seq[]") int[] seq){

		String resultcode;
		String resultmessage;

		try{
			
			for(int i=0; i<seq.length; i++){
    			BackgroundImageInfo backgroundImage = new BackgroundImageInfo();
    			backgroundImage.setSeq(seq[i]);
    			backgroundImage.setPosition_fix(fixList[i]);
    			
    			this.service.setLocationFix(backgroundImage);
    		}

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
	
	// category_id는 항상 VC를 줄것(VC로 줘야 최상위 카테고리가 검색된다)
		// albumElementid는 팝업으로 뜨는 이 화면에서 카테고리를 선택할 경우 앨범들이 검색되는데 그 앨범이 셋팅될 부모창의 html element id를 넘겨준다
		/**
		 * 카테고리 선택 팝업 화면
		 * @param category_id			카테고리 선택 팝업 화면에서 처음에 보이는 카테고리 선택 select box를 구성하기 위한 부모 카테고리 코드(여기엔 항상 VC가 와야 한다)
		 * @param albumElementid		카테고리 선택시 해당 카테고리에 속한 앨범들을 보여줄 부모창의 앨범목록을 보여주는 html 태그의 element id
		 * @param model					Model 객체
		 * @return						카테고리 선택 팝업 화면 URL
		 * @throws Exception
		 */
		@RequestMapping(value="/admin/backgroundimage/getCategoryAlbumList", method=RequestMethod.GET)
		public String getCategoryAlbumList(@RequestParam(value="category_id") String category_id
				, @RequestParam(value="albumElementid") String albumElementid
				, @RequestParam(value="type", defaultValue="I20") String type
				, @RequestParam(value="series_yn", defaultValue="N") String series_yn
				, Model model) throws Exception{
			
			HTMLCleaner cleaner = new HTMLCleaner();
			category_id 	= cleaner.clean(category_id);
			albumElementid 	= cleaner.clean(albumElementid);
			type 	= cleaner.clean(type);
			series_yn 	= cleaner.clean(series_yn);
			
			List<CategoryAlbumVO> result = service.getCategoryAlbumList(category_id, GlobalCom.isNull(type, "I20"), series_yn);
			
			List<CategoryAlbumVO> categoryResult = new ArrayList<CategoryAlbumVO>();
			List<CategoryAlbumVO> albumResult = new ArrayList<CategoryAlbumVO>();
			
			// 화면을 띄울때 최상위 카테고리를 조사하는데 여기에 카테고리와 앨범이 섞여 있을수도 있어서 이를 분리하는 작업을 진행한다
			for(CategoryAlbumVO item : result){
				if("".equals(item.getAlbum_id())){			// album_id가 ""일 경우는 카테고리임을 의미한다
					categoryResult.add(item);
				}else{										// album_id가 ""가 아닐 경우는 앨범이다
					albumResult.add(item);
				}
			}
			
			model.addAttribute("categoryResult", categoryResult);
			model.addAttribute("albumResult", albumResult);
			model.addAttribute("albumElementid", albumElementid);
			model.addAttribute("type", type);
			model.addAttribute("series_yn", series_yn);
			return "/admin/senior/backgroundimage/getCategoryAlbumList";
	}
		
		/**
		 * 카테고리 선택 팝업화면에서 카테고리 선택시 선택된 카테고리의 하위 카테고리와 앨범들을 조회한다
		 * @param category_id	카테고리 id
		 * @return				하위 카테고리와 앨범들의 목록 데이터가 있는 json 문자열
		 * @throws Exception
		 */
		@RequestMapping(value="/admin/backgroundimage/getCategoryAlbumList", method=RequestMethod.POST)
		public ResponseEntity<String> getCategoryAlbumList(@RequestParam(value="category_id") String category_id
				, @RequestParam(value="type", defaultValue="I20") String type
				, @RequestParam(value="series_yn", defaultValue="N") String series_yn) throws Exception{
			
			HTMLCleaner cleaner = new HTMLCleaner();
			category_id 	= cleaner.clean(category_id);
			type 	= cleaner.clean(type);
			series_yn 	= cleaner.clean(series_yn);
			
			List<CategoryAlbumVO> result = service.getCategoryAlbumList(category_id, GlobalCom.isNull(type, "I20"), series_yn);
			
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
			return new ResponseEntity<String>(JSONSerializer.toJSON(result).toString(), responseHeaders, HttpStatus.CREATED);
		}

}
