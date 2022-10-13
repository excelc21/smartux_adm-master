package com.dmi.smartux.admin.secondtv_push.service.impl;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.LogFactory;
import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmi.smartux.admin.news.service.NewsService;
import com.dmi.smartux.admin.news.vo.NewsVO;
import com.dmi.smartux.admin.pushconfig.component.PushConfigComponent;
import com.dmi.smartux.admin.pushconfig.vo.PushConfigVo;
import com.dmi.smartux.admin.secondtv_push.service.LatestScheduleService;
import com.dmi.smartux.admin.secondtv_push.service.PushScheduleService;
import com.dmi.smartux.admin.secondtv_push.vo.LatestContentVO;
import com.dmi.smartux.admin.secondtv_push.vo.PushUserVO;
import com.dmi.smartux.common.exception.SmartUXException;
import com.dmi.smartux.common.module.StatPushLogger;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.CLog;
import com.dmi.smartux.common.util.DateUtil;
import com.dmi.smartux.common.util.FileLockUtil;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.util.XmlUtil;
import com.dmi.smartux.common.vo.FileLockVO;
import com.dmi.smartux.common.vo.StatPushLogVo;

/**
 * Push Schedule Service Implements
 *
 * @author dongho
 */
@Service
public class PushScheduleServiceImpl implements PushScheduleService {
	public static final String LATEST_KEY = "latest";

	private CLog mLatestLog = new CLog(LogFactory.getLog("latestSchedule"));
	private CLog mNewsLog = new CLog(LogFactory.getLog("newsSchedule"));

	@Autowired
	NewsService mNewsService;

	@Autowired
	LatestScheduleService mLatestScheduleService;
	
	@Autowired
	PushConfigComponent pushConfigComponent;

