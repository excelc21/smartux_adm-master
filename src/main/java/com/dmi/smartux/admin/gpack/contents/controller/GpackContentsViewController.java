package com.dmi.smartux.admin.gpack.contents.controller;

import java.io.File;
import java.util.List;

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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.dmi.smartux.admin.gpack.contents.service.GpackContentsViewService;
import com.dmi.smartux.admin.gpack.contents.vo.GpackContentsAutoVO;
import com.dmi.smartux.admin.gpack.contents.vo.GpackContentsVO;
import com.dmi.smartux.admin.gpack.event.service.GpackPromotionViewService;
import com.dmi.smartux.admin.gpack.event.vo.GpackPromotionVO;
import com.dmi.smartux.admin.gpack.pack.service.GPackPackViewService;
import com.dmi.smartux.admin.gpack.pack.vo.GPackPackVO;
import com.dmi.smartux.common.exception.ExceptionHandler;
import com.dmi.smartux.common.exception.SmartUXException;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.HTMLCleaner;

@Controller
public class GpackContentsViewController {
	private final Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	GpackContentsViewService service;

	@Autowired
	GPackPackViewService packService;
	
	/** 프로모션 서비스 */
	@Autowired
	GpackPromotionViewService promotionViewService;
	
	/**
	 * 직접편성 조회 화면
	 * @param pack_id
	 * @param category_id
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/gpack/contents/getGpackContentsList", method=RequestMethod.GET )
	public String getGpackContentsList(
			@RequestParam(value="pack_id")     String pack_id, 
			@RequestParam(value="category_id") String category_id, 
			Model model) throws Exception{
		
		// 프로모션 상세 조회
		GpackPromotionVO promotionVO = new GpackPromotionVO();
		promotionVO.setPack_id(pack_id);
		promotionVO.setCategory_id(category_id);
		GpackPromotionVO gpackPromotionVO = promotionViewService.getGpackPromotionDetail(promotionVO);
		model.addAttribute("promotionVO", gpackPromotionVO);
		
		GpackContentsVO contentsVO = new GpackContentsVO();
		contentsVO.setPack_id(pack_id);
		contentsVO.setCategory_id(category_id);
		
		List<GpackContentsVO> contentsList = service.getGpackContentsList(contentsVO);
		model.addAttribute("contentsList", contentsList);

		// 팩이 최상위 카테고리가 되었으니까 팩 정보를 조회해서 IMCS_CATEGORY_ID를 조회한 후 다시 조회
		GPackPackVO packVO = new GPackPackVO();
		packVO.setPack_id(pack_id);
		packVO = packService.getGpackPackDetail(packVO);
		List categoryResult = service.getImcsCategoryList(packVO.getImcs_category_id(), "First","");
		model.addAttribute("categoryResult", categoryResult);
		
		return "/admin/gpack/contents/getGpackContentsList";
	}

	/**
	 * 직접편성 조회
	 * @param pack_id
	 * @param category_id
	 * @param contents_id
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/gpack/contents/getGpackContentsView", method=RequestMethod.POST )
	public ResponseEntity<String> getGpackContentsView(
			@RequestParam(value="pack_id")     String pack_id, 
			@RequestParam(value="category_id") String category_id, 
			@RequestParam(value="contents_id") String contents_id, 
			Model model) throws Exception{
		
		GpackContentsVO contentsVO = new GpackContentsVO();
		contentsVO.setPack_id(pack_id);
		contentsVO.setCategory_id(category_id);
		contentsVO.setContents_id(contents_id);
		
		contentsVO = service.getGpackContentsView(contentsVO);
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(contentsVO).toString(), responseHeaders, HttpStatus.CREATED);
	}
	
	/**
	 * imcs 카테고리 조회
	 * @param category_id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/gpack/contents/getGpackImcsCategoryList", method=RequestMethod.POST)
	public ResponseEntity<String> getGpackImcsCategoryList(
			@RequestParam(value="category_id") String category_id,
			@RequestParam(value="product_id", required=false, defaultValue="") String product_id
			) throws Exception{
		
		category_id 	= HTMLCleaner.clean(category_id);
		
		List result  = service.getImcsCategoryList(category_id, "", product_id);
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(result).toString(), responseHeaders, HttpStatus.CREATED);
	}
	
	/**
	 * 직접편성 저장(추가/수정)
	 * @param request
	 * @param response
	 * @param pack_id
	 * @param category_id
	 * @param vod_id
	 * @param vod_nm
	 * @param series_yn
	 * @param exist_img_use_yn
	 * @param img_file
	 * @param imcs_category_id
	 * @param album_id
	 * @param ordered
	 * @param use_yn
	 * @param view_gb
	 * @param chnl_service_id
	 * @param product_id
	 * @param dal_type
	 * @param login_id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/gpack/contents/insertGpackContents", method=RequestMethod.POST)
	public @ResponseBody String insertGpackContents(
			HttpServletRequest request, 
			HttpServletResponse response, 
			@RequestParam(value="pack_id") 			String pack_id, 
			@RequestParam(value="category_id") 		String category_id, 
			@RequestParam(value="contents_id",		required=false) String contents_id, 
			@RequestParam(value="contents_nm") 		String contents_nm,
			@RequestParam(value="movepath_type") 	String movepath_type,
			@RequestParam(value="movepath",		required=false) String movepath,
			@RequestParam(value="imcs_category_id",	required=false) String imcs_category_id,
			@RequestParam(value="album_id",			required=false) String album_id,
			@RequestParam(value="img_file",			required=false) MultipartFile img_file,
			@RequestParam(value="ordered",			required=false) String ordered,
			@RequestParam(value="use_yn") 			String use_yn,
			@RequestParam(value="view_gb") 			String view_gb,
			@RequestParam(value="chnl_service_id",	required=false) String chnl_service_id,
			@RequestParam(value="product_id",		required=false) String product_id,
			@RequestParam(value="dal_type",			required=false) String dal_type,
			@RequestParam(value="smartUXManager") 	String login_id) throws Exception{
		
		String resultcode = "";
		String resultmessage = "";

		//System.out.println("					insertGpackContents");
		try{
			GpackContentsVO contentsVO = new GpackContentsVO();

			// 배경 이미지 업로드
			String bg_img_file = "";
			String bg_ext = "";
			String realPath = "";
			String newFilename = "";
			String packPath = "";
			
			if("P0001".equals(pack_id)) {
				packPath = SmartUXProperties.getProperty("gpack.imgupload.tv.dir");
			} else if("P0002".equals(pack_id)) {
				packPath = SmartUXProperties.getProperty("gpack.imgupload.movie.dir");
			} else if("P0003".equals(pack_id)) {
				packPath = SmartUXProperties.getProperty("gpack.imgupload.kids.dir");
			} else {
				packPath = SmartUXProperties.getProperty("gpack.imgupload.spec.dir");
			}
			
			if(!img_file.isEmpty()){
				
				// 파일명은 "gpack_contents_bg__팩ID_시스템타임.확장자"
				String tmp_img_file = img_file.getOriginalFilename();
				
				if(tmp_img_file.length() > 0){
					bg_ext = tmp_img_file.substring(tmp_img_file.lastIndexOf(".")+1,tmp_img_file.length());
					bg_img_file = "gpack_contents_bg_" + pack_id + "_" + Long.toString(System.currentTimeMillis()) + "." + bg_ext;
					realPath = SmartUXProperties.getProperty("imgupload.dir") + "/" + packPath;
					newFilename = realPath + "/" + bg_img_file;

					File tmp_file = new File(newFilename);
					//이미지 폴더가 존재하지 않으면 폴더 만들기 
					if(!tmp_file.exists()){ 
						tmp_file.mkdirs();
					}
					img_file.transferTo(tmp_file);
				}
				System.out.println("----------------------------이미지업로드TEST------------------------");
				System.out.println("tmp_img_file	>> "+tmp_img_file);
				System.out.println("bg_ext			>> "+bg_ext);
				System.out.println("bg_img_file		>> "+bg_img_file);
				System.out.println("realPath		>> "+realPath);
				System.out.println("newFilename		>> "+newFilename);
				System.out.println("--------------------------------------------------------------------");
			}
			
			contentsVO.setPack_id(pack_id);
			contentsVO.setCategory_id(category_id);
			contentsVO.setContents_id(contents_id);
			contentsVO.setContents_nm(contents_nm);
			contentsVO.setMovepath_type(movepath_type);
			if("MT001".equals(movepath_type)){
				contentsVO.setMovepath(product_id);
			}else if("MT004".equals(movepath_type)){
				contentsVO.setMovepath(movepath);
				contentsVO.setDal_type(dal_type);
			}else if("MT005".equals(movepath_type)){
				contentsVO.setMovepath(chnl_service_id);
			}else {
				contentsVO.setMovepath(movepath);
			}
			contentsVO.setImcs_category_id(imcs_category_id);
			contentsVO.setAlbum_id(album_id);
			//contentsVO.setImg_path(realPath);
			contentsVO.setImg_file(bg_img_file);
			contentsVO.setOrdered(ordered);
			contentsVO.setUse_yn(use_yn);
			contentsVO.setCreate_id(login_id);
			contentsVO.setUpdate_id(login_id);

			/*
			System.out.println("----------------------------insertGpackVod TEST------------------------");
			System.out.println("view_gb : "+view_gb);
			System.out.println("pack_id : "+pack_id);
			System.out.println("category_id : "+category_id);
			System.out.println("contents_id : "+contents_id);
			System.out.println("contents_nm : "+contents_nm);
			System.out.println("movepath_type : "+movepath_type);
			System.out.println("movepath : "+movepath);
			System.out.println("imcs_category_id : "+imcs_category_id);
			System.out.println("album_id : "+album_id);
			System.out.println("img_file : "+bg_img_file);
			System.out.println("ordered : "+ordered);
			System.out.println("use_yn : "+use_yn);
			System.out.println("--------------------------------------------------------------------");
			*/
			validateInsertGpackVod(contentsVO, view_gb);

