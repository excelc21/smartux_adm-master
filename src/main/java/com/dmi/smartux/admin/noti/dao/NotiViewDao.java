package com.dmi.smartux.admin.noti.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.dmi.smartux.admin.noti.vo.CopyNotiPopupVo;
import com.dmi.smartux.admin.noti.vo.NotiListVO;
import com.dmi.smartux.admin.noti.vo.NotiPopVo;
import com.dmi.smartux.admin.noti.vo.NotiVO;
import com.dmi.smartux.common.dao.CommonDao;

/**
 * 
 * 관리자 공지 사항 DAO클래스
 *
 */

@Repository
public class NotiViewDao extends CommonDao{
	
	private final Log logger =LogFactory.getLog(getClass());	
	
	
	/**
	 * 
	 * 관리자 게시판 리스트
	 * @param notiListVO 게시판 정보 객체
	 * @return 게시판 리스트
	 * @throws DataAccessException
	 */
	
	@SuppressWarnings("unchecked")
	public List<NotiVO> getNotiList(NotiListVO notiListVO) throws DataAccessException{
		
		notiListVO.setStart_rnum(notiListVO.getPageNum()*notiListVO.getPageSize()-notiListVO.getPageSize()+1);
		notiListVO.setEnd_rnum(notiListVO.getStart_rnum()+notiListVO.getPageSize()-1);
		
		List<NotiVO> result= null;
		
		result=getSqlMapClientTemplate().queryForList("admin_noti.getNotiList",notiListVO);
		
		logger.debug("result.size() = " + result.size());
		
		return result;
	}
	
	/**
	 * 게시글별 단말정보 저장 DAO
	 * @param reg_no	
	 * @param term_model
	 * @throws DataAccessException
	 */	
	public void insertTerm(String reg_no,String term_model) throws DataAccessException{
			
		Map<String, String> param=new HashMap<String,String>();
		param.put("reg_no",reg_no);
		param.put("term_model",term_model);
		
		getSqlMapClientTemplate().insert("admin_noti.insertTerm",param);
		
	}
	
	/**
	 * 게시글별 단말 정보 삭제 DAO
	 * @param reg_no
	 * @throws DataAccessException
	 */
	public void deleteTerm(String reg_no) throws DataAccessException{
		
		getSqlMapClientTemplate().delete("admin_noti.deleteTerm",reg_no);
		
	}
	
	
	/**
	 * 관리자 게시판 리스트 갯수 조회 DAO
	 * @param notiListVO
	 * @return
	 * @throws DataAccessException
	 */
	

	public int getNotiListCnt(NotiListVO notiListVO) throws DataAccessException{
		
		int result=0;
		result=(Integer)getSqlMapClientTemplate().queryForObject("admin_noti.getNotiListCnt",notiListVO);
		
		return result;
	}
	
	/**
	 * 관리자 게시판 등록 DAO
	 * @param notiVO
	 * @return
	 * @throws DataAccessException
	 * @throws Exception
	 */
	
	public String createNoti(NotiVO notiVO) throws DataAccessException, Exception{	
		  
		String reg_no=(String)getSqlMapClientTemplate().insert("admin_noti.createNoti", notiVO);
		
		return reg_no;
	}
	
	/**
	 *   게시판 수정 DAO
	 *   @param NotiListVO
	 *   @return void 
	 *   @throws DataAccessException 
	 */	
	public void updateNoti(NotiVO notiVO) throws DataAccessException{
		getSqlMapClientTemplate().update("admin_noti.updateNoti", notiVO);		
	}
	
	/**
	 * 관리자 게시판 등록 화면 DAO
	 * @param reg_no
	 * @return
	 * @throws DataAccessException
	 */
	public  NotiVO getNotiView(String reg_no) throws DataAccessException{		
		
		NotiVO result=null;			
		result=(NotiVO)getSqlMapClientTemplate().queryForObject("admin_noti.getNotiView",reg_no);
		
		return result;		
	}
	
