package com.dmi.smartux.bonbang.service.impl;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmi.smartux.admin.retry.dao.RetryDao;
import com.dmi.smartux.bonbang.dao.RegistrationIDDao;
import com.dmi.smartux.bonbang.dao.ReservedProgramDao;
import com.dmi.smartux.bonbang.service.ReservedProgramService;
import com.dmi.smartux.bonbang.vo.RegistrationIDParamVO;
import com.dmi.smartux.bonbang.vo.ReservedProgramParamVO;
import com.dmi.smartux.bonbang.vo.ReservedProgramVO;
import com.dmi.smartux.common.exception.SmartUXException;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.service.CacheService;
import com.dmi.smartux.common.util.GlobalCom;
import com.dmi.smartux.ios_push.IOS_PushCall;
import com.dmi.smartux.push.PushCall;
import com.dmi.smartux.push.service.PushService;
import com.dmi.smartux.push.vo.PushResultVO;

/**
 * 시청예약 관리 서비스 클래스
 * @author YJ KIM
 *
 */
@Service
public class ReservedProgramServiceImpl implements ReservedProgramService{
	private final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	CacheService service;
	
	@Autowired
	RetryDao retryDao;

	@Autowired
	ReservedProgramDao dao;
	
	@Autowired
	RegistrationIDDao rdao;
	
	@Autowired
	PushService psService;
	
	//@Autowired
	//IOS_PushService ios_psService;
	
//	@Override
//	public PushResultVO setPushNoti(ReservedProgramParamVO rVO) throws Exception {
//		logger.debug("setPushNoti START");
//		PushResultVO prVO = new PushResultVO();
//		
//		//=====Push GW 전송값 세팅 & 호출
//		PushSocketVO psVO = psService.getPushGWSocket();
////		if(rVO.getApp_type().equalsIgnoreCase("ux")){
////			psVO.setRegist_id(rVO.getCtn_reg_id());
////		}else{
////			psVO.setRegist_id(rVO.getStb_reg_id());
////		}
//		
//		//테스트
//		for(int i=0;i<rVO.getReg_id().length;i++){
//			psVO.setNoti_message("setPushNoti"+GlobalCom.colsep+rVO.getIs_push());
//			psVO.setRegist_id(rVO.getReg_id()[i]);
//			prVO = psService.setNoti(psVO);
//		}
////		IOS_PushSocketVO ipsVO = ios_psService.getPushGWSocket();
////		for(int i=0;i<rVO.getReg_id().length;i++){
////			ipsVO.setNoti_message("setPushNoti"+GlobalCom.colsep+rVO.getIs_push());
////			ipsVO.setUser_id(rVO.getReg_id()[i]);
////			prVO = ios_psService.setNoti(ipsVO); 
////		}
//		
//		
//		logger.debug("setPushNoti END");
//		return prVO;
//	}

	@Override
	@Transactional(rollbackFor={
								SocketException.class,
								SocketTimeoutException.class})	
	public PushResultVO addReservedProgram(ReservedProgramParamVO rVO) throws Exception {
		logger.debug("addReservedProgram START");
		
		PushResultVO prVO = new PushResultVO();
		
		int dataCnt =  dao.getReservedProgramDataCnt(rVO);
		if(dataCnt > 0){
			//기존 데이터 존재 로직 처리 - Push쪽 문제로 이경우도 성공으로 표시하라고 함. 130124
			//prVO.setFlag(SmartUXProperties.getProperty("flag.bedata"));
			//prVO.setMessage(SmartUXProperties.getProperty("message.bedata"));
			prVO.setFlag(SmartUXProperties.getProperty("flag.success"));
			prVO.setMessage(SmartUXProperties.getProperty("message.success"));
		}else{
			
			int dataTimeCnt = dao.getReservedProgramDataTimeCnt(rVO);
			logger.debug("addReservedProgram dataTimeCnt = "+dataTimeCnt);
			if(dataTimeCnt > 0){
				//시간 중복 됨
				prVO.setFlag(SmartUXProperties.getProperty("flag.bedata"));
				prVO.setMessage(SmartUXProperties.getProperty("message.bedata"));
			}else{
				
				int dataListTotalCnt = dao.getReservedProgramListTotalCnt(rVO);
				
				//시청예약 최대 개수를 제한함
				String _maxDataCnt = SmartUXProperties.getProperty("ReservedProgramMaxCount");
				int maxDataCnt = Integer.parseInt(_maxDataCnt);
				logger.debug("addReservedProgram maxDataCnt = "+maxDataCnt);
				logger.debug("addReservedProgram dataListTotalCnt = "+dataListTotalCnt);
				if(dataListTotalCnt >= maxDataCnt){
					prVO.setFlag(SmartUXProperties.getProperty("flag.maxData"));
					prVO.setMessage(SmartUXProperties.getProperty("message.maxData"));
				}else{
					try{
						dao.setReservedProgramData(rVO);
						psService.pushSendForaddReservedProgram(rVO);						
						logger.debug("addReservedProgram DATA SET FINISH");
						
						prVO.setFlag(SmartUXProperties.getProperty("flag.success"));
						prVO.setMessage(SmartUXProperties.getProperty("message.success"));
						
					}catch(org.springframework.dao.DuplicateKeyException e){
						prVO.setFlag(SmartUXProperties.getProperty("flag.key1"));
						prVO.setMessage(SmartUXProperties.getProperty("message.key1"));
					}
				}
			}
		}

		logger.debug("addReservedProgram END");
		return prVO;
	}
	
	

