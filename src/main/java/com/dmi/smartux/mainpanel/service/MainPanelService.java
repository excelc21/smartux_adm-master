package com.dmi.smartux.mainpanel.service;

import java.util.List;

import com.dmi.smartux.mainpanel.vo.AlbumInfoVO;
import com.dmi.smartux.mainpanel.vo.MainPanelInfoVO;

public interface MainPanelService {
	
	/**
	 * Main Panel 버전 정보를 조회
	 * @param sa_id				가입번호
	 * @param stb_mac			맥주소
	 * @param app_type			어플타입(UX : 셋탑, SMA : 스마트폰)
	 * @param callByScheduler	단말에서 호출 했을 경우엔 N, 스케쥴러에서 호출했을땐 Y, 관리자툴에서 조회했을땐 A가 들어간다
	 * @return					Main Panel 버전 정보
	 * @throws Exception
	 */
	public String getMainPanelVersionInfo(String sa_id, String stb_mac, String app_type, String callByScheduler) throws Exception;
	
	/**
	 * Main Panel 연동 정보를 조회
	 * @param sa_id				가입번호
	 * @param stb_mac			맥주소
	 * @param app_type			어플타입(UX : 셋탑, SMA : 스마트폰)
	 * @param pannel_id		특정 패널의 값을 읽을려고 할때 사용하는 패널 ID(ex:CP01), 이 변수명은 단말에 알려주지 말것(연동정의서엔 특정 패널 조회가 없다)
	 * @param callByScheduler	단말에서 호출 했을 경우엔 N, 스케쥴러에서 호출했을땐 Y, 관리자툴에서 조회했을땐 A가 들어간다
	 * @return
	 * @throws Exception
	 */
	public List<MainPanelInfoVO> getMainPanelInterlockingInfo(String sa_id, String stb_mac, String app_type, String panel_id, String callByScheduler) throws Exception;
	
	/**
	 * I20 편성 앨범의 갯수를 조회
	 * @param sa_id				가입번호
	 * @param stb_mac			맥주소
	 * @param app_type			어플타입
	 * @param start_num			검색 시작 인덱스(-1이면 req_count 값을 무시하고 전체를 검색한다)
	 * @param req_count			검색 레코드 갯수
	 * @param category_id		검색하고자 하는 앨범이 있는 category_id(2개 이상일 경우 ||로 결합되어 있다)
	 * @param callByScheduler	단말에서 호출 했을 경우엔 N, 스케쥴러에서 호출했을땐 Y, 관리자툴에서 조회했을땐 A가 들어간다
	 * @return					I20 편성 앨범 갯수
	 * @throws Exception
	 */
	public int getI20AlbumListCount(String sa_id, String stb_mac, String app_type, String category_id, String start_num, String req_count, String callByScheduler) throws Exception;
	
	/**
	 * I20 편성 앻범 조회
	 * @param sa_id				가입번호
	 * @param stb_mac			맥주소
	 * @param app_type			어플타입
	 * @param start_num			검색 시작 인덱스(-1이면 req_count 값을 무시하고 전체를 검색한다)
	 * @param req_count			검색 레코드 갯수
	 * @param category_id		검색하고자 하는 앨범이 있는 category_id(2개 이상일 경우 ||로 결합되어 있다)
	 * @param callByScheduler	단말에서 호출 했을 경우엔 N, 스케쥴러에서 호출했을땐 Y, 관리자툴에서 조회했을땐 A가 들어간다
	 * @return					I20 편성 앨범
	 * @throws Exception
	 */
	public List<AlbumInfoVO> getI20AlbumList(String sa_id, String stb_mac, String app_type, String category_id, String start_num, String req_count, int fh_gbn, String callByScheduler) throws Exception;
}
