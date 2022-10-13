package com.dmi.smartux.sample.controller;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dmi.smartux.common.module.StatPushLogger;
import com.dmi.smartux.common.vo.StatPushLogVo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.dmi.smartux.common.exception.ExceptionHandler;
import com.dmi.smartux.common.exception.SmartUXException;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.vo.CUDResult;
import com.dmi.smartux.common.vo.Result;
import com.dmi.smartux.sample.service.SampleTable;
import com.dmi.smartux.sample.service.impl.TestPropertySample;
import com.dmi.smartux.sample.vo.EHCacheVO;
import com.dmi.smartux.sample.vo.ResultOne;
import com.dmi.smartux.sample.vo.SampleVO;
import com.dmi.smartux.sample.vo.SampleVO2;

@Controller
public class Sample {

	private final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	SampleTable service;

    @RequestMapping(value="/logTest")
    public void logTest() throws Exception{
        StatPushLogger pushLogger = new StatPushLogger();

        // 발송 로그
        StatPushLogVo logVO = new StatPushLogVo();
        logVO.setResult_code("0000");
        logVO.setClient_ip("127.0.0.1");
        logVO.setDev_info("SERVE");

        String pushID = String.valueOf(1234);

        LinkedHashMap<String, String> addInfo = new LinkedHashMap<String, String>();
        addInfo.put("PUSH_STYPE", "N");
        addInfo.put("PUSH_ID", pushID);
        addInfo.put("PUSH_LTYPE", "S");
        addInfo.put("PUSH_SENDCNT", "");
        addInfo.put("PUSH_NAME", "테스트로그");
        addInfo.put("PUSH_TYPE1", "P");
        addInfo.put("PUSH_TYPE2", "N");
        addInfo.put("PUSH_REGDATE", new SimpleDateFormat("yyyyMMddHHmmSS").format(new Date()));

        logVO.setAddInfo(addInfo);
        pushLogger.writePushLog(logVO);

        // 전송
        String code = "0000";

        // 수신 로그
        logVO.setResult_code(code);
        addInfo.clear();
        addInfo.put("PUSH_STYPE", "N");
        addInfo.put("PUSH_ID", pushID);
        addInfo.put("PUSH_LTYPE", "R");
        addInfo.put("PUSH_SENDCNT", "");
        addInfo.put("PUSH_NAME", "");
        addInfo.put("PUSH_TYPE1", "");
        addInfo.put("PUSH_TYPE2", "");
        addInfo.put("PUSH_REGDATE", "");

        logVO.setAddInfo(addInfo);
        pushLogger.writePushLog(logVO);
    }
	
	@RequestMapping(value="/abc/test.do")
	public String mytest(HttpServletRequest request, Model model) throws Exception{
		// model.addAttribute("list", service.select(1, 1));
		model.addAttribute("mytest", "테스트");
		String ipaddr = SmartUXProperties.getProperty("pushgateway.ip");
		logger.debug("ipaddr : " + ipaddr);
		
		List<String> readlist = Collections.synchronizedList(new ArrayList<String>());
		List<String> writelist = Collections.synchronizedList(new ArrayList<String>());
		
		writelist.add("1");
		writelist.add("2");
		writelist.add("3");
		
		logger.debug("before readlist.size() : " + readlist.size());
		logger.debug("before writelist.size() : " + writelist.size());
		
		readlist = writelist;
		
		writelist = Collections.synchronizedList(new ArrayList<String>());
		
		logger.debug("after readlist.size() : " + readlist.size());
		logger.debug("after writelist.size() : " + writelist.size());
		
		
		return "/test/abc";
	}
	
