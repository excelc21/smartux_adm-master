package com.dmi.smartux.admin.hotvod.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.dmi.smartux.admin.hotvod.dao.HotvodIptvDao;
import com.dmi.smartux.admin.hotvod.service.HotvodIptvService;
import com.dmi.smartux.admin.hotvod.vo.HotvodHitstatsVO;
import com.dmi.smartux.admin.hotvod.vo.HotvodSearchVO;
import com.dmi.smartux.admin.hotvod.vo.HotvodSiteVO;
import com.dmi.smartux.common.exception.ExceptionHandler;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.HTMLCleaner;
import com.dmi.smartux.common.vo.CUDResult;
import com.dmi.smartux.common.vo.HotvodFilteringSiteVo;

import net.sf.json.JSONSerializer;

/**
 * 화제동영상 - ServiceImpl
 * @author JKJ
 */
@SuppressWarnings("static-access")
@Service("HotvodIptvService")
public class HotvodIptvServiceImpl implements HotvodIptvService {
	
	private final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	HotvodIptvDao dao;
	
	/**
	 * 사이트 목록
	 */
	@Override
	public List<HotvodSiteVO> siteList(HotvodSearchVO hotvodSearchVO) throws Exception {
		return dao.siteList(hotvodSearchVO);
	}
	
	/**
	 * 사이트 목록갯수
	 */
	@Override
	public int siteListCnt(HotvodSearchVO hotvodSearchVO) throws Exception {
		return dao.siteListCnt(hotvodSearchVO);
	}
	
	/**
	 * 사이트 아이디 신규 채번
	 */
	@Override
	public String getSiteId() throws Exception{
		return dao.getSiteId();
	}
	
	/**
	 * 사이트 상세/수정
	 */
	@Override
	public HotvodSiteVO siteDetail(HotvodSearchVO hotvodSearchVO) throws Exception {
		return dao.siteDetail(hotvodSearchVO);
	}
	
	/**
	 * 사이트 수정
	 */
	@Override
	public void siteUpdate(HotvodSiteVO hotvodSiteVO) throws Exception {
		dao.siteUpdate(hotvodSiteVO);
	}
	
	/**
	 * 사이트 등록
	 */
	@Override
	public void siteInsert(HotvodSiteVO hotvodSiteVO) throws Exception {
		dao.siteInsert(hotvodSiteVO);
	}
	
	/**
	 * 사이트 아이디 중복검사
	 */
	@Override
	public int siteIdChk(String site_id) throws Exception {
		return dao.siteIdChk(site_id);
	}
	
	/**
	 * 사이트 삭제
	 */
	@Override
	public void siteDelete(String site_id) throws Exception {
		HTMLCleaner cleaner = new HTMLCleaner();
		
		String[] site_id_arr = site_id.split(",");
        for(String temp_id : site_id_arr){
        	String clean_id = cleaner.clean(temp_id);
        	dao.siteDelete(clean_id);
        }
	}
	
	/**
	 * 사이트 목록 (콤보박스)
	 */
	@Override
	public List<HotvodSiteVO> getSiteList() throws Exception {
		return dao.getSiteList();
	}
	
