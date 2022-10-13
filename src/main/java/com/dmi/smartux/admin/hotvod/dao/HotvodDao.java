package com.dmi.smartux.admin.hotvod.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dmi.smartux.admin.hotvod.vo.HotvodContentVO;
import com.dmi.smartux.admin.hotvod.vo.HotvodExcelVo;
import com.dmi.smartux.admin.hotvod.vo.HotvodFileUploadVO;
import com.dmi.smartux.admin.hotvod.vo.HotvodSearchVO;
import com.dmi.smartux.common.dao.CommonDao;

/**
 * 화제동영상 - DAO
 * @author JKJ
 */
@Repository
public class HotvodDao extends CommonDao {
	
	/**
	 * 컨텐츠 목록
	 * @param hotvodSearchVO
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<HotvodContentVO> contentList(HotvodSearchVO hotvodSearchVO){
		hotvodSearchVO.setStart_rnum(hotvodSearchVO.getPageNum()*hotvodSearchVO.getPageSize()-hotvodSearchVO.getPageSize()+1);
		hotvodSearchVO.setEnd_rnum(hotvodSearchVO.getStart_rnum()+hotvodSearchVO.getPageSize()-1);
		return getSqlMapClientTemplate().queryForList("hotvod.contentList", hotvodSearchVO);
	}
	
	/**
	 * 컨텐츠 목록갯수
	 * @param hotvodSearchVO
	 * @return
	 */
	public int contentListCnt(HotvodSearchVO hotvodSearchVO){
		return (Integer)getSqlMapClientTemplate().queryForObject("hotvod.contentListCnt", hotvodSearchVO);
	}

	/**
	 * 카테고리 상세/수정
	 * @param hotvodSearchVO
	 * @return
	 */
	public HotvodContentVO categoryDetail(HotvodSearchVO hotvodSearchVO){
		return (HotvodContentVO)getSqlMapClientTemplate().queryForObject("hotvod.categoryDetail", hotvodSearchVO);
	}
	
	/**
	 * 컨텐츠 상세/수정
	 * @param hotvodSearchVO
	 * @return
	 */
	public HotvodContentVO contentDetail(HotvodSearchVO hotvodSearchVO){
		return (HotvodContentVO)getSqlMapClientTemplate().queryForObject("hotvod.contentDetail", hotvodSearchVO);
	}
	
	/**
	 * 컨텐츠 수정
	 * CommMimsDao로 이동
	 * @param hotvodContentVO
	 */
	/*public void contentUpdate(HotvodContentVO hotvodContentVO){
		getSqlMapClientTemplate().update("hotvod.contentUpdate", hotvodContentVO);
	}*/
		
	/**
	 * 컨텐츠 조회수 로그 수정
	 * CommMimsDao로 이동
	 * @param hotvodContentVO
	 */
	/*public int contentLogUpdate(HotvodContentVO hotvodContentVO){
		return getSqlMapClientTemplate().update("hotvod.contentLogUpdate", hotvodContentVO);
	}*/
	
	/**
	 * 컨텐츠 확장 정보 수정
	 * @param hotvodContentVO
	 */
	public void contentExtUpdate(HotvodContentVO hotvodContentVO){
		getSqlMapClientTemplate().update("hotvod.contentExtUpdate", hotvodContentVO);
	}
	
	/**
	 * 카테고리-컨텐츠 매핑 삭제
	 * @param hotvodContentVO
	 */
	public void contentCateDelete(HotvodContentVO hotvodContentVO){
		getSqlMapClientTemplate().update("hotvod.contentCateDelete", hotvodContentVO);
	}		
	
	/**
	 * 카테고리 수정
	 * @param hotvodContentVO
	 */
	public void categoryUpdate(HotvodContentVO hotvodContentVO){
		getSqlMapClientTemplate().update("hotvod.categoryUpdate", hotvodContentVO);
	}	
	
	/**
	 * 컨텐츠 등록
	 * CommMimsDao로 이동
	 * @param hotvodContentVO
	 */
	/*public void contentInsert(HotvodContentVO hotvodContentVO){
		getSqlMapClientTemplate().insert("hotvod.contentInsert", hotvodContentVO);
	}*/
	
