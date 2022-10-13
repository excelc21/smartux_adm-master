package com.dmi.smartux.push.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmi.smartux.admin.pushconfig.component.PushConfigComponent;
import com.dmi.smartux.admin.pushconfig.vo.PushConfigVo;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.CLog;
import com.dmi.smartux.common.util.DateUtil;
import com.dmi.smartux.common.util.FileLockUtil;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.common.util.XmlUtil;
import com.dmi.smartux.common.vo.FileLockVO;
import com.dmi.smartux.push.service.NextUiScheduleService;
import com.dmi.smartux.push.service.PushScheduleService;
import com.dmi.smartux.push.vo.NextUiPushContentVO;
import com.dmi.smartux.push.vo.NextUiPushUserParamVO;
import com.dmi.smartux.push.vo.NextUiPushUserVO;

@Service("NewPushSchedule")
public class PushScheduleServiceImpl implements PushScheduleService {
	public static final String NEXTUI_KEY = "nextuipush";

	private CLog mNextuiLog = new CLog(LogFactory.getLog("nextuiPushSchedule"));

	@Autowired
	NextUiScheduleService service;

	@Autowired
	PushConfigComponent pushConfigComponent;
	
	@Override
	public void requestNextuiPush() throws Exception {
		Map<String, Integer> map = GlobalCom.getServerAdminIndex();

		int total = map.get("total");
		int index = map.get("index");

		Calendar cal = Calendar.getInstance();
		int minutes = cal.get(Calendar.MINUTE);
		
		mNextuiLog.startLog("requestNextuiPush", "Start");

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
				
				long clearInterval = Long.parseLong(GlobalCom.isNull(SmartUXProperties.getProperty("nextuipush.filelock.clearInterval"), "18000000"));
				long subTime = Long.parseLong(GlobalCom.isNull(SmartUXProperties.getProperty("nextuipush.dbquery.beforeSearchTime"), "1800000"));
				
				boolean workable;
				
				FileLockVO lockVO = FileLockUtil.getFileLock(NEXTUI_KEY);
				
				if (null == lockVO) {
					// Lock파일이 없으면 => 작업 가능
					mNextuiLog.middleLog("requestNextuiPush", "LockFile Exist::false", "Workable::true");
					lockVO = new FileLockVO();
					lockVO.setCheckedDate(new Date(System.currentTimeMillis() - subTime));
					workable = true;
				} else {
					if (!lockVO.isLocked()){
						// Lock 파일이 없고, isLocked가 false 이면 => 작업가능
						mNextuiLog.middleLog("requestNextuiPush", "LockFile Exist::true", "isLocked::false", "interval::" + clearInterval);
						workable = true;
					} else {
						long checkTime = DateUtil.getTimeDifference(lockVO.getCheckedDate());
						
						if (checkTime >= clearInterval) {
							// Lock 파일이 있고, isLocked가 true 이지만, interval 시간보다 크면 => 작업가능
							mNextuiLog.middleLog("requestNextuiPush", "Check Time Over::true", "checkTime::" + checkTime, "interval::" + clearInterval);
							mNextuiLog.middleLog("requestNextuiPush", "LockFile Exist::true", "isLocked::true", "Workable::true");
							workable = true;
						} else {
							// Lock 파일이 있고, isLocked가 true 이고, interval 시간보다 크면 => 작업 불가
							mNextuiLog.middleLog("requestNextuiPush", "Check Time Over::false", "checkTime::" + checkTime, "interval::" + clearInterval);
							mNextuiLog.middleLog("requestNextuiPush", "LockFile Exist::true", "isLocked::true", "Workable::false");
							workable = false;
						}
					}
				}
				
				if (workable) {
					// Task 시작
					try {
						List<NextUiPushContentVO> contLists =  service.getNextUiPushContentList();
						mNextuiLog.middleLog("requestNextuiPush", "Work", "total::" + total, "index::" + index, "Count::" + contLists.size());
						
						FileLockUtil.lockFile(NEXTUI_KEY, lockVO);
						
						// 예약발송이 있으면 실행
						if (!GlobalCom.isEmpty(contLists)) {
							// 예약 컨텐츠 별 사용자 push 발송
							for( NextUiPushContentVO content : contLists){
								// 시청예약 db 컬럼 varchar2가 아닌 date 형식 잘못 생성
								// contents_name 앞에 데이터 삽입 (ex: 48|컨텐츠명)
								try{
									String[] contentTemp = content.getContents_name().split("\\|");
									content.setEnd_date(contentTemp[0]);
									content.setContents_name(contentTemp[1]);
								} catch (Exception ex){
									//처리 후 complete업데이트
									content.setComplete_yn("X");
									service.updatePushComplete(content);
									mNextuiLog.errorLog("requestNextuiPush", "Contents_name split Error");
								}
								
								if( !"X".equals(content.getComplete_yn())){
									// 발송 전 Default 표기
									content.setComplete_yn("Y");
									// 컨텐츠 별 실패 건수 확인
									int failCnt = 0; 
									
									// 예약푸쉬 발송 사용자 총 건수 조회
									int totalUser = service.getNextUiPushUserCount(content);
									
									if (0 < totalUser) {
										int partitionNum = Integer.parseInt(GlobalCom.isNull(SmartUXProperties.getProperty("nextuipush.dbquery.partitionNum"), "10000"));
										int partitionLoopNum = (int) Math.floor((double)totalUser / partitionNum);
										
										//나눠서 처리한다.
										for(int loopNum = 0; loopNum <= partitionLoopNum; loopNum++){
											int realNum = loopNum * partitionNum;
											int startNum = realNum + 1;
											int endNum = realNum + partitionNum;
											
											NextUiPushUserParamVO paramVO = new NextUiPushUserParamVO();
											paramVO.setContents_id(content.getContents_id());;
											paramVO.setCategory_gb(content.getCategory_gb());
											paramVO.setStart_num(startNum);;
											paramVO.setEnd_num(endNum);
											
											mNextuiLog.middleLog("requestNextuiPush", "Search", "total::" + total, "index::" + index, "contentID::" + paramVO.getContents_id(), "Partition::" + startNum + " AND " + endNum);
											
											//위에서 이미 종영된 카테고리는 걸러냈으니 여기선 안걸러내도 된다.
											List<NextUiPushUserVO> userList = service.getNextUiPushUserList(paramVO);
											
											mNextuiLog.middleLog("requestNextuiPush", "Cate", "total::" + total, "index::" + index, "contentID::" + paramVO.getContents_id(), "size::" + userList.size());
											mNextuiLog.middleLog("requestNextuiPush", "total::" + total, "index::" + index, "catID::" + content.getCategory_id(), "contentID::" + content.getContents_id(), "contentName::" + content.getContents_name(), "seriesYn::" + content.getSeries_yn(), "seriesNo::" + content.getSeries_no(), "size::" + userList.size());
											
											if (!GlobalCom.isEmpty(userList)) {
												// 푸쉬 발송 정보 조회
												Map<String, String> transferInfo = getPushAgentInfo();
												
												int totalUserCnt = userList.size();
												int userCnt = totalUserCnt;
												int limitCnt = Integer.parseInt(pageCount);
												
												for (Object obj : userList) {
													NextUiPushUserVO push_user = (NextUiPushUserVO) obj;
													
													//String id = GlobalCom.isNull(push_user.getSa_id());
													
													// 전송
													String code = callNextUiPushAgent(1, index, transferInfo, content, push_user);
													if( !"0000".equals(code) ){
														content.setComplete_yn("X");
														failCnt++;
													}
													userCnt--;
													
													if(userCnt % limitCnt == 0) Thread.sleep(Integer.parseInt(GlobalCom.isNull(sleepTime, "100")));
												}
											}
											
											Thread.sleep(1000);
										}
									}
									
									//처리 후 complete업데이트
									service.updatePushComplete(content);
									
									//어느정도 시간이 났나?
									long waitTime = DateUtil.getTimeDifference(lockVO.getCheckedDate());
									
									//해당 시간이상 지났다면 다음 카테고리는 진행 안한다.
									long maxTime = Long.parseLong(GlobalCom.isNull(SmartUXProperties.getProperty("nextuipush.transaction.maxminute"), "86400000"));
									mNextuiLog.middleLog("requestNextuiPush", "TimeCheck", "total::" + total, "fail::" + failCnt, "index::" + index, "catID::" + content.getCategory_id(), "contentID::" + content.getContents_id(), "waitTime::" + waitTime);
									
									//지정시간 이상 지났으니 더이상 진행하지 말고 나가자
									if(waitTime > maxTime) break;
								}
							}
						}
						
					} catch (Exception e) {
						mNextuiLog.errorLog("requestNextuiPush", "Error::" + e.getMessage());
					} finally {
						FileLockUtil.unLockFile(NEXTUI_KEY);
					}
				}
			} else {
				mNextuiLog.middleLog("requestNextuiPush is BLOCK", "BLOCK");
			}
		}

		mNextuiLog.endLog("requestNextuiPush", "End");
	}
	
	/**
	 * Push Agent 정보를 가져온다.
	 * @return Push Agent 정보
	 */
	private Map<String, String> getPushAgentInfo() {
		String pushAddr = "";
		Map<String, String> transferInfo = new HashMap<String, String>();
		
		transferInfo.put("pushHost", GlobalCom.isNull(SmartUXProperties.getProperty("pushAgent.info.host")));
		transferInfo.put("pushPort", GlobalCom.isNull(SmartUXProperties.getProperty("pushAgent.info.port"),"80"));
		transferInfo.put("pushProtocol", GlobalCom.isNull(SmartUXProperties.getProperty("pushAgent.info.protocol")));
		transferInfo.put("pushMethod", GlobalCom.isNull(SmartUXProperties.getProperty("pushAgent.info.method")));
		transferInfo.put("pushTimeout", GlobalCom.isNull(SmartUXProperties.getProperty("pushAgent.info.timeout"),"3000"));
		transferInfo.put("pushAccType", GlobalCom.isNull(SmartUXProperties.getProperty("pushAgent.info.accType")));
		
		transferInfo.put("nextuiRetryCnt", GlobalCom.isNull(SmartUXProperties.getProperty("nextuipush.info.retrycnt"),"3"));
		
		pushAddr = GlobalCom.isNull(SmartUXProperties.getProperty("pushAgent.info.url"))
				+ "?app_id=" + GlobalCom.isNull(SmartUXProperties.getProperty("nextuipush.info.appId"))
				+ "&service_id=" + GlobalCom.isNull(SmartUXProperties.getProperty("nextuipush.info.serviceId"))
				;
		transferInfo.put("pushAddr", pushAddr);

		return transferInfo;
	}
	
	/**
	 * @return 
	 * @Method Name : callNextUiSinglePushAgent
	 * @Comment : 예약 푸시에이전트
	 */
	public String callNextUiPushAgent(int pushCnt, int myNum, Map<String, String> transferInfo, NextUiPushContentVO content, NextUiPushUserVO push_user) throws Exception {
		String show_type = "NOT";
		String noti_type = "REB";
		String stat_type = "REB";
		String stat_val = GlobalCom.getTodayFormat();
		
//		mNextuiLog.middleLog("[nextuiPushRequest-callNextUiPushAgent][S][MyNumber:"+myNum+"][PushCnt:"+pushCnt+"]["+content.getContents_id()+"]["+push_user.getSa_id()+"]");

		String msgData = "\\\"result\\\":{"
				+ "\\\"show_type\\\":" + "\\\"" + GlobalCom.isNull(show_type) + "\\\""
				+ "," + "\\\"noti_type\\\":" + "\\\"" + GlobalCom.isNull(noti_type) + "\\\""
				+ "," + "\\\"cat_id\\\":" + "\\\"" + GlobalCom.isNull(content.getCategory_id()) + "\\\""
				+ "," + "\\\"album_id\\\":" + "\\\"" + GlobalCom.isNull(content.getContents_id()) + "\\\""
				+ "," + "\\\"album_name\\\":" + "\\\"" + GlobalCom.isNull(content.getContents_name()) + "\\\""
				+ "," + "\\\"series_yn\\\":" + "\\\"" + GlobalCom.isNull(content.getSeries_yn()) + "\\\""
				+ "," + "\\\"series_no\\\":" + "\\\"" + GlobalCom.isNull(content.getSeries_no()) + "\\\""
				+ "," + "\\\"watch_end_date\\\":" + "\\\"" + GlobalCom.isNull(content.getEnd_date()) + "\\\""
				+ "," + "\\\"buy_date\\\":" + "\\\"" + GlobalCom.isNull(push_user.getBuy_date()) + "\\\""
				+ "," + "\\\"cat_gb\\\":" + "\\\"" + GlobalCom.isNull(content.getCategory_gb()) + "\\\""
				+ "," + "\\\"stat_type\\\":" + "\\\"" + GlobalCom.isNull(stat_type) + "\\\""
				+ "," + "\\\"stat_val\\\":" + "\\\"" + GlobalCom.isNull(stat_val) + "\\\"}";
		
		String pushParam = "<request><reg_id>"+GlobalCom.isNull(push_user.getReg_id())+"</reg_id><msg><![CDATA[" + msgData + "]]></msg></request>";
		
		String result = GlobalCom.callHttpClientPost(transferInfo.get("pushHost"), Integer.parseInt(transferInfo.get("pushPort")), transferInfo.get("pushAddr")
				, pushParam, Integer.parseInt(transferInfo.get("pushTimeout")), transferInfo.get("pushAccType"), transferInfo.get("pushProtocol"), "UTF-8");
		
//		mNextuiLog.middleLog("[nextuiPushRequest-callNextUiPushAgent][R][MyNumber:"+myNum+"][PushCnt:"+pushCnt+"]["+content.getCategory_id()+"]["+push_user.getSa_id()+"]["+pushParam+"]");
				
		XmlUtil XU = new XmlUtil();
		HashMap<String,String> hMap = XU.pushXmlParser(result, "text", 2);
		String code = GlobalCom.isNull(hMap.get("flag"));
		String message = GlobalCom.isNull(hMap.get("message"));
		String wait_time = "";

		if(code==null || "".equals(code)){//flag는 성공일때 code는 실패일때 XML형식이다.
			code = GlobalCom.isNull(hMap.get("code"));
			wait_time = GlobalCom.isNull(hMap.get("wait_time"));
		}
		
		if (!SmartUXProperties.getProperty("push.code.success").equals(code)) {
			mNextuiLog.middleLog("requestNextuiPush", "Retry", "catID::" + content.getCategory_id(), "contentID::" + content.getContents_id(), "saID::" + push_user.getSa_id(), "RetryCnt::" + transferInfo.get("nextuiRetryCnt"), "pushCnt::" + pushCnt, "code::" + code, "waitTime::" + wait_time);
			if(!"".equals(wait_time)){
				sleepTime(wait_time);
				code = callNextUiPushAgent(pushCnt, myNum, transferInfo, content, push_user);
			} else {
				if( pushCnt < Integer.parseInt(transferInfo.get("nextuiRetryCnt")) ){
					code = callNextUiPushAgent(++pushCnt, myNum, transferInfo, content, push_user);
				}
			}
			
		}
		
		return code;
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
}