	@Override
	@Transactional(rollbackFor={
			SocketException.class,
			SocketTimeoutException.class})
	public PushResultVO removeReservedProgram(ReservedProgramParamVO rVO) throws Exception {
		logger.debug("removeReservedProgram START");
		
		PushResultVO prVO = new PushResultVO();
		
		int delResult = dao.delReservedProgramData(rVO);
//		if(delResult <= 0){
//			SmartUXException exception = new SmartUXException();
//			exception.setFlag(SmartUXProperties.getProperty("flag.beNotData"));
//			exception.setMessage(SmartUXProperties.getProperty("message.beNotData"));
//			throw exception;
//		}
		
		//removeReservedProgram
		
		//=====Push GW 전송값 세팅 & 호출
		List<RegistrationIDParamVO> reg_list = rdao.getRegIDList(rVO.getSa_id(),rVO.getStb_mac(),rVO.getSma_mac(),rVO.getApp_type());
		
		if(reg_list.size() == 0){
			//Push Noti 하려는 곳이 존재 하지 않아도 성공으로 판단한다.
			prVO.setFlag(SmartUXProperties.getProperty("flag.success"));
			prVO.setMessage(SmartUXProperties.getProperty("message.success"));
		}else{
			RegistrationIDParamVO ridVO = null;
			for(int i=0;i<reg_list.size();i++){
				ridVO = reg_list.get(i);
				
				logger.debug("removeReservedProgram 단말 PUSH reg_list.size = "+reg_list.size());
				logger.debug("removeReservedProgram 단말 PUSH sma_mac = "+ridVO.getSma_mac());
				logger.debug("removeReservedProgram 단말 PUSH reg_id = "+ridVO.getReg_id());
				
				int retry = Integer.parseInt((String) service.getCache(retryDao, "setPushGateWayRetry", SmartUXProperties.getProperty("RetryDao.getPushGateWayRetry.cacheKey"), 0,"A0011"));
				
				//타사단말 Push 이용
				if(ridVO.getReg_id().equals("")){
					logger.debug("removeReservedProgram 타사단말 PUSH 호출!!!!");

					String noti_message =SmartUXProperties.getProperty("ios_pushgw.cp.noti_contenst3");
					String user_id = ridVO.getSma_mac();
					String user_param = "removeReservedProgram";
					
					IOS_PushCall iosPushCall = new IOS_PushCall(noti_message, user_id, user_param,retry, ridVO.getSa_id(), ridVO.getStb_mac(), ridVO.getSma_mac());
					iosPushCall.call();
					//Thread thread = new Thread(new IOS_PushThread(noti_message, user_id, user_param,retry, ridVO.getSa_id(), ridVO.getStb_mac(), ridVO.getSma_mac()));
					//thread.start();
				}else{
					//자사단말 Push 이용
					logger.debug("removeReservedProgram 자사단말 PUSH 호출!!!!");
					String noti_message ="removeReservedProgram";
					String reg_id = ridVO.getReg_id();
					
					PushCall pushCall = new PushCall(noti_message, reg_id, retry, ridVO.getSa_id(), ridVO.getStb_mac(), ridVO.getSma_mac());
					pushCall.call();
					//Thread thread = new Thread(new PushThread(noti_message, reg_id, retry, ridVO.getSa_id(), ridVO.getStb_mac(), ridVO.getSma_mac()));
					//thread.start();
				}
			}
			//Push G/W 에러가 발생하여도 DB에 정상적으로 입력되면 요청 결과를 성공 반환한다.
			prVO.setFlag(SmartUXProperties.getProperty("flag.success"));
			prVO.setMessage(SmartUXProperties.getProperty("message.success"));
		}
		return prVO;
	}

	@Override
	public List<ReservedProgramVO> getReservedProgramList(ReservedProgramParamVO rVO) throws Exception {
		logger.debug("getReservedProgramList START");
		
		List<ReservedProgramVO> result = null;
		
		result = dao.getReservedProgramList(rVO);
		
		logger.debug("getReservedProgramList END");
		return result;
	}

