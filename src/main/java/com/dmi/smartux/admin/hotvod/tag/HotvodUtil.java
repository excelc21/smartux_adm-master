package com.dmi.smartux.admin.hotvod.tag;

import org.apache.commons.lang.StringUtils;

import com.dmi.smartux.admin.hotvod.vo.HotvodConst;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.GlobalCom;

public class HotvodUtil {
	
	private static final String hotvodServiceList = GlobalCom.isNull(SmartUXProperties.getProperty("hotvod.service.list"), HotvodConst.DEFAULT_HOTVOD_SERVICE_LIST);
	private static final String hotvodBadgeList = GlobalCom.isNull(SmartUXProperties.getProperty("hotvod.badge.list"), HotvodConst.DEFAULT_HOTVOD_BADGE_LIST);

	public static int getHotvodBadgeMaxSize() {
		String[] badge_list_arr = hotvodBadgeList.split("\\|");
		if(badge_list_arr != null)
			return badge_list_arr.length;
		else 
			return 0;
	}

	/**
	 * multi_service_type(10진수)를 2진수로 변환
	 * @param multi_service_type
	 * @return
	 */
	public static String toBinaryString(Integer multi_service_type) {
		StringBuffer sb = new StringBuffer();
		
		while(multi_service_type > 0) {
			sb.append(multi_service_type%2);
			multi_service_type /= 2;
		}
		
		return sb.toString();		
	}
	
	/**
	 * 화제동영상 전체 서비스 개수만큼 10진수로 변환
	 * @return String
	 */
	public static String toDecStringByAllService() {
		String[] service_list_arr = hotvodServiceList.split("\\|");
	
		double sum = 0;
		for(int i=0;i<service_list_arr.length;i++) {
			sum += Math.pow(2, i);
		}
		return String.valueOf((int)sum);
	}
	
	/**
	 * 서비스타입을 10진수로 변환
	 * @param service_type
	 * @return String
	 */
	public static String toDecStringByService(String value) {
		String[] service_list_arr = hotvodServiceList.split("\\|");
		String[] service_type_arr;
		StringBuffer type = new StringBuffer();
		String result = ""; 
		if(value == null || "".equals(value)) {
			return result;
		} else {
			for(int i=0;i<service_list_arr.length;i++){
				service_type_arr=service_list_arr[i].split("\\^");
				
				if(value.equals(service_type_arr[0])){
					type.append("1");
				}
				else {
					type.append("0");
				}
			}
			result = Integer.toString(Integer.valueOf(type.reverse().toString(), 2));
			return result;
		}
	}							
							
	public static String drawServiceTypeName(String multi_service_type) {
		String[] service_list_arr = hotvodServiceList.split("\\|");
		String serviceBinary = toBinaryString(Integer.decode(StringUtils.defaultIfEmpty(multi_service_type, toDecStringByAllService())));
		
		String[] service_type_arr = null;
		StringBuffer serviceName =new StringBuffer();
		for(int j=0;j<serviceBinary.length();j++) {
			if(serviceBinary.charAt(j) == '1') {
				if(service_list_arr.length > j) {
					service_type_arr = service_list_arr[j].split("\\^");
					serviceName.append(service_type_arr[1]);
					serviceName.append(",");
				}
			}
		}
			
		return serviceName.substring(0, serviceName.length() == 0 ? serviceName.length() : serviceName.length()-1).toString();
	}
}
