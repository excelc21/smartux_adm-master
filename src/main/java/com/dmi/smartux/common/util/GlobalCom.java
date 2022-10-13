package com.dmi.smartux.common.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.Character.UnicodeBlock;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.simple.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import com.dmi.smartux.common.exception.SmartUXException;
import com.dmi.smartux.common.property.SmartUXProperties;

/**
 * 공통 유틸 클래스
 */
public class GlobalCom {

	private final Log logger = LogFactory.getLog ( this.getClass ( ) );

	public static final String rowsep = "\f"; // 행 분리자
	public static final String colsep = "!^"; // 열 분리자
	public static final String arrsep = "\b"; // 배열 분리자
	public static final String rssep = "!@"; // 레코드셋 분리자

	public static final String splitrowsep = "\\f"; // java의 split 메소드에서 사용할때 행 분리자
	public static final String splitcolsep = "\\!\\^"; // java의 split 메소드에서 사용할때 열 분리자
	public static final String splitarrsep = "\\b"; // java의 split 메소드에서 사용할때 배열 분리자
	public static final String splitrssep = "\\!\\@"; // java의 split 메소드에서 사용할때 레코드셋 분리자

	public static final String SMARTUX_EXTERNAL_PROPERTY_FILEPATH_KEY = "filepath.smartux.common";
	
	public static String isNullCheck ( String str ) {
		if ( ( str == null ) || ( str.trim ( ).equals ( "" ) ) || ( str.trim ( ).equalsIgnoreCase ( "null" ) ) || ( str.trim ( ).length ( ) == 0 ) || ( str.equalsIgnoreCase ( "undefined" ) ) ) return "";
		else return str.trim ( );
	}

	public static String isNull ( String str ) {
		if ( ( str == null ) || ( str.trim ( ).equals ( "" ) ) || ( str.trim ( ).equalsIgnoreCase ( "null" ) ) || ( str.trim ( ).length ( ) == 0 ) || ( str.equalsIgnoreCase ( "undefined" ) ) ) return "";
		else return str.trim ( );
	}

	public static String isNull ( String str, String str2 ) {
		if ( ( str == null ) || ( str.trim ( ).equals ( "" ) ) || ( str.trim ( ).equalsIgnoreCase ( "null" ) ) || ( str.trim ( ).length ( ) == 0 ) || ( str.equalsIgnoreCase ( "undefined" ) ) ) return str2;
		else return str.trim ( );
	}

	public static String isNullNumber ( String str ) {
		if ( ( str == null ) || ( str.trim ( ).equals ( "" ) ) || ( str.trim ( ).equalsIgnoreCase ( "null" ) ) || ( str.trim ( ).length ( ) == 0 ) || ( str.equalsIgnoreCase ( "undefined" ) ) ) return "1";
		else return str.trim ( );
	}

	public static int isNumber ( int num ) {
		if ( num < 0 || num == 0 ) {
			return 1;
		} else {
			return num;
		}
	}

	public static int isNumber ( int num, int _num ) {
		if ( num < 0 || num == 0 ) {
			return _num;
		} else {
			return num;
		}
	}

	public static int isNewButtonCheck ( String date ) {
		try {
			String today = getTodayFormat ( );
			date = replace ( date, "-", "" );
			int check = Integer.parseInt ( today ) - Integer.parseInt ( date );

			if ( check >= 0 || check <= 7 ) {
				return 1;
			}
			return 0;
		} catch ( Exception e ) {
			return 0;
		}

	}

	public static String getKenString ( String str, int len ) throws UnsupportedEncodingException {

		String fStr = str;
		int sLen = str.length ( );

		if ( sLen > len ) {
			fStr = str.substring ( 0, len ) + "...";
		}

		return fStr;
	}

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ Function + getTodayFormat Parameter + Description +
	 * 오늘 날짜를 반환한다. (yyyyMMdd) ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	public static String getTodayFormat ( ) {
		Date dt = new Date ( );
		SimpleDateFormat sdf = new SimpleDateFormat ( "yyyyMMdd" );
		return sdf.format ( dt );
	}
	
	public static String getCustomTodayFormat(String fmt) {
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(fmt);
		return sdf.format(dt);
	}

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ Function + getTodayFormat Parameter + Description +
	 * 오늘 날짜를 반환한다. (yyyy-MM-dd) ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	public static String getTodayFormat2 ( ) {
		Date dt = new Date ( );
		SimpleDateFormat sdf = new SimpleDateFormat ( "yyyy-MM-dd" );
		return sdf.format ( dt );
	}

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ Function + getTodayFormat Parameter + Description +
	 * 오늘 날짜를 반환한다. (yyyy-MM-dd-hh-mm-ss) ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	public static String getTodayFormat3 ( ) {
		Date dt = new Date ( );
		SimpleDateFormat sdf = new SimpleDateFormat ( "yyyy-MM-dd-hh-mm-ss" );
		return sdf.format ( dt );
	}

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ Function + getTodayFormat Parameter + Description +
	 * 오늘 날짜를 반환한다. (yyyyMMddhhmmss) ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	public static String getTodayFormat4 ( ) {
		Date dt = new Date ( );
		SimpleDateFormat sdf = new SimpleDateFormat ( "yyyyMMddhhmmss" );
		return sdf.format ( dt );
	}

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ Function + getTodayFormat Parameter + Description +
	 * 오늘 날짜를 반환한다. (yyyyMMddhhmmss) 24시간 타입 ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	public static String getTodayFormat4_24 ( ) {
		Date dt = new Date ( );
		SimpleDateFormat sdf = new SimpleDateFormat ( "yyyyMMddHHmmss" );
		return sdf.format ( dt );
	}

	public static String getTodayFormat4_24_m( ) {
		long time = System.currentTimeMillis(); 
		SimpleDateFormat dayTime = new SimpleDateFormat("yyyyMMddHHmmssSSS"); 
		String strDT = dayTime.format(new Date(time));
		return strDT;
	}
	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ Function + getTodayFormat Parameter + Description +
	 * 오늘 날짜를 반환한다. (yyyy-MM-dd hh:mm:ss) ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	public static String getTodayFormat5 ( ) {
		Date dt = new Date ( );
		SimpleDateFormat sdf = new SimpleDateFormat ( "yyyy-MM-dd hh:mm:ss" );
		return sdf.format ( dt );
	}

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ Function + getTodayFormat Parameter + Description +
	 * 오늘 날짜를 반환한다. (yyyy-MM-dd hh:mm:ss.SSS) ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	public static String getTodayFormat6 ( ) {
		Date dt = new Date ( );
		SimpleDateFormat sdf = new SimpleDateFormat ( "yyyy-MM-dd hh:mm:ss.SSS" );
		return sdf.format ( dt );
	}

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ Function + getTodayFormat Parameter + Description +
	 * 오늘 날짜를 반환한다. (yyyy-MM-dd hh:mm:ss.SSS) ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	public static String getTodayFormat6_24 ( ) {
		Date dt = new Date ( );
		SimpleDateFormat sdf = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss.SSS" );
		return sdf.format ( dt );
	}

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ Function + getTodayFormat Parameter + Description +
	 * 오늘 날짜를 반환한다. (yyyyMMdd) ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	public static String getTodayYearMonth ( ) {
		Date dt = new Date ( );
		SimpleDateFormat sdf = new SimpleDateFormat ( "yyyyMM" );
		return sdf.format ( dt );
	}

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ Function + getTodayYear Parameter + Description + 오늘
	 * 년도를 반환한다. (yyyy) ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	public static String getTodayYear ( ) {
		Date dt = new Date ( );
		SimpleDateFormat sdf = new SimpleDateFormat ( "yyyy" );
		return sdf.format ( dt );
	}

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ Function + getTodayMonth Parameter + Description +
	 * 오늘 월를 반환한다. (MM) ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	public static String getTodayMonth ( ) {
		Date dt = new Date ( );
		SimpleDateFormat sdf = new SimpleDateFormat ( "MM" );
		return sdf.format ( dt );
	}

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ Function + getTodayDay Parameter + Description + 오늘
	 * 날짜를 반환한다. (dd) ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	public static String getTodayDay ( ) {
		Date dt = new Date ( );
		SimpleDateFormat sdf = new SimpleDateFormat ( "dd" );
		return sdf.format ( dt );
	}

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ Function + getTodayFormat Parameter + Description +
	 * 오늘 요일을 반환한다.(문자형식) (월요일) ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	public static String getTodayCalendar ( ) {
		Calendar cal = Calendar.getInstance ( );
		String[] week = { "일", "월", "화", "수", "목", "금", "토" };

		return week[cal.get ( Calendar.DAY_OF_WEEK ) - 1] + "요일";
	}

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ Function + getTodayFormat Parameter + Description +
	 * 오늘 요일을 반환한다.(숫자형식) (1) ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	public static int getTodayCalendar2 ( ) {
		Calendar cal = Calendar.getInstance ( );
		int[] week = { 1, 2, 3, 4, 5, 6, 7 }; // 참조 : 일요일은 1 토요일은 7

		return week[cal.get ( Calendar.DAY_OF_WEEK ) - 1];
	}

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ Function + getTodayFormat Parameter + Description +
	 * 숫자형식 요일을 문자형식 요일로 변환하여 반환한다 (1:일요일~~~) ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	public static String getTodayCalendar ( int i_week ) {
		// String[] week = {"일","월","화","수","목","금","토"};
		String week = "";
		if ( i_week == 1 ) {
			week = "일";
		} else if ( i_week == 2 ) {
			week = "월";
		} else if ( i_week == 3 ) {
			week = "화";
		} else if ( i_week == 4 ) {
			week = "수";
		} else if ( i_week == 5 ) {
			week = "목";
		} else if ( i_week == 6 ) {
			week = "금";
		} else if ( i_week == 7 ) {
			week = "토";
		}

		return week + "요일";
	}

