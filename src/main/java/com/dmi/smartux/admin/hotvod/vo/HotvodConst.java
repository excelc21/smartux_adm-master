package com.dmi.smartux.admin.hotvod.vo;

public interface HotvodConst {
	
	/**
	 * 화제동영상 뱃지 리스트
	 * (뒤에서 뱃지 추가)
	 * 신규뱃지는 최상위 비트
	 */
	public static String DEFAULT_HOTVOD_BADGE_LIST = "신규|인기|연령1|연령2|연령3|추천1|추천2|추천3|기타1|기타2|기타3";
	
	/**
	 * 화제동영상 컨텐츠 서비스타입
	 * (서비스코드^서비스명) 
	 * (뒤에서 신규 서비스코드 추가)
	 * 신규 서비스코드는 최상위 비트
	 */
	public static String DEFAULT_HOTVOD_SERVICE_LIST = "I^IPTV";
	
	/**
	 * 화제동영상 통합검색 메타파일 서비스 타입
	 * (서비스코드^메타코드 [I^IPTV])
	 */
	public static String DEFAULT_HOTVOD_METAFILE_LIST = "";
	
	/**
	 * 화제동영상 필터링 사이트 구분코드
	 * (서비스명칭) 
	 */
	public static String DEFAULT_HOTVOD_FILTERING_SVC_LIST = "IPTV";
	
	/**
	 *	컨텐츠 타입
	 */
	public enum CONTENT_TYPE {
		/**
		 * 카테고리 (C)
		 */
		CATEGORY("C"),
		/**
		 * 컨텐츠 (V)
		 */
		CONTENTS("V"),
		/**
		 * 하이라이트(H) 
		 */
		HIGHLIGHT("H"),
		/**
		 * 아웃링크 (O)
		 */
		OUTLINK("O"),
		/**
		 * 이미지/텍스트(S)
		 */
		IMG_TEXT("S"),
		/**
		 * IMCS VOD(M)
		 */
		IMCS_VOD("M"),
		/**
		 * 콘서트(B) 
		 */
		CONCERT("B"),
		/**
		 * 방송(A)
		 */
		BROADCAST("A")
		;
		public final String CODE;
		
		private CONTENT_TYPE(String code) {
			this.CODE = code;
		}
		
		public boolean equals(String code) {
			return this.CODE.equals(code) ? true : false;
		}
	}
}