	/**
	 * 해당 팝업  적용 단말기 등록 DAO 
	 * @param regNo
	 * @param termNo
	 * @throws DataAccessException
	 */
	public void setTerm(int regNo, String termNo) throws DataAccessException{			
		
		Map<String ,String> map=new HashMap<String ,String>();			
		map.put("regNo", String.valueOf(regNo));			
		map.put("term", termNo);			
		
		getSqlMapClientTemplate().insert("admin_noti.setTerm",map);
	}
	
	/**
	 * 단말 정보 리스트 DAO
	 * @param param
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>>  getTerm(Map<String, String> param) throws DataAccessException{
		
		 List<Map<String, String>>result=null;
		 result=getSqlMapClientTemplate().queryForList("admin_noti.getTerm",param);
				 
		return result;		
	}

	/**
	 * 게시판 종류 리스트 DAO
	 * @param notiListVO
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>>  getBoardList(NotiListVO notiListVO) throws DataAccessException{
		
		List<Map<String, String>> result=null;
		result=getSqlMapClientTemplate().queryForList("admin_noti.getBoardList",notiListVO);
		
		return result;		
	}

	/**
	 * 게시글 단말 리스트 DAO
	 * @param reg_no
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public  List<Map<String, String>> getNotiTerminal(String reg_no){
		
		List<Map<String ,String>> result=null;
		result=getSqlMapClientTemplate().queryForList("admin_noti.getNotiTerminal", reg_no);
		
		return result;
	}
	
	
	/**
	 * 게시글 삭제 DAO
	 * @param reg_no
	 * @throws DataAccessException
	 */
	public void deleteNoti(String reg_no) throws DataAccessException{		
		getSqlMapClientTemplate().delete("admin_noti.deleteNoti", reg_no);		
	}
	
	/**
	 * 팝업 체크 DAO
	 * @param bbs_id
	 * @return
	 * @throws DataAccessException
	 */
	public String popUpCheck(String bbs_id) throws DataAccessException{
		
		String result=null;
		result=(String)getSqlMapClientTemplate().queryForObject("admin_noti.popUpCheck",bbs_id);
		
		return result;
	}
	
	/**
	 * 팝업 설정 변경 DAO
	 * @param reg_no
	 * @param is_fixed
	 */
	public void popUpChange(String reg_no,String is_fixed){
		
		Map<String, String> param=new HashMap<String, String>();
		param.put("reg_no",reg_no);
		param.put("is_fixed",is_fixed);
		
		getSqlMapClientTemplate().update("admin_noti.popUpChange",param);
	}

	
	/**
	 *  상단 게시물 갯수 DAO
	 * @param bbs_id
	 * @return
	 * @throws DataAccessException
	 */
	public  Integer getIsFixedCount(String bbs_id) throws DataAccessException{
		
		Integer result=null;
		result=(Integer)getSqlMapClientTemplate().queryForObject("admin_noti.getIsFixedCount",bbs_id);
		return result;
	}

	/**
	 * 상단 여부 설정/해지 DAO
	 * @param notiVO
	 * @throws DataAccessException
	 */
	public  void updateIsFixed(NotiVO notiVO) throws DataAccessException{
		
		getSqlMapClientTemplate().update("admin_noti.updateIsFixed",notiVO);
		
	}
	
	/**
	 * 검수 체크 DAO
	 * @param bbs_id
	 * @return
	 * @throws DataAccessException
	 */
	public String adtCheck(String bbs_id) throws DataAccessException{
		
		String result=null;
		result=(String)getSqlMapClientTemplate().queryForObject("admin_noti.adtCheck",bbs_id);
		
		return result;
	}
	/**
	 * 검수 확인 DAO
	 * @param notiVO
	 * @throws DataAccessException
	 */
	public  void updateConfirmAdt(String reg_no,String is_adt) throws DataAccessException{		
		Map<String, String> param=new HashMap<String, String>();
		param.put("reg_no",reg_no);
		param.put("is_adt",is_adt);
		getSqlMapClientTemplate().update("admin_noti.updateConfirmAdt",param);		
	}
	
