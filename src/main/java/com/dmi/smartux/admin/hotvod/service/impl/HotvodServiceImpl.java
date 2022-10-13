package com.dmi.smartux.admin.hotvod.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmi.smartux.admin.common.dao.CommMimsDao;
import com.dmi.smartux.admin.hotvod.dao.HotvodDao;
import com.dmi.smartux.admin.hotvod.service.HotvodService;
import com.dmi.smartux.admin.hotvod.tag.HotvodUtil;
import com.dmi.smartux.admin.hotvod.vo.HotvodConst;
import com.dmi.smartux.admin.hotvod.vo.HotvodContentVO;
import com.dmi.smartux.admin.hotvod.vo.HotvodExcelVo;
import com.dmi.smartux.admin.hotvod.vo.HotvodFileUploadVO;
import com.dmi.smartux.admin.hotvod.vo.HotvodFilteringSiteVo;
import com.dmi.smartux.admin.hotvod.vo.HotvodSearchVO;
import com.dmi.smartux.admin.imcs.service.ImcsService;
import com.dmi.smartux.common.exception.SmartUXException;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.DateUtils;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.util.HTMLCleaner;
import com.ice.tar.TarArchive;
import com.ice.tar.TarBuffer;
import com.ice.tar.TarEntry;

/**
 * 화제동영상 - ServiceImpl
 * @author JKJ
 */
@SuppressWarnings("static-access")
@Service("HotvodService")
public class HotvodServiceImpl implements HotvodService {
	
	private final Log logger = LogFactory.getLog(this.getClass());
	private final Log HFULogger = LogFactory.getLog("hotvodFileUpload");
	
	@Autowired
	HotvodDao dao;
	
	@Autowired
	CommMimsDao mimsDao;
	
	@Autowired
	ImcsService imcsService;
	
	/**
	 * 컨텐츠 목록
	 */
	@Override
	public List<HotvodContentVO> contentList(HotvodSearchVO hotvodSearchVO) throws Exception {
		return dao.contentList(hotvodSearchVO);
	}
	
	/**
	 * 컨텐츠 목록갯수
	 */
	@Override
	public int contentListCnt(HotvodSearchVO hotvodSearchVO) throws Exception {
		return dao.contentListCnt(hotvodSearchVO);
	}
	
	/**
	 * 카테고리 상세/수정
	 */
	@Override
	public HotvodContentVO categoryDetail(HotvodSearchVO hotvodSearchVO) throws Exception {
		return dao.categoryDetail(hotvodSearchVO);
	}	
	
	/**
	 * 컨텐츠 상세/수정
	 */
	@Override
	public HotvodContentVO contentDetail(HotvodSearchVO hotvodSearchVO) throws Exception {
		return dao.contentDetail(hotvodSearchVO);
	}
	
	/**
	 * 컨텐츠 수정
	 */
	@Override
	@Transactional	
	public void contentUpdate(HotvodContentVO hotvodContentVO) throws Exception {
		if(StringUtils.isBlank(hotvodContentVO.getHit_cnt())){
			hotvodContentVO.setHit_cnt("0");
		} else {
			int ret = mimsDao.contentLogUpdate(hotvodContentVO);
			if(ret == 0) {
				mimsDao.contentLogInsert(hotvodContentVO);
			}
		}
		mimsDao.contentUpdate(hotvodContentVO);
		
		int contentExtYn = dao.contentExtYn(hotvodContentVO.getContent_id()); // 방어형 로직. EXT테이블에 없을수도 있기 때문에 
		if (contentExtYn < 1){
			dao.contentExtInsert(hotvodContentVO);
		} else{
			dao.contentExtUpdate(hotvodContentVO);
		}
		
		dao.contentCateDelete(hotvodContentVO);
		dao.cateContentInsert(hotvodContentVO);
	}
	
	/**
	 * 카테고리 수정
	 */
	@Override
	public void categoryUpdate(HotvodContentVO hotvodContentVO) throws Exception {
		if(StringUtils.isBlank(hotvodContentVO.getHit_cnt())){
			hotvodContentVO.setHit_cnt("0");
		}
		dao.categoryUpdate(hotvodContentVO);
	}	
	
	/**
	 * 컨텐츠 아이디 신규 채번
	 */
	@Override
	public String getContentId() throws Exception {
		return mimsDao.getContentId();
	}
	
	/**
	 * 컨텐츠 등록
	 */
	@Override
	@Transactional
	public void contentInsert(HotvodContentVO hotvodContentVO) throws Exception {
		if(StringUtils.isBlank(hotvodContentVO.getHit_cnt())){
			hotvodContentVO.setHit_cnt("0");
		} else {
		mimsDao.contentLogInsert(hotvodContentVO);
		}
		mimsDao.contentInsert(hotvodContentVO);
		dao.contentExtInsert(hotvodContentVO);
		dao.cateContentInsert(hotvodContentVO);
	}
	
