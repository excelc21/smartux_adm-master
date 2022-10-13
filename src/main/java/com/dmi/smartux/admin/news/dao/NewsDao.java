package com.dmi.smartux.admin.news.dao;

import com.dmi.smartux.admin.news.vo.NewsVO;
import com.dmi.smartux.admin.news.vo.TargetVO;
import com.dmi.smartux.common.dao.CommonDao;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * 새소식 Dao
 *
 * @author dongho
 */
@Repository
public class NewsDao extends CommonDao {
	/**
	 * 새소식 리스트 조회
	 *
	 * @param newsVO 새소식 객체
	 * @return 새소식 리스트
	 * @throws DataAccessException
	 */
	public List getList(NewsVO newsVO) throws DataAccessException {
		newsVO.setStartNumber(newsVO.getPageNumber() * newsVO.getPageSize() - newsVO.getPageSize() + 1);
		newsVO.setEndNumber(newsVO.getStartNumber() + newsVO.getPageSize() - 1);

		return getSqlMapClientTemplate().queryForList("admin_news.getList", newsVO);
	}

	/**
	 * 새소식 조회
	 *
	 * @param regNumber 새소식 시퀀스
	 * @return 새소식
	 * @throws DataAccessException
	 */
	public NewsVO getData(String regNumber) throws DataAccessException {
		return (NewsVO) getSqlMapClientTemplate().queryForObject("admin_news.getData", regNumber);
	}

	/**
	 * 새소식 전체 개수
	 *
	 * @param newsVO 새소식 객체
	 * @return 전체 개수
	 * @throws DataAccessException
	 */
	public int getCount(NewsVO newsVO) throws DataAccessException {
		return (Integer) getSqlMapClientTemplate().queryForObject("admin_news.getCount", newsVO);
	}

	/**
	 * 새소식 등록
	 *
	 * @param newsVO 새소식 객체
	 * @return 삽입된 새소식 번호
	 * @throws DataAccessException
	 */
	/*public int insert(NewsVO newsVO) throws DataAccessException {
		return (Integer) getSqlMapClientTemplate().insert("admin_news.insert", newsVO);
	}*/

	/**
	 * 새소식 수정
	 *
	 * @param newsVO 새소식 객체
	 * @return 성공 1 / 실패 0
	 * @throws DataAccessException
	 */
	/*public int update(NewsVO newsVO) throws DataAccessException {
		return getSqlMapClientTemplate().update("admin_news.update", newsVO);
	}*/

	/**
	 * 새소식 삭제
	 *
	 * @param regNumber 새소식 시퀀스
	 * @return 삭제된 행 개수
	 * @throws DataAccessException
	 */
	/*public int delete(int regNumber) throws DataAccessException {
		return getSqlMapClientTemplate().delete("admin_news.delete", regNumber);
	}*/

	/**
	 * 새소식 로그 등록
	 *
	 * @param newsVO 새소식 객체
	 * @throws DataAccessException
	 */
	/*public void insertLog(NewsVO newsVO) throws DataAccessException {
		getSqlMapClientTemplate().insert("admin_news.insertLog", newsVO);
	}*/

	/**
	 * 새소식 전송 작업이 존재할 경우 작업중(WAS Index 번호)으로 표시한다.
	 *
	 * @param index WAS Index
	 * @return 업데이트 여부(1이상일 경우 업데이트 항목 존재)
	 * @throws DataAccessException
	 */
	/*public int markNews(String index) throws DataAccessException {
		return getSqlMapClientTemplate().update("admin_news.markNews", index);
	}*/

	/**
	 * WAS가 작업할 새소식 작업을 가져온다.
	 *
	 * @param index WAS Index
	 * @return 작업할 객체
	 * @throws DataAccessException
	 */
	/*public NewsVO getMarkingNews(String index) throws DataAccessException {
		return (NewsVO) getSqlMapClientTemplate().queryForObject("admin_news.getMarkingNews", index);
	}*/

	/**
	 * 새소식의 Sequence 를 가져온다.
	 *
	 * @return Sequence Number
	 * @throws DataAccessException
	 */
	public int getSequence() throws DataAccessException {
		return (Integer) getSqlMapClientTemplate().queryForObject("admin_news.getSequence");
	}

    /**
     * 이미지 서버의 URL을 가져온다.
     *
     * @return 이미지 서버 URL
     * @throws DataAccessException
     */
    public String getImageURL() throws DataAccessException {
        return (String) getSqlMapClientTemplate().queryForObject("admin_news.getImageURL");
    }

	/**
	 * 새소식 전송 상태값 변경
	 *
	 * @param resultMap 결과맵(result, index, regNumber)
	 * @return 성공 1 / 실패 0
	 * @throws DataAccessException
	 */
	/*public int updateNewsStatus(HashMap<String, String> resultMap) throws DataAccessException {
		return getSqlMapClientTemplate().update("admin_news.updateNewsStatus", resultMap);
	}*/

    /**
     * 타겟룰 조회
     *
     * @param regNumber 타겟룰 시퀀스
     * @return 타겟룰 객체
     * @throws DataAccessException
     */
    public TargetVO getTargetRule(int regNumber) throws DataAccessException {
        return (TargetVO) getSqlMapClientTemplate().queryForObject("admin_news.getTargetRule", regNumber);
    }

    /**
     * 타겟룰 등록
     *
     * @param newsVO TargetVO 상속 받은 새소식 객체
     * @throws DataAccessException
     */
    public void insertTargetRule(NewsVO newsVO) throws DataAccessException {
        getSqlMapClientTemplate().insert("admin_news.insertTargetRule", newsVO);
    }

    /**
     * 타겟룰 수정
     *
     * @param newsVO TargetVO 상속 받은 새소식 객체
     * @return 성공 1 / 실패 0
     * @throws DataAccessException
     */
    public int updateTargetRule(NewsVO newsVO) throws DataAccessException {
        return getSqlMapClientTemplate().update("admin_news.updateTargetRule", newsVO);
    }

    /**
     * 타겟룰 삭제
     *
     * @param regNumber 타겟룰 시퀀스
     * @return 삭제된 행 개수
     * @throws DataAccessException
     */
    public int deleteTargetRule(int regNumber) throws DataAccessException {
        return getSqlMapClientTemplate().delete("admin_news.deleteTargetRule", regNumber);
    }
}
