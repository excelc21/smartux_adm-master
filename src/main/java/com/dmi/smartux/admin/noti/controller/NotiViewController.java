package com.dmi.smartux.admin.noti.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.multipart.MultipartFile;

import com.dmi.commons.consts.ResultCode;
import com.dmi.commons.exception.MimsCommonException;
import com.dmi.commons.factory.FileProcessFunction;
import com.dmi.commons.factory.FileProcessLockFactory;
import com.dmi.commons.logger.CLog;
import com.dmi.commons.model.RestfulCodeMsg;
import com.dmi.commons.utility.HttpClientUtils;
import com.dmi.commons.utility.RestfulUtils;
import com.dmi.smartux.admin.code.service.CodeService;
import com.dmi.smartux.admin.code.vo.CodeItemVO;
import com.dmi.smartux.admin.noti.service.NotiViewService;
import com.dmi.smartux.admin.noti.vo.CopyNotiPopupVo;
import com.dmi.smartux.admin.noti.vo.NotiListVO;
import com.dmi.smartux.admin.noti.vo.NotiPopVo;
import com.dmi.smartux.admin.noti.vo.NotiVO;
import com.dmi.smartux.admin.notipop.service.NotiAdminPopService;
import com.dmi.smartux.admin.notipop.vo.notiPopVo;
import com.dmi.smartux.admin.statbbsAdmin.service.StatbbsAdminService;
import com.dmi.smartux.admin.statbbsAdmin.vo.StatbbsMiniListVo;
import com.dmi.smartux.common.exception.ExceptionHandler;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.CookieUtil;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.util.HTMLCleaner;
import com.dmi.smartux.common.vo.CUDResult;

import net.sf.json.JSONSerializer;



/**
 *  관리자 공지사항 컨트롤러
 *  
 */
@Controller
public class NotiViewController {
	@Autowired 
	NotiViewService service;
	
	@Autowired
	StatbbsAdminService stat_service;
	
	//긴급공지/팝업공지 캐싱하기 위한 데이터를 저장하기 위해
	@Autowired
	NotiAdminPopService notipopService;
	
	@Autowired
	CodeService codeservice;
		
	private final Log logger =LogFactory.getLog(NotiViewService.class);	
	
	private final Log applyLogger = LogFactory.getLog("adminApplyCacheHistroy");

