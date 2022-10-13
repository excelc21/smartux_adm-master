package com.dmi.smartux.common.service.impl;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.service.CacheService;
import com.dmi.smartux.common.util.CacheUtil;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.util.StringUtil;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

@Service
public class CacheServiceImpl implements CacheService {

	// private final Log logger = LogFactory.getLog(this.getClass());
	// private Log logger;

	@Autowired
	CacheManager cachemanager;

	@Override
	public void putCache ( String key, Object data ) throws Exception {
		Cache cache = cachemanager.getCache ( "selectCache" );
		cache.put ( new Element ( key, data ) );
	}
	
	@Override
	public void putCache(Map<String, Object> data) throws Exception {
		for (String key : data.keySet()) {
			Cache cache = cachemanager.getCache ( "selectCache" );
			cache.put(new Element(key, data.get(key)));
		}
	}
	
//	@Override
//	public Object getCache(String key) throws Exception {
//		Cache cache = cachemanager.getCache("selectCache");
//		return cache.get(key).getObjectValue();
//	}
	
	@Override
	public Object getCache(String key) throws Exception {
		Cache cache = cachemanager.getCache("selectCache");
		Element elm = cache.get(key);
		return (elm == null) ? null : elm.getObjectValue();
	}

	@Override
	public Object getCache ( Object obj, String methodname, String cachekey, long interval, Object... objparam ) throws Exception {
		// TODO Auto-generated method stub
		Log logger;

		if ( cachekey.indexOf ( "getRealRating" ) != -1 ) {
			logger = LogFactory.getLog ( "getRealRating" );
		} else if ( cachekey.indexOf ( "getThemeInfo" ) != -1 ) {
			logger = LogFactory.getLog ( "getThemeInfo" );
		} else if ( cachekey.indexOf ( "getConfiguration" ) != -1 ) {
			logger = LogFactory.getLog ( "getConfiguration" );
		} else if ( cachekey.indexOf ( "getYoutubeSearchKey" ) != -1 ) {
			logger = LogFactory.getLog ( "getYoutubeSearchKey" );
		} else if ( cachekey.indexOf ( "getI20AlbumList" ) != -1 ) {
			logger = LogFactory.getLog ( "getI20AlbumList" );
		} else if ( cachekey.indexOf ( "getMainPanelInterlockingInfo" ) != -1 ) {
			logger = LogFactory.getLog ( "getMainPanelInterlockingInfo" );
		} else if ( cachekey.indexOf ( "getMainPanelVersionInfo" ) != -1 ) {
			logger = LogFactory.getLog ( "getMainPanelVersionInfo" );
		} else if ( cachekey.indexOf ( "getGenreVodBestList" ) != -1 ) {
			logger = LogFactory.getLog ( "getGenreVodBestList" );
		} else if ( cachekey.indexOf ( "getSmartStartItemList" ) != -1 ) {
			logger = LogFactory.getLog ( "getSmartStartItemList" );
		} else if ( cachekey.indexOf ( "getSUXMAlbumList" ) != -1 ) {
			logger = LogFactory.getLog ( "getSUXMAlbumList" );
		} else if ( cachekey.indexOf ( "getAhomeInfoAgreeText" ) != -1 ) {
			logger = LogFactory.getLog ( "getAhomeInfoAgreeText" );
		} else {
			logger = LogFactory.getLog ( this.getClass ( ) );
		}

		// this.setLogger(cachekey);
		logger.debug ( "cacheAPIName  : " + cachekey );

		Object result = null;
		Cache cache = cachemanager.getCache ( "selectCache" );
		StringBuffer sbCachekey = new StringBuffer ( cachekey );

		// 고유한 캐쉬키를 만들기 위해 그 당시 사용했던 파라미터값을 같이 조합하여 캐쉬키를 유니크하게 만든다(파라미터 값을 -(하이픈)으로 연결하여 캐쉬키를 만든다)
		if ( ( objparam != null ) && ( objparam.length > 0 ) ) {
			for ( Object param : objparam ) {
				sbCachekey.append ( "-" );
				sbCachekey.append ( param );
			}
		}

		String useCachekey = sbCachekey.toString ( );
		logger.debug ( "사용 캐쉬키  : " + useCachekey );

		Element element = cache.get ( useCachekey );

		if ( element == null ) {
			logger.debug ( "캐쉬에 없어서 새로 넣는다" );
			logger.debug ( "생성때 currentTimeMillis : " + System.currentTimeMillis ( ) );

			try {
				result = getData ( obj, methodname, objparam );
				cache.put ( new Element ( useCachekey, result ) );
				logger.info ( "[CACHE WRITE][" + cachekey + "] " + result );
			} catch ( Exception e ) {
				logger.error ( e.getCause ( ).getMessage ( ) );
			}
		} else {
			logger.debug ( "캐쉬꺼 꺼내오기" );
			logger.debug ( "getCreationTime : " + element.getCreationTime ( ) );
			logger.debug ( "검색때 currentTimeMillis : " + System.currentTimeMillis ( ) );

			if ( interval == 0 ) { // interval이 0일때는 갱신간격 체크를 하지 말고 바로 캐쉬에서 가져와서 리턴한다
				result = element.getObjectValue ( );
			} else if ( interval == -1 ) { // interval이 -1일때는 무조건 조회한뒤에 캐쉬에 넣고 리턴
				try {
					result = getData ( obj, methodname, objparam );
					cache.put ( new Element ( useCachekey, result ) );
					logger.info ( "[CACHE WRITE][" + cachekey + "] " + result );
				} catch ( Exception e ) {
					logger.error ( e.getCause ( ).getMessage ( ) );
					result = element.getObjectValue ( );
				}
			} else { // interval이 0이 아닐때는 갱신간격 체크를 해서 DB에서 조회해서 캐시에 넣거나 또는 바로 캐쉬에서 가져와서 리턴한다
				long timeoutgap = System.currentTimeMillis ( ) - element.getCreationTime ( );
				logger.debug ( "timeoutgap : " + timeoutgap );
				if ( timeoutgap >= interval ) {
					try {
						result = getData ( obj, methodname, objparam );
						cache.put ( new Element ( useCachekey, result ) );
						logger.info ( "[CACHE WRITE][" + cachekey + "] " + result );
					} catch ( Exception e ) {
						logger.error ( e.getMessage ( ) );
						result = element.getObjectValue ( );
					}
				} else {
					result = element.getObjectValue ( );
				}
			}

		}

		return result;
	}
	