	/**
	 * 카테고리 등록
	 */
	@Override
	public void categoryInsert(HotvodContentVO hotvodContentVO) throws Exception {
		if(StringUtils.isBlank(hotvodContentVO.getHit_cnt())){
			hotvodContentVO.setHit_cnt("0");
		}
		dao.categoryInsert(hotvodContentVO);
	}	
	
	/**
	 * 컨텐츠 아이디 중복검사
	 */
	@Override
	public int contentIdChk(String content_id) throws Exception {
		return dao.contentIdChk(content_id);
	}
	
	/**
	 * 카테고리 삭제
	 */
	@Override
	@Transactional
	public void categoryDelete(String content_id, String mod_id) throws Exception {
		HTMLCleaner cleaner = new HTMLCleaner();
		
		String[] content_id_arr = content_id.split(",");
        for(String temp_id : content_id_arr){
        	String clean_id = cleaner.clean(temp_id);
        	dao.categoryDelete(clean_id, mod_id);
        	dao.cateContentDelete(clean_id, mod_id);
        }
	}
	
	/**
	 * 컨텐츠 삭제
	 */
	@Override
	@Transactional
	public void contentDelete(String content_id, String mod_id, String parent_id) throws Exception {
		HTMLCleaner cleaner = new HTMLCleaner();
		String[] content_id_arr = content_id.split(",");

		for (int i=0; i<content_id_arr.length; i++) {
			String clean_id = cleaner.clean(content_id_arr[i]);
			int index = clean_id.indexOf("!^");
			String temp_id = clean_id.substring(0, index);
			String content_type = clean_id.substring(index+2);
			
			if (content_type.equals("C")) {
				dao.categoryDelete(temp_id, mod_id);
				dao.cateContentDelete(temp_id, mod_id);
			} else {
				dao.contentDelete(temp_id, mod_id, parent_id);
			}     
			
			//2021-04-22 BPAS 편성관리 추가
			try {
				imcsService.insertImcs("D", "HOT", temp_id, "", "", "");
			}catch (Exception e) {
				logger.error("[insertImcs]["+e.getClass().getName()+"]["+e.getMessage()+"]");
			}
		}

	}
	
	/**
	 * 상세화면 카테고리삭제, 하위컨텐츠 삭제
	 */
	@Override
	@Transactional
	public void contentDetailDelete(String content_id, String content_type, String mod_id) throws Exception {
		HTMLCleaner cleaner = new HTMLCleaner();
		String clean_id = cleaner.clean(content_id);
		dao.categoryDelete(clean_id, mod_id);
		dao.cateContentDelete(clean_id, mod_id);
	}
			
	/**
	 * 카테고리 목록 (팝업)
	 */
	@Override
	public List<HotvodContentVO> getCategoryList(String content_type) throws Exception {
		return dao.getCategoryList(content_type);
	}
	
	/**
	 * 순서바꾸기 목록 (팝업)
	 */
	@Override
	public List<HotvodContentVO> getChangeList(String content_id) throws Exception {
		List<HotvodContentVO> result = new ArrayList<HotvodContentVO>();
		
		// 최상단 카테고리 목록 조회
		if(StringUtils.isBlank(content_id)) {
			result = dao.getChangeList(null);
		} else {
			// 자식관계 카테고리 개수 조회
			int categoryCount = dao.getCategoryCount(content_id);
			
			if(categoryCount > 0) {
				// 상위 카테고리인 경우 하위 카테고리 리스트 조회
				result = dao.getChangeList(content_id);
			} else {
				// 하위 카테고리인 경우 현 카테고리에 포함된 컨텐츠 리스트 조회
				result = dao.getChangeContentList(content_id);
			}
		}
		
		return result;
	}
	
	/**
	 * 순서바꾸기 저장
	 */
	@Transactional
	public void changeSave(List<String> orderList, List<String> parList, String id) throws Exception {
		//HTMLCleaner cleaner = new HTMLCleaner();
		//String[] content_id_arr = ((StringUtils) orderList).split(",");
		//String[] par_id_arr = ((StringUtils) parList).split(",");
		HotvodContentVO hotvodContentVO = new HotvodContentVO();
		int idx = 1;
		
		for(int i=0; i<orderList.size();i++){
			hotvodContentVO = new HotvodContentVO();
        	String clean_id = orderList.get(i).toString();
        	int index = clean_id.indexOf("!^");
			String type_id = clean_id.substring(0, index);
			String content_type = clean_id.substring(index+2);
			String parId = "";
			if(!("").equals(parList) && parList != null) {
				parId = parList.get(i).toString();
			}
			
			hotvodContentVO.setParent_id(parId);
        	hotvodContentVO.setContent_id(type_id);
        	hotvodContentVO.setContent_order(Integer.toString(idx));
        	
        	if (content_type.equals("C")) {
        		dao.changeSave(hotvodContentVO);
        	} else {
        		dao.changeContentsSave(hotvodContentVO);
        	}        	
        	idx++;
		}
	}
	
