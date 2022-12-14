package com.dmi.smartux.admin.quality.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.dmi.smartux.admin.quality.controller.QualityAdminController;
import com.dmi.smartux.admin.quality.service.QualityAdminService;
import com.dmi.smartux.admin.quality.vo.AddQualityMemberVo;
import com.dmi.smartux.admin.quality.vo.QualityListVo;
import com.dmi.smartux.admin.quality.vo.QualityMemberCacheListVo;
import com.dmi.smartux.admin.quality.vo.QualityMemberListVo;
import com.dmi.smartux.admin.quality.vo.ViewQualityMemberVo;
import com.dmi.smartux.common.base.BaseService;
import com.dmi.smartux.common.exception.ExceptionHandler;
import com.dmi.smartux.common.exception.SmartUXException;
import com.dmi.smartux.common.module.CustomComparator;
import com.dmi.smartux.common.module.CustomNameFilter;
import com.dmi.smartux.common.module.CustomTypeFilter;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.util.HTMLCleaner;
import com.dmi.smartux.common.vo.QualityMemberVo;

@Service("QualityServiceImpl")
public class QualityAdminServiceImpl extends BaseService implements QualityAdminService {
	
	private final Log logger = LogFactory.getLog(this.getClass());

	private final String memberFolderDir = SmartUXProperties.getProperty("quality.member.dir");
	private final String memberPortableFolderDir = SmartUXProperties.getProperty("quality.portable.member.dir");
	
	@Override
	public QualityListVo getCacheQualityMemberList(QualityListVo qualitylistVo, String cacheYn) throws Exception {
		List<QualityMemberCacheListVo> qualitymembercachelistVo = findQualityMemberList(qualitylistVo, cacheYn);

		qualitylistVo.setPageCount(qualitymembercachelistVo.size());
		qualitylistVo.setList(getQualityPasingList(qualitymembercachelistVo,qualitylistVo));
		
		return qualitylistVo;
	}
	
	/**
	 * ?????? ???????????? ?????? ?????? 
	 * 2019.01.17 serviceType??????
	 */
	@Override
	public void addQualityMember(AddQualityMemberVo addqualitymemberVo, String userId, String userIp, String serviceType) throws Exception {
		
    	String folderName = GlobalCom.getTodayYear()+GlobalCom.getTodayMonth();

    	QualityMemberVo qualitymemberVo = new QualityMemberVo(GlobalCom.isNull(addqualitymemberVo.getSa_id()), GlobalCom.isNull(addqualitymemberVo.getLog_type())
    			, GlobalCom.isNull(addqualitymemberVo.getFind_type()), GlobalCom.isNull(addqualitymemberVo.getSize()),userId,userIp, GlobalCom.isNull(addqualitymemberVo.getLog_level()));
    	
    	int rtnCheck = addQualityMemFile(qualitymemberVo, folderName, serviceType);
		
    	if(rtnCheck==0){
        	//??????????????? ?????????!!
        	//refreshAdminCacheOfQuality();
    		qualityAdminApplyCache();
    	}else{
    		SmartUXException e = new SmartUXException();
    		e.setFlag(SmartUXProperties.getProperty("flag.bedata"));
    		e.setMessage(SmartUXProperties.getProperty("message.bedata"));
    		throw e;
    	}
    	
	}
	
