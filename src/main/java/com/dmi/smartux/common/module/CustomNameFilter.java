package com.dmi.smartux.common.module;

import java.io.File;
import java.io.FilenameFilter;

public class CustomNameFilter implements FilenameFilter {
	
	String type = null;
	String[] findName = null;
	
	public CustomNameFilter(String type, String[] findName){
		this.type = type;
		this.findName = findName;
	}
	
	public CustomNameFilter(String type, String findName){
		this.type = type;
		this.findName = new String[]{findName};
	}

	/**
	 * 특정 폴더의 찾고자하는 파일을 지정한다.
	 */
	@Override
	public boolean accept(File dir, String name) {
		return checkRtn(name);
	}
	
	private boolean checkRtn(String name){
		boolean rtnVal = false;
		for(int i = 0;i<findName.length;i++){
			
			if("startWith".equals(type))
				rtnVal = name.startsWith(findName[i]);
			else if("endsWith".equals(type))
				rtnVal = name.endsWith(findName[i]);
			else if("equals".equals(type))
				rtnVal = name.equals(findName[i]);
			else if("contains".equals(type))
				rtnVal = name.contains(findName[i]);
			else
				rtnVal = name.endsWith(findName[i]);
			
			if(rtnVal==true) break;
			
		}
		return rtnVal;
	}

}
