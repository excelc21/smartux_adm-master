package com.dmi.smartux.admin.memberlist.service;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

public interface MemberListAdminService {

	String makeTemplate() throws Exception;

	void uploadFile(MultipartFile csvFile) throws Exception;

	void deleteUploadedFile() throws Exception;

	File getMemberListFile() throws Exception;

	boolean checkFile();

	String getRecentFileName();

}