	@Override
	public int addQualityMemberList(List<AddQualityMemberVo> addqualitymemberVo, String userId, String userIp, String serviceType) throws Exception {
		int failCnt = 0;
    	String folderName = GlobalCom.getTodayYear()+GlobalCom.getTodayMonth();

    	QualityMemberVo qualitymemberVo = null;
    	for(AddQualityMemberVo subMemVo : addqualitymemberVo){
	    	qualitymemberVo = new QualityMemberVo(GlobalCom.isNull(subMemVo.getSa_id()), GlobalCom.isNull(subMemVo.getLog_type())
	    			, GlobalCom.isNull(subMemVo.getFind_type()), GlobalCom.isNull(subMemVo.getSize()),userId,userIp, subMemVo.getLog_level());
	    	
	    	int rtnCheck = addQualityMemFile(qualitymemberVo, folderName, serviceType);
			
	    	if(rtnCheck!=0){//????????? ?????? ?????? ??????
				logger.info("[qualityMemberExcelUpload][Duplication SA_ID: "+GlobalCom.isNull(qualitymemberVo.getSa_id())+"]");
	    		failCnt++;
	    	}
    	}
    	//????????? ??????
		qualityAdminApplyCache();
		
		return failCnt;
	}
	
	/**
	 * ?????????????????? ?????? ????????????
	 */
	@Override
	public ViewQualityMemberVo viewQualityMember(String file_id, String serviceType) {
		ViewQualityMemberVo viewqualitymemberVo = new ViewQualityMemberVo();
		
		String fName = file_id+".qat";
		
		String folderPath = memberFolderDir;
		  
		if("P".equals(serviceType)){
			folderPath=memberPortableFolderDir;
		}
		
    	List<File> rtnFile = findFile(fName,"equals", folderPath);
    	
    	//??????????????? ????????? ????????????.
    	if(rtnFile!=null && rtnFile.size() > 0){
    		QualityMemberVo qualitymemberVo = getFileData(rtnFile.get(0));
    		
    		viewqualitymemberVo.setSa_id(GlobalCom.isNull(qualitymemberVo.getSa_id()));
    		viewqualitymemberVo.setLog_type(GlobalCom.isNull(qualitymemberVo.getLog_type()));
    		viewqualitymemberVo.setFind_type(GlobalCom.isNull(qualitymemberVo.getFind_type()));
    		viewqualitymemberVo.setSize(GlobalCom.isNull(qualitymemberVo.getSize()));
    		viewqualitymemberVo.setUserIP(GlobalCom.isNull(qualitymemberVo.getUserIP()));
    		viewqualitymemberVo.setUserId(GlobalCom.isNull(qualitymemberVo.getUserId()));
    		viewqualitymemberVo.setLog_level(GlobalCom.isNull(qualitymemberVo.getLog_level()));
    	}
		
		return viewqualitymemberVo;
	}
	
	/**
	 * ?????????????????? ?????? ??????
	 */
	@Override
	public void modifyQualityMember(AddQualityMemberVo addqualitymemberVo, String userId, String userIp, String serviceType) throws Exception {
		
    	String folderName = GlobalCom.getTodayYear()+GlobalCom.getTodayMonth();

    	QualityMemberVo qualitymemberVo = new QualityMemberVo(GlobalCom.isNull(addqualitymemberVo.getSa_id()), GlobalCom.isNull(addqualitymemberVo.getLog_type())
    			, GlobalCom.isNull(addqualitymemberVo.getFind_type()), GlobalCom.isNull(addqualitymemberVo.getSize()),userId,userIp, GlobalCom.isNull(addqualitymemberVo.getLog_level()));
    	
    	modifyQualityMemFile(qualitymemberVo, folderName, serviceType);
		
    	//??????????????? ?????????!!
    	//refreshAdminCacheOfQuality();
    	qualityAdminApplyCache();
    	
	}
	
	@Override
	public void deleteQualityMember(String file_id, String serviceType) throws Exception {
		HTMLCleaner cleaner = new HTMLCleaner();
		
        String[] file_id_arr = file_id.split(",");
        for(String temp_id : file_id_arr){
        	String clean_id = cleaner.clean(temp_id);
        	//????????? ???????????? ????????????
    		String fName = clean_id+".qat";
        	List<File> rtnFile = findFile(fName, "equals", "P".equals(serviceType)?memberPortableFolderDir:memberFolderDir);
        	for(File delFile : rtnFile){
        		delFile.delete();
        	}
        }
        
    	//??????????????? ?????????!!
    	//refreshAdminCacheOfQuality();
    	qualityAdminApplyCache();
	}
    
