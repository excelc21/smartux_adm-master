package com.dmi.smartux.admin.statbbsAdmin.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.dmi.smartux.common.dao.CommonDao;
import com.dmi.smartux.admin.statbbsAdmin.vo.StatPaticipantListArrVo;
import com.dmi.smartux.admin.statbbsAdmin.vo.StatPaticipantListVo;
import com.dmi.smartux.admin.statbbsAdmin.vo.StatbbsInsertProcVo;
import com.dmi.smartux.admin.statbbsAdmin.vo.StatbbsListArrVo;
import com.dmi.smartux.admin.statbbsAdmin.vo.StatbbsListVo;
import com.dmi.smartux.admin.statbbsAdmin.vo.StatbbsMiniListVo;
import com.dmi.smartux.admin.statbbsAdmin.vo.StatbbsUpdateProcVo;
import com.dmi.smartux.admin.statbbsAdmin.vo.StatbbsUpdateVo;

@Repository
public class StatbbsAdminDao extends CommonDao {
	private final Log logger = LogFactory.getLog(this.getClass());
	
	/**
	 * StatBbs 리스트
	 * @param statbbslistVo
	 * @return
	 * @throws DataAccessException
	 */
	public List<StatbbsListArrVo> getStatbbsList(StatbbsListVo statbbslistVo) throws DataAccessException{
		
		statbbslistVo.setStart_rnum(statbbslistVo.getPageNum()*statbbslistVo.getPageSize()-statbbslistVo.getPageSize()+1);
		statbbslistVo.setEnd_rnum(statbbslistVo.getStart_rnum()+statbbslistVo.getPageSize()-1);
		
		List<StatbbsListArrVo> result = getSqlMapClientTemplate().queryForList("admin_statbbs.getStatbbsList", statbbslistVo);

		logger.debug("result.size() = " + result.size());
		
		return result;
	}

	/**
	 * StatBbs 리스트  총 갯수
	 * @param couponlistVo
	 * @return
	 * @throws DataAccessException
	 */
	public int getStatbbsListTotalCnt(StatbbsListVo statbbslistVo) throws DataAccessException{
		int result = 0;
		
		result = (Integer)getSqlMapClientTemplate().queryForObject("admin_statbbs.getStatbbsListTotalCnt", statbbslistVo); 
		
		return result;
	}
	
	/**
	 * StatBbs 등록
	 * @param statbbsinsertprocVo
	 * @return
	 * @throws DataAccessException
	 */
	public String setStatbbsInsertProc(StatbbsInsertProcVo statbbsinsertprocVo) throws DataAccessException{
		
		return (String)getSqlMapClientTemplate().insert("admin_statbbs.setStatbbsInsertProc",statbbsinsertprocVo);
		
	}
	
	/**
	 * StatBbs 등록 로그
	 * @param statbbsinsertprocVo
	 * @throws DataAccessException
	 */
	public void setStatbbsInsertLog(StatbbsInsertProcVo statbbsinsertprocVo) throws DataAccessException{
		
		getSqlMapClientTemplate().insert("admin_statbbs.setStatbbsInsertLog",statbbsinsertprocVo);
		
	}
	
	/**
	 * StatBbs 상세 보기
	 * @param stat_no
	 * @return
	 * @throws DataAccessException
	 */
	public StatbbsUpdateVo setStatbbsUpdate(String stat_no) throws DataAccessException{
		
		StatbbsUpdateVo statbbsupdateVo = (StatbbsUpdateVo)getSqlMapClientTemplate().queryForObject("admin_statbbs.setStatbbsUpdate", stat_no); 
		return statbbsupdateVo;
		
	}
	
	/**
	 * StatBbs 수정
	 * @param statbbsupdateprocVo
	 * @throws DataAccessException
	 */
	public void setStatbbsUpdateProc(StatbbsUpdateProcVo statbbsupdateprocVo) throws DataAccessException{
		
		getSqlMapClientTemplate().update("admin_statbbs.setStatbbsUpdateProc", statbbsupdateprocVo); 
		
	}
	
