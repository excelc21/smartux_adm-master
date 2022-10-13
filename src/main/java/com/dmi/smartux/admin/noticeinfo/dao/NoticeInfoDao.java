package com.dmi.smartux.admin.noticeinfo.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.dmi.smartux.admin.noticeinfo.vo.InsertNoticeInfoVo;
import com.dmi.smartux.admin.noticeinfo.vo.NotiCodeListVo;
import com.dmi.smartux.admin.noticeinfo.vo.NotiMainCodeListVo;
import com.dmi.smartux.admin.noticeinfo.vo.NoticeInfoListProcVo;
import com.dmi.smartux.admin.noticeinfo.vo.NoticeInfoListVo;
import com.dmi.smartux.admin.noticeinfo.vo.UpdateNoticeInfoVo;
import com.dmi.smartux.admin.noticeinfo.vo.ViewNoticeInfoListVo;
import com.dmi.smartux.common.dao.CommonDao;

@Repository("adminNoticeInfoDao")
public class NoticeInfoDao extends CommonDao {
	private final Log logger = LogFactory.getLog(this.getClass());
	
	/**
	 * NoticeInfo 공지 리스트
	 * @param noticeinfolistprocVo
	 * @return
	 * @throws DataAccessException
	 */
	public List<NoticeInfoListVo> getNoticeInfoList(NoticeInfoListProcVo noticeinfolistprocVo) throws DataAccessException{
		
		List<NoticeInfoListVo> result = getSqlMapClientTemplate().queryForList("admin_noticeinfo.getNoticeInfoList", noticeinfolistprocVo);

		logger.debug("result.size() = " + result.size());
		
		return result;
	}

	/**
	 * NoticeInfo 공지 리스트 총 갯수
	 * @param noticeinfolistprocVo
	 * @return
	 * @throws DataAccessException
	 */
	public int getNoticeInfoListTotalCnt(NoticeInfoListProcVo noticeinfolistprocVo) throws DataAccessException{
		int result = 0;
		
		result = (Integer)getSqlMapClientTemplate().queryForObject("admin_noticeinfo.getNoticeInfoListTotalCnt", noticeinfolistprocVo); 
		
		return result;
	}
	
	/**
	 * NoticeInfo 장르 코드 목록
	 * @return
	 * @throws DataAccessException
	 */
	public List<NotiCodeListVo> getNoticeInfoCodeList() throws DataAccessException{
		
		List<NotiCodeListVo> result = getSqlMapClientTemplate().queryForList("admin_noticeinfo.getNoticeInfoCodeList");

		logger.debug("result.size() = " + result.size());
		
		return result;
	}
	
	/**
	 * NoticeInfo 메인 코드 목록
	 * @return
	 * @throws DataAccessException
	 */
	public List<NotiMainCodeListVo> getNotiMainCodeList() throws DataAccessException{
		
		List<NotiMainCodeListVo> result = getSqlMapClientTemplate().queryForList("admin_noticeinfo.getNotiMainCodeList");

		logger.debug("result.size() = " + result.size());
		
		return result;
	}
	
	/**
	 * NoticeInfo 저장
	 * @param insertnoticeinfoVo
	 * @throws Exception
	 */
	public void insertNoticeInfo(InsertNoticeInfoVo insertnoticeinfoVo) throws Exception {
		
		getSqlMapClientTemplate().insert("admin_noticeinfo.insertNoticeInfo", insertnoticeinfoVo);
	}
	
	/**
	 * NoticeInfo 수정
	 * @param updatenoticeinfoVo
	 * @throws Exception
	 */
	public void updateNoticeInfo(UpdateNoticeInfoVo updatenoticeinfoVo) throws Exception {
		
		getSqlMapClientTemplate().update("admin_noticeinfo.updateNoticeInfo", updatenoticeinfoVo);
	}
	
	/**
	 * NoticeInfo 삭제
	 * @param seq
	 * @throws Exception
	 */
	public void deleteNoticeInfo(String seq) throws Exception {
		
		getSqlMapClientTemplate().delete("admin_noticeinfo.deleteNoticeInfo", seq);
	}
	

	/**
	 * NoticeInfo 프리뷰 조회
	 * @param service
	 * @return
	 * @throws Exception
	 */
	public List<ViewNoticeInfoListVo> viewNoticeInfoList(String service) throws DataAccessException{
		List<ViewNoticeInfoListVo> result = null;
		result = getSqlMapClientTemplate().queryForList("admin_noticeinfo.viewNoticeInfoList", service);
		return result;
	}

}
