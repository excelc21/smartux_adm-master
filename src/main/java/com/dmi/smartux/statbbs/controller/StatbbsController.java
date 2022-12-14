package com.dmi.smartux.statbbs.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dmi.smartux.common.exception.SmartUXException;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.CLog;
import com.dmi.smartux.common.util.CharacterSet;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.vo.Result;
import com.dmi.smartux.common.vo.StatParticipateVo;
import com.dmi.smartux.statbbs.service.StatbbsFileUtil;
import com.dmi.smartux.statbbs.service.StatbbsService;
import com.dmi.smartux.statbbs.vo.BbsStatListVo;
import com.dmi.smartux.statbbs.vo.BbsStatResultVo;

@Controller
public class StatbbsController {

	@Autowired
	StatbbsService statbbsService;

	/*
	 * handleException 			Error/Exception Handling
	 * @param ex				SmartUXException
	 * @return					errorVO
	 * @throws Exception
	 */
	@ExceptionHandler(SmartUXException.class)
	public @ResponseBody String handleException(SmartUXException e, HttpServletRequest request,HttpServletResponse response) throws SmartUXException {
		if(request.getHeader("accept") == null || "*/*".equals(request.getHeader("accept"))){
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
	 * ??????????????? ?????? ????????? ???????????? ????????? ????????????.
	 * <pre>
	 * ex)
	 * ??????		: http://?????? ??????/hdtv/statbbs/cache
	 * </pre>
	 * @param request 
	 * @param response 
	 * @param bbs_id ????????? id
	 * @param callByScheduler ???????????? ????????? ???????????? ?????? ????????? ???????????? ?????? ???????????? ????????????.
	 * @return ?????? ?????? ?????? VO
	 */
	@RequestMapping(value="/statbbs/cache", method=RequestMethod.GET)
	public Result<String> refreshCacheOfStatBbs(
		HttpServletRequest request
		, HttpServletResponse response
		, @RequestParam(value="callByScheduler", required=false, defaultValue="N") String callByScheduler
	) {
		//#########[LOG START]#########
		Log log_logger = LogFactory.getLog("refreshCacheOfStatbbs");
		CLog cLog = new CLog(log_logger, request);
		cLog.startLog( "["+GlobalCom.isNull(callByScheduler)+"]" );

		Result<String> result = new Result<String>();
		result.setFlag(SmartUXProperties.getProperty("flag.success"));
		result.setMessage(SmartUXProperties.getProperty("message.success"));
		result.setRecordset(null);
		try {
			statbbsService.refreshCacheOfBbsStat(callByScheduler);
		} catch(Exception e) {
			cLog.errorLog("["+GlobalCom.isNull(callByScheduler)+"]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			com.dmi.smartux.common.exception.ExceptionHandler handler = new com.dmi.smartux.common.exception.ExceptionHandler(e);
			result.setFlag(handler.getFlag());
			result.setMessage(handler.getMessage());
		}

		//#########[LOG END]#########
		cLog.endLog( "["+GlobalCom.isNull(callByScheduler)+"] ["+result.getFlag()+"]");

		return result;
	}
	
	/**
	 * statbbs?????? ??????I/O?????? ????????? ?????? ??? ???????????????.
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/statbbs/bytebuffer/reset", method=RequestMethod.GET)
	public Result<String> refreshCacheOfNotiPop(
		HttpServletRequest request
		, HttpServletResponse response
	) {
		//#########[LOG START]#########
		Log log_logger = LogFactory.getLog("refreshCacheOfQualityMember");
		CLog cLog = new CLog(log_logger, request);
		cLog.startLog( "[Statbbs ByteBuffer Reset!!]" );

		Result<String> result = new Result<String>();
		result.setFlag(SmartUXProperties.getProperty("flag.success"));
		result.setMessage(SmartUXProperties.getProperty("message.success"));
		result.setRecordset(null);
		try {
			//?????? ?????? ??????
			StatbbsFileUtil SU = new StatbbsFileUtil();
			SU.settingBuff();
		} catch(Exception e) {
			cLog.errorLog("["+e.getClass().getName()+"]["+e.getMessage()+"]");
			com.dmi.smartux.common.exception.ExceptionHandler handler = new com.dmi.smartux.common.exception.ExceptionHandler(e);
			result.setFlag(handler.getFlag());
			result.setMessage(handler.getMessage());
		}

		//#########[LOG END]#########
		cLog.endLog( "[Statbbs ByteBuffer Reset END]["+result.getFlag()+"]");

		return result;
	}

	@RequestMapping(value="/v1/stat", method=RequestMethod.POST)
	public BbsStatResultVo addBbsStat_OpenApi(
			HttpServletRequest request
			, HttpServletResponse response
			,@RequestParam(value="access_key", 	 required=false) String access_key
			,@RequestParam(value="cp_id", 	 required=false) String cp_id
			,@RequestParam(value="sa_id", 	 required=false) String sa_id
			,@RequestParam(value="stb_mac", 	 required=false) String mac
			,@RequestParam(value="ctn", 	 required=false) String ctn
			,@RequestParam(value="ev_stat_id", 	 required=false) String ev_stat_id
		) throws Exception {
		
		//#########[LOG SET]#########
		Log log_logger = LogFactory.getLog("statbbs");
		CLog cLog = new CLog(log_logger, request);
		
		SmartUXException exception = new SmartUXException();
		
		BbsStatResultVo result = new BbsStatResultVo();
		try {
			
			//#########[LOG START]#########
			cLog.startLog( "[POST]-["+access_key+"]["+cp_id+"]["+sa_id+"]["+mac+"]["+ctn+"]["+ev_stat_id+"]" );
			
			//????????? ??????
			validateAddBbsStat(sa_id, mac, ctn, ev_stat_id);
			
			BbsStatListVo bbsstatlistVo = statbbsService.getBbsStat(ev_stat_id);
			
			String r_date = GlobalCom.getTodayFormat4_24();
			String bbsFilePath = "";
			if(bbsstatlistVo==null || "".equals(bbsstatlistVo.getStat_file_path())){
				cLog.endLog( "[POST]-["+access_key+"]["+cp_id+"]["+sa_id+"]["+mac+"]["+ctn+"]["+ev_stat_id+"][No CacheData]");
				exception.setFlag(SmartUXProperties.getProperty("flag.bbsstat.nostatdata"));
				exception.setMessage(SmartUXProperties.getProperty("message.bbsstat.nostatdata"));
				throw exception;
			}else{
				bbsFilePath = bbsstatlistVo.getStat_file_path();
			}
			
			StatParticipateVo statparticipateVo = new StatParticipateVo(sa_id, mac, ctn, r_date);
			
			statbbsService.addBbsStat(statparticipateVo, bbsFilePath);

			result.setFlag(SmartUXProperties.getProperty("flag.success"));
			result.setMessage(SmartUXProperties.getProperty("message.success"));
		}catch (SmartUXException e){
			cLog.endLog( "[POST]-["+access_key+"]["+cp_id+"]["+sa_id+"]["+mac+"]["+ctn+"]["+ev_stat_id+"]["+result.getFlag()+"]");
			throw e;	
		}catch(Exception e){
			//log_logger.error("[notilist]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			cLog.errorLog("[POST]-["+access_key+"]["+cp_id+"]["+sa_id+"]["+mac+"]["+ctn+"]["+ev_stat_id+"]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			com.dmi.smartux.common.exception.ExceptionHandler handler = new com.dmi.smartux.common.exception.ExceptionHandler(e);
			result.setFlag(handler.getFlag());
			result.setMessage(handler.getMessage());
		}

		//#########[LOG END]#########
		cLog.endLog( "[POST]-["+access_key+"]["+cp_id+"]["+sa_id+"]["+mac+"]["+ctn+"]["+ev_stat_id+"]["+result.getFlag()+"]" );
		
		return result;
	}
	
	@RequestMapping(value="/comm/stat", method=RequestMethod.POST)
	public BbsStatResultVo addBbsStat(
			HttpServletRequest request
			, HttpServletResponse response
			,@RequestParam(value="sa_id", 	 required=false) String sa_id
			,@RequestParam(value="stb_mac", 	 required=false) String mac
			,@RequestParam(value="ctn", 	 required=false) String ctn
			,@RequestParam(value="ev_stat_id", 	 required=false) String ev_stat_id
		) throws Exception {
		
		//#########[LOG SET]#########
		Log log_logger = LogFactory.getLog("statbbs");
		CLog cLog = new CLog(log_logger, request);
		
		SmartUXException exception = new SmartUXException();
		
		BbsStatResultVo result = new BbsStatResultVo();
		try {
			
			//#########[LOG START]#########
			cLog.startLog( "[POST]-["+sa_id+"]["+mac+"]["+ctn+"]["+ev_stat_id+"]" );
			
			//????????? ??????
			validateAddBbsStat(sa_id, mac, ctn, ev_stat_id);
			
			BbsStatListVo bbsstatlistVo = statbbsService.getBbsStat(ev_stat_id);
			
			String r_date = GlobalCom.getTodayFormat4_24();
			String bbsFilePath = "";
			if(bbsstatlistVo==null || "".equals(bbsstatlistVo.getStat_file_path())){
				cLog.endLog( "[POST]-["+sa_id+"]["+mac+"]["+ctn+"]["+ev_stat_id+"][No CacheData]");
				exception.setFlag(SmartUXProperties.getProperty("flag.bbsstat.nostatdata"));
				exception.setMessage(SmartUXProperties.getProperty("message.bbsstat.nostatdata"));
				throw exception;
			}else{
				bbsFilePath = bbsstatlistVo.getStat_file_path();
			}
			
			StatParticipateVo statparticipateVo = new StatParticipateVo(sa_id, mac, ctn, r_date);
			
			statbbsService.addBbsStat(statparticipateVo, bbsFilePath);

			result.setFlag(SmartUXProperties.getProperty("flag.success"));
			result.setMessage(SmartUXProperties.getProperty("message.success"));
		}catch (SmartUXException e){
			cLog.endLog( "[POST]-["+sa_id+"]["+mac+"]["+ctn+"]["+ev_stat_id+"]["+result.getFlag()+"]");
			throw e;	
		}catch (TaskRejectedException e){
			cLog.endLog( "[POST]-["+sa_id+"]["+mac+"]["+ctn+"]["+ev_stat_id+"][TaskRejected!]");
			com.dmi.smartux.common.exception.ExceptionHandler handler = new com.dmi.smartux.common.exception.ExceptionHandler(e);
			result.setFlag(handler.getFlag());
			result.setMessage(handler.getMessage());
		}catch(Exception e){
			//log_logger.error("[notilist]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			cLog.errorLog("[POST]-["+sa_id+"]["+mac+"]["+ctn+"]["+ev_stat_id+"]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			com.dmi.smartux.common.exception.ExceptionHandler handler = new com.dmi.smartux.common.exception.ExceptionHandler(e);
			result.setFlag(handler.getFlag());
			result.setMessage(handler.getMessage());
		}

		//#########[LOG END]#########
		cLog.endLog( "[POST]-["+sa_id+"]["+mac+"]["+ctn+"]["+ev_stat_id+"]["+result.getFlag()+"]" );
		
		return result;
	}

	private void validateAddBbsStat(String sa_id, String mac, String ctn, String ev_stat_id) {
		SmartUXException exception = new SmartUXException();
		
		//?????? ??? ??????
		if(!(StringUtils.hasText(sa_id))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "sa_id"));
			throw exception;
		}
		
		//?????? ??? ??????
		if(!(StringUtils.hasText(mac))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "stb_mac"));
			throw exception;
		}
		
		//???????????? ??? ??????
		if(!(StringUtils.hasText(ctn))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "ctn"));
			throw exception;
		}
		
		//?????????????????? ??? ??????
		if(!(StringUtils.hasText(ev_stat_id))){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "ev_stat_id"));
			throw exception;
		}
	}

}
