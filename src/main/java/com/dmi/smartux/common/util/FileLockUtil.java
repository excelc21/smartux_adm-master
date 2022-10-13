package com.dmi.smartux.common.util;

import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.vo.FileLockVO;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.util.Date;

/**
 * 파일 잠금을 위한 유틸리티 클래스
 *
 * @author dongho
 */
public class FileLockUtil {
	public static final String FOLDER_PATH = GlobalCom.isNull(SmartUXProperties.getPathProperty("file.lock.dir"), "/NAS_DATA/web/smartux/file_lock");

	public static FileLockVO getFileLock(String key) {
		FileInputStream fis = null;
		ObjectInputStream in = null;
		Object obj = null;

		try {
			fis = new FileInputStream(FilenameUtils.separatorsToSystem(FOLDER_PATH + "/" + key));
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

		if (null != obj && obj instanceof FileLockVO) {
			return (FileLockVO) obj;
		}

        return null;
	}

	private static void setFileLock(String key, FileLockVO lockVO) {
		File folder = new File(FOLDER_PATH);

		if (!folder.exists()) {
			//noinspection ResultOfMethodCallIgnored
			folder.mkdirs();
		}

		FileOutputStream fileout = null;
		ObjectOutputStream out = null;

        try {
            fileout = new FileOutputStream(FilenameUtils.separatorsToSystem(FOLDER_PATH + "/" + key));
            out = new ObjectOutputStream(fileout);
            out.writeObject(lockVO);
			out.flush();
			fileout.getFD().sync();
        } catch (IOException ignored) {
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

	}

	public static void lockFile(String key, FileLockVO lockVO) {
		lockVO.setLocked(true);
		lockVO.setCheckedDate(new Date());

		setFileLock(key, lockVO);
	}

	public static void unLockFile(String key) {
        FileLockVO lockVO = getFileLock(key);
        lockVO.setLocked(false);

        setFileLock(key, lockVO);
	}
}
