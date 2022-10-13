package com.dmi.smartux.admin.gpack.category.controller;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dmi.smartux.admin.gpack.category.service.GpackCategoryViewService;
import com.dmi.smartux.admin.gpack.category.vo.GpackCategoryVO;
import com.dmi.smartux.admin.gpack.contents.service.GpackContentsViewService;
import com.dmi.smartux.admin.gpack.contents.vo.GpackContentsAutoVO;
import com.dmi.smartux.admin.gpack.pack.service.GPackPackViewService;
import com.dmi.smartux.admin.gpack.pack.vo.GPackPackVO;
import com.dmi.smartux.admin.mainpanel.service.PanelViewService;
import com.dmi.smartux.common.exception.ExceptionHandler;
import com.dmi.smartux.common.exception.SmartUXException;
import com.dmi.smartux.common.property.SmartUXProperties;

@Controller
public class GpackCategoryViewController {
	private final Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	GpackCategoryViewService service;
	@Autowired
	GpackContentsViewService contentsService;

	@Autowired
	GPackPackViewService packViewService;

	@Autowired
	PanelViewService panelViewservice;
	
	/**
	 * 카테고리 목록 조회 화면 (메뉴트리 펼쳐지는거 때문에 똑같은 놈이 3개임-_ -)
	 * @param pack_id
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/gpack/category/getTvReplayGpackCategoryList", method=RequestMethod.GET )
	public String getTvReplayGpackCategoryList(
			@RequestParam(value="pack_id", required=false, defaultValue="P0001") String pack_id, 
			Model model) throws Exception{
		
		GpackCategoryVO categoryVO = new GpackCategoryVO();
		categoryVO.setPack_id(pack_id);
		List result = service.getGpackCategoryList(categoryVO);

		model.addAttribute("pack_id", pack_id);
		model.addAttribute("result", result);
		
		return "/admin/gpack/category/getGpackCategoryListNew";
		//return "/admin/gpack/category/getGpackCategoryList";
	}
	
	/**
	 * 카테고리 목록 조회 화면 (메뉴트리 펼쳐지는거 때문에 똑같은 놈이 3개임-_ -)
	 * @param pack_id
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/gpack/category/getMovieAniGpackCategoryList", method=RequestMethod.GET )
	public String getMovieAniGpackCategoryList(
			@RequestParam(value="pack_id", required=false, defaultValue="P0002") String pack_id, 
			Model model) throws Exception{
		
		GpackCategoryVO categoryVO = new GpackCategoryVO();
		categoryVO.setPack_id(pack_id);
		List result = service.getGpackCategoryList(categoryVO);

		model.addAttribute("pack_id", pack_id);
		model.addAttribute("result", result);

		return "/admin/gpack/category/getGpackCategoryListNew";
		//return "/admin/gpack/category/getGpackCategoryList";
	}
	
	/**
	 * 카테고리 목록 조회 화면 (메뉴트리 펼쳐지는거 때문에 똑같은 놈이 3개임-_ -)
	 * @param pack_id
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/gpack/category/getKidsEduGpackCategoryList", method=RequestMethod.GET )
	public String getKidsEduGpackCategoryList(
			@RequestParam(value="pack_id", required=false, defaultValue="P0003") String pack_id, 
			Model model) throws Exception{
		
		GpackCategoryVO categoryVO = new GpackCategoryVO();
		categoryVO.setPack_id(pack_id);
		List result = service.getGpackCategoryList(categoryVO);

		model.addAttribute("pack_id", pack_id);
		model.addAttribute("result", result);

		return "/admin/gpack/category/getGpackCategoryListNew";
		//return "/admin/gpack/category/getGpackCategoryList";
	}
	
	/**
	 * 카테고리 목록 조회 화면 (메뉴트리 펼쳐지는거 때문에 똑같은 놈이 3개임-_ -)
	 * @param pack_id
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/gpack/category/getSpecEventGpackCategoryList", method=RequestMethod.GET )
	public String getSpecEventGpackCategoryList(
			@RequestParam(value="pack_id", required=false, defaultValue="P0004") String pack_id, 
			Model model) throws Exception{
		
		GpackCategoryVO categoryVO = new GpackCategoryVO();
		categoryVO.setPack_id(pack_id);
		List result = service.getGpackCategoryList(categoryVO);
		
		model.addAttribute("pack_id", pack_id);
		model.addAttribute("result", result);
		
		return "/admin/gpack/category/getGpackCategoryListNew";
		//return "/admin/gpack/category/getGpackCategoryList";
	}

	/**
	 * 카테고리 목록 수정/등록 화면
	 * @param pack_id
	 * @param category_gb
	 * @param category_id
	 * @param view_gb
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/gpack/category/getGpackCategoryView", method=RequestMethod.GET)
	public String getGpackCategoryView(
			@RequestParam(value="pack_id") String pack_id, 
			@RequestParam(value="category_id", required=false) String category_id, 
			Model model) throws Exception{

		//카테고리 정보 
		GpackCategoryVO categoryVO = new GpackCategoryVO();
		categoryVO.setPack_id(pack_id);
		if(!category_id.isEmpty()){
			categoryVO.setCategory_id(category_id);
			categoryVO = service.getGpackCategoryView(categoryVO);
		}
		model.addAttribute("categoryVO", categoryVO);
		
		
		//자동편성(IMCS 카테고리 연동) 정보
		GpackContentsAutoVO autoVO = new GpackContentsAutoVO();
		autoVO.setPack_id(pack_id);
		autoVO.setCategory_id(category_id);
		autoVO = contentsService.getGpackContentsAutoView(autoVO);
		model.addAttribute("autoVO", autoVO);

		// 자동편성(IMCS 카테고리 연동) 정보
		// (팩이 최상위 카테고리가 되었으니까 팩 정보를 조회해서 IMCS_CATEGORY_ID를 조회한 후 다시 조회)
		GPackPackVO packVO = new GPackPackVO();
		packVO.setPack_id(pack_id);
		packVO = packViewService.getGpackPackDetail(packVO);
		List categoryResult = contentsService.getImcsCategoryList(packVO.getImcs_category_id(), "First","");
		model.addAttribute("categoryResult", categoryResult);
		
		return "/admin/gpack/category/getGpackCategoryViewNew";
		//return "/admin/gpack/category/getGpackCategoryView";
	}

	/**
	 * 카테고리 목록 수정/등록
	 * @param pack_id
	 * @param category_id
	 * @param category_nm
	 * @param category_gb
	 * @param contents_type
	 * @param vod_youtube_gb
	 * @param auto_yn
	 * @param applydate_gb
	 * @param applydate
	 * @param version
	 * @param ordered
	 * @param use_yn
	 * @param view_gb
	 * @param login_id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/gpack/category/InsertGpackCategory", method=RequestMethod.POST)
	public @ResponseBody String InsertGpackCategory(
			@RequestParam(value="pack_id") String pack_id, 
			@RequestParam(value="category_id", required=false) String category_id, 
			@RequestParam(value="category_nm") String category_nm,
			@RequestParam(value="category_comment", required=false) String category_comment,
			@RequestParam(value="auto_yn") String auto_yn,
			@RequestParam(value="ordered") String ordered,
			@RequestParam(value="use_yn") String use_yn,
			@RequestParam(value="view_gb") String view_gb, 
			@RequestParam(value="auto_set_id", required=false) String auto_set_id, 
			@RequestParam(value="imcs_category_id") String imcs_category_id, 
			@RequestParam(value="smartUXManager") String login_id) throws Exception{
		
		String resultcode = "";
		String resultmessage = "";
		
		try{
			GpackCategoryVO categoryVO = new GpackCategoryVO();
			
			categoryVO.setPack_id(pack_id);
			categoryVO.setCategory_id(category_id);
			categoryVO.setCategory_nm(category_nm);
			categoryVO.setCategory_comment(category_comment);
			categoryVO.setAuto_yn(auto_yn);
			categoryVO.setOrdered(ordered);
			categoryVO.setUse_yn(use_yn);
			categoryVO.setCreate_id(login_id);
			categoryVO.setUpdate_id(login_id);
			
			//카테고리 ID 조회
			if(category_id.isEmpty() || category_id.equals("")){
				category_id = service.getNewGpackCategoryId();
				categoryVO.setCategory_id(category_id);
			}
			
			//카테고리 정보 저장
			validateInsertGpackCategory(categoryVO, view_gb);
			service.insertGpackCategory(categoryVO);

			//자동편성(IMCS 카테고리 연동) 정보 저장
			GpackContentsAutoVO contentsAutoVO = new GpackContentsAutoVO();
			contentsAutoVO.setPack_id(pack_id);
			contentsAutoVO.setCategory_id(category_id);
			contentsAutoVO.setAuto_set_id(auto_set_id);
			contentsAutoVO.setImcs_category_id(imcs_category_id);
			contentsAutoVO.setUse_yn(use_yn);
			contentsAutoVO.setCreate_id(login_id);
			contentsAutoVO.setUpdate_id(login_id);
			validateInsertGpackAuto(contentsAutoVO);
			//카테고리와 연동 카테고리는 1대 1이니까 삭제하고 넣자. 겹친다.
			contentsService.deleteGpackContentsAuto(contentsAutoVO);
			contentsService.insertGpackContentsAuto(contentsAutoVO);

			
			resultcode = SmartUXProperties.getProperty("flag.success");
			resultmessage = SmartUXProperties.getProperty("message.success");
			
		}catch(Exception e){
			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}
		return "{\"flag\" : \"" + resultcode + "\", \"message\" : \"" + resultmessage + "\"}";
	}

	/**
	 * InsertCategory 함수에 대한 validation 작업 함수
	 * @param categoryVO
	 * @param view_gb
	 * @param contentsCnt
	 * @throws SmartUXException
	 */
	private void validateInsertGpackCategory(GpackCategoryVO categoryVO, String view_gb) throws SmartUXException{
		SmartUXException exception = new SmartUXException();
		if(!(StringUtils.hasText(categoryVO.getPack_id()))){
			exception.setFlag("NOT FOUND PACK ID");
			exception.setMessage("PACK ID가 없습니다.");
			throw exception;
		}
		
		if(!view_gb.equals("INSERT")){
			if(!(StringUtils.hasText(categoryVO.getCategory_id()))){
				exception.setFlag("NOT FOUND CATEGORY ID");
				exception.setMessage("CATEGORY ID가 없습니다.");
				throw exception;
			}
		}
		
		if(!(StringUtils.hasText(categoryVO.getCategory_nm()))){
			exception.setFlag("NOT FOUND CATEGORY NM");
			exception.setMessage("카테고리 명은 필수로 들어가야 합니다.");
			throw exception;
		}
		
		if((categoryVO.getCategory_nm()).length() > 100){
			exception.setFlag("CATEGORY NM LENGTH");
			exception.setMessage("카테고리 명은 100자 이내이어야 합니다");
			throw exception;
		}
		
		if(!(StringUtils.hasText(categoryVO.getAuto_yn()))){
			exception.setFlag("NOT FOUND PACK ID");
			exception.setMessage("자동여부 선택은 필수입니다.");
			throw exception;
		}
		
		if(!(StringUtils.hasText(categoryVO.getUse_yn()))){
			exception.setFlag("NOT FOUND PACK ID");
			exception.setMessage("사용여부 선택은 필수입니다.");
			throw exception;
		}
	}

