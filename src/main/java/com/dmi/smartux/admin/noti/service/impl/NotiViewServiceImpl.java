package com.dmi.smartux.admin.noti.service.impl;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.dmi.smartux.admin.noti.dao.NotiViewDao;
import com.dmi.smartux.admin.noti.service.NotiViewService;
import com.dmi.smartux.admin.noti.vo.CopyNotiPopupVo;
import com.dmi.smartux.admin.noti.vo.NotiListVO;
import com.dmi.smartux.admin.noti.vo.NotiPopVo;
import com.dmi.smartux.admin.noti.vo.NotiVO;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.notipop.service.NotiPopService;

/**
 * 
 * 관리자 공지사항 서비스 기능 구현 클래스
 * 
 */

@Repository
public class NotiViewServiceImpl implements NotiViewService{
	
	@Autowired
	NotiViewDao dao;
	
	//긴급공지/팝업공지 캐싱하기 위한 데이터를 저장하기 위해
	@Autowired
	NotiPopService notipopService;
	
	@Override
	@Transactional
	public List<NotiVO> getNotiList(NotiListVO notiListVO) throws Exception {
		
		List<NotiVO> result=null;
		result=dao.getNotiList(notiListVO);
		
		return result;
	}

	@Override
	@Transactional
	public int getNotiListCnt(NotiListVO notiListVO) throws Exception {
		
		int result=0;
		result=dao.getNotiListCnt(notiListVO);
		return result;
	}
	
	@Override
	@Transactional
    public void createNoti(NotiVO notiVO) throws Exception { 
		
		if(notiVO.getEv_type()==null || notiVO.getEv_type().isEmpty()){//없다면
			notiVO.setEv_detail("");
			notiVO.setEv_detail_cont("");
			notiVO.setEv_detail_prod("");
		}if("ev1".equals(notiVO.getEv_type())){//컨텐츠 노출형
			notiVO.setEv_detail(notiVO.getEv_detail_cont());//컨텐츠 리스트를 DB에 넣는다.
			notiVO.setEv_detail_prod("");
		}else if("ev7".equals(notiVO.getEv_type())){//공지/이벤트 게시판
			notiVO.setEv_detail(notiVO.getNoti_detail_name() + "," + notiVO.getNoti_detail());//공지/이벤트 게시글을 DB에 넣는다.
			notiVO.setEv_detail_prod("");
		}else if("ev2".equals(notiVO.getEv_type()) || "ev5".equals(notiVO.getEv_type()) || "ev6".equals(notiVO.getEv_type())){//월정액 가입,URL링크,특정카테고리
			notiVO.setEv_detail(notiVO.getEv_detail_prod());//상품코드를 DB에 넣는다.
			notiVO.setEv_detail_cont("");
		}else if("ev3".equals(notiVO.getEv_type())){//참여하기
			notiVO.setEv_detail("");
			notiVO.setEv_detail_cont("");
			notiVO.setEv_detail_prod("");
		}else if("ev4".equals(notiVO.getEv_type())){//초대하기
			notiVO.setEv_detail("");
			notiVO.setEv_detail_cont("");
			notiVO.setEv_detail_prod("");
		}
		
		//게시물 등록
		String reg_no=dao.createNoti(notiVO);		
		
		// 적용 단말기 리스트가 있는 경우 등록  
		if(notiVO.getTerminal_list()!=null){		
			String[] terminal_list=notiVO.getTerminal_list().split(",");		
			
			for(String terminal : terminal_list){
				
				dao.insertTerm(reg_no, terminal.trim());
			}
		}else{
			notiVO.setTerminal_list("ALL");
		}
		
		//LOG
		notiVO.setReg_no(reg_no);
		dao.insertNotiLog(notiVO);
	}
	
	@Override
	@Transactional
	public  NotiVO getNotiView(String reg_no) throws Exception {
		 NotiVO result=null;		 			 
		 result=dao.getNotiView(reg_no);
			
		return result;
	}

