package com.dmi.smartux.admin.smartstart.controller;

import java.util.ArrayList;
import java.util.List;

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

import com.dmi.smartux.common.exception.ExceptionHandler;
import com.dmi.smartux.common.exception.SmartUXException;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.util.HTMLCleaner;

import com.dmi.smartux.admin.code.vo.CodeItemVO;
import com.dmi.smartux.admin.mainpanel.vo.PanelVO;
import com.dmi.smartux.admin.smartstart.service.ItemListService;
import com.dmi.smartux.admin.smartstart.vo.ItemListVO;

@Controller
public class ItemListController {
	
	private final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	ItemListService service;
	
	/**
	 * SmartStart 항목정보 목록 조회 화면
	 * @param model		Model 객체
	 * @return			SmartStart 항목정보 목록 조회 화면 URL
	 */
	@RequestMapping(value="/admin/smartstart/getItemList")
	public String getItemList(Model model) throws Exception{
		List<CodeItemVO> result = service.getItemList();
		model.addAttribute("result", result);
		return "/admin/smartstart/getItemList";
	}
	
	/**
	 * SmartStart Item 코드값 상세 조회 화면
	 * @param code			SmartStart Item을 상세조회 하고자 하는 code
	 * @param item_code		SmartStart Item을 상세조회 하고자 하는 item_code
	 * @param model			Model 객체
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/smartstart/viewItemDetail", method=RequestMethod.GET)
	public String viewItemDetail(
			@RequestParam(value="code")			String code, 
			@RequestParam(value="item_code") 	String item_code,
			Model model
			) throws Exception{
		
		HTMLCleaner cleaner = new HTMLCleaner();
		code 	= cleaner.clean(code);
		item_code 	= cleaner.clean(item_code);
		
		CodeItemVO result = service.viewItem(code,item_code);
		model.addAttribute("result", result);
		return "/admin/smartstart/viewItemDetail";
	}

	/**
	 * SmartStart Item 코드값 수정 처리 작업
	 * @param code				SmartStart Item을 수정하고자 하는 code
	 * @param item_code			SmartStart Item의 기존 item_code
	 * @param newItem_code		SmartStart Item의 새로운 item_code
	 * @param item_nm			SmartStart Item의 item_nm
	 * @param use_yn			SmartStart Item의 사용여부(Y/N)
	 * @return
	 */
	@RequestMapping(value="/admin/smartstart/updateCodeItem", method=RequestMethod.POST)
	public @ResponseBody String updateCodeItem(@RequestParam(value="code") String code, 
			@RequestParam(value="item_code") String item_code, 
			@RequestParam(value="newItem_code") String newItem_code,
			@RequestParam(value="item_nm") String item_nm,
			@RequestParam(value="newItem_nm") String newItem_nm,
			@RequestParam(value="selitemgenre") String selitemgenre,
			@RequestParam(value="selitemtype") String selitemtype,
			@RequestParam(value="ordered") String ordered, 
			@RequestParam(value="newOrdered") String newOrdered,
			@RequestParam(value="use_yn") String use_yn) {
		
		HTMLCleaner cleaner = new HTMLCleaner();
		code 	= cleaner.clean(code);
		item_code 	= cleaner.clean(item_code);
		newItem_code 	= cleaner.clean(newItem_code);
		item_nm 	= cleaner.clean(item_nm);
		newItem_nm 	= cleaner.clean(newItem_nm);
		selitemgenre 	= cleaner.clean(selitemgenre);
		selitemtype 	= cleaner.clean(selitemtype);
		ordered 	= cleaner.clean(ordered);
		newOrdered 	= cleaner.clean(newOrdered);
		use_yn 	= cleaner.clean(use_yn);
		
		logger.debug("code : " + code);
		logger.debug("item_code : " + item_code);
		logger.debug("newItem_code : " + newItem_code);
		logger.debug("item_nm : " + item_nm);
		logger.debug("newItem_nm : " + newItem_nm);
		logger.debug("selitemgenre : " + selitemgenre);
		logger.debug("selitemtype : " + selitemtype);
		logger.debug("ordered : " + ordered);
		logger.debug("newOrdered : " + newOrdered);
		logger.debug("use_yn : " + use_yn);
		
		String update_id = "update_id";			// 수정자 ID(차후에 로그인 한 사람의 로그인 ID로 바꿀것
		String resultcode = "";
		String resultmessage = "";
		try{
			validateUpdateCodeItem(code,item_code,newItem_code,item_nm,newItem_nm,use_yn,ordered,newOrdered);	// 입력받은 파라미터 값들에 대한 validation 작업 진행
			
			if((item_code.equals(newItem_code)) && (item_nm.equals(newItem_nm))){				// 기존의 코드값, 코드명과 새로이 수정하고자 하는 코드값, 코드명이 같은 경우는 사용여부에 대한 수정만을 의미하므로 바로 수정하는 함수를 실행하도록 한다
				service.updateCodeItem(code, item_code, newItem_code, newItem_nm, selitemgenre, selitemtype, use_yn, update_id ,ordered,newOrdered);
				resultcode = SmartUXProperties.getProperty("flag.success");
				resultmessage = SmartUXProperties.getProperty("message.success");
			}else{
				if(!(item_code.equals(newItem_code))){			// 기존의 코드값과 새로이 입력한 코드값이 다르면 새로이 입력한 코드값이 기존에 존재하는지를 확인한다
					int count = service.getCodeItemcodeCnt(code, newItem_code);
					
					if(count == 0){								// 새로이 입력한 코드값이 존재하지 않으면
						service.updateCodeItem(code, item_code, newItem_code, newItem_nm, selitemgenre, selitemtype, use_yn, update_id ,ordered,newOrdered);
						resultcode = SmartUXProperties.getProperty("flag.success");
						resultmessage = SmartUXProperties.getProperty("message.success");
						
					}else{										// 새로이 입력한 코드값이 존재하면
						SmartUXException e = new SmartUXException();
						//e.setFlag(SmartUXProperties.getProperty("flag.key1"));
						//e.setMessage("item_code");
						e.setFlag("EXISTS ITEM_CODE");
						e.setMessage("item_code");
						throw e;
					}
					
				}else if(!(item_nm.equals(newItem_nm))){		// 기존의 코드명과 새로이 입력한 코드명이 다르면 새로이 입력한 코드명이 기존에 존재하는지를 확인한다
					int count = service.getCodeItemnmCnt(code, newItem_nm);
					
					if(count == 0){								// 새로이 입력한 코드명이 존재하지 않으면
						service.updateCodeItem(code, item_code, newItem_code, newItem_nm, selitemgenre, selitemtype, use_yn, update_id ,ordered,newOrdered);
						resultcode = SmartUXProperties.getProperty("flag.success");
						resultmessage = SmartUXProperties.getProperty("message.success");
					}else{										// 새로이 입력한 코드명이 존재하면
						SmartUXException e = new SmartUXException();
						//e.setFlag(SmartUXProperties.getProperty("flag.key1"));
						//e.setMessage("item_nm");
						e.setFlag("EXISTS ITEM_NM");
						e.setMessage("item_nm");
						throw e;
					}
				}
			}
		}catch(Exception e){
			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}
		
		
		return "{\"flag\" : \"" + resultcode + "\", \"message\" : \"" + resultmessage + "\"}";
	}
	
