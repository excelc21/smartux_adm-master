package com.dmi.smartux.admin.pushconfig.component;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dmi.smartux.admin.pushconfig.dao.PushConfigDao;
import com.dmi.smartux.admin.pushconfig.vo.PushConfigVo;
import com.dmi.smartux.common.util.GlobalCom;

@Component
public class PushConfigComponent {

	@Autowired
	PushConfigDao dao;
	
	private final static String DEFAULT_PATH = "/NAS_DATA/web/smartux/pushconfig/recode/";

	/**
	 * 	//푸시설정정보 조회
	 * @param service_type
	 * @return
	 * @throws Exception
	 */
	public PushConfigVo getPushConfig(String service_type) throws Exception{
		return dao.getPushConfig(service_type);
	}

	/**
	 * 	//발송이력조회
	 * @param service_type
	 * @param reg_id
	 * @param agent_type
	 * @return
	 * @throws Exception
	 */
	public int readPushRecode(String file_path, String service_type, String reg_id, String agent_type) throws Exception{
		int pushRecode = 0;
		try{
			File path = new File(GlobalCom.isNull(file_path, DEFAULT_PATH));
			
			final String filename = service_type + "_" + reg_id + "_" +  agent_type;
			
			File fileList[] = path.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return name.startsWith(filename);
				}
			});
			
			if(fileList.length > 0){
				long lastModifiedTime = Long.MIN_VALUE;
			    File chosenFile = null;
			    
			    for (File file : fileList){
		            if (file.lastModified() > lastModifiedTime){
		                chosenFile = file;
		                lastModifiedTime = file.lastModified();
		            }
		        }
			
			    String fileName = chosenFile.getName();
				String temp[] = fileName.split("_");
				if(temp.length > 3){
					if(fileName.contains(".block")){
						pushRecode = Integer.parseInt(temp[3].split("\\.")[0]);
					}else{
						pushRecode = Integer.parseInt(temp[3]);
					}
				}
			}
		}catch(Exception e){
			return 0;
		}
		
		return pushRecode;
	}
	
	/**
	 * //발송이력저장
	 * @param service_type
	 * @param reg_id
	 * @param agent_type
	 * @param recode
	 * @return
	 * @throws Exception
	 */
	public boolean savePushRecode(String file_path, String service_type, String reg_id, String agent_type, String recode) throws Exception{
		try{
			String pathStr = GlobalCom.isNull(file_path, DEFAULT_PATH);
			File path = new File(pathStr);
			
			if (!path.exists()) {
				path.mkdirs();
			}
			
			final String filename = service_type + "_" +  reg_id + "_" +  agent_type;
			
			String fileList[] = path.list(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return name.startsWith(filename);
				}
			});
		
			boolean checkBlock = false;
			
			if(fileList.length > 0){
				for(String name : fileList){
					File file = new File(pathStr + name);
					if(file.exists()){
						if(name.contains(".block")){
							checkBlock = true;
						}
						file.delete();
					}
				}
			}
		
			if(checkBlock){
				File file = new File(pathStr + filename + "_" + recode + ".block");
				file.createNewFile();
			}else{
				File file = new File(pathStr + filename + "_" + recode);
				file.createNewFile();
			}
			
		}catch(Exception e){
			return false;
		}
		
		return true;
	}
	
	/**
	 * //발송이력삭제
	 * @param service_type
	 * @param reg_id
	 * @param agent_type
	 * @param recode
	 * @return
	 * @throws Exception
	 */
	public boolean deletPushRecode(String file_path, String service_type, String reg_id, String agent_type) throws Exception{
		try{
			String pathStr = GlobalCom.isNull(file_path, DEFAULT_PATH);
			File path = new File(pathStr);
			
			final String filename = service_type + "_" +  reg_id + "_" +  agent_type;
			
			String fileList[] = path.list(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return name.startsWith(filename);
				}
			});
		
			if(fileList.length > 0){
				for(String name : fileList){
					File file = new File(pathStr + name);
					if(file.exists()){
						file.delete();
					}
				}
			}
		}catch(Exception e){
			return false;
		}
		
		return true;
	}
	
	public boolean checkPushBlock(String file_path, String service_type, String reg_id, String agent_type) throws Exception{
		try{
			File path = new File(GlobalCom.isNull(file_path, DEFAULT_PATH));
			
			final String filename = service_type + "_" + reg_id + "_" +  agent_type;
			
			File fileList[] = path.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return name.startsWith(filename);
				}
			});
			
			//블락의 경우 시점문제로 인해 최신파일이 아니라 block파일이 있으면 활성화 되도록 변경
			if(fileList.length > 0){
//				long lastModifiedTime = Long.MIN_VALUE;
//			    File chosenFile = null;
			    
			    for (File file : fileList){
			    	if(file.getName().contains(".block")){
			    		return true;
			    	}
//		            if (file.lastModified() > lastModifiedTime){
//		                chosenFile = file;
//		                lastModifiedTime = file.lastModified();
//		            }
		        }
			
//			    String fileName = chosenFile.getName();
			    
//			    if(fileName.contains(".block")){
//			    	return true;
//			    }
			}
		}catch(Exception e){
			return false;
		}
		
		return false;
	}
}