	@Override
	@Transactional
	public void deleteNoti(String reg_no,String cookieID,String ip) throws Exception {
		
		//delete
		String[] reg_no_list=reg_no.split(",");
		
		for (int i=0;i<reg_no_list.length;i++){
			
			String no=reg_no_list[i].trim();
			
			//단말 리스트 있을 경우 삭제
			if(dao.getNotiTerminal(no)!=null){
				dao.deleteTerm(no); 			
			}
			
			//LOG
			NotiVO notiVO=dao.getNotiView(reg_no_list[i]);
			notiVO.setAct_gbn("D");
			notiVO.setAct_id(cookieID);
			notiVO.setAct_ip(ip);
			dao.insertNotiLog(notiVO);
			
			/*
			 * 게시판에 등록된 이미지 삭제 로직
			 */
			//이미지 삭제를 위해 save_file_nm 외 데이터 조회
			NotiVO notiData = dao.getNotiView(no);
			String saveFileNm = notiData.getSave_file_nm();			
			//기존에 업로드한 이미지가 있다면
			if(saveFileNm != null && !saveFileNm.isEmpty()) {
				//확장자 포함 파일명
				String deleteFileNm = saveFileNm.substring(saveFileNm.lastIndexOf("/")+1);				
				File file = new File(SmartUXProperties.getProperty("noti.imgupload.dir")+ deleteFileNm);	    	
				//이미지 파일 삭제
				if(file.exists()) 
					file.delete();
			}			
			
			//게시글 삭제
			dao.deleteNoti(no);
		}
		
		

	}

	@Override
	@Transactional
	public void updateNoti(NotiVO notiVO) throws Exception {	

		if(notiVO.getEv_type()==null || notiVO.getEv_type().isEmpty()){//없다면
			notiVO.setEv_detail("");
			notiVO.setEv_detail_cont("");
			notiVO.setEv_detail_prod("");
		}if("ev1".equals(notiVO.getEv_type())){//컨텐츠 노출형
			notiVO.setEv_detail(notiVO.getEv_detail_cont());//컨텐츠 리스트를 DB에 넣는다.
			notiVO.setEv_detail_prod("");
		}else if("ev7".equals(notiVO.getEv_type())){//공지/이벤트 게시판
			notiVO.setEv_detail(notiVO.getNoti_detail_name() + "," + notiVO.getNoti_detail());//공지/이벤트 게시글을 DB에 넣는다.
			notiVO.setEv_detail_prod("");
		}else if("ev2".equals(notiVO.getEv_type()) || "ev5".equals(notiVO.getEv_type()) || "ev6".equals(notiVO.getEv_type())){//월정액 가입,URL링크,특정카테고리
			notiVO.setEv_detail(notiVO.getEv_detail_prod());//상품코드를 DB에 넣는다.
			notiVO.setEv_detail_cont("");
		}else if("ev3".equals(notiVO.getEv_type())){//참여하기
			notiVO.setEv_detail("");
			notiVO.setEv_detail_cont("");
			notiVO.setEv_detail_prod("");
		}else if("ev4".equals(notiVO.getEv_type())){//초대하기
			notiVO.setEv_detail("");
			notiVO.setEv_detail_cont("");
			notiVO.setEv_detail_prod("");
		}
		
		String reg_no=notiVO.getReg_no();
		
		if(dao.getNotiTerminal(reg_no)!=null){
			dao.deleteTerm(reg_no); //단말 리스트 삭제			
		} 
		
		if(notiVO.getTerminal_list()!=null){
			String[] terminal_list=notiVO.getTerminal_list().split(",");			
			for(String terminal : terminal_list){
				dao.insertTerm(reg_no, terminal.trim());
			}
		}else{
			notiVO.setTerminal_list("ALL");
		}
		
		dao.updateNoti(notiVO);
		
		//LOG		
		dao.insertNotiLog(notiVO);

	}
		
	@Override
	@Transactional
	public List<Map<String , String >> getBoardList(NotiListVO notiListVO) throws Exception{
		
		List<Map<String, String>> result=null;		
		result=dao. getBoardList(notiListVO);		
	
		return result;
	}
	@Override
	@Transactional
	public List<Map<String, String>> getTerm(Map<String, String> param) throws Exception {
		
		List<Map<String, String>> result=null;		
		result=dao.getTerm(param);
		
		return result;
	}

	@Override
	@Transactional
	public List<Map<String, String>> getNotiTerminal(String reg_no) throws Exception {
		
		List<Map<String, String>> result=null;		
		result=dao.getNotiTerminal(reg_no);
		
		return result;
	}

