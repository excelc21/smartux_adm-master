package com.dmi.smartux.admin.season.service.impl;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dmi.smartux.admin.season.dao.SeasonDao;
import com.dmi.smartux.admin.season.service.SeasonService;
import com.dmi.smartux.admin.season.vo.SeasonSearchVo;
import com.dmi.smartux.admin.season.vo.SeasonVo;
import com.dmi.smartux.common.property.SmartUXProperties;
import com.dmi.smartux.common.util.GlobalCom;


@Service("A.SeasonServiceImpl")
public class SeasonServiceImpl implements SeasonService {
	
	@Autowired
	SeasonDao dao;
	
	/**
	 * 즉시적용하기 위한 캐시 API호출
	 * @param url
	 * @param param
	 * @param timeout
	 * @param retrycnt
	 * @param protocolName
	 * @return
	 * @throws Exception
	 */
	@Override
	public Map<String,Object> applyCache(String url, String param, int timeout, int retrycnt, String protocolName) throws Exception{
		Map<String,Object> resultMap = new HashMap<String,Object>();
		Map<String,String> errorMap = new HashMap<String,String>();
		String res = "";
		String msg = "";
		long wait_time = 0;
		String result = "";
		String retryresult = "";
		String mathod = "GET";
		String hostsString = SmartUXProperties.getProperty("cache.ServerIPList");
		String [] hostList = hostsString.split("\\|");
		String portsString = SmartUXProperties.getProperty("cache.ServerPortList");
		String [] portList = portsString.split("\\|");
		String resultArr[] = null;
		int idx = -1; 
		
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		
		for(String host : hostList){
			idx++;
			try{
				result = GlobalCom.callHttpClient(host, Integer.parseInt(portList[idx]), url, param, timeout, protocolName, mathod);

				if(result.length() >= 4){
					resultArr = result.split("\\!\\^");
					res = resultArr[0];
					msg = resultArr[1];
					
					if(3 == resultArr.length){
						wait_time = Long.parseLong(resultArr[2]);
					}
					result = result.substring(0, 4);
				}
				
				if(SmartUXProperties.getProperty("flag.success").equals(result)||
					SmartUXProperties.getProperty("flag.cache.reload.waittime").equals(result)){
					
					if(SmartUXProperties.getProperty("flag.cache.reload.waittime").equals(result)){
						msg = SmartUXProperties.getProperty("message.cache.reload.waittime", sdf.format(wait_time));
					}
					break;
				}
			}catch(Exception e){
				for(int i=0; i < retrycnt; i++){
					try{
						retryresult = GlobalCom.callHttpClient(host, Integer.parseInt(portList[idx]), url, param, timeout, protocolName, mathod);
					}catch(Exception sube){}
					
					if(retryresult.length() >= 4){
						resultArr = retryresult.split("\\!\\^");
						
						if(3 == resultArr.length){
							wait_time = Long.parseLong(resultArr[2]);
						}
						retryresult = result.substring(0, 4);
						
						res = resultArr[0];
						try{
							msg = resultArr[1];
						}catch(Exception ex){
							res = SmartUXProperties.getProperty("flag.socket");
							msg = SmartUXProperties.getProperty("message.socket");
							
							if("".equals(retryresult)){
								res = SmartUXProperties.getProperty("flag.fail");
								msg = SmartUXProperties.getProperty("message.fail");
							}
							errorMap.put("res", res);
							errorMap.put("msg", msg);
							resultMap.put(Integer.toString(idx), errorMap);
							break;
						}
						retryresult = retryresult.substring(0, 4);
					}
					
					if(SmartUXProperties.getProperty("flag.success").equals(retryresult)||
						SmartUXProperties.getProperty("flag.cache.reload.waittime").equals(result)){
						if(SmartUXProperties.getProperty("flag.cache.reload.waittime").equals(result)){
							msg = SmartUXProperties.getProperty("message.cache.reload.waittime", sdf.format(wait_time));
						}
						break;
					}
				}
			}
		}
		
		resultMap.put("res",res);
		resultMap.put("msg",msg);
		return resultMap;
	}
	
