/**
 * Copyright (c) 2015 Medialog Corp. All Rights Reserved.
 *
 * @FileName : FileCacheServiceImpl.java
 * @Package  : com.dmi.smartux.common.service.impl
 * @Author   : DEOKSAN
 * @Date     : 2016. 6. 8. 오후 4:17:04
 * @Comment  :
 */

package com.dmi.smartux.common.service.impl;

import java.io.File;
import java.io.Serializable;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmi.smartux.admin.gpack.pack.service.GPackPackViewService;
import com.dmi.smartux.common.property.MimsConsts;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.service.CacheService;
import com.dmi.smartux.common.service.FileCacheService;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.vo.RequestVO;
import com.dmi.smartux.common.vo.ResultVO;
import com.dmi.smartux.gpack.category.dao.GpackCategoryInfoDao;
import com.dmi.smartux.gpack.pack.dao.GpackPackInfoDao;
import com.dmi.smartux.gpack.promotion.dao.GpackPromotionDao;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * <pre>
 * com.dmi.smartux.common.service.impl
 *    |_ FileCacheServiceImpl.java
 * </pre>
 * @date	: 2016. 6. 8. 오후 4:17:04
 * @version	:
 * @author	: deoksan
 */
/**
 * <PRE>
 * @ClassName : FileCacheServiceImpl
 * @Brief     :  
 * @FileName  : FileCacheServiceImpl.java
 * @Package   : com.dmi.smartux.common.service.impl
 * @Author    : DEOKSAN
 * @Date      : 2016. 6. 8. 오후 4:17:04
 * @Comment   :
 * </PRE>
 */
@Service("FileCacheService")
public class FileCacheServiceImpl implements FileCacheService {

	private final Log logger = LogFactory.getLog("gpackFileCache");

	private Kryo mKryo = new Kryo();
	
	public static final long GC_CHECK_SIZE = 1024 * 1024 * 50; // 50MB

	private static boolean isNotFirst = false;
	
	private static String packInfoCacheKey = SmartUXProperties.getProperty("GpackCategoryInfoDao.getPackInfo.cacheKey");
	private static String packPromotionCacheKey = SmartUXProperties.getProperty("GpackPackPromotionDao.getPackPromotion.cacheKey");
	private static String packCategoryCacheKey = SmartUXProperties.getProperty("GpackCategoryInfoDao.getPackCategory.cacheKey");
	
	@Autowired
	private CacheService cacheService;
	
	@Autowired
	private GpackPackInfoDao packInfoDao;
	
	@Autowired
	private GpackPromotionDao promotionDao;
	
	@Autowired
	private GpackCategoryInfoDao categoryDao;
	
	@Autowired
	GPackPackViewService packViewService;
	
