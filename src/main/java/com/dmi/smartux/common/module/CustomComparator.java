package com.dmi.smartux.common.module;

import java.io.File;
import java.util.Comparator;

import com.dmi.smartux.common.vo.QualityMemberVo;

public class CustomComparator implements Comparator<Object> {
	
	String type;
	public CustomComparator(String type){
		this.type = type;
	}

	/**
	 * 배열 정렬을 위하여 정렬할 기준을 세운다.
	 */
	@Override
	public int compare(Object o1, Object o2) {
		  if("LengthAsc".equals(type))
			  return qualityCompare(o1,o2);
		  else if("LengthDesc".equals(type))
			  return qualityCompare(o2,o1);
		  else if("QualityFile".equals(type))
			  return qualityFileCompare((File)o1,(File)o2);
		  else if("QualityDir".equals(type))
			  return qualityDirCompare((File)o1,(File)o2);
		  else
			  return 0;
	}
	
	private int qualityCompare(Object obj1, Object obj2){  
		QualityMemberVo qlt1 = (QualityMemberVo)obj1;
		QualityMemberVo qlt2 = (QualityMemberVo)obj2;
		
		String s1 = qlt1.getSa_id();
		String s2 = qlt2.getSa_id();
		
		return s1.length() - s2.length();
	}
	
	private int qualityFileCompare(File f1, File f2){
		long int_f1 = f1.lastModified();
		long int_f2 = f2.lastModified();
		
		if(int_f1>int_f2) return -1;
		else if(int_f1<int_f2) return 1;
		else return 0;
		  
	}
	
	private int qualityDirCompare(File f1, File f2){
		
		  return f1.getName().compareTo(f2.getName());
		  
	}

}
