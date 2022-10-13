package com.dmi.smartux.admin.login.controller;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.RandomStringUtils;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.CookieGenerator;

import com.dmi.smartux.common.util.CookieUtil;
import com.dmi.smartux.admin.login.service.LoginService;
import com.dmi.smartux.admin.login.service.PassLogService;
import com.dmi.smartux.admin.login.vo.AdminMenuVO;
import com.dmi.smartux.admin.login.vo.LoginVO;
import com.dmi.smartux.admin.login.vo.PassLogVO;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.util.HTMLCleaner;
import com.dmi.smartux.common.util.SHA512Hash;
import com.dmi.smartux.common.util.aes.Aes_Key;
import com.dmi.smartux.common.util.aes.StringEncrypter;

import net.sf.json.JSONArray;

/**
 * 관리자 계정 관리 컨트롤러 클래스
 * @author YJ KIM
 *
 */
@Controller
public class LoginController {
	
	@Autowired
	LoginService service;
	
	@Autowired
	PassLogService pservice;
	
	private final Log logger = LogFactory.getLog(this.getClass());
	
	/**
	 * 로그인 페이지 화면
	 * @return	로그인 페이지 화면 URL 
	 */
	@RequestMapping(value="/admin/login/login", method=RequestMethod.GET )
	public String getLogin(Model model, @RequestParam(value = "flagtype", required = false, defaultValue = "") String flagtype){
		
		String domain = SmartUXProperties.getProperty("smartux.domain.https");
		model.addAttribute("domain_https", domain);
		model.addAttribute("flagtype", flagtype);
		return "/admin/login/login";
	}
	
	/**
	 * 로그아웃 처리
	 * @param response	응답객체(Cookie 사용)
	 * @return		처리 결과 코드
	 */
	@RequestMapping(value="/admin/login/logout", method=RequestMethod.POST )
	public @ResponseBody String setLogout(
			HttpServletResponse response
			){
		logger.debug("setLogout START");
		//response.addCookie(new Cookie("smartUXManager",""));
		//response.addCookie(new Cookie("smartUXManagerAuth",""));
		CookieGenerator cg = new CookieGenerator();
		cg.setCookieName("smartUXManager");
		cg.addCookie(response,"");
		cg.setCookieName("smartUXManagerAuth");
		cg.addCookie(response,"");
		cg.setCookieName("smartUXCookieTimeout");
		cg.addCookie(response,"");
		
		logger.debug("setLogout END");
		return "SUCCESS";
	}
	
