package com.dmi.smartux.admin.greeting.service.impl;

import com.dmi.smartux.admin.greeting.dao.GreetingDao;
import com.dmi.smartux.admin.greeting.service.GreetingService;
import com.dmi.smartux.admin.greeting.vo.GreetingInfo;
import com.dmi.smartux.common.dao.ImageServerDao;
import com.dmi.smartux.common.exception.SmartUXException;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.JsonFileator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

import static com.dmi.smartux.admin.greeting.vo.GreetingComponent.SearchType;

@Service
public class GreetingServiceImpl implements GreetingService {

    private final Log logger = LogFactory.getLog(this.getClass());

	private final JsonFileator greeting;
	private final GreetingDao dao;
	private final ImageServerDao imageServerDao;
	@Autowired
	public GreetingServiceImpl(@Qualifier("greeting") JsonFileator greeting
            , GreetingDao dao
            , ImageServerDao imageServerDao) {
		this.greeting = greeting;
        this.dao = dao;
        this.imageServerDao = imageServerDao;
    }

	@Override
	public List getGreetingList(String searchType, String searchText) throws Exception {
        if(StringUtils.isEmpty(searchText)) {
            return this.greeting.getList();
        }
		return this.greeting.getList(searchType, searchText);
	}

	@Override
	public void insertGreeting(GreetingInfo greetingInfo, MultipartFile greeting_voice, MultipartFile bg_image)
            throws Exception {
	    File directory = new File(SmartUXProperties.getProperty("greeting.file.path") +
                SmartUXProperties.getProperty("greeting.file.voice.path"));
	    if(!directory.exists()) {
	        if(directory.mkdirs()) {
	            logger.info("greeting voice directory create : " + directory.getPath());
            }
        }
        directory = new File(SmartUXProperties.getProperty("greeting.file.path") +
                SmartUXProperties.getProperty("greeting.file.image.path"));
        if(!directory.exists()) {
            if(directory.mkdirs()) {
                logger.info("greeting image directory create : " + directory.getPath());
            }
        }

        greetingInfo.setImg_url(SmartUXProperties.getProperty("greeting.fileserver.url"));
		
		String voiceMaxSize = StringUtils.defaultString(SmartUXProperties.getProperty("greeting.voicefile.size"), "307200");
		String bgMaxSize = StringUtils.defaultString(SmartUXProperties.getProperty("greeting.bgimagefile.size"), "512000");
		 
        if(greeting_voice.getSize() != 0L) {
        	checkFileSize(greeting_voice, voiceMaxSize, "음성 파일");
            String fileName = greeting_voice.getOriginalFilename();
            String uploadFileName = System.currentTimeMillis() +
                    RandomStringUtils.randomAlphanumeric(8) +
                    FilenameUtils.EXTENSION_SEPARATOR_STR +
                    FilenameUtils.getExtension(fileName).toLowerCase();
            String filePath = SmartUXProperties.getProperty("greeting.file.voice.path");
            File file = new File(SmartUXProperties.getProperty("greeting.file.path") +
                    filePath + uploadFileName);
            greeting_voice.transferTo(file);
            greetingInfo.setGreeting_voice(filePath + uploadFileName);
            greetingInfo.setGreeting_voice_original_name(fileName);
        }
        if(bg_image.getSize() != 0L) {
        	checkFileSize(bg_image, bgMaxSize, "배경이미지 파일");
            String fileName = bg_image.getOriginalFilename();
            String uploadFileName = System.currentTimeMillis() +
                    RandomStringUtils.randomAlphanumeric(8) +
                    FilenameUtils.EXTENSION_SEPARATOR_STR +
                    FilenameUtils.getExtension(fileName).toLowerCase();
            String filePath = SmartUXProperties.getProperty("greeting.file.image.path");
            File file = new File(SmartUXProperties.getProperty("greeting.file.path") +
                                    filePath + uploadFileName);
            bg_image.transferTo(file);
            greetingInfo.setBg_image(filePath + uploadFileName);
            greetingInfo.setBg_image_original_name(fileName);
        }
        //noinspection unchecked
        this.greeting.insert(greetingInfo);
	}