	/**
	 * insertGpackContentsAuto 함수에 대한 validation 작업 함수
	 * @param contentsAutoVO
	 * @throws SmartUXException
	 */
	private void validateInsertGpackAuto(GpackContentsAutoVO contentsAutoVO) throws SmartUXException{
		SmartUXException exception = new SmartUXException();
		
		if(!(StringUtils.hasText(contentsAutoVO.getPack_id()))){
			exception.setFlag("NOT FOUND PACK ID");
			exception.setMessage("PACK ID가 없습니다.");
			throw exception;
		}
		
		if(!(StringUtils.hasText(contentsAutoVO.getCategory_id()))){
			exception.setFlag("NOT FOUND CATEGORY ID");
			exception.setMessage("CATEGORY ID가 없습니다.");
			throw exception;
		}
		if(!(StringUtils.hasText(contentsAutoVO.getImcs_category_id()))){
			exception.setFlag("NOT FOUND IMCS IMCS_CATEGORY_ID");
			exception.setMessage("IMCS 카테고리 ID가 없습니다.");
			throw exception;
		}
	}

	/**
	 * 삭제 처리 작업
	 * @param pack_id
	 * @param category_ids
	 * @return
	 */
	@RequestMapping(value="/admin/gpack/category/deleteGpackCategory", method=RequestMethod.POST)
	public @ResponseBody String deleteGpackCategory(
			@RequestParam(value="pack_id") String pack_id, 
			@RequestParam(value="category_ids[]") String [] category_ids) {
		
		String resultcode = "";
		String resultmessage = "";
		try{
			validateDeleteGpackCategory(pack_id, category_ids);		// 입력받은 파라미터 값들에 대한 validation 작업 진행
			service.deleteGpackCategory(pack_id, category_ids);
			
			resultcode = SmartUXProperties.getProperty("flag.success");
			resultmessage = SmartUXProperties.getProperty("message.success");
		}catch(Exception e){
			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}
		return "{\"flag\" : \"" + resultcode + "\", \"message\" : \"" + resultmessage + "\"}";
	}
	