	/**
	 * 로그인 처리
	 * @param id		아이디
	 * @param pwd		비밀번호
	 * @param model		JSP 리턴 오브젝트
	 * @param response	응답객체(Cookie 사용)
	 * @return			관리자 계정 관리 리스트 화면 URL
	 */
	@RequestMapping(value="/admin/login/login", method=RequestMethod.POST )
	public String setLogin(
			@RequestParam(value="smartux_id", required=false, defaultValue="") String id,
			@RequestParam(value="smartux_pwd", required=false, defaultValue="") String pwd,
			Model model,
			HttpServletResponse response
			){
		
		HTMLCleaner cleaner = new HTMLCleaner();
		id 	= cleaner.clean(id);
		pwd = cleaner.clean(pwd);
		
		model.addAttribute("smartux_id", id);
		model.addAttribute("smartux_pwd", pwd);
		
		String domain = SmartUXProperties.getProperty("smartux.domain.https");
		model.addAttribute("domain_https", domain);
		
		logger.debug("setLogin = id : " + id);
		logger.debug("setLogin = pwd : " + pwd);
		
		//유효성 검사
		String validate = validateSetLogin(id,pwd);
		
		if(!validate.equals("")){
			logger.debug("setLogin = validate : " + validate);
			model.addAttribute("result", validate);
			return "/admin/login/login";
		}else{
			try {
				
				pwd = SHA512Hash.getDigest(pwd);
				
				String result = service.setLogin(id,pwd);
				logger.debug("setLogin result = "+result);
				
				model.addAttribute("result", result);
				
				if(result.equals("NOT DATA")){	//데이터 미존재
					model.addAttribute("result", result);
				}else if(result.equals("PASSWORD FAIL")){	//비밀번호 오류
					model.addAttribute("result", result);
				}else if(result.equals("LOGIN FAIL CTN")){	//로그인 시도 3회 실패시 잠금 상태로 인하여 로그인 제한(슈퍼유저로 잠금 해제로 가능)
					model.addAttribute("result", result);
				}else if(result.equals("EXPDATE FAIL")){	//만료일 지남(계정 만료일이 지나 비밀번호 변경 페이지로 이동)
					model.addAttribute("expdate_fail", "expdate_fail");
					return "/admin/login/expdate";
				}else if(result.equals("SUCCESS")){		//로그인 성공
					
					LoginVO vo = new LoginVO();
					vo.setUser_id(id);
					vo = service.getAdminView(vo);
					
					
					//관리자 쿠키 저장
					Aes_Key aec_key = new Aes_Key();
					StringEncrypter encrypter = new StringEncrypter(aec_key.key,aec_key.vec);
					String id_encrypt = java.net.URLEncoder.encode(encrypter.encrypt(vo.getUser_id()));
					String auth_encrtpt = java.net.URLEncoder.encode(encrypter.encrypt(vo.getUser_auth()));
					
//					Cookie cookie = new Cookie("smartUXManager",id_encrypt);
//					cookie.setMaxAge(60*60); //1 hour
//					Cookie cookie2 = new Cookie("smartUXManagerAuth",auth_encrtpt);
//					cookie.setMaxAge(60*60); //1 hour
//					response.addCookie(cookie);
//					response.addCookie(cookie2);
					
					//response.addCookie(new Cookie("smartUXManager",id_encrypt));
					//response.addCookie(new Cookie("smartUXManagerAuth",auth_encrtpt));
					CookieGenerator cg = new CookieGenerator();
					cg.setCookieName("smartUXManager");
					cg.addCookie(response,id_encrypt);
					cg.setCookieName("smartUXManagerAuth");
					cg.addCookie(response,auth_encrtpt);
					cg.setCookieName("smartUXCookieTimeout");
					long cookieStartTime =  (long) (System.currentTimeMillis()/1000.0);
					cg.addCookie(response,Long.toString(cookieStartTime));
					
					domain = SmartUXProperties.getProperty("smartux.domain.http");
					model.addAttribute("domain_http", domain);
					model.addAttribute("user_auth" , vo.getUser_auth());
					
					
					String msa_open_Yn = "";
					try {
						msa_open_Yn = service.getCodeItemNm(SmartUXProperties.getProperty("msa.open.code"), SmartUXProperties.getProperty("msa.open.date.code"));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					model.addAttribute("msa_open_Yn" , msa_open_Yn);
					
					return "/admin/main";
					//return "redirect:/admin/login/list.do?";
					//return "forword:/admin/login/list.do";
				}
				
			} catch (Exception e) {
				//logger.error("setLogin "+e.getClass().getName());
				//logger.error("setLogin "+e.getMessage());
				logger.error("[setLogin]["+e.getClass().getName()+"]["+e.getMessage()+"]");
				model.addAttribute("result", "ERROR");
				return "/admin/login/login";
			}
		}
		
		return "/admin/login/login";	//관리자 메인페이지로 이동
	}
	
	/**
	 * 로그인 만료로 인한 수정
	 * @param id		아이디
	 * @param pwd		비밀번호
	 * @param expday	만료일
	 * @param model		JSP 리턴 오브젝트
	 * @return			로그인 페이지 화면 URL
	 */
	@RequestMapping(value="/admin/login/expdate", method=RequestMethod.POST )
	public String setExpDate(
			@RequestParam(value="smartux_id", required=false, defaultValue="") String id,
			@RequestParam(value="smartux_pwd", required=false, defaultValue="") String pwd,
			@RequestParam(value="smartux_exp_day", required=false, defaultValue="") String expday,
			Model model
			){
		
		HTMLCleaner cleaner = new HTMLCleaner();
		id 	= cleaner.clean(id);
		pwd = cleaner.clean(pwd);
		expday = cleaner.clean(expday);
		
		model.addAttribute("smartux_id", id);
		model.addAttribute("smartux_pwd", pwd);
		model.addAttribute("smartux_exp_day", expday);
		model.addAttribute("expdate_fail", "expdate_fail");
		
		logger.debug("setExpDate = id : " + id);
		logger.debug("setExpDate = pwd : " + pwd);
		logger.debug("setExpDate = expday : " + expday);
		
		//유효성 검사
		String validate = validateSetExpDate(id,pwd,expday);
		if(!validate.equals("")){
			logger.debug("setLogin = validate : " + validate);
			model.addAttribute("result", validate);			
			return "/admin/login/expdate";
		}else{
			try {
				
				pwd = SHA512Hash.getDigest(pwd);
				
				//랜덤 salt key생성
				String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@$%^&*";
				String salt = RandomStringUtils.random( 10, 0, characters.length(), false, false, characters.toCharArray(), new SecureRandom() );
				
				
				PassLogVO pvo = new PassLogVO();
				pvo.setPassword(pwd);
				pvo.setUser_id(id);
				
				
				//히스토리체크
				List<PassLogVO> chklist = pservice.getPassList(pvo);
				if(chklist.size() > 0){
					validate = "PASS HIST";
					logger.debug("setLogin = validate : " + validate);
					model.addAttribute("result", validate);			
					return "/admin/login/expdate";
				}
				
				LoginVO vo = new LoginVO();
				vo.setUser_id(id);
				LoginVO paVo = service.getAdminView(vo);
				
				if(pwd.equals(paVo.getPassword())) {
					validate = "PASS DUP";
					logger.debug("setLogin = validate : " + validate);
					model.addAttribute("result", validate);			
					return "/admin/login/expdate";
				}
				
				String result = service.setExpDate(id,pwd,expday,salt);
				
				
				
				String oldPass =  paVo.getPassword();
				pvo.setPassword(oldPass);
				pservice.insertPass(pvo);
				
				pvo.setPassword(null);
				List<PassLogVO> list = pservice.getPassList(pvo);
				
				if(list.size() > 3) {
					int pid = list.get(0).getP_id();
					pservice.deletePass(pid);
				}
				//log 저장 끝
				
				
				logger.debug("setExpDate result = "+result);
				//성공 
				model.addAttribute("result", "");
				model.addAttribute("smartux_id", "");
				model.addAttribute("smartux_pwd", "");
				return "/admin/login/login";
			} catch (Exception e) {
				//logger.error("setExpDate "+e.getClass().getName());
				//logger.error("setExpDate "+e.getMessage());
				logger.error("[setExpDate]["+e.getClass().getName()+"]["+e.getMessage()+"]");
				model.addAttribute("result", "ERROR");			
				return "/admin/login/expdate";
			}
		}
		//return "/admin/login/expdate";
	}
	
	/**
	 * 관리자 계정 리스트 
	 * @param findName		검색구분
	 * @param findValue		검색어	
	 * @param pageNum		페이지 번호
	 * @param model			JSP 리턴 오브젝트
	 * @return				관리자 계정 관리 리스트 화면 URL
	 */
	@RequestMapping(value="/admin/login/list", method=RequestMethod.GET )
	public String getAdminList(
			@RequestParam(value="findName", required=false, defaultValue="") String findName,
			@RequestParam(value="findValue", required=false, defaultValue="") String findValue,
			@RequestParam(value="pageNum", required=false, defaultValue="1") String pageNum,
			@RequestParam(value="print", required=false, defaultValue="") String print,
			Model model
			){
		
		HTMLCleaner cleaner = new HTMLCleaner();
		findName 	= cleaner.clean(findName);
		findValue = cleaner.clean(findValue);
		pageNum = cleaner.clean(pageNum);
		
		logger.debug("getAdminList = findName : " + findName);
		logger.debug("getAdminList = findValue : " + findValue);
		logger.debug("getAdminList = pageNum : " + pageNum);
		
		LoginVO loginVO = new LoginVO();
		
		pageNum = GlobalCom.isNull(pageNum, "1");
		
		loginVO.setFindName(findName);
		loginVO.setFindValue(findValue);
		loginVO.setPageNum(Integer.parseInt(pageNum));
		
		loginVO.setPageSize(GlobalCom.isNumber(loginVO.getPageSize(),10));
		loginVO.setBlockSize(GlobalCom.isNumber(loginVO.getBlockSize(),10));
		loginVO.setPageNum(GlobalCom.isNumber(loginVO.getPageNum(),1));
		
		loginVO.setFindName(GlobalCom.isNull(loginVO.getFindName(), "USER_ID"));
		loginVO.setFindValue(GlobalCom.isNull(loginVO.getFindValue()));
		
		try {
			List<LoginVO> list = service.getAdminList(loginVO);
			
			Aes_Key aec_key = new Aes_Key();
			StringEncrypter encrypter = new StringEncrypter(aec_key.key,aec_key.vec);
			
			List<LoginVO> result_list = new ArrayList<LoginVO>();
			for(int i=0;i<list.size();i++){
				LoginVO data = (LoginVO) list.get(i);
				
				String s_encrypt = encrypter.encrypt(data.getUser_id());
				s_encrypt = java.net.URLEncoder.encode(s_encrypt);
				data.setUser_id_aes(s_encrypt);
				
				result_list.add(data);
			}
			
			loginVO.setList(result_list);
			
			loginVO.setPageCount(service.getAdminListCtn(loginVO));
			
			model.addAttribute("vo", loginVO);
			model.addAttribute("print",print);
		} catch (Exception e) {
			//logger.error("getAdminList "+e.getClass().getName()); 
			//logger.error("getAdminList "+e.getMessage());
			logger.error("[getAdminList]["+e.getClass().getName()+"]["+e.getMessage()+"]");
		}
		
		
		return "/admin/login/list";
	}
	
	/**
	 * 관리가 계정 잠금해제
	 * @param id	아이디
	 * @return		결과 코드
	 */
	@RequestMapping(value="/admin/login/clear", method=RequestMethod.POST )
	public @ResponseBody String setLoginFailClear(
			@RequestParam(value="smartux_id", required=false, defaultValue="") String id
			){
		
		HTMLCleaner cleaner = new HTMLCleaner();
		id 	= cleaner.clean(id);
		
		logger.debug("setLoginFailClear = smartux_id : " + id);
		String result = "SUCCESS";
		
		if(!(StringUtils.hasText(id))){
			result = "NOT ID";
			return result;
		}
		
		try{
			service.setLoginFailClear(id);
		}catch(Exception e){
			result = "FAIL";
		}
		
		return result;
	}
	
	/**
	 * 관리자 계정 생성 화면 이동
	 * @param findName		검색구분
	 * @param findValue		검색어
	 * @param pageNum		페이지 번호
	 * @param model			JSP 리턴 오브젝트
	 * @return				관리가 계정 생성 화면 URL
	 * @throws Exception 
	 */
	@RequestMapping(value="/admin/login/insert", method=RequestMethod.GET )
	public String getAdminInsert(
			@RequestParam(value="findName", required=false, defaultValue="") String findName,
			@RequestParam(value="findValue", required=false, defaultValue="") String findValue,
			@RequestParam(value="pageNum", required=false, defaultValue="1") String pageNum,
			@RequestParam(value = "pass", required = false, defaultValue = "1") String pass, 
			HttpServletRequest request,
			Model model
			) throws Exception{
		
		HTMLCleaner cleaner = new HTMLCleaner();
		findName 	= cleaner.clean(findName);
		findValue = cleaner.clean(findValue);
		pageNum = cleaner.clean(pageNum);
		
		LoginVO loginVO = new LoginVO();
		
		pageNum = GlobalCom.isNull(pageNum, "1");
		
		loginVO.setFindName(findName);
		loginVO.setFindValue(findValue);
		loginVO.setPageNum(Integer.parseInt(pageNum));
		model.addAttribute("vo", loginVO);
		
		String loginUserId = CookieUtil.getCookieUserID(request);
		
		if (!GlobalCom.isNull(loginUserId).isEmpty() && !GlobalCom.isNull(pass).isEmpty()) {
			pass = SHA512Hash.getDigest(pass);
			
			String result;
			try {
				result = service.setLogin(loginUserId,pass);
			
			logger.debug("setLogin result = "+result);
			
			if(result.equals("SUCCESS")){
			}else if(result.equals("PASSWORD FAIL")){
				loginVO.setValidate("ERROR");
				model.addAttribute("vo", loginVO);
				return "redirect:/admin/login/list.do?print=5";
			} else if (result.equals("LOGIN FAIL CTN")) {
				loginVO.setValidate("ERROR");
				model.addAttribute("vo", loginVO);
				return "redirect:/admin/login/list.do?print=6";
			}
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				loginVO.setValidate("ERROR");
				model.addAttribute("vo", loginVO);
				return "redirect:/admin/login/list.do";
			}
		} else {
			loginVO.setValidate("ERROR");
			model.addAttribute("vo", loginVO);
			return "redirect:/admin/login/list.do?print=0";
		}
		
		String domain = SmartUXProperties.getProperty("smartux.domain.https");
		model.addAttribute("domain_https", domain);
		
		return "admin/login/insert";
		 
	}
	
	/**
	 * 관리자 계정 생성
	 * @param model			JSP 리턴 오브젝트
	 * @param findName		검색구분
	 * @param findValue		검색어
	 * @param pageNum		페이지 번호
	 * @param user_id		아이디
	 * @param name			이름
	 * @param password		비밀번호
	 * @param email			이메일
	 * @param exp_day		만료일
	 * @param memo			메모
	 * @return				관리자 계정 생성 화면 URL
	 */
	@RequestMapping(value="/admin/login/insert", method=RequestMethod.POST )
	public ModelAndView setAdminInsert(
			Model model,
			@RequestParam(value="findName", required=false, defaultValue="") String findName,
			@RequestParam(value="findValue", required=false, defaultValue="") String findValue,
			@RequestParam(value="pageNum", required=false, defaultValue="1") String pageNum,
			@RequestParam(value="user_id", required=false, defaultValue="") String user_id,
			@RequestParam(value="name", required=false, defaultValue="") String name,
			@RequestParam(value="password", required=false, defaultValue="") String password,
			@RequestParam(value="email", required=false, defaultValue="") String email,
			@RequestParam(value="exp_day", required=false, defaultValue="") String exp_day,
			@RequestParam(value="memo", required=false, defaultValue="") String memo,
			@RequestParam(value="user_auth", required=false, defaultValue="01") String user_auth,HttpServletRequest request
			){
		
		String domain = SmartUXProperties.getProperty("smartux.domain.http");
		ModelAndView mav = new ModelAndView();

		
		HTMLCleaner cleaner = new HTMLCleaner();
		findName 	= cleaner.clean(findName);
		findValue = cleaner.clean(findValue);
		pageNum = cleaner.clean(pageNum);
		user_id = cleaner.clean(user_id);
		name = cleaner.clean(name);
		password = cleaner.clean(password);
		email = cleaner.clean(email);
		exp_day = cleaner.clean(exp_day);
		memo = cleaner.clean(memo);
		user_auth = cleaner.clean(user_auth);
		
		logger.debug("setAdminInsert = findName : " + findName);
		logger.debug("setAdminInsert = findValue : " + findValue);
		logger.debug("setAdminInsert = pageNum : " + pageNum);
		logger.debug("setAdminInsert = user_id : " + user_id);
		logger.debug("setAdminInsert = name : " + name);
		logger.debug("setAdminInsert = password : " + password);
		logger.debug("setAdminInsert = email : " + email);
		logger.debug("setAdminInsert = exp_day : " + exp_day);
		logger.debug("setAdminInsert = memo : " + memo);
		logger.debug("setAdminInsert = user_auth : " + user_auth);
		
		LoginVO vo = new LoginVO();
		
		pageNum = GlobalCom.isNull(pageNum, "1");
		
		vo.setFindName(findName);
		vo.setFindValue(findValue);
		vo.setPageNum(Integer.parseInt(pageNum));
		
		
		//유효성 검사
		String validate = validateSetAdminInsert(user_id,password,exp_day,name);
		if(!validate.equals("")){
			logger.debug("setAdminInsert = validate : " + validate);
			vo.setValidate(validate);
			//model.addAttribute("vo", vo);
			//return "/admin/login/insert";
			mav.addObject("vo", vo);
			mav.setViewName("redirect:/admin/login/list.do?print=0");
			return mav;
		}else{
			//성공
			vo.setUser_id(user_id);
			vo.setName(name);
			vo.setPassword(SHA512Hash.getDigest(password));
			vo.setEmail(email);
			vo.setExp_day(Integer.parseInt(exp_day));
			vo.setMemo(memo);
			
			
			
			try{
				//랜덤 salt key생성
				/*SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
				byte[] bytes = new byte[10];
				random.nextBytes(bytes);
				String salt = new String(Base64.getEncoder().encode(bytes));*/
				
				String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@$%^&*";
				String salt = RandomStringUtils.random( 10, 0, characters.length(), false, false, characters.toCharArray(), new SecureRandom() );
				
				vo.setSalt_key(salt);
				
				LoginVO vo1 = new LoginVO();
				String c_user_id = CookieUtil.getCookieUserID(request);
				vo1.setUser_id(c_user_id);
				LoginVO paVo = service.getAdminView(vo1);
				
				if(!"00".equals(paVo.getUser_auth())) {
					
					vo.setUser_auth(paVo.getUser_auth());
				}else {
					vo.setUser_auth(user_auth);
				}
				
				validate = service.setAdminInsert(vo);
				
				if(validate.equals("SUCCESS")){
					vo.setValidate("INSERT SUCCESS");
				}else{
					vo.setValidate("IS DATA");
				}
				
				
				model.addAttribute("vo", vo);
				//return "/admin/login/insert";
				//return new ModelAndView("redirect:"+domain+"/smartux_adm/admin/login/insert.do");
				mav.addObject("vo", vo);
				mav.setViewName("redirect:/admin/login/list.do?print=1");
				return mav;
			}catch(Exception e){
				//logger.error("setAdminInsert "+e.getClass().getName());
				//logger.error("setAdminInsert "+e.getMessage());
				logger.error("[setAdminInsert]["+e.getClass().getName()+"]["+e.getMessage()+"]");
				validate = "ERROR";
				vo.setValidate(validate);
				//model.addAttribute("vo", vo);
				//return "/admin/login/insert";
				//return new ModelAndView("redirect:"+domain+"/smartux_adm/admin/login/insert.do");
				mav.addObject("vo", vo);
				mav.setViewName("redirect:/admin/login/list.do?print=0");
				return mav;
			}
		}
		
		//return new ModelAndView("redirect:"+domain+"/smartux_adm/admin/login/list.do");
		//return "admin/login/insert";
	}
	
	@RequestMapping(value = "/admin/login/chkpass", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> chkpass(
			@RequestParam(value = "password", required = false, defaultValue = "") String password,
			@RequestParam(value = "id", required = false, defaultValue = "") String id,
			HttpServletRequest request) throws Exception {
		
		String resultCode = "";
		String resultMsg = "";

		if (!GlobalCom.isNull(password).isEmpty() && !GlobalCom.isNull(password).isEmpty()) {
			try {
				
				password = SHA512Hash.getDigest(password);
				
				String result = service.setLogin(id,password);
				logger.debug("setLogin result = "+result);
				
				
				if(result.equals("SUCCESS")){
					resultCode = SmartUXProperties.getProperty("flag.success");
					resultMsg = SmartUXProperties.getProperty("message.success");
				}else {
					resultCode = SmartUXProperties.getProperty("flag.fail");
					resultMsg = SmartUXProperties.getProperty("message.fail");
				}
				
			} catch (Exception e) {
				
				resultCode = SmartUXProperties.getProperty("flag.fail");
				resultMsg = SmartUXProperties.getProperty("message.fail");
			}
			
		}

		String result = "{\"flag\" : \"" + resultCode + "\", \"message\" : \"" + resultMsg + "\"}";

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(result, responseHeaders, HttpStatus.CREATED);
	}
	
	/**
	 * 관리자 계정 정보 수정 화면 이동
	 * @param findName		검색구분
	 * @param findValue		검색어
	 * @param pageNum		페이지 번호
	 * @param user_id		아이디
	 * @param model			JSP 리턴 오브젝트
	 * @return				관리자 계정 수정 화면 URL
	 * @throws Exception 
	 */
	@RequestMapping(value="/admin/login/updatePage", method=RequestMethod.POST )
	public String getAdminView(
			@RequestParam(value="findName", required=false, defaultValue="") String findName,
			@RequestParam(value="findValue", required=false, defaultValue="") String findValue,
			@RequestParam(value="pageNum", required=false, defaultValue="1") String pageNum,
			@RequestParam(value="user_id", required=false, defaultValue="") String user_id,
			@RequestParam(value = "pass", required = false, defaultValue = "") String pass,
			Model model,
			HttpServletRequest request) throws Exception{
		try{
			Aes_Key aec_key = new Aes_Key();
			StringEncrypter encrypter = new StringEncrypter(aec_key.key,aec_key.vec);
			//user_id = encrypter.decrypt(java.net.URLDecoder.decode(user_id));
			//String s_encrypt = java.net.URLDecoder.decode(user_id); //쿠키사용시 decoding
			user_id = encrypter.decrypt(user_id);

		}catch(Exception e){}
		
		String domain = SmartUXProperties.getProperty("smartux.domain.https");
		model.addAttribute("domain_https", domain);
		
		HTMLCleaner cleaner = new HTMLCleaner();
		findName 	= cleaner.clean(findName);
		findValue = cleaner.clean(findValue);
		pageNum = cleaner.clean(pageNum);
		user_id = cleaner.clean(user_id);
		
		logger.debug("getAdminView = findName : " + findName);
		logger.debug("getAdminView = findValue : " + findValue);
		logger.debug("getAdminView = pageNum : " + pageNum);
		logger.debug("getAdminView = user_id : " + user_id);
		
		LoginVO vo = new LoginVO();
		
		pageNum = GlobalCom.isNull(pageNum, "1");
		
		vo.setFindName(findName);
		vo.setFindValue(findValue);
		vo.setPageNum(Integer.parseInt(pageNum));
		
		String loginUser = "";
		try {
			loginUser = CookieUtil.getCookieUserID(request);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw e1;
		}
		
		if (!GlobalCom.isNull(loginUser).isEmpty() && !GlobalCom.isNull(pass).isEmpty()) {
			pass = SHA512Hash.getDigest(pass);
			
			String result;
			try {
				result = service.setLogin(loginUser,pass);
			
			logger.debug("setLogin result = "+result);
			
			if(result.equals("SUCCESS")){
			}else if(result.equals("PASSWORD FAIL")){
				vo.setValidate("ERROR");
				model.addAttribute("vo", vo);
				return "redirect:/admin/login/list.do?print=5";
			} else if (result.equals("LOGIN FAIL CTN")) {
				vo.setValidate("ERROR");
				model.addAttribute("vo", vo);
				return "redirect:/admin/login/list.do?print=6";
			}
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				vo.setValidate("ERROR");
				model.addAttribute("vo", vo);
				return "redirect:/admin/login/list.do";
			}
		} else {
			vo.setValidate("ERROR");
			model.addAttribute("vo", vo);
			return "redirect:/admin/login/list.do?print=0";
		}
		
		if(!(StringUtils.hasText(user_id))){
			vo.setValidate("NOT ID");
			model.addAttribute("vo", vo);
			return "admin/login/list";
		}else{
			vo.setUser_id(user_id);
			
			try{
				LoginVO rvo = service.getAdminView(vo);
				rvo.setFindName(findName);
				rvo.setFindValue(findValue);
				rvo.setPageNum(Integer.parseInt(pageNum));
				
				model.addAttribute("vo", rvo);
				return "admin/login/update";
			}catch(Exception e){
				//logger.error("getAdminView "+e.getClass().getName());
				//logger.error("getAdminView "+e.getMessage());
				logger.error("[getAdminView]["+e.getClass().getName()+"]["+e.getMessage()+"]");
				vo.setValidate("ERROR");
				model.addAttribute("vo", vo);
				return "/admin/login/login";
			}
		}
		//return "admin/login/list";
	}
	
	/**
	 * 관리자 계정 정보 수정
	 * @param model			JSP 리턴 오브젝트
	 * @param findName		검색구분
	 * @param findValue		검색어
	 * @param pageNum		페이지 번호
	 * @param user_id		아이디
	 * @param name			이름
	 * @param password		비밀번호
	 * @param email			이메일
	 * @param exp_day		신규 만료일
	 * @param org_exp_day	기존 만료일
	 * @param exp_date		만료날짜
	 * @param memo			메모
	 * @return				관리자 계정 수정 화면 URL
	 * @throws Exception 
	 */
	@RequestMapping(value="/admin/login/update", method=RequestMethod.POST )
	public ModelAndView setAdminUpdate(
			Model model,
			@RequestParam(value="findName", required=false, defaultValue="") String findName,
			@RequestParam(value="findValue", required=false, defaultValue="") String findValue,
			@RequestParam(value="pageNum", required=false, defaultValue="1") String pageNum,
			@RequestParam(value="user_id", required=false, defaultValue="") String user_id,
			@RequestParam(value="name", required=false, defaultValue="") String name,
			@RequestParam(value="password", required=false, defaultValue="") String password,
			@RequestParam(value="email", required=false, defaultValue="") String email,
			@RequestParam(value="exp_day", required=false, defaultValue="") String exp_day,
			@RequestParam(value="org_exp_day", required=false, defaultValue="") String org_exp_day,
			@RequestParam(value="exp_date", required=false, defaultValue="") String exp_date,
			@RequestParam(value="memo", required=false, defaultValue="") String memo,
			@RequestParam(value="user_auth", required=false, defaultValue="01") String user_auth,
			@RequestParam(value="org_user_id", required=false, defaultValue="") String org_user_id,
			@RequestParam(value="org_user_auth", required=false, defaultValue="") String org_user_auth,
			@RequestParam(value="oldpassword", required=false, defaultValue="") String oldpassword,
			HttpServletResponse response,HttpServletRequest request
			) throws Exception{
		
		HTMLCleaner cleaner = new HTMLCleaner();
		findName 	= cleaner.clean(findName);
		findValue = cleaner.clean(findValue);
		pageNum = cleaner.clean(pageNum);
		user_id = cleaner.clean(user_id);
		name = cleaner.clean(name);
		password = cleaner.clean(password);
		email = cleaner.clean(email);
		exp_day = cleaner.clean(exp_day);
		org_exp_day = cleaner.clean(org_exp_day);
		exp_date = cleaner.clean(exp_date);
		memo = cleaner.clean(memo);
		user_auth = cleaner.clean(user_auth);
		org_user_id = cleaner.clean(org_user_id);
		org_user_auth = cleaner.clean(org_user_auth);
		oldpassword = cleaner.clean(oldpassword);
		
		
		logger.debug("setAdminUpdate = findName : " + findName);
		logger.debug("setAdminUpdate = findValue : " + findValue);
		logger.debug("setAdminUpdate = pageNum : " + pageNum);
		logger.debug("setAdminUpdate = user_id : " + user_id);
		logger.debug("setAdminUpdate = name : " + name);
		logger.debug("setAdminUpdate = password : " + password);
		logger.debug("setAdminUpdate = email : " + email);
		logger.debug("setAdminUpdate = exp_day : " + exp_day);
		logger.debug("setAdminUpdate = exp_date : " + exp_date);
		logger.debug("setAdminUpdate = memo : " + memo);
		logger.debug("setAdminUpdate = user_auth : " + user_auth);
		logger.debug("setAdminUpdate = org_user_id : " + org_user_id);
		logger.debug("setAdminUpdate = org_user_auth : " + org_user_auth);
		logger.debug("setAdminUpdate = oldpassword : " + oldpassword);
		
		String domain = SmartUXProperties.getProperty("smartux.domain.http");
		ModelAndView mav = new ModelAndView();
		
		LoginVO vo = new LoginVO();
		
		pageNum = GlobalCom.isNull(pageNum, "1");
		
		vo.setFindName(findName);
		vo.setFindValue(findValue);
		vo.setPageNum(Integer.parseInt(pageNum));
		
		//유효성 검사
		String validate = validateSetAdminUpdate(user_id,password,exp_day,name,oldpassword);

		if(!validate.equals("")){
			logger.debug("setAdminUpdate = validate : " + validate);
			vo.setValidate(validate);
			model.addAttribute("vo", vo);
			//return "/admin/login/update";
			
			mav.setViewName("redirect:"+"/admin/login/list.do?print=0");
			if (validate.equals("4")) {
				mav.setViewName("redirect:"+"/admin/login/list.do?print=4");
			}
			return mav;
		}else{
			//기존비번 비번 확인 3개가 다 있는경우 비번 변경
				//랜덤코드생성
				//생성된 번호와 비번저장
			//아닐경우 내용만 변경
			
			if (!GlobalCom.isNull(oldpassword).isEmpty() && !GlobalCom.isNull(password).isEmpty()) {
				oldpassword = SHA512Hash.getDigest(oldpassword);
				password = SHA512Hash.getDigest(password);
				String result = service.setLogin(user_id,oldpassword);
				logger.debug("setLogin result = "+result);
				
				
				if(result.equals("SUCCESS")){
					if(oldpassword.equals(password)){
						validate = "PASSWORD AGREE";
						vo.setValidate(validate);
						model.addAttribute("vo", vo);
						//return "/admin/login/update";
						mav.setViewName("redirect:"+"/admin/login/list.do?print=2");
						return mav;
					}else { //랜덤코드생성
						String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@$%^&*";
						String salt = RandomStringUtils.random( 10, 0, characters.length(), false, false, characters.toCharArray(), new SecureRandom() );
						
						vo.setPassword(password);
						vo.setSalt_key(salt);
						
					}
					
					
				}
				
			}
			LoginVO vo1 = new LoginVO();
			String c_user_id = CookieUtil.getCookieUserID(request);
			vo1.setUser_id(c_user_id);
			LoginVO paVo = service.getAdminView(vo1);
			
			if(!"00".equals(paVo.getUser_auth())) {
				
				vo.setUser_auth(paVo.getUser_auth());
			}else {
				vo.setUser_auth(user_auth);
			}
			
			//성공
			vo.setUser_id(user_id);
			vo.setName(name);
			vo.setEmail(email);
			vo.setExp_day(Integer.parseInt(exp_day));
			if(!org_exp_day.equals(exp_day)){
				vo.setExp_date(GlobalCom.getTodayAddDate(GlobalCom.getTodayFormat(),Integer.parseInt(exp_day)));
			}else{
				vo.setExp_date(exp_date);
			}
			vo.setMemo(memo);
			
			
			try{
				
				PassLogVO pvo = new PassLogVO();
				pvo.setPassword(password);
				pvo.setUser_id(user_id);
				LoginVO check_vo = service.getAdminView(vo);	
				
				if (!GlobalCom.isNull(oldpassword).isEmpty() && !GlobalCom.isNull(password).isEmpty()) {
					//비밀번호가 같은 경우 변경 불가능
								
					if(check_vo.getPassword().equals(vo.getPassword())){
						validate = "PASSWORD AGREE";
						vo.setValidate(validate);
						model.addAttribute("vo", vo);
						//return "/admin/login/update";
						mav.setViewName("redirect:"+"/admin/login/list.do?print=2");
						return mav;
					}
				
				
					//히스토리체크
					List<PassLogVO> chklist = pservice.getPassList(pvo);
					if(chklist.size() > 0){
						validate = "PASSWORD HIS";
						vo.setValidate(validate);
						model.addAttribute("vo", vo);
						//return "/admin/login/update";
						mav.setViewName("redirect:"+"/admin/login/list.do?print=3");
						return mav;
					}
				}
				
				
				service.setAdminUpdate(vo);
				
				if (!GlobalCom.isNull(oldpassword).isEmpty() && !GlobalCom.isNull(password).isEmpty()) {
					pvo.setPassword(oldpassword);
					pservice.insertPass(pvo);
					
					pvo.setPassword(null);
					List<PassLogVO> list = pservice.getPassList(pvo);
					
					//히스토리에 3개 넘으면 삭제
					if(list.size() > 3) {
						int pid = list.get(0).getP_id();
						pservice.deletePass(pid);
					}
				}
				//log 저장 끝
				
				LoginVO rvo = service.getAdminView(vo);
				rvo.setFindName(findName);
				rvo.setFindValue(findValue);
				rvo.setPageNum(Integer.parseInt(pageNum));
				rvo.setValidate("UPDATE SUCCESS");
				model.addAttribute("vo", rvo);
				
				if (!GlobalCom.isNull(oldpassword).isEmpty() && !GlobalCom.isNull(password).isEmpty()) {
					if(org_user_id.equals(user_id) && !check_vo.getPassword().equals(vo.getPassword())) {
						logger.debug("setLogout START");
						//response.addCookie(new Cookie("smartUXManager",""));
						//response.addCookie(new Cookie("smartUXManagerAuth",""));
						CookieGenerator cg = new CookieGenerator();
						cg.setCookieName("smartUXManager");
						cg.addCookie(response,"");
						cg.setCookieName("smartUXManagerAuth");
						cg.addCookie(response,"");
						cg.setCookieName("smartUXCookieTimeout");
						cg.addCookie(response,"");
						
						logger.debug("setLogout END");
						mav.setViewName("redirect:" + "/admin/login/login.do?flagtype=1");
						
					}else {
						
						//관리자 쿠키 저장
						Aes_Key aec_key = new Aes_Key();
						StringEncrypter encrypter = new StringEncrypter(aec_key.key,aec_key.vec);
						String auth_encrtpt = encrypter.encrypt(paVo.getUser_auth());
						//response.addCookie(new Cookie("smartUXManagerAuth",auth_encrtpt));
						CookieGenerator cg = new CookieGenerator();
						cg.setCookieName("smartUXManagerAuth");
						cg.addCookie(response,auth_encrtpt);
						
						//return "/admin/login/update";
						mav.setViewName("redirect:"+"/admin/login/list.do?print=1");
					}
				}else {
					//관리자 쿠키 저장
					Aes_Key aec_key = new Aes_Key();
					StringEncrypter encrypter = new StringEncrypter(aec_key.key,aec_key.vec);
					String auth_encrtpt = encrypter.encrypt(paVo.getUser_auth());
					//response.addCookie(new Cookie("smartUXManagerAuth",auth_encrtpt));
					CookieGenerator cg = new CookieGenerator();
					cg.setCookieName("smartUXManagerAuth");
					cg.addCookie(response,auth_encrtpt);
					
					//return "/admin/login/update";
					mav.setViewName("redirect:"+"/admin/login/list.do?print=1");
				}
				return mav;
			}catch(Exception e){
				//logger.error("setAdminUpdate "+e.getClass().getName());
				//logger.error("setAdminUpdate "+e.getMessage());
				logger.error("[setAdminUpdate]["+e.getClass().getName()+"]["+e.getMessage()+"]");
				validate = "ERROR";
				vo.setValidate(validate);
				model.addAttribute("vo", vo);
				//return "/admin/login/update";
				mav.setViewName("redirect:"+"/admin/login/list.do");
				return mav;
			}
		}
		
		//return "admin/login/insert";
	}
	
	/**
	 * 관리자 계정 삭제
	 * @param id	아이디
	 * @return		결과코드
	 */
	@RequestMapping(value="/admin/login/delete", method=RequestMethod.POST )
	public @ResponseBody String setAdminDelete(
			@RequestParam(value="smartux_id", required=false, defaultValue="") String id
			){
		

		HTMLCleaner cleaner = new HTMLCleaner();
		id 	= cleaner.clean(id);
		
		logger.debug("setAdminDelete = smartux_id : " + id);
		String result = "SUCCESS";
		
		if(!(StringUtils.hasText(id))){
			result = "NOT ID";
			return result;
		}
		
		try{
			service.setAdminDelete(id);
		}catch(Exception e){
			result = "FAIL";
		}
		
		return result;
	}
	
	@RequestMapping(value="/admin/login/setSuperAdminAuthinit", method=RequestMethod.GET )
	public @ResponseBody String setSuperAdminAuthinit(){
		String result = "SUCCESS";
		
		try {
			service.setSuperAdminAuthinit();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * 관리자 생성 유효성 검사
	 * @param id		아이디
	 * @param pwd		비밀번호
	 * @param expday	만료일
	 * @param name		이름
	 * @return		유효성 검사 결과
	 */
	public String validateSetAdminInsert(String id,String pwd,String expday,String name){
		String result = "";
		result = validateSetExpDate(id,pwd,expday);
		
		if(!(StringUtils.hasText(name))){
			result = "NOT NAME";
			return result;
		}
		
		return result;
	}
	
	/**
	 * 관리자 수정 유효성 검사
	 * @param id		아이디
	 * @param pwd		비밀번호
	 * @param expday	만료일
	 * @param name		이름
	 * @return		유효성 검사 결과
	 */
	public String validateSetAdminUpdate(String id,String pwd,String expday,String name, String oldpassword){
		String result = "";
		result = validateSetPwChk(id,pwd,expday,oldpassword);
		
		if(!(StringUtils.hasText(name))){
			result = "NOT NAME";
			return result;
		}
		
		return result;
	}
	
	/**
	 * 기존비밀번호 유효성 검사
	 * @param id		아이디
	 * @param pwd		비밀번호
	 * @param expdate	만료일
	 * @return			유효성 검사 결과
	 */
	public String validateSetPwChk(String id,String pwd,String expday,String oldpassword){
		String result = "";

		result = validateSetOrgPw(id,pwd,oldpassword);
		
		if(result.equals("")){
			if(!(StringUtils.hasText(expday))){
				result = "NOT EXPDATE";
			}else{
				//숫자만 입력 되었는지 체크
				if(!GlobalCom.checkInt(expday)){
					result = "EXPDATE NOT NUMBER TYPE";
				}
				
				if (Integer.parseInt(expday) > 90) {
					result = "4";
				}
			}
		}
		
		return result;
	}
	
	/**
	 * 로그인 페이지 유효성 검사(기존비밀번호 추가)
	 * @param id	아이디
	 * @param pwd	비밀번호
	 * @return		유효성 검사 결과
	 */
	public String validateSetOrgPw(String id, String pwd,String oldpassword){
		String result = "";
				
		if(!(StringUtils.hasText(id))){
			//result = "아이디가 존재 하지 않습니다.";
			result = "NOT ID";
			return result;
		}
		
		
		if(StringUtils.hasText(pwd)){
			if(pwd.length() < 10 || pwd.length() > 20){
				//result = "비밀번호는 10~20자리 입니다.";
				result = "PWD LENGTH";
				return result;
			}
			
			int pwdChk = GlobalCom.checkStrInt(pwd);
			logger.debug("validateSetLogin = pwdChk : " + pwdChk);
			if(pwdChk != 1){
				//result = "비밀번호는 영문/숫자 조합이 필요합니다.";
				result = "PWD MIX";
			}
		}
		
		if (!GlobalCom.isNull(oldpassword).isEmpty() && !GlobalCom.isNull(oldpassword).isEmpty()) {
			try {
				
				oldpassword = SHA512Hash.getDigest(oldpassword);
				
				result = service.setLogin(id,oldpassword);
				logger.debug("setLogin result = "+result);
				
				
				if(!result.equals("SUCCESS")){
					result = "NOT PWD";
				}else {
					result = "";
				}
				
			} catch (Exception e) {
				
				result = "PWD ERR";
			}
			
		}
		
		return result;
	}
	/**
	 * 만료일 수정 유효성 검사
	 * @param id		아이디
	 * @param pwd		비밀번호
	 * @param expdate	만료일
	 * @return			유효성 검사 결과
	 */
	public String validateSetExpDate(String id,String pwd,String expday){
		String result = "";

		result = validateSetLogin(id,pwd);
		
		if(result.equals("")){
			if(!(StringUtils.hasText(expday))){
				result = "NOT EXPDATE";
			}else{
				//숫자만 입력 되었는지 체크
				if(!GlobalCom.checkInt(expday)){
					result = "EXPDATE NOT NUMBER TYPE";
				}
			}
		}
		
		return result;
	}
	
	/**
	 * 로그인 페이지 유효성 검사
	 * @param id	아이디
	 * @param pwd	비밀번호
	 * @return		유효성 검사 결과
	 */
	public String validateSetLogin(String id, String pwd){
		String result = "";
				
		if(!(StringUtils.hasText(id))){
			//result = "아이디가 존재 하지 않습니다.";
			result = "NOT ID";
			return result;
		}
		
		if(!(StringUtils.hasText(pwd))){
			//result = "비밀번호가 존재 하지 않습니다.";
			result = "NOT PWD";
			return result;
		}else{
			if(pwd.length() < 10 || pwd.length() > 20){
				//result = "비밀번호는 10~20자리 입니다.";
				result = "PWD LENGTH";
				return result;
			}
			
			int pwdChk = GlobalCom.checkStrInt(pwd);
			logger.debug("validateSetLogin = pwdChk : " + pwdChk);
			if(pwdChk != 1){
				//result = "비밀번호는 영문/숫자 조합이 필요합니다.";
				result = "PWD MIX";
			}
		}
		
		return result;
	}
	
	@RequestMapping(value="/admin/login/menuchk", method=RequestMethod.POST)
	public @ResponseBody String menuchk(HttpServletRequest request,
			@RequestParam(value="auth_decrypt", required=false, defaultValue="") String auth_decrypt,
			@RequestParam(value="strArr", required=false, defaultValue="") String strArr
			){
		

		HTMLCleaner cleaner = new HTMLCleaner();
		auth_decrypt 	= cleaner.clean(auth_decrypt);
		List<AdminMenuVO> list = null;
		try {
			list = service.getMenuList(auth_decrypt, strArr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("================================================================"+list.size());
		JSONArray jsonArray = new JSONArray();
		if(list.size()>0){
			   jsonArray = JSONArray.fromObject(list);
		}
		return jsonArray.toString();
	}
	
	/**
	 * 패스워드 화면
	 * @return	패스워드 화면 URL 
	 */
	@RequestMapping(value="/admin/login/password", method=RequestMethod.GET )
	public String getPassword(Model model
			, @RequestParam(value = "user_id", required = false, defaultValue = "") String user_id
			, @RequestParam(value = "findName", required = false, defaultValue = "") String findName
			, @RequestParam(value = "findValue", required = false, defaultValue = "") String findValue
			, @RequestParam(value = "pageNum", required = false, defaultValue = "") String pageNum
			, @RequestParam(value = "type", required = false, defaultValue = "") String type){
		
		String domain = SmartUXProperties.getProperty("smartux.domain.https");
		model.addAttribute("domain_https", domain);
		model.addAttribute("user_id", user_id);
		model.addAttribute("findName", findName);
		model.addAttribute("findValue", findValue);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("type", type);
		return "/admin/login/password";
	}
	
	@RequestMapping(value="/admin/login/menu/displayChk", method=RequestMethod.GET )
	public @ResponseBody String getMenuDisplaychk(Model model){
		String code_id = "";
		try {
			code_id = service.getCodeItem(SmartUXProperties.getProperty("msa.open.code"), SmartUXProperties.getProperty("msa.open.date.code"), SmartUXProperties.getProperty("msa.amims.close.menu"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "{\"code_id\" : \"" + code_id +"\"}";
	}
	
	/**
	 * 메인 화면
	 * @return	메인 화면 URL 
	 */
	@RequestMapping(value="/admin/login/loginMain", method=RequestMethod.GET )
	public String getloginMain(Model model){
		return "/admin/login/loginMain";
	}
}
