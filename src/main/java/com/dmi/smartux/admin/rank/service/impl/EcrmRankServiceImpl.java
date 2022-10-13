package com.dmi.smartux.admin.rank.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmi.smartux.admin.code.vo.CodeItemVO;
import com.dmi.smartux.admin.code.vo.CodeVO;
import com.dmi.smartux.admin.schedule.vo.ScheduleDetailVO;
import com.dmi.smartux.admin.schedule.vo.ScheduleVO;
import com.dmi.smartux.admin.smartstart.dao.ItemListDao;
import com.dmi.smartux.admin.smartstart.service.ItemListService;
import com.dmi.smartux.admin.rank.service.EcrmRankService;
import com.dmi.smartux.admin.rank.vo.EcrmRankVO;
import com.dmi.smartux.admin.rank.dao.EcrmRankDao;
import com.dmi.smartux.admin.rule.vo.RuleVO;
import com.dmi.smartux.smartstart.vo.GenreVodBestListVO;

@Service
public class EcrmRankServiceImpl implements EcrmRankService {
	
	private final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	EcrmRankDao dao;
	
	/**
	 * Rank 데이터 항목정보 정보를 조회하는 서비스구현 클래스
	 * @return				Rank 데이터 항목정보 정보가 들어있는 EcrmRankVO 객체가 들어있는 List 객체
	 * @throws Exception
	 */
	@Override
	@Transactional(readOnly=true)
	public List<EcrmRankVO> getRankList(String category_gb, String catrank_yn) throws Exception {
		// TODO Auto-generated method stub
		return dao.getRankList(category_gb, catrank_yn);
	}
	
	/**
	 * Rank 데이터 항목정보 정보를 조회하는 서비스구현 클래스
	 * @param rank_code			랭킹코드
	 * @return				Rank 데이터 항목정보 정보가 들어있는 EcrmRankVO 객체
	 * @throws Exception
	 */
	@Override
	@Transactional(readOnly=true)
	public EcrmRankVO viewRankList(String rank_code) throws Exception {
		// TODO Auto-generated method stub
		return dao.viewRankList(rank_code);
	}
	
	/**
	 * Rank Item 코드값 등록처리하는 서비스구현 클래스
	 * @param rank_term		랭킹기한
	 * @param rank_name		랭킹이름
	 * @param hgenre		장르코드
	 * @param rule_name		룰이름
	 * @param smartUXManager		로그인아이디
	 * @return				
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void insertRank(String rank_term, String rank_name, String hgenre, String rule_name, String login_id, String category_gb) throws Exception {
		// TODO Auto-generated method stub
		dao.insertRank(rank_term, rank_name, hgenre ,rule_name, login_id, category_gb);
	}
	
	/**
	 * 룰코드 항목정보 정보를 조회하는 서비스구현 클래스
	 * @param rule_code			룰코드
	 * @return				룰 데이터 항목정보 정보가 들어있는 RuleVO 객체가 들어있는 List 객체
	 * @throws Exception
	 */
	@Override
	@Transactional(readOnly=true)
	public RuleVO viewRule(String rule_code) throws Exception {
		// TODO Auto-generated method stub
		return dao.viewRule(rule_code);
	}
	
