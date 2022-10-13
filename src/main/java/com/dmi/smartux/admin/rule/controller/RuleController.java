package com.dmi.smartux.admin.rule.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dmi.smartux.admin.rule.service.RuleService;
import com.dmi.smartux.admin.rule.vo.RuleDetailVO;
import com.dmi.smartux.admin.rule.vo.RuleTypeVO;
import com.dmi.smartux.admin.rule.vo.RuleVO;
import com.dmi.smartux.common.exception.ExceptionHandler;
import com.dmi.smartux.common.exception.SmartUXException;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.util.HTMLCleaner;

@Controller
public class RuleController {

	@Autowired
	RuleService service;
	
	/**
	 * Rule Type 값을 입력받아 Rule Type의 명칭을 얻어오는 함수
	 * @param rule_type		Rule Type 값
	 * @return				Rule Type 이름
	 */
	private String getRuleTypeName(String rule_type){
		String result = "";
		int length = Integer.parseInt(SmartUXProperties.getProperty("vodranking.rule.length"));
		for(int i=1; i <= length; i++){
			String key = "vodranking.rule.code" + i;
			if(rule_type.equals(SmartUXProperties.getProperty(key))){
				key = "vodranking.rule.msg" + i;
				result = SmartUXProperties.getProperty(key);
				break;
			}
		}
		
		return result;
	}
	
