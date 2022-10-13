package com.dmi.smartux.gpack.promotion.controller;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dmi.smartux.authentication.vo.AuthenticationCommon;
import com.dmi.smartux.common.exception.ExceptionHandler;
import com.dmi.smartux.common.exception.SmartUXException;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.CharacterSet;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.vo.Result;
import com.dmi.smartux.gpack.promotion.service.GpackPromotionService;
import com.dmi.smartux.gpack.promotion.vo.GpackPromotionInfoVO;
import com.dmi.smartux.gpack.promotion.vo.GpackPromotionResult;
import com.dmi.smartux.mainpanel.service.MainPanelService;
import com.dmi.smartux.mainpanel.vo.AlbumInfoVO;
import com.dmi.smartux.mainpanel.vo.MainPanelInfoVO;
import com.dmi.smartux.mainpanel.vo.MainPanelResult;
import com.dmi.smartux.mainpanel.vo.MainPanelVersionInfoVO;

@Controller
public class GpackPromotionController {

	private final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	GpackPromotionService service;
	
	@org.springframework.web.bind.annotation.ExceptionHandler(SmartUXException.class)
	//@ResponseStatus(value=HttpStatus.NOT_FOUND)
	public @ResponseBody String handleException(SmartUXException e, HttpServletRequest request,HttpServletResponse response) throws SmartUXException {
		//System.out.println("TEST = "+request.getHeader("accept"));
		//System.out.println("Handler : " + e.getMessage());
		
		//HttpHeaders rh = new HttpHeaders();
		//rh.add("Content-Type", "text/html; charset=UTF-8");
		if(request.getHeader("accept") == null ){
			response.setHeader("content-type", "text/plain;charset=UTF-8");
			StringBuffer sb = new StringBuffer();
			sb.append(e.getFlag());
			sb.append(GlobalCom.colsep);
			sb.append(CharacterSet.toKorean(e.getMessage()));
			String result;
			result = sb.toString();
			return result;
		}else{
			if(request.getHeader("accept").indexOf("application/json") != -1){
				response.setHeader("content-type", "application/json;charset=UTF-8");
				String result = "{\"error\":{\"code\":\""+e.getFlag()+"\",\"message\":\""+CharacterSet.toKorean(e.getMessage())+"\"}}";
				return result;
			}else if(request.getHeader("accept").indexOf("application/xml") != -1){
				response.setHeader("content-type", "application/xml;charset=UTF-8");
				String result;
				result = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><error><code>"+e.getFlag()+"</code><message>"+CharacterSet.toKorean(e.getMessage())+"</message></error>";
				return result;
			}else{
				response.setHeader("content-type", "text/plain;charset=UTF-8");
				StringBuffer sb = new StringBuffer();
				sb.append(e.getFlag());
				sb.append(GlobalCom.colsep);
				sb.append(CharacterSet.toKorean(e.getMessage()));
				String result;
				result = sb.toString();
				return result;
			}
		}
	}
	
