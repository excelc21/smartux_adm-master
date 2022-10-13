package com.dmi.smartux.admin.news.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONSerializer;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dmi.smartux.admin.commonMng.service.CommonModuleService;
import com.dmi.smartux.admin.commonMng.vo.ChannelVO;
import com.dmi.smartux.admin.commonMng.vo.FlatRateVO;
import com.dmi.smartux.admin.news.service.NewsService;
import com.dmi.smartux.admin.news.vo.LocationVO;
import com.dmi.smartux.admin.news.vo.NewsVO;
import com.dmi.smartux.admin.news.vo.TargetVO;
import com.dmi.smartux.admin.pvs.service.PvsService;
import com.dmi.smartux.admin.pvs.vo.PvsProductVO;
import com.dmi.smartux.common.exception.ExceptionHandler;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.CookieUtil;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.util.HTMLCleaner;

/**
 * 새소식 Controller
 *
 * @author dongho
 */
@Controller
public class NewsController {
    private final Log mLogger = LogFactory.getLog(this.getClass());

	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm";

	@Autowired
	NewsService mService;

	@Autowired
	CommonModuleService mCommonModuleService;

    @Autowired
    PvsService mPvsService;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		sdf.setLenient(true);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));
	}


	@RequestMapping(value = "/admin/news/newsList", method = RequestMethod.GET)
	public String getList(
			@RequestParam(value = "findName", required = false, defaultValue = "TITLE") String findName,
			@RequestParam(value = "findValue", required = false, defaultValue = "") String findValue,
			@RequestParam(value = "pageNum", required = false, defaultValue = "1") String pageNumber,
			@RequestParam(value = "sendingStatus", required = false, defaultValue = "") String sendingStatus,
			@RequestParam(value = "notiType", required = false, defaultValue = "") String notiType,
			Model model
	) throws Exception {
		findName 	= HTMLCleaner.clean(findName);
		findValue = HTMLCleaner.clean(findValue);
		pageNumber = HTMLCleaner.clean(pageNumber);
		sendingStatus = HTMLCleaner.clean(sendingStatus);
		notiType = HTMLCleaner.clean(notiType);

		NewsVO vo = new NewsVO();
		vo.setPageSize(10);
		vo.setBlockSize(10);
		vo.setPageNumber(Integer.parseInt(pageNumber));
		vo.setFindName(findName);
		vo.setFindValue(findValue);
		vo.setSendingStatus(sendingStatus);
		vo.setNotiType(notiType);

		List list = mService.getList(vo);

		vo.setList(list);
		vo.setPageCount(mService.getCount(vo));

		model.addAttribute("PushInfo", getPushLinkageInfo());
		model.addAttribute("PushCodeList", getPushCodeList());
		model.addAttribute("vo", vo);

		return "/admin/news/newsList";
	}

	@RequestMapping(value = "/admin/news/insertNews", method = RequestMethod.GET)
	public String insert(
			@RequestParam(value = "regNumber", required = false, defaultValue = "0") String regNumber,
			Model model
	) throws Exception {
		NewsVO vo = new NewsVO();

		if (!"0".equals(regNumber)) {
			vo = mService.getData(regNumber);

			if ("E".equals(vo.getPushType())) {
				StringBuilder filePath = new StringBuilder();
				filePath.append(SmartUXProperties.getProperty("smartux.domain.http")).append("/");
				filePath.append("smartux/admin/download?");
				filePath.append("path=").append("push/target/").append(vo.getRegNumber()).append(".txt");
				filePath.append("&system=").append("mims");
				model.addAttribute("filePath", filePath.toString());
			} else if ("I".equals(vo.getPushType())) {
				String folerPath = GlobalCom.isNull(SmartUXProperties.getPathProperty("n1.save.path"), "/NAS_DATA/web/smartux/push/target/");
				File file = new File(folerPath + vo.getRegNumber() + ".txt");

				if (file.exists() && file.isFile()) {
					List list = FileUtils.readLines(file);
					model.addAttribute("saIDList", list);
				}
			}
		} else {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MINUTE, 10);
			vo.setSendDate(cal.getTime());
		}

		// 실시간 채널 VO List
		List<ChannelVO> chInfoList = mCommonModuleService.getLiveCHList();

		// 월정액 VO List
		List<FlatRateVO> flatRateList = mCommonModuleService.getFlatRateList();

        // Pvs 상품 List
        List<PvsProductVO> pvsProductList = mPvsService.getPvsProductList();

        // Pvs 부가 상품 List
        List<PvsProductVO> optionalServiceList  = mPvsService.getOptionalServiceList();

        if ("T".equals(vo.getPushType())) {
            TargetVO targetVO = mService.getTargetRule(vo.getRegNumber());
            model.addAttribute("targetVO", targetVO);
        }

		model.addAttribute("chInfoList", chInfoList);
		model.addAttribute("flatRateList", flatRateList);
		model.addAttribute("pvsProductList", pvsProductList);
		model.addAttribute("optionalServiceList", optionalServiceList);
        model.addAttribute("vo", vo);
        model.addAttribute("location", getLocation());

        return "/admin/news/insertNews";
	}

	@RequestMapping(value = "/admin/news/insertNews", method = RequestMethod.POST)
	public String insert(
			NewsVO newsVO,
			HttpServletRequest request
	) throws Exception {
		String cookieID = CookieUtil.getCookieUserID(request);
		String remoteIP = request.getRemoteAddr();

		newsVO.setActID(cookieID);
		newsVO.setActIP(remoteIP);
		newsVO.setScreenType("T");
		newsVO.setNotiGB("N1");

		if (0 <newsVO.getRegNumber()) {
			newsVO.setActGbn("U");
			mService.update(newsVO);
		} else {
			newsVO.setActGbn("I");
			mService.insert(newsVO);
		}

		return "redirect:/admin/news/newsList.do";
	}

	@RequestMapping(value = "/admin/news/deleteNews", method = RequestMethod.POST)
	public ResponseEntity<String> delete(
			@RequestParam(value="numbers", required=false, defaultValue="") String numbers,
			HttpServletRequest request
	) throws Exception {
		String flag;
		String message;

		String cookieID = CookieUtil.getCookieUserID(request);
		String remoteIP = request.getRemoteAddr();

		try{
			String[] ary = numbers.split(",");

			for (String s : ary) {
				NewsVO newsVO = mService.getData(s);

				if (null != newsVO) {
					newsVO.setActID(cookieID);
					newsVO.setActIP(remoteIP);
					newsVO.setActGbn("D");

					mService.delete(newsVO);
				}
			}

			flag = SmartUXProperties.getProperty("flag.success");
			message = SmartUXProperties.getProperty("message.success");
		} catch (Exception e) {
			ExceptionHandler handler = new ExceptionHandler(e);
			flag = handler.getFlag();
			message = handler.getMessage();
		}

		String result = "{\"result\":{\"flag\":\"" + flag + "\",\"message\":\"" + message + "\"}}";

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(result).toString(), responseHeaders, HttpStatus.CREATED);
	}

    /**
     * 메시지 유효성 검사
     *
     * @param newsVO 새소식 객체
     * @return Json 결과
     * @throws Exception
     */
    @RequestMapping(value="/admin/news/validPushMessage", method=RequestMethod.POST)
    public ResponseEntity<String> validPushMessage(
            @ModelAttribute NewsVO newsVO) throws Exception {

        mLogger.info("[NewsController] validPushMessage ############# ");
        mLogger.info("request vo:" + newsVO.toString());

        Map<String, String> resultMap = mService.checkPushMessage(newsVO);

        String result = "{\"result\":{\"flag\":\"" + resultMap.get("res") + "\",\"message\":\"" + resultMap.get("msg") + "\"}}";

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
        return new ResponseEntity<String>(JSONSerializer.toJSON(result).toString(), responseHeaders, HttpStatus.CREATED);
    }

    /**
     * 지역구분 코드와 이름을 가져온다.
     *
     * @return 지역코드 리스트
     */
    private List getLocation() {
        List<LocationVO> list = new ArrayList<LocationVO>();

        String code = SmartUXProperties.getProperty("news.location.code");
        String name = SmartUXProperties.getProperty("news.location.name");

        if (!GlobalCom.isEmpty(code) && !GlobalCom.isEmpty(name)) {
            String[] codeAry = code.split("\\|\\|", -1);
            String[] nameAry = name.split("\\|\\|", -1);

            int count = codeAry.length;

            for (int i = 0; i < count ; i++) {
                try {
                    LocationVO vo = new LocationVO();
                    vo.setCode(Integer.parseInt(codeAry[i]));
                    vo.setName(nameAry[i]);
                    list.add(vo);
                } catch (NumberFormatException ignored) {
                }
            }
        }

        return list;
    }
    
    /**
	 * Push연동을 위한 정보 조회
	 * @return
	 */
	private Map<String, String> getPushLinkageInfo(){
		Map<String,String> resultMap = new HashMap<String,String>();
		
		resultMap.put("GcmSvcId", SmartUXProperties.getProperty("pushAgent.info.serviceId")!=null ? SmartUXProperties.getProperty("pushAgent.info.serviceId"):"");
		resultMap.put("GcmAppId", SmartUXProperties.getProperty("pushAgent.info.appId")!=null ? SmartUXProperties.getProperty("pushAgent.info.appId"):"");
		
		return resultMap;
	}
	
	/**
	 * Push연동 시 주요 에러코드 조회
	 * @return
	 */
	private List<String> getPushCodeList(){
		List<String> resultList = new ArrayList<String>();
		
		for(String key : SmartUXProperties.getKeys("pushgw.error.list")){
			if(key.startsWith("pushgw.error.list") && SmartUXProperties.getProperty(key)!=null)
				resultList.add(SmartUXProperties.getProperty(key));
		}
		
		return resultList;
	}
}