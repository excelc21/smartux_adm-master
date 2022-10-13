package com.dmi.smartux.common.service;

import java.util.Map;

import org.apache.commons.logging.Log;

public interface CacheService {

	/**
	 * 캐쉬에 data 의 내용을 key 의 값으로 넣는다.
	 * 
	 * @param key 캐쉬의 키
	 * @param data 캐쉬에 들어갈 데이터
	 * @throws Exception
	 */
	public void putCache ( String key, Object data ) throws Exception;
	
	/**
	 * <PRE>
	 * @MethodName : putCache
	 * @ClassName  : CacheService
	 * @Author     : DEOKSAN
	 * @Date       : 2016. 6. 17. 오후 3:50:51
	 * @Comment    : Map 객체에 들어가 있는 데이터를 캐쉬화
	 * </PRE>
	 * @return void
	 * @param map
	 * @throws Exception
	 */
	public void putCache(Map<String, Object> map) throws Exception;
	
	/**
	 * 캐쉬에서 key 에 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public Object getCache(String key) throws Exception;

	/**
	 * 캐시에 저장할 값을 리턴하는 객체의 함수를 대신 실행하여 캐시에 고유의 캐시키를 주어 저장한다
	 * 
	 * @param obj 캐시에 저장할 값을 리턴하는 함수가 있는 객체
	 * @param methodname 캐시에 저장할 값을 리턴하는 함수의 함수명
	 * @param cachekey 캐시키
	 * @param interval 캐시 갱신 간격(0일 경우 캐시 갱신 간격을 체크하지 말고 바로 캐시에서 가져온다)
	 * @param objparam 캐시에 저장할 값을 리턴하는 함수에서 사용하는 파라미터값들
	 * @return 캐시에 저장된 내용
	 * @throws Exception
	 */
	public Object getCache ( Object obj, String methodname, String cachekey, long interval, Object... objparam ) throws Exception;
	
	/**
	 * 캐시에 저장할 값을 리턴하는 객체의 함수를 대신 실행하여 캐시에 고유의 캐시키를 주어 저장한다
	 * 
	 * @param cachename 캐시 그룹명
	 * @param obj 캐시에 저장할 값을 리턴하는 함수가 있는 객체
	 * @param methodname 캐시에 저장할 값을 리턴하는 함수의 함수명
	 * @param cachekey 캐시키
	 * @param interval 캐시 갱신 간격(0일 경우 캐시 갱신 간격을 체크하지 말고 바로 캐시에서 가져온다)
	 * @param objparam 캐시에 저장할 값을 리턴하는 함수에서 사용하는 파라미터값들
	 * @return 캐시에 저장된 내용
	 * @throws Exception
	 */
	public Object getCache ( String cachename, Object obj, String methodname, String cachekey, long interval, Object... objparam ) throws Exception;
	
	/**
	 * 특정 캐시그룹의 해당하는 키 데이터를 삭제한다.
	 * @param cacheName
	 * @param cachekey
	 * @param objparam
	 * @return
	 * @throws Exception
	 */
	public boolean deleteCache(String cacheName, String cachekey, Object... objparam) throws Exception;

//	/**
//	 * 캐시키를 주어 해당 캐시키에 저장되어 있는 캐시내용을 리턴한다
//	 * 
//	 * @param cachekey 캐시키
//	 * @param objparam 캐시키를 만들때 사용하는 파라미터 값들
//	 * @return 캐시에 저장된 내용
//	 * @throws Exception
//	 */
//	public Object getCache ( String cachekey, Object... objparam ) throws Exception;

	/**
	 * 현재 사용중인 캐쉬의 크기를 리턴한다
	 * 
	 * @return 사용중인 캐쉬의 크기
	 * @throws Exception
	 */
	public long getCacheSize ( ) throws Exception;

	/**
	 * 캐시가 생성된 시간을 리턴한다.
	 * 
	 * @param cacheKey 캐시키
	 * @return 생성 시간(밀리타임)
	 * @throws Exception
	 */
	public long getCreationTime ( String cacheKey ) throws Exception;
	
	/**
	 * 각각의 WAS(인스턴스) 에 캐시를 동기화 한다.
	 * 
	 * @param cacheName
	 * @param cacheKey
	 * @return
	 * @throws Exception
	 */
	public void cacheFileSync(Log logger, String cacheName, String cacheKey) throws Exception;
	
}