	/**
	 * 컨텐츠 조회수 로그 등록
	 * CommMimsDao로 이동
	 * @param hotvodContentVO
	 */
	/*public void contentLogInsert(HotvodContentVO hotvodContentVO){
		getSqlMapClientTemplate().insert("hotvod.contentLogInsert", hotvodContentVO);
	}*/
	
	/**
	 * 컨텐츠 확장정보 등록
	 * @param hotvodContentVO
	 */
	public void contentExtInsert(HotvodContentVO hotvodContentVO){
		getSqlMapClientTemplate().insert("hotvod.contentExtInsert", hotvodContentVO);
	}
	
	/**
	 * 카테고리-컨텐츠 매핑 등록
	 * @param hotvodContentVO
	 */
	public void cateContentInsert(HotvodContentVO hotvodContentVO){
		getSqlMapClientTemplate().insert("hotvod.cateContentInsert", hotvodContentVO);
	}	
	
	/**
	 * 카테고리 등록
	 * @param hotvodContentVO
	 */
	public void categoryInsert(HotvodContentVO hotvodContentVO){
		getSqlMapClientTemplate().insert("hotvod.categoryInsert", hotvodContentVO);
	}
	
	/**
	 * 컨텐츠 확장정보 존재여부
	 * @param content_id
	 * @return
	 */	
	public int contentExtYn(String content_id){
		return (Integer)getSqlMapClientTemplate().queryForObject("hotvod.contentExtYn", content_id);
	}
	
	/**
	 * 컨텐츠 아이디 중복검사
	 * @param content_id
	 * @return
	 */
	public int contentIdChk(String content_id){
		return (Integer)getSqlMapClientTemplate().queryForObject("hotvod.contentIdChk", content_id);
	}
	
	/**
	 * 카테고리 삭제
	 * @param content_id, mod_id
	 */
	public void categoryDelete(String content_id, String mod_id){
		Map<String, String> param = new HashMap<String, String>();
		param.put("content_id", content_id);
		param.put("mod_id", mod_id);
		getSqlMapClientTemplate().update("hotvod.categoryDelete", param);
	}
	
	/**
	 * 컨텐츠 삭제
	 * @param content_id, mod_id
	 */
	public void contentDelete(String content_id, String mod_id, String parent_id){
		Map<String, String> param = new HashMap<String, String>();
		param.put("content_id", content_id);
		param.put("mod_id", mod_id);
		param.put("parent_id", parent_id);
		getSqlMapClientTemplate().update("hotvod.contentDelete", param);
	}
	
	/**
	 * 컨텐츠 삭제
	 * @param content_id, mod_id
	 */
	public void cateContentDelete(String content_id, String mod_id){
		Map<String, String> param = new HashMap<String, String>();
		param.put("content_id", content_id);
		param.put("mod_id", mod_id);
		getSqlMapClientTemplate().update("hotvod.cateContentDelete", param);
	}
	
	/**
	 * 컨텐츠 아이디 신규 채번
	 * CommMimsDao로 이동
	 * @return
	 */
	/*public String getContentId(){
		return (String)getSqlMapClientTemplate().queryForObject("hotvod.getContentId", null);
	}*/
	
	/**
	 * 카테고리 목록 (팝업)
	 * @param content_type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<HotvodContentVO> getCategoryList(String content_type){
		return getSqlMapClientTemplate().queryForList("hotvod.getCategoryList", content_type);
	}
	
	/**
	 * 카테고리 상세조회 (팝업)
	 * @param content_id
	 * @return
	 */
	public int getCategoryCount(String content_id){
		HotvodContentVO hotvocContentVO = new HotvodContentVO();
		hotvocContentVO.setContent_id(content_id);
		
		Integer result = (Integer)getSqlMapClientTemplate().queryForObject("hotvod.getCategoryCount", hotvocContentVO);
		if(result != null) {
			return result.intValue();
		}
		return 0;
	}
	