	/**
	 * 프로모션 정보 조회
	 * @param request			HttpServletRequest 객체
	 * @param response			HttpServletResponse 객체
	 * @param sa_id				가입번호
	 * @param stb_mac			맥주소
	 * @param app_type			어플타입
	 * @param pack_id			팩ID
	 * @param promotion_id		프로모션ID
	 * @param start_num			검색 시작 인덱스
	 * 								-1  : req_count 값을 무시하고 전체를 검색
	 * 								0 : req_count 값은 무시하며 레코드의 총갯수의 정보만 전달
	 * 								양수 : req_count 값을 확인하여 페이징 된 레코드만큼 정보 전달
	 * @param req_count			검색 레코드 갯수
	 * @param p_start_num		프로모션 시작 NUMBER	
	 * @param p_req_count		가져올 프로모션 개수(행의 개수)
	 * @param net_typ			망타입
	 * 								02: 광랜 
	 * 								01: HFC-100M 
	 * 								31: HFC-10M
	 * @param callByScheduler	단말에서 호출 했을 경우엔 N, 스케쥴러에서 호출했을땐 Y, 관리자툴에서 조회했을땐 A가 들어간다
	 * @return
	 */
	@RequestMapping(value="/gpack/getPackPromotion")
	public GpackPromotionResult API_getPackPromotion(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value="sa_id", required=false) String sa_id,
			@RequestParam(value="stb_mac", required=false) String stb_mac,
			@RequestParam(value="app_type", required=false) String app_type,
			@RequestParam(value="pack_id", required=false) String pack_id,
			@RequestParam(value="promotion_id", required=false) String promotion_id,
			@RequestParam(value="start_num", required=false) String start_num,
			@RequestParam(value="req_count", required=false) String req_count,
			@RequestParam(value="p_start_num", required=false) String p_start_num,
			@RequestParam(value="p_req_count", required=false) String p_req_count,
			@RequestParam(value="net_typ", required=false) String net_typ,
			@RequestParam(value="callByScheduler", required=false, defaultValue="N") String callByScheduler
			){
		
		//#########[LOG START]#########
		Log log_logger = LogFactory.getLog("getPackPromotion");
		long startTime = System.currentTimeMillis();
		String log_ip = request.getRemoteAddr();
		String log_url = request.getRequestURI();
		Enumeration log_penum = request.getParameterNames();
		String log_key = null;
		String log_value = null;
		String log_info_mac = "";
		String log_info_id = "";
		String log_info_app_type = "";
		String log_debug = "";

		while(log_penum.hasMoreElements()){
			log_key = (String)log_penum.nextElement();
			log_value = (new String(request.getParameter(log_key)) == null) ? "" : new String(request.getParameter(log_key));
			
			if(log_key.equalsIgnoreCase("stb_mac")){
				log_info_mac = log_value;
			}
			if(log_key.equalsIgnoreCase("sa_id")){
				log_info_id = log_value;
			}
			if(log_key.equalsIgnoreCase("app_type")){
				log_info_app_type = log_value;
			}
			log_debug += " ["+log_key+"="+log_value+"]";
		}

		log_logger.info("["+log_ip+"] ["+log_url+"][START] - ["+log_info_mac+"]["+log_info_id+"]["+log_info_app_type+"]");
		log_logger.debug("["+log_ip+"] ["+log_url+"][START] -"+log_debug);
		
		GpackPromotionResult result = new GpackPromotionResult();
		result.setRecordset(null);
		
		try{
			//망타입이 01,02,31.. 으로 넘어오는데 만약 A,B 등과 같은 형태로 넘어오면 잘못된 형식이다.
			int fh_gbn = 9999;
			try{
				int tmpGbn = Integer.parseInt(net_typ);
				if(tmpGbn==1 || tmpGbn==2){//fullHD 서비스 하는 망
					fh_gbn = 1;
				}else{
					fh_gbn = 2;
				}
			}catch(Exception e){
				net_typ = "FAIL";
			}
			
			// 단말에서 호출한 경우(API 호출)
			if ("N".equals(callByScheduler)) {
				validateGetPackPromotion(sa_id, stb_mac, app_type, pack_id, promotion_id, start_num, req_count, p_start_num, p_req_count, net_typ);

				// 검색된 총 갯수 셋팅
				int totalCount = service.getPackPromotionCount(sa_id, stb_mac, app_type, pack_id, promotion_id, start_num, req_count, p_start_num, p_req_count, callByScheduler);
				result.setTotal_count(totalCount);
				List<GpackPromotionInfoVO> list = service.getPackPromotion(sa_id, stb_mac, app_type, pack_id, promotion_id, start_num, req_count, p_start_num, p_req_count, fh_gbn, callByScheduler);
				result.setFlag(SmartUXProperties.getProperty("flag.success"));
				result.setMessage(SmartUXProperties.getProperty("message.success"));
				result.setRecordset(list);
			}
			// 어드민에서 호출한 경우
			else if ("A".equals(callByScheduler)) {
				List<GpackPromotionInfoVO> list = service.getPackPromotion("", "", "", pack_id, "", "", "", "", "", fh_gbn, callByScheduler);
				result.setFlag(SmartUXProperties.getProperty("flag.success"));
				result.setMessage(SmartUXProperties.getProperty("message.success"));
				result.setRecordset(list);
				//response.setStatus(201);
			}
			// 스케줄러가 호출한 경우(조회 결과는 셋팅하지 않아도 된다(작업 성공 여부만 셋팅))
			else {
				List<GpackPromotionInfoVO> list = service.getPackPromotion("", "", "", pack_id, "", "", "", "", "", fh_gbn, callByScheduler);
				result.setFlag(SmartUXProperties.getProperty("flag.success"));
				result.setMessage(SmartUXProperties.getProperty("message.success"));
				result.setRecordset(list);
				//response.setStatus(201);
			}
			
		}catch(Exception e){
			log_logger.error("["+log_ip+"] ["+log_url+"]["+log_info_mac+"]["+log_info_id+"]["+log_info_app_type+"]"+"[getPackPromotion]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			ExceptionHandler handler = new ExceptionHandler(e);
			result.setFlag(handler.getFlag());
			result.setMessage(handler.getMessage());
		}
		
		//#########[LOG END]#########
		long endTime = System.currentTimeMillis();
		String timeStr = Long.toString((endTime-startTime));
		if(timeStr.length() > 3){
			timeStr = timeStr.substring(0,timeStr.length()-3)+"."+timeStr.substring(timeStr.length()-3,timeStr.length());
		}else{
			for(int i=timeStr.length();i<3;i++){
				timeStr = "0"+timeStr;	
			}
			timeStr = "0."+timeStr;
		}
		log_logger.info("["+log_ip+"] ["+log_url+"][END]["+(timeStr)+" sec] - ["+log_info_mac+"]["+log_info_id+"]["+log_info_app_type+"] ["+result.getFlag()+"]");

		return result;
	}
	
	/**
	 * validateGetPackPromotion 함수에서 사용하는 validation 함수
	 * @param sa_id				가입번호
	 * @param stb_mac			맥주소
	 * @param app_type			어플타입
	 * @param pack_id			팩ID
	 * @param promotion_id		프로모션ID
	 * @param start_num			검색 시작 인덱스
	 * @param req_count			검색 레코드 갯수
	 * @param p_start_num		프로모션 시작 NUMBER	
	 * @param p_req_count		가져올 프로모션 개수(행의 개수)
	 * @param net_typ			망타입
	 * @throws SmartUXException validate에 문제가 발생했을 경우 코드와 메시지가 셋팅되서 던져지는 SmartUXException 객체
	 */
	private void validateGetPackPromotion(String sa_id, String stb_mac, String app_type, String pack_id
			, String promotion_id, String start_num, String req_count, String p_start_num, String p_req_count, String net_typ) throws SmartUXException {
		SmartUXException exception = new SmartUXException();
		
		// 가입 번호 파라미터 존재 유무
		if(!(StringUtils.hasText(sa_id))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "sa_id"));
			throw exception;
		}
		
		// 맥주소 파라미터 존재 유무
		
		if(!(StringUtils.hasText(stb_mac))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "stb_mac"));
			throw exception;
		}
		
		// 어플 타입 파라미터 존재 유무
		
		if(!(StringUtils.hasText(app_type))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "app_type"));
			throw exception;
		}
