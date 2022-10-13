package com.dmi.smartux.admin.starrating.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.dmi.smartux.common.dao.CommonDao;
import com.dmi.smartux.admin.starrating.vo.HistoryVO;
import com.dmi.smartux.admin.starrating.vo.StarRatingSearchVO;
import com.dmi.smartux.admin.starrating.vo.StarRatingVO;

@Repository
public class StarRatingDao extends CommonDao{
	
	/**
	 * 별점 목록
	 * @param vo
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<StarRatingVO> getStarRatingList(StarRatingSearchVO vo) throws DataAccessException {
		vo.setStart_rnum(vo.getPageNum()*vo.getPageSize()-vo.getPageSize()+1);
		vo.setEnd_rnum(vo.getStart_rnum()+vo.getPageSize()-1);
		return (List<StarRatingVO>)getSqlMapClientTemplate().queryForList("admin_starrating.getStarRatingList", vo);
	}
	
	/**
	 * 별점 목록 개수
	 * @param vo
	 * @return
	 * @throws DataAccessException
	 */
	public int getStarRatingListCnt(StarRatingSearchVO vo) throws DataAccessException {
		return (Integer)getSqlMapClientTemplate().queryForObject("admin_starrating.getStarRatingListCnt", vo);
	}
	
	/**
	 * 별점 아이디 생성
	 * @return
	 * @throws DataAccessException
	 */
	public String getStarRatingId() throws DataAccessException {
		return (String)getSqlMapClientTemplate().queryForObject("admin_starrating.getStarRatingId");
	}
	
	/**
	 * 별점 등록
	 * @param vo
	 * @throws DataAccessException
	 */
	public void insertProc(StarRatingVO vo) throws DataAccessException {
		getSqlMapClientTemplate().insert("admin_starrating.insertProc", vo);
	}
	
	/**
	 * 별점 수정
	 * @param vo
	 * @throws DataAccessException
	 */
	public void updateProc(StarRatingVO vo) throws DataAccessException {
		getSqlMapClientTemplate().update("admin_starrating.updateProc", vo);
	}
	
	/**
	 * 별점 삭제
	 * @param sr_id
	 * @throws DataAccessException
	 */
	public void deleteProc(String sr_id) throws DataAccessException {
		getSqlMapClientTemplate().delete("admin_starrating.deleteProc", sr_id);
	}
	
	/**
	 * 이미지 서버 URL 조회
	 * @return
	 * @throws DataAccessException
	 */
	public String getImgServer() throws DataAccessException {
		return (String)getSqlMapClientTemplate().queryForObject("admin_starrating.getImgServer");
	}
	
	/**
	 * 별점 제목 조회
	 * @param sr_id
	 * @return
	 * @throws DataAccessException
	 */
	public String getTitle(String sr_id) throws DataAccessException {
		return (String)getSqlMapClientTemplate().queryForObject("admin_starrating.getTitle", sr_id);
	}
	
	/**
	 * 별점 상세 목록 조회
	 * @param sr_pid
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<StarRatingVO> getSrList(String sr_pid) throws DataAccessException {
		return (List<StarRatingVO>)getSqlMapClientTemplate().queryForList("admin_starrating.getSrList", sr_pid);
	}
	
	/**
	 * 활성화상태 조회
	 * @param system_gb
	 * @return
	 * @throws DataAccessException
	 */
	public int getUseYnCnt(String system_gb) throws DataAccessException {
		return (Integer)getSqlMapClientTemplate().queryForObject("admin_starrating.getUseYnCnt", system_gb);
	}
	
	/**
	 * 활성화상태 수정
	 * @param vo
	 * @throws DataAccessException
	 */
	public void updateUseYn(StarRatingVO vo) throws DataAccessException {
		getSqlMapClientTemplate().update("admin_starrating.updateUseYn", vo);
	}
	
	/**
	 * 별점내역 조회
	 * @param vo
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<HistoryVO> getAlbumHistoryList(StarRatingSearchVO vo) throws DataAccessException {
		vo.setStart_rnum(vo.getPageNum()*vo.getPageSize()-vo.getPageSize()+1);
		vo.setEnd_rnum(vo.getStart_rnum()+vo.getPageSize()-1);
		return (List<HistoryVO>)getSqlMapClientTemplate().queryForList("admin_starrating.getAlbumHistoryList", vo);
	}
	
	/**
	 * 별점내역 개수 조회
	 * @param vo
	 * @return
	 * @throws DataAccessException
	 */
	public int getAlbumHistoryListCnt(StarRatingSearchVO vo) throws DataAccessException {
		return (Integer)getSqlMapClientTemplate().queryForObject("admin_starrating.getAlbumHistoryListCnt", vo);
	}
	
	/**
	 * 별점 주기 내역 조회
	 * @param vo
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<HistoryVO> getSrHistoryList(StarRatingSearchVO vo) throws DataAccessException {
		vo.setStart_rnum(vo.getPageNum()*vo.getPageSize()-vo.getPageSize()+1);
		vo.setEnd_rnum(vo.getStart_rnum()+vo.getPageSize()-1);
		return (List<HistoryVO>)getSqlMapClientTemplate().queryForList("admin_starrating.getSrHistoryList", vo);
	}
	
	/**
	 * 별점 주기 내역 개수 조회
	 * @param vo
	 * @return
	 * @throws DataAccessException
	 */
	public int getSrHistoryListCnt(StarRatingSearchVO vo) throws DataAccessException {
		return (Integer)getSqlMapClientTemplate().queryForObject("admin_starrating.getSrHistoryListCnt", vo);
	}
	
	/**
	 * 별점 주기 내역 별점평균 조회(검색조건)
	 * @param vo
	 * @return
	 * @throws DataAccessException
	 */
	public String getSrHistoryListAvg(StarRatingSearchVO vo) throws DataAccessException {
		return (String)getSqlMapClientTemplate().queryForObject("admin_starrating.getSrHistoryListAvg", vo);
	}
}
