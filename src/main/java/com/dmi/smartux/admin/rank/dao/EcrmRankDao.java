package com.dmi.smartux.admin.rank.dao;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import com.dmi.smartux.common.dao.CommonDao;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.smartstart.vo.GenreVodBestListVO;
import com.dmi.smartux.admin.code.vo.CodeItemVO;
import com.dmi.smartux.admin.rank.vo.EcrmRankVO;
import com.dmi.smartux.admin.rule.vo.RuleVO;
import com.dmi.smartux.admin.schedule.vo.ScheduleDetailVO;

@Repository
public class EcrmRankDao extends CommonDao {	

	private final Log logger = LogFactory.getLog(this.getClass());
		
	/**
	 * MaxCode 정보를 조회하는 DAO 클래스
	 * @return				MaxCode 정보 정보가 들어있는 String 객체
	 * @throws Exception
	 */
	public String getMaxCode() throws Exception{
		String maxcode = (String)(getSqlMapClientTemplate().queryForObject("admin_rank.getMaxCode"));
		return "R" + GlobalCom.appendLeftZero(maxcode, 4);
	}
	
	/**
	 * Rank 데이터 항목정보 정보를 조회하는 DAO 클래스
	 * @return				Rank 데이터 항목정보 정보가 들어있는 EcrmRankVO 객체가 들어있는 List 객체
	 * @throws Exception
	 */
	public List<EcrmRankVO> getRankList(String category_gb, String catrank_yn) throws Exception {
		// TODO Auto-generated method stub
		Map<String, String> param = new HashMap<String, String>();
		param.put("category_gb", category_gb);
		param.put("catrank_yn", catrank_yn);
		
		List<EcrmRankVO> result = getSqlMapClientTemplate().queryForList("admin_rank.getRankList", param);
	 
		return result;
	}

	/**
	 * Rank Item 코드값 등록처리하는 DAO 클래스
	 * @param rank_term		랭킹기한
	 * @param rank_name		랭킹이름
	 * @param hgenre		장르코드
	 * @param rule_name		룰이름
	 * @param smartUXManager		로그인아이디
	 * @return				
	 * @throws Exception
	 */
	public void insertRank(String rank_term, String rank_name, String hgenre, String rule_name, String login_id, String category_gb) throws Exception {
		// code 변수에 값이 아무것도 없을 경우는 신규로 입력하는 것이므로 getMaxCode() 값을 가져와야 한다
		// code 변수에 값이 있을 경우엔 입력된 코드로 입력하도록 한다
		String rank_code="";
		if(rank_code.equals("")){
			rank_code = getMaxCode();
		}
		Map<String, String> param = new HashMap<String, String>();
		param.put("rank_code", rank_code);
		param.put("rank_term", rank_term);
		param.put("rank_name", rank_name);
		param.put("hgenre", hgenre);
		param.put("rule_name", rule_name);
		param.put("create_id", login_id);
		param.put("update_id", login_id);
		param.put("category_gb", category_gb);
		
		getSqlMapClientTemplate().insert("admin_rank.insertRank", param);
	}
	
	/**
	 * 장르코드 선택 팝업 화면에서 장르대분류 정보를 조회하는 DAO 클래스
	 * @return				Rank 데이터 항목정보 정보가 들어있는 EcrmRankVO 객체가 들어있는 List 객체
	 * @throws Exception
	 */
	public List<EcrmRankVO> getGenreLargeList() throws Exception {
		// 장르대분류 리스트
		
		List<EcrmRankVO> result = getSqlMapClientTemplate().queryForList("admin_rank.getGenreLargeList");
	 
		return result;
	}	
	
	/**
	 * 장르코드 선택 팝업 화면에서 장르중분류 정보를 조회하는 DAO 클래스
	 * @return				Rank 데이터 항목정보 정보가 들어있는 EcrmRankVO 객체가 들어있는 List 객체
	 * @throws Exception
	 */
	public List<EcrmRankVO> getGenreMidList() throws Exception {
		// 장르중분류 리스트
		
		List<EcrmRankVO> result = getSqlMapClientTemplate().queryForList("admin_rank.getGenreMidList");
	 
		return result;
	}	
	
	/**
	 * 장르코드 선택 팝업 화면에서 장르소분류 정보를 조회하는 DAO 클래스
	 * @return				Rank 데이터 항목정보 정보가 들어있는 EcrmRankVO 객체가 들어있는 List 객체
	 * @throws Exception
	 */
	public List<EcrmRankVO> getGenreSmallList() throws Exception {
		// 장르소분류 리스트
		
		List<EcrmRankVO> result = getSqlMapClientTemplate().queryForList("admin_rank.getGenreSmallList");
	 
		return result;
	}
	
	/**
	 * Rank Item 코드값 삭제처리하는 DAO 클래스
	 * @param rank_codes[]		랭킹코드
	 * @return				
	 * @throws Exception
	 */
	public int deleteRank(String rank_code) throws Exception {
		// TODO Auto-generated method stub
		Map<String, String> param = new HashMap<String, String>();
		param.put("rank_code", rank_code);
		int result = getSqlMapClientTemplate().delete("admin_rank.deleteRank", param);
		return result;
	}
	