//		130703 타입모두 허용
//		else{
//			// 어플 타입 파라미터 값이 SETTOP인지 확인
//			/*
//			if(!("UX".equals(app_type))){
//				exception.setFlag(SmartUXProperties.getProperty("flag.mismatchvalue"));
//				exception.setMessage(SmartUXProperties.getProperty("message.mismatchvalue", "app_type", "UX"));
//				throw exception;
//			}
//			*/
//			if(!("UX".equals(app_type)) && !("SMA".equals(app_type))){
//				exception.setFlag(SmartUXProperties.getProperty("flag.mismatchvalue"));
//				exception.setMessage(SmartUXProperties.getProperty("message.mismatchvalue", "app_type", "UX 이거나 SMA"));
//				throw exception;
//			}
//		}
		
		// 팩ID
		
		if(!(StringUtils.hasText(pack_id))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "pack_id"));
			throw exception;
		}
		
		// 프로모션ID
		
		if(!(StringUtils.hasText(promotion_id))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "promotion_id"));
			throw exception;
		}
		
		//망타입이 잘못넘어 왔을 경우
		if("FAIL".equals(net_typ)){
			exception.setFlag(SmartUXProperties.getProperty("flag.format"));
			exception.setMessage(SmartUXProperties.getProperty("message.format", "net_typ"));
			throw exception;
		}
		
		// 검색 시작 인덱스가 있을 경우
		if(StringUtils.hasText(start_num)){
			
			int intStartnum = 0;
			// 검색 시작 인덱스가 숫자형인지 확인
			try{
				intStartnum = Integer.parseInt(start_num);
			}catch(NumberFormatException e){
				exception.setFlag(SmartUXProperties.getProperty("flag.numberformat"));
				exception.setMessage(SmartUXProperties.getProperty("message.numberformat" , "start_num"));
				throw exception;
			}
			
			// 검색 시작 인덱스가 -1 이상인 경우
			if(intStartnum >= -1){
				if(intStartnum == -1){			// 검색 시작 인덱스가 -1인 경우(전체 조회)
					
				}else if(intStartnum == 0){		// 검색 시작 인덱스가 0인 경우(검색된 레코드 총갯수만 return 하는 경우) 
					
				}else{							// 검색 시작 인덱스가 1 이상인 경우(페이징)
					if(!(StringUtils.hasText(req_count))){			// req_count 파라미터가 없으면
						exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
						exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "req_count")); 
						throw exception;
					}
					
					int intReqcount = 0;
					
					try{
						intReqcount = Integer.parseInt(req_count);	// req_count 숫자형인지 확인
					}catch(NumberFormatException e){
						exception.setFlag(SmartUXProperties.getProperty("flag.numberformat"));
						exception.setMessage(SmartUXProperties.getProperty("message.numberformat", "req_count"));
						throw exception;
					}
					
					if(intReqcount <= 0){							// req_count 파라미터 값이 0이하이면(req_count 파라미터는 1 이상 값이어야 한다)
						exception.setFlag(SmartUXProperties.getProperty("flag.range2"));
						exception.setMessage(SmartUXProperties.getProperty("message.range2", "req_count", "1"));
						throw exception;
					}
				}
			}else{	// 검색 시작 인덱스가 -1 미만인 경우
				exception.setFlag(SmartUXProperties.getProperty("flag.range2"));
				exception.setMessage(SmartUXProperties.getProperty("message.range2", "start_num", "-1"));
				throw exception;
			}
		}else{	// 검색 시작 인덱스가 없을 경우
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "start_num"));
			throw exception;
		}
		
		// 프로모션ID가 "0"일 경우에는 p_start_num과 p_req_count가 필수
		if("0".equals(promotion_id)) {
			
			if(StringUtils.hasText(p_start_num)){
				
				int intStartnum = 0;
				// 프로모션 시작 NUMBER가 숫자형인지 확인
				try{
					intStartnum = Integer.parseInt(p_start_num);
				}catch(NumberFormatException e){
					exception.setFlag(SmartUXProperties.getProperty("flag.numberformat"));
					exception.setMessage(SmartUXProperties.getProperty("message.numberformat" , "p_start_num"));
					throw exception;
				}
				
				// 프로모션 시작 NUMBER가 1 이상인 경우
				if(intStartnum >= 1){
					
					//가져올 프로모션 개수
					if(!(StringUtils.hasText(p_req_count))){			// p_req_count 파라미터가 없으면
						exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
						exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "p_req_count")); 
						throw exception;
					}
					
					int intReqcount = 0;
					
					try{
						intReqcount = Integer.parseInt(p_req_count);	// p_req_count 숫자형인지 확인
					}catch(NumberFormatException e){
						exception.setFlag(SmartUXProperties.getProperty("flag.numberformat"));
						exception.setMessage(SmartUXProperties.getProperty("message.numberformat", "p_req_count"));
						throw exception;
					}
					
					if(intReqcount <= 0){							// p_req_count 파라미터 값이 0이하이면(p_req_count 파라미터는 1 이상 값이어야 한다)
						exception.setFlag(SmartUXProperties.getProperty("flag.range2"));
						exception.setMessage(SmartUXProperties.getProperty("message.range2", "p_req_count", "1"));
						throw exception;
					}
						
				}else{	// 검색 시작 인덱스가 1미만인 경우
					exception.setFlag(SmartUXProperties.getProperty("flag.range2"));
					exception.setMessage(SmartUXProperties.getProperty("message.range2", "p_start_num", "1"));
					throw exception;
				}
			}else{	// 검색 시작 인덱스가 없을 경우
				exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
				exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "p_start_num"));
				throw exception;
			}
			
		}
	}
}