	/**
	 * Rank Item 코드값 변경처리하는 서비스구현 클래스
	 * @param rank_term		랭킹기한
	 * @param rank_name		랭킹이름
	 * @param hgenre		장르코드
	 * @param rule_name		룰이름
	 * @param smartUXManager		로그인아이디
	 * @return				
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void updateRank(String rank_code, String rank_term, String rank_name, String hgenre, String rule_name , String login_id) throws Exception {
		// TODO Auto-generated method stub
		dao.updateRank(rank_code, rank_term, rank_name, hgenre ,rule_name, login_id);
	}

	/**
	 * Rank Item 코드값 삭제처리하는 서비스구현 클래스
	 * @param rank_codes[]		랭킹코드
	 * @return				
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void deleteRank(String [] rank_codes) throws Exception {
		// TODO Auto-generated method stub
		for(String rank_code : rank_codes){
			dao.deleteRank(rank_code);
		}
	}
	
	/**
	 * 장르코드 선택 팝업 화면에서 장르대분류 정보를 조회하는 서비스구현 클래스
	 * @return				Rank 데이터 항목정보 정보가 들어있는 EcrmRankVO 객체가 들어있는 List 객체
	 * @throws Exception
	 */
	@Override
	@Transactional
	public List<EcrmRankVO> getGenreLargeList() throws Exception {
		// TODO Auto-generated method stub
		return dao.getGenreLargeList() ;
	}
	
	/**
	 * 장르코드 선택 팝업 화면에서 장르중분류 정보를 조회하는 서비스구현 클래스
	 * @return				Rank 데이터 항목정보 정보가 들어있는 EcrmRankVO 객체가 들어있는 List 객체
	 * @throws Exception
	 */
	@Override
	@Transactional
	public List<EcrmRankVO> getGenreMidList() throws Exception {
		// TODO Auto-generated method stub
		return dao.getGenreMidList() ;
	}
	
	/**
	 * 장르코드 선택 팝업 화면에서 장르소분류 정보를 조회하는 서비스구현 클래스
	 * @return				Rank 데이터 항목정보 정보가 들어있는 EcrmRankVO 객체가 들어있는 List 객체
	 * @throws Exception
	 */
	@Override
	@Transactional
	public List<EcrmRankVO> getGenreSmallList() throws Exception {
		// TODO Auto-generated method stub
		return dao.getGenreSmallList() ;
	}

	/**
	 * Rank 항목정보 미리보기 조회하는 서비스구현 클래스
	 * @param rank_code			랭킹코드
	 * @return				앨범 항목정보 정보가 들어있는 GenreVodBestListVO 객체가 들어있는 List 객체
	 * @throws Exception
	 */
	@Override
	@Transactional
	public List<GenreVodBestListVO> previewVodPopup(String rank_code) throws Exception {
		// TODO Auto-generated method stub
		return dao.previewVodPopup(rank_code) ;
	}

	/**
	 * viewAlbumVod 정보 조회하는 서비스구현 클래스
	 * @param sa_id			가입번호
	 * @return				자체편성정보가 들어있는 ScheduleDetailVO 객체가 들어있는 List 객체
	 * @throws Exception
	 */
	@Override
	@Transactional
	public List<ScheduleDetailVO> getAlbumDetailList(String rank_code, String rule_type)	throws Exception {
		// TODO Auto-generated method stub
		return dao.getAlbumDetailList(rank_code, rule_type);
	}

	/**
	 * Rank Item 앨범 변경처리하는 서비스구현 클래스
	 * @param album_id[]		앨범ID
	 * @param category_id[]		카테고리D
	 * @param rank_code			랭크코드
	 * @param rule_name			룰이름
	 * @param login_id			로그인아이디
	 * @return				
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void updateAlbumVod(String[] album_id, String[] category_id,String rank_code, String login_id, String rule_type) throws Exception {
		// TODO Auto-generated method stub
		dao.deleteAlbumVod( rank_code );
		
		int length = album_id.length;
		for(int i=1; i < length + 1; i++){
			//dao.viewAlbumList(album_id[i]);
			EcrmRankVO result = (EcrmRankVO)(dao.viewAlbumList(album_id[i-1]));
			//insertAlbumVod( i, album_id[i-1], result.getAlbum_name(), rank_code);
			if("S".equals(rule_type)){
				dao.insertAlbumVod( i, album_id[i-1], result.getAlbum_name(), rank_code, category_id[i-1]);
			}else{
				dao.insertAlbumVod( i, album_id[i-1], result.getAlbum_name(), rank_code, "");
			}
		}
	}	
}