	/**
	 * 현재 날짜 YYYYMMDD 형식 문자열 출력
	 *
	 * @param cal
	 * @return
	 */
	public static String getYyyymmdd ( Calendar cal ) {
		Locale currentLocale = new Locale ( "KOREAN", "KOREA" );
		String pattern = "yyyyMMdd";
		SimpleDateFormat formatter = new SimpleDateFormat ( pattern, currentLocale );
		return formatter.format ( cal.getTime ( ) );
	}

	/**
	 * 현재 날짜에 일자 더하여 날짜 구하기
	 *
	 * @param iDay 더할 날짜
	 * @return YYYYMMDD
	 */
	public static String getDate ( int iDay ) {
		Calendar temp = Calendar.getInstance ( );
		StringBuffer sbDate = new StringBuffer ( );

		temp.add ( Calendar.DAY_OF_MONTH, iDay );

		int nYear = temp.get ( Calendar.YEAR );
		int nMonth = temp.get ( Calendar.MONTH ) + 1;
		int nDay = temp.get ( Calendar.DAY_OF_MONTH );

		sbDate.append ( nYear );
		if ( nMonth < 10 ) sbDate.append ( "0" );
		sbDate.append ( nMonth );
		if ( nDay < 10 ) sbDate.append ( "0" );
		sbDate.append ( nDay );

		return sbDate.toString ( );
	}

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ Function + getSelected Parameter + param1, param2
	 * Description + 두 값을 비교해서 같으면 "Selected" 를 반환한다. ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	public static String getSelected ( String param1, String param2 ) {
		if ( param1.equals ( param2 ) ) return "Selected";
		else return "";
	}

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ Function + getChecked Parameter + param1, param2
	 * Description + 두 값을 비교해서 같으면 "Checked" 를 반환한다. ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	public static String getChecked ( String param1, String param2 ) {
		if ( param1.equals ( param2 ) ) return "Checked";
		else return "";
	}

	/*
	 * 파일 업로드취약점
	 */
	public int fileUploadCheck ( String fileName ) {
		int result = 0;
		String check = fileName.substring ( fileName.lastIndexOf ( "." ) );

		if(check.equalsIgnoreCase(".php")||check.equalsIgnoreCase(".php3")||check.equalsIgnoreCase(".asp")||check.equalsIgnoreCase(".jsp")||
				check.equalsIgnoreCase(".cgi")||check.equalsIgnoreCase(".inc")||check.equalsIgnoreCase(".pl")||check.equalsIgnoreCase(".exe")
				||check.equalsIgnoreCase(".sh")||check.equalsIgnoreCase(".bat")){
			result = 1;
		} else {
			result = 0;
		}

		return result;
	}

	/*
	 * 이미지파일업로드 체크
	 */
	public int fileUploadCheckJpg ( String fileName ) {
		int result = 0;
		String check = fileName.substring ( fileName.lastIndexOf ( "." ) );

		if ( check.equalsIgnoreCase ( ".jpg" ) || check.equalsIgnoreCase ( ".jpeg" ) || check.equalsIgnoreCase ( ".gif" ) ) {
			result = 1;
		} else {
			result = 0;
		}

		return result;
	}

	/*
	 * 동영상파일업로드 체크
	 */
	public int fileUploadCheckMovie ( String fileName ) {
		int result = 0;
		String check = fileName.substring ( fileName.lastIndexOf ( "." ) );

		if ( check.equalsIgnoreCase ( ".wmv" ) || check.equalsIgnoreCase ( ".avi" ) ) {
			result = 1;
		} else {
			result = 0;
		}

		return result;
	}

	/*
	 * 파일 확장자 가져오기
	 */
	public String fileUploadExt ( String fileName ) {
		String check = fileName.substring ( fileName.lastIndexOf ( "." ) );
		check = GlobalCom.replace ( check, ".", "" );

		return check;
	}

	/*
	 * 문자 치환 replace "123" ,"2", "" -> "13"
	 */
	public static String replace ( String _src, String _target, String _dest ) {
		if ( _src == null || _src.trim ( ).length ( ) == 0 ) return null;
		if ( _target == null ) return _src;

		StringBuffer tmpBuffer = new StringBuffer ( );

		int nStart = 0;
		int nEnd = 0;
		int nLength = _src.length ( );
		int nTargetLength = _target.length ( );

		while ( nEnd < nLength ) {
			nStart = _src.indexOf ( _target, nEnd );
			if ( nStart < 0 ) {
				tmpBuffer.append ( _src.substring ( nEnd, nLength ) );

				break;
			} else {
				tmpBuffer.append ( _src.substring ( nEnd, nStart ) ).append ( _dest );

				nEnd = nStart + nTargetLength;
			}
		}

		return tmpBuffer.toString ( );
	}

	/*
	 * 스크립트 처리 유틸
	 */
	public static String getScriptAlert ( String check_code ) {
		check_code = isNullCheck ( check_code );
		String result = "";
		if ( check_code.equals ( "1" ) ) {
			result = "<script type='text/javascript'>alert('정상적으로 등록이 처리되었습니다.')</script>";
		} else if ( check_code.equals ( "2" ) ) {
			result = "<script type='text/javascript'>alert('정상적으로 수정이 처리되었습니다.')</script>";
		} else if ( check_code.equals ( "3" ) ) {
			result = "<script type='text/javascript'>alert('정상적으로 삭제가 처리되었습니다.')</script>";
		} else if ( check_code.equals ( "4" ) ) {
			result = "<script type='text/javascript'>alert('처리중 오류가 발생되었습니다.')</script>";
		} else if ( check_code.equals ( "5" ) ) {
			result = "<script type='text/javascript'>alert('이미 투표를 참여하셨습니다.')</script>";
		} else if ( check_code.equals ( "6" ) ) {
			result = "<script type='text/javascript'>alert('기존에 가입을 하셨습니다.')</script>";
		} else if ( check_code.equals ( "7" ) ) {
			result = "<script type='text/javascript'>alert('회원가입이 정상적으로 완료되었습니다. 감사합니다.')</script>";
		} else if ( check_code.equals ( "8" ) ) {
			result = "<script type='text/javascript'>alert('메일이 정상적으로 발송되었습니다')</script>";
		} else if ( check_code.equals ( "9" ) ) {
			result = "<script type='text/javascript'>alert('선택은 1개이상 가능합니다')</script>";
		} else if ( check_code.equals ( "10" ) ) {
			result = "<script type='text/javascript'>alert('선택은 2개이상 가능합니다')</script>";
		} else if ( check_code.equals ( "11" ) ) {
			result = "<script type='text/javascript'>alert('기존 연구실적 메인 노출 데이터가 2개이상 존재합니다.메인삭제를 해주십시오')</script>";
		} else if ( check_code.equals ( "12" ) ) {
			result = "<script type='text/javascript'>alert('1개만 선택하여주십시오.')</script>";
		} else if ( check_code.equals ( "13" ) ) {
			result = "<script type='text/javascript'>alert('정상적으로 적용되었습니다.')</script>";
		} else if ( check_code.equals ( "14" ) ) {
			result = "<script type='text/javascript'>alert('아이디 및 비밀번호를 확인해주세요.')</script>";
		} else if ( check_code.equals ( "15" ) ) {
			result = "<script type='text/javascript'>alert('기존에 아이디가 존재합니다.')</script>";
		} else if ( check_code.equals ( "16" ) ) {
			result = "<script type='text/javascript'>alert('로그인후 사용해 주시기 바랍니다.')</script>";
		} else if ( check_code.equals ( "17" ) ) {
			result = "<script type='text/javascript'>alert('메일이 정상적으로 발송되지 않았습니다.')</script>";
		} else if ( check_code.equals ( "18" ) ) {
			result = "<script type='text/javascript'>alert('메일이 메일형식이 잘못되었습니다.')</script>";
		} else if ( check_code.equals ( "19" ) ) {
			result = "<script type='text/javascript'>alert('회원정보가 없습니다. 다시 확인해 주시기 바랍니다.')</script>";
		} else if ( check_code.equals ( "20" ) ) {
			result = "<script type='text/javascript'>alert('정상적으로 상담되었습니다. 답변은 메일로 보내드립니다.')</script>";
		}

		return result;
	}

