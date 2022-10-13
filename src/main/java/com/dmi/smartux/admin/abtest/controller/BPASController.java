package com.dmi.smartux.admin.abtest.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dmi.commons.utility.DateUtils;
import com.dmi.commons.utility.ExcelWorkBookFactory;
import com.dmi.smartux.admin.abtest.service.BPASService;
import com.dmi.smartux.admin.abtest.vo.BPASListVo;
import com.dmi.smartux.admin.abtest.vo.BPASSearchVo;
import com.dmi.smartux.admin.abtest.vo.CategoryListVo;
import com.dmi.smartux.admin.abtest.vo.PanelListVo;
import com.dmi.smartux.admin.abtest.vo.TestListVo;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.CookieUtil;
import com.dmi.smartux.common.util.GlobalCom;

@Controller
public class BPASController {
	
	private final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	BPASService service;
	
	/**
	 * 말풍선 목록페이지
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/abtest/getBPASList")
	public String getBpasList(Model model,
			@RequestParam(value="pageNum", required=false, defaultValue="") String pageNum
			,@RequestParam(value="startDt", required=false, defaultValue="") String startDt
			,@RequestParam(value="endDt", required=false, defaultValue="") String endDt
			,@RequestParam(value="ab_yn", required=false, defaultValue="") String ab_yn
			,@RequestParam(value="mims_type_arr", required=false, defaultValue="") String mims_type_arr
			,@RequestParam(value="findType", required=false, defaultValue="") String findType
			,@RequestParam(value="findValue", required=false, defaultValue="") String findValue
			,HttpServletRequest request) throws Exception {

		model.addAttribute("loginUser", CookieUtil.getCookieUserID(request));
		
		BPASSearchVo vo = new BPASSearchVo();
		//페이지 
		vo.setPageSize(GlobalCom.isNumber(vo.getPageSize(),10));
		vo.setBlockSize(GlobalCom.isNumber(vo.getBlockSize(),10));
		vo.setPageNum(GlobalCom.isNumber(Integer.parseInt(GlobalCom.isNull(pageNum, "1")),1));
		vo.setStart_rnum(vo.getPageNum()*vo.getPageSize()-vo.getPageSize()+1);
		vo.setEnd_rnum(vo.getStart_rnum()+vo.getPageSize()-1);

		if("".equals(startDt) || "".equals(endDt)) {
			vo.setStartDt(DateUtils.convertToString(DateUtils.addDate(new Date(), Calendar.DATE, -6), DateUtils.DatePattern.yyyyMMdd_2));
        	vo.setEndDt(DateUtils.convertToString(new Date(), DateUtils.DatePattern.yyyyMMdd_2));
        	
        	mims_type_arr = "PAP,BAN,SUM,HOT";
        	vo.setMims_type_arr(mims_type_arr);
		}else {
			vo.setStartDt(startDt);
			vo.setEndDt(endDt);
			vo.setMims_type_arr(mims_type_arr);
		}
		
		if("".equals(ab_yn)) {
			vo.setAb_yn("ALL");
		}else {
			vo.setAb_yn(ab_yn);
		}
		
		String[] mims_type_array = mims_type_arr.split(",");
		List<String> mims_type_list = Arrays.asList(mims_type_array);
		vo.setMims_type_list(mims_type_list);
		
		if(mims_type_list.indexOf("PAP") > -1) {
			model.addAttribute("mims_type_pap", "Y");
		}
		if(mims_type_list.indexOf("BAN") > -1) {
			model.addAttribute("mims_type_ban", "Y");
		}
		if(mims_type_list.indexOf("HOT") > -1) {
			model.addAttribute("mims_type_hot", "Y");
		}
		if(mims_type_list.indexOf("SUM") > -1) {
			model.addAttribute("mims_type_sum", "Y");
		}
		
		vo.setFindType(findType);
		vo.setFindValue(findValue);
		
		System.out.println(vo.toString());
		
		List<BPASListVo> list = service.getBPASList(vo);
		vo.setPageCount(service.getBPASListCount(vo));
		
		model.addAttribute("list", list);
		model.addAttribute("vo", vo);

		return "/admin/abtest/getBPASList";
	}
	
	/**
     * MultiPayment EXCEL 다운로드
     */
    @ResponseBody
	@RequestMapping(value = "/admin/abtest/downloadExcelFile", method = RequestMethod.GET)
	public byte[] downloadExcelFile(
			@RequestParam(value="pageNum", required=false, defaultValue="") String pageNum
			,@RequestParam(value="startDt", required=false, defaultValue="") String startDt
			,@RequestParam(value="endDt", required=false, defaultValue="") String endDt
			,@RequestParam(value="ab_yn", required=false, defaultValue="") String ab_yn
			,@RequestParam(value="mims_type_arr", required=false, defaultValue="") String mims_type_arr
			,@RequestParam(value="findType", required=false, defaultValue="") String findType
			,@RequestParam(value="findValue", required=false, defaultValue="") String findValue
            ,HttpServletRequest request
            ,HttpServletResponse response
			,Model model) throws Exception {
    	
    	logger.debug("[BPASController][downloadExcelFile][START]");
    	
    	BPASSearchVo vo = new BPASSearchVo();
		//페이지 

		if("".equals(startDt) || "".equals(endDt)) {
			vo.setStartDt(DateUtils.convertToString(DateUtils.addDate(new Date(), Calendar.DATE, -6), DateUtils.DatePattern.yyyyMMdd_2));
        	vo.setEndDt(DateUtils.convertToString(new Date(), DateUtils.DatePattern.yyyyMMdd_2));
        	
        	mims_type_arr = "PAP,BAN,SUM,HOT";
		}else {
			vo.setStartDt(startDt);
			vo.setEndDt(endDt);
		}
		
		if("".equals(ab_yn)) {
			vo.setAb_yn("ALL");
		}else {
			vo.setAb_yn(ab_yn);
		}
		
		String[] mims_type_array = mims_type_arr.split(",");
		List<String> mims_type_list = Arrays.asList(mims_type_array);
		vo.setMims_type_list(mims_type_list);
		
		if(mims_type_list.indexOf("PAP") > -1) {
			model.addAttribute("mims_type_pap", "Y");
		}
		if(mims_type_list.indexOf("BAN") > -1) {
			model.addAttribute("mims_type_ban", "Y");
		}
		if(mims_type_list.indexOf("HOT") > -1) {
			model.addAttribute("mims_type_hot", "Y");
		}
		if(mims_type_list.indexOf("SUM") > -1) {
			model.addAttribute("mims_type_sum", "Y");
		}
		
		vo.setFindType(findType);
		vo.setFindValue(findValue);
		

        List<Object> header = new ArrayList<Object>();
        List<List<Object>> data = new ArrayList<List<Object>>();

        String[] headerAry = SmartUXProperties.getProperty("bpas.excel.header").split("\\|");

        ExcelWorkBookFactory.ExcelSheetFactory sheet = ExcelWorkBookFactory.create(10000).sheet("BPAS 편성내역"+ vo.getStartDt() + "~" + vo.getEndDt());
        sheet.headers(1, headerAry);


        int pageCount = 1;
        while(true) {
            vo.setPageNum(pageCount++);
            vo.setPageSize(1000);
            
            vo.setStart_rnum(vo.getPageNum() * vo.getPageSize() - vo.getPageSize() + 1);
        	vo.setEnd_rnum(vo.getStart_rnum() + vo.getPageSize() - 1);
        	
            List<BPASListVo> list = service.getBPASList(vo);
            
            for(int i = 0 ; i < list.size(); i++) {
            	BPASSearchVo param = new BPASSearchVo();
            	param.setFindValue(list.get(i).getImcs_id());
            	List<TestListVo> testList = new ArrayList<TestListVo>();
	            if("ALB".equals(list.get(i).getImcs_type())) {
	            	testList = service.getTestIdInfo(param);
	    		}else if("CAT".equals(list.get(i).getImcs_type())) {
	    			testList = service.getTestIdInfo2(param);
	    		}
	            
	            String test_name = "";
	            for(int z = 0 ; z < testList.size(); z++) {
	            	if(!"".equals(test_name)) {
	            		test_name += ","+testList.get(z).getTest_name()+"("+testList.get(z).getTest_id()+")";
	            	}else {
	            		test_name += testList.get(z).getTest_name()+"("+testList.get(z).getTest_id()+")";
	            	}
	            }
	            list.get(i).setTest_name_arr(test_name);
            }
            
            if(list == null || list.size() == 0) {
                break;
            } else {
                sheet.rowCellValues(list, new ExcelWorkBookFactory.CellValueRef<BPASListVo>() {
                    @Override
                    public void setRowData(Row row, BPASListVo vo) {
                        int col = 1;
                        if("PAP".equals(vo.getMims_type())){
                        	row.createCell(col++).setCellValue("지면");
                        }else if("BAN".equals(vo.getMims_type())){
                        	row.createCell(col++).setCellValue("배너");
                        }else if("SUM".equals(vo.getMims_type())){
                        	row.createCell(col++).setCellValue("자체편성");
                        }else if("HOT".equals(vo.getMims_type())){
                        	row.createCell(col++).setCellValue("화제동영상");
                        }else {
                        	row.createCell(col++).setCellValue("");
                        }
                        
                        row.createCell(col++).setCellValue(vo.getTitle());
                        
                        if("ALB".equals(vo.getImcs_type())) {
                        	row.createCell(col++).setCellValue(vo.getAlbum_name());
                        	row.createCell(col++).setCellValue(vo.getImcs_id());
                        }else {
                        	row.createCell(col++).setCellValue("");
                        	row.createCell(col++).setCellValue("");
                        }
                        
                        row.createCell(col++).setCellValue("");
                        row.createCell(col++).setCellValue("");
                        row.createCell(col++).setCellValue("");
                        
                        if("CAT".equals(vo.getImcs_type())) {
                        	row.createCell(col++).setCellValue(vo.getCategory_name());
                        	row.createCell(col++).setCellValue(vo.getImcs_id());
                        }else {
                        	row.createCell(col++).setCellValue("");
                        	row.createCell(col++).setCellValue("");
                        }
                        
                        row.createCell(col++).setCellValue(vo.getAb_yn());
                        row.createCell(col++).setCellValue(vo.getReg_date());
                        row.createCell(col++).setCellValue(vo.getTest_name_arr());
                        
                    }
                });
            }
        }

		byte[] bytes = sheet.end().make();

		response.setHeader("Content-Disposition", "attachment; filename=BPAS_IPTV_" + GlobalCom.getTodayFormat() + ".xlsx");
		response.setContentLength(bytes.length);
		response.setContentType("application/vnd.ms-excel");

		logger.debug("[BPASController][downloadExcelFile][END]");
		
		return bytes;
	}
	