	/**
	 * 하위 목록 (팝업)
	 */
	public List<HotvodContentVO> getSubList(HotvodSearchVO hotvodSearchVO) throws Exception {
		return dao.getSubList(hotvodSearchVO);
	}
	
	/**
	 * 하위 목록 갯수
	 * @param hotvodSearchVO
	 * @return
	 * @throws Exception
	 */
	public int getSubListCnt(HotvodSearchVO hotvodSearchVO) throws Exception {
		return dao.getSubListCnt(hotvodSearchVO);
	}
	
	/**
	 * 카테고리, 앨범 정보 조회
	 * @param choiceData
	 * @return
	 * @throws Exception
	 */
	public HotvodContentVO getAlbumCateInfo(String[] choiceData) throws Exception{
		Map<String,String> param = new HashMap<String, String>();
		param.put("category_id", choiceData[0]);
		param.put("album_id", choiceData[1]);
		return dao.getAlbumCateInfo(param);
	}
	
	/**
	 * 구간점프 이미지 가져오기
	 * @param albumId, startTime, endTime
	 * @return
	 * @throws Exception
	 */
	public String getContentImg(String albumId, String startTime, String endTime) throws Exception{
		
		String imgName = "";
		
		try{
			//구간점프 이미지 중에 설정된 시간사이의 이미지나 제일 가까운 시간의 이미지 찾기
			Map<String, String> imgMap = new HashMap<String, String>(); 
			imgMap = dao.getImgInfo(albumId);
			
			String timeList = imgMap.get("timeList");
			String time[] = timeList.split(",");
			int index = -1;
			int temp1 = -1;
			int temp2 = -1;
			int sTime = Integer.parseInt(startTime.replaceAll(":", ""));
			int eTime = Integer.parseInt(endTime.replaceAll(":", ""));
			
			String img = "";
			
			//종료시간이 구간점프 시간의 시작시간보다 작은 경우 첫 이미지로 지정
			if(eTime <= Integer.parseInt(time[0])){
				img = "01";
			}
			//시작시간이 구간점프 시간의 마지막 시간보다 큰 경우 마지막 이미지로 지정
			if(Integer.parseInt(time[time.length-1]) < sTime){
				img = "" + time.length;
			}
			
			if("".equals(img)){
				for(int i=0;i<time.length-1;i++){
					
					//설정한 시간에 구간점프 구간이미지가 있을 경우
					if(sTime <= Integer.parseInt(time[i]) && Integer.parseInt(time[i]) <= eTime){
						index = i;
						break;
					}
					//시작시간과 제일 가까운 시간
					if(Integer.parseInt(time[i]) < sTime){
						temp1 = i;
					}
					//종료시간과 제일 가까운 시간
					if(Integer.parseInt(time[i]) < eTime){
						temp2 = i + 1;
					}
				}
	
				//설정한 시간에 구간점프 이미지가 없을 경우 시작시간과 종료시간중에서 제일 가까운 이미지로 지정
				if(index == -1){
					int a = sTime - Integer.parseInt(time[temp1]);
					int b = Integer.parseInt(time[temp2]) - eTime;
					
					if(a <= b){
						img = "" + (temp1+1);
					}else{
						img = "" + (temp2+1);
					}
				}else{
					img = "" + (index+1);
				}
			}
			
			if(img.length() < 2){
				img = "0" + img;
			}
			//압축파일에서 선택된 이미지만 풀어서 가져온다.
			String zipFile = SmartUXProperties.getProperty("hotvod.poster.dir")+ imgMap.get("zipFile");
			ZipInputStream in = new ZipInputStream(new FileInputStream(zipFile));
			
			ZipEntry entry = null;
			
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			String year = sdf.format(date);
			sdf = new SimpleDateFormat("MM");
			String month = sdf.format(date);
			String subPath = year + "/" + month + "/";
			String filePath = SmartUXProperties.getProperty("hotvod.upload.dir");
			File pathChk = new File(filePath);
			
			if(!pathChk.exists()){
				pathChk.mkdirs();
			}
			
			while((entry = in.getNextEntry())!=null){
				if(entry.getName().startsWith(img)){
					String ext = entry.getName().substring(	entry.getName().lastIndexOf(".") + 1,	entry.getName().length()); // 확장자 구하기
					ext = ext.toLowerCase();
					imgName = subPath + Long.toString(System.currentTimeMillis()) + "."+ ext; // 시스템타임.확장자 구조로 한다
					String newFilename = filePath+ imgName;
					
					OutputStream out = new FileOutputStream(newFilename);
					byte[] buf = new byte[1024];
				    int len;
				    while ((len = in.read(buf)) > 0) {
				    	out.write(buf, 0, len);
				    }
				    
				    in.closeEntry();
					out.close();
				}
			}
			in.close();
		}catch(Exception e){
			return "";
		}
		
		return imgName;
	}
	
