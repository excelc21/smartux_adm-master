package com.dmi.smartux.admin.news.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.CharEncoding;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.dmi.smartux.admin.common.dao.CommHmimsDao;
import com.dmi.smartux.admin.news.dao.NewsDao;
import com.dmi.smartux.admin.news.service.NewsService;
import com.dmi.smartux.admin.news.vo.NewsVO;
import com.dmi.smartux.admin.news.vo.TargetVO;
import com.dmi.smartux.admin.pvs.service.PvsService;
import com.dmi.smartux.common.exception.ExceptionHandler;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.GlobalCom;

/**
 * 새소식 Service Impl
 *
 * @author dongho
 */
@Service
public class NewsServiceImpl implements NewsService {
    private final Log mLogger = LogFactory.getLog(this.getClass());

	@Autowired
	NewsDao mDao;

	@Autowired
	CommHmimsDao hmimsDao;
	
    @Autowired
    PvsService mPvsService;

	@Override
	public List getList(NewsVO newsVO) throws Exception {
		return mDao.getList(newsVO);
	}

	@Override
	public NewsVO getData(String number) throws Exception {
		return mDao.getData(number);
	}

	@Override
	public int getCount(NewsVO newsVO) throws Exception {
		return mDao.getCount(newsVO);
	}

	@Override
	@Transactional
	public int insert(NewsVO newsVO) throws Exception {
		saveTitleImage(newsVO);

		int number = hmimsDao.insert(newsVO);

		// 로그 등록
		newsVO.setRegNumber(number);
		hmimsDao.insertLog(newsVO);

        if ("T".equals(newsVO.getPushType())) {
            // 월정액 가입일 Default (0:1주일) HDTV에서만 사용
            newsVO.setSignUpType("0");
            mDao.insertTargetRule(newsVO);

            List<String> list;

            if ("O".equals(newsVO.getSendType())) {
                if (!GlobalCom.isEmpty(newsVO.getOptionalServiceCode())) {
                    list = mPvsService.getIncludeSaIDList(newsVO);
                } else {
                    list = mPvsService.getAllSaIDList(newsVO);
                }
            } else {
                list = mPvsService.getExcludeSaIDList(newsVO);
            }

            saveTargetList(newsVO.getRegNumber() + ".txt", list);
        } else if ("E".equals(newsVO.getPushType())) {
			String folderPath = GlobalCom.isNull(SmartUXProperties.getPathProperty("n1.save.path"), "/NAS_DATA/web/smartux/push/target/");
			saveMultipartFile(folderPath, number + ".txt", newsVO.getFile());
		} else if ("I".equals(newsVO.getPushType())) {
			saveTargetList(number + ".txt", newsVO.getSaIDList());
		}

		return number;
	}

	@Override
	@Transactional
	public int update(NewsVO newsVO) throws Exception {
		saveTitleImage(newsVO);

		int result = hmimsDao.update(newsVO);
		hmimsDao.insertLog(newsVO);

        if ("T".equals(newsVO.getPushType())) {
            // 업데이트시에 타겟룰이 존재하지 않으면 등록한다.
            TargetVO targetVO = mDao.getTargetRule(newsVO.getRegNumber());

            if (targetVO == null) {
                mDao.insertTargetRule(newsVO);
            } else {
                mDao.updateTargetRule(newsVO);
            }

            mDao.updateTargetRule(newsVO);

            List<String> list;

            if ("O".equals(newsVO.getSendType())) {
                if (!GlobalCom.isEmpty(newsVO.getOptionalServiceCode())) {
                    list = mPvsService.getIncludeSaIDList(newsVO);
                } else {
                    list = mPvsService.getAllSaIDList(newsVO);
                }
            } else {
                list = mPvsService.getExcludeSaIDList(newsVO);
            }

            saveTargetList(newsVO.getRegNumber() + ".txt", list);
        } else if ("E".equals(newsVO.getPushType())) {
			String folderPath = GlobalCom.isNull(SmartUXProperties.getPathProperty("n1.save.path"), "/NAS_DATA/web/smartux/push/target/");
			saveMultipartFile(folderPath, newsVO.getRegNumber() + ".txt", newsVO.getFile());
		} else if ("I".equals(newsVO.getPushType())) {
			saveTargetList(newsVO.getRegNumber() + ".txt", newsVO.getSaIDList());
		}

		return result;
	}

