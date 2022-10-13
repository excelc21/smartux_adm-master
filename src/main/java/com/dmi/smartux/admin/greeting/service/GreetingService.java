package com.dmi.smartux.admin.greeting.service;

import com.dmi.smartux.admin.greeting.vo.GreetingInfo;
import com.dmi.smartux.admin.mainpanel.vo.ViewVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface GreetingService {
	List getGreetingList(String searchType, String searchText) throws Exception;
	void insertGreeting(GreetingInfo greetingInfo, MultipartFile greeting_voice, MultipartFile bg_image) throws Exception;
	void updateGreeting(GreetingInfo greetingInfo, MultipartFile greeting_voice, MultipartFile bg_image) throws Exception;
	void deleteList(int[] orderList) throws Exception;
	void setGreeting(GreetingInfo greetingInfo) throws Exception;
	void setGreetingOrder(int[] codeList) throws Exception;
	String getImageServerIpMims();
}
