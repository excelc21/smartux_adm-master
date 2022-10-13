package com.dmi.smartux.admin.schedule.service;

import java.util.List;

import com.dmi.smartux.admin.schedule.vo.CategoryAlbumVO;
import com.dmi.smartux.admin.schedule.vo.ScheduleDetailVO;
import com.dmi.smartux.admin.schedule.vo.ScheduleVO;


public interface ScheduleService {

	/**
	 * 자체편성 목록 조회
	 * @return				조회된 자체편성 목록
	 * @throws Exception
	 */
	public List<ScheduleVO> getScheduleList(String category_gb) throws Exception;
	
	/**
	 * 자체편성 상세 조회
	 * @param schedule_code		상세조회하고자 하는 자체편성의 schedule_code
	 * @return				상세조회된 자체편성 정보
	 * @throws Exception
	 */
	public ScheduleVO viewSchedule(String schedule_code) throws Exception;
	
	/**
	 * 자체편성 수정
	 * @param schedule_code		수정하고자 하는 자체편성의 schedule_code
	 * @param schedule_name		수정하고자 하는 자체편성의 자체편성 이름
	 * @param album_id			수정하고자 하는 자체편성에 새로이 편성된 앨범코드 값들의 배열
	 * @param category_id		수정하고자 하는 자체편성에 새로이 편성된 카테고리 코드 값들의 배열
	 * @param update_id			관리자 사이트에 로그인 한 사람의 로그인 id
	 * @throws Exception
	 */
	public void updateSchedule(String schedule_code, String schedule_name, String [] album_id, String [] category_id, String update_id, String category_gb, String test_id, String variation_id) throws Exception;
	
	
	/**
	 * 자체편성 삭제
	 * @param schedule_codes		삭제하고자 하는 자체편성의 schedule_code 값들의 배열
	 * @throws Exception
	 */
	public void deleteSchedule(String [] schedule_codes) throws Exception;
	
	/**
	 * 자체편성의 서브 정보 조회
	 * @param schedule_code		서브 정보를 조회하고자 하는 자체편성의 schedule_code
	 * @return					조회된 자체편성의 서브 정보
	 * @throws Exception
	 */
	public List<ScheduleDetailVO> getScheduleDetailList(String schedule_code, String category_gb) throws Exception;
	
	/**
	 * 자체편성 등록
	 * @param schedule_name		등록하고자 하는 자체편성 이름
	 * @param album_id			등록하고자 하는 자체편성에 속한 앨범 코드값들의 배열
	 * @param category_id		등록하고자 하는 자체편성에 속한 카테고리 코드 값들의 배열
	 * @param create_id			관리자 사이트에 로그인 한 사람의 로그인 id
	 * @throws Exception
	 */
	public void insertSchedule(String schedule_name, String[] album_id, String [] category_id, String create_id, String category_gb, String test_id, String variation_id) throws Exception;
	
	/**
	 * 카테고리 선택 팝업화면에서 카테고리 선택시 선택된 카테고리의 하위 카테고리와 앨범들을 조회한다
	 * @param category_id	카테고리 id
	 * @return				하위 카테고리와 앨범들의 목록 데이터가 있는 json 문자열
	 * @throws Exception
	 */
	
	/**
	 * 선택된 카테고리에 대한 하위 카테고리와 앨범 정보 조회
	 * @param category_id		카테고리 id
	 * @return					하위 카테고리와 앨범 정보
	 * @throws Exception
	 */
	public List<CategoryAlbumVO> getCategoryAlbumList(String category_id, String type, String series_yn) throws Exception;
	
	
	/**
	 * 구분자로 결합된 자체편성의 schedule_code값들이 지면 상용과 지면 임시에서 사용되고 있는지를 조사하여 사용되고 있을 경우 사용되고 있는 자체편성의 정보를 조회한다
	 * @param schedule_codes	구분자로 결합된 자체편성의 schedule_code값
	 * @return					자체편성 정보
	 * @throws Exception
	 */
	public List<ScheduleVO> selectUseSchedule(String schedule_codes) throws Exception;
	
	/**
	 * 선택된 카테고리에 대한 하위 카테고리와 앨범 정보 조회
	 * @param category_id		카테고리 id
	 * @return					하위 카테고리와 앨범 정보
	 * @throws Exception
	 */
	public List<CategoryAlbumVO> getAlbumListByCategoryIdAlbumId(String category_id,String album_id, String type) throws Exception;
	
	
	/**
	 * 메인패널 편성 시 이미지 등록 및 수정 16.12.09
	 * @param map
	 * @throws Exception
	 */
	public void mergeContentsImg(String albumId, String widthImg, String heightImg,String createId) throws Exception;
	
	/**
	 * 순서 변경 (orderList의 순서대로 순서를 변경)
	 *
	 * @param optionArray regNumber List
	 * @throws Exception
	 */
	public void changeOrder(String[] optionArray, int schedule_code) throws Exception;
}