	@Override
	public int getReservedProgramListTotalCnt(ReservedProgramParamVO rVO)
			throws Exception {
		logger.debug("getReservedProgramListCtn START");
		int result = 0;		
		
		result = dao.getReservedProgramListTotalCnt(rVO);
		
		logger.debug("getReservedProgramListCtn result = "+result);
		logger.debug("getReservedProgramListCtn END");
		return result;
	}

	@Override
	public PushResultVO reqPushMessage(ReservedProgramParamVO rVO)
			throws Exception {
		logger.debug("reqPushMessage START");
		PushResultVO prVO = new PushResultVO();
		
		
		//=====Push GW 전송값 세팅 & 호출
		List<RegistrationIDParamVO> reg_list = rdao.getRegIDList(rVO.getSa_id(),rVO.getStb_mac(),rVO.getSma_mac(),rVO.getApp_type());
		if(reg_list.size() == 0){
			//Push Noti 하려는 곳이 존재 하지 않아도 성공으로 판단한다.
			prVO.setFlag(SmartUXProperties.getProperty("flag.success"));
			prVO.setMessage(SmartUXProperties.getProperty("message.success"));
		}else{
			RegistrationIDParamVO ridVO = null;
			for(int i=0;i<reg_list.size();i++){
				ridVO = reg_list.get(i);

				logger.debug("reqPushMessage 단말 PUSH reg_list.size = "+reg_list.size());
				logger.debug("reqPushMessage 단말 PUSH sma_mac = "+ridVO.getSma_mac());
				logger.debug("reqPushMessage 단말 PUSH reg_id = "+ridVO.getReg_id());
				
				int retry = Integer.parseInt((String) service.getCache(retryDao, "setPushGateWayRetry", SmartUXProperties.getProperty("RetryDao.getPushGateWayRetry.cacheKey"), 0,"A0011"));
				
				if(ridVO.getReg_id().equals("")){
					logger.debug("reqPushMessage 타사단말 PUSH 호출!!!!");
					
					String noti_message = SmartUXProperties.getProperty("ios_pushgw.cp.noti_contents1")
										  +" "
										  +rVO.getProgram_name()
										  +" "
										  +SmartUXProperties.getProperty("ios_pushgw.cp.noti_contents2");
					String user_id = ridVO.getSma_mac();
					String user_param ="reqPushMessage"+GlobalCom.colsep+
							rVO.getService_id()+"|"+
							rVO.getChannel_no()+"|"+
							rVO.getChannel_name()+"|"+
							rVO.getProgram_id()+"|"+
							rVO.getProgram_name()+"|"+
							rVO.getProgram_info()+"|"+
							rVO.getDefin_flag()+"|"+
							rVO.getProgram_stime();
					
					//Thread thread = new Thread(new IOS_PushThread(noti_message, user_id, user_param, retry, ridVO.getSa_id(), ridVO.getStb_mac(), ridVO.getSma_mac()));
					//thread.start();
				}else{
					//자사단말 Push 이용
					logger.debug("reqPushMessage 자사단말 PUSH 호출!!!!");
					//\f사용하면 에러가 발생함 reqPushMessage!^서비스ID\f채널번호\f채널명\f프로그램ID\f프로그램명\f프로그램연령\f프로그램화질\f프로그램시작시간
					// | 사용함 reqPushMessage!^서비스ID|채널번호|채널명|프로그램ID|프로그램명|프로그램연령|프로그램화질|프로그램시작시간
					String noti_message ="reqPushMessage"+GlobalCom.colsep+
							rVO.getService_id()+"|"+
							rVO.getChannel_no()+"|"+
							rVO.getChannel_name()+"|"+
							rVO.getProgram_id()+"|"+
							rVO.getProgram_name()+"|"+
							rVO.getProgram_info()+"|"+
							rVO.getDefin_flag()+"|"+
							rVO.getProgram_stime();
					String reg_id = ridVO.getReg_id();
					
					//Thread thread = new Thread(new PushThread(noti_message, reg_id, retry, ridVO.getSa_id(), ridVO.getStb_mac(), ridVO.getSma_mac()));
					//thread.start();
				}
			}
		}
		
		//Push G/W 에러가 발생하여도 DB에 정상적으로 입력되면 요청 결과를 성공 반환한다.
		prVO.setFlag(SmartUXProperties.getProperty("flag.success"));
		prVO.setMessage(SmartUXProperties.getProperty("message.success"));
		
		logger.debug("reqPushMessage END");
		return prVO;
	}

	@Override
	//@Transactional 140708
	public void removeAllReservedProgram(ReservedProgramParamVO rVO)
			throws Exception {
		
		int delResult = dao.removeAllReservedProgram(rVO);
		
		if(delResult <= 0){
			SmartUXException exception = new SmartUXException();
			exception.setFlag(SmartUXProperties.getProperty("flag.beNotData"));
			exception.setMessage(SmartUXProperties.getProperty("message.beNotData"));
			throw exception;
		}
	}
	
	
}