    @Override
    public void updateGreeting(GreetingInfo greetingInfo, MultipartFile greeting_voice, MultipartFile bg_image)
            throws Exception {

        File directory = new File(SmartUXProperties.getProperty("greeting.file.path") +
                SmartUXProperties.getProperty("greeting.file.voice.path"));
        if(!directory.exists()) {
            if(directory.mkdirs()) {
                logger.info("greeting voice directory create : " + directory.getPath());
            }
        }
        directory = new File(SmartUXProperties.getProperty("greeting.file.path") +
                SmartUXProperties.getProperty("greeting.file.image.path"));
        if(!directory.exists()) {
            if(directory.mkdirs()) {
                logger.info("greeting image directory create : " + directory.getPath());
            }
        }

        List list = this.greeting.getList(SearchType.greeting_id.key(), String.valueOf(greetingInfo.getGreeting_id()));
        if(CollectionUtils.isNotEmpty(list)) {
            greetingInfo.setFileInfo((GreetingInfo) list.get(0));
        }
        
        String voiceMaxSize = StringUtils.defaultString(SmartUXProperties.getProperty("greeting.voicefile.size"), "307200");
		String bgMaxSize = StringUtils.defaultString(SmartUXProperties.getProperty("greeting.bgimagefile.size"), "512000");
		
        if(greeting_voice.getSize() != 0L) {
        	checkFileSize(greeting_voice, voiceMaxSize, "음성 파일");
            String fileName = greeting_voice.getOriginalFilename();
            String uploadFileName = System.currentTimeMillis() +
                    RandomStringUtils.randomAlphanumeric(8) +
                    FilenameUtils.EXTENSION_SEPARATOR_STR +
                    FilenameUtils.getExtension(fileName).toLowerCase();
            String filePath = SmartUXProperties.getProperty("greeting.file.voice.path");
            File file = new File(SmartUXProperties.getProperty("greeting.file.path") +
                    filePath + uploadFileName);
            greeting_voice.transferTo(file);
            greetingInfo.setGreeting_voice(filePath + uploadFileName);
            greetingInfo.setGreeting_voice_original_name(fileName);
            greetingInfo.setImg_url(SmartUXProperties.getProperty("greeting.fileserver.url"));
        }
        if(bg_image.getSize() != 0L) {
        	checkFileSize(bg_image, bgMaxSize, "배경이미지 파일");
            String fileName = bg_image.getOriginalFilename();
            String uploadFileName = System.currentTimeMillis() +
                    RandomStringUtils.randomAlphanumeric(8) +
                    FilenameUtils.EXTENSION_SEPARATOR_STR +
                    FilenameUtils.getExtension(fileName).toLowerCase();
            String filePath = SmartUXProperties.getProperty("greeting.file.image.path");
            File file = new File(SmartUXProperties.getProperty("greeting.file.path") +
                    filePath + uploadFileName);
            bg_image.transferTo(file);
            greetingInfo.setBg_image(filePath + uploadFileName);
            greetingInfo.setBg_image_original_name(fileName);
            greetingInfo.setImg_url(SmartUXProperties.getProperty("greeting.fileserver.url"));
        }
        //noinspection unchecked
        this.greeting.update(greetingInfo);
    }


	@Override
	public void deleteList(int[] orderList) throws Exception {
		this.greeting.delete(orderList);
	}

    @Override
    public void setGreeting(GreetingInfo greetingInfo) throws Exception {
        //noinspection unchecked
        this.greeting.update(greetingInfo);
    }
    @Override
    public void setGreetingOrder(int[] codeList) throws Exception {
        this.greeting.setOrder(codeList);
    }

    @Override
    public String getImageServerIpMims() {
        return imageServerDao.getImageServerIpMims();
    }

	/**
	 * 파일 사이즈 체크
	 * @param text_file
	 * @param fileMaxSize
	 */
	private void checkFileSize(MultipartFile text_file, String fileMaxSize, String param_name) {
		if(StringUtils.isNotBlank(fileMaxSize)) {
			long iFileMaxSize = Long.parseLong(fileMaxSize);
			if(text_file.getSize() > iFileMaxSize) throw new SmartUXException(SmartUXProperties.getProperty("flag.file.maxsize.over")
					, SmartUXProperties.getProperty("message.file.maxsize.over", param_name + ":" + fileMaxSize + "Byte 미만"));
		}
	}
}
