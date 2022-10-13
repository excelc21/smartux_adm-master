package com.dmi.smartux.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import com.dmi.smartux.common.exception.ExceptionHandler;
import com.dmi.smartux.common.property.SmartUXProperties;

/**
 * 파일 관련 Util Class
 *
 * @author dongho
 */
public class FileUtil {
	/**
	 * String Byte 체크
	 *
	 * @param text 체크할 텍스트
	 * @param encoding byte 인코딩
	 * @param maxSize 최대 크기
	 * @return 결과 Map(res:코드, msg:내용)
	 */
	public static Map<String, String> checkByteSize(String text, String encoding, int maxSize) {
		Map<String,String> result = new HashMap<String,String>();
		String resCode = SmartUXProperties.getProperty("flag.success");		// code:0000
		String resMessage = SmartUXProperties.getProperty("message.success");	// message:성공

		try {
			int msgSize = text.getBytes(encoding).length;

			// 유효길이 확인
			if (maxSize < msgSize) {
				resCode = SmartUXProperties.getProperty("flag.fail");		// code:9000
				resMessage = SmartUXProperties.getProperty("message.fail");// message:실패
			}
		} catch (Exception e) {
			ExceptionHandler handler = new ExceptionHandler(e);
			resCode = handler.getFlag();
			resMessage = handler.getMessage();

		} finally {
			result.put("res", resCode);
			result.put("msg", resMessage);
		}

		return result;
	}

	/**
	 * MultipartFile 파일을 저장한다.
	 *
	 * @param folderPath 저장할 경로
	 * @param fileName 저장할 파일명
	 * @param uploadFile 업로드할 파일
	 * @return 저장 여부
	 */
	@SuppressWarnings("ResultOfMethodCallIgnored")
	public static boolean saveMultipartFile(String folderPath, String fileName, MultipartFile uploadFile) {
		if (null != uploadFile && 0L < uploadFile.getSize()) {
			File folder = new File(FilenameUtils.separatorsToSystem(folderPath));

			if (!folder.exists()) {
				folder.mkdirs();
			}

			File file = new File(FilenameUtils.separatorsToSystem(folderPath + fileName));

			FileOutputStream fos = null;

			try {
				file.createNewFile();

				fos = new FileOutputStream(file);
				fos.write(uploadFile.getBytes());
				fos.flush();
				fos.getFD().sync();
			} catch (IOException e) {
				throw new RuntimeException(e);
			} finally {
				if (null != fos) {
					try {
						fos.close();
					} catch (IOException ignored) {
					}
				}
			}
		}

		return true;
	}

	/**
	 * fileSerialization
	 *
	 * @param path 폴더 경로
	 * @param key Key
	 * @param object Serialization 객체
	 * @return 성공 여부
	 */
	public static boolean fileSerialization(String path, String key, Object object) {
		File folder = new File(FilenameUtils.separatorsToSystem(path));

		if (!folder.exists()) {
			//noinspection ResultOfMethodCallIgnored
			folder.mkdirs();
		}

		FileOutputStream fileout = null;
		ObjectOutputStream out = null;

		try {
			fileout = new FileOutputStream(FilenameUtils.separatorsToSystem(path + key));
			out = new ObjectOutputStream(fileout);
			out.writeObject(object);
			out.flush();
			fileout.getFD().sync();
		} catch (IOException ignored) {
			return false;
		} finally {
			if (null != out) {
				try {
					out.close();
				} catch (IOException ignored) {
				}
			}

			if (null != fileout) {
				try {
					fileout.close();
				} catch (IOException ignored) {
				}
			}
		}

		return true;
	}

	/**
	 * fileDeserialization
	 *
	 * @param path 폴더 경로
	 * @param key Key
	 * @return Serialization 객체
	 */
	public static Object fileDeserialization(String path, String key) {
		FileInputStream fis = null;
		ObjectInputStream in = null;
		Object obj = null;

		try {
			fis = new FileInputStream(FilenameUtils.separatorsToSystem(path + key));
			in = new ObjectInputStream(fis);
			obj = in.readObject();
		} catch (IOException ignored) {
		} catch (ClassNotFoundException ignored) {
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException ignored) {
				}
			}

