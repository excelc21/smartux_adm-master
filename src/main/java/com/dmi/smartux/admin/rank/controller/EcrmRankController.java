package com.dmi.smartux.admin.rank.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dmi.commons.consts.ResultCode;
import com.dmi.commons.exception.MimsCommonException;
import com.dmi.commons.factory.FileProcessFunction;
import com.dmi.commons.factory.FileProcessLockFactory;
import com.dmi.commons.logger.CLog;
import com.dmi.commons.model.RestfulCodeMsg;
import com.dmi.commons.utility.HttpClientUtils;
import com.dmi.commons.utility.RestfulUtils;
import com.dmi.smartux.admin.rank.service.EcrmRankService;
import com.dmi.smartux.admin.rank.vo.EcrmRankVO;
import com.dmi.smartux.admin.rule.service.RuleService;
import com.dmi.smartux.admin.rule.vo.RuleVO;
import com.dmi.smartux.admin.schedule.vo.ScheduleDetailVO;
import com.dmi.smartux.common.exception.ExceptionHandler;
import com.dmi.smartux.common.exception.SmartUXException;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.CookieUtil;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.util.HTMLCleaner;
import com.dmi.smartux.common.vo.CUDResult;
import com.dmi.smartux.smartstart.service.GenreVodBestListService;
import com.dmi.smartux.smartstart.vo.GenreVodBestListVO;

import net.sf.json.JSONSerializer;

@Controller
public class EcrmRankController {
	
	private final Log logger = LogFactory.getLog(this.getClass());
	
	private final Log applyLogger = LogFactory.getLog("adminApplyCacheHistroy");
	
	@Autowired
	RuleService serviceRule;
	
	@Autowired
	EcrmRankService service;
		
	@Autowired
	GenreVodBestListService serviceVod;
	