	@Override
    @Transactional
	public int delete(NewsVO newsVO) throws Exception {
		hmimsDao.insertLog(newsVO);
        int regNumber = newsVO.getRegNumber();

        if ("T".equals(newsVO.getPushType())) {
            mDao.deleteTargetRule(regNumber);
        }

        return hmimsDao.delete(regNumber);
	}

	@Override
	public NewsVO getMarkingNews(String index) throws Exception {
		NewsVO newsVO = null;

		int result = hmimsDao.markNews(index);

		if (1 <= result) {
			newsVO = hmimsDao.getMarkingNews(index);
		}

		return newsVO;
	}

	@Override
	public int updateNewsStatus(HashMap<String, String> resultMap) throws Exception {
		return hmimsDao.updateNewsStatus(resultMap);
	}

    @Override
    public Map<String, String> checkPushMessage(NewsVO newsVo) throws Exception {
        Map<String,String> result;

        // 디폴트 값 세팅
        newsVo.setNotiGB("N1"); 	// 노티구분(NEW,N1)

        // 새소식 번호
        int regNumber = newsVo.getRegNumber();

        if (0 >= regNumber) {
            regNumber = mDao.getSequence();
            newsVo.setRegNumber(regNumber);
        }

        // 이미지푸시 신규등록(새소식 번호 미존재 && 이미지명 존재)
        if (!GlobalCom.isEmpty(newsVo.getImageName())) {
            String[] fileName = newsVo.getImageName().split("\\.");
            String ext = fileName[fileName.length - 1]; 			// 확장명(JPG, JPEG, PNG, ...)
            String imageUrl = mDao.getImageURL() + regNumber;	// ex) http://1.214.97.140:8888/smartux_adm/112
            newsVo.setImageURL(imageUrl + "." + ext); 			// 이미지URL + 확장명
        }

        result = compareMsgSize(newsVo);
        return result;
    }

    @Override
    public TargetVO getTargetRule(int regNumber) throws Exception {
        return mDao.getTargetRule(regNumber);
    }

