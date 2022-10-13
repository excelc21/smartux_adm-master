package com.dmi.smartux.noti.util;

import com.dmi.smartux.common.vo.CompareValue;

public class NotiUtils {

	/**
	 * <pre>
	 * 캐쉬의 버전과 단말에서 전달 된 버전을 비교한다.
	 * 버전의 형태는 아래와 같다.
	 * ex) 2012090901001 년,월,일,시,일련번호3자리
	 * </pre>
	 * 
	 * @param currentVersion 단말에서 전달 된 버전
	 * @param cacheVersion 캐쉬에서의 버전
	 * @return [CompareValue.Equal : 같음], <br/>
	 *         [CompareValue.NonEqual : 다름]
	 */
	public static CompareValue compareVersion ( String currentVersion, String cacheVersion ) throws NullPointerException {
		CompareValue result;
		if ( currentVersion.equals ( cacheVersion ) ) {
			result = CompareValue.Equal;
		} else {
			result = CompareValue.NonEqual;
		}

		return result;
	}

	// - 캐쉬보다 높은 버전이 들어오지 않기에 사용하지 않음
	// /**
	// * <pre>
	// * 캐쉬의 버전과 단말에서 전달 된 버전을 비교한다.
	// * 버전의 형태는 아래와 같다.
	// * ex) 2012090901001 년,월,일,시,일련번호3자리
	// * </pre>
	// * @param currentVersion 단말에서 전달 된 버전
	// * @param cacheVersion 캐쉬에서의 버전
	// * @return
	// * [CompareValue.Low : 단말 버전이 높음], <br/>
	// * [CompareValue.Equal : 같음], <br/>
	// * [CompareValue.High : 캐쉬 버전이 높음]
	// */
	// public static CompareValue compareVersion(String currentVersion, String cacheVersion) throws
	// NumberFormatException {
	// long convertCurrentVersion = Long.parseLong(currentVersion);
	// long convertCacheVersion = Long.parseLong(cacheVersion);
	//
	// CompareValue result;
	// if (convertCurrentVersion < convertCacheVersion) {
	// result = CompareValue.High;
	// } else if (convertCurrentVersion > convertCacheVersion) {
	// result = CompareValue.Low;
	// } else {
	// result = CompareValue.Equal;
	// }
	// return result;
	// }
}
