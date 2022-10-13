package com.dmi.smartux.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLCleaner {
	
	/** 
	* @author Shrek 
	* @param args 
	*/ 
	public static void main(String[] args) { 
//	 TODO Auto-generated method stub 
	HTMLCleaner cleaner = new HTMLCleaner(); 

	System.out.println(cleaner.clean("<html><head><script>aaaa</script></head><body><div>aaa</div> <div> <script></script></div><img src=\"http://tong.nate.com\" values=\">\"> 이건 어떻게 될까요 <!-- zmzm --></body></html> ")); 

	} 
	
	private static interface Patterns { 
//		 javascript tags and everything in between 
		public static final Pattern SCRIPTS = Pattern.compile("<(no)?script[^>]*>.*?</(no)?script>", Pattern.DOTALL); 

		public static final Pattern STYLE = Pattern.compile("<style[^>]*>.*</style>", Pattern.DOTALL); 
//		 HTML/XML tags 

		public static final Pattern TAGS = Pattern.compile("<(\"[^\"]*\"|\'[^\']*\'|[^\'\">])*>"); 

		public static final Pattern nTAGS = Pattern.compile("<\\w+\\s+[^<]*\\s*>"); 
//		 entity references 
		public static final Pattern ENTITY_REFS = Pattern.compile("&[^;]+;"); 
//		 repeated whitespace 
		public static final Pattern WHITESPACE = Pattern.compile("\\s\\s+"); 
	} 

	/** 
	* Clean the HTML input. 
	* SQL Injection 취약점 or 크로스 사이트 스크립팅 취약점
	*/ 
	public static String clean(String s) { 
		if (s == null) { 
		return null; 
		} 

		Matcher m; 

		m = Patterns.SCRIPTS.matcher(s); 
		s = m.replaceAll(""); 
		m = Patterns.STYLE.matcher(s); 
		s = m.replaceAll(""); 
		m = Patterns.TAGS.matcher(s); 
		s = m.replaceAll(""); 
		m = Patterns.ENTITY_REFS.matcher(s); 
		s = m.replaceAll(""); 
		m = Patterns.WHITESPACE.matcher(s); 
		s = m.replaceAll(" "); 
		
		s = LoginReplace(s);
		
		return s; 
	}
	
	public static String cleanwithoutLoginfunc(String s) { 
		if (s == null) { 
		return null; 
		} 

		Matcher m; 

		m = Patterns.SCRIPTS.matcher(s); 
		s = m.replaceAll(""); 
		m = Patterns.STYLE.matcher(s); 
		s = m.replaceAll(""); 
		m = Patterns.TAGS.matcher(s); 
		s = m.replaceAll(""); 
		m = Patterns.ENTITY_REFS.matcher(s); 
		s = m.replaceAll(""); 
		m = Patterns.WHITESPACE.matcher(s); 
		s = m.replaceAll(" "); 
		
		return s; 
	}
	
	/*로그인시 취약점 차단
	*/
	public static String LoginReplace(String str) {
			str = getReplaceString(str, "--", "");
			str = getReplaceString(str, "'", "");
			str = getReplaceString(str, "=", "");
			str = getReplaceString(str, ",", "");
			return str;
	}
	public static String getReplaceString(String str, String param1, String param2) {
			for(int i = 0; (i = str.indexOf(param1, i)) >= 0; i += param2.length())
				str = str.substring(0, i) + param2 + str.substring(i + param1.length());
			return str;
	}
	
	/*파입 업로드 취약점 차단 체크
	*/
	public int fileUploadCheck(String fileName){
		int result=0;
		String check = fileName.substring(fileName.lastIndexOf("."));
		
		if(check.equals(".php")||check.equals(".php3")||check.equals(".asp")||check.equals(".jsp")||check.equals(".cgi")||check.equals(".inc")||check.equals(".pl")){
			result = 1;
		}else{
			result = 0;
		}
		
		return result;
	}
	
	/*파일 다운로드 취약점 차단  
	 */
	public static String fileDownLoadCheck(String str) {
		str = getReplaceString(str, "./", "");
		str = getReplaceString(str, "../", "");	
		
		return str;
	}
}