	/**
	 * UpdateCodeItem 함수에 대한 validation 작업 함수
	 * @throws SmartUXException
	 */
	private void validateUpdateCodeItem(String code,String item_code,String newItem_code,String item_nm,String newItem_nm, String use_yn, String ordered, String newOrdered) throws SmartUXException{
		if(!(StringUtils.hasText(code))){
			SmartUXException exception = new SmartUXException();
			//exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			//exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "코드"));
			exception.setFlag("NOT FOUND CODE");
			exception.setMessage("코드값은 필수로 들어가야 합니다");
			throw exception;
		}
		if(!(StringUtils.hasText(item_code))){
			SmartUXException exception = new SmartUXException();
			//exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			//exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "아이템코드"));
			exception.setFlag("NOT FOUND ITEM_CODE");
			exception.setMessage("아이템 코드값은 필수로 들어가야 합니다");
			throw exception;
		}
		if(!(StringUtils.hasText(newItem_code))){
			SmartUXException exception = new SmartUXException();
			//exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			//exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "NEW아이템코드"));
			exception.setFlag("NOT FOUND NEW_ITEM_CODE");
			exception.setMessage("NEW아이템 코드값은 필수로 들어가야 합니다");
			throw exception;
		}
		if(!(StringUtils.hasText(item_nm))){
			SmartUXException exception = new SmartUXException();
			//exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			//exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "아이템이름"));
			exception.setFlag("NOT FOUND ITEM_NM");
			exception.setMessage("아이템이름은 필수로 들어가야 합니다");
			throw exception;
		}
		if(!(StringUtils.hasText(newItem_nm))){
			SmartUXException exception = new SmartUXException();
			//exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			//exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "NEW아이템이름"));
			exception.setFlag("NOT FOUND NEW_ITEM_NM");
			exception.setMessage("NEW아이템 이름은 필수로 들어가야 합니다");
			throw exception;
		}
		if(!(StringUtils.hasText(ordered))){
			SmartUXException exception = new SmartUXException();
			//exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			//exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "항목표시순서"));
			exception.setFlag("NOT FOUND ORDERED");
			exception.setMessage("항목표시순서는 필수로 들어가야 합니다");
			throw exception;
		}
		if(!(StringUtils.hasText(newOrdered))){
			SmartUXException exception = new SmartUXException();
			//exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			//exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "NEW항목표시순서"));
			exception.setFlag("NOT FOUND NEW_ORDERED");
			exception.setMessage("NEW 항목표시순서는 필수로 들어가야 합니다");
			throw exception;
		}
		if(!(StringUtils.hasText(use_yn))){
			SmartUXException exception = new SmartUXException();
			//exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			//exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "사용여부"));
			exception.setFlag("NOT FOUND USEYN");
			exception.setMessage("사용여부는 필수로 들어가야 합니다");
			throw exception;
		}
		if(newItem_code.length() > 100){
			SmartUXException exception = new SmartUXException();
			exception.setFlag("ITEM_CODE LENGTH");
			exception.setMessage("아이템코드 값은 100자 이내이어야 합니다");
			throw exception;
		}
		if(newItem_nm.length() > 100){
			SmartUXException exception = new SmartUXException();
			exception.setFlag("ITEM_NM LENGTH");
			exception.setMessage("아이템이름 값은 100자 이내이어야 합니다");
			throw exception;
		}
	}
	
