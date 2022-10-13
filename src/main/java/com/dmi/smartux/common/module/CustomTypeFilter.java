package com.dmi.smartux.common.module;

import java.io.File;
import java.io.FileFilter;

public class CustomTypeFilter implements FileFilter{

	@Override
	public boolean accept(File pathname) {
		return pathname.isDirectory();
	}

}
