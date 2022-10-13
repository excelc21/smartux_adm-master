package com.dmi.smartux.admin.commonMng.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONSerializer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.dmi.smartux.admin.commonMng.service.CommonModuleService;
import com.dmi.smartux.admin.commonMng.vo.CategoryAlbumListVo;
import com.dmi.smartux.admin.commonMng.vo.EventCategoryAlbumVo;
import com.dmi.smartux.admin.commonMng.vo.FlatRateVO;
import com.dmi.smartux.admin.commonMng.vo.MenuTreeVo;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.util.HTMLCleaner;

@Controller
public class CommonModuleController {
	private final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	CommonModuleService commService;
	
	// category_id는 항상 VC를 줄것(VC로 줘야 최상위 카테고리가 검색된다)
	/**
	 * 카테고리 선택 팝업 화면
	 * @param categoryId			카테고리 선택 팝업 화면에서 처음에 보이는 카테고리 선택 select box를 구성하기 위한 부모 카테고리 코드(여기엔 항상 VC가 와야 한다)
	 * @param model					Model 객체
	 * @return						카테고리 선택 팝업 화면 URL
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/commonMng/getOnceCategoryAlbum", method=RequestMethod.GET)
	public String getOnceCategoryAlbum(@RequestParam(value="categoryId") String categoryId
			,@RequestParam(value="hiddenName", required=false, defaultValue="") String hiddenName
			,@RequestParam(value="textName", required=false, defaultValue="") String textName
			,@RequestParam(value="textHtml", required=false, defaultValue="") String textHtml
			,@RequestParam(value="type", required=false) String type
			,@RequestParam(value="series", required=false) String series
			,@RequestParam(value="categoryYn", required=false, defaultValue="N") String categoryYn
			,@RequestParam(value="panelId", required=false, defaultValue="N") String panelId
			,@RequestParam(value="isTypeChange", required=false) String isTypeChange
			,@RequestParam(value="isAds", required=false) String isAds
			, Model model) throws Exception {

		HTMLCleaner cleaner = new HTMLCleaner();
		categoryId = cleaner.clean(categoryId);
		type = GlobalCom.isNull(cleaner.clean(type),"I20");
		series = GlobalCom.isNull(cleaner.clean(series),"N");
		categoryYn = GlobalCom.isNull(cleaner.clean(categoryYn),"N");
		panelId = cleaner.clean(panelId);
		isTypeChange = GlobalCom.isNull(cleaner.clean(isTypeChange),"N");
		isAds = GlobalCom.isNull(cleaner.clean(isAds),"N");
		
		String hdtvPanelID = SmartUXProperties.getProperty("hdtv.panel.id");
		
		if(!"I30".equals(type)){
			if (GlobalCom.contains(hdtvPanelID, panelId, "\\|")) {
				type = "NSC";
			} else {
				// 최상위 카테고리 정보 조회
				type = "I20";
			}
		}
		
		CategoryAlbumListVo categoryalbumlistVo = new CategoryAlbumListVo();
		categoryalbumlistVo.setCategoryId(categoryId);
		categoryalbumlistVo.setType(type);
		List<EventCategoryAlbumVo> result = commService.getEventCategoryAlbumList(categoryalbumlistVo);
		
		List<EventCategoryAlbumVo> categoryResult = new ArrayList<EventCategoryAlbumVo>();
//		List<EventCategoryAlbumVo> albumResult = new ArrayList<EventCategoryAlbumVo>();
		
		for (EventCategoryAlbumVo item : result) {
			if (!StringUtils.hasText(item.getAlbum_id())) {
				categoryResult.add(item);
			} 
//			else {
//				albumResult.add(item);
//			}
		}

		model.addAttribute("hiddenName", hiddenName);
		model.addAttribute("textName", textName);
		model.addAttribute("textHtml", textHtml);
		model.addAttribute("type", type);
		model.addAttribute("series", series);
		model.addAttribute("categoryId", categoryId);
		model.addAttribute("categoryResult", categoryResult);//카테고리
		model.addAttribute("categoryYn", categoryYn);//카테고리 선택 여부
		model.addAttribute("isTypeChange", isTypeChange); //카테고리 타입 변경 유무
//		model.addAttribute("albumResult", albumResult);//앨범
		model.addAttribute("isAds", isAds); //배너여부
		
		return "/admin/commonMng/getOnceCategoryAlbum";
	}
	
	/**
	 * 카테고리 선택 팝업 화면 (리턴 필드가 고정으로 되어 있다.. 공통으로 사용할려면 바꿔야겠는데.. 사간없네..)
	 * category_id는 항상 VC를 줄것(VC로 줘야 최상위 카테고리가 검색된다)
	 * @param categoryId			카테고리 선택 팝업 화면에서 처음에 보이는 카테고리 선택 select box를 구성하기 위한 부모 카테고리 코드(여기엔 항상 VC가 와야 한다)
	 * @param model					Model 객체
	 * @return						카테고리 선택 팝업 화면 URL
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/commonMng/getCategoryAlbumList", method=RequestMethod.GET)
	public String getCategoryAlbumList(@RequestParam(value="categoryId") String categoryId
			, @RequestParam(value="type", required=false) String type
			, @RequestParam(value="series", required=false) String series
			, @RequestParam(value="isTypeChange", required=false) String isTypeChange
			, Model model) throws Exception {

		HTMLCleaner cleaner = new HTMLCleaner();
		categoryId = cleaner.clean(categoryId);
		type = GlobalCom.isNull(cleaner.clean(type),"I20");
		series = GlobalCom.isNull(cleaner.clean(series),"N");
		isTypeChange = GlobalCom.isNull(cleaner.clean(isTypeChange),"N");
		
		CategoryAlbumListVo categoryalbumlistVo = new CategoryAlbumListVo();
		categoryalbumlistVo.setCategoryId(categoryId);
		categoryalbumlistVo.setType(type);
		List<EventCategoryAlbumVo> result = commService.getEventCategoryAlbumList(categoryalbumlistVo);
		
		List<EventCategoryAlbumVo> categoryResult = new ArrayList<EventCategoryAlbumVo>();
//		List<EventCategoryAlbumVo> albumResult = new ArrayList<EventCategoryAlbumVo>();
		
		for (EventCategoryAlbumVo item : result) {
			if (!StringUtils.hasText(item.getAlbum_id())) {
				categoryResult.add(item);
			} 
//			else {
//				albumResult.add(item);
//			}
		}

		model.addAttribute("type", type);
		model.addAttribute("series", series);
		model.addAttribute("categoryId", categoryId);
		model.addAttribute("categoryResult", categoryResult);//카테고리
		model.addAttribute("isTypeChange", isTypeChange); //카테고리 변경 유무
//		model.addAttribute("albumResult", albumResult);//앨범
		
		return "/admin/commonMng/getEventCategoryAlbumList";
	}
	
	/**
	 * 카테고리 선택 팝업화면에서 카테고리 선택시 선택된 카테고리의 하위 카테고리와 앨범들을 조회한다
	 * @param categoryId	카테고리 id
	 * @return				하위 카테고리와 앨범들의 목록 데이터가 있는 json 문자열
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/commonMng/getCategoryList", method=RequestMethod.POST)
	public ResponseEntity<String> getCategoryList(@RequestParam(value="categoryId") String categoryId
			, @RequestParam(value="type", required=false) String type) throws Exception{
		
		HTMLCleaner cleaner = new HTMLCleaner();
		categoryId 	= cleaner.clean(categoryId);
		type = GlobalCom.isNull(cleaner.clean(type),"I20");
		
		CategoryAlbumListVo categoryalbumlistVo = new CategoryAlbumListVo();
		categoryalbumlistVo.setCategoryId(categoryId);
		categoryalbumlistVo.setType(type);
		List<EventCategoryAlbumVo> result = commService.getEventCategoryAlbumList(categoryalbumlistVo);
		
		List<EventCategoryAlbumVo> categoryResult = new ArrayList<EventCategoryAlbumVo>();
//		List<EventCategoryAlbumVo> albumResult = new ArrayList<EventCategoryAlbumVo>();
		
		for (EventCategoryAlbumVo item : result) {
			if (!StringUtils.hasText(item.getAlbum_id())) {
				categoryResult.add(item);
			} 
		}
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(categoryResult).toString(), responseHeaders, HttpStatus.CREATED);
	}
	
	/**
	 * 카테고리 선택 팝업화면에서 카테고리 선택시 선택된 카테고리의 하위 카테고리와 앨범들을 조회한다
	 * @param categoryId	카테고리 id
	 * @return				하위 카테고리와 앨범들의 목록 데이터가 있는 json 문자열
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/commonMng/getCategoryAlbumList", method=RequestMethod.POST)
	public ResponseEntity<String> getCategoryAlbumList(@RequestParam(value="categoryId", required=false) String categoryId
			, @RequestParam(value="type", required=false) String type
			,@RequestParam(value="searchType", required=false) String searchType
			,@RequestParam(value="searchVal", required=false) String searchVal
			,@RequestParam(value="imgFlag", required=false, defaultValue="") String imgFlag
			,@RequestParam(value="specifyYn", required=false, defaultValue="N") String specifyYn
			,@RequestParam(value="serviceType", required=false, defaultValue="") String serviceType) throws Exception{
		
		HTMLCleaner cleaner = new HTMLCleaner();
		categoryId 	= cleaner.clean(categoryId);
		type = GlobalCom.isNull(cleaner.clean(type),"I30");
		
		CategoryAlbumListVo categoryalbumlistVo = new CategoryAlbumListVo();
		categoryalbumlistVo.setCategoryId(categoryId);
		categoryalbumlistVo.setType(type);
		
		List<EventCategoryAlbumVo> result = null;
		if("".equals(searchType) || null == searchType){
			result = commService.getEventCategoryAlbumList(categoryalbumlistVo);
		}else{
			categoryalbumlistVo.setSearchType(searchType);
			categoryalbumlistVo.setSearchVal(searchVal);
			
			result = commService.searchEventCategoryAlbumList(categoryalbumlistVo);
		}
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(result).toString(), responseHeaders, HttpStatus.CREATED);
	}
	
	@ResponseBody
	@RequestMapping(value = "/admin/download", method = RequestMethod.GET)
	public byte[] getNASData (
			@RequestParam(value = "path", required = false, defaultValue = "") String path,
			@RequestParam(value = "system", required = false, defaultValue = "") String system,
			HttpServletResponse response,
			Model model) throws Exception {

		String nasPath;

		if ("mims".equalsIgnoreCase(system)) {
			nasPath = GlobalCom.isNull(SmartUXProperties.getPathProperty("nas.mims.dir"), "/NAS_DATA/web/smartux/");
		} else {
			nasPath = GlobalCom.isNull(SmartUXProperties.getPathProperty("nas.hmims.dir"), "/NAS_DATA/web/smartux/hdtv/");
		}

		File file = new File(nasPath + path);

		response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
		response.setHeader("Content-Transfer-Encoding", "binary");
		response.setContentLength((int) file.length());
		response.setContentType("application/download; charset=utf-8");

		return FileCopyUtils.copyToByteArray(file);
	}
	
	/**
	 * 외부 프로퍼티에 등록된 사이즈와 포맷으로 업로드 할 이미지 사이즈 / 포맷 체크 - hdtv 포팅
	 * [이미지 사이즈 / 포맷 체크 추가-2015.05.12]
	 * @param imgPath
	 * @param propertyNm
	 * @return	 이미지명 json 문자열
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/commonMng/imgHeaderCheck",method=RequestMethod.POST)
	public ResponseEntity<String> imgHeaderCheck(
			@RequestParam(value="imgPath", required=false) MultipartFile imgPath,
			@RequestParam(value="propertyNm", required=false) String propertyNm,
			HttpServletRequest request,
			Model model
			) throws Exception{	
		
		String result = "";
		String resultcode = "";
		String resultmessage = "";
		
		Boolean formatChk = false;
		Boolean sizeChk = false;
		
		String imgF = "";
		String imgS = "";
		String iFormat = "";
		String[] imgFormat = SmartUXProperties.getProperty(propertyNm+".format").split("\\|");
		String sizeType = GlobalCom.isNull(SmartUXProperties.getProperty(propertyNm+".type"),"");
		int widthSize = Integer.parseInt(GlobalCom.isNull(SmartUXProperties.getProperty(propertyNm+".width"),"0"));
		int heightSize = Integer.parseInt(GlobalCom.isNull(SmartUXProperties.getProperty(propertyNm+".height"),"0"));
		
		if (!imgPath.equals("") || !imgPath.equals(null)) {
			//이미지 헤더의 포맷정보 구하기
			imgF = GlobalCom.imgFormatCheck(imgPath);
			
			 int format = imgFormat.length;
			 
            for (int i = 0; i < format; i++) {
            	iFormat = iFormat + " ." +imgFormat[i];
            	if (imgF.equals(imgFormat[i])) {
            		formatChk = true;
            	} 
            }
	         
            if (formatChk.equals(true)) {
            	resultmessage = "pass";
            } else if (formatChk.equals(false)) {
            	resultmessage = imgF;
            }
		} else {
			formatChk = true;
		}
		
		if (widthSize != 0 || heightSize != 0) {
			//이미지 헤더의 사이즈 정보 구하기
			imgS = GlobalCom.imgSizeCheck(imgPath, sizeType, widthSize, heightSize);
			
			if(imgS.equals("1")) {
				resultmessage = "pass";
				sizeChk = true;
			} else if (imgS.equals("2")) {
				resultmessage = "width";
			} else if (imgS.equals("3")) {
				resultmessage = "height";
			} else if (imgS.equals("4")) {
				resultmessage = "all";
			}
		} else {
			sizeChk = true;
		}
		
		if (formatChk.equals(true) && sizeChk.equals(true)) { // 포맷, 사이즈가 모두 체크 완료
			resultcode = "0000"; 
			resultmessage = "pass";
		} else if (formatChk.equals(false)) {  // 포맷 맞지 않음
			resultcode = "1001";
			resultmessage = imgF;
		} else if (formatChk.equals(true) && sizeChk.equals(false)) { // 포맷은 맞고, 사이즈가 맞지 않을때,
			resultcode = "2002";
		}
		
		result = "{\"result\":{\"message\":\""+resultmessage+"\",\"flag\":\""+resultcode+"\",\"type\":\""+sizeType+"\",\"format\":\""+iFormat+"\",\"width\":\""+widthSize+"\",\"height\":\""+heightSize+"\"}}";
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(result).toString(), responseHeaders, HttpStatus.CREATED);	
		
	}
    
    /**
     * 배너 마스터 팝업 조회
     * @param callbak	콜백 함수명
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/admin/commonMng/getBannerMasterPop", method = RequestMethod.GET)
    public String getBannerMasterPop(Model model
    		) throws Exception {
        
        List<HashMap<String,String>> itemList = commService.getBannerMasterPop();
		model.addAttribute("itemList", itemList);
        return "/admin/commonMng/getBannerMasterPop";
    }
    
    /**
     * MIMS  카테고리 팝업 
     * @param categoryId	상위카테고리 아이디
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/admin/commonMng/getMimsCategoryListPop", method = RequestMethod.GET)
    public String getMimsCategoryListPop(Model model,
    		@RequestParam(value = "categoryId", required=false, defaultValue="") String categoryId,
            @RequestParam(value = "menuId", required=false, defaultValue="") String menuId,
            @RequestParam(value = "contentType", required=false, defaultValue="") String contentType
    		) throws Exception {
        
    	HTMLCleaner cleaner = new HTMLCleaner();
		categoryId 	= cleaner.clean(categoryId);
		
		// 메뉴 아이디가 들어올시 메뉴아이디의 data_val값을 categoryId로 셋팅한다.
		if(!menuId.equals("")){
			MenuTreeVo menuTree = commService.selectMenuTreeInfo(menuId);
			if(menuTree!=null){
    			categoryId=menuTree.getData_val();
    		}
		}
		
		Map<String,String> param = new HashMap<String, String>();
        param.put("categoryId",categoryId);
        param.put("contentType",contentType);
		
        List<HashMap<String,String>> result = commService.getMimsCategoryList(param);
        List<HashMap<String,String>> categoryResult = new ArrayList();
        
        for (HashMap<String,String> item : result) {
        	categoryResult.add(item);	
        }
        
        model.addAttribute("categoryResult", categoryResult);
        model.addAttribute("albumElementId", "");
        
        return "/admin/commonMng/getMimsCategoryListPop";
    }
    
    /**
     * 카테고리 조회 (카테고리 아이디를 받아서 하위 카테고리 정보 리턴)
     * @param categoryId	상위 카테고리 아이디
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/admin/commonMng/getMimsCategoryListPop", method = RequestMethod.POST)
    public ResponseEntity<String> getMimsCategoryListPop(
    		@RequestParam(value = "categoryId", required=false, defaultValue="") String categoryId,
    		@RequestParam(value = "contentType", required=false, defaultValue="") String contentType
    		) throws Exception {
    	
    	HTMLCleaner cleaner = new HTMLCleaner();
		categoryId 	= cleaner.clean(categoryId);
		
		Map<String,String> param = new HashMap<String, String>();
		param.put("categoryId",categoryId);
		param.put("contentType",contentType);
		List<HashMap<String,String>> result = commService.getMimsCategoryList(param);
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(result).toString(), responseHeaders, HttpStatus.CREATED);
    }
    
    /**
     * (MIMS) 하위에 카테고리가 있는 카테고리 아이디 인지 유무
     * @param categoryId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/admin/commonMng/getMimsCategorySubYn", method = RequestMethod.GET)
    public ResponseEntity<String> getMimsCategorySubYn(@RequestParam(value = "categoryId", required=false, defaultValue="") String categoryId) throws Exception {
    	HTMLCleaner cleaner = new HTMLCleaner();
		categoryId 	= cleaner.clean(categoryId);
		
		Map<String,String> param = new HashMap<String, String>();
		param.put("categoryId",categoryId);
		String result = commService.getMimsCategorySubYn(param);
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(result, responseHeaders, HttpStatus.CREATED);
    }
    
    /**
     * IPTV MIMS  카테고리 팝업 
     * @param categoryId	상위카테고리 아이디
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/admin/commonMng/getMimsIptvCategoryListPop", method = RequestMethod.GET)
    public String getMimsIptvCategoryListPop(Model model,
    		@RequestParam(value = "categoryId", required=false, defaultValue="") String categoryId,
            @RequestParam(value = "menuId", required=false, defaultValue="") String menuId,
            @RequestParam(value = "contentType", required=false, defaultValue="") String contentType
    		) throws Exception {
        
    	HTMLCleaner cleaner = new HTMLCleaner();
		categoryId 	= cleaner.clean(categoryId);
		
		Map<String,String> param = new HashMap<String, String>();
        param.put("categoryId",categoryId);
        param.put("contentType",contentType);
		
        List<HashMap<String,String>> result = commService.getMimsIptvCategoryList(param);
        List<HashMap<String,String>> categoryResult = new ArrayList();
        
        for (HashMap<String,String> item : result) {
        	categoryResult.add(item);	
        }
        
        model.addAttribute("categoryResult", categoryResult);
        model.addAttribute("albumElementId", "");
        
        return "/admin/commonMng/getMimsIptvCategoryListPop";
    }
    
    /**
     * IPTV 카테고리 조회 (카테고리 아이디를 받아서 하위 카테고리 정보 리턴)
     * @param categoryId	상위 카테고리 아이디
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/admin/commonMng/getMimsIptvCategoryListPop", method = RequestMethod.POST)
    public ResponseEntity<String> getMimsIptvCategoryListPop(
    		@RequestParam(value = "categoryId", required=false, defaultValue="") String categoryId,
    		@RequestParam(value = "contentType", required=false, defaultValue="") String contentType
    		) throws Exception {
    	
    	HTMLCleaner cleaner = new HTMLCleaner();
		categoryId 	= cleaner.clean(categoryId);
		
		Map<String,String> param = new HashMap<String, String>();
		param.put("categoryId",categoryId);
		param.put("contentType",contentType);
		List<HashMap<String,String>> result = commService.getMimsIptvCategoryList(param);
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(result).toString(), responseHeaders, HttpStatus.CREATED);
    }
    
    /**
     * (IPTV MIMS) 하위에 카테고리가 있는 카테고리 아이디 인지 유무
     * @param categoryId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/admin/commonMng/getMimsIptvCategorySubYn", method = RequestMethod.GET)
    public ResponseEntity<String> getMimsIptvCategorySubYn(@RequestParam(value = "categoryId", required=false, defaultValue="") String categoryId) throws Exception {
    	HTMLCleaner cleaner = new HTMLCleaner();
		categoryId 	= cleaner.clean(categoryId);
		
		Map<String,String> param = new HashMap<String, String>();
		param.put("categoryId",categoryId);
		String result = commService.getMimsIptvCategorySubYn(param);
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(result, responseHeaders, HttpStatus.CREATED);
    }
    
    /**
	 * 상품 리스트 조회 팝업
	 *
	 * @param singleChoice 한개만 선택할 경우 Y / 멀티 셀렉트 N
	 * @param hiddenName
	 * @param textName
	 * @param textHtml
	 * @param model
	 * @return
     * @throws Exception
     */
	@RequestMapping(value="/admin/commonMng/getFlatRateList", method=RequestMethod.GET)
	public String getFlatRateList(
			@RequestParam(value="singleChoice", required=false, defaultValue="N") String singleChoice,
			@RequestParam(value="hiddenName", required=false, defaultValue="") String hiddenName,
	        @RequestParam(value="textName", required=false, defaultValue="") String textName,
	        @RequestParam(value="textHtml", required=false, defaultValue="") String textHtml,
			Model model) throws Exception {

		List<FlatRateVO> result = commService.getFlatRateList2();
		FlatRateVO vo = new FlatRateVO();
		
		vo.setProductID(SmartUXProperties.getProperty("smartux.product.code"));
		vo.setProductName(SmartUXProperties.getProperty("smartux.product.name"));
		result.add(0, vo);
		
		model.addAttribute("singleChoice", singleChoice);
		model.addAttribute("hiddenName", hiddenName);
		model.addAttribute("textName", textName);
		model.addAttribute("textHtml", textHtml);
		model.addAttribute("flatRateList", result); //월정액상품리스트

		return "/admin/commonMng/getFlatRateList";
	}
	
}
