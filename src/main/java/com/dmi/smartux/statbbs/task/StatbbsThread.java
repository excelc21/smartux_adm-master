package com.dmi.smartux.statbbs.task;

import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;

import org.springframework.core.task.TaskExecutor;

import com.dmi.smartux.common.vo.StatParticipateVo;
import com.dmi.smartux.statbbs.service.StatbbsFileUtil;
import com.dmi.smartux.statbbs.vo.ByteBufferVo;

public class StatbbsThread{
	
	private TaskExecutor taskExecutor;
	
	public StatbbsThread(TaskExecutor taskExecutor){
		this.taskExecutor = taskExecutor;
	}
	
	public void writeStat(StatParticipateVo statparticipateVo, String bbsFilePath){
		this.taskExecutor.execute(new addStatExecutor(statparticipateVo, bbsFilePath));
	}

	private class addStatExecutor  implements Runnable {
		
		private StatParticipateVo statparticipateVo;
		private String bbsFilePath;
		
		public addStatExecutor(StatParticipateVo statparticipateVo, String bbsFilePath){
			this.statparticipateVo = statparticipateVo;
			this.bbsFilePath = bbsFilePath;
		}

		@Override
		public void run() {
			FileOutputStream fileout = null;
			FileChannel fChn = null;
			ByteBufferVo ByteBufferVo = null;
			FileLock fl = null;
			
			try{
		        fileout = new FileOutputStream(this.bbsFilePath,true);
		        ByteBufferVo = StatbbsFileUtil.getByteBuffer();
		        ByteBufferVo.getByteBuffer().clear();
		        
		        ByteBufferVo.getByteBuffer().put(this.statparticipateVo.toString().getBytes());
		        ByteBufferVo.getByteBuffer().flip();
		        
		        fChn = fileout.getChannel();
				
				while(fl==null){
					try{
						fl=fChn.tryLock();
					}catch(OverlappingFileLockException e){Thread.sleep(100);}
				}
				
		        fChn.write(ByteBufferVo.getByteBuffer());
			}catch(java.lang.Exception e){
		        System.out.println(e.getClass().getName());
		        System.out.println(e.getMessage());
			}finally{
		        try {
			        fl.release();
		        	fChn.close();
			        fileout.close();
			        StatbbsFileUtil.finish(ByteBufferVo);
				} catch (java.lang.Exception e) {e.getClass().getName();}
			}
		}
		
	}

}
