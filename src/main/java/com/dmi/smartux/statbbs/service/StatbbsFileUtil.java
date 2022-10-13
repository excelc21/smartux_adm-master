package com.dmi.smartux.statbbs.service;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.CLog;
import com.dmi.smartux.common.util.DateUtils;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.statbbs.vo.ByteBufferVo;

public class StatbbsFileUtil {
	
	static int reqCnt = 0;
	static List<ByteBufferVo> arrByteBuff = Collections.synchronizedList(new ArrayList<ByteBufferVo>());
	static ByteBuffer tmpByteBuff = null;
	
	static int poolBuffSize = 200;
	static int sliceBuffSize = 200;
	static int buffUseTime = 600;
	
	final static int buffSize = 256;
	
	public void settingBuff(){
		Log log_logger = LogFactory.getLog("refreshCacheOfStatbbs");
		
		poolBuffSize = Integer.parseInt(GlobalCom.isNull(SmartUXProperties.getProperty("statbbs.buffer.poolbuffsize"),"200"));
		sliceBuffSize = Integer.parseInt(GlobalCom.isNull(SmartUXProperties.getProperty("statbbs.buffer.slicebuffsize"),"200"));
		buffUseTime = Integer.parseInt(GlobalCom.isNull(SmartUXProperties.getProperty("statbbs.buffer.buffusetime"),"600"));
		
		resetPool();
		
		ByteBuffer largeBuff = ByteBuffer.allocateDirect(buffSize*poolBuffSize).order(ByteOrder.nativeOrder());
		for(int i=0; i<poolBuffSize; i++){
			largeBuff.limit(largeBuff.position() + buffSize);
			
			ByteBufferVo bytebufferVo = new ByteBufferVo();
			bytebufferVo.setByteBuffer(largeBuff.slice());
			poolByteBuffer(bytebufferVo);
			largeBuff.position(largeBuff.limit());
		}
		log_logger.info("[StatbbsFileUtil][settingBuff] BuffSize:"+arrByteBuff.size());
	}
	
	public static ByteBufferVo getByteBuffer(){
		Log log_logger = LogFactory.getLog("statbbs");
		log_logger.info("[StatbbsFileUtil][getByteBuffer] BuffPoolSize:"+arrByteBuff.size());
		ByteBufferVo byteBuff = null;
		synchronized (arrByteBuff) {
			if(arrByteBuff.size() > 0){
				byteBuff = arrByteBuff.remove(0);
			}else{
				byteBuff = makeByteBuffer();
			}
			
			reqCnt++;
		}
			
		byteBuff.getByteBuffer().position(0).limit(buffSize);

		for (int i = 0, x = byteBuff.getByteBuffer().remaining(); i < x; i++) {
			byteBuff.getByteBuffer().put(i,(byte) 0);
	    }
		
		return byteBuff;
	}
	
	public static void finish(ByteBufferVo byteBuff){
		Log log_logger = LogFactory.getLog("statbbs");
		log_logger.info("[StatbbsFileUtil][finish] req:"+reqCnt+" pool:"+arrByteBuff.size());
		synchronized (arrByteBuff) {
			if(poolBuffSize < (arrByteBuff.size()+reqCnt)){
				Long waitTime = DateUtils.dateCompare(System.currentTimeMillis(),byteBuff.getUseTime());
				if(buffUseTime > waitTime) poolByteBuffer(byteBuff);
			}else{
				byteBuff.getByteBuffer().clear();
				poolByteBuffer(byteBuff);
			}
			
			reqCnt--;
		}
		if(reqCnt < 0) reqCnt=0;
	}
	
	private static ByteBufferVo makeByteBuffer(){
		Log log_logger = LogFactory.getLog("statbbs");
		log_logger.info("[StatbbsFileUtil][makeByteBuffer] makeByteBuffer!");
		ByteBufferVo bytebufferVo = new ByteBufferVo();
		if(tmpByteBuff==null || tmpByteBuff.capacity()==tmpByteBuff.position()){
			tmpByteBuff = ByteBuffer.allocateDirect(buffSize*sliceBuffSize).order(ByteOrder.nativeOrder());
		}

		tmpByteBuff.limit(tmpByteBuff.position() + buffSize);
		bytebufferVo.setByteBuffer(tmpByteBuff.slice());
		bytebufferVo.setUseTime(System.currentTimeMillis());
		
		tmpByteBuff.position(tmpByteBuff.limit());
		
		return bytebufferVo;
	}
	
	private static void poolByteBuffer(ByteBufferVo byteBuff){
		byteBuff.setUseTime(System.currentTimeMillis());
		arrByteBuff.add(byteBuff);
	}
	
	private static void resetPool(){
		Log log_logger = LogFactory.getLog("statbbs");
		arrByteBuff.clear();
		log_logger.info("[StatbbsFileUtil][resetPool] resetPool:"+arrByteBuff.size());
	}

}