			if (null != fis) {
				try {
					fis.close();
				} catch (IOException ignored) {
				}
			}
		}

		return obj;
	}
	
	/**
	 * 파일 최대 업로드 사이즈 체크
	 * @param file 지정파일
	 * @param maxSize MaxSize
	 * @return
	 */
	public static boolean fileSizeCheck(MultipartFile file, String maxSize){
		boolean checkYn = false;
		if (file != null && file.getSize() != 0L) {
			Long tmpSize = file.getSize();
			Long tmpCheckSize = Long.parseLong(GlobalCom.isNull(maxSize,"0"));
			
			if(tmpCheckSize ==0 || tmpSize <= tmpCheckSize){
				checkYn = true;
			}
		}else{
			checkYn = true;
		}
		
		return checkYn;
	}

//	/**
//	 * fileOutputStreamLock
//	 *
//	 * @param path 폴더 경로
//	 * @param key Key
//	 * @param object Serialization 객체
//	 * @return 성공 여부
//	 */
//	public static boolean fileOutputStreamLock(String path, String key, Object object) {
//		File folder = new File(FilenameUtils.separatorsToSystem(path));
//
//		if (!folder.exists()) {
//			//noinspection ResultOfMethodCallIgnored
//			folder.mkdirs();
//		}
//
//		FileOutputStream fileout = null;
//		ObjectOutputStream out = null;
//		FileLock fLock = null;
//
//		try {
//			fileout = new FileOutputStream(FilenameUtils.separatorsToSystem(path + key));
//
//	        int i = 0;
//			while(fLock==null){
//				try{
//					fLock = fileout.getChannel().tryLock();
//				}catch(OverlappingFileLockException e){
//					Thread.sleep(200);
//					if(i++>50){
//						System.out.println("FileLock Fail.");
//						break;
//					}
//				}catch(java.io.IOException e){
//					Thread.sleep(200);
//					if(i++>50){
//						System.out.println("FileLock Fail.");
//						break;
//					}
//				}
//			}
//			
//			out = new ObjectOutputStream(fileout);
//			out.writeObject(object);
//			out.flush();
//			fileout.getFD().sync();
//		} catch (Exception ignored) {
//			return false;
//		} finally {
//			if (null != fLock) {
//				try {
//					fLock.release();
//				} catch (IOException ignored) {
//				}
//			}
//			
//			if (null != out) {
//				try {
//					out.close();
//				} catch (IOException ignored) {
//				}
//			}
//
//			if (null != fileout) {
//				try {
//					fileout.close();
//				} catch (IOException ignored) {
//				}
//			}
//		}
//
//		return true;
//	}
//
//	/**
//	 * fileInputStreamLock
//	 *
//	 * @param path 폴더 경로
//	 * @param key Key
//	 * @return Serialization 객체
//	 */
//	public static Object fileInputStreamLock(String path, String key) {
//		FileInputStream fis = null;
//		ObjectInputStream in = null;
//		Object obj = null;
//		FileLock fLock = null;
//
//		try {
//			fis = new FileInputStream(FilenameUtils.separatorsToSystem(path + key));
//
//	        int i = 0;
//			while(fLock==null){
//				try{
//					fLock = fis.getChannel().tryLock(0L, Long.MAX_VALUE, true);
//				}catch(OverlappingFileLockException e){
//					Thread.sleep(200);
//					if(i++>50){
//						System.out.println("FileLock Fail.");
//						break;
//					}
//				}catch(java.io.IOException e){
//					Thread.sleep(200);
//					if(i++>50){
//						System.out.println("FileLock Fail.");
//						break;
//					}
//				}
//			}
//			
//			in = new ObjectInputStream(fis);
//			obj = in.readObject();
//		} catch (IOException ignored) {
//		} catch (ClassNotFoundException ignored) {
//		} catch (Exception ignored) {
//		} finally {
//			if (null != fLock) {
//				try {
//					fLock.release();
//				} catch (IOException ignored) {
//				}
//			}
//			
//			if (null != in) {
//				try {
//					in.close();
//				} catch (IOException ignored) {
//				}
//			}
//
//			if (null != fis) {
//				try {
//					fis.close();
//				} catch (IOException ignored) {
//				}
//			}
//		}
//
//		return obj;
//	}

}