	/**
	 * VOD 랭킹 룰 목록 조회 화면
	 * @param model			Model 객체
	 * @return				VOD 랭킹 룰 목록 조회 화면 URL
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/rule/getRuleList")
	public String getRuleList(Model model) throws Exception{
		List<RuleVO> result = service.getRuleList();
		if(result != null){
			for(RuleVO item : result){
				String rule_type = item.getRule_type();
				item.setRule_type(getRuleTypeName(rule_type));
			}
		}
		
		model.addAttribute("result", result);
		return "/admin/rule/getRuleList";
	}
	
	/**
	 * VOD 랭킹 룰 상세조회 화면
	 * @param rule_code		조회하고자 하는 VOD 랭킹 룰 코드
	 * @param model			Model 객체
	 * @return				VOD 랭킹 룰 목록 조회 화면 URL
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/rule/viewRule")
	public String viewRule(@RequestParam(value="rule_code") String rule_code, Model model) throws Exception{
		
		HTMLCleaner cleaner = new HTMLCleaner();
		rule_code 	= cleaner.clean(rule_code);
		
		RuleVO main = service.viewRule(rule_code);
		List<RuleDetailVO> detail = service.getRuleDetailList(rule_code);
		
		RuleTypeVO objVO = new RuleTypeVO();
		objVO.setRule_type(main.getRule_type());
		objVO.setRule_type_name(getRuleTypeName(main.getRule_type()));
		
		model.addAttribute("main", main);			// 메인 정보
		model.addAttribute("detail", detail);		// 상세 정보
		model.addAttribute("rule_type", objVO);		// Rule Type 정보
		model.addAttribute("rule_code", rule_code);	// Rule code
		
		return "/admin/rule/viewRule";
		
	}
	
	/**
	 * VOD 랭킹 룰 등록 화면
	 * @param model		Model 객체
	 * @return			VOD 랭킹 룰 등록 화면 URL
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/rule/insertRule", method=RequestMethod.GET)
	public String insertRule(Model model) throws Exception{
		List<RuleTypeVO> rule_type_list = new ArrayList<RuleTypeVO>();
		
		int length = Integer.parseInt(SmartUXProperties.getProperty("vodranking.rule.length"));
		for(int i=1; i <= length; i++){
			String rule_type_key = "vodranking.rule.code" + i;
			String rule_name_key = "vodranking.rule.msg" + i;
			
			RuleTypeVO objVO = new RuleTypeVO();
			objVO.setRule_type(SmartUXProperties.getProperty(rule_type_key));
			objVO.setRule_type_name(SmartUXProperties.getProperty(rule_name_key));
			
			rule_type_list.add(objVO);
		}
		
		model.addAttribute("rule_type_list", rule_type_list);	
		
		return "/admin/rule/insertRule";
	}
	
	/**
	 * VOD 랭킹 룰 등록 처리 작업
	 * @param rule_name		랭킹 룰 이름
	 * @param rule_type		랭킹 룰 타입
	 * @param dweights		일자별 랭킹 룰에서 가중치 값 배열
	 * @param pstart		가격별 랭킹 룰에서 시작 가격 값 배열
	 * @param pend			가격별 랭킹 룰에서 끝 가격 값 배열
	 * @param pweights		가격별 랭킹 룰에서 가중치 값 배열
	 * @param cweight		유/무료 랭킹 룰에서 유료 가중치
	 * @param fweight		유/무료 랭킹 룰에서 무료 가중치
	 * @param hgenre		장르별 랭킹 룰에서 장르 코드 값 배열
	 * @param gweights		장르별 랭킹 룰에서 가중치 값 배열
	 * @param login_id		관리자 사이트에 로그인 한 사람의 로그인 id
	 * @return				처리 결과를 기록한 json 문자열
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/rule/insertRule", method=RequestMethod.POST)
	public @ResponseBody String insertRule(@RequestParam(value="rule_name") String rule_name,
			@RequestParam(value="rule_type") String rule_type,
			@RequestParam(value="dweights[]", required=false) String [] dweights,
			@RequestParam(value="pstart[]", required=false) String [] pstart,
			@RequestParam(value="pend[]", required=false) String [] pend,
			@RequestParam(value="pweights[]", required=false) String [] pweights,
			@RequestParam(value="cweight", required=false) String cweight,
			@RequestParam(value="fweight", required=false) String fweight,
			@RequestParam(value="hgenre[]", required=false) String [] hgenre,
			@RequestParam(value="gweights[]", required=false) String [] gweights,
			@RequestParam(value="smartUXManager") String login_id) throws Exception{
		
		String resultcode = "";
		String resultmessage = "";
		
		try{
			validateInsertRule(rule_name, rule_type, dweights, pstart, pend, pweights, cweight, fweight, hgenre, gweights);
			
			if(rule_type.equals(SmartUXProperties.getProperty("vodranking.rule.code1"))){			// 일자별 랭킹
				service.insertDayRule(rule_name, rule_type, dweights, login_id);
			}else if(rule_type.equals(SmartUXProperties.getProperty("vodranking.rule.code2"))){		// 가격별 랭킹
				service.insertPriceRule(rule_name, rule_type, pstart, pend, pweights, login_id);
			}else if(rule_type.equals(SmartUXProperties.getProperty("vodranking.rule.code3"))){		// 유/무료별 랭킹
				service.insertFreeRule(rule_name, rule_type, cweight, fweight, login_id);
			}else if(rule_type.equals(SmartUXProperties.getProperty("vodranking.rule.code4"))){		// 장르별 랭킹
				service.insertGenreRule(rule_name, rule_type, hgenre, gweights, login_id);
			}else if(rule_type.equals(SmartUXProperties.getProperty("vodranking.rule.code5"))){		// 시리즈별 랭킹
				service.insertSeriesRule(rule_name, rule_type, login_id);
			}
			
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
	 * insertRule에 대한 validation 함수
	 * @param rule_name		랭킹 룰 이름
	 * @param rule_type		랭킹 룰 타입
	 * @param dweights		일자별 랭킹 룰에서 가중치 값 배열
	 * @param pstart		가격별 랭킹 룰에서 시작 가격 값 배열
	 * @param pend			가격별 랭킹 룰에서 끝 가격 값 배열
	 * @param pweights		가격별 랭킹 룰에서 가중치 값 배열
	 * @param cweight		유/무료 랭킹 룰에서 유료 가중치
	 * @param fweight		유/무료 랭킹 룰에서 무료 가중치
	 * @param hgenre		장르별 랭킹 룰에서 장르 코드 값 배열
	 * @param gweights		장르별 랭킹 룰에서 가중치 값 배열
	 * @throws SmartUXException
	 */
	private void validateInsertRule(String rule_name,
			String rule_type,
			String [] dweights,
			String [] pstart,
			String [] pend,
			String [] pweights,
			String cweight,
			String fweight,
			String [] hgenre,
			String [] gweights) throws SmartUXException{
		SmartUXException exception = new SmartUXException();
		if(!(StringUtils.hasText(rule_name))){
			
			exception.setFlag("NOT FOUND RULE_NM");
			exception.setMessage("RULE 이름은 필수로 들어가야 합니다");
			throw exception;
		}
		
		if(rule_name.length() > 100){
			exception.setFlag("RULE_NM LENGTH");
			exception.setMessage("RULE 이름은 100자 이내이어야 합니다");
			throw exception;
		}
		
		if(rule_type.equals(SmartUXProperties.getProperty("vodranking.rule.code1"))){			// 일자별 랭킹
			if((dweights == null) || (dweights.length == 0)){
				exception.setFlag("NOT FOUND DWEIGHTS");
				exception.setMessage("일자별 랭킹의 가중치가 입력되지 않았습니다");
				throw exception;
			}
		}else if(rule_type.equals(SmartUXProperties.getProperty("vodranking.rule.code2"))){		// 가격별 랭킹
			if((pstart == null) || (pstart.length == 0)){
				exception.setFlag("NOT FOUND PSTART");
				exception.setMessage("가격별 랭킹의 시작가격이 입력되지 않았습니다");
				throw exception;
			}
			
			if((pend == null) || (pend.length == 0)){
				exception.setFlag("NOT FOUND PEND");
				exception.setMessage("가격별 랭킹의 끝가격이 입력되지 않았습니다");
				throw exception;
			}
			
			if((pweights == null) || (pweights.length == 0)){
				exception.setFlag("NOT FOUND PWEIGHTS");
				exception.setMessage("가격별 랭킹의 가중치가 입력되지 않았습니다");
				throw exception;
			}
		}else if(rule_type.equals(SmartUXProperties.getProperty("vodranking.rule.code3"))){		// 유/무료별 랭킹
			if("".equals(GlobalCom.isEmail(cweight))){
				exception.setFlag("NOT FOUND CWEIGHT");
				exception.setMessage("유/무료별 랭킹의 유료 가중치가 입력되지 않았습니다");
				throw exception;
			}
			
			if("".equals(GlobalCom.isEmail(fweight))){
				exception.setFlag("NOT FOUND FWEIGHT");
				exception.setMessage("유/무료별 랭킹의 무료 가중치가 입력되지 않았습니다");
				throw exception;
			}
		}else if(rule_type.equals(SmartUXProperties.getProperty("vodranking.rule.code4"))){		// 장르별 랭킹
			if((hgenre == null) || (hgenre.length == 0)){
				exception.setFlag("NOT FOUND HGENRE");
				exception.setMessage("장르별 랭킹의 장르코드가 입력되지 않았습니다");
				throw exception;
			}
			
			if((gweights == null) || (gweights.length == 0)){
				exception.setFlag("NOT FOUND GWEIGHTS");
				exception.setMessage("장르별 랭킹의 가중치가 입력되지 않았습니다");
				throw exception;
			}
		}else if(rule_type.equals(SmartUXProperties.getProperty("vodranking.rule.code5"))){		// 시리즈별 랭킹
			//별도의 입력값 없음
		}
	}
	