	/**
	 * SmartStart Item 코드값 등록 화면
	 * @param code			SmartStart Item을 등록하고자 하는 code
	 * @return				SmartStart Item 코드값 등록 화면 URL
	 */
	@RequestMapping(value="/admin/smartstart/insertItem", method=RequestMethod.GET)
	public String insertItem(
			//@RequestParam(value="code") String code,
			Model model)  throws Exception{
		//List<CodeItemVO> result = service.getItemTypeList();
		
		List<ItemListVO> itemResult= new ArrayList<ItemListVO>();
		
		// itemtype.properties 파일에서 항목 타입들을 읽어온다
		int propertylength = Integer.parseInt(SmartUXProperties.getProperty("itemtype.length"));

		for(int i=1; i <= propertylength; i++){		
			ItemListVO itemlist = new ItemListVO();
			
			itemlist.setItemtype(SmartUXProperties.getProperty("itemtype.code" + i));

			itemResult.add(itemlist);
		}	
		
		model.addAttribute("itemResult", itemResult);

		//프로퍼티 파일 경로와 프로퍼티 키를 입력받아 해당 프로퍼티 파일 안에 있는 키에 해당하는 값을 읽어온다
		String code = SmartUXProperties.getProperty("code.code3");
		logger.debug("code : " + code );
		
		model.addAttribute("code", code);
		//model.addAttribute("result", result);
		
		return "/admin/smartstart/insertItem";
	}
	