	@Override
	public Object getCache ( String cachename, Object obj, String methodname, String cachekey, long interval, Object... objparam ) throws Exception {
		// TODO Auto-generated method stub
		Log logger;
		logger = LogFactory.getLog ( this.getClass ( ) );

		Object result = null;
		Cache cache = cachemanager.getCache (cachename);
		StringBuffer sbCachekey = new StringBuffer (cachekey);

		// 고유한 캐쉬키를 만들기 위해 그 당시 사용했던 파라미터값을 같이 조합하여 캐쉬키를 유니크하게 만든다(파라미터 값을 -(하이픈)으로 연결하여 캐쉬키를 만든다)
		if ( ( objparam != null ) && ( objparam.length > 0 ) ) {
			for ( Object param : objparam ) {
				sbCachekey.append ( "-" );
				sbCachekey.append ( param );
			}
		}

		String useCachekey = sbCachekey.toString ( );
		logger.debug ( "사용 캐쉬키  : " + useCachekey );

		Element element = cache.get ( useCachekey );

		if ( element == null ) {
			logger.debug ( "캐쉬에 없어서 새로 넣는다" );
			logger.debug ( "생성때 currentTimeMillis : " + System.currentTimeMillis ( ) );

			try {
				result = getData ( obj, methodname, objparam );
				cache.put ( new Element ( useCachekey, result ) );
				logger.info ( "[CACHE WRITE][" + cachekey + "] " + result );
			} catch ( Exception e ) {
				logger.error ( e.getCause ( ).getMessage ( ) );
			}
		} else {
			logger.debug ( "캐쉬꺼 꺼내오기" );
			logger.debug ( "getCreationTime : " + element.getCreationTime ( ) );
			logger.debug ( "검색때 currentTimeMillis : " + System.currentTimeMillis ( ) );

			if ( interval == 0 ) { // interval이 0일때는 갱신간격 체크를 하지 말고 바로 캐쉬에서 가져와서 리턴한다
				result = element.getObjectValue ( );
			} else if ( interval == -1 ) { // interval이 -1일때는 무조건 조회한뒤에 캐쉬에 넣고 리턴
				try {
					result = getData ( obj, methodname, objparam );
					cache.put ( new Element ( useCachekey, result ) );
					logger.info ( "[CACHE WRITE][" + cachekey + "] " + result );
				} catch ( Exception e ) {
					logger.error ( e.getCause ( ).getMessage ( ) );
					result = element.getObjectValue ( );
				}
			} else { // interval이 0이 아닐때는 갱신간격 체크를 해서 DB에서 조회해서 캐시에 넣거나 또는 바로 캐쉬에서 가져와서 리턴한다
				long timeoutgap = System.currentTimeMillis ( ) - element.getCreationTime ( );
				logger.debug ( "timeoutgap : " + timeoutgap );
				if ( timeoutgap >= interval ) {
					try {
						result = getData ( obj, methodname, objparam );
						cache.put ( new Element ( useCachekey, result ) );
						logger.info ( "[CACHE WRITE][" + cachekey + "] " + result );
					} catch ( Exception e ) {
						logger.error ( e.getMessage ( ) );
						result = element.getObjectValue ( );
					}
				} else {
					result = element.getObjectValue ( );
				}
			}

		}

		return result;
	}
	