	/**
	 * 단말기 등록 DAO
	 * @param param
	 * @throws DataAccessException
	 */
	public void createTermManage(Map<String, String> param , String id , String ip) throws DataAccessException {
		logger.debug("insertLog Start");
		param.put("act_id", id);
		param.put("act_ip", ip);
		param.put("act_gbn", "I");
		
		getSqlMapClientTemplate().insert("admin_noti.createTermManage", param);		
		getSqlMapClientTemplate().insert("admin_noti.writeLog",param);
		
	}
	
	/**
	 * 단말기 삭제 DAO
	 * @param param
	 * @throws DataAccessException
	 */
	public void deleteTermManage(Map<String, String> param) throws DataAccessException{
		
		getSqlMapClientTemplate().update("admin_noti.deleteTermManage", param);		
		getSqlMapClientTemplate().insert("admin_noti.writeLog2", param);
	}
	
	/**
	 * 로그
	 * 
	 */
	public void insertNotiLog(NotiVO param) throws DataAccessException{	
		getSqlMapClientTemplate().update("admin_noti.insertNotiLog", param);		
	}
	
	/**
	 *  단말모델 등록여부 확인
	 * @param term_model
	 * @return
	 * @throws DataAccessException
	 */
	public  Integer getIsExistModel(Map<String, String> param) throws DataAccessException{
		
		Integer result=null;
		result=(Integer)getSqlMapClientTemplate().queryForObject("admin_noti.getIsExistModel",param);
		return result;
	}

	
	/**
	 * 팝업공지 가져오기(팝업공지는 앱별 무조건 하나임)
	 * @return
	 * @throws DataAccessException
	 */
	public  String getNotiPopupId(String scr_tp) throws DataAccessException{
		
		return (String)getSqlMapClientTemplate().queryForObject("admin_noti.getNotiPopupId", scr_tp);
	}
	
	/**
	 * 게시판 팝업공지로 복사
	 * @param copynotipopupVo
	 * @throws DataAccessException
	 */
	public void copyNotiPopup(CopyNotiPopupVo copynotipopupVo) throws DataAccessException{
		String reg_no = (String) getSqlMapClientTemplate().insert("admin_noti.copyNotiPopup", copynotipopupVo);	
		System.out.println(copynotipopupVo.getReg_no());
		getSqlMapClientTemplate().insert("admin_noti.copyNotiTerm", copynotipopupVo);
	}	
	
	
	/**
	 * 
	 * Popup 게시판 리스트
	 * @param notiListVO 게시판 정보 객체
	 * @return 게시판 리스트
	 * @throws DataAccessException
	 */
	
	@SuppressWarnings("unchecked")
	public List<NotiPopVo> getNotiPopList(NotiListVO notiListVO) throws DataAccessException{
		
		notiListVO.setStart_rnum(notiListVO.getPageNum()*notiListVO.getPageSize()-notiListVO.getPageSize()+1);
		notiListVO.setEnd_rnum(notiListVO.getStart_rnum()+notiListVO.getPageSize()-1);
		
		List<NotiPopVo> result= null;
		
		result=getSqlMapClientTemplate().queryForList("admin_noti.getNotiPopList",notiListVO);
		
		return result;
	}
	
	
	/**
	 * 팝업 리스트 갯수 조회 DAO
	 * @param notiListVO
	 * @return
	 * @throws DataAccessException
	 */
	public int getNotiPopListCnt(NotiListVO notiListVO) throws DataAccessException{
		
		int result=0;
		result=(Integer)getSqlMapClientTemplate().queryForObject("admin_noti.getNotiPopListCnt",notiListVO);
		
		return result;
	}
	
	
}