	/**
	 * 상위카테고리의 노출여부 조회 
	 * @param hotvodContentVO
	 * @return
	 * @throws Exception
	 */
	public HotvodContentVO getParentContentDelYn(HotvodContentVO hotvodContentVO) throws Exception{
		
		HotvodContentVO result = new HotvodContentVO();
		String content_type = hotvodContentVO.getContent_type();
				
		if (content_type.equals("C")) {
			result = dao.getParentCategoryDelYn(hotvodContentVO);
		} else {
			result = dao.getParentContentDelYn(hotvodContentVO);
		}
		
		return result;
	}
	
	/**
	 * 대박영상 통합검색 파일업로드
	 * @throws Exception
	 */
	public void hotvodFileUpload() throws Exception{
		HFULogger.info("#### hotvod fileUpload start ####");
		String errorFlag = "0000";
		String errorMessage = "";
		
		try{
			Map<String, Integer> map = GlobalCom.getServerAdminIndex();
	    	
	        int total = map.get("total");
	        int index = map.get("index");
	        
	        Calendar cal = Calendar.getInstance();
	        int d = cal.get(Calendar.DATE);
	        if (d % total == index) {
	        	HFULogger.info("#### hotvod fileUpload [My turn] ");
	        	
	        	String filePath = SmartUXProperties.getProperty("hotvod.fileupload.path");
	    		String fileName = SmartUXProperties.getProperty("hotvod.fileupload.fileName");
	    		String hitFileName = SmartUXProperties.getProperty("hotvod.fileupload.hitFileName");
	    		String txtFile = filePath + fileName;
	    		String hitTxtFile = filePath + hitFileName;
	    		
	    		String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	    		String tarFile = filePath + "HOTVOD" + date + ".tar";
	    		String gzFile = tarFile + ".gz";
	    		String imgIServerUrl = "";
	    		String imgPServerUrl = "";
	    		
	    		List<HotvodFileUploadVO> list = null;
	    		List<HotvodFileUploadVO> highList = null;
	    		
	    		OutputStreamWriter writer = null;
	    		OutputStreamWriter hitWriter = null;
	    		OutputStreamWriter enWriter = null;
	    		
	    		try{
	    			//텍스트 파일 생성
	    			HFULogger.info("#### hotvod fileUpload [write text] start ");
	    			
	    			File dirChk = new File(filePath);
					if(!dirChk.exists()){
						dirChk.mkdirs();
					}
					
					imgIServerUrl = dao.getImgServerUrl("I");
					imgPServerUrl = dao.getImgServerUrl("P");
					
					list = dao.getHotvodListAll();
					highList = dao.getHotvodHighListAll();
					
					if(list != null) {
						HFULogger.info("#### hotvod fileUpload [write text] listSize : " + list.size());
					}
					if(highList != null) {
						HFULogger.info("#### hotvod fileUpload [write text] highList : " + highList.size());
					}
					
					writer = new OutputStreamWriter(new FileOutputStream(txtFile), "EUC-KR");
					hitWriter = new OutputStreamWriter(new FileOutputStream(hitTxtFile), "EUC-KR");
					
					HotvodFileUploadVO vo = null;
					
					/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					
					// 화제동영상 메타파일 생성(I^IPTV)
					String meta_type_list = GlobalCom.isNull(SmartUXProperties.getProperty("hotvod.metafile.list"), HotvodConst.DEFAULT_HOTVOD_METAFILE_LIST);
					if(StringUtils.isNotBlank(meta_type_list)) {
						String[] meta_type = meta_type_list.split("\\|");
	
						//IPTV
						String filtering_svcType_list = GlobalCom.isNull(SmartUXProperties.getProperty("hotvod.filtering.svcType"), HotvodConst.DEFAULT_HOTVOD_FILTERING_SVC_LIST);
						String[] filtering_svcType = filtering_svcType_list.split("\\|");
	
						if(meta_type.length > 0) {
							for(int i=0;i<meta_type.length;i++) {
								if(filtering_svcType.length > i) {
									this.HotVodMetaFileCreate(meta_type[i], filtering_svcType[i], imgIServerUrl, writer);
								} else {
									this.HotVodMetaFileCreate(meta_type[i], null, imgIServerUrl, writer);
								}
							}
						}
					}
					
					/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					
					String line = "";
					int lineCount = 0;
					
					if(highList != null) {
						
						for(int i=0;i<highList.size();i++){
							vo = new HotvodFileUploadVO();
							vo = highList.get(i);
							
							// 디비부하를 막기  위해, 쿼리에서 이미지url 함수를 사용하지 않기 위해 수정
							if (vo.getImg_url_hdtv() != null) {
								vo.setImg_url_hdtv(imgIServerUrl+"smartux/hotvod/"+vo.getImg_url_hdtv());
							}
							if (vo.getStill_img() != null) {
								vo.setStill_img(imgPServerUrl+"still/"+vo.getStill_img());
							}						
							
							if(HotvodConst.CONTENT_TYPE.HIGHLIGHT.equals(vo.getContent_type())){
								line = "HLT^|IPTV^|" + GlobalCom.isNull(vo.getCat_id())
										+ "^|" + GlobalCom.isNull(vo.getCat_name())
										+ "^|" + GlobalCom.isNull(vo.getContent_id())
										+ "^|" + GlobalCom.isNull(vo.getContent_type())
										+ "^|" + GlobalCom.isNull(vo.getContent_name())
										+ "^|" + GlobalCom.isNull(vo.getContent_info()).replaceAll("\r\n", "")
										+ "^|" + GlobalCom.isNull(vo.getImg_url_hdtv())
										+ "^|" + GlobalCom.isNull(vo.getDuration())
										+ "^|" + GlobalCom.isNull(vo.getHit_cnt())
										+ "^|" + GlobalCom.isNull(vo.getAlbum_id())
										+ "^|" + GlobalCom.isNull(vo.getCategory_id())
										+ "^|" + GlobalCom.isNull(vo.getStart_time())
										+ "^|" + GlobalCom.isNull(vo.getEnd_time())
										+ "^|" + GlobalCom.isNull(vo.getContents_name())
										+ "^|" + GlobalCom.isNull(vo.getSeries_no())
										+ "^|" + GlobalCom.isNull(vo.getSeries_desc())
										+ "^|" + GlobalCom.isNull(vo.getSponsor_name())
										+ "^|" + GlobalCom.isNull(vo.getStill_img())
										+ "^|" + GlobalCom.isNull(vo.getReg_date())
										+ "^|<<<EOL>>>\n";
								lineCount ++;
								hitWriter.write(line);
								hitWriter.flush();
							}
						}
					}
					
					HFULogger.info("#### hotvod fileUpload [write text] HLT|IPTV lineCount : " + lineCount);					
					
					HFULogger.info("#### hotvod fileUpload [write text] complate ");
	    		}catch(Exception e){
	    			HFULogger.error("#### hotvod fileUpload [write text] : " + e.getClass().getName()); 
	    			HFULogger.error("#### hotvod fileUpload [write text] : " + e.getMessage());
	    			errorFlag = "1000";
	    			errorMessage = "텍스트파일 생성실패";
					throw e;
	    		}finally{
					if(null != writer){
						writer.close();
					}
					if(null != hitWriter){
						hitWriter.close();
					}
					if(null != enWriter){
						enWriter.close();
					}
				}
	        	
				try{
					HFULogger.info("#### hotvod fileUpload [write gzFile] start ");
					
					int blockSize = TarBuffer.DEFAULT_BLKSIZE;
					TarArchive archive = new TarArchive( new FileOutputStream(tarFile), blockSize );
					
					TarEntry entry = null;
					
					String fileNameList[] = new String[]{fileName, hitFileName};
					String txtFileList[] = new String[]{txtFile, hitTxtFile};
					
					//압축파일 생성
					for(int i=0;i<fileNameList.length;i++){
						entry = new TarEntry( new File(txtFileList[i]));
						entry.setName(fileNameList[i]);
						archive.writeEntry( entry, true );
					}
					archive.closeArchive();
					
					FileInputStream fis=new FileInputStream(new File(tarFile));
					FileOutputStream fos=new FileOutputStream(gzFile);
					GZIPOutputStream gos=new GZIPOutputStream(fos);
					byte[] buf = new byte[1024];
					
					for (int i; (i=fis.read(buf))!=-1; ) {
						gos.write(buf, 0, i);
					}
					gos.close();
					fis.close();
					
					HFULogger.info("#### hotvod fileUpload [write gzFile] complate ");
				}catch(Exception e){
					HFULogger.error("#### hotvod fileUpload [write gzFile] : " + e.getClass().getName());
					HFULogger.error("#### hotvod fileUpload [write gzFile] : " + e.getMessage());
					errorFlag = "1001";
					errorMessage = "압축파일 생성실패";
					throw e;
				}
				
				try{
					
					String Ip = SmartUXProperties.getProperty("hotvod.ftp.ip");
					String Port = SmartUXProperties.getProperty("hotvod.ftp.port");
					String Id = SmartUXProperties.getProperty("hotvod.ftp.id");
					String Pw = SmartUXProperties.getProperty("hotvod.ftp.pw");
					String Path = SmartUXProperties.getProperty("hotvod.ftp.path");
					
					if( StringUtils.isBlank(Ip) ||
						StringUtils.isBlank(Port) || 
						StringUtils.isBlank(Id) ||
						StringUtils.isBlank(Pw) ||
						StringUtils.isBlank(Path) ) {
						HFULogger.info("#### hotvod fileUpload [ftp connect info empty]");
						return;	
					}
					
					// FTP 업로드 시작
					String ftpIp[] = Ip.split("\\|");
					String ftpPort[] = Port.split("\\|");
					String ftpId[] = Id.split("\\|");
					String ftpPw[] = Pw.split("\\|");
					String ftpPath[] = Path.split("\\|");
					
					for(int i=0;i<ftpIp.length;i++){
						for(int j=0;j<3;j++){
							if("0000".equals(this.ftpUploadProc(ftpIp[i], ftpPort[i], ftpId[i], ftpPw[i], ftpPath[i], gzFile))){
								break;
							}
						}
					}
					//FTP 업로드 종료
				}catch (Exception e){
					HFULogger.error("#### hotvod fileUpload [ftp upload] : " + e.getClass().getName());
					HFULogger.error("#### hotvod fileUpload [ftp upload] : " + e.getMessage());
					errorFlag = "1002";
					errorMessage = "FTP 업로드 실패";
					throw e;
				}
				
				try{
					//파일 삭제
					HFULogger.info("#### hotvod fileUpload [delete file] : start");
					File file = null;
					String delFiles[] = new String[]{txtFile, hitTxtFile, tarFile};
					System.gc();
					for(int i=0;i<delFiles.length;i++){
						file = new File(delFiles[i]);
						if(file.exists()){
							boolean del = file.delete();
							HFULogger.info("#### hotvod fileUpload [delete file] : " + file.getName() + ", " + del);
						}
					}
					HFULogger.info("#### hotvod fileUpload [delete file] : complate"); 
				}catch(Exception e){
					HFULogger.error("#### hotvod fileUpload [delete file] : " + e.getClass().getName()); 
					HFULogger.error("#### hotvod fileUpload [delete file] : " + e.getMessage()); 
				}
	        }
		}catch(Exception e){
			HFULogger.error("#### hotvod fileUpload : " + e.getClass().getName()); 
			HFULogger.error("#### hotvod fileUpload : " + e.getMessage()); 
			e.printStackTrace();
		} finally {
			if(!errorFlag.equals("0000")){
				throw new SmartUXException(errorFlag, errorMessage); 
			}
			
			HFULogger.info("#### hotvod fileUpload end ####");
		} 
	}
	
