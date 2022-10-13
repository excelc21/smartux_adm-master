package com.dmi.smartux.admin.memberlist.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;

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
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.dmi.commons.consts.ResultCode;
import com.dmi.commons.exception.MimsCommonException;
import com.dmi.commons.factory.FileProcessFunction;
import com.dmi.commons.factory.FileProcessLockFactory;
import com.dmi.commons.logger.CLog;
import com.dmi.commons.model.RestfulCodeMsg;
import com.dmi.commons.utility.HttpClientUtils;
import com.dmi.commons.utility.RestfulUtils;
import com.dmi.smartux.admin.memberlist.service.MemberListAdminService;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.CookieUtil;
import com.dmi.smartux.common.vo.CUDResult;

import net.sf.json.JSONSerializer;

@Controller
public class MemberListAdminController {

	@Autowired
	private MemberListAdminService memberListAdminService;
	
	private final Log logger = LogFactory.getLog(this.getClass());

	private final Log applyLogger = LogFactory.getLog("adminApplyCacheHistroy");
	
    /**
     * memberList 즉시적용
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/admin/memberlist/memberListApplyCache", method=RequestMethod.POST)
	public ResponseEntity<String> ApplyCache(
			HttpServletRequest request, 
			HttpServletResponse response
			) throws Exception {
		
    	final CLog cLog = new CLog(applyLogger, request.getRemoteAddr(), request.getRequestURI(), request.getMethod());
		final String loginUser = CookieUtil.getCookieUserID(request);
		final CUDResult result = new CUDResult();
		
		try {
			cLog.startLog("memberList 즉시적용",  loginUser);
			final String host = SmartUXProperties.getProperty("cache.sync.public.host");
			final String port = SmartUXProperties.getProperty("cache.sync.public.port");
			
			String fileLockPath = SmartUXProperties.getProperty("MemberList.refreshMemberList.fileLock");
			int waitTime = NumberUtils.toInt(SmartUXProperties.getProperty("cache.sync.apply.wait"), 5);
			
			FileProcessLockFactory.start(fileLockPath, waitTime,  new FileProcessFunction("memberList 즉시적용", ResultCode.ApplyRequestFail) {
				
				@Override
				public void run() throws Exception {
					
					RestfulCodeMsg codeMsg = RestfulUtils.getJSONRestfulCodeMsg(HttpClientUtils.sendApplyRequest(
							host, 
							port, 
							SmartUXProperties.getProperty("MemberList.refreshMemberList.url"),
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.method"), "POST"), 
							org.apache.commons.lang.StringUtils.defaultString(SmartUXProperties.getProperty("cache.sync.apply.encoding"), "UTF-8"), 
							MediaType.APPLICATION_JSON.toString(),
							NumberUtils.toInt(SmartUXProperties.getProperty("MemberList.refreshMemberList.timeout"), 90000)).getResponseBody());
					
					result.setFlag(codeMsg.getCode());
					result.setMessage(codeMsg.getMessage());
					cLog.middleLog("memberList 즉시적용", "API 호출", codeMsg.getCode(), codeMsg.getMessage());
				}
			});
		} catch (MimsCommonException e) {
			cLog.warnLog("memberList 즉시적용", "실패", e.getFlag(), e.getMessage(), e.getLocalizedMessage());
			result.setFlag(e.getFlag());
			result.setMessage(e.getMessage());
		} finally {
			cLog.endLog("memberList 즉시적용", loginUser, result.getFlag(), result.getMessage());
		}
		
		String resultMsg = "{\"flag\" : \"" + result.getFlag() + "\", \"message\" : \"" + result.getMessage() + "\"}";
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(resultMsg).toString(), responseHeaders, HttpStatus.OK);
	}

	@RequestMapping(value = "/admin/memberlist/memberList", method = RequestMethod.GET)
	public String getQualityMemberList(Model model) {
		model.addAttribute("test", "테스트");
		model.addAttribute("fileName", memberListAdminService.getRecentFileName());
		return "/admin/memberlist/memberList";
	}
	
	/**
	 * Template 다운로드
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/admin/memberlist/downloadTemplate", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getTotalList() throws Exception {
		
		byte[] makeTemplate = memberListAdminService.makeTemplate().getBytes(Charset.forName("UTF-8"));
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "text/plain; charset=UTF-8");
		headers.add("Content-Disposition", "attachment;filename=\"sample.txt\"");

		return new ResponseEntity<byte[]>(makeTemplate, headers, HttpStatus.CREATED);
	}
	
	/**
	 * 업로드
	 * @param uploadFile
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/memberlist/configurationMemberListUpload", method=RequestMethod.POST )
	public ResponseEntity<String> configurationMemberListUpload(
			@RequestParam(value="uploadFile", required=false, defaultValue="") MultipartFile uploadFile,
			HttpServletRequest request,
			Model model
			) throws Exception {
		
		String result = "";
		String resultcode = "";
		String resultmessage = "";
		try {
			memberListAdminService.uploadFile(uploadFile);

			resultcode = "1";
			resultmessage = "성공";
			
		} catch (FileNotFoundException e) {
			logger.error("[configurationMemberListUpload][Upload Fail]["+e.getClass().getName()+"]["+e.getMessage()+"]");	
			resultcode = "3";
			resultmessage = "폴더가 존재하지 않습니다";
		} catch (Exception e) {
			logger.error("[configurationMemberListUpload][Upload Fail]["+e.getClass().getName()+"]["+e.getMessage()+"]");	
			resultcode = "3";
			resultmessage = "처리에 문제 발생";
		}
		
		result = "{\"result\":{\"message\":\""+resultmessage+"\",\"flag\":\""+resultcode+"\"}}";
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(result).toString(), responseHeaders, HttpStatus.CREATED);	
	}
	
	@RequestMapping(value="/admin/memberlist/memberListDelete", method=RequestMethod.POST )
	public ResponseEntity<String> memberListDelete(
			HttpServletRequest request,
			Model model
			) throws Exception {
		
		String result = "";
		String resultcode = "";
		String resultmessage = "";
		try {
			memberListAdminService.deleteUploadedFile();

			resultcode = SmartUXProperties.getProperty("flag.success");
			resultmessage = SmartUXProperties.getProperty("message.success");
			
		} catch (Exception e) {
			logger.error("[memberListDelete][Delete Fail]["+e.getClass().getName()+"]["+e.getMessage()+"]");	
			resultcode = "3";
			resultmessage = "처리에 문제 발생";
		}
		
		result = "{\"result\":{\"message\":\""+resultmessage+"\",\"flag\":\""+resultcode+"\"}}";
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(result).toString(), responseHeaders, HttpStatus.CREATED);	
	}
	
	@RequestMapping(value="/admin/memberlist/checkFile", method=RequestMethod.GET )
	public ResponseEntity<String> checkFile(
			HttpServletRequest request,
			HttpServletResponse response,
			Model model
			) throws Exception {
		
		String result = "";
		String resultcode = "";
		String resultmessage = "";
		
		boolean memberList = memberListAdminService.checkFile();
		
		if (memberList) {
			resultcode = SmartUXProperties.getProperty("flag.success");
			resultmessage = SmartUXProperties.getProperty("message.success");
		} else {
			resultcode = "2";
			resultmessage = "파일이 없음";
		}
		
		result = "{\"result\":{\"message\":\""+resultmessage+"\",\"flag\":\""+resultcode+"\"}}";
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(result).toString(), responseHeaders, HttpStatus.CREATED);	
	}
	
	@RequestMapping(value="/admin/memberlist/downloadMemberList", method=RequestMethod.GET )
	public void downloadMemberList(
			HttpServletRequest request,
			HttpServletResponse response,
			Model model
			) throws Exception {
		
		File memberList = memberListAdminService.getMemberListFile();
		
		if (memberList != null) {

			response.setContentType("application/octat-stream");
			response.setContentLength((int) memberList.length());
			response.setHeader("Content-Disposition", "attachment;filename=\"" + memberList.getName() + "\"");
			
			FileInputStream inputStream = new FileInputStream(memberList);
			try {
				FileCopyUtils.copy(inputStream, response.getOutputStream());
			} catch (Exception e) {
				logger.error("[downloadMemberList][Download Fail]["+e.getClass().getName()+"]["+e.getMessage()+"]");	
			} finally {
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (Exception ignore) {
					}
				}
			}
		}
	}

}
