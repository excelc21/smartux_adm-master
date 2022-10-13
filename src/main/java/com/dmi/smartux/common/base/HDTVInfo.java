package com.dmi.smartux.common.base;

/**
 * HDTV 의 전체적인 전역 변수를 관리한다.
 * 
 * @author jch82@naver.com
 */
public interface HDTVInfo {

	/**
	 * 기존 로직에서는 callByScheduler 를 사용해야지 하며, 단말 요청 시 사용하기 위한 상수 값이다. 스케쥴로 구동시 한번만 캐싱하고 인터페이스 호출 시 무조건 설정한 interval이 지나면
	 * 캐싱하도록 해놧구만...
	 */
	// public String CallByScheduler = "N";
	public String CallByScheduler = "Y";

	public String AllTermModel = "ALL";

	/**
	 * 특정 변수의 의미<br/>
	 * 특정 변수를 기존과 다른 의미로 사용하기 위한 값
	 * 
	 * @author jch82@naver.com
	 */
	public interface Meaning {
		/**
		 * 게시판의 전체 개수만을 구한다. ( 이후 값은 생략 )
		 */
		public int TotalNumber = 0;
		/**
		 * 게시판의 전체 레코드를 구한다.
		 */
		public int TotalRecord = -1;

		/**
		 * 이 수보다 작은 값은 의미가 없다.
		 */
		public int NoMeaning = -2;
	}

}
