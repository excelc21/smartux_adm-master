package com.dmi.smartux.sample.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmi.smartux.common.exception.SmartUXException;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.service.CacheService;
import com.dmi.smartux.sample.dao.SampleTableDao;
import com.dmi.smartux.sample.service.SampleTable;
import com.dmi.smartux.sample.vo.EHCacheVO;
import com.dmi.smartux.sample.vo.Result;
import com.dmi.smartux.sample.vo.ResultOne;
import com.dmi.smartux.sample.vo.SampleVO;
import com.dmi.smartux.sample.vo.SampleVO2;
import com.googlecode.ehcache.annotations.Cacheable;

@Service
public class SampleTableImpl implements SampleTable{

	private final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	SampleTableDao dao;
	
	@Autowired
	CacheService cacheservice;

	/*
	@Transactional 설명
	interface에서 메소드 정의를 할 때 함수 정의 위에 @Transactional 어노테이션을 붙인다
	조회 위주의 함수에서는 ReadOnly=true를 붙여서 트랜잭션을 읽기 전용으로 열어서 작업을 한다
	트랜잭션에 시간을 지정해줄 때는 timeout 엘리먼트를 이용하여 지정할 수 있다(초 단위)
	
	ex : @Transactional(readOnly=false, timeout=10)
	
	@Transactional 어노테이션은 RuntimeException을 상속받은 경우에만 트랜잭션 작업에 대한 롤백을 수행하게 된다.
	때문에 RuntimeException이 아닌 다른 Exception이 발생할 경우 rollbackFor 속성을 주어 작업한다
	만약 RuntimeException이 발생했는데 이 Exception은 rollback이 되어서는 안될 경우엔 noRollbackFor를 넣어주면 된다
	
	ex : @Transactional(readonly=false, rollbackFor=IOException.class,MyException.class)
	     @Transactional(readonly=false, noRollbackFor=AException.class,BException.class)
	
	 */
	
	@Cacheable(cacheName = "selectCache")
	@Override
//	@Transactional(readOnly=true) 140708
	public List<SampleVO> select(int start, int end) throws Exception{
		// TODO Auto-generated method stub
		return dao.select(start, end);
	}
	
	@Cacheable(cacheName = "selectCache")
	@Override
//	@Transactional(readOnly=true) 140708
	public List<SampleVO2> select2(int start, int end) throws Exception{
		// TODO Auto-generated method stub
		return dao.select2(start, end);
	}
	
	@Override
//	@Transactional(readOnly=true) 140708
	public ResultOne oneselect(int idx) throws Exception {
		// TODO Auto-generated method stub
		ResultOne result = dao.oneselect(idx);
		return result;
	}

	@Override
//	@Transactional 140708
	public void insert(int idx, String name) throws Exception{
		// TODO Auto-generated method stub
		dao.insert(idx, name);
		
		// insert 작업 후 추가 작업을 하는 과정에서 예외가 발생하여 rollback을 해야 하는 경우 callException 함수에 관련 에러코드와 메시지를 셋팅하여 호출한다
		// 그러면 함수 내부에서 RuntimeException을 상속받은 SmartUXException을 return 하기 때문에 rollback이 된다
		// callException(SmartUXProperties.getProperty("errorcode.insert"), SmartUXProperties.getProperty("errormsg.insert"));
		
		
	}
	
	@Override
//	@Transactional 140708
	public void update(int idx, String name) throws Exception{
		// TODO Auto-generated method stub
		int resultcnt = dao.update(idx, name);
		logger.debug("수정 cnt : " + resultcnt);
		
		// update 작업 후 추가 작업을 하는 과정에서 예외가 발생하여 rollback을 해야 하는 경우 callException 함수에 관련 에러코드와 메시지를 셋팅하여 호출한다
		// 그러면 함수 내부에서 RuntimeException을 상속받은 SmartUXException을 return 하기 때문에 rollback이 된다
		// callException(SmartUXProperties.getProperty("errorcode.update"), SmartUXProperties.getProperty("errormsg.update"));
		
	}
	