	/*
	 * 스크립트 처리 유틸
	 */
	public static String getScriptAlertMove ( String check_code, String return_url ) {
		check_code = isNullCheck ( check_code );
		String result = "";

		if ( check_code.equals ( "1" ) ) {
			result = "<script type='text/javascript'>alert('메일이 메일형식이 잘못되었습니다.');location.href='" + return_url + "';</script>";
		}

		return result;
	}

	/**
	 * 데이터 형이 숫자인지 문자인지 체크하는 메소드
	 *
	 * @param value 숫자면 true 문자면 false
	 * @return boolean
	 */
	public static boolean checkInt ( String value ) {
		boolean returnVal = false;
		int a = 0;
		int b = 0;
		for ( int i = 0; i < value.length ( ); i++ ) {
			char c = value.charAt ( i );
			if ( 0x30 <= c && c <= 0x39 ) {
				a++;
			} else {
				b++;
			}
		}
		if ( a > 0 ) {
			returnVal = true;
		} else if ( b > 0 ) {
			returnVal = false;
		}
		return returnVal;
	}

	/**
	 * 영문/ 숫자 존재 여부 체크 메서드 *
	 *
	 * @param value
	 * @return 0:영문/숫자 미존재 1:영문/숫자 존재 2:영문 존재 3:숫자 존재
	 */
	public static int checkStrInt ( String value ) {
		int type = 0; // 0:영문/숫자 미존재 1:영문/숫자 존재 2:영문 존재 3:숫자 존재
		int type1 = 0; // 영문
		int type2 = 0; // 숫자
		for ( int i = 0; i < value.length ( ); i++ ) {
			char c = value.charAt ( i );
			// 영문
			if ( ( 0x61 <= c && c <= 0x7A ) || ( 0x41 <= c && c <= 0x5A ) ) {
				type1 = 1;
				// 숫자
			} else if ( 0x30 <= c && c <= 0x39 ) {
				type2 = 1;
			}
		}

		if ( type1 == 0 && type2 == 0 ) { // 영문 숫자 미존재
			type = 0;
		} else if ( type1 == 1 && type2 == 0 ) { // 영문만 존재
			type = 2;
		} else if ( type1 == 0 && type2 == 1 ) { // 숫자만 존재
			type = 3;
		} else { // 영문/숫자 존재
			type = 1;
		}

		return type;
	}

	/**
	 * System.out.print를 줄이기 위한 메소드
	 *
	 * @param value
	 */
	public static void nPrint ( String value ) {
		System.out.println ( value );
	}

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ Function + getSplit Parameter + str, param
	 * Description + str 문자를 param 구분자로 Split 한 후 배열로 반환한다. ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	public static String[] getSplit ( String str, String param ) {
		StringTokenizer st = new StringTokenizer ( str, param );
		String[] split = new String[st.countTokens ( )];
		int i = 0;

		while ( st.hasMoreTokens ( ) ) {
			split[i] = st.nextToken ( );
			i++;
		}
		return split;
	}

	/**
	 * session NULL 체크
	 */
	String getSession ( HttpSession session, String attrName ) {
		return session.getAttribute ( attrName ) != null ? (String) session.getAttribute ( attrName ) : "";
	}

	public static String getUniqueFileName ( String path, String file ) {
		File tmp = new File ( path + file.toLowerCase ( ) );
		String fileName = file.toLowerCase ( );
		int i = 0;

		System.out.println ( "------------------>exist" + tmp.exists ( ) );
		if ( tmp.exists ( ) ) {
			while ( tmp.exists ( ) ) {
				if ( fileName.indexOf ( "." ) != -1 ) {
					String lcTemp = "(" + i + ").";
					fileName = file.toLowerCase ( ).replaceAll ( ".", lcTemp );
				} else {
					fileName = file.toLowerCase ( ) + "(" + i + ")";
					tmp = new File ( path + fileName );
					i++;
				}
			}
		}
		return fileName;
	}

	/**
	 * 이메일 주소 유효성 체크
	 *
	 * @param email
	 * @return boolean
	 */
	public static boolean isEmail ( String email ) {
		if ( email == null ) return false;
		boolean b = Pattern.matches ( "[\\w\\~\\-\\.]+@[\\w\\~\\-]+(\\.[\\w\\~\\-]+)+", email.trim ( ) );
		return b;
	}

	/**
	 * 현재 유닉스 타임 가져오기
	 *
	 * @return
	 */
	public static long getTodayUnixtime ( ) {
		return System.currentTimeMillis ( ) / 1000;
	}

	/**
	 * 현재 유닉스 타임 + 초
	 *
	 * @param secend
	 * @return
	 */
	public static long getTodayUnixtime ( int secend ) {
		return System.currentTimeMillis ( ) / 1000 + secend;
	}

	/**
	 * 날짜 더하기 메서드
	 *
	 * @param dateString YYYYMMDD
	 * @param addDate 더할 날짜 (3 -> 3일)
	 * @return YYYYMMDD
	 */
	public static String getTodayAddDate ( String dateString, int addDate ) {
		String result = "";

		// String dateString = "20120301";
		SimpleDateFormat formatter = new SimpleDateFormat ( "yyyyMMdd" );
		try {
			Date date = formatter.parse ( dateString );
			Calendar calendar = Calendar.getInstance ( );
			calendar.setTime ( date );
			calendar.add ( Calendar.DAY_OF_MONTH, +( addDate - 1 ) );
			result = formatter.format ( calendar.getTime ( ) );
		} catch (ParseException e) {}

		return result;
	}

	public static void main ( String[] args ) {

		String value = "한글000";

		int type = 0; // 0:영문/숫자 미존재 1:영문/숫자 존재 2:영문 존재 3:숫자 존재
		int type1 = 0; // 영문
		int type2 = 0; // 숫자
		for ( int i = 0; i < value.length ( ); i++ ) {
			char c = value.charAt ( i );
			// 영문
			if ( ( 0x61 <= c && c <= 0x7A ) || ( 0x41 <= c && c <= 0x5A ) ) {
				type1 = 1;
				// 숫자
			} else if ( 0x30 <= c && c <= 0x39 ) {
				type2 = 1;
			}
		}

		if ( type1 == 0 && type2 == 0 ) { // 영문 숫자 미존재
			type = 0;
		} else if ( type1 == 1 && type2 == 0 ) { // 영문만 존재
			type = 2;
		} else if ( type1 == 0 && type2 == 1 ) { // 숫자만 존재
			type = 3;
		} else { // 영문/숫자 존재
			type = 1;
		}

		System.out.println ( type );
	}
	
	/**
	 * 날짜(월) 더하기 메서드  
	 * @param month		
	 * @return YYYY-MM-DD
	 */
	public static String getTodayAddMonth(int month) {
		String result = "";
	    SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd");
	    
		Calendar calendar = Calendar.getInstance();
	    calendar.add(Calendar.MONTH, month);
	    result = formatter.format(calendar.getTime());	    
	    
	    return result; 
	}

	/**
	 * 전체 자릿수와 숫자형 데이터를 입력받아 앞에 0을 붙인다 ex) appendLeftZero("1", 4) -> 0001
	 *
	 * @param number 숫자형 데이터
	 * @param size 전체 자릿수
	 * @return
	 */
	public static String appendLeftZero ( String number, int size ) {
		String result = number;
		int end = size - number.length ( );
		for ( int i = 0; i < end; i++ ) {
			result = "0" + result;
		}

		return result;

	}

	/**
	 * 프로퍼티 파일 경로와 프로퍼티 키를 입력받아 해당 프로퍼티 파일 안에 있는 키에 해당하는 값을 읽어온다
	 *
	 * @param filepath 프로퍼티 파일 경로
	 * @param key 입력받은 프로퍼티 파일 경로에 있는 프로퍼티 파일에 있는 프로퍼티 키
	 * @return 프로퍼티 값
	 */
	public static String getProperties ( String filepath, String key ) {
		String result = "";
		Properties props = null;
		FileInputStream fis = null;

		try {
			props = new Properties ( );
			fis = new FileInputStream ( filepath );
			props.load ( new java.io.BufferedInputStream ( fis ) );
			result = props.getProperty ( key ).trim ( );
		} catch ( Exception e ) {

		} finally {
			try {
				fis.close ( );
			} catch ( Exception e ) {

			}
		}

		return result;
	}

	/**
	 * 프로퍼티 파일 경로를 입력받아 해당 프로퍼티 파일의 key 값들을 가져온다
	 *
	 * @param filepath 프로퍼티 파일 경로
	 * @return 프로퍼티 key값들
	 */
	public static List<String> getPropertiesKeyset ( String filepath ) {
		List<String> result = new ArrayList<String> ( );
		Properties props = null;
		FileInputStream fis = null;

		try {
			props = new Properties ( );
			fis = new FileInputStream ( filepath );
			props.load ( new java.io.BufferedInputStream ( fis ) );
			Set keyset = props.keySet ( );
			Iterator iter = keyset.iterator ( );
			while ( iter.hasNext ( ) ) {
				String key = (String) ( iter.next ( ) );
				result.add ( key );
			}
		} catch ( Exception e ) {

		} finally {
			try {
				fis.close ( );
			} catch ( Exception e ) {

			}
		}

		return result;
	}