	/**
	 * 카테고리 순서바꾸기 목록 (팝업)
	 * @param content_id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<HotvodContentVO> getChangeList(String content_id){
		HotvodContentVO hotvocContentVO = new HotvodContentVO();
		hotvocContentVO.setContent_id(content_id);
		return getSqlMapClientTemplate().queryForList("hotvod.getChangeList", hotvocContentVO);
	}
	
	/**
	 * 컨텐츠 순서바꾸기 목록 (팝업)
	 * @param content_id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<HotvodContentVO> getChangeContentList(String content_id){
		HotvodContentVO hotvocContentVO = new HotvodContentVO();
		hotvocContentVO.setContent_id(content_id);
		return getSqlMapClientTemplate().queryForList("hotvod.getChangeContentList", hotvocContentVO);
	}	
	
	/**
	 * 카테고리 순서바꾸기 저장
	 * @param content_id
	 */
	public void changeSave(HotvodContentVO hotvodContentVO){
		getSqlMapClientTemplate().update("hotvod.changeSave", hotvodContentVO);
	}
	
	/**
	 * 컨텐츠 순서바꾸기 저장
	 * @param content_id
	 */
	public void changeContentsSave(HotvodContentVO hotvodContentVO){
		getSqlMapClientTemplate().update("hotvod.changeContentsSave", hotvodContentVO);
	}	
	
	/**
	 * 하위 목록 (팝업)
	 * @param hotvodSearchVO
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<HotvodContentVO> getSubList(HotvodSearchVO hotvodSearchVO){
		hotvodSearchVO.setStart_rnum(hotvodSearchVO.getPageNum()*hotvodSearchVO.getPageSize()-hotvodSearchVO.getPageSize()+1);
		hotvodSearchVO.setEnd_rnum(hotvodSearchVO.getStart_rnum()+hotvodSearchVO.getPageSize()-1);
		return getSqlMapClientTemplate().queryForList("hotvod.getSubList", hotvodSearchVO);
	}
	
	/**
	 * 하위 목록 갯수
	 * @param hotvodSearchVO
	 * @return
	 */
	public int getSubListCnt(HotvodSearchVO hotvodSearchVO){
		return (Integer)getSqlMapClientTemplate().queryForObject("hotvod.getSubListCnt", hotvodSearchVO);
	}
	
	/**
	 * 카테고리, 앨범 정보 조회
	 * @param param
	 * @return
	 */
	public HotvodContentVO getAlbumCateInfo(Map<String, String> param){
		return (HotvodContentVO)getSqlMapClientTemplate().queryForObject("hotvod.getAlbumCateInfo", param);
	}
	
	/**
	 * 구간점프 이미지 정보 조회
	 * @param albumId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, String> getImgInfo(String albumId){
		return (HashMap<String, String>)getSqlMapClientTemplate().queryForObject("hotvod.getImgInfo", albumId);
	}
	
	/**
	 * 컨텐츠일때 상위카테고리의 노출여부 조회 
	 * @param hotvodContentVO
	 * @return
	 */
	public HotvodContentVO getParentContentDelYn(HotvodContentVO hotvodContentVO){
		return (HotvodContentVO)getSqlMapClientTemplate().queryForObject("hotvod.getParentContentDelYn", hotvodContentVO);
	}
	
	/**
	 * 카테고리일때 상위카테고리의 노출여부 조회 
	 * @param hotvodContentVO
	 * @return
	 */
	public HotvodContentVO getParentCategoryDelYn(HotvodContentVO hotvodContentVO){
		return (HotvodContentVO)getSqlMapClientTemplate().queryForObject("hotvod.getParentCategoryDelYn", hotvodContentVO);
	}	
	
