package com.dmi.smartux.admin.rank.service;

import java.util.List;

import com.dmi.smartux.admin.rank.vo.EcrmRankVO;
import com.dmi.smartux.admin.rule.vo.RuleVO;
import com.dmi.smartux.admin.schedule.vo.ScheduleDetailVO;
import com.dmi.smartux.smartstart.vo.GenreVodBestListVO;



public interface EcrmRankService {	
	/**
	 * Rank 데이터 항목정보 정보를 조회하는 Service 인터페이스
	 * @return				Rank 데이터 항목정보 정보가 들어있는 EcrmRankVO 객체가 들어있는 List 객체
	 * @throws Exception
	 */
	public List<EcrmRankVO> getRankList(String category_gb, String catrank_yn) throws Exception;
	
	/**
	 * 장르코드 선택 팝업 화면에서 장르대분류 정보를 조회하는 Service 인터페이스
	 * @return				Rank 데이터 항목정보 정보가 들어있는 EcrmRankVO 객체가 들어있는 List 객체
	 * @throws Exception
	 */
	public List<EcrmRankVO> getGenreLargeList() throws Exception;	
	
	/**
	 * 장르코드 선택 팝업 화면에서 장르중분류 정보를 조회하는 Service 인터페이스
	 * @return				Rank 데이터 항목정보 정보가 들어있는 EcrmRankVO 객체가 들어있는 List 객체
	 * @throws Exception
	 */
	public List<EcrmRankVO> getGenreMidList() throws Exception;	

	/**
	 * 장르코드 선택 팝업 화면에서 장르소분류 정보를 조회하는 Service 인터페이스
	 * @return				Rank 데이터 항목정보 정보가 들어있는 EcrmRankVO 객체가 들어있는 List 객체
	 * @throws Exception
	 */
	public List<EcrmRankVO> getGenreSmallList() throws Exception;

	/**
	 * Rank Item 코드값 등록처리하는 Service 인터페이스
	 * @param rank_term		랭킹기한
	 * @param rank_name		랭킹이름
	 * @param hgenre		장르코드
	 * @param rule_name		룰이름
	 * @param smartUXManager		로그인아이디
	 * @return				
	 * @throws Exception
	 */
	public void insertRank(String rank_term, String rank_name, String hgenre,	String rule_name, String login_id, String category_gb) throws Exception;
	
	/**
	 * Rank Item 코드값 변경처리하는 Service 인터페이스
	 * @param rank_term		랭킹기한
	 * @param rank_name		랭킹이름
	 * @param hgenre		장르코드
	 * @param rule_name		룰이름
	 * @param smartUXManager		로그인아이디
	 * @return				
	 * @throws Exception
	 */
	public void updateRank(String rank_code, String rank_term, String rank_name, String hgenre,  String rule_name , String login_id) throws Exception;

	/**
	 * Rank Item 코드값 삭제처리하는 Service 인터페이스
	 * @param rank_codes[]		랭킹코드
	 * @return				
	 * @throws Exception
	 */
	public void deleteRank(String[] rank_codes) throws Exception;

	/**
	 * Rank 데이터 항목정보 정보를 조회하는 Service 인터페이스
	 * @param rank_code			랭킹코드
	 * @return				Rank 데이터 항목정보 정보가 들어있는 EcrmRankVO 객체
	 * @throws Exception
	 */
	public EcrmRankVO viewRankList(String rank_code) throws Exception;

	/**
	 * Rank 항목정보 미리보기 조회하는 Service 인터페이스
	 * @param rank_code			랭킹코드
	 * @return				앨범 항목정보 정보가 들어있는 GenreVodBestListVO 객체가 들어있는 List 객체
	 * @throws Exception
	 */
	public List<GenreVodBestListVO> previewVodPopup(String rank_code) throws Exception;

	/**
	 * viewAlbumVod 정보 조회하는 Service 인터페이스
	 * @param sa_id			가입번호
	 * @return				자체편성정보가 들어있는 ScheduleDetailVO 객체가 들어있는 List 객체
	 * @throws Exception
	 */
	public List<ScheduleDetailVO> getAlbumDetailList(String rank_code, String rule_type) throws Exception;

	/**
	 * Rank Item 앨범 변경처리하는 Service 인터페이스
	 * @param album_id[]		앨범ID
	 * @param category_id[]		카테고리D
	 * @param rank_code			랭크코드
	 * @param rule_name			룰이름
	 * @param login_id			로그인아이디
	 * @return				
	 * @throws Exception
	 */
	public void updateAlbumVod(String[] album_id, String[] category_id, String rank_code, String login_id, String rule_type) throws Exception;

	/**
	 * 룰코드 항목정보 정보를 조회하는 Service 인터페이스
	 * @param rule_code			룰코드
	 * @return				룰 데이터 항목정보 정보가 들어있는 RuleVO 객체가 들어있는 List 객체
	 * @throws Exception
	 */
	public RuleVO viewRule(String rule_code) throws Exception;	
	
	
	
}