	/**
	 * 시즌 목록 조회
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<SeasonVo> getSeasonList(SeasonSearchVo vo) throws Exception{
		return dao.getSeasonList(vo);
	}
	
	/**
	 * 시즌 목록 개수
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@Override
	public int getSeasonListCnt(SeasonSearchVo vo) throws Exception{
		return dao.getSeasonListCnt(vo);
	}
	
	/**
	 * 시즌 등록
	 * @param vo
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void insertProc(SeasonVo vo) throws Exception{

		//부모 시즌 먼저 생성
		String parent_id = dao.getSeasonId();
		SeasonVo parentVo = new SeasonVo();
		parentVo.setSeason_id(parent_id);
		parentVo.setSeason_title(vo.getSeason_title());
		parentVo.setSeries_yn(vo.getSeries_yn());
		parentVo.setReg_id(vo.getReg_id());
		parentVo.setMod_id(vo.getMod_id());
		parentVo.setApp_tp(vo.getApp_tp());	//I(I20), N(I30)
		dao.insertProc(parentVo);

		//자식 시즌 생성
		SeasonVo childVo = new SeasonVo();
		List<String> seasonData = vo.getSeasonData();
		String childData[] = null;
		String season_title = parentVo.getSeason_title();
		String season_name = "";
		String season_id = "";
		String content_id = "";
		String orders = "";
		String delYn = "";
		String appTp = "";
		
		for(int i=0;i<seasonData.size();i++){
			childData = seasonData.get(i).split("\\|");
			childVo = new SeasonVo();
			
			if(childData.length == 6){
				delYn = childData[4];
				
				if("N".equals(delYn)){
					season_name = childData[0];
					season_id = dao.getSeasonId();
					content_id = childData[1];
					orders = childData[3];
					appTp = childData[5];
					
					childVo.setSeason_name(season_name);
					childVo.setSeason_id(season_id);
					childVo.setContent_id(content_id);
					childVo.setReg_id(vo.getReg_id());
					childVo.setMod_id(vo.getMod_id());
					childVo.setSeason_title(season_title);
					childVo.setParent_season_id(parent_id);
					childVo.setOrders(Integer.parseInt(orders));
					childVo.setSeries_yn(vo.getSeries_yn());
					childVo.setApp_tp(appTp);
					
					dao.insertProc(childVo);
				}
			}
		}
	}
	
	/**
	 * 시즌 상세조회 (부모)
	 * @param season_id
	 * @return
	 * @throws Exception
	 */
	@Override
	public SeasonVo getSeasonDetail(String season_id) throws Exception{
		return dao.getSeasonDetail(season_id);
	}
	
	/**
	 * 시즌 상세조회 (자식)
	 * @param parent_season_id
	 * @return
	 * @throws Exception
	 */
	@Override
	public String getSeasonDetailList(String parent_season_id) throws Exception{
		List<SeasonVo> list = dao.getSeasonDetailList(parent_season_id);
		String childList = "";
		int i = 1;
		for(SeasonVo vo : list){
			childList += vo.getSeason_id() + "\\|" + vo.getSeason_name() + "\\|" + vo.getContent_id() 
					  + "\\|" + vo.getContent_name() + "\\|" + vo.getApp_tp();
			if(list.size() > i){
				childList += "\\@\\^";
			}
			i++;
		}
		return childList;
	}
	
