package com.dmi.smartux.admin.schedule.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.dmi.smartux.admin.schedule.vo.CategoryAlbumVO;
import com.dmi.smartux.admin.schedule.vo.ScheduleDetailVO;
import com.dmi.smartux.admin.schedule.vo.ScheduleVO;
import com.dmi.smartux.common.dao.CommonDao;

@Repository
public class ScheduleDao extends CommonDao {

	/**
	 * 자체편성 정보 테이블에서 자체편성 목록 조회
	 * @return		조회된 자체편성 목록
	 * @throws Exception
	 */
	public List<ScheduleVO> getScheduleList(String category_gb) throws Exception {
		// TODO Auto-generated method stub
		Map<String, String> param = new HashMap<String, String>();
		param.put("category_gb", category_gb);
		List<ScheduleVO> result = getSqlMapClientTemplate().queryForList("admin_schedule.getScheduleList", param);
		return result;
	}

	/**
	 * 자체편성 테이블에서 자체편성 상세조회 
	 * @param schedule_code		상세조회하고자 하는 자체편성의 schedule_code
	 * @return					조회된 자체편성 상세조회 정보
	 * @throws Exception
	 */
	public ScheduleVO viewSchedule(String schedule_code) throws Exception {
		// TODO Auto-generated method stub
		Map<String, String> param = new HashMap<String, String>();
		param.put("schedule_code", schedule_code);
		
		ScheduleVO result = (ScheduleVO)(getSqlMapClientTemplate().queryForObject("admin_schedule.viewSchedule", param));
		return result;
	}
	
	/**
	 * 자체편성 테이블에 자체편성 등록
	 * @param schedule_name		등록하고자 하는 자체편성의 자체편성 이름
	 * @param create_id			관리자 사이트에 로그인 한 사람의 로그인 id
	 * @return					등록된 자체편성의 schedule_code
	 * @throws Exception
	 */
	public long insertSchedule(String schedule_name, String create_id, String category_gb, String test_id, String variation_id) throws Exception {
		// TODO Auto-generated method stub
		
		Map<String, String> param = new HashMap<String, String>();
		// param.put("rule_code", "");
		param.put("schedule_name", schedule_name);
		param.put("create_id", create_id);
		param.put("category_gb", category_gb);
		param.put("test_id", test_id);
		param.put("variation_id", variation_id);
		
		return (Long)(getSqlMapClientTemplate().insert("admin_schedule.insertSchedule", param));
	}

	/**
	 * 자체편성 테이블에 자체편성 수정
	 * @param schedule_code		수정하고자 하는 자체편성의 shcedule_code
	 * @param schedule_name		수정하고자 하는 자체편성의 자체편성 이름
	 * @param update_id			관리자 사이트에 로그인 한 사람의 로그인 id
	 * @return					수정된 자체편성의 갯수
	 * @throws Exception
	 */
	public int updateSchedule(String schedule_code, String schedule_name, String update_id, String category_gb, String test_id, String variation_id) throws Exception {
		// TODO Auto-generated method stub
		Map<String, String> param = new HashMap<String, String>();
		param.put("schedule_name", schedule_name);
		param.put("update_id", update_id);
		param.put("schedule_code", schedule_code);
		param.put("category_gb", category_gb);
		param.put("test_id", test_id);
		param.put("variation_id", variation_id);
		
		int result = getSqlMapClientTemplate().update("admin_schedule.updateSchedule", param);
		return result;
	}

	/**
	 * 자체편성 테이블에서 자체편성 삭제
	 * @param schedule_code		삭제하고자 하는 자체편성의 schedule_code
	 * @return					삭제된 자체편성의 갯수
	 * @throws Exception
	 */
	public int deleteSchedule(String schedule_code) throws Exception {
		// TODO Auto-generated method stub
		Map<String, String> param = new HashMap<String, String>();
		param.put("schedule_code", schedule_code);
		int result = getSqlMapClientTemplate().delete("admin_schedule.deleteSchedule", param);
		return result;
	}
	
	/**
	 * 자체편성 정보 상세 테이블에서 자체편성 서브 데이터 조회
	 * @param schedule_code		자체편성의 schedule_code
	 * @return					조회된 자체편성 서브 데이터
	 * @throws Exception
	 */
	public List<ScheduleDetailVO> getScheduleDetailList(String schedule_code, String category_gb) throws Exception {
		// TODO Auto-generated method stub
		Map<String, String> param = new HashMap<String, String>();
		param.put("schedule_code", schedule_code);
		param.put("category_gb", category_gb);
		List<ScheduleDetailVO> result = getSqlMapClientTemplate().queryForList("admin_schedule.getScheduleDetailList", param);
		return result;
	}
	
