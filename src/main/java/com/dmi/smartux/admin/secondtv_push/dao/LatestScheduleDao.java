package com.dmi.smartux.admin.secondtv_push.dao;

import com.dmi.smartux.admin.secondtv_push.vo.LatestContentVO;
import com.dmi.smartux.common.dao.CommonDao;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 최신회 관리자 Dao
 *
 * @author dongho
 */
@Repository
public class LatestScheduleDao extends CommonDao {
	/**
	 * 최신회가 있는지 확인할 카테고리 리스트 조회
	 * CommMimsDao로 이동
	 *
	 * @return 카테고리 리스트
	 * @throws org.springframework.dao.DataAccessException
	 */
	/*public List getCategoryList() throws DataAccessException {
		return getSqlMapClientTemplate().queryForList("admin_latest.getCategoryList");
	}*/

	/**
	 * 선택한 기간의 해당 카테고리의 최신회 컨텐츠 조회
	 *
	 * @param latestContentVO 최신회 컨텐츠 VO
	 * @return 최신회 컨텐츠
	 * @throws org.springframework.dao.DataAccessException
	 */
	public LatestContentVO getContent(LatestContentVO latestContentVO) throws DataAccessException {
		return (LatestContentVO) getSqlMapClientTemplate().queryForObject("admin_latest.getContent", latestContentVO);
	}

	/**
	 * 푸쉬할 사용자의 카테고리에 맞는 전체 카운트를 가져온다.
	 * CommMimsDao로 이동
	 *
	 * @param catID 카테고리 아이디
	 * @return 카테고리에 맞는 전체 카운트
	 * @throws org.springframework.dao.DataAccessException
	 */
	/*public int getUserListCount(String catID) throws DataAccessException {
		return (Integer)getSqlMapClientTemplate().queryForObject("admin_latest.getUserListCount", catID);
	}*/

	/**
	 * 조회한 최신회의 푸쉬 해줄 사용자 조회
	 * CommMimsDao로 이동
	 *
	 * @param latestContentVO 최신회 컨텐츠 VO
	 * @return 푸시 사용자 리스트
	 * @throws org.springframework.dao.DataAccessException
	 */
	/*public List getUserList(LatestContentVO latestContentVO) throws DataAccessException {
		return getSqlMapClientTemplate().queryForList("admin_latest.getUserList", latestContentVO);
	}*/

	/**
	 * Push후 결과 저장
	 *
	 * @param completeVO 완료 VO
	 * @return 성공 여부
	 * @throws org.springframework.dao.DataAccessException
	 */
	/*public int updatePushComplete(LatestContentVO completeVO) throws DataAccessException {
		return getSqlMapClientTemplate().update("admin_latest.updatePushComplete", completeVO);

	}*/
}