	/**
	 * Rank 데이터 항목정보 정보를 조회하는 DAO 클래스
	 * @param rank_code			랭킹코드
	 * @return				Rank 데이터 항목정보 정보가 들어있는 EcrmRankVO 객체
	 * @throws Exception
	 */
	public EcrmRankVO viewRankList(String rank_code) throws Exception {
		// TODO Auto-generated method stub
		Map<String, String> param = new HashMap<String, String>();
		param.put("rank_code", rank_code);

		EcrmRankVO result = (EcrmRankVO)(getSqlMapClientTemplate().queryForObject("admin_rank.viewRankList", param));
		
		return result;
	}
	
	/**
	 * 룰코드 항목정보 정보를 조회하는 DAO 클래스
	 * @param rule_code			룰코드
	 * @return				룰 데이터 항목정보 정보가 들어있는 RuleVO 객체가 들어있는 List 객체
	 * @throws Exception
	 */
	public RuleVO viewRule(String rule_code) throws Exception {
		// TODO Auto-generated method stub
		Map<String, String> param = new HashMap<String, String>();
		param.put("rule_code", rule_code);
		
		RuleVO result = (RuleVO)(getSqlMapClientTemplate().queryForObject("admin_rank.viewRule", param));
		return result;
	}
	
	/**
	 * Rank Item 코드값 변경처리하는 DAO 클래스
	 * @param rank_term		랭킹기한
	 * @param rank_name		랭킹이름
	 * @param hgenre		장르코드
	 * @param rule_name		룰이름
	 * @param smartUXManager		로그인아이디
	 * @return				
	 * @throws Exception
	 */
	public void updateRank(String rank_code, String rank_term, String rank_name, String hgenre, String rule_name, String login_id) throws Exception {
		// TODO Auto-generated method stub
		// code 변수에 값이 아무것도 없을 경우는 신규로 입력하는 것이므로 getMaxCode() 값을 가져와야 한다
		// code 변수에 값이 있을 경우엔 입력된 코드로 입력하도록 한다
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("rank_code", rank_code);
		param.put("rank_term", rank_term);
		param.put("rank_name", rank_name);
		param.put("hgenre", hgenre);
		param.put("rule_name", rule_name);
		param.put("update_id", login_id);
		
		getSqlMapClientTemplate().update("admin_rank.updateRank", param);
	}

	/**
	 * Rank 항목정보 미리보기 조회하는 DAO 클래스
	 * @param rank_code			랭킹코드
	 * @return				앨범 항목정보 정보가 들어있는 GenreVodBestListVO 객체가 들어있는 List 객체
	 * @throws Exception
	 */
	public List<GenreVodBestListVO> previewVodPopup(String rank_code) throws Exception {
		// TODO Auto-generated method stub
		Map<String, String> param = new HashMap<String, String>();
		param.put("rank_code", rank_code);
		
		List<GenreVodBestListVO> result = getSqlMapClientTemplate().queryForList("admin_rank.previewVodPopup", param);
		 
		return result;
	}

	/**
	 * viewAlbumVod 정보 조회하는 DAO 클래스
	 * @param sa_id			가입번호
	 * @return				자체편성정보가 들어있는 ScheduleDetailVO 객체가 들어있는 List 객체
	 * @throws Exception
	 */
	public List<ScheduleDetailVO> getAlbumDetailList(String rank_code, String rule_type) throws Exception {
		// TODO Auto-generated method stub		
		Map<String, String> param = new HashMap<String, String>();
		param.put("rank_code", rank_code);
		param.put("rule_type", rule_type);
		
		List<ScheduleDetailVO> result = getSqlMapClientTemplate().queryForList("admin_rank.getAlbumDetailList", param);
		 
		return result;
	}
	
	/**
	 * viewAlbumVod 정보 등록하는 DAO 클래스
	 * @param album_id		앨범ID
	 * @param ordered		순서정보
	 * @param rank_code		랭크코드
	 * @param album_name	앨범이름
	 * @return				
	 * @throws Exception
	 */
	public void insertAlbumVod(int ordered, String album_id, String album_name,String rank_code, String category_id) {
		// TODO Auto-generated method stub
		Map<String, String> param = new HashMap<String, String>();
		param.put("rank_code", rank_code);
		param.put("ordered", Integer.toString(ordered));
		param.put("album_id", album_id);
		param.put("album_name", album_name);
		param.put("category_id", category_id);
		
		getSqlMapClientTemplate().insert("admin_rank.insertAlbumVod", param);
	}

	/**
	 * viewAlbumVod 정보 조회하는 DAO 클래스
	 * @param album_id		앨범ID
	 * @return			EcrmRankVO 객체	
	 * @throws Exception
	 */
	public EcrmRankVO viewAlbumList(String album_id) throws Exception {
		// TODO Auto-generated method stub
		Map<String, String> param = new HashMap<String, String>();
		param.put("album_id", album_id);

		EcrmRankVO result = (EcrmRankVO)(getSqlMapClientTemplate().queryForObject("admin_rank.viewAlbumList", param));
		
		return result;
	}

	/**
	 * viewAlbumVod 정보 등록하는 DAO 클래스
	 * @param rank_code		랭크코드
	 * @return				
	 * @throws Exception
	 */
	public void deleteAlbumVod(String rank_code) throws Exception {
		// TODO Auto-generated method stub
		Map<String, String> param = new HashMap<String, String>();
		param.put("rank_code", rank_code);
		getSqlMapClientTemplate().delete("admin_rank.deleteAlbumBestList", param);
	}
		
	
	
	
}