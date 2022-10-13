package com.dmi.smartux.admin.statbbsAdmin.service.impl;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmi.smartux.admin.statbbsAdmin.dao.StatbbsAdminDao;
import com.dmi.smartux.admin.statbbsAdmin.service.StatbbsAdminService;
import com.dmi.smartux.admin.statbbsAdmin.vo.StatParticipateFileVo;
import com.dmi.smartux.admin.statbbsAdmin.vo.StatPaticipantListArrVo;
import com.dmi.smartux.admin.statbbsAdmin.vo.StatPaticipantListVo;
import com.dmi.smartux.admin.statbbsAdmin.vo.StatbbsInsertProcVo;
import com.dmi.smartux.admin.statbbsAdmin.vo.StatbbsListArrVo;
import com.dmi.smartux.admin.statbbsAdmin.vo.StatbbsListVo;
import com.dmi.smartux.admin.statbbsAdmin.vo.StatbbsMiniListVo;
import com.dmi.smartux.admin.statbbsAdmin.vo.StatbbsUpdateProcVo;
import com.dmi.smartux.admin.statbbsAdmin.vo.StatbbsUpdateVo;
import com.dmi.smartux.common.exception.SmartUXException;
import com.dmi.smartux.common.util.HTMLCleaner;

@Service
public class StatbbsAdminServiceImpl implements StatbbsAdminService {

	@Autowired
	StatbbsAdminDao dao;

	@Override
	public List<StatbbsListArrVo> getStatbbsList ( StatbbsListVo statbbslistVo ) throws Exception {

		List<StatbbsListArrVo> result = null;

		result = dao.getStatbbsList ( statbbslistVo );

		return result;
	}

	@Override
	public int getStatbbsListTotalCnt ( StatbbsListVo statbbslistVo ) throws Exception {
		int result = 0;

		result = dao.getStatbbsListTotalCnt ( statbbslistVo );

		return result;
	}

	@Override
	@Transactional
	public String setStatbbsInsertProc ( StatbbsInsertProcVo statbbsinsertprocVo ) throws Exception {
		String statNo = dao.setStatbbsInsertProc ( statbbsinsertprocVo );

		statbbsinsertprocVo.setStat_no ( statNo );
		dao.setStatbbsInsertLog ( statbbsinsertprocVo );
		return statNo;
	}

	@Override
	public StatbbsUpdateVo setStatbbsUpdate ( String stat_no ) throws Exception {
		return dao.setStatbbsUpdate ( stat_no );
	}

	@Override
	@Transactional
	public void setStatbbsUpdateProc ( StatbbsUpdateProcVo statbbsupdateprocVo ) throws Exception {
		dao.setStatbbsUpdateProc ( statbbsupdateprocVo );
		dao.statbbsUpdateLog ( statbbsupdateprocVo );
	}

	@Override
	@Transactional
	public void setStatbbsDelete ( String stat_no, String id, String ip ) throws Exception {

		HTMLCleaner cleaner = new HTMLCleaner ( );

		String[] stat_no_arr = stat_no.split ( "," );
		StatbbsUpdateProcVo logVo = new StatbbsUpdateProcVo ( );
		for ( String temp_no : stat_no_arr ) {
			String clean_no = cleaner.clean ( temp_no );
			logVo.setStat_no ( clean_no );
			logVo.setAct_gbn ( "D" );
			logVo.setAct_id ( id );
			logVo.setAct_ip ( ip );
			dao.statbbsUpdateLog ( logVo );
			dao.setStatbbsDelete ( clean_no );
		}
	}

	@Override
	public List<StatPaticipantListArrVo> getStatPaticipantList ( StatPaticipantListVo statpaticipantlistVo ) throws Exception {

		List<StatPaticipantListArrVo> result = null;

		result = dao.getStatPaticipantList ( statpaticipantlistVo );

		return result;
	}

	@Override
	public File excelPaticipantList ( StatPaticipantListVo statpaticipantlistVo ) throws Exception {

		String stat_no = statpaticipantlistVo.getStat_no ( );
		File statFile = null;

		try {
			String statName = "";
			String filePath = "";

			StatbbsUpdateVo statbbsupdateVo = setStatbbsUpdate ( stat_no );
			if ( statbbsupdateVo != null ) {
				statName = statbbsupdateVo.getTitle ( );
				filePath = statbbsupdateVo.getStat_file_path ( );
			} else {
				SmartUXException ex = new SmartUXException ( );
				throw ex;
			}

			statFile = new File ( filePath );
		} catch ( java.lang.Exception e ) {
			System.out.println ( e.getClass ( ).getName ( ) );
			throw e;
		}

		return statFile;
	}