	/**
	 * 통합검색 메타파일 생성
	 * @param meta_type (메타파일 구분코드)
	 * @param filtering_svcType (필터링 사이트 서비스코드)
	 * @param imgIServerUrl (이미지url)
	 * @param writer (파일 스트림)
	 */
	protected void HotVodMetaFileCreate(String meta_type, String filtering_svcType, String imgIServerUrl, OutputStreamWriter writer) {
		
		//메타파일 구분자
		final String separator = "^|";
		
		List<HotvodFileUploadVO> hotvodList = null;
		List<HotvodFilteringSiteVo> filteringSiteList = null;
		
		boolean filteringCheck = false;
		HotvodFilteringSiteVo filteringSiteVO = new HotvodFilteringSiteVo();
		int lineCount = 0;
		StringBuilder line = new StringBuilder();
		
		try {
			String[] types = meta_type.split("\\^");
			String typeCheck = HotvodUtil.toDecStringByService(types[0]);
			// types[0] = 서비스코드
			// types[1] = 메타파일코드
			hotvodList = mimsDao.getHotvodServiceListAll(typeCheck);
			
			HFULogger.info("#### hotvod fileUpload [write text] [meta_type="+ meta_type+"] [filterSiteType="+ filtering_svcType + "]");
			
			if(CollectionUtils.isNotEmpty(hotvodList)) {
			
				HFULogger.info("#### hotvod fileUpload [write text] [meta_service="+ types[1]+"]" + " hotvodList : " + hotvodList.size());

				for(int i=0;i<hotvodList.size();i++){
					HotvodFileUploadVO vo = new HotvodFileUploadVO();
					vo = hotvodList.get(i);
					filteringCheck = false;

					if( HotvodConst.CONTENT_TYPE.CONTENTS.equals(vo.getContent_type()) ){
						
						if(StringUtils.isNotBlank(filtering_svcType)) {
							filteringSiteVO.setSvcType(filtering_svcType);
							filteringSiteList = this.getFilteringSiteList(filteringSiteVO);
						}

						if(CollectionUtils.isNotEmpty(filteringSiteList) && StringUtils.isNotBlank(vo.getContent_url())){
							for(HotvodFilteringSiteVo filteringSite : filteringSiteList){
								if(vo.getContent_url().startsWith(filteringSite.getSiteUrl())){
									filteringCheck = true;
									break;
								}
							}
						}
						if(filteringCheck == true){
							continue;
						}

						// 디비부하를 막기  위해, 쿼리에서 이미지url 함수를 사용하지 않기 위해 수정
						// HDTV파일생성시 여기서 셋팅한 list url정보를 가져다 쓰므로(call by reference) 여기서만 이미지정보 셋팅
						if (vo.getImg_url_hdtv() != null) {								
							vo.setImg_url_hdtv(imgIServerUrl+"smartux/hotvod/"+vo.getImg_url_hdtv());
						}
						if (vo.getImg_url_iptv() != null) {								
							vo.setImg_url_iptv(imgIServerUrl+"smartux/hotvod/"+vo.getImg_url_iptv());
						}
						if (vo.getSite_icon_hdtv() != null) {									
							vo.setSite_icon_hdtv(imgIServerUrl+"smartux/hotvod/"+vo.getSite_icon_hdtv());
						}
						if (vo.getSite_icon_iptv() != null) {								
							vo.setSite_icon_iptv(imgIServerUrl+"smartux/hotvod/"+vo.getSite_icon_iptv());
						}

						line.delete(0, line.length());
						
						line.append("HVD").append(separator).append(types[1]).append(separator).append(GlobalCom.isNull(vo.getCat_id()))
						.append(separator).append(GlobalCom.isNull(vo.getCat_name()))
						.append(separator).append(GlobalCom.isNull(vo.getContent_id()))
						.append(separator).append(GlobalCom.isNull(vo.getContent_type()))
						.append(separator).append(GlobalCom.isNull(vo.getContent_name()))
						.append(separator).append(GlobalCom.isNull(vo.getContent_info()).replaceAll("\r\n", ""))
						.append(separator).append(GlobalCom.isNull(vo.getContent_url()))
						.append(separator).append(GlobalCom.isNull(vo.getImg_url_hdtv()))
						.append(separator).append(GlobalCom.isNull(vo.getImg_url_iptv()))
						.append(separator).append(GlobalCom.isNull(vo.getDuration()))
						.append(separator).append(GlobalCom.isNull(vo.getHit_cnt()))
						.append(separator).append(GlobalCom.isNull(vo.getSite_id()))
						.append(separator).append(GlobalCom.isNull(vo.getSite_name()))
						.append(separator).append(GlobalCom.isNull(vo.getSite_url()))
						.append(separator).append(GlobalCom.isNull(vo.getSite_icon_hdtv()))
						.append(separator).append(GlobalCom.isNull(vo.getSite_icon_iptv()))
						.append(separator).append(GlobalCom.isNull(vo.getReg_date()))
						.append(separator).append("<<<EOL>>>\n");
					
						lineCount ++;
						writer.write(line.toString());
						writer.flush();
					}
				}
			}

			HFULogger.info("#### hotvod fileUpload [write text] HVD|"+types[1]+" lineCount : " + lineCount);
		
		} catch(Exception e) {
			HFULogger.error("HotVodMetaFileCreate", e);
		}
	}
	
