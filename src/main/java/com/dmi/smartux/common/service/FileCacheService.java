/**
 * Copyright (c) 2015 Medialog Corp. All Rights Reserved.
 *
 * @FileName : FileCacheService.java
 * @Package  : com.dmi.smartux.common.service
 * @Author   : DEOKSAN
 * @Date     : 2016. 6. 8. 오후 4:16:50
 * @Comment  :
 */

package com.dmi.smartux.common.service;

import java.io.Serializable;
import java.util.Map;

/**
 * <pre>
 * com.dmi.smartux.common.service
 *    |_ FileCacheService.java
 * </pre>
 * @date	: 2016. 6. 8. 오후 4:16:50
 * @version	:
 * @author	: deoksan
 */
/**
 * <PRE>
 * @ClassName : FileCacheService
 * @Brief     :  
 * @FileName  : FileCacheService.java
 * @Package   : com.dmi.smartux.common.service
 * @Author    : DEOKSAN
 * @Date      : 2016. 6. 8. 오후 4:16:50
 * @Comment   :
 * </PRE>
 */
public interface FileCacheService {

	// 파일 데이터 로딩 및 캐시저장
	public boolean loadFileCache(String filePath) throws Exception;
	
	// 파일 데이터 가져오기
	public Map<String,Object> loadFileData(String filePath) throws Exception;
	
//	// DB로부터 전체 데이터를 갱신
//	public boolean reloadAllCacheFromDb(String cacheKey, String filePath) throws Exception;
//	
//	// 전체 데이터 갱신(스케줄러)
//	public boolean reloadAllCache(String cacheKey, String filePath) throws Exception;
	
	// 데이터 부분갱신(즉시반영)
	public boolean updateAllCache(String filePath, Map<String,Object> dataObj, String packId) throws Exception;
	
	// 데이터 파일 저장
	public boolean writeFileCache(Serializable object, String filePath) throws Exception;

	// 데이터 파일 저장 및 캐시 저장
	public boolean writeFilePutCache(Map<String,Object> object, String filePath) throws Exception;
		
	// 파일 캐시 데이터 조회
	public Map<String,Object> getFileCache(String filePath);
	
	public boolean fileCacheJob(String packId);
	
}