    /**
     * 메시지 비교 메소드(GCM or APNS)
     *
     * @param newsVo 푸시전송객체
     */
    public Map<String,String> compareMsgSize(NewsVO newsVo) {
        Map<String,String> result = new HashMap<String,String>();
        String resCode = SmartUXProperties.getProperty("flag.success");		// code:0000
        String resMessage = SmartUXProperties.getProperty("message.success");	// message:성공

        int maxSize;	// 최대허용치
        int msgSize;	// 메시지길이

        try {
            maxSize = Integer.parseInt(GlobalCom.isNull(SmartUXProperties.getProperty("n1.msg.maxSize.android"), "1024"));
            msgSize = makeN1Message(newsVo).getBytes("UTF-8").length;

            mLogger.info("[NewsServiceImpl] compareMsgSize ######### ");
            mLogger.info("maxSize:" + maxSize);
            mLogger.info("msgSize:" + msgSize);

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
     * N1 Type의 메시지 전문을 만든다.
     *
     * @param vo 전송 데이터 객체
     * @return 메시지 전문
     */
    private String makeN1Message(NewsVO vo) {
        int regNumber = vo.getRegNumber();
        String notiGB = vo.getNotiGB();
        String showType = vo.getShowType();
        String netType = vo.getNetType();
        String notiType = vo.getNotiType();
        String notiDetail = vo.getNotiDetail();
        String imgURL = GlobalCom.isNull(vo.getImageURL()).replace("smartux", "PI");
        String title = vo.getTitle();
        String msg = vo.getContent();

        StringBuilder msgData = new StringBuilder();

        msgData.append("\\\"result\\\":{\\\"s_type\\\":\\\"").append(showType);
        msgData.append("\\\",\\\"n_type\\\":\\\"").append(netType);
        msgData.append("\\\",\\\"noti_type\\\":\\\"").append(notiGB);
        msgData.append("\\\",\\\"event_type\\\":\\\"").append(notiType);

        if (!GlobalCom.isEmpty(imgURL)) { msgData.append("\\\",\\\"img\\\":\\\"").append(imgURL); }

        msgData.append("\\\",\\\"title\\\":\\\"").append(title);
        msgData.append("\\\",\\\"msg\\\":\\\"").append(msg);
        msgData.append("\\\",\\\"id\\\":\\\"").append(regNumber);

        String p1 = "";
        String p2 = "";
        String p3 = "";
        String p4 = "";

        if ("NOT".equals(notiType)) {
            String[] ary = notiDetail.split(",");

            if (2 <= ary.length) {
                p1 = ary[0];
                p2 = ary[1];
            }

            if (!GlobalCom.isEmpty(p1)) { msgData.append("\\\",\\\"p1\\\":\\\"").append(p1); }
            if (!GlobalCom.isEmpty(p2)) { msgData.append("\\\",\\\"p2\\\":\\\"").append(p2); }
        } else if ("CON".equals(notiType)) {
            String[] ary = notiDetail.split("\\|\\|", -1);

            if (4 <= ary.length) {
                p1 = ary[0];
                p2 = ary[1];
                p3 = ary[2];
                p4 = ary[3];
            }

            if (!GlobalCom.isEmpty(p1)) { msgData.append("\\\",\\\"p1\\\":\\\"").append(p1); }
            if (!GlobalCom.isEmpty(p2)) { msgData.append("\\\",\\\"p2\\\":\\\"").append(p2); }
            if (!GlobalCom.isEmpty(p3)) { msgData.append("\\\",\\\"p3\\\":\\\"").append(p3); }
            if (!GlobalCom.isEmpty(p4)) { msgData.append("\\\",\\\"p4\\\":\\\"").append(p4); }
        } else if("CAT".equals(notiType) || "SVO".equals(notiType) || "URL".equals(notiType) || "LIV".equals(notiType)) {
            if (!GlobalCom.isEmpty(notiDetail)) { msgData.append("\\\",\\\"p1\\\":\\\"").append(notiDetail); }
        }

        msgData.append("\\\"}");

        mLogger.info("[NewsServiceImpl][makeN1Message] Sending Message:[" + msgData.toString() + "]");
        return msgData.toString();
    }

    /**
	 * 새소식 제목 이미지 저장
	 *
	 * @param newsVO 새소식 객체
	 */
	private void saveTitleImage(NewsVO newsVO) {
		MultipartFile imageFile = newsVO.getImageFile();

		if (null != imageFile && 0L < imageFile.getSize()) {
			String imgPath = GlobalCom.isNull(SmartUXProperties.getPathProperty("n1.img.path"), "/NAS_DATA/web/smartux/push/img/");

			StringBuilder fileName = new StringBuilder();

			int regNumber = newsVO.getRegNumber();

			if (0 >= regNumber) {
				regNumber = mDao.getSequence();
			}

			fileName.append(regNumber);

			String ext = FilenameUtils.getExtension(imageFile.getOriginalFilename());

			if (!GlobalCom.isEmpty(ext)) {
				fileName.append(".").append(ext);
			}

			newsVO.setImageName(fileName.toString());
			saveMultipartFile(imgPath, fileName.toString(), imageFile);
		}
	}

	/**
	 * MultipartFile 파일을 저장한다.
	 *
	 * @param folderPath 저장할 경로
	 * @param fileName 저장할 파일명
	 * @param uploadFile 업로드할 파일
	 */
	@SuppressWarnings("ResultOfMethodCallIgnored")
	private void saveMultipartFile(String folderPath, String fileName, MultipartFile uploadFile) {
		if (null != uploadFile && 0L < uploadFile.getSize()) {
			File folder = new File(folderPath);

			if (!folder.exists()) {
				folder.mkdirs();
			}

			File file = new File(folderPath + fileName);

			try {
				file.createNewFile();

				FileOutputStream fos = new FileOutputStream(file);
				fos.write(uploadFile.getBytes());
				fos.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * 타겟팅 직접입력 파일을 저장한다.
	 *
	 * @param fileName 저장할 파일명
	 * @param list 직접입력 리스트
	 */
	@SuppressWarnings("ResultOfMethodCallIgnored")
	private void saveTargetList(String fileName, List list) {
		try {
			String folderPath = GlobalCom.isNull(SmartUXProperties.getPathProperty("n1.save.path"), "/NAS_DATA/web/smartux/push/target/");

			File folder = new File(folderPath);

			if (!folder.exists()) {
				folder.mkdirs();
			}

			FileUtils.writeLines(new File(folderPath + fileName), CharEncoding.UTF_8, list);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
