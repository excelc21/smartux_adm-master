package com.dmi.smartux.common.util;

import java.util.Date;

/**
 * 시간 관련 유틸 클래스
 *
 * @author dongho
 */
public class DateUtil {
	public static long getTimeDifference(Date checkDate) {
		return System.currentTimeMillis() - checkDate.getTime();
	}
}