    /**
     * ?????? ??????
     * @param qualitymemberVo
     * @param serviceType (2019.01.17 ?????????TV???(P) ??????)
     * @param folderName
     */
    private int addQualityMemFile(QualityMemberVo qualitymemberVo, String folderName, String serviceType){    	
    	int rtnCheck = 0;
    	
    	String folderPath = memberFolderDir+folderName;
    	
    	if("P".equals(serviceType)){
    		folderPath = memberPortableFolderDir+folderName;
    	}
    	
		StringBuilder memberFileName = new StringBuilder();
		memberFileName.append(qualitymemberVo.getSa_id()).append("^!").append(qualitymemberVo.getLog_type()).append(".qat");
    	
    	List<File> rtnFile = findFile(qualitymemberVo.getSa_id()+"^!","startWith","P".equals(serviceType)?memberPortableFolderDir:memberFolderDir);
    	
    	if(rtnFile.size() == 0){
    		StringBuilder filePath = new StringBuilder();
    		filePath.append(folderPath).append("/").append(memberFileName.toString());
	    	
			FileOutputStream fileout = null; 
	        ObjectOutputStream out = null;
			try{
				File dirFile = new File(folderPath);
				if(!dirFile.exists()) dirFile.mkdir();
				
		        fileout = new FileOutputStream(filePath.toString(),false);
		        out = new ObjectOutputStream(fileout);
				
				out.writeObject(qualitymemberVo);
			}catch(java.lang.Exception e){
	    		rtnCheck= 1;
			}finally{
		        try {
					out.close();
			        fileout.close();
				} catch (java.lang.Exception e) {}
			}
    	}else{//?????? ?????? ????????? ????????? ???????????????
    		rtnCheck= 1;
    	}
    	return rtnCheck;
    }
    
    /**
     * ?????? ??????
     * @param qualitymemberVo
     * @param folderName
     */
    private void modifyQualityMemFile(QualityMemberVo qualitymemberVo, String folderName, String serviceType){
    	String folderPath = memberFolderDir+folderName;
    	
    	if("P".equals(serviceType)){
    		folderPath = memberPortableFolderDir+folderName;
    	}
    	
		StringBuilder memberFileName = new StringBuilder();
		memberFileName.append(qualitymemberVo.getSa_id()).append("^!").append(qualitymemberVo.getLog_type()).append(".qat");
    	
    	//????????? ???????????? ????????????
    	List<File> rtnFile = findFile(qualitymemberVo.getSa_id()+"^!","startWith", "P".equals(serviceType)?memberPortableFolderDir:memberFolderDir);
    	for(File delFile : rtnFile){
    		delFile.delete();
    	}

		StringBuilder filePath = new StringBuilder();
		filePath.append(folderPath).append("/").append(memberFileName.toString());
    	
		FileOutputStream fileout = null; 
        ObjectOutputStream out = null;
		try{
			File dirFile = new File(folderPath);
			if(!dirFile.exists()) dirFile.mkdir();
			
	        fileout = new FileOutputStream(filePath.toString(),false);
	        out = new ObjectOutputStream(fileout);
			
			out.writeObject(qualitymemberVo);
		}catch(java.lang.Exception e){
		}finally{
	        try {
				out.close();
		        fileout.close();
			} catch (java.lang.Exception e) {}
		}
    }
	
