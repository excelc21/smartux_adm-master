package com.dmi.smartux.quality.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.dmi.smartux.common.base.BaseService;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.module.CustomComparator;
import com.dmi.smartux.common.module.CustomNameFilter;
import com.dmi.smartux.common.module.CustomTypeFilter;
import com.dmi.smartux.common.vo.QualityMemberVo;
import com.dmi.smartux.quality.service.QualityService;

@Service
public class QualityServiceImpl extends BaseService implements QualityService {
	Log log_logger = LogFactory.getLog("refreshCacheOfQualityMember");
	
	@Override
	public void refreshCacheOfQualityMember(String callByScheduler) throws Exception {
		long interval = getInterval( callByScheduler, "SmartUXQualityDao.refreshQuality.interval" );
		
		//품질대상 단말 정보
		service.getCache(
					this
					, "findQualityMemberList"
					, SmartUXProperties.getProperty("SmartUXQualityDao.refreshQuality.cacheKey")
					, interval
				);
	}
	
	public Map<String, Object> findQualityMemberList() throws Exception{
		Map<String, Object> qualitymemberVo = new HashMap<String, Object>();
		Map<String, QualityMemberVo> qualitygbnDeVo = new HashMap<String, QualityMemberVo>();//기본
		List<QualityMemberVo> qualitygbnPRVo = new ArrayList<QualityMemberVo>();//prefix
		List<QualityMemberVo> qualitygbnPOVo = new ArrayList<QualityMemberVo>();//postfix
		QualityMemberVo submemberVo = null;
		
		String memberFolderDir = SmartUXProperties.getProperty("quality.member.dir");

    	File qatFolder = new File(memberFolderDir);
    	File[] qltDirectory = qatFolder.listFiles(new CustomTypeFilter());
    	
    	String qualityServer = GlobalCom.isNull(SmartUXProperties.getProperty("quality.req.server"),"NoServerList");//대상 서버 정보
    	qualitymemberVo.put("SERVER", qualityServer);
    	for(File dirName : qltDirectory){
    		
        	File qatFile = new File(memberFolderDir+dirName.getName());
    		List<File> fileList = Arrays.asList(qatFile.listFiles(new CustomNameFilter("endsWith",".qat")));
        	
        	for (File memFile : fileList) {
        		try{
	        		submemberVo = new QualityMemberVo();
	        		submemberVo = getFileData(memFile);
	        		String findType = submemberVo.getFind_type();
	        		if("DE".equals(findType)){
	        			qualitygbnDeVo.put(submemberVo.getSa_id(), submemberVo);
	        		}else if("PR".equals(findType)){
	        			qualitygbnPRVo.add(submemberVo);
	        		}else if("PO".equals(findType)){
	        			qualitygbnPOVo.add(submemberVo);
	        		}
        		}catch(java.lang.Exception e){
        			log_logger.info("findQualityMemberList:["+e.getClass().getName()+"]["+e.getMessage()+"]");
        		}
        	}
    	}
    	//정렬 기준
    	Collections.sort(qualitygbnPRVo,new CustomComparator("LengthDesc"));
    	Collections.sort(qualitygbnPOVo,new CustomComparator("LengthDesc"));
		
    	qualitymemberVo.put("DE", qualitygbnDeVo);
    	qualitymemberVo.put("PR", qualitygbnPRVo);
    	qualitymemberVo.put("PO", qualitygbnPOVo);
		
		return qualitymemberVo;
	}

	@Override
	public Map<String, Object> getQualityMemberList(){
		
		Map<String, Object> qualitymemberVo = new HashMap<String, Object>();
		
		try{
			qualitymemberVo = (Map<String, Object>) service.getCache(SmartUXProperties.getProperty("SmartUXQualityDao.refreshQuality.cacheKey"));
		}catch(java.lang.Exception e){
			log_logger.info("getQualityMemberList:["+e.getClass().getName()+"]["+e.getMessage()+"]");
		}
		
		return qualitymemberVo;
	}
	
	/**
	 * 해당 파일의 데이터 추출
	 * @param objFile
	 * @return
	 */
	private QualityMemberVo getFileData(File objFile) {
    	
    	FileInputStream filein = null;
		ObjectInputStream in = null;
		
		QualityMemberVo retVo = new QualityMemberVo();
		try{
			if(objFile.exists()){//파일 있다면
				filein = new FileInputStream(objFile);
				in = new ObjectInputStream(filein);
				
				Object obj = null;
		        if ((obj = in.readObject()) != null){
		        	if(obj instanceof QualityMemberVo){
		        		retVo = (QualityMemberVo)obj;
		        	}
		        }
			}
		}catch(java.io.EOFException e){//이건 정상.. 더이상 뺄게 없다는 거다..
		}catch(java.lang.Exception e){
			log_logger.info("QualityFileReadMiss:["+e.getClass().getName()+"]["+e.getMessage()+"]");
		}finally{
			try {
				try {
					in.close();
					filein.close();
				} catch (java.lang.Exception e) {
				}
			} catch (java.lang.Exception e) {
			}
		}
		
		return retVo;
    }

}