	@Override
	@Transactional
	public void popUpSetting(String reg_no,String bbs_id,String is_fixed,String cookieID,String ip) throws Exception {
		
		String result=dao.popUpCheck(bbs_id); //팝업 유무 결과
		
		 //설정된 팝업이 있을경우 해제로 변경
		if(result!=null){
			dao.popUpChange(result,"0");					
		}
		dao.popUpChange(reg_no,is_fixed);
		
		//LOG
		NotiVO notiVO=dao.getNotiView(reg_no);		
		notiVO.setAct_gbn("U");
		notiVO.setAct_id(cookieID);	
		notiVO.setAct_ip(ip);
		dao.insertNotiLog(notiVO);		
	}

	@Override
	public Integer getIsFixedCount(String bbs_id) throws Exception {
		Integer result=null;
		result=(Integer)dao.getIsFixedCount(bbs_id);
		return result;
	}

	@Override
	@Transactional
	public void updateIsFixed(NotiVO notiVO,String cookieID,String ip) throws Exception {
		dao.updateIsFixed(notiVO);
		
		//LOG
		NotiVO notiLog=dao.getNotiView(notiVO.getReg_no());		
		notiLog.setAct_gbn("U");	
		notiLog.setAct_id(cookieID);
		notiLog.setAct_ip(ip);
		dao.insertNotiLog(notiLog);
	}

	@Override
	@Transactional
	public void updateConfirmAdt(String reg_no,String is_adt, String bbs_id,String cookieID,String ip, String bbs_gbn) throws Exception {
		
		if("PU".equals(bbs_gbn)){
			
			String result=dao.adtCheck(bbs_id); //팝업 유무 결과		
			
			 //설정된 팝업이 있을경우 해제로 변경
			if(result!=null){
				dao.updateConfirmAdt(result,"0");					
			}
		}
		
		dao.updateConfirmAdt(reg_no,is_adt);
		
		
		//LOG
		NotiVO notiVO=dao.getNotiView(reg_no);		
		notiVO.setAct_gbn("U");
		notiVO.setAct_id(cookieID);		
		notiVO.setAct_ip(ip);
		
		dao.insertNotiLog(notiVO);
	}

	@Override
	@Transactional
	public void createTermManage(Map<String, String> param , String id , String ip) throws Exception {			
		dao.createTermManage(param, id, ip);		
	}

	@Override
	@Transactional
	public void deleteTermManage(String modelList, String del_yn ,String id, String ip, String scr_tp) throws Exception {
		
		String list[]=modelList.split(",");
		
		for(String param:list){
			
			Map<String,String> deleteParam=new HashMap<String, String>();
			deleteParam.put("act_id", id);
			deleteParam.put("act_ip", ip);
			deleteParam.put("act_gbn", "D");
			deleteParam.put("term_model",param.trim());
			deleteParam.put("del_yn", del_yn);
			deleteParam.put("scr_tp", scr_tp);
			dao.deleteTermManage(deleteParam);
		}
	}
	
	@Override
	public Integer getIsExistModel(String term_model, String scr_tp) throws Exception {
		Map<String,String> selParam=new HashMap<String, String>();
		selParam.put("term_model",term_model);
		selParam.put("scr_tp", scr_tp);
		
		Integer result=null;
		result=(Integer)dao.getIsExistModel(selParam);
		return result;
	}

	@Override
	public String getNotiPopupId(String scr_tp) throws Exception {
		return dao.getNotiPopupId(scr_tp);
	}

	@Override
	@Transactional
	public void copyNotiPopup(CopyNotiPopupVo copynotipopupVo) throws Exception {
		String[] cp_reg_no_arr = copynotipopupVo.getCp_reg_no().split(",");
		
		for(String crNo : cp_reg_no_arr){
			copynotipopupVo.setCp_reg_no(crNo);
			dao.copyNotiPopup(copynotipopupVo);
		}
	}

	@Override
	public List<NotiPopVo> getNotiPopList(NotiListVO notiListVO) throws Exception {
		
		List<NotiPopVo> result=null;
		result=dao.getNotiPopList(notiListVO);
		
		return result;
	}

	@Override
	public int getNotiPopListCnt(NotiListVO notiListVO) throws Exception {
		
		int result=0;
		result=dao.getNotiPopListCnt(notiListVO);
		return result;
	}

}