	/**
	 * ?????? ??????????????? ???????????? ???????????? ??????
	 * ????????? TV ?????? ??? ?????? ?????? 2019.01.17
	 * @param qualitylistVo
	 * @return
	 * @throws Exception
	 */
	private List<QualityMemberCacheListVo> findQualityMemberList(QualityListVo qualitylistVo, String cacheYn) throws Exception{
		
		List<QualityMemberCacheListVo> resultList = new ArrayList<QualityMemberCacheListVo>();
		//??????????????? ?????? ???????????? ?????? ??????
		int interval = Integer.parseInt(GlobalCom.isNull(SmartUXProperties.getProperty("SmartUX_AdmQualityDao.refreshQuality.interval"),"86400000"));
		if("Y".equals(cacheYn)) interval = -1; //?????? ?????? ?????? ????????? ???????????? ???????????? ??????.
		
		//????????? TV??? ??????
		//??????????????? ?????? ???????????? ????????? ?????????
		Map<String, List<QualityMemberCacheListVo>> qualityCacheMap = (Map<String, List<QualityMemberCacheListVo>>) service.getCache(
					this
					, "getQualityList"
					, SmartUXProperties.getProperty("SmartUX_AdmQualityDao.refreshQuality.cacheKey")
					, interval
				);
		
		List<QualityMemberCacheListVo> tempList = null;
		
		if("P".equals(qualitylistVo.getServiceType())){
			if(!CollectionUtils.isEmpty(qualityCacheMap)){
				tempList = qualityCacheMap.get("P");
			}
		
		}else{
			if(!CollectionUtils.isEmpty(qualityCacheMap)){
				tempList = qualityCacheMap.get("T");
			}
		}
		
		if(!CollectionUtils.isEmpty(tempList)){
			String findValue = GlobalCom.isNull(qualitylistVo.getFindValue());
			String sLogType = GlobalCom.isNull(qualitylistVo.getS_log_type());
			
			if(StringUtils.isNotBlank(findValue) ||StringUtils.isNotBlank(sLogType)){
				for(QualityMemberCacheListVo subVo : tempList){//????????? ????????? ?????? ????????? ?????? ??????..
					if(("".equals(findValue) || subVo.getSa_id().contains(findValue)) && ("".equals(sLogType) || subVo.getLog_type().equals(sLogType))){
						resultList.add(subVo);
					}
				}
				
			}else{//????????? ????????? ????????? ??????
				resultList = tempList;
			}
		}

		if("Y".equals(cacheYn)) QualityAdminController.chacheYn = "N"; //?????? ?????? ??? N?????? ?????????.
		return resultList;
	}
	
	/**
	 * ?????? ?????? ?????? cache??????
	 * -> ????????? TV ????????? ??????????????? cache ?????? ??????
	 * @return
	 * @throws Exception 
	 */
	public Map<String, List<QualityMemberCacheListVo>> getQualityList() throws Exception{
		
		Map<String, List<QualityMemberCacheListVo>> resultMap = new HashMap<String, List<QualityMemberCacheListVo>>();
		
		//????????? TV ?????? ?????? ?????? 
		List<QualityMemberCacheListVo> qualityList = getqualityFileData(memberFolderDir);
    	if(!CollectionUtils.isEmpty(qualityList)){
    		resultMap.put("T", qualityList);
    	}
		
		// ?????????TV 
    	List<QualityMemberCacheListVo> portableQualityList = getqualityFileData(memberPortableFolderDir);
    	if(!CollectionUtils.isEmpty(portableQualityList)){
    		resultMap.put("P", portableQualityList);
    	}
    	
		return resultMap;
	}
	