	/**
	 * deleteCategory 함수에 대한 validation 작업함수
	 * @param pack_id
	 * @param category_ids
	 * @throws SmartUXException
	 */
	private void validateDeleteGpackCategory(String pack_id, String [] category_ids) throws SmartUXException{
		SmartUXException exception = new SmartUXException();

		if(!(StringUtils.hasText(pack_id))){
			exception.setFlag("NOT FOUND PACK ID");
			exception.setMessage("PACK ID가 없습니다.");
			throw exception;
		}
		
		if((category_ids == null) ||(category_ids.length == 0)){
			exception.setFlag("NOT FOUND CATEGORY ID");
			exception.setMessage("CATEGORY ID는 필수로 들어가야 합니다");
			throw exception;
		}
	}

	/**
	 * 카테고리 순서바꾸기 화면
	 * @param pack_id
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/gpack/category/updateGpackCategoryOrderby", method=RequestMethod.GET )
	public String changeGpackCategoryOrder(
			@RequestParam(value="pack_id") String pack_id, 
			Model model) throws Exception{
		
		GpackCategoryVO categoryVO = new GpackCategoryVO();
		categoryVO.setPack_id(pack_id);
		List result = service.getGpackCategoryList(categoryVO);

		model.addAttribute("result", result);
		
		return "/admin/gpack/category/updateGpackCategoryOrderby";
	}
	
	/**
	 * 카테고리 순서 바꾸기 작업
	 * @param pack_id
	 * @param category_gb
	 * @param category_ids
	 * @param login_id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/gpack/category/updateGpackCategoryOrderby", method=RequestMethod.POST)
	public @ResponseBody String updateGpackCategoryOrderby(
			@RequestParam(value="pack_id") String pack_id, 
			@RequestParam(value="category_id[]") String [] category_ids,
			@RequestParam(value="smartUXManager") String login_id) throws Exception{
		
		String update_id = login_id;			// 수정자 ID(차후에 로그인 한 사람의 로그인 ID로 바꿀것
		
		String resultcode = "";
		String resultmessage = "";
		try{
			validateDeleteGpackCategory(pack_id, category_ids);		// 내용이 똑같아서 delete 체크할때와 같은 함수임 
			service.updateGpackCategoryOrderby(pack_id, category_ids, update_id);
			
			resultcode = SmartUXProperties.getProperty("flag.success");
			resultmessage = SmartUXProperties.getProperty("message.success");
		}catch(Exception e){
			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}
		return "{\"flag\" : \"" + resultcode + "\", \"message\" : \"" + resultmessage + "\"}";
		
	}

	/**
	 * 미리보기 화면
	 * @param pack_id
	 * @param category_gb
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/gpack/category/previewGpackCategory", method=RequestMethod.GET )
	public String previewGpackCategory(@RequestParam(value="pack_id") String pack_id, 
			@RequestParam(value="preview_gb", required=false, defaultValue="") String preview_gb, 
			Model model) throws Exception{

		//배경이미지 path (mainpanel)
		//String bg_img_url = SmartUXProperties.getProperty("imgserver.weburl") + SmartUXProperties.getProperty("imgserver.imgpath");
		String bg_img_url = panelViewservice.getImageServerURL("P");
		
		// 카테고리 정보
		GpackCategoryVO categoryVO = new GpackCategoryVO();
		categoryVO.setPack_id(pack_id);
		categoryVO.setUse_yn("Y");
		List categorylist = service.previewGpackCategory(categoryVO, preview_gb);
		
		// 각 콘텐츠 정보는 화면에서 불러올거긔!
		
		model.addAttribute("bg_img_url", bg_img_url);
		model.addAttribute("categorylist", categorylist);
		model.addAttribute("preview_gb", preview_gb);
		
		return "/admin/gpack/category/previewGpackCategoryNew";
		//return "/admin/gpack/category/previewGpackCategory";
	}
	
	/**
	 * 미리보기 - 콘텐츠 
	 * @param pack_id
	 * @param category_id
	 * @param contents_type
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/gpack/category/getPrevCategoryContentsList", method=RequestMethod.POST )
	public @ResponseBody String getPrevCategoryContentsList(
			@RequestParam(value="pack_id")       String pack_id, 
			@RequestParam(value="category_id")   String category_id, 
			@RequestParam(value="preview_gb", required=false, defaultValue="")   String preview_gb, 
			Model model) throws Exception{

		List contentsList = new ArrayList();
		if(preview_gb.equals("BIZ")){
			contentsList = contentsService.getPrevCategoryContentsBizList(pack_id, category_id);
		}else{
			contentsList = contentsService.getPrevCategoryContentsList(pack_id, category_id);
		}
		JSONArray jsonArray = new JSONArray();
		if(contentsList.size()>0){
			   jsonArray = JSONArray.fromObject(contentsList);
		}
		return jsonArray.toString();
	}
}