	/**
	 * 시즌 수정
	 * @param vo
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void updateProc(SeasonVo vo) throws Exception {
		
		//부모 먼저 수정
		SeasonVo parentVo = new SeasonVo();
		parentVo.setSeason_id(vo.getSeason_id());
		parentVo.setSeason_title(vo.getSeason_title());
		parentVo.setSeries_yn(vo.getSeries_yn());
		parentVo.setMod_id(vo.getMod_id());
		dao.updateProc(parentVo);
		
		//자식 수정
		SeasonVo childVo = new SeasonVo();
		List<String> seasonData = vo.getSeasonData();
		String childData[] = null;
		String season_title = parentVo.getSeason_title();
		String season_name = "";
		String season_id = "";
		String content_id = "";
		String orders = "";
		String delYn = "";
		String newYn = "";
		String appTp = "";
		
		for(int i=0;i<seasonData.size();i++){
			childData = seasonData.get(i).split("\\|");
			childVo = new SeasonVo();
			
			if(childData.length == 7){
				newYn = childData[2];
				delYn = childData[4];
				
				if("N".equals(delYn)){
					season_name = childData[0];
					content_id = childData[1];
					orders = childData[3];
					appTp = childData[6];
					
					childVo.setSeason_name(season_name);
					childVo.setContent_id(content_id);
					childVo.setParent_season_id(parentVo.getSeason_id());
					childVo.setSeason_title(season_title);
					childVo.setOrders(Integer.parseInt(orders));
					childVo.setSeries_yn(vo.getSeries_yn());
					childVo.setApp_tp(appTp);

					if("Y".equals(newYn)){
						//신규 등록 된 시즌
						season_id = dao.getSeasonId();
						childVo.setSeason_id(season_id);
						childVo.setReg_id(vo.getReg_id());
						childVo.setMod_id(vo.getMod_id());
						
						dao.insertProc(childVo);
					}else{
						//기존 시즌 수정
						season_id = childData[5];
						childVo.setSeason_id(season_id);
						childVo.setMod_id(vo.getMod_id());
						
						dao.updateProc(childVo);
					}
				} else if ("Y".equals(delYn) && "N".equals(newYn)) {
					//삭제된 시즌
					season_id = childData[5];
					dao.deleteProc(season_id,"N");
				}
			}
		}
	}
	
	/**
	 * 시즌 삭제
	 * @param delList
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void deleteProc(String delList, String app_tp) throws Exception{
		String[] delArr = delList.split(",");
		
		for(int i=0;i<delArr.length;i++){
			//부모 삭제
			dao.deleteProc(delArr[i], "Y");
			//자식 삭제
			dao.deleteProc(delArr[i], "N");
			
			//서버 캐시 삭제 요청
			this.applyCacheById(delArr[i], app_tp);
		}
	}
	
	/**
	 * 시즌적용 카테고리 목록 조회
	 * @param albumId
	 * @return
	 * @throws Exception
	 */
	@Override
	public String getCategoryList(String albumId, String series_yn, String category_gb) throws Exception{
		return dao.getCategoryList(albumId, series_yn, category_gb);
	}
	
	/**
	 * 시즌 제외카테고리 조회
	 * @return
	 * @throws Exception
	 */
	@Override
	public String getExceptList() throws Exception{
		List<String> list = dao.getExceptList();
		String result = "";
		
		for(int i=0;i<list.size();i++){
			if(i==0){
				result = list.get(i); 
			}else{
				result += "|^" + list.get(i);
			}
		}
		return result;
	}
	
	/**
	 * 시즌 제외카테고리 등록/삭제 
	 * @param addList
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void exceptProc(String addList) throws Exception{
		String[] addArr = addList.split("\\|\\^");
		//테이블 초기화
		dao.deleteExcept();
		
		for(int i=0;i<addArr.length;i++){
			//키워드 등록
			dao.insertExcept(addArr[i], "Y");
			//키워드에 해당하는 카테고리 등록
			dao.insertExcept(addArr[i], "N");
		}
	}
	
	/**
	 * 개별 즉시적용하기 위한 캐시 API 호출 (시즌을 삭제 하는 경우)
	 * 
	 * @throws Exception
	 */
	private void applyCacheById(String seasonId, String app_tp) throws Exception {
		String param = "operation=D&season_id=" + seasonId + "&app_tp=" + app_tp;		// U : add/upate, D : delete 
		int timeout = Integer.parseInt(SmartUXProperties.getProperty("scheduler.timeout"));     
		int retrycnt = Integer.parseInt(SmartUXProperties.getProperty("scheduler.retrycnt"));
		//VideoLTE
		String url = SmartUXProperties.getProperty("SeasonDao.refreshSeasonById.CacheURL");
		String protocolName = SmartUXProperties.getProperty("SeasonDao.refreshSeason.protocol");
		
		this.applyCache(url, param, timeout, retrycnt, protocolName); 
	}
	
	@Override
	public void updateCacheTime(String seasonId)  throws Exception {
		dao.updateCacheTime(seasonId);
	}

}