	/**
	 * SmartStart Item 코드값 등록 처리 작업
	 * @param code				SmartStart Item을 등록하고자 하는 code
	 * @param item_code			SmartStart Item의 item_code
	 * @param item_nm			SmartStart Item의 item_nm
	 * @param use_yn			SmartStart Item의 사용여부(Y/N)	
	 * @return
	 */
	@RequestMapping(value="/admin/smartstart/insertItem", method=RequestMethod.POST)
	public @ResponseBody String insertItem(
			@RequestParam(value="code") String code, 
			@RequestParam(value="item_code") String item_code, 
			@RequestParam(value="item_nm") String item_nm,
			@RequestParam(value="selitemgenre") String selitemgenre,
			@RequestParam(value="selitemtype") String selitemtype,
			@RequestParam(value="use_yn") String use_yn) {
		
		
		HTMLCleaner cleaner = new HTMLCleaner();
		code 	= cleaner.clean(code);
		item_code 	= cleaner.clean(item_code);
		item_nm 	= cleaner.clean(item_nm);
		selitemgenre 	= cleaner.clean(selitemgenre);
		selitemtype 	= cleaner.clean(selitemtype);
		use_yn 	= cleaner.clean(use_yn);
		
		logger.debug("code : " + code );
		logger.debug("selitemgenre : " + selitemgenre);
		logger.debug("selitemtype : " + selitemtype);
		
		String create_id = "create_id";			// 등록자 ID(차후에 로그인 한 사람의 로그인 ID로 바꿀것
		String resultcode = "";
		String resultmessage = "";
		try{
			validateInsertItem(code,item_code,item_nm,use_yn);		// 입력받은 파라미터 값들에 대한 validation 작업 진행
			int count = service.getCodeItemcodeCnt(code, item_code);
			
			if(count == 0){
				count = service.getCodeItemnmCnt(code, item_nm);
				if(count == 0){
					service.insertItem(code, item_code, item_nm, selitemgenre, selitemtype, use_yn, create_id);
					resultcode = SmartUXProperties.getProperty("flag.success");
					resultmessage = SmartUXProperties.getProperty("message.success");
				}else{
					SmartUXException e = new SmartUXException();
					//e.setFlag(SmartUXProperties.getProperty("flag.key1"));
					//e.setMessage("item_nm");
					e.setFlag("EXISTS ITEM_NM");
					e.setMessage("동일 아이템이름 존재");
					throw e;
				}
			}else{
				SmartUXException e = new SmartUXException();
				e.setFlag(SmartUXProperties.getProperty("flag.key1"));
				e.setMessage("item_code");
				throw e;
			}
			
		}catch(Exception e){
			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}
				
		return "{\"flag\" : \"" + resultcode + "\", \"message\" : \"" + resultmessage + "\"}";
	}
	
	/**
	 * InsertItem 함수에 대한 validation 작업 함수
	 * @throws SmartUXException
	 */
	private void validateInsertItem(String code,String item_code,String item_nm, String use_yn) throws SmartUXException{
		if(!(StringUtils.hasText(code))){
			SmartUXException exception = new SmartUXException();
			//exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			//exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "코드"));
			exception.setFlag("NOT FOUND CODE");
			exception.setMessage("코드 값은 필수로 들어가야 합니다");
			throw exception;
		}
		if(!(StringUtils.hasText(item_code))){
			SmartUXException exception = new SmartUXException();
			//exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			//exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "아이템코드"));
			exception.setFlag("NOT FOUND ITEM_CODE");
			exception.setMessage("아이템코드 값은 필수로 들어가야 합니다");
			throw exception;
		}
		if(!(StringUtils.hasText(item_nm))){
			SmartUXException exception = new SmartUXException();
			//exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			//exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "아이템이름"));
			exception.setFlag("NOT FOUND ITEM_NM");
			exception.setMessage("아이템이름은 필수로 들어가야 합니다");
			throw exception;
		}
		if(!(StringUtils.hasText(use_yn))){
			SmartUXException exception = new SmartUXException();
			//exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			//exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "사용여부"));
			exception.setFlag("NOT FOUND USE_YN");
			exception.setMessage("사용여부 값은 필수로 들어가야 합니다");
			throw exception;
		}
		if(item_code.length() > 100){
			SmartUXException exception = new SmartUXException();
			exception.setFlag("ITEM_CODE LENGTH");
			exception.setMessage("아이템코드 값은 100자 이내이어야 합니다");
			throw exception;
		}
		if(item_nm.length() > 100){
			SmartUXException exception = new SmartUXException();
			exception.setFlag("ITEM_NM LENGTH");
			exception.setMessage("아이템이름 값은 100자 이내이어야 합니다");
			throw exception;
		}
	}
	