	/**
	 * FTP 업로드 로직
	 * @param ip
	 * @param port
	 * @param id
	 * @param pw
	 * @param path
	 * @param gzFile
	 * @return
	 */
	public String ftpUploadProc(String ip, String port, String id, String pw, String path, String gzFile) {
		FTPClient ftp = null;
		FileInputStream fis = null;
		
		boolean isLogin = false;
		boolean isUpload = false;
		
		String resultFlag = "0000";
		
		try{
			try{
				
				HFULogger.info("#### hotvod fileUpload [FTP Upload] start : " + ip + ":" + port);
				ftp = new FTPClient();
				ftp.setControlEncoding("EUC-KR");
				ftp.connect(ip, Integer.parseInt(port));
				isLogin = ftp.login(id, pw);
				
				if(isLogin){
					HFULogger.info("#### hotvod fileUpload [FTP Upload] login ");
					File uploadFile = new File(gzFile);
					fis = new FileInputStream(uploadFile);
					ftp.enterLocalPassiveMode();
					ftp.setKeepAlive(true);
					ftp.setControlKeepAliveTimeout(30);
					ftp.setFileType(FTP.BINARY_FILE_TYPE);
					ftp.cwd(path);
					HFULogger.info("#### hotvod fileUpload [FTP Upload] CWD : " + path);
					isUpload = ftp.storeFile(uploadFile.getName(), fis);
					HFULogger.info("#### hotvod fileUpload [FTP Upload] getReplyString : " + ftp.getReplyString());
					
					if(isUpload == true){
						HFULogger.info("#### hotvod fileUpload [FTP Upload] complate ");
					}else{
						HFULogger.info("#### hotvod fileUpload [FTP Upload] upload fail ");
						resultFlag = "1002";
					}
				}else{
					HFULogger.info("#### hotvod fileUpload [FTP Upload] login fail");
					resultFlag = "1003";
				}
			}catch(Exception e){
				HFULogger.info("#### hotvod fileUpload [FTP Upload] : " + e.getClass().getName());
				HFULogger.info("#### hotvod fileUpload [FTP Upload] : " + e.getMessage());
				resultFlag = "1002";
			}finally{
				if(null != fis){
					fis.close();
				}
				if(isLogin){
					ftp.logout();
					HFULogger.info("#### hotvod fileUpload [FTP Upload] logout ");
				}
				if (ftp != null && ftp.isConnected()) {
					try {ftp.disconnect();HFULogger.info("#### hotvod fileUpload [FTP Upload] disconnect ");} catch (IOException e) {}
				}
			}
			
		}catch(Exception e){
			HFULogger.info("#### hotvod fileUpload [FTP Upload] : " + e.getClass().getName()); 
			HFULogger.info("#### hotvod fileUpload [FTP Upload] : " + e.getMessage()); 
			resultFlag = "1002";
		}
		
		return resultFlag;
	}
	