	/**
	 * 스케쥴러를 이용하여 캐쉬작업을 진행할때 해당 캐쉬의 interval을 선택적으로 리턴해주는 함수 (스케줄러로 호출한 경우엔 해당 프로퍼티에 셋팅된 interval 값을 리턴해주고 스케줄러가 호출한 것이
	 * 아닐 경우엔 강제로 DB에서 읽어오는 옵션값을 확인해서 그 값이 true일 경우엔 -1(DB에서 읽어온뒤 캐쉬에 셋팅하고 리턴) , false 일 경우엔 0(무조건 캐쉬에서 읽음)을 리턴하도록 한다)
	 *
	 * @param callByScheduler scheduler 호출 여부(Y:Scheduler, N:단말, A:관리툴)
	 * @param intervalProperty 프로퍼티에서 읽은 interval값
	 * @return 캐쉬 interval 값
	 */
	public static long getCacheInterval ( String callByScheduler, String intervalProperty ) {
		long interval = 0;

		String forceReadDB = SmartUXProperties.getProperty ( "cache.forceReadDB" ); // 현제 셋팅이 강제로 DB에서 읽어오도록 되어 있는지를 확인

		if ( "N".equals ( callByScheduler ) ) {
			if ( "Y".equals ( forceReadDB ) ) {
				interval = -1;
			} else {
				interval = 0;
			}
		} else if ( "Y".equals ( callByScheduler ) ) {
			// CacheIntervalService service = new CacheIntervalServiceImpl();
			// try {
			// interval = service.getCacheInterval(cacheIntervalName);
			// } catch (Exception e) {
			// interval = Long.parseLong(intervalProperty);
			// }
			interval = Long.parseLong ( intervalProperty );
		} else if ( "A".equals ( callByScheduler ) ) { // 관리툴에서 호출을 했을 경우엔 DB 변경이 있은 뒤의 반영이므로 DB에서 조회한뒤 캐쉬에 반영하도록 한다
			interval = -1;
		}

		// System.out.println("############Cache getCacheInterval Return["+interval+"]  조회 !!!!!!!!!!!##########");

		return interval;
	}

