package com.dmi.smartux.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

public class CacheUtil {
	
	/**
	 * Cache file 명 가져오는 함수
	 *  
	 * @param folderPath
	 * @param fileName
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String getCacheFilePath(String folderPath, String fileName) {
		
		Map<String, Integer> map = GlobalCom.getServerIndex();
		if (map == null) {
			return null;
		}
		
		int serverIndex = map.get("index");
		
		return folderPath + File.separator + fileName + "_" + serverIndex;
	}
	
//	/**
//	 * Cache file 저장
//	 * 
//	 * @param fileName
//	 * @param obj
//	 * @throws Exception
//	 */
//	public static void saveCacheFile(String fileName, Object obj) throws Exception {
//		saveCacheFile(GlobalCom.getProperties("videolte.cache.folder"), fileName, obj);
//	}
	
	/**
	 * Cache file 저장
	 * 
	 * @param folderPath
	 * @param fileName
	 * @param obj
	 * @throws Exception
	 */
	public static synchronized String saveCacheFile(String folderPath, String fileName, Object obj) throws Exception {
		
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		
		File file = null;
		
		try {
			file = new File(getCacheFilePath(folderPath, fileName));
			
			fos = new FileOutputStream(file);
			oos = new ObjectOutputStream(fos);
			
			oos.writeObject(obj);
			oos.flush();
			oos.reset();
		} catch(Exception e) {
			throw e;
		} finally {
			if (oos != null) {
				try { oos.close(); } catch(Exception ignored) { }
			}
			
			if (fos != null) {
				try { fos.close(); } catch(Exception ignored) { }
			}
		}
		
		return file.getName();
	}
	
//	/**
//	 * Cache file 로드
//	 * 
//	 * @param fileName
//	 * @return
//	 * @throws Exception
//	 */
//	public static Object loadCacheFile(String fileName) throws Exception {
//		return loadCacheFile(GlobalCom.getProperties("videolte.cache.folder"), fileName);
//	}
	
	/**
	 * Cache file 로드
	 * 
	 * @param folderPath
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static Object loadCacheFile(String folderPath, String fileName) throws Exception {
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		
		String filePath = getCacheFilePath(folderPath, fileName);
		
		File file = new File(filePath);
		if (!file.exists()) {
			return null;
		}
		
		try {
			fis = new FileInputStream(file);
			ois = new ObjectInputStream(fis);
			
			return ois.readObject();
		} catch(Exception e) {
			throw e;
		} finally {
			if (ois != null) {
				try { ois.close(); } catch(Exception ignored) { }
			}
			
			if (fis != null) {
				try { fis.close(); } catch(Exception ignored) { }
			}
		}
	}
	
	/**
	 * NAS에 저장된 캐시 파일을 로드함.
	 * 
	 * @param folderPath
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static Object nasLoadCacheFile(String filePath) throws Exception {
		FileInputStream fis = null;
		ObjectInputStream ois = null;

		File file = new File(filePath);
		if (!file.exists()) {
			return null;
		}

		try {
			fis = new FileInputStream(file);
			ois = new ObjectInputStream(fis);

			return ois.readObject();
		} catch (Exception e) {
			throw e;
		} finally {
			if (ois != null) {
				try {
					ois.close();
				} catch (Exception ignored) {
				}
			}

			if (fis != null) {
				try {
					fis.close();
				} catch (Exception ignored) {
				}
			}
		}
	}
}