	private String isNew(Date newDate) {
		String returnValue = "N";
		
		if(newDate == null){
			return returnValue;
		}
		
		Date currentDate = new Date();

		if(DateUtils.DateStr(GlobalCom.getTodayFormat(), newDate)){
			returnValue = "Y";
		}
		
		return returnValue;
	}
	
	

	/**
	 * 뱃지 데이터 체크 
	 * @param hotvodContentVO
	 * @return
	 * @throws Exception
	 */
	public HashMap<String,Boolean> getBadgeDataInsertYn(String content_id,String type) throws Exception
	{
		HashMap<String,Boolean> resultMap = new HashMap<String,Boolean>();
		boolean isValid = true;
		
		if("".equals(content_id) || "".equals(type))
			isValid = false;
				
		if(isValid)
		{
			int hotvodBadgeMaxSize = HotvodUtil.getHotvodBadgeMaxSize();
		}
		
		resultMap.put("isValid", isValid);
		
		return resultMap;
	}
	
	public String setAddstr(String value,int strLen)
	{		
		while(value.length() < strLen)
		{
			value = "0"+value;
		}
		return value;
	}
	
	public String toBinaryString(String value)
	{
		try
		{			
			String hotvodBadgeList = GlobalCom.isNull(SmartUXProperties.getProperty("hotvod.badge.list"), HotvodConst.DEFAULT_HOTVOD_BADGE_LIST);
			if(value == null)
				value = "0";
			if("".equals(value))
				value = "0";
			
			String binaryStr = Integer.toBinaryString(Integer.parseInt(value));
			String binaryBadge = setAddstr(binaryStr, hotvodBadgeList.split("\\|").length);
			return ( new StringBuffer(binaryBadge) ).reverse().toString();
		}catch(Exception e){
			return setAddstr("0", 11);
		}
		
	}

