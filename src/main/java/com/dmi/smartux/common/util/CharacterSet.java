package com.dmi.smartux.common.util;

/**
 * @author Administrator
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
import java.lang.*;
import java.io.*;

public class CharacterSet 
{
	    private final static boolean bEncodingCheck = true;
	
	    public static String toKorean(String s) 
		{
			try 
			{
				return new String(s.getBytes("UTF-8"), "ISO-8859-1");
			} 
				catch (UnsupportedEncodingException e) 
			{
				return null;
			} 
				catch (NullPointerException e) 
			{
				return null;
			}
	    }

	    public static String toKoreanQuery(String str) 
		{
			try 
			{
				return new String(str.getBytes(), "EUC_KR");
			} 
				catch (UnsupportedEncodingException e) 
			{
				return null;
			} 
				catch (NullPointerException e) 
			{
				return null;
			}
	    }
	
		public static String ascToKsc(String str)
			throws UnsupportedEncodingException
		{
			if(bEncodingCheck == false)
				return str;
			else if(str == null || str.trim().length() == 0)
				return "";
			else
				return new String(str.trim().getBytes("8859_1"), "KSC5601");
		}
	
		public static String kscToAsc(String str)
			throws UnsupportedEncodingException
		{
			if(bEncodingCheck == false)
				return str;
			else if(str == null || str.trim().length() == 0)
				return "";
			else
				return new String(str.trim().getBytes("KSC5601"), "8859_1");
		}
}
