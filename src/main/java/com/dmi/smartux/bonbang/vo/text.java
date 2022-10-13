package com.dmi.smartux.bonbang.vo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

public class text {

//	public static void main(String[] args) {
//		String test = "0123456";
//		String date = "20120404203000";
//	    System.out.println(date.substring(8));
//	    
//		Calendar oCalendar = Calendar.getInstance( );
//		//                         0     1     2     3     4     5     6     
//	    final String[] weeks = { "일", "월", "화", "수", "목", "금", "토" };
//
//	    int week = oCalendar.get(Calendar.DAY_OF_WEEK)-1;
//	    //int week = 5;
//	    System.out.println("현재 요일: " + (week));
//	    System.out.println("현재 요일: " + weeks[oCalendar.get(Calendar.DAY_OF_WEEK) - 1] + "요일");
//	    
//	    
//	    for(int i=0;i<test.length();i++){
//			System.out.println(test.substring(i,i+1));
//			int _weekChk =  Integer.parseInt(test.substring(i,i+1));
//			if (_weekChk < week) {
//				
//				System.out.println("작은 요일 : "+getDate((_weekChk - week)+7) );
//			}else if(week == _weekChk){
//				System.out.println("현재 시간 : "+getTodayFormat4_24().substring(8) );
//				System.out.println("데이터 시간 : "+date.substring(8) );
//				
//				if(Integer.parseInt(getTodayFormat4_24().substring(8)) >= Integer.parseInt(date.substring(8))){
//					System.out.println("같은 요일 시간 지남 : "+getDate(7) );
//				}else{
//					System.out.println("같은 요일 시간 지나지 않음 : "+getDate(0) );
//				}
//				
//				
//			}else{
//				System.out.println("다른 요일 : "+getDate(_weekChk - week) );
//			}
//		}
	    
	    
		
		
//		// Calendar 객체 생성.
//        Calendar c = Calendar.getInstance();
//        // 뽑아낼 요일 (일 : 1, 월 : 2, 화 : 3, 수 : 4, 목 : 5, 금 : 6, 토 : 7)
//
//        // 난 월요일 뽑을 거임.
//        int iDay = 2;
//        // 각 월 1일
//        int fDay = 14;
//        // 일주일 기준
//        int week = 7;
//        // 날짜 세팅 (아무 날짜나.. Day는 기준으로 할 날짜! : 기준날짜의 이후로만 뽑아옴)
//        c.set(1, 2012);
//        c.set(2, 6);
//        c.set(5, fDay);
//        
//        System.out.println("Calendar == " + c.get(Calendar.DAY_OF_WEEK));
//        
//        // 첫날의 요일과 뽑아낼 요일을 비교함
//        // 첫날의 요일이 작으면,
//        if (c.get(Calendar.DAY_OF_WEEK) < iDay) {
//            // 첫날의 요일에 (뽑아낼 요일 - 첫날의 요일)을 더한 날짜를 계산함
//            c.set(5, (iDay - c.get(Calendar.DAY_OF_WEEK)) + fDay);
//            System.out.println("C.day1 ==" + c.get(5));
//        // 첫날의 요일이 같으면,
//        } else if (c.get(Calendar.DAY_OF_WEEK) == iDay) {
//            // 그대로 세팅
//            c.set(5, fDay);
//            System.out.println("C.day2 ==" + c.get(5));
//        // 첫날의 요일이 크면,
//        } else {
//            // ((일주일 (7) + 뽑아낼 요일 (2)) - 첫날의 요일) + 날짜를 계산함
//            // 즉 다음주가 선택됨.
//            c.set(5, ((week + iDay) - c.get(Calendar.DAY_OF_WEEK)) + fDay);
//            System.out.println("C.day3 ==" + c.get(5));
//        }
//	}
	
	public static String getYyyymmdd(Calendar cal) {
	     Locale currentLocale = new Locale("KOREAN", "KOREA");
	     String pattern = "yyyyMMdd";
	     SimpleDateFormat formatter = new SimpleDateFormat(pattern, currentLocale);
	     return formatter.format(cal.getTime());
	}
	
	public static String getDate ( int iDay ){
		Calendar temp=Calendar.getInstance ( );
		StringBuffer sbDate=new StringBuffer ( );
	
		temp.add ( Calendar.DAY_OF_MONTH, iDay );
	
		int nYear = temp.get ( Calendar.YEAR );
		int nMonth = temp.get ( Calendar.MONTH ) + 1;
		int nDay = temp.get ( Calendar.DAY_OF_MONTH );
	
		sbDate.append ( nYear );
		if ( nMonth < 10 )
		sbDate.append ( "0" );
		sbDate.append ( nMonth );
		if ( nDay < 10 )
		sbDate.append ( "0" );
		sbDate.append ( nDay );
	
		return sbDate.toString ( );
	}
	
	public static String getTodayFormat4_24() {
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(dt);
	}	
	
	
	
	
//	public static void main(String[] args) {
//		  String s = "|o|test";
//
//	      StringTokenizer st = new StringTokenizer(s, "|");
//	      int cnt = st.countTokens();
//	      System.out.println(cnt);
//	      while (st.hasMoreTokens())
//	      {
//	         String test = st.nextToken();
//	         System.out.println(test);
//	      }
//	}
	
	public static void main(String[] args) {
		String s = "||";
		
		String [] arr = s.split("\\|\\|");
		System.out.println(arr.length);
		for(int i=0;i<arr.length;i++){
			System.out.println(arr[i]);
		}
	}
}
