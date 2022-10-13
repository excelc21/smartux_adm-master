package com.dmi.smartux.admin.memberlist.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

import org.apache.commons.lang.SystemUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dmi.smartux.admin.memberlist.service.MemberListAdminService;
import com.dmi.smartux.common.module.CustomNameFilter;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.FileUtil;

@Service
public class MemberListAdminServiceImpl implements MemberListAdminService {

	@Override
	public String makeTemplate() throws Exception {
//		StringBuilder builder= new StringBuilder();
//		for (int i = 0; i < 10000000; i++) {
//			builder.append("A군," + i + "\n");
//		}
		return "그룹정보,가입자번호(해당열은 삭제하지마세요!)\r\nA군,5000012345\r\nA군,5000022222\r\nB군,5000120012\r\nC군,M12345667";
	}

	@Override
	public void uploadFile(MultipartFile uploadFile) throws Exception {

		String filePath = SmartUXProperties.getProperty("memberlist.dir");
		File directory = new File(filePath);
		
		//파일 명 날짜 포맷
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		
		//폴더 최초 생성
		if (!directory.getParentFile().exists()) {
			throw new FileNotFoundException();
		} else if (!directory.exists()){
			directory.mkdirs();
		}
		
		//기존 N_ 파일 이름 변경
		File[] newFiles = directory.listFiles(new CustomNameFilter("startWith", "N"));
		
		for (int i = 0; i < newFiles.length; i++) {
			File listOfNewFiles = newFiles[i];
			listOfNewFiles.renameTo(new File(listOfNewFiles.getParent() + SystemUtils.FILE_SEPARATOR + listOfNewFiles.getName().replaceFirst("N_", "")));
		}
		
		File[] backupFiles = directory.listFiles();
		
		//날짜순으로 정렬
		Arrays.sort(backupFiles, new Comparator<File>() {
			@Override
			public int compare(File o1, File o2) {
				return (int)(o1.lastModified() - o2.lastModified());
			}
		});
		
		//최대 20개만 유지
		int remainSize = 20;
		if (backupFiles.length > remainSize) {
			for (int i = 0; i < backupFiles.length - remainSize; i++) {
				File file = backupFiles[i];
				file.delete();
			}
		}
		
		//파일 업로드
		String fileName = "N_" + dateFormat.format(new Date()) + "_member_group.txt";
		
		FileUtil.saveMultipartFile(filePath, fileName, uploadFile);
		
		File nameDirectory = new File(filePath + "/name");
		
		//폴더 최초 생성
		if(!nameDirectory.exists()){
			nameDirectory.mkdirs();
		}
		
		File[] nameFiles = nameDirectory.listFiles();
		
		if (nameFiles == null || nameFiles.length == 0) {
			File nameFile = new File(nameDirectory.getAbsolutePath() + "/" + uploadFile.getOriginalFilename() + ".name");
			nameFile.createNewFile();
		} else {
			nameFiles[0].renameTo(new File(nameDirectory.getAbsolutePath() + "/" + uploadFile.getOriginalFilename() + ".name"));
		}
	}

	@Override
	public void deleteUploadedFile() throws Exception {

		String filePath = SmartUXProperties.getProperty("memberlist.dir");
		File directory = new File(filePath);
		
		//기존 N_ 파일 이름 삭제
		File[] beforeFile = directory.listFiles();
		
		if (beforeFile != null) {
			for (int i = 0; i < beforeFile.length; i++) {
				File listOfFiles = beforeFile[i];
				if (listOfFiles.isFile() && listOfFiles.getName().startsWith("N") ) {
					listOfFiles.getAbsoluteFile().delete();
				}
			}
		}
		
		File nameDirectory = new File(filePath + "/name");
		
		File[] nameFiles = nameDirectory.listFiles();

		if (nameFiles != null) {
			for (File nameFile : nameFiles) {
				nameFile.delete();
			}
		}
	}

	@Override
	public File getMemberListFile() throws Exception {

		String filePath = SmartUXProperties.getProperty("memberlist.dir");
		File directory = new File(filePath);

		File[] newFiles = directory.listFiles(new CustomNameFilter("startWith", "N"));
		
		return newFiles[0];
	}

	@Override
	public boolean checkFile() {

		String filePath = SmartUXProperties.getProperty("memberlist.dir");
		File directory = new File(filePath);

		File[] newFiles = directory.listFiles(new CustomNameFilter("startWith", "N"));
		
		return newFiles != null && newFiles.length != 0;
	}

	@Override
	public String getRecentFileName() {
		String filePath = SmartUXProperties.getProperty("memberlist.dir");
		String nameFilePath = filePath + "/name";

		File directory = new File(filePath);
		File fileNameDirectory = new File(nameFilePath);

		File[] newFiles = directory.listFiles(new CustomNameFilter("startWith", "N"));
		String[] fileNames = fileNameDirectory.list();
		if (newFiles == null || newFiles.length == 0 || fileNames == null || fileNames.length == 0) {
			return "현재 업로드된 파일이 없습니다.";
		} else {
			return fileNames[0].substring(0, fileNames[0].indexOf(".name"));
		}
	}

}