	/**
	 * 조회수 통계 목록
	 * @param hotvodSearchVO
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public List<HotvodHitstatsVO> getHitstatsList(HotvodSearchVO hotvodSearchVO) throws Exception{
		if(hotvodSearchVO.getStartDt().equals("")){
			Date d = new Date();
			int year;
			int month;
			int date;
			String dt = "";
			d.setDate(d.getDate() - 1);
			year =  d.getYear() + 1900;
			month = d.getMonth()+1;
			date = d.getDate();
			if(month < 10){
				dt = ""+year+"0"+month;
			}else{
				dt = ""+year+""+month;
			}
			if(date < 10){
				dt = dt + "0" + date;
			}else{
				dt = dt + "" + date;
			}
			hotvodSearchVO.setEndDt(dt);
			
			d.setDate(d.getDate() - 7);
			year = d.getYear() + 1900;
			month = d.getMonth()+1;
			date = d.getDate();
			dt = ""+year+""+month+""+date;
			
			if(month < 10){
				dt = ""+year+"0"+month;
			}else{
				dt = ""+year+""+month;
			}
			if(date < 10){
				dt = dt + "0" + date;
			}else{
				dt = dt + "" + date;
			}
			hotvodSearchVO.setStartDt(dt);
		}
		
		hotvodSearchVO.setStartDt(hotvodSearchVO.getStartDt().replace("-", ""));
		hotvodSearchVO.setEndDt(hotvodSearchVO.getEndDt().replace("-", ""));
		return dao.getHitstatsList(hotvodSearchVO);
	}

	/**
	 * 필터링사이트 목록
	 * @param HotvodFilteringSiteVo
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<HotvodFilteringSiteVo> getFilteringSiteList(
			HotvodFilteringSiteVo filtSiteVo) throws Exception {

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

	/**
	 * 필터링사이트 등록
	 * @param HotvodFilteringSiteVo
	 * @return
	 * @throws Exception
	 */
	@Override
	public ResponseEntity<String> regFilteringSite(
			HotvodFilteringSiteVo filtSiteVo) throws Exception {
		
		final CUDResult result = new CUDResult();
		String resCode = "";
		String resMessage = "";
		
		try {
			String filePath = SmartUXProperties.getProperty("filteringsite.dir");
			String fileName = filePath + filtSiteVo.getSvcType().toLowerCase() + ".fts";
			int lineCount = 1;
			
			// If The File Is Existed,
			if(new File(fileName).exists()){
				// Check Number Of Lines.
				BufferedReader reader = new BufferedReader(new FileReader(fileName));
				while (reader.readLine()!=null) {
					lineCount++;
				}
				reader.close();
			}
			
			// Set Ordering Number.
			filtSiteVo.setOrderNo(String.valueOf(lineCount));
			
			// Set Registering Date.
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.KOREA);
			GregorianCalendar Gcal = new GregorianCalendar();
			String currentDate = sdf.format(Gcal.getTime());
			filtSiteVo.setRegDate(currentDate);

			// Write Text.(If the file is not existed, then create a new file.) 
			BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
			String str = filtSiteVo.getOrderNo() + "|" + filtSiteVo.getSiteUrl() + "|" + filtSiteVo.getRegDate();
			writer.write(str);
			writer.newLine();
			writer.close();
			
			// set Result.
			result.setFlag(SmartUXProperties.getProperty("flag.success"));
			result.setMessage(SmartUXProperties.getProperty("message.success"));
		}
		catch (IOException e) {
			logger.error("regFilteringSite:"+e.getClass().getName());
			logger.error("regFilteringSite:"+e.getMessage());
			
			ExceptionHandler handler = new ExceptionHandler(e);
			resCode 	= handler.getFlag();
			resMessage 	= handler.getMessage();
		}
		