	/**
	 * list 형태의 샘플
	 * dao 계층, service 계층에서는 List<T> 형태의 값이 리턴되게 한뒤 Result의 recordset 멤버변수에 리턴된 값이 셋팅되도록 한다
	 * @param start
	 * @param end
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	public Result<SampleVO> list(
			@RequestParam(value="start", required=false, defaultValue="1") String start,
			@RequestParam(value="end", required=false) String end,
			HttpServletRequest request) throws Exception{
	
		logger.debug("start : " + start);
		logger.debug("end :" + end);
		logger.debug("method : " + request.getMethod());
		
		Result<SampleVO> result = new Result<SampleVO>();
		result.setRecordset(null);
		
		
		try {
			validateList(start, end);
			logger.debug("after start : " + start);
			result.setFlag(SmartUXProperties.getProperty("flag.success"));
			result.setMessage(SmartUXProperties.getProperty("message.success"));
			result.setRecordset(service.select(Integer.parseInt(start), Integer.parseInt(end)));
		}catch(Exception e){
			ExceptionHandler handler = new ExceptionHandler(e);
			result.setFlag(handler.getFlag());
			result.setMessage(handler.getMessage());
		}
		// model.addAttribute("result", result.toString());
		// SVN 변경 테스트
		return result;
	}
	
	@RequestMapping(value="/list2")
	public Result<SampleVO2> list2(
			@RequestParam(value="start", required=false, defaultValue="1") String start,
			@RequestParam(value="end", required=false) String end,
			HttpServletRequest request) throws Exception{
	
		logger.debug("start : " + start);
		logger.debug("end :" + end);
		logger.debug("method : " + request.getMethod());
		
		Result<SampleVO2> result = new Result<SampleVO2>();
		result.setRecordset(null);
		
		
		try {
			validateList(start, end);
			logger.debug("after start : " + start);
			result.setFlag(SmartUXProperties.getProperty("flag.success"));
			result.setMessage(SmartUXProperties.getProperty("message.success"));
			result.setRecordset(service.select2(Integer.parseInt(start), Integer.parseInt(end)));
		}catch(Exception e){
			ExceptionHandler handler = new ExceptionHandler(e);
			result.setFlag(handler.getFlag());
			result.setMessage(handler.getMessage());
		}
		// model.addAttribute("result", result.toString());
		// SVN 변경 테스트
		return result;
	}
	
	@RequestMapping(value="/oneselect")
	public ResultOne oneselect(
			@RequestParam(value="idx", required=false) int idx
			) throws Exception{
	
		logger.debug("idx" + idx);
		
		ResultOne result = new ResultOne();
		
		try {
			
			result = service.oneselect(idx);
			// 검색 결과가 없을 경우
			if(result == null){
				result = new ResultOne();
				result.setFlag(SmartUXProperties.getProperty("errorcode.notfound"));
				result.setMessage(SmartUXProperties.getProperty("errormsg.notfound"));
				result.setIdx(-1);
				result.setName("");
			}else{
				result.setFlag(SmartUXProperties.getProperty("errorcode.success"));
				result.setMessage(SmartUXProperties.getProperty("errormsg.success"));
			}
		}catch(Exception e){
			ExceptionHandler handler = new ExceptionHandler(e);
			result.setFlag(handler.getFlag());
			result.setMessage(handler.getMessage());
		}
		// model.addAttribute("result", result.toString());
		// SVN 변경 테스트
		return result;
	}
	
	@RequestMapping(value="/insert")
	public CUDResult insert(
			@RequestParam(value="idx", required=false) int idx,
			@RequestParam(value="name", required=false) String name
			) throws Exception{
	
		CUDResult result = new CUDResult();
		
		try{
			service.insert(idx, name);
			result.setFlag(SmartUXProperties.getProperty("flag.success"));
			result.setMessage(SmartUXProperties.getProperty("message.success"));	
		}catch(Exception e){
			ExceptionHandler handler = new ExceptionHandler(e);
			result.setFlag(handler.getFlag());
			result.setMessage(handler.getMessage());
		}
		
		return result;
	}
	
	@RequestMapping(value="/update")
	public CUDResult update(
			@RequestParam(value="idx", required=false) int idx,
			@RequestParam(value="name", required=false) String name
			) throws Exception{
	
		logger.debug("idx : " + idx);
		logger.debug("name :" + name);
	
		CUDResult result = new CUDResult();

		try{
			service.update(idx, name);		
			result.setFlag(SmartUXProperties.getProperty("flag.success"));
			result.setMessage(SmartUXProperties.getProperty("message.success"));
		}catch(Exception e){
			ExceptionHandler handler = new ExceptionHandler(e);
			result.setFlag(handler.getFlag());
			result.setMessage(handler.getMessage());
		}
		return result;
	}	
	
	@RequestMapping(value="/delete")
	public CUDResult delete(
			@RequestParam(value="idx", required=false) int idx
			) throws Exception{
	
		logger.debug("idx : " + idx);
	
		CUDResult result = new CUDResult();

		try{
			service.delete(idx);		
			result.setFlag(SmartUXProperties.getProperty("flag.success"));
			result.setMessage(SmartUXProperties.getProperty("message.success"));
		}catch(Exception e){
			ExceptionHandler handler = new ExceptionHandler(e);
			result.setFlag(handler.getFlag());
			result.setMessage(handler.getMessage());
		}
		return result;
	}
	
	/**
	 * validate 샘플
	 * @param start
	 * @param end
	 * @throws SmartUXException
	 */
	private void validateList(String start, String end) throws SmartUXException{
		SmartUXException exception = new SmartUXException();
		
		if(end == null){
			exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
			exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", "end"));
			throw exception;
		}
	}
	