	@Override
	public boolean deleteCache(String cacheName, String cachekey, Object... objparam) throws Exception {
		
		Log logger = LogFactory.getLog(this.getClass());
		logger.debug("cacheAPIName  : " + cachekey);
		
		if (GlobalCom.isNull(cacheName).isEmpty()) {
			cacheName = "selectCache";
		}
		
		Cache cache = cachemanager.getCache(cacheName);
		
		StringBuffer sbCachekey = new StringBuffer(cachekey);
		// 고유한 캐쉬키를 만들기 위해 그 당시 사용했던 파라미터값을 같이 조합하여 캐쉬키를 유니크하게 만든다(파라미터 값을 -(하이픈)으로 연결하여 캐쉬키를 만든다)
		if((objparam != null) && (objparam.length > 0)){
			for(Object param : objparam){
				sbCachekey.append("-");
				sbCachekey.append(param);
			}
		}

		String useCachekey = sbCachekey.toString();
		logger.debug("사용 캐쉬키  : " + useCachekey);
		
		boolean isDel = cache.remove(useCachekey);
		
		return isDel;
	}

//	@Override
//	public Object getCache ( String cachekey, Object... objparam ) throws Exception {
//		// TODO Auto-generated method stub
//		Object result = null;
//		Cache cache = cachemanager.getCache ( "selectCache" );
//		StringBuffer sbCachekey = new StringBuffer ( cachekey );
//
//		// 고유한 캐쉬키를 만들기 위해 그 당시 사용했던 파라미터값을 같이 조합하여 캐쉬키를 유니크하게 만든다(파라미터 값을 -(하이픈)으로 연결하여 캐쉬키를 만든다)
//		if ( ( objparam != null ) && ( objparam.length > 0 ) ) {
//			for ( Object param : objparam ) {
//				sbCachekey.append ( "-" );
//				sbCachekey.append ( param );
//			}
//		}
//
//		String useCachekey = sbCachekey.toString ( );
//
//		Element element = cache.get ( useCachekey );
//
//		// 캐쉬에 저장되어 있을 경우
//		if ( element != null ) {
//			result = element.getObjectValue ( );
//		}
//
//		return result;
//
//	}

	private Object getData ( Object obj, String methodname, Object... objparam ) throws Exception {

		Class clazz = obj.getClass ( );
		Class[] args = new Class[objparam.length];
		
		for ( int i = 0; i < args.length; i++ ) {
			// logger.debug("Class name : " + objparam[i].getClass().getName());
			/*
			 * Java Reflection에서 method에 파라미터 사용시 해당 파라미터 타입이 int(primitive 타입)를 사용하든 Integer를 사용하든 똑같이 Integer 클래스로
			 * 인식한다 때문에 캐쉬를 사용하는 함수의 파라미터는 primitive 함수로 설계해야 한다
			 */
			if ( objparam[i] instanceof String ) {
				args[i] = String.class;
			} else if ( objparam[i] instanceof Integer ) {
				args[i] = int.class;
			} else if ( objparam[i] instanceof Long ) {
				args[i] = long.class;
			} else if ( objparam[i] instanceof Float ) {
				args[i] = float.class;
			} else if ( objparam[i] instanceof Double ) {
				args[i] = double.class;
			} else if ( objparam[i] instanceof Boolean ) {
				args[i] = boolean.class;
			} else if ( objparam[i] instanceof Byte ) {
				args[i] = byte.class;
			} else if ( objparam[i] instanceof Character ) {
				args[i] = char.class;
			} else {
				args[i] = objparam[i].getClass ( );
			}

		}
		Method method = clazz.getMethod ( methodname, args );
		Object result;
		
		try {
			result = method.invoke ( obj, objparam );
		} catch ( Exception e ) {
			result = new Object();
		}
		return result;
	}

	@Override
	public long getCacheSize ( ) throws Exception {
		// TODO Auto-generated method stub
		// 캐시 크기가 이상하다고 느껴질경우엔 캐시에 저장되는 클래스들이 Serializable 인터페이스를 구현했는지 확인한다
		Cache cache = cachemanager.getCache ( "selectCache" );
		return cache.calculateInMemorySize ( );
	}