	/**
	 * SmartStart Item 코드값 삭제 처리 작업
	 * @param code			SmartStart Item을 삭제하고자 하는 code	
	 * @param item_codes	SmartStart Item의 삭제하고자 하는 item_code들이 ,로 연결된 item_code값들 문자열
	 * @return
	 */
	@RequestMapping(value="/admin/smartstart/deleteItem", method=RequestMethod.POST)
	public @ResponseBody String deleteItem(
			//@RequestParam(value="code") String code, 
			@RequestParam(value="item_code[]") String [] item_codes) {
		//프로퍼티 파일 경로와 프로퍼티 키를 입력받아 해당 프로퍼티 파일 안에 있는 키에 해당하는 값을 읽어온다
		String code = SmartUXProperties.getProperty("code.code3");
		logger.debug("code : " + code );
		for(int i = 0 ; i < item_codes.length ; i++ ){
			logger.debug("item_codes : " + item_codes[i] );
		}
		
		String resultcode = "";
		String resultmessage = "";
		try{
			validateDeleteItem(code, item_codes);			// 입력받은 파라미터 값들에 대한 validation 작업 진행
			service.deleteItem(code, item_codes);
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
	 * DeleteItem 함수에 대한 validation 작업 함수
	 * @param item_codes					SmartUX 코드에서 삭제할 아이템코드들의 배열
	 * @throws SmartUXException
	 */
	private void validateDeleteItem(String code, String [] item_codes) {
		SmartUXException exception = new SmartUXException();
		
		if(!(StringUtils.hasText(code))){
			//exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			//exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "코드"));
			exception.setFlag("NOT FOUND CODE");
			exception.setMessage("코드값은 필수로 들어가야 합니다");
			throw exception;
		}
		
		if((item_codes == null) ||(item_codes.length == 0)){
			//exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			//exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "삭제 아이템코드 값 배열"));
			exception.setFlag("NOT FOUND ITEM_CODE");
			exception.setMessage("삭제 아이템코드 값 배열은 필수로 들어가야 합니다");
			throw exception;
		}
	}
	
	/**
	 * SmartStart Item 순서바꾸기 화면
	 * @param code		SmartStart Item을 조회하고자 하는 code
	 * @param model		Model 객체
	 * @return			SmartStart Item 코드값 목록 조회 화면 URL
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/smartstart/changeItemOrder", method=RequestMethod.GET)
	public String changeItemOrder(
			//@RequestParam(value="code", defaultValue="") String code, 
			Model model) throws Exception{
		List<CodeItemVO> result = null;
		
		//프로퍼티 파일 경로와 프로퍼티 키를 입력받아 해당 프로퍼티 파일 안에 있는 키에 해당하는 값을 읽어온다
		String code = SmartUXProperties.getProperty("code.code3");
		logger.debug("code : " + code );
		
		validateChangeItemOrder(code);
		result = service.getCodeItemList(code);
		model.addAttribute("code", code);
		model.addAttribute("result", result);
		
		return "/admin/smartstart/changeItemOrder";		
	}	
	
	/**
	 * changeCodeItemOrder 함수에 대한 validation 작업 함수
	 * @param code			순서를 바꾸고자 하는 SmartUX Item의 code 
	 */	
	private void validateChangeItemOrder(String code){		
		SmartUXException exception = new SmartUXException();		
		if(!(StringUtils.hasText(code))){
			//exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			//exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "코드"));
			exception.setFlag("NOT FOUND CODE");
			exception.setMessage("코드값은 필수로 들어가야 합니다");
			throw exception;
		}
	}
	
	/**
	 * SmartStart Item 순서바꾸기 작업
	 * @param code				순서를 바꾸고자 하는 SmartUX Item의 code 
	 * @param item_codes		순서를 바꾸고자 하는 SmartUX Item의 item_code들의 문자열 배열
	 * @return					작업 결과 json 문자열
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/smartstart/changeCodeItemOrder", method=RequestMethod.POST)
	public @ResponseBody String changeCodeItemOrder(@RequestParam(value="code") String code, @RequestParam(value="item_code[]") String [] item_codes) throws Exception{
		
		HTMLCleaner cleaner = new HTMLCleaner();
		code 	= cleaner.clean(code);
		
		String update_id = "update_id";			// 수정자 ID(차후에 로그인 한 사람의 로그인 ID로 바꿀것
		
		String resultcode = "";
		String resultmessage = "";
		try{
			validateChangeCodeItemOrder(code, item_codes);						// 입력받은 파라미터 값들에 대한 validation 작업 진행
			service.changeCodeItemOrder(code, item_codes, update_id);
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
	 * changeCodeItemOrder 함수에 대한 validation 작업 함수
	 * @param code			순서를 바꾸고자 하는 SmartUX Item의 code 
	 * @param item_codes	순서를 바꾸고자 하는 SmartUX Item의 item_code들의 문자열 배열
	 */	
	private void validateChangeCodeItemOrder(String code, String [] item_codes){		
		SmartUXException exception = new SmartUXException();
		
		if(!(StringUtils.hasText(code))){
			//exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			//exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "코드"));
			exception.setFlag("NOT FOUND CODE");
			exception.setMessage("코드값은 필수로 들어가야 합니다");
			throw exception;
		}		
		if((item_codes == null) ||(item_codes.length == 0)){
			//exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			//exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "삭제할 코드값들"));
			exception.setFlag("NOT FOUND ITEM_CODE");
			exception.setMessage("아이템코드 값 배열은 필수로 들어가야 합니다");
			throw exception;
		}		
	}
}