	/**
	 * 대박영상 통합검색 목록 조회 (컨텐츠)
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<HotvodFileUploadVO> getHotvodListAll(){
		return getSqlMapClientTemplate().queryForList("hotvod.getHotvodListAll", null);
	}
	
	/**
	 * 대박영상 통합검색 목록 조회 (하이라이트)
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<HotvodFileUploadVO> getHotvodHighListAll(){
		return getSqlMapClientTemplate().queryForList("hotvod.getHotvodHighListAll", null);
	}
	
	/**
	 * 대박영상 통합검색 목록 조회 
	 * CommMimsDao로 이동
	 * @param service_type (서비스타입)
	 * @return
	 */
	/*@SuppressWarnings("unchecked")
	public List<HotvodFileUploadVO> getHotvodServiceListAll(String service_type){
		return getSqlMapClientTemplate().queryForList("hotvod.getHotvodServiceListAll", service_type);
	}*/	
	
	/**
	 * 대박영상 통합검색 목록 조회
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<HotvodContentVO> getMasterContentValidList(String content_id){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("content_id", content_id);		
		return getSqlMapClientTemplate().queryForList("hotvod.getMasterContentValidList", param);
	}
	
	/**
	 * 대박영상 통합검색 목록 조회
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<HotvodContentVO> getFocusValidList(String content_id){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("content_id", content_id);		
		return getSqlMapClientTemplate().queryForList("hotvod.getFocusValidList", param);
	}
	
	/**
	 * 대박영상 엑셀다운로드
	 * @return List<HotvodExcelVo>
	 */	
	@SuppressWarnings("unchecked")
	public List<HotvodExcelVo> getExcelCategory(HotvodSearchVO hotvodSearchVO) throws SQLException {
		return getSqlMapClientTemplate().queryForList("hotvod.getExcelCategory", hotvodSearchVO);
	}

	/**
	 * 컨텐츠 복사존재여부 체크
	 * @param parent_id, content_id
	 * @return
	 */	
	public int contentCopyChk(String parent_id, String content_id) throws SQLException {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("parent_id", parent_id);
		param.put("content_id", content_id);
		return (Integer)getSqlMapClientTemplate().queryForObject("hotvod.contentCopyChk", param);
	}
	
	/**
	 * 컨텐츠 복사
	 * @param parent_id, content_id
	 * @return
	 */	
	public void contentCopy(String parent_id, String content_id, int order) throws SQLException {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("parent_id", parent_id);
		param.put("content_id", content_id);
		param.put("order", order);
		getSqlMapClientTemplate().insert("hotvod.contentCopy", param);
	}
	
	/**
	 * 컨텐츠 삭제여부 변경
	 * @param parent_id, content_id
	 * @return
	 */	
	public void contentDelYnUpdate(String parent_id, String content_id, String delYn, int order) throws SQLException {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("parent_id", parent_id);
		param.put("content_id", content_id);
		param.put("del_yn", delYn);
		param.put("order", order);
		getSqlMapClientTemplate().update("hotvod.contentDelYnUpdate", param);
	}
	
	/**
	 * 컨텐츠 삭제여부 조회
	 * @param parent_id, content_id
	 * @return
	 */	
	public String contentDelYn(String parent_id, String content_id) throws SQLException {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("parent_id", parent_id);
		param.put("content_id", content_id);
		return (String) getSqlMapClientTemplate().queryForObject("hotvod.contentDelYn", param);
	}		
	
	/**
	 * 복사된 컨텐츠를 제외한 순서 목록
	 * @param HotvodContentVO
	 * @return
	 */		
	@SuppressWarnings("unchecked")	
	public List<HotvodContentVO> getChangeOrderList(HotvodContentVO vo) {
		return getSqlMapClientTemplate().queryForList("hotvod.getChangeOrderList", vo);
	}	
	
	/**
	 * 카테고리 ID변경을 위한 등록
	 * @param hotvodContentVO
	 */
	public void cateContentSelectInsert(HotvodContentVO hotvodContentVO){
		getSqlMapClientTemplate().insert("hotvod.cateContentSelectInsert", hotvodContentVO);
	}
	
	/**
	 * 이미지서버 URL 조회
	 * @param gubun
	 * @return
	 */
	public String getImgServerUrl(String gubun) {
		return (String)getSqlMapClientTemplate().queryForObject("hotvod.getImgServerUrl",gubun);		
	}
}