	@Override
	public void requestLatestPush() throws Exception {
        // 1분 주기 스케쥴러라서 통합통계 파일 만드는 로직을 얻어 태움
        new StatPushLogger().makeFile();

		Map<String, Integer> map = GlobalCom.getServerAdminIndex();

		int total = map.get("total");
		int index = map.get("index");

		Calendar cal = Calendar.getInstance();
		int minutes = cal.get(Calendar.MINUTE);

		mLatestLog.startLog("requestLatestPush", "Start");

		if (index == minutes % total) {
			// 푸시설정정보조회
			PushConfigVo pushConfigVo = pushConfigComponent.getPushConfig("T");
			if(null == pushConfigVo){
				pushConfigVo = new PushConfigVo();
			}
			String blockPush = GlobalCom.isNull(pushConfigVo.getBlock_push(), "N");
			String pageCount = GlobalCom.isNull(pushConfigVo.getPage_count(), "400");
			String sleepTime = GlobalCom.isNull(pushConfigVo.getSleep_time(), "100");
			
			if("N".equalsIgnoreCase(blockPush)){
				
				long clearInterval = Long.parseLong(GlobalCom.isNull(SmartUXProperties.getProperty("latepush.filelock.clearInterval"), "18000000"));
				long subTime = Long.parseLong(GlobalCom.isNull(SmartUXProperties.getProperty("latepush.dbquery.beforeSearchTime"), "1800000"));
				
				boolean workable;
				
				FileLockVO lockVO = FileLockUtil.getFileLock(LATEST_KEY);
				
				if (null == lockVO) {
					// 작업 가능
					mLatestLog.middleLog("requestLatestPush", "LockFile Exist::false", "Workable::true");
					lockVO = new FileLockVO();
					lockVO.setCheckedDate(new Date(System.currentTimeMillis() - subTime));
					workable = true;
				} else {
					if (!lockVO.isLocked()) {
						long checkTime = DateUtil.getTimeDifference(lockVO.getCheckedDate());
						
						if (checkTime >= subTime) {
							mLatestLog.middleLog("requestLatestPush", "DB Time Over::true", "checkTime::" + checkTime, "interval::" + subTime);
							mLatestLog.middleLog("requestLatestPush", "LockFile Exist::true", "isLocked::false", "Workable::true");
							workable = true;
						} else {
							mLatestLog.middleLog("requestLatestPush", "DB Time Over::false", "checkTime::" + checkTime, "interval::" + subTime);
							mLatestLog.middleLog("requestLatestPush", "LockFile Exist::true", "isLocked::false", "Workable::false");
							workable = false;
						}
					} else {
						long checkTime = DateUtil.getTimeDifference(lockVO.getCheckedDate());
						
						if (checkTime >= clearInterval) {
							mLatestLog.middleLog("requestLatestPush", "Check Time Over::true", "checkTime::" + checkTime, "interval::" + clearInterval);
							mLatestLog.middleLog("requestLatestPush", "LockFile Exist::true", "isLocked::true", "Workable::true");
							workable = true;
						} else {
							mLatestLog.middleLog("requestLatestPush", "Check Time Over::false", "checkTime::" + checkTime, "interval::" + clearInterval);
							mLatestLog.middleLog("requestLatestPush", "LockFile Exist::true", "isLocked::true", "Workable::false");
							workable = false;
						}
					}
				}
				
				if (workable) {
					// Task 시작
					try {
						mLatestLog.middleLog("requestLatestPush", "Work", "total::" + total, "index::" + index);
						SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
						
						String startDate = sdf.format(lockVO.getCheckedDate());
						String endDate = sdf.format(new Date());
						
						List list =  mLatestScheduleService.getLatestContentList(startDate, endDate);
						
						mLatestLog.middleLog("requestLatestPush", "Work", "total::" + total, "index::" + index, "startDate::" + startDate, "endDate::" + endDate, "listSize::" + list.size());
						
						FileLockUtil.lockFile(LATEST_KEY, lockVO);
						
						if (!GlobalCom.isEmpty(list)) {
							for (Object o : list) {
								LatestContentVO latestContentVO = (LatestContentVO) o;
								String catID = GlobalCom.isNull(latestContentVO.getCategoryID(), "");
								String contentID = GlobalCom.isNull(latestContentVO.getContentID(), "");
								String contentName = GlobalCom.isNull(latestContentVO.getContentName(), "");
								String seriesNum = GlobalCom.isNull(latestContentVO.getSeriesNumber(), "");
								String catSeriesNum = GlobalCom.isNull(latestContentVO.getCategorySeriesNumber(), "");
								String catGB = GlobalCom.isNull(latestContentVO.getCategory_gb(), "");
								
								mLatestLog.middleLog("requestLatestPush", "TotalUserStart", "total::" + total, "index::" + index, "catID::" + catID, "catGB::" + catGB, "contentID::" + contentID);
								int totalUser = mLatestScheduleService.getUserListCount(catID);
								mLatestLog.middleLog("requestLatestPush", "TotalUserEnd", "total::" + total, "index::" + index, "catID::" + catID, "catGB::" + catGB, "contentID::" + contentID, "totalUser::" + totalUser);
								
								if (0 < totalUser) {
									int partitionNum = Integer.parseInt(GlobalCom.isNull(SmartUXProperties.getProperty("latepush.dbquery.partitionNum"), "10000"));
									int partitionLoopNum = (int) Math.floor((double)totalUser / partitionNum);
									
									//나눠서 처리한다.
									for(int loopNum = 0; loopNum <= partitionLoopNum; loopNum++){
										int realNum = loopNum * partitionNum;
										int startNum = realNum + 1;
										int endNum = realNum + partitionNum;
										
										LatestContentVO paramVO = new LatestContentVO();
										paramVO.setCategoryID(catID);
										paramVO.setStartNumber(startNum);
										paramVO.setEndNumber(endNum);
										
										mLatestLog.middleLog("requestLatestPush", "Search", "total::" + total, "index::" + index, "catID::" + catID, "catGB::" + catGB, "contentID::" + contentID, "Partition::" + startNum + " AND " + endNum);
										
										//위에서 이미 종영된 카테고리는 걸러냈으니 여기선 안걸러내도 된다.
										List userList = mLatestScheduleService.getUserList(paramVO);
										mLatestLog.middleLog("requestLatestPush", "Cate", "total::" + total, "index::" + index, "catID::" + catID, "catGB::" + catGB, "contentID::" + contentID, "size::" + userList.size());
										
										//전송 컨텐츠 내역을 따로 쌓는다.
										mLatestLog.middleLog("requestLatestPush", "total::" + total, "index::" + index, "catID::" + catID, "catGB::" + catGB, "contentID::" + contentID, "contentName::" + contentName, "seriesNum::" + seriesNum, "catSeriesNum::" + catSeriesNum, "size::" + userList.size());
										
										if (!GlobalCom.isEmpty(userList)) {
											Map<String, String> transferInfo = getPushServerInfo();
											
											int totalUserCnt = userList.size();
											
											// regid start end
											String regIdList = "";
											
											int limitCnt = Integer.parseInt(pageCount);
											int startIdx = 0;
											int endIdx = totalUserCnt < limitCnt ? totalUserCnt : limitCnt;
											
											StatPushLogger pushLogger = new StatPushLogger();
											
											String code;
											
											// 발송 로그
											StatPushLogVo logVO = new StatPushLogVo();
											logVO.setResult_code(SmartUXProperties.getProperty("push.code.success"));
											logVO.setClient_ip("127.0.0.1");
											logVO.setDev_info("SERVE");
											
											String pushID = new SimpleDateFormat("yyyyMMdd").format(new Date());
											
											LinkedHashMap<String, String> addInfo = new LinkedHashMap<String, String>();
											addInfo.put("PUSH_STYPE", "L");
											addInfo.put("PUSH_ID", pushID);
											addInfo.put("PUSH_LTYPE", "S");
											addInfo.put("PUSH_SENDCNT", String.valueOf(totalUserCnt));
											addInfo.put("PUSH_NAME", contentName);
											addInfo.put("PUSH_TYPE1", "");
											addInfo.put("PUSH_TYPE2", "");
											addInfo.put("PUSH_REGDATE", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
											addInfo.put("SVC_SUB_NAME", "01");
											
											logVO.setAddInfo(addInfo);
											pushLogger.writePushLog(logVO);
											
											while(startIdx < totalUserCnt){
												for(int i=startIdx;i<endIdx;i++){
													PushUserVO pushUserVO = (PushUserVO) userList.get(i);
													regIdList += "<reg_id>" + pushUserVO.getRegID() + "</reg_id>";
												}
												
												mLatestLog.middleLog("requestLatestPush", "Count", "total::" + total, "index::" + index, "totalUserCnt::" + totalUserCnt, "userCnt::" + endIdx, "catID::" + catID, "catGB::" + catGB, "contentID::" + contentID, "contentName::" + contentName, "seriesNum::" + seriesNum, "catSeriesNum::" + catSeriesNum);
												
												try {
													// 전송
													code = callLatePushAgent(1, index, transferInfo, catID, catGB, contentID, contentName, seriesNum, catSeriesNum, regIdList);
												} catch (ConnectTimeoutException ignored) {
													code = "8800";
												} catch (Exception e) {
													code = "9999";
													mLatestLog.middleLog("requestLatestPush", "callLatePushAgent", "Message::" + e.getMessage());
												}
												
												for(int i=startIdx;i<endIdx;i++){
													PushUserVO pushUserVO = (PushUserVO) userList.get(i);
													// 수신 로그
													logVO.setResult_code(code);
													logVO.setSid(pushUserVO.getRegID());
													addInfo.clear();
													addInfo.put("PUSH_STYPE", "L");
													addInfo.put("PUSH_ID", pushID);
													addInfo.put("PUSH_LTYPE", "R");
													addInfo.put("PUSH_SENDCNT", "");
													addInfo.put("PUSH_NAME", "");
													addInfo.put("PUSH_TYPE1", "");
													addInfo.put("PUSH_TYPE2", "");
													addInfo.put("PUSH_REGDATE", "");
													addInfo.put("SVC_SUB_NAME", "01");
													
													logVO.setAddInfo(addInfo);
													pushLogger.writePushLog(logVO);
												}
												
												//start,end 설정
												regIdList = "";
												startIdx = endIdx;
												endIdx += limitCnt;
												
												if(totalUserCnt < endIdx){
													endIdx = totalUserCnt;
												}
												
												//전송 후 대기
												Thread.sleep(Integer.parseInt(GlobalCom.isNull(sleepTime, "100")));
											}
										}
										Thread.sleep(1000);
									}
								}
								
								//처리 후 complete업데이트
								LatestContentVO completeVo = new LatestContentVO();
								completeVo.setCategoryID(catID);
								completeVo.setContentID(contentID);
								mLatestScheduleService.updatePushComplete(completeVo);
								
								//어느정도 시간이 났나?
								long waitTime = DateUtil.getTimeDifference(lockVO.getCheckedDate());
								
								//해당 시간이상 지났다면 다음 카테고리는 진행 안한다.
								long maxTime = Long.parseLong(GlobalCom.isNull(SmartUXProperties.getProperty("latepush.transaction.maxminute"), "86400000"));
								
								mLatestLog.middleLog("requestLatestPush", "TimeCheck", "total::" + total, "index::" + index, "catID::" + catID, "catGB::" + catGB, "contentID::" + contentID, "catSeriesNum::" + catSeriesNum, "waitTime::" + waitTime);
								
								//지정시간 이상 지났으니 더이상 진행하지 말고 나가자
								if(waitTime > maxTime) break;
							}
						}
					} catch (Exception e) {
						mLatestLog.errorLog("requestLatestPush", "Error::" + e.getMessage());
					} finally {
						FileLockUtil.unLockFile(LATEST_KEY);
					}
				}
			} else {
				mLatestLog.middleLog("requestLatestPush is BLOCK", "BLOCK");
			}
		}

		mLatestLog.endLog("requestLatestPush", "End");
	}

	@Override
	public void requestNewsPush() throws Exception {
 		Map<String, Integer> map = GlobalCom.getServerAdminIndex();

		int total = map.get("total");
		int index = map.get("index");

		Calendar cal = Calendar.getInstance();
		int minutes = cal.get(Calendar.MINUTE);

		mNewsLog.startLog("requestNewsPush", "Start", "total::" + total, "index::" + index);

		if (index == minutes % total) {
			// 푸시설정정보조회
			PushConfigVo pushConfigVo = pushConfigComponent.getPushConfig("T");
			if(null == pushConfigVo){
				pushConfigVo = new PushConfigVo();
			}
			String blockPush = GlobalCom.isNull(pushConfigVo.getBlock_push(), "N");
			String pageCount = GlobalCom.isNull(pushConfigVo.getPage_count(), "400");
			String sleepTime = GlobalCom.isNull(pushConfigVo.getSleep_time(), "100");
			
			if("N".equalsIgnoreCase(blockPush)){
				NewsVO newsVO = mNewsService.getMarkingNews(String.valueOf(index));
				
				if (null != newsVO) {
					mNewsLog.middleLog("requestNewsPush", "DataExist", newsVO.getNotiType(), newsVO.getNotiDetail(), newsVO.getTitle());
					
					//초기 셋팅
					HashMap<String, String> resultMap = new HashMap<String, String>();
					resultMap.put("regNumber", String.valueOf(newsVO.getRegNumber()));
					resultMap.put("index", String.valueOf(index));
					resultMap.put("result", "X");
					
					try {
						Map<String, String> transferInfo = getPushServerInfo();
						
						String pushType = newsVO.getPushType();
						
						String result = "X";
						
						StatPushLogger pushLogger = new StatPushLogger();
						
						if ("A".equals(pushType)) {
							// Announcement
							
							try{
								// 발송 로그
								StatPushLogVo logVO = new StatPushLogVo();
								logVO.setResult_code(SmartUXProperties.getProperty("push.code.success"));
								logVO.setClient_ip("127.0.0.1");
								logVO.setDev_info("SERVE");
								
								String pushID = String.valueOf(newsVO.getRegNumber());
								
								LinkedHashMap<String, String> addInfo = new LinkedHashMap<String, String>();
								addInfo.put("PUSH_STYPE", "N");
								addInfo.put("PUSH_ID", pushID);
								addInfo.put("PUSH_LTYPE", "S");
								addInfo.put("PUSH_SENDCNT", "10000000");
								addInfo.put("PUSH_NAME", newsVO.getTitle());
								addInfo.put("PUSH_TYPE1", newsVO.getShowType());
								addInfo.put("PUSH_TYPE2", GlobalCom.isEmpty(newsVO.getImageName()) ? "N" : "Y");
								addInfo.put("PUSH_REGDATE", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
								addInfo.put("SVC_SUB_NAME", "01");
								
								logVO.setAddInfo(addInfo);
								pushLogger.writePushLog(logVO);
								
								// 전송
								String code = callN1PushAgent_Announce(Integer.parseInt(GlobalCom.isNull(SmartUXProperties.getProperty("newspush.announce.retryCnt"), "1")), index, transferInfo, newsVO);
								
								// 수신 로그
								logVO.setResult_code(code);
								addInfo.clear();
								addInfo.put("PUSH_STYPE", "N");
								addInfo.put("PUSH_ID", pushID);
								addInfo.put("PUSH_LTYPE", "R");
								addInfo.put("PUSH_SENDCNT", "");
								addInfo.put("PUSH_NAME", "");
								addInfo.put("PUSH_TYPE1", "");
								addInfo.put("PUSH_TYPE2", "");
								addInfo.put("PUSH_REGDATE", "");
								addInfo.put("SVC_SUB_NAME", "01");
								
								logVO.setAddInfo(addInfo);
								pushLogger.writePushLog(logVO);
								
								if (SmartUXProperties.getProperty("push.code.success").equals(code)) {
									result = "Y";
								} else {
									result = "N";
									resultMap.put("result_code", code);
								}
							}catch(java.lang.Exception e){
								resultMap.put("result_code", SmartUXProperties.getProperty("flag.etc"));
								throw e;
							}
						} else if ("T".equals(pushType) || "E".equals(pushType) || "I".equals(pushType)) {
							// Targeting Input
							result = "Y";
							
							try {
								String savePath = GlobalCom.isNull(SmartUXProperties.getPathProperty("n1.save.path"), "/NAS_DATA/web/smartux/push/target/");
								
								File saveFile = new File(savePath + newsVO.getRegNumber() + ".txt");
								
								if (saveFile.exists() && saveFile.isFile()) {
									List<String> orgList = FileUtils.readLines(saveFile);
									
									if (!GlobalCom.isEmpty(orgList)) {
										//푸시 발송 이력 확인
										List<String> list = new ArrayList<String>(orgList);
										int recode = pushConfigComponent.readPushRecode(SmartUXProperties.getProperty("pushconfig.recode.path.stvnews"), "T", String.valueOf(newsVO.getRegNumber()), "E");
									
										//중단된 이후 부터 발송
										if(recode > 0){
											list = new ArrayList<String>(orgList.subList(recode,orgList.size()));
										}
										
										// regid start end
										String regIdList = "";

										int limitCnt = Integer.parseInt(pageCount);
										int startIdx = 0;
										int endIdx = list.size() < limitCnt ? list.size() : limitCnt;
										
										String code;
										
										// 발송 로그
										StatPushLogVo logVO = new StatPushLogVo();
										logVO.setResult_code(SmartUXProperties.getProperty("push.code.success"));
										logVO.setClient_ip("127.0.0.1");
										logVO.setDev_info("SERVE");
										
										String pushID = String.valueOf(newsVO.getRegNumber());
										
										LinkedHashMap<String, String> addInfo = new LinkedHashMap<String, String>();
										addInfo.put("PUSH_STYPE", "N");
										addInfo.put("PUSH_ID", pushID);
										addInfo.put("PUSH_LTYPE", "S");
										addInfo.put("PUSH_SENDCNT", String.valueOf(list.size()));
										addInfo.put("PUSH_NAME", newsVO.getTitle());
										addInfo.put("PUSH_TYPE1", newsVO.getShowType());
										addInfo.put("PUSH_TYPE2", GlobalCom.isEmpty(newsVO.getImageName()) ? "N" : "Y");
										addInfo.put("PUSH_REGDATE", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
										addInfo.put("SVC_SUB_NAME", "01");
										
										logVO.setAddInfo(addInfo);
										pushLogger.writePushLog(logVO);
										
										while(startIdx < list.size()){
											for(int i=startIdx;i<endIdx;i++){
												regIdList += "<reg_id>" + list.get(i) + "</reg_id>";
											}
											
											try {
												// 전송
												code = callN1PushAgent(index, transferInfo, newsVO, regIdList);
											} catch (ConnectTimeoutException ignored) {
												code = "8800";
											} catch (Exception e) {
												code = "9999";
												mNewsLog.middleLog("requestNewsPush", "callN1PushAgent", "Message::" + e.getMessage());
											}
											
											for(int i=startIdx;i<endIdx;i++){
												// 수신 로그
												logVO.setResult_code(code);
												logVO.setSid(list.get(i));
												addInfo.clear();
												addInfo.put("PUSH_STYPE", "N");
												addInfo.put("PUSH_ID", pushID);
												addInfo.put("PUSH_LTYPE", "R");
												addInfo.put("PUSH_SENDCNT", "");
												addInfo.put("PUSH_NAME", "");
												addInfo.put("PUSH_TYPE1", "");
												addInfo.put("PUSH_TYPE2", "");
												addInfo.put("PUSH_REGDATE", "");
												addInfo.put("SVC_SUB_NAME", "01");
												
												logVO.setAddInfo(addInfo);
												pushLogger.writePushLog(logVO);
											}
											
											//전송 후 기록
											pushConfigComponent.savePushRecode(SmartUXProperties.getProperty("pushconfig.recode.path.stvnews"), "T", String.valueOf(newsVO.getRegNumber()), "E", String.valueOf(recode+endIdx));
											
											//start,end 설정
											regIdList = "";
											startIdx = endIdx;
											endIdx += limitCnt;
											
											if(list.size() < endIdx){
												endIdx = list.size();
											}
											
											//전송 후 대기
											Thread.sleep(Integer.parseInt(GlobalCom.isNull(sleepTime, "100")));
										}
									}
								}
							} catch (IOException ignored) {
								result = "N";
							}
						}
						
						// 무한 재전송 방지를 위해 결과가 성공(Y)이 아니면 무조건 실패로 설정
						if (!"Y".equalsIgnoreCase(result)) {
							result = "X";
						}
						
						resultMap.put("result", result);
					} catch (Exception ignored) {
					} finally {
						mNewsService.updateNewsStatus(resultMap);
					}
				}
			} else{
				mNewsLog.middleLog("requestNewsPush is BLOCK", "BLOCK");
			}
		}

		mNewsLog.endLog("requestNewsPush", "End");
	}

	/**
	 * 최신회차 Push 전송
	 *
	 * @param pushCnt Push 전송 횟수
	 * @param myNum 서버 인덱스
	 * @param transferInfo Push 서버 정보 객체
	 * @param cateId 카테고리 아이디
	 * @param contentId 컨텐츠 아이디
	 * @param contentName 컨텐츠명
	 * @param seriesNo 시리즈 번호
	 * @param cateSeriesNo 카테고리 시리즈 번호
	 * @param regId 전송할 Push Reg ID
	 * @return 결과코드 (0000 : 성공)
	 * @throws Exception
	 */
	public String callLatePushAgent(int pushCnt, int myNum, Map<String, String> transferInfo, String cateId, String cat_gb, String contentId, String contentName, String seriesNo, String cateSeriesNo, String regId) throws Exception {
		String msgData = "\\\"result\\\":{\\\"noti_type\\\":\\\"LAT\\\",\\\"cat_id\\\":\\\""+cateId+"\\\",\\\"album_id\\\":\\\""+contentId+"\\\",\\\"album_series\\\":\\\""+seriesNo+"\\\",\\\"cate_series\\\":\\\""+cateSeriesNo+"\\\",\\\"album_name\\\":\\\""+GlobalCom.replaceDoubleQuotation(contentName)+"\\\",\\\"cat_gb\\\":\\\""+cat_gb+"\\\"}";
		String pushParam = "<request><msg><![CDATA[" + msgData + "]]></msg><items><item></item></items><users>"+GlobalCom.isNull(regId)+"</users></request>";
		
		String result = GlobalCom.callHttpClientPost(transferInfo.get("pushHost"), Integer.parseInt(transferInfo.get("pushPort")), transferInfo.get("pushAddr")
				, pushParam, Integer.parseInt(transferInfo.get("pushTimeout")), transferInfo.get("pushAccType"), transferInfo.get("pushProtocol"), "UTF-8", "application/xml");

		XmlUtil XU = new XmlUtil();
		HashMap<String,String> hMap = XU.pushXmlParser(result, "text", 2);
		String code = GlobalCom.isNull(hMap.get("flag"));
		String message = GlobalCom.isNull(hMap.get("message"));
		String wait_time = "";

		if ("".equals(code)) {//flag는 성공일때 code는 실패일때 XML형식이다.
			code = GlobalCom.isNull(hMap.get("code"));
			wait_time = GlobalCom.isNull(hMap.get("wait_time"));
		}

		if (!SmartUXProperties.getProperty("push.code.success").equals(code) && !"".equals(wait_time)) {
			sleepTime(wait_time);
			code = callLatePushAgent(++pushCnt, myNum, transferInfo, cateId, cat_gb, contentId, contentName, seriesNo, cateSeriesNo, regId);
		}

		return code;
	}

	/**
	 * N1 Type의 Announcement Push 전송
	 *
	 * @param retryCnt 재전송 카운트
	 * @param myNum 서버 인덱스
	 * @param transferInfo Push 서버 정보 객체
	 * @param vo 전송 데이터 객체
	 * @return 결과값 (Y : 성공)
	 * @throws Exception
	 */
	private String callN1PushAgent_Announce(int retryCnt, int myNum, Map<String, String> transferInfo, NewsVO vo) throws Exception {
		String notiType = vo.getNotiType();
		String notiDetail = vo.getNotiDetail();
		String title = vo.getTitle();

		mNewsLog.middleLog("[newsPushRequest-callN1PushAgent_Announce][S][MyNumber:" + myNum + "][retryCnt:" + retryCnt + "][" + notiType + "][" + notiDetail + "][" + title + "]");

		String pushParam = "<request><msg><![CDATA[" + makeN1Message(vo) + "]]></msg>";

		int multiCount;

		if (GlobalCom.isEmpty(vo.getImageURL())) {
			multiCount = Integer.parseInt(GlobalCom.isNull(SmartUXProperties.getProperty("n1.multi.count.text"), "0"));
		} else {
			multiCount = Integer.parseInt(GlobalCom.isNull(SmartUXProperties.getProperty("n1.multi.count.img"), "0"));
		}

		if (0 < multiCount) {
			pushParam += "<items><item><![CDATA[gcm_multi_count!^" + multiCount + "]]></item></items></request>";
		} else {
			pushParam += "</request>";
		}

		mNewsLog.middleLog("[newsPushRequest-callN1PushAgent_Announce][R][MyNumber:" + myNum + "][retryCnt:" + retryCnt + "][" + notiType + "][" + title + "][" + pushParam + "]");
		String httpResult = GlobalCom.callHttpClientPost(transferInfo.get("pushHost"), Integer.parseInt(transferInfo.get("pushPort")), transferInfo.get("pushAddr_Announce")
				, pushParam, Integer.parseInt(transferInfo.get("pushTimeout")), transferInfo.get("pushAccType"), transferInfo.get("pushProtocol"), "UTF-8");

		mNewsLog.middleLog("[newsPushRequest-callN1PushAgent_Announce][M][MyNumber:" + myNum + "][retryCnt:" + retryCnt + "][" + notiType + "][" + title + "][" + pushParam + "]");

		XmlUtil XU = new XmlUtil();
		HashMap<String,String> hMap = XU.pushXmlParser(httpResult, "text", 2);
		String code = GlobalCom.isNull(hMap.get("flag"));
		String message = GlobalCom.isNull(hMap.get("message"));

		if("".equals(code)){ //flag는 성공일때 code는 실패일때 XML형식이다.
			code = GlobalCom.isNull(hMap.get("code"));
		}

		if(SmartUXProperties.getProperty("push.code.success").equals(code)){
			mNewsLog.middleLog("[newsPushRequest-callN1PushAgent_Announce][E][MyNumber:" + myNum + "][retryCnt:" + retryCnt + "][" + notiType + "][" + title + "][" + pushParam + "][" + code + "][" + message + "]-[SUCCESS]");
		}else{
			mNewsLog.middleLog("[newsPushRequest-callN1PushAgent_Announce][E][MyNumber:" + myNum + "][retryCnt:" + retryCnt + "][" + notiType + "][" + title + "][" + pushParam + "][" + code + "][" + message + "]-[FAIL]");

			if(0 < retryCnt){
				sleepTime(GlobalCom.isNull(SmartUXProperties.getProperty("newspush.announce.retrySeelp"), "10000"));
				code = callN1PushAgent_Announce(--retryCnt, myNum, transferInfo, vo);
			}
		}

		return code;
	}

	/**
	 * N1 Type의 개별 Push 전송
	 *
	 * @param myNum 서버 인덱스
	 * @param transferInfo Push 서버 정보 객체
	 * @param vo 전송 데이터 객체
	 * @param regId 전송할 Push Reg ID
	 * @throws Exception
	 * @return 결과코드 (0000 : 성공)
	 */
	public String callN1PushAgent(int myNum, Map<String, String> transferInfo, NewsVO vo, String regId) throws Exception {
		String pushParam = "<request><msg><![CDATA[" + makeN1Message(vo) + "]]></msg><items><item></item></items><users>" + GlobalCom.isNull(regId) + "</users></request>";

		String httpResult = GlobalCom.callHttpClientPost(transferInfo.get("pushHost"), Integer.parseInt(transferInfo.get("pushPort")), transferInfo.get("pushAddr")
				, pushParam, Integer.parseInt(transferInfo.get("pushTimeout")), transferInfo.get("pushAccType"), transferInfo.get("pushProtocol"), "UTF-8", "application/xml");

		XmlUtil XU = new XmlUtil();
		HashMap<String,String> hMap = XU.pushXmlParser(httpResult, "text", 2);
		String code = GlobalCom.isNull(hMap.get("flag"));
		String message = GlobalCom.isNull(hMap.get("message"));
		String wait_time = "";

		if ("".equals(code)){ //flag는 성공일때 code는 실패일때 XML형식이다.
			code = GlobalCom.isNull(hMap.get("code"));
			wait_time = GlobalCom.isNull(hMap.get("wait_time"));
		}

		if (!SmartUXProperties.getProperty("push.code.success").equals(code) && !"".equals(wait_time)) {
			sleepTime(wait_time);
			code = callN1PushAgent(myNum, transferInfo, vo, regId);
		}

		return code;
	}

	/**
	 * N1 Type의 메시지 전문을 만든다.
	 *
	 * @param vo 전송 데이터 객체
	 * @return 메시지 전문
	 */
	private String makeN1Message(NewsVO vo) {
		SmartUXException exception = new SmartUXException();
		exception.setFlag(SmartUXProperties.getProperty("flag.format"));
		exception.setMessage(SmartUXProperties.getProperty("message.format"));

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

        msgData.append("\\\"PushCtrl\\\":\\\"MSG\\\",\\\"result\\\":{\\\"s_type\\\":\\\"").append(showType);
        msgData.append("\\\",\\\"n_type\\\":\\\"").append(netType);
        msgData.append("\\\",\\\"noti_type\\\":\\\"").append(notiGB);
        msgData.append("\\\",\\\"event_type\\\":\\\"").append(notiType);

        if (!GlobalCom.isEmpty(imgURL)) { msgData.append("\\\",\\\"img\\\":\\\"").append(imgURL); }

        msgData.append("\\\",\\\"title\\\":\\\"").append(GlobalCom.replaceDoubleQuotation(title));
        msgData.append("\\\",\\\"msg\\\":\\\"").append(GlobalCom.replaceDoubleQuotation(msg));
        msgData.append("\\\",\\\"id\\\":\\\"").append(regNumber);

        String p1 = "";
        String p2 = "";
        String p3 = "";
        String p4 = "";
        String p5 = "";

        if ("NOT".equals(notiType)) {
            String[] ary = notiDetail.split(",");

            if (2 <= ary.length) {
                p1 = ary[0];
                p2 = ary[1];
            } else {
				throw exception;
			}

            if (!GlobalCom.isEmpty(p1)) { msgData.append("\\\",\\\"p1\\\":\\\"").append(p1); }
            if (!GlobalCom.isEmpty(p2)) { msgData.append("\\\",\\\"p2\\\":\\\"").append(p2); }
        } else if("CON".equals(notiType)) {
            String[] ary = notiDetail.split("\\|\\|", -1);

            if (4 <= ary.length) {
                p1 = ary[0];
                p2 = ary[1];
                p3 = ary[2];
                p4 = ary[3];
                
                if(ary.length >4){
                	p5 = GlobalCom.isNull(ary[4], "I20");
                }
                
            } else {
				throw exception;
			}

            if (!GlobalCom.isEmpty(p1)) { msgData.append("\\\",\\\"p1\\\":\\\"").append(p1); }
            if (!GlobalCom.isEmpty(p2)) { msgData.append("\\\",\\\"p2\\\":\\\"").append(p2); }
            if (!GlobalCom.isEmpty(p3)) { msgData.append("\\\",\\\"p3\\\":\\\"").append(p3); }
            if (!GlobalCom.isEmpty(p4)) { msgData.append("\\\",\\\"p4\\\":\\\"").append(p4); }
            if (!GlobalCom.isEmpty(p5)) { msgData.append("\\\",\\\"p5\\\":\\\"").append(p5); }
        } else if("CAT".equals(notiType) || "SVO".equals(notiType) || "URL".equals(notiType) || "LIV".equals(notiType)) {
            if (!GlobalCom.isEmpty(notiDetail)) {
				msgData.append("\\\",\\\"p1\\\":\\\"").append(notiDetail);
			} else {
				throw exception;
			}
        }

        msgData.append("\\\"}");
        return msgData.toString();
	}

	/**
	 * 해당 시간 동안 Sleep을 시킨다.
	 *
	 * @param wait_time Sleep 시킬 시간(밀리타임)
	 */
	private void sleepTime(String wait_time) {
		int int_waitTime = Integer.parseInt(GlobalCom.isNull(wait_time,"3000"));
		try {Thread.sleep(int_waitTime); } catch (Exception ignored) {}
	}

	/**
	 * Push Module 정보가 담긴 데이터를 가져온다.
	 *
	 * @return Map 객체
	 */
	private Map<String, String> getPushServerInfo() {
		HashMap<String, String> transferInfo = new HashMap<String, String>();
		transferInfo.put("pushHost", GlobalCom.isNull(SmartUXProperties.getProperty("pushAgent.info.host")));
		transferInfo.put("pushPort", GlobalCom.isNull(SmartUXProperties.getProperty("pushAgent.info.port"),"80"));
		transferInfo.put("pushProtocol", GlobalCom.isNull(SmartUXProperties.getProperty("pushAgent.info.protocol")));
		transferInfo.put("pushMethod", GlobalCom.isNull(SmartUXProperties.getProperty("pushAgent.info.method")));
		transferInfo.put("pushTimeout", GlobalCom.isNull(SmartUXProperties.getProperty("pushAgent.info.timeout"),"3000"));
		transferInfo.put("pushAccType", GlobalCom.isNull(SmartUXProperties.getProperty("pushAgent.info.accType")));
		String pushAddr = GlobalCom.isNull(SmartUXProperties.getProperty("pushAgent.info.multiUrl")) + "?app_id="
				+ GlobalCom.isNull(SmartUXProperties.getProperty("pushAgent.info.appId")) + "&service_id="
				+ GlobalCom.isNull(SmartUXProperties.getProperty("pushAgent.info.serviceId"))
				+ "&multi_count=&push_type=G";
		transferInfo.put("pushAddr",pushAddr);
		String pushAddr_Announce = GlobalCom.isNull(SmartUXProperties.getProperty("pushAgent.info.announceUrl")) + "?app_id="
				+ GlobalCom.isNull(SmartUXProperties.getProperty("pushAgent.info.appId")) + "&service_id="
				+ GlobalCom.isNull(SmartUXProperties.getProperty("pushAgent.info.serviceId"));
		transferInfo.put("pushAddr_Announce",pushAddr_Announce);

		return transferInfo;
	}
}