	/**
	 * 자체편성 정보 상세 테이블에서 자체편성 서브 데이터 등록
	 * @param schedule_code		등록하고자 하는 자체편성 서브 데이터가 속하는 자체편성의 schedule_code
	 * @param album_id			앨범코드
	 * @param category_id		카테고리 코드
	 * @param ordered			앨범순서
	 * @param create_id			관리자 사이트에 로그인 한 사람의 로그인 id
	 * @throws Exception
	 */
	public void insertScheduleDetail(String schedule_code, String album_id, String category_id, int ordered, String create_id) throws Exception {
		// TODO Auto-generated method stub
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("schedule_code", schedule_code);
		param.put("album_id", album_id);
		param.put("category_id", category_id);
		param.put("ordered", String.valueOf(ordered));
		param.put("create_id", create_id);
		
		getSqlMapClientTemplate().insert("admin_schedule.insertScheduleDetail", param);
	}
	
	/**
	 * 자체편성 상세 정보 테이블에서 자체편성 서브 데이터 삭제
	 * @param schedule_code		삭제하고자 하는 자체편성 서브 데이터가 속하는 자체편성의 schedule_code
	 * @return					삭제된 자체편성 서브 데이터 갯수
	 * @throws Exception
	 */
	public int deleteScheduleDetail(String schedule_code) throws Exception {
		// TODO Auto-generated method stub
		Map<String, String> param = new HashMap<String, String>();
		param.put("schedule_code", schedule_code);
		int result = getSqlMapClientTemplate().delete("admin_schedule.deleteScheduleDetail", param);
		return result;
	}
	
	/**
	 * 카테고리 코드를 이용하여 입력받은 카테고리의 하위 카테고리와 앨범들을 조회
	 * @param category_id	카테고리 코드
	 * @return				하위 카테고리와 앨범 정보
	 * @throws Exception
	 */
	public List<CategoryAlbumVO> getCategoryAlbumList(String category_id, String type, String series_yn) throws Exception{
		Map<String, String> param = new HashMap<String, String>();
		param.put("category_id", category_id);
		param.put("type", type);
		param.put("series_yn", series_yn);
		
		List<CategoryAlbumVO> result = getSqlMapClientTemplate().queryForList("admin_schedule.getCategoryAlbumList", param);
		
		return result;
	}
	
	/**
	 * 구분자로 결합된 자체편성의 schedule_code값들이 지면 상용과 지면 임시에서 사용되고 있는지를 조사하여 사용되고 있을 경우 사용되고 있는 자체편성의 정보를 조회한다
	 * CommMimsDao로 이동
	 * @param schedule_codes		구분자로 결합된 자체편성의 schedule_code값
	 * @return						자체편성 정보
	 * @throws Exception
	 */
	/*public List<ScheduleVO> selectUseSchedule(String schedule_codes) throws Exception {
		// TODO Auto-generated method stub
		Map<String, String> param = new HashMap<String, String>();
		param.put("schedule_codes", schedule_codes);
		
		List<ScheduleVO> result = getSqlMapClientTemplate().queryForList("admin_schedule.selectUseSchedule", param);
		return result;
	}*/

	
	public List<CategoryAlbumVO> getAlbumListByCategoryIdAlbumId(String category_id,String album_id,String type) throws Exception{
		Map<String, String> param = new HashMap<String, String>();
		param.put("category_id", category_id);
		param.put("album_id", album_id);
		param.put("type", type);
		
		List<CategoryAlbumVO> result = getSqlMapClientTemplate().queryForList("admin_schedule.getAlbumListByCategoryIdAlbumId", param);
		
		return result;
	}
	
	
	
	
	/**
	 * 메인패널 편성 시 이미지 등록 및 수정 16.12.09
	 * @param scheduleImgVO
	 * @throws Exception
	 */
	public void mergeContentsImg(String albumId, String widthImg, String heightImg, String createId) throws Exception{
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("albumId", albumId);
		paramMap.put("widthImg", widthImg);
		paramMap.put("heightImg", heightImg);
		paramMap.put("createId", createId);
		getSqlMapClientTemplate().insert("admin_schedule.mergeContentsImg", paramMap);
	}
	
	/**
	 * 순서 변경
	 *
	 * @param PanelVO  객체
	 * @throws DataAccessException
	 */
	public void changeOrder(ScheduleDetailVO vo) throws DataAccessException {
		getSqlMapClientTemplate().update("admin_schedule.changeOrder", vo);
	}
}