	/**
	 * 아래 주석처리한 메소드들.. API와 관리자가 분리되면서 쓰면 안되는거 같은데? API와 관리자 양쪽에서 똑같은 init메소드를 호출한다..
	 * 캐시 스케쥴은 API에 넣은거 같으니 일단 관련된 부분만 좀 정리 함.
	 * 소스 수정할일 있으면 양쪽 소스를 다 수정해야 하도록 되어 있는데 이거 정리할 필요성 있어 보임..
	 */
//	@PostConstruct
//	public void initGpackService() {
//		
//		logger.info("[initGpackService] *********************** [START]");
//		logger.info("[CASE]:initializing !!!");
//		
//		/* ***************
//		 * 캐시 데이터 초기화
//		 * ***************
//		 * 1) 로컬 파일 존재 확인
//		 * 2) 존재: 파일 로딩 -> 캐시 갱신
//		 * 3) 미존재: DB조회 -> 캐시 갱신 -> 파일 저장(로컬)
//		 * */
//		
//		boolean isSucceed = false;
//
//		String cacheKey = "gpack_cache";
//		String folderPath = SmartUXProperties.getProperty("smartux.local.cache.folder");
//		String filePath = GlobalCom.getFilePathWithServerIndex(folderPath, cacheKey);
//		
//		try {
//			// 로컬 파일 존재 확인 및 캐시 갱신
//			isSucceed = loadFileCache(filePath);
//		} catch (Exception e) {
//			logger.info("[Exception]loadFileCache:" + e.getMessage());
//		}
//
//		if (!isSucceed) {
//			// DB조회 및 캐시 갱신 후 파일 저장(로컬)
//			try {
//				isSucceed = reloadAllCacheFromDb(cacheKey, filePath);
//			} catch (Exception e) {
//				logger.info("[Exception]reloadAllCacheFromDb:" + e.getMessage());
//			}
//		}
//		logger.info("isSucceed:" + isSucceed);
//		logger.info("[initGpackService] *********************** [END]");
//	}
//	
//	@Override
//	public boolean reloadAllCacheFromDb(String cacheKey, String filePath) throws Exception {
//		
//		boolean isWriteSucceed = false;
//		boolean cacheUpdate = true;
//
//		Map<String, Object> fileData = new LinkedHashMap<String, Object>();
//		
//		int retrycnt = Integer.parseInt(SmartUXProperties.getProperty("scheduler.retrycnt"));
//		try {
//			for (int i=0;i<retrycnt;i++) {
//				
//				fileData = getGpackAllData();
//				
//				if (!fileData.isEmpty()) {
//					cacheService.putCache(fileData);
//					break;
//				} else {
//					logger.info("There is no data..");
//				}
//			}
//			
//		} catch (Exception e) {
//			cacheUpdate = false;
//			logger.error("Exception[reloadAllCacheFromDb]:" + e.getMessage());
//		}
//
//		if (cacheUpdate) {
//			// 3) Write File
//			isWriteSucceed = writeFileCache((Serializable) fileData, filePath);
//		}
//
//		return isWriteSucceed;
//	}
//	
//	@Override
//	public boolean reloadAllCache(String cacheKey, String filePath) throws Exception {
//
//		boolean isSucceed = false;
//		boolean isUpdate = reloadAllCacheFromDb(cacheKey, filePath);
//		
//		if (isUpdate) {
//
//			// 로컬파일 데이터 조회
//			Map<String,Object> fileData = getFileCache(filePath);
//			logger.info("*******************************");
//			for (String key : fileData.keySet()) {
//				logger.info("key:" + key + " | " + fileData.get(key));
//			}
//			// 로컬파일 데이터 갱신
//			isSucceed = writeFilePutCache(fileData, filePath);
//			logger.info("isSucceed:" + isSucceed);
//			
//			if (isSucceed) {
//				logger.info("캐시 갱신성공!!!");
//				syncFileCache("gpack_cache");
//				isSucceed = true;
//			} else {
//				logger.info("캐시 갱신실패!!!");
//			}
//			logger.info("*******************************");			
//		}
//		
//		return isSucceed;
//	}
//	
//	/**
//	 * 모든 Gpack 데이터 DB 조회 후 리턴
//	 * @return
//	 * @throws Exception
//	 */
//	public Map<String, Object> getGpackAllData() {
//
//		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
//		try {
//			String[] packIdArr = SmartUXProperties.getProperty("gpack.cache.pack.id.all").split(",");
//			
//			for (String packId : packIdArr) {
//				
//				Object packInfoObj = packInfoDao.getGpackPackInfo(packId);
//				Object packPromObj = promotionDao.getPackPromotion(packId);
//				Object packCateObj = categoryDao.getPackCategory(packId);
//				
//				resultMap.put(packInfoCacheKey + "-" + packId, packInfoObj);
//				resultMap.put(packPromotionCacheKey + "-" + packId, packPromObj);
//				resultMap.put(packCategoryCacheKey + "-" + packId, packCateObj);
//			}
//		} catch (Exception e) {
//			logger.info("Exception[getGpackAllData]:" + e.getMessage());
//			resultMap = null;
//		}
//		
//		return resultMap;
//	}
//	
//	public void schedulerGpackService() throws Exception {
//
//		logger.info("[schedulerGpackService] ****************************** [START]");
//		logger.info("[CASE]:scheduling !!! | isNotFirst:" + isNotFirst);
//		if (isNotFirst) {
//		
//			String cacheKey = "gpack_cache";
//			String folderPath = SmartUXProperties.getProperty("smartux.local.cache.folder");
//			String filePath = GlobalCom.getFilePathWithServerIndex(folderPath, cacheKey);
//			
//			/* ***************
//			 * 파일캐시 전체갱신
//			 * ***************
//			 * 1) 본인차례 여부확인 
//			 * 2) 본인차례 YES: 파일캐시 전체갱신 -> 캐시동기화
//			 * 3) 본인차례 NO: 파일캐시 로딩 -> 캐시 저장
//			 * */
//			
//			String[] packIdArr = SmartUXProperties.getProperty("gpack.cache.pack.id.all").split(",");
//			
//			for (String packId : packIdArr) {
//				packViewService.insertGpackBiz(packId, "schedule");
//			}
//			boolean isSucceed = false; 
//			boolean isMyTurn = GlobalCom.isMyTurn(Calendar.HOUR);
//			
//			logger.info("Your Turn???");
//			
//			if (isMyTurn) {
//				
//				logger.info("Yes!!! It's My Turn!!! (^0^) ");
//				
//				// 파일캐시 전체 갱신
//				try {
//					logger.info(" >> DB조회 후 캐시 갱신 및 파일 생성한다~!!!");
//					isSucceed = reloadAllCacheFromDb("gpack_cache", filePath);
//				} catch (Exception e) {
//					logger.error("Exception[schedulerGpackService]:" + e.getMessage());
//				}
//				// 캐시동기화
//				if (isSucceed) {
//					logger.info(" >> 캐시 갱신성공!!!");
//					
//					// 로컬파일 데이터 조회
//					syncFileCache("gpack_cache");
//					
//					isSucceed = true;
//				} else {
//					logger.info(" >> 캐시 갱신실패!!!");
//				}
//				
//			} else {
//				logger.info("No... Not Me...(--^) ");
//			}
//			logger.info("isSucceed:" + isSucceed);
//		} else {
//			isNotFirst = true;
//		}
//		
//		logger.info("[schedulerGpackService] ****************************** [END]");
//		
//	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean loadFileCache(String filePath) throws Exception {