	/**
	 * 캐시가 생성된 시간을 리턴한다.
	 * 
	 * @param cacheKey 캐시키
	 * @return 생성 시간(밀리타임)
	 * @throws Exception
	 */
	@Override
	public long getCreationTime ( String cacheKey ) throws Exception {
		Cache cache = cachemanager.getCache ( "selectCache" );
		Element element = cache.get ( cacheKey );
		return null == element ? 0 : element.getCreationTime ( );
	}

	// 일반 공지 용
	public static String getNotiEtcCacheKey ( String bbs_id ) {
		return SmartUXProperties.getProperty ( "SmartUXNotiDao.cache_noti_etc.cacheKey" ) + "-" + bbs_id;
	}
	
	@Override
	public void cacheFileSync(Log logger, String cacheFileName, String cacheKey) throws Exception {
		String syncCacheNASPath = null;
		String strCreateFileNm = null;
		StringBuffer uniqueFileName = null;
		File nasCacheFile = null;
		
		try {
			Object obj = this.getCache(cacheKey);
			
			if(obj != null) {
				// 캐시파일 동기화 할 때 사용하는 NAS 경로
				syncCacheNASPath = SmartUXProperties.getProperty("smartux.sync.cache.folder");
				
				// NAS 에 파일 복사시에는 유니크한 파일명으로 한다.(중복을 피하기 위함)
				uniqueFileName = new StringBuffer();
				uniqueFileName.append(new StringUtil().getCurDttm("yyyyMMddHHmmssSSS"));
				uniqueFileName.append("_" + UUID.randomUUID().toString());
				
				strCreateFileNm = CacheUtil.saveCacheFile(syncCacheNASPath, uniqueFileName.toString(), obj);
				
				nasCacheFile = new File(syncCacheNASPath + File.separator + strCreateFileNm);
				
				logger.info("[NAS Cache File Saved]["+cacheKey+"]["+nasCacheFile.length()+"]["+strCreateFileNm+"]");
				
				// NAS에 복사된 캐시파일을 Copy 하라고 각 서버의 URL을 호출한다.
				int timeout = Integer.parseInt(SmartUXProperties.getProperty("smartux.common.cache.timeout")); 
				int retrycnt = Integer.parseInt(SmartUXProperties.getProperty("smartux.common.cache.retrycnt"));
				String url = SmartUXProperties.getProperty("smartux.common.cache.sync.url");
				String protocolName = SmartUXProperties.getProperty("smartux.common.cache.protocol");
				
				StringBuffer param = new StringBuffer();
				param.append("cacheKey=").append(cacheKey).append("&");
				param.append("nasCacheNm=").append(strCreateFileNm).append("&");
				param.append("localCacheNm=").append(cacheFileName);
	
				String currentIp = GlobalCom.getSystemProperty("JBOSS_IP");
				String currentPort = GlobalCom.getSystemProperty("JBOSS_PORT");
				String currentUri = currentIp + ":" + currentPort;
				String hostsString = SmartUXProperties.getProperty("cache.ServerIPList");
				String[] hostList = hostsString.split("\\|");
				String portsString = SmartUXProperties.getProperty("cache.ServerPortList");
				String[] portList = portsString.split("\\|");
				int idx = -1;

				for(String host : hostList) {
					idx++;
					String hostUri = host + ":" + portList[idx];
					
					// 자기자신은 제외하고 다른서버 호출
					if(!currentUri.equalsIgnoreCase(hostUri)) {
						// 다른 서버의 단말 API를 호출한다
						GlobalCom.callAPISync(host, Integer.parseInt(portList[idx]), url, param.toString(), timeout, retrycnt, protocolName);
						logger.info("[Sync URI][" + protocolName + "://" + hostUri + url + "?" + param.toString() + "]");
					}
				}
				
				// 캐시 파일 동기화 완료 후 NAS에 있는 임시 캐시파일은 삭제
				nasCacheFile.delete();
				
				// 생성된지 1시간이 지난 쓰레기 캐시 파일은 모두 삭제
				// 시스템 다운등의 이유로 쓰레기 캐시 파일이 있을 수 있음
				File nasCacheFolder = new File(syncCacheNASPath);
				File[] tempFiles = nasCacheFolder.listFiles();
				
				for(File f : tempFiles) {
					if(f != null && f.exists()) {
						long lastTime = f.lastModified();
						long nowTime = Calendar.getInstance().getTimeInMillis();
						
						if(((nowTime - lastTime) / 1000 / 60) > 60) {
							f.delete();
						}
					}
				}
				
				logger.info("[Cache File Sync End]["+cacheKey+"]["+strCreateFileNm+"]");
			}
			else {
				logger.info("[Cache is null]["+cacheKey+"]");
			}
		}
		catch(Exception e) {
			logger.error("[Cache File Sync Failed]["+cacheKey+"]["+e.getClass()+"]["+e.getMessage()+"]");
		}
	}

}