	/**
	 * VOD 랭킹 룰 수정 작업
	 * @param rule_code		수정하고자 하는 VOD 랭킹 룰의 rule_code
	 * @param rule_name		수정하고자 하는 VOD 랭킹 룰의 rule_name
	 * @param login_id		관리자 사이트에 로그인 한 사람의 로그인 id
	 * @return				처리 결과를 기록한 json 문자열
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/rule/updateRule", method=RequestMethod.POST)
	public @ResponseBody String updateRule(@RequestParam(value="rule_code") String rule_code
			, @RequestParam(value="rule_name") String rule_name
			, @RequestParam(value="smartUXManager") String login_id) throws Exception{
		
		HTMLCleaner cleaner = new HTMLCleaner();
		rule_code 	= cleaner.clean(rule_code);
		rule_name 	= cleaner.clean(rule_name);
		login_id 	= cleaner.clean(login_id);
		
		String resultcode = "";
		String resultmessage = "";
		
		try{
			validateUpdateRule(rule_code, rule_name);
			
			service.updateRule(rule_code, rule_name, login_id);
			
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
	 * updateRule 함수에 대한 validation 작업 함수
	 * @param rule_code		수정하고자 하는 VOD 랭킹 룰의 rule_code
	 * @param rule_name		수정하고자 하는 VOD 랭킹 룰의 rule_name
	 * @throws SmartUXException
	 */
	private void validateUpdateRule(String rule_code,	String rule_name) throws SmartUXException{
		SmartUXException exception = new SmartUXException();
		
		if(!(StringUtils.hasText(rule_code))){
			exception.setFlag("NOT FOUND RULE_CODE");
			exception.setMessage("RULE 코드값은 필수로 들어가야 합니다");
			throw exception;
		}
		
		if(!(StringUtils.hasText(rule_name))){
			exception.setFlag("NOT FOUND RULE_NAME");
			exception.setMessage("RULE 이름은 필수로 들어가야 합니다");
			throw exception;
		}
		
		if(rule_name.length() > 100){
			exception.setFlag("RULE_NAME LENGTH");
			exception.setMessage("RULE 이름은 100자 이내이어야 합니다");
			throw exception;
		}
	}
	
