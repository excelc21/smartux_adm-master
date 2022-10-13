package com.dmi.smartux.common.controller;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.service.CacheService;
import com.dmi.smartux.common.service.FileCacheService;
import com.dmi.smartux.common.util.CLog;
import com.dmi.smartux.common.util.CacheUtil;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.vo.ResultVO;

/**
 * 스케줄 또는 관리자 페이지에서 즉시반영시 생성되는 캐시 파일을 동기화 하는 Controller
 * 
 */
@Controller
public class CacheSyncController {

	private static final String NAS_CACHE_FOLDER = SmartUXProperties.getProperty("smartux.sync.cache.folder");
	private static final String LOCAL_CACHE_FOLDER = SmartUXProperties.getProperty("smartux.local.cache.folder");
	
	@Autowired
	protected CacheService service;
	@Autowired
	private FileCacheService fileCache;
	
	private final Log logger = LogFactory.getLog(this.getClass());
	
	/**
	 * 캐시 파일 생성시 동기화를 한다.
	 * 먼저 NAS 에 캐시 파일을 생성하고 cacheFileSync URL을 호출하면
	 * 각각의 WAS(인스턴스) 에서는 NAS에 생성된 캐시 파일을 로컬로 복사한다.
	 * 메소드에 쓰레드 동기화를 했음.
	 * 
	 * @param cacheKey 캐시키
	 * @param nasCacheNm NAS에 저장된 파일명
	 * @param localCacheNm 각각의 서버 로컬에 저장될 파일명
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/cache/cacheFileSyncNCopy", method = RequestMethod.GET)
	public ResultVO cacheFileSyncNCopy(
			@RequestParam(value = "cacheKey", required = false, defaultValue = "") String cacheKey,
			@RequestParam(value = "nasCacheNm", required = false, defaultValue = "") String nasCacheNm,
			@RequestParam(value = "localCacheNm", required = false, defaultValue = "") String localCacheNm,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		//#########[LOG SET]#########
		Log log_logger = LogFactory.getLog("cacheFileSync");
		CLog cLog = new CLog(log_logger, request);
		
		ResultVO result = new ResultVO();		

		try {
			//#########[LOG START]#########
			cLog.startLog("["+cacheKey+"]["+nasCacheNm+"]["+localCacheNm+"]");
			
			// 유효성 검사
			Map<String, String> map = new LinkedHashMap<String, String>();
			map.put("cacheKey", cacheKey);
			map.put("nasCacheNm", nasCacheNm);
			map.put("localCacheNm", localCacheNm);
			
			GlobalCom.checkValidation(map);

			// NAS에 저장된 캐시 파일 로드
			Object fileObj = CacheUtil.nasLoadCacheFile(NAS_CACHE_FOLDER + File.separator + nasCacheNm);
			
			if(fileObj != null) {
				service.putCache(cacheKey, fileObj);
			} else {
				throw new Exception("[CacheFileSyncNCopy FileNotFound ERROR]");
			}
			
			// 캐시 파일 로컬에 저장
			CacheUtil.saveCacheFile(LOCAL_CACHE_FOLDER, localCacheNm, fileObj);

			File localCacheFile = new File(CacheUtil.getCacheFilePath(LOCAL_CACHE_FOLDER, localCacheNm));			
			cLog.middleLog("[Local Cache File Size][" + localCacheFile.length() + "]");

			result.setFlag(SmartUXProperties.getProperty("flag.success"));
            result.setMessage(SmartUXProperties.getProperty("message.success"));	
				
		} catch(Exception e) {
			cLog.errorLog("["+cacheKey+"]["+nasCacheNm+"]["+localCacheNm+"]["+e.getClass().getName()+"]["+e.getMessage()+"]");
            com.dmi.smartux.common.exception.ExceptionHandler handler = new com.dmi.smartux.common.exception.ExceptionHandler(e);
			result.setFlag(handler.getFlag());
			result.setMessage(handler.getMessage());
		}
		
		//#########[LOG END]#########
		cLog.endLog("["+cacheKey+"]["+nasCacheNm+"]["+localCacheNm+"]["+result.getFlag()+"]");
		
		return result;
	}
	
	/**
	 * 캐시 동기화
	 *
	 * @param cacheName 캐시명
	 * @param fileName 캐시키
	 * @param nasFilePath NAS에 저장된 경로
	 * @param locFolderPath 로컬에 저장할 경로
	 * @param request HttpServletRequest
	 * @return 결과
	 * @throws Exception
	 */
	@RequestMapping(value = "/comm/cache/sync", method = RequestMethod.POST)
	public ResultVO cacheSync(
			@RequestParam(value="fileName", required = false, defaultValue = "") String fileName,
			@RequestParam(value="nasFilePath", required = false, defaultValue = "") String nasFilePath,
			@RequestParam(value="locFolderPath", required = false, defaultValue = "") String locFolderPath,
			HttpServletRequest request) throws Exception {

		logger.info("[/comm/cache/sync] Cache Reload Start ********** ");

		logger.info("fileName:" + fileName);
		logger.info("nasFilePath:" + nasFilePath);
		logger.info("locFolderPath:" + locFolderPath);
		
		ResultVO resultVO = new ResultVO();

		try {
			//유효성 검사
			Map<String, String> map = new LinkedHashMap<String, String>();
			map.put("fileName", fileName);		 // gpack_cache
			map.put("nasFilePath", nasFilePath); // ex) /NAS_DATA/web/smartux/sync_file_cache
			map.put("folderPath", locFolderPath);// ex)/app/smartux/file_cache

			GlobalCom.checkValidation(map);

			boolean isWriteSucceed = false;

			Map<String,Object> resultMap = new LinkedHashMap<String,Object>();
			// 1) Get Data From NAS_DATA
			resultMap = fileCache.loadFileData(nasFilePath); 

			boolean isReadSucceed = (resultMap.isEmpty()) ? false : true;

			// 2) Write a File To Local & Put Cache
			if (isReadSucceed) {
				String localFilePath = GlobalCom.getFilePathWithServerIndex(locFolderPath, fileName);
				isWriteSucceed = fileCache.writeFilePutCache(resultMap, localFilePath);
				logger.info("syncFinalResult:" + isWriteSucceed);
			}

			if (isReadSucceed && isWriteSucceed) {
				resultVO.setFlag(SmartUXProperties.getProperty("flag.success"));
				resultVO.setMessage(SmartUXProperties.getProperty("message.success"));
			} else {
				resultVO.setFlag(SmartUXProperties.getProperty("flag.cache.reload.fail"));
				resultVO.setMessage(SmartUXProperties.getProperty("message.cache.reload.fail"));
			}
			logger.info("isReadSucceed:" + isReadSucceed + " | isWriteSucceed:" + isWriteSucceed);
		} catch (Exception e) {
			logger.error("Exception[cacheSync]:" + e.getMessage());
			throw e;
		}

		logger.info("[/comm/cache/sync] Cache Reload End ********** ");
		
		return resultVO;
	}
}
