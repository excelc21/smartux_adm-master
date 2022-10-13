package com.dmi.smartux.admin.common.dao;

import java.util.HashMap;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.dmi.smartux.admin.news.vo.NewsVO;
import com.dmi.smartux.admin.secondtv_push.vo.LatestContentVO;
import com.dmi.smartux.common.dao.CommonHmimsDao;
import com.dmi.smartux.push.vo.NextUiPushContentVO;

/**
 * @author medialog
 * 
 * DB 분리로 인한 DAO분리건
 */
@Repository
public class CommHmimsDao extends CommonHmimsDao{

	/**
	 * 새소식 등록
	 * /smartux_adm/src/main/java/com/dmi/smartux/admin/news/dao/NewsDao.java
	 * @param newsVO 새소식 객체
	 * @return 삽입된 새소식 번호
	 * @throws DataAccessException
	 */
	public int insert(NewsVO newsVO) throws DataAccessException {
		return (Integer) getSqlMapClientTemplate().insert("hmims.insert", newsVO);
	}
	
	/**
	 * 새소식 로그 등록
	 * /smartux_adm/src/main/java/com/dmi/smartux/admin/news/dao/NewsDao.java
	 * @param newsVO 새소식 객체
	 * @throws DataAccessException
	 */
	public void insertLog(NewsVO newsVO) throws DataAccessException {
		getSqlMapClientTemplate().insert("hmims.insertLog", newsVO);
	}
	
	/**
	 * 새소식 삭제
	 * /smartux_adm/src/main/java/com/dmi/smartux/admin/news/dao/NewsDao.java
	 * @param regNumber 새소식 시퀀스
	 * @return 삭제된 행 개수
	 * @throws DataAccessException
	 */
	public int delete(int regNumber) throws DataAccessException {
		return getSqlMapClientTemplate().delete("hmims.delete", regNumber);
	}
	
	/**
	 * 새소식 수정
	 * /smartux_adm/src/main/java/com/dmi/smartux/admin/news/dao/NewsDao.java
	 * @param newsVO 새소식 객체
	 * @return 성공 1 / 실패 0
	 * @throws DataAccessException
	 */
	public int update(NewsVO newsVO) throws DataAccessException {
		return getSqlMapClientTemplate().update("hmims.update", newsVO);
	}
	
	/**
	 * 새소식 전송 작업이 존재할 경우 작업중(WAS Index 번호)으로 표시한다.
	 * /smartux_adm/src/main/java/com/dmi/smartux/admin/news/dao/NewsDao.java
	 * @param index WAS Index
	 * @return 업데이트 여부(1이상일 경우 업데이트 항목 존재)
	 * @throws DataAccessException
	 */
	public int markNews(String index) throws DataAccessException {
		return getSqlMapClientTemplate().update("hmims.markNews", index);
	}
	
	/**
	 * WAS가 작업할 새소식 작업을 가져온다.
	 * /smartux_adm/src/main/java/com/dmi/smartux/admin/news/dao/NewsDao.java
	 * @param index WAS Index
	 * @return 작업할 객체
	 * @throws DataAccessException
	 */
	public NewsVO getMarkingNews(String index) throws DataAccessException {
		return (NewsVO) getSqlMapClientTemplate().queryForObject("hmims.getMarkingNews", index);
	}
	
	/**
	 * 새소식 전송 상태값 변경
	 * /smartux_adm/src/main/java/com/dmi/smartux/admin/news/dao/NewsDao.java
	 * @param resultMap 결과맵(result, index, regNumber)
	 * @return 성공 1 / 실패 0
	 * @throws DataAccessException
	 */
	public int updateNewsStatus(HashMap<String, String> resultMap) throws DataAccessException {
		return getSqlMapClientTemplate().update("hmims.updateNewsStatus", resultMap);
	}
	
	/**
	 * Push후 결과 저장
	 * /smartux_adm/src/main/java/com/dmi/smartux/admin/secondtv_push/dao/LatestScheduleDao.java
	 * @param completeVO 완료 VO
	 * @return 성공 여부
	 * @throws org.springframework.dao.DataAccessException
	 */
	public int updatePushComplete(LatestContentVO completeVO) throws DataAccessException {
		return getSqlMapClientTemplate().update("hmims.updatePushComplete", completeVO);

	}
	
	/**
	 * Push후 결과 저장
	 * /smartux_adm/src/main/java/com/dmi/smartux/push/dao/NextUiPushDao.java
	 * 동일한 이름이 존재하여 updatePushComplete -> updatePushComplete_nextui 로 변경
	 * @param pushcompleteVo
	 * @throws DataAccessException
	 */
	public void updatePushComplete(NextUiPushContentVO pushcompleteVo) throws DataAccessException{
		getSqlMapClientTemplate().update("hmims.updatePushComplete_nextui", pushcompleteVo);
		
	}
}