	/**
	 * ???????????????????????? fileData ??????
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	private List<QualityMemberCacheListVo> getqualityFileData(String filePath) throws Exception{
		
		List<QualityMemberCacheListVo> resultList = new ArrayList<QualityMemberCacheListVo>();
		
		File qatFolder = new File(filePath);
    	File[] qltDirectory = qatFolder.listFiles(new CustomTypeFilter());
    	Arrays.sort(qltDirectory,new CustomComparator("QualityDir"));
    	
    	QualityMemberCacheListVo qltMemVo = null;
    	for(File dirName : qltDirectory){
        	File qatFile = new File(filePath+dirName.getName());
    		List<File> fileList = Arrays.asList(qatFile.listFiles(new CustomNameFilter("endsWith",".qat")));

    		Collections.sort(fileList,new CustomComparator("QualityFile"));
        	
        	for (File memFile : fileList) {
        		String fName = memFile.getName();
        		
        		qltMemVo = new QualityMemberCacheListVo();
        		String[] cacheData = fName.replace(".qat", "").split("\\^\\!");
        		
        		if(cacheData.length==2){
	        		qltMemVo.setSa_id(cacheData[0]);
	        		qltMemVo.setLog_type(cacheData[1]);
	        		
	        		resultList.add(qltMemVo);
        		}else{
        			//????????? ????????? ????????? ???????????????. .error???..
					try {
						logger.info("getQualityList:["+fName+"] rename..");
	        			File moveFile;
						moveFile = new File(memFile.getCanonicalPath().replace(".qat", ".error"));
	        			GlobalCom.moveFile(memFile, moveFile);
					} catch (IOException e) {
						logger.info("getQualityList:["+e.getClass().getName()+"]["+e.getMessage()+"]");
					}
        		}
        		
        	}
    	}
    	return resultList;
	}

	/**
	 * ??????????????? ?????? ????????? ??? ????????? ???????????? ????????? ???
	 * @param qualitymembercachelistVo
	 * @param qualitylistVo
	 * @return
	 * @throws Exception
	 */
	private List<QualityMemberListVo> getQualityPasingList(List<QualityMemberCacheListVo> qualitymembercachelistVo, QualityListVo qualitylistVo) throws Exception {

		qualitylistVo.setStart_rnum(qualitylistVo.getPageNum()*qualitylistVo.getPageSize()-qualitylistVo.getPageSize()+1);
		qualitylistVo.setEnd_rnum(qualitylistVo.getStart_rnum()+qualitylistVo.getPageSize()-1);
		
		List<QualityMemberListVo> qualitymemberlistVo = new ArrayList<QualityMemberListVo>();
		try {
			String folderPath =  memberFolderDir; 
			if("P".equals(qualitylistVo.getServiceType())){
				folderPath=memberPortableFolderDir;
			}
			
			int ArrNum = 0;
			for(QualityMemberCacheListVo qualitymemVo : qualitymembercachelistVo){
    			if(qualitylistVo.getStart_rnum() > ++ArrNum){
    				continue;//?????? ?????? ???????????? ????????????.
    			}else if(qualitylistVo.getEnd_rnum() >= ArrNum){
	    			String saId = GlobalCom.isNull(GlobalCom.isNull(qualitymemVo.getSa_id()));
	    			String logType = GlobalCom.isNull(GlobalCom.isNull(qualitymemVo.getLog_type()));
	    			
	    			
	    			StringBuilder fileId = new StringBuilder();
	    			fileId.append(saId).append("^!").append(logType);
	    			
	    			List<File> findFile = findFile(fileId.toString()+".qat","equals", folderPath );
	        		for(File subFile : findFile){//????????? ????????? ???????????? ???????????? ????????? ????????? ?????? ?????? ????????????.(ArrNum??? +1 ??? ????????????. ????????? ?????????????????????..)
	        			QualityMemberListVo qualitymemsubVo = new QualityMemberListVo();
	        			qualitymemsubVo.setFile_id(fileId.toString());
	        			qualitymemsubVo.setSa_id(saId);
	        			qualitymemsubVo.setLog_type(logType);

	            		QualityMemberVo memberVo = getFileData(subFile);
	        			
	            		if(memberVo!=null){
		        			qualitymemsubVo.setFind_type(GlobalCom.isNull(memberVo.getFind_type()));
		        			qualitymemsubVo.setSize(GlobalCom.isNull(memberVo.getSize()));
		        			qualitymemsubVo.setLog_level(StringUtils.defaultString(memberVo.getLog_level()));
	            		}
	        			qualitymemberlistVo.add(qualitymemsubVo);
	        		}
	        	}else{
	        		break;//??? ?????? ?????? ?????? ?????? ??????
	        	}
			}
		} catch (Exception e) {
			throw e;
		}
		
		return qualitymemberlistVo;
	}
	