    /**
	 * 지면&패널 조회
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/abtest/getAlbumCategoryInfo", method=RequestMethod.GET)
	public String getAlbumCategoryInfo(Model model, String imcs_type, String imcs_id) throws Exception {
		
		List<String> category_str_list = new ArrayList<String>();
		
		if("CAT".equals(imcs_type)) {
			List<CategoryListVo> category_list = service.getCategoryList(imcs_id);
			String category_str = "";
			
			for(int z = category_list.size()-2; z > -1; z--) {
				if("".equals(category_str)) {
					category_str = category_list.get(z).getCategory_name();
				}else {
					category_str += " ▶ "+category_list.get(z).getCategory_name();
				}
				if(z == 0) {
					category_str += "("+category_list.get(z).getCategory_id()+")";
				}
			}
			category_str_list.add(category_str);
			
			
			model.addAttribute("list", category_str_list);
			
		}else if("ALB".equals(imcs_type)) {
			List<CategoryListVo> categoryId_list = service.getCategoryIdList(imcs_id);
			for(int y = 0 ; y < categoryId_list.size(); y++) {
				List<CategoryListVo> category_list = service.getCategoryList(categoryId_list.get(y).getCategory_id());
				String category_str = "";
				for(int z = category_list.size()-2; z > -1; z--) {
					if("".equals(category_str)) {
						category_str = category_list.get(z).getCategory_name();
						if(z == 0) {
							category_str += "("+category_list.get(z).getCategory_id()+")";
						}
					}else {
						category_str += " ▶ "+category_list.get(z).getCategory_name();
						if(z == 0) {
							category_str += "("+category_list.get(z).getCategory_id()+")";
						}
					}
				}
				category_str_list.add(category_str);
			}
		}
		
		
		model.addAttribute("list", category_str_list);
		model.addAttribute("imcs_type", imcs_type);
		
		return "/admin/abtest/getAlbumCategoryInfo";
	}
	
	/**
	 * 지면&패널 조회
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/abtest/getPanelTitleInfo", method=RequestMethod.GET)
	public String getPanelTitleInfo(Model model, String mims_type, String mims_id, String imcs_type, String imcs_id) throws Exception {
		
		
		List<PanelListVo> panel_list = new ArrayList<PanelListVo>();
		if("SUM".equals(mims_type)) {
			String category_id = mims_id;
			panel_list = service.getPanelList(category_id);
		}else if("PAP".equals(mims_type)) {
			String[] id = mims_id.split("\\|");
			PanelListVo param = new PanelListVo();
			param.setPannel_id(id[0]);
			param.setTitle_id(id[1]);
			panel_list = service.getPanelList2(param);
		}else if("BAN".equals(mims_type)) {
			String ads_no = mims_id;
			panel_list = service.getPanelList3(ads_no);
		}else if("HOT".equals(mims_type)) {
			String content_id = mims_id;
			panel_list = service.getPanelList4(content_id);
		}
		
		model.addAttribute("list", panel_list);
		
		BPASSearchVo vo = new BPASSearchVo();
		vo.setFindType(imcs_type);
		vo.setFindValue(imcs_id);
		
		List<TestListVo> testList = new ArrayList<TestListVo>();
		
		if("ALB".equals(imcs_type)) {
			testList = service.getTestIdInfo(vo);
		}else if("CAT".equals(imcs_type)) {
			testList = service.getTestIdInfo2(vo);
		}
		
		model.addAttribute("list", panel_list);
		model.addAttribute("testList", testList);
		model.addAttribute("imcs_id", imcs_id);
		
		return "/admin/abtest/getPanelTitleInfo";
	}

}
