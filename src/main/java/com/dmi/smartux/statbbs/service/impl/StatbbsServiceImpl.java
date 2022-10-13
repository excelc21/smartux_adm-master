package com.dmi.smartux.statbbs.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmi.smartux.common.base.BaseService;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.vo.StatParticipateVo;
import com.dmi.smartux.statbbs.dao.StatbbsDao;
import com.dmi.smartux.statbbs.service.StatbbsFileUtil;
import com.dmi.smartux.statbbs.service.StatbbsService;
import com.dmi.smartux.statbbs.task.StatbbsThread;
import com.dmi.smartux.statbbs.vo.BbsStatListVo;

@Service
public class StatbbsServiceImpl extends BaseService implements StatbbsService {
	
	@Autowired
	StatbbsDao dao;
	
	@Autowired
	StatbbsThread statThread;
	
	@PostConstruct
	public void statBufferSetting(){
		//초기 버퍼 셋팅
		StatbbsFileUtil SU = new StatbbsFileUtil();
		SU.settingBuff();
	}

	@Override
	public List<BbsStatListVo> refreshCacheOfBbsStat(String callByScheduler) throws Exception {
		
		long interval = getInterval( callByScheduler, "SmartUXBbsStatDao.cache_bbsstat.interval" );
		List<BbsStatListVo> list = (List<BbsStatListVo>) service.getCache(
					dao
					, "refreshCacheOfStatBbs"
					, SmartUXProperties.getProperty("SmartUXBbsStatDao.cache_bbsstat.cacheKey")
					, interval
				);
		
		
		return list;
	}

	@Override
	public BbsStatListVo getBbsStat(String stat_no) throws Exception {
		BbsStatListVo list = null;
		List<BbsStatListVo> bbsstatlistVo = (List<BbsStatListVo>) service.getCache(SmartUXProperties.getProperty("SmartUXBbsStatDao.cache_bbsstat.cacheKey"));
		
		for(BbsStatListVo tempVo : bbsstatlistVo){
			if(stat_no.equals(tempVo.getStat_no())){
				list = tempVo;
				break;
			}
		}
		
		return list;
	}
	
	@Override
	public void addBbsStat(StatParticipateVo statparticipateVo, String bbsFilePath) throws Exception {
		statThread.writeStat(statparticipateVo, bbsFilePath);
	}

//	@Override
//	public void addBbsStat(StatParticipateVo statparticipateVo, String bbsFilePath) throws Exception {
//		
//		FileOutputStream fileout = null;
//		FileChannel fChn = null;
//		ByteBufferVo ByteBufferVo = null;
//		FileLock fl = null;
//		
//		try{
//	        fileout = new FileOutputStream(bbsFilePath,true);
//	        ByteBufferVo = StatbbsFileUtil.getByteBuffer();
//	        ByteBufferVo.getByteBuffer().clear();
//	        
//	        ByteBufferVo.getByteBuffer().put(statparticipateVo.toString().getBytes());
//	        ByteBufferVo.getByteBuffer().flip();
//	        
//	        fChn = fileout.getChannel();
//			
//			while(fl==null){
//				try{
//					fl=fChn.tryLock();
//				}catch(OverlappingFileLockException e){Thread.sleep(100);}
//			}
//			
//	        fChn.write(ByteBufferVo.getByteBuffer());
//		}catch(java.lang.Exception e){
//	        System.out.println(e.getClass().getName());
//	        System.out.println(e.getMessage());
//		}finally{
//	        try {
//		        fl.release();
//	        	fChn.close();
//		        fileout.close();
//		        StatbbsFileUtil.finish(ByteBufferVo);
//			} catch (java.lang.Exception e) {e.getClass().getName();}
//		}
//	}

//	@Override
//	public void addBbsStat(StatParticipateVo statparticipateVo, String bbsFilePath) throws Exception {
//		FileOutputStream fileout = null; 
//        ObjectOutputStream out = null;
//		try{
//			boolean firstData = true;
//			if(new File(bbsFilePath).exists()) firstData = false;//파일이 존재하는지 검사 
//			
//			
//	        fileout = new FileOutputStream(bbsFilePath,true);
//	        if(firstData)
//	        	out = new ObjectOutputStream(fileout);
//	        else
//	        	out = new CustomObjectOutputStream(fileout);
//			
//			out.writeObject(statparticipateVo);
//		}catch(java.lang.Exception e){
//		}finally{
//	        try {
//				out.close();
//		        fileout.close();
//			} catch (java.lang.Exception e) {}
//		}
//	}
}
