package com.dmi.smartux.smartepg.service;

import java.util.List;

import com.dmi.smartux.smartepg.vo.RealRatingInfoThemeVO;
import com.dmi.smartux.smartepg.vo.RealRatingInfoVO;
import com.dmi.smartux.smartepg.vo.ThemeInfoVO;

public interface SmartEPGService {

	/**
	 * 테마별 실시간 시청률을 조회하기 위해 사용되는 테마 정보의 갯수를 조회하는 Service 인터페이스
	 * @param sa_id				가입번호
	 * @param stb_mac			맥주소
	 * @param app_type			어플타입
	 * @param callByScheduler	단말이 호출한건지, 스케줄러가 호출했는지의 여부(Y이면 스케듈러, A면 관리자 화면)
	 * @return					검색된 레코드 총 갯수
	 * @throws Exception
	 */
	int getThemeInfoCount(String sa_id, String stb_mac, String app_type, String callByScheduler) throws Exception;
	
	/**
	 * 테마별 실시간 시청률을 조회하기 위해 사용되는 테마 정보를 조회하는 Service 인터페이스
	 * @param sa_id				가입번호
	 * @param stb_mac			맥주소
	 * @param app_type			어플타입
	 * @param start_num			검색 시작 인덱스(-1이면 req_count 값을 무시하고 전체를 검색한다)
	 * @param req_count			검색 레코드 갯수
	 * @param callByScheduler	단말이 호출한건지, 스케줄러가 호출했는지의 여부(Y이면 스케듈러, A면 관리자 화면)
	 * @return					테마 정보가 들어있는 ThemeInfoVO 객체가 들어있는 List 객체
	 * @throws Exception
	 */
	List<ThemeInfoVO> getThemeInfo(String sa_id, String stb_mac, String app_type, String start_num, String req_count, String callByScheduler) throws Exception;
	
	/**
	 * 테마별 실시간 시청률 정보의 갯수를 조회하는 Service 인터페이스(사용안함)
	 * @param sa_id			가입번호
	 * @param stb_mac		맥주소
	 * @param app_type		어플타입
	 * @param theme_code	검색하고자 하는 테마별 시청률의 테마 코드
	 * @return				검색된 레코드 총 갯수
	 * @throws Exception
	 */
	int getRealRatingByThemeCount(String sa_id, String stb_mac, String app_type, String theme_code, String callByScheduler) throws Exception;
	
	/**
	 * 테마별 실시간 시청률 정보를 조회하는 Service 인터페이스(사용안함)
	 * @param sa_id			가입번호
	 * @param stb_mac		맥주소
	 * @param app_type		어플타입
	 * @param start_num		검색 시작 인덱스(-1이면 req_count 값을 무시하고 전체를 검색한다)
	 * @param req_count		검색 레코드 갯수
	 * @param theme_code	검색하고자 하는 테마별 시청률의 테마 코드
	 * @return				실시간 시청률 정보가 들어있는 RealRatingInfoVO 객체가 들어있는 List 객체
	 * @throws Exception
	 */
	List<RealRatingInfoThemeVO> getRealRatingByTheme(String sa_id, String stb_mac, String app_type, String start_num, String req_count, String theme_code, String callByScheduler) throws Exception;
	
	
	/**
	 * 실시간 시청률 정보의 갯수를 조회하는 Service 인터페이스
	 * @param sa_id			가입번호
	 * @param stb_mac		맥주소
	 * @param app_type		어플타입
	 * @param localareacode 구분자로 결합된 지역코드
	 * @param callByScheduler	단말이 호출한건지, 스케줄러가 호출했는지의 여부(Y이면 스케듈러, A면 관리자 화면)
	 * @return				검색된 레코드 총 갯수
	 * @throws Exception
	 */
	int getRealRatingCount(String sa_id, String stb_mac, String app_type, String localareacode, String callByScheduler) throws Exception;
	
	/**
	 * 실시간 시청률 정보를 조회하는 Service 인터페이스
	 * @param sa_id			가입번호
	 * @param stb_mac		맥주소
	 * @param app_type		어플타입
	 * @param start_num		검색 시작 인덱스(-1이면 req_count 값을 무시하고 전체를 검색한다)
	 * @param req_count		검색 레코드 갯수
	 * @param localareacode 구분자로 결합된 지역코드
	 * @param callByScheduler	단말이 호출한건지, 스케줄러가 호출했는지의 여부(Y이면 스케듈러, A면 관리자 화면)
	 * @return				실시간 시청률 정보가 들어있는 RealRatingInfoVO 객체가 들어있는 List 객체
	 * @throws Exception
	 */
	List<RealRatingInfoVO> getRealRating(String sa_id, String stb_mac, String app_type, String start_num, String req_count, String localareacode, String callByScheduler) throws Exception;
}