	/**
	 * 목록화면에서 즉시적용 버튼을 클릭하여 캐쉬를 바로 적용할때 이용된다
	 * @param bbs_id : 2021.12.08 AMIMS 개선 : 부분 즉시적용 추가
	 * @param code	캐쉬를 바로 적용시킬 코드
	 * @return
	 */
	@RequestMapping(value="/admin/noti/applyCache", method=RequestMethod.POST)
	public ResponseEntity<String> applyCache(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value="callByScheduler", defaultValue="Y") final String callByScheduler,
			@RequestParam(value="bbs_id", defaultValue = "") final String bbs_id
			//@RequestParam(value="code") String code
			) throws Exception {
		
		final CLog cLog = new CLog(applyLogger, request.getRemoteAddr(), request.getRequestURI(), request.getMethod());
		final String loginUser = callByScheduler.equals("Y") ? "batch" : CookieUtil.getCookieUserID(request);
		final CUDResult result = new CUDResult();
		
		try {
			cLog.startLog("공지 즉시적용 :", callByScheduler, loginUser, bbs_id);
			final String host = SmartUXProperties.getProperty("cache.sync.public.host");
			final String port = SmartUXProperties.getProperty("cache.sync.public.port");
			
			String fileLockPath = SmartUXProperties.getProperty("SmartUXNotiDao.refreshNoti.fileLock");
			int waitTime = NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.wait"), 5);
			
			FileProcessLockFactory.start(fileLockPath, waitTime,  new FileProcessFunction("공지 즉시적용", ResultCode.ApplyRequestFail) {
				
				@Override
				public void run() throws Exception {
					
					//즉시적용 호출 시 sleep 값 추가 -> DB분리건으로 OGG가 10초 이상 걸리는 경우가 있어 즉시적용 시 반영이 안돼서 추가함
					Thread.sleep(Integer.parseInt(org.apache.commons.lang.StringUtils.defaultIfEmpty(SmartUXProperties.getProperty("apply.cache.call.sleep"), "10000")));
					
					RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
							host, 
							port, 
							SmartUXProperties.getProperty("SmartUXNotiDao.refreshNoti.url")+"?callByScheduler="+callByScheduler + "&bbs_id=" + bbs_id,
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.method"), "POST"), 
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"), 
							MediaType.APPLICATION_JSON.toString(),
							NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.timeout"), 60000)).getResponseBody());
					
					result.setFlag(codeMsg.getCode());
					result.setMessage(codeMsg.getMessage());
					cLog.middleLog("공지 즉시적용", "API 호출", codeMsg.getCode(), codeMsg.getMessage());
				}
			});
		} catch (MimsCommonException e) {
			cLog.warnLog("공지 즉시적용", "실패", e.getFlag(), e.getMessage(), e.getLocalizedMessage());
			result.setFlag(e.getFlag());
			result.setMessage(e.getMessage());
		} finally {
			cLog.endLog("공지 즉시적용", loginUser, bbs_id, result.getFlag(), result.getMessage());
		}
		
		String resultMsg = "{\"flag\" : \"" + result.getFlag() + "\", \"message\" : \"" + result.getMessage() + "\"}";
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(resultMsg).toString(), responseHeaders, HttpStatus.OK);
	}

	/**
	 * 목록화면에서 CJ 즉시적용 버튼을 클릭하여 캐쉬를 바로 적용할때 이용된다
	 * @param code	캐쉬를 바로 적용시킬 코드
	 * @return
	 */
	@RequestMapping(value="/admin/noti/applyCjCache", method=RequestMethod.POST)
	public ResponseEntity<String> applyCjCache(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value="callByScheduler", defaultValue="Y") final String callByScheduler
			//@RequestParam(value="code") String code
			) throws Exception {
		
		final CLog cLog = new CLog(applyLogger, request.getRemoteAddr(), request.getRequestURI(), request.getMethod());
		final String loginUser = callByScheduler.equals("Y") ? "batch" : CookieUtil.getCookieUserID(request);
		final CUDResult result = new CUDResult();
		
		try {
			cLog.startLog("CJ 공지 즉시적용 :", callByScheduler, loginUser);
			final String host = SmartUXProperties.getProperty("cache.sync.public.cj.host");
			final String port = SmartUXProperties.getProperty("cache.sync.public.cj.port");
			
			String fileLockPath = SmartUXProperties.getProperty("SmartUXNotiDao.refreshNoti.fileLock");
			int waitTime = NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.wait"), 5);
			
			FileProcessLockFactory.start(fileLockPath, waitTime,  new FileProcessFunction("CJ 공지 즉시적용", ResultCode.ApplyRequestFail) {
				
				@Override
				public void run() throws Exception {
					
					//즉시적용 호출 시 sleep 값 추가 -> DB분리건으로 OGG가 10초 이상 걸리는 경우가 있어 즉시적용 시 반영이 안돼서 추가함
					Thread.sleep(Integer.parseInt(org.apache.commons.lang.StringUtils.defaultIfEmpty(SmartUXProperties.getProperty("apply.cache.call.sleep"), "10000")));
					
					RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
							host, 
							port, 
							SmartUXProperties.getProperty("SmartUXNotiDao.refreshNoti.cj.url")+"?callByScheduler="+callByScheduler,
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.method"), "POST"), 
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"), 
							MediaType.APPLICATION_JSON.toString(),
							NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.timeout"), 60000)).getResponseBody());
					
					result.setFlag(codeMsg.getCode());
					result.setMessage(codeMsg.getMessage());
					cLog.middleLog("CJ 공지 즉시적용", "API 호출", codeMsg.getCode(), codeMsg.getMessage());
				}
			});
		} catch (MimsCommonException e) {
			cLog.warnLog("CJ 공지 즉시적용", "실패", e.getFlag(), e.getMessage(), e.getLocalizedMessage());
			result.setFlag(e.getFlag());
			result.setMessage(e.getMessage());
		} finally {
			cLog.endLog("CJ 공지 즉시적용", loginUser, result.getFlag(), result.getMessage());
		}
		
		String resultMsg = "{\"flag\" : \"" + result.getFlag() + "\", \"message\" : \"" + result.getMessage() + "\"}";
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(resultMsg).toString(), responseHeaders, HttpStatus.OK);
	}
	
	
	/**
	 * 
	 * 관리자 공지사항 리스트
	 ** @param findName		검색구분
	 * @param findValue		검색어	
	 * @param pageNum		페이지 번호
	 * @param bbs_id			게시판 ID
	 * @param bbs_gbn			게시판 구분
	 * @param scr_tp			screen type
	 * @param model			JSP 리턴 오브젝트
	 * return				공지사항  리스트 화면 URL
	 * 
	 */
	
	@RequestMapping(value="/admin/noti/getNotiList",method=RequestMethod.GET)
	public String  getNotiList(
			@RequestParam(value="findName", required=false, defaultValue="") String findName,
			@RequestParam(value="findValue", required=false, defaultValue="") String findValue,
			@RequestParam(value="pageNum", required=false, defaultValue="1") String pageNum,
			@RequestParam(value="bbs_id", required=false, defaultValue="") String bbs_id,
			@RequestParam(value="bbs_gbn", required=false, defaultValue="") String bbs_gbn,
			@RequestParam(value="scr_tp", required=false, defaultValue="") String scr_tp,
			Model model
			) throws Exception{	
		
		HTMLCleaner cleaner = new HTMLCleaner();
		findName 	= cleaner.clean(findName);
		findValue = cleaner.clean(findValue);
		pageNum = cleaner.clean(pageNum);
		
		logger.debug("getAdminList = findName : " + findName);
		logger.debug("getAdminList = findValue : " + findValue);
		logger.debug("getAdminList = pageNum : " + pageNum);
		logger.debug("getAdminList = bbs_id : " + bbs_id);
		logger.debug("getAdminList = bbs_gbn : " + bbs_gbn);
		logger.debug("getAdminList = scr_tp : " + scr_tp);
		
		NotiListVO notiListVO= new NotiListVO();
		
		notiListVO.setBbs_id(bbs_id);
		notiListVO.setBbs_gbn(bbs_gbn);
		
		//screen-type		
		notiListVO.setScr_tp(scr_tp);
		
		//페이지 
		notiListVO.setPageSize(GlobalCom.isNumber(notiListVO.getPageSize(),10));
		notiListVO.setBlockSize(GlobalCom.isNumber(notiListVO.getBlockSize(),10));
		notiListVO.setPageNum(GlobalCom.isNumber(Integer.parseInt(GlobalCom.isNull(pageNum, "1")),1));
		//검색 
		notiListVO.setFindName(GlobalCom.isNull(findName, "TITLE"));
		notiListVO.setFindValue(findValue);
		
		List<NotiVO> notiList=null;
		List<Map<String, String>> boardList=null;
		Integer isFixedCount=null;
		
		boardList=service.getBoardList(notiListVO);
		
		if(boardList==null || boardList.size()==0){
			model.addAttribute("is_none","Y");
			model.addAttribute("scr_tp",scr_tp);
			return "redirect:/admin/notimng/getNotiList.do";		
		}
		
		
		if(notiListVO.getBbs_id()==null || notiListVO.getBbs_id().equals("")){
			//가장 처음 것으로 출력
			bbs_id= boardList.get(0).get("BBS_ID");
			notiListVO.setBbs_id(bbs_id);
			
		}
		
		
		
		notiList=service.getNotiList(notiListVO);
		notiListVO.setPageCount(service.getNotiListCnt(notiListVO));
	    
		//상단 공지 가능여부(3개까지 가능)		
		isFixedCount=service.getIsFixedCount(bbs_id);
		String isFixedCheck="Y";
		if(isFixedCount>=3) isFixedCheck="N";
		
		logger.debug("isFixedCount==================>"+isFixedCount);
        
		model.addAttribute("boardList", boardList);
		model.addAttribute("notiList", notiList);
		model.addAttribute("notiListVO", notiListVO);
		model.addAttribute("isFixedCheck",isFixedCheck);		
		
		return "/admin/noti/notiList";		
	}
	
	
	/**
	 * 공지사항 등록 화면 이동
	 * @param findName		검색구분
	 * @param findValue		검색어	
	 * @param pageNum		페이지 번호
	 * @param bbs_id			게시판 ID
	 * @param bbs_gbn			게시판 구분
	 * @param scr_tp			screen type
	 * @param model			JSP 리턴 오브젝트 
	 *  return	공지사항 등록 화면 URL
	 */
	@RequestMapping(value="/admin/noti/getNotiView",method=RequestMethod.GET)
	public String getNotiView (
			@RequestParam(value="bbs_id", required=false, defaultValue="") String bbs_id,
			@RequestParam(value="bbs_gbn", required=false) String bbs_gbn,		
			@RequestParam(value="findName", required=false, defaultValue="") String findName,
			@RequestParam(value="findValue", required=false, defaultValue="") String findValue,
			@RequestParam(value="pageNum", required=false, defaultValue="1") String pageNum,
			@RequestParam(value="scr_tp" , required=false, defaultValue="T") String scr_tp,
			Model model
			) throws Exception{	
		
		HTMLCleaner cleaner = new HTMLCleaner();
		findName 	= cleaner.clean(findName);
		findValue = cleaner.clean(findValue);
		pageNum = cleaner.clean(pageNum);
		
		logger.debug("bbs_id : "+bbs_id);
		logger.debug("bbs_gbn : "+bbs_gbn);
		logger.debug("findName : "+findName);		
		logger.debug("findValue : "+findValue);
		logger.debug("pageNum : "+pageNum);
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:00",Locale.KOREA);
		GregorianCalendar Gcal=new GregorianCalendar();

		String statbbsListCnt = SmartUXProperties.getProperty("statbbs.sbox.listcnt");//지정 갯수만 선택가능 하도록(지난 이벤트 참여이력을 볼 필요가 없으므로)
		List<StatbbsMiniListVo> sboxListVo = stat_service.getStatbbsActive(statbbsListCnt);
		if("EV".equals(bbs_gbn)) {
			int imgSize = Integer.parseInt(GlobalCom.isNull(SmartUXProperties.getProperty("eventnoti.size")))/1024;	
			String imgFormat = GlobalCom.isNull(SmartUXProperties.getProperty("eventnoti.format"));
			model.addAttribute("imgSize", imgSize);
			model.addAttribute("imgFormat", imgFormat);
		}	
		String currentDate=sdf.format(Gcal.getTime());		
		Gcal.add(Calendar.DAY_OF_MONTH, 7);
		String nextWeekDate=sdf.format(Gcal.getTime());
		
		NotiListVO notiListVO=new NotiListVO();
		
		notiListVO.setBbs_id(bbs_id);
		notiListVO.setBbs_gbn(bbs_gbn);
		notiListVO.setFindName(findName);
		notiListVO.setFindValue(findValue);
		notiListVO.setPageNum(Integer.parseInt(pageNum));
		notiListVO.setScr_tp(scr_tp);
		
		//상단 공지 가능여부		
		Integer isFixedCount=service.getIsFixedCount(bbs_id);
		String isFixedCheck="Y";
		if(isFixedCount>=3) isFixedCheck="N";	
		
		List<CodeItemVO> netTypeList = codeservice.getCodeItemList("C0001");

		model.addAttribute("notiListVO", notiListVO);
		model.addAttribute("isUpdate",0);		
		model.addAttribute("isFixedCheck",isFixedCheck);
		model.addAttribute("currentDate",currentDate);
		model.addAttribute("nextWeekDate",nextWeekDate);
		model.addAttribute("sbox_vo", sboxListVo);
		model.addAttribute("netTypeList", netTypeList);
		model.addAttribute("isPermanent", "N");
		
		return "/admin/noti/notiForm";		
	}
	
	/**
	 * 공지사항 수정 화면  이동
	 * @param findName		검색구분
	 * @param findValue		검색어	
	 * @param pageNum		페이지 번호
	 * @param bbs_id			게시판 ID
	 * @param reg_no			게시글 ID
	 * @param bbs_gbn			게시판 구분
	 * @param scr_tp			screen type
	 * @param model			JSP 리턴 오브젝트 
	 *  return	공지사항 수정 화면 URL
	 *  @throws Exception
	 */
	
	@RequestMapping(value="/admin/noti/getNotiUpateView",method=RequestMethod.GET)
	public String getNotiUpateView(
			@RequestParam(value="bbs_id", required=false, defaultValue="") String bbs_id,
			@RequestParam(value="bbs_gbn", required=false) String bbs_gbn,
			@RequestParam(value="reg_no", required=false) String reg_no,
			@RequestParam(value="findName", required=false, defaultValue="") String findName,
			@RequestParam(value="findValue", required=false, defaultValue="") String findValue,
			@RequestParam(value="pageNum", required=false, defaultValue="1") String pageNum,	
			@RequestParam(value="scr_tp", required=false, defaultValue="1") String scr_tp,
			Model model
			) throws Exception{	
		
		HTMLCleaner cleaner = new HTMLCleaner();
		findName 	= cleaner.clean(findName);
		findValue = cleaner.clean(findValue);
		pageNum = cleaner.clean(pageNum);
		
		logger.debug("bbs_id : "+bbs_id);
		logger.debug("bbs_gbn : "+bbs_gbn);		
		logger.debug("reg_no : "+reg_no);
		logger.debug("findName : "+findName);		
		logger.debug("findValue : "+findValue);
		logger.debug("pageNum : "+pageNum);		
		logger.debug("scr_tp : "+scr_tp);	
		
		String rowGbn = "\\f\\^"; //컨텐츠 연동형 레코드 분리자
		String colsGbn= "\b\\^"; //컨텐츠 연동형 컬럼 분리자

		String statbbsListCnt = SmartUXProperties.getProperty("statbbs.sbox.listcnt");//지정 갯수만 선택가능 하도록(지난 이벤트 참여이력을 볼 필요가 없으므로)
		/*이미지 수정시 이미지 사이즈 체크_20200812*/
		if("EV".equals(bbs_gbn)) {
			int imgSize = Integer.parseInt(GlobalCom.isNull(SmartUXProperties.getProperty("eventnoti.size")))/1024;	
			String imgFormat = GlobalCom.isNull(SmartUXProperties.getProperty("eventnoti.format"));
			model.addAttribute("imgSize", imgSize);
			model.addAttribute("imgFormat", imgFormat);
		}
		List<StatbbsMiniListVo> sboxListVo = stat_service.getStatbbsActive(statbbsListCnt);
		
		NotiVO notiVO=null;
		NotiListVO notiListVO=new NotiListVO();
		List<Map<String, String>> notiTerminal=null;  				

		notiListVO.setBbs_id(bbs_id);			
		notiListVO.setBbs_gbn(bbs_gbn);			
		notiListVO.setFindName(findName);
		notiListVO.setFindValue(findValue);
		notiListVO.setPageNum(Integer.parseInt(pageNum));
		notiListVO.setScr_tp(scr_tp);

		notiVO =service.getNotiView(reg_no);
		notiTerminal=service.getNotiTerminal(reg_no); // 게시글 적용 단말 리스트
		
		//이미지 미리보기 URL(임시)
		notiVO.setSave_file_nm(notiVO.getSave_file_nm());

		//상단 공지 가능여부		
		Integer isFixedCount=service.getIsFixedCount(bbs_id);
		String isFixedCheck="Y"; // 상단 적용가능
		if(isFixedCount>=3) isFixedCheck="N";	//상단적용 불가능		
		
		ArrayList<String> choiceContent = new ArrayList<String>();
		if(notiVO.getEv_type()!=null && notiVO.getEv_type().equals("ev1")){
			
			String[] recCtn = notiVO.getEv_detail().split(rowGbn);
			int x = 0;
			for(String colCnt : recCtn){
				String[] arrCtn = colCnt.split(colsGbn);
				if(arrCtn.length==2){
					choiceContent.add(arrCtn[1]);
				} else if(arrCtn.length==3){
					choiceContent.add(arrCtn[1]);
				}
			}
		}
		
		List<CodeItemVO> netTypeList = codeservice.getCodeItemList("C0001");
		
		// 상시게시여부
		String isPermanent = "N";
		logger.debug("##################################"+notiVO.getMod_dt());
		if(!StringUtils.isEmpty(notiVO.getMod_dt())){
			Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(notiVO.getMod_dt());
			String year = new SimpleDateFormat("yyyy").format(date);
			logger.debug("##################################"+year);
			if(year.equals("9999")) { isPermanent = "Y"; }
		}
		
		model.addAttribute("reg_no",reg_no );
		model.addAttribute("isUpdate",1);		
		model.addAttribute("notiListVO", notiListVO);
		model.addAttribute("notiVO", notiVO);
		model.addAttribute("notiTerminal", notiTerminal);
		model.addAttribute("isFixedCheck", isFixedCheck);
		model.addAttribute("choiceContent", choiceContent);
		model.addAttribute("sbox_vo", sboxListVo);
		model.addAttribute("netTypeList", netTypeList);
		model.addAttribute("isPermanent", isPermanent);
		
		return "/admin/noti/notiForm";		
	}
	
	/**
	 * 
	 * 공지사항 등록
	 * @param notiVO     등록 정보
	 * @param uploadfile 등록 파일
	 * @param isUpdate  등록/수정 flag
	 * @param scr_tp     screen type
	 * @param request
	 * @param model      JSP 리턴 오브젝트 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/noti/createNoti",method=RequestMethod.POST)
	public String createNoti(
			NotiVO notiVO,
			@RequestParam(value="file", required=false, defaultValue="") MultipartFile uploadfile,
			@RequestParam(value="isUpdate", required=false) int isUpdate,			
			@RequestParam(value="scr_tp" , required=false, defaultValue="1") String scr_tp,
			@RequestParam(value="isPermanent", required=false, defaultValue="N") String isPermanent,
			@RequestParam(value="delete_img_flag", required=false, defaultValue="N") String delete_img_flag,
			HttpServletRequest request,
			Model model
			) throws Exception{				
		
		logger.debug("teminalList : "+notiVO.getTerminal_list());
		logger.debug("XML insert start");
		logger.debug("isUpdate : "+isUpdate);
		logger.debug("bbs_id : "+notiVO.getBbs_id());
		logger.debug("bbs_gbn : "+notiVO.getBbs_gbn());
		logger.debug("scr_tp : "+scr_tp);
		logger.debug("isUpdate : "+isUpdate);			
		logger.debug("isPermanent : "+isPermanent);
		
		// validation 작업에 통과하면 파일 업로드 작업을 진행한다
		
		String resultcode = "";
		String resultmessage = "";
		String tmp_file_name = "";
		String bg_img_file = "";
		File img_file = null;
		String oldSaveFilenm = notiVO.getSave_file_nm();
		
		if (uploadfile.getSize() != 0L) {
			tmp_file_name = uploadfile.getOriginalFilename();
			String ext = tmp_file_name.substring(	tmp_file_name.lastIndexOf(".") + 1,	tmp_file_name.length()); // 확장자 구하기
			ext = ext.toLowerCase();
			bg_img_file = Long.toString(System.currentTimeMillis()) + "."+ ext; // 시스템타임.확장자 구조로 한다
			String newFilename=SmartUXProperties.getProperty("noti.imgupload.dir")+ bg_img_file;

			logger.debug("tmp_file_name : " + tmp_file_name);
			logger.debug("ext :  " + ext);
			logger.debug("bg_img_file :  " + bg_img_file);
			logger.debug("newFilename :  " + newFilename);				
			
			//이미지 파일 업로드
			img_file = new File(newFilename);
			uploadfile.transferTo(img_file);
			
			notiVO.setOrg_file_nm(uploadfile.getOriginalFilename());
			notiVO.setSave_file_nm(bg_img_file);

		}else{
			
			String savefilenm = notiVO.getSave_file_nm();
			
			if(savefilenm != null  && !savefilenm.isEmpty() ){
				
				notiVO.setSave_file_nm(savefilenm.substring(savefilenm.lastIndexOf("/") +1));
			}
			
		}
		
		String cookieID = CookieUtil.getCookieUserID(request);
		
		// 상시게시여부
		if(isPermanent.equals("Y")){
			notiVO.setMod_dt("9999-12-31 00:00:00");
		}
		
		if(isUpdate==0){			
			
			//등록자 아이디			
			notiVO.setReg_id(cookieID);
			notiVO.setAct_id(cookieID);
			notiVO.setAct_gbn("I");
			notiVO.setAct_ip(request.getRemoteAddr());		
			notiVO.setIs_adt("0");
			service.createNoti(notiVO);			
		}else{			
			//업로드 파일 없고 AND 삭제버튼 클릭시
			if(uploadfile.getSize() == 0L && delete_img_flag.equals("Y")) {
				notiVO.setOrg_file_nm("");
				notiVO.setSave_file_nm("");
			}
			
			//수정자 아이디
			notiVO.setMod_id(cookieID);
			notiVO.setAct_id(cookieID);
			notiVO.setAct_gbn("U");
			notiVO.setAct_ip(request.getRemoteAddr());
			service.updateNoti(notiVO);
			
			//"업로드 이미지 수정시" OR "삭제버튼 클릭시" 기존 업로드된 파일 삭제
			if (uploadfile.getSize() != 0L || delete_img_flag.equals("Y")) {
				//기존에 업로드한 이미지가 있다면
				if(oldSaveFilenm != null && !oldSaveFilenm.isEmpty()) {
					//확장자 포함 파일명
					String deleteFileNm = oldSaveFilenm.substring(oldSaveFilenm.lastIndexOf("/")+1);				
					File file = new File(SmartUXProperties.getProperty("noti.imgupload.dir")+ deleteFileNm);	    	
					//이미지 파일 삭제
					if(file.exists()) 
						file.delete();
				}
			}
		}   

		
		model.addAttribute("bbs_gbn",notiVO.getBbs_gbn() );
		model.addAttribute("bbs_id",notiVO.getBbs_id() );		
		model.addAttribute("scr_tp",notiVO.getScr_tp());	
		
		return "redirect:/admin/noti/getNotiList.do";
	}	
	
	
	/**
	 * 
	 * 공지사항 삭제
	 * @param reg_no		게시글 ID 리스트
	 * @param bbs_id		게시판 ID 
	 * @param bbs_gbn		게시판 구분
	 * @param scr_tp 		screen type	
	 * @param pageNum	페이지 번호
 	 * @param model		JSP 리턴 오브젝트
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/noti/deleteNoti",method=RequestMethod.GET)
	public String deleteNoti(
			@RequestParam(value="reg_no", required=false) String reg_no_list,
			@RequestParam(value="bbs_id", required=false, defaultValue="") String bbs_id,
			@RequestParam(value="bbs_gbn", required=false) String bbs_gbn,
			@RequestParam(value="scr_tp", required=false, defaultValue="1") String scr_tp,
			@RequestParam(value="pageNum", required=false, defaultValue="1") String pageNum,
			HttpServletRequest request,
			Model model
			) throws Exception{	
		logger.debug("reg_no_list : "+reg_no_list);		
		logger.debug("bbs_id : "+bbs_id);
		logger.debug("bbs_gbn : "+bbs_gbn);
					
		String cookieID = CookieUtil.getCookieUserID(request);
		String ip=request.getRemoteAddr();
		service.deleteNoti(reg_no_list,cookieID, ip);			
		
		model.addAttribute("bbs_gbn",bbs_gbn );
		model.addAttribute("bbs_id",bbs_id );
		model.addAttribute("scr_tp",scr_tp );
		model.addAttribute("pageNum",pageNum );
		
		return "redirect:/admin/noti/getNotiList.do";		
	}
	
	/**
	 * 단말 정보 리스트	
	 * @param scr_tp		screen type
	 * @param model		JSP 리턴 오브젝트
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/noti/getTerm",method=RequestMethod.GET)
	public String getTerm(
			@RequestParam(value="scr_tp", required=false) String scr_tp,
			Model model) throws Exception{
		
		logger.debug("scr_tp : "+scr_tp);	
		
		List<Map<String, String>> termList=null;
		
		Map<String, String> delCheck= new HashMap<String, String>();
		delCheck.put("scr_tp", scr_tp);
		delCheck.put("del_yn", "0");
	
		termList=service.getTerm(delCheck);
		
		model.addAttribute("termList",termList );		
		model.addAttribute("isManageTerm","0");	
		
		return "/admin/noti/termViewList";
	}	
	
	/**
	 * 인터페이스와 관리자모드가 분리 되었어도 XML생성 파일은 NAS에 저장되기 때문에 그냥 관리자 모드에서 생성/수정 하면 된다.
	 * 팝업 설정/해지
	 * @param reg_no		게시글 번호
	 * @param bbs_id		게시판 ID
	 * @param is_fixed		팝업 설정 유무
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/noti/popUpSetting", method=RequestMethod.POST )
	public @ResponseBody  String popUpSetting(
			@RequestParam(value="reg_no", required=false, defaultValue="") String reg_no,
			@RequestParam(value="bbs_id", required=false, defaultValue="") String bbs_id,
			@RequestParam(value="scr_tp", required=false, defaultValue="T") String scr_tp,
			@RequestParam(value="is_fixed", required=false, defaultValue="") String is_fixed,
			HttpServletRequest request
			)  throws Exception{
		
		String resultcode = "";
		String resultmessage = "";
		String resultstate = "";
		
		String cookieID = CookieUtil.getCookieUserID(request);
		String ip=request.getRemoteAddr();
		
		try{
			
			service.popUpSetting(reg_no,bbs_id,is_fixed,cookieID,ip);
			
			if(is_fixed.equals("0")){
				resultstate="CANCLE";
				
				//긴급/팝업공지 용 데이터 처리
				notiPopVo notipopVo = new notiPopVo(scr_tp, "S", cookieID, ip);
				notipopService.callDeleteNotiPopProc(notipopVo, "PO");
			}else{
				NotiVO notiVo = service.getNotiView(reg_no);
				resultstate="SETTING";
				
				//캐시용 긴급/팝업공지 용 데이터 처리
				notiPopVo notipopVo = new notiPopVo(scr_tp, "S", notiVo.getBbs_id(), notiVo.getReg_no(), cookieID, ip);
				notipopService.callInsertNotiPopProc(notipopVo, "PO");
			}			
			
			resultcode = SmartUXProperties.getProperty("flag.success");
			resultmessage = SmartUXProperties.getProperty("message.success");
			
		}catch(Exception e){
			logger.error("popUpSetting : "+e.getClass().getName());
			logger.error("popUpSetting : "+e.getMessage());		
			
			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();			
		}
		
		return "{\"flag\" : \"" + resultcode + "\", \"message\" : \"" + resultmessage + "\",\"resultstate\" : \"" + resultstate + "\"}";
	}
	
	/**
	 * 
	 * 상단여부 설정/해지
	 * @param reg_no		게시글 ID
	 * @param is_fixed		상단 고정 여부
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/noti/updateIsFixed", method=RequestMethod.POST )
	public @ResponseBody  String updateIsFixed(
			@RequestParam(value="reg_no", required=false, defaultValue="") String reg_no,			
			@RequestParam(value="is_fixed", required=false, defaultValue="") String is_fixed,
			HttpServletRequest request
			) throws Exception{
		
		logger.debug("reg_no : "+reg_no);		
		logger.debug("is_fixed : "+is_fixed);
		
		NotiVO notiVO=new NotiVO();
		notiVO.setReg_no(reg_no);
		notiVO.setIs_fixed(Integer.parseInt(is_fixed));

		String resultcode = "";
		String resultmessage = "";
		String resultstate = "";
		
		String cookieID = CookieUtil.getCookieUserID(request);		
		String ip=request.getRemoteAddr();
		
		try{
				service.updateIsFixed(notiVO,cookieID,ip );				
				if(is_fixed.equals("0")){
					resultstate = "CANCLE";
				}else{
					resultstate = "SETTING";			
				}
				resultcode = SmartUXProperties.getProperty("flag.success");
				resultmessage = SmartUXProperties.getProperty("message.success");
		}catch(Exception e){
			logger.error("updateIsFixed : "+e.getClass().getName());
			logger.error("updateIsFixed : "+e.getMessage());		
			
			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}
		
		return "{\"flag\" : \"" + resultcode + "\", \"message\" : \"" + resultmessage + "\",\"resultstate\" : \"" + resultstate + "\"}";
	}
	

	/**
	 * 검수 확인
	 * @param reg_no		게시글 ID
	 * @param is_adt		검수 여부
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/noti/updateConfirmAdt", method=RequestMethod.POST )
	public @ResponseBody  String updateConfirmAdt(
			@RequestParam(value="reg_no", required=false, defaultValue="") String reg_no,			
			@RequestParam(value="is_adt", required=false, defaultValue="") String is_adt,
			@RequestParam(value="bbs_id", required=false, defaultValue="") String bbs_id,
			@RequestParam(value="scr_tp", required=false, defaultValue="T") String scr_tp,
			@RequestParam(value="bbs_gbn", required=false, defaultValue="") String bbs_gbn,
			HttpServletRequest request
			
			) throws Exception{
		
		logger.debug("reg_no : "+reg_no);		
		logger.debug("bbs_id : "+bbs_id);
		logger.debug("is_adt : "+is_adt);
		
		String resultcode = "";
		String resultmessage = "";		
		String resultstate = "SETTING";
		
		String cookieID = CookieUtil.getCookieUserID(request);		
		String ip=request.getRemoteAddr();
		try {		
			
			if("PU".equals(bbs_gbn)){
				
				if(is_adt.equals("0")){ //해제 일 경우
					resultstate="CANCLE";

					//긴급/팝업공지 용 데이터 처리
					notiPopVo notipopVo = new notiPopVo(scr_tp, "T", cookieID, ip);
					notipopService.callDeleteNotiPopProc(notipopVo, "PO");
				}else{					//설정 일 경우
					
					NotiVO notiVo = service.getNotiView(reg_no);
					resultstate = "SETTING";	

					//캐시용 긴급/팝업공지 용 데이터 처리
					notiPopVo notipopVo = new notiPopVo(scr_tp, "T", notiVo.getBbs_id(), notiVo.getReg_no(), cookieID, ip);
					notipopService.callInsertNotiPopProc(notipopVo, "PO");
				}
			}
			
			// 공지 검수 설정/해제 처리...
			service.updateConfirmAdt(reg_no,is_adt, bbs_id,cookieID ,ip, bbs_gbn);
			
			
			resultcode = SmartUXProperties.getProperty("flag.success");
			resultmessage = SmartUXProperties.getProperty("message.success");
			
		} catch (Exception e) {
			logger.error("updateConfirmAdt : "+e.getClass().getName());
			logger.error("updateConfirmAdt : "+e.getMessage());	
			
			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}
				
		return "{\"flag\" : \"" + resultcode + "\", \"message\" : \"" + resultmessage + "\",\"resultstate\" : \"" + resultstate + "\"}";
	}
	
	
	/**
	 * 
	 * 단말 정보 관리 리스트 	
	 * @param scr_tp		screen type
	 * @param del_yn		삭제 여부
	 * @param model		JSP 리턴 오브젝트
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/noti/getManageTerm",method=RequestMethod.GET)
	public String getManageTerm(
			@RequestParam(value="scr_tp", required=false) String scr_tp,
			@RequestParam(value="del_yn", required=false, defaultValue="0") String del_yn,
			Model model
			) throws Exception{
		
		logger.debug("scr_tp : "+scr_tp);
		logger.debug("del_yn : "+del_yn);
		
		List<Map<String, String>> termList=null;
		Map<String, String> delCheck= new HashMap<String, String>();
		delCheck.put("scr_tp", scr_tp);
		delCheck.put("del_yn", del_yn);
		
		termList=service.getTerm(delCheck);
		
		model.addAttribute("termList",termList );	
		model.addAttribute("isManageTerm","1");		
		model.addAttribute("delCheck",delCheck);
		
		return "/admin/noti/termViewManageList";
	}	
	
	/**
	 * 단말기 등록
	 * @param term_model		단말기 모델명
	 * @param term_make			단말기 제조사명
	 * @param lcd_spec			단말기 LCD 크기
	 * @param scr_tp				screen type
	 * @param model				JSP 리턴 오브젝트
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/noti/createTermManage", method=RequestMethod.POST )
	public  String createTermManage(
			@RequestParam(value="term_model", required=false, defaultValue="") String term_model,			
			@RequestParam(value="term_make", required=false, defaultValue="") String term_make,
			@RequestParam(value="lcd_spec", required=false, defaultValue="") String lcd_spec,
			@RequestParam(value="scr_tp", required=false, defaultValue="T") String scr_tp,
			Model model,
			HttpServletRequest request
			) throws Exception{
		
		logger.debug("term_model : "+term_model);		
		logger.debug("term_make : "+term_make);
		logger.debug("lcd_spec : "+lcd_spec);
		logger.debug("scr_tp : "+scr_tp);
		String cookieId = CookieUtil.getCookieUserID(request);
		String ip = request.getRemoteAddr();
		
		Map<String, String> term =new HashMap<String, String>();
		term.put("term_model", term_model);
		term.put("term_make", term_make);
		term.put("lcd_spec", lcd_spec);
		term.put("scr_tp", scr_tp);
	    
		service.createTermManage(term,cookieId,ip);
		
		model.addAttribute("scr_tp", scr_tp);
		
		return "redirect:/admin/noti/getManageTerm.do";
	}
	/**
	 *  단말기 삭제/미삭제
	 * @param modelList		단말 모델 리스트
	 * @param del_yn			삭제 여부
	 * @param model			JSP 리턴 오브젝트
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/noti/deleteTermManage", method=RequestMethod.POST )
	public  @ResponseBody String deleteTermManage(
			@RequestParam(value="modelList", required=false, defaultValue="") String modelList,
			@RequestParam(value="del_yn", required=false) String del_yn,			
			@RequestParam(value="scr_tp", required=false, defaultValue="T") String scr_tp,			
			HttpServletRequest request
			)  throws Exception{		
		logger.debug("modelList : "+modelList);
		logger.debug("del_yn : "+del_yn);

		String resultcode = "";
		String resultmessage = "";	
		String ip = request.getRemoteAddr();
		String id = CookieUtil.getCookieUserID(request);
		try {			
			service.deleteTermManage(modelList,del_yn,id,ip,scr_tp);
			
			resultcode = SmartUXProperties.getProperty("flag.success");
			resultmessage = SmartUXProperties.getProperty("message.success");			
			
		} catch (Exception e) {
			logger.error("deleteTermManage : "+e.getClass().getName());
			logger.error("deleteTermManage : "+e.getMessage());	
			
			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}				
		
		return "{\"flag\" : \"" + resultcode + "\", \"message\" : \"" + resultmessage + "\"}";		
	}
	

	/*@RequestMapping(value="/admin/noti/deleteImageFile", method=RequestMethod.POST )
	public  @ResponseBody String deleteImageFile(
			@RequestParam(value="reg_no", required=false, defaultValue="") String reg_no	
			)  throws Exception{		
		
		logger.debug("reg_no : "+reg_no);		

		String resultcode = "";
		String resultmessage = "";	
		try {			
			service.deleteImageFile(reg_no);
			
			resultcode = SmartUXProperties.getProperty("flag.success");
			resultmessage = SmartUXProperties.getProperty("message.success");			
			
		} catch (Exception e) {
			logger.error("deleteTermManage : "+e.getClass().getName());
			logger.error("deleteTermManage : "+e.getMessage());	
			
			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}				
		return "{\"flag\" : \"" + resultcode + "\", \"message\" : \"" + resultmessage + "\"}";		
	}*/
	
	/**
	 * 
	 * 단말모델 등록여부 확인
	 * @param term_model	단말모델	 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/noti/isExistModel", method=RequestMethod.POST )
	public @ResponseBody  String getIsExistModel(
			@RequestParam(value="term_model", required=true, defaultValue="") String term_model,
			@RequestParam(value="scr_tp", required=false, defaultValue="T") String scr_tp,	
			HttpServletRequest request
			) throws Exception{
		
		logger.debug("term_model : "+term_model);	

		String resultcode = "";
		String resultmessage = "";
		String resultstate = "";
		
		try{
				
			resultstate = String.valueOf( service.getIsExistModel(term_model.trim(), scr_tp.trim()) );
				
			resultcode = SmartUXProperties.getProperty("flag.success");
			resultmessage = SmartUXProperties.getProperty("message.success");
				
		}catch(Exception e){
			logger.error("getIsExistModel : "+e.getClass().getName());
			logger.error("getIsExistModel : "+e.getMessage());		
			
			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}
		
		return "{\"flag\" : \"" + resultcode + "\", \"message\" : \"" + resultmessage + "\",\"resultstate\" : \"" + resultstate + "\"}";
	}

	/**
	 * 게시판 글을 팝업 게시판으로 복사하는 기능
	 * @param reg_no
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/noti/copyNotiPopup", method=RequestMethod.POST )
	public ResponseEntity<String> copyNotiPopup(
			@RequestParam(value="reg_no", required=true, defaultValue="") String reg_no,
			@RequestParam(value="scr_tp", required=true, defaultValue="T") String scr_tp,
			HttpServletRequest request
			) throws Exception{

		String result = "";
		String resultcode = "";
		String resultmessage = "";
		String resultstate = "";
		
		String cookieID = CookieUtil.getCookieUserID(request);		
		String ip=request.getRemoteAddr();
		
		try{
			String pop_id = service.getNotiPopupId(scr_tp);
			
			CopyNotiPopupVo copynotipopupVo = new CopyNotiPopupVo();
			copynotipopupVo.setCp_reg_no(reg_no);
			copynotipopupVo.setBbs_id(pop_id);
			copynotipopupVo.setReg_id(cookieID);
			service.copyNotiPopup(copynotipopupVo);

			resultcode = SmartUXProperties.getProperty("flag.success");
			resultmessage = SmartUXProperties.getProperty("message.success");	
		}catch(Exception e){
			logger.error("copyNotiPopup : "+e.getClass().getName());
			logger.error("copyNotiPopup : "+e.getMessage());		
			
			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = handler.getFlag();
			resultmessage = handler.getMessage();
		}

		result = "{\"result\":{\"message\":\""+resultmessage+"\",\"flag\":\""+resultcode+"\"}}";
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(result).toString(), responseHeaders, HttpStatus.CREATED);
	}
	
	/**
	 * 
	 * 팝업 공지사항 리스트
	 ** @param findName		검색구분
	 * @param findValue		검색어	
	 * @param pageNum		페이지 번호
	 * @param bbs_id			게시판 ID
	 * @param bbs_gbn			게시판 구분
	 * @param scr_tp			screen type
	 * @param model			JSP 리턴 오브젝트
	 * return				공지사항  리스트 화면 URL
	 * 
	 */
	@RequestMapping(value="/admin/noti/getNotiPopList",method=RequestMethod.GET)
	public String  getNotiPopList(
			@RequestParam(value="findName", required=false, defaultValue="") String findName,
			@RequestParam(value="findValue", required=false, defaultValue="") String findValue,
			@RequestParam(value="pageNum", required=false, defaultValue="1") String pageNum,
			@RequestParam(value="bbs_id", required=false, defaultValue="") String bbs_id,
			@RequestParam(value="bbs_gbn", required=false, defaultValue="") String bbs_gbn,
			@RequestParam(value="scr_tp", required=false, defaultValue="") String scr_tp,
			@RequestParam(value="hiddenName", required=false, defaultValue="") String hiddenName,
			@RequestParam(value="textName", required=false, defaultValue="") String textName,
			@RequestParam(value="textHtml", required=false, defaultValue="") String textHtml,
			@RequestParam(value="delimiter", required=false, defaultValue="") String delimiter,
			Model model
			) throws Exception{	
		
		HTMLCleaner cleaner = new HTMLCleaner();
		findName 	= cleaner.clean(findName);
		findValue = cleaner.clean(findValue);
		pageNum = cleaner.clean(pageNum);
		textHtml = cleaner.clean(textHtml);
		delimiter = cleaner.clean(delimiter);

		String s_delimiter = delimiter;
		if(delimiter==null || "".equals(delimiter)){
			delimiter="|";
		}else if("comma".equals(delimiter)){
			delimiter=",";
			s_delimiter = "comma";
		}
		
		NotiListVO notiListVO= new NotiListVO();
		
		notiListVO.setBbs_id(bbs_id);
		notiListVO.setBbs_gbn(bbs_gbn);
		
		//screen-type		
		notiListVO.setScr_tp(scr_tp);
		
		//페이지 
		notiListVO.setPageSize(GlobalCom.isNumber(notiListVO.getPageSize(),10));
		notiListVO.setBlockSize(GlobalCom.isNumber(notiListVO.getBlockSize(),10));
		notiListVO.setPageNum(GlobalCom.isNumber(Integer.parseInt(GlobalCom.isNull(pageNum, "1")),1));
		//검색 
		notiListVO.setFindName(GlobalCom.isNull(findName, "TITLE"));
		notiListVO.setFindValue(findValue);
		
		List<NotiPopVo> notiList=null;
		
		
		notiList=service.getNotiPopList(notiListVO);
		notiListVO.setPageCount(service.getNotiPopListCnt(notiListVO));
		
		model.addAttribute("list", notiList);
		model.addAttribute("vo", notiListVO);
		model.addAttribute("hiddenName", hiddenName);
		model.addAttribute("textName", textName);
		model.addAttribute("textHtml", textHtml);
		model.addAttribute("delimiter", delimiter);
		model.addAttribute("s_delimiter", s_delimiter);
		
		return "/admin/noti/notiPopList";		
	}
	
	
}