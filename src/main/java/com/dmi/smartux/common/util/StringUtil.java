package com.dmi.smartux.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtil {

	public String getCurDttm( String i_sFormat )
	{
		
		SimpleDateFormat sdf = null;
		
		sdf = new SimpleDateFormat(i_sFormat);
		
		return sdf.format( new java.util.Date() );
	}

	public Date getDate(String pattern, String strDate )
	{
		
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		Date sDate;
		try {
			sDate = formatter.parse(strDate);
		} catch (ParseException e) {
			sDate = null;
		}
		return sDate;
	}

	/**
	 * strDate2가 strDate1보다 이전이거나 같을 경우에만 true
	 * @param pattern
	 * @param strDate1
	 * @param strDate2
	 * @return
	 */
	public boolean getDateComparison(String pattern, String strDate1, String strDate2)
	{
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		Date sDate1;
		Date sDate2;
		
		boolean returnValue = false;

		try {
			sDate1 = formatter.parse(strDate1);
			sDate2 = formatter.parse(strDate2);
			
			if(sDate1.before(sDate2)) 
	        {
				returnValue = false;
	        }
	        else
	        {
	        	returnValue = true;
	        }
		} catch (ParseException e) {
			returnValue = false;
		}
		
		return returnValue;
	}
	
}