	/**
	 * StatBbs 수정 로그
	 * @param statbbsupdateprocVo
	 * @throws DataAccessException
	 */
	public void statbbsUpdateLog(StatbbsUpdateProcVo statbbsupdateprocVo) throws DataAccessException{
		
		getSqlMapClientTemplate().insert("admin_statbbs.statbbsUpdateLog",statbbsupdateprocVo);
		
	}
	
	/**
	 * StatBbs 삭제
	 * @param stat_id
	 * @throws DataAccessException
	 */
	public void setStatbbsDelete(String stat_id) throws DataAccessException{
		
		getSqlMapClientTemplate().delete("admin_statbbs.setStatbbsDelete", stat_id); 
		
	}
	
	/**
	 * 참여한 데이터 리스트 : 파일형식 저장으로 바뀌면서 안쓰게 됨.
	 * @param statbbslistVo
	 * @return
	 * @throws DataAccessException
	 */
	public List<StatPaticipantListArrVo> getStatPaticipantList(StatPaticipantListVo statpaticipantlistVo) throws DataAccessException{
		
		statpaticipantlistVo.setStart_rnum(statpaticipantlistVo.getPageNum()*statpaticipantlistVo.getPageSize()-statpaticipantlistVo.getPageSize()+1);
		statpaticipantlistVo.setEnd_rnum(statpaticipantlistVo.getStart_rnum()+statpaticipantlistVo.getPageSize()-1);
		
		List<StatPaticipantListArrVo> result = getSqlMapClientTemplate().queryForList("admin_statbbs.getStatPaticipantList", statpaticipantlistVo);

		logger.debug("result.size() = " + result.size());
		
		return result;
	}
	
	/**
	 * 참여한 데이터 리스트 엑셀 뽑기 : 파일형식 저장으로 바뀌면서 안쓰게 됨.
	 * @param statbbslistVo
	 * @return
	 * @throws DataAccessException
	 */
	public List<StatPaticipantListArrVo> excelPaticipantList(StatPaticipantListVo statpaticipantlistVo) throws DataAccessException{
		
		List<StatPaticipantListArrVo> result = getSqlMapClientTemplate().queryForList("admin_statbbs.excelPaticipantList", statpaticipantlistVo);

		logger.debug("result.size() = " + result.size());
		
		return result;
	}

	/**
	 * 참여한 데이터 리스트  총 갯수 : 파일형식 저장으로 바뀌면서 안쓰게 됨.
	 * @param couponlistVo
	 * @return
	 * @throws DataAccessException
	 */
	public int getStatPaticipantListCnt(StatPaticipantListVo statpaticipantlistVo) throws DataAccessException{
		int result = 0;
		
		result = (Integer)getSqlMapClientTemplate().queryForObject("admin_statbbs.getStatPaticipantListCnt", statpaticipantlistVo); 
		
		return result;
	}
	
	/**
	 * 참여통계 지정 갯수 리스트
	 * @param list_Cnt
	 * @return
	 * @throws Exception
	 */
	public List<StatbbsMiniListVo> getStatbbsMiniList(String list_Cnt) throws DataAccessException{
		
		List<StatbbsMiniListVo> result = getSqlMapClientTemplate().queryForList("admin_statbbs.getStatbbsMiniList", list_Cnt);

		logger.debug("result.size() = " + result.size());
		
		return result;
	}
	
	/**
	 * 활성화 된 참여통계 지정 갯수 리스트
	 * @param list_Cnt
	 * @return
	 * @throws Exception
	 */
	public List<StatbbsMiniListVo> getStatbbsActive(String list_Cnt) throws DataAccessException{
		
		List<StatbbsMiniListVo> result = getSqlMapClientTemplate().queryForList("admin_statbbs.getStatbbsActive", list_Cnt);

		logger.debug("result.size() = " + result.size());
		
		return result;
	}

}