		String resultMsg = "{\"flag\" : \"" + result.getFlag() + "\", \"message\" : \"" + result.getMessage() + "\"}";
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(resultMsg).toString(), responseHeaders, HttpStatus.OK);
	}

	/**
	 * 필터링사이트 수정
	 * @param HotvodFilteringSiteVo
	 * @return
	 * @throws Exception
	 */
	@Override
	public ResponseEntity<String> modFilteringSite(
			HotvodFilteringSiteVo filtSiteVo) throws Exception {

		final CUDResult result = new CUDResult();
		String resCode = "";
		String resMessage = "";
		
		try {
			String filePath = SmartUXProperties.getProperty("filteringsite.dir");
			String fileName = filePath + filtSiteVo.getSvcType().toLowerCase() + ".fts";
			
			// If The File Is Existed,
			if (new File(fileName).exists()) {
				List<HotvodFilteringSiteVo> siteList = new ArrayList<HotvodFilteringSiteVo>();
				
				// Create Current Site List.
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
				
				// Update Site URL And Date.
				int idx = Integer.parseInt(filtSiteVo.getOrderNo())-1;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.KOREA);
				GregorianCalendar Gcal = new GregorianCalendar();
				String modDate = sdf.format(Gcal.getTime());
				String siteUrl= filtSiteVo.getSiteUrl();
				siteList.get(idx).setRegDate(modDate);
				siteList.get(idx).setSiteUrl(siteUrl);
			
				// Delete Old File.
				File file = new File(fileName);
				file.delete();
				
				// Write New Text.
				BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
				for (int i=0;i<siteList.size();i++) {
					String str = siteList.get(i).getOrderNo() + "|" + siteList.get(i).getSiteUrl() + "|" + siteList.get(i).getRegDate();
					writer.write(str);
					writer.newLine();
				}
				writer.close();
				
				// set Result.
				result.setFlag(SmartUXProperties.getProperty("flag.success"));
				result.setMessage(SmartUXProperties.getProperty("message.success"));
			}
		}
		catch (IOException e) {
			logger.error("modFilteringSite:"+e.getClass().getName());
			logger.error("modFilteringSite:"+e.getMessage());
			
			ExceptionHandler handler = new ExceptionHandler(e);
			resCode = handler.getFlag();
			resMessage 	= handler.getMessage();
		}
		
		String resultMsg = "{\"flag\" : \"" + result.getFlag() + "\", \"message\" : \"" + result.getMessage() + "\"}";
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(resultMsg).toString(), responseHeaders, HttpStatus.OK);
	}

	/**
	 * 필터링사이트 삭제
	 * @param HotvodFilteringSiteVo
	 * @return
	 * @throws Exception
	 */
	@Override
	public ResponseEntity<String> delFilteringSite(
			HotvodFilteringSiteVo filtSiteVo) throws Exception {

		//Map<String, String> result = new HashMap<String, String>();
		final CUDResult result = new CUDResult();
		String resCode = "";
		String resMessage = "";
		
		try {
			String filePath = SmartUXProperties.getProperty("filteringsite.dir");
			String fileName = filePath + filtSiteVo.getSvcType().toLowerCase() + ".fts";
			
			// If The File Is Existed,
			if (new File(fileName).exists()) {
				List<HotvodFilteringSiteVo> siteList = new ArrayList<HotvodFilteringSiteVo>();
				
				// Create Current Site List.
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
				
				// Remove selected Site With Index.
				int idx = Integer.parseInt(filtSiteVo.getOrderNo())-1;
				siteList.remove(idx);
				
				// Number Ordering Newly.
				for (int i=0;i<siteList.size();i++) {
					siteList.get(i).setOrderNo(String.valueOf(i+1));
				}
				
				// Delete Old File.
				File file = new File(fileName);
				file.delete();
				
				// Write New Text.
				BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
				for (int i=0;i<siteList.size();i++) {
					String str = siteList.get(i).getOrderNo() + "|" + siteList.get(i).getSiteUrl() + "|" + siteList.get(i).getRegDate();
					writer.write(str);
					writer.newLine();
				}
				writer.close();
				
				// set Result.
				result.setFlag(SmartUXProperties.getProperty("flag.success"));
				result.setMessage(SmartUXProperties.getProperty("message.success"));
			}
		}
		catch (IOException e) {
			logger.error("delFilteringSite:"+e.getClass().getName());
			logger.error("delFilteringSite:"+e.getMessage());
			
			ExceptionHandler handler = new ExceptionHandler(e);
			resCode = handler.getFlag();
			resMessage 	= handler.getMessage();
		}
		
		String resultMsg = "{\"flag\" : \"" + result.getFlag() + "\", \"message\" : \"" + result.getMessage() + "\"}";
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(JSONSerializer.toJSON(resultMsg).toString(), responseHeaders, HttpStatus.OK);
	}	
}