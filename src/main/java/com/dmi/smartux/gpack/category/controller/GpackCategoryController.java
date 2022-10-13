package com.dmi.smartux.gpack.category.controller;

import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dmi.smartux.admin.gpack.pack.service.GPackPackViewService;
import com.dmi.smartux.common.exception.ExceptionHandler;
import com.dmi.smartux.common.exception.SmartUXException;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.CharacterSet;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.vo.Result;
import com.dmi.smartux.gpack.category.service.GpackCategoryInfoService;
import com.dmi.smartux.gpack.category.vo.GpackCategoryAlbumInfoVO;
import com.dmi.smartux.gpack.category.vo.GpackCategoryInfoVO;
import com.dmi.smartux.gpack.category.vo.GpackCategoryResult;

@Controller
public class GpackCategoryController {

	private final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	GpackCategoryInfoService service;
	
	/** 팩(템플릿) 서비스 */
	@Autowired
	GPackPackViewService packViewService;
	
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
	 * 카테고리 정보 조회	
	 * @param request
	 * @param response
	 * @param sa_id
	 * @param stb_mac
	 * @param pack_id
	 * @param callByScheduler
	 * @return
	 */
	@RequestMapping(value="/gpack/getPackCategory")
	public GpackCategoryResult API_getPackCategory(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value="sa_id", required=false) String sa_id,
			@RequestParam(value="stb_mac", required=false) String stb_mac,
			@RequestParam(value="app_type", required=false) String app_type,
			@RequestParam(value="pack_id", required=false) String pack_id,
			@RequestParam(value="pcategory_id", required=false) String pcategory_id,
			@RequestParam(value="start_num", required=false, defaultValue="") String start_num,
			@RequestParam(value="req_count", required=false, defaultValue="") String req_count,
			@RequestParam(value="net_typ",required=false, defaultValue="2") String net_typ,
			@RequestParam(value="p_start_num",required=false, defaultValue="") String p_start_num,
			@RequestParam(value="p_req_count",required=false, defaultValue="") String p_req_count,
			@RequestParam(value="callByScheduler", required=false, defaultValue="N") String callByScheduler){

		//#########[LOG START]#########
		Log log_logger = LogFactory.getLog("getPackCategory");
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

		
		GpackCategoryResult result = new GpackCategoryResult();
		//result.setRecordset(null); 

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
			
			if("N".equals(callByScheduler)){		// 단말에서 호출한 경우
				validateGetPackCategory(sa_id, stb_mac, app_type, pack_id, pcategory_id, start_num, req_count, p_start_num, p_req_count, net_typ);

				result = service.getPackCategory(sa_id, stb_mac, pack_id, pcategory_id, start_num, req_count, p_start_num, p_req_count, fh_gbn, callByScheduler);
				
				if ( result.getRecordset_depth1 ( ).size ( ) == 0 ) {
					result.setFlag(SmartUXProperties.getProperty("flag.range6"));
					result.setMessage(SmartUXProperties.getProperty("message.range6"));
				} else {
					result.setFlag(SmartUXProperties.getProperty("flag.success"));
					result.setMessage(SmartUXProperties.getProperty("message.success"));
				}
				
			}else if("A".equals(callByScheduler)){
 
				result = service.getPackCategory("", "", pack_id, pcategory_id, "", "", "", "", fh_gbn, callByScheduler);
				result.setFlag(SmartUXProperties.getProperty("flag.success"));
				result.setMessage(SmartUXProperties.getProperty("message.success"));
				
			}else{									// 스케듈러가 호출한 경우(조회 결과는 셋팅하지 않아도 된다(작업 성공 여부만 셋팅))
				
				packViewService.insertGpackBiz(pack_id, "scheduler");
				
				result = service.getPackCategory("", "", pack_id, pcategory_id, "", "", "", "", fh_gbn, callByScheduler);
				
				result.setFlag(SmartUXProperties.getProperty("flag.success"));
				result.setMessage(SmartUXProperties.getProperty("message.success"));
				
			}
		}catch(Exception e){
			log_logger.error("["+log_ip+"] ["+log_url+"]["+log_info_mac+"]["+log_info_id+"]["+log_info_app_type+"]"+"[getPackCategory]["+e.getClass().getName()+"]["+e.getMessage()+"]");
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
	 * getPackCategory 함수에서 사용하는 validation 함수
	 * @param sa_id
	 * @param stb_mac
	 * @param pack_id
	 * @param category_id
	 * @throws SmartUXException
	 */
	private void validateGetPackCategory(String sa_id, String stb_mac, String app_type, String pack_id, String pcategory_id, String start_num, String req_count, String p_start_num, String p_req_count, String net_typ) throws SmartUXException {
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

		// 어플타입 파라미터 존재 유무
		if(!(StringUtils.hasText(app_type))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "app_type"));
			throw exception;
		}

		//망타입이 잘못넘어 왔을 경우
		if("FAIL".equals(net_typ)){
			exception.setFlag(SmartUXProperties.getProperty("flag.format"));
			exception.setMessage(SmartUXProperties.getProperty("message.format", "net_typ"));
			throw exception;
		}
		
		// 팩ID
		if(!(StringUtils.hasText(pack_id))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "pack_id"));
			throw exception;
		}else if(!pack_id.equals("P0001") && !pack_id.equals("P0002") && !pack_id.equals("P0003") && !pack_id.equals("P0004")){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "pack_id"));
			throw exception;
		}
		
		// 카테고리 ID
		if(!(StringUtils.hasText(pcategory_id))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "pcategory_id"));
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
		
		
		//카테고리 페이징 (pcategory_id=0일때 유효)
		if(pcategory_id.equals("0")){
			//해당 파라메터가 있는지 확인 
			if(!StringUtils.hasText(p_start_num)){
				exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
				exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "p_start_num"));
				throw exception;
			}
			if(!StringUtils.hasText(p_req_count)){
				exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
				exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "p_req_count"));
				throw exception;
			}
			
			//카테고리 검색 시작 인덱스 
			int intPStartnum = 0; 
			try{
				intPStartnum = Integer.parseInt(p_start_num);
			}catch(NumberFormatException e){
				exception.setFlag(SmartUXProperties.getProperty("flag.numberformat"));
				exception.setMessage(SmartUXProperties.getProperty("message.numberformat" , "p_start_num"));
				throw exception;
			}
			if(intPStartnum < 1){
				exception.setFlag(SmartUXProperties.getProperty("flag.range2"));
				exception.setMessage(SmartUXProperties.getProperty("message.range2", "p_start_num", "1"));
				throw exception;
			}
			
			//카테고리 검색 끝 인덱스 
			int intPReqcount = 0; 
			try{
				intPReqcount = Integer.parseInt(p_req_count);
			}catch(NumberFormatException e){
				exception.setFlag(SmartUXProperties.getProperty("flag.numberformat"));
				exception.setMessage(SmartUXProperties.getProperty("message.numberformat" , "p_req_count"));
				throw exception;
			}
			if(intPReqcount < 1){
				exception.setFlag(SmartUXProperties.getProperty("flag.range2"));
				exception.setMessage(SmartUXProperties.getProperty("message.range2", "p_req_count", "1"));
				throw exception;
			}
		}
		
	}
	
	/**
	 * 캐시 내용을 최신 내용으로 갱신하며 연관된 다른 WAS와 캐시를 동기화 하는 작업까지 진행한다
	 * @throws Exception
	 */
	private void CacheJob(String pack_id) throws Exception{
		
		//String host = SmartUXProperties.getProperty("scheduler.host"); 					// 자기 자신것을 호출할때 사용하는 host(스케듈러에 정의된 것을 사용)
		//int port = Integer.parseInt(SmartUXProperties.getProperty("scheduler.port"));	// 자기 자신것을 호출할때 사용하는 port(스케듈러에 정의된 것을 사용)
		String param = "callByScheduler=A&pack_id=" + pack_id;		// 관리자툴에서 호출한다는 의미로 셋팅해준다(이 값이 A여야 DB에서 바로 읽어서 캐쉬에 반영한다)
		int timeout = Integer.parseInt(SmartUXProperties.getProperty("scheduler.timeout"));		// timeout 값은 스케쥴러것을 사용
		int retrycnt = Integer.parseInt(SmartUXProperties.getProperty("scheduler.retrycnt"));	// 재시도 횟수는 스케줄러 것을 사용
		String protocolName = "";
		String url = "";
		
		// 팩 버전 정보 조회 단말 API URL을 가져온다
		protocolName = SmartUXProperties.getProperty("GpackPackVersionDao.getPackVersion.protocol");
		url = SmartUXProperties.getProperty("GpackPackVersionDao.getPackVersion.CacheScheduleURL");
		GlobalCom.syncServerCache(url, param, timeout, retrycnt, protocolName);		// 다른 서버의 캐쉬 동기화 작업 진행
		
		// 팩 기본정보 조회 단말 API URL을 가져온다
		protocolName = SmartUXProperties.getProperty("GpackCategoryInfoDao.getPackInfo.protocol");
		if(pack_id.equals("P0001")){
			url = SmartUXProperties.getProperty("GpackCategoryInfoDao.getPackInfo1.CacheScheduleURL");
		}else if(pack_id.equals("P0002")){
			url = SmartUXProperties.getProperty("GpackCategoryInfoDao.getPackInfo2.CacheScheduleURL");
		}else if(pack_id.equals("P0003")){
			url = SmartUXProperties.getProperty("GpackCategoryInfoDao.getPackInfo3.CacheScheduleURL");
		}else if(pack_id.equals("P0004")){
			url = SmartUXProperties.getProperty("GpackCategoryInfoDao.getPackInfo4.CacheScheduleURL");
		}
		GlobalCom.syncServerCache(url, "callByScheduler=Y", timeout, retrycnt, protocolName);		// 다른 서버의 캐쉬 동기화 작업 진행
		
		// 카테고리 조회 단말 API URL을 가져온다
		protocolName = SmartUXProperties.getProperty("GpackCategoryInfoDao.getPackCategory.protocol");
		if(pack_id.equals("P0001")){
			url = SmartUXProperties.getProperty("GpackCategoryInfoDao.getPackCategory1.CacheScheduleURL");
		}else if(pack_id.equals("P0002")){
			url = SmartUXProperties.getProperty("GpackCategoryInfoDao.getPackCategory2.CacheScheduleURL");
		}else if(pack_id.equals("P0003")){
			url = SmartUXProperties.getProperty("GpackCategoryInfoDao.getPackCategory3.CacheScheduleURL");
		}else if(pack_id.equals("P0004")){
			url = SmartUXProperties.getProperty("GpackCategoryInfoDao.getPackCategory4.CacheScheduleURL");
		}			
		GlobalCom.syncServerCache(url, "callByScheduler=Y", timeout, retrycnt, protocolName);		// 다른 서버의 캐쉬 동기화 작업 진행
		
		// 프로모션 조회 단말 API URL을 가져온다
		protocolName = SmartUXProperties.getProperty("GpackPackPromotionDao.getPackPromotion.protocol");
		if(pack_id.equals("P0001")){
			url = SmartUXProperties.getProperty("GpackPackPromotionDao.getPackPromotion1.CacheScheduleURL");
		}else if(pack_id.equals("P0002")){
			url = SmartUXProperties.getProperty("GpackPackPromotionDao.getPackPromotion2.CacheScheduleURL");
		}else if(pack_id.equals("P0003")){
			url = SmartUXProperties.getProperty("GpackPackPromotionDao.getPackPromotion3.CacheScheduleURL");
		}else if(pack_id.equals("P0004")){
			url = SmartUXProperties.getProperty("GpackPackPromotionDao.getPackPromotion4.CacheScheduleURL");
		}
		GlobalCom.syncServerCache(url, "callByScheduler=Y", timeout, retrycnt, protocolName);		// 다른 서버의 캐쉬 동기화 작업 진행
	}
}