	/**
	 * VOD 랭킹 룰 삭제 작업
	 * @param rule_codes	삭제하고자 하는 VOD 랭킹 룰의 rule_code 배열
	 * @return				처리 결과를 기록한 json 문자열
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/rule/deleteRule", method=RequestMethod.POST)
	public @ResponseBody String deleteRule(@RequestParam(value="rule_code[]") String [] rule_codes) throws Exception{
		String resultcode = "";
		String resultmessage = "";
		
		try{
			validateDeleteRule(rule_codes);
			
			// 파라미터로 받은 삭제할 rule_code값이 사용되고 있는지를 판단한다
			Set<String> useRuleSet = new HashSet<String>();
			
			// 기존에 사용중인 Rule code값들을 조회해서 분리한뒤 Set 객체에 넣는다
			List<String> useRuleList = service.selectUseRule();
			for(String item : useRuleList){
				String [] tmps = item.split("\\|\\|");
				for(String subitem : tmps){
					useRuleSet.add(subitem);
				}
			}
			
			// Set 객체 안에 사용자가 삭제할려고 하는 코드 값들이 있는지 확인해서 있는 경우 쿼리에서 IN 조건에 사용할 문자열을 만든다
			String existcode = "";
			for(String delcode : rule_codes){
				if(useRuleSet.contains(delcode)){
					if("".equals(existcode)){
						existcode = delcode;
					}else{
						existcode += "," + delcode;
					}
				}
			}
			
			
			// 쿼리에서 IN 조건에 사용할 문자열이 ""라는 것은 삭제할 것중 현재 사용중인 것이 없음을 나타내는 것이므로 삭제 작업을 진행한다
			// "" 가 아닌 경우는 삭제하고자 하는 코드중에 현재 사용중인것이 있다는 의미이므로 삭제할 코드중 현재 사용중인 Rule Code에 대한 이름들을 얻어온다
			if("".equals(existcode)){
				service.deleteRule(rule_codes);
			
				resultcode = SmartUXProperties.getProperty("flag.success");
				resultmessage = SmartUXProperties.getProperty("message.success");
			}else{
				List<RuleVO> useResult = service.getRuleList2(existcode);
				String msgResult = "";
				for(RuleVO item : useResult){
					if("".equals(msgResult)){
						msgResult = item.getRule_name();
					}else{
						msgResult += ", " + item.getRule_name();
					}
				}
				resultcode = "USE RULE CODE";
				resultmessage = msgResult;
			}
			
		}catch(Exception e){
			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}
		
		return "{\"flag\" : \"" + resultcode + "\", \"message\" : \"" + resultmessage + "\"}";
	}
	
	/**
	 * deleteRule 함수에 대한 validation 작업 함수
	 * @param rule_codes		삭제하고자 하는 VOD 랭킹 룰의 rule_code 배열
	 * @throws SmartUXException
	 */
	private void validateDeleteRule(String [] rule_codes) throws SmartUXException{
		SmartUXException exception = new SmartUXException();
		
		if((rule_codes == null) || (rule_codes.length == 0)){
			
			exception.setFlag("NOT FOUND RULE_CODE");
			exception.setMessage("코드값은 필수로 들어가야 합니다");
			throw exception;
		}
		
	}
}