	@Override
//	@Transactional 140708
	public void delete(int idx) throws Exception{
		// TODO Auto-generated method stub
		Result<SampleVO> result = new Result<SampleVO>();
		result.setRecordset(null);
		
		int resultcnt = dao.delete(idx);
		logger.debug("삭제 cnt : " + resultcnt);
		
		// delete 작업 후 추가 작업을 하는 과정에서 예외가 발생하여 rollback을 해야 하는 경우 callException 함수에 관련 에러코드와 메시지를 셋팅하여 호출한다
		// 그러면 함수 내부에서 RuntimeException을 상속받은 SmartUXException을 return 하기 때문에 rollback이 된다
		// callException(SmartUXProperties.getProperty("errorcode.delete"), SmartUXProperties.getProperty("errormsg.delete"));
		
	}
	
	public void callException(String flag, String message) throws RuntimeException{
		throw new SmartUXException(flag, message);
	}

	
	@Override
//	@Transactional 140708
	public List<EHCacheVO> ehcache() throws Exception {
		// TODO Auto-generated method stub
		return dao.ehcache();
	}

	@Override
	public List<EHCacheVO> ehcacheCustom(String category, String startnum, String reqcount) throws Exception {
		// TODO Auto-generated method stub
		/*
		 * CacheService 인터페이스의 getCache 함수는 사용하고자 하는 빈 객체의 함수의 값을 캐시에 넣거나 또는 기존에 캐시 내용이 있으면 
		 * 캐시에서 가져와서 리턴하는 함수이다
		 * getCache 함수에 들어가는 파라미터는 
		 * 1. 스프링 빈 객체
		 * 2. 실행하고자 하는 함수의 문자열
		 * 3. 사용하고자 하는 캐쉬키
		 * 4. 갱신 간격(여기에 0이 들어가면 갱신 간격을 체크하지 않고 바로 캐쉬에서 꺼내온다. 즉 캐쉬 갱신 작업이 없다)
		 * 5. 실행하고자 하는 함수에서의 파라미터 값들의 배열이 들어간다
		 * 
		 *  ex ) dao 객체의 mycache(paramaaa, parambbb) 함수의 내용을 캐쉬에서 관리해서 넣거나 그 내용을 캐시에서 꺼내오는 경우 
		 *  	getCache(dao
		 *  	, "mycache"
		 *  	, SmartUXProperties.getProperty("SampleTableDao.mycache.cacheKey")
		 *  	, Long.parseLong(SmartUXProperties.getProperty("SampleTableDao.mycache.interval")
		 *  	, paramaaa
		 *  	, parambbb
		 *  	)
		 * 
		 * getCache 함수를 이용할때는 주어진 캐쉬키와 dao의 파라미터 값들의 조합으로 캐쉬키가 만들어지기 때문에 이 부분이 신중해야 한다
		 * Service 함수의 설계까지는 페이징이 적용되지만 dao에서는 주어진 카테고리 코드로 검색된 전체가 리턴되도록 설계해야 하기 때문에 
		 * dao에는 startnum과 reqcount 파라미터가 빠진다
		 * 만약 dao에 startnum과 reqcount 파라미터를 추가해서 설계할 경우 캐쉬서비스객체에서 캐쉬키가 
		 * SmartUXProperties.getProperty("SampleTableDao.ehcacheCustom.cacheKey")-startnum-reqcount로 만들어져서 캐쉬가 관리되기 때문에
		 * 페이징 정보로 캐쉬키가 관리되어서 올바른 캐쉬 관리가 되질 않는다
		 */
		List<EHCacheVO> dbresult = (List<EHCacheVO>)cacheservice.getCache(
				dao
				, "ehcacheCustom"
				, SmartUXProperties.getProperty("SampleTableDao.ehcacheCustom.cacheKey"), 
				Long.parseLong(SmartUXProperties.getProperty("SampleTableDao.ehcacheCustom.interval"))
				, category);
		List<EHCacheVO> result = new ArrayList<EHCacheVO>();
		
		if(dbresult != null){
			int startidx = Integer.parseInt(startnum)-1;
			int endidx = startidx + Integer.parseInt(reqcount);
			for(int i = startidx; i < endidx; i++){
				result.add(dbresult.get(i));
			}
		}
		return result;
	}

	
	
	
}