	/**
	 * 주소, 포트번호, url, 파라미터, 타임아웃을 넘겨서 해당 웹주소를 호출하는 함수
	 *
	 * @param host 호스트(ex: localhost)
	 * @param port 포트번호(ex:8080)
	 * @param url URL(ex: /smartux_adm/getThemeInfo)
	 * @param param URL에 붙는 GET 방식 파라미터(ex:a=1234&b=4567)
	 * @param timeout HttpConnection의 Timeout 설정
	 * @param protocolName URI 호출 프로토콜명(http, https 등)
	 * @return 해당 웹페이지를 호출했을때 리턴되는 컨텐트 소스
	 * @throws Exception
	 */
	public static String callHttpClient ( String host, int port, String url, String param, int timeout, String protocolName ) throws Exception {
		String result = "";
		URI uri = null;
		HttpGet get = null;

		HttpClient httpClient = null;
		HttpResponse response = null;
		HttpEntity entity = null;
		InputStream is = null;
		StringBuffer sb = new StringBuffer ( );
		byte[] b = new byte[4096];

		if ( ( protocolName == null ) || "".equals ( protocolName ) ) {
			protocolName = "http";
		}

		try {

			// 넘겨받은 url안에 물음표가 있는 경우(ex : /smartux_adm/getRealRatingByTheme?theme_code=T002)엔 넘겨받은 url 안에 파라미터가 있다를 것이므로
			// url을 재구성하여 파라미터 부분은 param 파라미터에 합치도록 한다
			// System.out.println("url : " + url);
			String[] urlparams = url.split ( "\\?" );
			if ( urlparams != null ) {
				// System.out.println("urlparams.length : " + urlparams.length);
				if ( urlparams.length == 2 ) {
					url = urlparams[0];
					param = param + "&" + urlparams[1];

					// System.out.println("final url : " + url);
					// System.out.println("final param : " + param);

				}
			}

			// uri = URIUtils.createURI("http", host, port, url, param, null);
			uri = URIUtils.createURI ( protocolName, host, port, url, param, null );
			get = new HttpGet ( uri );
			// get.setHeader("Accept", "*/*");

			httpClient = new DefaultHttpClient ( );

			if ( "https".equals ( protocolName ) ) {
				TrustManager easyTrustManager = new X509TrustManager ( ) {

					public X509Certificate[] getAcceptedIssuers ( ) {
						// no-op
						return null;
					}

					public void checkServerTrusted ( X509Certificate[] chain, String authType ) throws CertificateException {}

					public void checkClientTrusted ( X509Certificate[] chain, String authType ) throws CertificateException {}
				};

				SSLContext sslcontext = SSLContext.getInstance ( "TLS" );
				sslcontext.init(null, new TrustManager[]{easyTrustManager}, null);

				// SSLSocketFactory socketFactory = new SSLSocketFactory(sslcontext,
				// SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);

				SSLSocketFactory socketFactory = new SSLSocketFactory ( sslcontext, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER );

				Scheme sch = new Scheme ( "https", 443, socketFactory );
				httpClient.getConnectionManager ( ).getSchemeRegistry ( ).register ( sch );

				/*
				 * HttpGet httpget = new HttpGet( "https://msp.f-secure.com/web-test/common/test.html");
				 * 
				 * System.out.println("executing request" + httpget.getRequestLine());
				 * 
				 * HttpResponse response = httpclient.execute(httpget);
				 * 
				 * HttpEntity entity = response.getEntity();
				 * 
				 * String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");
				 * 
				 * System.out.println(responseBody);
				 * 
				 * System.out.println("----------------------------------------");
				 * System.out.println(response.getStatusLine()); if (entity != null) {
				 * System.out.println("Response content length: " + entity.getContentLength()); }
				 * EntityUtils.consume(entity);
				 */
			}

			HttpParams params = httpClient.getParams ( );
			params.setParameter ( CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1 );
			HttpConnectionParams.setConnectionTimeout ( params, timeout );

			HttpConnectionParams.setSoTimeout ( params, timeout );

			response = httpClient.execute ( get );
			entity = response.getEntity ( );
			is = entity.getContent ( );
			sb = new StringBuffer ( );

			for ( int n; ( n = is.read ( b ) ) != -1; ) {
				sb.append ( new String ( b, 0, n ) );
			}
			result = sb.toString ( );

		} catch ( URISyntaxException e ) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			throw e;
		} catch ( ClientProtocolException e ) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			throw e;
		} catch ( IOException e ) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			throw e;
		} finally {
			httpClient.getConnectionManager ( ).shutdown ( );
		}

		return result;
	}

	/**
	 * 주소, 포트번호, url, 파라미터, 타임아웃을 넘겨서 해당 웹주소를 호출하는 함수
	 *
	 * @param host
	 *            호스트(ex: localhost)
	 * @param port
	 *            포트번호(ex:8080)
	 * @param url
	 *            URL(ex: /smartux_adm/getThemeInfo)
	 * @param param
	 *            URL에 붙는 GET 방식 파라미터(ex:a=1234&b=4567)
	 * @param timeout
	 *            HttpConnection의 Timeout 설정
	 * @param protocolName
	 *            URI 호출 프로토콜명(http, https 등)
     * @param method
     *            GET / POST / PUT / DELETE
	 * @return 해당 웹페이지를 호출했을때 리턴되는 컨텐트 소스
	 * @throws Exception
	 */
	public static String callHttpClient(String host, int port, String url, String param, int timeout, String protocolName, String method) throws Exception {

		String result = "";
		URI uri = null;

		HttpClient httpClient = null;
		HttpResponse response = null;
		HttpEntity entity = null;
		InputStream is = null;
		StringBuffer sb = new StringBuffer();
		byte[] b = new byte[4096];

		if ((protocolName == null) || "".equals(protocolName)) {
			protocolName = "http";
		}

		try {

			// 넘겨받은 url안에 물음표가 있는 경우(ex : /smartux_adm/getRealRatingByTheme?theme_code=T002)엔 넘겨받은 url 안에 파라미터가 있다를 것이므로
			// url을 재구성하여 파라미터 부분은 param 파라미터에 합치도록 한다
			String[] urlparams = url.split("\\?");
			if (urlparams != null) {
				if (urlparams.length == 2) {
					url = urlparams[0];
					param = param + "&" + urlparams[1];
				}
			}

			uri = URIUtils.createURI(protocolName, host, port, url, param, null);


			httpClient = new DefaultHttpClient();

			if ("https".equals(protocolName)) {

				TrustManager easyTrustManager = new X509TrustManager() {

					public X509Certificate[] getAcceptedIssuers() {
						// no-op
						return null;
					}

					public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					}

					public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					}
				};

				SSLContext sslcontext = SSLContext.getInstance("TLS");
				sslcontext.init(null, new TrustManager[] { easyTrustManager }, null);

				SSLSocketFactory socketFactory = new SSLSocketFactory(sslcontext, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

				Scheme sch = new Scheme("https", 443, socketFactory);
				httpClient.getConnectionManager().getSchemeRegistry().register(sch);
			}

			HttpParams params = httpClient.getParams();
			params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			HttpConnectionParams.setConnectionTimeout(params, timeout);
			HttpConnectionParams.setSoTimeout(params, timeout);

            if ("POST".equalsIgnoreCase(method)) {
                HttpPost post = new HttpPost(uri);
                response = httpClient.execute(post);
            } else if ("PUT".equalsIgnoreCase(method)) {
                HttpPut put = new HttpPut(uri);
                response = httpClient.execute(put);
            } else if ("DELETE".equalsIgnoreCase(method)) {
                HttpDelete delete = new HttpDelete(uri);
                response = httpClient.execute(delete);
            } else {
                HttpGet get = new HttpGet(uri);
                response = httpClient.execute(get);
            }

			entity = response.getEntity();
			is = entity.getContent();
			sb = new StringBuffer();

			for (int n; (n = is.read(b)) != -1;) {
				sb.append(new String(b, 0, n));
			}
			result = sb.toString();

		} catch (URISyntaxException e) {
			throw e;
		} catch (ClientProtocolException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			if(is != null) is.close();

			if(httpClient != null) httpClient.getConnectionManager().shutdown();
		}

		return result;
	}
	
	// public static String callHttpApi(String host, int port, String url, String param, int timeout, String
	// protocolName, String cnt_type) throws Exception{
	// DefaultHttpClient httpClient = new DefaultHttpClient();
	// if(timeout!=0){
	// httpClient.getParams().setParameter("http.protocol.expect-continue", false);
	// httpClient.getParams().setParameter("http.connection.timeout", timeout);
	// httpClient.getParams().setParameter("http.socket.timeout", timeout);
	// }
	//
	// String callUrl = protocolName + "://" + host + ":" + port + url;
	// if(param!=null && !"".equals(param)){
	// callUrl = callUrl + "?" + param;
	// }
	//
	// System.out.println(callUrl+"<<<<<<<<<<<<,");
	//
	// HttpGet get = new HttpGet(callUrl);
	// if(cnt_type != null && !"".equals(cnt_type)){
	// get.setHeader("Content-Type", cnt_type);
	// }
	//
	// ResponseHandler<String> responseHandler = new BasicResponseHandler();
	// String response = httpClient.execute(get,responseHandler);
	//
	// return response;
	// }

	public static String getSystemProperty ( String key ) {

		String val = "";

		Properties props = System.getProperties ( );
		Enumeration enums = props.keys ( );
		while ( enums.hasMoreElements ( ) ) {
			String _key = (String) enums.nextElement ( );

			// String _val = (String) props.get(_key);
			// System.out.println(_key + " = " + _val);

			if ( key.equals ( _key ) ) {
				val = (String) props.get ( key );
				break;
			}
		}
		return val;
	}

	/**
	 * 서버의 단말 API를 호출하는 함수(싱크용)
	 *
	 * @param host 서버 ip 주소
	 * @param port 서버 포트번호
	 * @param url 단말 API URL
	 * @param param 단말 API에 사용되는 파라미터(Get 방식 연결 문자열)
	 * @param timeout HttpClient timeout 시간
	 * @param retrycnt 호출 실패시 재시도 횟수
	 * @param protocolName URI 호출 프로토콜명(http, https 등)
	 */
	public static void callAPISync ( String host, int port, String url, String param, int timeout, int retrycnt, String protocolName ) {
		String result = "";
		String retryresult = "";

		try {
			result = callHttpClient ( host, port, url, param, timeout, protocolName );
			if ( result.length ( ) >= 4 ) {
				result = result.substring ( 0, 4 );
			}

			if ( !( SmartUXProperties.getProperty ( "flag.success" ).equals ( result ) ) ) {

				for ( int i = 0; i < retrycnt; i++ ) {
					retryresult = GlobalCom.callHttpClient ( host, port, url, param, timeout, protocolName );
					if ( retryresult.length ( ) >= 4 ) {
						retryresult = retryresult.substring ( 0, 4 );
					}

					if ( SmartUXProperties.getProperty ( "flag.success" ).equals ( retryresult ) ) {
						break;
					}
				}
			}
		} catch ( Exception e ) {
			for ( int i = 0; i < retrycnt; i++ ) {
				try {
					retryresult = GlobalCom.callHttpClient ( host, port, url, param, timeout, protocolName );
				} catch ( Exception sube ) {

				}

				if ( retryresult.length ( ) >= 4 ) {
					retryresult = retryresult.substring ( 0, 4 );
				}

				if ( SmartUXProperties.getProperty ( "flag.success" ).equals ( retryresult ) ) {
					break;
				}
			}
		}
	}

	// /**
	// * 서버의 단말 API를 호출하는 함수
	// * @param host 서버 ip 주소
	// * @param port 서버 포트번호
	// * @param url 단말 API URL
	// * @param param 단말 API에 사용되는 파라미터(Get 방식 연결 문자열)
	// * @param timeout HttpClient timeout 시간
	// * @param retrycnt 호출 실패시 재시도 횟수
	// * @param protocolName URI 호출 프로토콜명(http, https 등)
	// */
	// public static void callAPI(String host, int port, String url, String param, int timeout, int retrycnt, String
	// protocolName){
	// String result = "";
	// String retryresult = "";
	//
	// host = getSystemProperty("JBOSS_IP");
	// System.out.println("##########HOST = "+host);
	//
	// port = Integer.parseInt(getSystemProperty("JBOSS_PORT"));
	// System.out.println("##########PORT = "+port);
	// try{
	// result = callHttpClient(host, port, url, param, timeout, protocolName);
	// if(result.length() >= 4){
	// result = result.substring(0, 4);
	// }
	//
	// if(!(SmartUXProperties.getProperty("flag.success").equals(result))){
	//
	// for(int i=0; i < retrycnt; i++){
	// retryresult = GlobalCom.callHttpClient(host, port, url, param, timeout, protocolName);
	// if(retryresult.length() >= 4){
	// retryresult = retryresult.substring(0, 4);
	// }
	//
	// if(SmartUXProperties.getProperty("flag.success").equals(retryresult)){
	// break;
	// }
	// }
	// }
	// }catch(Exception e){
	// for(int i=0; i < retrycnt; i++){
	// try{
	// retryresult = GlobalCom.callHttpClient(host, port, url, param, timeout, protocolName);
	// }catch(Exception sube){
	//
	// }
	//
	// if(retryresult.length() >= 4){
	// retryresult = retryresult.substring(0, 4);
	// }
	//
	// if(SmartUXProperties.getProperty("flag.success").equals(retryresult)){
	// break;
	// }
	// }
	// }
	// }

	/**
	 * 서버의 단말 API를 호출하는 함수
	 *
	 * @param host 서버 ip 주소
	 * @param port 서버 포트번호
	 * @param url 단말 API URL
	 * @param param 단말 API에 사용되는 파라미터(Get 방식 연결 문자열)
	 * @param timeout HttpClient timeout 시간
	 * @param retrycnt 호출 실패시 재시도 횟수
	 * @param protocolName URI 호출 프로토콜명(http, https 등)
	 */
	public static void callAPI ( String url, String param, int timeout, int retrycnt, String protocolName ) {
		String result = "";
		String retryresult = "";

		String host = "";
		int port = 0;

		host = getSystemProperty ( "JBOSS_IP" );
		System.out.println ( "##########HOST = " + host );

		port = Integer.parseInt ( getSystemProperty ( "JBOSS_PORT" ) );
		System.out.println ( "##########PORT = " + port );
		try {
			result = callHttpClient ( host, port, url, param, timeout, protocolName );
			if ( result.length ( ) >= 4 ) {
				result = result.substring ( 0, 4 );
			}

			if ( !( SmartUXProperties.getProperty ( "flag.success" ).equals ( result ) ) ) {

				for ( int i = 0; i < retrycnt; i++ ) {
					retryresult = GlobalCom.callHttpClient ( host, port, url, param, timeout, protocolName );
					if ( retryresult.length ( ) >= 4 ) {
						retryresult = retryresult.substring ( 0, 4 );
					}

					if ( SmartUXProperties.getProperty ( "flag.success" ).equals ( retryresult ) ) {
						break;
					}
				}
			}
		} catch ( Exception e ) {
			for ( int i = 0; i < retrycnt; i++ ) {
				try {
					retryresult = GlobalCom.callHttpClient ( host, port, url, param, timeout, protocolName );
				} catch ( Exception sube ) {

				}

				if ( retryresult.length ( ) >= 4 ) {
					retryresult = retryresult.substring ( 0, 4 );
				}

				if ( SmartUXProperties.getProperty ( "flag.success" ).equals ( retryresult ) ) {
					break;
				}
			}
		}
	}

	/**
	 * IP 어드레스를 가져온다
	 *
	 * @return
	 */
	public static String getIPAddress ( ) {

		String result = "";

		String hostname = "";
		InetAddress[] inetAddresses = null;

		try {
			// 자기 자신의 IP가 2개 이상이 있을수 있다(랜카드 2장 이상이 존재할 경우)
			// 배열중 처음에 오는 IP를 서버를 사용한다
			hostname = InetAddress.getLocalHost ( ).getHostName ( );
			inetAddresses = InetAddress.getAllByName ( hostname );

			result = inetAddresses[0].getHostAddress ( );

			/*
			 * thisIp = InetAddress.getLocalHost(); localIP = thisIp.getHostAddress();
			 */
			// System.out.println("localIP : " + localIP);

		} catch ( UnknownHostException e1 ) {
			result = "127.0.0.1";
		}

		return result;
	}

	// /**
	// * 각 서버에 있는 단말 API를 호출하여 모든 서버의 캐쉬 메모리의 내용을 sync해주는 작업을 진행한다
	// * @param port 서버 포트번호
	// * @param url 단말에서 사용하는 API URL
	// * @param param 단말 API에 사용되는 파라미터(Get 방식 연결 문자열)
	// * @param timeout HttpConnection의 Timeout 설정
	// * @param retrycnt 호출 실패시 재시도 횟수
	// * @param protocolName URI 호출 프로토콜명(http, https 등)
	// */
	// public static void syncServerCache(String url, String param, int timeout, int retrycnt, String protocolName){
	// List<String> localIPList = new ArrayList();
	//
	// String hostname = "";
	// InetAddress [] inetAddresses = null;
	// try {
	// // 자기 자신의 IP가 2개 이상이 있을수 있다(랜카드 2장 이상이 존재할 경우)
	// // 그렇기 때문에 자기 자신의 IP를 저장하는 String Array로 이를 구현해야 한다
	//
	// hostname = InetAddress.getLocalHost().getHostName();
	// inetAddresses = InetAddress.getAllByName(hostname);
	//
	// for(InetAddress item : inetAddresses){
	// String ipaddr = item.getHostAddress();
	// localIPList.add(ipaddr);
	// }
	//
	// /*
	// thisIp = InetAddress.getLocalHost();
	// localIP = thisIp.getHostAddress();
	// */
	// // System.out.println("localIP : " + localIP);
	// } catch (UnknownHostException e1) {
	//
	// }
	// String hostsString = SmartUXProperties.getProperty("cache.ServerIPList");
	// String [] hostList = hostsString.split("\\|");
	// String portsString = SmartUXProperties.getProperty("cache.ServerPortList");
	// String [] portList = portsString.split("\\|");
	// int idx = -1;
	// for(String host : hostList){
	// /*
	//
	// if(host.equals(localIP)) continue; // IP 목록에 자기 자신의 ip가 있을 경우 호출하지 않도록 하기 위해 건너뛰도록 한다
	// callAPI(host, Integer.parseInt(portList[idx]), url, param, timeout, retrycnt); // 다른 서버의 단말 API를 호출한다
	// */
	// idx++;
	// boolean isLocal = false;
	// for(String localip : localIPList){
	// // System.out.println("localip : " + localip);
	// if(host.equals(localip)){
	// // System.out.println("find local");
	// isLocal = true;
	// break;
	// }
	// }
	//
	// if(isLocal == true){
	// // System.out.println("find local bypass");
	// continue;
	// }else{
	// callAPI(host, Integer.parseInt(portList[idx]), url, param, timeout, retrycnt, protocolName); // 다른 서버의 단말 API를
	// 호출한다
	// }
	// }
	// }
	//
	/**
	 * 각 서버에 있는 단말 API를 호출하여 모든 서버의 캐쉬 메모리의 내용을 sync해주는 작업을 진행한다
	 *
	 * @param port 서버 포트번호
	 * @param url 단말에서 사용하는 API URL
	 * @param param 단말 API에 사용되는 파라미터(Get 방식 연결 문자열)
	 * @param timeout HttpConnection의 Timeout 설정
	 * @param retrycnt 호출 실패시 재시도 횟수
	 * @param protocolName URI 호출 프로토콜명(http, https 등)
	 */
	public static void syncServerCache ( String url, String param, int timeout, int retrycnt, String protocolName ) {

		String hostsString = SmartUXProperties.getProperty ( "cache.ServerIPList" );
		String[] hostList = hostsString.split ( "\\|" );
		String portsString = SmartUXProperties.getProperty ( "cache.ServerPortList" );
		String[] portList = portsString.split ( "\\|" );
		int idx = -1;
		for ( String host : hostList ) {
			/*
			 * 
			 * if(host.equals(localIP)) continue; // IP 목록에 자기 자신의 ip가 있을 경우 호출하지 않도록 하기 위해 건너뛰도록 한다 callAPI(host,
			 * Integer.parseInt(portList[idx]), url, param, timeout, retrycnt); // 다른 서버의 단말 API를 호출한다
			 */
			idx++;
			
			callAPISync ( host, Integer.parseInt ( portList[idx] ), url, param, timeout, retrycnt, protocolName ); // 다른
			// 서버의
			// 단말
			// API를
			// 호출한다
		}
	}
	
	/**
	 * admin 각 서버에 있는 단말 API를 호출하여 모든 서버의 캐쉬 메모리의 내용을 sync해주는 작업을 진행한다
	 * @param url
	 * @param param
	 * @param timeout
	 * @param retrycnt
	 * @param protocolName
	 */
	public static void syncAdminServerCache ( String url, String param, int timeout, int retrycnt, String protocolName ) {
		
		String hostsString = SmartUXProperties.getProperty ( "myturn.AdminServerIPList" );
		String[] hostList = hostsString.split ( "\\|" );
		String portsString = SmartUXProperties.getProperty ( "myturn.AdminServerPortList" );
		String[] portList = portsString.split ( "\\|" );
		int idx = -1;
		for ( String host : hostList ) {
			/*
			 * 
			 * if(host.equals(localIP)) continue; // IP 목록에 자기 자신의 ip가 있을 경우 호출하지 않도록 하기 위해 건너뛰도록 한다 callAPI(host,
			 * Integer.parseInt(portList[idx]), url, param, timeout, retrycnt); // 다른 서버의 단말 API를 호출한다
			 */
			idx++;
			
			callAPISync ( host, Integer.parseInt ( portList[idx] ), url, param, timeout, retrycnt, protocolName ); // 다른
			// 서버의
			// 단말
			// API를
			// 호출한다
		}
	}

	public static void testPushGWLog ( String ip, String log ) {
		String file_name = SmartUXProperties.getProperty ( "filepath.pushgw.pushLog" ) + "IP_PushGW.log." + getTodayFormat2 ( );

		try {
			FileWriter testfw = new FileWriter ( file_name, true );

			// 개행문제 제거
			log = log.replace ( System.getProperty ( "line.separator" ), "" );
			log = log.replace ( "\r", "" );

			testfw.append ( "[" + getTodayFormat6_24 ( ) + "]" + " [" + ip + "]" + log + "\n" );
			testfw.close ( );
		} catch ( IOException e ) {
			e.printStackTrace ( );
		}
	}

	public static void testIOS_PushGWLog ( String ip, String log ) {
		String file_name = SmartUXProperties.getProperty ( "filepath.pushgw.pushLog" ) + "070_PushGW.log." + getTodayFormat2 ( );

		try {
			FileWriter testfw = new FileWriter ( file_name, true );

			// 개행문제 제거
			log = log.replace ( System.getProperty ( "line.separator" ), "" );
			log = log.replace ( "\r", "" );

			testfw.append ( "[" + getTodayFormat6_24 ( ) + "]" + " [" + ip + "]" + log + "\n" );
			testfw.close ( );
		} catch ( IOException e ) {
			e.printStackTrace ( );
		}
	}

	/**
	 * FTP 서버에 파일을 업로드 하는 함수
	 *
	 * @param ip 서버 ip
	 * @param port 서버 포트번호
	 * @param id 로그인 아이디
	 * @param password 로그인 패스워드
	 * @param directory 업로드하고자 하는 디렉토리
	 * @param file 업로드하고자 하는 파일
	 * @throws Exception
	 */
	public static void FTPUpload ( String ip, int port, String id, String password, String directory, File file ) throws SmartUXException {
		FTPClient ftp = null;
		FileInputStream fis = null;
		try {
			ftp = new FTPClient ( );
			ftp.setControlEncoding ( "UTF-8" );
			// ftp.setDefaultTimeout(2000);
			ftp.connect ( ip, port );
			boolean loginok = ftp.login ( id, password );
			if ( loginok == false ) {
				throw new Exception ( "FTP Login Fail" );
			}
			ftp.enterLocalPassiveMode ( ); // FTP 전송시 425 Failed to establish connection 에러가 발생하여 Passive Mode 접속 사용
			boolean changedir = ftp.changeWorkingDirectory ( directory );
			if ( changedir == false ) {
				throw new Exception ( "FTP Change Woking Directory Fail" );
			}
			fis = new FileInputStream ( file );
			// 텍스트 파일은 아스키 모드로, 그 외의 파일은 바이너리 모드로 잡는다
			String file_name = file.getName ( );
			String ext = file_name.substring ( file_name.lastIndexOf ( "." ) + 1, file_name.length ( ) ); // 확장자 구하기
			if ( "txt".equals ( file_name ) ) {
				ftp.setFileType ( FTP.ASCII_FILE_TYPE );
			} else {
				ftp.setFileType ( FTP.BINARY_FILE_TYPE );
			}

			boolean storefile = ftp.storeFile ( file.getName ( ), fis );
			if ( storefile == false ) {
				throw new Exception ( "FTP Store File Fail" );
			}

		} catch ( Exception e ) {
			System.out.println ( "FTPUploadException : " + e.getMessage ( ) );
			SmartUXException suxme = new SmartUXException ( );
			suxme.setFlag ( "FTP ERROR" );
			suxme.setMessage ( "FTP ERROR" );
			throw suxme;
		} finally {
			if ( ftp != null ) {
				try {
					ftp.logout ( );
				} catch ( Exception sube1 ) {

				}

				try {
					ftp.disconnect ( );
				} catch ( Exception sube2 ) {

				}
			}
		}
	}

	/**
	 * Restful 형식 URL 파라미터 정보만 Map으로 추출 메서드 ex)
	 * /smartux_adm/ThemeInfo/sa_id/210066192411/stb_mac/001c.626f.913e/app_type/UX/start_num/-1/req_count/null 1: context
	 * 2: API 명 3~~~: 파라미터
	 *
	 * @param uri request.getRequestURI()
	 * @return HashMap<String, String>
	 * @throws Exception
	 */
	public static HashMap<String, String> getRestfulParamMap ( int param_index, String uri ) throws Exception {
		HashMap<String, String> paramMap = new HashMap<String, String> ( );

		String[] uriArr = uri.split ( "/" );
		String _key = "";
		String _val = "";

		int k = 1;
		for ( int i = param_index; i < uriArr.length; i++ ) {
			if ( k % 2 == 1 ) { // 파라미터 키
				_key = uriArr[i];
			} else { // 파라미터 값
				_val = uriArr[i];
				paramMap.put ( _key, _val );
			}
			k++;
		}
		return paramMap;
	}

	/**
	 * Restful 형식 URL 파라미터 정보만 Map으로 추출 메서드 ex)
	 * /smartux_adm/ThemeInfo/sa_id/210066192411/stb_mac/001c.626f.913e/app_type/UX/start_num/-1/req_count/null 1: context
	 * 2: API 명 3~~~: 파라미터
	 *
	 * @param uri request.getRequestURI()
	 * @return HashMap<String, String>
	 * @throws Exception
	 */
	public static HashMap<String, String> getRestfulParamMap ( String uri ) throws Exception {
		HashMap<String, String> paramMap = new HashMap<String, String> ( );

		String[] uriArr = uri.split ( "/" );
		String _key = "";
		String _val = "";
		for ( int i = 3; i < uriArr.length; i++ ) {
			if ( i % 2 == 1 ) { // 파라미터 키
				_key = uriArr[i];
			} else { // 파라미터 값
				_val = uriArr[i];
				paramMap.put ( _key, _val );
			}
		}
		return paramMap;
	}

	/**
	 * Restful 형식 URL 파라미터 정보 Map중 요청한 키의 값 추출 메서드
	 *
	 * @param map getRestfulParamMap(uri)
	 * @param key String
	 * @return String
	 * @throws Exception
	 */
	public static String getRestfulParam ( HashMap<String, String> map, String key ) throws Exception {
		String result = map.get ( key );
		return result;
	}

	/**
	 * 현재 패널이 NSC 패널인지 구분한다
	 *
	 * @param panelID 패널 아이디
	 * @return NSC 패널 여부
	 */
	public static boolean isNSCPanel(String panelID) {
		String[] hdtvPanelID = SmartUXProperties.getProperty("hdtv.panel.id").split("\\|");

		boolean isNSC = false;

		for (String s : hdtvPanelID) {
			if (panelID.startsWith(s)) {
				isNSC = true;
				break;
			}
		}

		return isNSC;
	}

	public static boolean isEmpty(Object object) {
		if (null == object) {
			return true;
		}

		if (object instanceof String) {
			return (((String) object).trim().isEmpty());
		} else if (object instanceof List) {
			return (((List) object).isEmpty());
		} else {
			return true;
		}
	}

	/**
	 * Map에 담긴 파라미터의 유효성을 체크한다.
	 *
	 * @param params 파라미터 맵
	 */
	public static void checkValidation(Map<String, String> params) {
		SmartUXException exception = new SmartUXException();

		for (Map.Entry<String, String> entry : params.entrySet()) {
			if (isEmpty(entry.getValue())) {
				exception.setFlag(SmartUXProperties.getProperty("flag.paramnotfound"));
				exception.setMessage(SmartUXProperties.getProperty("message.paramnotfound", entry.getKey()));
				throw exception;
			}
		}
	}

	/**
	 * 현재 서버 인덱스를 가져온다(Deprecated)
     * 서버 환경변수 server.index 사용
     *
	 * @return HashMap key(index:인덱스, total: WAS 총 개수)
	 */
    @Deprecated
	public static Map<String, Integer> getServerIndex() {
		Map<String, Map<String, Integer>> map = new HashMap<String, Map<String, Integer>>();

		String[] ipList = SmartUXProperties.getProperty("cache.ServerIPList").split("\\|");
		String[] portList = SmartUXProperties.getProperty("cache.ServerPortList").split("\\|");

		int count = ipList.length;

		for (int i=0; i<count; i++) {
			StringBuilder key = new StringBuilder();
			key.append(ipList[i]).append(":").append(portList[i]);
			Map<String, Integer> values = new HashMap<String, Integer>();

			values.put("index", i);
			values.put("total", count);

			map.put(key.toString(), values);
		}

		String ip = getSystemProperty("JBOSS_IP");
		String port = getSystemProperty("JBOSS_PORT");

		StringBuilder address = new StringBuilder();
		address.append(ip.isEmpty() ? ipList[0] : ip).append(":").append(port.isEmpty() ? portList[0] : port);

		return map.get(address.toString());

	}
    
    
    /**
	 * 현재 관리서버 인덱스를 가져온다
     * 서버 환경변수 server.index 사용
     *
	 * @return HashMap key(index:인덱스, total: WAS 총 개수)
	 */
	public static Map<String, Integer> getServerAdminIndex() {
		Map<String, Map<String, Integer>> map = new HashMap<String, Map<String, Integer>>();

		String[] ipList = SmartUXProperties.getProperty("myturn.AdminServerIPList").split("\\|");
		String[] portList = SmartUXProperties.getProperty("myturn.AdminServerPortList").split("\\|");

		int count = ipList.length;

		for (int i=0; i<count; i++) {
			StringBuilder key = new StringBuilder();
			key.append(ipList[i]).append(":").append(portList[i]);
			Map<String, Integer> values = new HashMap<String, Integer>();

			values.put("index", i);
			values.put("total", count);

			map.put(key.toString(), values);
		}

		String ip = getSystemProperty("JBOSS_IP");
		String port = getSystemProperty("JBOSS_PORT");

		StringBuilder address = new StringBuilder();
		address.append(ip.isEmpty() ? ipList[0] : ip).append(":").append(port.isEmpty() ? portList[0] : port);

		return map.get(address.toString());

	}

	public static String callHttpClientPost(String host, int port, String url, String param, int timeout, String acceptHeader, String protocolName, String encoding) throws Exception{

		String response ="";

		try{
			DefaultHttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter("http.connection.timeout", timeout);

			String uri = "";
			uri = protocolName+"://"+host+":"+Integer.toString(port)+url;

			HttpPost post = new HttpPost(uri);
			post.setHeader("accept", acceptHeader);
			post.setEntity(new StringEntity(param,encoding));

			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			response = httpClient.execute(post,responseHandler);

		}catch(Exception e){
			throw e;
		}

		return response;
	}
	
	public static String callHttpClientPost(String host, int port, String url, String param, int timeout, String acceptHeader, String protocolName, String encoding, String contentType) throws Exception{ 
		
		String response ="";
		
		try{
        DefaultHttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter("http.connection.timeout", timeout);
        
        String uri = "";	
        uri = protocolName+"://"+host+":"+Integer.toString(port)+url;
        
        HttpPost post = new HttpPost(uri); 
        post.setHeader("accept", acceptHeader); 
        post.setHeader("Content-type", contentType);
        post.setEntity(new StringEntity(param,encoding)); 

        ResponseHandler<String> responseHandler = new BasicResponseHandler(); 
        response = httpClient.execute(post,responseHandler); 
        
		}catch(Exception e){
			throw e;
		}

		return response;
    }
	
	/**
	 * 해당 파일을 다른곳으로 이동한다.
	 * @param srFile
	 * @param dtFile
	 * @return
	 */
	public static boolean moveFile(File srFile, File dtFile){
		boolean result = true;
		try{
			if(srFile.renameTo(dtFile)==false){
				InputStream in = new FileInputStream(srFile);

				OutputStream out = new FileOutputStream(dtFile);

				byte[] buf = new byte[1024];
				int len;

				while ((len = in.read(buf)) > 0){
					out.write(buf, 0, len);
				}

				in.close();
				out.close();
				srFile.delete();
			}
		}catch(FileNotFoundException ex){
			System.out.println(ex.getMessage() + " in the specified directory.");
			//throw ex;
			result=false;
		}
		catch(IOException e){
			System.out.println(e.getMessage());
			result=false;
		}	
		
		return result;
	}
	

	
	/**
	 * 한글 포함 여부 확인
	 * @param str
	 * @return
	 */
	public static boolean containsHangul(String str)
	{
	    for(int i = 0 ; i < str.length() ; i++)
	    {
	        char ch = str.charAt(i);
	        Character.UnicodeBlock unicodeBlock = Character.UnicodeBlock.of(ch);
	        if(UnicodeBlock.HANGUL_SYLLABLES.equals(unicodeBlock) ||
	                UnicodeBlock.HANGUL_COMPATIBILITY_JAMO.equals(unicodeBlock) ||
	                UnicodeBlock.HANGUL_JAMO.equals(unicodeBlock))
	            return true;
	    }
	    return false;
	}

    /**
     * 자리수를 입력하면 해당 포맷에 맞게 리턴한다
     * data : 1, digit : 3 => 001
     * data : 1, digit : 2 => 01
     *
     * @param data 데이터
     * @param digit 자리수
     * @return 포맷된 값
     */
    public static String pad(String data, int digit) {
        StringBuilder result = new StringBuilder();
        int length = data.length();

        if (digit > length) {
            for (int i=0; i<digit - length; i++) {
                result.append("0");
            }

            result.append(data);
        } else {
            result.append(data);
        }

        return result.toString();
    }

	/**
	 * 데이터에 find 항목이 존재하는지 체크
	 *
	 * (ex : data = CP10||CP20||
	 *  find : CP20 = true, CP200 = false
	 *  delimiter \\|\\|
	 * )
	 *
	 * @param data 데이터
	 * @param find 찾을값
	 * @param delimiter 구분자
	 * @return 결과가 존재하면 true
	 */
	public static boolean contains(String data, String find, String delimiter) {
		boolean result = false;

		String[] ary = data.split(delimiter);

		for (String s : ary) {
			if (s.equals(find)) {
				result = true;
				break;
			}
		}

		return result;
	}

	/**
	 * Json파싱할 때 쌍따옴표가 에서 문제가 없도록 하기 위해서 문자열을 " -> \\"로 치환
	 *
	 * @param str 문자열
	 * @return 치환된 문자열
	 */
	public static String replaceDoubleQuotation(String str) {
		if (StringUtils.isEmpty(str)) {
			str = "";
		}

		str = str.replace("\"", "\\\\\\\"");

		return str;
	}
	
	/**
	 * JSONObject에서 String을 추출
	 * @param object JSONObject
	 * @param key Key
	 * @return
	 */
	public static String getJsonString(JSONObject object, String key) {
        String result = "";
        if (object.containsKey(key)) {
        	result = object.get(key) != null?object.get(key).toString():"";
        }
        return result;
    }
	
	/**
	 * 스케줄 또는 캐시 동기화시에 프로그램이 실행될 차례인지 아닌지를 return 2016.04.21
	 * @param timeType 추가
	 * @return
	 */
	public static boolean isMyTurn(int timeType)
	{
		Calendar cal = Calendar.getInstance();
		int value = cal.get(timeType);
		
		boolean retFlag = false;
		
		Map<String, Integer> map = GlobalCom.getServerIndex();
		int index = map.get("index");
		int total = map.get("total");

		if((value % total) == index)
		{
			retFlag = true;
		}
		
		return retFlag;
	}
	
	/**
	 * 스케줄 또는 캐시 동기화시에 프로그램이 실행될 차례인지 아닌지를 return 2016.04.21
	 * @param timeType 추가
	 * @return
	 */
	public static boolean isAdminMyTurn(int timeType)
	{
		Calendar cal = Calendar.getInstance();
		int value = cal.get(timeType);
		
		boolean retFlag = false;
		
		Map<String, Integer> map = GlobalCom.getServerAdminIndex();
		int index = map.get("index");
		int total = map.get("total");
		
		if((value % total) == index)
		{
			retFlag = true;
		}
		
		return retFlag;
	}
	
	@SuppressWarnings("ResultOfMethodCallIgnored")
    public static void makeParentDir(String filePath) {
        if (StringUtils.isNotBlank(filePath)) {
            String folderPath = FilenameUtils.getFullPath(filePath);

            if (StringUtils.isNotBlank(folderPath)) {
                File folder = new File(folderPath);

                if (!folder.exists()) {
                    folder.mkdirs();
                }
            }
        }
    }
	
	/**
     * 시스템 환경변수에 설정된 서버 인덱스를 조회한다
     *
     * @return 서버 인덱스
     */
    public static int getServerIndexNo() {
        return NumberUtils.toInt(GlobalCom.getSystemProperty("server.index"), 1);
    }
	
	/**
     * 서버인덱스을 포함한 경로 변환
     *
     * @param folderPath 폴더경로
     * @param fileName 파일명
     * @return 폴더경로 + 파일명 + _서버인덱스
     */
    public static String getFilePathWithServerIndex(String folderPath, String fileName) {
        StrBuilder sb = new StrBuilder();
        sb.append(folderPath);
        
        if (StringUtils.isNotBlank(folderPath)) {
            if (!folderPath.endsWith(File.separator)) {
                sb.append(File.separator);
//                sb.append("/");
            }

            sb.append(fileName).append("_").append(getServerIndexNo());
        }

        return sb.toString();
    }
	
	/*
	 * 이미지 헤더 포맷 체크
	 * 
	*/
	public static String imgFormatCheck(MultipartFile path) throws IOException{

		String imgFormat = "";
		
		ImageInputStream iis = ImageIO.createImageInputStream(path.getInputStream());
		Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
		
	   if (!iter.hasNext()){
	    System.out.println("imgFormatCheck >>> no readers found!");
	   }
	   
	   ImageReader reader = iter.next();
	   System.out.println("format: " + reader.getFormatName());
	   imgFormat = reader.getFormatName().toLowerCase();
		   
	   iis.close();
			
		return imgFormat;
	}
	
	/*
	 * 이미지 헤더 사이즈 체크
	 * 
	*/
	public static String imgSizeCheck(MultipartFile path, String sizeType, int maxWidth, int maxHeight) throws IOException{
		int imgW = 0;
		int imgH = 0;
		String result = "0";
		
		ImageInputStream iis = ImageIO.createImageInputStream(path.getInputStream());
		Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
		
	   if (!iter.hasNext()){
	    System.out.println("imgSizeCheck >>> no readers found!");
	   }
	   
	   BufferedImage bi = ImageIO.read( path.getInputStream());
	   imgW = bi.getWidth();
	   imgH = bi.getHeight();
		
		System.out.println("imgW >>>" + imgW);
		System.out.println("imgH >>>" + imgH);
		
		if(sizeType.equals("max")) { //type이 max라면 작거나 같은 사이즈까지 모두 체크
			if (imgW <= maxWidth && imgH <= maxHeight) {
				result = "1";  // 가로 * 세로 사이즈가 모두 작거나 같으면 1
			} else if (imgW > maxWidth && imgH <= maxHeight) {
				result = "2"; // 가로가 크고 세로는 작거나 같다면 3
			} else if (imgW <= maxWidth && imgH > maxHeight) {
				result = "3"; // 가로는 작거나 같고 세로는 크다면 2 
			} else if (imgW > maxWidth && imgH > maxHeight) {
				result = "4";  // 가로 세로 모두 크다면 4
			}
		} else if(sizeType.equals("equal")) { //type이 equal이라면 같은 사이즈만 체크
			if (imgW == maxWidth && imgH == maxHeight) {
				result = "1";  // 가로 * 세로 사이즈가 같으면 1
			} else if (imgW == maxWidth && imgH != maxHeight) {
				result = "2"; // 가로는 같고 세로는 같지 않다면 2
			} else if (imgW != maxWidth && imgH == maxHeight) {
				result = "3"; // 가로가 같지 않고 세로는 같다면 3
			} else if (imgW != maxWidth && imgH != maxHeight) {
				result = "4";  // 가로 세로 같지 않다면 4
			}
		}
		
		iis.close();
			
		return result;
	}
    
}
