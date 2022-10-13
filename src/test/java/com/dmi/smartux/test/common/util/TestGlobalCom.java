package com.dmi.smartux.test.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.dmi.smartux.test.common.property.TestSmartUXProperties;

/**
 * 공통 유틸 클래스
 */
public class TestGlobalCom {
	
	public static final String rowsep="\f"; 	// 행 분리자
	public static final String colsep="!^";	// 열 분리자
	public static final String arrsep="\b";	// 배열 분리자
	public static final String rssep="!@";	// 레코드셋 분리자
	
	public static final String splitrowsep="\\f";		// java의 split 메소드에서 사용할때 행 분리자 
	public static final String splitcolsep="\\!\\^";	// java의 split 메소드에서 사용할때 열 분리자
	public static final String splitarrsep="\\b";		// java의 split 메소드에서 사용할때 배열 분리자
	public static final String splitrssep="\\!\\@";	// java의 split 메소드에서 사용할때 레코드셋 분리자
	
	private static FileWriter testfw;
	
	/**
	 * 테스트 작업시 로그 파일을 기록하는데 그 로그 파일을 여는 작업을 한다
	 */
	public static void testLogOpen(){
		if(testfw == null){
			try {
				testfw = new FileWriter(TestSmartUXProperties.getProperty("test.logfilepath"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}
		}
	}
	
	/**
	 * 테스트 작업시 로그 파일을 기록하는데 그 로그 파일을 닫는 작업을 한다
	 */
	public static void testLogClose(){
		if(testfw != null){
			try {
				testfw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}
		}
	}
	
	/**
	 * 테스트 작업시 로그 파일을 기록하는데 그 로그 파일에 내용을 기록할때 쓴다
	 */
	public static void testLogWrite(String strWrite) {
		if(TestSmartUXProperties.getProperty("test.uselog").equals("Y")){
			if(testfw != null){
				try {
					testfw.append(strWrite);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
				}
			}
		}
	}
	 
	public static String isNullCheck(String str) {
		if((str == null) || (str.trim().equals("")) || (str.trim().equalsIgnoreCase("null")) || (str.trim().length() == 0) || (str.equalsIgnoreCase("undefined")))
			return "";
		else
		     return str.trim();
	}
	
	public static String isNull(String str) {
		if((str == null) || (str.trim().equals("")) || (str.trim().equalsIgnoreCase("null")) || (str.trim().length() == 0) || (str.equalsIgnoreCase("undefined")))
			return "";
		else
			return str.trim();
	}
	
	
	public static String isNull(String str,String str2) {
		if((str == null) || (str.trim().equals("")) || (str.trim().equalsIgnoreCase("null")) || (str.trim().length() == 0) || (str.equalsIgnoreCase("undefined")))
			return str2;
		else
			return str.trim();
	}
	
	public static String isNullNumber(String str) {
		if((str == null) || (str.trim().equals("")) || (str.trim().equalsIgnoreCase("null")) || (str.trim().length() == 0) || (str.equalsIgnoreCase("undefined")))
			return "1";
		else
			return str.trim();
	}
	
	public static int isNumber(int num){
		if(num < 0 || num == 0){
			return 1;
		}else{
			return num;
		}
	}
	public static int isNumber(int num,int _num){
		if(num < 0 || num == 0){
			return _num;
		}else{
			return num;
		}
	}
	
	public static int isNewButtonCheck(String date){
		try{
			String today= getTodayFormat();			
			date=replace(date, "-", "");
			int check=Integer.parseInt(today)-Integer.parseInt(date);
			
			if(check >= 0 || check <= 7){				
				return 1;				
			}
			return 0;			
		}catch(Exception e){
			return 0;
		}
		
	}
	
	public static String getKenString(String str,int len)throws UnsupportedEncodingException{

		String fStr = str;
		int sLen = str.length();

		if(sLen > len){
		fStr = str.substring(0, len)+"...";
		}

		return fStr;			
	}
	
	 /*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    Function       + getTodayFormat
    Parameter      +
    Description    + 오늘 날짜를 반환한다. (yyyyMMdd)
    ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	public static String getTodayFormat() {
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(dt);
	}
	 /*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    Function       + getTodayFormat
    Parameter      +
    Description    + 오늘 날짜를 반환한다. (yyyy-MM-dd)
    ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	public static String getTodayFormat2() {
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(dt);
	}
	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    Function       + getTodayFormat
    Parameter      +
    Description    + 오늘 날짜를 반환한다. (yyyy-MM-dd-hh-mm-ss)
    ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	public static String getTodayFormat3() {
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
		return sdf.format(dt);
	}	
	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    Function       + getTodayFormat
    Parameter      +
    Description    + 오늘 날짜를 반환한다. (yyyyMMddhhmmss)
    ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	public static String getTodayFormat4() {
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		return sdf.format(dt);
	}	
	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    Function       + getTodayYear
    Parameter      +
    Description    + 오늘 년도를 반환한다. (yyyy)
    ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	public static String getTodayYear() {
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		return sdf.format(dt);
	}
	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    Function       + getTodayMonth
    Parameter      +
    Description    + 오늘 월를 반환한다. (MM)
    ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	public static String getTodayMonth() {
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM");
		return sdf.format(dt);
	}
	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    Function       + getTodayDay
    Parameter      +
    Description    + 오늘 날짜를 반환한다. (dd)
    ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	public static String getTodayDay() {
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd");
		return sdf.format(dt);
	}	
	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    Function       + getTodayFormat
    Parameter      +
    Description    + 오늘 요일을 반환한다.(문자형식) (월요일)
    ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	public static String getTodayCalendar(){
		Calendar cal = Calendar.getInstance();
		String[] week = {"일","월","화","수","목","금","토"};
		
		return week[cal.get(Calendar.DAY_OF_WEEK) - 1]+"요일";
	}
	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    Function       + getTodayFormat
    Parameter      +
    Description    + 오늘 요일을 반환한다.(숫자형식) (1)
    ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	public static int getTodayCalendar2(){
		Calendar cal = Calendar.getInstance();
		int[] week = {1,2,3,4,5,6,7};	//참조 : 일요일은 1 토요일은 7
		
		return week[cal.get(Calendar.DAY_OF_WEEK) - 1];
	}
	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    Function       + getTodayFormat
    Parameter      +
    Description    + 숫자형식 요일을 문자형식 요일로 변환하여 반환한다 (1:일요일~~~)
    ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	public static String getTodayCalendar(int i_week){
		//String[] week = {"일","월","화","수","목","금","토"};
		String week="";
		if(i_week == 1){
			week ="일";
		}else if(i_week == 2){
			week ="월";
		}else if(i_week == 3){
			week ="화";
		}else if(i_week == 4){
			week ="수";
		}else if(i_week == 5){
			week ="목";
		}else if(i_week == 6){
			week ="금";
		}else if(i_week == 7){
			week ="토";
		}
		
		return week+"요일";
	}
	
    /*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    Function       + getSelected
    Parameter      + param1, param2
    Description    + 두 값을 비교해서 같으면 "Selected" 를 반환한다.
    ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	public static String getSelected(String param1, String param2) {
		if(param1.equals(param2))
			return "Selected";
		else
			return "";
	}

    /*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    Function       + getChecked
    Parameter      + param1, param2
    Description    + 두 값을 비교해서 같으면 "Checked" 를 반환한다.
    ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	public static String getChecked(String param1, String param2) {
		if(param1.equals(param2))
			return "Checked";
		else
			return "";
	}
	/*파일 업로드취약점 
	*/
	public int fileUploadCheck(String fileName){
		int result=0;
		String check = fileName.substring(fileName.lastIndexOf("."));

		if(check.equalsIgnoreCase(".php")||check.equalsIgnoreCase(".php3")||check.equalsIgnoreCase(".asp")||check.equalsIgnoreCase(".jsp")||
				check.equalsIgnoreCase(".cgi")||check.equalsIgnoreCase(".inc")||check.equalsIgnoreCase(".pl")||check.equalsIgnoreCase(".exe")
				||check.equalsIgnoreCase(".sh")||check.equalsIgnoreCase(".bat")){
			result = 1;
		}else{
			result = 0;
		}
		
		return result;
	}
	/*이미지파일업로드 체크 
	*/
	public int fileUploadCheckJpg(String fileName){
		int result=0;
		String check = fileName.substring(fileName.lastIndexOf("."));
		
		if(check.equalsIgnoreCase(".jpg")||check.equalsIgnoreCase(".jpeg")||check.equalsIgnoreCase(".gif")){
			result = 1;
		}else{
			result = 0;
		}
		
		return result;
	}
	/*동영상파일업로드 체크 
	*/
	public int fileUploadCheckMovie(String fileName){
		int result=0;
		String check = fileName.substring(fileName.lastIndexOf("."));
		
		if(check.equalsIgnoreCase(".wmv")||check.equalsIgnoreCase(".avi")){
			result = 1;
		}else{
			result = 0;
		}
		
		return result;
	}
	/*파일 확장자 가져오기 
	*/
	public String fileUploadExt(String fileName){
		String check = fileName.substring(fileName.lastIndexOf("."));
		check = TestGlobalCom.replace(check, ".", "");
				
		return check;
	}	
	
	/*문자 치환 replace
	* "123" ,"2", ""  -> "13"
	*/
	public static String replace(String _src, String _target, String _dest){
		if (_src == null || _src.trim().length()==0) return null;
		if (_target == null) return _src;

		StringBuffer tmpBuffer = new StringBuffer();

		int nStart = 0;
		int nEnd = 0;
		int nLength = _src.length();
		int nTargetLength = _target.length();
			
		while(nEnd < nLength){
			nStart = _src.indexOf(_target, nEnd);
			if (nStart < 0){
				tmpBuffer.append(_src.substring(nEnd, nLength));
					
				break;
			}else{
				tmpBuffer.append(_src.substring(nEnd, nStart)).append(_dest);
						
				nEnd = nStart + nTargetLength;
			}				
		}

		return tmpBuffer.toString();
	}

	/*스크립트 처리 유틸 
	*/
	public static String getScriptAlert(String check_code){
		check_code=isNullCheck(check_code);
		String result="";
		if(check_code.equals("1")){
			result="<script type='text/javascript'>alert('정상적으로 등록이 처리되었습니다.')</script>";
		}else if(check_code.equals("2")){
			result="<script type='text/javascript'>alert('정상적으로 수정이 처리되었습니다.')</script>";
		}else if(check_code.equals("3")){
			result="<script type='text/javascript'>alert('정상적으로 삭제가 처리되었습니다.')</script>";
		}else if(check_code.equals("4")){
			result="<script type='text/javascript'>alert('처리중 오류가 발생되었습니다.')</script>";
		}else if(check_code.equals("5")){
			result="<script type='text/javascript'>alert('이미 투표를 참여하셨습니다.')</script>";
		}else if(check_code.equals("6")){
			result="<script type='text/javascript'>alert('기존에 가입을 하셨습니다.')</script>";
		}else if(check_code.equals("7")){
			result="<script type='text/javascript'>alert('회원가입이 정상적으로 완료되었습니다. 감사합니다.')</script>";
		}else if(check_code.equals("8")){
			result="<script type='text/javascript'>alert('메일이 정상적으로 발송되었습니다')</script>";
		}else if(check_code.equals("9")){
			result="<script type='text/javascript'>alert('선택은 1개이상 가능합니다')</script>";
		}else if(check_code.equals("10")){
			result="<script type='text/javascript'>alert('선택은 2개이상 가능합니다')</script>";
		}else if(check_code.equals("11")){
			result="<script type='text/javascript'>alert('기존 연구실적 메인 노출 데이터가 2개이상 존재합니다.메인삭제를 해주십시오')</script>";
		}else if(check_code.equals("12")){
			result="<script type='text/javascript'>alert('1개만 선택하여주십시오.')</script>";
		}else if(check_code.equals("13")){
			result="<script type='text/javascript'>alert('정상적으로 적용되었습니다.')</script>";
		}else if(check_code.equals("14")){
			result="<script type='text/javascript'>alert('아이디 및 비밀번호를 확인해주세요.')</script>";
		}else if(check_code.equals("15")){
			result="<script type='text/javascript'>alert('기존에 아이디가 존재합니다.')</script>";
		}else if(check_code.equals("16")){
			result="<script type='text/javascript'>alert('로그인후 사용해 주시기 바랍니다.')</script>";
		}else if(check_code.equals("17")){
			result="<script type='text/javascript'>alert('메일이 정상적으로 발송되지 않았습니다.')</script>";
		}else if(check_code.equals("18")){
			result="<script type='text/javascript'>alert('메일이 메일형식이 잘못되었습니다.')</script>";
		}else if(check_code.equals("19")){
			result="<script type='text/javascript'>alert('회원정보가 없습니다. 다시 확인해 주시기 바랍니다.')</script>";
		}else if(check_code.equals("20")){
			result="<script type='text/javascript'>alert('정상적으로 상담되었습니다. 답변은 메일로 보내드립니다.')</script>";
		}
		
		
		return result;
	}
	
	/*스크립트 처리 유틸 
	*/
	public static String getScriptAlertMove(String check_code,String return_url){
		check_code=isNullCheck(check_code);
		String result="";
		
		if(check_code.equals("1")){
			result="<script type='text/javascript'>alert('메일이 메일형식이 잘못되었습니다.');location.href='"+return_url+"';</script>";
		}
		
		
		return result;
	}
	
	/**
	 * 데이터 형이 숫자인지 문자인지 체크하는 메소드
	 * @param value 숫자면 true 문자면 false
	 * @return boolean
	 */
	public static boolean checkInt(String value) {
		boolean returnVal = false;
		int a = 0;
		int b = 0;
		for (int i = 0; i < value.length(); i++) {
			char c = value.charAt(i);
			if (0x30 <= c && c <= 0x39) {
				a++;
			}else{
				b++;
			}
		}
		if(a>0){
			returnVal = true;
		}else if(b>0){
			returnVal = false;
		}
		return returnVal;
	}
	
	/**
	 * 영문/ 숫자 존재 여부 체크 메서드	 * 
	 * @param value
	 * @return 0:영문/숫자 미존재 1:영문/숫자 존재 2:영문 존재 3:숫자 존재
	 */
	public static int checkStrInt(String value){
		int type = 0; 	//0:영문/숫자 미존재 1:영문/숫자 존재 2:영문 존재 3:숫자 존재
		int type1 = 0; //영문
		int type2 = 0; //숫자
		for(int i=0;i<value.length();i++){
			char c=value.charAt(i);
			//영문
			if( ( 0x61 <= c && c <= 0x7A ) || ( 0x41 <= c && c <= 0x5A ) ){
				type1 = 1;
			//숫자
			}else if( 0x30 <= c && c <= 0x39 ){
				type2 = 1;
			}
		}
		
		if(type1 == 0 && type2 == 0){	//영문 숫자 미존재
			type = 0;
		}else if(type1 == 1 && type2 == 0){		//영문만 존재
			type = 2;
		}else if(type1 == 0 && type2 == 1){		//숫자만 존재
			type = 3;
		}else{	//영문/숫자 존재
			type = 1;	
		}
		
		return type;
	}
	
	/**
	 * System.out.print를 줄이기 위한 메소드
	 * 
	 * @param value
	 */
	public static void nPrint(String value) {
		System.out.println(value);
	}
	
	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    Function       + getSplit
    Parameter      + str, param
    Description    + str 문자를 param 구분자로 Split 한 후 배열로 반환한다.
    ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	public static String[] getSplit(String str, String param) {
		
		StringTokenizer st = new StringTokenizer(str, param);
		String[] split = new String[st.countTokens()];
		int i = 0;

		while(st.hasMoreTokens()) {
			split[i] = st.nextToken();
			i++;
		}
		return split;
	}
/**
 * session NULL 체크
 */	
	String getSession(HttpSession session, String attrName)
    {
        return session.getAttribute(attrName) != null ? (String)session.getAttribute(attrName) : "";
    }
	
	public static String getUniqueFileName(String path, String file) {
		  File tmp = new File(path + file.toLowerCase());
		  String fileName =  file.toLowerCase();
		  int i = 0;

		  System.out.println("------------------>exist"+tmp.exists());
		  if(tmp.exists()) {
		    while(tmp.exists()) {
		      if(fileName.indexOf(".") != -1){
		        String lcTemp = "(" + i + ").";
		        fileName = file.toLowerCase().replaceAll(".", lcTemp);
		      }else{
		        fileName =  file.toLowerCase() + "(" + i + ")";
		        tmp = new File(path + fileName);
		        i++;
		      }
		    }
		  }
		  return fileName;
	}

	/**
	 * 이메일 주소 유효성 체크
	 * @param email
	 * @return boolean
	 */
	public static boolean isEmail(String email) {
	     if (email==null) return false;
	     boolean b = Pattern.matches(
	         "[\\w\\~\\-\\.]+@[\\w\\~\\-]+(\\.[\\w\\~\\-]+)+",
	         email.trim());
	     return b;
	}
	
	/**
	 * 현재 유닉스 타임 가져오기
	 * @return
	 */
	public static long getTodayUnixtime(){
		return System.currentTimeMillis()/1000;
	}
	
	/**
	 * 현재 유닉스 타임 + 초
	 * @param secend
	 * @return
	 */
	public static long getTodayUnixtime(int secend){
		return System.currentTimeMillis()/1000+secend;
	}
	
	/**
	 * 날짜 더하기 메서드  
	 * @param dateString	YYYYMMDD
	 * @param addDate		더할 날짜 (3 -> 3일)
	 * @return YYYYMMDD
	 */
	public static String getTodayAddDate(String dateString,int addDate){
		 String result = "";
		
		 //String dateString = "20120301";
	     SimpleDateFormat formatter = new SimpleDateFormat ("yyyyMMdd");
	     try {
			Date date = formatter.parse(dateString);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
		    calendar.add(Calendar.DAY_OF_MONTH, +(addDate-1));
		    result = formatter.format(calendar.getTime());
		} catch (ParseException e) {}
	    
	    return result; 
	}
	
	
	public static void main(String[] args) {
		
		String value = "한글000";
		
		int type = 0; 	//0:영문/숫자 미존재 1:영문/숫자 존재 2:영문 존재 3:숫자 존재
		int type1 = 0; //영문
		int type2 = 0; //숫자
		for(int i=0;i<value.length();i++){
			char c=value.charAt(i);
			//영문
			if( ( 0x61 <= c && c <= 0x7A ) || ( 0x41 <= c && c <= 0x5A ) ){
				type1 = 1;
			//숫자
			}else if( 0x30 <= c && c <= 0x39 ){
				type2 = 1;
			}
		}
		
		if(type1 == 0 && type2 == 0){	//영문 숫자 미존재
			type = 0;
		}else if(type1 == 1 && type2 == 0){		//영문만 존재
			type = 2;
		}else if(type1 == 0 && type2 == 1){		//숫자만 존재
			type = 3;
		}else{	//영문/숫자 존재
			type = 1;	
		}
		
		System.out.println(type);
	}
	
	/**
	 * 전체 자릿수와 숫자형 데이터를 입력받아 앞에 0을 붙인다
	 * ex) appendLeftZero("1", 4) -> 0001 
	 * @param number 숫자형 데이터
	 * @param size   전체 자릿수
	 * @return
	 */
	public static String appendLeftZero(String number, int size){
		String result = number;
		int end = size - number.length();
		for(int i=0; i < end; i++){
			result = "0" + result;
		}
		
		return result;
		
	}
	
	/**
	 * 프로퍼티 파일 경로와 프로퍼티 키를 입력받아 해당 프로퍼티 파일 안에 있는 키에 해당하는 값을 읽어온다
	 * @param filepath		프로퍼티 파일 경로
	 * @param key			입력받은 프로퍼티 파일 경로에 있는 프로퍼티 파일에 있는 프로퍼티 키 
	 * @return				프로퍼티 값
	 */
	public static String getProperties(String filepath, String key){
		String result = "";
		Properties props = null;
		FileInputStream fis = null;
		
		try {
            props = new Properties();
            fis = new FileInputStream(filepath);
            props.load(new java.io.BufferedInputStream(fis));
            result = props.getProperty(key).trim();
        }catch(Exception e) {
            
        }finally{
        	try{
        		fis.close();
        	}catch(Exception e){
        		
        	}
        }
            
		return result;
	}
	
	/**
	 * 프로퍼티 파일 경로를 입력받아 해당 프로퍼티 파일의 key 값들을 가져온다
	 * @param filepath		프로퍼티 파일 경로
	 * @return				프로퍼티 key값들
	 */
	public static List<String> getPropertiesKeyset(String filepath){
		List<String> result = new ArrayList<String>();
		Properties props = null;
		FileInputStream fis = null;
		
		
		try {
            props = new Properties();
            fis = new FileInputStream(filepath);
            props.load(new java.io.BufferedInputStream(fis));
            Set keyset = props.keySet();
            Iterator iter = keyset.iterator();
            while(iter.hasNext()){
            	String key = (String)(iter.next());
            	result.add(key);
            }
        }catch(Exception e) {
            
        }finally{
        	try{
        		fis.close();
        	}catch(Exception e){
        		
        	}
        }
            
		return result;
	}

	/**
	 * 스케쥴러를 이용하여 캐쉬작업을 진행할때 해당 캐쉬의 interval을 선택적으로 리턴해주는 함수
	 * (스케줄러로 호출한 경우엔 해당 프로퍼티에 셋팅된 interval 값을 리턴해주고 
	 * 스케줄러가 호출한 것이 아닐 경우엔 강제로 DB에서 읽어오는 옵션값을 확인해서 그 값이 true일 경우엔 -1(DB에서 읽어온뒤 캐쉬에 셋팅하고 리턴)
	 * , false 일 경우엔 0(무조건 캐쉬에서 읽음)을 리턴하도록 한다) 
	 * @param callByScheduler		scheduler 호출 여부(Y:Scheduler, N:단말, A:관리툴)
	 * @param intervalProperty		프로퍼티에서 읽은 interval값
	 * @return						캐쉬 interval 값
	 */
	public static long getCacheInterval(String callByScheduler, String intervalProperty){
		long interval = 0;
		
		String forceReadDB = TestSmartUXProperties.getProperty("cache.forceReadDB");		// 현제 셋팅이 강제로 DB에서 읽어오도록 되어 있는지를 확인
		
		if("N".equals(callByScheduler)){
			if("Y".equals(forceReadDB)){
				interval = -1;
			}else{
				interval = 0;
			}
		}else if("Y".equals(callByScheduler)){
			interval = Long.parseLong(intervalProperty);
		}else if("A".equals(callByScheduler)){					// 관리툴에서 호출을 했을 경우엔 DB 변경이 있은 뒤의 반영이므로 DB에서 조회한뒤 캐쉬에 반영하도록 한다
			interval = -1;
		}
		
		return interval;
	}
	
	/**
	 * 주소, 포트번호, url, 파라미터, 타임아웃을 넘겨서 해당 웹주소를 호출하는 함수
	 * @param host				호스트(ex: localhost)
	 * @param port				포트번호(ex:8080)
	 * @param url				URL(ex: /smartux/getThemeInfo)
	 * @param param				URL에 붙는 GET 방식 파라미터(ex:a=1234&b=4567)
	 * @param timeout			HttpConnection의 Timeout 설정
	 * @return					해당 웹페이지를 호출했을때 리턴되는 컨텐트 소스
	 * @throws Exception
	 */
	public static String callHttpClient(String host, int port, String url, String param, int timeout) throws Exception{
		String result = "";
		URI uri = null;
		HttpGet get = null;
		
		HttpClient httpClient = null;
		HttpResponse response = null;
		HttpEntity entity = null;
		InputStream is = null;
		StringBuffer sb = new StringBuffer();
		byte[] b = new byte[4096];

		try {
			
			// 넘겨받은 url안에 물음표가 있는 경우(ex : /smartux/getRealRatingByTheme?theme_code=T002)엔 넘겨받은 url 안에 파라미터가 있다를 것이므로
			// url을 재구성하여 파라미터 부분은 param 파라미터에 합치도록 한다
			// System.out.println("url : " + url);
			String [] urlparams = url.split("\\?");
			if(urlparams != null){
				// System.out.println("urlparams.length : " + urlparams.length);
				if(urlparams.length == 2){
					url = urlparams[0];
					param = param + "&" + urlparams[1];
					
					// System.out.println("final url : " + url);
					// System.out.println("final param : " + param);
					
				}
			}
			
			
			uri = URIUtils.createURI("http", host, port, url, param, null);
			get = new HttpGet(uri);
			
			httpClient = new DefaultHttpClient();
			
			
			HttpParams params = httpClient.getParams();
            params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            HttpConnectionParams.setConnectionTimeout(params, timeout);
            HttpConnectionParams.setSoTimeout(params, timeout);
			
			
			response = httpClient.execute(get);
			entity = response.getEntity();
			is = entity.getContent();
			sb = new StringBuffer();
			
			for(int n; (n =is.read(b)) != -1;){
				sb.append(new String(b, 0, n));
			}
			result = sb.toString();
			
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			throw e;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			throw e;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			throw e;
		}
		
		return result;
	}
	
	/**
	 * 주소, 포트번호, url, 파라미터, 타임아웃을 넘겨서 해당 웹주소를 호출하는 함수
	 * @param host				호스트(ex: localhost)
	 * @param port				포트번호(ex:8080)
	 * @param url				URL(ex: /smartux/getThemeInfo)
	 * @param param				URL에 붙는 GET 방식 파라미터(ex:a=1234&b=4567)
	 * @param timeout			HttpConnection의 Timeout 설정
	 * @return					해당 웹페이지를 호출했을때 리턴되는 컨텐트 소스
	 * @throws Exception
	 */
	public static String callHttpClient(String host, int port, String url, String param, int timeout, String acceptHeader) throws Exception{
		String result = "";
		URI uri = null;
		HttpGet get = null;
		
		HttpClient httpClient = null;
		HttpResponse response = null;
		HttpEntity entity = null;
		InputStream is = null;
		StringBuffer sb = new StringBuffer();
		byte[] b = new byte[4096];

		try {
			
			// 넘겨받은 url안에 물음표가 있는 경우(ex : /smartux/getRealRatingByTheme?theme_code=T002)엔 넘겨받은 url 안에 파라미터가 있다를 것이므로
			// url을 재구성하여 파라미터 부분은 param 파라미터에 합치도록 한다
			// System.out.println("url : " + url);
			String [] urlparams = url.split("\\?");
			if(urlparams != null){
				// System.out.println("urlparams.length : " + urlparams.length);
				if(urlparams.length == 2){
					url = urlparams[0];
					param = param + "&" + urlparams[1];
					
					// System.out.println("final url : " + url);
					// System.out.println("final param : " + param);
					
				}
			}
			
			
			uri = URIUtils.createURI("http", host, port, url, param, null);
			get = new HttpGet(uri);
			if(!(isNull(acceptHeader).equals(""))){
				get.setHeader("Accept", acceptHeader);
			}
			
			httpClient = new DefaultHttpClient();
			
			
			HttpParams params = httpClient.getParams();
            params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            HttpConnectionParams.setConnectionTimeout(params, timeout);
            HttpConnectionParams.setSoTimeout(params, timeout);
			
			
			response = httpClient.execute(get);
			entity = response.getEntity();
			is = entity.getContent();
			sb = new StringBuffer();
			
			for(int n; (n =is.read(b)) != -1;){
				sb.append(new String(b, 0, n));
			}
			result = sb.toString();
			
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			throw e;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			throw e;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			throw e;
		}
		
		// 로그 파일에 url과 파라미터와 결과를 기록한다
		testLogWrite(url + "?" + param + " : " + result + "\r\n");
		return result;
	}
	
	/**
	 * 서버의 단말 API를 호출하는 함수
	 * @param host			서버 ip 주소
	 * @param port			서버 포트번호
	 * @param url			단말 API URL
	 * @param param			단말 API에 사용되는 파라미터(Get 방식 연결 문자열)
	 * @param timeout		HttpClient timeout 시간
	 * @param retrycnt		호출 실패시 재시도 횟수
	 */
	public static void callAPI(String host, int port, String url, String param, int timeout, int retrycnt){
		String result = "";
		String retryresult = "";
		
		try{
			result = callHttpClient(host, port, url, param, timeout);
			if(result.length() >= 4){
				result = result.substring(0, 4);
			}
			
			if(!(TestSmartUXProperties.getProperty("flag.success").equals(result))){
			
				for(int i=0; i < retrycnt; i++){
					retryresult = TestGlobalCom.callHttpClient(host, port, url, param, timeout);
					if(retryresult.length() >= 4){
						retryresult = retryresult.substring(0, 4);
					}
					
					if(TestSmartUXProperties.getProperty("flag.success").equals(retryresult)){
						break;
					}
				}
			}
		}catch(Exception e){
			for(int i=0; i < retrycnt; i++){
				try{
					retryresult = TestGlobalCom.callHttpClient(host, port, url, param, timeout);
				}catch(Exception sube){
					
				}
				
				if(retryresult.length() >= 4){
					retryresult = retryresult.substring(0, 4);
				}
				
				if(TestSmartUXProperties.getProperty("flag.success").equals(retryresult)){
					break;
				}
			}
		}
	}
	
	
	
	/**
	 * 각 서버에 있는 단말 API를 호출하여 모든 서버의 캐쉬 메모리의 내용을 sync해주는 작업을 진행한다
	 * @param port		서버 포트번호
	 * @param url		단말에서 사용하는 API URL
	 * @param param		단말 API에 사용되는 파라미터(Get 방식 연결 문자열)
	 * @param timeout	HttpConnection의 Timeout 설정
	 * @param retrycnt	호출 실패시 재시도 횟수
	 */
	public static void syncServerCache(String url, String param, int timeout, int retrycnt){
		String localIP = "";						// 자기 자신의 서버의 단말 API는 호출이 되지 않도록 자기 자신의 IP를 먼저 가져오는 작업을 진행한다
		InetAddress thisIp;
		
		try {
			thisIp = InetAddress.getLocalHost();
			localIP = thisIp.getHostAddress();
			// System.out.println("localIP : " + localIP);
		} catch (UnknownHostException e1) {
			
		}
		String hostsString = TestSmartUXProperties.getProperty("cache.ServerIPList");
		String [] hostList = hostsString.split("\\|");
		String portsString = TestSmartUXProperties.getProperty("cache.ServerPortList");
		String [] portList = portsString.split("\\|");
		int idx = -1; 
		for(String host : hostList){
			idx++;
			if(host.equals(localIP)) continue;		// IP 목록에 자기 자신의 ip가 있을 경우 호출하지 않도록 하기 위해 건너뛰도록 한다
			callAPI(host, Integer.parseInt(portList[idx]), url, param, timeout, retrycnt);		// 다른 서버의 단말 API를 호출한다
		}
	}
	
	
	
	
	
}