	/**
	 * GenreVodBest 즉시적용
	 * SmartUX Rank 데이터 목록화면에서 즉시적용 버튼을 클릭하여 캐쉬를 바로 적용할때 이용된다
	 * @param rank_code		랭킹코드
	 * @return 결과코드 , 결과메시지
	 */
	@RequestMapping(value="/admin/rank/activateCache", method=RequestMethod.POST)
	public ResponseEntity<String> activateCache(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value="callByScheduler",  defaultValue="Y") final  String callByScheduler,
			@RequestParam(value="rank_code") String rank_code) throws Exception{
		
		
		final CLog cLog = new CLog(applyLogger, request.getRemoteAddr(), request.getRequestURI(), request.getMethod());
		final String loginUser = callByScheduler.equals("Y") ? "batch" : CookieUtil.getCookieUserID(request);
		final CUDResult result = new CUDResult();
		
		try {
			cLog.startLog("GenreVodBest 즉시적용",  loginUser);
			
			HTMLCleaner cleaner = new HTMLCleaner();
			rank_code 	= cleaner.clean(rank_code);
			
			final String host = SmartUXProperties.getProperty("cache.sync.public.host");
			final String port = SmartUXProperties.getProperty("cache.sync.public.port");
			
			String fileLockPath = SmartUXProperties.getProperty("GenreVodBestListDao.refreshGenreVodBestList.fileLock");
			int waitTime = NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.wait"), 5);
			
			FileProcessLockFactory.start(fileLockPath, waitTime,  new FileProcessFunction("GenreVodBest 즉시적용", ResultCode.ApplyRequestFail) {
				
				@Override
				public void run() throws Exception {
					
					//즉시적용 호출 시 sleep 값 추가 -> DB분리건으로 OGG가 10초 이상 걸리는 경우가 있어 즉시적용 시 반영이 안돼서 추가함
					Thread.sleep(Integer.parseInt(org.apache.commons.lang.StringUtils.defaultIfEmpty(SmartUXProperties.getProperty("apply.cache.call.sleep"), "10000")));
					
					RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
							host, 
							port, 
							SmartUXProperties.getProperty("GenreVodBestListDao.refreshGenreVodBestList.url")+"?callByScheduler="+callByScheduler,
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.method"), "POST"), 
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"), 
							MediaType.APPLICATION_JSON.toString(),
							NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.timeout"), 60000)).getResponseBody());
					
					result.setFlag(codeMsg.getCode());
					result.setMessage(codeMsg.getMessage());
					cLog.middleLog("GenreVodBest 즉시적용", "API 호출", codeMsg.getCode(), codeMsg.getMessage());
				}
			});
		} catch (MimsCommonException e) {
			cLog.warnLog("GenreVodBest 즉시적용", "실패", e.getFlag(), e.getMessage(), e.getLocalizedMessage());
			result.setFlag(e.getFlag());
			result.setMessage(e.getMessage());
		} finally {
			cLog.endLog("GenreVodBest 즉시적용", loginUser, result.getFlag(), result.getMessage());
		}
		
		String resultMsg = "{\"flag\" : \"" + result.getFlag() + "\", \"message\" : \"" + result.getMessage() + "\"}";
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(resultMsg).toString(), responseHeaders, HttpStatus.OK);
		
	}
	
	/**
	 * Rank 데이터 항목정보 목록 조회 화면
	 * @param model		Model 객체
	 * @return			Rank 항목정보 목록 조회 화면 URL
	 */
	@RequestMapping(value="/admin/rank/getRankList")
	public String getRankList(Model model) throws Exception{
		List<EcrmRankVO> result = service.getRankList("", "");
		if(result != null){
			for(EcrmRankVO item : result){
				String tempg = item.getGenre_code().replaceAll("\\|\\|", " + ");
				item.setGenre_code(tempg);
				
				String[] split_rulecd = item.getRule_code().split("\\|\\|");
				String rule_type = "";
			
				for(int i = 0 ; i < split_rulecd.length ; i++) {
					// 룰코드를 받아서 룰명칭으로 다시 셋팅
					RuleVO ruleresult = service.viewRule(split_rulecd[i]);
					
					if(i==0){
						rule_type = ruleresult.getRule_name();						
					}else{
						rule_type = rule_type +" + "+ruleresult.getRule_name();						
					}					
				}
				item.setRule_code(rule_type);
			}
		}
		
		model.addAttribute("result", result);
		return "/admin/rank/getRankList";
	}
		
	/**
	 * Rule Type 값을 입력받아 Rule Type의 명칭을 얻어오는 함수
	 * @param rule_type		RULE 타입
	 * @return
	 */
	private String getRuleTypeName(String rule_type){
		String result = "";
		int length = Integer.parseInt(SmartUXProperties.getProperty("vodranking.rule.length"));
		for(int i=1; i <= length + 1; i++){
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
	 * Rule 목록을 조회하는 팝업 함수
	 * @param rule_type		RULE 타입
	 * @return Rank Rule 목록을 조회하는 화면 URL
	 */
	@RequestMapping(value="/admin/rank/selectRulePopup")
	public String getRuleList(Model model) throws Exception{
		List<RuleVO> result = serviceRule.getRuleList();
		if(result != null){
			for(RuleVO item : result){
				String rule_type = item.getRule_type();
				item.setRule_type(getRuleTypeName(rule_type));
			}
		}		
		model.addAttribute("result", result);
		return "/admin/rank/selectRulePopup";
	}
	
	
	
	/**
	 * 장르코드 선택 팝업 화면
	 * @param genreElementid		표시위한 장르코드 아이디
	 * @param genreHiddenid			실제값 장르코드 아이디
	 * @return				장르코드 선택 팝업 화면 화면 URL
	 */
	@RequestMapping(value="/admin/rank/selectGenrePopup", method=RequestMethod.GET)
	public String selectGenrePopup(
			@RequestParam(value="genreElementid" , required=false, defaultValue="") String genreElementid,
			@RequestParam(value="genreHiddenid" , required=false, defaultValue="") String genreHiddenid,			
			Model model)  throws Exception{
		
		HTMLCleaner cleaner = new HTMLCleaner();
		genreElementid 	= cleaner.clean(genreElementid);
		genreHiddenid 	= cleaner.clean(genreHiddenid);
		
		// 장르 대분류
		List<EcrmRankVO> result_large = service.getGenreLargeList();
		model.addAttribute("result_large", 	result_large);
		
		// 장르 중분류
		List<EcrmRankVO> result_mid = service.getGenreMidList();
		model.addAttribute("result_mid", 	result_mid);
		
		// 장르 소분류
		List<EcrmRankVO> result_small = service.getGenreSmallList();
		model.addAttribute("result_small", 	result_small);
		
		model.addAttribute("genreElementid", genreElementid);
		model.addAttribute("genreHiddenid", genreHiddenid);
		return "/admin/rank/selectGenrePopup";
	}
	
	/**
	 * Rank 항목정보 목록 조회 화면
	 * @param model		Model 객체
	 * @param rank_code		랭킹코드 
	 * @return			Rank 항목정보 목록 조회 화면 URL
	 */
	@RequestMapping(value="/admin/rank/viewRank")
	public String viewRank(@RequestParam(value="rank_code") String rank_code, Model model) throws Exception{

		HTMLCleaner cleaner = new HTMLCleaner();
		rank_code 	= cleaner.clean(rank_code);
		
		EcrmRankVO result = service.viewRankList(rank_code);
		
		if(result != null){			
				// 장르코드셋팅
				String tempg = result.getGenre_code().replaceAll("\\|\\|", " + ");
				result.setGenre_code(tempg);
						
				// 룰코드 셋팅
				String[] split_rulecd = result.getRule_code().split("\\|\\|"); 
				
				String rule_type = "";
				for(int i = 0 ; i < split_rulecd.length ; i++) {
					// 룰코드를 받아서 룰명칭으로 다시 셋팅
					RuleVO ruleresult = service.viewRule(split_rulecd[i]);
					
					if(i==0){
						rule_type = ruleresult.getRule_name();						
					}else{
						rule_type = rule_type +" + "+ruleresult.getRule_name();						
					}					
				}
				result.setRule_code(rule_type);
		}		
				
		model.addAttribute("result", result);
		model.addAttribute("rank_code", rank_code);	// rank code
		return "/admin/rank/viewRank";
	}	

	/**
	 * Rank 항목정보 미리보기 조회 화면
	 * @param model		Model 객체
	 * @param rank_code		랭킹코드 
	 * @return			Rank 항목정보 미리보기 조회 화면 URL
	 */
	@RequestMapping(value="/admin/rank/previewVodPopup")
	public String previewVodPopup(@RequestParam(value="rank_code") String rank_code, Model model) throws Exception{
		HTMLCleaner cleaner = new HTMLCleaner();
		rank_code 	= cleaner.clean(rank_code);
		
		List<GenreVodBestListVO> result = service.previewVodPopup(rank_code);
		
		model.addAttribute("result", result);
		model.addAttribute("rank_code", rank_code);	// rank code
		return "/admin/rank/previewVodPopup";
	}	
	
	/**
	 * viewAlbumVod 정보 조회 화면
	 * @param rank_code		랭킹코드
	 * @param model		Model 객체
	 * @return			viewAlbumVod 정보 조회 화면 URL
	 */
	@RequestMapping(value="/admin/rank/viewAlbumVod")
	public String viewAlbumVod(@RequestParam(value="rank_code") String rank_code,
			@RequestParam(value="rule_type") String rule_type,
			Model model) throws Exception{
		
		HTMLCleaner cleaner = new HTMLCleaner();
		rank_code 	= cleaner.clean(rank_code);
		rule_type 	= cleaner.clean(rule_type);
		
		List<ScheduleDetailVO> detail = service.getAlbumDetailList(rank_code, rule_type);
		EcrmRankVO result = service.viewRankList(rank_code);
		
		model.addAttribute("detail", detail);		// 상세 정보
		model.addAttribute("result", result);
		model.addAttribute("rank_code", rank_code);
		model.addAttribute("rule_type", rule_type);
		return "/admin/rank/viewAlbumVod";
	}
	
	/**
	 * Rank Item 코드값 등록 화면
	 * @param model		Model 객체
	 * @return			Rank Item 코드값 등록 화면 URL
	 */
	@RequestMapping(value="/admin/rank/insertRank", method=RequestMethod.GET)
	public  String insertRank(
			Model model)  throws Exception{
	
		return "/admin/rank/insertRank";
	}

	/**
	 * Rank Item 코드값 등록처리 함수
	 * @param rank_term		랭킹기한
	 * @param rank_name		랭킹이름
	 * @param hgenre		장르코드
	 * @param rule_name		룰이름
	 * @param smartUXManager		로그인아이디
	 * @return			결과코드 , 결과메시지
	 */
	@RequestMapping(value="/admin/rank/insertRank", method=RequestMethod.POST)
	public @ResponseBody String insertRank(			
			@RequestParam(value="rank_term") String rank_term,
			@RequestParam(value="rank_name") String rank_name,
			@RequestParam(value="hgenre") String hgenre,
			@RequestParam(value="rule_name") String rule_name,
			@RequestParam(value="smartUXManager") String login_id,
			@RequestParam(value="category_gb") String category_gb
			) throws Exception{
		
		HTMLCleaner cleaner = new HTMLCleaner();
		rank_term 	= cleaner.clean(rank_term);
		rank_name 	= cleaner.clean(rank_name);
		hgenre 	= cleaner.clean(hgenre);
		rule_name 	= cleaner.clean(rule_name);
		login_id 	= cleaner.clean(login_id);
		category_gb 	= cleaner.clean(category_gb);
		
		String resultcode = "";
		String resultmessage = "";
		
		try{
			validateinsertRank(rank_term,rank_name,hgenre,rule_name,login_id);
			
			service.insertRank(rank_term, rank_name, hgenre, rule_name, login_id, category_gb);
			
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
	 * insertRank/updateRank에 대한 validation 함수
	 * @param rank_term		랭킹기한
	 * @param rank_name		랭킹이름
	 * @param hgenre		장르코드
	 * @param rule_name		룰이름
	 * @param login_id		로그인아이디
	 * @throws SmartUXException
	 */
	private void validateinsertRank(
			String rank_term,
			String rank_name,
			String hgenre,
			String rule_name,
			String login_id
			) throws SmartUXException{
		SmartUXException exception = new SmartUXException();
		if(!(StringUtils.hasText(rank_term))){			
			exception.setFlag("NOT FOUND RANK_TERM");
			exception.setMessage("RANK 기한은 필수로 들어가야 합니다");
			throw exception;
		}
		
		if(!(StringUtils.hasText(rank_name))){			
			exception.setFlag("NOT FOUND RANK_NAME");
			exception.setMessage("RANK 이름은 필수로 들어가야 합니다");
			throw exception;
		}
		
		if(!(StringUtils.hasText(hgenre))){			
			exception.setFlag("NOT FOUND HGENRE");
			exception.setMessage("장르코드는 필수로 들어가야 합니다");
			throw exception;
		}
		
		if(!(StringUtils.hasText(rule_name))){			
			exception.setFlag("NOT FOUND RULE_NAME");
			exception.setMessage("RULE 이름은 필수로 들어가야 합니다");
			throw exception;
		}	
		
		if(!(StringUtils.hasText(login_id))){			
			exception.setFlag("NOT FOUND LOGIN_ID");
			exception.setMessage("로그인아이디는 필수로 들어가야 합니다");
			throw exception;
		}	
	}
	
	/**
	 * Rank Item 코드값 변경처리 함수
	 * @param rank_term		랭킹기한
	 * @param rank_name		랭킹이름
	 * @param hgenre		장르코드
	 * @param rule_name		룰이름
	 * @param smartUXManager		로그인아이디
	 * @return			결과코드 , 결과메시지
	 */
	@RequestMapping(value="/admin/rank/updateRank", method=RequestMethod.POST)
	public @ResponseBody String updateRank(		
			@RequestParam(value="rank_code") String rank_code,
			@RequestParam(value="rank_term") String rank_term,
			@RequestParam(value="rank_name") String rank_name,
			@RequestParam(value="hgenre") String hgenre,
			@RequestParam(value="rule_name") String rule_name,
			@RequestParam(value="smartUXManager") String login_id
			) throws Exception{
		
		HTMLCleaner cleaner = new HTMLCleaner();
		rank_code 	= cleaner.clean(rank_code);
		rank_term 	= cleaner.clean(rank_term);
		rank_name 	= cleaner.clean(rank_name);
		hgenre 	= cleaner.clean(hgenre);
		rule_name 	= cleaner.clean(rule_name);
		login_id 	= cleaner.clean(login_id);
		
		String resultcode = "";
		String resultmessage = "";
		
		try{
			validateinsertRank(rank_term,rank_name,hgenre,rule_name,login_id);
			
			service.updateRank(rank_code, rank_term, rank_name, hgenre, rule_name, login_id);
			
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
	 * Rank Item 코드값 삭제처리 함수
	 * @param rank_code[]		랭킹코드
	 * @return			결과코드 , 결과메시지
	 */
	@RequestMapping(value="/admin/rank/deleteRank", method=RequestMethod.POST)
	public @ResponseBody String deleteRank(@RequestParam(value="rank_code[]") String [] rank_codes) throws Exception{
		String resultcode = "";
		String resultmessage = "";
		
		try{
			validateDeleteRank(rank_codes);
			
			service.deleteRank(rank_codes);
			
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
	 * deleteRank에 대한 validation 함수
	 * @param rank_code[]		랭킹코드
	 * @throws SmartUXException
	 */
	private void validateDeleteRank(String [] rank_codes) throws SmartUXException{
		SmartUXException exception = new SmartUXException();		
		if((rank_codes == null) || (rank_codes.length == 0)){
			
			exception.setFlag("NOT FOUND RULE_CODE");
			exception.setMessage("랭킹코드값은 필수로 들어가야 합니다");
			throw exception;
		}		
	}
	
	/**
	 * Rank Item 앨범 변경처리 함수
	 * @param album_id[]		앨범ID
	 * @param category_id[]		카테고리D
	 * @param rank_code			랭크코드
	 * @param rule_name			룰이름
	 * @param login_id			로그인아이디
	 * @return			결과코드 , 결과메시지
	 */
	@RequestMapping(value="/admin/rank/updateAlbumVod", method=RequestMethod.POST)
	public @ResponseBody String updateAlbumVod(
			@RequestParam(value="album_id[]", required=false) String [] album_id,
			@RequestParam(value="category_id[]", required=false) String [] category_id,
			@RequestParam(value="rank_code", required=false) String rank_code,
			@RequestParam(value="smartUXManager") String login_id,
			@RequestParam(value="rule_type") String rule_type) 
			throws Exception{
		
		String resultcode = "";
		String resultmessage = "";
		
		try{
			validateUpdateAlbumVod(album_id, category_id, rank_code);
			
			service.updateAlbumVod(album_id, category_id, rank_code, login_id, rule_type);
			
			//getCache();			// 장르VOD 캐쉬 동기화 함수호출			-> 				/admin/rank/activateCache 에서 처리
			
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
	 * Rank Item 앨범 변경처리 함수 validation
	 * @param album_id[]		앨범ID
	 * @param category_id[]		카테고리D
	 * @param rank_code			랭크코드
	 * @return			결과코드 , 결과메시지
	 */
	private void validateUpdateAlbumVod( String [] album_id, String [] category_id, String rank_code) throws SmartUXException{
		SmartUXException exception = new SmartUXException();
			
		if((rank_code == null) || (rank_code.equals(""))){
			exception.setFlag("NOT FOUND RANK_CODE");
			exception.setMessage("RANK_CODE가 입력되지 않았습니다");
			throw exception;
		}
		
		if((album_id == null) || (album_id.length == 0)){
			exception.setFlag("NOT FOUND ALBUM_ID");
			exception.setMessage("앨범이 입력되지 않았습니다");
			throw exception;
		}
	
		if((category_id == null) || (category_id.length == 0)){
			exception.setFlag("NOT FOUND CATEGORY_ID");
			exception.setMessage("카테고리가 입력되지 않았습니다");
			throw exception;
		}			
	}		
	
}





