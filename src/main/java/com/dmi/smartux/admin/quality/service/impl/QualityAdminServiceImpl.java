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
	 * 품질 대상단말 정보 등록 
	 * 2019.01.17 serviceType추가
	 */
	@Override
	public void addQualityMember(AddQualityMemberVo addqualitymemberVo, String userId, String userIp, String serviceType) throws Exception {
		
    	String folderName = GlobalCom.getTodayYear()+GlobalCom.getTodayMonth();

    	QualityMemberVo qualitymemberVo = new QualityMemberVo(GlobalCom.isNull(addqualitymemberVo.getSa_id()), GlobalCom.isNull(addqualitymemberVo.getLog_type())
    			, GlobalCom.isNull(addqualitymemberVo.getFind_type()), GlobalCom.isNull(addqualitymemberVo.getSize()),userId,userIp, GlobalCom.isNull(addqualitymemberVo.getLog_level()));
    	
    	int rtnCheck = addQualityMemFile(qualitymemberVo, folderName, serviceType);
		
    	if(rtnCheck==0){
        	//수정됐으니 리캐시!!
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
			
	    	if(rtnCheck!=0){//기존에 해당 가번 존재
				logger.info("[qualityMemberExcelUpload][Duplication SA_ID: "+GlobalCom.isNull(qualitymemberVo.getSa_id())+"]");
	    		failCnt++;
	    	}
    	}
    	//리캐싱 요청
		qualityAdminApplyCache();
		
		return failCnt;
	}
	
	/**
	 * 품질대상단말 정보 상세조회
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
    	
    	//기본적으로 하나만 존재한다.
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
	 * 품질대상단말 정보 수정
	 */
	@Override
	public void modifyQualityMember(AddQualityMemberVo addqualitymemberVo, String userId, String userIp, String serviceType) throws Exception {
		
    	String folderName = GlobalCom.getTodayYear()+GlobalCom.getTodayMonth();

    	QualityMemberVo qualitymemberVo = new QualityMemberVo(GlobalCom.isNull(addqualitymemberVo.getSa_id()), GlobalCom.isNull(addqualitymemberVo.getLog_type())
    			, GlobalCom.isNull(addqualitymemberVo.getFind_type()), GlobalCom.isNull(addqualitymemberVo.getSize()),userId,userIp, GlobalCom.isNull(addqualitymemberVo.getLog_level()));
    	
    	modifyQualityMemFile(qualitymemberVo, folderName, serviceType);
		
    	//수정됐으니 리캐시!!
    	//refreshAdminCacheOfQuality();
    	qualityAdminApplyCache();
    	
	}
	
	@Override
	public void deleteQualityMember(String file_id, String serviceType) throws Exception {
		HTMLCleaner cleaner = new HTMLCleaner();
		
        String[] file_id_arr = file_id.split(",");
        for(String temp_id : file_id_arr){
        	String clean_id = cleaner.clean(temp_id);
        	//기존꺼 삭제하고 파일저장
    		String fName = clean_id+".qat";
        	List<File> rtnFile = findFile(fName, "equals", "P".equals(serviceType)?memberPortableFolderDir:memberFolderDir);
        	for(File delFile : rtnFile){
        		delFile.delete();
        	}
        }
        
    	//삭제됐으니 리캐시!!
    	//refreshAdminCacheOfQuality();
    	qualityAdminApplyCache();
	}
    
    /**
     * 파일 저장
     * @param qualitymemberVo
     * @param serviceType (2019.01.17 포터블TV값(P) 추가)
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
    	}else{//이전 해당 사번의 파일이 존재한다면
    		rtnCheck= 1;
    	}
    	return rtnCheck;
    }
    
    /**
     * 파일 수정
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
    	
    	//기존꺼 삭제하고 파일저장
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
	 * 해당 검색조건에 해당하는 데이터만 검색
	 * 포터블 TV 타입 값 비교 추가 2019.01.17
	 * @param qualitylistVo
	 * @return
	 * @throws Exception
	 */
	private List<QualityMemberCacheListVo> findQualityMemberList(QualityListVo qualitylistVo, String cacheYn) throws Exception{
		
		List<QualityMemberCacheListVo> resultList = new ArrayList<QualityMemberCacheListVo>();
		//방어적오르 특정 시간마다 캐시 갱신
		int interval = Integer.parseInt(GlobalCom.isNull(SmartUXProperties.getProperty("SmartUX_AdmQualityDao.refreshQuality.interval"),"86400000"));
		if("Y".equals(cacheYn)) interval = -1; //캐싱 전에 먼가 반영이 되었으니 즉시반영 해라.
		
		//세컨드 TV인 경우
		//방어용으로 특정 시간마다 리캐싱 하도록
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
				for(QualityMemberCacheListVo subVo : tempList){//조건이 있다면 해당 조건에 맞는 것만..
					if(("".equals(findValue) || subVo.getSa_id().contains(findValue)) && ("".equals(sLogType) || subVo.getLog_type().equals(sLogType))){
						resultList.add(subVo);
					}
				}
				
			}else{//조건이 없다면 전체가 대상
				resultList = tempList;
			}
		}

		if("Y".equals(cacheYn)) QualityAdminController.chacheYn = "N"; //캐시 반영 후 N으로 돌린다.
		return resultList;
	}
	
	/**
	 * 품질 수집 관리 cache적용
	 * -> 포터블 TV 정보가 추가되면서 cache 구조 변경
	 * @return
	 * @throws Exception 
	 */
	public Map<String, List<QualityMemberCacheListVo>> getQualityList() throws Exception{
		
		Map<String, List<QualityMemberCacheListVo>> resultMap = new HashMap<String, List<QualityMemberCacheListVo>>();
		
		//세컨드 TV 품질 수집 정보 
		List<QualityMemberCacheListVo> qualityList = getqualityFileData(memberFolderDir);
    	if(!CollectionUtils.isEmpty(qualityList)){
    		resultMap.put("T", qualityList);
    	}
		
		// 포터블TV 
    	List<QualityMemberCacheListVo> portableQualityList = getqualityFileData(memberPortableFolderDir);
    	if(!CollectionUtils.isEmpty(portableQualityList)){
    		resultMap.put("P", portableQualityList);
    	}
    	
		return resultMap;
	}
	
	/**
	 * 품질대상단말정보 fileData 조회
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
        			//구조가 이상한 파일은 리네임한다. .error로..
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
	 * 검색조건에 맞는 데이터 중 페이징 처리하여 리턴해 줌
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
    				continue;//해당 시작 넘버만큼 건너띈다.
    			}else if(qualitylistVo.getEnd_rnum() >= ArrNum){
	    			String saId = GlobalCom.isNull(GlobalCom.isNull(qualitymemVo.getSa_id()));
	    			String logType = GlobalCom.isNull(GlobalCom.isNull(qualitymemVo.getLog_type()));
	    			
	    			
	    			StringBuilder fileId = new StringBuilder();
	    			fileId.append(saId).append("^!").append(logType);
	    			
	    			List<File> findFile = findFile(fileId.toString()+".qat","equals", folderPath );
	        		for(File subFile : findFile){//똑같은 파일이 여러개면 안되지만 어쨋든 여러개 라면 같이 보여주자.(ArrNum는 +1 더 안해준다. 잘못된 데이터일테니까..)
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
	        		break;//끝 넘버 만큼 도달 하면 그만
	        	}
			}
		} catch (Exception e) {
			throw e;
		}
		
		return qualitymemberlistVo;
	}
	
	/**
	 * 해당 파일의 데이터를 추출하기 위해 파일을 찾는다.
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
	
	
	//각 관리자 서버로 데이터 변경이 있었으니 리캐시하라고 명령한다.(이놈은 리캐시하라는 상태 값만 바꿔준다. 실제로 해당 리스트페이지 들어갈때 리캐시한다.)
	private String qualityAdminApplyCache(){
		
		String resultcode = "Y";	
		
		try {
			//HDTV 프로퍼티로 부터 IP와 PORT 를 받는다. 
			String param = "callByScheduler=A"; // 관리자툴에서 호출한다는 의미로 셋팅해준다(이 값이 A여야 DB에서 바로 읽어서 캐쉬에 반영한다)
			int timeout = Integer.parseInt(SmartUXProperties.getProperty("scheduler.timeout"));		// timeout 값은 스케쥴러것을 사용
			int retrycnt = Integer.parseInt(SmartUXProperties.getProperty("scheduler.retrycnt"));	// 재시도 횟수는 스케줄러 것을 사용
			String url = "";
			String protocolName = "";
			
				// 게시글 목록 리스트 API 호출하여 캐쉬 갱신
			protocolName = SmartUXProperties.getProperty("SmartUX_AdmQualityDao.refreshQuality.protocol");
			url = SmartUXProperties.getProperty("SmartUX_AdmQualityDao.refreshQuality.CacheURL");
			GlobalCom.syncAdminServerCache(url, param, timeout, retrycnt, protocolName); 	// 다른 관리자 서버의 캐쉬 동기화 작업 진행(나중에 관리자모드 분리하면-이거 대상은 관리자서버IP를 동기화하도록 변경해야 한다.H_MIMS참조..)
			
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