	@RequestMapping(value="/ehcache")
	public Result<EHCacheVO> ehcache() throws Exception{
		Result<EHCacheVO> result = new Result<EHCacheVO>();
		result.setRecordset(null);


		try {
			result.setFlag(SmartUXProperties.getProperty("flag.success"));
			result.setMessage(SmartUXProperties.getProperty("message.success"));
			List<EHCacheVO> list = service.ehcache();
			result.setTotal_count(list.size());
			result.setRecordset(list);
		}catch(Exception e){
			ExceptionHandler handler = new ExceptionHandler(e);
			result.setFlag(handler.getFlag());
			result.setMessage(handler.getMessage());
		}
		
		return result;
	}
	
	@RequestMapping(value="/ehcacheCustom")
	public Result<EHCacheVO> ehcacheCustom(
			@RequestParam(value="category", required=false) String category,
			@RequestParam(value="startnum", required=false, defaultValue="1") String startnum,
			@RequestParam(value="reqcount", required=false) String reqcount
			) throws Exception{
		Result<EHCacheVO> result = new Result<EHCacheVO>();
		result.setRecordset(null);


		try {
			result.setFlag(SmartUXProperties.getProperty("flag.success"));
			result.setMessage(SmartUXProperties.getProperty("message.success"));
			List<EHCacheVO> list = service.ehcacheCustom(category, startnum, reqcount);
			result.setTotal_count(list.size());
			result.setRecordset(list);
		}catch(Exception e){
			ExceptionHandler handler = new ExceptionHandler(e);
			result.setFlag(handler.getFlag());
			result.setMessage(handler.getMessage());
		}
		
		return result;
	}
	
	@RequestMapping(value="/test/fileupload",method=RequestMethod.GET)
	public String viewfileupload() throws Exception{
		return "/test/fileupload";
	}
	
	@RequestMapping(value="/test/fileupload",method=RequestMethod.POST)
	public String procfileupload(HttpServletRequest request, HttpServletResponse response) throws Exception{
		logger.debug("contentType : " + request.getContentType());
		if(request instanceof MultipartHttpServletRequest){
			logger.debug("MultipartHttpServletRequest OK");
		}else{
			logger.debug("MultipartHttpServletRequest NOT OK");
		}
		
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		
		return "/test/fileupload";
	}
	
	@RequestMapping(value="/test/httpscall",method=RequestMethod.GET)
	public String httpscall(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String host = "bwstb.mylgtv.com";
		int port = 443;
		String url = "/";
		String param = "";
		int timeout = 5000;
		String protocolName = "https";
		
		String result = GlobalCom.callHttpClient(host, port, url, param, timeout, protocolName);
		System.out.println(result);
		return "/test/abc";
	}
}