			service.insertGpackContents(contentsVO);
			resultcode = SmartUXProperties.getProperty("flag.success");
			resultmessage = SmartUXProperties.getProperty("message.success");
			/*
			if(view_gb.equals("INSERT")){
				
				service.insertGpackContents(contentsVO);

				resultcode = SmartUXProperties.getProperty("flag.success");
				resultmessage = SmartUXProperties.getProperty("message.success");
				
			}else if(view_gb.equals("MODIFY")){
				
				service.updateGpackContents(contentsVO);

				resultcode = SmartUXProperties.getProperty("flag.success");
				resultmessage = SmartUXProperties.getProperty("message.success");
			}
			*/
		
		}catch(Exception e){
			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}
		return "{\"flag\" : \"" + resultcode + "\", \"message\" : \"" + resultmessage + "\"}";
	}

	/**
	 * insertGpackContents 함수에 대한 validation 작업 함수
	 * @param contentsVO
	 * @param view_gb
	 * @throws SmartUXException
	 */
	private void validateInsertGpackVod(GpackContentsVO contentsVO, String view_gb) throws SmartUXException{
		SmartUXException exception = new SmartUXException();
		
		if(!(StringUtils.hasText(contentsVO.getPack_id()))){
			exception.setFlag("NOT FOUND PACK ID");
			exception.setMessage("PACK ID가 없습니다.");
			throw exception;
		}
		
		if(!(StringUtils.hasText(contentsVO.getCategory_id()))){
			exception.setFlag("NOT FOUND CATEGORY ID");
			exception.setMessage("CATEGORY ID가 없습니다.");
			throw exception;
		}
		
		if(!view_gb.equals("INSERT")){
			if(!(StringUtils.hasText(contentsVO.getContents_id()))){
				exception.setFlag("NOT FOUND CONTENTS ID");
				exception.setMessage("CONTENTS ID가 없습니다.");
				throw exception;
			}
		}
		
		if(!(StringUtils.hasText(contentsVO.getContents_nm()))){
			exception.setFlag("NOT FOUND CONTENTS NM");
			exception.setMessage("CONTENTS 명은 필수로 들어가야 합니다.");
			throw exception;
		}
		
		if((contentsVO.getContents_nm()).length() > 100){
			exception.setFlag("CONTENTS NM LENGTH");
			exception.setMessage("CONTENTS 명은 100자 이내이어야 합니다");
			throw exception;
		}
		
		//이동경로 타입에 따라 (MT001 : 월정액, MT002 : VOD상세, MT003 : 양방향어플, MT004 : DAL, MT005 : 채널, MT006 : 카테고리)
		String movepath_type = contentsVO.getMovepath_type();
		if(!(StringUtils.hasText(movepath_type))){
			exception.setFlag("NOT FOUND MOVEPATH_TYPE");
			exception.setMessage("이동경로 타입은 필수로 들어가야 합니다.");
			throw exception;
		}
		
		// VOD상세가 아닐 경우, 포스터 이미지 필요함
		if(view_gb.equals("INSERT") && !movepath_type.equals("MT002") && !movepath_type.equals("MT006")){
			if(!(StringUtils.hasText(contentsVO.getImg_file()))){
				exception.setFlag("NOT FOUND IMG_FILE");
				exception.setMessage("포스터 이미지가 없습니다.");
				throw exception;
			}
		}
		
		if(movepath_type.equals("MT001") || movepath_type.equals("MT004") || movepath_type.equals("MT003") || movepath_type.equals("MT005")){
			//이동경로
			if(!(StringUtils.hasText(contentsVO.getMovepath()))){
				exception.setFlag("NOT FOUND MOVEPATH");
				exception.setMessage("이동경로가 없습니다.");
				throw exception;
			}
		}else if(movepath_type.equals("MT006")){
			//IMCS 카테고리ID
			if(!(StringUtils.hasText(contentsVO.getImcs_category_id()))){
				exception.setFlag("NOT FOUND IMCS_CATEGORY_ID");
				exception.setMessage("IMCS 카테고리 ID가 없습니다.");
				throw exception;
			}
		}else if(movepath_type.equals("MT002")){
			//IMCS 카테고리ID, ALBUM_ID
			if(!(StringUtils.hasText(contentsVO.getImcs_category_id()))){
				exception.setFlag("NOT FOUND IMCS_CATEGORY_ID");
				exception.setMessage("IMCS 카테고리 ID가 없습니다.");
				throw exception;
			}

			if(!(StringUtils.hasText(contentsVO.getAlbum_id()))){
				exception.setFlag("NOT FOUND IMCS ALBUM_ID");
				exception.setMessage("IMCS 앨범 ID가 없습니다.");
				throw exception;
			}
			
		}
		
	}
	
	/**
	 * 직접편성에 대한 삭제 
	 * @param pack_id
	 * @param category_id
	 * @param vod_ids
	 * @param login_id
	 * @return
	 */
	@RequestMapping(value="/admin/gpack/contents/deleteGpackContents", method=RequestMethod.POST)
	public @ResponseBody String deleteGpackContents(
			@RequestParam(value="pack_id")     String pack_id, 
			@RequestParam(value="category_id") String category_id,
			@RequestParam(value="contents_ids[]")    String [] contents_ids,
			@RequestParam(value="smartUXManager") String login_id) {
		
		String resultcode = "";
		String resultmessage = "";
		try{
			validateDeleteGpackVod(pack_id, category_id, contents_ids);		// 입력받은 파라미터 값들에 대한 validation 작업 진행
			service.deleteGpackContents(pack_id, category_id, contents_ids, login_id);
			
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
	 * deleteGpackContents 함수에 대한 validation 작업함수
	 * @param pack_id
	 * @param category_id
	 * @param vod_ids
	 * @throws SmartUXException
	 */
	private void validateDeleteGpackVod(String pack_id, String category_id, String[] contents_ids) throws SmartUXException{
		SmartUXException exception = new SmartUXException();

		if(!(StringUtils.hasText(pack_id))){
			exception.setFlag("NOT FOUND PACK ID");
			exception.setMessage("PACK ID가 없습니다.");
			throw exception;
		}
		
		if(!(StringUtils.hasText(category_id))){
			exception.setFlag("NOT FOUND CATEGORY ID");
			exception.setMessage("CATEGORY ID가 없습니다.");
			throw exception;
		}

		if((contents_ids == null) ||(contents_ids.length == 0)){
			exception.setFlag("NOT FOUND CONTENTS ID");
			exception.setMessage("CONTENTS ID가 없습니다.");
			throw exception;
		}
	}
	
	/**
	 * 자동편성(IMCS 카테고리 연동)  조회 화면
	 * @param pack_id
	 * @param category_id
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/gpack/contents/getGpackContentsAutoView", method=RequestMethod.GET )
	public String getGpackContentsAutoView(
			@RequestParam(value="pack_id")     String pack_id, 
			@RequestParam(value="category_id") String category_id, 
			Model model) throws Exception{
		
		GpackContentsAutoVO autoVO = new GpackContentsAutoVO();
		autoVO.setPack_id(pack_id);
		autoVO.setCategory_id(category_id);

		autoVO = service.getGpackContentsAutoView(autoVO);
		model.addAttribute("autoVO", autoVO);

		// 팩이 최상위 카테고리가 되었으니까 팩 정보를 조회해서 IMCS_CATEGORY_ID를 조회한 후 다시 조회
		GPackPackVO packVO = new GPackPackVO();
		packVO.setPack_id(pack_id);
		packVO = packService.getGpackPackDetail(packVO);
		List categoryResult = service.getImcsCategoryList(packVO.getImcs_category_id(), "First","");
		model.addAttribute("categoryResult", categoryResult);
		
		return "/admin/gpack/contents/getGpackContentsAutoView";
	}

	/**
	 * 자동편성(IMCS 카테고리 연동)  저장 (추가/수정)
	 * @param pack_id
	 * @param category_id
	 * @param tot_cnt
	 * @param order_type
	 * @param imcs_category_id
	 * @param use_yn
	 * @param login_id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/gpack/contents/insertGpackContentsAuto", method=RequestMethod.POST)
	public @ResponseBody String insertGpackContentsAuto(
			@RequestParam(value="pack_id") String pack_id, 
			@RequestParam(value="category_id") String category_id, 
			@RequestParam(value="auto_set_id", required=false) String auto_set_id,
			@RequestParam(value="imcs_category_id") String imcs_category_id,
			@RequestParam(value="ordered", required=false) String ordered,
			@RequestParam(value="use_yn") String use_yn,
			@RequestParam(value="smartUXManager") String login_id) throws Exception{
		
		String resultcode = "";
		String resultmessage = "";
		
		try{
			GpackContentsAutoVO contentsAutoVO = new GpackContentsAutoVO();
			
			contentsAutoVO.setPack_id(pack_id);
			contentsAutoVO.setCategory_id(category_id);
			contentsAutoVO.setAuto_set_id(auto_set_id);
			contentsAutoVO.setImcs_category_id(imcs_category_id);
			contentsAutoVO.setUse_yn(use_yn);
			contentsAutoVO.setCreate_id(login_id);
			contentsAutoVO.setUpdate_id(login_id);
			
			validateInsertGpackAuto(contentsAutoVO);

			//service.deleteGpackContentsAuto(contentsAutoVO);
			service.insertGpackContentsAuto(contentsAutoVO);

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
	 * 자동편성(IMCS 카테고리 연동) 에 대한 삭제 
	 * @param pack_id
	 * @param category_id
	 * @return
	 */
	@RequestMapping(value="/admin/gpack/contents/deleteGpackContentsAuto", method=RequestMethod.POST)
	public @ResponseBody String deleteGpackContentsAuto(
			@RequestParam(value="pack_id")     String pack_id, 
			@RequestParam(value="category_id") String category_id, 
			@RequestParam(value="auto_set_id") String auto_set_id) {
		
		String resultcode = "";
		String resultmessage = "";
		try{
			GpackContentsAutoVO contentsAutoVO = new GpackContentsAutoVO();
			
			contentsAutoVO.setPack_id(pack_id);
			contentsAutoVO.setCategory_id(category_id);
			contentsAutoVO.setAuto_set_id(auto_set_id);
			
			validateDeleteGpackAuto(contentsAutoVO);		// 입력받은 파라미터 값들에 대한 validation 작업 진행
			service.deleteGpackContentsAuto(contentsAutoVO);
			
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
	 * deleteGpackContentsAuto 함수에 대한 validation 작업함수
	 * @param contentsVO
	 * @throws SmartUXException
	 */
	private void validateDeleteGpackAuto(GpackContentsAutoVO contentsAutoVO) throws SmartUXException{
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
	}
	
	/**
	 * 콘텐츠 순서바꾸기 화면
	 * @param pack_id
	 * @param category_id 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/gpack/contents/updateGpackContentsOrderby", method=RequestMethod.GET )
	public String updateGpackContentsOrderby(
			@RequestParam(value="pack_id")       String pack_id, 
			@RequestParam(value="category_id")   String category_id, 
			Model model) throws Exception{
		
		// 프로모션 상세 조회
		GpackPromotionVO promotionVO = new GpackPromotionVO();
		promotionVO.setPack_id(pack_id);
		promotionVO.setCategory_id(category_id);
		GpackPromotionVO gpackPromotionVO = promotionViewService.getGpackPromotionDetail(promotionVO);
		model.addAttribute("promotionVO", gpackPromotionVO);

		GpackContentsVO contentsVO = new GpackContentsVO();
		contentsVO.setPack_id(pack_id);
		contentsVO.setCategory_id(category_id);
		
		List result = service.getGpackContentsList(contentsVO);
		
		model.addAttribute("result", result);
		
		return "/admin/gpack/contents/updateGpackContentsOrderby";
	}

	
	/**
	 * 콘텐츠 순서바꾸기 작업
	 * @param pack_id
	 * @param category_id
	 * @param contents_ids
	 * @param login_id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/gpack/contents/updateGpackContentsOrderby", method=RequestMethod.POST)
	public @ResponseBody String updateGpackContentsOrderby(
			@RequestParam(value="pack_id")        String pack_id, 
			@RequestParam(value="category_id")    String category_id, 
			@RequestParam(value="contents_id[]")  String [] contents_ids,
			@RequestParam(value="smartUXManager") String login_id) throws Exception{
		
		String update_id = login_id;			// 수정자 ID(차후에 로그인 한 사람의 로그인 ID로 바꿀것
		String resultcode = "";
		String resultmessage = "";
		try{
			validateDeleteGpackVod(pack_id, category_id, contents_ids);
			service.updateGpackContentsOrderby(pack_id, category_id, contents_ids, update_id);
			resultcode = SmartUXProperties.getProperty("flag.success");
			resultmessage = SmartUXProperties.getProperty("message.success");
			
		}catch(Exception e){
			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}
		return "{\"flag\" : \"" + resultcode + "\", \"message\" : \"" + resultmessage + "\"}";
		
	}
	
}
