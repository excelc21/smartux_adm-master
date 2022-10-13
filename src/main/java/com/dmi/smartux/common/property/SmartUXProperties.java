package com.dmi.smartux.common.property;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.util.StringUtils;

import com.dmi.smartux.common.util.CLog;
import com.dmi.smartux.common.util.GlobalCom;

public class SmartUXProperties extends	PropertyPlaceholderConfigurer {
	private static final String PATH = FilenameUtils.separatorsToSystem("/app/smartux_adm/conf/property/");
//	private static final String PATH = FilenameUtils.separatorsToSystem("D:/DEV/workspace/smartux_adm/properties");
    
	public static final String SMARTUX_EXTERNAL_PROPERTY_FILEPATH_KEY = "filepath.smartux.common";
	
	private static Map<String, String> properties = new HashMap<String, String>();
	private int springSystemPropertiesMode = SYSTEM_PROPERTIES_MODE_FALLBACK;

	public static void init() throws IOException {
		CLog cLog = new CLog(LogFactory.getLog(SmartUXProperties.class));
		cLog.startLog("MIMS Properties 갱신 시작");

        String env = GlobalCom.isNull(GlobalCom.getSystemProperty("env"), "local");

		File[] fileList = new File(PATH).listFiles();
		properties.clear();

		if (null != fileList) {
			cLog.middleLog("MIMS Properties 파일 존재");

			for (File file : fileList) {
				if (file.isFile() && file.getName().endsWith(".properties")) {
					cLog.middleLog("MIMS ####################### " + file.getName() + " #######################");

					java.util.Properties prop = new java.util.Properties();
					FileInputStream fis = new FileInputStream(file);
					prop.load(new BufferedInputStream(fis));

					for (Object obj : prop.keySet()) {
						String key = obj.toString().trim();
						String value = prop.get(key).toString().trim();

						if (!GlobalCom.isEmpty(value)) {
							// 4가지 환경에 맞는 값을 일단 걸러 낸 뒤 내 환경과 같은 값만 프로퍼티에 넣는다.
							if (key.startsWith("local.") || key.startsWith("dev.") || key.startsWith("function.") 
									|| key.startsWith("qual.") || key.startsWith("staging.") || key.startsWith("production.")) {
								if (key.startsWith(env)) {
									key = key.substring(env.length() + 1, key.length());
									properties.put(key, value);
									cLog.middleLog(key, value);
								}
							} else {
                                if (properties.containsKey(key)) {
                                    continue;
                                }

								properties.put(key, value);
								cLog.middleLog(key, value);
							}
						}
					}

					fis.close();
				}
			}

			cLog.endLog("MIMS Properties 갱신 완료");
		} else {
			cLog.endLog("MIMS Properties 파일 미존재");
		}

	}
	
	@Override
	public void setSystemPropertiesMode (int systemPropertiesMode) {
		super.setSystemPropertiesMode (systemPropertiesMode);
		springSystemPropertiesMode = systemPropertiesMode;
	}
	
	@Override
	protected void processProperties (ConfigurableListableBeanFactory beanFactory,	Properties props) throws BeansException {
		super.processProperties (beanFactory, props);

		try {
			init();
		} catch (IOException ignored) {
		}

		for (Object key: props.keySet ()) {
			String keyStr = key.toString ();
			String valueStr = resolvePlaceholder (keyStr, props, springSystemPropertiesMode);
			properties.put(keyStr, valueStr);
		}
	}

	/**
	* Return a property loaded by the place holder.
	* @param name the property name.
	* @return the property value.
	*/
	public static String getProperty(final String name) {
		return properties.get(name);
	}
	
	public static String getProperty(final String name, String ... replaceparam) {
		String returnmsg = properties.get(name);
		for(int i=0; i < replaceparam.length; i++){
			String replacestr = "{" + i + "}";
			returnmsg = StringUtils.replace(returnmsg, replacestr, replaceparam[i]);
		}
		
		return returnmsg;
	}

    /**
     * OS에 맞게 File Path를 가져온다.
     *
     * @param key Property Key
     * @return OS에 맞게 변환된 Path
     */
    public static String getPathProperty(String key) {
        if (!GlobalCom.isEmpty(key)) {
            return FilenameUtils.separatorsToSystem(properties.get(key));
        }

        return "";
    }
	
	/**
	 * 파리미터로 받은 key를 구성하는 문자열 값을 입력받아 이 문자열이 포함된 key값들을 리턴하는 함수
	 * @param keysubstring
	 * @return
	 */
	public static List<String> getKeys(String keysubstring){
		List<String> keylist = new ArrayList();
		Set<String> keyset = properties.keySet();
		for(String item : keyset){
			if(item.indexOf(keysubstring) > -1){
				keylist.add(item);
			}
		}
		return keylist;
	}
}
