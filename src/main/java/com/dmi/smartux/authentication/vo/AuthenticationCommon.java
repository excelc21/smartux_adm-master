package com.dmi.smartux.authentication.vo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.util.StringUtil;

public class AuthenticationCommon {
	// public static final String SYSTEM_CODE = "SUXM"; //OPENAPI 또는 통합DB에 관리 되고 있는 테이블의 시스템 구분 키값
	public static final String GBN = "^";
	public static final String SPLIT_GBN = "\\^"; // java의 split 메소드에서 사용할때 열 분리자

	// MAP KEY (KEY_TYPE^SERVICE_ID^API_ID) VALUE (AuthenticationVO)
	public static Map<String, AuthenticationVO> AUTH_MAP = new HashMap<String, AuthenticationVO> ( );

	/**
	 * 인증 허용 체크 메서드
	 * 
	 * @param access_key
	 * @param cp_id
	 * @return SUCCESS : 성공 EXDATE = 만료기간지남 ACCESS_KEY = 키값 없음 CP_ID = 키값 없음 URL = 허용된 URL 아님 FAIL = 허용된 키값 존재 하지 않음
	 */
	public static String isAuthenticationMap ( String access_key, String cp_id, String method, String url ) {
		String result = "SUCCESS";
		AuthenticationVO checkData = null;

		StringUtil SU = new StringUtil ( );

		// =========로그==========
		Set s = AuthenticationCommon.AUTH_MAP.entrySet ( );
		Iterator it = s.iterator ( );
		while ( it.hasNext ( ) ) {
			Map.Entry m = (Map.Entry) it.next ( );

			String key = (String) m.getKey ( );
			AuthenticationVO value = (AuthenticationVO) m.getValue ( );

			System.out.println ( "AuthenticationCommon.AUTH_MAP Key[" + key + "] api_id[" + value.getApi_id ( ) + "] key_type[" + value.getKey_type ( ) + "] use[" + value.getUse ( ) + "] date[" + value.getService_life ( ) + "]" );
		}
		// =========로그==========

		try {
			checkData = AUTH_MAP.get ( access_key );
			// AccessKey 체크
			if ( checkData != null ) {
				if ( GlobalCom.isNull ( checkData.getUse ( ) ).equals ( "Y" ) ) {
					// 만료일이 없으면 무제한이고 만료일이 존재하고 만료일이 안지났으면..Y
					// 만료일 체크
					if ( ( checkData.getService_life ( ) == null || checkData.getService_life ( ).equals ( "" ) ) || SU.getDateComparison ( "yy.MM.dd", checkData.getService_life ( ), SU.getCurDttm ( "yy.MM.dd" ) ) ) {
						// CP_ID 체크
						if ( !checkData.getCp_id ( ).equals ( cp_id ) ) {
							result = "CP_ID";
						} else {
							// URL 체크
							method = GlobalCom.isNull ( method );
							url = GlobalCom.isNull ( url );
							String dataFullUrl = GlobalCom.isNull ( checkData.getApi_id ( ) );
							if ( method.equals ( "" ) || url.equals ( "" ) || dataFullUrl.equals ( "" ) ) {
								result = "URL";
							} else {
								String[] dataArr = dataFullUrl.split ( "]" );
								String dataMethod = dataArr[0].replace ( "[", "" );
								if ( dataMethod.equals ( method ) ) {
									String tempDataUrl = dataArr[1];
									tempDataUrl = tempDataUrl.replace ( "http://", "" );
									tempDataUrl = tempDataUrl.replace ( "https://", "" );
									String[] dataUrlArr = tempDataUrl.split ( "/" );
									String dataUrl = tempDataUrl.substring ( dataUrlArr[0].length ( ), tempDataUrl.length ( ) );

									if ( !dataUrl.equals ( url ) ) {
										result = "URL";
									}
								} else {
									result = "URL";
								}
							}
						}
					} else {
						result = "EXDATE";
					}
				}else{//사용여부가 N이면 실패가 맞는 듯..
					result = "FAIL";
				}
			} else {
				// AccessKey 미존재
				result = "ACCESS_KEY";
			}
		} catch ( Exception e ) {
			result = "FAIL";
		}

		return result;
	}

	/**
	 * 위의 isAuthenticationMap는 /v1/aaa/list==/v1/aaa/list고 /v1/aaa!=/v1/aaa/list였지만 이건 /v1/aaa==/v1/aaa/list이 된다.
	 * 
	 * @param access_key
	 * @param cp_id
	 * @param method
	 * @param url
	 * @return SUCCESS : 성공 EXDATE = 만료기간지남 ACCESS_KEY = 키값 없음 CP_ID = 키값 없음 URL = 허용된 URL 아님 FAIL = 허용된 키값 존재 하지 않음
	 */
	public static String isAuthenticationChek ( String access_key, String cp_id, String method, String url ) {
		String result = "SUCCESS";
		AuthenticationVO checkData = null;

		StringUtil SU = new StringUtil ( );

		// =========로그==========
		// Set s=AuthenticationCommon.AUTH_MAP.entrySet();
		// Iterator it=s.iterator();
		// while(it.hasNext()){
		// Map.Entry m =(Map.Entry)it.next();
		//
		// String key=(String)m.getKey();
		// AuthenticationVO value=(AuthenticationVO)m.getValue();
		// }
		// =========로그==========

		try {
			checkData = AUTH_MAP.get ( access_key );
			// AccessKey 체크
			if ( checkData != null ) {
				if ( GlobalCom.isNull ( checkData.getUse ( ) ).equals ( "Y" ) ) {
					// 만료일이 없으면 무제한이고 만료일이 존재하고 만료일이 안지났으면..Y
					// 만료일 체크
					if ( ( checkData.getService_life ( ) == null || checkData.getService_life ( ).equals ( "" ) ) || SU.getDateComparison ( "yy.MM.dd", checkData.getService_life ( ), SU.getCurDttm ( "yy.MM.dd" ) ) ) {
						// CP_ID 체크
						if ( !checkData.getCp_id ( ).equals ( cp_id ) ) {
							result = "CP_ID";
						} else {
							// URL 체크
							method = GlobalCom.isNull ( method );
							url = GlobalCom.isNull ( url );

							String auth_uri = GlobalCom.isNull ( checkData.getUri ( ) );
							String auth_method = GlobalCom.isNull ( checkData.getMethod ( ) );
							if ( method.equals ( "" ) || url.equals ( "" ) || auth_uri.equals ( "" ) ) {
								result = "URL";
							} else {
								if ( method.equalsIgnoreCase ( auth_method ) ) {
									if ( !url.startsWith ( auth_uri ) ) {
										result = "URL";
									}
								} else {
									result = "URL";
								}
							}
						}
					} else {
						result = "EXDATE";
					}
				}else{//사용여부가 N이면 실패가 맞는 듯..
					result = "FAIL";
				}
			} else {
				// AccessKey 미존재
				result = "ACCESS_KEY";
			}
		} catch ( Exception e ) {
			result = "FAIL";
		}

		return result;
	}

}