		boolean isLoadSucceed = false;

		if (StringUtils.isNotBlank(filePath)) {
			Map<String,Object> resultMap;
			File file = null;

			try {
				file = new File(filePath);

				if (file.exists()) {
					try {
						Input input = new Input(FileUtils.openInputStream(file));
						resultMap = (Map<String, Object>) mKryo.readClassAndObject(input);
						input.close();
						
						// 캐시저장
						cacheService.putCache(resultMap);
						/* 데이터 확인용
						for (String key : resultMap.keySet()) {
							logger.info("key:" + key);
							logger.info("val:" + resultMap.get(key));
						}
						*/
						isLoadSucceed = true;
					} catch (Exception e) {
						logger.error("Exception[loadFileCache]:" + e.getMessage());
					}
					
					
				} else {
					logger.info("Does not exist File");
				}
				
			} finally {
				// 즉시 gc가 되는건 아니지만 메모리 관리를 위해 50M가 넘어가면 gc 요청
				if (file != null && file.length() > GC_CHECK_SIZE) {
					file = null;
					resultMap = null;
					System.gc();
				}
			}
		} else {
			logger.info("filePath is Blank");
		}

		return isLoadSucceed;
		
	}

	@Override
	public Map<String, Object> loadFileData(String filePath) throws Exception {
		
		Map<String,Object> resultMap = new LinkedHashMap<String,Object>();
		
		if (StringUtils.isNotBlank(filePath)) {
			File file = null;

			try {
				file = new File(filePath);

				if (file.exists()) {
					try {
						Input input = new Input(FileUtils.openInputStream(file));
						resultMap = (Map<String, Object>) mKryo.readClassAndObject(input);
						input.close();
						
					} catch (Exception e) {
						logger.error("Exception[loadFileData]:" + e.getMessage());
					}
					
				} else {
					logger.info("Does not exist File");
				}
			} finally {
				// 즉시 gc가 되는건 아니지만 메모리 관리를 위해 50M가 넘어가면 gc 요청
				if (file != null && file.length() > GC_CHECK_SIZE) {
					file = null;
					resultMap = null;
					System.gc();
				}
			}
		} else {
			logger.info("filePath is Blank");
		}
		
		return resultMap;
	}

	@Override
	public boolean updateAllCache(String filePath, Map<String,Object> dataObj, String packId) throws Exception {

		// 로컬파일 데이터 조회
		Map<String,Object> fileData = getFileCache(filePath);
		
		// 로컬파일 데이터 수정
		for (String key : fileData.keySet()) {
			
			for (String dataKey : dataObj.keySet()) {
				if (key.equals(dataKey)) {
					fileData.put(dataKey, dataObj.get(dataKey));
					break;
				}
			}
			
		}
		
		/**
		 * 관리자는 캐시가 없으니 이부분도 필요없다.
		 */
		// 로컬파일 데이터 갱신
		boolean isSucceed = writeFilePutCache(fileData, filePath);
		
		logger.info("캐시 갱신성공!!!");
		syncFileCache("gpack_cache");
		
		return true;
	}
	
	@Override
	public boolean writeFilePutCache(Map<String,Object> fileData, String filePath) throws Exception {
		cacheService.putCache(fileData);
		return writeFileCache((Serializable)fileData, filePath);
	}
	
	@Override
	public boolean writeFileCache(Serializable fileData, String filePath) throws Exception {

		logger.info("[writeFileCache] ********************** ");
		logger.info("[filePath]:" + filePath);
		boolean isWriteSucceed = false;

		if (StringUtils.isNotBlank(filePath)) {

			if (fileData != null) {
				File file = null;

				try {
					
					file = new File(filePath);

					GlobalCom.makeParentDir(filePath);

					Output output = new Output(FileUtils.openOutputStream(file));
					mKryo.writeClassAndObject(output, fileData);
					output.close();

					if (file.exists()) {
						isWriteSucceed = true;
						logger.info("---------------------------------");
						logger.info("filePath:"+filePath);
						logger.info("isWriteSucceed:" + isWriteSucceed);
						logger.info("---------------------------------");
					} else {
						logger.info("There is no file..");
					}
					
				} catch (Exception e) {
					logger.error("Exception[writeFileCache]:" + e.getMessage());
					
				}finally {
					
					// 즉시 gc가 되는건 아니지만 메모리 관리를 위해 50M가 넘어가면 gc 요청
					if (file != null && file.length() > GC_CHECK_SIZE) {
						file = null;
						System.gc();
					}
				}
			} else {
				logger.info("fileData is null");
			}
			
		} else {
			logger.info("filePath is blank");
		}

		return isWriteSucceed;
	}

	@Override
	public Map<String, Object> getFileCache(String filePath) {
		
		Map<String,Object> resultMap = null;
		
		if (StringUtils.isNotBlank(filePath)) {
			
			File file = null;

			try {
				file = new File(filePath);

				if (file.exists()) {
					try {
						Input input = new Input(FileUtils.openInputStream(file));
						resultMap = (Map<String, Object>) mKryo.readClassAndObject(input);
						input.close();
						
					} catch (Exception e) {
						logger.error("Exception[getFileCache]:" + e.getMessage());
					}
					
					
				} else {
					logger.info("Does not exist File");
				}
			} finally {
				// 즉시 gc가 되는건 아니지만 메모리 관리를 위해 50M가 넘어가면 gc 요청
				if (file != null && file.length() > GC_CHECK_SIZE) {
					file = null;
					resultMap = null;
					System.gc();
				}
			}
		} else {
			logger.info("filePath is Blank");
		}
	
		return resultMap;
	}
	
	public Map<String, Object> getGpackAllData(String packId) throws Exception{

		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
		
		Object packInfoObj = packInfoDao.getGpackPackInfo(packId);
		Object packPromObj = promotionDao.getPackPromotion(packId);
		Object packCateObj = categoryDao.getPackCategory(packId);
		
		resultMap.put(packInfoCacheKey + "-" + packId, packInfoObj);
		resultMap.put(packPromotionCacheKey + "-" + packId, packPromObj);
		resultMap.put(packCategoryCacheKey + "-" + packId, packCateObj);
		
		return resultMap;
	}
	
	/**
     * 다른 인스턴스에게 캐시 전달
     *
     * @param cacheName 캐시명
     * @param cacheKey 캐시키
     * @return ResultVO
     * @throws Exception
     */
    private ResultVO syncFileCache(String cacheKey) throws Exception {
    	
        RequestVO requestVO = new RequestVO();
        requestVO.setUrl(SmartUXProperties.getProperty("default.cache.reload.url"));
        requestVO.setMethod(SmartUXProperties.getProperty("default.cache.reload.method"));
        requestVO.setRetryCount(NumberUtils.toInt(SmartUXProperties.getProperty("gpack.cache.http.retry.count"), 0));
        requestVO.setTimeout(NumberUtils.toLong(SmartUXProperties.getProperty("gpack.cache.http.timeout"), 3000));
        requestVO.putParams("fileName", cacheKey);
        requestVO.putParams("locFolderPath", SmartUXProperties.getProperty("smartux.local.cache.folder"));
        requestVO.putParams("filter", MimsConsts.SyncNasFilter.REPLACE_FILTER.getValue());

        String nasFolderPath = SmartUXProperties.getProperty("smartux.sync.cache.folder");
        return syncFileCache(requestVO, cacheKey, nasFolderPath);
    }
    
	public ResultVO syncFileCache(RequestVO requestVO, String cacheKey, String nasFolderPath)
			throws Exception {
		
		logger.info("[syncFileCache] ################## [START]");
		logger.info("[CASE]:syncronizing !!!");
		
		boolean isSucceed = false;

		Map<Integer, String> resultMap = new HashMap<Integer, String>();

		if (StringUtils.isNotBlank(nasFolderPath)) {
			StrBuilder sb = new StrBuilder();
			sb.append(nasFolderPath);

			if (!nasFolderPath.endsWith(File.separator)) {
				sb.append(File.separator);
//				sb.append("/");
			}

			sb.append(cacheKey);

			String locFilePath = GlobalCom.getFilePathWithServerIndex(requestVO.getParams().get("locFolderPath"),cacheKey);
			// 로컬파일 데이터 조회
			Map<String,Object> fileData = getFileCache(locFilePath);
			
			// Write NAS File
			isSucceed = writeFileCache((Serializable) fileData, sb.toString());

			if (isSucceed) {
				File nasCacheFile = new File(sb.toString());

				if (nasCacheFile.exists()) {
					requestVO.putParams("nasFilePath", URLEncoder.encode(nasCacheFile.getAbsolutePath(), "UTF-8"));
				}
				
				/**
				 * 필요없는 로직인거 같음.				
				 */
//				String currentIp = GlobalCom.getSystemProperty("JBOSS_IP");
//
//				if (StringUtils.isBlank(currentIp)) {
//					currentIp = "127.0.0.1";
//				}
//
//				String currentPort = GlobalCom.getSystemProperty("JBOSS_PORT");
//
//				if (StringUtils.isBlank(currentPort)) {
//					currentPort = "8180";
//				}
//
//				String currentUri = currentIp + ":" + currentPort;
//				logger.info("currentUri:" + currentUri);
				
				String hosts = SmartUXProperties.getProperty("cache.ServerIPList");
				String[] hostList = hosts.split("\\|", -1);

				String ports = SmartUXProperties.getProperty("cache.ServerPortList");
				String[] portList = ports.split("\\|", -1);

				int idx = 0;

				for (String host : hostList) {
					logger.info("host:" + host + " | port:" + NumberUtils.toInt(portList[idx]));
					requestVO.setHost(host);
					requestVO.setPort(NumberUtils.toInt(portList[idx]));

					idx++;
					String resultCode = callRetryHttpRequest(requestVO);
					resultMap.put(idx, resultCode);
				}

				// 캐시 파일 동기화 완료 후 NAS에 있는 임시 캐시파일은 삭제
				nasCacheFile.delete();

				// 생성된지 1시간이 지난 쓰레기 캐시 파일은 모두 삭제
				// 시스템 다운등의 이유로 쓰레기 캐시 파일이 있을 수 있음
				deleteExpiredFile(nasFolderPath, 1000 * 60 * 60);
			}
		} else {
			logger.info("folderPath is Blank");
		}
		logger.info("[syncFileCache] ################## [END]");
		return convertResultMapToResultVO(resultMap);
	}
	
	/**
     * callHttpRequest()와 retryHttpRequest()를 래핑하고 있는 함수
     *
     * @param requestVO Request 정보를 담고 있는 객체
     * @return 결과 코드
     */
    private String callRetryHttpRequest(RequestVO requestVO) {
        String resultCode = SmartUXProperties.getProperty("flag.etc");
        String retryResultCode;

        try {
            resultCode = callHttpRequest(requestVO);

            boolean isSucceed = SmartUXProperties.getProperty("flag.success").equals(resultCode);

            if (isSucceed) {
                return resultCode;
            } else {
                retryResultCode = retryHttpRequest(requestVO);

                if (StringUtils.isNotBlank(retryResultCode)) {
                    resultCode = retryResultCode;
                }
            }
        } catch (Exception e) {
            retryResultCode = retryHttpRequest(requestVO);

            if (StringUtils.isNotBlank(retryResultCode)) {
                resultCode = retryResultCode;
            }
        }

        return resultCode;
    }
    
	/**
     * 해당 폴더내에 checkTime 이후의 파일을 삭제한다
     *
     * @param folderPath 폴더 경로
     * @param checkTime  체크 시간
     */
    private void deleteExpiredFile(String folderPath, long checkTime) {
        File nasCacheFolder = new File(folderPath);
        //noinspection unchecked
        Collection<File> files = FileUtils.listFiles(nasCacheFolder, null, false);

        if (CollectionUtils.isNotEmpty(files)) {
            for (File file : files) {
                if (file != null && file.exists()) {
                    if ((System.currentTimeMillis() - file.lastModified()) >= checkTime) {
                        //noinspection ResultOfMethodCallIgnored
                        file.delete();
//                        mLogger.info(LogUtil.format("deleteExpiredFile", file.getAbsolutePath()));
                    }
                }
            }
        }
    }
    
	/**
     * 서버인덱스, 결과코드 Map을 ResultVO 형태로 변환한다
     *
     * @param resultMap 서버인덱스, 결과코드 Map
     * @return ResultVO
     */
    private ResultVO convertResultMapToResultVO(Map<Integer, String> resultMap) {
        ResultVO resultVO = new ResultVO();

        boolean isSucceed = true;

        StrBuilder sb = new StrBuilder();

        for (Map.Entry<Integer, String> entry : resultMap.entrySet()) {
            String resultCode = entry.getValue();
            int serverIndex = entry.getKey();

            sb.append("[").append(serverIndex).append(":").append(resultCode).appendln("]");

            if (isSucceed && !SmartUXProperties.getProperty("flag.success").equals(resultCode)) {
                isSucceed = false;
            }
        }

        if (isSucceed) {
            resultVO.setFlag(SmartUXProperties.getProperty("flag.success"));
            resultVO.setMessage(SmartUXProperties.getProperty("message.success"));
        } else {
            resultVO.setFlag(SmartUXProperties.getProperty("flag.etc"));
            resultVO.setMessage(sb.toString());
        }

        return resultVO;
    }
    
    /**
     * Http Request를 재시도한다
     *
     * @param requestVO Request 정보를 담고 있는 객체
     * @return 결과 코드
     */
    private String retryHttpRequest(RequestVO requestVO) {
        String resultCode = null;

        int retryCount = requestVO.getRetryCount();

        for (int i = 0; i < retryCount; i++) {
            resultCode = callHttpRequest(requestVO);

            boolean isSucceed = SmartUXProperties.getProperty("flag.success").equals(resultCode);

            if (isSucceed) {
                return resultCode;
            }
        }

        return resultCode;
    }
    
    /**
     * Http Request Call
     *
     * @param requestVO Request 정보를 담고 있는 객체
     * @return 결과 코드
     */
    private String callHttpRequest(RequestVO requestVO) {
        String resultCode = SmartUXProperties.getProperty("flag.etc");

        String host = requestVO.getHost();
        int port = requestVO.getPort();
        String url = requestVO.getUrl();
        String params = requestVO.getFormattedParam();
        int timeout = (int) requestVO.getTimeout();
        String protocolName = requestVO.getProtocolName();
        String method = requestVO.getMethod();

        try {
//            mLogger.info(LogUtil.format("Start", "callHttpRequest", requestVO));

            String result = GlobalCom.callHttpClient(host, port, url, params, timeout, protocolName, method);

            if (result.length() >= 4) {
                resultCode = result.substring(0, 4);
            }

//            mLogger.info(LogUtil.format("End", "callHttpRequest", resultCode));
        } catch (Exception e) {
//            mLogger.error(LogUtil.format("Error", "callHttpRequest"), e);
        }

        return resultCode;
    }

	@Override
	public boolean fileCacheJob(String packId) {

		logger.info("[fileCacheJob] ****************************** [START]");
		logger.info("[CASE]:applying Now !!!");
		String folderPath = SmartUXProperties.getProperty("smartux.local.cache.folder");
		String filePath = GlobalCom.getFilePathWithServerIndex(folderPath, "gpack_cache");
		
		Map<String,Object> resultMap = new LinkedHashMap<String,Object>();
		
		try {
			resultMap = getGpackAllData(packId);
		} catch (Exception e) {
			logger.error("Exception[fileCacheJob]getGpackAllData:" + e.getMessage());
		}

		boolean isSucceed = false;
		
		// 부분 캐시 갱신
		try {
			isSucceed = updateAllCache(filePath, resultMap, packId);
			logger.info("[isSucceed]:" + isSucceed);
		} catch (Exception e) {
			logger.error("Exception[fileCacheJob]updateAllCache:" + e.getMessage());
		}
		logger.info("[fileCacheJob] ****************************** [END]");
		
		return isSucceed;
	}

}