	/* (non-Javadoc)
	 * @see com.dmi.hdtv3.admin.hotvod.service.HotvodService#getExcelCategory(com.dmi.hdtv3.admin.hotvod.vo.HotvodSearchVO)
	 */
	@Override
	public List<HotvodExcelVo> getExcelCategory(HotvodSearchVO hotvodSearchVO) throws Exception {
		return dao.getExcelCategory(hotvodSearchVO);
	}

	/**
	 * 컨텐츠 복사존재 여부 체크
	 */
	@Override
	public int contentCopyChk(String parent_id, String content_id) throws Exception {
		
		String[] content_id_arr = content_id.split(",");
		int result = 0;
		for (String temp_id : content_id_arr){
			result += dao.contentCopyChk(parent_id, temp_id);
		}
		return result;
	}
	
	/**
	 * 컨텐츠 복사
	 */
	@Override
	@Transactional
	public void contentCopy(String parent_id, String content_id) throws Exception {
		
		String[] content_id_arr = content_id.split(",");
		int order = 1; // 복사한 컨텐츠는 최상단에 위치
		
		for (String temp_id : content_id_arr) {
			if(StringUtils.equals("Y", dao.contentDelYn(parent_id, temp_id))) {
				dao.contentDelYnUpdate(parent_id, temp_id, "N", order);
			} else {
				dao.contentCopy(parent_id, temp_id, order);
			} 
		}
	}
	
	@Override
	@Transactional
	public void parentCateUpdate(HotvodContentVO hotvodContentVO) throws Exception {
		dao.cateContentSelectInsert(hotvodContentVO);
		dao.contentCateDelete(hotvodContentVO);
	}
		
	/**
	 * 카테고리 정보조회
	 * @param cate_no
	 * @return
	 */
	@Override
	public Map getCateInfo(String cate_no) {
		//TODO 카테고리 정보조회
		return null;
		//return (Map)getSqlMapClientTemplate().queryForObject("paper.getCateInfo",cate_no);	
	}
	
	/**
	 * 필터링사이트 목록
	 * @param HotvodFilteringSiteVo
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<HotvodFilteringSiteVo> getFilteringSiteList(HotvodFilteringSiteVo filtSiteVo) throws Exception {

		List<HotvodFilteringSiteVo> siteList = new ArrayList<HotvodFilteringSiteVo>();
		
		try {
			String filePath = SmartUXProperties.getProperty("filteringsite.dir");
			String fileName = filePath + filtSiteVo.getSvcType().toLowerCase() + ".fts";
			
			// If The File Is Existed,
			if (new File(fileName).exists()) {
				// Check Number Of Lines.
				Scanner reader = new Scanner(new FileReader(fileName));
				while (reader.hasNextLine()) {
					String[] row = (reader.nextLine()).split("\\|");
					HotvodFilteringSiteVo siteVo = new HotvodFilteringSiteVo();
					siteVo.setOrderNo(row[0]);
					siteVo.setSiteUrl(row[1]);
					siteVo.setRegDate(row[2]);
					siteList.add(siteVo);
				}
				reader.close();
			}
		}
		catch (IOException e) {
			logger.error("getFilteringSiteList:"+e.getClass().getName());
			logger.error("getFilteringSiteList:"+e.getMessage());
		}
		return siteList;
	}
}
