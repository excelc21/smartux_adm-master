package com.dmi.smartux.admin.pvs.controller;

import com.dmi.smartux.admin.news.vo.NewsVO;
import com.dmi.smartux.admin.pvs.service.PvsService;
import com.dmi.smartux.common.exception.ExceptionHandler;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.GlobalCom;
import net.sf.json.JSONSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * PVS 컨트롤러
 *
 * @author dongho
 */
@Controller
public class PvsController {
    @Autowired
    PvsService mService;

    /**
     * 타겟팅 개수 체크
     *
     * @param newsVO 새소식 객체
     * @return Json 결과
     * @throws Exception
     */
    @RequestMapping(value="/admin/pvs/checkCount", method= RequestMethod.POST)
    public ResponseEntity<String> checkCount(
            NewsVO newsVO
    ) throws Exception {
        String flag = SmartUXProperties.getProperty("flag.success");
        int count = 0;

        try {
            String sendType = newsVO.getSendType();
            String optionalServiceCode = newsVO.getOptionalServiceCode();

            if ("00".equals(newsVO.getTargetNetType())
                    && "GA".equals(newsVO.getModelType())
                    && "A".equals(newsVO.getGender())
                    && GlobalCom.isEmpty(newsVO.getProductionCode())
                    && GlobalCom.isEmpty(newsVO.getLocationCode())
                    && GlobalCom.isEmpty(newsVO.getMinAge())
                    && GlobalCom.isEmpty(newsVO.getMaxAge())
                    && ("O".equals(sendType) && GlobalCom.isEmpty(optionalServiceCode))) {
                flag = SmartUXProperties.getProperty("flag.notfound");
            } else {
                if ("O".equals(sendType)) {
                    if (!GlobalCom.isEmpty(optionalServiceCode)) {
                        count = mService.getIncludeSaIDCount(newsVO);
                    } else {
                        count = mService.getAllSaIDCount(newsVO);
                    }
                } else {
                    count = mService.getExcludeSaIDCount(newsVO);
                }
            }
        } catch (Exception e) {
            ExceptionHandler handler = new ExceptionHandler(e);
            flag = handler.getFlag();
        }

        String result = "{\"result\":{\"flag\":\"" + flag + "\",\"message\":\"" + count + "\"}}";

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
        return new ResponseEntity<String>(JSONSerializer.toJSON(result).toString(), responseHeaders, HttpStatus.CREATED);
    }
}