	@Override
	public int getStatPaticipantListCnt ( StatPaticipantListVo statpaticipantlistVo ) throws Exception {
		int result = 0;

		result = dao.getStatPaticipantListCnt ( statpaticipantlistVo );

		return result;
	}

	@Override
	public List<StatbbsMiniListVo> getStatbbsMiniList ( String list_Cnt ) throws Exception {

		List<StatbbsMiniListVo> result = null;

		result = dao.getStatbbsMiniList ( list_Cnt );

		return result;
	}

	@Override
	public List<StatbbsMiniListVo> getStatbbsActive ( String list_Cnt ) throws Exception {

		List<StatbbsMiniListVo> result = null;

		result = dao.getStatbbsActive ( list_Cnt );

		return result;
	}

	@Override
	public StatParticipateFileVo getStatPaticipantLoadList ( StatPaticipantListVo statpaticipantlistVo ) throws Exception {

		StatParticipateFileVo statparticipatefileVo = new StatParticipateFileVo ( );
		statpaticipantlistVo.setStart_rnum ( statpaticipantlistVo.getPageNum ( ) * statpaticipantlistVo.getPageSize ( ) - statpaticipantlistVo.getPageSize ( ) + 1 );
		statpaticipantlistVo.setEnd_rnum ( statpaticipantlistVo.getStart_rnum ( ) + statpaticipantlistVo.getPageSize ( ) - 1 );

		String stat_no = statpaticipantlistVo.getStat_no ( );

		try {
			String statName = "";
			String filePath = "";

			StatbbsUpdateVo statbbsupdateVo = setStatbbsUpdate ( stat_no );
			if ( statbbsupdateVo != null ) {
				statName = statbbsupdateVo.getTitle ( );
				filePath = statbbsupdateVo.getStat_file_path ( );
			} else {
				SmartUXException ex = new SmartUXException ( );
				throw ex;
			}

			FileInputStream filein = null;
			DataInputStream in = null;
			BufferedReader br = null;
			List<StatPaticipantListArrVo> statpaticipantlistarrVoArr = new ArrayList<StatPaticipantListArrVo> ( );
			int ArrNum = 0;
			try {
				if ( new File ( filePath ).exists ( ) ) {// 파일 있다면
					filein = new FileInputStream ( filePath );
					in = new DataInputStream ( filein );
					br = new BufferedReader ( new InputStreamReader ( in ) );

					String lineData = null;
					while ( ( lineData = br.readLine ( ) ) != null ) {
						if ( statpaticipantlistVo.getStart_rnum ( ) > ++ArrNum ) continue;// 해당 시작 넘버만큼 건너띈다.

						if ( statpaticipantlistVo.getEnd_rnum ( ) >= ArrNum ) {
							String[] arrStr = lineData.split ( "," );
							StatPaticipantListArrVo statpaticipantlistarrVo = new StatPaticipantListArrVo ( );
							statpaticipantlistarrVo.setStat_no ( stat_no );
							statpaticipantlistarrVo.setTitle ( statName );
							if ( arrStr.length >= 4 ) {
								statpaticipantlistarrVo.setSa_id ( arrStr[0] );
								statpaticipantlistarrVo.setMac ( arrStr[1] );
								statpaticipantlistarrVo.setCtn ( arrStr[2] );
								statpaticipantlistarrVo.setR_date ( arrStr[3] );
							}

							statpaticipantlistarrVoArr.add ( statpaticipantlistarrVo );
						}
					}
				}
			} catch ( java.io.EOFException e ) {// 이건 정상.. 더이상 뺄게 없다는 거다..
			} catch ( java.lang.Exception e ) {
				throw e;
			} finally {
				try {
					try {
						in.close ( );
						filein.close ( );
					} catch ( java.lang.Exception e ) {
					}
				} catch ( java.lang.Exception e ) {
				}
			}
			statparticipatefileVo.setStatpaticipantlistarrVo ( statpaticipantlistarrVoArr );
			statparticipatefileVo.setTotalCnt ( ArrNum );
		} catch ( Exception e ) {
			throw e;
		}

		return statparticipatefileVo;
	}

}
