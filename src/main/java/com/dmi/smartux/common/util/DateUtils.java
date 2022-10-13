package com.dmi.smartux.common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.dmi.smartux.common.vo.CompareValue;

/**
 * @author jch82@naver.com
 */
public class DateUtils {
	
	/**
	 * The enum Date pattern.
	 */
	public enum DatePattern {
		/**
		 * Year date pattern.
		 */
		Year("yyyy"),
		/**
		 * Month date pattern.
		 */
		Month("MM"),
		/**
		 * Day date pattern.
		 */
		Day("dd"),
		/**
		 * Yyyy m mdd 1 date pattern.
		 */
		yyyyMMdd_1("yyyyMMdd"),
		/**
		 * Yyyy m mdd 2 date pattern.
		 */
		yyyyMMdd_2("yyyy-MM-dd"),
		/**
		 * Yyyy m mdd h hmmss 1 date pattern.
		 */
		yyyyMMddHHmmss_1("yyyyMMddHHmmss"),
		/**
		 * The Yyyy m mdd h hmmss 2.
		 */
		yyyyMMddHHmmss_2("yyyy-MM-dd HH:mm:ss");

		private String pattern;

		private DatePattern(String pattern) {
			this.pattern = pattern;
		}

		/**
		 * Gets pattern.
		 *
		 * @return the pattern
		 */
		public String getPattern() {
			return this.pattern;
		}
	}
	
	/**
	 * date1 과 date2 를 비교한 결과를 돌려준다.
	 * 
	 * <pre>
	 * date1 을 기준으로 비교가 이루어 지며,
	 * date1 이 date2 보다 클 경우  CompareValue.High
	 * date1 이 date2 와 같을 경우 CompareValue.Equal
	 * date1 이 date2 보다 작을 경우 CompareValue.Low 
	 * 을 돌려준다.
	 * </pre>
	 * 
	 * @param date1 비교할 날짜1
	 * @param date2 비교할 날짜2
	 * @return date1 이 date2 보다 클 경우 CompareValue.High<br/>
	 *         date1 이 date2 와 같을 경우 CompareValue.Equal<br/>
	 *         date1 이 date2 보다 작을 경우 CompareValue.Low<br/>
	 */
	@SuppressWarnings( "deprecation" )
	public static CompareValue compare ( Date date1, Date date2 ) {
		CompareValue returnValue;
		if ( date1.getYear ( ) > date2.getYear ( ) ) {
			returnValue = CompareValue.High;
		} else if ( date1.getYear ( ) < date2.getYear ( ) ) {
			returnValue = CompareValue.Low;
		} else {
			if ( date1.getMonth ( ) > date2.getMonth ( ) ) {
				returnValue = CompareValue.High;
			} else if ( date1.getMonth ( ) < date2.getMonth ( ) ) {
				returnValue = CompareValue.Low;
			} else {
				if ( date1.getDate ( ) > date2.getDate ( ) ) {
					returnValue = CompareValue.High;
				} else if ( date1.getDate ( ) < date2.getDate ( ) ) {
					returnValue = CompareValue.Low;
				} else {
					returnValue = CompareValue.Equal;
				}
			}
		}
		return returnValue;
	}

	/**
	 * 
	 * @param date1 YYYYMMDD(오늘 기준)
	 * @param data2 Date(YYYYMMDD 변환 후 체크)
	 * @return
	 */
	public static boolean DateStr ( String date1, Date date2 ) {
		boolean returnValue = false;

		DateFormat sdFormat = new SimpleDateFormat ( "yyyyMMdd" );
		String tempDate = sdFormat.format ( date2 );

		if ( Integer.parseInt ( tempDate ) >= Integer.parseInt ( date1 ) ) {
			returnValue = true;
		}

		return returnValue;
	}

	/**
	 * 오늘날짜와 입력한 날짜를 비교한다.
	 * 
	 * @param cDate YYYYMMDD
	 * @return 오늘과 비교 => 입력한날짜가 앞이면 : true , 입력한날짜랑 같으면 : false , 입력한 날짜보다 크면 : false
	 * @throws Exception
	 */
	public static boolean ComparisonToday ( String cDate ) throws Exception {
		SimpleDateFormat date = new SimpleDateFormat ( "yyyyMMdd HH:mm:ss" );

		Date product_end = date.parse ( cDate + " 23:59:59" );
		Date current = new Date ( );

		return current.after ( product_end );
	}

	/**
	 * 두 SystemDate간의 차이를 초단위로 리턴
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static Long dateCompare ( Long date1, Long date2 ) {

		Long diffTime = date1 - date2;

		Long result = diffTime / 1000;

		return result;
	}

	/**
	 * Convert to date date.
	 *
	 * @param date    the date
	 * @param pattern the pattern
	 * @return the date
	 * @throws exception
	 */
	public static Date convertToDate(String date, DatePattern pattern) throws Exception {
		return convertToDate(date, pattern.getPattern());
	}

	/**
	 * Convert to date date.
	 *
	 * @param date    the date
	 * @param pattern the pattern
	 * @return the date
	 * @throws MimsCommonException the mims common exception
	 */
	public static Date convertToDate(String date, String pattern) throws Exception {
		try {
			if(StringUtils.isEmpty(date)) {
				return new Date();
			}
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			return sdf.parse(date);
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * Get Date Field
	 *
	 * @param date
	 * @return
	 */
	public static int getDateField(Date date, int type) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(type);
	}
	
	/**
	 * Convert to string string.
	 *
	 * @param date    the date
	 * @param pattern the pattern
	 * @return the string
	 * @throws MimsCommonException the mims common exception
	 */
	public static String convertToString(Date date, DatePattern pattern) throws Exception {
		return convertToString(date, pattern.getPattern());
	}
	
	/**
	 * Convert to string string.
	 *
	 * @param date    the date
	 * @param pattern the pattern
	 * @return the string
	 * @throws MimsCommonException the mims common exception
	 */
	public static String convertToString(Date date, String pattern) throws Exception {
		try {
			if(date == null){
				date = new Date();
			}
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			return sdf.format(date);
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * Add date date.
	 *
	 * @param field  the field
	 * @param amount the amount
	 * @return the date
	 */
	public static Date addDate(int field, int amount) {
		return addDate(new Date(), field, amount);
	}

	/**
	 * Add date date.
	 *
	 * @param date   the date
	 * @param field  the field
	 * @param amount the amount
	 * @return the date
	 */
	public static Date addDate(Date date, int field, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(field, amount);
		return cal.getTime();
	}
}