	/**
	 * ?????? ????????? ???????????? ???????????? ?????? ????????? ?????????.
	 * @param findName
	 * @return
	 */
    public List<File> findFile(String findName, String type, String folderDir){
    	File qatFolder = new File(folderDir);
    	
    	File[] qltDirectory = qatFolder.listFiles(new CustomTypeFilter());
    	
    	List<File> rtnFile = new ArrayList<File>();
    	for(File dirName : qltDirectory){
        	File qatFile = new File(folderDir+dirName.getName());
        	
        	List<File> fileList = Arrays.asList(qatFile.listFiles(new CustomNameFilter(type,findName)));
        	rtnFile.addAll(fileList);
    	}
    	
		return rtnFile;
    }
	
	/**
	 * ?????? ????????? ????????? ??????
	 * @param objFile
	 * @return
	 */
	private QualityMemberVo getFileData(File objFile) {
    	
    	FileInputStream filein = null;
		ObjectInputStream in = null;
		
		QualityMemberVo retVo = new QualityMemberVo();
		try{
			if(objFile.exists()){//?????? ?????????
				filein = new FileInputStream(objFile);
				in = new ObjectInputStream(filein);
				
				Object obj = null;
		        if ((obj = in.readObject()) != null){
		        	if(obj instanceof QualityMemberVo){
		        		retVo = (QualityMemberVo)obj;
		        	}
		        }
			}
		}catch(java.io.EOFException e){//?????? ??????.. ????????? ?????? ????????? ??????..
		}catch(java.lang.Exception e){
			logger.info("QualityFileReadMiss:["+e.getClass().getName()+"]["+e.getMessage()+"]");
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
	
	
	//??? ????????? ????????? ????????? ????????? ???????????? ?????????????????? ????????????.(????????? ?????????????????? ?????? ?????? ????????????. ????????? ?????? ?????????????????? ???????????? ???????????????.)
	private String qualityAdminApplyCache(){
		
		String resultcode = "Y";	
		
		try {
			//HDTV ??????????????? ?????? IP??? PORT ??? ?????????. 
			String param = "callByScheduler=A"; // ?????????????????? ??????????????? ????????? ???????????????(??? ?????? A?????? DB?????? ?????? ????????? ????????? ????????????)
			int timeout = Integer.parseInt(SmartUXProperties.getProperty("scheduler.timeout"));		// timeout ?????? ?????????????????? ??????
			int retrycnt = Integer.parseInt(SmartUXProperties.getProperty("scheduler.retrycnt"));	// ????????? ????????? ???????????? ?????? ??????
			String url = "";
			String protocolName = "";
			
				// ????????? ?????? ????????? API ???????????? ?????? ??????
			protocolName = SmartUXProperties.getProperty("SmartUX_AdmQualityDao.refreshQuality.protocol");
			url = SmartUXProperties.getProperty("SmartUX_AdmQualityDao.refreshQuality.CacheURL");
			GlobalCom.syncAdminServerCache(url, param, timeout, retrycnt, protocolName); 	// ?????? ????????? ????????? ?????? ????????? ?????? ??????(????????? ??????????????? ????????????-?????? ????????? ???????????????IP??? ?????????????????? ???????????? ??????.H_MIMS??????..)
			
		} catch (Exception e) {
			logger.error("qualityAdminApplyCache"+e.getClass().getName());
			logger.error("qualityAdminApplyCache"+e.getMessage());
			
			ExceptionHandler handler = new ExceptionHandler(e);
			resultcode = "N";
		}
		
		return resultcode;
		
	}
	
	@Override
	public void refreshAdminCacheOfQuality() throws Exception {
		
		service.getCache(
					this
					, "getQualityList"
					, SmartUXProperties.getProperty("SmartUX_AdmQualityDao.refreshQuality.cacheKey")
					, -1
				);
	}

}