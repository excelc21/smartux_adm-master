package com.dmi.smartux.admin.statbbsAdmin.service;

import java.io.File;
import java.util.List;

import com.dmi.smartux.admin.statbbsAdmin.vo.StatParticipateFileVo;
import com.dmi.smartux.admin.statbbsAdmin.vo.StatPaticipantListArrVo;
import com.dmi.smartux.admin.statbbsAdmin.vo.StatPaticipantListVo;
import com.dmi.smartux.admin.statbbsAdmin.vo.StatbbsInsertProcVo;
import com.dmi.smartux.admin.statbbsAdmin.vo.StatbbsListArrVo;
import com.dmi.smartux.admin.statbbsAdmin.vo.StatbbsListVo;
import com.dmi.smartux.admin.statbbsAdmin.vo.StatbbsMiniListVo;
import com.dmi.smartux.admin.statbbsAdmin.vo.StatbbsUpdateProcVo;
import com.dmi.smartux.admin.statbbsAdmin.vo.StatbbsUpdateVo;

public interface StatbbsAdminService {
	
	/**
	 * StatBbs 리스트
	 * @param statbbslistVo
	 * @return
	 * @throws Exception
	 */
	public List<StatbbsListArrVo> getStatbbsList(StatbbsListVo statbbslistVo) throws Exception;

	/**
	 * StatBbs 리스트 총 갯수
	 * @param statbbslistVo
	 * @return
	 * @throws Exception
	 */
	public int getStatbbsListTotalCnt(StatbbsListVo statbbslistVo)throws Exception;
	
	/**
	 * StatBbs 등록
	 * @param statbbsinsertprocVo
	 * @throws Exception
	 */
	public String setStatbbsInsertProc(StatbbsInsertProcVo statbbsinsertprocVo) throws Exception;
	
	/**
	 * StatBbs 상세보기
	 * @param stat_no
	 * @return
	 * @throws Exception
	 */
	public StatbbsUpdateVo setStatbbsUpdate(String stat_no) throws Exception;
	
	/**
	 * StatBbs 수정
	 */
	public void setStatbbsUpdateProc(StatbbsUpdateProcVo statbbsupdateprocVo) throws Exception;
	
	/**
	 * StatBbs 삭제
	 * @param stat_no
	 * @param id
	 * @param ip
	 * @throws Exception
	 */
	public void setStatbbsDelete(String stat_no, String id, String ip) throws Exception;

	/**
	 * 참여한 데이터 리스트 : 파일형식 저장으로 바뀌면서 안쓰게 됨.
	 * @param statpaticipantlistVo
	 * @return
	 * @throws Exception
	 */
	public List<StatPaticipantListArrVo> getStatPaticipantList(StatPaticipantListVo statpaticipantlistVo) throws Exception;

	/**
	 * 참여한 데이터 리스트 총 갯수 : 파일형식 저장으로 바뀌면서 안쓰게 됨.
	 * @param statpaticipantlistVo
	 * @return
	 * @throws Exception
	 */
	public int getStatPaticipantListCnt(StatPaticipantListVo statpaticipantlistVo) throws Exception;

	/**
	 * 참여통계 지정 갯수 리스트
	 * @param list_Cnt
	 * @return
	 * @throws Exception
	 */
	public List<StatbbsMiniListVo> getStatbbsMiniList(String list_Cnt) throws Exception;

	/**
	 * 참여한 데이터 리스트 엑셀 뽑기 : 파일형식 저장으로 바뀌면서 안쓰게 됨.
	 * @param statpaticipantlistVo
	 * @return
	 * @throws Exception
	 */
	public File excelPaticipantList(StatPaticipantListVo statpaticipantlistVo) throws Exception;

	/**
	 * 활성화 된 참여통계 지정 갯수 리스트
	 * @param list_Cnt
	 * @return
	 * @throws Exception
	 */
	public List<StatbbsMiniListVo> getStatbbsActive(String list_Cnt) throws Exception;
	
	/**
	 * 파일에서 데이터를 가져온다.
	 * @param statpaticipantlistVo
	 * @return
	 * @throws Exception
	 */
	public StatParticipateFileVo getStatPaticipantLoadList(StatPaticipantListVo statpaticipantlistVo) throws Exception;


}
