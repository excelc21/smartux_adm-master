package com.dmi.smartux.smartstart.service;

import java.util.List;

import com.dmi.smartux.admin.rank.vo.EcrmRankVO;
import com.dmi.smartux.smartstart.vo.GenreVodBestListVO;
import com.dmi.smartux.smartstart.vo.SUXMAlbumListVO;;

public interface SUXMAlbumListService {

	/**
	 * SUXMAlbumList 조회하기 위해 사용되는 정보의 갯수를 조회하는 Service 인터페이스
	 * @param sa_id			가입번호
	 * @param stb_mac		맥주소
	 * @param app_type		어플타입
	 * @param schedule_code	자체편성코드
	 * @param callByScheduler	스케듈러에서 호출했을 경우 여기 값에 Y가 들어온다. 스케줄러에서 호출한 경우 validation 로직을 타지 않고 바로 서비스를 호출한다
	 * @return				검색된 레코드 총 갯수
	 * @throws Exception
	 */
	int getSUXMAlbumListCount(String sa_id, String stb_mac,	String app_type, String schedule_code , String callByScheduler) throws Exception;
	
	/**
	 * SUXMAlbumList 조회하기 위해 사용되는 정보를 조회하는 Service 인터페이스
	 * @param sa_id			가입번호
	 * @param stb_mac		맥주소
	 * @param app_type		어플타입
	 * @param start_num		검색 시작 인덱스(-1이면 req_count 값을 무시하고 전체를 검색한다)
	 * @param req_count		검색 레코드 갯수
	 * @param schedule_code	자체편성코드
	 * @param callByScheduler	스케듈러에서 호출했을 경우 여기 값에 Y가 들어온다. 스케줄러에서 호출한 경우 validation 로직을 타지 않고 바로 서비스를 호출한다
	 * @return				SmartStartItemList 정보가 들어있는 SmartStartItemListVO 객체가 들어있는 List 객체
	 * @throws Exception
	 */
	List<SUXMAlbumListVO> getSUXMAlbumList(String sa_id, String stb_mac, String app_type, String start_num, String req_count, String schedule_code, int fh_gbn , String callByScheduler) throws Exception;

	/**
	 * SUXMAlbumList 조회하기 위해 사용되는 사전정보를 체크하는 Service 인터페이스
	 * @return				EcrmRank 정보가 들어있는 EcrmRankVO 클래스가 들어있는 List 객체
	 * @throws Exception
	 */
	List<EcrmRankVO> getCheckSCHEDULEList() throws Exception;
	
	/**
	 * SUXMAlbumList 조회하기 위해 사용되는 사전코드정보를 체크하는 Service 인터페이스
	 * @param schedule_code	자체편성코드
	 * @return				EcrmRank 정보가 들어있는 EcrmRankVO 클래스가 들어있는 List 객체
	 * @throws Exception
	 */
	List<EcrmRankVO> getCheckScheduleCodelist(String schedule_code) throws Exception;
}

